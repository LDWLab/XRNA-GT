package jimage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import util.GraphicsUtil;

public class GenImgPanel
extends DrawObjectView
{

public JComboBox		scaleCB = null;
public JTextField		scaleTF = null;

protected JCheckBox         showMousePosCB = null;

public JScrollPane genImgScrollPane = null;
public BevelBorder border = null;

public JScrollPane	spSystemOut = null;
public JTextPane		taSystemOut = null;

public JPanel			controlPanel = null;
public JPanel transformControlPanel = null;
public JPanel transformBtPanel = null;

public
GenImgPanel(double scaleVal, JFrame parent)
{
	this.setFigureScale(scaleVal);
	frameParent = parent;

	try
	{
		setGuiStartX(400);
		setGuiStartY(30);
		setGuiWidth(800);
		setGuiHeight(600);
		buildGui();
	}
	catch(Exception e)
	{
		handleException("Exception from Constructor: ", e, 1);
	}
}

public
GenImgPanel()
{
	this(1.0, null);
}

static public void
main(String args[])
{
	try
	{
		(new GenImgPanel(1.0, null)).setVisible(true);
	}
	catch(Exception e)
	{
		handleException("Exception from main: ", e, 101);
	}
}

public void
initFromProperties()
{
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
				renderDrawObjectView();
			}
			catch (Exception e)
			{
				handleException("Exception in renderDrawObjectView:", e, 101);
			}
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

	
	JButton writeGifBt = new JButton();
	writeGifBt.setText("Write Gif Img");
	writeGifBt.setActionCommand("Write Gif Img");
	writeGifBt.setFont(new Font("Dialog", Font.BOLD, 12));
	writeGifBt.setForeground(Color.black);
	writeGifBt.setBackground(guiBGColor);
	generalBtPanel.add(writeGifBt);
	writeGifBt.addMouseListener(new MouseAdapter()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			try
			{
				boolean running = true;
				while (running)
				{
				if (genFileChooser == null)
					return;
				int returnVal =
					genFileChooser.showSaveDialog(GenImgPanel.this);
				if (returnVal == JFileChooser.ERROR_OPTION)
					debug("ERROR IN OPENING FILE");
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;

				// (genFileChooser.getSelectedFile() == null))

				File selectedFile = genFileChooser.getSelectedFile();
				if (selectedFile.exists())
				{
					Object[] message = new Object[3];
					message[0] = new JLabel("File Exists");
					message[1] = new JLabel("Currently can't overwrite existing file");
					message[2] = new JLabel("Please list a unique file to write to");

					// maybe provide junk1.xml, junk2.xml, etc.
					
					Component parent = GenImgPanel.this;
					String[] options =
					{
						"Ok",
						"Cancel"
					};
					int result = JOptionPane.showOptionDialog(
						parent,  // the parent that the dialog blocks
						message,  // the dialog message array
						"File Exists", // the title of the dialog window
						JOptionPane.OK_CANCEL_OPTION, // option type
						JOptionPane.PLAIN_MESSAGE, // message type
						null, // optional icon, use null to use the default icon
						options, // options string array, will be made into buttons
						options[0] // option that should be made into a default button
						);

					switch (result)
					{
					  case 0: // yes
						// JOptionPane.showMessageDialog(parent, "Made it 0");
						break;
					  case 1: // no
						// JOptionPane.showMessageDialog(parent, "Made it 1");
						running = false;
						break;
					  default:
						break;
					}
				}
				else
				{
					/*
					File genImgOutFile = genFileChooser.getSelectedFile();
					debug("WORKING ON: " + genImgOutFile.toString() + " " +
						genImgOutFile.canWrite());
					genImgOutFile.createNewFile();
					debug("WORKING ON: " + genImgOutFile.toString() + " " +
						genImgOutFile.canWrite());
					FileWriter genFileWriter = new FileWriter(genImgOutFile);
					PrintWriter outFile = new PrintWriter(
						new BufferedWriter(genFileWriter), true);
					// GenImgPanel.this.getCurrentComplex().write(outFile);
					outFile.flush();
					outFile.close();
					*/

					File genImgOutFile = genFileChooser.getSelectedFile();
					debug("WORKING ON: " + genImgOutFile.toString() + " " +
						genImgOutFile.canWrite());
					
					String outFileName = genImgOutFile.getName();

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

					running = false;
				}
				}
			}
			catch (Exception e)
			{
				handleException("Exception in genFileChooser.showSaveDialog:", e, 101);
			}
		}

	});

	writeBt = new JButton();
	writeBt.setText("Write Img");
	writeBt.setActionCommand("Write Img");
	writeBt.setFont(new Font("Dialog", Font.BOLD, 12));
	writeBt.setForeground(Color.black);
	writeBt.setBackground(guiBGColor);
	generalBtPanel.add(writeBt);
	writeBt.addMouseListener(new MouseAdapter()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			try
			{
				boolean running = true;
				while (running)
				{
				if (genFileChooser == null)
					return;
				int returnVal =
					genFileChooser.showSaveDialog(GenImgPanel.this);
				if (returnVal == JFileChooser.ERROR_OPTION)
					debug("ERROR IN OPENING FILE");
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;

				// (genFileChooser.getSelectedFile() == null))

				File selectedFile = genFileChooser.getSelectedFile();
				if (selectedFile.exists())
				{
					Object[] message = new Object[3];
					message[0] = new JLabel("File Exists");
					message[1] = new JLabel("Currently can't overwrite existing file");
					message[2] = new JLabel("Please list a unique file to write to");

					// maybe provide junk1.xml, junk2.xml, etc.
					
					Component parent = GenImgPanel.this;
					String[] options =
					{
						"Ok",
						"Cancel"
					};
					int result = JOptionPane.showOptionDialog(
						parent,  // the parent that the dialog blocks
						message,  // the dialog message array
						"File Exists", // the title of the dialog window
						JOptionPane.OK_CANCEL_OPTION, // option type
						JOptionPane.PLAIN_MESSAGE, // message type
						null, // optional icon, use null to use the default icon
						options, // options string array, will be made into buttons
						options[0] // option that should be made into a default button
						);

					switch (result)
					{
					  case 0: // yes
						// JOptionPane.showMessageDialog(parent, "Made it 0");
						break;
					  case 1: // no
						// JOptionPane.showMessageDialog(parent, "Made it 1");
						running = false;
						break;
					  default:
						break;
					}
				}
				}
			}
			catch (Exception e)
			{
				handleException("Exception in genFileChooser.showSaveDialog:", e, 101);
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
	fontChooser = new /*Gen*/FontChooser(GenImgPanel.this);
	fontChooser.resetFontSize(24);
	fontChooser.resetFontStyle(Font.PLAIN);
	fontChooserBt.addMouseListener(new MouseAdapter()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			if (fontChooser == null)
				fontChooser = new /*Gen*/FontChooser(GenImgPanel.this);
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
			f.setSize(300, 320);
			colorChooser = new JColorChooser();
			// f.getContentPane().add(BorderLayout.CENTER, colorChooser);
			f.getContentPane().add(BorderLayout.CENTER, colorChooser);
			colorChooser.setVisible(true);
			f.setVisible(true);
			// f.addComponentListener(new ComponentListener());
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
	genFileFilter.addExtension("gif");
	genFileFilter.addExtension("GIF");
	genFileFilter.addExtension("jpg");
	genFileFilter.addExtension("JPG");
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
					genFileChooser.showOpenDialog(GenImgPanel.this);
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

	transformControlPanel = new JPanel(new BorderLayout());
	transformControlPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	transformControlPanel.setForeground(Color.black);
	transformControlPanel.setBackground(guiBGColor);
	transformControlPanel.setBorder(
		new TitledBorder(border, "Transform Controls:"));
	add(BorderLayout.WEST, transformControlPanel);

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
				GenImgPanel.this.setFigureScale(val);
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

	scaleCB.setActionCommand("Scale Figure");
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
				GenImgPanel.this.setFigureScale(val);
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

	transformBtPanel = new JPanel(new GridLayout(16, 1));
	transformBtPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	transformBtPanel.setForeground(Color.black);
	transformBtPanel.setBackground(guiBGColor);

	cartesianCoordsCB = new JCheckBox();
	cartesianCoordsCB.setSelected(true);
	cartesianCoordsCB.setText("show coords");
	cartesianCoordsCB.setActionCommand("show coords");
	cartesianCoordsCB.setFont(new Font("Dialog", Font.BOLD, 12));
	cartesianCoordsCB.setForeground(Color.black);
	cartesianCoordsCB.setBackground(guiBGColor);
	transformBtPanel.add(cartesianCoordsCB);
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
	showMousePosCB.setFont(new Font("Dialog", Font.BOLD, 12));
	showMousePosCB.setForeground(Color.black);
	showMousePosCB.setBackground(guiBGColor);
	transformBtPanel.add(showMousePosCB);

	landscapeModeCB = new JCheckBox();
	landscapeModeCB.setSelected(false);
	landscapeModeCB.setText("landscape mode");
	landscapeModeCB.setActionCommand("landscape mode");
	landscapeModeCB.setFont(new Font("Dialog", Font.BOLD, 12));
	landscapeModeCB.setForeground(Color.black);
	landscapeModeCB.setBackground(guiBGColor);
	transformBtPanel.add(landscapeModeCB);
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

				GenImgPanel.this.getViewImgCanvas().setPreferredSize(
					new Dimension(drawObjectImgW, drawObjectImgH));
				genImgScrollPane.setViewportView(GenImgPanel.this.getViewImgCanvas());

				renderDrawObjectView();
			}
			catch (Exception e)
			{
				handleException("Exception in landscapeModeCB.addItemListener:", e, 101);
			}
		}
	});

	transformControlPanel.add(BorderLayout.CENTER, transformBtPanel);

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
	genImgScrollPane.setBorder(new TitledBorder(border, "Test Only:"));
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
runTestBt()
{
}

