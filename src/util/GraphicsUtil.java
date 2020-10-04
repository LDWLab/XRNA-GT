package util;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class
GraphicsUtil
{

public static final Graphics2D unitG2 =
	(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)).createGraphics();

public static final double MIN_LINEWIDTH = 0.2;

public static final int PAPER_WIDTH = 612;
public static final int PAPER_WIDTH_MINUS_1_INCH_BORDER = 468;
public static final int PAPER_HEIGHT = 792;
public static final int PAPER_HEIGHT_MINUS_1_INCH_BORDER = 648;

public static BasicStroke thinLineStroke = new BasicStroke(0.0f,
	BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

public static BasicStroke thin1LineStroke =
	new BasicStroke((float)MIN_LINEWIDTH,
	BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

public static BasicStroke thin04LineStroke =
	new BasicStroke(0.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

public static BasicStroke thin06LineStroke =
	new BasicStroke(0.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

public static BasicStroke thin08LineStroke =
	new BasicStroke(0.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

public static float axisJump = 10.0f;
private static float axisOnUnits = 0.3f;
private static float axisOffUnits = (float)axisJump - axisOnUnits;
private static float[] dashVals = new float[]{axisOnUnits, axisOffUnits};
public static BasicStroke axisStroke = new BasicStroke(
	1.0f, // linewidth
	BasicStroke.CAP_BUTT,
	BasicStroke.JOIN_MITER,
	1.0f, // miterlimit
	dashVals,
	0.1f);

final static public RenderingHints qualityRenderHints =
	new RenderingHints(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);

final static public RenderingHints colorRenderHints =
	new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//qualityRenderHints);
static
{
	colorRenderHints.add(qualityRenderHints);
	
	colorRenderHints.put(RenderingHints.KEY_COLOR_RENDERING,
		RenderingHints.VALUE_COLOR_RENDER_QUALITY);
}

final static public RenderingHints imageRenderHints =
	new RenderingHints(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);
			//colorRenderHints);
static
{
	imageRenderHints.add(colorRenderHints);
	
	imageRenderHints.put(RenderingHints.KEY_DITHERING,
		RenderingHints.VALUE_DITHER_DISABLE);
}

final static public RenderingHints stringAliasedRenderHints =
	new RenderingHints(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);
			//colorRenderHints);
static
{
	/* Not sure what either of these values do for strings
	stringAliasedRenderHints.put(RenderingHints.KEY_DITHERING,
		RenderingHints.VALUE_DITHER_DISABLE);
	*/
	stringAliasedRenderHints.add(colorRenderHints);
	
	stringAliasedRenderHints.put(RenderingHints.KEY_DITHERING,
		RenderingHints.VALUE_DITHER_ENABLE);
	stringAliasedRenderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,
		RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	stringAliasedRenderHints.put(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);	
	stringAliasedRenderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
}

final static public RenderingHints stringUnAliasedRenderHints =
	new RenderingHints(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);
			//stringAliasedRenderHints);
static
{
	stringUnAliasedRenderHints.add(stringAliasedRenderHints);
	
	stringUnAliasedRenderHints.put(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_OFF);	
	stringUnAliasedRenderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
}

final static public RenderingHints lineAliasedRenderHints =
	new RenderingHints(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);//colorRenderHints);
static
{
	lineAliasedRenderHints.add(colorRenderHints);
	
	lineAliasedRenderHints.put(RenderingHints.KEY_DITHERING,
		RenderingHints.VALUE_DITHER_DISABLE);
	lineAliasedRenderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,
		RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	lineAliasedRenderHints.put(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);	
}

final static public RenderingHints lineUnAliasedRenderHints =
	new RenderingHints(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);//lineAliasedRenderHints);
static
{
	lineUnAliasedRenderHints.add(lineAliasedRenderHints);
	
	lineUnAliasedRenderHints.put(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_OFF);	
}

final static public RenderingHints aliasedRenderHints =
	new RenderingHints(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);//colorRenderHints);
static
{
	aliasedRenderHints.add(colorRenderHints);
	
	aliasedRenderHints.put(RenderingHints.KEY_DITHERING,
		RenderingHints.VALUE_DITHER_DISABLE);
	aliasedRenderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,
		RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	aliasedRenderHints.put(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);	
	aliasedRenderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
}

final static public RenderingHints unAliasedRenderHints =
	new RenderingHints(RenderingHints.KEY_RENDERING,
			RenderingHints.VALUE_RENDER_QUALITY);//colorRenderHints);
static
{
	unAliasedRenderHints.add(colorRenderHints);
	
	unAliasedRenderHints.put(RenderingHints.KEY_DITHERING,
		RenderingHints.VALUE_DITHER_DISABLE);
	unAliasedRenderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,
		RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	unAliasedRenderHints.put(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_OFF);	
	unAliasedRenderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
		RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
}

public static BasicStroke
getDotStroke(float scaleVal)
{
	float dotOnUnits = 1.0f/(float)scaleVal;
	float dotOffUnits = 1.0f/(float)scaleVal;
	float[] dashVals = new float[]{dotOnUnits, dotOffUnits};
	return(new BasicStroke(
		0.0f, // linewidth
		BasicStroke.CAP_BUTT,
		BasicStroke.JOIN_MITER,
		1.0f, // miterlimit
		dashVals,
		0.1f));
}

public static void
drawDotCross(double x, double y, double length, float scaleVal,
	Graphics2D g2)
{
	length /= 2.0;
	g2.setStroke(getDotStroke(scaleVal));
	Line2D.Double line = new Line2D.Double(
		x - length, -y, x + length, -y);
	g2.draw(line);
	line.setLine(x, -y - length, x, -y + length);
	g2.draw(line);
}

public static void
drawCross(double x, double y, double length, Graphics2D g2)
{
	length /= 2.0;
	g2.setStroke(thinLineStroke);
	Line2D.Double line = new Line2D.Double(
		x - length, -y, x + length, -y);
	g2.draw(line);
	line.setLine(x, -y - length, x, -y + length);
	g2.draw(line);
}

public static void
drawDotLine(double x0, double y0, double x1, double y1, float scaleVal,
	Graphics2D g2)
{
	g2.setStroke(getDotStroke(scaleVal));
	Line2D.Double line = new Line2D.Double(x0, -y0, x1, -y1);
	g2.draw(line);
}

public static void
drawAxisOfRect(double x, double y, double w, double h, Graphics2D g2)
{
	Line2D.Double line = new Line2D.Double(x, y, x + w, y + h);
	g2.draw(line);

	line.setLine(x + w, y, x, y + h);
	g2.draw(line);

	line.setLine(x + w/2.0, y, x + w/2.0, y + h);
	g2.draw(line);

	line.setLine(x, y + h/2.0, x + w, y + h/2.0);
	g2.draw(line);
}

public static void
drawAxisRect(Rectangle2D.Double rect, Graphics2D g2)
{
	g2.setStroke(thinLineStroke);
	drawAxisOfRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), g2);
	g2.draw(rect);
}

public static void
drawAxisRect(Rectangle2D rect, Graphics2D g2)
{
	g2.setStroke(thinLineStroke);
	drawAxisOfRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), g2);
	g2.draw(rect);
}

/* USE util.math.BLine2D
public static Point2D.Double
getLineMidPoint(Line2D.Double line)
{
	return (new Point2D.Double((line.getX1() + line.getX2())/2.0,
		(line.getY1() + line.getY2())/2.0));
}

public static Point2D.Double
getSegmentMidPoint(Point2D.Double pt0, Point2D.Double pt1)
{
	return (new Point2D.Double((pt0.getX() + pt1.getX())/2.0,
		(pt0.getY() + pt1.getY())/2.0));
}

public static Point2D.Double
getLineIntersectPoint(Line2D.Double line0, Line2D.Double line1)
{
	return(getSegmentIntersectPoint(
		line0.getP1().getX(),
		line0.getP1().getY(),
		line0.getP2().getX(),
		line0.getP2().getY(),
		line1.getP1().getX(),
		line1.getP1().getY(),
		line1.getP2().getX(),
		line1.getP2().getY()));
}

public static Point2D.Double
getSegmentIntersectPoint(Point2D.Double pt0StartPt, Point2D.Double pt0EndPt,
	Point2D.Double pt1StartPt, Point2D.Double pt1EndPt)
{
	return(getSegmentIntersectPoint(pt0StartPt.getX(), pt0StartPt.getY(),
		pt0EndPt.getX(), pt0EndPt.getY(), pt1StartPt.getX(),
		pt1StartPt.getY(), pt1EndPt.getX(), pt1EndPt.getY()));
}

public static Point2D.Double
getSegmentIntersectPoint(double head0x, double head0y,
	double tail0x, double tail0y, double head1x, double head1y,
	double tail1x, double tail1y)
{
	double tmp = 0.0;
	double tmpu = 0.0;
	double tmpv = 0.0;

	tmp = ((head1y - tail1y)*(head0x - tail0x) - (head1x - tail1x)*(head0y - tail0y));
	if(tmp == 0.0)
		return(null);
	tmpv = ((tail1x - tail0x)*(head0y - tail0y) - (tail1y - tail0y)*(head0x - tail0x)) / tmp;
	tmp = (head0x - tail0x)*(head1y - tail1y) - (head1x - tail1x)*(head0y - tail0y);
	if(tmp == 0.0)
		return(null);
	tmpu = ((tail1x - tail0x)*(head1y - tail1y) + (head1x - tail1x)*(tail0y - tail1y))/tmp;
	return(new Point2D.Double(
		tail1x + tmpv*(head1x - tail1x),
		tail1y + tmpv*(head1y - tail1y)));
}
*/

public static Rectangle2D
getLaserPrinterBoundingBox(boolean isLandscape, double scaleVal)
{
	int width = 0;
	int height = 0;

	if (isLandscape)
	{
		width = PAPER_HEIGHT;
		height = PAPER_WIDTH;
	}
	else
	{
		width = PAPER_WIDTH;
		height = PAPER_HEIGHT;
	}

	double minX = (-((double)width)/2.0 + 0.0)/scaleVal;
	double maxX = ((double)width/2.0 - 0.0)/scaleVal;
	double minY = (-((double)height)/2.0 + 0.0)/scaleVal;
	double maxY = ((double)height/2.0 - 0.0)/scaleVal;

	return (new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY));
}

