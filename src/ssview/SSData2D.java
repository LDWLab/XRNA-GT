package ssview;

import java.awt.*;
import java.awt.font.*;
import java.io.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.*;

import javax.swing.*;

import jimage.DrawObject;
import jimage.DrawAdobeObject;
import jimage.DrawCharObject;
import jimage.DrawCircleObject;
import jimage.DrawLineObject;
import jimage.DrawStringObject;
import jimage.DrawTriangleObject;
import jimage.DrawParallelogramObject;

import util.*;
import util.math.*;

// collection of nucs for one rna secondary structure. also contains
// labels associated with SSData.

public class
SSData2D
extends SSData
{

public
SSData2D()
throws Exception
{
	super ();
	setX(0.0);
	setY(0.0);
}

public
SSData2D(String name)
throws Exception
{
	super (name);
	setX(0.0);
	setY(0.0);
}

public
SSData2D(String primaryStructure, String name)
throws Exception
{
	this (name);
	this.addNucs(primaryStructure);
	(new RNASingleStrand2D(this.getNuc2DAt(1))).formatBlock();
}

// make a copy
public
SSData2D(SSData2D sstr)
throws Exception
{
	super (sstr.getName());
	setX(sstr.getX());
	setY(sstr.getY());
	this.copyNucs(sstr);

	// may have to have a copyLabel routine rather than addLabel
	if (sstr.getLabelList() != null)
		for (Enumeration e = sstr.getLabelList().elements();e.hasMoreElements();)
			this.addLabel((DrawObject)e.nextElement());
}

// make up new with a nuc structure. carefull not to use getX,Y() from
// nucstructure cause often the midpt of a basepair.
public
SSData2D(NucCollection2D nucStructure)
throws Exception
{
	super (nucStructure.getName());
	setX(0.0);
	setY(0.0);
	this.addNucs(nucStructure);
	if (nucStructure.getLabelList() != null)
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
			this.addLabel((DrawObject)e.nextElement());
}

// make a new structure from primary structure, secondary structure,
// and name.
public
SSData2D(String primaryStructure, String secondaryStructure, String name)
throws Exception
{
	super (name);

	for (int i = 0;i < primaryStructure.length();i++)
	{
		char nucChar = primaryStructure.charAt(i);
		if (Character.isWhitespace(nucChar))
			continue;
		if (!NucNode.isValidNucChar(nucChar))
		{
			throw new ComplexException(
				"Error in Primary Structure, Nuc at position: " + (i+1) +
				" must be (A|U|G|C|R|Y|N)");
		}
	}
	
	this.addNucs(primaryStructure);

	NucCollection2D.setBasePairs(this, secondaryStructure);

	// format entire structure
	this.formatAllStr(1000.0, 90.0,
		true, // left handed
		true); /* center */
}

// make a new structure from primary structure, secondary structure,
// name, and start angle for new formatting method. Need to ad
// radius of arc.
public
SSData2D(String primaryStructure, String secondaryStructure, String name,
	double startAngle, double arcRadius,
	double nucToNextNucDistance,
	double helixBaseDistance,
	double basepairDistance,
	double mismatchDistance,
	int nucFontSize,
	boolean isLeftHanded)
throws Exception
{
	super (name);
	setX(0.0);
	setY(0.0);

	for (int i = 0;i < primaryStructure.length();i++)
	{
		char nucChar = primaryStructure.charAt(i);
		if (Character.isWhitespace(nucChar))
			continue;
		if (!NucNode.isValidNucChar(nucChar))
		{
			throw new ComplexException(
				"Error in Primary Structure, Nuc at position: " + (i+1) +
				" must be (A|U|G|C|R|Y|N)");
		}
	}
	
	boolean centerStructure = true;
	this.addNucs(primaryStructure);
	NucCollection2D.setBasePairs(this, secondaryStructure);
	this.setRNANucToNextNucDistance(nucToNextNucDistance);
	this.setRNAHelixBaseDistance(helixBaseDistance);
	this.setRNABasePairDistance(basepairDistance);
	this.setRNAMisMatchBasePairDistance(mismatchDistance);
	this.setLevel0EndPts(arcRadius, startAngle);
	this.setFonts(new Font("Helvetica", Font.PLAIN, nucFontSize));

	// format entire structure
	this.formatAllStr(1000.0, 90.0, isLeftHanded, centerStructure);
}

// provide a random canonical structure
// NEED to provide a probability of non-canonical structure.
public
SSData2D(int length, String secondaryStructure, String name)
throws Exception
{
	super (name);

	if (secondaryStructure == null)
		throw new Exception("NULL secondaryStructure string");

	if (length <= 0)
		throw new Exception("invalid length: " + length);
	Random randomNucGenerator = new Random();
	StringBuffer buf = new StringBuffer();
	char randomNucChar = 'N';
	for (int i = 0;i < length;i++)
	{
		int randomInt = randomNucGenerator.nextInt(3);
		switch (randomInt)
		{
		  case 0 :
			randomNucChar = 'A';
			break;
		  case 1 :
			randomNucChar = 'U';
			break;
		  case 2 :
			randomNucChar = 'G';
			break;
		  case 3 :
			randomNucChar = 'C';
			break;
		  default :
			throw new Exception("invalid random int: " + randomInt);
		}
		buf.append(randomNucChar);
	}

	SSData2D tmpSStr = new SSData2D(buf.toString(), name);
	NucCollection2D.setBasePairs(tmpSStr, secondaryStructure);
	for (int nucID = 1;nucID <= tmpSStr.getNucCount();nucID++)
	{
		NucNode nuc = tmpSStr.getNucAt(nucID);
		if (!nuc.isFivePrimeBasePair())
			continue;
		RNABasePair bp = new RNABasePair(nuc);
		if (bp.isDefaultCanonical(nuc))
			continue;
		char nucChar = RNABasePair.genCanonicalNucChar(nuc.getNucChar());
		nuc.getBasePair().setNucChar(nucChar);
	}
	this.addNucs(tmpSStr.getPrimaryStructure());

	NucCollection2D.setBasePairs(this, secondaryStructure);

	// format entire structure
	this.formatAllStr(1000.0, 90.0,
		true, // left handed
		true); /* center */
}

public void
init()
throws Exception
{
	super.init();
}

public void
addNuc(char nucChar)
throws Exception
{
	if (Character.isWhitespace(nucChar))
		return;
	if (ComplexAlignment.isValidGapChar(nucChar))
		return;
	
	// any other char should be nucchar or throws exception
	this.addItem(new Nuc2D(nucChar));
}

public void
addNucs(String nucCharList)
throws Exception
{
	for (int i = 0;i < nucCharList.length();i++)
	{
		this.addNuc(nucCharList.charAt(i));
	}
}

public void
addNucs(Vector nucCharList)
throws Exception
{
	for (int i = 0;i < nucCharList.size();i++)
	{
		this.addNuc(((Character)nucCharList.elementAt(i)).charValue());
	}
	// this.addNucs(new String((char[])nucCharList.toArray()));
}

public void
copyNucs(SSData2D sstr)
throws Exception
{
	// this should make a null nuc also; watch out for.
	for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
		this.addNuc(new Nuc2D(sstr.getNuc2DAt(nucID)));
}

public Nuc2D
getNuc2DAt(int nucID)
{
	return ((Nuc2D)getItemAt(nucID));
}

public Nuc2D
getFirstNonNullNuc2D()
throws Exception
{
	return ((Nuc2D)this.getFirstNonNullNuc());
}

public Nuc2D
getEndNonNullNuc2D()
throws Exception
{
	return ((Nuc2D)this.getEndNonNullNuc());
}

public void
OldSchematicize(Graphics2D g2)
throws Exception
{
	/*
	g2.setRenderingHint(
		RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_OFF);
	*/
	g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);

	/* NEED to get scale from g2
	g2.setStroke(GraphicsUtil.getDotStroke((float)figureScale));	
	*/

	g2.setStroke(new BasicStroke((float)1.5,
		BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

	g2.setTransform(((ComplexCollection)this.getParentCollection()).
		getG2Transform());

	Line2D.Double line = new Line2D.Double();

	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0ID = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1ID = (Nuc2D)((NucNode)delineators.elementAt(i+1));
		line = new Line2D.Double(nuc0ID.getX(), -nuc0ID.getY(), 0.0, 0.0);
		for (int nucID = nuc0ID.getID();nucID <= nuc1ID.getID();nucID++)
		{
			Nuc2D nuc = getNuc2DAt(nucID);
			line.setLine(nuc.getX(), -nuc.getY(),
				line.getX1(), line.getY1());
			g2.draw(line);
		}
	}
}

public void
setLevel0EndPts(double arcRadius, double startAngle)
throws Exception
{
	RNACycle2D level0Cycle = new RNACycle2D(this.getFirstNonNullNuc2D());
	level0Cycle.resetCycleArcDistance(false);
	Vector cycleNucList = level0Cycle.getCycleNucs();

	Nuc2D startNuc = (Nuc2D)cycleNucList.elementAt(0);
	Nuc2D endNuc = (Nuc2D)cycleNucList.elementAt(cycleNucList.size() - 1);
	// debug("HERE: " + startNuc.getID() + " " + endNuc.getID());

	// check if case 2: single exit helix with no tag ends
	if (startNuc.isBasePair() && endNuc.isBasePair() &&
		(startNuc.getBasePair() == endNuc))
	{
		this.getFirstNonNullNuc2D().setXY(-100.0, 0.0);
		this.getEndNonNullNuc2D().setXY(100.0, 0.0);
	}
	else
	{
		BVector2d A = MathUtil.polarCoordToPoint(arcRadius, startAngle);
		double theta = level0Cycle.getCycleArcDistance()/arcRadius;
		BVector2d B = MathUtil.polarCoordToPoint(arcRadius, startAngle - (MathDefines.RadToDeg * theta));
		this.getFirstNonNullNuc2D().setXY(A.getX(), A.getY());
		this.getEndNonNullNuc2D().setXY(B.getX(), B.getY());
		// debug("A: " + A);
		// debug("B: " + B);
	}

}

public void
formatAllStr(Vector helixList, double arcRadius, double startAngle,
	boolean clockWiseFormat, boolean centerAtOrigin)
throws Exception
{
	if (helixList != null)
		NucCollection2D.setBasePairs(helixList);

	this.formatAllStr(arcRadius, startAngle, clockWiseFormat, centerAtOrigin);
}

public void
formatAllStr(double arcRadius, double startAngle, boolean clockWiseFormat,
	boolean centerAtOrigin)
throws Exception
{
	this.setLevel0EndPts(arcRadius, startAngle);

	Nuc2D startNuc = this.getFirstNonNullNuc2D();
	Nuc2D endNuc = this.getEndNonNullNuc2D();	

	// this test assumes first nuc and last nuc are already formatted
	double fivePrimeX = startNuc.getX();
	double fivePrimeY = startNuc.getY();
	double threePrimeX = endNuc.getX();
	double threePrimeY = endNuc.getY();

	this.formatCycleStr(startNuc, clockWiseFormat, true);

	// now start testing

	// first check if not case 2: startNuc and endNuc both basepaired together
	if (startNuc.isSingleStranded() && endNuc.isSingleStranded())
	{
		if ((fivePrimeX != startNuc.getX()) ||
			(fivePrimeY != startNuc.getY()))
			throw new ComplexException("Error in SSData2D.formatAllStr()",
				ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
				ComplexDefines.FORMAT_SSDATA_ENDPTS_ERROR_MSG,
				"startNuc, " + startNuc.getID() + " is off by a distance of: " +
				startNuc.getPoint2D().distance(new Point2D.Double(fivePrimeX, fivePrimeY)));
		if ((threePrimeX != endNuc.getX()) ||
			(threePrimeY != endNuc.getY()))
			throw new ComplexException("Error in SSData2D.formatAllStr()",
				ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
				ComplexDefines.FORMAT_SSDATA_ENDPTS_ERROR_MSG,
				"endNuc, " + endNuc.getID() + " is off by a distance of: " +
				endNuc.getPoint2D().distance(new Point2D.Double(threePrimeX, threePrimeY)));
	}
	else if (startNuc.isSingleStranded() && endNuc.isBasePair())
	{
		if ((fivePrimeX != startNuc.getX()) ||
			(fivePrimeY != startNuc.getY()))
			throw new ComplexException("Error in SSData2D.formatAllStr()",
				ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
				ComplexDefines.FORMAT_SSDATA_ENDPTS_ERROR_MSG,
				"startNuc, " + startNuc.getID() + " is off by a distance of: " +
				startNuc.getPoint2D().distance(new Point2D.Double(fivePrimeX, fivePrimeY)));
	}
	else if (startNuc.isBasePair() && endNuc.isSingleStranded())
	{
		if ((threePrimeX != endNuc.getX()) ||
			(threePrimeY != endNuc.getY()))
			throw new ComplexException("Error in SSData2D.formatAllStr()",
				ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
				ComplexDefines.FORMAT_SSDATA_ENDPTS_ERROR_MSG,
				"endNuc, " + endNuc.getID() + " is off by a distance of: " +
				endNuc.getPoint2D().distance(new Point2D.Double(threePrimeX, threePrimeY)));
	}
	else
	{
	// debug("NO GO");
	}
	
	/* NEED to develop better testing; need to take into account that at level0
	** nuc distances could be bigger
	// For Test purposes only. NEED to comment out this test during interactive usage.
	Nuc2D lastNuc = null;
	Vector delineators = this.getItemListDelineators();
	double distance = 0.0;
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0 = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1 = (Nuc2D)((NucNode)delineators.elementAt(i+1));
		for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++)
		{
			Nuc2D nuc = this.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			if (!nuc.getIsFormatted())
			{
				throw new ComplexException("Error in SSData2D.formatAllStr()",
					ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
					ComplexDefines.FORMAT_NUC_ERROR_MSG);
			}
			if (nuc.isFivePrimeBasePair() && !nuc.isMisMatch())
			{
				distance = nuc.getPoint2D().distance(nuc.getBasePair2D().getPoint2D());
				if (!this.isValidRNABasePairDistance(distance))
					throw new ComplexException("Error in SSData2D.formatAllStr()",
						ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
						ComplexDefines.FORMAT_SSDATA_BASEPAIR_ERROR_MSG,
						("NUCS INVOLVED, dist: " + lastNuc.getID() + ":" + nuc.getID() + " " + distance));
			}
			else if (nuc.isFivePrimeBasePair() && nuc.isMisMatch())
			{
				distance = nuc.getPoint2D().distance(nuc.getBasePair2D().getPoint2D());
				if (!this.isValidRNAMisMatchBasePairDistance(distance))
					throw new ComplexException("Error in SSData2D.formatAllStr()",
						ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
						ComplexDefines.FORMAT_SSDATA_BASEPAIR_ERROR_MSG,
						("NUCS INVOLVED, dist: " + lastNuc.getID() + ":" + nuc.getID() + " " + distance));
			}
			if (lastNuc == null)
			{
				lastNuc = nuc;
				continue;
			}
			if (nuc.isSingleStranded() && lastNuc.isSingleStranded() &&
				(nuc.getID() == lastNuc.getID() + 1))
			{
				distance = nuc.getPoint2D().distance(lastNuc.getPoint2D());
				if (!this.isValidRNANucToNextNucDistance(distance))
					throw new ComplexException("Error in SSData2D.formatAllStr()",
						ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
						ComplexDefines.FORMAT_SSDATA_NUC_TO_NUC_ERROR_MSG,
						("NUCS INVOLVED, dist: " + lastNuc.getID() + ":" + nuc.getID() + " " + distance));
			}
			else if (nuc.isBasePair() && lastNuc.isBasePair() &&
				nuc.isContiguousBasePairWith(lastNuc))
			{
				distance = nuc.getPoint2D().distance(lastNuc.getPoint2D());
				// NEED to see why tolerance of 2.0 has to be so high
				if (!this.isValidRNAHelixBaseDistance(distance, 2.0))
				{
					throw new ComplexException("Error in SSData2D.formatAllStr()",
						ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
						ComplexDefines.FORMAT_SSDATA_HELIXNUC_ERROR_MSG,
						("NUCS INVOLVED, dist: " + lastNuc.getID() + ":" + nuc.getID() + " " + distance));
				}
			}
			else if (nuc.isBasePair() && lastNuc.isBasePair() &&
				!nuc.isContiguousBasePairWith(lastNuc))
			{
				distance = nuc.getPoint2D().distance(lastNuc.getPoint2D());
				// NEED to see why tolerance of 2.0 has to be so high
				if (!this.isValidRNAHelixBaseDistance(distance, 2.0))
				{
					throw new ComplexException("Error in SSData2D.formatAllStr()",
						ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
						ComplexDefines.FORMAT_SSDATA_NUC_TO_NUC_ERROR_MSG,
						("NUCS INVOLVED, dist: " + lastNuc.getID() + ":" + nuc.getID() + " " + distance));
				}
			}
			else if (nuc.isBasePair() && lastNuc.isSingleStranded() &&
				(nuc.getID() == lastNuc.getID() + 1))
			{
				distance = nuc.getPoint2D().distance(lastNuc.getPoint2D());
				// NEED to see why tolerance of 2.0 has to be so high
				if (!this.isValidRNANucToNextNucDistance(distance, 2.0))
					throw new ComplexException("Error in SSData2D.formatAllStr()",
						ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
						ComplexDefines.FORMAT_SSDATA_NUC_TO_NUC_ERROR_MSG,
						("NUCS INVOLVED, dist: " + lastNuc.getID() + ":" + nuc.getID() + " " + distance));
			}
			else if (nuc.isSingleStranded() && lastNuc.isBasePair() &&
				(nuc.getID() == lastNuc.getID() + 1))
			{
				distance = nuc.getPoint2D().distance(lastNuc.getPoint2D());
				// NEED to see why tolerance of 2.0 has to be so high
				if (!this.isValidRNANucToNextNucDistance(distance, 2.0))
					throw new ComplexException("Error in SSData2D.formatAllStr()",
						ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
						ComplexDefines.FORMAT_SSDATA_NUC_TO_NUC_ERROR_MSG,
						("NUCS INVOLVED, dist: " + lastNuc.getID() + ":" + nuc.getID() + " " + distance));
			}
			else
			{
				throw new ComplexException("Error in SSData2D.formatAllStr()",
					ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.FORMAT_ERROR,
					ComplexDefines.FORMAT_NUC_COMPARISON_ERROR_MSG,
					("NUCS INVOLVED: " + lastNuc.getID() + ":" + nuc.getID()));
			}
			lastNuc = nuc;
		}
	}
	*/

	// Now go through and reshape helical runs that have 5' and 3'
	// difference of 1 nuc count.
	//

	for (int nucID = 1;nucID <= this.getNucCount();nucID++)
	{
		Nuc2D cycleNuc = this.getNuc2DAt(nucID);
		if (cycleNuc == null)
			continue;
		if (!cycleNuc.isHelixStart())
			continue;
		RNACycle2D cycle = new RNACycle2D(cycleNuc);
		if (cycle.atCycle0())
			continue;
		if (cycle.getExitHelicesCount() != 1)
			continue;

		RNAHelix2D exitHelix = cycle.getFirstExitHelix2D();

		RNASingleStrand2D testSingleStrand = new RNASingleStrand2D();
		Nuc2D last5PNuc = exitHelix.getFivePrimeStartNuc2D().lastNonNullNuc2D();
		int fivePSSCount = 0;
		int threePSSCount = 0;
		if ((last5PNuc != null) && last5PNuc.isSingleStranded())
		{
			testSingleStrand.set(last5PNuc);
			fivePSSCount = testSingleStrand.getNonDelineatedNucCount();
		}
		Nuc2D next3PNuc = exitHelix.getThreePrimeEndNuc2D().nextNonNullNuc2D();
		if ((next3PNuc != null) && next3PNuc.isSingleStranded())
		{
			testSingleStrand.set(next3PNuc);
			threePSSCount = testSingleStrand.getNonDelineatedNucCount();
		}
		if (((fivePSSCount == 1) || (fivePSSCount == 0)) &&
			((threePSSCount == 1) || (threePSSCount == 0)))
			continue;

		if (!((fivePSSCount == threePSSCount - 1) ||
			(threePSSCount == fivePSSCount - 1)))
			continue;

		DrawCircleObject cycleCircle = cycle.getCycleCircle();
		RNASubDomain2D subDomain = new RNASubDomain2D(exitHelix);
		while (true)
		{
			AffineTransform affTr = new AffineTransform();
			affTr.setToIdentity();
			affTr.rotate(MathDefines.DegToRad *
				(cycle.getEntryHelix2D().getAngle() - exitHelix.getAngle()),
				cycleCircle.getX(), cycleCircle.getY());
			subDomain.transform(affTr);
			boolean clockWiseFormatted = exitHelix.isClockWiseFormatted();
			Nuc2D refNuc = exitHelix.getFivePrimeStartNuc2D().lastNuc2D();
			boolean fivePrimeStrandOkay = true;
			if ((refNuc != null) && refNuc.isSingleStranded())
			{
				RNASingleStrand2D singleStrand = new RNASingleStrand2D(refNuc);
				double radius = cycleCircle.getCenterPt().distance(
					cycle.getEntryHelix2D().getThreePrimeStartNuc2D().getPoint2D());
				singleStrand.formatArc(cycleCircle.getCenterPt(),
					radius, clockWiseFormatted);
				fivePrimeStrandOkay = (refNuc.getPoint2D().distance(exitHelix.getFivePrimeStartNuc2D().getPoint2D()) >= this.getRNANucToNextNucDistance());
			}
			refNuc = exitHelix.getThreePrimeEndNuc2D().nextNuc2D();
			boolean threePrimeStrandOkay = true;
			if ((refNuc != null) && refNuc.isSingleStranded())
			{
				RNASingleStrand2D singleStrand = new RNASingleStrand2D(refNuc);
				double radius = cycleCircle.getCenterPt().distance(
					cycle.getEntryHelix2D().getThreePrimeStartNuc2D().getPoint2D());
				singleStrand.formatArc(cycleCircle.getCenterPt(),
					radius, clockWiseFormatted);
				threePrimeStrandOkay = (refNuc.getPoint2D().distance(exitHelix.getThreePrimeEndNuc2D().getPoint2D()) >= this.getRNANucToNextNucDistance());
			}
			if (fivePrimeStrandOkay && threePrimeStrandOkay)
				break;
			cycleCircle.setRadius(cycleCircle.getRadius() + 1.0);
			cycle.resetCycleCircle(cycleCircle);
			cycle.partialReset(true);
			cycle.formatCycle(cycleCircle.getCenterPt(), clockWiseFormatted, false, false);
		}
	}

	if (centerAtOrigin)
		centerAtOrigin();
}

public void
setHideForConstrain(boolean hideForConstrain)
throws Exception
{
	super.setHideForConstrain(hideForConstrain);

	if (getLabelList() == null)
		return;

	for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
	{
		DrawObject label = (DrawObject)e.nextElement();
		label.setHideForConstrain(hideForConstrain);
	}
}

public void
setEditColor(Color editColor)
throws Exception
{
	super.setEditColor(editColor);

	if (getLabelList() == null)
		return;

	for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
	{
		DrawObject label = (DrawObject)e.nextElement();
		label.setEditColor(editColor);
	}
}

/************** DrawObject Implementation *****************************/

public void
shiftXY(double x, double y)
throws Exception
{
	super.shiftXY(x, y);

	if (getLabelList() == null)
		return;

	for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
	{
		DrawObject label = (DrawObject)e.nextElement();
		label.shiftXY(x, y);
	}
}

public boolean
contains(double xPos, double yPos)
throws Exception
{
	// IS THIS RIGHT:??
	return (this.getIsPickable() && this.getBoundingBox().contains(xPos, yPos));
}

/**************** END DrawObject Implementation ************/

public DrawObject
findLeafNode(double xPos, double yPos, Vector includeTypes, Vector excludeTypes)
throws Exception
{
	// try looking in this's leaf nodes
	if (getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			/*
			debug("LOOKIN FOR DRWOBJ in (" + this.getName() + ") : " +  drawObject.toString());
			*/
			if (drawObject.contains(xPos - this.getX(), yPos + this.getY()))
			{
				/*
				debug("FOUND DRWOBJ: " + drawObject.toString());
				*/
				return(drawObject);
			}
		}
	}

	/*
	debug("NOW LOOKIN FOR NUC in (" + this.getName() + ") : ");
	debug("AT xPos,yPos, x,y+getX,getY: " + xPos + " " + yPos + " " + (xPos - this.getX()) + " " + (yPos + this.getY()));
	*/
	for (int nucID = 1;nucID <= ((SSData2D)this).getNucCount();nucID++)
	{
		Nuc2D nuc = this.getNuc2DAt(nucID);
		if (nuc == null)
			continue;
		if (nuc.isHidden())
			continue;

		DrawObject drawObject = (DrawObject)nuc.findLeafNode(
			xPos - this.getX(), yPos + this.getY(), includeTypes, excludeTypes);
		if (drawObject != null) // bottomed out at a leaf node
			return (drawObject);
	}

	return (null);
}

