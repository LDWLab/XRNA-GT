package ssview;

import java.util.*;
import java.io.*;

import jimage.DrawObjectCollection;

/**
** Collection for nuc drawObjects and any labels that may go with it.
*/

public class
NucNode
extends ComplexCollection
{

public
NucNode()
throws Exception
{
}

public
NucNode(char nucChar)
throws Exception
{
	this();
	this.setNucChar(nucChar);
}

public
NucNode(int id)
throws Exception
{
	this();
	this.setID(id);
}

public
NucNode(char nucChar, int id)
throws Exception
{
	this();
	this.setNucChar(nucChar);
	this.setID(id);
}

public
NucNode(SSData ssData)
throws Exception
{
	this();
	this.setParentCollection(ssData);
}

public
NucNode(char nucChar, SSData ssData)
throws Exception
{
	this(ssData);
	this.setNucChar(nucChar);
}

public
NucNode(char nucChar, int id, SSData ssData)
throws Exception
{
	this(ssData);
	this.setNucChar(nucChar);
	this.setID(id);
}

// copy of parameter nucnode
public
NucNode(NucNode nuc)
throws Exception
{
	this.setParentCollection(nuc.getParentCollection());
	this.setNucChar(nuc.getNucChar());
	this.setID(nuc.getID());
	this.setBasePairSStrName(nuc.getBasePairSStrName());
	this.setBasePairID(nuc.getBasePairID());
	this.setBasePair(nuc.getBasePair());
}

// items ordinal number in strand numbered 1->maxitemcount
// this is different than position in list. Used for nucs, etc.
int id = 0;

public void
setID(int id)
throws Exception
{
	if (id <= 0)
		throw new Exception("Exception in NucNode.setID():\n\t" +
			"'" + id + "' not a valid nuc id\n");
	this.id = id;
}

public int
getID()
{
	return (this.id);
}

public void
delete()
{
	this.unsetBasePair();
	this.getParentSSData().setItemAt(null, this.getID());
}

public void
unsetBasePair()
{
	if (this.isBasePair())
	{
		NucNode bp = this.getBasePair();
		bp.setBasePairSStrName(null);
		bp.setBasePairID(0);
		bp.setBasePair(null);
	}
	this.setBasePairSStrName(null);
	this.setBasePairID(0);
	this.setBasePair(null);
}

// set a non-natural delineator:
private boolean isSingleStrandDelineator = false;

public void
setIsSingleStrandDelineator(boolean isSingleStrandDelineator)
{
    this.isSingleStrandDelineator = isSingleStrandDelineator;
}

public boolean
getIsSingleStrandDelineator()
{
    return (this.isSingleStrandDelineator);
}

public boolean
isNaturalSingleStrandDelineator()
{
	return (isBasePair() || (this.getID() == 1) ||
		(this.getID() == this.getParentSSData().getNucCount()) ||
		(this.lastNuc() == null) || (this.nextNuc() == null));
}

public boolean
isSingleStrandDelineator()
{
	return (this.isNaturalSingleStrandDelineator() ||
		this.getIsSingleStrandDelineator());
}

public String
getParentName()
{
	return (this.getParentSSData().getName());
}

public int
getParentNucCount()
{
	return (this.getParentNucCollection2D().getNucCount());
}

public NucCollection2D
getParentNucCollection2D()
{
	return((NucCollection2D)this.getParentCollection());
}

public SSData
getParentSSData()
{
	return((SSData)this.getParentNucCollection2D());
}

public void
setParentCurrentItem()
{
	this.getParentNucCollection2D().setCurrentItem(this);
}

private char nucChar = 'N'; // A | U | G | C | R | Y | N

public void
setNucChar(char nucChar)
throws Exception
{
	if (!isValidNucChar(nucChar))
	{
		throw new Exception("Exception in NucNode.setNucChar() for id " +
		this.getID() + ":\n\t" +
			"'" + nucChar + "' not a valid nuc char\n" +
			"\tMust be: (A|U|G|C|R|Y|N)");
	}
	this.nucChar = nucChar;
}

public char
getNucChar()
{
	return (this.nucChar);
}

public boolean
isValidNucChar()
{
	// should only deal with upper case for nucChar. display of
	// nuc can be any symbol.
	return (isValidNucChar(this.getNucChar()));
}

public static boolean
isValidNucChar(char nucChar)
{
	// should only deal with upper case for nucChar. display of
	// nuc can be any symbol.
	return((nucChar == 'A') ||
		(nucChar == 'U') ||
		(nucChar == 'C') ||
		(nucChar == 'G') ||
		(nucChar == 'R') || // pyrmidine or purine ??
		(nucChar == 'Y') || // pyrmidine or purine ??
		(nucChar == 'N') || // any type
		(nucChar == 'T') || // what is T doing in here? (used in tRNA, but why?)
		(nucChar == 'D')); // what does D mean?
}

// this nucs basepair ordinal number if it has one, else 0
// ordinal refers to the id not the place in array
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
	/* NEED to make this work for tests: should work but doesn't
	if (this.getBasePair() == null)
		return (0);
	return (this.getBasePair().getID());
	*/
}

