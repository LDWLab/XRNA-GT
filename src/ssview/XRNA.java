package ssview;

import java.io.*;

public class
XRNA
{

public
XRNA()
{
}

static public void
main(String args[])
{	
	//String version = "beta 1.2.0, 3/27/09";
	String version = "GT RiboVision Version 3/15/19";
	System.out.println("XRNA version: " + version);

	if (args.length == 0)
	{
		(new ComplexParentFrame()).setVisible(true);
		return;
	}

	String firstArg = args[0];
	if ((args.length == 1) && (firstArg.endsWith(".xml") ||
		firstArg.endsWith(".xrna") || firstArg.endsWith(".ss") ||
		firstArg.endsWith(".ps")))
	{
		try
		{
			(new ComplexParentFrame(firstArg)).setVisible(true);
		}
		catch (Throwable t)
		{
			handleException("HERE", t, 101);
		}
		return;
	}

	for (int i = 0;i < args.length;i++)
	{
		String arg = args[i];
		if (arg.equals("-version"))
		{
			System.out.println(version);
			return;
		}
		if (arg.equals("-robot_test"))
		{
			(new ComplexParentFrame()).setVisible(true);
			return;
		}
		if (arg.equals("-derive"))
		{
			if (args.length < 4)
			{
				System.out.println("usage: -derive template_name derived_name align_file");
				System.out.println("Where template_name is the name of the sequence in an");
				System.out.println(" alignment file acting as a template.");
				System.out.println("Where derived_name is the name of the sequence in an");
				System.out.println(" alignment file to be derived.");
				System.out.println("Where align_file is the name of the alignment file in");
				System.out.println(" mase or Genbank format");
				System.out.println("Currently an alignment file must have the extension '.mase'");
				System.out.println(" for a mase alignment file or '.gb' for a Genbank alignment file.");
				return;
			}
			try
			{
				RunComplexAlignment dummy = new RunComplexAlignment(args);
			}
			catch(Exception e)
			{
				debug("GOT Exception in main(): " + e.toString());
				ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(
					new DataOutputStream(excptArray)));
				debug(new String(excptArray.toByteArray()));
				System.exit(0);
			}
		}
		if (arg.equals("-cmp_ss"))
		{
			if (args.length < 4)
			{
				System.out.println("usage: -cmp_ss [-reformat] xrna_file_name new_file_name bp_info_file_name");
				System.out.println("Where the optional flag, -reformat, means to try and");
				System.out.println("	produce a xrna figure as close as possible to the original by");
				System.out.println("	reformatting areas that have changed from original.");
				System.out.println("Where xrna_file_name is the name of the xrna file to be compared against");
				System.out.println("Where new_file_name is the name of the xrna file that shows");
				System.out.println(" a new secondary structure (this name will have .xrna appended to it");
				System.out.println("	if it isn't already there).");
				System.out.println("Where bp_info_file_name contains base pair information in XRNA format");
				return;
			}
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
			catch(Exception e)
			{
				debug("GOT Exception in main(): " + e.toString());
				ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(
					new DataOutputStream(excptArray)));
				debug(new String(excptArray.toByteArray()));
				System.exit(0);
			}
		}
	}
}

private static void
handleException(String extraMsg, Throwable t, int id)
{
	ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
	t.printStackTrace(new PrintStream(
		new DataOutputStream(excptArray)));
	debug(id + "    " + extraMsg + t.toString() +
		(new String(excptArray.toByteArray())));
	switch(id)
	{
	  case 0 :
		break;
	  default :
		break;
	}
}

private static void
debug(String s)
{
	System.err.println(s);
}

}
