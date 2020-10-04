package ssview;

import java.awt.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import jimage.DrawObject;
import jimage.DrawObjectCollection;
import jimage.DrawObjectLeafNode;
import jimage.DrawStringObject;
import jimage.DrawLineObject;
import jimage.DrawParallelogramObject;

import util.*;
import util.math.BRectangle2D;

/**
** List of complexs in scene.
*/

public class
ComplexScene2D
extends ComplexScene
{

public
ComplexScene2D()
{
	super();
}

public
ComplexScene2D(String name, String author)
{
	super(name, author);
}

public
ComplexScene2D(String name)
{
	super(name);
}

public
ComplexScene2D(String name, ComplexSSDataCollection collection)
throws Exception
{
	super(name, collection);
}

private double psScale = 1.0;

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
eraseNucPath(Graphics2D g2)
throws Exception
{
	// g2.translate(this.getX(), -this.getY());	
	/*
	for(int complexID = 0;complexID < this.getItemCount();complexID++)
		((DrawObject)this.getItemAt(complexID)).eraseNucPath(g2);
	*/
	for (int objID = 0;objID < this.getItemCount();objID++)
	{
		Object obj = this.getItemAt(objID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexSSDataCollection2D)
		{
			ComplexSSDataCollection2D collection =
				(ComplexSSDataCollection2D)obj;
			for (int obj1ID = 0;obj1ID < collection.getItemCount();obj1ID++)
			{
				Object obj1 = collection.getItemAt(obj1ID);
				if (obj1 instanceof SSData2D)
				{
					((SSData2D)obj1).erase(g2);
				}
			}
		}
	}
	// g2.translate(-this.getX(), this.getY());
}

public void
resetNucNumbers()
throws Exception
{
	for (int objID = 0;objID < this.getItemCount();objID++)
	{
		Object obj = this.getItemAt(objID);
		if (obj == null)
			continue;
		if (obj instanceof ComplexSSDataCollection2D)
			((ComplexSSDataCollection2D)obj).resetNucNumbers();
	}
}

/*************** DrawObject Implementation *******************/

public void
setColor(Color color)
throws Exception
{
	for(int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
		((DrawObject)this.getItemAt(complexItemID)).setColor(color);
}

public void
update()
throws Exception
{
	this.update(GraphicsUtil.unitG2);
}

public void
update(Graphics2D g2)
throws Exception
{
	super.update(g2);	

	BRectangle2D rect = null;

	for(int complexID = 0;complexID < this.getItemCount();complexID++)
	{
		Object complexElement = this.getItemAt(complexID);
		DrawObject drawObject = (DrawObject)complexElement;
		drawObject.update(g2);
		if (rect == null)
		{
			rect = new BRectangle2D();
			rect.setRect(drawObject.getBoundingBox());
		}
		else
		{
			rect.add(drawObject.getBoundingBox());
		}
	}

	if (this.getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			if (drawObject == null)
				continue;
			drawObject.update(g2);	
			/*
			if (drawObject instanceof DrawParallelogramObject)
				debug("PARALLEL.BB: " + drawObject.getBoundingBox());
			*/
			if (rect == null)
			{
				rect = new BRectangle2D();
				rect.setRect(drawObject.getBoundingBox());
			}
			else
			{
				rect.add(drawObject.getBoundingBox());
			}
		}
	}

	if (rect == null)
		return;

	BRectangle2D newRect = new BRectangle2D(
		rect.getX() + this.getX(),
		rect.getY() - this.getY(),
		rect.getWidth(),
		rect.getHeight());
	
	this.setBoundingBox(newRect);

	this.setBoundingShape(this.getBoundingBox());

	this.setDeltaX(newRect.getX() + newRect.getWidth()/2.0);
	this.setDeltaY(newRect.getY() + newRect.getHeight()/2.0);
}

public void
erase(Graphics2D g2)
throws Exception
{
	g2.translate(this.getX(), -this.getY());	
	for(int complexID = 0;complexID < this.getItemCount();complexID++)
		((DrawObject)this.getItemAt(complexID)).erase(g2);
	g2.translate(-this.getX(), this.getY());
}

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
	
	if (constrainedArea != null)
	{
		if (!this.intersects(constrainedArea, g2))
			return;
		// debug("IN ComplexScene2D " + this.getName() + ", intersects");
	}

	g2.translate(this.getX(), -this.getY());
	this.setG2Transform(g2.getTransform());
	
	for(int complexID = 0;complexID < this.getItemCount();complexID++)
		((DrawObject)this.getItemAt(complexID)).draw(g2, constrainedArea);

	this.drawLabelList(g2, constrainedArea);

	g2.translate(-this.getX(), this.getY());

	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.black);
		this.drawBoundingShape(g2);
	}
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	if (this.getIsHidden())
		return;

	g2.translate(this.getX(), -this.getY());
	psUtil.printPSGSave();
	psUtil.printPSTranslate(this.getX(), this.getY());

	this.setG2Transform(g2.getTransform());
	
	for(int complexID = 0;complexID < this.getItemCount();complexID++)
		((DrawObject)this.getItemAt(complexID)).printPS(g2, psUtil);

	this.printPSLabelList(g2, psUtil);

	psUtil.printPSGRestore();

	g2.translate(-this.getX(), this.getY());

	/*
	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.black);
		this.drawBoundingShape(g2);
	}
	*/
}

