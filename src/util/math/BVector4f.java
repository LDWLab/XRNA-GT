package util.math;

import java.awt.geom.*;

import javax.vecmath.*;

// USE for positional vector only

public class
BVector4f
extends Vector4f
{

public
BVector4f()
{
	super();
}

public
BVector4f(float x, float y, float z, float w)
{
	super(x, y, z, w);
}

public
BVector4f(float x, float y, float z)
{
	super(x, y, z, 1.0f);
}

public
BVector4f(BVector4f vec)
{
	super((Vector4f)vec);
}

public
BVector4f(BVector3f vec)
{
	this(vec.getX(), vec.getY(), vec.getZ());
}

public
BVector4f(BPoint3f pt)
{
	this(pt.getX(), pt.getY(), pt.getZ());
}

float[] vals = new float[4];

public void
copy(BVector4f fromPt)
{
	fromPt.get(vals);
	this.set(vals);
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
setW(float w)
{
	this.get(vals);
	vals[3] = w;
	this.set(vals);
}

public float
getW()
{
	this.get(vals);
	return (vals[3]);
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

private static void
debug(String s)
{
	System.out.println("BVector4f-> " + s);
}


}
