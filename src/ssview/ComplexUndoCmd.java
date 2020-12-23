package ssview;

import java.io.*;
import java.util.*;
import java.awt.geom.Point2D;

import jimage.DrawFontObject;

import util.*;

public class
ComplexUndoCmd
{

public
ComplexUndoCmd()
{
}

public
ComplexUndoCmd(NucCollection2D nucCollection, int constraintType, int undoLevel, int undoType)
throws Exception
{
	// this.setRefNuc(refNuc);
	this.setConstraintType(constraintType);
	this.setUndoLevel(undoLevel);
	this.setUndoType(undoType);
	this.initUndoCmd();
}

public
ComplexUndoCmd(Nuc2D refNuc, int constraintType, int undoLevel, int undoType)
throws Exception
{
	this.setRefNuc(refNuc);
	this.setConstraintType(constraintType);
	this.setUndoLevel(undoLevel);
	this.setUndoType(undoType);
	this.initUndoCmd();
}

public
ComplexUndoCmd(DrawFontObject refLabel, int constraintType, int undoLevel, int undoType)
throws Exception
{
	this.setRefLabel(refLabel);
	this.setConstraintType(constraintType);
	this.setUndoLevel(undoLevel);
	this.setUndoType(undoType);
	this.initUndoCmd();
}


private void
initUndoCmd()
throws Exception
{
	switch (this.getUndoType())
	{
	  case ComplexDefines.UNDO_TYPE_POSITION :
		switch (this.getConstraintType())
		{
		  case ComplexDefines.InRNASingleNuc :
			this.setSvePt(new Point2D.Double(
				this.getRefNuc().getPoint2D().getX(),
				this.getRefNuc().getPoint2D().getY()));
			break;
		  case ComplexDefines.InRNASingleStrand :
			// NEED to have a list of pts for single strand as
			// could change radius.
			break;
		  case ComplexDefines.InRNABasePair :
			break;
		  case ComplexDefines.InRNAHelix :
			break;
		  case ComplexDefines.InRNAHelicalRun :
			break;
		  case ComplexDefines.InRNASubDomain :
			break;
		  case ComplexDefines.InRNANamedGroup :
			break;
		  case ComplexDefines.InRNACycle :
			break;
		  case ComplexDefines.InRNAColorUnit :
			break;
		  case ComplexDefines.InRNAListNucs :
			break;
		  case ComplexDefines.InRNASSData :
			break;
		  case ComplexDefines.InComplex :
			break;
		  case ComplexDefines.InLabelsOnly :
			this.setSvePt(new Point2D.Double(
				refLabel.getX(), refLabel.getY()));
			break;
		  case ComplexDefines.InComplexScene :
			break;
		  default :
			break;
		}
	  break;
	}
}

public void
runUndo()
throws Exception
{
	switch (this.getUndoType())
	{
	  case ComplexDefines.UNDO_TYPE_POSITION :
		switch (this.getConstraintType())
		{
		  case ComplexDefines.InRNASingleNuc :
			this.getRefNuc().setXY(this.getSvePt().getX(), this.getSvePt().getY());
			break;
		  case ComplexDefines.InRNASingleStrand :
			break;
		  case ComplexDefines.InRNABasePair :
			break;
		  case ComplexDefines.InRNAHelix :
			break;
		  case ComplexDefines.InRNAHelicalRun :
			break;
		  case ComplexDefines.InRNASubDomain :
			break;
		  case ComplexDefines.InRNANamedGroup :
			break;
		  case ComplexDefines.InRNACycle :
			break;
		  case ComplexDefines.InRNAColorUnit :
			break;
		  case ComplexDefines.InRNAListNucs :
			break;
		  case ComplexDefines.InRNASSData :
			break;
		  case ComplexDefines.InComplex :
			break;
		  case ComplexDefines.InLabelsOnly :
			this.getRefLabel().setXY(this.getSvePt().getX(), this.getSvePt().getY());
			break;
		  case ComplexDefines.InComplexScene :
			break;
		  default :
			break;
		}
	  break;
	}
}

private Point2D svePt = null;

public void
setSvePt(Point2D svePt)
{
    this.svePt = svePt;
}

public Point2D
getSvePt()
{
    return (this.svePt);
}

private Nuc2D refNuc = null;

public void
setRefNuc(Nuc2D refNuc)
{
    this.refNuc = refNuc;
}

public Nuc2D
getRefNuc()
{
    return (this.refNuc);
}

private DrawFontObject refLabel = null;

public void
setRefLabel(DrawFontObject refLabel)
{
    this.refLabel = refLabel;
}

public DrawFontObject
getRefLabel()
{
    return (this.refLabel);
}

private int constraintType = 0;

public void
setConstraintType(int constraintType)
{
    this.constraintType = constraintType;
}

public int
getConstraintType()
{
    return (this.constraintType);
}

private int undoLevel = 0;

public void
setUndoLevel(int undoLevel)
{
    this.undoLevel = undoLevel;
}

public int
getUndoLevel()
{
    return (this.undoLevel);
}

private int undoType = ComplexDefines.UNDO_TYPE_NULL;

public void
setUndoType(int undoType)
{
    this.undoType = undoType;
}

public int
getUndoType()
{
    return (this.undoType);
}

public String
toString()
{
	return ("" + this.getUndoType());
}

public static void
debug(String s)
{
	System.err.println("ComplexUndoCmd-> " + s);
}

class SaveNucInfo
{

public SaveNucInfo()
{
}

}

}
