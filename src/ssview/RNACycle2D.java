package ssview;

import java.awt.*;
import java.awt.geom.*;
import java.util.Vector;
import java.io.*;

import jimage.DrawObjectCollection;
import jimage.DrawLineObject;
import jimage.DrawCircleObject;

import util.*;
import util.math.*;

public class
RNACycle2D
extends RNACycle
{

double halfRayDistance = 200.0;

RNABasePair2D refBasePair = null;

public
RNACycle2D()
throws Exception
{
	refBasePair = new RNABasePair2D();
}

public
RNACycle2D(NucNode nuc)
throws Exception
{
	refBasePair = new RNABasePair2D();
	this.set((Nuc2D)nuc);
}

public void
set(Nuc2D refNuc)
throws Exception
{
	super.set((NucNode)refNuc);
	// this.setDistancesFromCollection(refNuc.getParentSSData2D());
	// this.setDistancesFromCollection(refNuc.getParentNucCollection2D());
	this.setDistancesFromCollection((NucCollection)this.getParentCollection());

	if (refNuc.isBasePair())
		this.setRefHelix2D(new RNAHelix2D(refNuc));
}

public void
reset(boolean clockWiseFormat)
throws Exception
{
	this.partialReset(clockWiseFormat);
	this.resetCycleArcDistance(false);
}

public void
partialReset(boolean clockWiseFormat)
throws Exception
{
	super.reset();

	if (this.getCycleHelices() == null)
		return;
	BLine2D ray = new BLine2D(0.0, 0.0, 0.0, 0.0);
	RNAHelix2D helix = this.getEntryHelix2D();

	if (helix == null) // then at level 0
	{
		if (this.getFivePrimeNuc2D().isFormatted() &&
			this.getThreePrimeNuc2D().isFormatted())
			ray.setLine(this.getFivePrimeNuc2D().getPoint2D(),
				this.getThreePrimeNuc2D().getPoint2D());
		/*
		debug("5': " + this.getFivePrimeNuc2D().getPoint2D());
		debug("3': " + this.getThreePrimeNuc2D().getPoint2D());
		*/
	}
	else
	{
		if (this.getFivePrimeNuc2D().isFormatted() &&
			this.getThreePrimeNuc2D().isFormatted())
			ray.setLine(
				helix.getFivePrimeEndNuc2D().getPoint2D(),
				helix.getThreePrimeStartNuc2D().getPoint2D());
	}

	this.setArcSecant(ray);
	this.setArcSecantMidPt(ray.getMidPt());

	Point2D tmpMidPt = new Point2D.Double();
	Point2D rayTail = new Point2D.Double();
	// rayTail will be perp to arcSecant in opposite direction of arc
	this.getArcSecant().getPerpendicularPointAtT(
		0.5, tmpMidPt, rayTail, halfRayDistance, clockWiseFormat);
	BLine2D tmpRay = new BLine2D(rayTail, this.getArcSecantMidPt());
	Point2D rayHead = tmpRay.ptAtDistance(2.0*halfRayDistance);
	this.setPerpendicularArcSecant(new BLine2D(rayTail, rayHead));
	this.setPerpendicularMidPtToHeadRay(
		new BLine2D(this.getArcSecantMidPt(), rayHead));
}

public void
resetCycleArcDistance(boolean debugOn)
throws Exception
{
	SSData2D sstr = (SSData2D)this.getParentCollection();
	RNABasePair2D formatBasePair = new RNABasePair2D();
	RNAHelix probeHelix = new RNAHelix();
	RNABasePair basePair = new RNABasePair();
	double dist = 0.0;
	double nn1Dist = this.getRNANucToNextNucDistance();
	Nuc2D firstValidNuc = sstr.getFirstNonNullNuc2D();
	Nuc2D startNuc = this.getFivePrimeNuc2D();
	Nuc2D probeNuc = startNuc;

	// MAYBE NEED TO PUT BACK IN:
	// this.setCycleNucs(this.resetCycleNucs(startNuc, debugOn));
	Vector cycleNucList = this.getCycleNucs();
	if (debugOn) debug("IN resetCycleArcDistance, startNuc: " + startNuc);
	if (debugOn) debug("IN resetCycleArcDistance, cycleNucList.size(): " + cycleNucList.size());
	// CYCLES
	for (int i = 0;i < cycleNucList.size();i++)
	{
		probeNuc = (Nuc2D)cycleNucList.elementAt(i);
		if (debugOn) debug("PROBENUC now: " + probeNuc.getID());
		if (probeNuc.isSingleStranded())
		{
			// if (this.atCycle0() && (probeNuc != startNuc))
			if (probeNuc != startNuc)
				dist += nn1Dist;
			if (debugOn) debug("IN SS, dist: " + dist);
		}
		else if (probeNuc.isSingleBasePairHelix())
		{
			basePair.set(probeNuc);
			if (probeNuc == basePair.getFivePrimeNuc())
			{
				if (this.atCycle0() && (probeNuc == startNuc) && (probeNuc == firstValidNuc))
				{
					// then entering single BP exit helix at level 0
					// probeNuc = probeNuc.getBasePair();
				}
				else if (this.atCycle0())
				{
					// probeNuc = probeNuc.getBasePair();
					dist += nn1Dist;
				}
				else if (probeNuc == this.getEntryHelix().getFivePrimeEndNuc())
				{
					// then exiting single BP entry helix
					// probeNuc = probeNuc.nextNonNullNuc();
				}
				else
				{
					// then entering single BP exit helix
					// probeNuc = probeNuc.getBasePair();
					dist += nn1Dist;
				}
				if (debugOn) debug("probeNuc in 5' SSBPH, dist: " + dist);
			}
			else if (probeNuc == basePair.getThreePrimeNuc())
			{
				if ((!this.atCycle0()) && (probeNuc != this.getEntryHelix().getThreePrimeStartNuc()))
				{
					// then in exiting exit helix
					formatBasePair.set(probeNuc);
					formatBasePair.format(true);
					dist += formatBasePair.get5PBasePairRay().length();
				}
				else if (this.atCycle0())
				{
					// then in exiting exit helix
					formatBasePair.set(probeNuc);
					formatBasePair.format(true);
					dist += formatBasePair.get5PBasePairRay().length();
				}
				else
				{
					// entering entry helix
					dist += nn1Dist;
				}
				if (debugOn) debug("probeNuc in 3' SSBPH, dist: " + dist);
			}
		}
		else
		{
			probeHelix.set(probeNuc);
			if (probeNuc == probeHelix.getFivePrimeEndNuc())
			{
				// then in exiting entry helix; don't add
			}
			else if (probeNuc == probeHelix.getFivePrimeStartNuc())
			{
				// then in entering exit helix
				if (probeNuc != startNuc)
					dist += nn1Dist;
			}
			else if (probeNuc == probeHelix.getThreePrimeEndNuc())
			{
				// then in exiting exit helix
				formatBasePair.set(probeNuc);
				formatBasePair.format(true);
				dist += formatBasePair.get5PBasePairRay().length();
			}
			else if (probeNuc == probeHelix.getThreePrimeStartNuc())
			{
				// then in entering entry helix; don't count
				if (probeNuc != startNuc)
					dist += nn1Dist;
			}
			if (debugOn) debug("probeNuc in HELIX, dist: " + dist);
		}
	}

	if (debugOn) debug("dist: " + dist);
	this.setCycleArcDistance(dist);
}

public RNAHelix2D
getEntryHelix2D()
throws Exception
{
	if (this.atCycle0())
		return (null);
	if (this.getCycleHelices() == null)
		return (null);
	return (new RNAHelix2D((Nuc2D)this.getCycleHelices().elementAt(0)));
}

public RNAHelix2D
getFirstExitHelix2D()
throws Exception
{
	return (new RNAHelix2D((Nuc2D)this.getCycleHelices().elementAt(1)));
}

public RNAHelix2D
getLastExitHelix2D()
throws Exception
{
	return (new RNAHelix2D((Nuc2D)this.getCycleHelices().elementAt(this.getExitHelicesCount())));
}

public Nuc2D
getFivePrimeNuc2D()
throws Exception
{
	if (this.atCycle0())
		// return (((SSData2D)this.getParentCollection()).getNuc2DAt(1));
		return (((SSData2D)this.getParentCollection()).getFirstNonNullNuc2D());
	return (this.getEntryHelix2D().getFivePrimeEndNuc2D());
}

public Nuc2D
getThreePrimeNuc2D()
throws Exception
{
	if (this.atCycle0())
	{
		SSData2D sstr = (SSData2D)this.getParentCollection();
		return (sstr.getEndNonNullNuc2D());
	}
	return (this.getEntryHelix2D().getThreePrimeStartNuc2D());
}

public Nuc2D
getRefNuc2D()
{
	return ((Nuc2D)this.getRefNuc());
}

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

private BLine2D arcSecant = null;

public void
setArcSecant(BLine2D arcSecant)
{
    this.arcSecant = arcSecant;
}

public BLine2D
getArcSecant()
{
	return (this.arcSecant);
}

private Point2D arcSecantMidPt = null;

public void
setArcSecantMidPt(Point2D arcSecantMidPt)
{
    this.arcSecantMidPt = arcSecantMidPt;
}

public Point2D
getArcSecantMidPt()
{
    return (this.arcSecantMidPt);
}

private BLine2D perpendicularArcSecant = null;

public void
setPerpendicularArcSecant(BLine2D perpendicularArcSecant)
{
    this.perpendicularArcSecant = perpendicularArcSecant;
}

public BLine2D
getPerpendicularArcSecant()
{
    return (this.perpendicularArcSecant);
}

private BLine2D perpendicularMidPtToHeadRay = null;

public void
setPerpendicularMidPtToHeadRay(BLine2D perpendicularMidPtToHeadRay)
{
    this.perpendicularMidPtToHeadRay = perpendicularMidPtToHeadRay;
}

public BLine2D
getPerpendicularMidPtToHeadRay()
{
    return (this.perpendicularMidPtToHeadRay);
}

// distance from 5' -> 3' along secant
private double cycleArcDistance = 0.00;

public void
setCycleArcDistance(double cycleArcDistance)
{
    this.cycleArcDistance = cycleArcDistance;
}

public double
getCycleArcDistance()
{
    return (this.cycleArcDistance);
}

public double
getCycleAverageNN1Distance()
throws Exception
{
	double aveDist = 0.0;
	int nucCount = 0;
	Vector cycleNucList = this.getCycleNucs();
	Nuc2D lastNuc = (Nuc2D)cycleNucList.elementAt(0);

	for (int i = 1;i < cycleNucList.size();i++)
	{
		Nuc2D nuc = (Nuc2D)cycleNucList.elementAt(i);
		if (nuc.getID() != lastNuc.getID() + 1)
		{
			lastNuc = nuc;
			continue;
		}
		aveDist += nuc.getPoint2D().distance(lastNuc.getPoint2D());
		nucCount++;
		lastNuc = nuc;
	}

	return (aveDist/nucCount);
}

public void
setIsSchematic(boolean isSchematic)
throws Exception
{
	SSData2D sstr = (SSData2D)this.getParentCollection();
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		int startNucID = ((NucNode)delineators.elementAt(i)).getID();
		int endNucID = ((NucNode)delineators.elementAt(i+1)).getID();
		for (int nucID = startNucID;nucID <= endNucID;nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			nuc.setIsSchematic(isSchematic);
		}
	}
}

