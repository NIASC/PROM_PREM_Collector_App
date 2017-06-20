package core.containers;

public class Form
{
	private String entryKey;
	private String entryValue;
	
	public Form(String key)
	{
		entryKey = key;
	}
	
	public static Form copy(final Form form)
	{
		return new Form(form.getKey());
	}
	
	public String getKey() { return entryKey; }
	public String getValue() { return entryValue; }
	
	public void setValue(String value) { entryValue = value; }
}
