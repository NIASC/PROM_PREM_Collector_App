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

import implement.Database;
import implement.Encryption;

import java.util.HashMap;
import java.util.Map.Entry;
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
		db = new Database();
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
		in.reset();
		
		db.addClinic(clinic);
	}
	
	/**
	 * Adds a new user to the database. The administrator will be
	 * presented with a form to fill in and the data is formatted
	 * such that the database can add the user.
	 */
	private void addUser()
	{
		System.out.printf("Username?\n");
		String user = in.next();
		in.reset();

		System.out.printf("Password?\n");
		String password = in.next();
		in.reset();

		System.out.printf("Clinic?\n");
		HashMap<Integer, String> clinics = db.getClinics();
		for (Entry<Integer, String> e : clinics.entrySet())
			System.out.printf("%d: %s\n", e.getKey(), e.getValue());
		Integer clinic = null;
		if (in.hasNextInt())
			clinic = in.nextInt();
		else
		{
			in.next();
			System.out.printf("No such clinic. Exting\n");
			System.exit(1);
		}

		System.out.printf("Email?\n");
		String email = in.next();
		Encryption crypto = new Encryption();
		String salt = crypto.getNewSalt();
		db.addUser(user, crypto.hashString(password, salt), salt, clinic, email);
	}

	/** Open a connection */
	public boolean dbConnect()
	{
		System.out.printf("%s\n", "Connecting to database.");
		return db.connect() != Database.ERROR;
	}

	/** Close connection */
	public boolean dbDisconnect()
	{
		System.out.println("Disconnecting from database.");
		return db.disconnect() != Database.ERROR;
	}
	
	/**
	 * Runs the manager main loop. The administrator is presented with
	 * available management operations and can choose which one to do.
	 */
	public void runManager()
	{
		if (!dbConnect())
			System.exit(1);
		
		// Execute a query
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
		if (!dbDisconnect())
			System.exit(1);
	}
	
	public static void main(String[] args)
	{
		Manage m = new Manage();
		m.runManager();
		
	}
}
