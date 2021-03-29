package jimage;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.io.*;
import java.awt.print.*;
import java.util.*;

import javax.swing.*;

import util.*;
import util.math.*;

public abstract class
DrawObjectView
extends JPanel
{

// width X height for laser printer paper. This will default min size
public static final int BASE_IMG_WIDTH = GraphicsUtil.PAPER_WIDTH;
public static final int BASE_IMG_HEIGHT = GraphicsUtil.PAPER_HEIGHT;

public static final int MAX_IMG_WIDTH = 10000;
public static final int MAX_IMG_HEIGHT = 10000;

public int drawObjectImgW = BASE_IMG_WIDTH;
public int drawObjectImgH = BASE_IMG_HEIGHT;

//public Color guiBGColor = new Color(0xffbbbbff);
public Color guiBGColor = new Color(0xffeeeeff);

public JFrame 			frameParent = null;
public JCheckBox		landscapeModeCB = null;
public JCheckBox		cartesianCoordsCB = null;
public JComboBox		genPanelComboBox = null;
public JPanel			generalBtPanel;
public JButton			startBt = null;
public JButton			stopBt = null;
public JButton			printBt = null;
public JButton			testBt = null;
public JButton			writeBt = null;
public JButton			fontChooserBt = null;
public JButton			colorChooserBt = null;
public JButton			genFileChooserBt = null;
public JFileChooser		genFileChooser = null;
public GenFileFilter	genFileFilter = null;

// NEED to replace genFileFilter with more general write/read filefilter
public GenFileFilter	genReadFileFilter = null;
public GenFileFilter	genWriteFileFilter = null;
public GenFileFilter	genWriteFileFilterCSV = null;
public GenFileFilter	genWriteFileFilterSVG = null;
public GenFileFilter    genWriteFileFilterTR = null;
public GenFileFilter    genWriteFileFilterBPSeq = null;

public JColorChooser	colorChooser = null;
public FontChooser		fontChooser = null;
public OutputStream 	gifOutputFile = null;

public abstract void buildGui();
public abstract void resetFigureScale(double val);

private double figureScale = 1.0;

public void
setFigureScale(double figureScale)
{
    this.figureScale = figureScale;
}

public double
getFigureScale()
{
    return (this.figureScale);
}

/* IF want to change from an image, try:
public class MyComponent extends JComponent {
  public void paintComponent(Graphics g) {
    // perform painting here...
  }
}
public void paintComponent(Graphics g) {
   ...
   super.paintComponent(g); //<-this line
   ...
}
*/

private ViewImgCanvas viewImgCanvas = null;

public void
setViewImgCanvas(ViewImgCanvas viewImgCanvas)
{
    this.viewImgCanvas = viewImgCanvas;
}

public ViewImgCanvas
getViewImgCanvas()
{
    return (this.viewImgCanvas);
}

public void
setWindowViewParams(int x, int y, int w, int h)
{
	this.setWindowViewX(x);
	this.setWindowViewY(y);
	this.setWindowViewW(w);
	this.setWindowViewH(h);
// debug("WIN X,Y,W,H: " + x + " " + y + " " + w + " " + h);
}

private int windowViewX = 0;

public void
setWindowViewX(int windowViewX)
{
    this.windowViewX = windowViewX;
}

public int
getWindowViewX()
{
    return (this.windowViewX);
}

private int windowViewY = 0;

public void
setWindowViewY(int windowViewY)
{
    this.windowViewY = windowViewY;
}

public int
getWindowViewY()
{
    return (this.windowViewY);
}

private int windowViewW = 0;

public void
setWindowViewW(int windowViewW)
{
    this.windowViewW = windowViewW;
}

public int
getWindowViewW()
{
    return (this.windowViewW);
}

private int windowViewH = 0;

public void
setWindowViewH(int windowViewH)
{
    this.windowViewH = windowViewH;
}

public int
getWindowViewH()
{
    return (this.windowViewH);
}

private BufferedImage drawObjectImg = null;

public void
setSceneImg(BufferedImage drawObjectImg)
{
    this.drawObjectImg = drawObjectImg;
}

public BufferedImage
getSceneImg()
{
    return (this.drawObjectImg);
}

public void
setOffScreenImg()
{
	this.setOffScreenImg(
		this.getWindowViewX(), this.getWindowViewY(),
		this.getWindowViewW(), this.getWindowViewH());
}

public void
setOffScreenImg(int x, int y, int w, int h)
{
	this.setOffScreenImg(GraphicsUtil.cloneImage(this.getSceneImg().getSubimage(x, y, w, h)));
}

private BufferedImage offScreenImg = null;

public void
setOffScreenImg(BufferedImage offScreenImg)
{
    this.offScreenImg = offScreenImg;
}

public BufferedImage
getOffScreenImg()
{
    return (this.offScreenImg);
}

public void
setMouseMethod()
{
	this.getViewImgCanvas().addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			setCurrentViewX(event.getX());
			setCurrentViewY(event.getY());

			setMousePressedImgSpaceX(event.getX());
			setMousePressedImgSpaceY(event.getY());

			setCurrentMouseState(event.getModifiers());

			doViewImgMousePressed();
		}

		public void
		mouseReleased(MouseEvent event)
		{
			doViewImgMouseReleased();
		}
	});

	this.getViewImgCanvas().addMouseMotionListener(new MouseMotionListener()
	{
		public void
		mouseDragged(MouseEvent event)
		{
			setCurrentViewX(event.getX());
			setCurrentViewY(event.getY());
			if (getShowMousePos())
				printCurrentMousePosInfo();
			doViewImgMouseDragged();
		}

		public void
		mouseMoved(MouseEvent event)
		{
			setCurrentViewX(event.getX());
			setCurrentViewY(event.getY());
			if (getShowMousePos())
				printCurrentMousePosInfo();
			doViewImgMouseMoved();
		}
	});
}

