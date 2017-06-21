package implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import core.containers.Form;
import core.containers.FormContainer;

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

	public void registrationProcess()
	{
		Scanner in = new Scanner(System.in);
		PrintStream ps = System.out;
		FormContainer fc = new FormContainer();
		String[] formKeys = {"Name", "E-mail", "Clinic"};
		for (String s : formKeys)
			fc.addForm(new Form(s));
		ui.displayForm(fc);
		
		String emailSubject = "PROM_PREM:Registration request";
		String emailBody = String.format(
				("Registration request from:<br>"
						+ "<br> Name: %s<br>E-mail: %s<br>Clinic: %s"),
				fc.getValue("Name"), fc.getValue("E-mail"),
				fc.getValue("Clinic"));
		send(adminEmail, emailSubject, emailBody, "text/html");
	}
	
	/**
	 * 
	 * @param recipient
	 * @param emailSubject
	 * @param emailBody
	 * @param bodyFormat
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
	 * Send a single email.
	 * 
	 * @param fromEmailAddr The e-mail address of the sender.
	 * @param toEmailAddr The e-mail address of the receiver.
	 * @param subject E-mail subject.
	 * 
	 * @param body The contents of the e-mail.
	 */
	
	public void refreshConfig()
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
	
	private void loadEmailAccounts(File configFile)
	{
		try
		{
			Properties props = new Properties();
			props.load(new FileInputStream("src/implement/mailaccount_settings.ini"));
			adminEmail = props.getProperty("admin_email");
			serverEmail = props.getProperty("server_email");
			serverPassword = props.getProperty("server_password");
			props.clear();
		} catch (IOException ioe)
		{
			
		}
	}
}
