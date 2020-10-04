package util.math;

import java.awt.geom.*;

import javax.vecmath.*;

public class
BVector2d
extends Vector2d
{

public
BVector2d()
{
	super();
}

public
BVector2d(double x, double y)
{
	super(x, y);
}

public
BVector2d(Point2D pt)
{
	super(pt.getX(), pt.getY());
}

public
BVector2d(BVector2d vec)
{
	super((Vector2d)vec);
}

double[] vals = new double[2];

public void
copy(BVector2d fromPt)
{
	fromPt.get(vals);
	this.set(vals);
}

public double
distance(BVector2d vec)
{
	Point2d pt0 = new Point2d(getX(), getY());
	Point2d pt1 = new Point2d(vec.getX(), vec.getY());
	return (pt0.distance(pt1));
}

/*
public void
translate(BVector transPt)
throws Exception
{
	this.translate(transPt.xCoor(), transPt.yCoor(), transPt.zCoor());
}
*/

public void
translate(double xPos, double yPos)
throws Exception
{
	this.setXY(this.getX() + xPos, this.getY() + yPos);
}

public void
translate(BVector2d transPt)
throws Exception
{
	this.translate(transPt.getX(), transPt.getY());
}

// the angle in degrees between the + x-axis and BVector2d.
public double
angleInXYPlane()
{
	double angle = MathDefines.RadToDeg * new Vector2d(1.0, 0.0).angle(this);
	if (this.getY() < 0.0)
		return(360.0 - angle);
	return(angle);
}

// the angle in radians between the + x-axis and BVector2d.
public double
radiansInXYPlane()
{
	double radians = new Vector2d(1.0, 0.0).angle(this);
	/*
	if (this.getY() < 0.0)
		return(360.0 - radians);
	*/
	return(radians);
}

public void
setXY(double x, double y)
{
	this.get(vals);
	vals[0] = x;
	vals[1] = y;
	this.set(vals);
}

public void
setXY(Point2D pt)
{
	this.get(vals);
	vals[0] = pt.getX();
	vals[1] = pt.getY();
	this.set(vals);
}

public void
setX(double x)
{
	this.get(vals);
	vals[0] = x;
	this.set(vals);
}

public double
getX()
{
	this.get(vals);
	return (vals[0]);
}

public void
setY(double y)
{
	this.get(vals);
	vals[1] = y;
	this.set(vals);
}

public double
getY()
{
	this.get(vals);
	return (vals[1]);
}

public Point2D
getPoint2D()
{
	return (new Point2D.Double(this.getX(), this.getY()));
}

public static BVector2d
toBVector2d(Point2D pt)
{
	return (new BVector2d(pt.getX(), pt.getY()));
}

private static void
debug(String s)
{
	System.out.println("BVector2d-> " + s);
}


}
