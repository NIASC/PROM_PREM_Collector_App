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
package core.interfaces;

import implementation.Database;
import implementation.Encryption;
import implementation.Registration;
import implementation.UserInterface;

/**
 * This class acts as an interface between the implementation of the
 * interfaces and the core program itself. It contains constructor calls
 * to the classes that implements the interfaces.
 * 
 * @author Marcus Malmquist
 *
 */
public abstract class Implementations
{
	/**
	 * Constructor for the implementation of Database_interface.
	 * 
	 * @return A new instance of the current implementation of
	 * 		Database_interface.
	 */
	public static Database_interface Database()
	{
		return new Database();
	}
	
	/**
	 * Constructor for the implementation of Encryption_interface.
	 * 
	 * @return A new instance of the current implementation of
	 * 		Encryption_interface.
	 */
	public static Encryption_interface Encryption()
	{
		return new Encryption();
	}
	
	/**
	 * Constructor for the implementation of Registration_Interface.
	 * 
	 * @param ui The active user interface.
	 * 
	 * @return A new instance of the current implementation of
	 * 		Registration_Interface.
	 */
	public static Registration_Interface Registration(
			UserInterface_Interface ui)
	{
		return new Registration(ui);
	}
	
	/**
	 * Constructor for the implementation of Registration_Interface.
	 * 
	 * @return A new instance of the current implementation of
	 * 		UserInterface_Interface.
	 */
	public static UserInterface_Interface UserInterface()
	{
		return new UserInterface();
	}
}
