/*! ViewDataContainer.java
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
package se.nordicehealth.ppc_app.core.containers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is a container for statistical data that has been processed
 * and is ready to be displayed.
 * 
 * @author Marcus Malmquist
 *
 */
public class ViewDataContainer
{
	/**
	 * Initializes a class that contains processed statistical data.
	 * 
	 * @param res A list of the statistical data objects.
	 * @param upper The upper bound that this data was retrieved for.
	 * @param lower The lower bound that this data was retrieved for.
	 * @param nEntries The number of entries that have been used to
	 * 		create the statistical data.
	 */
	public ViewDataContainer(List<StatisticsData> res, Calendar upper,
                             Calendar lower, int nEntries)
	{
		this.res = res;
		this.upper = upper;
		this.lower = lower;
		this.nEntries = nEntries;
	}
	
	/**
	 * Retrieves the results as a string that should be places in some
	 * kind of text area. This method is temporary.
	 * 
	 * @return The results as a formatted string, The format is as
	 * 		follows:<br><br>
	 * 		<code>
	 * 		statement:<br>
	 * 		|- &lt;count&gt; (&lt;percent&gt; %) - answer/option<br>
	 * 		...<br>
	 * 		|- &lt;count&gt; (&lt;percent&gt; %) - answer/option<br>
	 * 		|- ----------<br>
	 * 		\- &lt;count&gt; (&lt;percent&gt; %) - Total<br><br>
	 * 		...<br><br>
	 * 		statement:<br>
	 * 		|- &lt;count&gt; (&lt;percent&gt; %) - answer/option<br>
	 * 		...<br>
	 * 		|- &lt;count&gt; (&lt;percent&gt; %) - answer/option<br>
	 * 		|- ----------<br>
	 * 		\- &lt;count&gt; (&lt;percent&gt; %) - Total<br>
	 * 		</code>
	 */
	public String getResults()
	{
		StringBuilder sb = new StringBuilder();
		for (StatisticsData s : res) {
            Question q = s.question;
            Map<String, Integer> ac = s.answerCount;
			int tot = 0;
            for (Integer i : ac.values())
                tot += i;
			sb.append(String.format("%s:\n", q.getStatement()));

			for (Entry<String, Integer> e : ac.entrySet())
				sb.append(String.format(Locale.getDefault(), "├─ %03d %% (%04d) - %s\n",
                        Math.round(100.0 * e.getValue().doubleValue() / tot),
                        e.getValue(), e.getKey()));

			sb.append("├─────────────────\n");
			sb.append(String.format(Locale.getDefault(), "└─ %03d %% (%04d) - %s\n\n", 100, tot, "Total"));
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @return The title as a formatted string. The title contains
	 * 		information about how many entries that have been used and
	 * 		which period that is being displayed.
	 */
	public String getTitle()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return String.format(Locale.US,
				"Displaying statistics for %d entries for period [%s -- %s]\n\n",
				nEntries, sdf.format(lower.getTime()),
				sdf.format(upper.getTime()));
	}

	private Calendar upper, lower;
	private int nEntries;
	private List<StatisticsData> res;
}
