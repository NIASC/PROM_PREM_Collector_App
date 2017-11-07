/** Email_Registration.java
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
package applet.implementation;

import java.util.ArrayList;
import java.util.List;

import applet.core.containers.Form;
import applet.core.containers.form.FieldContainer;
import applet.core.interfaces.Database;
import applet.core.interfaces.FormUtils;
import applet.core.interfaces.Implementations;
import applet.core.interfaces.Messages;
import applet.core.interfaces.Registration;
import applet.core.interfaces.UserInterface;

/**
 * This class is an example of an implementation of
 * Registration_Interface.
 * 
 * @author Marcus Malmquist
 *
 */
public class Email_Registration implements Registration, FormUtils
{
	/* Public */
	
	/**
	 * 
	 * @param ui The user user interface instance.
	 */
	public Email_Registration(UserInterface ui)
	{
		this.ui = ui;
	}
	
	/* 
	 * Public methods required by the interface.
	 */

	@Override
	public void registrationProcess()
	{
		Form f = new Form();
		FieldContainer name = new FieldContainer(false, false,
				Messages.getMessages().getInfo(Messages.INFO_REG_USER_NAME), null);
		f.insert(name, Form.AT_END);
		FieldContainer email = new FieldContainer(false, false,
				Messages.getMessages().getInfo(Messages.INFO_REG_USER_EMAIL), null);
		f.insert(email, Form.AT_END);
		FieldContainer clinic = new FieldContainer(false, false,
				Messages.getMessages().getInfo(Messages.INFO_REG_CLINIC_NAME), null);
		f.insert(clinic, Form.AT_END);
		f.jumpTo(Form.AT_BEGIN);
		
		ui.presentForm(f, this, true);
	}

	@Override
	public RetFunContainer ValidateUserInput(Form form)
	{
		RetFunContainer rfc = new RetFunContainer();
		List<String> answers = new ArrayList<String>();
		form.jumpTo(Form.AT_BEGIN);
		do
			answers.add((String) form.currentEntry().getEntry());
		while (form.nextEntry() != null);
		String name = answers.get(0);
		String email = answers.get(1);
		String clinic = answers.get(2);
		
		Database db = Implementations.Database();
		boolean success = db.requestRegistration(name, email, clinic);
		if (success)
			ui.displayMessage(Messages.getMessages().getInfo(
					Messages.INFO_REG_REQUEST_SENT), true);
		else
			ui.displayMessage(Messages.getMessages().getError(
					Messages.ERROR_REG_REQUEST_FAILED), true);
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
