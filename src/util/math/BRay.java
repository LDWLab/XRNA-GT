package util.math;

import java.util.Vector;

import javax.vecmath.*;

import util.*;

/**
** ray is defined by 3 position vectors, positionVector to show
** direction and length of ray, tailPt to define start
** of ray, headPt that defines the end of ray which
** is headPt - tailPt translated. Therefore:
** headPt = positionVector + tailPt
**
** resetting tailPt will not affect positionVector, but headPt needs
** to be refigured.
**
** currently just altering tailPt or postionVector and resetting headPt
** upon either alteration.
*/

public class
BRay
{

public static boolean isJDK13_0 = false;
public static boolean isJDK14_1_01 = false;

static
{
	String javaVersion = System.getProperty("java.version");
	debug("Running BRay with javaVersion: " + javaVersion);
	isJDK13_0 = isJDK14_1_01 = false;
	if (javaVersion.equals("1.4.1_01"))
		isJDK14_1_01 = true;
	else if (javaVersion.equals("1.3.0"))
		isJDK13_0 = true;
	if (!(isJDK13_0 || isJDK14_1_01))
		debug("WARNING: Untested java version: " + javaVersion);
}

public
BRay()
{
	this.setHeadPt(new BVector3d());
	this.setTailPt(new BVector3d());
	this.setPositionVector(new BVector3d());
}

public
BRay(BRay ray)
{
	this(ray.getHeadPt(), ray.getTailPt());
}

// make a new ray with headPt x,y,z values
public
BRay(double xPos, double yPos, double zPos)
{
	this();
	this.setHeadPt(xPos, yPos, zPos);
	this.resetPositionVector();
}

// make a new ray with headPt
public
BRay(BPoint3d headPt)
{
	this();
	this.getHeadPt().copy(headPt.getBVector3d());
	this.resetPositionVector();
}

// make a new ray with positionVector
public
BRay(BVector3d vec)
{
	this();
	this.getPositionVector().copy(vec);
	this.resetHeadPt();
}

// make a new ray with just tailPt and headPt. refigures positionVector.
public
BRay(BPoint3d tailPt, BPoint3d headPt)
{
	this();
	this.getTailPt().copy(tailPt.getBVector3d());
	this.getHeadPt().copy(headPt.getBVector3d());
	this.resetPositionVector();
}

// make a new ray with just tailPt and headPt. refigures positionVector.
public
BRay(BVector3d tailPt, BVector3d headPt)
{
	this();
	this.setTailPt(tailPt.getX(), tailPt.getY(), tailPt.getZ());
	this.setHeadPt(headPt.getX(), headPt.getY(), headPt.getZ());
	this.resetPositionVector();
}

// make a new ray with just tailPt and headPt x,y,z values
public
BRay(double tX, double tY, double tZ, double hX, double hY, double hZ)
{
	this(new BPoint3d(tX, tY, tZ), new BPoint3d(hX, hY, hZ));
}

private BVector3d tailPt = null;

public void
setTailPt(BVector3d tailPt)
{
	this.tailPt = tailPt;
}

public void
setTailPt(double xPos, double yPos, double zPos)
{
	this.getTailPt().set(xPos, yPos, zPos);
}

public BVector3d
getTailPt()
{
	return (this.tailPt);
}

private BVector3d headPt = null;

public void
setHeadPt(BVector3d headPt)
{
	this.headPt = headPt;
}

public void
setHeadPt(double xPos, double yPos, double zPos)
{
	this.getHeadPt().set(xPos, yPos, zPos);
}

public BVector3d
getHeadPt()
{
	return (this.headPt);
}

// position vector is headPt translated to origin: headPt - tailPt
private BVector3d positionVector = null;

public void
setPositionVector(BVector3d positionVector)
{
	this.positionVector = positionVector;
}

public void
setPositionVector(double xPos, double yPos, double zPos)
{
	this.getPositionVector().set(xPos, yPos, zPos);
}

public BVector3d
getPositionVector()
{
	return (this.positionVector);
}

/**
** sets tailpt headpt from params, resets position vec directly.
*/

public void
setRay(double tX, double tY, double tZ, double hX, double hY, double hZ)
{
	this.setTailPt(tX, tY, tZ);
	this.setHeadPt(hX, hY, hZ);
	this.resetPositionVector();
}

private Vector tVals = null;

public void
setTVals(Vector tVals)
{
	this.tVals = tVals;
}

public Vector
getTVals()
{
	return (this.tVals);
}

public void
setNewTValue(double tVal)
{
	if (this.tVals == null)
		this.tVals = new Vector();
	else
		this.tVals.clear();
	setNextTValue(tVal);
}

public void
setNextTValue(double tVal)
{
	if (this.tVals == null)
		this.tVals = new Vector();
	this.tVals.addElement(new Double(tVal));
}

/* Need to see if this makes sense for 3D
GetCoefficients(lineseg)
struct LineInformation *lineseg;
{
	double X1,Y1,X2,Y2;

	X1 = lineseg->fpt[XCoor];
	Y1 = lineseg->fpt[YCoor];
	X2 = lineseg->spt[XCoor];
	Y2 = lineseg->spt[YCoor];
	lineseg->Coefficient.ACoeff = Y1 - Y2;
	lineseg->Coefficient.BCoeff = X2 - X1;
	lineseg->Coefficient.CCoeff = X1*Y2 - X2*Y1;
}
*/

public double
getTailX()
{
	return (tailPt.getX());
}

public double
getTailY()
{
	return (tailPt.getY());
}

public double
getTailZ()
{
	return (tailPt.getZ());
}

public double
getHeadX()
{
	return (headPt.getX());
}

public double
getHeadY()
{
	return (headPt.getY());
}

public double
getHeadZ()
{
	return (headPt.getZ());
}

public void
setX(double x)
{
	if (Double.compare(x, -0.0) == 0)
		this.getPositionVector().setX(0.0);
	else
		this.getPositionVector().setX(x);
}

public double
getX()
{
	return (positionVector.getX());
}

public void
setY(double y)
{
	if (Double.compare(y, -0.0) == 0)
		this.getPositionVector().setY(0.0);
	else
		this.getPositionVector().setY(y);
}

public double
getY()
{
	return (positionVector.getY());
}

public void
setZ(double z)
{
	if (Double.compare(z, -0.0) == 0)
		this.getPositionVector().setZ(0.0);
	else
		this.getPositionVector().setZ(z);
}

public double
getZ()
{
	return (positionVector.getZ());
}

/**
** Sets positionVector to headPt - tailPt so shifted to origin.
**
** NEED a routine that maps any change to a position vector (or
** a unit vector) back to ray.
*/

public void
resetPositionVector()
{
	setPositionVector(
		getHeadX() - getTailX(),
		getHeadY() - getTailY(),
		getHeadZ() - getTailZ());
}

/**
** Sets headPt to positionVector + tailPt so shifts headPt
** to tailPt the same direction and magnitued as positionVector.
*/

public void
resetHeadPt()
{
	setHeadPt(
		getX() + getTailX(),
		getY() + getTailY(),
		getZ() + getTailZ());
}

/******* Vector Math Methods **************/

public double
length()
{
	return (this.getPositionVector().length());
}

public double
angle(BVector3d vec)
{
	return (((double)MathDefines.RadToDeg) *
		this.getPositionVector().angle(vec));
}

public double
angle(BRay ray)
{
	return (((double)MathDefines.RadToDeg) *
		this.getPositionVector().angle(ray.getPositionVector()));
}

public static final BVector3d xNormal = new BVector3d(1.0, 0.0, 0.0);
public static final BVector3d yNormal = new BVector3d(0.0, 1.0, 0.0);
public static final BVector3d zNormal = new BVector3d(0.0, 0.0, 1.0);

public double
angle()
{
	return (this.angle(xNormal));
}

public double
angleInXYPlane()
{
	return (this.ptInXYPlane().angle(xNormal));
}

public static BVector3d
ptInXYPlane(BVector3d pt)
{
	return (new BVector3d(pt.getX(), pt.getY(), 0.0));
}

public BVector3d
ptInXYPlane()
{
	return (this.ptInXYPlane(this.getPositionVector()));
}

public double
angleInZXPlane()
{
	return (this.ptInZXPlane().angle(zNormal));
}

public static BVector3d
ptInZXPlane(BVector3d pt)
{
	return (new BVector3d(pt.getX(), 0.0, pt.getZ()));
}

public BVector3d
ptInZXPlane()
{
	return (this.ptInZXPlane(this.getPositionVector()));
}

public double
angleInYZPlane()
{
	return (this.ptInYZPlane().angle(yNormal));
}

public static BVector3d
ptInYZPlane(BVector3d pt)
{
	return (new BVector3d(0.0, pt.getY(), pt.getZ()));
}

public BVector3d
ptInYZPlane()
{
	return (this.ptInYZPlane(this.getPositionVector()));
}

// angle is in counter clockwise direction in same plane of ray
public BRay
getRayAtAngle(double angle)
{
	BRay newRay = new BRay(this.ptInXYPlane());
	return (newRay);
}

public void
setToSphericalVector(double radius, double uVal, double vVal)
{
	this.setX(radius * (double)Math.sin((double)uVal) * (double)Math.cos((double)vVal));
	this.setY(radius * (double)Math.sin((double)uVal) * (double)Math.sin((double)vVal));
	this.setZ(radius * (double)Math.cos((double)uVal));
	this.resetHeadPt();
}

public void
setSphericalUVVals()
throws Exception
{
	BVector3d normal = this.getNormal();
	double bX = normal.getX();
	double bY = normal.getY();
	double bZ = normal.getZ();
	double uVal = Double.MAX_VALUE;
	double vVal = Double.MAX_VALUE;
	if (bY == 0.0)
	{
		// vVal = Math.asin(0.0);
		vVal = 0.0;
	}
	else if (bX == 0.0)
	{
		vVal = Math.acos(0.0);
	}
	else
	{
		vVal = Math.atan(bY/bX);
	}

	uVal = Math.abs(Math.acos(bZ));

	// MAYBE redo in radians: (but probably doesn't matter)
	double vDegrees = MathDefines.RadToDeg * vVal;
	if (vDegrees > 360.0)
		vDegrees -= 360.0;
	if (vDegrees < 0.0)
		vDegrees += 360.0;

	if ((bX >= 0.0) && (bY >= 0.0))
	{
		if ((vDegrees >= 0.0) && (vDegrees <= 90.0))
			uVal = uVal;
		else if ((vDegrees >= 180.0) && (vDegrees <= 270.0))
			uVal = -uVal;
	}
	else if ((bX < 0.0) && (bY >= 0.0))
	{
		if ((vDegrees >= 90.0) && (vDegrees <= 180.0))
			uVal = uVal;
		else if ((vDegrees >= 270.0) && (vDegrees <= 360.0))
			uVal = -uVal;
	}
	else if ((bX < 0.0) && (bY < 0.0))
	{
		if ((vDegrees >= 0.0) && (vDegrees <= 90.0))
			uVal = -uVal;
		else if ((vDegrees >= 180.0) && (vDegrees <= 270.0))
			uVal = uVal;
	}
	else if ((bX >= 0.0) && (bY < 0.0))
	{
		if ((vDegrees >= 90.0) && (vDegrees <= 180.0))
			uVal = -uVal;
		else if ((vDegrees >= 270.0) && (vDegrees <= 360.0))
			uVal = uVal;
	}
	else
		throw new Exception("Error in BRay.setSphericalUVVals(), Can't determine quadrant: " + bX + " " + bY + " " + vDegrees);


	if ((uVal == Double.NaN) || (vVal == Double.NaN))
		throw new Exception("Error in BRay.setSphericalUVVals(), uVal,vVal: " + uVal + " " + vVal);
	setSphericalUVal(uVal);
	setSphericalVVal(vVal);
}

private double sphericalUVal = 0.0;

public void
setSphericalUVal(double sphericalUVal)
{
    this.sphericalUVal = sphericalUVal;
}

public double
getSphericalUVal()
{
    return (this.sphericalUVal);
}

private double sphericalVVal = 0.0;

public void
setSphericalVVal(double sphericalVVal)
{
    this.sphericalVVal = sphericalVVal;
}

public double
getSphericalVVal()
{
    return (this.sphericalVVal);
}

/**
** just translate the tailPt. reset headPt. position vector remains
** invariante under translation.
*/

public void
translate(double xPos, double yPos, double zPos)
{
	this.getTailPt().translate(xPos, yPos, zPos);
	resetHeadPt();
}

public void
translate(BPoint3d transPt)
{
	this.translate(transPt.getX(), transPt.getY(), transPt.getZ());
}

public void
translate(BVector3d transPt)
{
	this.translate(transPt.getX(), transPt.getY(), transPt.getZ());
}

public void
changeLength(double length)
{
	resetPositionVector();
	length /= this.length();
	setPositionVector(
		getX() * length,
		getY() * length,
		getZ() * length);
	// NOW need to map this back to ray
}

public void
lengthenRay(double length)
{
	/* BUG: doesn't work yet
	double halfLength = length/2.0;
	debug("IN lengthenRay, halfLength: " + halfLength);
	debug("In lengthenRay, RL: " + this.length());
	getPointAtT(getHeadPt(), getTValue(this.length() + halfLength));
	getPointAtT(getTailPt(), getTValue(-halfLength));
	this.resetPositionVector();
	*/
}

public void
lengthenHeadPt(double length)
{
	/* BUG: doesn't work yet
	getPointAtT(getHeadPt(), getTValue(this.length() + length));
	*/
}

public void
lengthenTailPt(double length)
{
	/* BUG: doesn't work yet
	getPointAtT(getTailPt(), getTValue(-length));
	*/
}

/**
** Returns a t value for a vector given distance from
** tailpt to headpt if t==0.0 is t value at tailpt, and t==1.0 is t
** value at headpt.
*/

public double
getTValue(double distance)
{
	return(distance / this.length());
}

/**
** return the positional vector that represents the midpt
** between tailPt and headPt or ray.
*/

public void
midPoint(BPoint3d midPt)
{
	midPt.set(
		(getTailX() + getHeadX())/2.0,
		(getTailY() + getHeadY())/2.0,
		(getTailZ() + getHeadZ())/2.0);
}

public BPoint3d
midPoint()
{
	BPoint3d midPt = new BPoint3d();
	this.midPoint(midPt);
	return (midPt);
}

/**
** REWORK using vecmath
**
** DistPtToLine() returns the distance from a point, pt, to the line
** segment defined by tailPt, headPt. It also assigns to this.tVals
** the t value needed to find the point on line that cooresponds to
** the minimum distance found at this.tVals first cell.
*/

public double
distPtToLine(BPoint3d pt)
{
	double x = 0.0, y = 0.0, z = 0.0, x1 = 0.0, y1 = 0.0, z1 = 0.0;
	double x0 = 0.0, y0 = 0.0, z0 = 0.0, a = 0.0, b = 0.0, c = 0.0;
	double x2 = 0.0, y2 = 0.0, z2 = 0.0;
	double t = 0.0;

	x0 = pt.getX();
	y0 = pt.getY();
	z0 = pt.getZ();
	x1 = this.getTailX();
	y1 = getTailY();
	z1 = getTailZ();
	x2 = getHeadX();
	y2 = getHeadY();
	z2 = getHeadZ();
	
	a = x2 - x1;
	b = y2 - y1;
	c = z2 - z1;

	t = - (a*(x1 - x0) + b*(y1 - y0) + c*(z1 - z0))/ (a*a + b*b + c*c);
	setNewTValue(t);
	x = x1 + a*t;
	y = y1 + b*t;
	z = z1 + c*t;

	return ((double)Math.sqrt(
		(x - x0)*(x - x0) + (y - y0)*(y - y0) + (z - z0)*(z - z0)));
}

/**
** Sets a new point, on this vector, a multiple of t times away
** from tail.
*/

public void
getPointAtT(BPoint3d newPt, double t)
throws Exception
{
	if ((this.tailPt == null) || (this.headPt == null) || (newPt == null))
		throw new Exception("BPoint3d is null in BRay.getPointAtT");
	newPt.set(
		getTailX() +
			(t * (getHeadX() - getTailX())),
		getTailY() +
			(t * (getHeadY() - getTailY())),
		getTailZ() +
			(t * (getHeadZ() - getTailZ())));
}

public BPoint3d
getPointAtT(double t)
throws Exception
{
	BPoint3d newPt = new BPoint3d();
	getPointAtT(newPt, t);
	return(newPt);
}

// NEED to develop the concept of a ray being a linear
// combination of a vector space different than the ijk vector space.
/*
private double iComponent = 0.0;

public void
setIComponent(double iComponent)
{
    this.iComponent = iComponent;
}

public double
getIComponent()
{
    return (this.iComponent);
}

private double jComponent = 0.0;

public void
setJComponent(double jComponent)
{
    this.jComponent = jComponent;
}

public double
getJComponent()
{
    return (this.jComponent);
}

private double kComponent = 0.0;

public void
setKComponent(double kComponent)
{
    this.kComponent = kComponent;
}

public double
getKComponent()
{
    return (this.kComponent);
}

public void
setIJKComponents()
{
	BVector3d normal = this.getNormal();
	debug("ijk normal: " + normal);
	setIComponent(normal.getX());
	setJComponent(normal.getY());
	setKComponent(normal.getZ());
}
*/

public BVector3d
getNormal()
{
	if (this.getHeadPt().equals(this.getTailPt()))
		return (null);
	BVector3d normal = new BVector3d(this.getPositionVector());
	normal.normalize();
	return (normal);
}

// sets to unit vector with tailpt in same place
public void
normalize()
{
	this.getPositionVector().normalize();
	this.resetHeadPt();
}

public void
add(BRay ray)
{
	this.getPositionVector().add(ray.getPositionVector());
	this.resetHeadPt();
}

public void
add(BPoint3d point)
{
	this.getPositionVector().add(point);
	this.resetHeadPt();
}

public void
add(BVector3d vec)
{
	this.getPositionVector().add(vec);
	this.resetHeadPt();
}

public void
sub(BRay ray)
{
	this.getPositionVector().sub(ray.getPositionVector());
	this.resetHeadPt();
}

public void
sub(BPoint3d point)
{
	this.getPositionVector().sub(point);
	this.resetHeadPt();
}

public void
sub(BVector3d vec)
{
	this.getPositionVector().sub(vec);
	this.resetHeadPt();
}

// scales positionVector, resets headPt, with tailpt in same place
public void
scale(double scaleVal)
{
	this.getPositionVector().scale(scaleVal);
	this.resetHeadPt();
}

public BRay
getCrossProduct(BRay ray)
throws Exception
{
	BVector3d crossProduct = new BVector3d();
	crossProduct.cross(this.getPositionVector(), ray.getPositionVector());
	BRay tmpRay = new BRay(crossProduct.getX(), crossProduct.getY(),
		crossProduct.getZ());
	tmpRay.translate(this.getTailPt());
	return (tmpRay);
}

public BPoint3d
getRayIntersect(BRay ray, double[] uvVals)
{
	double tmp = 0.0;
	double tmpU = 0.0;
	double tmpV = 0.0;
	double tail0X = this.getTailPt().getX();
	double tail0Y = this.getTailPt().getY();
	double tail0Z = this.getTailPt().getZ();
	double tail1X = ray.getTailPt().getX();
	double tail1Y = ray.getTailPt().getY();
	double tail1Z = ray.getTailPt().getZ();
	double head0X = this.getHeadPt().getX();
	double head0Y = this.getHeadPt().getY();
	double head0Z = this.getHeadPt().getZ();
	double head1X = ray.getHeadPt().getX();
	double head1Y = ray.getHeadPt().getY();
	double head1Z = ray.getHeadPt().getZ();

	tmp = ((head1Y - tail1Y)*(head0X - tail0X) -
		(head1X - tail1X)*(head0Y - tail0Y));
	if(tmp == 0.0)
		return (null);
	tmpV = ((tail1X - tail0X)*(head0Y - tail0Y) -
		(tail1Y - tail0Y)*(head0X - tail0X)) / tmp;
	tmp = (head0X - tail0X)*(head1Y - tail1Y) -
		(head1X - tail1X)*(head0Y - tail0Y);
	if(tmp == 0.0)
		return (null);
	tmpU = ((tail1X - tail0X)*(head1Y - tail1Y) +
		(head1X - tail1X)*(tail0Y - tail1Y))/tmp;

	BPoint3d intersect = new BPoint3d(
		tail1X + tmpV*(head1X - tail1X),
		tail1Y + (tmpV*(head1Y - tail1Y)),
		tail1Z + tmpV*(head1Z - tail1Z));
	uvVals[0] = tmpU;
	uvVals[1] = tmpV;
	return (intersect);
}

public double[]
intersectsSphereAt(SphereInfo sphere)
{
	double h = sphere.getOrigin().getX();
	double k = sphere.getOrigin().getY();
	double l = sphere.getOrigin().getZ();
	double ax = this.getTailPt().getX();
	double ay = this.getTailPt().getY();
	double az = this.getTailPt().getZ();
	double cx = this.getHeadPt().getX();
	double cy = this.getHeadPt().getY();
	double cz = this.getHeadPt().getZ();

	double a = cz*cz - 2.0 *az*cz + cy*cy - 2.0 *ay*cy + cx*cx
		- 2.0 *ax*cx + az*az + ay*ay + ax *ax;
	double b = -2.0 *cz*l + 2.0 *az*l - 2.0 *cy*k + 2.0 *ay*k
		- 2.0 *cx*h + 2.0 *ax*h + 2.0 *az*cz + 2.0 *ay*cy
		+ 2.0 *ax*cx - 2.0 *az*az - 2.0 *ay*ay - 2.0 *ax*ax;
	double c = - (sphere.getRadius() * sphere.getRadius()) + l*l - 2.0 *az*l + k*k
		- 2.0 *ay*k + h*h - 2.0 *ax*h + az*az + ay*ay + ax*ax;

	double radical = b*b - (4.0 * a * c);
	if (radical < 0.0)
	{
		// uvVal[0] = uvVal[1] = -1.0;
		return (null);
	}
	double[] uvVals = new double[2];
	double rsqrt = (double)Math.sqrt(radical);

	uvVals[0] = (-b - rsqrt)/(2.0 * a);
	uvVals[1] = (-b + rsqrt)/(2.0 * a);

	return (uvVals);
}

/* NEED to get going:
public double
distanceToSphere(tailpt, headpt, origion, t)
point tailpt, headpt, origion;
double *t;
{
	double DistPtToLine();

	return(DistPtToLine(tailpt, headpt, origion, t));

}

public boolean
intersectsSphere(tailpt, headpt, sphereradius, sphereorigion)
point tailpt, headpt;
double sphereradius;
point sphereorigion;
{
	double t, firstt, lastt;
	double RaySphereDistance();

	if(RaySphereDistance(tailpt, headpt, sphereorigion, &t) > sphereradius)
		return(FALSE);
	RaySphereIntersect(tailpt, headpt, sphereorigion, sphereradius, &firstt, &lastt);
	if(((firstt >= 0.0) && (firstt <= 1.0)) || ((lastt >= 0.0) && (lastt <= 1.0)))
		return(TRUE);
	return(FALSE);
}
*/

// rotate around tailPt
// angles in radians
public void
rotate(double xAngle, double yAngle, double zAngle)
{
	BMatrix4d resultMat = BMatrix4d.setTransformation(
		this.getTailPt(), xAngle, yAngle, zAngle);
	resultMat.transform(this.getPositionVector());
	this.resetHeadPt();
}

public String
toString()
{
	/*
	return ("tail: " + this.getTailPt() + "head: " +
		this.getHeadPt() + "posVec: " + this.getPositionVector());
	*/
	return (this.getTailPt() + " " + this.getHeadPt() + " " +
		this.getPositionVector());
}

private static void
debug(String s)
{
	System.out.println("BRay-> " + s);
}

}
