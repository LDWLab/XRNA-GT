package ssview;

import java.awt.*;
import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;

import util.*;
import util.math.*;

// Contains nucs for one rna secondary structure
public class
SSData
extends NucCollection2D
{

public
SSData()
throws Exception
{
	this.setName(null);
}

public
SSData(String name)
throws Exception
{
	this();
	this.setName(name);
}

/*********** GENERAL ROUTINES *****************/

public void
addNucs(String nucCharList)
throws Exception
{
	for (int i = 0;i < nucCharList.length();i++)
	{
		char nucChar = nucCharList.charAt(i);
		if (Character.isWhitespace(nucChar))
			continue;
		this.addItem(new NucNode(nucChar));
	}
}

/* NOT SURE THIS IS RIGHT WAY TO DO THIS
public void
writeObj(String fileName)
throws Exception
{
	//
	// File ssDataOut = new File("ssData.out");
	// writeObject(new FileOutputStream(ssDataOut));
	//

	FileOutputStream ostream = new FileOutputStream(fileName);
	ObjectOutputStream p = new ObjectOutputStream(ostream);

	// p.writeInt(12345);
	p.writeObject(this);

	p.flush();
	ostream.close();
}

public static SSData
read(String fileName)
throws Exception
{
	FileInputStream istream = new FileInputStream(fileName);
	ObjectInputStream p = new ObjectInputStream(istream);
	// int i = p.readInt();
	// String today = (String)p.readObject();
	// Date date = (Date)p.readObject();
	SSData returnSSData = (SSData)p.readObject();
	istream.close();
	return (returnSSData);
}
*/

public void
init()
throws Exception
{
	this.setSSBPNucs();	
}

public void
resetNucStats()
throws Exception
{
	if (this instanceof SSData2D)
		super.resetNucStats();
}

public void
printSSData()
throws Exception
{
	for (int nucID = 1;nucID <= this.getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		System.err.println(nuc);
	}	
}

public void
resetName(String name)
throws Exception
{
	String currentName = new String(name);
	super.setName(name);

	for (int nucID = 1;nucID <= this.getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		if (nuc.isSingleStranded())
			continue;
		if (nuc.isSelfRefBasePair())
			continue;
		if (!nuc.getBasePair().getBasePairSStrName().equals(currentName))
			continue;
		nuc.getBasePair().setBasePairSStrName(name);
	}

	Object parentObj = this.getParentCollection();
	while (true)
	{
		if (parentObj == null)
			break;
		if (parentObj instanceof ComplexScene2D)
		{
			ComplexScene2D complexScene = (ComplexScene2D)parentObj;
			complexScene.init();
			complexScene.resetNucStats();
			break;
		}
		parentObj = ((ComplexCollection)parentObj).getParentCollection();
	}
}

public void
printPrimaryStructure()
throws Exception
{
	for (int nucID = 1;nucID <= getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			throw new Exception("Primary structure contains null nuc, therefore not valid");
		System.out.print(nuc.getNucChar());
	}	
	System.out.println();
}

public String
getSecondaryStructure()
throws Exception
{
	StringBuffer buf = new StringBuffer();

	for (int nucID = 1;nucID <= getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		if (!nuc.isHelixStart())
			continue;
		RNAHelix helix = new RNAHelix(nuc);
		buf.append(nuc.getID() + " " + nuc.getBasePair().getID()
			+ " " + helix.length() + "\n");
	}	
	return (buf.toString());
}

public void
printSecondaryStructure(int idAdjust)
throws Exception
{
	for (int nucID = 1;nucID <= getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		if (!nuc.isHelixStart())
			continue;
		RNAHelix helix = new RNAHelix(nuc);
		System.out.print((nuc.getID() - idAdjust) + " " + (nuc.getBasePair().getID() - idAdjust) + " " + helix.length() + "\n");
	}	
	System.out.println();
}

public void
printSecondaryStructure()
throws Exception
{
	this.printSecondaryStructure(0);
}

// does this rna strand interacte with any other rna strand
public boolean
interacts()
{
	for (int nucID = 1;nucID <= getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		if (nuc.isSingleStranded())
			continue;
		if (!nuc.isSelfRefBasePair())
			return (true);
	}	
	return (false);
}

public ComplexScene
wrapInComplexScene(String name)
throws Exception
{
	ComplexScene complexScene = new ComplexScene(name);
	ComplexSSDataCollection complexSSDataCollection =
		new ComplexSSDataCollection(name);
	complexSSDataCollection.addItem(this);
	complexScene.addItem(complexSSDataCollection);
	return (complexScene);
}

/*
public void
clearFlagged()
throws Exception
{
	for (int nucID = 1;nucID <= getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		nuc.setFlagged(false);
	}	
}
*/

// 1) represent node tree as a bunch of levels as nodes on an inverted
//		tree. Cycle 0 is the root of the tree.
// 2) the start of strand forms the bottom level node with the
//		end of strand. There is no entry helix here but 1 or more exit
//		helices.
// 3) if a loop has 1 exit helix and 0 entry helices then that loop
//		is a single stranded region starting out
// 4) each level node has one entry helix and 0 or more exit helices
// 5) if loop has one entry helix and 0 exit helices then loop is hairpin
// 6) if loop has one entry helix and one exit helix then in helical run
// 7) if loop has one entry helix and > 1 exit helices then
//		helical branch
// 8) every nuc belongs to a level. should be able to cycle through
//		all nucs and determine its level
// 9) every nuc belongs to a level. If single stranded then belongs
//		to a particular level. If nonhelical base pair then belongs
//		at a particular level. If helical basepair then 5' end
//		belongs to a level and represents start of an exit helix;
//		3' end belongs to a different level and is begining of that
//		level
//10) hairpin will be a termination of any more branching. A hairpin
//		helix will always be an exit helix.
//11) returned Vector will have the one entry helix in 0'th place. The
//		0th place will contain null if no entry helix (and therefore
//		at 0 level). The rest of Vector will contain exit helices
//		starting clockwise around entry helix.

public static Vector
getCycleHelices(NucNode refNuc)
throws Exception
{
	// always looking clockwise

	boolean debugOn = false;

	NucNode probeNuc = refNuc;
	NucNode startNuc = null;
	SSData sstr = probeNuc.getParentSSData();
	RNAHelix arbitrayExitHelix = null;
	Vector levelHelices = new Vector();

	// if in single strand then get to an exit helix in level

	// NEED to look into isSelfRefBasePair; not really telling us if single strand or not
	// if (!probeNuc.isSelfRefBasePair()) // orgional
	if (!probeNuc.isBasePair()) // trial 7/9/03
	{
		// first check if in hairpin as this is a special case; need
		// to back up to hairpins helices 5' end and carry on. A
		// hairpin helix will always be an exit helix to a level.
		if (debugOn) debug("CHECKING probeNuc for hairpin: " + probeNuc);
		if (debugOn) debug("setting single strand with refNuc: " + refNuc);
		RNASingleStrand singleStrand = new RNASingleStrand(refNuc);
		if (debugOn) debug("single strand: " + singleStrand);
		if (singleStrand.isHairPin()) // set to nearest bp'ed nuc
		{
			if (debugOn) debug("CHANGING probeNuc to 5' delineate nuc");
			probeNuc = singleStrand.getFivePrimeDelineateNuc();
			if (debugOn) debug("CHANGED probeNuc from hairpin?: " + (probeNuc.equals(refNuc)) + " " + probeNuc);
		}
		else
		{
			// find an exit helix
			if (debugOn) debug("STARTING WITH NON BP: " + probeNuc);
			startNuc = probeNuc;
			probeNuc = probeNuc.nextNonNullNuc();
			if (debugOn) debug("NEXT NONNULL NUC: " + probeNuc);
			while (true)
			{
				if (probeNuc.equals(startNuc))
					return (null); // no structure found in this strand
				// if (probeNuc.isSelfRefBasePair()) // original
				if (probeNuc.isBasePair()) // trial 7/10
					break;
				if (debugOn) debug("PROBENUC NOW: " + probeNuc);
				probeNuc = probeNuc.nextNonNullNuc();
				if (debugOn) debug("FOUND NEXT NUC: " + probeNuc);
			}
			// if (!probeNuc.isSelfRefBasePair()) // original
			if (!probeNuc.isBasePair()) // trial 7/10
				throw new ComplexException("Error 0 in getCycleHelices, for refNuc " +
					refNuc.getID() + ": " + " probeNuc not a basepair");
			if (debugOn) debug("FOUND FIRST BP: " + probeNuc);

			if (probeNuc.isThreePrimeBasePair()) // found an entry
											// helix; backup instead
			{
				if (debugOn) debug("PROBENUC IS 3': " + probeNuc);
				if (debugOn) debug("NOW backingup");
				probeNuc = probeNuc.lastNonNullNuc();
				// while (!probeNuc.isSelfRefBasePair()) // original
				while (!probeNuc.isBasePair()) // trial 7/10
				{
					if (debugOn) debug("PROBENUC NOW: " + probeNuc);
					probeNuc = probeNuc.lastNonNullNuc();
					if (probeNuc.isThreePrimeBasePair())
						probeNuc = probeNuc.getBasePair();
					if (debugOn) debug("FOUND NEXT NUC: " + probeNuc);
				}
			}
		}
	}

	// if (!probeNuc.isSelfRefBasePair()) // original
	if (!probeNuc.isBasePair()) // trial 7/10
		throw new ComplexException("Error 1 in getCycleHelices, refNuc " +
				refNuc.getID() + ": " + " probeNuc not a basepair");

	// refNuc now defined to be in an exit helix;
	// back up to 5' start of helix
	arbitrayExitHelix = new RNAHelix(probeNuc);

	// always at least one exit helix unless no structure
	if (arbitrayExitHelix == null)
		throw new ComplexException("Error 2 in getCycleHelices; no exit helix");
	probeNuc = arbitrayExitHelix.getFivePrimeStartNuc();

	if (debugOn) debug("PROBENUC (== startNuc) Determined: " + probeNuc);

	// if (!probeNuc.isSelfRefBasePair()) // original
	if (!probeNuc.isBasePair()) // trial 7/10
		throw new ComplexException("Error 3 in getCycleHelices " + refNuc.getID() + ": " +
			" probeNuc not a basepair");

	// determine entry helix; go clock wise around level until back
	// at starting pt. determine that only one entry helix,
	// throw exception if not.

	startNuc = probeNuc;
	probeNuc = probeNuc.getBasePair();
	if (debugOn) debug("JUMPED TO BP: " + probeNuc);
	NucNode entryHelixRefNuc = null;
	while (true)
	{
		// try and set entryHelixRefNuc to the entry helix. If not
		// found then maybe at root.

		probeNuc = probeNuc.nextNonNullNuc();
		if (probeNuc.equals(startNuc))
		{
			if (debugOn) debug("probe = start: " + probeNuc + " : " + startNuc);
			break;
		}

		// if (!probeNuc.isSelfRefBasePair()) // original
		if (!probeNuc.isBasePair()) // trial 7/10
			continue;
		if (probeNuc.isThreePrimeBasePair())
		{
			if (debugOn) debug("FOUND ENTRY HELIX: " + probeNuc);
			if (entryHelixRefNuc != null)
			{
				// NEED to check if this always constitutes pseudo-knot
				throw new ComplexException(
					"Error in SSData.getCycleHelices()",
					ComplexDefines.RNA_LEVEL_ERROR + ComplexDefines.CREATE_ERROR,
					ComplexDefines.CREATE_LEVEL_ENTRY_HELIX_MSG,
					"for refNuc.ID: " + refNuc.getID() + ", " +
					"entry helix: " + entryHelixRefNuc.getID() + ", probeNuc: " +
						probeNuc.getID());
			}
			entryHelixRefNuc = probeNuc;
		}
		probeNuc = probeNuc.getBasePair();
	}
	
	// now form vector with entry helix in 0 spot and go clockwise
	// around level adding 5' start ref nuc for each exit helix.

	// add entry helix ref nuc even if null
	levelHelices.add(entryHelixRefNuc);

	// identify entry helix 5' start to use as start of cycle
	// to assure counting exit helice starting clockwise from
	// and entry helix (or from 5' end of strand if at level 0).
	NucNode cycleStartNuc = entryHelixRefNuc;
	if (cycleStartNuc == null) // then at level 0, find first cycle nuc
	{
		cycleStartNuc = sstr.getNucAt(1);
		if (cycleStartNuc == null)
			cycleStartNuc = sstr.nextNonNullNuc();
	}
	
	// special cases:
	// 1) structure has one helix and 5' start and 3' end are
	//		first and last place in structure then add that helix
	//		as an exit helix and return
	if ((arbitrayExitHelix != null) &&
		(arbitrayExitHelix.getFivePrimeStartNuc().getID() == 1) &&
		(arbitrayExitHelix.getThreePrimeEndNuc().getID() == sstr.getNucCount()))
	{
		levelHelices.add(arbitrayExitHelix.getFivePrimeStartNuc());
		return (levelHelices);
	}
	// 2) structure has one helix and 5' start is in first place and
	// 3' end is not in last place, but single strand follows then add
	// that helix as an exit helix and return
	if ((arbitrayExitHelix != null) &&
		(arbitrayExitHelix.getFivePrimeStartNuc().getID() == 1))
	{
		boolean moreStructureFound = false;
		for (int nucID = arbitrayExitHelix.getThreePrimeEndNuc().getID() + 1;
			nucID <= sstr.getNucCount();nucID++)	
		{
			if (sstr.getNucAt(nucID).isBasePair())
			{
				moreStructureFound = true;
				break;
			}
		}
		if (!moreStructureFound)
		{
			levelHelices.add(arbitrayExitHelix.getFivePrimeStartNuc());
			return (levelHelices);
		}
	}

	if (debugOn) debug("CYCLE START NUC TO FIND EXIT HELICES: " + cycleStartNuc);
	startNuc = probeNuc = cycleStartNuc;
	// if (probeNuc.isSelfRefBasePair()) // original
	if (probeNuc.isBasePair()) // trial 7/10
		probeNuc = probeNuc.getBasePair();
	if (debugOn) debug("TRYING TO FIND EXIT HELICES with startnuc, probenuc: " +
		startNuc + " : " + probeNuc);

	// general circular case to find exit helices
	while (true)
	{
		probeNuc = probeNuc.nextNonNullNuc();

		if (probeNuc.equals(startNuc))
		{
			if (probeNuc.isFivePrimeBasePair())
			{
				if (debugOn) debug("FOUND EXIT HELIX: " + probeNuc);
				levelHelices.insertElementAt(probeNuc, 1);
			}
			break;
		}

		if (debugOn) debug("NOW AT: " + probeNuc);
		// if (!probeNuc.isSelfRefBasePair()) // original
		if (!probeNuc.isBasePair()) // trial 7/10
			continue;
		if (probeNuc.isFivePrimeBasePair())
		{
			if (debugOn) debug("FOUND EXIT HELIX: " + probeNuc);
			levelHelices.add(probeNuc);
		}
		probeNuc = probeNuc.getBasePair();
	}
	
	return (levelHelices);
}

public Vector
getCycle0Helices()
throws Exception
{
	// NEED to use first non null nuc (NEED a routine that gives
	// first non null nuc.
	return (getCycleHelices(this.getNucAt(1)));
}

public String
getPrimaryStructure()
throws Exception
{
	StringBuffer buf = new StringBuffer();

	for (int nucID = 1;nucID <= getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			throw new Exception("Primary structure contains null nuc, therefore not valid");
		buf.append(nuc.getNucChar());
	}	

	return (buf.toString());
}

// don't have to worry about base pairs since this is just a strand
public Vector
getItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();
	int itemID = 0;
	NucNode item = null;
	boolean inNucs = false;

	if (getNucCount() < 1)
		return (null);

	if (getNucCount() == 1)
	{
		if (getNucAt(1) == null)
			return (null);
		delineators.add(getNucAt(1));
		delineators.add(getNucAt(1));
		return(delineators);
	}

	while (true)
	{
		itemID++;
		if (itemID > getNucCount())
		{
			// finish up and return;
			if (item != null)
			{
				delineators.add(item);
				delineators.add(getNucAt(itemID - 1));
			}
			break;
		}

		if (getNucAt(itemID) == null)
		{
			if (inNucs)
			{
				delineators.add(item);
				delineators.add(getNucAt(itemID - 1));
				item = null;
				inNucs = false;
			}
		}
		else
		{	
			if (!inNucs)
			{
				inNucs = true;
				item = getNucAt(itemID);
			}
		}
	}

	// return((Object[])delineators.toArray());
	return(delineators);
}

