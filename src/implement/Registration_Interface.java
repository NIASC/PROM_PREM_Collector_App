package implement;

/**
 * This interface contains the methods required by the core part of
 * this program to function. The purpose of this interface is to give
 * the freedom of choosing your own registration process and what
 * it should contain. Since the database implementation can also be
 * chosen freely you are not limited to a pre-defined user structure
 * defined by the database.
 * 
 * @author Marcus Malmquist
 *
 */
public interface Registration_Interface
{
	/**
	 * Presets a registration form to the user. The form should
	 * contain necessary information to be able to register the user.
	 * 
	 * Necessary information should at least include the clinic.
	 */
	public void registrationProcess();
}