public Nuc2D
findNuc(double xPos, double yPos)
throws Exception
{
	for (int nucID = 1;nucID <= ((SSData2D)this).getNucCount();nucID++)
	{
		Nuc2D nuc = this.getNuc2DAt(nucID);
		if (nuc == null)
			continue;
		if (nuc.isHidden())
			continue;

		Nuc2D foundNuc = nuc.findNuc(xPos - this.getX(), yPos + this.getY());
		if (foundNuc != null)
			return (foundNuc);
	}

	return (null);
}

/************** PARSING STUFF *****************************/

public void
setNucsFromFile(String fileName, String dataType, int startNucID)
throws Exception
{
	String listData = FileUtil.getFileAsString(fileName);
	setNucsFromListData(listData, dataType, startNucID);
}

public void
setNucsFromListData(String listData, String dataType, int startNucID)
throws Exception
{
	StringTokenizer ssLineT = new StringTokenizer(listData, "\n");
	while (ssLineT.hasMoreTokens())
	{
		String ssLine = ssLineT.nextToken().trim();
		if (ssLine.indexOf("#") == 0)
			continue;
		//
		// if (ssLine.charAt(0) == 's')
		//
		if (dataType.equals("NucID.NucChar"))
		{
			StreamTokenizer streamT = new StreamTokenizer(
				new CharArrayReader(ssLine.toCharArray()));
			Nuc2D nuc = new Nuc2D(StringTokenUtil.getNextIntToken(ssLine, streamT));
			nuc.setNucChar(StringTokenUtil.getNextCharToken(ssLine, streamT));
			nuc.setParentCollection(this);
			this.addItem(nuc);
		}
		else if (dataType.equals("NucID.NucChar.XPos.YPos.FormatType.BPID"))
		{
			Nuc2D nuc = SSData2D.parseSSNucLine(ssLine);
			nuc.setParentCollection(this);
			this.addItem(nuc);
		}
		else if (dataType.equals("NucID.NucChar.XPos.YPos.FormatType.BPID"))
		{
			Nuc2D nuc = SSData2D.parseSSNucLine(ssLine);
			nuc.setParentCollection(this);
			this.addItem(nuc);
		}
		else if (dataType.equals("NucID.NucChar.XPos.YPos"))
		{
			StreamTokenizer streamT = new StreamTokenizer(
				new CharArrayReader(ssLine.toCharArray()));
			Nuc2D nuc = new Nuc2D(StringTokenUtil.getNextIntToken(ssLine, streamT));
			nuc.setNucChar(StringTokenUtil.getNextCharToken(ssLine, streamT));
			nuc.setX(StringTokenUtil.getNextDoubleToken(ssLine, streamT));
			nuc.setY(StringTokenUtil.getNextDoubleToken(ssLine, streamT));
			nuc.setParentCollection(this);
			this.addItem(nuc);
		}
		else if (dataType.equals("NucChar.XPos.YPos"))
		{
			StreamTokenizer streamT = new StreamTokenizer(
				new CharArrayReader(ssLine.toCharArray()));
			Nuc2D nuc = new Nuc2D(startNucID++);
			nuc.setNucChar(StringTokenUtil.getNextCharToken(ssLine, streamT));
			nuc.setX(StringTokenUtil.getNextDoubleToken(ssLine, streamT));
			nuc.setY(StringTokenUtil.getNextDoubleToken(ssLine, streamT));
			nuc.setParentCollection(this);
			this.addItem(nuc);
		}
	}
}

