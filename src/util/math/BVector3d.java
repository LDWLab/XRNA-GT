package util.math;

import java.awt.geom.*;

import javax.vecmath.*;

// USE for positional vector only

public class
BVector3d
extends Vector3d
{

public
BVector3d()
{
	super();
}

public
BVector3d(double x, double y, double z)
{
	super(x, y, z);
}

public
BVector3d(BVector3d vec)
{
	super((Vector3d)vec);
}

double[] vals = new double[3];

public void
copy(BVector3d fromPt)
{
	fromPt.get(vals);
	this.set(vals);
}

public void
set(BVector3d fromPt)
{
	fromPt.get(vals);
	this.set(vals);
}

public double
distance(BVector3d vec)
{
	return (this.getBPoint3d().distance(vec.getBPoint3d()));
}

public double
distance(BPoint3d pt)
{
	return (this.getBPoint3d().distance(pt));
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
translate(double x, double y, double z)
{
	this.set(this.getX() + x, this.getY() + y, this.getZ() + z);
}

public void
translate(BPoint3d transPt)
{
	this.translate(transPt.getX(), transPt.getY(), transPt.getZ());
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

private static void
debug(String s)
{
	System.out.println("BVector3d-> " + s);
}


}
