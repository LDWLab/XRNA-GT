package ssview;

import java.awt.geom.*;
import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.tree.*;

import jimage.DrawObjectCollection;
import jimage.DrawObject;
import jimage.DrawCharObject;
import jimage.DrawLineObject;
import jimage.DrawObjectLeafNode;

import util.math.*;

/**
** List of complexs in scene. Contains labels specific to this scene.
*/

public class
ComplexScene
extends ComplexCollection
{

public
ComplexScene()
{
	super();
	this.setCollection(new Vector());

	if (javaVersion == null)
	{
		javaVersion = System.getProperty("java.version");
		System.out.println("java version: " + javaVersion);
		isJDK13_0 = isJDK14_1_01 = false;
		if (javaVersion.equals("1.4.1_01") ||
			javaVersion.equals("1.4.1_02") ||
			javaVersion.equals("1.4.2-beta") ||
			javaVersion.equals("1.4.2_02"))
			isJDK14_1_01 = true;
		else if (javaVersion.equals("1.3.0"))
			isJDK13_0 = true;
		/*
		if (!(isJDK13_0 || isJDK14_1_01))
			System.err.println("WARNING: Untested java version: " + javaVersion);
		*/
	}
}

public
ComplexScene(String name, String author)
{
	this();
	this.setName(name);
	this.setAuthor(author);
}

public
ComplexScene(String name)
{
	this();
	this.setName(name);
}

public
ComplexScene(String name, ComplexSSDataCollection collection)
throws Exception
{
	this();
	this.setName(name);
	this.addItem(collection);
}

public void
addItem(Object sceneGraphNode)
throws Exception
{
	// check if sceneGraphNode is already in
	ComplexScene newComplex = (ComplexScene)sceneGraphNode;
	for(int complexID = 0;complexID < this.getItemCount();complexID++)
	{
		Object obj = this.getItemAt(complexID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexScene)
		{
			ComplexScene complex = (ComplexScene)obj;
			if(complex.getName().equals(newComplex.getName()))
				throw new Exception("Error in ComplexScene.addItem: " +
					newComplex.getName() + " is already in Complex");
		}
	}

	// this list way is just a vector, currently used for making a
	// list of other containers
	((ComplexCollection)sceneGraphNode).setParentCollection(this);
	((Vector)this.getCollection()).addElement(sceneGraphNode);
}

// this is different than one for SSData
// public DrawObjectCollection
public Object
getItemAt(int index)
{
	if (index < 0)
		return (null);
	return((DrawObjectCollection)((Vector)this.getCollection()).elementAt(index));
}

// this is different than one for SSData
public void
setItemAt(Object obj, int ordinalPos)
{
	((Vector)this.getCollection()).setElementAt(obj, ordinalPos);
}

private String name = null;

public void
setName(String name)
{
	this.name = name;
}

public String
getName()
{
	return (this.name);
}

private String author = null;

public void
setAuthor(String author)
{
	this.author = author;
}

public String
getAuthor()
{
	return (this.author);
}

public void
init()
throws Exception
{
	for (int complexID = 0;complexID < this.getItemCount();complexID++)
	{
		Object obj = this.getItemAt(complexID);
		if (obj == null)
			continue;
		((ComplexScene)obj).init();
	}	
}

public ComplexSSDataCollection2D
getComplexSSDataCollection2D(String name)
{
	if (this instanceof ComplexSSDataCollection2D)
		if (this.getName().equals(name))
			return ((ComplexSSDataCollection2D)this);
	
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof Nuc2D)
			continue;
		if (obj instanceof ComplexCollection)
		{
			Object returnObj = ((ComplexScene)obj).getComplexSSDataCollection2D(name);
			if (returnObj != null)
				return ((ComplexSSDataCollection2D)returnObj);
		}
	}

	return (null);
}

public ComplexSSDataCollection
getComplexSSDataCollection(String name)
{
	if (this instanceof ComplexSSDataCollection)
		if (this.getName().equals(name))
			return ((ComplexSSDataCollection)this);
	
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof NucNode)
			continue;
		if (obj instanceof ComplexCollection)
		{
			Object returnObj = ((ComplexScene)obj).getComplexSSDataCollection(name);
			if (returnObj != null)
				return ((ComplexSSDataCollection)returnObj);
		}
	}

	return (null);
}

public SSData
getComplexSSData(String name)
{
	if (this instanceof SSData)
		if (this.getName().equals(name))
			return ((SSData)this);
	
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof NucNode)
			continue;
		if (obj instanceof ComplexCollection)
		{
			Object returnObj = ((ComplexScene)obj).getComplexSSData(name);
			if (returnObj != null)
				return ((SSData)returnObj);
		}
	}

	return (null);
}

