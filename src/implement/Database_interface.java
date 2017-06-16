package implement;

public interface Database_interface
{
	public static final int ERROR = -1;
	public static final int CONNECT_SUCCESS = 0x10;
	public static final int DISCONNECT_SUCCESS = 0x20;
	public static final int QUERY_SUCCESS = 0x40;

	/**
	 * Opens a connection to the database.
	 * 
	 * @return CONNECT_SUCCESS on successful connection,
	 * 		ERROR on failure to connect.
	 */
	public int connect();

	/**
	 * Closes an open connection to the database.
	 * 
	 * @return DISCONNECT_SUCCESS on successful disconnection,
	 * 		ERROR on failure to disconnect.
	 */
	public int disconnect();
	
	/**
	 * Adds a new user to the database.
	 * 
	 * @param username The username of the new user
	 * @param password The (hashed) password of the new user
	 * @param clinic The clinic ID that the new user belongs to
	 * @param email The email of the new user.
	 * 
	 * @return QUERY_SUCCESS on successful update, ERROR on failure.
	 */
	public int addUser(String username,
			String password, int clinic, String email);
	
	/**
	 * Adds a new clinic to the database
	 * 
	 * @param clinicName The name of the clinic.
	 * 
	 * @return QUERY_SUCCESS on successful update, ERROR on failure.
	 */
	public int addClinic(String clinicName);
}