public void
doViewImgMousePressed()
{
}

public void
doViewImgMouseReleased()
{
}

public void
doViewImgMouseDragged()
{
}

public void
doViewImgMouseMoved()
{
}

public void
createDrawList()
throws Exception
{
	// createCircleDrawList();
	// createTriangleDrawList();
	// createParallelogramDrawList();
	// createLineDrawList();
	// createSheetMusicDrawList();
	// createStringDrawList();

	// NEED to be able to specify to draw bounding box and to scale
	createNucFontTestDrawList();
	// createImageDrawList();
	// createCharTestDrawList();
}

private void
createImageDrawList()
throws Exception
{
	BufferedImage bufImg = new BufferedImage(120, 30, BufferedImage.TYPE_INT_RGB);
	getDrawObjectList().add(new DrawImageObject(10.0, 10.0, bufImg));
}

private void
createCircleDrawList()
throws Exception
{
	/*
	float dotOnUnits = 1.0f/(float)this.getFigureScale();
	float dotOffUnits = 1.0f/(float)this.getFigureScale();
	float[] dashVals = new float[]{dotOnUnits, dotOffUnits};
	BasicStroke dotStroke = new BasicStroke(
		0.0f, // linewidth
		BasicStroke.CAP_BUTT,
		BasicStroke.JOIN_MITER,
		1.0f, // miterlimit
		dashVals,
		0.1f);
	*/

	/*
	getCurrentGraphics2D().setStroke(GraphicsUtil.getDotStroke((float)this.getFigureScale()));
	Line2D.Double line = new Line2D.Double(0.0, -10.0, 20.0, -10.0);
	getCurrentGraphics2D().draw(line);
	line = new Line2D.Double(10.0, -20.0, 10.0, 20.0);
	getCurrentGraphics2D().draw(line);
	*/

	GraphicsUtil.drawDotCross(10.0, 10.0, 10.0,
		(float)this.getFigureScale(), getCurrentGraphics2D());

	getCurrentGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

	getDrawObjectList().add(new DrawCircleObject(10.0, 10.0, 4.0, 0.0, 360.0,
		Color.black, true, 0.0));
}

