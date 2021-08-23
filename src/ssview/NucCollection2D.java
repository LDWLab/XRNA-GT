package ssview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;

import jimage.DrawLineObject;
import jimage.DrawObject;
import jimage.DrawStringObject;
import util.GraphicsUtil;
import util.PostScriptUtil;
import util.StringUtil;
import util.Tuple2;
import util.math.BLine2D;
import util.math.BRectangle2D;
import util.math.BVector2d;
import util.math.MathUtil;

// A 2D Container for nucs
public class
NucCollection2D
extends NucCollection
{
	//Boundaries for letters
	public static final double
		MAX_XCO = 576,
		MIN_XCO = 36,
		MAX_YCO = 756,
		MIN_YCO = 36;

public
NucCollection2D()
throws Exception
{
	super();
}

public
NucCollection2D(String name)
throws Exception
{
	super(name);
}

public Vector
getSchematicNucDelineators()
throws Exception
{
	//
	// if (this.getNucCount() < 1)
		// return (null);
	// 
	// if (getNucCount() == 1)
	// {
		// if (getNuc2DAt(1) == null)
			// return (null);
		// schemDelineators.add(getNuc2DAt(1));
		// schemDelineators.add(getNuc2DAt(1));
		// return(schemDelineators);
	// }
	//

	Vector schemDelineators = new Vector();
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);

		for (Nuc2D nuc = startStrandNuc;(nuc != null) &&
			(nuc.getID() <= endStrandNuc.getID());nuc = nuc.nextNuc2D())
		{
			Color startSchemColor = nuc.getSchematicColor();
			double startSchemLineWidth = nuc.getSchematicLineWidth();
			// find begin of schematic
			if (nuc.isSchematic())
			{
				schemDelineators.add(nuc);
				Nuc2D lastNuc = nuc;
				while (
					(nuc.getID() < endStrandNuc.getID()) &&
					nuc.isSchematic() &&
					(!nuc.isSymbol()) &&
					(nuc.getSchematicColor() == startSchemColor) &&
					(nuc.getSchematicLineWidth() == startSchemLineWidth)
					)
				{
					lastNuc = nuc;
					nuc = nuc.nextNuc2D();
				}
				nuc = lastNuc;
				schemDelineators.add(nuc);
			}
		}
	}

	return (schemDelineators);
}

public Vector
getNucPathDelineators()
throws Exception
{
	Vector nucPathDelineators = new Vector();
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);

		for (Nuc2D nuc = startStrandNuc;(nuc != null) &&
			(nuc.getID() <= endStrandNuc.getID());nuc = nuc.nextNuc2D())
		{
			Color startNucPathColor = nuc.getNucPathColor();
			double startNucPathLineWidth = nuc.getNucPathLineWidth();
			// find begin of nucPath
			if (nuc.isNucPath())
			{
				nucPathDelineators.add(nuc);
				Nuc2D lastNuc = nuc;
				while (
					(nuc.getID() < endStrandNuc.getID()) &&
					nuc.isNucPath() &&
					(nuc.getNucPathColor() == startNucPathColor) &&
					(nuc.getNucPathLineWidth() == startNucPathLineWidth)
					)
				{
					lastNuc = nuc;
					nuc = nuc.nextNuc2D();
				}
				nuc = lastNuc;
				nucPathDelineators.add(nuc);
			}
		}
	}

	return (nucPathDelineators);
}

/*********** GENERAL ROUTINES *****************/

private GeneralPath schematicPath = null;

public GeneralPath
getSchematicPath(Nuc2D nuc0, Nuc2D nuc1)
throws Exception
{
    if (this.schematicPath == null)
		this.schematicPath = new GeneralPath();
	
	Nuc2D refNuc = nuc0;
	while (true)
	{
		if ((refNuc == null) || (refNuc.getID() > nuc1.getID()))
			break;

		if (refNuc.getID() == nuc0.getID()) // then at begin of strand
		{
			this.schematicPath.reset();

			// adjust to what is behind this refNuc
			
			Nuc2D lastNuc = refNuc.lastNuc2D();
			if (lastNuc == null)
			{
				// no nuc behind to adjust to, start at refNuc
				this.schematicPath.moveTo((float)refNuc.getX(),
					-(float)refNuc.getY());
			}
			else
			{
				// found a nuc behind, moveTo part way back to it
				if (lastNuc.isSchematic() && !lastNuc.isSymbol())
				{
					// then found tail of last schem segment, need to only go 1/2
					// way as it joins up with another type of schem line
					Point2D pt = refNuc.getFivePrimeRay().getMidPt();
					this.schematicPath.moveTo((float)pt.getX(), -(float)pt.getY());
				}
				else
				{
					// look back to lastNuc char or lastNuc symbol, get ray up
					// to it minus lastNuc.3pGap
					Point2D pt = refNuc.getFivePrimeHeadIntersect(lastNuc.getTPSchemGap());
					if (pt == null)
					{
						debug("ERROR IN NucCollection2D.getSchematicPath() at 0, INVESTIGATE");
						this.schematicPath.moveTo((float)refNuc.getX(),
							-(float)refNuc.getY());
					}
					else
					{
						this.schematicPath.moveTo((float)pt.getX(),
							-(float)pt.getY());
					}
				}

				// now lineTo up to refNuc
				if (refNuc.isSymbol())
				{
					// lineTo part way up to refNuc
					Point2D pt = refNuc.getFivePrimeTailIntersect(refNuc.getFPSchemGap());
					if (pt == null)
					{
						debug("ERROR IN NucCollection2D.getSchematicPath() at 1, INVESTIGATE");
						this.schematicPath.lineTo((float)refNuc.getX(),
							-(float)refNuc.getY());
					}
					else
					{
						this.schematicPath.lineTo((float)pt.getX(),
							-(float)pt.getY());
					}
				}
				else
				{
					this.schematicPath.lineTo((float)refNuc.getX(),
						-(float)refNuc.getY());
				}
			}
		}

		if (refNuc.getID() == nuc1.getID()) // then at end of strand
		{
			if (refNuc.isSymbol())
			{
				Point2D pt = refNuc.getThreePrimeTailIntersect(refNuc.getTPSchemGap());
				if (pt == null)
				{
					debug("ERROR IN NucCollection2D.getSchematicPath() at 2, INVESTIGATE");
					this.schematicPath.moveTo((float)refNuc.getX(),
						-(float)refNuc.getY());
				}
				else
				{
					this.schematicPath.moveTo((float)pt.getX(),
						-(float)pt.getY());
				}
			}
			else
			{
				this.schematicPath.lineTo((float)refNuc.getX(),
					-(float)refNuc.getY());
			}

			Nuc2D nextNuc = refNuc.nextNuc2D();
			if (nextNuc != null)
			{
				if (nextNuc.isSchematic() && !nextNuc.isSymbol())
				// then go halfway
				{
					Point2D pt = refNuc.getThreePrimeRay().getMidPt();
					this.schematicPath.lineTo((float)pt.getX(), -(float)pt.getY());
				}
				else // refNuc is symbol or nucchar
				{
					// NEED to provide a t val towards nextNuc
					Point2D pt = refNuc.getThreePrimeHeadIntersect(nextNuc.getFPSchemGap());
					if (pt == null)
					{
						debug("ERROR IN NucCollection2D.getSchematicPath() at 3, INVESTIGATE");
					}
					else
					{
						this.schematicPath.lineTo((float)pt.getX(),
							-(float)pt.getY());
					}
				}
			}
		}

		if ((refNuc.getID() != nuc0.getID()) &&
			(refNuc.getID() != nuc1.getID())) // then in strand somewhere
		{
			if (refNuc.isSymbol())
			{
				Point2D pt = refNuc.getFivePrimeTailIntersect(refNuc.getFPSchemGap());
				if (pt == null)
				{
					debug("ERROR IN NucCollection2D.getSchematicPath() at 3, INVESTIGATE");
					this.schematicPath.lineTo((float)refNuc.getX(),
						-(float)refNuc.getY());
				}
				else
				{
					this.schematicPath.lineTo((float)pt.getX(),
						-(float)pt.getY());
				}
				pt = refNuc.getThreePrimeTailIntersect(refNuc.getTPSchemGap());
				if (pt == null)
				{
					debug("ERROR IN NucCollection2D.getSchematicPath() at 4, INVESTIGATE");
					this.schematicPath.moveTo((float)refNuc.getX(),
						-(float)refNuc.getY());
				}
				else
				{
					this.schematicPath.moveTo((float)pt.getX(),
						-(float)pt.getY());
				}
			}
			else
			{
				this.schematicPath.lineTo((float)refNuc.getX(),
					-(float)refNuc.getY());
			}
		}

		refNuc = refNuc.nextNuc2D();
	}

	return (this.schematicPath);
}

public void
drawSchemSegment(Nuc2D nuc0, Nuc2D nuc1, Graphics2D g2)
throws Exception
{
	GeneralPath schemPath = this.getSchematicPath(nuc0, nuc1);

	if (schemPath == null)
		return;
	
	/* SAVE for testing schemPath
	PathIterator pathIterator =
		schemPath.getPathIterator(new AffineTransform());
	while (!pathIterator.isDone())
	{
		double[] coords = new double[2];
		int pathType = pathIterator.currentSegment(coords);
		if (pathType == PathIterator.SEG_MOVETO)
			debug("pathType SEG_MOVETO: " + coords[0] + " " + coords[1]);
		else if (pathType == PathIterator.SEG_LINETO)
			debug("pathType SEG_LINETO: " + coords[0] + " " + coords[1]);
		else
			debug("pathType: ??");
		pathIterator.next();
	}
	*/
	
	g2.setColor(nuc0.getSchematicColor());
	g2.setStroke(new BasicStroke((float)nuc0.getSchematicLineWidth(),
		BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	g2.draw(schemPath);
}

private GeneralPath nucPath = null;

public GeneralPath
oldGetNucPath(Nuc2D nuc0, Nuc2D nuc1)
throws Exception
{
    if (this.nucPath == null)
		this.nucPath = new GeneralPath();
	
	Nuc2D refNuc = nuc0;
	while (true)
	{
		if ((refNuc == null) || (refNuc.getID() > nuc1.getID()))
			break;

		if (refNuc.getID() == nuc0.getID()) // then at begin of strand
		{
			this.nucPath.reset();

			// adjust to what is behind this refNuc
			
			Nuc2D lastNuc = refNuc.lastNuc2D();
			if (lastNuc == null)
			{
				// no nuc behind to adjust to, start at refNuc
				this.nucPath.moveTo((float)refNuc.getX(),
					-(float)refNuc.getY());
			}
			else
			{
				// found a nuc behind, moveTo part way back to it
				if (lastNuc.isNucPath() && !lastNuc.isSymbol())
				{
					// then found tail of last schem segment, need to only go 1/2
					// way as it joins up with another type of schem line
					Point2D pt = refNuc.getFivePrimeRay().getMidPt();
					this.nucPath.moveTo((float)pt.getX(), -(float)pt.getY());
				}
				else
				{
					// look back to lastNuc char or lastNuc symbol, get ray up
					// to it minus lastNuc.3pGap
					Point2D pt = refNuc.getFivePrimeHeadIntersect(lastNuc.getTPSchemGap());
					if (pt == null)
					{
						debug("ERROR IN NucCollection2D.getSchematicPath() at 0, INVESTIGATE");
						this.nucPath.moveTo((float)refNuc.getX(),
							-(float)refNuc.getY());
					}
					else
					{
						this.nucPath.moveTo((float)pt.getX(),
							-(float)pt.getY());
					}
				}

				// now lineTo up to refNuc
				if (refNuc.isSymbol())
				{
					// lineTo part way up to refNuc
					Point2D pt = refNuc.getFivePrimeTailIntersect(refNuc.getFPSchemGap());
					if (pt == null)
					{
						debug("ERROR IN NucCollection2D.getSchematicPath() at 1, INVESTIGATE");
						this.nucPath.lineTo((float)refNuc.getX(),
							-(float)refNuc.getY());
					}
					else
					{
						this.nucPath.lineTo((float)pt.getX(),
							-(float)pt.getY());
					}
				}
				else
				{
					this.nucPath.lineTo((float)refNuc.getX(),
						-(float)refNuc.getY());
				}
			}
		}

		if (refNuc.getID() == nuc1.getID()) // then at end of strand
		{
			if (refNuc.isSymbol())
			{
				Point2D pt = refNuc.getThreePrimeTailIntersect(refNuc.getTPSchemGap());
				if (pt == null)
				{
					debug("ERROR IN NucCollection2D.getSchematicPath() at 2, INVESTIGATE");
					this.nucPath.moveTo((float)refNuc.getX(),
						-(float)refNuc.getY());
				}
				else
				{
					this.nucPath.moveTo((float)pt.getX(),
						-(float)pt.getY());
				}
			}
			else
			{
				this.nucPath.lineTo((float)refNuc.getX(),
					-(float)refNuc.getY());
			}

			Nuc2D nextNuc = refNuc.nextNuc2D();
			if (nextNuc != null)
			{
				if (nextNuc.isSchematic() && !nextNuc.isSymbol())
				// then go halfway
				{
					Point2D pt = refNuc.getThreePrimeRay().getMidPt();
					this.nucPath.lineTo((float)pt.getX(), -(float)pt.getY());
				}
				else // refNuc is symbol or nucchar
				{
					// NEED to provide a t val towards nextNuc
					Point2D pt = refNuc.getThreePrimeHeadIntersect(nextNuc.getFPSchemGap());
					if (pt == null)
					{
						debug("ERROR IN NucCollection2D.getSchematicPath() at 3, INVESTIGATE");
					}
					else
					{
						this.nucPath.lineTo((float)pt.getX(),
							-(float)pt.getY());
					}
				}
			}
		}

		if ((refNuc.getID() != nuc0.getID()) &&
			(refNuc.getID() != nuc1.getID())) // then in strand somewhere
		{
			if (refNuc.isSymbol())
			{
				Point2D pt = refNuc.getFivePrimeTailIntersect(refNuc.getFPSchemGap());
				if (pt == null)
				{
					debug("ERROR IN NucCollection2D.getSchematicPath() at 3, INVESTIGATE");
					this.nucPath.lineTo((float)refNuc.getX(),
						-(float)refNuc.getY());
				}
				else
				{
					this.nucPath.lineTo((float)pt.getX(),
						-(float)pt.getY());
				}
				pt = refNuc.getThreePrimeTailIntersect(refNuc.getTPSchemGap());
				if (pt == null)
				{
					debug("ERROR IN NucCollection2D.getSchematicPath() at 4, INVESTIGATE");
					this.nucPath.moveTo((float)refNuc.getX(),
						-(float)refNuc.getY());
				}
				else
				{
					this.nucPath.moveTo((float)pt.getX(),
						-(float)pt.getY());
				}
			}
			else
			{
				this.nucPath.lineTo((float)refNuc.getX(),
					-(float)refNuc.getY());
			}
		}

		refNuc = refNuc.nextNuc2D();
	}

	return (this.nucPath);
}

public GeneralPath
getNucPath(Nuc2D nuc0, Nuc2D nuc1)
throws Exception
{
    if (this.nucPath == null)
		this.nucPath = new GeneralPath();
	
	Nuc2D refNuc = nuc0;
	while (true)
	{
		if ((refNuc == null) || (refNuc.getID() > nuc1.getID()))
			break;

		if (refNuc.getID() == nuc0.getID()) // then at begin of strand
		{
			this.nucPath.reset();
			this.nucPath.moveTo((float)refNuc.getX(),
				-(float)refNuc.getY());
		}
		else
		{
			this.nucPath.lineTo((float)refNuc.getX(),
				-(float)refNuc.getY());
		}

		refNuc = refNuc.nextNuc2D();
	}

	return (this.nucPath);
}

public void
drawNucPathSegment(Nuc2D nuc0, Nuc2D nuc1, Graphics2D g2)
throws Exception
{
	GeneralPath nucPath = this.getNucPath(nuc0, nuc1);

	if (nucPath == null)
		return;
	
	g2.setColor(nuc0.getNucPathColor());
	g2.setStroke(new BasicStroke((float)nuc0.getNucPathLineWidth(),
		BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	g2.draw(nucPath);
}

public void
runSetIsNucPath(boolean isNucPath, double pathWidth, Color color)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startNuc;
		while (true)
		{
			nuc.runSetIsNucPath(isNucPath, pathWidth, color);
			if (nuc.equals(endNuc))
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}

public void
clearAnnotations()
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			nuc.clearAnnotation();
			if (nuc.equals(endStrandNuc))
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}

private String groupName = null;

public void
setGroupName(String groupName)
throws Exception
{
    this.groupName = groupName;
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			nuc.setGroupName(groupName);
			if (nuc.equals(endStrandNuc))
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}

public String
getGroupName()
{
    return (this.groupName);
}

public void
clearFormatted()
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			((Nuc2D)nuc).setIsFormatted(false);
			if (nuc == endStrandNuc)
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}

public void
clearHidden()
throws Exception
{
	this.setIsHidden(false);
}

public void
colorAllRNASingleStrandedNucs(Color color)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			if (nuc.isSingleStranded())
				((Nuc2D)nuc).setColor(color);
			if (nuc == endStrandNuc)
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}

public void
setIsHidden(boolean hide)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			((Nuc2D)nuc).setIsHidden(hide);
			if (nuc == endStrandNuc)
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}

