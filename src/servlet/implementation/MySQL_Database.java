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
package servlet.implementation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import applet.core.interfaces.Messages;
import servlet.core.UserManager;
import servlet.core.Utilities;
import servlet.core.interfaces.Database;
import servlet.implementation.exceptions.DBReadException;
import servlet.implementation.exceptions.DBWriteException;
import common.implementation.Constants;

/**
 * This class is an example of an implementation of
 * Database_Interface. This is done using a MySQL database and a
 * MySQL Connector/J to provide a MySQL interface to Java.
 * 
 * This class is designed to be thread safe and a singleton.
 * 
 * @author Marcus Malmquist
 *
 */
@SuppressWarnings("unchecked")
public class MySQL_Database implements Database
{
	/* Public */
	
	/**
	 * 
	 * @return The active instance of this class.
	 */
	public static synchronized MySQL_Database getDatabase()
	{
		if (database == null)
			database = new MySQL_Database();
		return database;
	}

	@Override
	public final Object clone()
			throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	/* 
	 * Public methods required by the interface.
	 */

	@Override
	public String addUser(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "add_user");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String qInsert = String.format(
				"INSERT INTO `users` (`clinic_id`, `name`, `password`, `email`, `registered`, `salt`, `update_password`) VALUES ('%d', '%s', '%s', '%s', '%s', '%s', '%d')",
				Integer.parseInt(omap.get("clinic_id")),
				omap.get("name"), omap.get("password"), omap.get("email"),
				sdf.format(new Date()), omap.get("salt"), 1);
		try {
			queryUpdate(qInsert);
			rmap.put(Constants.INSERT_RESULT, Constants.INSERT_SUCCESS);
		}
		catch (DBWriteException dbw) {
			rmap.put(Constants.INSERT_RESULT, Constants.INSERT_FAIL);
		}
		return ret.toString();
	}

	@Override
	public String addQuestionnaireAnswers(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "add_questionnaire_answers");
		
		int clinic_id = Integer.parseInt(omap.get("clinic_id"));
		String identifier = omap.get("identifier");
		String patientInsert = String.format(
				"INSERT INTO `patients` (`clinic_id`, `identifier`, `id`) VALUES ('%d', '%s', NULL)",
				clinic_id, identifier);
		
		Map<String, String> m = (Map<String, String>) getJSONObject(omap.get("questions"));
		List<String> question_ids = new ArrayList<String>();
		List<String> question_answers = new ArrayList<String>();
		for (Entry<String, String> e : m.entrySet())
		{
			question_ids.add((String) e.getKey());
			question_answers.add((String) e.getValue());
		}

		String resultInsert = String.format("INSERT INTO `questionnaire_answers` (`clinic_id`, `patient_identifier`, `date`, %s) VALUES ('%d', '%s', '%s', %s)",
				String.join(", ", question_ids), clinic_id, identifier,
				(new SimpleDateFormat("yyyy-MM-dd")).format(new Date()),
				String.join(", ", question_answers));
		try {
			if (!patientInDatabase(identifier))
				queryUpdate(patientInsert);
			queryUpdate(resultInsert);
			rmap.put(Constants.INSERT_RESULT, Constants.INSERT_SUCCESS);
		}
		catch (DBWriteException dbw) {
			rmap.put(Constants.INSERT_RESULT, Constants.INSERT_FAIL);
		}
		return ret.toString();
	}

	@Override
	public String addClinic(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "add_clinic");
		
		String qInsert = String.format(
				"INSERT INTO `clinics` (`id`, `name`) VALUES (NULL, '%s')",
				omap.get("name"));
		try {
			queryUpdate(qInsert);
			rmap.put(Constants.INSERT_RESULT, Constants.INSERT_SUCCESS);
		}
		catch (DBWriteException dbw) {
			rmap.put(Constants.INSERT_RESULT, Constants.INSERT_FAIL);
		}
		return ret.toString();
	}
	
	@Override
	public String getClinics(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "get_clinics");
		
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `id`, `name` FROM `clinics`");
			if (rs != null)
			{
				JSONObject clinics = new JSONObject();
				Map<String, String> cmap = (Map<String, String>) clinics;
				while (rs.next())
					cmap.put(Integer.toString(rs.getInt("id")),
							rs.getString("name"));
				rmap.put("clinics", clinics.toString());
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret.toString();
	}

	@Override
	public String getUser(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "get_user");
		
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `clinic_id`, `name`, `password`, `email`, `salt`, `update_password` FROM `users`");
			if (rs == null)
				return ret.toString();

			String username = omap.get("name");
			JSONObject user = new JSONObject();
			Map<String, String> umap = (Map<String, String>) user;
			while (rs.next())
			{
				if (rs.getString("name").equals(username))
				{
					umap.put("clinic_id", Integer.toString(rs.getInt("clinic_id")));
					umap.put("name", rs.getString("name"));
					umap.put("password", rs.getString("password"));
					umap.put("email", rs.getString("email"));
					umap.put("salt", rs.getString("salt"));
					umap.put("update_password", Integer.toString(rs.getInt("update_password")));
					break;
				}
			}
			rmap.put("user", user.toString());
		}
		catch (DBReadException dbr) { }
		catch (SQLException se) { }
		return ret.toString();
	}

	@Override
	public String setPassword(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		Map<String, String> userobj = getUser(omap.get("name"));
		if (userobj == null)
			return null;
		Map<String, String> user = (Map<String, String>) getJSONObject(userobj.get("user"));
		
		String oldPass = omap.get("old_password");
		String newPass = omap.get("new_password");
		String newSalt = omap.get("new_salt");

		if (!user.get("password").equals(oldPass))
			return null;
		
		String qInsert = String.format(
				"UPDATE `users` SET `password`='%s',`salt`='%s',`update_password`=%d WHERE `users`.`name` = '%s'",
				newPass, newSalt, 0, user.get("name"));
		try {
			queryUpdate(qInsert);
		} catch (DBWriteException dbw) {
			return null;
		}

		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "get_user");
		rmap.put("name", omap.get("name"));
		return getUser(ret);
	}

	@Override
	public String getErrorMessages(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "get_error_messages");
		
		getMessages("error_messages", rmap);
		return ret.toString();
	}

	@Override
	public String getInfoMessages(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "get_info_messages");
		
		getMessages("info_messages", rmap);
		return ret.toString();
	}
	
	@Override
	public String loadQuestions(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "load_questions");
		
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT * FROM `questionnaire`");
			if (rs != null)
			{
				JSONObject questions = new JSONObject();
				Map<String, String> qmap = (Map<String, String>) questions;
				while (rs.next())
				{
					JSONObject question = new JSONObject();
					Map<String, String> questionmap = (Map<String, String>) question;
					for (int i = 0; ; ++i)
					{
						try
						{
							String tbl_name = String.format("option%d", i);
							String entry = rs.getString(tbl_name);
							if (entry == null || (entry = entry.trim()).isEmpty())
								break;
							questionmap.put(tbl_name, entry);
						}
						catch (SQLException e)
						{ /* no more options */
							break;
						}
					}
					String id = Integer.toString(rs.getInt("id"));
					questionmap.put("type", rs.getString("type"));
					questionmap.put("id", id);
					questionmap.put("question", rs.getString("question"));
					questionmap.put("description", rs.getString("description"));
					questionmap.put("optional", Integer.toString(rs.getInt("optional")));
					questionmap.put("max_val", Integer.toString(rs.getInt("max_val")));
					questionmap.put("min_val", Integer.toString(rs.getInt("min_val")));
					
					qmap.put(id, question.toString());
				}
				rmap.put("questions", questions.toString());
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret.toString();
	}

	@Override
	public String loadQResultDates(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "load_q_result_dates");

		Map<String, String> user = getUser(omap.get("name"));
		if (user == null)
			return null;
		
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, String.format(
					"SELECT `date` FROM `questionnaire_answers` WHERE `clinic_id` = %d",
					Integer.parseInt(user.get("clinic_id"))));
			if (rs != null)
			{
				JSONArray dates = new JSONArray();
				List<String> dlist = (List<String>) dates;
				while (rs.next())
					dlist.add(rs.getString("date"));
				rmap.put("dates", dates.toString());
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret.toString();
	}
	
	@Override
	public String loadQResults(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "load_q_results");

		Map<String, String> user = getUser(omap.get("name"));
		if (user == null)
			return null;
		
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();

			JSONParser parser = new JSONParser();
			JSONArray questions = null;
			try{
				questions = (JSONArray) parser.parse(omap.get("questions"));
			} catch (org.json.simple.parser.ParseException pe) {
				return null;
			}
			if (questions == null)
				return null;
			List<String> qlist = (List<String>) questions;
			
			List<String> lstr = new ArrayList<String>();
			for (Iterator<?> itr = questions.iterator(); itr.hasNext();)
				lstr.add((String) itr.next());
			
			ResultSet rs = query(s, String.format(
					"SELECT %s FROM `questionnaire_answers` WHERE `clinic_id` = %d AND `date` BETWEEN '%s' AND '%s'",
					String.join(", ", lstr), Integer.parseInt(user.get("clinic_id")),
					omap.get("begin"), omap.get("end")));
			
			if (rs != null)
			{
				JSONArray results = new JSONArray();
				List<String> rlist = (List<String>) results;
				while (rs.next())
				{
					JSONObject answers = new JSONObject();
					Map<String, String> amap = (Map<String, String>) ret;
					for (Iterator<String> itr = qlist.iterator(); itr.hasNext();)
					{
						String q = itr.next();
						amap.put(q, rs.getString(q));
					}
					rlist.add(answers.toString());
				}
				rmap.put("results", results.toString());
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret.toString();
	}
	
	@Override
	public String requestRegistration(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "request_registration");
		
		String name = omap.get("name");
		String email = omap.get("email");
		String clinic = omap.get("clinic");
		
		String emailSubject = "PROM_PREM: Registration request";
		String emailDescription = "Registration reguest from";
		String emailSignature = "This message was sent from the PROM/PREM Collector";
		String emailBody = String.format(
				("%s:<br><br> %s: %s<br>%s: %s<br>%s: %s<br><br> %s"),
				emailDescription, "Name", name, "E-mail",
				email, "Clinic", clinic, emailSignature);
		
		if (send(config.adminEmail, emailSubject, emailBody, "text/html"))
			rmap.put(Constants.INSERT_RESULT, Constants.INSERT_SUCCESS);
		else
			rmap.put(Constants.INSERT_RESULT, Constants.INSERT_FAIL);
		return ret.toString();
	}
	
	public String requestLogin(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "request_login");

		Map<String, String> userobj = getUser(omap.get("name"));
		if (userobj == null)
		{
			rmap.put(Constants.LOGIN_REPONSE, Constants.INVALID_DETAILS_STR);
			return ret.toString();
		}
		Map<String, String> user = (Map<String, String>) getJSONObject(userobj.get("user"));

		if (!user.get("password").equals(omap.get("password")))
		{
			rmap.put(Constants.LOGIN_REPONSE, Constants.INVALID_DETAILS_STR);
			return ret.toString();
		}
		
		UserManager um = UserManager.getUserManager();
		rmap.put(Constants.LOGIN_REPONSE, um.addUser(user.get("name")));
		return ret.toString();
	}
	
	public String requestLogout(JSONObject obj)
	{
		Map<String, String> omap = (Map<String, String>) obj;
		
		JSONObject ret = new JSONObject();
		Map<String, String> rmap = (Map<String, String>) ret;
		rmap.put("command", "request_logout");

		UserManager um = UserManager.getUserManager();
		String response = um.delUser(omap.get("name")) ? Constants.SUCCESS_STR : Constants.ERROR_STR;
		rmap.put(Constants.LOGOUT_REPONSE, response);
		return ret.toString();
	}
	
	/* Protected */
	
	/* Private */

	private static MySQL_Database database;
	/**
	 * Handles connection with the database.
	 */
	private DataSource dataSource;
	
	private EmailConfig config;
	
	/**
	 * Initializes variables and loads the database configuration.
	 * This class is a singleton and should only be instantiated once.
	 */
	private MySQL_Database()
	{
		config = new EmailConfig();
		try
		{
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/prom_prem_db");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if a patient with the {@code identifier} exists in the database.
	 * 
	 * @param identifier The identifier of the patient.
	 * 
	 * @return {@code true} if the patient exists in the database,
	 * 		{@code false} if not.
	 */
	private boolean patientInDatabase(String identifier)
	{
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `identifier` FROM `patients`");
			if (rs == null)
				return false;

			while (rs.next())
				if (rs.getString("identifier").equals(identifier))
					return true;
		}
		catch (DBReadException dbr) { }
		catch (SQLException se) { }
		return false;
	}
	
	private Map<String, String> getUser(String username)
	{
		JSONObject getuser = new JSONObject();
		getuser.put("command", "get_user");
		getuser.put("name", username);
		JSONParser parser = new JSONParser();
		try{
			JSONObject json = (JSONObject) parser.parse(getUser(getuser));
			return (Map<String, String>) json;
		} catch (org.json.simple.parser.ParseException pe) {
			return null;
		}
	}
	
	/**
	 * Query the database to update an entry i.e. modify an existing
	 * database entry.
	 * 
	 * @param message The command (specified by the SQL language)
	 * 		to send to the database.
	 * 
	 * @return QUERY_SUCCESS on successful query, ERROR on failure.
	 */
	private void queryUpdate(String message) throws DBWriteException
	{
		try (Connection c = dataSource.getConnection())
		{
			c.createStatement().executeUpdate(message);
		}
		catch (SQLException se) {
			throw new DBWriteException(String.format(
					"Database could not process request: '%s'. Check your arguments.",
					message));
		}
	}
	
	/**
	 * Query the database, typically for data (i.e. request data from
	 * the database).
	 * 
	 * @param s The statement that executes the query. The statement
	 * 		can be acquired by calling
	 * 		Connection c = DriverManager.getConnection(...)
	 * 		Statement s = c.createStatement()
	 * @param message The command (specified by the SQL language)
	 * 		to send to the database.
	 * 
	 * @return The the ResultSet from the database. If the statement
	 * 		is not initialized or a query error occurs then null is
	 * 		returned.
	 */
	private ResultSet query(Statement s, String message) throws DBReadException
	{
		ResultSet rs = null;
		try
		{
			rs = s.executeQuery(message);
		}
		catch (SQLException se) {
			throw new DBReadException(String.format(
					"Database could not process request: '%s'. Check your arguments.",
					message));
		}
		return rs;
	}

	/**
	 * Retrieves messages from the database and places them in a
	 * MessageContainer.
	 * 
	 * @param tableName The name of the (message) table to retrieve
	 * 		messages from.
	 * @param mc
	 * @return
	 */
	private boolean getMessages(String tableName, Map<String, String> retobj)
	{
		boolean ret = false;
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, String.format(
					"SELECT `code`, `name`, `locale`, `message` FROM `%s`",
					tableName));
			if (rs != null)
			{
				JSONObject messages = new JSONObject();
				Map<String, String> mmap = (Map<String, String>) messages;
				while (rs.next())
				{
					JSONObject msg = new JSONObject();
					Map<String, String> msgmap = (Map<String, String>) msg;
					msgmap.put(rs.getString("locale"), rs.getString("message"));
					
					JSONObject message = new JSONObject();
					Map<String, String> messagemap = (Map<String, String>) message;
					String name = rs.getString("name");
					messagemap.put("name", name);
					messagemap.put("code", rs.getString("code"));
					messagemap.put("message", msg.toString());
					
					mmap.put(name, message.toString());
				}
				retobj.put("messages", messages.toString());
				ret = true;
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret;
	}
	
	private JSONObject getJSONObject(String str)
	{
		JSONParser parser = new JSONParser();
		JSONObject userobj = null;
		try{
			userobj = (JSONObject) parser.parse(str);
		} catch (org.json.simple.parser.ParseException pe) {
			return null;
		}
		return userobj;
	}
	
	private JSONArray getJSONArray(String str)
	{
		JSONParser parser = new JSONParser();
		JSONArray userarr = null;
		try{
			userarr = (JSONArray) parser.parse(str);
		} catch (org.json.simple.parser.ParseException pe) {
			return null;
		}
		return userarr;
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
	private boolean send(String recipient, String emailSubject,
			String emailBody, String bodyFormat)
	{
		/* generate session and message instances */
		Session getMailSession = Session.getDefaultInstance(
				config.mailConfig, null);
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
			transport.connect(config.serverEmail, config.serverPassword);
			transport.sendMessage(generateMailMessage,
					generateMailMessage.getAllRecipients());
			transport.close();
		} catch (MessagingException me)
		{
			return false;
		}
		return true;
	}
	
	private final class EmailConfig
	{
		final String CONFIG_FILE =
				"servlet/implementation/mail_settings.txt";
		final String ACCOUNT_FILE =
				"servlet/implementation/mailaccount_settings.ini";
		Properties mailConfig;
		
		// server mailing account
		String serverEmail, serverPassword, adminEmail;
		
		EmailConfig()
		{
			mailConfig = new Properties();
			refreshConfig();
		}
		
		/**
		 * reloads the javax.mail config properties as well as
		 * the email account config.
		 */
		synchronized void refreshConfig()
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
		synchronized boolean loadConfig(String filePath)
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
		synchronized boolean loadEmailAccounts(String filePath)
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
	}
}