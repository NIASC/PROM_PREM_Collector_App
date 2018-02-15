package se.nordicehealth.ppc_app.core.containers.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SingleOptionContainer extends FormContainer
{
	public SingleOptionContainer(boolean allowEmpty, String statement, String description)
	{
		super(allowEmpty, statement, description);
		options = new ArrayList<>();
        selected = new ArrayList<>();
	}

	@Override
	public boolean hasEntry() {
		return entryIsSet && (allowEmpty || (!selected.isEmpty() && selected.get(0) != null));
	}
	
	@Override
	public List<Integer> getEntry() {
        return Collections.unmodifiableList(selected);
	}

	public boolean setEntry(List<Integer> selectedIDs)
	{
        entryIsSet = true;
        if (selectedIDs == null)
            return false;

        selected.clear();
        if (!selectedIDs.isEmpty()) {
			selected.add(selectedIDs.get(0));
		}
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

	public List<Integer> getSelectedID()
	{
		return Collections.unmodifiableList(selected);
	}
	
	private List<Option> options;
    List<Integer> selected;

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
