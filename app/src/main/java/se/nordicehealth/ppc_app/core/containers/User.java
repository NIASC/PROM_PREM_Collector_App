/*! User.java
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
package se.nordicehealth.ppc_app.core.containers;

import se.nordicehealth.ppc_app.core.interfaces.Encryption;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;

/**
 * This class is a data container for User data. A user has a set of
 * properties which should be defined in the database.
 * 
 * @author Marcus Malmquist
 *
 */
public class User
{
	/* Public */
	
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
	public User(int clinicID, String username, String password,
			String email, String salt, boolean updatePass)
	{
		this.clinicID = clinicID;
		this.username = username;
		this.password = password;
		this.email = email;
		this.salt = salt;
		this.updatePass = updatePass;
		crypto = Implementations.Encryption();
	}
	
	@Override
	public Object clone()
	{
		return new User(clinicID, username, password,
				email, salt, updatePass);
	}
	
	/**
	 * Retrieves the user's username.
	 * 
	 * @return The user's username.
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * Retrieves the user's email.
	 * 
	 * @return The user's email address.
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * Retrieves the user's clinic id.
	 * 
	 * @return The id of the clinic that the user is registered at.
	 */
	public int getClinicID()
	{
		return clinicID;
	}
	
	/**
	 * Retrieves the user's password update flag.
	 * 
	 * @return {@code true} if the user is flagged to password update.
	 * 		{@code false} if not.
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
	 * @return {@code true} if the hashed version of the password
	 * 		matches the user's password. {@code false} if not.
	 */
	public boolean passwordMatch(String unhashedPass)
	{
		return crypto.hashString(unhashedPass, salt).equals(password);
	}
	
	/**
	 * Hashes {@code unhashedPass} with the users salt.
	 * 
	 * @param unhashedPass The string to hash.
	 * 
	 * @return The hashed version of the string.
	 */
	public String hashWithSalt(String unhashedPass)
	{
		return crypto.hashString(unhashedPass, salt);
	}
	
	/* Protected */
	
	/* Private */

	private final Encryption crypto;
	private final String username, password, email, salt;
	private final int clinicID;
	private final boolean updatePass;
}
