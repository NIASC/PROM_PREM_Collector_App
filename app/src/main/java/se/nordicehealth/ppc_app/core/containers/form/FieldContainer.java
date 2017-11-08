/*! FieldContainer.java
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
package se.nordicehealth.ppc_app.core.containers.form;


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
public class FieldContainer extends AreaContainer
{
	
	/**
	 * Initializes this container with form with the supplied
	 * statement.
	 * 
	 * @param allowEmptyEntries True if this container allows empty
	 * 		entry (answer/response).
	 * @param secretEntry True if the input should be hidden. Useful
	 * 		for entering sensitive information such as passwords.
	 * @param statement The statement to initialize this form with.
	 * @param description A more detailed description of the
	 * 		{@code statement}.
	 */
	public FieldContainer(boolean allowEmptyEntries, boolean secretEntry,
			String statement, String description)
	{
		super(allowEmptyEntries, statement, description, 64);
		secret = secretEntry;
	}

	@Override
	public Object clone()
	{
		FieldContainer fc = new FieldContainer(allowEmpty, secret,
				statement, description);
		fc.setEntry(entry);
		return fc;
	}
	
	/**
	 * Whether or not the input should be hidden.
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

	private final boolean secret;
}
