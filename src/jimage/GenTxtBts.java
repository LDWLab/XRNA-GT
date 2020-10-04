package jimage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.GifEncoder;
import util.GraphicsUtil;

public class GenTxtBts
extends DrawObjectView
{

public JComboBox		scaleCB = null;
public JTextField		scaleTF = null;

protected JCheckBox         showMousePosCB = null;

private JScrollPane genImgScrollPane = null;
private BevelBorder border = null;

private JScrollPane	spSystemOut = null;
private JTextPane	taSystemOut = null;

private JPanel	controlPanel = null;
private JPanel	applyControlPanel = null;
private JPanel	transformControlPanel = null;
private JPanel	applyBtPanel = null;
// private Box	applyBtPanel = null;
private JScrollPane	applyBtScrollPane = null;
private JTextField btTextTF = null;
private JTextField widthTF = null;
private JTextField heightTF = null;
private JButton btTextBt = null;
private JButton applyDimensionBt = null;
private JButton autoShadeBt = null;
private JComboBox depthCBox = null;

private JRadioButton bgColorRB = null;
private JRadioButton bgBtColorRB = null;
private JRadioButton fgBtColorRB = null;
private JRadioButton topBtColorRB = null;
private JRadioButton bottomBtColorRB = null;
private JRadioButton selectFGBtColorRB = null;
private JRadioButton setColorChooserRB = null;

private JButton	printImgsBt = null;

private JCheckBox aliasOnChkBox = null;
private JCheckBox showFontsChkBox = null;

public
GenTxtBts(double scaleVal, JFrame parent)
{
	this.setFigureScale(scaleVal);
	this.setImgType(BufferedImage.TYPE_INT_RGB);
	frameParent = parent;

	try
	{
		setGuiStartX(400);
		setGuiStartY(30);
		setGuiWidth(800);
		setGuiHeight(600);

		this.setImgBtWidth(120);
		this.setImgBtHeight(30);

		setDrawImgBGColor(Color.white);

		buildGui();
	}
	catch(Exception e)
	{
		handleException("Exception from Constructor: ", e, 1);
	}
}

public void
initFromProperties(String fileName)
{
	super.initFromProperties(fileName);

	String val = null;

	val = this.getGenProperties().getProperty("btText");
	if (val != null)
		this.setBtText(val);

	val = this.getGenProperties().getProperty("fontSize");
	if (val != null)
		setBtFontSize(Integer.parseInt(val));

	val = this.getGenProperties().getProperty("fontStyle");
	if (val != null)
	{
		if (val.toUpperCase().equals("PLAIN"))
			setBtFontStyle(Font.PLAIN);
		else if (val.toUpperCase().equals("BOLD"))
			setBtFontStyle(Font.BOLD);
		else if (val.toUpperCase().equals("ITALIC"))
			setBtFontStyle(Font.ITALIC);
		else if (val.toUpperCase().equals("BOLDITALIC"))
			setBtFontStyle(Font.BOLD + Font.ITALIC);
	}

	val = this.getGenProperties().getProperty("fontFamily");
	if (val != null)
		setBtFontFamily(val);

	val = this.getGenProperties().getProperty("fontName");
	if (val != null)
		setBtFontName(val);

	val = this.getGenProperties().getProperty("fontFontName");
	if (val != null)
		setBtFontFontName(val);

/*
fontSize=20
fontStyle=ITALIC
fontFamily=Bookman Old Style
fontName=Bookman Old Style Cursiva
fontFontName=Bookman Old Style Italic
*/

	val = this.getGenProperties().getProperty("width");
	if (val != null)
		this.setImgBtWidth(Integer.parseInt(val));

	val = this.getGenProperties().getProperty("height");
	if (val != null)
		this.setImgBtHeight(Integer.parseInt(val));

	val = this.getGenProperties().getProperty("depth");
	if (val != null)
		this.setDepth(Integer.parseInt(val));

	val = this.getGenProperties().getProperty("bgColor");
	if (val != null)
		this.setDrawImgBGColor(new Color(Integer.parseInt(val, 16)));

	val = this.getGenProperties().getProperty("bgBtColor");
	if (val != null)
		this.setBGBtColor(new Color(Integer.parseInt(val, 16)));

	val = this.getGenProperties().getProperty("topBtColor");
	if (val != null)
		this.setTopBtColor(new Color(Integer.parseInt(val, 16)));

	val = this.getGenProperties().getProperty("bottomBtColor");
	if (val != null)
		this.setBottomBtColor(new Color(Integer.parseInt(val, 16)));

	val = this.getGenProperties().getProperty("fgBtColor");
	if (val != null)
		this.setFGBtColor(new Color(Integer.parseInt(val, 16)));

	val = this.getGenProperties().getProperty("selectFGBtColor");
	if (val != null)
		this.setSelectFGBtColor(new Color(Integer.parseInt(val, 16)));

	val = this.getGenProperties().getProperty("imgStartX");
	if (val != null)
		this.setImgStartX(Integer.parseInt(val));

	val = this.getGenProperties().getProperty("imgStartY");
	if (val != null)
		this.setImgStartY(Integer.parseInt(val));

	val = this.getGenProperties().getProperty("imgYJump");
	if (val != null)
		this.setImgYJump(Integer.parseInt(val));

	/*
	this.setBGBtColor(new Color(0xff298E9C)); // darker green
	this.setTopBtColor(this.getBGBtColor().brighter());
	this.setBottomBtColor(this.getBGBtColor().darker());
	this.setFGBtColor(
		new Color(0xff - 0x29, 0xff - 0x8e, 0xff - 0x9c).brighter());
	this.setSelectFGBtColor(new Color(this.getFGBtColor().getRed() + 0x00,
		this.getFGBtColor().getGreen() + 0x12,
		this.getFGBtColor().getBlue() + 0x12));
	*/
}

public
GenTxtBts()
{
	this(1.0, null);
}

static public void
main(String args[])
{
	try
	{
		(new GenTxtBts(1.0, null)).setVisible(true);
	}
	catch(Exception e)
	{
		handleException("Exception from main: ", e, 101);
	}
}

public void
buildGui()
{
	border = new BevelBorder(1);

	setLayout(new BorderLayout());
	setVisible(false);
	setBounds(getGuiStartX(), getGuiStartY(), getGuiWidth(), getGuiHeight());
	setBackground(guiBGColor);

	controlPanel = new JPanel();
	controlPanel.setLayout(new BorderLayout(0,0));
	controlPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	controlPanel.setForeground(Color.black);
	controlPanel.setBackground(guiBGColor);
	controlPanel.setBorder(new TitledBorder(border, "Main Controls:"));
	add(BorderLayout.NORTH, controlPanel);

	generalBtPanel = new JPanel();
	generalBtPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	generalBtPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	generalBtPanel.setForeground(Color.black);
	generalBtPanel.setBackground(guiBGColor);
	generalBtPanel.setBorder(border);
	controlPanel.add("North", generalBtPanel);

	startBt = new JButton();
	startBt.setText("Start");
	startBt.setActionCommand("Start");
	startBt.setFont(new Font("Dialog", Font.BOLD, 12));
	startBt.setForeground(Color.black);
	startBt.setBackground(guiBGColor);
	generalBtPanel.add(startBt);
	startBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				initFromProperties("txtbts.properties");
				btTextTF.setText(getBtText());

				fontChooser.resetFontSize(getBtFontSize());
				fontChooser.resetFontStyle(getBtFontStyle());
				Font testFont = null;
int style = getBtFontStyle();
String strFontStyle = null;
if (style == Font.PLAIN)
	strFontStyle = "PLAIN";
else if (style == Font.BOLD)
	strFontStyle = "BOLD";
else if (style == Font.ITALIC)
	strFontStyle = "ITALIC";
else if (style == (Font.BOLD + Font.ITALIC))
	strFontStyle = "BOLDITALIC";
fontChooser.setCurrentFont(testFont = Font.decode(getBtFontFamily() + "-" +
	strFontStyle + "-" + getBtFontSize()));
				debug(testFont.toString());
				widthTF.setText(String.valueOf(getImgBtWidth()));
				heightTF.setText(String.valueOf(getImgBtHeight()));
				depthCBox.setSelectedIndex(getDepth());

				if (showFontsChkBox.isSelected())
				{
					GenTxtBts.this.getViewImgCanvas().setPreferredSize(
						new Dimension(500, 33*drawObjectImgH));
						// new Dimension(500, 2*drawObjectImgH));
					genImgScrollPane.setViewportView(GenTxtBts.this.getViewImgCanvas());
					renderDrawObjectView(500, 33*drawObjectImgH);
					// renderDrawObjectView(500, 2*drawObjectImgH);
					updateUI();
				}
				else
				{
					renderDrawObjectView(drawObjectImgW, drawObjectImgH);
				}
			}
			catch (Exception e)
			{
				handleException("Exception in renderDrawObjectView:", e, 101);
			}
		}
	});

	genFileChooserBt = new JButton();
	genFileChooserBt.setText("Choose File");
	genFileChooserBt.setActionCommand("Choose_File");
	genFileChooserBt.setFont(new Font("Dialog", Font.BOLD, 12));
	genFileChooserBt.setForeground(Color.black);
	genFileChooserBt.setBackground(guiBGColor);
	generalBtPanel.add(genFileChooserBt);
	genFileChooser = new JFileChooser(".");
	genFileFilter = new GenFileFilter();
	genFileFilter.addExtension("jpg");
	genFileFilter.addExtension("JPG");
	genFileFilter.addExtension("gif");
	genFileFilter.addExtension("GIF");
	genFileFilter.addExtension("tif");
	genFileFilter.addExtension("TIF");
	genFileFilter.setDescription("Image Files");
	genFileChooser.setFileFilter(genFileFilter);
	genFileChooserBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				int returnVal =
					genFileChooser.showOpenDialog(GenTxtBts.this);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;
				
				setCurrentInputFile(genFileChooser.getSelectedFile());

				debug("Opening: " + getCurrentInputFile().getName() +
					"." + "\n");

				/*
				genImgScrollPane.setBorder(new TitledBorder(border, "View "
					+ genFileFilter.getBodyName(getCurrentInputFile())
					+ ":"));
				*/

				runSetFromInputFile();
				renderDrawObjectView();
			}
			catch (Exception e)
			{
				handleException("Exception in genFileChooser:", e, 101);
			}
		}
	});

	/*
	printBt = new JButton();
	printBt.setText("Print");
	printBt.setActionCommand("Print");
	printBt.setFont(new Font("Dialog", Font.BOLD, 12));
	printBt.setForeground(Color.black);
	printBt.setBackground(guiBGColor);
	generalBtPanel.add(printBt);
	printBt.addMouseListener(new MouseAdapter()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			PrinterJob pj = PrinterJob.getPrinterJob();

			if (landscapeModeCB.isSelected())
			{
				PageFormat pf = pj.defaultPage();
				pf.setOrientation(PageFormat.LANDSCAPE);
				pj.setPrintable(new GenImgPrintable(), pf);
			}
			else
			{
				pj.setPrintable(new GenImgPrintable());
			}

			if (pj.printDialog())
			{
				try
				{
					pj.print();
				}
				catch (PrinterException e)
				{
					handleException("Exception in printBt.mouseClicked:", e, 101);
				}
			}
		}
	});

	testBt = new JButton();
	testBt.setText("Test");
	testBt.setActionCommand("Test");
	testBt.setFont(new Font("Dialog", Font.BOLD, 12));
	testBt.setForeground(Color.black);
	testBt.setBackground(guiBGColor);
	generalBtPanel.add(testBt);
	testBt.addMouseListener(new MouseAdapter()
	{
		public void
		mouseClicked(MouseEvent event)
		{
		}
	});
	*/

	printImgsBt = new JButton();
	printImgsBt.setText("Write out Images");
	printImgsBt.setActionCommand("Write out Images");
	printImgsBt.setFont(new Font("Dialog", Font.BOLD, 12));
	printImgsBt.setForeground(Color.black);
	printImgsBt.setBackground(guiBGColor);
	generalBtPanel.add(printImgsBt);
	printImgsBt.addMouseListener(new MouseAdapter()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			try
			{
				printBtImgs();
			}
			catch (Exception e)
			{
				handleException("Exception in printImgsBt.mouseClicked:",
					e, 101);
			}
		}
	});

	fontChooserBt = new JButton();
	fontChooserBt.setText("Font");
	fontChooserBt.setActionCommand("Font");
	fontChooserBt.setFont(new Font("Dialog", Font.BOLD, 12));
	fontChooserBt.setForeground(Color.black);
	fontChooserBt.setBackground(guiBGColor);
	generalBtPanel.add(fontChooserBt);
	fontChooser = new FontChooser(GenTxtBts.this);

	fontChooser.resetFontSize(getBtFontSize());
	fontChooser.resetFontStyle(Font.PLAIN);
	// fontChooser.fontTestTF.setText("A U C G");

	fontChooserBt.addMouseListener(new MouseAdapter()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			/*
			if (fontChooser == null)
				fontChooser = new FontChooser(GenTxtBts.this);
			*/
			fontChooser.resetFontSize(getBtFontSize());
			fontChooser.setVisible(true);
		}
	});

	colorChooserBt = new JButton();
	colorChooserBt.setText("Color");
	colorChooserBt.setActionCommand("Color");
	colorChooserBt.setFont(new Font("Dialog", Font.BOLD, 12));
	colorChooserBt.setForeground(Color.black);
	colorChooserBt.setBackground(guiBGColor);
	generalBtPanel.add(colorChooserBt);
	colorChooserBt.addMouseListener(new MouseAdapter()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			JFrame f = new JFrame("Color Chooser");
			f.setBounds(10, 60, 300, 500);

			colorChooser = new JColorChooser(new Color(0xff333399));

			AbstractColorChooserPanel[] acList =
				colorChooser.getChooserPanels();
			AbstractColorChooserPanel[] newACList =
				new AbstractColorChooserPanel[4];
			newACList[0] = new HexColorChooserPanel();
			newACList[1] = acList[1];
			newACList[2] = acList[0];
			newACList[3] = acList[2];
			colorChooser.setChooserPanels(newACList);
			colorChooser.setPreviewPanel(new JPanel());

			f.getContentPane().add(BorderLayout.CENTER, colorChooser);
			colorChooser.setVisible(true);
			f.setVisible(true);

			colorChooser.getSelectionModel().addChangeListener(
			new ChangeListener()
			{
				public void
				stateChanged(ChangeEvent evt)
				{
					if (bgColorRB.isSelected())
						setDrawImgBGColor(colorChooser.getColor());
					else if (bgBtColorRB.isSelected())
						setBGBtColor(colorChooser.getColor());
					else if (fgBtColorRB.isSelected())
						setFGBtColor(colorChooser.getColor());
					else if (topBtColorRB.isSelected())
						setTopBtColor(colorChooser.getColor());
					else if (bottomBtColorRB.isSelected())
						setBottomBtColor(colorChooser.getColor());
					else if (selectFGBtColorRB.isSelected())
						setSelectFGBtColor(colorChooser.getColor());
					try
					{
						drawRoot();
					}
					catch (Exception e)
					{
						handleException("Exception in colorChooser.stateChanged:",
							e, 101);
					}
				}
			});

			// f.addComponentListener(new ComponentListener());
		}
	});

	stopBt = new JButton();
	stopBt.setText("Stop");
	stopBt.setActionCommand("Stop");
	stopBt.setFont(new Font("Dialog", Font.BOLD, 12));
	stopBt.setForeground(Color.black);
	stopBt.setBackground(guiBGColor);
	generalBtPanel.add(stopBt);
	stopBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			genImgExit();
		}
	});

	genPanelComboBox = new JComboBox(new String[] {
		"apply",
		"transform"});
	genPanelComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
	genPanelComboBox.setForeground(Color.black);
	genPanelComboBox.setBackground(guiBGColor);
	generalBtPanel.add(genPanelComboBox);	
	genPanelComboBox.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			String cmd = (String)genPanelComboBox.getSelectedItem();
			if (cmd.equals("apply"))
			{
				if (transformControlPanel != null)
					remove(transformControlPanel);
				if (applyControlPanel != null)
					add(BorderLayout.WEST, applyControlPanel);
			}
			else if (cmd.equals("transform"))
			{
				if (applyControlPanel != null)
					remove(applyControlPanel);
				if (transformControlPanel!= null)
					add(BorderLayout.WEST, transformControlPanel);
			}
			updateUI();
		}
	});

	transformControlPanel = new JPanel(new BorderLayout());
	transformControlPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	transformControlPanel.setForeground(Color.black);
	transformControlPanel.setBackground(guiBGColor);
	transformControlPanel.setBorder(
		new TitledBorder(border, "Transform Controls:"));
	// add(BorderLayout.WEST, transformControlPanel);

	applyControlPanel = new JPanel(new BorderLayout());
	applyControlPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	applyControlPanel.setForeground(Color.black);
	applyControlPanel.setBackground(guiBGColor);
	applyControlPanel.setBorder(
		new TitledBorder(border, "Application Controls:"));
	add(BorderLayout.WEST, applyControlPanel);

	JPanel scalePanel = new JPanel(new BorderLayout());
	scalePanel.setBackground(guiBGColor);
	scaleTF = new JTextField(" 0.00", 3);
	scaleTF.setForeground(Color.black);
	scaleTF.setBackground(Color.white);
	scaleTF.setVisible(true);
	scaleTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				double val = Double.parseDouble((String)scaleTF.getText());

				// made it past valid double exception
				GenTxtBts.this.setFigureScale(val);
				scaleTF.setText(Double.toString(val));
				drawRoot();
			}
			catch (NumberFormatException e)
			{
				System.out.println("IN NUMBERFORMAT EXCEPTION in scaleCB: " + e.toString());
				// keep last angle and don't do anything
			}
			catch (Exception e)
			{
				handleException("Exception in scaleCB.actionPerformed:", e, 101);
			}
		}
	});

	scaleCB = new JComboBox(new String[] {
		"0.2",
		"0.3",
		"0.4",
		"0.5",
		"0.6",
		"0.7",
		"0.8",
		"0.9",
		"1.0",
		"2.0",
		"3.0",
		"4.0",
		"5.0",
		"6.0",
		"7.0",
		"8.0",
		"9.0",
		"10.0"});
	resetFigureScale(this.getFigureScale());

	scaleCB.setActionCommand("Scale Image");
	scaleCB.setFont(new Font("Dialog", Font.BOLD, 12));
	scaleCB.setForeground(Color.black);
	scaleCB.setBackground(guiBGColor);
	JLabel scaleLabel = new JLabel("Scale Figure:");
	scaleLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
	scaleLabel.setLabelFor(scaleCB);
	scalePanel.add(BorderLayout.NORTH, scaleLabel);

	JPanel subScalePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	subScalePanel.setFont(new Font("Dialog", Font.BOLD, 12));
	subScalePanel.setForeground(Color.black);
	subScalePanel.setBackground(guiBGColor);

	subScalePanel.add(scaleCB);
	subScalePanel.add(scaleTF);
	scalePanel.add(BorderLayout.CENTER, subScalePanel);

	scaleCB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				double val = Double.parseDouble((String)scaleCB.getSelectedItem());

				// made it past valid double exception
				GenTxtBts.this.setFigureScale(val);
				scaleTF.setText(Double.toString(val));
				drawRoot();
			}
			catch (NumberFormatException e)
			{
				System.out.println("IN NUMBERFORMAT EXCEPTION in scaleCB: " + e.toString());
				// keep last angle and don't do anything
			}
			catch (Exception e)
			{
				handleException("Exception in scaleCB.actionPerformed:", e, 101);
			}
		}
	});
	transformControlPanel.add(BorderLayout.NORTH, scalePanel);

	int applyBtPanelFontSize = 10;
	applyBtPanel = new JPanel(new GridLayout(20, 1));
	// applyBtPanel = new Box(BoxLayout.Y_AXIS);
	applyBtPanel.setFont(new Font("Dialog", Font.PLAIN, applyBtPanelFontSize));
	applyBtPanel.setForeground(Color.black);
	applyBtPanel.setBackground(guiBGColor);

	applyBtScrollPane = new JScrollPane(applyBtPanel,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		/*
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		*/
	applyBtScrollPane.setBackground(guiBGColor);

	btTextTF = new JTextField(" ", 3);
	btTextTF.setForeground(Color.black);
	btTextTF.setBackground(Color.white);
	applyBtPanel.add(btTextTF);
	btTextTF.setVisible(true);
	btTextTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				setBtText(btTextTF.getText().trim());
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in btTextTF.actionPerformed:", e, 101);
			}
		}
	});

	btTextBt = new JButton("Apply Text");
	btTextBt.setActionCommand("Apply Text");
	btTextBt.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	btTextBt.setForeground(Color.black);
	btTextBt.setBackground(guiBGColor);
	applyBtPanel.add(btTextBt);
	btTextBt.setVisible(true);
	btTextBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				setBtText(btTextTF.getText().trim());
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in btTextBt.actionPerformed:", e, 101);
			}
		}
	});

	showFontsChkBox = new JCheckBox();
	showFontsChkBox.setSelected(false);
	showFontsChkBox.setText("show fonts");
	showFontsChkBox.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	showFontsChkBox.setForeground(Color.black);
	showFontsChkBox.setBackground(guiBGColor);
	showFontsChkBox.setVisible(true);
	applyBtPanel.add(showFontsChkBox);
	showFontsChkBox.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			try
			{
				if (showFontsChkBox.isSelected())
				{
				}
				else
				{
				}
				// drawRoot();
				
				// renderDrawObjectView(drawObjectImgW, 4 * drawObjectImgH);

			}
			catch (Exception e)
			{
				handleException("Exception in landscapeModeCB.addItemListener:", e, 101);
			}
		}
	});

	depthCBox = new JComboBox(new String[]
		{"0 pixel depth",
		"1 pixel depth",
		"2 pixel depth",
		"3 pixel depth",
		"4 pixel depth",
		"5 pixel depth",
		"6 pixel depth"});
	depthCBox.setActionCommand("depth");
	depthCBox.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	depthCBox.setForeground(Color.black);
	depthCBox.setBackground(guiBGColor);
	depthCBox.setSelectedIndex(3);
	applyBtPanel.add(depthCBox);
	depthCBox.setVisible(true);
	depthCBox.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				int val = Integer.parseInt(
					((String)depthCBox.getSelectedItem()).trim().substring(0, 1));

				// made it past valid Integer exception
				setDepth(val);
				drawRoot();
			}
			catch (NumberFormatException e)
			{
				System.out.println("IN NUMBERFORMAT EXCEPTION in depthCBox: " + e.toString());
				// keep last angle and don't do anything
			}
			catch (Exception e)
			{
				handleException("Exception in depthCBox.actionPerformed:", e, 101);
			}
		}
	});

	cartesianCoordsCB = new JCheckBox();
	cartesianCoordsCB.setSelected(false);
	cartesianCoordsCB.setText("show coords");
	cartesianCoordsCB.setActionCommand("show coords");
	cartesianCoordsCB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	cartesianCoordsCB.setForeground(Color.black);
	cartesianCoordsCB.setBackground(guiBGColor);
	applyBtPanel.add(cartesianCoordsCB);
	cartesianCoordsCB.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			try
			{
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in cartesianCoordsCB.addItemListener:", e, 101);
			}
		}
	});

	showMousePosCB = new JCheckBox();
	showMousePosCB.setSelected(true);
	showMousePosCB.setText("show mouse pos");
	showMousePosCB.setActionCommand("show mouse pos");
	showMousePosCB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	showMousePosCB.setForeground(Color.black);
	showMousePosCB.setBackground(guiBGColor);
	applyBtPanel.add(showMousePosCB);

	landscapeModeCB = new JCheckBox();
	landscapeModeCB.setSelected(false);
	landscapeModeCB.setText("landscape mode");
	landscapeModeCB.setActionCommand("landscape mode");
	landscapeModeCB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	landscapeModeCB.setForeground(Color.black);
	landscapeModeCB.setBackground(guiBGColor);
	applyBtPanel.add(landscapeModeCB);
	landscapeModeCB.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			try
			{
				if (landscapeModeCB.isSelected())
				{
					drawObjectImgW = BASE_IMG_HEIGHT;
					drawObjectImgH = BASE_IMG_WIDTH;
				}
				else
				{
					drawObjectImgW = BASE_IMG_WIDTH;
					drawObjectImgH = BASE_IMG_HEIGHT;
				}

				setInitTransX(drawObjectImgW/2);
				setInitTransY(drawObjectImgH/2);

				GenTxtBts.this.getViewImgCanvas().setPreferredSize(
					new Dimension(drawObjectImgW, drawObjectImgH));
				genImgScrollPane.setViewportView(GenTxtBts.this.getViewImgCanvas());

				renderDrawObjectView();
			}
			catch (Exception e)
			{
				handleException("Exception in landscapeModeCB.addItemListener:", e, 101);
			}
		}
	});

	aliasOnChkBox = new JCheckBox();
	aliasOnChkBox.setSelected(true);
	aliasOnChkBox.setText("alias text");
	aliasOnChkBox.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	aliasOnChkBox.setForeground(Color.black);
	aliasOnChkBox.setBackground(guiBGColor);
	applyBtPanel.add(aliasOnChkBox);
	aliasOnChkBox.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			try
			{
				if (aliasOnChkBox.isSelected())
				{
				}
				else
				{
				}
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in landscapeModeCB.addItemListener:", e, 101);
			}
		}
	});

	ItemListener colorRBItemListener = new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			try
			{
				if (colorChooser != null)
				{
					if (bgColorRB.isSelected())
						setDrawImgBGColor(colorChooser.getColor());
					else if (bgBtColorRB.isSelected())
						setBGBtColor(colorChooser.getColor());
					else if (fgBtColorRB.isSelected())
						setFGBtColor(colorChooser.getColor());
					else if (topBtColorRB.isSelected())
						setTopBtColor(colorChooser.getColor());
					else if (bottomBtColorRB.isSelected())
						setBottomBtColor(colorChooser.getColor());
					else if (selectFGBtColorRB.isSelected())
						setSelectFGBtColor(colorChooser.getColor());
					drawRoot();
				}
			}
			catch (Exception e)
			{
				handleException("Exception in colorRBItemListener:", e, 101);
			}
		}
	};

	ButtonGroup colorBtGroup = new ButtonGroup();

	bgColorRB = new JRadioButton();
	bgColorRB.setSelected(false);
	bgColorRB.setText("bg color");
	bgColorRB.setActionCommand("bg color");
	bgColorRB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	bgColorRB.setForeground(Color.black);
	bgColorRB.setBackground(guiBGColor);
	bgColorRB.addItemListener(colorRBItemListener);
	colorBtGroup.add(bgColorRB);
	applyBtPanel.add(bgColorRB);

	bgBtColorRB = new JRadioButton();
	bgBtColorRB.setSelected(false);
	bgBtColorRB.setText("bg pixel");
	bgBtColorRB.setActionCommand("bp shade");
	bgBtColorRB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	bgBtColorRB.setForeground(Color.black);
	bgBtColorRB.setBackground(guiBGColor);
	bgBtColorRB.addItemListener(colorRBItemListener);
	colorBtGroup.add(bgBtColorRB);
	applyBtPanel.add(bgBtColorRB);

	fgBtColorRB = new JRadioButton();
	fgBtColorRB.setSelected(false);
	fgBtColorRB.setText("fg pixel");
	fgBtColorRB.setActionCommand("fg shade");
	fgBtColorRB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	fgBtColorRB.setForeground(Color.black);
	fgBtColorRB.setBackground(guiBGColor);
	fgBtColorRB.addItemListener(colorRBItemListener);
	colorBtGroup.add(fgBtColorRB);
	applyBtPanel.add(fgBtColorRB);

	topBtColorRB = new JRadioButton();
	topBtColorRB.setSelected(false);
	topBtColorRB.setText("top shade");
	topBtColorRB.setActionCommand("top shade");
	topBtColorRB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	topBtColorRB.setForeground(Color.black);
	topBtColorRB.setBackground(guiBGColor);
	topBtColorRB.addItemListener(colorRBItemListener);
	colorBtGroup.add(topBtColorRB);
	applyBtPanel.add(topBtColorRB);

	bottomBtColorRB = new JRadioButton();
	bottomBtColorRB.setSelected(false);
	bottomBtColorRB.setText("bottom shadepixel");
	bottomBtColorRB.setActionCommand("bottom shade");
	bottomBtColorRB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	bottomBtColorRB.setForeground(Color.black);
	bottomBtColorRB.setBackground(guiBGColor);
	bottomBtColorRB.addItemListener(colorRBItemListener);
	colorBtGroup.add(bottomBtColorRB);
	applyBtPanel.add(bottomBtColorRB);

	selectFGBtColorRB = new JRadioButton();
	selectFGBtColorRB.setSelected(false);
	selectFGBtColorRB.setText("select FG shade");
	selectFGBtColorRB.setActionCommand("select FG shade");
	selectFGBtColorRB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	selectFGBtColorRB.setForeground(Color.black);
	selectFGBtColorRB.setBackground(guiBGColor);
	selectFGBtColorRB.addItemListener(colorRBItemListener);
	colorBtGroup.add(selectFGBtColorRB);
	applyBtPanel.add(selectFGBtColorRB);

	setColorChooserRB = new JRadioButton();
	setColorChooserRB.setSelected(false);
	setColorChooserRB.setText("set color chooser");
	setColorChooserRB.setActionCommand("set color chooser");
	setColorChooserRB.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	setColorChooserRB.setForeground(Color.black);
	setColorChooserRB.setBackground(guiBGColor);
	colorBtGroup.add(setColorChooserRB);
	applyBtPanel.add(setColorChooserRB);

	autoShadeBt = new JButton("Auto Shade");
	autoShadeBt.setActionCommand("Auto Shade");
	autoShadeBt.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	autoShadeBt.setForeground(Color.black);
	autoShadeBt.setBackground(guiBGColor);
	applyBtPanel.add(autoShadeBt);
	autoShadeBt.setVisible(true);
	autoShadeBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				setTopBtColor(getBGBtColor().brighter());
				setBottomBtColor(getBGBtColor().darker());
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in autoShadeBt.actionPerformed:", e, 101);
			}
		}
	});

	widthTF = new JTextField(String.valueOf(this.getImgBtWidth()), 3);
	widthTF.setForeground(Color.black);
	widthTF.setBackground(Color.white);
	applyBtPanel.add(widthTF);
	widthTF.setVisible(true);
	widthTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				setImgBtWidth(Integer.parseInt(widthTF.getText()));
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in widthTF.actionPerformed:", e, 101);
			}
		}
	});

	heightTF = new JTextField(String.valueOf(this.getImgBtHeight()), 3);
	heightTF.setForeground(Color.black);
	heightTF.setBackground(Color.white);
	applyBtPanel.add(heightTF);
	heightTF.setVisible(true);
	heightTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				setImgBtHeight(Integer.parseInt(heightTF.getText()));
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in heightTF.actionPerformed:", e, 101);
			}
		}
	});

	applyDimensionBt = new JButton("Apply Dimension");
	applyDimensionBt.setActionCommand("Apply Dimension");
	applyDimensionBt.setFont(new Font("Dialog", Font.BOLD, applyBtPanelFontSize));
	applyDimensionBt.setForeground(Color.black);
	applyDimensionBt.setBackground(guiBGColor);
	applyBtPanel.add(applyDimensionBt);
	applyDimensionBt.setVisible(true);
	applyDimensionBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				setImgBtWidth(Integer.parseInt(widthTF.getText()));
				setImgBtHeight(Integer.parseInt(heightTF.getText()));
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in applyDimensionBt.actionPerformed:", e, 101);
			}
		}
	});

	// applyControlPanel.add(BorderLayout.CENTER, applyBtPanel);
	applyControlPanel.add(BorderLayout.CENTER, applyBtScrollPane);

	/*
	imgStartXTF = new JTextField(String.valueOf(this.getImgBtHeight()), 3);
	imgStartXTF.setForeground(Color.black);
	imgStartXTF.setBackground(Color.white);
	applyBtPanel.add(imgStartXTF);
	imgStartXTF.setVisible(true);
	imgStartXTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				setImgBtHeight(Integer.parseInt(imgStartXTF.getText()));
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in imgStartXTF.actionPerformed:", e, 101);
			}
		}
	});
	*/

	this.setViewImgCanvas(new ViewImgCanvas(this.getSceneImg()));
	this.getViewImgCanvas().setBackground(guiBGColor);
	this.getViewImgCanvas().setOpaque(true);
	this.getViewImgCanvas().setPreferredSize(new Dimension(drawObjectImgW, drawObjectImgH));

	genImgScrollPane = new JScrollPane(this.getViewImgCanvas(),
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		/*
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		*/
	genImgScrollPane.setBorder(new TitledBorder(border, "View Img:"));
	genImgScrollPane.setBackground(guiBGColor);
	add(BorderLayout.CENTER, genImgScrollPane);
	this.getViewImgCanvas().setVisible(true);

	setMouseMethod();

	spSystemOut = new JScrollPane();
	spSystemOut.setOpaque(true);
	spSystemOut.setBounds(6,123,463,100);
	spSystemOut.setFont(new Font("Dialog", Font.PLAIN, 12));
	spSystemOut.setForeground(Color.black);
	spSystemOut.setBackground(guiBGColor);
	spSystemOut.setBorder(new TitledBorder(border, "System Out"));
	taSystemOut = new JTextPane();
	taSystemOut.setMargin(new java.awt.Insets(0,0,0,0));
	taSystemOut.setBounds(43,41,347,106);
	taSystemOut.setFont(new Font("Monospaced", Font.PLAIN, 12));
	taSystemOut.setForeground(Color.black);
	taSystemOut.setBackground(Color.white);
	spSystemOut.getViewport().add(taSystemOut);
	add(BorderLayout.SOUTH, spSystemOut);

	addComponentListener(new ComponentListener()
	{
		/*
		...
		//where initialization occurs:
			aFrame = new JFrame("A Frame");
			ComponentPanel p = new ComponentPanel(this);
			aFrame.addComponentListener(this);
			p.addComponentListener(this);
		...
		*/

		public void
		componentHidden(ComponentEvent e)
		{
			/*
			System.out.println("componentHidden event from "
			+ e.getComponent().getClass().getName());
			*/
		}

		public void
		componentMoved(ComponentEvent e)
		{
		}

		public void
		componentResized(ComponentEvent e)
		{
		}

		public void
		componentShown(ComponentEvent e)
		{
		}
	});

}

