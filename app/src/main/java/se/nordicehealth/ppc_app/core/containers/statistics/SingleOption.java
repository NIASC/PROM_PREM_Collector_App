package se.nordicehealth.ppc_app.core.containers.statistics;

import se.nordicehealth.ppc_app.core.containers.Question;

public class SingleOption extends Statistics {
	public SingleOption(Question question, int optionIdx) {
        super(question);
        put(optionIdx, question.getOption(optionIdx));
    }
}
