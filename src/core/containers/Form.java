package core.containers;

public class Form
{
	private String entryKey;
	private String entryValue;
	
	/**
	 * Creates a form. the form is represented on the form of a key
	 * and a value. The key is typically the description of what you
	 * expect the value to be, e.g. in the form 'Age: 30' the key is
	 * 'Age' and the value is '30'.
	 * 
	 * @param key This form's key/description.
	 */
	public Form(String key)
	{
		entryKey = key;
	}
	
	/**
	 * Copies a Form.
	 * 
	 * @param form The Form to copy.
	 * 
	 * @return The copied Form.
	 */
	public static Form copy(final Form form)
	{
		if (form == null)
			return null;
		return new Form(form.getKey());
	}
	
	/**
	 * 
	 * @return This Form's key.
	 */
	public String getKey()
	{
		return entryKey;
	}
	
	/**
	 * 
	 * @return This Form's value.
	 */
	public String getValue()
	{
		return entryValue;
	}
	
	/**
	 * Sets the value of this form.
	 * 
	 * @param value The value to set for this form.
	 */
	public void setValue(String value)
	{
		entryValue = value;
	}
}
