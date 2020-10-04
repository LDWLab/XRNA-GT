package ssview;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import jimage.DrawCharObject;
import jimage.DrawFontObject;
import jimage.DrawObject;
import jimage.DefaultDrawObjectPanel;
import jimage.DrawObjectCollection;
import jimage.DrawCircleObject;
import jimage.DefaultCirclePanel;
import jimage.DrawStringObject;
import jimage.DrawLineObject;
import jimage.DrawTriangleObject;
import jimage.DefaultTrianglePanel;
import jimage.DrawParallelogramObject;
import jimage.DefaultParallelogramPanel;
import jimage.FontChooser;
import jimage.GenFileFilter;

import util.*;

public class
ComplexSceneFormatSStrTab
extends ComplexSceneWorkTab
{

/********** COMMON STUFF FOR FORMATTING *******************/

private JRadioButton leftHandedRB = null;
private JRadioButton nullRB = null;

private JInternalFrame setDistancesFrame = null;
private JPanel setDistancesPanel = null;
public JTextField nucToNucDistanceTF = null;
public JTextField rnaHelixBaseDistanceTF = null;
public JTextField rnaBasePairDistanceTF = null;
public JTextField rnaMisMatchBasePairDistanceTF = null;

/********** END COMMON STUFF FOR FORMATTING *******************/

/*************** FORMAT NEW STRUCTURE STUFF **************************/
private JPanel formatNewStructurePanel = null;
private JInternalFrame formatNewStructureFrame = null;

private JInternalFrame editPSFrame = null;
private JPanel editPSPanel = null;
private JTextArea editPS_TA = null;
private JInternalFrame addEditHelixFrame = null;
private JPanel addEditHelixPanel = null;
private JTextArea helixList_TA = null;
private Vector redirectHelixList = null;

private JTextField newStr_Name_TF = null;
private JButton newStrNameBt = null;
private JTextField newStr_StartAngle_TF = null;
private JTextField newStr_ArcRadius_TF = null;

public JFileChooser	buildSStrFileChooser = null;
private GenFileFilter buildSStrReadFileFilter = null;

/*************** END FORMAT NEW STRUCTURE STUFF **************************/

protected transient PropertyChangeSupport
	propertyChangeListeners = null;

public
ComplexSceneFormatSStrTab()
{
	this.buildGui(complexSceneView.guiBGColor,
		complexSceneView.controlPanelW, complexSceneView.controlPanelH);
	propertyChangeListeners = new PropertyChangeSupport(this);	
}

public
ComplexSceneFormatSStrTab(Color guiBGColor, int panelW, int panelH)
{
	this.guiBGColor = guiBGColor;
	this.buildGui(guiBGColor, panelW, panelH);
	propertyChangeListeners = new PropertyChangeSupport(this);	
}

public void
setPostBuildGuiMethods()
throws Exception
{
	super.setPostBuildGuiMethods();
	this.setMouseMethod();
	this.setKeyMethod();
	this.resetFormatSStrRBs();
	this.buildEditPSPanel();
	this.buildAddHelixPanel();
	this.buildFormatHelixPanel();
	this.buildDistancesPanel();
	this.buildFormatNewStructurePanel();
	this.buildDeriveStructurePanel();
}

private void
buildDistancesPanel()
{
	SSData2D sstr = getCurrentWorkSStr();

	setDistancesPanel = new JPanel(new GridLayout(4, 1), true);
	setDistancesPanel.setFont(new Font("Helvetica", Font.PLAIN, 12));
	setDistancesPanel.setForeground(Color.black);
	setDistancesPanel.setBackground(guiBGColor);
	setDistancesPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	setDistancesPanel.setAlignmentX(LEFT_ALIGNMENT);

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("Nuc->Nuc+1 Distance:"));
	nucToNucDistanceTF = new JTextField("" + ComplexDefines.NUC_TO_NEXTNUC_DISTANCE, 4);
	tmpPanel.add(nucToNucDistanceTF);
	setDistancesPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("helix base distance:"));
	rnaHelixBaseDistanceTF = new JTextField("" + ComplexDefines.RNA_HELIX_BASE_DISTANCE, 4);
	tmpPanel.add(rnaHelixBaseDistanceTF);
	setDistancesPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("helix BP distance:"));
	rnaBasePairDistanceTF = new JTextField("" + ComplexDefines.RNA_BASEPAIR_DISTANCE, 4);
	tmpPanel.add(rnaBasePairDistanceTF);
	setDistancesPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("mis-match distance:"));
	rnaMisMatchBasePairDistanceTF = new JTextField("" + ComplexDefines.RNA_MISMATCH_BASEPAIR_DISTANCE, 4);
	tmpPanel.add(rnaMisMatchBasePairDistanceTF);
	setDistancesPanel.add(tmpPanel);
}

private void
buildEditPSPanel()
{
	buildSStrReadFileFilter = new GenFileFilter();
	buildSStrFileChooser = new JFileChooser(".");

	buildSStrReadFileFilter = new GenFileFilter();
	buildSStrReadFileFilter.addExtension("txt");
	buildSStrReadFileFilter.setDescription("Primary Structure input files");

	editPSPanel = new JPanel(new BorderLayout(), true);
	editPSPanel.setFont(new Font("Helvetica", Font.PLAIN, 12));
	editPSPanel.setForeground(Color.black);
	editPSPanel.setBackground(guiBGColor);
	editPSPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	editPSPanel.setAlignmentX(LEFT_ALIGNMENT);
	editPS_TA = new JTextArea(10, 20);
	editPS_TA.setForeground(Color.black);
	editPS_TA.setBackground(Color.white);
	/*
	editPS_TA.append("AUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGCAUGC");
	*/
	/*
	editPS_TA.append("GCGGAUUUAGCUCAGDDGGGAGAGCGCCAGACUGAAYAUCUGGAGGUCCUGUGTYCGAUCCACAGAAUUCGCACCA");
	*/


	JScrollPane editPSScrollPane = new JScrollPane(editPS_TA,
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	editPSScrollPane.setBorder(new TitledBorder(new BevelBorder(1),
		"Edit Primary Structure:"));
	editPSScrollPane.setBackground(guiBGColor);
	editPSPanel.add(editPSScrollPane, BorderLayout.CENTER);

	JPanel btPanel = this.getNewFlowLeftPanel();

	JButton clearBt = new JButton("Clear");
	clearBt.setFont(btFont);
	clearBt.setBackground(guiBGColor);
	clearBt.setForeground(Color.black);
	clearBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			editPS_TA.setText("");
		}
	});
	btPanel.add(clearBt);

	JButton readBt = new JButton("Read");
	readBt.setFont(btFont);
	readBt.setBackground(guiBGColor);
	readBt.setForeground(Color.black);
	readBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			buildSStrReadFileFilter.setDescription("Primary Structure input files");
			buildSStrFileChooser.setFileFilter(buildSStrReadFileFilter);
			// buildSStrFileChooser.setSelectedFile(null);
			try
			{
				int returnVal =
					buildSStrFileChooser.showOpenDialog(ComplexSceneFormatSStrTab.this);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;
				
				File primaryStructureFile = buildSStrFileChooser.getSelectedFile();
				editPS_TA.setText(FileUtil.getFileAsString(primaryStructureFile));
				// alert("primary structure string size: " + editPS_TA.getText().length());
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in buildSStrFileChooser:", e, 98);
			}
		}
	});
	btPanel.add(readBt);

	editPSPanel.add(btPanel, BorderLayout.SOUTH);
}

/*************** FORMAT HELIX STUFF **************************/

private JPanel formatHelixPanel = null;
private JInternalFrame formatHelixFrame = null;
private JTextField helixNuc0_TF = null;
private JTextField helixNuc1_TF = null;
private JTextField helixNuc2_TF = null;
private JTextField helixNuc3_TF = null;
/*
private JRadioButton useDefaultHelixDimensions_RB = null;
*/
private JRadioButton formatHairPin_RB = null;
private JLabel formatHelixHandLabel = null;

Nuc2D formatHelixNuc0 = null;
Nuc2D formatHelixNuc1 = null;
Nuc2D formatHelixNuc2 = null;
Nuc2D formatHelixNuc3 = null;

private RNAHelix2D formatHelix = null;
private boolean previewAccepted = false;

private void
initFormatHelixFrame()
throws Exception
{
	if (formatHelixFrame == null)
	{
		formatHelixFrame = complexSceneView.getBasicInternalFrame(110, 60, 240, 400);
		formatHelixFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		formatHelixFrame.addInternalFrameListener(new InternalFrameListener()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				if (!previewAccepted && (formatHelix != null))
				{
					try
					{
						int result = 0;
						Object[] message = new Object[3];
						message[1] = new JLabel("Preview wasn't accepted. Quitting will restore to original");
						message[2] = new JLabel("Quit Format Helix?");

						String[] options =
						{
							"Yes",
							"No",
							"Cancel"
						};
						result = JOptionPane.showOptionDialog(
							ComplexSceneFormatSStrTab.this,  // the parent that the dialog blocks
							message,  // the dialog message array
							"Check User Cancel", // the title of the dialog window
							JOptionPane.OK_CANCEL_OPTION, // option type
							JOptionPane.PLAIN_MESSAGE, // message type
							null, // optional icon, use null to use the default icon
							options, // options string array, will be made into buttons
							options[0] // option that should be made into a default button
							);

						switch (result)
						{
						  case 0: // yes
							formatHelix.restoreXY();
							/*
							runClearPreview();
							InternalFrameListener[] listenerList =
								formatHelixFrame.getInternalFrameListeners();
							if (listenerList.length > 1)
								debug("Problem 0 in ComplexSceneWorkTab.updateComplexPropertiesFrame().internalFrameClosed(): More than one listener");
							requestFocus();
							formatHelixFrame.restoreSubcomponentFocus();
							formatHelixFrame.setVisible(false); // hide the Frame
							formatHelixFrame.dispose();
							*/
							runStopHelixFrame();
							return;
						  case 1: // no
							break;
						  case 2: // cancel (same as no?)
							break;
						  default:
							break;
						}
					}
					catch (Exception e0)
					{
						complexSceneView.handleException("Exception 0 in initFormatHelixFrame:", e0, 98);
					}
				}
				else
				{
					try
					{
						/*
						runClearPreview();
						InternalFrameListener[] listenerList =
							formatHelixFrame.getInternalFrameListeners();
						if (listenerList.length > 1)
							debug("Problem 1 in ComplexSceneWorkTab.updateComplexPropertiesFrame().internalFrameClosed(): More than one listener: " + listenerList.length);
						requestFocus();
						formatHelixFrame.restoreSubcomponentFocus();
						formatHelixFrame.setVisible(false); // hide the Frame
						formatHelixFrame.dispose();
						*/
						runStopHelixFrame();
					}
					catch (Exception e1)
					{
						complexSceneView.handleException("Exception 1 in initFormatHelixFrame:", e1, 98);
					}
				}
			}

			public void internalFrameClosed(InternalFrameEvent e)
			{
				try
				{
					InternalFrameListener[] listenerList =
						formatHelixFrame.getInternalFrameListeners();
					if (listenerList.length > 1)
						debug("Problem 2 in ComplexSceneWorkTab.updateComplexPropertiesFrame().internalFrameClosed(): More than one listener");
					requestFocus();

					runClearPreview();
				}
				catch (Exception evt)
				{
					complexSceneView.handleException("Exception in CopmlexSceneFormatSStrTab.formatHelixFrame.internalFrameClosing:", evt, 101);
				}
			}

			public void internalFrameOpened(InternalFrameEvent e)
			{
			}

			public void internalFrameIconified(InternalFrameEvent e)
			{
			}

			public void internalFrameDeiconified(InternalFrameEvent e)
			{
			}

			public void internalFrameActivated(InternalFrameEvent e)
			{
			}

			public void internalFrameDeactivated(InternalFrameEvent e)
			{
				requestFocus();
			}
		});	
	}

	complexSceneView.updateBasicInternalFrame("Format Helix In Place",
		"Pick or enter corner nucs:",
		formatHelixPanel, formatHelixFrame);
	if (formatHelixFrame.isIcon())
		formatHelixFrame.setIcon(false);
}

