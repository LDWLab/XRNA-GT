package ssview;

import java.awt.*;
import java.io.*;
import java.awt.geom.*;
import java.util.*;

import jimage.DrawLineObject;

import util.*;
import util.math.*;

// A Container for nucs
public abstract class
NucCollection
extends ComplexScene
{

public
NucCollection()
throws Exception
{
	this.setName(null);
	this.init();
}

public
NucCollection(String name)
throws Exception
{
	this();
	this.setName(name);
}

public void
addNucs(String nucCharList)
throws Exception
{
	for (int i = 0;i < nucCharList.length();i++)
		this.addItem(new NucNode(nucCharList.charAt(i)));
}

public void
addNuc(NucNode nuc)
throws Exception
{
	this.addItem(nuc);
}

// this only adds non null nucs from delineate list.
// make sure nucCollection is cleared first.
public void
addNucs(Vector nucList)
throws Exception
{
	for (int i = 0;i < nucList.size();i+=2)
	{
		NucNode refNuc = (NucNode)nucList.elementAt(i);
		NucNode endNuc = (NucNode)nucList.elementAt(i+1);
		while (true)
		{
			this.addNuc(refNuc);
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc();
		}
	}
}

public void
addNucs(NucCollection nucCollection)
throws Exception
{
	Vector nucList = nucCollection.getItemListDelineators();
	if (nucList == null)
		return;
	SSData addFromSStr = null;
	if (nucCollection instanceof SSData)
		addFromSStr = (SSData)nucCollection;
	else
		addFromSStr = (SSData)nucCollection.getParentCollection();
	if (addFromSStr == null)
		throw new Exception("ERROR in NucCollection.addNucs(NucCollection):  NucCollection has no parent");
	for (int i = 0;i < nucList.size();i+=2)
	{
		NucNode refNuc = (NucNode)nucList.elementAt(i);
		NucNode endNuc = (NucNode)nucList.elementAt(i+1);
		while (true)
		{
			this.addNuc(refNuc);
			if (refNuc == endNuc)
				break;
			refNuc = addFromSStr.getNucAt(refNuc.getID() + 1);
		}
	}

	/* Might want to add in parent labels also
	if (addFromSStr.getLabelList() != null)
		for (Enumeration e = addFromSStr.getLabelList().elements();e.hasMoreElements();)
			this.addLabel((DrawObject)e.nextElement());
	*/
}

public void
addItem(Object sceneGraphNode)
throws Exception
{
	// first list way is for stuff like nucs and proteins
	// in a long list that can have gaps in its sequence
	if ((sceneGraphNode != null) && !(sceneGraphNode instanceof NucNode))
		throw new Exception("Trying to add a Non-NucNode object to NucCollection list");

	NucNode item = (NucNode)sceneGraphNode;
	if (item == null)
	{
		this.getCollection().add(item);
	}
	else if (item.getID() < 1) // then just append to end of list
	{
		this.getCollection().add(item);
		item.setID(getNucCount());
		item.setParentCollection(this);
	}
	else if (item.getID() == getNucCount() + 1) // then just append
	{
		this.getCollection().add(item);
		item.setParentCollection(this);
	}
	else if (item.getID() > getNucCount() + 1)
	// adding new nuc after adding null nucs to pad
	{
		for (int i = getNucCount() + 1;i < item.getID();i++)
			this.getCollection().add((NucNode)null);
		this.getCollection().add(item);
		item.setParentCollection(this);
	}
	else if (item.getID() <= this.getNucCount()) // update existing nuc
	{
		NucNode nuc = getNucAt(item.getID());
		if ((nuc != null) && (nuc.getNucChar() != item.getNucChar()) &&
			(item.getNucChar() != 'N'))
			throw new Exception("\nERROR in ComplexCollection.addItem(): "
				+ "trying to change nucchar\n");
		((Vector)this.getCollection()).set(item.getID() - 1, item);
	}
	else
	{
		throw new Exception("UNKNOWN situation in NucNode.add()");
	}

	setCurrentItem(item);
}

// is item id in range 1->getNucCount()
public boolean
itemInRange(int id)
{
	return((id >= 1) && (id <= this.getNucCount()));
}

// is item in range 1->maxitemcount
public boolean
itemInRange()
{
	return((getCurrentID() >= 1) &&
		(getCurrentID() <= this.getNucCount()));
}

// this is different than parent ComplexCollection
public Object
getItemAt(int ordinalPos)
{
	if (ordinalPos <= 0)
		return (null);
	if (ordinalPos > getNucCount())
		return (null);
	return((NucNode)((Vector)this.getCollection()).elementAt(ordinalPos - 1));
}

// this is different than parent ComplexCollection
public void
setItemAt(NucNode nuc, int ordinalPos)
{
	if (ordinalPos <= 0)
		return;
	((Vector)this.getCollection()).setElementAt(nuc, ordinalPos - 1);
}

// delete drawObject associated with nuc only, not labels. Labels
// should be associated with parents right before deletion.
public void
delete()
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		NucNode startNuc = (NucNode)delineators.elementAt(i);
		NucNode endNuc = (NucNode)delineators.elementAt(i+1);
		NucNode previousNuc = endNuc;
		NucNode probeNuc = previousNuc.lastNonNullNuc();
		// first go through and get rid of all 3' bp nucs
		while (true)
		{
			previousNuc.delete();
			if (probeNuc == startNuc)
			{
				probeNuc.delete();
				break;
			}
			previousNuc = probeNuc;
			probeNuc = probeNuc.lastNonNullNuc();
		}
	}
}

