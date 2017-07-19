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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import core.Utilities;
import core.containers.MessageContainer;
import core.containers.Patient;
import core.containers.QuestionContainer;
import core.containers.QuestionContainer.Question;
import core.containers.StatisticsContainer;
import core.containers.User;
import core.containers.form.AreaContainer;
import core.containers.form.FieldContainer;
import core.containers.form.FormContainer;
import core.containers.form.MultipleOptionContainer;
import core.containers.form.SingleOptionContainer;
import core.containers.form.SliderContainer;
import core.containers.form.TimePeriodContainer;
import core.interfaces.Database;
import core.interfaces.Encryption;
import core.interfaces.Implementations;
import core.interfaces.Questions;

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
	public int addQuestionnaireAnswers(Patient patient, List<FormContainer> answers)
	{
		int nQuestions = Questions.getQuestions().getContainer().getSize();
		if (answers.size() != nQuestions)
			return ERROR;
		
		List<String> values = new ArrayList<String>();
		List<String> fields = new ArrayList<String>();
		int i = 0;
		for (Iterator<FormContainer> itr = answers.iterator(); itr.hasNext();)
		{
			fields.add(String.format("`question%d`", i++));
			values.add(QDBFormat.getDBFormat(itr.next()));
		}
		
		String identifier = crypto.encryptMessage(
				patient.getForename(), patient.getPersonalNumber(),
				patient.getSurname());
		
		String patientInsert = String.format("INSERT INTO `patients` (`clinic_id`, `identifier`, `id`) VALUES ('%d', '%s', NULL)",
				patient.getClinicID(), identifier);

		String resultInsert = String.format("INSERT INTO `questionnaire_answers` (`clinic_id`, `patient_identifier`, `date`, %s) VALUES ('%d', '%s', '%s', %s)",
				String.join(", ", fields), patient.getClinicID(), identifier,
				(new SimpleDateFormat("yyyy-MM-dd")).format(new Date()),
				String.join(", ", values));
		
		int ret = QUERY_SUCCESS;
		if (!patientInDatabase(identifier))
			ret = queryUpdate(patientInsert);
		
		if (ret == QUERY_SUCCESS && queryUpdate(resultInsert) == ret)
			return QUERY_SUCCESS;
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
	public Map<Integer, String> getClinics()
	{
		Map<Integer, String> ret = new TreeMap<Integer, String>();
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
	public boolean patientInDatabase(String identifier)
	{
		try (Connection conn = DriverManager.getConnection(
				dbConfig.getURL(), dbConfig.getUser(), dbConfig.getPassword()))
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `identifier` FROM `patients`");
			if (rs == null)
				return false;

			while (rs.next())
				if (rs.getString("identifier").equals(identifier))
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
			ResultSet rs = query(s, "SELECT * FROM `questionnaire`");
			if (rs != null)
			{
				while (rs.next())
				{
					/* Allows for arbitrary number of options */
					List<String> options = new ArrayList<String>();
					for (int i = 0; ;++i)
					{
						try
						{
							String entry = rs.getString(String.format("option%d", i));
							if (entry == null || (entry = entry.trim()).isEmpty())
								break;
							options.add(entry);
						}
						catch (SQLException e)
						{
							/* getString throws SQLException if the column does
							 * not exist, so this seems like the only way to
							 * find out how many options that is stored in the
							 * database.
							 */
							break;
						}
					}
					
					Class<? extends FormContainer> c;
					if ((c = getContainerClass(rs.getString("type"))) == null)
						continue;
					qc.addQuestion(rs.getInt("id"), c, rs.getString("question"),
							rs.getString("description"), options,
							rs.getInt("optional") != 0, rs.getInt("max_val"), rs.getInt("min_val"));
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
					Calendar cal = new GregorianCalendar();
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
	
	@Override
	public int loadQResults(User user, Calendar begin, Calendar end,
			List<Integer> questionIDs, StatisticsContainer container)
	{
		int ret = ERROR;
		try (Connection conn = DriverManager.getConnection(
				dbConfig.getURL(), dbConfig.getUser(), dbConfig.getPassword()))
		{
			Statement s = conn.createStatement();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			List<String> lstr = new ArrayList<String>();
			for (Iterator<Integer> itr = questionIDs.iterator(); itr.hasNext();)
				lstr.add(String.format("`question%d`", itr.next()));
			
			ResultSet rs = query(s, String.format(
					"SELECT %s FROM `questionnaire_answers` WHERE `clinic_id` = %d AND `date` BETWEEN '%s' AND '%s'",
					String.join(", ", lstr), user.getClinicID(),
					sdf.format(begin.getTime()),
					sdf.format(end.getTime())));
			QuestionContainer qc = Questions.getQuestions().getContainer();
			if (rs != null)
			{
				while (rs.next())
				{
					for (Iterator<Integer> itr = questionIDs.iterator(); itr.hasNext();)
					{
						int qid = itr.next();
						Question q1 = qc.getQuestion(qid);
						String q = String.format("question%d", qid);
						container.addResult(q1, QDBFormat.getQFormat(rs.getString(q)));
					}
				}
				ret = QUERY_SUCCESS;
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return ret;
	}
	
	/* Protected */
	
	/* Private */

	private static MySQL_Database database;
	private DatabaseConfig dbConfig;
	private Encryption crypto;
	
	/**
	 * Initializes variables and loads the database configuration.
	 * This class is a singleton and should only be instantiated once.
	 */
	private MySQL_Database()
	{
		crypto = Implementations.Encryption();
		try
		{
			dbConfig = new DatabaseConfig();
		} catch (IOException e) { }
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
					Map<String, String> msg = new HashMap<String, String>();
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
	 * This method converts a container type from the String
	 * representation in the database to the appropriate class
	 * representation in java.
	 * 
	 * @param type The type of container as it appears in the database
	 * 		(SingleOption, Slider, Field etc.).
	 * 
	 * @return The class representation of the supplied {@code type}.
	 * 		The classes can be acquired using isAssignableFrom.<Br>
	 * 		Example:<br>
	 * 		<code>
	 * 		if (SliderContainer.class.isAssignableFrom(getContainerClass("Slider")))
	 * 		new SliderContainer( ... );</code>
	 * 
	 * @see Class#isAssignableFrom
	 */
	private Class<? extends FormContainer> getContainerClass(String type)
	{
		if (type.equalsIgnoreCase("SingleOption"))
			return SingleOptionContainer.class;
		else if (type.equalsIgnoreCase("MultipleOption"))
			return MultipleOptionContainer.class;
		else if (type.equalsIgnoreCase("Field"))
			return FieldContainer.class;
		else if (type.equalsIgnoreCase("Slider"))
			return SliderContainer.class;
		else if (type.equalsIgnoreCase("Area"))
			return AreaContainer.class;
		else
			return null;
	}
	
	private static class QDBFormat
	{
		static String getDBFormat(FormContainer fc)
		{
			if (fc.getEntry() == null)
				return "''";
			
			if (fc instanceof SingleOptionContainer)
			{
				SingleOptionContainer soc = (SingleOptionContainer) fc;
				return String.format("'option%d'", soc.getEntry());
			}
			else if (fc instanceof MultipleOptionContainer)
			{
				MultipleOptionContainer moc = (MultipleOptionContainer) fc;
				List<String> lstr = new ArrayList<String>();
				List<Integer> lint = new ArrayList<Integer>(moc.getEntry());
				Collections.sort(lint);
				for (Iterator<Integer> itr = lint.iterator(); itr.hasNext();)
					lstr.add(String.format("option%d", itr.next()));
				return String.format("[%s]", String.join(",", lstr));
			}
			else if (fc instanceof SliderContainer)
			{
				SliderContainer sc = (SliderContainer) fc;
				return String.format("'slider%d'", sc.getEntry());
			}
			else if (fc instanceof AreaContainer)
			{
				AreaContainer ac = (AreaContainer) fc;
				return String.format("'%s'", ac.getEntry());
			}
			else
				return "''";
		}
		
		static Object getQFormat(String dbEntry)
		{
			if (dbEntry == null || dbEntry.trim().isEmpty())
				return null;
			
			if (dbEntry.startsWith("option"))
			{ /* single option */
				return new Integer(dbEntry.substring("option".length()));
			}
			else if (dbEntry.startsWith("slider"))
			{ /* slider */
				return new Integer(dbEntry.substring("slider".length()));
			}
			else if (dbEntry.startsWith("[") &&  dbEntry.endsWith("]"))
			{ /* multiple answers */
				List<String> entries = Arrays.asList(dbEntry.split(","));
				if (entries.get(0).startsWith("option"))
				{ /* multiple option */
					List<Integer> lint = new ArrayList<Integer>();
					for (Iterator<String> itr = entries.iterator(); itr.hasNext();)
						lint.add(new Integer(itr.next().substring("option".length())));
					return lint;
				}
			}
			else
			{ /* must be plain text entry */
				return dbEntry;
			}
			return null;
		}
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