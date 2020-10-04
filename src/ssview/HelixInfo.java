package ssview;

import java.io.*;
import java.util.*;

import util.*;

public class
HelixInfo
{

public
HelixInfo()
{
}

public
HelixInfo(RNAHelix2D helix)
throws Exception
{
	if (!helix.getFivePrimeStartNuc2D().isFormatted())
		helix.getFivePrimeStartNuc2D().setXY();
	this.setRefNuc(new Nuc2D(helix.getFivePrimeStartNuc2D()));
	/*
	if (!this.getRefNuc().isFormatted())
		this.getRefNuc().setXY();
	*/
	if (!helix.getFivePrimeStartNuc2D().getBasePair2D().isFormatted())
		helix.getFivePrimeStartNuc2D().getBasePair2D().setXY();
	this.setBpNuc(new Nuc2D(helix.getFivePrimeStartNuc2D().getBasePair()));
	/*
	if (!this.getBpNuc().isFormatted())
		this.getBpNuc().setXY();
	*/
	this.setLength(helix.getLength());
	if (helix.getFivePrimeStartNuc().getBasePair().isNonSelfRefBasePair())
		this.setBPSStrName(helix.getFivePrimeStartNuc().getBasePairSStrName());
}

public
HelixInfo(Nuc2D refNuc, Nuc2D bpNuc, int length)
throws Exception
{
	if (!refNuc.isFormatted())
		refNuc.setXY();
	this.setRefNuc(new Nuc2D(refNuc));
	/*
	if (!this.getRefNuc().isFormatted())
		this.getRefNuc().setXY();
	*/
	if (!bpNuc.isFormatted())
		bpNuc.setXY();
	this.setBpNuc(new Nuc2D(bpNuc));
	/*
	if (!this.getBpNuc().isFormatted())
		this.getBpNuc().setXY();
	*/
	this.setLength(length);
}

/*
public
HelixInfo(SSData sstr, int refNucID, int bpNucID, int length)
throws Exception
{
	NucNode refNuc = sstr.getNucAt(refNucID);
	if (refNuc == null)
		throw new ComplexException("Error HelixInfo()",
			ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_OUT_OF_RANGE_ERROR_MSG,
			" for nuc at: " + refNucID);
	this.setRefNuc(new NucNode(refNuc));
	NucNode bpNuc = sstr.getNucAt(bpNucID);
	if (bpNuc == null)
		throw new ComplexException("Error HelixInfo()",
			ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_OUT_OF_RANGE_ERROR_MSG,
			" for nuc at: " + bpNucID);
	this.setBpNuc(new NucNode(bpNuc));
	this.setLength(length);
}
*/

public
HelixInfo(SSData2D sstr, int refNucID, int bpNucID, int length)
throws Exception
{
	// check for simple wrong length case:
	if (length <= 0)
		throw new ComplexException("Error HelixInfo()",
			ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_HELIX_INFO_ERROR_MSG,
			" for helix length: " + length);

	Nuc2D refNuc = sstr.getNuc2DAt(refNucID);
	if (refNuc == null)
		throw new ComplexException("Error HelixInfo()",
			ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_OUT_OF_RANGE_ERROR_MSG,
			" for nuc at: " + refNucID);
	if (!refNuc.isFormatted())
		refNuc.setXY();
	this.setRefNuc(new Nuc2D(refNuc));

	Nuc2D bpNuc = sstr.getNuc2DAt(bpNucID);
	if (bpNuc == null)
		throw new ComplexException("Error HelixInfo()",
			ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_OUT_OF_RANGE_ERROR_MSG,
			" for nuc at: " + bpNucID);
	if (!bpNuc.isFormatted())
		bpNuc.setXY();
	this.setBpNuc(new Nuc2D(bpNuc));

	this.setLength(length);
}

// explicitly saying that bp is in another strand
public
HelixInfo(SSData2D sstr, int refNucID, int bpNucID, String bpSStrName, int length)
throws Exception
{
	debug("HERE: " + bpSStrName);
	this.setRefNucID(refNucID);
	Nuc2D refNuc = sstr.getNuc2DAt(refNucID);
	if (refNuc == null)
		throw new ComplexException("Error HelixInfo()",
			ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_OUT_OF_RANGE_ERROR_MSG,
			" for nuc at: " + refNucID);
	if (!refNuc.isFormatted())
		refNuc.setXY();
	this.setRefNuc(new Nuc2D(refNuc));
	/*
	if (!this.getRefNuc().isFormatted())
		this.getRefNuc().setXY();
	*/

	this.setBPSStrName(bpSStrName);

	this.setBpNucID(bpNucID);
	ComplexScene complexScene = refNuc.getComplexScene();
	if (complexScene == null)
		throw new ComplexException("non existent parent for nuc: " + refNuc);
	SSData2D bpSStr = (SSData2D)complexScene.getComplexSSData(this.getBPSStrName());
	if (bpSStr == null)
		throw new ComplexException("non existent rna strand: " + this.getBPSStrName());
	Nuc2D bpNuc = bpSStr.getNuc2DAt(this.getBpNucID());

	if (bpNuc == null)
		throw new ComplexException("Error HelixInfo()",
			ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_OUT_OF_RANGE_ERROR_MSG,
			" for nuc at: " + bpNucID);

	if (!bpNuc.isFormatted())
		bpNuc.setXY();
	this.setBpNuc(new Nuc2D(bpNuc));
	/*
	if (!this.getBpNuc().isFormatted())
		this.getBpNuc().setXY();
	*/

	this.setLength(length);
}

public
HelixInfo(int refNucID, int bpNucID, int length)
throws Exception
{
	this.setRefNucID(refNucID);
	this.setBpNucID(bpNucID);
	this.setLength(length);
}

public
HelixInfo(int refNucID, int bpNucID, String bpSStrName, int length)
throws Exception
{
	this.setRefNucID(refNucID);
	this.setBpNucID(bpNucID);
	this.setBPSStrName(bpSStrName);
	this.setLength(length);
}

public void
reset()
throws Exception
{
}

private int listID = 0;

public void
setListID(int listID)
{
	this.listID = listID;
}

public int
getListID()
{
	return (this.listID);
}

private int refNucID = 0;

public void
setRefNucID(int refNucID)
{
	this.refNucID = refNucID;
}

public int
getRefNucID()
{
	return (this.refNucID);
}

private Nuc2D refNuc = null;

public void
setRefNuc(Nuc2D refNuc)
throws Exception
{
	if (refNuc == null)
		throw new ComplexException("Error HelixInfo.formatCycle()",
			ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_OUT_OF_RANGE_ERROR_MSG,
			"trying to set with null base");
    this.refNuc = refNuc;
	this.setRefNucID(refNuc.getID());
}

public Nuc2D
getRefNuc()
{
    return (this.refNuc);
}

private int bpNucID = 0;

public void
setBpNucID(int bpNucID)
{
	this.bpNucID = bpNucID;
}

public int
getBpNucID()
{
	return (this.bpNucID);
}

private Nuc2D bpNuc = null;

public void
setBpNuc(Nuc2D bpNuc)
throws Exception
{
	if (bpNuc == null)
		throw new ComplexException("Error HelixInfo.formatCycle()",
			ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_ERROR,
			ComplexDefines.FORMAT_NUC_OUT_OF_RANGE_ERROR_MSG,
			"trying to set with null base");
    this.bpNuc = bpNuc;
	this.setBpNucID(bpNuc.getID());
}

public Nuc2D
getBpNuc()
{
    return (this.bpNuc);
}

private int length = 0;

public void
setLength(int length)
{
	this.length = length;
}

public int
getLength()
{
	return (this.length);
}

private String bpSStrName = null;

public void
setBPSStrName(String bpSStrName)
{
    this.bpSStrName = bpSStrName;
}

public String
getBPSStrName()
{
    return (this.bpSStrName);
}

public static String
helixInfoListToString(Vector helixList)
{
	if (helixList == null)
		return (null);
	StringBuffer strBuf = new StringBuffer();
	for (int i = 0;i < helixList.size();i++)
	{
		HelixInfo helixInfo = (HelixInfo)helixList.elementAt(i);
		strBuf.append(helixInfo.toString());
		strBuf.append("\n");
	}

	return (strBuf.toString());
}

public static Vector
getHelixInfoList(File secondaryStructureFile)
throws Exception
{
	return (getHelixInfoList(FileUtil.getFileAsString(secondaryStructureFile)));
}

public static Vector
getHelixInfoList(String secondaryStructure)
throws Exception
{
	return (getHelixInfoList(null, secondaryStructure));
}

public static Vector
getHelixInfoList(SSData2D sstr, File secondaryStructureFile)
throws Exception
{
	return (getHelixInfoList(sstr, FileUtil.getFileAsString(secondaryStructureFile)));
}

// assume secondaryStructure helices are delineated by new line characters.
public static Vector
getHelixInfoList(SSData2D sstr, String secondaryStructure)
throws Exception
{
	int clearTokenVal = Integer.MIN_VALUE;
	StreamTokenizer st = new StreamTokenizer(new StringReader(secondaryStructure));
	Vector helixList = new Vector();
	st.eolIsSignificant(true);
	st.commentChar('#');
	st.wordChars('0', '9');
	st.wordChars(' ', ' ');
	st.wordChars('-', '-');
	st.wordChars('_', '_');
	// st.wordChars('.', '.');
	// st.wordChars('*', '*');

	int nucID = clearTokenVal;
	int bpID = clearTokenVal;
	int helixLength = clearTokenVal;
	String bpSStrName = null;
	int lineNumber = 1;
	while (st.ttype != StreamTokenizer.TT_EOF)
	{
		st.nextToken();
		switch (st.ttype)
		{
		  case StreamTokenizer.TT_EOL:
		  case StreamTokenizer.TT_EOF:
			if ((nucID > clearTokenVal) && (bpID > clearTokenVal) && (helixLength > clearTokenVal))
			{
				HelixInfo helixInfo = null;
				if ((sstr == null) && (bpSStrName == null))
					helixInfo = new HelixInfo(nucID, bpID, helixLength);
				else if ((sstr != null) && (bpSStrName != null))
					helixInfo = new HelixInfo(sstr, nucID, bpID, bpSStrName, helixLength);
				else if ((sstr == null) && (bpSStrName != null))
					helixInfo = new HelixInfo(nucID, bpID, bpSStrName, helixLength);
				else if ((sstr != null) && (bpSStrName == null))
					helixInfo = new HelixInfo(sstr, nucID, bpID, helixLength);
				helixList.add(helixInfo);
			}
			else if ((nucID > clearTokenVal) && (bpID > clearTokenVal))
			{
				// NEED to fill in for a simple base pair
				throw new ComplexException("Error HelixInfo.getHelixInfoList()",
					ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_BASEPAIR_ERROR,
					ComplexDefines.FORMAT_HELIX_INFO_ERROR_MSG,
						" line number: " + lineNumber);
			}
			else if ((nucID > clearTokenVal) || (bpID > clearTokenVal) || (helixLength > clearTokenVal) || (bpSStrName != null))
			{
				throw new ComplexException("Error HelixInfo.getHelixInfoList()",
					ComplexDefines.RNA_HELIX_ERROR + ComplexDefines.FORMAT_BASEPAIR_ERROR,
					ComplexDefines.FORMAT_HELIX_INFO_ERROR_MSG,
						" line number: " + lineNumber);
			}
			else // hopefully a blank line or commented out line
			{
			}
			nucID = bpID = helixLength = clearTokenVal;
			bpSStrName = null;
			lineNumber++;
			break;
		  case StreamTokenizer.TT_NUMBER:
			if (nucID <= clearTokenVal)
				nucID = (int)st.nval;
			else if (bpID <= clearTokenVal)
				bpID = (int)st.nval;
			else if (helixLength <= clearTokenVal)
				helixLength = (int)st.nval;
			break;
		  case StreamTokenizer.TT_WORD:
			if (bpSStrName == null)
				bpSStrName = (String)st.sval;
		  	break;
		  default:
			break;
		} // end Switch
	} // end while

	return (helixList);
}

public static Vector
getHelixInfoList(SSData2D sstr, String secondaryStructure, char delineator)
throws Exception
{
	String helixListString = (new String(secondaryStructure)).
		replace(delineator, '\n');
	return (getHelixInfoList(sstr, helixListString));
}

public String
toString()
{
	return (this.getRefNuc().getID() + " " + this.getBpNuc().getID() + " " + this.getLength());
}

public static void
debug(String s)
{
	System.err.println("HelixInfo-> " + s);
}

}
