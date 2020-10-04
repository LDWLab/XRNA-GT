package util.math;

import java.awt.*;
import java.awt.geom.*;

import javax.vecmath.*;

public class
BLine2D
extends Line2D.Double
{

public
BLine2D()
{
	super ();
}

public
BLine2D(double x1, double y1, double x2, double y2)
{
	super (x1, y1, x2, y2);
}

// vector with tail at origin
public
BLine2D(double x2, double y2)
{
	super (0.0, 0.0, x2, y2);
}

public
BLine2D(Point2D p1, Point2D p2)
{
	super (p1, p2);
}

public
BLine2D(BVector2d p1, BVector2d p2)
{
	super (p1.getX(), p1.getY(), p2.getX(), p2.getY());
}

public
BLine2D(BVector2d pt)
{
	this (pt.getX(), pt.getY());
}

public
BLine2D(Point2D pt)
{
	this (pt.getX(), pt.getY());
}

public
BLine2D(Line2D line)
{
	super (line.getP1(), line.getP2());
}

public
BLine2D(BLine2D line)
{
	super (line.getP1(), line.getP2());
}

public void
setFromAngle(double angle)
{
	BVector2d newPt = MathUtil.polarCoordToPoint(this.length(), angle);

	this.setLine(
		new Point2D.Double(
			-newPt.getX()/2.0 + this.getMidPtX(),
			-newPt.getY()/2.0 + this.getMidPtY()),
		new Point2D.Double(
			newPt.getX()/2.0 + this.getMidPtX(),
			newPt.getY()/2.0 + this.getMidPtY()));
}

// leaves tailpt alone
public void
setRayFromAngle(double angle)
{
	BVector2d newPt = MathUtil.polarCoordToPoint(this.length(), angle);

	this.setLine(
		this.getP1().getX(), this.getP1().getY(),
		this.getP1().getX() + newPt.getX(), this.getP1().getY() + newPt.getY());
}

// leaves tailpt alone
public void
setRayFromAngleAndLength(double angle, double length)
{
	BVector2d newPt = MathUtil.polarCoordToPoint(length, angle);

	this.setLine(
		this.getP1().getX(), this.getP1().getY(),
		this.getP1().getX() + newPt.getX(), this.getP1().getY() + newPt.getY());
}

// leaves tailpt alone
public void
setRayFromLength(double length)
{
	this.setLine(this.getP1(), this.ptAtDistance(length));
}

public void
setFromLength(double length)
{
	length /= this.length();

	Point2D newPt = new Point2D.Double(
		(this.getP2().getX() - this.getP1().getX()) * length,
		(this.getP2().getY() - this.getP1().getY()) * length);

	this.setLine(
		new Point2D.Double(
			-newPt.getX()/2.0 + this.getMidPtX(),
			-newPt.getY()/2.0 + this.getMidPtY()),
		new Point2D.Double(
			newPt.getX()/2.0 + this.getMidPtX(),
			newPt.getY()/2.0 + this.getMidPtY()));
}

public void
setFromAngleAndLength(double angle, double length)
{
	this.setFromAngle(angle);
	this.setFromLength(length);
}

public void
translate(double x, double y)
{
	this.setLine(this.getP1().getX() - x, this.getP1().getY() - y,
		this.getP2().getX() - x, this.getP2().getY() - y);
}

public void
setP1(Point2D pt)
{
	this.setLine(pt, this.getP2());
}

public void
setP2(Point2D pt)
{
	this.setLine(this.getP1(), pt);
}

public void
flipEndPts()
{
	this.setLine(this.getP2(), this.getP1());
}

public Point2D
getMidPt()
{
	return (new Point2D.Double(this.getMidPtX(), this.getMidPtY()));
}

public BVector2d
getVecMidPt()
{
	return (new BVector2d(this.getMidPtX(), this.getMidPtY()));
}

public static Point2D
getMidPt(Point2D pt0, Point2D pt1)
{
	return (new Point2D.Double((pt0.getX() + pt1.getX())/2.0,
		(pt0.getY() + pt1.getY())/2.0));
}

public double
getMidPtX()
{
	return ((getX1() + getX2())/2.0);
}

public double
getMidPtY()
{
	return ((getY1() + getY2())/2.0);
}

// returns a line such that it's center pt is now at origin
public Line2D.Double
centerAtMidPt()
{
	Point2D midPt = this.getMidPt();
	return new Line2D.Double(
		getX1() - midPt.getX(),
		getY1() - midPt.getY(),
		getX2() - midPt.getX(),
		getY2() - midPt.getY());
}

// returns a line such that it's tail pt is now at origin
public void
shiftToOrigin()
{
	this.setLine(0.0, 0.0,
		this.getP2().getX() - this.getP1().getX(),
		this.getP2().getY() - this.getP1().getY());
}

public Point2D.Double
getPointAtT(double t)
{
	return (new Point2D.Double(
		getX1() + t * (getX2() - getX1()),
		getY1() + t * (getY2() - getY1())));
}

public void
getPointAtT(Point2D pt, double t)
{
	pt.setLocation(
		getX1() + t * (getX2() - getX1()),
		getY1() + t * (getY2() - getY1()));
}

public static Point2D.Double
getSegmentIntersectPoint(double head0x, double head0y,
	double tail0x, double tail0y, double head1x, double head1y,
	double tail1x, double tail1y)
{
	double tmp = 0.0;
	double tmpu = 0.0;
	double tmpv = 0.0;

	tmp = ((head1y - tail1y)*(head0x - tail0x) - (head1x - tail1x)*(head0y - tail0y));
	if(tmp == 0.0)
		return(null);
	tmpv = ((tail1x - tail0x)*(head0y - tail0y) - (tail1y - tail0y)*(head0x - tail0x)) / tmp;
	tmp = (head0x - tail0x)*(head1y - tail1y) - (head1x - tail1x)*(head0y - tail0y);
	if(tmp == 0.0)
		return(null);
	tmpu = ((tail1x - tail0x)*(head1y - tail1y) + (head1x - tail1x)*(tail0y - tail1y))/tmp;
	return(new Point2D.Double(
		tail1x + tmpv*(head1x - tail1x),
		tail1y + tmpv*(head1y - tail1y)));
}

public static Point2D.Double
getSegmentIntersectPoint(Point2D.Double pt0StartPt, Point2D.Double pt0EndPt,
	Point2D.Double pt1StartPt, Point2D.Double pt1EndPt)
{
	return(getSegmentIntersectPoint(pt0StartPt.getX(), pt0StartPt.getY(),
		pt0EndPt.getX(), pt0EndPt.getY(), pt1StartPt.getX(),
		pt1StartPt.getY(), pt1EndPt.getX(), pt1EndPt.getY()));
}

public Point2D.Double
getSegmentIntersectPoint(BLine2D line)
{
	return(getSegmentIntersectPoint(
		this.getP1().getX(), this.getP1().getY(),
		this.getP2().getX(), this.getP2().getY(),
		line.getP1().getX(), line.getP1().getY(),
		line.getP2().getX(), line.getP2().getY()));
}

public double
length()
{
	double a = getX2() - getX1();
	double b = getY2() - getY1();

	return (Math.sqrt(a*a + b*b));
}

// gives the point of intersection no matter where the lines are
public Point2D.Double
getLineIntersectPoint(BLine2D line)
{
	return (getSegmentIntersectPoint(line));
}

/*
** Either intersect eventually or are parallel.
** uArray holds 2 vals, first is u for first ray, second is u for second ray.
*/

public static boolean
raysIntersectInPlane(Point2D tail0Pt, Point2D head0Pt, Point2D tail1Pt, Point2D head1Pt,
	Point2D intersect, double[] uArray)
{
	double tmp = ((head1Pt.getY() - tail1Pt.getY())*(head0Pt.getX() - tail0Pt.getX()) - (head1Pt.getX() - tail1Pt.getX())*(head0Pt.getY() - tail0Pt.getY()));
	if(tmp == 0.0)
		return(false);
	double tmpv = ((tail1Pt.getX() - tail0Pt.getX())*(head0Pt.getY() - tail0Pt.getY()) - (tail1Pt.getY() - tail0Pt.getY())*(head0Pt.getX() - tail0Pt.getX())) / tmp;
	tmp = (head0Pt.getX() - tail0Pt.getX())*(head1Pt.getY() - tail1Pt.getY()) - (head1Pt.getX() - tail1Pt.getX())*(head0Pt.getY() - tail0Pt.getY());
	if(tmp == 0.0)
		return(false);
	double tmpu = ((tail1Pt.getX() - tail0Pt.getX())*(head1Pt.getY() - tail1Pt.getY()) + (head1Pt.getX() - tail1Pt.getX())*(tail0Pt.getY() - tail1Pt.getY()))/tmp;

	intersect.setLocation(
		tail1Pt.getX() + tmpv*(head1Pt.getX() - tail1Pt.getX()),
		tail1Pt.getY() + tmpv*(head1Pt.getY() - tail1Pt.getY()));

	// intersect[ZCoor] = tail1Pt[ZCoor] + tmpv*(head1Pt[ZCoor] - tail1Pt[ZCoor]);

	uArray[0] = tmpu; // u of first ray
	uArray[1] = tmpv; // u of second ray

	return (true);
}

public static boolean
raysIntersectInPlane(BLine2D ray0, BLine2D ray1, Point2D intersect, double[] uArray)
{
	return (raysIntersectInPlane(ray0.getP1(), ray0.getP2(), ray1.getP1(), ray1.getP2(),
		intersect, uArray));
}

public boolean
raysIntersectInPlane(BLine2D ray1, Point2D intersect, double[] uArray)
{
	return (raysIntersectInPlane(this.getP1(), this.getP2(), ray1.getP1(), ray1.getP2(),
		intersect, uArray));
}

public static boolean
get2DBoxRayIntersect(Point2D boxCenter, double offSetX, double offSetY,
	Point2D rayTailPt, Point2D rayHeadPt, Point2D intersect, double[] uArray)
{
	Point2D bottomTailPt = new Point2D.Double(boxCenter.getX() - offSetX,
		boxCenter.getY() - offSetY);
	Point2D bottomHeadPt = new Point2D.Double(boxCenter.getX() + offSetX,
		boxCenter.getY() - offSetY);

	Point2D leftTailPt = new Point2D.Double(boxCenter.getX() - offSetX,
		boxCenter.getY() - offSetY);
	Point2D leftHeadPt = new Point2D.Double(boxCenter.getX() - offSetX,
		boxCenter.getY() + offSetY);

	Point2D topTailPt = new Point2D.Double(boxCenter.getX() - offSetX,
		boxCenter.getY() + offSetY);
	Point2D topHeadPt = new Point2D.Double(boxCenter.getX() + offSetX,
		boxCenter.getY() + offSetY);

	Point2D rightTailPt = new Point2D.Double(boxCenter.getX() + offSetX,
		boxCenter.getY() + offSetY);
	Point2D rightHeadPt = new Point2D.Double(boxCenter.getX() + offSetX,
		boxCenter.getY() - offSetY);
	
	Point2D tmpPt = new Point2D.Double();
	
	uArray[0] = java.lang.Double.MAX_VALUE;

	double[] usArray = new double[2];	

	if (raysIntersectInPlane(rayTailPt, rayHeadPt, bottomTailPt, bottomHeadPt, tmpPt, usArray))
	{
		if((usArray[0] >= 0.0) && (usArray[0] <= 1.0) && (usArray[1] >= 0.0) && (usArray[1] <= 1.0))
		{
			intersect.setLocation(tmpPt.getX(), tmpPt.getY());
			uArray[0] = usArray[0];
		}
	}
	if (raysIntersectInPlane(rayTailPt, rayHeadPt, leftTailPt, leftHeadPt, tmpPt, usArray))
	{
		if((usArray[0] >= 0.0) && (usArray[0] <= 1.0) && (usArray[1] >= 0.0) && (usArray[1] <= 1.0) && (usArray[0] < uArray[0]))
		{
			intersect.setLocation(tmpPt.getX(), tmpPt.getY());
			uArray[0] = usArray[0];
		}
	}
	if (raysIntersectInPlane(rayTailPt, rayHeadPt, topTailPt, topHeadPt, tmpPt, usArray))
	{
		if((usArray[0] >= 0.0) && (usArray[0] <= 1.0) && (usArray[1] >= 0.0) && (usArray[1] <= 1.0) && (usArray[0] < uArray[0]))
		{
			intersect.setLocation(tmpPt.getX(), tmpPt.getY());
			uArray[0] = usArray[0];
		}
	}
	if (raysIntersectInPlane(rayTailPt, rayHeadPt, rightTailPt, rightHeadPt, tmpPt, usArray))
	{
		if((usArray[0] >= 0.0) && (usArray[0] <= 1.0) && (usArray[1] >= 0.0) && (usArray[1] <= 1.0) && (usArray[0] < uArray[0]))
		{
			intersect.setLocation(tmpPt.getX(), tmpPt.getY());
			uArray[0] = usArray[0];
		}
	}

	return(uArray[0] < java.lang.Double.MAX_VALUE);
}

public static boolean
getRectangleRayIntersect(Rectangle2D rect, Point2D rayTailPt, Point2D rayHeadPt,
	Point2D intersect0Pt, Point2D intersect1Pt, double[] uArray, int[] sideArray)
{
	double lowerUVal = 0.0;
	// needs to be this value for jdk1.4 for certain cases;
	// works with 1.0 in jdk1.3
	double upperUVal = 1.00000000000001;

	Point2D bottomTailPt = new Point2D.Double(
		rect.getX(),
		rect.getY() - rect.getHeight());
	Point2D bottomHeadPt = new Point2D.Double(
		rect.getX() + rect.getWidth(),
		rect.getY() - rect.getHeight());

	Point2D leftTailPt = new Point2D.Double(
		rect.getX(),
		rect.getY() - rect.getHeight());
	Point2D leftHeadPt = new Point2D.Double(
		rect.getX(),
		rect.getY());

	Point2D topTailPt = new Point2D.Double(
		rect.getX(),
		rect.getY());
	Point2D topHeadPt = new Point2D.Double(
		rect.getX() + rect.getWidth(),
		rect.getY());

	Point2D rightTailPt = new Point2D.Double(
		rect.getX() + rect.getWidth(),
		rect.getY());
	Point2D rightHeadPt = new Point2D.Double(
		rect.getX() + rect.getWidth(),
		rect.getY() - rect.getHeight());
	
	Point2D tmpPt = new Point2D.Double();
	
	uArray[0] = -1.0;
	uArray[1] = -1.0;

	double[] intersectUs = new double[2];	

	if (raysIntersectInPlane(rayTailPt, rayHeadPt,
		bottomTailPt, bottomHeadPt, tmpPt, intersectUs))
	{
		// debug("LOOKING at bottom: " + intersectUs[0] + " " + intersectUs[1]);
		// debug("rayTailPt: " + rayTailPt);
		// debug("rayHeadPt: " + rayHeadPt);
		// debug("bottomTailPt: " + bottomTailPt);
		// debug("bottomHeadPt: " + bottomHeadPt);
		if((intersectUs[0] >= lowerUVal) && (intersectUs[0] <= upperUVal) &&
			(intersectUs[1] >= lowerUVal) && (intersectUs[1] <= upperUVal))
		{
			// debug("intersects bottom at: " + tmpPt.getX() + " " + tmpPt.getY());
			intersect0Pt.setLocation(tmpPt.getX(), tmpPt.getY());
			uArray[0] = intersectUs[0];
			sideArray[0] = 3;
		}
	}
	if (raysIntersectInPlane(rayTailPt, rayHeadPt,
		leftTailPt, leftHeadPt, tmpPt, intersectUs))
	{
		// debug("LOOKING at left: " + intersectUs[0] + " " + intersectUs[1]);
		if((intersectUs[0] >= lowerUVal) && (intersectUs[0] <= upperUVal) &&
			(intersectUs[1] >= lowerUVal) && (intersectUs[1] <= upperUVal))
		{
			// debug("intersects left at: " + tmpPt.getX() + " " + tmpPt.getY());
			if ((uArray[0] >= lowerUVal) && (uArray[0] <= upperUVal))
			{
				if (!MathUtil.precisionEquality(uArray[0], intersectUs[0]))
				{
					uArray[1] = intersectUs[0];
					intersect1Pt.setLocation(tmpPt.getX(), tmpPt.getY());
					sideArray[1] = 0;
				}
			}
			else
			{
				uArray[0] = intersectUs[0];
				intersect0Pt.setLocation(tmpPt.getX(), tmpPt.getY());
				sideArray[0] = 0;
			}
		}
	}
	if (raysIntersectInPlane(rayTailPt, rayHeadPt,
		topTailPt, topHeadPt, tmpPt, intersectUs))
	{
		// debug("LOOKING at top: " + intersectUs[0] + " " + intersectUs[1]);
		if((intersectUs[0] >= lowerUVal) && (intersectUs[0] <= upperUVal) &&
			(intersectUs[1] >= lowerUVal) && (intersectUs[1] <= upperUVal))
		{
			// debug("intersects top at: " + tmpPt.getX() + " " + tmpPt.getY());
			if ((uArray[0] >= lowerUVal) && (uArray[0] <= upperUVal) && 
				(uArray[1] >= lowerUVal) && (uArray[1] <= upperUVal))
			{
				// if both filled properly don't do anything
			}
			else if ((uArray[0] >= lowerUVal) && (uArray[0] <= upperUVal))
			{
				if (!MathUtil.precisionEquality(uArray[0], intersectUs[0]))
				{
					uArray[1] = intersectUs[0];
					intersect1Pt.setLocation(tmpPt.getX(), tmpPt.getY());
					sideArray[1] = 1;
				}
			}
			else
			{
				uArray[0] = intersectUs[0];
				intersect0Pt.setLocation(tmpPt.getX(), tmpPt.getY());
				sideArray[0] = 1;
			}
		}
	}
	if (raysIntersectInPlane(rayTailPt, rayHeadPt,
		rightTailPt, rightHeadPt, tmpPt, intersectUs))
	{
		// debug("LOOKING at right: " + intersectUs[0] + " " + intersectUs[1]);
		if((intersectUs[0] >= lowerUVal) && (intersectUs[0] <= upperUVal) &&
			(intersectUs[1] >= lowerUVal) && (intersectUs[1] <= upperUVal))
		{
			// debug("intersects right at: " + tmpPt.getX() + " " + tmpPt.getY());
			if ((uArray[0] >= lowerUVal) && (uArray[0] <= upperUVal) && 
				(uArray[1] >= lowerUVal) && (uArray[1] <= upperUVal))
			{
				// if both filled properly don't do anything
			}
			else if ((uArray[0] >= lowerUVal) && (uArray[0] <= upperUVal))
			{
				if (!MathUtil.precisionEquality(uArray[0], intersectUs[0]))
				{
					uArray[1] = intersectUs[0];
					intersect1Pt.setLocation(tmpPt.getX(), tmpPt.getY());
					sideArray[1] = 2;
				}
			}
			else
			{
				uArray[0] = intersectUs[0];
				intersect0Pt.setLocation(tmpPt.getX(), tmpPt.getY());
				sideArray[0] = 2;
			}
		}
	}

	// if intersects then must at least have uArray[0] set to
	// between lowerUVal and upperUVal
	return((uArray[0] >= lowerUVal) && (uArray[0] <= upperUVal));
}

public static boolean
getRectangleRayIntersect(Rectangle2D rect, BLine2D ray,
	Point2D intersect0Pt, Point2D intersect1Pt, double[] uArray, int[] sideArray)
{
	return (getRectangleRayIntersect(rect,
		ray.getP1(), ray.getP2(),
		intersect0Pt, intersect1Pt, uArray, sideArray));
}

// the angle in degrees between the + x-axis and bline2d after bline2d has been
// translated to origin
public double
angleInXYPlane()
{
	BVector2d transPt = new BVector2d(
		this.getP2().getX() - this.getP1().getX(),
		this.getP2().getY() - this.getP1().getY());
	
	// debug("transPt: " + transPt);

	double angle = MathDefines.RadToDeg * new Vector2d(1.0, 0.0).angle(transPt);
	if (transPt.getY() < 0.0)
	{
		// debug("ang0: " + (360.0 - angle));
		return(360.0 - angle);
	}
	// debug("ang1: " + angle);
	return(angle);
}

public double
getAngle()
{
	return (angleInXYPlane());
}

/*
** returns a t value for a vector given distance from
** tailpt to headpt if t==0.0 is t value at tailpt, and t==1.0 is t
** value at headpt.
*/

public double
tValue(double distance)
{
	return (distance/this.length());
}

public float
tValue(float distance)
{
	return (distance/(float)this.length());
}

// returns the t value of offset pt perpendicular to this line.
public double
offsetPtTValue(double x0, double y0)
{
	double x1 = this.getP1().getX();
	double y1 = this.getP1().getY();
	double x2 = this.getP2().getX();
	double y2 = this.getP2().getY();
	double a = x2 - x1;
	double b = y2 - y1;
	return (-(a*(x1 - x0) + b*(y1 - y0))/(a*a + b*b));
}

public double
offsetPtTValue(Point2D offsetPt)
{
	return (this.offsetPtTValue(offsetPt.getX(), offsetPt.getY()));
}

/*
** ptAtT() returns a new point, on the line given by the vector
** defined by tailpt and headpt, a multiple of t times away from tail.
** (was GetVectorLineAtT()).
*/

public Point2D
ptAtT(double t)
{
	return(new Point2D.Double(
		this.getP1().getX() + t *
			(this.getP2().getX() - this.getP1().getX()),
		this.getP1().getY() + t *
			(this.getP2().getY() - this.getP1().getY())));
}

public void
ptAtT(Point2D newPt, double t)
{
	newPt.setLocation(
		this.getP1().getX() + t * (this.getP2().getX() - this.getP1().getX()),
		this.getP1().getY() + t * (this.getP2().getY() - this.getP1().getY()));
}

public Point2D
ptAtDistance(double distance)
{
	return (this.ptAtT(this.tValue(distance)));
}

// return the pt perpendicular to this line from offsetPt
public Point2D
getPerpLinePtFromOffSetPt(Point2D offsetPt)
{
	return (this.ptAtT(this.offsetPtTValue(offsetPt)));
}

public void
getPerpendicularPointAtT(double t, Point2D tailPerpPt,
	Point2D headPerpPt, double length, boolean positiveSide)
{
	ptAtT(tailPerpPt, t);

	double deltaX = this.getP2().getX() - this.getP1().getX();
	double deltaY = this.getP2().getY() - this.getP1().getY();
	double multLength = length/Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	deltaX *= multLength;
	deltaY *= multLength;
	if (positiveSide)
		headPerpPt.setLocation(tailPerpPt.getX() + deltaY,
			tailPerpPt.getY() - deltaX);
	else
		headPerpPt.setLocation(tailPerpPt.getX() - deltaY,
			tailPerpPt.getY() + deltaX);
}

/*
** GetPerpendicularPointsAtT() returns the 2 points (tailPerpPt &
** headPerpPt) which are perpendicular to a new point that is a t value
** from tailPt to headPt a distance length from the new point.
** positiveSide is true or false depending on whether headPerpPt is on
** positive or negative side of line starting from tailPt -> headPt.
** tailPerpPt will be opposite headPerpPt an equal distance from line.
*/

public void
getPerpendicularPointsAtT(double t, Point2D tailPerpPt,
	Point2D headPerpPt, double length, boolean positiveSide)
{
	if (this.getP1().equals(this.getP2()))
	{
		tailPerpPt.setLocation(this.getP1());
		headPerpPt.setLocation(this.getP2());
		return;
	}
	double deltaX = this.getX2() - this.getX1();
	double deltaY = this.getY2() - this.getY1();
	double multLength = length/Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	deltaX *= multLength;
	deltaY *= multLength;

	getPointAtT(tailPerpPt, t);

	if(positiveSide)
	{
		headPerpPt.setLocation(tailPerpPt.getX() + deltaY, tailPerpPt.getY() - deltaX);
		tailPerpPt.setLocation(
			tailPerpPt.getX() - deltaY,
			tailPerpPt.getY() + deltaX);
	}
	else
	{
		headPerpPt.setLocation(tailPerpPt.getX() - deltaY, tailPerpPt.getY() + deltaX);
		tailPerpPt.setLocation(
			tailPerpPt.getX() + deltaY,
			tailPerpPt.getY() - deltaX);
	}
}

// returns ray with tailpt at midpt of 'this' at t
public BLine2D
getPerpendicularRayAtT(double t, double length, boolean positiveSide)
{
	Point2D tailPerpPt = new Point2D.Double();
	Point2D headPerpPt = new Point2D.Double();

	if (this.getP1().equals(this.getP2()))
	{
		tailPerpPt.setLocation(this.getP1());
		headPerpPt.setLocation(this.getP2());
		return (new BLine2D(tailPerpPt, headPerpPt));
	}
	double deltaX = this.getX2() - this.getX1();
	double deltaY = this.getY2() - this.getY1();
	double multLength = length/Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	deltaX *= multLength;
	deltaY *= multLength;

	this.getPointAtT(tailPerpPt, t);

	if(positiveSide)
	{
		headPerpPt.setLocation(tailPerpPt.getX() + deltaY, tailPerpPt.getY() - deltaX);
		/*
		tailPerpPt.setLocation(
			tailPerpPt.getX() - deltaY,
			tailPerpPt.getY() + deltaX);
		*/
	}
	else
	{
		headPerpPt.setLocation(tailPerpPt.getX() - deltaY, tailPerpPt.getY() + deltaX);
		/*
		tailPerpPt.setLocation(
			tailPerpPt.getX() + deltaY,
			tailPerpPt.getY() - deltaX);
		*/
	}
	return (new BLine2D(tailPerpPt, headPerpPt));
}

public void
normalize()
{
	double tailPtX = this.getP1().getX();
	double tailPtY = this.getP1().getY();

	this.shiftToOrigin();
	BVector2d normalPt = new BVector2d(this.getP2().getX(), this.getP2().getY());
	normalPt.normalize();
	this.setLine(tailPtX, tailPtY, tailPtX + normalPt.getX(), tailPtY + normalPt.getY());
}

public String
toString()
{
	return("BLine2D: " + getP1() + " " + getP2());
}

public static void
debug(String s)
{
	System.out.println("BLine2D-> " + s);
}

}
