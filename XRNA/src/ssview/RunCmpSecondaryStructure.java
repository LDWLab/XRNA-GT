package ssview;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.tree.*;

public class
RunCmpSecondaryStructure
{

private static SSData2D templateSStr = null;

public
RunCmpSecondaryStructure()
throws Exception
{
}

// -cmp_ss -reformat e.coli_5s test_junk e.coli.5s_changed_helices.txt
public
RunCmpSecondaryStructure(boolean reformat, String xrnaFileName, String newName, String bpInfoFileName)
throws Exception
{
	this.setReformat(reformat);
	this.setXRNAFileName(xrnaFileName);
	this.setNewName(newName);
	this.setBPInfoFileName(bpInfoFileName);

	this.runCmpSecondaryStructure();
}

public static void
main(String[] args)
{
	// debug("ARGS: " + args.length);
	try
	{
		if ((args.length == 5) && (args[1].toLowerCase().equals("-reformat")))
		{
			new RunCmpSecondaryStructure(true, args[2], args[3], args[4]);
		}
		else if (args.length == 4)
		{
			new RunCmpSecondaryStructure(false, args[1], args[2], args[3]);
		}
	}
	catch (Exception e)
	{
		debug("GOT Exception: " + e.toString());
		ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(
			new DataOutputStream(excptArray)));
		debug(new String(excptArray.toByteArray()));
		System.exit(0);
	}
}

private boolean
hasChangesToSS_Added_BP(RNAHelix helix)
throws Exception
{
	Vector delineators = helix.getItemListDelineators();
	if (delineators == null)
		return (false);

	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			if (nuc.isFlagged())
			{
				// debug("helix has changed: " + startStrandNuc.getID());
				return (true);
			}
			if (nuc == endStrandNuc)
				break;
			nuc = nuc.nextNuc2D();
		}
	}
	
	return (false);
}

private boolean
hasChangesToSS_Added_Single_Stranded_Nuc(RNASingleStrand singleStrand)
throws Exception
{
	Vector delineators = singleStrand.getItemListDelineators();
	if (delineators == null)
		return (false);

	for (int i = 0;i < delineators.size();i+=2)
	{
		Nuc2D startStrandNuc = (Nuc2D)delineators.elementAt(i);
		Nuc2D endStrandNuc = (Nuc2D)delineators.elementAt(i+1);
		Nuc2D nuc = startStrandNuc;
		while (true)
		{
			if (nuc.isFlagged())
			{
				// debug("singleStrand has changed: " + startStrandNuc.getID());
				return (true);
			}
			if (nuc == endStrandNuc)
				break;
			nuc = nuc.nextNuc2D();
		}
	}
	
	return (false);
}

