
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

import jmath.MathOps;
import jmath.BVector;

public class
ImageApps
{

static public Image
makeTestBresenhamImage(Component observer)
throws Exception
{
	int imgH = 200;
	int imgW = 580;
	// int imgW = 200;

	int[][] pixelRaster = new int[imgH][imgW];
	ImageOps.fillPixelRaster(pixelRaster, 0, 0, imgW, imgH, ImageOps.whitePixel);

	ImageOps.setPixelRasterGrid(pixelRaster, imgW, imgH, 6, ImageOps.blackPixel);
	int gridCellSize = 6;
	/*
	ImageOps.setPixelRasterTestBresenham(pixelRaster,
		(imgH/2/(gridCellSize-1)), (imgW/2/(gridCellSize-1)),
		gridCellSize, 16, ImageOps.blackPixel, imgW, imgH);
	*/
	/*
	ImageOps.setPixelRasterTest0Bresenham(pixelRaster,
		(imgH/2/(gridCellSize-1)), (imgW/3/(gridCellSize-1)),
		gridCellSize, 16, ImageOps.blackPixel, imgW, imgH);
	ImageOps.setPixelRasterTest1Bresenham(pixelRaster,
		(imgH/2/(gridCellSize-1)), (2*imgW/3/(gridCellSize-1)),
		gridCellSize, 16, ImageOps.blackPixel, imgW, imgH);
	*/
	/*
	ImageOps.setPixelRasterTest1Bresenham(pixelRaster,
		(imgH/2/(gridCellSize-1)), (imgW/2/(gridCellSize-1)),
		gridCellSize, 16, ImageOps.blackPixel, imgW, imgH);
	*/

	ImageOps.setPixelRasterTest2Bresenham(pixelRaster,
		(imgH/2/(gridCellSize-1)), (imgW/3/(gridCellSize-1)),
		gridCellSize, 16, 0xffff0000, imgW, imgH);

	ImageOps.setPixelRasterTestArc(pixelRaster,
		(imgH/2/(gridCellSize-1)), (2*imgW/3/(gridCellSize-1)),
		gridCellSize, 16, 0xff00ff00, imgW, imgH);

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
}

static public Image
makeShiftedPixelsRayTrace(Image editImg, int bgPixel, int shiftPixel,
	Component observer)
{
	int imgW = editImg.getWidth(observer);
	int imgH = editImg.getHeight(observer);
	int[][] pixelRaster = ImageOps.getPixelRasterFromImg(editImg, imgW, imgH);

	int[][] newPixelRaster = ImageOps.getShiftedPixels(pixelRaster, imgW, imgH,
		1, 1, bgPixel, shiftPixel);

	return (ImageOps.createPixelRasterMemoryImage(
		newPixelRaster, imgW, imgH, observer));
}

static public Image
makeImgEditImage(Image editImg, int fromPixel, int toPixel, Component observer)
{
	int imgW = editImg.getWidth(observer);
	int imgH = editImg.getHeight(observer);
	int[][] pixelRaster = ImageOps.getPixelRasterFromImg(editImg, imgW, imgH);

	/*
	Vector colorList = listImgColors(pixelRaster, imgW, imgH, true);
	*/

	/*
	ImageOps.setPixelRasterPixelToNewPixel(pixelRaster,
		0, 0, imgW, imgH, 0xffdce6f6, ImageOps.clearPixel);
	*/
	/*
	ImageOps.setPixelRasterPixelToNewPixel(pixelRaster,
		0, 0, imgW, imgH, 0xff1b1b8d, ImageOps.clearPixel);
	*/
	ImageOps.setPixelRasterPixelToNewPixel(pixelRaster,
		0, 0, imgW, imgH, fromPixel, toPixel);

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
}

static public Image
makeEditEuroImage(Image editImg, int fgPixel, int bgPixel,
	Component observer)
{
	int imgW = editImg.getWidth(observer);
	int imgH = editImg.getHeight(observer);
	int[][] pixelRaster = ImageOps.getPixelRasterFromImg(editImg, imgW, imgH);
	int euroFGPixel = 0xfffff3d3;
	int euroBGPixel = 0xffdde6f5;

	ImageOps.setPixelRasterPixelToNewPixel(pixelRaster,
		0, 0, imgW, imgH, euroFGPixel, fgPixel);
	ImageOps.setPixelRasterPixelToNewPixel(pixelRaster,
		0, 0, imgW, imgH, 0xffdde6f5, bgPixel);

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));

	/*
	for (int row = 0;row < imgH;row++)
	{
		for (int col = 0;col < 118;col++)
		{
			int pixel = pixelRaster[row][col];
			if (pixel != ImageOps.clearPixel)
				pixelRaster[row][col] = ImageOps.addToColor(pixel,
					addLiveRed, addLiveGreen, addLiveBlue);
		}
	}
	*/
}