// not quite sure how this works but looks like I should keep it.
// was used in TestSSView like:
// for (int nucID = 66;nucID < sstr.getNucCount();nucID++)
// {
//   Nuc2D sstrNuc = sstr.getNuc2DAt(nucID);
//   newSStr.replaceNuc(newSStrNuc, sstr.getNuc2DAt(nucID + 1));
// }
public void
replaceNuc(NucNode currentNuc, NucNode withNuc)
throws Exception
{
	if ((currentNuc != null) && !(currentNuc instanceof NucNode))
		throw new Exception("Trying to add a Non-NucNode object to NucCollection list");

	NucNode item = (NucNode)currentNuc;
	if (currentNuc == null)
	{
		this.getCollection().add(withNuc);
	}
	else if (currentNuc.getID() < 1) // then just append to end of list
	{
		this.getCollection().add(withNuc);
		withNuc.setID(getNucCount());
		withNuc.setParentCollection(this);
	}
	else if (currentNuc.getID() == getNucCount() + 1) // then just append
	{
		this.getCollection().add(withNuc);
		withNuc.setParentCollection(this);
	}
	else if (currentNuc.getID() > getNucCount() + 1)
	// adding new nuc after adding null nucs to pad
	{
		for (int i = getNucCount() + 1;i < currentNuc.getID();i++)
			this.getCollection().add((NucNode)null);
		this.getCollection().add(withNuc);
		withNuc.setParentCollection(this);
	}
	else if (currentNuc.getID() <= this.getNucCount()) // update existing nuc
	{
		// NEED to change getNucAt out to delineator
		NucNode nuc = getNucAt(currentNuc.getID());
		((Vector)this.getCollection()).set(currentNuc.getID() - 1, withNuc);
	}
	else
	{
		throw new Exception("UNKNOWN situation in NucNode.add()");
	}
}

private static void
debug(String s)
{
	System.err.println("SSData-> " + s);
}

}