private void
runStopHelixFrame()
throws Exception
{
	runClearPreview();
	InternalFrameListener[] listenerList =
		formatHelixFrame.getInternalFrameListeners();
	if (listenerList.length > 1)
	{
		/*
		debug("Problem 1 in ComplexSceneWorkTab.updateComplexPropertiesFrame().internalFrameClosed(): More than one listener: " + listenerList.length);
		*/
	}
	requestFocus();
	formatHelixFrame.restoreSubcomponentFocus();
	formatHelixFrame.setVisible(false); // hide the Frame
	formatHelixFrame.dispose();
}

private boolean
checkFormatHelix()
{
	try
	{
		Integer.parseInt(helixNuc0_TF.getText());
	}
	catch (NumberFormatException e)
	{
		alert("Error in first text field: " + helixNuc0_TF.getText() +
			": not a valid int");
		return (false);
	}	

	try
	{
		Integer.parseInt(helixNuc1_TF.getText());
	}
	catch (NumberFormatException e)
	{
		alert("Error in second text field: " + helixNuc1_TF.getText() +
			": not a valid int");
		return (false);
	}

	try
	{
		Integer.parseInt(helixNuc2_TF.getText());
	}
	catch (NumberFormatException e)
	{
		alert("Error in third text field: " + helixNuc2_TF.getText() +
			": not a valid int");
		return (false);
	}
	 
	try
	{
		Integer.parseInt(helixNuc3_TF.getText());
	}
	catch (NumberFormatException e)
	{
		alert("Error in fourth text field:" + helixNuc3_TF.getText() +
			": not a valid int");
		return (false);
	}

	NucNode[] nucA = new NucNode[4];
	nucA[0] = formatHelixNuc0;
	nucA[1] = formatHelixNuc1;
	nucA[2] = formatHelixNuc2;
	nucA[3] = formatHelixNuc3;

	try
	{
		NucCollection.sortHelixEndNucs(nucA);
	}
	catch (ComplexException ce)
	{
		runComplexExceptionAlert(ce, false);
		return (false);
	}
	formatHelixNuc0 = (Nuc2D)nucA[0];
	formatHelixNuc1 = (Nuc2D)nucA[1];
	formatHelixNuc2 = (Nuc2D)nucA[2];
	formatHelixNuc3 = (Nuc2D)nucA[3];

	return (true);
}

private boolean
setFormatHelix()
throws Exception
{
	SSData2D sstr = this.getCurrentWorkSStr();
	if (sstr == null)
	{
		alert("Error: rna strand is null");
		return (false);
	}
	try
	{
		NucCollection2D.setBasePairs(
			formatHelixNuc0, formatHelixNuc3,
			formatHelixNuc1.getID() - formatHelixNuc0.getID() + 1);
	}
	catch (ComplexException ce)
	{
		if (ce.getErrorCode() == (ComplexDefines.RNA_HELIX_ERROR +
			ComplexDefines.CREATE_HELIX_BASEPAIRS_ERROR))
		{
			// already previewed
			return (false);
		}
		runComplexExceptionAlert(ce, false);
		return (false);
	}

	if (formatHelix == null)
		formatHelix = new RNAHelix2D();
	formatHelix.set(formatHelixNuc0);
	formatHelixHandLabel.setText("clock-wise formatted: " + formatHelix.isClockWiseFormatted());

	return (true);
}

private void
resetFormatHelixPanel()
throws Exception
{
	helixNuc0_TF.setText("");
	helixNuc1_TF.setText("");
	helixNuc2_TF.setText("");
	helixNuc3_TF.setText("");
	formatHelixHandLabel.setText("clock-wise formatted:");
	previewAccepted = false;
}

private void
runAcceptPreview()
throws Exception
{
	if (formatHelix == null)
	{
		alert("Nothing to Accept");
		return;
	}
	previewAccepted = true;

	this.editErase(formatHelix, ComplexDefines.InRNAHelix);
	formatHelix.setEditColor(null);
	this.resetFormatHelixPanel();
	complexSceneView.renderDrawObjectView();
	formatHelix = null;
}

private void
runClearPreview()
throws Exception
{
	this.resetFormatHelixPanel();

	if (this.getCurrentWorkSStr() != null)
		this.getCurrentWorkSStr().setEditColor(null);

	if (formatHelix != null)
	{
		formatHelix.unsetBasePairs();
		formatHelix = null;
	}
	complexSceneView.renderDrawObjectView();
}

private void
runPreviewHelixFormatCmd()
throws Exception
{
	if (!checkFormatHelix())
	{
		this.runClearPreview();
		return;
	}

	if (!this.setFormatHelix())
		return;

	this.editErase(formatHelix, ComplexDefines.InRNAHelix);
	formatHelix.setFormatHairPin(formatHairPin_RB.isSelected());
	formatHelix.setEditColor(complexSceneView.moveableEditingColor);
	editRedraw(formatHelix, complexSceneView.getCurrentGraphics2D());
}

private void
runUndoReFormat()
throws Exception
{
	if (formatHelix == null)
	{
		alert("Nothing to undo");
		return;
	}
	if (!formatHelix.restoreXY())
	{
		alert("Nothing to undo");
		return;
	}
	complexSceneView.renderDrawObjectView();
}

private void
runReFormatHelix()
throws Exception
{
	if (formatHelix == null)
	{
		alert("Need to preview helix first");
		return;
	}
	if (!checkFormatHelix())
	{
		this.runClearPreview();
		return;
	}

	if (formatHelix != null)
		formatHelix.unsetBasePairs();

	if (!this.setFormatHelix())
		return;
	
	formatHelix.saveXY();

	// NEED to allow for single base paired helix

	/*
	if (!useDefaultHelixDimensions_RB.isSelected())
	{
		// try and figure out distances from existing graph

		// MAYBE should just leave stuff alone and not reformat at all.

		double nToN1Dist = 0.0;
		double helixBaseDist = 0.0;
		double helixBasePairDist = 0.0;
		double misMatchDist = 0.0;

		// resetNewHelixDistances(nToN1Dist, helixBaseDist, helixBasePairDist, misMatchDist);
	}
	*/
	/*
	else if (!resetDefaultHelixDistances(this.getCurrentWorkSStr()))
	{
		alert("Error: Couldn't reset formatting distances");
		return;
	}
	*/

	/*
	if (useDefaultHelixDimensions_RB.isSelected())
	{
	*/
		formatHelix.setFormatHairPin(formatHairPin_RB.isSelected());
		formatHelix.reformat();
	/*
	}
	*/
	complexSceneView.renderDrawObjectView();
}

// NEED a way to determine if trying to bp across different strands.
private void
setFormatHelixFrame()
throws Exception
{
	if (formatHelixFrame == null)
		return;
	if (!formatHelixFrame.isShowing())
		return;
	if (formatHelixPanel == null)
		return;
	
	Nuc2D refNuc = this.getCurrentWorkNuc();
	
	while (true)
	{
		try
		{
			Integer.parseInt(helixNuc0_TF.getText());
			// debug("formatHelixNuc0: " + formatHelixNuc0 + " " + formatHelixNuc0.getParentSSData().getName());
		}
		catch (NumberFormatException e)
		{
			helixNuc0_TF.setText(Integer.toString(refNuc.getID()));
			formatHelixNuc0 = refNuc;
			return;
		}
		try
		{
			Integer.parseInt(helixNuc1_TF.getText());
		}
		catch (NumberFormatException e)
		{
			helixNuc1_TF.setText(Integer.toString(refNuc.getID()));
			formatHelixNuc1 = refNuc;
			return;
		}
		try
		{
			Integer.parseInt(helixNuc2_TF.getText());
		}
		catch (NumberFormatException e)
		{
			helixNuc2_TF.setText(Integer.toString(refNuc.getID()));
			formatHelixNuc2 = refNuc;
			return;
		}
		try
		{
			Integer.parseInt(helixNuc3_TF.getText());
		}
		catch (NumberFormatException e)
		{
			helixNuc3_TF.setText(Integer.toString(refNuc.getID()));
			formatHelixNuc3 = refNuc;
			runPreviewHelixFormatCmd();
			return;
		}
		this.runClearPreview();
	}
}

private void
buildFormatHelixPanel()
{
	formatHelixPanel = new JPanel(new GridLayout(9, 1));
	formatHelixPanel.setFont(new Font("Helvetica", Font.PLAIN, 12));
	formatHelixPanel.setForeground(Color.black);
	formatHelixPanel.setBackground(guiBGColor);
	formatHelixPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	formatHelixPanel.setAlignmentX(CENTER_ALIGNMENT);

	JPanel tfPanel = this.getNewFlowLeftPanel();
	helixNuc0_TF = new JTextField(3);
	helixNuc0_TF.setText("");
	tfPanel.add(helixNuc0_TF);
	helixNuc1_TF = new JTextField(3);
	helixNuc1_TF.setText("");
	tfPanel.add(helixNuc1_TF);
	helixNuc2_TF = new JTextField(3);
	helixNuc2_TF.setText("");
	tfPanel.add(helixNuc2_TF);
	helixNuc3_TF = new JTextField(3);
	helixNuc3_TF.setText("");
	tfPanel.add(helixNuc3_TF);

	formatHelixPanel.add(tfPanel);

	JButton acceptPreviewBt = new JButton("Accept Preview");
	acceptPreviewBt.setFont(btFont);
	acceptPreviewBt.setBackground(guiBGColor);
	acceptPreviewBt.setForeground(Color.black);
	acceptPreviewBt.setAlignmentX(Component.LEFT_ALIGNMENT);
	acceptPreviewBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				runAcceptPreview();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneFormatSStrTab.acceptPreviewBt:", e, 98);
			}
		}
	});
	formatHelixPanel.add(acceptPreviewBt);	

	JButton clearPreviewBt = new JButton("Clear Preview");
	clearPreviewBt.setFont(btFont);
	clearPreviewBt.setBackground(guiBGColor);
	clearPreviewBt.setForeground(Color.black);
	clearPreviewBt.setAlignmentX(Component.LEFT_ALIGNMENT);
	clearPreviewBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				runClearPreview();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneFormatSStrTab.clearPreviewBt:", e, 98);
			}
		}
	});
	formatHelixPanel.add(clearPreviewBt);

	// need to interactively make things green, or type in nucs and click this preview bt.
	JButton previewBt = new JButton("Preview");
	previewBt.setFont(btFont);
	previewBt.setBackground(guiBGColor);
	previewBt.setForeground(Color.black);
	previewBt.setAlignmentX(Component.LEFT_ALIGNMENT);
	previewBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				runPreviewHelixFormatCmd();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneFormatSStrTab.previewBt:", e, 98);
			}
		}
	});
	formatHelixPanel.add(previewBt);

	/*
	useDefaultHelixDimensions_RB = new JRadioButton();
	useDefaultHelixDimensions_RB.setSelected(true);
	formatHelixPanel.add(getNewViewButtonLeftPanel(
		useDefaultHelixDimensions_RB, "Use Default Dimensions"));
	*/

	formatHairPin_RB = new JRadioButton();
	formatHairPin_RB.setSelected(true);
	formatHelixPanel.add(getNewViewButtonLeftPanel(
		formatHairPin_RB, "Format Hairpin"));

	formatHelixHandLabel = this.getNewViewLabel("clock-wise formatted:");
	formatHelixPanel.add(formatHelixHandLabel);

	JButton reFormatHelixBt = new JButton("Re-Format");
	reFormatHelixBt.setFont(btFont);
	reFormatHelixBt.setBackground(guiBGColor);
	reFormatHelixBt.setForeground(Color.black);
	reFormatHelixBt.setAlignmentX(Component.LEFT_ALIGNMENT);
	reFormatHelixBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				runReFormatHelix();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneFormatSStrTab.reFormatHelixBt:", e, 98);
			}
		}
	});
	formatHelixPanel.add(reFormatHelixBt);

	JButton undoReFormatBt = new JButton("Undo last re-format");
	undoReFormatBt.setFont(btFont);
	undoReFormatBt.setBackground(guiBGColor);
	undoReFormatBt.setForeground(Color.black);
	undoReFormatBt.setAlignmentX(Component.LEFT_ALIGNMENT);
	undoReFormatBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				runUndoReFormat();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneFormatSStrTab.undoReFormatBt:", e, 10);
			}
		}
	});
	formatHelixPanel.add(undoReFormatBt);
}

