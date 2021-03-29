package ssview;

import java.io.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

import jimage.DrawObject;
import jimage.DrawObjectCollection;
import jimage.DrawObjectLeafNode;
import jimage.DrawStringObject;
import jimage.DrawAdobeObject;
import jimage.DrawCharObject;
import jimage.DrawLineObject;
import jimage.DrawCircleObject;
import jimage.DrawArrowObject;
import jimage.DrawTriangleObject;
import jimage.DrawParallelogramObject;
import util.*;
import util.math.*;

public class
ComplexCollection
extends DrawObjectCollection
{

/*********** DrawObjectCollection Implementation ************/

public void
addItem(Object sceneGraphNode)
throws Exception
{
}

// this is different than one for SSData
// public DrawObjectCollection
public Object
getItemAt(int index)
{
	return (null);
}

// this is different than one for SSData
public void
setItemAt(Object obj, int ordinalPos)
{
	((Vector)this.getCollection()).setElementAt(obj, ordinalPos);
}

public void
delete()
throws Exception
{
	/*
	if (this instanceof NucNode)
	{
		debug("in delete, NucNode: " + ((NucNode)this) + ":" + ((Vector)((DrawObjectCollection)this.getParentCollection()).getCollection()).contains(this));
	}
	*/
	((Vector)((DrawObjectCollection)this.getParentCollection()).getCollection()).remove(this);
}

private Vector labelList = null;

public void
setLabelList(Vector labelList)
{
    this.labelList = labelList;
}

public Vector
getLabelList()
{
    return (this.labelList);
}

private DrawObject label = null;

public void
addLabel(DrawObject label)
{
	if (this.getLabelList() == null)
		this.setLabelList(new Vector());
	label.setParentCollection(this);
	this.getLabelList().add(label);
}

public void
removeLabel(DrawObject label)
{
	if (label == null)
		return;
	if (this.getLabelList() == null)
		return;
	this.getLabelList().remove(label);
}

public void
deleteAllLabels()
throws Exception
{
	if (this.getLabelList() == null)
		return;
	this.getLabelList().removeAllElements();
	this.setLabelList(null);
}

public void
drawLabelList(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getLabelList() == null)
		return;

	// look for all extraneous lines and draw them first, but,
	// look for overlaying lines and draw them last in the extraneous line list
	Vector allElseDrwObjList = null;
	Vector lineList = null;
	Vector overlayLineList = null;
	for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
	{
		DrawObject drwObj = (DrawObject)e.nextElement();
		if (drwObj instanceof DrawLineObject)
		{
			DrawLineObject lineObj = (DrawLineObject)drwObj;
			if (lineObj.getOverlayBorder() <= 0.0)
			{
				if (lineList == null)
					lineList = new Vector();
				lineList.add(lineObj);
			}
			else
			{
				if (overlayLineList == null)
					overlayLineList = new Vector();
				overlayLineList.add(lineObj);
			}
			continue;
		}
		else
		{
			if (allElseDrwObjList == null)
				allElseDrwObjList = new Vector();
			allElseDrwObjList.add(drwObj);
		}
	}
	if (lineList != null)
	{
		for (Enumeration e = lineList.elements();e.hasMoreElements();)
			((DrawLineObject)e.nextElement()).draw(g2, constrainedArea);
	}
	if (overlayLineList != null)
	{
		for (Enumeration e = overlayLineList.elements();e.hasMoreElements();)
			((DrawLineObject)e.nextElement()).draw(g2, constrainedArea);
	}
	if (allElseDrwObjList != null)
	{
		for (Enumeration e = allElseDrwObjList.elements();e.hasMoreElements();)
			((DrawObject)e.nextElement()).draw(g2, constrainedArea);
	}
}

public void
printPSLabelList(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	if (this.getLabelList() == null)
		return;

	// look for all extraneous lines and print them first, but,
	// look for overlaying lines and print them last in the extraneous line list
	Vector allElseDrwObjList = null;
	Vector lineList = null;
	Vector overlayLineList = null;
	for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
	{
		DrawObject drwObj = (DrawObject)e.nextElement();
		if (drwObj instanceof DrawLineObject)
		{
			DrawLineObject lineObj = (DrawLineObject)drwObj;
			if (lineObj.getOverlayBorder() <= 0.0)
			{
				if (lineList == null)
					lineList = new Vector();
				lineList.add(lineObj);
			}
			else
			{
				if (overlayLineList == null)
					overlayLineList = new Vector();
				overlayLineList.add(lineObj);
			}
			continue;
		}
		else
		{
			if (allElseDrwObjList == null)
				allElseDrwObjList = new Vector();
			allElseDrwObjList.add(drwObj);
		}
	}
	if (lineList != null)
	{
		for (Enumeration e = lineList.elements();e.hasMoreElements();)
			((DrawLineObject)e.nextElement()).printPS(g2, psUtil);
	}
	if (overlayLineList != null)
	{
		for (Enumeration e = overlayLineList.elements();e.hasMoreElements();)
			((DrawLineObject)e.nextElement()).printPS(g2, psUtil);
	}
	if (allElseDrwObjList != null)
	{
		for (Enumeration e = allElseDrwObjList.elements();e.hasMoreElements();)
			((DrawObject)e.nextElement()).printPS(g2, psUtil);
	}
}

public void
setAllNucsIsSchematic(boolean isSchematic, double lineWidth, double bpLineWidth, Color color)
throws Exception
{
	if (this instanceof Nuc2D)
	{
		Nuc2D nuc = (Nuc2D)this;
		if (isSchematic)
		{
			nuc.setSchematicLineWidth(lineWidth);
			nuc.setSchematicBPLineWidth(bpLineWidth);
			nuc.setSchematicColor(color);
		}
		nuc.setIsSchematic(isSchematic);
		return;
	}

	if (this instanceof NucCollection2D)
	{
		Vector delineators = ((NucCollection2D)this).getItemListDelineators();
		if (delineators == null)
			return;
		for (int i = 0;i < delineators.size();i+=2)
		{
			NucNode refNuc = (NucNode)delineators.elementAt(i);
			NucNode endNuc = (NucNode)delineators.elementAt(i+1);
			while (true)
			{
				refNuc.setAllNucsIsSchematic(isSchematic, lineWidth, bpLineWidth, color);
				if (refNuc.equals(endNuc))
					break;
				refNuc = refNuc.nextNuc();
			}
		}
		return;
	}
		
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexCollection)
			((ComplexCollection)obj).setAllNucsIsSchematic(isSchematic, lineWidth, bpLineWidth, color);
	}
}

