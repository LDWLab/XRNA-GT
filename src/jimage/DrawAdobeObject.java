package jimage;

import java.awt.*;

public class
DrawAdobeObject
extends DrawCharObject
{

public
DrawAdobeObject(char drawChar)
{
	super(drawChar);
}

public
DrawAdobeObject(double x, double y, char drawChar)
throws Exception
{
	super(x, y, drawChar);
}

public
DrawAdobeObject(double x, double y, Font font, Color color,
	char drawChar)
throws Exception
{
	super(x, y, font, color, drawChar);
}

}