static public Image
makeCropImage(Image startImg, int imgW, int imgH, int startX, int startY,
	int newWidth, int newHeight, Component observer)
{
	int[][] pixelRaster = ImageOps.getPixelRasterFromImg(startImg, imgW, imgH);

	int[][] newPixelRaster = ImageOps.getPixelRasterRegion(pixelRaster, imgW, imgH,
		startX, startY, newWidth, newHeight);

	ImageOps.setPixelRasterRectangle(newPixelRaster,
		new Rectangle(0, 0, newWidth - 1, newHeight - 1),
		ImageOps.whitePixel, newWidth, newHeight);

	return (ImageOps.createPixelRasterMemoryImage(
		newPixelRaster, newWidth, newHeight, observer));
}

static public Image
makeCircularEuroImage(int imgW, int imgH, int radius,
	int startArc, int endArc, int shiftCenterX, int shiftCenterY,
	int lineShiftX,
	int bgPixel, int fgPixel, int depth, Component observer)
{
	int[][] pixelRaster = ImageOps.getFilledPixelRaster(imgW, imgH, bgPixel);
	int centerX = imgW/2 + shiftCenterX;
	int centerY = imgH/2 + shiftCenterY;

	ImageOps.setPixelRasterArc(pixelRaster, imgW, imgH, centerX, centerY,
		startArc, endArc, radius, fgPixel);

	// for 12x11
	// down left side
	ImageOps.setPixelRasterPixel(pixelRaster, 3, 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 5, 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 7, 3, fgPixel);
	// up to right
	ImageOps.setPixelRasterPixel(pixelRaster, 2, 4, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 1, 5, fgPixel);
	// down to right
	ImageOps.setPixelRasterPixel(pixelRaster, 8, 4, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 9, 5, fgPixel);

	/* for 13x13
	// down left side
	ImageOps.setPixelRasterPixel(pixelRaster, 4, 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 6, 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 8, 3, fgPixel);

	// up to right
	ImageOps.setPixelRasterPixel(pixelRaster, 3, 2, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 2, 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 1, 4, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 0, 5, fgPixel);

	// down to right
	ImageOps.setPixelRasterPixel(pixelRaster, 9, 2, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 10, 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 11, 4, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 12, 5, fgPixel);
	*/

	/* for 12x12
	// down left side
	ImageOps.setPixelRasterPixel(pixelRaster, 3, 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 5, 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 7, 3, fgPixel);

	// up to right
	ImageOps.setPixelRasterPixel(pixelRaster, 2, 4, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 1, 5, fgPixel);

	// down to right
	ImageOps.setPixelRasterPixel(pixelRaster, 8, 4, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 9, 5, fgPixel);
	*/

	ImageOps.setPixelRasterLine(pixelRaster,
		0,
		centerY - 1,
		centerX + radius/2 - lineShiftX,
		centerY - 1,
		fgPixel, imgW, imgH);

	ImageOps.setPixelRasterLine(pixelRaster,
		0,
		centerY + 1,
		centerX + radius/2 - 1 - lineShiftX,
		centerY + 1,
		fgPixel, imgW, imgH);

	/*
	int fade05Pixel = ImageOps.getNewColorOnRGBLine(
		Color.white, new Color(0xff333399), 0.5).getRGB();
	int fade04Pixel = ImageOps.getNewColorOnRGBLine(
		Color.white, new Color(0xff333399), 0.4).getRGB();
	*/

	/* for yellow
	int fade05Pixel = ImageOps.getNewColorOnRGBLine(
		new Color(fgPixel), new Color(bgPixel), 0.5).getRGB();
	int fade04Pixel = ImageOps.getNewColorOnRGBLine(
		new Color(fgPixel), new Color(bgPixel), 0.4).getRGB();
	*/

	/* for 13x13
	// for black
	int fade05Pixel = ImageOps.getNewColorOnRGBLine(
		new Color(fgPixel), new Color(bgPixel), 0.7).getRGB();
	int fade04Pixel = ImageOps.getNewColorOnRGBLine(
		new Color(fgPixel), new Color(bgPixel), 0.6).getRGB();

	ImageOps.setPixelRasterPixel(pixelRaster, 0, 5, fade05Pixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 0, 10, fade04Pixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 12, 5, fade05Pixel);
	ImageOps.setPixelRasterPixel(pixelRaster, 12, 10, fade04Pixel);
	*/

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
}

