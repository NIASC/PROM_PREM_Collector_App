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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import applet.core.containers.User;
import servlet.core.interfaces.Database;
import servlet.implementation.exceptions.DBReadException;
import servlet.implementation.exceptions.DBWriteException;

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
		JSONObject ret = new JSONObject();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String qInsert = String.format(
				"INSERT INTO `users` (`clinic_id`, `name`, `password`, `email`, `registered`, `salt`, `update_password`) VALUES ('%d', '%s', '%s', '%s', '%s', '%s', '%d')",
				Integer.parseInt((String) obj.get("clinic_id")),
				obj.get("name"), obj.get("password"), obj.get("email"),
				sdf.format(new Date()), obj.get("salt"), 1);
		try {
			queryUpdate(qInsert);
			ret.put(INSERT_RESULT, INSERT_SUCCESS);
		}
		catch (DBWriteException dbw) {
			ret.put(INSERT_RESULT, INSERT_FAIL);
		}
		return ret.toString();
	}

	@Override
	public String addQuestionnaireAnswers(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		String patientInsert = String.format("INSERT INTO `patients` (`clinic_id`, `identifier`, `id`) VALUES ('%d', '%s', NULL)",
				obj.get("clinic_id"), obj.get("identifier"));

		String resultInsert = String.format("INSERT INTO `questionnaire_answers` (`clinic_id`, `patient_identifier`, `date`, %s) VALUES ('%d', '%s', '%s', %s)",
				obj.get("question_ids"), obj.get("clinic_id"), obj.get("identifier"),
				(new SimpleDateFormat("yyyy-MM-dd")).format(new Date()),
				obj.get("question_answers"));
		try {
			if (!patientInDatabase((String) obj.get("identifier")))
				queryUpdate(patientInsert);
			queryUpdate(resultInsert);
			ret.put(INSERT_RESULT, INSERT_SUCCESS);
		}
		catch (DBWriteException dbw) {
			ret.put(INSERT_RESULT, INSERT_FAIL);
		}
		return ret.toString();
	}

	@Override
	public String addClinic(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		String qInsert = String.format(
				"INSERT INTO `clinics` (`id`, `name`) VALUES (NULL, '%s')",
				obj.get("name"));
		try {
			queryUpdate(qInsert);
			ret.put(INSERT_RESULT, INSERT_SUCCESS);
		}
		catch (DBWriteException dbw) {
			ret.put(INSERT_RESULT, INSERT_FAIL);
		}
		return ret.toString();
	}
	
	@Override
	public String getClinics(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		ret.put("command", "get_clinics");
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `id`, `name` FROM `clinics`");
			if (rs != null)
			{
				JSONObject clinics = new JSONObject();
				while (rs.next())
					clinics.put(rs.getInt("id"), rs.getString("name"));
				ret.put("clinics", clinics.toString());
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret.toString();
	}

	@Override
	public String getUser(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		ret.put("command", "get_user");
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT `clinic_id`, `name`, `password`, `email`, `salt`, `update_password` FROM `users`");
			if (rs == null)
				return null;

			String username = (String) obj.get("name");
			while (rs.next())
			{
				if (rs.getString("name").equals(username))
				{
					String clinic_id = Integer.toString(rs.getInt("clinic_id"));
					String name = rs.getString("name");
					String password = rs.getString("password");
					String email = rs.getString("email");
					String salt = rs.getString("salt");
					String update_password = Integer.toString(rs.getInt("update_password"));
					
					ret.put("clinic_id", clinic_id);
					ret.put("name", name);
					ret.put("password", password);
					ret.put("email", email);
					ret.put("salt", salt);
					ret.put("update_password", update_password);
					break;
				}
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException se) { }
		return ret.toString();
	}

	@Override
	public String setPassword(JSONObject obj)
	{
		User user = getUser((String) obj.get("name"));
		if (user == null)
			return null;
		
		String oldPass = (String) obj.get("old_password");
		String newPass = (String) obj.get("new_password");
		String newSalt = (String) obj.get("new_salt");
		
		if (!user.passwordMatch(oldPass))
			return null;
		String qInsert = String.format(
				"UPDATE `users` SET `password`='%s',`salt`='%s',`update_password`=%d WHERE `users`.`name` = '%s'",
				newPass, newSalt, 0, user.getUsername());
		try {
			queryUpdate(qInsert);
		} catch (DBWriteException dbw) {
			return null;
		}

		JSONObject getuser = new JSONObject();
		getuser.put("command", "get_user");
		getuser.put("name", obj.get("name"));
		return getUser(getuser);
	}

	@Override
	public String getErrorMessages(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		ret.put("command", "get_error_messages");
		getMessages("error_messages", ret);
		return ret.toString();
	}

	@Override
	public String getInfoMessages(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		ret.put("command", "get_info_messages");
		getMessages("info_messages", ret);
		return ret.toString();
	}
	
	@Override
	public String loadQuestions(JSONObject obj)
	{
		JSONObject ret = new JSONObject();
		ret.put("command", "load_questions");
		
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, "SELECT * FROM `questionnaire`");
			if (rs != null)
			{
				JSONObject questions = new JSONObject();
				while (rs.next())
				{
					JSONObject question = new JSONObject();
					for (int i = 0; ; ++i)
					{
						try
						{
							String tbl_name = String.format("option%d", i);
							String entry = rs.getString(tbl_name);
							if (entry == null || (entry = entry.trim()).isEmpty())
								break;
							question.put(tbl_name, entry);
						}
						catch (SQLException e)
						{ /* no more options */
							break;
						}
					}
					question.put("type", rs.getString("type"));
					question.put("id", Integer.toString(rs.getInt("id")));
					question.put("question", rs.getString("question"));
					question.put("description", rs.getString("description"));
					question.put("optional", Integer.toString(rs.getInt("optional")));
					question.put("max_val", Integer.toString(rs.getInt("max_val")));
					question.put("min_val", Integer.toString(rs.getInt("min_val")));
					
					questions.put("id", question.toString());
				}
				ret.put("questions", questions.toString());
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret.toString();
	}

	@Override
	public String loadQResultDates(JSONObject obj)
	{
		JSONObject ret = new JSONObject();

		User user = getUser((String) obj.get("name"));
		if (user == null)
			return null;
		
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();
			ResultSet rs = query(s, String.format(
					"SELECT `date` FROM `questionnaire_answers` WHERE `clinic_id` = %d",
					user.getClinicID()));
			if (rs != null)
			{
				JSONArray dates = new JSONArray();
				while (rs.next())
					dates.add(rs.getString("date"));
				ret.put("dates", dates.toString());
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret.toString();
	}
	
	@Override
	public String loadQResults(JSONObject obj)
	{
		// Calendar begin, Calendar end, List<Integer> questionIDs, StatisticsContainer container
		JSONObject ret = new JSONObject();

		User user = getUser((String) obj.get("name"));
		if (user == null)
			return null;
		
		try (Connection conn = dataSource.getConnection())
		{
			Statement s = conn.createStatement();

			JSONParser parser = new JSONParser();
			JSONArray questions = null;
			try{
				questions = (JSONArray) parser.parse((String) obj.get("questions"));
			} catch (org.json.simple.parser.ParseException pe) {
				return null;
			}
			if (questions == null)
				return null;
			
			List<String> lstr = new ArrayList<String>();
			for (Iterator<?> itr = questions.iterator(); itr.hasNext();)
				lstr.add((String) itr.next());
			
			ResultSet rs = query(s, String.format(
					"SELECT %s FROM `questionnaire_answers` WHERE `clinic_id` = %d AND `date` BETWEEN '%s' AND '%s'",
					String.join(", ", lstr), user.getClinicID(),
					obj.get("begin"), obj.get("end")));
			
			if (rs != null)
			{
				JSONArray results = new JSONArray();
				while (rs.next())
				{
					JSONObject answers = new JSONObject();
					for (Iterator<?> itr = questions.iterator(); itr.hasNext();)
					{
						String q = (String) itr.next();
						answers.put(q, rs.getString(q));
					}
				}
				ret.put("results", results);
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret.toString();
	}
	
	/* Protected */
	
	/* Private */

	private static MySQL_Database database;
	/**
	 * Handles connection with the database.
	 */
	private DataSource dataSource;
	
	/**
	 * Initializes variables and loads the database configuration.
	 * This class is a singleton and should only be instantiated once.
	 */
	private MySQL_Database()
	{
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
	
	private User getUser(String username)
	{
		JSONObject getuser = new JSONObject();
		getuser.put("command", "get_user");
		getuser.put("name", username);
		JSONParser parser = new JSONParser();
		JSONObject userobj = null;
		try{
			userobj = (JSONObject) parser.parse(getUser(getuser));
		} catch (org.json.simple.parser.ParseException pe) {
			return null;
		}
		if (userobj == null)
			return null;
		
		return new User(Integer.parseInt((String) userobj.get("clinic_id")),
				(String) userobj.get("name"),
				(String) userobj.get("password"),
				(String) userobj.get("email"),
				(String) userobj.get("salt"),
				Integer.parseInt((String) userobj.get("update_password")) != 0);
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
	private boolean getMessages(String tableName, JSONObject retobj)
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
				while (rs.next())
				{
					JSONObject msg = new JSONObject();
					msg.put(rs.getString("locale"), rs.getString("message"));
					
					JSONObject message = new JSONObject();
					message.put("name", rs.getString("name"));
					message.put("code", rs.getString("code"));
					message.put("message", msg.toString());
					
					messages.put("name", message.toString());
				}
				retobj.put(tableName, messages.toString());
				ret = true;
			}
		}
		catch (DBReadException dbr) { }
		catch (SQLException e) { }
		return ret;
	}
}