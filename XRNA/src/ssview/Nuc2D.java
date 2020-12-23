package ssview;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.font.LineMetrics;
import java.awt.image.*;
import java.awt.geom.*;

import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import javax.swing.*;

import jimage.DrawObject;
import jimage.DrawAdobeObject;
import jimage.DrawCharObject;
import jimage.DrawCircleObject;
import jimage.DrawFontObject;
import jimage.DrawObjectCollection;
import jimage.DrawStringObject;
import jimage.RayIntersectDrawStringObject;
import jimage.DrawTriangleObject;
import jimage.DrawParallelogramObject;
import jimage.DrawLineObject;

import javax.vecmath.*;

import util.*;
import util.math.*;

// info for one planar graph nuc in a structure
public class
Nuc2D
extends NucNode
{

public
Nuc2D()
throws Exception
{
	super();
	this.setUseSymbol(false);
	this.setNucCharSymbol();
	this.setIsSchematic(false);
}

public
Nuc2D(int id)
throws Exception
{
	super(id);
	this.setUseSymbol(false);
	this.setNucCharSymbol();
	this.setIsSchematic(false);
}

public
Nuc2D(char nucChar, int id)
throws Exception
{
	super(nucChar, id);
	this.setUseSymbol(false);
	this.setNucCharSymbol();
	this.setIsSchematic(false);
}

public
Nuc2D(char nucChar)
throws Exception
{
	super(nucChar);
	this.setUseSymbol(false);
	this.setNucCharSymbol();
	this.setIsSchematic(false);
}

public
Nuc2D(char nucChar, double x, double y)
throws Exception
{
	super(nucChar);
	this.setXY(x, y);
	this.setNucCharSymbol();
	this.setUseSymbol(false);
}

// copy of parameter nucnode
public
Nuc2D(NucNode nuc)
throws Exception
{
	super (nuc);
}

// copy of parameter nuc2d
public
Nuc2D(Nuc2D nuc)
throws Exception
{
	this ((NucNode)nuc);
	this.setUseSymbol(nuc.getUseSymbol());
	this.setIsSchematic(nuc.getIsSchematic());
	this.setSchematicColor(nuc.getSchematicColor());
	this.setSchematicLineWidth(nuc.getSchematicLineWidth());
	this.setSchematicBPLineWidth(nuc.getSchematicBPLineWidth());
	this.setFont(nuc.getFont());
	this.setNucCharSymbol(nuc.getFont());
	this.setNucChar(nuc.getNucChar());
	this.setXY(nuc.getX(), nuc.getY());
	this.setColor(nuc.getColor());
	// Not sure I need these yet:
	// this.setParentCollection(nuc.getParentSSData2D());
	// this.setBoundingBox(nuc.getBoundingBox());
	//
}

private boolean isFormatted = false;

public void
setIsFormatted(boolean isFormatted)
{
    this.isFormatted = isFormatted;
}

public boolean
getIsFormatted()
{
    return (this.isFormatted);
}

public boolean
isFormatted()
{
    return (this.getIsFormatted());
}

public Point2D
getPoint2D()
throws Exception
{
	if (!this.isFormatted())
		throw new ComplexException("Error in Nuc2D.getPoint2D()",
			ComplexDefines.RNA_SINGLE_NUC_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_ERROR_MSG, "Nuc: " + this.getID());
	return (super.getPoint2D());
}

private boolean useSymbol = false;

public void
setUseSymbol(boolean useSymbol)
{
    this.useSymbol = useSymbol;
}

public boolean
getUseSymbol()
{
    return (this.useSymbol);
}

public Nuc2D
getBasePair2D()
{
	return ((Nuc2D)this.getBasePair());
}

private boolean isSchematic = false;

public void
setIsSchematic(boolean isSchematic)
{
	this.isSchematic = isSchematic;
}

public boolean
getIsSchematic()
{
    return (this.isSchematic);
}

public boolean
isSchematic()
{
    return (this.isSchematic);
}

public boolean
isSchematicWithSymbol()
{
    return (this.isSchematic() && this.isSymbol());
}

private Color schematicColor = Color.black;

public void
setSchematicColor(Color schematicColor)
{
	this.schematicColor = schematicColor;
}

public Color
getSchematicColor()
{
	return (this.schematicColor);
}

private double schematicLineWidth = 1.5;

public void
setSchematicLineWidth(double schematicLineWidth)
{
    this.schematicLineWidth = schematicLineWidth;
}

public double
getSchematicLineWidth()
{
    return (this.schematicLineWidth);
}

private double schematicBPLineWidth = 1.0;

public void
setSchematicBPLineWidth(double schematicBPLineWidth)
{
    this.schematicBPLineWidth = schematicBPLineWidth;
}

public double
getSchematicBPLineWidth()
{
    return (this.schematicBPLineWidth);
}

// if nuc is base pair and is not a schematic or is a schematic but is a
// nuc symbol then this is extra distance between canonical schematic base pair
// line and the intersect of that line to the nuc letter or symbol
private double bpSchemGap = 2.0;

public void
setBPSchemGap(double bpSchemGap)
{
    this.bpSchemGap = bpSchemGap;
}

public double
getBPSchemGap()
{
    return (this.bpSchemGap);
}

// if nuc is base pair and is not a schematic or is a schematic but is a
// nuc symbol then this is extra distance between schematic line and
// the intersect of that line to the nuc letter or symbol from the 5p
// direction.
private double fpSchemGap = 2.0;

public void
setFPSchemGap(double fpSchemGap)
{
    this.fpSchemGap = fpSchemGap;
}

public double
getFPSchemGap()
{
    return (this.fpSchemGap);
}

// if nuc is base pair and is not a schematic or is a schematic but is a
// nuc symbol then this is extra distance between schematic line and
// the intersect of that line to the nuc letter or symbol from the 3p
// direction.
private double tpSchemGap = 2.0;

public void
setTPSchemGap(double tpSchemGap)
{
    this.tpSchemGap = tpSchemGap;
}

public double
getTPSchemGap()
{
    return (this.tpSchemGap);
}

public void
setFont(Object fontObject)
{
	if (this.getDrawCharObject() == null)
		return;
	if (fontObject instanceof Font)
		this.getDrawCharObject().setFont((Font)fontObject);
}

public Font
getFont()
{
	if (this.getDrawCharObject() == null)
		return (null);
	return (this.getDrawCharObject().getFont());
}

public void
runSetIsNucPath(boolean isNucPath, double pathWidth, Color color)
throws Exception
{
	this.setIsNucPath(isNucPath);
	this.setNucPathLineWidth(pathWidth);
	this.setNucPathColor(color);
}

public void
setNucChar(char nucChar)
throws Exception
{
	super.setNucChar(nucChar);
	if (this.getDrawCharObject() == null)
		setDrawCharObject(new DrawCharObject(nucChar));
	else
		this.getDrawCharObject().setDrawChar(nucChar);
}

public void
setSymbol(Object drawObject)
throws Exception
{
	if (drawObject instanceof DrawCircleObject)
		this.setCircleSymbol((DrawCircleObject)drawObject);
	else if (drawObject instanceof DrawTriangleObject)
		this.setTriangleSymbol((DrawTriangleObject)drawObject);
	else if (drawObject instanceof DrawParallelogramObject)
		this.setParallelogramSymbol((DrawParallelogramObject)drawObject);
	else if (drawObject instanceof DrawCharObject)
		this.setCharSymbol((DrawCharObject)drawObject);
}

public void
setNucCharSymbol()
{
	// default for now
	// sve for trna:
	// this.setNucCharSymbol(new Font("Helvetica", Font.PLAIN, 18));

	// sve for 16s
	this.setNucCharSymbol(new Font("Helvetica", Font.PLAIN, ComplexDefines.DEFAULT_NUC_FONT_SIZE));
}

public void
setNucCharSymbol(Font font)
{
	DrawCharObject drawObject = null;
	if (getDrawCharObject() == null)
	{
		drawObject = new DrawCharObject(this.getNucChar());
		setDrawCharObject(drawObject);
	}
	else
	{
		drawObject = getDrawCharObject();
	}
	drawObject.setFont(font);
}

public void
setAdobeSymbol(char nucChar, Font font, Color color)
throws Exception
{
	this.setUseSymbol(true);
	DrawAdobeObject drawObject = null;
	if (getDrawAdobeObject() == null)
	{
		switch (nucChar)
		{
		  case 'Y' : // leave for converting .ss files
			drawObject = new DrawAdobeObject('\u03a8');
			break;
		  case 'y' : // leave for converting .ss files
			drawObject = new DrawAdobeObject('\u03c8');
			break;
		  default :
			drawObject = new DrawAdobeObject(nucChar);
		  	break;
		}
		setDrawAdobeObject(drawObject);
	}
	else
	{
		drawObject = getDrawAdobeObject();
	}
	drawObject.setFont(font);
	drawObject.setColor(color);
	drawObject.setX(0.0);
	drawObject.setY(0.0);
}

public void
setCircleSymbol(double arc0, double arc1, double radius,
	double lineWidth, boolean isOpen, Color color)
throws Exception
{
	this.setUseSymbol(true);
	DrawCircleObject circle = null;
	if (getDrawCircleObject() == null)
	{
		circle = new DrawCircleObject(
			0.0, 0.0, radius, arc0, arc1, color, isOpen, lineWidth);
		setDrawCircleObject(circle);
	}
	else
	{
		circle = getDrawCircleObject();
		circle.setRadius(radius);
		circle.setStartAngle(arc0);
		circle.setAngleExtent(arc1);
		circle.setIsOpen(isOpen);
		circle.setLineWidth(lineWidth);
		circle.setCircleExtents();
	}
	circle.setColor(color);
}

public void
setCircleSymbol(DrawCircleObject circle)
throws Exception
{
	this.setCircleSymbol(circle.getStartAngle(), circle.getAngleExtent(),
		circle.getRadius(), circle.getLineWidth(), circle.getIsOpen(),
		circle.getColor());
}

public void
setTriangleSymbol(
	double topPtX, double topPtY,
	double leftPtX, double leftPtY,
	double rightPtX, double rightPtY,
	double angle,
	double lineWidth, boolean isOpen, Color color)
throws Exception
{
	this.setUseSymbol(true);
	DrawTriangleObject triangle = null;
	if (getDrawTriangleObject() == null)
	{
		triangle = new DrawTriangleObject(
			0.0, 0.0,
			topPtX, topPtY, leftPtX, leftPtY, rightPtX, rightPtY,
			angle, lineWidth, isOpen, color);
		setDrawTriangleObject(triangle);
	}
	else
	{
		triangle = getDrawTriangleObject();
	}
	triangle.setColor(color);
}

public void
setTriangleSymbol(
	double height, double baseWidth,
	double angle, double scale,
	double lineWidth, boolean isOpen, Color color)
throws Exception
{
	this.setUseSymbol(true);
	DrawTriangleObject triangle = null;
	if (getDrawTriangleObject() == null)
	{
		triangle = new DrawTriangleObject(
			height, baseWidth,
			angle, scale, lineWidth, isOpen, color);
		setDrawTriangleObject(triangle);
	}
	else
	{
		triangle = getDrawTriangleObject();
		triangle.setLineWidth(lineWidth);
		triangle.setIsOpen(isOpen);
		triangle.set(height, baseWidth, angle, scale);
	}
	triangle.setColor(color);
}

public void
setTriangleSymbol(DrawTriangleObject triangle)
throws Exception
{
	this.setTriangleSymbol(
		triangle.getHeight(), triangle.getBaseWidth(),
		triangle.getAngle(), triangle.getScale(),
		triangle.getLineWidth(),
		triangle.getIsOpen(), triangle.getColor());
}

public void
setParallelogramSymbol(
	double angle1, double side1, double angle2, double side2,
	double lineWidth, boolean isOpen, Color color)
throws Exception
{
	this.setUseSymbol(true);
	DrawParallelogramObject drawObject = null;
	if (getDrawParallelogramObject() == null)
	{
		drawObject = new DrawParallelogramObject(
			0.0, 0.0, angle1, side1, angle2, side2, lineWidth, isOpen, color);
		setDrawParallelogramObject(drawObject);
	}
	else
	{
		drawObject = getDrawParallelogramObject();
		drawObject.set(angle1, side1, angle2, side2, lineWidth, isOpen);
	}
	drawObject.setColor(color);
}

public void
setParallelogramSymbol(DrawParallelogramObject parallelogram)
throws Exception
{
	this.setParallelogramSymbol(
		parallelogram.getAngle1(),
		parallelogram.getSide1(),
		parallelogram.getAngle2(),
		parallelogram.getSide2(),
		parallelogram.getLineWidth(),
		parallelogram.getIsOpen(),
		parallelogram.getColor());
}

public void
setCharSymbol(char symbolChar, Font font)
throws Exception
{
	this.setUseSymbol(true);
	DrawCharObject drawObject = null;
	if (getDrawCharSymbolObject() == null)
	{
		drawObject = new DrawCharObject(symbolChar, font);
		setDrawCharSymbolObject(drawObject);
	}
	else
	{
		drawObject = getDrawCharSymbolObject();
		drawObject.setDrawChar(symbolChar);
		drawObject.setFont(font);
	}
	drawObject.update(GraphicsUtil.unitG2);
}

// any char except nucChar
public void
setCharSymbol(DrawCharObject drawObject)
throws Exception
{
	this.setCharSymbol(drawObject.getDrawChar(), ((DrawCharObject)drawObject).getFont());
	this.setUseSymbol(true);
}

// HERE 0
public void
setNucSymbolDrawObject(DrawObject drwObj)
{
	if (drwObj instanceof DrawAdobeObject)
		setDrawAdobeObject((DrawAdobeObject)drwObj);
	else if (drwObj instanceof DrawCircleObject)
		setDrawCircleObject((DrawCircleObject)drwObj);
	else if (drwObj instanceof DrawTriangleObject)
		setDrawTriangleObject((DrawTriangleObject)drwObj);
	else if (drwObj instanceof DrawParallelogramObject)
		setDrawParallelogramObject((DrawParallelogramObject)drwObj);
	else if (drwObj instanceof DrawCharObject)
		setDrawCharSymbolObject((DrawCharObject)drwObj);
	this.setUseSymbol(true);	
}

private DrawCharObject drawCharObject = null;

public void
setDrawCharObject(DrawCharObject drawCharObject)
{
    this.drawCharObject = drawCharObject;
	this.drawCharObject.setParentCollection(this);
}

public DrawCharObject
getDrawCharObject()
{
    return (this.drawCharObject);
}

private DrawAdobeObject drawAdobeObject = null;

public void
setDrawAdobeObject(DrawAdobeObject drawAdobeObject)
{
    this.drawAdobeObject = drawAdobeObject;
	this.drawAdobeObject.setParentCollection(this);
}

public DrawAdobeObject
getDrawAdobeObject()
{
    return (this.drawAdobeObject);
}

private DrawCircleObject drawCircleObject = null;

public void
setDrawCircleObject(DrawCircleObject drawCircleObject)
{
    this.drawCircleObject = drawCircleObject;
	this.drawCircleObject.setParentCollection(this);
}

public DrawCircleObject
getDrawCircleObject()
{
    return (this.drawCircleObject);
}

private DrawTriangleObject drawTriangleObject = null;

public void
setDrawTriangleObject(DrawTriangleObject drawTriangleObject)
{
    this.drawTriangleObject = drawTriangleObject;
	this.drawTriangleObject.setParentCollection(this);
}

public DrawTriangleObject
getDrawTriangleObject()
{
    return (this.drawTriangleObject);
}

private DrawParallelogramObject drawParallelogramObject = null;

public void
setDrawParallelogramObject(DrawParallelogramObject drawParallelogramObject)
{
    this.drawParallelogramObject = drawParallelogramObject;
	this.drawParallelogramObject.setParentCollection(this);
}

public DrawParallelogramObject
getDrawParallelogramObject()
{
    return (this.drawParallelogramObject);
}

private DrawCharObject drawCharSymbolObject = null;

public void
setDrawCharSymbolObject(DrawCharObject drawCharSymbolObject)
{
    this.drawCharSymbolObject = drawCharSymbolObject;
	this.drawCharSymbolObject.setParentCollection(this);
}

public DrawCharObject
getDrawCharSymbolObject()
{
    return (this.drawCharSymbolObject);
}

public boolean
hasStandardAttributes()
{
	return (
	(this.getColor() == Color.black) &&
	(this.getFont().getName().equals("Helvetica")) &&
	(this.getFont().getSize() == 8) &&
	(this.getFont().getStyle() == Font.PLAIN)
	);
}

public boolean
hasEqualFontAttributes(Nuc2D nuc)
{
	if (nuc == null)
		return (false);

	if (this.getColor() == null)
	{
		debug("error: this.getColor() is null: " + this.getID());
	}
	if (nuc.getColor() == null)
	{
		debug("error: nuc.getColor() is null: " + nuc.getID());
	}
	if (this.getFont() == null)
	{
		debug("error: this.getFont() is null: " + this.getID());
	}
	if (nuc.getFont() == null)
	{
		debug("error: nuc.getFont() is null: " + nuc.getID());
	}

	if ((this.getColor() == null) && (nuc.getColor() != null))
		return (false);
	if ((this.getColor() != null) && (nuc.getColor() == null))
		return (false);
	if ((this.getFont() == null) && (nuc.getFont() != null))
		return (false);
	if ((this.getFont() != null) && (nuc.getFont() == null))
		return (false);
	
	if ((this.getFont() == null) && (nuc.getFont() == null))
		return (this.getColor() == nuc.getColor());

	return (
	(this.getColor() == nuc.getColor()) &&
	(this.getFont().getName().equals(
		nuc.getFont().getName())) &&
	(this.getFont().getSize() ==
		nuc.getFont().getSize()) &&
	(this.getFont().getStyle() ==
		nuc.getFont().getStyle())
	);
}

public boolean
hasEqualSchematicAttributes(Nuc2D nuc)
{
	if (nuc == null)
	{
		return (false);
	}

	return (
	(this.getIsSchematic() == nuc.getIsSchematic()) &&
	(this.getSchematicColor() == nuc.getSchematicColor()) &&
	(this.getSchematicLineWidth() == nuc.getSchematicLineWidth()) &&
	(this.getSchematicBPLineWidth() == nuc.getSchematicBPLineWidth()));
}

public boolean
hasEqualSchematicGapAttributes(Nuc2D nuc)
{
	if (nuc == null)
		return (false);
	return (
	(this.getBPSchemGap() == nuc.getBPSchemGap()) &&
	(this.getFPSchemGap() == nuc.getFPSchemGap()) &&
	(this.getTPSchemGap() == nuc.getTPSchemGap()));
}

public boolean
hasEqualHiddenAttributes(Nuc2D nuc)
{
	if (nuc == null)
		return (false);

	return (this.getIsHidden() == nuc.getIsHidden());
}

public boolean
hasEqualGroupNameAttributes(Nuc2D nuc)
{
	if (nuc == null)
		return (false);

	return (this.getGroupName() == nuc.getGroupName());
}

public boolean
hasEqualNucPathAttributes(Nuc2D nuc)
{
	if (nuc == null)
	{
		return (false);
	}

	return (
		(this.getIsNucPath() == nuc.getIsNucPath()) &&
		(this.getNucPathColor() == nuc.getNucPathColor()) &&
		(this.getNucPathLineWidth() == nuc.getNucPathLineWidth()));
}

public void
setNewLabel(Font font, double lineWidth, double lineLength, Color color,
	int nucNumber, boolean drawLineOnly) // nucNumber not necessarily nucID
throws Exception
{
	BLine2D ray = null;
	Point2D pt = null;
	SSData2D sstr = this.getParentSSData2D();

	this.deleteAllLabels();

	// NEED to get an angle going and project out a long ways to intersect.
	// if pt in bp then get angle of bp and extend. if pt in arc then
	// get circle of arc and project out perp to arc.

	if (this.isBasePair())
	{
		RNABasePair2D basePair = new RNABasePair2D(this);
		if (this.isFivePrimeBasePair())
			ray = basePair.get3PBasePairRay();
		else
			ray = basePair.get5PBasePairRay();
	}
	else
	{
		Nuc2D lastNuc = lastNonNullNuc2D();
		Nuc2D nextNuc = nextNonNullNuc2D();
		Nuc2D firstValidNuc = sstr.getFirstNonNullNuc2D();
		Nuc2D lastValidNuc = sstr.getEndNonNullNuc2D();
		double angle = 0.0;

		if (this == firstValidNuc)
		{
			BLine2D tmpRay = new BLine2D(
				nextNuc.getX(), nextNuc.getY(), this.getX(), this.getY());
			angle = tmpRay.angleInXYPlane();
		}
		else if (this == lastValidNuc)
		{
			BLine2D tmpRay = new BLine2D(
				lastNuc.getX(), lastNuc.getY(), this.getX(), this.getY());
			angle = tmpRay.angleInXYPlane();
		}
		else
		{
			BLine2D nextRay =
				new BLine2D(this.getX(), this.getY(), nextNuc.getX(), nextNuc.getY());
			BLine2D lastRay =
				new BLine2D(this.getX(), this.getY(), lastNuc.getX(), lastNuc.getY());
			nextRay.normalize();
			lastRay.normalize();

			boolean clockWiseFormat = (new RNASingleStrand2D(this)).getIsClockWiseFormatted();

			BLine2D baseRay = new BLine2D(lastRay.getP2(), nextRay.getP2());
			Point2D tailPerpPt = new Point2D.Double();
			Point2D headPerpPt = new Point2D.Double();
			baseRay.getPerpendicularPointAtT(0.5, tailPerpPt,
				headPerpPt, 100.0, !clockWiseFormat);
			BLine2D perpRay = new BLine2D(tailPerpPt, headPerpPt);
			angle = perpRay.angleInXYPlane();
		}

		ray = new BLine2D(0.0, 100.0);
		ray.setFromAngle(angle);
		ray.shiftToOrigin();
		ray.translate(-this.getX(), -this.getY());
	}
	
	// this recenters at midpt rather than extending in one direction
	ray.setFromLength(2000.0); // don't know why 1000.0 doesn't work

	RayIntersectNucNode nucIntersectObj =
		new RayIntersectNucNode(ray, this);
	nucIntersectObj.runRayDrawObjectIntersect();
	if (!nucIntersectObj.getRayIntersects())
		throw new Exception("Error in Nuc2D.setNewLabel(), 0: No intersect found for nuc: " + this);
	
	if (nucIntersectObj.getIntersectPtList().size() != 2)
	{
		/*
		ray.getP1().setLocation(ray.getP1().getX() - this.getX(), -ray.getP1().getY() + this.getY());
		ray.getP2().setLocation(ray.getP2().getX() - this.getX(), -ray.getP2().getY() + this.getY());
		*/
		/*
		ray.setLine(
			ray.getP1().getX() - this.getX(), ray.getP1().getY() - this.getY(),
			ray.getP2().getX() - this.getX(), ray.getP2().getY() - this.getY());
		this.addLabel(new DrawLineObject(ray, 0.1, Color.red));
		return;
		*/

		throw new Exception("Error in Nuc2D.setNewLabel(), 1: No intersect found for nuc: " + this);
		/* for debug purposes:
		this.addLabel(new DrawLineObject(ray, 1.0, Color.red));
		return;
		*/
	}

	// get ray starting at out intersect pt of nuc
	pt = (Point2D)nucIntersectObj.getIntersectPtList().elementAt(1);
	pt.setLocation(pt.getX() - this.getX(), pt.getY() - this.getY());
	ray.setP1(pt);

	// extend ray way past
	pt = ray.getP2();
	pt.setLocation(pt.getX() - this.getX(), pt.getY() - this.getY());
	ray.setP2(pt);

	// push ray tail up by getNucLabelLineInnerDistance amount
	pt = ray.ptAtDistance(sstr.getNucLabelLineInnerDistance());
	ray.setP1(pt);

	// now set length of ray
	pt = ray.ptAtDistance(lineLength);
	ray.setP2(pt);
	
	DrawStringObject nucIDStr = null;
	RayIntersectDrawStringObject strIntersectObj = null;
	double adjDist = 0.0;

	// NEED to try and do this outside of a loop
	while (true)
	{
		Point2D headPt = ray.ptAtDistance(lineLength +
			sstr.getNucLabelLineOuterDistance() + adjDist);

		nucIDStr = new DrawStringObject(
			headPt, font, color, String.valueOf(nucNumber));
		nucIDStr.update();

		strIntersectObj = new RayIntersectDrawStringObject(ray, nucIDStr);
		strIntersectObj.runRayDrawObjectIntersect();
		if (!strIntersectObj.getRayIntersects())
		{
			adjDist += 1.0;
			continue;
		}
		break;
	}
	pt = (Point2D)strIntersectObj.getIntersectPtList().elementAt(0);
	double pushDist = (new BLine2D(pt, nucIDStr.getPoint2D())).length();

	pt = ray.ptAtDistance(ray.length() + sstr.getNucLabelLineOuterDistance() +
		pushDist);
	nucIDStr = new DrawStringObject(
		pt, font, color, String.valueOf(nucNumber));
	nucIDStr.update();

	this.addLabel(new DrawLineObject(ray, lineWidth, Color.black));
	if (!drawLineOnly)
		this.addLabel(nucIDStr);
}

public void
setNewLabel(Font font, double lineWidth, double lineLength, Color color,
	int nucNumber) // nucNumber not necessarily nucID
throws Exception
{
	this.setNewLabel(font, lineWidth, lineLength, color, nucNumber, false);
}

public void
setNewLabel(Font font, double lineWidth, double lineLength, Color color)
throws Exception
{
	this.setNewLabel(font, lineWidth, lineLength, color, this.getID());
}

public void
setNewLabel(Font font, double nucLineLength)
throws Exception
{
	// initially should look threw this sstr and look at any
	// other nuc fonts.  thend should default to base font of
	// nucID on font of nuc
	this.setNewLabel(font,
		this.getParentSSData2D().getNucLabelLineWidth(),
		nucLineLength, Color.black, this.getID());
}

public void
setNewLabel(Font font, double nucLineLength, int nucNumber, boolean drawLineOnly)
throws Exception
{
	// initially should look threw this sstr and look at any
	// other nuc fonts.  thend should default to base font of
	// nucID on font of nuc
	this.setNewLabel(font,
		this.getParentSSData2D().getNucLabelLineWidth(),
		nucLineLength, Color.black, nucNumber, drawLineOnly);
}

public void
setNewLabel(Font font, double nucLineLength, int nucNumber)
throws Exception
{
	// initially should look threw this sstr and look at any
	// other nuc fonts.  thend should default to base font of
	// nucID on font of nuc
	this.setNewLabel(font,
		this.getParentSSData2D().getNucLabelLineWidth(),
		nucLineLength, Color.black, nucNumber);
}

public void
setNewLabel(Font font)
throws Exception
{
	// initially should look threw this sstr and look at any
	// other nuc fonts.  thend should default to base font of
	// nucID on font of nuc
	double lineLength = this.getNucLabelLineLength();
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	this.setNewLabel(font,
		this.getParentSSData2D().getNucLabelLineWidth(), lineLength,
		Color.black, this.getID());
}

public void
setNewLabel()
throws Exception
{
	// initially should look threw this sstr and look at any
	// other nuc fonts.  thend should default to base font of
	// nucID on font of nuc
	double lineLength = this.getNucLabelLineLength();
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	this.setNewLabel(
		new Font("Helvetica", Font.PLAIN, 12),
		this.getParentSSData2D().getNucLabelLineWidth(),
		lineLength, Color.black, this.getID());
}

// currently a valid nuc label is 1 or 2 labels. if 1 label then
// it has to be a line label. if 2 labels then 1 is a line label
// and the other is a string label that represents an integer.
public boolean
hasNucLabel()
{
	Vector labelList = this.getLabelList();
	if (labelList == null)
		return (false);
	if ((labelList.size() <= 0) || (labelList.size() > 2))
		return (false);
	DrawObject drwObj0 = null;
	DrawObject drwObj1 = null;
	drwObj0 = (DrawObject)labelList.elementAt(0);
	if (labelList.size() == 2)
		drwObj1 = (DrawObject)labelList.elementAt(1);
	if ((drwObj1 == null) && (drwObj0 instanceof DrawLineObject))
		return (true);
	if (drwObj1 == null)
		return (false);
	if ((drwObj0 instanceof DrawLineObject) && (drwObj1 instanceof DrawStringObject))
	{
		try
		{
			int val = Integer.parseInt(((DrawStringObject)drwObj1).getDrawString());
		}
		catch (NumberFormatException e)
		{
			return (false);
		}
		return (true);
	}
	if ((drwObj1 instanceof DrawLineObject) && (drwObj0 instanceof DrawStringObject))
	{
		try
		{
			int val = Integer.parseInt(((DrawStringObject)drwObj0).getDrawString());
		}
		catch (NumberFormatException e)
		{
			return (false);
		}
		return (true);
	}
	return (false);
}

public DrawLineObject
getLineLabel()
{
	if (this.getLabelList() == null)
		return (null);
	for (Enumeration e = this.getLabelList().elements();e.hasMoreElements();)
	{
		DrawObject drawObject = (DrawObject)e.nextElement();
		if (drawObject instanceof DrawLineObject)
			return ((DrawLineObject)drawObject);
	}
	return (null);
}

// return the number part of a nuclabel if there is one
public DrawStringObject
getNumberLabel()
{
	Vector labelList = this.getLabelList();
	if (labelList == null)
		return (null);
	if ((labelList.size() <= 0) || (labelList.size() > 2))
		return (null);
	DrawObject drwObj0 = null;
	DrawObject drwObj1 = null;
	drwObj0 = (DrawObject)labelList.elementAt(0);
	if (labelList.size() == 2)
		drwObj1 = (DrawObject)labelList.elementAt(1);
	if ((drwObj1 == null) && (drwObj0 instanceof DrawLineObject))
		return (null);
	if (drwObj1 == null)
		return (null);
	if ((drwObj0 instanceof DrawLineObject) && (drwObj1 instanceof DrawStringObject))
	{
		try
		{
			int val = Integer.parseInt(((DrawStringObject)drwObj1).getDrawString());
		}
		catch (NumberFormatException e)
		{
			return (null);
		}
		return ((DrawStringObject)drwObj1);
	}
	if ((drwObj1 instanceof DrawLineObject) && (drwObj0 instanceof DrawStringObject))
	{
		try
		{
			int val = Integer.parseInt(((DrawStringObject)drwObj0).getDrawString());
		}
		catch (NumberFormatException e)
		{
			return (null);
		}
		return ((DrawStringObject)drwObj0);
	}
	return (null);
}

public double
getNucLabelLineLength()
throws Exception
{
	return (getParentSSData2D().getNucLabelLineLength(this.getID()));
}

public void
resetNucLabelsLineLengths(double length)
throws Exception
{
	DrawLineObject line = this.getLineLabel();
	if (line == null)
		return;
	line.setFromLength(length);
}

public String
toString()
{
	try
	{
		String bpName = new String(" ");
		if (getBasePairSStrName() != null)
		{
			bpName = getBasePairSStrName();
			return (this.getID() + " " + this.getNucChar() + " " +
				StringUtil.roundStrVal(this.getX(), 2) + " " +
				StringUtil.roundStrVal(this.getY(), 2) + " " +
				this.getBasePairID() + " " + bpName);
		}

		return (this.getID() + " " + this.getNucChar() + " " +
			StringUtil.roundStrVal(this.getX(), 2) + " " +
			StringUtil.roundStrVal(this.getY(), 2) + " " +
			this.getBasePairID());
	}
	catch (ComplexException ce)
	{
		if (ce.getErrorCode() == ComplexDefines.RNA_SINGLE_NUC_ERROR + ComplexDefines.FORMAT_ERROR)
			return (ce.getErrorMsg() + " " + ce.getComment());
		else
			return ("Unknown Nuc2D Error");
	}
	catch (Exception e)
	{
		return (e.toString());
	}
}

public void
printComplexXML(PrintWriter outFile)
throws Exception
{
	PrintWriter out = (PrintWriter)outFile;

	String bpName = new String(" ");
	if (getBasePairSStrName() != null)
		bpName = getBasePairSStrName();

	// NEED to add any drawObjects being referred to as nuc symbols

	out.println(this.getID() + " " + this.getNucChar() + " " +
		StringUtil.roundStrVal(this.getX(), 2) + " " + StringUtil.roundStrVal(this.getY(), 2));
}

public void
printComplexCSV(PrintWriter outFile)
throws Exception
{
	PrintWriter out = (PrintWriter)outFile;

	String bpName = new String(" ");
	if (getBasePairSStrName() != null)
		bpName = getBasePairSStrName();

	// NEED to add any drawObjects being referred to as nuc symbols

	out.println(this.getID() + "," + this.getNucChar() + "," +
		StringUtil.roundStrVal(this.getX(), 2) + "," + StringUtil.roundStrVal(this.getY(), 2));
}

public void
printNucSymbol(PrintWriter out)
throws Exception
{
	if (!getUseSymbol())
		return;
	out.println("<NucSymbol>");

	if (this.getDrawAdobeObject() != null)
		this.printDrawObjectLabel(this.getDrawAdobeObject(), out);
	else if (this.getDrawCircleObject() != null)
		this.printDrawObjectLabel(this.getDrawCircleObject(), out);
	else if (this.getDrawTriangleObject() != null)
		this.printDrawObjectLabel(this.getDrawTriangleObject(), out);
	else if (this.getDrawParallelogramObject() != null)
		this.printDrawObjectLabel(this.getDrawParallelogramObject(), out);
	else if (this.getDrawCharSymbolObject() != null)
		this.printDrawObjectLabel(this.getDrawCharSymbolObject(), out);

	out.println("</NucSymbol>");
}

public DrawObject
getNucDrawObject()
{
	if (getUseSymbol())
	{
		// should be only one set, return first if not
		if (getDrawAdobeObject() != null)
			return (this.getDrawAdobeObject());
		if (this.getDrawCircleObject() != null)
			return (this.getDrawCircleObject());
		if (this.getDrawCircleObject() != null)
			return (this.getDrawCircleObject());
		if (this.getDrawTriangleObject() != null)
			return (this.getDrawTriangleObject());
		if (this.getDrawParallelogramObject() != null)
			return (this.getDrawParallelogramObject());
		if (this.getDrawCharSymbolObject() != null)
			return (this.getDrawCharSymbolObject());
	}
	if (this.getDrawCharObject() != null)
		return (this.getDrawCharObject());
	return (null);
}

public boolean
isSymbol()
{
	return (!(this.getNucDrawObject() instanceof DrawCharObject));
}

public Nuc2D
getNuc2DBasePair()
{
	return ((Nuc2D)this.getBasePair());
}

public Nuc2D
nextNuc2D()
{
	return ((Nuc2D)this.nextNuc());
}

public Nuc2D
lastNuc2D()
{
	return ((Nuc2D)this.lastNuc());
}

public Nuc2D
nextNonNullNuc2D()
throws Exception
{
	return ((Nuc2D)this.nextNonNullNuc());
}

public Nuc2D
lastNonNullNuc2D()
throws Exception
{
	return ((Nuc2D)this.lastNonNullNuc());
}

public Nuc2D
nextVisibleNuc2D()
throws Exception
{
	Nuc2D refNuc = this.nextNonNullNuc2D();
	while (true)
	{
		if (!refNuc.isHidden())
			return (refNuc);
		refNuc = refNuc.nextNonNullNuc2D();
	}
}

public Nuc2D
lastVisibleNuc2D()
throws Exception
{
	Nuc2D refNuc = this.lastNonNullNuc2D();
	while (true)
	{
		if (!refNuc.isHidden())
			return (refNuc);
		refNuc = refNuc.lastNonNullNuc2D();
	}
}

public SSData2D
getParentSSData2D()
{
	return ((SSData2D)this.getParentCollection());
}

/***************** ComplexCollection Implementation **************/

public void
reassociateLabel(DrawObject label, ComplexCollection fromCollection)
throws Exception
{
	fromCollection.getLabelList().remove(label);

	label.shiftXY(
		-((DrawObject)fromCollection).getX() + this.getX(),
		-((DrawObject)fromCollection).getY() + this.getY());
	
	this.addLabel(label);
}

public DrawObject
findLeafNode(double x, double y, Vector includeTypes, Vector excludeTypes)
throws Exception
{
	// try looking in nuc's labels
	if (this.getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			if (drawObject.contains(x - this.getX(), y + this.getY()))
				return (drawObject);
		}
	}

	// check if nucs are excluded; if so return null
	if (excludeTypes != null)
	{
		for (Enumeration e = excludeTypes.elements();e.hasMoreElements();)
		{
			String type = (String)e.nextElement();
			if (type.equals("Nuc2D"))
				return (null);
		}
	}

	if (this.contains(x - this.getX(), y + this.getY()))
		return (this.getNucDrawObject());

	return (null);
}

