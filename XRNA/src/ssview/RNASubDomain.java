package ssview;

import java.util.Vector;
import java.io.*;

import util.math.*;

public class
RNASubDomain
extends NucCollection2D
{

// parent container should be of type SSData

public
RNASubDomain()
throws Exception
{
	super();
}

public
RNASubDomain(NucNode nuc)
throws Exception
{
	this();
	this.set(nuc);
}

public
RNASubDomain(RNAHelix helix)
throws Exception
{
	this(helix.getFivePrimeStartNuc());
}

public void
set(NucNode nuc)
throws Exception
{
	this.setParentCollection((SSData)nuc.getParentCollection());
	if (this.getStartHelix() == null)
		this.setStartHelix(new RNAHelix(nuc));
	else
		this.getStartHelix().set(nuc);
	this.setLength(this.getThreePrimeNuc().getID() -
		this.getFivePrimeNuc().getID() + 1);	

	// check to make sure just a single strand makes up this subdomain
	if ((!this.getFivePrimeNuc().isSelfRefBasePair()) ||
		(!this.getThreePrimeNuc().isSelfRefBasePair()))
		throw new ComplexException("Error in RNASubDomain.set()",
			ComplexDefines.RNA_SUBDOMAIN_ERROR + ComplexDefines.CREATE_SUBDOMAIN_HELIX_BASEPAIRS_ERROR,
			"base pairs from two different rna strands in a\nsub-domain currently not supported.");

	SSData2D sstr = (SSData2D)this.getParentCollection();
	for (int nucID = this.getFivePrimeNuc().getID();
		nucID <= this.getThreePrimeNuc().getID();nucID++)
	{
		Nuc2D refNuc = sstr.getNuc2DAt(nucID);
		if (refNuc.isSingleStranded())
			continue;
		if (!refNuc.isSelfRefBasePair())
			throw new ComplexException("Error in RNASubDomain.set()",
				ComplexDefines.RNA_SUBDOMAIN_ERROR + ComplexDefines.CREATE_SUBDOMAIN_HELIX_BASEPAIRS_ERROR,
				"base pairs from two different rna strands in a\nsub-domain currently not supported.");
	}
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

public NucNode
getFivePrimeNuc()
{
	if (this.getStartHelix() == null)
		return (null);
	return (this.getStartHelix().getFivePrimeStartNuc());
}

public NucNode
getThreePrimeNuc()
{
	if (this.getStartHelix() == null)
		return (null);
	return (this.getStartHelix().getThreePrimeEndNuc());
}

// have to worry about base pairing across different strands.
public Vector
getItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();
	NucNode nuc = null;
	boolean inNucs = false;

	if (this.getStartHelix().getLength() < 1)
		return (null);

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

public String
toString()
{
	return("RNA SubDomain, ref Helix: " + this.getStartHelix().toString());
}

private void
debug(String s)
{
	System.err.println("RNASubDomain-> " + s);
}

}
