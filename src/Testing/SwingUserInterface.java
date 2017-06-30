package Testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import core.UserHandle;
import core.containers.Form;
import core.containers.form.FieldContainer;
import core.containers.form.SingleOptionContainer;
import core.interfaces.Messages;
import core.interfaces.UserInterface;

public class SwingUserInterface extends JApplet implements ActionListener, UserInterface
{
	public SwingUserInterface()
	{
		uh = new UserHandle(this);
		initGUI();
	}
	
	private void initGUI()
	{
		frame = new JFrame("PROM/PREM Collector GUI");
		frame.setContentPane(this);
		setLayout(new BorderLayout());
		
		// panel for displaying and questions and answers
		pageContent = new JPanel(new BorderLayout());
		pageContent.setPreferredSize(new Dimension(400,300));
		pageContent.setMinimumSize(new Dimension(200,100));
		add(pageContent, BorderLayout.CENTER);
		
		// panel with buttons
		add(makeMenuPanel(), BorderLayout.NORTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		setContent(new LoginScreen());
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
		databaseButton = AddButton("Exit", "exit",
				"Click here to exit the program.", true, true,
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
				if (b.getName().equals(databaseButton.getName()))
				{
					stop();
				}
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
	public boolean presentForm(Form form, ReturnFunction function, Object retpan)
	{
		Component panel = null;
		synchronized(pageContent.getTreeLock())
		{
			panel = pageContent.getComponents()[0];
		}
		setContent(new GUIForm(form, function, panel));
		/*
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
		*/
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends FormComponentDisplay> T createSingleOption(SingleOptionContainer soc)
	{
		return (T) new SingleOptionDisplay(soc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends FormComponentDisplay> T createField(FieldContainer fc)
	{
		return (T) new FieldDisplay(fc);
	}

	@Override
	public void init()
	{
		/* when the webpage is initialized */
		System.out.println("Applet initialized");
	}
	
	@Override
	public void destroy()
	{
		/* when the webpage is destroyed */

		System.out.println("Applet destroyed");

		if (frame != null)
			frame.dispose();
	}
	
	@Override
	public void start()
	{
		/* when the user returns to the webpage */
		System.out.println("Applet started");
		/* Maybe reload cached session?         */
	}
	
	public void stop()
	{
		/* when the user leaves the web page. */
		System.out.println("Applet stopped");
		/* Maybe save session to cache?       */
		if (frame != null)
			frame.dispose();
	}
	
	/**
	 * Displays the message to the user and waits for an answer.
	 * 
	 * @param message The message to display.
	 * 
	 * @return The use response.
	 */
	private String getUserInput(String message)
	{
		/*
		pageContent.displayNewQuestion(message);
		return pageContent.getUserInput();
		*/
		return null;
	}
	
	/**
	 * Prints a line of characters to make it simple for the user
	 * to separate active interface stuff (forms, options, messages
	 * etc.) from inactive interface stuff. This part may only be
	 * relevant when using CLI.
	 */
	private void separate()
	{
		/*
		pageContent.clearDisplayQuestion();
		*/
		return;
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
			HashMap<Integer, FormComponentDisplay> form)
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
	private HashMap<Integer, FormComponentDisplay> fillContents(Form form)
	{
		HashMap<Integer, FormComponentDisplay> contents =
				new HashMap<Integer, FormComponentDisplay>();
		int id = 0;
		form.jumpTo(Form.AT_BEGIN);
		do
		{
			contents.put(id++, form.currentEntry().getDisplayable(this));
		} while(!form.endOfForm() && form.nextEntry() != null);
		return contents;
	}
	
	/**
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private class LoginScreen extends JPanel implements ActionListener
	{
		private static final long serialVersionUID = 2352904758935918090L;
		private JPanel buttons, entryfields;
		private JButton login, register, exit;
		private JLabel usernameL, passwordL;
		private JTextField usernameTF;
		private JPasswordField passwordTF;

		public LoginScreen()
		{
			/* button panel */
			buttons = new JPanel(new GridLayout(1, 3));
			login = new JButton("Login");
			login.setName("login");
			login.addActionListener(this);
			register = new JButton("Register");
			register.setName("register");
			register.addActionListener(this);
			exit = new JButton("Exit");
			exit.setName("exit");
			exit.addActionListener(this);
			buttons.add(login);
			buttons.add(register);
			buttons.add(exit);
			
			/* entry fields */
			entryfields = new JPanel(new BorderLayout());
			
			JPanel labels = new JPanel(new GridLayout(2, 1));
			usernameL = new JLabel("Username");
			labels.add(usernameL);
			passwordL = new JLabel("Password");
			labels.add(passwordL);
			
			JPanel tfields = new JPanel(new GridLayout(2, 1));
			Dimension d = new Dimension(80, 25);
			usernameTF = new JTextField(20);
			usernameTF.setName("usernameTF");
			usernameTF.addActionListener(this);
			usernameTF.setPreferredSize(d);
			tfields.add(usernameTF);
			passwordTF = new JPasswordField(32);
			passwordTF.setName("passwordTF");
			passwordTF.addActionListener(this);
			passwordTF.setPreferredSize(d);
			tfields.add(passwordTF);
			entryfields.add(labels, BorderLayout.WEST);
			entryfields.add(tfields, BorderLayout.CENTER);
			
			add(entryfields, BorderLayout.CENTER);
			add(buttons, BorderLayout.SOUTH);
		}
		
		@Override
		public void requestFocus()
		{
			usernameTF.requestFocus();
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof JButton)
			{
				JButton b = (JButton)e.getSource();
				if (b.getName().equals(login.getName()))
				{
					uh.login(usernameTF.getText(), new String(passwordTF.getPassword()));
					if (uh.isLoggedIn())
					{
						usernameTF.setText(null);
						passwordTF.setText(null);
						setContent(new WelcomeScreen(this));
						uh.updatePassword();
					}
				}
				else if (b.getName().equals(register.getName()))
				{
					uh.register();
				}
				else if (b.getName().equals(exit.getName()))
				{
					stop();
				}
			}
			else if (e.getSource() instanceof JTextField)
			{
				JTextField tf = (JTextField)e.getSource();
				if (tf.getName().equals(usernameTF.getName()))
				{
					if (new String(passwordTF.getPassword()).isEmpty())
						passwordTF.requestFocus();
					else
						login.requestFocus();
				}
				else if (tf.getName().equals(passwordTF.getName()))
				{
					if (usernameTF.getText().isEmpty())
						usernameTF.requestFocus();
					else
						login.requestFocus();
				}
			}
		}
	}
	
	private class WelcomeScreen extends JPanel implements ActionListener
	{
		private static final long serialVersionUID = 2805273101165405918L;
		private JPanel buttons;
		private JButton questionnaire, viewData, logout;
		
		private Component retpan;
		
		public WelcomeScreen(Component retpan)
		{
			this.retpan = retpan;
			setLayout(new BorderLayout());
			buttons = new JPanel(new GridLayout(1, 3));
			questionnaire = new JButton("Start questionnaire");
			questionnaire.setName("questionnaire");
			questionnaire.addActionListener(this);
			viewData = new JButton("View Statistics");
			viewData.setName("viewData");
			viewData.addActionListener(this);
			logout = new JButton("Log out");
			logout.setName("logout");
			logout.addActionListener(this);
			buttons.add(questionnaire);
			buttons.add(viewData);
			buttons.add(logout);
			
			add(buttons, BorderLayout.NORTH);
		}
		
		@Override
		public void requestFocus()
		{
			
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof JButton)
			{
				JButton b = (JButton)e.getSource();
				if (b.getName().equals(questionnaire.getName()))
				{
					// start questionnaire
				}
				else if (b.getName().equals(viewData.getName()))
				{
					// view data
				}
				else if (b.getName().equals(logout.getName()))
				{
					uh.logout();
					setContent(retpan);
				}
			}
		}
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
	private class SingleOptionDisplay extends JPanel implements FormComponentDisplay, ItemListener
	{
		private static final long serialVersionUID = 7314170750059865699L;
		private SingleOptionContainer soc;
		private int responseID;
		
		private HashMap<String, JRadioButton> options;
		private ButtonGroup group;
		
		/**
		 * Initializes login variables.
		 * 
		 * @param soc The instance of the SingleOptionContainer that
		 * 		the instance of this SingleOptionDisplay should act as
		 * 		a wrapper for.
		 */
		public SingleOptionDisplay(SingleOptionContainer soc)
		{
			setLayout(new GridLayout(0, 1));
			this.soc = soc;

			group = new ButtonGroup();
			
			HashMap<Integer, String> opt = soc.getSOptions();
			options = new HashMap<String, JRadioButton>();
			for (Entry<Integer, String> e : opt.entrySet())
			{
				JRadioButton btn = new JRadioButton(e.getValue());
				btn.setName(e.getValue());
				group.add(btn);
				btn.addItemListener(this);
				add(btn);
				options.put(Integer.toString(e.getKey()), btn);
			}
		}
		
		@Override
		public void requestFocus()
		{
			
		}

		@Override
		public void itemStateChanged(ItemEvent ev)
		{
			boolean selected = (ev.getStateChange() == ItemEvent.SELECTED);
			AbstractButton button = (AbstractButton) ev.getItemSelectable();
			JRadioButton sel = options.get(button.getName());
			if (sel == null)
				return;
			if (selected) {
				responseID = Integer.parseInt(button.getName());
			}
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
	private class FieldDisplay extends JPanel implements FormComponentDisplay
	{
		private static final long serialVersionUID = 2210804480530383502L;

		private FieldContainer fc;
		
		private JLabel fieldLabel;
		private JTextField field;
		
		/**
		 * Initializes login variables.
		 * @param fc The instance of the FieldContainer that
		 * 		the instance of this FieldDisplay should act as a
		 * 		wrapper for.
		 */
		public FieldDisplay(FieldContainer fc)
		{
			setLayout(new BorderLayout());
			this.fc = fc;
			fieldLabel = new JLabel(fc.getStatement());
			add(fieldLabel, BorderLayout.WEST);
			field = fc.isSecret() ? new JPasswordField(32) : new JTextField(32);
			field.setPreferredSize(new Dimension(80, 25));
			add(field, BorderLayout.CENTER);
		}
		
		@Override
		public void requestFocus()
		{
			field.requestFocus();
		}

		@Override
		public boolean fillEntry()
		{
			fc.setEntry(field.getText());
			return true;
		}

		@Override
		public boolean entryFilled()
		{
			return fc.hasEntry();
		}
	}
	
	/**
-----------------------------------------------------------------------_--------
	 * This class should create a form. a form should have two types of
	 * layout:
	 * 		* One question at a time
	 * 		* All (or as many as the page can fit) questions at a
	 * 		  time.
	 * 
	 * Ths class should have methods to display the questions. should
	 * pass the caller to this object, or like a template class that
	 * contains the instance of the class, so we know where to return
	 * when the user has filled in the form.
	 * 
	 * @author marcus
	 *
	 */
	private class GUIForm extends JPanel implements ActionListener
	{
		private static final long serialVersionUID = -7513730435118997364L;
		private final int nEntries;
		private int cIdx;
		private Messages msg = Messages.getMessages();
		
		private JPanel formControl, formContent;
		private JButton fc_continue, fc_previous, fc_next, fc_exit;
		
		private final HashMap<Integer, FormComponentDisplay> components;
		private final Form form;
		private final ReturnFunction function;
		private final Component retpan;
		
		/**
		 * 
		 * @param form The form to display.
		 * @param function The function to call when this form has been
		 * 		filled.
		 * @param retpan The displayable object (panel) to return to
		 * 		when this function has returned.
		 */
		public GUIForm(final Form form, final ReturnFunction function,
				final Component retpan)
		{
			this.form = form;
			this.function = function;
			this.retpan = retpan;
			
			setLayout(new BorderLayout());
			components = fillContents(form); // TODO: move this function here
			nEntries = components.size();
			formControl = initControlPanel();
			formContent = new JPanel(new FlowLayout());
			formContent.setPreferredSize(new Dimension(150, 30));
			add(formContent, BorderLayout.CENTER);
			add(formControl, BorderLayout.SOUTH);
			setFormContent(cIdx);
		}
		
		@Override
		public void requestFocus()
		{
			synchronized(formContent.getTreeLock())
			{
				Component c = formContent.getComponents()[0];
				if (c != null)
					c.requestFocus();
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof JButton)
			{
				JButton b = (JButton)e.getSource();
				synchronized (this)
				{ // because of cIdx
					if (b.getName().equals(fc_continue.getName()))
					{
						components.get(cIdx).fillEntry();
						int nextComponent = getNextUnfilledEntry(
								cIdx, components);
						if (nextComponent == cIdx && components.get(cIdx).entryFilled())
						{
							if (function.call(form))
								setContent(retpan);
						}
						else
						{
							cIdx = nextComponent;
							setFormContent(cIdx);
						}
					}
					else if (b.getName().equals(fc_previous.getName()))
					{
						cIdx = getNextEntry(cIdx, nEntries, -1, false);
						setFormContent(cIdx);
					}
					else if (b.getName().equals(fc_next.getName()))
					{
						cIdx = getNextEntry(cIdx, nEntries, 1, false);
						setFormContent(cIdx);
					}
					else if (b.getName().equals(fc_exit.getName()))
					{
						setContent(retpan);
					}
				} // end synchronized block
			}
		}
		
		private void setFormContent(int id)
		{
			if (components.get(id) == null)
				return;
			formContent.removeAll();
			formContent.add((Component) components.get(id));
			requestFocus();
			formContent.revalidate();
			formContent.repaint();
		}
		
		private JPanel initControlPanel()
		{
			JPanel panel = new JPanel(new GridLayout(1, 3));
			fc_continue = new JButton(msg.getInfo(
					Messages.INFO_UI_FORM_CONTINUE));
			fc_continue.setName("fc_continue");
			fc_continue.addActionListener(this);
			fc_previous = new JButton(msg.getInfo(
					Messages.INFO_UI_FORM_PREVIOUS));
			fc_previous.setName("fc_previous");
			fc_previous.addActionListener(this);
			fc_next = new JButton(msg.getInfo(
					Messages.INFO_UI_FORM_NEXT));
			fc_next.setName("fc_next");
			fc_next.addActionListener(this);
			fc_exit = new JButton(msg.getInfo(
					Messages.INFO_UI_FORM_EXIT));
			fc_exit.setName("fc_exit");
			fc_exit.addActionListener(this);
			panel.add(fc_continue);
			panel.add(fc_previous);
			panel.add(fc_next);
			panel.add(fc_exit);
			return panel;
		}
	}
	
	public static final Font FONT = new Font("Courier", Font.PLAIN, 16);
	private JFrame frame;
	public UserHandle uh;
	private JButton restartButton, settingsButton,
	resultsButton, databaseButton;
	private JPanel pageContent;
	private static final long serialVersionUID = -3896988492887782839L;
	
	/**
	 * Sets the page content.
	 * 
	 * @param panel The panel that contains the page content.
	 */
	public void setContent(Component panel)
	{
		if (panel == null)
			return;
		pageContent.removeAll();
		pageContent.add(panel, BorderLayout.CENTER);
		panel.requestFocus();
		pageContent.revalidate();
		pageContent.repaint();
	}
	
	public void setContent(Container contentContainer, Component panel)
	{
		if (contentContainer == null || panel == null)
			return;
		contentContainer.removeAll();
		contentContainer.add(panel, BorderLayout.CENTER);
		panel.requestFocus();
		contentContainer.revalidate();
		contentContainer.repaint();
	}
	
	static
	{
		Runtime runtime = Runtime.getRuntime();

		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		sb.append("_______________________________\n");
		sb.append("|     Memory     |   Size     |\n");
		sb.append("|----------------|------------|\n");
		sb.append(String.format("| Free:\t\t\t | %7d Kb |\n", (freeMemory/1024)));
		sb.append(String.format("| Allocated:\t | %7d Kb |\n", (allocatedMemory/1024)));
		sb.append(String.format("| Max:\t\t\t | %7d Kb |\n", (maxMemory/1024)));
		sb.append(String.format("| Total free:\t | %7d Kb |\n", ((freeMemory + (maxMemory - allocatedMemory))/1024)));
		sb.append("|________________|____________|\n");
		System.out.println(sb.toString());
	}
}
