package implement;

/**
 * This interface contains the methods required by the core part of
 * this program to function. The purpose of this interface is to give
 * the freedom of choosing your own method of encryption (if any!).
 * 
 * @author Marcus Malmquist
 *
 */
public interface Encryption_interface
{
	/**
	 * Creates a hashed version of the input string.
	 * 
	 * @param s The string to be hashed
	 * @param salt The salt to be used for hashing
	 * 
	 * @return The hashed version of the string
	 */
	public String hashString(String s, String salt);
	
	/**
	 * Generates a new salt to be used for hashing.
	 * 
	 * @return A new salt
	 */
	public String getNewSalt();
}
