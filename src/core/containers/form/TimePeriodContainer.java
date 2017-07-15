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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
		entries = new ArrayList<Calendar>();
	}

	@Override
	public boolean hasEntry()
	{
		return upperSel != null && lowerSel != null;
	}

	@Override
	public TimePeriodContainer copy()
	{
		TimePeriodContainer tpc = new TimePeriodContainer(
				allowEmpty, statement, description);
		tpc.addDate((Calendar) lowerLim.clone());
		tpc.addDate((Calendar) upperLim.clone());
		return tpc;
	}

	@Override
	public Calendar[] getEntry()
	{
		return new Calendar[]{lowerSel, upperSel};
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
	public boolean setEntry(Calendar lower,
			Calendar upper)
	{
		entrySet = true;
		/* check for out of bounds */
		if (lower.compareTo(lowerLim) < 0)
			lower = (Calendar) lowerLim.clone();
		else if (lower.compareTo(upperLim) > 0)
			lower = (Calendar) upperLim.clone();

		if (upper.compareTo(lowerLim) < 0)
			upper = (Calendar) lowerLim.clone();
		else if (upper.compareTo(upperLim) > 0)
			upper = (Calendar) upperLim.clone();
		
		/* switch if wrong order */
		if (lower.compareTo(upper) > 0)
		{
			Calendar tmp = lower;
			lower = upper;
			upper = tmp;
		}
		
		lowerSel = lower;
		upperSel = upper;
		nSelDates = 0;
		for (Iterator<Calendar> itr = entries.iterator(); itr.hasNext();)
		{
			Calendar cal = itr.next();
			if (cal.compareTo(lowerSel) >= 0 && cal.compareTo(upperSel) <= 0)
				nSelDates++;
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
	public synchronized void addDate(Calendar cal)
	{
		if (cal == null)
			return;
		if (lowerLim == null || lowerLim.compareTo(cal) > 0)
			lowerLim = (Calendar) cal.clone();
		if (upperLim == null || upperLim.compareTo(cal) < 0)
			upperLim = (Calendar) cal.clone();
		entries.add(cal);
		Collections.sort(entries);
	}
	
	/**
	 * Retrieves the lower limit for this time period container.
	 * 
	 * @return The earliest date that have been added to this container.
	 */
	public Calendar getLowerLimit()
	{
		return (Calendar) lowerLim.clone();
	}
	
	/**
	 * Retrieves the upper limit for this time period container.
	 * 
	 * @return The latest date that have been added to this container.
	 */
	public Calendar getUpperLimit()
	{
		return (Calendar) upperLim.clone();
	}
	
	/**
	 * Retrieves the number of dates that have been added to this
	 * container.
	 * 
	 * @return The number of dates that have been added to this container.
	 */
	public int getDateCount()
	{
		return entries.size();
	}
	
	public int getPeriodEntries()
	{
		return nSelDates;
	}
	
	private Calendar upperLim, lowerLim, upperSel, lowerSel;
	private List<Calendar> entries;
	private int nSelDates;
}
