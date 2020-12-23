
package jimage;

import java.awt.Color;
import java.io.CharArrayReader;
import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.StringTokenizer;

import javax.vecmath.Color3f;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import util.FileUtil;
import util.GraphicsParser;
import util.SphereInfo;
import util.SphereInfoList;
import util.StringTokenUtil;

public class
ImageXMLParser
extends GraphicsParser
{

private static boolean debugOn = true;

private SphereInfoList sphereInfoList = null;

public void
setSphereInfoList(SphereInfoList sphereInfoList)
{
	this.sphereInfoList = sphereInfoList;
}

public SphereInfoList
getSphereInfoList()
{
	return (this.sphereInfoList);
}

private SphereInfoList negSphereInfoList = null;

public void
setNegSphereInfoList(SphereInfoList negSphereInfoList)
{
	this.negSphereInfoList = negSphereInfoList;
}

public SphereInfoList
getNegSphereInfoList()
{
	return (this.negSphereInfoList);
}

private SphereInfoList pdbSphereInfoList = null;

public void
setPDBSphereInfoList(SphereInfoList pdbSphereInfoList)
{
	this.pdbSphereInfoList = pdbSphereInfoList;
}

public SphereInfoList
getPDBSphereInfoList()
{
	return (this.pdbSphereInfoList);
}

private float veryWeakHitValue = 60.0f;

public void
setVeryWeakHitValue(float veryWeakHitValue)
{
    this.veryWeakHitValue = veryWeakHitValue;
}

public float
getVeryWeakHitValue()
{
    return (this.veryWeakHitValue);
}

private float weakHitValue = 55.0f;

public void
setWeakHitValue(float weakHitValue)
{
    this.weakHitValue = weakHitValue;
}

public float
getWeakHitValue()
{
    return (this.weakHitValue);
}

private float mediumHitValue = 35.0f;

public void
setMediumHitValue(float mediumHitValue)
{
    this.mediumHitValue = mediumHitValue;
}

public float
getMediumHitValue()
{
    return (this.mediumHitValue);
}

private float strongHitValue = 25.0f;

public void
setStrongHitValue(float strongHitValue)
{
    this.strongHitValue = strongHitValue;
}

public float
getStrongHitValue()
{
    return (this.strongHitValue);
}

private String pdbFileName = null;

public void
setPDBFileName(String pdbFileName)
{
	this.pdbFileName = pdbFileName;
	debug("Setting PDB file: " + this.getPDBFileName());
}

public String
getPDBFileName()
{
	return (this.pdbFileName);
}

private String cloudDataFileName = null;

public void
setCloudDataFileName(String cloudDataFileName)
{
	this.cloudDataFileName = cloudDataFileName;
	debug("Setting Data file: " + this.getCloudDataFileName());
}

public String
getCloudDataFileName()
{
	return (this.cloudDataFileName);
}

private String cloudOutputFileName = null;

public void
setCloudOutputFileName(String cloudOutputFileName)
{
    this.cloudOutputFileName = cloudOutputFileName;
	debug("Setting Output File: " + this.getCloudOutputFileName());
}

public String
getCloudOutputFileName()
{
    return (this.cloudOutputFileName);
}

private float densitySpacing = 1.0f;

public void
setDensitySpacing(float densitySpacing)
{
    this.densitySpacing = densitySpacing;
}

public float
getDensitySpacing()
{
    return (this.densitySpacing);
}

public void
parse(FileReader cloudsFile)
throws Exception
{
	// debug("Parsing ImageXML Document in parse(fileReader)\n\n");

	// need to figure out how to include dtd programmatically
	parse(new InputSource(cloudsFile));
}

public void
startElement(String namespaceURI, String localName,
	String rawName, Attributes atts)
throws SAXException
{
	if (rawName.equals("ImageXMLDocument"))
	{
		// debug("IN PARSE.ImageXMLDocument");
	}
	else if (rawName.equals("Sphere"))
	{
// debug("IN PARSE.Sphere");
		if (this.getSphereInfoList() == null)
			this.setSphereInfoList(new SphereInfoList());
		String sphereStr = atts.getValue("X");
		float sX  = Float.valueOf(sphereStr).floatValue();
		sphereStr = atts.getValue("Y");
		float sY  = Float.valueOf(sphereStr).floatValue();
		sphereStr = atts.getValue("Z");
		float sZ  = Float.valueOf(sphereStr).floatValue();
		sphereStr = atts.getValue("Radius");
		float radius  = Float.valueOf(sphereStr).floatValue();
		sphereStr = atts.getValue("Color");
		if (sphereStr == null)
		{
			this.getSphereInfoList().add(new SphereInfo(
				sX, sY, sZ, radius, new Color3f(1.0f, 1.0f, 1.0f)));
		}
		else
		{
			this.getSphereInfoList().add(new SphereInfo(
				sX, sY, sZ, radius, new Color3f(
				new Color(Integer.valueOf(sphereStr, 16).intValue()))));
		}
	}
	else if (rawName.equals("NegSphere"))
	{
		if (this.getNegSphereInfoList() == null)
			this.setNegSphereInfoList(new SphereInfoList());
		String sphereStr = atts.getValue("X");
		float sX  = Float.valueOf(sphereStr).floatValue();
		sphereStr = atts.getValue("Y");
		float sY  = Float.valueOf(sphereStr).floatValue();
		sphereStr = atts.getValue("Z");
		float sZ  = Float.valueOf(sphereStr).floatValue();
		sphereStr = atts.getValue("Radius");
		float radius  = Float.valueOf(sphereStr).floatValue();
		// all neg spheres are translucent black for now
		this.getNegSphereInfoList().add(new SphereInfo(
			sX, sY, sZ, radius, new Color3f(0.0f, 0.0f, 0.0f)));
	}
	else if (rawName.equals("PDBFile"))
	{
		if (this.getPDBSphereInfoList() == null)
			this.setPDBSphereInfoList(new SphereInfoList());
		try
		{
			this.setPDBFileName(atts.getValue("FileName"));
			getPDBSphereInfoList().readPDB(this.getPDBFileName());
		}
		catch (Exception e)
		{
			this.setPDBSphereInfoList(null);
			debug("Error in setPDBSphereList: " + e.toString());
		}
		/*
		debug("GOT pdb sphere list: " + this.getPDBSphereInfoList().size());
		for (int i = 0;i < this.getPDBSphereInfoList().size();i++)
			debug(((SphereInfo)this.getPDBSphereInfoList().elementAt(i)).toString());
		*/
	}
	else if (rawName.equals("HitValues"))
	{
		String valueStr = atts.getValue("VeryWeakValue");
		if (valueStr != null)
			this.setVeryWeakHitValue(
				Float.valueOf(valueStr).floatValue());
		valueStr = atts.getValue("WeakValue");
		if (valueStr != null)
			this.setWeakHitValue(
				Float.valueOf(valueStr).floatValue());
		valueStr = atts.getValue("MediumValue");
		if (valueStr != null)
			this.setMediumHitValue(
				Float.valueOf(valueStr).floatValue());
		valueStr = atts.getValue("StrongValue");
		if (valueStr != null)
			this.setStrongHitValue(
				Float.valueOf(valueStr).floatValue());
	}
	else if (rawName.equals("DensitySpacing"))
	{
		this.setDensitySpacing(Float.valueOf(atts.getValue("Distance")).floatValue());
	}
	else if (rawName.equals("CloudDataFile"))
	{
		if (this.getPDBSphereInfoList() == null)
			return;
		if (this.getSphereInfoList() == null)
			this.setSphereInfoList(new SphereInfoList());
		try
		{
			this.setCloudDataFileName(atts.getValue("FileName"));
			String cloudData =
				FileUtil.getFileAsString(this.getCloudDataFileName());
			StringTokenizer st = new StringTokenizer(cloudData, "\n");
			while (st.hasMoreTokens())
			{
				String cloudLine = st.nextToken().trim();
				if (cloudLine.length() <= 0)
				{
					continue;
				}
				else if (cloudLine.indexOf("#") == 0)
				{
					continue;
				}
				else if (Character.isDigit(cloudLine.charAt(0)))
				{
					if (this.getPDBSphereInfoList() == null)
						throw new SAXException("Error in ImageXMLParser.endDocument: " +
						"Need to set a pdb file");
					StreamTokenizer streamT = new StreamTokenizer(
						new CharArrayReader(cloudLine.toCharArray()));

					int residueID = StringTokenUtil.getNextIntToken(cloudLine, streamT);
					String hitRadius = StringTokenUtil.getNextStringToken(
						cloudLine, streamT);
					setTarget(residueID, hitRadius, "0f0fff");
				}
				else
				{
					debug("ERROR: " + cloudLine);
				}
			}
		}
		catch (Exception e)
		{
			this.setSphereInfoList(null);
			debug("Error in setSphereList: " + e.toString());
			/*
			throw new SAXException("Error in " +
				" ImageXMLParser.startDocument with CloudDataFile: " +
				e.toString());
			*/
		}
	}
	else if (rawName.equals("CloudOutputFile"))
	{
		this.setCloudOutputFileName(atts.getValue("FileName"));
	}
	else if (rawName.equals("PDBSphere"))
	{
		if (getPDBSphereInfoList() == null)
			throw new SAXException("Error in ImageXMLParser.endDocument: " +
			"Need to set a pdb file");
		setTarget(
			Integer.valueOf(atts.getValue("ResidueID")).intValue(),
			atts.getValue("HitRadius"), atts.getValue("Color"));
	}
}

private void
setTarget(int residueID, String iRadius, String colorStr)
{
	float radius = 0.0f;
	if (iRadius.equals("VeryWeak") ||
		iRadius.equals("veryweak") ||
		iRadius.equals("V") ||
		iRadius.equals("v"))
		radius = getVeryWeakHitValue();
	else if (iRadius.equals("Weak") ||
		iRadius.equals("weak") ||
		iRadius.equals("W") ||
		iRadius.equals("w"))
		radius = getWeakHitValue();
	else if (iRadius.equals("Medium") ||
		iRadius.equals("medium") ||
		iRadius.equals("M") ||
		iRadius.equals("m"))
		radius = getMediumHitValue();
	else if (iRadius.equals("Strong") ||
		iRadius.equals("strong") ||
		iRadius.equals("S") ||
		iRadius.equals("s"))
		radius = getStrongHitValue();

	Color3f sphereColor = null;
	if (colorStr == null)
		sphereColor = new Color3f(1.0f, 1.0f, 1.0f);
	else
		sphereColor = new Color3f(
			new Color(Integer.valueOf(colorStr, 16).intValue()));
	
	boolean isRNA = this.getPDBSphereInfoList().getIsRNA();
	boolean isProtein = this.getPDBSphereInfoList().getIsProtein();
	String residueName = null;
	if (isRNA && isProtein)
	{
		debug("ERROR IN PDBFILE, 'P' and 'CA' represented in first line");
		return;
	}
	else if (!isRNA && !isProtein)
	{
		debug("ERROR IN PDBFILE, 'P' or 'CA' not represented in first line");
		return;
	}
	else if (isRNA)
	{
		residueName = "P";
	}
	else if (isProtein)
	{
		residueName = "CA";
	}

	SphereInfo pdbSphereInfo =
		this.getPDBSphereInfoList().getSphereInfoByResidue(residueName, residueID);

	if (pdbSphereInfo == null)
	{
		debug("ERROR, NOT FOUND: " + residueName + " " + residueID);
		return;
	}

/*
debug("ADD residue: " + residueID + " " +
	pdbSphereInfo.getXPos() + " " +
	pdbSphereInfo.getYPos() + " " +
	pdbSphereInfo.getZPos() + " " +
	radius);
*/

}

public void
endElement(String namespaceURI, String localName, String rawName)
throws SAXException
{
	if (rawName.equals("ImageXMLDocument"))
	{
		/*
		for (int i = 0;i < this.getSphereInfoList().size();i++)
		{
			debug("IN LIST: " + ((SphereInfo)this.getSphereInfoList().
				elementAt(i)).toString());
		}
		*/
	}
	else if (rawName.equals("Sphere"))
	{
	}
	else if (rawName.equals("PDBFile"))
	{
	}
	else if (rawName.equals("HitValues"))
	{
	}
	else if (rawName.equals("CloudDataFile"))
	{
	}
	else if (rawName.equals("PDBSphere"))
	{
	}
	charactersString = null;
}

public void
endDocument()
throws SAXException
{
	/*
	try
	{
	}
	catch (Exception e)
	{
		throw new SAXException("Error in ImageXMLParser.endDocument: ", e);
	}
	*/
}	

public static void
debug(String s)
{
	if (!debugOn)
		return;	
	System.out.println("ImageXMLParser-> " + s);
}

public String generateComplexXML_DTD() {
	// TODO Auto-generated method stub
	return null;
}

public void initParse() throws Exception {
	// TODO Auto-generated method stub
	
}

}