public static Rectangle2D
getLaserPrinterBoundingBox(boolean isLandscape, int xSubAmt, int ySubAmt)
{
	int width = 0;
	int height = 0;

	if (isLandscape)
	{
		width = PAPER_HEIGHT;
		height = PAPER_WIDTH;
	}
	else
	{
		width = PAPER_WIDTH;
		height = PAPER_HEIGHT;
	}
	width -= xSubAmt;
	height -= ySubAmt;

	double minX = (-((double)width)/2.0);
	double maxX = ((double)width/2.0);
	double minY = (-((double)height)/2.0);
	double maxY = ((double)height/2.0);

	return (new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY));
}

public static Rectangle2D
getLaserPrinterMinusOneInchBoundingBox(boolean isLandscape, double scaleVal)
{
	int width = 0;
	int height = 0;

	// width X height for 1 inch in from side of paper
	if (isLandscape)
	{
		width = PAPER_HEIGHT_MINUS_1_INCH_BORDER;
		height = PAPER_WIDTH_MINUS_1_INCH_BORDER;
	}
	else
	{
		width = PAPER_WIDTH_MINUS_1_INCH_BORDER;
		height = PAPER_HEIGHT_MINUS_1_INCH_BORDER;
	}

	double minX = (-((double)width)/2.0 + 0.0)/scaleVal;
	double maxX = ((double)width/2.0 - 0.0)/scaleVal;
	double minY = (-((double)height)/2.0 + 0.0)/scaleVal;
	double maxY = ((double)height/2.0 - 0.0)/scaleVal;

	return (new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY));
}

