package se.nordicehealth.ppc_app.core.containers.form;

public class SliderContainer extends FormContainer
{
	public SliderContainer(boolean allowEmpty, String statement, String description, int min, int max)
	{
		super(allowEmpty, statement, description);
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

	public boolean setEntry(int entry)
	{
		entryIsSet = true;
		boolean valid = withinBounds(entry);
		if (valid)
			value = entry;
		return valid;
	}

	public int upperBound()
	{
		return upper;
	}

	public int lowerBound()
	{
		return lower;
	}
	
	private final int upper, lower;
	private Integer value;

	private boolean withinBounds(int val)
	{
		return val >= lower && val <= upper;
	}
}
