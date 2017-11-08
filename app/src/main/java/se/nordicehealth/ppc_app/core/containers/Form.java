/*! Form.java
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
package se.nordicehealth.ppc_app.core.containers;

import se.nordicehealth.ppc_app.core.containers.form.FormContainer;

/**
 * This class is a container for the FiledContainer class and its
 * subclasses. It is used to create a form and add entries to it. This
 * class also provides many useful functions for navigating the form.
 * 
 * This class have been written with flexibility in mind. It should be
 * easy to add new types of form entries as long as those types inherit
 * from the FieldContainer class.
 * 
 * @author Marcus Malmquist
 *
 */
public class Form
{
	/* Public */
	
	/**
	 * Operation should be performed at the beginning of the list.
	 */
	public static final int AT_BEGIN = 0;
	
	/**
	 * Operation should be performed at the end of the list.
	 */
	public static final int AT_END = 1;
	
	/**
	 * Operation should be performed at the previous position in the list.
	 */
	public static final int AT_PREVIOUS = 2;
	
	/**
	 * Operation should be performed at the next position in the list.
	 */
	public static final int AT_NEXT = 3;
	
	/**
	 * Initializes an empty form.
	 */
	public Form()
	{
		currentFC = null;
	}
	
	/**
	 * Insert an entry to the form. An insertion can occur at the
	 * beginning, at the end, before or after the current position.
	 * 
	 * @param fc The {@code FormContainer} to insert.
	 * @param location The location to insert. The location is
	 * 		specified by the AT_* flags.
	 * 
	 * @see FormContainer
	 */
	public void insert(FormContainer fc, int location)
	{
		if (currentFC == null)
		{
			currentFC = fc;
			return;
		}
		if (duplicateEntry(fc))
			fc = (FormContainer) fc.clone(); // duplicates causes errors.
		
		final FormContainer tmp = currentFC;
		switch(location)
		{
		case AT_BEGIN:
			jumpTo(AT_BEGIN);
			insertBefore(fc);
			break;
		case AT_PREVIOUS:
			insertBefore(fc);
			break;
		case AT_END:
			jumpTo(AT_END);
			insertAfter(fc);
			break;
		case AT_NEXT:
			insertAfter(fc);
			break;
		default:
			break;
		}
		currentFC = tmp;
	}
	
	/**
	 * Delete an entry from the form. A deletion can occur at the
	 * beginning, at the end, before or after the current position.
	 * 
	 * @param location The location to delete. The location is
	 * 		specified by the AT_* flags.
	 * 
	 * @return The deleted {@code FormContainer}.
	 * 
	 * @see FormContainer
	 */
	public FormContainer delete(int location)
	{
		if (currentFC == null)
			return null;
		final FormContainer tmp = currentFC;
		FormContainer del = null;
		switch(location)
		{
		case AT_BEGIN:
			jumpTo(AT_BEGIN);
		case AT_PREVIOUS:
			del = deleteBefore();
			break;
		case AT_END:
			jumpTo(AT_END);
		case AT_NEXT:
			del = deleteAfter();
			break;
		default:
			break;
		}
		currentFC = tmp;
		return del;
	}
	
	/**
	 * Moves the current entry to the next and retrieves it. If the
	 * next entry is null then nothing is done and null is returned.
	 * 
	 * @return The new current entry, or null if there are no next
	 * 		entries.
	 * 
	 * @see FormContainer
	 */
	public synchronized FormContainer nextEntry()
	{
		if (currentFC == null || currentFC.getNextFC() == null)
			return null;
		return currentFC = currentFC.getNextFC();
	}

	/**
	 * Retrieves the current form entry.
	 * 
	 * @return The current entry in this form or null if this form has
	 * 		no entries.
	 * 
	 * @see FormContainer
	 */
	public synchronized FormContainer currentEntry()
	{
		return currentFC;
	}

	/**
	 * Moves the current entry to the previous and retrieves it. If
	 * the previous entry is null then nothing is done and null is
	 * returned.
	 * 
	 * @return The new current entry, or null if there are no previous
	 * 		entries.
	 * 
	 * @see FormContainer
	 */
	public synchronized FormContainer prevEntry()
	{
		if (currentFC == null || currentFC.getPrevFC() == null)
			return null;
		return currentFC = currentFC.getPrevFC();
	}
	
