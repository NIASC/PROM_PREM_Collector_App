/*! CalendarPanel.java
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
package se.nordicehealth.ppc_app.implementation.containerdisplay.extended;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;

import java.util.Calendar;

/**
 * A panel that displays date using a spinner for day, month, year
 * and combo box (i.e. drop-down menu) for day and month. The this
 * class is a swing implementation for the {@code Calendar} object
 * which is uses as a backend.
 * 
 * @author Marcus Malmquist
 *
 * @see Calendar
 */
public class MyCheckBox extends AppCompatCheckBox
{
    public MyCheckBox(Context c)
    {
        super(c);
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }

    private String label;
}
