package ssview;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import javax.vecmath.*;

import jimage.DrawObject;
import jimage.DrawObjectCollection;
import jimage.DrawCircleObject;
import jimage.DrawLineObject;
import util.*;
import util.math.*;

// rna single strand should have only one parent and each nuc joined
// to it's n-1, n, n+1 neighbor; unlike basepairs which can have
// two parents for the base pair interaction

public class
RNASingleStrand2D
extends RNASingleStrand
{
	
// arbitrarily long for now; for constructing arcs along a ray
public static final double halfRayDistance = 200.0;

public
RNASingleStrand2D()
throws Exception
{
}

// figures out endpts on it's own
public
RNASingleStrand2D(NucNode refNuc)
throws Exception
{
	/*
	debug("refNuc: " + refNuc);
	debug("refNuc.isFormatted: " + ((Nuc2D)refNuc).isFormatted());
	*/
	this.initNewRNASingleStrand(refNuc);
	this.set((Nuc2D)refNuc);
}

public
RNASingleStrand2D(NucNode fpNuc, NucNode tpNuc)
throws Exception
{
	this.initNewRNASingleStrand(fpNuc);
	this.set((Nuc2D)fpNuc, (Nuc2D)tpNuc);
}

public void
set(Nuc2D refNuc)
throws Exception
{
	this.setSecant(new BLine2D());
	super.set((NucNode)refNuc);
	this.setDistancesFromCollection(refNuc.getParentSSData2D());
	/*
	debug("getRNAHelixBaseDistance(): " + this.getRNAHelixBaseDistance());
	debug("getRNABasePairDistance(): " + this.getRNABasePairDistance());
	debug("getRNAMisMatchBasePairDistance(): " + this.getRNAMisMatchBasePairDistance());
	debug("getRNANucToNextNucDistance(): " + this.getRNANucToNextNucDistance());
	*/
	this.reset();
}

public void
set(Nuc2D fpNuc, Nuc2D tpNuc)
throws Exception
{
	this.setSecant(new BLine2D());
// debug("SETTING FPNUC: " + fpNuc.getID());
// debug("SETTING TPNUC: " + tpNuc.getID());
	super.set((NucNode)fpNuc, (NucNode)tpNuc);
	this.setDistancesFromCollection(fpNuc.getParentSSData2D());
	/*
	debug("getRNAHelixBaseDistance(): " + this.getRNAHelixBaseDistance());
	debug("getRNABasePairDistance(): " + this.getRNABasePairDistance());
	debug("getRNAMisMatchBasePairDistance(): " + this.getRNAMisMatchBasePairDistance());
	debug("getRNANucToNextNucDistance(): " + this.getRNANucToNextNucDistance());
	*/
	this.reset();
}

// parent will always be single rna strand
public SSData2D
getParentSSData2D()
{
	return ((SSData2D)this.getParentCollection());
}

public void
increment5PDelinNuc()
throws Exception
{
	Nuc2D fpDelinNuc = this.getFivePrimeDelineateNuc2D();
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Nuc2D tpDelinNuc = this.getThreePrimeDelineateNuc2D();
	if (fpDelinNuc.getID() >= (tpDelinNuc.getID() - 2))
		return;
	this.initNewRNASingleStrand(fpDelinNuc.nextNuc2D());
	this.set(fpDelinNuc.nextNuc2D(), tpDelinNuc);
}

public void
decrement5PDelinNuc()
throws Exception
{
	Nuc2D fpDelinNuc = this.getFivePrimeDelineateNuc2D();
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Nuc2D tpDelinNuc = this.getThreePrimeDelineateNuc2D();
	if (fpDelinNuc.isBasePair())
		return;
	if (tpDelinNuc.getID() < (fpDelinNuc.getID() + 2))
		return;
	this.initNewRNASingleStrand(fpDelinNuc.lastNuc2D());
	this.set(fpDelinNuc.lastNuc2D(), tpDelinNuc);
}

public void
increment3PDelinNuc()
throws Exception
{
	Nuc2D fpDelinNuc = this.getFivePrimeDelineateNuc2D();
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Nuc2D tpDelinNuc = this.getThreePrimeDelineateNuc2D();
	if (tpDelinNuc.isBasePair())
		return;
	if (tpDelinNuc.getID() < (fpDelinNuc.getID() - 3))
		return;
	this.initNewRNASingleStrand(tpDelinNuc.nextNuc2D());
	this.set(fpDelinNuc, tpDelinNuc.nextNuc2D());
}

public void
decrement3PDelinNuc()
throws Exception
{
	Nuc2D fpDelinNuc = this.getFivePrimeDelineateNuc2D();
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Nuc2D tpDelinNuc = this.getThreePrimeDelineateNuc2D();
	if (tpDelinNuc.getID() < (fpDelinNuc.getID() + 3))
		return;
	this.initNewRNASingleStrand(tpDelinNuc.lastNuc2D());
	this.set(fpDelinNuc, tpDelinNuc.lastNuc2D());
}

public void
reset()
throws Exception
{
	super.reset();

	// needs to be delineated for forming arcs:
	if ((!this.getFivePrimeDelineateNuc2D().getIsFormatted()) ||
		(!this.getThreePrimeDelineateNuc2D().getIsFormatted()))
		return;

	// needs to be delineated for forming arcs:
	this.resetSecant();

	this.setSecantMidPt(this.getSecant().getMidPt());

	// this has to remain midLengthNuc since it will fail if use
	// getRefNuc and getRefNuc is at an endpt of singlestrand
	if (this.getMidLengthNuc2D().getIsFormatted())
	{
		this.setIsClockWiseFormatted(MathUtil.ptRelationToRayInXYPlane(
			this.getMidLengthNuc2D().getPoint2D(),
			this.getSecant()) == -1);
	}
	else
	{
		// try and get from context
	}
	
	Point2D tmpMidPt = new Point2D.Double();
	Point2D rayTail = new Point2D.Double();
	// rayTail will be perp to arcSecant in opposite direction of arc
	this.getSecant().getPerpendicularPointAtT(
		0.5, tmpMidPt, rayTail, halfRayDistance, this.getIsClockWiseFormatted());
	BLine2D tmpRay = new BLine2D(rayTail, this.getSecantMidPt());
	Point2D rayHead = tmpRay.ptAtDistance(2.0*halfRayDistance);
	this.setPerpendicularArcSecant(new BLine2D(rayTail, rayHead));

	this.setPerpendicularMidPtToHeadRay(
		new BLine2D(this.getSecantMidPt(), rayHead));
}

// use when know formatting with a straight line
public void
resetNucLine()
throws Exception
{
	super.reset();

	// needs to be delineated for forming arcs:
	if ((!this.getFivePrimeDelineateNuc2D().getIsFormatted()) ||
		(!this.getThreePrimeDelineateNuc2D().getIsFormatted()))
		return;

	// Probably just needs to be set once for nucline; this is
	// especially true for editing nuc line where one end of
	// delin nuc is moving.
	// this.resetSecant();

	/*
	debug("5' DEL: " + this.getFivePrimeDelineateNuc2D());
	debug("5' : " + this.getFivePrimeNuc2D());
	debug("3' : " + this.getThreePrimeNuc2D());
	debug("3' DEL: " + this.getThreePrimeDelineateNuc2D());
	debug("secant: " + this.getSecant());
	*/

	this.setSecantMidPt(this.getSecant().getMidPt());
	/*
	debug("secant.midPt: " + this.getSecant().getMidPt());
	debug("getX,Y():: " + this.getX() + " " + this.getY());
	*/
}

private void
resetSecant()
throws Exception
{
	this.getSecant().setLine(
		this.getFivePrimeDelineateNuc2D().getPoint2D(),
		this.getThreePrimeDelineateNuc2D().getPoint2D());
}

public Nuc2D
getRefNuc2D()
{
      return ((Nuc2D)this.getRefNuc());
}

public void
setRefNuc(NucNode nuc)
throws Exception
{
	super.setRefNuc(nuc);
}

public Nuc2D
getMidLengthNuc2D()
{
	return ((Nuc2D)this.getMidLengthNuc());
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

public Nuc2D
getFivePrimeDelineateNuc2D()
{
    return ((Nuc2D)this.getFivePrimeDelineateNuc());
}

public Nuc2D
getThreePrimeDelineateNuc2D()
{
    return ((Nuc2D)this.getThreePrimeDelineateNuc());
}

private BLine2D secant = null;

// 5' -> 3' delineated nucs
public void
setSecant(BLine2D secant)
{
    this.secant = secant;
}

public BLine2D
getSecant()
{
	return (this.secant);
}

// 5' -> 3' non-delineated nucs
public BLine2D
getRay()
throws Exception
{
	return (new BLine2D(this.getFivePrimeNuc2D().getPoint2D(),
		this.getThreePrimeNuc2D().getPoint2D()));
}

public double
getAngle()
throws Exception
{
	return (this.getRay().angleInXYPlane());
}

public double
getDelineatedAngle()
throws Exception
{
	return (this.getSecant().angleInXYPlane());
}

private Point2D secantMidPt = null;

public void
setSecantMidPt(Point2D secantMidPt)
{
    this.secantMidPt = secantMidPt;
}

public Point2D
getSecantMidPt()
{
    return (this.secantMidPt);
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

// see if nucs are formated as arc
// arc for now is just non-straight line
public boolean
isWellFormattedArc()
throws Exception
{
	if (this.isStraightLine())
		return (false);

	/*
	Point2D centerPt = null;
	try
	{
		centerPt = MathUtil.getCircleCenterFrom3Pts(
			this.getFivePrimeDelineateNuc2D().getPoint2D(),
			this.getThreePrimeDelineateNuc2D().getPoint2D(),
			this.getFivePrimeNuc2D().getPoint2D());
	}
	catch (Exception e)
	{
		// most certainly dividing by 0 and therefore not an arc
		return (false);
	}
	*/
	Point2D centerPt = this.getCurrentCenterPt();
	if (centerPt == null)
		// most certainly dividing by 0 and therefore not an arc
		return (false);

	SSData2D sstr = this.getParentSSData2D();

	double radius = centerPt.distance(
		this.getFivePrimeDelineateNuc2D().getPoint2D());

	// NOW iterate through all strand nucs and check radius
	for (int nucID = this.getFivePrimeNuc().getID();
		nucID <= this.getThreePrimeNuc().getID();nucID++)
	{
		if (!MathUtil.precisionEquality(radius,
			sstr.getNuc2DAt(nucID).getPoint2D().distance(centerPt), 2))
		{
			/*
			debug("Not a good arc radius: " + radius + " nuc dist: " +
				sstr.getNuc2DAt(nucID).getPoint2D().distance(centerPt)
				+ " for nuc: " + nucID);
			*/
			return(false);
		}
	}

	double refNucDist = this.getMidLengthNuc2D().getThreePrimeRay().length();
	// debug("refNucDist: " + refNucDist);
	// NOW iterate through all strand nucs and check nuc->nuc distance
	for (int nucID = this.getFivePrimeNuc().getID();
		nucID < this.getThreePrimeNuc().getID();nucID++)
	{
		if (!MathUtil.precisionEquality(refNucDist,
			sstr.getNuc2DAt(nucID).getThreePrimeRay().length(), 1))
		{
			/*
			debug("Not a good arc nuc to nuc distance: " + refNucDist + " nuc dist: " +
				sstr.getNuc2DAt(nucID).getThreePrimeRay().length()
				+ " for nuc: " + nucID);
			*/
			return(false);
		}
	}

	return (true);
}

/* LEAVE FOR AWHILE:
// was singlestrand formatted as arced or straight line
private boolean formattedArced = false;

public void
setFormattedArced(boolean formattedArced)
{
    this.formattedArced = formattedArced;
}

public boolean
getFormattedArced()
{
    return (this.formattedArced);
}
*/

public boolean
isRefNucDelineator(Nuc2D refNuc)
{
	return (this.getFivePrimeDelineateNuc2D().equals(refNuc) ||
		this.getThreePrimeDelineateNuc2D().equals(refNuc));
}

public void
clearNonNaturalDelineators()
{
	SSData2D sstr = (SSData2D)this.getParentSSData2D();
	for (int nucID = this.getFivePrimeDelineateNuc2D().getID();nucID <= this.getThreePrimeDelineateNuc2D().getID();nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		if (nuc == null)
			continue;
		if (nuc.isNaturalSingleStrandDelineator())
			continue;
		nuc.setIsSingleStrandDelineator(false);
	}
}

// arc for now is just non-straight line
public boolean
isArc()
throws Exception
{
	return (!this.isStraightLine());
}

public boolean
isStraightLine()
throws Exception
{
	// distance from delineate ray to be considered on line
	double distanceCriteria = 1.0;

	SSData2D sstr = (SSData2D)this.getParentSSData2D();
	if (sstr == null)
		return (false);

	BLine2D line = new BLine2D(this.getFivePrimeDelineateNuc2D().getPoint2D(),
		this.getThreePrimeDelineateNuc2D().getPoint2D());
	if (line == null)
		return (false);

	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	for (int nucID = this.getFivePrimeNuc2D().getID();nucID <= tpNuc.getID();nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		if (nuc == null)
			return (false);
		if (!nuc.isFormatted())
			return (false);
		Point2D nucPt = nuc.getPoint2D();
		if (line.ptLineDist(nucPt) > distanceCriteria)
			return (false);
	}
	return (true);
}

// considered clock wise formatted if mid nuc in arc is to left
// of delineate nuc ray
private boolean isClockWiseFormatted = false;

public void
setIsClockWiseFormatted(boolean isClockWiseFormatted)
{
    this.isClockWiseFormatted = isClockWiseFormatted;
}

public boolean
getIsClockWiseFormatted()
throws Exception
{
    return (this.isClockWiseFormatted);
}

public boolean
isClockWiseFormatted()
throws Exception
{
	if (this.isStraightLine())
	{
		// try and figure out from surrounding nucs

		boolean clockWiseFormatted = true; // default to true
		RNAHelix2D helix = null;

		if (this.getFivePrimeDelineateNuc2D().isBasePair())
		{
			helix = new RNAHelix2D(this.getFivePrimeDelineateNuc2D());
			clockWiseFormatted = helix.isClockWiseFormatted();
		}
		else if (this.getThreePrimeDelineateNuc2D().isBasePair())
		{
			helix = new RNAHelix2D(this.getThreePrimeDelineateNuc2D());
			clockWiseFormatted = helix.isClockWiseFormatted();
		}

		// for now default to true until can figure out better criteria
		// for determining contexts handedness
		return (clockWiseFormatted);
	}

	return (MathUtil.ptRelationToRayInXYPlane(
		this.getRefNuc2D().getPoint2D(), this.getSecant()) == -1);
}

// current distance from delineate nuc ray perpendicular to arc
public double
getCurrentSecantToArcDistance()
throws Exception
{
	double radius = this.getCurrentRadius();
	BLine2D arcSecant = this.getSecant();
	Point2D centerPt = this.getCurrentCenterPt();
	if (centerPt == null)
		return (0.0);
	int side = MathUtil.ptRelationToRayInXYPlane(centerPt, arcSecant);
	if (side == 0)
		return (radius);

	if (this.getIsClockWiseFormatted())
	{
		if (side == 1)
			return (radius - arcSecant.ptLineDist(centerPt));
		if (side == -1)
			return (radius + arcSecant.ptLineDist(centerPt));
	}
	else
	{
		if (side == 1)
			return (radius + arcSecant.ptLineDist(centerPt));
		if (side == -1)
			return (radius - arcSecant.ptLineDist(centerPt));
	}
	return (0.0);
}

public Point2D
getCurrentCenterPt()
throws Exception
{
	if (this.isStraightLine())
		return (this.getSecantMidPt());
	try
	{
		Point2D centerPt = MathUtil.getCircleCenterFrom3Pts(
			this.getThreePrimeDelineateNuc2D().getPoint2D(),
			this.getMidLengthNuc2D().getPoint2D(),
			this.getFivePrimeDelineateNuc2D().getPoint2D());

		// check to see if reasonable distance to centerpt; the
		// value 2000.0 is very arbitrary for now. The correct way
		// to deal with this is to figure out if the arc of the
		// circle is so straight that a straight nuc line is formed.
		if (this.getMidLengthNuc2D().getPoint2D().distance(centerPt) >
			2000.0)
			return (this.getSecantMidPt());
		return (centerPt);
	}
	catch (Exception e)
	{
		/*
		debug("ERROR in RNASingleStrand2D.getCurrentCenterPt():" +
			e.toString() + ":");
		ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(
			new DataOutputStream(excptArray)));
		debug(new String(excptArray.toByteArray()));
		// return (null);
		*/
		// couldn't get circle centerpt, return arc secant midpt instead
		return (this.getSecantMidPt());
	}
}

// current distance from delineate nuc ray to centerpt of arc
public double
getCurrentCenterPtDistance()
throws Exception
{
	Point2D centerPt = this.getCurrentCenterPt();	
	if (centerPt == null)
		return (0.0);
	double dist = this.getSecantMidPt().distance(
		centerPt);
	// -1 == (pt left of ray), 0 == (pt on ray), 1 == (pt right of ray)
	if (getIsClockWiseFormatted())
	{
		if (MathUtil.ptRelationToRayInXYPlane(centerPt,
			this.getSecant()) == 1)
			dist = -dist;
	}
	else
	{
		if (MathUtil.ptRelationToRayInXYPlane(centerPt,
			this.getSecant()) == -1)
			dist = -dist;
	}

	return (dist);
}

public double
getCurrentRadius()
throws Exception
{
	Point2D centerPt = this.getCurrentCenterPt();
	if (centerPt == null)
		return (0.0);
	double radius = centerPt.distance(
		this.getSecant().getP1());
	return (radius);
}

// reformat an arc based on a pt shifted
public void
reformatArc(Point2D startNucPt, Point2D shiftedNucPt)
throws Exception
{
	Point2D centerPt = this.getCurrentCenterPt();
	BLine2D perpLine = this.getPerpendicularArcSecant();
	BLine2D arcSecant = this.getSecant();
	Point2D perpNucPt = perpLine.getPerpLinePtFromOffSetPt(startNucPt);
	Point2D perpNewPt = perpLine.getPerpLinePtFromOffSetPt(shiftedNucPt);

	double startNucPtDist = startNucPt.distance(centerPt);
	double shiftedNucPtDist = shiftedNucPt.distance(centerPt);

	double perpShiftedDist = perpNucPt.distance(perpNewPt);
	double dist = getCurrentCenterPtDistance();

	if (startNucPtDist > shiftedNucPtDist) // heading in
	{
		if (this.getCurrentSecantToArcDistance() < 4.0)
			return;
		dist -= perpShiftedDist;
	}
	else
	{
		dist += perpShiftedDist;
	}

	this.formatArc(dist, this.getIsClockWiseFormatted());
}

public void
reformatArc(Point2D centerPt, boolean clockWiseFormat)
throws Exception
{
	double dist = this.getSecantMidPt().distance(centerPt);	
	// -1 == (pt left of ray), 0 == (pt on ray), 1 == (pt right of ray)
	if (clockWiseFormat)
	{
		if (MathUtil.ptRelationToRayInXYPlane(
			centerPt, this.getSecant()) == 1)
			dist = -dist;
	}
	else
	{
		if (MathUtil.ptRelationToRayInXYPlane(
			centerPt, this.getSecant()) == -1)
			dist = -dist;
	}
	this.formatArc(dist, clockWiseFormat);
}

/* new attempt using vector
public void
reformatArc(Point2D tailPt, Point2D headPt)
throws Exception
{
	double currentDist = this.getCurrentCenterPtDistance();

	Point2D perpNucPt = this.getSecant().getPerpLinePtFromOffSetPt(((Nuc2D)this.getRefNuc()).getPoint2D());
	Point2D newPt = new Point2D.Double(localX, localY);
	Point2D perpNewPt = this.getSecant().getPerpLinePtFromOffSetPt(newPt);
	Point2D midPt = this.getSecantMidPt();
	double perpMidPtNucDist = midPt.distance(perpNucPt);
	double perpMidPtNewDist = midPt.distance(perpNewPt);

	double dist = 0.0;
	if (perpMidPtNucDist < perpMidPtNewDist)
		dist = currentDist + perpNucPt.distance(perpNewPt);
	else
		dist = currentDist - perpNucPt.distance(perpNewPt);
	this.formatArc(dist, getIsClockWiseFormatted());
}
*/

public void
reformatArc(boolean clockWiseFormat)
throws Exception
{
	Point2D centerPt = this.getCurrentCenterPt();
	if (centerPt == null)
		return;
	this.reformatArc(centerPt, clockWiseFormat);
}

public void
reformatArc()
throws Exception
{
	this.reformatArc(this.isClockWiseFormatted());
}

public void
reformatEditArc(Nuc2D pickedNuc, double shiftX, double shiftY)
throws Exception
{
	Point2D sveNucPt = new Point2D.Double();
	sveNucPt.setLocation(pickedNuc.getPoint2D());
	pickedNuc.shiftXY(shiftX, shiftY);
	Point2D newNucPt = new Point2D.Double();
	newNucPt.setLocation(pickedNuc.getPoint2D());
	pickedNuc.setXY(sveNucPt.getX(), sveNucPt.getY());
	this.reformatArc(sveNucPt, newNucPt);
}

public void
formatArc(double midPtDist, boolean clockWiseFormat)
throws Exception
{
	if (this.getNonDelineatedNucCount() < 1)
		return;
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(true);
	*/

	// the line between delineating nucs of singleStrand
	BLine2D arcSecant = this.getSecant();

	Point2D centerPt =
		this.getPerpendicularMidPtToHeadRay().ptAtDistance(midPtDist);
	double radius = centerPt.distance(arcSecant.getP1());
	this.formatArc(centerPt, radius, clockWiseFormat);
}

// this is bottom of all calls to reformatArc() and formatArc().
public void
formatArc(Point2D centerPt, double radius, boolean clockWiseFormat)
throws Exception
{
	if (this.getNonDelineatedNucCount() < 1)
		return;
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(true);
	*/

	SSData2D sstr = this.getParentSSData2D();

	double angleTValue = 1.0/(double)this.getNonDelineatedNucCount();
	int probeNucID = this.getFivePrimeNuc().getID();

	double startAngle = 0.0;
	double endAngle = 0.0;
	double arcAngle = 0.0;

	startAngle = this.getArcStartAngle(centerPt, this.getSecant());
	endAngle = this.getArcEndAngle(centerPt, this.getSecant());

	// double angleInc = this.getAngleInc(startAngle, endAngle);
	// double totalAngle = 360.0 - endAngle + startAngle;
	double totalAngle = 0.0;
	if (clockWiseFormat)
		totalAngle = 360.0 - (endAngle - startAngle);
	else
		totalAngle = 360.0 - (startAngle - endAngle);
	if (totalAngle > 360.0)
		totalAngle -= 360.0;
	double angleInc = (totalAngle/(double)(this.getNonDelineatedNucCount() + 1));

	int i = 1;
	while (probeNucID <= this.getThreePrimeNuc().getID())
	{
		Nuc2D probeNuc = sstr.getNuc2DAt(probeNucID);
		probeNucID++;
		if (probeNuc == null)
		{
			i++;
			continue;
		}
		
		if (clockWiseFormat)
			arcAngle = startAngle - ((double)i * angleInc);
		else
			arcAngle = startAngle + ((double)i * angleInc);
		if (arcAngle > 360.0)
			arcAngle -= 360.0;
		if (arcAngle < 0.0)
			arcAngle += 360.0;
		BVector2d newPt = MathUtil.polarCoordToPoint(radius, arcAngle);
		probeNuc.setXY(newPt.getX() + centerPt.getX(),
			newPt.getY() + centerPt.getY());
		i++;
	}

	/* SHOWS nuc to nuc distance after a format
	for (int nucID = this.getFivePrimeNuc().getID();nucID < this.getThreePrimeNuc().getID();nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		Nuc2D nextNuc = sstr.getNuc2DAt(nucID + 1);
		debug("nuc->nuc+1 distance: " + nuc.getPoint2D().distance(nextNuc.getPoint2D()));
	}
	*/
}

public void
formatArc(double midPtDist)
throws Exception
{
	this.formatArc(midPtDist, this.getIsClockWiseFormatted());
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(true);
	*/
}

public boolean debugOn = false;

public void
formatArc(boolean clockWiseFormat, boolean debugOn)
throws Exception
{
	this.debugOn = debugOn;
	formatArc(clockWiseFormat);
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(true);
	*/
}

public void
formatArc()
throws Exception
{
	this.formatArc(this.isClockWiseFormatted());

	/* LEAVE FOR AWHILE:
	this.setFormattedArced(true);
	*/
}

public void
formatArc(boolean clockWiseFormat)
throws Exception
{
	// NEED to figure out a midPtDistance s.t. nuc->nuc+1 distance
	// is equal to getRNANucToNextNucDistance().

	double nn1Dist = this.getRNANucToNextNucDistance();
	if (debugOn) debug("nn1Dist 0: " + nn1Dist);
	Nuc2D nuc = this.getFivePrimeNuc2D();

	if (this.isSingleNuc() && this.isHairPin())
	{
		this.formatArc(this.getSecant().length()/6.0, clockWiseFormat);
		return;
	}
	if (this.isDoubleNucs() && this.isHairPin())
	{
		this.formatArc(this.getSecant().length()/6.0, clockWiseFormat);
		return;
	}
	if (this.isSingleNuc())
		nn1Dist -= (0.1 * nn1Dist);
	else if (this.isDoubleNucs())
		nn1Dist -= (0.2 * nn1Dist);
	if (debugOn) debug("nn1Dist 1: " + nn1Dist);

	Nuc2D nextNuc = nuc.nextNuc2D();
	if (nextNuc == null)
	{
		debug("nextNuc at nuc: " + nuc);
		debug("5' nuc: " + this.getFivePrimeNuc2D());
		debug("3' nuc: " + this.getThreePrimeNuc2D());
		throw new Exception("Error in RNASingleStrand2D.formatArc(boolean): " +
			" nextNuc is null");
	}

	/* Currently not used for anything:
	Nuc2D midNuc = this.getMidLengthNuc2D();
	if (debugOn) debug("nuc, nextNuc, midNuc: " + nuc.getID() + " " + nextNuc.getID() + " " + midNuc.getID());
	*/

	if (debugOn) debug("SS NUC COUNT: " + this.getNonDelineatedNucCount());
	double largestVal = 10000.0;
	double smallestVal = -10000.0;
	double midVal = 0.0;
	double tolerance = this.getDistanceTolerance();
	double upperNucDist = nn1Dist + tolerance;
	double lowerNucDist = nn1Dist - tolerance;
	double precision = 1000.0;

	if (debugOn) debug("5' DELINEATE NUC: " +
		this.getFivePrimeDelineateNuc2D());
	if (debugOn) debug("3' DELINEATE NUC: " +
		this.getThreePrimeDelineateNuc2D());
	if (debugOn) debug("getSecant: " + this.getSecant());
	if (debugOn) debug("ArcSecant length: " + this.getSecant().length());

	// can arc have getRNANucToNextNucDistance() as distance between nucs?
	/*
	boolean isArcSecantTooLong =
		(this.getSecant().length()/((double)this.getNonDelineatedNucCount()) <=
			this.getRNANucToNextNucDistance());
	
	if (!isArcSecantTooLong)
	{
		nn1Dist = this.getSecant().length()/(double)this.getNonDelineatedNucCount();
		upperNucDist = nn1Dist + tolerance;
		lowerNucDist = nn1Dist - tolerance;
	}
	*/
	double doubleArcNucCount = (double)this.getNonDelineatedNucCount() + 1.0;
	double totalArcDistance = doubleArcNucCount * this.getRNANucToNextNucDistance();
	if (debugOn) debug("totalArcDistance: " + totalArcDistance);
	double tmpTolerance = 1.0; // so doesn't degenerate to flat arc
	if (this.getSecant().length() >= totalArcDistance + tmpTolerance)
	{
		nn1Dist = this.getRNANucToNextNucDistance();
		while (this.getSecant().length() >= (nn1Dist * doubleArcNucCount) + tmpTolerance)
		{
			/*
			double diff = this.getSecant().length() - totalArcDistance;
			diff /= ((double)this.getNonDelineatedNucCount() + 1.0);
			nn1Dist = this.getSecant().length()/(double)this.getNonDelineatedNucCount()); 
			*/
			nn1Dist += this.getRNANucToNextNucDistance()/3.0;

			// double fudgeFactor = 6.0; // play around with 1, 6, 8
			// nn1Dist += fudgeFactor;

			upperNucDist = nn1Dist + tmpTolerance;
			lowerNucDist = nn1Dist - tmpTolerance;
		}
	}

	if (debugOn) debug("nn1Dist: " + nn1Dist);

	while (true)
	{
		this.formatArc(smallestVal, clockWiseFormat);
		double smallestNucDist = nuc.getPoint2D().distance(nextNuc.getPoint2D());
		if ((smallestNucDist >= lowerNucDist) && (smallestNucDist <= upperNucDist))
		{
			if (debugOn) debug("formatting arc at: " + smallestNucDist);
			break;
		}

		this.formatArc(largestVal, clockWiseFormat);
		double largestNucDist = nuc.getPoint2D().distance(nextNuc.getPoint2D());
		if ((largestNucDist >= lowerNucDist) && (largestNucDist <= upperNucDist))
		{
			if (debugOn) debug("formatting arc at: " + largestNucDist);
			break;
		}

		this.formatArc(midVal, clockWiseFormat);
		double midNucDist = nuc.getPoint2D().distance(nextNuc.getPoint2D());
		if ((midNucDist >= lowerNucDist) && (midNucDist <= upperNucDist))
		{
			if (debugOn) debug("formatting arc at: " + midNucDist);
			break;
		}
		
		if (
			MathUtil.precisionEquality(smallestNucDist, midNucDist, precision) &&
			MathUtil.precisionEquality(largestNucDist, midNucDist, precision) &&
			MathUtil.precisionEquality(smallestNucDist, largestNucDist, precision))
		{
			if (debugOn) debug("formatting arc at equality");
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
		else
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatArc()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.FORMAT_ERROR,
				"Invalid partition: " + 
					StringUtil.roundStrVal(smallestNucDist, 2) + " " +
					StringUtil.roundStrVal(midNucDist, 2) + " " +
					StringUtil.roundStrVal(largestNucDist, 2) + " " +
					StringUtil.roundStrVal(smallestVal, 2) + " " +
					StringUtil.roundStrVal(midVal, 2) + " " +
					StringUtil.roundStrVal(largestVal, 2)
					);
			/*
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
			*/
		}
		midVal = (smallestVal + largestVal)/2.0;
	}
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(true);
	*/
}

// use delineated nucs as fixed pts and generate line between them
public void
formatDelineatedNucLine()
throws Exception
{
	this.formatNucLine(
		this.getFivePrimeDelineateNuc2D().getPoint2D(),
		this.getThreePrimeDelineateNuc2D().getPoint2D(),
		this.getFivePrimeDelineateNuc2D().getID(),
		this.getThreePrimeDelineateNuc2D().getID());
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(false);
	*/
}

public void
formatNucLineFromAngle(double angle)
throws Exception
{
	BLine2D line = new BLine2D(
		this.getFivePrimeDelineateNuc2D().getPoint2D(),
		this.getThreePrimeDelineateNuc2D().getPoint2D());
	line.setRayFromAngle(angle);
	this.getThreePrimeDelineateNuc2D().setX(line.getP2().getX());
	this.getThreePrimeDelineateNuc2D().setY(line.getP2().getY());

	formatDelineatedNucLine();
	this.reset();
}

public void
formatNucLineFromAngleAndNucDistance(double angle, double nucDist)
throws Exception
{
	Nuc2D fpDelinNuc = this.getFivePrimeDelineateNuc2D();
	Nuc2D tpDelinNuc = this.getThreePrimeDelineateNuc2D();

	if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
	{
		if (fpDelinNuc.isBasePair())
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatNucLineFromAngleAndNucDistance()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.FORMAT_SINGLESTRAND_ERROR,
				ComplexDefines.FORMAT_SINGLESTRAND_ERROR_MSG,
				"Can't move nuc: " + fpDelinNuc.getID() + ", " +
					" since basepaired; need to set another single strand delineate nuc to format");
		}
		BLine2D line = new BLine2D(
			tpDelinNuc.getPoint2D(),
			fpDelinNuc.getPoint2D());
		angle += 180.0;
		if (angle >= 360.0)
			angle -= 360.0;
		line.setRayFromAngleAndLength(angle, nucDist * ((double)this.getNonDelineatedNucCount() + 1.0));
		fpDelinNuc.setX(line.getP2().getX());
		fpDelinNuc.setY(line.getP2().getY());
	}
	else if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
	{
		if (fpDelinNuc.isBasePair())
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatNucLineFromAngleAndNucDistance()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.FORMAT_SINGLESTRAND_ERROR,
				ComplexDefines.FORMAT_SINGLESTRAND_ERROR_MSG,
				"Can't move nuc: " + fpDelinNuc.getID() + ", " +
					" since basepaired; need to set another single strand delineate nuc to format");
		}
		if (tpDelinNuc.isBasePair())
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatNucLineFromAngleAndNucDistance()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.FORMAT_SINGLESTRAND_ERROR,
				ComplexDefines.FORMAT_SINGLESTRAND_ERROR_MSG,
				"Can't move nuc: " + tpDelinNuc.getID() + ", " +
					" since basepaired; need to set another single strand delineate nuc to format");
		}
		BLine2D line = new BLine2D(
			fpDelinNuc.getPoint2D(),
			tpDelinNuc.getPoint2D());
		line.setFromAngleAndLength(angle, nucDist * ((double)this.getNonDelineatedNucCount() + 1.0));
		fpDelinNuc.setX(line.getP1().getX());
		fpDelinNuc.setY(line.getP1().getY());
		tpDelinNuc.setX(line.getP2().getX());
		tpDelinNuc.setY(line.getP2().getY());
	}
	else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
	{
		if (tpDelinNuc.isBasePair())
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatNucLineFromAngleAndNucDistance()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.FORMAT_SINGLESTRAND_ERROR,
				ComplexDefines.FORMAT_SINGLESTRAND_ERROR_MSG,
				"Can't move nuc: " + tpDelinNuc.getID() + ", " +
					" since basepaired; need to set another single strand delineate nuc to format");
		}
		BLine2D line = new BLine2D(
			fpDelinNuc.getPoint2D(),
			tpDelinNuc.getPoint2D());
		line.setRayFromAngleAndLength(angle, nucDist * ((double)this.getNonDelineatedNucCount() + 1.0));
		tpDelinNuc.setX(line.getP2().getX());
		tpDelinNuc.setY(line.getP2().getY());
	}

	formatDelineatedNucLine();
	this.reset();
}