public void
deleteAllNucLabels()
throws Exception
{
	if (this instanceof NucNode)
	{
		this.deleteAllLabels();
		return;
	}

	if (this instanceof NucCollection2D)
	{
		Vector delineators = ((NucCollection2D)this).getItemListDelineators();
		for (int i = 0;i < delineators.size();i+=2)
		{
			NucNode refNuc = (NucNode)delineators.elementAt(i);
			NucNode endNuc = (NucNode)delineators.elementAt(i+1);
			while (true)
			{
				refNuc.deleteAllNucLabels();
				if (refNuc.equals(endNuc))
					break;
				refNuc = refNuc.nextNuc();
			}
		}
		return;
	}
		
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexCollection)
			((ComplexCollection)obj).deleteAllNucLabels();
	}
}

public void
addNucLabels(Font font, double lineLength, int numberModulus, int lineModulus, int startID)
throws Exception
{
	if (this instanceof Nuc2D)
	{
		Nuc2D nuc = (Nuc2D)this;
		nuc.setNewLabel(font, lineLength, startID);
		return;
	}

	if (this instanceof NucCollection2D)
	{
		Vector nucList = ((NucCollection2D)this).getItemListDelineators();
		for (int i = 0;i < nucList.size();i+=2)
		{
			NucNode refNuc = (NucNode)nucList.elementAt(i);
			NucNode endNuc = (NucNode)nucList.elementAt(i+1);
			while (true)
			{
				int adjustedNucNumber = refNuc.getID() - startID + 1;
				if (adjustedNucNumber > 0)
				{
					if (adjustedNucNumber % numberModulus == 0)
						((Nuc2D)refNuc).setNewLabel(font, lineLength, adjustedNucNumber,
							false);
					else if (adjustedNucNumber % lineModulus == 0)
						((Nuc2D)refNuc).setNewLabel(font, lineLength, adjustedNucNumber,
							true);
				}
				if (refNuc.equals(endNuc))
					break;
				refNuc = refNuc.nextNuc();
			}
		}
		return;
	}
		
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexCollection)
			((ComplexCollection)obj).addNucLabels(font, lineLength, numberModulus, lineModulus, startID);
	}
}

public boolean
containsNonSelfRefBasePairs()
throws Exception
{
	if (this instanceof NucNode)
	{
		// could contain a reference to a bp in another rna strand
		return (((NucNode)this).isNonSelfRefBasePair());
	}

	if (this instanceof NucCollection2D)
	{
		Vector delineators = ((NucCollection2D)this).getItemListDelineators();
		for (int i = 0;i < delineators.size();i+=2)
		{
			NucNode refNuc = (NucNode)delineators.elementAt(i);
			NucNode endNuc = (NucNode)delineators.elementAt(i+1);
			while (true)
			{
				if (refNuc.isNonSelfRefBasePair())
					return (true);
				if (refNuc.equals(endNuc))
					break;
				refNuc = refNuc.nextNuc();
			}
		}
		return (false);
	}
		
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexCollection))
			continue;
		if (((ComplexCollection)obj).containsNonSelfRefBasePairs())
			return (true);
	}

	return (false);
}

public void
reformat()
throws Exception
{
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexCollection)
			((ComplexCollection)obj).reformat();
	}
}

public void
reassociateLabel(DrawObject label, ComplexCollection fromCollection)
throws Exception
{
	// delete label from current container
	fromCollection.getLabelList().remove(label);

	// add label to parents parent container
	this.addLabel(label);

	// reset labels x,y
	label.shiftXY(-((DrawObject)fromCollection).getX(),
		-((DrawObject)fromCollection).getY());
}

///////////////////////////////////////////////////////////////////////////
public static void
printDrawObjectLabel(DrawObject drawObject, PrintWriter out)
throws Exception
{
	// currently using non-pickable drawObjects as helper graphics
	if (!drawObject.getIsPickable())
		return;

	out.println(printDrawObjectLabel(drawObject));
}