public void
runCmpSecondaryStructure()
throws Exception
{
	ComplexXMLParser complexXMLParser = new ComplexXMLParser();
	if (this.getXRNAFileName().endsWith(".xrna"))
		complexXMLParser.parse(new FileReader(this.getXRNAFileName()));
	else
		complexXMLParser.parse(new FileReader(this.getXRNAFileName() + ".xrna"));
	ComplexScene2D complexScene = complexXMLParser.getComplexScene();

	SSData2D sstr = complexScene.getFirstComplexSSData2D();
	// assertTrue(sstr != null);

	complexXMLParser = new ComplexXMLParser();
	if (this.getXRNAFileName().endsWith(".xrna"))
		complexXMLParser.parse(new FileReader(this.getXRNAFileName()));
	else
		complexXMLParser.parse(new FileReader(this.getXRNAFileName() + ".xrna"));
	complexScene = complexXMLParser.getComplexScene();
	SSData2D newSStr = complexScene.getFirstComplexSSData2D();
	// assertTrue(newSStr != null);
	newSStr.clearFlagged();
	newSStr.unsetBasePairs();

	File bpInfoFile = new File(this.getBPInfoFileName());
	if (!bpInfoFile.exists())
	{
		debug("Can't find base pair info file: " + this.getBPInfoFileName());
		return;
	}
	NucCollection.setBasePairs(newSStr, HelixInfo.getHelixInfoList(bpInfoFile));

	for (int nucID = 1;nucID <= newSStr.getNucCount();nucID++)
	{
		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		if (nuc == null)
			continue;
		Nuc2D newNuc = newSStr.getNuc2DAt(nucID);
		if (newNuc == null)
			continue;

		if (newNuc.isBasePair() && nuc.isBasePair())
			continue;
		if (newNuc.isSingleStranded() && nuc.isSingleStranded())
			continue;

		// assume secondary structure has changed

		newNuc.setColor(Color.red);
		newNuc.setFlagged(true);
	}

	// Now reconfigure secondary structure graph

	if (this.getReformat())
	{
		for (int nucID = 1;nucID <= newSStr.getNucCount();nucID++)
		{
			NucNode nuc = newSStr.getNucAt(nucID);
			if (nuc == null)
				continue;
			if (!nuc.isHelixStart())
				continue;
			RNAHelix2D helix = new RNAHelix2D(nuc);
			// assertTrue(helix != null);
			boolean helixHasChanged = hasChangesToSS_Added_BP(helix);
			if (!helixHasChanged)
				continue;
			helix.reformat();
			if (nuc.getID() <= 1)
				continue;

			NucNode ssNuc = null;
			// look behind helix on 5' side:
			int tmpNucID = nucID - 1;
			if (tmpNucID > 1)
			{
				ssNuc = newSStr.getNucAt(tmpNucID);
				if ((ssNuc != null) && (ssNuc.isSingleStranded()))
					(new RNASingleStrand2D(ssNuc)).reformatArc();
			}

			// look ahead of helix on 5' side:
			tmpNucID = helix.getFivePrimeEndNuc().getID() + 1;
			if (tmpNucID < newSStr.getNucCount())
			{
				ssNuc = newSStr.getNucAt(tmpNucID);
				if ((ssNuc != null) && (ssNuc.isSingleStranded()))
					(new RNASingleStrand2D(ssNuc)).reformatArc();
			}

			// look behind helix on 3' side:
			tmpNucID = helix.getThreePrimeStartNuc().getID() - 1;
			if (tmpNucID > 1)
			{
				ssNuc = newSStr.getNucAt(tmpNucID);
				if ((ssNuc != null) && (ssNuc.isSingleStranded()))
					(new RNASingleStrand2D(ssNuc)).reformatArc();
			}

			// look ahead of helix on 3' side:
			tmpNucID = helix.getThreePrimeEndNuc().getID() + 1;
			if (tmpNucID < newSStr.getNucCount())
			{
				ssNuc = newSStr.getNucAt(tmpNucID);
				if ((ssNuc != null) && (ssNuc.isSingleStranded()))
					(new RNASingleStrand2D(ssNuc)).reformatArc();
			}
		}

		// assertTrue(!newSStr.getNucAt(1).isSingleStrandStartNuc());
		// assertTrue(!newSStr.getNucAt(10).isSingleStrandStartNuc());
		// assertTrue(newSStr.getNucAt(11).isSingleStrandStartNuc());
		// assertTrue(!newSStr.getNucAt(12).isSingleStrandStartNuc());
		for (int nucID = 1;nucID <= newSStr.getNucCount();nucID++)
		{
			NucNode nuc = newSStr.getNucAt(nucID);
			if (nuc == null)
				continue;
			if (!nuc.isSingleStrandStartNuc())
				continue;
			RNASingleStrand2D singleStrand = new RNASingleStrand2D(nuc);

			boolean singleStrandHasChanged = hasChangesToSS_Added_Single_Stranded_Nuc(singleStrand);
			if (!singleStrandHasChanged)
				continue;

			// check if isStraightLine in real structure; if not then check if it used to
			// be a basepair and try and get format info from bp.
			if (singleStrand.isStraightLine())
			{
				Nuc2D checkNuc = sstr.getNuc2DAt(nuc.getID());
				if (checkNuc.isBasePair())
				{
					boolean bpIsClockWiseFormatted = (new RNABasePair2D(checkNuc)).isClockWiseFormatted();
					singleStrand.formatArc(bpIsClockWiseFormatted);
				}
				else
				{
					RNASingleStrand2D checkSingleStrand = new RNASingleStrand2D(checkNuc);
					if (checkSingleStrand.isStraightLine())
						singleStrand.formatDelineatedNucLine();
				}
			}
			else
			{
				singleStrand.formatArc();
			}
		}
	}


	File xrnaFile = null;
	String outFileName = null;
	if (this.getNewName().endsWith(".xrna"))
	{
		outFileName = this.getNewName();
		xrnaFile = new File(outFileName);
	}
	else
	{
		outFileName = this.getNewName() + ".xrna";
		xrnaFile = new File(outFileName);
	}

	System.out.println("Output in: " + outFileName);

	complexScene.printComplexXML(xrnaFile);
}

private boolean reformat = false;

public void
setReformat(boolean reformat)
{
    this.reformat = reformat;
}

public boolean
getReformat()
{
    return (this.reformat);
}

private String xrnaFileName = null;

public void
setXRNAFileName(String xrnaFileName)
{
    this.xrnaFileName = xrnaFileName;
}

public String
getXRNAFileName()
{
    return (this.xrnaFileName);
}

private String newName = null;

public void
setNewName(String newName)
{
    this.newName = newName;
}

public String
getNewName()
{
    return (this.newName);
}

private String bpInfoFileName = null;

public void
setBPInfoFileName(String bpInfoFileName)
{
    this.bpInfoFileName = bpInfoFileName;
}

public String
getBPInfoFileName()
{
    return (this.bpInfoFileName);
}

private static void
debug(String s)
{
	System.err.println("RunCmpSecondaryStructure-> " + s);
}	

}
