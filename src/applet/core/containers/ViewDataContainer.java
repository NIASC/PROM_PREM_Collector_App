package applet.core.containers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import applet.core.UserHandle;
import applet.core.containers.StatisticsContainer.Statistics;
import applet.core.containers.form.AreaContainer;
import applet.core.containers.form.SingleOptionContainer;
import applet.core.containers.form.SliderContainer;
import applet.core.interfaces.Implementations;
import applet.core.interfaces.UserInterface;

public class ViewDataContainer
{
	public ViewDataContainer(UserInterface ui, List<Statistics> res,
			Calendar upper, Calendar lower, int nEntries)
	{
		this.userInterface = ui;
		this.res = res;
		this.upper = upper;
		this.lower = lower;
		this.nEntries = nEntries;
	}
	
	public String getResults()
	{
		StringBuilder sb = new StringBuilder();
		for (Statistics s : res)
		{
			List<Object> lstr = new ArrayList<Object>();
			List<Integer> lint = new ArrayList<Integer>();
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
					lint.add(count);
					lstr.add(statement);
					tot += count;
				}
			}
			else if (SliderContainer.class.isAssignableFrom(c))
			{
				for (int i = s.getLowerBound(); i <= s.getUpperBound(); ++i)
				{
					if ((count = ans.get(i)) == null)
						continue;
					lint.add(count);
					lstr.add(i);
					tot += count;
				}
			}
			else if (AreaContainer.class.isAssignableFrom(c))
			{
				for (Entry<Object, Integer> e : ans.entrySet())
				{
					count = e.getValue();
					lint.add(count);
					lstr.add(e.getKey().toString());
					tot += count;
				}
			}
			sb.append(String.format("%s:\n", s.getStatement()));
			
			Iterator<Object> sitr;
			Iterator<Integer> iitr;
			for (sitr = lstr.iterator(), iitr = lint.iterator();
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
	
	public String getTitle()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return String.format(
				"Displaying statistics for %d entries for period [%s -- %s]\n\n",
				nEntries, sdf.format(lower.getTime()),
				sdf.format(upper.getTime()));
	}

	private UserInterface userInterface;
	private Calendar upper, lower;
	private int nEntries;
	private List<Statistics> res;
}
