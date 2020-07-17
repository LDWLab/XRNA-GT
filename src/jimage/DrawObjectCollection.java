package jimage;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import jimage.DrawObject;

import util.*;
import util.math.BRectangle2D;

public abstract class
DrawObjectCollection
implements DrawObject
{

public static String javaVersion = null;
public static boolean isJDK13_0 = false;
public static boolean isJDK14_1_01 = false;

// this is the collection of any group of DrawObjects
private Collection collection = null;

public void
setCollection(Collection collection)
{
    this.collection = collection;
}

public Collection
getCollection()
{
    return (this.collection);
}

public int
getItemCount()
{
	if (this.getCollection() == null)
		return (0);
	return (this.getCollection().size());
}

// this refers to the parent collection of any DrawObject
private Object parentCollection = null;

public void
setParentCollection(Object parentCollection)
{
    this.parentCollection = parentCollection;
}

public Object
getParentCollection()
{
    return (this.parentCollection);
}

public abstract void addItem(Object sceneGraphNode) throws Exception;

public abstract Object getItemAt(int index);
public abstract void setItemAt(Object obj, int index);

public abstract void init() throws Exception;
public abstract void delete() throws Exception;

/**
** Find final drawObject under x,y.
** Can find path to drawObject by backing out through
** drawObject.getParentContainer until null.
** include/exclude types are for when leaf nodes are too mixed up
** to pick.
*/
public abstract DrawObject
findLeafNode(double x, double y, Vector includeTypes, Vector excludeTypes)
throws Exception;

/*
//public abstract void writeObj(String fileName) throws Exception;
public void
writeObj(String fileName)
throws Exception
{
	debug("WRITING TO: " + fileName);
	FileOutputStream ostream = new FileOutputStream(fileName);
	ObjectOutputStream p = new ObjectOutputStream(ostream);

	p.writeObject(this);

	p.flush();
	ostream.close();
}

public static SSData
readObj(String fileName)
throws Exception
{
	FileInputStream istream = new FileInputStream(fileName);
	ObjectInputStream p = new ObjectInputStream(istream);
	// int i = p.readInt();
	// String today = (String)p.readObject();
	// Date date = (Date)p.readObject();
	SSData returnSSData = (SSData)p.readObject();
	istream.close();
	return (returnSSData);
}
*/

/************** DrawObject Implementation ******************/

public void
set(Object obj)
throws Exception
{
}

private double scaleValue = 1.0;

public void
setScaleValue(double scaleValue)
{
	this.scaleValue = scaleValue;
}

public double
getScaleValue()
{
	return (this.scaleValue);
}

private double x = 0.0;

public void
setX(double x)
throws Exception
{
	this.x = x;
}

public double
getX()
throws Exception
{
	return (this.x);
}

private double y = 0.0;

public void
setY(double y)
throws Exception
{
	this.y = y;
}

public double
getY()
throws Exception
{
	return (this.y);
}

public void
setXY(double x, double y)
throws Exception
{
	this.setX(x);
	this.setY(y);
}

public void
setXY(Point2D pt)
throws Exception
{
	this.setX(pt.getX());
	this.setY(pt.getY());
}

public void
shiftX(double x)
throws Exception
{
	this.setX(this.getX() - x);
}

public void
shiftY(double y)
throws Exception
{
	this.setY(this.getY() - y);
}

public void
shiftXY(double x, double y)
throws Exception
{
	this.shiftX(x);
	this.shiftY(y);
}

private double deltaX = 0.00;

public void
setDeltaX(double deltaX)
{
	this.deltaX = deltaX;
}

public double
getDeltaX()
{
	return (this.deltaX);
}

private double deltaY = 0.00;

public void
setDeltaY(double deltaY)
{
	this.deltaY = deltaY;
}

public double
getDeltaY()
{
	return (this.deltaY);
}

private Point2D point2D = null;

public Point2D
getPoint2D()
throws Exception
{
	if (point2D == null)
		point2D = new Point2D.Double();

	point2D.setLocation(this.getX(), this.getY());
	return (point2D);
}

private Hashtable saveLocationHashtable = null;

public void
setSaveLocationHashtable(Hashtable saveLocationHashtable)
{
    this.saveLocationHashtable = saveLocationHashtable;
}

public Hashtable
getSaveLocationHashtable()
{
    return (this.saveLocationHashtable);
}

public void
runSetLocationHashtable(int level)
throws Exception
{
	if (this.getSaveLocationHashtable() == null)
		this.setSaveLocationHashtable(new Hashtable());
	this.getSaveLocationHashtable().put(new Integer(level),
		new Point2D.Double(this.getX(), this.getY()));
}

public boolean
resetLocationFromHashtable(int level)
throws Exception
{
	if (this.getSaveLocationHashtable() == null)
		return (false);
	Point2D pt = (Point2D)this.getSaveLocationHashtable().get(new Integer(level));
	if (pt == null)
		return (false);
	this.setXY(pt.getX(), pt.getY());

	return (true);
}

public void
clearLocationFromHashtable(int level)
throws Exception
{
	if (this.getSaveLocationHashtable() == null)
		return;
	this.getSaveLocationHashtable().remove(new Integer(level));
	if (this.getSaveLocationHashtable().size() <= 0)
		this.setSaveLocationHashtable(null);
}

public Point2D.Double
getImgSpacePoint2D()
throws Exception
{
	AffineTransform affTr = ((DrawObjectCollection)
		this.getParentCollection()).getG2Transform();
	Point2D.Double nodePt =
		new Point2D.Double(this.getX(), -this.getY());
	Point2D.Double testPt = new Point2D.Double();
	affTr.transform(nodePt, testPt);
	return(testPt);
}

private Color color = Color.black;

public void
setColor(Color color)
throws Exception
{
    this.color = color;
}

public Color
getColor()
{
    return (this.color);
}

private Color editColor = null;

public void
setEditColor(Color editColor)
throws Exception
{
    this.editColor = editColor;
}

public Color
getEditColor()
{
    return (this.editColor);
}

private boolean isEditable = true;

public void
setIsEditable(boolean isEditable)
{
    this.isEditable = isEditable;
}

public boolean
getIsEditable()
throws Exception
{
    return (this.isEditable);
}

public BRectangle2D boundingBox = null;

public void
setBoundingBox(BRectangle2D rect)
{
	this.boundingBox = rect;
}

public BRectangle2D
getBoundingBox()
{
	return (this.boundingBox);
}

// get objects bounding box centered around objects x,y
public BRectangle2D
getCenteredBoundingBox()
throws Exception
{
	if (this.getBoundingBox() == null)
		return (null);

	BRectangle2D rect = this.getBoundingBox();
	return (new BRectangle2D(
		this.getX() - (rect.getWidth()/2.0),
		this.getY() + (rect.getHeight()/2.0),
		rect.getWidth(), rect.getHeight()));
}

private boolean showBoundingBox = false;

public void
setShowBoundingBox(boolean showBoundingBox)
{
	this.showBoundingBox = showBoundingBox;
}

public boolean
getShowBoundingBox()
{
	return (this.showBoundingBox);
}

public void
drawBoundingBox(Graphics2D g2)
{
	if (this.getBoundingBox() == null)
		return;
	BasicStroke sveStroke = (BasicStroke)g2.getStroke();
	RenderingHints sveRenderHints = g2.getRenderingHints();

	g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
	GraphicsUtil.drawAxisRect(this.getBoundingBox(), g2);

	g2.setStroke(sveStroke);
	g2.setRenderingHints(sveRenderHints);
}

private Shape boundingShape = null;

public void
setBoundingShape(Shape boundingShape)
{
    this.boundingShape = boundingShape;
}

public Shape
getBoundingShape()
{
    return (this.boundingShape);
}

private boolean showBoundingShape = false;

public void
setShowBoundingShape(boolean showBoundingShape)
{
	this.showBoundingShape = showBoundingShape;
}

public boolean
getShowBoundingShape()
{
	return (this.showBoundingShape);
}

public void
drawBoundingShape(Graphics2D g2)
{
	if (this.getBoundingShape() == null)
		return;

	// contingent on setting bounding shape simply to bounding box
	if (this.getBoundingShape() instanceof BRectangle2D)
	{
		this.drawBoundingBox(g2);
		return;
	}

	BasicStroke sveStroke = (BasicStroke)g2.getStroke();
	RenderingHints sveRenderHints = g2.getRenderingHints();

	g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
	g2.draw(this.getBoundingShape());

	g2.setStroke(sveStroke);
	g2.setRenderingHints(sveRenderHints);
}

// NEED to replace box with a shape and use its contains
public boolean
contains(double x, double y)
throws Exception
{
	if (this.getBoundingShape() == null)
		return (false);
	return (this.getIsPickable() && this.getBoundingShape().contains(x, y));
}

public boolean
contains(DrawObject drawObject)
throws Exception
{
	// need to go back up tree until drawObject is at this,
	// then see it's boundary is within this boundary
	return (false);
}

private static BRectangle2D checkThisRectangle = new BRectangle2D();
private static Point2D intersectTransPt = new Point2D.Double();
private static Point2D intersectNewPt = new Point2D.Double();

public boolean
intersects(BRectangle2D rect, Graphics2D g2)
throws Exception
{
	if (this.getBoundingBox() == null)
		throw new Exception("ERROR in intersects(), bounding box is null");

	checkThisRectangle.setRect(this.getBoundingBox());
	double scaleVal = g2.getTransform().getScaleX();
	intersectTransPt.setLocation(this.getBoundingBox().getX(), this.getBoundingBox().getY());
	g2.getTransform().transform(intersectTransPt, intersectNewPt);
	checkThisRectangle.setRect(intersectNewPt.getX(), intersectNewPt.getY(),
		scaleVal * checkThisRectangle.getWidth(), scaleVal * checkThisRectangle.getHeight());

	return (checkThisRectangle.intersects(rect));
}

private boolean isPickable = true;

public void
setIsPickable(boolean isPickable)
{
    this.isPickable = isPickable;
}

public boolean
getIsPickable()
{
    return (this.isPickable);
}

// update is always called from draw and inherits the shift of x,y
// from transform
public void
update(Graphics2D g2)
throws Exception
{
	// MAYBE get rid of all this
	this.setDrawObjectImg(null);

	AffineTransform at = g2.getTransform();
	double scaleVal = at.getScaleX();
	if (scaleVal == 0.0)
	{
		double mat[] = new double[6];
		at.getMatrix(mat);
		scaleVal = mat[2];
	}

	this.setScaleValue(scaleVal);
}

public void
update()
throws Exception
{
	((DrawObject)this).update(GraphicsUtil.unitG2);
}

private boolean isHidden = false;

public void
setIsHidden(boolean isHidden)
throws Exception
{
    this.isHidden = isHidden;
}

public boolean
getIsHidden()
{
    return (this.isHidden);
}

public boolean
isHidden()
{
    return (this.getIsHidden());
}

private boolean hideForConstrain = false;

public void
setHideForConstrain(boolean hideForConstrain)
throws Exception
{
    this.hideForConstrain = hideForConstrain;
}

public boolean
getHideForConstrain()
{
    return (this.hideForConstrain);
}

private BufferedImage drawObjectImg = null;

public void
setDrawObjectImg(BufferedImage drawObjectImg)
{
    this.drawObjectImg = drawObjectImg;
}

public BufferedImage
getDrawObjectImg()
throws Exception
{
    return (this.drawObjectImg);
}

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
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
}