static public Image
makeEuroImage(int imgW, int imgH, int startTextX, int startTextY,
	int topLineY,
	int topLineXLftShift, int topLineXRhtShift,
	int btmLineXLftShift, int btmLineXRhtShift,
	int topLineYShift, int fontStyle, int fontSize,
	int bgPixel, Color fgColor, Component observer)
{
	int[][] pixelRaster = new int[imgH][imgW];
	ImageOps.fillPixelRaster(pixelRaster, 0, 0, imgW, imgH, bgPixel);
	Font euroTextFont = new Font("Dialog", fontStyle, fontSize);

	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, "C",
		euroTextFont, fgColor, startTextX, startTextY, observer);

	ImageOps.setPixelRasterLine(pixelRaster,
		startTextX + topLineXLftShift,
		topLineY,
		startTextX + topLineXRhtShift,
		topLineY,
		fgColor.getRGB(), imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster,
		startTextX + btmLineXLftShift,
		topLineY + topLineYShift,
		startTextX + btmLineXRhtShift,
		topLineY + topLineYShift,
		fgColor.getRGB(), imgW, imgH);

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
}

/*
static public BufferedImage
*/
static public Image
makeTextButtonImage(int imgW, int imgH, String btText,
	int startTextX, int startTextY, int shiftX, Font btTextFont,
	int fgPixel, int bgPixel, int topPixel, int bottomPixel,
	int depth, Component observer)
{
	int[][] pixelRaster = ImageOps.getFilledPixelRaster(imgW, imgH, bgPixel);

	/* for shadow effect
	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, btText,
		btTextFont, Color.black, startTextX + shiftX + 1, startTextY + 1,
		observer);
	*/

	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, btText,
		btTextFont, new Color(fgPixel), startTextX + shiftX, startTextY,
		observer);

	/*
	// for euro image:
	int radius = 6;
	int centerX = imgW - 32;
	int centerY = imgH/2;
	int startX = centerX - radius/2 - 5;
	int startY = centerY - 6;
	ImageOps.setPixelRasterArc(pixelRaster, imgW, imgH, centerX, centerY,
		4, 4, radius, fgPixel);

	// down left side
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 4, startX + 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 6, startX + 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 8, startX + 3, fgPixel);

	// up to right
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 3, startX + 2, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 2, startX + 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 1, startX + 4, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 0, startX + 5, fgPixel);

	// down to right
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 9, startX + 2, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 10, startX + 3, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 11, startX + 4, fgPixel);
	ImageOps.setPixelRasterPixel(pixelRaster, startY + 12, startX + 5, fgPixel);

	int lineShiftX = 7;
	ImageOps.setPixelRasterLine(pixelRaster,
		startX,
		centerY - 1,
		startX + radius/2 + lineShiftX,
		centerY - 1,
		fgPixel, imgW, imgH);

	ImageOps.setPixelRasterLine(pixelRaster,
		startX,
		centerY + 1,
		startX + radius/2 - 1 + lineShiftX,
		centerY + 1,
		fgPixel, imgW, imgH);
	*/

	ImageOps.setDepthRectangle(pixelRaster, new Rectangle(0, 0, imgW, imgH),
		depth, topPixel, bottomPixel);

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
	
	/*
	BufferedImage bufImg = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);
	Raster raster = new Raster(SampleModel sampleModel,
		 DataBuffer dataBuffer, new Point());
	bufImg.setData(raster);
	return (bufImg);
	*/
}

