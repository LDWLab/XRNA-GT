package ssview;

import java.io.*;
import java.util.*;
import javax.swing.tree.*;

public class
RunComplexAlignment
{

private static SSData2D templateSStr = null;

public
RunComplexAlignment()
throws Exception
{
}

public
RunComplexAlignment(String templateName, String newName, String alignFileName)
throws Exception
{
	this.setTemplateName(templateName);
	this.setNewName(newName);
	this.setAlignFileName(alignFileName);
	this.runComplexAlignment();
}

public
RunComplexAlignment(String[] args)
throws Exception
{
	this (args[1], args[2], args[3]);
}

public static void
main(String[] args)
{
	try
	{
		new RunComplexAlignment(args[0], args[1], args[2]);
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

public void
runComplexAlignment()
throws Exception
{
	ComplexAlignment alignment = null;
	if (this.getAlignFileName().toLowerCase().endsWith(".gb"))
		alignment = new ComplexAlignment(this.getAlignFileName(),
			ComplexAlignment.GENBANK_INPUT);
	else
		alignment = new ComplexAlignment(this.getAlignFileName());

	// temporary location, need to pass into this method:
	alignment.setIncludeExtraneousLabels(true);

	if (!alignment.getAlignmentSet())
	{
		System.err.println("Error: in alignment file parsing of " + this.getAlignFileName());
		return;
	}

	/*
	for (Enumeration e = alignment.keys();e.hasMoreElements();)
	{
		String seqName = (String)e.nextElement();
		Vector seqInfo = (Vector)alignment.get(seqName);
		System.err.println("nucStats for: " + seqName);
		alignment.printNucStats(seqName);
		for (int i = 0;i < seqInfo.size();i++)
		{
			Character c = (Character)seqInfo.elementAt(i);
			// System.err.print(c);
		}
		// System.err.println();
	}
	// alignment.printPrimaryStructure("e.coli.5s");
	*/

	if (!alignment.containsKey(this.getTemplateName()))
	{
		System.err.println("Error: alignment file " + this.getAlignFileName() + " does not contain " +
			this.getTemplateName());
		return;
	}

	Vector templateSeqInfo = (Vector)alignment.get(this.getTemplateName());
	for (int col = 0;col < templateSeqInfo.size();col++)
	{
		char nucChar = ((Character)templateSeqInfo.elementAt(col)).charValue();
		if (ComplexAlignment.isValidGapChar(nucChar))
			continue;
		if (!NucNode.isValidNucChar(Character.toUpperCase(nucChar)))
		{
			System.err.println("Error: Non valid nuc char in template sequence, "
				+ nucChar + " at column " + col);
			System.err.println("Template primary sequence in alignment file must be ");
			System.err.println("well represented and match primary sequence in XRNA I/O file.");
			return;
		}
	}

	if (!alignment.containsKey(this.getNewName()))
	{
		System.err.println("Error: alignment file " + this.getAlignFileName() + " does not contain " +
			this.getNewName());
		return;
	}

	Vector newSeqInfo = (Vector)alignment.get(this.getNewName());
	for (int col = 0;col < newSeqInfo.size();col++)
	{
		char nucChar = ((Character)newSeqInfo.elementAt(col)).charValue();
		if (ComplexAlignment.isValidGapChar(nucChar))
			continue;
		if (!NucNode.isValidNucChar(Character.toUpperCase(nucChar)))
		{
			System.err.println("Warning: Non valid nuc char in new sequence, "
				+ nucChar + " at column " + col + " ... treating as gap char");
		}
	}

	ComplexXMLParser complexXMLParser = new ComplexXMLParser();

	File templateFile = new File(this.getTemplateName() + ".xrna");
	if (!templateFile.exists())
	{
		System.err.println("Error: " + templateFile.getName() + " does not exist");
		System.err.println("There must be a XRNA input file (*.xrna) with the prefix name");
		System.err.println("corresponding to parent_name, the first command argument");
		return;
	}

	complexXMLParser.parse(new FileReader(templateFile));

	ComplexScene2D complexScene = complexXMLParser.getComplexScene();
	DefaultMutableTreeNode node = (DefaultMutableTreeNode)complexScene.createComplexNodes();
	SSData2D templateSStr = null;
	if (node.getLeafCount() > 1)
	{
		templateSStr = (SSData2D)complexScene.getChildByName(
			this.getTemplateName() + "&" + this.getTemplateName(), '&');
		/*
		System.err.println("error, template structure can currently only contain one rna strand");
		return;
		*/
	}
	else
	{
		templateSStr = complexScene.getFirstComplexSSData2D();
	}
	if (templateSStr == null)
	{
		System.err.println("error, template structure not created");
		return;
	}

	SSData2D newSStr = alignment.mapSequence(templateSStr, this.getTemplateName(), this.getNewName());
	if (newSStr == null)
	{
		System.err.println("error, new structure could not be created");
		return;
	}

	File newFile = new File(this.getNewName() + ".xrna");
	if (newFile.exists() && !newFile.canWrite())
	{
		System.err.println("Error: " + newFile.getName() + " cannot be created or is not writeable");
		return;
	}
	newSStr.wrapInComplexScene2D(this.getNewName()).printComplexXML(newFile);
	System.out.println("XRNA I/O output in: " + this.getNewName() + ".xrna");
}

private String templateName = null;

public void
setTemplateName(String templateName)
{
    this.templateName = templateName;
}

public String
getTemplateName()
{
    return (this.templateName);
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

private String alignFileName = null;

public void
setAlignFileName(String alignFileName)
{
    this.alignFileName = alignFileName;
}

public String
getAlignFileName()
{
    return (this.alignFileName);
}


/*********************** END INNER CLASSES ******************************/

private static void
debug(String s)
{
	System.err.println("RunComplexAlignment-> " + s);
}	

}
