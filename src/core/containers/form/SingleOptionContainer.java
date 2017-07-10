/**
 * Copyright 2017 Marcus Malmquist
 * 
 * This file is part of PROM_PREM_Collector.
 * 
 * PROM_PREM_Collector is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * PROM_PREM_Collector is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PROM_PREM_Collector.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package core.containers.form;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * This class handles single-option objects. It allows you to
 * group several options with individual identifiers and text to form
 * a complete set of options to choose from as well as a way of
 * retrieving the selected option.
 * A single-option object consists of a statement and multiple options
 * to choose from and it only allows the user to choose one of the
 * options.
 * 
 * @author Marcus Malmquist
 * 
 * @see FormContainer
 *
 */
public class SingleOptionContainer extends FormContainer
{
	/* Public */
	
	/**
	 * Initializes a single-option container that does not allow empty
	 * entries;
	 */
	public SingleOptionContainer()
	{
		this(false, null);
	}
	
	/**
	 * Initializes this container without a description.
	 * 
	 * @param allowEmptyEntry
	 */
	public SingleOptionContainer(boolean allowEmptyEntry)
	{
		this(allowEmptyEntry, null);
	}
	
	/**
	 * Creates a container for single-option objects.
	 * 
	 * @param allowEmptyEntry {@code true} if this container allows
	 * 		empty entry (answer/response). {@code false} if not.
	 * @param description The description of this entry, i.e. what the
	 * 		different options mean.
	 */
	public SingleOptionContainer(boolean allowEmptyEntry, String description)
	{
		super(allowEmptyEntry);
		this.description = description;
		nextFC = prevFC = null;
		options = new HashMap<Integer, SingleOption>();
		nextOption = 0;
		selectedID = null;
	}

	@Override
	public boolean hasEntry()
	{
		return allowEmpty || selectedID != null;
	}

	@Override
	public SingleOptionContainer copy()
	{
		SingleOptionContainer soc = new SingleOptionContainer(allowEmpty, description);
		for (Entry<Integer, SingleOption> e : options.entrySet())
			soc.addSingleOption(e.getKey(), e.getValue().text);
		return soc;
	}
	
	@Override
	public Integer getEntry()
	{
		return getSelected();
	}
	
	/**
	 * Marks the option with the supplied ID as selected, if an option
	 * with that ID exists in the list of options.
	 * 
	 * @param id The ID of the selected option.
	 * @return True if an option with the supplied ID exists (and that
	 * 		option was marked as selected).
	 * 		False if not (and no option has been marked as selected).
	 */
	@Override
	public <T extends Object> boolean setEntry(T id)
	{
		if (!(id instanceof Integer))
			return false;
		int selID = ((Integer) id).intValue();
		if (options.get(selID) == null)
			return false;
		selectedID = selID;
		return true;
	}
	
	/**
	 * Adds a new option to the container.
	 * 
	 * @param identifier The identifier of the option. This is
	 * 		typically used to identify what action should be taken
	 * 		if this option is selected.
	 * @param text The text that describes what this option means.
	 */
	public synchronized void addSingleOption(int identifier, String text)
	{
		options.put(nextOption++, new SingleOption(identifier, text));
	}
	
	/**
	 * Puts the options in a map that contains the ID of the option
	 * as well as the option's text. The ID should be used to mark
	 * the selected option through the method setSelected.
	 * The ID ranges from 0 <= ID < <no. entries>.
	 * 
	 * @return
	 */
	public HashMap<Integer, String> getSingleOptions()
	{
		HashMap<Integer, String> sopts = new HashMap<Integer, String>();
		for (Entry<Integer, SingleOption> e : options.entrySet())
			sopts.put(e.getKey(), e.getValue().text);
		return sopts;
	}
	
	/**
	 * Retrieves the identifier of the selected option. If no option
	 * has been selected the method returns null.
	 * 
	 * @return The identifier of the selected option, or null if no
	 * 		option has been selected;
	 */
	public Integer getSelected()
	{
		if (selectedID == null)
			return null;
		return options.get(selectedID).identifier;
	}
	
	public Integer getSelectedID()
	{
		return selectedID;
	}
	
	/**
	 * Sets this container's description to {@code description}.
	 * 
	 * @param description This container's description.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * 
	 * @return This container's description.
	 */
	public String getDescription()
	{
		return description;
	}
	
	/* Protected */
	
	/* Private */
	
	private String description;
	private HashMap<Integer, SingleOption> options;
	private int nextOption;
	private Integer selectedID;
	
	/**
	 * This class is a data container for single-option form entries.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private final class SingleOption
	{
		/**
		 * The identifier for this Option.
		 */
		final int identifier;
		/**
		 * The text for this Option.
		 */
		final String text;
		
		/**
		 * Initializes the class with an identifier and a text.
		 * The identifier is useful to make it easier to identify what
		 * action should be taken if this option is selected.
		 * 
		 * @param identifier The option identifier.
		 * @param text The option text.
		 */
		SingleOption(int identifier, String text)
		{
			this.identifier = identifier;
			this.text = text;
		}
	}
}
