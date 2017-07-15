package core.containers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import core.containers.QuestionContainer.Question;

public class StatisticsContainer
{
	public StatisticsContainer()
	{
		ah = new AnswerHandle();
		mstat = new TreeMap<Integer, Statistics>();
	}
	
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
	
	public List<Statistics> getStatistics()
	{
		return Collections.unmodifiableList(
				new ArrayList<Statistics>(mstat.values()));
	}
	
	public class Statistics
	{
		public List<String> getOptions()
		{
			List<String> lst = question.getOptions();
			if (lst == null || lst.isEmpty())
				return null;
			return lst;
		}
		
		public String getStatement()
		{
			return question.getStatement();
		}
		
		public Class<?> getQuestionClass()
		{
			return question.getContainerClass();
		}
		
		public Integer getUpperBound()
		{
			return question.getUpper();
		}
		
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
		
		private Statistics(Question q)
		{
			question = q;
		}
	}
	
	private AnswerHandle ah;
	private Map<Integer, Statistics> mstat;
	
	private class AnswerHandle
	{
		
		Map<Integer, Question> questions;
		Map<Integer, AnswerCount> answers;
		
		AnswerHandle()
		{
			answers = new TreeMap<Integer, AnswerCount>();
			questions = new TreeMap<Integer, Question>();
		}
		
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
	
	private class AnswerCount
	{
		Map<Object, Integer> answerCount;
		
		AnswerCount()
		{
			answerCount = new TreeMap<Object, Integer>();
		}
		
		void addAnswer(Object answer)
		{
			if (answer instanceof Object[])
			{
				List<Object> lobj = Arrays.asList((Object[]) answer);
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