public static String
printDrawObjectLabel(DrawObject drawObject)
throws Exception
{
	if (drawObject instanceof DrawStringObject)
	{
		DrawStringObject drawStringObject =
			(DrawStringObject)drawObject;
		Font font = drawStringObject.getFont();
		int fontID = StringUtil.fontToFontID(font);
		//     x       y    ang  size font color
		// s 411.00 -141.00 0.0  12    2     0     "ACCEPTOR"
		return ("s " + StringUtil.roundStrVal(drawStringObject.getX(), 2) + " " +
			StringUtil.roundStrVal(drawStringObject.getY(), 2) + " 0.0 " +
			font.getSize() + " " + fontID + " " +
			Integer.toHexString(drawStringObject.getColor().
				getRGB() & 0x00ffffff) + " \"" +
			drawStringObject.getDrawString() + "\"");
	}
	else if (drawObject instanceof DrawAdobeObject)
	{
		DrawAdobeObject drawAdobeObject = (DrawAdobeObject)drawObject;
		Font font = drawAdobeObject.getFont();
		int fontID = StringUtil.fontToFontID(font);
		StringBuffer strBuf = new StringBuffer();
		//     x       y    ang  size font color
		// s 411.00 -141.00 0.0  12    2     0     "ACCEPTOR"
		strBuf.append("S " + StringUtil.roundStrVal(drawAdobeObject.getX(), 2) + " " +
			StringUtil.roundStrVal(drawAdobeObject.getY(), 2) + " 0.0 " +
			font.getSize() + " " + fontID + " " +
			Integer.toHexString(drawAdobeObject.getColor().
				getRGB() & 0x00ffffff) + " \"");
		switch (drawAdobeObject.getDrawChar())
		{
		  case '\u03a8' :
			strBuf.append("u03a8");
			break;
		  case '\u03c8' :
			strBuf.append("u03c8");
			break;
		}
		strBuf.append("\"");
		return (strBuf.toString());
	}
	else if (drawObject instanceof DrawCharObject)
	{
		DrawCharObject drawCharObject =
			(DrawCharObject)drawObject;
		Font font = drawCharObject.getFont();
		int fontID = StringUtil.fontToFontID(font);
		//     x       y    ang  size font color
		// c 411.00 -141.00 0.0  12    2     0     "ACCEPTOR"
		return ("c " + StringUtil.roundStrVal(drawCharObject.getX(), 2) + " " +
			StringUtil.roundStrVal(drawCharObject.getY(), 2) + " 0.0 " +
			font.getSize() + " " + fontID + " " +
			Integer.toHexString(drawCharObject.getColor().
				getRGB() & 0x00ffffff) + " \"" +
			drawCharObject.getDrawChar() + "\"");
	}
	else if (drawObject instanceof DrawLineObject)
	{
		DrawLineObject lineObj =
			(DrawLineObject)drawObject;
		// l 303.00 -309.00 314.00 -327.00 0.00 0 0 0 0 0 0
		return ("l " +
			StringUtil.roundStrVal(lineObj.getBLine2D().getP1().getX(), 2) + " " +
			StringUtil.roundStrVal(lineObj.getBLine2D().getP1().getY(), 2) + " " +
			StringUtil.roundStrVal(lineObj.getBLine2D().getP2().getX(), 2) + " " +
			StringUtil.roundStrVal(lineObj.getBLine2D().getP2().getY(), 2) + " " +
			StringUtil.roundStrVal(lineObj.getLineStroke().getLineWidth(), 2) + " " +
			Integer.toHexString(lineObj.getColor().getRGB() & 0x00ffffff) + " " +
			StringUtil.roundStrVal(lineObj.getOverlayBorder(), 2) + " 0 0 0 0");
	}
	else if (drawObject instanceof DrawArrowObject)
	{
		DrawArrowObject drawArrowObject =
			(DrawArrowObject)drawObject;

		return ("r " +
			StringUtil.roundStrVal(drawArrowObject.getX(), 2) + " " +
			StringUtil.roundStrVal(drawArrowObject.getY(), 2) + " " +
			StringUtil.roundStrVal(drawArrowObject.getHeight(), 2) + " " +
			StringUtil.roundStrVal(drawArrowObject.getBaseWidth(), 2) + " " +
			StringUtil.roundStrVal(drawArrowObject.getTailLength(), 2) + " " +
			StringUtil.roundStrVal(drawArrowObject.getTailWidth(), 2) + " " +
			StringUtil.roundStrVal(drawArrowObject.getBaseIndent(), 2) + " " +
			StringUtil.roundStrVal(drawArrowObject.getAngle(), 2) + " " +
			StringUtil.roundStrVal(drawArrowObject.getLineWidth(), 2) + " " +
			(drawArrowObject.getIsOpen() ? "1 " : "0 ") + " " +
			Integer.toHexString(drawArrowObject.getColor().getRGB() & 0x00ffffff));
	}
	else if (drawObject instanceof DrawTriangleObject)
	{
		DrawTriangleObject drawTriangleObject =
			(DrawTriangleObject)drawObject;
		// t 392.10 43.21 0.00 6.00 -5.00 -4.00 5.00 -4.00 0.00 1.00 0 0
		return ("t " +
			StringUtil.roundStrVal(drawTriangleObject.getX(), 2) + " " +
			StringUtil.roundStrVal(drawTriangleObject.getY(), 2) + " " +
			StringUtil.roundStrVal(drawTriangleObject.getTopPt().getX(), 2) + " " +
			StringUtil.roundStrVal(drawTriangleObject.getTopPt().getY(), 2) + " " +
			StringUtil.roundStrVal(drawTriangleObject.getLeftPt().getX(), 2) + " " +
			StringUtil.roundStrVal(drawTriangleObject.getLeftPt().getY(), 2) + " " +
			StringUtil.roundStrVal(drawTriangleObject.getRightPt().getX(), 2) + " " +
			StringUtil.roundStrVal(drawTriangleObject.getRightPt().getY(), 2) + " " +
			StringUtil.roundStrVal(drawTriangleObject.getAngle(), 2) + " " +
			StringUtil.roundStrVal(drawTriangleObject.getLineWidth(), 2) + " " +
			(drawTriangleObject.getIsOpen() ? "1 " : "0 ") + " " +
			Integer.toHexString(drawTriangleObject.getColor().getRGB() & 0x00ffffff));
	}
// NEED to find out why z is in there. leave out for now. also leave nucid
// out. it goes some where else.
// p 382.0 -223.0 0.0 25.0 23.0 115.0 10.0 0 0.0 1 0
// p 343.0 -222.0 0.0 -23068.0 23.0 62.0 10.0 0 0.0 1 0
//    x      y     z    ang1   side1 ang2 side2 nucid linewidth isopen color
	else if (drawObject instanceof DrawParallelogramObject)
	{
		DrawParallelogramObject drawParallelogramObject =
			(DrawParallelogramObject)drawObject;
		return ("p " +
			StringUtil.roundStrVal(drawParallelogramObject.getX(), 2) + " " +
			StringUtil.roundStrVal(drawParallelogramObject.getY(), 2) + " " +
			StringUtil.roundStrVal(drawParallelogramObject.getAngle1(), 2) + " " +
			StringUtil.roundStrVal(drawParallelogramObject.getSide1(), 2) + " " +
			StringUtil.roundStrVal(drawParallelogramObject.getAngle2(), 2) + " " +
			StringUtil.roundStrVal(drawParallelogramObject.getSide2(), 2) + " " +
			StringUtil.roundStrVal(drawParallelogramObject.getLineWidth(), 2) + " " +
			(drawParallelogramObject.getIsOpen() ? "1 " : "0 ") + " " +
			Integer.toHexString(drawParallelogramObject.getColor().getRGB() & 0x00ffffff));
	}
	else if (drawObject instanceof DrawCircleObject)
	{
		DrawCircleObject drawCircleObject = (DrawCircleObject)drawObject;
		return ("a " +
			StringUtil.roundStrVal(drawCircleObject.getX(), 2) + " " +
			StringUtil.roundStrVal(drawCircleObject.getY(), 2) + " " +
			StringUtil.roundStrVal(drawCircleObject.getRadius(), 2) + " " +
			StringUtil.roundStrVal(drawCircleObject.getStartAngle(), 2) + " " +
			StringUtil.roundStrVal(drawCircleObject.getAngleExtent(), 2) + " " +
			StringUtil.roundStrVal(drawCircleObject.getLineWidth(), 2) + " " +
			(drawCircleObject.getIsOpen() ? "1 " : "0 ") + " " +
			Integer.toHexString(drawCircleObject.getColor().getRGB() & 0x00ffffff));
	}
	return (null);
}