public void
shiftXY(double x, double y)
throws Exception
{
	SSData2D sstr = (SSData2D)this.getParentCollection();
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		int startNucID = ((NucNode)delineators.elementAt(i)).getID();
		int endNucID = ((NucNode)delineators.elementAt(i+1)).getID();
		for (int nucID = startNucID;nucID <= endNucID;nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			nuc.shiftXY(x, y);
		}
	}
}

public void
printCycleNucs()
throws Exception
{
	Nuc2D startNuc = null;
	Nuc2D endNuc = null;

	if (this.atCycle0()) // include sstr endpts
	{
		System.err.println("IN PRINTCYCLENUCS, atlevel0");
		Vector nucList =
			((SSData2D)this.getParentCollection()).getItemListDelineators();
		startNuc = (Nuc2D)nucList.elementAt(0);
		endNuc = (Nuc2D)nucList.elementAt(nucList.size() - 1);
	}
	else
	{
		System.err.println("IN PRINTCYCLENUCS, not at atlevel0");
		startNuc = this.getEntryHelix2D().getFivePrimeEndNuc2D();
		endNuc = this.getEntryHelix2D().getThreePrimeStartNuc2D();
	}
	if (startNuc == null)
		throw new Exception("ERROR in RNACycle2D.formatCycle(): No startNuc");
	if (endNuc == null)
		throw new Exception("ERROR in RNACycle2D.formatCycle(): No endNuc");

	System.err.println("IN PRINTCYCLENUCS, startNuc, endNuc: " + startNuc + ":" + endNuc);

	Nuc2D probeNuc = startNuc;
	while(true)
	{
		System.err.println("CYCLE NUC: " + probeNuc);

		if (probeNuc.equals(endNuc))
			break;

		if (probeNuc.isSelfRefBasePair() &&
			probeNuc.isFivePrimeBasePair())
			probeNuc = probeNuc.getBasePair2D();
		else
			probeNuc = probeNuc.nextNonNullNuc2D();
	}
	System.err.println();
	System.err.println();
}

// for testting purposes:
public void
formatAtMidPtDist(double midPtDist, boolean clockWiseFormat)
throws Exception
{
	if (this.getCycleHelices() == null)
		throw new ComplexException(
			"Error in RNACycle2D.formatAtMidPtDist(boolean, boolean)",
			ComplexDefines.RNA_LEVEL_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_NO_HELICES_ERROR_MSG,
			"need to set some helices");

	this.reset(clockWiseFormat);

	Nuc2D startNuc = null;
	Nuc2D endNuc = null;

	if (this.atCycle0()) // include sstr endpts
	{
		Vector nucList =
			((SSData2D)this.getParentCollection()).getItemListDelineators();
		startNuc = (Nuc2D)nucList.elementAt(0);
		endNuc = (Nuc2D)nucList.elementAt(nucList.size() - 1);
	}
	else
	{
		startNuc = this.getEntryHelix2D().getFivePrimeEndNuc2D();
		endNuc = this.getEntryHelix2D().getThreePrimeStartNuc2D();
	}
	if (startNuc == null)
		throw new Exception("ERROR in RNACycle2D.formatCycle(): No startNuc");
	if (endNuc == null)
		throw new Exception("ERROR in RNACycle2D.formatCycle(): No endNuc");

	formatCycle(startNuc, endNuc, midPtDist, clockWiseFormat);
}

boolean debugOn = false;
static boolean runFormatTesting = false;

public void
format(boolean clockWiseFormat, boolean debugOn)
throws Exception
{
	this.debugOn = debugOn;
	this.format(clockWiseFormat);
	this.debugOn = false;
}