/************* END DrawObject **********************/

public void
center()
throws Exception
{
	this.shiftX(this.getDeltaX());
	this.shiftY(-this.getDeltaY());
}

public void
printComplexXML(PrintWriter outFile)
throws Exception
{
	// outFile.println("<!DOCTYPE ComplexDocument SYSTEM 'file:../ssview/ComplexXML.dtd'>");
	outFile.println();

	outFile.print("<ComplexDocument Name='" + this.getName());
	if (this.getAuthor() != null)
		outFile.print("' Author='" + this.getAuthor());
	if (this.getPSScale() != 1.0)
		outFile.print("' PSScale='" + this.getPSScale());
	if (this.getLandscapeMode())
		outFile.print("' LandscapeMode='true");
	outFile.println("'>");

	//System.out.println("Got the Document Name");
	
	// currently don't print out scale as it is taken from browser
	// need to put a particular scale val back in by hand
	if ((this.getX() != 0.0) || (this.getY() != 0.0) ||
		(this.getScaleValue() != 1.0))
	{
		if (this.getScaleValue() == 1.0)
			outFile.println("<SceneNodeGeom CenterX='" + StringUtil.roundStrVal(this.getX(), 2) +
				"' CenterY='" + StringUtil.roundStrVal(this.getY(), 2) + "' />");
		else
			outFile.println("<SceneNodeGeom CenterX='" + StringUtil.roundStrVal(this.getX(), 2) +
				"' CenterY='" + StringUtil.roundStrVal(this.getY(), 2) + "' Scale='" +
					StringUtil.roundStrVal(this.getScaleValue(), 2) + "' />");
	}
	//System.out.println("Got the Centers and Scale");
	printLabelList(outFile);
	//System.out.println("Printed the Labels");
	for(int complexID = 0;complexID < this.getItemCount();complexID++)
	{
		Object obj = this.getItemAt(complexID);
		if (obj == null)
			continue;
		
		((ComplexCollection)obj).printComplexXML(outFile);
		//System.out.println("Called the other XML print");
	}

	outFile.println("</ComplexDocument>");
	
}

@Override
public void
printComplexCSV(PrintWriter outFile)
throws Exception
{
	
		for(int complexID = 0;complexID < this.getItemCount();complexID++)
		{
			Object obj = this.getItemAt(complexID);
			if (obj == null)
				continue;
			
			((ComplexCollection)obj).printComplexCSV(outFile);
		}
	
}

@Override
public void printComplexTR(PrintWriter outFile) throws Exception {
	for (int complexID = 0; complexID < this.getItemCount(); complexID++) {
		Object obj = this.getItemAt(complexID);
		if (obj != null) { ((ComplexCollection)obj).printComplexTR(outFile); }
	}
}

