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
package applet.implementation.containerdisplay;

import java.awt.BorderLayout;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import applet.core.containers.form.TimePeriodContainer;
import applet.core.interfaces.Messages;
import applet.core.interfaces.UserInterface;
import applet.core.interfaces.UserInterface.FormComponentDisplay;
import applet.implementation.SwingComponents;
import applet.implementation.containerdisplay.extended.CalendarPanel;

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

		String description = "";
		if (tpc.getDescription() != null && !tpc.getDescription().isEmpty())
			description = "\n\n"+tpc.getDescription();
		JTextArea jta = AddTextArea(
				(tpc.allowsEmpty() ? "(Optional) " : "") + tpc.getStatement()
				+ description + "\n", 0, 35);
		add(jta, BorderLayout.NORTH);
		try
		{
			dpFrom = new CalendarPanel(
					tpc.getLowerLimit(), tpc.getUpperLimit());
			dpTo = new CalendarPanel(
					tpc.getLowerLimit(), tpc.getUpperLimit());
		} catch (NullPointerException npe)
		{
			dpFrom = new CalendarPanel(
					new GregorianCalendar(), new GregorianCalendar());
			dpTo = new CalendarPanel(
					new GregorianCalendar(), new GregorianCalendar());
		}
		Messages msg = Messages.getMessages();
		JPanel from = new JPanel(new BorderLayout());
		from.add(dpFrom, BorderLayout.CENTER);
		from.add(AddLabel(msg.getInfo(Messages.INFO_VD_DATE_FROM)),
				BorderLayout.NORTH);

		JPanel to = new JPanel(new BorderLayout());
		to.add(dpTo, BorderLayout.CENTER);
		to.add(AddLabel(msg.getInfo(Messages.INFO_VD_DATE_TO)),
				BorderLayout.NORTH);

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
