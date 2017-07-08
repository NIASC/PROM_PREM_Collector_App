package Testing;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateContainer
{
	public static void main(String[] args)
	{
		DateContainer dc = new DateContainer();
	}
	
	public DateContainer()
	{
		this(new GregorianCalendar(Locale.getDefault()));
	}
	
	public DateContainer(GregorianCalendar c)
	{
		if (c.isLenient())
			calendar = c;
		else
			calendar = new GregorianCalendar(Locale.getDefault());
		refresh();
	}
	
	public int getDay()
	{
		return day;
	}
	
	public int getMonth()
	{
		return month;
	}
	
	public int getYear()
	{
		return year;
	}
	
	public void setDay(int day)
	{
		if (day == this.day)
			return;
		calendar.set(Calendar.DAY_OF_MONTH, day);
		refresh();
	}
	
	public void setMonth(int month)
	{
		if (month == this.month)
			return;
		setDay(correctDay(new GregorianCalendar(year, month, 1)));
		calendar.set(Calendar.MONTH, month);
		refresh();
	}
	
	public void setYear(int year)
	{
		if (year == this.year || year < BEGIN_YEAR || year > END_YEAR)
			return;
		setDay(correctDay(new GregorianCalendar(year, month, 1)));
		calendar.set(Calendar.YEAR, year);
		refresh();
	}
	
	public int getDaysInMonth()
	{
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public int getMaxMonthInYear()
	{
		return calendar.getActualMaximum(Calendar.MONTH);
	}
	
	
	public int firstYear()
	{
		return BEGIN_YEAR;
	}
	
	public int lastYear()
	{
		return END_YEAR;
	}
	
	
	private static int BEGIN_YEAR = 2005; // earliest entry
	private static int END_YEAR = 2017; // latest entry / today
	private int day, month, year;
	private GregorianCalendar calendar;

	/**
	 * If current day of month does not exist in new month
	 * we should set the day of month to the closed day in
	 * the new month.
	 * 
	 * @param gc The calendar for the date to check.
	 * 
	 * @return the corrected day.
	 */
	private int correctDay(GregorianCalendar gc)
	{
		int daysInMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
		return daysInMonth < day ? daysInMonth : day;
	}
	
	private synchronized void refresh()
	{
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH);
		year = calendar.get(Calendar.YEAR);
	}
	
}