public void
setHideForConstrain(boolean hideForConstrain)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			((Nuc2D)nuc).setHideForConstrain(hideForConstrain);
			if (nuc == endStrandNuc)
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}

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

public boolean
getIsEditable()
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	if (delineators == null)
		return (false);
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			if (nuc.getIsEditable())
				return (true);
			if (nuc == endStrandNuc)
				break;
			nuc = nuc.nextNuc2D();
		}
	}
	return (false);
}

public void
setSymbols(Object drawObject)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			nuc.setSymbol(drawObject);
			if (nuc.equals(endStrandNuc))
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}

public void
setFonts(Font font)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			nuc.setFont(font);
			if (nuc.equals(endStrandNuc))
				break;
			nuc = nuc.nextNuc2D();
		}
	}
}

public static double
getArcStartAngle(Point2D centerPt, BLine2D base)
{
	Point2D fpPt = base.getP1();
	double arcAngle = MathUtil.angleInXYPlane(new BVector2d(
		fpPt.getX() - centerPt.getX(),
		fpPt.getY() - centerPt.getY()));
	
	if (arcAngle > 360.0)
		arcAngle -= 360.0;
	if (arcAngle < 0.0)
		arcAngle += 360.0;

	return (arcAngle);
}

public static double
getArcEndAngle(Point2D centerPt, BLine2D base)
{
	Point2D tpPt = base.getP2();
	double arcAngle = MathUtil.angleInXYPlane(new BVector2d(
		tpPt.getX() - centerPt.getX(),
		tpPt.getY() - centerPt.getY()));

	if (arcAngle > 360.0)
		arcAngle -= 360.0;
	if (arcAngle < 0.0)
		arcAngle += 360.0;

	return (arcAngle);
}

public static double
getArcAngle(Point2D centerPt, BLine2D base, boolean clockWiseFormat)
{
	double arcAngle = 0.0;

	// -1 == (pt left of ray), 0 == (pt on ray), 1 == (pt right of ray)
	int side = MathUtil.ptRelationToRayInXYPlane(centerPt, base);

	if ((side == -1) || (side == 0))
	{
		if (clockWiseFormat)
			arcAngle = getArcEndAngle(centerPt, base) -
				getArcStartAngle(centerPt, base);
		else
			arcAngle = 360.0 - (getArcEndAngle(centerPt, base) -
				getArcStartAngle(centerPt, base));
	}
	else
	{
		if (clockWiseFormat)
			arcAngle = 360.0 - (getArcStartAngle(centerPt, base) -
				getArcEndAngle(centerPt, base));
		else
			arcAngle = getArcStartAngle(centerPt, base) -
				getArcEndAngle(centerPt, base);
	}
	
	if (arcAngle > 360.0)
		arcAngle -= 360.0;
	if (arcAngle < 0.0)
		arcAngle += 360.0;

	return (arcAngle);
}

public void
transform(AffineTransform affTrans)
throws Exception
{
	Point2D tmpPt = new Point2D.Double();
	Point2D transformPt = new Point2D.Double();

	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			tmpPt.setLocation(refNuc.getPoint2D());
			affTrans.transform(tmpPt, transformPt);
			refNuc.setXY(transformPt.getX(), transformPt.getY());

			/*
			if ((affTrans.getType() == AffineTransform.TYPE_FLIP) && refNuc.hasNucLabel())
			*/
			/* Doesn't work yet. seems like have to rotate nuc label
			 * in its own orbit around a nuc while the nucs are orbiting
			 * around the helix midpt.
			if (refNuc.hasNucLabel())
			{
				DrawLineObject lineObj = refNuc.getLineLabel();
				if (lineObj != null)
				{
					tmpPt.setLocation(
						lineObj.getP1().getX(),
						lineObj.getP1().getY());
					affTrans.transform(tmpPt, transformPt);
					double sveX = transformPt.getX();
					double sveY = transformPt.getY();
					tmpPt.setLocation(
						lineObj.getP2().getX(),
						lineObj.getP2().getY());
					affTrans.transform(tmpPt, transformPt);
					lineObj.reset(sveX, sveY, transformPt.getX(), transformPt.getY());
				}
				DrawStringObject stringObj = refNuc.getNumberLabel();
				if (stringObj != null)
				{
					tmpPt.setLocation(stringObj.getX(), stringObj.getY());
					affTrans.transform(tmpPt, transformPt);
					stringObj.setX(transformPt.getX());
					stringObj.setY(transformPt.getY());
				}
			}
			*/

			if (refNuc == endNuc)
				break;
			// refNuc = refNuc.nextNonNullNuc2D();
			refNuc = refNuc.nextNuc2D();
		}
	}
}

public void
reformat()
throws Exception
{
	Vector delineators = this.getItemListDelineators();

	// looking for first non-singlebasepaired helix in collection.
	// if none found, assume clockWiseFormat == true
	boolean clockWiseFormat = true;

	// first try and figure out handedness of nuc collection
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);
		while (true)
		{
			if (refNuc.isHelixStart())
			{
				RNAHelix2D helix = new RNAHelix2D(refNuc);
				if (!helix.isSingleBasePairHelix())
				{
					clockWiseFormat = helix.isClockWiseFormatted();
					break;
				}
			}
			if (refNuc.equals(endNuc))
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}

	this.reformat(clockWiseFormat);
}

public void
reformat(boolean clockWiseFormat)
throws Exception
{
	Vector delineators = this.getItemListDelineators();

	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);
		while (true)
		{
			// find first helix, format it, start auto formatting from there
			if (refNuc.isHelixStart())
			{
				RNAHelix2D helix = new RNAHelix2D(refNuc);

				// format helix first
				if (helix.isSingleBasePairHelix())
				{
					// this looks same as next else statement
					helix.reformat(clockWiseFormat, (NucCollection)this);
				}
				else
				{
					helix.reformat(clockWiseFormat, (NucCollection)this);
				}

				if (helix.isHairPin())
					return;

				if (helix.isEmptyHairPin())
					return;

				if (this instanceof RNAStackedHelix2D)
				{
					if (helix.getFivePrimeStartNuc2D() == 
						((RNAStackedHelix2D)this).getThreePrimeHelix2D().getFivePrimeStartNuc2D())
						return;
				}

				if (this instanceof RNASubDomain2D)
				{
					if (helix.getFivePrimeStartNuc2D() == 
						((RNASubDomain2D)this).getStartHelix2D().getFivePrimeStartNuc2D())
						return;
				}

				// if formatting a single helix then this next
				// step causes the same starting helix above
				// to be reformatted again. Leaving for now since
				// it still works.

				// now format everything downstream
				this.formatCycleStr(
					helix.getFivePrimeEndNuc2D().nextNonNullNuc2D(),
					clockWiseFormat, true);
				
				return;
			}
			if (refNuc.equals(endNuc))
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

public void
formatCycleStr(Nuc2D refNuc, boolean clockWiseFormat, boolean continueBuild)
throws Exception
{
	// NEED to catch complexExcpetion here and make sure no pseudo knots. If so
	// then check if we're really formatting nothing more than a helix. If in
	// rna stacked helix then could affect what's behind. If in subdomain and
	// it really is just a helix to format then don't care what's behind.
	
	RNACycle2D level = new RNACycle2D(refNuc);

	level.setParentCollection((NucCollection)this);
	// level.setDistancesFromCollection((NucCollection)this);
	RNABasePair basePair = new RNABasePair();

	level.format(clockWiseFormat);

	if (!continueBuild)
		return;

	Vector levelHelices = level.getCycleHelices();

	if (levelHelices == null)
		return;

	for (int i = 1;i < levelHelices.size();i++)
	{
		RNAHelix helix = new RNAHelix((NucNode)levelHelices.elementAt(i));

		if (helix.isHairPin())
			continue;
		
		if (this instanceof RNAStackedHelix)
		{
			if (helix.getFivePrimeStartNuc() == 
				((RNAStackedHelix)this).getThreePrimeHelix().getFivePrimeStartNuc())
				return;
		}

		// check if isolated single bp helix
		if (helix.isSingleBasePairHelix() && (
			(helix.getFivePrimeStartNuc().getID() + 1) ==
			helix.getThreePrimeEndNuc().getID()))
			continue;

		NucNode nuc = helix.getFivePrimeEndNuc();
		if (nuc.isSingleBasePairHelix())
		{
			basePair.set(nuc);
			nuc = basePair.getFivePrimeNuc();
		}
		formatCycleStr((Nuc2D)nuc.nextNonNullNuc(), clockWiseFormat, continueBuild);
	}
}

public void
reformatHelicesInPlace()
throws Exception
{
	RNAHelix2D helix = new RNAHelix2D();
	boolean clockWiseFormat = true; // arbitrary for now
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		NucNode refNuc = (NucNode)delineators.elementAt(i);
		NucNode endNuc = (NucNode)delineators.elementAt(i+1);
		while (true)
		{
			if (refNuc.isHelixStart())
			{
				helix.set(refNuc);
				if (helix.isSingleBasePairHelix())
				{
					helix.reformat(clockWiseFormat, this);
				}
				else
				{
					// looking at just any helix in this collection.
					// there's probabaly a better way to do this.
					clockWiseFormat = helix.isClockWiseFormatted();
					helix.reformat(this);
				}
			}
			if (refNuc.equals(endNuc))
				break;
			refNuc = refNuc.nextNuc();
		}
	}
}

public void
printNucs()
throws Exception
{
	debug("IN PRINTNUCS, NUCCOUNT CURRENTLY: " + getNucCount());
	for (int nucID = 1;nucID <= getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		System.out.println(nuc);
	}	
}

public void
clearFlagged()
throws Exception
{
	for (int nucID = 1;nucID <= getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		nuc.setFlagged(false);
	}	
}

