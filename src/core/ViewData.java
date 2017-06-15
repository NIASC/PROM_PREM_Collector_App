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
			userInterface.displayError(
					"You must be logged in to view statistics.");
			return;
		}
		System.out.println("Viewing data");
	}
}