////calls other printdrawobjectcsv and prints info to outfile
//public static void
//printDrawObjectLabelCSV(DrawLineObject drawObject, PrintWriter out, SSData sstr)
//throws Exception
//{
//	// currently using non-pickable drawObjects as helper graphics
//	if (!drawObject.getIsPickable())
//		return;
//
//	out.print(printDrawObjectLabelCSV(drawObject, sstr));
//}

//prints label list information in csv format, currently only printing out 
//line and string labels
public static String
printDrawObjectLabelCSV(DrawLineObject lineObj, DrawStringObject drawStringObject, SSData sstr)
throws Exception {
	// l 303.00 -309.00 314.00 -327.00 0.00 0 0 0 0 0 0
	
	BLine2D
		bLine2D = lineObj.getBLine2D();
	Point2D
		p1 = bLine2D.getP1(),
		p2 = bLine2D.getP2();
	int
		nucID = Integer.parseInt(drawStringObject.getDrawString());
	Nuc2D
		nuc = sstr.getNuc2DAt(nucID);
	double
		nucX = nuc.getX(),
		nucY = nuc.getY();
	String lineStr =
		StringUtil.roundStrVal(nucX + p1.getX(), 2) + "," +
		StringUtil.roundStrVal(nucY + p1.getY(), 2) + "," +
		StringUtil.roundStrVal(nucX + p2.getX(), 2) + "," +
		StringUtil.roundStrVal(nucY + p2.getY(), 2) + "," +
		StringUtil.roundStrVal(lineObj.getLineStroke().getLineWidth(), 2) + "," +
		Integer.toHexString(lineObj.getColor().getRGB() & 0x00ffffff) + ",";
	Font font = drawStringObject.getFont();
	
	//     x       y    ang  size font color
	// s 411.00 -141.00 0.0  12    2     0     "ACCEPTOR"
	return lineStr + (StringUtil.roundStrVal(nucX + drawStringObject.getX(), 2) + "," +
		StringUtil.roundStrVal(nucY + drawStringObject.getY(), 2) + "," +
		drawStringObject.getDrawString()+ "," + font.getSize() + "," +
		Integer.toHexString(drawStringObject.getColor().
		getRGB() & 0x00ffffff));
}


public void
printLabelList(PrintWriter out)
throws Exception
{
	if (this.getLabelList() == null)
		return;

	out.println("<LabelList>");
	for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		this.printDrawObjectLabel((DrawObject)e.nextElement(), out);
	out.println("</LabelList>");
}

//sets up call to printDrawObjectLabelCSV to get info on each label
public void
printLabelListCSV(PrintWriter out, SSData sstr)
throws Exception
{
	if (this.getLabelList() == null) {
		return;
	}
	for (Enumeration e = this.getLabelList().elements(); e.hasMoreElements();) {
		// currently using non-pickable drawObjects as helper graphics
		DrawLineObject
			drawLineObject = (DrawLineObject)e.nextElement();
		DrawStringObject
			drawStringObject = (DrawStringObject)e.nextElement();
		if (drawLineObject.getIsPickable() && drawStringObject.getIsPickable()) {			
			out.print(printDrawObjectLabelCSV(drawLineObject, drawStringObject, sstr));
		}
	}
}

//calls appropriate functions to adjust label list coordinates
//int type = scale(0) or shift(1), reverseY(2)
//if scale, then scaling factor must be passed in
//double xChange
//double yChange
//doesnt matter what the x and y changes are if scale is set
//calls each individual label
public void
callAdjustLabelListCSV(int type, double xChange, double yChange, double scale) 
throws Exception
{
	if (this.getLabelList() == null) 
		return;
	if (type == 0) {
		//scale
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
			this.scaleLabelListCSV((DrawObject)e.nextElement(), scale);
	} else if (type == 1) {
		//shift
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
			this.shiftLabelListCSV((DrawObject)e.nextElement(), xChange, yChange);
	} else if (type == 2) {
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
			this.yLabelListCSV((DrawObject)e.nextElement());
	} else {
		System.out.println("Not a valid label adjustment type\n");
	}
}

public void yLabelListCSV(DrawObject drawObject) 
throws Exception{
	double maxY = 756; //boundary
	if (drawObject instanceof DrawStringObject) {
		DrawStringObject drawStringObject = (DrawStringObject)drawObject;
		
		double oldY = drawStringObject.getY();
		
		double newY = 0 - oldY;
		drawStringObject.setY(newY);
	}
	else if (drawObject instanceof DrawLineObject) {
		DrawLineObject lineObj =
				(DrawLineObject)drawObject;
			
		double oldX1 = lineObj.getBLine2D().getP1().getX();
		double oldY1 = lineObj.getBLine2D().getP1().getY();
		double oldX2 = lineObj.getBLine2D().getP2().getX();
		double oldY2 = lineObj.getBLine2D().getP2().getY();
			
		double newY1 = 0 - oldY1;
		double newY2 = 0 - oldY2;
		
		lineObj.reset(oldX1, newY1, oldX2, newY2);
			
	}
}

