
package jimage;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class
FontChooser
extends JDialog
implements PropertyChangeListener
{

static public Font[] systemFontList = null;
static public Font[] fontList = null;

public JPanel fontCenterPanel = null;
public JPanel fontStylePanel = null;
public JPanel fontExtraBtsPanel = null;

public JTextField fontTestTF = null;

public JComboBox fontNameCB = null;

public JRadioButton fontPlainBt = null;
public JRadioButton fontBoldBt = null;
public JRadioButton fontItalicBt = null;
public JRadioButton fontBoldItalicBt = null;

public JRadioButton font5Size = new JRadioButton();
public JRadioButton font6Size = new JRadioButton();
public JRadioButton font7Size = new JRadioButton();
public JRadioButton font8Size = new JRadioButton();
public JRadioButton font9Size = new JRadioButton();
public JRadioButton font10Size = new JRadioButton();
public JRadioButton font11Size = new JRadioButton();
public JRadioButton font12Size = new JRadioButton();
public JRadioButton font13Size = new JRadioButton();
public JRadioButton font14Size = new JRadioButton();
public JRadioButton font15Size = new JRadioButton();
public JRadioButton font16Size = new JRadioButton();
public JRadioButton font17Size = new JRadioButton();
public JRadioButton font18Size = new JRadioButton();
public JRadioButton font19Size = new JRadioButton();
public JRadioButton font20Size = new JRadioButton();
public JRadioButton font21Size = new JRadioButton();
public JRadioButton font22Size = new JRadioButton();
public JRadioButton font23Size = new JRadioButton();
public JRadioButton font24Size = new JRadioButton();
public JRadioButton font25Size = new JRadioButton();
public JRadioButton font26Size = new JRadioButton();
public JRadioButton font27Size = new JRadioButton();
public JRadioButton font28Size = new JRadioButton();
public JRadioButton font29Size = new JRadioButton();
public JRadioButton font30Size = new JRadioButton();
public JRadioButton font31Size = new JRadioButton();
public JRadioButton font32Size = new JRadioButton();

public Vector fontSizeList = null;

public SimpleAttributeSet attributes = null;
public JPanel fontChooserParent = null;
public JPanel fontAttributesPanel = null;

public
FontChooser(JPanel parent)
{
	// super(parent, "Font Chooser", false);
	super();
	this.setModal(true);

	fontChooserParent = parent;

	buildGui();
}

private void
buildGui()
{
// debug("IN FONTCHOOSER BUILDGUI");
	fontSizeList = new Vector();
	fontSizeList.add(font5Size);
	fontSizeList.add(font6Size);
	fontSizeList.add(font7Size);
	fontSizeList.add(font8Size);
	fontSizeList.add(font9Size);
	fontSizeList.add(font10Size);
	fontSizeList.add(font11Size);
	fontSizeList.add(font12Size);
	fontSizeList.add(font13Size);
	fontSizeList.add(font14Size);
	fontSizeList.add(font15Size);
	fontSizeList.add(font16Size);
	fontSizeList.add(font17Size);
	fontSizeList.add(font18Size);
	fontSizeList.add(font19Size);
	fontSizeList.add(font20Size);
	fontSizeList.add(font21Size);
	fontSizeList.add(font22Size);
	fontSizeList.add(font23Size);
	fontSizeList.add(font24Size);
	fontSizeList.add(font25Size);
	fontSizeList.add(font26Size);
	fontSizeList.add(font27Size);
	fontSizeList.add(font28Size);
	fontSizeList.add(font29Size);
	fontSizeList.add(font30Size);
	fontSizeList.add(font31Size);
	fontSizeList.add(font32Size);

	setSize(300, 200);
	setLocationRelativeTo(fontChooserParent);
	toFront();
	attributes = new SimpleAttributeSet();

	// make sure that any way they cancel the window does the right thing

	// start setting up interface
	Container parentContainer = getContentPane();
	parentContainer.setLayout(new BorderLayout());

	makeFontAttributesPanel();

	JPanel fontSizePanel = new JPanel();
	fontSizePanel.setLayout(new GridLayout(7, 4));
	fontSizePanel.setBorder(new TitledBorder(new BevelBorder(1),
		"Select Font Size:"));

	ActionListener fontSizeListener = new FontSizeActionListener();

	ButtonGroup sizeBtGroup = new ButtonGroup();
	int btSize = 5;
	for (Enumeration e = fontSizeList.elements();e.hasMoreElements();)
	{
		JRadioButton tmpBt = (JRadioButton)e.nextElement();
		fontSizePanel.add(tmpBt);
		tmpBt.setSelected(false);
		String tmpInt = String.valueOf(btSize);
		tmpBt.setText(tmpInt);
		tmpBt.setActionCommand(tmpInt);
		tmpBt.setFont(new Font("Dialog", Font.BOLD, 12));
		tmpBt.addActionListener(fontSizeListener);
		sizeBtGroup.add(tmpBt);
		btSize++;
	}
	font14Size.setSelected(true);

	parentContainer.add(fontAttributesPanel, BorderLayout.NORTH);
	parentContainer.add(fontSizePanel, BorderLayout.CENTER);

	// updates get reflected in our preview label.
	// renderPreviewPanel();

	addWindowListener(new WindowAdapter()
	{
		public void
		windowClosing(WindowEvent e)
		{
			closeAndCancel();
		}
	});
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


		fontList = new Font[systemFontList.length + 5];

		fontList[0] = new Font("Dialog", Font.PLAIN, 1);
		fontList[1] = new Font("DialogInput", Font.PLAIN, 1);
		fontList[2] = new Font("Monospaced", Font.PLAIN, 1);
		fontList[3] = new Font("Serif", Font.PLAIN, 1);
		fontList[4] = new Font("SansSerif", Font.PLAIN, 1);
		for (int i = 5;i < fontList.length;i++)
			fontList[i] = systemFontList[i - 5];
	}

	String[] fontNameList = new String[fontList.length];
	fontNameList[0] = "Dialog";
	fontNameList[1] = "DialogInput";
	fontNameList[2] = "Courier";
	fontNameList[3] = "TimesRoman";
	fontNameList[4] = "Helvetica";
	for (int i = 5;i < fontList.length;i++)
		fontNameList[i] = new String(fontList[i].getFamily() + " : " +
			fontList[i].getName() + " : " + fontList[i].getFontName());
	fontNameCB = new JComboBox(fontNameList);
	fontNameCB.setSelectedIndex(0);
	fontNameCB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			fontTestTF.setFont(getCurrentFont());
		}
	});

	fontAttributesPanel.add(fontNameCB, BorderLayout.NORTH);

	fontStylePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	fontStylePanel.setBorder(new TitledBorder(new BevelBorder(1),
		"Select Font Style:"));

	fontExtraBtsPanel = buildFontExtraBtsPanel();

	ButtonGroup styleBtGroup = new ButtonGroup();
	ActionListener styleActionListener = new StyleBtActionListener();

	fontPlainBt = new JRadioButton("Plain");
	fontPlainBt.setSelected(false);
	fontPlainBt.addActionListener(styleActionListener);
	styleBtGroup.add(fontPlainBt);

	fontItalicBt = new JRadioButton("Italic");
	fontItalicBt.setSelected(false);
	fontItalicBt.addActionListener(styleActionListener);
	styleBtGroup.add(fontItalicBt);

	fontBoldItalicBt = new JRadioButton("Bold-Italic");
	fontBoldItalicBt.setSelected(false);
	fontBoldItalicBt.addActionListener(styleActionListener);
	styleBtGroup.add(fontBoldItalicBt);

	fontStylePanel.add(fontPlainBt);
