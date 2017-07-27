/**
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import applet.core.containers.form.SingleOptionContainer;
import applet.core.interfaces.Messages;
import applet.core.interfaces.UserInterface.FormComponentDisplay;
import applet.implementation.SwingComponents;

/**
 * This class is a displayable wrapper the for
 * {@code SingleOptionContainer}.
 * It handles placing the {@code SingleOptionContainer} in an object
 * that the implementation of the {@code UserInterface} can display.
 * 
 * @author Marcus Malmquist
 * 
 * @see SingleOptionContainer
 * @see UserInterface
 *
 */
public class SingleOptionDisplay extends JPanel implements FormComponentDisplay, ItemListener
{
	/* Public */
	
	@Override
	public void requestFocus()
	{
		
	}

	@Override
	public void itemStateChanged(ItemEvent ev)
	{
		if (ev.getItemSelectable() instanceof AbstractButton)
		{
			AbstractButton button = (AbstractButton) ev.getItemSelectable();
			boolean selected = (ev.getStateChange() == ItemEvent.SELECTED);
			JRadioButton sel = options.get(button.getName());
			if (sel == null)
				return;
			if (selected)
				responseID = Integer.parseInt(button.getName());
		}
	}

	@Override
	public boolean fillEntry()
	{
		return soc.setEntry(responseID);
	}

	@Override
	public boolean entryFilled() 
	{
		return soc.hasEntry();
	}
	
	/* Protected */
	
	/**
	 * Creates a displayable wrapper for {@code soc}.
	 * 
	 * @param soc The instance of the SingleOptionContainer that
	 * 		the instance of this SingleOptionDisplay should act as
	 * 		a wrapper for.
	 */
	protected SingleOptionDisplay(SingleOptionContainer soc)
	{
		setLayout(new BorderLayout());
		this.soc = soc;
		responseID = null;

		String description = "";
		String optional = Messages.getMessages().getInfo(
				Messages.INFO_UI_FORM_OPTIONAL);
		if (soc.getDescription() != null && !soc.getDescription().isEmpty())
			description = "\n\n"+soc.getDescription();
		JTextArea jta = AddTextArea(
				(soc.allowsEmpty() ? "("+optional+") " : "") + soc.getStatement()
				+ description + "\n", 0, 35);
		add(jta, BorderLayout.NORTH);
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets.bottom = gbc.insets.top = 1;
		gbc.gridx = 0;

		ButtonGroup group = new ButtonGroup();
		Map<Integer, String> opt = soc.getOptions();
		Integer selected = soc.getSelectedID();
		options = new TreeMap<String, JRadioButton>();
		int gridy = 0;
		for (Entry<Integer, String> e : opt.entrySet())
		{
			gbc.gridy = gridy++;
			String buttonName = Integer.toString(e.getKey());
			JRadioButton btn = addToggleButton(
					e.getValue(), buttonName, this);
			if (selected != null && selected == e.getKey())
				btn.setSelected(true);
			group.add(btn);
			buttonPanel.add(btn, gbc);
			options.put(buttonName, btn);
		}
		add(buttonPanel, BorderLayout.WEST);
	}
	
	/* Private */
	
	private static final long serialVersionUID = 7314170750059865699L;
	private SingleOptionContainer soc;
	private Integer responseID;
	
	private Map<String, JRadioButton> options;
	
	private static JRadioButton addToggleButton(
			String buttonText, String name, ItemListener listener)
	{
		return SwingComponents.addToggleButton(buttonText, name,
				null, false, null, null, null, null, listener);
	}
	
	private static JTextArea AddTextArea(String text, int rows, int columns)
	{
		return SwingComponents.makeTextArea(text, null, null, false,
				null, null, null, null, null, false, rows, columns);
	}
}
