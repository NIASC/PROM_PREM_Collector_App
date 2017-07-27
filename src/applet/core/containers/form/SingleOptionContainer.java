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
package applet.core.containers.form;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
	 * Creates a container for single-option objects.
	 * 
	 * @param allowEmptyEntry {@code true} if this container allows
	 * 		empty entry (answer/response). {@code false} if not.
	 * @param statement The statement that the user should respond to.
	 * 		The statement should be relevant to the options that are
	 * 		added later.
	 * @param description A more detailed description of the
	 * 		{@code statement}.
	 */
	public SingleOptionContainer(boolean allowEmptyEntry, String statement,
			String description)
	{
		super(allowEmptyEntry, statement, description);
		
		options = new TreeMap<Integer, Option>();
		nextOption = 0;
		selected = null;
	}

	@Override
	public boolean hasEntry()
	{
		return entrySet && (allowEmpty || selected != null);
	}

	@Override
	public Object clone()
	{
		SingleOptionContainer soc = new SingleOptionContainer(
				allowEmpty, statement, description);
		for (Entry<Integer, Option> e : options.entrySet())
			soc.addOption(e.getKey(), e.getValue().text);
		return soc;
	}
	
	@Override
	public Integer getEntry()
	{
		return selected != null ? options.get(selected).identifier : null;
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
	public boolean setEntry(Integer id)
	{
		entrySet = true;
		if (id == null || options.get(id) == null)
			return false;

		selected = id;
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
	public synchronized void addOption(int identifier, String text)
	{
		options.put(nextOption++, new Option(identifier, text));
	}
	
	/**
	 * Puts the options in a map that contains the ID of the option
	 * as well as the option's text. The ID should be used to mark
	 * the selected option through the method setSelected.
	 * The ID ranges from 0 <= ID < <no. entries>.
	 * 
	 * @return A map of the options, the keys are the ID of the options
	 * 		(the order at which they where added to this container,
	 * 		starting from 0).
	 */
	public Map<Integer, String> getOptions()
	{
		Map<Integer, String> sopts = new TreeMap<Integer, String>();
		for (Entry<Integer, Option> e : options.entrySet())
			sopts.put(e.getKey(), e.getValue().text);
		return sopts;
	}
	
	public Integer getSelectedID()
	{
		return selected;
	}
	
	/* Protected */
	
	/* Private */
	
	private Map<Integer, Option> options;
	private int nextOption;
	
	private Integer selected;
	
	/**
	 * This class is a data container for single-option form entries.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private final class Option
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
		Option(int identifier, String text)
		{
			this.identifier = identifier;
			this.text = text;
		}
	}
}
