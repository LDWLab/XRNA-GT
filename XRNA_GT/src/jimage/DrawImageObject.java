package jimage;

import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;

import util.GraphicsUtil;
import util.PostScriptUtil;
import util.math.BRectangle2D;

public class
DrawImageObject
extends DrawObjectLeafNode
{

public
DrawImageObject(double x, double y, BufferedImage img)
throws Exception
{
	this.setX(x);
	this.setY(y);
	this.setImg(img);
	this.setImgW(img.getWidth());
	this.setImgH(img.getHeight());
	this.setBoundingBox(new BRectangle2D(x, y,
		(double)this.getImgW(), (double)this.getImgH()));
}

/*
public
DrawImageObject(double x, double y, BufferedImage img, RenderingHints renderHints)
{
	this(x, y, img);
	this.setRenderHints(renderHints);
}
*/

public void
setX(double x)
throws Exception
{
	super.setX(x);
	this.setImageExtents();
}

public void
setY(double y)
throws Exception
{
	super.setY(y);
	this.setImageExtents();
}

private BufferedImage image = null;

public void
setImg(BufferedImage image)
{
    this.image = image;
}

public BufferedImage
getImg()
{
    return (this.image);
}

private int imgW = 0;

public void
setImgW(int imgW)
{
    this.imgW = imgW;
}

public int
getImgW()
{
    return (this.imgW);
}

private int imgH = 0;

public void
setImgH(int imgH)
{
    this.imgH = imgH;
}

public int
getImgH()
{
    return (this.imgH);
}

// NEED to replace box with a shape and use its contains
public boolean
contains(double x, double y)
throws Exception
{
	return (this.getIsPickable() && this.getBoundingShape().contains(x, y));
}

public void
setImageExtents()
throws Exception
{
	if (this.getImg() == null)
		return;
	this.setBoundingBox(new BRectangle2D(this.getX(), this.getY(),
		(double)this.getImgW(), (double)this.getImgH()));
	this.setBoundingShape((Shape)this.getBoundingBox());
}

public void
update(Graphics2D g2)
throws Exception
{
	super.update(g2);

	this.setImageExtents();
	// do g2 stuff
}

public void
draw(Graphics2D g2, BRectangle2D constrainedArea)
throws Exception
{
	if (this.getIsHidden())
		return;
	if (this.getHideForConstrain())
		return;
	AffineTransform af = new AffineTransform();
	// af.translate(this.getX(), this.getY());

	AffineTransformOp ato = new AffineTransformOp(af,
		GraphicsUtil.imageRenderHints);

	g2.drawImage(this.getImg(), ato,
		(int)this.getX(), -(int)this.getY());

	// NEED to deal with Image
	// g2.translate(-this.getX(), this.getY());
}

public void
printPS(Graphics2D g2, PostScriptUtil psUtil)
throws Exception
{
}

private void
debug(String s)
{
	System.out.println("DrawCircleObject-> " + s);
}

}
