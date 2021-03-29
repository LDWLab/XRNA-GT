package ssview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jimage.DrawObject;
import jimage.DrawObjectCollection;
import jimage.GenFileFilter;

public class
ComplexSceneIOTab
extends ComplexSceneWorkTab
{

/* SVE FOR DEBUG PURPOSES:
public JCheckBox showBoundingShapes_CB = null;
*/
public JCheckBox landscapeMode_CB = null;
private JCheckBox showCartesianCoordsCB = null;
private ComplexScene2D ioComplexScene = null;
public JTextField psScale_TF = null;

public
ComplexSceneIOTab()
{
	this.buildGui(complexSceneView.guiBGColor,
		complexSceneView.controlPanelW, complexSceneView.controlPanelH);
}

public
ComplexSceneIOTab(Color guiBGColor, int panelW, int panelH)
{
	this.guiBGColor = guiBGColor;
	this.buildGui(guiBGColor, panelW, panelH);
}

private JFileChooser genFileChooser = null;

public void
setPostBuildGuiMethods()
throws Exception
{
	super.setPostBuildGuiMethods();

	landscapeMode_CB = new JCheckBox();
	if (complexSceneView.getComplexScene() != null)
	{
		landscapeMode_CB.setSelected(true);
		complexSceneView.setLandscapeModeOn(true);
		complexSceneView.setLandscapeView(true, false);
	}
	else
	{
		landscapeMode_CB.setSelected(false);
		complexSceneView.setLandscapeModeOn(false);
		complexSceneView.setLandscapeView(false, false);
	}
	landscapeMode_CB.setText("landscape mode");
	landscapeMode_CB.setActionCommand("landscape_mode");
	landscapeMode_CB.setFont(new Font("Dialog", Font.BOLD, 12));
	landscapeMode_CB.setForeground(Color.black);
	landscapeMode_CB.setBackground(guiBGColor);
	controlBtPanel.add(landscapeMode_CB);
	landscapeMode_CB.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			try
			{
				boolean landscapeModeOn = landscapeMode_CB.isSelected();
				complexSceneView.setLandscapeModeOn(landscapeModeOn);

				// this currently renders:
				complexSceneView.setLandscapeView(landscapeModeOn, true);

				if (complexSceneView.getComplexScene() != null)
					complexSceneView.getComplexScene().setLandscapeMode(landscapeModeOn);
			}
			catch (Exception e)
			{
				handleException("Exception in landscapeMode_CB.addItemListener:", e, 101);
			}
		}
	});

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	psScale_TF = new JTextField(3);
	if (complexSceneView.getComplexScene() != null)
		psScale_TF.setText("" + complexSceneView.getComplexScene().getPSScale());
	else
		psScale_TF.setText("" + 1.0);
	psScale_TF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double val = 0.0;
			try
			{
				val = Double.valueOf(psScale_TF.getText()).doubleValue();
			}
			catch (NumberFormatException e)
			{
				alert("Number format error for postscript scale value: " + psScale_TF.getText());
				return;
			}
			if (complexSceneView.getComplexScene() != null)
				complexSceneView.getComplexScene().setPSScale(val);
		}
	});
	tmpPanel.add(this.getNewViewLabel("postscript scale: "));
	tmpPanel.add(psScale_TF);
	JButton applyPSScale_Bt = this.getNewViewImgPlainButton();
	applyPSScale_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double val = 0.0;
			try
			{
				val = Double.valueOf(psScale_TF.getText()).doubleValue();
			}
			catch (NumberFormatException e)
			{
				alert("Number format error for postscript scale value: " + psScale_TF.getText());
				return;
			}
			if (complexSceneView.getComplexScene() != null)
				complexSceneView.getComplexScene().setPSScale(val);
		}
	});
	tmpPanel.add(applyPSScale_Bt);
	controlBtPanel.add(tmpPanel);

	JButton printStructureBt = this.getNewViewButton("Print Postscript");
	printStructureBt.setActionCommand(ComplexDefines.IO_PRINT_STRUCTURE);
	printStructureBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double val = 0.0;
			try
			{
				val = Double.valueOf(psScale_TF.getText()).doubleValue();
			}
			catch (NumberFormatException e)
			{
				alert("Number format error for postscript scale value: " + psScale_TF.getText());
				return;
			}
			if (complexSceneView.getComplexScene() != null)
				complexSceneView.getComplexScene().setPSScale(val);
			complexSceneView.setCursor(complexSceneView.complexWaitCursor);
			complexSceneView.runComplexPrinterJob(true, null);
			complexSceneView.setCursor(complexSceneView.complexDefaultCursor);
		}
	});
	controlBtPanel.add(printStructureBt);

