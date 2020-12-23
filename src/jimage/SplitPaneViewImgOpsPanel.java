package jimage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
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
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.GraphicsUtil;

public class SplitPaneViewImgOpsPanel
extends DrawObjectView
{

public JComboBox		scaleCB = null;
public JTextField		scaleTF = null;

protected JCheckBox         showMousePosCB = null;

private JSplitPane splitPane = null;
private JScrollPane lftImgScrollPane = null;
private JScrollPane rhtImgScrollPane = null;
private BevelBorder border = null;

private JScrollPane	spSystemOut = null;
private JTextPane	taSystemOut = null;

private JPanel	controlPanel = null;
private JPanel	transformControlPanel = null;
private JPanel	transformBtPanel = null;
private JTextField widthTF = null;
private JTextField heightTF = null;
private JButton cropToWinBt = null;
private JButton setDimensionFromWinBt = null;
private JButton applyDimensionBt = null;
private JRadioButton cropRB = null;
private JRadioButton cropUpperLeftRB = null;
private JRadioButton cropLowerRightRB = null;

private JTextField renderScaleTF = null;
private JButton renderScaleBt = null;

private JTextField renderRotateTF = null;
private JButton renderRotateBt = null;

private JButton edgeDetectBt = null;

private ViewImgCanvas viewLftImgCanvas = null;
private ViewImgCanvas viewRhtImgCanvas = null;

private BufferedImage lftBaseImg = null;
private BufferedImage rhtBaseImg = null;

private int upperLeftMouseX = 0;
private int upperLeftMouseY = 0;
private int lowerRightMouseX = 0;
private int lowerRightMouseY = 0;

public
SplitPaneViewImgOpsPanel(double scaleVal, JFrame parent)
{
	this.setFigureScale(scaleVal);
	frameParent = parent;

	drawObjectImgW = 2000;
	drawObjectImgH = 2000;

	try
	{
		setGuiStartX(400);
		setGuiStartY(30);
		setGuiWidth(800);
		setGuiHeight(600);

		setDrawImgBGColor(Color.white);

		buildGui();
		widthTF.setText("" + lftBaseImg.getWidth());
		heightTF.setText("" + lftBaseImg.getHeight());
	}
	catch(Exception e)
	{
		handleException("Exception from Constructor: ", e, 1);
	}
}

public
SplitPaneViewImgOpsPanel()
{
	this(1.0, null);
}

static public void
main(String args[])
{
	try
	{
		(new SplitPaneViewImgOpsPanel(1.0, null)).setVisible(true);
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

	writeBt = new JButton();
	writeBt.setText("Write");
	writeBt.setActionCommand("Write");
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
					genFileChooser.showSaveDialog(SplitPaneViewImgOpsPanel.this);
				if (returnVal == JFileChooser.ERROR_OPTION)
					debug("ERROR IN OPENING FILE");
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;

				File selectedFile = genFileChooser.getSelectedFile();
				String selectedFileName = selectedFile.getName();
	
				File genImgOutFile = genFileChooser.getSelectedFile();
				debug("WORKING ON: " + genImgOutFile.toString() + " " +
					genImgOutFile.canWrite());
					
				debug("PRNTING IMG SIZE: " + rhtBaseImg.getWidth() + " " + rhtBaseImg.getHeight());
					running = false;
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
	fontChooser = new /*Gen*/FontChooser(SplitPaneViewImgOpsPanel.this);
	fontChooser.resetFontSize(24);
	fontChooser.resetFontStyle(Font.PLAIN);
	fontChooserBt.addMouseListener(new MouseAdapter()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			if (fontChooser == null)
				fontChooser = new /*Gen*/FontChooser(SplitPaneViewImgOpsPanel.this);
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
			f.setBounds(10, 60, 300, 460);

			colorChooser = new JColorChooser(new Color(0xff333399));

			AbstractColorChooserPanel[] acList =
				colorChooser.getChooserPanels();
			AbstractColorChooserPanel[] newACList =
				new AbstractColorChooserPanel[3];
			newACList[0] = new HexColorChooserPanel();
			newACList[1] = acList[1];
			newACList[2] = acList[0];
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
				}
			});

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
					genFileChooser.showOpenDialog(SplitPaneViewImgOpsPanel.this);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;
				
				setCurrentInputFile(genFileChooser.getSelectedFile());

				debug("Opening: " + getCurrentInputFile().getName() +
					"." + "\n");

				/*
				lftImgScrollPane.setBorder(new TitledBorder(border, "View "
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
				setFigureScale(val);
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
				setFigureScale(val);
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

	transformBtPanel = new JPanel(new GridLayout(17, 1));
	transformBtPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	transformBtPanel.setForeground(Color.black);
	transformBtPanel.setBackground(guiBGColor);

	cartesianCoordsCB = new JCheckBox();
	cartesianCoordsCB.setSelected(false);
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

				viewLftImgCanvas.setPreferredSize(
					new Dimension(drawObjectImgW, drawObjectImgH));
				lftImgScrollPane.setViewportView(viewLftImgCanvas);

				viewRhtImgCanvas.setPreferredSize(
					new Dimension(drawObjectImgW, drawObjectImgH));
				rhtImgScrollPane.setViewportView(viewRhtImgCanvas);

				renderDrawObjectView();
			}
			catch (Exception e)
			{
				handleException("Exception in landscapeModeCB.addItemListener:", e, 101);
			}
		}
	});

	widthTF = new JTextField("120", 3);
	widthTF.setForeground(Color.black);
	widthTF.setBackground(Color.white);
	transformBtPanel.add(widthTF);
	widthTF.setVisible(true);
	widthTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				setToImgWidth(Integer.parseInt(widthTF.getText()));
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in widthTF.actionPerformed:", e, 101);
			}
		}
	});

	heightTF = new JTextField("30", 3);
	heightTF.setForeground(Color.black);
	heightTF.setBackground(Color.white);
	transformBtPanel.add(heightTF);
	heightTF.setVisible(true);
	heightTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				setToImgHeight(Integer.parseInt(heightTF.getText()));
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in heightTF.actionPerformed:", e, 101);
			}
		}
	});

	/*
	applyDimensionBt = new JButton("Apply Dimension");
	applyDimensionBt.setActionCommand("Apply Dimension");
	applyDimensionBt.setFont(new Font("Dialog", Font.BOLD, 12));
	applyDimensionBt.setForeground(Color.black);
	applyDimensionBt.setBackground(guiBGColor);
	transformBtPanel.add(applyDimensionBt);
	applyDimensionBt.setVisible(true);
	applyDimensionBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
	*/
				/* can't seem to get to work:
				ParameterBlock params = new ParameterBlock();
				debug("lftBaseImg: " + lftBaseImg.getMinX() + " " +
					lftBaseImg.getMinY() + " " +
					lftBaseImg.getWidth() + " " +
					lftBaseImg.getHeight());
				params.addSource(lftBaseImg);
				params.add(20.0f); // x pos in lft image
				params.add(20.0f); // y pos in lft image
				// params.add(Float.parseFloat(widthTF.getText())); // x translate
				// params.add(Float.parseFloat(heightTF.getText())); // y translate
				params.add(20.0f); // width in lft image
				params.add(20.0f); // height in lft image

				// params.add(interp);       // interpolation method
				StringBuffer strBuf = new StringBuffer();
				if (
				(new CropDescriptor()).validateArguments("crop",
					params, strBuf))
				{
					RenderedOp toRenderedOp = JAI.create("crop", params);
					rhtBaseImg = toRenderedOp.getAsBufferedImage();
					drawRoot();
				}
				else
				{
					debug("ERR: " + strBuf.toString());
				}
				*/

				/*
				rhtBaseImg = lftBaseImg.getSubimage(
					imgSpaceX(getCurrentViewX()),
					imgSpaceY(getCurrentViewY()),
					Integer.parseInt(widthTF.getText()),
					Integer.parseInt(heightTF.getText()));

				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in applyDimensionBt.actionPerformed:", e, 101);
			}
		}
	});
	*/

	ButtonGroup renderBtGroup = new ButtonGroup();

	cropRB = new JRadioButton();
	cropRB.setSelected(false);
	cropRB.setText("crop with mouse");
	cropRB.setActionCommand("crop with mouse");
	cropRB.setFont(new Font("Dialog", Font.BOLD, 12));
	cropRB.setForeground(Color.black);
	cropRB.setBackground(guiBGColor);
	renderBtGroup.add(cropRB);
	transformBtPanel.add(cropRB);

	cropUpperLeftRB = new JRadioButton();
	cropUpperLeftRB.setSelected(false);
	cropUpperLeftRB.setText("pick upper left");
	cropUpperLeftRB.setActionCommand("pick_upper_left");
	cropUpperLeftRB.setFont(new Font("Dialog", Font.BOLD, 12));
	cropUpperLeftRB.setForeground(Color.black);
	cropUpperLeftRB.setBackground(guiBGColor);
	renderBtGroup.add(cropUpperLeftRB);
	transformBtPanel.add(cropUpperLeftRB);

	cropLowerRightRB = new JRadioButton();
	cropLowerRightRB.setSelected(false);
	cropLowerRightRB.setText("pick lower right");
	cropLowerRightRB.setActionCommand("pick_lower_right");
	cropLowerRightRB.setFont(new Font("Dialog", Font.BOLD, 12));
	cropLowerRightRB.setForeground(Color.black);
	cropLowerRightRB.setBackground(guiBGColor);
	renderBtGroup.add(cropLowerRightRB);
	transformBtPanel.add(cropLowerRightRB);

// private JButton setDimensionFromWinBt = null;

	JButton cropUpperLeftBt = new JButton();
	cropUpperLeftBt.setSelected(false);
	cropUpperLeftBt.setText("place upper left");
	cropUpperLeftBt.setActionCommand("place_upper_left");
	cropUpperLeftBt.setFont(new Font("Dialog", Font.BOLD, 12));
	cropUpperLeftBt.setForeground(Color.black);
	cropUpperLeftBt.setBackground(guiBGColor);
	transformBtPanel.add(cropUpperLeftBt);
	cropUpperLeftBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				/*
				Rectangle rect = lftImgScrollPane.getViewportBorderBounds();
				rhtBaseImg = lftBaseImg.getSubimage(
					lftImgScrollPane.getHorizontalScrollBar().getValue(),
					lftImgScrollPane.getVerticalScrollBar().getValue(),
					(int)Math.round(rect.getWidth() - 1),
					(int)Math.round(rect.getHeight() - 1));
				*/
				/*
				rhtBaseImg = lftBaseImg.getSubimage(
					upperLeftMouseX, upperLeftMouseY,
					lowerRightMouseX - upperLeftMouseX + 1, lowerRightMouseY - upperLeftMouseY + 1);
				drawRoot();
				*/
				upperLeftMouseX = lftImgScrollPane.getHorizontalScrollBar().getValue();
				upperLeftMouseY = lftImgScrollPane.getVerticalScrollBar().getValue();
			}
			catch (Exception e)
			{
				handleException("Exception in cropToWinBt.actionPerformed:", e, 101);
			}
		}
	});


	JButton cropLowerRightBt = new JButton();
	cropLowerRightBt.setSelected(false);
	cropLowerRightBt.setText("place lower right");
	cropLowerRightBt.setActionCommand("place_lower_right");
	cropLowerRightBt.setFont(new Font("Dialog", Font.BOLD, 12));
	cropLowerRightBt.setForeground(Color.black);
	cropLowerRightBt.setBackground(guiBGColor);
	transformBtPanel.add(cropLowerRightBt);
	
	
	cropLowerRightBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				Rectangle rect = lftImgScrollPane.getViewportBorderBounds();
				
				lowerRightMouseX = lftImgScrollPane.getHorizontalScrollBar().getValue() + (int)Math.round(rect.getWidth() - 1);
				lowerRightMouseY = lftImgScrollPane.getVerticalScrollBar().getValue() + (int)Math.round(rect.getHeight() - 1);


				ParameterBlock params = new ParameterBlock();
				params.add((float)(upperLeftMouseX));
				params.add((float)(upperLeftMouseY));
				params.add((float)(lowerRightMouseX - upperLeftMouseX + 1));
				params.add((float)(lowerRightMouseY - upperLeftMouseY + 1));

				try
				{
					drawRoot();
				}
				catch (IllegalArgumentException iae)
				{
					printConsole("ERROR: " + iae.toString());
				}
			}
			catch (Exception e)
			{
				handleException("Exception in cropToWinBt.actionPerformed:", e, 101);
			}
		}
	});

	cropToWinBt = new JButton("Crop to Window");
	cropToWinBt.setActionCommand("Crop to Window");
	cropToWinBt.setFont(new Font("Dialog", Font.BOLD, 12));
	cropToWinBt.setForeground(Color.black);
	cropToWinBt.setBackground(guiBGColor);
	transformBtPanel.add(cropToWinBt);
	cropToWinBt.setVisible(true);
	cropToWinBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				rhtBaseImg = lftBaseImg.getSubimage(
					upperLeftMouseX, upperLeftMouseY,
					lowerRightMouseX - upperLeftMouseX + 1, lowerRightMouseY - upperLeftMouseY + 1);
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in cropToWinBt.actionPerformed:", e, 101);
			}
		}
	});

	renderScaleTF = new JTextField("0.5");
	renderScaleTF.setForeground(Color.black);
	renderScaleTF.setBackground(Color.white);
	transformBtPanel.add(renderScaleTF);
	renderScaleTF.setVisible(true);

	renderScaleBt = new JButton("Render Scale");
	renderScaleBt.setActionCommand("Render Scale");
	renderScaleBt.setFont(new Font("Dialog", Font.BOLD, 12));
	renderScaleBt.setForeground(Color.black);
	renderScaleBt.setBackground(guiBGColor);
	transformBtPanel.add(renderScaleBt);
	renderScaleBt.setVisible(true);
	renderScaleBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				float val = Float.parseFloat(renderScaleTF.getText());

				ParameterBlock params = new ParameterBlock();
				params.addSource(lftBaseImg);
				params.add(val);         // x scale factor
				params.add(val);         // y scale factor
				params.add(0.0F);         // x translate
				params.add(0.0F);         // y translate
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in renderScaleBt.actionPerformed:", e, 101);
			}
		}
	});

	renderRotateTF = new JTextField("90.0");
	renderRotateTF.setForeground(Color.black);
	renderRotateTF.setBackground(Color.white);
	transformBtPanel.add(renderRotateTF);
	renderRotateTF.setVisible(true);

	renderRotateBt = new JButton("Render Rotate");
	renderRotateBt.setActionCommand("Render Rotate");
	renderRotateBt.setFont(new Font("Dialog", Font.BOLD, 12));
	renderRotateBt.setForeground(Color.black);
	renderRotateBt.setBackground(guiBGColor);
	transformBtPanel.add(renderRotateBt);
	renderRotateBt.setVisible(true);
	renderRotateBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				float val = Float.parseFloat(renderRotateTF.getText());

				float angle = (float)(val * (Math.PI/180.0F));

				ParameterBlock params = new ParameterBlock();
				params.addSource(lftBaseImg);           // The source image
				params.add(0.0F);                       // The x origin
				params.add(0.0F);                       // The y origin
				params.add(angle);                      // The rotation angle
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in renderRotateBt.actionPerformed:", e, 101);
			}
		}
	});

	edgeDetectBt = new JButton("Detect Edge");
	edgeDetectBt.setActionCommand("Detect Edge");
	edgeDetectBt.setFont(new Font("Dialog", Font.BOLD, 12));
	edgeDetectBt.setForeground(Color.black);
	edgeDetectBt.setBackground(guiBGColor);
	transformBtPanel.add(edgeDetectBt);
	edgeDetectBt.setVisible(true);
	edgeDetectBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				drawRoot();
			}
			catch (Exception e)
			{
				handleException("Exception in edgeDetectBt.actionPerformed:", e, 101);
			}
		}
	});

	transformControlPanel.add(BorderLayout.CENTER, transformBtPanel);

	lftBaseImg = new BufferedImage(drawObjectImgW, drawObjectImgH,
		/*
		BufferedImage.TYPE_INT_ARGB);
		BufferedImage.TYPE_INT_RGB);
		*/
		BufferedImage.TYPE_BYTE_INDEXED);
	Graphics2D g2 = lftBaseImg.createGraphics();
	g2.setColor(Color.black);
	g2.setRenderingHints(GraphicsUtil.imageRenderHints);
	g2.fill(new Rectangle(0, 0, drawObjectImgW, drawObjectImgH));

	viewLftImgCanvas = new ViewImgCanvas(lftBaseImg);
	viewLftImgCanvas.setBackground(guiBGColor);
	viewLftImgCanvas.setOpaque(true);
	viewLftImgCanvas.setPreferredSize(new Dimension(drawObjectImgW, drawObjectImgH));

	rhtBaseImg = new BufferedImage(drawObjectImgW, drawObjectImgH,
		/*
		BufferedImage.TYPE_INT_ARGB);
		BufferedImage.TYPE_INT_RGB);
		*/
		BufferedImage.TYPE_BYTE_INDEXED);
	g2 = rhtBaseImg.createGraphics();
	g2.setColor(Color.black);
	g2.setRenderingHints(GraphicsUtil.imageRenderHints);
	g2.fill(new Rectangle(0, 0, drawObjectImgW, drawObjectImgH));

	viewRhtImgCanvas = new ViewImgCanvas(rhtBaseImg);
	viewRhtImgCanvas.setBackground(guiBGColor);
	viewRhtImgCanvas.setOpaque(true);
	viewRhtImgCanvas.setPreferredSize(new Dimension(drawObjectImgW, drawObjectImgH));

	lftImgScrollPane = new JScrollPane(viewLftImgCanvas,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	lftImgScrollPane.setBorder(new TitledBorder(border, "View Img:"));
	lftImgScrollPane.setBackground(guiBGColor);
	viewLftImgCanvas.setVisible(true);
	lftImgScrollPane.setVisible(true);

	rhtImgScrollPane = new JScrollPane(viewRhtImgCanvas,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	rhtImgScrollPane.setBorder(new TitledBorder(border, "View Img:"));
	rhtImgScrollPane.setBackground(guiBGColor);
	viewRhtImgCanvas.setVisible(true);
	rhtImgScrollPane.setVisible(true);

	setMouseMethod();

	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		lftImgScrollPane, rhtImgScrollPane);
	splitPane.setContinuousLayout(true);
	splitPane.setOneTouchExpandable(true);
	splitPane.setDividerLocation(BASE_IMG_WIDTH/2);
	splitPane.setVisible(true);

	add(BorderLayout.CENTER, splitPane);

	spSystemOut = new JScrollPane();
	spSystemOut.setOpaque(true);
	spSystemOut.setBounds(6, 123, 463, 100);
	spSystemOut.setFont(new Font("Dialog", Font.PLAIN, 12));
	spSystemOut.setForeground(Color.black);
	spSystemOut.setBackground(guiBGColor);
	spSystemOut.setBorder(new TitledBorder(border, "System Out"));
	taSystemOut = new JTextPane();
	taSystemOut.setMargin(new java.awt.Insets(0, 0, 0, 0));
	taSystemOut.setBounds(43, 41, 347, 106);
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
setMouseMethod()
{
	viewLftImgCanvas.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			setCurrentViewX(event.getX());
			setCurrentViewY(event.getY());

			setMousePressedImgSpaceX(event.getX());
			setMousePressedImgSpaceY(event.getY());

			int mod = event.getModifiers();
			setCurrentMouseState(mod);

			doViewLftImgMousePressed();
		}

		public void
		mouseReleased(MouseEvent event)
		{
			doViewLftImgMouseReleased();
		}
	});

	viewLftImgCanvas.addMouseMotionListener(new MouseMotionListener()
	{
		public void
		mouseDragged(MouseEvent event)
		{
			if (showMousePosCB.isSelected())
			{
				setCurrentViewX(event.getX());
				setCurrentViewY(event.getY());
				printCurrentLftMousePosInfo();
			}
		}

		public void
		mouseMoved(MouseEvent event)
		{
			if (showMousePosCB.isSelected())
			{
				setCurrentViewX(event.getX());
				setCurrentViewY(event.getY());
				printCurrentLftMousePosInfo();
			}
		}
	});

	viewRhtImgCanvas.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			setCurrentViewX(event.getX());
			setCurrentViewY(event.getY());

			setMousePressedImgSpaceX(event.getX());
			setMousePressedImgSpaceY(event.getY());

			int mod = event.getModifiers();
			setCurrentMouseState(mod);

			doViewRhtImgMousePressed();
		}

		public void
		mouseReleased(MouseEvent event)
		{
			doViewRhtImgMouseReleased();
		}
	});

	viewRhtImgCanvas.addMouseMotionListener(new MouseMotionListener()
	{
		public void
		mouseDragged(MouseEvent event)
		{
			if (showMousePosCB.isSelected())
			{
				setCurrentViewX(event.getX());
				setCurrentViewY(event.getY());
				printCurrentRhtMousePosInfo();
			}
		}

		public void
		mouseMoved(MouseEvent event)
		{
			if (showMousePosCB.isSelected())
			{
				setCurrentViewX(event.getX());
				setCurrentViewY(event.getY());
				printCurrentRhtMousePosInfo();
			}
		}
	});
}

