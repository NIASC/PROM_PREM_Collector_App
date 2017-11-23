package se.nordicehealth.ppc_app.core.containers;

import java.util.Map;

class StatisticsData
{
    final Question question;
    final Map<String, Integer> answerCount;

    StatisticsData(Question q, Map<String, Integer> ac)
    {
        question = q;
        answerCount = ac;
    }
}
