package core;

public class UserHandle
{
	private boolean loggedIn;

	public UserHandle()
	{
		loggedIn = false;
	}
	
	public void login()
	{
		if (loggedIn)
			return;
		initLoginVars();
	}
	
	public void register()
	{
		if (loggedIn)
			return;
	}
	
	public void logout()
	{
		if (!loggedIn)
			return;
		resetLoginVars();
	}
	
	public boolean isLoggedIn()
	{
		return loggedIn;
	}
	
	private void initLoginVars()
	{
		loggedIn = true;
	}
	
	private void resetLoginVars()
	{
		loggedIn = false;
	}
}
