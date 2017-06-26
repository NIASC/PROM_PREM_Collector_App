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

/**
 * This class is a data container for Forms. A form is a structure
 * which has a label/key/name and a value/entry/text.
 * The purpose of this class is to encapsulate this information into a
 * class so it can be passed as an argument and be easily modifiable.
 * 
 * @author Marcus Malmquist
 *
 */
public class Field
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
	public Field(String key)
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
	public static Field copy(final Field form)
	{
		if (form == null)
			return null;
		return new Field(form.getKey());
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
