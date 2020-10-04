package ssview;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.print.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

// import javax.print.attribute.standard.MediaSize.ISO;

import jimage.DrawObjectView;
import jimage.ViewImgCanvas;


import util.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/* SAVE if want to go back to using JPDFPrinter.jar from QOPPA
import com.qoppa.pdfPrinter.PDFPrinterJob;
*/

public class
XRNABean
extends DrawObjectView
implements Printable, Serializable
{

public XRNABean()
{
	this.setComplexXMLParser(new ComplexXMLParser());
	// this.setImgType(BufferedImage.TYPE_INT_RGB);
}


protected transient PropertyChangeSupport
propertyChangeListeners = null;

private Object parentObject = null;

public void
setParentObject(Object parentObject)
{
	propertyChangeListeners = new PropertyChangeSupport(this);
	addPropertyChangeListener((PropertyChangeListener)parentObject);
}

public Object
getParentObject()
{
	return (this.parentObject);
}

public synchronized void
addPropertyChangeListener(PropertyChangeListener l)
{
	propertyChangeListeners.addPropertyChangeListener(l);
}

public synchronized void
removePropertyChangeListener(PropertyChangeListener l)
{
	propertyChangeListeners.removePropertyChangeListener(l);
}

private ComplexXMLParser complexXMLParser = null;

public void
setComplexXMLParser(ComplexXMLParser complexXMLParser)
{
   this.complexXMLParser = complexXMLParser;
}

public ComplexXMLParser
getComplexXMLParser()
{
   return (this.complexXMLParser);
}

private ComplexScene2D complexScene = null;

public void
setComplexScene(ComplexScene2D complexScene)
{
   this.complexScene = complexScene;
}

public ComplexScene2D
getComplexScene()
{
   return (this.complexScene);
}

public void
setComplexScene(SSData2D sstr)
throws Exception
{
	this.setComplexScene(new ComplexScene2D(sstr.getName(),
	new ComplexSSDataCollection2D(sstr.getName(), sstr)));
}

private String imageType = null;

public void
setImageType(String imageType)
{
   this.imageType = imageType;
}

public String
getImageType()
{
   return (this.imageType);
}

private int imageBorder = 0;

public void
setImageBorder(int imageBorder)
{
   this.imageBorder = imageBorder;
}

public int
getImageBorder()
{
   return (this.imageBorder);
}

public void
printImage(String outFileName)
throws ComplexException
{
	try
	{
		File genImgOutFile = new File(outFileName);

//		String testFileName = JAIUtil.printImage(this.getSceneImg(), genImgOutFile);
		ImageIO.write(this.getSceneImg(), "jpg", genImgOutFile);
		
	//	if (testFileName == null)
	//		debug("ERROR: IMAGE NOT PRINTED");
	//	else
	//		debug("Image printed to: " + testFileName);
	
	}
	catch (Exception e)
	{
		throw new ComplexException("Error, file not written:\n" + e.toString());
	}
}

public void
printImage(String imgType, OutputStream out)
throws ComplexException
{
	try
	{

//		JAIUtil.printImage(this.getSceneImg(), imgType, out);
		ImageIO.write(this.getSceneImg(), "jpg", out);

	
	}
	catch (Exception e)
	{
		throw new ComplexException("Error, outstream not written to:\n" + e.toString());
	}
}

public Hashtable
getNucRectangles()
{
	Hashtable nucRects = new Hashtable();

	// this isn't ready yet
	return (nucRects);
}

/******* DrawObjectView Implementation ***********************/

public void
clearConsole()
{
}

public void
printConsole(String s)
{
}

public void
buildGui()
{
	this.setViewImgCanvas(new ViewImgCanvas(this));
	this.getViewImgCanvas().setBackground(new Color(0xff9999ff));
	this.getViewImgCanvas().setOpaque(true);
}

public void
resetFigureScale(double val)
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
	if (this.getComplexScene() == null)
	{
		// debug("ComplexScene is NULL");
		return;
	}

	this.getDrawObjectList().add(this.getComplexScene());
}

public void
updateImgWindowView()
{
}

public void
updateAfterRender()
throws Exception
{
}

public void
runSetFromInputFile()
throws Exception
{
	try
	{
		this.getComplexXMLParser().parse(new FileReader(this.getCurrentInputFile().getName()));
	}
	catch (Exception e)
	{
		throw new ComplexException("can't parse: " + this.getCurrentInputFile().getName());
	}
	if (this.getComplexXMLParser().getComplexScene() == null)
		throw new ComplexException("can't parse: " + this.getCurrentInputFile() + "; complexScene not set");
	this.setComplexScene(this.getComplexXMLParser().getComplexScene());
	try
	{
		this.runFromComplexScene();
	}
	catch (Exception e)
	{
		throw new ComplexException("Can't render image for file: " + this.getCurrentInputFile().getName());
	}
}