private String
nucAtListString(Nuc2D nuc, Nuc2D cmpNuc, String nucList, int checkMode)
throws Exception
{
	Font nucFont = nuc.getFont();

	if (cmpNuc == null)
	{
		if (checkMode == 0)
		{
			return("<Nuc RefIDs='" + nucList + "' " +
				"Color='" +
				Integer.toHexString(nuc.getColor().getRGB() & 0x00ffffff) + "' " +
				"FontID='" +
				StringUtil.fontToFontID(nucFont) + "' " +
				"FontSize='" +
				nucFont.getSize()
				+ "' " + "/>");
		}
		else if (checkMode == 1)
		{
			if (nuc.getSchematicColor() == null)
				nuc.setSchematicColor(Color.black);
			return("<Nuc RefIDs='" + nucList + "' " +
				/*
				"IsSchematic='false' " +
				*/
				"IsSchematic='" + nuc.isSchematic() + "' " +
				"SchematicColor='" +
				Integer.toHexString(nuc.getSchematicColor().getRGB() & 0x00ffffff) + "' " +
				"SchematicLineWidth='" + nuc.getSchematicLineWidth() + "' " +
				"SchematicBPLineWidth='" + nuc.getSchematicBPLineWidth()
				+ "' " + "/>");
		}
		else if (checkMode == 2)
		{
			if (nuc.getIsHidden())
				return("<Nuc RefIDs='" + nucList + "' " +
					"IsHidden='" + nuc.getIsHidden() + "' " + "/>");
		}
		else if (checkMode == 3)
		{
			if (nuc.getGroupName() != null)
				return("<Nuc RefIDs='" + nucList + "' " +
					"GroupName='" + nuc.getGroupName() + "' " + "/>");
		}
		else if (checkMode == 4)
		{
			return("<Nuc RefIDs='" + nucList + "' " +
				"SchematicBPGap='" + nuc.getBPSchemGap() + "' " +
				"SchematicFPGap='" + nuc.getFPSchemGap() + "' " +
				"SchematicTPGap='" + nuc.getTPSchemGap()
				+ "' " + "/>");
		}
		else if (checkMode == 5)
		{
			if (nuc.getNucPathColor() == null)
				nuc.setNucPathColor(Color.green);
			return("<Nuc RefIDs='" + nucList + "' " +
				"IsNucPath='" + nuc.isNucPath() + "' " +
				"NucPathColor='" +
				Integer.toHexString(nuc.getNucPathColor().getRGB() & 0x00ffffff) + "' " +
				"NucPathLineWidth='" + nuc.getNucPathLineWidth() + "' " + "/>");
		}
		return (null);
	}
	
	StringBuffer strBuf = new StringBuffer();

	if (checkMode == 0)
	{
		if (nuc.getColor().getRGB() != cmpNuc.getColor().getRGB())
			strBuf.append("Color='" + Integer.toHexString(nuc.getColor().getRGB() & 0x00ffffff) + "' ");

		if (StringUtil.fontToFontID(nuc.getFont()) != StringUtil.fontToFontID(cmpNuc.getFont()))
			strBuf.append("FontID='" + StringUtil.fontToFontID(nucFont) + "' ");

		if (nuc.getFont().getSize() != cmpNuc.getFont().getSize())
			strBuf.append("FontSize='" + nucFont.getSize() + "' ");
	}
	else if (checkMode == 1)
	{
		if (nuc.getIsSchematic() && !cmpNuc.getIsSchematic())
			strBuf.append("IsSchematic='true' ");
		else if ((!nuc.getIsSchematic()) && cmpNuc.getIsSchematic())
			strBuf.append("IsSchematic='false' ");
		if (nuc.getSchematicColor() == null)
			nuc.setSchematicColor(Color.black);
		if (nuc.getSchematicColor().getRGB() != cmpNuc.getSchematicColor().getRGB())
			strBuf.append("SchematicColor='" + Integer.toHexString(nuc.getSchematicColor().getRGB() & 0x00ffffff) + "' ");
		if (nuc.getSchematicLineWidth() != cmpNuc.getSchematicLineWidth())
			strBuf.append("SchematicLineWidth='" + nuc.getSchematicLineWidth() + "' ");
		if (nuc.getSchematicBPLineWidth() != cmpNuc.getSchematicBPLineWidth())
			strBuf.append("SchematicBPLineWidth='" + nuc.getSchematicBPLineWidth() + "' ");
	}
	else if (checkMode == 2)
	{
		if (nuc.getIsHidden() != cmpNuc.getIsHidden())
			strBuf.append("IsHidden='" + nuc.getIsHidden() + "' ");
	}
	else if (checkMode == 3)
	{
		if (nuc.getGroupName() != cmpNuc.getGroupName())
			strBuf.append("GroupName='" + nuc.getGroupName() + "' ");
	}
	else if (checkMode == 4)
	{
		if (nuc.getBPSchemGap() != cmpNuc.getBPSchemGap())
			strBuf.append("SchematicBPGap='" + nuc.getBPSchemGap() + "' ");
		if (nuc.getFPSchemGap() != cmpNuc.getFPSchemGap())
			strBuf.append("SchematicFPGap='" + nuc.getFPSchemGap() + "' ");
		if (nuc.getTPSchemGap() != cmpNuc.getTPSchemGap())
			strBuf.append("SchematicTPGap='" + nuc.getTPSchemGap() + "' ");
	}
	else if (checkMode == 5)
	{
		if (nuc.getIsNucPath() && !cmpNuc.getIsNucPath())
			strBuf.append("IsNucPath='true' ");
		else if ((!nuc.getIsNucPath()) && cmpNuc.getIsNucPath())
			strBuf.append("IsNucPath='false' ");
		if (nuc.getNucPathColor() == null)
			nuc.setNucPathColor(Color.green);
		if (nuc.getNucPathColor().getRGB() != cmpNuc.getNucPathColor().getRGB())
			strBuf.append("NucPathColor='" + Integer.toHexString(nuc.getNucPathColor().getRGB() & 0x00ffffff) + "' ");
		if (nuc.getNucPathLineWidth() != cmpNuc.getNucPathLineWidth())
			strBuf.append("NucPathLineWidth='" + nuc.getNucPathLineWidth() + "' ");
	}
	
	if (strBuf.toString().length() <= 0)
		// NEED to return null and have it dealt with properly
		// return (" ");
		return (null);

	strBuf.insert(0, "<Nuc RefIDs='" + nucList + "' ");

	strBuf.append("/>");

	return (strBuf.toString());
}

// checkMode == 0 -> nuc attributes
// checkMode == 1 -> nuc schematic attributes
// checkMode == 2 -> nuc hidden attribute
// checkMode == 3 -> nuc group name
// checkMode == 4 -> nuc schematic gap attributes
// checkMode == 5 -> nuc path attributes
public String
likeNucAttsList(SSData2D sstr, String delineator, int checkMode)
throws Exception
{
	if (sstr.getNucCount() == 1)
	{
		Nuc2D nuc = sstr.getNuc2DAt(1);
		return(this.nucAtListString(nuc, null, "1", checkMode));
	}

	this.clearFlagged();

	Vector nucList = new Vector();
	Vector largestList = new Vector();
	for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		if (nuc == null)
			continue;
		if (nuc.getFlagged())
			continue;
		nuc.setFlagged(true);
		Vector likeList = new Vector();
		likeList.add(nuc);
		for (int id = nucID + 1;id <= sstr.getNucCount();id++)
		{
			Nuc2D newNuc = sstr.getNuc2DAt(id);
			if (checkMode == 0) // nuc attributes
			{
				if (nuc.hasEqualFontAttributes(newNuc))
				{
					newNuc.setFlagged(true);
					likeList.add(newNuc);
				}
			}
			else if (checkMode == 1) // nuc schematic attributes
			{
				if (nuc.hasEqualSchematicAttributes(newNuc))
				{
					newNuc.setFlagged(true);
					likeList.add(newNuc);
				}
			}
			else if (checkMode == 2) // nuc hidden attributes
			{
				if (nuc.hasEqualHiddenAttributes(newNuc))
				{
					newNuc.setFlagged(true);
					likeList.add(newNuc);
				}
			}
			else if (checkMode == 3) // nuc group name attribute
			{
				if (nuc.hasEqualGroupNameAttributes(newNuc))
				{
					newNuc.setFlagged(true);
					likeList.add(newNuc);
				}
			}
			else if (checkMode == 4) // nuc schematic gap attributes
			{
				if (nuc.hasEqualSchematicGapAttributes(newNuc))
				{
					newNuc.setFlagged(true);
					likeList.add(newNuc);
				}
			}
			else if (checkMode == 5) // nuc path attributes
			{
				if (nuc.hasEqualNucPathAttributes(newNuc))
				{
					newNuc.setFlagged(true);
					likeList.add(newNuc);
				}
			}
		}
		nucList.add(likeList);
		if (likeList.size() >= largestList.size())
		{
			largestList = likeList;
		}
	}

	if (nucList.size() <= 0)
		return (null);
	
	if (nucList.size() == 1)
	{
		Vector likeList = (Vector)nucList.elementAt(0);
		return (this.nucAtListString((Nuc2D)likeList.elementAt(1), null, "1-" + sstr.getNucCount(), checkMode));
	}

	StringBuffer strBuf = new StringBuffer();

	// first print out largestList
	Nuc2D refNuc = (Nuc2D)largestList.elementAt(0);

	strBuf.append(this.nucAtListString(refNuc, null, "1-" + sstr.getNucCount(), checkMode));

	for (int i = 0;i < nucList.size();i++)
	{
		Vector likeList = (Vector)nucList.elementAt(i);

		// NEED to skip largestList
		if (likeList == largestList)
			continue;
		
		if (likeList.size() == 1)
		{
			Nuc2D nuc = (Nuc2D)likeList.elementAt(0);
			strBuf.append(delineator + this.nucAtListString(nuc, refNuc, Integer.toString(nuc.getID()), checkMode));
			continue;
		}

		StringBuffer subStrBuf = new StringBuffer();
		int lastNucID = 0;
		int currNucID = 0;
		boolean inRun = false;
		for (int j = 0;j < likeList.size();j++)
		{
			currNucID = ((Nuc2D)likeList.elementAt(j)).getID();
			if (j == 0)
			{
				subStrBuf.append(Integer.toString(currNucID));
				lastNucID = currNucID;
				continue;
			}
			if (currNucID > lastNucID + 1)
			{
				if (inRun)
				{
					inRun = false;
					subStrBuf.append(lastNucID);
				}
				subStrBuf.append("," + currNucID);
			}
			else if (currNucID == lastNucID + 1)
			{
				if (!inRun)
				{
					subStrBuf.append("-");
					inRun = true;
				}
			}
			lastNucID = currNucID;
		}
		if (inRun)
		{
			subStrBuf.append(currNucID);
		}
		strBuf.append(delineator + this.nucAtListString((Nuc2D)likeList.elementAt(0), refNuc, subStrBuf.toString(), checkMode));
	}

	String atts = strBuf.toString().trim();
	if (atts.length() == 0)
		return (null);
	return (atts);
}

