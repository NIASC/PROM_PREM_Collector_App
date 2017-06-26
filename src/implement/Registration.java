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
package implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import core.containers.form.Field;
import core.containers.form.FieldContainer;

/**
 * This class is an example of an implementation of
 * Registration_Interface.
 * 
 * @author Marcus Malmquist
 *
 */
public class Registration implements Registration_Interface
{
	private Properties mailConfig;
	private UserInterface ui;
	private final String CONFIG_FILE = "src/implement/mail_settings.txt";
	private final String ACCOUNT_FILE = "src/implement/mailaccount_settings.ini";
	
	// server mailing account
	private String serverEmail, serverPassword, adminEmail;
	
	/**
	 * Initializes Mailer class and loads configuration.
	 */
	public Registration(UserInterface ui)
	{
		this.ui = ui;
		mailConfig = new Properties();
		refreshConfig();
	}

	@Override
	public void registrationProcess()
	{
		FieldContainer fc = new FieldContainer();
		String[] formKeys = {"Name", "E-mail", "Clinic"};
		for (String s : formKeys)
			fc.addForm(new Field(s));
		ui.displayForm(fc);
		
		String emailSubject = "PROM_PREM:Registration request";
		String emailBody = String.format(
				("Registration request from:"
						+ "<br><br> Name: %s<br>E-mail: %s<br>Clinic: %s"
						+ "<br><br> This message was sent from the PROM/PREM collector"),
				fc.getValue("Name"), fc.getValue("E-mail"),
				fc.getValue("Clinic"));
		send(adminEmail, emailSubject, emailBody, "text/html");
	}
	
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
		Session getMailSession = Session.getDefaultInstance(
				mailConfig, null);
		MimeMessage generateMailMessage = new MimeMessage(
				getMailSession);
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
			ui.displayMessage("Sending registration request...");
			transport.sendMessage(generateMailMessage,
					generateMailMessage.getAllRecipients());
			transport.close();
		} catch (MessagingException me)
		{
			me.printStackTrace();
		}
		ui.displayMessage("Registration request sent.");
	}
	
	/**
	 * reloads the javax.mail config properties as well as
	 * the email account config.
	 */
	private void refreshConfig()
	{
		loadConfig(new File(CONFIG_FILE));
		loadEmailAccounts(new File(ACCOUNT_FILE));
	}
	
	/**
	 * Loads the javax.mail config properties contained in the
	 * supplied config file.
	 * 
	 * @param configFile The file while the javax.mail config
	 * 		properties are located.
	 * 
	 * @return True if the file was loaded. False if an error
	 * 		occurred.
	 */
	private boolean loadConfig(File configFile)
	{
		if (!mailConfig.isEmpty())
			mailConfig.clear();
		try (FileInputStream in = new FileInputStream(configFile))
		{
			mailConfig.load(in);
		}
		catch(FileNotFoundException fnf) { return false; }
		catch(IOException ioe) { return false; }
		return true;
	}
	
	/**
	 * Loads the registration program's email account information
	 * as well as the email address of the administrator who will
	 * receive registration requests.
	 * 
	 * @param configFile The file that contains the email account
	 * 		information.
	 */
	private void loadEmailAccounts(File configFile)
	{
		try
		{
			Properties props = new Properties();
			props.load(new FileInputStream(configFile));
			adminEmail = props.getProperty("admin_email");
			serverEmail = props.getProperty("server_email");
			serverPassword = props.getProperty("server_password");
			props.clear();
		} catch (IOException ioe)
		{
			
		}
	}
}