public void
setSSDataFromPSFile(File sstrFile)
throws Exception
{
	Font currFont = new Font("Helvetica", Font.PLAIN, ComplexDefines.DEFAULT_NUC_FONT_SIZE);

	String fileName = sstrFile.getPath();
	if (fileName.endsWith(".ps"))
	{
		String prefixName = new String(fileName);
		int indexOfFileSeperator = prefixName.lastIndexOf(sstrFile.separatorChar);
		if (indexOfFileSeperator >= 0)
			prefixName = prefixName.substring(indexOfFileSeperator + 1, prefixName.length());
		this.setName(prefixName.substring(0, prefixName.length() - 3));
	}
	else
	{
		this.setName(fileName);
	}

	String psString = FileUtil.getFileAsString(fileName);

	if (psString.indexOf("lwstring") == -1)
	{
		// System.err.println("Error: Not an old style XRNA postscript output file: " + psFileName);
		throw new ComplexException("Error in SSData2D.setSSDataFromPSFile()",
			ComplexDefines.RNA_SSDATA_ERROR + ComplexDefines.READ_PS_ERROR,
			ComplexDefines.READ_PS_TO_SSDATA_ERROR_MSG, fileName);
	}

	// first get nucs
	Color nucColor = Color.black;
	StringTokenizer psT = new StringTokenizer(psString, "\n");
	while (psT.hasMoreTokens())
	{
		String psLine = psT.nextToken().trim();
		if (psLine.endsWith("setfont"))
		{
			/*
			/Helvetica-Oblique findfont 14.00 scalefont setfont
			*/
			StreamTokenizer fontT = new StreamTokenizer(
				new CharArrayReader(psLine.substring(1).toCharArray()));
			String fontType = StringTokenUtil.getNextStringToken(psLine, fontT);
			String dummy = StringTokenUtil.getNextStringToken(psLine, fontT);
			int scaleVal = (int)Math.round(StringTokenUtil.getNextDoubleToken(psLine, fontT));
			// debug("FOUND FONT: " + fontType + " " + scaleVal);
			if (fontType.toLowerCase().equals("helvetica"))
				currFont = new Font("Helvetica", Font.PLAIN, scaleVal);
			else if (fontType.toLowerCase().equals("helvetica-bold"))
				currFont = new Font("Helvetica", Font.BOLD, scaleVal);
			else if (fontType.toLowerCase().equals("helvetica-oblique"))
				currFont = new Font("Helvetica", Font.ITALIC, scaleVal);
		}
		else if (psLine.endsWith("setrgbcolor"))
		{
			StreamTokenizer colorT = new StreamTokenizer(
				new CharArrayReader(psLine.toCharArray()));
			float redCol = (float)StringTokenUtil.getNextDoubleToken(psLine, colorT);
			float greenCol = (float)StringTokenUtil.getNextDoubleToken(psLine, colorT);
			float blueCol = (float)StringTokenUtil.getNextDoubleToken(psLine, colorT);
			nucColor = new Color(redCol, greenCol, blueCol);
		}
		if (!psLine.endsWith("lwstring"))
			continue;
		if (psLine.charAt(0) != '(')
			continue;
		if (psLine.charAt(2) != ')')
			continue;
		char nucChar = psLine.charAt(1);
		if (!NucNode.isValidNucChar(Character.toUpperCase(nucChar)))
			continue;

		StreamTokenizer streamT = new StreamTokenizer(
			new CharArrayReader(psLine.substring(3).toCharArray()));
		
		double x = StringTokenUtil.getNextDoubleToken(psLine, streamT);
		double y = StringTokenUtil.getNextDoubleToken(psLine, streamT);
		Nuc2D nuc = new Nuc2D(Character.toUpperCase(nucChar), x, y);
		nuc.setColor(nucColor);
		nuc.setFont(currFont);
		this.addNuc(nuc);
	}

	Graphics2D tmpImgG = GraphicsUtil.unitG2;

	// next get labels
	psT = new StringTokenizer(psString, "\n");
	while (psT.hasMoreTokens())
	{
		String psLine = psT.nextToken().trim();
		if (psLine.endsWith("setfont"))
		{
			StreamTokenizer fontT = new StreamTokenizer(
				new CharArrayReader(psLine.substring(1).toCharArray()));
			String fontType = StringTokenUtil.getNextStringToken(psLine, fontT);
			String dummy = StringTokenUtil.getNextStringToken(psLine, fontT);
			int scaleVal = (int)Math.round(StringTokenUtil.getNextDoubleToken(psLine, fontT));
			if (fontType.toLowerCase().equals("helvetica"))
				currFont = new Font("Helvetica", Font.PLAIN, scaleVal);
			else if (fontType.toLowerCase().equals("helvetica-bold"))
				currFont = new Font("Helvetica", Font.BOLD, scaleVal);
			else if (fontType.toLowerCase().equals("helvetica-oblique"))
				currFont = new Font("Helvetica", Font.ITALIC, scaleVal);
		}
		if (!psLine.endsWith("lwstring"))
			continue;

		// check if nuc; bypass if so
		if ((psLine.charAt(0) == '(') && (psLine.charAt(2) == ')') &&
			(NucNode.isValidNucChar(Character.toUpperCase(psLine.charAt(1)))))
			continue;
		
		int firstLeftParenIndex = psLine.indexOf('(');
		int lastRightParenIndex = 0;
		for (int i = 0;i < psLine.length();i++)
		{
			if (psLine.charAt(i) == ')')
				lastRightParenIndex = i;
		}

		String labelStr = psLine.substring(firstLeftParenIndex + 1, 
			lastRightParenIndex).trim();
		
		// NEED to drop pure integers as nuc numbers
		try
		{
			Integer.parseInt(labelStr);
			continue;
		}
		catch (NumberFormatException e)
		{
		}

		StreamTokenizer streamT = new StreamTokenizer(new CharArrayReader(
			(psLine.substring(lastRightParenIndex + 1).toCharArray())));
		
		double x = StringTokenUtil.getNextDoubleToken(psLine, streamT);
		double y = StringTokenUtil.getNextDoubleToken(psLine, streamT);

		DrawStringObject strObj = new DrawStringObject(x, y,
			currFont, Color.black, labelStr.trim());
		strObj.setFont(currFont);
		strObj.update(tmpImgG);
		// debug("this.getXY: " + strObj.getX() + " " + strObj.getY());
		strObj.setX(strObj.getX() + strObj.getDeltaX() - this.getX());
		strObj.setY(strObj.getY() + strObj.getDeltaY() - this.getY());
		this.addLabel(strObj);
	}

	this.centerAtOrigin();
}

