/**
 * Copyright 2017 Marcus Malmquist
 * 
 * This file is part of PROM_PREM_Collector.
 * 
 * PROM_PREM_Collector is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * PROM_PREM_Collector is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with PROM_PREM_Collector.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
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
