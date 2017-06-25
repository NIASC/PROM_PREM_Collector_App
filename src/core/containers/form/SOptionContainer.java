package core.containers.form;

import java.util.HashMap;
import java.util.Map.Entry;

import implement.UserInterface;

public class SOptionContainer extends FContainer
{
	private HashMap<Integer, SOption> options;
	private int nextOption;
	private SOption selected;
	
	public SOptionContainer()
	{
		nextFC = prevFC = null;
		options = new HashMap<Integer, SOption>();
		nextOption = 0;
		selected = null;
	}
	
	/** a */
	/**
	 * Adds a new option to the container.
	 * 
	 * @param identifier The identifier of the SOption. This is
	 * 		typically used to identify what action should be taken
	 * 		if this option is selected.
	 * @param text The text that describes what this option means.
	 */
	public void addSOption(int identifier, String text)
	{
		options.put(nextOption++, new SOption(identifier, text));
	}
	
	/**
	 * Puts the options in a map that contains the ID of the option
	 * as well as the option's text. The ID should be used to mark
	 * the selected option through the method setSelected.
	 * The ID ranges from 0 <= ID < <no. entries>.
	 * 
	 * @return
	 */
	public HashMap<Integer, String> getSOptions()
	{
		HashMap<Integer, String> sopts = new HashMap<Integer, String>();
		for (Entry<Integer, SOption> e : options.entrySet())
			sopts.put(e.getKey(), e.getValue().getText());
		return sopts;
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
	public boolean setSelected(int id)
	{
		return (selected = options.get(id)) != null;
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
		return selected != null ? selected.getIdentifier() : null;
	}

	@Override
	public void draw(UserInterface ui) {}
	
	private class SOption
	{
		private int identifier;
		private String text;
		
		/**
		 * Initializes the class with an identifier and a text.
		 * The identifier is useful to make it easier to identify what
		 * action should be taken if this option is selected.
		 * 
		 * @param identifier The option identifier.
		 * @param text The option text.
		 */
		public SOption(int identifier, String text)
		{
			this.identifier = identifier;
			this.text = text;
		}
		
		/**
		 * 
		 * @return The identifier for this Option.
		 */
		public int getIdentifier()
		{
			return identifier;
		}
		
		/**
		 * 
		 * @return The text for this Option.
		 */
		public String getText()
		{
			return text;
		}
		
		/**
		 * Copies the option from the supplied Option.
		 * 
		 * @param opt The option to copy from.
		 * @return A copy of the supplied Option.
		 */
		public SOption makeCopy()
		{
			return new SOption(identifier, text);
		}
	}
}