public void
printComplexXML(PrintWriter out)
throws Exception
{
	//out.println("NucCollection2D.java printComplexXML() used.");
	
	Vector delineators = this.getItemListDelineators();
	if (delineators == null)
		return;
	if (delineators.size() == 0)
		return;
	Nuc2D startNuc = (Nuc2D)((NucNode)delineators.elementAt(0));

	SSData2D sstr = null;

	String moleculeName = this.getName();

	if (moleculeName == null)
	{
		if (this instanceof RNABasePair2D)
			moleculeName = "RNABasePair_" + startNuc.getID();
		/*
		else if (this instanceof RNAComplexArea2D)
			moleculeName = "RNAComplexArea_" + startNuc.getID();
		*/
		else if (this instanceof RNASubDomain2D)
			moleculeName = "RNASubDomain_" + startNuc.getID();
		else if (this instanceof RNAHelix2D)
			moleculeName = "RNAHelix_" + startNuc.getID();
		else if (this instanceof RNACycle2D)
			moleculeName = "RNACycle_" + startNuc.getID();
		else if (this instanceof RNAListNucs2D)
			moleculeName = "RNAListNucs_" + startNuc.getID();
		else if (this instanceof RNASingleStrand2D)
			moleculeName = "RNASingleStrand_" + startNuc.getID();
		else if (this instanceof RNAStackedHelix2D)
			moleculeName = "RNAStackedHelix_" + startNuc.getID();
		else if (this instanceof SSData2D)
			moleculeName = "SSData_" + startNuc.getID();
		else
			moleculeName = "Unknown type";
	}

	out.println("<RNAMolecule Name='" + moleculeName + "'>");

	// RNAStackedHelix has weird getX,Y(); need to center on nucs only
	if ( (!(this instanceof RNAStackedHelix2D)) )
	{
	// print out geom info
	if ((this.getX() != 0.0) || (this.getY() != 0.0))
		out.println(
			"<SceneNodeGeom CenterX='" + StringUtil.roundStrVal(this.getX(), 2) +
				"' CenterY='" + StringUtil.roundStrVal(this.getY(), 2) + "' />");
	}
	
	// print this's label list
	printLabelList(out);

	// print out nucs
	if (sstr == null)
	{
		sstr = ((Nuc2D)((NucNode)delineators.elementAt(0))).getParentSSData2D();
		if (sstr == null)
			return;
	}
/////////////////////////////////////////////////////////test spot
	//this.reverseY();
	//this.scaleCSV();
	//this.ridNegatives();
	//this.centerEverything();
	
	
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0 = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1 = (Nuc2D)((NucNode)delineators.elementAt(i+1));

		out.println("<NucListData StartNucID='" + nuc0.getID() +
			"' DataType='NucChar.XPos.YPos'>");

		// print out formatted nucs (for now throws exception in getX,Y() if unformatted
		for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			out.println(nuc.getNucChar() + " " +
				StringUtil.roundStrVal(nuc.getX(), 3) + " " +
				StringUtil.roundStrVal(nuc.getY(), 3));
			
		}

		out.println("</NucListData>");
	}

	// print out nuc attributes
	String nucAtts = this.likeNucAttsList(sstr, ":", 0);
	StringTokenizer nucRefLines = null;

	if (nucAtts != null)
	{
		nucRefLines = new StringTokenizer(nucAtts, ":");
		while (nucRefLines.hasMoreTokens())
		{
			String testStr = nucRefLines.nextToken().trim();
			if (!testStr.equals("null"))
				out.println(testStr);
		}
	}

	// print out nuc schematic attributes
	nucAtts = this.likeNucAttsList(sstr, ":", 1);
	if (nucAtts != null)
	{
		// print out any nuc schematics info
		nucRefLines = new StringTokenizer(nucAtts, ":");
		while (nucRefLines.hasMoreTokens())
		{
			String testStr = nucRefLines.nextToken().trim();
			if (!testStr.equals("null"))
				out.println(testStr);
		}
	}

	// print out hidden attribute
	nucAtts = this.likeNucAttsList(sstr, ":", 2);
	if (nucAtts != null)
	{
		nucRefLines = new StringTokenizer(nucAtts, ":");
		while (nucRefLines.hasMoreTokens())
		{
			String testStr = nucRefLines.nextToken().trim();
			if (!testStr.equals("null"))
				out.println(testStr);
		}
	}

	// print out nuc group name
	nucAtts = this.likeNucAttsList(sstr, ":", 3);
	if (nucAtts != null)
	{
		nucRefLines = new StringTokenizer(nucAtts, ":");
		while (nucRefLines.hasMoreTokens())
		{
			String testStr = nucRefLines.nextToken().trim();
			if (!testStr.equals("null"))
				out.println(testStr);
		}
	}

	// print out nuc schematic gap attributes
	nucAtts = this.likeNucAttsList(sstr, ":", 4);
	nucRefLines = null;

	if (nucAtts != null)
	{
		nucRefLines = new StringTokenizer(nucAtts, ":");
		while (nucRefLines.hasMoreTokens())
		{
			String testStr = nucRefLines.nextToken().trim();
			if (!testStr.equals("null"))
				out.println(testStr);
		}
	}

	// print out nuc path attributes
	nucAtts = this.likeNucAttsList(sstr, ":", 5);
	nucRefLines = null;

	if (nucAtts != null)
	{
		nucRefLines = new StringTokenizer(nucAtts, ":");
		while (nucRefLines.hasMoreTokens())
		{
			String testStr = nucRefLines.nextToken().trim();
			if (!testStr.equals("null"))
				out.println(testStr);
		}
	}

	// print out any nuc special format cmds (like line or circle)
// <Nuc RefID='2' Color='ff00ff' />
	
	// print out nucs labels
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0 = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1 = (Nuc2D)((NucNode)delineators.elementAt(i+1));

		for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			if ((nuc.getLabelList() == null) && (!nuc.getUseSymbol()))
				continue;
			
			out.println("<Nuc RefID='" + nuc.getID() + "'>");
			if (nuc.getLabelList() != null)
				nuc.printLabelList(out);
			if (nuc.getUseSymbol())
				nuc.printNucSymbol(out);
			out.println("</Nuc>");
		}
	}

	// print out base pair info
	RNAHelix rnaHelix = new RNAHelix();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0 = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1 = (Nuc2D)((NucNode)delineators.elementAt(i+1));

		for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			if (!nuc.isHelixStart())
				continue;
			rnaHelix.set(nuc);
			rnaHelix.printXML(out);
		}
	}
	
	// print out base pair type info (canonical, wobble, etc.)
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0 = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1 = (Nuc2D)((NucNode)delineators.elementAt(i+1));

		for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++)
		{
			Nuc2D refNuc = sstr.getNuc2DAt(nucID);
			if (refNuc == null)
				continue;
			if (!refNuc.isFivePrimeBasePair())
				continue;

			// first make sure that base overwritten pair type really
			// needs to be overwritten
			if ((refNuc.getBasePairType() == ComplexDefines.BP_TYPE_CANONICAL) &&
					refNuc.inDefaultCanonicalBasePair())
				refNuc.setNonDefaultBasePairType(ComplexDefines.BP_TYPE_UNKNOWN);
			else if ((refNuc.getBasePairType() == ComplexDefines.BP_TYPE_WOBBLE) &&
					refNuc.inDefaultWobbleBasePair())
				refNuc.setNonDefaultBasePairType(ComplexDefines.BP_TYPE_UNKNOWN);
			else if ((refNuc.getBasePairType() == ComplexDefines.BP_TYPE_MISMATCH) &&
					refNuc.inDefaultMisMatchBasePair())
				refNuc.setNonDefaultBasePairType(ComplexDefines.BP_TYPE_UNKNOWN);

			// print out overwritten base pair types
			if (refNuc.isNonDefaultBasePairType())
			{
				if (refNuc.getBasePairType() == ComplexDefines.BP_TYPE_PHOSPHATE)
					out.println("<BasePair RefID='" + refNuc.getID() + "' Type='" +
						RNABasePair.typeToString(refNuc) + "'" +

						" Line5PDeltaX='" + refNuc.getLine5PDeltaX() + "'" +
						" Line5PDeltaY='" + refNuc.getLine5PDeltaY() + "'" +
						" Line3PDeltaX='" + refNuc.getLine3PDeltaX() + "'" +
						" Line3PDeltaY='" + refNuc.getLine3PDeltaY() + "'" +
						" LabelDeltaX='" + refNuc.getLabelDeltaX() + "'" +
						" LabelDeltaY='" + refNuc.getLabelDeltaY() + "'" +
						" Label5PSide='" + refNuc.getLabel5PSide() + "' />");
				else
					out.println("<BasePair RefID='" + refNuc.getID() + "' Type='" +
						RNABasePair.typeToString(refNuc) + "'" + " />");
			}
		}
	}

	out.println("</RNAMolecule>");
}

@Override
public void printComplexCSV(PrintWriter out, LinkedList<Nuc2D> nucleotides, double minX, double minY, double maxX, double maxY) throws Exception {
	Vector
		delineators = this.getItemListDelineators();
	if (delineators != null && delineators.size() > 0) {
		Nuc2D
			startNuc = (Nuc2D)(delineators.elementAt(0));
		String
			moleculeName = this.getName();
		if (moleculeName == null) {
			if (this instanceof RNABasePair2D) {
				moleculeName = "RNABasePair_" + startNuc.getID();
			/*
			} else if (this instanceof RNAComplexArea2D) {
				moleculeName = "RNAComplexArea_" + startNuc.getID();
			*/
			} else if (this instanceof RNASubDomain2D) {
				moleculeName = "RNASubDomain_" + startNuc.getID();
			} else if (this instanceof RNAHelix2D) {
				moleculeName = "RNAHelix_" + startNuc.getID();
			} else if (this instanceof RNACycle2D) {
				moleculeName = "RNACycle_" + startNuc.getID();
			} else if (this instanceof RNAListNucs2D) {
				moleculeName = "RNAListNucs_" + startNuc.getID();
			} else if (this instanceof RNASingleStrand2D) {
				moleculeName = "RNASingleStrand_" + startNuc.getID();
			} else if (this instanceof RNAStackedHelix2D) {
				moleculeName = "RNAStackedHelix_" + startNuc.getID();
			} else if (this instanceof SSData2D) {
				moleculeName = "SSData_" + startNuc.getID();
			} else {
				moleculeName = "Unknown type";
			}
		}
		SSData2D
			sstr = ((Nuc2D)((NucNode)delineators.elementAt(0))).getParentSSData2D();
		if (sstr != null) {
			for (int i = 0; i < delineators.size(); i += 2) {
				Nuc2D
					nuc0 = (Nuc2D)delineators.elementAt(i),
					nuc1 = (Nuc2D)delineators.elementAt(i + 1);
				for (int nucID = nuc0.getID(); nucID <= nuc1.getID(); nucID++) {
					Nuc2D
						nucOriginal = (Nuc2D)sstr.getNuc2DAt(nucID),
						nucCopy = new Nuc2D(nucOriginal);
					if (nucOriginal != null) {
						Vector
							labelListOriginal = nucOriginal.getLabelList();
						if (labelListOriginal != null) {
							Vector
								labelListCopy = new Vector();
							for (Object labelObject : labelListOriginal) {
								if (labelObject instanceof DrawStringObject) {
									labelListCopy.add(new DrawStringObject((DrawStringObject)labelObject));
								} else if (labelObject instanceof DrawLineObject) {
									labelListCopy.add(new DrawLineObject((DrawLineObject)labelObject));
								}
							}
							nucCopy.setLabelList(labelListCopy);
						}
						nucleotides.add(nucCopy);
					}
				}
			}
			
			double
				// sx = (MAX_XCO - MIN_XCO) / (maxX - minX),
				// sy = -(MAX_YCO - MIN_YCO) / (maxY - minY),
				sx = Math.min((MAX_XCO - MIN_XCO) / (maxX - minX), (MAX_YCO - MIN_YCO) / (maxY - minY)),
				sy = -sx,
				cx0 = (minX + maxX) / 2d,
				cx1 = (MIN_XCO + MAX_XCO) / 2d,
				cy0 = (minY + maxY) / 2d,
				cy1 = (MIN_YCO + MAX_YCO) / 2d;
			double
				fontScalar = 1.2d;
			for (Nuc2D nucleotide : nucleotides) {
				Font
					nucleotideFont = nucleotide.getFont();
				FontMetrics
					metrics = ComplexParentFrame.frame.getFontMetrics(nucleotideFont);
				double
					nucX = sx * (nucleotide.getX() - cx0) + cx1,
					nucY = sy * (nucleotide.getY() - cy0) + cy1;
				int
					nucFontSize = (int)Math.round(Math.abs(sy) * fontScalar * nucleotideFont.getSize2D());
				out.print(
					moleculeName + ":" + nucleotide.getID() + 
					"," + nucleotide.getNucChar() + 
					"," + StringUtil.roundStrVal(nucX, 3) + 
					"," + StringUtil.roundStrVal(nucY, 3) + 
					"," + Integer.toHexString(nucleotide.getColor().getRGB() & 0x00ffffff) + 
					"," + nucFontSize
				);
				Vector
					labelList = nucleotide.getLabelList();
				if (labelList != null) {
					for (int i = 1; i < labelList.size(); i += 2) {
						DrawLineObject
							drawLineObject = (DrawLineObject)labelList.get(i - 1);
						DrawStringObject
							drawStringObject = (DrawStringObject)labelList.get(i);
						if (drawLineObject.getIsPickable() && drawStringObject.getIsPickable()) {
							BLine2D
								bLine2D = drawLineObject.getBLine2D();
							Point2D
								p1 = bLine2D.getP1(),
								p2 = bLine2D.getP2();
							String
								labelText = drawStringObject.getDrawString();
							int
								fontSize = (int)Math.round(Math.abs(sy) * drawStringObject.getFont().getSize2D());
							out.print(
								"," + StringUtil.roundStrVal(nucX + sx * p1.getX(), 2) + 
								"," + StringUtil.roundStrVal(nucY + sy * p1.getY(), 2) + 
								"," + StringUtil.roundStrVal(nucX + sx * p2.getX(), 2) + 
								"," + StringUtil.roundStrVal(nucY + sy * p2.getY(), 2) + 
								"," + StringUtil.roundStrVal(drawLineObject.getLineStroke().getLineWidth(), 2) + 
								"," + Integer.toHexString(drawLineObject.getColor().getRGB() & 0x00ffffff) +
								"," + StringUtil.roundStrVal(nucX + sx * (drawStringObject.getX() - metrics.stringWidth(labelText) / 2d), 2) +
								"," + StringUtil.roundStrVal(nucY + sy * drawStringObject.getY() + nucFontSize / 2d, 2) + 
//								"," + StringUtil.roundStrVal(sx * (nucX + drawStringObject.getX() - cx0) + cx1 - metrics.stringWidth(labelText) / 2d, 2) + 
//								"," + StringUtil.roundStrVal(sy * (nucY + drawStringObject.getY() - cy0) + cy1 + nucFontSize / 4d, 2) + 
								"," + labelText +
								"," + StringUtil.roundStrVal(fontSize, 2) + 
								"," + Integer.toHexString(drawStringObject.getColor().getRGB() & 0x00ffffff)
							);
						}
					}
				}
				out.println();
			}
//			this.reverseY(nucleotides);
//			minY = MAX_YCO - maxY;
//			maxY = MAX_YCO - minY;
////			minY = MAX_YCO - minY;
////			maxY = MAX_YCO - maxY;
////			double temp = minY;
////			minY = maxY;
////			maxY = temp;
////			this.scaleEverything(nucleotides, minX, minY, maxX, maxY); //also scales font of nuc and labels
//			double
//				scale = Math.min((MAX_XCO - MIN_XCO) / (maxX - minX), (MAX_YCO - MIN_YCO) / (maxY - minY));
//			minX *= scale;
//			minY *= scale;
//			maxX *= scale;
//			maxY *= scale;
////			this.ridNegatives(nucleotides, minX, minY);
//			double
//				shiftX = MIN_XCO - minX,
//				shiftY = MIN_YCO - minY;
//			minX += shiftX;
//			maxX += shiftX;
//			minY += shiftY;
//			maxY += shiftY;
////			this.centerEverything(nucleotides, maxX, maxY);
//			
//			double
//				fontScalar = 1.2d;
//			for (Nuc2D nucleotide : nucleotides) {
//				Font
//					nucleotideFont = nucleotide.getFont();
//				FontMetrics
//					metrics = ComplexParentFrame.frame.getFontMetrics(nucleotideFont);
//				out.print(
//					moleculeName + ":" + nucleotide.getID() + 
//					"," + nucleotide.getNucChar() + 
//					"," + StringUtil.roundStrVal(nucleotide.getX(), 3) + 
//					"," + StringUtil.roundStrVal(nucleotide.getY(), 3) + 
//					"," + Integer.toHexString(nucleotide.getColor().getRGB() & 0x00ffffff) + 
//					"," + StringUtil.roundStrVal(nucleotideFont.getSize() * fontScalar, 3)
//				);
//				Vector
//					labelList = nucleotide.getLabelList();
//				if (labelList != null) {
//					for (int j = 1; j < labelList.size(); j += 2) {
//						DrawLineObject
//							drawLineObject = (DrawLineObject)labelList.get(j - 1);
//						DrawStringObject
//							drawStringObject = (DrawStringObject)labelList.get(j);
//						if (drawLineObject.getIsPickable() && drawStringObject.getIsPickable()) {
//							BLine2D
//								bLine2D = drawLineObject.getBLine2D();
//							Point2D
//								p1 = bLine2D.getP1(),
//								p2 = bLine2D.getP2();
//							double
//								nucX = nucleotide.getX(),
//								nucY = nucleotide.getY();
//							String
//								labelText = drawStringObject.getDrawString();
//							int
//								fontSize = drawStringObject.getFont().getSize();
//							out.print(
//								"," + StringUtil.roundStrVal(nucX + p1.getX(), 2) + 
//								"," + StringUtil.roundStrVal(nucY + p1.getY(), 2) + 
//								"," + StringUtil.roundStrVal(nucX + p2.getX(), 2) + 
//								"," + StringUtil.roundStrVal(nucY + p2.getY(), 2) + 
//								"," + StringUtil.roundStrVal(drawLineObject.getLineStroke().getLineWidth(), 2) + 
//								"," + Integer.toHexString(drawLineObject.getColor().getRGB() & 0x00ffffff) + 
//								"," + StringUtil.roundStrVal(nucX + drawStringObject.getX() - metrics.stringWidth(labelText) / 2d, 2) + 
//								"," + StringUtil.roundStrVal(nucY + drawStringObject.getY() + nucleotideFont.getSize() / 4d, 2) + 
//								"," + labelText + 
//								"," + StringUtil.roundStrVal(fontSize * fontScalar, 2) + 
//								"," + Integer.toHexString(drawStringObject.getColor().getRGB() & 0x00ffffff)
//							);
//						}
//					}
//				}
//				out.println();
//			}
////			for (int i = 0; i < delineators.size(); i+=2) {
////				Nuc2D
////					nuc0 = (Nuc2D)((NucNode)delineators.elementAt(i)),
////					nuc1 = (Nuc2D)((NucNode)delineators.elementAt(i+1));
////				for (int nucID = nuc0.getID(); nucID <= nuc1.getID(); nucID++) {
////					Nuc2D
////						nuc = sstr.getNuc2DAt(nucID);
////					if (nuc != null) {
////						String
////							outputLine = moleculeName + ":" + nucID + "," + nuc.getNucChar() + "," + StringUtil.roundStrVal(nuc.getX(), 3) + "," + StringUtil.roundStrVal(nuc.getY(), 3) + "," + Integer.toHexString(nuc.getColor().getRGB() & 0x00ffffff) + "," + nuc.getFont().getSize();
////						out.print(outputLine);
////						Vector
////							labelList = nuc.getLabelList();
////						if (labelList != null) {
////							int
////								labelListSize = labelList.size();
////							for (int j = 1; j < labelListSize; j += 2) {
////								DrawLineObject
////									drawLineObject = (DrawLineObject)labelList.get(j - 1);
////								DrawStringObject
////									drawStringObject = (DrawStringObject)labelList.get(j);
////								if (drawLineObject.getIsPickable() && drawStringObject.getIsPickable()) {
////									BLine2D
////										bLine2D = drawLineObject.getBLine2D();
////									Point2D
////										p1 = bLine2D.getP1(),
////										p2 = bLine2D.getP2();
////									double
////										nucX = nuc.getX(),
////										nucY = nuc.getY();
//////									System.out.println(drawStringObject.getDrawString());
////									out.print(", " + StringUtil.roundStrVal(nucX, 2) + "," + StringUtil.roundStrVal(nucY, 2) + "," + StringUtil.roundStrVal(nucX + drawStringObject.getX(), 2) + "," + StringUtil.roundStrVal(nucY + drawStringObject.getY(), 2) + "," + StringUtil.roundStrVal(drawLineObject.getLineStroke().getLineWidth(), 2) + "," + Integer.toHexString(drawLineObject.getColor().getRGB() & 0x00ffffff) + "," + (StringUtil.roundStrVal(nucX + drawStringObject.getX(), 2) + "," + StringUtil.roundStrVal(nucY + drawStringObject.getY(), 2) + "," + drawStringObject.getDrawString()+ "," + drawStringObject.getFont().getSize() + "," + Integer.toHexString(drawStringObject.getColor().getRGB() & 0x00ffffff)));
//////									out.print(", " + StringUtil.roundStrVal(nucX + p1.getX(), 2) + "," + StringUtil.roundStrVal(nucY + p1.getY(), 2) + "," + StringUtil.roundStrVal(nucX + p2.getX(), 2) + "," + StringUtil.roundStrVal(nucY + p2.getY(), 2) + "," + StringUtil.roundStrVal(drawLineObject.getLineStroke().getLineWidth(), 2) + "," + Integer.toHexString(drawLineObject.getColor().getRGB() & 0x00ffffff) + "," + (StringUtil.roundStrVal(nucX + drawStringObject.getX(), 2) + "," + StringUtil.roundStrVal(nucY + drawStringObject.getY(), 2) + "," + drawStringObject.getDrawString()+ "," + drawStringObject.getFont().getSize() + "," + Integer.toHexString(drawStringObject.getColor().getRGB() & 0x00ffffff)));
////								}
////							}
////						}
////						out.println();
////					}
////				}
////			}
		}
	}
}


