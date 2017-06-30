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
package manage;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import core.interfaces.Database;
import core.interfaces.Encryption;
import core.interfaces.Implementations;

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
	private Scanner in;
	private Database db;
	
	/**
	 * Initializes login variables.
	 */
	public Manage()
	{
		db = Implementations.Database();
		in = new Scanner(System.in);
	}
	
	/**
	 * Adds a new clinic to the database. The administrator will be
	 * presented with a form to fill in and the data is formatted
	 * such that the database can add the clinic.
	 */
	private void addClinic()
	{
		System.out.printf("Clinic?\n");
		String clinic = in.next();
		if (Pattern.compile("[^\\p{Print}]").matcher(clinic).find())
		{
			System.err.println("Using non-ascii characters may cause trouble. Exiting.");
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
		System.out.printf("Enter Username\n");
		String user = in.next();
		if (validsRegEx.matcher(user).find())
		{
			System.err.println("Using non-ascii characters may cause trouble. Exiting.");
			return;
		}

		System.out.printf("Enter Password\n");
		String password = in.next();
		if (validsRegEx.matcher(password).find())
		{
			System.err.println("Using non-ascii characters may cause trouble. Exiting.");
			return;
		}

		HashMap<Integer, String> clinics = db.getClinics();
		if (clinics.size() == 0)
		{
			System.out.printf("There are no clinics in the database\n");
			return;
		}
		System.out.printf("Select Clinic\n");
		for (Entry<Integer, String> e : clinics.entrySet())
			System.out.printf("%d: %s\n", e.getKey(), e.getValue());
		Integer clinic = null;
		if (in.hasNextInt())
			clinic = in.nextInt();
		else
		{
			in.next();
			System.out.printf("No such clinic.\n");
			return;
		}

		System.out.printf("Enter Email\n");
		String email = in.next();
		Encryption crypto = Implementations.Encryption();
		String salt = crypto.getNewSalt();
		db.addUser(user, crypto.hashString(password, salt), salt, clinic, email);
	}
	
	/**
	 * Runs the manager main loop. The administrator is presented with
	 * available management operations and can choose which one to do.
	 */
	public void runManager()
	{
		final int EXIT = 0, ADD_CLINIC = 1, ADD_USER = 2;
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
			System.out.printf("Unknown option. Exting\n");
			System.exit(1);
		}
		switch (input)
		{
		case EXIT:
			break;
		case ADD_CLINIC:
			addClinic();
			break;
		case ADD_USER:
			addUser();
			break;
		default:
			break;
		}
	}
	
	public static void main(String[] args)
	{
		Manage m = new Manage();
		m.runManager();
		
	}
}