/*
	JButton pdfBt = this.getNewViewButton("PDF");
	// pdfBt.setActionCommand(ComplexDefines.IO_PRINT_STRUCTURE);
	pdfBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			complexSceneView.setCursor(complexSceneView.complexWaitCursor);
			try
			{
				//alert("NOT YET");
				
				
				if (complexSceneView.getComplexScene() != null)
				complexSceneView.setCursor(complexSceneView.complexWaitCursor);
				complexSceneView.runPDFPrinterJob(true, null);
				complexSceneView.setCursor(complexSceneView.complexDefaultCursor);
				//if (false)
				//	throw new Exception("dummy exception");
			}
			catch (Exception e)
			{
				debug("ERROR");
			}
			complexSceneView.setCursor(complexSceneView.complexDefaultCursor);
		}
	});
	controlBtPanel.add(pdfBt);
*/
	
	/* SVE FOR DEBUG PURPOSES:
	showMousePosCB = new JCheckBox();
	showMousePosCB.setSelected(false);
	showMousePosCB.setText("show mouse pos");
	showMousePosCB.setActionCommand("show mouse pos");
	showMousePosCB.setFont(new Font("Dialog", Font.BOLD, 12));
	showMousePosCB.setForeground(Color.black);
	showMousePosCB.setBackground(guiBGColor);
	controlBtPanel.add(showMousePosCB);
	showMousePosCB.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			complexSceneView.setShowMousePos(
				ComplexSceneIOTab.this.showMousePosCB.isSelected());
		}
	});
	*/

	/* SVE FOR DEBUG PURPOSES:
	showBoundingShapes_CB = new JCheckBox();
	showBoundingShapes_CB.setSelected(false);
	showBoundingShapes_CB.setText("show bounding shapes");
	showBoundingShapes_CB.setActionCommand("show bounding shapes");
	showBoundingShapes_CB.setFont(new Font("Dialog", Font.BOLD, 12));
	showBoundingShapes_CB.setForeground(Color.black);
	showBoundingShapes_CB.setBackground(guiBGColor);
	controlBtPanel.add(showBoundingShapes_CB);
	showBoundingShapes_CB.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			try
			{
				complexSceneView.getComplexScene().traverseSceneShowBoundingShape(
					showBoundingShapes_CB.isSelected());
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				handleException("Exception in showBoundingShapes_CB.addItemListener:", e, 101);
			}
		}
	});
	*/

	showCartesianCoordsCB = new JCheckBox();
	showCartesianCoordsCB.setSelected(false);
	showCartesianCoordsCB.setText("cartesian coords");
	showCartesianCoordsCB.setFont(new Font("Dialog", Font.BOLD, 12));
	showCartesianCoordsCB.setForeground(Color.black);
	showCartesianCoordsCB.setBackground(guiBGColor);
	controlBtPanel.add(showCartesianCoordsCB);
	showCartesianCoordsCB.addItemListener(new ItemListener()
	{
		public void
		itemStateChanged(ItemEvent event)
		{
			try
			{
				complexSceneView.setDrawWithCoords(
					showCartesianCoordsCB.isSelected());
				complexSceneView.drawCartesianCoords();

				complexSceneView.renderDrawObjectView();
				// complexSceneView.updateImg();
			}
			catch (Exception e)
			{
				handleException("Exception in showCartesianCoordsCB.addItemListener:", e, 101);
			}
		}
	});

	JButton checkPseudoKnotsBt = new JButton("Check Pseudo-Knots");
	checkPseudoKnotsBt.setActionCommand("Check_Pseudo-Knots");
	checkPseudoKnotsBt.setFont(new Font("Dialog", Font.BOLD, 12));
	checkPseudoKnotsBt.setForeground(Color.black);
	checkPseudoKnotsBt.setBackground(guiBGColor);

	btInsets = checkPseudoKnotsBt.getInsets();
	checkPseudoKnotsBt.setMargin(
		new Insets(btInsets.top, 0, btInsets.bottom, btInsets.right + lftInset + 8));

	// NEED to put this in somewhere appropriate
	// controlBtPanel.add(checkPseudoKnotsBt);
	checkPseudoKnotsBt.addActionListener(new ActionListener()
	{

		public void
		actionPerformed(ActionEvent evt)
		{
			if (complexSceneView.getComplexScene() == null)
			{
				complexSceneView.printConsole("Display Secondary Structure First");
				return;
			}
			try
			{
				JPanel checkPseudoKnotsPanel =
					new JPanel(new GridLayout(20, 1), true);
				checkPseudoKnotsPanel.setBackground(complexSceneView.guiBGColor);
				checkPseudoKnotsPanel.add(new JButton("trna 12"));
				JFrame checkPseudoKnotsFrame = new JFrame("Pseudo-Knot Report");
				checkPseudoKnotsFrame.setBounds(110, 60, 200, 500);
				checkPseudoKnotsFrame.getContentPane().add(BorderLayout.CENTER, checkPseudoKnotsPanel);
				checkPseudoKnotsPanel.setVisible(true);
				// checkPseudoKnotsFrame.pack();
				checkPseudoKnotsFrame.setVisible(true);
			}
			catch (Exception e)
			{
				handleException("Exception in ComplexSceneView.checkPseudoKnotsBt:", e, 101);
			}
		}
	});

	genFileChooser = complexSceneView.genFileChooser;
	this.setMouseMethod();
	this.setKeyMethod();
}

// public for XRNARobot.java:
public JButton chooseInputFile_Bt = null;

/* SVE FOR DEBUG PURPOSES:
private JCheckBox showMousePosCB = null;
*/

public void
buildGui(Color guiBGColor, int panelW, int panelH)
{
	super.buildGui(guiBGColor, panelW, panelH);

	controlBtPanel = new JPanel(new GridLayout(15, 1), true);
	controlBtPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	controlBtPanel.setForeground(Color.black);
	controlBtPanel.setBackground(guiBGColor);
	
	chooseInputFile_Bt = new JButton();
	chooseInputFile_Bt.setText("Choose Input File");
	chooseInputFile_Bt.setFont(new Font("Dialog", Font.BOLD, 12));
	chooseInputFile_Bt.setForeground(Color.black);
	chooseInputFile_Bt.setBackground(guiBGColor);
	chooseInputFile_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				complexSceneView.setComplexXMLRepositoryPath(genFileChooser.getCurrentDirectory().toString());
				genFileChooser.setCurrentDirectory(new File(complexSceneView.getComplexXMLRepositoryPath()));

				File inFile = null;
				String currDir = null;

				if (complexSceneView.hostIsMac)
				{
					Frame f = new Frame("XRNA Load Frame");
					FileDialog dialog = new FileDialog(f, "XRNA Load Dialog", FileDialog.LOAD);
					// dialog.setFilenameFilter(());
					dialog.show();
					// debug("FILE NAME: " + dialog.getFile());
					// debug("DIR: " + dialog.getDirectory());
					inFile = new File(dialog.getDirectory() + "/" + dialog.getFile());
					currDir = inFile.getAbsolutePath();
					genFileChooser.setCurrentDirectory(new File(dialog.getDirectory()));
				}
				else
				{
					genFileChooser.setFileFilter(complexSceneView.genReadFileFilter);

					int returnVal =
						genFileChooser.showOpenDialog(ComplexSceneIOTab.this);

					// JAVA 1.3 bug: returnVal upon clicking on the upper-right
					// X box returns that last thing selected. This causes
					// one to reread the file on occasion. Work around is to
					// always click on Cancel for now.
					// debug("RETURNVAL: " + returnVal);

					if (returnVal != JFileChooser.APPROVE_OPTION)
						return;
					
					// JAVA 1.3 bug: when go to new directory that directory name is placed
					// into window as if it were a file to be selected. For now just don't select
					if (genFileChooser.getSelectedFile().isDirectory())
						return;
					inFile = genFileChooser.getSelectedFile();
					currDir = genFileChooser.getCurrentDirectory().toString();
				}

				// debug("currDir: " + currDir);
				// debug("inFile abs path: " + inFile.getAbsolutePath());
				// debug("inFile canon path: " + inFile.getCanonicalPath());

				complexSceneView.setCursor(complexSceneView.complexWaitCursor);
				complexSceneView.runInitFromProperties(inFile, currDir);
				complexSceneView.setCursor(complexSceneView.complexDefaultCursor);

				if (complexSceneView.getComplexScene() != null)
				{
					if (psScale_TF != null)
						psScale_TF.setText("" + complexSceneView.getComplexScene().getPSScale());

					// this may cause a second redraw:
					if (landscapeMode_CB != null)
						landscapeMode_CB.setSelected(complexSceneView.getComplexScene().getLandscapeMode());
				}
			}
			catch (Exception e)
			{
				handleException("Exception in genFileChooser:", e, 98);
			}
		}
	});
	controlBtPanel.add(chooseInputFile_Bt);
	
	// NEED to make pop up write window put the logical new extracted feature name
	// into file name spot. (SEE notes in todos).
	JButton writeXMLBt = this.getNewViewButton("Save in XML/XRNA Format");
	writeXMLBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
			if (complexSceneView.getComplexScene() == null)
			{
				alert("Display Secondary Structure First");
				return;
			}