private void
createTriangleDrawList()
throws Exception
{
	/*
	GraphicsUtil.drawDotCross(-10.0, 10.0, 10.0,
		(float)this.getFigureScale(), getCurrentGraphics2D());
	*/

	getCurrentGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

	getDrawObjectList().add(new DrawTriangleObject(-10.0, 10.0,
		0.0, 4.0, -4.0, -4.0, 4.0, -4.0, 180.0, 2.0/this.getFigureScale(), true, Color.black));
}

// p 208.0 -308.0 0.0 0.0 10.0 90.0 10.0 0 0.0 0 c4ff4e
private void
createParallelogramDrawList()
throws Exception
{
	/*
	GraphicsUtil.drawDotCross(-10.0, 10.0, 10.0,
		(float)this.getFigureScale(), getCurrentGraphics2D());
	*/

	getCurrentGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

	getDrawObjectList().add(new DrawParallelogramObject(-10.0, 10.0,
		10.0, 0.0, 10.0, 90.0, 1.0, true, Color.black));
}

private void
createLineDrawList()
throws Exception
{
	/*
	GraphicsUtil.drawDotCross(-10.0, 10.0, 10.0,
		(float)this.getFigureScale(), getCurrentGraphics2D());
	*/

	getDrawObjectList().add(new DrawLineObject(-20.0, 20.0, 10.0, -10.0,
		1.0, Color.orange/*, GraphicsUtil.lineAliasedRenderHints*/));
}

