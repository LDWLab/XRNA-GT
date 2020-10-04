package util;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;

import util.*;
import util.math.*;

public class
PostScriptUtil
{
private final double LWXSHIFTORIGIN  = 306.0;
private final double LWYSHIFTORIGIN  = 396.0;
private final double PSDOTSPERINCH   = 300.0;
private final double PSPOINTSPERINCH = 72.0;
private final double DEFPSSCALEVAL   = 0.75;
private final double DEFFONTSCALE    = 80.0;

private final int CAPBUTT	= 0;
private final int CAPROUND	= 1;

private final int HELVETICA            = 0;
private final int HELVETICAOBLIQUE     = 1;
private final int HELVETICABOLD        = 2;
private final int HELVETICABOLDOBLIQUE = 3;
private final int TIMESROMAN           = 4;
private final int TIMESITALIC          = 5;
private final int TIMESBOLD            = 6;
private final int TIMESBOLDITALIC      = 7;
private final int COURIER              = 8;
private final int COURIEROBLIQUE       = 9;
private final int COURIERBOLD          = 10;
private final int COURIERBOLDOBLIQUE   = 11;
private final int ADOBESYMBOL          = 12;

private  String NucFontType = null;

public
PostScriptUtil()
{
	this.setPSStrBuf(new StringBuffer());
}

public void
init2DPSCmds(boolean landscapeModeOn, Graphics2D g2)
{
	NucFontType = new String();
	printPSHeader();
	initLWCmds();
	printPSGSave();

	// DON'T KNOW WHY THIS WORKS YET:
	// printPSTranslate(LWXSHIFTORIGIN/2.0, LWYSHIFTORIGIN/2.0);

	printPSTranslate(LWXSHIFTORIGIN, LWYSHIFTORIGIN);
	// printPSRotate(270.0);

	if (landscapeModeOn)
	{
		printPSRotate(90.0);
	}
	printPSM4String("%m4rotate");
	/*
	if (StampSSFigure)
	{
		printSSStamp();
	}
	*/
	printPSScale(DEFPSSCALEVAL * g2.getTransform().getScaleX());
	printPSM4String("%m4scale");

	double transX = g2.getTransform().getTranslateX();
	double transY = g2.getTransform().getTranslateY();
	/* 	GetSSFigGeometry(sstr, &transx, &transy, 1, SSNucCount(sstr), FALSE);
	if (printSuppressCentering)
		printPSTranslate(UserSSTransXVal, UserSSTransYVal);
	else
		printPSTranslate(-transx + UserSSTransXVal, -transy + UserSSTransYVal);
	*/
	// printPSTranslate(transX, transY);

	printPSM4String("%m4translate");

	// NEED to get original background from ComplexSceneView.java
	/*
	if (printPSOutPutOnly)
	{
		tmpcolor = CurrentRNA2DWindowColorVal();
		if (tmpcolor != Color.white)
			printColorPSBackground(tmpcolor);
	}
	else if (GetXRNAWindowColor(RNA2DWindow) != Color.white)
	{
		// NEED to get original background from ComplexSceneView.java
		printColorPSBackground(Color.white);
	}
	*/
	printColorPSBackground(Color.white);
}

public void
initLWCmds()
{
	// this.getPSStrBuf().append("/LPCENX {" + MathUtil.roundVal(LWXSHIFTORIGIN, 2) + "} def\n");
	// this.getPSStrBuf().append("/LPCENY {" + MathUtil.roundVal(LWYSHIFTORIGIN, 2) + "} def\n");
	// this.getPSStrBuf().append("/centeronpaper {LPCENX LPCENY translate} def\n");
	this.getPSStrBuf().append("/lwline {newpath moveto lineto stroke} def\n");
	this.getPSStrBuf().append("/lwbox {newpath /boxy exch def /boxx exch def\n");
	this.getPSStrBuf().append("\tboxx boxy moveto lineto lineto lineto boxx boxy lineto stroke} def\n");
	this.getPSStrBuf().append("/lwfbox {newpath /boxy exch def /boxx exch def\n");
	this.getPSStrBuf().append("\tboxx boxy moveto lineto lineto lineto boxx boxy lineto fill} def\n");
	this.getPSStrBuf().append("/lwgrayline {setgray newpath moveto lineto stroke} def\n");
	/*
	this.getPSStrBuf().append("/m4translate {} def\n");
	this.getPSStrBuf().append("/m4rotate {} def\n");
	this.getPSStrBuf().append("/m4scale {} def\n");
	*/

	/*
	this.getPSStrBuf().append("/" + NucFontType + " findfont " +
		MathUtil.roundVal(fontsize, 2) + " scalefont setfont\n");
	*/
	// this.getPSStrBuf().append("/set_font {showpage} def\n");
	// /Helvetica findfont 12.0 scalefont setfont

	this.getPSStrBuf().append("/m4showpage {showpage} def\n");
	this.getPSStrBuf().append(
		"/lwarc {newpath gsave translate scale /rad exch def /ang1 exch def /ang2 exch def\n  0.0 0.0 rad ang1 ang2 arc stroke grestore} def\n");
	this.getPSStrBuf().append(
		"/lwfarc {newpath gsave translate scale /rad exch def /ang1 exch def /ang2 exch def\n  0.0 0.0 rad ang1 ang2 arc fill grestore} def\n");
	this.getPSStrBuf().append(
		"/lwenclosegrayarc {/cypos exch def /cxpos exch def /cscale exch def /crad exch def /clinewidth exch def /cgrayval exch def newpath gsave cgrayval cgrayval cgrayval setrgbcolor cxpos cypos translate cscale cscale scale 0.0 0.0 crad 0.0 360.0 arc fill grestore newpath gsave 0.0 0.0 0.0 setrgbcolor clinewidth setlinewidth cxpos cypos translate cscale cscale scale 0.0 0.0 crad 0.0 360.0 arc stroke grestore} def\n");
	
	this.getPSStrBuf().append("/lwarcbox {newpath gsave translate scale /rad0 exch def\n  /rad1 exch def /ang0 exch def /ang1 exch def /grayval exch def\n  grayval setgray newpath 0.0 0.0 rad1 ang1 ang0 arcn 0.0 0.0 rad0 ang0 ang1 arc closepath fill\n  0.0 setgray newpath 0.0 0.0 rad1 ang1 ang0 arcn\n  0.0 0.0 rad0 ang0 ang1 arc closepath stroke grestore} def\n");

	this.getPSStrBuf().append(
		"/lwcircle {newpath gsave translate scale /rad exch def\n \t0.0 0.0 rad 0.0 360.0 arc stroke grestore} def\n");
	this.getPSStrBuf().append(
		"/lwfcircle {newpath gsave translate scale /rad exch def\n \t0.0 0.0 rad 0.0 360.0 arc fill grestore} def\n");
	this.getPSStrBuf().append(
		"/lwfgcircle {newpath gsave setgray translate scale /rad exch def\n \t0.0 0.0 rad 0.0 360.0 arc fill grestore} def\n");
	this.getPSStrBuf().append(
		"/lwtriangle {newpath gsave translate scale rotate moveto lineto lineto lineto stroke grestore} def\n");
	this.getPSStrBuf().append(
		"/lwftriangle {newpath gsave translate scale rotate moveto lineto lineto lineto fill grestore} def\n");

	this.getPSStrBuf().append(
		"/lwfrect {1.0 setgray newpath moveto lineto lineto lineto lineto fill} def\n");

	/*
	this.getPSStrBuf().append(
		"/lwflinerect {setgray /r0x exch def /r0y exch def /r1x exch def /r1y exch def /r2x exch def /r2y exch def /r3x exch def /r3y exch def newpath r0x r0y moveto r1x r1y lineto r2x r2y lineto r3x r3y lineto r0x r0y lineto fill 0.0 setgray newpath r0x r0y moveto r1x r1y lineto stroke r2x r2y moveto r3x r3y lineto stroke} def\n");
	*/
	this.getPSStrBuf().append(
		"/lwflinerect {setrgbcolor /r0x exch def /r0y exch def /r1x exch def /r1y exch def /r2x exch def /r2y exch def /r3x exch def /r3y exch def newpath r0x r0y moveto r1x r1y lineto r2x r2y lineto r3x r3y lineto r0x r0y lineto fill 0.0 setgray newpath r0x r0y moveto r1x r1y lineto stroke r2x r2y moveto r3x r3y lineto stroke} def\n");


	/*
	this.getPSStrBuf().append(
		"/lwflinetriangle {setgray /t0x exch def /t0y exch def /t1x exch def /t1y exch def /t2x exch def /t2y exch def newpath t0x t0y moveto t1x t1y lineto t2x t2y lineto t0x t0y lineto fill 0.0 setgray newpath t1x t1y moveto t2x t2y lineto stroke} def\n");
	*/
	this.getPSStrBuf().append(
		"/lwflinetriangle {setrgbcolor /t0x exch def /t0y exch def /t1x exch def /t1y exch def /t2x exch def /t2y exch def newpath t0x t0y moveto t1x t1y lineto t2x t2y lineto t0x t0y lineto fill 0.0 setgray newpath t1x t1y moveto t2x t2y lineto stroke} def\n");

	this.getPSStrBuf().append("/lwstring {moveto show} def\n");
	this.getPSStrBuf().append("/lwlabel {gsave translate rotate 0.0 0.0 moveto show grestore} def\n");
	this.getPSStrBuf().append("/lwarrow {gsave translate scale rotate moveto lineto lineto lineto stroke 0.0 0.0 lwline grestore} def\n");
	this.getPSStrBuf().append("/lwfarrow {gsave translate scale rotate moveto lineto lineto lineto\n  fill lwline grestore} def\n");
	this.getPSStrBuf().append("/lwgraystring {setgray moveto show} def\n");
	this.getPSStrBuf().append("/getboxshow {false charpath flattenpath pathbbox stroke} def\n");
	this.getPSStrBuf().append("/getbox {false charpath flattenpath pathbbox} def\n");

	this.getPSStrBuf().append("/drawbox {newpath moveto getboxshow newpath");
	this.getPSStrBuf().append("\t/ury exch def /urx exch def /lly exch def /llx exch def\n");
	this.getPSStrBuf().append("\tllx lly moveto llx ury lineto urx ury lineto urx lly lineto");
	this.getPSStrBuf().append("\tllx lly lineto stroke} def\n");
	
	this.getPSStrBuf().append("/getstrmidpt {");
	this.getPSStrBuf().append("\t/cury exch def /curx exch def curx cury moveto getbox");
	this.getPSStrBuf().append("\t/ury exch def /urx exch def /lly exch def /llx exch def\n");
	this.getPSStrBuf().append("\t2.0 curx mul llx urx add 2.0 div sub 2.0 cury\n  mul lly ury add 2.0 div sub translate");
	this.getPSStrBuf().append("\t} def\n");

	this.getPSStrBuf().append("/print_nuc_char {gsave translate setrgbcolor lwstring grestore} def\n");
	this.getPSStrBuf().append("/print_nuc_strlabel {gsave translate translate setrgbcolor lwstring grestore} def\n");

	// 453.76 -272.5 453.76 -261.5 1.0 0.0 0.0 0.0 print_line
	this.getPSStrBuf().append("/print_line {gsave setrgbcolor setlinewidth lwline grestore} def\n");

	/*
	gsave
	346.76 -250.51 translate
	gsave
	0.2 setlinewidth
	0.0 0.0 0.0 setrgbcolor
	0.0 7.5 0.0 15.5 lwline
	grestore
	grestore
	*/
	// this.getPSStrBuf().append("/print_nuc_linelabel {gsave translate lwline grestore} def\n");
	this.getPSStrBuf().append("/print_nuc_linelabel {gsave translate print_line grestore} def\n");

	reSetAllPSStaticVals();
	printCurrentDash();
	setPrintCurrentPSColorVal(Color.black);
	setCurrentFont(getCurrentFont());
	printCurrentFont(DEFFONTSCALE);
	this.getPSStrBuf().append("1 setlinejoin\n");
	setPrintCurrentPSLineWidth(0.05);
	setPrintCurrentPSLineCap(CAPBUTT);
	printPSNewPath();
}

private StringBuffer psStrBuf = null;

public void
setPSStrBuf(StringBuffer psStrBuf)
{
	this.psStrBuf = psStrBuf;
}

public StringBuffer
getPSStrBuf()
{
	return (this.psStrBuf);
}

private double currentlinewidth = 0.0;

public double
getCurrentLineWidth()
{
	return(currentlinewidth);
}

public void
setCurrentLineWidth(double linewidth)
{
	currentlinewidth = linewidth;
}

private double cmpCurrentLineWidth = -10.00;

public void
setCmpCurrentLineWidth(double lineWidth)
{
	cmpCurrentLineWidth = lineWidth;
}

public double
getCmpCurrentLineWidth()
{
	return (cmpCurrentLineWidth);
}

public void
printCurrentPSLineWidth()
{
	if (!MathUtil.precisionEquality(getCurrentLineWidth(), getCmpCurrentLineWidth(), 2.0))
	{
		this.getPSStrBuf().append(MathUtil.roundVal(getCurrentLineWidth(), 2) + " setlinewidth\n");
		setCmpCurrentLineWidth(getCurrentLineWidth());
	}
}

public void
setPrintCurrentPSLineWidth(double lineWidth)
{
	setCurrentLineWidth(lineWidth);
	printCurrentPSLineWidth();
}

private  Color currentpscolorval = Color.black;

public  Color
getCurrentPSColorVal()
{
	return(currentpscolorval);
}

public void
setCurrentPSColorVal(Color colorval)
{
	currentpscolorval = colorval;
}

// private Color curcolorval = MAXCOLORVAL + 1;
private  Color curcolorval = Color.white;

public void
printCurrentPSColorVal()
{
	if (getCurrentPSColorVal() == curcolorval)
		return;
	this.getPSStrBuf().append(
		MathUtil.roundVal(MathUtil.colorToRedNormal(getCurrentPSColorVal()), 2) + " " +
		MathUtil.roundVal(MathUtil.colorToGreenNormal(getCurrentPSColorVal()), 2) + " " +
		MathUtil.roundVal(MathUtil.colorToBlueNormal(getCurrentPSColorVal()), 2) + " " +
		"setrgbcolor\n");
	curcolorval = getCurrentPSColorVal();
}

public void
setPrintCurrentPSColorVal(Color colorval)
{
	setCurrentPSColorVal(colorval);
	printCurrentPSColorVal();
}

private int currentlinecap = CAPBUTT;

public int
getCurrentLineCap()
{
	return(currentlinecap);
}

public void
setCurrentLineCap(int linecap)
{
	currentlinecap = linecap;
}

private int printcurlinecap = -1;

public void
printCurrentPSLineCap()
{
	if (getCurrentLineCap() != printcurlinecap)
	{
		this.getPSStrBuf().append(getCurrentLineCap() + " setlinecap\n");
		printcurlinecap = getCurrentLineCap();
	}
}

public void
setPrintCurrentPSLineCap(int linecap)
{
	setCurrentLineCap(linecap);
	printCurrentPSLineCap();
}

private int currentfont = HELVETICABOLD;

public int
getCurrentFont()
{
	return(currentfont);
}

public void
setCurrentFont(int font)
{
	currentfont = font;
	switch (font)
	{
	  case HELVETICA :
		NucFontType = "Helvetica";
		break;
	  case HELVETICAOBLIQUE :
		NucFontType = "Helvetica-Oblique";
		break;
	  case HELVETICABOLD :
		NucFontType = "Helvetica-Bold";
		break;
	  case HELVETICABOLDOBLIQUE :
		NucFontType = "Helvetica-BoldOblique";
		break;
	  case TIMESROMAN :
		NucFontType = "Times-Roman";
		break;
	  case TIMESITALIC :
		NucFontType = "Times-Italic";
		break;
	  case TIMESBOLD :
		NucFontType = "Times-Bold";
		break;
	  case TIMESBOLDITALIC :
		NucFontType = "Times-BoldItalic";
		break;
	  case COURIER :
		NucFontType = "Courier";
		break;
	  case COURIEROBLIQUE :
		NucFontType = "Courier-Oblique";
		break;
	  case COURIERBOLD :
		NucFontType = "Courier-Bold";
		break;
	  case COURIERBOLDOBLIQUE :
		NucFontType = "Courier-BoldOblique";
		break;
	  case ADOBESYMBOL :
		NucFontType = "Symbol";
		break;
	  default :
		break;
	}
}

public void
setCurrentFont(String fontName)
{
	if (fontName.equals("Helvetica") || fontName.equals("sansserif"))
	{
		NucFontType = "Helvetica";
		currentfont = HELVETICA;
	}
	else if (fontName.equals("Helvetica-Oblique"))
	{
		NucFontType = fontName;
		currentfont = HELVETICAOBLIQUE;
	}
	else if (fontName.equals("Helvetica-Bold"))
	{
		NucFontType = fontName;
		currentfont = HELVETICABOLD;
	}
	else if (fontName.equals("Helvetica-BoldOblique"))
	{
		NucFontType = fontName;
		currentfont = HELVETICABOLDOBLIQUE;
	}
	else if (fontName.equals("Times-Roman"))
	{
		NucFontType = fontName;
		currentfont = TIMESROMAN;
	}
	else if (fontName.equals("Times-Italic"))
	{
		NucFontType = fontName;
		currentfont = TIMESITALIC;
	}
	else if (fontName.equals("Times-Bold"))
	{
		NucFontType = fontName;
		currentfont = TIMESBOLD;
	}
	else if (fontName.equals("Times-BoldItalic"))
	{
		NucFontType = fontName;
		currentfont = HELVETICA;
	}
	else if (fontName.equals("Courier"))
	{
		NucFontType = fontName;
		currentfont = COURIER;
	}
	else if (fontName.equals("Courier-Oblique"))
	{
		NucFontType = fontName;
		currentfont = COURIEROBLIQUE;
	}
	else if (fontName.equals("Courier-Bold"))
	{
		NucFontType = fontName;
		currentfont = COURIERBOLD;
	}
	else if (fontName.equals("Courier-BoldOblique"))
	{
		NucFontType = fontName;
		currentfont = COURIERBOLDOBLIQUE;
	}
	else if (fontName.equals("Symbol"))
	{
		NucFontType = fontName;
		currentfont = ADOBESYMBOL;
	}
	else
	{
		debug("ERROR in font type: " + fontName);
	}
}

private double lastcurfontsize = -10.0;
private double lastcurlinewidth = -10.0;
private int lastcurfont = -1;

public void
printCurrentFont(double fontsize)
{
	if (!((!MathUtil.precisionEquality(lastcurfontsize, fontsize, 2.0)) ||
		(lastcurfont != getCurrentFont()) ||
		(!MathUtil.precisionEquality(lastcurlinewidth, getCurrentLineWidth(), 2))))
		return;
	
	lastcurfontsize = fontsize;
	lastcurfont = getCurrentFont();
	lastcurlinewidth = getCurrentLineWidth();
	/* BUG: TAKE CARE OF THIS: */
	/*
	this.getPSStrBuf().append( "/%s %s %.2f %s\n", NucFontType, "findfont", 10.0 * fontsize, "scalefont setfont");
	*/
	/* :END BUG */
	this.getPSStrBuf().append('/' + NucFontType + " findfont " + MathUtil.roundVal(fontsize, 2) + " scalefont setfont\n");
}

public void
setPrintCurrentFont(Graphics2D g2)
{
	setCurrentFont(g2.getFont().getFamily());
	printCurrentFont((double)g2.getFont().getSize2D());
}

public void
printPSGSave()
{
	this.getPSStrBuf().append("gsave\n");
}

public void
printPSNewPath()
{
	this.getPSStrBuf().append("newpath\n");
}

private int arrayval0 = 0;
private int arrayval1 = 0;
private int arrayoffset = 0;

public void
setCurrentDash(int val0, int val1, int set)
{
	arrayval0 = val0;
	arrayval1 = val1;
	arrayoffset = set;
}

public void
printCurrentDash()
{
	printDash(arrayval0, arrayval1, arrayoffset);
}

public void
setPrintCurrentDash(int val0, int val1, int set)
{
	setCurrentDash(val0, val1, set);
	printCurrentDash();
}

private int lastdashval0 = -1;
private int lastdashval1 = -1;
private int lastdashset = -1;

public void
printDash(int val0, int val1, int offset)
{
	if ((lastdashval0 != val0) || (lastdashval1 != val1) || (lastdashset != offset))
	{
		lastdashval0 = val0;
		lastdashval1 = val1;
		lastdashset = offset;
		if ((val0 == 0) && (val1 == 0))
		{
			this.getPSStrBuf().append("[] " + offset + " setdash\n");
		}
		else if (val1 == 0)
		{
			this.getPSStrBuf().append("[" + val0 + "] " + offset + " setdash\n");
		}
		else
		{
			this.getPSStrBuf().append("[" + val0 + " " + val1 + "] " + offset + " setdash\n");
		}
	}
}

public void
endLWCmds()
{
	printPSStroke();
	printPSGRestore();
	printPSShowPage();
}

public void
endRestorePSCmds()
{
	printPSStroke();
	printPSGRestore();
}

public void
printPSHeader()
{
	this.getPSStrBuf().append("%!\n");
}

public void
printPSClear()
{
	this.getPSStrBuf().append("clear\n");
}

public void
printPSLineTo(double xpos, double ypos)
{
	this.getPSStrBuf().append(MathUtil.roundVal(xpos, 2) + " " + MathUtil.roundVal(ypos, 2) + " lineto\n");
}

public void
printPSIntLineTo(int x, int y)
{
	this.getPSStrBuf().append(x + " " + y + " lineto\n");
}

public void
printPSMoveTo(double xpos, double ypos)
{
	this.getPSStrBuf().append(MathUtil.roundVal(xpos, 2) + " " + MathUtil.roundVal(ypos, 2) + " moveto\n");
}

public void
printPSIntMoveTo(int x, int y)
{
	this.getPSStrBuf().append(x + " " + y + " moveto\n");
}

public void
printPSStroke()
{
	this.getPSStrBuf().append("stroke\n");
}

public void
printPSFill()
{
	this.getPSStrBuf().append("fill\n");
}

public void
printPSGRestore()
{
	this.getPSStrBuf().append("grestore\n");
}

public void
printPSShowPage()
{
	this.getPSStrBuf().append("showpage\n");
}

public void
printPSLine(double x1, double y1, double x2, double y2, double lineWidth, Color color)
{
	setPrintCurrentPSLineWidth(lineWidth);
	setPrintCurrentPSColorVal(color);
	this.getPSStrBuf().append(
		MathUtil.roundVal(x1, 2) + " " +
		MathUtil.roundVal(y1, 2) + " " +
		MathUtil.roundVal(x2, 2) + " " +
		MathUtil.roundVal(y2, 2) + " lwline\n");
}

public void
printPSLine(double x1, double y1, double x2, double y2, Color color)
{
	printPSLine(x1, y1, x2, y2, getCurrentLineWidth(), color);
}

public void
printPSLine(Point2D pt0, Point2D pt1, Color color)
{
	printPSLine(pt0.getX(), pt0.getY(), pt1.getX(), pt1.getY(), getCurrentLineWidth(), color);
}

public void
printPSLine(double x1, double y1, double x2, double y2, Graphics2D g2)
{
	printPSLine(x1, y1, x2, y2, (double)((BasicStroke)g2.getStroke()).getLineWidth(), g2.getColor());
}

public void
printPSLine(Point2D pt0, Point2D pt1, Graphics2D g2)
{
	printPSLine(pt0.getX(), pt0.getY(), pt1.getX(), pt1.getY(), g2);
}

public void
printPSIntLine(int x1, int y1, int x2, int y2)
{
	this.getPSStrBuf().append(x1 + " " + y1 + " " + x2 + " " + y2 + " lwline\n");
}


public void
psPrintEnclosedGrayCircle(double xpos, double ypos, double scaleval, double radius, double linewidth, double grayval)
{
	this.getPSStrBuf().append(
		MathUtil.roundVal(grayval, 2) + " " +
		MathUtil.roundVal(linewidth, 2) + " " +
		MathUtil.roundVal(radius, 2) + " " +
		MathUtil.roundVal(scaleval, 2) + " " +
		MathUtil.roundVal(xpos, 2) + " " +
		MathUtil.roundVal(ypos, 2) + " lwenclosegrayarc\n");
}

public void
printPSArc(double ang1, double ang2, double radius, double scaleval, double xpos, double ypos)
{
	this.getPSStrBuf().append(
		MathUtil.roundVal(ang1, 2) + " " +
		MathUtil.roundVal(ang2, 2) + " " +
		MathUtil.roundVal(radius, 2) + " " +
		MathUtil.roundVal(scaleval, 2) + " " +
		MathUtil.roundVal(scaleval, 2) + " " +
		MathUtil.roundVal(xpos, 2) + " " +
		MathUtil.roundVal(ypos, 2) + " lwarc\n");
}

public void
printPSFillArc(double ang1, double ang2, double radius, double scaleval, double xpos, double ypos)
{
	this.getPSStrBuf().append(
		MathUtil.roundVal(ang1, 2) + " " +
		MathUtil.roundVal(ang2, 2) + " " +
		MathUtil.roundVal(radius, 2) + " " +
		MathUtil.roundVal(scaleval, 2) + " " +
		MathUtil.roundVal(scaleval, 2) + " " +
		MathUtil.roundVal(xpos, 2) + " " +
		MathUtil.roundVal(ypos, 2) + " lwfarc\n");
}

public void
psPrintCircle(Color color, double xpos, double ypos, double radius, double scaleval, boolean isopen)
{
	setPrintCurrentPSLineWidth(0.0);
	setPrintCurrentPSLineCap(CAPROUND);
	setPrintCurrentDash(0, 0, 0);
	setPrintCurrentPSColorVal(color);
	printPSFillArc(360.0, 0.0, radius, scaleval, xpos, ypos);
	if (!isopen)
		return;
	setPrintCurrentPSColorVal(Color.black);
	printPSArc(360.0, 0.0, radius, scaleval, xpos, ypos);
}

public void
printPSIntString(String str, int x, int y)
{
	this.getPSStrBuf().append("(" + str + ")" + x + " " + y + " lwstring\n");
}

public void
printPSBackSlashIntString(String str, int x, int y)
{
	this.getPSStrBuf().append("(" + '\\' + str + ")" + x + " " + y + " lwstring\n");
}

public void
printPSBackSlashString(String str, double xpos, double ypos)
{
	this.getPSStrBuf().append("(" + '\\' + str + ") " +
		MathUtil.roundVal(xpos, 2) + " " +
		MathUtil.roundVal(ypos, 2) + " " +
		" lwstring\n");
}

public void
printPSString(String str, double xpos, double ypos)
{
	this.getPSStrBuf().append("(" + str + ") " +
		MathUtil.roundVal(xpos, 2) + " " +
		MathUtil.roundVal(ypos, 2) + " lwstring\n");
}

public void
printPSRotate(double angle)
{
	this.getPSStrBuf().append(MathUtil.roundVal(angle, 2) + " rotate\n");
}

public void
printPSScale(double scaleval)
{
	this.getPSStrBuf().append(MathUtil.roundVal(scaleval, 2) + " " +
		MathUtil.roundVal(scaleval, 2) + " scale\n");
}

public void
printPSXYScale(double scalexval, double scaleyval)
{
	this.getPSStrBuf().append(MathUtil.roundVal(scalexval, 2) + " " +
		MathUtil.roundVal(scaleyval, 2) + " scale\n");
}

public void
printPSTranslate(double xpos, double ypos)
{
	this.getPSStrBuf().append(MathUtil.roundVal(xpos, 2) + " " +
		MathUtil.roundVal(ypos, 2) + " translate\n");
}

public void
printPSTranslate(Graphics2D g2)
{
	AffineTransform aft = g2.getTransform();
	this.getPSStrBuf().append(MathUtil.roundVal(aft.getTranslateX(), 2) + " " +
		MathUtil.roundVal(aft.getTranslateY(), 2) + " translate\n");
}

public void
psPrintCenterString(double xpos, double ypos, double scale, String str, int font)
{
	setCurrentFont(font);
	printCurrentFont(scale);
	/*
	printCurrentFont(scale * DEFFONTSCALE);
	*/
	printPSGSave();
	this.getPSStrBuf().append("(" + str + ") " + MathUtil.roundVal(xpos, 2) + " " +
		MathUtil.roundVal(ypos, 2) + " getstrmidpt\n");
	printPSString(str, 0.0, 0.0);
	printPSGRestore();
}

public void
printPSImageMatrix(int samplesize, int height)
{
	this.getPSStrBuf().append(samplesize + " " + height + " " + 1 + " [" + samplesize + " " +
		0 + " " + 0 + " " + (-height) + " " + 0 + " " + height + "]\n");
}

public void
printPSImageStart()
{
	this.getPSStrBuf().append("{currentfile picstr readhexstring pop}\n");
	this.getPSStrBuf().append("image\n");
}

public void
printPSImageEnd()
{
	this.getPSStrBuf().append(">}\n");
	this.getPSStrBuf().append("image\n");
}

public void
printPSDefHexString(int size)
{
	this.getPSStrBuf().append("/picstr " + size + " string def\n");
}

public void
printPSNewLine()
{
	this.getPSStrBuf().append("\n");
}

public void
printPSParallelogram(Point2D pt0, Point2D pt1, Point2D pt2, Point2D pt3, Color colorval)
{
	setPrintCurrentPSColorVal(colorval);
	printPSNewPath();
	printPSMoveTo(pt0.getX(), pt0.getY());
	printPSLineTo(pt1.getX(), pt1.getY());
	printPSLineTo(pt2.getX(), pt2.getY());
	printPSLineTo(pt3.getX(), pt3.getY());
	printPSLineTo(pt0.getX(), pt0.getY());
	printPSFill();
}

public void
printPSCenterString(double xpos, double ypos, double scale, String str, int font)
{
	setCurrentFont(font);
	printPSDefFont(scale);
	printPSGSave();
	this.getPSStrBuf().append("(" + str + ") " + MathUtil.roundVal(xpos, 2) + " " +
		MathUtil.roundVal(ypos, 2) + " getstrmidpt\n");
	printPSString(str, 0.0, 0.0);
	printPSGRestore();
}

public void
printPSDefFont(double fontscale)
{
	printBPCurrentFont(DEFFONTSCALE * fontscale);
}

private double lastfontsize = -10.0;
private int lastfont = -1;

public void
printBPCurrentFont(double fontsize)
{
	if ((!MathUtil.precisionEquality(lastfontsize, fontsize, 2.0)) ||
		(lastfont != getCurrentFont()))
	{
		lastfontsize = fontsize;
		lastfont = getCurrentFont();
		this.getPSStrBuf().append("/" + NucFontType + " findfont " +
			MathUtil.roundVal(fontsize, 2) + " scalefont setfont\n");
	}
}

public void
printPSFileNameAndDate()
{
	printPSGRestore();
	printPSGSave();
}

public void
printPSM4String(String m4string)
{
	this.getPSStrBuf().append(m4string + "\n");
}

private final int BGVAL = 100000;

public void
printColorPSBackground(Color bgcolor)
{
	Color savecurrentcolor;

	savecurrentcolor = getCurrentPSColorVal();
	setPrintCurrentPSColorVal(bgcolor);
	printPSNewPath();
	printPSIntMoveTo(-BGVAL, BGVAL);
	printPSIntLineTo(BGVAL, BGVAL);
	printPSIntLineTo(BGVAL, -BGVAL);
	printPSIntLineTo(-BGVAL, -BGVAL);
	printPSIntLineTo(-BGVAL, BGVAL);
	printPSFill();
	printPSStroke();
	setPrintCurrentPSColorVal(savecurrentcolor);
}

public void
reSetAllPSStaticVals()
{
	setCurrentLineWidth(0.0);
	setCmpCurrentLineWidth(-10.0);
	currentpscolorval = Color.black;
	// curcolorval = MAXCOLORVAL + 1;
	curcolorval = Color.white;
	currentlinecap = CAPBUTT;
	printcurlinecap = -1;
	currentfont = HELVETICABOLD;
	lastcurfontsize = -10.0;
	lastcurlinewidth = -10.0;
	lastcurfont = -1;
	arrayval0 = arrayval1 = arrayoffset = 0;
	lastdashval0 = lastdashval1 = lastdashset = -1;
	lastfontsize = -10.0;
	lastfont = -1;
}

public void
drawPSCartesianCoordinateSystem(boolean landscapeModeOn, Color color,
	double scaleVal, Graphics2D g2)
throws Exception
{
	Rectangle2D rect = GraphicsUtil.getLaserPrinterMinusOneInchBoundingBox(landscapeModeOn, scaleVal);
	double minX = rect.getX();
	double minY = rect.getY();
	double maxX = rect.getX() + rect.getWidth();
	double maxY = rect.getY() + rect.getHeight();

	setPrintCurrentPSLineWidth(0.0);
	// setPrintCurrentPSColorVal(color);
	printPSLine(minX, minY, maxX, minY, color.red);
	printPSLine(maxX, minY, maxX, maxY, color.green);
	printPSLine(maxX, maxY, minX, maxY, color.blue);
	printPSLine(minX, maxY, minX, minY, color.cyan);

	// draw axis
	setPrintCurrentDash(0, 0, 0);

	/*
	Line2D.Double line = null;
	line = new Line2D.Double(minX, 0.0, maxX, 0.0);
	g2.draw(line);
	line = new Line2D.Double(0.0, minY, 0.0, maxY);
	g2.draw(line);
	*/
	printPSLine(minX, 0.0, maxX, 0.0, color);
	printPSLine(0.0, minY, 0.0, maxY, color);

	/*
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

	rect = GraphicsUtil.getLaserPrinterBoundingBox(landscapeModeOn, scaleVal);
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
	*/
}

public void
printPSNucChar(String str, double xpos, double ypos)
{
	this.getPSStrBuf().append("(" + str + ") " +
		MathUtil.roundVal(xpos, 2) + " " +
		MathUtil.roundVal(ypos, 2) + " print_nuc_char\n");
}

public void
append(String str)
{
	this.getPSStrBuf().append(str);
}

public String
toString()
{
	return (this.getPSStrBuf().toString());
}

private void
debug(String s)
{
	System.err.println("PostScriptUtil-> " + s);
}

}