// format this cycle and all further cycles
public void
format(boolean clockWiseFormat)
throws Exception
{
	if (this.getCycleHelices() == null)
		return;

	NucCollection2D nucCollection = (NucCollection2D)this.getParentCollection();
	// boolean debugOn = false;

	this.reset(clockWiseFormat);

	Nuc2D startNuc = null;
	Nuc2D endNuc = null;

	if (debugOn) debug("FORMATTING AT LEVEL 0: " + this.atCycle0());

	if (this.atCycle0()) // include nuccollection endpts
	{
		Vector nucList =
			((SSData2D)this.getParentCollection()).getItemListDelineators();
		startNuc = (Nuc2D)nucList.elementAt(0);
		endNuc = (Nuc2D)nucList.elementAt(nucList.size() - 1);
		// debug("HERE: " + startNuc.getID() + " " + endNuc.getID());

		// check if case 2: single exit helix with no tag ends
		if (startNuc.isBasePair() && endNuc.isBasePair() &&
			(startNuc.getBasePair() == endNuc)) // then case 2
		{
			this.formatCycle(startNuc, endNuc, 0.0, clockWiseFormat);
			if (runFormatTesting)
				testFormat(startNuc, endNuc);
			return;
		}
	}
	else
	{
		startNuc = this.getEntryHelix2D().getFivePrimeEndNuc2D();
		endNuc = this.getEntryHelix2D().getThreePrimeStartNuc2D();
	}
	if (startNuc == null)
		throw new Exception("ERROR in RNACycle2D.format(boolean): No startNuc");
	if (endNuc == null)
		throw new Exception("ERROR in RNACycle2D.format(boolean): No endNuc");
	if (debugOn) debug("FORMATTING CYCLE: "  + startNuc.getID() + " " + endNuc.getID());
	if (debugOn) debug("cycleArcDistance: " + this.getCycleArcDistance());

	// START find cmp0, cmp1 nucs
	// try and find a suitable pair of nucs to compare distances
	Nuc2D cmp0Nuc = null;
	Nuc2D cmp1Nuc = null;
	RNAHelix2D probeHelix = new RNAHelix2D();
	RNABasePair2D basePair = new RNABasePair2D();
	Nuc2D probeNuc = startNuc;
	// Nuc2D firstValidNuc = sstr.getFirstNonNullNuc2D();
	Nuc2D firstValidNuc = (Nuc2D)nucCollection.getFirstNonNullNuc();

	if (this.atCycle0())
	{
		if (startNuc.isSingleStranded())
		{
			cmp0Nuc = startNuc;
			cmp1Nuc = cmp0Nuc.nextNuc2D();
		}
		else
		{
			cmp0Nuc = this.getFirstExitHelix2D().getThreePrimeEndNuc2D();
			cmp1Nuc = cmp0Nuc.nextNuc2D();
		}

	}
	else
	{
		// look at exit helix 5' end nuc and that nucs nextnuc
		// this should always work
		cmp0Nuc = this.getEntryHelix2D().getFivePrimeEndNuc2D();
		cmp1Nuc = cmp0Nuc.nextNuc2D();
	}

	if (cmp0Nuc == null)
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp0Nuc is null");

	if (cmp1Nuc == null)
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp1Nuc is null");

	if (RNABasePair.nucsAreBasePaired(cmp0Nuc, cmp1Nuc))
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp nucs are basepaired together");
	
	// have to make sure cmp0Nuc and cmp1Nuc are cycle nucs
	// pattern after printCycles when printCycleNucs is fixed
	if (!this.getCycleNucs().contains(cmp0Nuc))
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp0Nuc: " + cmp0Nuc.getID() + " not a cycle nuc");
	if (!this.getCycleNucs().contains(cmp0Nuc))
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp0Nuc: " + cmp0Nuc.getID() + " not a cycle nuc");

	if (!( (cmp1Nuc.getID() == cmp0Nuc.getID() + 1) || (cmp1Nuc.getID() == cmp0Nuc.getID() - 1) ))
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp0Nuc, cmp1Nuc: " + cmp0Nuc.getID() + ", " + cmp1Nuc.getID() + " not contiguous");
	// END find cmp0, cmp1 nucs

	double largestVal = 100000.0;
	double smallestVal = -1000.0; // works best so far
	
	// look at special cases:
	if (!this.atCycle0())
	{
		if ((this.getExitHelicesCount() == 3) && (this.getCycleSSNucCount() == 0))
		{
			largestVal = 17.0;
			smallestVal = 15.0;
		}
		else if ((this.getExitHelicesCount() == 3) && (this.getCycleSSNucCount() == 1))
		{
			largestVal = 18.0;
			smallestVal = 14.0;
		}
		else if ((this.getExitHelicesCount() == 4) && (this.getCycleSSNucCount() <= 1))
		{
			largestVal = 40.0;
			smallestVal = 20.0;
		}
		else if ((this.getExitHelicesCount() == 5) && (this.getCycleSSNucCount() <= 4))
		{
			largestVal = 40.0;
			smallestVal = 20.0;
		}
			/*
			debug("HERE");
			this.formatCycle(startNuc, endNuc, 22.0, clockWiseFormat);
			return;
			*/
	}
	
	double midVal = (smallestVal + largestVal)/2.0;
	double nn1Dist = this.getRNANucToNextNucDistance();
	double tolerance = this.getDistanceTolerance();
	double upperNucTolerance = nn1Dist + tolerance;
	double lowerNucTolerance = nn1Dist - tolerance;
	double precision = 1000.0;

	if (debugOn) debug("arcSecant.length: " + this.getArcSecant().length());
	if (debugOn) debug("arcSecant.length: " + this.getArcSecant().length());
	if (debugOn) debug("final CycleArcDistance: " + this.getCycleArcDistance());
	if (debugOn) debug("final nn1Dist: " + nn1Dist);	

	while (true)
	{
		// debug("largestVal: " + largestVal);
		this.formatCycle(startNuc, endNuc, largestVal, clockWiseFormat);
		double largestNucDist = cmp0Nuc.getPoint2D().distance(cmp1Nuc.getPoint2D());
		if ((largestNucDist >= lowerNucTolerance) && (largestNucDist <= upperNucTolerance))
			break;

		// debug("smallestVal: " + smallestVal);
		this.formatCycle(startNuc, endNuc, smallestVal, clockWiseFormat);
		double smallestNucDist = cmp0Nuc.getPoint2D().distance(cmp1Nuc.getPoint2D());
		if ((smallestNucDist >= lowerNucTolerance) && (smallestNucDist <= upperNucTolerance))
			break;

		// debug("midVal: " + midVal);
		this.formatCycle(startNuc, endNuc, midVal, clockWiseFormat);
		double midNucDist = cmp0Nuc.getPoint2D().distance(cmp1Nuc.getPoint2D());
		if ((midNucDist >= lowerNucTolerance) && (midNucDist <= upperNucTolerance))
			break;

		if (
			MathUtil.precisionEquality(smallestNucDist, midNucDist, precision) &&
			MathUtil.precisionEquality(largestNucDist, midNucDist, precision) &&
			MathUtil.precisionEquality(smallestNucDist, largestNucDist, precision))
		{
			break;
		}
		
if (debugOn)
{
debug("snd,mnd,lnd: " +
	StringUtil.roundStrVal(smallestNucDist, 2) + " " +
	StringUtil.roundStrVal(midNucDist, 2) + " " +
	StringUtil.roundStrVal(largestNucDist, 2)
	);
debug("sv,mv,lv: " +
	StringUtil.roundStrVal(smallestVal, 2) + " " +
	StringUtil.roundStrVal(midVal, 2) + " " +
	StringUtil.roundStrVal(largestVal, 2)
	);
debug("");
}

		if ((smallestNucDist > nn1Dist) && (nn1Dist > midNucDist))
		{
			largestVal = midVal;
		}
		else if ((midNucDist > nn1Dist) && (nn1Dist > largestNucDist))
		{
			smallestVal = midVal;
		}
		else if ((largestNucDist > nn1Dist) && (nn1Dist > midNucDist))
		{
			smallestVal = midVal;
		}
		else if ((smallestNucDist < nn1Dist) && (nn1Dist < midNucDist))
		{
			largestVal = midVal;
		}
		/*
		else if ((smallestNucDist > nn1Dist) && (nn1Dist < midNucDist))
		{
			smallestVal -= 10.0;
		}
		*/
		else
		{
			/*
			debug("startNuc, endNuc: " + startNuc.getID() + " " +
				endNuc.getID());
			*/
			throw new ComplexException(
				"Error in RNACycle2D.format(boolean)",
				ComplexDefines.RNA_LEVEL_ERROR +
					ComplexDefines.FORMAT_ERROR,
				"Invalid partition: " + 
					StringUtil.roundStrVal(smallestNucDist, 2) + " " +
					StringUtil.roundStrVal(midNucDist, 2) + " " +
					StringUtil.roundStrVal(largestNucDist, 2) + " " +
					StringUtil.roundStrVal(smallestVal, 2) + " " +
					StringUtil.roundStrVal(midVal, 2) + " " +
					StringUtil.roundStrVal(largestVal, 2) + "\n" +
					" for nucs: " + startNuc.getID() + " " + endNuc.getID() + "\n" +
					" cmp nucs: " + cmp0Nuc.getID() + " " + cmp1Nuc.getID()
					);
		}
		midVal = (smallestVal + largestVal)/2.0;

	} // end while(true)

	if (runFormatTesting)
		testFormat(startNuc, endNuc);
}