public Nuc2D
findNuc(double xPos, double yPos)
throws Exception
{
	if (this.contains(xPos - this.getX(), yPos + this.getY()))
		return (this);

	return (null);
}

private Hashtable saveBasePairHashtable = null;

public void
setSaveBasePairHashtable(Hashtable saveBasePairHashtable)
{
    this.saveBasePairHashtable = saveBasePairHashtable;
}

public Hashtable
getSaveBasePairHashtable()
{
    return (this.saveBasePairHashtable);
}

public void
runSetBasePairHashtable(int level)
throws Exception
{
	if (this.isSingleStranded())
		return;
	if (this.getSaveBasePairHashtable() == null)
		this.setSaveBasePairHashtable(new Hashtable());
	this.getSaveBasePairHashtable().put(new Integer(level),
		new BasePairInfo(this));
}

public boolean
resetBasePairFromHashtable(int level)
throws Exception
{
	if (this.getSaveBasePairHashtable() == null)
		return (false);
	BasePairInfo basePairInfo = (BasePairInfo)this.getSaveBasePairHashtable().get(new Integer(level));
	if (basePairInfo == null)
		return (false);

	// Now reset basepair from info
	this.setBasePairSStrName(basePairInfo.getBasePairStrandName());
	this.setBasePairID(basePairInfo.getBasePairID());
	this.resetBasePair();

	return (true);
}

