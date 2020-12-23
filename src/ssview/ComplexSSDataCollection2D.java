package ssview;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Vector;

import jimage.DrawCircleObject;
import jimage.DrawObject;
import util.PostScriptUtil;
import util.StringUtil;
import util.Tuple4;
import util.math.BLine2D;
import util.math.BRectangle2D;

// currently holds SSData2Ds

public class
ComplexSSDataCollection2D
extends ComplexSSDataCollection
{

public
ComplexSSDataCollection2D()
{
	super();
}

public
ComplexSSDataCollection2D(String name, String author)
{
	super(name, author);
}

public
ComplexSSDataCollection2D(String name)
{
	super(name);
}

/*
public
ComplexSSDataCollection2D(String name, SSData sstr)
throws Exception
{
	super(name, sstr);
}
*/

public
ComplexSSDataCollection2D(String name, NucCollection2D nucCollection)
throws Exception
{
	super(name, nucCollection);
}

public void
setNewSSComplexElement(SSData2D newSSData)
throws Exception
{
	// first check if newSSData is already in
	for(int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
	{
		Object complexElement = this.getItemAt(complexItemID);
		if (complexElement instanceof SSData2D)
		{
			SSData2D ssData = (SSData2D)complexElement;
			if(ssData.getName().equals(newSSData.getName()))
				throw new Exception("Error in Complex.setNewSSComlexElement: " +
					newSSData.getName() + " is already in Complex");
		}
	}
	this.setCurrentSSData(newSSData);
	addItem(this.getCurrentSSData());
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
		if (!(obj instanceof SSData2D))
			continue;
		((SSData2D)obj).clearAnnotations();
	}
}

public void
setSymbols(Object drawObject)
throws Exception
{
	for (int objID = 0;objID < this.getItemCount();objID++)
	{
		Object obj = this.getItemAt(objID);
		if (obj == null)
			continue;
		if (!(obj instanceof SSData2D))
			continue;
		((SSData2D)obj).setSymbols(drawObject);
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
		if (!(obj instanceof SSData2D))
			continue;
		((SSData2D)obj).setFonts(font);
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
		if (!(obj instanceof SSData2D))
			continue;
		((SSData2D)obj).runSetIsNucPath(isNucPath, pathWidth, color);
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
		if (!(obj instanceof SSData2D))
			continue;
		Nuc2D nuc = ((SSData2D)obj).findNuc(xPos, yPos);
		if (nuc != null)
			return (nuc);
	}

	return (null);
}
*/

public static ComplexScene2D
getNewPartitionedComplexScene(SSData2D sstr, int nucID0, int nucID1)
throws Exception
{
	return (getNewPartitionedComplexScene(sstr, sstr.getNuc2DAt(nucID0),
		sstr.getNuc2DAt(nucID1)));
}

public static ComplexScene2D
getNewPartitionedComplexScene(SSData2D sstr, Nuc2D firstListNuc, Nuc2D secondListNuc)
throws Exception
{
	Vector partitionList = sstr.partitionSSData(firstListNuc, secondListNuc);

	if ((partitionList == null) || (partitionList.size() <= 1) ||
		(partitionList.size() > 3)) // MAYBE allow for > 3 later
		return (null);

	ComplexScene2D complexScene = new ComplexScene2D("testScene");
	ComplexSSDataCollection2D complexSSDataCollection =
		new ComplexSSDataCollection2D("testStr");
	

	ComplexSSDataCollection2D parentSSData2DCollection =
		(ComplexSSDataCollection2D)sstr.getParentCollection();
	for (int objID = 0;objID < parentSSData2DCollection.getItemCount();objID++)
	{
		Object obj = parentSSData2DCollection.getItemAt(objID);
		if ((obj == null) || !(obj instanceof SSData2D))
			continue;
		SSData2D tmpSStr = (SSData2D)obj;
		if (tmpSStr.getName().equals(sstr.getName()))
			continue;
		complexSSDataCollection.addItem(tmpSStr);
	}

	complexSSDataCollection.addItem((SSData2D)partitionList.elementAt(0));
	complexSSDataCollection.addItem((SSData2D)partitionList.elementAt(1));
	if (partitionList.size() == 3)
		complexSSDataCollection.addItem((SSData2D)partitionList.elementAt(2));
	complexScene.addItem(complexSSDataCollection);

	complexScene.resetNucNumbers();


	/*
	SSData2D testSStr = (SSData2D)complexSSDataCollection.getItemAt(0);
	debug("HEREERE sstr0: " + testSStr.getName() + " " + testSStr.getNucCount());
	testSStr = (SSData2D)complexSSDataCollection.getItemAt(1);
	debug("HEREERE sstr1: " + testSStr.getName() + " " + testSStr.getNucCount());
	testSStr = (SSData2D)complexSSDataCollection.getItemAt(2);
	debug("HEREERE sstr2: " + testSStr.getName() + " " + testSStr.getNucCount());
	*/


	return (complexScene);
}

public void
resetNucNumbers()
throws Exception
{
	/*
	debug("BEFORE:");
	for (int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
	{
		Object complexElement = this.getItemAt(complexItemID);
		if (!(complexElement instanceof SSData2D))
			continue;
		SSData2D sstr = (SSData2D)complexElement;
		for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			// if (nuc == null)
				// continue;
			System.out.println(sstr.getName() + " : " + nucID + " : " + nuc);
		}
	}
	*/

	for (int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
	{
		Object complexElement = this.getItemAt(complexItemID);
		if (!(complexElement instanceof SSData2D))
			continue;
		SSData2D sstr = (SSData2D)complexElement;
		if (sstr.getNuc2DAt(1) != null)
			continue;

		// now find first non-null nuc
		int firstNonNullNucID = 0;
		for (firstNonNullNucID = 1;
			firstNonNullNucID <= sstr.getNucCount();firstNonNullNucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(firstNonNullNucID);
			if (nuc != null)
				break;
		}

		int resetNucID = 0;
		for (int nucID = firstNonNullNucID;nucID <= sstr.getNucCount();nucID++)
		{
			resetNucID++;
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			if (nuc != null)
				nuc.setID(resetNucID);
			((Vector)sstr.getCollection()).set(resetNucID - 1, nuc);
		}
		for (int nucID = resetNucID + 1;nucID <= sstr.getNucCount();nucID++)
			((Vector)sstr.getCollection()).set(nucID - 1, null);
		((Vector)sstr.getCollection()).setSize(resetNucID);
		((Vector)sstr.getCollection()).trimToSize();

		// now reset basepair IDs
		for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);

			if (!nuc.isBasePair())
				continue;

			nuc.getBasePair().setBasePairID(nucID);
			if (!((NucCollection2D)nuc.getBasePair().getParentCollection()).getName().equals(((NucCollection2D)nuc.getParentCollection()).getName()))
				nuc.setBasePairSStrName(((NucCollection2D)nuc.getBasePair().getParentCollection()).getName());
		}

		sstr.setSSBPNucs();
	}

	/*
	debug("AFTER:");
	for (int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
	{
		Object complexElement = this.getItemAt(complexItemID);
		if (!(complexElement instanceof SSData2D))
			continue;
		SSData2D sstr = (SSData2D)complexElement;
		for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
		{
			Nuc2D nuc = sstr.getNuc2DAt(nucID);
			// if (nuc == null)
				// continue;
			System.out.println(sstr.getName() + " : " + nucID + " : " + nuc);
		}
	}
	*/
}

