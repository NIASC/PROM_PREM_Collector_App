/*! QuestionContainer.java
 * 
 * Copyright 2017 Marcus Malmquist
 * 
 * This file is part of PROM_PREM_Collector.
 * 
 * PROM_PREM_Collector is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * PROM_PREM_Collector is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PROM_PREM_Collector.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package se.nordicehealth.ppc_app.core.containers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import se.nordicehealth.ppc_app.core.Questionnaire;
import se.nordicehealth.ppc_app.core.containers.form.FormContainer;

/**
 * This class is a container for {@code Questionnaire} entries.
 * The class is capable of storing the questions in an ordered manner
 * determined by the order at which they are added to this container.
 * 
 * @author Marcus Malmquist
 * 
 * @see Questionnaire
 *
 */
public class QuestionContainer
{
	/* public */

	public QuestionContainer()
	{
		questions = new TreeMap<>();
	}
	
	@Override
	public Object clone()
	{
		QuestionContainer qc = new QuestionContainer();
		synchronized (this) {
			for (Question q : questions.values())
				qc.addQuestion(q);
		}
		return qc;
	}

	public synchronized void addQuestion(Question q)
	{
		if (questions.containsKey(q.getID()))
			return;
		questions.put(q.getID(), q);
	}
	
	/**
	 * Retrieves the question labeled with {@code index} which represents
	 * the order in which questions were added.
	 * 
	 * @param index The index for the question.
	 * 
	 * @return The container for the question labeled with {@code index} if
	 * 		that question exist. Else {@code null} is returned.
	 */
	public FormContainer getContainer(int index)
	{
		Question q = getQuestion(index);
		return q != null ? q.getContainer() : null;
	}
	
	/**
	 * Retrieves the question at index {@code index}. The index represents
	 * the order at which the question was added in.
	 * 
	 * @param index The index of the question to retrieve.
	 * 
	 * @return The question at index {@code index}.
	 */
	public Question getQuestion(int index)
	{
		if (index >= 0 && index < questions.size())
		{
			int i = 0;
			for (Iterator<Question> itr = questions.values().iterator(); itr.hasNext(); ++i)
			{
				Question q = itr.next();
				if (i == index)
					return q;
			}
		}
		return null;
	}
	
	/**
	 * Retrieves the number of entries in this container.
	 * 
	 * @return The size (number of entries) of this container.
	 */
	public int getSize()
	{
		return questions.size();
	}
	
	/* protected */
	
	/* private */

	private Map<Integer, Question> questions;
}
