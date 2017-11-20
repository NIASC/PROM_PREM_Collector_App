/*! Messages.java
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
package se.nordicehealth.ppc_app.core.interfaces;

/**
 * This class should contain error and information messages from the
 * {@code Database}.
 * The purpose of this class is to store messages from the
 * {@code Database} in a local class so you don't have to query the
 * {@code Database} every time you want to display a message to the
 * user.
 * This implementation allows you do easily choose the language to
 * display messages for and quick access to messages.
 * 
 * @author Marcus Malmquist
 * 
 * @see Database
 *
 */
public interface Messages
{
	/**
	 * Name of the error message. This variable can be seen as an
	 * implementation-independent pointer to the
	 * implementation-dependent error message.
	 */
	String  ERROR_UH_PR_PASSWORD_SIMPLE		= "UH_PR_PASSWORD_SIMPLE"	,
			ERROR_UH_PR_INVALID_LENGTH		= "UH_PR_INVALID_LENGTH"	,
			ERROR_UH_PR_MISMATCH_NEW		= "UH_PR_MISMATCH_NEW"		,
			ERROR_UH_PR_INVALID_CURRENT		= "UH_PR_INVALID_CURRENT"	,
			ERROR_UH_INVALID_LOGIN			= "UH_INVALID_LOGIN"		,
			ERROR_NOT_LOGGED_IN				= "NOT_LOGGED_IN"			,
			ERROR_OPERATION_NOT_PERMITTED	= "OPERATION_NOT_PERMITTED"	,
			ERROR_NULL_SELECTED				= "NULL_SELECTED"			,
			ERROR_UNKNOWN_RESPONSE			= "UNKNOWN_RESPONSE"		,
			ERROR_UH_ALREADY_ONLINE			= "UH_ALREADY_ONLINE"		,
			ERROR_UH_SERVER_FULL			= "UH_SERVER_FULL"			,
			ERROR_REG_REQUEST_FAILED		= "REG_REQUEST_FAILED"		,
			ERROR_QP_INVALID_PID			= "QP_INVALID_PID"			,
			ERROR_VD_INVALID_PERIOD			= "VD_INVALID_PERIOD"		,
			ERROR_VD_FEW_ENTRIES			= "VD_FEW_ENTRIES"
					;

	/**
	 * Name of the information message. This variable can be seen as
	 * an implementation-independent pointer to the
	 * implementation-dependent information message.
	 */
	String INFO_SELECT_OPTION				= "SELECT_OPTION"			,
			INFO_EXIT						= "EXIT"					,
			INFO_LOGIN						= "LOGIN"					,
			INFO_REGISTER					= "REGISTER"				,
			INFO_NEW_PASS_INFO				= "NEW_PASS_INFO"			,
			INFO_CURRENT_PASSWORD			= "CURRENT_PASSWORD"		,
			INFO_NEW_PASSWORD				= "NEW_PASSWORD"			,
			INFO_RE_NEW_PASSWORD			= "RE_NEW_PASSWORD"			,
			INFO_START_QUESTIONNAIRE		= "START_QUESTIONNAIRE"		,
			INFO_VIEW_STATISTICS			= "VIEW_STATISTICS"			,
			INFO_LOGOUT						= "LOGOUT"					,
			INFO_UH_ENTER_USERNAME			= "UH_ENTER_USERNAME"		,
			INFO_UH_ENTER_PASSWORD			= "UH_ENTER_PASSWORD"		,
			INFO_UH_UPDATE_PASSWORD			= "UH_UPDATE_PASSWORD"		,
			INFO_REG_CLINIC_NAME			= "REG_CLINIC_NAME"			,
			INFO_REG_USER_NAME				= "REG_USER_NAME"			,
			INFO_REG_USER_EMAIL				= "REG_USER_EMAIL"			,
			INFO_REG_REQUEST_SENT			= "REG_REQUEST_SENT"		,
			INFO_UI_UNFILLED				= "UI_UNFILLED"				,
			INFO_UI_FILLED					= "UI_FILLED"				,
			INFO_UI_FORM_OPTIONAL			= "UI_FORM_OPTIONAL"		,
			INFO_UI_FORM_BACK				= "UI_FORM_BACK"			,
			INFO_UI_FORM_NEXT				= "UI_FORM_NEXT"			,
			INFO_UI_FORM_PREVIOUS			= "UI_FORM_PREVIOUS"		,
			INFO_UI_FORM_CONTINUE			= "UI_FORM_CONTINUE"		,
			INFO_UI_FORM_FINISH				= "UI_FORM_FINISH"			,
			INFO_VD_SELECT_PERIOD           = "VD_SELECT_PREIOD"		,
			INFO_VD_SELECT_QUESTIONS		= "VD_SELECT_QUESTIONS"		,
			INFO_VD_DATE_FROM				= "VD_DATE_FROM"			,
			INFO_VD_DATE_TO					= "VD_DATE_TO"				,
			INFO_Q_PATIENT_FORENAME			= "Q_PATIENT_FORENAME"		,
			INFO_Q_PATIENT_SURNAME			= "Q_PATIENT_SURNAME"		,
			INFO_Q_PATIENT_PNR				= "Q_PATIENT_PNR"
					;

    String fallbackLocale = "en";
	
	/**
	 * Retrieves the error message associated with {@code errorName}
	 * in the current locale.
	 * 
	 * @param errorName The name of the error, defined by the static
	 * 		variables {@code ERROR_*}.
	 * 
	 * @return The error message for the current locale. If the
	 * 		message does not exist in the current locale then the
	 * 		message for the default locale is returned.
	 */
	String getError(String errorName);
	
	/**
	 * Retrieves the error message associated with {@code infoName}
	 * in the current locale.
	 * 
	 * @param infoName The name of the information, defined by the
	 * 		static variables {@code INFO_*}.
	 * 
	 * @return The information message for the current locale. If the
	 * 		message does not exist in the current locale then the
	 * 		message for the default locale is returned.
	 */
	String getInfo(String infoName);
}
