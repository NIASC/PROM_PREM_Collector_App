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

import core.containers.Form;
import core.containers.form.FieldContainer;
import core.containers.form.SingleOptionContainer;

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
	/**
	 * Initializes variables and opens the scanner stream.
	 */
	public UserInterface()
	{
		in = new Scanner(System.in);
	}
	
	/* 
	 * Public methods required by the interface.
	 */
	
	@Override
	public void close()
	{
		if (in != null)
			in.close();
	}
	
	@Override
	public void displayError(String message)
	{
		PrintStream ps = System.out;
		ps.printf(ANSI_RED);
		separate(ps);
		print(message, ps);
		separate(ps);
		ps.printf(ANSI_RESET);
	}

	@Override
	public void displayMessage(String message)
	{
		separate(System.out);
		print(message, System.out);
	}

	@Override
	public int selectOption(String message, SingleOptionContainer options)
	{
		separate(System.out);
		if (message != null)
		{
			print(message, System.out);
			System.out.printf("\n");
		}
		System.out.printf("Select option\n");
		HashMap<Integer, String> opt = options.getSOptions();
		for (Entry<Integer, String> e : opt.entrySet())
			System.out.printf("%d: %s\n", e.getKey(), e.getValue());
		int input = ERROR;
		if (in.hasNextInt())
			input = in.nextInt();
		else
			in.next();
		return input;
	}

	@Override
	public boolean presentForm(Form form)
	{
		HashMap<Integer, ExtraImplementation> components = fillContents(form);
		int nEntries = components.size();

		final int ERROR = 0, EXIT = 1, CONTINUE = 2,
				GOTO_PREV = 3, GOTO_NEXT = 4;
		SingleOptionContainer options = new SingleOptionContainer();
		options.addSOption(CONTINUE, "Continue to next unfilled entry");
		options.addSOption(GOTO_PREV, "Go to previous question");
		options.addSOption(GOTO_NEXT, "Go to next question");
		options.addSOption(EXIT, "Exit (answers will be discarded)");

		int cIdx = 0;
		boolean allFilled = false;
		while(!allFilled)
		{
			options.setSelected(selectOption(
					String.format("Entry: %d/%d (%s)", cIdx, nEntries-1,
							components.get(cIdx).entryFilled()
							? "filled" : "unfilled"),
					options));
			Integer identifier = options.getSelected();
			int response = identifier != null ? identifier : ERROR;
			switch(response)
			{
			case CONTINUE:
				components.get(cIdx).present();
				components.get(cIdx).fillEntry();
				int nextComponent = getNextUnfilledEntry(cIdx,
						components);
				if (nextComponent == cIdx)
					allFilled = true;
				else
					cIdx = nextComponent;
				break;
			case GOTO_PREV:
				cIdx = getNextEntry(cIdx, nEntries, -1, false);
				break;
			case GOTO_NEXT:
				cIdx = getNextEntry(cIdx, nEntries, 1, false);
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

	@SuppressWarnings("unchecked")
	@Override
	public <T extends FormComponentDisplay> T createSingleOption(SingleOptionContainer soc)
	{
		return (T) new SingleOptionDisplay(this, soc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends FormComponentDisplay> T createField(FieldContainer fc)
	{
		return (T) new FieldDisplay(this, fc);
	}
	
	/* 
	 * Private methods not required by the interface.
	 */
	
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
	
	/**
	 * Prints a supplied message using the supplied PrintStream. The
	 * string will be formatted so that it does not take up more space
	 * than LINE_LENGTH characters wide.
	 * 
	 * @param message The message to print.
	 * @param ps The PrintStream to use to print the message.
	 */
	private void print(String message, PrintStream ps)
	{
		/* If the message is too wide it must be split up */
		StringBuilder msgFormatted = new StringBuilder();
		final int msgLength = message.length();
		int beginIndex = 0, step;
		do
		{
			if (msgLength - beginIndex > LINE_LENGTH)
				step = LINE_LENGTH;
			else
				step = msgLength - beginIndex;
			
			msgFormatted.append(String.format("%s\n",
					message.substring(beginIndex, beginIndex + step)));
			beginIndex += step;
		} while (step == LINE_LENGTH); // while >LINE_LENGTH remains
		ps.printf("%s", msgFormatted.toString());
	}
	
	/**
	 * Moves cIndex steps positions. If warp is true the function will
	 * wrap around at the beginning and at the end. If wrap is false
	 * the method will at most change to 0 <= index < nEntries.
	 * 
	 * @param cIndex The (current) to start from.
	 * @param nEntries The number of entries / length of list etc.
	 * @param steps The number of steps to move from cIndex. Negative
	 * 		indices will move backwards.
	 * @param wrap True if the increment/decrement should wrap (i.e. if
	 * 		cIndex+steps >= nEntries then the next index will continue
	 * 		from zero).
	 * 
	 * @return The new index after incrementing.
	 */
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
		while (form.get(i).entryFilled()
				&& i != currentIdx)
			i = getNextEntry(i, entries, 1, true);
		return i;
	}
	
	/**
	 * Creates a map with ID for keys and objects that are able to
	 * display the form entries from the supplied form.
	 * 
	 * @param form The form that should be 'converted' to a map of
	 * 		displayable objects.
	 * 
	 * @return A map of displayable objects constructed from the
	 * 		supplied form. The keys are the ID (i.e. order of appearance)
	 * 		and the values are the displayable objects.
	 */
	private HashMap<Integer, ExtraImplementation> fillContents(Form form)
	{
		HashMap<Integer, ExtraImplementation> contents =
				new HashMap<Integer, ExtraImplementation>();
		int id = 0;
		form.jumpTo(Form.AT_BEGIN);
		do
		{
			contents.put(id++, form.currentEntry().draw(this));
		} while(!form.endOfForm() && form.nextEntry() != null);
		return contents;
	}
	
	/**
	 * This interface Is a superset of the FormComponentDisplay and
	 * contains a set of methods that are require for this particular
	 * implementation of the user interface to work.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private interface ExtraImplementation extends FormComponentDisplay
	{
		/**
		 * This method presents the container and stores the response
		 * from the user. It is up to the implementation of this method
		 * to prevent null/empty entries.
		 */
		public void present();
	}
	
	/**
	 * This class is a displayable wrapper the for SingleOption
	 * container. In this implementation this class displays the
	 * SingleOption container and stores the response.
	 * In a GUI implementaion a corresponding class could just extend
	 * a JComponent that specializes in displaying select-single-option
	 * content and not necessarily displaying the content itself.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private class SingleOptionDisplay implements ExtraImplementation
	{
		private SingleOptionContainer soc;
		private UserInterface ui;
		private int responseID;
		
		/**
		 * Initializes login variables.
		 * 
		 * @param ui The instance of the active UserInterface object.
		 * @param soc The instance of the SingleOptionContainer that
		 * 		the instance of this SingleOptionDisplay should act as
		 * 		a wrapper for.
		 */
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
				if (in.hasNextInt())
					responseID = in.nextInt();
				else
					in.next();
				if (opt.containsKey(responseID))
					done = true;
				else
					ui.displayError(Messages.error.getMessage(
							"UNKNOWN_RESPONSE", "en"));
			}
		}
	}
	
	/**
	 * This class is a displayable wrapper the for the Field container. 
	 * In this implementation this class displays the Field container
	 * and stores the response.
	 * In a GUI implementaion a corresponding class could just extend
	 * a JComponent that specializes in displaying text field content
	 * and not necessarily displaying the content itself.
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private class FieldDisplay implements ExtraImplementation
	{
		private UserInterface ui;
		private FieldContainer fc;
		private String entry;
		
		/**
		 * Initializes login variables.
		 * 
		 * @param ui The instance of the active UserInterface object.
		 * @param fc The instance of the FieldContainer that
		 * 		the instance of this FieldDisplay should act as a
		 * 		wrapper for.
		 */
		public FieldDisplay(UserInterface ui, FieldContainer fc)
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
	
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_BLACK = "\u001B[30m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";
	
	private Scanner in;
	private static final int LINE_LENGTH = 80;
	private static final String SEPARATION_CHARACTER = "-";
	private static String separation;
	
	/* create the ------ line that separates output form the UI. */
	static
	{
		StringBuilder sb = new StringBuilder(LINE_LENGTH);
		for (int i = 0 ; i < LINE_LENGTH; ++i)
			sb.append(SEPARATION_CHARACTER);
		separation = sb.toString();
	}
}