/* fontStylePanel.add(fontBoldBt); */
	fontStylePanel.add(fontItalicBt);
	fontStylePanel.add(fontBoldItalicBt);

	fontCenterPanel = new JPanel(new FlowLayout());

	fontCenterPanel.add(fontStylePanel);
	fontCenterPanel.add(fontExtraBtsPanel);

	fontAttributesPanel.add(fontCenterPanel, BorderLayout.CENTER);

	fontTestTF = new JTextField("ABCD abcd 0123456789");
	fontTestTF.setFont(getCurrentFont());
	fontTestTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent e)
		{
			debug("fontTestTF.FONT: " + fontTestTF.getFont());
		}
	});
	fontTestTF.setForeground(Color.black);
	fontTestTF.setBackground(Color.white);
	fontTestTF.setVisible(true);
	fontAttributesPanel.add(fontTestTF, BorderLayout.SOUTH);
}

public JPanel
buildFontExtraBtsPanel()
{
	JPanel tmpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	tmpPanel.setBorder(new TitledBorder(new BevelBorder(1),
		"Commands:"));
	tmpPanel.add(new JButton("test"));
	return (tmpPanel);
}

static int j = 0;

class StyleBtActionListener
implements ActionListener
{
	public void
	actionPerformed(ActionEvent e)
	{
		if (fontTestTF != null)
		{
			fontTestTF.setFont(getCurrentFont());
			// debug("fontTestTF.text now: " + fontTestTF.getText());
			// fontTestTF.repaint();
		}
	}
}

