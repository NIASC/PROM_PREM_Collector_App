package core.containers;

import java.util.HashMap;

public class OptionContainer
{
	private HashMap<Integer, Option> options;
	private Option selected;

	public OptionContainer()
	{
		options = new HashMap<Integer, Option>();
		selected = null;
	}
	
	/**
	 * Fills this container with options.
	 * 
	 * @param opt The options that should be stored in this container.
	 * 
	 * @return boolean True if the container was filled. False if
	 * 		an error occurred.
	 */
	public boolean fill(Option[] opt)
	{
		if (opt == null)
			return false;
		

		int id = 0;
		for (int i = 0; i < opt.length; ++i)
		{
			if (opt[i] != null)
				options.put(id++, opt[i]);
		}
		return true;
	}
	
	/**
	 * Puts the Options data contained in this class in an Integer-Option map.
	 * The Integer value is not related to the identifier in the Options class.
	 * 
	 * @return A map containing a map id and an Option.
	 */
	public HashMap<Integer, Option> get()
	{
		HashMap<Integer, Option> opt = new HashMap<Integer, Option>(options.size());
		opt.putAll(options);
		return opt;
	}
	
	/**
	 * Marks the Option associated with the id as selected.
	 * 
	 * @param id The id of the selected Option
	 * 
	 * @return boolean True if the selected Option was marked. False if the
	 * 		id is not in this container.
	 */
	public boolean setSelected(int id)
	{
		if (!options.containsKey(id))
			return false;
		selected = options.get(id);
		return true;
	}
	
	/**
	 * Returns the selected Option. If no option has been selected this function
	 * returns null.
	 * 
	 * @return Option The selected option or null if no option has been selected.
	 */
	public Option getSelected()
	{
		return Option.copy(selected);
	}
}
