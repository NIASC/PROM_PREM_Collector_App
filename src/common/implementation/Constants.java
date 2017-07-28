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
}
