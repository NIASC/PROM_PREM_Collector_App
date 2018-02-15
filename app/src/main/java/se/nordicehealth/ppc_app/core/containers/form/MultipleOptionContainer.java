package se.nordicehealth.ppc_app.core.containers.form;

import java.util.Collections;
import java.util.List;

public class MultipleOptionContainer extends SingleOptionContainer
{
	public MultipleOptionContainer(boolean allowEmpty, String statement, String description)
	{
		super(allowEmpty, statement, description);
	}

	@Override
	public boolean hasEntry() {
		return entryIsSet && (allowEmpty || !selected.isEmpty());
	}

	@Override
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
}