public void
erase(Graphics2D g2)
throws Exception
{
	if (this.getBoundingShape() == null)
		return;
	g2.setColor(g2.getBackground());
	g2.fill(this.getBoundingShape());
}

public void
clearBoundingBoxArea(Graphics2D g2)
throws Exception
{
	if (this.getBoundingBox() == null)
		return;

	RenderingHints sveRenderingHints = g2.getRenderingHints();
	g2.setRenderingHints(GraphicsUtil.unAliasedRenderHints);

	g2.setColor(g2.getBackground());
	// g2.setColor(Color.green);
	// g2.setColor(Color.red);
	// g2.setColor(Color.gray);
	// debug("BB: " + this.getBoundingBox());

	// so far I think I should use BB until I can understand
	// BS of a triangle, etc. Right now I use BS of a triangle
	// to draw the outline of it.
	// g2.fill(this.getBoundingShape());
	g2.fill(this.getBoundingBox());

	g2.setRenderingHints(sveRenderingHints);
}

public void
delete(Graphics2D g2)
throws Exception
{
	this.erase(g2);
}

private AffineTransform g2Transform = null;

public void
setG2Transform(AffineTransform g2Transform)
{
    this.g2Transform = g2Transform;
}

public AffineTransform
getG2Transform()
{
    return (this.g2Transform);
}