public void
runSetFromString(String source, int width, int height)
throws Exception
{
	if (this.getComplexXMLParser() == null)
		throw new ComplexException("parser not available");

	try
	{
		this.getComplexXMLParser().parse(source);
	}
	catch (Exception e)
	{
		throw e;
	}
	if (this.getComplexXMLParser().getComplexScene() == null)
		throw new ComplexException("can't parse" + "; complexScene not set");
	this.setComplexScene(this.getComplexXMLParser().getComplexScene());
	try
	{
		this.runFromComplexScene(width, height);
	}
	catch (Exception e)
	{
		throw new ComplexException("Can't render image");
	}
}

public void
runSetFromString(String source, int width, int height, int border)
throws Exception
{
	if (this.getComplexXMLParser() == null)
		throw new ComplexException("parser not available");

	try
	{
		this.getComplexXMLParser().parse(source);
	}
	catch (Exception e)
	{
		throw e;
	}
	if (this.getComplexXMLParser().getComplexScene() == null)
		throw new ComplexException("can't parse" + "; complexScene not set");
	this.setComplexScene(this.getComplexXMLParser().getComplexScene());
	try
	{
		this.runFromComplexScene(width, height, border);
	}
	catch (Exception e)
	{
		throw new ComplexException("Can't render image");
	}
}

public void
runSetFromString(String source)
throws Exception
{
	if (source == null)
		throw new ComplexException("source null in runSetFromString()");
	if (this.getComplexXMLParser() == null)
		throw new ComplexException("parser not available in runSetFromString()");

	try
	{
		this.getComplexXMLParser().parse(source);
	}
	catch (Exception e)
	{
		throw e;
	}
	if (this.getComplexXMLParser().getComplexScene() == null)
		throw new ComplexException("can't parse" + "; complexScene not set");
	this.setComplexScene(this.getComplexXMLParser().getComplexScene());
	try
	{
		this.runFromComplexScene();
	}
	catch (Exception e)
	{
		throw new ComplexException("Can't render image");
	}
}

public void
runFromComplexScene()
throws Exception
{
	this.buildGui();
	this.renderDrawObjectView();
}

public void
runFromComplexScene(int width, int height)
throws Exception
{
	this.buildGui();
	this.renderDrawObjectView(width, height);
}

public void
runFromComplexScene(int width, int height, int border)
throws Exception
{
	this.buildGui();
	this.renderDrawObjectView(width, height, border);
}

public void
setInputFileName(String inputFileName)
throws Exception
{
	super.setInputFileName(inputFileName);
	InputStream streamIOFile = this.getClass().getResourceAsStream(this.getInputFileName());
	if (streamIOFile == null)
	{
		// debug("Can't find: " + xrnaIOFileName);
		throw new ComplexException("Can't find: " + this.getInputFileName());
	}

	if (streamIOFile.available() <= 0)
	{
		// debug("Can't find: " + xrnaIOFileName);
		throw new ComplexException(this.getInputFileName() + " has no available bytes");
	}

	byte[] chArray = new byte[streamIOFile.available()];
	int chCount = 0;
	while (true)
	{
		int ch = streamIOFile.read();
		if (ch == -1)
			break;
		//
		// if (ch == '<')
			// out.print("&lt;");
			// debug("FOUND LT");
			// chArray[chCount] = '<';
		// else
		//
			chArray[chCount] = (byte)ch;
		chCount++;
	}
	// debug("chCount: " + chCount);
	this.setInitXRNAIOParseString(new String(chArray, "US-ASCII"));
}

private String initXRNAIOParseString = null;

public void
setInitXRNAIOParseString(String initXRNAIOParseString)
{
   this.initXRNAIOParseString = initXRNAIOParseString;
}

public String
getInitXRNAIOParseString()
{
   return (this.initXRNAIOParseString);
}

/*
private boolean schematicize = false;

public void
setSchematicize(boolean schematicize)
{
   this.schematicize = schematicize;
}

public boolean
getSchematicize()
{
   return (this.schematicize);
}
*/

/******* End DrawObjectView Implementation ***********************/

