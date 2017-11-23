/*! SliderDisplay.java
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
import android.widget.SeekBar;
import android.widget.TextView;

import se.nordicehealth.ppc_app.core.containers.form.SliderContainer;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;

/**
 * This class is a displayable wrapper for {@code SliderContainer}.
 * It handles placing the {@code SliderContainer} in an object that
 * the implementation of the {@code UserInterface} can display.
 * 
 * @author Marcus Malmquist
 * 
 * @see SliderContainer
 * @see UserInterface
 * 
 */
public class SliderDisplay extends LinearLayout implements FormComponentDisplay
{
	/* Public */

	@Override
	public boolean fillEntry()
	{
		return sc.setEntry(response);
	}

	@Override
	public boolean entryIsFilled()
	{
		return sc.hasEntry();
	}

	/* Protected */

	protected SliderDisplay(Context c, SliderContainer sc)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.sc = sc;
		response = sc.getLowerBound();

		String description = "";
		String optional = Implementations.Messages().info(
				Messages.INFO.UI_FORM_OPTIONAL);
		if (sc.getDescription() != null && !sc.getDescription().isEmpty())
			description = "\n\n"+sc.getDescription();

		TextView jta = new TextView(c);
		jta.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		jta.setSingleLine(false);
		jta.setMaxLines(35);
		jta.setText((sc.allowsEmpty() ? "("+optional+") " : "") + sc.getStatement()
				+ description + "\n");
		addView(jta);

        SeekBar slider = new SeekBar(c);
		//slider.setMin(sc.getLowerBound());
		slider.setMax(sc.getUpperBound());
		slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				response = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		addView(slider);
	}

	/* Private */

	private SliderContainer sc;
	private Integer response;
}
