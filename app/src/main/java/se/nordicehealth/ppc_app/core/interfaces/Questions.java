package se.nordicehealth.ppc_app.core.interfaces;

import se.nordicehealth.ppc_app.core.containers.QuestionContainer;

public final class Questions
{
	public static QuestionContainer getContainer()
	{
		if (!loaded)
			loadQuestionnaire();
		return (QuestionContainer) qEntries.clone();
	}

	private static QuestionContainer qEntries;
    private static boolean loaded;

    static {
        qEntries = new QuestionContainer();
    }

    private static synchronized boolean loadQuestionnaire()
    {
        loaded = Implementations.Server().loadQuestions(qEntries);
        return loaded;
    }
}
