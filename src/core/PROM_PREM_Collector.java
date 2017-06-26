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

import core.containers.form.SingleOptionContainer;
import implement.*;

/**
 * This is the main program that is the PROM/PREM Collector.
 * This purpose of this class is the be the central point in this
 * program. This is in a was the 'main menu', where you end up when
 * you start the program and where you quit the program and everything
 * in between.
 * 
 * @author Marcus Malmquist
 *
 */
public class PROM_PREM_Collector
{
	private UserHandle userHandle;
	private Questionaire questionaire;
	private ViewData viewData;
	
	private UserInterface ui;

	/**
	 * Initializes class variables.
	 */
	public PROM_PREM_Collector()
	{
		ui = new UserInterface();
		userHandle = new UserHandle(ui);
		questionaire = new Questionaire(ui, userHandle);
		viewData = new ViewData(ui, userHandle);
	}

	/**
	 * The program loop. When this function returns the program
	 * has exited.
	 */
	public void start()
	{
		if (!Messages.loadMessages())
		{
			ui.displayError("Database error: unable to load messages.");
			System.exit(1);
		}
		boolean quit = false;
		do
		{
			quit = loginOptions();
			if (userHandle.isLoggedIn())
				loginActions();
		} while (!quit);
		ui.close();
	}
	
	/**
	 * Presents login options to the user and evaluates them.
	 * This function continues to present login options until the
	 * user successfully logs in or wants to exit the program.
	 * 
	 * @return True if the user wants to quit, False if user has
	 * 		successfully logged in.
	 */
	private boolean loginOptions()
	{
		while(!userHandle.isLoggedIn())
		{
			int response = ui.displayLoginScreen();
			switch (response)
			{
			case UserInterface_Interface.EXIT:
				return true;
			case UserInterface_Interface.LOGIN:
				userHandle.login();
				break;
			case UserInterface_Interface.REGISTER:
				userHandle.register();
				break;
			default:
				ui.displayError(Messages.error.getMessage(
						"UNKNOWN_RESPONSE", "en"));
				break;
			}
		}
		return false;
	}
	
	/**
	 * Presents available actions and executes them while logged in.
	 * If the user is not logged in then nothing is done.
	 */
	private void loginActions()
	{
		if (!userHandle.isLoggedIn())
			return;

		final int ERROR = 0, LOGOUT = 1, START_QUESTIONAIRE = 2, VIEW_DATA = 3;
		SingleOptionContainer options = new SingleOptionContainer();
		options.addSOption(START_QUESTIONAIRE, "Start questionaire.");
		options.addSOption(VIEW_DATA, "View statistics (for this clinic).");
		options.addSOption(LOGOUT, "Log out.");
		
		// present options and evaluate them
		while (userHandle.isLoggedIn())
		{
			options.setSelected(ui.selectOption(options));
			Integer identifier = options.getSelected();
			int response = identifier != null ? identifier : ERROR;
			switch (response)
			{
			case START_QUESTIONAIRE:
				questionaire.start();
				break;
			case VIEW_DATA:
				viewData.start();
				break;
			case LOGOUT:
				userHandle.logout();
				break;
			case ERROR:
				ui.displayError(Messages.error.getMessage(
						"NULL_SELECTED", "en"));
				break;
			default:
				ui.displayError(Messages.error.getMessage(
						"UNKNOWN_RESPONSE", "en"));
				break;
			}
		}
	}
}