public void
unsetBasePairs()
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		NucNode refNuc = (NucNode)delineators.elementAt(i);
		NucNode endNuc = (NucNode)delineators.elementAt(i+1);
		while (true)
		{
			refNuc.unsetBasePair();
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc();
		}
	}
}

public void
unsetNonSelfRefBasepairs()
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		NucNode refNuc = (NucNode)delineators.elementAt(i);
		NucNode endNuc = (NucNode)delineators.elementAt(i+1);
		while (true)
		{
			if (refNuc.isSingleStranded())
			{
				if (refNuc == endNuc)
					break;
				refNuc = refNuc.nextNuc();
				continue;
			}
			if (refNuc.isSelfRefBasePair())
			{
				if (refNuc == endNuc)
					break;
				refNuc = refNuc.nextNuc();
				continue;
			}

			refNuc.unsetBasePair();

			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc();
		}
	}
}

public NucNode
getFirstNonNullNuc()
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	return ((NucNode)delineators.elementAt(0));
}

public NucNode
getEndNonNullNuc()
throws Exception
{
	NucNode endNuc = null;
	Vector delineators = this.getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
		endNuc = (NucNode)delineators.elementAt(i+1);
	return (endNuc);
}

// currentID should only range 1->N, but reset should start at
// 0 so that we can always inc first when iterating.
private int currentID = 0;

public void
setCurrentID(int currentID)
{
	this.currentID = currentID;
}

public void
setCurrentID(NucNode nuc)
{
	this.currentID = nuc.getID();
}

public int
getCurrentID()
{
	return (this.currentID);
}

public void
incCurrentItem()
{
	if (getCurrentID() < getNucCount())
		setCurrentID(getCurrentID() + 1);
}

public void
decCurrentItem()
{
	if (getCurrentID() > 1)
		setCurrentID(getCurrentID() - 1);
}

public void
setCurrentItem(NucNode nuc)
{
	if (nuc == null)
		setCurrentID(0);
	else
		setCurrentID(nuc.getID());
}

public void
setCurrentItem(int id)
{
	if (!this.itemInRange(id))
		setCurrentID(0);
	else
		setCurrentID(id);
}

// NEED to deprecate to getCurrentNuc()
public NucNode
getCurrentItem()
{
	return(getNucAt(getCurrentID()));
}

public NucNode
getCurrentNuc()
{
	return(getNucAt(getCurrentID()));
}

// doesn't iterate
public NucNode
nextItem()
{
	return(getNucAt(getCurrentID() < getNucCount() ?
		getCurrentID() + 1 : getCurrentID()));
}

// used to get a delineated list of non null nucs
// public Object[]
public abstract Vector
getItemListDelineators()
throws Exception;

public abstract void
setGroupName(String groupName)
throws Exception;

public abstract String
getGroupName();

public Vector
getHelixInfoList()
throws Exception
{
	if (this instanceof RNASingleStrand)
		return (null);
	RNAHelix2D helix = new RNAHelix2D();
	Vector helixList = new Vector();
	Vector nucList = this.getItemListDelineators();
	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D refNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		while (true)
		{
			if (refNuc.isHelixStart())
			{
				helix.set(refNuc);
				HelixInfo helixInfo = new HelixInfo(helix);
				helixList.add(helixInfo);
			}
			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc2D();
		}

		// need to account for redundant info in single basepair helix
		if ((this instanceof RNAHelix) && (((RNAHelix)this).isSingleBasePairHelix()))
			break;
	}
	return (helixList);
}

// doesn't iterate
// circular, next nuc of nuc at nuccount is 1
public NucNode
nextNonNullNuc()
throws Exception
{
	int itemID = getCurrentID();
	/*
	if (itemID >= this.getNucCount())
		return (this.getNucAt(this.getNucCount()));
	*/
	do
	{
		itemID++;
		if (itemID > this.getNucCount())
			itemID = 1;
	} while(/*(itemID != this.getNucCount()) &&*/(this.getNucAt(itemID) == null));

	/*
	if (this.getNucAt(itemID) == null) // gone to far
	do
	{
		itemID--;
	} while((itemID != 1) && (this.getNucAt(itemID) == null));
	*/

	if (this.getNucAt(itemID) == null)
		throw new Exception
			("Can't find non-null nuc in NucNode.getNextNonNullNuc()");

	return (this.getNucAt(itemID));
}

// doesn't iterate
// circular, last nuc of 1 is nuc at nuccount
public NucNode
lastNonNullNuc()
throws Exception
{
	int itemID = getCurrentID();

	do
	{
		itemID--;
		if (itemID < 1)
			itemID = this.getNucCount();
	} while(/*(itemID != 1) &&*/(getNucAt(itemID) == null));

	/*
	if (getNucAt(itemID) == null) // at begin of strand
	do
	{
		itemID++;
	} while((itemID != getNucCount()) && (getNucAt(itemID) == null));
	*/

	if (getNucAt(itemID) == null)
		throw new Exception
			("Can't find non-null nuc in NucNode.getLastNonNullNuc()");

	return (getNucAt(itemID));
}

public boolean
atBeginOfItemList()
{
	return(getCurrentID() == 1);
}

public boolean
atBeginOfItemList(NucNode nuc)
{
	return((nuc != null) && (nuc.getID() == 1));
}

public boolean
atEndOfItemList()
{
	return(getCurrentID() == getNucCount());
}

///
// returns next non-null item or lastItem if no more. iterates.
//

public NucNode
getNextNonNullNuc()
throws Exception
{
	do
	{
		incCurrentItem();
	} while(!atEndOfItemList() && (getCurrentItem() == null));

	if (getCurrentItem() == null) // gone to far
	do
	{
		decCurrentItem();
	} while(!atBeginOfItemList() && (getCurrentItem() == null));

	if (getCurrentItem() == null)
		throw new Exception
			("Can't find non-null nuc in NucNode.getNextNonNullNuc()");

	return (getCurrentItem());
}

