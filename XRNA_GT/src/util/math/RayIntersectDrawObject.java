package util.math;

import java.util.*;

import jimage.DrawObject;

public abstract class
RayIntersectDrawObject
{

public
RayIntersectDrawObject()
{
	this.setIntersectPtList(new Vector());
	this.setUList(new Vector());
}

public
RayIntersectDrawObject(DrawObject drwObj)
{
	this();
	this.setDrawObject(drwObj);
}

public
RayIntersectDrawObject(BLine2D ray)
{
	this();
	this.setRay(ray);
}

public
RayIntersectDrawObject(BLine2D ray, DrawObject drwObj)
{
	this();
	this.setDrawObject(drwObj);
	this.setRay(ray);
}

private DrawObject drawObject = null;

public void
setDrawObject(DrawObject drawObject)
{
    this.drawObject = drawObject;
}

public DrawObject
getDrawObject()
{
    return (this.drawObject);
}

private BLine2D ray = null;

public void
setRay(BLine2D ray)
{
    this.ray = ray;
}

public BLine2D
getRay()
{
    return (this.ray);
}

private boolean rayIntersects = false;

public void
setRayIntersects(boolean rayIntersects)
{
    this.rayIntersects = rayIntersects;
}

public boolean
getRayIntersects()
{
    return (this.rayIntersects);
}

public abstract void runRayDrawObjectIntersect()
throws Exception;

private Vector intersectPtList = null;

public void
setIntersectPtList(Vector intersectPtList)
{
    this.intersectPtList = intersectPtList;
}

public Vector
getIntersectPtList()
{
    return (this.intersectPtList);
}

private Vector uList = null;

public void
setUList(Vector uList)
{
    this.uList = uList;
}

public Vector
getUList()
{
    return (this.uList);
}

private static void
debug(String s)
{
	System.out.println("RayIntersectDrawObject-> " + s);
}

}
