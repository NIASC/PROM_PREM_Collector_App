package se.nordicehealth.ppc_app.core.containers.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MultipleOptionContainer extends FormContainer
{
	public MultipleOptionContainer(boolean allowEmpty, String statement, String description)
	{
		super(allowEmpty, statement, description);
		options = new ArrayList<>();
		selected = new ArrayList<>();
	}

	@Override
	public boolean hasEntry()
	{
		return entryIsSet && (allowEmpty || !selected.isEmpty());
	}
	
	@Override
	public List<Integer> getEntry()
	{
		return Collections.unmodifiableList(selected);
	}

	public boolean setEntry(List<Integer> selectedIDs)
	{
		entryIsSet = true;
		if (selectedIDs == null)
			return false;

		selected.clear();
		for (Integer i : selectedIDs)
			selected.add(i);
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

	public List<Integer> getSelectedIDs()
	{
		return Collections.unmodifiableList(selected);
	}
	
	private List<Option> options;
	private List<Integer> selected;

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