private void
createSheetMusicDrawList()
throws Exception
{
	getCurrentGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_OFF);

	// this.currentViewX = ((double)(x - getInitTransX()))/this.getFigureScale();

	double topGap = -64.0;
	double sideGap = 0.0;
	double staffXShift = 40.0;
	double staffStartX = staffXShift + (-((double)GraphicsUtil.PAPER_WIDTH_MINUS_1_INCH_BORDER)/2.0 + sideGap)/this.getFigureScale();
	double staffEndX = staffXShift + (((double)GraphicsUtil.PAPER_WIDTH_MINUS_1_INCH_BORDER)/2.0 - sideGap)/this.getFigureScale();

	double staffStartY = (((double)GraphicsUtil.PAPER_HEIGHT_MINUS_1_INCH_BORDER)/2.0 - topGap)/this.getFigureScale();
	double stringLineJump = 4.0; // gap between lines in strings
	double staffLineJump = 6.0; // gap between lines in staff
	double stringsHeight = 24.0;
	double stringStaffJump = 18.0;
	double nextStaffJump = 20.0;

	for (int i = 0;i < 9;i++)
		drawStringStaff(staffStartX, staffEndX,
		staffStartY - (double)i*(nextStaffJump + stringsHeight + stringStaffJump + 4.0*staffLineJump),
		stringLineJump, staffLineJump, stringsHeight, stringStaffJump);
}

private void
drawStringStaff(double staffStartX, double staffEndX, double staffStartY,
	double stringLineJump, double staffLineJump, double stringsHeight,
	double stringStaffJump)
throws Exception
{

	drawAllStrings(staffStartX, staffEndX, staffStartY, stringsHeight, stringLineJump);
	drawStaff(staffStartX, staffEndX, staffStartY - stringsHeight - stringStaffJump, staffLineJump);
}