public NucNode
lastNuc()
{
	if (this.getID() == 1)
		return (null);
	return(this.getParentNucCollection2D().getNucAt(this.getID() - 1));
}

// null or not
public NucNode
nextNuc()
{
	if (this.getID() == this.getParentNucCollection2D().getNucCount())
		return (null);
	return(this.getParentNucCollection2D().getNucAt(this.getID() + 1));
}

public NucNode
nextNonNullNuc()
throws Exception
{
	this.setParentCurrentItem();
	return (this.getParentNucCollection2D().nextNonNullNuc());
}

public NucNode
lastNonNullNuc()
throws Exception
{
	this.setParentCurrentItem();
	return (this.getParentNucCollection2D().lastNonNullNuc());
}

public NucNode
getNextNonNullNuc()
throws Exception
{
	return (this.getParentNucCollection2D().getNextNonNullNuc());
}

public NucNode
getLastNonNullNuc()
throws Exception
{
	return (this.getParentNucCollection2D().getLastNonNullNuc());
}

// does this nucs bp refer to same rna strand; Can't use this
// to also check if it is singlestranded since it could return
// false if basepaired to a different rna strand.
public boolean
isSelfRefBasePair()
{
	return (this.isBasePair() && ((getBasePairSStrName() == null) ||
		(getBasePairSStrName().equals(this.getParentNucCollection2D().getName()))));
}

// does this nucs bp refer to some other rna strand; Can't use this
// to also check if it is singlestranded since it could return
// false if basepaired to same rna strand.
public boolean
isNonSelfRefBasePair()
{
	return (this.isBasePair() && (getBasePairSStrName() != null) &&
		!getBasePairSStrName().equals(this.getParentNucCollection2D().getName()));
}

public boolean
isStartNuc()
{
	return (this.getID() == 1);
}

// start of non null strand
public boolean
isStartSegment()
{
	return (this.isStartNuc() || (this.lastNuc() == null));
}

public boolean
isEndNuc()
{
	return (this.getID() == this.getParentNucCollection2D().getNucCount());
}

// end of non null strand
public boolean
isEndSegment()
{
	return (this.isEndNuc() || (this.nextNuc() == null));
}

// name of strand containing basepair if different from nucs strand
private String basePairSStrName = null;

public void
setBasePairSStrName(String basePairSStrName)
{
	this.basePairSStrName = basePairSStrName;
}

public String
getBasePairSStrName()
{
	return (this.basePairSStrName);
}

private NucNode basePair = null;

public void
setBasePair(NucNode basePair)
{
	this.basePair = basePair;
}

public NucNode
getBasePair()
{
	return (this.basePair);
}

public void
resetBasePair() // use this after read ss
throws Exception
{
	if (this.getBasePairID() <= 0) // then not a bp
		return;
	SSData bpSStr = null;
	NucNode bpNuc = null;
	if (getBasePairSStrName() == null) // then bp is part of same strand
	{
		bpSStr = this.getParentSSData();
	}
	else
	{
		bpSStr = ((ComplexSSDataCollection)this.getParentNucCollection2D().getParentCollection()).
			getSStrByName(getBasePairSStrName());

		// debug("bpSStr: " + bpSStr.getName());

		if (bpSStr == null)
		{
			throw new Exception(
				"Error in NucNode.resetBasePair: " +
				"trying to base pair nuc " + this.getParentNucCollection2D().getName() + ":" +
				getID() + " to external nuc " +
				getBasePairSStrName() + ":" + this.getBasePairID() +
				" which is non-existant");
		}
	}
	if (bpSStr.getNucCount() < this.getBasePairID())
	{
		throw new Exception(
			"Error in NucNode.resetBasePair: " +
			"trying to base pair nuc " + this.getParentNucCollection2D().getName() + ":" +
			getID() + " to nuc " +
			bpSStr.getName() + ":" + this.getBasePairID() +
			" which is non-existant");
	}
	bpNuc = bpSStr.getNucAt(this.getBasePairID());
	if (bpNuc == null)
	{
		throw new Exception(
			"Error in NucNode.resetBasePair: " +
			"trying to base pair nuc " + this.getParentNucCollection2D().getName() + ":" +
			getID() + " to nuc " +
			bpSStr.getName() + ":" + this.getBasePairID() +
			" which is non-existant");
	}
	setBasePair(bpNuc);
	// set or overwrite base pairs existing bp info
	bpNuc.setBasePairID(this.getID());
	if (bpSStr != this.getParentNucCollection2D()) // then bps with another strand
		bpNuc.setBasePairSStrName(this.getParentNucCollection2D().getName());
	bpNuc.setBasePair(this);
}

