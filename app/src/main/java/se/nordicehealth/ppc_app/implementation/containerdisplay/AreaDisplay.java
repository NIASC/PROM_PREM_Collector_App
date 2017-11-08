/*! AreaDisplay.java
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.nordicehealth.ppc_app.core.containers.form.AreaContainer;
import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;

/**
 * This class is a displayable wrapper for {@code FieldContainer}.
 * It handles placing the {@code FieldContainer} in an object that
 * the implementation of the {@code UserInterface} can display.
 * 
 * @author Marcus Malmquist
 * 
 * @see FieldContainer
 * @see UserInterface
 *
 */
public class AreaDisplay extends LinearLayout implements FormComponentDisplay
{
	/* Public */

	@Override
	public boolean fillEntry()
	{
		return ac.setEntry(area.getText().toString());
	}

	@Override
	public boolean entryFilled()
	{
		return ac.hasEntry();
	}

	/* Protected */

	/**
	 * Creates a displayable wrapper for {@code ac}.
	 *
	 * @param ac The instance of the {@code AreaContainer} that
	 * 		the instance of this {@code AreaDisplay} should act as a
	 * 		wrapper for.
	 *
	 * @see AreaContainer
	 */
	protected AreaDisplay(Context c, AreaContainer ac)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.ac = ac;

		String description = "";
		String optional = Messages.getMessages().getInfo(
				Messages.INFO_UI_FORM_OPTIONAL);
		if (ac.getDescription() != null && !ac.getDescription().isEmpty())
			description = "\n\n"+ac.getDescription();

        TextView jta = new TextView(c);
        jta.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        jta.setSingleLine(false);
        jta.setMaxLines(35);
        jta.setText((ac.allowsEmpty() ? "("+optional+") " : "") + ac.getStatement()
                + description + "\n");
        addView(jta);

        area = new EditText(c);
        area.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
        area.setSingleLine(false);
        area.setMaxLines(35);
		addView(area);
	}

	/* Private */

	private AreaContainer ac;
	private EditText area;
}
