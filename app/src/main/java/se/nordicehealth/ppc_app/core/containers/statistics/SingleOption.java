package se.nordicehealth.ppc_app.core.containers.statistics;

import java.util.Map;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.Question;

public class SingleOption extends Statistics
{
	public SingleOption(Question q, int optionIdx)
    {
        super(q);
        this.optionIdx = new TreeMap<>();
        this.optionIdx.put(optionIdx, q.getOption(optionIdx));
    }

    public Map<Object, String> answers()
    {
        return optionIdx;
    }

    private Map<Object, String> optionIdx;
}
