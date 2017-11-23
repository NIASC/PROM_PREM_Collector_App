/*! StatisticsContainer.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.statistics.Statistics;

public class StatisticsContainer
{
	public StatisticsContainer()
	{
        answers = new TreeMap<>();
	}

	public void addResult(Statistics answer)
	{
		if (answer != null)
            addAnswer(answer.question(), answer);
	}

	public List<StatisticsData> getStatistics()
	{
        List<StatisticsData> l = new ArrayList<>();
        for (QA qa : answers.values()) {
            Map<String, Integer> m = new TreeMap<>();
            for (Ans a : qa.ac.answerCount.values())
                m.put(a.statement, a.count);
            l.add(new StatisticsData(qa.q, m));
        }
        return l;
	}

    private void addAnswer(Question q, Statistics answer)
    {
        if (!answers.containsKey(q.getID()))
            answers.put(q.getID(), new QA(q, new AnswerCount()));
        answers.get(q.getID()).ac.addAnswer(answer);
    }

    private Map<Integer, QA> answers;

	private class AnswerCount
	{
		Map<Object, Ans> answerCount;

		AnswerCount()
		{
			answerCount = new TreeMap<>();
		}

		void addAnswer(Statistics answer)
		{
			for (Entry<Object, String> e : answer.answers().entrySet()) {
				if (!answerCount.containsKey(e.getKey()))
                    answerCount.put(e.getKey(), new Ans(e.getValue()));
                answerCount.get(e.getKey()).count++;
			}
		}
	}

    private class Ans
    {
        String statement;
        int count;

        Ans(String statement)
        {
            this.statement = statement;
            count = 0;
        }
    }

    private class QA
    {
        Question q;
        AnswerCount ac;

        QA(Question q, AnswerCount ac)
        {
            this.q = q;
            this.ac = ac;
        }
    }
}