public static Rectangle2D
getLaserPrinterMinusOneInchBoundingBox(boolean isLandscape)
{
	int width = 0;
	int height = 0;

	// width X height for 1 inch in from side of paper
	if (isLandscape)
	{
		width = PAPER_HEIGHT_MINUS_1_INCH_BORDER;
		height = PAPER_WIDTH_MINUS_1_INCH_BORDER;
	}
	else
	{
		width = PAPER_WIDTH_MINUS_1_INCH_BORDER;
		height = PAPER_HEIGHT_MINUS_1_INCH_BORDER;
	}

	double minX = (-((double)width)/2.0);
	double maxX = ((double)width/2.0);
	double minY = (-((double)height)/2.0);
	double maxY = ((double)height/2.0);

	return (new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY));
}

public static void
draw2DCartesianCoordinateSystem(boolean isLandscape, Color color,
	double scaleVal, Graphics2D g2)
throws Exception
{
	Rectangle2D rect = getLaserPrinterMinusOneInchBoundingBox(isLandscape, scaleVal);
	double minX = rect.getX();
	double minY = rect.getY();
	double maxX = rect.getX() + rect.getWidth();
	double maxY = rect.getY() + rect.getHeight();

	double jump = 10.0;
	g2.setPaint(color);

	// draw axis
	g2.setStroke(thinLineStroke);
	Line2D.Double line = null;
	line = new Line2D.Double(minX, 0.0, maxX, 0.0);
	g2.draw(line);
	line = new Line2D.Double(0.0, minY, 0.0, maxY);
	g2.draw(line);

	// draw some ref pts
	/*
	g2.setStroke(axisStroke);
	for (double j = jump;j <= maxY;j += jump)
	{
		g2.draw(new Line2D.Double(0.0, -j, maxX, -j));
		g2.draw(new Line2D.Double(0.0, j, maxX, j));
		g2.draw(new Line2D.Double(0.1, -j, -maxX, -j));
		g2.draw(new Line2D.Double(0.1, j, -maxX, j));

		g2.draw(new Line2D.Double(-j, 0.0, -j, maxY));
		g2.draw(new Line2D.Double(j, 0.0, j, maxY));
		g2.draw(new Line2D.Double(-j, 0.1, -j, -maxY));
		g2.draw(new Line2D.Double(j, 0.1, j, -maxY));
	}
	*/

	g2.setStroke(thinLineStroke);
	g2.setPaint(Color.blue);
	line = new Line2D.Double(minX, minY, minX, maxY);
	g2.draw(line);
	line = new Line2D.Double(minX, minY, maxX, minY);
	g2.draw(line);
	line = new Line2D.Double(minX, maxY, maxX, maxY);
	g2.draw(line);
	line = new Line2D.Double(maxX, minY, maxX, maxY);
	g2.draw(line);

	rect = getLaserPrinterBoundingBox(isLandscape, scaleVal);
	minX = rect.getX();
	minY = rect.getY();
	maxX = rect.getX() + rect.getWidth();
	maxY = rect.getY() + rect.getHeight();
	line = new Line2D.Double(minX, minY, minX, maxY);
	g2.draw(line);
	line = new Line2D.Double(minX, minY, maxX, minY);
	g2.draw(line);
	line = new Line2D.Double(minX, maxY, maxX, maxY);
	g2.draw(line);
	line = new Line2D.Double(maxX, minY, maxX, maxY);
	g2.draw(line);
}