//scales coordinates of drawObject passed in
//used for scaling labels
public void scaleLabelListCSV(DrawObject drawObject, double scale)
throws Exception
{
	if (drawObject instanceof DrawStringObject)
		{
			DrawStringObject drawStringObject =
			(DrawStringObject)drawObject;
			Font font = drawStringObject.getFont();
			int fontID = StringUtil.fontToFontID(font);
			
			double oldX = drawStringObject.getX();
			double oldY = drawStringObject.getY();
			
			double newX = oldX * scale;
			double newY = oldY * scale;
			double newSize = font.getSize() * scale;
			drawStringObject.setX(newX);
			drawStringObject.setY(newY);
			
			//or font.PLAIN instead of font.getStyle() if error
			Font newfont = new Font(font.getName(), font.getStyle(), (int)newSize);
			drawStringObject.setFont(newfont);
			
			System.out.println("Scaling String by: " + scale + " old: " + oldX + ", " + oldY + " -> " + drawStringObject.getX() + ", " + drawStringObject.getY() + "\n");
			
			//     x       y    ang  size font color
			// s 411.00 -141.00 0.0  12    2     0     "ACCEPTOR"
			/*	return (StringUtil.roundStrVal(drawStringObject.getX(), 2) + "," +
					StringUtil.roundStrVal(drawStringObject.getY(), 2) + "," +
					drawStringObject.getDrawString()+ "," + font.getSize() + "," +
						Integer.toHexString(drawStringObject.getColor().
							getRGB() & 0x00ffffff));
			*/
			}
			
			else if (drawObject instanceof DrawLineObject)
			{
				DrawLineObject lineObj =
					(DrawLineObject)drawObject;
				
				double oldX1 = lineObj.getBLine2D().getP1().getX();
				double oldY1 = lineObj.getBLine2D().getP1().getY();
				double oldX2 = lineObj.getBLine2D().getP2().getX();
				double oldY2 = lineObj.getBLine2D().getP2().getY();
				
				double newX1 = oldX1 * scale;
				double newX2 = oldX2 * scale;
				double newY1 = oldY1 * scale;
				double newY2 = oldY2 * scale;
				
				lineObj.reset(newX1, newY1, newX2, newY2);
				
				//test
				
				System.out.println("Scaling Label old1 by " + scale + ": " + oldX1 + ", " + oldY1 + " -> " + lineObj.getBLine2D().getP1().getX() + ", " + lineObj.getBLine2D().getP1().getY());
				System.out.println("Scaling Label old2 by " + scale + ": " + oldX2 + ", " + oldY2 + " -> " + lineObj.getBLine2D().getP2().getX() + ", " + lineObj.getBLine2D().getP2().getY() + "\n");
				// l 303.00 -309.00 314.00 -327.00 0.00 0 0 0 0 0 0
			/*	return (StringUtil.roundStrVal(lineObj.getBLine2D().getP1().getX(), 2) + "," +
					StringUtil.roundStrVal(lineObj.getBLine2D().getP1().getY(), 2) + "," +
					StringUtil.roundStrVal(lineObj.getBLine2D().getP2().getX(), 2) + "," +
					StringUtil.roundStrVal(lineObj.getBLine2D().getP2().getY(), 2) + "," +
					StringUtil.roundStrVal(lineObj.getLineStroke().getLineWidth(), 2) + "," +
					Integer.toHexString(lineObj.getColor().getRGB() & 0x00ffffff) + ",");
			*/
			}
			
		}

//shifts coordinates of a label
public void
shiftLabelListCSV(DrawObject drawObject, double xChange, double yChange)
throws Exception {
	if (drawObject instanceof DrawStringObject) {
		DrawStringObject drawStringObject =
				(DrawStringObject)drawObject;
		double oldX = drawStringObject.getX();
		double oldY = drawStringObject.getY();
		double newX = oldX + xChange;
		double newY = oldY + yChange;
		drawStringObject.setX(newX);
		drawStringObject.setY(newY);
		//test
		System.out.println("Shifting X by: " + xChange + " OldX: " + oldX + " NewX: " + drawStringObject.getX());
		System.out.println("Shifting Y by: " + yChange + " OldY: " + oldY + " NewY: " + drawStringObject.getY() + "\n");
	}
	else if (drawObject instanceof DrawLineObject) {
		DrawLineObject lineObj =
				(DrawLineObject)drawObject;
		double oldX1 = lineObj.getBLine2D().getP1().getX();
		double oldY1 = lineObj.getBLine2D().getP1().getY();
		double oldX2 = lineObj.getBLine2D().getP2().getX();
		double oldY2 = lineObj.getBLine2D().getP2().getY();
		
		double newX1 = oldX1 + xChange;
		double newY1 = oldY1 + yChange;
		double newX2 = oldX2 + xChange;
		double newY2 = oldY2 + yChange;
		
		lineObj.reset(newX1, newY1, newX2, newY2);
		//test
		System.out.println("Shifting Label by (" + xChange + ", " + yChange + ") "  + "\nOldX1: " + oldX1 + " NewX1: " + lineObj.getBLine2D().getP1().getX() + "\nOldY1: " + oldY1 + " NewY1: " + lineObj.getBLine2D().getP1().getY());
		System.out.println("OldX2: " + oldX2 + " NewX2: " + lineObj.getBLine2D().getP2().getX() + "\nOldY2: " + oldY2 + " NewY2: " + lineObj.getBLine2D().getP2().getY() + "\n");
	}
	
}


/**
 * 
 * Adding printComplexCSV under each printComplexXML
 * 
 */

public void
printComplexXML(PrintWriter outFile)
throws Exception
{
}

public void
printComplexCSV(PrintWriter outFile)
throws Exception
{
}

public void printComplexSVG(PrintWriter outFile) throws Exception { }

public void printComplexBPSeq(PrintWriter outFile) throws Exception { }

public void printComplexTR(PrintWriter outFile) throws Exception { }

public void printComplexCSV(PrintWriter outFile, LinkedList<Nuc2D> nucleotides, double minX, double minY, double maxX, double maxY) throws Exception { }

public void printComplexTR(PrintWriter outFile, LinkedList<Nuc2D> nucleotides, double minX, double minY, double maxX, double maxY) throws Exception { }

