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

import java.util.HashMap;

import core.Questionnaire;
import core.containers.form.FieldContainer;
import core.containers.form.FormContainer;
import core.containers.form.SingleOptionContainer;
import core.containers.form.SliderContainer;

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
	
	/**
	 * Creates a container for {@code Questionnaire} entries.
	 */
	public QuestionContainer()
	{
		questions = new HashMap<Integer, Question>();
		indexToID = new HashMap<Integer, Integer>();
		index = 0;
	}
	
	/**
	 * Adds a {@code Questionnaire} entry to this container.
	 * 
	 * @param id The ID of the question (as it appears in the
	 * 		database).
	 * @param type The type of question (Slider, SelectOption, Field
	 * 		etc.) as it appears in the database.
	 * @param question The question/statement that the user should
	 * 		respond to.
	 * @param options The available options to select in response to
	 * 		the question/statement. If this entry type does not have
	 * 		options this variable will be discarded can be set to
	 * 		{@null}.
	 * @param optional If it is <i><b>not</b></i> required to answer
	 * 		this question this should be set to {@code true} else
	 * 		{@code false}.
	 * @param upper The upper limit for this entry. If this user is
	 * 		supposed to enter a numerical value this should be the
	 * 		upper limit for that value.
	 * @param lower The lower limit for this entry. If this user is
	 * 		supposed to enter a numerical value this should be the
	 * 		lower limit for that value.
	 * 
	 * @see Questionnaire
	 */
	public synchronized void addQuestion(int id, String type, String question,
			String[] options, boolean optional, Integer upper, Integer lower)
	{
		if (questions.containsKey(id))
			return;
		Question q = new Question(
				id, type, question, options, optional, upper, lower);
		questions.put(id, q);
		indexToID.put(index++, id);
	}
	
	/**
	 * Retrieves the question labeled with {@code index} which
	 * represents the order in which questions were added.
	 * 
	 * @param index The index for the question.
	 * 
	 * @return The container for the question labeled with
	 * 		{@code index}.
	 */
	public FormContainer getQuestion(int index)
	{
		if (indexToID.containsKey(index)
				&& questions.containsKey(indexToID.get(index)))
		{
			return questions.get(indexToID.get(index)).getContainer();
		}
		return null;
	}
	
	/**
	 * 
	 * @return The size (number of entries) of this container.
	 */
	public int getSize()
	{
		return index;
	}
	
	public QuestionContainer copy()
	{
		QuestionContainer qc = new QuestionContainer();
		synchronized (this)
		{
			for (int i = 0; i < index; ++i)
			{
				Question q = questions.get(indexToID.get(i));
				qc.addQuestion(indexToID.get(i), q.type(), q.question(),
						q.options(), q.optional(), q.getUpper(), q.getLower());
			}
		}
		return qc;
	}
	
	/* protected */
	
	/* private */

	private HashMap<Integer, Question> questions;
	private HashMap<Integer, Integer> indexToID;
	private int index;
	
	/**
	 * This class contains questionnaire questions.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private class Question
	{
		/* public */
		
		/**
		 * Adds a {@code Questionnaire} entry to this container.
		 * 
		 * @param id The ID of the question (as it appears in the
		 * 		database).
		 * @param type The type of question (Slider, SelectOption,
		 * 		Field etc.) as it appears in the database.
		 * @param question The question/statement that the user should
		 * 		respond to.
		 * @param options The available options to select in response
		 * 		to the question/statement. If this entry type does not
		 * 		have options this variable will be discarded can be
		 * 		set to {@null}.
		 * @param optional If it is <i><b>not</b></i> required to
		 * 		answer this question this should be set to
		 * 		{@code true} else {@code false}.
		 * @param upper The upper limit for this entry. If this user
		 * 		is supposed to enter a numerical value this should be
		 * 		the upper limit for that value.
		 * @param lower The lower limit for this entry. If this user
		 * 		is supposed to enter a numerical value this should be
		 * 		the lower limit for that value.
		 */
		public Question(int id, String type, String question,
				String[] options, boolean optional,
				Integer upper, Integer lower)
		{
			this.id = id;
			this.type = type;
			this.question = question;
			this.options = options;
			this.optional = optional;
			maxVal = upper;
			minVal = lower;
		}
		
		/**
		 * 
		 * @return The type of this question (Slider, SelectOption,
		 * 		Field etc.)
		 */
		public String type()
		{
			return type;
		}
		
		/**
		 * 
		 * @return The question/statement of this question.
		 */
		public String question()
		{
			return question;
		}
		
		/**
		 * 
		 * @return the available options for this question.
		 */
		public String[] options()
		{
			return options;
		}
		
		/**
		 * 
		 * @return {@code true} if it is not required to answer this
		 * 		question. Else {@code false}.
		 */
		public boolean optional()
		{
			return optional;
		}
		
		/**
		 * 
		 * @return The upper limit for this entry.
		 */
		public Integer getUpper()
		{
			return maxVal;
		}
		
		/**
		 * 
		 * @return The lower limit for this entry.
		 */
		public Integer getLower()
		{
			return minVal;
		}
		
		/**
		 * Places this question in an appropriate container based on
		 * the type of question this is.
		 * 
		 * @return The container for this question.
		 * 
		 * @see FormContainer
		 */
		public FormContainer getContainer()
		{
			if (type.equalsIgnoreCase("SingleOption"))
			{
				SingleOptionContainer soc =
						new SingleOptionContainer(optional, question);
				for (int i = 0; i < options.length; ++i)
					soc.addSingleOption(i, options[i]);
				return soc;
			}
			else if (type.equalsIgnoreCase("Field"))
				return new FieldContainer(optional, false, question);
			else if (type.equalsIgnoreCase("Slider"))
				return new SliderContainer(
						optional, question, minVal, maxVal);
			else
				return null;
		}
		
		/* protected */
		
		/* private */
		
		private int id;
		private boolean optional;
		private String type;
		private String question;
		private String[] options;
		private Integer maxVal, minVal;
	}
}
