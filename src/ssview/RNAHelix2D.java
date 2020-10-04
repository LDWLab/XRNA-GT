package ssview;

import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;
import java.io.*;

import jimage.DrawObjectCollection;

import util.math.*;

public class
RNAHelix2D
extends RNAHelix
{

// parent container should be of type ComplexSSDataCollection2D
// as any rna secondary structure element in a ComplexSSDataCollection2D
// can be part of a helix.

public
RNAHelix2D()
throws Exception
{
	this.initRNAHelix();
}

public
RNAHelix2D(NucNode nuc)
throws Exception
{
	this.initRNAHelix();
	this.set((Nuc2D)nuc);
}

// make a new helix in this sstr only
public
RNAHelix2D(SSData sstr, int refNucID, int bpNucID, int length)
throws Exception
{
	this.initRNAHelix();
	this.setBasePairs(sstr.getNucAt(refNucID), sstr.getNucAt(bpNucID), length);
	this.set((Nuc2D)sstr.getNucAt(refNucID));
}

// make a new helix with arbitray nucs (could be in different strands)
public
RNAHelix2D(NucNode nuc, NucNode bpNuc, int length)
throws Exception
{
	this.setBasePairs(nuc, bpNuc, length);
	this.initRNAHelix();
	this.set((Nuc2D)nuc);
}

public void
initRNAHelix()
throws Exception
{
	this.setBasePair(new RNABasePair2D());
}

public void
set(Nuc2D refNuc)
throws Exception
{
	super.set((NucNode)refNuc);
	// this.setDistancesFromCollection(refNuc.getParentSSData2D());
	this.setDistancesFromCollection((NucCollection2D)this.getParentCollection());
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

public SSData2D
getFivePrimeNuc2DParent()
{
	return ((SSData2D)this.getFivePrimeStartNuc2D().getParentNucCollection2D());
}

public SSData2D
getThreePrimeNuc2DParent()
{
	return ((SSData2D)this.getThreePrimeStartNuc2D().getParentNucCollection2D());
}

// considered clockwise formatted if threePrimeEndNuc is to the
// right of ray of helix.
public boolean
isClockWiseFormatted()
throws Exception
{
	// NEED to get to work for SingleBasePairHelix
	// for now assume formatted one way or another

	/* NEED to always supply whether we want clockwise or not.
	if (this.isSingleBasePairHelix())
	{
		RNABasePair2D basePair = new RNABasePair2D(this.getFivePrimeStartNuc2D());
		return (basePair.isClockWiseFormatted());
	}
	*/

	if (this.isSingleBasePairHelix())
	{
		/*
		throw new Exception("Currently can't use isClockWiseFormatted() for single base pair helix");
		*/
		return ((new RNABasePair2D(this.getFivePrimeStartNuc2D())).isClockWiseFormatted());
	}

	return (MathUtil.ptRelationToRayInXYPlane(
		this.getThreePrimeEndNuc2D().getPoint2D(),
		this.getHelixAxis()) == 1);
}

// format with ray
public void
format(BVector2d fpMidPt, BVector2d tpMidPt, double baseDist,
	double bpDist, double misMatchBPDist, boolean clockWiseFormat)
throws Exception
{
	SSData2D sstr = (SSData2D)this.getFivePrimeStartNuc().getParentSSData();
	int startFPNucID = this.getFivePrimeStartNuc().getID();
	int endFPNucID = this.getFivePrimeEndNuc().getID();

	BVector2d fpSideNucPt = new BVector2d();
	BVector2d tpSideNucPt = new BVector2d();
	
	int nucBPCount = 0;
	RNABasePair2D basePair = new RNABasePair2D();
	for (int nucID = startFPNucID;nucID <= endFPNucID;nucID++)
	{
		basePair.set(sstr.getNuc2DAt(nucID));
		basePair.format(new BLine2D(fpMidPt, tpMidPt), nucBPCount,
			baseDist, bpDist, misMatchBPDist, clockWiseFormat);
		nucBPCount++;
	}

	if (this.isHairPin() && this.getFormatHairPin())
	{
		RNASingleStrand2D hairPin = this.getHairPin2D();
		if (hairPin != null)
			hairPin.formatArc(clockWiseFormat);
	}
}

// format with ray
public void
format(BVector2d fpMidPt, BVector2d tpMidPt, boolean clockWiseFormat)
throws Exception
{
	/*
	debug("HRE: " +
		this.getRNAHelixBaseDistance() + " : " + 
		this.getRNABasePairDistance() + " : " + 
		this.getRNAMisMatchBasePairDistance());
	*/
	this.format(fpMidPt, tpMidPt,
		this.getRNAHelixBaseDistance(),
		this.getRNABasePairDistance(),
		this.getRNAMisMatchBasePairDistance(),
		clockWiseFormat);
}

// format with fpMidPt, angle
public void
format(BVector2d fpMidPt, double angle, boolean clockWiseFormat)
throws Exception
{
	double rayLength = 1000.0; // arbitrary for now
	BVector2d tpMidPt = MathUtil.polarCoordToPoint(rayLength, angle);
	tpMidPt.translate(fpMidPt);
		
	this.format(fpMidPt, tpMidPt, clockWiseFormat);
}

// format with fpMidPt, angle
public void
format(BVector2d fpMidPt, double angle, double baseDist,
	double bpDist, double misMatchBPDist, boolean clockWiseFormat)
throws Exception
{
	double rayLength = 1000.0; // arbitrary for now
	BVector2d tpMidPt = MathUtil.polarCoordToPoint(rayLength, angle);
	tpMidPt.translate(fpMidPt);
		
	this.format(fpMidPt, tpMidPt, baseDist, bpDist, misMatchBPDist, clockWiseFormat);
}

// format angle, using existing fpMidPt
public void
format(double angle, boolean clockWiseFormat)
throws Exception
{
	this.format(BVector2d.toBVector2d(this.getFivePrimeMidPt()), angle, clockWiseFormat);
}

// format angle, using existing fpMidPt
public void
format(double angle, double baseDist, double bpDist,
	double misMatchBPDist, boolean clockWiseFormat)
throws Exception
{
	this.format(BVector2d.toBVector2d(this.getFivePrimeMidPt()), angle, baseDist, bpDist, misMatchBPDist, clockWiseFormat);
}

// format between delineate nucs
// figure out fpMidPt and tpMidPt from nuc endpts info
public void
format(boolean clockWiseFormat)
throws Exception
{
	if ((this.getFivePrimeStartNuc() == null) ||
		(this.getFivePrimeEndNuc() == null) ||
		(this.getThreePrimeStartNuc() == null) ||
		(this.getThreePrimeEndNuc() == null))
	{
		throw new ComplexException("Error in RNAHelix2D.format(): " +
			"end nucs not identified");
	}

	if (!this.getFivePrimeStartNuc2D().isFormatted())
	{
		(new RNABasePair2D(this.getFivePrimeStartNuc2D())).format(clockWiseFormat);
	}

	Point2D tmp5pPt = new Point2D.Double();
	Point2D tmp3pPt = new Point2D.Double();
	this.getBaseRay().getPerpendicularPointAtT(0.5,
		tmp5pPt, tmp3pPt, 500.0, !clockWiseFormat);

	this.format(BVector2d.toBVector2d(tmp5pPt),
		BVector2d.toBVector2d(tmp3pPt), clockWiseFormat);
}

public void
reformat(boolean clockWiseFormat, NucCollection parent)
throws Exception
{
	this.setDistancesFromCollection(parent);
	this.format(clockWiseFormat);
}

public void
reformat(NucCollection parent)
throws Exception
{
	this.setDistancesFromCollection(parent);
//
// debug("IN RNAHELIX2D HB DIST: " + this.getRNAHelixBaseDistance());
// debug("IN RNAHELIX2D BP DIST: " + this.getRNABasePairDistance());
// debug("IN RNAHELIX2D MISM DIST: " + this.getRNAMisMatchBasePairDistance());
// debug("IN RNAHELIX2D NN1 DIST: " + this.getRNANucToNextNucDistance());
//
	this.format(this.isClockWiseFormatted());
}

public void
reformat(boolean clockWiseFormat)
throws Exception
{
	this.reformat(clockWiseFormat, this.getFivePrimeNuc2DParent());
}

public void
reformat()
throws Exception
{
	this.reformat(this.isClockWiseFormatted());
}

public RNABasePair2D
getBasePair2D()
{
    return ((RNABasePair2D)this.getBasePair());
}

public RNASingleStrand2D
getHairPin2D()
throws Exception
{
	if (!this.isHairPin())
		return (null);
	if (this.hairPinLength() <= 0)
		return (null);
	this.getFivePrimeNuc2DParent().setCurrentID(this.getFivePrimeEndNuc2D());
	return (new RNASingleStrand2D((Nuc2D)this.getFivePrimeNuc2DParent().nextNonNullNuc()));
}

// overwrite auto formatting of hairpin
private boolean formatHairPin = true;

public void
setFormatHairPin(boolean formatHairPin)
{
    this.formatHairPin = formatHairPin;
}

public boolean
getFormatHairPin()
{
    return (this.formatHairPin);
}

public double
averageNucToNucDist()
throws Exception
{
	return (this.getHelixAxis().length()/(double)(this.getLength() - 1));
}

public double
averageRegularBasePairDistance()
throws Exception
{
	double distance = 0.0;
	int bpCount = 0;

	this.setStartBasePair();
	while (true)
	{
		RNABasePair2D bp = this.getBasePair2D();
		if (!bp.isMisMatch())
		{
			distance += bp.distance();
			bpCount++;
		}
		if (this.isLastBasePair())
			return (distance/(double)bpCount);
		this.setNextBasePair();
	}
}

public double
averageMisMatchBasePairDistance()
throws Exception
{
	double distance = 0.0;
	int bpCount = 0;

	this.setStartBasePair();
	while (true)
	{
		RNABasePair2D bp = this.getBasePair2D();
		if (bp.isMisMatch())
		{
			distance += bp.distance();
			bpCount++;
		}
		if (this.isLastBasePair())
			return (distance/(double)bpCount);
		this.setNextBasePair();
	}
}

public void
setIsSchematic(boolean isSchematic)
throws Exception
{
	this.setStartBasePair();
	while (true)
	{
		this.getBasePair2D().setIsSchematic(isSchematic);
		if (this.isLastBasePair())
			break;
		this.setNextBasePair();
	}
}

// fp end midpt
public Point2D
getFivePrimeMidPt()
throws Exception
{
	return (BLine2D.getMidPt(this.getFivePrimeStartNuc2D().getPoint2D(),
		this.getThreePrimeEndNuc2D().getPoint2D()));
}

// tp end midpt
public Point2D
getThreePrimeMidPt()
throws Exception
{
	return (BLine2D.getMidPt(this.getFivePrimeEndNuc2D().getPoint2D(),
		this.getThreePrimeStartNuc2D().getPoint2D()));
}

// the ray that runs the length of the helix in the middle 5p -> 3p
public BLine2D
getHelixAxis()
throws Exception
{
	if (this.isSingleBasePairHelix())
	{
		/*
		debug("MADE IT: " + (new RNABasePair2D(
					this.getFivePrimeStartNuc2D())).isClockWiseFormatted());
		*/
		if (this.isHairPin())
			return(this.getSingleBasePairHelixAxis(this.getHairPin2D().getIsClockWiseFormatted()));
		else
			return(this.getSingleBasePairHelixAxis((new RNABasePair2D(
				this.getFivePrimeStartNuc2D())).isClockWiseFormatted()));
	}

	return (new BLine2D(this.getFivePrimeMidPt(), this.getThreePrimeMidPt()));
}

public BLine2D
getSingleBasePairHelixAxis(boolean clockWiseFormat)
throws Exception
{
	Point2D tmp5pPt = new Point2D.Double();
	Point2D tmp3pPt = new Point2D.Double();
	this.getBaseRay().getPerpendicularPointAtT(0.5,
		tmp5pPt, tmp3pPt,
		/*
		0.5 * this.getRNAHelixBaseDistance(),
		*/
		1.0, // try normal length for now (I might have had some kind
		// of formatting reason for doing above
		!clockWiseFormat);
	return (new BLine2D(tmp5pPt, tmp3pPt));
}

public double
getAngle()
throws Exception
{
	/*
	if (this.isSingleBasePairHelix())
		throw new Exception("Currently can't use getAngle() for single base pair helix");
	*/
	return (this.getHelixAxis().angleInXYPlane());
}

public double
getSingleBasePairHelixAngle(boolean clockWiseFormat)
throws Exception
{
	if (!this.isSingleBasePairHelix())
		throw new Exception("Error in RNAHelix2D.getSingleBasePairHelixAngle(): This is not a single base paired helix");
	return (this.getSingleBasePairHelixAxis(clockWiseFormat).angleInXYPlane());
}

// the ray that is perpendicular to getHelixAxis at the 5p end of helix
public BLine2D
getBaseRay()
throws Exception
{
	return (new BLine2D(this.getFivePrimeStartNuc2D().getPoint2D(),
		this.getThreePrimeEndNuc2D().getPoint2D()));
}

// from nuccollection
/*
public void
setEditColor(Color editColor)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	if (delineators == null)
		return;
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			nuc.setEditColor(editColor);
			if (nuc == endStrandNuc)
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}
*/

// from single strand:
/*
public void
setEditColor(Color editColor)
throws Exception
{
	if (editColor == null)
	{
		// explicitly set to null in case they're part of a helix
		// or part of a single strand with no non-delineated nucs
		// in which case we'd get itemListDelineator == null
		this.getFivePrimeDelineateNuc2D().setEditColor(null);
		this.getThreePrimeDelineateNuc2D().setEditColor(null);

		if (this.getItemListDelineators() != null)
			super.setEditColor(null);

		return;
	}

	// might not need concept of formatStraight or formatArced
	if (true)
	{
		this.getFivePrimeDelineateNuc2D().setEditColor(Color.red);
		this.getThreePrimeDelineateNuc2D().setEditColor(Color.red);

		SSData2D sstr = this.getParentSSData2D();
		for (int nucID = this.getFivePrimeDelineateNuc2D().getID() + 1;
			nucID < this.getThreePrimeDelineateNuc2D().getID();nucID++)
		{
			Nuc2D refNuc = sstr.getNuc2DAt(nucID);
			if (refNuc == null)
				continue;
			refNuc.setEditColor(Color.green);
		}
	}
}
*/

public void
setEditColor(Color editColor)
throws Exception
{
	if (editColor == null)
	{
		/*
		// explicitly set to null in case they're part of a helix
		// or part of a single strand with no non-delineated nucs
		// in which case we'd get itemListDelineator == null
		this.getFivePrimeDelineateNuc2D().setEditColor(null);
		this.getThreePrimeDelineateNuc2D().setEditColor(null);
		*/

		if (this.getItemListDelineators() != null)
			super.setEditColor(null);

		return;
	}

	// first color everything
	if (this.getItemListDelineators() != null)
		super.setEditColor(editColor);
	
	// check if formatting hairpin or not; if not then uncolor
	if (this.isHairPin() && !this.getFormatHairPin())
	{
		RNASingleStrand2D singleStrand = this.getHairPin2D();
		singleStrand.setEditColor(null);
		this.getFivePrimeEndNuc2D().setEditColor(editColor);
		this.getThreePrimeStartNuc2D().setEditColor(editColor);
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

// direction is false to the 5' direction, true to the 3' direction
public void
shiftDistance(double distance, boolean direction)
throws Exception
{
	BLine2D ray = this.getHelixAxis();
	Point2D midPt = this.getFivePrimeMidPt();
	Point2D newPt = null;
	if (direction)
		newPt = ray.ptAtDistance(distance);
	else
		newPt = ray.ptAtDistance(-distance);
	double x = midPt.getX() - newPt.getX();
	double y = midPt.getY() - newPt.getY();

	this.setStartBasePair();
	while (true)
	{
		this.getBasePair2D().getFivePrimeNuc2D().shiftXY(x, y);
		this.getBasePair2D().getThreePrimeNuc2D().shiftXY(x, y);
		if (this.isLastBasePair())
			break;
		this.setNextBasePair();
	}
	if (this.isHairPin())
		this.getHairPin2D().shiftXY(x, y);
}

public void
setHelixColor(Color color)
throws Exception
{
	this.setStartBasePair();
	while (true)
	{
		this.getBasePair2D().setColor(color);
		if (this.isLastBasePair())
			break;
		this.setNextBasePair();
	}
	/*
	if (this.isHairPin())
		this.getHairPin2D().setColor(color);
	*/
}

public void
delete(Graphics2D g2)
throws Exception
{
	this.erase(g2);

	if (this.isHairPin())
		this.getHairPin2D().delete(g2);
	this.setStartBasePair();
	while (true)
	{
		this.getBasePair2D().delete(g2);
		if (this.isLastBasePair())
			break;
		this.setNextBasePair();
	}
}

private static void
debug(String s)
{
	System.err.println("RNAHelix2D-> " + s);
}
	
}
