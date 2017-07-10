package Testing;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import core.containers.form.FormContainer;

public class TimePeriodContainer extends FormContainer
{
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
		tpc.addDate(lowerLimit.get(Calendar.YEAR), lowerLimit.get(Calendar.MONTH),
				lowerLimit.get(Calendar.DAY_OF_MONTH));
		tpc.addDate(upperLimit.get(Calendar.YEAR), upperLimit.get(Calendar.MONTH),
				upperLimit.get(Calendar.DAY_OF_MONTH));
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
	
	public synchronized void addDate(int year, int month, int day)
	{
		month -= 1; /* Calendar counts month from 0-11 rather than 1-12 */
		GregorianCalendar d = new GregorianCalendar(year, month, day);
		if (lowerLimit == null || lowerLimit.compareTo(d) > 0)
			lowerLimit = d;
		if (upperLimit == null || upperLimit.compareTo(d) < 0)
			upperLimit = d;
	}
	
	public GregorianCalendar getLowerLimit()
	{
		return (GregorianCalendar) lowerLimit.clone();
	}
	
	public GregorianCalendar getUpperLimit()
	{
		return (GregorianCalendar) upperLimit.clone();
	}
	
	
	public String getStatement()
	{
		return statement;
	}
	
	private String statement;
	private GregorianCalendar upperLimit, lowerLimit, upperSelected, lowerSelected;
}