public SSData2D
getComplexSSData2D(String name)
{
	if (this instanceof SSData2D)
		if (this.getName().equals(name))
			return ((SSData2D)this);
	
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof Nuc2D)
			continue;
		if (obj instanceof ComplexCollection)
		{
			Object returnObj = ((ComplexScene)obj).getComplexSSData2D(name);
			if (returnObj != null)
				return ((SSData2D)returnObj);
		}
	}

	return (null);
}

public SSData2D
getFirstComplexSSData2D()
{
	if (this instanceof SSData2D)
		return ((SSData2D)this);
	
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof Nuc2D)
			continue;
		if (obj instanceof ComplexCollection)
		{
			Object returnObj = ((ComplexScene)obj).getFirstComplexSSData2D();
			if (returnObj != null)
				return ((SSData2D)returnObj);
		}
	}

	return (null);
}

public static int
getChildLevel(ComplexCollection complexNode)
throws Exception
{
	if (complexNode instanceof ComplexScene2D)
		return (0);
	else if (complexNode instanceof ComplexSSDataCollection2D)
		return (1);
	else if (complexNode instanceof SSData2D)
		return (2);
	throw new Exception("Error in ComplexScene.getChildLevel: " +
		"Unknown level determination for: " + complexNode.toString());
}

public ComplexScene
getChildByName(String name, char delineator)
throws Exception
{
	// debug("parentName: '" + name + "'");

	String childName = null;
	String subChildName = null;

	int delineateIndex = name.indexOf(delineator);
	// debug("delineateIndex: " + delineateIndex);

	if (delineateIndex < 0)
	{
		childName = name;
		subChildName = null;
	}
	else
	{
		childName = name.substring(0, delineateIndex);
		subChildName = name.substring(delineateIndex + 1);
	}
	// debug("childName: '" + childName + "'");
	// debug("subChildName: '" + subChildName + "'");

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		ComplexScene complexCollection = (ComplexScene)obj;
		if (complexCollection.getName() == null)
		{
			// then leaf node, gone too far
			continue;
		}
		if (complexCollection.getName().equals(childName))
		{
			if (subChildName == null)
			{
				// debug("SUBCHILD NAME NULL");
				return (complexCollection);
			}
			return (complexCollection.getChildByName(subChildName, delineator));
		}
	}	
	// debug("RETURNING NULL");
	return (null);
}

// use for single depth
public ComplexScene
getChildByName(String name)
throws Exception
{
	return (getChildByName(name, ':'));
}

public static String
getPickModePrefixTitle(int mode)
{
	switch (mode)
	{
	  case ComplexDefines.InRNASingleNuc :
		return("Single Nuc");
	  case ComplexDefines.InRNASingleStrand :
		return("Single Strand");
	  case ComplexDefines.InRNABasePair :
		return("BasePair");
	  case ComplexDefines.InRNAHelix :
		return("Helix");
	  case ComplexDefines.InRNAHelicalRun :
		return("Stacked Helix");
	  case ComplexDefines.InRNASubDomain :
		return("Sub Domain");
	  case ComplexDefines.InRNANamedGroup :
		return("Named Group");
	  case ComplexDefines.InRNAColorUnit :
		return("Color Unit");
	  case ComplexDefines.InRNACycle :
		return("Cycle");
	  case ComplexDefines.InRNAListNucs :
		return("List Nucs");
	  case ComplexDefines.InRNASSData :
		return("SSData");
	  case ComplexDefines.InComplex :
		return("Complex");
	  case ComplexDefines.InLabelsOnly :
		return("Labels Only");
		/*
	  case ComplexDefines.InComplexArea :
		return("Area");
		*/
	  case ComplexDefines.InComplexScene :
		return("Scene");
	  default :
		return (null);
	}
}

// Need to null these out everytime called
private static DefaultMutableTreeNode complexSceneBranchNode = null;
private static DefaultMutableTreeNode complexCollectionBranchNode = null;
private static DefaultMutableTreeNode ssDataCollectionBranchNode = null;

public void
resetComplexNodes()
{
	complexSceneBranchNode = null;
	complexCollectionBranchNode = null;
	ssDataCollectionBranchNode = null;
}

public TreeNode
createComplexNodes()
{
	if (this instanceof ComplexScene2D)
	{
		// debug("found ComplexScene2D: " + this.getName());
		complexSceneBranchNode = new DefaultMutableTreeNode(this.getName());
	}
	else if (this instanceof ComplexSSDataCollection2D)
	{
		// debug("found ComplexSSDataCollection2D: " + this.getName());
		complexCollectionBranchNode = new DefaultMutableTreeNode(this.getName());;
		complexSceneBranchNode.add(complexCollectionBranchNode);
	}
	else if (this instanceof SSData2D)
	{
		// debug("found SSData2D: " + this.getName());
		ssDataCollectionBranchNode = new DefaultMutableTreeNode(this.getName());;
		complexCollectionBranchNode.add(ssDataCollectionBranchNode);
	}

	/*
	if (getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			debug(drawObject);
		}
	}
	*/

	if (this instanceof SSData2D)
	{
		// bottom out for now.
	}
	else
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		/*
		if (obj instanceof Nuc2D)
		{
			debug("found Nuc2D: " + ((Nuc2D)obj).toString());
			continue;
		}
		*/
		if (obj instanceof ComplexCollection)
			((ComplexScene)obj).createComplexNodes();
	}

	return (complexSceneBranchNode);
}