public void
resetFigureScale(double val)
{
	if ((scaleCB == null) || (scaleTF == null))
		return;
	scaleCB.setSelectedItem(Double.toString(val));
	scaleTF.setText(Double.toString(val));
}

public void
printBtImgs()
throws Exception
{
	printBtImg(this.getUpImg(), this.getBtOutBaseName() + "_up");
	printBtImg(this.getDownImg(), this.getBtOutBaseName() + "_down");
	printBtImg(this.getSelectImg(), this.getBtOutBaseName() + "_select");
	printBtImg(this.getOverImg(), this.getBtOutBaseName() + "_over");

	printConsole(this.getBtOutBaseName() + " Images printed");
}

private void
printBtImg(BufferedImage img, String imgFileName)
throws Exception
{
	OutputStream gifOutputFile = new BufferedOutputStream(
		/*
		new FileOutputStream(imgFileName + ".jpg"));
		*/
		new FileOutputStream(imgFileName + ".gif"));

	GifEncoder gifWrite = new GifEncoder(img, gifOutputFile);
	gifWrite.encode();
	gifOutputFile.flush();
	gifOutputFile.close();

	/*
	File genImgOutFile = new File(imgFileName);
	debug("WORKING ON: " + genImgOutFile.toString() + " " +
		genImgOutFile.canWrite());
	
	String outFileName = JAIUtil.printImage(img, genImgOutFile);
	if (outFileName == null)
	{
		printConsole("ERROR: IMAGE NOT PRINTED");
		debug("ERROR: IMAGE NOT PRINTED");
	}
	else
	{
		printConsole("Image printed to: " + outFileName);
		debug("Image printed to: " + outFileName);
	}
	*/
}

