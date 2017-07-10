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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import core.Utilities;
import core.containers.MessageContainer;
import core.containers.Patient;
import core.containers.QuestionContainer;
import core.containers.User;
import core.containers.form.TimePeriodContainer;
import core.interfaces.Database;

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
	public int addUser(String username, String password,
			String salt, int clinic, String email)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String qInsert = String.format(
				"INSERT INTO `users` (`clinic_id`, `name`, `password`, `email`, `registered`, `salt`, `update_password`) VALUES ('%d', '%s', '%s', '%s', '%s', '%s', '%d')",
				clinic, username, password, email,
				sdf.format(new Date()), salt, 1);
		return queryUpdate(qInsert);
	}

	@Override
	public int addQuestionnaireAnswers(Patient patient, List<Object> answers)
	{
		int nQuestions = 6;
		if (answers.size() < nQuestions)
			return ERROR;
		String[] ans = new String[nQuestions];
		for (int i = 0; i < ans.length; ++i)
		{
			if (i < 5) // single option
				ans[i] = "option" + answers.get(i).toString();
			else if (i == 5) // slider
				ans[i] = answers.get(i).toString();
			else
				ans[i] = "";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String qInsert0 = String.format("INSERT INTO `patients` (`clinic_id`, `pnr`, `forename`, `lastname`, `id`) VALUES ('%d', '%s', '%s', '%s', NULL)",
				patient.getClinicID(), patient.getPersonalNumber(), patient.getForename(), patient.getSurname());
		String qInsert1 = String.format("INSERT INTO `questionnaire_answers` (`clinic_id`, `patient_pnr`, `date`, `question0`, `question1`, `question2`, `question3`, `question4`, `question5`) VALUES ('%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
				patient.getClinicID(), patient.getPersonalNumber(), sdf.format(new Date()),
				ans[0], ans[1], ans[2], ans[3], ans[4], ans[5]);
		
		int ret = QUERY_SUCCESS;
		if (!patientInDatabase(patient.getPersonalNumber()))
			ret = queryUpdate(qInsert0);
		
		if (ret == QUERY_SUCCESS && queryUpdate(qInsert1) == ret)
			return ret;
		else
			return ERROR;
	}

	@Override
	public int addClinic(String clinicName)
	{
		String qInsert = String.format(
				"INSERT INTO `clinics` (`id`, `name`) VALUES (NULL, '%s')", clinicName);
		return queryUpdate(qInsert);
	}
	
	@Override
	public HashMap<Integer, String> getClinics()
	{
		HashMap<Integer, String> ret = new HashMap<Integer, String>();
		try (Connection conn = DriverManager.getConnection(
				dbConfig.getURL(), dbConfig.getUser(), dbConfig.getPassword()))
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `id`, `name` FROM `clinics`");
			if (rs != null)
				while (rs.next())
					ret.put(rs.getInt("id"), rs.getString("name"));
		} catch (SQLException e) { }
		return ret;
	}

	@Override
	public User getUser(String username)
	{
		User user = null;
		try (Connection conn = DriverManager.getConnection(
				dbConfig.getURL(), dbConfig.getUser(), dbConfig.getPassword()))
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `clinic_id`, `name`, `password`, `email`, `salt`, `update_password` FROM `users`");
			if (rs == null)
				return null;

			while (rs.next() && user == null)
				if (rs.getString("name").equals(username))
					user = new User(rs.getInt("clinic_id"),
							rs.getString("name"), rs.getString("password"),
							rs.getString("email"), rs.getString("salt"),
							rs.getInt("update_password") != 0);
		}
		catch (SQLException se) { }
		return user;
	}
	
	@Override
	public boolean patientInDatabase(String pnr)
	{
		try (Connection conn = DriverManager.getConnection(
				dbConfig.getURL(), dbConfig.getUser(), dbConfig.getPassword()))
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `pnr` FROM `patients`");
			if (rs == null)
				return false;

			while (rs.next())
				if (rs.getString("pnr").equals(pnr))
					return true;
		}
		catch (SQLException se) { }
		return false;
	}

	@Override
	public User setPassword(User user, String oldPass, String newPass,
			String newSalt)
	{
		if (!user.passwordMatch(oldPass))
			return null;
		String qInsert = String.format(
				"UPDATE `users` SET `password`='%s',`salt`='%s',`update_password`=%d WHERE `users`.`name` = '%s'",
				newPass, newSalt, 0, user.getUsername());
		return queryUpdate(qInsert) == QUERY_SUCCESS ? getUser(user.getUsername()) : null;
	}

	@Override
	public int getErrorMessages(MessageContainer mc)
	{
		if (mc == null)
			return ERROR;
		return getMessages("error_messages", mc);
	}

	@Override
	public int getInfoMessages(MessageContainer mc)
	{
		if (mc == null)
			return ERROR;
		return getMessages("info_messages", mc);
	}
	
	@Override
	public int loadQuestions(QuestionContainer qc)
	{
		int ret = ERROR;
		try (Connection conn = DriverManager.getConnection(
				dbConfig.getURL(), dbConfig.getUser(), dbConfig.getPassword()))
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `id`, `type`, `optional`, `question`, `option0`, `option1`, `option2`, `max_val`, `min_val` FROM `questionnaire`");
			if (rs != null)
			{
				while (rs.next())
				{
					qc.addQuestion(rs.getInt("id"), rs.getString("type"),
							rs.getString("question"), new String[]{
									rs.getString("option0"),
									rs.getString("option1"),
									rs.getString("option2")},
							rs.getInt("optional") != 0,
							rs.getInt("max_val"), rs.getInt("min_val"));
				}
				ret = QUERY_SUCCESS;
			}
		}
		catch (SQLException e) { }
		return ret;
	}

	@Override
	public int loadQResultDates(User user, TimePeriodContainer tpc)
	{
		int ret = ERROR;
		try (Connection conn = DriverManager.getConnection(
				dbConfig.getURL(), dbConfig.getUser(), dbConfig.getPassword()))
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, String.format(
					"SELECT `date` FROM `questionnaire_answers` WHERE `clinic_id` = %d",
					user.getClinicID()));
			if (rs != null)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				while (rs.next())
				{
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(sdf.parse(rs.getString("date")));
					tpc.addDate(cal);
				}
				ret = QUERY_SUCCESS;
			}
		}
		catch (SQLException e) { }
		catch (ParseException e) { }
		return ret;
	}
	
	int loadQResults(User user, Calendar begin, Calendar end,
			Object[] questions)
	{
		int ret = ERROR;
		try (Connection conn = DriverManager.getConnection(
				dbConfig.getURL(), dbConfig.getUser(), dbConfig.getPassword()))
		{
			Statement s = conn.createStatement();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// could use a string builder here to only get the questions we want
			ResultSet rs = query(s, String.format(
					"SELECT * FROM `questionnaire_answers` WHERE `clinic_id` = %d AND `date` BETWEEN '%s' AND '%s'",
					user.getClinicID(), sdf.format(begin.getTime()), sdf.format(end.getTime())));
			if (rs != null)
			{
				while (rs.next())
				{
					// load question results into a container
					// get question identifiers as string and add them to
					// the container. when displaying the data load the
					// questions and display the answer rather than the
					// question identifier.
				}
				ret = QUERY_SUCCESS;
			}
		}
		catch (SQLException e) { }
		return ret;
	}
	
	/* Protected */
	
	/* Private */

	private static MySQL_Database database;
	private DatabaseConfig dbConfig;
	
	/**
	 * Initializes variables and loads the database configuration.
	 * This class is a singleton and should only be instantiated once.
	 */
	private MySQL_Database()
	{
		try
		{
			dbConfig = new DatabaseConfig();
		} catch (IOException e)
		{
			// e.printStackTrace();
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
	private int queryUpdate(String message)
	{
		int ret = ERROR;
		try (Connection c = DriverManager.getConnection(dbConfig.getURL(),
				dbConfig.getUser(), dbConfig.getPassword()))
		{
			c.createStatement().executeUpdate(message);
			ret = QUERY_SUCCESS;
		}
		catch (SQLException se) { }
		return ret;
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
	private ResultSet query(Statement s, String message)
	{
		ResultSet rs = null;
		try
		{
			rs = s.executeQuery(message);
		}
		catch (SQLException se) { }
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
	private int getMessages(String tableName, MessageContainer mc)
	{
		int ret = ERROR;
		try (Connection conn = DriverManager.getConnection(
				dbConfig.getURL(), dbConfig.getUser(), dbConfig.getPassword()))
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, String.format(
					"SELECT `code`, `name`, `locale`, `message` FROM `%s`",
					tableName));
			if (rs != null)
			{
				while (rs.next())
				{
					HashMap<String, String> msg = new HashMap<String, String>();
					msg.put(rs.getString("locale"), rs.getString("message"));
					mc.addMessage(rs.getInt("code"), rs.getString("name"), msg);
				}
				ret = QUERY_SUCCESS;
			}
		}
		catch (SQLException e) { }
		return ret;
	}

	/**
	 * Contains the database configuration.
	 * The configuration provides a link between the Java code and
	 * the database, and contains necessary information to log in to
	 * the database and get as well as put data in it.
	 * 
	 * The path to the configuration file is hard-coded.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private final class DatabaseConfig
	{
		private final String cfgFile = "implementation/settings.ini";
		// JDBC driver name and database URL
		private String jdbcDriver, dbURL;
		//  Database credentials
		private String username, password;
		
		/**
		 * Loads the settings from implementation/settings.ini.
		 * 
		 * @throws IOException
		 */
		private DatabaseConfig() throws IOException
		{
			Properties props = new Properties();
			props.load(Utilities.getResourceStream(getClass(), cfgFile));
			jdbcDriver = props.getProperty("jdbc_driver");
			dbURL = props.getProperty("url");
			username = props.getProperty("db_login");
			password = props.getProperty("db_password");
			props.clear();
			try
			{ // Register JDBC driver
				Class.forName(jdbcDriver);
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public Object clone()
				throws CloneNotSupportedException
		{
			throw new CloneNotSupportedException();
		}
		
		/**
		 * 
		 * @return The URL of the database.
		 */
		public String getURL()
		{
			return dbURL;
		}
		
		/**
		 * 
		 * @return The login name to the database.
		 */
		public String getUser()
		{
			return username;
		}
		
		/**
		 * 
		 * @return The password to the database in plain text.
		 */
		public String getPassword()
		{
			return password;
		}
	}
}