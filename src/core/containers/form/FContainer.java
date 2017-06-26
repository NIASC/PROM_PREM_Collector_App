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

import implement.UserInterface;

/**
 * This class is the base class of data types that should be included
 * in a form. These data types can for example be a text field question,
 * single option question or multiple option question.
 * 
 * In addition to providing doubly-linked list properties, this class
 * includes an abstract method for drawing/presenting the
 * implementation of this class. This means that the user interface has
 * to provide a way of presenting the data type of the implementation
 * and the drawing/presenting method should redirect the function call
 * to the appropriate method in the user interface.
 * 
 * @author Marcus Malmquist
 *
 */
public abstract class FContainer
{
	protected FContainer nextFC, prevFC;
	
	/**
	 * Draws the objects contained in this container.
	 * NOTE: Since the object that is returned from this method should
	 * be defined in the implementation of the user interface, this
	 * method should only be called in the implementation of the user
	 * interface to be of any use.
	 * 
	 * The drawing method should redirect the call to this method to
	 * the appropriate method provided by the user interface and
	 * return whatever that method returns.
	 * 
	 * @param ui The instance of the active user interface.
	 * 
	 * @return The (subclass of) Object that is returned from the
	 * 		method in the UserInterface class
	 */
	public abstract Object draw(UserInterface ui);
	
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
