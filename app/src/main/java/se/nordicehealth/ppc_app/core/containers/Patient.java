/*! Patient.java
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

/**
 * This class is a data container for Patient data. A patient has a set
 * of properties which should be defined in the database.
 * 
 * @author Marcus Malmquist
 *
 */
public class Patient
{
	/* Public */
	
	/**
	 * 
	 * @param forename The forename of this patient.
	 * @param surname The surname of this patient.
	 * @param pnr The personal number of this patient.
	 * 
	 * @throws NullPointerException If the supplied user is null.
	 */
	public Patient(String forename, String surname, String pnr) throws NullPointerException
	{
		this.forename = forename.trim().toLowerCase();
		this.surname = surname.trim().toLowerCase();
		this.pnr = pnr;
	}
	
	/**
	 * Retrieves this patient's forename.
	 * 
	 * @return This patient's forename
	 */
	public String getForename()
	{
		return forename;
	}
	
	/**
	 * Retrieves this patient's surname.
	 * 
	 * @return This patient's surname.
	 */
	public String getSurname()
	{
		return surname;
	}
	
	/**
	 * Retrieves this patient's personal id number.
	 * 
	 * @return This patient's personal number.
	 */
	public String getPersonalNumber()
	{
		return pnr;
	}
	
	/* Protected */
	
	/* Private */
	
	private final String forename, surname, pnr;
}
