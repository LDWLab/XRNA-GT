package jimage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;
import javax.swing.plaf.basic.BasicBorders.MarginBorder;
import javax.swing.plaf.basic.*;

public class
HexColorChooserPanel
extends AbstractColorChooserPanel
{

private BasicArrowButton incRedUpBt = null;
private BasicArrowButton incRedDownBt = null;
private BasicArrowButton incGreenUpBt = null;
private BasicArrowButton incGreenDownBt = null;
private BasicArrowButton incBlueUpBt = null;
private BasicArrowButton incBlueDownBt = null;

private JComboBox hexLeastRedCB = null;
private JComboBox hexMostRedCB = null;
private JComboBox hexLeastGreenCB = null;
private JComboBox hexMostGreenCB = null;
private JComboBox hexLeastBlueCB = null;
private JComboBox hexMostBlueCB = null;
private JTextField intRedTF = null;
private JTextField intGreenTF = null;
private JTextField intBlueTF = null;
private JTextField intRGBTF = null;
private JTextField hexRGBTF = null;
private JPanel intColorPanel = null;

private JPanel rColorPanel = null;
private JPanel gColorPanel = null;
private JPanel bColorPanel = null;

private BevelBorder border = null;

private Font colorChooserFont = null;

private Color guiBGColor = Color.white;

public boolean resetGuiValue = true;

public
HexColorChooserPanel()
{
	super();

	colorChooserFont = new Font("Dialog", Font.BOLD, 12);

	/*
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	
	String[] fontList = ge.getAvailableFontFamilyNames() ;
	StringBuffer strBuf = new StringBuffer();
	for (int i = 0;i < fontList.length;i++)
		strBuf.append(fontList[i] + "\n");
	debug("FONTS: " + strBuf.toString());
	*/

	/*
	Font[] fontList = ge.getAllFonts();
	StringBuffer strBuf = new StringBuffer();
	for (int i = 0;i < fontList.length;i++)
		strBuf.append(fontList[i].toString() + "\n");
	debug("FONTS: " + strBuf.toString());
	*/
}
	
public void
buildChooser()
{
	border = new BevelBorder(1);
	setLayout(new BorderLayout());
	setBorder(new BevelBorder(BevelBorder.LOWERED));

	setCurrentColor(getColorSelectionModel().getSelectedColor());

	JPanel mainPanel = new JPanel(new GridLayout(8, 1));

	JPanel incBtPanel = new JPanel(
		new FlowLayout(FlowLayout.CENTER, 10, 0));
	incBtPanel.setBorder(new TitledBorder(border, "inc R,G,B:"));

	JPanel hexCBPanel = new JPanel(
		new FlowLayout(FlowLayout.CENTER, 2, 0));
	hexCBPanel.setBorder(new TitledBorder(border, "select R,G,B:"));

	JPanel hexStringTFPanel = new JPanel(
		new FlowLayout(FlowLayout.CENTER, 2, 0));
	hexStringTFPanel.setBorder(new TitledBorder(border, "edit hex string:"));

	JPanel rgbTFPanel = new JPanel(
		new FlowLayout(FlowLayout.CENTER, 2, 0));
	rgbTFPanel.setBorder(new TitledBorder(border, "edit R,G,B string:"));

	JPanel rgbColorPanel = new JPanel(
		new FlowLayout(FlowLayout.CENTER, 2, 0));
	rgbColorPanel.setBackground(this.getBackground());
	rgbColorPanel.setSize(120, 40);
	rgbColorPanel.setBorder(new TitledBorder(border, "R,G,B colors:"));

	JPanel intTFPanel = new JPanel(
		new FlowLayout(FlowLayout.CENTER, 2, 0));
	intTFPanel.setBorder(new TitledBorder(border, "edit Color int string:"));

	intColorPanel = new JPanel();
	intColorPanel.setBorder(new TitledBorder(border, "Current Color:"));
	
	rColorPanel = new JPanel();
	gColorPanel = new JPanel();
	bColorPanel = new JPanel();

	rColorPanel.add(new JLabel("        "));
	gColorPanel.add(new JLabel("        "));
	bColorPanel.add(new JLabel("        "));

	incRedUpBt = new BasicArrowButton(SwingConstants.NORTH);
	incRedUpBt.setActionCommand("Inc Up");
	incRedUpBt.setFont(colorChooserFont);
	incRedUpBt.setForeground(Color.black);
	incRedUpBt.setBackground(guiBGColor);
	incBtPanel.add(incRedUpBt);
	incRedUpBt.setVisible(true);
	incRedUpBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (getCurrentColor().getRed() < 0xff)
				{
					setCurrentColor(new Color(
						getCurrentColor().getRed() + 1,
						getCurrentColor().getGreen(),
						getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				debug("Exception in incRedUpBt.actionPerformed:" + e.toString());
			}
		}
	});

	incRedDownBt = new BasicArrowButton(SwingConstants.SOUTH);
	incRedDownBt.setActionCommand("Inc Down");
	incRedDownBt.setFont(colorChooserFont);
	incRedDownBt.setForeground(Color.black);
	incRedDownBt.setBackground(guiBGColor);
	incBtPanel.add(incRedDownBt);
	incRedDownBt.setVisible(true);
	incRedDownBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (getCurrentColor().getRed() > 0x00)
				{
					setCurrentColor(new Color(
						getCurrentColor().getRed() - 1,
						getCurrentColor().getGreen(),
						getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				debug("Exception in incRedDownBt.actionPerformed:" + e.toString());
			}
		}
	});

	incGreenUpBt = new BasicArrowButton(SwingConstants.NORTH);
	incGreenUpBt.setActionCommand("Inc Up");
	incGreenUpBt.setFont(colorChooserFont);
	incGreenUpBt.setForeground(Color.black);
	incGreenUpBt.setBackground(guiBGColor);
	incBtPanel.add(incGreenUpBt);
	incGreenUpBt.setVisible(true);
	incGreenUpBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (getCurrentColor().getGreen() < 0xff)
				{
					setCurrentColor(new Color(
						getCurrentColor().getRed(),
						getCurrentColor().getGreen() + 1,
						getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				debug("Exception in incGreenUpBt.actionPerformed:" + e.toString());
			}
		}
	});


	incGreenDownBt = new BasicArrowButton(SwingConstants.SOUTH);
	incGreenDownBt.setActionCommand("Inc Up");
	incGreenDownBt.setFont(colorChooserFont);
	incGreenDownBt.setForeground(Color.black);
	incGreenDownBt.setBackground(guiBGColor);
	incBtPanel.add(incGreenDownBt);
	incGreenDownBt.setVisible(true);
	incGreenDownBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (getCurrentColor().getGreen() > 0x00)
				{
					setCurrentColor(new Color(
						getCurrentColor().getRed(),
						getCurrentColor().getGreen() - 1,
						getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				debug("Exception in incGreenDownBt.actionPerformed:" + e.toString());
			}
		}
	});

	incBlueUpBt = new BasicArrowButton(SwingConstants.NORTH);
	incBlueUpBt.setActionCommand("Inc Up");
	incBlueUpBt.setFont(colorChooserFont);
	incBlueUpBt.setForeground(Color.black);
	incBlueUpBt.setBackground(guiBGColor);
	incBtPanel.add(incBlueUpBt);
	incBlueUpBt.setVisible(true);
	incBlueUpBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (getCurrentColor().getBlue() < 0xff)
				{
					setCurrentColor(new Color(
						getCurrentColor().getRed(),
						getCurrentColor().getGreen(),
						getCurrentColor().getBlue() + 1));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				debug("Exception in incBlueUpBt.actionPerformed:" + e.toString());
			}
		}
	});

	incBlueDownBt = new BasicArrowButton(SwingConstants.SOUTH);
	incBlueDownBt.setActionCommand("Inc Up");
	incBlueDownBt.setFont(colorChooserFont);
	incBlueDownBt.setForeground(Color.black);
	incBlueDownBt.setBackground(guiBGColor);
	incBtPanel.add(incBlueDownBt);
	incBlueDownBt.setVisible(true);
	incBlueDownBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (getCurrentColor().getBlue() > 0x00)
				{
					setCurrentColor(new Color(
						getCurrentColor().getRed(),
						getCurrentColor().getGreen(),
						getCurrentColor().getBlue() - 1));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				debug("Exception in incBlueDownBt.actionPerformed:" + e.toString());
			}
		}
	});

	hexMostRedCB = new JComboBox(new String[] {"0", "1", "2", "3", "4",
		"5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"});
	hexMostRedCB.setSelectedIndex(0);
	hexMostRedCB.setActionCommand("hex least red");
	hexMostRedCB.setFont(colorChooserFont);
	hexMostRedCB.setForeground(Color.black);
	hexMostRedCB.setBackground(guiBGColor);
	hexMostRedCB.setVisible(true);
	hexMostRedCB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(((String)hexMostRedCB.
						getSelectedItem()).trim(), 16);
					setCurrentColor(new Color(
						(val << 4) + (getCurrentColor().getRed() & 0x0f),
						getCurrentColor().getGreen(),
						getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in hexMostRedCB.actionPerformed:"
					+ e.toString());
			}
		}
	});
	hexCBPanel.add(hexMostRedCB);

	hexLeastRedCB = new JComboBox(new String[] {"0", "1", "2", "3", "4",
		"5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"});
	hexLeastRedCB.setSelectedIndex(0);
	hexLeastRedCB.setActionCommand("hex least red");
	hexLeastRedCB.setFont(colorChooserFont);
	hexLeastRedCB.setForeground(Color.black);
	hexLeastRedCB.setBackground(guiBGColor);
	hexLeastRedCB.setVisible(true);
	hexLeastRedCB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(((String)hexLeastRedCB.
						getSelectedItem()).trim(), 16);
					setCurrentColor(new Color(
						(getCurrentColor().getRed() & 0xf0) + val,
						getCurrentColor().getGreen(),
						getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in hexLeastRedCB.actionPerformed:"
					+ e.toString());
			}
		}
	});
	hexCBPanel.add(hexLeastRedCB);

	hexMostGreenCB = new JComboBox(new String[] {"0", "1", "2", "3", "4",
		"5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"});
	hexMostGreenCB.setSelectedIndex(0);
	hexMostGreenCB.setActionCommand("hex least red");
	hexMostGreenCB.setFont(colorChooserFont);
	hexMostGreenCB.setForeground(Color.black);
	hexMostGreenCB.setBackground(guiBGColor);
	hexMostGreenCB.setVisible(true);
	hexMostGreenCB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(((String)hexMostGreenCB.
						getSelectedItem()).trim(), 16);
					setCurrentColor(new Color(
						getCurrentColor().getRed(),
						(val << 4) + (getCurrentColor().getGreen() & 0x0f),
						getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in hexMostGreenCB.actionPerformed:"
					+ e.toString());
			}
		}
	});
	hexCBPanel.add(hexMostGreenCB);

	hexLeastGreenCB = new JComboBox(new String[] {"0", "1", "2", "3", "4",
		"5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"});
	hexLeastGreenCB.setSelectedIndex(0);
	hexLeastGreenCB.setActionCommand("hex least red");
	hexLeastGreenCB.setFont(colorChooserFont);
	hexLeastGreenCB.setForeground(Color.black);
	hexLeastGreenCB.setBackground(guiBGColor);
	hexLeastGreenCB.setVisible(true);
	hexLeastGreenCB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(((String)hexLeastGreenCB.
						getSelectedItem()).trim(), 16);
					setCurrentColor(new Color(
						getCurrentColor().getRed(),
						(getCurrentColor().getGreen() & 0xf0) + val,
						getCurrentColor().getBlue()));

					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in hexLeastGreenCB.actionPerformed:"
					+ e.toString());
			}
		}
	});
	hexCBPanel.add(hexLeastGreenCB);

	hexMostBlueCB = new JComboBox(new String[] {"0", "1", "2", "3", "4",
		"5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"});
	hexMostBlueCB.setSelectedIndex(0);
	hexMostBlueCB.setActionCommand("hex least red");
	hexMostBlueCB.setFont(colorChooserFont);
	hexMostBlueCB.setForeground(Color.black);
	hexMostBlueCB.setBackground(guiBGColor);
	hexMostBlueCB.setVisible(true);
	hexMostBlueCB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(((String)hexMostBlueCB.
						getSelectedItem()).trim(), 16);
					setCurrentColor(new Color(
						getCurrentColor().getRed(),
						getCurrentColor().getGreen(),
						(val << 4) + (getCurrentColor().getBlue() & 0x0f)));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in hexMostBlueCB.actionPerformed:"
					+ e.toString());
			}
		}
	});
	hexCBPanel.add(hexMostBlueCB);

	hexLeastBlueCB = new JComboBox(new String[] {"0", "1", "2", "3", "4",
		"5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"});
	hexLeastBlueCB.setSelectedIndex(0);
	hexLeastBlueCB.setActionCommand("hex least red");
	hexLeastBlueCB.setFont(colorChooserFont);
	hexLeastBlueCB.setForeground(Color.black);
	hexLeastBlueCB.setBackground(guiBGColor);
	hexLeastBlueCB.setVisible(true);
	hexLeastBlueCB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(((String)hexLeastBlueCB.
						getSelectedItem()).trim(), 16);
					setCurrentColor(new Color(
						getCurrentColor().getRed(),
						getCurrentColor().getGreen(),
						(getCurrentColor().getBlue() & 0xf0) + val));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in hexLeastBlueCB.actionPerformed:"
					+ e.toString());
			}
		}
	});
	hexCBPanel.add(hexLeastBlueCB);

	intRedTF = new JTextField(" ", 3);
	intRedTF.setForeground(Color.black);
	intRedTF.setBackground(guiBGColor);
	intRedTF.setFont(colorChooserFont);
	intRedTF.setVisible(true);
	intRedTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(intRedTF.getText().trim());
					if (val > 255)
						val = 255;
					setCurrentColor(new Color(val, getCurrentColor().getGreen(),
						getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in intRedTF.actionPerformed:" + e.toString());
			}
		}
	});
	rgbTFPanel.add(intRedTF);

	intGreenTF = new JTextField(" ", 3);
	intGreenTF.setForeground(Color.black);
	intGreenTF.setBackground(guiBGColor);
	intGreenTF.setFont(colorChooserFont);
	intGreenTF.setVisible(true);
	intGreenTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(intGreenTF.getText().trim());
					if (val > 255)
						val = 255;
					setCurrentColor(new Color(getCurrentColor().getRed(), val,
						getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in intGreenTF.actionPerformed:" + e.toString());
			}
		}
	});
	rgbTFPanel.add(intGreenTF);

	intBlueTF = new JTextField(" ", 3);
	intBlueTF.setForeground(Color.black);
	intBlueTF.setBackground(guiBGColor);
	intBlueTF.setFont(colorChooserFont);
	intBlueTF.setVisible(true);
	intBlueTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(intBlueTF.getText().trim());
					if (val > 255)
						val = 255;
					setCurrentColor(new Color(getCurrentColor().getRed(),
						getCurrentColor().getGreen(), val));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in intBlueTF.actionPerformed:" + e.toString());
			}
		}
	});
	rgbTFPanel.add(intBlueTF);

	hexRGBTF = new JTextField(" ", 6);
	hexRGBTF.setForeground(Color.black);
	hexRGBTF.setBackground(guiBGColor);
	hexRGBTF.setFont(colorChooserFont);
	hexRGBTF.setVisible(true);
	hexRGBTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					String hexString = hexRGBTF.getText().trim();
					if (hexString.startsWith("0x") || hexString.startsWith("0X"))
						hexString = hexString.substring(2);
					int val = Integer.parseInt(hexString, 16);
					// int val = Integer.parseInt(hexRGBTF.getText().trim().substring(2), 16);
					if (val > 0xffffff)
						val = 0xffffff;
					setCurrentColor(new Color(val));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in hexRGBTF.actionPerformed:" + e.toString());
			}
		}
	});
	hexStringTFPanel.add(hexRGBTF);

	intRGBTF = new JTextField(" ", 7);
	intRGBTF.setForeground(Color.black);
	intRGBTF.setBackground(guiBGColor);
	intRGBTF.setFont(colorChooserFont);
	intRGBTF.setVisible(true);
	intRGBTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				if (resetGuiValue)
				{
					int val = Integer.parseInt(intRGBTF.getText().trim());
					if (val > 0xffffff)
					{
						val = 0xffffff;
						intRGBTF.setText("16777215");
					}
					setCurrentColor(new Color(val));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception in intBlueTF.actionPerformed:" + e.toString());
			}
		}
	});
	intTFPanel.add(intRGBTF);

	intColorPanel.setBackground(this.getCurrentColor());
	
	rColorPanel.setBackground(new Color(this.getCurrentColor().getRed()));
	rColorPanel.setVisible(true);
	gColorPanel.setBackground(new Color(this.getCurrentColor().getGreen()));
	gColorPanel.setVisible(true);
	bColorPanel.setBackground(new Color(this.getCurrentColor().getBlue()));
	bColorPanel.setVisible(true);

	JPanel appPanel = new JPanel(new FlowLayout());
	JButton appComplementBt = new JButton("complement");
	appComplementBt.setText("complement");
	appComplementBt.setActionCommand("complement");
	appComplementBt.setFont(new Font("Dialog", Font.BOLD, 12));
	appComplementBt.setForeground(Color.black);
	appComplementBt.setBackground(Color.gray);
	appPanel.add(appComplementBt);
	appComplementBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				if (resetGuiValue)
				{
					setCurrentColor(new Color(
						0xff - getCurrentColor().getRed(),
						0xff - getCurrentColor().getGreen(),
						0xff - getCurrentColor().getBlue()));
					updateChooser();
				}
			}
			catch (Exception e)
			{
				debug("Exception in appComplementBt: " + e.toString());
			}
		}
	});

	rgbColorPanel.add(rColorPanel);
	rgbColorPanel.add(gColorPanel);
	rgbColorPanel.add(bColorPanel);

	incBtPanel.setVisible(true);
	hexCBPanel.setVisible(true);
	hexStringTFPanel.setVisible(true);
	rgbTFPanel.setVisible(true);
	rgbColorPanel.setVisible(true);
	intTFPanel.setVisible(true);
	intColorPanel.setVisible(true);
	appPanel.setVisible(true);

	mainPanel.add(incBtPanel);
	mainPanel.add(hexCBPanel);
	mainPanel.add(hexStringTFPanel);
	mainPanel.add(rgbTFPanel);
	mainPanel.add(intTFPanel);
	mainPanel.add(rgbColorPanel);
	mainPanel.add(intColorPanel);
	mainPanel.add(appPanel);
	add(BorderLayout.CENTER, mainPanel);
}