public void
clearBasePairFromHashtable(int level)
throws Exception
{
	if (this.getSaveBasePairHashtable() == null)
		return;
	this.getSaveBasePairHashtable().remove(new Integer(level));
	if (this.getSaveBasePairHashtable().size() <= 0)
		this.setSaveBasePairHashtable(null);
}

class
BasePairInfo
{

public BasePairInfo(Nuc2D refNuc)
{
	this.setBasePairID(refNuc.getBasePairID());
	this.setBasePairStrandName(refNuc.getBasePairSStrName());
}

private int basePairID = 0;

public void
setBasePairID(int basePairID)
{
    this.basePairID = basePairID;
}

public int
getBasePairID()
{
    return (this.basePairID);
}

private String basePairStrandName = null;

public void
setBasePairStrandName(String basePairStrandName)
{
    this.basePairStrandName = basePairStrandName;
}

public String
getBasePairStrandName()
{
    return (this.basePairStrandName);
}

}

/***************** DrawObject Implementation ****************/

private double x = 0.0;

public void
setX(double x)
throws Exception
{
	this.x = x;
	this.setIsFormatted(true);
	if (this.getDrawCharObject() != null)
		this.getDrawCharObject().setX(0.0);
	if (this.getDrawAdobeObject() != null)
		this.getDrawAdobeObject().setX(0.0);
	if (this.getDrawCircleObject() != null)
		this.getDrawCircleObject().setX(0.0);
	if (this.getDrawTriangleObject() != null)
		this.getDrawTriangleObject().setX(0.0);
	if (this.getDrawParallelogramObject() != null)
		this.getDrawParallelogramObject().setX(0.0);
}

