package util.math;

import java.awt.geom.*;

import javax.vecmath.*;

public class
BPoint3f
extends Point3f
{

public
BPoint3f()
{
	super();
}

public
BPoint3f(float x, float y, float z)
{
	super(x, y, z);
}

public
BPoint3f(BPoint3f vec)
{
	super((Point3f)vec);
}

public
BPoint3f(BVector3f vec)
{
	super(vec.getX(), vec.getY(), vec.getZ());
}

float[] vals = new float[3];

public void
copy(BPoint3f fromPt)
{
	fromPt.get(vals);
	this.set(vals);
}

public void
translate(float x, float y, float z)
{
	this.set(this.getX() + x, this.getY() + y, this.getZ() + z);
}

public void
translate(BPoint3f transPt)
{
	this.translate(transPt.getX(), transPt.getY(), transPt.getZ());
}

public void
transform(Matrix4f mat)
{
/*
debug("mat:\n");
debug("" + mat.m00 + " " + mat.m01 + " " + mat.m02 + " " + mat.m03);
debug("" + mat.m10 + " " + mat.m11 + " " + mat.m02 + " " + mat.m13);
debug("" + mat.m20 + " " + mat.m21 + " " + mat.m22 + " " + mat.m23);
debug("" + mat.m30 + " " + mat.m31 + " " + mat.m32 + " " + mat.m33);
*/

	// Point3f newPt = new Point3f(this.getX(), this.getY(), this.getZ());
	BVector4f vec = new BVector4f(this.getX(), this.getY(), this.getZ());
	// debug("setting: " + vec);
	mat.transform(vec);
	float newVals[] = new float[4]; 
	vec.get(newVals);
	this.set(newVals[0], newVals[1], newVals[2]);
}

public static BPoint3f
getMidPt(BPoint3f pt0, BPoint3f pt1)
{
	return (new BPoint3f(
		(pt0.getX() + pt1.getX())/2.0f,
		(pt0.getY() + pt1.getY())/2.0f,
		(pt0.getZ() + pt1.getZ())/2.0f));
}

public void
setX(float x)
{
	this.get(vals);
	vals[0] = x;
	this.set(vals);
}

public float
getX()
{
	this.get(vals);
	return (vals[0]);
}

public void
setY(float y)
{
	this.get(vals);
	vals[1] = y;
	this.set(vals);
}

public float
getY()
{
	this.get(vals);
	return (vals[1]);
}

public void
setZ(float z)
{
	this.get(vals);
	vals[2] = z;
	this.set(vals);
}

public float
getZ()
{
	this.get(vals);
	return (vals[2]);
}

/*
** 2 position vectors, u,v, are equal if u.x==v.x  && u.y==v.y && u.z==v.z
*/

public boolean
equals(float x, float y, float z)
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
equals(BPoint3f testPt)
{
	return (this.equals(testPt.getX(), testPt.getY(), testPt.getZ()));
}

public Vector3f
getVector3f()
{
	return (new Vector3f(this.getX(), this.getY(), this.getZ()));
}

public BVector3f
getBVector3f()
{
	return (new BVector3f(this.getX(), this.getY(), this.getZ()));
}


public float
distance(BVector3f vec)
{
	return (this.distance(vec.getBPoint3f()));
}

private static void
debug(String s)
{
	System.out.println("BPoint3f-> " + s);
}


}