public String
getDisplayName()
{
	return "Hex Color Chooser";
}

public Icon
getSmallDisplayIcon()
{
	return (null);
}

public Icon
getLargeDisplayIcon()
{
	return (null);
}

public void
setCurrentColor(Color currentColor)
{
	getColorSelectionModel().setSelectedColor(currentColor);
}

public Color
getCurrentColor()
{
	return(getColorSelectionModel().getSelectedColor());
}

static int i = 0;

public void
updateChooser()
{
	// debug("IN UPDATE CHOOSER: " + (i++));

	resetGuiValue = false;

	hexMostRedCB.setSelectedIndex(
		(this.getCurrentColor().getRed() & 0xf0) >> 4);
	hexLeastRedCB.setSelectedIndex(
		this.getCurrentColor().getRed() & 0x0f);

	hexMostGreenCB.setSelectedIndex(
		(this.getCurrentColor().getGreen() & 0xf0) >> 4);
	hexLeastGreenCB.setSelectedIndex(
		this.getCurrentColor().getGreen() & 0x0f);

	hexMostBlueCB.setSelectedIndex(
		(this.getCurrentColor().getBlue() & 0xf0) >> 4);
	hexLeastBlueCB.setSelectedIndex(
		this.getCurrentColor().getBlue() & 0x0f);

	intRedTF.setText(" " + this.getCurrentColor().getRed());
	intGreenTF.setText(" " + this.getCurrentColor().getGreen());
	intBlueTF.setText(" " + this.getCurrentColor().getBlue());

	// not concerned with alpha right now
	hexRGBTF.setText(" 0x" + Integer.toHexString((
		this.getCurrentColor().getRGB() & 0x00ffffff)));
	intRGBTF.setText("  " + (this.getCurrentColor().getRGB() & 0x00ffffff));

	intColorPanel.setBackground(this.getCurrentColor());
	
	rColorPanel.setBackground(new Color(
		this.getCurrentColor().getRed(), 0x00, 0x00));
	gColorPanel.setBackground(new Color(
		0x00, this.getCurrentColor().getGreen(), 0x00));
	bColorPanel.setBackground(new Color(
		0x00, 0x00, this.getCurrentColor().getBlue()));

	getColorSelectionModel().setSelectedColor(this.getCurrentColor());

	resetGuiValue = true;
}

private static void
debug(String s)
{
	System.out.println(s);
}
	
}
