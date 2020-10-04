package util.math;

import java.awt.geom.*;

import javax.vecmath.*;

public class
BPoint3d
extends Point3d
{

public
BPoint3d()
{
	super();
}

public
BPoint3d(double x, double y, double z)
{
	super(x, y, z);
}

public
BPoint3d(BPoint3d vec)
{
	super((Point3d)vec);
}

public
BPoint3d(BVector3d vec)
{
	super(vec.getX(), vec.getY(), vec.getZ());
}

double[] vals = new double[3];

public void
copy(BPoint3d fromPt)
{
	fromPt.get(vals);
	this.set(vals);
}

public void
translate(double x, double y, double z)
{
	this.set(this.getX() + x, this.getY() + y, this.getZ() + z);
}

public void
translate(BPoint3d transPt)
{
	this.translate(transPt.getX(), transPt.getY(), transPt.getZ());
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
	this.set(newVals[0], newVals[1], newVals[2]);
}

public static BPoint3d
getMidPt(BPoint3d pt0, BPoint3d pt1)
{
	return (new BPoint3d(
		(pt0.getX() + pt1.getX())/2.0,
		(pt0.getY() + pt1.getY())/2.0,
		(pt0.getZ() + pt1.getZ())/2.0));
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

public void
setLocation(double x, double y, double z)
{
	this.setX(x);
	this.setY(y);
	this.setZ(z);
}

public void
setLocation(BPoint3d pt)
{
	this.setLocation(pt.getX(), pt.getY(), pt.getZ());
}

public double
getY()
{
	this.get(vals);
	return (vals[1]);
}

public void
setZ(double z)
{
	this.get(vals);
	vals[2] = z;
	this.set(vals);
}

public double
getZ()
{
	this.get(vals);
	return (vals[2]);
}

/*
** 2 position vectors, u,v, are equal if u.x==v.x  && u.y==v.y && u.z==v.z
*/

public boolean
equals(double x, double y, double z)
{
	return(
		(Math.round(MathDefines.PRECISION * this.getX()) ==
			Math.round(MathDefines.PRECISION * x)) &&
		(Math.round(MathDefines.PRECISION * this.getY()) ==
			Math.round(MathDefines.PRECISION * y)) &&
		(Math.round(MathDefines.PRECISION * this.getZ()) ==
			Math.round(MathDefines.PRECISION * z)));
}

public boolean
equals(BPoint3d testPt)
{
	return (this.equals(testPt.getX(), testPt.getY(), testPt.getZ()));
}

public Vector3d
getVector3d()
{
	return (new Vector3d(this.getX(), this.getY(), this.getZ()));
}

public BVector3d
getBVector3d()
{
	return (new BVector3d(this.getX(), this.getY(), this.getZ()));
}


public double
distance(BVector3d vec)
{
	return (this.distance(vec.getBPoint3d()));
}

private static void
debug(String s)
{
	System.out.println("BPoint3d-> " + s);
}


}
