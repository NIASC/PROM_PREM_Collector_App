package implement;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import core.containers.OptionContainer;
import core.containers.Option;

public class UserInterface implements UserInterface_Interface
{
	private Scanner in;
	public UserInterface()
	{
		in = new Scanner(System.in);
	}
	
	public void close()
	{
		if (in != null)
			in.close();
	}
	
	private void separate()
	{
		System.out.printf("%s%s\n", "----------------------------------------",
				"----------------------------------------");
	}
	
	@Override
	public void displayError(String s)
	{
		System.err.printf("%s\n", s);
	}

	@Override
	public int displayLoginScreen()
	{
		separate();
		System.out.printf(
				"What would you like to do?\n\t%s\n\t%s\n\t%s\n",
				"1: Login", "2: Register", "0: Exit");
		int input = in.nextInt();
		in.reset();
		int out = UserInterface_Interface.ERROR;
		switch (input)
		{
		case UserInterface_Interface.EXIT: //exit
			out = UserInterface_Interface.EXIT;
			break;
		case UserInterface_Interface.LOGIN:
			out = UserInterface_Interface.LOGIN;
			break;
		case UserInterface_Interface.REGISTER:
			out = UserInterface_Interface.REGISTER;
			break;
		default:
			break;
		}
		return out;
	}

	@Override
	public int selectOption(OptionContainer options)
	{
		separate();
		System.out.printf("Select option\n");
		HashMap<Integer, Option> opt = options.get();
		for (Entry<Integer, Option> e : opt.entrySet())
		{
			System.out.printf("%d: %s\n", e.getKey(), e.getValue().getText());
		}
		int input = in.nextInt();
		in.reset();
		return input;
	}

}
