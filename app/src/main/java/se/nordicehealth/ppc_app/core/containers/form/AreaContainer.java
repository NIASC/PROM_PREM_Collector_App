package se.nordicehealth.ppc_app.core.containers.form;

public class AreaContainer extends FormContainer
{
	public AreaContainer(boolean allowEmpty, String statement, String description, int capacity)
	{
		super(allowEmpty, statement, description);
		entryIsSet = false;
		this.capacity = capacity;
	}

	@Override
	public boolean hasEntry()
	{
		return entryIsSet && (allowEmpty || (entry != null && !entry.isEmpty()));
	}
	
	@Override
	public String getEntry()
	{
		return entry;
	}

	public boolean setEntry(String entry)
	{
		entryIsSet = true;
		if (entry == null) {
            this.entry = null;
            return true;
        } else {
			entry = entry.trim();
			if (entry.isEmpty() && !allowEmpty)
				return false;
			this.entry = entry.substring(0, Math.min(entry.length(), capacity));
            return true;
		}
	}

	protected String entry;

	private final int capacity;
}
