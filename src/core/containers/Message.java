package core.containers;

import java.util.HashMap;

public class Message
{
	private int code;
	private String name;
	private HashMap<String, String> message;
	
	/**
	 * Creates a Message that has a code, name and a list of messages
	 * for different locales (for multiple language support).
	 * 
	 * @param code The message code/id.
	 * @param name The message name/identifier.
	 * @param message The list of messages for different locales. The
	 * 		format should be <locale, message> e.g <"en", "Hello">
	 * 		or <"fr", "Bonjour">.
	 */
	public Message(int code, String name, HashMap<String, String> message)
	{
		this.code = code;
		this.name = name;
		this.message = new HashMap<String, String>();
		this.message.putAll(message);
	}
	
	/**
	 * Fetches the message for a given locale.
	 * 
	 * @param locale The locale e.g. 'en', 'fr', 'de'.
	 * 
	 * @return The message in the language specified by the locale.
	 */
	public String getMessage(String locale)
	{
		return message.get(locale);
	}
	
	/**
	 * 
	 * @return The message's name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * 
	 * @return The message's code.
	 */
	public int getCode()
	{
		return code;
	}
}
