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

public class Patient
{
	public Patient(String forename, String surname, String pnr)
	{
		this.forename = forename.trim().toLowerCase();
		this.surname = surname.trim().toLowerCase();
		this.pnr = pnr;
	}

	public String getForename()
	{
		return forename;
	}

	public String getSurname()
	{
		return surname;
	}

	public String getPersonalNumber()
	{
		return pnr;
	}
	
	private final String forename, surname, pnr;
}