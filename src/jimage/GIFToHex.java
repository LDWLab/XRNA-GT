
package jimage;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class
GIFToHex extends Object
{
	static final int maxBytes = 5000;
	static byte readGifBytes[] = null;
	static String readHexString = null;
	static char[] readHexCharArray = null;
	static boolean globalColorTableFollows = false;
	static Vector fileList = null;
	static int gifByteCount = 0;
	static String imgStringName = null;

	public
	GIFToHex(String fileName)
	throws Exception
	{
		readGifBytes = null;
		readHexString = null;
		readHexCharArray = null;
		globalColorTableFollows = false;
		gifByteCount = 0;
		imgStringName = null;

		// System.out.println("IN GIFToHex: " + fileName);
		readGifBytes = new byte[maxBytes];

		FileInputStream in = null;
		FileOutputStream out = null;

		try
		{
			in = new FileInputStream(fileName);
		}
		catch (FileNotFoundException fnfe)
		{
			System.out.println("file not found: " + fileName);
			return;
		}

		/*SAVE for out
		try
		{
			out = new FileOutputStream("out.gif");
		}
		catch (IOException ioe)
		{
			System.out.println("no write: " + "out.gif");
		}
		*/

		imgStringName = fileName.substring(0, fileName.indexOf(".gif"));

		// to read in a gif image into a byte array:
		try
		{
			gifByteCount = in.read(readGifBytes, 0, maxBytes);
			// System.out.println("bytes read: " + gifByteCount);

			if (gifByteCount > maxBytes)
			{
				throw new Exception("gifByteCount > maxBytes");
			}

			// System.out.println("bytes read: " + gifByteCount);

		}
		catch (IOException ioe)
		{
			System.out.println("NO GO on reading");
		}

		/* SAVE for rewriting gif file to check
		try
		{
			out.write(gifBytes);
		}
		catch (IOException ioe)
		{
			System.out.println("can't write");
		}
		*/


		try
		{
			in.close();
			// out.close();
		}
		catch (IOException ioe)
		{
			System.out.println("can't close");
		}

	}

	public int
	gifByteCount()
	{
		return (gifByteCount);
	}

	public void
	printHexChars(int mod)
	{
		System.out.println("public static String " + imgStringName +
			"ImgString" + " =");
		System.out.print((char)0x22);
		for (int i = 1;i < (2 * gifByteCount) + 1;i++)
		{
			System.out.print(readHexCharArray[i - 1]);

			if ((i != 0) && (i % mod == 0))
			{
				System.out.println((char)0x22 + " +");
				System.out.print((char)0x22);
			}
		}
		System.out.println((char)0x22 + ";");
		System.out.println();
	}

	public void
	printHeader(byte[] gifBytes)
	{
		System.out.print("GIF HEADER: ");
		for(int i = 0;i < 6;i++)
			System.out.print((new Character((char)gifBytes[i])).toString());
		System.out.println();
	}

	public void
	printGIFWidth(char[] hexCharArray)
	{
		char[] wordArray = new char[4];

		wordArray[0] = hexCharArray[14];
		wordArray[1] = hexCharArray[15];
		wordArray[2] = hexCharArray[12];
		wordArray[3] = hexCharArray[13];
		System.out.println("WIDTH: " + (new String(wordArray)));
	}

	public void
	printGIFHeight(char[] hexCharArray)
	{
		char[] wordArray = new char[4];

		wordArray[0] = hexCharArray[18];
		wordArray[1] = hexCharArray[19];
		wordArray[2] = hexCharArray[16];
		wordArray[3] = hexCharArray[17];
		System.out.println("HEIGHT: " + (new String(wordArray)));
	}

	public void
	printGIFColorTableInfo(byte[] gifBytes, char[] hexCharArray)
	{
		char[] hexByte = new char[2];

		hexByte[0] = hexCharArray[20];
		hexByte[1] = hexCharArray[21];
		System.out.println("COLOR TABLE INFO: " + hexByte);
		//System.out.println("COLOR TABLE FOLLOWS: " + (gifBytes[10] & 0x80));
		globalColorTableFollows = (gifBytes[10] & 0x80) == 0x80;
		System.out.println("COLOR TABLE FOLLOWS: " + globalColorTableFollows);
		int bitsPerPixel = ((gifBytes[10] & 0x70) >> 4) + 1;
		System.out.println("BITS PER PIXEL: " + bitsPerPixel);
		boolean colorTableSorted = ((gifBytes[10] & 0x08) == 0x08);
		System.out.println("COLOR TABLE SORTED: " + colorTableSorted);

		/*
		** Size of the color table by the formula bytes = 2 raised to
		** (bit6to8 + 1) * 3.
		*/

		int exponent = (int)(gifBytes[10] & 0x07) + 1;
		int colorTableSize = 1;
		for(int i = 0;i < exponent;i++)
			colorTableSize *= 2;
		colorTableSize *= 3;
		System.out.println("COLOR TABLE SIZE: " + colorTableSize);

		int bgColorIndex = gifBytes[11];
		System.out.println("BACKGROUND COLOR PIXEL INDEX: " + bgColorIndex);

		double pixelAspectRatio = (double)gifBytes[12];
		if (pixelAspectRatio > 0.0)
			pixelAspectRatio = 0.25 + (pixelAspectRatio / 64.0);
		System.out.println("PIXEL ASPECT RATIO: " + (new Float(pixelAspectRatio)).toString());
	}

	public void
	printGIFBGColorIndex(char[] hexCharArray)
	{
		System.out.println("BACKGROUND COLOR INDEX: " + hexCharArray[22]);
	}

	public void
	printGIFColorTableRGB(char[] hexCharArray)
	{
		System.out.println("GLOBAL COLOR TABLE RGB: " +
			hexCharArray[26] + hexCharArray[27] + " " +
			hexCharArray[28] + hexCharArray[29] + " " +
			hexCharArray[30] + hexCharArray[31]);
	}

    public static void
    main(String[] args)
    {
		GIFToHex GIFConverter = null;
		String options = null;
		boolean printGIFHeader = false;

		if ((args == null) || (args.length == 0))
		{
			System.out.println("NO ARGS");
			return;
		}

		int argsLength = args.length;
		int startFileIndex = 0;

		if (args[0].startsWith("-"))
		{
			options = args[0].substring(1, args[0].length());
			// System.out.println("options.substring:" + options.substring(1, options.length()) + ":");
			if (options.substring(0, 1).equals("h"))
				printGIFHeader = true;
			startFileIndex = 1;
		}

		fileList = new Vector();

		for (int i = startFileIndex;i < argsLength;i++)
		{
			fileList.addElement(args[i]);
		}


		for (int i = 0;i < fileList.size();i++)
		{
			try
			{
				GIFConverter = new GIFToHex((String)fileList.elementAt(i));
			}
			catch (Exception e)
			{
				System.out.println("NO GO");
				continue;
			}

			if (printGIFHeader)
			{
				GIFConverter.printHeader(readGifBytes);
				GIFConverter.printGIFWidth(readHexCharArray);
				GIFConverter.printGIFHeight(readHexCharArray);
				GIFConverter.printGIFColorTableInfo(
					readGifBytes, readHexCharArray);
				if (globalColorTableFollows)
					GIFConverter.printGIFColorTableRGB(readHexCharArray);
			}
			else
			{
				GIFConverter.printHexChars(70);
			}

		}
    }

/*
static String moneyGifImgString =
"47494638396120001C00B308000000008000000080008080000000808000" +
"80008080808080C0C0C0FF000000FF00FFFF000000FFFF00FF00FFFFFFFF" +
"FF21F90401000008002C0000000020001C000004E050C849ABBD2823A944" +
"BF20E70980B67580A7A4C0EA026DACA05EA949C0D1CEFC0AF7409E2D533B" +
"848E4892E893A47468AA516AC67AF9ACB4D5E803784CADB0DD6E46F22A71" +
"353310A6DBA1CBACE2E4EB5A211E8871EF9C2219B7545E2B07843984872D" +
"528A5C42545D8478263A1C7D027F2441769126196D3861535D7A8E3A070F" +
"0F876D82431B703E6361086D613A66AC8B15AD969D883035264D12B2A039" +
"95ACC4C8A007BC85C49CCFD09DC43AD192A9CFCBB288D51AD91AACD2A0DC" +
"DDA8DBDA87E3D1B336DE19E0E9EAE585F0EAA9EBF4D0ED79F8DCA6A9EFFC" +
"2489C31701003B0000000000000000000000000000000000000000000000";
*/

}
