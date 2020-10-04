package util.math;

import java.awt.*;
import java.awt.geom.*;

import javax.vecmath.*;

public class
BRectangle2D
extends Rectangle2D.Double
{

public
BRectangle2D()
{
	super();
}

public
BRectangle2D(double x, double y, double width, double height)
{
	super(x, y, width, height);
}

public
BRectangle2D(Rectangle2D rect)
{
	this(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
}

public boolean
intersects(BRectangle2D rect)
{
	return (this.intersects(rect.getX(), rect.getY(),
		rect.getWidth(), rect.getHeight()));
}

public void
translate(double x, double y)
{
	this.setRect(this.getX() + x, this.getY() + y,
		this.getWidth(), this.getHeight());
}

public void
setLocation(double x, double y)
{
	this.setRect(x, y, this.getWidth(), this.getHeight());
}

public String
toString()
{
	return("BRectangle2D: " + this.getX() + " " + this.getY() + " " + this.getWidth() + " " + this.getHeight());
}

public static void
debug(String s)
{
	System.out.println("BRectangle2D-> " + s);
}

}
