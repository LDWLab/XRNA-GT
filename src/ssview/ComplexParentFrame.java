package ssview;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;

public class
ComplexParentFrame
extends JFrame
{

private int guiStartX = 300;
private int guiStartY = 20;
// this sets the guiWidth
private int guiWidth = 900;
private int guiHeight = 670;

//private static String complexXMLRepositoryPath = "/home/donohue/xrna4/xmlstuff/";
//private static String propertiesPath = "/home/donohue/xrna/xrna4/xrna/ssview/complexview.properties";
private static String complexXMLRepositoryPath = "";
private static String propertiesPath = "";

//private static String propertiesPath = "/home/ascari/bryn/PERFORCE/XRNA/ssview/complexview.properties";

public
ComplexParentFrame()
{
	this.initComplexParentFrame();
}

public
ComplexParentFrame(String fileName)
{
	this.setRunWithFileName(fileName);
	this.initComplexParentFrame();
}

public static JInternalFrame frame;

private void
initComplexParentFrame()
{
	Properties complexViewProperties = null;
	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	try
	{
		if (this.getRunWithFileName() == null)
		{
			if (complexViewProperties == null)
				complexViewProperties = getComplexViewProperties(propertiesPath);
			if (complexViewProperties != null)
			{

				debug("setting values here");

				String value = complexViewProperties.getProperty("runTest");
				if (value != null)
					this.setRunWithFileName(value);
			
				value = complexViewProperties.getProperty("repositoryPath");
				if (value != null)
					complexXMLRepositoryPath = value; 
			}
		}


		setBounds(guiStartX, guiStartY, guiWidth, guiHeight);
		// this background is quickly overwritten
		setBackground(new Color(0xff000000));
		buildGui();
		// pack();
		setVisible(true);
	}
	catch(Exception e)
	{
		handleException("Exception From Constructor: ", e, 1);
	}
}

static public void
main(String args[])
{
	(new ComplexParentFrame()).setVisible(true);
}

JDesktopPane deskTop = null;
private Integer FIRST_FRAME_LAYER = new Integer(1);
private Integer POPUP_FRAME_LAYER = new Integer(2);
public ComplexSceneView complexSceneView = null;

protected void
buildGui()
throws Exception
{
	deskTop = new JDesktopPane();

	complexSceneView = new ComplexSceneView(this.getRunWithFileName(), this);
	complexSceneView.setComplexXMLRepositoryPath(complexXMLRepositoryPath); 
//	debug(" data here " + complexXMLRepositoryPath);
	JPanel complexSceneViewPanel = (JPanel)complexSceneView;
	complexSceneViewPanel.setVisible(true);

	JInternalFrame jif = new JInternalFrame();
	frame = jif;
	jif.setBounds(10, 10, guiWidth - 26, guiHeight - 44);
	jif.setIconifiable(true);
	jif.setMaximizable(true);
	jif.setResizable(true);

	JComponent c = (JComponent)jif.getContentPane();
    c.add(complexSceneViewPanel);
	c.setVisible(true);
	jif.setVisible(true);
	deskTop.add(jif, FIRST_FRAME_LAYER);
	jif.setMaximum(true);

	// getContentPane().add(complexSceneViewPanel, BorderLayout.CENTER);	
	getContentPane().add(deskTop);

	complexSceneView.runInitFromProperties();

	addWindowListener(new WindowAdapter()
	{
		public void
		windowClosing(WindowEvent event)
		{
			Object object = event.getSource();
			int result = complexSceneView.stopXRNA();
			switch (result)
			{
			  case 0: // yes
				complexParentFrameExit(event);
				break;
			  case 1: // no
				return;
			  case 2: // cancel (same as no?)
				return;
			  default:
				break;
			}
			if (object == ComplexParentFrame.this)
				complexParentFrameExit(event);
		}
	});
}

public void
addInternalFrame(JInternalFrame jif)
{
	JInternalFrame[] jifList = deskTop.getAllFrames();
	for (int i = 0;i < jifList.length;i++)
		if ((JInternalFrame)jifList[i] == jif)
			return;
	deskTop.add(jif, POPUP_FRAME_LAYER);
}

private void
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
	  default:
		break;
	}
	if (id >= 100)
		complexParentFrameExit();
}

public void
complexParentFrameExit(java.awt.event.WindowEvent event)
{
	complexParentFrameExit();
}

public void
complexParentFrameExit()
{
	setVisible(false); // hide the Frame
	dispose();       // free the system resources
	System.exit(0);    // close the application
}

private Properties
getComplexViewProperties(String propertiesFileName)
{
	Properties p = null;

	try
	{
		// set up new properties object from file "complexView.properties"
		FileInputStream propFile = new FileInputStream(propertiesFileName);
		p = new Properties(System.getProperties());
		p.load(propFile);

		// set the system properties
		System.setProperties(p);

		// list to standard out
		//System.getProperties().list(System.out);
		/*
		String path=System.getProperty("java.class.path");
		debug("CLASSPATH: " + path);
		*/
	}
	catch (java.io.FileNotFoundException fnfe)
	{
		return (null);
	}
	catch (java.io.IOException ioe)
	{
		return (null);
	}
	catch (Exception e)
	{
		return (null);
	}
	return (p);
}

private String runWithFileName = null;

public void
setRunWithFileName(String runWithFileName)
{
    this.runWithFileName = runWithFileName;
}

public String
getRunWithFileName()
{
    return (this.runWithFileName);
}


private static void
debug(String s)
{
	System.err.println(s);
}

}
