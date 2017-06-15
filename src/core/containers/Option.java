package core.containers;

/*
--------------------------------------------------------------------------------
 */
public class Option
{
	private int identifier;
	private String text;
	
	/**
	 * Initializes the class with an identifier and a text.
	 * The identifier is useful to make it easier to identify what action should
	 * be taken if this option is selected.
	 * @param identifier The option identifier.
	 * @param text The option text.
	 */
	public Option(int identifier, String text)
	{
		this.identifier = identifier;
		this.text = text;
	}
	
	public int getIdentifier()
	{
		return identifier;
	}
	
	public String getText()
	{
		return text;
	}
	
	public static Option copy(final Option opt)
	{
		return new Option(opt.getIdentifier(), opt.getText());
	}
}
