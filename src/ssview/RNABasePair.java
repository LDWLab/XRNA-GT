package ssview;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import jimage.DrawObject;
import util.math.BLine2D;
import util.GraphicsUtil;

// info for one basepair in a structure
public class
RNABasePair
extends NucCollection2D
{

public
RNABasePair()
throws Exception
{
	super();
}

// presumes that refNuc is basepaired else exception
public
RNABasePair(NucNode refNuc)
throws Exception
{
	this.set(refNuc);
}

// presumes that caller wants nucs to be base paired together
public
RNABasePair(NucNode nuc0, NucNode nuc1)
throws Exception
{
	this.initNewBasePair(nuc0, nuc1);
	this.set(nuc0);
}
public void
initNewBasePair(NucNode nuc0, NucNode nuc1)
throws Exception
{
	nuc0.setBasePairID(nuc1.getID());
	nuc1.setBasePairID(nuc0.getID());

	/* this messes up "Complex PseudoKnots testing"
	if (!nuc0.getParentSSData().getName().equals(nuc1.getParentSSData().getName()))
	{
		nuc0.setBasePairSStrName(nuc1.getParentSSData().getName());
		nuc1.setBasePairSStrName(nuc0.getParentSSData().getName());
	}
	*/

	// make sure nucs have a sstr environment to live in or exception
	nuc0.resetBasePair();
	nuc1.resetBasePair();

	// NEED to look into if nuc0 and nuc1 have different parents
	this.setParentCollection(nuc0.getParentNucCollection2D());
}

// presumes that refNuc is basepaired else exception
public void
set(NucNode refNuc)
throws Exception
{
	if (!refNuc.isBasePair())
		throw new Exception("refNuc not base paired: " + refNuc);
	this.initNewBasePair(refNuc, refNuc.getBasePair());	
	if (refNuc.isFivePrimeBasePair())
	{
		this.setFivePrimeNuc(refNuc);
		this.setThreePrimeNuc(refNuc.getBasePair());
	}
	else
	{
		this.setFivePrimeNuc(refNuc.getBasePair());
		this.setThreePrimeNuc(refNuc);
	}
}

public void
unset()
{
	this.getFivePrimeNuc().unsetBasePair();
	this.getThreePrimeNuc().unsetBasePair();
}

// might have to worry about base pairing across strands
public Vector
getItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();

	if (this.getFivePrimeNuc() != null)
	{
		delineators.add(this.getFivePrimeNuc());
		delineators.add(this.getFivePrimeNuc());
	}
	if (this.getThreePrimeNuc() != null)
	{
		delineators.add(this.getThreePrimeNuc());
		delineators.add(this.getThreePrimeNuc());
	}
	return(delineators);
}

public static String
typeToString(NucNode nuc)
throws Exception
{
	switch (nuc.getBasePairType())
	{
	  case ComplexDefines.BP_TYPE_CANONICAL :
		return ("Canonical");
	  case ComplexDefines.BP_TYPE_WOBBLE :
		return ("Wobble");
	  case ComplexDefines.BP_TYPE_MISMATCH :
		return ("MisMatch");
	  case ComplexDefines.BP_TYPE_WEAK :
		return ("Weak");
	  case ComplexDefines.BP_TYPE_PHOSPHATE :
		return ("Phosphate");
	}
	return (null);
}

public String
typeToString()
throws Exception
{
	switch (this.getType())
	{
	  case ComplexDefines.BP_TYPE_CANONICAL :
		return ("Canonical");
	  case ComplexDefines.BP_TYPE_WOBBLE :
		return ("Wobble");
	  case ComplexDefines.BP_TYPE_MISMATCH :
		return ("MisMatch");
	  case ComplexDefines.BP_TYPE_WEAK :
		return ("Weak");
	  case ComplexDefines.BP_TYPE_PHOSPHATE :
		return ("Phosphate");
	}
	return (null);
}

public static int
stringToType(String bpType)
{
	if (bpType.equals("Canonical"))
		return(ComplexDefines.BP_TYPE_CANONICAL);
	if (bpType.equals("Wobble"))
		return(ComplexDefines.BP_TYPE_WOBBLE);
	if (bpType.equals("MisMatch"))
		return(ComplexDefines.BP_TYPE_MISMATCH);
	if (bpType.equals("Weak"))
		return(ComplexDefines.BP_TYPE_WEAK);
	if (bpType.equals("Phosphate"))
		return(ComplexDefines.BP_TYPE_PHOSPHATE);
	return(ComplexDefines.BP_TYPE_CANONICAL);
}

