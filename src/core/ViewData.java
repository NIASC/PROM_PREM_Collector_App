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
import java.util.Date;
import java.util.GregorianCalendar;

import core.containers.Form;
import core.containers.form.FieldContainer;
import core.containers.form.TimePeriodContainer;
import core.interfaces.Implementations;
import core.interfaces.Messages;
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
				"Select which period you want to see statistics for.");
		Implementations.Database().loadQResultDates(userHandle.getUser(), timeperiod);
		form.insert(timeperiod, Form.AT_END);
		// select which questions, should be multiple-choice

		form.jumpTo(Form.AT_BEGIN);

		userInterface.presentForm(form, this::validateSelection, false);
	}
	
	private RetFunContainer validateSelection(Form form)
	{
		RetFunContainer rfc = new RetFunContainer(this::displayStatistics);
		form.jumpTo(Form.AT_BEGIN);
		TimePeriodContainer timeperiod = (TimePeriodContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		// get selected questions entry
		
		GregorianCalendar[] bounds = timeperiod.getEntry();
		lower = bounds[0];
		upper = bounds[1];
		if (lower == null || upper == null)
		{
			rfc.message = "Invalid time period";
			return rfc;
		}
		// validate selected questions

		System.out.printf("Number of entries for selected date: %d\n",
				timeperiod.getDateCount());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.printf("Selected lower bound: %s\n",
				sdf.format(lower.getTime()));
		System.out.printf("Selected upper bound: %s\n",
				sdf.format(upper.getTime()));
		rfc.valid = true;
		return rfc;
	}
	
	private void displayStatistics()
	{
		
	}
	
	private UserHandle userHandle;
	private UserInterface userInterface;
	private GregorianCalendar upper, lower;
}