public void
doViewImgMousePressed()
{
}

public void
doViewImgMouseReleased()
{
	if (setColorChooserRB.isSelected())
		colorChooser.setColor(this.getViewImgCanvas().getImgPixelAt(
			imgSpaceX(getCurrentViewX()),
			imgSpaceY(getCurrentViewY())));
}

public void
doViewImgMouseDragged()
{
}

public void
doViewImgMouseMoved()
{
}

private int imgStartX = -60;

public void
setImgStartX(int imgStartX)
{
    this.imgStartX = imgStartX;
}

public int
getImgStartX()
{
    return (this.imgStartX);
}

private int imgStartY = 200;

public void
setImgStartY(int imgStartY)
{
    this.imgStartY = imgStartY;
}

public int
getImgStartY()
{
    return (this.imgStartY);
}

private int imgYJump = 80;

public void
setImgYJump(int imgYJump)
{
    this.imgYJump = imgYJump;
}

public int
getImgYJump()
{
    return (this.imgYJump);
}

private String btFontFamily = "Helvetica";

public void
setBtFontFamily(String btFontFamily)
{
    this.btFontFamily = btFontFamily;
}

public String
getBtFontFamily()
{
    return (this.btFontFamily);
}

private String btFontName = "Helvetica";

public void
setBtFontName(String btFontName)
{
    this.btFontName = btFontName;
}