public void
setSSDataFromSSFile(File sstrFile)
throws Exception
{
	String fileName = sstrFile.getPath();
	if (fileName.endsWith(".ss"))
	{
		String prefixName = new String(fileName);
		int indexOfFileSeperator = prefixName.lastIndexOf(sstrFile.separatorChar);
		if (indexOfFileSeperator >= 0)
			prefixName = prefixName.substring(indexOfFileSeperator + 1, prefixName.length());
		this.setName(prefixName.substring(0, prefixName.length() - 3));
	}
	else
	{
		this.setName(fileName);
	}

	String listData = FileUtil.getFileAsString(fileName);

	StringTokenizer ssLineT = new StringTokenizer(listData, "\n");
	while (ssLineT.hasMoreTokens())
	{
		String ssLine = ssLineT.nextToken().trim();
		if (ssLine == null)
		{
			continue;
		}
		else if (ssLine.length() <= 0)
		{
			continue;
		}
		else if (ssLine.indexOf("#!") == 0)
		{
			// parse global command (see helixio.c InterpretSSParam)
			continue;
		}
		else if (ssLine.indexOf("#!") > 0)
		{
			// all old .ss files have attributes at bottom of file
			// so can be assured nuc exists.
			parseNucsAttribute(ssLine, this);
			continue;
		}
		else if (ssLine.indexOf("#") == 0)
		{
			// System.out.println("found comment: " + ssLine);
			continue;
		}
		else if (Character.isDigit(ssLine.charAt(0)))
		{
			// ss nuc line found
			// System.out.println(ssLine);
			Nuc2D nuc = parseSSNucLine(ssLine);
			nuc.setParentCollection(this);
			this.addItem((NucNode)nuc);
		}
		else
		{
			// ss nuc label found
			switch(ssLine.trim().charAt(0))
			{
			// l 303.00 -309.00 314.00 -327.00 10 0.00 0 0 0 0 0 0
			  case 'l' :
				parseLineLabelAttribute(ssLine);
			  	break;
			  case 't' :
				parseTriangleLabelAttribute(ssLine);
			  	break;
			  case 'p' :
				parseParallelogramLabelAttribute(ssLine);
			  	break;
			  case 's' :
				parseStringLabelAttribute(ssLine);
			  	break;
			}
		}
	}
	this.setSSBPNucs();
}

