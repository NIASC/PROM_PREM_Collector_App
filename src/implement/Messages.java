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
}
