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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import core.Questionnaire;
import core.containers.form.FieldContainer;
import core.containers.form.FormContainer;
import core.containers.form.MultipleOptionContainer;
import core.containers.form.SingleOptionContainer;
import core.containers.form.SliderContainer;
import core.containers.form.TimePeriodContainer;

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
	 * @param description TODO
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
	 * @see Questionnaire
	 */
	public synchronized void addQuestion(int id,
			Class<? extends FormContainer> type, String question,
			String description, List<String> options, boolean optional,
			Integer upper, Integer lower)
	{
		if (questions.containsKey(id))
			return;
		Question q = new Question(
				id, type, question, description, options, optional,
				upper, lower);
		questions.put(id, q);
		indexToID.put(index++, id);
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
	
	public Question getQuestion(int index)
	{
		if (indexToID.containsKey(index)
				&& questions.containsKey(indexToID.get(index)))
		{
			return questions.get(indexToID.get(index));
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
				qc.addQuestion(
						indexToID.get(i), q.type, q.question, q.description,
						q.options, q.optional, q.upper, q.lower);
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
	public final class Question
	{
		public int getID() { return id; }
		public boolean isOptional() { return optional; }
		public String getStatement() { return question; }
		public String getDescription() { return description; }
		public String getOptions(int id)
		{
			String optn = null;
			try
			{
				optn = options.get(id);
			}
			catch (IndexOutOfBoundsException e) {
				/* optn is already null */
			}
			return optn;
		}
		public int getUpper() { return upper; }
		public int getLower() { return lower; }
		
		private int id;
		
		/**
		 * Whether or not it is not required to answer this question.
		 */
		private boolean optional;
		/**
		 * The type of question (SliderContainer, SingleOptionContainer,
		 * FieldContainer etc.)
		 */
		private Class<? extends FormContainer> type;
		
		/**
		 * The question/statement of this question.
		 */
		private String question;
		
		/**
		 * A more detailed description of the {@link #question}.
		 */
		private String description;
		
		/**
		 * The available options for this question.
		 */
		private List<String> options;
		
		/**
		 * The upper limit for this entry.
		 */
		private Integer upper;
		
		/**
		 * The lower limit for this entry.
		 */
		private Integer lower;
		
		/**
		 * Adds a {@code Questionnaire} entry to this container.
		 * 
		 * @param id The ID of the question (as it appears in the
		 * 		database).
		 * @param type The type of question as it appears in the
		 * 		database. Valid types are currently: SingleOption,
		 * 		Slider, Field
		 * @param question The question/statement that the user should
		 * 		respond to.
		 * @param description TODO
		 * @param options The available options to select in response
		 * 		to the question/statement. If this entry type does not
		 * 		have options this variable will be discarded can be
		 * 		set to {@code null}.
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
		private Question(int id, Class<? extends FormContainer> type,
				String question, String description,
				List<String> options, boolean optional, Integer upper, Integer lower)
		{
			this.id = id;
			this.type = type;
			this.question = question;
			this.description = description;
			this.options = options;
			this.optional = optional;
			this.upper = upper;
			this.lower = lower;
		}
		
		/**
		 * Places this question in an appropriate container based on
		 * the type of question this is.
		 * 
		 * @return The container for this question.
		 * 
		 * @see FormContainer
		 */
		private FormContainer getContainer()
		{
			if (type == null)
				return null;
			if (type.isAssignableFrom(SingleOptionContainer.class))
			{
				SingleOptionContainer soc = new SingleOptionContainer(
						optional, question, description);
				int i = 0;
				for (Iterator<String> itr = options.iterator(); itr.hasNext(); ++i)
					soc.addOption(i, itr.next());
				return soc;
			}
			else if (type.isAssignableFrom(MultipleOptionContainer.class))
			{
				MultipleOptionContainer moc = new MultipleOptionContainer(
						optional, question, description);
				int i = 0;
				for (Iterator<String> itr = options.iterator(); itr.hasNext(); ++i)
					moc.addOption(i, itr.next());
				return moc;
			}
			else if (type.isAssignableFrom(FieldContainer.class))
				return new FieldContainer(optional, false, question, description);
			else if (type.isAssignableFrom(SliderContainer.class))
				return new SliderContainer(
						optional, question, description, lower, upper);
			else if (type.isAssignableFrom(TimePeriodContainer.class))
				return null;
			else
				return null;
		}
	}
}
