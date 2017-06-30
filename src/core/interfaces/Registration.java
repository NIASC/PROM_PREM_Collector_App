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

import core.containers.Form;

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
public interface Registration
{
	/* Public */
	
	/**
	 * Presets a registration form to the user. The form should
	 * contain necessary information to be able to register the user.
	 * 
	 * Necessary information should at least include the clinic and
	 * an email (since these are requires to initialize an instance of
	 * a user).
	 */
	public void registrationProcess();
	
	/* Protected */
	
	/* Private */
}