public void
testFormat(Nuc2D startNuc, Nuc2D endNuc)
throws Exception
{
	NucCollection2D nucCollection = (NucCollection2D)this.getParentCollection();
	SSData2D sstr = (SSData2D)this.getParentCollection();
	// debug("TESTING CYCLE: " + this.toString());
	Vector cycleNucList = this.getCycleNucs();
	// cycle through nuc list and see if distances are reasonable
	Nuc2D lastNuc = null;
	Nuc2D probeNuc = null;
	double distance = 0.0;

	if (this.isBulgedNucCycle())
	{
		boolean misMatchFound = false;
		// for now skip any non-canonical base pairs 
		for (int i = 0;i < cycleNucList.size();i++)
		{
			probeNuc = (Nuc2D)cycleNucList.elementAt(i);
			if (probeNuc.inMisMatchBasePair())
				misMatchFound = true;
		}

		if (!misMatchFound)
		{
		probeNuc = this.getEntryHelix2D().getFivePrimeEndNuc2D();
		Nuc2D nextNuc = this.getFirstExitHelix2D().getFivePrimeStartNuc2D();
		distance = probeNuc.getPoint2D().distance(
			nextNuc.getPoint2D());
		if (!nucCollection.isValidRNANucToNextNucDistance(distance))
			throw new ComplexException(
				"Error in RNACycle2D.format(boolean)",
				ComplexDefines.RNA_LEVEL_ERROR + ComplexDefines.FORMAT_ERROR,
				ComplexDefines.FORMAT_LEVEL_MSG,
				"nucs " + probeNuc.getID() + "," +
					nextNuc.getID() + " distance not valid: " + distance + "\n" +
					"should be: " + this.getRNANucToNextNucDistance() + "\n" +
					"in rna strand: " + probeNuc.getParentName() + "\n" +
					sstr.getPrimaryStructure() + "\n" +
					sstr.getSecondaryStructure());
		probeNuc = this.getEntryHelix2D().getThreePrimeStartNuc2D();
		lastNuc = this.getFirstExitHelix2D().getThreePrimeEndNuc2D();
		distance = probeNuc.getPoint2D().distance(
			lastNuc.getPoint2D());
		if (!nucCollection.isValidRNANucToNextNucDistance(distance))
			throw new ComplexException(
				"Error in RNACycle2D.format(boolean)",
				ComplexDefines.RNA_LEVEL_ERROR + ComplexDefines.FORMAT_ERROR,
				ComplexDefines.FORMAT_LEVEL_MSG,
				"nucs " + probeNuc.getID() + "," +
					lastNuc.getID() + " distance not valid: " + distance + "\n" +
					"should be: " + this.getRNANucToNextNucDistance() + "\n" +
					"in rna strand: " + probeNuc.getParentName() + "\n" +
					sstr.getPrimaryStructure() + "\n" +
					sstr.getSecondaryStructure());
		}

	}
	else if ((this.getEntryHelix() != null) && (this.getEntryHelix().isSingleBasePairHelix()))
	{
		// no test for now
	}
	// check if 2 bulge nucs one on either side
	else if ((this.getEntryHelix() != null) && (cycleNucList.size() < 7) &&
		(this.getEntryHelix().getFivePrimeEndNuc().getID() + 1 !=
			this.getFirstExitHelix().getFivePrimeStartNuc().getID()) &&
			(this.getEntryHelix().getThreePrimeStartNuc().getID() - 1 !=
			 this.getFirstExitHelix().getThreePrimeEndNuc().getID()))
	{
		// debug("MADE IT 000000000000000: " + this.getEntryHelix().getFivePrimeEndNuc().getID());
		// no test for now
	}
	else
	{
		for (int i = 0;i < cycleNucList.size();i++)
		{
			probeNuc = (Nuc2D)cycleNucList.elementAt(i);
			if (probeNuc == startNuc)
			{
				lastNuc = probeNuc;
				continue;
			}
			if (probeNuc.getID() != lastNuc.getID() + 1)
			{
				lastNuc = probeNuc;
				continue;
			}

			/*
			// for now skip any non-canonical base pairs 
			if (probeNuc.inMisMatchBasePair() || lastNuc.inMisMatchBasePair())
			{
				lastNuc = probeNuc;
				continue;
			}
			*/

			distance = probeNuc.getPoint2D().distance(lastNuc.getPoint2D());
			// debug("TESTING: " + lastNuc.getID() + " " + probeNuc.getID() + " " + distance);
			// NEED to deal with this. This messes up on single nuc format
			// for helical run SS format
			if (!nucCollection.isValidRNANucToNextNucDistance(distance))
				throw new ComplexException(
					"Error in RNACycle2D.format(boolean)",
					ComplexDefines.RNA_LEVEL_ERROR + ComplexDefines.FORMAT_ERROR,
					ComplexDefines.FORMAT_LEVEL_MSG,
					"nucs " + probeNuc.getID() + "," +
						lastNuc.getID() + " distance not valid: " + distance + "\n" +
						"should be: " + this.getRNANucToNextNucDistance() + "\n" +
						"in rna strand: " + probeNuc.getParentName() + "\n" +
						sstr.getPrimaryStructure() + "\n" +
						sstr.getSecondaryStructure());

			lastNuc = probeNuc;
		}
	}

	// now test if radius of cycle circle is reasonable (no check for
	// level 0 yet):
	if ((!this.atCycle0()) && (!this.isBulgedNucCycle()))
	{
		double cycleRadius = this.getCycleCircle().getRadius();
		// debug("testing cycle radius: " + cycleRadius);

		// this is checking against an arbitrary radius for now
		RNAHelix2D entryHelix = this.getEntryHelix2D();
		if ((entryHelix != null) && (!entryHelix.isSingleBasePairHelix()))
		{
			double checkRadius = 0.0;
			// these check radii are dependent on standard distances
			if (cycleNucList.size() < 7) // then 2 bulge nucs possibly on same side
			{
				if ((entryHelix.getFivePrimeEndNuc().getID() + 1 ==
					this.getFirstExitHelix().getFivePrimeStartNuc().getID())
					||
					(entryHelix.getThreePrimeStartNuc().getID() - 1 ==
					 this.getFirstExitHelix().getThreePrimeEndNuc().getID())
					)
				{
					checkRadius = 7.0; // nucs on same side
				}
				else
				{
					checkRadius = 3.9; // one nuc on each side
				}
			}
			else if (cycleNucList.size() < 8)
			{
				checkRadius = 8.0;
			}
			else if (cycleNucList.size() == 8)
			{
				if ((entryHelix.getFivePrimeEndNuc().getID() + 1 ==
					this.getFirstExitHelix().getFivePrimeStartNuc().getID())
					||
					(entryHelix.getThreePrimeStartNuc().getID() - 1 ==
					 this.getFirstExitHelix().getThreePrimeEndNuc().getID())
					)
				{
					checkRadius = 9.0; // nucs on same side
				}
				else
				{
					checkRadius = 8.0;
				}
			}
			else if (this.getExitHelicesCount() == 1)
			{
				checkRadius = 0.495*
					entryHelix.getFivePrimeEndNuc2D().getPoint2D().distance(
					entryHelix.getThreePrimeStartNuc2D().getPoint2D());
			}
			else
			{
				checkRadius = 0.7*
					entryHelix.getFivePrimeEndNuc2D().getPoint2D().distance(
					entryHelix.getThreePrimeStartNuc2D().getPoint2D());
			}

			if (cycleRadius < checkRadius)
				throw new ComplexException(
					"Error in RNACycle2D.format(boolean)",
					ComplexDefines.RNA_LEVEL_ERROR + ComplexDefines.FORMAT_ERROR,
					ComplexDefines.FORMAT_LEVEL_MSG,
					"cycle: " + this.toString() + "\n" +
					"not valid radius: " + cycleRadius + "\n" +
					"in rna strand: " + entryHelix.getFivePrimeStartNuc().getParentName() + "\n" +
					"should be >= " + checkRadius + "\n" +
					sstr.getPrimaryStructure() + "\n" +
					sstr.getSecondaryStructure());
		}
	}

	// debug("TESTING COMPLETE\n");
}

