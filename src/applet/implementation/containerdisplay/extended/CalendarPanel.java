/** CalendarPanel.java
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
package applet.implementation.containerdisplay.extended;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import applet.implementation.SwingComponents;

/**
 * A panel that displays date using a spinner for day, month, year
 * and combo box (i.e. drop-down menu) for day and month. The this
 * class is a swing implementation for the {@code Calendar} object
 * which is uses as a backend.
 * 
 * @author Marcus Malmquist
 *
 * @see Calendar
 */
public class CalendarPanel extends JPanel implements ChangeListener, ItemListener
{
	/* public */
	
	/**
	 * Creates a panel that contains menus to select a date. The available
	 * dates will range from {@code lower} to {@code upper} but the
	 * displayed dates will range from the first day for the {@code lower}
	 * year to the last day of the {@code upper} year, but if a date
	 * outside the valid time period is selected it will automatically be
	 * set to the relevant limit.<br>
	 * If {@code lower} or {@code upper} is null the current date will be
	 * chosen and if {@code lower} occurs after {@code upper} they will
	 * automatically be switched.
	 * 
	 * @param lower The lower bound for the time period.
	 * @param upper The upper bound for the time period.
	 */
	public CalendarPanel(Calendar lower, Calendar upper)
	{
		if (lower == null || upper == null)
			throw new NullPointerException("Null bound(s)!");
		if (lower.compareTo(upper) > 0)
		{
			this.lower = (Calendar) upper.clone();
			this.upper = (Calendar) lower.clone();
		}
		else
		{
			this.lower = (Calendar) lower.clone();
			this.upper = (Calendar) upper.clone();
		}
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(1, 1, 1, 1);
		
		date = new DateContainer((Calendar) this.upper.clone());
		
		dayDropDown = AddComboBox(String.class, "dayDropDown", null);
		monthDropDown = AddComboBox(String.class, "monthDropDown", null);
		yearDropDown = AddComboBox(String.class, "yearDropDown", null);

		initDays();
		initMonths(Locale.getDefault());
		initYears();
		
		Dimension d = new Dimension(150, 30);
		daySpinner = AddSpinner(
				date.getDay(), "daySpinner", d, null, dayDropDown);
		monthSpinner = AddSpinner(
				date.getMonth(), "monthSpinner", d, null, monthDropDown);
		yearSpinner = AddSpinner(
				date.getYear(), "yearSpinner", d, null, yearDropDown);

		addListeners();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(AddLabel("Day"), gbc);
		gbc.gridy = 1;
		add(AddLabel("Month"), gbc);
		gbc.gridy = 2;
		add(AddLabel("Year"), gbc);

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
	
	/**
	 * Retrieves the selected date.
	 * 
	 * @return The selected date.
	 */
	public Calendar getDate()
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

	/**
	 * Fills the month combobox with months names.
	 * 
	 * @param l The locale which determines how the month names will be
	 * 		displayed.
	 */
	private void initMonths(Locale l)
	{
		List<String> monthName = Arrays.asList((new DateFormatSymbols(l)).getMonths());

		monthDropDown.removeAllItems();
		for (Iterator<String> itr = monthName.iterator(); itr.hasNext();)
			monthDropDown.addItem(itr.next());

		monthDropDown.setSelectedIndex(date.getMonth());
	}
	
	/**
	 * Fills the year combobox with years rangin from the lower year limit
	 * to the upper year limit.
	 */
	private void initYears()
	{
		yearDropDown.removeAllItems();
		for (int i = lower.get(Calendar.YEAR); i <= upper.get(Calendar.YEAR); ++i)
			yearDropDown.addItem(Integer.toString(i));

		yearDropDown.setSelectedIndex(date.getYear()-lower.get(Calendar.YEAR));
	}
	
	/**
	 * Fills the day combobox with day numbers 1-31* and selects the
	 * current day.
	 * 
	 * <br><br>* If the current month does not have 31 day the values will
	 * instead range from 1 to the highest day number.
	 */
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
	
	/**
	 * Updates the date values of {@code date} and sets the appropriate
	 * values/indices in the spinner and combobox components. This method
	 * will set {@code refreshing} to {@code true} while it executes
	 * because the implementation may fire events which in turn could call
	 * this method. It is therefore recommended to check if this method is
	 * running ({@code if (refreshing) return;}) in any listener methods
	 * that call this method.
	 */
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
	
	/**
	 * Adds ChangeListener to JSpinner and ItemListener to JComboBox.
	 * 
	 * Adding a listener may cause an event to fire, which could cause a
	 * NullPointerException if some components are not initialized when the
	 * listeners are added. It is therefore recommended to initialize the
	 * components first and then add all of the listeners (using this
	 * method).
	 */
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
	 * 
	 * @param year The year of the date to set.
	 * @param month The month of the date to set.
	 * @param day The day on the month of the date to set.
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
	
	private static JLabel AddLabel(String labelText)
	{
		return SwingComponents.makeLabel(
				labelText, null, null, false, null, null, null, null);
	}

	private static JSpinner AddSpinner(Object value, String name,
			Dimension d, ChangeListener listener, JComponent editor)
	{
		return SwingComponents.makeSpinner(value, name, null, false,
				null, null, null, d, listener, editor);
	}

	private static <T> JComboBox<T> AddComboBox(Class<T> c,
			String name, ItemListener listener)
	{
		return SwingComponents.makeComboBox(c, name, null, false, null,
				null, null, null, listener, false,
				new EmptyBorder(0, 0, 0, 0));
	}
	
	private class DateContainer
	{
		DateContainer(Calendar c)
		{
			calendar = (c.isLenient() ? c : new GregorianCalendar(
					c.get(Calendar.YEAR),
					c.get(Calendar.MONTH),
					c.get(Calendar.DAY_OF_MONTH)));
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
		Calendar calendar;

		/**
		 * If current day of month does not exist in new month
		 * we should set the day of month to the closed day in
		 * the new month.
		 * 
		 * @param gc The calendar for the date to check.
		 * 
		 * @return the corrected day.
		 */
		int correctDay(Calendar gc)
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
