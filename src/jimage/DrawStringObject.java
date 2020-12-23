package jimage;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

public class
DrawStringObject
extends DrawFontObject
{

public
DrawStringObject(double x, double y, Font font, Color color, String drawString)
throws Exception
{
	this.setX(x);
	this.setY(y);
	this.setFont(font);
	this.setColor(color);
	setDrawString(drawString);
}

public
DrawStringObject(Point2D pt, Font font, Color color, String drawString)
throws Exception
{
	this(pt.getX(), pt.getY(), font, color, drawString);
}

public
DrawStringObject(double x, double y, Font font, Color color,
	boolean showBoundingShape, String drawString)
throws Exception
{
	this(x, y, font, color, drawString);
	this.setShowBoundingShape(showBoundingShape);
}

// make a copy
public
DrawStringObject(DrawStringObject stringObj)
throws Exception
{
	this (
		stringObj.getX(),
		stringObj.getY(),
		stringObj.getFont(),
		stringObj.getColor(),
		stringObj.getDrawString());
}

public String
toString()
{
	return ("string: " + getDrawString());
}

private void
debug(String s)
{
	System.out.println("DrawStringObject-> " + s);
}

}
