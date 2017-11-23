package se.nordicehealth.ppc_app.core.interfaces;

import se.nordicehealth.ppc_app.core.containers.QuestionContainer;

public final class Questions
{
	public static QuestionContainer getContainer()
	{
		if (qEntries == null)
            qEntries = loadQuestionnaire();
		return (QuestionContainer) qEntries.clone();
	}

	private static QuestionContainer qEntries;

    private static synchronized QuestionContainer loadQuestionnaire()
    {
        return Implementations.Server().loadQuestions();
    }
}