/* Doesn't work yet; uses a new dividor routine
public void
new_format(boolean clockWiseFormat)
throws Exception
{
	if (this.getCycleHelices() == null)
		return;

	// SSData2D sstr = (SSData2D)this.getParentCollection();
	NucCollection2D nucCollection = (NucCollection2D)this.getParentCollection();
	// boolean debugOn = false;

	this.reset(clockWiseFormat);

	Nuc2D startNuc = null;
	Nuc2D endNuc = null;

	if (debugOn) debug("FORMATTING AT LEVEL 0: " + this.atCycle0());

	if (this.atCycle0()) // include nuccollection endpts
	{
		Vector nucList =
			((SSData2D)this.getParentCollection()).getItemListDelineators();
		startNuc = (Nuc2D)nucList.elementAt(0);
		endNuc = (Nuc2D)nucList.elementAt(nucList.size() - 1);

		// check if case 2: single exit helix with no tag ends
		if (startNuc.isBasePair() && endNuc.isBasePair() &&
			(startNuc.getBasePair() == endNuc)) // then case 2
		{
			this.formatCycle(startNuc, endNuc, 0.0, clockWiseFormat);
			return;
		}
	}
	else
	{
		startNuc = this.getEntryHelix2D().getFivePrimeEndNuc2D();
		endNuc = this.getEntryHelix2D().getThreePrimeStartNuc2D();
	}
	if (startNuc == null)
		throw new Exception("ERROR in RNACycle2D.format(boolean): No startNuc");
	if (endNuc == null)
		throw new Exception("ERROR in RNACycle2D.format(boolean): No endNuc");
	if (debugOn) debug("FORMATTING CYCLE: "  + startNuc.getID() + " " + endNuc.getID());
	if (debugOn) debug("cycleArcDistance: " + this.getCycleArcDistance());

	// START find cmp0, cmp1 nucs
	// try and find a suitable pair of nucs to compare distances
	// NEED to clean up. I think this could be a lot easier by look at 1st
	// exit helix 3' end nuc and then nuc + 1
	Nuc2D cmp0Nuc = null;
	Nuc2D cmp1Nuc = null;
	RNAHelix2D probeHelix = new RNAHelix2D();
	RNABasePair2D basePair = new RNABasePair2D();
	Nuc2D probeNuc = startNuc;
	// Nuc2D firstValidNuc = sstr.getFirstNonNullNuc2D();
	Nuc2D firstValidNuc = (Nuc2D)nucCollection.getFirstNonNullNuc();

	// CYCLES
	Vector cycleNucList = this.getCycleNucs();
	for (int i = 0;i < cycleNucList.size();i++)
	{
		probeNuc = (Nuc2D)cycleNucList.elementAt(i);
		if (debugOn) debug("PROBENUC now: " + probeNuc.getID());
		if (probeNuc.isSingleStranded())
		{
			if ((cmp0Nuc == null) && (cmp1Nuc == null))
			{
				cmp0Nuc = probeNuc;
			}
			else if ((cmp0Nuc != null) && (cmp1Nuc == null) &&
				(probeNuc.getID() == cmp0Nuc.getID() + 1))
			{
				cmp1Nuc = probeNuc;
				break;
			}
			else
			{
				cmp0Nuc = cmp1Nuc = null;
			}
		}
		else if (probeNuc.isSingleBasePairHelix())
		{
			basePair.set(probeNuc);
			if (probeNuc == basePair.getFivePrimeNuc())
			{
				if (this.atCycle0() && (probeNuc == startNuc) &&
					(probeNuc == firstValidNuc))
				{
					// then entering single BP exit helix at level 0
				}
				else if (this.atCycle0())
				{
					// probeNuc = probeNuc.getBasePair();
				}
				else if (probeNuc == this.getEntryHelix().getFivePrimeEndNuc())
				{
					// then exiting single BP entry helix
				}
				else
				{
					// then entering single BP exit helix
				}
			}
			else if (probeNuc == basePair.getThreePrimeNuc())
			{
				if ((!this.atCycle0()) &&
					(probeNuc != this.getEntryHelix().getThreePrimeStartNuc()))
				{
					// then in exiting exit helix
				}
				else if (this.atCycle0())
				{
					// then in exiting exit helix
				}
				else
				{
					// entering entry helix
				}
			}
		}
		else
		{
			probeHelix.set(probeNuc);
			if (probeNuc == probeHelix.getFivePrimeEndNuc())
			{
				// then in exiting entry helix; don't add
			}
			else if (probeNuc == probeHelix.getFivePrimeStartNuc())
			{
				// then in entering exit helix
				// if (probeNuc != startNuc)
					// dist += nn1Dist;
			}
			else if (probeNuc == probeHelix.getThreePrimeEndNuc())
			{
				// then in exiting exit helix
			}
			else if (probeNuc == probeHelix.getThreePrimeStartNuc())
			{
				// then in entering entry helix; don't count
				// if (probeNuc != startNuc)
					// dist += nn1Dist;
			}
		}
	}

	if ((cmp0Nuc == null) || (cmp1Nuc == null) ||
		cmp1Nuc.getID() != cmp0Nuc.getID() + 1)
	// then look for a pair of contiguous nucs single stranded or not;
	// do this by looking for first exit helix 3' end nuc and next nuc
	{
		if (this.atCycle0())
		{
			if (startNuc.isSingleStranded() && startNuc.nextNuc().isBasePair())
			{
				cmp0Nuc = startNuc;
				cmp1Nuc = startNuc.nextNuc2D();
			}
			else
			{
				cmp0Nuc = this.getFirstExitHelix2D().getThreePrimeEndNuc2D();
				// throw new Exception("MADE IT: " + cmp0Nuc.getID() + ":" + startNuc.getID() + ":" + endNuc.getID());
				if (cmp0Nuc.isSingleBasePairHelix())
				{
					basePair.set(cmp0Nuc);
					cmp0Nuc = basePair.getThreePrimeNuc2D();
				}
				cmp1Nuc = cmp0Nuc.nextNonNullNuc2D();
			}
		}
		else
		{
			// if (this.isHelicalRun() && (this.getCycleSSNucCount() == 1))
			if (this.isBulgedNucCycle())
			{
				// in a bulged nuc cycle
				for (int i = 0;i < cycleNucList.size();i++)
				{
					Nuc2D probeBulgeCycleNuc = (Nuc2D)cycleNucList.elementAt(i);
					if (probeBulgeCycleNuc.isSingleStranded())
					{
						cmp0Nuc = probeBulgeCycleNuc;
						Nuc2D testNuc = cmp0Nuc.nextNonNullNuc2D();
						if (testNuc == this.getEntryHelix2D().getThreePrimeStartNuc2D())
						{
							cmp1Nuc = testNuc;
							break;
						}
						testNuc = cmp0Nuc.lastNonNullNuc2D();
						if (testNuc == this.getEntryHelix2D().getFivePrimeEndNuc2D())
						{
							cmp1Nuc = testNuc;
							break;
						}
						throw new ComplexException(
							"Error in RNACycle2D.format(boolean)",
							ComplexDefines.RNA_LEVEL_ERROR +
								ComplexDefines.FORMAT_ERROR,
							ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
							"cmp nucs not found in bulged nuc cycle: ");
					}
				}
			}
			else
			{
				cmp0Nuc = this.getEntryHelix2D().getFivePrimeEndNuc2D();
				if (cmp0Nuc.isSingleBasePairHelix())
				{
					basePair.set(cmp0Nuc);
					cmp0Nuc = basePair.getFivePrimeNuc2D();
				}
				cmp1Nuc = cmp0Nuc.nextNonNullNuc2D();
			}
		}
	}

	if (cmp0Nuc == null)
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp0Nuc is null");

	if (cmp1Nuc == null)
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp1Nuc is null");

	if (RNABasePair.nucsAreBasePaired(cmp0Nuc, cmp1Nuc))
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp nucs are basepaired together");
	
	// have to make sure cmp0Nuc and cmp1Nuc are cycle nucs
	// pattern after printCycles when printCycleNucs is fixed
	if (!this.getCycleNucs().contains(cmp0Nuc))
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp0Nuc: " + cmp0Nuc.getID() + " not a cycle nuc");
	if (!this.getCycleNucs().contains(cmp0Nuc))
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp0Nuc: " + cmp0Nuc.getID() + " not a cycle nuc");

	if (!( (cmp1Nuc.getID() == cmp0Nuc.getID() + 1) || (cmp1Nuc.getID() == cmp0Nuc.getID() - 1) ))
		throw new ComplexException(
			"Error in RNACycle2D.format(boolean)",
			ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_LEVEL_INVALID_CMP_NUC_ERROR_MSG,
			"cmp0Nuc, cmp1Nuc: " + cmp0Nuc.getID() + ", " + cmp1Nuc.getID() + " not contiguous");
	// END find cmp0, cmp1 nucs

	double largestVal = 100000.0;
	double smallestVal = 10.0;
	double midVal = (smallestVal + largestVal)/2.0;

	double nn1Dist = this.getRNANucToNextNucDistance();
	double tolerance = this.getDistanceTolerance();
	double upperNucTolerance = nn1Dist + tolerance;
	double lowerNucTolerance = nn1Dist - tolerance;
	double precision = 1000.0;

	if (debugOn) debug("arcSecant.length: " + this.getArcSecant().length());
	if (debugOn) debug("arcSecant.length: " + this.getArcSecant().length());
	if (debugOn) debug("final CycleArcDistance: " + this.getCycleArcDistance());
	if (debugOn) debug("final nn1Dist: " + nn1Dist);	

	debug("cmp0,1 nuc: " + cmp0Nuc.getID() + " " + cmp1Nuc.getID());
	while (true)
	{
		debug("Trying: " + smallestVal + " " + largestVal);
		midVal = (smallestVal + largestVal)/2.0;
		this.formatCycle(startNuc, endNuc, midVal, clockWiseFormat);
		double testNucDist = cmp0Nuc.getPoint2D().distance(cmp1Nuc.getPoint2D());
		if ((testNucDist >= lowerNucTolerance) && (testNucDist <= upperNucTolerance))
			break;
		if (testNucDist < nn1Dist)
			smallestVal = midVal;
		else if (testNucDist > nn1Dist)
			largestVal = midVal;

	} // end while(true)
}
*/

