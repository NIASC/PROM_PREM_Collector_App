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
package core.containers.form;

import java.util.HashMap;

/**
 * This class is a handles for Form objects. It allows you to group
 * several Form objects to create a complete Form that the user can
 * fill in.
 * The purpose of this class is to encapsulate this information into a
 * class so it can be passed as an argument and be easily modifiable.
 * 
 * @author Marcus Malmquist
 *
 */
public class FieldContainer
{
	private HashMap<Integer, Field> form;
	private HashMap<String, Integer> keyToID;
	private int formID;
	
	/**
	 * Initializes variables.
	 */
	public FieldContainer()
	{
		form = new HashMap<Integer, Field>();
		keyToID = new HashMap<String, Integer>();
		formID = 0;
	}
	
	/**
	 * Adds a Form to this container.
	 * 
	 * @param form The Form to add.
	 */
	public void addForm(Field form)
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
	public HashMap<Integer, Field> get()
	{
		return form;
	}
}