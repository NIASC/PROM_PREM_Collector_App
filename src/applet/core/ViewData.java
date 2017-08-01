/** ViewData.java
 * 
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
package applet.core;

import java.util.Calendar;
import java.util.List;

import applet.core.containers.Form;
import applet.core.containers.QuestionContainer;
import applet.core.containers.StatisticsContainer;
import applet.core.containers.ViewDataContainer;
import applet.core.containers.form.MultipleOptionContainer;
import applet.core.containers.form.TimePeriodContainer;
import applet.core.interfaces.Implementations;
import applet.core.interfaces.Messages;
import applet.core.interfaces.Questions;
import applet.core.interfaces.UserInterface;
import applet.core.interfaces.UserInterface.RetFunContainer;

/**
 * This class is the central point for the data viewing part of the
 * program. This is where the users can choose what type of data they
 * want to display.
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
		this.ui = ui;
		this.uh = uh;
	}
	
	/**
	 * Starts the data viewing where the user can decide what data
	 * to view.
	 */
	public void start()
	{
		if (!uh.isLoggedIn())
		{
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_NOT_LOGGED_IN), false);
			return;
		}
		queryTimePeriod();
	}
	
	/* Protected */
	
	/* Private */
	
	/**
	 * Create a form that lets the user pick which questions to view and
	 * what time period to view them for.
	 */
	private void queryTimePeriod()
	{
		Messages msg = Messages.getMessages();
		Form form = new Form();
		QuestionContainer qc = Questions.getQuestions().getContainer();
		MultipleOptionContainer questionselect =
				new MultipleOptionContainer(false, msg.getInfo(
						Messages.INFO_VD_SELECT_PREIOD), null);
		
		for (int i = 0; i < qc.getSize(); ++i)
			questionselect.addOption(i, qc.getContainer(i).getStatement());
		form.insert(questionselect, Form.AT_END);

		TimePeriodContainer timeperiod =
				new TimePeriodContainer(false, msg.getInfo(
						Messages.INFO_VD_SELECT_QUESTIONS), null);
		Implementations.Database().loadQResultDates(uh.getUser(), timeperiod);
		form.insert(timeperiod, Form.AT_END);
		
		form.jumpTo(Form.AT_BEGIN);

		ui.presentForm(form, this::validateSelection, false);
	}
	
	/**
	 * Validates the user entry for the time period and questions.
	 * 
	 * @param form The form that has been filled in by the user.
	 * 
	 * @return The {@code RetFunContainer} that contains information
	 * 		about the validation process.
	 * 
	 * @see RetFunContainer
	 */
	private RetFunContainer validateSelection(Form form)
	{
		Messages msg = Messages.getMessages();
		RetFunContainer rfc = new RetFunContainer(this::displayStatistics);
		form.jumpTo(Form.AT_BEGIN);
		MultipleOptionContainer questionselect = (MultipleOptionContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		TimePeriodContainer timeperiod = (TimePeriodContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		
		List<Calendar> bounds = timeperiod.getEntry();
		lower = bounds.get(0);
		upper = bounds.get(1);
		if (lower == null || upper == null)
		{
			rfc.message = msg.getError(
					Messages.ERROR_VD_INVALID_PERIOD);
			return rfc;
		}
		if (timeperiod.getPeriodEntries() < 5)
		{
			rfc.message = msg.getError(
					Messages.ERROR_VD_FEW_ENTRIES);
			return rfc;
		}
		nEntries = timeperiod.getPeriodEntries();
		
		// validate selected questions not needed
		selQuestions = questionselect.getEntry();

		StatisticsContainer sc = new StatisticsContainer();
		Implementations.Database().loadQResults(uh.getUser(),
				lower, upper, selQuestions, sc);
		vdc = new ViewDataContainer(sc.getStatistics(), upper,
				lower, nEntries);
		
		rfc.valid = true;
		return rfc;
	}
	
	/**
	 * Displays the statistics. This method should be replaced by a
	 * method to properly display statistics to the user.
	 */
	private void displayStatistics()
	{
		ui.presentViewData(vdc);
	}
	
	private UserHandle uh;
	private UserInterface ui;
	private Calendar upper, lower;
	private int nEntries;
	private List<Integer> selQuestions;
	private ViewDataContainer vdc;
}
