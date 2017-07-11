/** SwingComponents.java
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
package implementation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;

/**
 * This class provides functions to make swing components in a compact
 * manner as well as make it easier to modify default settings to all
 * components in the program.
 * 
 * Any swing components (JLabel, JButton etc.) in this program should
 * use these 'constructors'.
 * 
 * @author Marcus
 *
 */
public class SwingComponents
{

	public static JButton makeButton(String buttonText, String name,
			String tooltip, boolean opaque, Color background,
			Color foreground, Color border, Dimension d,
			ActionListener listener)
	{
		JButton button = new JButton();
		SwingComponents.modifyButton(button, buttonText, name, tooltip, opaque,
				background, foreground, border, d);
		if (listener != null)
			button.addActionListener(listener);
		return button;
	}
	
	public static JRadioButton addToggleButton(String buttonText, String name,
			String tooltip, boolean opaque, Color background,
			Color foreground, Color border, Dimension d,
			ItemListener listener)
	{
		JRadioButton button = new JRadioButton();
		SwingComponents.modifyButton(button, buttonText, name, tooltip, opaque,
				background, foreground, border, d);
		if (listener != null)
			button.addItemListener(listener);
		return button;
	}
	
	public static JCheckBox addToggleButton2(String buttonText, String name,
			String tooltip, boolean opaque, Color background,
			Color foreground, Color border, Dimension d,
			ItemListener listener)
	{
		JCheckBox button = new JCheckBox();
		SwingComponents.modifyButton(button, buttonText, name, tooltip, opaque,
				background, foreground, border, d);
		if (listener != null)
			button.addItemListener(listener);
		return button;
	}

	public static JLabel makeLabel(String labelText, String name,
			String tooltip, boolean opaque, Color background,
			Color foreground, Color border, Dimension d)
	{
		JLabel label = new JLabel();
		SwingComponents.modifyJComponent(label, name, tooltip, opaque,
				background, foreground, border, d);
		label.setText(labelText);
		return label;
	}

	public static JSpinner makeSpinner(Object value, String name,
			String tooltip, boolean opaque, Color background,
			Color foreground, Color border, Dimension d,
			ChangeListener listener, JComponent editor)
	{
		JSpinner spinner = new JSpinner();
		SwingComponents.modifyJComponent(spinner, name, tooltip, opaque,
				background, foreground, border, d);
		spinner.setValue(value);
		spinner.setEditor(editor);
		if (listener != null)
			spinner.addChangeListener(listener);
		return spinner;
	}

	public static <T> JComboBox<T> makeComboBox(Class<T> c,
			String name, String tooltip, boolean opaque,
			Color bgColor, Color fgColor, Color borderColor,
			Dimension d, ItemListener listener, boolean editable,
			Border border)
	{
		JComboBox<T> combobox = new JComboBox<T>();
		SwingComponents.modifyJComponent(combobox, name, tooltip, opaque,
				bgColor, fgColor, borderColor, d);
		combobox.setEditable(editable);
		combobox.setBorder(border);
		if (listener != null)
			combobox.addItemListener(listener);
		return combobox;
	}

	public static JTextField makeTextField(String text, String name,
			String tooltip, boolean opaque, Color background,
			Color foreground, Color border, Dimension d,
			ActionListener listener, boolean editable)
	{
		JTextField textfield = new JTextField();
		SwingComponents.modifyJComponent(textfield, name, tooltip, opaque,
				background, foreground, border, d);
		textfield.setText(text);
		textfield.setEditable(editable);
		if (listener != null)
			textfield.addActionListener(listener);
		return textfield;
	}

	public static JPasswordField makeSecretTextField(String text,
			String name, String tooltip, boolean opaque,
			Color background, Color foreground, Color border,
			Dimension d, ActionListener listener, boolean editable)
	{
		JPasswordField textfield = new JPasswordField();
		SwingComponents.modifyJComponent(textfield, name, tooltip, opaque,
				background, foreground, border, d);
		textfield.setText(text);
		textfield.setEditable(editable);
		if (listener != null)
			textfield.addActionListener(listener);
		return textfield;
	}

	public static JTextArea makeTextArea(String text, String name,
			String tooltip, boolean opaque, Color background,
			Color foreground, Color border, Dimension d,
			ActionListener listener, boolean editable,
			int rows, int columns)
	{
		JTextArea jtf = new JTextArea(rows, columns);
		SwingComponents.modifyJComponent(jtf, name, tooltip, opaque,
				background, foreground, border, d);
		jtf.setEditable(editable);
		jtf.setLineWrap(true);
		jtf.setWrapStyleWord(true);
		jtf.setText(text);
		return jtf;
	}

	public static JSlider makeSlider(String text, String name,
			String tooltip, boolean opaque, Color background,
			Color foreground, Color border, Dimension d,
			ChangeListener listener, int lowerBound, int upperBound)
	{
		JSlider slider = new JSlider();
		SwingComponents.modifyJComponent(slider, name, tooltip, opaque,
				background, foreground, border, d);
		slider = new JSlider(JSlider.HORIZONTAL,
				lowerBound, upperBound, (lowerBound + upperBound) / 2);
		if (listener != null)
			slider.addChangeListener(listener);
		slider.setMajorTickSpacing((lowerBound + upperBound) / 2);
		slider.setMinorTickSpacing((lowerBound + upperBound) / 10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		return slider;
	}

	/**
	 * Applies the supplied values to the supplied {@code JComponent}.
	 * 
	 * @param jc The component to modify.
	 * @param nameSet The name to set for the object.
	 * @param tooltip The component's tooltip.
	 * @param opaque whether or not the component should be opaque.
	 * @param background The background color.
	 * @param foreground The foreground color.
	 * @param border The border color.
	 * @param d The preferred dimension for the component.
	 */
	public static void modifyJComponent(JComponent jc, String nameSet,
			String tooltip, boolean opaque, Color background,
			Color foreground, Color border, Dimension d)
	{
		jc.setName(nameSet);
		jc.setToolTipText(tooltip);
		jc.setOpaque(opaque);
		if (background != null)
			jc.setBackground(background);
		if (foreground != null)
			jc.setForeground(foreground);
		if (border != null)
			jc.setBorder(new LineBorder(border));
		if (d != null)
			jc.setPreferredSize(d);
		jc.setFont(GUI_UserInterface.FONT);
	}

	/**
	 * Applied the supplied values to the supplied
	 * {@code AbstractButton}.
	 * 
	 * @param button The button to modify.
	 * @param buttonText The text to put on the button.
	 * @param name The name of the button. Used for determining
	 * 		which button is clicked in the action listener.
	 * @param tooltip The tooltip text.
	 * @param opaque Whether or not the button should be opaque.
	 * @param background The background color.
	 * @param foreground The foreground color.
	 * @param border The border color.
	 * @param d The preferred dimension of the button.
	 */
	public static void modifyButton(AbstractButton button,
			String buttonText, String name, String tooltip,
			boolean opaque, Color background, Color foreground,
			Color border, Dimension d)
	{
			modifyJComponent(button, name, tooltip, opaque,
					background, foreground, border, d);
			buttonText = String.format("<html>%s</html>",
					buttonText.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
			button.setText(buttonText);
	}

}