////adding new file filter for csv and svg
			genFileChooser.setFileFilter(genWriteFileFilter);
			//genFileChooser.addChoosableFileFilter(genWriteFileFilterCSV);
			//genFileChooser.addChoosableFileFilter(genWriteFileFilterSVG);

			if (complexSceneView.getCurrentInputFile() == null)
				complexSceneView.setCurrentInputFile(new File("new_structure.xrna"));

			// Input File stats checked
			if ((!complexSceneView.getCurrentInputFile().isDirectory()) &&
				(complexSceneView.getCurrentInputFile().getName().endsWith(".xml") ||
				complexSceneView.getCurrentInputFile().getName().endsWith(".xrna")))
			{
				String filePrefixName = complexSceneView.getCurrentInputFile().getName();
				File newFile = new File(filePrefixName.substring(0, filePrefixName.lastIndexOf('.')) + ".xrna");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			}
			else if ((genFileChooser.getSelectedFile() == null) &&
				(complexSceneView.getCurrentInputFile().getName().endsWith(".ss") ||
				complexSceneView.getCurrentInputFile().getName().endsWith(".ps")))
			{
				// Maybe need to set complexSceneView.getCurrentInputFile() to this
				// new output xrna file:
				String filePrefixName = complexSceneView.getCurrentInputFile().getName();
				File newFile = new File(filePrefixName.substring(0, filePrefixName.lastIndexOf('.')) + ".xrna");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			}
			// If formerly saved as csv, it changes extension to xml
			else if ((genFileChooser.getSelectedFile() == null) &&
					(complexSceneView.getCurrentInputFile().getName().endsWith(".csv"))) {
				String filePrefixName = complexSceneView.getCurrentInputFile().getName();
				File newFile = new File(filePrefixName.substring(0, filePrefixName.lastIndexOf('.')) + ".xrna");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			}
			
			
			}
			catch (Exception e)
			{
				handleException("Exception in ComplexSceneIOTab.writeXMLBt:", e, 101);
			}
			// put back in if don't want previous selection to show
			// genFileChooser.setSelectedFile(null);
			try
			{
				boolean overWrite = false;
				boolean running = true;
				while (running)
				{
				if (genFileChooser == null)
					return;
					
						
					
				//Showing save options
				File selectedFile = null;
				if (!overWrite)
				{
					if (complexSceneView.hostIsMac)
					{
						int returnVal = genFileChooser.showSaveDialog(complexSceneView);
						
							if (returnVal == JFileChooser.CANCEL_OPTION) {
								genFileChooser.resetChoosableFileFilters();
								return;
							}
							if (returnVal == JFileChooser.ERROR_OPTION) {
								debug("ERROR IN OPENING FILE");
							}
							if (returnVal != JFileChooser.APPROVE_OPTION) {
								return;
							}
					
							selectedFile = genFileChooser.getSelectedFile();
						
					}
					else
					{
						int returnVal = genFileChooser.showSaveDialog(complexSceneView);
						
						if (returnVal == JFileChooser.CANCEL_OPTION) {
							genFileChooser.resetChoosableFileFilters();
							return;
						}
						if (returnVal == JFileChooser.ERROR_OPTION)
							debug("ERROR IN OPENING FILE");
						if (returnVal != JFileChooser.APPROVE_OPTION)
							return;
						selectedFile = genFileChooser.getSelectedFile();
					}
				}
			
				if (selectedFile == null)
				{
					return;
				}
				else if (selectedFile.exists() && (!overWrite))
				{
					Object[] message = new Object[3];
					message[0] = new JLabel("File Exists");
					message[1] = new JLabel("Overwrite: " + selectedFile.getName() + " ?");
					
					Component parent = complexSceneView;

					String[] options =
					{
						"Yes",
						"Cancel"
					};
					int result = JOptionPane.showOptionDialog(
						parent,  // the parent that the dialog blocks
						message,  // the dialog message array
						"File Exists", // the title of the dialog window
						JOptionPane.OK_CANCEL_OPTION, // option type
						JOptionPane.PLAIN_MESSAGE, // message type
						null, // optional icon, use null to use the default icon
						options, // options string array, will be made into buttons
						options[0] // option that should be made into a default button
						);

					switch (result)
					{
					  case 0: // yes
						// JOptionPane.showMessageDialog(parent, "Made it 0");
						overWrite = true;
						try {
							selectedFile.delete();
						}
						catch (Exception e){
							alert("Error, file not overwritten:\n" + e.toString());
						}
						complexSceneView.complexSceneOutFile = selectedFile;
						
						
						//.xrna and xml calls runWriteXMLBt()
						if ((selectedFile.getName().endsWith(".xrna")) || (selectedFile.getName().endsWith(".xml"))) {
							try
							{
								boolean writeComplete = complexSceneView.runWriteXMLBt();
							}
							catch (Exception e)
							{
								alert("Error, file not written:\n" + e.toString());
								handleException("Exception in ComplexSceneIO.WriteXML:", e, 101);
							}
							running = false;
						}
						
						else {
							//System.out.println("Not a valid extension");
							alert("Not a valid extension.\nNeed to change extension to .xrna or .xml");
								
						}
						break;
					  case 1: // no
						// JOptionPane.showMessageDialog(parent, "Made it 1");
						running = false;
						break;
					  default:
						break;
					}
					
				}
				else
				{
					complexSceneView.complexSceneOutFile = selectedFile;
					
			
					//.xrna and xml calls runWriteXMLBt()
					if ((selectedFile.getName().endsWith(".xrna")) || (selectedFile.getName().endsWith(".xml"))) {
						try
						{
							boolean writeComplete = complexSceneView.runWriteXMLBt();
						}
						catch (Exception e)
						{
							alert("Error, file not written:\n" + e.toString());
							handleException("Exception in ComplexSceneIO.WriteXML:", e, 101);
						}
						running = false;
					}
					
					else {
						//System.out.println("Not a valid extension");
						alert("Not a valid extension.\nNeed to change extension to .xrna or .xml");
							
					}
					
				}
				}
			}
			catch (Exception e)
			{
				handleException("Exception in genFileChooser.showSaveDialog:", e, 101);
			}
			genFileChooser.resetChoosableFileFilters();
		}
		
	});
	controlBtPanel.add(writeXMLBt);
	
	JButton writeCSVBt = this.getNewViewButton("Save in CSV Format");
	writeCSVBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
			if (complexSceneView.getComplexScene() == null)
			{
				alert("Display Secondary Structure First");
				return;
			}
