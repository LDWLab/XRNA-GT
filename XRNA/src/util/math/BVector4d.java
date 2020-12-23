package util.math;

import java.awt.geom.*;

import javax.vecmath.*;

// USE for positional vector only

public class
BVector4d
extends Vector4d
{

public
BVector4d()
{
	super();
}

public
BVector4d(double x, double y, double z, double w)
{
	super(x, y, z, w);
}

public
BVector4d(double x, double y, double z)
{
	super(x, y, z, 1.0);
}

public
BVector4d(BVector4d vec)
{
	super((Vector4d)vec);
}

public
BVector4d(BVector3d vec)
{
	this(vec.getX(), vec.getY(), vec.getZ());
}

public
BVector4d(BPoint3d pt)
{
	this(pt.getX(), pt.getY(), pt.getZ());
}

double[] vals = new double[4];

public void
copy(BVector4d fromPt)
{
	fromPt.get(vals);
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

public void
setW(double w)
{
	this.get(vals);
	vals[3] = w;
	this.set(vals);
}

public double
getW()
{
	this.get(vals);
	return (vals[3]);
}

public Point3d
getPoint3d()
{
	return (new Point3d(this.getX(), this.getY(), this.getZ()));
}

public BPoint3d
getBPoint3d()
{
	return (new BPoint3d(this.getX(), this.getY(), this.getZ()));
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
distance(BVector4d vec)
{
	return (this.getBPoint3d().distance(vec.getBPoint3d()));
}

// use instead of length() as my usage of putting a 1.0 in W spot screws
// up length
public double
length3D()
{
	return (this.getBVector3d().length());
}

private static void
debug(String s)
{
	System.out.println("BVector4d-> " + s);
}


}
