/** AreaDisplay.java
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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import applet.core.containers.form.AreaContainer;
import applet.core.containers.form.FieldContainer;
import applet.core.interfaces.Messages;
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
public class AreaDisplay extends JPanel implements FormComponentDisplay
{
	/* Public */
	
	@Override
	public void requestFocus()
	{
		area.requestFocus();
	}

	@Override
	public boolean fillEntry()
	{
		return ac.setEntry(area.getText());
	}

	@Override
	public boolean entryFilled()
	{
		return ac.hasEntry();
	}
	
	/* Protected */
	
	/**
	 * Creates a displayable wrapper for {@code ac}.
	 * 
	 * @param ac The instance of the {@code AreaContainer} that
	 * 		the instance of this {@code AreaDisplay} should act as a
	 * 		wrapper for.
	 * 
	 * @see AreaContainer
	 */
	protected AreaDisplay(AreaContainer ac)
	{
		setLayout(new BorderLayout());
		this.ac = ac;

		String description = "";
		String optional = Messages.getMessages().getInfo(
				Messages.INFO_UI_FORM_OPTIONAL);
		if (ac.getDescription() != null && !ac.getDescription().isEmpty())
			description = "\n\n"+ac.getDescription();
		JTextArea jta = AddTextArea(
				(ac.allowsEmpty() ? "("+optional+") " : "") + ac.getStatement()
				+ description + "\n", 0, 35, false, false);
		add(jta, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(null,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		area = AddTextArea(null, 5, 35, true, true);
		scrollPane.getViewport().add(area);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	/* Private */

	private static final long serialVersionUID = -1966954986653918924L;
	private AreaContainer ac;
	private JTextArea area;
	
	private static JTextArea AddTextArea(String text, int rows, int columns,
			boolean opaque, boolean editable)
	{
		return SwingComponents.makeTextArea(text, null, null, opaque,
				null, null, null, null, null, editable, rows, columns);
	}
}
