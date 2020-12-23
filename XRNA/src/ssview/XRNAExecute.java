package ssview;

import java.util.*;
import java.io.*;

public class
XRNAExecute
{

public
XRNAExecute()
{
}

public
XRNAExecute(String outFileName, String scriptName)
{
	this.setOutFileName(outFileName);
	//this.setScriptName(scriptName); ///////commented out
    
    this.setScriptName("/Users/amk0013/Desktop/School Spring 2015/Ribovision Research/workspace/xrna1.2.0/ssview/single_bp_helix.xrna"); /////////////added...didnt help
    
    System.out.println("Set outputfile and input file"); //////////added
	this.runExecute();
}

public static void
main(String args[])
{
	if (args.length < 2)
	{
		System.out.println("USAGE java XRNAExecute <outputfile> <script>");
		System.exit(1);
	}
    
    System.out.println("Correct number of args");///////////////added
	XRNAExecute dummy = (new XRNAExecute(args[0], args[1]));
}

public void
runExecute()
{
    System.out.println("Inside method runExecute()");
	try
	{            
		FileOutputStream fos = new FileOutputStream(this.getOutFileName());
        
        System.out.println("Made new outputfile object"); ////added
        
		Runtime rt = Runtime.getRuntime();
        System.out.println("Made it past getruntime object");////added
        
        System.out.println(rt.exec("ls"));/////////added
        
		Process proc = rt.exec(this.getScriptName());
        System.out.println("executed the script"); /////////added
        
		// any error message?
		StreamGobbler errorGobbler = new 
		StreamGobbler(proc.getErrorStream(), "ERROR");            
		
		// any output?
		StreamGobbler outputGobbler = new 
		StreamGobbler(proc.getInputStream(), "OUTPUT", fos);
			
		// kick them off
		errorGobbler.start();
		outputGobbler.start();
								
		// any error???
		int exitVal = proc.waitFor();
		// System.out.println("ExitValue: " + exitVal);
		fos.flush();
		fos.close();        
	}
	catch (Throwable t)
	{
		t.printStackTrace();
	}
}

private String outFileName = null;

public void
setOutFileName(String outFileName)
{
    this.outFileName = outFileName;
}

public String
getOutFileName()
{
    return (this.outFileName);
}

private String scriptName = null;

public void
setScriptName(String scriptName)
{
    this.scriptName = scriptName;
}

public String
getScriptName()
{
    return (this.scriptName);
}

}

class StreamGobbler
extends Thread
{

InputStream is;
String type;
OutputStream os;

StreamGobbler(InputStream is, String type)
{
	this (is, type, null);
}

StreamGobbler(InputStream is, String type, OutputStream redirect)
{
	this.is = is;
	this.type = type;
	this.os = redirect;
}

public void
run()
{
	try
	{
		PrintWriter pw = null;
		if (os != null)
			pw = new PrintWriter(os);
			
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null)
		{
			if (pw != null)
				pw.println(line);
			// System.out.println(type + ">" + line);    
		}
		if (pw != null)
			pw.flush();
	}
	catch (IOException ioe)
	{
		ioe.printStackTrace();  
	}
}

}
