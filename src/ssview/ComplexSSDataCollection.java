package ssview;

import java.io.*;
import java.util.Vector;

/**
** Contains rna secondary structures, ribosomal proteins secondary
** structures, and any other secondary structures of molecules,
** ligands, etc., that make up a Complex.
** These parts may be interacting or not. Contains labels specific
** to these items.
*/

public class
ComplexSSDataCollection
extends ComplexScene
{

public
ComplexSSDataCollection()
{
	super();
}

public
ComplexSSDataCollection(String name, String author)
{
	this();
	this.setName(name);
	this.setAuthor(author);
}

public
ComplexSSDataCollection(String name)
{
	this();
	this.setName(name);
}

public
ComplexSSDataCollection(String name, NucCollection nucCollection)
throws Exception
{
	this();
	this.setName(name);
	this.addItem(nucCollection);
}

/*
private void
GetPDBDataFromFile(String fileName) throws IOException
{
	InputStream in;

	if(fileName.length() == 0)
		in = System.in;
	else
		in = new FileInputStream(fileName);
	StreamTokenizer st =
		new StreamTokenizer(new BufferedInputStream(in, 4000));
	st.eolIsSignificant(true);
	st.commentChar('#');
	st.wordChars('a', 'z');
	st.wordChars('A', 'Z');
	st.wordChars(128 + 32, 255);
	st.wordChars(' ', ' ');
	st.wordChars('-', '-');
	st.wordChars('.', '.');
	st.wordChars('*', '*');

	try
	{
		String atomtype;
		double tsx, tsy, tsz;
		int nuc;
scan :
		while(true)
		{
			st.nextToken();
			switch(st.ttype)
			{
			  case StreamTokenizer.TT_EOL:
				break;
			  case StreamTokenizer.TT_EOF:
				break scan;
			  case StreamTokenizer.TT_WORD:
				if(!st.sval.startsWith("ATOM"))
					break;
				atomtype = st.sval.substring(12, 16);
				boolean phosphatehit = false, nonphosphatehit = false;
				for(int i = 0;i < atomtype.length();i++) //get phosphates only
				{
					if(atomtype.charAt(i) == ' ')
						continue;
					if(atomtype.charAt(i) == 'P')
						phosphatehit = true;
					else
						nonphosphatehit = true;
				}
				if(nonphosphatehit || !phosphatehit)
					break;
				tsx = (double)Double.valueOf(st.sval.substring(27, 38).trim()).doubleValue();
				tsy = (double)Double.valueOf(st.sval.substring(38, 46).trim()).doubleValue();
				tsz = (double)Double.valueOf(st.sval.substring(46, 54).trim()).doubleValue();
				nuc = Integer.parseInt(st.sval.substring(20, 28).trim());
				Vector nucvec = getCurrentSSData().NucNodeList();
				NucNode tmpNucNode = (NucNode)nucvec.elementAt(nuc);
				tmpNucNode.setNucTSPoint(tsx, tsy, tsz);
				break;
			  default:
				break;
			} // end Switch
		} // end while
	} // end try
	catch(IOException e) {}
	in.close();
	if (st.ttype != StreamTokenizer.TT_EOF)
		throw new IOException(st.toString());
} // end GetPDBDataFromFile()
*/

public void
setNewSSComplexElement(SSData newSSData)
throws Exception
{
	// first check if newSSData is already in
	for(int complexID = 0;complexID < this.getItemCount();complexID++)
	{
		Object obj = this.getItemAt(complexID);

		if (obj == null)
			continue;
		if (obj instanceof SSData)
		{
			SSData ssData = (SSData)obj;
			if(ssData.getName().equals(newSSData.getName()))
				throw new Exception("Error in ComplexSSDataCollection.setNewSSComlexElement: " +
					newSSData.getName() + " is already in Complex");
		}
	}
	this.setCurrentSSData(newSSData);
	addItem(this.getCurrentSSData());
}

private SSData currentSSData = null;

public void
setCurrentSSData(SSData currentSSData)
{
	this.currentSSData = currentSSData;
}

public SSData
getCurrentSSData()
{
	return(currentSSData);
}

public void
setCurrentStructureToNull()
{
	setCurrentSSData((SSData)null);
}

public void
setCurrentSSData(String sstrName)
{
	setCurrentSSData(this.getSStrByName(sstrName));
}

public SSData
getSStrByName(String sstrName)
{
	for(int complexID = 0;complexID < this.getItemCount();complexID++)
	{
		Object obj = this.getItemAt(complexID);

		if (obj instanceof SSData)
		{
			SSData ssData = (SSData)obj;
			if(ssData.getName().equals(sstrName))
				return(ssData);
		}
	}
	return(null);
}

private int modeType = ComplexDefines.InRNASingleNuc;

public void
setModeType(int modeType)
{
	this.modeType = modeType;
	// debug("MODE TYPE: " + this.getModeType());
}

public int
getModeType()
{
	return (this.modeType);
}

public void
resetNucNumbers()
throws Exception
{
}

private void
debug(String s)
{
	System.err.println(s);
}

}