/*************** END FORMAT HELIX STUFF **************************/

/*************** FORMAT NEW STRUCTURE STUFF **********************/

private void
initFormatNewStructureFrame()
throws Exception
{
	if (formatNewStructureFrame == null)
		formatNewStructureFrame = complexSceneView.getBasicInternalFrame(110, 60, 240, 500);
	complexSceneView.updateBasicInternalFrame("Format New Structure",
		"Set Format Parameters:",
		formatNewStructurePanel, formatNewStructureFrame);
	if (formatNewStructureFrame.isIcon())
		formatNewStructureFrame.setIcon(false);
}

private void
buildFormatNewStructurePanel()
{
	// formatNewStructurePanel = new Box(BoxLayout.Y_AXIS);
	formatNewStructurePanel = new JPanel(new GridLayout(11, 1));
	formatNewStructurePanel.setFont(new Font("Helvetica", Font.PLAIN, 12));
	formatNewStructurePanel.setForeground(Color.black);
	formatNewStructurePanel.setBackground(guiBGColor);
	formatNewStructurePanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	formatNewStructurePanel.setAlignmentX(LEFT_ALIGNMENT);

	JButton runFormatNewSStrBt = this.getNewViewButton("Run Format");
	formatNewStructurePanel.add(getNewViewButtonLeftPanel(runFormatNewSStrBt));
	runFormatNewSStrBt.addActionListener(new FormatSStrActionListener());

	ButtonGroup buildSStrGroupRBs = new ButtonGroup();

	JButton newPrimaryStructureBt = this.getNewViewButton("New Primary Structure");
	formatNewStructurePanel.add(getNewViewButtonLeftPanel(newPrimaryStructureBt));
	newPrimaryStructureBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (editPSFrame == null)
					editPSFrame = complexSceneView.getBasicInternalFrame(110, 60, 300, 300);
				complexSceneView.updateBasicInternalFrame("New Primary Structure",
					"Enter new sequence",
					editPSPanel, editPSFrame);
				if (editPSFrame.isIcon())
					editPSFrame.setIcon(false);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneFormatSStrTab.newPrimaryStructureBt: ", e, 101);
			}
		}
	});

	JButton addHelixBt = this.getNewViewButton("Set New Helices");
	formatNewStructurePanel.add(getNewViewButtonLeftPanel(addHelixBt));
	addHelixBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				popHelixFrame();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneFormatSStrTab.addHelixBt: ", e, 101);
			}
		}
	});

ActionListener changeSStrNameActionListener = new ActionListener()
{
	public void
	actionPerformed(ActionEvent evt)
	{
		try
		{
			if ((newStr_Name_TF.getText() == null) || (newStr_Name_TF.getText().length() < 1))
			{
				alert("Need to type in a name for structure");
				return;
			}
			if (complexSceneView.getComplexScene() == null)
			{
				return;
			}
			complexSceneView.setCurrentInputFile(new File(newStr_Name_TF.getText() + ".xrna"));
			complexSceneView.getComplexScene().setName(newStr_Name_TF.getText());

			((ComplexScene)((ComplexSSDataCollection)getCurrentWorkSStr().getParentCollection())).setName(newStr_Name_TF.getText());

			getCurrentWorkSStr().setName(newStr_Name_TF.getText());
		}
		catch (Exception e)
		{
			complexSceneView.handleException(
				"Exception in ComplexSceneFormatSStrTab.changeSStrNameActionListener:", e, 101);
		}
	}
};

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("prefix name:"));
	newStr_Name_TF = new JTextField("new_structure", 6);
	newStr_Name_TF.addActionListener(changeSStrNameActionListener);
	tmpPanel.add(newStr_Name_TF);

	newStrNameBt = this.getNewViewImgPlainButton();
	newStrNameBt.addActionListener(changeSStrNameActionListener);
	tmpPanel.add(newStrNameBt);
	formatNewStructurePanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("start angle:"));
	newStr_StartAngle_TF = new JTextField("90.0", 4);
	tmpPanel.add(newStr_StartAngle_TF);
	formatNewStructurePanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("arc radius:"));
	newStr_ArcRadius_TF = new JTextField("1000.0", 4);
	tmpPanel.add(newStr_ArcRadius_TF);
	formatNewStructurePanel.add(tmpPanel);
}

/*************** END FORMAT NEW STRUCTURE STUFF **********************/

/*************** DERIVE STRUCTURE STUFF **********************/

private JPanel deriveStructurePanel = null;
private JInternalFrame deriveStructureFrame = null;

private Hashtable newDerivedStructureFrames_HT = null;

public JFileChooser	alignmentFileChooser = null;
private GenFileFilter alignmentFileFilter = null;
private JComboBox alignmentTemplateNames_CB = null;
private JComboBox alignmentNewNames_CB = null;
private JRadioButton includeExtraneousLabels_RB = null;
private JRadioButton includeNucLabels_RB = null;
private ComplexAlignment alignment = null;
private File alignmentFile = null;
private String templateName = null;

private void
initDeriveStructureFrame()
throws Exception
{
	if (deriveStructureFrame == null)
		deriveStructureFrame = complexSceneView.getBasicInternalFrame(110, 60, 280, 340);
	complexSceneView.updateBasicInternalFrame("Derive structure from template",
		"Set derive parameters:",
		deriveStructurePanel, deriveStructureFrame);
	if (deriveStructureFrame.isIcon())
		deriveStructureFrame.setIcon(false);
}

private XRNABean xrnaBean = null;

private JPanel
initNewDerivedStructurePanel(String newName)
throws Exception
{
	if (this.xrnaBean == null)
		xrnaBean = new XRNABean();

	xrnaBean.setCurrentInputFile(new File(newName + ".xrna"));
	xrnaBean.runSetFromInputFile();

	xrnaBean.getViewImgCanvas().setPreferredSize(
		new Dimension(xrnaBean.drawObjectImgW, xrnaBean.drawObjectImgH));

	// NEED to provide mouse listeners to detect picked area and use the following
	 // to be able to pick a nuc:

	// this.setCurrentDrawObject(
		// (DrawObject)complexSceneView.getComplexScene().findLeafNode(
			// complexSceneView.getCurrentViewX(), complexSceneView.getCurrentViewY(), null, excludeTypes));
	//
	//
	
	return (xrnaBean.getViewImgCanvas());
}

private void
initNewDerivedStructureFrame(String newName)
throws Exception
{
	if (newDerivedStructureFrames_HT == null)
		newDerivedStructureFrames_HT = new Hashtable();

	JInternalFrame tmpFrame = null;
	JPanel tmpPanel = null;

	tmpPanel = initNewDerivedStructurePanel(newName);

	if (newDerivedStructureFrames_HT.containsKey(newName))
	{
		tmpFrame = (JInternalFrame)newDerivedStructureFrames_HT.get(newName);
		// debug("FOUND tmpFrame at " + newName);
	}
	else
	{
		tmpFrame = complexSceneView.getBasicInternalFrame(200, 100, 240, 500);
		newDerivedStructureFrames_HT.put(newName, tmpFrame);
	}
	tmpPanel.setVisible(true);
	JScrollPane tmpScrollPane = new JScrollPane(tmpPanel,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	tmpScrollPane.setBackground(Color.white);
	tmpScrollPane.setVisible(true);
	tmpScrollPane.setViewportView(tmpPanel);
	tmpScrollPane.setViewport(tmpScrollPane.getViewport());

	complexSceneView.updateBasicInternalFrame(
		newName + " derived structure",
		"pickable image of XRNA I/O file" + newName + ".xrna :",
		tmpScrollPane, tmpFrame);

	if (tmpFrame.isIcon())
		tmpFrame.setIcon(false);
}

private void
buildDeriveStructurePanel()
{
	alignmentFileFilter = new GenFileFilter();
	alignmentFileChooser = new JFileChooser(".");

	alignmentFileFilter = new GenFileFilter();
	alignmentFileFilter.addExtension("mase");
	alignmentFileFilter.addExtension("GB");
	alignmentFileFilter.addExtension("gb");
	alignmentFileFilter.setDescription("alignment files");
	alignmentFileChooser.setFileFilter(alignmentFileFilter);

	deriveStructurePanel = new JPanel(new GridLayout(12, 1));
	deriveStructurePanel.setFont(new Font("Helvetica", Font.PLAIN, 12));
	deriveStructurePanel.setForeground(Color.black);
	deriveStructurePanel.setBackground(guiBGColor);
	deriveStructurePanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	deriveStructurePanel.setAlignmentX(LEFT_ALIGNMENT);

	JButton readAlignmentFile_Bt = this.getNewViewButton("Read alignment file");
	deriveStructurePanel.add(getNewViewButtonLeftPanel(readAlignmentFile_Bt));

	readAlignmentFile_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				// SAVE FOR file dialog
				int returnVal =
					alignmentFileChooser.showOpenDialog(ComplexSceneFormatSStrTab.this);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;
				
				alignmentFile = alignmentFileChooser.getSelectedFile();

				setCursor(complexSceneView.complexWaitCursor);
				if (alignmentFile.getName().toLowerCase().endsWith(".gb"))
					alignment = new ComplexAlignment(alignmentFile.getName(),
						ComplexAlignment.GENBANK_INPUT);
				else
					alignment = new ComplexAlignment(alignmentFile.getName());
				setCursor(complexSceneView.complexDefaultCursor);

				if (!alignment.getAlignmentSet())
				{
					alert("Error: in alignment file parsing of " + alignmentFile.getName());
					return;
				}

				alignmentTemplateNames_CB.removeAllItems();
				alignmentNewNames_CB.removeAllItems();
				Vector nameList = alignment.getNameList();
				for (int i = 0;i < nameList.size();i++)
				{
					String seqName = (String)nameList.elementAt(i);
					alignmentTemplateNames_CB.addItem(seqName);
					alignmentNewNames_CB.addItem(seqName);
				}
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in alignmentFileChooser:", e, 98);
			}
		}
	});

	alignmentTemplateNames_CB = new JComboBox();
	alignmentTemplateNames_CB.addItem("                    ");
	deriveStructurePanel.add(getNewViewButtonLeftPanel(alignmentTemplateNames_CB, "Choose template name"));
	alignmentNewNames_CB = new JComboBox();
	alignmentNewNames_CB.addItem("                    ");
	deriveStructurePanel.add(getNewViewButtonLeftPanel(alignmentNewNames_CB, "Choose derived name"));

	JButton previewTemplate_Bt = this.getNewViewButton("Preview Template");
	deriveStructurePanel.add(getNewViewButtonLeftPanel(previewTemplate_Bt));
	previewTemplate_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				templateName = ((String)alignmentTemplateNames_CB.getSelectedItem()).trim();
				if ((templateName == null) || (templateName.length() <= 0))
				{
					alert("Need to select a template name");
					return;
				}
				complexSceneView.setCurrentInputFile(new File(templateName + ".xrna"));

				if (!complexSceneView.getCurrentInputFile().exists())
				{
					alert("Can't find " + templateName + ".xrna");
					return;
				}

				complexSceneView.setCursor(complexSceneView.complexWaitCursor);
				complexSceneView.runSetFromInputFile();
				complexSceneView.setCursor(complexSceneView.complexDefaultCursor);
			}
			catch (Exception e)
			{
				// complexSceneView.handleException("Exception in alignmentFileChooser:", e, 98);
				ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(
					new DataOutputStream(excptArray)));
				debug(toString() + (new String(excptArray.toByteArray())));
			}
		}
	});

	includeExtraneousLabels_RB = new JRadioButton();
	includeExtraneousLabels_RB.setSelected(true);
	deriveStructurePanel.add(getNewViewButtonLeftPanel(
		includeExtraneousLabels_RB, "Include Extraneous Labels"));

	includeNucLabels_RB = new JRadioButton();
	includeNucLabels_RB.setSelected(true);
	deriveStructurePanel.add(getNewViewButtonLeftPanel(
		includeNucLabels_RB, "Include Parent Nuc Labels"));

	JButton runDeriveNewSStr_Bt = this.getNewViewButton("Run Derive");
	deriveStructurePanel.add(getNewViewButtonLeftPanel(runDeriveNewSStr_Bt));
	runDeriveNewSStr_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if ((templateName == null) || (templateName.length() <= 0))
				{
					alert("Need to select a template name and preview template");
					return;
				}
				if (complexSceneView.getComplexScene() == null)
				{
					alert("Need to preview template XRNA I/O file first");
					return;
				}

				String newDerivedName = ((String)alignmentNewNames_CB.getSelectedItem()).trim();
				if ((newDerivedName == null) || (newDerivedName.length() <= 0))
				{
					alert("Need to select a new name to derive from template");
					return;
				}

				if (newDerivedName.equals(templateName))
				{
					alert("Can't map same sequence");
					return;
				}

				alignment.setIncludeExtraneousLabels(includeExtraneousLabels_RB.isSelected());
				alignment.setIncludeNucLabels(includeNucLabels_RB.isSelected());

				if (!alignment.getAlignmentSet())
				{
					alert("Error: in alignment file parsing of " + alignmentFile.getName());
					return;
				}

				// seems like this could get ambiguous results if more than one ssdata collection
				// had the same names sstr:
				SSData2D templateSStr = complexSceneView.getComplexScene().getComplexSSData2D(templateName);

				if (templateSStr == null)
				{
					alert("Can't find rna strand named " + templateName + "\n" +
						"Make sure the rna strand is named correctly in the XRNA I/O file\n" +
						"and is the same as the name in the alignment file");
					return;
				}

				SSData2D newSStr = alignment.mapSequence(templateSStr, templateName, newDerivedName);
				if (newSStr == null)
				{
					alert("error, new structure could not be created");
					return;
				}

				String newFileName = newDerivedName + ".xrna";
				File newFile = new File(newFileName);
				if (newFile.exists() && newFile.canWrite())
				{
					Object[] message = new Object[3];
					message[0] = new JLabel("File Exists");
					message[1] = new JLabel("Overwrite: " + newFile.getName() + " ?");
					
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
						break;
					  case 1: // no
						// JOptionPane.showMessageDialog(parent, "Made it 1");
						return;
					  default:
						break;
					}
				}
				else if (newFile.exists())
				{
					alert("Error: " + newFile.getName() + " is not writeable; need to make writeable");
					return;
				}

				newSStr.wrapInComplexScene2D(newDerivedName).printComplexXML(newFile);

				initNewDerivedStructureFrame(newDerivedName);

				/*
				(new ComplexParentFrame(newFileName)).setVisible(true);
				*/
			}
			catch (Exception e)
			{
				// complexSceneView.handleException("Exception in alignmentFileChooser:", e, 98);
				ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(
					new DataOutputStream(excptArray)));
				debug(toString() + (new String(excptArray.toByteArray())));
			}
		}
	});
}

