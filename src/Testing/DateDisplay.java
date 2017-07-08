package Testing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
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

public class DateDisplay extends JPanel implements ChangeListener, ItemListener
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
	
	public DateDisplay()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(1, 1, 1, 1);
		
		date = new DateContainer();
		
		JLabel dayLabel = new JLabel("Day");
		JLabel monthLabel = new JLabel("Month");
		JLabel yearLabel = new JLabel("Year");
		
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
		add(dayLabel, gbc);
		gbc.gridy = 1;
		add(monthLabel, gbc);
		gbc.gridy = 2;
		add(yearLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		add(daySpinner, gbc);
		gbc.gridy = 1;
		add(monthSpinner, gbc);
		gbc.gridy = 2;
		add(yearSpinner, gbc);
		
	}
	
	public void initMonths(Locale l)
	{
		String[] monthNames = (new DateFormatSymbols(l)).getMonths();

		monthDropDown.removeAllItems();
		for (int i = 0; i <= date.getMaxMonthInYear(); i++)
			monthDropDown.addItem(monthNames[i]);

		monthDropDown.setSelectedIndex(date.getMonth());
	}
	
	public void initYears()
	{
		yearDropDown.removeAllItems();
		for (int i = date.firstYear(); i <= date.lastYear(); ++i)
			yearDropDown.addItem(Integer.toString(i));

		yearDropDown.setSelectedIndex(date.getYear()-date.firstYear());
	}
	
	public void initDays()
	{
		int nDays = date.getDaysInMonth();
		if (nDays == dayDropDown.getItemCount())
			return;
		
		dayDropDown.removeAllItems();
		for (int i = 1; i <= nDays; ++i)
			dayDropDown.addItem(Integer.toString(i));

		dayDropDown.setSelectedIndex(date.getDay()-1);
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
	
	private boolean refreshing; /* prevents recursive event firing */
	private JSpinner daySpinner, monthSpinner, yearSpinner;
	private JComboBox<String> dayDropDown, monthDropDown, yearDropDown;
	private DateContainer date;
	
	private static final long serialVersionUID = 3218284075407373771L;
	
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
		yearDropDown.setSelectedIndex(date.getYear()-date.firstYear());
		
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
}
