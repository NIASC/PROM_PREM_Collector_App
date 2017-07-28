package testing;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class test extends JFrame
{
	public test()
	{
		getContentPane().add(new JLabel("hello world"));
		setVisible(true);
		setResizable(true);
		pack();
	}
}