public void
printComplexXML(PrintWriter outFile)
throws Exception
{
	PrintWriter out = (PrintWriter)outFile;	

	//out.println("ComplexSSDataCollection2D.java printComplexXML() used.");
	
	String tabSpaces = "    ";
	out.print("<Complex Name='" + this.getName());
	if (this.getAuthor() != null)
		out.print("' Author='" + this.getAuthor());
	out.println("'>");
	// NEED to check if none 0.0,0.0 before printing
	if ((this.getX() != 0.0) || (this.getY() != 0.0))
		out.println(tabSpaces + "<SceneNodeGeom CenterX='" +
			StringUtil.roundStrVal(this.getX(), 2) + "' CenterY='" +
			StringUtil.roundStrVal(this.getY(), 2) + "' />");
	
	printLabelList(out);

	Object complexList = null;
	for(int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
		((ComplexCollection)this.getItemAt(complexItemID)).printComplexXML(out);

	out.println("</Complex>");
}

@Override
public void
printComplexCSV(PrintWriter outFile)
throws Exception
{
	PrintWriter out = (PrintWriter)outFile;	
	out.println("resNum,unModResName,X,Y,resColor,FontSize,LineX1,LineY1,LineX2,LineY2,LineThickness,LineColor,LabelX,LabelY,LabelSymbol,LabelFontSize,LabelColor");
	Tuple4<Double, Double, Double, Double>
		bounds = this.bounds();
	for(int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++) {
		((NucCollection2D)this.getItemAt(complexItemID)).printComplexCSV(out, new LinkedList<>(), bounds.t0, bounds.t1, bounds.t2, bounds.t3);
	}
}

@Override
public void printComplexTR(PrintWriter outFile) throws Exception {
	PrintWriter out = (PrintWriter) outFile;
	out.println("<structure>");
	Tuple4<Double, Double, Double, Double>
		bounds = this.bounds();
	for (int complexItemID = 0; complexItemID < this.getItemCount(); complexItemID++) {
		((ComplexCollection)this.getItemAt(complexItemID)).printComplexTR(out, new LinkedList<>(), bounds.t0, bounds.t1, bounds.t2, bounds.t3);
	}
	out.println("</structure>");
}

@Override
public void printComplexSVG(PrintWriter outFile) throws Exception {
	outFile.println("<?xml version='1.0' encoding='UTF-8'?>\n<svg version=\"1.1\" baseProfile=\"basic\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\" width=\"612px\" height=\"792px\" viewBox=\"0 0 612 792\" xml:space=\"preserve\">");
	Tuple4<Double, Double, Double, Double>
		bounds = this.bounds();
	double
		minX = bounds.t0,
		minY = bounds.t1,
		maxX = bounds.t2,
		maxY = bounds.t3;
	LinkedList<LinkedList<Nuc2D>>
		allNucleotides = new LinkedList<>();
	for (int complexItemID = 0; complexItemID < this.getItemCount(); complexItemID++) {
		LinkedList<Nuc2D>
			nucleotides = new LinkedList<>();
		((ComplexCollection)this.getItemAt(complexItemID)).printComplexSVG(outFile, nucleotides, minX, minY, maxX, maxY);
		allNucleotides.add(nucleotides);
	}
	LinkedList<String>
		nucleotideLines = new LinkedList<>(),
		nucleotideCircles = new LinkedList<>();
	for (LinkedList<Nuc2D> nucleotides : allNucleotides) {
		for (Nuc2D nuc : nucleotides) {
			int
				nucID = nuc.getID(),
				pairID = nuc.getBasePairID();
			if (pairID != 0 && pairID > nucID) {
				Nuc2D
					pair = nuc.getBasePair2D();
				double
					dx = nuc.getFont().getSize() / 3.0,
					dy = -nuc.getFont().getSize() / 3.0;
				if (nuc.inCanonicalBasePair()) {
					Point2D.Double 
						a = new Point2D.Double(nuc.getX() + dx, nuc.getY() + dy), 
						b = new Point2D.Double(pair.getX() + dx, pair.getY() + dy);
					Point2D 
						p1 = new BLine2D(a, b).getPointAtT(RNABasePair.BP_LINE_MULT),
						p2 = new BLine2D(b, a).getPointAtT(RNABasePair.BP_LINE_MULT);
					nucleotideLines.add("<line fill=\"none\" stroke=\"black\" stroke-width=\"" + 0.2/*lineObj.getLineStroke().getLineWidth()*/ + "\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"" + StringUtil.roundStrVal(p1.getX(), 2) + "\" y1=\"" + StringUtil.roundStrVal(p1.getY(), 2) + "\" x2=\"" + StringUtil.roundStrVal(p2.getX(), 2) + "\" y2=\"" + StringUtil.roundStrVal(p2.getY(), 2) + "\"/>");
				} else if (nuc.inWobbleBasePair()) {
					Point2D.Double
						a = new Point2D.Double(nuc.getX() + dx * 1.1d, nuc.getY() + dy), 
						b = new Point2D.Double(pair.getX() + dx * 1.1d, pair.getY() + dy),
						center = new Point2D.Double((a.getX() + b.getX()) / 2d, (a.getY() + b.getY()) / 2d);
					DrawCircleObject
						circle = nuc.getDrawCircleObject();
					double
						radius = (circle == null ? nuc.getFont().getSize2D() / 3f : circle.getRadius()) * 0.5d;
					nucleotideCircles.add("<circle cx=\"" + center.x + "\" cy=\"" + center.y + "\" r=\"" + radius + "\" fill=\"black\"/>");
				} else if (nuc.inMisMatchBasePair()) {
					Point2D.Double
						a = new Point2D.Double(nuc.getX() + dx * 1.1d, nuc.getY() + dy), 
						b = new Point2D.Double(pair.getX() + dx * 1.1d, pair.getY() + dy),
						center = new Point2D.Double((a.getX() + b.getX()) / 2d, (a.getY() + b.getY()) / 2d);
					DrawCircleObject
						circle = nuc.getDrawCircleObject();
					double
						radius = (circle == null ? nuc.getFont().getSize2D() / 3d : circle.getRadius()) * 0.5d;
					nucleotideCircles.add("<circle cx=\"" + center.x + "\" cy=\"" + center.y + "\" r=\"" + radius + "\" stroke=\"black\" fill-opacity=\"0.0\" stroke-width=\"" + (radius / 10d) + "\"/>");
				}
			}
		}
	}
	outFile.println("<g id=\"Nucleotide_Lines\">");
	for (String nucleotideLine : nucleotideLines) {
		outFile.println("\t" + nucleotideLine);
	}
	outFile.println("</g>");
	outFile.println("<g id=\"Nucleotide_Circles\">");
	for (String nucleotideCircle : nucleotideCircles) {
		outFile.println("\t" + nucleotideCircle);
	}
	outFile.println("</g>");
	outFile.println("</svg>");
//	LinkedList<String>
//		nucleotideLines = new LinkedList<>(),
//		nucleotideCircles = new LinkedList<>();
//	for (int complexItemID = 0; complexItemID < this.getItemCount(); complexItemID++) {
//		nucleotideLines.add("<g id=\"Nucleotide_Lines" + complexItemID + "\">");
//		nucleotideCircles.add("<g id=\"Nucleotide_Circles" + complexItemID + "\">");
//		Vector
//			delineators = ((NucCollection2D)this.getItemAt(complexItemID)).getItemListDelineators();
//		if (delineators != null) {
//			SSData2D
//				sstr = ((Nuc2D)((NucNode)delineators.elementAt(0))).getParentSSData2D();
//			for (int i = 0; i < delineators.size(); i += 2) {
//				Nuc2D 
//					nuc0 = (Nuc2D)delineators.elementAt(i),
//					nuc1 = (Nuc2D)delineators.elementAt(i + 1);
//				for (int nucID = nuc0.getID(); nucID <= nuc1.getID(); nucID++) {
//					Nuc2D
//						nuc = sstr.getNuc2DAt(nucID);
//					if (nuc != null) {
//						int
//							pairID = nuc.getBasePairID();
//						if (pairID != 0 && pairID > nucID) {
//							Nuc2D
//								pair = nuc.getBasePair2D();
//							double
//								dx = nuc.getFont().getSize() / 3.0,
//								dy = -nuc.getFont().getSize() / 3.0;
//							if (nuc.inCanonicalBasePair()) {
//								Point2D.Double 
//									a = new Point2D.Double(nuc.getX() + dx, nuc.getY() + dy), 
//									b = new Point2D.Double(pair.getX() + dx, pair.getY() + dy);
//								Point2D 
//									p1 = new BLine2D(a, b).getPointAtT(RNABasePair.BP_LINE_MULT),
//									p2 = new BLine2D(b, a).getPointAtT(RNABasePair.BP_LINE_MULT);
//								nucleotideLines.add("\t<line fill=\"none\" stroke=\"black\" stroke-width=\"" + 0.2/*lineObj.getLineStroke().getLineWidth()*/ + "\" stroke-linejoin=\"round\" stroke-miterlimit=\"10\" x1=\"" +
//										StringUtil.roundStrVal(p1.getX(), 2) + "\" y1=\"" + StringUtil.roundStrVal(p1.getY(), 2) + "\" x2=\"" + StringUtil.roundStrVal(p2.getX(), 2) + "\" y2=\"" + StringUtil.roundStrVal(p2.getY(), 2) + "\"/>");
//							} else if (nuc.inWobbleBasePair()) {
//								Point2D.Double
//									a = new Point2D.Double(nuc.getX() + dx * 1.1d, nuc.getY() + dy), 
//									b = new Point2D.Double(pair.getX() + dx * 1.1d, pair.getY() + dy),
//									center = new Point2D.Double((a.getX() + b.getX()) / 2d, (a.getY() + b.getY()) / 2d);
//								DrawCircleObject
//									circle = nuc.getDrawCircleObject();
//								double
//									radius = (circle == null ? nuc.getFont().getSize2D() / 3f : circle.getRadius()) * 0.5d;
//								nucleotideCircles.add("\t<circle cx=\"" + center.x + "\" cy=\"" + center.y + "\" r=\"" + radius + "\" fill=\"black\"/>");
//							} else if (nuc.inMisMatchBasePair()) {
//								Point2D.Double
//									a = new Point2D.Double(nuc.getX() + dx * 1.1d, nuc.getY() + dy), 
//									b = new Point2D.Double(pair.getX() + dx * 1.1d, pair.getY() + dy),
//									center = new Point2D.Double((a.getX() + b.getX()) / 2d, (a.getY() + b.getY()) / 2d);
//								DrawCircleObject
//									circle = nuc.getDrawCircleObject();
//								double
//									radius = (circle == null ? nuc.getFont().getSize2D() / 3d : circle.getRadius()) * 0.5d;
//								nucleotideCircles.add("\t<circle cx=\"" + center.x + "\" cy=\"" + center.y + "\" r=\"" + radius + "\" stroke=\"black\" fill-opacity=\"0.0\" stroke-width=\"" + (radius / 10d) + "\"/>");
//							}
//						}
//					}
//				}
//			}
//		}
//		nucleotideLines.add("</g>");
//		nucleotideCircles.add("</g>");
//	}
//	for (String nucleotideLine : nucleotideLines) {
//		out.println(nucleotideLine);
//	}
//	for (String nucleotideCircle : nucleotideCircles) {
//		out.println(nucleotideCircle);
//	}
//	out.print("<g id=\"WaterMark\">\n"
//			+ "<text id=\"Watermark\" transform=\"matrix(1 0 0 1 8.738 760.145)\" fil=\"black\" font-family=\"Myriad Pro\" font-size=\"10\">A 3D-based secondary structure, generated by RiboVision.</text>\n"
//			+ "<text id=\"Date\" transform=\"matrix(1 0 0 1 83.738 775.145)\" fill=\"black\" font-family=\"Myriad Pro\" font-size=\"8\">Saved on " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "</text>\n</g>\n");
//	out.println("</svg>");
}

public ComplexScene2D
wrapInComplexScene2D(String name)
throws Exception
{
	ComplexScene2D complexScene = new ComplexScene2D(name);
	complexScene.addItem(this);
	return (complexScene);
}

/*************** Draw Object Implementation *******************/

public void
setColor(Color color)
throws Exception
{
	for(int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
		((DrawObject)this.getItemAt(complexItemID)).setColor(color);
}

public void
update(Graphics2D g2)
throws Exception
{
	BRectangle2D rect = null;

	for(int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
	{
		Object complexElement = this.getItemAt(complexItemID);
		DrawObject drawObject = (DrawObject)complexElement;
		drawObject.update(g2);
		/*
		debug("DRWOBJ: " + drawObject.toString());
		debug("ADDING BBOX: " + drawObject.getBoundingBox());
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

	if (this.getLabelList() != null)
	{
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
		{
			DrawObject drawObject = (DrawObject)e.nextElement();
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

	BRectangle2D newRect = new BRectangle2D(
		rect.getX() + this.getX(),
		rect.getY() - this.getY(),
		rect.getWidth(),
		rect.getHeight());
	
	this.setBoundingBox(newRect);
	this.setBoundingShape(this.getBoundingBox());
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
		// debug("IN ComplexSSDataCollection2D " + this.getName() + ", intersects");
	}

	g2.translate(this.getX(), -this.getY());
	this.setG2Transform(g2.getTransform());
	
	for(int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
		((DrawObject)this.getItemAt(complexItemID)).draw(g2, constrainedArea);

	this.drawLabelList(g2, constrainedArea);

	g2.translate(-this.getX(), this.getY());

	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.magenta);
		this.drawBoundingShape(g2);
	}
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
	if (this.getIsHidden())
		return;
	
	psUtil.printPSGSave();
	g2.translate(this.getX(), this.getY());
	this.setG2Transform(g2.getTransform());
	
	for(int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
		((DrawObject)this.getItemAt(complexItemID)).printPS(g2, psUtil);

	this.printPSLabelList(g2, psUtil);
	psUtil.printPSGRestore();

	g2.translate(-this.getX(), this.getY());

	/*
	if (this.getShowBoundingShape())
	{
		g2.setColor(Color.magenta);
		this.drawBoundingShape(g2);
	}
	*/
}

public void
erase(Graphics2D g2)
throws Exception
{
	for(int complexItemID = 0;complexItemID < this.getItemCount();complexItemID++)
		((DrawObject)this.getItemAt(complexItemID)).erase(g2);

	if (this.getLabelList() != null)
		for (Enumeration e = getLabelList().elements();e.hasMoreElements();)
			((DrawObject)e.nextElement()).erase(g2);
}

public static void
debug(String s)
{
	System.err.println("ComplexSSDataCollection2D-> " + s);
}

}