////adding new file filter for csv and svg
			genFileChooser.setFileFilter(genWriteFileFilterCSV);
			

			if (complexSceneView.getCurrentInputFile() == null)
				complexSceneView.setCurrentInputFile(new File("new_structure.csv"));

			// Input File stats checked
			if ((!complexSceneView.getCurrentInputFile().isDirectory()) &&
				(complexSceneView.getCurrentInputFile().getName().endsWith(".xml") ||
				complexSceneView.getCurrentInputFile().getName().endsWith(".xrna")))
			{
				String filePrefixName = complexSceneView.getCurrentInputFile().getName();
				File newFile = new File(filePrefixName.substring(0, filePrefixName.lastIndexOf('.')) + ".csv");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			}
			else if ((genFileChooser.getSelectedFile() == null) &&
				(complexSceneView.getCurrentInputFile().getName().endsWith(".ss") ||
				complexSceneView.getCurrentInputFile().getName().endsWith(".ps")))
			{
				// Maybe need to set complexSceneView.getCurrentInputFile() to this
				// new output xrna file:
				String filePrefixName = complexSceneView.getCurrentInputFile().getName();
				File newFile = new File(filePrefixName.substring(0, filePrefixName.lastIndexOf('.')) + ".csv");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			}
			
			}
			catch (Exception e)
			{
				handleException("Exception in ComplexSceneIOTab.writeXMLBt:", e, 101);
			}
			// put back in if don't want previous selection to show
			// genFileChooser.setSelectedFile(null);
			try
			{
				boolean overWrite = false;
				boolean running = true;
				while (running)
				{
				if (genFileChooser == null)
					return;
					
						
					
				//Showing save options
				File selectedFile = null;
				if (!overWrite)
				{
					if (complexSceneView.hostIsMac)
					{
						int returnVal = genFileChooser.showSaveDialog(complexSceneView);
						
							if (returnVal == JFileChooser.CANCEL_OPTION) {
								genFileChooser.resetChoosableFileFilters();
								return;
							}
							if (returnVal == JFileChooser.ERROR_OPTION) {
								debug("ERROR IN OPENING FILE");
							}
							if (returnVal != JFileChooser.APPROVE_OPTION) {
								return;
							}
					
							selectedFile = genFileChooser.getSelectedFile();
						
					}
					else
					{
						int returnVal = genFileChooser.showSaveDialog(complexSceneView);
						
						if (returnVal == JFileChooser.CANCEL_OPTION) {
							genFileChooser.resetChoosableFileFilters();
							return;
						}
						if (returnVal == JFileChooser.ERROR_OPTION)
							debug("ERROR IN OPENING FILE");
						if (returnVal != JFileChooser.APPROVE_OPTION)
							return;
						selectedFile = genFileChooser.getSelectedFile();
					}
				}
			
				if (selectedFile == null)
				{
					return;
				}
				else if (selectedFile.exists() && (!overWrite))
				{
					Object[] message = new Object[3];
					message[0] = new JLabel("File Exists");
					message[1] = new JLabel("Overwrite: " + selectedFile.getName() + " ?");
					
					Component parent = complexSceneView;

					String[] options =
					{
						"Yes",
						"Cancel"
					};
					int result = JOptionPane.showOptionDialog(
						parent,  // the parent that the dialog blocks
						message,  // the dialog message array
						"File Exists", // the title of the dialog window
						JOptionPane.OK_CANCEL_OPTION, // option type
						JOptionPane.PLAIN_MESSAGE, // message type
						null, // optional icon, use null to use the default icon
						options, // options string array, will be made into buttons
						options[0] // option that should be made into a default button
						);

					switch (result)
					{
					  case 0: // yes
						// JOptionPane.showMessageDialog(parent, "Made it 0");
						overWrite = true;
						try {
							selectedFile.delete();
						}
						catch (Exception e){
							alert("Error, file not overwritten:\n" + e.toString());
						}
						
						complexSceneView.complexSceneOutFile = selectedFile;
						
						//.csv calls runWriteCSVBt()
						if (selectedFile.getName().endsWith(".csv")) {
							//System.out.println("This will soon be a csv file");
							try
							{
								boolean writeComplete = complexSceneView.runWriteCSVBt();
							}
							catch (Exception e)
							{
								alert("Error, file not written:\n" + e.toString());
								handleException("Exception in ComplexSceneIO.WriteCSV:", e, 101);
							}
							running = false;
						}
						else {
							//System.out.println("Not a valid extension");
							alert("Not a valid extension.\nNeed to change extension to .csv");
								
						}
						
						break;
					  case 1: // no
						// JOptionPane.showMessageDialog(parent, "Made it 1");
						running = false;
						break;
					  default:
						break;
					}
					
				}
				else
				{
					complexSceneView.complexSceneOutFile = selectedFile;
					
					//.csv calls runWriteCSVBt()
					if (selectedFile.getName().endsWith(".csv")) {
						//System.out.println("This will soon be a csv file");
						try
						{
							boolean writeComplete = complexSceneView.runWriteCSVBt();
						}
						catch (Exception e)
						{
							alert("Error, file not written:\n" + e.toString());
							handleException("Exception in ComplexSceneIO.WriteCSV:", e, 101);
						}
						running = false;
					}
					else {
						//System.out.println("Not a valid extension");
						alert("Not a valid extension.\nNeed to change extension to .csv");
							
					}
					
				}
				}
			}
			catch (Exception e)
			{
				handleException("Exception in genFileChooser.showSaveDialog:", e, 101);
			}
			genFileChooser.resetChoosableFileFilters();
		}

	});
	controlBtPanel.add(writeCSVBt);
	
	JButton writeImgBt = this.getNewViewButton("Write JPG Image");
	writeImgBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			boolean overWrite = false;
			genFileChooser.setFileFilter(genWriteFileFilter);
			// MAYBE Take out:
			// jpd was comment below
			//if ((genFileChooser.getSelectedFile() == null) && (
			//	!complexSceneView.getCurrentInputFile().isDirectory()
			//		))
			//	genFileChooser.setSelectedFile(
			//		complexSceneView.getCurrentInputFile());
			// jpd was commented above
			// put back in if don't want previous selection to show
			// genFileChooser.setSelectedFile(null);
			if (complexSceneView.getComplexScene() == null)
			{
				alert("Display Secondary Structure First");
				return;
			}

			GenFileFilter imgWriteFileFilter = new GenFileFilter();
			imgWriteFileFilter.addExtension("jpg");
			imgWriteFileFilter.setDescription("JPG Image Files");
			genFileChooser.setFileFilter(imgWriteFileFilter);

			//NEED to fix up when get straightened out
			//genFileChooser.setSelectedFile(complexSceneView.getCurrentInputFile());
			
			genFileChooser.setSelectedFile(new File(complexSceneView.getComplexScene().getName() + ".jpg"));
			try
			{
				boolean running = true;
				while (running)
				{
				if (genFileChooser == null)
					return;
				if (!overWrite)
				{
				int returnVal = genFileChooser.showSaveDialog(complexSceneView);
				if (returnVal == JFileChooser.ERROR_OPTION)
					debug("ERROR IN OPENING FILE");
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;
				}

				File selectedFile = genFileChooser.getSelectedFile();
				if (selectedFile == null)
				{
					return;
				}
				else if (selectedFile.exists() && (!overWrite))
				{
					Object[] message = new Object[3];
					message[0] = new JLabel("File Exists");
					message[1] = new JLabel("Overwrite: " + selectedFile.getName() + " ?");
					
					Component parent = complexSceneView;

					String[] options =
					{
						"Yes",
						"Cancel"
					};
					int result = JOptionPane.showOptionDialog(
						parent,  // the parent that the dialog blocks
						message,  // the dialog message array
						"File Exists", // the title of the dialog window
						JOptionPane.OK_CANCEL_OPTION, // option type
						JOptionPane.PLAIN_MESSAGE, // message type
						null, // optional icon, use null to use the default icon
						options, // options string array, will be made into buttons
						options[0] // option that should be made into a default button
						);

					switch (result)
					{
					  case 0: // yes
						// JOptionPane.showMessageDialog(parent, "Made it 0");
						overWrite = true;
						break;
					  case 1: // no
						// JOptionPane.showMessageDialog(parent, "Made it 1");
						running = false;
						break;
					  default:
						break;
					}
				}
				else
				{
					try
					{
						File genImgOutFile = genFileChooser.getSelectedFile();

						complexSceneView.setCursor(complexSceneView.complexWaitCursor);
						String outFileName = null;
						// was commented below
						//outFileName = JAIUtil.printImage(complexSceneView.getSceneImg(),
						//	genImgOutFile);
						
						//JAI.create(complexSceneView.getSceneImg(),
						//		genImgOutFile);
						// was commented above
						try{
							
							
//							 BufferedImage newImg = new BufferedImage(
//						        ofImg.getWidth(), ofImg.getHeight(),
				            // BufferedImage.TYPE_INT_RGB);
//						        ofImg.getType());
//							Graphics2D g2 = newImg.createGraphics();
//							AffineTransform af = new AffineTransform();
//							AffineTransformOp ato = new AffineTransformOp(af,
//							GraphicsUtil.imageRenderHints);
//							g2.drawImage(ofImg, ato, 0, 0);

							ImageIO.write( complexSceneView.getSceneImg()  ,"jpg" , genImgOutFile);
						}catch (IOException ex){
							ex.printStackTrace();
						}
						
						complexSceneView.setCursor(complexSceneView.complexDefaultCursor);
						if (outFileName == null) {
							complexSceneView.printConsole("ERROR: IMAGE NOT PRINTED");
						} else {
							complexSceneView.printConsole("Image printed to: " + outFileName);
						}
					}
					catch (Exception e)
					{
						alert("Error, file not written:\n" + e.toString());
						handleException("Exception in ComplexSceneIO.WriteXML:", e, 101);
					}
					running = false;
				}
				}
			}
			catch (Exception e)
			{
				handleException("Exception in genFileChooser.showSaveDialog:", e, 101);
			}
			
	
		}

	});
	controlBtPanel.add(writeImgBt);
	
	JButton writeTravelerBtn = this.getNewViewButton("Save in TR Format");
	writeTravelerBtn.addActionListener(event -> {
		try {
			if (complexSceneView.getComplexScene() == null) { 
				alert("Display Secondary Structure First");
				return;
			}
			genFileChooser.setFileFilter(genWriteFileFilterTR);
			if (complexSceneView.getCurrentInputFile() == null) { complexSceneView.setCurrentInputFile(new File("new_structure.tr")); }
			File cur;
			String curName;
			if (!(cur = complexSceneView.getCurrentInputFile()).isDirectory() && ((curName = cur.getName()).endsWith(".xrna") || curName.endsWith(".xrna"))) {
				File newFile = new File(curName.substring(0, curName.lastIndexOf('.')) + ".tr");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			} else if (genFileChooser.getSelectedFile() == null && ((curName = (cur = complexSceneView.getCurrentInputFile()).getName()).endsWith(".ss") || curName.endsWith(".ps"))) {
				File newFile = new File(curName.substring(0, curName.lastIndexOf('.')) + ".tr");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			}
		} catch (Exception ex) { handleException("Exception in ComplexSceneIOTab.writeXMLBt", ex, 101); } 
		try {
			boolean overwrite = false, running = true;
			while (running) {
				if (genFileChooser != null) {
					File selectedFile = null;
					if (!overwrite) {
						int returnVal = genFileChooser.showSaveDialog(complexSceneView);
						if (returnVal == JFileChooser.CANCEL_OPTION) {
							genFileChooser.resetChoosableFileFilters();
							return;
						}
						if (returnVal == JFileChooser.ERROR_OPTION) { debug("ERROR IN OPENING FILE"); }
						if (returnVal != JFileChooser.APPROVE_OPTION) { return; }
						selectedFile = genFileChooser.getSelectedFile();
					}
					if (selectedFile != null) { 
						if (selectedFile.exists() && !overwrite) {
							switch (JOptionPane.showOptionDialog(
								complexSceneView,
								new Object[] {new JLabel("File Exists"), new JLabel("Overwrite: " + selectedFile.getName() + "?"), null},
								"File Exists",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.PLAIN_MESSAGE,
								null,
								new String[] {"Yes", "Cancel"},
								"Yes")) {
								case 0:
									overwrite = true;
									try { selectedFile.delete(); }
									catch (Exception ex) { alert("Error, file not overwritten:\n" + ex); }
									
									complexSceneView.complexSceneOutFile = selectedFile;
									if (selectedFile.getName().endsWith(".tr")) {
										try { complexSceneView.runWriteTRBt(); }
										catch (Exception ex) {
											alert("Error, file not written:\n" + ex);
											handleException("Exception in ComplexSceneIO.WriteTR:", ex, 101);
										}
										running = false;
									} else { alert("Not a valid extension.\nNeed to change extension to .tr"); }
									break;
								case 1:
									running = false;
									break;
							}
						} else {
							complexSceneView.complexSceneOutFile = selectedFile;
							
							if (selectedFile.getName().endsWith(".tr")) {
								try { complexSceneView.runWriteTRBt(); }
								catch (Exception ex) {
									alert("Error, file not written:\n" + ex);
									handleException("Exception in ComplexSceneIO.WriteTR:", ex, 101);
								}
								running = false;
							} else { alert("Not a valid extension.\nNeed to change extension to .tr"); }
						}
					}
				}
			}
		} catch (Exception ex) { handleException("Exception in genFileChooser.showSaveDialog:", ex, 101); }
		genFileChooser.resetChoosableFileFilters();
	});
	controlBtPanel.add(writeTravelerBtn);
	
	JButton
		writeSVGBtn = this.getNewViewButton("Save in SVG Format");
	writeSVGBtn.addActionListener(event -> {
		try {
			if (complexSceneView.getComplexScene() == null) { 
				alert("Display Secondary Structure First");
				return;
			}
			genFileChooser.setFileFilter(genWriteFileFilterSVG);
			if (complexSceneView.getCurrentInputFile() == null) { complexSceneView.setCurrentInputFile(new File("new_structure.svg")); }
			File cur;
			String curName;
			if (!(cur = complexSceneView.getCurrentInputFile()).isDirectory() && ((curName = cur.getName()).endsWith(".xrna") || curName.endsWith(".xrna"))) {
				File newFile = new File(curName.substring(0, curName.lastIndexOf('.')) + ".svg");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			} else if (genFileChooser.getSelectedFile() == null && ((curName = (cur = complexSceneView.getCurrentInputFile()).getName()).endsWith(".ss") || curName.endsWith(".ps"))) {
				File newFile = new File(curName.substring(0, curName.lastIndexOf('.')) + ".svg");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			}
		} catch (Exception ex) { handleException("Exception in ComplexSceneIOTab.writeSVGBtn", ex, 101); } 
		try {
			boolean
				overwrite = false,
				running = true;
			while (running) {
				if (genFileChooser != null) {
					File selectedFile = null;
					if (!overwrite) {
						int returnVal = genFileChooser.showSaveDialog(complexSceneView);
						if (returnVal == JFileChooser.CANCEL_OPTION) {
							genFileChooser.resetChoosableFileFilters();
							return;
						}
						if (returnVal == JFileChooser.ERROR_OPTION) { debug("ERROR IN OPENING FILE"); }
						if (returnVal != JFileChooser.APPROVE_OPTION) { return; }
						selectedFile = genFileChooser.getSelectedFile();
					}
					if (selectedFile != null) { 
						if (selectedFile.exists() && !overwrite) {
							switch (JOptionPane.showOptionDialog(
								complexSceneView,
								new Object[] {new JLabel("File Exists"), new JLabel("Overwrite: " + selectedFile.getName() + "?"), null},
								"File Exists",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.PLAIN_MESSAGE,
								null,
								new String[] {"Yes", "Cancel"},
								"Yes")) {
								case 0:
									overwrite = true;
									try { selectedFile.delete(); }
									catch (Exception ex) { alert("Error, file not overwritten:\n" + ex); }
									
									complexSceneView.complexSceneOutFile = selectedFile;
									if (selectedFile.getName().endsWith(".svg")) {
										running = this.printSVG(selectedFile, running);
									} else {
										alert("Not a valid extension.\nNeed to change extension to .svg");
									}
									break;
								case 1:
									running = false;
									break;
							}
						} else {
							complexSceneView.complexSceneOutFile = selectedFile;
							running = this.printSVG(selectedFile, running);
						}
					}
				}
			}
		} catch (Exception ex) { handleException("Exception in genFileChooser.showSaveDialog:", ex, 101); }
		genFileChooser.resetChoosableFileFilters();
	});
	controlBtPanel.add(writeSVGBtn);
    
	JButton writeBPSeq = this.getNewViewButton("Save in BPSeq Format");
	writeBPSeq.addActionListener(event -> {
		try {
			if (complexSceneView.getComplexScene() == null) { 
				alert("Display Secondary Structure First");
				return;
			}
			genFileChooser.setFileFilter(genWriteFileFilterBPSeq);
			if (complexSceneView.getCurrentInputFile() == null) { complexSceneView.setCurrentInputFile(new File("new_structure.bpseq")); }
			File cur;
			String curName;
			if (!(cur = complexSceneView.getCurrentInputFile()).isDirectory() && ((curName = cur.getName()).endsWith(".xrna") || curName.endsWith(".xrna"))) {
				File newFile = new File(curName.substring(0, curName.lastIndexOf('.')) + ".bpseq");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			} else if (genFileChooser.getSelectedFile() == null && ((curName = (cur = complexSceneView.getCurrentInputFile()).getName()).endsWith(".ss") || curName.endsWith(".ps"))) {
				File newFile = new File(curName.substring(0, curName.lastIndexOf('.')) + ".bpseq");
				genFileChooser.setSelectedFile(newFile);
				complexSceneView.setCurrentInputFile(newFile);
			}
		} catch (Exception ex) { handleException("Exception in ComplexSceneIOTab.writeBPSeqBtn", ex, 101); } 
		try {
			boolean
				overwrite = false,
				running = true;
			while (running) {
				if (genFileChooser != null) {
					File selectedFile = null;
					if (!overwrite) {
						int returnVal = genFileChooser.showSaveDialog(complexSceneView);
						if (returnVal == JFileChooser.CANCEL_OPTION) {
							genFileChooser.resetChoosableFileFilters();
							return;
						}
						if (returnVal == JFileChooser.ERROR_OPTION) { debug("ERROR IN OPENING FILE"); }
						if (returnVal != JFileChooser.APPROVE_OPTION) { return; }
						selectedFile = genFileChooser.getSelectedFile();
					}
					if (selectedFile != null) { 
						if (selectedFile.exists() && !overwrite) {
							switch (JOptionPane.showOptionDialog(
								complexSceneView,
								new Object[] {new JLabel("File Exists"), new JLabel("Overwrite: " + selectedFile.getName() + "?"), null},
								"File Exists",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.PLAIN_MESSAGE,
								null,
								new String[] {"Yes", "Cancel"},
								"Yes")) {
								case 0:
									overwrite = true;
									try { selectedFile.delete(); }
									catch (Exception ex) { alert("Error, file not overwritten:\n" + ex); }
									
									complexSceneView.complexSceneOutFile = selectedFile;
									if (selectedFile.getName().endsWith(".bpseq")) {
										running = this.printBPSeq(selectedFile, running);
									} else {
										alert("Not a valid extension.\nNeed to change extension to .bpseq");
									}
									break;
								case 1:
									running = false;
									break;
							}
						} else {
							complexSceneView.complexSceneOutFile = selectedFile;
							running = this.printBPSeq(selectedFile, running);
						}
					}
				}
			}
		} catch (Exception ex) { handleException("Exception in genFileChooser.showSaveDialog:", ex, 101); }
		genFileChooser.resetChoosableFileFilters();
	});
	controlBtPanel.add(writeBPSeq);

	controlBtPanelScrollPane = new JScrollPane(controlBtPanel,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	controlPanel.add(BorderLayout.CENTER, controlBtPanelScrollPane);
}

