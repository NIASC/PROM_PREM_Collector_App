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

import core.containers.form.FieldContainer;
import core.containers.form.FormContainer;
import core.containers.form.SingleOptionContainer;
import core.containers.form.SliderContainer;

public class QuestionContainer
{
	/* public */
	
	public QuestionContainer()
	{
		questions = new HashMap<Integer, Question>();
		indexToID = new HashMap<Integer, Integer>();
		index = 0;
	}
	
	public void addQuestion(int id, String type, String question,
			String[] options, boolean optional, Integer upper, Integer lower)
	{
		if (questions.containsKey(id))
			return;
		Question q = new Question(
				id, type, question, options, optional, upper, lower);
		questions.put(id, q);
		indexToID.put(index++, id);
	}
	
	public FormContainer getQuestion(int index)
	{
		if (indexToID.containsKey(index)
				&& questions.containsKey(indexToID.get(index)))
		{
			return questions.get(indexToID.get(index)).getContainer();
		}
		return null;
	}
	
	public int getSize()
	{
		return index;
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
		
		public Question(int id, String type, String question,
				String[] options, boolean optional,
				Integer max, Integer min)
		{
			this.id = id;
			this.type = type;
			this.question = question;
			this.options = options;
			this.optional = optional;
			max_val = max;
			min_val = min;
		}
		
		public String type()
		{
			return type;
		}
		
		public String question()
		{
			return question;
		}
		
		public String[] options()
		{
			return options;
		}
		
		public boolean optional()
		{
			return optional;
		}
		
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
						optional, question, min_val, max_val);
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
		private Integer max_val, min_val;
	}
}
