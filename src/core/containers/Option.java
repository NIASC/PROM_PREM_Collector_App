package core.containers;

/**
 * This class is a data container for Options. An option is a
 * structure which has a key/id associated with information about what
 * the option is for as well as an action code (in the event that the
 * option is selected).
 * The purpose of this class is to encapsulate this information into a
 * class so it can be passed as an argument and be easily modifiable.
 * 
 * @author Marcus Malmquist
 *
 */
public class Option
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
	public Option(int identifier, String text)
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
	public static Option copy(final Option opt)
	{
		if (opt == null)
			return null;
		return new Option(opt.getIdentifier(), opt.getText());
	}
}