/*************** END DERIVE STRUCTURE STUFF **********************/

/******************** HELIX LIST STUFF *************************************/

private void
buildAddHelixPanel()
{
	addEditHelixPanel = new JPanel(new BorderLayout(), true);
	addEditHelixPanel.setFont(new Font("Helvetica", Font.PLAIN, 12));
	addEditHelixPanel.setForeground(Color.black);
	addEditHelixPanel.setBackground(guiBGColor);
	addEditHelixPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	addEditHelixPanel.setAlignmentX(LEFT_ALIGNMENT);

	helixList_TA = new JTextArea(10, 20);
	helixList_TA.setText("");
	helixList_TA.setForeground(Color.black);
	helixList_TA.setBackground(Color.white);

	JScrollPane addEditHelixScrollPane = new JScrollPane(helixList_TA,
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	addEditHelixScrollPane.setBorder(new TitledBorder(new BevelBorder(1),
		"Helix List:"));
	addEditHelixScrollPane.setPreferredSize(new Dimension(160, 160));
	addEditHelixScrollPane.setBackground(guiBGColor);
	addEditHelixScrollPane.setForeground(Color.black);
	addEditHelixPanel.add(addEditHelixScrollPane, BorderLayout.CENTER);

	JPanel southPanel = new JPanel(new GridLayout(2, 1));

	JButton readBt = new JButton("Read");
	readBt.setFont(btFont);
	readBt.setBackground(guiBGColor);
	readBt.setForeground(Color.black);
	readBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			buildSStrReadFileFilter.setDescription("Helix input files");
			buildSStrFileChooser.setFileFilter(buildSStrReadFileFilter);
			// buildSStrFileChooser.setSelectedFile(null);
			File buildSStrFile = null;
			try
			{
				// SAVE FOR file dialog
				int returnVal =
					buildSStrFileChooser.showOpenDialog(ComplexSceneFormatSStrTab.this);
				if (returnVal != JFileChooser.APPROVE_OPTION)
					return;
				
				buildSStrFile = buildSStrFileChooser.getSelectedFile();
				Vector helixList = HelixInfo.getHelixInfoList(buildSStrFile);
				for (int i = 0;i < helixList.size();i++)
					addNewHelixInfo((HelixInfo)helixList.elementAt(i));
			}
			catch (java.io.FileNotFoundException e)
			{
				alert("Can't find file: " + buildSStrFile.getName());
				return;
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in buildSStrFileChooser:", e, 101);
			}
		}
	});
	southPanel.add(readBt);

	JButton clearBt = new JButton("Clear");
	clearBt.setFont(btFont);
	clearBt.setBackground(guiBGColor);
	clearBt.setForeground(Color.black);
	clearBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			helixList_TA.setText("");
		}
	});
	southPanel.add(clearBt);

	addEditHelixPanel.add(southPanel, BorderLayout.SOUTH);
}

private void
addNewHelixInfo(String str0, String str1, String str2)
{
	if ((helixList_TA.getText() == null) || (helixList_TA.getText().length() <= 1))
		helixList_TA.setText(str0 + " " + str1 + " " + str2);
	else
		helixList_TA.setText(helixList_TA.getText() + "\n" + str0 + " " + str1 + " " + str2);
}

private void
addNewHelixInfo(String str0, String str1, String str2, String str3)
{
	if ((helixList_TA.getText() == null) || (helixList_TA.getText().length() <= 1))
		helixList_TA.setText(str0 + " " + str1 + " " + str2 + " " + str3);
	else
		helixList_TA.setText(helixList_TA.getText() + "\n" + str0 + " " + str1 + " " + str2 + " " + str3);
}

private void
addNewHelixInfo(HelixInfo helixInfo)
{
	if (helixInfo.getBPSStrName() == null)
		addNewHelixInfo(
			String.valueOf(helixInfo.getRefNucID()),
			String.valueOf(helixInfo.getBpNucID()),
			String.valueOf(helixInfo.getLength()));
	else
		addNewHelixInfo(
			String.valueOf(helixInfo.getRefNucID()),
			String.valueOf(helixInfo.getBpNucID()),
			String.valueOf(helixInfo.getLength()),
			String.valueOf(helixInfo.getBPSStrName()));
}

private void
popHelixFrame()
throws Exception
{
	if (addEditHelixFrame == null)
		addEditHelixFrame = complexSceneView.getBasicInternalFrame(110, 60, 300, 300);
	complexSceneView.updateBasicInternalFrame("Add/Edit Helix",
		addEditHelixPanel, addEditHelixFrame);
	if (addEditHelixFrame.isIcon())
		addEditHelixFrame.setIcon(false);
}

private boolean
setHelixListToCurrentPickMode()
throws Exception
{
	SSData2D sstr = getCurrentWorkSStr();
	Vector helixList = null;

	helixList_TA.setText("");

	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
	  	helixList = null;
		break;
	  case ComplexDefines.InRNASingleStrand :
		if (redirectHelixList != null)
			helixList = new Vector(redirectHelixList);
		break;
	  case ComplexDefines.InRNABasePair :
	  	helixList = getCurrentWorkBasePair().getHelixInfoList();
		break;
	  case ComplexDefines.InRNAHelix :
	  	helixList = getCurrentWorkHelix().getHelixInfoList();
		break;
	  case ComplexDefines.InRNAHelicalRun :
	  	helixList = getCurrentWorkStackedHelix().getHelixInfoList();
		break;
	  case ComplexDefines.InRNASubDomain :
	  	helixList = getCurrentWorkSubDomain().getHelixInfoList();
		break;
	  case ComplexDefines.InRNANamedGroup :
	  	helixList = getCurrentWorkNamedGroup().getHelixInfoList();
		break;
	  case ComplexDefines.InRNAColorUnit :
	  	helixList = getCurrentWorkColorUnit().getHelixInfoList();
		break;
	  case ComplexDefines.InRNACycle :
	  	helixList = getCurrentWorkCycle().getHelixInfoList();
		break;
	  case ComplexDefines.InRNAListNucs :
	  	helixList = getCurrentWorkListNucs().getHelixInfoList();
		break;
	  case ComplexDefines.InRNASSData :
	  	helixList = getCurrentWorkSStr().getHelixInfoList();
		break;
	  case ComplexDefines.InComplex :
	  	alert("Not available for rna complex constraint mode.\nUse a tighter constraints");
		return (false);
	  case ComplexDefines.InLabelsOnly :
		return (false);
	  case ComplexDefines.InComplexScene :
	  	alert("Not available for entire scene constraint mode.\nUse a tighter constraints");
		return (false);
	  default :
		break;
	}
	redirectHelixList = null;
	
	if (helixList != null)
	{
		for (int i = 0;i < helixList.size();i++)
			addNewHelixInfo((HelixInfo)helixList.elementAt(i));
	}

	helixList = null;

	return (true);
}

/******************** END HELIX LIST STUFF *************************************/

