package jimage;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

import util.*;
import util.math.*;

public class
DrawParallelogramObject
extends DrawObjectLeafNode
{

// p 208.0 -308.0 0.0 0.0 10.0 90.0 10.0 0 0.0 0 c4ff4e
public
DrawParallelogramObject(double x, double y,
	double angle1, double side1, double angle2, double side2,
	double lineWidth, boolean isOpen, Color color)
throws Exception
{
	this.setX(x);
	this.setY(y);
	this.set(angle1, side1, angle2, side2, lineWidth, isOpen);
	this.setColor(color);
}

// make a copy
public
DrawParallelogramObject(DrawParallelogramObject parallelogramObj)
throws Exception
{
	this(
		parallelogramObj.getX(),
		parallelogramObj.getY(),
		parallelogramObj.getAngle1(),
		parallelogramObj.getSide1(),
		parallelogramObj.getAngle2(),
		parallelogramObj.getSide2(),
		parallelogramObj.getLineWidth(),
		parallelogramObj.getIsOpen(),
		parallelogramObj.getColor());
}

public void
set(double angle1, double side1, double angle2, double side2, double lineWidth, boolean isOpen)
throws Exception
{
	this.setSide1(side1);
	this.setAngle1(angle1);
	this.setSide2(side2);
	this.setAngle2(angle2);
	this.setIsOpen(isOpen);
	this.setLineWidth(lineWidth);
	this.reset();
}

public void
reset()
throws Exception
{
	this.setDeltaXY(); // sets to parallelogram centerpt in relation to x,y
}

private double angle1 = 0.00;

public void
setAngle1(double angle1)
{
    this.angle1 = angle1;
}

public double
getAngle1()
{
    return (this.angle1);
}

private double side1 = 0.00;

public void
setSide1(double side1)
{
    this.side1 = side1;
}

public double
getSide1()
{
    return (this.side1);
}

private double angle2 = 0.00;

public void
setAngle2(double angle2)
{
    this.angle2 = angle2;
}

public double
getAngle2()
{
    return (this.angle2);
}

private double side2 = 0.00;

public void
setSide2(double side2)
{
    this.side2 = side2;
}

public double
getSide2()
{
    return (this.side2);
}

public BVector2d
getVector1()
{
	return (MathUtil.polarCoordToPoint(this.getSide1(), this.getAngle1()));
}

public BVector2d
getVector2()
{
	return (MathUtil.polarCoordToPoint(this.getSide2(), this.getAngle2()));
}

public BVector2d
getVector1(double addLength)
{
	return (MathUtil.polarCoordToPoint(this.getSide1() + addLength, this.getAngle1()));
}

public BVector2d
getVector2(double addLength)
{
	return (MathUtil.polarCoordToPoint(this.getSide2() + addLength, this.getAngle2()));
}

private void
setDeltaXY()
throws Exception
{
	/*
	BVector2d vec1 = this.getVector1();
	BVector2d vec2 = this.getVector2();

	BLine2D line = new BLine2D(this.getX(), this.getY(),
		this.getX() + vec1.getX() + vec2.getX(),
		this.getY() + vec1.getY() + vec2.getY());
	Point2D centerPt = line.getMidPt();
	this.setDeltaX(centerPt.getX());
	this.setDeltaY(-centerPt.getY());
	*/
}

public double
angleBetweenSide1AndSide2()
{
	return(MathDefines.RadToDeg * this.getVector1().angle(this.getVector2()));
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
	if (!this.getIsPickable())
		return (false);
	if (!this.getIsOpen())
		return (this.getBoundingShape().contains(x, y));
	
	return (this.getBoundingShape().contains(x, y) && !getInterior().contains(x, y));
}

public Point2D
getCenterPt()
throws Exception
{
	BVector2d vec1 = this.getVector1();
	BVector2d vec2 = this.getVector2();

	BLine2D line = new BLine2D(this.getX(), this.getY(),
		this.getX() + vec1.getX() + vec2.getX(),
		this.getY() + vec1.getY() + vec2.getY());
	return (line.getMidPt());
}

public Shape
getInterior()
throws Exception
{
	double scaleVal = 0.85;
	GeneralPath interiorPath = this.getPath();
	AffineTransform scaleAT = new AffineTransform();
	Point2D centerPt = this.getCenterPt();

	scaleAT.translate(centerPt.getX(), -centerPt.getY());
	scaleAT.scale(scaleVal, scaleVal);
	scaleAT.translate(-centerPt.getX(), centerPt.getY());
	interiorPath.transform(scaleAT);

	return (interiorPath);
}

public GeneralPath
getPath()
throws Exception
{
	GeneralPath parallelogramPath = new GeneralPath();
	float x = (float)this.getX() - (float)this.getDeltaX();
	float y = (float)this.getY() - (float)this.getDeltaY();
	BVector2d vec1 = this.getVector1();
	BVector2d vec2 = this.getVector2();
	float vec1_X = (float)vec1.getX();
	float vec1_Y = (float)vec1.getY();
	float vec2_X = (float)vec2.getX();
	float vec2_Y = (float)vec2.getY();

	parallelogramPath.moveTo(
		x,
		-y
		);

	parallelogramPath.lineTo(
		x + vec2_X,
		-y - vec2_Y
		);

	parallelogramPath.lineTo(
		x + (vec1_X + vec2_X),
		-y - (vec1_Y + vec2_Y)
		);

	parallelogramPath.lineTo(
		x + vec1_X,
		-y - vec1_Y
		);

	parallelogramPath.closePath();

	return (parallelogramPath);
}

public void
update(Graphics2D g2)
throws Exception
{
	super.update(g2);

	this.reset();

	// maybe should use dX,Y to getto center and then just make sides bigger by linewidth

	this.setDrawShape(this.getPath());
	BRectangle2D rect = new BRectangle2D(this.getDrawShape().getBounds2D());
	BRectangle2D bbRect = new BRectangle2D(
		rect.getX() - this.getLineWidth(),
		rect.getY() - this.getLineWidth(),
		rect.getWidth() + 2.0*this.getLineWidth(),
		rect.getHeight() + 2.0*this.getLineWidth());
	this.setBoundingShape(this.getDrawShape());
	this.setBoundingBox(bbRect);
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

	g2.translate(-this.getX(), this.getY());

	g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
	g2.setStroke(this.getLineStroke());
	/*
	PathIterator pathIterator = this.getBoundingShape().getPathIterator(new AffineTransform());
	while (!pathIterator.isDone())
	{
		double[] coords = new double[2];
		int pathType = pathIterator.currentSegment(coords);
		if (pathType == PathIterator.SEG_MOVETO)
			debug("pathType SEG_MOVETO: " + coords[0] + " " + coords[1]);
		else if (pathType == PathIterator.SEG_LINETO)
			debug("pathType SEG_LINETO: " + coords[0] + " " + coords[1]);
		else
			debug("pathType: ??");
		pathIterator.next();
	}
	*/
	if (this.getIsOpen())
		g2.draw(this.getDrawShape());
	else
		g2.fill(this.getBoundingShape());

	/* for testing interior:
	g2.setColor(Color.green);
	g2.fill(this.getInterior());
	*/

	if (this.getShowBoundingShape()) // drw bb instead
	{
		g2.setColor(Color.blue);
		// this.drawBoundingShape(g2);
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
	return("parallelogram: " +
		this.getAngle1() + " " +
		this.getSide1() + " " +
		this.getAngle2() + " " +
		this.getSide2());
}

private void
debug(String s)
{
	System.out.println("DrawParallelogramObject-> " + s);
}

}