	/**
	 * Jumps to the entry at location. The location should be
	 * specified by the AT_* flags.
	 * 
	 * @param location The location to jump to.
	 */
	public void jumpTo(int location)
	{
		if (currentFC == null)
			return;
		switch(location)
		{
		case AT_BEGIN:
			while (prevEntry() != null);
			break;
		case AT_PREVIOUS:
			prevEntry();
			break;
		case AT_END:
			while (nextEntry() != null);
			break;
		case AT_NEXT:
			nextEntry();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Jumps a number of steps (&gt;0 forward, &lt;0 backward).
	 * 
	 * @param steps The number of steps to jump.
	 * 		Positive number of steps will move forward.
	 *  	Negative number of steps will move backward.
	 */
	public void jumpSteps(int steps)
	{
		if (steps < 0)
			jumpBackward(-steps);
		else if (steps > 0)
			jumpForward(steps);
	}
	
	/**
	 * Checks if this form has entries after the current entry.
	 * 
	 * @return {@code true} if current entry is the last one.
	 * 		{@code true} if there are more entries.
	 */
	public boolean endOfForm()
	{
		return currentFC == null || currentFC.getNextFC() == null;
	}
	
	/**
	 * Checks if this form has entries before the current entry.
	 * 
	 * @return {@code true} if current entry is the first one.
	 * 		{@code false} if there are previous entries.
	 */
	public boolean beginningOfForm()
	{
		return currentFC == null || currentFC.getPrevFC() == null;
	}
	
	/* Protected */
	
	/* Private */
	
	private FormContainer currentFC;
	
	/**
	 * Inserts the supplied FormContainer after current container. If
	 * the current container or the supplied container is {@code null}
	 * then nothing is done.
	 * 
	 * @param fc The {@code FormContainer} to insert.
	 * 
	 * @see FormContainer
	 */
	private synchronized void insertAfter(FormContainer fc)
	{
		if (fc == null || currentFC == null)
			return;
		fc.setPrevFC(currentFC);
		fc.setNextFC(currentFC.getNextFC());
		currentFC.setNextFC(fc);
		if (fc.getNextFC() != null)
			fc.getNextFC().setPrevFC(fc);
	}
	
	/**
	 * Inserts the supplied {@code FormContainer} before current
	 * container. If the current container or the supplied container
	 * is {@code null} then nothing is done.
	 * 
	 * @param fc The {@code FormContainer} to insert.
	 * 
	 * @see FormContainer
	 */
	private synchronized void insertBefore(FormContainer fc)
	{
		if (fc == null || currentFC == null)
			return;
		fc.setPrevFC(currentFC.getPrevFC());
		fc.setNextFC(currentFC);
		currentFC.setPrevFC(fc);
		if (fc.getPrevFC() != null)
			fc.getPrevFC().setNextFC(fc);
	}
	
	/**
	 * Deletes the {@code FormContainer} after the current. If the
	 * current {@code FormContainer} of the one after the current is
	 * {@code null}, nothing is done.
	 * 
	 * @return The {@code FormContainer} that was deleted.
	 * 
	 * @see FormContainer
	 */
	private synchronized FormContainer deleteAfter()
	{
		if (currentFC == null || currentFC.getNextFC() == null)
			return null;
		
		FormContainer del = currentFC.getNextFC();
		currentFC.setNextFC(del.getNextFC());
		if (del.getNextFC() != null)
			del.getNextFC().setPrevFC(currentFC);
		
		/* delete links to other containers. */
		del.setNextFC(null);
		del.setPrevFC(null);
		return del;
	}
	
	/**
	 * Deletes the {@code FormContainer} before the current. If the
	 * current {@code FormContainer} of the one before the current is
	 * {@code null}, nothing is done.
	 * 
	 * @return The {@code FormContainer} that was deleted.
	 * 
	 * @see FormContainer
	 */
	private synchronized FormContainer deleteBefore()
	{
		if (currentFC == null || currentFC.getPrevFC() == null)
			return null;
		
		FormContainer del = currentFC.getPrevFC();
		currentFC.setPrevFC(del.getPrevFC());
		if (del.getPrevFC() != null)
			del.getPrevFC().setNextFC(currentFC);
		
		/* delete links to other containers. */
		del.setNextFC(null);
		del.setPrevFC(null);
		return del;
	}
	
	/**
	 * Moves forward a number of steps. If the number of steps are
	 * &lt;= 0 then nothing is done.
	 * 
	 * @param steps The number of steps (&gt;0) to move forward.
	 */
	private void jumpForward(int steps)
	{
		if (steps > 0) {
			if (nextEntry() == null)
				return;
			jumpForward(steps - 1);
		}
	}
	
	/**
	 * Moves backward a number of steps. If the number of steps are
	 * &ld;= 0 then nothing is done.
	 * 
	 * @param steps The number of steps (&gt;0) to move backward.
	 */
	private void jumpBackward(int steps)
	{
        if (steps > 0) {
			if (prevEntry() == null)
				return;
			jumpBackward(steps - 1);
		}
	}
	
	/**
	 * Compares the {@code FormContainer} in this form and checks if
	 * {@code fc} is a reference to an object already in this form.
	 * 
	 * @param fc The {@code FormContainer} to check if it already
	 * 		exists in this form.
	 * 
	 * @return {@code true} if this form already contains the supplied
	 * 		container. {@code true} if not.
	 */
	private boolean duplicateEntry(FormContainer fc)
	{
		final FormContainer tmp = currentFC;
		jumpTo(AT_BEGIN);
		boolean duplicate = false;
		do
		{
			if (fc == currentEntry())
			{
				duplicate = true;
				break;
			}
		} while (nextEntry() != null);
		currentFC = tmp;
		return duplicate;
	}
}
