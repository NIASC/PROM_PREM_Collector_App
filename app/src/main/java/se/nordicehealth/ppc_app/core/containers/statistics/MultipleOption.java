package se.nordicehealth.ppc_app.core.containers.statistics;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.Question;

public class MultipleOption extends Statistics
{
	public MultipleOption(Question q, List<Integer> optionIdx)
    {
        super(q);
        this.optionIdx = new TreeMap<>();
        for (Integer i : optionIdx)
            this.optionIdx.put(i, q.getOption(i));
    }

    public Map<Object, String> answers()
    {
        return optionIdx;
    }

    private Map<Object, String> optionIdx;
}