public double
getX()
throws Exception
{
	if (!this.isFormatted())
		throw new ComplexException("Error in Nuc2D.getX()",
			ComplexDefines.RNA_SINGLE_NUC_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_ERROR_MSG, "Nuc: " + this.getID());

	return (this.x);
}

private double y = 0.0;

public void
setY(double y)
throws Exception
{
	this.y = y;
	this.setIsFormatted(true);
	if (this.getDrawCharObject() != null)
		this.getDrawCharObject().setY(0.0);
	if (this.getDrawAdobeObject() != null)
		this.getDrawAdobeObject().setY(0.0);
	if (this.getDrawCircleObject() != null)
		this.getDrawCircleObject().setY(0.0);
	if (this.getDrawTriangleObject() != null)
		this.getDrawTriangleObject().setY(0.0);
	if (this.getDrawParallelogramObject() != null)
		this.getDrawParallelogramObject().setY(0.0);
}

public void
setXY(double x, double y)
throws Exception
{
	this.setX(x);
	this.setY(y);
}

// for setting formatted
public void
setXY()
throws Exception
{
	this.setXY(0.0, 0.0);
}

public double
getY()
throws Exception
{
	if (!this.isFormatted())
		throw new ComplexException("Error in Nuc2D.getY()",
			ComplexDefines.RNA_SINGLE_NUC_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_ERROR_MSG, "Nuc: " + this.getID());

	return (this.y);
}

