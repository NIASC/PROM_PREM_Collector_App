/** TimePeriodDisplay.java
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
package implementation.containerdisplay;

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
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.containers.form.SliderContainer;
import core.containers.form.TimePeriodContainer;
import core.interfaces.UserInterface.FormComponentDisplay;
import implementation.SwingComponents;
import implementation.containerdisplay.extended.CalendarPanel;

/**
 * This class is a displayable wrapper for {@code TimePeriodContainer}.<br>
 * It handles placing the {@code TimePeriodContainer} in an object that
 * the implementation of the {@code UserInterface} can display.
 * 
 * @author Marcus Malmquist
 * 
 * @see TimePeriodContainer
 * @see UserInterface
 * 
 */
public class TimePeriodDisplay extends JPanel implements FormComponentDisplay
{
	/* public */

	public static void main(String[] argv)
	{
		TimePeriodContainer tpc = new TimePeriodContainer(false, "Select dates");
		tpc.addDate(new GregorianCalendar(1989, 5, 1));
		tpc.addDate(new GregorianCalendar(1998, 1, 9));
		tpc.addDate(new GregorianCalendar(2011, 9, 9));
		tpc.addDate(new GregorianCalendar(1965, 12, 31)); /* lower */
		tpc.addDate(new GregorianCalendar(2018, 1, 31)); /* upper */
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(new TimePeriodDisplay(tpc));
		frame.setSize(100, 100);
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void requestFocus()
	{
		
	}

	@Override
	public boolean fillEntry()
	{
		return tpc.setEntry(dpFrom.getDate(), dpTo.getDate());
	}

	@Override
	public boolean entryFilled()
	{
		fillEntry();
		return tpc.hasEntry();
	}
	
	/* protected */
	
	/**
	 * Creates a displayable wrapper for {@code tpc}.
	 * 
	 * @param tpc The {@code TimePeriodContainer} that this wrapper should
	 * 		display.
	 * 
	 * @see TimePeriodContainer
	 */
	protected TimePeriodDisplay(TimePeriodContainer tpc)
	{
		setLayout(new BorderLayout());
		this.tpc = tpc;
		
		JTextArea jtf = AddTextArea(tpc.getStatement(), 0, 35);
		add(jtf, BorderLayout.NORTH);
		try
		{
			dpFrom = new CalendarPanel(tpc.getLowerLimit(), tpc.getUpperLimit());
			dpTo = new CalendarPanel(tpc.getLowerLimit(), tpc.getUpperLimit());
		} catch (NullPointerException npe)
		{
			dpFrom = new CalendarPanel(
					new GregorianCalendar(), new GregorianCalendar());
			dpTo = new CalendarPanel(
					new GregorianCalendar(), new GregorianCalendar());
		}
		
		JPanel from = new JPanel(new BorderLayout());
		from.add(dpFrom, BorderLayout.CENTER);
		from.add(AddLabel("From"), BorderLayout.NORTH);

		JPanel to = new JPanel(new BorderLayout());
		to.add(dpTo, BorderLayout.CENTER);
		to.add(AddLabel("To"), BorderLayout.NORTH);

		add(from, BorderLayout.WEST);
		add(to, BorderLayout.EAST);
	}
	
	/* private */

	private static final long serialVersionUID = -3248412101555211790L;
	private TimePeriodContainer tpc;
	private CalendarPanel dpFrom, dpTo;
	
	private static JLabel AddLabel(String labelText)
	{
		return SwingComponents.makeLabel(
				labelText, null, null, false, null, null, null, null);
	}
	
	private static JTextArea AddTextArea(String text, int rows, int columns)
	{
		return SwingComponents.makeTextArea(text, null, null, false,
				null, null, null, null, null, false, rows, columns);
	}
}
