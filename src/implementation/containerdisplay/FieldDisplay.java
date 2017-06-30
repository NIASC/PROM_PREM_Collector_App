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
package implementation.containerdisplay;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import core.containers.form.FieldContainer;
import core.interfaces.UserInterface.FormComponentDisplay;

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
public class FieldDisplay extends JPanel implements FormComponentDisplay
{
	private static final long serialVersionUID = 2210804480530383502L;

	private FieldContainer fc;
	
	private JLabel fieldLabel;
	private JTextField field;
	
	/**
	 * This constructor should not be used.
	 */
	@SuppressWarnings("unused")
	private FieldDisplay() { }
	
	/**
	 * Initializes login variables.
	 * @param fc The instance of the FieldContainer that
	 * 		the instance of this FieldDisplay should act as a
	 * 		wrapper for.
	 */
	protected FieldDisplay(FieldContainer fc)
	{
		setLayout(new BorderLayout());
		this.fc = fc;
		fieldLabel = new JLabel(fc.getStatement());
		add(fieldLabel, BorderLayout.WEST);
		field = fc.isSecret() ? new JPasswordField(32) : new JTextField(32);
		field.setPreferredSize(new Dimension(80, 25));
		add(field, BorderLayout.CENTER);
	}
	/*
	@Override
	public void requestFocus()
	{
		field.requestFocus();
	}*/

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
