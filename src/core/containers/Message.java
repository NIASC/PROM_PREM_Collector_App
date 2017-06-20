package core.containers;

import java.util.HashMap;

public class Message
{
	private int code;
	private String name;
	private HashMap<String, String> message;
	
	public Message(int code, String name, HashMap<String, String> message)
	{
		this.code = code;
		this.name = name;
		this.message = new HashMap<String, String>();
		this.message.putAll(message);
	}
	
	public String getMessage(String locale)
	{
		return message.get(locale);
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getCode()
	{
		return code;
	}
}
