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
	
	private void initLoginVars()
	{
		loggedIn = true;
	}
	
	private void resetLoginVars()
	{
		loggedIn = false;
	}
}
