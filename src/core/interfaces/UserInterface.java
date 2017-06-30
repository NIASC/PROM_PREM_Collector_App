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
package core.interfaces;

import core.containers.Form;
import core.containers.form.FormContainer;

/**
 * This interface contains the methods required by the core part of
 * this program to function. The purpose of this interface is to give
 * the freedom of choosing how the end user interacts with your
 * program.
 * 
 * @author Marcus Malmquist
 *
 */
public interface UserInterface
{
	/**
	 * Presents an error message to the user. The message
	 * should contain clear information about why the error
	 * was displayed.
	 * 
	 * @param s The error message.
	 */
	public void displayError(String s);
	
	/**
	 * Displays information to the user. For displaying error messages
	 * please use displayForm.
	 * 
	 * @param message The message to display to the user.
	 */
	public void displayMessage(String message);
	
	/**
	 * Presents a form that the user should fill in. The form has
	 * entries which follows the specifications of the abstract class
	 * core.containers.FormContainer. One of those specifications are
	 * a method to display themselves using the implementation of this
	 * class.
	 * 
	 * @param form The form container that the user should fill in.
	 * @param retfun The function that should be called when the form
	 * 		has been filled.
	 * @return True if the form was filled. False if the form was not
	 * 		fully filled in.
	 */
	public boolean presentForm(Form form, ReturnFunction retfun);
	
	/**
	 * Creates an object that can store a form container.
	 * 
	 * @param fc The form container object.
	 * 
	 * @return An object that that can display the form container.
	 */
	public FormComponentDisplay getContainerDisplay(FormContainer fc);

	/**
	 * This interface contains a set of functions that every class
	 * that presents form entry objects must implement.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	public interface FormComponentDisplay
	{
		/**
		 * Retrieves the data from the user and fills the
		 * implementation's instance of FormContainer with that data.
		 * 
		 * @return True if all entries were filled. False if some were
		 * 		not filled.
		 */
		public boolean fillEntry();
		
		/**
		 * Checks if the entry has been filled.
		 * 
		 * @return True if the entry has been filled. False it not.
		 */
		public boolean entryFilled();
	}

	/**
	 * This is a functional interface designed to allow functions to
	 * be passed as arguments to other function.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	@FunctionalInterface
	interface ReturnFunction
	{
		/**
		 * This function should be called when the UserInterface has
		 * displayed a form and should return with the results.
		 * 
		 * @param form The form that you received
		 * 
		 * @return TODO
		 */
		boolean call(Form form);
	}
}
