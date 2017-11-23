/*! SingleOptionDisplay.java
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
package se.nordicehealth.ppc_app.implementation.containerdisplay;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;
import se.nordicehealth.ppc_app.implementation.containerdisplay.extended.MyRadioButton;

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
public class SingleOptionDisplay extends LinearLayout implements FormComponentDisplay
{
	/* Public */

	@Override
	public boolean fillEntry()
	{
		return soc.setEntry(Collections.singletonList(responseID));
	}

	@Override
	public boolean entryIsFilled()
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
	protected SingleOptionDisplay(Context c, SingleOptionContainer soc)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.soc = soc;
		responseID = null;

		String description = "";
		String optional = Implementations.Messages().info(
				Messages.INFO.UI_FORM_OPTIONAL);
		if (soc.getDescription() != null && !soc.getDescription().isEmpty())
			description = "\n\n"+soc.getDescription();

		TextView jta = new TextView(c);
		jta.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		jta.setSingleLine(false);
		jta.setMaxLines(35);
		jta.setText((soc.allowsEmpty() ? "("+optional+") " : "") + soc.getStatement()
				+ description + "\n");
		addView(jta);

		RadioGroup group = new RadioGroup(c);
        group.setOrientation(LinearLayout.VERTICAL);
		Map<Integer, String> opt = soc.getOptions();
        List<Integer> selected = soc.getSelectedID();
		for (Entry<Integer, String> e : opt.entrySet())
		{
			String buttonName = Integer.toString(e.getKey());
            MyRadioButton btn = new MyRadioButton(c);
			btn.setText(e.getValue());
            btn.setLabel(buttonName);
			if (!selected.isEmpty() && selected.get(0).equals(e.getKey()))
				btn.setSelected(true);
			group.addView(btn);
		}
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                MyRadioButton sel = group.findViewById(checkedId);
                if (sel.isChecked())
                    responseID = Integer.parseInt(sel.getLabel());
            }
        });
		addView(group);
	}

	/* Private */

	private SingleOptionContainer soc;
	private Integer responseID;
}
