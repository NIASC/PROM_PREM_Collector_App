/*! SliderContainer.java
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
package se.nordicehealth.ppc_app.core.containers.form;

/**
 * This class handles slider objects. It allows you to present a
 * statement and a slider bar that the user should move in response
 * to the statement. The response is stored in this container and can
 * be retrieved later.
 * 
 * @author Marcus Malmquist
 * 
 * @see FormContainer
 *
 */
public class SliderContainer extends FormContainer
{
	/* Public */
	
	/**
	 * Creates a slider container. The slider container contains a
	 * slider  object which is a slider with an upper and a lower
	 * bound as well as a slider position (value). The slider can be
	 * moved between its bounds.
	 * 
	 * @param allowEmptyEntries {@code true} if the entry is optional.
	 * @param statement The statement to initialize this form with.
	 * 		The statement is used to explain what the slider value
	 * 		means.
	 * @param description A more detailed description of the
	 * 		{@code statement}
	 * @param min The upper bound of the slider.
	 * @param max The lower bound of the slider.
	 * @throws NullPointerException If {@code max} or {@code min} is
	 * 		{@code null}.
	 */
	public SliderContainer(boolean allowEmptyEntries,
			String statement, String description,
			Integer min, Integer max)
					throws NullPointerException
	{
		super(allowEmptyEntries, statement, description);
		if (min == null || max == null)
			throw new NullPointerException();
		value = null;
		lower = min;
		upper = max;
	}

	@Override
	public boolean hasEntry()
	{
		return entryIsSet && (allowEmpty || value != null);
	}

	@Override
	public Integer getEntry()
	{
		return value;
	}
	
	/**
	 * Sets the entry (i.e. the user input in response to the field's
	 * statement) of this container's field. Setting the entry to
	 * {@code null} can be used to reset the field.
	 * 
	 * @param entry The user entry to set.
	 * 
	 * @return {@code true} if the value was set. {@code false} if not.
	 */
	public boolean setEntry(int entry)
	{
		entryIsSet = true;
		if (withinBounds(entry))
		{
			value = entry;
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieves the upper bound for this slider.
	 * 
	 * @return The upper bound of this slider.
	 */
	public int getUpperBound()
	{
		return upper;
	}
	
	/**
	 * Retrieves the lower bound for this slider.
	 * 
	 * @return The upper bound of this slider.
	 */
	public int getLowerBound()
	{
		return lower;
	}
	
	/* Protected */
	
	/* Private */
	
	private final int upper, lower;
	private Integer value;
	
	/**
	 * Checks if {@code val} is within the slider bounds. the
	 * comparison is closed meaning that if {@code val} is exactly
	 * equal to one of the bounds it is considered within the bounds.
	 * 
	 * @param val The value to check.
	 * 
	 * @return {@code true} if the value is within the slider bounds.
	 * 		{@code false} if not. 
	 */
	private boolean withinBounds(int val)
	{
		return val >= lower && val <= upper;
	}
}