// implies that user doesn't expect to have it reformated to
// delineate nucs (USE readjust for this).
// try and determine from refNuc, partion, etc., how to format nuc line
// that is being interactively edited. This implies that refNuc is in
// single strand and not at delineated nucs. This also implies that only
// non-delineate nucs will move unless at start or end of segment.
public void
formatEditNucLine(double shiftX, double shiftY, boolean anchorDelinNucs)
throws Exception
{
	Nuc2D fpDelinNuc = this.getFivePrimeDelineateNuc2D();
	Nuc2D tpDelinNuc = this.getThreePrimeDelineateNuc2D();
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(false);
	*/

	if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
	{
		if (fpDelinNuc.isBasePair())
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatEditNucLine()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.FORMAT_SINGLESTRAND_ERROR,
				ComplexDefines.FORMAT_SINGLESTRAND_ERROR_MSG,
				"Can't move nuc: " + fpDelinNuc.getID() + ", " +
					" since basepaired; need to set another single strand delineate nuc to format");
		}
		fpDelinNuc.shiftXY(shiftX, shiftY);
	}
	else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
	{
		if (tpDelinNuc.isBasePair())
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatEditNucLine()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.FORMAT_SINGLESTRAND_ERROR,
				ComplexDefines.FORMAT_SINGLESTRAND_ERROR_MSG,
				"Can't move nuc: " + tpDelinNuc.getID() + ", " +
					" since basepaired; need to set another single strand delineate nuc to format");
		}
		tpDelinNuc.shiftXY(shiftX, shiftY);
	}
	else // assume midpt
	{
		if (fpDelinNuc.isBasePair())
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatEditNucLine()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.FORMAT_SINGLESTRAND_ERROR,
				ComplexDefines.FORMAT_SINGLESTRAND_ERROR_MSG,
				"Can't move nuc: " + fpDelinNuc.getID() + ", " +
					" since basepaired; need to set another single strand delineate nuc to format");
		}
		fpDelinNuc.shiftXY(shiftX, shiftY);
		if (tpDelinNuc.isBasePair())
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatEditNucLine()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.FORMAT_SINGLESTRAND_ERROR,
				ComplexDefines.FORMAT_SINGLESTRAND_ERROR_MSG,
				"Can't move nuc: " + tpDelinNuc.getID() + ", " +
					" since basepaired; need to set another single strand delineate nuc to format");
		}
		tpDelinNuc.shiftXY(shiftX, shiftY);
	}
	this.formatEditNucLine(fpDelinNuc, tpDelinNuc);

	/*
	// case 0, fpDelinNuc, tpDelinNuc have structure (easiest case)
	if (fpDelinNuc.isBasePair() && tpDelinNuc.isBasePair())
	{
		if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
		{
			fpNuc.shiftXY(shiftX, shiftY);
			if (anchorDelinNucs)
				this.formatEditNucLine(fpNuc, tpDelinNuc);
			else
				this.formatEditNucLine(fpNuc, tpNuc);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
		{
			fpNuc.shiftXY(shiftX, shiftY);
			tpNuc.shiftXY(shiftX, shiftY);
			this.formatEditNucLine(fpNuc, tpNuc);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
		{
			tpNuc.shiftXY(shiftX, shiftY);
			if (anchorDelinNucs)
				this.formatEditNucLine(fpDelinNuc, tpNuc);
			else
				this.formatEditNucLine(fpNuc, tpNuc);
		}
		else
		{
			throw new Exception("Couldn't formatEditNucLine at 0: "
				+ this.getLinePartition());
		}
		return;
	}
	
	// case 1, fpDelinNuc is not a basepair but a startnuc;
	// tpDelinNuc is basePair or otherwise fixed (NEED to test this with
	// a structure that is just a straight line)
	if (((!fpDelinNuc.isBasePair()) && fpDelinNuc.isStartSegment()) &&
			tpDelinNuc.isBasePair())
	{
		if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
		{
			fpDelinNuc.shiftXY(shiftX, shiftY);
			if (anchorDelinNucs)
				this.formatEditNucLine(fpDelinNuc, tpDelinNuc);
			else
				this.formatEditNucLine(fpDelinNuc, tpNuc);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
		{
			fpDelinNuc.shiftXY(shiftX, shiftY);
			tpNuc.shiftXY(shiftX, shiftY);
			this.formatEditNucLine(fpDelinNuc, tpNuc);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
		{
			tpNuc.shiftXY(shiftX, shiftY);
			if (anchorDelinNucs)
				this.formatEditNucLine(fpDelinNuc, tpNuc);
			else
				this.formatEditNucLine(fpDelinNuc, tpNuc);
		}
		else
		{
			throw new Exception("Couldn't formatEditNucLine at 1: "
				+ this.getLinePartition());
		}
		return;
	}

	// case 2, tpDelinNuc is not a basepair but an endnuc;
	// fpDelinNuc is basePair or otherwise fixed (NEED to test this with
	// a structure that is just a straight line)
	if (((!tpDelinNuc.isBasePair()) && tpDelinNuc.isEndSegment()) &&
			fpDelinNuc.isBasePair())
	{
		if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
		{
			fpNuc.shiftXY(shiftX, shiftY);
			if (anchorDelinNucs)
				this.formatEditNucLine(fpNuc, tpDelinNuc);
			else
				this.formatEditNucLine(fpNuc, tpDelinNuc);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
		{
			fpNuc.shiftXY(shiftX, shiftY);
			tpDelinNuc.shiftXY(shiftX, shiftY);
			this.formatEditNucLine(fpNuc, tpDelinNuc);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
		{
			tpDelinNuc.shiftXY(shiftX, shiftY);
			if (anchorDelinNucs)
				this.formatEditNucLine(fpDelinNuc, tpDelinNuc);
			else
				this.formatEditNucLine(fpNuc, tpDelinNuc);
		}
		else
		{
			throw new Exception("Couldn't formatEditNucLine at 2: "
				+ this.getLinePartition());
		}
		return;
	}

	// case 3, straight nuc line with no structure at end pts
	if (fpDelinNuc.isStartSegment() && tpDelinNuc.isEndSegment())
	{
		if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
		{
			fpDelinNuc.shiftXY(shiftX, shiftY);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
		{
			fpDelinNuc.shiftXY(shiftX, shiftY);
			tpDelinNuc.shiftXY(shiftX, shiftY);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
		{
			tpDelinNuc.shiftXY(shiftX, shiftY);
		}
		else
		{
			throw new Exception("Couldn't formatEditNucLine at 3: "
				+ this.getLinePartition());
		}
		this.formatEditNucLine(fpDelinNuc, tpDelinNuc);
		return;
	}

	// case 4, straight nuc line with structure at 5' end
	if (fpDelinNuc.isBasePair() && !tpDelinNuc.isBasePair())
	{
		if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
		{
			fpNuc.shiftXY(shiftX, shiftY);
			this.formatEditNucLine(fpNuc, tpDelinNuc);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
		{
			fpNuc.shiftXY(shiftX, shiftY);
			tpNuc.shiftXY(shiftX, shiftY);
			this.formatEditNucLine(fpNuc, tpNuc);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
		{
				tpNuc.shiftXY(shiftX, shiftY);
				this.formatEditNucLine(fpDelinNuc, tpNuc);
		}
		else
		{
			throw new Exception("Couldn't formatEditNucLine at 3: "
				+ this.getLinePartition());
		}
		return;
	}

	throw new Exception("Couldn't formatEditNucLine");
	*/
}