// logical element types for nucs

public boolean
isBasePair()
{
	return ((this.getBasePairID() > 0) && (getBasePair() != null));
}

public boolean
isSingleStranded()
{
	return (!this.isBasePair());
}

public boolean
isSingleStrandStartNuc()
{
	if (this.isBasePair())
		return (false);

	RNASingleStrand singleStrand = null;

	try
	{
		singleStrand = new RNASingleStrand(this);
	}
	catch (Exception e)
	{
		return (false);
	}

	if (singleStrand == null)
		return (false);
	if (singleStrand.getFivePrimeNuc() == null)
		return (false);

	return (this.getID() == singleStrand.getFivePrimeNuc().getID());
}

public boolean
isFivePrimeBasePair()
{
	return(this.isBasePair() && (this.isSelfRefBasePair() ?
		(this.getID() < this.getBasePairID()) : true));
}

public boolean
isThreePrimeBasePair()
{
	return(this.isBasePair() && (this.isSelfRefBasePair() ?
		(this.getID() > this.getBasePairID()) : true));
}

// the lowest number on helix if self ref. or lowest nuc on fp side
// of side that nucNode is on if refers to another strand.
public boolean
isHelixStart()
{
	boolean is5PEnd = fivePrimeEndOfHelix();
	if (this.isSelfRefBasePair())
		return (is5PEnd && (this.getID() < this.getBasePair().getID()));

	// else non self ref helix.
	return (is5PEnd);
}

// five prime end no matter what side
public boolean
fivePrimeEndOfHelix()
{
	if (!this.isBasePair())
		return(false);

	if (this.getID() == 1)
		return(true);

	// this presumes that there will be no null nucs in a helix
	if (this.lastNuc() == null)
		return(true);

	if (!((NucNode)this.lastNuc()).isBasePair())
		return(true);
	if (!isContiguousBasePairWith(((NucNode)this.lastNuc())))
		return(true);
	return(false);
}

public boolean
isContiguousBasePairWith(NucNode withNuc)
{
	if (withNuc == null)
		return (false);
	if (this == withNuc)
		return (false);
	if (!this.isBasePair())
		return (false);
	if (!withNuc.isBasePair())
		return (false);
	if (Math.abs(this.getID() - withNuc.getID()) != 1)
		return (false);
	if (this.getID() < withNuc.getID())
	{
		if (withNuc.getBasePair().getID() != (this.getBasePair().getID() - 1))
			return (false);
	}
	else
	{
		if (withNuc.getBasePair().getID() != (this.getBasePair().getID() + 1))
			return (false);
	}
	return (true);
}

// nuc is in a base pair that is in a helix
public boolean
isHelixBasePair()
{
	return (isContiguousBasePairWith(this.lastNuc()) ||
		isContiguousBasePairWith(this.nextNuc()));
}

// nuc is in a base pair that is not in a helix
public boolean
isSingleBasePairHelix()
{
	return(this.isBasePair() && !this.isHelixBasePair());
}

public boolean
inDefaultCanonicalBasePair()
{
	if (!this.isBasePair())
		return (false);
	return (RNABasePair.isDefaultCanonical(this));
}

// either default bp type or overwritten type
public boolean
inCanonicalBasePair()
throws Exception
{
	if (!this.isBasePair())
		return (false);
	return (this.getBasePairType() == ComplexDefines.BP_TYPE_CANONICAL);	
}

public boolean
inDefaultPurinePurineMisMatchBasePair()
{
	if (!this.isBasePair())
		return (false);
	return (RNABasePair.isDefaultPurinePurineMisMatch(this));
}

public boolean
inDefaultPyrimidinePyrimidineMisMatchBasePair()
{
	if (!this.isBasePair())
		return (false);
	return (RNABasePair.isDefaultPyrimidinePyrimidineMisMatch(this));
}

