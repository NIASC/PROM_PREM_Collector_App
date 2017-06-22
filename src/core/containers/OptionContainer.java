package core.containers;

import java.util.HashMap;

/**
 * This class is a handles for Option objects. It allows you to group
 * several Options objects to form a complete set of options to choose
 * from as well as a way of retrieving the selected option.
 * The purpose of this class is to encapsulate this information into a
 * class so it can be passed as an argument and be easily modifiable.
 * 
 * @author Marcus Malmquist
 *
 */
public class OptionContainer
{
	private HashMap<Integer, Option> options;
	private HashMap<Integer, Integer> keyToID;
	private int optionID;
	private Option selected;

	/**
	 * Initializes variables.
	 */
	public OptionContainer()
	{
		options = new HashMap<Integer, Option>();
		keyToID = new HashMap<Integer, Integer>();
		optionID = 0;
		selected = null;
	}
	
	/**
	 * Adds an Option to this container.
	 * 
	 * @param option The Option to add.
	 */
	public void addOption(Option option)
	{
		if (option == null)
			return;
		options.put(optionID, option);
		keyToID.put(option.getIdentifier(), optionID++);
	}
	
	/**
	 * Puts the Options data contained in this class in an
	 * Integer-Option map.
	 * The Integer value is not related to the identifier in the
	 * Options class.
	 * 
	 * @return A map containing a map id and an Option.
	 */
	public HashMap<Integer, Option> get()
	{
		return options;
	}
	
	/**
	 * Marks the Option associated with the id as selected.
	 * 
	 * @param id The id of the selected Option
	 * 
	 * @return boolean True if the selected Option was marked. False if
	 * 		the id is not in this container.
	 */
	public boolean setSelected(int id)
	{
		if (!options.containsKey(id))
			return false;
		selected = options.get(id);
		return true;
	}
	
	/**
	 * Returns the selected Option. If no option has been selected this
	 * 		function returns null.
	 * 
	 * @return Option The selected option or null if no option has been
	 * 		selected.
	 */
	public Option getSelected()
	{
		return Option.copy(selected);
	}
}
