package implement;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import core.containers.Message;
import core.containers.MessageContainer;
import core.containers.User;


public class Database implements Database_interface
{
	
	private DatabaseConfig dbConfig;

	private Connection conn;
	private Statement stmt;

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

	@Override
	public int connect()
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
	public int disconnect()
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
	public int addUser(String username,
			String password, int clinic, String email)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String qInsert = String.format(
				"INSERT INTO `users` (`clinic_id`, `name`, `password`, `email`, `registered`) VALUES ('%d', '%s', '%s', '%s', '%s')",
				clinic, username, password, email, sdf.format(new Date()));
		return queryUpdate(qInsert);
	}

	@Override
	public int addClinic(String clinicName)
	{
		String qInsert = String.format(
				"INSERT INTO `clinics` (`id`, `name`) VALUES (NULL, '%s')", clinicName);
		return queryUpdate(qInsert);
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
	private ResultSet query(String message)
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
	public User setPassword(User user, String oldPass, String newPass, String newSalt)
	{
		if (!user.passwordMatch(oldPass))
			return null;
		String qInsert = String.format(
				"UPDATE `users` SET `password`='%s',`salt`='%s',`update_password`=%d WHERE `users`.`name` = '%s'",
				newPass, newSalt, 0, user.getUsername());
		System.out.println(qInsert);
		return queryUpdate(qInsert) == QUERY_SUCCESS ? getUser(user.getUsername()) : null;
	}

	@Override
	public int getErrorMessages(MessageContainer mc)
	{
		ResultSet rs = query("SELECT `code`, `name`, `locale`, `message` FROM `error_messages` WHERE `locale` = 'en'");
		if (rs == null)
			return ERROR;
		try
		{
			while (rs.next())
			{
				/* put messages for all locale in msg.
				 * currently only English(en) is loaded
				 * so only adding English messages.
				 */
				HashMap<String, String> msg = new HashMap<String, String>();
				msg.put(rs.getString("locale"), rs.getString("message"));
				
				mc.addMessage(new Message(rs.getInt("code"),
						rs.getString("name"), msg));
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return QUERY_SUCCESS;
	}

	private final class DatabaseConfig
	{
		// JDBC driver name and database URL
		private String jdbcDriver, dbURL;
		//  Database credentials
		private String username, password;
		
		public DatabaseConfig() throws IOException
		{
	        Properties props = new Properties();
	        props.load(new FileInputStream("src/implement/settings.ini"));
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
		
		public String getURL() { return dbURL; }
		public String getUser() { return username; }
		public String getPassword() { return password; }
	}
}
