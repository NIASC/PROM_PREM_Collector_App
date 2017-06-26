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
package core.containers;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * This class is a data container for Messages. A message is a
 * structure which has a code/key/name and a set of messages in
 * different languages associated with different locales.
 * The purpose of this class is to encapsulate this information into a
 * class so it can be passed as an argument and be easily modifiable.
 * 
 * @author Marcus Malmquist
 *
 */
public class Message
{
	private int code;
	private String name;
	private HashMap<String, String> messages;
	
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
		messages = new HashMap<String, String>();
		messages.putAll(message);
	}
	
	/**
	 * Adds a message to this Message.
	 * This function can be used for adding messages for different
	 * locales to this Message. It is up to the database maintainers to
	 * make sure that the message has the same meaning for all locales.
	 * 
	 * @param message The message to add.
	 * @param locale The locale that this message was written for.
	 * @param replace Previously stored messages for the supplied
	 * 		locale will be replaced if replace is true and kept if
	 * 		replace is false.
	 * 
	 * @return If this Message already contains a message for the
	 * 		supplied locale and replace is true then that message will
	 * 		be returned. Else null will be returned.
	 */
	private String addMessage(String message, String locale,
			boolean replace)
	{
		if (replace || getMessage(locale) == null)
			return messages.put(locale, message);
		return null;
	}
	
	/**
	 * Retrieves the message for a given locale.
	 * 
	 * @param locale The locale of the message.
	 * 
	 * @return The message in the language specified by the locale.
	 * 		If no message exists for the supplied locale then null is
	 * 		returned.
	 */
	public String getMessage(String locale)
	{
		return messages.get(locale);
	}
	
	/**
	 * Puts the locales and their corresponding messages in a map
	 * where the locale is the key and the messages is the value.
	 * 
	 * @return A map that contains the locales and messages.
	 */
	public HashMap<String, String> getMessages()
	{
		HashMap<String, String> tmp = new HashMap<String, String>();
		tmp.putAll(messages);
		return tmp;
	}
	
	/**
	 * Merges the supplied Message with this Message by copying the
	 * messages and locales to this Message.
	 * If there are overlapping locales (and messages) then the
	 * messages contained in this Message are kept.
	 * 
	 * @param message
	 */
	public void merge(final Message message)
	{
		HashMap<String, String> msgMap = message.getMessages();
		for (Entry<String, String> e: msgMap.entrySet())
		{
			addMessage(e.getValue(), e.getKey(), false);
		}
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
