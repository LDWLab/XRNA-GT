package ssview;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import jimage.FontChooser;

public class
NucNodeFontChooser
extends FontChooser
{

public
NucNodeFontChooser(JPanel parent)
{
	super(parent);
}

public void
makeFontAttributesPanel()
{
	fontAttributesPanel = new JPanel();
	fontAttributesPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

	fontNameCB = new JComboBox(new String[] {"TimesRoman", "Helvetica", "Courier", "Dialog"});
	fontNameCB.setSelectedIndex(1);
	StyleConstants.setFontFamily(attributes,
		(String)fontNameCB.getSelectedItem());
	fontNameCB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent e)
		{
			if (!StyleConstants.getFontFamily(attributes).equals(
				fontNameCB.getSelectedItem()))
			{
				StyleConstants.setFontFamily(attributes, 
				   (String)fontNameCB.getSelectedItem());
				updateFont();
			}
		}
	});

	fontBoldBt = new JRadioButton("Bold");
	fontBoldBt.setSelected(true);
	StyleConstants.setBold(attributes, true);
	fontBoldBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent e)
		{
			StyleConstants.setBold(attributes,
				fontBoldBt.isSelected());
			updateFont();
		}
	});

	fontItalicBt = new JRadioButton("Italic");
	StyleConstants.setItalic(attributes, false);
	fontItalicBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent e)
		{
			StyleConstants.setItalic(attributes,
				fontItalicBt.isSelected());
			updateFont();
		}
	});

	fontAttributesPanel.add(fontNameCB);
	fontAttributesPanel.add(fontBoldBt);
	fontAttributesPanel.add(fontItalicBt);
}

public void
updateFont()
{
	String name = StyleConstants.getFontFamily(attributes);
	boolean bold = StyleConstants.isBold(attributes);
	boolean ital = StyleConstants.isItalic(attributes);
	int size = StyleConstants.getFontSize(attributes);

	//Bold and italic don't work properly in beta 4.
	setCurrentFont(new Font(name, (bold ? Font.BOLD : 0) + (ital ? Font.ITALIC : 0), size));
}


}

