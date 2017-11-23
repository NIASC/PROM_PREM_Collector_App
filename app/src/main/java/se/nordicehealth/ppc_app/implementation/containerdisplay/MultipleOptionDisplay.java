/*! MultipleOptionDisplay.java
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
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.form.MultipleOptionContainer;
import se.nordicehealth.ppc_app.core.containers.form.SingleOptionContainer;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;
import se.nordicehealth.ppc_app.implementation.containerdisplay.extended.MyCheckBox;

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
public class MultipleOptionDisplay extends LinearLayout implements FormComponentDisplay
{
	/* Public */

	@Override
	public boolean fillEntry()
	{
		return moc.setEntry(responseID);
	}

	@Override
	public boolean entryFilled()
	{
		return moc.hasEntry();
	}

	/* Protected */

	/**
	 * Creates a displayable wrapper for {@code moc}.
	 *
	 * @param moc The instance of the MultipleOptionContainer that
	 * 		the instance of this MultipleOptionDisplay should act as
	 * 		a wrapper for.
	 */
	protected MultipleOptionDisplay(Context c, MultipleOptionContainer moc)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.moc = moc;
		responseID = new ArrayList<>();

		String description = "";
		String optional = Implementations.Messages().info(
				Messages.INFO.UI_FORM_OPTIONAL);
		if (moc.getDescription() != null && !moc.getDescription().isEmpty())
			description = "\n\n"+moc.getDescription();

		TextView jta = new TextView(c);
		jta.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		jta.setSingleLine(false);
		jta.setMaxLines(35);
		jta.setText((moc.allowsEmpty() ? "("+optional+") " : "") + moc.getStatement()
				+ description + "\n");
		addView(jta);

		LinearLayout buttonPanel = new LinearLayout(c);
		buttonPanel.setOrientation(LinearLayout.VERTICAL);

		Map<Integer, String> opt = moc.getOptions();

		List<Integer> selected = moc.getSelectedIDs();

		options = new TreeMap<>();
		for (Entry<Integer, String> e : opt.entrySet())
		{
			String buttonName = Integer.toString(e.getKey());
			MyCheckBox btn = new MyCheckBox(c);
			btn.setText(e.getValue());
			btn.setLabel(buttonName);
			if (selected.contains(e.getKey()))
				btn.setSelected(true);
			btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					MyCheckBox button = (MyCheckBox) buttonView;
					if (options.get(button.getLabel()) == null)
						return;

					if (isChecked)
						responseID.add(Integer.parseInt(button.getLabel()));
					else
						responseID.remove(Integer.valueOf(button.getLabel()));
				}
			});
			buttonPanel.addView(btn);
			options.put(buttonName, btn);
		}
		addView(buttonPanel);
	}

	/* Private */

	private MultipleOptionContainer moc;
	private List<Integer> responseID;

	private Map<String, MyCheckBox> options;
}
