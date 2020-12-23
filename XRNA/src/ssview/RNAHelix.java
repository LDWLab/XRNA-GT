package ssview;

import java.util.Vector;
import java.io.*;

import util.math.*;

public class
RNAHelix
extends NucCollection2D
{

// parent container should be of type ComplexSSDataCollection
// as any rna secondary structure element in a ComplexSSDataCollection
// can be part of a helix.

public
RNAHelix()
throws Exception
{
	super();
	initRNAHelix();	
}

public
RNAHelix(NucNode nuc)
throws Exception
{
	this();
	this.set(nuc);
}

// make a new helix in this sstr only
public
RNAHelix(SSData sstr, int refNucID, int bpNucID, int length)
throws Exception
{
	this();
	setBasePairs(sstr, refNucID, bpNucID, length);
	this.set(sstr.getNucAt(refNucID));
}

// make a new helix with arbitray nucs (could be in different strands)
public
RNAHelix(NucNode nuc, NucNode bpNuc, int length)
throws Exception
{
	this();
	setBasePairs(nuc, bpNuc, length);
	this.set(nuc);
}

public void
set(NucNode nuc)
throws Exception
{
	this.getHelixEndNucs(nuc);
	this.setStartBasePair();
	this.setParentCollection(nuc.getParentCollection());
}

public void
initRNAHelix()
throws Exception
{
	this.setBasePair(new RNABasePair());
}

public Vector
getHelixEndNucsVector(NucNode refNuc)
throws Exception
{
	getHelixEndNucs(refNuc);

	Vector helixEndNucsList = new Vector();
	helixEndNucsList.setSize(4);

	helixEndNucsList.setElementAt(getFivePrimeStartNuc(), 0);
	helixEndNucsList.setElementAt(getFivePrimeEndNuc(), 1);
	helixEndNucsList.setElementAt(getThreePrimeStartNuc(), 2);
	helixEndNucsList.setElementAt(getThreePrimeEndNuc(), 3);

	return (helixEndNucsList);
}

public void
getHelixEndNucs(NucNode refNuc)
throws Exception
{
	if (refNuc == null)
		throw new ComplexException(
			"Error RNAHelix.getHelixEndNucs()",
			ComplexDefines.RNA_HELIX_ERROR +
				ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR,
			ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
			"trying to set helix with null reference nuc");
		/*
		throw new Exception("Error in RNAHelix constructor: trying set helix" +
			" with null reference nuc");
		*/

	// NEED to look into this reset buisness
	// boolean reset = (refNuc.getParentSSData()).getCurrentItem() == refNuc;
	boolean reset = false;

	setRefNuc(refNuc);
	setProbeNuc(refNuc);

	if (!getRefNuc().isBasePair())
		throw new ComplexException(
			"Error RNAHelix.getHelixEndNucs()",
			ComplexDefines.RNA_HELIX_ERROR +
				ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR,
			ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
			"trying to set helix with non base paired reference nuc");
		/*
		throw new Exception("Error in RNAHelix constructor: trying set helix" +
			" with non base paired reference nuc");
		*/

	setBPRefNuc(refNuc.getBasePair());
	setBPProbeNuc(refNuc.getBasePair());
	setHelixEndNucs();

	// undo any change to current nuc if caller is iterating
	if (reset)
		refNuc.setParentCurrentItem();
	/*
	debug("FP END: " + getFivePrimeEndNuc().getID());
	debug("FP START: " + getFivePrimeStartNuc().getID());
	*/
	setLength(getFivePrimeEndNuc().getID() -
		getFivePrimeStartNuc().getID() + 1);
}

private int length = 0;

public void
setLength(int l)
{
	length = l;
}

public int
getLength()
{
	return (length);
}

public int
length()
{
	return (this.getLength());
}

private NucNode refNuc = null;

public void
setRefNuc(NucNode nuc)
{
	refNuc = nuc;
}

public NucNode
getRefNuc()
{
	return (refNuc);
}

private NucNode probeNuc = null;

public void
setProbeNuc(NucNode nuc)
{
	probeNuc = nuc;
}

public NucNode
getProbeNuc()
{
	return (probeNuc);
}

private NucNode bpRefNuc = null;

public void
setBPRefNuc(NucNode nuc)
{
	bpRefNuc = nuc;
}

public NucNode
getBPRefNuc()
{
	return (bpRefNuc);
}

private NucNode bpProbeNuc = null;

public void
setBPProbeNuc(NucNode nuc)
{
	bpProbeNuc = nuc;
}

public NucNode
getBPProbeNuc()
{
	return (bpProbeNuc);
}

private NucNode fivePrimeStartNuc = null;

public void
setFivePrimeStartNuc(NucNode nuc)
{
	fivePrimeStartNuc = nuc;
}

public NucNode
getFivePrimeStartNuc()
{
	return (fivePrimeStartNuc);
}

private NucNode fivePrimeEndNuc = null;

public void
setFivePrimeEndNuc(NucNode nuc)
{
	fivePrimeEndNuc = nuc;
}

public NucNode
getFivePrimeEndNuc()
{
	return (fivePrimeEndNuc);
}

private NucNode threePrimeStartNuc = null;

public void
setThreePrimeStartNuc(NucNode nuc)
{
	threePrimeStartNuc = nuc;
}

public NucNode
getThreePrimeStartNuc()
{
	return (threePrimeStartNuc);
}

private NucNode threePrimeEndNuc = null;

public void
setThreePrimeEndNuc(NucNode nuc)
{
	threePrimeEndNuc = nuc;
}

public NucNode
getThreePrimeEndNuc()
{
	return (threePrimeEndNuc);
}

public SSData
getFivePrimeNucParent()
{
	return ((SSData)this.getFivePrimeStartNuc().getParentNucCollection2D());
}

public SSData
getThreePrimeNucParent()
{
	return ((SSData)this.getThreePrimeStartNuc().getParentNucCollection2D());
}

public boolean
isSelfRef()
{
	return (this.getFivePrimeNucParent() == this.getThreePrimeNucParent());
}

public boolean
hasMisMatchedBasePairs()
throws Exception
{
	this.setStartBasePair();
	while (true)
	{
		if (this.getBasePair().isMisMatch())
			return (true);
		if (this.isLastBasePair())
			return (false);
		this.setNextBasePair();
	}	
}

public void
setHelixEndNucs()
throws Exception
{
	setFivePrimeStartNuc(findFivePrimeStartNuc());
	setFivePrimeEndNuc(findFivePrimeEndNuc());
	setThreePrimeStartNuc(findThreePrimeStartNuc());
	setThreePrimeEndNuc(findThreePrimeEndNuc());
}

public void
checkSelfRefBP(boolean need5pSide)
{
	if (getRefNuc().isSelfRefBasePair())
	{
		if (need5pSide && (getRefNuc().getID() > getBPRefNuc().getID()))
		{
			// then 5' side is lower numbered nuc side, so swap
			setProbeNuc(getBPRefNuc());
			setBPProbeNuc(getRefNuc().getBasePair());
		}
		else if (need5pSide)
		{
			// just set probe to ref
			setProbeNuc(getRefNuc());
			setBPProbeNuc(getBPRefNuc());
		}
		else if ((!need5pSide) && (getRefNuc().getID() < getBPRefNuc().getID()))
		{
			// then 3' side is lower numbered nuc side, so swap
			setProbeNuc(getBPRefNuc());
			setBPProbeNuc(getRefNuc().getBasePair());
		}
		else if (!need5pSide)
		{
			// just set probe to ref
			setProbeNuc(getRefNuc());
			setBPProbeNuc(getBPRefNuc());
		}
	}
	else
	{
		if (need5pSide)
		{
			// just set probe to ref
			setProbeNuc(getRefNuc());
			setBPProbeNuc(getBPRefNuc());
		}
		else
		{
			// then 3' side is on different strand
			setProbeNuc(getBPRefNuc());
			setBPProbeNuc(getRefNuc().getBasePair());
		}
	}
}

// find the beginning of the fiveprime side of a helix
public NucNode
findFivePrimeStartNuc()
throws Exception
{
	checkSelfRefBP(true);
	SSData sstr = getProbeNuc().getParentSSData();

	if (sstr == null)
		throw new Exception("Error in RNAHelix.findFivePrimeStartNuc(): " +
			"Probe nuc needs to be in a SSData collection");

	// set refNuc as sstr's currentNuc
	getProbeNuc().setParentCurrentItem();

	if (sstr.atBeginOfItemList())
		return (getProbeNuc());

	while (
		(!sstr.atBeginOfItemList()) &&
		sstr.currentNucIsBasePair() &&
		sstr.currentNucsBP().isBasePair() &&
		((NucNode)sstr.lastNonNullNuc()).isBasePair() &&
		((sstr.lastNonNullNuc().getID() + 1 ) ==
			sstr.getCurrentItem().getID()) &&
		((((NucNode)sstr.lastNonNullNuc()).getBasePair().getID() - 1) ==
			sstr.currentNucsBP().getID())
		)
	{
		((NucNode)sstr.getLastNonNullNuc()).setParentCurrentItem();
	}

	/*
	debug("IN HELIX, returning: " + sstr.getCurrentItem().getID() +
		" for nuc: " + getProbeNuc().getID());
	*/
	return ((NucNode)sstr.getCurrentItem());
}

public NucNode
findFivePrimeEndNuc()
throws Exception
{
	checkSelfRefBP(true);
	SSData sstr = getProbeNuc().getParentSSData();

	// set refNuc as sstr's currentNuc
	getProbeNuc().setParentCurrentItem();

	if (sstr.atEndOfItemList())
		return (getProbeNuc());

	// check if single bp helix
	if ((getProbeNuc().getID() + 1) == getProbeNuc().getBasePair().getID())
		return (getProbeNuc());

	while (
		(!sstr.atEndOfItemList()) &&
		sstr.currentNucIsBasePair() &&
		sstr.currentNucsBP().isBasePair() &&
		((NucNode)sstr.nextNonNullNuc()).isBasePair() &&
		((sstr.nextNonNullNuc().getID() - 1 ) ==
			sstr.getCurrentItem().getID()) &&
		((((NucNode)sstr.nextNonNullNuc()).getBasePair().getID() + 1) ==
			((NucNode)sstr.currentNucsBP()).getID())
		)
	{
		NucNode testNuc = (NucNode)sstr.getCurrentItem();
		// check for empty hairpin
		if ((testNuc.getID() + 1) == testNuc.getBasePair().getID())
			break;
		((NucNode)sstr.getNextNonNullNuc()).setParentCurrentItem();
	}

	return ((NucNode)sstr.getCurrentItem());
}

public NucNode
findThreePrimeStartNuc()
throws Exception
{
	checkSelfRefBP(false);
	SSData sstr = getProbeNuc().getParentSSData();

	// check special cases:
	// 1) sstr is a single basepair:
	if (sstr.getNucCount() == 2)
		return (sstr.getNucAt(2));

	// set refNuc as sstr's currentNuc
	getProbeNuc().setParentCurrentItem();

	if (sstr.atBeginOfItemList())
	{
		return (getProbeNuc());
	}

	// check if single bp helix
	if ((getProbeNuc().getID() - 1) == getProbeNuc().getBasePair().getID())
		return (getProbeNuc());

	while (
		(!sstr.atBeginOfItemList()) &&
		sstr.currentNucIsBasePair() &&
		sstr.currentNucsBP().isBasePair() &&
		((NucNode)sstr.lastNonNullNuc()).isBasePair() &&
		((sstr.lastNonNullNuc().getID() + 1) ==
			sstr.getCurrentItem().getID()) &&
		((((NucNode)sstr.lastNonNullNuc()).getBasePair().getID() - 1) ==
			((NucNode)sstr.currentNucsBP()).getID())
		)
	{
		NucNode testNuc = (NucNode)sstr.getCurrentItem();
		// debug("testNuc: " + testNuc.getID());
		if ((testNuc.getID() - 1) == testNuc.getBasePair().getID())
			break;
		((NucNode)sstr.getLastNonNullNuc()).setParentCurrentItem();
	}

	return ((NucNode)sstr.getCurrentItem());
}

public NucNode
findThreePrimeEndNuc()
throws Exception
{
	checkSelfRefBP(false);
	SSData ssData = getProbeNuc().getParentSSData();

	// set refNuc as ssData's currentNuc
	getProbeNuc().setParentCurrentItem();

	if (ssData.atEndOfItemList())
		return (getProbeNuc());

	while (
		(!ssData.atEndOfItemList()) &&
		ssData.currentNucIsBasePair() &&
		ssData.currentNucsBP().isBasePair() &&
		((NucNode)ssData.nextNonNullNuc()).isBasePair() &&
		((ssData.nextNonNullNuc().getID() - 1 ) ==
			ssData.getCurrentItem().getID()) &&
		((((NucNode)ssData.nextNonNullNuc()).getBasePair().getID() + 1) ==
			ssData.currentNucsBP().getID())
		)
	{
		((NucNode)ssData.getNextNonNullNuc()).setParentCurrentItem();
	}

	/*
	debug("IN HELIX.findThreePrimeEndNuc(), returning: " +
		ssData.getCurrentItem().getID() + " for nuc: " + getProbeNuc().getID() + " " + getProbeNuc().getParentCollection().getName());
	*/
	return ((NucNode)ssData.getCurrentItem());
}

private RNABasePair basePair = null;

public void
setBasePair(RNABasePair basePair)
{
    this.basePair = basePair;
}

public RNABasePair
getBasePair()
{
    return (this.basePair);
}

public void
setStartBasePair()
throws Exception
{
	this.getBasePair().set(this.getFivePrimeStartNuc());
}

public void
setNextBasePair()
throws Exception
{
	if (this.getBasePair().getFivePrimeNuc().getID() <
		this.getFivePrimeEndNuc().getID())
		this.getBasePair().set(this.getBasePair().getFivePrimeNuc().nextNuc());
	// else leave getBasePair and end of helix
}

public void
setLastBasePair()
throws Exception
{
	this.getBasePair().set(this.getThreePrimeStartNuc());
}

public boolean
isLastBasePair()
{
	return (this.getBasePair().getFivePrimeNuc().getID() ==
		this.getFivePrimeEndNuc().getID());
}

// Dosn't work yet; need to think about every cycle entry helix
// is also a non level 0 exit helix for another cycle.
public boolean
isCycleEntryHelix()
{
	try
	{
		if (this.getFivePrimeStartNuc() == null)
			return (false);
		RNACycle cycle = new RNACycle(this.getFivePrimeStartNuc());
		if (cycle == null)
			return (false);
		if (cycle.getEntryHelix() == null)
			return (false);
		if (cycle.getEntryHelix().getFivePrimeStartNuc() == null)
			return (false);
		/*
		debug("MADE IT HERE: " +
			cycle.getEntryHelix().getFivePrimeStartNuc().getID() + " " +
			this.getFivePrimeStartNuc().getID());
		*/
		return (cycle.getEntryHelix().getFivePrimeStartNuc() ==
			this.getFivePrimeStartNuc());
	}
	catch (Exception e)
	{
		return (false);
	}
}

// helix terminates with 0 nucs in hairpin
public boolean
isEmptyHairPin()
throws Exception
{
	/*
	debug("helix: " + this);
	debug("5' endNuc: " + this.getFivePrimeEndNuc().getID());
	debug("3' startNuc: " + this.getThreePrimeStartNuc().getID());
	*/
	return (this.isSelfRef() &&
		((this.getFivePrimeEndNuc().getID() + 1) ==
			this.getThreePrimeStartNuc().getID()));
}

public int
hairPinLength()
{
	return (this.getThreePrimeStartNuc().getID() -
		this.getFivePrimeEndNuc().getID() - 1);
}

// This should allow for hairpins of length 0
public boolean
isHairPin()
throws Exception
{
	// seems like can't have a hairpin if basepairs aren't
	// in same collection.

	if (!this.getFivePrimeEndNuc().isSelfRefBasePair())
		return (false);
	
	SSData sstr = this.getFivePrimeNucParent();

	// check if any nucs in hairpin (maybe a single basepair helix)
	if ((this.getFivePrimeStartNuc().getID() + 1) ==
		this.getThreePrimeEndNuc().getID())
		return (false);

	// check if helix is hairpin of length 0
	if ((this.getFivePrimeEndNuc().getID() + 1) ==
		this.getThreePrimeStartNuc().getID())
		return (true); // since allowing for 0 length hairpin

	// NEED to decide to do with single helix structure that
	// has all null nucs in hairpin. for now just make a check
	// and return false if all nucs between endpts of helix are null.
	/*
	NucNode probeNuc = this.getFivePrimeEndNuc().nextNonNullNuc();
	if (probeNuc == this.getThreePrimeStartNuc())
		return (false);
	*/

	// this should cover case where all nucs inbetween endnucs are null
	boolean hairpinNucFound = false;

	for (int nucID = this.getFivePrimeEndNuc().getID() + 1;
		nucID < this.getThreePrimeStartNuc().getID();nucID++)
	{
		NucNode refNuc = sstr.getNucAt(nucID);
		if (refNuc == null)
			continue;
		if (refNuc.isBasePair())
			return (false);
		hairpinNucFound = true;
	}

	return (hairpinNucFound);
}

public boolean
isSingleBasePairHelix()
{
	return (this.getLength() == 1);
}

public RNASingleStrand
getHairPin()
throws Exception
{
	if (!isHairPin())
		return (null);
	this.getFivePrimeNucParent().setCurrentID(this.getFivePrimeEndNuc());
	return (new RNASingleStrand(this.getFivePrimeNucParent().nextNonNullNuc()));
}

/* TO ADD
public boolean
strThreePrimeEndOfBPStrand(int nuc)
{
	if(!currStr.strNucIsBasePair(nuc))
		return (false);
	if(!currStr.strNucIsBasePair(nuc + 1))
		return (true);
	if(nuc == currStr.strNucsBasePair(nuc) - 1)
		return (true);
	if(!strNucsContiguousBasePairs(nuc, nuc + 1))
		return (true);
	return (false);
}

*/

/* NOT SURE WHAT THIS DOES
public int
getNextContiguousHelix(int nuc)
{
	int fpnuc, tpnuc, maxnuccount;

	if(!strNucNodeAt(nuc).inSSNucs())
			return (0);
	maxnuccount = currStr.nucCount();
	getStrContiguousHelixEndNucs(nuc);
	fpnuc = fivePrimeEndNuc + 1;
	tpnuc = threePrimeStartNuc - 1;
	while((!currStr.strNucIsBasePair(fpnuc)) &&
			(fpnuc < maxnuccount) && (fpnuc < tpnuc))
			fpnuc++;
	if(fpnuc == tpnuc)
			return (0);
	while((!currStr.strNucIsBasePair(tpnuc)) &&
			(tpnuc > 1) && tpnuc > fpnuc)
			tpnuc--;
	if(tpnuc == fpnuc)
			return (0);
	if((currStr.strNucIsBasePair(fpnuc) && currStr.strNucIsBasePair(tpnuc)) &&
			(fpnuc < tpnuc) && (currStr.strNucsBasePair(fpnuc) == tpnuc) &&
			//IsValidFormattedNuc(fpnuc) &&
			strBasePairInHelix(fpnuc))
			return (fpnuc);
	return (0);
}
*/


/* TO ADD
public void
getStrHelixAndLoopEndNucs(int hp[])
{
	int fiveend, threeend;
 
	getStrContiguousHelixEndNucs(refNuc);
	hp[1] = fivePrimeStartNuc;
	hp[2] = fivePrimeEndNuc;
	hp[5] = threePrimeStartNuc;
	hp[6] = threePrimeEndNuc;
	getFormattedStrLoopEndNucs(hp[1] - 1);
	hp[0] = loopFivePrimeEnd > 1 ? loopFivePrimeEnd - 1 : hp[1] - 1;
	getFormattedStrLoopEndNucs(hp[2] + 1);
	hp[3] = loopThreePrimeEnd != 0 ? loopThreePrimeEnd + 1 : hp[2] + 1;
	getFormattedStrLoopEndNucs(hp[5] - 1);
	hp[4] = loopFivePrimeEnd != 0 ? loopFivePrimeEnd - 1 : hp[5] - 1;
	getFormattedStrLoopEndNucs(hp[6] + 1);
	hp[7] = loopThreePrimeEnd != 0 ? loopThreePrimeEnd + 1 : hp[6] + 1;
}

// BUG: doesn't deal properly with end nucs from other strands
public void
getStrContiguousHelixEndNucs(int nuc)
{
	if(!currStr.strNucIsBasePair(refNuc))
	{
		fivePrimeStartNuc = fivePrimeEndNuc = threePrimeStartNuc = threePrimeEndNuc = 0;
		return;
	}
	while(!strFivePrimeEndOfBPStrand(nuc))
		nuc--;
	if(nuc < currStr.strNucsBasePair(nuc))
	{
		fivePrimeStartNuc = nuc;
		threePrimeEndNuc = currStr.strNucsBasePair(nuc);
	}
	else
	{
		threePrimeStartNuc = nuc;
		fivePrimeEndNuc = currStr.strNucsBasePair(nuc);
	}
	while(!strThreePrimeEndOfBPStrand(nuc))
		nuc++;
	if(nuc < currStr.strNucsBasePair(nuc))
	{
		fivePrimeEndNuc = nuc;
		threePrimeStartNuc = currStr.strNucsBasePair(nuc);
	}
	else
	{
		threePrimeEndNuc = nuc;
		fivePrimeStartNuc = currStr.strNucsBasePair(nuc);
	}
}

public boolean
strBasePairInHelix()
{
	if(!currStr.strNucIsBasePair(refNuc))
		return (false);

	// maybe take out
	getStrContiguousHelixEndNucs(refNuc);

	return (fivePrimeStartNuc != fivePrimeEndNuc);
}

public boolean
strNucInHairPinHelix() //nuc is basepaired but helix has hairpin
{
	if(!currStr.strNucIsBasePair(refNuc))
		return (false);

	// maybe take out
	getStrContiguousHelixEndNucs(refNuc);

	for(int hpnuc = fivePrimeEndNuc + 1;hpnuc < threePrimeStartNuc;hpnuc++)
	{
		if(currStr.strNucIsBasePair(hpnuc))
			return (false);
	}
	return (true);
}


// PUT INTO LOOP SECTION
// is nuc in hairpin?

public boolean
strNucInHairPinLoop(int nuc)
{
	if(currStr.strNucIsBasePair(nuc))
		return (false);
	getStrLoopEndNucs(nuc);
	getStrContiguousHelixEndNucs(loopFivePrimeEnd - 1);
	return (threePrimeStartNuc == loopThreePrimeEnd + 1);
}

// PUT INTO LOOP SECTION
// A connecting loop for now is any single stranded region that
// is not a hairpin loop.
public boolean
strNucInConnectingLoop(int nuc)
{
	if(currStr.strNucIsBasePair(nuc))
		return (false);
	getStrLoopEndNucs(nuc);
	getStrContiguousHelixEndNucs(loopFivePrimeEnd - 1);
	return (threePrimeStartNuc != loopThreePrimeEnd + 1);
}


// length of any non-basepaired strand
public int
strLoopSize(int nuc)
{
	if(currStr.strNucIsBasePair(nuc))
		return (0);
	getStrLoopEndNucs(nuc);
	return (loopThreePrimeEnd - loopFivePrimeEnd + 1);
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

public void
printXML(PrintWriter out)
throws Exception
{
	out.print("<BasePairs nucID='" + getFivePrimeStartNuc().getID() +
		"' length='" + getLength() + "' ");
	out.print("bpNucID='" + getThreePrimeEndNuc().getID() + "'");
	if (!this.getFivePrimeStartNuc().isSelfRefBasePair())
		out.print(" bpName='" + refNuc.getBasePairSStrName() + "'");
	out.println(" />");
}


// have to worry about base pairing across different strands (maybe).
public Vector
getItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();
	NucNode nuc = null;
	boolean inNucs = false;

	if (this.getLength() < 1)
		return (null);

	if ((this.getLength() == 1) && this.isHairPin())
	{
		// single base pair with a hairpin
		delineators.add(this.getFivePrimeStartNuc());
		delineators.add(this.getThreePrimeEndNuc());
	}
	else if (this.getLength() == 1)
	{
		if (this.getFivePrimeStartNuc() == null)
			return (null);
		delineators.add(this.getFivePrimeStartNuc());
		delineators.add(this.getFivePrimeStartNuc());
		if (this.getThreePrimeStartNuc() == null)
			return (null);
		delineators.add(this.getThreePrimeEndNuc());
		delineators.add(this.getThreePrimeEndNuc());
		return (delineators);
	}

	// THIS IS TMP ONLY:
	if (this.isHairPin())
	{
		delineators.add(this.getFivePrimeStartNuc());
		delineators.add(this.getThreePrimeEndNuc());
	}
	else
	{
		delineators.add(this.getFivePrimeStartNuc());
		delineators.add(this.getFivePrimeEndNuc());
		delineators.add(this.getThreePrimeStartNuc());
		delineators.add(this.getThreePrimeEndNuc());
	}

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

	return (delineators);
}

public void
unsetBasePairs()
throws Exception
{
	this.setStartBasePair();
	while (true)
	{
		this.getBasePair().unset();
		if (this.isLastBasePair())
			break;
		this.setNextBasePair();
	}
}

public String
toString()
{
	StringBuffer strBuf = new StringBuffer();
	strBuf.append("RNA Helix in: ");
	strBuf.append(getParentCollection().toString());
	if (!this.getRefNuc().isSelfRefBasePair())
		strBuf.append(" and: " + this.getRefNuc().getBasePair().getParentCollection().toString());
	strBuf.append("\n");
	strBuf.append("5' start: " + this.getFivePrimeStartNuc() + "\n");
	strBuf.append("5' end: " + this.getFivePrimeEndNuc() + "\n");
	strBuf.append("3' start: " + this.getThreePrimeStartNuc() + "\n");
	strBuf.append("3' end: " + this.getThreePrimeEndNuc() + "\n");
	strBuf.append("\n");

	return (strBuf.toString());
}

private void
debug(String s)
{
	System.err.println("RNAHelix-> " + s);
}

}
