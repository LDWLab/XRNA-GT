package util;

import java.awt.Font;

import util.math.MathUtil;

public class
StringUtil
{

public static String
roundStrVal(double val, int decimalPlace)
{
	/*
	double pow = Math.pow(10.0, (double)decimalPlace);
	return(String.valueOf((double)Math.round(val * pow)/pow));
	*/
	return (String.valueOf(MathUtil.roundVal(val, decimalPlace)));
}

public static String
roundStrVal(double val)
{
	return (roundStrVal(val, 2));
}

public static String
truncateVal(double dVal, int decimalPlace)
{
	String strVal = String.valueOf(dVal);
	int strValLength = strVal.length();
	int i = strVal.indexOf('.') + decimalPlace + 1;
	if (i > strValLength)
	{
		for (int j = 0;j < i - strValLength;j++)
			strVal = strVal.toString() + "0";
	}
	return (strVal.substring(0, i));
}

public static Font
ssFontToFont(int nucFontType, int nucFontSize)
{
	switch (nucFontType)
	{
	  case 0 :	// HELVETICA
	  	return (new Font("Helvetica", Font.PLAIN, nucFontSize));
	  case 1 :	// HELVETICAOBLIQUE ?? ITALIC??
	  	return (new Font("Helvetica", Font.ITALIC, nucFontSize));
	  case 2 :	// HELVETICABOLD
	  	return (new Font("Helvetica", Font.BOLD, nucFontSize));
	  case 3 :	// HELVETICABOLDOBLIQUE
	  	return (new Font("Helvetica", Font.BOLD + Font.ITALIC, nucFontSize));
	  case 4 :	// TIMESROMAN
	  	return (new Font("TimesRoman", Font.PLAIN, nucFontSize));
	  case 5 :	// TIMESITALIC
	  	return (new Font("TimesRoman", Font.ITALIC, nucFontSize));
	  case 6 :	// TIMESBOLD
	  	return (new Font("TimesRoman", Font.BOLD, nucFontSize));
	  case 7 :	// TIMESBOLDITALIC
	  	return (new Font("TimesRoman", Font.BOLD + Font.ITALIC, nucFontSize));
	  case 8 :	// COURIER
	  	return (new Font("Courier", Font.PLAIN, nucFontSize));
	  case 9 :	// COURIEROBLIQUE
	  	return (new Font("Courier", Font.ITALIC, nucFontSize));
	  case 10 :	// COURIERBOLD
	  	return (new Font("Courier", Font.BOLD, nucFontSize));
	  case 11 :	// COURIERBOLDOBLIQUE
	  	return (new Font("Courier", Font.BOLD + Font.ITALIC, nucFontSize));
	  case 12 :	// ADOBESYMBOL
	  	return (new Font("TimesRoman", Font.PLAIN, nucFontSize));
	  case 13 :	// DIALOG
	  	return (new Font("Dialog", Font.PLAIN, nucFontSize));
	  case 14 :	// DIALOGOBLIQUE
	  	return (new Font("Dialog", Font.ITALIC, nucFontSize));
	  case 15 :	// DIALOGBOLD
	  	return (new Font("Dialog", Font.BOLD, nucFontSize));
	  case 16 :	// DIALOGBOLDOBLIQUE
	  	return (new Font("Dialog", Font.BOLD + Font.ITALIC, nucFontSize));
	  case 17 :	// DIALOGINPUT
	  	return (new Font("DialogInput", Font.PLAIN, nucFontSize));
	  case 18 :	// DIALOGINPUTOBLIQUE
	  	return (new Font("DialogInput", Font.ITALIC, nucFontSize));
	  case 19 :	// DIALOGINPUTBOLD
	  	return (new Font("DialogInput", Font.BOLD, nucFontSize));
	  case 20 :	// DIALOGINPUTBOLDOBLIQUE
	  	return (new Font("DialogInput", Font.BOLD + Font.ITALIC, nucFontSize));
	  default : // HELVETICA
	  	return (new Font("Helvetica", Font.PLAIN, nucFontSize));
	}
}

public static int
fontToFontID(Font font)
throws Exception
{
	String fontName = font.getName();
	int fontStyle = font.getStyle();

	if (
		fontName.toLowerCase().equals("helvetica") ||
		fontName.toLowerCase().equals("arial") ||
		fontName.toLowerCase().equals("sansserif") ||
		fontName.toLowerCase().equals("helvetica-light")
		)
	{
		if (fontStyle == Font.PLAIN)
			return(0);
		if (fontStyle == Font.ITALIC)
			return(1);
		if (fontStyle == Font.BOLD)
			return(2);
		if (fontStyle == Font.BOLD + Font.ITALIC)
			return(3);
	}
	else if (fontName.toLowerCase().equals("timesroman"))
	{
		if (fontStyle == Font.PLAIN)
			return(4);
		if (fontStyle == Font.ITALIC)
			return(5);
		if (fontStyle == Font.BOLD)
			return(6);
		if (fontStyle == Font.BOLD + Font.ITALIC)
			return(7);
	}
	else if (fontName.toLowerCase().equals("courier"))
	{
		if (fontStyle == Font.PLAIN)
			return(8);
		if (fontStyle == Font.ITALIC)
			return(9);
		if (fontStyle == Font.BOLD)
			return(10);
		if (fontStyle == Font.BOLD + Font.ITALIC)
			return(11);
	}
	else if (fontName.toLowerCase().equals("adobesymbol"))
	{
		return(12);
	}
	else if (fontName.toLowerCase().equals("dialog"))
	{
		if (fontStyle == Font.PLAIN)
			return(13);
		if (fontStyle == Font.ITALIC)
			return(14);
		if (fontStyle == Font.BOLD)
			return(15);
		if (fontStyle == Font.BOLD + Font.ITALIC)
			return(16);
	}
	else if (fontName.toLowerCase().equals("dialoginput"))
	{
		if (fontStyle == Font.PLAIN)
			return(17);
		if (fontStyle == Font.ITALIC)
			return(18);
		if (fontStyle == Font.BOLD)
			return(19);
		if (fontStyle == Font.BOLD + Font.ITALIC)
			return(20);
	}
	else
	{
		throw new Exception(
			"\nERROR in ComplexCollection.fontToFontID(): " + " " +
			fontName + " not represented\n");
	}
	
	return (-1);
}

private static void
debug(String s)
{
	System.out.println("StringUtil-> " + s);
}

}
