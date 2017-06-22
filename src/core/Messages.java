package core;

import core.containers.MessageContainer;
import implement.Database;
import implement.Database_interface;

public class Messages
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
}