@Override
public void printComplexTR(PrintWriter out, LinkedList<Nuc2D> nucleotides, double minX, double minY, double maxX, double maxY) throws Exception {
	Vector delineators = this.getItemListDelineators();
	if (delineators != null && delineators.size() > 0) {
		Nuc2D
			startNuc = (Nuc2D)delineators.elementAt(0);
	
		String
			moleculeName = this.getName();
		if (moleculeName == null) {
			if (this instanceof RNABasePair2D)
				moleculeName = "RNABasePair_" + startNuc.getID();
			/*
			else if (this instanceof RNAComplexArea2D)
				moleculeName = "RNAComplexArea_" + startNuc.getID();
			*/
			else if (this instanceof RNASubDomain2D)
				moleculeName = "RNASubDomain_" + startNuc.getID();
			else if (this instanceof RNAHelix2D)
				moleculeName = "RNAHelix_" + startNuc.getID();
			else if (this instanceof RNACycle2D)
				moleculeName = "RNACycle_" + startNuc.getID();
			else if (this instanceof RNAListNucs2D)
				moleculeName = "RNAListNucs_" + startNuc.getID();
			else if (this instanceof RNASingleStrand2D)
				moleculeName = "RNASingleStrand_" + startNuc.getID();
			else if (this instanceof RNAStackedHelix2D)
				moleculeName = "RNAStackedHelix_" + startNuc.getID();
			else if (this instanceof SSData2D)
				moleculeName = "SSData_" + startNuc.getID();
			else
				moleculeName = "Unknown type";
		}
		// print out nucs
		SSData2D
			sstr = ((Nuc2D)((NucNode)delineators.elementAt(0))).getParentSSData2D();
		if (sstr != null) {
			for (int i = 1; i < delineators.size(); i += 2) {
				Nuc2D
					nuc0 = (Nuc2D)delineators.elementAt(i - 1),
					nuc1 = (Nuc2D)delineators.elementAt(i);
				for (int nucID = nuc0.getID(); nucID <= nuc1.getID(); nucID++) {
					Nuc2D
						nuc = sstr.getNuc2DAt(nucID);
					if (nuc != null) {
						nucleotides.add(new Nuc2D(nuc));
					}
				}
			}
			
			this.reverseY(nucleotides);
			minY = MAX_YCO - minY;
			maxY = MAX_YCO - maxY;
			double temp = minY;
			minY = maxY;
			maxY = temp;
			this.scaleEverything(nucleotides, minX, minY, maxX, maxY); //also scales font of nuc and labels
			double scale = Math.min((MAX_XCO - MIN_XCO) / (maxX - minX), (MAX_YCO - MIN_YCO) / (maxY - minY));
			minX *= scale;
			minY *= scale;
			maxX *= scale;
			maxY *= scale;
			this.ridNegatives(nucleotides, minX, minY);
			double
				shiftX = MIN_XCO - minX,
				shiftY = MIN_YCO - minY;
			minX += shiftX;
			maxX += shiftX;
			minY += shiftY;
			maxY += shiftY;
			this.centerEverything(nucleotides, maxX, maxY);
			
			for (Nuc2D nucleotide : nucleotides) {
				out.println("<point x=\"" + StringUtil.roundStrVal(nucleotide.getX(), 3) + "\" y=\"" + StringUtil.roundStrVal(nucleotide.getY(), 3) + "\" b=\"" + nucleotide.getNucChar() + "\" numbering-label=\"" + nucleotide.getID() + "\" />");
			}
//			for (int i = 0; i < delineators.size(); i += 2) {
//				Nuc2D nuc0 = (Nuc2D)((NucNode)delineators.elementAt(i));
//				Nuc2D nuc1 = (Nuc2D)((NucNode)delineators.elementAt(i+1));
//				
//				// print out formatted nucs (for now throws exception in getX,Y() if unformatted
//				for (int nucID = nuc0.getID(); nucID <= nuc1.getID(); nucID++) {
//					Nuc2D nuc = sstr.getNuc2DAt(nucID);
//					if (nuc != null) {
//						out.println("<point x=\"" + StringUtil.roundStrVal(nuc.getX(), 3) + 
//							"\" y=\"" + StringUtil.roundStrVal(nuc.getY(), 3) + 
//							"\" b=\"" + nuc.getNucChar() + "\" numbering-label=\"" + nucID + "\" />");
//					}
//				}
//			}
		}
	}
}


