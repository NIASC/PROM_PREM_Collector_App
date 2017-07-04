package core.containers.form;

public class SliderContainer extends FormContainer
{
	/* Public */
	
	/**
	 * Creates a slider container. the slider container contains a
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
		return allowEmpty || slider.getValue() != null;
	}

	@Override
	public FormContainer copy()
	{
		SliderContainer sc = new SliderContainer(
				allowEmpty, slider.getStatement(), lower, upper);
		sc.setEntry(slider.getValue());
		return sc;
	}

	@Override
	public Integer getEntry()
	{
		return slider.getValue();
	}
	
	/**
	 * Sets the content of this container. A slider container is only
	 * allowed to contain one slider so if this container already have
	 * an entry it will be overwritten.
	 * 
	 * @param statement The statement that you want the user to
	 * 		respond to.
	 */
	public void setSlider(String statement)
	{
		if (statement != null)
		{
			statement = statement.trim();
			if (!allowEmpty && statement.isEmpty())
				return;
		}
		slider = new Slider(statement);
	}
	
	/**
	 * Retrieves the statement (i.e. the statement that you request
	 * the user to respond to)
	 * 
	 * @return This container's statement.
	 */
	public String getStatement()
	{
		return slider.getStatement();
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
			slider.setValue(entry);
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
	private int upper, lower;
	
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
	private class Slider
	{
		/* Public */
		
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
		
		/**
		 * Sets the value of this slider.
		 * 
		 * @param val The value to set.
		 */
		public void setValue(int val)
		{
			value = val;
		}
		
		/**
		 * 
		 * @return The statements that the user should respond to.
		 */
		public String getStatement()
		{
			return statement;
		}
		
		/**
		 * 
		 * @return The value of this slider (i.e. the user's response).
		 * 		If no value have been set this method returns
		 * 		{@code null}.
		 */
		public Integer getValue()
		{
			return value;
		}
		
		/* Protected */
		
		/* Private */
		
		private String statement;
		private Integer value;
	}
}
