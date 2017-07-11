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

import core.containers.Form;
import core.interfaces.UserInterface;
import core.interfaces.UserInterface.FormComponentDisplay;

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
	 * Check if this {@code FormContainer} has been filled (i.e. an option
	 * has been selected or the field has text in it etc.).<br>
	 * If this form allows empty entries ({@code allowsEmpty() == true})
	 * then this method will always return true.
	 * 
	 * @return {@code true} if this form has entry/entries.
	 * 		{@code false} if not.
	 */
	public abstract boolean hasEntry();
	
	/**
	 * Creates a copy of this container. It is implementation
	 * dependent whether or not the references to the next and
	 * previous containers.
	 * 
	 * @return A copy of this container.
	 */
	public abstract FormContainer copy();
	
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
	 * Sets/unsets/adds an entry for this form.
	 * 
	 * @param entry The entry object to set.
	 * 
	 * @return {@code true} if the entry was set to the new state.
	 * 		{@code false} if not.
	 */
	public abstract <T extends Object> boolean setEntry(T entry);
	
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
	 * 
	 * @return True if this container allows empty entries. False if
	 * 		not.
	 */
	public boolean allowsEmpty()
	{
		return allowEmpty;
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
	 * Sets the next FormContainer to the supplied FormContainer and
	 * returns the FormContainer that currently is the next.
	 * 
	 * @param fc The FormContainer that will be the FormContainer
	 * 		after this.
	 * 
	 * @return The FormContainer that currently is the FormContainer
	 * 		after this.
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
	 * Sets the previous FormContainer to the supplied FormContainer
	 * and returns the FormContainer that currently is the previous.
	 * 
	 * @param fc The FormContainer that will be the FormContainer
	 * 		before this.
	 * 
	 * @return The FormContainer that currently is the FormContainer
	 * 		before this.
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
	 * The next container in the {@code Form}
	 * 
	 * @see Form
	 */
	protected FormContainer nextFC;
	/**
	 * The next container in the {@code Form}
	 * 
	 * @see Form
	 */
	protected FormContainer prevFC;
	/**
	 * if the {@code FormContainer} should allow empty entries (i.e.
	 * the entry is optional) this should be {@code true}, else
	 * {@code false}.
	 */
	protected final boolean allowEmpty;
	
	/**
	 * Initializes this container as either optional (allows empty) or
	 * mandatory (does not allow empty).
	 * 
	 * @param allowEmpty True if this container should allow empty
	 * 		entries.
	 */
	protected FormContainer(boolean allowEmpty)
	{
		this.allowEmpty = allowEmpty;
		nextFC = prevFC = null;
	}
	
	/* Private */
}
