/**
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
package core.containers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import core.containers.QuestionContainer.Question;

/**
 * This class contains data for viewing questionnaire statistics.
 * The statistics consist of the question identifiers, options and
 * option count. The option count is the number of occurrences of
 * a particular option.
 * 
 * @author Marcus Malmquist
 *
 */
public class StatisticsContainer
{
	/**
	 * Creates an empty container for questionnaire statistics.
	 */
	public StatisticsContainer()
	{
		ah = new AnswerHandle();
		mstat = new TreeMap<Integer, Statistics>();
	}
	
	/**
	 * Append a question with the result to this container.
	 * 
	 * @param q The question which contains the question data
	 * 		as it appears in the database
	 * @param answer The answer to the question in {@code q}.
	 */
	public void addResult(Question q, Object answer)
	{
		if (answer == null)
			return;
		List<String> lst = q.getOptions();
		if (lst != null && !lst.isEmpty())
		{
			try
			{
				ah.addAnswer(q, q.getOption(new Integer(answer.toString())));
			}
			catch (NumberFormatException e)
			{
				return; /* Unsupported answer format */
			}
		}
		else
		{
			ah.addAnswer(q, answer); // answers are not IDs
		}
		if (!mstat.containsKey(q.getID()))
			mstat.put(q.getID(), new Statistics(q));
	}
	
	/**
	 * Retrieves a list of the statistics.
	 * 
	 * @return A list of {@code Statistics} objects.
	 * 
	 * @see Statistics
	 */
	public List<Statistics> getStatistics()
	{
		return Collections.unmodifiableList(
				new ArrayList<Statistics>(mstat.values()));
	}
	
	/**
	 * A container class for statistical data. The statistical
	 * data contains the question and the number of occurrences.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	public class Statistics
	{
		/**
		 * Retrieves the options for object's question.
		 * 
		 * @return A list of the options for this object's question.
		 */
		public List<String> getOptions()
		{
			List<String> lst = question.getOptions();
			if (lst == null || lst.isEmpty())
				return null;
			return lst;
		}
		
		/**
		 * Retrieves the question/statement for this objects'
		 * question.
		 * 
		 * @return The question/statement for this object's question.
		 */
		public String getStatement()
		{
			return question.getStatement();
		}
		
		/**
		 * Retrieves the type of question that this object contains.
		 * 
		 * @return The question's class.
		 */
		public Class<?> getQuestionClass()
		{
			return question.getContainerClass();
		}
		
		/**
		 * The upper bound for this question, if it has an integer
		 * limit.
		 * 
		 * @return The upper bound for this question.
		 */
		public Integer getUpperBound()
		{
			return question.getUpper();
		}
		
		/**
		 * The lower bound for this question, if it has an integer
		 * limit.
		 * 
		 * @return The upper bound for this question.
		 */
		public Integer getLowerBound()
		{
			return question.getLower();
		}
		
		/**
		 * 
		 * @return The Options/values as keys and the number of
		 * 		occurrences as values.
		 */
		public Map<Object, Integer> getAnswerCounts()
		{
			return Collections.unmodifiableMap(
					ah.answers.get(question.getID()).answerCount);
		}
		
		private Question question;
		
		/**
		 * Creates a new statistics object for {@code q}.
		 * @param q
		 */
		private Statistics(Question q)
		{
			question = q;
		}
	}
	
	private AnswerHandle ah;
	private Map<Integer, Statistics> mstat;
	
	/**
	 * This class contains questions and the number of occurrences for the
	 * different answers.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private class AnswerHandle
	{
		
		Map<Integer, Question> questions;
		Map<Integer, AnswerCount> answers;
		
		/**
		 * Create an empty handler.
		 */
		AnswerHandle()
		{
			answers = new TreeMap<Integer, AnswerCount>();
			questions = new TreeMap<Integer, Question>();
		}
		
		/**
		 * Adds an answer to the supplied question.
		 * 
		 * @param q The question to add the answer to.
		 * @param answer The answer to the question.
		 */
		void addAnswer(Question q, Object answer)
		{
			if (!questions.containsKey(q.getID()))
			{
				answers.put(q.getID(), new AnswerCount());
				questions.put(q.getID(), q);
			}
			answers.get(q.getID()).addAnswer(answer);
		}
	}
	
	/**
	 * This class counts the number of occurrences of an answer.
	 * Only one instance per question should be used in order for
	 * this class to make any sense since this class does not know
	 * which question it counts for.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private class AnswerCount
	{
		Map<Object, Integer> answerCount;
		
		/**
		 * Creates an empty counter.
		 */
		AnswerCount()
		{
			answerCount = new TreeMap<Object, Integer>();
		}
		
		/**
		 * Adds {@code answer} to this container. Each answer
		 * has its own counter which is incremented whenever
		 * an answer is added.
		 * 
		 * @param answer The answer to add to this counter.
		 */
		void addAnswer(Object answer)
		{
			if (answer instanceof List)
			{
				/* All 'List' objects are actually
				 * 'List<? extends Object>' */
				@SuppressWarnings("unchecked")
				List<Object> lobj = (List<Object>) answer;
				
				for (Iterator<Object> itr = lobj.iterator(); itr.hasNext();)
				{
					Object o = itr.next();
					if (!answerCount.containsKey(o))
						answerCount.put(o, 0);
					answerCount.put(o, answerCount.get(o).intValue() + 1);
				}
			}
			else
			{
				if (!answerCount.containsKey(answer))
					answerCount.put(answer, 0);
				answerCount.put(answer, answerCount.get(answer).intValue() + 1);
			}
		}
	}
}
