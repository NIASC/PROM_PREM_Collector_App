package core;

import core.containers.MessageContainer;
import implement.Database;

public class Messages
{
	public static MessageContainer errorMessages, infoMessages;
	public static final String LOCALE = "en";
	public static final String DATABASE_ERROR = "Database error.";
	
	public static void loadMessages()
	{
		errorMessages = new MessageContainer();
		errorMessages = new MessageContainer();
		
		Database db = new Database();
		db.connect();
		db.getErrorMessages(errorMessages);
		db.disconnect();
	}
}
