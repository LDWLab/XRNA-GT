package ssview;

import java.util.Vector;
import java.io.*;

import util.*;
import util.math.*;

public class
RNASingleStrand
extends NucCollection2D
{

public
RNASingleStrand()
throws Exception
{
}

// figures out endpts on it's own from obvious delineation nucs
public
RNASingleStrand(NucNode nuc)
throws Exception
{
	super();
	this.initNewRNASingleStrand(nuc);
	this.set(nuc);
}

public
RNASingleStrand(NucNode fpNuc, NucNode tpNuc)
throws Exception
{
	super();
	this.initNewRNASingleStrand(fpNuc);
	this.set(fpNuc, tpNuc);
}

public void
initNewRNASingleStrand(NucNode nuc)
throws Exception
{
	this.setParentCollection(nuc.getParentNucCollection2D());
}

public void
set(NucNode nuc)
throws Exception
{
	this.setParentCollection(nuc.getParentCollection());
	this.setSingleStrandEndNucs(nuc);
	this.reset();
	if (nuc.getIsSingleStrandDelineator())
	{
		if (this.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
		{
			NucNode lastNuc = nuc.lastNuc();
			if ((lastNuc != null) && (lastNuc.isSingleStranded()))
			throw new ComplexException(
				"Error in RNASingleStrand.set(NucNode)",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.CREATE_ERROR,
				ComplexDefines.CREATE_SINGLESTRAND_AMBIGUOUS_MSG);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_MID)
		{
			throw new ComplexException(
				"Error in RNASingleStrand.set(NucNode)",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.CREATE_ERROR,
				ComplexDefines.CREATE_SINGLESTRAND_AMBIGUOUS_MSG);
		}
		else if (this.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
		{
			NucNode nextNuc = nuc.nextNuc();
			if ((nextNuc != null) && (nextNuc.isSingleStranded()))
				throw new ComplexException(
					"Error in RNASingleStrand.set(NucNode)",
					ComplexDefines.RNA_SINGLE_STRAND_ERROR +
						ComplexDefines.CREATE_ERROR,
					ComplexDefines.CREATE_SINGLESTRAND_AMBIGUOUS_MSG);
		}
		else
		{
			throw new ComplexException(
				"Error in RNASingleStrand.set(NucNode)",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.CREATE_ERROR,
				ComplexDefines.CREATE_SINGLESTRAND_AMBIGUOUS_MSG);
		}
	}

	boolean testWellFormed = this.isWellFormed();
}

public void
set(NucNode fpNuc, NucNode tpNuc)
throws Exception
{
	if (fpNuc.getParentSSData() != tpNuc.getParentSSData())
		throw new Exception(
			"Error in RNASingleStrand.set(fpNuc, tpNuc):\n" +
				"trying to define single strand with ref nucs of different parents");
	this.setParentCollection(fpNuc.getParentCollection());
	this.setSingleStrandEndNucs(fpNuc, tpNuc);
	this.reset();
	boolean testWellFormed = this.isWellFormed();
}

public boolean
isWellFormed()
throws ComplexException
{
	int fpDelinNuc = this.getFivePrimeDelineateNuc().getID();
	int fpNuc = this.getFivePrimeNuc().getID();
	int tpNuc = this.getThreePrimeNuc().getID();
	int tpDelinNuc = this.getThreePrimeDelineateNuc().getID();

	int nucCount = this.getDelineatedNucCount();
	if (nucCount < 1)
	{
		throw new ComplexException(
			"Error in RNASingleStrand.isWellFormed()",
			ComplexDefines.RNA_SINGLE_STRAND_ERROR +
				ComplexDefines.CREATE_ERROR,
			"Number of nucs in single strand is less than 1");
	}
	if (nucCount == 1)
	{
		if ((fpDelinNuc != fpNuc) || (fpNuc != tpNuc) || (tpNuc != tpDelinNuc))
			throw new ComplexException(
				"Error in RNASingleStrand.isWellFormed()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.CREATE_ERROR,
				"order of nucs wrong: " + fpDelinNuc + " " + fpNuc + " " + tpNuc + " " + tpDelinNuc);
	}
	else if (nucCount == 2)
	{
		if ((fpDelinNuc != fpNuc) || (tpNuc != tpDelinNuc))
			throw new ComplexException(
				"Error in RNASingleStrand.isWellFormed()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.CREATE_ERROR,
				"order of nucs wrong: " + fpDelinNuc + " " + fpNuc + " " + tpNuc + " " + tpDelinNuc);
	}
	else if (nucCount == 3)
	{
		if ((fpDelinNuc >= fpNuc) || (tpNuc >= tpDelinNuc))
			throw new ComplexException(
				"Error in RNASingleStrand.isWellFormed()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.CREATE_ERROR,
				"order of nucs wrong: " + fpDelinNuc + " " + fpNuc + " " + tpNuc + " " + tpDelinNuc);
	}
	else
	{
		if ((fpDelinNuc >= fpNuc) || (fpNuc >= tpNuc) ||
				(tpNuc >= tpDelinNuc))
			throw new ComplexException(
				"Error in RNASingleStrand.isWellFormed()",
				ComplexDefines.RNA_SINGLE_STRAND_ERROR +
					ComplexDefines.CREATE_ERROR,
				"order of nucs wrong: " + fpDelinNuc + " " + fpNuc + " " + tpNuc + " " + tpDelinNuc);
	}
	
	return (true);
}

public void
reset()
throws Exception
{
	this.setMidLengthNuc(this.getParentSSData().getNucAt(
		(getFivePrimeDelineateNuc().getID() +
		getThreePrimeDelineateNuc().getID())/2));
	this.setLinePartition();
}

// figure out end nucs
private void
setSingleStrandEndNucs(NucNode nuc)
throws Exception
{
	if (nuc == null)
		throw new Exception("Error in RNASingleStrand constructor: trying set singlestrand" +
			" with null reference nuc");

	this.setRefNuc(nuc);

	if (getRefNuc().isBasePair())
		throw new Exception("Error in RNASingleStrand constructor: trying set single-strand" +
			" with base-paired reference nuc: " + nuc);

	this.setSingleStrandEndNucs();
}

// explicitly set end nucs
private void
setSingleStrandEndNucs(NucNode fpNuc, NucNode tpNuc)
throws Exception
{
	if ((fpNuc == null) || (tpNuc == null))
		throw new Exception("Error in RNASingleStrand.setSingleStrandEndNucs(fpNuc, tpNuc): trying set helix with null reference nuc");

	/*
	if (fpNuc.isBasePair() || tpNuc.isBasePair())
		throw new Exception("Error in RNASingleStrand constructor: trying set single-strand" +
			" with base-paired nucs");
	*/
	
	if (tpNuc != tpNuc)
	{
		NucNode probeNuc = fpNuc.nextNonNullNuc();
		if (probeNuc.getID() == tpNuc.getID())
			return;

		NucNode lastNuc = tpNuc.lastNonNullNuc();
		if (probeNuc.getID() > lastNuc.getID())
			return;
		while (probeNuc != lastNuc)
		{
			if (probeNuc.isBasePair())
				throw new Exception("Error in RNASingleStrand constructor: trying set single-strand" +
					" with base-paired nucs");
			
			probeNuc = probeNuc.nextNonNullNuc();
		}
	}

	this.setRefNuc(fpNuc);

	// special cases:
	if (fpNuc == tpNuc) // single strand length == 1:
	{
		this.setFivePrimeDelineateNuc(fpNuc);
		this.setFivePrimeNuc(fpNuc);
		this.setThreePrimeNuc(fpNuc);
		this.setThreePrimeDelineateNuc(fpNuc);
	}
	else if (fpNuc.getID() == tpNuc.getID() - 1) // single strand length == 2:
	{
		this.setFivePrimeDelineateNuc(fpNuc);
		this.setFivePrimeNuc(fpNuc);
		this.setThreePrimeNuc(tpNuc);
		this.setThreePrimeDelineateNuc(tpNuc);
	}
	else if (fpNuc.getID() == tpNuc.getID() - 2) // single strand length == 3:
	{
		this.setFivePrimeDelineateNuc(fpNuc);
		this.setFivePrimeNuc(fpNuc.nextNuc());
		this.setThreePrimeNuc(fpNuc.nextNuc());
		this.setThreePrimeDelineateNuc(tpNuc);
	}
	else // regular case
	{
		this.setFivePrimeDelineateNuc(fpNuc);
		this.setFivePrimeNuc(fpNuc.nextNuc());
		this.setThreePrimeNuc(tpNuc.lastNuc());
		this.setThreePrimeDelineateNuc(tpNuc);
	}

	this.resetNucCount();
	this.reset();
	setRefNuc(this.getMidLengthNuc());
}

// like a bulged nuc or a single nuc hairpin
public boolean
isSingleNuc()
{
	return (this.getNonDelineatedNucCount() == 1);
}

// has 2 nucs in single strand
public boolean
isDoubleNucs()
{
	return (this.getNonDelineatedNucCount() == 2);
}

// is singlestrands delineate nucs both basepairs
public boolean
isBasePairDelineated()
{
	return(this.getFivePrimeDelineateNuc().isBasePair() &&
		this.getThreePrimeDelineateNuc().isBasePair());
}

// is singlestrands 5' delineate nuc a basepair and not 3'
// delineate nuc
public boolean
isFivePrimeBasePairDelineatedOnly()
{
	return(this.getFivePrimeDelineateNuc().isBasePair() &&
		(!this.getThreePrimeDelineateNuc().isBasePair()));
}

// is singlestrands 3' delineate nuc a basepair and not 5'
// delineate nuc
public boolean
isThreePrimeBasePairDelineatedOnly()
{
	return((!this.getFivePrimeDelineateNuc().isBasePair()) &&
		this.getThreePrimeDelineateNuc().isBasePair());
}

private void
setSingleStrandEndNucs()
throws Exception
{
	// try first finding delineate nucs and then set end nucs to inside this
	this.setDelineateNucs();
	this.setEndNucs();

	this.resetNucCount();
}

private void
resetNucCount()
{
	// set count of all non delineated nucs
	/*
	this.setLength(this.getThreePrimeNuc().getID() -
		this.getFivePrimeNuc().getID() + 1);
	*/
	
	// set count of all single stranded nucs
	// first look at special cases where delineate nucs
	// are part of the single strand (like nucs 1 and endnuc)
	if ((!this.getFivePrimeDelineateNuc().isBasePair()) &&
		(!this.getThreePrimeDelineateNuc().isBasePair()))
		this.setAllNucCount(this.getThreePrimeDelineateNuc().getID() -
			this.getFivePrimeDelineateNuc().getID() + 1);
	else if ((!this.getFivePrimeDelineateNuc().isBasePair()) &&
		(this.getThreePrimeDelineateNuc().isBasePair()))
		this.setAllNucCount(this.getThreePrimeDelineateNuc().getID() -
			this.getFivePrimeNuc().getID() + 1);
	else if ((this.getFivePrimeDelineateNuc().isBasePair()) &&
		(!this.getThreePrimeDelineateNuc().isBasePair()))
		this.setAllNucCount(this.getThreePrimeNuc().getID() -
			this.getFivePrimeDelineateNuc().getID() + 1);
	else
		this.setAllNucCount(this.getThreePrimeNuc().getID() -
			this.getFivePrimeNuc().getID() + 1);
}

// count of all nucs between delineated nucs inclusive
private int allNucCount = 0;

public void
setAllNucCount(int allNucCount)
{
    this.allNucCount = allNucCount;
}

public int
getAllNucCount()
{
    return (this.allNucCount);
}

public int
getNonDelineatedNucCount()
{
	return(this.getThreePrimeNuc().getID() -
		this.getFivePrimeNuc().getID() + 1);
}

public int
getDelineatedNucCount()
{
	return(this.getThreePrimeDelineateNuc().getID() -
		this.getFivePrimeDelineateNuc().getID() + 1);
}

private NucNode midLengthNuc = null;

public void
setMidLengthNuc(NucNode midLengthNuc)
{
    this.midLengthNuc = midLengthNuc;
}

public NucNode
getMidLengthNuc()
{
    return (this.midLengthNuc);
}

private NucNode refNuc = null;

public void
setRefNuc(NucNode nuc)
throws Exception
{
	refNuc = nuc;
	this.setLinePartition();
}

public NucNode
getRefNuc()
{
	return (refNuc);
}

private NucNode fivePrimeNuc = null;

public void
setFivePrimeNuc(NucNode nuc)
{
	fivePrimeNuc = nuc;
}

public NucNode
getFivePrimeNuc()
{
	return (fivePrimeNuc);
}

private NucNode threePrimeNuc = null;

public void
setThreePrimeNuc(NucNode nuc)
{
	threePrimeNuc = nuc;
}

public NucNode
getThreePrimeNuc()
{
	return (threePrimeNuc);
}

private NucNode fivePrimeDelineateNuc = null;

public void
setFivePrimeDelineateNuc(NucNode nuc)
{
	fivePrimeDelineateNuc = nuc;
}

public NucNode
getFivePrimeDelineateNuc()
{
	return (fivePrimeDelineateNuc);
}

private NucNode threePrimeDelineateNuc = null;

public void
setThreePrimeDelineateNuc(NucNode nuc)
{
	threePrimeDelineateNuc = nuc;
}

public NucNode
getThreePrimeDelineateNuc()
{
	return (threePrimeDelineateNuc);
}

public void
setEndNucs()
throws Exception
{
	SSData sstr = this.getParentSSData();

	// deal with special cases:
	if (sstr.getNucCount() == 1)
	{
		this.setFivePrimeNuc(sstr.getNucAt(1));
		this.setThreePrimeNuc(sstr.getNucAt(1));
		return;
	}
	if (sstr.getNucCount() == 2)
	{
		this.setFivePrimeNuc(sstr.getNucAt(1));
		this.setThreePrimeNuc(sstr.getNucAt(2));
		return;
	}
	if (sstr.getNucCount() == 3)
	{
		this.setFivePrimeNuc(sstr.getNucAt(2));
		this.setThreePrimeNuc(sstr.getNucAt(2));
		return;
	}
	if (sstr.getNucCount() == 4)
	{
		this.setFivePrimeNuc(sstr.getNucAt(2));
		this.setThreePrimeNuc(sstr.getNucAt(3));
		return;
	}

	// look at single strand length of 2:
	if (this.getThreePrimeDelineateNuc().getID() - this.getFivePrimeDelineateNuc().getID() == 1)
	{
		this.setFivePrimeNuc(this.getFivePrimeDelineateNuc());
		this.setThreePrimeNuc(this.getThreePrimeDelineateNuc());
		return;
	}

	NucNode nuc = sstr.getNucAt(this.getFivePrimeDelineateNuc().getID() + 1);
	if (nuc == null)
		throw new Exception("NUC IS NULL 0");
	this.setFivePrimeNuc(nuc);

	nuc = sstr.getNucAt(this.getThreePrimeDelineateNuc().getID() - 1);
	if (nuc == null)
		throw new Exception("NUC IS NULL 1: " + this.getThreePrimeDelineateNuc());
	this.setThreePrimeNuc(nuc);
}

public void
setDelineateNucs()
throws Exception
{
	SSData sstr = this.getParentSSData();

	// deal with special cases:
	if (sstr.getNucCount() == 1)
	{
		this.setFivePrimeDelineateNuc(sstr.getNucAt(1));
		this.setThreePrimeDelineateNuc(sstr.getNucAt(1));
		return;
	}
	if (sstr.getNucCount() == 2)
	{
		this.setFivePrimeDelineateNuc(sstr.getNucAt(1));
		this.setThreePrimeDelineateNuc(sstr.getNucAt(2));
		return;
	}
	if (sstr.getNucCount() == 3)
	{
		this.setFivePrimeDelineateNuc(sstr.getNucAt(1));
		this.setThreePrimeDelineateNuc(sstr.getNucAt(3));
		return;
	}
	if (sstr.getNucCount() == 4)
	{
		this.setFivePrimeDelineateNuc(sstr.getNucAt(1));
		this.setThreePrimeDelineateNuc(sstr.getNucAt(4));
		return;
	}

	this.setFivePrimeDelineateNuc(this.findFivePrimeDelineateNuc());
	this.setThreePrimeDelineateNuc(this.findThreePrimeDelineateNuc());
}

public NucNode
findFivePrimeDelineateNuc()
throws Exception
{
	SSData sstr = this.getParentSSData();
	if (sstr == null)
		throw new Exception("Error in RNASingleStrand.findFiveDelineatePrimeNuc(): " +
			"Probe nuc needs to be in a SSData collection");

	// look at special cases:
	if (sstr.getNucCount() <= 4)
		return (sstr.getNucAt(1));

	if (this.getRefNuc().getID() == 1)
		return (this.getRefNuc());

	if (this.getRefNuc().isSingleStrandDelineator() && (this.getRefNuc().nextNuc() != null) && this.getRefNuc().nextNuc().isSingleStranded())
		return (this.getRefNuc());
	int nucID = this.getRefNuc().getID() - 1;
	NucNode searchNuc = null;
	while (true)
	{
		searchNuc = sstr.getNucAt(nucID);
		if (searchNuc == null)
		{
			searchNuc = sstr.getNucAt(nucID + 1);
			break;
		}
		if (searchNuc.isSingleStrandDelineator())
			break;
		nucID--;
	}
	return (searchNuc);
}

public NucNode
findThreePrimeDelineateNuc()
throws Exception
{
	SSData sstr = this.getParentSSData();
	if (sstr == null)
		throw new Exception("Error in RNASingleStrand.findFiveDelineatePrimeNuc(): " +
			"Probe nuc needs to be in a SSData collection");

	// look at special cases:
	if (sstr.getNucCount() <= 4)
		return (sstr.getNucAt(sstr.getNucCount()));	

	if (this.getRefNuc().getID() == sstr.getNucCount())
		return (this.getRefNuc());

	if (this.getRefNuc().isSingleStrandDelineator() && (this.getRefNuc().lastNuc() != null) && this.getRefNuc().lastNuc().isSingleStranded())
		return (this.getRefNuc());
	int nucID = this.getRefNuc().getID() + 1;
	NucNode searchNuc = null;
	while (true)
	{
		searchNuc = sstr.getNucAt(nucID);
		if (searchNuc == null)
		{
			searchNuc = sstr.getNucAt(nucID - 1);
			break;
		}
		if (searchNuc.isSingleStrandDelineator())
			break;
		nucID++;
	}
	return (searchNuc);
}

// is nuc in hairpin of a helix; but not necessarily formatted as
// such. this is a 2D problem (RNASingleStrand2D.isArc()).
public boolean
isHairPin()
{
	return (RNABasePair.nucsAreBasePaired(
		this.getFivePrimeDelineateNuc(),
		this.getThreePrimeDelineateNuc()));
}

// might have to worry about base pairing across strands if the
// delineated nucs are base paired nucs and are from different strands.
public Vector
getItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();
	NucNode nuc = null;
	boolean inNucs = false;

	if (this.getNonDelineatedNucCount() < 1)
		return (null);

	if (this.getNonDelineatedNucCount() == 1)
	{
		if (this.getFivePrimeNuc() == null)
			return (null);
		delineators.add(this.getFivePrimeNuc());
		delineators.add(this.getFivePrimeNuc());
		return(delineators);
	}

	// THIS IS TMP ONLY:
	// make sure delineate nucs aren't part of strand
	if (this.getFivePrimeDelineateNuc().isSingleStranded())
		delineators.add(this.getFivePrimeDelineateNuc());
	else
		delineators.add(this.getFivePrimeNuc());
	if (this.getThreePrimeDelineateNuc().isSingleStranded())
	delineators.add(this.getThreePrimeDelineateNuc());
	else
		delineators.add(this.getThreePrimeNuc());

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

// MathDefines.LINE_PARTITION_NOT_SET = -2;
// MathDefines.LINE_PARTITION_TAIL = -1;
// MathDefines.LINE_PARTITION_MID = 0;
// MathDefines.LINE_PARTITION_HEAD = 1;
// MathDefines.LINE_PARTITION_ERROR = 2;
private int linePartition = MathDefines.LINE_PARTITION_NOT_SET;

public void
setLinePartition()
throws Exception
{
	this.setLinePartition(this.getRefNuc());
}

public void
setLinePartition(int partition)
throws Exception
{
	this.linePartition = partition;
}

public void
setLinePartition(NucNode refNuc)
throws Exception
{
	if ((this.getFivePrimeNuc() == null) ||
		(this.getThreePrimeNuc() == null))
	{
		this.setLinePartition(MathDefines.LINE_PARTITION_NOT_SET);
		return; // not initialized yet
	}
	int parentNucCount = refNuc.getParentSSData().getNucCount();

	// special cases
	if (parentNucCount == 1)
	{
		this.setLinePartition(MathDefines.LINE_PARTITION_DEGENERATE);
		return; // no concept of tail, head, midpt
	}
	if ((parentNucCount == 4) && ((refNuc.getID() == 2) || (refNuc.getID() == 3)))
	{
		this.setLinePartition(MathDefines.LINE_PARTITION_MID);
		return;
	}
	if (this.getNonDelineatedNucCount() == 1)
	{
		if ((refNuc.getID() == this.getFivePrimeNuc().getID()) ||
			(refNuc.getID() == this.getThreePrimeNuc().getID()))
		{
			this.setLinePartition(MathDefines.LINE_PARTITION_MID);
			return;
		}
		if (refNuc.getID() == this.getFivePrimeDelineateNuc().getID())
		{
			this.setLinePartition(MathDefines.LINE_PARTITION_TAIL);
			return;
		}
		if (refNuc.getID() == this.getThreePrimeDelineateNuc().getID())
		{
			this.setLinePartition(MathDefines.LINE_PARTITION_HEAD);
			return;
		}
	}

	int fivePrimeNucID = this.getFivePrimeNuc().getID();
	int threePrimeNucID = this.getThreePrimeNuc().getID();
	int refNucID = refNuc.getID();
	int midNucID = this.getMidLengthNuc().getID();
	if (refNucID == fivePrimeNucID)
	{
		this.setLinePartition(MathDefines.LINE_PARTITION_TAIL);
		return;
	}
	if (refNucID == midNucID)
	{
		this.setLinePartition(MathDefines.LINE_PARTITION_MID);
		return;
	}
	if (refNucID == threePrimeNucID)
	{
		this.setLinePartition(MathDefines.LINE_PARTITION_HEAD);
		return;
	}

	int fivePrimeEndDiff = Math.abs(fivePrimeNucID - refNucID);
	int threePrimeEndDiff = Math.abs(threePrimeNucID - refNucID);
	int midDiff = Math.abs(midNucID - refNucID);

	if ((fivePrimeEndDiff <= threePrimeEndDiff) &&
		(fivePrimeEndDiff <= midDiff))
	{
		this.setLinePartition(MathDefines.LINE_PARTITION_TAIL);
		return;
	}
	if ((threePrimeEndDiff <= fivePrimeEndDiff) &&
		(threePrimeEndDiff <= midDiff))
	{
		this.setLinePartition(MathDefines.LINE_PARTITION_HEAD);
		return;
	}
	if ((midDiff <= threePrimeEndDiff) &&
		(midDiff <= fivePrimeEndDiff))
	{
		this.setLinePartition(MathDefines.LINE_PARTITION_MID);
		return;
	}
	throw new Exception("ERROR IN setLinePartition: fivePrimeEndDiff: " + fivePrimeEndDiff +
		" threePrimeEndDiff: " + threePrimeEndDiff +
		" midDiff: " + midDiff);
}

// MathDefines.LINE_PARTITION_NOT_SET = -2;
// MathDefines.LINE_PARTITION_TAIL = -1;
// MathDefines.LINE_PARTITION_MID = 0;
// MathDefines.LINE_PARTITION_HEAD = 1;
// MathDefines.LINE_PARTITION_ERROR = 2;

public int
getLinePartition()
throws Exception
{
	return (this.linePartition);
}

public SSData
getParentSSData()
{
	return ((SSData)getRefNuc().getParentCollection());
}

// NEED a isConnectingArc and isLine


/* TO ADD

public boolean
strThreePrimeEndOfBPStrand(int nuc)
{
	if(!currStr.strNucIsBasePair(nuc))
		return(false);
	if(!currStr.strNucIsBasePair(nuc + 1))
		return(true);
	if(nuc == currStr.strNucsBasePair(nuc) - 1)
		return(true);
	if(!strNucsContiguousBasePairs(nuc, nuc + 1))
		return(true);
	return(false);
}

*/

/* TO ADD
// A connecting loop for now is any single stranded region that
// is not a hairpin loop.
public boolean
strNucInConnectingLoop(int nuc)
{
	if(currStr.strNucIsBasePair(nuc))
		return(false);
	getStrLoopEndNucs(nuc);
	getStrContiguousHelixEndNucs(loopFivePrimeEnd - 1);
	return(threePrimeStartNuc != loopThreePrimeEnd + 1);
}

public void
getStrLoopEndNucs(int nuc)
{
	int maxnuccount, findnuc = nuc;

	if(currStr.strNucIsBasePair(nuc))
	{
		loopFivePrimeEnd = loopThreePrimeEnd = 0;
		return;
	}
	maxnuccount = currStr.nucCount();
	while((findnuc <= maxnuccount) && !currStr.strNucIsBasePair(findnuc))
		findnuc++;
	findnuc--;
	loopThreePrimeEnd = findnuc;
	while((findnuc > 0) && !currStr.strNucIsBasePair(findnuc))
		findnuc--;
	loopFivePrimeEnd = findnuc + 1;
}

public void
getFormattedStrLoopEndNucs(int nuc)
{
	int loopnuc = nuc, maxnuccount;

	if((!currStr.strNucNodeAt(nuc).inSSNucs()) || currStr.strNucIsBasePair(nuc))
	{
		loopFivePrimeEnd = loopThreePrimeEnd = 0;
		return;
	}
	maxnuccount = currStr.nucCount();
	while((loopnuc <= maxnuccount) && (!currStr.strNucIsNilNuc(loopnuc)) &&
		currStr.strNucIsFormatted(loopnuc) && !currStr.strNucIsBasePair(loopnuc))
		loopnuc++;
	loopnuc--;
	loopThreePrimeEnd = loopnuc;
	while((loopnuc > 0) && (!currStr.strNucIsNilNuc(loopnuc)) &&
		currStr.strNucIsFormatted(loopnuc) && !currStr.strNucIsBasePair(loopnuc))
		loopnuc--;
	loopFivePrimeEnd = loopnuc + 1;
}
*/

public String
toString()
{
	return(
		"RNA Single Strand in: " + getParentCollection().toString() + "\n" +
		"5' delineate nuc: " + this.getFivePrimeDelineateNuc() + "\n" +
		"5' nuc: " + this.getFivePrimeNuc() + "\n" +
		"3' nuc: " + this.getThreePrimeNuc() + "\n" +
		"3' delineate nuc: " + this.getThreePrimeDelineateNuc()

		);
}

private void
debug(String s)
{
	System.err.println("RNASingleStrand-> " + s);
}

}
