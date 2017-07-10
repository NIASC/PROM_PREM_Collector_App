package implementation.containerdisplay.extended;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CalendarPanel extends JPanel implements ChangeListener, ItemListener
{
	/* public */
	
	public CalendarPanel(Calendar lower, Calendar upper)
	{
		this.lower = lower != null ? lower : new GregorianCalendar();
		this.upper = upper != null ? upper : new GregorianCalendar();
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(1, 1, 1, 1);
		
		date = new DateContainer(new GregorianCalendar(
				Locale.getDefault()));
		
		dayDropDown = new JComboBox<String>();
		monthDropDown = new JComboBox<String>();
		yearDropDown = new JComboBox<String>();
		initDropDownLists();
		
		daySpinner = new JSpinner();
		monthSpinner = new JSpinner();
		yearSpinner = new JSpinner();
		initSpinners();

		addListeners();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel("Day"), gbc);
		gbc.gridy = 1;
		add(new JLabel("Month"), gbc);
		gbc.gridy = 2;
		add(new JLabel("Year"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		add(daySpinner, gbc);
		gbc.gridy = 1;
		add(monthSpinner, gbc);
		gbc.gridy = 2;
		add(yearSpinner, gbc);
	}
	
	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() instanceof JSpinner)
		{
			if (refreshing) /* prevents recursive event firing */
				return;
			
			JSpinner s = (JSpinner) e.getSource();
			int value = ((Integer) s.getModel().getValue()).intValue();
			if (s.getName().equals(daySpinner.getName()))
				setDate(date.getYear(), date.getMonth(), value);
			else if (s.getName().equals(monthSpinner.getName()))
				setDate(date.getYear(), value, date.getDay());
			else if (s.getName().equals(yearSpinner.getName()))
				setDate(value, date.getMonth(), date.getDay());
			update();
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if (e.getSource() instanceof JComboBox)
		{
			JComboBox<?> c = (JComboBox<?>) e.getSource();
			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				if (refreshing) /* prevents recursive event firing */
					return;

				if (c.getName().equals(dayDropDown.getName()))
					setDate(date.getYear(), date.getMonth(),
							dayDropDown.getSelectedIndex()+1);
				else if (c.getName().equals(monthDropDown.getName()))
					setDate(date.getYear(), monthDropDown.getSelectedIndex(),
							date.getDay());
				else if (c.getName().equals(yearDropDown.getName()))
					setDate(yearDropDown.getSelectedIndex()+lower.get(Calendar.YEAR),
							date.getMonth(), date.getDay());
				update();
			}
		}
	}
	
	public GregorianCalendar getDate()
	{
		return new GregorianCalendar(
				date.getYear(), date.getMonth(), date.getDay());
	}
	
	/* protected */
	
	/* private */
	
	private boolean refreshing; /* prevents recursive event firing */
	private JSpinner daySpinner, monthSpinner, yearSpinner;
	private JComboBox<String> dayDropDown, monthDropDown, yearDropDown;
	private DateContainer date;
	private final Calendar upper, lower;
	
	private static final long serialVersionUID = 3218284075407373771L;

	
	private void initMonths(Locale l)
	{
		String[] monthNames = (new DateFormatSymbols(l)).getMonths();

		monthDropDown.removeAllItems();
		for (int i = 0; i <= date.getMaxMonthInYear(); i++)
			monthDropDown.addItem(monthNames[i]);

		monthDropDown.setSelectedIndex(date.getMonth());
	}
	
	private void initYears()
	{
		yearDropDown.removeAllItems();
		for (int i = lower.get(Calendar.YEAR); i <= upper.get(Calendar.YEAR); ++i)
			yearDropDown.addItem(Integer.toString(i));

		yearDropDown.setSelectedIndex(date.getYear()-lower.get(Calendar.YEAR));
	}
	
	private void initDays()
	{
		int nDays = date.getDaysInMonth();
		if (nDays == dayDropDown.getItemCount())
			return;
		
		dayDropDown.removeAllItems();
		for (int i = 1; i <= nDays; ++i)
			dayDropDown.addItem(Integer.toString(i));

		dayDropDown.setSelectedIndex(date.getDay()-1);
	}
	
	private void update()
	{
		/* Setting values and indices triggers events.
		 * "Locking" this part of the code prevent that.
		 */
		refreshing = true;
		
		/* day */
		initDays();
		daySpinner.setValue(date.getDay());
		dayDropDown.setSelectedIndex(date.getDay()-1);
		/* month */
		monthSpinner.setValue(date.getMonth());
		monthDropDown.setSelectedIndex(date.getMonth());
		/* year */
		yearSpinner.setValue(date.getYear());
		yearDropDown.setSelectedIndex(date.getYear()-lower.get(Calendar.YEAR));
		
		refreshing = false;
	}
	
	private void initDropDownLists()
	{
		dayDropDown.setName("dayDropDown");
		monthDropDown.setName("monthDropDown");
		yearDropDown.setName("yearDropDown");
		
		initDays();
		initMonths(Locale.getDefault());
		initYears();
	}
	
	private void initSpinners()
	{
		daySpinner.setName("daySpinner");
		monthSpinner.setName("monthSpinner");
		yearSpinner.setName("yearSpinner");
		
		Dimension d = new Dimension(150, 30);
		daySpinner.setPreferredSize(d);
		monthSpinner.setPreferredSize(d);
		yearSpinner.setPreferredSize(d);
		
		daySpinner.setValue(date.getDay());
		monthSpinner.setValue(date.getMonth());
		yearSpinner.setValue(date.getYear());
		
		Border border = new EmptyBorder(0, 0, 0, 0);
		dayDropDown.setBorder(border);
		monthDropDown.setBorder(border);
		yearDropDown.setBorder(border);
		
		daySpinner.setEditor(dayDropDown);
		monthSpinner.setEditor(monthDropDown);
		yearSpinner.setEditor(yearDropDown);
	}
	
	private void addListeners()
	{
		daySpinner.addChangeListener(this);
		yearSpinner.addChangeListener(this);
		monthSpinner.addChangeListener(this);
		
		dayDropDown.addItemListener(this);
		monthDropDown.addItemListener(this);
		yearDropDown.addItemListener(this);
	}
	
	/**
	 * This method checks if the supplied date is within the upper and
	 * lower bounds. This method is preferred over the methods in
	 * {@code DateContainer} to modify the date.
	 * @param year
	 * @param month
	 * @param day
	 */
	private void setDate(int year, int month, int day)
	{
		boolean setDay = day != date.getDay();
		if (lower.compareTo(new GregorianCalendar(year, month, day)) > 0)
		{
			year = lower.get(Calendar.YEAR);
			month = lower.get(Calendar.MONTH);
			day = lower.get(Calendar.DAY_OF_MONTH);
		}
		else if (upper.compareTo(new GregorianCalendar(year, month, day)) < 0)
		{
			year = upper.get(Calendar.YEAR);
			month = upper.get(Calendar.MONTH);
			day = upper.get(Calendar.DAY_OF_MONTH);
		}

		date.setYear(year);
		date.setMonth(month);
		if (date.getDaysInMonth() < day && !setDay)
			day = date.getDaysInMonth();
		date.setDay(day);
	}
	
	private class DateContainer
	{
		DateContainer(GregorianCalendar c)
		{
			if (c.isLenient())
				calendar = c;
			else
			{
				calendar = new GregorianCalendar(Locale.getDefault());
				calendar.set(c.get(Calendar.YEAR),
						c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH));
			}
			refresh();
		}
		
		int getDay()
		{
			return day;
		}
		
		int getMonth()
		{
			return month;
		}
		
		int getYear()
		{
			return year;
		}
		
		void setDay(int day)
		{
			if (day == this.day)
				return;
			calendar.set(Calendar.DAY_OF_MONTH, day);
			refresh();
		}
		
		void setMonth(int month)
		{
			if (month == this.month)
				return;
			setDay(correctDay(new GregorianCalendar(year, month, 1)));
			calendar.set(Calendar.MONTH, month);
			refresh();
		}
		
		void setYear(int year)
		{
			if (year == this.year)
				return;
			setDay(correctDay(new GregorianCalendar(year, month, 1)));
			calendar.set(Calendar.YEAR, year);
			refresh();
		}
		
		int getDaysInMonth()
		{
			return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		
		int getMaxMonthInYear()
		{
			return calendar.getActualMaximum(Calendar.MONTH);
		}
		
		int day, month, year;
		GregorianCalendar calendar;

		/**
		 * If current day of month does not exist in new month
		 * we should set the day of month to the closed day in
		 * the new month.
		 * 
		 * @param gc The calendar for the date to check.
		 * 
		 * @return the corrected day.
		 */
		int correctDay(GregorianCalendar gc)
		{
			int daysInMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
			return daysInMonth < day ? daysInMonth : day;
		}
		
		synchronized void refresh()
		{
			day = calendar.get(Calendar.DAY_OF_MONTH);
			month = calendar.get(Calendar.MONTH);
			year = calendar.get(Calendar.YEAR);
		}
		
	}
}