private boolean printSVG(File selected, boolean running) {
	if (selected.getName().endsWith(".svg")) {
		try {
			complexSceneView.runWriteSVGBt();
			
			// ComplexCollection line 857
				// ComplexScene2D line 366
				// ComplexSSDataCollection2D line 388
					// Line 406
				// NucCollection2D line 2215
			
		} catch (Exception ex) {
			alert("Error, file not written:\n" + ex);
			handleException("Exception in ComplexSceneIO.WriteSVG:", ex, 101);
		}
		running = false;
	} else { alert("Not a valid extension.\nNeed to change extension to .svg"); }
	return running;
}

private boolean printBPSeq(File selected, boolean running) {
	if (selected.getName().endsWith(".bpseq")) {
		try {
			complexSceneView.runWriteBPSeqBt();
		} catch (Exception ex) {
			alert("Error, file not written:\n" + ex);
			handleException("Exception in ComplexSceneIO.WriteBPSeq:", ex, 101);
		}
		running = false;
	} else { alert("Not a valid extension.\nNeed to change extension to .bpseq"); }
	return running;
}

/****************** SHOW PROPERTIES *******************************/

IOBtListener ioBtListener = new IOBtListener();

public Box
addIOProperties(Box panel)
throws Exception
{
	JButton extractRNAFeatureBt = this.getNewViewButton("Extract RNA Feature");
	extractRNAFeatureBt.setActionCommand(ComplexDefines.IO_EXTRACT_RNA_FEATURE);
	panel.add(this.getNewViewButtonPanel(extractRNAFeatureBt));
	extractRNAFeatureBt.addActionListener(ioBtListener);

	return (panel);
}

