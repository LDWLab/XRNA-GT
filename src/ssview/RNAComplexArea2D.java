package ssview;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import jimage.DrawObject;
import jimage.DrawObjectCollection;
import jimage.DrawLineObject;
import util.math.*;

// rna single strand should have only one parent and each nuc joined
// to it's n-1, n, n+1 neighbor; unlike basepairs which can have
// two parents for the base pair interaction

public class
RNAComplexArea2D
extends RNAComplexArea
{

// parent container should be of type SSData since single strand
// can

public
RNAComplexArea2D()
throws Exception
{
	super();
}

public
RNAComplexArea2D(ComplexScene2D complexScene, Point2D pt0, Point2D pt1)
throws Exception
{
	super();
	this.setComplexScene2D(complexScene);
	if ((pt0.getX() < pt1.getX()) && (pt0.getY() < pt1.getY()))
	{
		this.setUpperLeftPt(pt0);
		this.setLowerRightPt(pt1);
	}
	else
	{
		this.setUpperLeftPt(pt1);
		this.setLowerRightPt(pt0);
	}
	BRectangle2D rect = new BRectangle2D(
		this.getUpperLeftPt().getX(),
		this.getUpperLeftPt().getY(),
		this.getLowerRightPt().getX() - this.getUpperLeftPt().getX(),
		-(this.getUpperLeftPt().getY() - this.getLowerRightPt().getY()));	
	this.setComplexAreaRect(rect);

	/*
	debug("UPPERLFT: " + this.getUpperLeftPt());
	debug("LOWERRHT: " + this.getLowerRightPt());
	debug("rect: " + this.getComplexAreaRect());
	*/
}

private ComplexScene2D complexScene = null;

public void
setComplexScene2D(ComplexScene2D complexScene)
{
    this.complexScene = complexScene;
}

public ComplexScene2D
getComplexScene2D()
{
    return (this.complexScene);
}

public void
setIsSchematic(boolean isSchematic)
{
	/*
	for (Nuc2D nuc = this.getFivePrimeNuc2D();
		(nuc != null) && (nuc.getID() <= this.getThreePrimeNuc().getID());
			nuc = nuc.nextNuc2D())
		nuc.setIsSchematic(isSchematic);
	*/
}

/*************** DrawObject Implementation *******************/

// THIS SHOULD PROBABLY HAPPEN IN NucCollection2D
public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
}

// delete drawObject associated with nuc only, not labels. Labels
// should be associated with parents right before deletion.
public void
delete(Graphics2D g2)
throws Exception
{
	this.erase(g2);

	RNABasePair2D basePair = null;
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
						// debug("CHECKING NUC.BB: " + nuc.getID() + " " + nuc.getBoundingBox());


	/*
	BRectangle2D tmpNucRect = (BRectangle2D)nuc.getBoundingBox().clone();
debug("COMPLEXSCENE.getX: " + this.getComplexScene2D().getX());
debug("COMPLEXSCENE.getY: " + this.getComplexScene2D().getY());
	tmpNucRect.setRect(
		tmpNucRect.getX() + this.getComplexScene2D().getX(),
		tmpNucRect.getY() - this.getComplexScene2D().getY(),
		tmpNucRect.getWidth(),
		tmpNucRect.getHeight());
	// debug("tmpNucRect: " + tmpNucRect);

						if (!this.getComplexAreaRect().contains(tmpNucRect))
	*/

	// AffineTransform affTrans = ((SSData2D)nuc.getParentCollection()).getG2Transform();
	// AffineTransform affTrans = complexScene.getG2Transform();
	/*
	debug("affTrans transX: " + affTrans.getTranslateX());
	debug("affTrans transY: " + affTrans.getTranslateY());
	*/
	AffineTransform affTrans = nuc.getG2Transform();
	BRectangle2D nucRect = new BRectangle2D(
		affTrans.getTranslateX() - this.getDeltaX(),
		affTrans.getTranslateY() + this.getDeltaY(),
		nuc.getBoundingBox().getWidth(),
		nuc.getBoundingBox().getHeight());

						//if (!this.getComplexAreaRect().contains(nuc.getBoundingBox()))
						if (!this.getComplexAreaRect().contains(nucRect))
							continue;
						// debug("NUC IS CONTAINED: " + nuc);
						if (nuc.isFivePrimeBasePair())
						{
							if (basePair == null)
								basePair = new RNABasePair2D();
							basePair.set(nuc);
							basePair.delete(g2);
							continue;
						}
						if (nuc.isBasePair()) // then it is a three prime side bp
							continue;
						nuc.delete(g2);
					}
				}
			}
		}
	}
}

private void
debug(String s)
{
	System.err.println("RNAComplexArea2D-> " + s);
}

}