static public void
drawDepthLines(Graphics2D g2, int wx, int wy, int ww, int wh, int depth,
	int border, Color topShadow, Color bottomShadow)
{
	int i, j, winx, winy, winwidth, winheight;
	boolean darkenBorder = false;

	winx = wx + depth + border;
	winy = wy + depth + border;
	ww -= (2*depth + 2*border);
	wh -= (2*depth + 2*border);

	if(depth < 0)
		depth = -depth;
	for(i=1,j=1;i <= depth + border;i++,j+=2)
	{
			winx--;
			winy--;
			winwidth = ww + j;
			winheight = wh + j;

			if(i > depth)
			{
				if(darkenBorder)
					g2.setColor(Color.black);
				else
					g2.setColor(Color.black);
			}
			else
			{
				g2.setColor(topShadow);
			}
			// top
			g2.drawLine(winx, winy, winx + winwidth, winy);
			// left side
			g2.drawLine(winx, winy, winx, winy + winheight);

			if(i > depth)
			{
				if(darkenBorder)
					g2.setColor(Color.black);
				else
					g2.setColor(Color.black);
			}
			else
			{
				g2.setColor(bottomShadow);
			}
			// right
			g2.drawLine(winx + winwidth, winy + 1, winx + winwidth,
				winy  + winheight);
			// bottom
			g2.drawLine(winx + 1, winy  + winheight, winx + winwidth,
				winy  + winheight);
	}
}

public static BufferedImage
cloneImage(BufferedImage ofImg)
{
	BufferedImage newImg = new BufferedImage(
		ofImg.getWidth(), ofImg.getHeight(),
			ofImg.getType());
	Graphics2D g2 = newImg.createGraphics();
	AffineTransform af = new AffineTransform();
	AffineTransformOp ato = new AffineTransformOp(af, imageRenderHints);
	g2.drawImage(ofImg, ato, 0, 0);

	return (newImg);
}

private static void
debug(String s)
{
	System.out.println("GraphicsUtil-> " + s);
}

}

