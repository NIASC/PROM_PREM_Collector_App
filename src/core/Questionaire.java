package core;

import implement.UserInterface;

public class Questionaire
{
	private UserHandle userHandle;
	private UserInterface userInterface;

	public Questionaire(UserInterface ui, UserHandle uh)
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
		System.out.println("Starting Questionaire");
	}
}
