package se.nordicehealth.ppc_app.core.containers.statistics;

import se.nordicehealth.ppc_app.core.containers.Question;

public class Area extends Statistics {
	public Area(Question question, String text) {
        super(question);
        put(text, text);
    }
}
