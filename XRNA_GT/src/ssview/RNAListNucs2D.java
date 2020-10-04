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
RNAListNucs2D
extends RNAListNucs
{

public
RNAListNucs2D()
throws Exception
{
	super();
}

public
RNAListNucs2D(NucNode nuc)
throws Exception
{
	this.set((Nuc2D)nuc);
}

public
RNAListNucs2D(NucNode nuc0, NucNode nuc1)
throws Exception
{
	this.set((Nuc2D)nuc0, (Nuc2D)nuc1);
}

public
RNAListNucs2D(SSData sstr, int nucID0, int nucID1)
throws Exception
{
	this.set(((SSData2D)sstr).getNuc2DAt(nucID0),
		((SSData2D)sstr).getNuc2DAt(nucID1));
}

public void
set(Nuc2D nuc)
throws Exception
{
	super.set((NucNode)nuc);
}

public void
set(Nuc2D nuc0, Nuc2D nuc1)
throws Exception
{
	super.set((NucNode)nuc0, (NucNode)nuc1);
}

// parent will always be single rna strand
public SSData2D
getParentSSData2D()
{
	return ((SSData2D)this.getParentCollection());
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

public void
setIsSchematic(boolean isSchematic)
{
	for (Nuc2D nuc = this.getFivePrimeNuc2D();
		(nuc != null) && (nuc.getID() <= this.getThreePrimeNuc().getID());
			nuc = nuc.nextNuc2D())
		nuc.setIsSchematic(isSchematic);
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

public double
getX()
throws Exception
{
	return ((new BLine2D(this.getFivePrimeNuc2D().getPoint2D(),
		this.getThreePrimeNuc2D().getPoint2D())).getMidPt().getX());	
}

public double
getY()
throws Exception
{
	return ((new BLine2D(this.getFivePrimeNuc2D().getPoint2D(),
		this.getThreePrimeNuc2D().getPoint2D())).getMidPt().getY());	
}

private void
debug(String s)
{
	System.err.println("RNAListNucs2D-> " + s);
}

}
