package ssview;

import java.util.Vector;
import java.io.*;

import util.math.*;

public class
RNACycle
extends NucCollection2D
{
// parent container should be of type SSData

public
RNACycle()
throws Exception
{
	super();
}

public
RNACycle(NucNode refNuc)
throws Exception
{
	this();
	this.set(refNuc);
}

public void
set(NucNode refNuc)
throws Exception
{
	if (refNuc == null)
		throw new Exception("Error in RNASingleStrand constructor: trying set singlestrand" +
			" with null reference nuc");

	this.setRefNuc(refNuc);
	if (refNuc.isBasePair())
		this.setRefHelix(new RNAHelix(refNuc));
	this.reset();
}

public void
reset()
throws Exception
{
	boolean debugOn = false;
	/*
	if (this.getRefNuc().getID() == 6)
		debugOn = true;
	*/
	if (debugOn) debug("HERE 0 IN RESET");
	this.setParentCollection(this.getRefNuc().getParentSSData());
	if (debugOn) debug("HERE 1 IN RESET");
	this.setCycleHelices(SSData.getCycleHelices(this.getRefNuc()));
	if (debugOn) debug("HERE 2 IN RESET");
	this.setItemListDelineators(this.resetItemListDelineators());
	if (debugOn) debug("HERE 3 IN RESET");
	if (this.getCycleHelices() == null)
		return;
	if (debugOn) debug("HERE 4 IN RESET");
	this.setExitHelicesCount(this.getCycleHelices().size() - 1);

	this.setCycleNucs(this.resetCycleNucs(this.getRefNuc(), debugOn));
	// this.resetCycleSSNucCount(debugOn);
	this.newResetCycleSSNucCount(this.getRefNuc(), debugOn);
}

public void
newResetCycleSSNucCount(NucNode refNuc, boolean debugOn)
throws Exception
{
	if (debugOn) debug("In newResetCycleSSNucCOunt, refNuc: " + refNuc);
	Vector cycleNucList = this.getCycleNucs();
	int count = 0;
	if (debugOn) debug("IN newResetCycleSSNucCount, cycleNucList.size(): " + cycleNucList.size());
	for (int i = 0;i < cycleNucList.size();i++)
	{
		NucNode nuc = (NucNode)cycleNucList.elementAt(i);
		if (debugOn) debug("IN newResetCycleSSNucCount: " + nuc);
		if (nuc.isSingleStranded())
			count++;
	}
	this.setCycleSSNucCount(count);
}

// count of all single stranded nucs in cycle only
private int cycleSSNucsCount = 0;

public void
setCycleSSNucCount(int cycleSSNucsCount)
{
    this.cycleSSNucsCount = cycleSSNucsCount;
}

public int
getCycleSSNucCount()
{
    return (this.cycleSSNucsCount);
}

private Vector cycleNucs = null;

public void
setCycleNucs(Vector cycleNucs)
{
    this.cycleNucs = cycleNucs;
}

public Vector
getCycleNucs()
{
    return (this.cycleNucs);
}

// assert that all nucs involved live in same parent (for now)
public Vector
resetCycleNucs(NucNode refNuc, boolean debugOn)
throws Exception
{
	if (debugOn) debug("In getCycleNucs(), refNuc: " + refNuc);
	if (refNuc == null)
		return (null);
	
	SSData sstr = refNuc.getParentSSData();
	NucNode firstValidNuc = sstr.getFirstNonNullNuc();
	Vector nucList = new Vector();
	if (sstr.getNucCount() == 1)
	{
		nucList.add(sstr.getNucAt(1));
		return (nucList);
	}

	RNAHelix helix = new RNAHelix();
	RNABasePair basePair = new RNABasePair();
	// NucNode startNuc = refNuc;
	NucNode startNuc = this.getFivePrimeNuc();
	NucNode probeNuc = startNuc;
	if (debugOn) debug("In getCycleNucs(), startNuc: " + startNuc);

	// NEED to deal with this:
	//
	// if (probeNuc.isSingleBasePairHelix())
		// probeNuc = probeNuc.nextNonNullNuc();
	//

	// CYCLES
	while (true)
	{
		if (debugOn) debug("PROBE NUC NOW: " + probeNuc.getID());
		if (probeNuc.isSingleStranded())
		{
			if (debugOn) debug("ADDING SS");
			nucList.add(probeNuc);
			probeNuc = probeNuc.nextNonNullNuc();
		}
		else if (probeNuc.isSingleBasePairHelix())
		{
			nucList.add(probeNuc);
			basePair.set(probeNuc);
			if (probeNuc == basePair.getFivePrimeNuc())
			{
				if (debugOn) debug("ADDING 5' SBPH");
				if (this.atCycle0() && (probeNuc == startNuc) &&
					(probeNuc == firstValidNuc))
				{
					// then entering single BP exit helix at level 0
					probeNuc = probeNuc.getBasePair();
				}
				else if (this.atCycle0())
				{
					probeNuc = probeNuc.getBasePair();
				}
				else if (probeNuc == this.getEntryHelix().getFivePrimeEndNuc())
				{
					// then exiting single BP entry helix
					probeNuc = probeNuc.nextNonNullNuc();
				}
				else
				{
					// then entering single BP exit helix
					probeNuc = probeNuc.getBasePair();
				}
			}
			else if (probeNuc == basePair.getThreePrimeNuc())
			{
				if (debugOn) debug("ADDING 3' SBPH");
				if ((!this.atCycle0()) && (probeNuc != this.getEntryHelix().getThreePrimeStartNuc()))
				{
					// then in exiting exit helix
					probeNuc = probeNuc.nextNonNullNuc();
				}
				else if (this.atCycle0())
				{
					// then in exiting exit helix
					probeNuc = probeNuc.nextNonNullNuc();
				}
				else
				{
					// then entering single BP exit helix
					probeNuc = probeNuc.getBasePair();
				}
			}
		}
		else
		{
			if (debugOn) debug("ADDING HELIX");
			helix.set(probeNuc);
			if (probeNuc == helix.getFivePrimeEndNuc())
			{
				// then in exiting entry helix; don't add
				nucList.add(probeNuc);
				probeNuc = probeNuc.nextNonNullNuc();
			}
			else if (probeNuc == helix.getFivePrimeStartNuc())
			{
				// then in entering exit helix
				nucList.add(probeNuc);
				probeNuc = probeNuc.getBasePair();
			}
			else if (probeNuc == helix.getThreePrimeEndNuc())
			{
				// then in exiting exit helix
				nucList.add(probeNuc);
				probeNuc = probeNuc.nextNonNullNuc();
			}
			else if (probeNuc == helix.getThreePrimeStartNuc())
			{
				// then in entering entry helix
				nucList.add(probeNuc);
				probeNuc = probeNuc.getBasePair();
			}
		}
		// STILL NEED to account for non self ref
		// if (probeNuc.isSelfRefBasePair())
			// probeNuc = probeNuc.getBasePair();
		//
		if (probeNuc.equals(startNuc))
			break;
	}
	if (debugOn)
	{
		for (int i = 0;i < nucList.size();i++)
			debug("NUCLIST AT " + i + ": " + ((NucNode)nucList.elementAt(i)));
	}
	return (nucList);
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

private RNAHelix refHelix = null;

public void
setRefHelix(RNAHelix refHelix)
{
	this.refHelix = refHelix;
}

public RNAHelix
getRefHelix()
{
	return (this.refHelix);
}

private Vector levelHelices = null;

public void
setCycleHelices(Vector levelHelices)
{
    this.levelHelices = levelHelices;
}

public Vector
getCycleHelices()
{
    return (this.levelHelices);
}

public boolean
atCycle0()
{
	if (this.getCycleHelices() == null)
		return (false);
	return (levelHelices.elementAt(0) == null);
}

public boolean
isHelicalRun()
{
	return ((!this.atCycle0()) && (this.getExitHelicesCount() == 1));
}

public boolean
isBulgedNucCycle()
{
	return (this.isHelicalRun() && (this.getCycleSSNucCount() == 1));
}

private int exitHelixCount = 0;

public void
setExitHelicesCount(int exitHelixCount)
{
    this.exitHelixCount = exitHelixCount;
}

public int
getExitHelicesCount()
{
    return (this.exitHelixCount);
}

public RNAHelix
getEntryHelix()
throws Exception
{
	if (this.atCycle0())
		return (null);
	return (new RNAHelix((NucNode)this.getCycleHelices().elementAt(0)));
}

public RNAHelix
getFirstExitHelix()
throws Exception
{
	return (new RNAHelix((NucNode)this.getCycleHelices().elementAt(1)));
}

public RNAHelix
getLastExitHelix()
throws Exception
{
	return (new RNAHelix((NucNode)this.getCycleHelices().elementAt(this.getExitHelicesCount())));
}

public NucNode
getFivePrimeNuc()
throws Exception
{
	if (this.atCycle0())
		// return (((SSData)this.getParentCollection()).getNucAt(1));
		return (((SSData)this.getParentCollection()).getFirstNonNullNuc());
	/*
	if (this.atCycle0())
	{
		NucNode nuc = ((SSData)this.getParentCollection()).getFirstNonNullNuc();
		// debug("FIRST NONNULL NUC: " + nuc);
		return (nuc);
	}
	*/

	return (this.getEntryHelix().getFivePrimeEndNuc());
}

public NucNode
getThreePrimeNuc()
throws Exception
{
	if (this.atCycle0())
	{
		SSData sstr = (SSData)this.getParentCollection();
		return (sstr.getEndNonNullNuc());
	}
	return (this.getEntryHelix().getThreePrimeStartNuc());
}

private Vector itemListDelineators = null;

public void
setItemListDelineators(Vector itemListDelineators)
{
    this.itemListDelineators = itemListDelineators;
}

public Vector
getItemListDelineators()
{
    return (this.itemListDelineators);
}

// used to get a delineated list of non null nucs
public Vector
resetItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();
	boolean inNucs = false;
	RNAHelix entryHelix = null;
	RNAHelix currentExitHelix = null;
	NucNode refNuc = null;
	SSData sstr = (SSData)this.getParentCollection();

	if (this.getCycleHelices() == null) // then no structure
	{
		delineators.add(sstr.getNucAt(1));
		delineators.add(sstr.getNucAt(sstr.getNucCount()));
		return (delineators);
	}

	refNuc = (NucNode)this.getCycleHelices().elementAt(0);
	if (refNuc == null) // then no entry helix: at level 0
	{
		delineators.add(sstr.getNucAt(1));
	}
	else
	{
		entryHelix = new RNAHelix(refNuc);
		delineators.add(entryHelix.getFivePrimeStartNuc());
	}
	// debug("exitHelixCount: " + this.getExitHelicesCount());
	for (int i = 1;i <= this.getExitHelicesCount();i++)
	{
		NucNode nuc = (NucNode)this.getCycleHelices().elementAt(i);
		currentExitHelix = new RNAHelix(nuc);
		if (currentExitHelix.isHairPin())
			continue;
		delineators.add(currentExitHelix.getFivePrimeEndNuc());
		delineators.add(currentExitHelix.getThreePrimeStartNuc());
	}
	if (entryHelix != null)
		delineators.add(entryHelix.getThreePrimeEndNuc());
	else if ((currentExitHelix != null) && (!currentExitHelix.isHairPin()))
		delineators.add(currentExitHelix.getThreePrimeEndNuc());

	if (refNuc == null) // then no entry helix: at level 0
	{
		delineators.add(sstr.getNucAt(sstr.getNucCount()));
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

public String
toString()
{
	try
	{
		StringBuffer nucStringBuf = new StringBuffer();

		if (this.atCycle0())
			nucStringBuf.append("At RNA cycle level 0");
		else
			nucStringBuf.append("RNA cycle");
		if ((this.getRefNuc() != null) && (this.getRefNuc().getParentSSData() != null))
			nucStringBuf.append(" in RNA strand: " + this.getRefNuc().getParentSSData().getName());

		if (this.getEntryHelix() != null)
			nucStringBuf.append("\nentry helix: " + this.getEntryHelix().getFivePrimeStartNuc().getID());
		if (this.getFivePrimeNuc() != null)
			nucStringBuf.append("\n5' nuc: " + this.getFivePrimeNuc().getID());
		if (this.getThreePrimeNuc() != null)
			nucStringBuf.append("\n3' nuc: " + this.getThreePrimeNuc().getID());

		return (nucStringBuf.toString());
	}
	catch (Exception e)
	{
		return ("Error in RNACycle2D.toString(): " + e.toString());
	}
}

private void
debug(String s)
{
	System.err.println("RNACycle-> " + s);
}

}