public double
getDeltaX()
{
	return (this.getNucDrawObject().getDeltaX());
}

public double
getDeltaY()
{
	return (this.getNucDrawObject().getDeltaY());
}

private Color color = Color.black;

public void
setColor(Color color)
throws Exception
{
    this.color = color;
	if (this.getDrawCharObject() != null)
		this.getDrawCharObject().setColor(color);
	if (this.getDrawAdobeObject() != null)
		this.getDrawAdobeObject().setColor(color);
	if (this.getDrawCircleObject() != null)
		this.getDrawCircleObject().setColor(color);
	if (this.getDrawTriangleObject() != null)
		this.getDrawTriangleObject().setColor(color);
	if (this.getDrawParallelogramObject() != null)
		this.getDrawParallelogramObject().setColor(color);
}

public Color
getColor()
{
    return (this.color);
}

private boolean isNucPath = false;

public void
setIsNucPath(boolean isNucPath)
{
    this.isNucPath = isNucPath;
}

public boolean
getIsNucPath()
{
    return (this.isNucPath);
}

public boolean
isNucPath()
{
    return (this.getIsNucPath());
}

private Color nucPathColor = Color.red;

public void
setNucPathColor(Color nucPathColor)
{
	this.nucPathColor = nucPathColor;
}

public Color
getNucPathColor()
{
	return (this.nucPathColor);
}

