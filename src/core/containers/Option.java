package core.containers;

/*
-----------------------------------------------------------------------_--------
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
