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
	
	public static final String CMD_ADD_USER		= "add_user";
	public static final String CMD_ADD_QANS		= "add_questionnaire_answers";
	public static final String CMD_ADD_CLINIC	= "add_clinic";
	public static final String CMD_GET_CLINICS	= "get_clinics";
	public static final String CMD_GET_USER		= "get_user";
	public static final String CMD_SET_PASSWORD	= "set_password";
	public static final String CMD_GET_ERR_MSG	= "get_error_messages";
	public static final String CMD_GET_INFO_MSG	= "get_info_messages";
	public static final String CMD_LOAD_Q		= "load_questions";
	public static final String CMD_LOAD_QR_DATE	= "load_q_result_dates";
	public static final String CMD_LOAD_QR		= "load_q_results";
	public static final String CMD_REQ_REGISTR	= "request_registration";
	public static final String CMD_REQ_LOGIN	= "request_login";
	public static final String CMD_REQ_LOGOUT	= "request_logout";
}