static int i = 0;

class FontSizeActionListener
implements ActionListener
{
	public void
	actionPerformed(ActionEvent ae)
	{
		JRadioButton sizeBt = null;
		for (Enumeration e = fontSizeList.elements();e.hasMoreElements();)
		{
			JRadioButton tmpBt = (JRadioButton)e.nextElement();
			if (tmpBt.isSelected())
			{
				sizeBt = tmpBt;
				break;
			}
		}

		try
		{
			if (sizeBt != null)
			{
				// int val = Integer.parseInt((String)sizeBt.getActionCommand());
				fontTestTF.setFont(getCurrentFont());
			}

		}
		catch (NumberFormatException e)
		{
			System.out.println("IN NUMBERFORMAT EXCEPTION in fontSize: " + e.toString());
			// keep last angle and don't do anything
		}
		catch (Exception e)
		{
			System.out.println("Exception in fontSize.actionPerformed");
			return;
		}
	}
}

public void
resetFontSize(int size)
{
	int btSize = 5;
	for (Enumeration e = fontSizeList.elements();e.hasMoreElements();)
	{
		JRadioButton tmpBt = (JRadioButton)e.nextElement();
		if (btSize == size)
			tmpBt.setSelected(true);
		else
			tmpBt.setSelected(false);
		btSize++;
	}
}

public int
getFontSize()
{
	JRadioButton tmpBt = null;
	for (Enumeration e = fontSizeList.elements();e.hasMoreElements();)
	{
		tmpBt = (JRadioButton)e.nextElement();
		if (tmpBt.isSelected())
			break;
	}
	if ((tmpBt == null) || (tmpBt.getActionCommand() == null) ||
		(tmpBt.getActionCommand().length() <= 0))
		return (24);
	return (Integer.parseInt(tmpBt.getActionCommand()));
}

public void
resetFontStyle(int style)
{
	if (fontPlainBt != null)
		fontPlainBt.setSelected(style == Font.PLAIN);
	if (fontBoldBt != null)
		fontBoldBt.setSelected(style == Font.BOLD);
	if (fontItalicBt != null)
		fontItalicBt.setSelected(style == Font.ITALIC);
	if (fontBoldItalicBt != null)
		fontBoldItalicBt.setSelected(style == (Font.BOLD + Font.ITALIC));
}

public int
getFontStyle()
{
	if (fontPlainBt.isSelected())
		return(Font.PLAIN);
	else if (fontBoldBt.isSelected())
		return(Font.BOLD);
	else if (fontItalicBt.isSelected())
		return(Font.ITALIC);
	else if (fontBoldItalicBt.isSelected())
		return(Font.BOLD + Font.ITALIC);
	return(Font.PLAIN);
}

private Font newFont = null;
private Font saveFont = null;

