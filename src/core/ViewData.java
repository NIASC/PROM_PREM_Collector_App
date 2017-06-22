package core;

import implement.UserInterface;

public class ViewData
{
	private UserHandle userHandle;
	private UserInterface userInterface;

	public ViewData(UserInterface ui, UserHandle uh)
	{
		userInterface = ui;
		userHandle = uh;
	}
	
	public void start()
	{
		if (!userHandle.isLoggedIn())
		{
			userInterface.displayError(Messages.error.getMessage(
					"NOT_LOGGED_IN", "en"));
			return;
		}
		System.out.println("Viewing data");
	}
}
