package implement;

import java.util.HashMap;

import core.containers.form.FieldContainer;
import core.containers.form.SingleOptionContainer;

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
}
