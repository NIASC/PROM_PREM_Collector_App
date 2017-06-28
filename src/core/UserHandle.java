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
 * logout and redirecting to the registration form.
 * The purpose of this class is to handle user-related processes.
 * 
 * @author Marcus Malmquist
 *
 */
public class UserHandle
{
	public static final int USER_FOUND = 0x10;
	public static final int DETAILS_MATCH = 0x20;
	
	private Database user_db;
	private UserInterface ui;
	private User user;
	
	private boolean loggedIn;
	
	/**
	 * Initialize variables.
	 * 
	 * @param ui The active instance of the user interface.
	 */
	public UserHandle(UserInterface ui)
	{
		this.ui = ui;
		user_db = Implementations.Database();
		user = null;
		loggedIn = false;
	}
	
	/**
	 * Prompts the user for login details and attempts to match them
	 * with data in the database.
	 * If the user successfully logs in it will be flagged as logged in
	 * (which can be used to verify that the user has logged in using
	 * the function isLoggedIn).
	 */
	public void login()
	{
		if (loggedIn)
			return;
		user_db.connect();
		Form f = new Form();
		FieldContainer usrnam = new FieldContainer(
				Messages.getMessages().getInfo(
						Messages.INFO_UH_ENTER_USERNAME));
		f.insert(usrnam, Form.AT_END);
		FieldContainer pswrd = new FieldContainer(
				Messages.getMessages().getInfo(
						Messages.INFO_UH_ENTER_PASSWORD));
		f.insert(pswrd, Form.AT_END);
		f.jumpTo(Form.AT_BEGIN);
		
		if (!ui.presentForm(f))
			return;
		
		int ret = validateDetails(usrnam.getEntry(), pswrd.getEntry());
		if ((ret & (USER_FOUND | DETAILS_MATCH)) != (USER_FOUND | DETAILS_MATCH))
		{
			ui.displayError(Messages.getMessages().getError(
					Messages.ERROR_UH_INVALID_LOGIN));
			// TODO: add functionality for resetting password.
			return;
		}
		initLoginVars();
		if (user.getUpdatePassword())
		{
			ui.displayMessage(Messages.getMessages().getInfo(
					Messages.INFO_UH_UPDATE_PASSWORD));
			setPassword();
		}
		user_db.disconnect();
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
	 * particular it resets the 'logged in' flag).
	 */
	public void logout()
	{
		if (!loggedIn)
			return;
		resetLoginVars();
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

		final String CP_MSG = Messages.getMessages().getInfo(
				Messages.INFO_CURRENT_PASSWORD);
		final String NP1_MSG = Messages.getMessages().getInfo(
				Messages.INFO_NEW_PASSWORD);
		final String NP2_MSG = Messages.getMessages().getInfo(
				Messages.INFO_RE_NEW_PASSWORD);
		
		
		Form form = new Form();
		FieldContainer currentPassword = new FieldContainer(CP_MSG);
		form.insert(currentPassword, Form.AT_END);
		FieldContainer newPassword1 = new FieldContainer(NP1_MSG);
		form.insert(newPassword1, Form.AT_END);
		FieldContainer newPassword2 = new FieldContainer(NP2_MSG);
		form.insert(newPassword2, Form.AT_END);
		form.jumpTo(Form.AT_BEGIN);
		
		boolean match = false;
		while (!match)
		{
			ui.displayMessage(Messages.getMessages().getInfo(
					Messages.INFO_NEW_PASS_INFO));
			if (!ui.presentForm(form))
				return;
			if (newPassError(
					currentPassword.getEntry(),
					newPassword1.getEntry(),
					newPassword2.getEntry()))
			{
				currentPassword.setEntry(null);
				newPassword1.setEntry(null);
				newPassword2.setEntry(null);
			}
			else
				match = true;
		}
		Encryption crypto = Implementations.Encryption();
		String newSalt = crypto.getNewSalt();
		User tmpUser = user_db.setPassword(
				user, currentPassword.getEntry(),
				crypto.hashString(
						newPassword1.getEntry(), newSalt),
				newSalt);
		if (tmpUser != null)
			user = tmpUser;
		else
			ui.displayError(Messages.DATABASE_ERROR);
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
	 * Attempts to match the supplied username and password with users
	 * that are already in the database. If the username and password
	 * matches the user information will be placed in the local user
	 * object.
	 * Bitwise masking can be use to mask out different return values
	 * (i.e. USER_FOUND | DETAILS_MATCH) is returned if the user is
	 * found and the password matches.
	 * 
	 * Bitwise masks:
	 * USER_FOUND		- User was found.
	 * DETAILS_MATCH	- Entered password matches the user's password
	 * 					  in the database.
	 * 
	 * @param username The name of the user to look for.
	 * @param password The password of the user to look for.
	 * @return A bitwise masking of USER_FOUND and DETAILS_MATCH
	 */
	private int validateDetails(String username, String password)
	{
		User tmp = findDetails(username);
		if (tmp == null)
			return 0;
		if (!tmp.passwordMatch(password))
			return 0;
		// username and password matches if we reach this point
		user = tmp;
		return USER_FOUND | DETAILS_MATCH;
	}
	
	/**
	 * Query the database to look for the user with the supplied name
	 * and return the instance of that user.
	 * 
	 * @param username The name of the user to look for in the
	 * 		database.
	 * @return The instance of the user with the supplied username if
	 * 		it was found. If the user was not found or an error
	 * 		occurred then null is returned.
	 */
	private User findDetails(String username)
	{
		return user_db.getUser(username);
	}
	
	/**
	 * Validates the password according to the security criterion.
	 * This should typically be done when the user wants to set a new
	 * password.
	 * The criterion are:
	 * password length: >5 and < 33 characters.
	 * password strength: contain characters from at least 2 groups
	 * 		out of [a-z], [A-Z], [0-9], [^a-zA-Z0-9] (symbols).
	 * 
	 * @param password The password to validate.
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
				Pattern.compile("[a-z]"),
				Pattern.compile("[A-Z]"),
				Pattern.compile("[0-9]"),
				Pattern.compile("[^a-zA-Z0-9]")
		};
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
	 * Resets any variable that was initialized through initLoginVars()
	 */
	private void resetLoginVars()
	{
		loggedIn = false;
	}
}