static public Image
makeEuroButtonImage(int imgW, int imgH, String btText,
	int startTextX, int startTextY,
	int topLineY,
	int topLineXLftShift, int topLineXRhtShift,
	int btmLineXLftShift, int btmLineXRhtShift,
	int topLineYShift, int fontStyle, int fontSize,
	int bgPixel, Color fgColor, Component observer)
{
	int[][] pixelRaster = new int[imgH][imgW];
	ImageOps.fillPixelRaster(pixelRaster, 0, 0, imgW, imgH, bgPixel);

	Font btTextFont = new Font("Dialog", Font.PLAIN, fontSize);
	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, btText,
		btTextFont, fgColor, 4, startTextY, observer);

	Font euroTextFont = new Font("Dialog", fontStyle, fontSize);
	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, "C",
		euroTextFont, fgColor, startTextX, startTextY, observer);

	ImageOps.setPixelRasterLine(pixelRaster,
		startTextX + topLineXLftShift,
		topLineY,
		startTextX + topLineXRhtShift,
		topLineY,
		fgColor.getRGB(), imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster,
		startTextX + btmLineXLftShift,
		topLineY + topLineYShift,
		startTextX + btmLineXRhtShift,
		topLineY + topLineYShift,
		fgColor.getRGB(), imgW, imgH);

	ImageOps.setPixelRasterLine(pixelRaster, 0, 0, imgW - 1, 0,
		0xffdcdee5, imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster, 0, 0, 0, imgH - 1,
		0xffdcdee5, imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster, 1, 1, imgW - 2, 1,
		0xffdcdee5, imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster, 1, 1, 1, imgH - 2,
		0xffdcdee5, imgW, imgH);

	ImageOps.setPixelRasterLine(pixelRaster, imgW - 1, imgH - 1, imgW - 1, 0,
		0xff5d6069, imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster, 0, imgH - 1, imgW - 1, imgH - 1,
		0xff5d6069, imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster, imgW - 2, imgH - 2, imgW - 2, 1,
		0xff5d6069, imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster, 1, imgH - 2, imgW - 2, imgH - 2,
		0xff5d6069, imgW, imgH);

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
}

// HERE
static public Image
makeLMLogoRegisteredImage(Image bgImg, int addHeight, int shortenW,
	int radius, int shiftRadX, int shiftRadY, int fontSize,
	Component observer)
{
	int bgImgW = bgImg.getWidth(observer);
	int imgW = bgImgW - shortenW;
	int imgH = bgImg.getHeight(observer);
	int[][] pixelRaster = null;

	if (addHeight > 0)
	{
		int[][] imgPixelRaster = ImageOps.getPixelRasterFromImg(bgImg, imgW, imgH);
		pixelRaster = ImageOps.getPixelRasterFilled(imgW, imgH + addHeight,
			ImageOps.whitePixel);
		ImageOps.addPixelRasterToPixelRaster(imgPixelRaster, pixelRaster,
			imgW, imgH, 0, addHeight, imgW, imgH + addHeight);
		imgH += addHeight;
	}
	else
	{
		pixelRaster = ImageOps.getPixelRasterFromImg(bgImg, imgW, imgH);
	}

	if (shortenW < 0)
	{
		ImageOps.fillPixelRaster(pixelRaster, bgImgW, 0, imgW, imgH, ImageOps.whitePixel);
	}

	/*
	pixelRaster = ImageOps.getPixelRasterFromImgRegion(bgImg, imgW, imgH,
		startRegionX, startRegionY, int regionW, int regionH)
	*/

	// change last part of img rectangle to clear out TM
	ImageOps.setPixelRasterPixels(pixelRaster, imgW - 20, 0, imgW, imgH, ImageOps.whitePixel);

	/*
	Font logoTextFont = new Font("Serif", Font.PLAIN, fontSize);
	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, "R",
		logoTextFont, Color.black, imgW - 14, 10, observer);

	ImageOps.setPixelRasterCircle(pixelRaster, imgW, imgH, imgW - radius - shiftRadX,
		radius + shiftRadY, radius, 0xff000000);
	*/

	Font logoTextFont = new Font("Serif", Font.PLAIN, fontSize);
	char[] testC = new char[1];
	testC[0] = 174;
	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH,
		new String(testC),
		logoTextFont, Color.black, imgW - 17, 11, observer);

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
}

