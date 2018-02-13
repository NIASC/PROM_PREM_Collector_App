package se.nordicehealth.ppc_app.core.containers.statistics;

import java.util.Map;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.Question;

public class Slider extends Statistics  {
	public Slider(Question q, int val) {
        super(q);
        put(val, Integer.toString(val));
    }
}
