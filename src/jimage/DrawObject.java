package jimage;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import util.PostScriptUtil;
import util.math.BRectangle2D;

public interface
DrawObject
{

public abstract void setScaleValue(double val);
public abstract double getScaleValue();

public abstract void
set(Object obj) throws Exception;

public abstract void setX(double x) throws Exception;
public abstract double getX() throws Exception;

public abstract void setY(double y) throws Exception;
public abstract double getY() throws Exception;

public abstract void setXY(double x, double y)
throws Exception;

public abstract void setXY(Point2D pt)
throws Exception;

public abstract void shiftX(double x) throws Exception;
public abstract void shiftY(double y) throws Exception;
public abstract void shiftXY(double x, double y) throws Exception;

public abstract void setDeltaX(double deltaX);
public abstract double getDeltaX();

public abstract void setDeltaY(double deltaY);
public abstract double getDeltaY();

public abstract void
setSaveLocationHashtable(Hashtable saveLocationHashtable);
public abstract Hashtable getSaveLocationHashtable();

public void runSetLocationHashtable(int level) throws Exception;
public boolean resetLocationFromHashtable(int level) throws Exception;

public void clearLocationFromHashtable(int level) throws Exception;

public abstract Point2D.Double getImgSpacePoint2D()
throws Exception;

public abstract void setColor(Color color) throws Exception;
public abstract Color getColor();

public abstract void setEditColor(Color editColor) throws Exception;
public abstract Color getEditColor();

public abstract void setIsEditable(boolean isEditable);
public abstract boolean getIsEditable() throws Exception;

public abstract void
setBoundingBox(BRectangle2D rect);

public abstract BRectangle2D
getBoundingBox();

public abstract BRectangle2D
getCenteredBoundingBox() throws Exception;

public abstract void
setShowBoundingBox(boolean showBoundingBox);

public abstract boolean
getShowBoundingBox();

public abstract void
drawBoundingBox(Graphics2D g);

public abstract void setBoundingShape(Shape boundingShape);
public abstract Shape getBoundingShape();

public abstract void
setShowBoundingShape(boolean showBoundingShape);

public abstract boolean
getShowBoundingShape();

public abstract void
drawBoundingShape(Graphics2D g);

public abstract boolean
contains(double xPos, double yPos) throws Exception;

public abstract boolean
contains(DrawObject drawObject) throws Exception;

public abstract boolean
intersects(BRectangle2D rect, Graphics2D g2) throws Exception;

public abstract void
setIsPickable(boolean isPickable);

public abstract boolean
getIsPickable();

public abstract void
update(Graphics2D g2) throws Exception; // g2 could be null

public abstract void
update() throws Exception;

public abstract void
setIsHidden(boolean isHidden) throws Exception;

public abstract boolean
getIsHidden();

public abstract boolean
isHidden();

public abstract void
setHideForConstrain(boolean hideForConstrain) throws Exception;

public abstract boolean
getHideForConstrain();

public abstract void
draw(Graphics2D g2, BRectangle2D constrainedArea) throws Exception;

public abstract void
printPS(Graphics2D g2, PostScriptUtil psUtil) throws Exception;

public abstract void
erase(Graphics2D g2) throws Exception;

public abstract void
clearBoundingBoxArea(Graphics2D g2) throws Exception;

public abstract void
delete(Graphics2D g2) throws Exception;

public abstract BufferedImage
drawImage(int imgW, int imgH, Color bgColor,
	Color topColor, Color bottomColor, int depth, int imgType)
throws Exception;

public abstract void setDrawObjectImg(BufferedImage drawObjectImg);
public abstract BufferedImage getDrawObjectImg() throws Exception;

public abstract void setDrawImgBGColor(Color drawImgBGColor);
public abstract Color getDrawImgBGColor();

public abstract void
setG2Transform(AffineTransform g2Transform);

public abstract AffineTransform
getG2Transform();

public abstract AffineTransform
getParentG2Transform();

/*
public abstract void
setRenderHints(RenderingHints renderHints);

public abstract RenderingHints
getRenderHints();
*/

public abstract void showProperties(Component parent);

public abstract Object getParentCollection();
public abstract void setParentCollection(Object parentCollection);

}