// explicitly format between 2 nucs while editing
public void
formatEditNucLine(Nuc2D nuc0, Nuc2D nuc1)
throws Exception
{
	this.formatEditNucLine(nuc0.getPoint2D(), nuc1.getPoint2D(),
		nuc0.getID(), nuc1.getID());
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(false);
	*/
}

public void
formatEditNucLine(Point2D startPt, Point2D endPt, int nuc0ID, int nuc1ID)
throws Exception
{
	if (nuc1ID - nuc0ID + 1 <= 2)
		return;
	this.formatNucsInLine(startPt, endPt, nuc0ID, nuc1ID);
	// might have to optimize for interactice editing (or else use something else):
	this.resetNucLine();
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(false);
	*/
}

// explicitly format between 2 nucs
public void
formatNucLine(Nuc2D nuc0, Nuc2D nuc1)
throws Exception
{
	this.formatNucLine(nuc0.getPoint2D(), nuc1.getPoint2D(),
		nuc0.getID(), nuc1.getID());
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(false);
	*/
}

// try and determine valid endpts to format
public void
formatNucLine(Point2D startPt, Point2D endPt)
throws Exception
{
	int nuc0ID = 0;
	int nuc1ID = 0;
	
	if (this.getFivePrimeDelineateNuc2D().isSingleStranded())
		nuc0ID = this.getFivePrimeDelineateNuc2D().getID();
	else
		nuc0ID = this.getFivePrimeNuc2D().getID();

	if (this.getThreePrimeDelineateNuc2D().isSingleStranded())
		nuc1ID = this.getThreePrimeDelineateNuc2D().getID();
	else
		nuc1ID = this.getThreePrimeNuc2D().getID();

	this.formatNucLine(startPt, endPt, nuc0ID, nuc1ID);
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(false);
	*/
}