public void printComplexSVG(PrintWriter outFile, LinkedList<Nuc2D> nucleotides, double minX, double minY, double maxX, double maxY) throws Exception { }

public void printComplexBPSeq(PrintWriter outFile, LinkedList<Nuc2D> nucleotides) throws Exception {}

public void
printComplexXML(File outFile)
throws Exception
{
	// NEED to catch an exception and not overwrite a file if
	// there's a problem
	// or maybe make a backup file if it is same name

	if (outFile.exists() && (!outFile.canWrite()))
	{
		debug("Can't write " + outFile.getName() + " ; Need to set writeable");
		return;
	}

	if (outFile.exists())
		FileUtil.copyFile(outFile, new File(outFile.getName() + ".bak"));

	StringWriter strWriter = null;
	PrintWriter printWriter = null;

	// first get xrna input file as string to bypass any exceptions
	try
	{
		strWriter = new StringWriter();
		printWriter = new PrintWriter(strWriter);
		this.printComplexXML(printWriter);
		
	}
	catch (Exception e)
	{
		// showUserMsg("Error, file not written:\n" + e.toString());
		throw e;
	}

	// made it past any errors in creating xrna input file

	try
	{
		outFile.createNewFile();
		FileWriter genFileWriter = new FileWriter(outFile);
		PrintWriter pWriter = new PrintWriter(
			new BufferedWriter(genFileWriter), true);
		pWriter.print(strWriter.toString());
		
		//System.out.println("PrintXML from ComplexCollection START");
		//System.out.println(strWriter.toString());
		//System.out.println("PrintXML from ComplexCollection END");
		
		pWriter.flush();
		pWriter.close();
	}
	catch (Exception e)
	{
		throw e;
	}
}

public void printComplexTR(File outFile) throws Exception {
	// NEED to catch an exception and not overwrite a file if
	// there's a problem
	// or maybe make a backup file if it is same name
	if (outFile.exists() && (!outFile.canWrite())) {
		debug("Can't write " + outFile.getName() + " ; Need to set writeable");
		return;
	}

	if (outFile.exists()) { FileUtil.copyFile(outFile, new File(outFile.getName() + ".bak")); }

	StringWriter strWriter = new StringWriter();

	// first get xrna input file as string to bypass any exceptions
	this.printComplexTR(new PrintWriter(strWriter));

	// made it past any errors in creating xrna input file
	outFile.createNewFile();
	FileWriter genFileWriter = new FileWriter(outFile);
	PrintWriter pWriter = new PrintWriter(new BufferedWriter(genFileWriter), true);
	pWriter.print(strWriter.toString());
	
	pWriter.flush();
	pWriter.close();
}

public void printComplexSVG(File outFile) throws Exception {
	// NEED to catch an exception and not overwrite a file if
	// there's a problem
	// or maybe make a backup file if it is same name
	if (outFile.exists() && (!outFile.canWrite())) {
		debug("Can't write " + outFile.getName() + " ; Need to set writeable");
		return;
	}

	if (outFile.exists()) { FileUtil.copyFile(outFile, new File(outFile.getName() + ".bak")); }

	StringWriter strWriter = new StringWriter();

	// first get xrna input file as string to bypass any exceptions
	this.printComplexSVG(new PrintWriter(strWriter));

	// made it past any errors in creating xrna input file
	outFile.createNewFile();
	FileWriter genFileWriter = new FileWriter(outFile);
	PrintWriter pWriter = new PrintWriter(new BufferedWriter(genFileWriter), true);
	pWriter.print(strWriter.toString());
	
	pWriter.flush();
	pWriter.close();
}

public void printComplexBPSeq(File outFile) throws Exception {
	// NEED to catch an exception and not overwrite a file if
	// there's a problem
	// or maybe make a backup file if it is same name
	if (outFile.exists() && (!outFile.canWrite())) {
		debug("Can't write " + outFile.getName() + " ; Need to set writeable");
		return;
	}

	if (outFile.exists()) { FileUtil.copyFile(outFile, new File(outFile.getName() + ".bak")); }

	StringWriter strWriter = new StringWriter();

	// first get xrna input file as string to bypass any exceptions
	this.printComplexBPSeq(new PrintWriter(strWriter));

	// made it past any errors in creating xrna input file
	outFile.createNewFile();
	FileWriter genFileWriter = new FileWriter(outFile);
	PrintWriter pWriter = new PrintWriter(new BufferedWriter(genFileWriter), true);
	pWriter.print(strWriter.toString());
	
	pWriter.flush();
	pWriter.close();
}

public void
printComplexCSV(File outFile)
throws Exception
{
	// NEED to catch an exception and not overwrite a file if
	// there's a problem
	// or maybe make a backup file if it is same name

	if (outFile.exists() && (!outFile.canWrite()))
	{
		debug("Can't write " + outFile.getName() + " ; Need to set writeable");
		return;
	}

	if (outFile.exists())
		FileUtil.copyFile(outFile, new File(outFile.getName() + ".bak"));

	StringWriter strWriter = null;
	PrintWriter printWriter = null;

	// first get xrna input file as string to bypass any exceptions
	try
	{
		strWriter = new StringWriter();
		printWriter = new PrintWriter(strWriter);
		this.printComplexCSV(printWriter);
	
	}
	catch (Exception e)
	{
		// showUserMsg("Error, file not written:\n" + e.toString());
		throw e;
	}

	// made it past any errors in creating xrna input file

	try
	{
		outFile.createNewFile();
		FileWriter genFileWriter = new FileWriter(outFile);
		PrintWriter pWriter = new PrintWriter(
			new BufferedWriter(genFileWriter), true);
		pWriter.print(strWriter.toString());
		
		pWriter.flush();
		pWriter.close();
	}
	catch (Exception e)
	{
		throw e;
	}
}

