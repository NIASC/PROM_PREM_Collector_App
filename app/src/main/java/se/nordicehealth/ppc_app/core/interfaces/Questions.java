/*! Questions.java
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
package se.nordicehealth.ppc_app.core.interfaces;

import se.nordicehealth.ppc_app.core.containers.QuestionContainer;

/**
 * This class contains the questionnaire questions. The questions should
 * be loaded into this class when the program starts.
 * 
 * @author Marcus Malmquist
 *
 */
public final class Questions
{
	@Override
	public final Object clone()
			throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	/**
	 * Retrieves the active instance of this class.
	 * 
	 * @return The active instance of this class.
	 */
	public static synchronized Questions getQuestions()
	{
		if (questions == null)
			questions = new Questions();
		return questions;
	}
	
	/**
	 * Attempts to load questions from the {@code Database}.
	 * 
	 * @return {@code true} if questions was successfully loaded.
	 * 		{@code false} if an error occurred while loading questions.
	 * 
	 * @see Database
	 */
	public synchronized final boolean loadQuestionnaire()
	{
		qEntries = new QuestionContainer();
		return Implementations.Database().loadQuestions(qEntries);
	}
	
	/**
	 * 
	 * @return A {@code QuestionContainer} that contains the
	 * 		questionnaire entires from the database.
	 */
	public QuestionContainer getContainer()
	{
		if (qEntries == null && !loadQuestionnaire())
			return null;
		return (QuestionContainer) qEntries.clone();
	}

	private Questions()
	{

	}

	private static Questions questions;
	private QuestionContainer qEntries;

}
