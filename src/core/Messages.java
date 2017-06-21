package core;

import core.containers.MessageContainer;
import implement.Database;
import implement.Database_interface;

public class Messages
{
	public static MessageContainer errorMessages, infoMessages;
	public static final String LOCALE = "en";
	public static final String DATABASE_ERROR = "Database error.";
	
	/**
	 * Attempts to load messages from the database.
	 * 
	 * @return True if messages was successfully loaded. False if an
	 * 		error occurred while loading messages.
	 */
	public static boolean loadMessages()
	{
		errorMessages = new MessageContainer();
		infoMessages = new MessageContainer();
		
		Database db = new Database();
		db.connect();
		int errMsgLoadCode = db.getErrorMessages(errorMessages);
		int infoMsgLoadCode = db.getErrorMessages(infoMessages);
		db.disconnect();
		return errMsgLoadCode == Database_interface.QUERY_SUCCESS
				&& infoMsgLoadCode == Database_interface.QUERY_SUCCESS;
	}
}
