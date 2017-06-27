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
package core.interfaces;

import core.containers.MessageContainer;

/**
 * This class should contain error and information messages fomr the
 * database.
 * The purpose of this class is to store messages from the database in
 * a local class so you don't have to query the database every time
 * you want to display a message to the user.
 * This implementation allows you do easily choose the language to
 * display messages for and quick access to messages.
 * 
 * @author Marcus Malmquist
 *
 */
public final class Messages
{
	private static MessageContainer error, info;
	private static final String LOCALE = "en";
	public static final String FALLBACK_LOCALE = "en";
	public static final String DATABASE_ERROR = "Database error.";
	
	/**
	 * Attempts to load messages from the database.
	 * 
	 * @return True if messages was successfully loaded. False if an
	 * 		error occurred while loading messages.
	 */
	public static final boolean loadMessages()
	{
		error = new MessageContainer();
		info = new MessageContainer();
		
		Database_interface db = Implementations.Database();
		db.connect();
		int errMsgLoadCode = db.getErrorMessages(error);
		int infoMsgLoadCode = db.getInfoMessages(info);
		db.disconnect();
		return errMsgLoadCode == Database_interface.QUERY_SUCCESS
				&& infoMsgLoadCode == Database_interface.QUERY_SUCCESS;
	}
	
	public static final String getError(String errorName)
	{
		return getMessage(error, errorName, LOCALE);
	}
	
	public static final String getInfo(String infoName)
	{
		return getMessage(info, infoName, LOCALE);
	}
	
	private static final String getMessage(
			MessageContainer mc, String messageName, String locale)
	{
		if (mc == null && !loadMessages())
			return null;
		return mc.getMessage(messageName, locale);
	}
	
	public static final String
	ERROR_UH_PR_PASSWORD_SIMPLE = "UH_PR_PASSWORD_SIMPLE",
	ERROR_UH_PR_INVALID_LENGTH = "UH_PR_INVALID_LENGTH",
	ERROR_UH_PR_MISMATCH_NEW = "UH_PR_MISMATCH_NEW",
	ERROR_UH_PR_INVALID_CURRENT = "UH_PR_INVALID_CURRENT",
	ERROR_UH_INVALID_LOGIN = "UH_INVALID_LOGIN",
	ERROR_NOT_LOGGED_IN = "NOT_LOGGED_IN",
	ERROR_OPERATION_NOT_PERMITTED = "OPERATION_NOT_PERMITTED",
	ERROR_NULL_SELECTED = "NULL_SELECTED",
	ERROR_UNKNOWN_RESPONSE = "UNKNOWN_RESPONSE"
	;
	
	public static final String
	INFO_SELECT_OPTION = "SELECT_OPTION",
	INFO_EXIT = "EXIT",
	INFO_LOGIN = "LOGIN",
	INFO_REGISTER = "REGISTER",
	INFO_NEW_PASS_INFO = "NEW_PASS_INFO",
	INFO_CURRENT_PASSWORD = "CURRENT_PASSWORD",
	INFO_NEW_PASSWORD = "NEW_PASSWORD",
	INFO_RE_NEW_PASSWORD = "RE_NEW_PASSWORD"
	;
}
