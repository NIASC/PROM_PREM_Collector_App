/*! Email_Registration.java
 * 
 * Copyright 2017 Marcus Malmquist
 * 
 * This file is part of PROM_PREM_Collector.
 * 
 * PROM_PREM_Collector is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * PROM_PREM_Collector is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PROM_PREM_Collector.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
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

/**
 * This class is an example of an implementation of
 * Registration_Interface.
 * 
 * @author Marcus Malmquist
 *
 */
public class EmailRegistration implements Registration, FormControl
{
	/* Public */
	
	/**
	 * 
	 * @param ui The user user interface instance.
	 */
	public EmailRegistration(UserInterface ui)
	{
		this.ui = ui;
	}
	
	/* 
	 * Public methods required by the interface.
	 */

	@Override
	public void registrationProcess()
	{
		List<FormContainer> f = new LinkedList<>();
		FieldContainer name = new FieldContainer(false, false,
				Resource.messages().info(Messages.INFO.REG_USER_NAME), null);
		f.add(name);
		FieldContainer email = new FieldContainer(false, false,
				Resource.messages().info(Messages.INFO.REG_USER_EMAIL), null);
		f.add(email);
		FieldContainer clinic = new FieldContainer(false, false,
				Resource.messages().info(Messages.INFO.REG_CLINIC_NAME), null);
		f.add(clinic);
		
		ui.presentForm(f, this, true);
	}

	@Override
	public ValidationStatus validateUserInput(List<FormContainer> form)
	{
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
			ui.displayMessage(Resource.messages().info(
					Messages.INFO.REG_REQUEST_SENT), true);
		else
			ui.displayMessage(Resource.messages().error(
					Messages.ERROR.REG_REQUEST_FAILED), true);
		rfc.valid = true;
		return rfc;
	}

	@Override
	public void callNext()
	{
		
	}
	
	/* Protected */
	
	/* Private */
	
	private UserInterface ui;
}
