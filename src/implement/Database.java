package implement;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;


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

	/**
	 * Open a connection to the database.
	 */
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
		{ // nothing we can do
			se.printStackTrace();
		}
		return ret;
	}
	
	public int addUser(String username,
			String password, int clinic, String email)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String qInsert = String.format(
				"INSERT INTO `users` (`clinic_id`, `name`, `password`, `email`, `registered`) VALUES ('%d', '%s', '%s', '%s', '%s')",
				clinic, username, password, email, sdf.format(new Date()));
		return queryUpdate(qInsert);
	}
	
	public int addClinic(String clinicName)
	{
		String qInsert = String.format(
				"INSERT INTO `clinics` (`id`, `name`) VALUES (NULL, '%s')", clinicName);
		return queryUpdate(qInsert);
	}
	
	private int queryUpdate(String message)
	{
		int ret = ERROR;
		try
		{
			stmt.executeUpdate(message);
			ret = QUERY_SUCCESS;
		} catch (SQLException se)
		{
			se.printStackTrace();
		}
		return ret;
	}
	
	private ResultSet query(String message)
	{
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
	
	public HashMap<Integer, String> getClinics()
	{
		ResultSet rs = query("SELECT `id`, `name` FROM `clinics`");
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

	private final class DatabaseConfig
	{
		// JDBC driver name and database URL
		private String jdbcDriver, dbURL;
		private int port;
		//  Database credentials
		private String username, password;
		
		public DatabaseConfig() throws IOException
		{
	        Properties props = new Properties();
	        props.load(new FileInputStream("src/implement/settings.ini"));
	        jdbcDriver = props.getProperty("jdbc_driver");
	        dbURL = props.getProperty("url");
	        port = Integer.parseInt(props.getProperty("port"));
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
