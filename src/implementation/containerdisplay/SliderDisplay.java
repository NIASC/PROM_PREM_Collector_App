package implementation.containerdisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.containers.form.SliderContainer;
import core.interfaces.UserInterface.FormComponentDisplay;

public class SliderDisplay extends JPanel implements FormComponentDisplay, ChangeListener
{
	/* Public */
	
	@Override
	public void requestFocus()
	{
		
	}

	@Override
	public void stateChanged(ChangeEvent e)
	{
		if (e.getSource() instanceof JSlider)
		{
			JSlider s = (JSlider) e.getSource();
			if (!s.getValueIsAdjusting())
			{
				response = s.getValue();
			}
		}
	}

	@Override
	public boolean fillEntry()
	{
		if (response == null)
			return false;
		return sc.setEntry(response);
	}

	@Override
	public boolean entryFilled()
	{
		fillEntry();
		return sc.hasEntry();
	}
	
	/* Protected */
	
	protected SliderDisplay(SliderContainer sc)
	{
		setLayout(new BorderLayout());
		this.sc = sc;
		response = null;

		JTextArea jtf = new JTextArea(0, 35);
		jtf.setEditable(false);
		jtf.setLineWrap(true);
		jtf.setWrapStyleWord(true);
		jtf.setForeground(Color.BLACK);
		jtf.setBackground(new Color(0xf0, 0xf0, 0xf0));
		jtf.setText(sc.getStatement());
		add(jtf, BorderLayout.NORTH);
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL,
				sc.getLowerBound(), sc.getUpperBound(),
				(sc.getLowerBound() + sc.getUpperBound()) / 2);
		slider.addChangeListener(this);
		slider.setMajorTickSpacing((sc.getLowerBound() + sc.getUpperBound()) / 2);
		slider.setMinorTickSpacing((sc.getLowerBound() + sc.getUpperBound()) / 10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		add(slider, BorderLayout.CENTER);
	}
	
	/* Private */
	
	private static final long serialVersionUID = 244108485873521112L;
	private SliderContainer sc;
	private Integer response;
}
