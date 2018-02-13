package se.nordicehealth.ppc_app.core.containers;

import java.util.Map;

class StatisticsData  {
    final Question question;
    final Map<String, Integer> answersAndCount;

    StatisticsData(Question q, Map<String, Integer> ac)  {
        question = q;
        answersAndCount = ac;
    }
}
