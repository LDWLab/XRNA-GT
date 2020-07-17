package ssview;

import java.util.Vector;
import java.io.*;

import util.math.*;

// currently a rna stacked helix is made up of one or two strands.
// if two strands then one strand will always be 3' side for all
// helices.
// For now assuming that if two strands then one will be the 5' side
// for all helices in stacked helices. This should be checked out.
// stacked helix is a set of contiguous helices.
// reference helix is lowest numbered five prime start nuc of all
// helices in helical run and probably starts and stops at
// cycles with more than one exit helix.

public class
RNAStackedHelix
extends NucCollection2D
{

public
RNAStackedHelix()
throws Exception
{
	super();
}

public
RNAStackedHelix(NucNode nuc)
throws Exception
{
	this();
	this.set(nuc);
}

public
RNAStackedHelix(SSData sstr, int nucID)
throws Exception
{
	this();
	this.set(sstr.getNucAt(nucID));
}

public void
set(NucNode refNuc)
throws Exception
{
	if (refNuc.isBasePair())
		this.setRefHelix(new RNAHelix(refNuc));

	this.setParentCollection(refNuc.getParentCollection());

	Vector levelHelices = null;
	RNAHelix helix = null;
	NucNode probeNuc = refNuc;
	boolean testForSingleHairPinHelix = false;

	// look at special cases
	if (probeNuc.isBasePair()) // look for single helical run hairpin helix
	{
		helix = new RNAHelix(probeNuc);
		if (helix.isHairPin())
		{
			// debug("TESTING FOR single helical run hairpin helix");
			testForSingleHairPinHelix = true;
		}
	}
	else // look for either hairpin loop or level 0 start loops
	{
		RNASingleStrand singleStrand = new RNASingleStrand(probeNuc);
		if (singleStrand.isHairPin())
		{
			// debug("TESTING FOR single helical run hairpin helix");
			probeNuc = singleStrand.getFivePrimeDelineateNuc();
			testForSingleHairPinHelix = true;
		}
		else // it is a nuc in connecting loop; test for level, pseudoknot, etc.
		{
			// debug("TESTING FOR level 0 outer connecting loops");
			// first test if nuc is in 0 level 5' start loop
			probeNuc = refNuc;
			while (true)
			{
				if (probeNuc.isStartNuc()) // refNuc at 0 level loop
				{
					throw new ComplexException("Error in RNAStackedHelix.set()",
						ComplexDefines.RNA_HELICAL_RUN_ERROR,
						"nuc in level 0 start loop");
				}
				if (probeNuc.isBasePair()) // refNuc in regular connecting loop
					break;
				probeNuc = probeNuc.lastNonNullNuc();
			}
			// next test if nuc is in 0 level 3' end loop
			probeNuc = refNuc;
			while (true)
			{
				if (probeNuc.isEndNuc()) // refNuc at 0 level loop
					throw new ComplexException("Error in RNAStackedHelix.set()",
						ComplexDefines.RNA_HELICAL_RUN_ERROR,
						"nuc in level 0 end loop");
				if (probeNuc.isBasePair()) // refNuc in regular connecting loop
					break;
				probeNuc = probeNuc.nextNonNullNuc();
			}
		}
	}

	if (testForSingleHairPinHelix)
	{
		try
		{
			levelHelices = SSData.getCycleHelices(probeNuc);
		}
		catch (ComplexException e)
		{
			if (e.getErrorCode() == ComplexDefines.CREATE_LEVEL_PSEUDOKNOT_ERROR)
			{
				throw new ComplexException("Error in RNAStackedHelix.set()",
					ComplexDefines.RNA_HELICAL_RUN_ERROR
						+ ComplexDefines.CREATE_LEVEL_PSEUDOKNOT_ERROR,
					"testing single hairpin helix, pseudoknot error");
			}
			else
			{
				throw e;
			}
		}
		boolean found = false;
		if (levelHelices.size() != 2)
		{
			found = true;
		}
		if (levelHelices.elementAt(0) == null)
		{
			found = true;
		}
		if (found)
		{
			helix = new RNAHelix(probeNuc);
			this.setFivePrimeStartNuc(helix.getFivePrimeStartNuc());
			this.setFivePrimeEndNuc(helix.getFivePrimeEndNuc());
			this.setThreePrimeStartNuc(helix.getThreePrimeStartNuc());
			this.setThreePrimeEndNuc(helix.getThreePrimeEndNuc());
			this.setStartHelix(helix);
			this.setThreePrimeHelix(helix);
			return;
		}
	}

	// first cycle down to 5' end of helical run
	probeNuc = refNuc;
	helix = null;
	// boolean pseuodoKnotInCycleFound = false;
	while (true)
	{
		try
		{
			levelHelices = SSData.getCycleHelices(probeNuc);
		}
		catch (ComplexException e)
		{
			if (e.getErrorCode() == (ComplexDefines.RNA_LEVEL_ERROR + ComplexDefines.CREATE_ERROR))
			{
				// debug("pseudoknot found at level");
				break;

				// don't know what's going on.
				// throw e;
			}
			else
			{
				throw e;
			}
		}
		if (levelHelices.size() != 2)
		{
			break;
		}
		if (levelHelices.elementAt(0) == null)
		{
			break;
		}
		// follow down entry helices
		probeNuc = (NucNode)levelHelices.elementAt(0);
	}

	if (!probeNuc.isBasePair())
	{
		throw new ComplexException("Error in RNAStackedHelix.set()",
			ComplexDefines.RNA_HELICAL_RUN_ERROR,
			"probeNuc should be a base pair at this point");

	}
	helix = new RNAHelix(probeNuc); // set to entry helix of last helical run
	this.setFivePrimeStartNuc(helix.getFivePrimeStartNuc());
	this.setThreePrimeEndNuc(helix.getThreePrimeEndNuc());
	this.setStartHelix(helix);
		

	probeNuc = helix.getFivePrimeEndNuc().nextNonNullNuc();
	// probeNuc = (NucNode)levelHelices.elementAt(1);
	helix = null;

	// next cycle up to 3' end of helical run
	while (true)
	{
		try
		{
			levelHelices = SSData.getCycleHelices(probeNuc);
		}
		catch (ComplexException e)
		{
			if (e.getErrorCode() == ComplexDefines.CREATE_LEVEL_PSEUDOKNOT_ERROR)
			{
				throw new ComplexException("Error in RNAStackedHelix.set()",
					ComplexDefines.RNA_HELICAL_RUN_ERROR +
						ComplexDefines.CREATE_LEVEL_PSEUDOKNOT_ERROR,
					"pseudoknot problem trying to find 3' end of helical run");
			}
			else
			{
				throw e;
			}
		}
		if (levelHelices.size() != 2)
		{
			break;
		}
		if (levelHelices.elementAt(0) == null)
		{
			break;
		}
		probeNuc = (NucNode)levelHelices.elementAt(1);
		helix = new RNAHelix(probeNuc);
		if (helix.isHairPin())
		{
			break;
		}
		probeNuc = helix.getFivePrimeEndNuc().nextNonNullNuc();
	}
	if ((helix == null) || (!helix.isHairPin()))
		helix = new RNAHelix((NucNode)levelHelices.elementAt(0));

	this.setFivePrimeEndNuc(helix.getFivePrimeEndNuc());
	this.setThreePrimeStartNuc(helix.getThreePrimeStartNuc());
	this.setThreePrimeHelix(helix);

	// check to make sure just a single strand makes up this stacked helix
	SSData2D sstr = (SSData2D)this.getParentCollection();
	for (int nucID = this.getFivePrimeStartNuc().getID();
		nucID <= this.getFivePrimeEndNuc().getID();nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		if (nuc.isSingleStranded())
			continue;
		if (!nuc.isSelfRefBasePair())
			throw new ComplexException("Error in RNAStackedHelix.set()",
				ComplexDefines.RNA_HELICAL_RUN_ERROR + ComplexDefines.CREATE_STACKED_HELIX_BASEPAIRS_ERROR,
				"base pairs from two different rna strands in a\nstacked helix currently not supported.");
	}
}

// the 5' most helix
private RNAHelix startHelix = null;

public void
setStartHelix(RNAHelix startHelix)
{
    this.startHelix = startHelix;
}

public RNAHelix
getStartHelix()
{
    return (this.startHelix);
}

// the helix picked, if a basepaired nuc was picked
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


// last helix at three prime end
private RNAHelix threePrimeHelix = null;

public void
setThreePrimeHelix(RNAHelix threePrimeHelix)
{
    this.threePrimeHelix = threePrimeHelix;
}

public RNAHelix
getThreePrimeHelix()
{
    return (this.threePrimeHelix);
}

public boolean
isHairPin()
throws Exception
{
	return ((new RNAHelix(this.getFivePrimeEndNuc())).isHairPin());
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

// have to worry about base pairing across different strands. especially
// important as this uses Cycles to determine if stacked. Cycles currently
// handle base pairs from other strands as pseudo knots.
public Vector
getItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();
	NucNode nuc = null;
	boolean inNucs = false;


	if (this.getStartHelix().getLength() < 1)
		return (null);

	if (this.getStartHelix().isSelfRef())
	{
	}
	else
	{
	}


	// THIS IS TMP ONLY: NEED to allow for gaps
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

	return(delineators);
}

public String
toString()
{
	StringBuffer strBuf = new StringBuffer();
	strBuf.append("RNAStackedHelix in: ");
	strBuf.append(getParentCollection().toString());
	strBuf.append("\n");
	strBuf.append("5' start: " + this.getFivePrimeStartNuc() + "\n");
	strBuf.append("5' end: " + this.getFivePrimeEndNuc() + "\n");
	strBuf.append("3' start: " + this.getThreePrimeStartNuc() + "\n");
	strBuf.append("3' end: " + this.getThreePrimeEndNuc() + "\n");
	strBuf.append("\n");

	return(strBuf.toString());
}

private void
debug(String s)
{
	System.err.println("RNAStackedHelix-> " + s);
}

}
