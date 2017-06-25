package core.containers.form;

import implement.UserInterface;

public abstract class FContainer
{
	protected FContainer nextFC, prevFC;
	
	/** Draws the objects contained in this container. */
	public abstract void draw(UserInterface ui);
	
	/**
	 * Retrieves the next FContainer.
	 * 
	 * @return The next FContainer.
	 */
	public FContainer getNextFC()
	{
		return nextFC;
	}
	
	/**
	 * Retrieves the previous FContainer.
	 * 
	 * @return The previous FContainer.
	 */
	public FContainer getPrevFC()
	{
		return prevFC;
	}
	
	/**
	 * Sets the next FContainer to the supplied FContainer and returns
	 * the FContainer that currently is the next.
	 * 
	 * @param fc The FContainer that will be the FContainer after this.
	 * 
	 * @return The FContainer that currently is the FContainer after
	 * 		this.
	 */
	public FContainer setNextFC(FContainer fc)
	{
		FContainer old = nextFC;
		nextFC = fc;
		return old;
	}
	
	/** sets the previous FContainer and returns the previous previous */
	public FContainer setPrevFC(FContainer fc)
	{
		FContainer old = prevFC;
		prevFC = fc;
		return old;
	}
	
}