private void
drawAllStrings(double staffStartX, double staffEndX, double staffStartY, double stringsHeight, double stringLineJump)
throws Exception
{
	double jump = (staffEndX - staffStartX)/12.0;
	double gap = 6.0;

	drawStrings(staffStartX + 1.0*jump - gap, staffStartY, stringsHeight, stringLineJump);
	drawStrings(staffStartX + 2.0*jump + gap, staffStartY, stringsHeight, stringLineJump);
	drawStrings(staffStartX + 4.0*jump - gap, staffStartY, stringsHeight, stringLineJump);
	drawStrings(staffStartX + 5.0*jump + gap, staffStartY, stringsHeight, stringLineJump);
	drawStrings(staffStartX + 7.0*jump - gap, staffStartY, stringsHeight, stringLineJump);
	drawStrings(staffStartX + 8.0*jump + gap, staffStartY, stringsHeight, stringLineJump);
	drawStrings(staffStartX + 10.0*jump - gap, staffStartY, stringsHeight, stringLineJump);
	drawStrings(staffStartX + 11.0*jump + gap, staffStartY, stringsHeight, stringLineJump);
}

private void
drawStrings(double stringsStartX, double stringsStartY, double stringsHeight,
	double stringLineJump)
throws Exception
{
	double stringsWidth = 5.0 * stringLineJump;
	double currStaffStartX = 0.0;

	for (int i = 0;i < 6;i++)
	{
		currStaffStartX = stringsStartX + ((double)i * stringLineJump) -
			stringsWidth/2.0;
		getDrawObjectList().add(new DrawLineObject(
			currStaffStartX,
			stringsStartY,
			currStaffStartX,
			stringsStartY - stringsHeight,
			0.0, Color.black));
	}

	for (int i = 0;i < 4;i++)
	{
		getDrawObjectList().add(new DrawLineObject(
			stringsStartX - stringsWidth/2.0,
			stringsStartY - ((double)(i+1)*stringsHeight)/5.0,
			currStaffStartX,
			stringsStartY - ((double)(i+1)*stringsHeight)/5.0,
			0.0, Color.black));
	}
}

private void
drawStaff(double staffStartX, double staffEndX, double staffStartY, double staffLineJump)
throws Exception
{
	// double staffLineJump = 6.0;
	double currStaffLineY = 0.0;

	for (int i = 0;i < 5;i++)
	{
		currStaffLineY = staffStartY - ((double)i * staffLineJump);
		getDrawObjectList().add(new DrawLineObject(
			staffStartX,
			currStaffLineY,
			staffEndX,
			currStaffLineY,
			0.0, Color.black));
	}

	getDrawObjectList().add(new DrawLineObject(
		staffStartX + (staffEndX - staffStartX)/4.0,
		staffStartY,
		staffStartX + (staffEndX - staffStartX)/4.0,
		currStaffLineY,
		0.0, Color.black));

	getDrawObjectList().add(new DrawLineObject(
		staffStartX + (staffEndX - staffStartX)/2.0,
		staffStartY,
		staffStartX + (staffEndX - staffStartX)/2.0,
		currStaffLineY,
		0.0, Color.black));

	getDrawObjectList().add(new DrawLineObject(
		staffStartX + 3.0*(staffEndX - staffStartX)/4.0,
		staffStartY,
		staffStartX + 3.0*(staffEndX - staffStartX)/4.0,
		currStaffLineY,
		0.0, Color.black));
}


/*
private void
createParallelogramDrawList()
{
	GraphicsUtil.drawDotCross(10.0, 10.0, 10.0,
		(float)this.getFigureScale(), getCurrentGraphics2D());

	getCurrentGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

	getDrawObjectList().add(new DrawParallelogramObject(10.0, 10.0,
		0.0, 5.0, 90.0, 5.0, 0.0, true, Color.black));
}
*/

/*
Rectangle2D src1 = null;
Rectangle2D src2 = null;
*/

