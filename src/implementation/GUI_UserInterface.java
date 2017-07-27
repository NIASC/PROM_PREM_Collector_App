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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

import core.UserHandle;
import core.containers.Form;
import core.containers.form.FormContainer;
import core.interfaces.Messages;
import core.interfaces.UserInterface;
import implementation.containerdisplay.ContainerDisplays;

public class GUI_UserInterface extends JApplet implements ActionListener, UserInterface
{
	/* Public */
	
	public static final Font FONT = new Font(Font.SERIF, Font.PLAIN, 16);
	
	public GUI_UserInterface()
	{
		this(true);
	}
	
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
	public void displayError(String message, boolean popup)
	{
		if (popup)
			JOptionPane.showMessageDialog(
					null, message, "Error",
					JOptionPane.ERROR_MESSAGE);
		else
			displayEmbeddedMessage(String.format("%s", message),
					Color.RED);
	}

	@Override
	public void displayMessage(String message, boolean popup)
	{
		if (popup)
			JOptionPane.showMessageDialog(
					null, message, "Message",
					JOptionPane.PLAIN_MESSAGE);
		else
			displayEmbeddedMessage(message, Color.BLACK);
	}

	@Override
	public boolean presentForm(Form form, ReturnFunction function,
			boolean multiple)
	{
		setContent(new GUIForm(form, function, getContent(), multiple));
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
	}
	
	@Override
	public void destroy()
	{
		/* when the webpage is destroyed */
		if (!embedded && frame != null)
			frame.dispose();
	}
	
	@Override
	public void start()
	{
		/* when the user returns to the webpage. */
		if (!embedded)
		{
			frame.setVisible(true);
			frame.pack();
		}
		setContent(new LoginScreen());
	}
	
