package se.nordicehealth.ppc_app.core.containers.statistics;

import java.util.Map;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.Question;

public class Area extends Statistics
{
	public Area(Question q, String text)
    {
        super(q);
        entry = new TreeMap<>();
        entry.put(text, text);
    }

    public Map<Object, String> answers()
    {
        return entry;
    }

    private Map<Object, String> entry;
}
