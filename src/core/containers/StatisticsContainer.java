package core.containers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StatisticsContainer
{
	public StatisticsContainer()
	{
		questions = new HashMap<String, AnswerCount>();
	}
	
	public void addResult(String questionNumber, String answer)
	{
		if (!questions.containsKey(questionNumber))
			questions.put(questionNumber, new AnswerCount());
		questions.get(questionNumber).addAnswer(answer);
	}
	
	public Map<String, Map<String, Integer>> getStatistics()
	{
		Map<String, Map<String, Integer>> result =
				new HashMap<String, Map<String, Integer>>();
		for (Entry<String, AnswerCount> e : questions.entrySet())
		{
			result.put(e.getKey(),
					Collections.unmodifiableMap(e.getValue().answerCount));
		}
		return Collections.unmodifiableMap(result);
	}
	
	private HashMap<String, AnswerCount> questions;
	
	private class AnswerCount
	{
		HashMap<String, Integer> answerCount;
		
		AnswerCount()
		{
			answerCount = new HashMap<String, Integer>();
		}
		
		void addAnswer(String answer)
		{
			if (!answerCount.containsKey(answer))
				answerCount.put(answer, 0);
			answerCount.put(answer, answerCount.get(answer).intValue() + 1);
		}
	}
}
