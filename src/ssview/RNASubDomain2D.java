package ssview;

import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;
import java.io.*;

import jimage.DrawObjectCollection;

import util.math.*;

public class
RNASubDomain2D
extends RNASubDomain
{

RNABasePair2D refBasePair = null;

public
RNASubDomain2D()
throws Exception
{
	super();
	refBasePair = new RNABasePair2D();
}

public
RNASubDomain2D(NucNode nuc)
throws Exception
{
	refBasePair = new RNABasePair2D();
	this.set((Nuc2D)nuc);
}

public
RNASubDomain2D(RNAHelix helix)
throws Exception
{
	super(helix);
	this.set((Nuc2D)helix.getFivePrimeStartNuc());
}

public void
set(Nuc2D nuc)
throws Exception
{
	super.set((NucNode)nuc);
	if (this.getStartHelix2D() == null)
		this.setStartHelix2D(new RNAHelix2D((Nuc2D)this.getStartHelix().getFivePrimeStartNuc()));
	else
		this.getStartHelix2D().set((Nuc2D)this.getStartHelix().getFivePrimeStartNuc());
	this.setDistancesFromCollection(nuc.getParentSSData2D());
}

// the 5' most helix
private RNAHelix2D startHelix = null;

public void
setStartHelix2D(RNAHelix2D startHelix)
{
    this.startHelix = startHelix;
}

public RNAHelix2D
getStartHelix2D()
{
    return (this.startHelix);
}

public Point2D
getFivePrimeMidPt()
throws Exception
{
	return (getStartHelix2D().getFivePrimeMidPt());
}

public Nuc2D
getFivePrimeNuc2D()
{
    return ((Nuc2D)this.getFivePrimeNuc());
}

public Nuc2D
getThreePrimeNuc2D()
{
    return ((Nuc2D)this.getThreePrimeNuc());
}

public void
setIsSchematic(boolean isSchematic)
throws Exception
{
	SSData2D sstr = (SSData2D)this.getParentCollection();
	for (int nucID = this.getFivePrimeNuc2D().getID();
		nucID <= this.getThreePrimeNuc2D().getID();
		nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		if (nuc == null)
			continue;
		nuc.setIsSchematic(isSchematic);
	}
}

// direction is false to the 5' direction, true to the 3' direction
public void
shiftDistance(double distance, boolean direction)
throws Exception
{
	BLine2D ray = this.getStartHelixAxis();
	Point2D midPt = this.getFivePrimeMidPt();
	Point2D newPt = null;
	if (direction)
		newPt = ray.ptAtDistance(distance);
	else
		newPt = ray.ptAtDistance(-distance);
	double x = midPt.getX() - newPt.getX();
	double y = midPt.getY() - newPt.getY();

	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			refNuc.shiftXY(x, y);
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

public double
getAngle()
throws Exception
{
	return (this.getStartHelixAngle());
}

public double
getStartHelixAngle()
throws Exception
{
	return (this.getStartHelix2D().getAngle());
}

public BLine2D
getStartHelixAxis()
throws Exception
{
	return (this.getStartHelix2D().getHelixAxis());
}

public Point2D
getStartFivePrimeMidPt()
throws Exception
{
	return (this.getStartHelix2D().getFivePrimeMidPt());
}

/*************** DrawObject Implementation *******************/

public void
setX(double x)
throws Exception
{
	this.shiftX(this.getX() - x);
}

public void
setY(double y)
throws Exception
{
	this.shiftY(this.getY() - y);
}

public double
getX()
throws Exception
{
	return (getFivePrimeMidPt().getX());	
}

public double
getY()
throws Exception
{
	return (getFivePrimeMidPt().getY());	
}

private static void
debug(String s)
{
	System.err.println("RNASubDomain2D-> " + s);
}
	
}