public String
getBtFontName()
{
    return (this.btFontName);
}

private String btFontFontName = "Helvetica";

public void
setBtFontFontName(String btFontFontName)
{
    this.btFontFontName = btFontFontName;
}

public String
getBtFontFontName()
{
    return (this.btFontFontName);
}

private int btFontStyle = Font.PLAIN;

public void
setBtFontStyle(int btFontStyle)
{
    this.btFontStyle = btFontStyle;
}

public int
getBtFontStyle()
{
    return (this.btFontStyle);
}

private int btFontSize = 24;

public void
setBtFontSize(int btFontSize)
{
    this.btFontSize = btFontSize;
}

public int
getBtFontSize()
{
    return (this.btFontSize);
}

private BufferedImage upImg = null;

public void
setUpImg(BufferedImage upImg)
{
    this.upImg = upImg;
}

public BufferedImage
getUpImg()
{
    return (this.upImg);
}

private BufferedImage downImg = null;

public void
setDownImg(BufferedImage downImg)
{
    this.downImg = downImg;
}

public BufferedImage
getDownImg()
{
    return (this.downImg);
}

private BufferedImage overImg = null;

public void
setOverImg(BufferedImage overImg)
{
    this.overImg = overImg;
}

public BufferedImage
getOverImg()
{
    return (this.overImg);
}

