package implement;

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
	 * @return A new salt
	 */
	public String getNewSalt();
}