public void
formatCycle(Nuc2D startNuc, Nuc2D endNuc, double midPtDist, boolean clockWiseFormat)
throws Exception
{
	// case 0: startNuc and endNuc non basepaired
	if (!((startNuc.isBasePair()) || (endNuc.isBasePair())))
	{
		if (debugOn) debug("FORMATTING case 0: " + startNuc + ":" + endNuc);
	}
	// case 1: startNuc and endNuc both basepaired but not together
	else if (startNuc.isBasePair() && endNuc.isBasePair() &&
		(startNuc.getBasePair() != endNuc))
	{
		if (debugOn) debug("FORMATTING case 1: " + startNuc + ":" + endNuc);
	}
	// case 2: startNuc and endNuc both basepaired together
	else if (startNuc.isBasePair() && endNuc.isBasePair() &&
		(startNuc.getBasePair() == endNuc))
	{
		if (debugOn) debug("FORMATTING case 2: " + startNuc + ":" + endNuc);
	}
	// case 3: startNuc not basepaired and endNuc basepaired
	else if ((!startNuc.isBasePair()) && endNuc.isBasePair())
	{
		if (debugOn) debug("FORMATTING case 3: " + startNuc + ":" + endNuc);
	}
	// case 4: startNuc basepaired and endNuc not basepaired
	else if (startNuc.isBasePair() && (!endNuc.isBasePair()))
	{
		if (debugOn) debug("FORMATTING case 4: " + startNuc + ":" + endNuc);
	}
	else
	{
		throw new Exception("Error in RNACycle2D.formatCycle(Nuc2D, Nuc2D, double, boolean, boolean): unknown case");
	}

	// formats helices only; doesn't continue on.
	this.formatExitHelices(clockWiseFormat);

	this.formatCycle(midPtDist, clockWiseFormat);
}

public void
formatCycle(double midPtDist, boolean clockWiseFormat)
throws Exception
{
	if (debugOn) debug("midPtDist: " + midPtDist);
	if (debugOn) debug("getPerpendicularMidPtToHeadRay(): " +
		this.getPerpendicularMidPtToHeadRay());
	this.formatCycle(
		this.getPerpendicularMidPtToHeadRay().ptAtDistance(midPtDist),
		clockWiseFormat);
}

public void
formatCycle(Point2D centerPt, boolean clockWiseFormat)
throws Exception
{
	this.formatCycle(centerPt, clockWiseFormat, true, false);
}

public void
formatCycle(Point2D centerPt, boolean clockWiseFormat,
	boolean reformatExitHelices, boolean translateOnly)
throws Exception
{
	BLine2D arcSecant = this.getArcSecant();
	double radius = centerPt.distance(arcSecant.getP1());

	this.formatCycle(centerPt, radius, clockWiseFormat, reformatExitHelices, translateOnly);
}

public void
formatCycle(Point2D centerPt, double radius, boolean clockWiseFormat,
	boolean reformatExitHelices, boolean translateOnly)