private BufferedImage selectImg = null;

public void
setSelectImg(BufferedImage selectImg)
{
    this.selectImg = selectImg;
}

public BufferedImage
getSelectImg()
{
    return (this.selectImg);
}

private BufferedImage compareImg = null;

public void
setCompareImg(BufferedImage compareImg)
{
    this.compareImg = compareImg;
}

public BufferedImage
getCompareImg()
{
    return (this.compareImg);
}

private String btOutBaseName = null;

public void
setBtOutBaseName(String btOutBaseName)
{
    this.btOutBaseName = btOutBaseName;
}

public String
getBtOutBaseName()
{
    return (this.btOutBaseName);
}

private String btText = null;

public void
setBtText(String btText)
{
    this.btText = btText;
	this.setBtOutBaseName(this.getBtText().trim().replace(' ', '_').replace('\n', '_').toLowerCase());
}

public String
getBtText()
{
    return (this.btText);
}

private Color bgBtColor = null;

public void
setBGBtColor(Color bgBtColor)
{
    this.bgBtColor = bgBtColor;
}

public Color
getBGBtColor()
{
    return (this.bgBtColor);
}

private Color fgBtColor = null;

public void
setFGBtColor(Color fgBtColor)
{
    this.fgBtColor = fgBtColor;
}

