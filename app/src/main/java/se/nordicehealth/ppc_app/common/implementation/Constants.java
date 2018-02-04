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

import android.util.Log;

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

	public static final URL SERVER_URL;
	
	static
    {
		URL url = null;
		try {
            //url = new URL("http://188.114.242.3:443/PROM_PREM_Collector/main");
			url = new URL("http://192.168.1.22:8080/PROM_PREM_Collector/main");
		}
		catch (IOException | IllegalArgumentException ignore) {
			Log.e("ECONN", ignore.getMessage());
		}
		SERVER_URL = url;
	}
	
	public static final String INSERT_RESULT = "insert_result";
	public static final String INSERT_SUCCESS = "1";
	public static final String INSERT_FAIL = "0";
	
	public static final int ERROR           = -1;
	public static final int SUCCESS         = 0x0;
	public static final int SERVER_FULL     = 0x2;
	public static final int ALREADY_ONLINE  = 0x4;
	public static final int INVALID_DETAILS = 0x8;
    public static final int MISMATCH_NEW    = 0x10;
    public static final int PASSWORD_SHORT  = 0x20;
    public static final int PASSWORD_SIMPLE = 0x40;

    public static final String SETPASS_REPONSE = "setpass_response";
	public static final String LOGIN_REPONSE = "login_response";
	public static final String LOGOUT_REPONSE = "logout_response";
	public static final String LOGIN_UID = "login_uid";

	public static final String CMD_PING         = "ping";
    public static final String CMD_VALIDATE_PID = "validate_pid";
	public static final String CMD_ADD_QANS		= "add_questionnaire_answers";
	public static final String CMD_SET_PASSWORD	= "set_password";
	public static final String CMD_LOAD_Q		= "load_questions";
	public static final String CMD_LOAD_QR_DATE	= "load_q_result_dates";
	public static final String CMD_LOAD_QR		= "load_q_results";
	public static final String CMD_REQ_REGISTR	= "request_registration";
	public static final String CMD_REQ_LOGIN	= "request_login";
	public static final String CMD_REQ_LOGOUT	= "request_logout";
}
