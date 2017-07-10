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

import core.interfaces.UserInterface.FormComponentDisplay;
import implementation.containerdisplay.extended.CalendarPanel;

public class DateDisplay extends JPanel implements FormComponentDisplay
{
	/* public */

	public static void main(String[] argv)
	{
		TimePeriodContainer tpc = new TimePeriodContainer(false, "Select dates");
		tpc.addDate(1989, 5, 1);
		tpc.addDate(1998, 1, 9);
		tpc.addDate(2011, 9, 9);
		tpc.addDate(1965, 12, 31); /* lower */
		tpc.addDate(2018, 1, 31); /* upper */
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(new DateDisplay(tpc));
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
		return tpc.setEntry(new GregorianCalendar[]
				{dpFrom.getDate(), dpTo.getDate()});
	}

	@Override
	public boolean entryFilled()
	{
		fillEntry();
		return tpc.hasEntry();
	}
	
	/* protected */
	
	protected DateDisplay(TimePeriodContainer tpc)
	{
		this.tpc = tpc;
		setLayout(new BorderLayout());
		
		dpFrom = new CalendarPanel(tpc.getLowerLimit(), tpc.getUpperLimit());
		dpTo = new CalendarPanel(tpc.getLowerLimit(), tpc.getUpperLimit());
		
		JPanel from = new JPanel(new BorderLayout());
		from.add(dpFrom, BorderLayout.CENTER);
		from.add(new JLabel("From"), BorderLayout.NORTH);

		JPanel to = new JPanel(new BorderLayout());
		to.add(dpTo, BorderLayout.CENTER);
		to.add(new JLabel("To"), BorderLayout.NORTH);

		add(from, BorderLayout.WEST);
		add(to, BorderLayout.EAST);
	}
	
	/* private */

	private static final long serialVersionUID = -3248412101555211790L;
	private TimePeriodContainer tpc;
	private CalendarPanel dpFrom, dpTo;
}
