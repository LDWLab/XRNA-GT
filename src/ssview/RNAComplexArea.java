package ssview;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

import util.math.*;

public class
RNAComplexArea
extends NucCollection2D
{

public
RNAComplexArea()
throws Exception
{
	super();
}

// ordinarily the following would be in RNAComplexArea2D, but need here
// as this whole helper class implies 2D and currently getItemListDelineator
// is in here.

private Point2D upperLeftPt = null;

public void
setUpperLeftPt(Point2D upperLeftPt)
{
    this.upperLeftPt = upperLeftPt;
}

public Point2D
getUpperLeftPt()
{
    return (this.upperLeftPt);
}

private Point2D lowerRightPt = null;

public void
setLowerRightPt(Point2D lowerRightPt)
{
    this.lowerRightPt = lowerRightPt;
}

public Point2D
getLowerRightPt()
{
    return (this.lowerRightPt);
}

private BRectangle2D complexAreaRect = null;

public void
setComplexAreaRect(BRectangle2D complexAreaRect)
{
    this.complexAreaRect = complexAreaRect;
}

public BRectangle2D
getComplexAreaRect()
{
    return (this.complexAreaRect);
}

// used to get a delineated list of non null nucs
public Vector
getItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();

	// doesn't work yet
	/*
	Nuc2D nuc = null;
	boolean inNucs = false;

	for (int objID = 0;objID < this.getComplexScene2D().getItemCount();objID++)
	{
		Object obj =
			this.getComplexScene2D().getItemAt(objID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexSSDataCollection2D)
		{
			ComplexSSDataCollection2D collection =
				(ComplexSSDataCollection2D)obj;
			for (int obj1ID = 0;obj1ID < collection.getItemCount();obj1ID++)
			{
				Object obj1 = collection.getItemAt(obj1ID);
				if (obj1 instanceof SSData2D)
				{
					SSData2D sstr = (SSData2D)obj1;
					for (int nucID = 1;nucID <= sstr.getNucCount(); nucID++)
					{
						Nuc2D nuc = sstr.getNuc2DAt(nucID);
						if (nuc == null)
							continue;

	AffineTransform affTrans = nuc.getG2Transform();
	BRectangle2D nucRect = new BRectangle2D(
		affTrans.getTranslateX() - this.getDeltaX(),
		affTrans.getTranslateY() + this.getDeltaY(),
		nuc.getBoundingBox().getWidth(),
		nuc.getBoundingBox().getHeight());

						// if (!getComplexAreaRect().contains(nuc.getBoundingBox()))
						if (!getComplexAreaRect().contains(nucRect))
							continue;
						if (nuc.isFivePrimeBasePair())
						{
							if (basePair == null)
								basePair = new RNABasePair2D();
							basePair.set(nuc);
							// NEED to add to list Here
							// basePair.erase(g2);
							continue;
						}
						if (nuc.isBasePair()) // then it is a three prime side bp
							continue;
						// NEED to add to list Here
						// nuc.erase(g2);
					}
				}
			}
		}
	}
	*/

	/* NEED to account for any null nucs within strand
	for (NucNode nuc = this.getFivePrimeNuc();
		(nuc != null) && (nuc.getID() <= this.getThreePrimeNuc().getID());
			nuc = nuc.nextNuc())
	{
		if (nuc.getID() == this.getThreePrimeNuc().getID())
		{
			// finish up and return;
			delineators.add(nuc);
			delineators.add(getNucAt(nucID - 1));
			break;
		}

		if (getNucAt(nucID) == null)
		{
			if (inNucs)
			{
				delineators.add(nuc);
				delineators.add(getNucAt(nucID - 1));
				nuc = null;
				inNucs = false;
			}
		}
		else
		{	
			if (!inNucs)
			{
				inNucs = true;
				nuc = getNucAt(nucID);
			}
		}
	}
	*/

	return(delineators);
}

public String
toString()
{
	return("RNA ComplexArea in: " + getParentCollection().toString());
}

private void
debug(String s)
{
	System.err.println("RNAComplexArea-> " + s);
}

}
