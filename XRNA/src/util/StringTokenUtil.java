package util;

import java.io.*;
import java.util.StringTokenizer;

public class
StringTokenUtil
{

public static int
getNextIntToken(String nucLine, StreamTokenizer streamT)
throws Exception
{
	if (streamT.nextToken() != StreamTokenizer.TT_NUMBER) 
	{
		StringBuffer buf = new StringBuffer();
		buf.append("Error trying to parse:");
		buf.append("\n");
		buf.append(nucLine);
		buf.append("\n");
		buf.append("expected int, got: " + streamT.sval);
		buf.append("\n");
		throw new Exception(buf.toString());
	}
	return((int)streamT.nval);
}

public static float
getNextFloatToken(String nucLine, StreamTokenizer streamT)
throws Exception
{
	if(streamT.nextToken() != StreamTokenizer.TT_NUMBER)
	{
		StringBuffer buf = new StringBuffer();
		buf.append("Error trying to parse:");
		buf.append("\n");
		buf.append(nucLine);
		buf.append("\n");
		buf.append("expected float, got: " + streamT.sval);
		buf.append("\n");
		throw new Exception(buf.toString());
	}
	return((float)streamT.nval);
}

public static double
getNextDoubleToken(String nucLine, StreamTokenizer streamT)
throws Exception
{
	if(streamT.nextToken() != StreamTokenizer.TT_NUMBER)
	{
		StringBuffer buf = new StringBuffer();
		buf.append("Error trying to parse:");
		buf.append("\n");
		buf.append(nucLine);
		buf.append("\n");
		buf.append("expected float, got: " + streamT.sval);
		buf.append("\n");
		throw new Exception(buf.toString());
	}
	return(streamT.nval);
}

public static String
getNextStringToken(String nucLine, StreamTokenizer streamT)
throws Exception
{
	if (streamT.nextToken() != StreamTokenizer.TT_WORD) 
	{
		StringBuffer buf = new StringBuffer();
		buf.append("Error trying to parse:");
		buf.append("\n");
		buf.append(nucLine);
		buf.append("\n");
		buf.append("expected char, got: " + streamT.sval);
		buf.append("\n");
		throw new Exception(buf.toString());
	}
	return (streamT.sval);
}

public static String
getNextHexStringToken(String nucLine, StreamTokenizer streamT)
throws Exception
{
	int tokenType = streamT.nextToken();
	return (streamT.toString());
}

public static char
getNextCharToken(String nucLine, StreamTokenizer streamT)
throws Exception
{
	StringBuffer buf = null;

	if (streamT.nextToken() != StreamTokenizer.TT_WORD) 
	{
		buf = new StringBuffer();
		buf.append("Error trying to parse:");
		buf.append("\n");
		buf.append(nucLine);
		buf.append("\n");
		buf.append("expected char, got: " + streamT.sval);
		buf.append("\n");
		throw new Exception(buf.toString());
	}
	if (streamT.sval.length() != 1)
	{
		buf = new StringBuffer();
		buf.append("Error trying to parse:");
		buf.append("\n");
		buf.append(nucLine);
		buf.append("\n");
		buf.append("expected char of length 1, got: " + streamT.sval);
		buf.append("\n");
		throw new Exception(buf.toString());
	}
	return (streamT.sval.charAt(0));
}

}