public TreeNode
createPickModeNodes()
{
	if (this instanceof ComplexScene2D)
	{
		complexSceneBranchNode = new DefaultMutableTreeNode(this.getName());
		complexSceneBranchNode.add(new DefaultMutableTreeNode("Single Nuc"));
		complexSceneBranchNode.add(new DefaultMutableTreeNode("Helix"));
		complexSceneBranchNode.add(new DefaultMutableTreeNode("Labels"));
	}
	else if (this instanceof ComplexSSDataCollection2D)
	{
		complexCollectionBranchNode = new DefaultMutableTreeNode(this.getName());;
		complexCollectionBranchNode.add(new DefaultMutableTreeNode("SSingle Nuc"));
		complexCollectionBranchNode.add(new DefaultMutableTreeNode("Helix"));
		complexCollectionBranchNode.add(new DefaultMutableTreeNode("Labels"));
		complexSceneBranchNode.add(complexCollectionBranchNode);
	}
	else if (this instanceof SSData2D)
	{
		ssDataCollectionBranchNode = new DefaultMutableTreeNode(this.getName());;
		ssDataCollectionBranchNode.add(new DefaultMutableTreeNode("Single Nuc"));
		ssDataCollectionBranchNode.add(new DefaultMutableTreeNode("Helix"));
		ssDataCollectionBranchNode.add(new DefaultMutableTreeNode("Labels"));
		complexCollectionBranchNode.add(ssDataCollectionBranchNode);
	}

	if (this instanceof SSData2D)
	{
		// bottom out for now.
	}
	else
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		/*
		if (obj instanceof Nuc2D)
		{
			debug("found Nuc2D: " + ((Nuc2D)obj).toString());
			continue;
		}
		*/
		if (obj instanceof ComplexCollection)
			((ComplexScene)obj).createPickModeNodes();
	}

	return (complexSceneBranchNode);
}

public static String
printDrawObjectPath(DrawObjectCollection graphNode)
{
	if (graphNode == null)
		return (null);
	StringBuffer pathName = new StringBuffer();
	DrawObjectCollection ptr =
		(DrawObjectCollection)graphNode.getParentCollection();
	
	String cmpString = null;
	while(ptr != null)
	{
		if (!((ptr instanceof Nuc2D) && (graphNode instanceof DrawCharObject)))
		{
			String currString = ptr.toString();
			if (cmpString == null)
			{
				cmpString = currString;
				pathName.insert(0, new String('(' + currString + ')'));
			}
			else if (!cmpString.equals(currString))
			{
				cmpString = currString;
				pathName.insert(0, new String('(' + currString + ')'));
			}
		}
		ptr = (DrawObjectCollection)ptr.getParentCollection();
	}
	String path = pathName.toString();
	if (path == null)
	{
		return("Found: " + graphNode.toString());
	}
	else
	{
		if ((graphNode.getParentCollection() instanceof Nuc2D) &&
			(graphNode instanceof DrawCharObject))
		{ // then found nuc
			Nuc2D nuc = (Nuc2D)graphNode.getParentCollection();
			if (nuc.isBasePair())
			return("Found BasePaired Nuc: " +
				'(' + graphNode.getParentCollection().toString() + ") in " + 
					pathName.toString());
			else
			return("Found Nuc: " +
				'(' + graphNode.getParentCollection().toString() + ") in " + 
					pathName.toString());
		}
		else // found label
		{
			return("Found: " +
				'(' + graphNode.toString() + ") in " + pathName.toString());
		}
	}
}

public void
traverseScenePrintNames(Vector objNameVector)
{
	if (objNameVector == null)
		return;
	/*
	if (this instanceof NucNode)
		objNameVector.add(this.toString());
	else
	*/
	objNameVector.add(this.getName());
	if (getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			objNameVector.add(drawObject.toString());
		}
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexScene)
			((ComplexScene)obj).traverseScenePrintNames(objNameVector);
	}
}

public void
traverseSceneWithVectors()
throws Exception
{
	if (getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			Object obj = e.nextElement();
			// debug("Found Leaf Node: " + obj);
		}
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		// debug ("found collection: " + obj);
		if (obj instanceof ComplexScene)
			((ComplexScene)obj).traverseSceneWithVectors();
		else
			throw new Exception("HUH??: " + obj);
	}
}