public abstract void doViewImgMousePressed();
public abstract void doViewImgMouseReleased();
public abstract void doViewImgMouseDragged();
public abstract void doViewImgMouseMoved();
public abstract void createDrawList() throws Exception;
public abstract void updateImgWindowView();
public abstract void updateAfterRender() throws Exception;

public void
updateImg(BufferedImage bufImg)
{
	if (this.getViewImgCanvas() == null)
		return;
	this.getViewImgCanvas().setImage(bufImg);
	this.getViewImgCanvas().repaint();
}

public void
updateImg()
{
	this.updateImg(this.getSceneImg());
}

public void
paintWindowView()
{
	this.getViewImgCanvas().paintImmediately(
		this.getWindowViewX(), this.getWindowViewY(),
		this.getWindowViewW(), this.getWindowViewH());
}

// this works fastest on non Mac machines:
private int imgType = BufferedImage.TYPE_BYTE_INDEXED;

// works on Mac OS X and non mac machines but is too slow on macs:
// private int imgType = BufferedImage.TYPE_INT_RGB;

// works fastest for macs but is slow on non mac machines
// private int imgType = BufferedImage.TYPE_INT_ARGB_PRE;

public void
setImgType(int imgType)
{
    this.imgType = imgType;
}

public int
getImgType()
{
    return (this.imgType);
}

public void
renderDrawObjectView(int w, int h)
throws Exception
{
	this.renderDrawObjectView(w, h, 0);
}

public void
renderDrawObjectView(int w, int h, int border)
throws Exception
{
	this.renderDrawObjectView(border, false);
}

public void
renderDrawObjectView()
throws Exception
{
	this.renderDrawObjectView(40, true);
}

public void
renderDrawObjectView(int border, boolean checkDimensions)
throws Exception
{
	Rectangle2D rect = this.resetCurrentBoundingBox();

	if (rect != null)
	{
		int rectW = (int)Math.round((rect.getWidth() + border) * this.getFigureScale());
		int rectH = (int)Math.round((rect.getHeight() + border) * this.getFigureScale());

		/*
		if (checkDimensions)
		{
			if (rectW < BASE_IMG_WIDTH)
				drawObjectImgW = BASE_IMG_WIDTH;
			else if (rectW > MAX_IMG_WIDTH)
				drawObjectImgW = MAX_IMG_WIDTH;
			else
				drawObjectImgW = rectW;

			if (rectH < BASE_IMG_HEIGHT)
				drawObjectImgH = BASE_IMG_HEIGHT;
			else if (rectH > MAX_IMG_HEIGHT)
				drawObjectImgH = MAX_IMG_HEIGHT;
			else
				drawObjectImgH = rectH;
		}
		*/

		drawObjectImgW = rectW;
		if (checkDimensions)
		{
			if (rectW < BASE_IMG_WIDTH)
				drawObjectImgW = BASE_IMG_WIDTH;
			else if (rectW > MAX_IMG_WIDTH)
				drawObjectImgW = MAX_IMG_WIDTH;
		}

		drawObjectImgH = rectH;
		if (checkDimensions)
		{
			if (rectH < BASE_IMG_HEIGHT)
				drawObjectImgH = BASE_IMG_HEIGHT;
			else if (rectH > MAX_IMG_HEIGHT)
				drawObjectImgH = MAX_IMG_HEIGHT;
		}

		this.resetInitTrans();
	}

	if ((this.getSceneImg() == null) ||
		(!((this.getSceneImg().getWidth() == drawObjectImgW) &&
		(this.getSceneImg().getHeight() == drawObjectImgH))))
	{
		this.setSceneImg(
			new BufferedImage(drawObjectImgW, drawObjectImgH,
				this.getImgType()));
		/*
		// this solves printing to jpg problem (1, or 3 bands)
		// but introduces stippling in image.
		BufferedImage.TYPE_BYTE_INDEXED);
		*/

		this.setSceneGraphics2D(this.getSceneImg().createGraphics());
		this.getSceneGraphics2D().setRenderingHints(
			this.getDrawObjectRenderHints());
	}
	this.updateImgWindowView();
	this.drawRoot();
	this.updateAfterRender();
}

