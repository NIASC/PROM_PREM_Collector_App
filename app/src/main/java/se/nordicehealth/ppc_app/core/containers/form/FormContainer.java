package se.nordicehealth.ppc_app.core.containers.form;

import se.nordicehealth.ppc_app.core.interfaces.UserInterface;
import se.nordicehealth.ppc_app.core.interfaces.UserInterface.FormComponentDisplay;

public abstract class FormContainer
{
	public abstract boolean hasEntry();
	public abstract Object getEntry();

	public FormComponentDisplay getDisplayable(UserInterface ui)
	{
		return ui.getContainerDisplay(this);
	}

	public boolean allowsEmpty()
	{
		return allowEmpty;
	}

	public String getStatement()
	{
		return statement;
	}

	public String getDescription()
	{
		return description;
	}

	FormContainer(boolean allowEmpty, String statement, String description)
	{
		this.allowEmpty = allowEmpty;
		this.statement = statement;
		this.description = description;
	}

    final boolean allowEmpty;
    boolean entryIsSet;

    private final String statement;
    private final String description;
}