public void
buildGui(Color guiBGColor, int panelW, int panelH)
{

// NEED a textfield for a new name of a new structure. init with
// "new_structure.xrna" link in IOTab. this is for case when
// open up an xrna I/O file and then go to format tab and create a new
// structure. if try and write out then gets old name.

	super.buildGui(guiBGColor, panelW, panelH);

	controlBtPanel = new JPanel(new GridLayout(10, 1), true);
	controlBtPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	controlBtPanel.setForeground(Color.black);
	controlBtPanel.setBackground(guiBGColor);

	leftHandedRB = new JRadioButton();
	leftHandedRB.setSelected(true);
	leftHandedRB.setText("clockwise format");
	leftHandedRB.setActionCommand("clockwise_format");
	leftHandedRB.setFont(btFont);
	leftHandedRB.setForeground(Color.black);
	leftHandedRB.setBackground(guiBGColor);
	controlBtPanel.add(leftHandedRB);

	int btLengthAdjust = -15; // make longest string centered
	JButton setDistancesBt = new JButton();
	btInsets = setDistancesBt.getInsets();
	setDistancesBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	setDistancesBt.setText("Set Format Distances");
	setDistancesBt.setActionCommand("format_distances");
	setDistancesBt.setFont(btFont);
	setDistancesBt.setForeground(Color.black);
	setDistancesBt.setBackground(guiBGColor);
	controlBtPanel.add(setDistancesBt);
	setDistancesBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (setDistancesFrame == null)
					setDistancesFrame = complexSceneView.getBasicInternalFrame(110, 60, 300, 200);
				complexSceneView.updateBasicInternalFrame("Format Distances", "Type Distances: ",
					setDistancesPanel, setDistancesFrame);
				if (setDistancesFrame.isIcon())
					setDistancesFrame.setIcon(false);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneFormatSStrTab.newPrimaryStructureBt: ", e, 101);
			}
		}
	});

	JButton formatNewHelixBt = new JButton();
	btInsets = formatNewHelixBt.getInsets();
	formatNewHelixBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	formatNewHelixBt.setText("Format Helix");
	formatNewHelixBt.setActionCommand("format_helix");
	formatNewHelixBt.setFont(btFont);
	formatNewHelixBt.setForeground(Color.black);
	formatNewHelixBt.setBackground(guiBGColor);
	controlBtPanel.add(formatNewHelixBt);
	formatNewHelixBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (complexSceneView.getComplexScene() == null)
				{
					alert("Need to show a structure first");
					return;
				}
				if (getCurrentComplexPickMode() != ComplexDefines.InRNASingleNuc)
				{
					alert("Need to be in rna single nuc constraint mode\n" +
						" to use Format Helix");
					return;
				}
				initFormatHelixFrame();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneFormatSStrTab.formatNewHelixBt: ", e, 101);
			}
		}
	});

	JButton formatNewStructureBt = new JButton();
	btInsets = formatNewStructureBt.getInsets();
	formatNewStructureBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	formatNewStructureBt.setText("Format New Structure");
	formatNewStructureBt.setActionCommand("format_new_structure");
	formatNewStructureBt.setFont(btFont);
	formatNewStructureBt.setForeground(Color.black);
	formatNewStructureBt.setBackground(guiBGColor);
	controlBtPanel.add(formatNewStructureBt);
	formatNewStructureBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				initFormatNewStructureFrame();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneFormatSStrTab.formatNewStructureBt: ", e, 101);
			}
		}
	});

	JButton deriveStructureBt = new JButton();
	btInsets = deriveStructureBt.getInsets();
	deriveStructureBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	deriveStructureBt.setText("Structure from template");
	deriveStructureBt.setActionCommand("derive_structure_from_template");
	deriveStructureBt.setFont(btFont);
	deriveStructureBt.setForeground(Color.black);
	deriveStructureBt.setBackground(guiBGColor);
	controlBtPanel.add(deriveStructureBt);
	deriveStructureBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				/*
				if (complexSceneView.getComplexScene() == null)
				{
					alert("Template structure needs to be shown");
					return;
				}
				*/
				initDeriveStructureFrame();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneFormatSStrTab.deriveStructureBt: ", e, 101);
			}
		}
	});

	controlBtPanelScrollPane = new JScrollPane(controlBtPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	controlPanel.add(BorderLayout.CENTER, controlBtPanelScrollPane);
}

