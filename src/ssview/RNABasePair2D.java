package ssview;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;

import jimage.DrawObject;
import jimage.DrawLineObject;
import util.math.*;
import util.*;

// info for one basepair in a structure
public class
RNABasePair2D
extends RNABasePair
{

public
RNABasePair2D()
throws Exception
{
	super();
}

// presumes that refNuc is basepaired else exception
public
RNABasePair2D(Nuc2D refNuc)
throws Exception
{
	this.set(refNuc);
}

// presumes that caller wants nucs to be base paired together
public
RNABasePair2D(Nuc2D nuc0, Nuc2D nuc1)
throws Exception
{
	this.initNewBasePair(nuc0, nuc1);
	this.set(nuc0);
}

// presumes that refNuc is basepaired else exception
public void
set(Nuc2D refNuc)
throws Exception
{
	super.set((NucNode)refNuc);
	this.setDistancesFromCollection(refNuc.getParentSSData2D());
}

public void
reformat()
throws Exception
{
	(new RNAHelix2D(this.getFivePrimeNuc2D())).reformat();
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

/*********** Base Pair Graphics ********************/

public Point2D
getMidPt()
throws Exception
{
	return (this.get5PBasePairRay().getMidPt());
}

// Try and get clockWise format info from context.
// I currently don't think it possible to tell if a single
// base pair is clock wise formatted. Seems like have to know some
// formatted nucs around the given BP.
// Should check forward first as a base pair could be part of
// a bigger structure like a helix, stacked helix, domain. if check
// backward first then more of a chance to get an arbitrarily
// formatted single stranded nuc.

public boolean
isClockWiseFormatted()
throws Exception
{
	Nuc2D nextNuc = this.getFivePrimeNuc2D().nextNonNullNuc2D();
	if (nextNuc.isSingleStranded())
		return((new RNASingleStrand2D(nextNuc)).getIsClockWiseFormatted());
	Nuc2D lastNuc = this.getFivePrimeNuc2D().lastNonNullNuc2D();
	if (lastNuc.isSingleStranded())
		return((new RNASingleStrand2D(lastNuc)).getIsClockWiseFormatted());

	nextNuc = this.getThreePrimeNuc2D().lastNonNullNuc2D();
	if (nextNuc.isSingleStranded())
		return((new RNASingleStrand2D(nextNuc)).getIsClockWiseFormatted());
	lastNuc = this.getThreePrimeNuc2D().nextNonNullNuc2D();
	if (lastNuc.isSingleStranded())
		return((new RNASingleStrand2D(lastNuc)).getIsClockWiseFormatted());

	// neither lastNuc or nextNuc is single stranded. assume nextNuc belongs
	// to a helix.
	return ((new RNAHelix2D(nextNuc)).isClockWiseFormatted());
}

// NEED to deprecate to length()
public double
distance()
throws Exception
{
	return (this.get5PBasePairRay().length());
}

public double
length()
throws Exception
{
	return (this.get5PBasePairRay().length());
}

// tailpt at 5p nuc, headpt at 3p nuc
public BLine2D
get5PBasePairRay()
throws Exception
{
	return (new BLine2D(
		this.getFivePrimeNuc2D().getPoint2D(),
		this.getThreePrimeNuc2D().getPoint2D()));
}

// tailpt at 3p nuc, headpt at 5p nuc
public BLine2D
get3PBasePairRay()
throws Exception
{
	return (new BLine2D(
		this.getThreePrimeNuc2D().getPoint2D(),
		this.getFivePrimeNuc2D().getPoint2D()));
}

// returns get5PBasePairRay() if non symbols
public BLine2D
getBPSymbolIntersectRay(double fpBPGap, double tpBPGap)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();

	if (fpNuc.isSchematic() && tpNuc.isSchematic())
	{
		if ((!fpNuc.isSymbol()) && (!tpNuc.isSymbol()))
		{
			return (this.get5PBasePairRay());
		}
		if ((fpNuc.isSymbol()) && (!tpNuc.isSymbol()))
		{
			BLine2D tmpLine = new BLine2D(this.get3PBasePairRay().getP1(),
				this.getFivePrimeIntersect());
			tmpLine.setRayFromLength(tmpLine.length() - fpBPGap);
			return (tmpLine);
		}
		if ((!fpNuc.isSymbol()) && (tpNuc.isSymbol()))
		{
			BLine2D tmpLine = new BLine2D(this.get5PBasePairRay().getP1(),
				this.getThreePrimeIntersect());
			tmpLine.setRayFromLength(tmpLine.length() - tpBPGap);
			return (tmpLine);
			/*
			return (new BLine2D(this.get5PBasePairRay().getP1(),
				this.getThreePrimeIntersect()));
			*/
		}
	}
	else if (fpNuc.isSchematic())
	{
		if (fpNuc.isSymbol())
		{
			BLine2D tmpLine = new BLine2D(
				this.getThreePrimeIntersect(),
				this.getFivePrimeIntersect());
			tmpLine.setRayFromLength(tmpLine.length() - fpBPGap);
			tmpLine.flipEndPts();
			tmpLine.setRayFromLength(tmpLine.length() - tpBPGap);
			tmpLine.flipEndPts();
			return (tmpLine);
		}
		else
		{
			BLine2D tmpLine = new BLine2D(this.get5PBasePairRay().getP1(),
				this.getThreePrimeIntersect());
			tmpLine.setRayFromLength(tmpLine.length() - fpBPGap);
			return (tmpLine);
		}
	}
	else if (tpNuc.isSchematic())
	{
		if (tpNuc.isSymbol())
		{
			BLine2D tmpLine = new BLine2D(
				this.getFivePrimeIntersect(),
				this.getThreePrimeIntersect());
			tmpLine.setRayFromLength(tmpLine.length() - tpBPGap);
			tmpLine.flipEndPts();
			tmpLine.setRayFromLength(tmpLine.length() - fpBPGap);
			tmpLine.flipEndPts();
			return (tmpLine);
		}
		else
		{
			BLine2D tmpLine = new BLine2D(this.get3PBasePairRay().getP1(),
				this.getFivePrimeIntersect());
			tmpLine.setRayFromLength(tmpLine.length() - tpBPGap);
			return (tmpLine);
		}
	}
	else debug("WHAT?");

	// must be the case that neither is symbol
	return (null);
}

// intersect pt with threePrimeRay() and fivePrimeNucs bounding box
public Point2D
getFivePrimeIntersect()
throws Exception
{
	double[] uArray = new double[2];
	int[] sideArray = new int[2];
	Point2D intersect0Pt = new Point2D.Double();
	Point2D intersect1Pt = new Point2D.Double();

	Rectangle2D rect = this.getFivePrimeNuc().getCenteredBoundingBox();

	boolean intersects = BLine2D.getRectangleRayIntersect(
		rect, this.get3PBasePairRay(),
		intersect0Pt, intersect1Pt, uArray, sideArray);

	if (!intersects)
		return (null);
	return (intersect0Pt);
}

// intersect pt with fivePrimeRay() and threePrimeNucs's bounding box
public Point2D
getThreePrimeIntersect()
throws Exception
{
	double[] uArray = new double[2];
	int[] sideArray = new int[2];
	Point2D intersect0Pt = new Point2D.Double();
	Point2D intersect1Pt = new Point2D.Double();

	Rectangle2D rect = this.getThreePrimeNuc().getCenteredBoundingBox();

	boolean intersects = BLine2D.getRectangleRayIntersect(
		rect, this.get5PBasePairRay(),
		intersect0Pt, intersect1Pt, uArray, sideArray);

	if (!intersects)
		return (null);
	return (intersect0Pt);
}

public void
setIsSchematic(boolean isSchematic)
{
	this.getFivePrimeNuc2D().setIsSchematic(isSchematic);
	this.getThreePrimeNuc2D().setIsSchematic(isSchematic);
}

/***************** Base Pair Symbol Stuff ********************************/

private void
drawBasePairSymbol(double fpBPGap, double tpBPGap, Graphics2D g2)
throws Exception
{
	// NEED to have a seperate bp symbol color
	g2.setColor(Color.black);

	if (this.isCanonical())
		this.drawDefaultCanonicalBPSymbol(fpBPGap, tpBPGap, g2);
	else if (this.isMisMatch())
		this.drawDefaultNonCanonicalBPSymbol(g2);
	else if (this.isWobble())
		this.drawDefaultClosedArcBPSymbol(g2);
	else if (this.isWeak())
		this.drawDefaultWeakBPSymbol(g2);
	else if (this.isPhosphate())
		this.drawDefaultPhosphateBPSymbol(g2);
}

private void
printPSBasePairSymbol(double fpBPGap, double tpBPGap, Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	// NEED to have a seperate bp symbol color
	g2.setColor(Color.black);

	if (this.isCanonical())
		this.printPSDefaultCanonicalBPSymbol(fpBPGap, tpBPGap, g2, psUtil);
	else if (this.isMisMatch())
		this.printPSDefaultNonCanonicalBPSymbol(g2, psUtil);
	else if (this.isWobble())
		this.printPSDefaultClosedArcBPSymbol(g2, psUtil);
	else if (this.isWeak())
		this.printPSDefaultWeakBPSymbol(g2, psUtil);
	else if (this.isPhosphate())
		this.printPSDefaultPhosphateBPSymbol(g2, psUtil);
}

private void
drawDefaultCanonicalBPSymbol(double fpBPGap, double tpBPGap, Graphics2D g2)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();

	// NEED to draw differently for different nuc atts (like if
	// fpNuc is a different schematic color than tpNuc
	if (fpNuc.isSchematic() || tpNuc.isSchematic())
	{
		this.drawDefaultCanonicalSchematicLine(g2, fpBPGap, tpBPGap);
	}
	else // regular non-schematicized basepair
	{
		this.drawDefaultCanonicalLineBPSymbol(g2);
	}
}

