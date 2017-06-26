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
package implement;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map.Entry;

import java.util.Scanner;

import javax.swing.JComponent;

import core.containers.Form;
import core.containers.form.Fcontainer;
import core.containers.form.Field;
import core.containers.form.FieldContainer;
import core.containers.form.FormContainer;
import core.containers.form.SingleOptionContainer;
import implement.UserInterface_Interface.FormComponentDisplay;

/**
 * This class is an example of an implementation of
 * UserInterface_Interface. This implementation is done using
 * command-line interface (CLI) for simplicity of development.
 * 
 * @author Marcus Malmquist
 *
 */
public class UserInterface implements UserInterface_Interface
{
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	private Scanner in;
	private static final int LINE_LENGTH = 80;
	private static final String SEPARATION_CHARACTER = "-";
	private static String separation;
	
	static
	{
		StringBuilder sb = new StringBuilder(LINE_LENGTH);
		for (int i = 0 ; i < LINE_LENGTH; ++i)
			sb.append(SEPARATION_CHARACTER);
		separation = sb.toString();
	}
	
	public UserInterface()
	{
		in = new Scanner(System.in);
	}
	
	@Override
	public void close()
	{
		if (in != null)
			in.close();
	}
	
	/**
	 * Prints a line of characters to make it simple for the user
	 * to separate active interface stuff (forms, options, messages
	 * etc.) from inactive interface stuff. This part may only be
	 * relevant when using CLI.
	 * @param ps TODO
	 */
	private void separate(PrintStream ps)
	{
		ps.printf("%s\n", separation);
	}
	
	@Override
	public void displayError(String message)
	{
		separate(System.out);
		System.out.println(ANSI_RED);
		print(message, System.out);
		System.out.println(ANSI_RESET);
	}

	@Override
	public void displayMessage(String message)
	{
		print(message, System.out);
	}

	@Override
	public int displayLoginScreen()
	{
		separate(System.out);
		System.out.printf(
				"What would you like to do?\n%s\n%s\n%s\n",
				"1: Login", "2: Register", "0: Exit");
		int input = in.nextInt();
		in.reset();
		int out = UserInterface_Interface.ERROR;
		switch (input)
		{
		case 0: //exit
			out = UserInterface_Interface.EXIT;
			break;
		case 1:
			out = UserInterface_Interface.LOGIN;
			break;
		case 2:
			out = UserInterface_Interface.REGISTER;
			break;
		default:
			break;
		}
		return out;
	}
	
	@Override
	public HashMap<String, String> requestLoginDetails(String usernameKey, String passwordKey)
	{
		separate(System.out);
		HashMap<String, String> details = new HashMap<String, String>(2);
		System.out.printf("%s\n", "Enter username");
		details.put(usernameKey, in.next());
		in.reset();
		System.out.printf("%s\n", "Enter password");
		details.put(passwordKey, in.next());
		in.reset();
		return details;
	}

	@Override
	public int selectOption(SingleOptionContainer options)
	{
		separate(System.out);
		System.out.printf("Select option\n");
		HashMap<Integer, String> opt = options.getSOptions();
		for (Entry<Integer, String> e : opt.entrySet())
			System.out.printf("%d: %s\n", e.getKey(), e.getValue());
		int input = in.nextInt();
		in.reset();
		return input;
	}

	@Override
	public void displayForm(FieldContainer form)
	{
		separate(System.out);
		for (Entry<Integer, Field> e : form.get().entrySet())
		{
			Field f = e.getValue();
			System.out.printf("%d) %s: %s\n", e.getKey(), f.getKey(),
					(f.getValue() == null ? "" : f.getValue()));
			/* Sometimes the input is empty. not allowed. */
			String entry;
			while ((entry = in.nextLine()).equals(""));
			f.setValue(entry);
			in.reset();
		}
	}
	
	private void print(String message, PrintStream ps)
	{
		/* If the message is too wide it must be split up */
		StringBuilder sb = new StringBuilder();
		final int msgLength = message.length();
		int beginIndex = 0, step;
		do
		{
			step = (msgLength - beginIndex > LINE_LENGTH)
					? LINE_LENGTH : msgLength - beginIndex;
			sb.append(String.format("%s\n", message.substring(
					beginIndex, beginIndex + step)));
			beginIndex += step;
		} while (step == LINE_LENGTH); // while >LINE_LENGTH remains
		ps.printf("%s\n", sb.toString());
	}

	@Override
	public boolean presentForm(Form form)
	{
		HashMap<Integer, ExtraImplementation> components = fillContents(form);
		int nComponents = components.size();

		final int ERROR = 0, EXIT = 1, CONTINUE = 2,
				GOTO_PREV = 3, GOTO_NEXT = 4;
		SingleOptionContainer options = new SingleOptionContainer();
		options.addSOption(CONTINUE, "Continue to next unfilled entry");
		options.addSOption(GOTO_PREV, "Go to previous question");
		options.addSOption(GOTO_NEXT, "Go to next question");
		options.addSOption(EXIT, "Exit (answers will be discarded)");

		int component = 0;
		boolean allFilled = false;
		while(!allFilled)
		{
			displayMessage(String.format(
					"Entry: %d/%d (%s)", component, nComponents-1,
					components.get(component).entryFilled()
					? "filled" : "unfilled"));
			options.setSelected(selectOption(options));
			Integer identifier = options.getSelected();
			int response = identifier != null ? identifier : ERROR;
			switch(response)
			{
			case CONTINUE:
				components.get(component).present();
				components.get(component).fillEntry();
				int nextComponent = getNextUnfilledEntry(component,
						components);
				if (nextComponent == component)
					allFilled = true;
				else
					component = nextComponent;
				break;
			case GOTO_PREV:
				component = getNextEntry(component, nComponents, -1, false);
				break;
			case GOTO_NEXT:
				component = getNextEntry(component, nComponents, 1, false);
				break;
			case EXIT:
				return false;
			case ERROR:
				displayError(Messages.error.getMessage(
						"NULL_SELECTED", "en"));
				break;
			default:
				displayError(Messages.error.getMessage(
						"UNKNOWN_RESPONSE", "en"));
				break;
			}
		}
		return true;
	}
	
