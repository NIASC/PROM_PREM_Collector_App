package core;

import core.containers.Option;
import core.containers.OptionContainer;
import implement.*;

public class PROM_PREM_Collector
{
	private UserHandle userHandle;
	private Questionaire questionaire;
	private ViewData viewData;
	private final int LOGOUT = 0;
	private final int START_QUESTIONAIRE = 1;
	private final int VIEW_DATA = 2;
	
	private UserInterface ui;

	public PROM_PREM_Collector()
	{
		ui = new UserInterface();
		
		userHandle = new UserHandle();
		questionaire = new Questionaire(ui, userHandle);
		viewData = new ViewData(ui, userHandle);
	}

	public void start()
	{
		while(!userHandle.isLoggedIn())
		{
			int response = ui.displayLoginScreen();
			switch (response)
			{
			case UserInterface_Interface.EXIT:
				System.exit(0);
			case UserInterface_Interface.LOGIN:
				userHandle.login();
				break;
			case UserInterface_Interface.REGISTER:
				userHandle.register();
				break;
			default:
				ui.displayError("Unknown option " + response
						+ ".");
				break;
			}
		}

		OptionContainer options = new OptionContainer();
		options.fill(new Option[]
				{
						new Option(START_QUESTIONAIRE, "Start questionaire."),
						new Option(VIEW_DATA, "View statistics for this clinic."),
						new Option(LOGOUT, "Log out.")
				});
		boolean running = true;
		while (running)
		{
			int response = ui.selectOption(options);
			switch (response)
			{
			case LOGOUT:
				userHandle.logout();
				running = false;
				break;
			case START_QUESTIONAIRE:
				questionaire.start();
				break;
			case VIEW_DATA:
				viewData.start();
				break;
			default:
				ui.displayError("Unknown option " + response
						+ ".");
				break;
			}
		}
		ui.close();
	}
}