// private double nucPathLineWidth = (ComplexDefines.DEFAULT_NUC_FONT_SIZE + 6);
private double nucPathLineWidth = 0.0;

public void
setNucPathLineWidth(double nucPathLineWidth)
{
    this.nucPathLineWidth = nucPathLineWidth;
}

public double
getNucPathLineWidth()
{
    return (this.nucPathLineWidth);
}

public void
setEditColor(Color color)
throws Exception
{
	super.setEditColor(color);
	if (this.getDrawCharObject() != null)
		this.getDrawCharObject().setEditColor(color);
	/*
	if (this.getDrawAdobeObject() != null)
		this.getDrawAdobeObject().setColor(color);
	if (this.getDrawCircleObject() != null)
		this.getDrawCircleObject().setColor(color);
	if (this.getDrawTriangleObject() != null)
		this.getDrawTriangleObject().setColor(color);
	if (this.getDrawParallelogramObject() != null)
		this.getDrawParallelogramObject().setColor(color);
	*/
}

public boolean
getIsEditable()
throws Exception
{
    return ((this.getEditColor() != null) || (this.getDrawCharObject().getEditColor() != null));
}

// current nuc symbol translated to nuc position
public BRectangle2D
getNucSymbolBoundingBox()
throws Exception
{
	BRectangle2D rect = (BRectangle2D)
		this.getNucDrawObject().getBoundingBox().clone();
	if (rect == null)
		return (null);
	
	rect.setRect(rect.getX() + this.getX(), rect.getY() - this.getY(),
		rect.getWidth(), rect.getHeight());
	return (rect);
}

