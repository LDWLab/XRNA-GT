/*
TODOS:
1) to get a pixel:
      import java.awt.image.PixelGrabber;
      import java.awt.Image;
         ...
      static public int pixelValue(Image image, int x, int y) {
      // precondition: buffer must not be created from ImageProducer!
      // x,y should be inside the image, 
      // Returns an integer representing color value of the x,y pixel.
          int[] pixel=new int[1];
          pixel[0]=0;

      // pixel grabber fills the array with zeros if image you are
      // trying to grab from is non existent (or throws an exception)
          PixelGrabber grabber = new PixelGrabber(image,
                                           x, y, 1, 1, pixel, 0, 0);
          try {
              grabber.grabPixels();
          } catch (Exception e) {System.err.println(e.getMessage());}
          return pixel[0];
      }

2)

*/

package jimage;

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;
import java.applet.Applet;
import java.awt.image.ImageProducer;
import java.awt.image.ImageFilter;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

// import java.awt.image.Raster; // use when go to 1.2

import jmath.*;

public class
ImageOps
{

/*
** NEED TO BE ABLE TO DO masks, like if started with img background
** then put complement on top of that
*/

static public int clearPixel = 0x00c0c0c0;
static public int blackPixel = 0xff000000;
static public int whitePixel = 0xffffffff;


/*
DLHORLGSMTPLFT  DLHORLGSMTPRHT    DLHORLGSMBTLFT   DLHORLGSMBTRHT
s ---------- e  e ----------- s      ---------        --------  
 --------        ---------          -------          ------ 
  ------          -------          s ----- e        e ---- s  

DLHORSMLGTPLFT  DLHORSMLGTPRHT   DLHORSMLGBTLFT   DLHORSMLGBTRHT
 s ---- e       e ----- s            -----            ----
  ------         -------            -------          ------
 --------       ---------        s --------- e    e -------- s
													
DLHORSTACKLFTTPLFT  DLHORSTACKLFTTPRHT  DLHORSTACKRHTBTLFT  DLHORSTACKRHTBTRHT
s --------- e        e -------- s           --------            -------
---------            --------             --------            -------
---------            --------           s -------- e        e ------- s
													
DLHORSTACKRHTTPLFT  DLHORSTACKRHTTPRHT  DLHORSTACKLFTBTLFT  DLHORSTACKLFTBTRHT
s --------- e       e -------- s           --------            -------
 ---------           --------             --------            -------
  ---------            --------          s -------- e        e ------- s
													

DLVERLGSMTPRHT   DLVERLGSMBTRHT   DLVERSMLGTPLFT  DLVERSMLGBTLFT 
	 | s              | e            |              |  
	||               ||           s ||           e ||
   |||              |||            |||            |||
   |||              |||            |||            |||
   |||              |||            |||            |||  
	||               ||           e ||           s ||
	 | e              | s            |              |

DLVERLGSMTPLFT   DLVERLGSMBTLFT   DLVERSMLGTPRHT  DLVERSMLGBTRHT 
 s |              e |              |              |    
   ||               ||             || s           || e
   |||              |||            |||            |||
   |||              |||            |||            |||
   |||              |||            |||            |||  
   ||               ||             || e           || s
 e |              s |              |              |  

DLVERSTACKDOWNTPLFT DLVERSTACKDOWNBTLFT DLVERSTACKUPTPRHT DLVERSTACKUPBTRHT
 s |                 e |                    |                  |  
   ||                  ||                   ||                 || 
   |||                 |||                  ||| s              ||| e
   |||                 |||                  |||                |||
 e |||               s |||                  |||                |||
	||                  ||                   ||                 ||
	 |                   |                    | e                | s

DLVERSTACKUPTPLFT DLVERSTACKUPBTLFT  DLVERSTACKDOWNTPRHT DLVERSTACKDOWNBTRHT
	  |                  |                 | s                | e
	 ||                 ||                ||                 ||
  s |||              e |||               |||                |||
	|||                |||               |||                |||
	|||                |||               ||| e              ||| s
	||                 ||                ||                 || 
  e |                s |                 |                  |  

DLHORLGSMTPLFTBUTT    DLHORLGSMTPRHTBUTT
s -------                 ------- s  
 ------                 ------     
e -----                 ----- e    

DLVERLGSMTPLFTBUTT  DLVERLGSMTPRHTBUTT

s |                     | s
  ||                   ||  
  |||                 |||      
  |||                 |||      
  |||                 |||      
e |||                 ||| e    
*/
static final int DLHORLGSMTPLFT      = 0;
static final int DLHORLGSMTPRHT      = 1;
static final int DLHORLGSMBTLFT      = 2;
static final int DLHORLGSMBTRHT      = 3;
static final int DLHORSMLGTPLFT      = 4;
static final int DLHORSMLGTPRHT      = 5;
static final int DLHORSMLGBTLFT      = 6;
static final int DLHORSMLGBTRHT      = 7;
static final int DLHORSTACKLFTTPLFT  = 8;
static final int DLHORSTACKLFTTPRHT  = 9;
static final int DLHORSTACKRHTBTLFT  = 10;
static final int DLHORSTACKRHTBTRHT  = 11;
static final int DLHORSTACKRHTTPLFT  = 12;
static final int DLHORSTACKRHTTPRHT  = 13;
static final int DLHORSTACKLFTBTLFT  = 14;
static final int DLHORSTACKLFTBTRHT  = 15;
static final int DLVERLGSMTPRHT      = 16;
static final int DLVERLGSMBTRHT      = 17;
static final int DLVERSMLGTPLFT      = 18;
static final int DLVERSMLGBTLFT      = 19;
static final int DLVERLGSMTPLFT      = 20;
static final int DLVERLGSMBTLFT      = 21;
static final int DLVERSMLGTPRHT      = 22;
static final int DLVERSMLGBTRHT      = 23;
static final int DLVERSTACKDOWNTPLFT = 24;
static final int DLVERSTACKDOWNBTLFT = 25;
static final int DLVERSTACKUPTPLFT   = 26;
static final int DLVERSTACKUPBTLFT   = 27;
static final int DLVERSTACKUPTPRHT   = 28;
static final int DLVERSTACKUPBTRHT   = 29;
static final int DLVERSTACKDOWNTPRHT = 30;
static final int DLVERSTACKDOWNBTRHT = 31;
static final int DLHORLGSMTPLFTBUTT  = 32;
static final int DLHORLGSMTPRHTBUTT  = 33;
static final int DLVERLGSMTPLFTBUTT  = 34;
static final int DLVERLGSMTPRHTBUTT  = 35;
static final int DLVERLGSMBTLFTBUTT  = 36;

static public void
setDepthRectangle(int[][] pixelRaster, Rectangle rect, int borderWidth,
	int topPixel, int bottomPixel)
{
	setDepthLine(pixelRaster, rect.x, rect.y,
		rect.height, borderWidth, DLVERLGSMTPLFT, topPixel);
	setDepthLine(pixelRaster, rect.x + 1, rect.y,
		rect.width - 1, borderWidth, DLHORLGSMTPLFT, topPixel);
	setDepthLine(pixelRaster, rect.x + rect.width - 1, rect.y + 1,
		rect.height - 2, borderWidth, DLVERLGSMTPRHT, bottomPixel);
	setDepthLine(pixelRaster, rect.x + 1, rect.y + rect.height - 1,
		rect.width - 1, borderWidth, DLHORSMLGBTLFT, bottomPixel);
}

static public void
setDepthDivider(int[][] pixelRaster, int startx, int starty, int length,
	int depth, int brightlinetype, int brightPixel,
	int dimlinetype, int dimPixel, boolean topisbright)
{
	if(topisbright)
	{
		setDepthLine(pixelRaster, startx, starty, length, depth,
			dimlinetype, dimPixel);
		setDepthLine(pixelRaster, startx, starty, length, depth,
			brightlinetype, brightPixel);
	}
	else
	{
		setDepthLine(pixelRaster, startx, starty, length, depth,
			brightlinetype, brightPixel);
		setDepthLine(pixelRaster, startx, starty, length, depth,
			dimlinetype, dimPixel);
	}
}

static public void
setDepthLine(int[][] pixelRaster, int startx, int starty, int length,
	int depth, int linetype, int pixel)
{
	int row, col, d, endx, endy;

	switch(linetype)
	{
	  case DLHORLGSMTPLFT : // *
		endx = startx + length - 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx + d;col <= endx - d;col++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORLGSMTPRHT : // *
		endx = startx - length + 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx - d;col >= endx + d;col--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORLGSMBTLFT : // *
		endx = startx + length - 1;
		for(d = 0;d < depth;d++)
		{
			row = starty - d;
			for(col = startx - d;col <= endx + d;col++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORLGSMBTRHT :
		endx = startx - length + 1;
		for(d = 0;d < depth;d++)
		{
			row = starty - d;
			for(col = startx + d;col >= endx - d;col--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSMLGTPLFT :
		endx = startx + length - 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx - d;col <= endx + d;col++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSMLGTPRHT :
		endx = startx - length + 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx + d;col >= endx - d;col--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSMLGBTLFT :
		endx = startx + length - 1;
		for(d = 0;d < depth;d++)
		{
			row = starty - d;
			for(col = startx + d;col <= endx - d;col++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSMLGBTRHT :
		endx = startx - length + 1;
		for(d = 0;d < depth;d++)
		{
			row = starty - d;
			for(col = startx - d;col >= endx + d;col--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSTACKLFTTPLFT :
		endx = startx + length - 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx - d;col <= endx - d;col++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSTACKLFTTPRHT :
		endx = startx - length + 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx - d;col >= endx - d;col--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSTACKRHTBTLFT :
		endx = startx + length - 1;
		for(d = 0;d < depth;d++)
		{
			row = starty - d;
			for(col = startx + d;col <= endx + d;col++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSTACKRHTBTRHT :
		endx = startx - length + 1;
		for(d = 0;d < depth;d++)
		{
			row = starty - d;
			for(col = startx + d;col >= endx + d;col--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSTACKRHTTPLFT :
		endx = startx + length - 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx + d;col <= endx + d;col++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSTACKRHTTPRHT :
		endx = startx - length + 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx + d;col >= endx + d;col--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSTACKLFTBTLFT :
		endx = startx + length - 1;
		for(d = 0;d < depth;d++)
		{
			row = starty - d;
			for(col = startx - d;col <= endx - d;col++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORSTACKLFTBTRHT :
		endx = startx - length + 1;
		for(d = 0;d < depth;d++)
		{
			row = starty - d;
			for(col = startx - d;col >= endx - d;col--)
				pixelRaster[row][col] = pixel;
		}
		break;

	  case DLVERLGSMTPRHT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx - d;
			for(row = starty + d;row <= endy - d;row++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERLGSMBTRHT :
		endy = starty - length + 1;
		for(d = 0;d < depth;d++)
		{
			col = startx - d;
			for(row = starty - d;row >= endy + d;row--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSMLGTPLFT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty - d;row <= endy + d;row++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSMLGBTLFT :
		endy = starty - length + 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty + d;row >= endy - d;row--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERLGSMTPLFT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty + d;row <= endy - d;row++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERLGSMBTLFT :
		endy = starty - length + 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty - d;row >= endy + d;row--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSMLGTPRHT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx - d;
			for(row = starty - d;row <= endy + d;row++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSMLGBTRHT :
		endy = starty - length + 1;
		for(d = 0;d < depth;d++)
		{
			col = startx - d;
			for(row = starty + d;row >= endy - d;row--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSTACKDOWNTPLFT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty + d;row <= endy + d;row++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSTACKDOWNBTLFT : // last one
		endy = starty - length + 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty + d;row >= endy + d;row--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSTACKUPTPRHT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx - d;
			for(row = starty - d;row <= endy - d;row++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSTACKUPBTRHT :
		endy = starty - length + 1;
		for(d = 0;d < depth;d++)
		{
			col = startx - d;
			for(row = starty - d;row >= endy - d;row--)
				pixelRaster[row][col] = pixel;
		}
		break;

	  case DLVERSTACKUPTPLFT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty - d;row <= endy - d;row++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSTACKUPBTLFT :
		endy = starty - length + 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty - d;row >= endy - d;row--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSTACKDOWNTPRHT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx - d;
			for(row = starty + d;row <= endy + d;row++)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLVERSTACKDOWNBTRHT :
		endy = starty - length + 1;
		for(d = 0;d < depth;d++)
		{
			col = startx - d;
			for(row = starty + d;row >= endy + d;row--)
				pixelRaster[row][col] = pixel;
		}
		break;
	  case DLHORLGSMTPLFTBUTT :
		endx = startx + length - 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx + d;col <= endx;col++)
				pixelRaster[row][col] = pixel;
		}
		break;

	  case DLHORLGSMTPRHTBUTT :
		endx = startx - length + 1;
		for(d = 0;d < depth;d++)
		{
			row = starty + d;
			for(col = startx - d;col >= endx;col--)
				pixelRaster[row][col] = pixel;
		}
		break;

	  case DLVERLGSMTPLFTBUTT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty + d;row <= endy;row++)
				pixelRaster[row][col] = pixel;
		}
		break;

	  case DLVERLGSMTPRHTBUTT :
		endy = starty + length - 1;
		for(d = 0;d < depth;d++)
		{
			col = startx - d;
			for(row = starty + d;row <= endy;row++)
				pixelRaster[row][col] = pixel;
		}
		break;

	  case DLVERLGSMBTLFTBUTT :
		endy = starty - length + 1;
		for(d = 0;d < depth;d++)
		{
			col = startx + d;
			for(row = starty - d;row >= endy;row--)
				pixelRaster[row][col] = pixel;
		}
		break;

	  default :
		break;

	}
}

static public Image
createNewImage(int width, int height, int fillPixel, Component observer)
{
	int[][] pixelRaster = new int[height][width];
	for (int row = 0;row < height;row++)
		for (int col = 0;col < width;col++)
			pixelRaster[row][col] = fillPixel;
	return (createMemoryImage(pixelRasterToPixelArray(
		pixelRaster, width, height), width, height, observer));
}

static public Image
createPixelRasterMemoryImage(int[][] pixelRaster, int width, int height,
	Component observer)
{
	return (createMemoryImage(pixelRasterToPixelArray(
		pixelRaster, width, height), width, height, observer));
}

static public Image
createMemoryImage(int[] pixelArray, int width, int height,
	Component observer)
{
	return (observer.createImage(
		new java.awt.image.MemoryImageSource(
			width, height, pixelArray, 0, width)));
}

static public Image
getInLineGIF(String imgString)
{
	// bug: need to speed up convertHexToByte
	byte gifBytes[] = MathOps.convertHexToByte(imgString);
	if (gifBytes == null)
	{
		return (null);
	}
	return (java.awt.Toolkit.getDefaultToolkit().createImage(gifBytes));
}

private static int	browserType		= -1;
private static String	javaVendor		= null;
private static final String MS_IE = "Microsoft";
private static final String NS_C = "Netscape";
private static final int IE = 0;	// MS Internet Explorer
private static final int NS = 1;	// Netscape Communicator
private static final int OTHER = 2;	// just appletviewer for now

private static int
getBrowserType()
{
	if (browserType < 0)
	{
		if (javaVendor == null)
			javaVendor = System.getProperty("java.vendor");

		// determine which java vendor we're running in...
		if ((javaVendor.indexOf(MS_IE)) >= 0)
			browserType = IE;	// Microsoft IE
		else if ((javaVendor.indexOf(NS_C)) >= 0)
			browserType = NS;	// assume Netscape Communicator
		else
			browserType = OTHER; // just appletviewer for now
	}

	return (browserType);
}

static public Image
getResourceImg(String imgPath, Component observer)
{
	InputStream sysIS = null;
	byte[] buffer = null;

	/*
	if (getBrowserType() == IE)
	{
	*/
		// URL imgURL = observer.getClass().getResource(imgPath);
		URL imgURL = null;
		try
		{
			imgURL = new URL("file", "D:/XRNA/BRAYS", "out.gif");
		}
		catch (MalformedURLException mue)
		{
			System.out.println("ERROR mue: " + mue.toString());
			return(null);
		}
		if (imgURL != null)
			return (observer.getToolkit().getImage(imgURL));
	/*
	}
	else if (getBrowserType() == NS)
	{
		// append path to image from CLASSPATH to imgPath
		imgPath = "com/livemedia/external/gui/" + imgPath;
		try
		{
			sysIS = ClassLoader.getSystemResourceAsStream(imgPath);
			if (sysIS != null)
			{
				buffer = new byte[sysIS.available()];
				sysIS.read(buffer);
				return(Toolkit.getDefaultToolkit().createImage(buffer));
			}
			else
			{
				//
				// If using Netscape for development, then images must be
				// in a jar file in CLASSPATH. Netscape has a problem with
				// the "getResource()" family of commands. This is well
				// documented in http://developer.netscape.com/
				//      docs/technote/java/getresource/getresource.html
				// Another alternative is to just use Internet Explorer for
				// development and test with Netscape when everything is in
				// a jar file.
				//

				System.err.println("Unable to read image at: " + imgPath);
			}
		}
		catch (java.io.IOException e)
		{
			System.err.println("Unable to read image at: " + imgPath);
			e.printStackTrace();
		}
	}
	*/
	return (null);
}

static public Image
setImageString(Image bgImg, int imgW, int imgH, String imgText,
	Font imgTextFont, Color textColor, int textX, int textY,
	Component observer)
{
	Image returnImg = observer.createImage(imgW, imgH);
	Graphics returnGraphics = returnImg.getGraphics();
	// returnGraphics.setColor(new Color(clearPixel));
	returnGraphics.drawImage(bgImg, 0, 0, observer);
	returnGraphics.setColor(textColor);
	returnGraphics.setFont(imgTextFont);
	returnGraphics.drawString(imgText, textX, textY);
	return (returnImg);
}

static public void
trackImage(Image trackImg, Component observer)
{
	MediaTracker tracker = new MediaTracker(observer);
	tracker.addImage(trackImg, 0);
	try
	{
		tracker.waitForID(0);
	}
	catch (InterruptedException ie)
	{
		System.err.println("in ImageOps.trackImage()," +
			" InterruptedException: " + ie.toString());
	}
}

/*
* fill an image with smaller image.
*/
static public void
fillImage(Image fillImg, Image withImg, Component obs)
{
	int fillImgW, fillImgH, withImgW, withImgH;
	Graphics fillG = fillImg.getGraphics();

	fillImgW = fillImg.getWidth(obs);
	fillImgH = fillImg.getHeight(obs);
	withImgW = withImg.getWidth(obs);
	withImgH = withImg.getHeight(obs);
	for(int i = 0;i <= fillImgW/withImgW;i++)
		for(int j = 0;j <= fillImgH/withImgH;j++)
			fillG.drawImage(withImg, i * withImgW, j * withImgH, obs);
}

static public Image
getFilteredImg(Image img, Rectangle rect, boolean brighter,
	int power, Component obs)
{
	ImageFilter cropfilter = null;

	if(rect == null)
		cropfilter = new CropImageFilter(
			0, 0, img.getWidth(obs), img.getHeight(obs));
	else
		cropfilter = new CropImageFilter(
				rect.x, rect.y, rect.width, rect.height);
	if(power == 0)
		return(obs.createImage(new FilteredImageSource(
			img.getSource(), cropfilter)));
	else
		return(obs.createImage(
			new FilteredImageSource(
				new FilteredImageSource(
					img.getSource(), cropfilter),
					new HighlightFilter(brighter, power))));
}

static public Image
getCroppedImg(Image img, Rectangle rect, Component obs)
{
	ImageFilter cropfilter = null;

	cropfilter = new CropImageFilter(
			rect.x, rect.y, rect.width, rect.height);
	return(obs.createImage(
		new FilteredImageSource(img.getSource(), cropfilter)));
}

static public void
drawPoint(Graphics g, int x, int y, Color col)
{
	g.setColor(col);
	g.drawLine(x, y, x, y);
}

static public void
drawOutline(Graphics g, Rectangle rect, Color col)
{
	g.setColor(col);
	g.drawLine(rect.x, rect.y, rect.x + rect.width, rect.y);
	g.drawLine(rect.x + rect.width, rect.y,
		rect.x + rect.width, rect.y + rect.height);
	g.drawLine(rect.x + rect.width, rect.y + rect.height,
		rect.x, rect.y + rect.height);
	g.drawLine(rect.x, rect.y + rect.height,
		rect.x, rect.y);
}

/*
Cannot use awt.image.PixelGrabber with offscreen images
If java.awt.image.PixelGrabber is used to fetch pixels from an
offs creen image, PixelGrabber.grabPixels() does not return.
Workaround: Copy the PixelGrabber source code from the sources.zip
src/ directory of the JDK distribution into the applet source code
directory, remove or change its package statement, make the
following code changes, and include the new class as one of their
applet's class.
Code Changes required to PixelGrabber.grabPixels(long ms):

if (!grabbing) {
   producer.startProduction(this);
   grabbing = true;
   flags &= ~(ImageObserver.ABORT);
}

To:

if (!grabbing)
{
   grabbing = true;
   flags &= ~(ImageObserver.ABORT);
   producer.startProduction(this);
}
*/

static public int[]
pixelRasterToPixelArray(int[][] pixelRaster, int imgW, int imgH)
{
	int[] pixelArray = new int[imgW * imgH];

	for (int row = 0;row < imgH;row++)
		for (int col = 0;col < imgW;col++)
			pixelArray[row * imgW + col] = pixelRaster[row][col];
	return(pixelArray);
}

static public int[]
setImagePixels(Image img, int w, int h)
{
	int pixelArray[] = new int[w * h];
	for(int trytimes = 1;trytimes <= 3;trytimes++)
	{
		if(!grabImagePixels(img, 0, 0, w, h, pixelArray))
		{
			continue;
		}
		else
		{
			break;
		}
	}
	return (pixelArray);
}

static public boolean
grabImagePixels(Image img, int x, int y, int w, int h, int[] raster)
{
	PixelGrabber bgPG = new PixelGrabber(img, x, y, w, h, raster, 0, w);
	try
	{
		bgPG.grabPixels();
	}
	catch (InterruptedException e)
	{
		System.err.println("interrupted waiting for raster!");
		return(false);
	}
	if ((bgPG.status() & ImageObserver.ABORT) != 0)
	{
		/* BUG: need to fix PixelGrabber with workaround */
		//System.err.println("image fetch aborted or errored");
		return(false);
	}
	return(true);
}

static public void
setRectangularHighLight(Image img, Rectangle rect, int depth, Component obs)
{
	int imgW = img.getWidth(obs);
	int imgH = img.getHeight(obs);
	int raster[] = new int[imgW * imgH];
	PixelGrabber pg = new PixelGrabber(img, 0, 0, imgW, imgH, raster, 0,imgW);
	try
	{
		pg.grabPixels();
	}
	catch (InterruptedException e)
	{
		System.err.println("interrupted waiting for raster!");
		return;
	}
	if ((pg.status() & ImageObserver.ABORT) != 0)
	{
		System.err.println("image fetch aborted or errored");
		return;
	}
	Graphics imgGraphics = img.getGraphics();
	drawBorderLines(imgGraphics,
		rect.x, rect.y, rect.width, rect.height, depth);
}

// BUG: does this really work?
static public int
pixelToTransparent(int pixel)
{
	return(pixel & 0x00ffffff);
}

static public int
pixelToComplement(int pixel)
{
	return (pixel & 0xff000000) |
		((0xff - ((pixel >> 16) & 0xff)) << 16) |
		((0xff - ((pixel >> 8) & 0xff)) << 8) |
		((0xff - ((pixel >> 0) & 0xff)) << 0);
}

static public int
colorValsToIntColor(int redVal, int greenVal, int blueVal)
{
	return (0xff000000 |
		(redVal << 16) | (greenVal << 8) | (blueVal << 0));
}

static public Color
colorValsToJColor(int redVal, int greenVal, int blueVal)
{
	/*
	return (new Color(0xff000000 |
		(redVal << 16) | (greenVal << 8) | (blueVal << 0)));
	*/
	return (new Color(colorValsToIntColor(redVal, greenVal, blueVal)));
}

static public Color
addToColor(Color col, Color addCol)
{
	return (new Color(addToColor(col.getRGB(), addCol.getRGB())));
}

static public int
addToColor(int pixel, int addPixel)
{
	return (addToColor(pixel,
		(addPixel >> 16) & 0xff,
		(addPixel >> 8) & 0xff,
		(addPixel >> 0) & 0xff));
}

static public int
addToColor(int pixel, int rAddVal, int gAddVal, int bAddVal)
{
	int rPixel = (pixel >> 16) & 0xff;
	int gPixel = (pixel >> 8) & 0xff;
	int bPixel = (pixel >> 0) & 0xff;
	int newRPixel = rPixel + rAddVal;
	if (newRPixel > 255)
		newRPixel = 255;
	else if (newRPixel < 0)
		newRPixel = 0;
	int newGPixel = gPixel + gAddVal;
	if (newGPixel > 255)
		newGPixel = 255;
	else if (newGPixel < 0)
		newGPixel = 0;
	int newBPixel = bPixel + bAddVal;
	if (newBPixel > 255)
		newBPixel = 255;
	else if (newBPixel < 0)
		newBPixel = 0;

	return ((0xff000000) |
		(newRPixel << 16) | (newGPixel << 8) | (newBPixel << 0));
}

static public Color
addToColor(Color col, int rAddVal, int gAddVal, int bAddVal)
{
	return (addToColor(col, new Color(rAddVal, gAddVal, bAddVal)));
}

static public int
pixelToBrightness(int pixel, boolean brighter, int percent)
{
	int r = (pixel >> 16) & 0xff;
	int g = (pixel >> 8) & 0xff;
	int b = (pixel >> 0) & 0xff;
	if (brighter)
	{
		r = (255 - ((255 - r) * (100 - percent) / 100));
		g = (255 - ((255 - g) * (100 - percent) / 100));
		b = (255 - ((255 - b) * (100 - percent) / 100));
	}
	else
	{
		r = (r * (100 - percent) / 100);
		g = (g * (100 - percent) / 100);
		b = (b * (100 - percent) / 100);
	}
	return (pixel & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
}

static public Color
getNewColorOnRGBLine(Color startColor, Color endColor, double tVal)
throws Exception
{
	BRay testRay = new BRay(
		(double)(startColor.getRed()),
		(double)(startColor.getGreen()),
		(double)(startColor.getBlue()),
		(double)(endColor.getRed()),
		(double)(endColor.getGreen()),
		(double)(endColor.getBlue()));
	BVector newPt = testRay.getPointAtT(tVal);

	return (colorValsToJColor(
		(int)Math.round(newPt.xCoor()),
		(int)Math.round(newPt.yCoor()),
		(int)Math.round(newPt.zCoor())));
}

// NOT FINISHED:
/*
static public int
getNewColorOnRGBLine(int startColor, int endColor, double tVal)
{
	BVector startPt = new BVector(
		(double)(startColor & 0x00ff0000),
		(double)(startColor & 0x00ff0000),
		(double)(startColor & 0x00ff0000));
	BVector endPt = new BVector(
		(double)(endColor.getRed()),
		(double)(endColor.getGreen()),
		(double)(endColor.getBlue()));
	BVector newPt = new BVector();

	MathOps.getVectorLineAtT(startPt, endPt, newPt, tVal);

	return (colorValsToJColor(
		(int)Math.round(newPt.xCoor()),
		(int)Math.round(newPt.yCoor()),
		(int)Math.round(newPt.zCoor())));
}
*/

static public double
colorDistanceTVal(Color startColor, Color ofColor, Color toColor,
	boolean printDebug)
throws Exception
{
	// if ((ofColor.getRGB() & 0x00ffffff))
		// return (1.0);
	// return ((ofColor.getRGB() & 0x00ffffff) /
		// (withColor.getRGB() & 0x00ffffff));

	/* NEED to replace with BRay
	BVector startPt = new BVector(
		(double)(startColor.getRed()),
		(double)(startColor.getGreen()),
		(double)(startColor.getBlue()));
	BVector toPt = new BVector(
		(double)(toColor.getRed()),
		(double)(toColor.getGreen()),
		(double)(toColor.getBlue()));
	BVector ofPt = new BVector(
		(double)(ofColor.getRed()),
		(double)(ofColor.getGreen()),
		(double)(ofColor.getBlue()));
	double[] tVal = new double[1];
	double dist = MathOps.distPtToLine(startPt, toPt, ofPt, tVal);

	if (printDebug)
	{
		System.out.println("TVAL: " + tVal[0] + " " + "DIST: " + dist);
		System.out.print("   startPt: " + startPt.toString());
		System.out.print("   toPt: " + toPt.toString());
		System.out.print("   ofPt: " + ofPt.toString());
	}

	return (tVal[0]);
	*/
	return (0.0);
}

static public void
drawBorderLines(Graphics g, int wx, int wy, int ww, int wh, int depth)
{
	int i, j, winx, winy, winwidth, winheight;
	boolean darkenBorder = false;

	winx = wx + depth;
	winy = wy + depth;
	ww -= 2*depth;
	wh -= 2*depth;

	if(depth < 0)
		depth = -depth;
	for(i=1,j=1;i <= depth;i++,j+=2)
	{
			winx--;
			winy--;
			winwidth = ww + j;
			winheight = wh + j;

			/*
			if(i > depth)
			{
				if(darkenBorder)
					g.setColor(Color.black);
				else
					g.setColor(Color.black);
			}
			else
			{
				g.setColor(topShadow);
			}
			*/
			/* get pixels for top shadow */
			g.drawLine(winx, winy, winx + winwidth, winy);
			g.drawLine(winx, winy, winx, winy + winheight);

			/*
			if(i > depth)
			{
				if(darkenBorder)
					g.setColor(Color.black);
				else
					g.setColor(Color.black);
			}
			else
			{
				g.setColor(bottomShadow);
			}
			*/
			/* get pixels for top shadow */
			g.drawLine(winx + winwidth, winy + 1, winx + winwidth,
				winy  + winheight);
			g.drawLine(winx, winy  + winheight, winx + winwidth,
				winy  + winheight);
	}
}

static public void
drawDepthLines(Graphics g, int wx, int wy, int ww, int wh, int depth,
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
					g.setColor(Color.black);
				else
					g.setColor(Color.black);
			}
			else
			{
				g.setColor(topShadow);
			}
			g.drawLine(winx, winy, winx + winwidth, winy);
			g.drawLine(winx, winy, winx, winy + winheight);

			if(i > depth)
			{
				if(darkenBorder)
					g.setColor(Color.black);
				else
					g.setColor(Color.black);
			}
			else
			{
				g.setColor(bottomShadow);
			}
			g.drawLine(winx + winwidth, winy + 1, winx + winwidth,
				winy  + winheight);
			g.drawLine(winx, winy  + winheight, winx + winwidth,
				winy  + winheight);
	}
}

static public int
bitMaskHeight(int[] bitmask)
{
	int scansize = bitmask[1];
	int entrycount = bitmask[2];
	int entryid, bitcount, totalbits = 0;

	for(entryid = 3;entryid < entrycount + 3;entryid++)
	{
		bitcount = bitmask[entryid] & 0x0000ff00;
		bitcount >>= 8;
		totalbits+=bitcount;
	}
	return(totalbits/scansize);
}

static public void
bitMaskToImageString(int[] bitmask, byte[][]pixeltypes,
	int startx, int starty, byte bgpixtype, byte fgpixtype)
{
	int scansize = bitmask[1];
	int entrycount = bitmask[2];
	int x, entryval, entryid, bitcount, bitval, i, totalbits = 0;
	x = 0;
	for(entryid = 3;entryid < entrycount + 3;entryid++)
	{
		entryval = bitmask[entryid];
		bitcount = entryval & 0x0000ff00;
		bitcount >>= 8;
		bitval = entryval & 0x000000ff;
		/*
		printf("%d %d %d\n", entryid, bitcount, bitval);
		*/
		for(i = 0;i < bitcount;i++)
		{
			if(bitval == 0xff)
			{
				pixeltypes[starty][startx + x] = fgpixtype;
				//printf("%c", '1');
				//System.out.print("1");
			}
			else if(bitval == 0x00)
			{
				//System.out.print("0");
			}
			totalbits++;
			x++;
			if(totalbits % scansize == 0)
			{
				starty++;
				x = 0;
				//System.out.println();
			}
		}
	}
}

static public void
revinttobin(int number, char[] bin, int place)
{
	int i, mask, binlength;
	//char[] tmp = new char[64];
 
	mask = 1;
	mask <<= place-1;
	for(i = place - 1;i >= 0;i--)
	{
		//tmp[i]=((number & mask)==0)?'0':'1';
		bin[i]=((number & mask)==0)?'0':'1';
		number <<= 1;
	}
	bin[place] = '\0';
	//tmp[place] = '\0';
	//strcpy(bin, tmp);
}

static boolean printMess = false;

static public void
xBitMaskToImageString(int imgW, int imgH, int entryCount,
	char[] bitMask, byte[][]pixeltypes,
	int startx, int starty, byte bgpixtype, byte fgpixtype)
{
	int x = 0, entryval, entryid, bitcount = 0, bitval, i,
		totalbits = 0;
	char[] bin = new char[9]; // 8 bits plus eol

	/*
	if(printMess)
	{
		System.out.println(imgW);
		System.out.println(imgH);
		System.out.println(entryCount);
	}
	*/
	for(entryid = 0;entryid < entryCount;entryid++)
	{
		revinttobin(bitMask[entryid] & 0x0000ff, bin, 8);
		for(i = 0;i < 8;i++)
		{
			//printf("%c", bin[i]);
			/*
			if(printMess)
				System.out.println(starty + " " + (startx + x));
			*/
			if(bin[i] == '1')
				pixeltypes[starty][startx + x] = fgpixtype;
			x++;
			if(++bitcount % imgW == 0)
			{
				//printf("\n");
				bitcount = 0;
				starty++;
				x = 0;
				break;
			}
		}

		/*
		entryval = bitmask[entryid];
		bitcount = entryval & 0x0000ff00;
		bitcount >>= 8;
		bitval = entryval & 0x000000ff;
		//printf("%d %d %d\n", entryid, bitcount, bitval);
		for(i = 0;i < bitcount;i++)
		{
			if(bitval == 0xff)
			{
				pixeltypes[starty][startx + x] = fgpixtype;
				//printf("%c", '1');
				//System.out.print("1");
			}
			else if(bitval == 0x00)
			{
				//System.out.print("0");
			}
			totalbits++;
			x++;
			if(totalbits % imgW == 0)
			{
				starty++;
				x = 0;
				//System.out.println();
			}
		}
		*/
	}
}

// make a pixel raster out of pixel array
static public int[][]
getPixelRasterFromPixelArray(int pixelArray[], int w, int h)
{
	int[][] pixelRaster = new int[h][w];
	// need to assert that pixelArray.length == pixelRaster.w*pixelRaster.h
	for (int row = 0;row < h;row++)
		for (int col = 0;col < w;col++)
			pixelRaster[row][col] = pixelArray[row*w + col];
	return (pixelRaster);
}

static public int[][]
getPixelRasterFromImg(Image img, int imgW, int imgH)
{
	return (getPixelRasterFromPixelArray(setImagePixels(img, imgW, imgH),
		imgW, imgH));
}

static public int[][]
getPixelRasterFromImgRegion(Image img, int imgW, int imgH,
	int startX, int startY, int newW, int newH)
{
	int[][] pixelRaster = getPixelRasterFromPixelArray(
		setImagePixels(img, imgW, imgH), imgW, imgH);
	return (getPixelRasterRegion(pixelRaster, imgW, imgH,
		startX, startY, newW, newH));
}

// make a pixel raster with pixel
static public int[][]
getPixelRasterFilled(int w, int h, int pixel)
{
	int[][] pixelRaster = new int[h][w];
	for (int row = 0;row < h;row++)
		for (int col = 0;col < w;col++)
			pixelRaster[row][col] = pixel;
	return (pixelRaster);
}

// return a pixel raster with vertical region subtracted out
static public int[][]
getPixelRasterSubtractVerticalRegion(int[][] pixelRaster, int w, int h,
	int startRegionX, int regionWidth)
{
	int endRegionX = startRegionX + regionWidth - 1;

	if (startRegionX >= endRegionX)
		return (pixelRaster);

	int subtractRegionWidth = endRegionX - startRegionX + 1;
	int newWidth = w - subtractRegionWidth;
	int[][] newPixelRaster = new int[h][newWidth];

	// copy first region directly
	for (int row = 0;row < h;row++)
		for (int col = 0;col < startRegionX;col++)
			newPixelRaster[row][col] = pixelRaster[row][col];

	for (int row = 0;row < h;row++)
		for (int col = endRegionX + 1;col < w;col++)
			newPixelRaster[row][col - subtractRegionWidth] =
				pixelRaster[row][col];

	return (newPixelRaster);
}

// return a pixel raster with vertical region subtracted out
static public int[][]
getPixelRasterRegion(int[][] pixelRaster, int imgW, int imgH,
	int startRegionX, int startRegionY, int regionW, int regionH)
{
	int[][] newPixelRaster = new int[regionH][regionW];

	// copy first region directly
	for (int row = startRegionY;row < startRegionY + regionH;row++)
	{
		if (row >= imgH)
			continue;
		for (int col = startRegionX;col < startRegionX + regionW;col++)
		{
			if (col >= imgW)
				continue;
			newPixelRaster[row - startRegionY][col - startRegionX] =
				pixelRaster[row][col];
		}
	}

	return (newPixelRaster);
}


// return a pixel raster with vertical region subtracted out
static public int[][]
getFilledPixelRaster(int imgW, int imgH, int bgPixel)
{
	int[][] pixelRaster = new int[imgH][imgW];
	fillPixelRaster(pixelRaster, 0, 0, imgW, imgH, bgPixel);
	return (pixelRaster);
}

// shift pixels in raster specified x, y
static public int[][]
getShiftedPixels(int[][] pixelRaster, int imgW, int imgH,
	int deltaX, int deltaY, int bgPixel, int shiftPixel)
{
	int[][] shiftedPixelRaster = getFilledPixelRaster(imgW, imgH, bgPixel);

	for (int row = 0;row < imgH;row++)
	{
		for (int col = 0;col < imgW;col++)
		{
			if ((row+deltaY >= imgH) || (col+deltaX >= imgW))
				continue;
			/*
			int orgPixel = pixelRaster[row][col];
			if (orgPixel == shiftPixel)
				shiftedPixelRaster[row+deltaY][col+deltaX] = orgPixel;
			*/
			shiftedPixelRaster[row+deltaY][col+deltaX] =
				pixelRaster[row][col];
		}
	}
	return (shiftedPixelRaster);
	/*
	for (int row = imgH - 1;row >= 0;row--)
	{
		for (int col = imgW - 1;col >= 0;col--)
		{
			int orgPixel = pixelRaster[row][col];
			pixelRaster[row][col] = pixel;
		}
	}
	*/
}

// fill a region of a pixel raster with given pixel
static public void
fillPixelRaster(int[][] pixelRaster, int startX, int startY,
	int w, int h, int pixel)
{
	for (int row = startY;row < h;row++)
		for (int col = startX;col < w;col++)
			pixelRaster[row][col] = pixel;
}

static public void
addPixelRasterToPixelRaster(int smallRaster[][], int largeRaster[][],
	int smallRasterW, int smallRasterH,
	int addX, int addY, int largeRasterW, int largeRasterH)
{
	for (int row = 0;row < smallRasterH;row++)
	{
		for (int col = 0;col < smallRasterW;col++)
		{
			if ((addY + row >= largeRasterH) ||
				(addX + col >= largeRasterW) ||
				(addY + row < 0) ||
				(addX + col < 0))
				continue;
			largeRaster[addY + row][addX + col] = smallRaster[row][col];
		}
	}
}

static public int[]
combinePixelRastersToPixelArray(int largeRaster[][],
	int smallRaster[][], int addX, int addY,
	int largeRasterW, int largeRasterH, int smallRasterW, int smallRasterH)
{
	int[] pixelArray = new int[largeRasterW * largeRasterH];

	addPixelRasterToPixelRaster(smallRaster, largeRaster,
		smallRasterW, smallRasterH, addX, addY, largeRasterW, largeRasterH);
	for (int row = 0;row < largeRasterH;row++)
		for (int col = 0;col < largeRasterW;col++)
			pixelArray[row * largeRasterW + col] = largeRaster[row][col];
	return (pixelArray);		
}

static public Image
pixelRasterToImage(int[][] pixelRaster)
{
	return ((Image)null);
}

static public Image
pixelArrayToImage(int[] pixelArray)
{
	return ((Image)null);
}

static public void
setPixelRasterBorder(int[][] pixelRaster, int pixel, int imgW, int imgH)
{
}

static public void
setPixelRasterGrid(int[][] pixelRaster, int imgW, int imgH, int gridCellSize, int pixel)
{
	for (int row = 0;row < imgH;row+=gridCellSize-1)
	{
		for (int column = 0;column < imgW;column+=gridCellSize-1)
		{
			pixelRaster[row][column] = pixel;
		}
	}
}

static public void
setPixelRasterGridCell(int[][] pixelRaster, int cellRow,
	int cellColumn, int gridCellSize, int pixel)
{
	int startRow = (gridCellSize - 1) * cellRow;
	int startColumn = (gridCellSize - 1) * cellColumn;

/*
System.out.println("DRAW, cellr, cellc: " + cellRow + " " + cellColumn);
*/
	for (int row = 0;row < gridCellSize;row++)
	{
		for (int column = 0;column < gridCellSize;column++)
		{
/*
System.out.println("DRAW: " + (startRow + row) + " " + (startColumn + column) + " " + gridCellSize);
*/
			pixelRaster[startRow + row][startColumn + column] = pixel;
		}
	}
}

public static void
setPixelRasterTest0Bresenham(int[][] pixelRaster, int cellRow,
	int cellColumn, int gridCellSize, int radius, int pixel,
	int imgW, int imgH)
{
	int centerX = cellColumn;
	int centerY = cellRow;
	int bresX = radius, bresY = 0, delta = 2*(1-radius), limit = 0;
 
	/*
	// draw line from center to right (0 degrees)
	for (int x = centerX;x < centerX + radius;x++)
		setPixelRasterGridCell(pixelRaster,
			centerY, x, gridCellSize, pixel);
	*/

	while(true)
	{
		// fill in lower-right quadrant
		setPixelRasterGridCell(pixelRaster,
			bresY + centerY, bresX + centerX, gridCellSize, pixel);
		/*
		System.out.println("angle: " + bresY + " " + bresX + " " + MathOps.angleInXYPlane((double)bresY, (double)bresX));
		*/

		// fill in upper-right quadrant
		setPixelRasterGridCell(pixelRaster,
			-bresX + centerY, bresY + centerX, gridCellSize, pixel);
		/*
		System.out.println("angle: " + -bresX + " " + bresY + " " + MathOps.angleInXYPlane((double)-bresX, (double)bresY));
		*/

		// fill in lower-left quadrant
		setPixelRasterGridCell(pixelRaster,
			bresX + centerY, -bresY + centerX, gridCellSize, pixel);
		/*
		System.out.println("angle: " + bresX + " " + -bresY + " " + MathOps.angleInXYPlane((double)bresX, (double)-bresY));
		*/
		// fill in upper-left quadrant
		setPixelRasterGridCell(pixelRaster,
			-bresX + centerY, -bresY + centerX, gridCellSize, pixel);
		/*
		System.out.println("angle: " + -bresX + " " + -bresY + " " + MathOps.angleInXYPlane((double)-bresX, (double)-bresY));
		*/
		
		if(bresX <= limit)
		{
			/*
			Point pt = MathOps.polarCoordToXY(radius, 45.0);
			setPixelRasterGridCell(pixelRaster,
				-pt.y + centerY, pt.x + centerX, gridCellSize,
				0xffff0000);
			pt = MathOps.polarCoordToXY(radius, 90.0);
			setPixelRasterGridCell(pixelRaster,
				-pt.y + centerY, pt.x + centerX, gridCellSize,
				0xffff0000);
			pt = MathOps.polarCoordToXY(radius, 180.0);
			setPixelRasterGridCell(pixelRaster,
				-pt.y + centerY, pt.x + centerX, gridCellSize,
				0xffff0000);
			pt = MathOps.polarCoordToXY(radius, 270.0);
			setPixelRasterGridCell(pixelRaster,
				-pt.y + centerY, pt.x + centerX, gridCellSize,
				0xffff0000);
			pt = MathOps.polarCoordToXY(radius, 360.0);
			setPixelRasterGridCell(pixelRaster,
				-pt.y + centerY, pt.x + centerX, gridCellSize,
				0xffff0000);
			pt = MathOps.polarCoordToXY(radius, 315.0);
			setPixelRasterGridCell(pixelRaster,
				-pt.y + centerY, pt.x + centerX, gridCellSize,
				0xff00ff00);
			pt = MathOps.polarCoordToXY(radius, 190.0);
			setPixelRasterGridCell(pixelRaster,
				-pt.y + centerY, pt.x + centerX, gridCellSize,
				0xff0000ff);
			pt = MathOps.polarCoordToXY(radius, 211.0);
			setPixelRasterGridCell(pixelRaster,
				-pt.y + centerY, pt.x + centerX, gridCellSize,
				0xff0000ff);
			*/
			break;
		}

		if(delta < 0)
		{
			if((2*delta + 2*bresX - 1) <= 0)
			{
				bresY++;
				delta += 2*bresY + 1;
			}
			else
			{
				bresY++;
				bresX--;
				delta += 2*bresY - 2*bresX + 2;
			}
		}
		else if(delta > 0)
		{
			if((2*delta - 2*bresY - 1) <= 0)
			{
				bresY++;
				bresX--;
				delta += 2*bresY - 2*bresX + 2;
			}
			else
			{
				bresX--;
				delta += -2*bresX + 1;
			}
		}
		else
		{
			bresY++;
			bresX--;
			delta += 2*bresY - 2*bresX + 2;
		}
		// System.out.println("delta: " + delta);
	}
}

public static void
setPixelRasterTest1Bresenham(int[][] pixelRaster, int cellRow,
	int cellColumn, int gridCellSize, int radius, int pixel,
	int imgW, int imgH)
{
	int centerX = cellColumn;
	int centerY = cellRow;
	int bresX = 0, bresY = radius, delta = 3 - 2*radius;
	int varyColorPixel = blackPixel;
	boolean firstLoop = true;
 
	while(bresX <= bresY)
	{
		// fill in upper-right-lower quadrant,row=-bresX,col=bresY
		setPixelRasterGridCell(pixelRaster,
			centerY - bresX, centerX + bresY, gridCellSize,
			varyColorPixel);
		System.out.println("angle URL: " + bresY + " " + bresX + " "
			+ MathOps.angleInXYPlane((double)bresY, (double)bresX));

		// fill in upper-right-upper quadrant, row=-bresY,col=bresX
		setPixelRasterGridCell(pixelRaster,
			centerY - bresY, centerX + bresX, gridCellSize,
			varyColorPixel);
		System.out.println("angle URU: " + bresX + " " + bresY + " "
			+ MathOps.angleInXYPlane((double)bresX, (double)bresY));

		if (!firstLoop)
		{
		// fill in upper-left-upper quadrant,row=-bresY,col=-bresX
		setPixelRasterGridCell(pixelRaster,
			centerY - bresY, centerX - bresX, gridCellSize,
			varyColorPixel);
		System.out.println("angle ULU: " + -bresX + " " + bresY + " "
			+ MathOps.angleInXYPlane((double)-bresX, (double)bresY));
		}

		// fill in upper-left-lower quadrant,row=-bresX,col=-bresY
		setPixelRasterGridCell(pixelRaster,
			centerY - bresX, centerX - bresY, gridCellSize,
			varyColorPixel);
		System.out.println("angle ULL: " + -bresY + " " + bresX + " "
			+ MathOps.angleInXYPlane((double)-bresY, (double)bresX));

		if (!firstLoop)
		{
		// fill in lower-left-upper quadrant, row=bresX,col=-bresY
		setPixelRasterGridCell(pixelRaster,
			centerY + bresX, centerX - bresY, gridCellSize,
			varyColorPixel);
		System.out.println("angle LLU: " + -bresY + " " + -bresX + " "
			+ MathOps.angleInXYPlane((double)-bresY, (double)-bresX));
		}

		// fill in lower-left-lower quadrant, row=bresY,col=-bresX
		setPixelRasterGridCell(pixelRaster,
			centerY + bresY, centerX - bresX, gridCellSize,
			varyColorPixel);
		System.out.println("angle LLL: " + -bresX + " " + -bresY + " "
			+ MathOps.angleInXYPlane((double)-bresX, (double)-bresY));

		if (!firstLoop)
		{
		// fill in lower-right-lower quadrant, row=bresY, col=bresX
		setPixelRasterGridCell(pixelRaster,
			centerY + bresY, centerX + bresX, gridCellSize,
			varyColorPixel);
		System.out.println("angle LRL: " + bresX + " " + -bresY + " "
			+ MathOps.angleInXYPlane((double)bresX, (double)-bresY));

		// fill in lower-right-upper quadrant, row=bresX, col=bresY
		setPixelRasterGridCell(pixelRaster,
			centerY + bresX, centerX + bresY, gridCellSize,
			varyColorPixel);
		System.out.println("angle LRU: " + bresY + " " + -bresX + " "
			+ MathOps.angleInXYPlane((double)bresY, (double)-bresX));
		}
		
		varyColorPixel += 0x00111111;
		firstLoop = false;

		if(delta < 0)
		{
			delta += 4*bresX + 6;
			bresX++;
		}
		else
		{
			delta += 4*(bresX - bresY) + 10;
			bresX++;
			bresY--;
		}
	}
}

public static void
setPixelRasterTest2Bresenham(int[][] pixelRaster, int cellRow,
	int cellColumn, int gridCellSize, int radius, int pixel,
	int imgW, int imgH)
{
	int centerX = cellColumn;
	int centerY = cellRow;
	double angleInc = 10.0;
 
	for (double angle = 0.0; angle < 360.0;angle += angleInc)
	{
		Point pt = getBresCirclePtAtAngle(centerX, centerY, radius,
			angle);
		setPixelRasterGridCell(pixelRaster, pt.y, pt.x, gridCellSize,
			pixel);
	}
}

public static Point
getBresCirclePtAtAngle(int centerX, int centerY, int radius, double angle)
{
	int bresX = 0, bresY = radius, delta = 3 - 2*radius;
	boolean firstLoop = true;
	double currBestAngle = 1000.0, testAngle = 0.0;
	Point currBestPt = new Point(0, 0);
 
	while(bresX <= bresY)
	{
		// fill in upper-right-lower quadrant,row=-bresX,col=bresY
		testAngle = MathOps.angleInXYPlane((double)bresY, (double)bresX);
		if (Math.abs(testAngle - angle) < Math.abs(currBestAngle - angle))
		{
			currBestAngle = testAngle;
			currBestPt.setLocation(centerX + bresY, centerY + bresX);
		}

		// fill in upper-right-upper quadrant, row=-bresY,col=bresX
		testAngle = MathOps.angleInXYPlane((double)bresX, (double)bresY);
		if (Math.abs(testAngle - angle) < Math.abs(currBestAngle - angle))
		{
			currBestAngle = testAngle;
			currBestPt.setLocation(centerX + bresX, centerY + bresY);
		}

		if (!firstLoop)
		{
			// fill in upper-left-upper quadrant,row=-bresY,col=-bresX
			testAngle = MathOps.angleInXYPlane((double)-bresX, (double)bresY);
			if (Math.abs(testAngle - angle) < Math.abs(currBestAngle - angle))
			{
				currBestAngle = testAngle;
				currBestPt.setLocation(centerX + -bresX, centerY + bresY);
			}
		}

		// fill in upper-left-lower quadrant,row=-bresX,col=-bresY
		testAngle = MathOps.angleInXYPlane((double)-bresY, (double)bresX);
		if (Math.abs(testAngle - angle) < Math.abs(currBestAngle - angle))
		{
			currBestAngle = testAngle;
			currBestPt.setLocation(centerX + -bresY, centerY + bresX);
		}

		if (!firstLoop)
		{
			// fill in lower-left-upper quadrant, row=bresX,col=-bresY
			testAngle = MathOps.angleInXYPlane((double)-bresY, (double)-bresX);
			if (Math.abs(testAngle - angle) < Math.abs(currBestAngle - angle))
			{
				currBestAngle = testAngle;
				currBestPt.setLocation(centerX + -bresY, centerY + -bresX);
			}
		}

		// fill in lower-left-lower quadrant, row=bresY,col=-bresX
		testAngle = MathOps.angleInXYPlane((double)-bresX, (double)-bresY);
		if (Math.abs(testAngle - angle) < Math.abs(currBestAngle - angle))
		{
			currBestAngle = testAngle;
			currBestPt.setLocation(centerX + -bresX, centerY + -bresY);
		}

		if (!firstLoop)
		{
			// fill in lower-right-lower quadrant, row=bresY, col=bresX
			testAngle = MathOps.angleInXYPlane((double)bresX, (double)-bresY);
			if (Math.abs(testAngle - angle) < Math.abs(currBestAngle - angle))
			{
				currBestAngle = testAngle;
				currBestPt.setLocation(centerX + bresX, centerY + -bresY);
			}

			// fill in lower-right-upper quadrant, row=bresX, col=bresY
			testAngle = MathOps.angleInXYPlane((double)bresY, (double)-bresX);
			if (Math.abs(testAngle - angle) < Math.abs(currBestAngle - angle))
			{
				currBestAngle = testAngle;
				currBestPt.setLocation(centerX + bresY, centerY + -bresX);
			}
		}
		firstLoop = false;

		if(delta < 0)
		{
			delta += 4*bresX + 6;
			bresX++;
		}
		else
		{
			delta += 4*(bresX - bresY) + 10;
			bresX++;
			bresY--;
		}
	}
	return (currBestPt);
}

public static void
setPixelRasterTestArc(int[][] pixelRaster, int cellRow,
	int cellColumn, int gridCellSize, double radius, int pixel,
	int imgW, int imgH)
throws Exception
{
	int centerX = cellColumn;
	int centerY = cellRow;
	double angleInc = 10.0;
 
	for (double angle = 0.0; angle < 360.0;angle += angleInc)
	{
		// need to replace with more current version
		// Point pt = MathOps.polarCoordToXY(radius, angle);
		int x = (int)Math.round(MathOps.polarCoordToX(radius, angle));
		int y = (int)Math.round(MathOps.polarCoordToY(radius, angle));
		setPixelRasterGridCell(pixelRaster,
			// -pt.y + centerY, pt.x + centerX, gridCellSize,
			-y + centerY, x + centerX, gridCellSize,
			pixel);
		//
	}
}


// draw a circle into image raster with pixel
static public void
setPixelRasterCircle(int[][] pixelRaster, int imgW, int imgH,
	int centerX, int centerY, int radius, int pixel)
{
	int quarterindex[] = new int[1]; // 7 ??
	Vector result = new Vector();
	int status = MathOps.getBresenhamCircle(
		centerX, centerY, radius, quarterindex, result);
	for (int i = 0;i < result.size();i++)
	{
		Point pt = (Point)result.elementAt(i);
		if (((pt.y < 0) || (pt.y >= imgH)) ||
			((pt.x < 0) || (pt.x >= imgW)))
			continue;
		pixelRaster[pt.y][pt.x] = pixel;
	}
}

// draw a arc into image raster with pixel
// NEED to provide true notion of arc, i.e., using radians.
static public void
setPixelRasterArc(int[][] pixelRaster, int imgW, int imgH,
	int centerX, int centerY,
	int startIndex, int endIndex,
	int radius, int fgPixel)
{
	int quarterindex[] = new int[1]; // 7 ??
	Vector result = new Vector();
	int status = MathOps.getBresenhamCircle(
		centerX, centerY, radius, quarterindex, result);
	/*
	if (endIndex > result.size())
		endIndex = result.size();
	*/
	for (int i = startIndex;i < result.size() - endIndex;i++)
	{
		Point pt = (Point)result.elementAt(i);
		if (((pt.y < 0) || (pt.y >= imgH)) ||
			((pt.x < 0) || (pt.x >= imgW)))
			continue;
		pixelRaster[pt.y][pt.x] = fgPixel;
	}
}

// draw a line into image raster with pixel
static public void
setPixelRasterLine(int[][] pixelRaster, int startX, int startY,
	int endX, int endY, int pixel, int imgW, int imgH)
{
	Vector result = new Vector();
	// getBresenhamLine(int x1, int y1, int x2, int y2, Vector result)
	MathOps.getBresenhamLine(startX, startY, endX, endY, result);
	for (int i = 0;i < result.size();i++)
	{
		Point pt = (Point)result.elementAt(i);
		if (((pt.y < 0) || (pt.y >= imgH)) ||
			((pt.x < 0) || (pt.x >= imgW)))
			continue;
		pixelRaster[pt.y][pt.x] = pixel;
	}
}

// draw a rectangle into image raster with pixel
static public void
setCartesianCoordinateSystem(int[][] pixelRaster, int cX, int cY, int length,
	int pixel, int imgW, int imgH)
{
	setPixelRasterLine(pixelRaster,
		cX, cY, cX + length, cY, pixel, imgW, imgH);
}

// draw a rectangle into image raster with pixel
static public void
setPixelRasterRectangle(int[][] pixelRaster, Rectangle rect, int pixel,
	int imgW, int imgH)
{
	setPixelRasterLine(pixelRaster,
		rect.x, rect.y, rect.x + rect.width, rect.y,
		pixel, imgW, imgH);
	setPixelRasterLine(pixelRaster,
		rect.x + rect.width, rect.y, rect.x + rect.width,
			rect.y + rect.height,
		pixel, imgW, imgH);
	setPixelRasterLine(pixelRaster,
		rect.x + rect.width, rect.y + rect.height,
			rect.x, rect.y + rect.height,
		pixel, imgW, imgH);
	setPixelRasterLine(pixelRaster,
		rect.x, rect.y + rect.height, rect.x, rect.y,
		pixel, imgW, imgH);
}

// draw a line into image raster with complement of current pixel
static public void
setPixelRasterLine(int[][] pixelRaster, int startX, int startY,
	int endX, int endY, int imgW, int imgH)
{
	Vector result = new Vector();
	// getBresenhamLine(int x1, int y1, int x2, int y2, Vector result)
	MathOps.getBresenhamLine(startX, startY, endX, endY, result);
	for (int i = 0;i < result.size();i++)
	{
		Point pt = (Point)result.elementAt(i);
		if (((pt.y < 0) || (pt.y >= imgH)) ||
			((pt.x < 0) || (pt.x >= imgW)))
			continue;
		pixelRaster[pt.y][pt.x] =
			pixelToComplement(pixelRaster[pt.y][pt.x]);
	}
}

// draw a rectangle into image raster with complement of current pixel
static public void
setPixelRasterRectangle(int[][] pixelRaster, Rectangle rect,
	int imgW, int imgH)
{
	setPixelRasterLine(pixelRaster,
		rect.x, rect.y, rect.x + rect.width, rect.y, imgW, imgH);
	setPixelRasterLine(pixelRaster,
		rect.x + rect.width, rect.y, rect.x + rect.width,
			rect.y + rect.height, imgW, imgH);
	setPixelRasterLine(pixelRaster,
		rect.x + rect.width, rect.y + rect.height,
			rect.x, rect.y + rect.height, imgW, imgH);
	setPixelRasterLine(pixelRaster,
		rect.x, rect.y + rect.height, rect.x, rect.y, imgW, imgH);
}

// draw a line into image raster with pixel
static public void
setPixelRaster3DLine(int[][] pixelRaster, BVector ray,
	int pixel, int imgW, int imgH)
{
	Color fgColor = new Color(pixel);
}

// need to create an image, fill with complement of textColor,
// grab new pixel positions upon drawing in string and send back
static public void
setPixelRasterString(int[][] pixelRaster, int imgW, int imgH,
	String imgText, Font imgTextFont, Color textColor,
	int textX, int textY, Component observer)
{
	Image returnImg = observer.createImage(imgW, imgH);
	int textPixel = textColor.getRGB();
	Graphics returnGraphics = returnImg.getGraphics();

	returnGraphics.setColor(new Color(pixelToComplement(textPixel)));
	// returnGraphics.setColor(Color.black);
	returnGraphics.fillRect(0, 0, imgW, imgH);
	returnGraphics.setColor(textColor);
	returnGraphics.setFont(imgTextFont);
	returnGraphics.drawString(imgText, textX, textY);

	int[][] returnPixelRaster = getPixelRasterFromPixelArray(
		setImagePixels(returnImg, imgW, imgH), imgW, imgH);

	for (int row = 0;row < imgH;row++)
	{
		for (int col = 0;col < imgW;col++)
		{
			if (returnPixelRaster[row][col] == textPixel)
			/*
			if (returnPixelRaster[row][col] != 0xff000000)
			*/
				pixelRaster[row][col] = textPixel;
		}
	}

}

// sets all pixels in range to toPixel if its not excludePixel
static public void
setPixelRasterExclusivePixel(int[][] pixelRaster, int x, int y,
	int w, int h, int excludePixel, int toPixel)
{
	for (int row = y;row < h;row++)
	{
		for (int col = x;col < w;col++)
		{
			if (pixelRaster[row][col] != excludePixel)
				pixelRaster[row][col] = toPixel;
		}
	}
}

static public void
setPixelRasterPixelToNewPixel(int[][] pixelRaster,
	int startCol, int startRow, int width, int height,
	int fromPixel, int toPixel)
{
	for (int row = startRow;row < height;row++)
	{
		for (int col = startCol;col < width;col++)
		{
			if (pixelRaster[row][col] == fromPixel)
				pixelRaster[row][col] = toPixel;
		}
	}
}

static public void
setPixelRasterPixelToNewPixel(int[][] pixelRaster,
	int startCol, int startRow, int width, int height,
	Color fromColor, Color toColor)
{
	int fromPixel = fromColor.getRGB();
	int toPixel = toColor.getRGB();

	for (int row = startRow;row < height;row++)
	{
		for (int col = startCol;col < width;col++)
		{
			if (pixelRaster[row][col] == fromPixel)
				pixelRaster[row][col] = toPixel;
		}
	}
}

static public void
setPixelRasterPixels(int[][] pixelRaster, int x, int y,
	int w, int h, int toPixel)
{
	for (int row = y;row < h;row++)
		for (int col = x;col < w;col++)
			setPixelRasterPixel(pixelRaster, row, col, toPixel);
}

static public void
setPixelRasterPixel(int[][] pixelRaster, int row, int col, int toPixel)
{
	pixelRaster[row][col] = toPixel;
}

/* an attempt to add Raster
static public void
setPixelRasterPixel(Raster pixelRaster, int row, int col, int toPixel)
{
	pixelRaster[row][col] = toPixel;
}
*/

static public void
setPixelRasterToShowFonts(int[][] pixelRaster, String showText,
	int bgPixel, int fgPixel, int startX, int startY, int xJump, int yJump,
	int fontSize, int imgW, int imgH, Component observer)
{
	/* USAGE:
	setPixelRasterToShowFonts(pixelRaster, "C",
		whitePixel, blackPixel, 14, 40, 20, 20, 14, imgW, imgH, observer);
	*/

	fillPixelRaster(pixelRaster, 0, 0, imgW, imgH, bgPixel);

	Font showTextFont = null;
	Color fgColor = new Color(fgPixel);

	showTextFont = new Font("Helvetica", Font.PLAIN, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (0 * xJump), startY + (0 * yJump), observer);

	showTextFont = new Font("Helvetica", Font.BOLD, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (1 * xJump), startY + (0 * yJump), observer);

	showTextFont = new Font("Dialog", Font.PLAIN, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (2 * xJump), startY + (0 * yJump), observer);

	showTextFont = new Font("Dialog", Font.BOLD, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (3 * xJump), startY + (0 * yJump), observer);

	showTextFont = new Font("DialogInput", Font.PLAIN, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (4 * xJump), startY + (0 * yJump), observer);

	showTextFont = new Font("DialogInput", Font.BOLD, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (5 * xJump), startY + (0 * yJump), observer);

	showTextFont = new Font("Monospaced", Font.PLAIN, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (0 * xJump), startY + (1 * yJump), observer);

	showTextFont = new Font("Monospaced", Font.BOLD, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (1 * xJump), startY + (1 * yJump), observer);

	showTextFont = new Font("Serif", Font.PLAIN, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (2 * xJump), startY + (1 * yJump), observer);

	showTextFont = new Font("Serif", Font.BOLD, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (3 * xJump), startY + (1 * yJump), observer);

	showTextFont = new Font("SansSerif", Font.PLAIN, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (4 * xJump), startY + (1 * yJump), observer);

	showTextFont = new Font("SansSerif", Font.BOLD, fontSize);
	setPixelRasterString(pixelRaster, imgW, imgH, showText,
		showTextFont, fgColor,
		startX + (5 * xJump), startY + (1 * yJump), observer);
}


static public Vector
listImgColors(int[][] pixelRaster, int imgW, int imgH, boolean print)
{
	Vector colorList = new Vector();
	int[] colorCount = new int[256];
	for (int i = 0;i < 256;i++)
		colorCount[i] = 0;

	for (int row = 0;row < imgH;row++)
	{
		for (int col = 0;col < imgW;col++)
		{
			/*
			if (pixelRaster[row][col] == clearPixel)
				continue;
			*/
			Color listColor = new Color(pixelRaster[row][col]);
			if (colorList.contains(listColor))
			{
				colorCount[colorList.indexOf(listColor)]++;
				continue;
			}
			colorList.addElement(listColor);
			colorCount[colorList.indexOf(listColor)]++;
			
			if (!print)
				continue;
			
			/*
			System.out.println(
				listColor.toString() + " " +
				Integer.toHexString(pixelRaster[row][col])
				//
				listColor.toString() + " " +
				// " " + pixelRaster[row][col] + " " +
				Integer.toHexString(pixelRaster[row][col]) + " " + 
				// listColor.getRed() + " " +
				// listColor.getGreen() + " " +
				// listColor.getBlue() + " " +
				// listColor.getRGB() + " " +
				(listColor.getRGB() & 0x00ffffff) + " " +
				colorDistanceTVal(newLiveColor, listColor, Color.white,
					true)
				//
				);
			System.out.println();
			*/
		}
	}

	if (print)
	{
		for (int i = 0;i < colorList.size();i++)
		{
			Color printColor = (Color)colorList.elementAt(i);
			System.out.println(
				printColor.toString() + " " +
				Integer.toHexString(printColor.getRGB()) + " " +
				colorCount[i]
			);
		}
	}

	return (colorList);
}

}