private void
createNucFontTestDrawList()
throws Exception
{
	/*
	getCurrentGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	*/
	this.setDrawObjectRenderHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON));

	int fontSize = 24;
	DrawCharObject tmp = null;
	getDrawObjectList().add(tmp = new DrawCharObject(10.0, 10.0, 'A'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	/*
	src1 = tmp.getBoundingBox();
	*/
	getDrawObjectList().add(tmp = new DrawCharObject(10.0, -10.0, 'U'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	/*
	src2 = tmp.getBoundingBox();

	// Rectangle2D dest = new Rectangle2D();
	
	// Rectangle2D.union(src1, src2, dest);
	// src1.createUnion(src2);
	src1.add(src2);
	*/

	getDrawObjectList().add(tmp = new DrawCharObject(-10.0, -10.0, 'G'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	tmp.getShowBoundingShape();
	getDrawObjectList().add(tmp = new DrawCharObject(-10.0, 10.0, 'C'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	tmp.getShowBoundingShape();
	getDrawObjectList().add(tmp = new DrawCharObject(-30.0, 10.0, 'R'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	tmp.getShowBoundingShape();
	getDrawObjectList().add(tmp = new DrawCharObject(-30.0, -10.0, 'Y'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	tmp.getShowBoundingShape();
	getDrawObjectList().add(tmp = new DrawCharObject(30.0, 10.0, 'N'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	tmp.getShowBoundingShape();
}

private void
createStringDrawList()
throws Exception
{
	getCurrentGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

	DrawStringObject tmp = null;
	/*
	getDrawObjectList().add(tmp = new DrawStringObject(20.0, 20.0,
		new Font("Helvetica", Font.PLAIN, 12),
		Color.red, GraphicsUtil.stringAliasedRenderHints, "This is Test 0"));

	getDrawObjectList().add(tmp = new DrawStringObject(-20.0, -20.0,
		new Font("Helvetica", Font.PLAIN, 20),
		Color.red, GraphicsUtil.stringAliasedRenderHints, "This is Test 0"));
	*/

	getDrawObjectList().add(tmp = new DrawStringObject(0.0, 0.0,
		new Font("Helvetica", Font.PLAIN, 20),
		Color.red, /*GraphicsUtil.stringAliasedRenderHints,*/ "This is Test 1"));
		/*
		Color.red, GraphicsUtil.stringAliasedRenderHints, "14001"));
		*/
	tmp.setShowBoundingShape(true);
}

private void
createCharTestDrawList()
throws Exception
{
	getCurrentGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

	int fontSize = 24;
	DrawCharObject tmp = null;
	// open diamond
	getDrawObjectList().add(tmp = new DrawCharObject(10.0, 10.0, '\u2662'));
	tmp.setFont(new Font("Arial Unicode MS", Font.PLAIN, fontSize));

	// open triangle
	getDrawObjectList().add(tmp = new DrawCharObject(10.0, -10.0, '\u25bd'));
	tmp.setFont(new Font("SimSun", Font.PLAIN, fontSize));

	/* looks bad
	getDrawObjectList().add(tmp = new DrawCharObject(-10.0, -10.0, '\u25bd'));
	tmp.setFont(new Font("Arial Unicode MS", Font.PLAIN, fontSize));

	getDrawObjectList().add(tmp = new DrawCharObject(-10.0, 10.0, '\u25c7'));
	tmp.setFont(new Font("Arial Unicode MS", Font.PLAIN, fontSize));
	*/

	/*
	getDrawObjectList().add(tmp = new DrawCharObject(-30.0, 10.0, 'R'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	getDrawObjectList().add(tmp = new DrawCharObject(-30.0, -10.0, 'Y'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	getDrawObjectList().add(tmp = new DrawCharObject(30.0, 10.0, 'N'));
	tmp.setFont(new Font("Helvetica", Font.PLAIN, fontSize));
	*/
}

public void
updateDrawList(Graphics2D g2)
throws Exception
{
	for (Enumeration e = getDrawObjectList().elements();e.hasMoreElements();)
	{
		Object drawObject = e.nextElement();
		if (drawObject instanceof DrawObject)
			((DrawObject)drawObject).update(g2);
	}
}

public void
runSetFromInputFile()
throws Exception
{
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
	System.out.println("GenImgPanel-> " + s);
}

}


