package se.nordicehealth.ppc_app.core.containers.statistics;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.Question;

public class MultipleOption extends Statistics {
	public MultipleOption(Question question, List<Integer> optionIdx) {
        super(question);
        for (Integer i : optionIdx) { put(i, question.getOption(i)); }
    }
}
