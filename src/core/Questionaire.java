package core;

import implement.Messages;
import implement.UserInterface;

/**
 * This class is the central point for the questionaire part of the
 * program. This is where the questionare loop is run from.
 * 
 * @author Marcus Malmquist
 *
 */
public class Questionaire
{
	private UserHandle userHandle;
	private UserInterface userInterface;

	/**
	 * Initialize variables.
	 * 
	 * @param ui The active instance of the user interface.
	 * @param uh The active instance of the user handle.
	 */
	public Questionaire(UserInterface ui, UserHandle uh)
	{
		userInterface = ui;
		userHandle = uh;
	}
	
	/**
	 * Starts the questionaire.
	 */
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
