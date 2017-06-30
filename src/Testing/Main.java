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
package Testing;

import java.awt.GraphicsEnvironment;

import core.UserHandle;
import core.containers.User;
import core.interfaces.Database;
import core.interfaces.Implementations;
import core.interfaces.Messages;
import core.interfaces.UserInterface;

public class Main
{
	public static void main(String[] args)
	{
		/*
		Database db = Implementations.Database();
		db.connect();
		for (int i = 0; i < 20; ++i)
		{
			(new Thread(new tClass(db))).start();
		}
		*/
		
		//printFonts();
		if (!Messages.getMessages().loadMessages())
		{
			System.out.printf("%s\n", Messages.DATABASE_ERROR);
			System.exit(1);
		}
		SwingUserInterface qf1 = new SwingUserInterface();
		qf1.start();
		
		/*
		CLI_UserInterface ui = new CLI_UserInterface();
		ui.open();
		 */
	}
	
	public static class tClass implements Runnable
	{
		Database db;
		public tClass(Database db)
		{
			this.db = db;
		}
		@Override
		public void run() {
			for (int i = 0; i < 10; ++i)
			{
				User user = db.getUser("user#0");
				System.out.printf("%s, %b\n", user.getUsername(), user.passwordMatch("p4ssw0rd"));
			}
		}
		
	}
	
	public static void printFonts()
	{
		String fonts[] = 
				GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		
		for ( int i = 0; i < fonts.length; i++ )
		{
			System.out.println(fonts[i]);
		}
	}
}
