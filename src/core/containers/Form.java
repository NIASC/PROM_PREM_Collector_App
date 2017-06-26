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
package core.containers;

import core.containers.form.FormContainer;

/**
 * This class is a container for the FiledContainer class and its
 * subclasses. It is used to create a form and add entries to it. This
 * class also provides many useful functions for navigating the form.
 * 
 * This class have been written with great flexibility in mind. It
 * should be easy to add new types of form entries as long as those
 * types inherit from the FieldContainer class.
 * 
 * @author Marcus Malmquist
 *
 */
public class Form
{
	public static final int AT_BEGIN = 0, AT_END = 1,
			AT_PREVIOUS = 2, AT_NEXT = 3;
	
	private FormContainer currentFC;
	
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
	 * @param fc The FContainer to insert.
	 * @param location The location to insert. The location is
	 * 		specified by the AT_* flags.
	 */
	public void insert(FormContainer fc, int location)
	{
		if (currentFC == null)
		{
			currentFC = fc;
			return;
		}
		final FormContainer tmp = currentFC;
		switch(location)
		{
		case AT_BEGIN:
			while (currentFC.getPrevFC() != null)
				currentFC = currentFC.getPrevFC();
			insertBefore(fc);
			break;
		case AT_PREVIOUS:
			insertBefore(fc);
			break;
		case AT_END:
			while (currentFC.getNextFC() != null)
				currentFC = currentFC.getNextFC();
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
	 * Inserts the supplied FContainer after current container. If the
	 * current container or the supplied container is null then
	 * nothing is done.
	 * 
	 * @param fc The FContainer to insert.
	 */
	private void insertAfter(FormContainer fc)
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
	 * Inserts the supplied FContainer before current container. If
	 * the current container or the supplied container is null then
	 * nothing is done.
	 * 
	 * @param fc The FContainer to insert.
	 */
	private void insertBefore(FormContainer fc)
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
	 * Delete an entry from the form. An deletion can occur at the
	 * beginning, at the end, before or after the current position.
	 * 
	 * @param location The location to delete. The location is
	 * 		specified by the AT_* flags.
	 * 
	 * @return The deleted FContainer.
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
			while (currentFC.getPrevFC() != null)
				currentFC = currentFC.getPrevFC();
			del = deleteBefore();
			break;
		case AT_PREVIOUS:
			del = deleteBefore();
			break;
		case AT_END:
			while (currentFC.getNextFC() != null)
				currentFC = currentFC.getNextFC();
			del = deleteAfter();
			break;
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
	 * Deletes the FContainer after the current. If the current
	 * FContainer of the one after the current is null, nothing is
	 * done.
	 * 
	 * @return The FContainer that was deleted.
	 */
	private FormContainer deleteAfter()
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
	 * Deletes the FContainer before the current. If the current
	 * FContainer of the one before the current is null, nothing is
	 * done.
	 * 
	 * @return The FContainer that was deleted.
	 */
	private FormContainer deleteBefore()
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
	 * Moves the current entry to the next and retrieves it. If the
	 * next entry is null then nothing is done and null is returned.
	 * 
	 * @return The new current entry, or null if there are no next
	 * 		entries.
	 */
	public FormContainer nextEntry()
	{
		if (currentFC.getNextFC() == null)
			return null;
		return currentFC = currentFC.getNextFC();
	}

	/**
	 * Retrieves the current form entry.
	 * 
	 * @return The current entry in this form or null if this form has
	 * 		no entries.
	 */
	public FormContainer currentEntry()
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
	 */
	public FormContainer prevEntry()
	{
		if (currentFC.getPrevFC() == null)
			return null;
		return currentFC = currentFC.getPrevFC();
	}
	
	/**
	 * Jumps to the entry at location. The location should be specified
	 * by the AT_* flags.
	 * 
	 * @param location The location to jump to.
	 */
	public void jumpTo(int location)
	{
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
	 * Jumps a number of steps (>0 forward, <0 backward).
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
	 * Moves forward a number of steps. If the number of steps are
	 * <= 0 then nothing is done.
	 * 
	 * @param steps The number of steps (>0) to move forward.
	 */
	private void jumpForward(int steps)
	{
		if (steps <= 0)
			return;
		else
		{
			if (currentFC.getNextFC() == null)
				return;
			currentFC = currentFC.getNextFC();
			jumpForward(steps - 1);
		}
	}
	
	/**
	 * Moves backward a number of steps. If the number of steps are
	 * <= 0 then nothing is done.
	 * 
	 * @param steps The number of steps (>0) to move backward.
	 */
	private void jumpBackward(int steps)
	{
		if (steps <= 0)
			return;
		else
		{
			if (currentFC.getPrevFC() == null)
				return;
			currentFC = currentFC.getPrevFC();
			jumpBackward(steps - 1);
		}
	}
	
	/**
	 * Determines if all entries are filled. if a form is not filled
	 * the function will jump to to the first unfilled entry in this
	 * form if specified by jumpToUnfilled.
	 * 
	 * @param jumpToUnfilled True if the method should jump to the
	 * 		first unfilled entry in this form. False if it should not.
	 * 
	 * @return True if all entire are filled. False if there are
	 * 		unfilled entries.
	 */
	public boolean allEntriesFilled(boolean jumpToUnfilled)
	{
		FormContainer tmp = currentFC;
		jumpTo(AT_BEGIN);
		boolean entriesFilled;
		while(currentFC.hasEntry())
			if (nextEntry() == null)
				break;
		entriesFilled = nextEntry() == null;
		if (!jumpToUnfilled)
			currentFC = tmp;
		return entriesFilled;
	}
	
	/**
	 * Checks if this form has entries after the current entry.
	 * 
	 * @return True if current entry is the last one. False if there
	 * 		are more entries.
	 */
	public boolean endOfForm()
	{
		return currentFC.getNextFC() == null;
	}
	
	/**
	 * Checks if this form has entries before the current entry.
	 * 
	 * @return True if current entry is the first one. False if there
	 * 		are previous entries.
	 */
	public boolean beginningOfForm()
	{
		return currentFC.getPrevFC() == null;
	}
}
