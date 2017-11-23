package se.nordicehealth.ppc_app.implementation;

import java.util.ArrayList;
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
import se.nordicehealth.ppc_app.implementation.res.Resource;

public class EmailRegistration implements Registration, FormControl
{
	public EmailRegistration(UserInterface ui)
	{
		this.ui = ui;
	}

	@Override
	public void registrationProcess()
	{
		Messages msg = Resource.messages();
		List<FormContainer> f = new LinkedList<>();
		FieldContainer name = new FieldContainer(false, false, msg.info(Messages.INFO.REG_USER_NAME), null);
		FieldContainer email = new FieldContainer(false, false, msg.info(Messages.INFO.REG_USER_EMAIL), null);
		FieldContainer clinic = new FieldContainer(false, false, msg.info(Messages.INFO.REG_CLINIC_NAME), null);
		f.add(name);
		f.add(email);
		f.add(clinic);
		
		ui.presentForm(f, this, true);
	}

	@Override
	public ValidationStatus validateUserInput(List<FormContainer> form)
	{
        Messages msg = Resource.messages();
		ValidationStatus rfc = new ValidationStatus();
		List<String> answers = new ArrayList<>();
        for (FormContainer fc : form)
			answers.add((String) fc.getEntry());
		String name = answers.get(0);
		String email = answers.get(1);
		String clinic = answers.get(2);
		
		Server db = Implementations.Server();
		boolean success = db.requestRegistration(name, email, clinic);
		if (success)
			ui.displayMessage(msg.info(Messages.INFO.REG_REQUEST_SENT), true);
		else
			ui.displayMessage(msg.error(Messages.ERROR.REG_REQUEST_FAILED), true);
		rfc.valid = true;
		return rfc;
	}

	@Override
	public void callNext() { }
	
	private UserInterface ui;
}