// MAKE this the general case; provide others that represent all natural
// occuring nuclines:
// take a naturally occuring nuc line and format; figure out delineate pts.
public void
formatNucLine(Point2D startPt, Point2D endPt, int nuc0ID, int nuc1ID)
throws Exception
{
	if (nuc1ID - nuc0ID + 1 <= 2)
		return;
	this.formatNucsInLine(startPt, endPt, nuc0ID, nuc1ID);
	// might have to optimize for interactice editing (or else use something else):
	this.reset();
	/* LEAVE FOR AWHILE:
	this.setFormattedArced(false);
	*/
}

public void
formatNucsInLine(Point2D startPt, Point2D endPt, int nuc0ID, int nuc1ID)
throws Exception
{
	if (nuc1ID - nuc0ID + 1 <= 2)
		return;
	BLine2D nucRay = new BLine2D(startPt, endPt);
	double count = 0.0;

	double length = nucRay.length();
	double nucCount = (double)(nuc1ID - nuc0ID + 1) - 1.0;
	
	SSData2D sstr = this.getParentSSData2D();
	for (int nucID = nuc0ID;nucID <= nuc1ID;nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		if (nuc.isBasePair())
		{
			count += 1.0;
			continue; // don't reset a fixed base pair
		}
		// debug("FORMATTING NUC IN LINE: " + nuc);
		// for some reason can't premultiply nucCount*length
		nuc.setXY(nucRay.ptAtDistance((count/nucCount)*length));

		count += 1.0;
	}	
	// might have to optimize for interactice editing (or else use something else):
}

