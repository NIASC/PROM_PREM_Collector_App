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
package core.containers;

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
	 * @param user The user that added this patient.
	 * 
	 * @throws NullPointerException If the supplied user is null.
	 */
	public Patient(String forename, String surname, String pnr,
			final User user) throws NullPointerException
	{
		if (user == null)
			throw new NullPointerException();
		this.forename = forename;
		this.surname = surname;
		this.pnr = pnr;
		this.user = user;
		this.clinicID = user.getClinicID();
	}
	
	/**
	 * 
	 * @return This patient's forename
	 */
	public String getForename()
	{
		return forename;
	}
	
	/**
	 * 
	 * @return This patient's surname.
	 */
	public String getSurname()
	{
		return surname;
	}
	
	/**
	 * 
	 * @return This patient's personal number.
	 */
	public String getPersonalNumber()
	{
		return pnr;
	}
	
	/**
	 * 
	 * @return The user that added this patient.
	 */
	public User getUser()
	{
		return user;
	}
	
	/**
	 * 
	 * @return The clinic ID that this user belongs to. This will be
	 * 		the same clinic ID as that of the user who added this
	 * 		patient.
	 */
	public int getClinicID()
	{
		return clinicID;
	}
	
	/* Protected */
	
	/* Private */
	
	private final String forename, surname, pnr;
	private final int clinicID;
	private final User user;
}