public boolean
inDefaultPurinePyrimidineMisMatchBasePair()
{
	if (!this.isBasePair())
		return (false);
	return (RNABasePair.isDefaultPurinePyrimidineMisMatch(this));
}

public boolean
inDefaultMisMatchBasePair()
{
	if (!this.isBasePair())
		return (false);
	return (RNABasePair.isDefaultMisMatch(this));
}

// either default bp type or overwritten type
public boolean
inMisMatchBasePair()
throws Exception
{
	if (!this.isBasePair())
		return (false);
	return (this.getBasePairType() == ComplexDefines.BP_TYPE_MISMATCH);	
}

public boolean
inDefaultWobbleBasePair()
{
	if (!this.isBasePair())
		return (false);
	return (RNABasePair.isDefaultWobble(this));
}

// either default bp type or overwritten type
public boolean
inWobbleBasePair()
throws Exception
{
	if (!this.isBasePair())
		return (false);
	return (this.getBasePairType() == ComplexDefines.BP_TYPE_WOBBLE);	
}

// either default bp type or overwritten type
public boolean
inWeakBasePair()
throws Exception
{
	if (!this.isBasePair())
		return (false);
	return (this.getBasePairType() == ComplexDefines.BP_TYPE_WEAK);	
}

// either default bp type or overwritten type
public boolean
inPhosphateBasePair()
throws Exception
{
	if (!this.isBasePair())
		return (false);
	return (this.getBasePairType() == ComplexDefines.BP_TYPE_PHOSPHATE);	
}

private int nonDefaultBasePairType = ComplexDefines.BP_TYPE_UNKNOWN;

// override regular basepair types (see RNABasePair.java)
public void
setNonDefaultBasePairType(int nonDefaultBasePairType)
{
    this.nonDefaultBasePairType = nonDefaultBasePairType;
}

public int
getNonDefaultBasePairType()
{
    return (this.nonDefaultBasePairType);
}

public boolean
isNonDefaultBasePairType()
{
	return (this.getNonDefaultBasePairType() != ComplexDefines.BP_TYPE_UNKNOWN);
}

public int
getBasePairType()
{
	// first try and determine if type is overridden
	if (RNABasePair.isDefaultCanonical(this))
	{
	  	if (this.isNonDefaultBasePairType())
		{
			return (this.getNonDefaultBasePairType());
		}
		else
		{
			return (ComplexDefines.BP_TYPE_CANONICAL);
		}
	}

	if (RNABasePair.isDefaultPurinePurineMisMatch(this))
	{
	  	if (this.isNonDefaultBasePairType())
		{
			return (this.getNonDefaultBasePairType());
		}
		else
		{
			return (ComplexDefines.BP_TYPE_MISMATCH);
		}
	}

	if (RNABasePair.isDefaultPyrimidinePyrimidineMisMatch(this))
	{
	  	if (this.isNonDefaultBasePairType())
		{
			return (this.getNonDefaultBasePairType());
		}
		else
		{
			return (ComplexDefines.BP_TYPE_MISMATCH);
		}
	}

	if (RNABasePair.isDefaultPurinePyrimidineMisMatch(this))
	{
	  	if (this.isNonDefaultBasePairType())
		{
			return (this.getNonDefaultBasePairType());
		}
		else
		{
			return (ComplexDefines.BP_TYPE_MISMATCH);
		}
	}

	if (RNABasePair.isDefaultWobble(this))
	{
	  	if (this.isNonDefaultBasePairType())
		{
			return (this.getNonDefaultBasePairType());
		}
		else
		{
			return (ComplexDefines.BP_TYPE_WOBBLE);
		}
	}

	// NEED to investigate:
	// debug("FOUND UNKNOWN BP TYPE AT: " + this.getID());
	return (ComplexDefines.BP_TYPE_UNKNOWN);
}

private String groupName = null;

public void
setGroupName(String groupName)
{
    this.groupName = groupName;
}

public String
getGroupName()
{
    return (this.groupName);
}

private double line5PDeltaX = 0.00;

public void
setLine5PDeltaX(double line5PDeltaX)
{
    this.line5PDeltaX = line5PDeltaX;
}

public double
getLine5PDeltaX()
{
    return (this.line5PDeltaX);
}

private double line5PDeltaY = 0.00;

public void
setLine5PDeltaY(double line5PDeltaY)
{
    this.line5PDeltaY = line5PDeltaY;
}

public double
getLine5PDeltaY()
{
    return (this.line5PDeltaY);
}

private double line3PDeltaX = 0.00;

