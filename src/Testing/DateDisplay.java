package Testing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DateDisplay extends JPanel
{
	public static void main(String[] argv)
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(new DateDisplay());
		frame.setSize(100, 100);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * TODO: initialize 
	 */
	public DateDisplay()
	{
		setLayout(new BorderLayout());
		
		DatePanel dpFrom = new DatePanel();
		DatePanel dpTo = new DatePanel();
		
		JPanel from = new JPanel(new BorderLayout());
		from.add(dpFrom, BorderLayout.CENTER);
		from.add(new JLabel("From"), BorderLayout.NORTH);

		JPanel to = new JPanel(new BorderLayout());
		to.add(dpTo, BorderLayout.CENTER);
		to.add(new JLabel("To"), BorderLayout.NORTH);

		add(from, BorderLayout.WEST);
		add(to, BorderLayout.EAST);
	}
	
	private class DatePanel extends JPanel implements ChangeListener, ItemListener
	{

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
					date.setDay(value);
				else if (s.getName().equals(monthSpinner.getName()))
					date.setMonth(value);
				else if (s.getName().equals(yearSpinner.getName()))
					date.setYear(value);
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
						date.setDay(dayDropDown.getSelectedIndex()+1);
					else if (c.getName().equals(monthDropDown.getName()))
						date.setMonth(monthDropDown.getSelectedIndex());
					else if (c.getName().equals(yearDropDown.getName()))
						date.setYear(yearDropDown.getSelectedIndex()+date.firstYear());
					update();
				}
			}
		}
		
		DatePanel()
		{
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
		
		void initMonths(Locale l)
		{
			String[] monthNames = (new DateFormatSymbols(l)).getMonths();

			monthDropDown.removeAllItems();
			for (int i = 0; i <= date.getMaxMonthInYear(); i++)
				monthDropDown.addItem(monthNames[i]);

			monthDropDown.setSelectedIndex(date.getMonth());
		}
		
		void initYears()
		{
			yearDropDown.removeAllItems();
			for (int i = date.firstYear(); i <= date.lastYear(); ++i)
				yearDropDown.addItem(Integer.toString(i));

			yearDropDown.setSelectedIndex(date.getYear()-date.firstYear());
		}
		
		void initDays()
		{
			int nDays = date.getDaysInMonth();
			if (nDays == dayDropDown.getItemCount())
				return;
			
			dayDropDown.removeAllItems();
			for (int i = 1; i <= nDays; ++i)
				dayDropDown.addItem(Integer.toString(i));

			dayDropDown.setSelectedIndex(date.getDay()-1);
		}
		
		boolean refreshing; /* prevents recursive event firing */
		JSpinner daySpinner, monthSpinner, yearSpinner;
		JComboBox<String> dayDropDown, monthDropDown, yearDropDown;
		DateContainer date;
		
		static final long serialVersionUID = 3218284075407373771L;
		
		void update()
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
			yearDropDown.setSelectedIndex(date.getYear()-date.firstYear());
			
			refreshing = false;
		}
		
		void initDropDownLists()
		{
			dayDropDown.setName("dayDropDown");
			monthDropDown.setName("monthDropDown");
			yearDropDown.setName("yearDropDown");
			
			initDays();
			initMonths(Locale.getDefault());
			initYears();
		}
		
		void initSpinners()
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
		
		void addListeners()
		{
			daySpinner.addChangeListener(this);
			yearSpinner.addChangeListener(this);
			monthSpinner.addChangeListener(this);
			
			dayDropDown.addItemListener(this);
			monthDropDown.addItemListener(this);
			yearDropDown.addItemListener(this);
		}
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
			if (year == this.year || year < BEGIN_YEAR || year > END_YEAR)
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
		
		
		int firstYear()
		{
			return BEGIN_YEAR;
		}
		
		int lastYear()
		{
			return END_YEAR;
		}
		
		
		int BEGIN_YEAR = 2005; // earliest entry
		int END_YEAR = 2017; // latest entry / today
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
	
	private class DateBound
	{
		DateBound(int year, int month, int day)
		{
			this.year = year;
			this.month = month;
			this.day = day;
			compare(this);
		}
		
		/**
		 * Compares this {@code DateBound} with {@code date}.
		 * 
		 * @param date The {@code DateBound} to compare with this.
		 * 
		 * @return
		 * 		0 if dates are equal<br>
		 * 		&lt;1 if this {@code DateBound} occurs before
		 * 		{@code date}<br>
		 * 		&gt;1 if {@code date} occurs before this
		 * 		{@code DateBound}
		 */
		int compare(DateBound date)
		{
			GregorianCalendar c1 = new GregorianCalendar(
					year, month, day);
			GregorianCalendar c2 = new GregorianCalendar(
					date.year, date.month, date.day);
			return c1.compareTo(c2);
		}
		
		final int year, month, day;
	}
}