throws Exception
{
	SSData2D sstr = (SSData2D)this.getParentCollection();
	// the line between delineating nucs of singleStrand
	// BLine2D arcSecant = this.getArcSecant();
	// if (debugOn) debug("arcSecant: " + arcSecant);

	if (debugOn) debug("centerPt: " + centerPt);

	// double radius = centerPt.distance(arcSecant.getP1());
	if (debugOn) debug("radius: " + radius);
	double startAngle = this.getArcStartAngle(centerPt, this.getArcSecant());
	// if (this.atCycle0()) debug("HERE 4: " + this.getArcSecant());
	if (debugOn) debug("startAngle: " + startAngle);
	double endAngle = this.getArcEndAngle(centerPt, this.getArcSecant());
	if (debugOn) debug("endAngle: " + endAngle);

	double entryHelixAngleInc =
		this.getArcAngle(centerPt, this.getArcSecant(), clockWiseFormat);
	if (debugOn) debug("entryHelixAngleInc: " + entryHelixAngleInc);
	double exitHelicesAngleInc = getExitHelicesAngleInc(radius, debugOn);
	if (debugOn) debug("exitHelicesAngleInc: " + exitHelicesAngleInc);

	double totalAngle = 0.0;
	if (clockWiseFormat)
		totalAngle = 360.0 - entryHelixAngleInc - exitHelicesAngleInc;
	else
		totalAngle = 360.0 - entryHelixAngleInc - exitHelicesAngleInc;
	if (totalAngle > 360.0)
		totalAngle -= 360.0;
	if (debugOn) debug("totalAngle: " + totalAngle);

	// count how many single stranded nucs in cycle
	int validNucCount = this.getCycleSSNucCount();
	if (debugOn) debug("getCycleSSNucCOunt: " + this.getCycleSSNucCount());

	// if level 0 then check to see if endpts are single stranded. consider
	// as delineate nucs if so.
	if (this.atCycle0())
	{
		// debug("arcSecant: " + this.getArcSecant());
		if (this.getFivePrimeNuc().isSingleStranded())
		{
			this.getFivePrimeNuc2D().setXY(
				this.getArcSecant().getP1().getX(),
				this.getArcSecant().getP1().getY());
			validNucCount--;
		}
		if (this.getThreePrimeNuc().isSingleStranded())
		{
			this.getThreePrimeNuc2D().setXY(
				this.getArcSecant().getP2().getX(),
				this.getArcSecant().getP2().getY());
			validNucCount--;
		}
	}
	validNucCount += this.getExitHelicesCount();
	if (debugOn) debug("validNucCount: " + validNucCount);
	double nucAngleInc = (totalAngle/(double)(validNucCount + 1));
	if (debugOn) debug("nucAngleInc: " + nucAngleInc);
	if (debugOn) debug("atCycle0: " + this.atCycle0());

	NucNode startNuc = null;
	NucNode endNuc = null;
	if (this.atCycle0())
	{
		if (this.getFivePrimeNuc().isSingleStranded())
			startNuc = this.getFivePrimeNuc().nextNonNullNuc();
		else
			startNuc = this.getFivePrimeNuc();
		if (this.getThreePrimeNuc().isSingleStranded())
			endNuc = this.getThreePrimeNuc().lastNonNullNuc();
		else
			endNuc = this.getThreePrimeNuc();
	}
	else
	{
		startNuc = this.getFivePrimeNuc().nextNonNullNuc();
		endNuc = this.getThreePrimeNuc().lastNonNullNuc();
	}

	if (debugOn) debug("startNuc, endNuc: " + startNuc + ":" + endNuc);
	NucNode probeNuc = startNuc;
	RNAHelix2D currentExitHelix = null;

	RNASingleStrand2D testSingleStrand = new RNASingleStrand2D();
	RNAHelix2D previousHelix = this.getEntryHelix2D();
	RNAHelix2D probeHelix = getFirstExitHelix2D();
	BLine2D previousHelixAxis = null;
	Nuc2D next5PNuc = null;
	Nuc2D last3PNuc = null;
	int fivePSSCount = 0;
	int threePSSCount = 0;
	if (previousHelix != null)
	{
		if (previousHelix.isSingleBasePairHelix())
		{
			previousHelixAxis = previousHelix.getSingleBasePairHelixAxis(clockWiseFormat);
		}
		else
		{
			previousHelixAxis = previousHelix.getHelixAxis();
		}
		next5PNuc = previousHelix.getFivePrimeEndNuc2D().nextNonNullNuc2D();
		if ((next5PNuc != null) && next5PNuc.isSingleStranded())
		{
			testSingleStrand.set(next5PNuc);
			fivePSSCount = testSingleStrand.getNonDelineatedNucCount();
		}
		last3PNuc = previousHelix.getThreePrimeStartNuc2D().lastNonNullNuc2D();
		if ((last3PNuc != null) && last3PNuc.isSingleStranded())
		{
			testSingleStrand.set(last3PNuc);
			threePSSCount = testSingleStrand.getNonDelineatedNucCount();
		}
	}

	// look for case of 1 or 2 bulged nucs:
	if ((!this.atCycle0()) && (this.getExitHelicesCount() == 1) &&
		(fivePSSCount < 2) && (threePSSCount < 2))
	{
		// rnaHelixBaseDistance is the distances of bp'd nucs
		// in helix n->n+1 & n-1->n
		double extendDistance = this.getRNAHelixBaseDistance();

		/*
		// adjust extendDistance to special cases:
		if ((fivePSSCount < 3) && (threePSSCount < 3))
		{
		}
		else if ((fivePSSCount == 1) || (threePSSCount == 1))
		{
		}
		else
   		{
   			extendDistance *= 2.0;
   		}
		*/


		Point2D extendedPt = null;
		if (previousHelix.isSingleBasePairHelix())
			extendedPt = previousHelixAxis.ptAtDistance(extendDistance);
		else
			extendedPt = previousHelixAxis.ptAtDistance(
				previousHelixAxis.length() + extendDistance);

		BVector2d newTailPt = new BVector2d(extendedPt);
		if (reformatExitHelices)
		{
			if (this.getEntryHelix2D().isSingleBasePairHelix())
				probeHelix.format(newTailPt,
					this.getEntryHelix2D().getSingleBasePairHelixAngle(
						clockWiseFormat), clockWiseFormat);
			else
				probeHelix.format(newTailPt,
					this.getEntryHelix2D().getAngle(), clockWiseFormat);
		}
		else // shift subDomain over to new position
		{
			RNASubDomain2D subDomain = new RNASubDomain2D(probeHelix);
			subDomain.translate(probeHelix.getFivePrimeMidPt(),
				newTailPt.getPoint2D());
		}
		if ((next5PNuc != null) && next5PNuc.isSingleStranded())
		{
			testSingleStrand = new RNASingleStrand2D(next5PNuc);
			testSingleStrand.formatArc(clockWiseFormat);
		}
		if ((last3PNuc != null) && last3PNuc.isSingleStranded())
		{
			testSingleStrand = new RNASingleStrand2D(last3PNuc);
			testSingleStrand.formatArc(clockWiseFormat);
		}
	}
	else
	{
		double angleInc = 0.0;
		double halfHelixArc = 0.0;
		while (true)
		{
			// if (debugOn) debug("AT PROBENUC: " + probeNuc);
			angleInc += nucAngleInc;
			double halfBaseLength = 0.0;
			if (probeNuc.isSelfRefBasePair())
			{
				currentExitHelix = new RNAHelix2D(probeNuc);
				halfBaseLength = 0.5*currentExitHelix.getBaseRay().length();
				if (radius < halfBaseLength)
					halfHelixArc = 90.0;
				else
					halfHelixArc = MathDefines.RadToDeg*Math.asin(halfBaseLength/radius);
				angleInc += halfHelixArc;
			}
			double arcAngle = 0.0;
			if (clockWiseFormat)
				arcAngle = startAngle - angleInc;
			else
				arcAngle = startAngle + angleInc;
			if (arcAngle > 360.0)
				arcAngle -= 360.0;
			if (arcAngle < 0.0)
				arcAngle += 360.0;
			BVector2d newPt = MathUtil.polarCoordToPoint(radius, arcAngle);
			// if (this.atCycle0()) debug("HERE 2: " + arcAngle + " " + startAngle + " " + angleInc);

			if (probeNuc.isSelfRefBasePair())
			{
				if (debugOn) debug("FORMATTING HELIX: " + probeNuc.getID());

				// newPt is a little too far away, now bring back
				double correctDistance = radius;
				if (radius > halfBaseLength)
					correctDistance = Math.sqrt(radius*radius - halfBaseLength*halfBaseLength);
				Point2D correctedPt =
					(new BLine2D(newPt)).ptAtDistance(correctDistance);

				newPt.setXY(correctedPt.getX() + centerPt.getX(),
					correctedPt.getY() + centerPt.getY());
				if (reformatExitHelices)
				{
					// if (this.atCycle0()) debug("HERE 0: " + arcAngle);
					currentExitHelix.format(newPt, arcAngle, clockWiseFormat);
				}
				else // shift subDomain over to new position
				{
					RNASubDomain2D subDomain = new RNASubDomain2D(currentExitHelix);
					subDomain.translate(currentExitHelix.getFivePrimeMidPt(),
						newPt.getPoint2D());
					if (!translateOnly)
					{
						AffineTransform rotateAffTr = new AffineTransform();
						rotateAffTr.setToIdentity();
						rotateAffTr.rotate(
							MathDefines.DegToRad *
								(arcAngle - subDomain.getAngle()),
							newPt.getX(), newPt.getY());
						subDomain.transform(rotateAffTr);
					}
				}

				angleInc += halfHelixArc;
				probeNuc = probeNuc.getBasePair();
			}
			else
			{
				if (debugOn) debug("FORMATTING NUC: " + probeNuc.getID());
				((Nuc2D)probeNuc).setXY(newPt.getX() + centerPt.getX(),
					newPt.getY() + centerPt.getY());
			}

			if (probeNuc.equals(endNuc))
				break;
			probeNuc = probeNuc.nextNonNullNuc();
		}
	}
}

public void
formatExitHelices(boolean clockWiseFormat)
throws Exception
{
	if (this.getCycleHelices() == null) // no structure
		return;
	RNAHelix2D currentExitHelix = new RNAHelix2D();
	for (int i = 1;i <= this.getExitHelicesCount();i++)
	{
		currentExitHelix.set((Nuc2D)this.getCycleHelices().elementAt(i));
		// level.setParentCollection((NucCollection2D)this);
		currentExitHelix.setDistancesFromCollection((NucCollection)this.getParentCollection());
/*
debug("HB DIST: " + currentExitHelix.getRNAHelixBaseDistance());
debug("BP DIST: " + currentExitHelix.getRNABasePairDistance());
debug("MISM DIST: " + currentExitHelix.getRNAMisMatchBasePairDistance());
debug("NN1 DIST: " + currentExitHelix.getRNANucToNextNucDistance());
*/
		currentExitHelix.format(clockWiseFormat);
	}
}

// all exit helices angles combined
public double
getExitHelicesAngleInc(double radius, boolean debugOn)
throws Exception
{
	double angleInc = 0.0;
	if (this.getCycleHelices() == null) // no structure
		return (angleInc);
	// if (debugOn) debug("@@@@@@@ RADIUS @@@@@@@@: " + radius);
	for (int i = 1;i <= this.getExitHelicesCount();i++)
	{
		RNAHelix2D currentExitHelix =
			new RNAHelix2D((Nuc2D)this.getCycleHelices().elementAt(i));
		// if (debugOn) debug("@@@@@@@ BASE @@@@@@@@: " + currentExitHelix.getBaseRay());
		double halfBaseLength = 0.5*currentExitHelix.getBaseRay().length();
		// if (debugOn) debug("@@@@@@@ 1/2 BASE LENGTH @@@@@@@@: " + halfBaseLength);
		if (radius < halfBaseLength)
			angleInc += 180.0;
		else
			angleInc += (2.0*MathDefines.RadToDeg*Math.asin(halfBaseLength/radius));
		// if (debugOn) debug("@@@@@@@ ANGLEINC @@@@@@@@: " + angleInc);
	}
	return (angleInc);
}