public void
renderPSDrawObjectView(File outFile, int border, boolean checkDimensions)
throws Exception
{
	// NEED to catch an exception and not overwrite a file if
	// there's a problem
	// or maybe make a backup file if it is same name

	if (outFile.exists() && (!outFile.canWrite()))
	{
		debug("Can't write " + outFile.getName() + " ; Need to set writeable");
		return;
	}

	/*
	if (outFile.exists())
		FileUtil.copyFile(outFile, new File(outFile.getName() + ".bak"));
	*/

	PostScriptUtil psUtil = new PostScriptUtil();

	// first get xrna input file as string to bypass any exceptions
	try
	{
		this.renderPSDrawObjectView(psUtil, border, checkDimensions);
	}
	catch (Exception e)
	{
		// showUserMsg("Error, file not written:\n" + e.toString());
		throw e;
	}

	// made it past any errors in creating xrna input file

	try
	{
		outFile.createNewFile();
		FileWriter genFileWriter = new FileWriter(outFile);
		PrintWriter pWriter = new PrintWriter(
			new BufferedWriter(genFileWriter), true);
		pWriter.print(psUtil.toString());
		pWriter.flush();
		pWriter.close();
	}
	catch (Exception e)
	{
		throw e;
	}
}

public void
renderPSDrawObjectView(PostScriptUtil psUtil, int border, boolean checkDimensions)
throws Exception
{
	Rectangle2D rect = this.resetCurrentBoundingBox();

	if (rect != null)
	{
		int rectW = (int)Math.round((rect.getWidth() + border) * this.getFigureScale());
		int rectH = (int)Math.round((rect.getHeight() + border) * this.getFigureScale());

		/*
		if (checkDimensions)
		{
			if (rectW < BASE_IMG_WIDTH)
				drawObjectImgW = BASE_IMG_WIDTH;
			else if (rectW > MAX_IMG_WIDTH)
				drawObjectImgW = MAX_IMG_WIDTH;
			else
				drawObjectImgW = rectW;

			if (rectH < BASE_IMG_HEIGHT)
				drawObjectImgH = BASE_IMG_HEIGHT;
			else if (rectH > MAX_IMG_HEIGHT)
				drawObjectImgH = MAX_IMG_HEIGHT;
			else
				drawObjectImgH = rectH;
		}
		*/

		drawObjectImgW = rectW;
		if (checkDimensions)
		{
			if (rectW < BASE_IMG_WIDTH)
				drawObjectImgW = BASE_IMG_WIDTH;
			else if (rectW > MAX_IMG_WIDTH)
				drawObjectImgW = MAX_IMG_WIDTH;
		}

		drawObjectImgH = rectH;
		if (checkDimensions)
		{
			if (rectH < BASE_IMG_HEIGHT)
				drawObjectImgH = BASE_IMG_HEIGHT;
			else if (rectH > MAX_IMG_HEIGHT)
				drawObjectImgH = MAX_IMG_HEIGHT;
		}

		this.resetInitTrans();
	}

	// SEE about replacing this with a simpler way to get SceneGraphics2D
	if ((this.getSceneImg() == null) ||
		(!((this.getSceneImg().getWidth() == drawObjectImgW) &&
		(this.getSceneImg().getHeight() == drawObjectImgH))))
	{
		this.setSceneImg(
			new BufferedImage(drawObjectImgW, drawObjectImgH,
				this.getImgType()));
		/*
		// this solves printing to jpg problem (1, or 3 bands)
		// but introduces stippling in image.
		BufferedImage.TYPE_BYTE_INDEXED);
		*/

		this.setSceneGraphics2D(this.getSceneImg().createGraphics());
		this.getSceneGraphics2D().setRenderingHints(
			this.getDrawObjectRenderHints());
	}
	this.printPSRoot(psUtil);
}

private RenderingHints drawObjectRenderHints =
	GraphicsUtil.aliasedRenderHints;

public void
setDrawObjectRenderHints(RenderingHints drawObjectRenderHints)
{
    this.drawObjectRenderHints = drawObjectRenderHints;
}

public RenderingHints
getDrawObjectRenderHints()
{
    return (this.drawObjectRenderHints);
}

private boolean useXOR = false;

public void
setUseXOR(boolean useXOR)
{
    this.useXOR = useXOR;
}

public boolean
getUseXOR()
{
    return (this.useXOR);
}

static int drawCount = 0;

public void
drawRoot()
throws Exception
{
	if (this.getSceneImg() == null)
		return;

	/*
	debug("SCENEIMG W,H: " + this.getSceneImg().getWidth() + " " +
			this.getSceneImg().getHeight());
	*/

	// debug("IN drawRoot(): " + (++drawCount) + ", figureScale: " + this.getFigureScale() + " useXOR: " + this.getUseXOR());

	// NEW: need to get a different criteria for if statment
	/*
	if (this.getCurrentGraphics2D() == null)
	{
		setCurrentGraphics2D(this.getSceneImg().createGraphics());
		this.getCurrentGraphics2D().setRenderingHints(
			this.getDrawObjectRenderHints());
	}
	*/
	// ORIGINAL
	setCurrentGraphics2D(this.getSceneImg().createGraphics());
	/* EXPERIMENTAL: increases speed of render significantly
	this.getCurrentGraphics2D().setRenderingHints(
		this.getDrawObjectRenderHints());
	*/
	//
	/* doesn't work
	this.setCurrentGraphics2D(this.getSceneGraphics2D());
	this.getCurrentGraphics2D().setColor(Color.green);
	this.getCurrentGraphics2D().clearRect(0, 0, drawObjectImgW, drawObjectImgH);
	*/

	drawRoot(this.getCurrentGraphics2D());

	this.updateImg();

	/*
	debug("WINDOW VIEW X: " + this.getWindowViewX());
	debug("WINDOW VIEW Y: " + this.getWindowViewY());
	debug("WINDOW VIEW W: " + this.getWindowViewW());
	debug("WINDOW VIEW H: " + this.getWindowViewH());
	*/

	// debug("OUT drawRoot(): " + (drawCount));
}