@Override
public void printComplexSVG(PrintWriter out, LinkedList<Nuc2D> nucleotides, double minX, double minY, double maxX, double maxY) throws Exception {
	Vector
		delineators = this.getItemListDelineators();
	if (delineators != null && delineators.size() > 0) {
		Nuc2D
			startNuc = (Nuc2D)delineators.elementAt(0);
		String
			moleculeName = this.getName();
		if (moleculeName == null) {
			if (this instanceof RNABasePair2D) {
				moleculeName = "RNABasePair_" + startNuc.getID();
			}
			/*
			else if (this instanceof RNAComplexArea2D)
				moleculeName = "RNAComplexArea_" + startNuc.getID();
			*/
			else if (this instanceof RNASubDomain2D) {
				moleculeName = "RNASubDomain_" + startNuc.getID();
			} else if (this instanceof RNAHelix2D) {
				moleculeName = "RNAHelix_" + startNuc.getID();
			} else if (this instanceof RNACycle2D) {
				moleculeName = "RNACycle_" + startNuc.getID();
			} else if (this instanceof RNAListNucs2D) {
				moleculeName = "RNAListNucs_" + startNuc.getID();
			} else if (this instanceof RNASingleStrand2D) {
				moleculeName = "RNASingleStrand_" + startNuc.getID();
			} else if (this instanceof RNAStackedHelix2D) {
				moleculeName = "RNAStackedHelix_" + startNuc.getID();
			} else if (this instanceof SSData2D) {
				moleculeName = "SSData_" + startNuc.getID();
			} else {
				moleculeName = "Unknown type";
			}
		}
		SSData2D
			sstr = ((Nuc2D)delineators.elementAt(0)).getParentSSData2D();
		if (sstr != null) {
			for (int i = 1; i < delineators.size(); i += 2) {
				Nuc2D
					nuc0 = (Nuc2D)delineators.elementAt(i - 1),
					nuc1 = (Nuc2D)delineators.elementAt(i);
				int
					nuc1ID = nuc1.getID();
				for (int nucID = nuc0.getID(); nucID <= nuc1ID; nucID++) {
					Nuc2D
						nucOriginal = sstr.getNuc2DAt(nucID);
					if (nucOriginal != null) {
						Nuc2D
							nucCopy = new Nuc2D(nucOriginal);
						Vector
							labelListOriginal = nucOriginal.getLabelList();
						if (labelListOriginal != null) {
							Vector
								labelListCopy = new Vector();
							for (Object labelObject : labelListOriginal) {
								if (labelObject instanceof DrawStringObject) {
									labelListCopy.add(new DrawStringObject((DrawStringObject)labelObject));
								} else if (labelObject instanceof DrawLineObject) {
									labelListCopy.add(new DrawLineObject((DrawLineObject)labelObject));
								}
							}
							nucCopy.setLabelList(labelListCopy);
						}
						nucleotides.add(nucCopy);
					}
				}
			}
			nucleotides.sort((Nuc2D n0, Nuc2D n1) -> n0.getID() - n1.getID());
			for (Nuc2D nuc : nucleotides) {
				if (nuc.isBasePair()) {
					nuc.setBasePair(nucleotides.get(nuc.getBasePairID() - 1));
				}
			}
			this.reverseY(nucleotides);
			minY = MAX_YCO - minY;
			maxY = MAX_YCO - maxY;
			double
				temp = minY;
			minY = maxY;
			maxY = temp;
			this.scaleEverything(nucleotides, minX, minY, maxX, maxY); //also scales font of nuc and labels
			double
				scalar = Math.min((MAX_XCO - MIN_XCO) / (maxX - minX), (MAX_YCO - MIN_YCO) / (maxY - minY));
			minX *= scalar;
			minY *= scalar;
			maxX *= scalar;
			maxY *= scalar;
			this.ridNegatives(nucleotides, minX, minY);
			double
				dx = MIN_XCO - minX,
				dy = MIN_YCO - minY;
			minX += dx;
			maxX += dx;
			minY += dy;
			maxY += dy;
			this.centerEverything(nucleotides, maxX, maxY);
			LinkedList<String>
				letters = new LinkedList<String>(),
				labelsLines = new LinkedList<String>(),
				labelsText = new LinkedList<String>();
			for (Nuc2D nuc : nucleotides) {
				Font
					nucFont = nuc.getFont();
				FontMetrics
					metrics = ComplexParentFrame.frame.getFontMetrics(nucFont);
				double
					nucX = nuc.getX(),
					nucY = nuc.getY();
				Color
					nucColor = nuc.getColor();
				letters.add("<text id=\"" + nuc.getID() + "\" transform=\"matrix(1 0 0 1 " + StringUtil.roundStrVal(nucX, 2) + " " + StringUtil.roundStrVal(nucY, 2) + ")\" fill=\"rgb(" + nucColor.getRed() + ", " + nucColor.getGreen() + ", " + nucColor.getBlue() + ")\" font-family=\"" + nucFont.getFamily() + "\" font-weight=\"normal\" font-size=\"" + nucFont.getSize() + "\">" + nuc.getNucChar() + "</text>");
				dx = metrics.charWidth(nuc.getNucChar()) / 2d;
				dy = -nucFont.getSize2D() / 2d;
				Vector
					labelList = nuc.getLabelList();
				if (labelList != null) {
					int
						labelListSize = labelList.size();
					for (int i = 1; i < labelListSize;) {
						DrawLineObject
							drawLineObject = (DrawLineObject)labelList.get(i - 1);
						if (drawLineObject.getIsPickable()) {
							BLine2D
								bLine2D = drawLineObject.getBLine2D();
							Point2D
								p0 = bLine2D.getP1(),
								p1 = bLine2D.getP2();
							labelsLines.add("<line fill=\"none\" stroke=\"black\" stroke-width=\"" + drawLineObject.getLineStroke().getLineWidth() + "\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"" + StringUtil.roundStrVal(nucX + dx + p0.getX(), 2) + "\" y1=\"" + StringUtil.roundStrVal(nucY + dy + p0.getY(), 2) + "\" x2=\"" + StringUtil.roundStrVal(nucX + dx + p1.getX(), 2) + "\" y2=\"" + StringUtil.roundStrVal(nucY + dy + p1.getY(), 2) + "\"/>");
						}
						Object
							labelObject = labelList.get(i);
						if (labelObject instanceof DrawStringObject) {
							DrawStringObject
								drawStringObject = (DrawStringObject)labelObject;
							if (drawStringObject.getIsPickable()) {
								Font
									labelFont = drawStringObject.getFont();
								FontMetrics
									labelMetrics = ComplexParentFrame.frame.getFontMetrics(labelFont);
								String
									label = drawStringObject.getDrawString();
								dx = -labelMetrics.stringWidth(label) / 2d / 1.25d;
								labelsText.add("<text transform=\"matrix(1 0 0 1 " + StringUtil.roundStrVal(nucX + drawStringObject.getX() + dx, 2) + " " + StringUtil.roundStrVal(nucY + drawStringObject.getY(), 2) + ")\" fill=\"" + Integer.toHexString(drawStringObject.getColor().getRGB() & 0x00ffffff) + "\" font-family=\"" + labelFont.getFamily() + "\" font-size=\"" + labelFont.getSize() + "\">" + label + "</text>");
							}
							i += 2;
						} else {
							i++;
						}
					}
				}
			}
			out.println("<g id=\"Letters\">");
			for (String letter : letters) {
				out.println("\t" + letter);
			}
			out.println("</g>");
			out.println("<g id=\"Labels_Lines\">");
			for (String labelLine : labelsLines) {
				out.println("\t" + labelLine);
			}
			out.println("</g>");
			out.println("<g id=\"Labels_Text\">");
			for (String labelText : labelsText) {
				out.println("\t" + labelText);
			}
			out.println("</g>");
		}
	}
}

@Override
public void printComplexBPSeq(PrintWriter out, LinkedList<Nuc2D> nucleotides) throws Exception {
	Vector
		delineators = this.getItemListDelineators();
	if (delineators != null && delineators.size() > 0) {
		SSData2D
			sstr = ((Nuc2D)delineators.elementAt(0)).getParentSSData2D();
		if (sstr != null) {
			for (int i = 1; i < delineators.size(); i += 2) {
				Nuc2D
					nuc0 = (Nuc2D)delineators.elementAt(i - 1),
					nuc1 = (Nuc2D)delineators.elementAt(i);
				int
					nuc1ID = nuc1.getID();
				for (int nucID = nuc0.getID(); nucID <= nuc1ID; nucID++) {
					nucleotides.add(sstr.getNuc2DAt(nucID));
				}
			}
		}
	}
}

public void scaleCSV() throws Exception {
	double maxY = 0;
	double maxX = 0;
	double minY = 0;
	double minX = 0;
	double newY = 0;
	double newX = 0;
	//double newLX
	
	float fontSize = 0;
	
	minY = this.getSmallestYVal();
	minX = this.getSmallestXVal();
	maxY = this.getLargestYVal();
	maxX = this.getLargestXVal();
	
	double rangeXP = MAX_XCO - MIN_XCO;
	double rangeYP = MAX_YCO - MIN_YCO;
	
	double rx = maxX - minX;
	double ry = maxY - minY;
	
	//scalling up
	if ((rx < rangeXP) && (ry < rangeYP)) {
		double scaleX = rangeXP/rx;
		double scaleY = rangeYP/ry;
		double scale = 0;
		
		//scale up by the smallest scale, either x or y
		scale = scaleX < scaleY ? scaleX : scaleY;
		
		Vector nucList = this.getItemListDelineators();
		SSData2D sstr = ((Nuc2D)((NucNode)nucList.elementAt(0))).getParentSSData2D();
		for (int i = 0;i < nucList.size();i+=2)
		{
			Nuc2D nuc0 = (Nuc2D)((NucNode)nucList.elementAt(i));
			Nuc2D nuc1 = (Nuc2D)((NucNode)nucList.elementAt(i+1));

			for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++)
			{
				Nuc2D refNuc = sstr.getNuc2DAt(nucID);
				
				newY = refNuc.getY() * scale;
				newX = refNuc.getX() * scale;
				refNuc.setY(newY);
				refNuc.setX(newX);
				
				fontSize = (float)(refNuc.getFont().getSize() * scale);
				Font font = new Font(refNuc.getFont().getFontName(), refNuc.getFont().getStyle(),Math.round(fontSize));
				refNuc.setFont(font);
				
				if (refNuc.getLabelList() != null) {
					System.out.println("Scaling by: " + scale);
					refNuc.callAdjustLabelListCSV(0, 0, 0, scale);
				}
			}
			
		}
	}
	//scale down
	else if ((rx > rangeXP) || (ry > rangeYP)) {
		
		double scaleX = rangeXP/rx;
		double scaleY = rangeYP/ry;
		
		double scale = 0;	
		//scale may be the only difference between scaling up or down... or not
		//probably redundant
		//need to use smaller scale because bigger range creates smaller fraction
		if ((rx > rangeXP) && (ry > rangeYP)) { scale = scaleX < scaleY ? scaleX : scaleY; }
		else if ((rx > rangeXP) && (ry < rangeYP)) { scale = scaleX; }
		else if  ((ry > rangeYP) && (rx < rangeXP)) { scale = scaleY; }
		
		Vector nucList = this.getItemListDelineators();
		SSData2D sstr = ((Nuc2D)((NucNode)nucList.elementAt(0))).getParentSSData2D();
		for (int i = 0;i < nucList.size(); i +=2) {
			Nuc2D nuc0 = (Nuc2D)((NucNode)nucList.elementAt(i));
			Nuc2D nuc1 = (Nuc2D)((NucNode)nucList.elementAt(i+1));

			for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++) {
				Nuc2D refNuc = sstr.getNuc2DAt(nucID);
				
				newY = refNuc.getY() * scale;
				newX = refNuc.getX() * scale;
				refNuc.setY(newY);
				refNuc.setX(newX);
				
				fontSize = (float)(refNuc.getFont().getSize() * scale);
				Font font = new Font(refNuc.getFont().getFontName(), refNuc.getFont().getStyle(),Math.round(fontSize));
				refNuc.setFont(font);
				
				if (refNuc.getLabelList() != null) {
					System.out.println("Scaling by: " + scale);
					refNuc.callAdjustLabelListCSV(0, 0, 0, scale);
				}
			}
		}
	}
}

public void scaleEverything(LinkedList<Nuc2D> nucleotides, double minX, double minY, double maxX, double maxY) throws Exception {
	double
		scalar = Math.min((MAX_XCO - MIN_XCO) / (maxX - minX), (MAX_YCO - MIN_YCO) / (maxY - minY));
	for (Nuc2D nuc : nucleotides) {
		nuc.setX(nuc.getX() * scalar);
		nuc.setY(nuc.getY() * scalar);
		Font
			font = nuc.getFont();
		nuc.setFont(new Font(font.getName(), font.getStyle(), (int)Math.round(font.getSize() * scalar)));
		Vector
			labelList = nuc.getLabelList();
		if (labelList != null) {
			int
				labelListSize = labelList.size();
			for (int i = 0; i < labelListSize; i++) {
				Object
					drawObject = labelList.get(i);
				if (drawObject instanceof DrawStringObject) {
					DrawStringObject
						drawStringObject = (DrawStringObject)drawObject;
					Font
						drawFont = drawStringObject.getFont();
					drawStringObject.setX(drawStringObject.getX() * scalar);
					drawStringObject.setY(drawStringObject.getY() * scalar);
					
					drawStringObject.setFont(new Font(font.getName(), font.getStyle(), (int)(drawFont.getSize() * scalar)));
				} else if (drawObject instanceof DrawLineObject) {
					DrawLineObject
						lineObj = (DrawLineObject)drawObject;
					BLine2D
						bLine2D = lineObj.getBLine2D();
					Point2D
						p0 = bLine2D.getP1(),
						p1 = bLine2D.getP2();
					lineObj.reset(p0.getX() * scalar, p0.getY() * scalar, p1.getX() * scalar, p1.getY() * scalar);
				}
			}
		}
	}
}

public void scaleEverything(double minX, double minY, double maxX, double maxY) throws Exception {
	float
		fontSize = 0f;
	double
		rangeXP = MAX_XCO - MIN_XCO,
		rangeYP = MAX_YCO - MIN_YCO,
		rx = maxX - minX,
		ry = maxY - minY,
		newX = 0d,
		newY = 0d;
	
	//scalling up
	if ((rx < rangeXP) && (ry < rangeYP)) {
		double
			scaleX = rangeXP/rx,
			scaleY = rangeYP/ry,
			scale = 0d;
		
		//scale up by the smallest scale, either x or y
		scale = scaleX < scaleY ? scaleX : scaleY;
		
		Vector nucList = this.getItemListDelineators();
		SSData2D sstr = ((Nuc2D)((NucNode)nucList.elementAt(0))).getParentSSData2D();
		for (int i = 0;i < nucList.size();i+=2)
		{
			Nuc2D nuc0 = (Nuc2D)((NucNode)nucList.elementAt(i));
			Nuc2D nuc1 = (Nuc2D)((NucNode)nucList.elementAt(i+1));

			for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++)
			{
				Nuc2D refNuc = sstr.getNuc2DAt(nucID);
				
				newY = refNuc.getY() * scale;
				newX = refNuc.getX() * scale;
				refNuc.setY(newY);
				refNuc.setX(newX);
				
				fontSize = (float)(refNuc.getFont().getSize() * scale);
				Font font = new Font(refNuc.getFont().getFontName(), refNuc.getFont().getStyle(),Math.round(fontSize));
				refNuc.setFont(font);
				
				if (refNuc.getLabelList() != null) {
					System.out.println("Scaling by: " + scale);
					refNuc.callAdjustLabelListCSV(0, 0, 0, scale);
				}
			}
			
		}
	}
	//scale down
	else if ((rx > rangeXP) || (ry > rangeYP)) {
		
		double scaleX = rangeXP/rx;
		double scaleY = rangeYP/ry;
		
		double scale = 0;	
		//scale may be the only difference between scaling up or down... or not
		//probably redundant
		//need to use smaller scale because bigger range creates smaller fraction
		if ((rx > rangeXP) && (ry > rangeYP)) { scale = scaleX < scaleY ? scaleX : scaleY; }
		else if ((rx > rangeXP) && (ry < rangeYP)) { scale = scaleX; }
		else if ((ry > rangeYP) && (rx < rangeXP)) { scale = scaleY; }
		
		// scaleX = rangeXP / rx
		// flagA = rx > rangeXP <-> scaleX < 1
		
		// scaleY = rangeXP / ry
		// flagB = ry > rangeYP <-> scaleY < 1
		
		// flagA | flagB | scaleX < scaleY | scale
		// false | false | ?			   | min(scaleX, scaleY)
		// false | true  | false		   | scaleY
		// true  | false | true			   | scaleX
		// true  | true  | ?			   | min(scaleX, scaleY)
		
		// Conclusion: scale = min(scaleX, scaleY)
		
		Vector nucList = this.getItemListDelineators();
		SSData2D sstr = ((Nuc2D)((NucNode)nucList.elementAt(0))).getParentSSData2D();
		for (int i = 0; i < nucList.size(); i +=2) {
			Nuc2D nuc0 = (Nuc2D)((NucNode)nucList.elementAt(i));
			Nuc2D nuc1 = (Nuc2D)((NucNode)nucList.elementAt(i+1));

			for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++) {
				Nuc2D refNuc = sstr.getNuc2DAt(nucID);
				
				newY = refNuc.getY() * scale;
				newX = refNuc.getX() * scale;
				refNuc.setY(newY);
				refNuc.setX(newX);
				
				fontSize = (float)(refNuc.getFont().getSize() * scale);
				Font font = new Font(refNuc.getFont().getFontName(), refNuc.getFont().getStyle(),Math.round(fontSize));
				refNuc.setFont(font);
				
				if (refNuc.getLabelList() != null) {
					System.out.println("Scaling by: " + scale);
					refNuc.callAdjustLabelListCSV(0, 0, 0, scale);
				}
			}
		}
	}
}