private class
IOBtListener
implements ActionListener
{
	public void
	actionPerformed(ActionEvent evt)
	{
		String actionCmd = evt.getActionCommand();
		try
		{
		Graphics2D g2 = complexSceneView.getCurrentGraphics2D();
		switch (getCurrentComplexPickMode())
		{
		  case ComplexDefines.InRNASingleNuc :
			Nuc2D nuc = getCurrentWorkNuc();
			if (nuc == null)
			{
				// alert("Must pick nuc to edit");
				return;
			}
			break;
		  case ComplexDefines.InRNASingleStrand :
			RNASingleStrand singleStrand = getCurrentWorkSingleStrand();
			if (singleStrand == null)
			{
				// alert("Must pick RNA single stranded region to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = singleStrand.createComplexScene();
			break;
		  case ComplexDefines.InRNABasePair :
			RNABasePair2D basePair = getCurrentWorkBasePair();
			if (basePair == null)
			{
				// alert("Must pick RNA Basepair to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = basePair.createComplexScene();
			break;
		  case ComplexDefines.InRNAHelix :
			RNAHelix2D helix = getCurrentWorkHelix();
			if (helix == null)
			{
				// alert("Must pick RNA Helix to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = helix.createComplexScene();
			break;
		  case ComplexDefines.InRNAHelicalRun :
		  	RNAStackedHelix2D helicalRun = getCurrentWorkStackedHelix();
			if (helicalRun == null)
			{
				// alert("Must pick RNA Helical Run to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = helicalRun.createComplexScene();
			break;
		  case ComplexDefines.InRNASubDomain :
			RNASubDomain2D subDomain = getCurrentWorkSubDomain();
			if (subDomain == null)
			{
				// alert("Must pick RNA Sub Domain to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = subDomain.createComplexScene();
			break;
		  case ComplexDefines.InRNANamedGroup :
			RNANamedGroup2D namedGroup = getCurrentWorkNamedGroup();
			if (namedGroup == null)
			{
				// alert("Must pick RNA Domain to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = namedGroup.createComplexScene();
			break;
		  case ComplexDefines.InRNAColorUnit :
			break;
		  case ComplexDefines.InRNACycle :
			RNACycle2D level = getCurrentWorkCycle();
			if (level == null)
			{
				// alert("Must pick RNA Cycle to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = level.createComplexScene();
			break;
		  case ComplexDefines.InRNAListNucs :
			RNAListNucs2D listNucs = getCurrentWorkListNucs();
			if (listNucs == null)
			{
				// alert("Must pick consecutive nucs to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = listNucs.createComplexScene();
			break;
		  case ComplexDefines.InRNASSData :
		  	SSData2D sstr = getCurrentWorkSStr();
			if (sstr == null)
			{
				// alert("Must pick Secondary Structure to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = sstr.createComplexScene();
			break;
		  case ComplexDefines.InComplex :
		  	ComplexSSDataCollection2D complex = getCurrentWorkRNASSComplex();
			if (complex == null)
			{
				// alert("Must pick Complex to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = null;
			break;
		  case ComplexDefines.InLabelsOnly :
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = null;
			break;
			/*
		  case ComplexDefines.InComplexArea :
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = null;
			break;
			*/
		  case ComplexDefines.InComplexScene :
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
				ioComplexScene = null;
			break;
		  default :
		  	break;
		}

		if (ioComplexScene != null)
		{
			if (actionCmd.equals(ComplexDefines.IO_EXTRACT_RNA_FEATURE))
			{
				complexSceneView.setComplexScene(ioComplexScene);
				complexSceneView.resetFigureScale(complexSceneView.getFigureScale());
				complexSceneView.resetImgTitle();
				complexSceneView.centerScrollBars();
			}
		}
		else
			debug("IOCOMPLEX IS NULL");

		}
		catch (Exception e)
		{
			handleException("Exception in ComplexSceneIO.IOBtListener:", e, 101);
		}
	}
}

public Box
showRNASingleNucProperties()
throws Exception
{
	return (null);
	/*
	Box mainPanel = super.showRNASingleNucProperties();
	addIOProperties(mainPanel);
	return (mainPanel);
	*/
}

public Box
showRNASingleStrandProperties()
throws Exception
{
	Box mainPanel = super.showRNASingleStrandProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNABasePairProperties()
throws Exception
{
	Box mainPanel = super.showRNABasePairProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNAHelixProperties()
throws Exception
{
	Box mainPanel = super.showRNAHelixProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNAStackedHelixProperties()
throws Exception
{
	Box mainPanel = super.showRNAStackedHelixProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNASubDomainProperties()
throws Exception
{
	Box mainPanel = super.showRNASubDomainProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNANamedGroupProperties()
throws Exception
{
	Box mainPanel = super.showRNANamedGroupProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNAColorUnitProperties()
throws Exception
{
	Box mainPanel = super.showRNAColorUnitProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNACycleProperties()
throws Exception
{
	Box mainPanel = super.showRNACycleProperties();

	/*
	addIOProperties(mainPanel);
	*/
	
	return (mainPanel);
}

public Box
showRNAListNucsProperties()
throws Exception
{
	Box mainPanel = super.showRNAListNucsProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNASSDataProperties()
throws Exception
{
	Box mainPanel = super.showRNASSDataProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showComplexProperties()
throws Exception
{
	Box mainPanel = super.showComplexProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showLabelsOnlyProperties()
throws Exception
{
	// alert("No I/O properties for Labels");
	return (null);

	/*
	Box mainPanel = super.showLabelsOnlyProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
	*/
}

/*
public Box
showComplexAreaProperties()
throws Exception
{
	Box mainPanel = super.showComplexAreaProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}
*/

public Box
showComplexSceneProperties()
throws Exception
{
	Box mainPanel = super.showComplexSceneProperties();

	addIOProperties(mainPanel);
	
	return (mainPanel);
}

/*************** MOUSE STUFF ***************************/

public void
runMousePressed(MouseEvent event)
throws Exception
{
	super.runMousePressed(event);
	if (this.getCurrentDrawObject() == null)
		return;
}

public void
runMouseDragged(int x, int y)
throws Exception
{
	super.runMouseDragged(x, y);
	/*
	if (this.getCurrentDrawObject() == null)
		return;
	DrawObject drawObject = this.getCurrentDrawObject();
	*/
}

public void
runMouseReleased()
throws Exception
{
	super.runMouseReleased();
	if (this.getCurrentDrawObject() == null)
		return;
	DrawObject drawObject = this.getCurrentDrawObject();
}

/*************  END MOUSE STUFF *********************/

/*************  MISC STUFF **************************/

public void
setColorChangedState()
{
	super.setColorChangedState();
	if (this.getColorChooser() == null)
		return;
	this.getColorChooser().getSelectionModel().addChangeListener(
	new ChangeListener()
	{
		public void
		stateChanged(ChangeEvent chgEvt)
		{
			// debug("IN EDIT TAB COLOR STATE CHANGED");
			//
			// if (bgColorRB.isSelected())
				// setImgPanelBGColor(colorChooser.getColor());
			// else if (bgBtColorRB.isSelected())
				// setBGBtColor(colorChooser.getColor());
			// else if (fgBtColorRB.isSelected())
				// setFGBtColor(colorChooser.getColor());
			// else if (topBtColorRB.isSelected())
				// setTopBtColor(colorChooser.getColor());
			// else if (bottomBtColorRB.isSelected())
				// setBottomBtColor(colorChooser.getColor());
			// else if (selectFGBtColorRB.isSelected())
			// setSelectFGBtColor(colorChooser.getColor());
			// complexSceneView.reDraw();
			//
		}
	});	
}

public static void
handleException(String extraMsg, Throwable t, int id)
{
	ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
	t.printStackTrace(new PrintStream(
		new DataOutputStream(excptArray)));
	debug(id + " " + extraMsg + t.toString() +
		(new String(excptArray.toByteArray())));
	switch(id)
	{
	  case 0 :
		break;
	  default :
		break;
	}
	/*
	if (id >= 100)
		System.exit(0);
	*/
}

private static void
debug(String s)
{
	System.err.println("ComplexSceneIOTab-> " + s);
}

}