public void
printPSRoot(PostScriptUtil psUtil)
throws Exception
{
	setCurrentGraphics2D(this.getSceneImg().createGraphics());
	printPSRoot(psUtil, this.getCurrentGraphics2D());
}

public void
initG2Transform(Graphics2D g2)
{
	// took out since affected printing. Not sure why it is in here.
	// (seems to affect annotate)
	// g2.setTransform(new AffineTransform());

	g2.setBackground(this.getDrawImgBGColor());
	g2.translate(this.getInitTransX(), this.getInitTransY());
	g2.setClip(-this.getInitTransX(), -this.getInitTransY(),
		drawObjectImgW, drawObjectImgH);
	// debug("IN INTG2Trans, getFigureScale: " + this.getFigureScale());
	g2.scale(this.getFigureScale(), this.getFigureScale());
}

public Graphics2D
getInitViewGraphics2D()
{
	Graphics2D g2 = this.getSceneImg().createGraphics();
	initG2Transform(g2);
	return (g2);
}

public Graphics2D
getClearedInitViewGraphics2D()
{
	Graphics2D g2 = this.getSceneImg().createGraphics();
	clearImg(g2, drawObjectImgW, drawObjectImgH);
	initG2Transform(g2);
	return (g2);
}

public void
clearImg(Graphics2D g2, int w, int h)
{
	g2.setColor(this.getDrawImgBGColor());
	g2.fillRect(0, 0, w, h);
}

private String inputFileName = null;

public void
setInputFileName(String inputFileName)
throws Exception
{
    this.inputFileName = inputFileName;
}

public String
getInputFileName()
{
    return (this.inputFileName);
}

private Vector drawObjectList = null;

public void
setDrawObjectList(Vector drawObjectList)
{
    this.drawObjectList = drawObjectList;
}

public Vector
getDrawObjectList()
{
    return (this.drawObjectList);
}

public void
setClearedDrawObjectList()
{
	if (this.getDrawObjectList() == null)
		this.setDrawObjectList(new Vector());
	this.getDrawObjectList().clear();
}

private BRectangle2D currentBoundingBox = null;

public void
setCurrentBoundingBox(BRectangle2D currentBoundingBox)
{
	this.currentBoundingBox = currentBoundingBox;
}

public BRectangle2D
getCurrentBoundingBox()
{
	return (this.currentBoundingBox);
}

private BRectangle2D
resetCurrentBoundingBox()
throws Exception
{
	// make a new drw obj list or clear out the old one
	this.setClearedDrawObjectList();
	this.createDrawList();

	Graphics2D tmpG = (new BufferedImage(1, 1, this.getImgType()).createGraphics());

	this.initG2Transform(tmpG);
	this.updateDrawList(tmpG);

	BRectangle2D rect = null;
	for (Enumeration e = this.getDrawObjectList().elements();e.hasMoreElements();)
	{
		Object obj = e.nextElement();
		if (!(obj instanceof DrawObject))
			continue;
		DrawObject drawObject = (DrawObject)obj;
		if (drawObject.getBoundingBox() == null)
			continue;
		if (rect == null)
		{
			rect = new BRectangle2D();
			rect.setRect(drawObject.getBoundingBox());
		}
		else
		{
			rect.add(drawObject.getBoundingBox());
		}

		/* MAY HAVE TO DRAW TO GET LEAF NODES TO UPDATE
		if (drawObject instanceof DrawObject)
			if (!(drawObject instanceof DrawObjectLeafNode))
				((DrawObject)drawObject).update(g2);
		*/
	}
	this.setCurrentBoundingBox(rect);
	return (rect);
}

public void
drawRoot(Graphics2D g2)
throws Exception
{
	this.clearImg(g2, drawObjectImgW, drawObjectImgH);

	if (this.getDrawObjectList() == null)
	{
		// make a new drw obj list or clear out the old one
		this.setClearedDrawObjectList();
		this.createDrawList();
	}

	this.initG2Transform(g2);

	this.drawList(g2, null);

	if (this.getDrawWithCoords())
		drawCartesianCoords();
}