// this version passes through midpt of entry helix end. Maybe get
// a circle from this circle that passes through nucs in 3' end of
// helix
public DrawCircleObject
getCycleCircle()
throws Exception
{
	DrawCircleObject cycleCircle = null;
	RNAHelix2D entryHelix = this.getEntryHelix2D();

	// NEED to provide level 0 cycleCircle
	if (entryHelix == null) // at level0
		return (null);

	RNAHelix2D exitHelix = null;

	// NEED to allow for case where single exit helix not flanked by single
	// strands
	/* NOT FINISHED
	if (entryHelix == null) // at level0
	{
		exitHelix = new RNAHelix2D((Nuc2D)this.getCycleHelices().elementAt(1));
		RNASingleStrand2D singleStrand = new RNASingleStrand2D(
			exitHelix.getFivePrimeStartNuc2D().lastNuc2D());

		Point2D pt1 = ??

		Point2D pt2 = exitHelix.getFivePrimeMidPt();

		Point2D tmpCenterPt = 
			MathUtil.getCircleCenterFrom3Pts(
				entryHelix.getThreePrimeMidPt(), pt1, pt2);
		cycleCircle = new DrawCircleObject(
			tmpCenterPt.getX(),
			tmpCenterPt.getY(),
			tmpCenterPt.distance(pt1), Color.green);

		this.resetCycleCircle(cycleCircle);
		return (cycleCircle);
	}
	*/

	if (this.getExitHelicesCount() < 2)
	{
		exitHelix = new RNAHelix2D((Nuc2D)this.getCycleHelices().elementAt(1));
		Point2D tmpCenterPt = new BLine2D(entryHelix.getThreePrimeMidPt(),
			exitHelix.getFivePrimeMidPt()).getMidPt();
		cycleCircle = new DrawCircleObject(tmpCenterPt,
			tmpCenterPt.distance(entryHelix.getThreePrimeMidPt()), Color.green);
	}
	else
	{
		/* Save for theoretical best fit
		Vector ptSet = new Vector();
		// this passes through nucs at end of entry helix
		ptSet.add(entryHelix.getThreePrimeMidPt());
		for (int i = 1;i <= this.getExitHelicesCount();i++)
		{
			exitHelix = new RNAHelix2D((Nuc2D)this.getCycleHelices().elementAt(i));
			ptSet.add(exitHelix.getFivePrimeMidPt());
		}
		// this does a theoretical best fit and doesn't work too good
		// DrawCircleObject cycleCircle = DrawCircleObject.bestFit(ptSet);
		*/

		// figure out a best circle from entry helix and first 2 exit helices
		exitHelix = new RNAHelix2D((Nuc2D)this.getCycleHelices().elementAt(1));
		Point2D pt1 = exitHelix.getFivePrimeMidPt();
		exitHelix = new RNAHelix2D((Nuc2D)this.getCycleHelices().elementAt(2));
		Point2D pt2 = exitHelix.getFivePrimeMidPt();
		Point2D tmpCenterPt = 
			MathUtil.getCircleCenterFrom3Pts(
				entryHelix.getThreePrimeMidPt(), pt1, pt2);
		cycleCircle = new DrawCircleObject(
			tmpCenterPt.getX(),
			tmpCenterPt.getY(),
			tmpCenterPt.distance(pt1), Color.green);
	}

	this.resetCycleCircle(cycleCircle);

	return (cycleCircle);
}

// reset the cycleCircle so that it is tangent to entryHelix 3' mipPt
public void
resetCycleCircle(DrawCircleObject cycleCircleObj)
throws Exception
{
	BLine2D entryHelixAxis = this.getEntryHelix2D().getHelixAxis();
	Point2D centerPt = entryHelixAxis.ptAtDistance(entryHelixAxis.length() +
		cycleCircleObj.getRadius());
	cycleCircleObj.setLocation(centerPt);
	cycleCircleObj.setLineWidth(0.5);
}

public void
setEditColor(Color editColor)
throws Exception
{
	if (editColor == null)
	{
		if (this.getItemListDelineators() != null)
			super.setEditColor(null);
		return;
	}

	// first color everything
	if (this.getItemListDelineators() != null)
		super.setEditColor(editColor);

	if (this.getEntryHelix2D() != null)
		this.getEntryHelix2D().setEditColor(Color.red);
}

// set nucs in sub domain after exit cycle helixs if not hairpin
public void
setCycleHelicesSubDomainHidden()
throws Exception
{
	if (this.getCycleHelices() == null) // no structure
		return;
	RNAHelix2D currentExitHelix = new RNAHelix2D();
	for (int i = 1;i <= this.getExitHelicesCount();i++)
	{
		currentExitHelix.set((Nuc2D)this.getCycleHelices().elementAt(i));
		if (currentExitHelix.isHairPin())
			continue;
		if (!currentExitHelix.getFivePrimeEndNuc2D().isSelfRefBasePair())
			continue;
		Nuc2D fpNuc = currentExitHelix.getFivePrimeEndNuc2D().nextNonNullNuc2D();
		if (fpNuc.getID() >= currentExitHelix.getThreePrimeStartNuc2D().getID())
			continue;
		SSData2D sstr = (SSData2D)this.getParentCollection();
		for (int nucID = fpNuc.getID();nucID < currentExitHelix.getThreePrimeStartNuc2D().getID();nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			nuc.setIsHidden(true);
		}
	}
}

/*************** DrawObject Implementation *******************/

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
	AffineTransform transform =
		((DrawObjectCollection)this.getParentCollection()).getG2Transform();
	/*
	g2.setTransform(((DrawObjectCollection)this.getParentCollection()).getG2Transform());
	*/
	g2.setTransform(transform);

	if (false)
	{
		if (this.getPerpendicularArcSecant() != null)
		{
			(new DrawLineObject(this.getPerpendicularArcSecant(), 1.0, Color.green)).draw(g2, constrainedArea);
			(new DrawCircleObject(this.getPerpendicularArcSecant().getP2(), 3.0, Color.red, false)).draw(g2, constrainedArea);
			g2.setTransform(transform);
		}

		/*
		Point2D centerPt =
			this.getPerpendicularMidPtToHeadRay().ptAtDistance(testMidPtDist);
		if ((centerPt != null) && (!Double.isNaN(centerPt.getX())) && (!Double.isNaN(centerPt.getY())))
			(new DrawCircleObject(centerPt, 2.0, Color.red)).draw(g2, constrainedArea);
		g2.setTransform(transform);
		*/

		/*
		if (perpNucPt != null)
		{
			(new DrawCircleObject(perpNucPt, 1.4, Color.red, false)).draw(g2, constrainedArea);
			g2.setTransform(transform);
		}
		if (perpNewPt != null)
		{
			(new DrawCircleObject(perpNewPt, 1.4, Color.blue, false)).draw(g2, constrainedArea);
			g2.setTransform(transform);
		}
		*/
		// (new DrawCircleObject(testNucPt, 1.4, Color.red, false)).draw(g2, constrainedArea);
		// g2.setTransform(transform);
	}

	SSData2D sstr = (SSData2D)this.getParentCollection();
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		int startNucID = ((NucNode)delineators.elementAt(i)).getID();
		int endNucID = ((NucNode)delineators.elementAt(i+1)).getID();
		for (int nucID = startNucID;nucID <= endNucID;nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			if (!nuc.isBasePair())
			{
				nuc.draw(g2, constrainedArea);
			}
			else if (nuc.isFivePrimeBasePair())
			{
				refBasePair.set(nuc);
				refBasePair.draw(g2, constrainedArea);
			}
		}
	}
}

/***************** END DRAWOBJECT IMPL **********************/

public void
printSecondaryStructure()
throws Exception
{

	if (this.getCycleHelices() == null) // no structure
		return;
	RNAHelix2D helix = new RNAHelix2D();
	for (int i = 0;i < this.getCycleHelices().size();i++)
	{
		helix.set((Nuc2D)this.getCycleHelices().elementAt(i));
		Nuc2D nuc = helix.getFivePrimeStartNuc2D();
		System.out.print(nuc.getID() + " " + nuc.getBasePair().getID() + " " + helix.length() + "\n");
	}
	System.out.println();
}

private static void
debug(String s)
{
	System.err.println("RNACycle2D-> " + s);
}
	
}
