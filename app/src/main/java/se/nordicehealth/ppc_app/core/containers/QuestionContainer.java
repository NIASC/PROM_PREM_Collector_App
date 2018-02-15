package se.nordicehealth.ppc_app.core.containers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import se.nordicehealth.ppc_app.core.containers.form.FormContainer;

public class QuestionContainer
{
	public QuestionContainer() {
		questions = new TreeMap<>();
	}
	
	@Override
	public Object clone() {
		QuestionContainer qc = new QuestionContainer();
		for (Question q : questions.values()) {
            qc.addQuestion(q);
        }
		return qc;
	}

	public void addQuestion(Question q) {
        int id = q.getID();
		if (questions.containsKey(id)) {
            return;
        }
		questions.put(id, q);
	}

	public Iterable<FormContainer> createForms() {
		List<FormContainer> containers = new LinkedList<>();
    	for (Entry<Integer, Question> e : questions.entrySet()) {
			containers.add(e.getValue().getContainer());
		}
        return containers;
    }

	public Question getQuestion(int index) {
        return (Question) atIndex(index, questions.values());
	}

	public int getSize() {
		return questions.size();
	}

	private Map<Integer, Question> questions;

    private Object atIndex(int index, Iterable<?> e) {
        int i = 0;
        for (Object q : e) {
            if (i == index) {
                return q;
            }
            i++;
        }
        return null;
    }
}
