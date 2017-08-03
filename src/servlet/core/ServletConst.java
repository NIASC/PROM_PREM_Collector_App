package servlet.core;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ServletConst
{
	public static final URL LOCAL_URL;
	
	static {
		URL url = null;
		try
		{
			url = new URL("http://localhost:8080/PROM_PREM_Collector/main");
		}
		catch (MalformedURLException _e) { }
		LOCAL_URL = url;
	}

	public static final String CMD_ADD_USER		= "add_user";
	public static final String CMD_ADD_CLINIC	= "add_clinic";
	public static final String CMD_RSP_REGISTR	= "respond_registration";
}