public static final double BP_LINE_MULT = 0.66666666;

public static int
bpNucCharsToBPDefine(NucNode nucNode)
{
	if (!nucNode.isBasePair())
		return(ComplexDefines.NULL_BASEPAIR);
	char nucChar = nucNode.getNucChar();
	char bpNucChar = nucNode.getBasePair().getNucChar();

	if((nucChar == 'A') && (bpNucChar == 'A'))
		return(ComplexDefines.AA_BASEPAIR);
	if((nucChar == 'A') && (bpNucChar == 'U'))
		return(ComplexDefines.AU_BASEPAIR);
	if((nucChar == 'A') && (bpNucChar == 'G'))
		return(ComplexDefines.AG_BASEPAIR);
	if((nucChar == 'A') && (bpNucChar == 'C'))
		return(ComplexDefines.AC_BASEPAIR);
	if((nucChar == 'U') && (bpNucChar == 'A'))
		return(ComplexDefines.UA_BASEPAIR);
	if((nucChar == 'U') && (bpNucChar == 'U'))
		return(ComplexDefines.UU_BASEPAIR);
	if((nucChar == 'U') && (bpNucChar == 'G'))
		return(ComplexDefines.UG_BASEPAIR);
	if((nucChar == 'U') && (bpNucChar == 'C'))
		return(ComplexDefines.UC_BASEPAIR);
	if((nucChar == 'G') && (bpNucChar == 'A'))
		return(ComplexDefines.GA_BASEPAIR);
	if((nucChar == 'G') && (bpNucChar == 'U'))
		return(ComplexDefines.GU_BASEPAIR);
	if((nucChar == 'G') && (bpNucChar == 'G'))
		return(ComplexDefines.GG_BASEPAIR);
	if((nucChar == 'G') && (bpNucChar == 'C'))
		return(ComplexDefines.GC_BASEPAIR);
	if((nucChar == 'C') && (bpNucChar == 'A'))
		return(ComplexDefines.CA_BASEPAIR);
	if((nucChar == 'C') && (bpNucChar == 'U'))
		return(ComplexDefines.CU_BASEPAIR);
	if((nucChar == 'C') && (bpNucChar == 'G'))
		return(ComplexDefines.CG_BASEPAIR);
	if((nucChar == 'C') && (bpNucChar == 'C'))
		return(ComplexDefines.CC_BASEPAIR);
	return(ComplexDefines.NULL_BASEPAIR);
}

public int
bpNucCharsToBPDefine()
{
	return (bpNucCharsToBPDefine(this.getFivePrimeNuc()));
}

public static boolean
nucsAreBasePaired(NucNode fpNuc, NucNode tpNuc)
{
	if (fpNuc.getBasePair() != tpNuc)
		return (false);
	if (tpNuc.getBasePair() != fpNuc)
		return (false);
	return (true);
}

public static boolean
isDefaultCanonical(NucNode nuc)
{
	switch (bpNucCharsToBPDefine(nuc))
	{
	  case ComplexDefines.AU_BASEPAIR :
	  case ComplexDefines.UA_BASEPAIR :
	  case ComplexDefines.GC_BASEPAIR :
	  case ComplexDefines.CG_BASEPAIR :
	  	return (true);
	  default :
	  	return (false);
	}
}

public boolean
isDefaultCanonical()
{
	return (isDefaultCanonical(this.getFivePrimeNuc()));
}

// either default bp type or overwritten type
public boolean
isCanonical()
throws Exception
{
	return (this.getType() == ComplexDefines.BP_TYPE_CANONICAL);
}

public static char
genCanonicalNucChar(char nucChar)
{
	switch (nucChar)
	{
	  case 'A' :
		return ('U');
	  case 'U' :
		return ('A');
	  case 'G' :
		return ('C');
	  case 'C' :
		return ('G');
	}

	return ('N');
}

public static boolean
isDefaultPurinePurineMisMatch(NucNode nuc)
{
	switch (bpNucCharsToBPDefine(nuc))
	{
	  case ComplexDefines.AA_BASEPAIR :
	  case ComplexDefines.GG_BASEPAIR :
	  case ComplexDefines.AG_BASEPAIR :
	  case ComplexDefines.GA_BASEPAIR :
	  	return (true);
	  default :
	  	return (false);
	}
}

