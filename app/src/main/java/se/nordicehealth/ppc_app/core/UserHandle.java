/*! UserHandle.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import se.nordicehealth.ppc_app.core.containers.Form;
import se.nordicehealth.ppc_app.core.containers.User;
import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.interfaces.Database;
import se.nordicehealth.ppc_app.core.interfaces.Encryption;
import se.nordicehealth.ppc_app.core.interfaces.FormUtils;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.Registration;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.common.implementation.Constants;

/**
 * This class handles the user. This mostly means handling the login,
 * logout and redirecting to the registration form as well as
 * redirecting to the different applications that are available to
 * users that have logged in.
 * 
 * @author Marcus Malmquist
 *
 */
public class UserHandle
{
	/* Public */
	
	/**
	 * Initialize variables.
	 * 
	 * @param ui The active instance of the {@code UserInterface}.
	 * 
	 * @see UserInterface
	 */
	public UserHandle(UserInterface ui)
	{
		this.ui = ui;
		db = Implementations.Database();
		viewData = new ViewData(ui, this);
		updatePass = new UpdatePassword();
		user = null;
		loggedIn = false;
	}
	
	/**
	 * Attempts to match the supplied user details with data in the
	 * database. If the user successfully logs in it will be flagged
	 * as logged in (which can be used to verify that the user has
	 * logged in using the function isLoggedIn). This flag prevents
	 * methods only available when logged in from being called if
	 * the user has logged out.
	 * 
	 * @param username The username of the user that wants to log in.
	 * @param password The (unhashed) password of the user that wants
	 * 		to log in.
	 */
	public void login(String username, String password)
	{
		Database.Session session = db.requestLogin(username, password);
		switch(session.response)
		{
		case Constants.SUCCESS:
			user = db.getUser(username);
            update_password = session.update_password;
			initLoginVars();
            questionnaire = new Questionnaire(ui, this);
			break;
		case Constants.ALREADY_ONLINE:
			ui.displayError(Implementations.Messages().getError(
					Messages.ERROR_UH_ALREADY_ONLINE), false);
			break;
		case Constants.SERVER_FULL:
			ui.displayError(Implementations.Messages().getError(
					Messages.ERROR_UH_SERVER_FULL), false);
			break;
		case Constants.INVALID_DETAILS:
			ui.displayError(Implementations.Messages().getError(
					Messages.ERROR_UH_INVALID_LOGIN), false);
			break;
		default:
			ui.displayError(Implementations.Messages().getError(
					Messages.ERROR_UNKNOWN_RESPONSE), false);
			break;
		}
	}
	
	/**
	 * Presents a registration form that the user must fill in and
	 * sends the request to an administrator who can add the user if
	 * it is authorized.
	 */
	public void register()
	{
		if (loggedIn)
			return;
		Registration register = Implementations.Registration(ui);
		register.registrationProcess();
	}
	
	/**
	 * Resets any variables that was initialized during login (in
	 * particular it unflags the user as logged in).
	 */
	public void logout()
	{
		if (!loggedIn)
			return;
		db.requestLogout(user.getUsername());
		resetLoginVars();
	}
	
	/**
	 * Starts the questionnaire.
	 */
	public void startQuestionnaire()
	{
		if (!loggedIn)
			return;
		questionnaire.start();
	}
	
	/**
	 * Starts the data viewing.
	 */
	public void viewData()
	{
		if (!loggedIn)
			return;
		viewData.start();
	}
	
	/**
	 * Returns the 'logged in' flag.
	 * 
	 * @return {@code true} if the user is logged in. {@code false} if not.
	 */
	public boolean isLoggedIn()
	{
		return loggedIn;
	}
	
	/**
	 * If the user has/wants to update his password then this method
	 * will call the part of program that displays that form.
	 * 
	 * @return {@code true} if the user needed to update its password.
	 * 		{@code false} if not.
	 */
	public boolean updatePassword()
	{
		if (update_password)
		{
			ui.displayMessage(Implementations.Messages().getInfo(
					Messages.INFO_UH_UPDATE_PASSWORD), true);
			updatePass.setPassword();
			return true;
		}
		return false;
	}
	
	/* Protected */
	
	/**
	 * Retrieves this handle's active {@code User}.
	 * 
	 * @return This handle's active {@code User}. If this handle does
	 * 		not have an active user then {@code null} is returned.
	 * 
	 * @see User
	 */
	protected User getUser()
	{
		return (User) user.clone();
	}
	
	/* Private */
	
	private Database db;
	private UserInterface ui;
	private Questionnaire questionnaire;
	private ViewData viewData;
	private UpdatePassword updatePass;
	private User user;
	private boolean loggedIn;
    private boolean update_password;

	private boolean newPassError(int response)
	{
		switch(response) {
			case Constants.INVALID_DETAILS:
				ui.displayError(Implementations.Messages().getError(
						Messages.ERROR_UH_PR_INVALID_CURRENT), false);
				return true;
			case Constants.MISMATCH_NEW:
				ui.displayError(Implementations.Messages().getError(
						Messages.ERROR_UH_PR_MISMATCH_NEW), false);
				return true;
			case Constants.PASSWORD_SHORT:
				ui.displayError(Implementations.Messages().getError(
						Messages.ERROR_UH_PR_INVALID_LENGTH), false);
				return true;
            case Constants.PASSWORD_SIMPLE:
				ui.displayError(Implementations.Messages().getError(
						Messages.ERROR_UH_PR_PASSWORD_SIMPLE), false);
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * Initializes variables that are useful during the time the user
	 * is logged in.
	 */
	private void initLoginVars()
	{
		loggedIn = true;
	}
	
	/**
	 * Resets any variable that was initialized through initLoginVars
	 */
	private void resetLoginVars()
	{
		loggedIn = false;
		user = null;
	}
	
	private class UpdatePassword implements FormUtils
	{

		@Override
		public RetFunContainer ValidateUserInput(Form form) {
			RetFunContainer rfc = new RetFunContainer();
			List<String> answers = new ArrayList<>();
			form.jumpTo(Form.AT_BEGIN);
			do
				answers.add((String) form.currentEntry().getEntry());
			while (form.nextEntry() != null);
			String current = answers.get(0);
			String new1 = answers.get(1);
			String new2 = answers.get(2);

            int response = db.setPassword(user.getUsername(), current, new1, new2);
			if (!newPassError(response))
                rfc.valid = true;
			return rfc;
		}

		@Override
		public void callNext()
		{
			
		}
		
		private UpdatePassword()
		{
			
		}
		
		/**
		 * Query the user for current password and new password if the user
		 * is logged in. If the old password is correct and the password
		 * matches the valid passwords criterion.
		 * This function will query the user until the old and new password
		 * fulfill the criterion described above.
		 */
		private void setPassword()
		{
			if (!loggedIn)
				return; // no user to set password for

			Form form = new Form();
			form.insert(new FieldContainer(false, true, Implementations.Messages().getInfo(
			        Messages.INFO_CURRENT_PASSWORD), null), Form.AT_END);
			form.insert(new FieldContainer(false, true, Implementations.Messages().getInfo(
			        Messages.INFO_NEW_PASSWORD), null), Form.AT_END);
			form.insert(new FieldContainer(false, true, Implementations.Messages().getInfo(
                    Messages.INFO_RE_NEW_PASSWORD), null), Form.AT_END);
			form.jumpTo(Form.AT_BEGIN);
			
			ui.displayMessage(Implementations.Messages().getInfo(
					Messages.INFO_NEW_PASS_INFO), false);
			ui.presentForm(form, this, true);
		}
	}
}