private void
printPSDefaultCanonicalBPSymbol(double fpBPGap, double tpBPGap, Graphics2D g2,
	PostScriptUtil psUtil)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();

	// NEED to draw differently for different nuc atts (like if
	// fpNuc is a different schematic color than tpNuc
	if (fpNuc.isSchematic() || tpNuc.isSchematic())
	{
		this.printPSDefaultCanonicalSchematicLine(g2, fpBPGap, tpBPGap, psUtil);
	}
	else // regular non-schematicized basepair
	{
		this.printPSDefaultCanonicalLineBPSymbol(g2, psUtil);
	}
}

private void
drawDefaultCanonicalSchematicLine(Graphics2D g2, double fpBPGap, double tpBPGap)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();

	if (fpNuc.isSchematic() && tpNuc.isSchematic())
	{
		// go half way and draw other bp line

		BLine2D symbolIntersectLine = this.getBPSymbolIntersectRay(fpBPGap, tpBPGap);
		BLine2D tmpLine = new BLine2D(symbolIntersectLine);
		g2.setStroke(new BasicStroke(
			(float)fpNuc.getSchematicBPLineWidth(),
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(fpNuc.getSchematicColor());
		tmpLine.setLine(
			tmpLine.getP1().getX(), -tmpLine.getP1().getY(),
			tmpLine.getMidPt().getX(), -tmpLine.getMidPt().getY());
		g2.draw(tmpLine);

		tmpLine.setLine(symbolIntersectLine);
		g2.setStroke(new BasicStroke(
			(float)tpNuc.getSchematicBPLineWidth(),
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(tpNuc.getSchematicColor());
		tmpLine.setLine(
			tmpLine.getMidPt().getX(), -tmpLine.getMidPt().getY(),
			tmpLine.getP2().getX(), -tmpLine.getP2().getY());
		g2.draw(tmpLine);

		return;
	}
	else if (fpNuc.isSchematic())
	{
		g2.setStroke(new BasicStroke(
			(float)fpNuc.getSchematicBPLineWidth(),
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(fpNuc.getSchematicColor());
	}
	else if (tpNuc.isSchematic())
	{
		g2.setStroke(new BasicStroke(
			(float)tpNuc.getSchematicBPLineWidth(),
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(tpNuc.getSchematicColor());
	}
	else
	{
		return;
	}

	BLine2D tmpLine = this.getBPSymbolIntersectRay(fpBPGap, tpBPGap);
	tmpLine.setLine(
		tmpLine.getP1().getX(), -tmpLine.getP1().getY(),
		tmpLine.getP2().getX(), -tmpLine.getP2().getY());
	g2.draw(tmpLine);
}

private void
printPSDefaultCanonicalSchematicLine(Graphics2D g2, double fpBPGap, double tpBPGap,
	PostScriptUtil psUtil)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();

	if (fpNuc.isSchematic() && tpNuc.isSchematic())
	{
		// go half way and draw other bp line

		BLine2D symbolIntersectLine = this.getBPSymbolIntersectRay(fpBPGap, tpBPGap);
		BLine2D tmpLine = new BLine2D(symbolIntersectLine);
		g2.setStroke(new BasicStroke(
			(float)fpNuc.getSchematicBPLineWidth(),
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(fpNuc.getSchematicColor());
		tmpLine.setLine(
			tmpLine.getP1().getX(), -tmpLine.getP1().getY(),
			tmpLine.getMidPt().getX(), -tmpLine.getMidPt().getY());
		g2.draw(tmpLine);

		tmpLine.setLine(symbolIntersectLine);
		g2.setStroke(new BasicStroke(
			(float)tpNuc.getSchematicBPLineWidth(),
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(tpNuc.getSchematicColor());
		tmpLine.setLine(
			tmpLine.getMidPt().getX(), -tmpLine.getMidPt().getY(),
			tmpLine.getP2().getX(), -tmpLine.getP2().getY());
		g2.draw(tmpLine);

		return;
	}
	else if (fpNuc.isSchematic())
	{
		g2.setStroke(new BasicStroke(
			(float)fpNuc.getSchematicBPLineWidth(),
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(fpNuc.getSchematicColor());
	}
	else if (tpNuc.isSchematic())
	{
		g2.setStroke(new BasicStroke(
			(float)tpNuc.getSchematicBPLineWidth(),
			BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(tpNuc.getSchematicColor());
	}
	else
	{
		return;
	}

	BLine2D tmpLine = this.getBPSymbolIntersectRay(fpBPGap, tpBPGap);
	tmpLine.setLine(
		tmpLine.getP1().getX(), -tmpLine.getP1().getY(),
		tmpLine.getP2().getX(), -tmpLine.getP2().getY());
	g2.draw(tmpLine);
}

private void
drawDefaultNonCanonicalBPSymbol(Graphics2D g2)
throws Exception
{
	this.drawDefaultOpenArcBPSymbol(g2);
}

private void
printPSDefaultNonCanonicalBPSymbol(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	this.printPSDefaultOpenArcBPSymbol(g2, psUtil);
}

private void
drawDefaultCanonicalLineBPSymbol(Graphics2D g2)
throws Exception
{
	this.drawDefaultLineBPSymbol(GraphicsUtil.thin1LineStroke, g2);
}

private void
printPSDefaultCanonicalLineBPSymbol(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	this.printPSDefaultLineBPSymbol(GraphicsUtil.thin1LineStroke, g2, psUtil);
}

private void
drawDefaultWeakBPSymbol(Graphics2D g2)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Point2D.Double pt1 = new Point2D.Double(
		fpNuc.getX(), -fpNuc.getY());
	Point2D.Double pt2 = new Point2D.Double(
		tpNuc.getX(), -tpNuc.getY());
	BLine2D nucLine = new BLine2D(pt1, pt2);

	g2.setStroke(GraphicsUtil.thin06LineStroke);
	Point2D newPt1 = nucLine.getPointAtT(BP_LINE_MULT);
	nucLine = new BLine2D(pt2, pt1);
	Point2D newPt2 = nucLine.getPointAtT(BP_LINE_MULT);
	Point2D midPt = nucLine.getMidPt();
	g2.draw(new Line2D.Double(newPt1, newPt1));
	g2.draw(new Line2D.Double(newPt2, newPt2));
	g2.draw(new Line2D.Double(midPt, midPt));
}

private void
printPSDefaultWeakBPSymbol(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Point2D.Double pt1 = new Point2D.Double(
		fpNuc.getX(), -fpNuc.getY());
	Point2D.Double pt2 = new Point2D.Double(
		tpNuc.getX(), -tpNuc.getY());
	BLine2D nucLine = new BLine2D(pt1, pt2);

	g2.setStroke(GraphicsUtil.thin06LineStroke);
	Point2D newPt1 = nucLine.getPointAtT(BP_LINE_MULT);
	nucLine = new BLine2D(pt2, pt1);
	Point2D newPt2 = nucLine.getPointAtT(BP_LINE_MULT);
	Point2D midPt = nucLine.getMidPt();
	g2.draw(new Line2D.Double(newPt1, newPt1));
	g2.draw(new Line2D.Double(newPt2, newPt2));
	g2.draw(new Line2D.Double(midPt, midPt));
}

private void
drawDefaultLineBPSymbol(Stroke stroke, Graphics2D g2)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Point2D.Double pt1 = new Point2D.Double(
		fpNuc.getX(), -fpNuc.getY());
	Point2D.Double pt2 = new Point2D.Double(
		tpNuc.getX(), -tpNuc.getY());
	BLine2D nucLine = new BLine2D(pt1, pt2);

	// g2.setStroke(GraphicsUtil.thin1LineStroke);
	g2.setStroke(stroke);
	Point2D.Double newPt1 = nucLine.getPointAtT(BP_LINE_MULT);
	nucLine = new BLine2D(pt2, pt1);
	Point2D.Double newPt2 = nucLine.getPointAtT(BP_LINE_MULT);
	g2.draw(new Line2D.Double(newPt1, newPt2));
}

private void
printPSDefaultLineBPSymbol(Stroke stroke, Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Point2D.Double pt1 = new Point2D.Double(
		fpNuc.getX(), fpNuc.getY());
	Point2D.Double pt2 = new Point2D.Double(
		tpNuc.getX(), tpNuc.getY());
	BLine2D nucLine = new BLine2D(pt1, pt2);

	// g2.setStroke(GraphicsUtil.thin1LineStroke);
	g2.setStroke(stroke);
	Point2D.Double newPt1 = nucLine.getPointAtT(BP_LINE_MULT);
	nucLine = new BLine2D(pt2, pt1);
	Point2D.Double newPt2 = nucLine.getPointAtT(BP_LINE_MULT);
	// g2.draw(new Line2D.Double(newPt1, newPt2));
	(new DrawLineObject(newPt1, newPt2)).printPS(g2, psUtil);
	psUtil.append(" print_line\n");
}

private void
drawDefaultOpenArcBPSymbol(Graphics2D g2)
throws Exception
{
	Point2D midPt = this.get5PBasePairRay().getMidPt();
	g2.setStroke(GraphicsUtil.thin04LineStroke);
	g2.draw(new Arc2D.Double(
		midPt.getX() - ComplexDefines.DEFAULT_BP_SYMBOL_OPEN_RADIUS,
		-midPt.getY() - ComplexDefines.DEFAULT_BP_SYMBOL_OPEN_RADIUS,
		2.0*ComplexDefines.DEFAULT_BP_SYMBOL_OPEN_RADIUS,
		2.0*ComplexDefines.DEFAULT_BP_SYMBOL_OPEN_RADIUS, 0.0, 360.0, Arc2D.OPEN));
}

private void
printPSDefaultOpenArcBPSymbol(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	Point2D midPt = this.get5PBasePairRay().getMidPt();
	g2.setStroke(GraphicsUtil.thin04LineStroke);
	g2.draw(new Arc2D.Double(
		midPt.getX() - ComplexDefines.DEFAULT_BP_SYMBOL_OPEN_RADIUS,
		-midPt.getY() - ComplexDefines.DEFAULT_BP_SYMBOL_OPEN_RADIUS,
		2.0*ComplexDefines.DEFAULT_BP_SYMBOL_OPEN_RADIUS,
		2.0*ComplexDefines.DEFAULT_BP_SYMBOL_OPEN_RADIUS, 0.0, 360.0, Arc2D.OPEN));
}

private void
drawDefaultClosedArcBPSymbol(Graphics2D g2)
throws Exception
{
	Point2D midPt = this.get5PBasePairRay().getMidPt();
	g2.fill(new Arc2D.Double(
		midPt.getX() - ComplexDefines.DEFAULT_BP_SYMBOL_CLOSED_RADIUS,
		-midPt.getY() - ComplexDefines.DEFAULT_BP_SYMBOL_CLOSED_RADIUS,
		2.0*ComplexDefines.DEFAULT_BP_SYMBOL_CLOSED_RADIUS,
		2.0*ComplexDefines.DEFAULT_BP_SYMBOL_CLOSED_RADIUS, 0.0, 360.0,
		Arc2D.OPEN));
}

private void
printPSDefaultClosedArcBPSymbol(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	Point2D midPt = this.get5PBasePairRay().getMidPt();
	g2.fill(new Arc2D.Double(
		midPt.getX() - ComplexDefines.DEFAULT_BP_SYMBOL_CLOSED_RADIUS,
		-midPt.getY() - ComplexDefines.DEFAULT_BP_SYMBOL_CLOSED_RADIUS,
		2.0*ComplexDefines.DEFAULT_BP_SYMBOL_CLOSED_RADIUS,
		2.0*ComplexDefines.DEFAULT_BP_SYMBOL_CLOSED_RADIUS, 0.0, 360.0,
		Arc2D.OPEN));
}

private static float dotLineJump = 4.0f;
private static float dotLineOnUnits = 1.0f;
private static float dotLineOffUnits =
	(float)dotLineJump - dotLineOnUnits;
private static float[] dashVals =
	new float[]{dotLineOnUnits, dotLineOffUnits};
private static BasicStroke dotLineStroke = new BasicStroke(
	0.4f, // linewidth
	BasicStroke.CAP_ROUND,
	BasicStroke.JOIN_ROUND,
	1.0f, // miterlimit
	dashVals,
	0.1f); // dash phase

private Font phosFont = new Font("Helvetica", Font.PLAIN, ComplexDefines.DEFAULT_NUC_FONT_SIZE);

private void
drawDefaultPhosphateBPSymbol(Graphics2D g2)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Point2D.Double pt1 = new Point2D.Double(
		fpNuc.getX(), -fpNuc.getY());
	Point2D.Double pt2 = new Point2D.Double(
		tpNuc.getX(), -tpNuc.getY());
	BLine2D nucLine = new BLine2D(pt1, pt2);

	g2.setColor(Color.black);
	g2.setStroke(this.dotLineStroke);

	Point2D newPt1 = new Point2D.Double(
		(float)pt1.getX() - (float)fpNuc.getLine5PDeltaX(),
		(float)pt1.getY() + (float)fpNuc.getLine5PDeltaY());
	Point2D newPt2 = new Point2D.Double(
		(float)pt2.getX() + (float)fpNuc.getLine3PDeltaX(),
		(float)pt2.getY() - (float)fpNuc.getLine3PDeltaY());

	nucLine = new BLine2D(newPt1, newPt2);

	g2.draw(nucLine);

	g2.setFont(phosFont);

	if (fpNuc.getLabel5PSide())
	{
		g2.drawString("p",
			(float)pt1.getX() - (float)fpNuc.getLabelDeltaX(),
			(float)pt1.getY() + (float)fpNuc.getLabelDeltaY());
	}
	else
	{
		g2.drawString("p",
			(float)pt2.getX() + (float)fpNuc.getLabelDeltaX(),
			(float)pt2.getY() - (float)fpNuc.getLabelDeltaY());
	}
}

private void
printPSDefaultPhosphateBPSymbol(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	Nuc2D fpNuc = this.getFivePrimeNuc2D();
	Nuc2D tpNuc = this.getThreePrimeNuc2D();
	Point2D.Double pt1 = new Point2D.Double(
		fpNuc.getX(), -fpNuc.getY());
	Point2D.Double pt2 = new Point2D.Double(
		tpNuc.getX(), -tpNuc.getY());
	BLine2D nucLine = new BLine2D(pt1, pt2);

	g2.setColor(Color.black);
	g2.setStroke(this.dotLineStroke);

	Point2D newPt1 = new Point2D.Double(
		(float)pt1.getX() - (float)fpNuc.getLine5PDeltaX(),
		(float)pt1.getY() + (float)fpNuc.getLine5PDeltaY());
	Point2D newPt2 = new Point2D.Double(
		(float)pt2.getX() + (float)fpNuc.getLine3PDeltaX(),
		(float)pt2.getY() - (float)fpNuc.getLine3PDeltaY());

	nucLine = new BLine2D(newPt1, newPt2);

	g2.draw(nucLine);

	g2.setFont(phosFont);

	if (fpNuc.getLabel5PSide())
	{
		g2.drawString("p",
			(float)pt1.getX() - (float)fpNuc.getLabelDeltaX(),
			(float)pt1.getY() + (float)fpNuc.getLabelDeltaY());
	}
	else
	{
		g2.drawString("p",
			(float)pt2.getX() + (float)fpNuc.getLabelDeltaX(),
			(float)pt2.getY() - (float)fpNuc.getLabelDeltaY());
	}
}

/***************** End Base Pair Symbol Stuff ********************************/

Point2D fpSideNucPt = null;
Point2D tpSideNucPt = null;

// format along a line as if forming a helix
// bpPosition is for helices formed along perpLine and starts at 0th position;
// for a single basepair, usually form at bpPosition 0

public void
format(BLine2D perpLine, int bpPosition, double baseDist,
	double bpDist, double misMatchBPDist, boolean clockWiseFormat)
throws Exception
{
	double bpLength = 0.5 * bpDist;
	if (this.isMisMatch())
		bpLength = 0.5 * misMatchBPDist;
	if (fpSideNucPt == null)
		fpSideNucPt = new Point2D.Double();
	if (tpSideNucPt == null)
		tpSideNucPt = new Point2D.Double();	
	perpLine.getPerpendicularPointsAtT(
		((double)bpPosition * baseDist)/perpLine.length(),
		fpSideNucPt, tpSideNucPt, bpLength, clockWiseFormat);
	this.format(fpSideNucPt, tpSideNucPt);
}

public void
format(Point2D fpPt, Point2D tpPt)
throws Exception
{
	this.getFivePrimeNuc2D().setXY(fpPt);
	this.getThreePrimeNuc2D().setXY(tpPt);
}

// make a basic format with default values
public void
format(boolean clockWiseFormat)
throws Exception
{
	this.format(
		new BLine2D(0.0, 0.0, 0.0, 1000.0),
		0,
		this.getRNAHelixBaseDistance(),
		this.getRNABasePairDistance(),
		getRNAMisMatchBasePairDistance(),
		clockWiseFormat);
}

public void
format(double angle)
throws Exception
{
	BLine2D ray = this.get5PBasePairRay();
	Point2D midPt = ray.getMidPt();
	Point2D sveMidPt = new Point2D.Double(midPt.getX(), midPt.getY());
	ray.setRayFromAngle(angle);

	Point2D newMidPt = ray.getMidPt();
	double dX = newMidPt.getX() - sveMidPt.getX();
	double dY = newMidPt.getY() - sveMidPt.getY();

	ray.setLine(
		ray.getP1().getX() - dX,
		ray.getP1().getY() - dY,
		ray.getP2().getX() - dX,
		ray.getP2().getY() - dY);

	this.format(ray.getP1(), ray.getP2());
}

public void
format()
throws Exception
{
	BLine2D ray = this.get5PBasePairRay();
	Point2D midPt = ray.getMidPt();
	Point2D sveMidPt = new Point2D.Double(midPt.getX(), midPt.getY());

	double length = 0.0;
	if (this.isMisMatch())
		length = getRNAMisMatchBasePairDistance();
	else
		length = this.getRNABasePairDistance();
	ray.setFromLength(length);

	Point2D newMidPt = ray.getMidPt();
	double dX = newMidPt.getX() - sveMidPt.getX();
	double dY = newMidPt.getY() - sveMidPt.getY();

	ray.setLine(
		ray.getP1().getX() - dX,
		ray.getP1().getY() - dY,
		ray.getP2().getX() - dX,
		ray.getP2().getY() - dY);

	this.format(ray.getP1(), ray.getP2());
}

public double
getAngle()
throws Exception
{
	return (this.get5PBasePairRay().angleInXYPlane());
}

/*************** DrawObject Implementation *******************/

public void
setX(double x)
throws Exception
{
	double currentX = this.getX();

	this.getFivePrimeNuc2D().shiftX(currentX - x);
	this.getThreePrimeNuc2D().shiftX(currentX - x);
}

public void
setY(double y)
throws Exception
{
	double currentY = this.getY();

	this.getFivePrimeNuc2D().shiftY(currentY - y);
	this.getThreePrimeNuc2D().shiftY(currentY - y);
}

public double
getX()
throws Exception
{
	return (getMidPt().getX());	
}

public double
getY()
throws Exception
{
	return (getMidPt().getY());	
}

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;

	if (this.getFivePrimeNuc2D().getIsHidden() && this.getThreePrimeNuc2D().getIsHidden())
		return;
	if (this.getFivePrimeNuc2D().getHideForConstrain() && this.getThreePrimeNuc2D().getHideForConstrain())
		return;

	if (constrainedArea != null)
	{
		// Not sure if this should be || or &&; if || then get a few more
		// base pair symbols, but this might be okay
		if (!(this.getFivePrimeNuc2D().intersects(constrainedArea, g2) ||
			this.getThreePrimeNuc2D().intersects(constrainedArea, g2)))
			return;
	}	

	g2.setTransform(this.getFivePrimeNuc2D().getParentSSData2D().getG2Transform());
	this.getFivePrimeNuc2D().draw(g2, constrainedArea);

	if (this.getFivePrimeNuc2D().isSelfRefBasePair())
	{
		g2.setTransform(this.getThreePrimeNuc2D().getParentSSData2D().getG2Transform());
		this.getThreePrimeNuc2D().draw(g2, constrainedArea);
	}

	this.drawBasePairSymbol(
		this.getFivePrimeNuc2D().getBPSchemGap(),
		this.getThreePrimeNuc2D().getBPSchemGap(), g2);
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	if (this.getIsHidden())
		return;

	if (this.getFivePrimeNuc2D().getIsHidden() && this.getThreePrimeNuc2D().getIsHidden())
		return;

	g2.setTransform(this.getFivePrimeNuc2D().getParentSSData2D().getG2Transform());
	this.getFivePrimeNuc2D().printPS(g2, psUtil);

	if (this.getFivePrimeNuc2D().isSelfRefBasePair())
	{
		g2.setTransform(this.getThreePrimeNuc2D().getParentSSData2D().getG2Transform());
		this.getThreePrimeNuc2D().printPS(g2, psUtil);
	}

	this.printPSBasePairSymbol(this.getFivePrimeNuc2D().getBPSchemGap(),
		this.getThreePrimeNuc2D().getBPSchemGap(), g2, psUtil);
}

public void
eraseBPSymbol(Graphics2D g2)
throws Exception
{
	Nuc2D nuc = this.getFivePrimeNuc2D();
	Nuc2D bpNuc = this.getThreePrimeNuc2D();

	g2.setTransform(((ComplexCollection)nuc.getParentCollection()).getG2Transform());

	RayIntersectNucNode rayIntersect5pNuc =
		new RayIntersectNucNode(this.get5PBasePairRay(), nuc);
	rayIntersect5pNuc.runRayDrawObjectIntersect();	
	if (!rayIntersect5pNuc.getRayIntersects())
	{
		return;
	}
	Point2D fpPt = (Point2D)rayIntersect5pNuc.getIntersectPtList().elementAt(0);
	RayIntersectNucNode rayIntersect3pNuc =
		new RayIntersectNucNode(this.get3PBasePairRay(), bpNuc);
	rayIntersect3pNuc.runRayDrawObjectIntersect();	
	if (!rayIntersect3pNuc.getRayIntersects())
	{
		return;
	}
	Point2D tpPt = (Point2D)rayIntersect3pNuc.getIntersectPtList().elementAt(0);

	Polygon bpFillPolygon = new Polygon();
	Rectangle2D rect = nuc.getNucSymbolBoundingBox();
	Rectangle2D bpRect = bpNuc.getNucSymbolBoundingBox();
	int fpSide = ((Integer)rayIntersect5pNuc.getSideList().elementAt(0)).intValue();
	int tpSide = ((Integer)rayIntersect3pNuc.getSideList().elementAt(0)).intValue();
	int rx = (int)Math.round(rect.getX());
	int ry = (int)Math.round(rect.getY());
	int rw = (int)Math.round(rect.getWidth());
	int rh = (int)Math.round(rect.getHeight());
	int brx = (int)Math.round(bpRect.getX());
	int bry = (int)Math.round(bpRect.getY());
	int brw = (int)Math.round(bpRect.getWidth());
	int brh = (int)Math.round(bpRect.getHeight());
	g2.setColor(Color.black);
	double angle = rayIntersect5pNuc.getRay().angleInXYPlane();
	// don't think I need
	// boolean clockWiseFormatted = this.isClockWiseFormatted();
	if ((fpSide == 3) && (tpSide == 1))
	{
		if (true /*clockWiseFormatted*/)
		{
			if ((angle >= 260.0) && (angle <= 280.0))
			{
				bpFillPolygon.addPoint(rx, ry + rh);
				bpFillPolygon.addPoint(rx + rh, ry + rh);
				bpFillPolygon.addPoint(brx + brw, bry);
				bpFillPolygon.addPoint(brx, bry);
			}
			else if ((angle > 180.0) && (angle < 260.0))
			{
				bpFillPolygon.addPoint(rx, ry);
				bpFillPolygon.addPoint(rx, ry + rh);
				bpFillPolygon.addPoint(rx + rw, ry + rh);
				bpFillPolygon.addPoint(brx + brw, bry + brh);
				bpFillPolygon.addPoint(brx + brw, bry);
				bpFillPolygon.addPoint(brx, bry);
			}
			else if ((angle > 280.0) && (angle < 360.0))
			{
				bpFillPolygon.addPoint(rx, ry + rh);
				bpFillPolygon.addPoint(rx + rw, ry + rh);
				bpFillPolygon.addPoint(rx + rw, ry);
				bpFillPolygon.addPoint(brx + brw, bry);
				bpFillPolygon.addPoint(brx, bry);
				bpFillPolygon.addPoint(brx, bry + brh);
			}
		}
	}
	else if ((fpSide == 1) && (tpSide == 3))
	{
		if (true /*clockWiseFormatted*/)
		{
			if ((angle >= 80.0) && (angle <= 100.0))
			{
				bpFillPolygon.addPoint(rx, ry);
				bpFillPolygon.addPoint(rx + rw, ry);
				bpFillPolygon.addPoint(brx + brw, bry + brh);
				bpFillPolygon.addPoint(brx, bry + brh);
			}
			else if ((angle > 0.0) && (angle < 80.0))
			{
				bpFillPolygon.addPoint(rx, ry);
				bpFillPolygon.addPoint(rx + rw, ry);
				bpFillPolygon.addPoint(rx + rw, ry + rh);
				bpFillPolygon.addPoint(brx + brw, bry + brh);
				bpFillPolygon.addPoint(brx, bry + brh);
				bpFillPolygon.addPoint(brx, bry);
			}
			else if ((angle > 100.0) && (angle < 180.0))
			{
				bpFillPolygon.addPoint(rx, ry + rh);
				bpFillPolygon.addPoint(rx, ry);
				bpFillPolygon.addPoint(rx + rw, ry);
				bpFillPolygon.addPoint(brx + brw, bry);
				bpFillPolygon.addPoint(brx + brw, bry + brh);
				bpFillPolygon.addPoint(brx, bry + brh);
			}
		}
	}
	else if ((fpSide == 2) && (tpSide == 0))
	{
		if (true /*clockWiseFormatted*/)
		{
			if (((angle >= 0.0) && (angle <= 10.0)) ||
				((angle >= 350.0) && (angle <= 360.0)))
			{
				bpFillPolygon.addPoint(rx + rw, ry);
				bpFillPolygon.addPoint(rx + rw, ry + rh);
				bpFillPolygon.addPoint(brx, bry + brh);
				bpFillPolygon.addPoint(brx, bry);
			}
			else if ((angle > 10.0) && (angle < 180.0))
			{
				bpFillPolygon.addPoint(rx, ry);
				bpFillPolygon.addPoint(rx + rw, ry);
				bpFillPolygon.addPoint(rx + rw, ry + rh);
				bpFillPolygon.addPoint(brx + brw, bry + brh);
				bpFillPolygon.addPoint(brx, bry + brh);
				bpFillPolygon.addPoint(brx, bry);
			}
			else if ((angle > 180.0) && (angle < 350.0))
			{
				bpFillPolygon.addPoint(rx + rw, ry);
				bpFillPolygon.addPoint(rx + rw, ry + rh);
				bpFillPolygon.addPoint(rx, ry + rh);
				bpFillPolygon.addPoint(brx, bry + brh);
				bpFillPolygon.addPoint(brx, bry);
				bpFillPolygon.addPoint(brx + brw, bry);
			}
		}
	}
	else if ((fpSide == 0) && (tpSide == 2))
	{
		if (true /*clockWiseFormatted*/)
		{
			if ((angle >= 170.0) && (angle <= 190.0))
			{
				bpFillPolygon.addPoint(rx, ry);
				bpFillPolygon.addPoint(rx, ry + rh);
				bpFillPolygon.addPoint(brx + brw, bry + brh);
				bpFillPolygon.addPoint(brx + brw, bry);
			}
			else if (angle < 170.0)
			{
				bpFillPolygon.addPoint(rx + rw, ry);
				bpFillPolygon.addPoint(rx, ry);
				bpFillPolygon.addPoint(rx, ry + rh);
				bpFillPolygon.addPoint(brx, bry + brh);
				bpFillPolygon.addPoint(brx + brw, bry + brh);
				bpFillPolygon.addPoint(brx + brw, bry);
			}
			else if (angle > 190.0)
			{
				bpFillPolygon.addPoint(rx, ry);
				bpFillPolygon.addPoint(rx, ry + rh);
				bpFillPolygon.addPoint(rx + rw, ry + rh);
				bpFillPolygon.addPoint(brx + brw, bry + brh);
				bpFillPolygon.addPoint(brx + brw, bry);
				bpFillPolygon.addPoint(brx, bry);
			}
		}
	}
	else if (((fpSide == 2) && (tpSide == 3)) || ((fpSide == 1) && (tpSide == 0)))
	{
		if (true /*clockWiseFormatted*/)
		{
			bpFillPolygon.addPoint(rx, ry);
			bpFillPolygon.addPoint(rx + rw, ry);
			bpFillPolygon.addPoint(rx + rw, ry + rh);
			bpFillPolygon.addPoint(brx + brw, bry + brh);
			bpFillPolygon.addPoint(brx, bry + brh);
			bpFillPolygon.addPoint(brx, bry);
		}
	}
	else if (((fpSide == 0) && (tpSide == 3)) || ((fpSide == 1) && (tpSide == 2)))
	{
		if (true /*clockWiseFormatted*/)
		{
			bpFillPolygon.addPoint(rx, ry + rh);
			bpFillPolygon.addPoint(rx, ry);
			bpFillPolygon.addPoint(rx + rw, ry);
			bpFillPolygon.addPoint(brx + brw, bry);
			bpFillPolygon.addPoint(brx + brw, bry + brh);
			bpFillPolygon.addPoint(brx, bry + brh);
		}
	}
	else if (((fpSide == 3) && (tpSide == 0)) || ((fpSide == 2) && (tpSide == 1)))
	{
		if (true /*clockWiseFormatted*/)
		{
			bpFillPolygon.addPoint(rx + rw, ry);
			bpFillPolygon.addPoint(rx + rw, ry + rh);
			bpFillPolygon.addPoint(rx, ry + rh);
			bpFillPolygon.addPoint(brx, bry + brh);
			bpFillPolygon.addPoint(brx, bry);
			bpFillPolygon.addPoint(brx + brw, bry);
		}
	}
	else if (((fpSide == 3) && (tpSide == 2)) || ((fpSide == 0) && (tpSide == 1)))
	{
		if (true /*clockWiseFormatted*/)
		{
			bpFillPolygon.addPoint(rx, ry);
			bpFillPolygon.addPoint(rx, ry + rh);
			bpFillPolygon.addPoint(rx + rw, ry + rh);
			bpFillPolygon.addPoint(brx + brw, bry + brh);
			bpFillPolygon.addPoint(brx + brw, bry);
			bpFillPolygon.addPoint(brx, bry);
		}
	}
	else
	{
		debug("NO GO ON SIDES: " + fpSide + " " + tpSide);
	}
	if (bpFillPolygon.npoints > 2)
	{
		g2.setColor(g2.getBackground());
		g2.fill(bpFillPolygon);
	}
}

// delete drawObject associated with nuc only, not labels. Labels
// should be associated with parents right before deletion.
public void
delete(Graphics2D g2)
throws Exception
{
	this.erase(g2);
	this.getFivePrimeNuc2D().delete(g2);
	this.getThreePrimeNuc2D().delete(g2);
}

private static void
debug(String s)
{
	System.err.println("RNABasePair2D-> " + s);
}

}