	public void stop()
	{
		/* when the user leaves the web page. */
		/* stores the page in cache              */
		
		uh.logout();
		if (frame != null)
		{
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	/**
	 * Sets the page content.
	 * 
	 * @param panel The panel that contains the page content.
	 */
	public void setContent(Container panel)
	{
		if (panel == null)
			return;
		displayMessage("", false); // reset any error messages
		JViewport jvp = pageScroll.getViewport();
		jvp.removeAll();
		jvp.add(panel);
		panel.requestFocus();
		jvp.revalidate();
		jvp.repaint();
	}
	
	/**
	 * Retrieves the current contaient container.
	 * 
	 * @return The current content container.
	 */
	public Container getContent()
	{
		Container c = null;
		synchronized(pageScroll.getViewport().getTreeLock())
		{
			c = (Container) pageScroll.getViewport().getComponent(0);
		}
		return c;
	}
	
	/* Protected */
	
	/* Private */
	
	private boolean embedded;
	
	private JFrame frame;
	private UserHandle uh;
	private JButton mainmenuButton, logoutButton, menuButton1, exitButton;
	private JPanel pageContent;
	private JScrollPane pageScroll, messagePanel;
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
		sb.append("_____________________________\n");
		sb.append("|    Memory    |    Size    |\n");
		sb.append("|______________|____________|\n");
		sb.append(String.format("| Free:        | %7d Kb |\n", (freeMemory/1024)));
		sb.append(String.format("| Allocated:   | %7d Kb |\n", (allocatedMemory/1024)));
		sb.append(String.format("| Max:         | %7d Kb |\n", (maxMemory/1024)));
		sb.append(String.format("| Total free:  | %7d Kb |\n", ((freeMemory + (maxMemory - allocatedMemory))/1024)));
		sb.append("|______________|____________|\n");
		System.out.println(sb.toString());
	}
	
	/**
	 * Initializes the GUI. 
	 */
	private void initGUI()
	{
		setLayout(new BorderLayout());
		
		pageContent = new JPanel(new BorderLayout());
		pageContent.setPreferredSize(new Dimension(600,300));
		pageScroll = new JScrollPane(null,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pageContent.add(pageScroll, BorderLayout.CENTER);
		
		messagePanel = new JScrollPane(null,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		messagePanel.getViewport().setPreferredSize(new Dimension(200, 100));
		
		add(pageContent, BorderLayout.CENTER);
		add(makeMenuPanel(), BorderLayout.NORTH);
		add(messagePanel, BorderLayout.SOUTH);
		
		if (!embedded)
		{
			frame = new JFrame("PROM/PREM Collector GUI");
			frame.setContentPane(this);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
		}
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
		Dimension dim = new Dimension(150, 30);
		JPanel bPanel = new JPanel(new GridLayout(1, 4));
		mainmenuButton = SwingComponents.makeButton("Main menu", "mainmenuButton",
				"Return to main menu.", false, null, null, null, dim, this);
		menuButton1 = SwingComponents.makeButton("Dummy1", "menuButton1",
				"Unused.", false, null, null, null, dim, this);
		logoutButton = SwingComponents.makeButton("Logout", "logoutButton",
				"Log out.", false, null, null, null, dim, this);
		exitButton = SwingComponents.makeButton("Exit", "exit",
				"Exit.", false, null, null, null, dim, this);
		bPanel.add(mainmenuButton);
		bPanel.add(menuButton1);
		bPanel.add(logoutButton);
		bPanel.add(exitButton);
		return bPanel;
	}
	
	private void displayEmbeddedMessage(String message, Color textColor)
	{
		JTextArea jtf = SwingComponents.makeTextArea(message, null, null, false,
				null, textColor, null, null, null, false, 0, 0);
		JViewport jvp = messagePanel.getViewport();
		jvp.removeAll();
		jvp.add(jtf);
		jvp.revalidate();
		jvp.repaint();
	}
	
	/**
	 * 
	 * @author Marcus Malmquist
	 *
	 */
	private class LoginScreen extends JPanel implements ActionListener
	{
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
		
		static final long serialVersionUID = 2352904758935918090L;
		JButton login, register;
		JTextField usernameTF;
		JPasswordField passwordTF;

		LoginScreen()
		{
			setLayout(new GridBagLayout());
			
			/* entry fields */
			Dimension d = new Dimension(240, 25);
			
			JPanel userPanel = new JPanel(new BorderLayout());
			JLabel usernameL = SwingComponents.makeLabel(String.format("%s: ",
					Messages.getMessages().getInfo(Messages.INFO_UH_ENTER_USERNAME)),
					null, null, false, null, null, null, null);
			userPanel.add(usernameL, BorderLayout.WEST);
			usernameTF = SwingComponents.makeTextField(null, "usernameTF", null, true,
					null, null, null, d, this, true);
			userPanel.add(usernameTF, BorderLayout.CENTER);
			
			JPanel passPanel = new JPanel(new BorderLayout());
			JLabel passwordL = SwingComponents.makeLabel(String.format("%s: ",
					Messages.getMessages().getInfo(Messages.INFO_UH_ENTER_PASSWORD)),
					null, null, false, null, null, null, null);
			passPanel.add(passwordL, BorderLayout.WEST);
			passwordTF = SwingComponents.makeSecretTextField(null, "passwordTF", null,
					true, null, null, null, d, this, true);
			passPanel.add(passwordTF, BorderLayout.CENTER);
			
			/* button panel */
			JPanel buttons = new JPanel(new GridLayout(1, 0));
			login = SwingComponents.makeButton(
					Messages.getMessages().getInfo(Messages.INFO_LOGIN),
					"login", null, false, null, null, null, null, this);
			register = SwingComponents.makeButton(
					Messages.getMessages().getInfo(Messages.INFO_REGISTER),
					"register", null, false, null, null, null, null, this);

			buttons.add(login);
			buttons.add(register);
			
			/* add components */

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.EAST;
			gbc.insets.bottom = gbc.insets.top = 1;
			gbc.gridx = 0;
			int gridy = 0;

			gbc.gridy = gridy++;
			add(userPanel, gbc);
			gbc.gridy = gridy++;
			add(passPanel, gbc);
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets.bottom = gbc.insets.top = 5;
			gbc.gridy = gridy++;
			add(buttons, gbc);
		}
	}
	
	private class WelcomeScreen extends JPanel implements ActionListener
	{
		
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
		
		static final long serialVersionUID = 2805273101165405918L;
		JButton questionnaire, viewData;
		
		WelcomeScreen()
		{
			setLayout(new GridBagLayout());

			questionnaire = SwingComponents.makeButton(
					Messages.getMessages().getInfo(Messages.INFO_START_QUESTIONNAIRE),
					"questionnaire", null, false, null, null, null, null, this);
			viewData = SwingComponents.makeButton(
					Messages.getMessages().getInfo(Messages.INFO_VIEW_STATISTICS),
					"viewData", null, false, null, null, null, null, this);

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets.bottom = gbc.insets.top = 5;
			gbc.gridx = 0;
			gbc.gridy = 0;
			add(questionnaire, gbc);
			gbc.gridy = 1;
			add(viewData, gbc);
		}
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
		
		@Override
		public void requestFocus()
		{
			List<Component> lcomp = new ArrayList<Component>();
			synchronized(getTreeLock())
			{
				lcomp.addAll(Arrays.asList(getComponents()));
			}
			for (Iterator<Component> itr = lcomp.iterator(); itr.hasNext();)
			{
				Component c = itr.next();
				if (c == (Component) components.get(cIdx))
				{
					c.requestFocus();
					break;
				}
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
							RetFunContainer rfc = function.call(form);
							if (rfc.valid)
							{
								displayMessage("", false);
								setContent(retpan);
								if (rfc.nextfunc != null)
									rfc.nextfunc.call();
							}
							else if (rfc.message != null)
								displayError(rfc.message, false);
						}
						else
						{
							cIdx = nextComponent;
							setFormContent();
						}
					}
					else if (b.getName().equals(fc_previous.getName()))
					{
						components.get(cIdx).fillEntry();
						cIdx = getNextEntry(cIdx, nEntries, -1, false);
						setFormContent();
					}
					else if (b.getName().equals(fc_next.getName()))
					{
						components.get(cIdx).fillEntry();
						cIdx = getNextEntry(cIdx, nEntries, 1, false);
						setFormContent();
					}
					else if (b.getName().equals(fc_back.getName()))
					{
						setContent(retpan);
					}
					if (cIdx == getNextEntry(cIdx, nEntries, 1, false))
						fc_next.setEnabled(false);
					else
						fc_next.setEnabled(true);
					
					if (cIdx == getNextEntry(cIdx, nEntries, -1, false))
						fc_previous.setEnabled(false);
					else
						fc_previous.setEnabled(true);
					if (getNextUnfilledEntry(cIdx, components) == cIdx)
						fc_continue.setText(msg.getInfo(
								Messages.INFO_UI_FORM_FINISH));
					else
						fc_continue.setText(msg.getInfo(
								Messages.INFO_UI_FORM_CONTINUE));
				} // end synchronized block
			}
		}
		
		static final long serialVersionUID = -7513730435118997364L;
		final int nEntries;
		boolean displayMultiple;
		int cIdx;
		Messages msg = Messages.getMessages();
		
		JPanel formControl;
		JButton fc_continue, fc_previous, fc_next, fc_back;
		GridBagConstraints gbc;
		
		final Map<Integer, FormComponentDisplay> components;
		final Form form;
		final ReturnFunction function;
		final Container retpan;
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
		GUIForm(final Form form, final ReturnFunction function,
				final Container retpan, boolean displayMultiple)
		{
			this.form = form;
			this.function = function;
			this.retpan = retpan;
			this.displayMultiple = displayMultiple;
			
			setLayout(new GridBagLayout());
			gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.EAST;
			gbc.gridx = 0;
			
			components = fillContents(form);
			nEntries = components.size();
			
			formControl = initControlPanel();
			
			setFormContent();
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
		Map<Integer, FormComponentDisplay> fillContents(Form form)
		{
			Map<Integer, FormComponentDisplay> contents =
					new HashMap<Integer, FormComponentDisplay>();
			int id = 0;
			form.jumpTo(Form.AT_BEGIN);
			do
			{
				contents.put(id++, form.currentEntry().getDisplayable(GUI_UserInterface.this));
			} while(!form.endOfForm() && form.nextEntry() != null);
			return contents;
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
		int getNextEntry(int cIndex, int nEntries, int steps,
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
		int getNextUnfilledEntry(int currentIdx,
				Map<Integer, FormComponentDisplay> form)
		{
			int entries = form.size();
			int i = getNextEntry(currentIdx, entries, 1, true);
			while (form.get(i).entryFilled()
					&& i != currentIdx)
				i = getNextEntry(i, entries, 1, true);
			return i;
		}
		
		void setFormContent()
		{
			removeAll();

			int i = 0;
			gbc.insets.bottom = gbc.insets.top = 1;
			if (displayMultiple)
			{
				for (; i < components.size(); ++i)
				{
					gbc.gridy = i;
					add((Component) components.get(i), gbc);
				}
			}
			else
			{
				gbc.gridy = i++;
				add((Component) components.get(cIdx), gbc);
			}
			gbc.gridy = i;
			gbc.insets.bottom = gbc.insets.top = 5;
			add(formControl, gbc);
			
			requestFocus();
			revalidate();
			repaint();
		}
		
		JPanel initControlPanel()
		{
			JPanel panel = new JPanel(new GridLayout(1, 0));
			fc_continue = SwingComponents.makeButton(msg.getInfo(Messages.INFO_UI_FORM_CONTINUE),
					"fc_continue", null, false, null, null, null, null, this);
			fc_previous = SwingComponents.makeButton(msg.getInfo(Messages.INFO_UI_FORM_PREVIOUS),
					"fc_previous", null, false, null, null, null, null, this);
			if (cIdx == getNextEntry(cIdx, nEntries, -1, false))
				fc_previous.setEnabled(false);
			
			fc_next = SwingComponents.makeButton(msg.getInfo(Messages.INFO_UI_FORM_NEXT),
					"fc_next", null, false, null, null, null, null, this);
			fc_back = SwingComponents.makeButton(msg.getInfo(Messages.INFO_UI_FORM_BACK),
					"fc_back", null, false, null, null, null, null, this);
			
			panel.add(fc_continue);
			if (!displayMultiple)
			{
				panel.add(fc_previous);
				panel.add(fc_next);
			}
			panel.add(fc_back);
			return panel;
		}
	}
}
