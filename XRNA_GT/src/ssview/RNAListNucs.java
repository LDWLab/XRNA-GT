package ssview;

import java.util.Vector;
import java.io.*;

public class
RNAListNucs
extends NucCollection2D
{

public
RNAListNucs()
throws Exception
{
	super();
}

public
RNAListNucs(NucNode nuc)
throws Exception
{
	super();
	this.set(nuc);
}

public
RNAListNucs(NucNode nuc0, NucNode nuc1)
throws Exception
{
	super();
	this.set(nuc0, nuc1);
}

public
RNAListNucs(SSData sstr, int nucID0, int nucID1)
throws Exception
{
	this.set(sstr.getNucAt(nucID0), sstr.getNucAt(nucID1));
}

public void
set(NucNode nuc)
throws Exception
{
	if (this.getFirstNucPicked() != null)
	{
		this.set(this.getFirstNucPicked(), nuc);
	}
	else
	{
		this.setFirstNucPicked(nuc);
		this.setFivePrimeNuc(null);
		this.setThreePrimeNuc(null);
	}
}

public void
set(NucNode nuc0, NucNode nuc1)
throws Exception
{
	if (nuc0.getParentNucCollection2D() !=
		nuc1.getParentNucCollection2D())
		throw new ComplexException("Error in RNAListNucs constructor: ",
			ComplexDefines.RNA_LIST_NUCS_ERROR,
			"Can't use list nucs across different rna strands");
	this.setParentCollection(nuc0.getParentNucCollection2D());	
	if (nuc0.getID() < nuc1.getID())
	{
		this.setFivePrimeNuc(nuc0);
		this.setThreePrimeNuc(nuc1);
	}
	else
	{
		this.setFivePrimeNuc(nuc1);
		this.setThreePrimeNuc(nuc0);
	}

	this.setFirstNucPicked(null);

	this.setLength(this.getThreePrimeNuc().getID() -
		this.getFivePrimeNuc().getID() + 1);
}

public SSData
getParentSSData()
{
	// return ((SSData)this.getFivePrimeNuc().getParentCollection());
	return ((SSData)this.getParentCollection());
}

public boolean
isSet()
{
	return ((this.getFivePrimeNuc() != null) &&
		(this.getThreePrimeNuc() != null));
}

public boolean
firstNucPicked()
{
	return ((this.getFirstNucPicked() != null) &&
		(this.getFivePrimeNuc() == null) &&
		(this.getThreePrimeNuc() == null));
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

private NucNode firstNucPicked = null;

public void
setFirstNucPicked(NucNode firstNucPicked)
{
    this.firstNucPicked = firstNucPicked;
}

public NucNode
getFirstNucPicked()
{
    return (this.firstNucPicked);
}

// shouldn't have to worry about base pairing across 2 different strands.
public Vector
getItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();
	NucNode nuc = null;
	boolean inNucs = false;

	if (this.getLength() < 1)
		return (null);

	if (this.getLength() == 1)
	{
		if (this.getFivePrimeNuc() == null)
			return (null);
		delineators.add(this.getFivePrimeNuc());
		delineators.add(this.getThreePrimeNuc());
		return(delineators);
	}

	// THIS IS TMP ONLY:
	delineators.add(this.getFivePrimeNuc());
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

// used only for RNAListNucs, RNAColorUnit and RNANamedGroup
public boolean
basePairsOutsideOfCollection()
throws Exception
{
	Vector delineators = ((NucCollection2D)this).getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		NucNode startNuc1 = (NucNode)delineators.elementAt(i);
		NucNode refNuc1 = startNuc1;
		NucNode endNuc1 = (NucNode)delineators.elementAt(i+1);
		while (true)
		{
			if (refNuc1.isBasePair())
			{
				boolean refNuc1IsSelfRef = refNuc1.isSelfRefBasePair();
				// this is probably true only for RNAListNucs as it is
				// only allowed to list in a single rna strand
				if (!refNuc1IsSelfRef)
					return (true);
				for (int j = 0;j < delineators.size();j+=2)
				{
					NucNode refNuc2 = (NucNode)delineators.elementAt(j);
					NucNode endNuc2 = (NucNode)delineators.elementAt(j+1);
					while (true)
					{
						if (refNuc2.isSingleStranded())
						{
							if (refNuc2.equals(endNuc2))
								break;
							refNuc2 = refNuc2.nextNuc();
							continue;
						}
						NucNode cmpBPNuc = refNuc1.getBasePair();

						if ((cmpBPNuc.getID() < startNuc1.getID())
							|| (cmpBPNuc.getID() > endNuc1.getID()))
						{
							return (true);
						}

						if (refNuc2.equals(endNuc2))
							break;
						refNuc2 = refNuc2.nextNuc();
					}
				}
			}
			if (refNuc1.equals(endNuc1))
				break;
			refNuc1 = refNuc1.nextNuc();
		}
	}

	return (false);
}

public String
toString()
{
	// return("RNA ListNucs in: " + getParentCollection().toString());
	StringBuffer strBuf = new StringBuffer();
	if (this.getFirstNucPicked() == null)
		strBuf.append("1st pick nuc: null");
	else
		strBuf.append("1st pick nuc: " + this.getFirstNucPicked().getID());

	strBuf.append("\n");

	if (this.getFivePrimeNuc() == null)
		strBuf.append("5' nuc: null");
	else
		strBuf.append("5' nuc: " + this.getFivePrimeNuc().getID());

	strBuf.append("\n");

	if (this.getThreePrimeNuc() == null)
		strBuf.append("3' nuc: null");
	else
		strBuf.append("3' nuc: " + this.getThreePrimeNuc().getID());

	return (strBuf.toString());
}

private void
debug(String s)
{
	System.err.println("RNAListNucs-> " + s);
}

}
