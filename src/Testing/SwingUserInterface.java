package Testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import core.PROM_PREM_Collector;
import core.containers.Form;
import core.containers.form.FieldContainer;
import core.containers.form.SingleOptionContainer;
import core.interfaces.Messages;
import core.interfaces.UserInterface;

public class SwingUserInterface extends JApplet implements ActionListener, UserInterface
{
	public SwingUserInterface()
	{
		ppc = new PROM_PREM_Collector(this);
		initGUI();
	}
	
	private void initGUI()
	{
		frame = new JFrame("PROM/PREM Collector GUI");
		frame.setContentPane(this);
		setLayout(new BorderLayout());
		
		// panel for displaying and questions and answers
		add(console = new SwingConsole(), BorderLayout.CENTER);
		
		// panel with buttons
		add(makeMenuPanel(), BorderLayout.SOUTH);

		// set focus to answer field
		frame.addWindowListener( new WindowAdapter() {
			public void windowOpened( WindowEvent e ){
				console.requestFocus();
			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
	}
	
	private JPanel makeMenuPanel()
	{
		Dimension dim = new Dimension(150, 25);
		JPanel bPanel = new JPanel(new GridLayout(1, 4));
		restartButton = AddButton("(Re)start", "restart",
				"Click here to (re)start the round.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		resultsButton = AddButton("Show results", "results",
				String.format("%s %s", "Click here to show the results.",
						"Doing so will end the round."), true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		settingsButton = AddButton("Settings", "settings",
				"Click here to modify the settings.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		databaseButton = AddButton("Database", "database",
				"Click here to modify the database.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		bPanel.add(restartButton);
		bPanel.add(resultsButton);
		bPanel.add(settingsButton);
		bPanel.add(databaseButton);
		return bPanel;
	}
	
	private JButton AddButton(String buttonText, String nameSet, String tooltip,
			boolean actionListen, boolean opaque, Color background,
			Color border, Dimension d)
	{
		JButton button = new JButton(buttonText);
		button.setName(nameSet);
		button.setToolTipText(tooltip);
		if(actionListen)
			button.addActionListener(this);
		button.setOpaque(opaque);
		button.setBackground(background);
		button.setBorder(new LineBorder(border));
		if (d != null)
			button.setPreferredSize(d);
		button.setFont(FONT);
		return button;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton b = (JButton) e.getSource();
			if (b.getName() != null)
			{
				// unimplemented
			}
		}
		else if (e.getSource() instanceof JTextField)
		{
			JTextField t = (JTextField) e.getSource();
			if (t.getName() != null)
			{
				// unimplemented
			}
		}
	}

	@Override
	public void displayError(String s)
	{
		JOptionPane.showMessageDialog(
				null, s, "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void displayMessage(String message)
	{
		JOptionPane.showMessageDialog(
				null, message, "Message",
				JOptionPane.PLAIN_MESSAGE);
	}

	@Override
	public int selectOption(String message, SingleOptionContainer options)
	{
		separate();
		StringBuilder sb = new StringBuilder();
		if (message != null)
		{
			sb.append(String.format("%s\n", message));
		}
		sb.append(String.format("%s\n",
				Messages.getMessages().getInfo(
						Messages.INFO_UI_SELECT_SINGLE)));
		HashMap<Integer, String> opt = options.getSOptions();
		for (Entry<Integer, String> e : opt.entrySet())
			sb.append(String.format("%d: %s\n", e.getKey(), e.getValue()));
		
		int input = UserInterface.ERROR;
		try
		{
			input = Integer.parseInt(getUserInput(sb.toString()));
		} catch (NumberFormatException nfe) {}
		return input;
	}

	@Override
	public boolean presentForm(Form form)
	{
		HashMap<Integer, ExtraImplementation> components = fillContents(form);
		int nEntries = components.size();

		final int ERROR = 0, EXIT = 1, CONTINUE = 2,
				GOTO_PREV = 3, GOTO_NEXT = 4;
		Messages msg = Messages.getMessages();
		SingleOptionContainer options = new SingleOptionContainer();
		options.addSOption(CONTINUE, msg.getInfo(
				Messages.INFO_UI_FORM_CONTINUE));
		options.addSOption(GOTO_PREV, msg.getInfo(
				Messages.INFO_UI_FORM_PREVIOUS));
		options.addSOption(GOTO_NEXT, msg.getInfo(
				Messages.INFO_UI_FORM_NEXT));
		options.addSOption(EXIT, msg.getInfo(
				Messages.INFO_UI_FORM_EXIT));

		int cIdx = 0;
		boolean allFilled = false;
		while(!allFilled)
		{
			String entryState;
			if (components.get(cIdx).entryFilled())
				entryState = msg.getInfo(Messages.INFO_UI_FILLED);
			else
				entryState = msg.getInfo(Messages.INFO_UI_UNFILLED);
			Integer identifier = null;
			if (options.setSelected(selectOption(
					String.format("%s: %d/%d (%s)",
							msg.getInfo(Messages.INFO_UI_ENTRY),
							cIdx, nEntries-1, entryState),
					options)))
				identifier = options.getSelected();
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
			default:
				displayError(Messages.getMessages().getError(
						Messages.ERROR_UNKNOWN_RESPONSE));
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

	@Override
	public void run()
	{
		while (!ppc.start());
		close();
	}

	/**
	 * Opens the PROM/PREM Collector program.
	 */
	public void open()
	{
		ppc = new PROM_PREM_Collector(this);
		(new Thread(this)).start();
	}

	/**
	 * Closes the user interface (if it is open).
	 */
	public void close()
	{
		frame.dispose();
	}
	
	private String getUserInput(String message)
	{
		console.displayNewQuestion(message);
		return console.getUserInput();
	}
	
	/**
	 * Prints a line of characters to make it simple for the user
	 * to separate active interface stuff (forms, options, messages
	 * etc.) from inactive interface stuff. This part may only be
	 * relevant when using CLI.
	 */
	private void separate()
	{
		console.clearDisplayQuestion();
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
		public boolean fillEntry()
		{
			return soc.setSelected(responseID);
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
				separate();
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%s\n",
						Messages.getMessages().getInfo(
								Messages.INFO_UI_SELECT_SINGLE)));
				HashMap<Integer, String> opt = soc.getSOptions();
				Integer selected = soc.getSelected();
				for (Entry<Integer, String> e : opt.entrySet())
				{
					Integer id = e.getKey();
					if (selected != null && id == selected)
						sb.append(String.format("[%d]: %s\n", id, e.getValue()));
					else
						sb.append(String.format(" %d : %s\n", id, e.getValue()));
				}
				
				try
				{
					responseID = Integer.parseInt(getUserInput(sb.toString()));
				} catch (NumberFormatException nfe) {}
				if (opt.containsKey(responseID))
					done = true;
				else
					ui.displayError(Messages.getMessages().getError(
							Messages.ERROR_UNKNOWN_RESPONSE));
				
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
		public boolean fillEntry()
		{
			fc.setEntry(entry);
			return true;
		}

		@Override
		public boolean entryFilled()
		{
			return fc.hasEntry();
		}

		@Override
		public void present()
		{
			separate();
			StringBuilder sb = new StringBuilder();
			String cEntry = fc.getEntry();
			sb.append(String.format("%s: %s\n", fc.getStatement(),
					(cEntry == null ? "" : cEntry)));
			entry = getUserInput(sb.toString());
		}
	}
	
	public static final Font FONT = new Font("Courier", Font.PLAIN, 18);
	private JFrame frame;
	private PROM_PREM_Collector ppc;
	private JButton restartButton, settingsButton,
	resultsButton, databaseButton;
	private SwingConsole console;
	private static final long serialVersionUID = -3896988492887782839L;
}
