package manage;

import implement.Database;
import implement.UserInterface_Interface;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Manage
{
	private Scanner in;
	private Database db;
	
	public Manage()
	{
		db = new Database();
		in = new Scanner(System.in);
	}
	
	private void addClinic()
	{
		System.out.printf("Clinic?\n");
		String clinic = in.next();
		in.reset();
		
		db.addClinic(clinic);
	}
	
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
			System.out.printf("%d: %s", e.getKey(), e.getValue());
		int clinic = in.nextInt();
		in.reset();

		System.out.printf("Email?\n");
		String email = in.next();
		in.reset();
		
		db.addUser(user, password, clinic, email);
	}
	
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
		
	}//end main
}
