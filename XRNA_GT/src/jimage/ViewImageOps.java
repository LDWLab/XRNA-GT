
package jimage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

import jmath.MathOps;
import util.FileUtil;
import util.GifEncoder;

class ViewImageOps
extends JFrame
{

private int guiStartX = 300;
private int guiStartY = 80;
private int guiWidth = 720;
private int guiHeight = 580;
JScrollPane fromImgScrollPane = null;
JScrollPane toImgScrollPane = null;
ViewImgCanvas fromImgCanvas = null;
ViewImgCanvas toImgCanvas = null;

protected JScrollPane       spSystemOut = null;
protected JTextPane         taSystemOut = null;
protected JPanel            controlPanel = null;
protected JPanel            imgsPanel = null;
protected JPanel            transformControlPanel = null;
protected JPanel			imgTransformPanel = null;

protected JCheckBox         wallEyedStereoCB = null;
protected JCheckBox         cartesianCoordsCB = null;
protected JRadioButton		rotateGlobalTransformBt = null;
protected JRadioButton		rotateLocalTransformBt = null;
protected JRadioButton		translateTransformBt = null;
protected JRadioButton		scaleTransformBt = null;

protected JPanel            generalBtPanel;
protected JButton           startBt = null;
protected JButton           readImgBt = null;
protected JButton           readHexFileBt = null;
protected JButton           stopBt = null;
protected JButton           printFromGIFBt = null;
protected JButton           printToGIFBt = null;
protected JButton           renderBt = null;
protected JButton           colorChooserBt = null;

protected JMenuBar          mbMain;
protected JMenu             mFile;
protected JMenuItem         miExit;
protected JColorChooser		colorChooser = null;

BevelBorder border = null;

protected JFileChooser imgFileChooser = null;
protected GenFileFilter imgFileFilter = null;

private static URL documentURL = null;

protected OutputStream gifOutputFile = null;

protected Color greyBG = new Color(-3355444);
protected Color viewImgOpsCanvasBGColor = greyBG;
protected Color viewImgOpsBorderColor = new Color(0xff8888ff);

Properties viewImgOpsImageProperties = null;

int showImgStartX = 0;
int showImgStartY = 0;
BufferedImage fromImg = null;
BufferedImage toImg = null;

int viewImgCanvasW = 200;
int viewImgCanvasH = 134;

double imgScale = 0.5;

public
ViewImageOps()
{
	try
	{
		if (viewImgOpsImageProperties == null)
			viewImgOpsImageProperties = getViewImageOpsProperties(
				"C:/XRNA/jimage/viewImgOps.properties");
		String getBGColor =
			viewImgOpsImageProperties.getProperty("bgColor");
		if (getBGColor != null)
			viewImgOpsCanvasBGColor =
				new Color(MathOps.hexStringToInt(getBGColor.substring(2)));
		buildGui();
	}
	catch(Exception e)
	{
		handleException("Exception From Constructor: ", e, 1);
	}
}

static public void
main(String args[])
{
	(new ViewImageOps()).setVisible(true);
}

protected void
buildGui()
{
	border = new BevelBorder(1);

	setTitle("ImageOps View");
	getContentPane().setLayout(new BorderLayout());
	setVisible(false);
	setBounds(guiStartX, guiStartY, guiWidth, guiHeight);
	setBackground(greyBG);

	imgsPanel = new JPanel();
	imgsPanel.setLayout(new BorderLayout(0,0));
	imgsPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	imgsPanel.setForeground(Color.black);
	imgsPanel.setBackground(viewImgOpsBorderColor);
	imgsPanel.setBorder(new TitledBorder(border, "Images Panel:"));
	getContentPane().add(BorderLayout.CENTER, imgsPanel);

	controlPanel = new JPanel();
	controlPanel.setLayout(new BorderLayout(0,0));
	controlPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	controlPanel.setForeground(Color.black);
	controlPanel.setBackground(viewImgOpsBorderColor);
	controlPanel.setBorder(new TitledBorder(border, "Main Controls:"));
	getContentPane().add(BorderLayout.NORTH, controlPanel);

	int btFontSize = 11;
	generalBtPanel = new JPanel();
	generalBtPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	generalBtPanel.setFont(new Font("Dialog", Font.PLAIN, btFontSize));
	generalBtPanel.setForeground(Color.black);
	generalBtPanel.setBackground(greyBG);
	generalBtPanel.setBorder(new TitledBorder(border, "General Controls:"));
	controlPanel.add("North", generalBtPanel);

	startBt = new JButton();
	startBt.setText("Start");
	startBt.setActionCommand("Start");
	startBt.setFont(new Font("Dialog", Font.BOLD, btFontSize));
	startBt.setForeground(Color.black);
	startBt.setBackground(greyBG);
	generalBtPanel.add(startBt);
	startBt.addMouseListener(new ViewImageOpsMouseListener()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				initViewImageOps();
			}
			catch (Exception e)
			{
				handleException("Exception in initViewImageOps:", e, 101);
			}
		}
	});

	readImgBt = new JButton();
	readImgBt.setText("Read Image");
	readImgBt.setActionCommand("Read_Image");
	readImgBt.setFont(new Font("Dialog", Font.BOLD, btFontSize));
	readImgBt.setForeground(Color.black);
	readImgBt.setBackground(greyBG);
	generalBtPanel.add(readImgBt);
	/*
	readImgBt.addMouseListener(new ViewImageOpsMouseListener()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				FileSeekableStream stream =
					new FileSeekableStream("c:/tmp2/June090.jpg");
				RenderedOp fromRenderedOp =
					JAI.create("stream", stream);
				// ParameterBlock pBlock = fromRenderedOp.getParameterBlock();
				fromImg = fromRenderedOp.getAsBufferedImage();
				int imgW = fromImg.getWidth();
				int imgH = fromImg.getHeight();
				fromImgCanvas.setPreferredSize(new Dimension(
					imgW < viewImgCanvasW ? viewImgCanvasW : imgW,
					imgH < viewImgCanvasH ? viewImgCanvasH : imgH));
				fromImgScrollPane.updateUI();
				displayFromImage();
			}
			catch (Exception e)
			{
				handleException("Exception in mousePressed for readImgBt:", e, 101);
			}
		}
	});
	*/

	imgFileChooser = new JFileChooser(".");
	imgFileFilter = new GenFileFilter();
	imgFileFilter.addExtension("gif");
	imgFileFilter.addExtension("jpg");
	imgFileFilter.addExtension("GIF");
	imgFileFilter.addExtension("JPG");
	imgFileFilter.setDescription("Image Files");
	imgFileChooser.setFileFilter(imgFileFilter);
	readImgBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent event)
		{
			int returnVal =
				imgFileChooser.showOpenDialog(ViewImageOps.this);
			if (returnVal != JFileChooser.APPROVE_OPTION)
				return;
			File imgFile = imgFileChooser.getSelectedFile();
			//this is where a real application would open the file.
			debug("Opening: " + imgFile.getName() + "." + "\n");
			fromImgScrollPane.setBorder(new TitledBorder(border,
				"View " + imgFileFilter.getBodyName(imgFile) + ":"));

			try
			{
				
				// ParameterBlock pBlock = fromRenderedOp.getParameterBlock();
	
				int imgW = fromImg.getWidth();
				int imgH = fromImg.getHeight();
				fromImgCanvas.setPreferredSize(new Dimension(
					imgW < viewImgCanvasW ? viewImgCanvasW : imgW,
					imgH < viewImgCanvasH ? viewImgCanvasH : imgH));
				fromImgScrollPane.updateUI();
				displayFromImage();
			}
			catch (Exception e)
			{
				handleException("Exception in mousePressed for readImgBt:", e, 101);
			}

		}
	});

	readHexFileBt = new JButton();
	readHexFileBt.setText("Read Hex File");
	readHexFileBt.setActionCommand("Read_Hex_File");
	readHexFileBt.setFont(new Font("Dialog", Font.BOLD, btFontSize));
	readHexFileBt.setForeground(Color.black);
	readHexFileBt.setBackground(greyBG);
	generalBtPanel.add(readHexFileBt);
	readHexFileBt.addMouseListener(new ViewImageOpsMouseListener()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				String imgString = FileUtil.getFileAsString("c:/XRNA/jimage/imgstr.txt");
				imgString = imgString.substring(0, imgString.length() - 2);
				debug("imgString.length(): " + imgString.length());
				fromImg = (BufferedImage)ImageOps.getInLineGIF(imgString);

				// might have to have a min size
				/*
				fromImgCanvas.setPreferredSize(new Dimension(
					fromImg.getWidth(), fromImg.getHeight()));
				fromImgScrollPane.updateUI();
				*/
				displayFromImage();
			}
			catch (Exception e)
			{
				handleException("Exception in mousePressed for readHexFileBt:", e, 101);
			}
		}
	});

	printFromGIFBt = new JButton();
	printFromGIFBt.setText("Print From GIF");
	printFromGIFBt.setActionCommand("Print_From_GIF");
	printFromGIFBt.setFont(new Font("Dialog", Font.BOLD, btFontSize));
	printFromGIFBt.setForeground(Color.black);
	printFromGIFBt.setBackground(greyBG);
	generalBtPanel.add(printFromGIFBt);
	printFromGIFBt.addMouseListener(new ViewImageOpsMouseListener()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			try
			{
				if (fromImgCanvas.getImage() == null)
				{
					printConsole("No image to print out");
				}
				else
				{
					// NEED to supply an image type to print out
					// and catch exceptions. ie., some jpg images
					// have too many colors to printout to gifs.
					String fileName = "C:/XRNA/jimage/from.out.gif";
					gifOutputFile = new BufferedOutputStream(
						new FileOutputStream(fileName));
					GifEncoder gifWrite =
						new GifEncoder(fromImgCanvas.getImage(), gifOutputFile);
					gifWrite.encode();
					gifOutputFile.flush();
					gifOutputFile.close();
					printConsole("gif written to: " + fileName + "    img.Width: " +
						fromImgCanvas.getImgWidth() + " img.Height: " +
						fromImgCanvas.getImgHeight());
				}
			}
			catch (Exception e)
			{
				handleException("Exception in viewImgOps.gifEncoder:", e, 101);
			}
		}
	});

	printToGIFBt = new JButton();
	printToGIFBt.setText("Print To GIF");
	printToGIFBt.setActionCommand("Print_To_GIF");
	printToGIFBt.setFont(new Font("Dialog", Font.BOLD, btFontSize));
	printToGIFBt.setForeground(Color.black);
	printToGIFBt.setBackground(greyBG);
	generalBtPanel.add(printToGIFBt);
	printToGIFBt.addMouseListener(new ViewImageOpsMouseListener()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			try
			{
				if (toImgCanvas.getImage() == null)
				{
					printConsole("No image to print out");
				}
				else
				{
					String fileName = "C:/XRNA/jimage/to.out.gif";
					gifOutputFile = new BufferedOutputStream(
						new FileOutputStream(fileName));
					GifEncoder gifWrite =
						new GifEncoder(toImgCanvas.getImage(), gifOutputFile);
					gifWrite.encode();
					gifOutputFile.flush();
					gifOutputFile.close();
					printConsole("gif written to: " + fileName + "    img.Width: " +
						toImgCanvas.getImgWidth() + " img.Height: " +
						toImgCanvas.getImgHeight());
				}
			}
			catch (Exception e)
			{
				handleException("Exception in viewImgOps.gifEncoder:", e, 101);
			}
		}
	});

	stopBt = new JButton();
	stopBt.setText("Stop");
	stopBt.setActionCommand("Stop");
	stopBt.setFont(new Font("Dialog", Font.BOLD, btFontSize));
	stopBt.setForeground(Color.black);
	stopBt.setBackground(greyBG);
	generalBtPanel.add(stopBt);
	stopBt.addMouseListener(new ViewImageOpsMouseListener()
	{
		public void
		mousePressed(MouseEvent event)
		{
			viewImgOpsExit();
		}
	});

	imgTransformPanel = new JPanel();
	imgTransformPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	imgTransformPanel.setForeground(Color.black);
	imgTransformPanel.setBackground(greyBG);
	imgTransformPanel.setBorder(
		new TitledBorder(border, "Image Transform Controls:"));
	controlPanel.add("South", imgTransformPanel);

	renderBt = new JButton();
	renderBt.setText("Render");
	renderBt.setActionCommand("Render");
	renderBt.setFont(new Font("Dialog", Font.BOLD, btFontSize));
	renderBt.setForeground(Color.black);
	renderBt.setBackground(greyBG);
	imgTransformPanel.add(renderBt);
	renderBt.addMouseListener(new ViewImageOpsMouseListener()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			try
			{

				ParameterBlock params = new ParameterBlock();
				params.addSource(fromImg);
				params.add(0.5F);         // x scale factor
				params.add(0.5F);         // y scale factor
				params.add(0.0F);         // x translate
				params.add(0.0F);         // y translate

				int imgW = toImg.getWidth();
				int imgH = toImg.getHeight();
				toImgCanvas.setPreferredSize(new Dimension(
					imgW < viewImgCanvasW ? viewImgCanvasW : imgW,
					imgH < viewImgCanvasH ? viewImgCanvasH : imgH));
				toImgScrollPane.updateUI();
				displayToImage();
			}
			catch (Exception e)
			{
				handleException("Exception in mousePressed for readImgBt:", e, 101);
			}
		}
	});

	colorChooserBt = new JButton();
	colorChooserBt.setText("Color");
	colorChooserBt.setActionCommand("Color");
	colorChooserBt.setFont(new Font("Dialog", Font.BOLD, btFontSize));
	colorChooserBt.setForeground(Color.black);
	colorChooserBt.setBackground(greyBG);
	imgTransformPanel.add(colorChooserBt);
	colorChooserBt.addMouseListener(new ViewImageOpsMouseListener()
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

	transformControlPanel = new JPanel();
	transformControlPanel.setLayout(new GridLayout(7, 1));
	transformControlPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	transformControlPanel.setForeground(Color.black);
	transformControlPanel.setBackground(greyBG);
	transformControlPanel.setBorder(
		new TitledBorder(border, "Transform Controls:"));
	getContentPane().add(BorderLayout.WEST, transformControlPanel);

	wallEyedStereoCB = new JCheckBox();
	wallEyedStereoCB.setSelected(false);
	wallEyedStereoCB.setText("Wall-Eyed Stereo");
	wallEyedStereoCB.setActionCommand("Wall-Eyed Stereo");
	wallEyedStereoCB.setFont(new Font("Dialog", Font.BOLD, 12));
	wallEyedStereoCB.setForeground(Color.black);
	wallEyedStereoCB.setBackground(greyBG);
	transformControlPanel.add(wallEyedStereoCB);
	wallEyedStereoCB.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
		}
	});

	cartesianCoordsCB = new JCheckBox();
	cartesianCoordsCB.setSelected(false);
	cartesianCoordsCB.setText("show coords");
	cartesianCoordsCB.setActionCommand("show coords");
	cartesianCoordsCB.setFont(new Font("Dialog", Font.BOLD, 12));
	cartesianCoordsCB.setForeground(Color.black);
	cartesianCoordsCB.setBackground(greyBG);
	transformControlPanel.add(cartesianCoordsCB);
	cartesianCoordsCB.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			try
			{
				displayFromImage();
			}
			catch (Exception e)
			{
				handleException("Exception in cartesianCoordsCB.addItemListener:", e, 101);
			}
		}
	});

	rotateGlobalTransformBt = new JRadioButton();
	rotateGlobalTransformBt.setSelected(true);
	rotateGlobalTransformBt.setText("Rotate Global");
	rotateGlobalTransformBt.setActionCommand("Rotate Global");
	rotateGlobalTransformBt.setFont(new Font("Dialog", Font.BOLD, 12));
	rotateGlobalTransformBt.setForeground(Color.black);
	rotateGlobalTransformBt.setBackground(greyBG);
	transformControlPanel.add(rotateGlobalTransformBt);
	rotateGlobalTransformBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent e)
		{
		}
	});

	rotateLocalTransformBt = new JRadioButton();
	rotateLocalTransformBt.setSelected(false);
	rotateLocalTransformBt.setText("Rotate Local");
	rotateLocalTransformBt.setActionCommand("Rotate Local");
	rotateLocalTransformBt.setFont(new Font("Dialog", Font.BOLD, 12));
	rotateLocalTransformBt.setForeground(Color.black);
	rotateLocalTransformBt.setBackground(greyBG);
	transformControlPanel.add(rotateLocalTransformBt);
	rotateLocalTransformBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent e)
		{
		}
	});

	translateTransformBt = new JRadioButton();
	translateTransformBt.setSelected(false);
	translateTransformBt.setText("Translate");
	translateTransformBt.setActionCommand("Translate");
	translateTransformBt.setFont(new Font("Dialog", Font.BOLD, 12));
	translateTransformBt.setForeground(Color.black);
	translateTransformBt.setBackground(greyBG);
	transformControlPanel.add(translateTransformBt);
	translateTransformBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent e)
		{
		}
	});

	scaleTransformBt = new JRadioButton();
	scaleTransformBt.setSelected(false);
	scaleTransformBt.setText("Scale");
	scaleTransformBt.setActionCommand("Scale");
	scaleTransformBt.setFont(new Font("Dialog", Font.BOLD, 12));
	scaleTransformBt.setForeground(Color.black);
	scaleTransformBt.setBackground(greyBG);
	transformControlPanel.add(scaleTransformBt);
	scaleTransformBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent e)
		{
		}
	});

	ButtonGroup group = new ButtonGroup();
	group.add(rotateGlobalTransformBt);
	group.add(rotateLocalTransformBt);
	group.add(translateTransformBt);
	group.add(scaleTransformBt);
  
	fromImgCanvas = new ViewImgCanvas(null, 0, 0);
	fromImgCanvas.setBackground(Color.black);
	fromImgCanvas.setOpaque(true);
	// NEED to have an img size that is always centered in
	// fromImgCanvas, even upon resizing. scroll bars work
	// out accordingly
	fromImgCanvas.setPreferredSize(new Dimension(viewImgCanvasW, viewImgCanvasH));

	fromImgScrollPane = new JScrollPane(fromImgCanvas,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	fromImgScrollPane.setBorder(new TitledBorder(border, "View From Image:"));
	// getContentPane().add(BorderLayout.CENTER, fromImgScrollPane);
	imgsPanel.add(BorderLayout.NORTH, fromImgScrollPane);
	fromImgCanvas.setVisible(true);

	fromImgCanvas.addMouseListener(new ViewImageOpsMouseListener()
	{
		public void
		mousePressed(MouseEvent event)
		{
			setCurrentMouseX(event.getX());
			setCurrentMouseY(event.getY());
			setLastMouseX(event.getX());
			setLastMouseY(event.getY());
			int mod = event.getModifiers();
			setCurrentMouseState(mod);
		}

		public void
		mouseReleased(MouseEvent event)
		{
		}
	});
	fromImgCanvas.addMouseMotionListener(new ViewImageOpsMouseMotionListener()
	{
		public void
		mouseDragged(MouseEvent event)
		{
		}

		public void
		mouseMoved(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
		}
	});
  
	toImgCanvas = new ViewImgCanvas(null, 0, 0);
	toImgCanvas.setBackground(Color.black);
	toImgCanvas.setOpaque(true);
	// NEED to have an img size that is always centered in
	// toImgCanvas, even upon resizing. scroll bars work
	// out accordingly
	toImgCanvas.setPreferredSize(new Dimension(viewImgCanvasW, viewImgCanvasH));

	toImgScrollPane = new JScrollPane(toImgCanvas,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	toImgScrollPane.setBorder(new TitledBorder(border, "View To Image:"));
	// getContentPane().add(BorderLayout.CENTER, toImgScrollPane);
	imgsPanel.add(BorderLayout.SOUTH, toImgScrollPane);
	toImgCanvas.setVisible(true);

	toImgCanvas.addMouseListener(new ViewImageOpsMouseListener()
	{
		public void
		mousePressed(MouseEvent event)
		{
			setCurrentMouseX(event.getX());
			setCurrentMouseY(event.getY());
			setLastMouseX(event.getX());
			setLastMouseY(event.getY());
			int mod = event.getModifiers();
			setCurrentMouseState(mod);
		}

		public void
		mouseReleased(MouseEvent event)
		{
		}
	});
	toImgCanvas.addMouseMotionListener(new ViewImageOpsMouseMotionListener()
	{
		public void
		mouseDragged(MouseEvent event)
		{
		}

		public void
		mouseMoved(MouseEvent event)
		{
			int x = event.getX();
			int y = event.getY();
		}
	});

	spSystemOut = new JScrollPane();
	spSystemOut.setOpaque(true);
	spSystemOut.setBounds(6,123,463,100);
	spSystemOut.setFont(new Font("Dialog", Font.PLAIN, 12));
	spSystemOut.setForeground(Color.black);
	spSystemOut.setBackground(greyBG);
	spSystemOut.setBorder(new TitledBorder(border, "System Out"));
	taSystemOut = new JTextPane();
	taSystemOut.setMargin(new java.awt.Insets(0,0,0,0));
	taSystemOut.setBounds(43,41,347,106);
	taSystemOut.setFont(new Font("Monospaced", Font.PLAIN, 12));
	taSystemOut.setForeground(Color.black);
	taSystemOut.setBackground(Color.white);
	spSystemOut.getViewport().add(taSystemOut);
	getContentPane().add(BorderLayout.SOUTH, spSystemOut);

	mbMain = new JMenuBar();
	//$$ mbMain.move(485,0);
	mFile = new JMenu();
	mFile.setText("File");
	mFile.setActionCommand("File");
	//$$ mFile.move(0,0);
	mbMain.add(mFile);

	miExit = (JMenuItem) mFile.add(new JMenuItem("Exit"));
	miExit.setMnemonic('x');
	miExit.getAccessibleContext().
		setAccessibleDescription("Exit application");
	miExit.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	);

	mFile.add(miExit);
	setJMenuBar(mbMain);

	addComponentListener(new ViewImageOpsComponentListener());

	SymWindow aSymWindow = new SymWindow();
	addWindowListener(aSymWindow);
}

