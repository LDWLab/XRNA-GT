package ssview;

import java.awt.Color;
import java.util.Vector;
import java.io.*;

import util.math.*;

public class
RNAColorUnit
extends NucCollection2D
{

// parent container should be of type SSData

public
RNAColorUnit()
throws Exception
{
	super();
}

public
RNAColorUnit(ComplexScene complexScene, Color unitColor)
throws Exception
{
	this();
	this.set(complexScene, unitColor);
	this.setParentCollection(complexScene);
}

public void
set(ComplexScene complexScene, Color unitColor)
throws Exception
{
	this.setComplexScene(complexScene);
	this.setUnitColor(unitColor);
}

private ComplexScene complexScene = null;

public void
setComplexScene(ComplexScene complexScene)
{
    this.complexScene = complexScene;
}

public ComplexScene
getComplexScene()
{
    return (this.complexScene);
}

private Color unitColor = null;

public void
setUnitColor(Color unitColor)
{
    this.unitColor = unitColor;
}

public Color
getUnitColor()
{
    return (this.unitColor);
}

public void
setColor(Color color)
throws Exception
{
	super.setColor(color);
	this.setUnitColor(color);
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

// Don't have to worry about basepairing across different strands as this is just a
// collection of all nucs of the same color in the entire scene.
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
						Nuc2D colorUnitNuc = sstr.getNuc2DAt(nucID);
						if (colorUnitNuc == null)
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
						if (!colorUnitNuc.getColor().equals(this.getUnitColor()))
						{
							if (inNucs)
							{
								inNucs = false;
								if (lastNuc != null)
									delineators.add(lastNuc);
								// lastNuc = null;
							}
							continue;
						}
						if (colorUnitNuc.getID() == sstr.getNucCount())
						{
							if (inNucs)
							{
								delineators.add(colorUnitNuc);
							}
							break;
						}
						if (!inNucs)
						{
							inNucs = true;
							delineators.add(colorUnitNuc);
						}
						lastNuc = colorUnitNuc;
					}
				}
			}
		}
	}

	return(delineators);
}

public boolean
basePairsOutsideOfCollection()
throws Exception
{
	Vector delineators = ((NucCollection2D)this).getItemListDelineators();
	for (int i = 0;i < delineators.size();i+=2)
	{
		NucNode refNuc = (NucNode)delineators.elementAt(i);
		NucNode endNuc = (NucNode)delineators.elementAt(i+1);
		while (true)
		{
			if (refNuc.isBasePair() && !refNuc.getBasePair().getColor().equals(this.getUnitColor()))
				return (true);
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
	return("RNA Color Unit, ref Color: " + this.getColor());
}

private void
debug(String s)
{
	System.err.println("RNAColorUnit-> " + s);
}

}