///
// returns last non-null item or firstItem if at begin of strand. iterates.
//

public NucNode
getLastNonNullNuc()
throws Exception
{
	do
	{
		decCurrentItem();
	} while(!atBeginOfItemList() && (getCurrentItem() == null));

	if (getCurrentItem() == null) // at begin of strand
	do
	{
		incCurrentItem();
	} while(!atEndOfItemList() && (getCurrentItem() == null));

	if (getCurrentItem() == null)
		throw new Exception
			("Can't find non-null nuc in NucNode.getLastNonNullNuc()");

	return (getCurrentItem());
}

public int
getNucCount()
{
    return (this.getCollection().size());
}

public NucNode
getNucAt(int nucID)
{
	return ((NucNode)getItemAt(nucID));
}

public boolean
currentNucIsBasePair()
{
	return (((NucNode)getCurrentItem()).isBasePair());
}

public NucNode
currentNucsBP()
{
	return (((NucNode)getCurrentItem()).getBasePair());
}

public NucNode
currentNuc()
{
	return ((NucNode)getCurrentItem());
}

public void
setSSBPNucs()
throws Exception
{
	RNABasePair basePair = new RNABasePair();
	for (int nucID = 1;nucID <= this.getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		nuc.resetBasePair();

		if (!nuc.isBasePair())
			continue;
		// test that base nucs bp types are same
		basePair.set(nuc);
		basePair.getType();
	}
}

/*********** GENERAL ROUTINES *****************/

/*
// used only for RNAListNucs, RNAColorUnit and RNANamedGroup
public boolean
basePairsOutsideOfCollection()
throws Exception
{
	if (!((this instanceof RNAListNucs) || (this instanceof RNAColorUnit) || (this instanceof RNANamedGroup)))
		throw new ComplexException("Error in NucCollection.basePairsOutsideOfColleciton():" +
			" Must be instanceof RNAListNucs | RNAColorUnit | RNANamedGroup");

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
*/

