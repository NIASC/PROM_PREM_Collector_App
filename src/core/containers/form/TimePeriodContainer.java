/** TimePeriodContainer.java
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
package core.containers.form;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class contains data for selecting a time period. It contains an
 * upper and a lower bound for the time period and the ability to add dates
 * to this container. If the added dates pushed the current bounds the time
 * period will grow to fit the added date.
 * When all of the dates have been added the time period ranges form the
 * earliest occurring date to the latest occurring date.
 * 
 * @author Marcus Malmquist
 *
 */
public class TimePeriodContainer extends FormContainer
{
	/**
	 * Creates a time period container. The time period can be extended by
	 * adding dates (using {@code addDate(int, int, int)}).
	 * 
	 * @param allowEmpty Whether or not this container should require an
	 * 		entry / user response.
	 * @param statement The statement that the user should respond to.
	 * @param description A more detailed description of the
	 * 		{@code statement}
	 * @see #addDate(int, int, int)
	 */
	public TimePeriodContainer(boolean allowEmpty, String statement,
			String description)
	{
		super(allowEmpty, statement, description);
	}

	@Override
	public boolean hasEntry()
	{
		return allowEmpty
				|| (upperSelected != null && lowerSelected != null);
	}

	@Override
	public TimePeriodContainer copy()
	{
		TimePeriodContainer tpc = new TimePeriodContainer(
				allowEmpty, statement, description);
		tpc.addDate((GregorianCalendar) lowerLimit.clone());
		tpc.addDate((GregorianCalendar) upperLimit.clone());
		return tpc;
	}

	@Override
	public Calendar[] getEntry()
	{
		return new Calendar[]{lowerSelected, upperSelected};
	}
	
	/**
	 * Sets the upper and lower bounds of the time period. The date that
	 * occurs first will be set to the lower bound and the date that occurs
	 * last will be set to the upper bound, even if {@code upper} occurs
	 * before {@code upper}.
	 * 
	 * @param lower The lower bound for this time period.
	 * @param upper The upper bound for this time period.
	 * 
	 * @return true if the bounds were set.
	 */
	public boolean setEntry(GregorianCalendar lower,
			GregorianCalendar upper)
	{
		/* check for out of bounds */
		if (lower.compareTo(lowerLimit) < 0)
			lower = (GregorianCalendar) lowerLimit.clone();
		else if (lower.compareTo(upperLimit) > 0)
			lower = (GregorianCalendar) upperLimit.clone();

		if (upper.compareTo(lowerLimit) < 0)
			upper = (GregorianCalendar) lowerLimit.clone();
		else if (upper.compareTo(upperLimit) > 0)
			upper = (GregorianCalendar) upperLimit.clone();
		
		/* switch if wrong order */
		if (lower.compareTo(upper) > 0)
		{
			GregorianCalendar tmp = lower;
			lower = upper;
			upper = tmp;
		}
		
		lowerSelected = lower;
		upperSelected = upper;
		return true;
	}
	
	/**
	 * Adds a date to this container.<br>
	 * If this date pushes the current bounds then the bounds will be
	 * changed to fit the supplied date.
	 * 
	 * @param cal A calendar which contains the dates to add.
	 */
	public synchronized void addDate(GregorianCalendar cal)
	{
		if (cal == null)
			return;
		if (lowerLimit == null || lowerLimit.compareTo(cal) > 0)
			lowerLimit = (GregorianCalendar) cal.clone();
		if (upperLimit == null || upperLimit.compareTo(cal) < 0)
			upperLimit = (GregorianCalendar) cal.clone();
		nDates++;
	}
	
	/**
	 * Retrieves the lower limit for this time period container.
	 * 
	 * @return The earliest date that have been added to this container.
	 */
	public GregorianCalendar getLowerLimit()
	{
		return (GregorianCalendar) lowerLimit.clone();
	}
	
	/**
	 * Retrieves the upper limit for this time period container.
	 * 
	 * @return The latest date that have been added to this container.
	 */
	public GregorianCalendar getUpperLimit()
	{
		return (GregorianCalendar) upperLimit.clone();
	}
	
	/**
	 * Retrieves the number of dates that have been added to this
	 * container.
	 * 
	 * @return The number of dates that have been added to this container.
	 */
	public int getDateCount()
	{
		return nDates;
	}
	
	private int nDates;
	private Calendar upperLimit, lowerLimit, upperSelected, lowerSelected;
}
