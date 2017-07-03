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
package implementation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import core.UserHandle;
import core.containers.Form;
import core.containers.form.FormContainer;
import core.interfaces.Messages;
import core.interfaces.UserInterface;
import implementation.containerdisplay.ContainerDisplays;

public class GUI_UserInterface extends JApplet implements ActionListener, UserInterface
{
	/* Public */
	
	public static final Font FONT = new Font("Courier", Font.PLAIN, 16);
	
	public GUI_UserInterface(boolean embedded)
	{
		this.embedded = embedded;
		uh = new UserHandle(this);
		initGUI();
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton b = (JButton) e.getSource();
			if (b.getName() != null)
			{
				if (b.getName().equals(mainmenuButton.getName()))
				{
					if (uh.isLoggedIn())
						setContent(new WelcomeScreen());
					else
						setContent(new LoginScreen());
				}
				else if (b.getName().equals(menuButton1.getName()))
				{
					// unused
				}
				else if (b.getName().equals(logoutButton.getName()))
				{
					uh.logout();
					setContent(new LoginScreen());
				}
				else if (b.getName().equals(exitButton.getName()))
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
	public boolean presentForm(Form form, ReturnFunction function,
			boolean displayMultiple)
	{
		Component panel = null;
		synchronized(pageContent.getTreeLock())
		{
			panel = pageContent.getComponents()[0];
		}
		setContent(new GUIForm(form, function, panel, displayMultiple));
		return true;
	}
	
	@Override
	public FormComponentDisplay getContainerDisplay(FormContainer fc)
	{
		return ContainerDisplays.getDisplay(fc);
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

		if (!embedded && frame != null)
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
		uh.logout();
		if (frame != null)
			frame.dispose();
	}
	
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
	
	/* Protected */
	
	/* Private */
	
	private boolean embedded;
	
	private JFrame frame;
	private UserHandle uh;
	private JButton mainmenuButton, logoutButton, menuButton1, exitButton;
	private JPanel pageContent;
	private static final long serialVersionUID = -3896988492887782839L;
	
	static
	{
		/* Prints (an estimation of the) memory usage of this
		 * application. The code is a placeholder for future
		 * statistics. */
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
	
	/**
	 * Initializes the GUI. 
	 */
	private void initGUI()
	{
		setLayout(new BorderLayout());
		
		pageContent = new JPanel(new BorderLayout());
		pageContent.setPreferredSize(new Dimension(400,300));
		pageContent.setMinimumSize(new Dimension(200,100));
		pageContent.setMaximumSize(new Dimension(400,300));
		add(pageContent, BorderLayout.CENTER);
		
		add(makeMenuPanel(), BorderLayout.NORTH);
		
		if (!embedded)
		{
			frame = new JFrame("PROM/PREM Collector GUI");
			frame.setContentPane(this);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setVisible(true);
			frame.pack();
		}
		
		setContent(new LoginScreen());
	}
	
	/**
	 * Creates the static menu bars. The menu bars are not part of
	 * the page content so they should not be changed anywhere except
	 * in this class.
	 * 
	 * @return The panel containing menu buttons.
	 */
	private JPanel makeMenuPanel()
	{
		Dimension dim = new Dimension(150, 25);
		JPanel bPanel = new JPanel(new GridLayout(1, 4));
		mainmenuButton = AddButton("Main menu", "mainmenuButton", "Return to main menu.",
				true, true, Color.LIGHT_GRAY, Color.BLACK, dim);
		menuButton1 = AddButton("Dummy1", "menuButton1", "Unused.",
				true, true, Color.LIGHT_GRAY, Color.BLACK, dim);
		logoutButton = AddButton("Logout", "logoutButton", "Log out.",
				true, true, Color.LIGHT_GRAY, Color.BLACK, dim);
		exitButton = AddButton("Exit", "exit",
				"Exit.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, dim);
		bPanel.add(mainmenuButton);
		bPanel.add(menuButton1);
		bPanel.add(logoutButton);
		bPanel.add(exitButton);
		return bPanel;
	}
	
	/**
	 * Creates a button with various attributes.
	 * 
	 * @param buttonText The text to put on the button.
	 * @param nameSet The name of the button. Used for determining
	 * 		which button is clicked in the action listener.
	 * @param tooltip The tooltip text.
	 * @param actionListen the action listener.
	 * @param opaque Whether or not the button should be opaque.
	 * @param background The background color.
	 * @param border The border color.
	 * @param d The preferred dimension of the button.
	 * 
	 * @return A button with the default settings with the addition of
	 * 		the supplied settings.
	 */
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
	private int getNextEntry(int cIndex, int nEntries, int steps,
			boolean wrap)
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
	 * 		supplied form. The keys are the ID (i.e. order of
	 * 		appearance) and the values are the displayable objects.
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
		/* Public */

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
			buttons.add(login);
			buttons.add(register);
			
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
					uh.login(usernameTF.getText(),
							new String(passwordTF.getPassword()));
					if (uh.isLoggedIn())
					{
						usernameTF.setText(null);
						passwordTF.setText(null);
						setContent(new WelcomeScreen());
						uh.updatePassword();
					}
				}
				else if (b.getName().equals(register.getName()))
				{
					uh.register();
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
		/* Protected */
		
		/* Private */
		
		private static final long serialVersionUID = 2352904758935918090L;
		private JPanel buttons, entryfields;
		private JButton login, register;
		private JLabel usernameL, passwordL;
		private JTextField usernameTF;
		private JPasswordField passwordTF;
	}
	
	private class WelcomeScreen extends JPanel implements ActionListener
	{
		/* Public */
		
		public WelcomeScreen()
		{

			setLayout(new BorderLayout());
			buttons = new JPanel(new GridLayout(1, 3));
			questionnaire = new JButton("Start questionnaire");
			questionnaire.setName("questionnaire");
			questionnaire.addActionListener(this);
			viewData = new JButton("View Statistics");
			viewData.setName("viewData");
			viewData.addActionListener(this);
			buttons.add(questionnaire);
			buttons.add(viewData);
			
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
					uh.startQuestionnaire();
				}
				else if (b.getName().equals(viewData.getName()))
				{
					uh.viewData();
				}
			}
		}
		
		/* Protected */
		
		/* Private */
		
		private static final long serialVersionUID = 2805273101165405918L;
		private JPanel buttons;
		private JButton questionnaire, viewData;
	}
	
	/**
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
		/**
		 * 
		 * @param form The form to display.
		 * @param function The function to call when this form has been
		 * 		filled.
		 * @param retpan The displayable object (panel) to return to
		 * 		when this function has returned.
		 * @param displayMultiple True if the form should display
		 * 		multiple entries at the same time.
		 */
		public GUIForm(final Form form, final ReturnFunction function,
				final Component retpan, boolean displayMultiple)
		{
			this.form = form;
			this.function = function;
			this.retpan = retpan;
			this.displayMultiple = displayMultiple;
			
			setLayout(new BorderLayout());
			
			components = fillContents(form);
			nEntries = components.size();
			
			formControl = initControlPanel();
			
			formContent = new JPanel(new GridBagLayout());
			scrollArea = new JScrollPane(formContent,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			add(scrollArea, BorderLayout.CENTER);
			add(formControl, BorderLayout.SOUTH);
			
			setFormContent();
		}
		
		@Override
		public void requestFocus()
		{
			Component[] c = null;
			synchronized(formContent.getTreeLock())
			{
				c = formContent.getComponents();
			}
			if (c != null)
				for (Component comp : c)
					if (comp != null && comp == (Component) components.get(cIdx))
						comp.requestFocus();
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
							setFormContent();
						}
					}
					else if (b.getName().equals(fc_previous.getName()))
					{
						cIdx = getNextEntry(cIdx, nEntries, -1, false);
						setFormContent();
					}
					else if (b.getName().equals(fc_next.getName()))
					{
						cIdx = getNextEntry(cIdx, nEntries, 1, false);
						setFormContent();
					}
					else if (b.getName().equals(fc_exit.getName()))
					{
						setContent(retpan);
					}
				} // end synchronized block
			}
		}
		
		/* Protected */
		
		/* Private */
		
		private static final long serialVersionUID = -7513730435118997364L;
		private final int nEntries;
		private boolean displayMultiple;
		private int cIdx;
		private Messages msg = Messages.getMessages();
		
		private JPanel formControl, formContent;
		private JScrollPane scrollArea;
		private JButton fc_continue, fc_previous, fc_next, fc_exit;
		
		private final HashMap<Integer, FormComponentDisplay> components;
		private final Form form;
		private final ReturnFunction function;
		private final Component retpan;
		
		private void setFormContent()
		{
			formContent.removeAll();
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.EAST;
			gbc.gridx = 0;
			if (displayMultiple)
			{
				for (int i = 0; i < components.size(); ++i)
				{
					gbc.gridy = i;
					formContent.add((Component) components.get(i), gbc);
				}
			}
			else
			{
				gbc.gridy = 0;
				formContent.add((Component) components.get(cIdx), gbc);
			}
			
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
}
