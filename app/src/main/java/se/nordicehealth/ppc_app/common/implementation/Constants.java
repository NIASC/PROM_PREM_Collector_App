/*! Constants.java
 * 
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
package se.nordicehealth.ppc_app.common.implementation;

import java.io.IOException;
import java.net.URL;

/**
 * This interface contains constant values that are used in both the
 * applet and servlet when communicating between them.
 * 
 * @author Marcus Malmquist
 *
 */
public abstract class Constants {
	
	/**
	 * The URL to the server.
	 */
	public static final URL SERVER_URL;
	
	static {
		URL url = null;
		try {
            //url = new URL("http://188.114.242.3:443/PROM_PREM_Collector/main");
			url = new URL("http://192.168.1.53:8080/PROM_PREM_Collector/main");
		}
		catch (IOException | IllegalArgumentException ignore) { }
		SERVER_URL = url;
	}
	
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
	
	public static final String CMD_ADD_QANS		= "add_questionnaire_answers";
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
