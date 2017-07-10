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
	 * 
	 * @see #addDate(int, int, int)
	 */
	public TimePeriodContainer(boolean allowEmpty, String statement)
	{
		super(allowEmpty);
		this.statement = statement;
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
		TimePeriodContainer tpc = new TimePeriodContainer(allowEmpty, statement);
		tpc.addDate((GregorianCalendar) lowerLimit.clone());
		tpc.addDate((GregorianCalendar) upperLimit.clone());
		return tpc;
	}

	@Override
	public GregorianCalendar[] getEntry()
	{
		return new GregorianCalendar[]{lowerSelected, upperSelected};
	}
	
	@Override
	public <T> boolean setEntry(T entry)
	{
		if (!(entry instanceof GregorianCalendar[]))
			return false;
		GregorianCalendar[] entries = (GregorianCalendar[]) entry;
		if (entries.length < 2)
			return false;
		int cmp = entries[0].compareTo(entries[1]);
		if (cmp > 0)
		{
			lowerSelected = entries[1];
			upperSelected = entries[0];
		}
		else
		{
			lowerSelected = entries[0];
			upperSelected = entries[1];
		}
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
	 * Retrieves the statement that the use should respond to (i.e. pick a
	 * date in response to the statement).
	 * 
	 * @return The statment for this container.
	 */
	public String getStatement()
	{
		return statement;
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
	
	private String statement;
	private int nDates;
	private GregorianCalendar upperLimit, lowerLimit, upperSelected, lowerSelected;
}