static public Image
makeLMBasicShortLogoImage(Image bgImg, int addHeight, int shortenW,
	int radius, int shiftRadX, int shiftRadY,
	Component observer)
{
	/*
	radius = 7;
	shiftRadX = 6;
	shiftRadY = 2;
	*/

	int imgW = bgImg.getWidth(observer) - shortenW;
	int imgH = bgImg.getHeight(observer);
	int[][] pixelRaster = null;

	if (addHeight > 0)
	{
		int[][] imgPixelRaster = ImageOps.getPixelRasterFromImg(bgImg, imgW, imgH);
		pixelRaster = ImageOps.getPixelRasterFilled(imgW, imgH + addHeight,
			ImageOps.whitePixel);
		ImageOps.addPixelRasterToPixelRaster(imgPixelRaster, pixelRaster,
			imgW, imgH, 0, addHeight, imgW, imgH + addHeight);
		imgH += addHeight;
	}
	else
	{
		pixelRaster = ImageOps.getPixelRasterFromImg(bgImg, imgW, imgH);
	}

	// change last part of img rectangle to clear out TM
	ImageOps.setPixelRasterPixels(pixelRaster, 263, 0,
		imgW, imgH, ImageOps.whitePixel);

	int regionWidth = 5;
	pixelRaster = ImageOps.getPixelRasterSubtractVerticalRegion(pixelRaster,
		imgW, imgH, 17, regionWidth);
	imgW -= regionWidth;

	regionWidth = 6;
	pixelRaster = ImageOps.getPixelRasterSubtractVerticalRegion(pixelRaster,
		imgW, imgH, 43, regionWidth);
	imgW -= regionWidth;

	regionWidth = 6;
	pixelRaster = ImageOps.getPixelRasterSubtractVerticalRegion(pixelRaster,
		imgW, imgH, 71, regionWidth);
	imgW -= regionWidth;

	regionWidth = 2;
	pixelRaster = ImageOps.getPixelRasterSubtractVerticalRegion(pixelRaster,
		imgW, imgH, 135, regionWidth);
	imgW -= regionWidth;

	regionWidth = 3;
	pixelRaster = ImageOps.getPixelRasterSubtractVerticalRegion(pixelRaster,
		imgW, imgH, 160, regionWidth);
	imgW -= regionWidth;

	regionWidth = 4;
	pixelRaster = ImageOps.getPixelRasterSubtractVerticalRegion(pixelRaster,
		imgW, imgH, 191, regionWidth);
	imgW -= regionWidth;

	regionWidth = 5;
	pixelRaster = ImageOps.getPixelRasterSubtractVerticalRegion(pixelRaster,
		imgW, imgH, 205, regionWidth);
	imgW -= regionWidth;

	/*
	// draw debug lines
	ImageOps.setPixelRasterRectangle(pixelRaster,
		new Rectangle(0, 0, imgW - 1, imgH - 1),
		pixelToComplement(newMediaBGColor.getRGB()), imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster,
		118, 0, 118, imgH - 1,
		pixelToComplement(newMediaBGColor.getRGB()), imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster,
		263, 0, 263, imgH - 1,
		pixelToComplement(newMediaBGColor.getRGB()), imgW, imgH);
	*/

	/*
	Font logoTextFont = new Font("Serif", Font.PLAIN, 12);
	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, "R",
		logoTextFont, Color.black, imgW - 16, 14, observer);

	ImageOps.setPixelRasterCircle(pixelRaster, imgW, imgH, imgW - radius - shiftRadX,
		radius + shiftRadY, radius, 0xff000000);
	*/

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
}

static public Image
makeLMBasicLogoImage(Image bgImg, int addHeight, int shortenW,
	int radius, int shiftRadX, int shiftRadY,
	Component observer)
{
	/*
	radius = 7;
	shiftRadX = 6;
	shiftRadY = 2;
	*/

	int imgW = bgImg.getWidth(observer) - shortenW;
	int imgH = bgImg.getHeight(observer);
	int[][] pixelRaster = null;

	if (addHeight > 0)
	{
		int[][] imgPixelRaster = ImageOps.getPixelRasterFromImg(bgImg, imgW, imgH);
		pixelRaster = ImageOps.getPixelRasterFilled(imgW, imgH + addHeight,
			ImageOps.whitePixel);
		ImageOps.addPixelRasterToPixelRaster(imgPixelRaster, pixelRaster,
			imgW, imgH, 0, addHeight, imgW, imgH + addHeight);
		imgH += addHeight;
	}
	else
	{
		pixelRaster = ImageOps.getPixelRasterFromImg(bgImg, imgW, imgH);
	}

	// change last part of img rectangle to clear out TM
	ImageOps.setPixelRasterPixels(pixelRaster, 263, 0,
		imgW, imgH, ImageOps.whitePixel);

	/*
	pixelRaster = ImageOps.getPixelRasterSubtractVerticalRegion(pixelRaster,
		imgW, imgH, 222, 3);
	imgW -= 3;
	*/

	/*
	// draw debug lines
	ImageOps.setPixelRasterRectangle(pixelRaster,
		new Rectangle(0, 0, imgW - 1, imgH - 1),
		pixelToComplement(newMediaBGColor.getRGB()), imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster,
		118, 0, 118, imgH - 1,
		pixelToComplement(newMediaBGColor.getRGB()), imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster,
		263, 0, 263, imgH - 1,
		pixelToComplement(newMediaBGColor.getRGB()), imgW, imgH);
	*/

	/*
	Font logoTextFont = new Font("Serif", Font.PLAIN, 12);
	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, "R",
		logoTextFont, Color.black, imgW - 16, 14, observer);

	ImageOps.setPixelRasterCircle(pixelRaster, imgW, imgH, imgW - radius - shiftRadX,
		radius + shiftRadY, radius, 0xff000000);
	*/

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
}