public AffineTransform
getParentG2Transform()
{
	return (((DrawObjectCollection)this.getParentCollection()).getG2Transform());
}

/*
private RenderingHints renderHints = null;

public void
setRenderHints(RenderingHints renderHints)
{
    this.renderHints = renderHints;
}

public RenderingHints
getRenderHints()
{
    return (this.renderHints);
}
*/

public void
showProperties(Component parent)
{
	/*
	setParentComponent(parent);
	Object[]      message = new Object[1];
	JButton fontChooserBt = null;
	fontChooserBt = new JButton();
	fontChooserBt.setText("Select Font");
	fontChooserBt.setActionCommand("Select Font");
	fontChooserBt.setFont(new Font("Dialog", Font.BOLD, 12));
	fontChooserBt.addMouseListener(new nuc2DMouseListener()
	{
		public void
		mouseClicked(MouseEvent event)
		{
			//
			// if (fontChooser == null)
				// fontChooser = new FontChooser(parent);
			// fontChooser.setVisible(true);
			//
			NucModeFontChooser fontChooser =
				new NucModeFontChooser(Nuc2D.this, (Frame)getParentComponent());
			fontChooser.setVisible(true);
		}
	});

	// message[0] = cb;
	message[0] = fontChooserBt;

	// Options
	String[] options =
	{
		"Ok",
		"Cancel"
	};
	int result = JOptionPane.showOptionDialog(
		parent,                             // the parent that the dialog blocks
		message,                                    // the dialog message array
		"Nuc Properties", // the title of the dialog window
		JOptionPane.OK_CANCEL_OPTION,                 // option type
		JOptionPane.PLAIN_MESSAGE,            // message type
		null,                                       // optional icon, use null to use the default icon
		options,                                    // options string array, will be made into buttons
		options[0]                                  // option that should be made into a default button
		);

	// debug ("NUC MODE: " + (String)cb.getSelectedItem());
	switch(result)
	{
	   case 0: // yes
		 JOptionPane.showMessageDialog(parent, "Made it 0");
		 break;
	   case 1: // no
		 JOptionPane.showMessageDialog(parent, "Made it 1");
		 break;
	   default:
		 break;
	}
	*/
}

public BufferedImage
drawImage(int imgW, int imgH, Color bgColor,
	Color topColor, Color bottomColor, int depth, int imgType)
throws Exception
{
	BufferedImage bufImg = new BufferedImage(imgW, imgH, imgType);
	Graphics2D g2 = bufImg.createGraphics();

	g2.setRenderingHints(GraphicsUtil.imageRenderHints);

	g2.setColor(bgColor);
	g2.fillRect(0, 0, imgW, imgH);

	//
	// show center for debug only:
	// g2.setColor(Color.black);
	// g2.fillRect(imgW/2, imgH/2, 1, 1);
	//

	if (depth > 0)
		GraphicsUtil.drawDepthLines(g2, 0, 0, imgW, imgH, depth,
			0, topColor, bottomColor);

	this.draw(g2, null);

	return (bufImg);
}

/************** DrawObject Implementation ******************/

private static void
debug(String s)
{
	System.out.println("DrawObjectCollection-> " + s);
}

}
