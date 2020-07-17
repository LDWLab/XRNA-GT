package ssview;

import java.awt.geom.*;
import java.util.*;

import util.math.RayIntersectDrawObject;
import util.math.BLine2D;

public class
RayIntersectNucNode
extends RayIntersectDrawObject
{

public
RayIntersectNucNode()
{
	super();
	this.setSideList(new Vector());
}

public
RayIntersectNucNode(Nuc2D nuc)
{
	super(nuc);
	this.setSideList(new Vector());
}

public
RayIntersectNucNode(BLine2D ray)
{
	super(ray);
	this.setSideList(new Vector());
}

public
RayIntersectNucNode(BLine2D ray, Nuc2D nuc)
{
	super(ray, nuc);
	this.setSideList(new Vector());
}

private Vector sideList = null;

public void
setSideList(Vector sideList)
{
    this.sideList = sideList;
}

public Vector
getSideList()
{
    return (this.sideList);
}

public Nuc2D
getNuc()
{
	return ((Nuc2D)this.getDrawObject());
}

public void
runRayDrawObjectIntersect()
throws Exception
{
	getIntersectPtList().clear();
	getUList().clear();
	getSideList().clear();
	Nuc2D nuc = this.getNuc();
	Rectangle2D rect = nuc.getNucSymbolBoundingBox();
	rect.setRect(rect.getX(), -rect.getY(), rect.getWidth(), rect.getHeight());
	Point2D intersect0Pt = new Point2D.Double();
	Point2D intersect1Pt = new Point2D.Double();
	double uArray[] = new double[2];
	int sideArray[] = new int[2];

	// debug("IN RAYINTERSECNUCNOD, nuc: " + nuc);
	// debug("IN RAYINTERSECNUCNOD, rect: " + rect);
	// debug("IN RAYINTERSECNUCNOD, ray: " + this.getRay());

	this.setRayIntersects(BLine2D.getRectangleRayIntersect(
		rect, this.getRay(), intersect0Pt, intersect1Pt, uArray,
			sideArray));

	// debug("IN RAYINTERSECNUCNOD, intersect0Pt: " + intersect0Pt);
	// debug("IN RAYINTERSECNUCNOD, intersect1Pt: " + intersect1Pt);

	if (!this.getRayIntersects())
		return;

	if ((uArray[0] >= 0.0) && (uArray[1] >= 0.0))
	{
		if (uArray[0] < uArray[1])
		{
			getIntersectPtList().add(0, intersect0Pt);
			getUList().add(0, new Double(uArray[0]));
			getSideList().add(0, new Integer(sideArray[0]));
			getIntersectPtList().add(1, intersect1Pt);
			getUList().add(1, new Double(uArray[1]));
			getSideList().add(1, new Integer(sideArray[1]));
		}
		else
		{
			getIntersectPtList().add(0, intersect1Pt);
			getUList().add(0, new Double(uArray[1]));
			getSideList().add(0, new Integer(sideArray[1]));
			getIntersectPtList().add(1, intersect0Pt);
			getUList().add(1, new Double(uArray[0]));
			getSideList().add(1, new Integer(sideArray[0]));
		}
	}
	else if (uArray[0] >= 0.0)
	{
		getIntersectPtList().add(0, intersect0Pt);
		getUList().add(0, new Double(uArray[0]));
		getSideList().add(0, new Integer(sideArray[0]));
	}
	else if (uArray[1] >= 0.0)
	{
		getIntersectPtList().add(0, intersect1Pt);
		getUList().add(0, new Double(uArray[1]));
		getSideList().add(0, new Integer(sideArray[1]));
	}
	
	// debug("u's: " + uArray[0] + " " + uArray[1]);
	// debug("pt's: " + intersect0Pt + " " + intersect1Pt);
}

public String
toString()
{
	StringBuffer strBuf = new StringBuffer();
	strBuf.append("For ray/NucNode intersect:\n");
	strBuf.append("ray: " + this.getRay() + "\n");
	strBuf.append("nuc: " + this.getNuc() + "\n");

	strBuf.append("U's:\n");
	if (getUList().size() <= 0)
		strBuf.append("\tNADA\n");
	else
		for (int i = 0;i < getUList().size();i++)
			strBuf.append((Double)getUList().elementAt(i) + "\n");
	strBuf.append("Pt's:\n");
	if (getIntersectPtList().size() <= 0)
		strBuf.append("\tNADA\n");
	else
		for (int i = 0;i < getIntersectPtList().size();i++)
			strBuf.append((Point2D)getIntersectPtList().elementAt(i) + "\n");
	strBuf.append("Sides's:\n");
	if (getSideList().size() <= 0)
		strBuf.append("\tNADA\n");
	else
		for (int i = 0;i < getSideList().size();i++)
			strBuf.append(((Integer)getSideList().elementAt(i)).intValue() + "\n");
	return (strBuf.toString());
}

private static void
debug(String s)
{
	System.err.println("RayIntersectNucNode-> " + s);
}

}
