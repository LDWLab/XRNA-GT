package ssview;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import jimage.DrawObjectCollection;
import jimage.DrawObject;

import util.math.*;

public class
RNAColorUnit2D
extends RNAColorUnit
{

public
RNAColorUnit2D()
throws Exception
{
	super();
}

public
RNAColorUnit2D(ComplexScene complexScene, Color unitColor)
throws Exception
{
	this.set(complexScene, unitColor);
	this.setParentCollection(complexScene);
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

// currently this should be same as RNANamedGroup; looking into consolidation
// of all NucCollection update()'s

private BRectangle2D nucRect = null;

public void
update(Graphics2D g2)
throws Exception
{
	Vector delineators = this.getItemListDelineators();

	if (delineators == null)
		return;

	BRectangle2D rect = null;
	if (nucRect == null)
		nucRect = new BRectangle2D();

	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);

		// TEST only; take out when convinced
		/*
		if (refNuc.getParentCollection() != endNuc.getParentCollection())
			throw new Exception("ERROR in RNAColorUnit2D.update(): different parents");
		*/

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

			nucRect.setRect(refNuc.getBoundingBox());

			DrawObject drwObj = (DrawObject)refNuc.getParentCollection();
			while (true)
			{
				if (drwObj instanceof ComplexScene2D)
					break;
				nucRect.translate(drwObj.getX(), -drwObj.getY());
				drwObj = (DrawObject)drwObj.getParentCollection();
			}

			if (rect == null)
			{
				rect = new BRectangle2D();
				rect.setRect(nucRect);
			}
			else
			{
				rect.add(nucRect);
			}
			
			if (refNuc.equals(endNuc))
				break;
			refNuc = refNuc.nextNuc2D();
		}
	}
	
	// NOT DONE YET: (not sure about color unit having its own labels)
	if (this.getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			drawObject.update(g2);	
			rect.add(drawObject.getBoundingBox());
		}
	}

	if (rect == null)
	{
		this.setBoundingBox(null);
		this.setBoundingShape(null);
		return;
	}

	this.setBoundingBox(rect);
	this.setBoundingShape(this.getBoundingBox());

	// THIS IS NEW (12/15/02) and not sure of repercussions
	// (added for printing out centered partial strucutures)
	this.setDeltaX(rect.getX() + rect.getWidth()/2.0);
	this.setDeltaY(rect.getY() + rect.getHeight()/2.0);
}

private RNABasePair2D refDrawBasePair = null;

// currently this should be same as RNANamedGroup; looking into consolidation
// of all NucCollection draw()'s
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

	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);

		// TEST only; take out when convinced
		/*
		if (refNuc.getParentCollection() != endNuc.getParentCollection())
			throw new Exception("ERROR in RNAColorUnit2D.update(): different parents");
		*/
		g2.setTransform(refNuc.getParentG2Transform());

		while (true)
		{
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

	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.green);
		this.drawBoundingShape(g2);
	}
}

private static void
debug(String s)
{
	System.err.println("RNAColorUnit2D-> " + s);
}
	
}
