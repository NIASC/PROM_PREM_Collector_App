package core.containers;

import java.util.HashMap;

public class FormContainer
{
	private HashMap<Integer, Form> form;
	private HashMap<String, Integer> keyToID;
	private int formID;
	
	public FormContainer()
	{
		form = new HashMap<Integer, Form>();
		keyToID = new HashMap<String, Integer>();
		formID = 0;
	}
	
	/**
	 * Adds a Form to this container.
	 * 
	 * @param form The Form to add.
	 */
	public void addForm(Form form)
	{
		if (form == null)
			return;
		this.form.put(formID, form);
		keyToID.put(form.getKey(), formID++);
	}
	
	/**
	 * Retrieves the value from the Form associated with the
	 * supplied id.
	 * 
	 * @param id The form's id.
	 * @return The value contained in the form associated with
	 * 		the id.
	 */
	public String getValue(int id)
	{
		return form.get(id).getValue();
	}

	
	/**
	 * Retrieves the value from the Form associated with the
	 * supplied key.
	 * 
	 * @param key The form's name.
	 * @return The value contained in the form associated with
	 * 		the key.
	 */
	public String getValue(String key)
	{
		return getValue(keyToID.get(key));
	}
	
	/**
	 * Retrieves the ID of the form with the given key.
	 * 
	 * @param key The form's name.
	 * @return The ID of the form associated with the key.
	 */
	public int getID(String key)
	{
		return keyToID.get(key);
	}
	
	/**
	 * Puts the Form data contained in this class in an Integer-Form
	 * map.
	 * 
	 * @return A map containing a map-id and a Form.
	 */
	public HashMap<Integer, Form> get()
	{
		return form;
	}
}