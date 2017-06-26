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

import core.containers.MessageContainer;
import core.containers.User;

/**
 * This interface contains the methods required by the core part of
 * this program to function. The purpose of this interface is to give
 * the freedom of choosing your own database along with the core part
 * of this program.
 * 
 * @author Marcus Malmquist
 *
 */
public interface Database_interface
{
	public static final int ERROR = -1;
	public static final int CONNECT_SUCCESS = 0x10;
	public static final int DISCONNECT_SUCCESS = 0x20;
	public static final int QUERY_SUCCESS = 0x40;
	public static final int FOUND = 0x80;
	public static final int NOT_FOUND = 0x100;

	/**
	 * Opens a connection to the database.
	 * 
	 * @return CONNECT_SUCCESS on successful connection,
	 * 		ERROR on failure to connect.
	 */
	public int connect();

	/**
	 * Closes an open connection to the database.
	 * 
	 * @return DISCONNECT_SUCCESS on successful disconnection,
	 * 		ERROR on failure to disconnect.
	 */
	public int disconnect();
	
	/**
	 * Adds a new user to the database.
	 * 
	 * @param username The username of the new user.
	 * @param password The (hashed) password of the new user.
	 * @param salt The salt that was used to hash the password.
	 * @param clinic The clinic ID that the new user belongs to.
	 * @param email The email of the new user.
	 * 
	 * @return QUERY_SUCCESS on successful update, ERROR on failure.
	 */
	public int addUser(String username,
			String password, String salt, int clinic, String email);

	/**
	 * Adds a new clinic to the database.
	 * 
	 * @param clinicName The name of the clinic.
	 * 
	 * @return QUERY_SUCCESS on successful update, ERROR on failure.
	 */
	public int addClinic(String clinicName);
	
	/**
	 * Collects the clinic names and id and places them in a HashMap.
	 * 
	 * @return A HashMap containing clinic id as keys and clinic names
	 * 		as values.
	 */
	public HashMap<Integer, String> getClinics();
	
	/**
	 * Collects the information about the user from the database.
	 * 
	 * @param username The name of the user to look for.
	 * @return If the user was found the instance of the user is
	 * 		returned else null.
	 */
	public User getUser(String username);

	/**
	 * Updates the user's password and salt to newPass and newSalt if
	 * oldPass matches the user's current password.
	 * 
	 * @param user The user that is updating it's password.
	 * @param oldPass The old (current) (unhashed) password.
	 * @param newPass The new password hashed using the new salt.
	 * @param newSalt The new salt.
	 * 
	 * @return The instance of the user with updated password. If the
	 * 		old password does not match the user's password then the
	 * 		password is not updated and null is returned.
	 */
	public User setPassword(User user, String oldPass, String newPass,
			String newSalt);
	
	/**
	 * Loads error messages from the database and puts them in a
	 * MessageContainer.
	 * 
	 * @param mc The (empty) message container to put error messages in.
	 * 
	 * @return QUERY_SUCCESS if the error messages were successfully
	 * 		loaded. ERROR if there was an error with the database.
	 */
	public int getErrorMessages(MessageContainer mc);
	
	/**
	 * Loads info messages from the database and puts them in a
	 * MessageContainer.
	 * 
	 * @param mc The (empty) message container to put info messages in.
	 * 
	 * @return QUERY_SUCCESS if the info messages were successfully
	 * 		loaded. ERROR if there was an error with the database.
	 */
	public int getInfoMessages(MessageContainer mc);
}
