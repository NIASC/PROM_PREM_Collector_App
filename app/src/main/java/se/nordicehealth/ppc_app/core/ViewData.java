/*! ViewData.java
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
package se.nordicehealth.ppc_app.core;

import java.util.Calendar;
import java.util.List;

import se.nordicehealth.ppc_app.core.containers.Form;
import se.nordicehealth.ppc_app.core.containers.QuestionContainer;
import se.nordicehealth.ppc_app.core.containers.StatisticsContainer;
import se.nordicehealth.ppc_app.core.containers.ViewDataContainer;
import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.TimePeriodContainer;
import se.nordicehealth.ppc_app.core.interfaces.FormUtils;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.Questions;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;

/**
 * This class is the central point for the data viewing part of the
 * program. This is where the users can choose what type of data they
 * want to display.
 * 
 * @author Marcus Malmquist
 *
 */
class ViewData
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
	ViewData(UserInterface ui, UserHandle uh)
	{
		this.ui = ui;
		this.uh = uh;
		dateSel = new DateSelection();
	}
	
	/**
	 * Starts the data viewing where the user can decide what data
	 * to view.
	 */
	void start()
	{
		if (!uh.isLoggedIn())
		{
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_NOT_LOGGED_IN), false);
			return;
		}
		dateSel.queryTimePeriod();
	}
	
	/* Protected */
	
	/* Private */
	
	private UserHandle uh;
	private UserInterface ui;
	private DateSelection dateSel;
	private ViewDataContainer vdc;
	
	private class DateSelection implements FormUtils
	{

		@Override
		public RetFunContainer ValidateUserInput(Form form) {
			Messages msg = Messages.getMessages();
			RetFunContainer rfc = new RetFunContainer();
			form.jumpTo(Form.AT_BEGIN);
			MultipleOptionContainer questionselect = (MultipleOptionContainer) form.currentEntry();
			form.jumpTo(Form.AT_NEXT);
			TimePeriodContainer timeperiod = (TimePeriodContainer) form.currentEntry();
			form.jumpTo(Form.AT_NEXT);
			
			List<Calendar> bounds = timeperiod.getEntry();
			Calendar lower = bounds.get(0);
			Calendar upper = bounds.get(1);
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
			int nEntries = timeperiod.getPeriodEntries();
			
			// validate selected questions not needed
			List<Integer> selQuestions = questionselect.getEntry();

			StatisticsContainer sc = new StatisticsContainer();
			Implementations.Database().loadQResults(uh.getUser(),
					lower, upper, selQuestions, sc);
			vdc = new ViewDataContainer(sc.getStatistics(), upper,
					lower, nEntries);
			
			rfc.valid = true;
			return rfc;
		}

		@Override
		public void callNext() {
			ui.presentViewData(vdc);
		}
		
		private DateSelection()
		{
			
		}
		
		/**
		 * Create a form that lets the user pick which questions to view and
		 * what time period to view them for.
		 */
		private void queryTimePeriod()
		{
			Messages msg = Messages.getMessages();
			Form form = new Form();
			QuestionContainer qc = Questions.getQuestions().getContainer();
			if (qc == null) {
				throw new AssertionError();
			}
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

			ui.presentForm(form, this, false);
		}
		
	}
}
