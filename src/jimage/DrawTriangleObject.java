package jimage;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import util.*;
import util.math.*;

public class
DrawTriangleObject
extends DrawObjectLeafNode
{

public
DrawTriangleObject(double x, double y,
	double topPtX, double topPtY, double leftPtX, double leftPtY,
	double rightPtX, double rightPtY, double angle, double lineWidth,
	boolean isOpen, Color color)
throws Exception
{
	this.setX(x);
	this.setY(y);
	this.setTopPt(topPtX, topPtY);
	this.setLeftPt(leftPtX, leftPtY);
	this.setRightPt(rightPtX, rightPtY);
	this.setHeight(this.getTopPt().distance(BLine2D.getMidPt(this.getLeftPt(), this.getRightPt())));
	this.setBaseWidth(this.getLeftPt().distance(this.getRightPt()));
	this.setAngle(angle);
	this.setDeltaXY(); // sets to triangle centerpt in relation to x,y
	this.setColor(color);
	this.setIsOpen(isOpen);
	this.setLineWidth(lineWidth);
	this.reset();
}

public
DrawTriangleObject(double x, double y,
	double topPtX, double topPtY, double leftPtX, double leftPtY,
	double rightPtX, double rightPtY, double height, double baseWidth,
	double angle, double lineWidth, double scaleVal, boolean isOpen,
	Color color)
throws Exception
{
	this.setX(x);
	this.setY(y);
	this.setTopPt(topPtX, topPtY);
	this.setLeftPt(leftPtX, leftPtY);
	this.setRightPt(rightPtX, rightPtY);
	this.setHeight(height);
	this.setBaseWidth(baseWidth);
	this.setAngle(angle);
	this.setLineWidth(lineWidth);
	this.setScale(scaleVal);
	this.setDeltaXY(); // sets to triangle centerpt in relation to x,y
	this.setColor(color);
	this.setIsOpen(isOpen);
	this.reset();
}

// make a copy
public
DrawTriangleObject(DrawTriangleObject triangleObj)
throws Exception
{
	this(
		triangleObj.getX(),
		triangleObj.getY(),
		triangleObj.getTopPt().getX(),
		triangleObj.getTopPt().getY(),
		triangleObj.getLeftPt().getX(),
		triangleObj.getLeftPt().getY(),
		triangleObj.getRightPt().getX(),
		triangleObj.getRightPt().getY(),
		triangleObj.getHeight(),
		triangleObj.getBaseWidth(),
		triangleObj.getAngle(),
		triangleObj.getLineWidth(),
		triangleObj.getScale(),
		triangleObj.getIsOpen(),
		triangleObj.getColor());
}

public
DrawTriangleObject(double height, double baseWidth,
	double angle, double scale,
	double lineWidth, boolean isOpen, Color color)
throws Exception
{
	this.setX(0.0);
	this.setY(0.0);
	this.setColor(color);
	this.setIsOpen(isOpen);
	this.setLineWidth(lineWidth);
	this.set(height, baseWidth, angle, scale);
}

public void
set(double height, double baseWidth, double angle, double scale)
{
	this.setHeight(height);
	this.setBaseWidth(baseWidth);
	this.setAngle(angle);
	this.setScale(scale);
	this.reset();
}

public void
reset()
{
	this.setTopPt(0.0, this.getScale() * this.getHeight()/2.0);
	this.setLeftPt(this.getScale() * -this.getBaseWidth()/2.0,
		this.getScale() * -this.getHeight()/2.0);
	this.setRightPt(this.getScale() * this.getBaseWidth()/2.0,
		this.getScale() * -this.getHeight()/2.0);
	this.setDeltaXY(); // sets to triangle centerpt in relation to x,y
}

private Point2D.Double topPt = null;

public void
setTopPt(Point2D.Double topPt)
{
    this.topPt = topPt;
}

public void
setTopPt(double x, double y)
{
	if (this.getTopPt() == null)
		this.setTopPt(new Point2D.Double(x, y));
	else
		this.getTopPt().setLocation(x, y);
    this.topPt = topPt;
}

public Point2D.Double
getTopPt()
{
    return (this.topPt);
}

private Point2D.Double leftPt = null;

public void
setLeftPt(Point2D.Double leftPt)
{
    this.leftPt = leftPt;
}

public void
setLeftPt(double x, double y)
{
	if (this.getLeftPt() == null)
		this.setLeftPt(new Point2D.Double(x, y));
	else
		this.getLeftPt().setLocation(x, y);
}

public Point2D.Double
getLeftPt()
{
    return (this.leftPt);
}

private Point2D.Double rightPt = null;

public void
setRightPt(Point2D.Double rightPt)
{
    this.rightPt = rightPt;
}

public void
setRightPt(double x, double y)
{
	if (this.getRightPt() == null)
		this.setRightPt(new Point2D.Double(x, y));
	else
		this.getRightPt().setLocation(x, y);
}

public Point2D.Double
getRightPt()
{
    return (this.rightPt);
}


/*
Point2D.Double acMidPt = new Point2D.Double();
Point2D.Double bcMidPt = new Point2D.Double();
BLine2D testLine =  new BLine2D();
*/
private void
setDeltaXY()
{
	/*
	testLine.setLine(this.getTopPt(), this.getRightPt());
	acMidPt.setLocation(testLine.getMidPtX(), testLine.getMidPtY());
	testLine.setLine(this.getLeftPt(), this.getRightPt());
	bcMidPt.setLocation(testLine.getMidPtX(), testLine.getMidPtY());
	Point2D.Double centerPt = 
		BLine2D.getSegmentIntersectPoint(
			this.getTopPt(), bcMidPt, this.getLeftPt(), acMidPt);
	if (centerPt == null)
	{
		debug("centerPt == null");
		return;
	}
	this.setDeltaX(centerPt.getX());
	this.setDeltaY(centerPt.getY());
	*/

	this.setDeltaX(0.0);
	this.setDeltaY(0.0);
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

private double baseWidth = 1.0;

public void
setBaseWidth(double baseWidth)
{
    this.baseWidth = baseWidth;
}

public double
getBaseWidth()
{
    return (this.baseWidth);
}

private double height = 1.0;

public void
setHeight(double height)
{
    this.height = height;
}

public double
getHeight()
{
    return (this.height);
}

private double scale = 1.0;

public void
setScale(double scale)
{
    this.scale = scale;
}

public double
getScale()
{
    return (this.scale);
}

public void
setX(double x)
throws Exception
{
	super.setX(x);
}

public void
setY(double y)
throws Exception
{
	super.setY(y);
}

// NEED to replace box with a shape and use its contains
public boolean
contains(double x, double y)
throws Exception
{
	return (this.getIsPickable() && this.getBoundingShape().contains(x, y));
}

public void
update(Graphics2D g2)
throws Exception
{
	super.update(g2);

	this.reset();

	if ((getTopPt() == null) || (getLeftPt() == null) || (getRightPt() == null))
		return;
	GeneralPath trianglePath = new GeneralPath();
	trianglePath.moveTo(
		(float)this.getX() + (float)getTopPt().getX() -(float)getDeltaX(),
		-(float)this.getY() -(float)getTopPt().getY() + (float)getDeltaY());
	trianglePath.lineTo(
		(float)this.getX() + (float)getLeftPt().getX() -(float)getDeltaX(),
		-(float)this.getY() -(float)getLeftPt().getY() + (float)getDeltaY());
	trianglePath.lineTo(
		(float)this.getX() + (float)getRightPt().getX() -(float)getDeltaX(),
		-(float)this.getY() -(float)getRightPt().getY() + (float)getDeltaY());
	trianglePath.closePath();

	this.setBoundingShape(trianglePath.createTransformedShape(
		AffineTransform.getRotateInstance((Math.PI / 180.0) * this.getAngle(),
			this.getX(), -this.getY())));

	setBoundingBox(new BRectangle2D(this.getBoundingShape().getBounds2D()));

	double sveHeight = this.getHeight();
	double sveBaseWidth = this.getBaseWidth();
	this.setHeight(this.getHeight() - 2.0*this.getLineWidth());
	this.setBaseWidth(this.getBaseWidth() - 2.0*this.getLineWidth());
	this.reset();

	trianglePath = new GeneralPath();
	trianglePath.moveTo(
		(float)this.getX() + (float)getTopPt().getX() - (float)getDeltaX(),
		-(float)this.getY() -(float)getTopPt().getY() + (float)getDeltaY());
	trianglePath.lineTo(
		(float)this.getX() + (float)getLeftPt().getX() -(float)getDeltaX(),
		-(float)this.getY() -(float)getLeftPt().getY() + (float)getDeltaY());
	trianglePath.lineTo(
		(float)this.getX() + (float)getRightPt().getX() -(float)getDeltaX(),
		-(float)this.getY() -(float)getRightPt().getY() + (float)getDeltaY());
	trianglePath.closePath();

	this.setDrawShape(trianglePath.createTransformedShape(
			AffineTransform.getRotateInstance((Math.PI / 180.0) * this.getAngle(),
						this.getX(), -this.getY())));

	this.setHeight(sveHeight);
	// debug("RESETTING BASEWIDTH: " + sveBaseWidth);
	this.setBaseWidth(sveBaseWidth);
	this.reset();
}

private Shape drawShape = null;

public void
setDrawShape(Shape drawShape)
{
    this.drawShape = drawShape;
}

public Shape
getDrawShape()
{
    return (this.drawShape);
}

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
	super.draw(g2, constrainedArea); // sets g2 color and any render hints

	// undo shift set in super
	// debug("IN DRW, BEFORE: " + this.getX() + " " + this.getY());
	g2.translate(-this.getX(), this.getY());

	g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
	g2.setStroke(this.getLineStroke());
	if (this.getIsOpen())
		// g2.draw(this.getBoundingShape());
		g2.draw(this.getDrawShape());
	else
		g2.fill(this.getBoundingShape());

	if (this.getShowBoundingShape()) // show bb instead since drwing with shape
	{
		g2.setColor(Color.blue);
		this.drawBoundingBox(g2);
	}
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
}

public String
toString()
{
	return ("triangle w,h,a: " +
		this.getBaseWidth() + " " + this.getHeight() + " " + this.getAngle());
}

private void
debug(String s)
{
	System.out.println("DrawTriangleObject-> " + s);
}

}