int SSNucBlockingFactorRowLength = 92;
int SSNucBlockingFactorRowSpace = 0;
int SSNucBlockingFactorCharSpace = 0;
int ptSize = 8;
int yGap = 7;
int xGap = 2;

public void
formatBlock()
throws Exception
{
	if (SSNucBlockingFactorRowSpace == 0)
		SSNucBlockingFactorRowSpace = ptSize + yGap;
			/*
			(int)Math.round(2.0 * sstr[FirstListNuc]->nucdeltay) + yGap;
			*/
	if (SSNucBlockingFactorCharSpace == 0)
		SSNucBlockingFactorCharSpace = ptSize + xGap;
			/*
			(int)Math.round(2.0 * sstr[FirstListNuc]->nucdeltax) + xGap;
			*/

	int nucCount = 0;
	double startX = 0.0;
	double currX = startX;
	double currY = 0.0;
	SSData2D sstr = this.getParentSSData2D();
	for (int nucID = this.getFivePrimeDelineateNuc().getID();
		nucID <= this.getThreePrimeDelineateNuc().getID();nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		if (nuc == null)
			continue;
		if (nuc.isBasePair())
		{
			throw new ComplexException(
				"Error in RNASingleStrand2D.formatBlock()",
				ComplexDefines.RNA_BASE_PAIR_ERROR +
					ComplexDefines.FORMAT_BASEPAIR_ERROR,
				ComplexDefines.FORMAT_SINGLESTRAND_ERROR_MSG,
				"nuc: " + nuc.getID() + ", " +
					" is basepaired; need to unbasepair to format");
		}
	
		if (nucCount % SSNucBlockingFactorRowLength == 0)
		{
			currX = startX;
			if (!nuc.equals(this.getFivePrimeDelineateNuc()))
				currY += SSNucBlockingFactorRowSpace;
		}
		nuc.setXY(currX, -currY);
		currX += SSNucBlockingFactorCharSpace;
		nucCount++;
	}	
}