public void
printPSRoot(PostScriptUtil psUtil, Graphics2D g2)
throws Exception
{
	if (this.getDrawObjectList() == null)
	{
		// make a new drw obj list or clear out the old one
		this.setClearedDrawObjectList();
		this.createDrawList();
	}

	this.initG2Transform(g2);
	/*
	debug("g2.xy: " +
		g2.getTransform().getTranslateX() + " " +
		g2.getTransform().getTranslateY());
	*/

	psUtil.init2DPSCmds(this.getLandscapeModeOn(), g2);

	/*
	debug("g2 trans: " +
		g2.getTransform().getTranslateX() + " " +
		g2.getTransform().getTranslateY() + " " +
		g2.getTransform().getScaleX());
	*/

	Rectangle2D rect = this.getCurrentBoundingBox();

	this.printPSList(g2, psUtil);

	if (true /*this.getDrawWithCoords()*/)
		// NEED to get postscript scale value into 0.5 v
		psUtil.drawPSCartesianCoordinateSystem(this.getLandscapeModeOn(), Color.black, 0.5, g2);

	psUtil.endLWCmds();
}


public void
updateDrawList(Graphics2D g2)
throws Exception
{
	for (Enumeration e = this.getDrawObjectList().elements();e.hasMoreElements();)
	{
		Object drawObject = e.nextElement();
		if (drawObject instanceof DrawObject)
			if (!(drawObject instanceof DrawObjectLeafNode))
				((DrawObject)drawObject).update(g2);
	}
}

public void
drawList(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	/*
	for (Enumeration e = this.getDrawObjectList().elements();e.hasMoreElements();)
	{
		Object drawObject = e.nextElement();
		if (drawObject instanceof DrawObject)
			((DrawObject)drawObject).draw(g2, constrainedArea);
		else if (drawObject instanceof Shape)
			g2.draw((Shape)drawObject);
	}
	*/

	// Currently assuming that any Shape wants to be drawn first
	/*
	for (Enumeration e = this.getDrawObjectList().elements();e.hasMoreElements();)
	{
		Object drawObject = e.nextElement();
		if (drawObject instanceof Shape)
		{
			debug("DRWOBJ SHP: " + drawObject);
			g2.draw((Shape)drawObject);
		}
	}
	*/
	for (Enumeration e = this.getDrawObjectList().elements();e.hasMoreElements();)
	{
		Object drawObject = e.nextElement();
		if (drawObject instanceof Shape)
			g2.draw((Shape)drawObject);
	}
	for (Enumeration e = this.getDrawObjectList().elements();e.hasMoreElements();)
	{
		Object drawObject = e.nextElement();
		if (drawObject instanceof DrawObject)
			((DrawObject)drawObject).draw(g2, constrainedArea);
	}
}

public void
printPSList(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	for (Enumeration e = this.getDrawObjectList().elements();e.hasMoreElements();)
	{
		Object drawObject = e.nextElement();
		if (drawObject instanceof DrawObject)
			((DrawObject)drawObject).printPS(g2, psUtil);
		/*
		else if (drawObject instanceof Shape)
			g2.draw((Shape)drawObject);
		*/
	}
}

// draw everything in scene that is in bounding box but constrain
public void
drawConstrainedArea(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	g2.setTransform(new AffineTransform());
	this.initG2Transform(g2);
	this.drawList(g2, constrainedArea);
}

public void
printPSFromJDK(Graphics2D g2, double printScaleVal)
throws Exception
{
	Rectangle2D rect = GraphicsUtil.getLaserPrinterBoundingBox(this.getLandscapeModeOn(), 0, 0);
	double minX = rect.getX();
	double minY = rect.getY();
	double maxX = rect.getX() + rect.getWidth();
	double maxY = rect.getY() + rect.getHeight();

	this.clearImg(g2, drawObjectImgW, drawObjectImgH);

	// make a new drw obj list or clear out the old one
	this.setClearedDrawObjectList();

	this.createDrawList();

	// this.initG2Transform(g2);
	// g2.translate(this.getInitTransX(), this.getInitTransY());
	// g2.translate(minX + rect.getWidth()/2.0, minY + rect.getHeight()/2.0);
	g2.translate(rect.getWidth()/2.0, rect.getHeight()/2.0);

	/*
	g2.setClip(-this.getInitTransX(), -this.getInitTransY(),
		drawObjectImgW, drawObjectImgH);
	*/

	/*
	g2.setColor(Color.black);
	g2.drawLine(-1000, 0, 1000, 0);
	g2.drawLine(0, -1000, 0, 1000);
	g2.draw(GraphicsUtil.getLaserPrinterMinusOneInchBoundingBox(this.getLandscapeModeOn()));
	g2.draw(GraphicsUtil.getLaserPrinterBoundingBox(this.getLandscapeModeOn(), 80, 80));
	*/

	g2.scale(printScaleVal, printScaleVal);
	g2.setBackground(this.getDrawImgBGColor());

	this.updateDrawList(g2);
	this.drawList(g2, null);

	/*
	if (this.getDrawWithCoords())
		GraphicsUtil.draw2DCartesianCoordinateSystem(
			this.getLandscapeModeOn(), Color.black,
			this.getFigureScale(), g2);
	*/
}

