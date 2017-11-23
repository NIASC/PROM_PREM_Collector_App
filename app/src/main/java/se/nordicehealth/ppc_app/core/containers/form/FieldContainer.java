package se.nordicehealth.ppc_app.core.containers.form;

public class FieldContainer extends AreaContainer
{
	public FieldContainer(boolean allowEmpty, boolean secretEntry, String statement, String description)
	{
		super(allowEmpty, statement, description, 64);
		secret = secretEntry;
	}

	public boolean isSecret()
	{
		return secret;
	}

	private final boolean secret;
}