// need to read in a standard .ss file and get all the goods
public static SSData2D
getSSDataFromSSFile(File sstrFile)
throws Exception
{
	SSData2D ssData = new SSData2D();
	ssData.setSSDataFromSSFile(sstrFile);
	
	return(ssData);
}

// need to read in an old style XRNA .ps file and get all the goods
public static SSData2D
getSSDataFromPSFile(File sstrFile)
throws Exception
{
	SSData2D ssData = new SSData2D();
	ssData.setSSDataFromPSFile(sstrFile);
	
	return(ssData);
}

/**
** Parse an individual nuc line from a SS file. Return a Nuc2D
*/

public static Nuc2D
parseSSNucLine(String nucLine)
throws Exception
{
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(nucLine.toCharArray()));
	Nuc2D nuc = new Nuc2D(StringTokenUtil.getNextIntToken(nucLine, streamT));
	nuc.setNucChar(StringTokenUtil.getNextCharToken(nucLine, streamT));
	nuc.setX(StringTokenUtil.getNextDoubleToken(nucLine, streamT));
	nuc.setY(StringTokenUtil.getNextDoubleToken(nucLine, streamT));

	// bypass old notion of format type
	int dummy = StringTokenUtil.getNextIntToken(nucLine, streamT);

	nuc.setBasePairID(StringTokenUtil.getNextIntToken(nucLine, streamT));
	return(nuc);
}

public void
parseCharAttribute(int nuc1ID, int nuc2ID, String attLine, ComplexCollection list)
throws Exception
{
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(attLine.toCharArray()));

	StringTokenUtil.getNextCharToken(attLine, streamT);
	int nucFontSize = StringTokenUtil.getNextIntToken(attLine, streamT);
	int nucFontType = StringTokenUtil.getNextIntToken(attLine, streamT);

	String nucColor = attLine.substring(attLine.lastIndexOf(' '),
		attLine.length()).trim();
	Color nucFontColor = new Color(Integer.valueOf(nucColor, 16).intValue());

	Font nucFont = StringUtil.ssFontToFont(nucFontType, nucFontSize);

	if (nuc2ID <= 0)
		nuc2ID = nuc2ID;
	for (int nucID = nuc1ID;nucID <= nuc2ID;nucID++)
	{
		Nuc2D nuc = ((SSData2D)list).getNuc2DAt(nucID);
		nuc.setNucCharSymbol(nucFont);
		nuc.setColor(nucFontColor);
	}
}

public void
parseAdobeAttribute(int nuc1ID, int nuc2ID, String attLine, ComplexCollection list)
throws Exception
{
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(attLine.toCharArray()));

	StringTokenUtil.getNextCharToken(attLine, streamT);
	int nucFontSize = StringTokenUtil.getNextIntToken(attLine, streamT);
	char nucChar = StringTokenUtil.getNextCharToken(attLine, streamT);

	String nucColor = attLine.substring(attLine.lastIndexOf(' '),
		attLine.length()).trim();
	Color nucFontColor = new Color(Integer.valueOf(nucColor, 16).intValue());

	Font nucFont = new Font("TimesRoman", Font.PLAIN, nucFontSize);

	if (nuc2ID <= 0)
		nuc2ID = nuc2ID;
	for (int nucID = nuc1ID;nucID <= nuc2ID;nucID++)
	{
		Nuc2D nuc = ((SSData2D)list).getNuc2DAt(nucID);
		nuc.setAdobeSymbol(nucChar, nucFont, nucFontColor);
	}
}

// 4#!c 0 360 8 0 1 ff0000
public void
parseCircleAttribute(int nuc1ID, int nuc2ID, String attLine, ComplexCollection list)
throws Exception
{
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(attLine.toCharArray()));
	StringTokenUtil.getNextCharToken(attLine, streamT);
	double arc0 = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double arc1 = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double radius = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double lineWidth = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double tmpIsOpen = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	boolean isOpen = (tmpIsOpen == 1.0);

	String nucColor = attLine.substring(attLine.lastIndexOf(' '),
		attLine.length()).trim();
	Color nucFontColor = new Color(Integer.valueOf(nucColor, 16).intValue());
	if (nuc2ID <= 0)
		nuc2ID = nuc2ID;
	for (int nucID = nuc1ID;nucID <= nuc2ID;nucID++)
	{
		Nuc2D nuc = ((SSData2D)list).getNuc2DAt(nucID);
		nuc.setCircleSymbol(arc0, arc1, radius, lineWidth, isOpen, nucFontColor);
	}
}

public void
parseTriangleAttribute(int nuc1ID, int nuc2ID, String attLine, ComplexCollection list)
throws Exception
{
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(attLine.toCharArray()));
	StringTokenUtil.getNextCharToken(attLine, streamT);
	double topPtX = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double topPtY = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double leftPtX = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double leftPtY = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double rightPtX = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double rightPtY = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double angle = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double lineWidth = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double tmpIsOpen = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	boolean isOpen = (tmpIsOpen == 1.0);

	String nucColor = attLine.substring(attLine.lastIndexOf(' '),
		attLine.length()).trim();
	Color nucFontColor = new Color(Integer.valueOf(nucColor, 16).intValue());
	if (nuc2ID <= 0)
		nuc2ID = nuc2ID;
	for (int nucID = nuc1ID;nucID <= nuc2ID;nucID++)
	{
		Nuc2D nuc = ((SSData2D)list).getNuc2DAt(nucID);
		nuc.setTriangleSymbol(topPtX, topPtY, leftPtX, leftPtY,
			rightPtX, rightPtY, angle, lineWidth, isOpen, nucFontColor);
	}
}

public void
parseParallelogramAttribute(int nuc1ID, int nuc2ID, String attLine, ComplexCollection list)
throws Exception
{
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(attLine.toCharArray()));
	StringTokenUtil.getNextCharToken(attLine, streamT);
	double angle1 = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double side1 = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double angle2 = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double side2 = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double lineWidth = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	double tmpIsOpen = StringTokenUtil.getNextDoubleToken(attLine, streamT);
	boolean isOpen = (tmpIsOpen == 1.0);

	String nucColor = attLine.substring(attLine.lastIndexOf(' '),
		attLine.length()).trim();
	Color nucFontColor = new Color(Integer.valueOf(nucColor, 16).intValue());
	if (nuc2ID <= 0)
		nuc2ID = nuc2ID;
	for (int nucID = nuc1ID;nucID <= nuc2ID;nucID++)
	{
		Nuc2D nuc = ((SSData2D)list).getNuc2DAt(nucID);
		nuc.setParallelogramSymbol(angle1, side1, angle2, side2,
			lineWidth, isOpen, nucFontColor);
	}
}

// l 303.00 -309.00 314.00 -327.00 10 0.00 0 0 0 0 0 0
public void
parseLineLabelAttribute(String labelLine)
throws Exception
{
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(labelLine.toCharArray()));
	StringTokenUtil.getNextCharToken(labelLine, streamT);
	double x1 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double y1 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double x2 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double y2 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	int nucID = StringTokenUtil.getNextIntToken(labelLine, streamT);
	Nuc2D nuc = getNuc2DAt(nucID);

	/*
	if (nuc == null)
		throw new Exception("ERROR in parseLineLabelAttribute:\n" +
			" null nuc in parseline:\n" + labelLine + "\n");
	*/

	double lineWidth = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	// TESTING reseting linewidth from 0.0 to 1.0 just for comparison
	if (lineWidth < GraphicsUtil.MIN_LINEWIDTH)
		lineWidth = GraphicsUtil.MIN_LINEWIDTH;
	
	// going to run into trouble with non black (0) colors. for now just fake it.
	int tmpColor = StringTokenUtil.getNextIntToken(labelLine, streamT);

	//
	// String nucColor = labelLine.substring(labelLine.lastIndexOf(' '),
		// labelLine.length()).trim();
	// Color nucFontColor = new Color(Integer.valueOf(nucColor, 16).intValue());
	//

	Color nucFontColor = Color.black;

	if ((nuc == null) || (nucID == 0))
		this.addLabel(new DrawLineObject(
			x1 - this.getX(), y1 - this.getY(), x2 - this.getX(),
			y2 - this.getY(), lineWidth, nucFontColor));
	else
		nuc.addLabel(new DrawLineObject(
			x1 - nuc.getX(), y1 - nuc.getY(), x2 - nuc.getX(),
			y2 - nuc.getY(), lineWidth, nucFontColor));
}

