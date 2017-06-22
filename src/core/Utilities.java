package core;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * This class consists of static functions that can be useful but don't
 * really fit anywhere.
 * 
 * @author Marcus Malmquist
 *
 */
public class Utilities
{
	/**
	 * Finds the system path to the folder where the supplied class
	 * is located.
	 * 
	 * @param c The class that the path finding will be based on.
	 * 
	 * @return The /path/to/class/c.
	 */
	@SuppressWarnings("rawtypes")
	public static String getClassPath(Class c)
	{
		URL url = c.getProtectionDomain().getCodeSource().getLocation();
		String projectPath = null;
		try {
			projectPath = URLDecoder.decode(url.getFile(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new File(projectPath).getParentFile().getPath();
	}
}
