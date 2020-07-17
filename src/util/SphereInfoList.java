package util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Vector;

import javax.vecmath.Color3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;

import util.math.BMatrix4d;
import util.math.BPoint3d;
import util.math.BVector3d;
import util.math.MathDefines;

public class
SphereInfoList
extends Vector
{

public
SphereInfoList()
{
	super();
}

public
SphereInfoList(SphereInfoList fromList)
{
	super(fromList);
}

public SphereInfo
getSphereInfoAt(int index)
{
	return ((SphereInfo)this.elementAt(index));
}

private String pdbFileName = null;

public void
setPDBFileName(String pdbFileName)
{
	this.pdbFileName = pdbFileName;
	// debug("Setting: " + this.getPDBFileName());
}

public String
getPDBFileName()
{
	return (this.pdbFileName);
}

private int lastSerialID = 0;

public void
setLastSerialID(int lastSerialID)
{
    this.lastSerialID = lastSerialID;
}

public int
getLastSerialID()
{
    return (this.lastSerialID);
}

public SphereInfo
getSphereInfoByResidue(String atomType, int residueID)
{
	for (int i = 0;i < this.size();i++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(i);

		if (sphereInfo.getResidueID() != residueID)
			continue;
		if (!sphereInfo.getAtomType().equals(atomType))
			continue;
		return (sphereInfo);
	}
	return (null);
}

public SphereInfo
getSphereInfoByResidue(String atomType, String chainID, int residueID)
{
	for (int i = 0;i < this.size();i++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(i);

		if (!sphereInfo.getChainID().equals(chainID))
			continue;
		if (sphereInfo.getResidueID() != residueID)
			continue;
		if (!sphereInfo.getAtomType().equals(atomType))
			continue;
		return (sphereInfo);
	}
	return (null);
}

private boolean isRNA = false;

public void
setIsRNA(boolean isRNA)
{
    this.isRNA = isRNA;
}

public boolean
getIsRNA()
{
    return (this.isRNA);
}

private boolean isProtein = false;

public void
setIsProtein(boolean isProtein)
{
    this.isProtein = isProtein;
}

public boolean
getIsProtein()
{
    return (this.isProtein);
}


public void
readPDB(String fileName)
throws IOException
{
	this.setPDBFileName(fileName);
	this.readPDB();
}

// private int chainIDPDBColStart = 17;
private int chainIDPDBColStart = 21;

public void
setChainIDPDBColStart(int chainIDPDBColStart)
{
    this.chainIDPDBColStart = chainIDPDBColStart;
}

public int
getChainIDPDBColStart()
{
    return (this.chainIDPDBColStart);
}

// private int chainIDPDBColEnd = 19;
private int chainIDPDBColEnd = 22;

public void
setChainIDPDBColEnd(int chainIDPDBColEnd)
{
    this.chainIDPDBColEnd = chainIDPDBColEnd;
}

public int
getChainIDPDBColEnd()
{
    return (this.chainIDPDBColEnd);
}

public void
readPDB()
throws IOException
{
	if ((this.getPDBFileName() == null) || (this.getPDBFileName().length() == 0))
		throw new IOException("Can't find PDB file: " + this.getPDBFileName());

	StreamTokenizer st = new StreamTokenizer(new FileReader(new File(this.getPDBFileName())));

	st.eolIsSignificant(true);
	st.commentChar('#');
	st.wordChars('a', 'z');
	st.wordChars('A', 'Z');
	st.wordChars(128 + 32, 255);
	st.wordChars(' ', ' ');
	st.wordChars('-', '-');
	st.wordChars('.', '.');
	st.wordChars('*', '*');
	st.wordChars('$', '$');

	try
	{
		String atomType;
		double tsx, tsy, tsz;
		int residueID = 0;
		// char chainID = Character.MIN_VALUE;
		String chainID;
		boolean running = true;

		while (running)
		{
			st.nextToken();
			switch(st.ttype)
			{
			  case StreamTokenizer.TT_EOL:
				break;
			  case StreamTokenizer.TT_EOF:
				running = false;
				continue;
			  case StreamTokenizer.TT_WORD:
				if(!st.sval.startsWith("ATOM"))
					break;
				atomType = st.sval.substring(12, 16).trim();
				if (atomType.equals("P"))
					this.setIsRNA(true);
				if (atomType.equals("CA"))
					this.setIsProtein(true);
				//
				// boolean phosphatehit = false, nonphosphatehit = false;
				// for(int i = 0;i < atomType.length();i++) //get phosphates only
				// {
					// if(atomType.charAt(i) == ' ')
						// continue;
					// if(atomType.charAt(i) == 'P')
						// phosphatehit = true;
					// else
						// nonphosphatehit = true;
				// }
				// if(nonphosphatehit || !phosphatehit)
					// break;
				//
				//
				// if (!(atomType.equals("C") || (atomType.equals("N"))))
				// if (!atomType.equals("C"))
					// break;
				//
				int serialID =
					Integer.parseInt(st.sval.substring(7, 11).trim());
				if (this.getLastSerialID() < serialID)
					this.setLastSerialID(serialID);

				// chainID = st.sval.charAt(21); // SHOULD BE col 17-19 (java 18)
				chainID = st.sval.substring(this.getChainIDPDBColStart() - 1,
					this.getChainIDPDBColEnd()).trim();
				// debug("CHAINID: " + chainID);
				residueID =
					Integer.parseInt(st.sval.substring(22, 26).trim());
				// debug("RESIDUEID: " + residueID);

				tsx = Double.valueOf(st.sval.substring(30, 38).trim()).doubleValue();
				tsy = Double.valueOf(st.sval.substring(38, 46).trim()).doubleValue();
				tsz = Double.valueOf(st.sval.substring(46, 54).trim()).doubleValue();
				// debug("PDB item: " + residueID + " " + tsx + " " + tsy + " " + tsz);
				// NEED to set residueID to nuc in SS or protein atom
				// Vector nucvec = getCurrentSSData().NucNodeList();
				// NucNode tmpNucNode = (NucNode)nucvec.elementAt(nuc - 1);
				// tmpNucNode.setNucTSPoint(tsx, tsy, tsz);
				//
// debug(serialID + " " + atomType + " " + tsx + " " + tsy + " " + tsz);
				this.add(new SphereInfo(tsx, tsy, tsz,
					chainID, serialID, atomType, residueID));
				break;
			  default:
				break;
			} // end Switch
		} // end while
	} // end try
	catch(IOException e) {}
	if (st.ttype != StreamTokenizer.TT_EOF)
		throw new IOException(st.toString());
}

public void
writePDB(PrintWriter outFile)
{
	int ptCount = this.size();
	// debug("ptCount: " + ptCount);
	char[] pdbLine = new char[66];

	for (int i = 0;i < ptCount;i++)
	{
		SphereInfo sphere = this.getSphereInfoAt(i);
		for (int a = 0;a < 66;a++)
			pdbLine[a] = ' ';

		// header:
		pdbLine[0] = 'A';
		pdbLine[1] = 'T';
		pdbLine[2] = 'O';
		pdbLine[3] = 'M';

		// serial number:
		int linePos = 0;
		char[] val = String.valueOf(sphere.getSerialNumber()).trim().toCharArray();
		linePos = 10;
		for (int j = val.length - 1;j >= 0;j--)
			pdbLine[linePos--] = val[j];

		// atom type:
		linePos = 15;
		val = sphere.getAtomType().trim().toCharArray();
		for (int j = val.length - 1;j >= 0;j--)
			pdbLine[linePos--] = val[j];

		// chain ID:
		linePos = 19;
		val = sphere.getChainID().trim().toCharArray();
		for (int j = val.length - 1;j >= 0;j--)
			pdbLine[linePos--] = val[j];

		// pdbLine[21] = '$';

		// residueID:
		int residueCount = sphere.getResidueID();
		char[] countArray = String.valueOf(residueCount).trim().toCharArray();
		if (residueCount < 10)
		{
			pdbLine[25] = countArray[0];
		}
		else if (residueCount < 100)
		{
			pdbLine[24] = countArray[0];
			pdbLine[25] = countArray[1];
		}
		else if (residueCount < 1000)
		{
			pdbLine[23] = countArray[0];
			pdbLine[24] = countArray[1];
			pdbLine[25] = countArray[2];
		}
		else if (residueCount < 10000)
		{
			pdbLine[22] = countArray[0];
			pdbLine[23] = countArray[1];
			pdbLine[24] = countArray[2];
			pdbLine[25] = countArray[3];
		}
		else if (residueCount < 100000)
		{
			pdbLine[21] = countArray[0];
			pdbLine[22] = countArray[1];
			pdbLine[23] = countArray[2];
			pdbLine[24] = countArray[3];
			pdbLine[25] = countArray[4];
		}

		val = String.valueOf(sphere.getX()).trim().toCharArray();
		int periodIndex = 0;
		linePos = 34;
		pdbLine[linePos--] = '.';
		while((val[periodIndex] != '.') && (periodIndex < val.length))
			periodIndex++;
		for (int l = periodIndex - 1;l >= 0;l--)
			pdbLine[linePos--] = val[l];
		linePos = 34 + 1;
		// debug("val.length: " + val.length);
		for (int l = periodIndex+1;l < periodIndex+4;l++)
		{
			if (l < val.length)
			{
				// debug("l: " + l);
				pdbLine[linePos++] = val[l];
			}
			else
			{
				// debug("linePos: " + linePos);
				pdbLine[linePos++] = '0';
			}
		}

		val = String.valueOf(sphere.getY()).trim().toCharArray();
		periodIndex = 0;
		linePos = 42;
		pdbLine[linePos--] = '.';
		while((val[periodIndex] != '.') && (periodIndex < val.length))
			periodIndex++;
		for (int l = periodIndex - 1;l >= 0;l--)
			pdbLine[linePos--] = val[l];
		linePos = 42 + 1;
		// debug("val.length: " + val.length);
		for (int l = periodIndex+1;l < periodIndex+4;l++)
		{
			if (l < val.length)
			{
				// debug("l: " + l);
				pdbLine[linePos++] = val[l];
			}
			else
			{
				// debug("linePos: " + linePos);
				pdbLine[linePos++] = '0';
			}
		}

		val = String.valueOf(sphere.getZ()).trim().toCharArray();
		periodIndex = 0;
		linePos = 50;
		pdbLine[linePos--] = '.';
		while((val[periodIndex] != '.') && (periodIndex < val.length))
			periodIndex++;
		for (int l = periodIndex - 1;l >= 0;l--)
			pdbLine[linePos--] = val[l];
		linePos = 50 + 1;
		// debug("val.length: " + val.length);
		for (int l = periodIndex+1;l < periodIndex+4;l++)
		{
			if (l < val.length)
			{
				// debug("l: " + l);
				pdbLine[linePos++] = val[l];
			}
			else
			{
				// debug("linePos: " + linePos);
				pdbLine[linePos++] = '0';
			}
		}


/*
ATOM   1566  O   HOH $ 288      42.800  39.169  38.849  1.00 52.08   8
*/

		pdbLine[56] = '1';
		pdbLine[57] = '.';
		pdbLine[58] = '0';
		pdbLine[59] = '0';

		pdbLine[62] = '0';
		pdbLine[63] = '.';
		pdbLine[64] = '0';
		pdbLine[65] = '0';

		outFile.println(new String(pdbLine));
	}
}

public static void
writePtListToPDB(PrintWriter outFile, Object intersectList, int startSerialID)
{
	if (intersectList == null)
	{
		debug("NO INTERSECT LIST FOR writeShellIntersect()");
		return;
	}

	int ptCount = ((Vector)intersectList).size();
	int serialID = startSerialID;
	char[] pdbLine = new char[66];

	for (int i = 0;i < ptCount;i++)
	{
		for (int a = 0;a < 66;a++)
			pdbLine[a] = ' ';
		pdbLine[0] = 'A';
		pdbLine[1] = 'T';
		pdbLine[2] = 'O';
		pdbLine[3] = 'M';
		double[] ptVals = new double[3];
		int linePos = 0;
		char[] val = String.valueOf(serialID).trim().toCharArray();
		linePos = 10;
		for (int j = val.length - 1;j >= 0;j--)
			pdbLine[linePos--] = val[j];
		pdbLine[15] = 'O';
		pdbLine[17] = 'H';
		pdbLine[18] = 'O';
		pdbLine[19] = 'H';
		// pdbLine[21] = '$';
		int residueCount = i + 1;
		char[] countArray = String.valueOf(residueCount).trim().toCharArray();
		if (residueCount < 10)
		{
			pdbLine[25] = countArray[0];
		}
		else if (residueCount < 100)
		{
			pdbLine[24] = countArray[0];
			pdbLine[25] = countArray[1];
		}
		else if (residueCount < 1000)
		{
			pdbLine[23] = countArray[0];
			pdbLine[24] = countArray[1];
			pdbLine[25] = countArray[2];
		}
		else if (residueCount < 10000)
		{
			pdbLine[22] = countArray[0];
			pdbLine[23] = countArray[1];
			pdbLine[24] = countArray[2];
			pdbLine[25] = countArray[3];
		}
		else if (residueCount < 100000)
		{
			pdbLine[21] = countArray[0];
			pdbLine[22] = countArray[1];
			pdbLine[23] = countArray[2];
			pdbLine[24] = countArray[3];
			pdbLine[25] = countArray[4];
		}

		BPoint3d pt = null;
		if (intersectList instanceof SphereInfoList)
			pt = ((SphereInfo)(((SphereInfoList)intersectList).elementAt(i))).getOrigin();
		else
			pt = (BPoint3d)((Vector)intersectList).elementAt(i);
		/*
		if (intersectList instanceof Vector)
			pt = (BPoint3d)((Vector)intersectList).elementAt(i);
		else if (intersectList instanceof SphereInfoList)
		{
// pt = ((SphereInfo)(((SphereInfoList)intersectList).elementAt(i))).getOrigin();
			SphereInfoList sphereList = (SphereInfoList)intersectList;
			SphereInfo sphere = sphereList.getSphereInfoAt(i);
			pt = sphere.getOrigin();
		}
		*/

		// Point3d pt = (Point3d)intersectList.elementAt(i);

		pt.get(ptVals);

		val = String.valueOf(ptVals[0]).trim().toCharArray();
		int periodIndex = 0;
		linePos = 34;
		pdbLine[linePos--] = '.';
		while((val[periodIndex] != '.') && (periodIndex < val.length))
			periodIndex++;
		for (int l = periodIndex - 1;l >= 0;l--)
			pdbLine[linePos--] = val[l];
		linePos = 34 + 1;
		// debug("val.length: " + val.length);
		for (int l = periodIndex+1;l < periodIndex+4;l++)
		{
			if (l < val.length)
			{
				// debug("l: " + l);
				pdbLine[linePos++] = val[l];
			}
			else
			{
				// debug("linePos: " + linePos);
				pdbLine[linePos++] = '0';
			}
		}

		val = String.valueOf(ptVals[1]).trim().toCharArray();
		periodIndex = 0;
		linePos = 42;
		pdbLine[linePos--] = '.';
		while((val[periodIndex] != '.') && (periodIndex < val.length))
			periodIndex++;
		for (int l = periodIndex - 1;l >= 0;l--)
			pdbLine[linePos--] = val[l];
		linePos = 42 + 1;
		// debug("val.length: " + val.length);
		for (int l = periodIndex+1;l < periodIndex+4;l++)
		{
			if (l < val.length)
			{
				// debug("l: " + l);
				pdbLine[linePos++] = val[l];
			}
			else
			{
				// debug("linePos: " + linePos);
				pdbLine[linePos++] = '0';
			}
		}

		val = String.valueOf(ptVals[2]).trim().toCharArray();
		periodIndex = 0;
		linePos = 50;
		pdbLine[linePos--] = '.';
		while((val[periodIndex] != '.') && (periodIndex < val.length))
			periodIndex++;
		for (int l = periodIndex - 1;l >= 0;l--)
			pdbLine[linePos--] = val[l];
		linePos = 50 + 1;
		// debug("val.length: " + val.length);
		for (int l = periodIndex+1;l < periodIndex+4;l++)
		{
			if (l < val.length)
			{
				// debug("l: " + l);
				pdbLine[linePos++] = val[l];
			}
			else
			{
				// debug("linePos: " + linePos);
				pdbLine[linePos++] = '0';
			}
		}


/*
ATOM   1566  O   HOH $ 288      42.800  39.169  38.849  1.00 52.08   8
*/

		pdbLine[56] = '1';
		pdbLine[57] = '.';
		pdbLine[58] = '0';
		pdbLine[59] = '0';

		pdbLine[62] = '0';
		pdbLine[63] = '.';
		pdbLine[64] = '0';
		pdbLine[65] = '0';

		outFile.println(new String(pdbLine));
		serialID++;
	}

}

public void
writeShellIntersect(PrintWriter outFile, Vector intersectList)
{
	/*
	if (intersectList == null)
	{
		debug("NO INTERSECT LIST FOR writeShellIntersect()");
		return;
	}

	int serialID = this.getLastSerialID() + 1;
	*/
	this.writePtListToPDB(outFile, intersectList, this.getLastSerialID() + 1);

	/*
	int ptCount = intersectList.size();
	// debug("SERIALID: " + serialID);
	char[] pdbLine = new char[66];

	for (int i = 0;i < ptCount;i++)
	{
		for (int a = 0;a < 66;a++)
			pdbLine[a] = ' ';
		pdbLine[0] = 'A';
		pdbLine[1] = 'T';
		pdbLine[2] = 'O';
		pdbLine[3] = 'M';
		double[] ptVals = new double[3];
		int linePos = 0;
		char[] val = String.valueOf(serialID).trim().toCharArray();
		linePos = 10;
		for (int j = val.length - 1;j >= 0;j--)
			pdbLine[linePos--] = val[j];
		pdbLine[15] = 'O';
		pdbLine[17] = 'H';
		pdbLine[18] = 'O';
		pdbLine[19] = 'H';
		// pdbLine[21] = '$';
		int residueCount = i + 1;
		char[] countArray = String.valueOf(residueCount).trim().toCharArray();
		if (residueCount < 10)
		{
			pdbLine[25] = countArray[0];
		}
		else if (residueCount < 100)
		{
			pdbLine[24] = countArray[0];
			pdbLine[25] = countArray[1];
		}
		else if (residueCount < 1000)
		{
			pdbLine[23] = countArray[0];
			pdbLine[24] = countArray[1];
			pdbLine[25] = countArray[2];
		}
		else if (residueCount < 10000)
		{
			pdbLine[22] = countArray[0];
			pdbLine[23] = countArray[1];
			pdbLine[24] = countArray[2];
			pdbLine[25] = countArray[3];
		}
		else if (residueCount < 100000)
		{
			pdbLine[21] = countArray[0];
			pdbLine[22] = countArray[1];
			pdbLine[23] = countArray[2];
			pdbLine[24] = countArray[3];
			pdbLine[25] = countArray[4];
		}
		Point3d pt = (Point3d)intersectList.elementAt(i);
		pt.get(ptVals);

		val = String.valueOf(ptVals[0]).trim().toCharArray();
		int periodIndex = 0;
		linePos = 34;
		pdbLine[linePos--] = '.';
		while((val[periodIndex] != '.') && (periodIndex < val.length))
			periodIndex++;
		for (int l = periodIndex - 1;l >= 0;l--)
			pdbLine[linePos--] = val[l];
		linePos = 34 + 1;
		// debug("val.length: " + val.length);
		for (int l = periodIndex+1;l < periodIndex+4;l++)
		{
			if (l < val.length)
			{
				// debug("l: " + l);
				pdbLine[linePos++] = val[l];
			}
			else
			{
				// debug("linePos: " + linePos);
				pdbLine[linePos++] = '0';
			}
		}

		val = String.valueOf(ptVals[1]).trim().toCharArray();
		periodIndex = 0;
		linePos = 42;
		pdbLine[linePos--] = '.';
		while((val[periodIndex] != '.') && (periodIndex < val.length))
			periodIndex++;
		for (int l = periodIndex - 1;l >= 0;l--)
			pdbLine[linePos--] = val[l];
		linePos = 42 + 1;
		// debug("val.length: " + val.length);
		for (int l = periodIndex+1;l < periodIndex+4;l++)
		{
			if (l < val.length)
			{
				// debug("l: " + l);
				pdbLine[linePos++] = val[l];
			}
			else
			{
				// debug("linePos: " + linePos);
				pdbLine[linePos++] = '0';
			}
		}

		val = String.valueOf(ptVals[2]).trim().toCharArray();
		periodIndex = 0;
		linePos = 50;
		pdbLine[linePos--] = '.';
		while((val[periodIndex] != '.') && (periodIndex < val.length))
			periodIndex++;
		for (int l = periodIndex - 1;l >= 0;l--)
			pdbLine[linePos--] = val[l];
		linePos = 50 + 1;
		// debug("val.length: " + val.length);
		for (int l = periodIndex+1;l < periodIndex+4;l++)
		{
			if (l < val.length)
			{
				// debug("l: " + l);
				pdbLine[linePos++] = val[l];
			}
			else
			{
				// debug("linePos: " + linePos);
				pdbLine[linePos++] = '0';
			}
		}


//
// ATOM   1566  O   HOH $ 288      42.800  39.169  38.849  1.00 52.08   8
//

		pdbLine[56] = '1';
		pdbLine[57] = '.';
		pdbLine[58] = '0';
		pdbLine[59] = '0';

		pdbLine[62] = '0';
		pdbLine[63] = '.';
		pdbLine[64] = '0';
		pdbLine[65] = '0';

		outFile.println(new String(pdbLine));
		serialID++;
	}
	*/

}


private Vector volumeIntersectList = null;

public void
setVolumeIntersectList(Vector volumeIntersectList)
{
    this.volumeIntersectList = volumeIntersectList;
}

public Vector
getVolumeIntersectList()
{
    return (this.volumeIntersectList);
}

public void
setVolumeIntersectingSpherePts(double densityInc,
	SphereInfoList negSphereInfoList)
{
	debug("IN setVolumeIntersectingSpherePts");
	Object[] sphereInfoArray = this.toArray();
	Object[] negSphereInfoArray = null;
	if (negSphereInfoList != null)
		negSphereInfoArray = negSphereInfoList.toArray();

	double minX = Double.MAX_VALUE;
	double maxX = -Double.MAX_VALUE;
	double minY = Double.MAX_VALUE;
	double maxY = -Double.MAX_VALUE;
	double minZ = Double.MAX_VALUE;
	double maxZ = -Double.MAX_VALUE;

	Color3f ptColor = new Color3f(1.0f, 0.0f, 0.0f);

	setVolumeIntersectList(new Vector());

	for (int i = 0;i < sphereInfoArray.length;i++)
	{
		SphereInfo sphereInfo = (SphereInfo)sphereInfoArray[i];
		double radius = sphereInfo.getRadius();
		if(minX > sphereInfo.getX() - radius)
			minX = sphereInfo.getX() - radius;
		if(minY > sphereInfo.getY() - radius)
			minY = sphereInfo.getY() - radius;
		if(minZ > sphereInfo.getZ() - radius)
			minZ = sphereInfo.getZ() - radius;
		if(maxX < sphereInfo.getX() + radius)
			maxX = sphereInfo.getX() + radius;
		if(maxY < sphereInfo.getY() + radius)
			maxY = sphereInfo.getY() + radius;
		if(maxZ < sphereInfo.getZ() + radius)
			maxZ = sphereInfo.getZ() + radius;
	}
	minX -= densityInc;
	minY -= densityInc;
	minZ -= densityInc;
	maxX += densityInc;
	maxY += densityInc;
	maxZ += densityInc;

	double inc = densityInc;
	for (double tx = minX;tx <= maxX;tx+=inc)
	for (double ty = minY;ty <= maxY;ty+=inc)
	for (double tz = minZ;tz <= maxZ;tz+=inc)
	{
		//
		// SetPoint(spherept, tx, ty, tz);
		// if(InSphereList(spherept) &&
			// (!InCylinder(spherept, axispt0, axispt1, cylrad)) &&
			// (!InNegSphereList(spherept)))
		// {
			// // if(InWholeCylinderShadow(spherept, cylsrf, axispt0, axispt1, cylrad))
			// if(InShadow(spherept, axispt0, axispt1, cylrad))
				// fprintf(shadowout,"%c %.2f %.2f %.2f\n", 'P', tx, ty, tz);
			// else
				// fprintf(out, "%c %.2f %.2f %.2f\n", 'P', tx, ty, tz);
		// }
		//

		if (!inAllSpheres((double)tx, (double)ty, (double)tz))
			continue;
		if (inAnySpheres((double)tx, (double)ty, (double)tz, negSphereInfoArray))
			continue;
		getVolumeIntersectList().add(new Point3d(tx, ty, tz));
	}

	if (getVolumeIntersectList().size() <= 0)
	{

		return;
	}
	int ptIndex = 0;
	Object[] ptListArray = getVolumeIntersectList().toArray();
	for (int i = 0;i < ptListArray.length;i++)
	{
		ptIndex++;
	}


}


private Vector shellIntersectList = null;

public void
setShellIntersectList(Vector shellIntersectList)
{
    this.shellIntersectList = shellIntersectList;
}

public Vector
getShellIntersectList()
{
    return (this.shellIntersectList);
}

public void
setShellIntersectingSpherePts(double densityInc,
	SphereInfoList negSphereInfoList)
{
	Object[] sphereInfoArray = this.toArray();
	Object[] negSphereInfoArray = null;
	if (negSphereInfoList != null)
		negSphereInfoArray = negSphereInfoList.toArray();

	double minX = Double.MAX_VALUE;
	double maxX = -Double.MAX_VALUE;
	double minY = Double.MAX_VALUE;
	double maxY = -Double.MAX_VALUE;
	double minZ = Double.MAX_VALUE;
	double maxZ = -Double.MAX_VALUE;

	Color3f ptColor = new Color3f(1.0f, 0.0f, 0.0f);

	setShellIntersectList(new Vector());

	for (int i = 0;i < sphereInfoArray.length;i++)
	{
		SphereInfo sphereInfo = (SphereInfo)sphereInfoArray[i];
		double radius = sphereInfo.getRadius();
		if(minX > sphereInfo.getX() - radius)
			minX = sphereInfo.getX() - radius;
		if(minY > sphereInfo.getY() - radius)
			minY = sphereInfo.getY() - radius;
		if(minZ > sphereInfo.getZ() - radius)
			minZ = sphereInfo.getZ() - radius;
		if(maxX < sphereInfo.getX() + radius)
			maxX = sphereInfo.getX() + radius;
		if(maxY < sphereInfo.getY() + radius)
			maxY = sphereInfo.getY() + radius;
		if(maxZ < sphereInfo.getZ() + radius)
			maxZ = sphereInfo.getZ() + radius;
	}
	minX -= densityInc;
	minY -= densityInc;
	minZ -= densityInc;
	maxX += densityInc;
	maxY += densityInc;
	maxZ += densityInc;

	double inc = densityInc;

	Point3d tailPt = new Point3d();
	Point3d headPt = new Point3d();

	for (double tz = minZ;tz <= maxZ;tz+=inc)
	for (double ty = minY;ty <= maxY;ty+=inc)
	{
		tailPt.set(minX, ty, tz);
		headPt.set(maxX, ty, tz);
		getOuterPts(tailPt, headPt, negSphereInfoArray);
	}

	for (double tx = minX;tx <= maxX;tx+=inc)
	for (double ty = minY;ty <= maxY;ty+=inc)
	{
		tailPt.set(tx, ty, minZ);
		headPt.set(tx, ty, maxZ);
		getOuterPts(tailPt, headPt, negSphereInfoArray);
	}

	for (double tz = minZ;tz <= maxZ;tz+=inc)
	for (double tx = minX;tx <= maxX;tx+=inc)
	{
		tailPt.set(tx, minY, tz);
		headPt.set(tx, maxY, tz);
		getOuterPts(tailPt, headPt, negSphereInfoArray);
	}

	if (getShellIntersectList().size() <= 0)
	{
		return;
	}

	int ptIndex = 0;
	Object[] ptListArray = getShellIntersectList().toArray();
	for (int i = 0;i < ptListArray.length;i++)
	{
//		ptArray.setCoordinate(ptIndex, (Point3d)ptListArray[i]);
//		ptArray.setColor(ptIndex, ptColor);
		ptIndex++;
	}

//	setShellPtArray(ptArray);
}

private void
getOuterPts(Point3d tailPt, Point3d headPt, Object[] negSphereInfoArray)
{
	double tInc = 0.001;
	Point3d newPt = new Point3d();
	double[] tvals = new double[2];
	boolean intersects = false;
	int sphereID = 0;
	double smallestTVal = Double.MAX_VALUE;
	double largestTVal = -Double.MAX_VALUE;
	for (sphereID = 0;sphereID < this.size();sphereID++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(sphereID);

//		intersects = Graphics3DUtil.raySphereIntersect(
//			tailPt, headPt, sphereInfo.getOrigin(),
//			sphereInfo.getRadius(), tvals);
//		if (!intersects)
//			break
			
		if (tvals[0] < smallestTVal)
			smallestTVal = tvals[0];
		if (tvals[1] > largestTVal)
			largestTVal = tvals[1];
	}

	
	if (!((sphereID == this.size()) && intersects))
		return;

	// ray intersects all spheres

	// NEED to optimize by not adding similar pts to list

	double[] ptVals = new double[3];
	for (double t = smallestTVal;t <= largestTVal;t += tInc)
	{
		Graphics3DUtil.getPointAtT(tailPt, headPt, newPt, t);
		newPt.get(ptVals);
		if (!inAllSpheres(ptVals[0], ptVals[1], ptVals[2]))
			continue;
		if (inAnySpheres(ptVals[0], ptVals[1], ptVals[2], negSphereInfoArray))
			continue;
		getShellIntersectList().add(new Point3d(ptVals[0], ptVals[1], ptVals[2]));
		break;
	}
	for (double t = largestTVal;t >= smallestTVal;t -= tInc)
	{
		Graphics3DUtil.getPointAtT(tailPt, headPt, newPt, t);
		newPt.get(ptVals);
		if (!inAllSpheres(ptVals[0], ptVals[1], ptVals[2]))
			continue;
		if (inAnySpheres(ptVals[0], ptVals[1], ptVals[2], negSphereInfoArray))
			continue;
		getShellIntersectList().add(new Point3d(ptVals[0], ptVals[1], ptVals[2]));
		break;
	}
}

public boolean
inAllSpheres(double x, double y, double z)
{
	for (int i = 0;i < this.size();i++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(i);
		if (!sphereInfo.contains(x, y, z))
			return (false);
	}
	return (true);
}

public boolean
inAllSpheres(double x, double y, double z, Object[] sphereInfoArray)
{
	for (int i = 0;i < sphereInfoArray.length;i++)
	{
		SphereInfo sphereInfo = (SphereInfo)sphereInfoArray[i];
		if (!sphereInfo.contains(x, y, z))
			return (false);
	}
	return (true);
}

public boolean
inAnySpheres(double x, double y, double z)
{
	for (int i = 0;i < this.size();i++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(i);
		if (sphereInfo.contains(x, y, z))
			return (true);
	}
	return (false);
}

public boolean
inAnySpheres(double x, double y, double z, Object[] sphereInfoArray)
{
	if (sphereInfoArray == null)
		return (false);
	for (int i = 0;i < sphereInfoArray.length;i++)
	{
		SphereInfo sphereInfo = (SphereInfo)sphereInfoArray[i];
		if (sphereInfo.contains(x, y, z))
			return (true);
	}
	return (false);
}

private Point3d meanPt = null;

public void
setMeanPt(Point3d meanPt)
{
    this.meanPt = meanPt;
}

public Point3d
getMeanPt()
{
    return (this.meanPt);
}

private Point3d variancePt = null;

public void
setVariancePt(Point3d variancePt)
{
    this.variancePt = variancePt;
}

public Point3d
getVariancePt()
{
    return (this.variancePt);
}

private int meanPtID = 0;

public void
setMeanPtID(int meanPtID)
{
    this.meanPtID = meanPtID;
}

public int
getMeanPtID()
{
    return (this.meanPtID);
}

private int variancePtID = 0;

public void
setVariancePtID(int variancePtID)
{
    this.variancePtID = variancePtID;
}

public int
getVariancePtID()
{
    return (this.variancePtID);
}

// this is used in the clouds project; a more general method is in mathutils.
public void
getMoment(double densityInc, SphereInfoList negSphereInfoList)
{
	if ((this.getMeanPt() != null) && (this.getVariancePt() != null))
		return;

	double mean = 0.0;
	double variance = 0.0;
	double smallestmean = Double.MAX_VALUE;
	double smallestvariance = Double.MAX_VALUE;

	if ((getVolumeIntersectList() == null) || (getVolumeIntersectList().size() <= 0))
		setVolumeIntersectingSpherePts(densityInc, negSphereInfoList);
	Object[] ptListArray = getVolumeIntersectList().toArray();
	debug("ptListArray.size: " + ptListArray.length);

	setMeanPt(new Point3d());
	setVariancePt(new Point3d());

	for (int i = 0;i < ptListArray.length;i++)
	{
		mean = getMean(i);
		variance = getVariance(i, mean);
		if (mean < smallestmean)
		{
			smallestmean = mean;
			setMeanPtID(i);
		}
		if (variance < smallestvariance)
		{
			smallestvariance = variance;
			setVariancePtID(i);
		}
	}

	getMeanPt().set(new Point3d((Point3d)ptListArray[getMeanPtID()]));
	getVariancePt().set(new Point3d((Point3d)ptListArray[getVariancePtID()]));
}

public double
getMean(int ptID)
{
	Object[] ptListArray = getVolumeIntersectList().toArray();

	Point3d ptAtID = (Point3d)ptListArray[ptID];
	Point3d searchPt = new Point3d();
	double accum = 0.0;

	for (int i = 0;i < ptListArray.length;i++)
	{
		if (i == ptID) // don't deal with pt in question
			continue;
		searchPt.set((Point3d)ptListArray[i]);
		accum += ptAtID.distance(searchPt);
	}
	return(accum/(double)(ptListArray.length - 1)); // don't count point in question
}

public double
getVariance(int ptID, double mean)
{
	Object[] ptListArray = getVolumeIntersectList().toArray();
	Point3d ptAtID = (Point3d)ptListArray[ptID];
	Point3d searchPt = new Point3d();
	double accum = 0.0;

	for (int i = 0;i < ptListArray.length;i++)
	{
		if (i == ptID) // don't deal with pt in question
			continue;
		searchPt.set((Point3d)ptListArray[i]);
		double dist = (double)ptAtID.distance(searchPt);
		accum += ((dist - mean) * (dist - mean));
	}
	return(accum/(double)(ptListArray.length - 1)); // don't count point in question
}

public void
transform(Matrix4d mat)
{
	for (int i = 0;i < this.size();i++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(i);
		sphereInfo.transform(mat);
	}
}

// transform around origin
public void
transform(double transX, double transY, double transZ)
{
	this.transform(BMatrix4d.setTransformation(new BVector3d(),
		MathDefines.DegToRad * transX,
		MathDefines.DegToRad * transY,
		MathDefines.DegToRad * transZ));
}

// transform around a translated origin; this is not the same as
// first doing a transform and then a shift
public void
transform(double transX, double transY, double transZ,
	double shiftX, double shiftY, double shiftZ)
{
	this.transform(BMatrix4d.setTransformation(
		new BVector3d(shiftX, shiftY, shiftZ),
		MathDefines.DegToRad * transX,
		MathDefines.DegToRad * transY,
		MathDefines.DegToRad * transZ));
}

public void
centerAtOrigin()
{
	double minX = Double.MAX_VALUE;
	double maxX = -Double.MAX_VALUE;
	double minY = Double.MAX_VALUE;
	double maxY = -Double.MAX_VALUE;
	double minZ = Double.MAX_VALUE;
	double maxZ = -Double.MAX_VALUE;

	for (int i = 0;i < this.size();i++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(i);
		double radius = sphereInfo.getRadius();
		if (minX > sphereInfo.getX() - radius)
			minX = sphereInfo.getX() - radius;
		if (minY > sphereInfo.getY() - radius)
			minY = sphereInfo.getY() - radius;
		if (minZ > sphereInfo.getZ() - radius)
			minZ = sphereInfo.getZ() - radius;
		if (maxX < sphereInfo.getX() + radius)
			maxX = sphereInfo.getX() + radius;
		if (maxY < sphereInfo.getY() + radius)
			maxY = sphereInfo.getY() + radius;
		if (maxZ < sphereInfo.getZ() + radius)
			maxZ = sphereInfo.getZ() + radius;
	}
	
	double centerX = minX + ((maxX - minX)/2.0);
	double centerY = minY + ((maxY - minY)/2.0);
	double centerZ = minZ + ((maxZ - minZ)/2.0);

	/*
	for (int i = 0;i < this.size();i++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(i);
		sphereInfo.setPos(sphereInfo.getX() - centerX, sphereInfo.getY() - centerY, sphereInfo.getZ() - centerZ);
	}
	*/
	this.shift(centerX, centerY, centerZ);
}

public void
shift(double dX, double dY, double dZ)
{
	for (int i = 0;i < this.size();i++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(i);
		sphereInfo.setPos(sphereInfo.getX() - dX, sphereInfo.getY() - dY, sphereInfo.getZ() - dZ);
	}
}

public void
printXML(Object outFile)
throws Exception
{
	PrintWriter out = (PrintWriter)outFile;
	String tabSpaces = "    ";

	out.println("<!DOCTYPE CloudsXMLDocument SYSTEM '/home/donohue/xrna/xrna4/xrna/clouds/CloudsXML.dtd'>");
	out.println();

	out.println("<CloudsXMLDocument>");

	for (int i = 0;i < this.size();i++)
		this.getSphereInfoAt(i).printXML(out);

	out.println("</CloudsXMLDocument>");
}

public Vector
toPtList()
{
	Vector tmpVector = new Vector();
	for (int i = 0;i < this.size();i++)
		tmpVector.add(this.getSphereInfoAt(i).getBVector3d());

	return (tmpVector);
}

public String
toString()
{
	StringBuffer strBuf = new StringBuffer();

	for (int i = 0;i < this.size();i++)
	{
		SphereInfo sphereInfo = this.getSphereInfoAt(i);
		strBuf.append(sphereInfo.toString() + "\n");
	}
	return (strBuf.toString());
}

private static void
debug(String s)
{
	System.out.println("SphereInfoList-> " + s);
}

public static void getMoment(Vector srfPtList, BPoint3d meanPt2, BPoint3d variancePt2) {
	// TODO Auto-generated method stub
	
}

}