static public Image
makeLMLogoImage(Image bgImg, int addHeight, int shortenW,
	int radius, int shiftRadX, int shiftRadY,
	Color liveColor,
	Color liveBGColor,
	Color mediaColor,
	Color mediaBGColor,
	Component observer)
throws Exception
{
	int imgW = bgImg.getWidth(observer) - shortenW;
	int imgH = bgImg.getHeight(observer);
	int[][] pixelRaster = null;
	Vector liveColorList = new Vector();
	Vector mediaColorList = new Vector();
	int addLiveRed = liveColor.getRed();
	int addLiveGreen = liveColor.getGreen();
	int addLiveBlue = liveColor.getBlue();
	int liveBGRed = liveBGColor.getRed();
	int liveBGGreen = liveBGColor.getGreen();
	int liveBGBlue = liveBGColor.getBlue();
	int addMediaRed = mediaColor.getRed();
	int addMediaGreen = mediaColor.getGreen();
	int addMediaBlue = mediaColor.getBlue();
	int mediaBGRed = mediaBGColor.getRed();
	int mediaBGGreen = mediaBGColor.getGreen();
	int mediaBGBlue = mediaBGColor.getBlue();


	if (addHeight > 0)
	{
		int[][] imgPixelRaster = ImageOps.getPixelRasterFromImg(bgImg, imgW, imgH);
		pixelRaster = ImageOps.getPixelRasterFilled(imgW, imgH + addHeight,
			ImageOps.whitePixel);
		ImageOps.addPixelRasterToPixelRaster(imgPixelRaster, pixelRaster,
			imgW, imgH, 0, addHeight, imgW, imgH + addHeight);
		imgH += addHeight;
	}
	else
	{
		pixelRaster = ImageOps.getPixelRasterFromImg(bgImg, imgW, imgH);
	}

	// change all white pixels to clear
	ImageOps.setPixelRasterPixelToNewPixel(pixelRaster, 0, 0,
		imgW, imgH, ImageOps.whitePixel, ImageOps.clearPixel);

	// change last part of img rectangle to clear out TM
	ImageOps.setPixelRasterPixels(pixelRaster, 263, 0,
		imgW, imgH, ImageOps.clearPixel);

	/******************* Live ****************/

	// LMLogo Images original bg color
	Color origBGColor = new Color(255, 255, 255); // ImageOps.whitePixel bg

	/*
	// starting live color
	Color startLiveColor = new Color(0xff660000); // starting main color

	// starting live color + altered color for "live"
	Color newLiveColor =
		ImageOps.addToColor(startLiveColor, addLiveRed, addLiveGreen, addLiveBlue);
	*/
	Color newLiveColor = liveColor;

	/*
	// starting MEDIA color
	Color startMediaColor = Color.black;

	// starting MEDIA color + altered color for "MEDIA"
	Color newMediaColor = ImageOps.addToColor(startMediaColor,
		addMediaRed, addMediaGreen, addMediaBlue);
	*/
	Color newMediaColor = mediaColor;

	Color newLiveBGColor = new Color(liveBGRed, liveBGGreen, liveBGBlue);

	Color newMediaBGColor =
		new Color(mediaBGRed, mediaBGGreen, mediaBGBlue);

	// add color to each non clear pixel in first part of image
	if (!((addLiveRed == 0) && (addLiveGreen == 0) && (addLiveBlue == 0)))
	{
		for (int row = 0;row < imgH;row++)
		{
			for (int col = 0;col < 118;col++)
			{
				int pixel = pixelRaster[row][col];
				if (pixel != ImageOps.clearPixel)
					pixelRaster[row][col] = ImageOps.addToColor(pixel,
						addLiveRed, addLiveGreen, addLiveBlue);
			}
		}
	}

	int[] liveColorCount = new int[256];
	for (int i = 0;i < 256;i++)
		liveColorCount[i] = 0;

	// get some pixels to change later for Live part
	for (int row = 0;row < imgH;row++)
	{
		for (int col = 0;col < 118;col++)
		{
			if (pixelRaster[row][col] == ImageOps.clearPixel)
				continue;
			Color listColor = new Color(pixelRaster[row][col]);
			if (liveColorList.contains(listColor))
			{
				liveColorCount[liveColorList.indexOf(listColor)]++;
				continue;
			}
			liveColorList.addElement(listColor);
			liveColorCount[liveColorList.indexOf(listColor)]++;
			
			/*
			System.out.println(
				listColor.toString() + " " +
				// " " + pixelRaster[row][col] + " " +
				Integer.toHexString(pixelRaster[row][col]) + " " + 
				// listColor.getRed() + " " +
				// listColor.getGreen() + " " +
				// listColor.getBlue() + " " +
				// listColor.getRGB() + " " +
				(listColor.getRGB() & 0x00ffffff) + " " +
				ImageOps.colorDistanceTVal(newLiveColor, listColor, Color.white,
					true)
				);
			System.out.println();
			*/
		}
	}
	
	BVector startPt = new BVector(
		(double)(newLiveColor.getRed()),
		(double)(newLiveColor.getGreen()),
		(double)(newLiveColor.getBlue()));
	BVector toPt = new BVector(
		(double)(newLiveBGColor.getRed()),
		(double)(newLiveBGColor.getGreen()),
		(double)(newLiveBGColor.getBlue()));
	BVector ofPt = new BVector();
	BVector newPt = new BVector();

	// blend Live half to new background
	for (int i = 0;i < liveColorList.size();i++)
	{
		Color mapColor = (Color)liveColorList.elementAt(i);
		ofPt.setPoint(
			(double)(mapColor.getRed()),
			(double)(mapColor.getGreen()),
			(double)(mapColor.getBlue()));

		/* NEED to work out for BRay
		MathOps.getVectorLineAtT(startPt, toPt, newPt,
			ImageOps.colorDistanceTVal(newLiveColor, mapColor, origBGColor, false));
		*/

		Color newColor = ImageOps.colorValsToJColor(
			(int)Math.round(newPt.xCoor()),
			(int)Math.round(newPt.yCoor()),
			(int)Math.round(newPt.zCoor()));

		ImageOps.setPixelRasterPixelToNewPixel(pixelRaster, 0, 0,
			118, imgH, mapColor.getRGB(), newColor.getRGB());
		/*
		System.out.println("NEW COLOR: " + newColor + " " +
			Integer.toHexString(newColor.getRGB()));
		System.out.println();
		*/
	}

	/******************* Media ****************/

	// add some blue to each non clear pixel in media part of image
	if (!((addMediaRed == 0) && (addMediaGreen == 0) &&
		(addMediaBlue == 0)))
	{
		for (int row = 0;row < imgH;row++)
		{
			for (int col = 118;col < imgW;col++)
			{
				int pixel = pixelRaster[row][col];
				if (pixel != ImageOps.clearPixel)
					pixelRaster[row][col] = ImageOps.addToColor(pixel,
						addMediaRed, addMediaGreen, addMediaBlue);
			}
		}
	}

	int[] mediaColorCount = new int[256];
	for (int i = 0;i < 256;i++)
		mediaColorCount[i] = 0;

	// get some pixels to change later Media part
	for (int row = 0;row < imgH;row++)
	{
		for (int col = 118;col < imgW;col++)
		{
			if (pixelRaster[row][col] == ImageOps.clearPixel)
				continue;
			Color listColor = new Color(pixelRaster[row][col]);
			if (mediaColorList.contains(listColor))
			{
				mediaColorCount[mediaColorList.indexOf(listColor)]++;
				continue;
			}
			mediaColorList.addElement(listColor);
			mediaColorCount[mediaColorList.indexOf(listColor)]++;
			
			/*
			System.out.println(
				listColor.toString() + " " +
				// " " + pixelRaster[row][col] + " " +
				Integer.toHexString(pixelRaster[row][col]) + " " + 
				// listColor.getRed() + " " +
				// listColor.getGreen() + " " +
				// listColor.getBlue() + " " +
				// listColor.getRGB() + " " +
				(listColor.getRGB() & 0x00ffffff) + " " +
				ImageOps.colorDistanceTVal(Color.black, listColor, Color.white, true)
				);
			System.out.println();
			*/
		}
	}

	/* print out Live color info
	for (int i = 0;i < liveColorList.size();i++)
	{
		Color mapColor = (Color)liveColorList.elementAt(i);
		int colorID = liveColorList.indexOf(mapColor);
		int colorMax = liveColorCount[colorID];

		System.out.println(
			mapColor.toString() +
			mapColor.getRed() + " " +
			mapColor.getGreen() + " " +
			mapColor.getBlue() + " " +
			mapColor.getRGB() + " count: " + colorMax);
	}
	*/


	// blend Media half to new background

	// First change non-clear pixels to something like add light blue

	startPt.setPoint(
		(double)(newMediaColor.getRed()),
		(double)(newMediaColor.getGreen()),
		(double)(newMediaColor.getBlue()));
	toPt.setPoint(
		(double)(newMediaBGColor.getRed()),
		(double)(newMediaBGColor.getGreen()),
		(double)(newMediaBGColor.getBlue()));
	ofPt.getPositionVector().buildNullTransformMatrix();
	newPt.getPositionVector().buildNullTransformMatrix();

	for (int i = 0;i < mediaColorList.size();i++)
	{
		Color mapColor = (Color)mediaColorList.elementAt(i);
		ofPt.setPoint(
			(double)(mapColor.getRed()),
			(double)(mapColor.getGreen()),
			(double)(mapColor.getBlue()));

		/* NEED to work out for BRAY
		MathOps.getVectorLineAtT(startPt, toPt, newPt,
			ImageOps.colorDistanceTVal(newMediaColor, mapColor, origBGColor, false));
		*/

		Color newColor = ImageOps.colorValsToJColor(
			(int)Math.round(newPt.xCoor()),
			(int)Math.round(newPt.yCoor()),
			(int)Math.round(newPt.zCoor()));
		ImageOps.setPixelRasterPixelToNewPixel(pixelRaster, 118, 0,
			imgW, imgH, mapColor.getRGB(), newColor.getRGB());
	}

	/*
	// draw debug lines
	ImageOps.setPixelRasterRectangle(pixelRaster,
		new Rectangle(0, 0, imgW - 1, imgH - 1),
		pixelToComplement(newMediaBGColor.getRGB()), imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster,
		118, 0, 118, imgH - 1,
		pixelToComplement(newMediaBGColor.getRGB()), imgW, imgH);
	ImageOps.setPixelRasterLine(pixelRaster,
		263, 0, 263, imgH - 1,
		pixelToComplement(newMediaBGColor.getRGB()), imgW, imgH);
	*/

	/*
	Font logoTextFont = new Font("Times", Font.PLAIN, 10);
	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, "\256",
		logoTextFont, newMediaColor, imgW - 22, 26, observer);
	*/

	Font logoTextFont = new Font("Serif", Font.PLAIN, 12);
	ImageOps.setPixelRasterString(pixelRaster, imgW, imgH, "R",
		logoTextFont, newMediaColor, imgW - 16, 14, observer);

	ImageOps.setPixelRasterCircle(pixelRaster, imgW, imgH, imgW - radius - shiftRadX,
		radius + shiftRadY, radius, newMediaColor.getRGB());

	return (ImageOps.createPixelRasterMemoryImage(
		pixelRaster, imgW, imgH, observer));
}

