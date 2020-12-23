package ssview;

import java.io.*;
import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import util.*;

public class
ComplexSceneInitParser
extends GraphicsParser
{

private boolean complexSceneSceneNodeGeomSet = false;
private double figureScale = 1.0;

public void
setFigureScale(double figureScale)
{
    this.figureScale = figureScale;
}

public double
getFigureScale()
{
    return (this.figureScale);
}

private double psScale = 1.00;

public void
setPSScale(double psScale)
{
    this.psScale = psScale;
}

public double
getPSScale()
{
    return (this.psScale);
}

private String complexSceneName = null;

public void
setComplexSceneName(String complexSceneName)
{
    this.complexSceneName = complexSceneName;
}

public String
getComplexSceneName()
{
    return (this.complexSceneName);
}

private boolean landscapeMode = false;

public void
setLandscapeMode(boolean landscapeMode)
{
    this.landscapeMode = landscapeMode;
}

public boolean
getLandscapeMode()
{
    return (this.landscapeMode);
}

public void
initParse()
throws Exception
{
	/*
	if (this.getDTDPath() == null)
	{
		Properties p = new Properties(System.getProperties());
		String xrnaHome = p.getProperty("XRNA_HOME");
		// debug("XRNAHOME: " + xrnaHome);
		if (xrnaHome == null)
			this.setDTDPath(null);
		else
			this.setDTDPath(xrnaHome + "/ssview/ComplexXML.dtd");
		// this.setDTDPath("../ssview/ComplexXML.dtd");
	}
	*/

	complexSceneSceneNodeGeomSet = false;
}

StringBuffer charactersBuf = null;

// override GraphicsParsers characters() for large amount of data
public void
characters(char[] ch, int start, int length)
throws SAXException
{
	charactersString = new String(ch, start, length);

	if (charactersBuf != null)
		charactersBuf.append(charactersString);
}

public void
startElement(String namespaceURI, String localName,
	String rawName, Attributes atts)
throws SAXException
{
	String val = null;

	try
	{
		if (rawName.equals("ComplexDocument"))
		{
			this.setComplexSceneName(atts.getValue("Name"));
			val = atts.getValue("LandscapeMode");
			if (val != null)
				this.setLandscapeMode(val.equalsIgnoreCase("true"));
			val = atts.getValue("PSScale");
			if (val != null)
				this.setPSScale(Double.valueOf(val).doubleValue());
		}
		else if (rawName.equals("SceneNodeGeom"))
		{
			if (!complexSceneSceneNodeGeomSet)
			{
				val = atts.getValue("Scale");
				if (val != null)
					this.setFigureScale(Double.valueOf(val).doubleValue());
				complexSceneSceneNodeGeomSet = true;
			}
		}
	}
	catch (Exception e)
	{
		throw new SAXException(
			"Error in ComplexSceneInitParser.startElement()", e);
	}
}

public void
endElement(String namespaceURI, String localName, String rawName)
throws SAXException
{
	charactersString = null;
}

public void
endDocument()
throws SAXException
{
	try
	{
	}
	catch (Exception e)
	{

	ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(
			new DataOutputStream(excptArray)));
	debug(new String(excptArray.toByteArray()));

		throw new SAXException("Error in ComplexSceneInitParser.endDocument: ", e);

	}
}	

public String
generateComplexXML_DTD()
{
	return (ComplexDTDDefine.complexDTD);
}

public static void
debug(String s)
{
	System.err.println("ComplexSceneInitParser-> " + s);
}

}
