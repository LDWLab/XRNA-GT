package jimage;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import util.*;
import util.math.BRectangle2D;

public class
DrawObjectLeafNode
extends DrawObjectCollection
{

/************ DrawObjectCollection Implementation ***************/

public void
addItem(Object sceneGraphNode)
throws Exception
{
}

public Object
getItemAt(int index)
{
	return (null);
}

public void
setItemAt(Object obj, int index)
{
}

public void
delete()
throws Exception
{
	((Vector)((DrawObjectCollection)this.getParentCollection()).getCollection()).remove(this);
}

public DrawObject
findLeafNode(double x, double y, Vector includeTypes, Vector excludeTypes)
throws Exception
{
	return (null);
}

public void
init()
throws Exception
{
}

private boolean isOpen = false;

public void
setIsOpen(boolean isOpen)
{
    this.isOpen = isOpen;
}

public boolean
getIsOpen()
{
    return (this.isOpen);
}

private double lineWidth = 1.0;

public void
setLineWidth(double lineWidth)
{
    this.lineWidth = lineWidth;
	this.setLineStroke(new BasicStroke((float)lineWidth,
		BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
}

public double
getLineWidth()
{
    return (this.lineWidth);
}

private BasicStroke lineStroke = null;

public void
setLineStroke(BasicStroke lineStroke)
{
    this.lineStroke = lineStroke;
}

public BasicStroke
getLineStroke()
{
    return (this.lineStroke);
}

/************ DrawObject Implementation ***************/

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
	g2.setColor(this.getColor());
	g2.translate(this.getX(), -this.getY());
	this.setG2Transform(g2.getTransform());
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
}

private void
debug(String s)
{
	System.out.println("DrawObjectLeafNode-> " + s);
}

}
