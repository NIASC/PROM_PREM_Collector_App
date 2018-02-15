package se.nordicehealth.ppc_app.core.containers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticsContainer {
	public StatisticsContainer() {
	    answers = new ArrayList<>();
	}

	public void addResult(StatisticsData answer) {
		if (answer != null) {
            answers.add(answer);
		}
	}

	public List<StatisticsData> getStatistics() {
	    return Collections.unmodifiableList(answers);
	}

    private List<StatisticsData> answers;
}