public void
setNewFont(Font newFont)
{
    this.newFont = newFont;
}

public Font
getNewFont()
{
    return (this.newFont);
}

private Font currentFont = null;

public void
setCurrentFont(Font currentFont)
{
    this.currentFont = currentFont;

/*
debug("Font NAME: " + currentFont.getName());
debug("Font FONTNAME: " + currentFont.getFontName());
debug("family NAME: " + currentFont.getFamily());
debug("style NAME: " + currentFont.getStyle());
*/

	/*
	fontNameList[0] = "Dialog";
	fontNameList[1] = "DialogInput";
	fontNameList[2] = "Courier";
	fontNameList[3] = "TimesRoman";
	fontNameList[4] = "Helvetica";
	for (int i = 5;i < fontList.length;i++)
		fontNameList[i] = new String(fontList[i].getFamily() + " : " +
			fontList[i].getName() + " : " + fontList[i].getFontName());
	fontNameCB = new JComboBox(fontNameList);
	fontNameCB.setSelectedIndex(0);
	*/

	if (currentFont.getName().toLowerCase().equals("dialog"))
		fontNameCB.setSelectedIndex(0);
	else if (currentFont.getName().toLowerCase().equals("dialoginput"))
		fontNameCB.setSelectedIndex(1);
	else if (currentFont.getName().toLowerCase().equals("courier"))
		fontNameCB.setSelectedIndex(2);
	else if (currentFont.getName().toLowerCase().equals("timesroman"))
		fontNameCB.setSelectedIndex(3);
	else if (currentFont.getName().toLowerCase().equals("helvetica"))
		fontNameCB.setSelectedIndex(4);

	this.resetFontSize(currentFont.getSize());
	this.resetFontStyle(currentFont.getStyle());
}

public Font
getCurrentFont()
{
	Font font = fontList[fontNameCB.getSelectedIndex()].deriveFont((float)getFontSize());
	String fontFamily = font.getFamily();

	if (fontPlainBt.isSelected())
		font = font.deriveFont(Font.PLAIN);
	/*else if (fontBoldBt.isSelected())
		font = font.deriveFont(Font.BOLD);*/
	else if (fontItalicBt.isSelected())
		font = font.deriveFont(Font.ITALIC);
	else if (fontBoldItalicBt.isSelected())
		font = font.deriveFont(Font.BOLD + Font.ITALIC);
	
	if (!fontFamily.equals(font.getFamily()))
	{
		// font = fontList[fontNameCB.getSelectedIndex()].deriveFont((float)getFontSize());
		// debug("NO GO: " + font);
	}
	// else debug("font: " + font);

	return (font);
}

public AttributeSet
getAttributes()
{
	return attributes;
}

/*
public void
show()
{
	super.setVisible(true);
	// saveFont = previewLabel.getFont();
}
*/

private DrawObject currentDrawObject = null;

public void
setCurrentDrawObject(DrawObject currentDrawObject)
{
	this.currentDrawObject = currentDrawObject;
	if (currentDrawObject == null)
	debug("IN FONT CURRENT DRWOBJ NULL");
	else
	debug("IN FONT CURRENT DRWOBJ: " + this.currentDrawObject.toString());
}

public DrawObject
getCurrentDrawObject()
{
	return (this.currentDrawObject);
}

public void
propertyChange(PropertyChangeEvent evt)
{
	String propertyName = evt.getPropertyName();

	if (propertyName == null)
		return;

	if (propertyName.equals("CurrendDrawObject"))
	{
		this.setCurrentDrawObject((DrawObject)evt.getNewValue());
	}
}

// Handle a click of the OK button
public void
closeAndSave()
{
	// setNewFont(previewLabel.getFont());
	setNewFont(null);

	// and then close the window
	setVisible(false);
}

// Handle a click of the Cancel button
public void
closeAndCancel()
{
	// erase any font information and then close the window
	setNewFont(saveFont);
	setVisible(false);
}

private static void
debug(String s)
{
	System.out.println("FontChooser-> " + s);
}

}