private void
handleException(String extraMsg, Throwable t, int id)
{
	ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
	t.printStackTrace(new PrintStream(
		new DataOutputStream(excptArray)));
	debug(id + "    " + extraMsg + t.toString() +
		(new String(excptArray.toByteArray())));
	switch(id)
	{
	  case 0 :
		break;
	  default :
		break;
	}
	if (id >= 100)
		viewImgOpsExit();
}

static int startID = 0;

private void
initViewImageOps()
throws Exception
{
	printConsole("Start viewing: " + (startID++));
	// need to set an image
	displayFromImage();
}

private void
displayFromImage()
throws Exception
{
	if (fromImg == null)
		return;
	fromImgCanvas.setImage(fromImg);
	fromImgCanvas.setImgStartXY(showImgStartX, showImgStartY);
	fromImgCanvas.repaint();
}

private void
displayToImage()
throws Exception
{
	if (toImg == null)
		return;
	toImgCanvas.setImage(toImg);
	toImgCanvas.setImgStartXY(showImgStartX, showImgStartY);
	toImgCanvas.repaint();
}

private int currentMouseState = 0;

public void
setCurrentMouseState(int currentMouseState)
{
	this.currentMouseState = currentMouseState;
}

public int
getCurrentMouseState()
{
	return (this.currentMouseState);
}


