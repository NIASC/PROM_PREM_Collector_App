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
 * This class handles entry field object. It allows you to present a
 * statement or a question that you want the user to respond to. The
 * response can then be stored in this container and retrieved later.
 * 
 * @author Marcus Malmquist
 * 
 * @see FormContainer
 *
 */
public class FieldContainer extends FormContainer
{
	
	/**
	 * Initializes a field container that does not allow empty entries and
	 * does not have secret entries.
	 * @param statement The statement to initialize this form with.
	 */
	public FieldContainer(String statement)
	{
		this(false, false, statement);
	}
	
	/**
	 * Initializes this container with form with the supplied
	 * statement.
	 * 
	 * @param allowEmptyEntries True if this container allows empty
	 * 		entry (answer/response).
	 * @param secretEntry True if the input should be hidden. Useful
	 * 		for entering sensitive information such as passwords.
	 * @param statement The statement to initialize this form with.
	 */
	public FieldContainer(boolean allowEmptyEntries, boolean secretEntry,
			String statement)
	{
		super(allowEmptyEntries);
		field = new FieldEntry(statement);
		secret = secretEntry;
	}

	@Override
	public boolean hasEntry()
	{
		return field.entry != null
				&& (allowEmpty || !field.entry.isEmpty());
	}

	@Override
	public FieldContainer copy()
	{
		FieldContainer fc = new FieldContainer(allowEmpty, secret, field.statement);
		fc.setEntry(field.entry);
		return fc;
	}
	
	@Override
	public String getEntry()
	{
		return field.entry;
	}
	
	/**
	 * Sets the entry (i.e. the user input in response to the field's
	 * statement) of this container's field. Setting the entry to null
	 * can be used to reset the field.
	 * 
	 * @param entry The user entry to set.
	 * @return TODO
	 */
	@Override
	public <T extends Object> boolean setEntry(T entry)
	{
		if (entry == null)
			field.entry = null;
		else if (!(entry instanceof String))
			return false;
		else
		{
			String str = (String) entry;
			if (str.trim().isEmpty() && !allowEmpty)
				return false;
			field.entry = str.trim();
		}
		return true;
	}
	
	/**
	 * Retrieves the statement (i.e. the statement that you request
	 * the user to respond to)
	 * 
	 * @return This container's statement.
	 */
	public String getStatement()
	{
		return field.statement;
	}
	
	/**
	 * 
	 * @return {@code true} if this form's entry should be hidden.
	 * 		{@code false} if it should be displayed in plain text.
	 */
	public boolean isSecret()
	{
		return secret;
	}
	
	/* Protected */
	
	/* Private */

	private FieldEntry field;
	private final boolean secret;
	
	/**
	 * This class is a data container for field entries. It is
	 * designed with extensibility in mind and it should be possible
	 * to modify FieldContainer to be able to container several field
	 * entries using this class as a container for the entries.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private final class FieldEntry
	{
		
		/**
		 * This field's statement that the user should respond to.
		 */
		final String statement;
		
		/**
		 * This field's user entry/response.
		 */
		String entry;
		
		/**
		 * Creates a field entry. The field is represented on the form
		 * of a key and a value. The key is typically the description
		 * of what you expect the value to be, e.g. in the form
		 * 'Age: 30' the key is 'Age' and the value is '30'.
		 * 
		 * @param statement This field's key/description.
		 */
		FieldEntry(String statement)
		{
			this.statement = statement;
			entry = null;
		}
	}
}