public void ridNegatives(LinkedList<Nuc2D> nucleotides, double minX, double minY) throws Exception {
	double
		dx = MIN_XCO - minX,
		dy = MIN_YCO - minY;
	for (Nuc2D nuc : nucleotides) {
		nuc.setX(nuc.getX() + dx);
		nuc.setY(nuc.getY() + dy);
	}
}

public void ridNegatives(double minX, double minY) throws Exception {
	if (minX != MIN_XCO) {
		double
			shiftX = MIN_XCO - minX;
		this.shiftAllX(shiftX);
	}
	if (minY != MIN_YCO) {
		double
			shiftY = MIN_YCO - minY;
		this.shiftAllY(shiftY);
	}
}

public void centerEverything(LinkedList<Nuc2D> nucleotides, double maxX, double maxY) throws Exception {
	if (maxX < MAX_XCO) {
		double
			dx = (MAX_XCO - maxX) / 2d;
		for (Nuc2D nuc : nucleotides) {
			nuc.setX(nuc.getX() + dx);
		}
	}
	if (maxY < MAX_YCO) {
		double
			dy = (MAX_YCO - maxY) / 2d;
		for (Nuc2D nuc : nucleotides) {
			nuc.setY(nuc.getY() + dy);
		}
	}
}

public void centerEverything(double maxX, double maxY) throws Exception {
	if (maxX < MAX_XCO) {
		double shiftX = (MAX_XCO-maxX)/2;
		shiftAllX(shiftX);
	}
	if (maxY < MAX_YCO) {
		double shiftY = (MAX_YCO-maxY)/2;
		shiftAllY(shiftY);
	}
}

public void reverseY(LinkedList<Nuc2D> nucleotides) throws Exception {
	for (Nuc2D nuc : nucleotides) {
		nuc.setY(MAX_YCO - nuc.getY());
		Vector
			labelList = nuc.getLabelList();
		if (labelList != null) {
			int
				labelListSize = labelList.size();
			for (int i = 0; i < labelListSize; i++) {
				Object
					drawObject = labelList.get(i);
				if (drawObject instanceof DrawStringObject) {
					DrawStringObject
						drawStringObject = (DrawStringObject)drawObject;
					drawStringObject.setY(-drawStringObject.getY());
				} else if (drawObject instanceof DrawLineObject) {
					DrawLineObject
						lineObject = (DrawLineObject)drawObject;
					BLine2D
						bLine2D = lineObject.getBLine2D();
					Point2D
						p0 = bLine2D.getP1(),
						p1 = bLine2D.getP2();
					lineObject.reset(p0.getX(), -p0.getY(), p1.getX(), -p1.getY());
				}
			}
		}
	}
}

//images come out upsidedown so we reverse y to fix it
//y max coordinate is 756
public void
reverseY()
throws Exception {
	Vector
		nucList = this.getItemListDelineators();
	SSData2D sstr = ((Nuc2D)((NucNode)nucList.elementAt(0))).getParentSSData2D();
	
	for (int i = 1; i < nucList.size(); i+=2)
	{
		Nuc2D
			nuc0 = (Nuc2D)((NucNode)nucList.elementAt(i - 1)),
			nuc1 = (Nuc2D)((NucNode)nucList.elementAt(i));
		for (int nucID = nuc0.getID(); nucID <= nuc1.getID(); nucID++)
		{
			Nuc2D
				refNuc = sstr.getNuc2DAt(nucID);
			refNuc.setY(MAX_YCO - refNuc.getY());
			refNuc.callAdjustLabelListCSV(2, 0, 0, 0);
			
		}
	}
	/* TESTING BOUNDARIES
	double maxY = 0;
	double maxX = 0;
	double minX = 0;
	double minY = 0;
	
	minY = this.getSmallestYVal();
	minX = this.getSmallestXVal();
	maxY = this.getLargestYVal();
	maxX = this.getLargestXVal();
	System.out.println("reverseY()");
	System.out.println("MinX: " + minX);
	System.out.println("MaxX: " + maxX);
	System.out.println("MinY: " + minY);
	System.out.println("MaxY: " + maxY);
	System.out.println();
	*/
}


//and x coordinates right to at least 36
// gets rid of negatives or shifts lowest coordinates to 36,36
public void
ridNegatives()
throws Exception {
	double maxY = 0;
	double maxX = 0;
	double minY = 0;
	double minX = 0;
	
	minY = this.getSmallestYVal();
	minX = this.getSmallestXVal();
	maxY = this.getLargestYVal();
	maxX = this.getLargestXVal();
	
	if (minX < MIN_XCO) {
		double shiftX = MIN_XCO - minX;
		this.shiftAllX(shiftX);
	}
	else if (minX > MIN_XCO) {
		double shiftX = MIN_XCO - minX;
		this.shiftAllX(shiftX);
	}
	if (minY < MIN_YCO) {
		double shiftY = MIN_YCO - minY;
		this.shiftAllY(shiftY);
	}
	else if (minY > MIN_YCO) {
		double shiftY = MIN_YCO - minY;
		this.shiftAllY(shiftY);
	}
	
	/* TESTING BOUNDARIES
	minY = this.getSmallestYVal();
	minX = this.getSmallestXVal();
	maxY = this.getLargestYVal();
	maxX = this.getLargestXVal();
	System.out.println("ridNegatives()");
	System.out.println("MinX: " + minX);
	System.out.println("MaxX: " + maxX);
	System.out.println("MinY: " + minY);
	System.out.println("MaxY: " + maxY);
	System.out.println();
	*/
}


//Centers structure if within upper bounds
public void
centerEverything() 
throws Exception {
	
	double
		maxY = 0,
		maxX = 0,
		minX = 0,
		minY = 0;
	
	maxY = this.getLargestYVal();
	maxX = this.getLargestXVal();
	minY = this.getSmallestYVal();
	minX = this.getSmallestXVal();
	
	if (maxX < MAX_XCO) {
		double shiftX = (MAX_XCO-maxX)/2;
		shiftAllX(shiftX);
	}
	if (maxY < MAX_YCO) {
		double shiftY = (MAX_YCO-maxY)/2;
		shiftAllY(shiftY);
	}
	
	/* TESTING BOUNDARIES
	minY = this.getSmallestYVal();
	minX = this.getSmallestXVal();
	maxY = this.getLargestYVal();
	maxX = this.getLargestXVal();
	System.out.println("Center()");
	System.out.println("MinX: " + minX);
	System.out.println("MaxX: " + maxX);
	System.out.println("MinY: " + minY);
	System.out.println("MaxY: " + maxY);
	System.out.println();
	*/
}

//shift increase if pos number, shift decrease if negative
//shifts coordinates of nucs and labels
public void
shiftAllX(double shift) 
throws Exception{
	Vector nucList = this.getItemListDelineators();
	double newX = 0;
	double oldX = 0;
	
	SSData2D sstr = ((Nuc2D)((NucNode)nucList.elementAt(0))).getParentSSData2D();;
	int counter = 0;
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D nuc0 = (Nuc2D)((NucNode)nucList.elementAt(i));
		Nuc2D nuc1 = (Nuc2D)((NucNode)nucList.elementAt(i+1));

		for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++)
		{
			Nuc2D refNuc = sstr.getNuc2DAt(nucID);
			oldX = refNuc.getX();
			newX = refNuc.getX() + shift;
			
			//System.out.println("Old X: " + refNuc.getX());
			refNuc.setX(newX);
			//System.out.println("New X: " + refNuc.getX());
			if (refNuc.getLabelList() != null) {
				counter++;
				System.out.println("Count: " + counter + " Shifting X by: " + shift);
				System.out.println("OldX: " + oldX + " NewX: " + refNuc.getX());
				//refNuc.callAdjustLabelListCSV(1, shift, 0, 0);
			}
			
		}
		
	}
	counter = 0;
	
}

//shift increase if pos number, shift decrease if negative
public void
shiftAllY(double shift) 
throws Exception{
	Vector nucList = this.getItemListDelineators();
	double newY = 0;
	double oldY = 0;
	SSData2D sstr = ((Nuc2D)((NucNode)nucList.elementAt(0))).getParentSSData2D();;
	
	int counter = 0;
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D nuc0 = (Nuc2D)((NucNode)nucList.elementAt(i));
		Nuc2D nuc1 = (Nuc2D)((NucNode)nucList.elementAt(i+1));

		for (int nucID = nuc0.getID();nucID <= nuc1.getID();nucID++)
		{
			Nuc2D refNuc = sstr.getNuc2DAt(nucID);
			oldY = refNuc.getY();
			newY = refNuc.getY() + shift;
			//System.out.println("Old X: " + refNuc.getX());
			refNuc.setY(newY);
			//System.out.println("New X: " + refNuc.getX());
			if (refNuc.getLabelList() != null) {
				counter++;
				System.out.println("Count: " + counter + "Shifting Y by: " + shift);
				System.out.println("OldY: " + oldY + " NewY: " + refNuc.getY());
				//refNuc.callAdjustLabelListCSV(1, 0, shift, 0);
			}
			
		}
		
	}
	counter = 0;
}

public void
drawBackbone(Graphics2D g2)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
	g2.setRenderingHints(GraphicsUtil.lineUnAliasedRenderHints);

	g2.setStroke(new BasicStroke(0.0f,
		BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

	Line2D.Double line = new Line2D.Double();
	// Line2D.Double bpLine = new Line2D.Double();

	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D nuc0ID = (Nuc2D)((NucNode)delineators.elementAt(i));
		Nuc2D nuc1ID = (Nuc2D)((NucNode)delineators.elementAt(i+1));
		line = new Line2D.Double(nuc0ID.getX(), -nuc0ID.getY(), 0.0, 0.0);
		for (int nucID = nuc0ID.getID();nucID <= nuc1ID.getID();nucID++)
		{
			Nuc2D nuc = (Nuc2D)this.getNucAt(nucID);
			if (nuc == null)
				continue;
			if (!nuc.getIsFormatted())
				continue;
			if (nuc.getIsHidden())
				continue;
			if (nuc.getHideForConstrain())
				continue;
			line.setLine(nuc.getX(), -nuc.getY(),
				line.getX1(), line.getY1());
			g2.draw(line);
			//
			// if (nuc.isBasePair())
			// {
				// Nuc2D bpNuc = nuc.getBasePair2D();
				// bpLine.setLine(nuc.getX(), -nuc.getY(),
					// bpNuc.getX(), -bpNuc.getY());
				// g2.draw(bpLine);
			// }
			//
		}
	}
}

// for debug purposes only. currently doesn't shift sstr.labels
// this shifts individual nucs in sstr
// This method depends on nucs being formatted.
public void
centerAtOrigin()
throws Exception
{
	this.update(GraphicsUtil.unitG2);
	Rectangle2D rect = this.getBoundingBox();
	this.shiftXY(
		rect.getX() + rect.getWidth()/2.0,
		-rect.getY() - rect.getHeight()/2.0);
	/* This doesn't work at all:
	if (this.getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			debug("SHIFTTING: " + drawObject + " by: " +
				(rect.getX() + rect.getWidth()/2.0) + " " +
				(-rect.getY() - rect.getHeight()/2.0));
			drawObject.shiftXY(
				rect.getX() + rect.getWidth()/2.0,
				-rect.getY() - rect.getHeight()/2.0);
		}
	}	
	*/
}

public double
getLargestXVal()
throws Exception
{
	double largestX = -Double.MAX_VALUE;
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			if (!refNuc.isFormatted())
			{
				if (refNuc == endNuc)
					break;
				refNuc = refNuc.nextNuc2D();
				continue;
			}
			if (largestX < refNuc.getX())
				largestX = refNuc.getX();
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
	return (largestX);
}

public double
getSmallestXVal()
throws Exception
{
	double smallestX = Double.MAX_VALUE;
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			if (!refNuc.isFormatted())
			{
				if (refNuc == endNuc)
					break;
				refNuc = refNuc.nextNuc2D();
				continue;
			}
			if (smallestX > refNuc.getX())
				smallestX = refNuc.getX();
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
	return (smallestX);
}

