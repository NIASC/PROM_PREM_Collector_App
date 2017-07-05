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
package core.containers.form;

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
	 * @param min The upper bound of the slider.
	 * @param max The lower bound of the slider.
	 * 
	 * @throws NullPointerException If {@code max} or {@code min} is
	 * 		{@code null}.
	 */
	public SliderContainer(boolean allowEmptyEntries,
			String statement, Integer min, Integer max)
					throws NullPointerException
	{
		super(allowEmptyEntries);
		if (min == null || max == null)
			throw new NullPointerException();
		slider = new Slider(statement);
		lower = min;
		upper = max;
	}

	@Override
	public boolean hasEntry()
	{
		return allowEmpty || slider.value != null;
	}

	@Override
	public FormContainer copy()
	{
		SliderContainer sc = new SliderContainer(
				allowEmpty, slider.statement, lower, upper);
		sc.setEntry(slider.value);
		return sc;
	}

	@Override
	public Integer getEntry()
	{
		return slider.value;
	}
	
	/**
	 * Retrieves the statement (i.e. the statement that you request
	 * the user to respond to)
	 * 
	 * @return This container's statement.
	 */
	public String getStatement()
	{
		return slider.statement;
	}
	
	/**
	 * Sets the entry (i.e. the user input in response to the field's
	 * statement) of this container's field. Setting the entry to null
	 * can be used to reset the field.
	 * 
	 * @param entry The user entry to set.
	 * 
	 * @return {@code true} if the value was set. {@code false} if
	 * 		not.
	 */
	public boolean setEntry(int entry)
	{
		if (withinBounds(entry))
		{
			slider.value = entry;
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return The lower bound of this slider.
	 */
	public int getUpperBound()
	{
		return upper;
	}
	
	/**
	 * 
	 * @return The upper bound of this slider.
	 */
	public int getLowerBound()
	{
		return lower;
	}
	
	/* Protected */
	
	/* Private */
	
	private Slider slider;
	private final int upper, lower;
	
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
	
	/**
	 * This class contains a slider object. A slider object is an
	 * object that has an upper and a lower bound as well as a marker
	 * that can be moved between the bounds. the position of the
	 * marker determines the value of this component.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private final class Slider
	{
		/**
		 * The statements that the user should respond to.
		 */
		final String statement;
		
		/**
		 * The value of this slider (i.e. the user's response).
		 */
		Integer value;
		
		/**
		 * Creates a new Slider object with the statement set to
		 * {@code statement}.
		 * 
		 * @param statement The statement that the user should move
		 * 		the slider in response to.
		 */
		public Slider(String statement)
		{
			this.statement = statement;
			value = null;
		}
	}
}
