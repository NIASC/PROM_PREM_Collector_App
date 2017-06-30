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

import java.util.regex.Pattern;

import core.containers.Form;
import core.containers.User;
import core.containers.form.FieldContainer;
import core.interfaces.Database;
import core.interfaces.Encryption;
import core.interfaces.Implementations;
import core.interfaces.Messages;
import core.interfaces.Registration;
import core.interfaces.UserInterface;

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
	 * @param ui The active instance of the user interface.
	 */
	public UserHandle(UserInterface ui)
	{
		this.ui = ui;
		user_db = Implementations.Database();
		questionnaire = new Questionnaire(ui, this);
		viewData = new ViewData(ui, this);
		user = null;
		loggedIn = false;
	}
	
	/**
	 * Attempts to match the supplied user deatils with data in the
	 * database. If the user successfully logs in it will be flagged
	 * as logged in (which can be used to verify that the user has
	 * logged in using the function isLoggedIn). This flag prevents
	 * the same user from being logged in on the same account in
	 * multiple instances.
	 */
	public void login(String username, String password)
	{
		if (!validateDetails(username, password))
		{
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_UH_INVALID_LOGIN));
			// TODO: add functionality for resetting password.
			return;
		}
		switch(UserManager.getUserManager().addUser(this))
		{
		case UserManager.SUCCESS:
			initLoginVars();
			break;
		case UserManager.ALREADY_ONLINE:
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_UH_ALREADY_ONLINE));
			break;
		case UserManager.SERVER_FULL:
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_UH_SERVER_FULL));
			break;
		default:
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_UNKNOWN_RESPONSE));
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
		resetLoginVars();
		UserManager.getUserManager().delUser(this);
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
	 * @return True if the user is logged in. False if not.
	 */
	public boolean isLoggedIn()
	{
		return loggedIn;
	}
	
	/**
	 * If the user has/wants to update his password then this method
	 * will call the part of program that displays that form.
	 * 
	 * @return True if the user needed to update its password. False
	 * 		if not.
	 */
	public boolean updatePassword()
	{
		if (user.getUpdatePassword())
		{
			ui.displayMessage(Messages.getMessages().getInfo(
					Messages.INFO_UH_UPDATE_PASSWORD));
			setPassword();
			return true;
		}
		return false;
	}
	
	/**
	 * Query the user for current password and new password if the user
	 * is logged in. If the old password is correct and the password
	 * matches the valid passwords criterion.
	 * This function will query the user until the old and new password
	 * fulfill the criterion described above.
	 */
	public void setPassword()
	{
		if (!loggedIn)
			return; // no user to set password for

		Form form = new Form();
		form.insert(new FieldContainer(false, true,
				Messages.getMessages().getInfo(
						Messages.INFO_CURRENT_PASSWORD)),
				Form.AT_END);
		form.insert(new FieldContainer(false, true,
				Messages.getMessages().getInfo(
						Messages.INFO_NEW_PASSWORD)),
				Form.AT_END);
		form.insert(new FieldContainer(false, true,
				Messages.getMessages().getInfo(
						Messages.INFO_RE_NEW_PASSWORD)),
				Form.AT_END);
		form.jumpTo(Form.AT_BEGIN);
		
		ui.displayMessage(Messages.getMessages().getInfo(
				Messages.INFO_NEW_PASS_INFO));
		ui.presentForm(form, this::setPassReturn);
	}
	
	/* Protected */
	
	/**
	 * Attempts to match the supplied username and password with users
	 * that are already in the database. If the username and password
	 * matches the user information will be placed in the local user
	 * object.
	 * 
	 * @param username The name of the user to look for.
	 * @param password The password of the user to look for.
	 * 
	 * @return True if the user was found and the details matched.
	 * 		False if not.
	 */
	protected boolean validateDetails(String username, String password)
	{
		User tmp = user_db.getUser(username);
		if (tmp == null)
			return false;
		if (!tmp.passwordMatch(password))
			return false;
		user = tmp;
		return true;
	}
	
	/**
	 * 
	 * @return This handle's active user. If this handle does not have
	 * 		an active user then null is returned.
	 */
	protected User getUser()
	{
		return user;
	}
	
	/* Private */
	
	private Database user_db;
	private UserInterface ui;
	private Questionnaire questionnaire;
	private ViewData viewData;
	private User user;
	private boolean loggedIn;
	
	/**
	 * The function to return to after the user have entered a new
	 * password.
	 * 
	 * @param form The form that was sent to the UI.
	 * 
	 * @return True if the form was sent. False if not.
	 */
	private boolean setPassReturn(Form form)
	{
		form.jumpTo(Form.AT_BEGIN);
		FieldContainer current = (FieldContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		FieldContainer new1 = (FieldContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);
		FieldContainer new2 = (FieldContainer) form.currentEntry();
		form.jumpTo(Form.AT_NEXT);

		if (newPassError(current.getEntry(), new1.getEntry(), new2.getEntry()))
			return false;
		
		Encryption crypto = Implementations.Encryption();
		String newSalt = crypto.getNewSalt();
		User tmpUser = user_db.setPassword(user, current.getEntry(),
				crypto.hashString(new1.getEntry(), newSalt), newSalt);
		if (tmpUser == null)
		{
			ui.displayError(Messages.DATABASE_ERROR);
			return false;
		}
		user = tmpUser;
		return true;
	}
	
	/**
	 * Attempts to find any errors in the supplied passwords.
	 * The errors it looks for are:
	 * Old/current password must match the user's current password.
	 * The two new passwords must be the same.
	 * The new password must fit the length constraint.
	 * The new password must fit the strength constraint.
	 * 
	 * @param oldPass The unhashed old/current password.
	 * @param newPass1 The first entry of the new unhashed password.
	 * @param newPass2 The second entry of the new unhashed password.
	 *  
	 * @return True if an error occurred (see description above). False
	 * 		if no errors were found.
	 */
	private boolean newPassError(
			String oldPass, String newPass1, String newPass2)
	{
		if (!user.passwordMatch(oldPass))
		{
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_UH_PR_INVALID_CURRENT));
			return true;
		}
		if (!newPass1.equals(newPass2))
		{
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_UH_PR_MISMATCH_NEW));
			return true;
		}
		int ret = validatePassword(newPass1);
		if (ret < 0)
		{
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_UH_PR_INVALID_LENGTH));
			return true;
		}
		else if (ret == 0)
		{
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_UH_PR_PASSWORD_SIMPLE));
			return true;
		}
		return false;
	}
	
	/**
	 * Validates the password according to the security criterion.
	 * This should typically be done when the user wants to set a new
	 * password.
	 * The criterion are:
	 * password length: >5 and < 33 characters.
	 * password strength: contain characters from at least 2 groups
	 * 		out of lowercase, uppercase, digits, punctuation
	 * 		(including space).
	 * 
	 * @param password The password to validate.
	 * 
	 * @return <0 if the password does not fit the length constraint.
	 * 		0 if the password does not fit the strength constraint.
	 * 		>0 if the password fits the length constraint and strength
	 * 		constraint.
	 */
	private int validatePassword(String password)
	{
		if (password.length() < 6 || password.length() > 32)
			return -1;
		Pattern[] pattern = new Pattern[]{
				Pattern.compile("\\p{Lower}"), /* lowercase */
				Pattern.compile("\\p{Upper}"), /* uppercase */
				Pattern.compile("\\p{Digit}"), /* digits */
				Pattern.compile("[\\p{Punct} ]") /* punctuation and space */
		};
		if (Pattern.compile("[^\\p{Print}]").matcher(password).find())
		{ // expression contains non-ascii or non-printable characters
			return 0;
		}
		int points = 0;
		if (pattern[0].matcher(password).find())
			++points;
		if (pattern[1].matcher(password).find())
			++points;
		if (pattern[2].matcher(password).find())
			++points;
		if (pattern[3].matcher(password).find())
			++points;
		return points - 1;
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
}
