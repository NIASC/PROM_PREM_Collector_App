package core.containers;

import implement.Encryption;

/**
 * This class is a data container for User data. A user has a set of
 * properties which should be defined in the database.
 * The purpose of this class is to encapsulate this information into a
 * class so it can be passed as an argument and be easily modifiable.
 * 
 * @author Marcus Malmquist
 *
 */
public class User
{
	private Encryption crypto;
	private String username, password, email, salt;
	private int clinicID;
	private boolean updatePass;
	
	/**
	 * Initializes a user with the given parameters.
	 * 
	 * @param clinicID The id of the clinic that the user belongs to.
	 * @param username The username of the user.
	 * @param password The (hashed) password of the user, as it appears
	 * 		in the database.
	 * @param email The email of the user.
	 * @param salt The salt that was used to produce the hashed
	 * 		password.
	 * @param updatePass True if the user is flagged for password
	 * 		update.
	 */
	public User(int clinicID, String username, String password, String email,
			String salt, boolean updatePass)
	{
		this.clinicID = clinicID;
		this.username = username;
		this.password = password;
		this.email = email;
		this.salt = salt;
		this.updatePass = updatePass;
		crypto = new Encryption();
	}
	
	/**
	 * 
	 * @return The user's username.
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * 
	 * @return The user's email address.
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * 
	 * @return The id of the clinic that the user is registered at.
	 */
	public int getClinicID()
	{
		return clinicID;
	}
	
	/**
	 * 
	 * @return True if the user is flagged to password update.
	 * 		False if not.
	 */
	public boolean getUpdatePassword()
	{
		return updatePass;
	}
	
	/**
	 * Hashes the unhashed password with the user's salt and compares
	 * the hashed password with the user's password.
	 * 
	 * @param unhashedPass The unhashed password to be compared with
	 * 		the user's password.
	 * 
	 * @return True if the hashed version of the password matches the
	 * 		user's password. False if not.
	 */
	public boolean passwordMatch(String unhashedPass)
	{
		return crypto.hashString(unhashedPass, salt).equals(password);
	}
}
