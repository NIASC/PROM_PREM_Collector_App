/**
 * Copyright 2017 Marcus Malmquist
 * 
 * This file is part of PROM_PREM_Collector.
 * 
 * PROM_PREM_Collector is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * PROM_PREM_Collector is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PROM_PREM_Collector.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import core.containers.Form;
import core.containers.QuestionContainer;
import core.containers.StatisticsContainer;
import core.containers.StatisticsContainer.Statistics;
import core.containers.form.FieldContainer;
import core.containers.form.MultipleOptionContainer;
import core.containers.form.SingleOptionContainer;
import core.containers.form.SliderContainer;
import core.containers.form.TimePeriodContainer;
import core.interfaces.Implementations;
import core.interfaces.Messages;
import core.interfaces.Questions;
import core.interfaces.UserInterface;
import core.interfaces.UserInterface.RetFunContainer;

/**
 * This class is the central point for the data viewing part of the
 * program. This is where you can choose what type of data you want to
 * display and how you want to display it.
 * 
 * @author Marcus Malmquist
 *
 */
public class ViewData
{
	/* Public */
	
	/**
	 * Initialize variables.
	 * 
	 * @param ui The active instance of {@code UserInterface}.
	 * @param uh The active instance of {@code UserHandle}.
	 * 
	 * @see UserInterface
	 * @see UserHandle
	 */
	protected ViewData(UserInterface ui, UserHandle uh)
	{
		userInterface = ui;
		userHandle = uh;
	}
	
	/**
	 * Starts the data viewing loop, where the user can decide what
	 * to view and how to present the data.
	 */
	public void start()
	{
		if (!userHandle.isLoggedIn())
		{
			userInterface.displayError(Messages.getMessages().getError(
					Messages.ERROR_NOT_LOGGED_IN), false);
			return;
		}
		queryTimePeriod();
	}
	
	/* Protected */
	
	/* Private */
	private void queryTimePeriod()
	{
		Form form = new Form();
		TimePeriodContainer timeperiod = new TimePeriodContainer(false,
				"Select which period you want to see statistics for.", null);
		Implementations.Database().loadQResultDates(userHandle.getUser(), timeperiod);
		form.insert(timeperiod, Form.AT_END);
		
		QuestionContainer qc = Questions.getQuestions().getContainer();
		MultipleOptionContainer questionselect =
				new MultipleOptionContainer(false,
						"Select which questions you would like to view", null);
		
		for (int i = 0; i < qc.getSize(); ++i)
			questionselect.addOption(i, qc.getContainer(i).getStatement());
		form.insert(questionselect, Form.AT_END);

		form.jumpTo(Form.AT_BEGIN);

		userInterface.presentForm(form, this::validateSelection, false);
	}
	
	private RetFunContainer validateSelection(Form form)
	{
		RetFunContainer rfc = new RetFunContainer(this::displayStatistics);
		form.jumpTo(Form.AT_BEGIN);
		TimePeriodContainer timeperiod = (TimePeriodContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		MultipleOptionContainer questionselect = (MultipleOptionContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		
		Calendar[] bounds = timeperiod.getEntry();
		lower = bounds[0];
		upper = bounds[1];
		if (lower == null || upper == null)
		{
			rfc.message = "Invalid time period";
			return rfc;
		}
		if (timeperiod.getPeriodEntries() < 5)
		{
			rfc.message = "For privacy reasons the selected period must "
					+ "contain at leas 5 entries. Please extend the period.";
			return rfc;
		}
		
		// validate selected questions
		List<Integer> selQuestions = questionselect.getEntry();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.printf("Displaying statistics for %d entries for period [%s -- %s]\n",
				timeperiod.getPeriodEntries(), sdf.format(lower.getTime()),
				sdf.format(upper.getTime()));
		
		StatisticsContainer sc = new StatisticsContainer();
		System.out.printf("\n");
		Implementations.Database().loadQResults(userHandle.getUser(),
				lower, upper, selQuestions, sc);
		List<Statistics> res = sc.getStatistics();
		for (Statistics s : res)
		{
			List<Object> lstr = new ArrayList<Object>();
			List<Integer> lint = new ArrayList<Integer>();
			int tot = 0;
			
			Class<?> c = s.getQuestionClass();
			if (c.isAssignableFrom(SingleOptionContainer.class))
			{
				Map<Object, Integer> ans = s.getAnswerCounts();
				String statement;
				Integer count;
				for (Iterator<String> itr = s.getOptions().iterator(); itr.hasNext();)
				{
					statement = itr.next();
					count = ans.get(statement);
					lint.add(count);
					lstr.add(statement);
					tot += count;
				}
			}
			else if (c.isAssignableFrom(SliderContainer.class))
			{
				Map<Object, Integer> ans = s.getAnswerCounts();
				Integer count;
				for (int i = s.getLowerBound(); i <= s.getUpperBound(); ++i)
				{
					if ((count = ans.get(i)) == null)
						continue;
					lint.add(count);
					lstr.add(i);
					tot += count;
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s:\n", s.getStatement()));
			
			Iterator<Object> sitr;
			Iterator<Integer> iitr;
			for (sitr = lstr.iterator(), iitr = lint.iterator();
					sitr.hasNext() && iitr.hasNext();)
			{
				Integer i = iitr.next();
				sb.append(String.format("|- %4d (%3.0f%%) - %s\n",
						i, 100.0 * i.doubleValue() / tot, sitr.next()));
			}
			sb.append("|- ------------ -\n");
			sb.append(String.format("\\- %4d (%3.0f %%) - %s\n", tot, 100D, "Total"));
			System.out.println(sb.toString());
		}
		
		rfc.valid = true;
		return rfc;
	}
	
	private void displayStatistics()
	{
		
	}
	
	private UserHandle userHandle;
	private UserInterface userInterface;
	private Calendar upper, lower;
}
