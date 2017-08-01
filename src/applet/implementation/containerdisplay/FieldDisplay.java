/** FieldDisplay.java
 * 
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
package applet.implementation.containerdisplay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import applet.core.containers.form.FieldContainer;
import applet.core.interfaces.UserInterface;
import applet.core.interfaces.UserInterface.FormComponentDisplay;
import applet.implementation.SwingComponents;

/**
 * This class is a displayable wrapper for {@code FieldContainer}.
 * It handles placing the {@code FieldContainer} in an object that
 * the implementation of the {@code UserInterface} can display.
 * 
 * @author Marcus Malmquist
 * 
 * @see FieldContainer
 * @see UserInterface
 *
 */
public class FieldDisplay extends JPanel implements FormComponentDisplay,
FocusListener
{
	/* Public */

	@Override
	public void focusGained(FocusEvent e)
	{
		;
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		fillEntry();
	}
	
	@Override
	public void requestFocus()
	{
		field.requestFocus();
	}

	@Override
	public boolean fillEntry()
	{
		return fc.setEntry(field.getText());
	}

	@Override
	public boolean entryFilled()
	{
		return fc.hasEntry();
	}
	
	/* Protected */
	
	/**
	 * Creates a displayable wrapper for {@code fc}.
	 * 
	 * @param fc The instance of the {@code FieldContainer} that
	 * 		the instance of this {@code FieldDisplay} should act as a
	 * 		wrapper for.
	 * 
	 * @see FieldContainer
	 */
	protected FieldDisplay(FieldContainer fc)
	{
		setLayout(new BorderLayout());
		this.fc = fc;
		
		fieldLabel = AddLabel(String.format("%s: ", fc.getStatement()));
		add(fieldLabel, BorderLayout.WEST);
		
		Dimension d = new Dimension(240, 25);
		if (fc.isSecret())
			field = AddSecretTextField(fc.getEntry(), d);
		else
			field = AddTextField(fc.getEntry(), d);
		field.addFocusListener(this);
		add(field, BorderLayout.CENTER);
	}
	
	/* Private */
	
	private static final long serialVersionUID = 2210804480530383502L;
	private FieldContainer fc;
	private JLabel fieldLabel;
	private JTextField field;
	
	private static JLabel AddLabel(String labelText)
	{
		return SwingComponents.makeLabel(
				labelText, null, null, false, null, null, null, null);
	}
	
	private static JTextField AddTextField(String text, Dimension d)
	{
		return SwingComponents.makeTextField(text, null, null, true,
				null, null, null, d, null, true);
	}
	
	private static JPasswordField AddSecretTextField(String text, Dimension d)
	{
		return SwingComponents.makeSecretTextField(text, null, null,
				true, null, null, null, d, null, true);
	}
}