public void
setIsSchematic(boolean isSchematic)
{
	for (Nuc2D nuc = this.getFivePrimeNuc2D();
		(nuc != null) && (nuc.getID() <= this.getThreePrimeNuc().getID());
			nuc = nuc.nextNuc2D())
	{
		nuc.setIsSchematic(isSchematic);
	}
}

/*************** DrawObject Implementation *******************/

public double
getX()
throws Exception
{
	return (this.getSecantMidPt().getX());	
}

public double
getY()
throws Exception
{
	return (this.getSecantMidPt().getY());	
}

public void
setX(double x)
throws Exception
{
	SSData2D sstr = this.getParentSSData2D();
	for (int nucID = this.getFivePrimeNuc().getID();
		nucID <= this.getThreePrimeNuc().getID();nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		nuc.shiftX(x);
	}
}

public void
setY(double y)
throws Exception
{
	SSData2D sstr = this.getParentSSData2D();
	for (int nucID = this.getFivePrimeNuc().getID();
		nucID <= this.getThreePrimeNuc().getID();nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		nuc.shiftY(y);
	}
}

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

	this.getFivePrimeDelineateNuc2D().setEditColor(Color.red);
	this.getThreePrimeDelineateNuc2D().setEditColor(Color.red);

	SSData2D sstr = this.getParentSSData2D();
	for (int nucID = this.getFivePrimeDelineateNuc2D().getID() + 1;
		nucID < this.getThreePrimeDelineateNuc2D().getID();nucID++)
	{
		Nuc2D refNuc = sstr.getNuc2DAt(nucID);
		if (refNuc == null)
			continue;
		refNuc.setEditColor(editColor);
	}
}

