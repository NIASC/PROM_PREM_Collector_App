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
public class AreaContainer extends FormContainer
{
	
	/**
	 * Initializes this container with form with the supplied
	 * statement.
	 * 
	 * @param allowEmptyEntries True if this container allows empty
	 * 		entry (answer/response).
	 * @param statement The statement to initialize this form with.
	 * @param description A more detailed description of the
	 * 		{@code statement}.
	 */
	public AreaContainer(boolean allowEmptyEntries, String statement,
			String description)
	{
		super(allowEmptyEntries, statement, description);
		entrySet = false;
	}

	@Override
	public boolean hasEntry()
	{
		return entrySet && (allowEmpty || (entry != null && !entry.isEmpty()));
	}

	@Override
	public AreaContainer copy()
	{
		AreaContainer fc = new AreaContainer(allowEmpty, statement,
				description);
		fc.setEntry(entry);
		return fc;
	}
	
	@Override
	public String getEntry()
	{
		return entry;
	}
	
	/**
	 * Sets the entry (i.e. the user input in response to the field's
	 * statement) of this container's field. Setting the entry to null
	 * can be used to reset the field.
	 * 
	 * @param entry The user entry to set.
	 * 
	 * @return True if the entry was set
	 */
	public boolean setEntry(String entry)
	{
		entrySet = true;
		if (entry == null)
			this.entry = null;
		else
		{
			if (entry.trim().isEmpty() && !allowEmpty)
				return false;
			this.entry = entry.trim();
		}
		return true;
	}
	
	/* Protected */
	protected String entry;
	
	/* Private */
}