public void
setLine3PDeltaX(double line3PDeltaX)
{
    this.line3PDeltaX = line3PDeltaX;
}

public double
getLine3PDeltaX()
{
    return (this.line3PDeltaX);
}

private double line3PDeltaY = 0.00;

public void
setLine3PDeltaY(double line3PDeltaY)
{
    this.line3PDeltaY = line3PDeltaY;
}

public double
getLine3PDeltaY()
{
    return (this.line3PDeltaY);
}

private double labelDeltaX = 0.00;

public void
setLabelDeltaX(double labelDeltaX)
{
    this.labelDeltaX = labelDeltaX;
}

public double
getLabelDeltaX()
{
    return (this.labelDeltaX);
}

private double labelDeltaY = 0.00;

public void
setLabelDeltaY(double labelDeltaY)
{
    this.labelDeltaY = labelDeltaY;
}

public double
getLabelDeltaY()
{
    return (this.labelDeltaY);
}

private boolean label5PSide = false;

public void
setLabel5PSide(boolean label5PSide)
{
    this.label5PSide = label5PSide;
}

public boolean
getLabel5PSide()
{
    return (this.label5PSide);
}

/*************** TO ADD *****************************/

/*
public boolean
nucIsNonStandardBasePair()
{
	char nucChar1, nucChar2;

	if(!isBasePair())
		return(false);
	nucChar1 = nucChar;
	nucChar2 = getBasePair().nucChar;
	nucChar1 = Character.isUpperCase(nucChar1) ? nucChar1 :
		Character.toUpperCase(nucChar1);
	nucChar2 = Character.isUpperCase(nucChar2) ? nucChar2 :
		Character.toUpperCase(nucChar2);
	return( (nucChar1 == 'A' && nucChar2 == 'A') ||
		(nucChar1 == 'G' && nucChar2 == 'G') ||
		(nucChar1 == 'C' && nucChar2 == 'C') ||
		(nucChar1 == 'U' && nucChar2 == 'U') ||
		(nucChar1 == 'A' && nucChar2 == 'C') ||
		(nucChar1 == 'C' && nucChar2 == 'A') ||
		(nucChar1 == 'C' && nucChar2 == 'U') ||
		(nucChar1 == 'U' && nucChar2 == 'C'));
}

public boolean
nucIsNonCanonicalBasePair()
{
	char nucChar1, nucChar2;

	if(isBasePair())
		return(false);
	nucChar1 = nucChar;
	nucChar2 = getBasePair().nucChar;
	nucChar1 = Character.isUpperCase(nucChar1) ? nucChar1 :
		Character.toUpperCase(nucChar1);
	nucChar2 = Character.isUpperCase(nucChar2) ? nucChar2 :
		Character.toUpperCase(nucChar2);
	return((nucChar1 == 'U' && nucChar2 == 'G') ||
		(nucChar1 == 'G' && nucChar2 == 'U') ||
		(nucChar1 == 'A' && nucChar2 == 'G') ||
		(nucChar1 == 'G' && nucChar2 == 'A'));
}
*/

/**************** General Methods *******************/

private boolean flagged = false;

public void
setFlagged(boolean flagged)
{
    this.flagged = flagged;
}

public boolean
getFlagged()
{
    return (this.flagged);
}

public boolean
isFlagged()
{
	return (this.getFlagged());
}

// This needs to check against ComplexScene2D as this will be entire
// scene; some other classes are children of ComplexScene.
public ComplexScene
getComplexScene()
throws Exception
{
	ComplexCollection nucsComplexScene = (ComplexCollection)this.getParentCollection();
	if (nucsComplexScene == null)
		throw new ComplexException("Nuc has no parent collection in RNANamedGroup constructor");

	while (true)
	{
		if (nucsComplexScene instanceof ComplexScene2D)
			break;
		nucsComplexScene = (ComplexCollection)nucsComplexScene.getParentCollection();
	}
	if (!(nucsComplexScene instanceof ComplexScene2D))
		throw new ComplexException("Nucs has no complexScene as parent collection in RNANamedGroup constructor");

	return ((ComplexScene)nucsComplexScene);
}

public String
toShortStringWithParentsName()
{
	return(this.getID() + " " + nucChar + " in: " +
		this.getParentSSData().getName());
}

public String
toString()
{
	String bpName = new String(" ");
	if (getBasePairSStrName() != null)
		bpName = getBasePairSStrName();

	return(this.getID() + " " + nucChar + " " +
		" " + this.getBasePairID() + " " + bpName);
}

private static void
debug(String s)
{
	System.err.println("NucNode-> " + s);
}

}
