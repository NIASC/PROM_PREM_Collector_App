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
package core;

import java.util.HashMap;
import java.util.Map;

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

	protected static final int ERROR = -1, SUCCESS = 0,
			SERVER_FULL = 2, ALREADY_ONLINE = 4;
	
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
	protected synchronized int addUser(UserHandle uh)
	{
		if (uh == null || uh.getUser() == null)
			return ERROR;
		if (userHandle.size() >= MAX_USERS)
			return SERVER_FULL;
		if (userHandle.containsKey(uh.getUser().getUsername()))
			return ALREADY_ONLINE;
		userHandle.put(uh.getUser().getUsername(), uh);
		return SUCCESS;
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
	protected synchronized boolean delUser(UserHandle uh)
	{
		if (uh == null || uh.getUser() == null
				|| !userHandle.containsKey(uh.getUser().getUsername()))
			return false;
		userHandle.remove(uh.getUser().getUsername());
		return true;
	}
	
	/* Private */
	
	private static UserManager manager;
	private static final int MAX_USERS;
	private Map<String, UserHandle> userHandle;

	/**
	 * Singleton class
	 */
	private UserManager()
	{
		userHandle = new HashMap<String, UserHandle>();
	}
	
	static
	{
		MAX_USERS = 2;
	}
}
