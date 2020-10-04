package jimage;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import util.*;
import util.math.*;

public class
DrawLineObject
extends DrawObjectLeafNode
{

public
DrawLineObject()
throws Exception
{
	this (0.0, 0.0, 0.0, 0.0, 1.0, Color.black);
}

public
DrawLineObject(double x1, double y1, double x2, double y2)
throws Exception
{
	this.setBLine2D(new BLine2D());
	this.setDrawBLine2D(new BLine2D());
	this.setDrawOverlayBLine2D(new BLine2D());
	this.reset(x1, y1, x2, y2);
	this.setAngle(this.getBLine2D().angleInXYPlane());
	this.setColor(Color.black);
	this.setLineWidth(1.0);
	this.setLinePartition(MathDefines.LINE_PARTITION_MID);
}

public
DrawLineObject(Point2D pt1, Point2D pt2)
throws Exception
{
	this(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY());
}

public
DrawLineObject(double x1, double y1, double x2, double y2,
	double lineWidth, Color color)
throws Exception
{
	this.setBLine2D(new BLine2D());
	this.setDrawBLine2D(new BLine2D());
	this.setDrawOverlayBLine2D(new BLine2D());
	this.reset(x1, y1, x2, y2);
	this.setAngle(this.getBLine2D().angleInXYPlane());
	this.setColor(color);
	this.setLineWidth(lineWidth);
	this.setLinePartition(MathDefines.LINE_PARTITION_MID);
}

public
DrawLineObject(double x1, double y1, double x2, double y2,
	double lineWidth, double overlayBorder, Color color)
throws Exception
{
	this (x1, y1, x2, y2, lineWidth, color);
	this.setOverlayBorder(overlayBorder);
	this.reset(x1, y1, x2, y2);
}

public
DrawLineObject(BLine2D line, double lineWidth, Color color)
throws Exception
{
	this(
		line.getP1().getX(),
		line.getP1().getY(),
		line.getP2().getX(),
		line.getP2().getY(),
		lineWidth,
		color);
}

public
DrawLineObject(Point2D pt1, Point2D pt2, double lineWidth, Color color)
throws Exception
{
	this(
		pt1.getX(),
		pt1.getY(),
		pt2.getX(),
		pt2.getY(),
		lineWidth,
		color);
}

public
DrawLineObject(BVector2d pt1, BVector2d pt2, double lineWidth, Color color)
throws Exception
{
	this(
		pt1.getX(),
		pt1.getY(),
		pt2.getX(),
		pt2.getY(),
		lineWidth,
		color);
}

// for drawing a point
public
DrawLineObject(Point2D pt1, double lineWidth, Color color)
throws Exception
{
	this(
		pt1.getX(),
		pt1.getY(),
		pt1.getX(),
		pt1.getY(),
		lineWidth,
		color);
	this.setLineStroke(new BasicStroke((float)lineWidth,
		BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));	
}

// for drawing a point
public
DrawLineObject(BVector2d pt1, double lineWidth, Color color)
throws Exception
{
	this(
		pt1.getX(),
		pt1.getY(),
		pt1.getX(),
		pt1.getY(),
		lineWidth,
		color);
	this.setLineStroke(new BasicStroke((float)lineWidth,
		BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));	
}

// make copy
public
DrawLineObject(DrawLineObject lineObj)
throws Exception
{
	this(lineObj.getP1().getX(), lineObj.getP1().getY(),
		lineObj.getP2().getX(), lineObj.getP2().getY(),
		lineObj.getLineWidth(), lineObj.getColor());
}

public void
reset(double x1, double y1, double x2, double y2)
throws Exception
{
	this.getDrawBLine2D().setLine(x1, -y1, x2, -y2);
	this.setX(this.getDrawBLine2D().getMidPtX());
	this.setY(this.getDrawBLine2D().getMidPtY());
	this.getBLine2D().setLine(x1, y1, x2, y2);
	this.getDrawOverlayBLine2D().setLine(x1, -y1, x2, -y2);
	this.getDrawOverlayBLine2D().setFromLength(this.getDrawBLine2D().length() -
		2.0*(getOverlayBorder() + 0.0));
}

public void
resetCSV(double x1, double y1, double x2, double y2)
throws Exception
{
	this.getDrawBLine2D().setLine(x1, y1, x2, y2);
	//this.setX(this.getDrawBLine2D().getMidPtX());
	//this.setY(this.getDrawBLine2D().getMidPtY());
	this.getBLine2D().setLine(x1, y1, x2, y2);
	this.getDrawOverlayBLine2D().setLine(x1, y1, x2, y2);
	//double distance = (y2-y1)/(x2-x1);
	//this.getDrawOverlayBLine2D().setFromLength(distance);
}

public void
setFromAngle(double angle)
throws Exception
{
	this.getBLine2D().setFromAngle(angle);
	this.setAngle(this.getBLine2D().angleInXYPlane());
	if (this.getAngle() == 360.0)
		this.setAngle(0.0);
	this.reset(
		this.getBLine2D().getP1().getX(),
		this.getBLine2D().getP1().getY(),
		this.getBLine2D().getP2().getX(),
		this.getBLine2D().getP2().getY());
}

// this sets length both ways over midPt
public void
setFromLength(double length)
throws Exception
{
	this.getBLine2D().setFromLength(length);
	this.reset(
		this.getBLine2D().getP1().getX(),
		this.getBLine2D().getP1().getY(),
		this.getBLine2D().getP2().getX(),
		this.getBLine2D().getP2().getY());
}

public double
getLength()
{
	return (this.getBLine2D().length());
}

// this sets length from tailPt to headPt leaving tailPt same
public void
setHeadFromLength(double length)
throws Exception
{
	Point2D headPt = this.getBLine2D().ptAtDistance(length);
	this.getBLine2D().setP2(headPt);
	this.reset(
		this.getBLine2D().getP1().getX(),
		this.getBLine2D().getP1().getY(),
		headPt.getX(), headPt.getY());
}

public void
flipEndPts()
{
	this.getBLine2D().flipEndPts();
	this.getDrawBLine2D().flipEndPts();
	this.getDrawOverlayBLine2D().flipEndPts();
}

// MathDefines.LINE_PARTITION_NOT_SET = -2;
// MathDefines.LINE_PARTITION_TAIL = -1;
// MathDefines.LINE_PARTITION_MID = 0;
// MathDefines.LINE_PARTITION_HEAD = 1;
// MathDefines.LINE_PARTITION_ERROR = 2;
private int linePartition = MathDefines.LINE_PARTITION_NOT_SET;

public void
setLinePartition(double pickX, double pickY)
{
	double t = this.getDrawBLine2D().offsetPtTValue(pickX, pickY);
	if (t <= 0.33)
		linePartition = MathDefines.LINE_PARTITION_TAIL;
	else if ((t >= 0.33) && (t <= 0.66))
		linePartition = MathDefines.LINE_PARTITION_MID;
	else if (t > 0.66)
		linePartition = MathDefines.LINE_PARTITION_HEAD;
}

public void
setLinePartition(int linePartition)
{
	this.linePartition = linePartition;
}

public int
getLinePartition()
{
	return (linePartition);
}

public void
shiftX(double x)
throws Exception
{
	if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
	{
		this.reset(
			this.getBLine2D().getP1().getX() - x,
			this.getBLine2D().getP1().getY(),
			this.getBLine2D().getP2().getX() - x,
			this.getBLine2D().getP2().getY());
	}
	else if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
	{
		this.reset(
			this.getBLine2D().getP1().getX() - x,
			this.getBLine2D().getP1().getY(),
			this.getBLine2D().getP2().getX(),
			this.getBLine2D().getP2().getY());
	}
	else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
	{
		this.reset(
			this.getBLine2D().getP1().getX(),
			this.getBLine2D().getP1().getY(),
			this.getBLine2D().getP2().getX() - x,
			this.getBLine2D().getP2().getY());
	}
}

public void
shiftY(double y)
throws Exception
{
	if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
	{
		this.reset(
			this.getBLine2D().getP1().getX(),
			this.getBLine2D().getP1().getY() - y,
			this.getBLine2D().getP2().getX(),
			this.getBLine2D().getP2().getY() - y);
	}
	else if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
	{
		this.reset(
			this.getBLine2D().getP1().getX(),
			this.getBLine2D().getP1().getY() - y,
			this.getBLine2D().getP2().getX(),
			this.getBLine2D().getP2().getY());
	}
	else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
	{
		this.reset(
			this.getBLine2D().getP1().getX(),
			this.getBLine2D().getP1().getY(),
			this.getBLine2D().getP2().getX(),
			this.getBLine2D().getP2().getY() - y);
	}
}

public void
shiftXY(double x, double y)
throws Exception
{
	if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
	{
		this.reset(
			this.getBLine2D().getP1().getX() - x,
			this.getBLine2D().getP1().getY() - y,
			this.getBLine2D().getP2().getX() - x,
			this.getBLine2D().getP2().getY() - y);
	}
	else if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
	{
		this.reset(
			this.getBLine2D().getP1().getX() - x,
			this.getBLine2D().getP1().getY() - y,
			this.getBLine2D().getP2().getX(),
			this.getBLine2D().getP2().getY());
	}
	else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
	{
		this.reset(
			this.getBLine2D().getP1().getX(),
			this.getBLine2D().getP1().getY(),
			this.getBLine2D().getP2().getX() - x,
			this.getBLine2D().getP2().getY() - y);
	}
}

public void
shiftPartitionedLineXY(double x, double y, double pickX, double pickY)
throws Exception
{
	// debug("pickX,Y: " + pickX + " " + pickY);
	this.shiftXY(x, y);
}

private BLine2D bLine2D = null;

public void
setBLine2D(BLine2D bLine2D)
{
    this.bLine2D = bLine2D;
}

public BLine2D
getBLine2D()
{
    return (this.bLine2D);
}

private BLine2D drawBLine2D = null;

public void
setDrawBLine2D(BLine2D drawBLine2D)
{
    this.drawBLine2D = drawBLine2D;
}

public BLine2D
getDrawBLine2D()
{
    return (this.drawBLine2D);
}

private BLine2D drawOverlayBLine2D = null;

public void
setDrawOverlayBLine2D(BLine2D drawOverlayBLine2D)
{
    this.drawOverlayBLine2D = drawOverlayBLine2D;
}

public BLine2D
getDrawOverlayBLine2D()
{
    return (this.drawOverlayBLine2D);
}

public Point2D
getP1()
{
    return (this.getBLine2D().getP1());
}

public Point2D
getP2()
{
    return (this.getBLine2D().getP2());
}

private BasicStroke lineStroke = null;

public void
setLineStroke(BasicStroke lineStroke)
{
    this.lineStroke = lineStroke;
}

public BasicStroke
getLineStroke()
{
    return (this.lineStroke);
}

public void
setLineWidth(double lineWidth)
{
	this.setLineStroke(new BasicStroke((float)lineWidth,
		BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
}

public double
getLineWidth()
{
	return ((double)getLineStroke().getLineWidth());
}

private double angle = 0.0;

public void
setAngle(double angle)
{
    this.angle = angle;
}

public double
getAngle()
{
    return (this.angle);
}

private double overlayBorder = 0.0;

public void
setOverlayBorder(double overlayBorder)
{
    this.overlayBorder = overlayBorder;
	this.setOverlayLineStroke(new BasicStroke(
		(float)this.getLineWidth() + (float)this.getOverlayBorder(),
		BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));	
}

public double
getOverlayBorder()
{
    return (this.overlayBorder);
}

private BasicStroke overlayLineStroke = null;

public void
setOverlayLineStroke(BasicStroke overlayLineStroke)
{
    this.overlayLineStroke = overlayLineStroke;
}

public BasicStroke
getOverlayLineStroke()
{
    return (this.overlayLineStroke);
}

public double
length()
{
	return (this.getBLine2D().length());
}

public void
runSetLocationHashtable(int level)
throws Exception
{
	if (this.getSaveLocationHashtable() == null)
		this.setSaveLocationHashtable(new Hashtable());
	this.getSaveLocationHashtable().put(new Integer(level),
		new BLine2D(this.getBLine2D().getP1(), this.getBLine2D().getP2()));
}

public boolean
resetLocationFromHashtable(int level)
throws Exception
{
	if (this.getSaveLocationHashtable() == null)
		return (false);
	BLine2D line = (BLine2D)this.getSaveLocationHashtable().get(new Integer(level));
	if (line == null)
		return (false);
	this.reset(
		line.getP1().getX(),
		line.getP1().getY(),
		line.getP2().getX(),
		line.getP2().getY());

	return (true);
}

public boolean
contains(double x, double y)
throws Exception
{
	return (this.getIsPickable() && this.getBoundingShape().contains(x, y));
}

/* Currently too slow and expensive
public void
newUpdate(Graphics2D g2)
throws Exception
{
	// get the current scaleval from g2
	AffineTransform at = g2.getTransform();
	// scaleX and scaleY should always be same
	double scaleVal = at.getScaleX();

	Rectangle2D rect = this.getBLine2D().getBounds();
	this.setBoundingBox(new BRectangle2D(
		rect.getX(), rect.getY(),
		rect.getWidth(), rect.getHeight()));
	//
	// this.setBoundingShape(this.getLineStroke().createStrokedShape(
		// this.getBLine2D()));
	//

	BufferedImage tmpImg = new BufferedImage(
		(int)scaleVal * ((int)Math.round(rect.getWidth()) + 10),
		(int)scaleVal * ((int)Math.round(rect.getHeight()) + 10),
		BufferedImage.TYPE_INT_RGB);
	
	Graphics2D g2i = tmpImg.createGraphics();
	g2i.scale(scaleVal, scaleVal);
	// NEEDs to be passed in g2
	g2i.setRenderingHints(GraphicsUtil.aliasedRenderHints);
	g2i.setBackground(this.getDrawImgBGColor());
	g2i.setColor(this.getDrawImgBGColor());
	g2i.fillRect(0, 0, tmpImg.getWidth()/(int)Math.round(scaleVal),
		tmpImg.getHeight()/(int)Math.round(scaleVal));

	double midImgX = ((double)tmpImg.getWidth())/2.0;
	double midImgY = ((double)tmpImg.getHeight())/2.0;

	// draw a centerPt to image
	// g2i.setColor(Color.green);
	// g2i.drawLine(
		// (int)Math.round(midImgX/scaleVal),
		// (int)Math.round(midImgY/scaleVal),
		// (int)Math.round(midImgX/scaleVal),
		// (int)Math.round(midImgY/scaleVal));
	//

	BLine2D centeredLine =
		(new BLine2D(this.getBLine2D())).centerAtMidPt();
	BLine2D imgSpaceLine =
		(new BLine2D(centeredLine)).centerAtPt(new Point2D.Double(
			-midImgX/scaleVal, -midImgY/scaleVal));
	
	g2i.setColor(this.getColor());
	g2i.setStroke(this.getLineStroke());
	g2i.draw(imgSpaceLine);

	int threshold = 7;
	int bgRGB = this.getDrawImgBGColor().getRGB() & 0x00ffffff;
	int bgRedPixel = (bgRGB & 0x00ff0000) >> 16;
	int bgGreenPixel = (bgRGB & 0x0000ff00) >> 8;
	int bgBluePixel = bgRGB & 0x000000ff;
	int minX = Integer.MAX_VALUE;
	int minY = Integer.MAX_VALUE;
	int maxX = -Integer.MAX_VALUE;
	int maxY = -Integer.MAX_VALUE;
	Area area = new Area();
	
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
			
			area.add(new Area(new BRectangle2D(
				(double)col/scaleVal, (double)row/scaleVal, 0.5, 0.5)));
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

	// debug("min/max XY: " + minX + " " + minY + " " + maxX + " " + maxY);

	this.setBoundingShape(area);

	//
	// g2i.setColor(Color.magenta);
	// g2i.draw(this.getBoundingShape());
	// g2i.setColor(this.getColor());
	// g2i.setStroke(this.getLineStroke());
	// g2i.draw(imgSpaceLine);
	//

	this.setDrawObjectImg(tmpImg);
	//
	// tmpImg.getSubimage(minX + 1, minY + 1, midImgW - 1, midImgH - 1));
	//
}
*/

public void
update(Graphics2D g2)
throws Exception
{
	super.update(g2);

	// debug("UDATING LINE: " +  this.getBLine2D() + " angle: " + this.getBLine2D().angleInXYPlane() + " length: " + this.getBLine2D().length());

	// get the current scaleval from g2
	AffineTransform at = g2.getTransform();
	// scaleX and scaleY should always be same
	double scaleVal = at.getScaleX();

	// trying making bounding shape 1.0 more than given linewidth
	this.setBoundingShape(
		(new BasicStroke((float)(this.getLineStroke().getLineWidth() + 1.0),
		BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER)).createStrokedShape(
			this.getDrawBLine2D()));

	// new 06/20/03:
	this.setBoundingBox(new BRectangle2D(this.getBoundingShape().getBounds2D()));
}

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
	super.draw(g2, constrainedArea); // sets g2 color and any render hints, translate x,y
	
	// undo the shift to midpt from call to super before draw
	g2.translate(-this.getX(), this.getY());

	if (this.getOverlayBorder() > 0.0)
	{
		g2.setStroke(this.getOverlayLineStroke());
		g2.setColor(g2.getBackground());
		// NEED to draw a slightly shorter line so overlay
		// doesn't interfere with line connections.
		g2.draw(this.getDrawOverlayBLine2D());
	}

	g2.setColor(this.getColor());
	g2.setStroke(this.getLineStroke());
	g2.draw(this.getDrawBLine2D());

	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.blue);
		this.drawBoundingShape(g2);
	}
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	if (this.getIsHidden())
		return;
	
	if (this.getOverlayBorder() > 0.0)
	{
		/*
		g2.setStroke(this.getOverlayLineStroke());
		g2.setColor(g2.getBackground());
		// NEED to draw a slightly shorter line so overlay
		// doesn't interfere with line connections.
		g2.draw(this.getDrawOverlayBLine2D());
		psUtil.printPSString(this.getDrawString(),
			-this.getDeltaX(), -this.getDeltaY());
		*/
	}

	g2.setColor(this.getColor());
	g2.setStroke(this.getLineStroke());
	
	/*
	psUtil.printPSLine(this.getP1().getX(), this.getP1().getY(),
		this.getP2().getX(), this.getP2().getY(), g2);
	*/
	psUtil.append(
		MathUtil.roundVal(this.getP1().getX(), 2) + " " +
		MathUtil.roundVal(this.getP1().getY(), 2) + " " +
		MathUtil.roundVal(this.getP2().getX(), 2) + " " +
		MathUtil.roundVal(this.getP2().getY(), 2) + " " +
		MathUtil.roundVal(this.getLineWidth(), 2) + " " +
		MathUtil.roundVal(MathUtil.colorToRedNormal(this.getColor()), 2) + " " +
		MathUtil.roundVal(MathUtil.colorToGreenNormal(this.getColor()), 2) + " " +
		MathUtil.roundVal(MathUtil.colorToBlueNormal(this.getColor()), 2));
}

public String
toString()
{
	return("line: " +
		StringUtil.roundStrVal(this.getBLine2D().getP1().getX(), 2) + " " +
		StringUtil.roundStrVal(this.getBLine2D().getP1().getY(), 2) + " " +
		StringUtil.roundStrVal(this.getBLine2D().getP2().getX(), 2) + " " +
		StringUtil.roundStrVal(this.getBLine2D().getP2().getY(), 2));
}

private void
debug(String s)
{
	System.out.println("DrawLineObject-> " + s);
}

}
