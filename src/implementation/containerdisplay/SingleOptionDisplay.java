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
package implementation.containerdisplay;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import core.containers.form.SingleOptionContainer;
import core.interfaces.UserInterface.FormComponentDisplay;

/**
 * This class is a displayable wrapper the for SingleOption
 * container. In this implementation this class displays the
 * SingleOption container and stores the response.
 * In a GUI implementaion a corresponding class could just extend
 * a JComponent that specializes in displaying select-single-option
 * content and not necessarily displaying the content itself.
 * 
 * @author Marcus Malmquist
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
		boolean selected = (ev.getStateChange() == ItemEvent.SELECTED);
		AbstractButton button = (AbstractButton) ev.getItemSelectable();
		JRadioButton sel = options.get(button.getName());
		if (sel == null)
			return;
		if (selected) {
			responseID = Integer.parseInt(button.getName());
		}
	}

	@Override
	public boolean fillEntry()
	{
		return soc.setSelected(responseID);
	}

	@Override
	public boolean entryFilled() 
	{
		return soc.hasEntry();
	}
	
	/* Protected */
	
	/**
	 * Initializes login variables.
	 * 
	 * @param soc The instance of the SingleOptionContainer that
	 * 		the instance of this SingleOptionDisplay should act as
	 * 		a wrapper for.
	 */
	protected SingleOptionDisplay(SingleOptionContainer soc)
	{
		setLayout(new GridLayout(0, 1));
		this.soc = soc;

		group = new ButtonGroup();
		
		HashMap<Integer, String> opt = soc.getSOptions();
		options = new HashMap<String, JRadioButton>();
		for (Entry<Integer, String> e : opt.entrySet())
		{
			JRadioButton btn = new JRadioButton(e.getValue());
			btn.setName(e.getValue());
			group.add(btn);
			btn.addItemListener(this);
			add(btn);
			options.put(Integer.toString(e.getKey()), btn);
		}
	}
	
	/* Private */
	
	private static final long serialVersionUID = 7314170750059865699L;
	private SingleOptionContainer soc;
	private int responseID;
	
	private HashMap<String, JRadioButton> options;
	private ButtonGroup group;
}
