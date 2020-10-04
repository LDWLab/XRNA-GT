package jimage;

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
ImgAppsTabs
extends JFrame
{

JTabbedPane tabbedPane = null;
private int guiStartX = 300;
private int guiStartY = 30;
private int guiWidth = 800;
private int guiHeight = 600;

public
ImgAppsTabs()
{
	try
	{
		setBounds(guiStartX, guiStartY, guiWidth, guiHeight);
		buildGui();
		pack();
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
	(new ImgAppsTabs()).setVisible(true);
}

protected void
buildGui()
{
	// create tab 
	tabbedPane = new JTabbedPane();
	tabbedPane.setPreferredSize(new Dimension(700, 600));
	getContentPane().add(tabbedPane, BorderLayout.CENTER);	

	ViewImgOpsPanel viewImgOpsPanel = new ViewImgOpsPanel();
	tabbedPane.addTab("Render Img", viewImgOpsPanel);

	GenImgPanel genImgPanel = new GenImgPanel(3.0, this);
	tabbedPane.addTab("Draw Obj", genImgPanel);


	GenTxtBts genTxtBts = new GenTxtBts(1.0, this);
	tabbedPane.addTab("Gen Txt Bts", genTxtBts);

	/*
	JColorChooser colorChooser = new JColorChooser(new Color(0xff333399));
	AbstractColorChooserPanel[] acList = colorChooser.getChooserPanels();
	for (int i = 0;i < acList.length;i++)
		debug(" AC : " + acList[i].toString());
	try
	{
		colorChooser.removeChooserPanel(acList[2]);
	}
	catch (IllegalArgumentException e)
	{
		debug("MADE IT");
	}

	colorChooser.setPreviewPanel(new JPanel());
	colorChooser.addChooserPanel(new HexColorChooserPanel());
	tabbedPane.addTab("color chooser", colorChooser);
	*/

	SplitPaneViewImgOpsPanel splitPaneViewImgOpsPanel =
		new SplitPaneViewImgOpsPanel(1.0, this);
	tabbedPane.addTab("Split Pane Test", splitPaneViewImgOpsPanel);

	// tabbedPane.addTab("JPEG TEST", new JPEGWriterTest());

	// HERE
	tabbedPane.setSelectedIndex(3);

	tabbedPane.getModel().addChangeListener(new ChangeListener()
	{
		public void
		stateChanged(ChangeEvent e)
		{
			SingleSelectionModel model =
				(SingleSelectionModel) e.getSource();
			// debug("PANE: " + model.getSelectedIndex());
			/*
			if(model.getSelectedIndex() == tabbedPane.getTabCount()-1)
				spin.go();
			*/
		}
	});

	addWindowListener(new WindowAdapter()
	{
		public void
		windowClosing(WindowEvent event)
		{
			Object object = event.getSource();
			if (object == ImgAppsTabs.this)
				imgAppsTabsExit(event);
		}
	});
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
	  default :
		break;
	}
	if (id >= 100)
		imgAppsTabsExit();
}

public void
imgAppsTabsExit(java.awt.event.WindowEvent event)
{
	imgAppsTabsExit();
}

public void
imgAppsTabsExit()
{
	setVisible(false); // hide the Frame
	dispose();       // free the system resources
	System.exit(0);    // close the application
}

private static void
debug(String s)
{
	System.out.println(s);
}

}
