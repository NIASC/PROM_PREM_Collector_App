package applet.core.interfaces;

import applet.core.containers.Form;

public interface FormUtils {
	
	/**
	 * This method should be used by the {@code UserInterface}
	 * to let the handler for this form parse the input.
	 * 
	 * @param form The {@code Form} that the
	 * 		{@code UserInterface} received.
	 * 
	 * @return A {@code RetFunContainer} containing information
	 * 		about if the function accepted the {@code Form}
	 * 		entries. The {@code RetFunContainer} also contains
	 * 		a functional interface {@code NextFunction} which
	 * 		should be called if the {@code Form} was accepted (and
	 * 		the function is not {@code null}). If the {@code Form}
	 * 		was not accepted by the function then {@code null} is
	 * 		returned.
	 * 
	 * @see UserInterface
	 * @see Form
	 */
	public RetFunContainer ValidateUserInput(Form form);
	
	/**
	 * This function should be called if the {@code valid}
	 * flag is {@code true}.
	 * 
	 * @see RetFunContainer#valid
	 */
	public void callNext();
	
	/**
	 * This class is a communication link between the implementation
	 * of this interface and the user interface. The implementation
	 * can set flags when it parses the form results and the user
	 * interface can check if the form was accepted.
	 * 
	 * @author Marcus Malmquist
	 */
	public class RetFunContainer
	{
		/**
		 * This flag should be set to true if {@code ReturnFunction}
		 * are ready to call {@code NextFunction}, meaning that it was
		 * satisfied with whatever it got. 
		 * 
		 * @see NextFunction#call()
		 */
		public boolean valid;
		
		/**
		 * This is a message that can be used to send an error
		 * message from {@code ReturnFunction} to its caller. This is
		 * typically useful if the {@code valid} flag is {@code false}
		 * to send a message to the user about why the flag was set to
		 * {@code false}.
		 * 
		 * @see #valid
		 */
		public String message;
	}
}
