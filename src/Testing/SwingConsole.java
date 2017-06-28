package Testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

public class SwingConsole extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private JLabel progressLabel;
	private JTextArea questionLabel;
	private JScrollPane scrollArea;
	private JButton answerButton;
	private JTextField answerField;
	
	private boolean userAnswered;
	
	public SwingConsole()
	{
		setLayout(new BorderLayout());
		progressLabel = new JLabel(String.format("%d/%d questions answered", 0, 0));
		questionLabel = new JTextArea(20, 20);
		questionLabel.setEditable(false);
		questionLabel.setLineWrap(true);
		questionLabel.setWrapStyleWord(true);
		questionLabel.setText("Hint: Hit return in the answer field to start a new "
				+ "round with current (default) settings.");
		scrollArea = new JScrollPane(questionLabel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		// textfield to put answer in
		JPanel answerPanel = new JPanel(new BorderLayout());
		answerField = new JTextField(20);
		answerField.setName("answer");
		answerField.addActionListener(this);
		answerField.requestFocusInWindow();
		answerPanel.add(answerField, BorderLayout.CENTER);
		Dimension d = new Dimension(75, 25);
		answerButton = AddButton("Send", "answer", "Click to Answer.", true, true,
				Color.LIGHT_GRAY, Color.BLACK, d);
		answerPanel.add(answerButton, BorderLayout.EAST);
		add(answerPanel, BorderLayout.SOUTH);
		
		add(progressLabel, BorderLayout.NORTH);
		add(scrollArea, BorderLayout.CENTER);
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
		return button;
	}
	
	public void displayNewQuestion(String message)
	{
		progressLabel.setText(String.format("%d/%d questions answered", 0, 0));
		questionLabel.setText(message);
	}
	
	public void clearDisplayQuestion()
	{
		progressLabel.setText(String.format("%d/%d questions answered", 0, 0));
		questionLabel.setText("");
		answerField.setText("");
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
	public String getUserInput()
	{
		synchronized (this)
		{
			while (!userAnswered)
			{
				try
				{
					wait();
				}
				catch (InterruptedException ie) {}
			}
			userAnswered = false;
		}
		return answerField.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JButton)
		{
			JButton b = (JButton) e.getSource();
			if (b.getName() != null)
			{
				if (b.getName().equals(answerField.getName()))
				{
					userAnswered = true;
					synchronized(this)
					{
						notify();
					}
				}
			}
		}
		else if (e.getSource() instanceof JTextField)
		{
			JTextField t = (JTextField) e.getSource();
			if (t.getName() != null)
			{
				if (t.getName().equals(answerButton.getName()))
				{
					userAnswered = true;
					synchronized(this)
					{
						notify();
					}
				}
			}
		}
	}
	
	@Override
	public void requestFocus()
	{
		answerField.requestFocus();
	}
}