private int currentMouseX = 0;

public void
setCurrentMouseX(int currentMouseX)
{
	this.currentMouseX = currentMouseX;
}

public int
getCurrentMouseX()
{
	return (this.currentMouseX);
}

private int currentMouseY = 0;

public void
setCurrentMouseY(int currentMouseY)
{
	this.currentMouseY = currentMouseY;
}

public int
getCurrentMouseY()
{
	return (this.currentMouseY);
}

private int lastMouseX = 0;

public void
setLastMouseX(int lastMouseX)
{
	this.lastMouseX = lastMouseX;
}

public int
getLastMouseX()
{
	return (this.lastMouseX);
}

private int lastMouseY = 0;

public void
setLastMouseY(int lastMouseY)
{
	this.lastMouseY = lastMouseY;
}

public int
getLastMouseY()
{
	return (this.lastMouseY);
}

class ViewImageOpsMouseListener
extends java.awt.event.MouseAdapter
{
	/* Stuff to implement
	public void
	mousePressed(MouseEvent event)
	{
	}

	public void
	mouseClicked(MouseEvent event)
	{
	}

	public void
	mouseReleased(MouseEvent event)
	{
	}

	public void
	mouseEntered(MouseEvent event)
	{
	}

	public void
	mouseExited(MouseEvent event)
	{
	}
	*/
}