public double
getLargestYVal()
throws Exception
{
	double largestY = -Double.MAX_VALUE;
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			if (!refNuc.isFormatted())
			{
				if (refNuc == endNuc)
					break;
				refNuc = refNuc.nextNuc2D();
				continue;
			}
			if (largestY < refNuc.getY())
				largestY = refNuc.getY();
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
	return (largestY);
}
public double 
getSmallestYVal()
throws Exception
{
	double smallestY = Double.MAX_VALUE;
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			if (!refNuc.isFormatted())
			{
				if (refNuc == endNuc)
					break;
				refNuc = refNuc.nextNuc2D();
				continue;
			}
			if (smallestY > refNuc.getY())
				smallestY = refNuc.getY();
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
	return (smallestY);
}

public static boolean
checkGeometry(NucCollection2D sstr0, NucCollection2D sstr1)
throws Exception
{
	if (sstr0.getNucCount() != sstr1.getNucCount())
		return (false);
	Vector nucList0 = sstr0.getItemListDelineators();
	Vector nucList1 = sstr1.getItemListDelineators();
	if (nucList0.size() != nucList1.size())
		return (false);
	for (int nucID = 1;nucID <= sstr0.getNucCount();nucID++)
	{
		Nuc2D refNuc0 = sstr0.getNuc2DAt(nucID);
		Nuc2D refNuc1 = sstr1.getNuc2DAt(nucID);
		if ((refNuc0 == null) && (refNuc1 != null))
			return (false);
		if ((refNuc0 != null) && (refNuc1 == null))
			return (false);
		if (refNuc0 == null)
			continue;
		for (int testID = 1;testID <= sstr0.getNucCount();testID++)
		{
			Nuc2D testNuc0 = sstr0.getNuc2DAt(testID);
			Nuc2D testNuc1 = sstr1.getNuc2DAt(testID);
			if (testNuc0 == null)
				continue;
			if (testNuc1 == null)
				continue;
			if (refNuc0.getPoint2D().distance(testNuc0.getPoint2D()) !=
				refNuc1.getPoint2D().distance(testNuc1.getPoint2D()))
				return (false);
		}
	}
	return (true);
}

public Nuc2D
getNuc2DAt(int nucID)
{
	return ((Nuc2D)getItemAt(nucID));
}

public void
runSetLocationHashtable(int level)
throws Exception
{
	// NOT sure have to do:
	super.runSetLocationHashtable(level);

	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			refNuc.runSetLocationHashtable(level);
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

public void
runSetBasePairHashtable(int level)
throws Exception
{
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			refNuc.runSetBasePairHashtable(level);
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

/***************** DrawObject Implementation ****************/

public void
translate(Point2D refPt, Point2D toPt)
throws Exception
{
	// debug("TRANSLATING: " + (refPt.getX() - toPt.getX()) + " " + (refPt.getY() - toPt.getY()));
	this.shiftXY(refPt.getX() - toPt.getX(), refPt.getY() - toPt.getY());
}

public void
shiftXY(double x, double y)
throws Exception
{
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			if (!refNuc.isFormatted())
			{
				if (refNuc == endNuc)
					break;
				refNuc = refNuc.nextNuc2D();
				continue;
			}
			refNuc.shiftXY(x, y);
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

public void
shiftX(double x)
throws Exception
{
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			refNuc.shiftX(x);
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

public void
shiftY(double y)
throws Exception
{
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			refNuc.shiftY(y);
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

public void
setColor(Color color)
throws Exception
{
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			refNuc.setColor(color);
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

public void
update(Graphics2D g2)
throws Exception
{
	Vector delineators = this.getItemListDelineators();

	if (delineators == null)
		return;

	BRectangle2D rect = null;

	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);

		// TEST only; take out when convinced
		if (refNuc.getParentCollection() != endNuc.getParentCollection())
			throw new Exception("ERROR in NucCollection2D.update(): different parents");

		while (true)
		{
			if (!refNuc.getIsFormatted())
			{
				if (refNuc.equals(endNuc))
					break;
				refNuc = refNuc.nextNuc2D();
				continue;
			}

			// added 04/23/03 ; not sure of repercussions yet
			if (refNuc.getIsHidden())
			{
				if (refNuc.equals(endNuc))
					break;
				refNuc = refNuc.nextNuc2D();
				continue;
			}
			// added 05/3/03 ; not sure of repercussions yet
			if (refNuc.getHideForConstrain())
			{
				if (refNuc.equals(endNuc))
					break;
				refNuc = refNuc.nextNuc2D();
				continue;
			}

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
			// debug("dealt with: " + refNuc + " " + refNuc.getParentSSData().getName());
			
			if (refNuc.equals(endNuc))
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
	
	if (this.getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			drawObject.update(g2);	
			if (rect == null)
			{
				rect = new BRectangle2D();
				rect.setRect(drawObject.getBoundingBox());
			}
			else
			{
				rect.add(drawObject.getBoundingBox());
			}
			// rect.add(drawObject.getBoundingBox());
		}
	}

	if (rect == null)
	{
		this.setBoundingBox(null);
		this.setBoundingShape(null);
		return;
	}

	BRectangle2D newRect = new BRectangle2D(
		rect.getX() + this.getX(),
		rect.getY() - this.getY(),
		rect.getWidth(),
		rect.getHeight());

	this.setBoundingBox(newRect);
	this.setBoundingShape(this.getBoundingBox());

	// THIS IS NEW (12/15/02) and not sure of repercussions
	// (added for printing out centered partial strucutures)
	this.setDeltaX(newRect.getX() + newRect.getWidth()/2.0);
	this.setDeltaY(newRect.getY() + newRect.getHeight()/2.0);

	/* this messes up:
	this.setX(this.getDeltaX()/2.0);
	this.setY(-this.getDeltaY()/2.0);
	*/

	// debug("IN NUCCOLLECTION, Delta X,Y: " + this.getDeltaX() + " " + this.getDeltaY());
}

private RNABasePair2D refDrawBasePair = null;

// currently making the distinction between SSData as the other types are
// for now editing only.
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
	}

	if (refDrawBasePair == null)
		refDrawBasePair = new RNABasePair2D();

	g2.translate(this.getX(), -this.getY());

	if (this instanceof SSData2D)
	{
		this.setG2Transform(g2.getTransform());

		// Draw nuc path
		Vector nucPathDelineators = this.getNucPathDelineators();
		if (nucPathDelineators != null)
		{
			g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
			for (int i = 0;i < nucPathDelineators.size();i+=2)
			{
				Nuc2D nuc0 = (Nuc2D)nucPathDelineators.elementAt(i);
				Nuc2D nuc1 = (Nuc2D)nucPathDelineators.elementAt(i+1);

				this.drawNucPathSegment(nuc0, nuc1, g2);
			}	
		}

		// this is probably need to counter effect of drawing
		// schematics below and nuc path above
		g2.setRenderingHints(GraphicsUtil.stringAliasedRenderHints);
	}

	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);
		while (true)
		{
			/* THIS Doesn't work yet; temporarily putting old way
			 * back in so can get on with creating images. this way
			 * seems to overwrite bps.
			boolean fivePrimeBPSideFound = false;
			if (refNuc.isSingleStranded())
			{
				refNuc.draw(g2, constrainedArea);
			}
			else if (refNuc.isFivePrimeBasePair())
			{
				fivePrimeBPSideFound = true;
				refDrawBasePair.set(refNuc);
				refDrawBasePair.draw(g2, constrainedArea);
			}
			// hopefully this takes care of problem of list nucs
			// using a 3p nuc of a basepair but not its 5p nuc
			else if (refNuc.isThreePrimeBasePair() && !fivePrimeBPSideFound)
			{
				refDrawBasePair.set(refNuc.getBasePair2D());
				refDrawBasePair.draw(g2, constrainedArea);
			}

			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
			*/

			if (refNuc.isSingleStranded())
			{
				refNuc.draw(g2, constrainedArea);
			}
			else if (refNuc.isFivePrimeBasePair())
			{
				refDrawBasePair.set(refNuc);
				refDrawBasePair.draw(g2, constrainedArea);
			}

			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}

	if (this instanceof SSData2D)
	{
		this.drawLabelList(g2, constrainedArea);
		
		// now draw schematics

		g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
		Vector schemDelineators = this.getSchematicNucDelineators();

		for (int i = 0;i < schemDelineators.size();i+=2)
		{
			Nuc2D nuc0 = (Nuc2D)schemDelineators.elementAt(i);
			Nuc2D nuc1 = (Nuc2D)schemDelineators.elementAt(i+1);

			this.drawSchemSegment(nuc0, nuc1, g2);
		}	
	}

	g2.translate(-this.getX(), this.getY());

	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.green);
		this.drawBoundingShape(g2);
	}
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	if (this.getIsHidden())
		return;

	if (refDrawBasePair == null)
		refDrawBasePair = new RNABasePair2D();

	psUtil.printPSGSave();

	/*
	if (this instanceof SSData2D)
	{
		this.setG2Transform(g2.getTransform());

		// Draw nuc path
		Vector nucPathDelineators = this.getNucPathDelineators();
		if (nucPathDelineators != null)
		{
			g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
			for (int i = 0;i < nucPathDelineators.size();i+=2)
			{
				Nuc2D nuc0 = (Nuc2D)nucPathDelineators.elementAt(i);
				Nuc2D nuc1 = (Nuc2D)nucPathDelineators.elementAt(i+1);

				this.drawNucPathSegment(nuc0, nuc1, g2);
			}	
		}

		// this is probably need to counter effect of drawing
		// schematics below and nuc path above
		g2.setRenderingHints(GraphicsUtil.stringAliasedRenderHints);
	}
	*/

	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);
		while (true)
		{
			if (refNuc.isSingleStranded())
			{
				refNuc.printPS(g2, psUtil);
			}
			else if (refNuc.isFivePrimeBasePair())
			{
				refDrawBasePair.set(refNuc);
				refDrawBasePair.printPS(g2, psUtil);
			}

			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}

	if (this instanceof SSData2D)
	{
		this.printPSLabelList(g2, psUtil);
		
		/*
		// now draw schematics

		g2.setRenderingHints(GraphicsUtil.lineAliasedRenderHints);
		Vector schemDelineators = this.getSchematicNucDelineators();

		for (int i = 0;i < schemDelineators.size();i+=2)
		{
			Nuc2D nuc0 = (Nuc2D)schemDelineators.elementAt(i);
			Nuc2D nuc1 = (Nuc2D)schemDelineators.elementAt(i+1);

			this.drawSchemSegment(nuc0, nuc1, g2);
		}	
		*/
	}

	psUtil.printPSGRestore();
}

public void
erase(Graphics2D g2)
throws Exception
{
	if (refDrawBasePair == null)
		refDrawBasePair = new RNABasePair2D();
	Nuc2D refNuc = null;
	Nuc2D endNuc = null;
	g2.setColor(g2.getBackground());

	Vector delineators = this.getItemListDelineators();

	if (this instanceof RNABasePair2D)
	{
		refNuc = (Nuc2D)delineators.elementAt(0);
		refNuc.erase(g2);
		refDrawBasePair.set(refNuc);
		refDrawBasePair.eraseBPSymbol(g2);

		// special delineate case for basepairs:
		refNuc = (Nuc2D)delineators.elementAt(2);

		refNuc.erase(g2);
		return;
	}

	for (int i = 0;i < delineators.size();i+=2)
	{
		refNuc = (Nuc2D)delineators.elementAt(i);
		endNuc = (Nuc2D)delineators.elementAt(i+1);
		while (true)
		{
			if (refNuc.isSingleStranded())
			{
				refNuc.erase(g2);
			}
			else if (refNuc.isFivePrimeBasePair())
			{
				refDrawBasePair.set(refNuc);
				refDrawBasePair.erase(g2);
			}

			if (refNuc.equals(endNuc))
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

// delete drawObject associated with nuc only, not labels. Labels
// should be associated with parents right before deletion.
public void
delete(Graphics2D g2)
throws Exception
{
	RNABasePair2D basePair = null;

	this.erase(g2);

	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);
		while (true)
		{
			if (refNuc.isFivePrimeBasePair())
			{
				if (basePair == null)
					basePair = new RNABasePair2D();
				basePair.set(refNuc);
				basePair.delete(g2);
				continue;
			}
			if (refNuc.isBasePair()) // then it is a three prime side bp
			{
				refNuc = refNuc.nextNuc2D();
				continue;
			}
			refNuc.delete(g2);
			if (refNuc.equals(endNuc))
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
}

/***************** End DrawObject Implementation ****************/

public Vector
getItemListDelineators()
throws Exception
{
	return (null);
}

/*
public Nuc2D
BAD_findNuc(double xPos, double yPos)
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		int startNucID = ((Nuc2D)delineators.elementAt(i)).getID();
		int endNucID = ((Nuc2D)delineators.elementAt(i+1)).getID();
		for (int nucID = startNucID;nucID <= endNucID;nucID++)
		{
			Nuc2D nuc = this.getNuc2DAt(nucID);
			debug("HERE: " + nuc.getID());
			if (nuc.isHidden())
				continue;

			if (nuc.findNuc(xPos - this.getX(), yPos + this.getY())
				!= null) // bottomed out at a leaf node
				return (nuc);
		}
	}

	return (null);
}
*/

public String
toString()
{
	return (this.getName());
}

private static void
debug(String s)
{
	System.err.println("NucCollection2D-> " + s);
}

}