public static void
sortHelixEndNucs(int[] nucA)
throws ComplexException
{
	MathUtil.shellSort(nucA);
	int nuc0ID = nucA[0];
	int nuc1ID = nucA[1];
	int nuc2ID = nucA[2];
	int nuc3ID = nucA[3];

	if ((nuc1ID - nuc0ID) != (nuc3ID - nuc2ID))
		throw new ComplexException(
			"ERROR 0 in NucCollection.setBasePairs(SSData, int, int, int, int)",
			(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
			ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
			"range of 5' nucs (" + nuc1ID + "-" + nuc0ID +
				") doesn't equal range of 3' nucs (" +
				nuc3ID + "-" + nuc2ID + ")");
}

public static void
sortHelixEndNucs(NucNode[] nucA)
throws ComplexException
{
	/*
	debug("TO START\n" +
		nucA[0] + " " + nucA[0].getParentSSData().getName() + "\n" +
		nucA[1] + " " + nucA[1].getParentSSData().getName() + "\n" +
		nucA[2] + " " + nucA[2].getParentSSData().getName() + "\n" +
		nucA[3] + " " + nucA[3].getParentSSData().getName() + "\n");
	*/
	NucNode nuc0 = nucA[0];
	NucNode nuc1 = nucA[1];
	NucNode nuc2 = nucA[2];
	NucNode nuc3 = nucA[3];
	SSData sstr0 = nuc0.getParentSSData();
	SSData sstr1 = nuc1.getParentSSData();
	SSData sstr2 = nuc2.getParentSSData();
	SSData sstr3 = nuc3.getParentSSData();

	if (sstr0.equals(sstr1) && sstr1.equals(sstr2) &&
		sstr2.equals(sstr3))
	{
		int[] nucID_A = new int[4];
		nucID_A[0] = nuc0.getID();
		nucID_A[1] = nuc1.getID();
		nucID_A[2] = nuc2.getID();
		nucID_A[3] = nuc3.getID();
		MathUtil.shellSort(nucID_A);
		nucA[0] = sstr0.getNuc2DAt(nucID_A[0]);
		nucA[1] = sstr0.getNuc2DAt(nucID_A[1]);
		nucA[2] = sstr0.getNuc2DAt(nucID_A[2]);
		nucA[3] = sstr0.getNuc2DAt(nucID_A[3]);
	}
	else if ((sstr0.equals(sstr1) && sstr2.equals(sstr3)) ||
		(sstr0.equals(sstr2) && sstr1.equals(sstr3)) ||
		(sstr0.equals(sstr3) && sstr2.equals(sstr1)))
	{
		if (sstr0.equals(sstr1))
		{
			if (nuc0.getID() < nuc1.getID())
			{
				nucA[0] = nuc0;
				nucA[1] = nuc1;
			}
			else
			{
				nucA[0] = nuc1;
				nucA[1] = nuc0;
			}
			if (nuc2.getID() < nuc3.getID())
			{
				nucA[2] = nuc2;
				nucA[3] = nuc3;
			}
			else
			{
				nucA[2] = nuc3;
				nucA[3] = nuc2;
			}
			/*
			debug("nucA: " + "\n" +
				nucA[0] + " " + nucA[0].getParentSSData().getName() + "\n" +
				nucA[1] + " " + nucA[1].getParentSSData().getName() + "\n" +
				nucA[2] + " " + nucA[2].getParentSSData().getName() + "\n" +
				nucA[3] + " " + nucA[3].getParentSSData().getName() + "\n");
			*/
		}
		else if (sstr0.equals(sstr2))
		{
			if (nuc0.getID() < nuc2.getID())
			{
				nucA[0] = nuc0;
				nucA[1] = nuc2;
			}
			else
			{
				nucA[0] = nuc2;
				nucA[1] = nuc0;
			}
			if (nuc1.getID() < nuc3.getID())
			{
				nucA[2] = nuc1;
				nucA[3] = nuc3;
			}
			else
			{
				nucA[2] = nuc3;
				nucA[3] = nuc1;
			}
		}
		else if (sstr0.equals(sstr3))
		{
			if (nuc0.getID() < nuc3.getID())
			{
				nucA[0] = nuc0;
				nucA[1] = nuc3;
			}
			else
			{
				nucA[0] = nuc3;
				nucA[1] = nuc0;
			}
			if (nuc1.getID() < nuc2.getID())
			{
				nucA[2] = nuc1;
				nucA[3] = nuc2;
			}
			else
			{
				nucA[2] = nuc2;
				nucA[3] = nuc1;
			}
		}
	}
	else
		throw new ComplexException(
			"ERROR 0 in NucCollection.sortHelixEndNucs(NucNode[])",
			(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
			ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
				"Trying to base pair across more that 2 rna strands");

	nuc0 = nucA[0];
	nuc1 = nucA[1];
	nuc2 = nucA[2];
	nuc3 = nucA[3];

	if ((nuc1.getID() - nuc0.getID()) != (nuc3.getID() - nuc2.getID()))
	{
		/*
		debug("ERROR: " +
				nuc0.getID() + " " +
				nuc1.getID() + " " +
				nuc2.getID() + " " +
				nuc3.getID());
		*/
		throw new ComplexException(
			"ERROR 1 in NucCollection.setBasePairs(SSData, int, int, int, int)",
			(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
			ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
			"range of 5' nucs (" + nuc1.getID() + "-" + nuc0.getID() +
				") doesn't equal range of 3' nucs (" +
				nuc3.getID() + "-" + nuc2.getID() + ")");
	}
}

// implies that all nucs in same sstr
public static void
setBasePairs(SSData sstr, int nuc0ID, int nuc1ID, int nuc2ID, int nuc3ID)
throws Exception
{
	int[] nucA = new int[4];
	nucA[0] = nuc0ID;
	nucA[1] = nuc1ID;
	nucA[2] = nuc2ID;
	nucA[3] = nuc3ID;
	sortHelixEndNucs(nucA);
	nuc0ID = nucA[0];
	nuc1ID = nucA[1];
	nuc2ID = nucA[2];
	nuc3ID = nucA[3];

	int bpNucID = nuc3ID;
	for(int nucID = nuc0ID;nucID <= nuc1ID;nucID++)
	{
		NucNode nuc = sstr.getNucAt(nucID);
		NucNode bpNuc = sstr.getNucAt(bpNucID);
		if (nuc.isBasePair())
			throw new ComplexException(
				"ERROR 1 in NucCollection.setBasePairs(SSData, int, int, int, int)",
				(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
				ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
				"nuc: " + nuc.getID() + " is already basepaired");
		if (bpNuc.isBasePair())
			throw new ComplexException(
				"ERROR 2 in NucCollection.setBasePairs(SSData, int, int, int, int)",
				(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
				ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
				"nuc: " + bpNuc.getID() + " is already basepaired");
		nuc.setBasePairID(bpNucID);
		bpNuc.setBasePairID(nucID);
		nuc.resetBasePair();
		bpNuc.resetBasePair();
		bpNucID--;
	}
}

// implies that bpNuc is in sstr
public static void
setBasePairs(SSData sstr, int refNucID, int bpNucID, int length)
throws Exception
{
	if (length <= 0)
		throw new ComplexException(
			"ERROR in NucCollection.setBasePairs(SSData, int, int, int)",
			(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
			ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
			"helix length must be greater than 0");
	int end5PNucID = (refNucID + length - 1);
	int start3PNucID = (bpNucID - length + 1);
	if (end5PNucID >= start3PNucID)
		throw new ComplexException(
			"ERROR in NucCollection2D.setBasePairs(SSData, int, int, int)",
			(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
			ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
			"Helix length is too long");
	setBasePairs(sstr, refNucID, bpNucID, end5PNucID, start3PNucID);
}

// refNuc and bpNuc could have different parents
public static void
setBasePairs(NucNode refNuc, NucNode bpNuc, int length)
throws Exception
{
	if (refNuc.getParentSSData().equals(bpNuc.getParentSSData()))
	{
		setBasePairs(refNuc.getParentSSData(), refNuc.getID(),
			bpNuc.getID(), length);
		return;
	}

	SSData refSStr = refNuc.getParentSSData();
	SSData bpSStr = bpNuc.getParentSSData();
	String refName = refSStr.getName();
	String bpName = bpSStr.getName();

	// nuc0ID,nuc1ID will be 5',3' nuc of refNuc's strand
	// nuc2ID,nuc3ID will be 5',3' nuc of bpNuc's strand
	int nuc0ID = refNuc.getID();
	int nuc1ID = nuc0ID + length - 1;
	int nuc3ID = bpNuc.getID();
	int nuc2ID = nuc3ID - length + 1;

	/*
	debug("refNuc: " + refNuc);
	debug("refName: " + refName);
	debug("bpNuc: " + bpNuc);
	debug("bpName: " + bpName);
	debug("nuc0ID: " + nuc0ID);
	debug("nuc1ID: " + nuc1ID);
	debug("nuc2ID: " + nuc2ID);
	debug("nuc3ID: " + nuc3ID);
	*/

	if ((nuc1ID - nuc0ID) != (nuc3ID - nuc2ID))
		throw new ComplexException(
			"ERROR 0 in NucCollection2D.setBasePairs(NucNode, NucNode, int)",
			(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
			ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
			"range of 5' nucs (" + nuc0ID + "-" + nuc1ID +
				") doesn't equal range of 3' nucs (" +
				nuc2ID + "-" + nuc3ID + ")");

	int bpNucID = nuc3ID;
	for(int nucID = nuc0ID;nucID <= nuc1ID;nucID++)
	{
		NucNode nuc = refSStr.getNucAt(nucID);
		NucNode bpProbeNuc = bpSStr.getNucAt(bpNucID);
		if (nuc.isBasePair())
			throw new ComplexException(
				"ERROR 1 in NucCollection2D.setBasePairs(NucNode, NucNode, int)",
				(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
				ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
				"nuc: " + nuc.getID() + " is already basepaired");
		if (bpProbeNuc.isBasePair())
			throw new ComplexException(
				"ERROR 2 in NucCollection2D.setBasePairs(NucNode, NucNode, int)",
				(ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR),
				ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR_MSG,
				"nuc: " + bpProbeNuc.getID() + " is already basepaired");
		nuc.setBasePairID(bpNucID);
		nuc.setBasePairSStrName(bpName);
		bpProbeNuc.setBasePairID(nucID);
		bpProbeNuc.setBasePairSStrName(refName);
		nuc.resetBasePair();
		bpProbeNuc.resetBasePair();
		bpNucID--;
	}
}

public static void
setBasePairs(Vector helixList)
throws Exception
{
	if (helixList == null)
		return;
	for (int i = 0;i < helixList.size();i++)
	{
		HelixInfo helixInfo = (HelixInfo)helixList.elementAt(i);
		if (helixInfo == null)
			continue;
		if ((helixInfo.getRefNuc() == null) ||
			(helixInfo.getBpNuc() == null) ||
			(helixInfo.getLength() == 0))
			continue;
		setBasePairs(helixInfo.getRefNuc(), helixInfo.getBpNuc(),
			helixInfo.getLength());
	}
}

// implies only have refNucID, bpNucID, length in helixList.
// if have getRefNuc and getBPNuc then go ahead and use first.
public static void
setBasePairs(SSData sstr, Vector helixList)
throws Exception
{
	if (helixList == null)
		return;
	for (int i = 0;i < helixList.size();i++)
	{
		HelixInfo helixInfo = (HelixInfo)helixList.elementAt(i);
		if (helixInfo == null)
			continue;
		if (helixInfo.getLength() == 0)
			continue;
		NucNode refNuc = helixInfo.getRefNuc();
		if (refNuc == null)
			refNuc = sstr.getNucAt(helixInfo.getRefNucID());
		if (refNuc == null)
			continue;
		NucNode bpNuc = helixInfo.getBpNuc();
		if (bpNuc == null)
		{
			if (helixInfo.getBPSStrName() != null)
			{
				ComplexScene complexScene = refNuc.getComplexScene();
				if (complexScene == null)
					throw new ComplexException("non existent parent for nuc: " + refNuc);
				SSData bpSStr = complexScene.getComplexSSData(helixInfo.getBPSStrName());
				if (bpSStr == null)
					throw new ComplexException("non existent rna strand: " + helixInfo.getBPSStrName());
				bpNuc = bpSStr.getNucAt(helixInfo.getBpNucID());
			}
			else
			{
				bpNuc = sstr.getNucAt(helixInfo.getBpNucID());
			}
		}
		if (bpNuc == null)
			continue;
		setBasePairs(refNuc, bpNuc, helixInfo.getLength());
	}
}

public static void
setBasePairs(SSData2D sstr, String secondaryStructure)
throws Exception
{
	if (secondaryStructure == null)
		return;

	Vector helixList = null;
	if (secondaryStructure.indexOf(';') >= 0)
		helixList = HelixInfo.getHelixInfoList(sstr, secondaryStructure, ';');
	else if (secondaryStructure.indexOf(':') >= 0)
		helixList = HelixInfo.getHelixInfoList(sstr, secondaryStructure, ':');
	else
		helixList = HelixInfo.getHelixInfoList(sstr, secondaryStructure);
	setBasePairs(helixList);
}

private double sveRNAHelixBaseDistance = 0.0;
private double sveRNABasePairDistance = 0.0;
private double sveRNAMisMatchBasePairDistance = 0.0;
private double sveRNANucToNextNucDistance = 0.0;

public void
saveDefaultDistances()
{
	sveRNAHelixBaseDistance = getRNAHelixBaseDistance();
	sveRNABasePairDistance = getRNABasePairDistance();
	sveRNAMisMatchBasePairDistance = getRNAMisMatchBasePairDistance();
	sveRNANucToNextNucDistance = getRNANucToNextNucDistance();
	/*
	debug("sveRNAHelixBaseDistance: " + sveRNAHelixBaseDistance);
	debug("sveRNABasePairDistance: " + sveRNABasePairDistance);
	debug("sveRNAMisMatchBasePairDistance: " + sveRNAMisMatchBasePairDistance);
	debug("sveRNANucToNextNucDistance: " + sveRNANucToNextNucDistance);
	*/
}

public void
resetFromSaveDefaultDistances()
{
	setRNAHelixBaseDistance(sveRNAHelixBaseDistance);
	setRNABasePairDistance(sveRNABasePairDistance);
	setRNAMisMatchBasePairDistance(sveRNAMisMatchBasePairDistance);
	setRNANucToNextNucDistance(sveRNANucToNextNucDistance);
}

private double rnaHelixBaseDistance =
	ComplexDefines.RNA_HELIX_BASE_DISTANCE;

public void
setRNAHelixBaseDistance(double rnaHelixBaseDistance)
{
    this.rnaHelixBaseDistance = rnaHelixBaseDistance;
}

public double
getRNAHelixBaseDistance()
{
    return (this.rnaHelixBaseDistance);
}

private double rnaBasePairDistance =
	ComplexDefines.RNA_BASEPAIR_DISTANCE;

public void
setRNABasePairDistance(double rnaBasePairDistance)
{
    this.rnaBasePairDistance = rnaBasePairDistance;
}

public double
getRNABasePairDistance()
{
    return (this.rnaBasePairDistance);
}

private double rnaMisMatchBasePairDistance =
	ComplexDefines.RNA_MISMATCH_BASEPAIR_DISTANCE;

public void
setRNAMisMatchBasePairDistance(double rnaMisMatchBasePairDistance)
{
    this.rnaMisMatchBasePairDistance = rnaMisMatchBasePairDistance;
}

public double
getRNAMisMatchBasePairDistance()
{
    return (this.rnaMisMatchBasePairDistance);
}

private double rnaNucToNextNucDistance =
	ComplexDefines.NUC_TO_NEXTNUC_DISTANCE;

public void
setRNANucToNextNucDistance(double rnaNucToNextNucDistance)
{
    this.rnaNucToNextNucDistance = rnaNucToNextNucDistance;
}

public double
getRNANucToNextNucDistance()
{
    return (this.rnaNucToNextNucDistance);
}

// static int testCount = 0;
// NEED to figure out why this gets fired so many times

public void
setDistancesFromCollection(NucCollection collection)
throws Exception
{
	this.setDistanceParameters(collection.getRNANucToNextNucDistance(),
		collection.getRNAHelixBaseDistance(), collection.getRNABasePairDistance(),
		collection.getRNAMisMatchBasePairDistance());
}

public void
setDistanceParameters(double nucToNextNucDistance, double helixBaseDistance,
	double basePairDistance, double mismatchDistance)
{
	this.setRNANucToNextNucDistance(nucToNextNucDistance);
	this.setRNAHelixBaseDistance(helixBaseDistance);
	this.setRNABasePairDistance(basePairDistance);
	this.setRNAMisMatchBasePairDistance(mismatchDistance);
}

private double distanceTolerance =
	ComplexDefines.RNA_DISTANCE_TOLERANCE;

public void
setDistanceTolerance(double distanceTolerance)
{
    this.distanceTolerance = distanceTolerance;
}

public double
getDistanceTolerance()
{
    return (this.distanceTolerance);
}

public boolean
isValidRNAHelixBaseDistance(double cmpDistance)
{
	double dist = this.getRNAHelixBaseDistance();
	double tolerance = this.getDistanceTolerance();
	return ((cmpDistance >= dist - tolerance) &&
		(cmpDistance <= dist + tolerance));
}

public boolean
isValidRNAHelixBaseDistance(double cmpDistance, double tolerance)
{
	double dist = this.getRNAHelixBaseDistance();
	return ((cmpDistance >= dist - tolerance) &&
		(cmpDistance <= dist + tolerance));
}

public boolean
isValidRNABasePairDistance(double cmpDistance)
{
	double dist = this.getRNABasePairDistance();
	double tolerance = this.getDistanceTolerance();
	return ((cmpDistance >= dist - tolerance) &&
		(cmpDistance <= dist + tolerance));
}

public boolean
isValidRNAMisMatchBasePairDistance(double cmpDistance)
{
	double dist = this.getRNAMisMatchBasePairDistance();
	double tolerance = this.getDistanceTolerance();
	return ((cmpDistance >= dist - tolerance) &&
		(cmpDistance <= dist + tolerance));
}

public boolean
isValidRNANucToNextNucDistance(double cmpDistance)
{
	double dist = this.getRNANucToNextNucDistance();
	double tolerance = this.getDistanceTolerance();
	return ((cmpDistance >= dist - tolerance) &&
		(cmpDistance <= dist + tolerance));
}

public boolean
isValidRNANucToNextNucDistance(double cmpDistance, double tolerance)
{
	double dist = this.getRNANucToNextNucDistance();
	return ((cmpDistance >= dist - tolerance) &&
		(cmpDistance <= dist + tolerance));
}

// NEED to set these in Nuc2D and make these here into iterators
// throughout nuccollection.

private double nucLabelLineWidth = ComplexDefines.NUCLABEL_LINE_WIDTH;

public void
setNucLabelLineWidth(double nucLabelLineWidth)
{
    this.nucLabelLineWidth = nucLabelLineWidth;
}

public double
getNucLabelLineWidth()
{
    return (this.nucLabelLineWidth);
}

private double nucLabelLineInnerDistance = ComplexDefines.NUCLABEL_NUC_TO_LINE_DISTANCE;

public void
setNucLabelLineInnerDistance(double nucLabelLineInnerDistance)
{
    this.nucLabelLineInnerDistance = nucLabelLineInnerDistance;
}

public double
getNucLabelLineInnerDistance()
{
    return (this.nucLabelLineInnerDistance);
}

private double nucLabelLineOuterDistance = ComplexDefines.NUCLABEL_LINE_TO_NUMBER_DISTANCE;

public void
setNucLabelLineOuterDistance(double nucLabelLineOuterDistance)
{
    this.nucLabelLineOuterDistance = nucLabelLineOuterDistance;
}

public double
getNucLabelLineOuterDistance()
{
    return (this.nucLabelLineOuterDistance);
}

/* not sure about this; rna strands could contain bps outside
public boolean
rnaConstraintSelfRefOnly()
{
	return ((this instanceof SSData) || (this instanceof SSData2D) ||
		(this instanceof NucNode) || (this instanceof Nuc2D) ||
		(this instanceof RNASingleStrand) || (this instanceof RNASingleStrand2D));
}
*/

// this method looks upstream and downstream to determine best nuc label
// line length.
public double
getNucLabelLineLength(int currNucID)
throws Exception
{
	SSData2D sstr = null;

	if (!((this instanceof SSData) || (this instanceof SSData2D) ||
		(this instanceof RNASingleStrand) || (this instanceof RNASingleStrand2D) ||
		(this instanceof RNAListNucs) || (this instanceof RNAListNucs2D)))
		return (ComplexDefines.NUCLABEL_LINE_LENGTH);

	if (this instanceof SSData2D)
		sstr = (SSData2D)this;
	else if (this instanceof RNASingleStrand2D)
		sstr = (SSData2D)((RNASingleStrand2D)this).getParentSSData2D();
	else if (this instanceof RNASingleStrand)
		sstr = (SSData2D)((RNASingleStrand)this).getParentSSData();
	else if (this instanceof RNAListNucs2D)
		sstr = (SSData2D)((RNAListNucs2D)this).getParentSSData2D();
	else if (this instanceof RNAListNucs)
		sstr = (SSData2D)((RNAListNucs)this).getParentSSData();

	Vector delineators = sstr.getItemListDelineators();
	if (delineators == null)
		return (0.0);
	int lastNucID = ((NucNode)delineators.elementAt(delineators.size() - 1)).getID();
	int fpSide = currNucID; // make sure to look at currNucID
	int tpSide = currNucID + 1;

	while (true)
	{
		if (fpSide > 0)
		{
			Nuc2D refNuc = (Nuc2D)sstr.getNucAt(fpSide);
			if (refNuc != null)
			{
				DrawLineObject line = refNuc.getLineLabel();
				if (line != null)
					return (line.length());
			}
		}
		if (tpSide <= lastNucID)
		{
			Nuc2D refNuc = (Nuc2D)sstr.getNucAt(tpSide);
			if (refNuc != null)
			{
				DrawLineObject line = refNuc.getLineLabel();
				if (line != null)
					return (line.length());
			}
		}
		if ((fpSide <= 0) && (tpSide > lastNucID))
			break;
		
		fpSide--;
		tpSide++;
	}

	return (0.0);
}

public void
resetNucStats()
throws Exception
{
	Vector nucList = this.getItemListDelineators();
	if (nucList == null)
		return;

	// check to make sure that can look at x,y positions. use defaults
	// if can't determine
	if (!(nucList.elementAt(0) instanceof Nuc2D))
		// then probably of type NucNode, therefore no x,y positions
		return;
	if (!((Nuc2D)nucList.elementAt(0)).getIsFormatted())
		// for now assume if one nuc isn't formatted then rest aren't
		return;

	Hashtable helixBaseDistHT = new Hashtable();
	Hashtable basePairDistHT = new Hashtable();
	Hashtable misMatchBasePairDistHT = new Hashtable();
	Hashtable nucToNucDistHT = new Hashtable();
	Hashtable nucLineLabelWidthHT = new Hashtable();

	RNABasePair2D basePair = new RNABasePair2D();
	RNABasePair2D lastBasePair = new RNABasePair2D();

	for (int i = 0;i < nucList.size();i+=2)
	{
		Nuc2D startNuc = (Nuc2D)nucList.elementAt(i);
		Nuc2D endNuc = (Nuc2D)nucList.elementAt(i+1);
		Nuc2D refNuc = startNuc;
		Nuc2D lastNuc = refNuc;
		while (true)
		{
			// get nuc line label stats
			DrawLineObject lineObj = refNuc.getLineLabel();
			if (lineObj != null)
			{
				Double lineWidth = new Double(lineObj.getLineWidth());
				Integer count = new Integer(1);
				if (nucLineLabelWidthHT.containsKey(lineWidth))
				{
					count = (Integer)nucLineLabelWidthHT.get(lineWidth);
					int val = count.intValue();
					count = new Integer(val + 1);
				}
				nucLineLabelWidthHT.put(lineWidth, count);
			}

			if (refNuc.isFivePrimeBasePair())
			{
				// do bp dists only

				basePair.set(refNuc);

				Double dist = new Double(MathUtil.roundVal(
					basePair.distance(), 0));
				Integer count = new Integer(1);
				if (basePair.isMisMatch())
				{
					if (misMatchBasePairDistHT.containsKey(dist))
					{
						count = (Integer)misMatchBasePairDistHT.get(dist);
						int val = count.intValue();
						count = new Integer(val + 1);
					}
					misMatchBasePairDistHT.put(dist, count);
				}
				else
				{
					if (basePairDistHT.containsKey(dist))
					{
						count = (Integer)basePairDistHT.get(dist);
						int val = count.intValue();
						count = new Integer(val + 1);
					}
					basePairDistHT.put(dist, count);
				}
			}
			if (refNuc == startNuc)
			{
				// to cover case where delineation only covers one nuc
				if (refNuc == endNuc)
					break;
				lastNuc = refNuc;
				refNuc = refNuc.nextNuc2D();
				continue;
			}

			if (refNuc.isBasePair() && lastNuc.isBasePair() &&
				(refNuc.isContiguousBasePairWith(lastNuc)))
			{
				// check contiguous helices
				// do nton1 in helix
				basePair.set(refNuc);
				lastBasePair.set(lastNuc);

				Double dist = new Double(MathUtil.roundVal(
					basePair.getMidPt().distance(lastBasePair.getMidPt()), 0));
				Integer count = new Integer(1);
				if (helixBaseDistHT.containsKey(dist))
				{
					count = (Integer)helixBaseDistHT.get(dist);
					int val = count.intValue();
					count = new Integer(val + 1);
				}
				helixBaseDistHT.put(dist, count);
			}
			else if (
				(refNuc.isBasePair() && lastNuc.isSingleStranded()) ||
				(refNuc.isSingleStranded() && lastNuc.isSingleStranded()) ||
				(refNuc.isSingleStranded() && lastNuc.isBasePair()))
			{
				Double dist = new Double(MathUtil.roundVal(
					refNuc.getPoint2D().distance(lastNuc.getPoint2D()), 0));
				Integer count = new Integer(1);
				if (nucToNucDistHT.containsKey(dist))
				{
					count = (Integer)nucToNucDistHT.get(dist);
					int val = count.intValue();
					count = new Integer(val + 1);
				}
				nucToNucDistHT.put(dist, count);
			}

			if (refNuc == endNuc)
				break;
			lastNuc = refNuc;
			refNuc = refNuc.nextNuc2D();
		}
	}

	Double largestDistCount = null;
	int largestVal = 0;
	for (Enumeration e = nucToNucDistHT.keys() ; e.hasMoreElements() ;)
	{
		Double dist = (Double)e.nextElement();
		int val = ((Integer)nucToNucDistHT.get(dist)).intValue();
		if (val > largestVal)
		{
			largestVal = val;
			largestDistCount = dist;
		}
	}
	if (largestDistCount != null)
		this.setRNANucToNextNucDistance(largestDistCount.doubleValue());

	largestDistCount = null;
	largestVal = 0;
	for (Enumeration e = helixBaseDistHT.keys() ; e.hasMoreElements() ;)
	{
		Double dist = (Double)e.nextElement();
		int val = ((Integer)helixBaseDistHT.get(dist)).intValue();
		if (val > largestVal)
		{
			largestVal = val;
			largestDistCount = dist;
		}
	}
	if (largestDistCount != null)
		this.setRNAHelixBaseDistance(largestDistCount.doubleValue());

	largestDistCount = null;
	largestVal = 0;
	for (Enumeration e = basePairDistHT.keys() ; e.hasMoreElements() ;)
	{
		Double dist = (Double)e.nextElement();
		int val = ((Integer)basePairDistHT.get(dist)).intValue();
		if (val > largestVal)
		{
			largestVal = val;
			largestDistCount = dist;
		}
	}
	if (largestDistCount != null)
		this.setRNABasePairDistance(largestDistCount.doubleValue());

	largestDistCount = null;
	largestVal = 0;
	for (Enumeration e = misMatchBasePairDistHT.keys() ; e.hasMoreElements() ;)
	{
		Double dist = (Double)e.nextElement();
		int val = ((Integer)misMatchBasePairDistHT.get(dist)).intValue();
		if (val > largestVal)
		{
			largestVal = val;
			largestDistCount = dist;
		}
	}
	if (largestDistCount != null)
		this.setRNAMisMatchBasePairDistance(largestDistCount.doubleValue());

	largestDistCount = null;
	largestVal = 0;
	for (Enumeration e = nucLineLabelWidthHT.keys() ; e.hasMoreElements() ;)
	{
		Double dist = (Double)e.nextElement();
		int val = ((Integer)nucLineLabelWidthHT.get(dist)).intValue();
		if (val > largestVal)
		{
			largestVal = val;
			largestDistCount = dist;
		}
	}
	if (largestDistCount != null)
		this.setNucLabelLineWidth(largestDistCount.doubleValue());

	helixBaseDistHT = null;
	basePairDistHT = null;
	misMatchBasePairDistHT = null;
	nucToNucDistHT = null;
	nucLineLabelWidthHT = null;
}

private Vector xyList = null;

public void
saveXY()
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	if (delineators == null)
		return;
	xyList = new Vector();

	for (int i = 0;i < delineators.size();i+=2)
	{
		NucNode startNuc = (NucNode)delineators.elementAt(i);
		NucNode endNuc = (NucNode)delineators.elementAt(i+1);
		NucNode refNuc = startNuc;
		while (true)
		{
			Nuc2D nuc = (Nuc2D)refNuc;
			xyList.addElement(new Point2D.Double(nuc.getX(), nuc.getY()));

			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc();
		}
	}
}

public boolean
restoreXY()
throws Exception
{
	Vector delineators = this.getItemListDelineators();
	if (delineators == null)
		return (false);
	if (xyList == null)
		return (false);

	Enumeration xyListEnum = xyList.elements();

	for (int i = 0;i < delineators.size();i+=2)
	{
		NucNode startNuc = (NucNode)delineators.elementAt(i);
		NucNode endNuc = (NucNode)delineators.elementAt(i+1);
		NucNode refNuc = startNuc;
		while (true)
		{
			Nuc2D nuc = (Nuc2D)refNuc;
			Point2D pt = (Point2D)xyListEnum.nextElement();
			nuc.setXY(pt.getX(), pt.getY());

			if (refNuc == endNuc)
				break;
			refNuc = refNuc.nextNuc();
		}
	}	

	return (true);
}

public void
printNucs()
throws Exception
{
	debug("IN PRINTNUCS, NUCCOUNT CURRENTLY: " + getNucCount());
	for (int nucID = 1;nucID <= getNucCount();nucID++)
	{
		NucNode nuc = getNucAt(nucID);
		if (nuc == null)
			continue;
		System.out.println(nuc);
	}	
}

public String
toString()
{
	return (this.getName());
}

private static void
debug(String s)
{
	System.err.println("NucCollection-> " + s);
}

}
