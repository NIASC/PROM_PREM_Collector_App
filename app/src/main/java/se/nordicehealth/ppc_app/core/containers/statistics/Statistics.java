package se.nordicehealth.ppc_app.core.containers.statistics;

import java.util.Map;

import se.nordicehealth.ppc_app.core.containers.Question;

public abstract class Statistics
{
    protected Question q;

    Statistics(Question q) {
        this.q = q;
    }

    public Question question()
    {
        return q;
    }

    public abstract Map<Object, String> answers();

}