// t 193.0 -339.0 0.0 0.0 6.0 -5.0 -4.0 5.0 -4.0 0.0 0 1.0 0 7bffcc
public void
parseTriangleLabelAttribute(String labelLine)
throws Exception
{
	/* THIS IS FOR LINE; NEED TO CHANGE TO TRIANGLE
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(labelLine.toCharArray()));
	StringTokenUtil.getNextCharToken(labelLine, streamT);
	double x1 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double y1 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double x2 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double y2 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	int nucID = StringTokenUtil.getNextIntToken(labelLine, streamT);
	Nuc2D nuc = getNuc2DAt(nucID);

	//
	// if (nuc == null)
		// throw new Exception("ERROR in parseTriangleLabelAttribute:\n" +
			// " null nuc in parseline:\n" + labelLine + "\n");
	//

	double lineWidth = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	// TESTING reseting linewidth from 0.0 to 1.0 just for comparison
	if (lineWidth < GraphicsUtil.MIN_LINEWIDTH)
		lineWidth = GraphicsUtil.MIN_LINEWIDTH;
	
	// going to run into trouble with non black (0) colors. for now just fake it.
	int tmpColor = StringTokenUtil.getNextIntToken(labelLine, streamT);

	//
	// String nucColor = labelLine.substring(labelLine.lastIndexOf(' '),
		// labelLine.length()).trim();
	// Color nucFontColor = new Color(Integer.valueOf(nucColor, 16).intValue());
	//

	Color nucFontColor = Color.black;

	if ((nuc == null) || (nucID == 0))
		this.addLabel(new DrawLineObject(
			x1 - this.getX(), y1 - this.getY(), x2 - this.getX(),
			y2 - this.getY(), lineWidth, nucFontColor));
	else
		nuc.addLabel(new DrawLineObject(
			x1 - nuc.getX(), y1 - nuc.getY(), x2 - nuc.getX(),
			y2 - nuc.getY(), lineWidth, nucFontColor));
	*/
}


// p 382.0 -223.0 0.0 25.0 23.0 115.0 10.0 0 0.0 1 0
// p 343.0 -222.0 0.0 -23068.0 23.0 62.0 10.0 0 0.0 1 0
public void
parseParallelogramLabelAttribute(String labelLine)
throws Exception
{
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(labelLine.toCharArray()));
	StringTokenUtil.getNextCharToken(labelLine, streamT);
	double x = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double y = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double z = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double angle1 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double side1 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double angle2 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double side2 = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	int nucID = StringTokenUtil.getNextIntToken(labelLine, streamT);
	Nuc2D nuc = getNuc2DAt(nucID);

	double lineWidth = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	// TESTING reseting linewidth from 0.0 to 1.0 just for comparison
	if (lineWidth < GraphicsUtil.MIN_LINEWIDTH)
		lineWidth = GraphicsUtil.MIN_LINEWIDTH;

	double tmpIsOpen = StringTokenUtil.getNextIntToken(labelLine, streamT);
	boolean isOpen = (tmpIsOpen == 1);
	
	// going to run into trouble with non black (0) colors. for now just fake it.
	int tmpColor = StringTokenUtil.getNextIntToken(labelLine, streamT);

	//
	// String nucColor = labelLine.substring(labelLine.lastIndexOf(' '),
		// labelLine.length()).trim();
	// Color nucFontColor = new Color(Integer.valueOf(nucColor, 16).intValue());
	//

	Color nucFontColor = Color.black;

	
	/*
	debug("SETTING PARALLELOGRAM:");
	debug("\t" + x + " " + y);
	debug("\t" + angle1 + " " + side1);
	debug("\t" + angle2 + " " + side2);
	*/
	if ((nuc == null) || (nucID == 0))
		this.addLabel(new DrawParallelogramObject(x, y, angle1, side1,
			angle2, side2, lineWidth, isOpen, nucFontColor));
	else
		nuc.addLabel(new DrawParallelogramObject(x, y, angle1, side1,
			angle2, side2, lineWidth, isOpen, nucFontColor));
}

//     x       y     z    ang  nuc  size font color
// s 411.00 -141.00 0.00  0.0  76   12    2     0     "ACCEPTOR"
public void
parseStringLabelAttribute(String labelLine)
throws Exception
{
	StreamTokenizer streamT = new StreamTokenizer(
		new CharArrayReader(labelLine.toCharArray()));
	StringTokenUtil.getNextCharToken(labelLine, streamT);
	double x = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	double y = StringTokenUtil.getNextDoubleToken(labelLine, streamT);
	StringTokenUtil.getNextDoubleToken(labelLine, streamT); // bypass z
	StringTokenUtil.getNextDoubleToken(labelLine, streamT); // bypass angle
	Nuc2D nuc = getNuc2DAt(StringTokenUtil.getNextIntToken(labelLine, streamT));

	int strFontSize = StringTokenUtil.getNextIntToken(labelLine, streamT);
	int strFontType = StringTokenUtil.getNextIntToken(labelLine, streamT);

	int colorType = StringTokenUtil.getNextIntToken(labelLine, streamT);
	// NEED to figure out how to read in hex
	// String nucColor = labelLine.substring(labelLine.lastIndexOf(' '),
		// labelLine.length()).trim();
	// Color strFontColor = new Color(Integer.valueOf(nucColor, 16).intValue());
	//
	Color strFontColor = Color.black;

	Font strFont = StringUtil.ssFontToFont(strFontType, strFontSize);

	// String str = StringTokenUtil.getNextStringToken(labelLine, streamT);
	String str = labelLine.substring(labelLine.indexOf('"') + 1,
		labelLine.lastIndexOf('"')).trim();

	ComplexCollection drwObj = ((nuc != null) ?
		(ComplexCollection)nuc : (ComplexCollection)this);
	if (drwObj != null)
	{
		// need to shift over string objects from a .ss file
		Graphics2D tmpImgG = GraphicsUtil.unitG2;

		if (strFontType == 12)
		{
			DrawAdobeObject adobeObj = null;
			if (str.equals("Y"))
			{
				adobeObj = new DrawAdobeObject(x, y,
					strFont, strFontColor, '\u03a8');
				adobeObj.update(tmpImgG);
				adobeObj.setX(adobeObj.getX() + adobeObj.getDeltaX() -
					drwObj.getX());
				adobeObj.setY(adobeObj.getY() + adobeObj.getDeltaY() -
					drwObj.getY());
				drwObj.addLabel(adobeObj);
			}
			else if (str.equals("y"))
			{
				adobeObj = new DrawAdobeObject(x, y,
					strFont, strFontColor, '\u03c8');
				adobeObj.update(tmpImgG);
				adobeObj.setX(adobeObj.getX() + adobeObj.getDeltaX() -
					drwObj.getX());
				adobeObj.setY(adobeObj.getY() + adobeObj.getDeltaY() -
					drwObj.getY());
				drwObj.addLabel(adobeObj);
			}
		}
		else
		{
			DrawStringObject strObj = new DrawStringObject(x, y,
				strFont, strFontColor, str);
			strObj.update(tmpImgG);
			strObj.setX(strObj.getX() + strObj.getDeltaX() - drwObj.getX());
			strObj.setY(strObj.getY() + strObj.getDeltaY() - drwObj.getY());
			drwObj.addLabel(strObj);
		}
		tmpImgG = null;
	}
	else
	{
		// set the label to this
	}
}

public void
parseNucsAttribute(String ssAttLine, ComplexCollection list)
throws Exception
{
	StringTokenizer st = new StringTokenizer(ssAttLine, "#!");
	if (st.countTokens() != 2)
		throw new Exception("Error in parseNucsAttribute, " +
			"invalid nucs attribute line: " + ssAttLine);
	String nucListStr = st.nextToken().trim();
	String attLine = st.nextToken().trim();
	if (nucListStr.indexOf(',') >= 0)
	{
		StringTokenizer nucListST = new StringTokenizer(nucListStr, ",");
		while (nucListST.hasMoreTokens())
		{
			String nucsStr = nucListST.nextToken().trim();
			if (nucsStr.indexOf('-') >= 0)
			{
				StringTokenizer rangeST = new StringTokenizer(nucsStr, "-");
				if (rangeST.countTokens() != 2)
					throw new Exception("Error in parseNucsAttribute, " +
						"invalid nucs attribute line: " + ssAttLine);
				int firstNuc = Integer.valueOf(rangeST.nextToken().trim()).intValue();
				int secondNuc = Integer.valueOf(rangeST.nextToken().trim()).intValue();
				parseAttType(firstNuc, secondNuc, attLine, list);

			}
			else if (Character.isDigit(nucsStr.charAt(0))) // then single nuc
			{
				int nuc = Integer.valueOf(nucsStr).intValue();
				parseAttType(nuc, nuc, attLine, list);
			}
			else
			{
				throw new Exception("Error in parseNucsAttribute, " +
					"invalid nucs attribute line: " + ssAttLine);
			}
		}
	}
	else if (nucListStr.indexOf('-') >= 0) // then single range
	{
		StringTokenizer rangeST = new StringTokenizer(nucListStr, "-");
		if (rangeST.countTokens() != 2)
			throw new Exception("Error in parseNucsAttribute, " +
				"invalid nucs attribute line: " + ssAttLine);
		int firstNuc = Integer.valueOf(rangeST.nextToken().trim()).intValue();
		int secondNuc = Integer.valueOf(rangeST.nextToken().trim()).intValue();
		parseAttType(firstNuc, secondNuc, attLine, list);
	}
	else if (Character.isDigit(nucListStr.charAt(0))) // then single nuc
	{
		int nuc = Integer.valueOf(nucListStr).intValue();
		parseAttType(nuc, nuc, attLine, list);
	}
	else
	{
		throw new Exception("Error in parseNucsAttribute, " +
			"invalid nucs attribute line: " + ssAttLine);
	}
}