public void
runPrinterJob(boolean showDialog, String jobName)
throws Exception
{
	PrinterJob pj = PrinterJob.getPrinterJob();
	PageFormat pf = pj.defaultPage();

	if (this.getLandscapeModeOn())
	{
		pf.setOrientation(PageFormat.LANDSCAPE);
		// pj.setPrintable((Printable)this, pf);
	}
	else
	{
		// pj.setPrintable((Printable)this);
	}
	Paper paper = new Paper();
	double margin = 36.0;
	paper.setImageableArea(margin, margin,
		paper.getWidth() - margin * 2,
		paper.getHeight() - margin * 2);
	pf.setPaper(paper);
	pj.setPrintable((Printable)this, pf);

	if (showDialog)
	{
		if (pj.printDialog())
		{
			try
			{
				pj.print();
			}
			catch (PrinterException e)
			{
				throw e;
				// handleEx("PrinterException in ComplexSceneView.runComplexPrinterJob0:", e, 101);
			}
			catch (Exception e)
			{
				throw e;
				// handleEx("Exception in ComplexSceneView.runComplexPrinterJob0:", e, 101);
			}
		}
	}
	else if (jobName != null)
	{
		pj.setJobName(jobName);
		pj.print();
	}
}



public void
oldRunPrintToOutputStream(OutputStream out, DocFlavor flavor, String mimeType)
throws Exception
{
	// Locate factory which can export a GIF image stream as Postscript
	StreamPrintServiceFactory[] factories =
		StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor, mimeType);
	if (factories.length == 0)
		throw new Exception("No suitable factories");

	// debug("FACTORY COUNT: " + factories.length);
	try
	{
		// Create a Stream printer for Postscript
		StreamPrintService sps = factories[0].getPrintService(out);

		// debug("PRINT SERvice attributes: " + sps.getSupportedAttributeValues());

		// Create and call a Print Job
		DocPrintJob pj = sps.createPrintJob();
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		// aset.add(new MediaPrintableArea(10, 10, 10, 10, MediaPrintableArea.INCH));
		// aset.add((Attribute)(new MediaSize(10.0f, 10.0f, MediaSize.INCH)));
		// aset.add(MediaSize.ISO.A4);
		// aset.add(MediaSizeName.ISO_A4); 
		// aset.add(new MediaPrintableArea(5, 5, 200, 300, MediaPrintableArea.MM));
		Attribute atts[] = aset.toArray();
		debug("atts.size: " + atts.length);
		

		Doc doc = new SimpleDoc(this, flavor, null);

		pj.print(doc, aset);
		out.close();
	}
	catch (PrintException pe)
	{ 
		System.err.println(pe);
		throw pe;
	}
	catch (IOException ie)
	{ 
		System.err.println(ie);
		throw ie;
	}
}

public void
runPrintToOutputStream(OutputStream out, DocFlavor flavor, String mimeType)
throws Exception
{
	PrinterJob job = PrinterJob.getPrinterJob();
	PageFormat pf = job.defaultPage();
		if (this.getLandscapeModeOn())
		pf.setOrientation(PageFormat.LANDSCAPE);
	Paper paper = new Paper();
	double margin = 36.0;
	paper.setImageableArea(margin, margin,
		paper.getWidth() - margin * 2,
		paper.getHeight() - margin * 2);
	pf.setPaper(paper);
	job.setPrintable((Printable)this, pf);

	// Locate factory which can export a GIF image stream as Postscript
	StreamPrintServiceFactory[] factories =
		StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor, mimeType);
	if (factories.length == 0)
		throw new Exception("No suitable factories");

	try
	{
		// Create a Stream printer for Postscript
		StreamPrintService sps = factories[0].getPrintService(out);
		job.setPrintService(sps); // if app wants to specify this printer.
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(MediaSizeName.ISO_A4); 
		aset.add(new MediaPrintableArea(5, 5, 200, 300, MediaPrintableArea.MM));
		job.print(aset);
		out.close();
	}
	catch (IOException ie)
	{ 
		System.err.println(ie);
		throw ie;
	}
}

public void
runPrintToFile(String fileName, DocFlavor flavor, String mimeType)
throws Exception
{
	this.runPrintToOutputStream(new FileOutputStream(fileName), flavor, mimeType);
}

