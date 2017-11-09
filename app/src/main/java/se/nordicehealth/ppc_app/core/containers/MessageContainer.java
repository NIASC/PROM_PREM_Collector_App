/*! MessageContainer.java
 * 
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
package se.nordicehealth.ppc_app.core.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;

import java.util.TreeMap;

/**
 * This class is a handle for Message objects. It allows you to group
 * several Message objects to make it simple to find the same type of
 * messages in a single object.
 * The purpose of this class is to encapsulate this information into a
 * class so it can be passed as an argument and be easily modifiable.
 * 
 * @author Marcus Malmquist
 *
 */
public class MessageContainer
{
	/* Public */

	/**
	 * Creates a container for Messages.
	 */
	public MessageContainer()
	{
		messages = new HashMap<>();
		codeToName = new TreeMap<>();
	}
	
	/**
	 * Adds a message to this container. If a message with the same
	 * name as the supplied message exists in this container then the
	 * supplied message will be merged with the existing message.
	 * 
	 * @param code The message code/id.
	 * @param name The message name/identifier.
	 * @param text The list of messages for different locales. The
	 * 		format should be &lt;locale, message&gt; e.g &lt;"en",
	 * 		"Hello"&gt;, &lt;"fr", "Bonjour"&gt; etc.
	 */
	public synchronized void addMessage(int code, String name,
			Map<String, String> text)
	{
		Message message = new Message(code, name, text);
		if (!messages.containsKey(name))
		{ // new message
			messages.put(name, message);
			codeToName.put(code, name);
		}
		else
		{ // message exists with a different locale
			messages.get(name).merge(message);
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
		return getMessage(codeToName.get(code), locale);
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
		if (messages.get(name) != null)
		{
			Message message = messages.get(name);
			String ret = message.getMessage(locale);
			if (ret != null)
				return ret;
			// message exists but not in the requested locale
			return message.getMessage(Implementations.Messages().fallbackLocale);
		}
		return null;
	}
	
	/* Protected */
	
	/* Private */
	
	/**
	 * format: <locale, message>
	 */
	private Map<String, Message> messages;
	/**
	 * format: <id, locale>
	 */
	private Map<Integer, String> codeToName;
	
	/**
	 * This class is a data container for Messages. A message is a
	 * structure which has a code/key/name and a set of messages in
	 * different languages associated with different locales.
	 * The purpose of this class is to encapsulate this information
	 * into a class so it can be passed as an argument and be easily
	 * modifiable.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private final class Message
	{
		int code;
		String name;
		/**
		 * format: <locale, message>
		 */
		Map<String, String> messages;
		
		/**
		 * Creates a {@code Message} that has a code, name and a list
		 * of messages for different locales (for multiple language
		 * support).
		 * 
		 * @param code The message code/id.
		 * @param name The message name/identifier.
		 * @param message The list of messages for different locales.
		 * 		The format should be <locale, message> e.g
		 * 		<"en", "Hello"> or <"fr", "Bonjour">.
		 */
		Message(int code, String name, Map<String, String> message)
		{
			this.code = code;
			this.name = name;
			messages = new HashMap<>();
			messages.putAll(message);
		}
		
		/**
		 * Retrieves the message for a given locale.
		 * 
		 * @param locale The locale of the message.
		 * 
		 * @return The message in the language specified by the locale.
		 * 		If no message exists for the supplied locale then null
		 * 		is returned.
		 */
		String getMessage(String locale)
		{
			return messages.get(locale);
		}
		
		/**
		 * Puts the locales and their corresponding messages in a map
		 * where the locale is the key and the messages is the value.
		 * 
		 * @return A map that contains the locales and messages.
		 */
		Map<String, String> getMessages()
		{
			Map<String, String> tmp = new HashMap<>();
			tmp.putAll(messages);
			return tmp;
		}
		
		/**
		 * Merges the supplied {@code Message} with this
		 * {@code Message} by copying the messages and locales to this
		 * Message.
		 * If there are overlapping locales (and messages) then the
		 * messages contained in this {@code Message} are kept.
		 * 
		 * @param message The {@code Message} to merge with this.
		 */
		void merge(final Message message)
		{
			Map<String, String> msgMap = message.getMessages();
			for (Entry<String, String> e: msgMap.entrySet())
			{
				addMessage(e.getValue(), e.getKey(), false);
			}
		}
		
		/**
		 * Adds a message to this {@code Message}.
		 * This function can be used for adding messages for different
		 * locales to this {@code Message}. It is up to the database
		 * maintainers to make sure that the message has the same
		 * meaning for all locales.
		 * 
		 * @param message The message to add.
		 * @param locale The locale that this message was written for.
		 * @param replace Previously stored messages for the supplied
		 * 		locale will be replaced if replace is {@code true} and
		 * 		kept if replace is {@code false}.
		 * 
		 * @return If this {@code Message} already contains a message
		 * 		for the supplied locale and replace is {@code true}
		 * 		then that message will be returned. Else {@code null}
		 * 		will be returned.
		 */
		String addMessage(String message, String locale,
				boolean replace)
		{
			if (replace || getMessage(locale) == null) {
				return messages.put(locale, message);
			}
			return null;
		}
	}
}
