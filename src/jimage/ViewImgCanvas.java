package jimage;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.print.*;

import javax.swing.*;
// import javax.media.jai.*;

import jimage.ImageOps;

public class
ViewImgCanvas
extends JPanel
// implements Printable
{

BufferedImage canvasImg = null;
int imgStartX, imgStartY;
int currImgX = 0, currImgY = 0;
int mousePressX = 0, mousePressY = 0;
int imgW = 0;
int imgH = 0;
private DrawObjectView parent = null;

public
ViewImgCanvas()
{
	imgStartX = imgStartY = 0;
	this.setDoubleBuffered(true);
}

public
ViewImgCanvas(DrawObjectView parent)
{
	this();
	this.parent = parent;
}

public
ViewImgCanvas(BufferedImage img)
{
	this();
	canvasImg = img;
}

public
ViewImgCanvas(BufferedImage img, int x, int y)
{
	this(img);
	setImgStartXY(x, y);
}

public void
update(Graphics g)
{
	paint(g);
}

private static AffineTransform paintAffTran = null;
static
{
	paintAffTran = new AffineTransform();
};

public void
paintComponent(Graphics g)
{
	super.paintComponent(g);

	if (parent == null)
	{
		 // debug("PICK AFFTRAN: " + paintAffTran.getTranslateX() + " " + paintAffTran.getTranslateY() + " " + paintAffTran.getScaleX());
		AffineTransform af = ((Graphics2D)g).getTransform();
		 // debug("PICK G2: " + af.getTranslateX() + " " + af.getTranslateY() + " " + af.getScaleX());
	}
	else
	{
		 // debug("SS AFFTRAN: " + paintAffTran.getTranslateX() + " " + paintAffTran.getTranslateY() + " " + paintAffTran.getScaleX());
		AffineTransform af = ((Graphics2D)g).getTransform();
		 // debug("SS G2: " + af.getTranslateX() + " " + af.getTranslateY() + " " + af.getScaleX());
	}

	if (canvasImg == null)
		return;
	Graphics2D g2 = (Graphics2D)g;

	/*
	// paintAffTran.translate((double)this.getImgStartX(), (double)this.getImgStartY());
	*/
	g2.drawRenderedImage((RenderedImage)canvasImg, paintAffTran);
	/*
	paintAffTran.translate((double)this.getWindowViewX() - paintAffTran.getTranslateX(), (double)this.getWindowViewY() - paintAffTran.getTranslateY());
	((Graphics2D)g).drawRenderedImage((RenderedImage)canvasImg.getSubimage(
		this.getWindowViewX(),
		this.getWindowViewY(),
		this.getWindowViewW(),
		this.getWindowViewH()), paintAffTran);
	*/
	
		/*
		g.drawImage(canvasImg, imgStartX, imgStartY, this);
		*/

		/* to scale: (BUT MAYBE Use affine transforms
		g.drawImage(canvasImg, imgStartX, imgStartY, 2 * canvasImg.getWidth(this), 2 * canvasImg.getHeight(this), this);
		*/
	
	/*
	g2.setColor(Color.green);
	g2.drawLine(
		this.getWindowViewX(),
		this.getWindowViewY(),
		this.getWindowViewX() + this.getWindowViewW(),
		this.getWindowViewY() + this.getWindowViewH());
	*/
	if (parent != null)
		parent.drawOverlay(g2);
}

public void
newPaintComponent(Graphics g)
{
	if (canvasImg == null)
		return;

	((Graphics2D)g).drawRenderedImage((RenderedImage)canvasImg.getSubimage(
		this.getWindowViewX(),
		this.getWindowViewY(),
		this.getWindowViewW(),
		this.getWindowViewH()), paintAffTran);
}

public void
setWindowViewParams(int x, int y, int w, int h)
{
	this.setWindowViewX(x);
	this.setWindowViewY(y);
	this.setWindowViewW(w);
	this.setWindowViewH(h);
}

private int windowViewX = 0;

public void
setWindowViewX(int windowViewX)
{
    this.windowViewX = windowViewX;
}

public int
getWindowViewX()
{
    return (this.windowViewX);
}

private int windowViewY = 0;

public void
setWindowViewY(int windowViewY)
{
    this.windowViewY = windowViewY;
}

public int
getWindowViewY()
{
    return (this.windowViewY);
}

private int windowViewW = 0;

public void
setWindowViewW(int windowViewW)
{
    this.windowViewW = windowViewW;
}

public int
getWindowViewW()
{
    return (this.windowViewW);
}

private int windowViewH = 0;

public void
setWindowViewH(int windowViewH)
{
    this.windowViewH = windowViewH;
}

public int
getWindowViewH()
{
    return (this.windowViewH);
}

public void
setImage(BufferedImage img)
{
	canvasImg = img;
	imgW = canvasImg.getWidth(this);
	imgH = canvasImg.getHeight(this);
	if (this.getWindowViewW() == 0)
		this.setWindowViewW(imgW);
	if (this.getWindowViewH() == 0)
		this.setWindowViewH(imgH);
	/*
	setPixelRaster();
	*/
}

public BufferedImage
getImage()
{
	return (canvasImg);
}

private int[][] pixelRaster = null;

public void
setPixelRaster()
{
	if (canvasImg == null)
		return;

	/*
	pixelRaster = ImageOps.getPixelRasterFromImg(canvasImg,
		getImgWidth(), getImgHeight());
	*/
}

/*
public int[][]
getPixelRaster()
{
	return (this.pixelRaster);
}
*/

public int
getImgPixelAt(int x, int y)
{
	/*
	// System.out.println("MADE IT 0: " + x + " " + y);
	if (pixelRaster == null)
		return (this.getBackground().getRGB());
	// System.out.println("MADE IT 1: " + getImgWidth() + " " + getImgHeight());
	
	if ((x < 0) || (x >= getImgWidth()))
		return (this.getBackground().getRGB());
	if ((y < 0) || (y >= getImgHeight()))
		return (this.getBackground().getRGB());

	return (pixelRaster[y][x]);
	*/
	if ((x < 0) || (y < 0) || (x >= getImgWidth()) || (y >= getImgHeight()))
		return(0);
	return (canvasImg.getRGB(x, y));
}

public Color
getImgColorAt(int x, int y)
{
	/*
	if (pixelRaster == null)
		return (this.getBackground());
	
	if ((x < 0) || (x >= getImgWidth()))
		return (this.getBackground());
	if ((y < 0) || (y >= getImgHeight()))
		return (this.getBackground());

	return (new Color(pixelRaster[y][x]));
	*/
	return (new Color(canvasImg.getRGB(x, y)));
}

public void
setImgStartXY(int x, int y)
{
	setImgStartX(x);
	setImgStartY(y);
}

public void
setImgStartX(int imgx)
{
	imgStartX = imgx;
}

public int
getImgStartX()
{
	return(imgStartX);
}

public void
setImgStartY(int imgy)
{
	imgStartY = imgy;
}

public int
getImgStartY()
{
	return(imgStartY);
}

public int
getImgWidth()
{
	/*
	if (canvasImg == null)
		return(0);
	return(canvasImg.getWidth(this));
	*/
	return (imgW);
}

public int
getImgHeight()
{
	/*
	if (canvasImg == null)
		return(0);
	return(canvasImg.getHeight(this));
	*/
	return (imgH);
}

/*
public int
print(Graphics g, PageFormat pageFormat, int pageIndex)
{
	if (pageIndex > 0)
		return (NO_SUCH_PAGE);
	Graphics2D g2 = (Graphics2D)g;
	g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	boolean wasBuffered = disableDoubleBuffering(this);
	this.paint(g2);
	restoreDoubleBuffering(this, wasBuffered);
	return (PAGE_EXISTS);
}

private boolean
disableDoubleBuffering(Component c)
{
	if (c instanceof JComponent == false)
		return false;
	JComponent jc = (JComponent)c;
	boolean wasBuffered = jc.isDoubleBuffered();
	jc.setDoubleBuffered(false);
	return (wasBuffered);
}

private void
restoreDoubleBuffering(Component c, boolean wasBuffered)
{
	if (c instanceof JComponent)
		((JComponent)c).setDoubleBuffered(wasBuffered);
}
*/

private static void
debug(String s)
{
	System.out.println("ViewImgCanvas-> " + s);
}

}
