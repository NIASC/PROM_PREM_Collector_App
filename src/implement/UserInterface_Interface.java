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
package implement;

import java.util.HashMap;

import core.containers.Form;
import core.containers.form.Fcontainer;
import core.containers.form.FieldContainer;
import core.containers.form.SingleOptionContainer;
import implement.UserInterface_Interface.FormComponentDisplay;

/**
 * This interface contains the methods required by the core part of
 * this program to function. The purpose of this interface is to give
 * the freedom of choosing how the end user interacts with your
 * program.
 * 
 * @author Marcus Malmquist
 *
 */
public interface UserInterface_Interface
{
	public static final int ERROR = -1;
	public static final int EXIT = 0x100;
	public static final int LOGIN = 0x200;
	public static final int REGISTER = 0x400;
	/**
	 * Presents an error message to the user. The message
	 * should contain clear information about why the error
	 * was displayed.
	 * 
	 * @param s The error message.
	 */
	public void displayError(String s);
	
	/**
	 * Displays the login screen. It should contain an input
	 * field for entering login id & password as well as
	 * functionality to login or request registration
	 * 
	 * @return int LOGIN if the user wants to log in,
	 * 		REGISTER if the user wants to register,
	 * 		EXIT if the user wants to exit.
	 */
	public int displayLoginScreen();
	
	/**
	 * Requests login details (i.e. username & password) from the user.
	 * 
	 * @return A HashMap where the username is associated with 'usernameKey'
	 * 		and the password is associated with 'passwordKey'
	 */
	public HashMap<String, String> requestLoginDetails(String usernameKey, String passwordKey);
	
	/**
	 * Displays options and require the user to select one of them.
	 * 
	 * @param options The options
	 * @return int The id of the selected option
	 */
	public int selectOption(SingleOptionContainer options);
	
	/**
	 * Displays a form that the user has to fill in. The supplied
	 * FormContainer may contain forms that has already been filled
	 * or contains default values which should be displayed.
	 * 
	 * @param form A container for form entries.
	 */
	public void displayForm(FieldContainer form);
	
	/**
	 * Displays information to the user. For displaying error messages
	 * please use displayForm.
	 * 
	 * @param message The message to display to the user.
	 */
	public void displayMessage(String message);

	/**
	 * Closes the user interface (if it is open).
	 */
	void close();
	
	/**
	 * Presents a form that the user should fill in. The form has
	 * entries which follows the specifications of the abstract class
	 * core.containers.FormContainer. One of those specifications are
	 * a method to display themselves using the implementation of this
	 * class.
	 * 
	 * @param form The form container that the user should fill in.
	 * 
	 * @return True if the form was filled in early. False if the form
	 * 		was not fully filled in.
	 */
	public boolean presentForm(Form form);
	
	/**
	 * Creates an object that can store a single-option container.
	 * The function should return that object, or an Integer containing
	 * the ID of the selected option if the method itself presents the
	 * options from the single-option container.
	 * 
	 * @param soc The container that stores the options.
	 * 
	 * @return The object that stores the container and implements
	 * 		FormComponentDisplay.
	 */
	public <T extends FormComponentDisplay> T createSingleOption(SingleOptionContainer soc);
	
	/**
	 * Creates an object that can store a field container.
	 * The function should return that object, or a string containing
	 * the ID of the selected option if the method itself presents the
	 * options from the single-option container.
	 * 
	 * @param fc
	 * 
	 * @return The object that stores the container and implements
	 * 		FormComponentDisplay.
	 */
	public <T extends FormComponentDisplay> T createField(Fcontainer fc);

	public interface FormComponentDisplay
	{
		/**
		 * Retrieves the data from the user and fills the
		 * implementation's instance of FormContainer with that data.
		 */
		public void fillEntry();
		
		/**
		 * Checks if the entry has been filled.
		 * 
		 * @return True if the entry has been filled. False it not.
		 */
		public boolean entryFilled();
	}
}
