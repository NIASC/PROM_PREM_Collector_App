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
package servlet.manage;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import applet.core.interfaces.Database;
import applet.core.interfaces.Encryption;
import applet.core.interfaces.Implementations;

import java.util.Scanner;

/**
 * This class is used for managing the database and is only intended
 * to be used by administrators.
 * The managing includes adding new clinics and users to the database.
 * 
 * @author Marcus Malmquist
 *
 */
public class Manage
{
	/* Public */
	
	public static void main(String[] args)
	{
		Manage m = new Manage();
		m.runManager();
		
	}
	
	/**
	 * Initializes login variables.
	 */
	public Manage()
	{
		db = Implementations.Database();
		in = new Scanner(System.in);
	}
	
	/**
	 * Runs the manager main loop. The administrator is presented with
	 * available management operations and can choose which one to do.
	 */
	public void runManager()
	{
		boolean exit = false;
		final int EXIT = 0, ADD_CLINIC = 1, ADD_USER = 2;
		while (!exit)
		{
			System.out.printf(
					"What would you like to do?\n%d: %s\n%d: %s\n%d: %s\n",
					ADD_CLINIC, "Add Clinic",
					ADD_USER, "Add user",
					EXIT, "Exit");
			int input = EXIT;
			if (in.hasNextInt())
				input = in.nextInt();
			else
			{
				in.next();
				System.out.printf("Unknown option.\n\n");
				continue;
			}
			switch (input)
			{
			case EXIT:
				exit = true;
				break;
			case ADD_CLINIC:
				addClinic();
				break;
			case ADD_USER:
				addUser();
				break;
			default:
				System.out.printf("Unknown option.\n\n");
				break;
			}
		}
	}
	
	/* Protected */
	
	/* Private */
	
	/**
	 * Adds a new clinic to the database. The administrator will be
	 * presented with a form to fill in and the data is formatted
	 * such that the database can add the clinic.
	 */
	private void addClinic()
	{
		Map<Integer, String> clinics = db.getClinics();
		System.out.printf("Existing clinics:\n");
		for (Entry<Integer, String> e : clinics.entrySet())
			System.out.printf("%4d: %s\n", e.getKey(), e.getValue());
		System.out.printf("Enter new clinic:\n");
		String clinic = in.next();
		if (Pattern.compile("[^\\p{Print}]").matcher(clinic).find())
		{
			System.out.printf("Using non-ascii characters may cause trouble.\n\n");
			return;
		}
		for (String s : clinics.values())
			if (s.equals(clinic))
			{
				System.out.printf("That clinic already exist.\n\n");
				return;
			}
		db.addClinic(clinic);
	}
	
	/**
	 * Adds a new user to the database. The administrator will be
	 * presented with a form to fill in and the data is formatted
	 * such that the database can add the user.
	 */
	private void addUser()
	{
		Pattern validsRegEx = Pattern.compile("[^\\p{Print}]");
		System.out.printf("Enter Username:\n");
		String user = in.next();
		if (validsRegEx.matcher(user).find())
		{
			System.out.printf("Using non-ascii characters may cause trouble.\n\n");
			return;
		}
		if (db.getUser(user) != null)
		{
			System.out.printf("That username is not available.\n\n");
			return;
		}

		System.out.printf("Enter Password:\n");
		String password = in.next();
		if (validsRegEx.matcher(password).find())
		{
			System.out.printf("Using non-ascii characters may cause trouble.\n\n");
			return;
		}

		Map<Integer, String> clinics = db.getClinics();
		if (clinics.size() == 0)
		{
			System.out.printf("There are no clinics in the database.\n\n");
			return;
		}
		System.out.printf("Select Clinic:\n");
		for (Entry<Integer, String> e : clinics.entrySet())
			System.out.printf("%d: %s\n", e.getKey(), e.getValue());
		Integer clinic = null;
		if (in.hasNextInt())
			clinic = in.nextInt();
		else
		{
			in.next();
			System.out.printf("No such clinic.\n\n");
			return;
		}

		System.out.printf("Enter Email:\n");
		String email = in.next();
		Encryption crypto = Implementations.Encryption();
		String salt = crypto.getNewSalt();
		db.addUser(user, crypto.hashString(password, salt), salt, clinic, email);
	}
	private Scanner in;
	private Database db;
}
