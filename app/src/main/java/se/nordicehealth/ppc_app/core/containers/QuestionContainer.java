package se.nordicehealth.ppc_app.core.containers;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.containers.form.FormContainer;

public class QuestionContainer
{
	public QuestionContainer()
	{
		questions = new TreeMap<>();
		containers = new TreeMap<>();
	}
	
	@Override
	public Object clone()
	{
		QuestionContainer qc = new QuestionContainer();
		for (Question q : questions.values())
			qc.addQuestion(q);
		return qc;
	}

	public void addQuestion(Question q)
	{
        int id = q.getID();
		if (questions.containsKey(id))
			return;
		questions.put(id, q);
		containers.put(id, q.getContainer());
	}

	public Iterable<FormContainer> forms()
    {
        return containers.values();
    }

	public Question getQuestion(int index)
	{
        return (Question) atIndex(index, questions.values());
	}

	public int getSize()
	{
		return questions.size();
	}

	private Map<Integer, Question> questions;
	private Map<Integer, FormContainer> containers;

    private Object atIndex(int index, Iterable<? extends Object> e)
    {
        int i = 0;
        for (Object q : e)
            if (i++ == index)
                return q;
        return null;
    }
}
