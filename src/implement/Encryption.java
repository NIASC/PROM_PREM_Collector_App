package implement;

/**
 * This class is an example of an implementation of
 * Entryption_Interface.
 * 
 * @author Marcus Malmquist
 *
 */
public class Encryption implements Encryption_interface
{

	@Override
	public String hashString(String s, String salt)
	{
		return s + salt;
	}

	public String getNewSalt()
	{
		return "";
	}
	
	public Encryption()
	{
		
	}

}
