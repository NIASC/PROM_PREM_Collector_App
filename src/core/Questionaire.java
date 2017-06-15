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
			userInterface.displayError(
					"You must be logged in to start the questionaire.");
			return;
		}
		System.out.println("Starting Questionaire");
	}
}
