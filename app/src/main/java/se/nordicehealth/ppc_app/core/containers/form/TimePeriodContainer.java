package se.nordicehealth.ppc_app.core.containers.form;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class TimePeriodContainer extends FormContainer
{
	public TimePeriodContainer(boolean allowEmpty, String statement, String description)
	{
		super(allowEmpty, statement, description);
		entries = new ArrayList<>();

        selected = new CalendarBounds(null, null);
        limits = new CalendarBounds(new GregorianCalendar(), new GregorianCalendar());
        limits.sort();
	}

	@Override
	public boolean hasEntry()
	{
		return selected.upper != null && selected.lower != null;
	}

	@Override
	public CalendarBounds getEntry()
	{
        return selected;
	}

	public boolean setEntry(Calendar lower, Calendar upper)
	{
		entryIsSet = true;

        selected.lower = limits.setWithin(lower);
        selected.upper = limits.setWithin(upper);
        selected.sort();

		nSelDates = 0;
		for (Calendar cal : entries)
            if (selected.isWithin(cal))
				nSelDates++;
		return true;
	}

	public void addDate(Calendar cal)
	{
		if (cal == null)
			return;
        limits.pushBounds(cal);
		entries.add(cal);
		Collections.sort(entries);
	}

	public Calendar getLowerLimit()
	{
		return (Calendar) limits.lower.clone();
	}

	public Calendar getUpperLimit()
	{
		return (Calendar) limits.upper.clone();
	}

	public int getPeriodEntries()
	{
		return nSelDates;
	}

    private CalendarBounds limits, selected;
	private List<Calendar> entries;
	private int nSelDates;

    public class CalendarBounds
    {

        public Calendar getUpper()
        {
            return upper;
        }

        public Calendar getLower()
        {
            return lower;
        }
        private Calendar upper, lower;

        private CalendarBounds(Calendar upper, Calendar lower)
        {
            this.upper = upper;
            this.lower = lower;
        }

        private Calendar setWithin(Calendar cal)
        {
            if (cal.compareTo(lower) < 0)
                cal = (Calendar) lower.clone();
            else if (cal.compareTo(upper) > 0)
                cal = (Calendar) upper.clone();
            return cal;
        }

        private boolean isWithin(Calendar cal)
        {
            return cal.compareTo(lower) >= 0 && cal.compareTo(upper) <= 0;
        }

        private void pushBounds(Calendar cal)
        {
            if (lower == null || lower.compareTo(cal) > 0)
                lower = (Calendar) cal.clone();
            if (upper == null || upper.compareTo(cal) < 0)
                upper = (Calendar) cal.clone();
        }

        private void sort()
        {
            if (lower.compareTo(upper) > 0) {
                Calendar tmp = lower;
                lower = upper;
                upper = tmp;
            }
        }
    }
}