public static void
main(String args[])
{
	int imgCount = 40;
	/*
	for (int i = 1;i < imgCount+1;i++)
	*/
	for (int i = imgCount;i >= 1;i--)
	{

		double tVal = MathOps.rOneToROneMap(
			(double)i, 1.0, (double)imgCount, 0.0, 1.0);
		/*
		System.out.println("0x" +
			Integer.toHexString(ImageOps.getNewColorOnRGBLine(
				// new Color(0xff5151ba), new Color(0xffc0c0c0),
				new Color(0xff5151ba), new Color(0xffaeb2c3),
			tVal).getRGB()) + ",");
		*/
	}
}

}


/*
class
HighlightFilter extends RGBImageFilter
{
boolean brighter;
int percent;

public
HighlightFilter(boolean b, int p)
{
	brighter = b;
	percent = p;
	canFilterIndexColorModel = true;
}

public int
filterRGB(int x, int y, int rgb)
{
	int r = (rgb >> 16) & 0xff;
	int g = (rgb >> 8) & 0xff;
	int b = (rgb >> 0) & 0xff;
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
	if (r < 0)
		r = 0;
	if (r > 255)
		r = 255;
	if (g < 0)
		g = 0;
	if (g > 255)
		g = 255;
	if (b < 0)
		b = 0;
	if (b > 255)
		b = 255;
	return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
}
}
*/

