package util.math;

import java.awt.geom.*;

import javax.vecmath.*;

// USE for positional vector only

public class
BVector3f
extends Vector3f
{

public
BVector3f()
{
	super();
}

public
BVector3f(float x, float y, float z)
{
	super(x, y, z);
}

public
BVector3f(BVector3f vec)
{
	super((Vector3f)vec);
}

float[] vals = new float[3];

public void
copy(BVector3f fromPt)
{
	fromPt.get(vals);
	this.set(vals);
}

public void
set(BVector3f fromPt)
{
	fromPt.get(vals);
	this.set(vals);
}

public float
distance(BVector3f vec)
{
	return (this.getBPoint3f().distance(vec.getBPoint3f()));
}

public float
distance(BPoint3f pt)
{
	return (this.getBPoint3f().distance(pt));
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

public Point3f
getPoint3f()
{
	return (new Point3f(this.getX(), this.getY(), this.getZ()));
}

public BPoint3f
getBPoint3f()
{
	return (new BPoint3f(this.getX(), this.getY(), this.getZ()));
}

private static void
debug(String s)
{
	System.out.println("BVector3f-> " + s);
}


}
