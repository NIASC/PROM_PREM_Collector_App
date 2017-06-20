package core.containers;

import java.util.HashMap;

public class MessageContainer
{
	private HashMap<Integer, Message> messages;
	private HashMap<String, Integer> nameToCode;

	public MessageContainer()
	{
		messages = new HashMap<Integer, Message>();
		nameToCode = new HashMap<String, Integer>();
	}
	
	public void addMessage(Message message)
	{
		messages.put(message.getCode(), message);
		nameToCode.put(message.getName(), message.getCode());
	}
	
	public String getMessage(int code, String locale)
	{
		return messages.get(code).getMessage(locale);
	}
	
	public String getMessage(String name, String locale)
	{
		return getMessage(nameToCode.get(name), locale);
	}
}