public ComplexScene2D
createComplexScene()
throws Exception
{
	if (this instanceof NucNode)
	{
		// this.deleteAllLabels();
		return (null);
	}

	if (this instanceof NucCollection2D)
	{
		NucCollection2D nucStructure = (NucCollection2D)this;

		String parentName = ((ComplexScene)this.getParentCollection()).getName();
		String structureTypeName = null;
		String idName = null;
		String prefixName = null;


		if (this instanceof RNASingleStrand2D)
		{
			structureTypeName = "single_strand";
			idName = String.valueOf(((RNASingleStrand2D)this).getFivePrimeNuc().getID());
		}
		else if (this instanceof RNABasePair2D)
		{
			structureTypeName = "base_pair";
			idName = String.valueOf(((RNABasePair2D)this).getFivePrimeNuc().getID())
				+ "_" + String.valueOf(((RNABasePair2D)this).getThreePrimeNuc().getID());
		}
		else if (this instanceof RNAHelix2D)
		{
			structureTypeName = "helix";
			idName = String.valueOf(((RNAHelix2D)this).getFivePrimeStartNuc().getID());
		}
		else if (this instanceof RNASubDomain2D)
		{
			structureTypeName = "domain";
			idName = String.valueOf(((RNASubDomain2D)this).getFivePrimeNuc().getID());
		}
		else if (this instanceof RNAStackedHelix2D)
		{
			structureTypeName = "stacked_helix";
			idName = String.valueOf(((RNAStackedHelix2D)this).getFivePrimeStartNuc().getID());
		}
		else if (this instanceof RNACycle2D)
		{
			structureTypeName = "cycle";
			idName = String.valueOf(((RNACycle2D)this).getFivePrimeNuc().getID());
		}
		else if (this instanceof SSData2D)
		{
			structureTypeName = "ssdata";
			idName = ((SSData2D)this).getName();
		}
		else if (this instanceof RNAListNucs2D)
		{
			structureTypeName = "list_of_nucs";
			idName = "_" + String.valueOf(((RNAListNucs2D)this).getFivePrimeNuc().getID())
				+ "_" + String.valueOf(((RNAListNucs2D)this).getThreePrimeNuc().getID());
		}

		if (structureTypeName == null)
			throw new Exception("Null structureTypeName in ComplexCollection.createComplexScene()");
		if (idName == null)
			throw new Exception("Null idName in ComplexCollection.createComplexScene()");

		if (parentName == null)
			prefixName = structureTypeName + "_" + idName;
		else
			prefixName = parentName + "_" + structureTypeName + "_" + idName;

		ComplexSSDataCollection2D testSSDataCollection = null;
		ComplexScene2D testScene = null;

		SSData2D newSStr = new SSData2D(nucStructure);
		newSStr.setName(prefixName);
		newSStr.centerAtOrigin();
		testSSDataCollection = new ComplexSSDataCollection2D(prefixName, newSStr);
		testScene = new ComplexScene2D(prefixName, testSSDataCollection);

		if (testScene == null)
			throw new Exception("Null testScene in ComplexCollection.createComplexScene()");
		StringWriter strWriter = null;
		PrintWriter printWriter = null;

		try
		{
			strWriter = new StringWriter();
			printWriter = new PrintWriter(strWriter);
			testScene.printComplexXML(printWriter);
		}
		catch (Exception e)
		{
			throw e;
		}
		ComplexXMLParser xmlParser = new ComplexXMLParser();
		xmlParser.parse(strWriter.toString());
		
		return (xmlParser.getComplexScene());
	}
		
	ComplexScene2D tmpComplexScene = null;
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexCollection)
		{
			tmpComplexScene = ((ComplexCollection)obj).createComplexScene();
			if (tmpComplexScene != null)
				break;
		}
	}
	
	return (tmpComplexScene);
}

public void
init()
throws Exception
{
}

/**
** Find final drawObject under xPos,yPos.
** Can find path to drawObject by backing out through
** drawObject.getParentContainer until null.
*/

public DrawObject
findLeafNode(double xPos, double yPos, Vector includeTypes, Vector excludeTypes)
throws Exception
{
	// try looking in this's leaf nodes
	if (getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			if (drawObject.contains(xPos - this.getX(), yPos + this.getY()))
				return(drawObject);
		}
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexCollection)
		{
			DrawObject drawObject = (DrawObject)((ComplexScene)obj).findLeafNode(
				xPos - this.getX(), yPos + this.getY(), includeTypes, excludeTypes);
			if ((drawObject != null) && (drawObject instanceof DrawObjectLeafNode))
				return (drawObject);
		}
	}

	return (null);
}

public Nuc2D
findNuc(double xPos, double yPos)
throws Exception
{
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		
		if (obj instanceof ComplexCollection)
		{
			DrawObject drawObject = (DrawObject)((ComplexScene)obj).findNuc(
				xPos - this.getX(), yPos + this.getY());
			if ((drawObject != null) && (drawObject instanceof Nuc2D))
			{
				return ((Nuc2D)drawObject);
			}
		}
	}

	return (null);
}

public void
resetNucLabelsLineLengths(double length)
throws Exception
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
				nuc.resetNucLabelsLineLengths(length);
				/*
				if (nuc.getLabelList() == null)
					continue;
				for (Enumeration e = nuc.getLabelList().elements();e.hasMoreElements();)
				{
					DrawObject drawObject = (DrawObject)e.nextElement();
					if (!(drawObject instanceof DrawLineObject))
						continue;
					((DrawLineObject)drawObject).setFromLength(length);
				}
				*/
			}
		}
		if (obj instanceof ComplexScene)
			((ComplexScene)obj).resetNucLabelsLineLengths(length);
	}
}

public void
listLeafNodes()
throws Exception
{
	if (this.getLabelList() != null)
	{
		for (Enumeration e = this.getLabelList().elements();e.hasMoreElements();)
		{
			Object obj = e.nextElement();
			System.out.println("Found Leaf Node: " + obj);
		}
	}

	if (this instanceof SSData)
	{
		SSData sstr = (SSData)this;
		for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
		{
			NucNode nuc = sstr.getNucAt(nucID);
			nuc.listLeafNodes();
		}
		return;
	}

	if (this.getItemCount() == 0) // then treat as leafNode (Nuc2D)
	{
		System.out.println("Found Leaf Node: " + this.toString());
		return;
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexCollection)
		{
			((ComplexCollection)obj).listLeafNodes();
		}
		else
		{
			throw new Exception("UNKNOWN OBJECT??: " + obj.toString());
		}
	}
}

