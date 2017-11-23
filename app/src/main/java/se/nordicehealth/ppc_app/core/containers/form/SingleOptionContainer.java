package se.nordicehealth.ppc_app.core.containers.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SingleOptionContainer extends FormContainer
{
	public SingleOptionContainer(boolean allowEmpty, String statement, String description)
	{
		super(allowEmpty, statement, description);
		options = new ArrayList<>();
		selected = null;
	}

	@Override
	public boolean hasEntry()
	{
		return entryIsSet && (allowEmpty || selected != null);
	}
	
	@Override
	public Integer getEntry()
	{
		return selected != null ? options.get(selected).identifier : null;
	}

	public boolean setEntry(Integer id)
	{
		entryIsSet = true;
		if (id == null || options.get(id) == null)
			return false;

		selected = id;
		return true;
	}

	public void addOption(int identifier, String text)
	{
		options.add(new Option(identifier, text));
	}

	public Map<Integer, String> getOptions()
	{
		Map<Integer, String> sopts = new TreeMap<>();
        int i = 0;
		for (Option o : options)
			sopts.put(i++, o.text);
		return sopts;
	}

	public Integer getSelectedID()
	{
		return selected;
	}
	
	private List<Option> options;
	private Integer selected;

	private final class Option
	{
		final int identifier;
		final String text;

		Option(int identifier, String text)
		{
			this.identifier = identifier;
			this.text = text;
		}
	}
}
