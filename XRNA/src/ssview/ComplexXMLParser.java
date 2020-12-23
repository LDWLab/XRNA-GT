package ssview;

import java.io.*;
import java.util.*;
import java.awt.Color;
import java.awt.Font;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import util.*;

import jimage.DrawObject;
import jimage.DrawStringObject;
import jimage.DrawCharObject;
import jimage.DrawAdobeObject;
import jimage.DrawLineObject;
import jimage.DrawCircleObject;
import jimage.DrawArrowObject;
import jimage.DrawTriangleObject;
import jimage.DrawParallelogramObject;

public class
ComplexXMLParser
extends GraphicsParser
{

private int startNucID = 1;
private String nucChars = null;
private String listDataType = null;
private static boolean debugOn = false;
public static boolean testDebugOn = false;

double labelXPos = 0.0;
double labelYPos = 0.0;
Color labelColor = Color.black;

private Stack complexDefineStack = null;

private ComplexCollection complexCollection = null;

private boolean complexSceneSceneNodeGeomSet = false;

public void
setComplexCollection(ComplexCollection complexCollection)
{
	this.complexCollection = complexCollection;
}

public ComplexCollection
getComplexCollection()
{
	return (this.complexCollection);
}

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

public void
initParse()
throws Exception
{
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

	this.setComplexScene(null);
	complexSceneSceneNodeGeomSet = false;
}

private ComplexScene2D complexScene = null;

public void
setComplexScene(ComplexScene2D complexScene)
{
	this.complexScene = complexScene;
}

public ComplexScene2D
getComplexScene()
{
	return (this.complexScene);
}

private ComplexSSDataCollection2D complex = null;

public void
setComplex(ComplexSSDataCollection2D complex)
{
	this.complex = complex;
}

public ComplexSSDataCollection2D
getComplexSSDataCollection()
{
	return (this.complex);
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

private void
addLabel(DrawObject label)
{
	int stackType = ((Integer)complexDefineStack.peek()).intValue();
	if (stackType == ComplexDefines.InComplexScene)
	{
		this.getComplexScene().addLabel(label);
	}
	else if (stackType == ComplexDefines.InComplex)
	{
		this.getComplexSSDataCollection().addLabel(label);
	}
	else if (this.getComplexCollection() instanceof SSData2D)
	{
		switch( ((Integer)complexDefineStack.peek()).intValue())
		{
		  case ComplexDefines.InRNASingleNuc :
			this.getComplexSSDataCollection().getCurrentSSData().getCurrentItem().addLabel(label);
			break;
		  case ComplexDefines.InRNASingleStrand :
			// System.out.println("Dealing with Single Strand");
			break;
		  case ComplexDefines.InRNABasePair :
			// System.out.println("Dealing with Base Pair");
			break;
		  case ComplexDefines.InRNAHelix :
			// System.out.println("Dealing with Helix");
			break;
		  case ComplexDefines.InRNASubDomain :
			// System.out.println("Dealing with Sub Domain");
			break;
		  case ComplexDefines.InRNANamedGroup :
			// System.out.println("Dealing with Domain");
			break;
		  case ComplexDefines.InRNACycle :
			// System.out.println("Dealing with Cycle");
			break;
		  case ComplexDefines.InRNASSData :
			// System.out.println("Dealing with SSData");
			((SSData2D)this.getComplexCollection()).addLabel(label);
			break;
		  case ComplexDefines.InRNAListNucs :
			// System.out.println("Dealing with ListNucs");
			break;
		  case ComplexDefines.InLabelsOnly :
			// System.out.println("Dealing with Labels Only");
			break;
		  case ComplexDefines.InRNAHelicalRun :
			// System.out.println("Dealing with Helix");
			break;
		  default :
			System.out.println("Dealing with NADA");
			break;
		}
	}
}

// NEED to figure out if this is best place for this.
static public DrawObject
parseDrawObjectDataLine(String dataLine)
throws Exception
{
	StringTokenizer st = new StringTokenizer(dataLine);
	while (st.hasMoreTokens())
	{
		String token = st.nextToken();

		// l 303.00 -309.00 314.00 -327.00 0.00 0 0 0 0 0 0
		if (token.equals("l"))
		{
			double x1 = Double.parseDouble(st.nextToken());
			double y1 = Double.parseDouble(st.nextToken());
			double x2 = Double.parseDouble(st.nextToken());
			double y2 = Double.parseDouble(st.nextToken());

			double lineWidth = Double.parseDouble(st.nextToken());
			// TESTING reseting linewidth from 0.0 to 1.0 just for comparison
			if (lineWidth < GraphicsUtil.MIN_LINEWIDTH)
				lineWidth = GraphicsUtil.MIN_LINEWIDTH;
			Color nucFontColor = new Color(Integer.valueOf(st.nextToken(), 16).intValue());

			double overlayBorder = Double.parseDouble(st.nextToken());
			// st.nextToken();

			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();

			DrawLineObject lineObj = new DrawLineObject(x1, y1, x2, y2, lineWidth, nucFontColor);
			if (overlayBorder > 0.0)
			{
				lineObj.setOverlayBorder(overlayBorder);
				lineObj.reset(x1, y1, x2, y2);
			}
			return (lineObj);
		}
		//     x       y    ang  size font color
		// s 411.00 -141.00 0.0  12    2     0     "ACCEPTOR"
		else if (token.equals("s"))
		{
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			st.nextToken(); // bypass angle

			int strFontSize = Integer.parseInt(st.nextToken());
			int strFontType = Integer.parseInt(st.nextToken());

			Color strFontColor = new Color(Integer.valueOf(st.nextToken(), 16).intValue());
			Font strFont = StringUtil.ssFontToFont(strFontType, strFontSize);

			// read till begin quoted string
			st.nextToken("\"");
			String str = st.nextToken("\"");
			
			if (strFontType == 12)
			{
				DrawAdobeObject adobeDrawObject = null;
				if (str.equals("Y"))
				{
					adobeDrawObject = new DrawAdobeObject(x, y,
						strFont, strFontColor, '\u03a8');
				}
				else if (str.equals("y"))
				{
					adobeDrawObject = new DrawAdobeObject(x, y,
						strFont, strFontColor, '\u03c8');
				}
				return (adobeDrawObject);
			}
			else
			{
				return (new DrawStringObject(
					x, y, strFont, strFontColor, str));
			}
		}
		else if (token.equals("S"))
		{
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			st.nextToken(); // bypass angle

			int strFontSize = Integer.parseInt(st.nextToken());
			int strFontType = Integer.parseInt(st.nextToken());

			Color strFontColor = new Color(Integer.valueOf(st.nextToken(), 16).intValue());
			Font strFont = StringUtil.ssFontToFont(strFontType, strFontSize);

			// read till begin quoted string
			st.nextToken("\"");
			String str = st.nextToken("\"");

			DrawAdobeObject adobeDrawObject = null;
			if (str.equals("u03a8"))
			{
				adobeDrawObject = new DrawAdobeObject(x, y,
					strFont, strFontColor, '\u03a8');
			}
			else if (str.equals("u03c8"))
			{
				adobeDrawObject = new DrawAdobeObject(x, y,
					strFont, strFontColor, '\u03c8');
			}
			return (adobeDrawObject);
		}
		//     x       y    ang  size font color
		// c 411.00 -141.00 0.0  12    2     0     "a"
		else if (token.equals("c"))
		{
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			st.nextToken(); // bypass angle

			int strFontSize = Integer.parseInt(st.nextToken());
			int strFontType = Integer.parseInt(st.nextToken());

			Color strFontColor = new Color(Integer.valueOf(st.nextToken(), 16).intValue());
			Font strFont = StringUtil.ssFontToFont(strFontType, strFontSize);

			// read till begin quoted string
			st.nextToken("\"");
			String str = st.nextToken("\"");

			if (strFontType == 12)
			{
				DrawAdobeObject adobeDrawObject = null;
				if (str.equals("Y"))
				{
					adobeDrawObject = new DrawAdobeObject(x, y,
						strFont, strFontColor, '\u03a8');
				}
				else if (str.equals("y"))
				{
					adobeDrawObject = new DrawAdobeObject(x, y,
						strFont, strFontColor, '\u03c8');
				}
				return (adobeDrawObject);
			}
			else
			{
				return (new DrawCharObject(
					x, y, strFont, strFontColor, str.charAt(0)));
			}
		}
		else if (token.equals("r")) // arrow label
		{
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			double height = Double.parseDouble(st.nextToken());
			double baseWidth = Double.parseDouble(st.nextToken());
			double tailLength = Double.parseDouble(st.nextToken());
			double tailWidth = Double.parseDouble(st.nextToken());
			double baseIndent = Double.parseDouble(st.nextToken());
			double angle = Double.parseDouble(st.nextToken());
			double lineWidth = Double.parseDouble(st.nextToken());

			// TESTING reseting linewidth from 0.0 to 1.0 just for comparison
			if (lineWidth < GraphicsUtil.MIN_LINEWIDTH)
				lineWidth = GraphicsUtil.MIN_LINEWIDTH;
			int isOpenInt = Integer.parseInt(st.nextToken());
			boolean isOpen = (isOpenInt == 1);
			Color arrowColor = new Color(Integer.valueOf(st.nextToken(), 16).intValue());
			
			return (new DrawArrowObject(x, y, height, baseWidth, tailLength,
				tailWidth, baseIndent, angle,
				lineWidth, isOpen, arrowColor));

		}
		else if (token.equals("t"))
		{
//t 392.10 43.21 0.00 6.00 -5.00 -4.00 5.00 -4.00 0.00 1.00 0 0
//t -44.0 -27.5  0.0  2.5  -2.5  -2.5  2.5  -2.5  0.0  0.5  1 0
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			double topX = Double.parseDouble(st.nextToken());
			double topY = Double.parseDouble(st.nextToken());
			double leftX = Double.parseDouble(st.nextToken());
			double leftY = Double.parseDouble(st.nextToken());
			double rightX = Double.parseDouble(st.nextToken());
			double rightY = Double.parseDouble(st.nextToken());
			double angle = Double.parseDouble(st.nextToken());
			double lineWidth = Double.parseDouble(st.nextToken());

			// TESTING reseting linewidth from 0.0 to 1.0 just for comparison
			if (lineWidth < GraphicsUtil.MIN_LINEWIDTH)
				lineWidth = GraphicsUtil.MIN_LINEWIDTH;
			int isOpenInt = Integer.parseInt(st.nextToken());
			boolean isOpen = (isOpenInt == 1);
			Color triangleColor = new Color(Integer.valueOf(st.nextToken(), 16).intValue());
			/*
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			st.nextToken();
			*/
			
			return (new DrawTriangleObject(x, y,
				topX, topY, leftX, leftY, rightX, rightY,
				angle, lineWidth, isOpen, triangleColor));

		}
// p 382.0 -223.0 0.0 25.0 23.0 115.0 10.0 0 0.0 1 0
// p 343.0 -222.0 0.0 -23068.0 23.0 62.0 10.0 0 0.0 1 0
		else if (token.equals("p"))
		{
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			double angle1 = Double.parseDouble(st.nextToken());
			double side1 = Double.parseDouble(st.nextToken());
			double angle2 = Double.parseDouble(st.nextToken());
			double side2 = Double.parseDouble(st.nextToken());
			double lineWidth = Double.parseDouble(st.nextToken());

			// TESTING reseting linewidth from 0.0 to 1.0 just for comparison
			if (lineWidth < GraphicsUtil.MIN_LINEWIDTH)
				lineWidth = GraphicsUtil.MIN_LINEWIDTH;
			int isOpenInt = Integer.parseInt(st.nextToken());
			boolean isOpen = (isOpenInt == 1);
			Color objColor = new Color(Integer.valueOf(st.nextToken(), 16).intValue());
// debug("parallelogram: " + x + " " + y + " " + angle1 + " " + side1 + " " + angle2 + " " + side2 + " " + lineWidth + " " + isOpen + " " + objColor);
			return (new DrawParallelogramObject(x, y, angle1, side1,
				angle2, side2, lineWidth, isOpen, objColor));
		}
// a 494.00 -327.00 0.00 307 509.01 749.81 21.00 0.00 1 0
		else if (token.equals("a"))
		{
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			double radius = Double.parseDouble(st.nextToken());
			double arc0 = Double.parseDouble(st.nextToken());
			double arc1 = Double.parseDouble(st.nextToken());
			double lineWidth = Double.parseDouble(st.nextToken());

			// TESTING reseting linewidth from 0.0 to 1.0 just for comparison
			if (lineWidth < GraphicsUtil.MIN_LINEWIDTH)
				lineWidth = GraphicsUtil.MIN_LINEWIDTH;
			int isOpenInt = Integer.parseInt(st.nextToken());
			boolean isOpen = (isOpenInt == 1);
			Color objColor = new Color(Integer.valueOf(st.nextToken(), 16).intValue());
			return (new DrawCircleObject(x, y, radius, arc0, arc1,
				objColor, isOpen, lineWidth));
		}
	}

	return (null);
}

public void
startElement(String namespaceURI, String localName,
	String rawName, Attributes atts)
throws SAXException
{
	try
	{
		complexXMLStartElement(namespaceURI, localName, rawName, atts);
	}
	catch (SAXException e)
	{
		throw (e);
	}
	catch (Exception e)
	{
		throw new SAXException(
			"Error in ComplexXMLParser.startElement()", e);
	}
}

private void
complexXMLStartElement(String namespaceURI, String localName,
	String rawName, Attributes atts)
throws Exception
{
	String val = null;

	//System.out.println("ComplexXMLParser.java complexXMLStartElement() used");
	
	if (rawName.equals("ComplexDocument"))
	{
		debugParse("ComplexDocument.Name: " + atts.getValue("Name"));
		debugParse("ComplexDocument.Author: " + atts.getValue("Author"));

		/*
		this.setComplexScene(new ComplexScene2D(
			atts.getValue("Name"), atts.getValue("Author")));
		
		complexDefineStack = new Stack();
		complexDefineStack.push(new Integer(ComplexDefines.InComplexScene));
		*/

		if (this.getComplexScene() == null)
		{
			complexSceneSceneNodeGeomSet = false;
			this.setComplexScene(new ComplexScene2D(
				atts.getValue("Name"), atts.getValue("Author")));

			val = atts.getValue("PSScale");
			if (val != null)
				this.getComplexScene().setPSScale(Double.valueOf(val).doubleValue());

			val = atts.getValue("LandscapeMode");
			this.getComplexScene().setLandscapeMode(false);
			if ((val != null) && (val.toLowerCase().equals("true")))
				this.getComplexScene().setLandscapeMode(true);

			complexDefineStack = new Stack();
			complexDefineStack.push(new Integer(ComplexDefines.InComplexScene));
		}
	}
	else if (rawName.equals("Complex"))
	{
		debugParse("Complex.Name: " + atts.getValue("Name"));
		debugParse("Complex.Author: " + atts.getValue("Author"));

		this.setComplex(new ComplexSSDataCollection2D(atts.getValue("Name"),
			atts.getValue("Author")));

		try
		{
			this.getComplexScene().addItem(this.getComplexSSDataCollection());
		}
		catch (Exception e)
		{
			throw new SAXException(
				"Error in ComplexXMLParser.startElement()", e);
		}
		complexDefineStack.push(new Integer(ComplexDefines.InComplex));
	}
	else if (rawName.equals("WithComplex"))
	{
		debugParse("Complex.Name: " + atts.getValue("Name"));
		debugParse("Complex.Author: " + atts.getValue("Author"));

		try
		{
			this.setComplex((ComplexSSDataCollection2D)
				this.getComplexScene().getChildByName(atts.getValue("Name")));
			if (this.getComplexSSDataCollection() == null)
				throw new Exception("Complex is null");
		}
		catch (Exception e)
		{
			throw new SAXException(
				"Error in ComplexXMLParser.startElement().WithComplex", e);
		}

		/* NEED to lookup existing Complex
		this.setComplex(new ComplexSSDataCollection2D(atts.getValue("Name"),
			atts.getValue("Author")));

		try
		{
			this.getComplexScene().addItem(this.getComplexSSDataCollection());
		}
		catch (Exception e)
		{
			throw new SAXException(
				"Error in ComplexXMLParser.startElement()", e);
		}
		*/

		complexDefineStack.push(new Integer(ComplexDefines.InComplex));
	}
	else if (rawName.equals("RNAMolecule"))
	{
		if (this.getComplexSSDataCollection() == null)
			throw new SAXException(
				"\n\nError in ComplexXMLParser.startElement().RNAMolecule: " +
					"\nNo Complex Set for SSData\n");

		debugParse("RNAMolecule.Name: " + atts.getValue("Name"));
		// currently not doing anything with author
		debugParse("RNAMolecule.Author: " + atts.getValue("Author"));

		try
		{
			complexDefineStack.push(new Integer(ComplexDefines.InRNASSData));
			this.getComplexSSDataCollection().setModeType(ComplexDefines.InRNASSData);
			this.setComplexCollection(new SSData2D(atts.getValue("Name")));
			this.getComplexSSDataCollection().setNewSSComplexElement((SSData2D)this.getComplexCollection());
		}
		catch (Exception e)
		{
			/*
			ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(
				new DataOutputStream(excptArray)));
			debug(e.toString() + (new String(excptArray.toByteArray())));
			*/
			throw new SAXException(
				"Error in ComplexXMLParser.startElement().RNAMolecule", e);
		}
	}
	else if (rawName.equals("Nuc"))
	{
		try
		{
			char nucChar = '\0';
			double xPos = -Double.MAX_VALUE;
			double yPos = -Double.MAX_VALUE;
			Color nucColor = null;
			int fontID = -1;
			int fontSize = 0;
			boolean isHidden = false;
			String groupName = null;
			boolean isSchematic = false;
			Color schematicColor = null;
			double schematicLineWidth = -Double.MAX_VALUE;
			double schematicBPLineWidth = -Double.MAX_VALUE;
			double schemBPGap = -Double.MAX_VALUE;
			double schemFPGap = -Double.MAX_VALUE;
			double schemTPGap = -Double.MAX_VALUE;
			boolean isSchematicSet = false;
			boolean isHiddenSet = false;
			boolean groupNameSet = false;
			boolean isNucPath = false;
			Color nucPathColor = null;
			double nucPathLineWidth = -Double.MAX_VALUE;
			boolean isNucPathSet = false;

			// get nuc attributes
			val = atts.getValue("NucChar");
			if (val != null)
				nucChar = val.charAt(0);
			val = atts.getValue("XPos");
			if (val != null)
				xPos = Double.valueOf(val).doubleValue();
			val = atts.getValue("YPos");
			if (val != null)
				yPos = Double.valueOf(val).doubleValue();
			val = atts.getValue("Color");
			if (val != null)
				nucColor = new Color(Integer.valueOf(val, 16).intValue());
			val = atts.getValue("FontID");
			if (val == null)
				fontID = -1;
			else
				fontID = Integer.valueOf(val).intValue();
			val = atts.getValue("FontSize");
			if (val == null)
				fontSize = 0;
			else
				fontSize = Integer.valueOf(val).intValue();
			
			val = atts.getValue("IsSchematic");
			if (val != null)
			{
				if (val.toLowerCase().equals("true"))
					isSchematic = true;
				else
					isSchematic = false;
				isSchematicSet = true;
			}
			val = atts.getValue("SchematicColor");
			if (val != null)
				schematicColor = new Color(Integer.valueOf(val, 16).intValue());
			val = atts.getValue("SchematicLineWidth");
			if (val != null)
				schematicLineWidth = Double.valueOf(val).doubleValue();
			val = atts.getValue("SchematicBPLineWidth");
			if (val != null)
				schematicBPLineWidth = Double.valueOf(val).doubleValue();

			val = atts.getValue("IsHidden");
			if ((val != null) && (val.toLowerCase().equals("true")))
			{
				isHidden = true;
				isHiddenSet = true;
			}

			val = atts.getValue("GroupName");
			if (val != null)
			{
				groupName = val;
				groupNameSet = true;
			}

			val = atts.getValue("SchematicBPGap");
			if (val != null)
				schemBPGap = Double.valueOf(val).doubleValue();
			val = atts.getValue("SchematicFPGap");
			if (val != null)
				schemFPGap = Double.valueOf(val).doubleValue();
			val = atts.getValue("SchematicTPGap");
			if (val != null)
				schemTPGap = Double.valueOf(val).doubleValue();
			
			val = atts.getValue("IsNucPath");
			if (val != null)
			{
				if (val.toLowerCase().equals("true"))
					isNucPath = true;
				else
					isNucPath = false;
				isNucPathSet = true;
			}
			val = atts.getValue("NucPathColor");
			if (val != null)
				nucPathColor = new Color(Integer.valueOf(val, 16).intValue());
			val = atts.getValue("NucPathLineWidth");
			if (val != null)
				nucPathLineWidth = Double.valueOf(val).doubleValue();

			Nuc2D nuc = null;
			complexDefineStack.push(new Integer(ComplexDefines.InRNASingleNuc));

			// Why do I do this?
			this.getComplexSSDataCollection().setModeType(ComplexDefines.InRNASingleNuc);

			// can only have one of RefIDs, RefID, or NucID

			val = atts.getValue("RefIDs");
			if (val != null)
			{
				StringTokenizer refIDsST = new StringTokenizer(val, ",");
				while (refIDsST.hasMoreTokens())
				{
					String nucRangeStr = refIDsST.nextToken().trim();

					StringTokenizer nucRangeST = new StringTokenizer(nucRangeStr, "-");
					if ((nucRangeST.countTokens() < 1) || (nucRangeST.countTokens() > 2))
					{
						throw new SAXException(
							"\nError in ComplexXMLParser.startElm().Nuc.RefIDs" +
							" badly formed RefIDs value: " + val + "\n");
					}

					int refID0 = 0;
					int refID1 = 0;

					if (nucRangeST.countTokens() == 1)
					{
						refID0 = refID1 = Integer.valueOf(nucRangeST.nextToken().trim()).intValue();
					}
					else if (nucRangeST.countTokens() == 2)
					{
						refID0 = Integer.valueOf(nucRangeST.nextToken().trim()).intValue();
						refID1 = Integer.valueOf(nucRangeST.nextToken().trim()).intValue();
					}

					for (int nucID = refID0;nucID <= refID1;nucID++)
					{
						nuc = (Nuc2D)this.getComplexSSDataCollection().getCurrentSSData().getItemAt(nucID);

						if (nuc == null)
							continue;

						/*
						throw new SAXException(
							"\nError in ComplexXMLParser.startElm().Nuc" +
							" referring to null nuc " + nucID +
							" in:\n" +
						this.getComplexSSDataCollection().getCurrentSSData().getName() +
						" in range: " + refID0 + "->" + refID1);
						*/

						if (nucChar != '\0')
							nuc.setNucChar(nucChar);
						if (xPos > -Double.MAX_VALUE)
							nuc.setX(xPos);
						if (yPos > -Double.MAX_VALUE)
							nuc.setY(yPos);
						if (nucColor != null)
							nuc.setColor(nucColor);

						Font font = null;
						if ((fontID >= 0) && (fontSize > 0))
						{
							font = StringUtil.ssFontToFont(fontID, fontSize);
						}
						else if (fontID >= 0)
						{
							font = StringUtil.ssFontToFont(fontID, nuc.getFont().getSize());
						}
						/* Currently can only change fontID by itself
						else if (fontSize > 0)
						{
							font = ssFontToFont(fontID, fontSize);
						}
						*/
						if (font != null)
						{
							nuc.setFont(font);
						}

						if (isSchematicSet)
							nuc.setIsSchematic(isSchematic);
						if (isNucPathSet)
							nuc.setIsNucPath(isNucPath);
						if (isHiddenSet)
							nuc.setIsHidden(isHidden);
						if (groupNameSet)
							nuc.setGroupName(groupName);

						if (schematicColor != null)
							nuc.setSchematicColor(schematicColor);
						if (schematicLineWidth > -Double.MAX_VALUE)
							nuc.setSchematicLineWidth(schematicLineWidth);
						if (schematicBPLineWidth > -Double.MAX_VALUE)
							nuc.setSchematicBPLineWidth(schematicBPLineWidth);
						if (schemBPGap > -Double.MAX_VALUE)
							nuc.setBPSchemGap(schemBPGap);
						if (schemFPGap > -Double.MAX_VALUE)
							nuc.setFPSchemGap(schemFPGap);
						if (schemTPGap > -Double.MAX_VALUE)
							nuc.setTPSchemGap(schemTPGap);

						if (nucPathColor != null)
							nuc.setNucPathColor(nucPathColor);
						if (nucPathLineWidth > -Double.MAX_VALUE)
							nuc.setNucPathLineWidth(nucPathLineWidth);
					}
				}
			}
			else
			{
				val = atts.getValue("RefID");
				if (val != null)
				{
					// refer to nuc only for subsequent parsing
					int refID = Integer.valueOf(val).intValue();
					if (this.getComplexSSDataCollection().getCurrentSSData().getItemAt(refID) == null)
						throw new SAXException(
							"\nError in ComplexXMLParser.startElm().Nuc" +
							" referring to null nuc\n");
					// set current for subsequent parsing like labels
					this.getComplexSSDataCollection().getCurrentSSData().setCurrentItem(refID);
					nuc = (Nuc2D)this.getComplexSSDataCollection().getCurrentSSData().getCurrentItem();
				}
				else
				{
					nuc = new Nuc2D();
					val = atts.getValue("NucID");
					if (val != null)
						nuc.setID(Integer.valueOf(val).intValue());
					this.getComplexSSDataCollection().getCurrentSSData().addItem(nuc);
				}

				if (nucChar != '\0')
					nuc.setNucChar(nucChar);
				if (xPos > -Double.MAX_VALUE)
					nuc.setX(xPos);
				if (yPos > -Double.MAX_VALUE)
					nuc.setY(yPos);
				if (nucColor != null)
					nuc.setColor(nucColor);
			}
		}
		catch (Exception e)
		{
			throw new SAXException(
				"Error in ComplexXMLParser.startElement()", e);
		}
	}
	else if (rawName.equals("NucChars"))
	{
	}
	else if (rawName.equals("NucSegment"))
	{
		val = atts.getValue("StartNucID");
		if (val == null)
			startNucID = 1;
		else
			startNucID = Integer.valueOf(val).intValue();
	}
	else if (rawName.equals("NucListData"))
	{
		charactersBuf = new StringBuffer();

		val = atts.getValue("StartNucID");
		listDataType = atts.getValue("DataType");
		if (val == null)
			startNucID = 1;
		else
			startNucID = Integer.valueOf(val).intValue();
	}
	else if (rawName.equals("LabelList"))
	{
		charactersBuf = new StringBuffer();
	}
	else if (rawName.equals("NucSymbol"))
	{
		charactersBuf = new StringBuffer();
	}
	else if (rawName.equals("Label"))
	{
		val = atts.getValue("XPos");
		labelXPos = 0.0;
		if (val != null)
			labelXPos = Double.valueOf(val).doubleValue();
		labelYPos = 0.0;
		val = atts.getValue("YPos");
		if (val != null)
			labelYPos = Double.valueOf(val).doubleValue();
		val = atts.getValue("Color");
		if (val != null)
			labelColor = new Color(Integer.valueOf(val, 16).intValue());
	}
	else if (rawName.equals("StringLabel"))
	{
		String fontNameStr = atts.getValue("FontName");
		if (fontNameStr == null)
			fontNameStr = "Helvetica";
		String fontTypeStr = atts.getValue("FontType");
		int fontType = Font.PLAIN;
		if (fontTypeStr == null)
			fontType = Font.PLAIN;
		else if (fontTypeStr.equals("PLAIN"))
			fontType = Font.PLAIN;
		else if (fontTypeStr.equals("BOLD"))
			fontType = Font.BOLD;
		else if (fontTypeStr.equals("OBLIQUE") || fontTypeStr.equals("ITALIC"))
			fontType = Font.ITALIC;
		val = atts.getValue("FontSize");
		int fontSize = 8;
		if (val == null)
			fontSize = 8;
		else
			fontSize = Integer.valueOf(val).intValue();
		Font font = new Font(fontNameStr, fontType, fontSize);

		DrawStringObject stringLabel = new DrawStringObject(
			labelXPos, labelYPos, font, labelColor,
			/*
			GraphicsUtil.stringAliasedRenderHints,
			*/
			atts.getValue("Text"));
		this.addLabel(stringLabel);
	}
	else if (rawName.equals("CircleLabel"))
	{
		val = atts.getValue("Arc0");
		double arc0 = 0.0;
		if (val != null)
			arc0 = Double.valueOf(val).doubleValue();
		val = atts.getValue("Arc1");
		double arc1 = 360.0;
		if (val != null)
			arc1 = Double.valueOf(val).doubleValue();
		val = atts.getValue("Radius");
		double radius = 0.0;
		if (val != null)
			radius = Double.valueOf(val).doubleValue();
		val = atts.getValue("LineWidth");
		double lineWidth = 0.0;
		if (val != null)
			lineWidth = Double.valueOf(val).doubleValue();
		val = atts.getValue("IsOpen");
		boolean isOpen = true;
		if ((val != null) && (!val.toLowerCase().equals("true")))
			isOpen = false;

		DrawCircleObject circleLabel = new DrawCircleObject(
			labelXPos, labelYPos, radius, arc0, arc1, labelColor);
		this.addLabel(circleLabel);
	}
	else if (rawName.equals("TriangleLabel"))
	{
	}
	else if (rawName.equals("ParallelogramLabel"))
	{
		/*
		double x = Double.parseDouble(st.nextToken());
		double y = Double.parseDouble(st.nextToken());
		double angle1 = Double.parseDouble(st.nextToken());
		double side1 = Double.parseDouble(st.nextToken());
		double angle2 = Double.parseDouble(st.nextToken());
		double side2 = Double.parseDouble(st.nextToken());
		double lineWidth = Double.parseDouble(st.nextToken());

		// TESTING reseting linewidth from 0.0 to 1.0 just for comparison
		if (lineWidth < GraphicsUtil.MIN_LINEWIDTH)
			lineWidth = GraphicsUtil.MIN_LINEWIDTH;
		int isOpenInt = Integer.parseInt(st.nextToken());
		boolean isOpen = (isOpenInt == 1);
		Color objColor = new Color(Integer.valueOf(st.nextToken(), 16).intValue());
debug("parallelogram: " + x + " " + y + " " + angle1 + " " + side1 + " " + angle2 + " " + side2 + " " + lineWidth + " " + isOpen + " " + objColor);
		return (new DrawParallelogramObject(x, y, angle1, side1,
			angle2, side2, lineWidth, isOpen, objColor));
		*/
		val = atts.getValue("Angle1");
		double angle1 = 0.0;
		if (val != null)
			angle1 = Double.valueOf(val).doubleValue();
		val = atts.getValue("Side1");
		double side1 = 0.0;
		if (val != null)
			side1 = Double.valueOf(val).doubleValue();
		val = atts.getValue("Angle2");
		double angle2 = 0.0;
		if (val != null)
			angle2 = Double.valueOf(val).doubleValue();
		val = atts.getValue("Side2");
		double side2 = 0.0;
		if (val != null)
			side2 = Double.valueOf(val).doubleValue();
		val = atts.getValue("LineWidth");
		double lineWidth = 0.0;
		if (val != null)
			lineWidth = Double.valueOf(val).doubleValue();
		val = atts.getValue("IsOpen");
		boolean isOpen = true;
		if ((val != null) && (!val.toLowerCase().equals("true")))
			isOpen = false;
debug("PARALLELOGRAM.X: " + labelXPos);
debug("PARALLELOGRAM.Y: " + labelYPos);
debug("PARALLELOGRAM.angle1: " + angle1);
debug("PARALLELOGRAM.side1: " + side1);
debug("PARALLELOGRAM.angle2: " + angle2);
debug("PARALLELOGRAM.side2: " + side2);
debug("PARALLELOGRAM.linewidth: " + lineWidth);
debug("PARALLELOGRAM.isOpen: " + isOpen);
debug("PARALLELOGRAM.labelColor: " + labelColor);
		DrawParallelogramObject parallelogramLabel = new DrawParallelogramObject(
	labelXPos, labelYPos, angle1, side1, angle2, side2, lineWidth, isOpen, labelColor);
		this.addLabel(parallelogramLabel);
	}
	else if (rawName.equals("LineLabel"))
	{
		val = atts.getValue("X1");
		double lineX1 = 0.0;
		if (val != null)
			lineX1 = Double.valueOf(val).doubleValue();
		val = atts.getValue("Y1");
		double lineY1 = 0.0;
		if (val != null)
			lineY1 = Double.valueOf(val).doubleValue();
		val = atts.getValue("LineWidth");
		double lineWidth = 0.0;
		if (val != null)
			lineWidth = Double.valueOf(val).doubleValue();

		DrawLineObject lineLabel = new DrawLineObject(
			labelXPos, labelYPos, lineX1, lineY1, lineWidth, labelColor);
		this.addLabel(lineLabel);
	}
	else if (rawName.equals("RNAFile"))
	{
		try
		{
			String fileName = atts.getValue("FileName");
			debugParse("FileName: " + fileName);
			String fileType = atts.getValue("FileType");
			complexDefineStack.push(new Integer(ComplexDefines.InRNASSData));
			this.getComplexSSDataCollection().setModeType(ComplexDefines.InRNASSData);
			if (fileType.equals("XML"))
			{
			}
			else
			{
				// need to determine what we're reading
				((SSData2D)this.getComplexCollection()).setNucsFromFile(fileName, fileType, startNucID);
			}
		}
		catch (Exception e)
		{
			throw new SAXException("Error in ComplexXMLParser.startElement.RNAFile: ", e);
		}
	}
	else if (rawName.equals("BasePairs"))
	{
		complexDefineStack.push(new Integer(ComplexDefines.InRNABasePair));
		this.getComplexSSDataCollection().setModeType(ComplexDefines.InRNABasePair);
		String bpName = atts.getValue("bpName");

		int nucID = 0;
		int bpNucID = 0;
		int length = 0;

		try
		{
			length = Integer.valueOf(atts.getValue("length")).intValue();
		}
		catch (Exception e)
		{
			System.err.println("Error in ComplexXMLParser.BasePairs " +
				"for value of length: " + atts.getValue("length"));
			throw new SAXException("Error in ComplexXMLParser.BasePairs " +
				"for value of length: " + atts.getValue("length"), e);
		}

		if (length < 1)
		{
			System.err.println("Error in ComplexXMLParser.BasePairs " +
				"for value of length: " + atts.getValue("length") +
				" must be greater than 0");
			throw new SAXException("Error in ComplexXMLParser.BasePairs " +
				"for value of length: " + atts.getValue("length") +
				" must be greater than 0");
		}

		try
		{
			nucID = Integer.valueOf(atts.getValue("nucID")).intValue();
		}
		catch (Exception e)
		{
			System.err.println("Error in ComplexXMLParser.BasePairs " +
				"for value of nucID: " + atts.getValue("nucID"));
			throw new SAXException("Error in ComplexXMLParser.BasePairs " +
				"for value of nucID: " + atts.getValue("nucID"), e);
		}

		if ((nucID < 1) ||
			((bpName == null) && (nucID > ((SSData2D)this.getComplexCollection()).getNucCount())))
		{
			System.err.println("Error in ComplexXMLParser.BasePairs: " + nucID +
				" is out of range 1->" + ((SSData2D)this.getComplexCollection()).getNucCount());
			throw new SAXException("Error in ComplexXMLParser.BasePairs: " + nucID +
				" is out of range 1->" + ((SSData2D)this.getComplexCollection()).getNucCount());
		}

		try
		{
			bpNucID = Integer.valueOf(atts.getValue("bpNucID")).intValue();
		}
		catch (Exception e)
		{
			System.err.println("Error in ComplexXMLParser.BasePairs " +
				"for value of bpNucID: " + atts.getValue("bpNucID"));
			throw new SAXException("Error in ComplexXMLParser.BasePairs " +
				"for value of bpNucID: " + atts.getValue("bpNucID"), e);
		}

		if ((bpNucID < 1) ||
			((bpName == null) && (bpNucID > ((SSData2D)this.getComplexCollection()).getNucCount())))
		{
			System.err.println("Error in ComplexXMLParser.BasePairs: " + bpNucID +
				" is out of range 1->" + ((SSData2D)this.getComplexCollection()).getNucCount());
			throw new SAXException("Error in ComplexXMLParser.BasePairs: " + bpNucID +
				" is out of range 1->" + ((SSData2D)this.getComplexCollection()).getNucCount());
		}

		if ((bpNucID < nucID) && (bpName == null))
		{
			int tmp = bpNucID;
			bpNucID = nucID;
			nucID = tmp;
		}

		for (int i = 0;i < length;i++)
		{
			SSData sstr = (SSData)this.getComplexCollection();
			NucNode nuc = null;

			try
			{
				nuc = ((SSData)this.getComplexCollection()).
					getNucAt(nucID + i);
			}
			catch (java.lang.ArrayIndexOutOfBoundsException e)
			{
				throw new SAXException("\nError in " +
					"ComplexXMLParser.BasePairs: " +
					"non-existant nuc: " + (nucID + i) + "\n");
			}

			if (nuc == null)
			{
				throw new SAXException("Error in " +
					"ComplexXMLParser.BasePairs: Trying to BasePair " +
					"non-existant nuc: " + (nucID + i));
			}

			// if bpName is null then means bp'ing in same strand
			nuc.setBasePairID(bpNucID - i);
			nuc.setBasePairSStrName(bpName);
		}
	}
	else if (rawName.equals("BasePair"))
	{
		complexDefineStack.push(new Integer(ComplexDefines.InRNABasePair));
		this.getComplexSSDataCollection().setModeType(ComplexDefines.InRNABasePair);

		int refID = 0;

		try
		{
			refID = Integer.valueOf(atts.getValue("RefID")).intValue();
		}
		catch (Exception e)
		{
			System.err.println("Error in ComplexXMLParser.BasePairs " +
				"for value of refID: " + atts.getValue("RefID"));
			throw new SAXException("Error in ComplexXMLParser.BasePairs " +
				"for value of nucID: " + atts.getValue("RefID"), e);
		}

		SSData2D sstr = (SSData2D)this.getComplexCollection();

		if ((refID < 1) || (refID > sstr.getNucCount()))
		{
			System.err.println("Error in ComplexXMLParser.BasePairs: " + refID +
				" is out of range 1->" + sstr.getNucCount());
			throw new SAXException("Error in ComplexXMLParser.BasePairs: " + refID +
				" is out of range 1->" + sstr.getNucCount());
		}

		NucNode refNuc = sstr.getNucAt(refID);
		// debug("refNuc: " + refNuc);

		/* can't use yet:
		if (!refNuc.isBasePair())
		*/
		if (refNuc.getBasePairID() <= 0)
		{
			System.err.println("Error in ComplexXMLParser.BasePairs: refNuc not a basepair");
			throw new SAXException("Error in ComplexXMLParser.BasePairs: refNuc not a basepair");
		}

		String type = null;

		try
		{
			type = atts.getValue("Type");
		}
		catch (Exception e)
		{
			System.err.println("Error in ComplexXMLParser.BasePairs " +
				"for value of Type: " + atts.getValue("Type"));
			throw new SAXException("Error in ComplexXMLParser.BasePairs " +
				"for value of Type: " + atts.getValue("Type"), e);
		}

		refNuc.setNonDefaultBasePairType(RNABasePair.stringToType(type));
		if (refNuc.getBasePairSStrName() == null)
			sstr.getNucAt(refNuc.getBasePairID()).setNonDefaultBasePairType(RNABasePair.stringToType(type));
		
		if (refNuc.getNonDefaultBasePairType() == ComplexDefines.BP_TYPE_PHOSPHATE)
		{
			double line5PDeltaX = 0.0;
			double line5PDeltaY = 0.0;
			double line3PDeltaX = 0.0;
			double line3PDeltaY = 0.0;
			double labelDeltaX = 0.0;
			double labelDeltaY = 0.0;
			boolean label5PSide = true;

			val = atts.getValue("Line5PDeltaX");
			if (val != null)
				line5PDeltaX  = Double.valueOf(val).doubleValue();
			val = atts.getValue("Line5PDeltaY");
			if (val != null)
				line5PDeltaY  = Double.valueOf(val).doubleValue();
			val = atts.getValue("Line3PDeltaX");
			if (val != null)
				line3PDeltaX  = Double.valueOf(val).doubleValue();
			val = atts.getValue("Line3PDeltaY");
			if (val != null)
				line3PDeltaY  = Double.valueOf(val).doubleValue();
			val = atts.getValue("LabelDeltaX");
			if (val != null)
				labelDeltaX  = Double.valueOf(val).doubleValue();
			val = atts.getValue("LabelDeltaY");
			if (val != null)
				labelDeltaY  = Double.valueOf(val).doubleValue();
			val = atts.getValue("Label5PSide");
			if ((val != null) && (val.toLowerCase().equals("false")))
				label5PSide = false;

			refNuc.setLine5PDeltaX(line5PDeltaX);
			refNuc.setLine5PDeltaY(line5PDeltaY);
			refNuc.setLine3PDeltaX(line3PDeltaX);
			refNuc.setLine3PDeltaY(line3PDeltaY);
			refNuc.setLabelDeltaX(labelDeltaX);
			refNuc.setLabelDeltaY(labelDeltaY);
			refNuc.setLabel5PSide(label5PSide);
		}
	}
	else if (rawName.equals("SceneNodeGeom"))
	{
		double cX = 0.0;
		double cY = 0.0;
		val = atts.getValue("CenterX");
		if (val != null)
			cX  = Double.valueOf(val).doubleValue();
		val = atts.getValue("CenterY");
		if (val != null)
			cY  = Double.valueOf(val).doubleValue();

		switch (((Integer)complexDefineStack.peek()).intValue())
		{
		  case ComplexDefines.InComplexScene :
			if (!complexSceneSceneNodeGeomSet)
			{
				this.getComplexScene().setX(cX);
				this.getComplexScene().setY(cY);

				// currently only scaleing whole scene
				val = atts.getValue("Scale");
				if (val != null)
					this.setFigureScale(Double.valueOf(val).doubleValue());
				complexSceneSceneNodeGeomSet = true;
			}
			break;

		  case ComplexDefines.InComplex :
			this.getComplexSSDataCollection().setX(cX);
			this.getComplexSSDataCollection().setY(cY);
			break;

		  case ComplexDefines.InRNASSData :
			((DrawObject)this.getComplexCollection()).setX(cX);
			((DrawObject)this.getComplexCollection()).setY(cY);
			break;
		}
	}
}

public void
endElement(String namespaceURI, String localName, String rawName)
throws SAXException
{
	if (rawName.equals("ComplexDocument"))
	{
		// complexDefineStack.pop();
	}
	else if (rawName.equals("Complex"))
	{
		complexDefineStack.pop();
	}
	else if (rawName.equals("WithComplex"))
	{
		complexDefineStack.pop();
	}
	else if (rawName.equals("RNAMolecule"))
	{
		complexDefineStack.pop();
		this.setComplexCollection(null);
	}
	else if (rawName.equals("Nuc"))
	{
		complexDefineStack.pop();
	}
	else if (rawName.equals("NucChars"))
	{
		nucChars = new String(charactersString);
	}
	else if (rawName.equals("NucSegment"))
	{
		if (this.getComplexCollection() == null)
			throw new SAXException(
				"Error in ComplexXMLParser.endElement().NucSegment, " +
				"ssData == null");
		debugParse("IN END NUCSEGMENT, startNucID: " + startNucID);
		for (int i = 0;i < nucChars.length();i++)
		{
			char nucChar = nucChars.charAt(i);
			if (NucNode.isValidNucChar(nucChar))
			{
				// need to be able to set a nuc if not one already set.
				// has to be put into vector at proper spot.
				// if already there then update with new attributes.
				// probably want to throw an exception if new attribute
				// changes nucChar.
				try
				{
					this.getComplexCollection().addItem(
						new NucNode(nucChar, startNucID + i));
				}
				catch (Exception e)
				{
					throw new SAXException("error in ComplexXMLParser.endElement.NucSegment.ssData.addItem()", e);
				}
			}
			else
			{
				switch (nucChar)
				{
				  case '\n' :
				  case '\t' :
				  case ' ' :
					break;
				  default :
					throw new SAXException("Invalid Nuc Char: " + nucChar);
				}
			}
		}
	}
	else if (rawName.equals("NucListData"))
	{
		if (this.getComplexCollection() == null)
			throw new SAXException(
				"Error in ComplexXMLParser.endElement().NucSegment, " +
				"ssData == null");
		try
		{
			SSData2D sstr = (SSData2D)this.getComplexCollection();
			sstr.setNucsFromListData(
				charactersBuf.toString(), listDataType, startNucID);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SAXException("Error in ComplexXMLParser.endElement.NucListData: ", e);
		}
		charactersBuf = null;
	}
	else if (rawName.equals("LabelList"))
	{
		try
		{
			StringTokenizer st = new StringTokenizer(charactersBuf.toString(), "\n");
			while (st.hasMoreTokens())
			{
				this.addLabel(this.parseDrawObjectDataLine(
					st.nextToken()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SAXException("Error in ComplexXMLParser.endElement.NucListData: ", e);
		}
		charactersBuf = null;
	}
	else if (rawName.equals("NucSymbol"))
	{
		try
		{
			if ((this.getComplexCollection() instanceof SSData2D) &&
				(((Integer)complexDefineStack.peek()).intValue() ==
					ComplexDefines.InRNASingleNuc))
			{
				Nuc2D nuc = (Nuc2D)this.getComplexSSDataCollection().
					getCurrentSSData().getCurrentItem();
				DrawObject drwObj = parseDrawObjectDataLine(charactersBuf.toString());
				if (drwObj != null)
				{
					drwObj.setX(0.0);
					drwObj.setY(0.0);
					nuc.setNucSymbolDrawObject(drwObj);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new SAXException("Error in ComplexXMLParser.endElement.NucListData: ", e);
		}
		charactersBuf = null;
	}
	else if (rawName.equals("Label"))
	{
	}
	else if (rawName.equals("StringLabel"))
	{
	}
	else if (rawName.equals("CircleLabel"))
	{
	}
	else if (rawName.equals("TriangleLabel"))
	{
	}
	else if (rawName.equals("ParallelogramLabel"))
	{
	}
	else if (rawName.equals("LineLabel"))
	{
	}
	else if (rawName.equals("RNAFile"))
	{
		complexDefineStack.pop();
	}
	else if (rawName.equals("BasePairs"))
	{
		complexDefineStack.pop();
	}
	else if (rawName.equals("BasePair"))
	{
		complexDefineStack.pop();
	}
	charactersString = null;
}

public void
endDocument()
throws SAXException
{
	try
	{
		this.getComplexScene().init();
		this.getComplexScene().resetNucStats();
	}
	catch (Exception e)
	{

	ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(
			new DataOutputStream(excptArray)));
	debug(new String(excptArray.toByteArray()));

		throw new SAXException("Error in ComplexXMLParser.endDocument: ", e);

	}
}	

public String
generateComplexXML_DTD()
{
	return (ComplexDTDDefine.complexDTD);
}

public static void
debugParse(String s)
{
	if (!debugOn)
		return;	
	System.err.println("ComplexXMLParser-> " + s);
}

public static void
debug(String s)
{
	System.err.println("ComplexXMLParser-> " + s);
}

}