public void
setIsHidden(boolean hide)
throws Exception
{
	SSData2D sstr = this.getParentSSData2D();
	int startID = 0;
	int endID = 0;
	if (this.getFivePrimeDelineateNuc2D().isSingleStranded())
		startID = this.getFivePrimeDelineateNuc2D().getID();
	else
		startID = this.getFivePrimeNuc2D().getID();
	if (this.getThreePrimeDelineateNuc2D().isSingleStranded())
		endID = this.getThreePrimeDelineateNuc2D().getID();
	else
		endID = this.getThreePrimeNuc2D().getID();
	for (int nucID = startID;nucID <= endID;nucID++)
	{
		Nuc2D refNuc = sstr.getNuc2DAt(nucID);
		if (refNuc == null)
			continue;
		refNuc.setIsHidden(hide);
	}
}

public void
setHideForConstrain(boolean hideForConstrain)
throws Exception
{
	SSData2D sstr = this.getParentSSData2D();
	int startID = 0;
	int endID = 0;
	if (this.getFivePrimeDelineateNuc2D().isSingleStranded())
		startID = this.getFivePrimeDelineateNuc2D().getID();
	else
		startID = this.getFivePrimeNuc2D().getID();
	if (this.getThreePrimeDelineateNuc2D().isSingleStranded())
		endID = this.getThreePrimeDelineateNuc2D().getID();
	else
		endID = this.getThreePrimeNuc2D().getID();
	for (int nucID = startID;nucID <= endID;nucID++)
	{
		Nuc2D refNuc = sstr.getNuc2DAt(nucID);
		if (refNuc == null)
			continue;
		refNuc.setHideForConstrain(hideForConstrain);
	}
}