private boolean showBoundingBox = false;

public void
setShowBoundingBox(boolean showBoundingBox)
{
	this.showBoundingBox = showBoundingBox;
	if (this.getDrawCharObject() != null)
		this.getDrawCharObject().setShowBoundingBox(showBoundingBox);
	if (this.getDrawAdobeObject() != null)
		this.getDrawAdobeObject().setShowBoundingBox(showBoundingBox);
	if (this.getDrawCircleObject() != null)
		this.getDrawCircleObject().setShowBoundingBox(showBoundingBox);
	if (this.getDrawTriangleObject() != null)
		this.getDrawTriangleObject().setShowBoundingBox(showBoundingBox);
	if (this.getDrawParallelogramObject() != null)
		this.getDrawParallelogramObject().setShowBoundingBox(showBoundingBox);
}

public boolean
getShowBoundingBox()
{
	// NEED to deal with this. not sure if need draw(X)Objects box
	return (this.showBoundingBox);
}

private boolean showBoundingShape = false;

public void
setShowBoundingShape(boolean showBoundingShape)
{
	this.showBoundingShape = showBoundingShape;
	if (this.getDrawCharObject() != null)
		this.getDrawCharObject().setShowBoundingShape(showBoundingShape);
	if (this.getDrawAdobeObject() != null)
		this.getDrawAdobeObject().setShowBoundingShape(showBoundingShape);
	if (this.getDrawCircleObject() != null)
		this.getDrawCircleObject().setShowBoundingShape(showBoundingShape);
	if (this.getDrawTriangleObject() != null)
		this.getDrawTriangleObject().setShowBoundingShape(showBoundingShape);
	if (this.getDrawParallelogramObject() != null)
		this.getDrawParallelogramObject().setShowBoundingShape(showBoundingShape);
}

public boolean
getShowBoundingShape()
{
	// NEED to deal with this.
	return (this.showBoundingShape);
}

public boolean
contains(double xPos, double yPos)
throws Exception
{
	if (!this.getIsPickable())
		return (false);
	if (this.getNucDrawObject() == null)
		return (false);
	if (!this.isFormatted())
		return (false);

	return (this.getNucDrawObject().contains(xPos, yPos));
}

public void
update(Graphics2D g2)
throws Exception
{
	if (!this.isFormatted())
		return;

	// doing this here so that any sub drawObject's color can be set
	if (this.getColor() == null)
		this.setColor(Color.black);
	else
		this.setColor(this.getColor());

	boolean drawSymbolAdded = false;
	BRectangle2D rect = new BRectangle2D();

	if (this.getDrawAdobeObject() != null)
	{
		this.getDrawAdobeObject().update(g2);
		rect.setRect(this.getDrawAdobeObject().getBoundingBox());
		drawSymbolAdded = true;
	}
	else if (this.getDrawCircleObject() != null)
	{
		this.getDrawCircleObject().update(g2);
		rect.setRect(this.getDrawCircleObject().getBoundingBox());
		drawSymbolAdded = true;
	}
	else if (this.getDrawTriangleObject() != null)
	{
		this.getDrawTriangleObject().update(g2);
		rect.setRect(this.getDrawTriangleObject().getBoundingBox());
		drawSymbolAdded = true;
	}
	else if (this.getDrawParallelogramObject() != null)
	{
		this.getDrawParallelogramObject().update(g2);
		rect.setRect(this.getDrawParallelogramObject().getBoundingBox());
		drawSymbolAdded = true;
	}
	else if (this.getDrawCharSymbolObject() != null)
	{
		this.getDrawCharSymbolObject().update(g2);
		rect.setRect(this.getDrawCharSymbolObject().getBoundingBox());
		drawSymbolAdded = true;
	}

	if (this.getDrawCharObject() != null)
	{
		this.getDrawCharObject().update(g2);
		if (!drawSymbolAdded)
			rect.setRect(this.getDrawCharObject().getBoundingBox());
	}

	if (this.getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			drawObject.update(g2);	
			rect.add(drawObject.getBoundingBox());
		}
	}

	BRectangle2D newRect = new BRectangle2D(
		rect.getX() + this.getX(),
		rect.getY() - this.getY(),
		rect.getWidth(),
		rect.getHeight()
		);
	this.setBoundingBox(newRect);

	this.setBoundingShape(this.getBoundingBox());
}

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (!this.isFormatted())
		return;
	if (this.getIsHidden())
		return;	
	if (this.getHideForConstrain())
		return;	

	if (constrainedArea != null)
	{
		if (!this.intersects(constrainedArea, g2))
			return;
	}

	g2.translate(this.getX(), -this.getY());

	this.setG2Transform(g2.getTransform());

	if (this.getNucDrawObject() != null)
	{
		if ((!this.getIsSchematic()) ||
			(this.isSymbol() && this.getUseSymbol()))
		{
			this.getNucDrawObject().draw(g2, constrainedArea);
		}
	}

	if (this.getLabelList() != null)
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
			((DrawObject)e.nextElement()).draw(g2, constrainedArea);	
	g2.translate(-this.getX(), this.getY());	

	/* not showing since drawObject underneath will draw BB. Use if want to see
	** if a nucs label is bounded properly
	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.green);
		this.drawBoundingShape(g2);
	}
	*/
}

public void
psLookAhead(DrawObject drwObj, Graphics2D g2, PostScriptUtil psUtil)
{
	if (drwObj instanceof DrawFontObject)
	{
		g2.setFont(((DrawFontObject)drwObj).getFont());
		psUtil.setPrintCurrentFont(g2);
	}
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	if (!this.isFormatted())
		return;
	if (this.getIsHidden())
		return;	

	// Do a look ahead at ps attributes and print before gsave so
	// as to make the change global over a rna strand
	this.psLookAhead(this.getNucDrawObject(), g2, psUtil);

	if (this.getNucDrawObject() != null)
	{
		if ((!this.getIsSchematic()) ||
			(this.isSymbol() && this.getUseSymbol()))
		{
			((DrawFontObject)(this.getNucDrawObject())).printPS(g2, psUtil);
			psUtil.append(" " + this.getX() + " " + this.getY() + " print_nuc_char\n");	
		}
	}

	if (this.getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drwObj = (DrawObject)e.nextElement();
			if (drwObj instanceof DrawFontObject)
			{
				g2.setFont(((DrawFontObject)drwObj).getFont());
				psUtil.setPrintCurrentFont(g2);
				drwObj.printPS(g2, psUtil);
				psUtil.append(" " + drwObj.getX() + " " + drwObj.getY() + " " +
					this.getX() + " " + this.getY() + " print_nuc_strlabel\n");	
			}
			else if (drwObj instanceof DrawLineObject)
			{
				/*
				psUtil.printPSGSave();
				psUtil.printPSTranslate(this.getX(), this.getY());
				psUtil.printPSGSave();
				drwObj.printPS(g2, psUtil);
				psUtil.printPSGRestore();
				psUtil.printPSGRestore();
				*/

				// NEED to change fundamentally the way lineObj.printPS() prints
				/*
				gsave
				346.76 -250.51 translate
				gsave
				0.2 setlinewidth
				0.0 0.0 0.0 setrgbcolor
				0.0 7.5 0.0 15.5 lwline
				grestore
				grestore
				*/
				drwObj.printPS(g2, psUtil);
				psUtil.append(" " + this.getX() + " " + this.getY() + " print_nuc_linelabel\n");	
			}
		}
	}

	// psUtil.printPSGRestore();
	g2.translate(-this.getX(), this.getY());	
}

