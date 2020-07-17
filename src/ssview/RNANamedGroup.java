package ssview;

import java.util.Vector;
import java.io.*;

import util.math.*;

public class
RNANamedGroup
extends NucCollection2D
{

// parent container should be of type SSData

public
RNANamedGroup()
throws Exception
{
	super();
}

public
RNANamedGroup(ComplexScene complexScene, String groupName)
throws Exception
{
	this();
	if (groupName == null)
		throw new Exception("groupName is null in RNANamedGroup constructor");
	this.setGroupName(groupName);
	this.setComplexScene(complexScene);
}

public
RNANamedGroup(NucNode nuc)
throws Exception
{
	this();

	if (nuc.getGroupName() == null)
		throw new ComplexException("Nuc has no group name in RNANamedGroup constructor");

	ComplexCollection nucsComplexScene = (ComplexCollection)nuc.getParentCollection();
	if (nucsComplexScene == null)
		throw new ComplexException("Nuc has no parent collection in RNANamedGroup constructor");

	while (true)
	{
		if (nucsComplexScene instanceof ComplexScene)
			break;
		nucsComplexScene = (ComplexCollection)nucsComplexScene.getParentCollection();
	}
	if (!(nucsComplexScene instanceof ComplexScene))
		throw new ComplexException("Nucs has no complexScene as parent collection in RNANamedGroup constructor");

	this.setGroupName(nuc.getGroupName());
	this.setComplexScene((ComplexScene)nucsComplexScene);
}

private ComplexScene complexScene = null;

public void
setComplexScene(ComplexScene complexScene)
{
    this.complexScene = complexScene;
	this.setParentCollection(complexScene);
}

public ComplexScene
getComplexScene()
{
    return (this.complexScene);
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

// have to worry about base pairing across different strands.
public Vector
getItemListDelineators()
throws Exception
{
	Vector delineators = new Vector();
	NucNode nuc = null;

	for (int graphNodeID = 0;graphNodeID < this.getComplexScene().getItemCount();graphNodeID++)
	{
		Object obj = this.getComplexScene().getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexScene)
		{
			ComplexScene subComplexScene = (ComplexScene)obj;
			for (int complexSceneID = 0;complexSceneID < subComplexScene.getItemCount();complexSceneID++)
			{
				Object subObj = subComplexScene.getItemAt(complexSceneID);
				if (subObj == null)
					continue;
				if (subObj instanceof SSData2D)
				{
					SSData2D sstr = (SSData2D)subObj;
					boolean inNucs = false;
					Nuc2D lastNuc = null;
					for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
					{
						Nuc2D domainNuc = sstr.getNuc2DAt(nucID);
						if (domainNuc == null)
						{
							if (inNucs)
							{
								inNucs = false;
								if (lastNuc != null)
									delineators.add(lastNuc);
								lastNuc = null;
							}
							continue;
						}
						if ((domainNuc.getGroupName() == null) ||
							!domainNuc.getGroupName().equals(this.getGroupName()))
						{
							if (inNucs)
							{
								inNucs = false;
								if (lastNuc != null)
									delineators.add(lastNuc);
							}
							continue;
						}
						if (domainNuc.getID() == sstr.getNucCount())
						{
							if (inNucs)
							{
								delineators.add(domainNuc);
							}
							break;
						}
						if (!inNucs)
						{
							inNucs = true;
							delineators.add(domainNuc);
						}
						lastNuc = domainNuc;
					}
				}
			}
		}
	}

	return(delineators);
}

// used only for RNAListNucs, RNAColorUnit and RNANamedGroup
public boolean
basePairsOutsideOfCollection()
throws Exception
{
	Vector delineators = ((NucCollection2D)this).getItemListDelineators();
	if (delineators == null)
		return (false);

	for (int i = 0;i < delineators.size();i+=2)
	{
		NucNode refNuc = (NucNode)delineators.elementAt(i);
		NucNode endNuc = (NucNode)delineators.elementAt(i+1);
		while (true)
		{
			if (refNuc.isBasePair())
			{
				if (refNuc.getBasePair().getGroupName() == null)
					return (true);
				if (!refNuc.getBasePair().getGroupName().equals(this.getGroupName()))
					return (true);
			}
			if (refNuc.equals(endNuc))
				break;
			refNuc = refNuc.nextNuc();
		}
	}

	return (false);
}

public String
toString()
{
	return("RNA NamedGroup name: " + this.getGroupName());
}

private void
debug(String s)
{
	System.err.println("RNANamedGroup-> " + s);
}

}
