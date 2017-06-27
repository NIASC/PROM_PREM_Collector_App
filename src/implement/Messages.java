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
package implement;

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
public abstract class Messages
{
	public static MessageContainer error, info;
	public static final String LOCALE = "en";
	public static final String FALLBACK_LOCALE = "en";
	public static final String DATABASE_ERROR = "Database error.";
	
	/**
	 * Attempts to load messages from the database.
	 * 
	 * @return True if messages was successfully loaded. False if an
	 * 		error occurred while loading messages.
	 */
	public static boolean loadMessages()
	{
		error = new MessageContainer();
		info = new MessageContainer();
		
		Database db = new Database();
		db.connect();
		int errMsgLoadCode = db.getErrorMessages(error);
		int infoMsgLoadCode = db.getInfoMessages(info);
		db.disconnect();
		return errMsgLoadCode == Database_interface.QUERY_SUCCESS
				&& infoMsgLoadCode == Database_interface.QUERY_SUCCESS;
	}
	
	public static String ERROR_ = ""; 
}