public Color
getFGBtColor()
{
    return (this.fgBtColor);
}

private Color topBtColor = null;

public void
setTopBtColor(Color topBtColor)
{
    this.topBtColor = topBtColor;
}

public Color
getTopBtColor()
{
    return (this.topBtColor);
}

private Color bottomBtColor = null;

public void
setBottomBtColor(Color bottomBtColor)
{
    this.bottomBtColor = bottomBtColor;
}

public Color
getBottomBtColor()
{
    return (this.bottomBtColor);
}

private Color selectFGBtColor = null;

public void
setSelectFGBtColor(Color selectFGBtColor)
{
    this.selectFGBtColor = selectFGBtColor;
}

public Color
getSelectFGBtColor()
{
    return (this.selectFGBtColor);
}

private int imgBtWidth = 0;

public void
setImgBtWidth(int imgBtWidth)
{
    this.imgBtWidth = imgBtWidth;
}

public int
getImgBtWidth()
{
    return (this.imgBtWidth);
}

private int imgBtHeight = 0;

public void
setImgBtHeight(int imgBtHeight)
{
    this.imgBtHeight = imgBtHeight;
}

public int
getImgBtHeight()
{
    return (this.imgBtHeight);
}

private int depth = 3;

public void
setDepth(int depth)
{
    this.depth = depth;
}