private void
parseAttType(int firstNuc, int secondNuc, String attLine, ComplexCollection list)
throws Exception
{
	switch(attLine.charAt(0))
	{
	  case 'c' :
		// 4#!c 0 360 8 0 1 ff0000
		parseCircleAttribute(firstNuc, secondNuc, attLine, list);
	  	break;
	  case 's' :
		parseCharAttribute(firstNuc, secondNuc, attLine, list);
		break;
	  case 'S' :
		parseAdobeAttribute(firstNuc, secondNuc, attLine, list);
		break;
	  case 't' :
		parseTriangleAttribute(firstNuc, secondNuc, attLine, list);
		break;
	  case 'p' :
		parseParallelogramAttribute(firstNuc, secondNuc, attLine, list);
		break;
	  default :
		break;
	}
}

/*************** FORMATTING STUFF *************************/

public void
blockFormat(double startX, double startY, double nucGap, int colCount)
throws Exception
{
	int col = 0;
	int row = 0;
	for (int nucID = 1;nucID <= this.getNucCount();nucID++)
	{
		Nuc2D nuc = getNuc2DAt(nucID);
		if (col % colCount == 0)
		{
			col = 0;
			row++;
		}
		if (nuc == null)
		{
			col++;
			continue;
		}
		nuc.setX(startX + (col * nucGap));
		nuc.setY(startY - (row * nucGap));
		col++;
	}
}

private static final int maxCycleList = 80;

public Vector
getPseudoKnotList(int nuc0ID, int nuc1ID)
throws Exception
{
	this.clearFlagged();
	return (findPseudoKnots(nuc0ID, nuc1ID));
}

private Vector
findPseudoKnots(int nuc0ID, int nuc1ID)
{
	Vector pseudoKnotList = new Vector();

	int i = 0;
	int[] levelList = new int[maxCycleList];

	int levelListCount = getCycleList(nuc0ID, nuc1ID, levelList);
	if(levelListCount == 0) // at hairpin
		return (pseudoKnotList);
	if(levelListCount == 1) // then stack the helix
	{
		findDomainPseudoKnots(levelList[0], pseudoKnotList);
	}
	else
	{
		i = 0;
		while (true)
		{
			if(levelList[i] == 0)
				break;
			findDomainPseudoKnots(levelList[i], pseudoKnotList);
			i+=2;
		}
	}
	return (pseudoKnotList);
}

private void
findDomainPseudoKnots(int currNucID, Vector pseudoKnotList)
{
	int nucID = 0;
	int bpNucID = 0;
	int tmpBPNucID = 0;

	while (true)
	{
		if (currNucID > getNucCount())
			break;
		if (this.getNucAt(currNucID).isHelixStart())
		{
			bpNucID = (this.getNucAt(currNucID)).getBasePairID();
			for(nucID = currNucID;nucID <= bpNucID;nucID++)
			{
				if ((this.getNucAt(nucID)).isBasePair())
				{
					tmpBPNucID = this.getNucAt(nucID).getBasePairID();
					if(!((tmpBPNucID >= currNucID) &&
						(tmpBPNucID <= bpNucID)))
					{
						this.getNucAt(nucID).setFlagged(true);
						this.getNucAt(tmpBPNucID).setFlagged(true);
						// debug("FOUND PK NUCS: " + nucID + " " + tmpBPNucID);
						/* NEED to add fivePNuc only. BP info is implicit
						pseudoKnotList.add(
							new RNABasePair(this.getNucAt(nucID),
								this.getNucAt(tmpBPNucID)));
						*/
					}
				}
			}
		}
		// else if(abs(this[currNucID]->pointtype) == LHelix1End)
		else if (this.getNucAt(currNucID).isHelixStart())
		{
			if(!this.getNucAt(currNucID).getFlagged())
				findPseudoKnots(currNucID, this.getNucAt(currNucID).getBasePairID());
			break;
		}
		currNucID++;
	}
}

private int
getCycleList(int nuc0ID, int nuc1ID, int[] levelList)
{
	int levelListCount = 0;
	int i = 0;
	int nucID = nuc0ID - 1;

	levelList[0] = 0;

	while(true)
	{
		if(++nucID > nuc1ID)
		{
			levelList[i] = 0;
			break;
		}
		if(nucID == nuc1ID)
		{
			levelList[i] = 0;
			break;
		}
		// if(abs(this[nucID]->pointtype) == LHelix1Begin)
		if (this.getNucAt(nucID).isHelixStart())
		{
			levelListCount++;
			levelList[i++] = nucID;
			nucID = (this.getNucAt(nucID)).getBasePair().getID();
			levelList[i++] = nucID;
		}
	}
	return (levelListCount);
}

/*
#define MAXLEVELLIST    80

FindPseudoKnots(sstr, curnuc, lastnuc, bpcount, out)
NODETREE sstr;
int curnuc, lastnuc, *bpcount;
FILE *out;
{
	register int i;
	int levellist[MAXLEVELLIST], levellistcount;

	GetCycleList(sstr, curnuc, lastnuc, levellist, &levellistcount);
	if(levellistcount == 0) // at hairpin
		return;
	if(levellistcount == 1) // then stack the helix
	{
		FindDomainPseudoKnots(sstr, levellist[0], bpcount, out);
	}
	else
	{
		i = 0;
		while(TRUE)
		{
			if(levellist[i] == 0)
				break;
			FindDomainPseudoKnots(sstr, levellist[i], bpcount, out);
			i+=2;
		}
	}

}

FindDomainPseudoKnots(sstr, currnuc, bpcount, out)
NODETREE sstr;
int currnuc, *bpcount;
FILE *out;
{
	int nuc, bpnuc, tmpbpnuc;

	while(TRUE)
	{
		if(abs(sstr[currnuc]->pointtype) == LHelix1Begin)
		{
			bpnuc = sstr[currnuc]->basepair;
			for(nuc = currnuc;nuc <= bpnuc;nuc++)
			{
				if(StrNucIsBasePair(sstr, nuc))
				{
					tmpbpnuc = sstr[nuc]->basepair;
					if(!((tmpbpnuc >= currnuc) &&
						(tmpbpnuc <= bpnuc)))
					{
						sstr[nuc]->markstr =
							sstr[tmpbpnuc]->markstr =
								TRUE;
						if(out != (FILE *)NULL)
						fprintf(out, "%d %d\n",
							nuc, tmpbpnuc);
						(*bpcount)++;
					}
				}
			}
		}
		else if(abs(sstr[currnuc]->pointtype) == LHelix1End)
		{
			if(!sstr[currnuc]->markstr)
				FindPseudoKnots(sstr, currnuc,
					sstr[currnuc]->basepair, bpcount, out);
			break;
		}
		currnuc++;
	}
}

GetCycleList(sstr, curnuc, endnuc, levellist, levellistcount)
NODETREE sstr;
int curnuc, endnuc, levellist[], *levellistcount;
{
	register int nuc, i = 0;

	*levellistcount = levellist[i] = 0;
	nuc = curnuc - 1;
	while(TRUE)
	{
		if(++nuc > endnuc)
		{
			levellist[i] = 0;
			break;
		}
		if(nuc == endnuc)
		{
			levellist[i] = 0;
			break;
		}
		if(abs(sstr[nuc]->pointtype) == LHelix1Begin)
		{
			(*levellistcount)++;
			levellist[i++] = nuc;
			nuc = sstr[nuc]->basepair;
			levellist[i++] = nuc;
		}
	}

}
*/

public Vector
partitionSSData(Nuc2D nuc0, Nuc2D nuc1)
throws Exception
{
	return (this.partitionSSData(nuc0.getID(), nuc1.getID()));
}