class ViewImageOpsMouseMotionListener
extends MouseInputAdapter
{
	/* Stuff to implement
	public void
	mousePressed(MouseEvent event)
	{
	}

	public void
	mouseClicked(MouseEvent event)
	{
	}

	public void
	mouseReleased(MouseEvent event)
	{
	}

	public void
	mouseEntered(MouseEvent event)
	{
	}

	public void
	mouseExited(MouseEvent event)
	{
	}
	public void
	mouseDragged(MouseEvent e)
	{
	}

	public void
	mouseMoved(MouseEvent e)
	{
	}
	*/
}

/*
public void
run()
{
	Thread me = Thread.currentThread( );
	me.setPriority(Thread.MIN_PRIORITY);

	while (true)
	{
		try
		{
			thread.sleep(10);
		} catch( InterruptedException e )
		{
			return;
		}

		// Modify the values in the pixels array at (x, y, w, h)

		// Send the new data to the interested ImageConsumers
		source.newPixels(x, y, w, h);
	}	
}
*/

class SymWindow
extends java.awt.event.WindowAdapter
{
	public void windowClosing(java.awt.event.WindowEvent event)
	{
		Object object = event.getSource();
		if (object == ViewImageOps.this)
			viewImgOpsExit(event);
	}
}

public class
ViewImageOpsComponentListener
implements ComponentListener
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
		System.out.println("componentHidden event From "
		   + e.getComponent().getClass().getName());
		*/
	}

	public void
	componentMoved(ComponentEvent e)
	{
		/*
		System.out.println("componentMoved event From "
		   + e.getComponent().getClass().getName());
		*/
	}

	public void
	componentResized(ComponentEvent e)
	{
		/*
		System.out.println("componentResized event From "
		   + e.getComponent().getClass().getName());
		*/
	}

	public void
	componentShown(ComponentEvent e)
	{
		/*
		System.out.println("componentShown event From "
		   + e.getComponent().getClass().getName());
		*/
	}
}

