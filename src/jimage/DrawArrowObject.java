package jimage;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import util.*;
import util.math.*;

public class
DrawArrowObject
extends DrawObjectLeafNode
{

public
DrawArrowObject(double x, double y, double height, double baseWidth, double tailLength,
	double tailWidth, double baseIndent, double angle,
	double lineWidth, boolean isOpen, Color color)
throws Exception
{
	this.setX(x);
	this.setY(y);
	this.setHeight(height);
	this.setBaseWidth(baseWidth);
	this.setTailLength(tailLength);
	this.setTailWidth(tailWidth);
	this.setBaseIndent(baseIndent);
	this.setAngle(angle);
	this.setLineWidth(lineWidth);
	this.setIsOpen(isOpen);
	this.setColor(color);
	this.reset();
}

public
DrawArrowObject(double height, double baseWidth, double tailLength,
	double tailWidth, double baseIndent, double angle,
	double lineWidth, boolean isOpen, Color color)
throws Exception
{
	this (0.0, 0.0, height, baseWidth, tailLength, tailWidth, baseIndent, angle,
		lineWidth, isOpen, color);
}

// make a copy
public
DrawArrowObject(DrawArrowObject arrowObj)
throws Exception
{
	this(arrowObj.getX(),
		arrowObj.getY(),
		arrowObj.getHeight(),
		arrowObj.getBaseWidth(),
		arrowObj.getTailLength(),
		arrowObj.getTailWidth(),
		arrowObj.getBaseIndent(),
		arrowObj.getAngle(),
		arrowObj.getLineWidth(),
		arrowObj.getIsOpen(),
		arrowObj.getColor());
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

private double tailLength = 0.0;

public void
setTailLength(double tailLength)
{
    this.tailLength = tailLength;
}

public double
getTailLength()
{
    return (this.tailLength);
}

private double baseIndent = 0.0;

public void
setBaseIndent(double baseIndent)
{
    this.baseIndent = baseIndent;
}

public double
getBaseIndent()
{
    return (this.baseIndent);
}

private double tailWidth = 0.00;

public void
setTailWidth(double tailWidth)
{
    this.tailWidth = tailWidth;
}

public double
getTailWidth()
{
    return (this.tailWidth);
}


// NEED to replace box with a shape and use its contains
public boolean
contains(double x, double y)
throws Exception
{
	return (this.getIsPickable() && this.getBoundingShape().contains(x, y));
}

float tipXPos = 0.0f;
float tipYPos = 0.0f;
float leftXPos = 0.0f;
float leftYPos = 0.0f;
float rightXPos = 0.0f;
float rightYPos = 0.0f;
float tailStartXPos = 0.0f;
float tailEndXPos = 0.0f;

public void
reset()
throws Exception
{
	tipXPos = (float)this.getX();
	tipYPos = -(float)this.getY();
	leftXPos = tipXPos - (float)(this.getScale() * this.getHeight());
	leftYPos = tipYPos - (float)(this.getScale() * this.getBaseWidth()/2.0);
	rightXPos = leftXPos;
	rightYPos = tipYPos + (float)(this.getScale() * this.getBaseWidth()/2.0);
	tailStartXPos = leftXPos + (float)this.getBaseIndent() - (float)this.getLineWidth();
	tailEndXPos = tailStartXPos - (float)this.getTailLength();
}

private GeneralPath
getArrowPath()
throws Exception
{
	GeneralPath arrowPath = new GeneralPath();
	float tailWidth = (float)this.getTailWidth();

	// move to tip
	arrowPath.moveTo(tipXPos, tipYPos);

	// draw to left side
	arrowPath.lineTo(leftXPos, leftYPos);

	// go to indent:
	arrowPath.lineTo(tailStartXPos, tipYPos - tailWidth);
	arrowPath.lineTo(tailEndXPos, tipYPos - tailWidth);
	arrowPath.lineTo(tailEndXPos, tipYPos + tailWidth);
	arrowPath.lineTo(tailStartXPos, tipYPos + tailWidth);

	arrowPath.lineTo(rightXPos, rightYPos);
	arrowPath.closePath();

	return (arrowPath);
}

public void
update(Graphics2D g2)
throws Exception
{
	super.update(g2);

	this.reset();

	this.setBoundingShape(this.getArrowPath().createTransformedShape(
		AffineTransform.getRotateInstance((Math.PI / 180.0) * -this.getAngle(),
			this.getX(), -this.getY())));

	setBoundingBox(new BRectangle2D(this.getBoundingShape().getBounds2D()));

	double sveHeight = this.getHeight();
	double sveBaseWidth = this.getBaseWidth();
	double sveBaseIndent = this.getBaseIndent();
	this.setHeight(this.getHeight() - 2.0*this.getLineWidth());
	this.setBaseWidth(this.getBaseWidth() - 2.0*this.getLineWidth());
	this.setBaseIndent(this.getBaseIndent() - 2.0*this.getLineWidth());
	this.reset();

	this.setDrawShape(this.getArrowPath().createTransformedShape(
		AffineTransform.getRotateInstance((Math.PI / 180.0) * -this.getAngle(),
			this.getX(), -this.getY())));

	this.setHeight(sveHeight);
	this.setBaseWidth(sveBaseWidth);
	this.setBaseIndent(sveBaseIndent);
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
	g2.translate(-this.getX(), this.getY());

	g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
	g2.setStroke(this.getLineStroke());
	if (this.getIsOpen())
		// g2.draw(this.getBoundingShape());
		g2.draw(this.getDrawShape());
	else
		/*
		g2.fill(this.getBoundingShape());
		*/
		g2.fill(this.getDrawShape());

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

private void
debug(String s)
{
	System.out.println("DrawArrowObject-> " + s);
}

}
