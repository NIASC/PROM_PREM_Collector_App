package core.containers;

import core.containers.form.FContainer;

public class Form
{
	public static final int AT_FIRST = 0, AT_LAST = 1,
			AT_BEFORE = 2, AT_AFTER = 3;
	
	private FContainer currentFC;
	
	public Form() {
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
	public void insert(FContainer fc, int location)
	{
		if (currentFC == null)
		{
			currentFC = fc;
			return;
		}
		final FContainer tmp = currentFC;
		switch(location)
		{
		case AT_FIRST:
			while (currentFC.getPrevFC() != null)
				currentFC = currentFC.getPrevFC();
		case AT_BEFORE:
			insertBefore(fc);
			break;
		case AT_LAST:
			while (currentFC.getNextFC() != null)
				currentFC = currentFC.getNextFC();
		case AT_AFTER:
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
	private void insertAfter(FContainer fc)
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
	private void insertBefore(FContainer fc)
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
	public FContainer delete(int location)
	{
		if (currentFC == null)
			return null;
		final FContainer tmp = currentFC;
		FContainer del = null;
		switch(location)
		{
		case AT_FIRST:
			while (currentFC.getPrevFC() != null)
				currentFC = currentFC.getPrevFC();
		case AT_BEFORE:
			del = deleteBefore();
			break;
		case AT_LAST:
			while (currentFC.getNextFC() != null)
				currentFC = currentFC.getNextFC();
		case AT_AFTER:
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
	private FContainer deleteAfter()
	{
		if (currentFC == null || currentFC.getNextFC() == null)
			return null;
		
		FContainer del = currentFC.getNextFC();
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
	private FContainer deleteBefore()
	{
		if (currentFC == null || currentFC.getPrevFC() == null)
			return null;
		
		FContainer del = currentFC.getPrevFC();
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
	public FContainer nextEntry()
	{
		if (currentFC.getNextFC() == null)
			return null;
		return currentFC = currentFC.getNextFC();
	}
	
	/**
	 * Moves the current entry to the previous and retrieves it. If
	 * the previous entry is null then nothing is done and null is
	 * returned.
	 * 
	 * @return The new current entry, or null if there are no previous
	 * 		entries.
	 */
	public FContainer prevEntry()
	{
		if (currentFC.getPrevFC() == null)
			return null;
		return currentFC = currentFC.getPrevFC();
	}
	
	/** jumps to entry n */
	public void jumpTo(int location)
	{
		switch(location)
		{
		case AT_FIRST:
			while (prevEntry() != null);
			break;
		case AT_BEFORE:
			prevEntry();
			break;
		case AT_LAST:
			while (nextEntry() != null);
			break;
		case AT_AFTER:
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
}