public void
printJava140PSFromJDK(Graphics2D g2, PageFormat pageFormat)
throws Exception
{
	// this gives an UNDEFINED in output:
	// pageFormat.setOrientation(PageFormat.LANDSCAPE);

	this.setClearedDrawObjectList();
	this.createDrawList();

	// debug("G2.clip, before: " + g2.getClipRect());

	// NEED to center perfectly on paper. currently a little off both ways
	g2.translate(
		pageFormat.getImageableX() + (pageFormat.getImageableWidth()/2.0),
		pageFormat.getImageableY() + (pageFormat.getImageableHeight()/2.0));

	// debug("G2.clip, after: " + g2.getClipRect());

	/*
	g2.setColor(Color.black);
	g2.drawLine(-1000, 0, 1000, 0);
	g2.drawLine(0, -1000, 0, 1000);
	*/

	g2.scale(this.getFigureScale(), this.getFigureScale());
	// debug("G2.clip, after scale: " + g2.getClipRect());

	// NEED to figure out clip so that printer can clip rather than
	// program.
	// g2.setClip(-59, -81, 118, 162);
	g2.setClip(-1000, -1000, 2000, 2000);

	// debug("TRANSFORM: " + g2.getTransform());

	this.updateDrawList(g2);
	this.drawList(g2, null);
}

public void
drawCartesianCoords()
{
	try
	{
		Graphics2D g2 = this.getInitViewGraphics2D();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setXORMode(this.getDrawImgBGColor());
		GraphicsUtil.draw2DCartesianCoordinateSystem(
			this.getLandscapeModeOn(), Color.green, this.getFigureScale(), g2);
	}
	catch (Exception e)
	{
		handleException("Exception in DrawObjectView.drawCartesianCoords:", e, 101);
	}
}

private int gridInc = 10;

public void
setGridInc(int gridInc)
{
    this.gridInc = gridInc;
}

public int
getGridInc()
{
    return (this.gridInc);
}

public Graphics2D
getGridGraphics()
{
	Graphics2D g2 = this.getSceneImg().createGraphics();
	int gridW = this.getSceneImg().getWidth();
	int gridH = this.getSceneImg().getHeight();
	g2.translate(0, 0);
	g2.setClip(0, 0, gridW, gridH);
	g2.scale(1.0, 1.0);
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_OFF);
	g2.setXORMode(this.getDrawImgBGColor());
	g2.setColor(Color.green);

	return (g2);
}