public boolean
isDefaultPurinePurineMisMatch()
{
	return (isDefaultPurinePurineMisMatch(this.getFivePrimeNuc()));
}

public static boolean
isDefaultPyrimidinePyrimidineMisMatch(NucNode nuc)
{
	switch (bpNucCharsToBPDefine(nuc))
	{
	  case ComplexDefines.CC_BASEPAIR :
	  case ComplexDefines.UU_BASEPAIR :
	  case ComplexDefines.CU_BASEPAIR :
	  case ComplexDefines.UC_BASEPAIR :
	  	return (true);
	  default :
	  	return (false);
	}
}

public boolean
isDefaultPyrimidinePyrimidineMisMatch()
{
	return (isDefaultPyrimidinePyrimidineMisMatch(this.getFivePrimeNuc()));
}

public static boolean
isDefaultPurinePyrimidineMisMatch(NucNode nuc)
{
	switch (bpNucCharsToBPDefine(nuc))
	{
	  case ComplexDefines.AC_BASEPAIR :
	  case ComplexDefines.CA_BASEPAIR :
	  	return (true);
	  default :
	  	return (false);
	}
}

public boolean
isDefaultPurinePyrimidineMisMatch()
{
	return (isDefaultPurinePyrimidineMisMatch(this.getFivePrimeNuc()));
}

public static boolean
isDefaultMisMatch(NucNode nuc)
{
	return (isDefaultPurinePurineMisMatch(nuc) ||
		isDefaultPyrimidinePyrimidineMisMatch(nuc) ||
		isDefaultPurinePyrimidineMisMatch(nuc));
}

public boolean
isDefaultMisMatch()
{
	return (isDefaultMisMatch(this.getFivePrimeNuc()));
}

// either default bp type or overwritten type
public boolean
isMisMatch()
throws Exception
{
	return (this.getType() == ComplexDefines.BP_TYPE_MISMATCH);
}

public static boolean
isDefaultWobble(NucNode nuc)
{
	switch (bpNucCharsToBPDefine(nuc))
	{
	  case ComplexDefines.UG_BASEPAIR :
	  case ComplexDefines.GU_BASEPAIR :
	  	return (true);
	  default :
	  	return (false);
	}
}

public boolean
isDefaultWobble()
{
	return (isDefaultWobble(this.getFivePrimeNuc()));
}

// either default bp type or overwritten type
public boolean
isWobble()
throws Exception
{
	return (this.getType() == ComplexDefines.BP_TYPE_WOBBLE);
}

// no default bp type, so return overwritten type
public boolean
isWeak()
throws Exception
{
	return (this.getType() == ComplexDefines.BP_TYPE_WEAK);
}

// either default bp type or overwritten type
public boolean
isPhosphate()
throws Exception
{
	return (this.getType() == ComplexDefines.BP_TYPE_PHOSPHATE);
}

private NucNode fivePrimeNuc = null;

public void
setFivePrimeNuc(NucNode fivePrimeNuc)
{
    this.fivePrimeNuc = fivePrimeNuc;
}

public NucNode
getFivePrimeNuc()
{
    return (this.fivePrimeNuc);
}

private NucNode threePrimeNuc = null;

public void
setThreePrimeNuc(NucNode threePrimeNuc)
{
    this.threePrimeNuc = threePrimeNuc;
}

public NucNode
getThreePrimeNuc()
{
    return (this.threePrimeNuc);
}

public void
setNonDefaultBasePairType(int type)
{
	this.getFivePrimeNuc().setNonDefaultBasePairType(type);	
	if (this.getFivePrimeNuc().getBasePairSStrName() == null) // then self ref bp
		this.getThreePrimeNuc().setNonDefaultBasePairType(type);	
}

public int
getType()
throws Exception
{
	if (this.getFivePrimeNuc().getBasePairType() !=
		this.getThreePrimeNuc().getBasePairType())
		throw new ComplexException(
			"Error in BasePair.getBasePairType()",
			ComplexDefines.RNA_BASE_PAIR_ERROR + ComplexDefines.CREATE_ERROR,
			ComplexDefines.CREATE_BASEPAIR_ERROR_MSG,
			"for BasePair: " + this.toString());
	
    return (this.getFivePrimeNuc().getBasePairType());
}

public String
toString()
{
	return("5p nuc: " + this.getFivePrimeNuc() + ", " +
		"3p nuc: " + this.getThreePrimeNuc());
}

private static void
debug(String s)
{
	System.err.println("RNABasePair-> " + s);
}

}
