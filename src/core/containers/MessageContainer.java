package core.containers;

import java.util.HashMap;

public class MessageContainer
{
	private HashMap<Integer, Message> messages;
	private HashMap<String, Integer> nameToCode;

	/**
	 * Creates a container for Messages.
	 */
	public MessageContainer()
	{
		messages = new HashMap<Integer, Message>();
		nameToCode = new HashMap<String, Integer>();
	}
	
	/**
	 * Adds a Message to this container.
	 * 
	 * @param message The Message to add to this container.
	 */
	public void addMessage(Message message)
	{
		if (message == null)
			return;
		messages.put(message.getCode(), message);
		nameToCode.put(message.getName(), message.getCode());
	}
	
	/**
	 * Fetches the message associated with the message code for a
	 * 		given locale.
	 * 
	 * @param code The code for the message that is sought.
	 * @param locale The locale that specifies the language of the
	 * 		message, e.g. 'en' for English.
	 * 
	 * @return The message associated with the code and in the
	 * 		language specified by the locale.
	 */
	public String getMessage(int code, String locale)
	{
		return messages.get(code).getMessage(locale);
	}
	
	/**
	 * Fetches the message associated with the message name for a
	 * 		given locale.
	 * 
	 * @param name The name of the message that is sought.
	 * @param locale The locale that specifies the language of the
	 * 		message, e.g. 'en' for English.
	 * 
	 * @return The message associated with the name and in the
	 * 		language specified by the locale.
	 */
	public String getMessage(String name, String locale)
	{
		return getMessage(nameToCode.get(name), locale);
	}
}