private static BasicStroke arcLineStroke = 
	new BasicStroke((float)1.0,
		BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

public void
resetOverlayGraphics(Graphics2D g2)
{
	g2.scale(1.0, 1.0);
	g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
	g2.setColor(Color.green);
	g2.setStroke(arcLineStroke);
}

public void
drawOverlay(Graphics2D g2)
{
	if (!(this.getDrawWithVerticalGrid() || this.getDrawWithHorizontalGrid() ||
		this.getDrawWithCircleOverlay()))
		return;
	
	int gridJump = this.getGridInc();
	int gridW = this.getWindowViewW();
	int gridH = this.getWindowViewH();
	int startX = this.getWindowViewX();
	int endX = this.getWindowViewX() + gridW;
	int startY = this.getWindowViewY();
	int endY = this.getWindowViewY() + gridH - 1;
	this.resetOverlayGraphics(g2);

	if (this.getDrawWithVerticalGrid())
	{
		for (int col = startX + gridJump - 1;col < endX - gridJump; col += gridJump)
			g2.drawLine(col, startY, col, endY);
	}
	if (this.getDrawWithHorizontalGrid())
	{
		for (int row = startY;row < endY; row += gridJump)
			g2.drawLine(startX, row, endX, row);
	}
	if (this.getDrawWithCircleOverlay())
	{
		g2.drawLine(startX, startY, endX, endY);
		g2.drawLine(startX + gridW/2, startY, startX + gridW/2, endY);
		g2.drawLine(startX, startY + gridH/2, endX, startY + gridH/2);
		g2.drawLine(endX, startY, startX, endY);
		g2.draw(this.getArc2D(
			(double)(startX + gridW/2) - this.getOverlayArcRadius(),
			(double)(startY + gridH/2) - this.getOverlayArcRadius()));
	}
}

private Arc2D arc2D = null;

public void
setArc2D(double x, double y)
{
	if (this.arc2D == null)
		this.arc2D = new Arc2D.Double();

	this.arc2D.setArc(x, y,
		2.0*this.getOverlayArcRadius(),
		2.0*this.getOverlayArcRadius(), 0.0, 360.0,
		Arc2D.OPEN);
}

public Arc2D
getArc2D(double x, double y)
{
	this.setArc2D(x, y);
    return (this.arc2D);
}

private double overlayArcRadius = 40.0;

public void
setOverlayArcRadius(double overlayArcRadius)
{
	this.overlayArcRadius = overlayArcRadius;
}

public double
getOverlayArcRadius()
{
	return (this.overlayArcRadius);
}

public void
updateDrawList()
throws Exception
{
	this.updateDrawList((new BufferedImage(1, 1, this.getImgType())).createGraphics());
}

private boolean showMousePos = false;

public void
setShowMousePos(boolean showMousePos)
{
    this.showMousePos = showMousePos;
}

public boolean
getShowMousePos()
{
    return (this.showMousePos);
}

private boolean landscapeModeOn = false;

public void
setLandscapeModeOn(boolean landscapeModeOn)
{
    this.landscapeModeOn = landscapeModeOn;
}

public boolean
getLandscapeModeOn()
{
    return (this.landscapeModeOn);
}

private boolean drawWithCoords = false;

public void
setDrawWithCoords(boolean drawWithCoords)
{
    this.drawWithCoords = drawWithCoords;
}

public boolean
getDrawWithCoords()
{
    return (this.drawWithCoords);
}

private boolean drawWithGrid = false;

public void
setDrawWithGrid(boolean drawWithGrid)
{
    this.drawWithGrid = drawWithGrid;
}

public boolean
getDrawWithGrid()
{
    return (this.drawWithGrid);
}

private boolean drawWithHorizontalGrid = false;

public void
setDrawWithHorizontalGrid(boolean drawWithHorizontalGrid)
{
    this.drawWithHorizontalGrid = drawWithHorizontalGrid;
}

public boolean
getDrawWithHorizontalGrid()
{
    return (this.drawWithHorizontalGrid);
}

private boolean drawWithVerticalGrid = false;

public void
setDrawWithVerticalGrid(boolean drawWithVerticalGrid)
{
    this.drawWithVerticalGrid = drawWithVerticalGrid;
}

public boolean
getDrawWithVerticalGrid()
{
    return (this.drawWithVerticalGrid);
}

private boolean drawWithCircleOverlay = false;

public void
setDrawWithCircleOverlay(boolean drawWithCircleOverlay)
{
    this.drawWithCircleOverlay = drawWithCircleOverlay;
}

public boolean
getDrawWithCircleOverlay()
{
    return (this.drawWithCircleOverlay);
}

public abstract void runSetFromInputFile() throws Exception;

private Color drawImgBGColor = Color.white;

public void
setDrawImgBGColor(Color drawImgBGColor)
{
    this.drawImgBGColor = drawImgBGColor;
}

public Color
getDrawImgBGColor()
{
    return (this.drawImgBGColor);
}

public void
resetInitTrans()
{
	this.setInitTransX(drawObjectImgW/2);
	this.setInitTransY(drawObjectImgH/2);
}

private int initTransX = BASE_IMG_WIDTH/2;

public void
setInitTransX(int initTransX)
{
    this.initTransX = initTransX;
}

public int
getInitTransX()
{
    return (this.initTransX);
}

private int initTransY = BASE_IMG_HEIGHT/2;

public void
setInitTransY(int initTransY)
{
    this.initTransY = initTransY;
}

public int
getInitTransY()
{
    return (this.initTransY);
}

private File currentInputFile = null;

public void
setCurrentInputFile(File currentInputFile)
throws Exception
{
	this.currentInputFile = currentInputFile;
}

public File
getCurrentInputFile()
{
	return (this.currentInputFile);
}

private Graphics2D currentGraphics2D = null;

public void
setCurrentGraphics2D(Graphics2D currentGraphics2D)
{
	this.currentGraphics2D = currentGraphics2D;
}

public Graphics2D
getCurrentGraphics2D()
{
	return (this.currentGraphics2D);
}

private Graphics2D sceneGraphics2D = null;

public void
setSceneGraphics2D(Graphics2D sceneGraphics2D)
{
    this.sceneGraphics2D = sceneGraphics2D;
}

public Graphics2D
getSceneGraphics2D()
{
    return (this.sceneGraphics2D);
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

private double currentViewX = 0.0;

public void
setCurrentViewX(int x)
{
	this.setCurrentImgSpaceX(x);
	this.currentViewX = ((double)(x - this.getInitTransX()))/this.getFigureScale();
}

public double
getCurrentViewX()
{
	return (this.currentViewX);
}

private double currentViewY = 0.0;

public void
setCurrentViewY(int y)
{
	this.setCurrentImgSpaceY(y);
	this.currentViewY = ((double)(y - this.getInitTransY()))/this.getFigureScale();
}

private int currentImgSpaceX = 0;

public void
setCurrentImgSpaceX(int currentImgSpaceX)
{
    this.currentImgSpaceX = currentImgSpaceX;
}

public int
getCurrentImgSpaceX()
{
    return (this.currentImgSpaceX);
}

private int currentImgSpaceY = 0;

public void
setCurrentImgSpaceY(int currentImgSpaceY)
{
    this.currentImgSpaceY = currentImgSpaceY;
}

public int
getCurrentImgSpaceY()
{
    return (this.currentImgSpaceY);
}

public double
getCurrentViewY()
{
	return (this.currentViewY);
}

public int
imgSpaceX(double xPos)
{
	return ((int)Math.round((xPos*this.getFigureScale()) + (double)this.getInitTransX()));
}

public int
imgSpaceY(double yPos)
{
	return ((int)Math.round((yPos*this.getFigureScale()) + (double)this.getInitTransY()));
}

public double
viewSpaceX(int imgX)
{
	return (((double)(imgX - this.getInitTransX()))/this.getFigureScale());
}

public double
viewSpaceY(int imgY)
{
	return (((double)(imgY - this.getInitTransY()))/this.getFigureScale());
}

private int mousePressedImgSpaceX = 0;

public void
setMousePressedImgSpaceX(int mousePressedImgSpaceX)
{
	this.mousePressedImgSpaceX = mousePressedImgSpaceX;
}

public int
getMousePressedImgSpaceX()
{
	return (this.mousePressedImgSpaceX);
}

private int mousePressedImgSpaceY = 0;

public void
setMousePressedImgSpaceY(int mousePressedImgSpaceY)
{
	this.mousePressedImgSpaceY = mousePressedImgSpaceY;
}

public int
getMousePressedImgSpaceY()
{
	return (this.mousePressedImgSpaceY);
}

private int guiStartX = 0;

public void
setGuiStartX(int guiStartX)
{
    this.guiStartX = guiStartX;
}

public int
getGuiStartX()
{
    return (this.guiStartX);
}

private int guiStartY = 0;

public void
setGuiStartY(int guiStartY)
{
    this.guiStartY = guiStartY;
}

public int
getGuiStartY()
{
    return (this.guiStartY);
}

private int guiWidth = 0;

public void
setGuiWidth(int guiWidth)
{
    this.guiWidth = guiWidth;
}

public int
getGuiWidth()
{
    return (this.guiWidth);
}

private int guiHeight = 0;

public void
setGuiHeight(int guiHeight)
{
    this.guiHeight = guiHeight;
}

public int
getGuiHeight()
{
    return (this.guiHeight);
}

public void
runPrintCmd(Graphics g)
{
	Graphics2D sveG2 = this.getCurrentGraphics2D();
	Graphics2D g2 = (Graphics2D)g;
	setCurrentGraphics2D(g2);
	g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
		RenderingHints.VALUE_FRACTIONALMETRICS_ON);

	try
	{
		drawRoot(g2);
		setCurrentGraphics2D(sveG2);
	}
	catch (Exception e)
	{
		handleException("Exception in GenImgPrintable: ", e, 1);
	}
}

class GenImgPrintable
implements Printable
{
	public int
	print(Graphics g, PageFormat pf, int pageIndex)
	{
		if (pageIndex != 0)
			return (NO_SUCH_PAGE);

		runPrintCmd(g);
		return (PAGE_EXISTS);
	}
}

public abstract void printConsole(String s);
public abstract void clearConsole();

public void
printCurrentMousePosInfo()
{
	int imgX = imgSpaceX(getCurrentViewX());
	int imgY = imgSpaceY(getCurrentViewY());

	printConsole("color: " + "0x" +
		Integer.toHexString(this.getViewImgCanvas().getImgPixelAt(
			imgX, imgY) & 0x00ffffff) + " " +
		(this.getViewImgCanvas().getImgPixelAt(
			imgX, imgY) & 0x00ffffff) + " " +
		"img x,y: " + imgX + " " + imgY + " " +
		"view x,y: " +
			util.StringUtil.truncateVal(getCurrentViewX(), 1)
			+ " " +
			util.StringUtil.truncateVal(-getCurrentViewY(), 1)
		);
}

public void
initFromProperties(String fileName)
{
	setGenProperties(fileName);
}

public Properties genProperties = null;

public Properties
getGenProperties()
{
	return (genProperties);
}

public void
setGenProperties(String fileName)
{
	File propertiesFile = new File(fileName);

	/*
	try
	{
	*/
		propertiesFile = new File(fileName);
		if (propertiesFile == null)
		{
			debug("Can't find: " + fileName);
			return;
		}
		/* debug("propertiesFile.canRead: " + propertiesFile.canRead());
	}
	catch (Exception e)
	{
		handleException(e, 90);
	}
	*/

	try
	{
		FileInputStream propFile = new FileInputStream(propertiesFile);

		genProperties = new Properties(System.getProperties());
		genProperties.load(propFile);

		// set the system properties
		System.setProperties(genProperties);

		// list to standard out
		// System.getProperties().list(System.out);
	}
	catch (java.io.FileNotFoundException fnfe)
	{
		/*
		handleException("Exception in DrawObjectView.getGenProperties()"); " +
			fnfe.toString());
		*/
	}
	catch (java.io.IOException ioe)
	{
		/*
		System.err.println("Exception in getViewImageOpsProperties(): " +
			ioe.toString());
		*/
	}
}

public static void
handleException(String extraMsg, Throwable t, int id)
{
	switch(id)
	{
	  case 98 :
	  	debug("ComplexXMLParser Error:\n" + extraMsg + t.toString() + "\n");
		break;
	  default :
		ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
		t.printStackTrace(new PrintStream(
			new DataOutputStream(excptArray)));
		debug(id + " " + extraMsg + t.toString() +
			(new String(excptArray.toByteArray())));
		break;
	}
	if (id >= 100)
		System.exit(0);
}

public static void
handleException(Throwable t, int id)
{
	handleException(" ", t, id);
}

public void
genImgExit(java.awt.event.WindowEvent event)
{
	genImgExit();
}

public void
genImgExit()
{
	this.setVisible(false); // hide the Frame
	System.exit(0);	// close the application
}

private static void
debug(String s)
{
	System.err.println("DrawObjectView-> " + s);
}

}
