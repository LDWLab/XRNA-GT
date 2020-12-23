package util;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Vector;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;

import util.math.BPoint3d;
import util.math.BVector3d;
import util.math.BVector4d;
import util.math.BVector4f;

public class
SphereInfo
extends Object
{

private double[] originVals = new double[3];

// make copy
public
SphereInfo(SphereInfo fromSphereInfo)
{
	this.set(fromSphereInfo.getX(), fromSphereInfo.getY(), fromSphereInfo.getZ(),
		fromSphereInfo.getRadius(), fromSphereInfo.getColor3f());
	this.setSerialNumber(fromSphereInfo.getSerialNumber());
	this.setAtomType(fromSphereInfo.getAtomType());
	this.setResidueID(fromSphereInfo.getResidueID());
	this.setChainID(fromSphereInfo.getChainID());
	this.setOrigin(fromSphereInfo.getOrigin());
	this.setColor(fromSphereInfo.getColor());
	this.setIsTransparent(fromSphereInfo.getIsTransparent());
	this.setName(fromSphereInfo.getName());

}

public
SphereInfo()
{
	// set the unit sphere at origin with color black
	this.set(0.0, 0.0, 0.0, 1.0, new Color3f());
}

public
SphereInfo(double x, double y, double z, double radius, Color3f color)
{
	this.set(x, y, z, radius, color);
}

public
SphereInfo(double x, double y, double z, double radius,
	boolean isTransparent, Color3f color)
{
	this.set(x, y, z, radius, color);
	this.setIsTransparent(isTransparent);
}

public
SphereInfo(double x, double y, double z, double radius)
{
	this(x, y, z, radius, new Color3f());
}

public
SphereInfo(BPoint3d pt, double radius, Color3f color)
{
	this.set(pt.getX(), pt.getY(), pt.getZ(), radius, color);
}

public
SphereInfo(BPoint3d pt, double radius, boolean isTransparent, Color3f color)
{
	this(pt.getX(), pt.getY(), pt.getZ(), radius, isTransparent, color);
}

public
SphereInfo(BPoint3d pt, double radius)
{
	this.set(pt.getX(), pt.getY(), pt.getZ(), radius);
}

public
SphereInfo(BVector3d pt, double radius, Color3f color)
{
	this.set(pt.getX(), pt.getY(), pt.getZ(), radius, color);
}

public
SphereInfo(BVector3d pt, double radius, boolean isTransparent, Color3f color)
{
	this(pt.getX(), pt.getY(), pt.getZ(), radius, isTransparent, color);
}

public
SphereInfo(BVector3d pt, double radius)
{
	this.set(pt.getX(), pt.getY(), pt.getZ(), radius);
}

public
SphereInfo(BVector4d pt, double radius)
{
	this.set(pt.getX(), pt.getY(), pt.getZ(), radius);
}

public
SphereInfo(BVector4f pt, double radius)
{
	this.set((double)pt.getX(), (double)pt.getY(), (double)pt.getZ(), radius);
}

public
SphereInfo(BVector4f pt, float radius)
{
	this.set((double)pt.getX(), (double)pt.getY(), (double)pt.getZ(), (double)radius);
}

public
SphereInfo(BPoint3d pt)
{
	// set to unit sphere
	this.set(pt.getX(), pt.getY(), pt.getZ(), 1.0);
}

public
SphereInfo(double radius)
{
	this(0.0, 0.0, 0.0, radius);
}

public
SphereInfo(double x, double y, double z, String chainID, int serialNumber,
	String atomType, int residueID)
{
	this.setPos(x, y, z);

	this.setSerialNumber(serialNumber);
	this.setAtomType(atomType);
	this.setChainID(chainID);
	this.setResidueID(residueID);

	// SEE "Notes on PDB ATOM record format" at bmerc-www.bu.edu
	// for description on CA being either alpha carbon or calcium
	double unknownRadius = 1.8f;
	// NEED to make sure I've got right van der Waals radii
	// these are from www.webelements.com
	if ( // carbon
		this.getAtomType().equals("C")   ||
		this.getAtomType().equals("C1") || // C in nuc??
		this.getAtomType().equals("C2") || // C in nuc??
		this.getAtomType().equals("C2A") || // C in nuc??
		this.getAtomType().equals("C2B") || // C in nuc??
		this.getAtomType().equals("C3") || // C in nuc??
		this.getAtomType().equals("C4") || // C in nuc??
		this.getAtomType().equals("C5") || // C in nuc??
		this.getAtomType().equals("C5A") || // C in nuc??
		this.getAtomType().equals("C6") || // C in nuc??
		this.getAtomType().equals("C7") || // C in nuc??
		this.getAtomType().equals("C8") || // C in nuc??
		this.getAtomType().equals("C9") || // C in nuc??
		this.getAtomType().equals("C10") || // C in nuc??
		this.getAtomType().equals("C11") || // C in nuc??
		this.getAtomType().equals("C12") || // C in nuc??
		this.getAtomType().equals("C13") || // C in nuc??
		this.getAtomType().equals("C14") || // C in nuc??
		this.getAtomType().equals("C15") || // C in nuc??
		this.getAtomType().equals("C16") || // C in nuc??
		this.getAtomType().equals("C17") || // C in nuc??
		this.getAtomType().equals("C18") || // C in nuc??
		this.getAtomType().equals("C19") || // C in nuc??
		this.getAtomType().equals("C21") || // C in nuc??
		this.getAtomType().equals("C24") || // C in nuc??
		this.getAtomType().equals("C1*") || // C in nuc??
		this.getAtomType().equals("C2*") || // C in nuc??
		this.getAtomType().equals("C3*") || // C in nuc??
		this.getAtomType().equals("C4*") || // C in nuc??
		this.getAtomType().equals("C5*") || // C in nuc??
		this.getAtomType().equals("CA")	 || // alpha carbon (see note above)
		this.getAtomType().equals("CG")  ||
		this.getAtomType().equals("CG1") ||
		this.getAtomType().equals("CG2") ||
		this.getAtomType().equals("CB")  ||
		this.getAtomType().equals("CE")  ||
		this.getAtomType().equals("CH")  ||
		this.getAtomType().equals("CH2") ||
		this.getAtomType().equals("CZ")  ||
		this.getAtomType().equals("CZ1") ||
		this.getAtomType().equals("CZ2") ||
		this.getAtomType().equals("CZ3"))
	{
		this.setColor3f(new Color3f(1.0f, 0.0f, 0.0f));
		this.setRadius(1.72);
	}
	else if ( // calcium (see note above)
		this.getAtomType().equals("CA"))
	{
		this.setColor3f(new Color3f(0.8f, 0.8f, 1.0f));
		this.setRadius(unknownRadius);
	}
	else if ( // cadmium?? or carbon?? (see note above)
		this.getAtomType().equals("CD")	||
		this.getAtomType().equals("CD1")||
		this.getAtomType().equals("CD2"))
	{
		this.setColor3f(new Color3f(1.0f, 0.0f, 0.0f));
		this.setRadius(1.58); // if cadmium
	}
	else if ( // cerium?? or carbon?? (see note above)
		this.getAtomType().equals("CE") ||
		this.getAtomType().equals("CE1") ||
		this.getAtomType().equals("CE2") ||
		this.getAtomType().equals("CE3"))
	{
		this.setColor3f(new Color3f(0.0f, 1.0f, 0.0f));
		this.setRadius(1.85); // if cerium
	}
	else if (
		this.getAtomType().equals("N") ||
		this.getAtomType().equals("N1") || // in nuc??
		this.getAtomType().equals("N2") || // in nuc??
		this.getAtomType().equals("N3") || // in nuc??
		this.getAtomType().equals("N4") || // in nuc??
		this.getAtomType().equals("N5") || // in nuc??
		this.getAtomType().equals("N6") || // in nuc??
		this.getAtomType().equals("N7") || // in nuc??
		this.getAtomType().equals("N8") || // in nuc??
		this.getAtomType().equals("N9") || // in nuc??
		this.getAtomType().equals("N20") || // in nuc??
		this.getAtomType().equals("NH1") ||
		this.getAtomType().equals("NH2") ||
		this.getAtomType().equals("NZ")) // if NZ is some kind of nitrogen
	{
		this.setColor3f(new Color3f(0.0f, 0.0f, 1.0f));
		this.setRadius(1.55);
	}
	else if (
		this.getAtomType().equals("NE") ||
		this.getAtomType().equals("NE1") ||
		this.getAtomType().equals("NE2"))
	{
		this.setColor3f(new Color3f(1.0f, 1.0f, 0.0f));// hopefully purple
		this.setRadius(1.54);
	}
	else if (
		this.getAtomType().equals("ND") ||
		this.getAtomType().equals("ND1") ||
		this.getAtomType().equals("ND2"))
	{
		this.setColor3f(new Color3f(1.0f, 1.0f, 0.0f));// hopefully purple
		this.setRadius(unknownRadius); // if Neodymium
	}
	else if ( // oxygen
		this.getAtomType().equals("O") ||
		this.getAtomType().equals("O1P") || // O off of P in nuc??
		this.getAtomType().equals("O2P") || // O off of P in nuc??
		this.getAtomType().equals("O1") || // O off of P in nuc??
		this.getAtomType().equals("O2") || // O off of P in nuc??
		this.getAtomType().equals("O3") || // O off of P in nuc??
		this.getAtomType().equals("O4") || // O off of P in nuc??
		this.getAtomType().equals("O5") || // O off of P in nuc??
		this.getAtomType().equals("O6") || // O off of P in nuc??
		this.getAtomType().equals("O7") || // O off of P in nuc??
		this.getAtomType().equals("O8") || // O off of P in nuc??
		this.getAtomType().equals("O9") || // O off of P in nuc??
		this.getAtomType().equals("O17") || // O off of P in nuc??
		this.getAtomType().equals("O18") || // O off of P in nuc??
		this.getAtomType().equals("O21") || // O off of P in nuc??
		this.getAtomType().equals("O22") || // O off of P in nuc??
		this.getAtomType().equals("O23") || // O off of P in nuc??
		this.getAtomType().equals("O2*") || // O off of P in nuc (what is *)??
		this.getAtomType().equals("O3*") || // O off of P in nuc (what is *)??
		this.getAtomType().equals("O4*") || // O off of P in nuc (what is *)??
		this.getAtomType().equals("O5*") || // O off of P in nuc (what is *)??
		this.getAtomType().equals("OE1") ||
		this.getAtomType().equals("OE2") ||
		this.getAtomType().equals("OD1") ||
		this.getAtomType().equals("OD2") ||
		this.getAtomType().equals("OG") ||
		this.getAtomType().equals("OG1") ||
		this.getAtomType().equals("OH") ||
		this.getAtomType().equals("OXT"))
	{
		this.setColor3f(new Color3f(0.3f, 0.3f, 1.0f));
		this.setRadius(1.52);
	}
	else if (
		this.getAtomType().equals("P")) // Phosphorus of nuc
	{
		this.setColor3f(new Color3f(1.0f, 1.0f, 1.0f));
		this.setRadius(unknownRadius); // if sulpher then 1.8
	}
	else if (
	this.getAtomType().equals("SD") ||
	this.getAtomType().equals("SG") // DON't know what SG is
	)
	{
		this.setColor3f(new Color3f(1.0f, 0.0f, 1.0f));
		this.setRadius(unknownRadius); // if sulpher then 1.8
	}
	else
	{
		debug("ATOM TYPE NOT FOUND: " + this.getAtomType());
		this.setColor3f(new Color3f(1.0f, 1.0f, 1.0f));
		this.setRadius(unknownRadius);
	}
}

public void
set(double x, double y, double z, double radius, Color3f color)
{
	this.setPos(x, y, z);
	this.setColor3f(color);
	this.setRadius(radius);
}

public void
set(double x, double y, double z, double radius)
{
	this.setPos(x, y, z);
	this.setRadius(radius);
}

public void
set(double x, double y, double z)
{
	this.setPos(x, y, z);
}

public void
set(double radius)
{
	this.setRadius(radius);
}

public void
setPos(double x, double y, double z)
{
	this.setOrigin(new BPoint3d(x, y, z));
}

public boolean
contains(double x, double y, double z)
{
	BPoint3d pt0 = new BPoint3d(x, y, z);
	BPoint3d pt1 = new BPoint3d(
		this.getX(), this.getY(), this.getZ());
	return(pt0.distance(pt1) <= this.getRadius());
}

private int serialNumber = 0;

public void
setSerialNumber(int serialNumber)
{
	this.serialNumber = serialNumber;
}

public int
getSerialNumber()
{
	return (this.serialNumber);
}

private String atomType = null;

public void
setAtomType(String atomType)
{
	this.atomType = atomType;
}

public String
getAtomType()
{
	return (this.atomType);
}

private int residueID = 0;

public void
setResidueID(int residueID)
{
	this.residueID = residueID;
}

public int
getResidueID()
{
	return (this.residueID);
}

private String chainID = null;

public void
setChainID(String chainID)
{
	this.chainID = chainID;
}

public String
getChainID()
{
	return (this.chainID);
}

private BPoint3d origin = null;

public void
setOrigin(BPoint3d origin)
{
	this.origin = origin;
	this.origin.get(originVals);
}

public BPoint3d
getOrigin()
{
	return (this.origin);
}

// NEED to sometime deprecate getOrigin
public BPoint3d
getCenterPt()
{
	return (this.origin);
}

public double
getX()
{
	return (originVals[0]);
}

public double
getY()
{
	return (originVals[1]);
}

public double
getZ()
{
	return (originVals[2]);
}

private double radius = 0.0;

public void
setRadius(double radius)
{
	this.radius = radius;
}

public double
getRadius()
{
	return (this.radius);
}

private Color3f color3f = null;

public void
setColor3f(float r, float g, float b)
{
	this.color3f = new Color3f(r, g, b);
	this.setColor(new Color(r, g, b));
}

public void
setColor3f(Color3f color3f)
{
	this.color3f = color3f;
	float[] vals = new float[3];
	color3f.get(vals);
	this.setColor(new Color(vals[0], vals[1], vals[2]));
	vals = null;
}

public Color3f
getColor3f()
{
	return (this.color3f);
}

private Color color = null;

public void
setColor(Color color)
{
	this.color = color;
}

public Color
getColor()
{
	return (this.color);
}

private boolean isTransparent = false;

public void
setIsTransparent(boolean isTransparent)
{
	this.isTransparent = isTransparent;
}

public boolean
getIsTransparent()
{
	return (this.isTransparent);
}

// NEED a lookup by Name in SphereInfoList
private String name = null;

public void
setName(String name)
{
	this.name = name;
}

public String
getName()
{
	return (this.name);
}


/************* MATH ***********************/

public boolean
intersects(SphereInfo s)
{
	return (this.getOrigin().distance(s.getOrigin()) <=
		(this.getRadius() + s.getRadius()));
}

public boolean
surfacesIntersect(SphereInfo s)
{
	if (!this.intersects(s))
		return (false);
	// check if sphere s is contained in this sphere
	if (this.getOrigin().distance(s.getOrigin()) +
		s.getRadius() <= this.getRadius())
		return (false);
	// check if this sphere is contained in sphere s
	if (s.getOrigin().distance(this.getOrigin()) +
		this.getRadius() <= s.getRadius())
		return (false);
	return (true);
}

public double
distance(SphereInfo s)
{
	return (this.getOrigin().distance(s.getOrigin()));
}

public void
transform(Matrix4d mat)
{
/*
debug("mat:\n");
debug("" + mat.m00 + " " + mat.m01 + " " + mat.m02 + " " + mat.m03);
debug("" + mat.m10 + " " + mat.m11 + " " + mat.m02 + " " + mat.m13);
debug("" + mat.m20 + " " + mat.m21 + " " + mat.m22 + " " + mat.m23);
debug("" + mat.m30 + " " + mat.m31 + " " + mat.m32 + " " + mat.m33);
*/

	// Point3d newPt = new Point3d(this.getX(), this.getY(), this.getZ());
	BVector4d vec = new BVector4d(this.getX(), this.getY(), this.getZ());
	// debug("setting: " + vec);
	mat.transform(vec);
	double newVals[] = new double[4]; 
	vec.get(newVals);
	this.setPos(newVals[0], newVals[1], newVals[2]);
}

// seems like a lot of repeats and misses huge areas. N is arbitrary
public static Vector
generateSphereVer0(BPoint3d centerPt, double radius, double inc)
{
	Vector ptList = new Vector();
	BVector3d pt = new BVector3d();
	double shiftX = centerPt.getX();
	double shiftY = centerPt.getY();
	double shiftZ = centerPt.getZ();
	for (double x = -1.0;x <= 1.0;x+=inc)
	for (double y = -1.0;y <= 1.0;y+=inc)
	for (double z = -1.0;z <= 1.0;z+=inc)
	{
		pt.set(x, y, z);
		if (pt.length() > 1.0)
			continue;
		pt.normalize();
		ptList.add(new BPoint3d(
			(pt.getX() * radius) + shiftX,
			(pt.getY() * radius) + shiftY,
			(pt.getZ() * radius) + shiftZ));
	}
	return (ptList);
}

// huge gaps, no repeats but higher density at polar ends;typical
// polar coords solution.
public static Vector
generateSphereVer1(BPoint3d centerPt, double radius, int N)
{
	Vector ptList = new Vector();
	BVector3d pt = new BVector3d();
	double shiftX = centerPt.getX();
	double shiftY = centerPt.getY();
	double shiftZ = centerPt.getZ();

	double theta = 0.0;
	double phi = 0.0;
	double lastPhi = 0.0;
	for (int i = 0;i < N;i++)
	{
		int kVal = i + 1;
		double h = -1.0 + 2.0*((double)(kVal-1))/(double)(N-1);
		theta = (double)Math.acos((double)h);
		if ((kVal == 1) || (kVal == N))
			phi = 0.0;
		else phi = lastPhi + 3.6/(double)Math.sqrt(((double)N)*(1.0-(h*h)));
		if (phi > (double)(2.0*Math.PI))
			phi -= (double)(2.0*Math.PI);
		if (phi < 0.0)
			phi += (double)(2.0*Math.PI);
		ptList.add(new BPoint3d(
			((double)(Math.cos(theta)*Math.sin(phi)) * radius) + shiftX,
			((double)(Math.sin(theta)*Math.sin(phi)) * radius) + shiftY,
			((double)(Math.cos(phi)) * radius) + shiftZ));
		lastPhi = phi;
	}

	return (ptList);
}

// move pts around until even. too slow and doesn't seem accurate.
public static Vector
generateSphereVer2(BPoint3d centerPt, double radius, int maxIterations, int N)
{
	Vector ptList = new Vector();
	BVector3d pt = new BVector3d();
	double shiftX = centerPt.getX();
	double shiftY = centerPt.getY();
	double shiftZ = centerPt.getZ();
	BVector3d pt1 = new BVector3d();
	BVector3d pt2 = new BVector3d();

	BVector3d[] ptArray = new BVector3d[N];

	/*
	// Create the initial random cloud //
	for (int i = 0;i < N;i++)
	{
		BVector3d randPt = new BVector3d();
		double x = (double)((Math.random() * 1000.0) - 500.0);
		double y = (double)((Math.random() * 1000.0) - 500.0);
		double z = (double)((Math.random() * 1000.0) - 500.0);
		randPt.set(x, y, z);
		randPt.normalize();
		// ptArray[i] = new BVector3d(randPt.getX(), randPt.getY(), randPt.getZ());
		ptArray[i] = randPt;
	}
	*/
	/* doesn't work
	BVector3d setPt = new BVector3d();
	for (double x = -1.0;x <= 1.0;x+=inc)
	for (double y = -1.0;y <= 1.0;y+=inc)
	for (double z = -1.0;z <= 1.0;z+=inc)
	{
		setPt.set(x, y, z);
		if (setPt.length() > 1.0)
			continue;
		setPt.normalize();
		ptArray[i] = new BVector3d(setPt.getX(), setPt.getY(), setPt.getZ());
	}
	*/

	for (int i = 0;i < N;i++)
		ptList.add(new BPoint3d());

	double fitnessFactor = 30.0;
	double fitness = Double.MAX_VALUE;

	while (fitness > fitnessFactor)
	{

	for (int i = 0;i < maxIterations; i++)
	{
		// Find the closest two points //
		int minPt1ID = 0;
		int minPt2ID = 1;

		double minDistance = ptArray[minPt1ID].distance(ptArray[minPt2ID]);
		double maxDistance = minDistance;

		for (int pt1ID = 0;pt1ID < N - 1;pt1ID++)
		{
			for (int pt2ID = pt1ID + 1;pt2ID < N;pt2ID++)
			{
				double dist = ptArray[pt1ID].distance(ptArray[pt2ID]);
				if (dist < minDistance)
				{
				   minDistance = dist;
				   minPt1ID = pt1ID;
				   minPt2ID = pt2ID;
				}
				if (dist > maxDistance)
				   maxDistance = dist;
			}
		}

		// Move the two minimal points apart, in this case by 1%
		// but should really vary this for refinement
		double mvPosDist = 1.01;
		double mvNegDist = 0.01;
		/*
		double mvPosDist = 2.02;
		double mvNegDist = 0.02;
		*/

		pt1.set(ptArray[minPt1ID]);
		pt2.set(ptArray[minPt2ID]);

		ptArray[minPt2ID].set(
			(double)(pt1.getX() + mvPosDist * (pt2.getX() - pt1.getX())),
			(double)(pt1.getY() + mvPosDist * (pt2.getY() - pt1.getY())),
			(double)(pt1.getZ() + mvPosDist * (pt2.getZ() - pt1.getZ())));
		ptArray[minPt2ID].normalize();

		ptArray[minPt1ID].set(
			(double)(pt1.getX() - mvNegDist * (pt2.getX() - pt1.getX())),
			(double)(pt1.getY() - mvNegDist * (pt2.getY() - pt1.getY())),
			(double)(pt1.getZ() - mvNegDist * (pt2.getZ() - pt1.getZ())));
		ptArray[minPt1ID].normalize();
	}

	for (int i = 0;i < N;i++)
	{
		BVector3d tmpVect = ptArray[i];
		BPoint3d tmpPt = (BPoint3d)ptList.elementAt(i);
		tmpPt.set(
			(tmpVect.getX() * radius) + shiftX,
			(tmpVect.getY() * radius) + shiftY,
			(tmpVect.getZ() * radius) + shiftZ);
	}

	double minClosestDist = Double.MAX_VALUE;
	double maxClosestDist = -Double.MAX_VALUE;
	for (int i = 0;i < N;i++)
	{
		double closestDist = Double.MAX_VALUE;
		BPoint3d cmpPt = (BPoint3d)ptList.elementAt(i);
		for (int j = 0;j < N;j++)
		{
			if (i == j)
				continue;
			BPoint3d tmpPt = (BPoint3d)ptList.elementAt(j);
			double dist = (double)cmpPt.distance(tmpPt);
			if (dist < closestDist)
				closestDist = dist;
		}
		// debug("closestDist: " + closestDist);
		if (closestDist < minClosestDist)
			minClosestDist = closestDist;
		if (closestDist > maxClosestDist)
			maxClosestDist = closestDist;
	}
	/*
	debug("closestDist: " + minClosestDist);
	debug("farthestDist: " + maxClosestDist);
	debug("FIT: " + (maxClosestDist - minClosestDist));
	*/
	fitness = maxClosestDist - minClosestDist;
	maxIterations = 50;
	debug("FIT: " + fitness);

	}

	return (ptList);
}

/*
public static BVector3d[] spherePoints =
{
	new BVector3d(0.36, 0.79, 0.49),
	new BVector3d(0.58, 0.79, -0.19),
	new BVector3d(0.00, 0.79, -0.61)
};

// Point[] points = { circle1.getCenterPoint(), circle2.getCenterPoint() };

public static Point2D[] testF =
{
new Point2D.Double(1.0, 2.0),
new Point2D.Double(4.0, 3.0)
};
*/

/* THESE are original and aren't neccesarily length 1.0. Probably the
** correct pt got rounded off.
public static BVector3d[] spherePoints =
{
	new BVector3d(0.36d, 0.79d, 0.49d),
	new BVector3d(0.58d, 0.79d, -0.19d),
	new BVector3d(-0.00d, 0.79d, -0.61d),
	new BVector3d(-0.58d, 0.79d, -0.19d),
	new BVector3d(-0.36d, 0.79d, 0.49d),
	new BVector3d(0.58d, 0.19d, 0.79d),
	new BVector3d(0.93d, 0.19d, -0.30d),
	new BVector3d(-0.00d, 0.19d, -0.98d),
	new BVector3d(-0.93d, 0.19d, -0.30d),
	new BVector3d(-0.58d, 0.19d, 0.79d),
	new BVector3d(-0.00d, 1.00d, 0.00d),
	new BVector3d(0.34d, 0.93d, 0.11d),
	new BVector3d(0.21d, 0.93d, -0.29d),
	new BVector3d(-0.21d, 0.93d, -0.29d),
	new BVector3d(-0.34d, 0.93d, 0.11d),
	new BVector3d(-0.00d, 0.93d, 0.35d),
	new BVector3d(0.85d, 0.45d, 0.28d),
	new BVector3d(0.64d, 0.52d, 0.56d),
	new BVector3d(0.85d, 0.16d, 0.50d),
	new BVector3d(0.98d, 0.16d, 0.10d),
	new BVector3d(0.85d, 0.52d, -0.08d),
	new BVector3d(0.64d, 0.74d, 0.21d),
	new BVector3d(0.53d, 0.45d, -0.72d),
	new BVector3d(0.74d, 0.52d, -0.44d),
	new BVector3d(0.74d, 0.16d, -0.66d),
	new BVector3d(0.40d, 0.16d, -0.90d),
	new BVector3d(0.19d, 0.52d, -0.84d),
	new BVector3d(0.40d, 0.74d, -0.55d),
	new BVector3d(-0.53d, 0.45d, -0.72d),
	new BVector3d(-0.19d, 0.52d, -0.84d),
	new BVector3d(-0.40d, 0.16d, -0.90d),
	new BVector3d(-0.74d, 0.16d, -0.66d),
	new BVector3d(-0.74d, 0.52d, -0.44d),
	new BVector3d(-0.40d, 0.74d, -0.55d),
	new BVector3d(-0.85d, 0.45d, 0.28d),
	new BVector3d(-0.85d, 0.52d, -0.08d),
	new BVector3d(-0.98d, 0.16d, 0.10d),
	new BVector3d(-0.85d, 0.16d, 0.50d),
	new BVector3d(-0.64d, 0.52d, 0.56d),
	new BVector3d(-0.64d, 0.74d, 0.21d),
	new BVector3d(-0.00d, 0.45d, 0.89d),
	new BVector3d(-0.34d, 0.52d, 0.79d),
	new BVector3d(-0.21d, 0.16d, 0.96d),
	new BVector3d(0.21d, 0.16d, 0.96d),
	new BVector3d(0.34d, 0.52d, 0.79d),
	new BVector3d(-0.00d, 0.74d, 0.68d),
	new BVector3d(-0.00d, -0.74d, -0.68d),
	new BVector3d(-0.34d, -0.52d, -0.79d),
	new BVector3d(-0.21d, -0.16d, -0.96d),
	new BVector3d(0.21d, -0.16d, -0.96d),
	new BVector3d(0.34d, -0.52d, -0.79d),
	new BVector3d(-0.00d, -0.45d, -0.89d),
	new BVector3d(0.64d, -0.74d, -0.21d),
	new BVector3d(0.64d, -0.52d, -0.56d),
	new BVector3d(0.85d, -0.16d, -0.50d),
	new BVector3d(0.98d, -0.16d, -0.10d),
	new BVector3d(0.85d, -0.52d, 0.08d),
	new BVector3d(0.85d, -0.45d, -0.28d),
	new BVector3d(0.40d, -0.74d, 0.55d),
	new BVector3d(0.74d, -0.52d, 0.44d),
	new BVector3d(0.74d, -0.16d, 0.66d),
	new BVector3d(0.40d, -0.16d, 0.90d),
	new BVector3d(0.19d, -0.52d, 0.84d),
	new BVector3d(0.53d, -0.45d, 0.72d),
	new BVector3d(-0.40d, -0.74d, 0.55d),
	new BVector3d(-0.19d, -0.52d, 0.84d),
	new BVector3d(-0.40d, -0.16d, 0.90d),
	new BVector3d(-0.74d, -0.16d, 0.66d),
	new BVector3d(-0.74d, -0.52d, 0.44d),
	new BVector3d(-0.53d, -0.45d, 0.72d),
	new BVector3d(-0.64d, -0.74d, -0.21d),
	new BVector3d(-0.85d, -0.52d, 0.08d),
	new BVector3d(-0.98d, -0.16d, -0.10d),
	new BVector3d(-0.85d, -0.16d, -0.50d),
	new BVector3d(-0.64d, -0.52d, -0.56d),
	new BVector3d(-0.85d, -0.45d, -0.28d),
	new BVector3d(-0.00d, -0.93d, -0.35d),
	new BVector3d(0.34d, -0.93d, -0.11d),
	new BVector3d(0.21d, -0.93d, 0.29d),
	new BVector3d(-0.21d, -0.93d, 0.29d),
	new BVector3d(-0.34d, -0.93d, -0.11d),
	new BVector3d(-0.00d, -1.00d, 0.00d),
	new BVector3d(0.58d, -0.19d, -0.79d),
	new BVector3d(0.93d, -0.19d, 0.30d),
	new BVector3d(-0.00d, -0.19d, 0.98d),
	new BVector3d(-0.93d, -0.19d, 0.30d),
	new BVector3d(-0.58d, -0.19d, -0.79d),
	new BVector3d(0.36d, -0.79d, -0.49d),
	new BVector3d(0.58d, -0.79d, 0.19d),
	new BVector3d(-0.00d, -0.79d, 0.61d),
	new BVector3d(-0.58d, -0.79d, 0.19d),
	new BVector3d(-0.36d, -0.79d, -0.49d)
};
*/

// these pt are taken from above, but normalized. might be something
// lost in this process (like distance between pts on sphere not being
// optimal).
public static BVector3d[] spherePoints =
{
new BVector3d(0.3611212, 0.79246044, 0.4915261),
new BVector3d(0.5809885, 0.79134643, -0.19032381),
new BVector3d(0.0, 0.79150534, -0.6111623),
new BVector3d(-0.5809885, 0.79134643, -0.19032381),
new BVector3d(-0.3611212, 0.79246044, 0.4915261),
new BVector3d(0.5809885, 0.19032381, 0.79134643),
new BVector3d(0.9342134, 0.19086081, -0.30135918),
new BVector3d(0.0, 0.19033338, -0.98171955),
new BVector3d(-0.9342134, 0.19086081, -0.30135918),
new BVector3d(-0.5809885, 0.19032381, 0.79134643),
new BVector3d(0.0, 1.0, 0.0),
new BVector3d(0.34126502, 0.9334602, 0.11040927),
new BVector3d(0.21072826, 0.93322515, -0.2910057),
new BVector3d(-0.21072826, 0.93322515, -0.2910057),
new BVector3d(-0.34126502, 0.9334602, 0.11040927),
new BVector3d(0.0, 0.935915, 0.35222605),
new BVector3d(0.8485587, 0.44923696, 0.27952522),
new BVector3d(0.64205784, 0.521672, 0.56180066),
new BVector3d(0.8508086, 0.1601522, 0.50047565),
new BVector3d(0.98196584, 0.16032095, 0.10020059),
new BVector3d(0.85029775, 0.52018213, -0.08002802),
new BVector3d(0.63958436, 0.7395195, 0.20986362),
new BVector3d(0.5295236, 0.44959554, -0.7193529),
new BVector3d(0.73574495, 0.51701, -0.43746996),
new BVector3d(0.7367653, 0.15930061, -0.65711504),
new BVector3d(0.40088293, 0.16035315, -0.90198654),
new BVector3d(0.18886083, 0.51688224, -0.8349636),
new BVector3d(0.39799514, 0.736291, -0.5472433),
new BVector3d(-0.5295236, 0.44959554, -0.7193529),
new BVector3d(-0.18886083, 0.51688224, -0.8349636),
new BVector3d(-0.40088293, 0.16035315, -0.90198654),
new BVector3d(-0.7367653, 0.15930061, -0.65711504),
new BVector3d(-0.73574495, 0.51701, -0.43746996),
new BVector3d(-0.39799514, 0.736291, -0.5472433),
new BVector3d(-0.8485587, 0.44923696, 0.27952522),
new BVector3d(-0.85029775, 0.52018213, -0.08002802),
new BVector3d(-0.98196584, 0.16032095, 0.10020059),
new BVector3d(-0.8508086, 0.1601522, 0.50047565),
new BVector3d(-0.64205784, 0.521672, 0.56180066),
new BVector3d(-0.63958436, 0.7395195, 0.20986362),
new BVector3d(0.0, 0.45121998, 0.89241284),
new BVector3d(-0.33829588, 0.5173937, 0.7860405),
new BVector3d(-0.21091948, 0.16070056, 0.9642034),
new BVector3d(0.21091948, 0.16070056, 0.9642034),
new BVector3d(0.33829588, 0.5173937, 0.7860405),
new BVector3d(0.0, 0.7363275, 0.6766253),
new BVector3d(0.0, -0.7363275, -0.6766253),
new BVector3d(-0.33829588, -0.5173937, -0.7860405),
new BVector3d(-0.21091948, -0.16070056, -0.9642034),
new BVector3d(0.21091948, -0.16070056, -0.9642034),
new BVector3d(0.33829588, -0.5173937, -0.7860405),
new BVector3d(0.0, -0.45121998, -0.89241284),
new BVector3d(0.63958436, -0.7395195, -0.20986362),
new BVector3d(0.64205784, -0.521672, -0.56180066),
new BVector3d(0.8508086, -0.1601522, -0.50047565),
new BVector3d(0.98196584, -0.16032095, -0.10020059),
new BVector3d(0.85029775, -0.52018213, 0.08002802),
new BVector3d(0.8485587, -0.44923696, -0.27952522),
new BVector3d(0.39799514, -0.736291, 0.5472433),
new BVector3d(0.73574495, -0.51701, 0.43746996),
new BVector3d(0.7367653, -0.15930061, 0.65711504),
new BVector3d(0.40088293, -0.16035315, 0.90198654),
new BVector3d(0.18886083, -0.51688224, 0.8349636),
new BVector3d(0.5295236, -0.44959554, 0.7193529),
new BVector3d(-0.39799514, -0.736291, 0.5472433),
new BVector3d(-0.18886083, -0.51688224, 0.8349636),
new BVector3d(-0.40088293, -0.16035315, 0.90198654),
new BVector3d(-0.7367653, -0.15930061, 0.65711504),
new BVector3d(-0.73574495, -0.51701, 0.43746996),
new BVector3d(-0.5295236, -0.44959554, 0.7193529),
new BVector3d(-0.63958436, -0.7395195, -0.20986362),
new BVector3d(-0.85029775, -0.52018213, 0.08002802),
new BVector3d(-0.98196584, -0.16032095, -0.10020059),
new BVector3d(-0.8508086, -0.1601522, -0.50047565),
new BVector3d(-0.64205784, -0.521672, -0.56180066),
new BVector3d(-0.8485587, -0.44923696, -0.27952522),
new BVector3d(0.0, -0.935915, -0.35222605),
new BVector3d(0.34126502, -0.9334602, -0.11040927),
new BVector3d(0.21072826, -0.93322515, 0.2910057),
new BVector3d(-0.21072826, -0.93322515, 0.2910057),
new BVector3d(-0.34126502, -0.9334602, -0.11040927),
new BVector3d(0.0, -1.0, 0.0),
new BVector3d(0.5809885, -0.19032381, -0.79134643),
new BVector3d(0.9342134, -0.19086081, 0.30135918),
new BVector3d(0.0, -0.19033338, 0.98171955),
new BVector3d(-0.9342134, -0.19086081, 0.30135918),
new BVector3d(-0.5809885, -0.19032381, -0.79134643),
new BVector3d(0.3611212, -0.79246044, -0.4915261),
new BVector3d(0.5809885, -0.79134643, 0.19032381),
new BVector3d(0.0, -0.79150534, 0.6111623),
new BVector3d(-0.5809885, -0.79134643, 0.19032381),
new BVector3d(-0.3611212, -0.79246044, -0.4915261)
};

public static Vector
generateSphereVer3(BPoint3d centerPt, double radius, Vector lineLists)
{
	Vector ptList = new Vector();
	BVector3d pt = null;
	double shiftX = centerPt.getX();
	double shiftY = centerPt.getY();
	double shiftZ = centerPt.getZ();
	int N = spherePoints.length;

	for (int i = 0;i < N;i++)
	{
		pt = spherePoints[i];
		ptList.add(new BVector3d(
			(pt.getX() * radius) + shiftX,
			(pt.getY() * radius) + shiftY,
			(pt.getZ() * radius) + shiftZ));
	}

	/* ATTEMPT at adding more pts to sphere:
	int cmpID = 10;
	// BVector3d cmpPt = spherePoints[cmpID];
	BPoint3d cmpPt = (BPoint3d)ptList.elementAt(cmpID);
	BPoint3d redPt = null;
	double minDist = Double.MAX_VALUE;
	for (int i = 0;i < N;i++)
	{
		if (i == cmpID)
			continue;
		//
		// pt = spherePoints[i];
		// double dist = pt.distance(cmpPt);
		//
		redPt = (BPoint3d)ptList.elementAt(i);
		double dist = refPt.distance(cmpPt);
		if (dist < minDist)
			minDist = dist;
	}
	// debug("minDist: " + minDist);
	double tolerance = minDist/2.0;

	Vector lineList = new Vector();
	for (int i = 0;i < N;i++)
	{
		if (i == cmpID)
			continue;
		// pt = spherePoints[i];
		redPt = (BPoint3d)ptList.elementAt(i);
		// double dist = pt.distance(cmpPt);
		double dist = refPt.distance(cmpPt);
		if ((dist >= minDist - tolerance) && (dist <= minDist + tolerance))
		{
			// debug("found match at: " + i + " : " + (dist));
			// debug(refPt.getX() + " " + refPt.getY() + " " + refPt.getZ());
			lineList.add(cmpPt);
			lineList.add(refPt);
		}
	}
	if (lineLists != null)
		lineLists.add(lineList);
	*/

	return (ptList);
}

// original sphere generator seems very poor. probably many repeats.
// big gaps. try to optomize with one of the ones up above.
public static Vector
generateSphereVer4(BPoint3d centerPt, double radius, double cubeInc,
	double sphereRadiusTolerance)
{
	Vector ptList = new Vector();
	BVector3d pt = new BVector3d();
	double shiftX = centerPt.getX();
	double shiftY = centerPt.getY();
	double shiftZ = centerPt.getZ();

	for (double x = -radius;x <= radius;x += cubeInc)
	for (double y = -radius;y <= radius;y += cubeInc)
	for (double z = -radius;z <= radius;z += cubeInc)
	{
		// 1) determine pts on surface
		if (Math.abs(Math.sqrt(x*x + y*y + z*z) - radius) > sphereRadiusTolerance)
			continue;
		// found a surface point
		// MAYBE: now move it closer to surface
		ptList.add(new BPoint3d(x + shiftX, y + shiftY, z + shiftZ));
	}

	return (ptList);
}

/****************** END MATH *****************************/

public void
printXML(Object outFile)
throws Exception
{
	PrintWriter out = (PrintWriter)outFile;
	String tabSpaces = "    ";

	out.print("<Sphere ");
	out.print("X='" + this.getX() + "' ");
	out.print("Y='" + this.getY() + "' ");
	out.print("Z='" + this.getZ() + "' ");
	out.print("Radius='" + this.getRadius() + "' ");
	if (this.getIsTransparent())
		out.print("IsTransparent='true' ");
	out.print("Color='" +
		Integer.toHexString(this.getColor().getRGB() & 0x00ffffff) +
		"' ");
	out.println("/>");
}

public BVector4f
getBVector4f()
{
	return (new BVector4f((float)this.getX(), (float)this.getY(), (float)this.getZ(), 1.0f));
}

public BVector4d
getBVector4d()
{
	return (new BVector4d(this.getX(), this.getY(), this.getZ(), 1.0));
}

public BVector3d
getBVector3d()
{
	return (new BVector3d(this.getX(), this.getY(), this.getZ()));
}

public String
toString()
{
	/*
	return("serial#,type,ID,x,y,z,rad,color: " +
		this.getSerialNumber() + " " +
		this.getAtomType() + " " +
		this.getResidueID() + " " +
		this.getX() + " " +
		this.getY() + " " +
		this.getZ() + " " +
		this.getRadius() + " " +
		this.getColor3f().toString());
	*/
	return("" +
		this.getX() + " " +
		this.getY() + " " +
		this.getZ() + " " +
		this.getRadius());
}

public static void
debug(String s)
{
	System.out.println("SphereInfo-> " + s);
}

}