// NEED to get to working correctly, currently doesn't shift nucs
// NEED to flatten everything around cartesian (0,0).
public void
reCenter(double deltaX, double deltaY)
throws Exception
{

	if (this instanceof Nuc2D)
	{
		// debug("\nSHIFTING NUC, before: " + this);
		this.shiftX(deltaX);
		this.shiftY(deltaY);
		// debug("SHIFTING NUC, after: " + this);
		return;
	}

	deltaX -= this.getX();
	deltaY -= this.getY();

	/*
	if ((this instanceof ComplexCollection) && (!(this instanceof NucNode)))
		debug("RECENTERING: " + this.toString() + " at: " + deltaX + " " + deltaY);
	*/

	if (this.getLabelList() != null)
	{
		for (Enumeration e = this.getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			drawObject.shiftX(deltaX);
			drawObject.shiftY(deltaY);
			// System.out.println("Found Leaf Node: " + obj);
		}
	}

	if (this instanceof SSData2D)
	{
		SSData2D sstr = (SSData2D)this;
		/*
		for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			nuc.reCenter(deltaX, deltaY);
		}
		*/
		// debug("RECENTERING with: " + deltaX + " " + deltaY);
		Vector delineators = sstr.getItemListDelineators();
		for (int i = 0;i < delineators.size();i+=2)
		{
			Nuc2D refNuc = (Nuc2D)delineators.elementAt(i);
			Nuc2D endNuc = (Nuc2D)delineators.elementAt(i+1);
			while (true)
			{
				// debug("RECENTERING before: " + refNuc);
				refNuc.reCenter(deltaX, deltaY);
				// debug("RECENTERING after: " + refNuc);
				if (refNuc.equals(endNuc))
					break;
				refNuc = refNuc.nextNuc2D();
			}
		}
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (obj instanceof NucNode)
			continue;
		if (obj instanceof ComplexCollection)
		{
			((ComplexCollection)obj).reCenter(deltaX, deltaY);
		}
		else
		{
			throw new Exception("UNKNOWN OBJECT??: " + obj.toString());
		}
	}

	this.setX(0.0);
	this.setY(0.0);
}

public void
clearHidden()
throws Exception
{
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexCollection))
			continue;
		((ComplexCollection)obj).setIsHidden(false);
		((ComplexCollection)obj).clearHidden();
	}
}

/* I think I was using this to set hidden attribute once colored. I'd rather
* have a hide single strands button in cycle
public void
colorAllRNASingleStrandedNucs(Color color)
throws Exception
{
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexCollection))
			continue;
		((ComplexCollection)obj).colorAllRNASingleStrandedNucs(color);
	}
}
*/

public void
setIsHidden(boolean hide)
throws Exception
{
	if (this instanceof NucNode)
	{
		super.setIsHidden(hide);
		return;
	}
	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexCollection))
			continue;
		((ComplexCollection)obj).setIsHidden(hide);
	}
}

public void
setHideForConstrain(boolean hideForConstrain)
throws Exception
{
	// this needs to be here since Nuc2D calls super which is this and
	// then this calls super to do its thing
	if (this instanceof NucNode)
	{
		super.setHideForConstrain(hideForConstrain);
		return;
	}

	if (this.getLabelList() != null)
	{
		for (Enumeration e = this.getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject label = (DrawObject)e.nextElement();
			label.setHideForConstrain(hideForConstrain);
		}
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexCollection))
			continue;
		((ComplexCollection)obj).setHideForConstrain(hideForConstrain);
	}
}

public void
setEditColor(Color editColor)
throws Exception
{
	// this needs to be here since Nuc2D calls super which is this and
	// then this calls super to do its thing
	if (this instanceof NucNode)
	{
		super.setEditColor(editColor);
		return;
	}

	if (this.getLabelList() != null)
	{
		for (Enumeration e = this.getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject label = (DrawObject)e.nextElement();
			label.setEditColor(editColor);
		}
	}

	for (int graphNodeID = 0;graphNodeID < this.getItemCount();graphNodeID++)
	{
		Object obj = this.getItemAt(graphNodeID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexCollection))
			continue;
		ComplexCollection testObj = (ComplexCollection)obj;
		((ComplexCollection)obj).setEditColor(editColor);
	}
}

public void
resetNucStats()
throws Exception
{
	for (int complexID = 0;complexID < this.getItemCount();complexID++)
	{
		Object obj = this.getItemAt(complexID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexCollection))
			continue;
		((ComplexCollection)obj).resetNucStats();
	}	
}

/*
public Vector
updateUndoList(int level, Object undoCmd)
{
	super (level, undoCmd);
}

public void
runUndo(int level)
throws Exception
{
	super (runUndo);
	// this should go through everything and find draw objects with a non-null
	// update list and then go through and look for "level" and run.
	// NEED to work in undo of an undo; maybe an undo of an undo keeps on adding
	// to undo list.
	// NEED to do a flow diagram
}
*/

public Vector
getComplexRNAStrands()
{
	Vector rnaStrandList = new Vector();
	
	for (int depth1ID = 0;depth1ID < this.getItemCount();depth1ID++)
	{
		Object rnaStrandCollectionObj = this.getItemAt(depth1ID);
		if (rnaStrandCollectionObj == null)
			continue;
		if (rnaStrandCollectionObj instanceof ComplexSSDataCollection2D)
		{
			ComplexSSDataCollection2D rnaStrandCollection =
				(ComplexSSDataCollection2D)rnaStrandCollectionObj;
			for (int depth2ID = 0;depth2ID < rnaStrandCollection.getItemCount();depth2ID++)
			{
				Object rnaStrandObj = rnaStrandCollection.getItemAt(depth2ID);
				if (rnaStrandObj == null)
					continue;
				// debug("rnaStrandOBJ: " + rnaStrandObj.toString());
				if (rnaStrandObj instanceof SSData2D)
					rnaStrandList.add((SSData2D)rnaStrandObj);
			}
		}
	}

	return (rnaStrandList);
}

private static void
debug(String s)
{
	System.err.println("ComplexCollection-> " + s);
}

}
