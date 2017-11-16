/*! FieldDisplay.java
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
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.nordicehealth.ppc_app.core.containers.form.FieldContainer;
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
public class FieldDisplay extends LinearLayout implements FormComponentDisplay
{
	/* Public */

	@Override
	public boolean fillEntry()
	{
		return fc.setEntry(field.getText().toString());
	}

	@Override
	public boolean entryFilled()
	{
		if (!field.getText().toString().trim().isEmpty())
			fillEntry();
		return fc.hasEntry();
	}

	/* Protected */

	protected FieldDisplay(Context c, FieldContainer fc)
	{
		super(c);
		setOrientation(LinearLayout.HORIZONTAL);
		this.fc = fc;

		TextView fieldLabel = new TextView(c);
		fieldLabel.setText(String.format("%s: ", fc.getStatement()));
		addView(fieldLabel);

		field = new EditText(c);
		field.setText(fc.getEntry());
		field.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		if (fc.isSecret())
			field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		field.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					fillEntry();
				}
			}
		});
		addView(field);
	}

	/* Private */

	private FieldContainer fc;
	private EditText field;
}
