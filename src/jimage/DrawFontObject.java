package jimage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.font.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import util.*;
import util.math.*;

public abstract class
DrawFontObject
extends DrawObjectLeafNode
{

private Font font = null;

public void
setFont(Font font)
{
    this.font = font;
}

public Font
getFont()
{
    return (this.font);
}

private String drawString = null;

public void
setDrawString(String drawString)
{
    this.drawString = drawString;
}

public String
getDrawString()
{
    return (this.drawString);
}

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

class
LikeConstraints
{
	private
	LikeConstraints(Graphics2D g2, Font font, Color color, Color bgColor, String string)
	{
		if (font == null)
			return;
		
		// get the current scaleval from g2
		AffineTransform at = g2.getTransform();
		double scaleX = at.getScaleX();
		double scaleY = at.getScaleY();

		// scaleX and scaleY should always be same
		double scaleVal = scaleX;

		// NEED to address this BUGGGGGG
		if (scaleVal == 0.0)
		{
			double mat[] = new double[6];
			at.getMatrix(mat);
			scaleVal = mat[2];
			scaleX = scaleVal;
		}

		// NEED to work out stuff less than 1.0
		if (scaleVal < 1.0)
			scaleVal = 1.0;

		// NEEDs to be passed in g2
		g2.setRenderingHints(GraphicsUtil.stringAliasedRenderHints);

		g2.setFont(font);
		FontRenderContext frc = g2.getFontRenderContext();
		GlyphVector gv = g2.getFont().createGlyphVector(frc, string);

		ShapeGraphicAttribute shapeAtt = new ShapeGraphicAttribute(
			gv.getOutline(), ShapeGraphicAttribute.BOTTOM_ALIGNMENT, false); 
		Rectangle2D rect = shapeAtt.getBounds();
		Rectangle2D gvRect = gv.getLogicalBounds();
		
		BufferedImage tmpImg = new BufferedImage(
			(int)scaleVal * ((int)Math.round(rect.getWidth()) + 10),
			(int)scaleVal * ((int)Math.round(rect.getHeight()) + 10),
			BufferedImage.TYPE_INT_RGB);

		Graphics2D g2i = tmpImg.createGraphics();
		g2i.scale(scaleX, scaleY);
		// NEEDs to be passed in g2
		g2i.setRenderingHints(GraphicsUtil.stringAliasedRenderHints);

		g2i.setFont(font);
		g2i.setBackground(bgColor);
		g2i.setColor(bgColor);
		g2i.fillRect(0, 0, tmpImg.getWidth()/(int)Math.round(scaleVal),
			tmpImg.getHeight()/(int)Math.round(scaleVal));
		g2i.setColor(color);
		float drawStrX = (float)Math.round(((float)((tmpImg.getWidth() -
			(scaleX * gvRect.getWidth()))/2.0))/(float)scaleX);
		float drawStrY = (float)Math.round((
			(float)tmpImg.getHeight() - ((float)((tmpImg.getHeight() -
			(scaleX * rect.getHeight()))/2.0)))/(float)scaleX);
		g2i.drawString(string, drawStrX, drawStrY);

		// debug threshold problem (draw seems to put in an extra 514 or 1028)
		int threshold = 7;
		int bgRGB = bgColor.getRGB() & 0x00ffffff;
		int bgRedPixel = (bgRGB & 0x00ff0000) >> 16;
		int bgGreenPixel = (bgRGB & 0x0000ff00) >> 8;
		int bgBluePixel = bgRGB & 0x000000ff;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = -Integer.MAX_VALUE;
		int maxY = -Integer.MAX_VALUE;
		
		for (int col = 0;col < tmpImg.getWidth();col++)
		for (int row = 0;row < tmpImg.getHeight();row++)
		{
			int tmpColor = tmpImg.getRGB(col, row) & 0x00ffffff;
			if ((tmpColor != bgRGB) &&
			(((int)Math.abs(bgRedPixel - ((tmpColor & 0x00ff0000) >> 16) ) > threshold) ||
			((int)Math.abs(bgGreenPixel - ((tmpColor & 0x0000ff00) >> 8) ) > threshold) ||
			((int)Math.abs(bgBluePixel - (tmpColor & 0x000000ff) ) > threshold)))
			{
				//
				// debug("COL: " + col + " " + row + " " + Integer.toHexString(tmpColor));
				// debug("\t" + "red diff: " + ((int)Math.abs(bgRedPixel - (tmpColor & 0x00ff0000))));
				// debug("\t" + "green diff: " + ((int)Math.abs(bgGreenPixel - (tmpColor & 0x0000ff00))));
				// debug("\t" + "blue diff: " + ((int)Math.abs(bgBluePixel - (tmpColor & 0x000000ff))));
				//

				if (minX > col)
					minX = col;
				if (minY > row)
					minY = row;
				if (maxX < col)
					maxX = col;
				if (maxY < row)
					maxY = row;
			}
			//
			// else if (tmpColor != bgRGB)
				// debug("letting pixel through: " + col + " " + row + " " + Integer.toHexString(tmpColor));
			//
		}

		if ((minX == Integer.MAX_VALUE) ||
			(minY == Integer.MAX_VALUE) ||
			(maxX == -Integer.MAX_VALUE) ||
			(maxY == -Integer.MAX_VALUE))
		{
			/*
			throw new Exception("Error in DrawFontObject: " +
				"Bounding Box not found for " + string + "\n");
			*/
			System.out.println("Error in DrawFontObject: " +
				"Bounding Box not found for " + string + "\n");
		}
		minX--;
		minY--;
		maxX++;
		maxY++;

		// save to show where str x,y and min/max x,y are
		// tmpImg.setRGB((int)scaleX * (int)Math.round(drawStrX),
			// (int)scaleX * (int)Math.round(drawStrY), 0xff00ff00);
		// 
		// if ((minX > 0) && (minY > 0))
			// tmpImg.setRGB(minX, minY, 0xff000000);
		// if ((maxX < tmpImg.getWidth() - 1) && (maxY < tmpImg.getHeight() - 1))
			// tmpImg.setRGB(maxX, maxY, 0xff000000);
		//

		this.setLikeFont(font);
		this.setLikeScaleVal(scaleVal);
		this.setLikeColor(color);
		this.setLikeString(string);
		this.setDrawStrW(maxX - minX);
		this.setDrawStrH(maxY - minY);
		this.setStrBBoxXDiff(((int)Math.round(scaleX * drawStrX)) - minX);
		this.setStrBBoxYDiff(((int)Math.round(scaleX * drawStrY)) - maxY);
	}

	private Font likeFont = null;

	public void
	setLikeFont(Font likeFont)
	{
		this.likeFont = likeFont;
	}

	public Font
	getLikeFont()
	{
		return (this.likeFont);
	}

	private double likeScaleVal = 0.0;

	public void
	setLikeScaleVal(double likeScaleVal)
	{
		this.likeScaleVal = likeScaleVal;
	}

	public double
	getLikeScaleVal()
	{
		return (this.likeScaleVal);
	}

	private Color likeColor = null;

	public void
	setLikeColor(Color likeColor)
	{
		this.likeColor = likeColor;
	}

	public Color
	getLikeColor()
	{
		return (this.likeColor);
	}

	private String likeString = null;

	public void
	setLikeString(String likeString)
	{
		this.likeString = likeString;
	}

	public String
	getLikeString()
	{
		return (this.likeString);
	}

	private int strBBoxXDiff = 0;

	public void
	setStrBBoxXDiff(int strBBoxXDiff)
	{
		this.strBBoxXDiff = strBBoxXDiff;
	}

	public int
	getStrBBoxXDiff()
	{
		return (this.strBBoxXDiff);
	}

	private int strBBoxYDiff = 0;

	public void
	setStrBBoxYDiff(int strBBoxYDiff)
	{
		this.strBBoxYDiff = strBBoxYDiff;
	}

	public int
	getStrBBoxYDiff()
	{
		return (this.strBBoxYDiff);
	}

	private int drawStrW = 0;

	public void
	setDrawStrW(int drawStrW)
	{
		this.drawStrW = drawStrW;
	}

	public int
	getDrawStrW()
	{
		return (this.drawStrW);
	}

	private int drawStrH = 0;

	public void
	setDrawStrH(int drawStrH)
	{
		this.drawStrH = drawStrH;
	}

	public int
	getDrawStrH()
	{
		return (this.drawStrH);
	}

	public String
	toString()
	{
		return("" +
			getLikeString() + " " +
			getLikeScaleVal() + " " +
			getLikeColor().getRGB() + " " +
			getStrBBoxXDiff() + " " +
			getStrBBoxYDiff() + " " +
			getDrawStrW() + " " +
			getDrawStrH());
	}
}

static Vector likeConstraintList = null;
static Hashtable constraintHashTable = new Hashtable();

public static LikeConstraints
lookUpLikeConstraints(Font font, double scaleVal, Color color, String string)
{
	likeConstraintList = (Vector)constraintHashTable.get(new Double(scaleVal));
	if (likeConstraintList == null)
	{
		likeConstraintList = new Vector();
		constraintHashTable.put(new Double(scaleVal), likeConstraintList);
		return (null);
	}

	for (Enumeration e = likeConstraintList.elements();e.hasMoreElements();)
	{
		LikeConstraints constraint = (LikeConstraints)e.nextElement();
		if (!constraint.getLikeFont().equals(font))
			continue;
		if (constraint.getLikeScaleVal() != scaleVal)
			continue;
		if (constraint.getLikeColor().getRGB() != color.getRGB())
			continue;
		if (!constraint.getLikeString().equals(string))
			continue;
		
		return (constraint);
	}

	return (null);
}

/* Might want to look at this as a means of encompassing box:
 DESCRIPTION OF THE PROBLEM :
Draw a text with Graphics and draw a rectangle
around the text. The rectangle has the same with
as the text.
When you scale the text and rectangle using AffineTransform
the text won't still be inside the rectangle. The result
differs when you use other fonts. The scaled text may
be much smaller than the surrounding rectangle.

STEPS TO FOLLOW TO REPRODUCE THE PROBLEM :
1. start the following code example
2.
3.

This bug can be reproduced always.

---------- BEGIN SOURCE ----------
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Test {

    public Test() {

	JFrame          frame;
	Container       contentpane;
	JPanel          panel;

	frame = new JFrame("Test");
        frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e) {

		    System.exit(0);
		}
            });

	contentpane = frame.getContentPane();
	contentpane.setLayout(new BorderLayout());

	contentpane.add(new MyPanel(), BorderLayout.CENTER);
	frame.pack();
	frame.setSize(500,500);
	frame.show();
    }

    public static void main(String strArg[]) {

	new Test();
    }


    private class MyPanel extends JPanel {

	public MyPanel() {

	    super();
	}

	public void paint(Graphics g) {

	    FontMetrics      fm;
	    Font             font;
	    String           strText;
	    Graphics2D       g2;
	    AffineTransform  at;

	    int              iW;
	    int              iAsc;
	    int              iDesc;

	    strText = "Text1234567890";
	    font = new Font("Courier", Font.PLAIN, 12);

	    super.paint(g);

	    g2 = (Graphics2D) g;
	    at = new AffineTransform();
	    at.setToScale(4,4);
	    g2.setTransform(at);

	    g.setFont(font);
	    g.drawString(strText, 20,20);

	    fm = getFontMetrics(font);
	    iW = fm.stringWidth(strText);
	    iAsc = fm.getMaxAscent();
	    iDesc = fm.getMaxDescent();

	    g.drawRect(20,20-iAsc,iW+1,iAsc+iDesc);
	}
    }
}


---------- END SOURCE ----------
(Review ID: 145871)


webbug
 
Workaround  None.
 
Evaluation  This is user error - not a bug and provides a neat illustration of the
importance of measuring text in the context in which it will be displayed.
Editing one key line of this user's program fixes the programming error.

Change
fm = getFontMetrics(font);
To
fm = g.getFontMetrics(font);

And it works perfectly.
This gets the metrics from the graphics which knows about the
AffineTransform and other font hints (such as Fractional Metrics).
Without this information its impossible to measure text properly because
fonts aren't designed to scale linearly. This is what the font designer
intended.

Also this program does something which should not be done in general
and definitely not in a Swing application.
It *SETS* the transform rather than concatenating it.
This means any transform previously in effect is obliterated.
This code would not print properly and doesn't repaint properly either.
Try hiding part (not all) of the window behind another window and
then exposing it - you'll see it repaints in the wrong place.

You should almost always call transform - NOT setTransform
*/

public void
update(Graphics2D g2)
throws Exception
{
	int drawStrW = 0;
	int drawStrH = 0;
	int strBBoxXDiff = 0;
	int strBBoxYDiff = 0;

	// get the current scaleval from g2
	AffineTransform at = g2.getTransform();
	double scaleX = at.getScaleX();
	double scaleVal = scaleX;

	// NEED to address this BUGGGGGG
	if (scaleVal == 0.0)
	{
		double mat[] = new double[6];
		at.getMatrix(mat);
		scaleVal = mat[2];
		scaleX = scaleVal;
	}
	/* NEED to figure out why this can't replace above: (probably cause
	** doesn't call super.update(g2)
	double scaleX = this.getViewScale();
	double scaleVal = scaleX;
	*/

	// NEED to work out stuff less than 1.0
	if (scaleVal < 1.0)
		scaleVal = 1.0;

	LikeConstraints	likeConstraints =
		this.lookUpLikeConstraints(this.getFont(), scaleVal, this.getColor(), this.getDrawString());

	if (likeConstraints == null)
	{
		likeConstraints = new LikeConstraints(
			g2, this.getFont(), this.getColor(),
			this.getDrawImgBGColor(), this.getDrawString());
		// debug("ADDING CONSTRAINT: " + likeConstraints);
		likeConstraintList.add(likeConstraints);
	}
	else
	{
		// debug("FOUND OLD CONSTRAINT: " + likeConstraints);
	}

	drawStrW = likeConstraints.getDrawStrW();
	drawStrH = likeConstraints.getDrawStrH();
	strBBoxXDiff = likeConstraints.getStrBBoxXDiff();
	strBBoxYDiff = likeConstraints.getStrBBoxYDiff();
	double halfDrawStrW = ((double)drawStrW)/2.0;
	double halfDrawStrH = ((double)drawStrH)/2.0;

	/*
	debug("\nFOR string: " + this.getDrawString());
	debug("drawStrW: " + drawStrW);
	debug("drawStrH: " + drawStrH);
	debug("strBBoxXDiff: " + strBBoxXDiff);
	debug("strBBoxYDiff: " + strBBoxYDiff);
	*/
	
	/*
	if (isJDK13_0)
	{
	*/
		this.setDeltaX((halfDrawStrW - (double)strBBoxXDiff)/scaleX);
		this.setDeltaY((halfDrawStrH + (double)strBBoxYDiff)/scaleX);
	/*
	}
	else if (isJDK14_1_01)
	{
		this.setDeltaX((halfDrawStrW - (double)strBBoxXDiff)/scaleX);
		this.setDeltaY((halfDrawStrH + (double)strBBoxYDiff)/scaleX);
	}
	*/

	this.setBoundingBox(new BRectangle2D(
		this.getX() - halfDrawStrW/scaleX,
		-this.getY() - halfDrawStrH/scaleX,
		(double)drawStrW/scaleX, (double)drawStrH/scaleX));

	// simplest bounding shape for now
	this.setBoundingShape(this.getBoundingBox());

	if (isJDK14_1_01)
	{
		this.setDeltaX((halfDrawStrW - (double)strBBoxXDiff + 0.5)/scaleX);
		this.setDeltaY((halfDrawStrH + (double)strBBoxYDiff - 0.5)/scaleX);
	}
}

/*
public void
oldUpdate(Graphics2D g2)
throws Exception
{
	if (this.getFont() == null)
		return;
	// NEEDs to be passed in g2
	g2.setRenderingHints(GraphicsUtil.aliasedRenderHints);

	g2.setFont(this.getFont());
	FontRenderContext frc = g2.getFontRenderContext();
	GlyphVector gv = g2.getFont().createGlyphVector(frc,
		this.getDrawString());

	ShapeGraphicAttribute shapeAtt = new ShapeGraphicAttribute(
		gv.getOutline(), ShapeGraphicAttribute.BOTTOM_ALIGNMENT, false); 
	Rectangle2D rect = shapeAtt.getBounds();
	Rectangle2D gvRect = gv.getLogicalBounds();

	// get the current scaleval from g2
	AffineTransform at = g2.getTransform();
	double scaleX = at.getScaleX();
	double scaleY = at.getScaleY();

	// scaleX and scaleY should always be same
	double scaleVal = scaleX;
	// NEED to work out stuff less than 1.0
	if (scaleVal < 1.0)
		scaleVal = 1.0;
	
	BufferedImage tmpImg = new BufferedImage(
		(int)scaleVal * ((int)Math.round(rect.getWidth()) + 10),
		(int)scaleVal * ((int)Math.round(rect.getHeight()) + 10),
		BufferedImage.TYPE_INT_RGB);

	Graphics2D g2i = tmpImg.createGraphics();
	g2i.scale(scaleX, scaleY);
	// NEEDs to be passed in g2
	g2i.setRenderingHints(GraphicsUtil.aliasedRenderHints);

	g2i.setFont(this.getFont());
	g2i.setBackground(this.getDrawImgBGColor());
	g2i.setColor(this.getDrawImgBGColor());
	g2i.fillRect(0, 0, tmpImg.getWidth()/(int)Math.round(scaleVal),
		tmpImg.getHeight()/(int)Math.round(scaleVal));
	g2i.setColor(this.getColor());
	float drawStrX = (float)Math.round(((float)((tmpImg.getWidth() -
		(scaleX * gvRect.getWidth()))/2.0))/(float)scaleX);
	float drawStrY = (float)Math.round((
		(float)tmpImg.getHeight() - ((float)((tmpImg.getHeight() -
		(scaleX * rect.getHeight()))/2.0)))/(float)scaleX);
	g2i.drawString(this.getDrawString(), drawStrX, drawStrY);
	
	// debug threshold problem (draw seems to put in an extra 514 or 1028)
	int threshold = 7;
	int bgRGB = this.getDrawImgBGColor().getRGB() & 0x00ffffff;
	int bgRedPixel = (bgRGB & 0x00ff0000) >> 16;
	int bgGreenPixel = (bgRGB & 0x0000ff00) >> 8;
	int bgBluePixel = bgRGB & 0x000000ff;
	int minX = Integer.MAX_VALUE;
	int minY = Integer.MAX_VALUE;
	int maxX = -Integer.MAX_VALUE;
	int maxY = -Integer.MAX_VALUE;
	
	for (int col = 0;col < tmpImg.getWidth();col++)
	for (int row = 0;row < tmpImg.getHeight();row++)
	{
		int color = tmpImg.getRGB(col, row) & 0x00ffffff;
		if ((color != bgRGB) &&
		(((int)Math.abs(bgRedPixel - ((color & 0x00ff0000) >> 16) ) > threshold) ||
		((int)Math.abs(bgGreenPixel - ((color & 0x0000ff00) >> 8) ) > threshold) ||
		((int)Math.abs(bgBluePixel - (color & 0x000000ff) ) > threshold)))
		{
			//
			// debug("COL: " + col + " " + row + " " + Integer.toHexString(color));
			// debug("\t" + "red diff: " + ((int)Math.abs(bgRedPixel - (color & 0x00ff0000))));
			// debug("\t" + "green diff: " + ((int)Math.abs(bgGreenPixel - (color & 0x0000ff00))));
			// debug("\t" + "blue diff: " + ((int)Math.abs(bgBluePixel - (color & 0x000000ff))));
			//

			if (minX > col)
				minX = col;
			if (minY > row)
				minY = row;
			if (maxX < col)
				maxX = col;
			if (maxY < row)
				maxY = row;
		}
		//
		// else if (color != bgRGB)
			// debug("letting pixel through: " + col + " " + row + " " + Integer.toHexString(color));
		//
	}

	if ((minX == Integer.MAX_VALUE) ||
		(minY == Integer.MAX_VALUE) ||
		(maxX == -Integer.MAX_VALUE) ||
		(maxY == -Integer.MAX_VALUE))
		throw new Exception("Error in DrawFontObject: " +
			"Bounding Box not found for " + this.toString() + "\n");
	minX--;
	minY--;
	maxX++;
	maxY++;

	// save to show where str x,y and min/max x,y are
	// tmpImg.setRGB((int)scaleX * (int)Math.round(drawStrX),
		// (int)scaleX * (int)Math.round(drawStrY), 0xff00ff00);
	// 
	// if ((minX > 0) && (minY > 0))
		// tmpImg.setRGB(minX, minY, 0xff000000);
	// if ((maxX < tmpImg.getWidth() - 1) && (maxY < tmpImg.getHeight() - 1))
		// tmpImg.setRGB(maxX, maxY, 0xff000000);
	//

	int drawStrW = maxX - minX;
	int drawStrH = maxY - minY;

	this.setDrawObjectImg(
		tmpImg.getSubimage(minX + 1, minY + 1, drawStrW - 1, drawStrH - 1));

	int strBBoxXDiff = ((int)Math.round(scaleX * drawStrX)) - minX;
	int strBBoxYDiff = ((int)Math.round(scaleX * drawStrY)) - maxY;

	this.setStrBBoxXDiff(strBBoxXDiff);
	this.setStrBBoxYDiff(strBBoxYDiff);

	setDeltaX((((double)drawStrW)/2.0 - (double)strBBoxXDiff)/scaleX);
	setDeltaY((((double)drawStrH)/2.0 + (double)strBBoxYDiff)/scaleX);

	this.setBoundingBox(new BRectangle2D(
		this.getX() - this.getDeltaX() - ((double)strBBoxXDiff)/scaleX,
		-this.getY() - this.getDeltaY() + ((double)strBBoxYDiff)/scaleX,
		(double)drawStrW/scaleX, (double)drawStrH/scaleX));

	// simplest bounding shape for now
	this.setBoundingShape(this.getBoundingBox());
}
*/

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
	super.draw(g2, constrainedArea); // sets g2 color, any render hints, centerx,y,
					// and current font and resets font extents

	// debug("IN FONT: " + this.getEditColor());
	if (this.getEditColor() != null)
		g2.setColor(this.getEditColor());
	
	g2.setFont(this.getFont());

	// debug("x,y,dx,dy: " + this.getX() + " " + this.getY() + " " +
		// this.getDeltaX() + " " + this.getDeltaY());

	/*
	debug("TRANS: " + g2.getTransform());
	debug("x,y: " + this.getX() + " " + this.getY());
	debug("dx,dy: " + this.getDeltaX() + " " + this.getDeltaY());
	*/

	/* SAVE FOR DRWING TEXT IMG BTS WITH 2 LINES
	int newLineIndex = this.getDrawString().indexOf('\n');
	if (newLineIndex < 0)
	{
	*/

		// debug("G2.COLOR: " + g2.getColor());
		g2.drawString(this.getDrawString(),
			-(float)this.getDeltaX(), (float)this.getDeltaY());

	/* SAVE FOR DRWING TEXT IMG BTS WITH 2 LINES
	}
	else
	{
		String subString0 = this.getDrawString().substring(0, newLineIndex);
		String subString1 = this.getDrawString().substring(newLineIndex+1, this.getDrawString().length());

		double yJmp = 6.3;
		DrawStringObject strObj = new DrawStringObject(
			0.0, yJmp,
			this.getFont(), this.getColor(),
			this.getShowBoundingShape(), subString0);
		strObj.update(g2);
		strObj.draw(g2, constrainedArea);
		strObj = new DrawStringObject(
			0.0, -yJmp,
			this.getFont(), this.getColor(),
			this.getShowBoundingShape(), subString1);
		strObj.update(g2);
		strObj.draw(g2, constrainedArea);
	}
	*/
	
	g2.translate(-this.getX(), this.getY());

	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.blue);
		this.drawBoundingShape(g2);
	}

	/*
	for super accurate printing (but prints darker)
	g2.setColor(this.getColor());
	g2.setFont(this.getFont());
	this.drawBoundingBox(g2);
	this.getShapeAtt().draw(g2, 0.0f, 0.0f);
	*/
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	if (this.getIsHidden())
		return;

	psUtil.append(
		"(" + this.getDrawString() + ") " +
		(-this.getDeltaX()) + " " + (-this.getDeltaY()) + " " +
		MathUtil.roundVal(MathUtil.colorToRedNormal(this.getColor()), 2) + " " +
		MathUtil.roundVal(MathUtil.colorToGreenNormal(this.getColor()), 2) + " " +
		MathUtil.roundVal(MathUtil.colorToBlueNormal(this.getColor()), 2)
		);
}

private ShapeGraphicAttribute shapeAtt = null;

public void
setShapeAtt(ShapeGraphicAttribute shapeAtt)
{
    this.shapeAtt = shapeAtt;
}

public ShapeGraphicAttribute
getShapeAtt()
{
    return (this.shapeAtt);
}

private static void
debug(String s)
{
	System.err.println("DrawFontObject-> " + s);
}

}