public void
buildNewStructure()
throws Exception
{
	String primaryStructure = editPS_TA.getText();
	if (primaryStructure == null)
	{
		alert("Primary Structure not entered");
		return;
	}
	primaryStructure = primaryStructure.trim().toUpperCase();
	if ((primaryStructure == null) || (primaryStructure.length() <= 0))
	{
		alert("Primary Structure not entered");
		return;
	}
	for (int i = 0;i < primaryStructure.length();i++)
	{
		char nucChar = primaryStructure.charAt(i);
		if (Character.isWhitespace(nucChar))
			continue;
		if (!NucNode.isValidNucChar(nucChar))
		{
			alert("Error in Primary Structure, Nuc at position: " + (i+1) +
				" must be (A|U|G|C|R|Y|N)");
			return;
		}
	}

	SSData2D sstr = new SSData2D("New SStr");
	sstr.addNucs(primaryStructure);
	this.setCurrentWorkSStr(sstr);

	boolean primaryStructureOnly = false;
	if ((helixList_TA == null) || (helixList_TA.getText() == null) || (helixList_TA.getText().length() <= 1))
	{
		RNASingleStrand2D singleStrand = new RNASingleStrand2D(
			this.getCurrentWorkSStr().getNuc2DAt(1), this.getCurrentWorkSStr().getNuc2DAt(this.getCurrentWorkSStr().getNucCount()));

		singleStrand.formatBlock();
		primaryStructureOnly = true;
	}
	else
	{
		/*
		if (!this.checkHelixInfo())
		{
			complexSceneView.setComplexScene(null);
			return;
		}
		*/
		try
		{
			this.getCurrentWorkSStr().unsetBasePairs();
			NucCollection2D.setBasePairs(this.getCurrentWorkSStr(),
				helixList_TA.getText());
		}
		catch (ComplexException ce)
		{
			runComplexExceptionAlert(ce, false);
			complexSceneView.setComplexScene(null);
			return;
		}
	}

	if (!this.resetDefaultHelixDistances(this.getCurrentWorkSStr()))
	{
		complexSceneView.setComplexScene(null);
		return;
	}

// private JTextField newStr_StartAngle_TF = null;
// private JTextField newStr_ArcRadius_TF = null;
	double startAngle = 0.0;
	double arcRadius = 0.0;
	try
	{
		startAngle = Double.valueOf(newStr_StartAngle_TF.getText()).doubleValue();
	}
	catch (NumberFormatException e)
	{
		alert("Something wrong with start angle: " + newStr_StartAngle_TF.getText());
		return;
	}
	try
	{
		arcRadius = Double.valueOf(newStr_ArcRadius_TF.getText()).doubleValue();
	}
	catch (NumberFormatException e)
	{
		alert("Something wrong with arc radius: " + newStr_ArcRadius_TF.getText());
		return;
	}

	if (!primaryStructureOnly)
	{
		try
		{
			/* this is now incorporated into formatAllStr:
			this.getCurrentWorkSStr().setLevel0EndPts(arcRadius, startAngle);
			*/
			// format entire structure
			this.getCurrentWorkSStr().formatAllStr(
				arcRadius,
				startAngle,
				leftHandedRB.isSelected(),
				true); // always center after format
		}
		catch (ComplexException ce)
		{
			runComplexExceptionAlert(ce, false);
			complexSceneView.setComplexScene(null);
			return;
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	// NEEDS to be a routine in ComplexSceneView
	getCurrentWorkSStr().setName(newStr_Name_TF.getText());
	complexSceneView.setCurrentInputFile(new File(this.newStr_Name_TF.getText() + ".xrna"));
	complexSceneView.setComplexScene(new ComplexScene2D(
		this.newStr_Name_TF.getText(),
		new ComplexSSDataCollection2D(this.newStr_Name_TF.getText(),
		this.getCurrentWorkSStr())));
	complexSceneView.resetFigureScale(1.0);
	complexSceneView.renderDrawObjectView();
	complexSceneView.centerScrollBars();
}

/*
public boolean
checkHelixInfo()
throws Exception
{
	String bpInfo = helixList_TA.getText();
	if ((bpInfo == null) || (bpInfo.length() <= 0))
	{
		alert("No helix information given in text area");
		return (false);
	}

	for (int i = 0;i < bpInfo.length();i++)
	{
		char c = bpInfo.charAt(i);
		if (Character.isWhitespace(c))
			continue;
		if (Character.isDigit(c))
			continue;
		alert("Non digit character in helix info: " + c);
		return (false);
	}

	return (true);
}
*/

private void
resetNewHelixDistances(double nToN1Dist, double helixBaseDist, double helixBasePairDist, double misMatchDist)
{
	this.getCurrentWorkSStr().setRNANucToNextNucDistance(nToN1Dist);
	this.getCurrentWorkSStr().setRNAHelixBaseDistance(helixBaseDist);
	this.getCurrentWorkSStr().setRNABasePairDistance(helixBasePairDist);
	this.getCurrentWorkSStr().setRNAMisMatchBasePairDistance(misMatchDist);
}

private boolean
resetDefaultHelixDistances(SSData2D sstr)
{
	double nucToNextNucDistance = 0.0;
	double helixBaseDistance = 0.0;
	double basePairDistance = 0.0;
	double mismatchDistance = 0.0;

	try
	{
		nucToNextNucDistance = Double.parseDouble(nucToNucDistanceTF.getText());
	}
	catch (NumberFormatException e)
	{
		alert("Something wrong with Nuc to next nuc distance: " + nucToNucDistanceTF.getText());
		return (false);
	}

	try
	{
		helixBaseDistance = Double.parseDouble(rnaHelixBaseDistanceTF.getText());
	}
	catch (NumberFormatException e)
	{
		alert("Something wrong with helix base distance: " + rnaHelixBaseDistanceTF.getText());
		return (false);
	}

	try
	{
		basePairDistance = Double.parseDouble(rnaBasePairDistanceTF.getText());
	}
	catch (NumberFormatException e)
	{
		alert("Something wrong with base pair distance: " + rnaBasePairDistanceTF.getText());
		return (false);
	}

	try
	{
		mismatchDistance = Double.parseDouble(rnaMisMatchBasePairDistanceTF.getText());
	}
	catch (NumberFormatException e)
	{
		alert("Something wrong with mismatch base pair distance: " + rnaMisMatchBasePairDistanceTF.getText());
		return (false);
	}

	sstr.setDistanceParameters(nucToNextNucDistance, helixBaseDistance,
		basePairDistance, mismatchDistance);

	return (true);
}

private class
FormatSStrActionListener
implements ActionListener
{
	private FormatSStrActionListener()
	{
		super();
	}

	public void
	actionPerformed(ActionEvent actionEvt)
	{
		try
		{
			setCursor(complexSceneView.complexWaitCursor);
			if ((complexSceneView.getComplexScene() == null) ||
				complexSceneView.getComplexScene().getName().equals(newStr_Name_TF.getText()))
			{
				buildNewStructure();
				return;
			}
			// alert("NEED to get rid of complex scene: " + complexSceneView.getComplexScene().getName());
			Object[] message = new Object[3];
			message[1] = new JLabel("Discard current scene: " +
				complexSceneView.getComplexScene().getName() + "?");
			message[2] = new JLabel(
				"Discarding will cause loss of any changes made to: " +
				complexSceneView.getComplexScene().getName() +
				"; write out first if desired");

			String[] options =
			{
				"Yes",
				"No",
				"Cancel"
			};
			int result = JOptionPane.showOptionDialog(
				complexSceneView.frameParent,  // the parent that the dialog blocks
				message,  // the dialog message array
				"Previous Structure Options", // the title of the dialog window
				JOptionPane.OK_CANCEL_OPTION, // option type
				JOptionPane.PLAIN_MESSAGE, // message type
				null, // optional icon, use null to use the default icon
				options, // options string array, will be made into buttons
				options[0] // option that should be made into a default button
				);

			switch (result)
			{
			  case 0: // yes
				// alert("Made it 0");
				buildNewStructure();
				break;
			  case 1: // no
				// alert("Made it 1");
				break;
			  case 2: // cancel (same as no?)
				break;
			  default:
				alert("Made it default: " + result);
				break;
			}
		}
		catch (ComplexException ce)
		{
			runComplexExceptionAlert(ce, false);
		}
		catch (Exception e)
		{
			complexSceneView.handleException("Exception in ComplexSceneFormatSStrTab.buildNewSStrBt: ", e, 101);
		}
		finally
		{
			setCursor(complexSceneView.complexDefaultCursor);
		}
	}
}

ReFormatBtListener reformatBt_AL = new ReFormatBtListener();

public Box
addFormatProperties(Box panel)
throws Exception
{
	int currentPickMode = this.getCurrentComplexPickMode();
	
	JButton reformat_Bt = this.getNewViewButton("reformat");
	reformat_Bt.setActionCommand(ComplexDefines.FORMAT_NUCS);
	reformat_Bt.addActionListener(reformatBt_AL);
	panel.add(this.getNewViewButtonLeftPanel(reformat_Bt));
	
	JButton reformatInPlace_Bt = this.getNewViewButton("set base pairs in place");
	reformatInPlace_Bt.setActionCommand(ComplexDefines.FORMAT_NUCS_IN_PLACE);
	reformatInPlace_Bt.addActionListener(reformatBt_AL);
	panel.add(this.getNewViewButtonLeftPanel(reformatInPlace_Bt));

	if (currentPickMode == ComplexDefines.InRNAHelicalRun)
	{
		JButton straightenFormatBt = this.getNewViewButton("re-format straight");
		straightenFormatBt.addActionListener(new ActionListener()
		{
			public void
			actionPerformed(ActionEvent evt)
			{
				try
				{
					getCurrentWorkStackedHelix().formatStraight();
					complexSceneView.renderDrawObjectView();
				}
				catch (Exception e)
				{
					complexSceneView.handleException("Exception in ComplexSceneEdit.straightenFormatBt:", e, 101);
				}
			}
		});
		panel.add(this.getNewViewButtonLeftPanel(straightenFormatBt));
	}
	
	if (
		(currentPickMode != ComplexDefines.InRNASingleStrand) &&
		(currentPickMode != ComplexDefines.InRNACycle) &&
		(!((currentPickMode == ComplexDefines.InRNASingleNuc) &&
		   getCurrentWorkNuc().isSingleStranded())))
	{
		JButton unsetBasePairs_Bt = null;
		if ((currentPickMode == ComplexDefines.InRNASingleNuc) ||
			(currentPickMode == ComplexDefines.InRNABasePair))
			unsetBasePairs_Bt = this.getNewViewButton("unset base pair");
		else
			unsetBasePairs_Bt = this.getNewViewButton("unset base pairs");
		unsetBasePairs_Bt.setActionCommand(ComplexDefines.FORMAT_UNSET_BASE_PAIRS);
		unsetBasePairs_Bt.addActionListener(reformatBt_AL);
		panel.add(this.getNewViewButtonLeftPanel(unsetBasePairs_Bt));
	}

	SSData2D sstr = getCurrentWorkSStr();
	if (sstr != null)
	{
		this.nucToNucDistanceTF.setText(String.valueOf(	
			sstr.getRNANucToNextNucDistance()));
		this.rnaHelixBaseDistanceTF.setText(String.valueOf(	
			sstr.getRNAHelixBaseDistance()));
		this.rnaBasePairDistanceTF.setText(String.valueOf(	
			sstr.getRNABasePairDistance()));
		this.rnaMisMatchBasePairDistanceTF.setText(String.valueOf(	
			sstr.getRNAMisMatchBasePairDistance()));
	}

	if (currentPickMode != ComplexDefines.InRNABasePair)
	{
		JPanel tmpPanel = this.getNewFlowLeftPanel();
		tmpPanel.add(setDistancesPanel);
		panel.add(tmpPanel);
		setDistancesPanel.setVisible(true);

		if (setHelixListToCurrentPickMode())
		{
			tmpPanel = this.getNewFlowLeftPanel();
			tmpPanel.add(addEditHelixPanel);
			panel.add(tmpPanel);
			addEditHelixPanel.setVisible(true);
		}
	}
	return (panel);
}

private class
ReFormatBtListener
implements ActionListener
{
	public void
	actionPerformed(ActionEvent evt)
	{
		String actionCmd = evt.getActionCommand();
		try
		{
		SSData2D sstr = getCurrentWorkSStr();
		Graphics2D g2 = complexSceneView.getCurrentGraphics2D();
		switch (getCurrentComplexPickMode())
		{
		  case ComplexDefines.InRNASingleNuc :
			Nuc2D nuc = getCurrentWorkNuc();
			if (nuc == null)
			{
				alert("Must pick RNA Nuc to edit");
				return;
			}

			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS) ||
				actionCmd.equals(ComplexDefines.FORMAT_NUCS_IN_PLACE))
			{
				if (nuc.isBasePair())
				{
					alert("Nuc is base paired. Use base pair formatting");
					return;
				}
				RNABasePair2D basePair = null;
				try
				{
					Vector helixList = HelixInfo.getHelixInfoList(
						helixList_TA.getText());
					if (helixList.size() < 1)
					{
						alert("Non valid helix info given in helix list text area:\n" + helixList_TA.getText());
						return;
					}
					if (helixList.size() > 1)
					{
						alert("Too much helix info given in: " + helixList_TA.getText() + "\n" +
							"Need to use one helix info entry only");
						return;
					}
					HelixInfo helixInfo = (HelixInfo)helixList.elementAt(0);
					if (helixInfo.getLength() != 1)
					{
						alert("Can only format a single base pair in single nuc mode"
								+ "\n" + "Use single strand mode or the 'Format Helix' menu");
						return;
					}
					if (helixInfo.getBpNucID() <= 0)
					{
						alert("Need to provide base pair id");
						return;
					}
					basePair = new RNABasePair2D(
						sstr.getNuc2DAt(helixInfo.getRefNucID()),
						sstr.getNuc2DAt(helixInfo.getBpNucID()));
				}
				catch (ComplexException ce)
				{
					runComplexExceptionAlert(ce, false);
					return;
				}

				/*
				sstr.saveDefaultDistances();
				if (!resetDefaultHelixDistances(sstr))
					return;
				basePair.setDistancesFromCollection((NucCollection)sstr);
				*/
				if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
					basePair.format();
				/*
				sstr.resetFromSaveDefaultDistances();
				*/

				// need to rerender anytime added structure to stacked helix.
				complexSceneView.renderDrawObjectView();

				alert("Properties menu for single rna nuc no longer valid");
				currentWorkDrawObject = null;
				setCurrentWorkNuc(null);
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
			}
			else if (actionCmd.equals(ComplexDefines.FORMAT_UNSET_BASE_PAIRS))
			{
				RNABasePair2D basePair = new RNABasePair2D(getCurrentWorkNuc());
				editErase(basePair, ComplexDefines.InRNABasePair);
				Nuc2D nuc5P = basePair.getFivePrimeNuc2D();
				Nuc2D nuc3P = basePair.getThreePrimeNuc2D();
				nuc5P.unsetBasePair();
				nuc3P.unsetBasePair();
				editErase(nuc5P, ComplexDefines.InRNASingleNuc);
				editRedraw(nuc5P, sceneImgG2);
				editErase(nuc3P, ComplexDefines.InRNASingleNuc);
				editRedraw(nuc3P, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNASingleStrand :
			RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();
			if (singleStrand == null)
			{
				alert("Must pick RNA Single Strand to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS) ||
				actionCmd.equals(ComplexDefines.FORMAT_NUCS_IN_PLACE))
			{
				try
				{
					Vector helixList = HelixInfo.getHelixInfoList(
						getCurrentWorkSStr(), helixList_TA.getText());
					if (helixList.size() < 1)
					{
						alert("Non valid helix info given in helix list text area:\n" + helixList_TA.getText());
						return;
					}
					singleStrand.unsetBasePairs();
					singleStrand.setBasePairs(helixList);
				}
				catch (ComplexException ce)
				{
					runComplexExceptionAlert(ce, false);
					return;
				}

				if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
				{
					sstr.saveDefaultDistances();
					if (!resetDefaultHelixDistances(sstr))
						return;
					singleStrand.setDistancesFromCollection((NucCollection)sstr);
					try
					{
						singleStrand.reformat(leftHandedRB.isSelected());
					}
					catch (ComplexException ce)
					{
						runComplexExceptionAlert(ce, false);
						return;
					}
					sstr.resetFromSaveDefaultDistances();
				}

				// need to rerender anytime added structure to stacked helix.
				complexSceneView.renderDrawObjectView();

				alert("Properties menu for rna single strand no longer valid");
				currentWorkDrawObject = null;
				setCurrentWorkSingleStrand(null);
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
			}
			break;
		  case ComplexDefines.InRNABasePair :
			RNABasePair2D basePair = getCurrentWorkBasePair();
			if (basePair == null)
			{
				alert("Must pick RNA Basepair to edit");
				return;
			}
			editErase(basePair, ComplexDefines.InRNABasePair);
			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
			{
				basePair.reformat();
				editRedraw(basePair, sceneImgG2);
			}
			else if (actionCmd.equals(ComplexDefines.FORMAT_UNSET_BASE_PAIRS))
			{
				Nuc2D nuc5P = getCurrentWorkBasePair().getFivePrimeNuc2D();
				Nuc2D nuc3P = getCurrentWorkBasePair().getThreePrimeNuc2D();
				nuc5P.unsetBasePair();
				nuc3P.unsetBasePair();
				editErase(nuc5P, ComplexDefines.InRNASingleNuc);
				nuc5P.setEditColor(null);
				editRedraw(nuc5P, sceneImgG2);
				editErase(nuc3P, ComplexDefines.InRNASingleNuc);
				nuc3P.setEditColor(null);
				editRedraw(nuc3P, sceneImgG2);
				alert("Properties menu for base pair no longer valid");
				currentWorkDrawObject = null;
				setCurrentWorkBasePair(null);
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
			}
			break;
		  case ComplexDefines.InRNAHelix :
			RNAHelix2D helix = getCurrentWorkHelix();
			if (helix == null)
			{
				alert("Must pick RNA Helix to edit");
				return;
			}

			editErase(helix, ComplexDefines.InRNAHelix);

			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS) ||
				actionCmd.equals(ComplexDefines.FORMAT_NUCS_IN_PLACE))
			{
				/*
				if (!checkHelixInfo())
				{
					editRedraw(helix, sceneImgG2);
					return;
				}
				*/
				Vector helixList = HelixInfo.getHelixInfoList(getCurrentWorkSStr(),
					helixList_TA.getText());

				// NEED to try and make this work with non-contiguous basepairs.
				// this would create internal helices.
				if (helixList.size() > 1)
				{
					alert("Can't reformat helix with non-contiguous basepairs.\n" +
						"Use 'RNA Sub Domain' or 'RNA Stacked Helix'");
					editRedraw(helix, sceneImgG2);
					return;
				}

				try
				{
					helix.setEditColor(null);
					helix.unsetBasePairs();
					helix.setBasePairs(helixList);
					setCurrentWorkHelix(new RNAHelix2D(helix.getFivePrimeStartNuc2D()));
					helix = getCurrentWorkHelix();
				}
				catch (ComplexException ce)
				{
					runComplexExceptionAlert(ce, false);
					editRedraw(helix, sceneImgG2);
					return;
				}

				if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
				{
					sstr.saveDefaultDistances();
					if (!resetDefaultHelixDistances(sstr))
					{
						editRedraw(helix, sceneImgG2);
						return;
					}
					helix.setDistancesFromCollection((NucCollection)sstr);
					helix.reformat(leftHandedRB.isSelected());
					sstr.resetFromSaveDefaultDistances();
				}
				// editRedraw(helix, sceneImgG2);
				// NEED to rework nuc edit color scheme. messus up on redraw
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.FORMAT_UNSET_BASE_PAIRS))
			{
				helix.setEditColor(null);
				redirectHelixList = new Vector(helix.getHelixInfoList());
				helix.unsetBasePairs();
				complexSceneView.renderDrawObjectView();
				alert("Properties menu for helix no longer valid;\nSwitching to RNA Single Strand properties menu");
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
				redirectPropertiesMenu(helix.getFivePrimeStartNuc2D(),
					ComplexDefines.InRNASingleStrand);
			}
			break;
		  case ComplexDefines.InRNAHelicalRun :
		  	RNAStackedHelix2D helicalRun = getCurrentWorkStackedHelix();
			if (helicalRun == null)
			{
				alert("Must pick RNA Helical Run to edit");
				return;
			}

			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS) ||
				actionCmd.equals(ComplexDefines.FORMAT_NUCS_IN_PLACE))
			{
				Vector helixList = HelixInfo.getHelixInfoList(getCurrentWorkSStr(),
					helixList_TA.getText());
				try
				{
					helicalRun.unsetBasePairs();
					helicalRun.setBasePairs(helixList);
				}
				catch (ComplexException ce)
				{
					runComplexExceptionAlert(ce, false);
					return;
				}

				if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
				{
					sstr.saveDefaultDistances();
					if (!resetDefaultHelixDistances(sstr))
						return;
					helicalRun.setDistancesFromCollection((NucCollection)sstr);
					helicalRun.reformat(leftHandedRB.isSelected());
					sstr.resetFromSaveDefaultDistances();
				}

				// need to rerender anytime added structure to stacked helix.
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.FORMAT_UNSET_BASE_PAIRS))
			{
				helicalRun.setEditColor(null);
				redirectHelixList = new Vector(helicalRun.getHelixInfoList());
				helicalRun.unsetBasePairs();
				complexSceneView.renderDrawObjectView();
				alert("Properties menu for stacked helix no longer valid;\nSwitching to RNA Single Strand properties menu");
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
				redirectPropertiesMenu(helicalRun.getFivePrimeStartNuc2D(),
					ComplexDefines.InRNASingleStrand);
			}
			break;
		  case ComplexDefines.InRNASubDomain :
			RNASubDomain2D subDomain = getCurrentWorkSubDomain();
			if (subDomain == null)
			{
				alert("Must pick RNA Sub Domain to edit");
				return;
			}
			
			editErase(subDomain, ComplexDefines.InRNASubDomain);

			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS) ||
				actionCmd.equals(ComplexDefines.FORMAT_NUCS_IN_PLACE))
			{
				Vector helixList = HelixInfo.getHelixInfoList(getCurrentWorkSStr(),
					helixList_TA.getText());
				try
				{
					subDomain.unsetBasePairs();
					subDomain.setBasePairs(helixList);
				}
				catch (ComplexException ce)
				{
					runComplexExceptionAlert(ce, false);
					return;
				}

				if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
				{
					sstr.saveDefaultDistances();
					if (!resetDefaultHelixDistances(sstr))
						return;
					subDomain.setDistancesFromCollection((NucCollection)sstr);
					/* MAYBE want to try this way instead of resetDefDist
					subDomain.setRNAHelixBaseDistance();
					subDomain.setRNABasePairDistance();
					subDomain.setRNAMisMatchBasePairDistance();
					subDomain.setRNANucToNextNucDistance();
					*/
					subDomain.reformat(leftHandedRB.isSelected());
					sstr.resetFromSaveDefaultDistances();
				}

				editRedraw(subDomain, sceneImgG2);
			}
			else if (actionCmd.equals(ComplexDefines.FORMAT_UNSET_BASE_PAIRS))
			{
				subDomain.setEditColor(null);
				redirectHelixList = new Vector(subDomain.getHelixInfoList());
				subDomain.unsetBasePairs();
				complexSceneView.renderDrawObjectView();
				alert("Properties menu for sub-domain no longer valid;\nSwitching to RNA Single Strand properties menu");
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
				redirectPropertiesMenu(subDomain.getFivePrimeNuc2D(),
					ComplexDefines.InRNASingleStrand);
			}
			break;
		  case ComplexDefines.InRNANamedGroup :
			RNANamedGroup2D namedGroup = getCurrentWorkNamedGroup();
			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
			{
			}
			else if (actionCmd.equals(ComplexDefines.FORMAT_UNSET_BASE_PAIRS))
			{
				// NOT sure about switching to single strand for this
				namedGroup.setEditColor(null);
				// redirectHelixList = new Vector(namedGroup.getHelixInfoList());
				namedGroup.unsetBasePairs();
				complexSceneView.renderDrawObjectView();
				// alert("Properties menu for named group no longer valid;\nSwitching to RNA Single Strand properties menu");
				alert("Properties menu for named group no longer valid");
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
				// redirectPropertiesMenu(namedGroup.getFivePrimeStartNuc2D(),
					// ComplexDefines.InRNASingleStrand);
			}
			break;
		  case ComplexDefines.InRNAColorUnit :
			RNAColorUnit2D colorUnit = getCurrentWorkColorUnit();
			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
			{
			}
			else if (actionCmd.equals(ComplexDefines.FORMAT_UNSET_BASE_PAIRS))
			{
				// NOT sure about switching to single strand for this
				colorUnit.setEditColor(null);
				// redirectHelixList = new Vector(colorUnit.getHelixInfoList());
				colorUnit.unsetBasePairs();
				complexSceneView.renderDrawObjectView();
				// alert("Properties menu for color unit no longer valid;\nSwitching to RNA Single Strand properties menu");
				alert("Properties menu for color unit no longer valid");
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
				// redirectPropertiesMenu(colorUnit.getFivePrimeStartNuc2D(),
					// ComplexDefines.InRNASingleStrand);
			}
			break;
		  case ComplexDefines.InRNACycle :
			RNACycle2D cycle = getCurrentWorkCycle();
			if (cycle == null)
			{
				alert("Must pick RNA Cycle to edit");
				return;
			}

			editErase(cycle, ComplexDefines.InRNACycle);

			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS) ||
				actionCmd.equals(ComplexDefines.FORMAT_NUCS_IN_PLACE))
			{
				Vector helixList = HelixInfo.getHelixInfoList(getCurrentWorkSStr(),
					helixList_TA.getText());
				try
				{
					cycle.unsetBasePairs();
					cycle.setBasePairs(helixList);
				}
				catch (ComplexException ce)
				{
					runComplexExceptionAlert(ce, false);
					return;
				}

				if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
				{
					sstr.saveDefaultDistances();
					if (!resetDefaultHelixDistances(sstr))
						return;
					cycle.setDistancesFromCollection((NucCollection)sstr);
					cycle.reformat();
					sstr.resetFromSaveDefaultDistances();
				}
			}
			editRedraw(cycle, sceneImgG2);
			break;
		  case ComplexDefines.InRNAListNucs :
			RNAListNucs2D listNucs = getCurrentWorkListNucs();
			if (listNucs == null)
			{
				alert("Must pick consecutive nucs to edit");
				return;
			}

			editErase(listNucs, ComplexDefines.InRNAListNucs);

			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS) ||
				actionCmd.equals(ComplexDefines.FORMAT_NUCS_IN_PLACE))
			{
				Vector helixList = HelixInfo.getHelixInfoList(getCurrentWorkSStr(),
					helixList_TA.getText());
				try
				{
					listNucs.unsetBasePairs();
					listNucs.setBasePairs(helixList);
				}
				catch (ComplexException ce)
				{
					runComplexExceptionAlert(ce, false);
					return;
				}

				if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
				{
					sstr.saveDefaultDistances();
					if (!resetDefaultHelixDistances(sstr))
						return;
					listNucs.setDistancesFromCollection((NucCollection)sstr);
					listNucs.reformat();
					sstr.resetFromSaveDefaultDistances();
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.FORMAT_UNSET_BASE_PAIRS))
			{
				// NOT sure about switching to single strand for this
				listNucs.setEditColor(null);
				// redirectHelixList = new Vector(listNucs.getHelixInfoList());
				listNucs.unsetBasePairs();
				complexSceneView.renderDrawObjectView();
				// alert("Properties menu for named group no longer valid;\nSwitching to RNA Single Strand properties menu");
				alert("Properties menu for named group no longer valid");
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
				// redirectPropertiesMenu(listNucs.getFivePrimeStartNuc2D(),
					// ComplexDefines.InRNASingleStrand);
			}
			break;
		  case ComplexDefines.InRNASSData :
			if (sstr == null)
			{
				alert("Must pick Secondary Structure to edit");
				return;
			}
			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS) ||
				actionCmd.equals(ComplexDefines.FORMAT_NUCS_IN_PLACE))
			{
				try
				{
					Vector helixList = HelixInfo.getHelixInfoList(getCurrentWorkSStr(),
						helixList_TA.getText());
					sstr.unsetBasePairs();
					sstr.setBasePairs(helixList);
				}
				catch (ComplexException ce)
				{
					runComplexExceptionAlert(ce, false);
					return;
				}

				if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
				{
					sstr.saveDefaultDistances();
					if (!resetDefaultHelixDistances(sstr))
						return;
					sstr.setDistancesFromCollection((NucCollection)sstr);
					try
					{
						sstr.reformat(leftHandedRB.isSelected());
					}
					catch (ComplexException ce)
					{
						String userTryCmd0 = "Try commenting out any basepairing that could cause a pseudo-knot,";
						String userTryCmd1 = "Or try commenting out any basepairing across different rna strands";
						if ((ce.getErrorMsg() != null) && (ce.getComment() != null))
							alert(ce.getErrorMsg() + "\n" + ce.getComment() +
								"\n" + userTryCmd0 + "\n" + userTryCmd1);
						else if (ce.getErrorMsg() != null)
							alert(ce.getErrorMsg() + "\n" + userTryCmd0 + "\n" +
								userTryCmd1);
						return;
					}
					sstr.resetFromSaveDefaultDistances();
				}
			}
			else if (actionCmd.equals(ComplexDefines.FORMAT_UNSET_BASE_PAIRS))
			{
				sstr.setEditColor(null);
				redirectHelixList = new Vector(sstr.getHelixInfoList());
				sstr.unsetBasePairs();
			}
			complexSceneView.renderDrawObjectView();
			break;
		  case ComplexDefines.InComplex :
		  	ComplexSSDataCollection2D complex = getCurrentWorkRNASSComplex();
			if (complex == null)
			{
				alert("Must pick Complex to edit");
				return;
			}

			editErase(complex, ComplexDefines.InComplex);
			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
				complex.reformat();
			sceneImgG2.setTransform(complex.getParentG2Transform());
			editRedraw(complex, sceneImgG2);
			break;
		  case ComplexDefines.InLabelsOnly :
			break;
		  case ComplexDefines.InComplexScene :
			if (actionCmd.equals(ComplexDefines.FORMAT_NUCS))
				getCurrentWorkComplexScene().reformat();
			complexSceneView.renderDrawObjectView();
			break;
		  default :
		  	break;
		}
		}
		catch (Exception e)
		{
			complexSceneView.handleException("Exception in ComplexSceneFormatSStr.ReFormatBtListener:", e, 101);
		}
	}
}