public void
runPrintToFile(String fileName)
throws Exception
{
	// Use the pre-defined flavor for a Printable from an InputStream
	DocFlavor flavor = null;

	// Specify the type of the output stream
	String mimeType = null;

	if (fileName.toLowerCase().endsWith("ps"))
	{
		flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		mimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
	}
	else if (fileName.toLowerCase().endsWith("pdf"))
	{
		// NONE OF THIS WORKS:

		flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		// flavor = DocFlavor.INPUT_STREAM.PDF;
		mimeType = DocFlavor.BYTE_ARRAY.PDF.getMimeType();
	}

	if ((flavor == null) || (mimeType == null))
		throw new ComplexException("extension unknown of file: " + fileName);

	this.runPrintToFile(fileName, flavor, mimeType);
}

public void
runPrintToOutputStream(String imgType, OutputStream out)
throws Exception
{
	// Use the pre-defined flavor for a Printable from an InputStream
	DocFlavor flavor = null;

	// Specify the type of the output stream
	String mimeType = null;

	if (imgType.equalsIgnoreCase("ps"))
	{
		flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		mimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
	}
	else if (imgType.equalsIgnoreCase("pdf"))
	{
		// NONE OF THIS WORKS:

		flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		// flavor = DocFlavor.INPUT_STREAM.PDF;
		mimeType = DocFlavor.BYTE_ARRAY.PDF.getMimeType();
	}

	if ((flavor == null) || (mimeType == null))
		throw new ComplexException("image type unknown: " + imgType);

	this.runPrintToOutputStream(out, flavor, mimeType);
}

/*
Increasing the default Pixels per inch value on graphics2D before printing :
How can I set the number of pixels per inch at 144 instead of the default 72 value???
Indeed, I have 6 images (640 x 480 ), loading on a JPanel, that I want to draw on
a single A4 paper. This can be done by increasing the number of pixels per inch
(the print size will be reduced) before printing.

I don't do exactly what you do - I set the DPI to the actual printer's DPI instead
of 72. But you could do something similar by mucking with the AffineTransform.
This only works in JDK 1.2 style printing. I do this in my
print(Graphics g, PageFormat pgFormat, int iPageIndex)
method that gets called from the JDK printing model. See below .

Graphics2D g2 = (java.awt.Graphics2D)g;
AffineTransform tf = g2.getTransform();
AffineTransform oldTF = null;
int iResX = 72;
int iResY = 72;
if (tf.getScaleX() != 1 || tf.getScaleY() != 1) {
oldTF = new AffineTransform(tf);
if (pgFormat.getOrientation() == pgFormat.LANDSCAPE && tf.getScaleX() == 0 && tf.getScaleY() == 0) {
tf.rotate(Math.PI / 2.0);
}
double dOldScaleX = Math.abs(tf.getScaleX());
double dOldScaleY = Math.abs(tf.getScaleY());
iResX = (int)((dOldScaleX * 72.0) + 0.5);
iResY = (int)((dOldScaleY * 72.0) + 0.5);
tf.scale(1.0 / dOldScaleX, 1.0 / dOldScaleY);
if (pgFormat.getOrientation() == pgFormat.LANDSCAPE) {
tf.rotate(-Math.PI / 2.0);
}
g2.setTransform(tf);
}
// now the resolution is iResX, iResY and the AffineTransform for the printers Graphic is set such that we can draw in printer DPI
draw(g2); // call your drawing method
if (oldTF != null) {
g2.setTransform(oldTF); // reset to old transform
}
*/

/****************** Printable Implementation *******************/

public int
print(Graphics g, PageFormat pf, int pageIndex)
throws PrinterException
{
	if (pageIndex != 0)
		return (Printable.NO_SUCH_PAGE);

	Graphics2D g2 = (Graphics2D)g;	
	g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
		RenderingHints.VALUE_FRACTIONALMETRICS_ON);

	try
	{
		/*
		Rectangle2D outline =  new BRectangle2D(
			pf.getImageableX(), pf.getImageableY(),
			pf.getImageableWidth(), pf.getImageableHeight());
		g2.draw(outline);
		*/
		// this.draw(g2, null);

		// NEED to change this to send new getPSScale() by trying
		// to take a percentage of this.getScaleValue() with
		// this.getComplexScene().getPSScale()
		// or by providing a PSScale text field in xrna_controls
		this.printPSFromJDK(g2, this.getComplexScene().getPSScale());
	}
	catch (PrinterException e)
	{
		throw e;
	}
	catch (Exception e)
	{
		throw (new PrinterException("Exception in XRNABean.print(): " +
			e.toString()));
	}

	return (Printable.PAGE_EXISTS);
}

/****************** End Printable Implementation *******************/

private static void
debug(String s)
{
	System.err.println("XRNABean-> " + s);
}

}

