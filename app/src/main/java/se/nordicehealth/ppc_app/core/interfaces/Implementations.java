/*! Implementations.java
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
package se.nordicehealth.ppc_app.core.interfaces;

import se.nordicehealth.ppc_app.implementation.Email_Registration;
import se.nordicehealth.ppc_app.implementation.Locale_se;
import se.nordicehealth.ppc_app.implementation.ResourceStrings;
import se.nordicehealth.ppc_app.implementation.ServletCommunication;
import se.nordicehealth.ppc_app.implementation.SHA_Encryption;

/**
 * This class acts as an interface between the implementation of the
 * interfaces and the core program itself. It contains constructor calls
 * to the classes that implements the interfaces.
 * There is no method for calling the constructor for the UserInterface
 * implementation because the program is started by the UserInterface
 * and should only use that instance.
 * 
 * @author Marcus Malmquist
 *
 */
public abstract class Implementations
{
	/**
	 * Constructor for the implementation of {@code Database}.
	 * 
	 * @return A new/running instance of the current implementation of
	 * 		{@code Database}.
	 * 
	 * @see Database
	 */
	public static Database Database()
	{
		return ServletCommunication.getDatabase();
	}
	
	/**
	 * Constructor for the implementation of {@code Encryption}.
	 * 
	 * @return A new instance of the current implementation of
	 * 		{@code Encryption}.
	 * 
	 * @see Encryption
	 */
	public static Encryption Encryption()
	{
		return new SHA_Encryption();
	}
	
	/**
	 * Constructor for the implementation of {@code Registration}.
	 * 
	 * @param ui The active instance of {@code UserInterface}.
	 * 
	 * @return A new instance of the current implementation of
	 * 		{@code Registration}.
	 * 
	 * @see Registration
	 * @see UserInterface
	 */
	public static Registration Registration(UserInterface ui)
	{
		return new Email_Registration(ui);
	}
	
	/**
	 * Constructor for the implementation of {@code Locale}.
	 * 
	 * @return A new/running instance of the current implementation of
	 * 		{@code Locale}.
	 * 
	 * @see Locale
	 */
	public static Locale Locale()
	{
		return Locale_se.getLocale();
	}

	public static Messages Messages()
	{
		return ResourceStrings.getMessages();
	}
	
	/* Protected */
	
	/* Private */
}
