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

public class Patient
{
	/* Public */
	
	public Patient(String forename, String lastname, String pnr,
			final User user) throws NullPointerException
	{
		if (user == null)
			throw new NullPointerException();
		this.forename = forename;
		this.lastname = lastname;
		this.pnr = pnr;
		this.user = user;
		this.clinicID = user.getClinicID();
	}
	
	public String getForename()
	{
		return forename;
	}
	
	public String getLastname()
	{
		return lastname;
	}
	
	public String getPersonalNumber()
	{
		return pnr;
	}
	
	public User getUser()
	{
		return user;
	}
	
	public int getClinicID()
	{
		return clinicID;
	}
	
	/* Protected */
	
	/* Private */
	
	private String forename, lastname, pnr;
	private int clinicID;
	private final User user;
}
