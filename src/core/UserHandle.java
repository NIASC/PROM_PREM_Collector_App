package core;

import java.util.HashMap;

import implement.Database;
import implement.UserInterface;
import core.containers.User;

public class UserHandle
{
	public static final int USER_FOUND = 0x10;
	public static final int USER_NOT_FOUND = 0x20;
	public static final int DETAILS_MATCH = 0x40;
	public static final int DETAILS_MISMATCH = 0x80;
	
	private Database user_db;
	private UserInterface ui;
	private User user;
	
	private boolean loggedIn;
	
	public UserHandle(UserInterface ui)
	{
		this.ui = ui;
		user = null;
		loggedIn = false;
	}
	
	public void login()
	{
		if (loggedIn)
			return;
		user_db = new Database();
		HashMap<String, String> details = ui.requestLoginDetails("user", "pass");
		int ret = validateDetails(details.get("user"), details.get("pass"));
		System.out.printf("Logging in\n");
		initLoginVars();
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
		user_db.setPassword(user, oldPass, newPass, newSalt);
	}
	
	/**
	 * Attempts to match the supplied username and password with users that are
	 * already in the database.
	 * Bitwise masking can be use to mask out different return values (i.e.
	 * USER_FOUND | DETAILS_MATCH) is returns if the user is found and the password
	 * matches.
	 * 
	 * @param username The name of the user to look for.
	 * @param password The password of the user to look for.
	 * @return If the user was found in the database then USER_FOUND is returned.
	 * 		If the user was not found then USER_NOT_FOUND is returned.
	 * 		If the password matches with the user's then DETAILS_MATCH is returned.
	 * 		If the password did not match then DETAILS_MISMATCH is returned.
	 */
	private int validateDetails(String username, String password)
	{
		int ret = 0;
		user = findDetails(username);
		if (user == null)
			return USER_NOT_FOUND | DETAILS_MISMATCH;
		ret = USER_FOUND | (user.passwordMatch(password) ? DETAILS_MATCH : DETAILS_MISMATCH);
		return ret;
	}
	
	/**
	 * Query the database to look for the user with the supplied name and
	 * return the instance of that user.
	 * If the user was found it will be assigned t
	 * The instance of the user with the supplied username if it was found.
	 * 		If the user was not found or an error occured then null is returned.
	 * 
	 * @param username The name of the user to look for in the database.
	 * @return 
	 */
	private User findDetails(String username)
	{
		return user_db.getUser(username);
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