	private int getNextEntry(int cIndex, int nEntries, int steps, boolean wrap)
	{
		if (wrap)
			return (cIndex + steps + nEntries) % nEntries;

		if (cIndex + steps >= nEntries)
			return cIndex;
		else if (cIndex + steps < 0)
			return 0;
		else
			return cIndex + steps;
	}
	
	/**
	 * Searches for unfilled entries in the supplied form. The search
	 * starts at the entry after currentIdx and iterates until an
	 * unfilled entry is found or the search index becomes the same as
	 * the current index (i.e. all entries are filled). The search
	 * wraps around at the end so the next empty index can be smaller
	 * than the current.
	 * NOTE: This method will not detect if the entry at the current
	 * index is filled so if the returned index is the same as the
	 * current it is up to the caller to decide what to do.
	 * 
	 * @param currentIdx The index (question id) of the current entry.
	 * @param form The map of entries, where the keys range from
	 * 		0<idx<form.size().
	 * 
	 * @return The index/id of the next unfilled entry in form. If
	 * 		the returned id is the same as the (supplied) current id
	 * 		it means that the search has wrapped around at the end
	 * 		and not found an unfilled entry.
	 */
	private int getNextUnfilledEntry(int currentIdx,
			HashMap<Integer, ExtraImplementation> form)
	{
		int entries = form.size();
		int i = getNextEntry(currentIdx, entries, 1, true);
		//int i = (currentIdx + 1) % entries;
		try {
		while (form.get(i).entryFilled()
				&& i != currentIdx)
			i = getNextEntry(i, entries, 1, true); //(i + 1) % entries;
		} catch (NullPointerException npe)
		{
			this.displayError(String.format("%d, %d", entries, i));
		}
		return i;
	}
	
	private HashMap<Integer, ExtraImplementation> fillContents(Form form)
	{
		HashMap<Integer, ExtraImplementation> contents =
				new HashMap<Integer, ExtraImplementation>();
		int id = 0;
		form.jumpTo(Form.AT_FIRST);
		do
		{
			contents.put(id++, form.currentEntry().draw(this));
		} while(!form.endOfForm() && form.nextEntry() != null);
		return contents;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends FormComponentDisplay> T createSingleOption(SingleOptionContainer soc)
	{
		return (T) new SingleOptionDisplay(this, soc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends FormComponentDisplay> T createField(Fcontainer fc)
	{
		return (T) new FieldDisplay(this, fc);
	}
	
	private interface ExtraImplementation extends FormComponentDisplay
	{
		/**
		 * This method presents the container and stores the response
		 * from the user. It is up to the implementation of this method
		 * to prevent null/empty entries.
		 */
		public void present();
	}
	
	private class SingleOptionDisplay implements ExtraImplementation
	{
		private SingleOptionContainer soc;
		private UserInterface ui;
		private int responseID;
		
		public SingleOptionDisplay(UserInterface ui,
				SingleOptionContainer soc)
		{
			this.ui = ui;
			this.soc = soc;
		}

		@Override
		public void fillEntry()
		{
			soc.setSelected(responseID);
		}

		@Override
		public boolean entryFilled() 
		{
			return soc.hasEntry();
		}

		@Override
		public void present()
		{
			boolean done = false;
			while (!done)
			{
				separate(System.out);
				System.out.printf("Select option\n");
				HashMap<Integer, String> opt = soc.getSOptions();
				Integer selected = soc.getSelected();
				for (Entry<Integer, String> e : opt.entrySet())
				{
					Integer id = e.getKey();
					if (selected != null && id == selected)
						System.out.printf("[%d]: %s\n", id, e.getValue());
					else
						System.out.printf(" %d : %s\n", id, e.getValue());
				}
				responseID = in.nextInt();
				in.reset();
				if (opt.containsKey(responseID))
					done = true;
				else
					ui.displayError(Messages.error.getMessage(
							"UNKNOWN_RESPONSE", "en"));
			}
		}
	}
	
	private class FieldDisplay implements ExtraImplementation
	{
		private UserInterface ui;
		private Fcontainer fc;
		private String entry;
		
		public FieldDisplay(UserInterface ui, Fcontainer fc)
		{
			this.ui = ui;
			this.fc = fc;
			entry = null;
		}

		@Override
		public void fillEntry()
		{
			fc.setEntry(entry);
		}

		@Override
		public boolean entryFilled()
		{
			return fc.hasEntry();
		}

		@Override
		public void present()
		{
			separate(System.out);
			String cEntry = fc.getEntry();
			System.out.printf("%s: %s\n", fc.getStatement(),
					(cEntry == null ? "" : cEntry));
			/* Sometimes the input is empty. not allowed. */
			String entry;
			while ((entry = in.nextLine()).equals(""));
			this.entry = entry;
			in.reset();
		}
	}
}