public Box
showRNASingleNucProperties()
throws Exception
{
	Box mainPanel = super.showRNASingleNucProperties();

	this.addFormatProperties(mainPanel);

	addNewHelixInfo(String.valueOf(this.getCurrentWorkNuc().getID()), "    ", "1");

	return (mainPanel);
}

public Box
showRNASingleStrandProperties()
throws Exception
{
	Box mainPanel = super.showRNASingleStrandProperties();

	this.addFormatProperties(mainPanel);

	return (mainPanel);
}

public Box
showRNABasePairProperties()
throws Exception
{
	Box mainPanel = super.showRNABasePairProperties();

	this.addFormatProperties(mainPanel);

	JButton setToCanonicalBt = this.getNewViewButton("Set To Canonical");
	setToCanonicalBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkBasePair().setNonDefaultBasePairType(ComplexDefines.BP_TYPE_CANONICAL);
				(new RNAHelix2D(getCurrentWorkBasePair().getFivePrimeNuc2D())).reformat();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(setToCanonicalBt));

	JButton setToWobbleBt = this.getNewViewButton("Set To Wobble");
	setToWobbleBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkBasePair().setNonDefaultBasePairType(ComplexDefines.BP_TYPE_WOBBLE);
				(new RNAHelix2D(getCurrentWorkBasePair().getFivePrimeNuc2D())).reformat();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(setToWobbleBt));

	JButton setToMisMatchBt = this.getNewViewButton("Set To MisMatch");
	setToMisMatchBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkBasePair().setNonDefaultBasePairType(ComplexDefines.BP_TYPE_MISMATCH);
				(new RNAHelix2D(getCurrentWorkBasePair().getFivePrimeNuc2D())).reformat();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(setToMisMatchBt));

	JButton setToWeakBt = this.getNewViewButton("Set To Weak");
	setToWeakBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkBasePair().setNonDefaultBasePairType(ComplexDefines.BP_TYPE_WEAK);
				(new RNAHelix2D(getCurrentWorkBasePair().getFivePrimeNuc2D())).reformat();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(setToWeakBt));

	JButton setToPhosphateBt = this.getNewViewButton("Set To Phosphate");
	setToPhosphateBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkBasePair().setNonDefaultBasePairType(ComplexDefines.BP_TYPE_PHOSPHATE);
				(new RNAHelix2D(getCurrentWorkBasePair().getFivePrimeNuc2D())).reformat();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(setToPhosphateBt));

	return (mainPanel);
}

