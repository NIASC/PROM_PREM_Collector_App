package core;

import java.util.HashMap;
import java.util.regex.Pattern;

import implement.Database;
import implement.Encryption;
import implement.UserInterface;
import core.containers.Form;
import core.containers.FormContainer;
import core.containers.User;

public class UserHandle
{
	public static final int USER_FOUND = 0x10;
	public static final int DETAILS_MATCH = 0x20;
	
	private Database user_db;
	private UserInterface ui;
	private User user;
	
	private boolean loggedIn;
	
	public UserHandle(UserInterface ui)
	{
		this.ui = ui;
		user_db = new Database();
		user = null;
		loggedIn = false;
	}
	
	public void login()
	{
		if (loggedIn)
			return;
		user_db.connect();
		HashMap<String, String> details = ui.requestLoginDetails("user", "pass");
		int ret = validateDetails(details.get("user"), details.get("pass"));
		if ((ret & (USER_FOUND | DETAILS_MATCH)) != (USER_FOUND | DETAILS_MATCH))
		{
			ui.displayError(Messages.errorMessages.getMessage(
					"UH_INVALID_LOGIN", Messages.LOCALE));
			// TODO: add functionality for resetting password.
			return;
		}
		initLoginVars();
		if (user.getUpdatePassword())
		{
			ui.displayMessage("Your account have been lagged for password update.");
			setPassword();
		}
		user_db.disconnect();
	}
	
	public void register()
	{
		if (loggedIn)
			return;
		System.out.printf("Registering\n");
	}
	
	public void logout()
	{
		if (!loggedIn)
			return;
		System.out.printf("Logging out\n");
		resetLoginVars();
	}
	
	public boolean isLoggedIn()
	{
		return loggedIn;
	}
	
	private void setPassword()
	{
		if (!loggedIn)
			return; // no user to set password for
		FormContainer fc = new FormContainer();
		fc.fill(new Form[] {
				new Form("Current password"),
				new Form("Enter new password"),
				new Form("Repeat new password")
		});
		final int cp = 0, np1 = 1, np2 = 2;
		HashMap<Integer, Form> formMap = null;
		boolean match = false;
		while (!match)
		{
			ui.displayForm(fc);
			formMap = fc.get();
			if (newPassError(
					formMap.get(cp).getValue(),
					formMap.get(np1).getValue(),
					formMap.get(np2).getValue()))
			{
				formMap.get(cp).setValue(null);
				formMap.get(np1).setValue(null);
				formMap.get(np2).setValue(null);
			}
			else
				match = true;
		}
		Encryption crypto = new Encryption();
		String newSalt = crypto.getNewSalt();
		User tmpUser = user_db.setPassword(user, formMap.get(cp).getValue(),
				crypto.hashString(formMap.get(np1).getValue(), newSalt),
				newSalt);
		if (tmpUser != null)
			user = tmpUser;
		else
			ui.displayError(Messages.DATABASE_ERROR);
	}
	
	private boolean newPassError(String oldPass, String newPass1, String newPass2)
	{
		if (!user.passwordMatch(oldPass))
		{
			ui.displayError(Messages.errorMessages.getMessage(
					"UH_PR_INVALID_CURRENT", Messages.LOCALE));
			return true;
		}
		if (!newPass1.equals(newPass2))
		{
			ui.displayError(Messages.errorMessages.getMessage(
					"UH_PR_MISMATCH_NEW", Messages.LOCALE));
			return true;
		}
		int ret = validatePassword(newPass1);
		if (ret < 0)
		{
			ui.displayError(Messages.errorMessages.getMessage(
					"UH_PR_INVALID_LENGTH", Messages.LOCALE));
			return true;
		}
		else if (ret == 0)
		{
			ui.displayError(Messages.errorMessages.getMessage(
					"UH_PR_PASSWORD_SIMPLE", Messages.LOCALE));
			return true;
		}
		return false;
	}
	
	/**
	 * Attempts to match the supplied username and password with users that are
	 * already in the database.
	 * Bitwise masking can be use to mask out different return values (i.e.
	 * USER_FOUND | DETAILS_MATCH) is returns if the user is found and the password
	 * matches.
	 * 
	 * Bitwise masks:
	 * USER_FOUND		- User was found.
	 * DETAILS_MATCH	- Entered password matches the user's password
	 * 					  in the database.
	 * 
	 * @param username The name of the user to look for.
	 * @param password The password of the user to look for.
	 * @return A bitwise masking of
	 */
	private int validateDetails(String username, String password)
	{
		user = findDetails(username);
		if (user == null)
			return 0;
		return USER_FOUND | (user.passwordMatch(password) ? DETAILS_MATCH : 0);
	}
	
	/**
	 * Query the database to look for the user with the supplied name and
	 * return the instance of that user.
	 * 
	 * 
	 * @param username The name of the user to look for in the database.
	 * @return The instance of the user with the supplied username if it was found.
	 * 		If the user was not found or an error occurred then null is returned.
	 */
	private User findDetails(String username)
	{
		return user_db.getUser(username);
	}
	
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
	 * Initializes variables that are useful during the time the user is logged in.
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
