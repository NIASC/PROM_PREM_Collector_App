package core.containers;

import java.util.HashMap;

import core.Messages;

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
	 * Adds a Message to this container. If a message with the same
	 * code as the supplied message exists in this container then the
	 * supplied message will be merged with the existing message.
	 * 
	 * @param message The Message to add to this container.
	 */
	public void addMessage(Message message)
	{
		if (message == null)
			return;
		if (messages.get(message.getCode()) == null)
		{ // new message
			messages.put(message.getCode(), message);
			nameToCode.put(message.getName(), message.getCode());
		}
		else
		{ // message exists with a different locale
			messages.get(message.getCode()).merge(message);
		}
	}
	
	/**
	 * Retrieves the message associated with the message code for a
	 * 		given locale.
	 * 
	 * @param code The code for the message that is sought.
	 * @param locale The locale that specifies the language of the
	 * 		message, e.g. 'en' for English.
	 * 
	 * @return The message associated with the code and in the
	 * 		language specified by the locale.
	 * 		If no messages exists for the supplied locale then the
	 * 		message will be returned for the default locale.
	 * 		If no message exists for the supplied code then null is
	 * 		returned.
	 */
	public String getMessage(int code, String locale)
	{
		if (messages.get(code) != null)
		{
			Message message = messages.get(code);
			String ret = message.getMessage(locale);
			if (ret != null)
				return ret;
			// message exists but not in the requested locale
			return message.getMessage(Messages.FALLBACK_LOCALE);
		}
		return null;
	}
	
	/**
	 * Retrieves the message associated with the message name for a
	 * 		given locale.
	 * 
	 * @param name The name of the message that is sought.
	 * @param locale The locale that specifies the language of the
	 * 		message, e.g. 'en' for English.
	 * 
	 * @return The message associated with the name and in the
	 * 		language specified by the locale.
	 * 		If no messages exists for the supplied locale then the
	 * 		message will be returned for the default locale.
	 * 		If no message exists for the supplied code then null is
	 * 		returned.
	 */
	public String getMessage(String name, String locale)
	{
		return getMessage(nameToCode.get(name), locale);
	}
}
