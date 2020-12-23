package ssview;

import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;
import java.io.*;

import jimage.DrawObjectCollection;

import util.math.*;

public class
RNAStackedHelix2D
extends RNAStackedHelix
{

RNABasePair2D refBasePair = null;

public
RNAStackedHelix2D()
throws Exception
{
	refBasePair = new RNABasePair2D();
}

public
RNAStackedHelix2D(NucNode nuc)
throws Exception
{
	refBasePair = new RNABasePair2D();
	this.set((Nuc2D)nuc);
}

public
RNAStackedHelix2D(SSData sstr, int nucID)
throws Exception
{
	this();
	this.set(((SSData2D)sstr).getNuc2DAt(nucID));
}

public void
set(Nuc2D nuc)
throws Exception
{
	super.set((NucNode)nuc);

	if (nuc.isBasePair())
		this.setRefHelix2D(new RNAHelix2D(nuc));

	if (this.getStartHelix2D() == null)
		this.setStartHelix2D(new RNAHelix2D((Nuc2D)this.getStartHelix().getFivePrimeStartNuc()));
	else
		this.getStartHelix2D().set((Nuc2D)this.getStartHelix().getFivePrimeStartNuc());
	if (this.getThreePrimeHelix2D() == null)
		this.setThreePrimeHelix2D(new RNAHelix2D((Nuc2D)this.getThreePrimeHelix().getFivePrimeStartNuc()));
	else
		this.getThreePrimeHelix2D().set((Nuc2D)this.getThreePrimeHelix().getFivePrimeStartNuc());
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

// the helix picked, if a basepaired nuc was picked
private RNAHelix2D refHelix2D = null;

public void
setRefHelix2D(RNAHelix2D refHelix2D)
{
    this.refHelix2D = refHelix2D;
}

public RNAHelix2D
getRefHelix2D()
{
    return (this.refHelix2D);
}

public Point2D
getFivePrimeMidPt()
throws Exception
{
	return (getStartHelix2D().getFivePrimeMidPt());
}

private RNAHelix2D threePrimeHelix2D = null;

public void
setThreePrimeHelix2D(RNAHelix2D threePrimeHelix2D)
{
    this.threePrimeHelix2D = threePrimeHelix2D;
}

public RNAHelix2D
getThreePrimeHelix2D()
{
    return (this.threePrimeHelix2D);
}


public Nuc2D
getFivePrimeStartNuc2D()
{
	return ((Nuc2D)this.getFivePrimeStartNuc());
}

public Nuc2D
getFivePrimeEndNuc2D()
{
	return ((Nuc2D)this.getFivePrimeEndNuc());
}

public Nuc2D
getThreePrimeStartNuc2D()
{
	return ((Nuc2D)this.getThreePrimeStartNuc());
}

public Nuc2D
getThreePrimeEndNuc2D()
{
	return ((Nuc2D)this.getThreePrimeEndNuc());
}

// considered clockwise formatted if threePrimeEndNuc of startHelix
// is to the right of ray of helix.
public boolean
isClockWiseFormatted()
throws Exception
{
	// for now assume formatted one way or another
	return(getStartHelix2D().isClockWiseFormatted());
}

public void
setIsSchematic(boolean isSchematic)
throws Exception
{
	SSData2D sstr = (SSData2D)this.getParentCollection();
	for (int nucID = this.getStartHelix2D().getFivePrimeStartNuc2D().getID();
		nucID <= this.getStartHelix2D().getThreePrimeEndNuc2D().getID();
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

public void
formatStraight()
throws Exception
{
	this.formatStraight(this.isClockWiseFormatted());
}

public void
formatStraight(boolean clockWiseFormat)
throws Exception
{
	SSData2D sstr = this.getFivePrimeStartNuc2D().getParentSSData2D();
	double angle = 0.0;
	if (this.isHairPin())
	{
		angle = this.getStartHelixAngle();
	}
	else
	{
		double angle1 = this.getStartHelixAngle();
		double angle2 = this.getThreePrimeHelix2D().getAngle();
		angle = (angle1 + angle2)/2.0;

		// need to alter startHelix's ray
	}

	BVector2d newTailPt = new BVector2d();
	RNASingleStrand2D testSingleStrand = new RNASingleStrand2D();

	RNAHelix2D previousHelix = this.getStartHelix2D();
	for (int nucID = this.getFivePrimeStartNuc2D().getID();
		nucID <= this.getFivePrimeEndNuc2D().getID();nucID++)
	{
		Nuc2D probeNuc = sstr.getNuc2DAt(nucID);
		if (!probeNuc.isBasePair())
			continue;
		if (!probeNuc.isHelixStart()) // NEED to test this on 1 bp
			continue;
		if (probeNuc.isSingleBasePairHelix())
		{
			// debug("FOUND HELIX START: " + probeNuc.getID());
		}
		else
		{
			// debug("FOUND HELIX START: " + probeNuc.getID());
		}
		RNAHelix2D probeHelix = new RNAHelix2D(probeNuc);
		if (probeHelix.getFivePrimeStartNuc().getID() ==
			this.getStartHelix2D().getFivePrimeStartNuc().getID())
			continue;
		// debug("FOUND HELIX, 5' start: " + probeHelix.getFivePrimeStartNuc().getID());

		BLine2D previousHelixRay = previousHelix.getHelixAxis();
		int fivePSSCount = 0;
		int threePSSCount = 0;
		Nuc2D next5PNuc = previousHelix.getFivePrimeEndNuc2D().nextNonNullNuc2D();
		if ((next5PNuc != null) && next5PNuc.isSingleStranded())
		{
			testSingleStrand.set(next5PNuc);
			fivePSSCount = testSingleStrand.getNonDelineatedNucCount();
		}
		Nuc2D last3PNuc = previousHelix.getThreePrimeStartNuc2D().lastNonNullNuc2D();
		if ((last3PNuc != null) && last3PNuc.isSingleStranded())
		{
			testSingleStrand.set(last3PNuc);
			threePSSCount = testSingleStrand.getNonDelineatedNucCount();
		}

		// debug("fivePSSCount: " + fivePSSCount);
		// debug("threePSSCount: " + threePSSCount);

		double extendDistance = this.getRNAHelixBaseDistance();
		if ((fivePSSCount < 3) && (threePSSCount < 3))
		{
		}
		else if (
			(fivePSSCount == 1) || (threePSSCount == 1)
			)
		{
		}
		else
		{
			extendDistance *= 2.0;
		}
		Point2D extendedPt =
			previousHelixRay.ptAtDistance(previousHelixRay.length() +
				extendDistance);

		newTailPt.setXY(extendedPt);
		probeHelix.format(newTailPt, angle, clockWiseFormat);

		if ((next5PNuc != null) && next5PNuc.isSingleStranded())
		{
			// debug("FORMATTING ARC AT: " + next5PNuc.getID());
			testSingleStrand = new RNASingleStrand2D(next5PNuc);
			boolean debugOn = false;
			/*
			if (next5PNuc.getID() == 12)
			{
				debug("formating SS: " + testSingleStrand);
				debugOn = true;
			}
			*/
			testSingleStrand.formatArc(clockWiseFormat, debugOn);
		}
		if ((last3PNuc != null) && last3PNuc.isSingleStranded())
		{
			// debug("FORMATTING ARC AT: " + last3PNuc.getID());
			testSingleStrand = new RNASingleStrand2D(last3PNuc);
			testSingleStrand.formatArc(clockWiseFormat);
		}

		previousHelix = probeHelix;
	}
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
	System.err.println("RNAStackedHelix2D-> " + s);
}
	
}
