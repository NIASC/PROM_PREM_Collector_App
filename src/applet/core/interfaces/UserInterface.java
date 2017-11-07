/** UserInterface.java
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
package applet.core.interfaces;

import applet.core.containers.Form;
import applet.core.containers.ViewDataContainer;
import applet.core.containers.form.FormContainer;

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
	/* Public */
	
	/**
	 * Presents an error message to the user. The message should
	 * contain clear information about why the error was displayed.
	 * For displaying information messages please use
	 * {@link #displayMessage}.
	 * 
	 * @param message The error message.
	 * @param popup {@code true} if the error message should be
	 * 		displayed in a frame of its own. {@code false} if the
	 * 		message should be embedded in the main frame.
	 */
	public void displayError(String message, boolean popup);
	
	/**
	 * Displays a message to the user. For displaying error messages
	 * please use {@link #displayError}.
	 * 
	 * @param message The message to display to the user.
	 * @param popup {@code true} if the error message should be
	 * 		displayed in a frame of its own. {@code false} if the
	 * 		message should be embedded in the main frame.
	 */
	public void displayMessage(String message, boolean popup);
	
	/**
	 * Presents a {@code Form} that the user should fill in. The
	 * {@code Form} has entries which follows the specifications of
	 * {@code FormContainer}.
	 * 
	 * This interface does not guarantee that {@code multiple} will be
	 * used in the implementation but it should serve as a hint if the
	 * {@code Form} should have all of its entries displayed at the
	 * same time or not.
	 * 
	 * @param form The {@code Form} that the user should fill in.
	 * @param retfun The {@code FormContainer} that should be called
	 * 		when the form has been filled.
	 * @param multiple {@code true} if the UserInterface
	 * 		implementation should display all form entries at the same
	 * 		time. {@code false} if only one entry should be displayed
	 * 		at a time.
	 * 
	 * @return {@code true} if the form was displayed.
	 * 
	 * @see Form
	 * @see ReturnFunction
	 */
	public boolean presentForm(Form form, FormUtils retfun,
			boolean multiple);

	/**
	 * Presents the statistical data contained in {@code vdc}.
	 * 
	 * @param vdc The object which contains statistical data.
	 * 
	 * @return {@code true} if the content was displayed.
	 */
	public boolean presentViewData(ViewDataContainer vdc);
	
	/**
	 * Creates an object that can store a form container.
	 * 
	 * @param fc The {@code FormContainer} object.
	 * 
	 * @return A {@code FormComponentDisplay} object that that
	 * 		can display the {@code FormContainer}.
	 * 
	 * @see FormContainer
	 * @see FormComponentDisplay
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
		 * implementation's instance of {@code FormContainer} with
		 * that data.
		 * 
		 * @return {@code true} if all entries were filled.
		 * 		{@code false} if some were not filled.
		 * 
		 * @see FormContainer
		 */
		public boolean fillEntry();
		
		/**
		 * Checks if the entry has been filled. The entry should be
		 * filled by calling {@code fillEntry()}.
		 * 
		 * @return {@code true} if the entry has been filled.
		 * 		{@code false} it not.
		 * 
		 * @see #fillEntry()
		 */
		public boolean entryFilled();
	}
	
	/* Protected */
	
	/* Private */
}
