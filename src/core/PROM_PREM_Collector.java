package core;

import core.containers.Option;
import core.containers.OptionContainer;
import implement.*;

public class PROM_PREM_Collector
{
	private UserHandle userHandle;
	private Questionaire questionaire;
	private ViewData viewData;
	
	private UserInterface ui;

	/**
	 * Initializes class variables.
	 */
	public PROM_PREM_Collector()
	{
		ui = new UserInterface();
		userHandle = new UserHandle(ui);
		questionaire = new Questionaire(ui, userHandle);
		viewData = new ViewData(ui, userHandle);
	}

	/**
	 * The program loop. When this function returns the program
	 * has exited.
	 */
	public void start()
	{
		if (!Messages.loadMessages())
		{
			ui.displayError("Database error: unable to load messages.");
			System.exit(1);
		}
		boolean quit = false;
		do
		{
			quit = loginOptions();
			if (userHandle.isLoggedIn())
				loginActions();
		} while (!quit);
		ui.close();
	}
	
	/**
	 * Presents login options to the user and evaluates them.
	 * This function continues to present login options until the
	 * user successfully logs in or wants to exit the program.
	 * 
	 * @return True if the user wants to quit, False if user has
	 * 		successfully logged in.
	 */
	private boolean loginOptions()
	{
		while(!userHandle.isLoggedIn())
		{
			int response = ui.displayLoginScreen();
			switch (response)
			{
			case UserInterface_Interface.EXIT:
				return true;
			case UserInterface_Interface.LOGIN:
				userHandle.login();
				break;
			case UserInterface_Interface.REGISTER:
				userHandle.register();
				break;
			default:
				ui.displayError(Messages.errorMessages.getMessage(
						"UNKNOWN_RESPONSE", "en"));
				break;
			}
		}
		return false;
	}
	
	/**
	 * Presents available actions and executes them while logged in.
	 * If the user is not logged in then nothing is done.
	 */
	private void loginActions()
	{
		if (!userHandle.isLoggedIn())
			return;

		final int ERROR = 0, LOGOUT = 1, START_QUESTIONAIRE = 2, VIEW_DATA = 3;
		OptionContainer options = new OptionContainer();
		options.fill(new Option[] {
				new Option(START_QUESTIONAIRE, "Start questionaire."),
				new Option(VIEW_DATA, "View statistics (for this clinic)."),
				new Option(LOGOUT, "Log out.")
		});

		// present options and evaluate them
		while (userHandle.isLoggedIn())
		{
			options.setSelected(ui.selectOption(options));
			Option selected = options.getSelected();
			int response = selected != null ? selected.getIdentifier() : ERROR;
			switch (response)
			{
			case START_QUESTIONAIRE:
				questionaire.start();
				break;
			case VIEW_DATA:
				viewData.start();
				break;
			case LOGOUT:
				userHandle.logout();
				break;
			case ERROR:
				ui.displayError(Messages.errorMessages.getMessage(
						"NULL_SELECTED", "en"));
				break;
			default:
				ui.displayError(Messages.errorMessages.getMessage(
						"UNKNOWN_RESPONSE", "en"));
				break;
			}
		}
	}
}
