package se.nordicehealth.ppc_app.core;

import java.util.LinkedList;
import java.util.List;

import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;
import se.nordicehealth.ppc_app.core.interfaces.Server;
import se.nordicehealth.ppc_app.core.interfaces.FormControl;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.Registration;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.common.implementation.Constants;

public class UserHandle
{
	public UserHandle(UserInterface ui)
	{
		this.ui = ui;
        msg = Implementations.Messages();
		db = Implementations.Server();
		viewData = new ViewData(ui, this);
		updatePass = new PasswordUpdate();
        resetLoginVars();
	}

	public void login(String username, String password)
	{
		Server.Session session = db.requestLogin(username, password);
		switch(session.response) {
            case Constants.SUCCESS:
                initLoginVars(new User(session.uid, session.update_password, true));
                questionnaire = new Questionnaire(ui, this);
                break;
            case Constants.ALREADY_ONLINE:
                ui.displayError(msg.error(Messages.ERROR.UH_ALREADY_ONLINE), false);
                break;
            case Constants.SERVER_FULL:
                ui.displayError(msg.error(Messages.ERROR.UH_SERVER_FULL), false);
                break;
            case Constants.INVALID_DETAILS:
                ui.displayError(msg.error(Messages.ERROR.UH_INVALID_LOGIN), false);
                break;
            default:
                ui.displayError(msg.error(Messages.ERROR.UNKNOWN_RESPONSE), false);
                break;
		}
	}

	public void registration()
	{
		Registration register = Implementations.Registration(ui);
		register.registrationProcess();
	}

	public void logout()
	{
		db.requestLogout(user.uid);
		resetLoginVars();
	}

	public void startQuestionnaire()
	{
		questionnaire.start();
	}

	public void viewData()
	{
		viewData.start();
	}

	public boolean isLoggedIn()
	{
		return user.loggedIn;
	}

	public void updatePassword()
	{
		if (user.updatePassword) {
			ui.displayMessage(msg.info(Messages.INFO.UH_UPDATE_PASSWORD), true);
			updatePass.createForm();
		}
	}

    long getUID()
    {
        return user.uid;
    }

	private Messages msg;
	private Server db;
	private UserInterface ui;
	private Questionnaire questionnaire;
	private ViewData viewData;
	private PasswordUpdate updatePass;
    private volatile User user;
	private volatile boolean running;
	private Thread monitor;

	private String newPassError(int response)
	{
		switch(response) {
            case Constants.SUCCESS:
                return null;
            case Constants.INVALID_DETAILS:
				return msg.error(Messages.ERROR.UH_PR_INVALID_CURRENT);
			case Constants.MISMATCH_NEW:
				return msg.error(Messages.ERROR.UH_PR_MISMATCH_NEW);
			case Constants.PASSWORD_SHORT:
				return msg.error(Messages.ERROR.UH_PR_INVALID_LENGTH);
            case Constants.PASSWORD_SIMPLE:
				return msg.error(Messages.ERROR.UH_PR_PASSWORD_SIMPLE);
			default:
				return msg.error(Messages.ERROR.UNKNOWN_RESPONSE);
		}
	}

	private void initLoginVars(User user)
	{
        this.user = user;
		running = true;
		monitor = new Thread(new PingServer());
		monitor.start();
	}

	private void resetLoginVars()
	{
		running = false;
		if (monitor != null && monitor.isAlive()) {
			try {
				monitor.join(0);
			} catch (InterruptedException ignored) { }
            monitor = null;
		}
		user = new User(0L, false, false);
	}
	
	private class PasswordUpdate implements FormControl
	{
		@Override
		public ValidationStatus validateUserInput(List<FormContainer> form) {
			ValidationStatus rfc = new ValidationStatus();
			List<String> answers = new LinkedList<>();
            for (FormContainer fc : form)
				answers.add((String) fc.getEntry());
			String current = answers.get(0);
			String new1 = answers.get(1);
			String new2 = answers.get(2);

            int response = db.setPassword(user.uid, current, new1, new2);
			if ((rfc.message = newPassError(response)) == null)
                rfc.valid = true;
			return rfc;
		}

		@Override
		public void callNext() { }
		
		PasswordUpdate() { }

		void createForm()
		{
            List<FormContainer> form = new LinkedList<>();
			form.add(new FieldContainer(false, true, msg.info(Messages.INFO.CURRENT_PASSWORD), null));
			form.add(new FieldContainer(false, true, msg.info(Messages.INFO.NEW_PASSWORD), null));
			form.add(new FieldContainer(false, true, msg.info(Messages.INFO.RE_NEW_PASSWORD), null));
			
			ui.displayMessage(msg.info(Messages.INFO.NEW_PASS_INFO), false);
			ui.presentForm(form, this, true);
		}
	}

	private class User
    {
        long uid;
        boolean updatePassword;
        boolean loggedIn;

        User(long uid, boolean update_password, boolean loggedIn)
        {
            this.uid = uid;
            this.updatePassword = update_password;
            this.loggedIn = loggedIn;
        }
    }

    private class PingServer implements Runnable
	{
        final int sleepTimePerCycle = 100;
        final int cyclesPerInterval = 50;

		@Override
		public void run() {
			while (running) {
                for (int i = 0; running && i < cyclesPerInterval; i++) {
                    try {
                        Thread.sleep(sleepTimePerCycle);
                    } catch (InterruptedException ignored) {

                    }
                }
                db.ping(user.uid);
			}
		}
	}
}