@Override
public void printComplexSVG(PrintWriter outFile) throws Exception {
	for (int complexID = 0; complexID < this.getItemCount(); complexID++) {
		Object obj = this.getItemAt(complexID);
		if (obj != null) { ((ComplexCollection)obj).printComplexSVG(outFile); }
	}
}

public ComplexScene2D
getComplexSceneFromParse()
throws Exception
{
	StringWriter strWriter = new StringWriter();
	PrintWriter printWriter = new PrintWriter(strWriter);
	this.printComplexXML(printWriter);
	ComplexXMLParser complexXMLParser = new ComplexXMLParser();
	complexXMLParser.parse(strWriter.toString());	
	return (complexXMLParser.getComplexScene());
}

public void
clearAnnotations()
throws Exception
{
	for (int objID = 0;objID < this.getItemCount();objID++)
	{
		Object obj = this.getItemAt(objID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexSSDataCollection2D))
			continue;
		((ComplexSSDataCollection2D)obj).clearAnnotations();
	}
}

/*
public Nuc2D
BAD_findNuc(double xPos, double yPos)
throws Exception
{
	for (int objID = 0;objID < this.getItemCount();objID++)
	{
		Object obj = this.getItemAt(objID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexSSDataCollection2D))
			continue;
		Nuc2D nuc = ((ComplexSSDataCollection2D)obj).findNuc(xPos - this.getX(), yPos + this.getY());
		if (nuc != null)
			return (nuc);
	}

	return (null);
}
*/

public void
setSymbols(Object drawObject)
throws Exception
{
	for (int objID = 0;objID < this.getItemCount();objID++)
	{
		Object obj = this.getItemAt(objID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexSSDataCollection2D))
			continue;
		((ComplexSSDataCollection2D)obj).setSymbols(drawObject);
	}
}

public void
setFonts(Font font)
throws Exception
{
	for (int objID = 0;objID < this.getItemCount();objID++)
	{
		Object obj = this.getItemAt(objID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexSSDataCollection2D))
			continue;
		((ComplexSSDataCollection2D)obj).setFonts(font);
	}
}

public void
runSetIsNucPath(boolean isNucPath, double pathWidth, Color color)
throws Exception
{
	for (int objID = 0;objID < this.getItemCount();objID++)
	{
		Object obj = this.getItemAt(objID);
		if (obj == null)
			continue;
		if (!(obj instanceof ComplexSSDataCollection2D))
			continue;
		((ComplexSSDataCollection2D)obj).runSetIsNucPath(isNucPath, pathWidth, color);
	}
}

public boolean
resetLocationFromHashtable(int level)
throws Exception
{
	boolean undoAtLevelFound = false;

	if (this.getLabelList() != null)
	{
		for (Enumeration e = this.getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			if (drawObject == null)
				continue;
			if (drawObject.resetLocationFromHashtable(level))
				undoAtLevelFound = true;
		}
	}

	for (int depth1ID = 0;depth1ID < this.getItemCount();depth1ID++)
	{
		Object rnaStrandCollectionObj = this.getItemAt(depth1ID);
		if (rnaStrandCollectionObj == null)
			continue;
		if (rnaStrandCollectionObj instanceof ComplexSSDataCollection2D)
		{
			ComplexSSDataCollection2D rnaStrandCollection =
				(ComplexSSDataCollection2D)rnaStrandCollectionObj;
			if (rnaStrandCollection.resetLocationFromHashtable(level))
				undoAtLevelFound = true;
			if (rnaStrandCollection.getLabelList() != null)
			{
				for (Enumeration e = rnaStrandCollection.getLabelList().elements();e.hasMoreElements();)
				{
					DrawObject drawObject = (DrawObject)e.nextElement();
					if (drawObject == null)
						continue;
					if (drawObject.resetLocationFromHashtable(level))
						undoAtLevelFound = true;
				}
			}
			for (int depth2ID = 0;depth2ID < rnaStrandCollection.getItemCount();depth2ID++)
			{
				Object rnaStrandObj = rnaStrandCollection.getItemAt(depth2ID);
				if (rnaStrandObj == null)
					continue;
				if (rnaStrandObj instanceof SSData2D)
				{
					if (((ComplexCollection)rnaStrandObj).getLabelList() != null)
					{
					for (Enumeration e = ((ComplexCollection)rnaStrandObj).getLabelList().elements();e.hasMoreElements();)
					{
						DrawObject drawObject = (DrawObject)e.nextElement();
						if (drawObject == null)
							continue;
						if (drawObject.resetLocationFromHashtable(level))
							undoAtLevelFound = true;
					}
					}
					if (((SSData2D)rnaStrandObj).resetLocationFromHashtable(level))
						undoAtLevelFound = true;
				}
			}
		}
	}

	return (undoAtLevelFound);
}

