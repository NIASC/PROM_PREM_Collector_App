/** ViewDataContainer.java
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
package applet.core.containers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import applet.core.containers.StatisticsContainer.Statistics;
import applet.core.containers.form.AreaContainer;
import applet.core.containers.form.SingleOptionContainer;
import applet.core.containers.form.SliderContainer;

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
	public ViewDataContainer(List<Statistics> res, Calendar upper,
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
		for (Statistics s : res)
		{
			List<Object> statements = new ArrayList<Object>();
			List<Integer> occurences = new ArrayList<Integer>();
			int tot = 0;
			
			Class<?> c = s.getQuestionClass();
			Map<Object, Integer> ans = s.getAnswerCounts();
			Integer count;
			if (SingleOptionContainer.class.isAssignableFrom(c))
			{
				String statement;
				for (Iterator<String> itr = s.getOptions().iterator(); itr.hasNext();)
				{
					statement = itr.next();
					if ((count = ans.get(statement)) == null)
						continue;
					occurences.add(count);
					statements.add(statement);
					tot += count;
				}
			}
			else if (SliderContainer.class.isAssignableFrom(c))
			{
				for (int i = s.getLowerBound(); i <= s.getUpperBound(); ++i)
				{
					if ((count = ans.get(i)) == null)
						continue;
					occurences.add(count);
					statements.add(i);
					tot += count;
				}
			}
			else if (AreaContainer.class.isAssignableFrom(c))
			{
				for (Entry<Object, Integer> e : ans.entrySet())
				{
					count = e.getValue();
					occurences.add(count);
					statements.add(e.getKey().toString());
					tot += count;
				}
			}
			sb.append(String.format("%s:\n", s.getStatement()));
			
			Iterator<Object> sitr;
			Iterator<Integer> iitr;
			for (sitr = statements.iterator(), iitr = occurences.iterator();
					sitr.hasNext() && iitr.hasNext();)
			{
				Integer i = iitr.next();
				sb.append(String.format("|- %4d (%3d%%) - %s\n",
						i, Math.round(100.0 * i.doubleValue() / tot),
						sitr.next()));
			}
			sb.append("|- ------------ -\n");
			sb.append(String.format("\\- %4d (%3.0f %%) - %s\n\n", tot, 100D, "Total"));
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return String.format(
				"Displaying statistics for %d entries for period [%s -- %s]\n\n",
				nEntries, sdf.format(lower.getTime()),
				sdf.format(upper.getTime()));
	}

	private Calendar upper, lower;
	private int nEntries;
	private List<Statistics> res;
}