// not sure this is the way to do it
public void
update(Graphics2D g2)
throws Exception
{
	BRectangle2D rect = null;

	SSData2D sstr = this.getParentSSData2D();
	int startID = this.getFivePrimeDelineateNuc2D().getID();
	int endID = this.getThreePrimeDelineateNuc2D().getID();
	for (int nucID = startID;nucID <= endID;nucID++)
	{
		Nuc2D refNuc = sstr.getNuc2DAt(nucID);
		if (refNuc == null)
			continue;
		if (!refNuc.getIsFormatted())
			continue;
		refNuc.update(g2);
		if (rect == null)
		{
			rect = new BRectangle2D();
			rect.setRect(refNuc.getBoundingBox());
		}
		else
		{
			rect.add(refNuc.getBoundingBox());
		}
	}

	BRectangle2D newRect = new BRectangle2D(
		rect.getX() + this.getX(),
		rect.getY() - this.getY(),
		rect.getWidth(),
		rect.getHeight());

	this.setBoundingBox(newRect);
	this.setBoundingShape(this.getBoundingBox());

	this.setDeltaX(newRect.getX() + newRect.getWidth()/2.0);
	this.setDeltaY(newRect.getY() + newRect.getHeight()/2.0);
}

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
	if (constrainedArea != null)
	{
		if (!this.intersects(constrainedArea, g2))
			return;
		// debug("IN SSDRW " + this.getName() + ", intersects");
	}

	g2.translate(this.getX(), -this.getY());

	/*
	boolean inEditColor = (this.getRefNuc2D().getEditColor() != null);
	if (inEditColor)
	{
		// explicitly draw in case delineator below doesn't handle it
		if (!refNuc.equals(this.getFivePrimeDelineateNuc2D()))
	}
	*/

	// NEED to be able to go into a delineate nuc if it is a basepair
	// and clear it and draw it if need be

	SSData2D sstr = this.getParentSSData2D();
	int startID = this.getFivePrimeDelineateNuc2D().getID();
	int endID = this.getThreePrimeDelineateNuc2D().getID();
	for (int nucID = startID;nucID <= endID;nucID++)
	{
		Nuc2D refNuc = sstr.getNuc2DAt(nucID);
		if (refNuc == null)
			continue;
		refNuc.draw(g2, constrainedArea);
	}

	g2.translate(-this.getX(), this.getY());

	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.green);
		this.drawBoundingShape(g2);
	}
}


/*************** End DrawObject Implementation *******************/

private void
debug(String s)
{
	System.err.println("RNASingleStrand2D-> " + s);
}

}