public Box
showRNAHelixProperties()
throws Exception
{
	Box mainPanel = super.showRNAHelixProperties();

	this.addFormatProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNAStackedHelixProperties()
throws Exception
{
	Box mainPanel = super.showRNAStackedHelixProperties();

	this.addFormatProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNASubDomainProperties()
throws Exception
{
	Box mainPanel = super.showRNASubDomainProperties();

	this.addFormatProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNANamedGroupProperties()
throws Exception
{
	Box mainPanel = super.showRNANamedGroupProperties();

	// not sure about this yet; could be same as color unit
	this.addFormatProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNACycleProperties()
throws Exception
{
	Box mainPanel = super.showRNACycleProperties();

	/*
	this.addFormatProperties(mainPanel);
	*/
	
	return (mainPanel);
}

public Box
showRNAListNucsProperties()
throws Exception
{
	Box mainPanel = super.showRNAListNucsProperties();

	if (this.getCurrentWorkListNucs() == null)
		return (mainPanel);
	if (this.getCurrentWorkListNucs().getThreePrimeNuc() == null)
	{
		mainPanel.add(this.getNewLabelPanel("Right Click on next Nuc"));
		return (mainPanel);
	}
	this.addFormatProperties(mainPanel);

	addNewHelixInfo(
		String.valueOf(this.getCurrentWorkListNucs().getFivePrimeNuc().getID()),
		String.valueOf(this.getCurrentWorkListNucs().getThreePrimeNuc().getID()),
		null);
	
	return (mainPanel);
}

public Box
showRNASSDataProperties()
throws Exception
{
	Box mainPanel = super.showRNASSDataProperties();

	this.addFormatProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showComplexProperties()
throws Exception
{
	/*
	Box mainPanel = super.showComplexProperties();
	this.addFormatProperties(mainPanel);
	return (mainPanel);
	*/
	alert("Not available for rna group constraint mode.\nUse a tighter constraints");
	return (null);
}

public Box
showLabelsOnlyProperties()
throws Exception
{
	alert ("Not applicable for labels only constraint mode");
	return (null);
}

public Box
showComplexSceneProperties()
throws Exception
{
	/*
	Box mainPanel = super.showComplexSceneProperties();
	this.addFormatProperties(mainPanel);
	return (mainPanel);
	*/

	alert("Not available for entire scene constraint mode.\nUse a tighter constraints");
	return (null);
}

/*************** MOUSE STUFF ***************************/

private boolean mouseReleased = false;
private boolean shiftKeyPressed = false;
private boolean rhtButtonPressed = false;

public void
runMousePressed(MouseEvent event)
throws Exception
{
	mouseReleased = false;
	shiftKeyPressed = false;
	rhtButtonPressed = false;

	super.runMousePressed(event);

	if (
		(complexSceneView.getCurrentMouseState() == 4) ||
		(complexSceneView.getCurrentMouseState() == 5))
	{
		if (!this.currentVarsSet)
			return;
		rhtButtonPressed = true;
		boolean test = initTabScene();
		return;
	}

	// make sure left button was clicked
	if (!((complexSceneView.getCurrentMouseState() == 16) || // lft bt
		(complexSceneView.getCurrentMouseState() == 17)))  // lft bt + shift key
		return;
	
	if (event.isShiftDown())
		shiftKeyPressed = true;
	
	boolean testReset = this.resetCurrentVars(!inFormatHelix());
	if (!testReset && !inFormatHelix())
		return;

	DrawObject drawObject = this.getCurrentDrawObject();
	if (drawObject == null)
		return;

	if (!initTabScene())
		return;

	if (inFormatHelix())
	{
		setFormatHelixFrame();
		return;
	}
}

private boolean
inFormatHelix()
{
	return ((formatHelixFrame != null) && formatHelixFrame.isShowing()
		&& (!formatHelixFrame.isIcon()));
}

private void
resetFormatSStrRBs()
{
}

public boolean
initTabScene()
throws Exception
{
	if (inFormatHelix() && (this.getCurrentComplexPickMode() != ComplexDefines.InRNASingleNuc))
	{
		alert("Need to be in rna single nuc constraint mode\n" +
			" to use Format Helix");
		return (false);
	}

	if (!super.initTabScene())
		return (false);

	// first constrain to see if valid translate edit

	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc to affect");
			return (false);
		}
		if (inFormatHelix() && this.getCurrentWorkNuc().isBasePair())
		{
			alert("Can't format a nucleotide that is already base paired");
			return (false);
		}
	  	break;
	  case ComplexDefines.InRNASingleStrand :
	  	if ((this.getCurrentWorkNuc() == null) || this.getCurrentWorkNuc().isBasePair())
		{
			alert("Must pick Non-BasePaired Nuc in single strand to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InRNABasePair :
	  	if ((this.getCurrentWorkNuc() == null) || !this.getCurrentWorkNuc().isBasePair())
		{
			alert("Must pick BasePaired Nuc in Helix to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InRNAHelix :
	  	if ((this.getCurrentWorkNuc() == null) || !this.getCurrentWorkNuc().isBasePair())
		{
			alert("Must pick BasePaired Nuc in Helix to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InRNAHelicalRun :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in stacked helix to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InRNASubDomain :
	  	if ((this.getCurrentWorkNuc() == null) || !this.getCurrentWorkNuc().isBasePair())
		{
			alert("Must pick BasePaired Nuc in sub-domain to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InRNANamedGroup :
	  	if ((this.getCurrentWorkNuc() == null) || (this.getCurrentWorkNuc().getGroupName() == null))
		{
			alert("Must pick Nuc in a domain to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InRNAColorUnit :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in color-unit to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InRNACycle :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in level to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InRNAListNucs :
		if (!rhtButtonPressed)
		{
			alert("'rna list nucs' can't be used in left mouse button editing");
			return (false);
		}
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in list-nucs to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InRNASSData :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in a rna strand to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InComplex :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in complex to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InLabelsOnly :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Label to affect");
			return (false);
		}
		break;
	  case ComplexDefines.InComplexScene :
		break;

	  default :
	  	break;
	}

	// erase from sceneImg
	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		editErase(this.getCurrentWorkNuc(), ComplexDefines.InRNASingleNuc);
	  	break;
	  case ComplexDefines.InRNASingleStrand :
		editErase(this.getCurrentWorkSingleStrand(), ComplexDefines.InRNASingleStrand);
		break;
	  case ComplexDefines.InRNABasePair :
		editErase(this.getCurrentWorkBasePair(), ComplexDefines.InRNABasePair);
		break;
	  case ComplexDefines.InRNAHelix :
		editErase(this.getCurrentWorkHelix(), ComplexDefines.InRNAHelix);
		break;
	  case ComplexDefines.InRNAHelicalRun :
		editErase(this.getCurrentWorkStackedHelix(), ComplexDefines.InRNAHelicalRun);
		break;
	  case ComplexDefines.InRNASubDomain :
		editErase(this.getCurrentWorkSubDomain(), ComplexDefines.InRNASubDomain);
		break;
	  case ComplexDefines.InRNANamedGroup :
		editErase(this.getCurrentWorkNamedGroup(), ComplexDefines.InRNANamedGroup);
		break;
	  case ComplexDefines.InRNAColorUnit :
		editErase(this.getCurrentWorkColorUnit(), ComplexDefines.InRNAColorUnit);
		break;
	  case ComplexDefines.InRNACycle :
		editErase(this.getCurrentWorkCycle(), ComplexDefines.InRNACycle);
		break;
	  case ComplexDefines.InRNAListNucs :
		if (this.getCurrentWorkListNucs().isSet())
			editErase(this.getCurrentWorkListNucs(), ComplexDefines.InRNAListNucs);
		break;
	  case ComplexDefines.InRNASSData :
		editErase(this.getCurrentWorkSStr(), ComplexDefines.InRNASSData);
		break;
	  case ComplexDefines.InComplex :
		editErase(this.getCurrentWorkRNASSComplex(), ComplexDefines.InComplex);
		break;
	  case ComplexDefines.InLabelsOnly :
		break;
	  case ComplexDefines.InComplexScene :
		// NOT SURE yet what this implies
		// editErase(this.getCurrentWorkComplexScene(), ComplexDefines.InComplexScene);
		//
		break;
	  default :
	  	break;
	}
	
	this.initTabGraphics();
	this.updateEditColor(this.getCurrentComplexPickMode(),
		complexSceneView.moveableEditingColor, sceneImgG2);

	return (true);
}

public void
undoEditColorScheme()
throws Exception
{
	if (inFormatHelix())
		return;
	if ((this.currentWorkDrawObject != null) && (this.currentWorkDrawObject.getIsEditable()))
	{
		this.updateEditColor(this.currentWorkDrawObject, null, sceneImgG2);
	}
}

public boolean
resetCurrentVars(boolean constrain)
throws Exception
{
	// undo edit coloring if possible
	if ((this.currentWorkDrawObject != null) && (this.currentWorkDrawObject.getIsEditable()))
	{
		if ((this.currentWorkDrawObject == this.getCurrentWorkListNucs()) &&
			!this.getCurrentWorkListNucs().isSet())
			return (super.resetCurrentVars(constrain));
		this.undoEditColorScheme();
	}
	
	return (super.resetCurrentVars(constrain));
}

/*************  END MOUSE STUFF *********************/

/************** PropertyChangeListener Implementaion ***************/

public void
propertyChange(PropertyChangeEvent evt)
{
	super.propertyChange(evt);

	String propertyName = evt.getPropertyName();

	if (propertyName == null)
		return;
}

/************** End PropertyChangeListener Implementaion *************/

/*************** Private Classes *************************/
/*************** End Private Classes *************************/

private static void
debug(String s)
{
	System.err.println("ComplexSceneFormatSStrTab-> " + s);
}

}
