package se.nordicehealth.ppc_app.core.containers.statistics;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.Question;

public abstract class Statistics  {
    public Question question() { return question; }
    public Set<Map.Entry<Object, String>> answerIdentifierAndText() {
        return Collections.unmodifiableSet(answerIdentifiersWithText.entrySet());
    }

    Statistics(Question question) {
        this.question = question;
        answerIdentifiersWithText = new TreeMap<>();
    }

    void put(Object key, String val) {
        answerIdentifiersWithText.put(key, val);
    }

    private Question question;
    private Map<Object, String> answerIdentifiersWithText;
}