static int sceneDepth = -1;

public void
traverseScene()
{
	sceneDepth++;
	StringBuffer tabsBuf = new StringBuffer();
	for (int i = 0;i < sceneDepth;i++)
		tabsBuf.append("    ");
	String tabs = tabsBuf.toString();

	/*
	if (this instanceof NucNode)
		debug(tabs + "found nucnode: " + this.toString());
	else
	*/
		debug(tabs + "found: " + this.getName() + "\n");
	if (getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			debug(tabs + "found label: " + drawObject);
		}
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexCollection)
			((ComplexScene)obj).traverseScene();
	}
	sceneDepth--;
}

public void
traverseSceneCenterScene(double x, double y)
throws Exception
{
	this.shiftX(x);
	this.shiftY(y);
	if (getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			drawObject.shiftX(x);
			drawObject.shiftY(y);
		}
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexScene)
			((ComplexScene)obj).traverseSceneCenterScene(x, y);
	}
}

public void
traverseSceneShowBoundingBox(boolean show)
{
	this.setShowBoundingBox(show);
	if (getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			drawObject.setShowBoundingBox(show);
		}
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexScene)
			((ComplexScene)obj).traverseSceneShowBoundingBox(show);
	}
}

public void
traverseSceneShowBoundingShape(boolean show)
{
	this.setShowBoundingShape(show);
	
	// debug("for " + this.getName() + ": " + this.getBoundingShape());
	if (getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			drawObject.setShowBoundingShape(show);
		}
	}

	// this is special case where items begin at 1 instead of 0
	if (this instanceof SSData2D)
	{
		SSData2D sstr = (SSData2D)this;
		for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			nuc.setShowBoundingShape(show);
			if (nuc.getLabelList() != null)
			{
				for (Enumeration e = nuc.getLabelList().elements();e.hasMoreElements();)
				{
					DrawObject drawObject = (DrawObject)e.nextElement();
					drawObject.setShowBoundingShape(show);
				}
			}
		}
	}
	else
	{
		for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
		{
			Object obj = this.getItemAt(graphNodeID);
			if (obj == null)
				continue;
			if (obj instanceof ComplexScene)
				((ComplexScene)obj).traverseSceneShowBoundingShape(show);
		}
	}
}

/*
public void
resetNucLabelsLineLengths(double length)
{
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof SSData2D)
		{
			SSData2D sstr = (SSData2D)obj;
			for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
			{
				Nuc2D nuc = sstr.getNuc2DAt(nucID);
				if (nuc == null)
					continue;
				if (nuc.getLabelList() == null)
					continue;
				for (Enumeration e = nuc.getLabelList().elements();e.hasMoreElements();)
				{
					DrawObject drawObject = (DrawObject)e.nextElement();
					if (!(drawObject instanceof DrawLineObject))
						continue;
					((DrawLineObject)drawObject).setFromLength(length);
				}
			}
		}
		if (obj instanceof ComplexScene)
			((ComplexScene)obj).resetNucLabelsLineLengths(length);
	}
}
*/

public Nuc2D
checkNucLabelsLineEndPts()
{
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof SSData2D)
		{
			SSData2D sstr = (SSData2D)obj;
			for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
			{
				Nuc2D nuc = sstr.getNuc2DAt(nucID);
				if (nuc == null)
					continue;
				if (nuc.getLabelList() == null)
					continue;
				Point2D originPt = new Point2D.Double();
				for (Enumeration e = nuc.getLabelList().elements();e.hasMoreElements();)
				{
					DrawObject drawObject = (DrawObject)e.nextElement();
					if (!(drawObject instanceof DrawLineObject))
						continue;
					BLine2D line = ((DrawLineObject)drawObject).getBLine2D();
					if (line.getP1().distance(originPt) >
						line.getP2().distance(originPt))
						return (nuc);
				}
			}
		}
		if (obj instanceof ComplexScene)
			return (((ComplexScene)obj).checkNucLabelsLineEndPts());
	}
	return (null);
}

public Font
getBestGuessNucLabelFont()
{
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		ComplexScene obj = (ComplexScene)this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		for (int objID = 0;objID < obj.getItemCount();objID++)
		{
			Object subObj = obj.getItemAt(objID);
			if (subObj == null)
				continue;
			if (subObj instanceof SSData2D)
			{
				SSData2D sstr = (SSData2D)subObj;
				Font sstrBestGuessFont = sstr.getBestGuessNucLabelFont(2);
				if (sstrBestGuessFont != null)
					return (sstrBestGuessFont);
			}
		}
	}
	return (null);
}

public String
toString()
{
	return (this.getName());
}

private void
debug(String s)
{
	System.err.println("ComplexScene-> " + s);
}

}