public Vector
partitionSSData(int nuc0ID, int nuc1ID)
throws Exception
{
	NucNode nuc0 = this.getNucAt(nuc0ID);
	if (nuc0 == null)
		throw new Exception("Error in SSData.partitionSSData(): " +
			"nuc for nuc0ID: " + nuc0ID + " is non-existent");
	NucNode nuc1 = this.getNucAt(nuc1ID);
	if (nuc1 == null)
		throw new Exception("Error in SSData.partitionSSData(): " +
			"nuc for nuc1ID: " + nuc1ID + " is non-existent");
	
	Vector partitionList = new Vector();
	SSData2D sstr = null;

	// NEED to put nucs into new sstr in proper position

	if ((nuc0.getID() == 1) && (nuc1.getID() == this.getNucCount()))
	{
		sstr = new SSData2D(this.getName() + "_0");
		for (int nucID = 1;nucID <= this.getNucCount();nucID++)
			sstr.addItem(this.getNuc2DAt(nucID));
		partitionList.add(sstr);
	}
	else if (nuc0.getID() == 1)
	{
		sstr = new SSData2D(this.getName() + "_0");
		for (int nucID = 1;nucID <= nuc1ID;nucID++)
			sstr.addItem(this.getNuc2DAt(nucID));
		partitionList.add(sstr);

		sstr = new SSData2D(this.getName() + "_1");
		for (int nucID = nuc1ID + 1;nucID <= this.getNucCount();nucID++)
			sstr.addItem(this.getNuc2DAt(nucID));
		partitionList.add(sstr);
	}
	else if (nuc1.getID() == this.getNucCount())
	{
		sstr = new SSData2D(this.getName() + "_0");
		for (int nucID = 1;nucID < nuc0ID;nucID++)
			sstr.addItem(this.getNuc2DAt(nucID));
		partitionList.add(sstr);

		sstr = new SSData2D(this.getName() + "_1");
		for (int nucID = nuc0ID;nucID <= this.getNucCount();nucID++)
			sstr.addItem(this.getNuc2DAt(nucID));
		partitionList.add(sstr);
	}
	else
	{
		sstr = new SSData2D(this.getName() + "_0");
		for (int nucID = 1;nucID < nuc0ID;nucID++)
			sstr.addItem(this.getNuc2DAt(nucID));
		partitionList.add(sstr);

		sstr = new SSData2D(this.getName() + "_1");
		for (int nucID = nuc0ID;nucID <= nuc1ID;nucID++)
			sstr.addItem(this.getNuc2DAt(nucID));
		partitionList.add(sstr);

		sstr = new SSData2D(this.getName() + "_2");
		for (int nucID = nuc1ID + 1;nucID <= this.getNucCount();nucID++)
			sstr.addItem(this.getNuc2DAt(nucID));
		partitionList.add(sstr);
	}
	return (partitionList);
}



/*************** END FORMATTING STUFF *************************/


public void
printNucPosAssertsToFile(String fileName)
throws Exception
{
	File outFile = new File("junk");
	StringWriter strWriter = null;
	PrintWriter printWriter = null;
	try
	{
		strWriter = new StringWriter();
		printWriter = new PrintWriter(strWriter);
		for (int nucID = 1;nucID <= this.getNucCount();nucID++)
		{
			Nuc2D nuc = this.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			printWriter.println(
				"assertTrue(MathUtil.precisionEquality(sstr.getNuc2DAt(" +
				nucID + ").getX(), " + nuc.getX() + "));");
			printWriter.println(
				"assertTrue(MathUtil.precisionEquality(sstr.getNuc2DAt(" +
				nucID + ").getY(), " + nuc.getY() + "));");
		}
	}
	catch (Exception e)
	{
		debug("Error, file not written:\n" + e.toString());
		throw e;
	}
	try
	{
		outFile.createNewFile();
		FileWriter genFileWriter = new FileWriter(outFile);
		PrintWriter pWriter = new PrintWriter(
			new BufferedWriter(genFileWriter), true);
		pWriter.print(strWriter.toString());
		pWriter.flush();
		pWriter.close();
	}
	catch (Exception e)
	{
		throw e;
	}
}

public ComplexScene2D
wrapInComplexScene2D(String name)
throws Exception
{
	ComplexScene2D complexScene = new ComplexScene2D(name);
	ComplexSSDataCollection2D complexSSDataCollection =
		new ComplexSSDataCollection2D(name);
	complexSSDataCollection.addItem(this);
	complexScene.addItem(complexSSDataCollection);

	return (complexScene);
}

public ComplexScene2D
wrapInComplexScene2D()
throws Exception
{
	return (this.wrapInComplexScene2D(this.getName()));
}

// this implies that only sstr is getting written to new xrna
// file and therefore have to go through and unbasepair any
// nucs basepaired outside of sstr.
// MIGHT want to put this into NucCollection so can write out
// any nuc collection
public void
writeNewComplexSceneXML(String fileName)
throws Exception
{
	this.unsetNonSelfRefBasepairs();
	this.wrapInComplexScene2D().printComplexXML(new File(fileName));
}

public static SSData2D
getFirstFromXML(String fileName)
throws Exception
{
	ComplexXMLParser complexXMLParser = new ComplexXMLParser();
	complexXMLParser.parse(new FileReader(fileName));
	ComplexScene2D complexScene = complexXMLParser.getComplexScene();

	return (complexScene.getFirstComplexSSData2D());
}

public Font
getBestGuessNucLabelFont(int currNucID)
{
	for (int nucID = currNucID + 1;nucID <= this.getNucCount();nucID++)
	{
		Nuc2D refNuc = this.getNuc2DAt(nucID);
		if (refNuc == null)
			continue;
		if (!refNuc.hasNucLabel())
			continue;
		DrawStringObject numberLabel = refNuc.getNumberLabel();
		if ((numberLabel != null) && (numberLabel.getFont() != null))
			return (numberLabel.getFont());
	}

	for (int nucID = currNucID - 1;nucID > 0;nucID--)
	{
		Nuc2D refNuc = this.getNuc2DAt(nucID);
		if (refNuc == null)
			continue;
		if (!refNuc.hasNucLabel())
			continue;
		DrawStringObject numberLabel = refNuc.getNumberLabel();
		if ((numberLabel != null) && (numberLabel.getFont() != null))
			return (numberLabel.getFont());
	}

	return (null);
}

public boolean
resetLocationFromHashtable(int level)
throws Exception
{
	boolean undoAtLevelFound = false;
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0ID = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1ID = (Nuc2D)((NucNode)delineators.elementAt(i+1));
		for (int nucID = nuc0ID.getID();nucID <= nuc1ID.getID();nucID++)
		{
			Nuc2D nuc = getNuc2DAt(nucID);
			if (nuc.resetLocationFromHashtable(level))
				undoAtLevelFound = true;
			if (nuc.hasNucLabel())
			{
				DrawStringObject strObj = nuc.getNumberLabel();
				if (strObj != null)
				{
					if (strObj.resetLocationFromHashtable(level))
						undoAtLevelFound = true;
				}
				DrawLineObject lineObj = nuc.getLineLabel();
				if (lineObj != null)
				{
					if (lineObj.resetLocationFromHashtable(level))
						undoAtLevelFound = true;
				}
			}
		}
	}

	return (undoAtLevelFound);
}

public void
clearLocationFromHashtable(int level)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0ID = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1ID = (Nuc2D)((NucNode)delineators.elementAt(i+1));
		for (int nucID = nuc0ID.getID();nucID <= nuc1ID.getID();nucID++)
		{
			Nuc2D nuc = getNuc2DAt(nucID);
			nuc.clearLocationFromHashtable(level);
			if (nuc.hasNucLabel())
			{
				DrawStringObject strObj = nuc.getNumberLabel();
				if (strObj != null)
				{
					strObj.clearLocationFromHashtable(level);
				}
				DrawLineObject lineObj = nuc.getLineLabel();
				if (lineObj != null)
				{
					lineObj.clearLocationFromHashtable(level);
				}
			}
		}
	}
}

public boolean
resetBasePairFromHashtable(int level)
throws Exception
{
	boolean undoAtLevelFound = false;
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0ID = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1ID = (Nuc2D)((NucNode)delineators.elementAt(i+1));
		for (int nucID = nuc0ID.getID();nucID <= nuc1ID.getID();nucID++)
		{
			Nuc2D nuc = getNuc2DAt(nucID);
			if (nuc.resetBasePairFromHashtable(level))
				undoAtLevelFound = true;
		}
	}

	return (undoAtLevelFound);
}

public void
clearBasePairFromHashtable(int level)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0ID = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1ID = (Nuc2D)((NucNode)delineators.elementAt(i+1));
		for (int nucID = nuc0ID.getID();nucID <= nuc1ID.getID();nucID++)
		{
			Nuc2D nuc = getNuc2DAt(nucID);
			nuc.clearBasePairFromHashtable(level);
		}
	}
}

public void
renumber(int startNucID)
throws Exception
{
	if (startNucID != 1)
		throw new Exception("NOT YET");

	if (this.getNuc2DAt(1) != null)
		throw new Exception("ALREADY NUMBERED STARTING WITH 1");

	// find first non-null nuc
	Nuc2D startNuc = null;
	for (int nucID = 1;nucID <= this.getNucCount();nucID++)
	{
		startNuc = getNuc2DAt(nucID);
		if (startNuc == null)
			continue;
		break;
	}
	int tmpStartNucID = startNuc.getID();
	int initID = 0;
	for (int nucID = tmpStartNucID;nucID <= this.getNucCount();nucID++)
	{
		Nuc2D nuc = getNuc2DAt(nucID);
		if (nuc == null)
			continue;
		initID++;
		nuc.setID(initID);
		this.setItemAt(nuc, initID);
	}
}

public void
renumber()
throws Exception
{
	this.renumber(1);
}

public static void
debug(String s)
{
	System.err.println("SSData2D-> " + s);
}

}
