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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import core.Utilities;
import core.containers.MessageContainer;
import core.containers.User;
import core.interfaces.Database_interface;

/**
 * This class is an example of an implementation of
 * Database_Interface. This is done using a MySQL database and a
 * MySQL Connector/J to provide a MySQL interface to Java.
 * 
 * @author Marcus Malmquist
 *
 */
public class Database implements Database_interface
{
	/**
	 * Initializes variables and loads the database configuration.
	 */
	public Database()
	{
		try
		{
			dbConfig = new DatabaseConfig();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/* 
	 * Public methods required by the interface.
	 */

	@Override
	public synchronized int connect()
	{
		int ret = ERROR;
		try
		{
			conn = DriverManager.getConnection(dbConfig.getURL(),
					dbConfig.getUser(), dbConfig.getPassword());
			stmt = conn.createStatement();
			ret = CONNECT_SUCCESS;
		} catch (SQLException se)
		{
			se.printStackTrace();
		}
		return ret;
	}
	
	@Override
	public synchronized int disconnect()
	{
		int ret = ERROR;
		try
		{
			if(stmt != null)
				stmt.close();
			stmt = null;
			if(conn != null)
				conn.close();
			conn = null;
			ret = DISCONNECT_SUCCESS;
		}
		catch(SQLException se)
		{
			se.printStackTrace();
		}
		return ret;
	}

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
	public int addClinic(String clinicName)
	{
		String qInsert = String.format(
				"INSERT INTO `clinics` (`id`, `name`) VALUES (NULL, '%s')", clinicName);
		return queryUpdate(qInsert);
	}
	
	@Override
	public HashMap<Integer, String> getClinics()
	{
		ResultSet rs = query("SELECT `id`, `name` FROM `clinics`");
		if (rs == null)
			return null;
		HashMap<Integer, String> ret = new HashMap<Integer, String>();
		try
		{
			while (rs.next())
				ret.put(rs.getInt("id"), rs.getString("name"));
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public User getUser(String username)
	{
		ResultSet rs = query("SELECT `clinic_id`, `name`, `password`, `email`, `salt`, `update_password` FROM `users`");
		if (rs == null)
			return null;
		User user = null;
		try
		{
			while (rs.next())
			{
				if (rs.getString("name").equals(username))
				{
					user = new User(rs.getInt("clinic_id"), rs.getString("name"), rs.getString("password"),
							rs.getString("email"), rs.getString("salt"), rs.getInt("update_password") != 0);
					break;
				}
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return user;
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
		return getMessages("error_messages", mc) ? QUERY_SUCCESS : ERROR;
	}

	@Override
	public int getInfoMessages(MessageContainer mc)
	{
		if (mc == null)
			return ERROR;
		return getMessages("info_messages", mc) ? QUERY_SUCCESS : ERROR;
	}
	
	/* 
	 * Private methods not required by the interface.
	 */
	
	/**
	 * Query the database to update an entry i.e. modify an existing
	 * database entry.
	 * 
	 * @param message The command (specified by the SQL language)
	 * 		to send to the database.
	 * 
	 * @return QUERY_SUCCESS on successful query, ERROR on failure.
	 */
	private synchronized int queryUpdate(String message)
	{
		int ret = ERROR;
		if (stmt == null)
			return ret;
		try
		{
			stmt.executeUpdate(message);
			ret = QUERY_SUCCESS;
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Query the database, typically for data (i.e. request data from
	 * the database).
	 * 
	 * @param message The command (specified by the SQL language)
	 * 		to send to the database.
	 * 
	 * @return The the ResultSet from the database. If the statement
	 * 		is not initialized or a query error occurs then null is
	 * 		returned.
	 */
	private synchronized ResultSet query(String message)
	{
		if (stmt == null)
			return null;
		ResultSet rs = null;
		try
		{
			rs = stmt.executeQuery(message);
		} catch (SQLException se)
		{
			se.printStackTrace();
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
	private boolean getMessages(String tableName, MessageContainer mc)
	{
		ResultSet rs = query(String.format(
				("SELECT `code`, `name`, `locale`, `message` "
						+ "FROM `%s`"), tableName));
		if (rs == null)
			return false;
		try
		{
			if (rs.isClosed())
				return false;
			while (rs.next())
			{
				HashMap<String, String> msg = new HashMap<String, String>();
				msg.put(rs.getString("locale"), rs.getString("message"));
				mc.addMessage(rs.getInt("code"), rs.getString("name"), msg);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
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
	
	private DatabaseConfig dbConfig;
	private Connection conn;
	private Statement stmt;
}