public int
getDepth()
{
    return (this.depth);
}

public void
createDrawList()
throws Exception
{
	boolean showTwoLinesOfText = false;

	/* USE for general images
	RenderingHints renderHints =
		GraphicsUtil.stringAliasedRenderHints;
	*/
	// USE for bt Imgs:
	RenderingHints renderHints =
		GraphicsUtil.stringUnAliasedRenderHints;

	if (showFontsChkBox.isSelected())
	{
		double startX = ((float)-getInitTransX()) + 250.0;
		double startY = ((float)getInitTransY()) - 20.0;
		double yJump = 20.0;
		String text = btTextTF.getText();

		Font plainFont = new Font("Helvetica", Font.PLAIN, 10);
		Font[] fontList = fontChooser.fontList;

		/*
		String[] fontFamilyList =
			GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		*/

		double yInc = startY + yJump;
		for (int i = 0;i < fontList.length;i++)
		// for (int i = 0;i <= 10;i++)
		{
			Font font = fontList[i].deriveFont((float)getBtFontSize());

			yInc -= yJump;
			getDrawObjectList().add(new DrawStringObject(startX, yInc,
				plainFont, Color.black,
				String.valueOf(i) + ") " + font.getName() + ":"));

			yInc -= yJump;
			getDrawObjectList().add(new DrawStringObject(startX, yInc,
				font, Color.black, text));
			debug(i + ": " + font.toString());

			yInc -= yJump;
			font = font.deriveFont(Font.BOLD);
			getDrawObjectList().add(new DrawStringObject(startX, yInc,
				font, Color.black, text));
			debug(i + ": " + font.toString());

			yInc -= yJump;
			font = font.deriveFont(Font.ITALIC);
			getDrawObjectList().add(new DrawStringObject(startX, yInc,
				font, Color.black, text));
			debug(i + ": " + font.toString());

			yInc -= yJump;
			font = font.deriveFont(Font.BOLD + Font.ITALIC);
			getDrawObjectList().add(new DrawStringObject(startX, yInc,
				font, Color.black, text));
			debug(i + ": " + font.toString());

			/*
			getDrawObjectList().add(new DrawStringObject(startX + 900, startY - (i * yJump),
				plainFont, Color.black, //renderHints,//
				"       ::  " + font.getName()));
			*/
		}
	}
	else if (showTwoLinesOfText)
	{
		Font btTextFont = fontChooser.getCurrentFont();
		// Font btTextFont = fontChooser.getCurrentFont().deriveFont((float)getBtFontSize());
		// debug("btTextFont: " + btTextFont.toString());

		int shiftX = 0; // shift text x amt to excess of centered
		int i = 0;
		boolean showBox = false;

		Graphics2D tmpG = (new BufferedImage(1, 1, this.getImgType()).createGraphics());
		DrawStringObject string1Object = null;

		string1Object = new DrawStringObject(
			this.getImgBtWidth()/2, -this.getImgBtHeight()/2, btTextFont,
			this.getFGBtColor(), showBox, this.getBtText());
		string1Object.update(tmpG);
		this.setUpImg(string1Object.drawImage(
			this.getImgBtWidth(), this.getImgBtHeight(), this.getBGBtColor(),
			this.getTopBtColor(), this.getBottomBtColor(), this.getDepth(), BufferedImage.TYPE_INT_RGB));
		getDrawObjectList().add(new DrawImageObject(this.getImgStartX(),
			this.getImgStartY() - (this.getImgYJump() * i++), this.getUpImg()));

		string1Object = new DrawStringObject(
			this.getImgBtWidth()/2, -this.getImgBtHeight()/2, btTextFont,
			this.getSelectFGBtColor(), showBox, this.getBtText());
		string1Object.update(tmpG);
		this.setOverImg(string1Object.drawImage(
			this.getImgBtWidth(), this.getImgBtHeight(), this.getBGBtColor(),
			this.getTopBtColor(), this.getBottomBtColor(), this.getDepth(), BufferedImage.TYPE_INT_RGB));
		getDrawObjectList().add(new DrawImageObject(this.getImgStartX(),
			this.getImgStartY() - (this.getImgYJump() * i++), this.getOverImg()));

		string1Object = new DrawStringObject(
			this.getImgBtWidth()/2, -this.getImgBtHeight()/2, btTextFont,
			this.getFGBtColor(), showBox, this.getBtText());
		string1Object.update(tmpG);
		this.setDownImg(string1Object.drawImage(
			this.getImgBtWidth(), this.getImgBtHeight(), this.getBGBtColor(),
			this.getBottomBtColor(), this.getTopBtColor(), this.getDepth(), BufferedImage.TYPE_INT_RGB));
		getDrawObjectList().add(new DrawImageObject(this.getImgStartX(),
			this.getImgStartY() - (this.getImgYJump() * i++), this.getDownImg()));

		string1Object = new DrawStringObject(
			this.getImgBtWidth()/2, -this.getImgBtHeight()/2, btTextFont,
			this.getSelectFGBtColor(), showBox, this.getBtText());
		string1Object.update(tmpG);
		this.setSelectImg(string1Object.drawImage(
			this.getImgBtWidth(), this.getImgBtHeight(), this.getBGBtColor(),
			this.getTopBtColor(), this.getBottomBtColor(), this.getDepth(), BufferedImage.TYPE_INT_RGB));
		getDrawObjectList().add(new DrawImageObject(this.getImgStartX(),
			this.getImgStartY() - (this.getImgYJump() * i++), this.getSelectImg()));
	}
	else
	{
		Font btTextFont = fontChooser.getCurrentFont();
		// Font btTextFont = fontChooser.getCurrentFont().deriveFont((float)getBtFontSize());
		debug("btTextFont: " + btTextFont.toString());

		int shiftX = 0; // shift text x amt to excess of centered
		int i = 0;
		boolean showBox = false;

		Graphics2D tmpG = (new BufferedImage(1, 1, this.getImgType()).createGraphics());
		DrawStringObject stringObject = null;

		stringObject = new DrawStringObject(
			this.getImgBtWidth()/2, -this.getImgBtHeight()/2, btTextFont,
			this.getFGBtColor(), showBox, this.getBtText());
		stringObject.update(tmpG);
		this.setUpImg(stringObject.drawImage(
			this.getImgBtWidth(), this.getImgBtHeight(), this.getBGBtColor(),
			this.getTopBtColor(), this.getBottomBtColor(), this.getDepth(), BufferedImage.TYPE_INT_RGB));
		getDrawObjectList().add(new DrawImageObject(this.getImgStartX(),
			this.getImgStartY() - (this.getImgYJump() * i++), this.getUpImg()));

		stringObject = new DrawStringObject(
			this.getImgBtWidth()/2, -this.getImgBtHeight()/2, btTextFont,
			this.getSelectFGBtColor(), showBox, this.getBtText());
		stringObject.update(tmpG);
		this.setOverImg(stringObject.drawImage(
			this.getImgBtWidth(), this.getImgBtHeight(), this.getBGBtColor(),
			this.getTopBtColor(), this.getBottomBtColor(), this.getDepth(), BufferedImage.TYPE_INT_RGB));
		getDrawObjectList().add(new DrawImageObject(this.getImgStartX(),
			this.getImgStartY() - (this.getImgYJump() * i++), this.getOverImg()));

		stringObject = new DrawStringObject(
			this.getImgBtWidth()/2, -this.getImgBtHeight()/2, btTextFont,
			this.getFGBtColor(), showBox, this.getBtText());
		stringObject.update(tmpG);
		this.setDownImg(stringObject.drawImage(
			this.getImgBtWidth(), this.getImgBtHeight(), this.getBGBtColor(),
			this.getBottomBtColor(), this.getTopBtColor(), this.getDepth(), BufferedImage.TYPE_INT_RGB));
		getDrawObjectList().add(new DrawImageObject(this.getImgStartX(),
			this.getImgStartY() - (this.getImgYJump() * i++), this.getDownImg()));

		stringObject = new DrawStringObject(
			this.getImgBtWidth()/2, -this.getImgBtHeight()/2, btTextFont,
			this.getSelectFGBtColor(), showBox, this.getBtText());
		stringObject.update(tmpG);
		this.setSelectImg(stringObject.drawImage(
			this.getImgBtWidth(), this.getImgBtHeight(), this.getBGBtColor(),
			this.getTopBtColor(), this.getBottomBtColor(), this.getDepth(), BufferedImage.TYPE_INT_RGB));
		getDrawObjectList().add(new DrawImageObject(this.getImgStartX(),
			this.getImgStartY() - (this.getImgYJump() * i++), this.getSelectImg()));

		if (this.getCompareImg() != null)
			getDrawObjectList().add(new DrawImageObject(this.getImgStartX(),
				this.getImgStartY() + (this.getImgYJump() * i++), this.getCompareImg()));
	}
}


public void
printConsole(String s)
{
	taSystemOut.setText(s);
}

public void
clearConsole()
{
	taSystemOut.setText("  ");
}

public void
updateImgWindowView()
{
}

public void
updateAfterRender()
{
}

private static void
debug(String s)
{
	System.out.println("GenTxtBts-> " + s);
}

public void runSetFromInputFile() throws Exception {
	// TODO Auto-generated method stub
	
}

}
