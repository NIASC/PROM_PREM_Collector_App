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
	public StatisticsContainer() {
	    answers = new TreeMap<>();
	}

	public void addResult(Statistics answer) {
		if (answer != null) {
		    addAnswer(answer.question(), answer);
		}
	}

	public List<StatisticsData> getStatistics() {
        List<StatisticsData> out = new ArrayList<>();
        for (StatementOccurrence occurrence : answers.values()) {
            out.add(new StatisticsData(occurrence.getQuestion(), occurrence.getStatementCount()));
        }
        return out;
	}

    private void addAnswer(Question q, Statistics answer) {
        if (!answers.containsKey(q.getID())) {
            answers.put(q.getID(), new StatementOccurrence(q));
        }
        answers.get(q.getID()).addAnswer(answer);
    }

    private Map<Integer, StatementOccurrence> answers;

    private class StatementOccurrence {
        Question question;
        Map<Object, Occurrence> sortedStatementCount;

        class Occurrence {
            String statement;
            int count;
            Occurrence(String statement) { this.statement = statement; count = 0; }
        }

        StatementOccurrence(Question question) {
            this.question = question;
            sortedStatementCount = new TreeMap<>();
        }

        Question getQuestion() {
            return question;
        }

        Map<String, Integer> getStatementCount() {
            Map<String, Integer> statementCount = new TreeMap<>();
            for (Occurrence answerCount : sortedStatementCount.values()) {
                statementCount.put(answerCount.statement, answerCount.count);
            }
            return statementCount;
        }

        void addAnswer(Statistics statistics) {
            for (Entry<Object, String> e : statistics.answerIdentifierAndText()) {
                if (!sortedStatementCount.containsKey(e.getKey())) {
                    sortedStatementCount.put(e.getKey(), new Occurrence(e.getValue()));
                }
                sortedStatementCount.get(e.getKey()).count++;
            }
        }
    }
}