public void
printCurrentLftMousePosInfo()
{
	Rectangle rect = lftImgScrollPane.getViewportBorderBounds();
	printConsole("color: " + "0x" +
		// print out color hex val
		Integer.toHexString(viewLftImgCanvas.getImgPixelAt(
			imgSpaceX(getCurrentViewX()),
			imgSpaceY(getCurrentViewY())) & 0x00ffffff) + " " +
		/* print out color int val
		(viewLftImgCanvas.getImgPixelAt(
			imgSpaceX(getCurrentViewX()),
			imgSpaceY(getCurrentViewY())) & 0x00ffffff) + " " +
		*/
		// print out img space x,y of mouse position
		"img x,y: " + 
			imgSpaceX(getCurrentViewX()) + " " +
			imgSpaceY(getCurrentViewY()) + " " +
		/* print out user space x,y of mouse position (with cartesian 0,0 in center)
		"view x,y: " +
			util.StringUtil.truncateVal(getCurrentViewX(), 1)
			+ " " +
			util.StringUtil.truncateVal(-getCurrentViewY(), 1)
		*/
		/*
		"win size: " +
		(splitPane.getLeftComponent().getWidth() - lftImgScrollPane.getVerticalScrollBar().getWidth()) + " " +
		(splitPane.getLeftComponent().getHeight() - lftImgScrollPane.getHorizontalScrollBar().getHeight()) + " " +
		*/

		/*
		"divider x: " + splitPane.getDividerLocation() + " " +
		*/
		
		/*
		"viewport: " + lftImgScrollPane.getViewportBorderBounds().toString() + " " +
		"scrollx: " + lftImgScrollPane.getHorizontalScrollBar().getValue() + " " +
		"scrolly: " + lftImgScrollPane.getVerticalScrollBar().getValue()
		*/

		"lft view: " +
			lftImgScrollPane.getHorizontalScrollBar().getValue() + " " +
			lftImgScrollPane.getVerticalScrollBar().getValue() + " " +
			((int)Math.round(rect.getWidth() - 1)) + " " +
			((int)Math.round(rect.getHeight() - 1))

		);
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
printCurrentRhtMousePosInfo()
{
	printConsole("color: " + "0x" +
		Integer.toHexString(viewRhtImgCanvas.getImgPixelAt(
			imgSpaceX(getCurrentViewX()),
			imgSpaceY(getCurrentViewY())) & 0x00ffffff) + " " +
		(viewRhtImgCanvas.getImgPixelAt(
			imgSpaceX(getCurrentViewX()),
			imgSpaceY(getCurrentViewY())) & 0x00ffffff) + " " +
		"img x,y: " + 
			imgSpaceX(getCurrentViewX()) + " " +
			imgSpaceY(getCurrentViewY()) + " " +
		"view x,y: " +
			util.StringUtil.truncateVal(getCurrentViewX(), 1)
			+ " " +
			util.StringUtil.truncateVal(-getCurrentViewY(), 1)
		);
}

public void
printCurrentMousePosInfo()
{
}

public void
drawRoot()
throws Exception
{
	if (lftBaseImg != null)
	{
		Graphics2D g2 = lftBaseImg.createGraphics();
		g2.setRenderingHints(GraphicsUtil.imageRenderHints);

		if ((lftBaseImg.getWidth() > drawObjectImgW) || (lftBaseImg.getHeight() > drawObjectImgH))
		{
			viewLftImgCanvas.setPreferredSize(
				new Dimension(lftBaseImg.getWidth(), lftBaseImg.getHeight()));
			lftImgScrollPane.updateUI();
			viewLftImgCanvas.updateUI();
		}
		viewLftImgCanvas.setImage(lftBaseImg);
		// NEED to switch to java.raster
		viewLftImgCanvas.setPixelRaster();

		viewLftImgCanvas.repaint();
	}
	if (rhtBaseImg != null)
	{
		Graphics2D g2 = rhtBaseImg.createGraphics();
		g2.setRenderingHints(GraphicsUtil.imageRenderHints);

		viewRhtImgCanvas.setImage(rhtBaseImg);
		// NEED to switch to java.raster
		viewRhtImgCanvas.setPixelRaster();

		viewRhtImgCanvas.repaint();
	}
}

public void
doViewLftImgMousePressed()
{
	if (cropUpperLeftRB.isSelected())
	{
		try
		{
			upperLeftMouseX = imgSpaceX(getCurrentViewX());
			upperLeftMouseY = imgSpaceY(getCurrentViewY());
		}
		catch (Exception e)
		{
			handleException("Exception in doViewLftImgMousePressed.cropUpperLeftRB:", e, 101);
		}
	}
	else if (cropLowerRightRB.isSelected())
	{
		try
		{
			lowerRightMouseX = imgSpaceX(getCurrentViewX());
			lowerRightMouseY = imgSpaceY(getCurrentViewY());

			rhtBaseImg = lftBaseImg.getSubimage(
				upperLeftMouseX, upperLeftMouseY,
				lowerRightMouseX - upperLeftMouseX + 1, lowerRightMouseY - upperLeftMouseY + 1);
			drawRoot();
		}
		catch (Exception e)
		{
			handleException("Exception in doViewLftImgMousePressed.cropLowerRightRB:", e, 101);
		}
	}
	else if (cropRB.isSelected())
	{
		try
		{

/*
>  Does any one know how to change a BufferedImage into a RenderedOp

It is possible to create a RenderedOp having the given
BufferedImage as a source for a "do-nothing" operation.
First get a PlanarImage from your BufferedImage:

RenderedImageAdapter ria = new RenderedImageAdapter(myBufferedImage);

then create a RenderedOp by taking that PlanarImage as a source
and using an operator that will not modify the image (for this
example, I use a Crop operator with the full size of the
image to keep the image unchanged)

   ParameterBlock pb = new ParameterBlock();
   pb.add(ria.getMinX()); // may have to cast to float (-b)
   pb.add(ria.getMinY());
   pb.add(ria.getWidth());
   pb.add(ria.getHeight());
   pbs.addSource(ria);
   RenderedOp ro=JAI.create("Crop", pb, null);

*/


			ParameterBlock params = new ParameterBlock();
			params.add((float)imgSpaceX(getCurrentViewX()));
			params.add((float)imgSpaceY(getCurrentViewY()));
			params.add(Float.parseFloat(widthTF.getText()));
			params.add(Float.parseFloat(heightTF.getText()));			
			drawRoot();
		}
		catch (Exception e)
		{
			handleException("Exception in doViewLftImgMousePressed.cropRB:", e, 101);
		}
	}
	/*
	debug(" " + imgSpaceX(getCurrentViewX()) + " " +
		imgSpaceY(getCurrentViewY()));
	*/
}

public void
doViewLftImgMouseReleased()
{
}

public void
doViewRhtImgMousePressed()
{
}

public void
doViewRhtImgMouseReleased()
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

private int toImgWidth = 0;

public void
setToImgWidth(int toImgWidth)
{
    this.toImgWidth = toImgWidth;
}

public int
getToImgWidth()
{
    return (this.toImgWidth);
}

private int toImgHeight = 0;

public void
setToImgHeight(int toImgHeight)
{
    this.toImgHeight = toImgHeight;
	rhtBaseImg = lftBaseImg.getSubimage(
		0, 0,
		lftBaseImg.getWidth(), toImgHeight);
}

public int
getToImgHeight()
{
    return (this.toImgHeight);
}

public void
createDrawList()
throws Exception
{
}

public void
runSetFromInputFile()
throws Exception
{
	if (this.getCurrentInputFile() == null)
	{
		debug("Currently no Image File Set");
		return;
	}

	cropUpperLeftRB.setSelected(false);
	cropLowerRightRB.setSelected(false);

	debug("currentfile: " + getCurrentInputFile().getName());

	if (lftImgScrollPane != null)
		lftImgScrollPane.setBorder(new TitledBorder(border, "View "
			+ genFileFilter.getBodyName(getCurrentInputFile())
			+ ":"));

	try
	{
		widthTF.setText("" + lftBaseImg.getWidth());
		heightTF.setText("" + lftBaseImg.getHeight());

	}
	catch (Exception e)
	{
		handleException("Exception in mousePressed for readImgBt:", e, 90);
		return;
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

private static void
debug(String s)
{
	System.out.println("SplitPaneViewImgOpsPanel-> " + s);
}

public ImageIcon
createImageIcon(String fileName, String description)
{
	/*
	if(getSwingSet2() != null)
	{
		return getSwingSet2().createImageIcon(filename, description);
	}
	else
	{
		String path = "/resources/images/" + filename;
		return new ImageIcon(getClass().getResource(path), description); 
	}
	*/
	return new ImageIcon(fileName, description); 
}

public void
updateAfterRender()
{
}

}
