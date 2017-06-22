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
		int clinic = in.nextInt();
		in.reset();

		System.out.printf("Email?\n");
		String email = in.next();
		in.reset();
		Encryption crypto = new Encryption();
		String salt = crypto.getNewSalt();
		db.addUser(user, crypto.hashString(password, salt), salt, clinic, email);
	}
	
	/**
	 * Runs the manager main loop. The administrator is presented with
	 * available management operations and can choose which one to do.
	 */
	public void runManager()
	{
		// Open a connection
		System.out.printf("%s", "Connecting to database... ");
		if (db.connect() == Database.ERROR)
		{
			System.out.printf("%s\n", "Failed!");
			System.exit(1);
		}
		System.out.printf("%s\n", "Success!");
		
		// Execute a query
		System.out.printf(
				"What would you like to do?\n%s\n%s\n%s\n",
				"1: Add Clinic", "2: Add user", "0: Exit");
		int input = in.nextInt();
		in.reset();
		switch (input)
		{
		case 0: //exit
			break;
		case 1:
			addClinic();
			break;
		case 2:
			addUser();
			break;
		default:
			break;
		}
		
		// Close connection
		System.out.println("Disconnecting from database... ");
		if (db.disconnect() == Database.ERROR)
		{
			System.out.printf("%s\n", "Failed!");
			System.exit(1);
		}
		System.out.printf("%s\n", "Done!");
	}
	
	public static void main(String[] args)
	{
		Manage m = new Manage();
		m.runManager();
		
	}
}
