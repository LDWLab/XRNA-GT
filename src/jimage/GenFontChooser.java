
package jimage;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

public class
GenFontChooser
extends FontChooser
{

public
GenFontChooser(JPanel parent)
{
	super(parent);
}

public void
makeFontAttributesPanel()
{
	setSize(686, 260);

	fontAttributesPanel = new JPanel();
	fontAttributesPanel.setLayout(new BorderLayout(0, 0));

	if (systemFontList == null)
	{
		systemFontList = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getAllFonts();

		fontList = new Font[systemFontList.length];

		for (int i = 0;i < systemFontList.length;i++)
			fontList[i] = systemFontList[i];
	}

	/*
	String[] fontNameList = new String[fontList.length + 12];
	
	fontNameList[0] = "Helvetica";
	fontNameList[1] = "Helvetica Bold";
	fontNameList[2] = "Helvetica Italic";
	fontNameList[3] = "Courier";
	fontNameList[4] = "Courier Bold";
	fontNameList[5] = "Courier Italic";
	fontNameList[6] = "TimesRoman";
	fontNameList[7] = "TimesRoman Bold";
	fontNameList[8] = "TimesRoman Italic";
	fontNameList[9] = "Dialog";
	fontNameList[10] = "Dialog Bold";
	fontNameList[11] = "Dialog Italic";
	for (int i = 0;i < fontList.length;i++)
		// fontNameList[12 + i] = fontList[i].getFontName();
		fontNameList[12 + i] = fontList[i].getName();
	*/

	fontNameCB = new JComboBox(fontList);
	fontNameCB.setSelectedIndex(0);

	fontAttributesPanel.add(fontNameCB, BorderLayout.NORTH);

	fontStylePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	fontStylePanel.setBorder(new TitledBorder(new BevelBorder(1),
		"Select Font Style:"));

	ButtonGroup styleBtGroup = new ButtonGroup();

	fontPlainBt = new JRadioButton("Plain");
	fontPlainBt.setSelected(false);
	fontPlainBt.addActionListener(new StyleBtActionListener());
	styleBtGroup.add(fontPlainBt);

	fontBoldBt = new JRadioButton("Bold");
	fontBoldBt.setSelected(false);
	fontBoldBt.addActionListener(new StyleBtActionListener());
	styleBtGroup.add(fontBoldBt);

	fontItalicBt = new JRadioButton("Italic");
	fontItalicBt.setSelected(false);
	fontItalicBt.addActionListener(new StyleBtActionListener());
	styleBtGroup.add(fontItalicBt);

	fontBoldItalicBt = new JRadioButton("Bold-Italic");
	fontBoldItalicBt.setSelected(false);
	fontBoldItalicBt.addActionListener(new StyleBtActionListener());
	styleBtGroup.add(fontBoldItalicBt);

	fontStylePanel.add(fontPlainBt);
	fontStylePanel.add(fontBoldBt);
	fontStylePanel.add(fontItalicBt);
	fontStylePanel.add(fontBoldItalicBt);

	fontAttributesPanel.add(fontStylePanel, BorderLayout.CENTER);

	fontTestTF = new JTextField("Test");
	fontTestTF.setFont(getCurrentFont());
	/*
	fontTestTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent e)
		{
			debug("fontTestTF.FONT: " + fontTestTF.getFont());
		}
	});
	*/
	fontTestTF.setForeground(Color.black);
	fontTestTF.setBackground(Color.white);
	fontTestTF.setVisible(true);
	fontAttributesPanel.add(fontTestTF, BorderLayout.SOUTH);
}

static int i = 0;

class StyleBtActionListener
implements ActionListener
{
	public void
	actionPerformed(ActionEvent e)
	{
		if (fontTestTF != null)
		{
			fontTestTF.setFont(getCurrentFont());
			fontTestTF.setText(fontTestTF.getText());
			// fontTestTF.setText(fontTestTF.getText() + " T" + i++);
			// debug("fontTestTF.text now: " + fontTestTF.getText());
			// fontTestTF.repaint();
		}
	}
}

private static void
debug(String s)
{
	System.out.println("GenFontChooser-> " + s);
}

}

