/**
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
package implementation;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import core.Utilities;
import core.containers.Form;
import core.containers.form.FieldContainer;
import core.interfaces.Messages;
import core.interfaces.Registration;
import core.interfaces.UserInterface;

/**
 * This class is an example of an implementation of
 * Registration_Interface.
 * 
 * @author Marcus Malmquist
 *
 */
public class Email_Registration implements Registration
{
	/**
	 * Initializes Mailer class and loads configuration.
	 */
	public Email_Registration(UserInterface ui)
	{
		this.ui = ui;
		mailConfig = new Properties();
		refreshConfig();
	}
	
	/* 
	 * Public methods required by the interface.
	 */

	@Override
	public void registrationProcess()
	{
		String nameStr = Messages.getMessages().getInfo(
				Messages.INFO_REG_USER_NAME);
		String emailStr = Messages.getMessages().getInfo(
				Messages.INFO_REG_USER_EMAIL);
		String clinicStr = Messages.getMessages().getInfo(
				Messages.INFO_REG_CLINIC_NAME);
		Form f = new Form();
		FieldContainer name = new FieldContainer(nameStr);
		f.insert(name, Form.AT_END);
		FieldContainer email = new FieldContainer(emailStr);
		f.insert(email, Form.AT_END);
		FieldContainer clinic = new FieldContainer(clinicStr);
		f.insert(clinic, Form.AT_END);
		f.jumpTo(Form.AT_BEGIN);
		if (!ui.presentForm(f))
			return;
		
		String emailSubject = Messages.getMessages().getInfo(
				Messages.INFO_REG_EMAIL_SUBJECT);
		String emailDescription = Messages.getMessages().getInfo(
				Messages.INFO_REG_BODY_DESCRIPTION);
		String emailSignature = Messages.getMessages().getInfo(
				Messages.INFO_REG_EMAIL_SIGNATURE);
		String emailBody = String.format(
				("%s:<br><br> %s: %s<br>%s: %s<br>%s: %s<br><br> %s"),
				emailDescription, nameStr, name.getEntry(), emailStr,
				email.getEntry(), clinicStr, clinic.getEntry(), emailSignature);
		send(adminEmail, emailSubject, emailBody, "text/html");
	}
	
	/* 
	 * Private methods not required by the interface.
	 */
	
	/**
	 * Sends an email from the program's email account.
	 * 
	 * @param recipient The email address of to send the email to.
	 * @param emailSubject The subject of the email.
	 * @param emailBody The body/contents of the email.
	 * @param bodyFormat The format of the body. This could for
	 * 		example be 'text', 'html', 'text/html' etc.
	 */
	private void send(String recipient, String emailSubject,
			String emailBody, String bodyFormat)
	{
		/* generate session and message instances */
		Session getMailSession = Session.getDefaultInstance(mailConfig, null);
		MimeMessage generateMailMessage = new MimeMessage(getMailSession);
		try
		{
			/* create email */
			generateMailMessage.addRecipient(Message.RecipientType.TO,
					new InternetAddress(recipient));
			generateMailMessage.setSubject(emailSubject);
			generateMailMessage.setContent(emailBody, bodyFormat);
			
			/* login to server email account and send email. */
			Transport transport = getMailSession.getTransport();
			transport.connect(serverEmail, serverPassword);
			ui.displayMessage(Messages.getMessages().getInfo(
					Messages.INFO_REG_REQUEST_SENDING));
			transport.sendMessage(generateMailMessage,
					generateMailMessage.getAllRecipients());
			transport.close();
		} catch (MessagingException me)
		{
			me.printStackTrace();
		}
		ui.displayMessage(Messages.getMessages().getInfo(
				Messages.INFO_REG_REQUEST_SENT));
	}
	
	/**
	 * reloads the javax.mail config properties as well as
	 * the email account config.
	 */
	private void refreshConfig()
	{
		loadConfig(CONFIG_FILE);
		loadEmailAccounts(ACCOUNT_FILE);
	}
	
	/**
	 * Loads the javax.mail config properties contained in the
	 * supplied config file.
	 * 
	 * @param filePath The file while the javax.mail config
	 * 		properties are located.
	 * 
	 * @return True if the file was loaded. False if an error
	 * 		occurred.
	 */
	private boolean loadConfig(String filePath)
	{
		if (!mailConfig.isEmpty())
			mailConfig.clear();
		try
		{
			mailConfig.load(Utilities.getResourceStream(getClass(), filePath));
		}
		catch(IOException ioe)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Loads the registration program's email account information
	 * as well as the email address of the administrator who will
	 * receive registration requests.
	 * 
	 * @param filePath The file that contains the email account
	 * 		information.
	 * 
	 * @return True if the file was loaded. False if an error
	 * 		occurred.
	 */
	private boolean loadEmailAccounts(String filePath)
	{
		try
		{
			Properties props = new Properties();
			props.load(Utilities.getResourceStream(getClass(), filePath));
			adminEmail = props.getProperty("admin_email");
			serverEmail = props.getProperty("server_email");
			serverPassword = props.getProperty("server_password");
			props.clear();
		} catch (IOException ioe)
		{
			return false;
		}
		return true;
	}
	
	private Properties mailConfig;
	private UserInterface ui;
	private final String CONFIG_FILE =
			"implementation/mail_settings.txt";
	private final String ACCOUNT_FILE =
			"implementation/mailaccount_settings.ini";
	
	// server mailing account
	private String serverEmail, serverPassword, adminEmail;
}