private void /* KeyFile */
getKeyFile(String keyPathName, String keyFileName)
throws Exception
{
	/*
	if (keyFileName.equals("VCKeys.txt"))
		return (new VCKeyFile(keyPathName));
	if (keyFileName.equals("PSKeys.vsm"))
		return (new PSKeyFile(keyPathName));
	if (keyFileName.equals("MRKeys.txt"))
		return (new MRKeyFile(keyPathName));
	return (null);
	*/
}

private Properties
getViewImageOpsProperties(String propertiesFileName)
{
	Properties p = null;

	try
	{
		// set up new properties object From file "viewImgOpsImage.properties"
		// FileInputStream propFile = new FileInputStream("viewImgOpsImage.properties");
		FileInputStream propFile = new FileInputStream(propertiesFileName);
		// FileInputStream propFile = new FileInputStream("ViewImageOps.properties");
		p = new Properties(System.getProperties());
		p.load(propFile);

		// set the system properties
		System.setProperties(p);

		// list to standard out
		/*
		System.getProperties().list(System.out);
		*/
	}
	catch (java.io.FileNotFoundException fnfe)
	{
		System.err.println("Exception in viewImgOpsImage.getViewImageOpsProperties(): " +
			fnfe.toString());
		return (null);
	}
	catch (java.io.IOException ioe)
	{
		System.err.println("Exception in getViewImageOpsProperties(): " +
			ioe.toString());
		return (null);
	}
	return (p);
}

public void
viewImgOpsExit(java.awt.event.WindowEvent event)
{
	viewImgOpsExit();
}

public void
viewImgOpsExit()
{
	setVisible(false); // hide the Frame
	dispose();       // free the system resources
	System.exit(0);    // close the application
}

private static void
debug(String s)
{
	System.out.println(s);
}

private void
printConsole(String s)
{
	taSystemOut.setText(s);
}

}

