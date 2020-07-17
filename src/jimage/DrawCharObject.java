package jimage;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

public class
DrawCharObject
extends DrawFontObject
{

public
DrawCharObject(char drawChar)
{
	setDrawChar(drawChar);
}

public
DrawCharObject(char drawChar, Color color)
throws Exception
{
	setDrawChar(drawChar);
	this.setColor(color);
}

public
DrawCharObject(char drawChar, Font font)
{
	setDrawChar(drawChar);
	this.setFont(font);
}

public
DrawCharObject(double x, double y, char drawChar)
throws Exception
{
	this(drawChar);
	this.setX(x);
	this.setY(y);
}

public
DrawCharObject(double x, double y, Font font, Color color,
	char drawChar)
throws Exception
{
	this(x, y, drawChar);
	this.setFont(font);
	this.setColor(color);
}

private char drawChar = '\0';

public void
setDrawChar(char drawChar)
{
    this.drawChar = drawChar;
	this.setCharString();
}

public char
getDrawChar()
{
    return (this.drawChar);
}

private char[] drawCharArray = new char[1];

public void
setCharString()
{
	drawCharArray[0] = this.getDrawChar();
	this.setDrawString(new String(drawCharArray));
}

private void
debug(String s)
{
	System.out.println("DrawCharObject-> " + s);
}

}
