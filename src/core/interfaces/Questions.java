package core.interfaces;

import core.containers.QuestionContainer;

public final class Questions
{
	/* Public */

	@Override
	public final Object clone()
			throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	/**
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
		return Implementations.Database().loadQuestions(qEntries)
				== Database.QUERY_SUCCESS;
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
		QuestionContainer qc = null;
		qc = (QuestionContainer) qEntries.clone();
		return qc;
	}
	
	/* Protected */
	
	/* Private */

	private static Questions questions;
	private QuestionContainer qEntries;

}
