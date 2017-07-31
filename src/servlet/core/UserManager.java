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
package servlet.core;

import java.util.ArrayList;
import java.util.List;

import common.implementation.Constants;

/**
 * This class keeps track of which users are online and how many users
 * that can be online at any time. This class adds users to the list
 * of online users if they are allowed to log in, and removes them
 * from the list when they log out.
 * 
 * @author Marcus Malmquist
 *
 */
public class UserManager
{
	/* Public */

	@Override
	public final Object clone()
			throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	/**
	 * 
	 * @return The active instance of this class.
	 */
	public static synchronized UserManager getUserManager()
	{
		if (manager == null)
			manager = new UserManager();
		return manager;
	}
	
	/* Protected */
	
	/**
	 * Adds {@code uh} to the list of online users if they exist.
	 * 
	 * @param uh The {@code UserHandle} to add.
	 * 
	 * @return {@code true} if the {@code UserHandle} was added.
	 * 		{@code false} if not.
	 * 
	 * @see UserHandle
	 */
	public synchronized String addUser(String username)
	{
		if (username == null || username.isEmpty())
			return Constants.ERROR_STR;
		if (users.size() >= MAX_USERS)
			return Constants.SERVER_FULL_STR;
		if (users.contains(username))
			return Constants.ALREADY_ONLINE_STR;
		users.add(username);
		return Constants.SUCCESS_STR;
	}
	
	/**
	 * Removes {@code uh} from the list of online users if they exist.
	 * 
	 * @param uh The {@code UserHandle} to delete.
	 * 
	 * @return {@code true} if the user was deleted. False if the user
	 * 		did not exist in the list of users.
	 * 
	 * @see UserHandle
	 */
	public synchronized boolean delUser(String username)
	{
		if (username == null || username.isEmpty() || !users.contains(username))
			return false;
		users.remove(username);
		return true;
	}
	
	/* Private */
	
	private static UserManager manager;
	private static final int MAX_USERS;
	private List<String> users;

	/**
	 * Singleton class
	 */
	private UserManager()
	{
		users = new ArrayList<String>();
	}
	
	static
	{
		MAX_USERS = 2;
	}
}