public void
negateBasePair()
{
	if (!this.isBasePair())
		return;
	this.getBasePair().setBasePairID(0);
	this.getBasePair().setBasePairSStrName(null);
	this.getBasePair().setBasePair(null);
	this.setBasePairID(0);
	this.setBasePairSStrName(null);
	this.setBasePair(null);
}

public void
erase(Graphics2D g2)
throws Exception
{
	g2.setTransform(this.getG2Transform());
	this.getNucDrawObject().erase(g2);
	if (this.getLabelList() == null)
		return;
	for (Enumeration e = this.getLabelList().elements();e.hasMoreElements();)
		((DrawObject)e.nextElement()).erase(g2);
}

public void
delete(Graphics2D g2)
throws Exception
{
	this.erase(g2);
	this.negateBasePair();
	this.getParentSSData2D().setItemAt(null, this.getID());
}

/***************** End DrawObject Implementation ****************/

public BRectangle2D
getCenteredBoundingBox()
throws Exception
{
	BRectangle2D rect = (BRectangle2D)
		this.getNucDrawObject().getBoundingBox().clone();
	rect.setRect(
		this.getX() - (rect.getWidth()/2.0),
		this.getY() + (rect.getHeight()/2.0),
		rect.getWidth(), rect.getHeight());
	return (rect);
}

private BVector2d bVector2d = null;

public BVector2d
getBVector2d()
throws Exception
{
	if (bVector2d == null)
		bVector2d = new BVector2d();

	bVector2d.set(this.getX(), this.getY());
	return (bVector2d);
}

// NEED to not make this a new BLine2D every time.
// ray with this nuc as tailpt and lastnuc as headpt
public BLine2D
getFivePrimeRay()
throws Exception
{
	if (this.lastNuc2D() == null)
		return (null);
	return (new BLine2D(
		this.getPoint2D(),
		this.lastNuc2D().getPoint2D()));
}

// ray with this nuc as tailpt and nextnuc as headpt
public BLine2D
getThreePrimeRay()
throws Exception
{
	if (this.nextNuc2D() == null)
		return (null);
	return (new BLine2D(
		this.getPoint2D(),
		this.nextNuc2D().getPoint2D()));
}

// intersect point of fivePrimeRay() and lastnuc's bounding box. gap is
// at end of ray and creates more space between line and lastNuc.
public Point2D
getFivePrimeHeadIntersect(double gap)
throws Exception
{
	double[] uArray = new double[2];
	int[] sideArray = new int[2];
	Point2D intersect0Pt = new Point2D.Double();
	Point2D intersect1Pt = new Point2D.Double();

	Nuc2D lastNuc = this.lastNuc2D();

	if (lastNuc == null)
		return (null);

	Rectangle2D rect = lastNuc.getCenteredBoundingBox();

	boolean intersects = BLine2D.getRectangleRayIntersect(
		rect, this.getFivePrimeRay(),
		intersect0Pt, intersect1Pt, uArray, sideArray);

	if (!intersects)
		return (null);

	if (gap != 0.0)
	{
		BLine2D tmpLine = new BLine2D(this.getFivePrimeRay().getP1(),
			intersect0Pt);
		tmpLine.setRayFromLength(tmpLine.length() - gap);
		intersect0Pt = tmpLine.getP2();
	}

	return (intersect0Pt);
}


// intersect pt with fivePrimeRay() and this nuc's bounding box. gap creates
// more space coming up to refNuc. 
public Point2D
getFivePrimeTailIntersect(double gap)
throws Exception
{
	double[] uArray = new double[2];
	int[] sideArray = new int[2];
	Point2D intersect0Pt = new Point2D.Double();
	Point2D intersect1Pt = new Point2D.Double();

	Rectangle2D rect = this.getCenteredBoundingBox();

	boolean intersects = BLine2D.getRectangleRayIntersect(
		rect, this.getFivePrimeRay(),
		intersect0Pt, intersect1Pt, uArray, sideArray);

	if (!intersects)
		return (null);

	if (gap != 0.0)
	{
		BLine2D tmpLine = new BLine2D(this.getFivePrimeRay().getP1(),
			intersect0Pt);
		tmpLine.setRayFromLength(tmpLine.length() + gap);
		intersect0Pt = tmpLine.getP2();
	}

	return (intersect0Pt);
}

// intersect pt with threePrimeRay() and nextnuc's bounding box
public Point2D
getThreePrimeHeadIntersect(double gap)
throws Exception
{
	double[] uArray = new double[2];
	int[] sideArray = new int[2];
	Point2D intersect0Pt = new Point2D.Double();
	Point2D intersect1Pt = new Point2D.Double();

	Nuc2D nextNuc = this.nextNuc2D();

	if (nextNuc == null)
		return (null);

	Rectangle2D rect = nextNuc.getCenteredBoundingBox();

	boolean intersects = BLine2D.getRectangleRayIntersect(
		rect, this.getThreePrimeRay(),
		intersect0Pt, intersect1Pt, uArray, sideArray);

	if (!intersects)
		return (null);

	if (gap != 0.0)
	{
		BLine2D tmpLine = new BLine2D(this.getThreePrimeRay().getP1(),
			intersect0Pt);
		tmpLine.setRayFromLength(tmpLine.length() - gap);
		intersect0Pt = tmpLine.getP2();
	}

	return (intersect0Pt);
}

// intersect pt with threePrimeRay() and this nuc's bounding box
public Point2D
getThreePrimeTailIntersect(double gap)
throws Exception
{
	double[] uArray = new double[2];
	int[] sideArray = new int[2];
	Point2D intersect0Pt = new Point2D.Double();
	Point2D intersect1Pt = new Point2D.Double();

	Rectangle2D rect = this.getCenteredBoundingBox();

	boolean intersects = BLine2D.getRectangleRayIntersect(
		rect, this.getThreePrimeRay(),
		intersect0Pt, intersect1Pt, uArray, sideArray);

	if (!intersects)
		return (null);

	if (gap != 0.0)
	{
		BLine2D tmpLine = new BLine2D(this.getThreePrimeRay().getP1(),
			intersect0Pt);
		tmpLine.setRayFromLength(tmpLine.length() + gap);
		intersect0Pt = tmpLine.getP2();
	}

	return (intersect0Pt);
}

public static Nuc2D
getFromDrawObject(DrawObject drawObject)
{
	if (drawObject == null)
		return (null);
	DrawObjectCollection parent =
		(DrawObjectCollection)drawObject.getParentCollection();

	if ((parent == null) || !(parent instanceof Nuc2D) ||
		(((Nuc2D)parent).getNucDrawObject() != drawObject))
		return (null);
	
	return ((Nuc2D)parent);
}

// this is more general and will catch nucs line label
public static Nuc2D
getFromLabel(DrawObject drawObject)
{
	if (drawObject == null)
		return (null);
	DrawObjectCollection parent =
		(DrawObjectCollection)drawObject.getParentCollection();

	if ((parent == null) || !(parent instanceof Nuc2D))
		return (null);
	
	return ((Nuc2D)parent);
}

public void
clearAnnotation()
throws Exception
{
	this.setColor(Color.black);
	this.setUseSymbol(false);
}

public void
showProperties(Component parent)
{
}

private static void
debug(String s)
{
	System.err.println("Nuc2D-> " + s);
}

}
