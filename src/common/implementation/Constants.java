package common.implementation;

public interface Constants {
	/**
	 * If the messages was not retrieved from the database this
	 * message (in English) should be used to notify the caller that
	 * there was a database error.
	 */
	public static final String DATABASE_ERROR = "Database error.";
	
	public static final String INSERT_RESULT = "insert_result";
	public static final String INSERT_SUCCESS = "1";
	public static final String INSERT_FAIL = "0";
	
	public static final int ERROR = -1;
	public static final int SUCCESS = 0;
	public static final int SERVER_FULL = 2;
	public static final int ALREADY_ONLINE = 4;
	public static final int INVALID_DETAILS = 8;
	
	public static final String LOGIN_REPONSE = "login_response";
	public static final String LOGOUT_REPONSE = "logout_response";
	public static final String ERROR_STR = Integer.toString(ERROR);
	public static final String SUCCESS_STR = Integer.toString(SUCCESS);
	public static final String SERVER_FULL_STR = Integer.toString(SERVER_FULL);
	public static final String ALREADY_ONLINE_STR = Integer.toString(ALREADY_ONLINE);
	public static final String INVALID_DETAILS_STR = Integer.toString(INVALID_DETAILS);
}
