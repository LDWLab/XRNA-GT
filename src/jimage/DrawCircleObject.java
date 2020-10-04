package jimage;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import util.*;
import util.math.*;

public class
DrawCircleObject
extends DrawObjectLeafNode
{

public
DrawCircleObject(double x, double y, double radius, double startAngle, double angleExtent)
throws Exception
{
	this.setX(x);
	this.setY(y);
	this.setRadius(radius);
	this.setDeltaX(radius);
	this.setDeltaY(-radius);
	this.setColor(Color.black);
	this.setStartAngle(startAngle);
	this.setAngleExtent(angleExtent);
	this.setIsOpen(true);
	/*
	this.setLineStroke(new BasicStroke((float)this.getLineWidth(),
		BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
	*/
	this.setCircleExtents();
}

public
DrawCircleObject(double x, double y, double radius,
	double startAngle, double angleExtent, Color color)
throws Exception
{
	this(x, y, radius, startAngle, angleExtent);
	this.setColor(color);
}

public
DrawCircleObject(double radius)
throws Exception
{
	this(0.0, 0.0, radius, 0.0, 360.0);
}

public
DrawCircleObject(double x, double y, double radius, Color color)
throws Exception
{
	this(x, y, radius, 0.0, 360.0);
	this.setColor(color);
}

public
DrawCircleObject(Point2D centerPt, double radius, Color color)
throws Exception
{
	this(centerPt.getX(), centerPt.getY(), radius, color);
}

public
DrawCircleObject(Point2D centerPt, double radius, Color color,
	boolean isOpen)
throws Exception
{
	this(centerPt, radius, color);
	this.setIsOpen(isOpen);
}

public
DrawCircleObject(double radius, double lineWidth)
throws Exception
{
	this(0.0, 0.0, radius, 0.0, 360.0);
	this.setLineWidth(lineWidth);
}

public
DrawCircleObject(double x, double y, double radius, double startAngle, double angleExtent,
	Color color, boolean isOpen)
throws Exception
{
	this(x, y, radius, startAngle, angleExtent, color);
	this.setIsOpen(isOpen);
}

public
DrawCircleObject(double x, double y, double radius, double startAngle,
	double angleExtent, Color color, boolean isOpen, double lineWidth)
throws Exception
{
	this(x, y, radius, startAngle, angleExtent, color, isOpen);
	this.setLineWidth(lineWidth);
}

// make a copy
public
DrawCircleObject(DrawCircleObject circleObj)
throws Exception
{
	this(
		circleObj.getX(),
		circleObj.getY(),
		circleObj.getRadius(),
		circleObj.getStartAngle(),
		circleObj.getAngleExtent());
	this.setLineWidth(circleObj.getLineWidth());
	this.setIsOpen(circleObj.getIsOpen());
	this.setColor(circleObj.getColor());
}

private double lineWidth = 1.0;

public void
setLineWidth(double lineWidth)
{
    this.lineWidth = lineWidth;
	if (this.getStartAngle() + this.getAngleExtent() == 360.0)
		this.setLineStroke(new BasicStroke((float)lineWidth,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));	
	else
		this.setLineStroke(new BasicStroke((float)lineWidth,
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));	
}

public double
getLineWidth()
{
    return (this.lineWidth);
}

private Arc2D.Double Arc2D = null;

public void
setArc2D(Arc2D.Double Arc2D)
{
    this.Arc2D = Arc2D;
}

public Arc2D.Double
getArc2D()
{
    return (this.Arc2D);
}

private double radius = 0.0;

public void
setRadius(double radius)
{
    this.radius = radius;
	this.setDeltaX(radius);
	this.setDeltaY(-radius);
}

public double
getRadius()
{
    return (this.radius);
}

private double startAngle = 0.00;

public void
setStartAngle(double startAngle)
{
    this.startAngle = startAngle;
}

public double
getStartAngle()
{
    return (this.startAngle);
}

private double angleExtent = 0.00;

public void
setAngleExtent(double angleExtent)
{
    this.angleExtent = angleExtent;
}

public double
getAngleExtent()
{
    return (this.angleExtent);
}

public void
setLocation(Point2D centerPt)
throws Exception
{
	super.setX(centerPt.getX());
	super.setY(centerPt.getY());
	this.setCircleExtents();
}

public void
setLocation(double x, double y)
throws Exception
{
	super.setX(x);
	super.setY(y);
	this.setCircleExtents();
}

public void
setX(double x)
throws Exception
{
	super.setX(x);
	this.setCircleExtents();
}

public void
setY(double y)
throws Exception
{
	super.setY(y);
	this.setCircleExtents();
}

public boolean
contains(double x, double y)
throws Exception
{
	if (!this.getIsPickable())
		return (false);

	// return (this.getBoundingShape().contains(x, y));
	
	Shape testShape = (Shape)
		new Arc2D.Double(
			this.getX() - this.getDeltaX(),
			-this.getY() + this.getDeltaY(),
			2.0*this.getRadius(),
			2.0*this.getRadius(),
			this.getStartAngle(),
			this.getAngleExtent(),
			Arc2D.PIE);
	return (testShape.contains(x, y));
}

private Point2D centerPt = null;

public void
setCenterPt(Point2D centerPt)
{
    this.centerPt = centerPt;
}

public Point2D
getCenterPt()
{
    return (this.centerPt);
}

public void
setCircleExtents()
throws Exception
{
	if (this.getCenterPt() == null)
		this.setCenterPt(new Point2D.Double(this.getX(), this.getY()));
	else
		this.getCenterPt().setLocation(this.getX(), this.getY());
	if (this.getArc2D() == null)
		this.setArc2D(new Arc2D.Double(
			this.getX() - this.getDeltaX(),
			-this.getY() + this.getDeltaY(),
			2.0*radius, 2.0*radius, this.getStartAngle(), this.getAngleExtent(), Arc2D.OPEN));
	else
		this.getArc2D().setArc(
			this.getX() - this.getDeltaX(),
			-this.getY() + this.getDeltaY(),
			2.0*this.getRadius(), 2.0*this.getRadius(),
			this.getStartAngle(), this.getAngleExtent(), Arc2D.OPEN);

	Rectangle2D rect = this.getArc2D().getBounds2D();

	BRectangle2D bbRect = new BRectangle2D(
		rect.getX() - this.getLineWidth(),
		rect.getY() - this.getLineWidth(),
		rect.getWidth() + 2.0*this.getLineWidth(),
		rect.getHeight() + 2.0*this.getLineWidth());

	this.setBoundingBox(bbRect);

	this.setBoundingShape((Shape)this.getArc2D());
	/*
	this.setBoundingShape((Shape)
		new Arc2D.Double(
			this.getArc2D().getX(),
			this.getArc2D().getY(),
			this.getArc2D().getWidth(),
			this.getArc2D().getHeight(),
			this.getArc2D().getAngleStart(),
			this.getArc2D().getAngleExtent(),
			Arc2D.PIE
			));
	*/
}

public void
update(Graphics2D g2)
throws Exception
{
	super.update(g2);

	this.setCircleExtents();
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

	// need to undo super.draw()'s shift as shape already has x,y
	g2.translate(-this.getX(), this.getY());

	/*
	PathIterator pathIterator = this.getArc2D().getPathIterator(new AffineTransform());
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

	g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
	g2.setStroke(this.getLineStroke());
	if (this.getIsOpen())
		g2.draw(this.getArc2D());
	else
		g2.fill(this.getArc2D());
	
	// MIGHT HAVE TO DEAL WITH THIS FOR SHOWING BOUND SHAPE.
	// TRY x , -y
	// g2.translate(-this.getX(), this.getY());

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
}

/*************** STATIC METHODS *******************************/

public static DrawCircleObject
bestFit(Vector ptSet)
throws Exception
{
	// this approach simply uses a best lower bound on radius
	// from Jung's thereom on circles

	double setDiameter = MathUtil.ptSetDiameter(ptSet);

	double radius = (Math.sqrt(3.0)/3.0)*setDiameter; // Jung's thereom

	return (new DrawCircleObject(radius));

// another approach:
/*
Brute Force Approach (leads to a non-linear system): [Amara Graps]
-------------

Idea: Minimize by least squares the root-mean-squared error of the radius
in the equation for a circle. In this method, one minimizes the sum of
the squares of the *length* differences, which gives you a non-linear
problem with all the associated pitfalls.

(x-X0)^2 + (y-Y0)^2 = R^2   Equation for a circle
R = SQRT[ (x-X0)^2 + (y-Y0)^2)  ]     Radius of the circle

where:
(X0,Y0) = center of circle
(x,y) = point coordinates
R = radius

1) Get first estimate for (X0,Y0,R).
(Details: Find some points to make first estimates- either solve the
circle exactly (3 equations, 3 unknowns) to get a first estimate of the
center and radius, or just do a kind of centroid calculation on all
points- to get a rough estimate of the center and radius.)

2) Calculate r (r1, r2,.. rN) for each of your N points from the equation
for a radius of a circle.

3) Calculate the root-mean-squared error
For example, for 5 given points on the circle:

RMS error = SQRT[ [ (r1-R)^2 + (r2-R)^2 + (r3-R)^2 + (r4-R)^2
                  + (r5-R)^2] / 3 ]
(dividing by "3" because we have 3 free parameters.)

4) Change (X0,Y0,R) slightly in your minimization algorithm
to try a new (X0,Y0,R).

(Details: Because minimization algorithms can get very computationally
intensive, if one's problem is a simple one, I would look for a "canned"
minimization routine. Some commercial computer programs for plotting and
spreadsheets do this sort of thing. For example, the Excel spreadsheet
has a built-in "solver" that will perform minimzation. Other
possibilties for software: Matlab with the optimization toolbox,
MACSYMA, ODRPACK from Netlib, the recent L-BFGS-B package (from
ftp://eecs.nwu.edu/pub/lbfgs) which allows you to specify bounds on the
variables.)

5) Calculate r (r1, r2 etc.) again from new (X0,Y0,R) above.

6) Calculate RMS error again.

7) Compare current RMS error with previous RMS error. If it
doesn't vary by more some small amount, say 10^{-3} then
you're done, otherwise continue steps 4 -- 7.


*/

}

/*************** END STATIC METHODS *******************************/

public String
toString()
{
	return("circle: " + getRadius() + " " + getColor());
}

private void
debug(String s)
{
	System.out.println("DrawCircleObject-> " + s);
}

}
