package implement;

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
