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
import javax.swing.JPanel;
import javax.swing.JSpinner;
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
		gbc.gridy = 0;
		
		date = new DateContainer();

		dayDropDown = new JComboBox<String>();
		dayDropDown.setName("dayDropDown");
		initDays();
		monthDropDown = new JComboBox<String>();
		monthDropDown.setName("monthDropDown");
		initMonths(Locale.getDefault());
		yearDropDown = new JComboBox<String>();
		yearDropDown.setName("yearDropDown");
		initYears();
		
		daySpinner = new JSpinner();
		monthSpinner = new JSpinner();
		yearSpinner = new JSpinner();

		daySpinner.setName("daySpinner");
		daySpinner.setPreferredSize(new Dimension(150, 30));
		daySpinner.setValue(date.getDay());
		dayDropDown.setBorder(new EmptyBorder(0, 0, 0, 0));
		daySpinner.setEditor(dayDropDown);

		yearSpinner.setName("yearSpinner");
		yearSpinner.setPreferredSize(new Dimension(150, 30));
		yearSpinner.setValue(date.getYear());
		yearDropDown.setBorder(new EmptyBorder(0, 0, 0, 0));
		yearSpinner.setEditor(yearDropDown);
		
		monthSpinner.setName("monthSpinner");
		monthSpinner.setPreferredSize(new Dimension(150, 30));
		monthSpinner.setValue(date.getMonth());
		monthDropDown.setBorder(new EmptyBorder(0, 0, 0, 0));
		monthSpinner.setEditor(monthDropDown);

		daySpinner.addChangeListener(this);
		yearSpinner.addChangeListener(this);
		monthSpinner.addChangeListener(this);
		
		dayDropDown.addItemListener(this);
		monthDropDown.addItemListener(this);
		yearDropDown.addItemListener(this);
		

		gbc.gridx = 0;
		add(daySpinner, gbc);
		gbc.gridx = 1;
		add(monthSpinner, gbc);
		gbc.gridx = 2;
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
	{ // spinner
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
	{ // combobox
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
	
	private boolean refreshing;
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
}
