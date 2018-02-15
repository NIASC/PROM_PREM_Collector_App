package se.nordicehealth.ppc_app.core.containers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import se.nordicehealth.ppc_app.common.Util;

public class ViewDataContainer
{
	public ViewDataContainer(List<StatisticsData> statistics, Calendar upper, Calendar lower, int nEntries)
	{
		this.res = statistics;
		this.upper = upper;
		this.lower = lower;
		this.nEntries = nEntries;
	}

	public String representation()
	{
		StringBuilder sb = new StringBuilder();
		for (StatisticsData s : res)
            sb.append(formatStatisticsData(s));
		return sb.toString();
	}

	public String title()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return String.format(Locale.US, "Displaying statistics for %d entries for period [%s -- %s]\n\n",
				nEntries, sdf.format(lower.getTime()), sdf.format(upper.getTime()));
	}

	private Calendar upper, lower;
	private int nEntries;
	private List<StatisticsData> res;

    private String formatStatisticsData(StatisticsData s)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s:\n", s.question.getStatement()));

        Map<String, Integer> sortedByOccurrence = Util.sortByValue(s.answersAndCount, true);
        int tot = totalEntries(sortedByOccurrence.values());
        for (Entry<String, Integer> e : sortedByOccurrence.entrySet()) {
            sb.append(formatLine("├", e.getValue(), tot, e.getKey()));
        }

        sb.append("├─────────────────\n");
        sb.append(formatLine("└", tot, tot, "Total")); sb.append("\n");
        return sb.toString();
    }

    private int totalEntries(Iterable<Integer> count) {
        int tot = 0; for (int i : count) { tot += i; }
        return tot;
    }

    private String formatLine(String edge, int count, int total, String statement) {
    	double percent = Math.round(100D*count/total);
        return String.format(Locale.getDefault(), "%s─ %.0f %% (%d) - %s\n",
                edge, percent, count, statement);
    }
}
