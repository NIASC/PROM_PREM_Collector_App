/** FormContainer.java
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
package applet.core.containers.form;

import applet.core.containers.Form;
import applet.core.interfaces.UserInterface;
import applet.core.interfaces.UserInterface.FormComponentDisplay;

/**
 * This class is the base class of data types that should be included
 * in a form. These data types can for example be a text field
 * question, single option question or multiple option question.
 * 
 * In addition to providing doubly-linked list properties, this class
 * includes an abstract method for drawing/presenting the
 * implementation of this class. This means that the
 * {@code UserInteface} has to provide a way of presenting the data
 * type of the implementation and the drawing/presenting method should
 * redirect the function call to the appropriate method in the
 * {@code UserInteface}.
 * 
 * @author Marcus Malmquist
 * 
 * @see UserInterface#getContainerDisplay(FormContainer)
 *
 */
public abstract class FormContainer
{
	/* Public */
	
	/**
	 * Creates a copy of this container. It is implementation
	 * dependent whether or not the references to the next and
	 * previous containers.
	 * 
	 * @return A copy of this container.
	 */
	@Override
	public abstract Object clone();
	
	/**
	 * Check if this {@code FormContainer} has been filled (i.e. an option
	 * has been selected or the field has text in it etc.).<br><br>
	 * If this form allows empty entries ({@code allowsEmpty() == true})
	 * then this method should return {@code true} if an (empty) entry has
	 * been set, but still be able to return {@code false} if an entry has
	 * not been set. This can be facilitated using {@code entrySet}.
	 * 
	 * @return {@code true} if this form has entry/entries.
	 * 		{@code false} if not.
	 * 
	 * @see #entrySet
	 */
	public abstract boolean hasEntry();
	
	/**
	 * Retrieves the entry/answer for this container.<br>
	 * If this form allows empty entries ({@code allowsEmpty() == true})
	 * then {@code hasEntry()} will always return {@code true}. A
	 * consequence of that is that this method may return null if this form
	 * has not entry.
	 * 
	 * @return This container's user entry.
	 * 
	 * @see #hasEntry()
	 */
	public abstract Object getEntry();
	
	/**
	 * Creates a displayable wrapper for this container and puts this
	 * container inside it.
	 * 
	 * NOTE: Since the object that is returned from this method should
	 * be defined in the implementation of {@code UserInterface}, this
	 * method should only be called in the implementation of the user
	 * interface to be of any use.
	 * 
	 * @param ui The instance of the active user interface.
	 * 
	 * @return A displayable wrapper object for the this container.
	 * 
	 * @see UserInterface#getContainerDisplay(FormContainer)
	 */
	public FormComponentDisplay getDisplayable(UserInterface ui)
	{
		return ui.getContainerDisplay(this);
	}
	
	/**
	 * Whether or not this container allows empty entries.
	 * 
	 * @return {@code true} if this container allows empty entries.
	 * 		{@code false} if not.
	 */
	public boolean allowsEmpty()
	{
		return allowEmpty;
	}
	
	/**
	 * Retrieves this container's statement.
	 * 
	 * @return This container's statement.
	 */
	public String getStatement()
	{
		return statement;
	}
	
	/**
	 * Retrieves this container's description.
	 * 
	 * @return This container's description.
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Retrieves the next FormContainer.
	 * 
	 * @return The next FormContainer.
	 * 
	 * @see Form
	 */
	public FormContainer getNextFC()
	{
		return nextFC;
	}
	
	/**
	 * Retrieves the previous FormContainer.
	 * 
	 * @return The previous FormContainer.
	 * 
	 * @see Form
	 */
	public FormContainer getPrevFC()
	{
		return prevFC;
	}
	
	/**
	 * Sets the next {@code FormContainer} to the supplied
	 * {@code FormContainer} and returns the {@code FormContainer} that
	 * currently is the next.
	 * 
	 * @param fc The {@code FormContainer} that will be the
	 * 		{@code FormContainer} after this.
	 * 
	 * @return The {@code FormContainer} that currently is the
	 * 		{@code FormContainer} after this.
	 * 
	 * @see Form
	 */
	public synchronized FormContainer setNextFC(FormContainer fc)
	{
		FormContainer old = nextFC;
		nextFC = fc;
		return old;
	}
	
	/**
	 * Sets the previous {@code FormContainer} to the supplied
	 * {@code FormContainer} and returns the {@code FormContainer} that
	 * currently is the previous.
	 * 
	 * @param fc The {@code FormContainer} that will be the
	 * 		{@code FormContainer} before this.
	 * 
	 * @return The {@code FormContainer} that currently is the
	 * 		{@code FormContainer} before this.
	 * 
	 * @see Form
	 */
	public synchronized FormContainer setPrevFC(FormContainer fc)
	{
		FormContainer old = prevFC;
		prevFC = fc;
		return old;
	}
	
	/* Protected */

	/**
	 * The next container in the {@code Form}.
	 * 
	 * @see Form
	 */
	protected FormContainer nextFC;
	/**
	 * The next container in the {@code Form}.
	 * 
	 * @see Form
	 */
	protected FormContainer prevFC;
	
	/**
	 * This flag can be used in conjunction with setting the entry for
	 * this container. The flag is can be used to prevent optional
	 * containers from getting overlooked when someone searches for empty
	 * containers while it should not prevent the form from being flagged
	 * as fully answered if the entry was skipped on purpose.<br><br>
	 * 
	 * The intended use for this flag is to set it to true when the entry
	 * is successfully filled (determined by the container itself), and
	 * then used in {@code hasEntry()} something like this:<br>
	 * {@code return entrySet && (allowEmpty || <conditions>)}<br>
	 * 
	 * @see #hasEntry()
	 */
	protected boolean entrySet;
	
	/**
	 * if the {@code FormContainer} should allow empty entries (i.e.
	 * the entry is optional) this should be {@code true}, else
	 * {@code false}.
	 */
	protected final boolean allowEmpty;
	
	/**
	 * The statement that the user should respond to. The statement should
	 * be relevant to the type of response requested from the user.
	 */
	protected final String statement;
	
	/**
	 * A more detailed description of the {@code #statement}.
	 */
	protected final String description;
	
	/**
	 * Initializes this container as either optional (allows empty) or
	 * mandatory (does not allow empty).
	 * 
	 * @param allowEmpty True if this container should allow empty
	 * 		entries.
	 * @param statement The statement that the user should respond to. The
	 * 		statement should be relevant to the type of response requested
	 * 		from the user.
	 * @param description A more detailed description of the
	 * 		{@code statement}.
	 */
	protected FormContainer(boolean allowEmpty, String statement, String description)
	{
		this.allowEmpty = allowEmpty;
		this.statement = statement;
		this.description = description;
		nextFC = prevFC = null;
	}
	
	/* Private */
}
