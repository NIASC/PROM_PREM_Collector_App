/*! TimePeriodDisplay.java
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
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import se.nordicehealth.ppc_app.core.containers.form.TimePeriodContainer;
import se.nordicehealth.ppc_app.core.interfaces.Implementations;
import se.nordicehealth.ppc_app.core.interfaces.Messages;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;

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
public class TimePeriodDisplay extends LinearLayout implements FormComponentDisplay
{
	/* public */

	@Override
	public boolean fillEntry()
	{
		return tpc.setEntry(
				new GregorianCalendar(dpFrom.getYear(), dpFrom.getMonth(), dpFrom.getDayOfMonth()),
				new GregorianCalendar(dpTo.getYear(), dpTo.getMonth(), dpTo.getDayOfMonth()));
	}

	@Override
	public boolean entryFilled()
	{
		return tpc.hasEntry();
	}

	/* protected */

	protected TimePeriodDisplay(Context c, TimePeriodContainer tpc)
	{
		super(c);
		setOrientation(LinearLayout.VERTICAL);
		this.tpc = tpc;

		String description = "";
		if (tpc.getDescription() != null && !tpc.getDescription().isEmpty())
			description = "\n\n"+tpc.getDescription();

		TextView jta = new TextView(c);
		jta.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		jta.setSingleLine(false);
		jta.setMaxLines(35);
		jta.setText((tpc.allowsEmpty() ? "(Optional) " : "") + tpc.getStatement()
				+ description + "\n");
		addView(jta);

		try
		{
            Calendar upper = tpc.getUpperLimit();
            upper.set(upper.get(Calendar.YEAR), upper.get(Calendar.MONTH) + 1, 0, 0, 0, 0 );
            Calendar lower = tpc.getUpperLimit();
            lower.set(lower.get(Calendar.YEAR), lower.get(Calendar.MONTH)    , 1, 0, 0, 0 );

			dpFrom = new DatePicker(c);
            dpFrom.updateDate(lower.get(Calendar.YEAR),
                    lower.get(Calendar.MONTH),
                    lower.get(Calendar.DAY_OF_MONTH));
            dpFrom.setMinDate(lower.getTimeInMillis());
            dpFrom.setMaxDate(upper.getTimeInMillis());

			dpTo = new DatePicker(c);
            dpTo.updateDate(upper.get(Calendar.YEAR),
                    upper.get(Calendar.MONTH),
                    upper.get(Calendar.DAY_OF_MONTH));
            dpTo.setMinDate(lower.getTimeInMillis());
            dpTo.setMaxDate(upper.getTimeInMillis());
		} catch (Exception npe)
		{
			dpFrom = new DatePicker(c);
			//dpFrom.setMaxDate((new GregorianCalendar()).getTimeInMillis());
			//dpFrom.setMinDate((new GregorianCalendar()).getTimeInMillis());

			dpTo = new DatePicker(c);
			//dpTo.setMaxDate((new GregorianCalendar()).getTimeInMillis());
			//dpTo.setMinDate((new GregorianCalendar()).getTimeInMillis());
		}
		Messages msg = Implementations.Messages();

		LinearLayout date = new LinearLayout(c);
		date.setOrientation(LinearLayout.VERTICAL);
        date.setLayoutParams(new FrameLayout.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

		LinearLayout from = new LinearLayout(c);
        from.setOrientation(LinearLayout.VERTICAL);
		TextView fromTF = new TextView(c);
		fromTF.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		fromTF.setText(msg.info(Messages.INFO_VD_DATE_FROM));
		from.addView(fromTF);
		from.addView(dpFrom);

		LinearLayout to = new LinearLayout(c);
        to.setOrientation(LinearLayout.VERTICAL);
		TextView toTF = new TextView(c);
		toTF.setLayoutParams(new FrameLayout.LayoutParams(
				LinearLayoutCompat.LayoutParams.MATCH_PARENT,
				LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
		toTF.setText(msg.info(Messages.INFO_VD_DATE_TO));
		to.addView(toTF);
		to.addView(dpTo);

		date.addView(from);
		date.addView(to);

		addView(date);
	}

	/* private */

	private TimePeriodContainer tpc;
	private DatePicker dpFrom, dpTo;
}