public void
clearLocationFromHashtable(int level)
throws Exception
{
	if (this.getLabelList() != null)
	{
		for (Enumeration e = this.getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
			if (drawObject == null)
				continue;
			drawObject.clearLocationFromHashtable(level);
		}
	}

	for (int depth1ID = 0;depth1ID < this.getItemCount();depth1ID++)
	{
		Object rnaStrandCollectionObj = this.getItemAt(depth1ID);
		if (rnaStrandCollectionObj == null)
			continue;
		if (rnaStrandCollectionObj instanceof ComplexSSDataCollection2D)
		{
			ComplexSSDataCollection2D rnaStrandCollection =
				(ComplexSSDataCollection2D)rnaStrandCollectionObj;
			rnaStrandCollection.clearLocationFromHashtable(level);
			if (rnaStrandCollection.getLabelList() != null)
			{
				for (Enumeration e = rnaStrandCollection.getLabelList().elements();e.hasMoreElements();)
				{
					DrawObject drawObject = (DrawObject)e.nextElement();
					if (drawObject == null)
						continue;
					drawObject.clearLocationFromHashtable(level);
				}
			}
			for (int depth2ID = 0;depth2ID < rnaStrandCollection.getItemCount();depth2ID++)
			{
				Object rnaStrandObj = rnaStrandCollection.getItemAt(depth2ID);
				if (rnaStrandObj == null)
					continue;
				if (rnaStrandObj instanceof SSData2D)
				{
					if (((ComplexCollection)rnaStrandObj).getLabelList() != null)
					{
					for (Enumeration e = ((ComplexCollection)rnaStrandObj).getLabelList().elements();e.hasMoreElements();)
					{
						DrawObject drawObject = (DrawObject)e.nextElement();
						if (drawObject == null)
							continue;
						drawObject.clearLocationFromHashtable(level);
					}
					}
					((SSData2D)rnaStrandObj).clearLocationFromHashtable(level);
				}
			}
		}
	}
}

public boolean
resetBasePairFromHashtable(int level)
throws Exception
{
	boolean undoAtLevelFound = false;

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
				if (rnaStrandObj instanceof SSData2D)
				{
					if (((SSData2D)rnaStrandObj).resetBasePairFromHashtable(level))
						undoAtLevelFound = true;
				}
			}
		}
	}

	return (undoAtLevelFound);
}

public void
clearBasePairFromHashtable(int level)
throws Exception
{
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
				if (rnaStrandObj instanceof SSData2D)
				{
					((SSData2D)rnaStrandObj).clearBasePairFromHashtable(level);
				}
			}
		}
	}
}

/*
public String
toString()
{
	StringBuffer buf = new StringBuffer();

	for(int complexID = 0;complexID < this.getItemCount();complexID++)
	{
		Object complexElement = this.getItemAt(complexID);
		ComplexCollection complex = (ComplexCollection)complexElement;
		buf.append(complex.getName());
		buf.append("\n");
		buf.append(complex.toString());
		buf.append("\n");
	}
	return(buf.toString());
}
*/

private void
debug(String s)
{
	System.err.println("ComplexScene2D-> " + s);
}

}
