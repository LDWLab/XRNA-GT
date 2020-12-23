package ssview;

import java.awt.*;
import java.io.*;
import java.awt.geom.*;
import java.util.*;

import jimage.DrawObject;
import jimage.DrawArrowObject;
import jimage.DrawCircleObject;
import jimage.DrawLineObject;
import jimage.DrawParallelogramObject;
import jimage.DrawStringObject;
import jimage.DrawTriangleObject;

import util.*;
import util.math.*;

public class
ComplexAlignment
extends Hashtable
{

public static final int MASE_INPUT = 0;
public static final int GENBANK_INPUT = 1;
int[][] seqNumArray = null;

public
ComplexAlignment()
{
	super();
}

public
ComplexAlignment(String alignmentFileName)
throws Exception
{
	this (alignmentFileName, MASE_INPUT);
}

public
ComplexAlignment(String alignmentFileName, int inputType)
throws Exception
{
	this();

	this.setInputType(inputType);

	File alignmentFile = new File(alignmentFileName);
	if (!alignmentFile.exists())
	{
		debug("Error: alignment file, " + alignmentFileName + ", does not exist");
		return;
	}
	FileReader fileReader = new FileReader(alignmentFileName);

	StringWriter alignmentWriter = new StringWriter();

	int maxChars = 10000;
	char cArray[] = new char[maxChars];
	for (int b = fileReader.read(cArray, 0, maxChars);b != -1;b = fileReader.read(cArray, 0, maxChars))
	{
		alignmentWriter.write(cArray, 0, b);
	}
	this.setNameList(new Vector());
	if (inputType == MASE_INPUT)
	{
		if (!this.parseMaseAlignment(alignmentWriter.toString()))
		{
			debug("Unsuccessful parse of alignment file: " + alignmentFileName);
			return;
		}
	}
	else if (inputType == GENBANK_INPUT)
	{
		if (!this.parseGenBankAlignment(alignmentWriter.toString()))
		{
			debug("Unsuccessful parse of alignment file: " + alignmentFileName);
			return;
		}
	}
	else
	{
		throw new Exception("UNKNOWN INPUT TYPE: " + inputType);
	}
	

	/*
	for (Enumeration e = this.keys() ; e.hasMoreElements() ;)
	{
		String seqName = (String)e.nextElement();
		System.out.println(seqName);
		Vector seqInfo = (Vector)this.get(seqName);
		for (int i = 0;i < seqInfo.size();i++)
		{
			Character c = (Character)seqInfo.elementAt(i);
			System.out.print(c);
		}
		System.out.println();
	}
	*/

	this.setSeqNumArray();
	this.setAlignmentSet(true);

	/*
	debug("seqNumArray.length: " + seqNumArray.length);
	for (int row = 0;row < seqNumArray.length;row++)
	{
		int rowInfo[] = seqNumArray[row];
		for (int col = 0;col < rowInfo.length;col++)
		{
			System.out.print(rowInfo[col] + ",");
		}
		System.out.println();
	}
	*/
}

private Vector nameList = null;

public void
setNameList(Vector nameList)
{
    this.nameList = nameList;
}

public Vector
getNameList()
{
    return (this.nameList);
}
private boolean alignmentSet = false;

public void
setAlignmentSet(boolean alignmentSet)
{
    this.alignmentSet = alignmentSet;
}

public boolean
getAlignmentSet()
{
    return (this.alignmentSet);
}

public void
setSeqNumArray()
{
	int maxSize = 0;
	for (Enumeration e = this.keys();e.hasMoreElements();)
	{
		String seqName = (String)e.nextElement();
		Vector seqInfo = (Vector)this.get(seqName);
		maxSize = seqInfo.size();
		break;
	}

	seqNumArray = new int[this.size()][maxSize];
	for (int row = 0;row < this.size();row++)
	for (int col = 0;col < maxSize;col++)
		seqNumArray[row][col] = 0;

	int row = 0;
	for (Enumeration e = this.keys();e.hasMoreElements();)
	{
		String seqName = (String)e.nextElement();
		Vector seqInfo = (Vector)this.get(seqName);
		int seqNumCount = 0;
		int[] templateArray = seqNumArray[row++];
		for (int col = 0;col < maxSize;col++)
		{
			char nucChar = ((Character)seqInfo.elementAt(col)).charValue();
			if (NucNode.isValidNucChar(Character.toUpperCase(nucChar)))
				templateArray[col] = ++seqNumCount;
		}
		// debug("SEQNUMCOUNT FOR SEQNAME " + seqName + " : " + seqNumCount);
	}
}

public void
printAlignment(String fileName)
throws Exception
{
	File outFile = new File(fileName);
	StringWriter strWriter = null;
	PrintWriter printWriter = null;
	try
	{
		strWriter = new StringWriter();
		printWriter = new PrintWriter(strWriter);
		/*
		for (int nucID = 1;nucID <= this.getNucCount();nucID++)
		{
			Nuc2D nuc = this.getNuc2DAt(nucID);
			if (nuc == null)
				continue;
			printWriter.println(
				"assert(MathUtil.precisionEquality(sstr.getNuc2DAt(" +
				nucID + ").getX(), " + nuc.getX() + "));");
			printWriter.println(
				"assert(MathUtil.precisionEquality(sstr.getNuc2DAt(" +
				nucID + ").getY(), " + nuc.getY() + "));");
		}
		*/

		for (Enumeration e = this.keys();e.hasMoreElements();)
		{
			String seqName = (String)e.nextElement();
			Vector seqInfo = (Vector)this.get(seqName);
			for (int col = 0;col < seqInfo.size();col++)
			{
				char nucChar = ((Character)seqInfo.elementAt(col)).charValue();
				printWriter.print(nucChar);
			}
			printWriter.println();
		}
	}
	catch (Exception e)
	{
		debug("Error, file not written:\n" + e.toString());
		throw e;
	}
	try
	{
		outFile.createNewFile();
		FileWriter genFileWriter = new FileWriter(outFile);
		PrintWriter pWriter = new PrintWriter(
			new BufferedWriter(genFileWriter), true);
		pWriter.print(strWriter.toString());
		pWriter.flush();
		pWriter.close();
	}
	catch (Exception e)
	{
		throw e;
	}
}

public void
printPrimaryStructure(String lookUpName)
{
	Vector seqInfo = (Vector)this.get(lookUpName);
	for (int i = 0;i < seqInfo.size();i++)
	{
		char nucChar = ((Character)seqInfo.elementAt(i)).charValue();
		if (NucNode.isValidNucChar(Character.toUpperCase(nucChar)))
			System.out.print(nucChar);
	}
	System.out.println();
}

public void
printNucStats(String lookUpName)
{
	int aCount = 0;
	int uCount = 0;
	int gCount = 0;
	int cCount = 0;
	int otherCount = 0;
	Vector seqInfo = (Vector)this.get(lookUpName);
	for (int i = 0;i < seqInfo.size();i++)
	{
		char nucChar = Character.toUpperCase(((Character)seqInfo.elementAt(i)).charValue());
		if (!NucNode.isValidNucChar(nucChar))
			continue;
		if (nucChar == 'A')
			aCount++;
		else if (nucChar == 'U')
			uCount++;
		else if (nucChar == 'G')
			gCount++;
		else if (nucChar == 'C')
			cCount++;
		else
			otherCount++;
	}
	System.out.println("aCount: " + aCount);
	System.out.println("uCount: " + uCount);
	System.out.println("gCount: " + gCount);
	System.out.println("cCount: " + cCount);
	System.out.println("others: " + otherCount);
	System.out.println("total: " + (aCount + uCount + gCount + cCount + otherCount));
}

public int
seqIndex(String lookUpName)
{
	int index = 0;
	for (Enumeration e = this.keys();e.hasMoreElements();)
	{
		String seqName = (String)e.nextElement();
		if (seqName.equals(lookUpName))
			return (index);
		index++;
	}
	return (-1);
}

public int
seqNucToSeqArrayIndex(String lookUpName, int seqNucID)
{
	int seqID = seqIndex(lookUpName);

	int[] seqNumLine = seqNumArray[seqID];
	for (int i = seqNucID-1;i < seqNumLine.length;i++)
		if (seqNumLine[i] == seqNucID)
			return(i);
	return(0);
}

public SSData2D
mapSequence(SSData2D templateSStr, String mappedName)
throws Exception
{
	return (this.mapSequence(templateSStr, templateSStr.getName(), mappedName));
}

public SSData2D
mapSequence(SSData2D templateSStr, String templateName, String mappedName)
throws Exception
{
	Vector templateInfo = (Vector)this.get(templateName);
	Vector mappedInfo = (Vector)this.get(mappedName);
	int templateSeqID = this.seqIndex(templateName);
	int mappedSeqID = this.seqIndex(mappedName);
	int[] templateSeqNums = this.seqNumArray[templateSeqID];
	int[] mappedSeqNums = this.seqNumArray[mappedSeqID];
	SSData2D mappedSStr = new SSData2D();
	mappedSStr.setName(mappedName);

	/* SVE for testing gb file having too many nucs
	debug("templateSStr.nucCount: " + templateSStr.getNucCount());
	debug("seqNumArray[0] at 5000: " + seqNumArray[0][5000]);
	debug("seqNumArray[1] at 5000: " + seqNumArray[1][5000]);
	debug("templateSeqNums at 5000: " + templateSeqNums[5000]);
	debug("mappedSeqNums at 5000: " + mappedSeqNums[5000]);
	*/

	// debug("MAX: " + templateSeqNums.length);
	// first map all nucs from template to an aligned nuc if it exists
	for (int arrayIndex = 0;arrayIndex < templateSeqNums.length;arrayIndex++)
	{
		int templateNucID = templateSeqNums[arrayIndex];
		if (templateNucID <= 0)
			continue;
		/* SVE for testing gb file having too many nucs
		if (arrayIndex == 5000) debug("TEMPLATENUCID: " + templateNucID);
		*/
		int mappedNucID = mappedSeqNums[arrayIndex];
		/* SVE for testing gb file having too many nucs
		if (arrayIndex == 5000) debug("MAPPEDNUCID: " + mappedNucID);
		*/
		if (mappedNucID <= 0)
			continue;
		// check to see if goes beyond what is in rna strand in xrna input file
		Nuc2D templateNuc = templateSStr.getNuc2DAt(templateNucID);
		/* SVE for testing gb file having too many nucs
		if (arrayIndex == 5000) debug("TEMPLATENUC: " + templateNuc);
		*/
		if (templateNuc == null)
			continue;
		// make a copy of templateNuc into newNuc
		Nuc2D newNuc = new Nuc2D(templateNuc);

		// debug("SETTING newNuc to: " + mappedNucID + " at arrayid: " + arrayIndex);
		newNuc.setID(mappedNucID);
		newNuc.setNucChar(Character.toUpperCase(((Character)mappedInfo.elementAt(arrayIndex)).charValue()));
		newNuc.setBasePairID(0);
		newNuc.setBasePair(null);
		if (newNuc.getNucChar() == templateNuc.getNucChar())
			newNuc.setColor(Color.red);
		else
			newNuc.setColor(Color.blue);
		if (this.getIncludeNucLabels() && templateNuc.hasNucLabel())
		{
			DrawLineObject lineObj = templateNuc.getLineLabel();
			if (lineObj != null)
				newNuc.addLabel(new DrawLineObject((DrawLineObject)lineObj));
			DrawStringObject stringObj = templateNuc.getNumberLabel();
			if (stringObj != null)
				newNuc.addLabel(new DrawStringObject((DrawStringObject)stringObj));
		}
		mappedSStr.addNuc(newNuc);
	}

	// next set base pair info
	for (int arrayIndex = 0;arrayIndex < templateSeqNums.length;arrayIndex++)
	{
		int templateNucID = templateSeqNums[arrayIndex];
		if (templateNucID <= 0)
			continue;
		int mappedNucID = mappedSeqNums[arrayIndex];
		if (mappedNucID <= 0)
			continue;
		// check to see if goes beyond what is in rna strand in xrna input file
		Nuc2D templateNuc = templateSStr.getNuc2DAt(templateNucID);
		if (templateNuc == null)
			continue;

		Nuc2D bpNuc = templateSStr.getNuc2DAt(templateNucID);
		if (!bpNuc.isBasePair())
			continue;
		if (!bpNuc.isSelfRefBasePair())
			continue;
		// debug("LOOKING AT BP: " + bpNuc);
		
		int bpArrayIndex = this.seqNucToSeqArrayIndex(templateName, bpNuc.getBasePair().getID());
		if ((bpArrayIndex > 0) && (mappedSeqNums[bpArrayIndex] > 0))
		{
			Nuc2D mappedNuc = mappedSStr.getNuc2DAt(mappedNucID);
			// debug("mappedSeqNums[bpArrayIndex]: " + mappedSeqNums[bpArrayIndex]);
			Nuc2D mappedBPNuc = mappedSStr.getNuc2DAt(mappedSeqNums[bpArrayIndex]);
			if (mappedBPNuc != null)
			{
				mappedNuc.setBasePairID(mappedBPNuc.getID());
				mappedNuc.setBasePair(mappedBPNuc.getBasePair());
				// debug("RESETING: " + mappedNuc.getID());
				mappedNuc.resetBasePair();
			}
		}
	}

	if (this.getIncludeExtraneousLabels())
	{
		if (templateSStr.getLabelList() != null)
		{
			for (Enumeration e = templateSStr.getLabelList().elements();e.hasMoreElements();)
			{
				DrawObject drwObj = (DrawObject)e.nextElement();

				if (drwObj instanceof DrawArrowObject)
					mappedSStr.addLabel(new DrawArrowObject((DrawArrowObject)drwObj));
				else if (drwObj instanceof DrawCircleObject)
					mappedSStr.addLabel(new DrawCircleObject((DrawCircleObject)drwObj));
				else if (drwObj instanceof DrawLineObject)
					mappedSStr.addLabel(new DrawLineObject((DrawLineObject)drwObj));
				else if (drwObj instanceof DrawParallelogramObject)
					mappedSStr.addLabel(new DrawParallelogramObject((DrawParallelogramObject)drwObj));
				else if (drwObj instanceof DrawStringObject)
					mappedSStr.addLabel(new DrawStringObject((DrawStringObject)drwObj));
				else if (drwObj instanceof DrawTriangleObject)
					mappedSStr.addLabel(new DrawTriangleObject((DrawTriangleObject)drwObj));
			}
		}
	}

	// center before adding leftover nucs
	mappedSStr.centerAtOrigin();

	/* SVE for testing gb file having too many nucs
	if (mappedSStr.getNucAt(299) == null)
		debug("299 is NULL");
	*/

	// deal with left over nucs in mappedSStr

	for (int arrayIndex = 0;arrayIndex < mappedSeqNums.length;arrayIndex++)
	{
		int mappedNucID = mappedSeqNums[arrayIndex];
		if (mappedNucID <= 0)
			continue;
		int templateNucID = templateSeqNums[arrayIndex];
		if (templateNucID > 0)
			continue;
		/*
		// check to see if goes beyond what is in rna strand in xrna input file
		Nuc2D templateNuc = templateSStr.getNuc2DAt(templateNucID);
		if (templateNuc == null)
			continue;
		*/
		mappedSStr.addNuc(new Nuc2D(Character.toUpperCase(((Character)mappedInfo.elementAt(arrayIndex)).charValue()), mappedNucID));
		/*
		debug("Adding nuc at: " + mappedNucID + " " + arrayIndex);
		*/
	}

	// check for consistency
	for (int nucID = 1;nucID < mappedSStr.getNucCount();nucID++)
	{
		Nuc2D nuc = mappedSStr.getNuc2DAt(nucID);
		if (nuc == null)
		{
			System.out.println("Error: nuc in new sequence at " + nucID + " is null");
			// NEW 04/25/04:
			/*
			return (null);
			*/
		}
		// NEW 04/25/04: (adding (nuc != null)
		if ((nuc != null) && (nuc.getParentSSData2D() == null))
			nuc.setParentCollection(mappedSStr);
	}

	// deal with new basepair conformations. try going through all
	// helices and reformatting in place.
	// (NEED to make this an option).
	//
	// NEED to be able to override these distances:
	//
	// NEED to be able to have the option of just doing helices and
	// not hairpin loops; just doing helices with hairpin loops;
	// doing all loops; etc.
	mappedSStr.setDistancesFromCollection(templateSStr);
	for (int nucID = 1;nucID < mappedSStr.getNucCount();nucID++)
	{
		Nuc2D nuc = mappedSStr.getNuc2DAt(nucID);

		// NEW 04/25/04:
		if (nuc == null)
			continue;

		if (!nuc.isHelixStart())
			continue;
		RNAHelix2D helix = new RNAHelix2D(nuc);
		helix.reformat();
	}

	// these maybe should depend on font size used
	double xJump = 8.0;
	double yJump = 12.0;
	double startX = mappedSStr.getLargestXVal() + 20.0;
	double startY = mappedSStr.getLargestYVal() - 20.0;
	int runLength = 1;
	boolean inFormatted = true;
	for (int nucID = 1;nucID < mappedSStr.getNucCount();nucID++)
	{
		if (inFormatted)
		{
			runLength = 1;
		}
		else
		{
			runLength++;
		}
		Nuc2D nuc = mappedSStr.getNuc2DAt(nucID);

		// NEW 04/25/04:
		if (nuc == null)
			continue;

		if (nuc.isFormatted())
		{
			inFormatted = true;
			continue;
		}
		else
		{
			if (inFormatted)
				startY -= yJump;
			inFormatted = false;
		}
		nuc.setX(startX + ((double)runLength * xJump));
		nuc.setY(startY);
	}

	mappedSStr.centerAtOrigin();

	return (mappedSStr);
}

public boolean
parseMaseAlignment(String alignment)
throws Exception
{
	StringTokenizer st = new StringTokenizer(alignment, "\n");
	boolean inComments = false;
	boolean newSequence = false;
	Vector currentSeqInfo = null;
	while (st.hasMoreTokens())
	{
		String fileLine = st.nextToken();
		if (fileLine.trim().startsWith(";"))
		{
			inComments = true;
			newSequence = false;
			continue;
		}
		if (inComments && !newSequence)
		{
			inComments = false;
			newSequence = true;
			currentSeqInfo = new Vector();
			this.put(fileLine.trim(), currentSeqInfo);
			this.getNameList().add(fileLine.trim());
			continue;
		}
		fileLine = fileLine.trim();
		// everything from here on should be seq info
		for (int i = 0;i < fileLine.length();i++)
		{
			char alignmentChar = fileLine.charAt(i);
			
			// FOR now allow any char as it seems like all kinds a chars are
			// being used for gap chars.
			/*
			if (NucNode.isValidNucChar(Character.toUpperCase(alignmentChar)) ||
				this.isValidGapChar(alignmentChar))
			{
				currentSeqInfo.add(new Character(Character.toUpperCase(alignmentChar)));
				continue;
			}
			*/

			if (currentSeqInfo == null)
			{
				System.out.println("Invalid Sequence; does each sequence in mase file start with a ';'?");
				return (false);
			}

			currentSeqInfo.add(new Character(Character.toUpperCase(alignmentChar)));

			/*
			System.out.println("Invalid nuc char:" + alignmentChar + ":");
			return (false);
			*/
		}
	}

	int maxSize = 0;
	for (Enumeration e = this.keys();e.hasMoreElements();)
	{
		String seqName = (String)e.nextElement();
		Vector seqInfo = (Vector)this.get(seqName);
		if (maxSize < seqInfo.size())
			maxSize = seqInfo.size();
	}
	for (Enumeration e = this.keys();e.hasMoreElements();)
	{
		String seqName = (String)e.nextElement();
		Vector seqInfo = (Vector)this.get(seqName);
		for (int i = seqInfo.size();i < maxSize;i++)
			seqInfo.add(new Character('-'));
	}
	return (true);
}

public boolean
parseGenBankAlignment(String alignment)
throws Exception
{
	StringTokenizer alignmentST = new StringTokenizer(alignment, "\n");
	StringTokenizer fileLineST = null;
	boolean inComments = true;
	boolean newSequence = true;
	boolean startOfFile = true;
	Vector currentSeqInfo = null;
	String currentSeqName = null;
	while (alignmentST.hasMoreTokens())
	{
		String fileLine = alignmentST.nextToken();
		if (startOfFile || fileLine.trim().startsWith("//"))
		{
			inComments = true;
			newSequence = true;
			startOfFile = false;
			if (fileLine.startsWith("LOCUS"))
			{
				fileLineST = new StringTokenizer(fileLine.trim());
				fileLineST.nextToken();
				currentSeqInfo = new Vector();
				currentSeqName = fileLineST.nextToken();
				this.put(currentSeqName, currentSeqInfo);
				this.getNameList().add(currentSeqName);
			}
			continue;
		}

		if (fileLine.startsWith("LOCUS"))
		{
			fileLineST = new StringTokenizer(fileLine.trim());
			fileLineST.nextToken();
			currentSeqInfo = new Vector();
			currentSeqName = fileLineST.nextToken();
			this.put(currentSeqName, currentSeqInfo);
			this.getNameList().add(currentSeqName);
			continue;
		}

		if (fileLine.startsWith("ORIGIN"))
			inComments = false;

		if (inComments)
			continue;

		// everything from here on should be seq info

		fileLine = fileLine.trim();
		if (fileLine.length() <= 0)
			continue;
		fileLineST = new StringTokenizer(fileLine);
		fileLineST.nextToken();

		while (fileLineST.hasMoreTokens())
		{
			String seqSegment = fileLineST.nextToken();
			for (int i = 0;i < seqSegment.length();i++)
			{
				char alignmentChar = seqSegment.charAt(i);

				// FOR now allow any char as it seems like all kinds a chars are
				// being used for gap chars.
				/*
				if (NucNode.isValidNucChar(Character.toUpperCase(alignmentChar)) ||
					this.isValidGapChar(alignmentChar))
				{
					currentSeqInfo.add(new Character(Character.toUpperCase(alignmentChar)));
					continue;
				}
				*/
				currentSeqInfo.add(new Character(Character.toUpperCase(alignmentChar)));

				/*
				throw new Exception("In: " + currentSeqName + " at: " + currentSeqInfo.size() + " Invalid nuc char:" + alignmentChar + ":");
				*/
			}
		}
	}

	int maxSize = 0;
	for (Enumeration e = this.keys();e.hasMoreElements();)
	{
		String seqName = (String)e.nextElement();
		Vector seqInfo = (Vector)this.get(seqName);
		if (maxSize < seqInfo.size())
			maxSize = seqInfo.size();
	}
	for (Enumeration e = this.keys();e.hasMoreElements();)
	{
		String seqName = (String)e.nextElement();
		Vector seqInfo = (Vector)this.get(seqName);
		for (int i = seqInfo.size();i < maxSize;i++)
			seqInfo.add(new Character('-'));
	}

	return (true);
}

public static boolean
isValidGapChar(char alignmentChar)
{
	// return (!NucNode.isValidNucChar(alignmentChar));
	
	return (
		/*
		(alignmentChar == 'E') ||
		(alignmentChar == 'V') ||
		(alignmentChar == 'e') ||
		(alignmentChar == 'h') ||
		(alignmentChar == 'm') ||
		(alignmentChar == 's') ||
		(alignmentChar == 'v') ||
		(alignmentChar == 'w') ||
		(alignmentChar == 'x') ||
		*/
		(alignmentChar == '|') ||
		(alignmentChar == '(') ||
		(alignmentChar == ')') ||
		(alignmentChar == '[') ||
		(alignmentChar == ']') ||
		(alignmentChar == '-') ||
		(alignmentChar == '.') ||
		(alignmentChar == '~'));
}

private int inputType = MASE_INPUT;

public void
setInputType(int inputType)
{
    this.inputType = inputType;
}

public int
getInputType()
{
    return (this.inputType);
}

private boolean includeNucLabels = false;

public void
setIncludeNucLabels(boolean includeNucLabels)
{
    this.includeNucLabels = includeNucLabels;
}

public boolean
getIncludeNucLabels()
{
    return (this.includeNucLabels);
}

private boolean includeExtraneousLabels = false;

public void
setIncludeExtraneousLabels(boolean includeExtraneousLabels)
{
    this.includeExtraneousLabels = includeExtraneousLabels;
}

public boolean
getIncludeExtraneousLabels()
{
    return (this.includeExtraneousLabels);
}

private static void
debug(String s)
{
	System.err.println("ComplexAlignment-> " + s);
}

}
