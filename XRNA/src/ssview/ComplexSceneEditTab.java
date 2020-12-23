package ssview;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import jimage.DrawObjectLeafNode;
import jimage.DrawCharObject;
import jimage.DrawArrowObject;
import jimage.DrawCircleObject;
import jimage.DrawTriangleObject;
import jimage.DrawParallelogramObject;
import jimage.DrawFontObject;
import jimage.DrawLineObject;
import jimage.DrawObject;
import jimage.DrawObjectCollection;
import jimage.DrawStringObject;
import jimage.FontChooser;

import util.*;
import util.math.*;

public class
ComplexSceneEditTab
extends ComplexSceneWorkTab
{

private JRadioButton moveAlongAxisEditModeRB = null;
private JRadioButton addNewStringLabelRB = null;
private JRadioButton singleStrandFormatStraight_RB = null;
private JRadioButton overlayHorizontalGridRB = null;
private JRadioButton overlayVerticalGridRB = null;
private JRadioButton overlayCircleRB = null;
private JRadioButton showCycleRB = null;

private JRadioButton nucNumberLabelsEveryFiftyRB = null;
private JRadioButton nucNumberLabelsEveryTenRB = null;
private JRadioButton nucLineLabelsEveryFiftyRB = null;
private JRadioButton nucLineLabelsEveryTenRB = null;
private JTextField nucNumberLabelStartID_TF = null;

private JComboBox gridIncComboBox = null;
private JTextField arcRadiusTF = null;
private JButton circleLabelRadiusUpBt = null;
private JButton circleLabelRadiusDownBt = null;

private JButton triangleLabelHeightUpBt = null;
private JButton triangleLabelHeightDownBt = null;
private JButton triangleLabelWidthUpBt = null;
private JButton triangleLabelWidthDownBt = null;
private JButton triangleLabelAngleUpBt = null;
private JButton triangleLabelAngleDownBt = null;
private JTextField triangleHeight_TF = null;
private JTextField triangleWidth_TF = null;
private JTextField triangleAngle_TF = null;

private JButton extendLineLonger_Bt = null;
private JButton extendLineShorter_Bt = null;
private JTextField overlayBorder_TF = null;

private JButton parallelogramLabelSide1Up_Bt = null;
private JButton parallelogramLabelAngle1Up_Bt = null;
private JButton parallelogramLabelSide2Up_Bt = null;
private JButton parallelogramLabelAngle2Up_Bt = null;
private JButton parallelogramLabelSide1Down_Bt = null;
private JButton parallelogramLabelAngle1Down_Bt = null;
private JButton parallelogramLabelSide2Down_Bt = null;
private JButton parallelogramLabelAngle2Down_Bt = null;
private JTextField parallelogramLabelSide1_TF = null;
private JTextField parallelogramLabelAngle1_TF = null;
private JTextField parallelogramLabelSide2_TF = null;
private JTextField parallelogramLabelAngle2_TF = null;

private JButton arrowLabelHeightUp_Bt = null;
private JButton arrowLabelHeightDown_Bt = null;
private JButton arrowLabelBaseWidthUp_Bt = null;
private JButton arrowLabelBaseWidthDown_Bt = null;
private JButton arrowLabelTailLengthUp_Bt = null;
private JButton arrowLabelTailLengthDown_Bt = null;
private JButton arrowLabelTailWidthUp_Bt = null;
private JButton arrowLabelTailWidthDown_Bt = null;
private JButton arrowLabelBaseIndentUp_Bt = null;
private JButton arrowLabelBaseIndentDown_Bt = null;
private JButton arrowLabelAngleUp_Bt = null;
private JButton arrowLabelAngleDown_Bt = null;
private JTextField arrowLabelHeight_TF = null;
private JTextField arrowLabelBaseWidth_TF = null;
private JTextField arrowLabelTailLength_TF = null;
private JTextField arrowLabelTailWidth_TF = null;
private JTextField arrowLabelBaseIndent_TF = null;
private JTextField arrowLabelAngle_TF = null;

private JButton applyDrawObjectCenterXRightBt = null;
private JButton applyDrawObjectCenterXLeftBt = null;
private JButton applyDrawObjectCenterYUpBt = null;
private JButton applyDrawObjectCenterYDownBt = null;
private JButton applyDrawObjectCenterXBt = null;
private JButton applyDrawObjectCenterYBt = null;
private JButton cycleRadiusUpBt = null;
private JButton cycleRadiusDownBt = null;
private JButton domainRotateCycleUpBt = null;
private JButton domainRotateCycleDownBt = null;
private JButton applyChangeBasePairDistanceRightBt = null;
private JButton applyChangeBasePairDistanceLeftBt = null;
private JButton moveDelNucLeftBt = null;
private JButton moveDelNucRightBt = null;
private JButton moveDelNucUpBt = null;
private JButton moveDelNucDownBt = null;

private JButton editFontChooserBt = null;

JRadioButton lineEndPtMidRB = null;
JRadioButton lineEndPt3PRB = null;
JRadioButton lineEndPt5PRB = null;
double moveSingleStrandInc = 0.2;

JTextField sstrNameTF = null;
JTextField complexNameTF = null;
JTextField complexSceneNameTF = null;
JTextField nucLineLength_TF = null;
JTextField nucLineInnerDist_TF = null;
JTextField nucLineOuterDist_TF = null;

private Cursor complexEditCursor = null;

private AffineTransform parentAffTr = null;
private AffineTransform parentInvAffTr = null;
private Point2D parentTransformTestPt = null;
private Point2D parentTransformedPt = null;
private AffineTransform rotateAffTr = null;
private Point2D rotateMousePt = null; // pt picked in relation to drawObject
private Point2D lastRotateMousePt = null; // pt picked in relation to drawObject
private Point2D rotateAnchorPt = null; // pt on drawObject rotateMousePt translates to

protected transient PropertyChangeSupport
	propertyChangeListeners = null;

JPanel partitionSSPanel = null;
JPanel deletePanel = null;

private JTextField currentGroupName_TF = null;

// not sure static will work very well unless everything associated with it is static
// public static JPanel nucLabelsPanel = null;
private JInternalFrame nucLabelsFrame = null;
private JPanel nucLabelsPanel = null;

public
ComplexSceneEditTab()
{
	this.makeCursors();
	this.buildGui(complexSceneView.guiBGColor,
		complexSceneView.controlPanelW, complexSceneView.controlPanelH);
	propertyChangeListeners = new PropertyChangeSupport(this);
}

public
ComplexSceneEditTab(Color guiBGColor, int panelW, int panelH)
{
	this.guiBGColor = guiBGColor;
	this.makeCursors();
	this.buildGui(guiBGColor, panelW, panelH);
	
//	this.buildGui(guiBGColor,complexSceneView.controlPanelW, complexSceneView.controlPanelH);
	
	propertyChangeListeners = new PropertyChangeSupport(this);
}

public void
setPostBuildGuiMethods()
throws Exception
{
	super.setPostBuildGuiMethods();
	this.setMouseMethod();
	this.setKeyMethod();	
}

public void
buildGui(Color guiBGColor, int panelW, int panelH)
{
	super.buildGui(guiBGColor, panelW, panelH);

	controlBtPanel = new JPanel(new GridLayout(15, 1), true);
	controlBtPanel.setFont(new Font("Dialog", Font.PLAIN, 10));
	controlBtPanel.setForeground(Color.black);
	controlBtPanel.setBackground(guiBGColor);

	ButtonGroup editGroupRBs = new ButtonGroup();

	moveAlongAxisEditModeRB = new JRadioButton();
	moveAlongAxisEditModeRB.setSelected(false);
//	moveAlongAxisEditModeRB.setText("move along helical axis (arrow key edit only)");
	moveAlongAxisEditModeRB.setText("move along helical axis");
	moveAlongAxisEditModeRB.setActionCommand("move along helical axis");
	moveAlongAxisEditModeRB.setFont(btFont);
	moveAlongAxisEditModeRB.setForeground(Color.black);
	moveAlongAxisEditModeRB.setBackground(guiBGColor);
	controlBtPanel.add(moveAlongAxisEditModeRB);
	editGroupRBs.add(moveAlongAxisEditModeRB);
	moveAlongAxisEditModeRB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (complexSceneView.getComplexScene() != null)
					printEditTabPickMode();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	addNewStringLabelRB = new JRadioButton();
	addNewStringLabelRB.setSelected(false);
	addNewStringLabelRB.setText("Add New String");
	addNewStringLabelRB.setActionCommand("add_new_string");
	addNewStringLabelRB.setFont(btFont);
	addNewStringLabelRB.setForeground(Color.black);
	addNewStringLabelRB.setBackground(guiBGColor);
	/* NOT READY YET
	controlBtPanel.add(addNewStringLabelRB);
	editGroupRBs.add(addNewStringLabelRB);
	*/
	addNewStringLabelRB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				printEditTabPickMode();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	singleStrandFormatStraight_RB = new JRadioButton();
	singleStrandFormatStraight_RB.setSelected(false);
	singleStrandFormatStraight_RB.setText("Single Strands straight");
	singleStrandFormatStraight_RB.setActionCommand("always_format_single_strands_straight");
	singleStrandFormatStraight_RB.setFont(btFont);
	singleStrandFormatStraight_RB.setForeground(Color.black);
	singleStrandFormatStraight_RB.setBackground(guiBGColor);
	controlBtPanel.add(singleStrandFormatStraight_RB);

	overlayHorizontalGridRB = new JRadioButton();
	overlayHorizontalGridRB.setSelected(false);
	overlayHorizontalGridRB.setText("Show HorizontalGrid");
	overlayHorizontalGridRB.setFont(btFont);
	overlayHorizontalGridRB.setForeground(Color.black);
	overlayHorizontalGridRB.setBackground(guiBGColor);
	controlBtPanel.add(overlayHorizontalGridRB);
	overlayHorizontalGridRB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				complexSceneView.setDrawWithHorizontalGrid(
					overlayHorizontalGridRB.isSelected());
				if (complexSceneView.getComplexScene() != null)
					complexSceneView.updateImg();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	overlayVerticalGridRB = new JRadioButton();
	overlayVerticalGridRB.setSelected(false);
	overlayVerticalGridRB.setText("Show VerticalGrid");
	overlayVerticalGridRB.setFont(btFont);
	overlayVerticalGridRB.setForeground(Color.black);
	overlayVerticalGridRB.setBackground(guiBGColor);
	controlBtPanel.add(overlayVerticalGridRB);
	overlayVerticalGridRB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				complexSceneView.setDrawWithVerticalGrid(
					overlayVerticalGridRB.isSelected());
				if (complexSceneView.getComplexScene() != null)
					complexSceneView.updateImg();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	gridIncComboBox	 = new JComboBox(new String[]
	{ "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
		"15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
		"25", "26", "27", "28", "29", "30"});
	gridIncComboBox.setActionCommand("Grid Inc");
	gridIncComboBox.setSelectedItem("10");
	gridIncComboBox.setFont(new Font("Dialog", Font.BOLD, 12));
	gridIncComboBox.setForeground(Color.black);
	gridIncComboBox.setBackground(guiBGColor);
	JLabel gridLabel = new JLabel("Grid Inc:");
	gridLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
	gridLabel.setLabelFor(gridIncComboBox);
	JPanel tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.add(gridLabel);
	tmpPanel.add(gridIncComboBox);
	controlBtPanel.add(tmpPanel);	
	gridIncComboBox.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent ae)
		{
			try
			{
				int val = Integer.parseInt((String)gridIncComboBox.getSelectedItem());

				// made it past valid double exception
				complexSceneView.setGridInc(val);
				if (complexSceneView.getComplexScene() != null)
					complexSceneView.updateImg();
			}
			catch (NumberFormatException e)
			{
				System.out.println("IN NUMBERFORMAT EXCEPTION in gridIncComboBox: " + e.toString());
				// keep last angle and don't do anything
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in gridIncComboBox.actionPerformed:", e, 101);
			}
		}
	});

	overlayCircleRB = new JRadioButton();
	overlayCircleRB.setSelected(false);
	overlayCircleRB.setText("Show Circle Overlay");
	overlayCircleRB.setFont(btFont);
	overlayCircleRB.setForeground(Color.black);
	overlayCircleRB.setBackground(guiBGColor);
	controlBtPanel.add(overlayCircleRB);
	overlayCircleRB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				complexSceneView.setDrawWithCircleOverlay(
					overlayCircleRB.isSelected());
				if (complexSceneView.getComplexScene() != null)
					complexSceneView.updateImg();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.setFont(smBtFont);
	JLabel tmpLabel = this.getNewViewLabel("radius");
	tmpLabel.setFont(smBtFont);
	tmpPanel.add(tmpLabel);
	arcRadiusTF = new JTextField("40.0", 4);
	arcRadiusTF.setFont(smBtFont);
	tmpPanel.add(arcRadiusTF);
	arcRadiusTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				complexSceneView.setOverlayArcRadius(
					Double.parseDouble(arcRadiusTF.getText()));
				if (complexSceneView.getComplexScene() != null)
					complexSceneView.updateImg();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid radius value: " + arcRadiusTF.getText());
				return;
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	JButton radiusDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(radiusDownBt);
	radiusDownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				if (!overlayCircleRB.isSelected())
					return;
				if (complexSceneView.getComplexScene() == null)
					return;
				(new OverlayArcRadiusBtPressed(false)).start();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}

		public void
		mouseReleased(MouseEvent event)
		{
			mouseIsPressed = false;
		}
	});

	JButton radiusUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(radiusUpBt);
	radiusUpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				if (!overlayCircleRB.isSelected())
					return;
				(new OverlayArcRadiusBtPressed(true)).start();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}

		public void
		mouseReleased(MouseEvent event)
		{
			mouseIsPressed = false;
		}
	});

	controlBtPanel.add(tmpPanel);

	/* Not sure why Color is in Edit. Leave for now.
	JButton editColorChooserBt = new JButton();
	btInsets = editColorChooserBt.getInsets();
	editColorChooserBt.setMargin(
		new Insets(btInsets.top, 0, btInsets.bottom, btInsets.right + lftInset + 57));
	editColorChooserBt.setText("Select Color");
	editColorChooserBt.setActionCommand("Select_Color");
	editColorChooserBt.setFont(btFont);
	editColorChooserBt.setForeground(Color.black);
	editColorChooserBt.setBackground(guiBGColor);
	controlBtPanel.add(editColorChooserBt);
	editColorChooserBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			ComplexSceneEditTab.this.setColorFrame();
			ComplexSceneEditTab.this.getColorChooser().setVisible(true);
			// ComplexSceneEditTab.this.getColorFrame().setVisible(true);
		}
	});
	*/

	/* SAVE and put back in after work on it
	JButton partitionSSBt = new JButton();
	btInsets = partitionSSBt.getInsets();
	partitionSSBt.setMargin(
		new Insets(btInsets.top, 0, btInsets.bottom, btInsets.right + lftInset + 57));
	partitionSSBt.setText("Partition SS");
	partitionSSBt.setFont(btFont);
	partitionSSBt.setForeground(Color.black);
	partitionSSBt.setBackground(guiBGColor);
	controlBtPanel.add(partitionSSBt);
	partitionSSBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			setRBsToFalse();
			NEEDTOCHANGE_SPartitionSSDialog();
		}
	});
	*/

//	int btLengthAdjust = -15; // make longest string centered

	int btLengthAdjust = -8;
	
	JButton reCenterBt = new JButton();
	btInsets = reCenterBt.getInsets();
	reCenterBt.setMargin(new Insets(btInsets.top, lftInset,
		btInsets.bottom, btInsets.right + btLengthAdjust));
	reCenterBt.setText("Re-Center");
//	reCenterBt.setFont(btFont);
	reCenterBt.setFont(new Font("Dialog", Font.BOLD, 10));
	reCenterBt.setForeground(Color.black);
	reCenterBt.setBackground(guiBGColor);
	controlBtPanel.add(reCenterBt);
	reCenterBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			try
			{
				if (complexSceneView.getComplexScene() == null)
					return;
				complexSceneView.getComplexScene().reCenter(0.0, 0.0);
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.reCenter():", e, 101);
			}
		}
	});

	/* Not sure I need both Re-Center and center; but maybe should have one called Flatten*/
	// this shifts everything
	JButton centerSceneBt = new JButton("Center Scene at Origin");
	centerSceneBt.setActionCommand("center_scene");
	centerSceneBt.setFont(new Font("Dialog", Font.BOLD, 10));
	centerSceneBt.setForeground(Color.black);
	centerSceneBt.setBackground(guiBGColor);
	btInsets = centerSceneBt.getInsets();
	centerSceneBt.setMargin(new Insets(btInsets.top, lftInset,
		btInsets.bottom, btInsets.right + btLengthAdjust));
	controlBtPanel.add(centerSceneBt);
	centerSceneBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			if (complexSceneView.getComplexScene() == null)
			{
				alert("Display Secondary Structure First");
				return;
			}
			try
			{
				complexSceneView.getComplexScene().center();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneView.centerSceneBt:", e, 101);
			}
		}
	});
	/**/

	/* NEED to get going, but not here:
	JButton editComplexCollectionBt = new JButton();
	btInsets = editComplexCollectionBt.getInsets();
	editComplexCollectionBt.setMargin(
		new Insets(btInsets.top, 0, btInsets.bottom, btInsets.right + lftInset + 57));
	editComplexCollectionBt.setText("Edit Current Complex");
	editComplexCollectionBt.setFont(btFont);
	editComplexCollectionBt.setForeground(Color.black);
	editComplexCollectionBt.setBackground(guiBGColor);
	controlBtPanel.add(editComplexCollectionBt);
	editComplexCollectionBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			try
			{
				// ALL OF THIS THIS DOESN't WORK YET
				setCurrentDrawObject(null);
				if (!resetCurrentVars(true))
					return;
				String title = null;
				ComplexCollection complexNode =
					complexSceneView.getComplexTreePick();
				int nodeLevel =
					ComplexScene.getChildLevel(complexNode);
				
				if (nodeLevel == 0)
				{
					title = "Edit Scene Properties";
					complexPropertiesPanel = showComplexSceneProperties();
				}
				else if (nodeLevel == 1)
				{
					ComplexSSDataCollection2D collection =
						(ComplexSSDataCollection2D)complexNode;
					setCurrentWorkRNASSComplex(collection);
					title = "Edit Complex Properties";
					complexPropertiesPanel = showComplexProperties();
				}
				else if (nodeLevel == 2)
				{
					SSData2D sstr = (SSData2D)complexNode;
					debug("sstr: " + sstr);
					setCurrentWorkSStr(sstr);
					title = "Edit SSData Properties";
					complexPropertiesPanel = showRNASSDataProperties();
					// ComplexSceneEditTab.this.runSchematics(sstr);
				}
				//
				// alert("CURRENT COLLECTION LEVEL,NAME: " + nodeLevel + " " +
					// complexNode.toString());
				//
				updateComplexPropertiesFrame(title);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.editComplexCollectionBt:", e, 101);
			}
		}
	});
	*/

	controlBtPanelScrollPane = new JScrollPane(controlBtPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	controlPanel.add(BorderLayout.CENTER, controlBtPanelScrollPane);
}

class OverlayArcRadiusBtPressed
extends Thread
{
	private double radiusInc = 1.0;
	private boolean radiusDirection = false;

	OverlayArcRadiusBtPressed(boolean direction)
	{
		mouseIsPressed = true;
		radiusDirection = direction;
	}

	public void
	run()
	{
		setNewRadius();
		try
		{
			this.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
		while (true)
		{
			this.yield();
			if (!mouseIsPressed)
				break;
			setNewRadius();
		}
	}

	private void
	setNewRadius()
	{
		double currentRadius = complexSceneView.getOverlayArcRadius();
		if ((!radiusDirection) && (currentRadius <= 2.0))
			return;
		if (radiusDirection && (2.0*currentRadius > complexSceneView.getWindowViewW()))
			return;
		if (radiusDirection)
			complexSceneView.setOverlayArcRadius(currentRadius + radiusInc);
		else
			complexSceneView.setOverlayArcRadius(currentRadius - radiusInc);
		arcRadiusTF.setText(StringUtil.roundStrVal(
			complexSceneView.getOverlayArcRadius(), 2));
		complexSceneView.updateImg();
	}
}

private boolean keyInitiallyPressed = false;
private boolean keyBeingHeldDown = false;
private boolean keyWasReleased = false;

public void
setKeyMethod()
{
	this.addKeyListener(new KeyAdapter()
	{
		public void
		keyPressed(KeyEvent keyEvent)
		{
			if ((!keyInitiallyPressed) && (!keyBeingHeldDown))
			{
				keyInitiallyPressed = true;
				keyBeingHeldDown = true;
				keyWasReleased = false;
			}
			else
			{
				keyInitiallyPressed = false;
				keyBeingHeldDown = true;
				keyWasReleased = false;
			}
			try
			{
				int keyCode = keyEvent.getKeyCode();
				switch (keyCode)
				{
				  case 38 : // up arrow
				  case 33 : // up rht arrow
				  case 39 : // rht arrow
				  case 34 : // down rht arrow
				  case 40 : // down arrow
				  case 35 : // down lft arrow
				  case 37 : // lft arrow
				  case 36 : // up lft arrow

					// Cycle through nucs one by one
					  /* NEED to look into alternative to cycling through nucs
					if (inquireOnlyRB.isSelected() &&
						(getCurrentComplexPickMode() == ComplexDefines.InRNASingleNuc) &&
						(getCurrentWorkNuc() != null))
					{
						if ((keyCode == 38) || (keyCode == 39))
						{
							complexSceneView.setWindowToDrawObjectCenter(getCurrentWorkNuc().nextVisibleNuc2D());

							complexSceneView.viewRobot.mousePress(InputEvent.BUTTON1_MASK);
							complexSceneView.viewRobot.mouseRelease(InputEvent.BUTTON1_MASK);
						}
						else if ((keyCode == 40) || (keyCode == 37))
						{
							complexSceneView.setWindowToDrawObjectCenter(getCurrentWorkNuc().lastVisibleNuc2D());

							complexSceneView.viewRobot.mousePress(InputEvent.BUTTON1_MASK);
							complexSceneView.viewRobot.mouseRelease(InputEvent.BUTTON1_MASK);
						}
					}
					else
					{
					*/
						runMoveEditKeyPressed(getCurrentDrawObject(), keyCode);
					/*
					}
					*/
					break;
				  default :
					// printConsole("KEY PRESSED: " + keyCode);
					// debug("KEY PRESSED: " + keyCode);
					break;
				}
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.setKeyMethod().keyPressed():", e, 2);
			}
		}

		public void
		keyReleased(KeyEvent keyEvent)
		{
			keyInitiallyPressed = false;
			keyBeingHeldDown = false;
			keyWasReleased = true;
			try
			{
				int keyCode = keyEvent.getKeyCode();
				if (keyEvent.getKeyChar() == ':')
				{
					return;
				}
				switch (keyCode)
				{
				  case 38 : // up arrow
				  case 33 : // up rht arrow
				  case 39 : // rht arrow
				  case 34 : // down rht arrow
				  case 40 : // down arrow
				  case 35 : // down lft arrow
				  case 37 : // lft arrow
				  case 36 : // up lft arrow
					runMoveEditKeyReleased(getCurrentDrawObject(), keyCode);
					break;
				  default :
					// printConsole("KEY RELEASED: " + keyCode);
					// debug("KEY RELEASED: " + keyCode);
					break;
				}
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.setKeyMethod().keyReleased():", e, 2);
			}
		}
	});
}

private void
setRBsToFalse()
{
	moveAlongAxisEditModeRB.setSelected(false);
	addNewStringLabelRB.setSelected(false);
	overlayHorizontalGridRB.setSelected(false);
	overlayVerticalGridRB.setSelected(false);
	overlayCircleRB.setSelected(false);
}

private JFrame partitionSSFrame = null;

public void
NEEDTOCHANGE_SPartitionSSDialog()
{
	String title = null;

	title = ComplexScene.getPickModePrefixTitle(this.getCurrentComplexPickMode()) +
		" PartitionSS";
	partitionSSPanel = new PartitionSSPanel(title);

	this.addPropertyChangeListener((PropertyChangeListener)partitionSSPanel);

	if (partitionSSFrame == null)
	{
		partitionSSFrame = new JFrame();
		partitionSSFrame.setBounds(110, 60, 200, 500);
		partitionSSFrame.addWindowListener(new WindowAdapter()
		{
			public void
			windowClosing(WindowEvent event)
			{
				Object object = event.getSource();
				if (event.getSource() != partitionSSFrame) 
					return;
				ComplexSceneEditTab.this.removePropertyChangeListener(
					(PropertyChangeListener)partitionSSPanel);
				// debug("MADE IT TO WIN CLOSE");
				//
				// if (object == partitionSSFrame)
					// complexTabsExit(event);
				//
			}

			public void
			windowActivated(WindowEvent event)
			{
				// debug("MADE IT TO WIN ACTIVATED: " + event.getSource());
			}

			public void
			windowDeactivated(WindowEvent event)
			{
				if (event.getSource() != partitionSSFrame) 
					return;
				// debug("MADE IT TO PARTITIONFRAME DEACTIVATED: " + event.getSource());
				// debug("MADE IT TO WIN DEACTIVATED: " +
					// partitionSSFrame.isShowing() + " " +
					// partitionSSFrame.isVisible() + " " +
					// partitionSSFrameIconified);

				// if (!partitionSSFrameIconified)
					// partitionSSFrame.toFront();
			}

			public void
			windowIconified(WindowEvent event)
			{
				if (event.getSource() != partitionSSFrame) 
					return;
				// partitionSSFrameIconified = true;
			}

			public void
			windowDeiconified(WindowEvent event)
			{
				if (event.getSource() != partitionSSFrame) 
					return;
				// partitionSSFrameIconified = false;
			}
		});	
	}
	partitionSSFrame.setTitle(title);
	partitionSSFrame.getContentPane().removeAll();
	partitionSSFrame.getContentPane().add(
		BorderLayout.CENTER, partitionSSPanel);

	partitionSSPanel.setVisible(true);
	// partitionSSFrame.pack();
	partitionSSFrame.setVisible(true);

	/*
	JDialog partitionSSDialog =
		new JDialog(complexSceneView.frameParent, title, true);
	partitionSSDialog.setVisible(true);
	*/
}

/*************** MOUSE STUFF ***************************/

private EditBtListener editBtListener = new EditBtListener();

public Box
addEditProperties(Box panel)
throws Exception
{
	JPanel tmpPanel = null;

	if (this.getCurrentWorkLabel() == null) // then must have been a nuc pick
	{
		this.setBestGuessNucLabelFont();

		// should try and make this all static at some point
		if (nucLabelsPanel == null)
		{
			nucLabelsPanel = new JPanel(new GridLayout(12, 1), true);
			nucLabelsPanel.setBackground(complexSceneView.guiBGColor);
			nucLabelsPanel.setForeground(Color.black);

			// MAYBE should be creating just one button to be used over and
			// over again. make it global and if null make then only.
			if (editFontChooserBt == null)
			{
				editFontChooserBt = this.getNewViewButton("Select Font");
				editFontChooserBt.setActionCommand("Select_Font");
				editFontChooserBt.addActionListener(new ActionListener()
				{
					public void
					actionPerformed(ActionEvent evt)
					{
						ComplexSceneEditTab.this.getFontChooser().setVisible(true);
					}
				});
			}
			nucLabelsPanel.add(this.getNewViewButtonLeftPanel(editFontChooserBt));

			ButtonGroup nucNumberLabelGroup_RBs = new ButtonGroup();
			ButtonGroup nucLineLabelGroup_RBs = new ButtonGroup();

			if (nucNumberLabelsEveryTenRB == null)
			{
				nucNumberLabelsEveryTenRB = new JRadioButton();
				nucNumberLabelsEveryTenRB.setSelected(true);
				nucNumberLabelGroup_RBs.add(nucNumberLabelsEveryTenRB);
			}
			nucLabelsPanel.add(this.getNewViewButtonLeftPanel(nucNumberLabelsEveryTenRB, "Add Number Every 10"));

			if (nucNumberLabelsEveryFiftyRB == null)
			{
				nucNumberLabelsEveryFiftyRB = new JRadioButton();
				nucNumberLabelsEveryFiftyRB.setSelected(false);
				nucNumberLabelGroup_RBs.add(nucNumberLabelsEveryFiftyRB);
			}
			nucLabelsPanel.add(this.getNewViewButtonLeftPanel(nucNumberLabelsEveryFiftyRB, "Add Number Every 50"));

			if (nucLineLabelsEveryTenRB == null)
			{
				nucLineLabelsEveryTenRB = new JRadioButton();
				nucLineLabelsEveryTenRB.setSelected(true);
				nucLineLabelGroup_RBs.add(nucLineLabelsEveryTenRB);
			}
			nucLabelsPanel.add(this.getNewViewButtonLeftPanel(nucLineLabelsEveryTenRB, "Add Line Every 10"));

			if (nucLineLabelsEveryFiftyRB == null)
			{
				nucLineLabelsEveryFiftyRB = new JRadioButton();
				nucLineLabelsEveryFiftyRB.setSelected(false);
				nucLineLabelGroup_RBs.add(nucLineLabelsEveryFiftyRB);
			}
			nucLabelsPanel.add(this.getNewViewButtonLeftPanel(nucLineLabelsEveryFiftyRB, "Add Line Every 50"));

			tmpPanel = this.getNewFlowLeftPanel();
			nucNumberLabelStartID_TF = new JTextField(4);
			nucNumberLabelStartID_TF.setText("1");
			tmpPanel.add(this.getNewViewLabel("nuc startID: "));
			tmpPanel.add(nucNumberLabelStartID_TF);
			nucLabelsPanel.add(tmpPanel);

			tmpPanel = this.getNewFlowLeftPanel();
			nucLineLength_TF = new JTextField(4);
			tmpPanel.add(this.getNewViewLabel("nuc line length: "));
			tmpPanel.add(nucLineLength_TF);
			nucLabelsPanel.add(tmpPanel);

			tmpPanel = this.getNewFlowLeftPanel();
			nucLineInnerDist_TF = new JTextField(4);
			nucLineInnerDist_TF.setText("" + ComplexDefines.NUCLABEL_NUC_TO_LINE_DISTANCE);
			tmpPanel.add(this.getNewViewLabel("nuc to start line dist: "));
			tmpPanel.add(nucLineInnerDist_TF);
			nucLabelsPanel.add(tmpPanel);

			tmpPanel = this.getNewFlowLeftPanel();
			nucLineOuterDist_TF = new JTextField(4);
			nucLineOuterDist_TF.setText("" + ComplexDefines.NUCLABEL_LINE_TO_NUMBER_DISTANCE);
			tmpPanel.add(this.getNewViewLabel("end line to number dist: "));
			tmpPanel.add(nucLineOuterDist_TF);
			nucLabelsPanel.add(tmpPanel);

			JButton setAllNucsLinesBt = this.getNewViewButton("Set All Nucs linelengths");
			setAllNucsLinesBt.addActionListener(new ActionListener()
			{
				public void
				actionPerformed(ActionEvent evt)
				{
					double length = 0.0;
					try
					{
						length = Double.parseDouble(nucLineLength_TF.getText());
					}
					catch (NumberFormatException e)
					{
						alert("Invalid length value: " + nucLineLength_TF.getText());
						return;
					}
					try
					{
						// this needs to be in Complex Collection so all constraints can be hit
						getCurrentWorkComplexScene().resetNucLabelsLineLengths(length);
						complexSceneView.renderDrawObjectView();
					}
					catch (Exception e)
					{
						complexSceneView.handleException("Exception in ComplexSceneEdit.setAllNucsLinesBt:", e, 101);
					}
				}
			});
			nucLabelsPanel.add(this.getNewViewButtonLeftPanel(setAllNucsLinesBt));

			JButton addNucLabelBt = this.getNewViewButton("Add Nuc Labels");
			addNucLabelBt.setActionCommand(ComplexDefines.EDIT_ADD_NUC_LABELS);
			nucLabelsPanel.add(this.getNewViewButtonLeftPanel(addNucLabelBt));
			addNucLabelBt.addActionListener(editBtListener);

			JButton deleteNucLabelBt = this.getNewViewButton("Delete Nuc Labels");
			deleteNucLabelBt.setActionCommand(ComplexDefines.EDIT_DELETE_NUC_LABELS);
			nucLabelsPanel.add(this.getNewViewButtonLeftPanel(deleteNucLabelBt));
			deleteNucLabelBt.addActionListener(editBtListener);
		}

		JButton addNucLabels_Bt = this.getNewViewButton("Nuc Labels");
		addNucLabels_Bt.addActionListener(new ActionListener()
		{
			public void
			actionPerformed(ActionEvent evt)
			{
				try
				{
					if (nucLabelsFrame == null)
						nucLabelsFrame = complexSceneView.getBasicInternalFrame(110, 60, 260, 460);
					complexSceneView.updateBasicInternalFrame("Add Nuc Labels", " ",
						nucLabelsPanel, nucLabelsFrame);
					if (nucLabelsFrame.isIcon())
						nucLabelsFrame.setIcon(false);
				}
				catch (Exception e)
				{
					complexSceneView.handleException(
						"Exception in ComplexSceneEditTab.addNucLabels: ", e, 101);
				}
			}
		});
		panel.add(this.getNewViewButtonLeftPanel(addNucLabels_Bt));
	}

	JButton setHiddenAttributeBt = this.getNewViewButton("Set Hidden Attribute");
	setHiddenAttributeBt.setActionCommand(ComplexDefines.EDIT_HIDE_DRAWOBJECT);
	panel.add(this.getNewViewButtonLeftPanel(setHiddenAttributeBt));
	setHiddenAttributeBt.addActionListener(editBtListener);

	if (currentGroupName_TF == null)
	{
		currentGroupName_TF = new JTextField("", 8);
		currentGroupName_TF.setFont(btFont);
	}
	currentGroupName_TF.setActionCommand(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP);
	currentGroupName_TF.addActionListener(editBtListener);
	tmpPanel = this.getNewFlowLeftPanel();
	JButton setToNamedGroup_Bt = this.getNewViewLeftButton("Add to Group named:");
	setToNamedGroup_Bt.setActionCommand(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP);
	tmpPanel.add(setToNamedGroup_Bt);
	setToNamedGroup_Bt.addActionListener(editBtListener);
	tmpPanel.setFont(smBtFont);
	tmpPanel.add(currentGroupName_TF);

	panel.add(tmpPanel);

	return (panel);
}

JTextField labelLineWidth_TF = null;
public Box
addCommonLabelProperties(Box panel)
throws Exception
{
	if (this.getCurrentWorkLabel() == null)
		return (panel);

	labelLineWidth_TF = new JTextField(4);
	ActionListener labelLineWidth_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double lineWidth = 0.0;
			try
			{
				lineWidth = Double.parseDouble(labelLineWidth_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid lineWidth value: " + labelLineWidth_TF.getText());
				return;
			}
			try
			{
				DrawObjectLeafNode labelObj = (DrawObjectLeafNode)getCurrentWorkLabel();
				editErase(labelObj, ComplexDefines.InLabelsOnly);
				labelObj.setLineWidth(lineWidth);
				editRedraw(labelObj, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.labelLineWidth_AL:", e, 101);
			}
		}
	};

	DrawObjectLeafNode labelObj = (DrawObjectLeafNode)this.getCurrentWorkLabel();

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	labelLineWidth_TF.addActionListener(labelLineWidth_AL);
	labelLineWidth_TF.setText("" + StringUtil.roundStrVal(labelObj.getLineWidth(), 2));
	tmpPanel.add(this.getNewViewLabel("Line Width: "));
	tmpPanel.add(labelLineWidth_TF);
	JButton applyLineWidthBt = this.getNewViewImgPlainButton();
	applyLineWidthBt.addActionListener(labelLineWidth_AL);
	tmpPanel.add(applyLineWidthBt);
	panel.add(tmpPanel);

	JRadioButton toggleFillBt = new JRadioButton();
	toggleFillBt.setSelected(!labelObj.getIsOpen());
	toggleFillBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				DrawObjectLeafNode labelObj = (DrawObjectLeafNode)getCurrentWorkLabel();
				editErase(labelObj, ComplexDefines.InLabelsOnly);
				labelObj.setIsOpen(labelObj.getIsOpen() ? false : true);
				editRedraw(labelObj, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	panel.add(this.getNewViewButtonPanel(toggleFillBt, "toggle fill"));

	JButton makeThisDefaultBt = this.getNewViewButton("Make this default");
	panel.add(this.getNewViewButtonPanel(makeThisDefaultBt));
	makeThisDefaultBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				DrawObjectLeafNode labelObj = (DrawObjectLeafNode)getCurrentWorkLabel();
				if (labelObj instanceof DrawLineObject)
				{
					DrawLineObject defLineObj = new DrawLineObject((DrawLineObject)labelObj);
					defLineObj.shiftXY(defLineObj.getX(), -defLineObj.getY());
					complexSceneView.annotateTabPanel.defaultLinePanel.setDefaultLine(defLineObj);
					alert("Default line in Annotate tab set to this line");
				}
				else if (labelObj instanceof DrawTriangleObject)
				{
					complexSceneView.annotateTabPanel.defaultTrianglePanel.setDefaultTriangle(new DrawTriangleObject((DrawTriangleObject)labelObj));
					alert("Default triangle in Annotate tab set to this triangle");
				}
				else if (labelObj instanceof DrawCircleObject)
				{
					complexSceneView.annotateTabPanel.defaultCirclePanel.setDefaultCircle(new DrawCircleObject((DrawCircleObject)labelObj));
					alert("Default circle in Annotate tab set to this circle");
				}
				else if (labelObj instanceof DrawParallelogramObject)
				{
					complexSceneView.annotateTabPanel.defaultParallelogramPanel.setDefaultParallelogram(new DrawParallelogramObject((DrawParallelogramObject)labelObj));
					alert("Default parallelogram in Annotate tab set to this parallelogram");
				}
				else if (labelObj instanceof DrawArrowObject)
				{
					complexSceneView.annotateTabPanel.defaultArrowPanel.setDefaultArrow(new DrawArrowObject((DrawArrowObject)labelObj));
					alert("Default arrow in Annotate tab set to this arrow");
				}
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	
	JButton deleteThisLabelBt = this.getNewViewButton("Delete Label");
	deleteThisLabelBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				DrawObject label = ComplexSceneEditTab.this.getCurrentWorkLabel();
				editErase(label, ComplexDefines.InLabelsOnly);
				((ComplexCollection)label.getParentCollection()).removeLabel(label);
				complexPropertiesPanel.setVisible(false);
				complexPropertiesFrame.setVisible(false);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.deleteThisLabelBt:", e, 101);
			}
		}
	});
	panel.add(this.getNewViewButtonPanel(deleteThisLabelBt));

	JButton reassociateToParentBt = this.getNewViewButton("Reassociate to parent");
	reassociateToParentBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				DrawObject label = ComplexSceneEditTab.this.getCurrentWorkLabel();
				ComplexCollection parent = (ComplexCollection)label.getParentCollection();
				if (parent instanceof ComplexScene) // then at top level, don't reassociate
				{
					alert("label already at top level, can't reassociate");
					return;
				}
				ComplexSceneEditTab.this.setCursor(complexSceneView.complexWaitCursor);
				((ComplexCollection)parent.getParentCollection()).reassociateLabel(label, parent);
				complexSceneView.updateDrawList();
				ComplexSceneEditTab.this.setCursor(complexSceneView.complexDefaultCursor);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.reassociateToParentBt:", e, 101);
			}
		}
	});
	panel.add(this.getNewViewButtonLeftPanel(reassociateToParentBt));

	JButton reassociateToCurrentBt = this.getNewViewButton("Reassociate to picked strand");
	reassociateToCurrentBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				DrawObject label = getCurrentWorkLabel();
				ComplexCollection parent = (ComplexCollection)label.getParentCollection();
				ComplexCollection newParent = complexSceneView.getComplexTreePick();
				newParent.reassociateLabel(label, parent);
				complexSceneView.updateDrawList();
				ComplexSceneEditTab.this.setCursor(complexSceneView.complexDefaultCursor);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.reassociateToCurrentBt:", e, 101);
			}
		}
	});
	panel.add(this.getNewViewButtonLeftPanel(reassociateToCurrentBt));

	return (panel);
}

private class
EditBtListener
implements ActionListener
{
	public void
	actionPerformed(ActionEvent evt)
	{
		String actionCmd = evt.getActionCommand();
		double nucLineLength = 0.0;

		if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
		{
			try
			{
				nucLineLength = Double.parseDouble(nucLineLength_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid nuc line length value: " +
					nucLineLength_TF.getText());
				nucLineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
			}
			if (nucLineLength <= 0.0)
				nucLineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
			nucLineLength_TF.setText(StringUtil.roundStrVal(nucLineLength, 2));
		}

		try
		{

		switch (getCurrentComplexPickMode())
		{
		  case ComplexDefines.InRNASingleNuc :
			Nuc2D nuc = getCurrentWorkNuc();
			if (nuc == null)
			{
				// alert("Must pick nuc to edit");
				return;
			}

			editErase(nuc, ComplexDefines.InRNASingleNuc);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				nuc.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				nuc.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				getCurrentWorkNuc().setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				getCurrentWorkNuc().setGroupName(testGroupName);
			}
			editRedraw(nuc, sceneImgG2);
			break;
		  case ComplexDefines.InRNASingleStrand :
			RNASingleStrand singleStrand = getCurrentWorkSingleStrand();
			if (singleStrand == null)
			{
				// alert("Must pick RNA single stranded region to edit");
				return;
			}

			editErase(singleStrand, ComplexDefines.InRNASingleStrand);

			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				singleStrand.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				singleStrand.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				singleStrand.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				singleStrand.setGroupName(testGroupName);
			}
			editRedraw(singleStrand, sceneImgG2);
			break;
		  case ComplexDefines.InRNABasePair :
			RNABasePair2D basePair = getCurrentWorkBasePair();
			if (basePair == null)
			{
				// alert("Must pick RNA Basepair to edit");
				return;
			}

			editErase(basePair, ComplexDefines.InRNABasePair);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				basePair.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				basePair.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				basePair.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				basePair.setGroupName(testGroupName);
			}

			editRedraw(basePair, sceneImgG2);
			break;
		  case ComplexDefines.InRNAHelix :
			RNAHelix2D helix = getCurrentWorkHelix();
			if (helix == null)
			{
				// alert("Must pick RNA Helix to edit");
				return;
			}
			editErase(helix, ComplexDefines.InRNAHelix);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				helix.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				helix.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				helix.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				helix.setGroupName(testGroupName);
			}
			editRedraw(helix, sceneImgG2);
			break;
		  case ComplexDefines.InRNAHelicalRun :
		  	RNAStackedHelix2D helicalRun = getCurrentWorkStackedHelix();
			if (helicalRun == null)
			{
				// alert("Must pick RNA Helical Run to edit");
				return;
			}
			editErase(helicalRun, ComplexDefines.InRNAHelicalRun);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				helicalRun.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				helicalRun.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				helicalRun.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				helicalRun.setGroupName(testGroupName);
			}
			editRedraw(helicalRun, sceneImgG2);
			break;
		  case ComplexDefines.InRNASubDomain :
			RNASubDomain2D subDomain = getCurrentWorkSubDomain();
			if (subDomain == null)
			{
				// alert("Must pick RNA Domain to edit");
				return;
			}
			editErase(subDomain, ComplexDefines.InRNASubDomain);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				subDomain.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				subDomain.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				subDomain.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				subDomain.setGroupName(testGroupName);
			}
			editRedraw(subDomain, sceneImgG2);
			break;
		  case ComplexDefines.InRNANamedGroup :
			RNANamedGroup2D namedGroup = getCurrentWorkNamedGroup();
			if (namedGroup == null)
			{
				// alert("Must pick RNA Domain to edit");
				return;
			}
			editErase(namedGroup, ComplexDefines.InRNANamedGroup);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				namedGroup.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				namedGroup.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				namedGroup.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				namedGroup.setGroupName(testGroupName);
			}
			editRedraw(namedGroup, sceneImgG2);
			break;
		  case ComplexDefines.InRNAColorUnit :
		  	RNAColorUnit2D colorUnit = getCurrentWorkColorUnit();
			if (colorUnit == null)
			{
				// alert("Must pick RNA ColorUnit to edit");
				return;
			}
			editErase(colorUnit, ComplexDefines.InRNAColorUnit);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				colorUnit.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				colorUnit.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				colorUnit.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				colorUnit.setGroupName(testGroupName);
			}
			editRedraw(colorUnit, sceneImgG2);
			break;
		  case ComplexDefines.InRNACycle :
			RNACycle2D level = getCurrentWorkCycle();
			if (level == null)
			{
				// alert("Must pick RNA Cycle to edit");
				return;
			}
			editErase(level, ComplexDefines.InRNACycle);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				level.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				level.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				level.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				level.setGroupName(testGroupName);
			}
			editRedraw(level, sceneImgG2);
			break;
		  case ComplexDefines.InRNAListNucs :
			RNAListNucs2D listNucs = getCurrentWorkListNucs();
			if (listNucs == null)
			{
				// alert("Must pick consecutive nucs to edit");
				return;
			}
			editErase(listNucs, ComplexDefines.InRNAListNucs);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				listNucs.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				listNucs.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				listNucs.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				listNucs.setGroupName(testGroupName);
			}
			editRedraw(listNucs, sceneImgG2);
			break;
		  case ComplexDefines.InRNASSData :
		  	SSData2D sstr = getCurrentWorkSStr();
			if (sstr == null)
			{
				// alert("Must pick Secondary Structure to edit");
				return;
			}

			editErase(sstr, ComplexDefines.InRNASSData);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				sstr.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				sstr.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
			{
				sstr.setIsHidden(true);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_TO_NAMED_GROUP))
			{
				String testGroupName = new String(getUserDefinedGroupName());
				if (testGroupName == null)
				{
					alert("No group name specified");
					return;
				}
				sstr.setGroupName(testGroupName);
			}
			editRedraw(sstr, sceneImgG2);
			break;
		  case ComplexDefines.InComplex :
		  	ComplexSSDataCollection2D complex = getCurrentWorkRNASSComplex();
			if (complex == null)
			{
				// alert("Must pick Complex to edit");
				return;
			}
			editErase(complex, ComplexDefines.InComplex);
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				complex.deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				complex.addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
				complex.setIsHidden(true);
			editRedraw(complex, sceneImgG2);
			break;
		  case ComplexDefines.InLabelsOnly :
			break;
			/*
		  case ComplexDefines.InComplexArea :
			break;
			*/
		  case ComplexDefines.InComplexScene :
			if (actionCmd.equals(ComplexDefines.EDIT_DELETE_NUC_LABELS))
				getCurrentWorkComplexScene().deleteAllNucLabels();
			else if (actionCmd.equals(ComplexDefines.EDIT_ADD_NUC_LABELS))
			{
				int startID = 0;
				try
				{
					startID = Integer.parseInt(nucNumberLabelStartID_TF.getText());
				}
				catch (NumberFormatException e)
				{
					alert("Non integer in startID text field");
					return;
				}
				getCurrentWorkComplexScene().addNucLabels(complexSceneView.fontChooser.getCurrentFont(), nucLineLength,
					nucNumberLabelsEveryTenRB.isSelected() ? 10 : 50,
					nucLineLabelsEveryTenRB.isSelected() ? 10 : 50, startID);
			}
			else if (actionCmd.equals(ComplexDefines.EDIT_HIDE_DRAWOBJECT))
				getCurrentWorkComplexScene().setIsHidden(true);
			complexSceneView.runRenderBt();
			break;
		  default :
		  	break;
		}
		}
		catch (Exception e)
		{
			complexSceneView.handleException("Exception in ComplexSceneEdit.EditBtListener:", e, 101);
		}
	}
}

private String
getUserDefinedGroupName()
{
	String groupName = currentGroupName_TF.getText();
	if ((groupName == null) || (groupName.length() < 1))
		return (null);

	groupName = currentGroupName_TF.getText().trim();
	if ((groupName == null) || (groupName.length() < 1))
		return (null);

	return (groupName);
}

// needs to be external to showNucProperties since used in inner Classes

private JTextField stringTF = null;

private JTextField drawObject_Center_X_TF = null;
private JTextField drawObject_Center_Y_TF = null;
private JTextField helixFPMidXTF = null;
private JTextField helixFPMidYTF = null;
private JTextField bpDistanceTF = null;

private JTextField lineAngle_TF = null;
private JTextField line_Length_TF = null;
private JTextField line_Extend_TF = null;
private JTextField line_P1_X_TF = null;
private JTextField line_P1_Y_TF = null;
private JTextField line_P2_X_TF = null;
private JTextField line_P2_Y_TF = null;

private JTextField singleStrand_Angle_TF = null;
private JTextField singleStrand_NucDist_TF = null;

private JTextField circle_Radius_TF = null;
private JTextField circle_StartAngle_TF = null;
private JTextField circle_AngleExtent_TF = null;

private JTextField cycle_Radius_TF = null;
private JTextField domainRotateCycleAngle_TF = null;

private JTextField beginXTF = null;
private JTextField beginYTF = null;
private JTextField endXTF = null;
private JTextField endYTF = null;

ActionListener
changeDrawObjectCenter_AL = new ActionListener()
{
	public void
	actionPerformed(ActionEvent evt)
	{
	  	if ((getCurrentComplexPickMode() == ComplexDefines.InRNASingleNuc) &&
			(getCurrentWorkNuc() != null) && getCurrentWorkNuc().isBasePair())
		{
			alert("Can't change position of base paired nucleotide in RNA Single Nuc mode\n" +
				"Use BasePair mode in the Edit Panel or the Format Panel"); 
			return;
		}

		DrawObject drwObj = currentDrawObjectStructure();
		if (drwObj == null)
		{
			alert("SOMETHING WRONG 100");
			return;
		}

		try
		{
			drwObj.runSetLocationHashtable(incCurrentUndoLevel());
		}
		catch (Exception e)
		{
			complexSceneView.handleException(
				"Exception in ComplexSceneEditTab.changeDrawObjectCenter_AL:", e, 101);
		}

		double x = 0.0;
		double y = 0.0;
		try
		{
			x = Double.parseDouble(drawObject_Center_X_TF.getText());
			y = -Double.parseDouble(drawObject_Center_Y_TF.getText());
		}
		catch (NumberFormatException e)
		{
			alert("Invalid position values: " +
				drawObject_Center_X_TF.getText() + " " + drawObject_Center_X_TF.getText());
			return;
		}
		try
		{
			int currentNucModeType = ComplexDefines.drawObjToNucModeDefine(drwObj);
			if (currentNucModeType == ComplexDefines.InComplexScene)
			{
			}
			else
			{
				editErase(drwObj, currentNucModeType);
			}
			if (drwObj instanceof DrawLineObject)
			{
				DrawLineObject lineObj = (DrawLineObject)drwObj;
				lineObj.setLinePartition(MathDefines.LINE_PARTITION_MID);
				lineObj.shiftXY(lineObj.getX() - x, -lineObj.getY() - y);
				editRedraw(lineObj, sceneImgG2);
				updateLineProperties(lineObj);
			}
			else
			{
				drwObj.setX(x);
				drwObj.setY(y);
				drawObject_Center_X_TF.setText("" + StringUtil.roundStrVal(drwObj.getX(), 2));
				drawObject_Center_Y_TF.setText("" + StringUtil.roundStrVal(-drwObj.getY(), 2));
			}

			if (currentNucModeType == ComplexDefines.InComplexScene)
			{
				complexSceneView.renderDrawObjectView();
			}
			else
			{
				editRedraw(drwObj, sceneImgG2);
			}
		}
		catch (Exception e)
		{
			complexSceneView.handleException(
				"Exception in ComplexSceneEditTab.changeDrawObjectCenter_AL:", e, 101);
		}
	}
};

MouseAdapter
changeDrawObjectCenter_ML = new MouseAdapter()
{
	public void
	mousePressed(MouseEvent evt)
	{
	  	if ((getCurrentComplexPickMode() == ComplexDefines.InRNASingleNuc) &&
			(getCurrentWorkNuc() != null) && getCurrentWorkNuc().isBasePair())
		{
			alert("Can't change position of base paired nucleotide in RNA Single Nuc mode\n" +
				"Use BasePair mode in the Edit Panel or the Format Panel"); 
			return;
		}

		Component mouseBt = evt.getComponent();
		try
		{
			DrawObject drwObj = currentDrawObjectStructure();
			if (drwObj == null)
			{
				alert("SOMETHING WRONG 100");
				return;
			}

			try
			{
				drwObj.runSetLocationHashtable(incCurrentUndoLevel());
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEditTab.changeDrawObjectCenter_AL:", e, 101);
			}

			editErase(drwObj, ComplexDefines.drawObjToNucModeDefine(drwObj));
			double shiftAmt = 0.5;
			if (drwObj instanceof DrawLineObject)
				((DrawLineObject)drwObj).setLinePartition(MathDefines.LINE_PARTITION_MID);

			if (mouseBt == applyDrawObjectCenterXRightBt)
			{
				drwObj.shiftX(-shiftAmt);
			}
			else if (mouseBt == applyDrawObjectCenterXLeftBt)
			{
				drwObj.shiftX(shiftAmt);
			}
			else if (mouseBt == applyDrawObjectCenterYUpBt)
			{
				drwObj.shiftY(-shiftAmt);
			}
			else if (mouseBt == applyDrawObjectCenterYDownBt)
			{
				drwObj.shiftY(shiftAmt);
			}

			if (drwObj instanceof DrawLineObject)
			{
				updateLineProperties((DrawLineObject)drwObj);
			}
			else
			{
				drawObject_Center_X_TF.setText("" + StringUtil.roundStrVal(drwObj.getX(), 2));
				drawObject_Center_Y_TF.setText("" + StringUtil.roundStrVal(-drwObj.getY(), 2));
			}

			editRedraw(drwObj, sceneImgG2);
		}
		catch (Exception e)
		{
			complexSceneView.handleException(e, 1);
		}
	}
};

private JPanel
changeDrawObjectXPanel()
throws Exception
{
	DrawObject drwObj = currentDrawObjectStructure();

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("Center X: "));
	drawObject_Center_X_TF = new JTextField(4);
	drawObject_Center_X_TF.setFont(smBtFont);
	drawObject_Center_X_TF.setText("" + StringUtil.roundStrVal(drwObj.getX(), 2));
	drawObject_Center_X_TF.addActionListener(changeDrawObjectCenter_AL);
	tmpPanel.add(drawObject_Center_X_TF);

	applyDrawObjectCenterXBt = this.getNewViewImgPlainButton();
	applyDrawObjectCenterXBt.addActionListener(changeDrawObjectCenter_AL);
	tmpPanel.add(applyDrawObjectCenterXBt);

	applyDrawObjectCenterXLeftBt = this.getNewViewImgLeftButton();
	applyDrawObjectCenterXLeftBt.addMouseListener(changeDrawObjectCenter_ML);
	tmpPanel.add(applyDrawObjectCenterXLeftBt);
	
	applyDrawObjectCenterXRightBt = this.getNewViewImgRightButton();
	applyDrawObjectCenterXRightBt.addMouseListener(changeDrawObjectCenter_ML);
	tmpPanel.add(applyDrawObjectCenterXRightBt);

	return (tmpPanel);
}

private JPanel
changeDrawObjectYPanel()
throws Exception
{
	DrawObject drwObj = currentDrawObjectStructure();

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("Center Y: "));
	drawObject_Center_Y_TF = new JTextField(4);
	drawObject_Center_Y_TF.setFont(smBtFont);
	drawObject_Center_Y_TF.setText("" + StringUtil.roundStrVal(-drwObj.getY(), 2));
	drawObject_Center_Y_TF.addActionListener(changeDrawObjectCenter_AL);
	tmpPanel.add(drawObject_Center_Y_TF);

	applyDrawObjectCenterYBt = this.getNewViewImgPlainButton();
	applyDrawObjectCenterYBt.addActionListener(changeDrawObjectCenter_AL);
	tmpPanel.add(applyDrawObjectCenterYBt);
	
	applyDrawObjectCenterYUpBt = this.getNewViewImgUpButton();
	applyDrawObjectCenterYUpBt.addMouseListener(changeDrawObjectCenter_ML);
	tmpPanel.add(applyDrawObjectCenterYUpBt);

	applyDrawObjectCenterYDownBt = this.getNewViewImgDownButton();
	applyDrawObjectCenterYDownBt.addMouseListener(changeDrawObjectCenter_ML);
	tmpPanel.add(applyDrawObjectCenterYDownBt);
	return (tmpPanel);
}

private JRadioButton setNucToSingleStrandDelineator_RB = null;

public Box
showRNASingleNucProperties()
throws Exception
{
	Box mainPanel = super.showRNASingleNucProperties();

	SSData2D sstr = this.getCurrentWorkNuc().getParentSSData2D();
	Nuc2D firstValidNuc = sstr.getFirstNonNullNuc2D();
	Nuc2D lastValidNuc = sstr.getEndNonNullNuc2D();
	if (this.getCurrentWorkNuc() != firstValidNuc)
	{
		mainPanel.add(this.getNewLabelPanel("Distance to last nuc: " +
			StringUtil.roundStrVal(this.getCurrentWorkNuc().getPoint2D().distance(
				this.getCurrentWorkNuc().lastNonNullNuc2D().getPoint2D()), 2)));
	}
	if (this.getCurrentWorkNuc() != lastValidNuc)
	{
		mainPanel.add(this.getNewLabelPanel("Distance to next nuc: " +
			StringUtil.roundStrVal(this.getCurrentWorkNuc().getPoint2D().distance(
				this.getCurrentWorkNuc().nextNonNullNuc2D().getPoint2D()), 2)));
	}

	mainPanel.add(this.getNewLabelPanel("Is Formatted: " +
		this.getCurrentWorkNuc().getIsFormatted()));

	mainPanel.add(this.getNewLabelPanel("Is Single Strand Delineator: " +
		this.getCurrentWorkNuc().isSingleStrandDelineator()));

	if (this.getCurrentWorkNuc().getGroupName() != null)
		mainPanel.add(this.getNewLabelPanel("belongs to domain named: " + this.getCurrentWorkNuc().getGroupName()));

	setNucToSingleStrandDelineator_RB = new JRadioButton();
	setNucToSingleStrandDelineator_RB.setSelected(this.getCurrentWorkNuc().isSingleStrandDelineator());
	setNucToSingleStrandDelineator_RB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			Nuc2D nuc = getCurrentWorkNuc();
			if (nuc.isNaturalSingleStrandDelineator())
			{
				alert("Can't unset this nuc as a single strand delineator");
			}
			else
			{
				nuc.setIsSingleStrandDelineator(setNucToSingleStrandDelineator_RB.isSelected());
			}
		}
	});
	mainPanel.add(this.getNewViewButtonLeftPanel(
		setNucToSingleStrandDelineator_RB, "set to Single Strand delineator"));

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	this.addEditProperties(mainPanel);
	double lineLength = this.getCurrentWorkNuc().getNucLabelLineLength();
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("" + this.getCurrentWorkNuc().getID());

	JButton reassociateBt = this.getNewViewButton("Reassociate");
	reassociateBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			Object[] message = new Object[3];
			message[1] = new JLabel("Associate with: " + "pickmode here");
			// message[1] = new JLabel("Currently can't overwrite existing file");
			// message[2] = new JLabel("Please list a unique file to write to");

			String[] options =
			{
				"Yes",
				"Cancel"
			};
			int result = JOptionPane.showOptionDialog(
				complexSceneView.frameParent,  // the parent that the dialog blocks
				message,  // the dialog message array
				"Reassociate Label", // the title of the dialog window
				JOptionPane.OK_CANCEL_OPTION, // option type
				JOptionPane.PLAIN_MESSAGE, // message type
				null, // optional icon, use null to use the default icon
				options, // options string array, will be made into buttons
				options[0] // option that should be made into a default button
				);

			switch (result)
			{
			  case 0: // yes
				JOptionPane.showMessageDialog(complexSceneView.frameParent, "Made it 0");
				break;
			  case 1: // no
				JOptionPane.showMessageDialog(complexSceneView.frameParent, "Made it 1");
				break;
			  default:
				break;
			}
		}
	});
	mainPanel.add(this.getNewViewButtonLeftPanel(reassociateBt));

	return (mainPanel);
}

JTextField arcDistTF = null;
JButton arcDistDownBt = null;
JButton arcDistUpBt = null;

/* LEAVE FOR AWHILE:
JRadioButton formatStraightRB = null;
JRadioButton formatArcedRB = null;
*/

public Box
showRNASingleStrandProperties()
throws Exception
{
	Box mainPanel = super.showRNASingleStrandProperties();

	/* LEAVE FOR AWHILE:
	ButtonGroup formatTypeGroupRBs = new ButtonGroup();
	*/

	JPanel formatArcedPanel = new JPanel(new GridLayout(4, 1), true);
	formatArcedPanel.setBackground(complexSceneView.guiBGColor);
	formatArcedPanel.setForeground(Color.black);
	formatArcedPanel.setBorder(new TitledBorder(this.beveledBorder, "Format Arced:"));

	/* LEAVE FOR AWHILE:
	formatArcedRB = new JRadioButton();
	formatArcedRB.setSelected(!isStraightLine);
	formatTypeGroupRBs.add(formatArcedRB);

	formatArcedPanel.add(this.getNewViewButtonPanel(formatArcedRB, "Format Arced"));
	*/

	// remake arc with current center distance
	JButton readjustArcInPlaceBt = this.getNewViewButton("Readjust Arc in place");
	readjustArcInPlaceBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			/* LEAVE FOR AWHILE:
			if (formatStraightRB.isSelected())
			{
				alert("Need to select 'Format Arced'\nin Properties menu");
				return;
			}
			*/
			try
			{
				RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();
				editErase(singleStrand, ComplexDefines.InRNASingleStrand);
				singleStrand.reformatArc();
				editRedraw(singleStrand, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Error in showRNASingleStrandProperties()", e, 1);
			}
		}
	});
	formatArcedPanel.add(this.getNewViewButtonPanel(readjustArcInPlaceBt));

	// readjust according to set defaults
	JButton readjustArcBt = this.getNewViewButton("Readjust Arc to default");
	readjustArcBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			/* LEAVE FOR AWHILE:
			if (formatStraightRB.isSelected())
			{
				alert("Need to select 'Format Arced'\nin Properties menu");
				return;
			}
			*/
			try
			{
				RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();
				editErase(singleStrand, ComplexDefines.InRNASingleStrand);
				singleStrand.formatArc();
				editRedraw(singleStrand, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Error in showRNASingleStrandProperties()", e, 1);
			}
		}
	});
	formatArcedPanel.add(this.getNewViewButtonPanel(readjustArcBt));

	// flip to other side of delineators
	JButton flipArc_Bt = this.getNewViewButton("flip");
	flipArc_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			/* LEAVE FOR AWHILE:
			if (formatStraightRB.isSelected())
			{
				alert("Need to select 'Format Arced'\nin Properties menu");
				return;
			}
			*/
			try
			{
				RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();
				if (singleStrand.isStraightLine())
				{
					alert("Single strand is straight; use 'Readjust Arc in place' first");
					return;
				}
				editErase(singleStrand, ComplexDefines.InRNASingleStrand);
				singleStrand.reformatArc(!singleStrand.isClockWiseFormatted());
				singleStrand.reset();
				editRedraw(singleStrand, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Error in showRNASingleStrandProperties()", e, 1);
			}
		}
	});
	formatArcedPanel.add(this.getNewViewButtonPanel(flipArc_Bt));

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	JLabel label = this.getNewViewLabel("Center dist");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	arcDistTF = new JTextField(
		StringUtil.roundStrVal(getCurrentWorkSingleStrand().getCurrentCenterPtDistance(), 2), 4);
	arcDistTF.setFont(smBtFont);
	tmpPanel.add(arcDistTF);
	arcDistTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			/* LEAVE FOR AWHILE:
			if (formatStraightRB.isSelected())
			{
				alert("Need to select 'Format Arced'\nin Properties menu");
				return;
			}
			*/
			try
			{
				getCurrentWorkSingleStrand().formatArc(
					Double.parseDouble(arcDistTF.getText()));
				complexSceneView.renderDrawObjectView();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid radius value: " + arcDistTF.getText());
				return;
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	arcDistDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(arcDistDownBt);
	
	arcDistDownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				/* LEAVE FOR AWHILE:
				if (formatStraightRB.isSelected())
				{
					alert("Need to select 'Format Arced'\nin Properties menu");
					return;
				}
				*/
				mouseIsPressed = true;
				setNewArcDist(false);
				// THIS causes properties gui to mess up
				// (new ArcCenterDistBtPressed(false)).start();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}

		public void
		mouseReleased(MouseEvent event)
		{
			mouseIsPressed = false;
		}
	});

	arcDistUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(arcDistUpBt);

	arcDistUpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				/* LEAVE FOR AWHILE:
				if (formatStraightRB.isSelected())
				{
					alert("Need to select 'Format Arced'\nin Properties menu");
					return;
				}
				*/
				mouseIsPressed = true;
				setNewArcDist(true);
				// THIS causes properties gui to mess up
				// (new ArcCenterDistBtPressed(true)).start();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}

		public void
		mouseReleased(MouseEvent event)
		{
			mouseIsPressed = false;
		}
	});
	formatArcedPanel.add(tmpPanel);

	mainPanel.add(formatArcedPanel);

	JPanel formatStraightPanel = new JPanel(new GridLayout(5, 1), true);
	formatStraightPanel.setBackground(complexSceneView.guiBGColor);
	formatStraightPanel.setForeground(Color.black);
	formatStraightPanel.setBorder(new TitledBorder(this.beveledBorder,
		"Format Straight:"));

	/* LEAVE FOR AWHILE:
	formatStraightRB = new JRadioButton();
	formatStraightRB.setSelected(getCurrentWorkSingleStrand().isStraightLine());
	formatTypeGroupRBs.add(formatStraightRB);
	formatStraightPanel.add(this.getNewViewButtonPanel(formatStraightRB, "Format Straight"));
	*/

	JButton readjustDelLineBt = this.getNewViewButton("Readjust Nuc Line");
	readjustDelLineBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				/* LEAVE FOR AWHILE:
				if (formatArcedRB.isSelected())
				{
					alert("Need to select 'Format Straight'\nin Properties menu");
					return;
				}
				*/
				RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();
				editErase(singleStrand, ComplexDefines.InRNASingleStrand);
				singleStrand.formatDelineatedNucLine();
				editRedraw(singleStrand, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Error in showRNASingleStrandProperties()", e, 1);
			}
		}
	});
	formatStraightPanel.add(this.getNewViewButtonPanel(readjustDelLineBt));

	ActionListener singleStrandAngleNucDist_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double angle = 0.0;
			try
			{
				angle = Double.parseDouble(singleStrand_Angle_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + singleStrand_Angle_TF.getText());
				return;
			}

			double nucDist = 0.0;
			try
			{
				nucDist = Double.parseDouble(singleStrand_NucDist_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid nuc distance value: " + singleStrand_NucDist_TF.getText());
				return;
			}

			try
			{
				RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();
				int sveLinePartition = singleStrand.getLinePartition();
				singleStrand.setLinePartition(MathDefines.LINE_PARTITION_HEAD);
				boolean check = checkNucLineConstraints();
				if (check)
				{
					editErase(singleStrand, ComplexDefines.InRNASingleStrand);
					singleStrand.formatNucLineFromAngleAndNucDistance(angle, nucDist);
					// editRedraw(singleStrand, sceneImgG2); // FOR some reason offsets until render
					complexSceneView.renderDrawObjectView();
				}
				singleStrand.setLinePartition(sveLinePartition);
				singleStrand_Angle_TF.setText(StringUtil.roundStrVal(singleStrand.getDelineatedAngle(), 2));
				singleStrand_NucDist_TF.setText(
					StringUtil.roundStrVal(singleStrand.getFivePrimeDelineateNuc2D().getPoint2D().distance(
						singleStrand.getFivePrimeDelineateNuc2D().nextNuc2D().getPoint2D()), 2));
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyAngleBt:", e, 101);
			}
		}
	};

	RNASingleStrand2D singleStrand = this.getCurrentWorkSingleStrand();
	tmpPanel = this.getNewFlowLeftPanel();
	singleStrand_Angle_TF = new JTextField(4);
	singleStrand_Angle_TF.setText(StringUtil.roundStrVal(singleStrand.getDelineatedAngle(), 2));
	singleStrand_Angle_TF.addActionListener(singleStrandAngleNucDist_AL);
	tmpPanel.add(this.getNewViewLabel("5'->3' Angle: "));
	tmpPanel.add(singleStrand_Angle_TF);

	JButton applySingleStrandAngleBt = this.getNewViewImgPlainButton();
	applySingleStrandAngleBt.addActionListener(singleStrandAngleNucDist_AL);
	tmpPanel.add(applySingleStrandAngleBt);
	formatStraightPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	singleStrand_NucDist_TF = new JTextField(4);
	singleStrand_NucDist_TF.setText(
		StringUtil.roundStrVal(singleStrand.getFivePrimeDelineateNuc2D().getPoint2D().distance(
			singleStrand.getFivePrimeDelineateNuc2D().nextNuc2D().getPoint2D()), 2));
	singleStrand_NucDist_TF.addActionListener(singleStrandAngleNucDist_AL);
	tmpPanel.add(this.getNewViewLabel("Nuc->Nuc+1 distance: "));
	tmpPanel.add(singleStrand_NucDist_TF);

	JButton applySingleStrandNucDistBt = this.getNewViewImgPlainButton();
	applySingleStrandNucDistBt.addActionListener(singleStrandAngleNucDist_AL);
	tmpPanel.add(applySingleStrandNucDistBt);
	formatStraightPanel.add(tmpPanel);

	ButtonGroup lineEndPtRBGroup = new ButtonGroup();
	tmpPanel = this.getNewFlowLeftPanel();

	lineEndPt5PRB = this.getNewViewRadioButton("5'");
	lineEndPt5PRB.setSelected(false);
	tmpPanel.add(lineEndPt5PRB);
	lineEndPtRBGroup.add(lineEndPt5PRB);
	lineEndPt5PRB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	lineEndPtMidRB = this.getNewViewRadioButton("mid");
	lineEndPtMidRB.setSelected(true);
	tmpPanel.add(lineEndPtMidRB);
	lineEndPtRBGroup.add(lineEndPtMidRB);
	lineEndPtMidRB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	lineEndPt3PRB = this.getNewViewRadioButton("3'");
	lineEndPt3PRB.setSelected(false);
	tmpPanel.add(lineEndPt3PRB);
	lineEndPtRBGroup.add(lineEndPt3PRB);
	lineEndPt3PRB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	formatStraightPanel.add(tmpPanel);

	MouseAdapter moveDelNuc_MA = new MouseAdapter()
	{
		// NEED to make this into a thread like DrawObjectPanel threads:
		public void
		mousePressed(MouseEvent evt)
		{
			if (getCurrentWorkSingleStrand() == null)
				return;
			mouseIsPressed = true;
			/* LEAVE FOR AWHILE:
			if (!formatStraightRB.isSelected())
			{
				alert("Need to select 'Format Straight'\nin Properties menu");
				return;
			}
			*/
			Component mouseBt = evt.getComponent();
			RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();
			try
			{
				if (lineEndPtMidRB.isSelected())
					singleStrand.setLinePartition(MathDefines.LINE_PARTITION_MID);
				else if (lineEndPt5PRB.isSelected())
					singleStrand.setLinePartition(MathDefines.LINE_PARTITION_TAIL);
				else if (lineEndPt3PRB.isSelected())
					singleStrand.setLinePartition(MathDefines.LINE_PARTITION_HEAD);

				if (!checkNucLineConstraints())
					return;

				editErase(singleStrand, ComplexDefines.InRNASingleStrand);
				if (mouseBt == moveDelNucLeftBt)
				{
					singleStrand.formatEditNucLine(moveSingleStrandInc, 0.0, true);
				}
				else if (mouseBt == moveDelNucRightBt)
				{
					singleStrand.formatEditNucLine(-moveSingleStrandInc, 0.0, true);
				}
				else if (mouseBt == moveDelNucUpBt)
				{
					singleStrand.formatEditNucLine(0.0, -moveSingleStrandInc, true);
				}
				else if (mouseBt == moveDelNucDownBt)
				{
					singleStrand.formatEditNucLine(0.0, moveSingleStrandInc, true);
				}
				animateEdit(singleStrand);
				
				/* SAVE for if ever figure out how to run smoothly without redrawing
				 * this gui weirdly:
				mouseIsPressed = true;
				if (lineEndPtMidRB.isSelected())
					(new DelineateNucLineTransform((JButton)mouseBt, MathDefines.LINE_PARTITION_TAIL)).start();
				else if (lineEndPt5PRB.isSelected())
					(new DelineateNucLineTransform((JButton)mouseBt, MathDefines.LINE_PARTITION_MID)).start();
				else if (lineEndPt3PRB.isSelected())
					(new DelineateNucLineTransform((JButton)mouseBt, MathDefines.LINE_PARTITION_HEAD)).start();
				*/
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}

		public void
		mouseReleased(MouseEvent event)
		{
			mouseIsPressed = false;
		}
	};

	tmpPanel = this.getNewFlowLeftPanel();

	moveDelNucLeftBt = this.getNewViewImgLeftButton();
	tmpPanel.add(moveDelNucLeftBt);
	moveDelNucLeftBt.addMouseListener(moveDelNuc_MA);

	moveDelNucRightBt = this.getNewViewImgRightButton();
	tmpPanel.add(moveDelNucRightBt);
	moveDelNucRightBt.addMouseListener(moveDelNuc_MA);

	moveDelNucUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(moveDelNucUpBt);
	moveDelNucUpBt.addMouseListener(moveDelNuc_MA);

	moveDelNucDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(moveDelNucDownBt);
	moveDelNucDownBt.addMouseListener(moveDelNuc_MA);

	formatStraightPanel.add(tmpPanel);

	mainPanel.add(formatStraightPanel);

	this.addEditProperties(mainPanel);

	double lineLength = this.getCurrentWorkSingleStrand().getNucLabelLineLength(
		this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	return (mainPanel);
}

private void
setNewArcDist(boolean arcDistDirection)
throws Exception
{
	double arcDistInc = 0.4;

	RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();

	/* LEAVE FOR AWHILE:
	singleStrand.setFormattedArced(!(((formatStraightRB != null) &&
		(formatStraightRB.isSelected())) || singleStrand.isStraightLine()));
	*/

	editErase(singleStrand, ComplexDefines.InRNASingleStrand);
	updateEditColor(singleStrand, complexSceneView.moveableEditingColor, sceneImgG2);

	double currentArcDist = singleStrand.getCurrentCenterPtDistance();
	if ((!arcDistDirection) &&
		(singleStrand.getCurrentSecantToArcDistance() < 4.0))
		return;
	if (arcDistDirection)
		singleStrand.formatArc(currentArcDist + arcDistInc);
	else
		singleStrand.formatArc(currentArcDist - arcDistInc);
	arcDistTF.setText(StringUtil.roundStrVal(
		singleStrand.getCurrentCenterPtDistance(), 2));
	singleStrand.draw(sceneImgG2, null);
}

/* SAVE for if ever figure out how to run smoothly without redrawing
 * this gui weirdly:
class DelineateNucLineTransform
extends Thread
{
private double transformInc = 0.2;
private int transformDirection = MathDefines.LINE_PARTITION_MID;
JButton mouseBt = null;

DelineateNucLineTransform(JButton bt, int direction)
{
	mouseIsPressed = true;
	transformDirection = direction;
	mouseBt = bt;
}

public void
run()
{
	try
	{
		RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();
		singleStrand.setLinePartition(transformDirection);
		if (!checkNucLineConstraints())
			return;
		setNewDelineateNucLineTransform(singleStrand);
		try
		{
			this.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
		while (true)
		{
			this.yield();
			if (!mouseIsPressed)
				break;
			setNewDelineateNucLineTransform(singleStrand);
		}
	}
	catch (Exception e)
	{
		complexSceneView.handleException(e, 1);
	}
}

private void
setNewDelineateNucLineTransform(RNASingleStrand2D singleStrand)
throws Exception
{
	editErase(singleStrand, ComplexDefines.InRNASingleStrand);
	if (mouseBt == moveDelNucLeftBt)
	{
		singleStrand.formatEditNucLine(moveSingleStrandInc, 0.0, true);
	}
	else if (mouseBt == moveDelNucRightBt)
	{
		singleStrand.formatEditNucLine(-moveSingleStrandInc, 0.0, true);
	}
	else if (mouseBt == moveDelNucUpBt)
	{
		singleStrand.formatEditNucLine(0.0, -moveSingleStrandInc, true);
	}
	else if (mouseBt == moveDelNucDownBt)
	{
		singleStrand.formatEditNucLine(0.0, moveSingleStrandInc, true);
	}
	animateEdit(singleStrand);
}
} // end inner class
*/

// THIS causes properties gui to mess up
// Try putting on to arrow keys
/*
class ArcCenterDistBtPressed
extends Thread
{
private double arcDistInc = 0.4;
private boolean arcDistDirection = false;

ArcCenterDistBtPressed(boolean direction)
{
	mouseIsPressed = true;
	arcDistDirection = direction;
}

public void
run()
{
	setNewArcDist();
	try
	{
		this.sleep(500);
	}
	catch (InterruptedException e)
	{
	}
	while (true)
	{
		this.yield();
		if (!mouseIsPressed)
			break;
		setNewArcDist();
	}
}

private void
setNewArcDist()
{
	try
	{
	  	RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();

		singleStrand.setFormattedArced(!(((formatStraightRB != null) &&
			(formatStraightRB.isSelected())) || singleStrand.isStraightLine()));

		editErase(singleStrand, ComplexDefines.InRNASingleStrand);
		updateEditColor(singleStrand, complexSceneView.moveableEditingColor, sceneImgG2);

		double currentArcDist = singleStrand.getCurrentCenterPtDistance();
		if ((!arcDistDirection) &&
			(singleStrand.getCurrentSecantToArcDistance() < 4.0))
			return;
		if (arcDistDirection)
			singleStrand.formatArc(currentArcDist + arcDistInc);
		else
			singleStrand.formatArc(currentArcDist - arcDistInc);
		arcDistTF.setText(StringUtil.roundStrVal(
			singleStrand.getCurrentCenterPtDistance(), 2));
		singleStrand.draw(sceneImgG2, null);
	}
	catch (Exception e)
	{
		complexSceneView.handleException(e, 1);
	}
}
}
*/

ActionListener changeBasePairDistanceActionListener = new ActionListener()
{
	public void
	actionPerformed(ActionEvent evt)
	{
		double bpDistance = 0.0;
		try
		{
			bpDistance = Double.parseDouble(bpDistanceTF.getText());
		}
		catch (NumberFormatException e)
		{
			alert("Invalid position values: " +
				drawObject_Center_X_TF.getText() + " " + drawObject_Center_X_TF.getText());
			return;
		}

		try
		{
			redrawBasePairDistance(bpDistance);
		}
		catch (Exception e)
		{
			complexSceneView.handleException(
				"Exception in ComplexSceneEdit.showSSDataProperties:", e, 101);
		}
	}
};

MouseAdapter changeBasePairDistanceMouseListener = new MouseAdapter()
{
	public void
	mousePressed(MouseEvent evt)
	{
		Component mouseBt = evt.getComponent();
		try
		{
			if (mouseBt == applyChangeBasePairDistanceLeftBt)
			{
				redrawBasePairDistance(getCurrentWorkBasePair().get5PBasePairRay().length() - 1.0);
			}
			else if (mouseBt == applyChangeBasePairDistanceRightBt)
			{
				redrawBasePairDistance(getCurrentWorkBasePair().get5PBasePairRay().length() + 1.0);
			}
		}
		catch (Exception e)
		{
			complexSceneView.handleException(e, 1);
		}
	}
};

JTextField bpAngleTF = null;

public Box
showRNABasePairProperties()
throws Exception
{
	Box mainPanel = super.showRNABasePairProperties();
	JPanel tmpPanel = null;

	mainPanel.add(this.getNewLabelPanel("Distance between basepairs: " +
		StringUtil.roundStrVal(this.getCurrentWorkBasePair().get5PBasePairRay().length(), 2)));
	
	// USE single base pair helix instead
	/*
	ActionListener bpAngleActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			if (getCurrentWorkBasePair() == null)
				return;
			double newHelixAngle = 0.0;
			try
			{
				newHelixAngle = Double.parseDouble(bpAngleTF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + bpAngleTF.getText());
				return;
			}

			Graphics2D g2 = complexSceneView.getCurrentGraphics2D();

			// NEED to fix up for multi strand basepair:
			g2.setTransform(getCurrentWorkBasePair().getFivePrimeNuc2D().getParentG2Transform());

			try
			{
				getCurrentWorkBasePair().erase(g2);

				getCurrentWorkBasePair().format(newHelixAngle);

				editRedraw(getCurrentWorkBasePair(), g2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("angle: ", smBtFont));
	bpAngleTF = new JTextField(6);
	bpAngleTF.setText("" + StringUtil.roundStrVal(this.getCurrentWorkBasePair().getAngle(), 2));
	bpAngleTF.addActionListener(bpAngleActionListener);
	tmpPanel.add(bpAngleTF);

	JButton applyAngleBt = this.getNewViewImgPlainButton();
	applyAngleBt.addActionListener(bpAngleActionListener);
	tmpPanel.add(applyAngleBt);
	mainPanel.add(tmpPanel);
	*/

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

/*************************************************************************************/

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("BP distance: ", smBtFont));
	bpDistanceTF = new JTextField(4);
	bpDistanceTF.setText(StringUtil.roundStrVal(this.getCurrentWorkBasePair().distance(), 2));
	bpDistanceTF.addActionListener(changeBasePairDistanceActionListener);
	tmpPanel.add(bpDistanceTF);

	JButton applyBPDistanceBt = this.getNewViewImgPlainButton();
	applyBPDistanceBt.addActionListener(changeBasePairDistanceActionListener);
	tmpPanel.add(applyBPDistanceBt);

	applyChangeBasePairDistanceLeftBt = this.getNewViewImgLeftButton();
	applyChangeBasePairDistanceLeftBt.addMouseListener(changeBasePairDistanceMouseListener);
	tmpPanel.add(applyChangeBasePairDistanceLeftBt);

	applyChangeBasePairDistanceRightBt = this.getNewViewImgRightButton();
	applyChangeBasePairDistanceRightBt.addMouseListener(changeBasePairDistanceMouseListener);
	tmpPanel.add(applyChangeBasePairDistanceRightBt);

	mainPanel.add(tmpPanel);

/*************************************************************************************/

	this.addEditProperties(mainPanel);

	double lineLength = this.getCurrentWorkBasePair().getNucLabelLineLength(this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	return (mainPanel);
}

JTextField rnaHelixAngleTF = null;

public Box
showRNAHelixProperties()
throws Exception
{
	Box mainPanel = super.showRNAHelixProperties();
	JPanel tmpPanel = null;

	RNAHelix2D helix = this.getCurrentWorkHelix();

	mainPanel.add(this.getNewLabelPanel("ave bp distance: " +
		StringUtil.roundStrVal(helix.averageRegularBasePairDistance(), 2)));
	if (helix.hasMisMatchedBasePairs())
		mainPanel.add(this.getNewLabelPanel("ave mismatch bp distance: " +
			StringUtil.roundStrVal(helix.averageMisMatchBasePairDistance(), 2)));
	if (!helix.isSingleBasePairHelix())
		mainPanel.add(this.getNewLabelPanel("nuc->nuc distance: " +
			StringUtil.roundStrVal(helix.averageNucToNucDist(), 2)));
	
	ActionListener rnaHelixAngleActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			if (getCurrentWorkHelix() == null)
				return;
			double newHelixAngle = 0.0;
			try
			{
				newHelixAngle = Double.parseDouble(rnaHelixAngleTF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + rnaHelixAngleTF.getText());
				return;
			}

			try
			{
				RNAHelix2D helix = getCurrentWorkHelix();
				editErase(helix, ComplexDefines.InRNAHelix);
				Point2D midPt = helix.getFivePrimeMidPt();
				AffineTransform affTrans = new AffineTransform();
				affTrans.rotate(
					MathDefines.DegToRad * (newHelixAngle - helix.getAngle()),
						midPt.getX(), midPt.getY());
				helix.transform(affTrans);
				editRedraw(helix, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("angle: ", smBtFont));
	rnaHelixAngleTF = new JTextField(6);
	rnaHelixAngleTF.setText("" + StringUtil.roundStrVal(helix.getAngle(), 2));
	rnaHelixAngleTF.addActionListener(rnaHelixAngleActionListener);
	tmpPanel.add(rnaHelixAngleTF);

	JButton applyAngleBt = this.getNewViewImgPlainButton();
	applyAngleBt.addActionListener(rnaHelixAngleActionListener);
	tmpPanel.add(applyAngleBt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	JButton flipHelix_Bt = this.getNewViewButton("Flip");
	flipHelix_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				RNAHelix2D helix = getCurrentWorkHelix();
				editErase(helix, ComplexDefines.InRNAHelix);

				if (helix.isSingleBasePairHelix())
				{
					// just exchange x,y's between bps
					
					Point2D svePt = new Point2D.Double(
						helix.getFivePrimeStartNuc2D().getPoint2D().getX(),
						helix.getFivePrimeStartNuc2D().getPoint2D().getY());
					helix.getFivePrimeStartNuc2D().setXY(helix.getThreePrimeEndNuc2D().getX(),
						helix.getThreePrimeEndNuc2D().getY());
					helix.getFivePrimeEndNuc2D().setXY(helix.getThreePrimeEndNuc2D().getX(),
						helix.getThreePrimeEndNuc2D().getY());
					helix.getThreePrimeStartNuc2D().setXY(svePt.getX(), svePt.getY());
					helix.getThreePrimeEndNuc2D().setXY(svePt.getX(), svePt.getY());
				}
				else
				{
					double sveAngle = helix.getAngle();
					double helixAngle = MathDefines.DegToRad * sveAngle;
					Point2D midPt = helix.getFivePrimeMidPt();

					AffineTransform affTrans = AffineTransform.getTranslateInstance(
						midPt.getX(), midPt.getY());
					affTrans.scale(1.0, -1.0);  // flip horizontally
					affTrans.rotate(-helixAngle);
					affTrans.translate(-midPt.getX(), -midPt.getY());
					helix.transform(affTrans);

					affTrans = new AffineTransform();
					affTrans.rotate(
						MathDefines.DegToRad * (sveAngle - helix.getAngle()),
							midPt.getX(), midPt.getY());
					helix.transform(affTrans);
				}
				editRedraw(helix, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	tmpPanel.add(flipHelix_Bt);
	mainPanel.add(tmpPanel);

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	this.addEditProperties(mainPanel);
	double lineLength = this.getCurrentWorkHelix().getNucLabelLineLength(this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	return (mainPanel);
}

JTextField rnaStackedHelixAngleTF = null;

public Box
showRNAStackedHelixProperties()
throws Exception
{
	Box mainPanel = super.showRNAStackedHelixProperties();
	JPanel tmpPanel = null;

	ActionListener rnaStackedHelixAngleActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			if (getCurrentWorkStackedHelix() == null)
				return;
			double newHelixAngle = 0.0;
			try
			{
				newHelixAngle = Double.parseDouble(rnaStackedHelixAngleTF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + rnaStackedHelixAngleTF.getText());
				return;
			}

			try
			{
				RNAStackedHelix2D helicalRun = getCurrentWorkStackedHelix();
				editErase(helicalRun, ComplexDefines.InRNAHelicalRun);

				Point2D midPt = helicalRun.getStartHelix2D().getFivePrimeMidPt();
				AffineTransform affTrans = new AffineTransform();
				affTrans.rotate(
					MathDefines.DegToRad * (newHelixAngle - helicalRun.getAngle()),
						midPt.getX(), midPt.getY());
				helicalRun.transform(affTrans);

				editRedraw(getCurrentWorkStackedHelix(), sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("angle: ", smBtFont));
	rnaStackedHelixAngleTF = new JTextField(6);
	rnaStackedHelixAngleTF.addActionListener(rnaStackedHelixAngleActionListener);
	rnaStackedHelixAngleTF.setText("" + StringUtil.roundStrVal(this.getCurrentWorkStackedHelix().getAngle(), 2));
	tmpPanel.add(rnaStackedHelixAngleTF);

	JButton applyAngleBt = this.getNewViewImgPlainButton();
	applyAngleBt.addActionListener(rnaStackedHelixAngleActionListener);
	tmpPanel.add(applyAngleBt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	JButton flipHelix_Bt = this.getNewViewButton("Flip");
	flipHelix_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				RNAStackedHelix2D helicalRun = getCurrentWorkStackedHelix();
				editErase(helicalRun, ComplexDefines.InRNAHelicalRun);

				double sveAngle = helicalRun.getAngle();
				double helicalRunAngle = MathDefines.DegToRad * sveAngle;
				Point2D midPt = helicalRun.getFivePrimeMidPt();

				AffineTransform affTrans = AffineTransform.getTranslateInstance(
					midPt.getX(), midPt.getY());
				affTrans.scale(1.0, -1.0);  // flip horizontally
				affTrans.rotate(-helicalRunAngle);
				affTrans.translate(-midPt.getX(), -midPt.getY());
				helicalRun.transform(affTrans);

				affTrans = new AffineTransform();
				affTrans.rotate(
					MathDefines.DegToRad * (sveAngle - helicalRun.getAngle()),
						midPt.getX(), midPt.getY());
				helicalRun.transform(affTrans);
				
				editRedraw(helicalRun, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	tmpPanel.add(flipHelix_Bt);
	mainPanel.add(tmpPanel);

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	this.addEditProperties(mainPanel);
	double lineLength = this.getCurrentWorkStackedHelix().getNucLabelLineLength(this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	return (mainPanel);
}

JTextField rnaSubDomainAngleTF = null;

public Box
showRNASubDomainProperties()
throws Exception
{
	Box mainPanel = super.showRNASubDomainProperties();
	JPanel tmpPanel = null;

	ActionListener rnaSubDomainAngleActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			if (getCurrentWorkSubDomain() == null)
				return;
			double newHelixAngle = 0.0;
			try
			{
				newHelixAngle = Double.parseDouble(rnaSubDomainAngleTF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + rnaSubDomainAngleTF.getText());
				return;
			}

			try
			{
				RNASubDomain2D domain = getCurrentWorkSubDomain();
				editErase(domain, ComplexDefines.InRNASubDomain);

				Point2D midPt = domain.getStartHelix2D().getFivePrimeMidPt();
				AffineTransform affTrans = new AffineTransform();
				affTrans.rotate(
					MathDefines.DegToRad * (newHelixAngle - domain.getAngle()),
						midPt.getX(), midPt.getY());
				domain.transform(affTrans);

				editRedraw(domain, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("angle: ", smBtFont));
	rnaSubDomainAngleTF = new JTextField(6);
	rnaSubDomainAngleTF.addActionListener(rnaSubDomainAngleActionListener);
	rnaSubDomainAngleTF.setText("" + StringUtil.roundStrVal(this.getCurrentWorkSubDomain().getAngle(), 2));
	tmpPanel.add(rnaSubDomainAngleTF);

	JButton applyAngleBt = this.getNewViewImgPlainButton();
	applyAngleBt.addActionListener(rnaSubDomainAngleActionListener);
	tmpPanel.add(applyAngleBt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	JButton flipHelix_Bt = this.getNewViewButton("Flip");
	flipHelix_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				RNASubDomain2D subDomain = getCurrentWorkSubDomain();
				editErase(subDomain, ComplexDefines.InRNASubDomain);

				double sveAngle = subDomain.getAngle();
				double subDomainAngle = MathDefines.DegToRad * sveAngle;
				Point2D midPt = subDomain.getFivePrimeMidPt();

				AffineTransform affTrans = AffineTransform.getTranslateInstance(
					midPt.getX(), midPt.getY());
				affTrans.scale(1.0, -1.0);  // flip horizontally
				affTrans.rotate(-subDomainAngle);
				affTrans.translate(-midPt.getX(), -midPt.getY());
				subDomain.transform(affTrans);

				affTrans = new AffineTransform();
				affTrans.rotate(
					MathDefines.DegToRad * (sveAngle - subDomain.getAngle()),
						midPt.getX(), midPt.getY());
				subDomain.transform(affTrans);
				
				editRedraw(subDomain, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	tmpPanel.add(flipHelix_Bt);
	mainPanel.add(tmpPanel);

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	this.addEditProperties(mainPanel);
	double lineLength = this.getCurrentWorkSubDomain().getNucLabelLineLength(this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	tmpPanel = this.getNewFlowLeftPanel();
	JButton clearSubDomainHiddenNucsBt = this.getNewViewButton("Clear hidden attribute");
	tmpPanel.add(clearSubDomainHiddenNucsBt);
	clearSubDomainHiddenNucsBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkSubDomain().clearHidden();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.clearSubDomainHiddenNucsBt:", e, 101);
			}
		}
	});
	mainPanel.add(tmpPanel);

	return (mainPanel);
}

public Box
showRNANamedGroupProperties()
throws Exception
{
	Box mainPanel = super.showRNANamedGroupProperties();
	JPanel tmpPanel = null;
	RNANamedGroup2D namedGroup = this.getCurrentWorkNamedGroup();

	if (!namedGroup.basePairsOutsideOfCollection())
	{
		mainPanel.add(this.changeDrawObjectXPanel());
		mainPanel.add(this.changeDrawObjectYPanel());
	}

	this.addEditProperties(mainPanel);
	double lineLength = namedGroup.getNucLabelLineLength(this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	JButton changeToSStr_Bt = this.getNewViewButton("Change to rna strand");
	mainPanel.add(this.getNewViewButtonLeftPanel(changeToSStr_Bt));
	changeToSStr_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				complexSceneView.splitOffSStr(getCurrentWorkNamedGroup());
				complexSceneView.resetPickTreePanels();
				complexSceneView.renderDrawObjectView();
			}
			catch (ComplexException ce)
			{
				if (ce.getErrorCode() == ComplexDefines.COMPLEX_SCENE_SSTR_SPLIT_ERROR)
					alert(ce.getExceptionMsg() + "\n" + ce.getErrorMsg() + "\n" + "Try unsetting basepairing and reseting after splitting off rna strand");
				else
					complexSceneView.handleException(ce, 1);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	return (mainPanel);
}

public Box
showRNAColorUnitProperties()
throws Exception
{
	Box mainPanel = super.showRNAColorUnitProperties();
	JPanel tmpPanel = null;

	if (!this.getCurrentWorkColorUnit().basePairsOutsideOfCollection())
	{
		mainPanel.add(this.changeDrawObjectXPanel());
		mainPanel.add(this.changeDrawObjectYPanel());
	}

	this.addEditProperties(mainPanel);
	double lineLength = this.getCurrentWorkColorUnit().getNucLabelLineLength(this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	return (mainPanel);
}

private static DrawCircleObject cycleCircle = null;
private RNACycle2D resetCycle = null;
private RNAHelix2D levelRefHelix = null;
private SSData2D resetSStr = null;
private JRadioButton resetCycleHelicesAnglesRB = null;

public Box
showRNACycleProperties()
throws Exception
{
	Box mainPanel = super.showRNACycleProperties();
	JPanel tmpPanel = null;

	resetCycle = getCurrentWorkCycle();
	if (resetCycle == null)
		return (null);
	resetSStr = (SSData2D)resetCycle.getParentCollection();
	levelRefHelix = resetCycle.getRefHelix2D();

	resetSStr.removeLabel(cycleCircle);
	cycleCircle = resetCycle.getCycleCircle();
	if (cycleCircle != null)
		cycleCircle.setIsPickable(false);

	// NEED to add this circle to a list indexed by a ref nuc in
	// level. not sure whether to use Vector and go through list
	// to see if that ref nuc is in Vector or to use a hashtable
	// which indexs circleObj by ref Nuc.

	// resetSStr.addLabel(cycleCircle);

	/* Not ready yet:
	JButton resetArcsBt = this.getNewViewButton("Reset Arcs");
	resetArcsBt.setActionCommand("reset_arcs");
	mainPanel.add(this.getNewViewButtonPanel(resetArcsBt));
	resetArcsBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			debug("resetCycle: " + resetCycle);
			alert("Not implemented yet");
		}
	});
	*/

	showCycleRB = new JRadioButton();
	showCycleRB.setSelected(false);
	mainPanel.add(this.getNewViewButtonPanel(showCycleRB, "Show Cycle Circle"));
	showCycleRB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (resetCycle == null)
				{
					alert("Need to repick a Cycle");
					return;
				}
				resetSStr.removeLabel(cycleCircle);
				if (showCycleRB.isSelected())
				{
					if (cycleCircle == null)
					{
						alert("Currently not implemented for cycle level 0");
						return;
					}
					resetSStr.addLabel(cycleCircle);
				}
				complexSceneView.runRenderBt();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	JButton resetCycleBt = this.getNewViewButton("Reset Cycle");
	resetCycleBt.setActionCommand("reset_cycle");
	mainPanel.add(this.getNewViewButtonPanel(resetCycleBt));
	resetCycleBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (resetCycle == null)
				{
					alert("Need to repick a Cycle");
					return;
				}

				cycleCircle = resetCycle.getCycleCircle();

				if (cycleCircle == null)
				{
					alert("Currently not implemented for cycle level 0");
					return;
				}

				// NEED to add this circle to a list indexed by a ref nuc in
				// level. not sure whether to use Vector and go through list
				// to see if that ref nuc is in Vector or to use a hashtable
				// which indexs circleObj by ref Nuc.

				resetCycle.partialReset(true);
				resetCycle.formatCycle(cycleCircle.getCenterPt(), true, false, false);

				complexSceneView.runRenderBt();
				if (levelRefHelix != null)
					domainRotateCycleAngle_TF.setText("" + StringUtil.roundStrVal(levelRefHelix.getAngle(), 2));
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	ActionListener cycleActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double radius = 0.0;
			try
			{
				radius = Double.parseDouble(cycle_Radius_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid radius value: " + cycle_Radius_TF.getText());
				return;
			}
			try
			{
				redrawCycleRadius(radius);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyRadiusBt:", e, 101);
			}
		}
	};

	MouseAdapter cycleMouseListener = new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent evt)
		{
			Component mouseBt = evt.getComponent();
			try
			{
				if (cycleCircle == null)
				{
					alert("Currently not implemented for cycle level 0");
					return;
				}
				double MIN_CYCLE_RADIUS = 10.0; // arbitrary for now
				double radius = MIN_CYCLE_RADIUS;
				if (mouseBt == cycleRadiusUpBt)
				{
					radius = cycleCircle.getRadius() + 1.0;
				}
				else if (mouseBt == cycleRadiusDownBt)
				{
					radius = cycleCircle.getRadius() - 1.0;
				}
				if (radius < MIN_CYCLE_RADIUS)
					radius = MIN_CYCLE_RADIUS;
				cycle_Radius_TF.setText("" + StringUtil.roundStrVal(radius, 2));
		// Need to reset this when radius changes
		// domainRotateCycleAngle_TF.setText("" + StringUtil.roundStrVal(angle, 2));
				redrawCycleRadius(radius);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	// Box doesn't seem to color background properly
	// Box tmpBox = new Box(BoxLayout.Y_AXIS);
	JPanel tmpBox = new JPanel(new GridLayout(2, 1));
	tmpBox.setBorder(this.beveledBorder);
	tmpBox.setBackground(guiBGColor);
	tmpBox.setForeground(Color.black);
	tmpBox.setBorder(new TitledBorder(complexSceneView.border, "Cycle Radius:"));

	resetCycleHelicesAnglesRB = new JRadioButton();
	resetCycleHelicesAnglesRB.setSelected(true);
	resetCycleHelicesAnglesRB.setText("Reset Helix Angles");
	resetCycleHelicesAnglesRB.setFont(btFont);
	resetCycleHelicesAnglesRB.setForeground(Color.black);
	resetCycleHelicesAnglesRB.setBackground(guiBGColor);
	/* Not ready yet
	tmpBox.add(this.getNewViewButtonPanel(resetCycleHelicesAnglesRB));
	*/

	tmpPanel = this.getNewFlowCenterPanel();
	cycle_Radius_TF = new JTextField(4);
	if (cycleCircle != null)
		cycle_Radius_TF.setText("" + StringUtil.roundStrVal(cycleCircle.getRadius(), 2));
	// tmpPanel.add(this.getNewViewLabel("Radius: "));
	tmpPanel.add(cycle_Radius_TF);
	cycle_Radius_TF.addActionListener(cycleActionListener);

	JButton applyRadiusBt = this.getNewViewImgPlainButton();
	applyRadiusBt.addActionListener(cycleActionListener);
	tmpPanel.add(applyRadiusBt);

	cycleRadiusUpBt = this.getNewViewImgUpButton();
	cycleRadiusUpBt.addMouseListener(cycleMouseListener);
	tmpPanel.add(cycleRadiusUpBt);

	cycleRadiusDownBt = this.getNewViewImgDownButton();
	cycleRadiusDownBt.addMouseListener(cycleMouseListener);
	tmpPanel.add(cycleRadiusDownBt);
	tmpBox.add(tmpPanel);
	mainPanel.add(tmpBox);

	ActionListener domainRotateCycleActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double newDomainCycleAngle = 0.0;
			try
			{
				newDomainCycleAngle = Double.parseDouble(domainRotateCycleAngle_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid radius value: " + domainRotateCycleAngle_TF.getText());
				return;
			}
			try
			{
				redrawCycleDomain(newDomainCycleAngle);
				complexSceneView.runRenderBt();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyRadiusBt:", e, 101);
			}
		}
	};

	MouseAdapter domainRotateCycleMouseListener = new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent evt)
		{
			Component mouseBt = evt.getComponent();
			try
			{
				if (levelRefHelix == null)
						return;
				double inc = 1.0;
				double angle = levelRefHelix.getAngle();
				if (mouseBt == domainRotateCycleUpBt)
					angle += inc;
				else if (mouseBt == domainRotateCycleDownBt)
					angle -= inc;
				if (angle >= 360.0)
					angle -= 360.0;
				if (angle <= 0.0)
					angle += 360.0;
				redrawCycleDomain(angle);
				complexSceneView.runRenderBt();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	if (levelRefHelix != null)
	{
		tmpBox = new JPanel(new GridLayout(2, 1));
		tmpBox.setBorder(this.beveledBorder);
		tmpBox.setBackground(guiBGColor);
		tmpBox.setForeground(Color.black);
		tmpBox.setBorder(new TitledBorder(complexSceneView.border, "Cycle Helix Angle:"));

		tmpPanel = this.getNewFlowCenterPanel();

		double angle = 0.0;
		if (cycleCircle != null)
		{
			/*
			BLine2D circleLine = new BLine2D(
				cycleCircle.getCenterPt(), levelRefHelix.getFivePrimeMidPt());
			double angle = circleLine.angleInXYPlane();
			*/
			angle = new BLine2D(cycleCircle.getCenterPt(),
				levelRefHelix.getFivePrimeMidPt()).angleInXYPlane();
		}
		domainRotateCycleAngle_TF = new JTextField(4);
		domainRotateCycleAngle_TF.setText("" + StringUtil.roundStrVal(angle, 2));
		tmpPanel.add(domainRotateCycleAngle_TF);
		domainRotateCycleAngle_TF.addActionListener(domainRotateCycleActionListener);

		JButton applyHelixRotateBt = this.getNewViewImgPlainButton();
		applyHelixRotateBt.addActionListener(domainRotateCycleActionListener);
		tmpPanel.add(applyHelixRotateBt);

		domainRotateCycleUpBt = this.getNewViewImgUpButton();
		domainRotateCycleUpBt.addMouseListener(domainRotateCycleMouseListener);
		tmpPanel.add(domainRotateCycleUpBt);

		domainRotateCycleDownBt = this.getNewViewImgDownButton();
		domainRotateCycleDownBt.addMouseListener(domainRotateCycleMouseListener);
		tmpPanel.add(domainRotateCycleDownBt);
		tmpBox.add(tmpPanel);
		mainPanel.add(tmpBox);
	}

	JButton hideConnectingSS_Bt = this.getNewViewButton("Hide connecting single strands");
	hideConnectingSS_Bt.setActionCommand("hide_connecting_single_strands");
	mainPanel.add(this.getNewViewButtonPanel(hideConnectingSS_Bt));
	hideConnectingSS_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (resetCycle == null)
				{
					alert("Need to repick a Cycle");
					return;
				}
				if (resetCycle.getCycleHelices() == null) // no structure
				{
					alert("No structure; use a different constraint mode to hide single stranded regions");
					return;
				}
				Vector cycleNucList = resetCycle.getCycleNucs();
				for (int i = 0;i < cycleNucList.size();i++)
				{
					Nuc2D nuc = (Nuc2D)cycleNucList.elementAt(i);
					if (nuc.isSingleStranded())
						nuc.setIsHidden(true);
				}
				complexSceneView.runRenderBt();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	JButton showConnectingSS_Bt = this.getNewViewButton("Show connecting single strands");
	showConnectingSS_Bt.setActionCommand("show_connecting_single_strands");
	mainPanel.add(this.getNewViewButtonPanel(showConnectingSS_Bt));
	showConnectingSS_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (resetCycle == null)
				{
					alert("Need to repick a Cycle");
					return;
				}
				if (resetCycle.getCycleHelices() == null) // no structure
				{
					alert("No structure; use a different constraint mode to show single stranded regions");
					return;
				}

				Vector cycleNucList = resetCycle.getCycleNucs();
				for (int i = 0;i < cycleNucList.size();i++)
				{
					Nuc2D nuc = (Nuc2D)cycleNucList.elementAt(i);
					if (nuc.isSingleStranded())
						nuc.setIsHidden(false);
				}
				complexSceneView.runRenderBt();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	JButton resetSingleStrands_Bt = this.getNewViewButton("Reset single strands");
	resetSingleStrands_Bt.setActionCommand("reset_single_strands");
	mainPanel.add(this.getNewViewButtonPanel(resetSingleStrands_Bt));
	resetSingleStrands_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (resetCycle == null)
				{
					alert("Need to repick a Cycle");
					return;
				}
				if (resetCycle.getCycleHelices() == null) // no structure
				{
					alert("No structure; use a different constraint mode to show single stranded regions");
					return;
				}

				RNAHelix2D currentExitHelix = new RNAHelix2D();
				for (int i = 1;i <= resetCycle.getExitHelicesCount();i++)
				{
					currentExitHelix.set((Nuc2D)resetCycle.getCycleHelices().elementAt(i));
					Nuc2D refNuc = currentExitHelix.getFivePrimeStartNuc2D().lastNuc2D();
					boolean clockWiseFormatted = currentExitHelix.isClockWiseFormatted();
					if ((refNuc != null) && refNuc.isSingleStranded())
						redrawCycleSingleStrandAt(refNuc, clockWiseFormatted);
					// this is redundant in all but last exit helix; NEED to fix:
					refNuc = currentExitHelix.getThreePrimeEndNuc2D().nextNuc2D();
					if ((refNuc != null) && refNuc.isSingleStranded())
						redrawCycleSingleStrandAt(refNuc, clockWiseFormatted);
				}

				complexSceneView.runRenderBt();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});

	tmpPanel = this.getNewFlowLeftPanel();
	JButton clearExtendedSubDomainsNucsBt = this.getNewViewButton("set extended subdomains hidden attribute");
	tmpPanel.add(clearExtendedSubDomainsNucsBt);
	clearExtendedSubDomainsNucsBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkCycle().setCycleHelicesSubDomainHidden();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.clearExtendedSubDomainsNucsBt:", e, 101);
			}
		}
	});
	mainPanel.add(tmpPanel);

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

	if (this.getCurrentWorkListNucs().basePairsOutsideOfCollection())
	{
		alert("This list of nucs base pairs outside of list and\n" +
			"therefore can't be translated");
	}
	else
	{
		mainPanel.add(this.changeDrawObjectXPanel());
		mainPanel.add(this.changeDrawObjectYPanel());
	}

	this.addEditProperties(mainPanel);
	double lineLength = this.getCurrentWorkListNucs().getNucLabelLineLength(this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	return (mainPanel);
}

public Box
showRNASSDataProperties()
throws Exception
{
	Box mainPanel = super.showRNASSDataProperties();

	SSData2D sstr = this.getCurrentWorkSStr();

	ActionListener collectionName_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkSStr().resetName(sstrNameTF.getText().trim());
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	sstrNameTF = new JTextField(8);
	sstrNameTF.setText(sstr.getName());
	sstrNameTF.addActionListener(collectionName_AL);
	tmpPanel.add(this.getNewViewLabel("Name: "));
	tmpPanel.add(sstrNameTF);
	JButton applyBt = this.getNewViewImgPlainButton();
	tmpPanel.add(applyBt);
	applyBt.addActionListener(collectionName_AL);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	JButton flipHelix_Bt = this.getNewViewButton("Flip");
	flipHelix_Bt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				SSData2D sstr = getCurrentWorkSStr();
				editErase(sstr, ComplexDefines.InRNASSData);

				Rectangle2D rect = sstr.getBoundingBox();

				Point2D midPt = new Point2D.Double(
					rect.getX() + rect.getWidth()/2.0,
					-rect.getY() - rect.getHeight()/2.0);

				AffineTransform affTrans = AffineTransform.getTranslateInstance(
					midPt.getX(), midPt.getY());
				affTrans.scale(1.0, -1.0);  // flip horizontally
				affTrans.translate(-midPt.getX(), -midPt.getY());
				sstr.transform(affTrans);

				editRedraw(sstr, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	tmpPanel.add(flipHelix_Bt);
	mainPanel.add(tmpPanel);

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	this.addEditProperties(mainPanel);

	double lineLength = this.getCurrentWorkSStr().getNucLabelLineLength(this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	JButton reassociateBt = this.getNewViewButton("Reassociate");
	reassociateBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			Object[] message = new Object[3];
			message[1] = new JLabel("Associate with: " + "pickmode here");
			// message[1] = new JLabel("Currently can't overwrite existing file");
			// message[2] = new JLabel("Please list a unique file to write to");

			String[] options =
			{
				"Yes",
				"Cancel"
			};
			int result = JOptionPane.showOptionDialog(
				complexSceneView.frameParent,  // the parent that the dialog blocks
				message,  // the dialog message array
				"Reassociate Label", // the title of the dialog window
				JOptionPane.OK_CANCEL_OPTION, // option type
				JOptionPane.PLAIN_MESSAGE, // message type
				null, // optional icon, use null to use the default icon
				options, // options string array, will be made into buttons
				options[0] // option that should be made into a default button
				);

			switch (result)
			{
			  case 0: // yes
				JOptionPane.showMessageDialog(complexSceneView.frameParent, "Made it 0");
				break;
			  case 1: // no
				JOptionPane.showMessageDialog(complexSceneView.frameParent, "Made it 1");
				break;
			  default:
				break;
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(reassociateBt));

	/* NOT SURE what this does; maybe centers drawObject?
	JButton centerBt = new JButton("Center Label");
	centerBt.setFont(new Font("Dialog", Font.BOLD, 12));
	centerBt.setBackground(guiBGColor);
	centerBt.setForeground(Color.black);
	centerPanel.add(centerBt);
	centerBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentDrawObject().setX(0.0);
				getCurrentDrawObject().setY(0.0);
				getCurrentDrawObject().update(
					complexSceneView.getCurrentGraphics2D());
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	*/

	JButton showBPErrorsBt = this.getNewViewButton("Show BasePair Errors");
	showBPErrorsBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				JInternalFrame bpErrorsFrame = complexSceneView.getBasicInternalFrame(
					propertiesFrameStartX + 40, propertiesFrameStartY + 40,
					propertiesFrameWidth, propertiesFrameHeight);
				complexSceneView.addInternalFrame(bpErrorsFrame);
				bpErrorsFrame.setTitle("BP Errors");
				JTextArea bpErrorsTA = new JTextArea();

				SSData2D sstr = getCurrentWorkSStr();
				RNABasePair2D basePair = new RNABasePair2D();
				int bpErrCount = 0;
				int precision = 1;
				for (int nucID = 1;nucID <= sstr.getNucCount();nucID++)
				{
					Nuc2D nuc = sstr.getNuc2DAt(nucID);
					if (nuc == null)
						continue;
					if (!nuc.isFivePrimeBasePair())
						continue;
					basePair.set(nuc);

					if (basePair.isCanonical() &&
						!MathUtil.precisionEquality(basePair.length(), ComplexDefines.RNA_BASEPAIR_DISTANCE, precision))
					{
						bpErrCount++;
						bpErrorsTA.append(nuc.getID() + " " + basePair.length() + "\n");
					}
					else if (basePair.isMisMatch()  &&
						!MathUtil.precisionEquality(basePair.length(), ComplexDefines.RNA_MISMATCH_BASEPAIR_DISTANCE, precision))
					{
						bpErrCount++;
						bpErrorsTA.append(nuc.getID() + " " + basePair.length() + "\n");
					}
					else if (basePair.isWobble()  &&
						!MathUtil.precisionEquality(basePair.length(), ComplexDefines.RNA_BASEPAIR_DISTANCE, precision))
					{
						bpErrCount++;
						bpErrorsTA.append(nuc.getID() + " " + basePair.length() + "\n");
					}
					else if (basePair.isWeak()  &&
						!MathUtil.precisionEquality(basePair.length(), ComplexDefines.RNA_BASEPAIR_DISTANCE, precision))
					{
						bpErrCount++;
						bpErrorsTA.append(nuc.getID() + " " + basePair.length() + "\n");
					}
				}

				bpErrorsTA.append("BP ERR COUNT: " + bpErrCount);

				complexSceneView.updateBasicInternalFrame("BP Errors", bpErrorsTA,
					bpErrorsFrame);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Error in showLabelsOnlyProperties()", e, 1);
			}
		}
	});

	mainPanel.add(this.getNewViewButtonPanel(showBPErrorsBt));

	JButton centerAtOriginBt = this.getNewViewButton("Center At Origin");
	centerAtOriginBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkSStr().centerAtOrigin();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(centerAtOriginBt));

	return (mainPanel);
}

public Box
showComplexProperties()
throws Exception
{
	Box mainPanel = super.showComplexProperties();

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	complexNameTF = new JTextField(12);
	complexNameTF.setText(this.getCurrentWorkRNASSComplex().getName());
	complexNameTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkRNASSComplex().setName(complexNameTF.getText().trim());
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	tmpPanel.add(this.getNewViewLabel("Name: "));
	tmpPanel.add(complexNameTF);
	mainPanel.add(tmpPanel);

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	this.addEditProperties(mainPanel);
	// really need to go through list of sstrs and first look at current one
	// and then iterate through the rest of them.
	for (int childNodeID = 0;childNodeID < this.getCurrentWorkRNASSComplex().getItemCount();childNodeID++)
	{
		Object obj = (Object)this.getCurrentWorkRNASSComplex().getItemAt(childNodeID);
		if (obj == null)
			continue;
		if (obj instanceof SSData2D)
		{
			SSData2D sstr = (SSData2D)obj;
			// this will return the first one found in the first sstr
			double lineLength = sstr.getNucLabelLineLength(1);
			if (lineLength <= 0.0)
				lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
			nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
			nucNumberLabelStartID_TF.setText("1");
		}
	}

	return (mainPanel);
}

public Box
showLabelsOnlyProperties()
throws Exception
{
	if (this.getCurrentWorkLabel() instanceof DrawStringObject)
		return (this.showStringLabelProperties());
	if (this.getCurrentWorkLabel() instanceof DrawCircleObject)
		return (this.showCircleLabelProperties());
	if (this.getCurrentWorkLabel() instanceof DrawTriangleObject)
		return (this.showTriangleLabelProperties());
	if (this.getCurrentWorkLabel() instanceof DrawParallelogramObject)
		return (this.showParallelogramLabelProperties());
	if (this.getCurrentWorkLabel() instanceof DrawArrowObject)
		return (this.showArrowLabelProperties());
	if (this.getCurrentWorkLabel() instanceof DrawLineObject)
		return (this.showLineLabelProperties());
	return (super.showLabelsOnlyProperties());
}

private void
updateLineProperties(DrawLineObject lineObj)
throws Exception
{
	BLine2D line = lineObj.getBLine2D();
	line_P1_X_TF.setText("" + StringUtil.roundStrVal(line.getP1().getX(), 2));
	line_P1_Y_TF.setText("" + StringUtil.roundStrVal(-line.getP1().getY(), 2));
	line_P2_X_TF.setText("" + StringUtil.roundStrVal(line.getP2().getX(), 2));
	line_P2_Y_TF.setText("" + StringUtil.roundStrVal(-line.getP2().getY(), 2));
	lineAngle_TF.setText("" + StringUtil.roundStrVal(line.getAngle(), 2));
	line_Length_TF.setText("" + StringUtil.roundStrVal(line.length(), 2));
	line_Extend_TF.setText("" + StringUtil.roundStrVal(line.length(), 2));
	drawObject_Center_X_TF.setText("" + StringUtil.roundStrVal(lineObj.getX(), 2));
	drawObject_Center_Y_TF.setText("" + StringUtil.roundStrVal(lineObj.getY(), 2));
}

public Box
showLineLabelProperties()
throws Exception
{
	Box mainPanel = super.showLabelsOnlyProperties();
	JPanel tmpPanel = null;

	// BLine2D line = ((DrawLineObject)this.getCurrentWorkLabel()).getBLine2D();

	ActionListener lineEndPt_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double x1 = 0.0;
			double y1 = 0.0;
			double x2 = 0.0;
			double y2 = 0.0;
			try
			{
				x1 = Double.parseDouble(line_P1_X_TF.getText());
				y1 = Double.parseDouble(line_P1_Y_TF.getText());
				x2 = Double.parseDouble(line_P2_X_TF.getText());
				y2 = Double.parseDouble(line_P2_Y_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid position values: " +
					drawObject_Center_X_TF.getText() + " " + drawObject_Center_Y_TF.getText());
				return;
			}
			try
			{
				DrawLineObject lineObj = (DrawLineObject)getCurrentWorkLabel();
				editErase(lineObj, ComplexDefines.InLabelsOnly);
				lineObj.reset(x1, -y1, x2, -y2);
				editRedraw(lineObj, sceneImgG2);
				updateLineProperties(lineObj);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Error in showLabelsOnlyProperties()", e, 1);
			}
		}
	};

	DrawLineObject lineObj = (DrawLineObject)this.getCurrentWorkLabel();
	BLine2D line = lineObj.getBLine2D();

	tmpPanel = this.getNewFlowLeftPanel();
	String lineEndString = null;
	if (lineObj.getLinePartition() == MathDefines.LINE_PARTITION_MID)
		lineEndString = "Mid Point";
	else if (lineObj.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
		lineEndString = "Tail Point";
	else if (lineObj.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
		lineEndString = "Head Point";
	tmpPanel.add(this.getNewViewLabel("End Picked: " + lineEndString));
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	line_P1_X_TF = new JTextField(4);
	line_P1_X_TF.addActionListener(lineEndPt_AL);
	tmpPanel.add(this.getNewViewLabel("pt1 X,Y: "));
	tmpPanel.add(line_P1_X_TF);
	line_P1_Y_TF = new JTextField(4);
	line_P1_Y_TF.addActionListener(lineEndPt_AL);
	tmpPanel.add(line_P1_Y_TF);
	JButton applyBt = this.getNewViewImgPlainButton();
	tmpPanel.add(applyBt);
	applyBt.addActionListener(lineEndPt_AL);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	line_P2_X_TF = new JTextField(4);
	line_P2_X_TF.addActionListener(lineEndPt_AL);
	tmpPanel.add(this.getNewViewLabel("pt2 X,Y: "));
	tmpPanel.add(line_P2_X_TF);
	line_P2_Y_TF = new JTextField(4);
	line_P2_Y_TF.addActionListener(lineEndPt_AL);
	tmpPanel.add(line_P2_Y_TF);
	applyBt = this.getNewViewImgPlainButton();
	tmpPanel.add(applyBt);
	applyBt.addActionListener(lineEndPt_AL);
	mainPanel.add(tmpPanel);

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	ActionListener lineAngle_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double angle = 0.0;
			try
			{
				angle = Double.parseDouble(lineAngle_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + lineAngle_TF.getText());
				return;
			}
			try
			{
				DrawLineObject lineObj = (DrawLineObject)getCurrentWorkLabel();
				editErase(lineObj, ComplexDefines.InLabelsOnly);
				((DrawLineObject)ComplexSceneEditTab.this.getCurrentWorkLabel()).setFromAngle(angle);
				editRedraw(lineObj, sceneImgG2);
				updateLineProperties(lineObj);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyAngleBt:", e, 101);
			}
		}
	};

	tmpPanel = this.getNewFlowLeftPanel();
	lineAngle_TF = new JTextField("" + line.getAngle(), 4);
	lineAngle_TF.addActionListener(lineAngle_AL);
	tmpPanel.add(this.getNewViewLabel("Angle: "));
	tmpPanel.add(lineAngle_TF);

	JButton applyAngleBt = this.getNewViewImgPlainButton();
	applyAngleBt.addActionListener(lineAngle_AL);
	tmpPanel.add(applyAngleBt);
	mainPanel.add(tmpPanel);

	ActionListener lineLengthListener_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double length = 0.0;
			try
			{
				length = Double.parseDouble(line_Length_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid length value: " + line_Length_TF.getText());
				return;
			}
			try
			{
				DrawLineObject lineObj = (DrawLineObject)getCurrentWorkLabel();
				editErase(lineObj, ComplexDefines.InLabelsOnly);
				((DrawLineObject)ComplexSceneEditTab.this.getCurrentWorkLabel()).setFromLength(length);
				editRedraw(lineObj, sceneImgG2);
				updateLineProperties(lineObj);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyLengthBt:", e, 101);
			}
		}
	};

	tmpPanel = this.getNewFlowLeftPanel();
	line_Length_TF = new JTextField(4);
	line_Length_TF.addActionListener(lineLengthListener_AL);
	tmpPanel.add(this.getNewViewLabel("Length: "));
	tmpPanel.add(line_Length_TF);

	JButton applyLengthBt = this.getNewViewImgPlainButton();
	applyLengthBt.addActionListener(lineLengthListener_AL);
	tmpPanel.add(applyLengthBt);
	mainPanel.add(tmpPanel);

	ActionListener lineExtendListener_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double length = 0.0;
			try
			{
				length = Double.parseDouble(line_Extend_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid length value: " + line_Extend_TF.getText());
				return;
			}
			try
			{
				DrawLineObject lineObj = (DrawLineObject)getCurrentWorkLabel();
				editErase(lineObj, ComplexDefines.InLabelsOnly);
				((DrawLineObject)ComplexSceneEditTab.this.getCurrentWorkLabel()).setHeadFromLength(length);
				editRedraw(lineObj, sceneImgG2);
				updateLineProperties(lineObj);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyExtendBt:", e, 101);
			}
		}
	};

	MouseAdapter changeLineExtendMouseListener = new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent evt)
		{
			Component mouseBt = evt.getComponent();
			try
			{
				DrawLineObject lineObj = (DrawLineObject)getCurrentWorkLabel();
				editErase(lineObj, ComplexDefines.drawObjToNucModeDefine(lineObj));
				double shiftAmt = 0.5;
				if (mouseBt == extendLineLonger_Bt)
				{
					lineObj.setHeadFromLength(lineObj.getLength() + shiftAmt);
				}
				else if (mouseBt == extendLineShorter_Bt)
				{
					lineObj.setHeadFromLength(lineObj.getLength() - shiftAmt);
				}
				updateLineProperties(lineObj);
				editRedraw(lineObj, sceneImgG2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("extend: "));
	line_Extend_TF = new JTextField(4);
	line_Extend_TF.setFont(smBtFont);
	line_Extend_TF.setText("" + StringUtil.roundStrVal(lineObj.getX(), 2));
	line_Extend_TF.addActionListener(lineExtendListener_AL);
	tmpPanel.add(line_Extend_TF);
	JButton applyExtend_Bt = this.getNewViewImgPlainButton();
	applyExtend_Bt.addActionListener(lineExtendListener_AL);
	tmpPanel.add(applyExtend_Bt);
	extendLineLonger_Bt = this.getNewViewImgUpButton();
	extendLineLonger_Bt.addMouseListener(changeLineExtendMouseListener);
	tmpPanel.add(extendLineLonger_Bt);
	extendLineShorter_Bt = this.getNewViewImgDownButton();
	extendLineShorter_Bt.addMouseListener(changeLineExtendMouseListener);
	tmpPanel.add(extendLineShorter_Bt);
	mainPanel.add(tmpPanel);

	ActionListener overlayBorder_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double overlayBorder = 0.0;
			try
			{
				overlayBorder = Double.parseDouble(overlayBorder_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid overlayBorder value: " + overlayBorder_TF.getText());
				return;
			}
			try
			{
				DrawLineObject lineObj = (DrawLineObject)getCurrentWorkLabel();
				lineObj.setOverlayBorder(overlayBorder);
				editRedraw(lineObj, sceneImgG2);
				updateLineProperties(lineObj);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.overlayBorder_AL:", e, 101);
			}
		}
	};

	tmpPanel = this.getNewFlowLeftPanel();
	overlayBorder_TF = new JTextField(4);
	overlayBorder_TF.addActionListener(overlayBorder_AL);
	overlayBorder_TF.setText("" + StringUtil.roundStrVal(lineObj.getOverlayBorder(), 2));
	tmpPanel.add(this.getNewViewLabel("Overlay Border: "));
	tmpPanel.add(overlayBorder_TF);
	JButton applyOverlayBorderBt = this.getNewViewImgPlainButton();
	applyOverlayBorderBt.addActionListener(overlayBorder_AL);
	tmpPanel.add(applyOverlayBorderBt);
	mainPanel.add(tmpPanel);

	JButton flipEndPtsBt = this.getNewViewButton("flip end points");
	flipEndPtsBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			((DrawLineObject)ComplexSceneEditTab.this.getCurrentWorkLabel()).flipEndPts();
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(flipEndPtsBt));

	this.addEditProperties(mainPanel);
	this.addCommonLabelProperties(mainPanel);

	this.updateLineProperties(lineObj);
	return (mainPanel);
}

public Box
showStringLabelProperties()
throws Exception
{
	Box mainPanel = super.showLabelsOnlyProperties();
	JPanel tmpPanel = null;

	tmpPanel = this.getNewFlowLeftPanel();
	stringTF = new JTextField(6);
	stringTF.setText(((DrawStringObject)getCurrentWorkLabel()).getDrawString());
	tmpPanel.add(this.getNewViewLabel("edit string: "));
	tmpPanel.add(stringTF);
	JButton applyBt = this.getNewViewImgPlainButton();
	tmpPanel.add(applyBt);
	applyBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				DrawStringObject stringObj = (DrawStringObject)getCurrentWorkLabel();
				Graphics2D g2 = complexSceneView.getCurrentGraphics2D();
				g2.setTransform(getCurrentWorkLabel().getParentG2Transform());
				getCurrentWorkLabel().erase(g2);
				stringObj.setDrawString(stringTF.getText());
				editRedraw(getCurrentWorkLabel(), g2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Error in showLabelsOnlyProperties()", e, 1);
			}
		}
	});
	mainPanel.add(tmpPanel);

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	this.addEditProperties(mainPanel);

	JButton makeFontDefaultBt = this.getNewViewButton("Set default font to this");
	makeFontDefaultBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				DrawFontObject label = (DrawFontObject)ComplexSceneEditTab.this.getCurrentWorkLabel();
				complexSceneView.fontChooser.setCurrentFont(label.getFont());
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.makeFontDefaultBt:", e, 101);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(makeFontDefaultBt));

	JButton setFontToDefaultBt = this.getNewViewButton("Set font to default");
	setFontToDefaultBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				DrawFontObject label = (DrawFontObject)ComplexSceneEditTab.this.getCurrentWorkLabel();
				Graphics2D g2 = complexSceneView.getCurrentGraphics2D();
				g2.setTransform(getCurrentWorkLabel().getParentG2Transform());
				getCurrentWorkLabel().erase(g2);
				label.setFont(complexSceneView.fontChooser.getCurrentFont());
				editRedraw(getCurrentWorkLabel(), g2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.setFontToDefaultBt:", e, 101);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(setFontToDefaultBt));

	this.addCommonLabelProperties(mainPanel);

	return (mainPanel);
}

public Box
showCircleLabelProperties()
throws Exception
{
	Box mainPanel = super.showLabelsOnlyProperties();
	JPanel tmpPanel = null;

	ActionListener circleActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double radius = 0.0;
			double startAngle = 0.0;
			double angleExtent = 0.0;
			double x = 0.0;
			double y = 0.0;
			try
			{
				radius = Double.parseDouble(circle_Radius_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid radius value: " + circle_Radius_TF.getText());
				return;
			}
			try
			{
				x = Double.parseDouble(drawObject_Center_X_TF.getText());
				y = -Double.parseDouble(drawObject_Center_Y_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid position values: " +
					drawObject_Center_X_TF.getText() + " " + drawObject_Center_Y_TF.getText());
				return;
			}
			try
			{
				startAngle = Double.parseDouble(circle_StartAngle_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid start angle value: " + circle_StartAngle_TF.getText());
				return;
			}
			try
			{
				angleExtent = Double.parseDouble(circle_AngleExtent_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle extent value: " + circle_AngleExtent_TF.getText());
				return;
			}
			try
			{
				redrawCircleObjectTFs(x, y, radius, startAngle, angleExtent);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyRadiusBt:", e, 101);
			}
		}
	};

	MouseAdapter circleMouseListener = new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent evt)
		{
			Component mouseBt = evt.getComponent();
			try
			{
				DrawCircleObject circleObj = (DrawCircleObject)getCurrentWorkLabel();
				if (mouseBt == circleLabelRadiusUpBt)
				{
					redrawCircleObjectTFs(circleObj.getX(), circleObj.getY(),
						circleObj.getRadius() + 1.0,
						circleObj.getStartAngle(), circleObj.getAngleExtent());
				}
				else if (mouseBt == circleLabelRadiusDownBt)
				{
					if (circleObj.getRadius() > 2.0)
						redrawCircleObjectTFs(circleObj.getX(), circleObj.getY(),
						circleObj.getRadius() - 1.0,
						circleObj.getStartAngle(), circleObj.getAngleExtent());
				}
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	DrawCircleObject circleObj = (DrawCircleObject)this.getCurrentWorkLabel();

	tmpPanel = this.getNewFlowLeftPanel();
	circle_Radius_TF = new JTextField(4);
	circle_Radius_TF.setText("" + StringUtil.roundStrVal(circleObj.getRadius(), 2));
	tmpPanel.add(this.getNewViewLabel("Radius: "));
	tmpPanel.add(circle_Radius_TF);
	circle_Radius_TF.addActionListener(circleActionListener);

	JButton applyRadiusBt = this.getNewViewImgPlainButton();
	applyRadiusBt.addActionListener(circleActionListener);
	tmpPanel.add(applyRadiusBt);

	circleLabelRadiusUpBt = this.getNewViewImgUpButton();
	circleLabelRadiusUpBt.addMouseListener(circleMouseListener);
	tmpPanel.add(circleLabelRadiusUpBt);

	circleLabelRadiusDownBt = this.getNewViewImgDownButton();
	circleLabelRadiusDownBt.addMouseListener(circleMouseListener);
	tmpPanel.add(circleLabelRadiusDownBt);

	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	circle_StartAngle_TF = new JTextField(4);
	circle_StartAngle_TF.setText("" + StringUtil.roundStrVal(circleObj.getStartAngle(), 2));
	tmpPanel.add(this.getNewViewLabel("Start Angle: "));
	tmpPanel.add(circle_StartAngle_TF);
	circle_StartAngle_TF.addActionListener(circleActionListener);

	JButton applyStartAngle_Bt = this.getNewViewImgPlainButton();
	applyStartAngle_Bt.addActionListener(circleActionListener);
	tmpPanel.add(applyStartAngle_Bt);

	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	circle_AngleExtent_TF = new JTextField(4);
	circle_AngleExtent_TF.setText("" + StringUtil.roundStrVal(circleObj.getAngleExtent(), 2));
	tmpPanel.add(this.getNewViewLabel("Angle Extent: "));
	tmpPanel.add(circle_AngleExtent_TF);
	circle_AngleExtent_TF.addActionListener(circleActionListener);

	JButton applyAngleExtent_Bt = this.getNewViewImgPlainButton();
	applyAngleExtent_Bt.addActionListener(circleActionListener);
	tmpPanel.add(applyAngleExtent_Bt);

	mainPanel.add(tmpPanel);

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	this.addEditProperties(mainPanel);
	this.addCommonLabelProperties(mainPanel);

	return (mainPanel);
}

public Box
showTriangleLabelProperties()
throws Exception
{
	Box mainPanel = super.showLabelsOnlyProperties();
	JPanel tmpPanel = null;

	ActionListener triangleActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double height = 0.0;
			double baseWidth = 0.0;
			double angle = 0.0;
			double x = 0.0;
			double y = 0.0;
			try
			{
				height = Double.parseDouble(triangleHeight_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid height value: " + triangleHeight_TF.getText());
				return;
			}
			try
			{
				baseWidth = Double.parseDouble(triangleWidth_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid width value: " + triangleWidth_TF.getText());
				return;
			}
			try
			{
				angle = Double.parseDouble(triangleAngle_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + triangleAngle_TF.getText());
				return;
			}
			try
			{
				x = Double.parseDouble(drawObject_Center_X_TF.getText());
				y = -Double.parseDouble(drawObject_Center_Y_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid position values: " +
					drawObject_Center_X_TF.getText() + " " + drawObject_Center_Y_TF.getText());
				return;
			}
			try
			{
				redrawTriangleObjectTFs(x, y, baseWidth, height, angle);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyHeightBt:", e, 101);
			}
		}
	};

	MouseAdapter triangleMouseListener = new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent evt)
		{
			Component mouseBt = evt.getComponent();
			try
			{
				double inc = 0.5;
				DrawTriangleObject triangleObj = (DrawTriangleObject)getCurrentWorkLabel();
				if (mouseBt == triangleLabelWidthUpBt)
				{
					redrawTriangleObjectTFs(triangleObj.getX(), triangleObj.getY(), triangleObj.getBaseWidth() + inc, triangleObj.getHeight(), triangleObj.getAngle());
					triangleWidth_TF.setText(StringUtil.roundStrVal(triangleObj.getBaseWidth(), 2));
				}
				else if (mouseBt == triangleLabelWidthDownBt)
				{
					if (triangleObj.getBaseWidth() > 2.0)
						redrawTriangleObjectTFs(triangleObj.getX(), triangleObj.getY(),
						triangleObj.getBaseWidth() - inc,
						triangleObj.getHeight(), triangleObj.getAngle());
					triangleWidth_TF.setText(StringUtil.roundStrVal(triangleObj.getBaseWidth(), 2));
				}
				else if (mouseBt == triangleLabelHeightUpBt)
				{
					redrawTriangleObjectTFs(triangleObj.getX(), triangleObj.getY(), triangleObj.getBaseWidth(), triangleObj.getHeight() + inc, triangleObj.getAngle());
					triangleHeight_TF.setText(StringUtil.roundStrVal(triangleObj.getHeight(), 2));
				}
				else if (mouseBt == triangleLabelHeightDownBt)
				{
					if (triangleObj.getHeight() > 2.0)
						redrawTriangleObjectTFs(triangleObj.getX(), triangleObj.getY(),
						triangleObj.getBaseWidth(),
						triangleObj.getHeight() - inc, triangleObj.getAngle());
					triangleHeight_TF.setText(StringUtil.roundStrVal(triangleObj.getHeight(), 2));
				}
				else if (mouseBt == triangleLabelAngleUpBt)
				{
					redrawTriangleObjectTFs(triangleObj.getX(), triangleObj.getY(),
						triangleObj.getBaseWidth(), triangleObj.getHeight(),
						triangleObj.getAngle() + inc);
					if (triangleObj.getAngle() >= 360.0)
						triangleObj.setAngle(triangleObj.getAngle() - 360.0);
					triangleAngle_TF.setText(StringUtil.roundStrVal(triangleObj.getAngle(), 2));
				}
				else if (mouseBt == triangleLabelAngleDownBt)
				{
					redrawTriangleObjectTFs(triangleObj.getX(), triangleObj.getY(),
						triangleObj.getBaseWidth(), triangleObj.getHeight(),
						triangleObj.getAngle() - inc);
					triangleAngle_TF.setText(StringUtil.roundStrVal(triangleObj.getAngle(), 2));
				}
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	DrawTriangleObject triangleObj = (DrawTriangleObject)this.getCurrentWorkLabel();

	tmpPanel = this.getNewFlowLeftPanel();
	triangleWidth_TF = new JTextField(4);
	triangleWidth_TF.setText("" + StringUtil.roundStrVal(triangleObj.getBaseWidth(), 2));
	tmpPanel.add(this.getNewViewLabel("Base Width: "));
	tmpPanel.add(triangleWidth_TF);
	triangleWidth_TF.addActionListener(triangleActionListener);

	JButton applyWidthBt = this.getNewViewImgPlainButton();
	applyWidthBt.addActionListener(triangleActionListener);
	tmpPanel.add(applyWidthBt);

	triangleLabelWidthUpBt = this.getNewViewImgUpButton();
	triangleLabelWidthUpBt.addMouseListener(triangleMouseListener);
	tmpPanel.add(triangleLabelWidthUpBt);

	triangleLabelWidthDownBt = this.getNewViewImgDownButton();
	triangleLabelWidthDownBt.addMouseListener(triangleMouseListener);
	tmpPanel.add(triangleLabelWidthDownBt);

	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	triangleHeight_TF = new JTextField(4);
	triangleHeight_TF.setText("" + StringUtil.roundStrVal(triangleObj.getHeight(), 2));
	tmpPanel.add(this.getNewViewLabel("Height: "));
	tmpPanel.add(triangleHeight_TF);
	triangleHeight_TF.addActionListener(triangleActionListener);

	JButton applyHeightBt = this.getNewViewImgPlainButton();
	applyHeightBt.addActionListener(triangleActionListener);
	tmpPanel.add(applyHeightBt);

	triangleLabelHeightUpBt = this.getNewViewImgUpButton();
	triangleLabelHeightUpBt.addMouseListener(triangleMouseListener);
	tmpPanel.add(triangleLabelHeightUpBt);

	triangleLabelHeightDownBt = this.getNewViewImgDownButton();
	triangleLabelHeightDownBt.addMouseListener(triangleMouseListener);
	tmpPanel.add(triangleLabelHeightDownBt);

	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	triangleAngle_TF = new JTextField(4);
	triangleAngle_TF.setText("" + StringUtil.roundStrVal(triangleObj.getAngle(), 2));
	tmpPanel.add(this.getNewViewLabel("Angle: "));
	tmpPanel.add(triangleAngle_TF);
	triangleAngle_TF.addActionListener(triangleActionListener);

	JButton applyAngleBt = this.getNewViewImgPlainButton();
	applyAngleBt.addActionListener(triangleActionListener);
	tmpPanel.add(applyAngleBt);

	triangleLabelAngleUpBt = this.getNewViewImgUpButton();
	triangleLabelAngleUpBt.addMouseListener(triangleMouseListener);
	tmpPanel.add(triangleLabelAngleUpBt);

	triangleLabelAngleDownBt = this.getNewViewImgDownButton();
	triangleLabelAngleDownBt.addMouseListener(triangleMouseListener);
	tmpPanel.add(triangleLabelAngleDownBt);

	mainPanel.add(tmpPanel);

	this.addCommonLabelProperties(mainPanel);
	this.addEditProperties(mainPanel);

	return (mainPanel);
}

public Box
showParallelogramLabelProperties()
throws Exception
{
	Box mainPanel = super.showLabelsOnlyProperties();
	JPanel tmpPanel = null;

	ActionListener parallelogramActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double side1 = 0.0;
			double angle1 = 0.0;
			double side2 = 0.0;
			double angle2 = 0.0;
			double x = 0.0;
			double y = 0.0;
			try
			{
				side1 = Double.parseDouble(parallelogramLabelSide1_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid side1 value: " + parallelogramLabelSide1_TF.getText());
				return;
			}
			try
			{
				side2 = Double.parseDouble(parallelogramLabelSide2_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid width value: " + parallelogramLabelSide2_TF.getText());
				return;
			}
			try
			{
				angle1 = Double.parseDouble(parallelogramLabelAngle1_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle1 value: " + parallelogramLabelAngle1_TF.getText());
				return;
			}
			try
			{
				angle2 = Double.parseDouble(parallelogramLabelAngle2_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle2 value: " + parallelogramLabelAngle2_TF.getText());
				return;
			}
			try
			{
				x = Double.parseDouble(drawObject_Center_X_TF.getText());
				y = -Double.parseDouble(drawObject_Center_Y_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid position values: " +
					drawObject_Center_X_TF.getText() + " " + drawObject_Center_Y_TF.getText());
				return;
			}
			try
			{
				redrawParallelogramObjectTFs(x, y, side1, angle1, side2, angle2);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyHeightBt:", e, 101);
			}
		}
	};

	MouseAdapter parallelogramMouseListener = new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent evt)
		{
			Component mouseBt = evt.getComponent();
			try
			{
				double inc = 0.2;
				DrawParallelogramObject parallelogramObj = (DrawParallelogramObject)getCurrentWorkLabel();
				if (mouseBt == parallelogramLabelSide1Up_Bt)
				{
					redrawParallelogramObjectTFs(
						parallelogramObj.getX(), parallelogramObj.getY(),
						parallelogramObj.getSide1() + inc,
						parallelogramObj.getAngle1(),
						parallelogramObj.getSide2(),
						parallelogramObj.getAngle2());
				}
				else if (mouseBt == parallelogramLabelSide1Down_Bt)
				{
					redrawParallelogramObjectTFs(
						parallelogramObj.getX(), parallelogramObj.getY(),
						parallelogramObj.getSide1() - inc,
						parallelogramObj.getAngle1(),
						parallelogramObj.getSide2(),
						parallelogramObj.getAngle2());
				}
				else if (mouseBt == parallelogramLabelAngle1Up_Bt)
				{
					redrawParallelogramObjectTFs(
						parallelogramObj.getX(), parallelogramObj.getY(),
						parallelogramObj.getSide1(),
						parallelogramObj.getAngle1() + inc,
						parallelogramObj.getSide2(),
						parallelogramObj.getAngle2());
				}
				else if (mouseBt == parallelogramLabelAngle1Down_Bt)
				{
					redrawParallelogramObjectTFs(
						parallelogramObj.getX(), parallelogramObj.getY(),
						parallelogramObj.getSide1(),
						parallelogramObj.getAngle1() - inc,
						parallelogramObj.getSide2(),
						parallelogramObj.getAngle2());
				}
				else if (mouseBt == parallelogramLabelSide2Up_Bt)
				{
					redrawParallelogramObjectTFs(
						parallelogramObj.getX(), parallelogramObj.getY(),
						parallelogramObj.getSide1(),
						parallelogramObj.getAngle1(),
						parallelogramObj.getSide2() + inc,
						parallelogramObj.getAngle2());
				}
				else if (mouseBt == parallelogramLabelSide2Down_Bt)
				{
					redrawParallelogramObjectTFs(
						parallelogramObj.getX(), parallelogramObj.getY(),
						parallelogramObj.getSide1(),
						parallelogramObj.getAngle1(),
						parallelogramObj.getSide2() - inc,
						parallelogramObj.getAngle2());
				}
				else if (mouseBt == parallelogramLabelAngle2Up_Bt)
				{
					redrawParallelogramObjectTFs(
						parallelogramObj.getX(), parallelogramObj.getY(),
						parallelogramObj.getSide1(),
						parallelogramObj.getAngle1(),
						parallelogramObj.getSide2(),
						parallelogramObj.getAngle2() + inc);
				}
				else if (mouseBt == parallelogramLabelAngle2Down_Bt)
				{
					redrawParallelogramObjectTFs(
						parallelogramObj.getX(), parallelogramObj.getY(),
						parallelogramObj.getSide1(),
						parallelogramObj.getAngle1(),
						parallelogramObj.getSide2(),
						parallelogramObj.getAngle2() - inc);
				}
				parallelogramLabelSide1_TF.setText(StringUtil.roundStrVal(parallelogramObj.getSide1(), 2));
				parallelogramLabelSide2_TF.setText(StringUtil.roundStrVal(parallelogramObj.getSide2(), 2));
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	DrawParallelogramObject parallelogramObj = (DrawParallelogramObject)this.getCurrentWorkLabel();
	/*
private JButton parallelogramLabelSide1Up_Bt = null;
private JButton parallelogramLabelAngle1Up_Bt = null;
private JButton parallelogramLabelSide2Up_Bt = null;
private JButton parallelogramLabelAngle2Up_Bt = null;
private JTextField parallelogramLabelSide1_TF = null;
private JTextField parallelogramLabelAngle1_TF = null;
private JTextField parallelogramLabelSide2_TF = null;
private JTextField parallelogramLabelAngle2_TF = null;
	*/

	tmpPanel = this.getNewFlowLeftPanel();
	parallelogramLabelSide1_TF = new JTextField(4);
	parallelogramLabelSide1_TF.setText("" + StringUtil.roundStrVal(parallelogramObj.getSide1(), 2));
	tmpPanel.add(this.getNewViewLabel("Side 1: "));
	tmpPanel.add(parallelogramLabelSide1_TF);
	parallelogramLabelSide1_TF.addActionListener(parallelogramActionListener);
	JButton applySide1_Bt = this.getNewViewImgPlainButton();
	applySide1_Bt.addActionListener(parallelogramActionListener);
	tmpPanel.add(applySide1_Bt);
	parallelogramLabelSide1Up_Bt = this.getNewViewImgUpButton();
	parallelogramLabelSide1Up_Bt.addMouseListener(parallelogramMouseListener);
	tmpPanel.add(parallelogramLabelSide1Up_Bt);
	parallelogramLabelSide1Down_Bt = this.getNewViewImgDownButton();
	parallelogramLabelSide1Down_Bt.addMouseListener(parallelogramMouseListener);
	tmpPanel.add(parallelogramLabelSide1Down_Bt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	parallelogramLabelAngle1_TF = new JTextField(4);
	parallelogramLabelAngle1_TF.setText("" + StringUtil.roundStrVal(parallelogramObj.getAngle1(), 2));
	tmpPanel.add(this.getNewViewLabel("Angle 1: "));
	tmpPanel.add(parallelogramLabelAngle1_TF);
	parallelogramLabelAngle1_TF.addActionListener(parallelogramActionListener);
	JButton applyAngle1_Bt = this.getNewViewImgPlainButton();
	applyAngle1_Bt.addActionListener(parallelogramActionListener);
	tmpPanel.add(applyAngle1_Bt);
	parallelogramLabelAngle1Up_Bt = this.getNewViewImgUpButton();
	parallelogramLabelAngle1Up_Bt.addMouseListener(parallelogramMouseListener);
	tmpPanel.add(parallelogramLabelAngle1Up_Bt);
	parallelogramLabelAngle1Down_Bt = this.getNewViewImgDownButton();
	parallelogramLabelAngle1Down_Bt.addMouseListener(parallelogramMouseListener);
	tmpPanel.add(parallelogramLabelAngle1Down_Bt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	parallelogramLabelSide2_TF = new JTextField(4);
	parallelogramLabelSide2_TF.setText("" + StringUtil.roundStrVal(parallelogramObj.getSide2(), 2));
	tmpPanel.add(this.getNewViewLabel("Side 2: "));
	tmpPanel.add(parallelogramLabelSide2_TF);
	parallelogramLabelSide2_TF.addActionListener(parallelogramActionListener);
	JButton applySide2_Bt = this.getNewViewImgPlainButton();
	applySide2_Bt.addActionListener(parallelogramActionListener);
	tmpPanel.add(applySide2_Bt);
	parallelogramLabelSide2Up_Bt = this.getNewViewImgUpButton();
	parallelogramLabelSide2Up_Bt.addMouseListener(parallelogramMouseListener);
	tmpPanel.add(parallelogramLabelSide2Up_Bt);
	parallelogramLabelSide2Down_Bt = this.getNewViewImgDownButton();
	parallelogramLabelSide2Down_Bt.addMouseListener(parallelogramMouseListener);
	tmpPanel.add(parallelogramLabelSide2Down_Bt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	parallelogramLabelAngle2_TF = new JTextField(4);
	parallelogramLabelAngle2_TF.setText("" + StringUtil.roundStrVal(parallelogramObj.getAngle2(), 2));
	tmpPanel.add(this.getNewViewLabel("Angle 2: "));
	tmpPanel.add(parallelogramLabelAngle2_TF);
	parallelogramLabelAngle2_TF.addActionListener(parallelogramActionListener);
	JButton applyAngle2_Bt = this.getNewViewImgPlainButton();
	applyAngle2_Bt.addActionListener(parallelogramActionListener);
	tmpPanel.add(applyAngle2_Bt);
	parallelogramLabelAngle2Up_Bt = this.getNewViewImgUpButton();
	parallelogramLabelAngle2Up_Bt.addMouseListener(parallelogramMouseListener);
	tmpPanel.add(parallelogramLabelAngle2Up_Bt);
	parallelogramLabelAngle2Down_Bt = this.getNewViewImgDownButton();
	parallelogramLabelAngle2Down_Bt.addMouseListener(parallelogramMouseListener);
	tmpPanel.add(parallelogramLabelAngle2Down_Bt);
	mainPanel.add(tmpPanel);

	this.addCommonLabelProperties(mainPanel);
	this.addEditProperties(mainPanel);

	return (mainPanel);
}

public Box
showArrowLabelProperties()
throws Exception
{
	Box mainPanel = super.showLabelsOnlyProperties();
	JPanel tmpPanel = null;

	ActionListener arrowActionListener = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double height = 0.0;
			double baseWidth = 0.0;
			double tailLength = 0.0;
			double tailWidth = 0.0;
			double baseIndent = 0.0;
			double angle = 0.0;
			double x = 0.0;
			double y = 0.0;
			try
			{

			try
			{
				height = Double.parseDouble(arrowLabelHeight_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid side1 value: " + arrowLabelHeight_TF.getText());
				return;
			}
			try
			{
				baseWidth = Double.parseDouble(arrowLabelBaseWidth_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid side1 value: " + arrowLabelBaseWidth_TF.getText());
				return;
			}
			try
			{
				tailLength = Double.parseDouble(arrowLabelTailLength_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid side1 value: " + arrowLabelTailLength_TF.getText());
				return;
			}
			try
			{
				tailWidth = Double.parseDouble(arrowLabelTailWidth_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid side1 value: " + arrowLabelTailWidth_TF.getText());
				return;
			}
			try
			{
				baseIndent = Double.parseDouble(arrowLabelBaseIndent_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid side1 value: " + arrowLabelBaseIndent_TF.getText());
				return;
			}
			try
			{
				angle = Double.parseDouble(arrowLabelAngle_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid side1 value: " + arrowLabelAngle_TF.getText());
				return;
			}
			try
			{
				x = Double.parseDouble(drawObject_Center_X_TF.getText());
				y = -Double.parseDouble(drawObject_Center_Y_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid position values: " +
					drawObject_Center_X_TF.getText() + " " + drawObject_Center_Y_TF.getText());
				return;
			}
			
			redrawArrowObjectTFs(x, y, height, baseWidth, tailLength, tailWidth, baseIndent, angle);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneEdit.applyHeightBt:", e, 101);
			}
		}
	};

	MouseAdapter arrowMouseListener = new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent evt)
		{
			Component mouseBt = evt.getComponent();
			try
			{
				double inc = 0.2;
				DrawArrowObject arrowObj = (DrawArrowObject)getCurrentWorkLabel();
				if (mouseBt == arrowLabelHeightUp_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight() + inc,
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength(),
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent(),
						arrowObj.getAngle());
					arrowLabelHeight_TF.setText(StringUtil.roundStrVal(arrowObj.getHeight(), 2));
				}
				else if (mouseBt == arrowLabelHeightDown_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight() - inc,
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength(),
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent(),
						arrowObj.getAngle());
					arrowLabelHeight_TF.setText(StringUtil.roundStrVal(arrowObj.getHeight(), 2));
				}
				if (mouseBt == arrowLabelBaseWidthUp_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth() + inc,
						arrowObj.getTailLength(),
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent(),
						arrowObj.getAngle());
					arrowLabelBaseWidth_TF.setText(StringUtil.roundStrVal(arrowObj.getBaseWidth(), 2));
				}
				else if (mouseBt == arrowLabelBaseWidthDown_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth() - inc,
						arrowObj.getTailLength(),
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent(),
						arrowObj.getAngle());
					arrowLabelBaseWidth_TF.setText(StringUtil.roundStrVal(arrowObj.getBaseWidth(), 2));
				}
				if (mouseBt == arrowLabelTailLengthUp_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength() + inc,
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent(),
						arrowObj.getAngle());
					arrowLabelTailLength_TF.setText(StringUtil.roundStrVal(arrowObj.getTailLength(), 2));
				}
				else if (mouseBt == arrowLabelTailLengthDown_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength() - inc,
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent(),
						arrowObj.getAngle());
					arrowLabelTailLength_TF.setText(StringUtil.roundStrVal(arrowObj.getTailLength(), 2));
				}
				if (mouseBt == arrowLabelTailWidthUp_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength(),
						arrowObj.getTailWidth() + inc,
						arrowObj.getBaseIndent(),
						arrowObj.getAngle());
					arrowLabelTailWidth_TF.setText(StringUtil.roundStrVal(arrowObj.getTailWidth(), 2));
				}
				else if (mouseBt == arrowLabelTailWidthDown_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength(),
						arrowObj.getTailWidth() - inc,
						arrowObj.getBaseIndent(),
						arrowObj.getAngle());
					arrowLabelTailWidth_TF.setText(StringUtil.roundStrVal(arrowObj.getTailWidth(), 2));
				}
				if (mouseBt == arrowLabelBaseIndentUp_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength(),
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent() + inc,
						arrowObj.getAngle());
					arrowLabelBaseIndent_TF.setText(StringUtil.roundStrVal(arrowObj.getBaseIndent(), 2));
				}
				else if (mouseBt == arrowLabelBaseIndentDown_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength(),
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent() - inc,
						arrowObj.getAngle());
					arrowLabelBaseIndent_TF.setText(StringUtil.roundStrVal(arrowObj.getBaseIndent(), 2));
				}
				if (mouseBt == arrowLabelAngleUp_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength(),
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent(),
						arrowObj.getAngle() + inc);
					arrowLabelAngle_TF.setText(StringUtil.roundStrVal(arrowObj.getAngle(), 2));
				}
				else if (mouseBt == arrowLabelAngleDown_Bt)
				{
					redrawArrowObjectTFs(
						arrowObj.getX(), arrowObj.getY(),
						arrowObj.getHeight(),
						arrowObj.getBaseWidth(),
						arrowObj.getTailLength(),
						arrowObj.getTailWidth(),
						arrowObj.getBaseIndent(),
						arrowObj.getAngle() - inc);
					arrowLabelAngle_TF.setText(StringUtil.roundStrVal(arrowObj.getAngle(), 2));
				}
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	};

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	DrawArrowObject arrowObj = (DrawArrowObject)this.getCurrentWorkLabel();

	tmpPanel = this.getNewFlowLeftPanel();
	arrowLabelHeight_TF = new JTextField(4);
	arrowLabelHeight_TF.setText("" + StringUtil.roundStrVal(arrowObj.getHeight(), 2));
	tmpPanel.add(this.getNewViewLabel("Height: "));
	tmpPanel.add(arrowLabelHeight_TF);
	arrowLabelHeight_TF.addActionListener(arrowActionListener);
	JButton applyHeight_Bt = this.getNewViewImgPlainButton();
	applyHeight_Bt.addActionListener(arrowActionListener);
	tmpPanel.add(applyHeight_Bt);
	arrowLabelHeightUp_Bt = this.getNewViewImgUpButton();
	arrowLabelHeightUp_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelHeightUp_Bt);
	arrowLabelHeightDown_Bt = this.getNewViewImgDownButton();
	arrowLabelHeightDown_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelHeightDown_Bt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	arrowLabelBaseWidth_TF = new JTextField(4);
	arrowLabelBaseWidth_TF.setText("" + StringUtil.roundStrVal(arrowObj.getBaseWidth(), 2));
	tmpPanel.add(this.getNewViewLabel("BaseWidth: "));
	tmpPanel.add(arrowLabelBaseWidth_TF);
	arrowLabelBaseWidth_TF.addActionListener(arrowActionListener);
	JButton applyBaseWidth_Bt = this.getNewViewImgPlainButton();
	applyBaseWidth_Bt.addActionListener(arrowActionListener);
	tmpPanel.add(applyBaseWidth_Bt);
	arrowLabelBaseWidthUp_Bt = this.getNewViewImgUpButton();
	arrowLabelBaseWidthUp_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelBaseWidthUp_Bt);
	arrowLabelBaseWidthDown_Bt = this.getNewViewImgDownButton();
	arrowLabelBaseWidthDown_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelBaseWidthDown_Bt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	arrowLabelTailLength_TF = new JTextField(4);
	arrowLabelTailLength_TF.setText("" + StringUtil.roundStrVal(arrowObj.getTailLength(), 2));
	tmpPanel.add(this.getNewViewLabel("TailLength: "));
	tmpPanel.add(arrowLabelTailLength_TF);
	arrowLabelTailLength_TF.addActionListener(arrowActionListener);
	JButton applyTailLength_Bt = this.getNewViewImgPlainButton();
	applyTailLength_Bt.addActionListener(arrowActionListener);
	tmpPanel.add(applyTailLength_Bt);
	arrowLabelTailLengthUp_Bt = this.getNewViewImgUpButton();
	arrowLabelTailLengthUp_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelTailLengthUp_Bt);
	arrowLabelTailLengthDown_Bt = this.getNewViewImgDownButton();
	arrowLabelTailLengthDown_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelTailLengthDown_Bt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	arrowLabelTailWidth_TF = new JTextField(4);
	arrowLabelTailWidth_TF.setText("" + StringUtil.roundStrVal(arrowObj.getTailWidth(), 2));
	tmpPanel.add(this.getNewViewLabel("TailWidth: "));
	tmpPanel.add(arrowLabelTailWidth_TF);
	arrowLabelTailWidth_TF.addActionListener(arrowActionListener);
	JButton applyTailWidth_Bt = this.getNewViewImgPlainButton();
	applyTailWidth_Bt.addActionListener(arrowActionListener);
	tmpPanel.add(applyTailWidth_Bt);
	arrowLabelTailWidthUp_Bt = this.getNewViewImgUpButton();
	arrowLabelTailWidthUp_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelTailWidthUp_Bt);
	arrowLabelTailWidthDown_Bt = this.getNewViewImgDownButton();
	arrowLabelTailWidthDown_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelTailWidthDown_Bt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	arrowLabelBaseIndent_TF = new JTextField(4);
	arrowLabelBaseIndent_TF.setText("" + StringUtil.roundStrVal(arrowObj.getBaseIndent(), 2));
	tmpPanel.add(this.getNewViewLabel("BaseIndent: "));
	tmpPanel.add(arrowLabelBaseIndent_TF);
	arrowLabelBaseIndent_TF.addActionListener(arrowActionListener);
	JButton applyBaseIndent_Bt = this.getNewViewImgPlainButton();
	applyBaseIndent_Bt.addActionListener(arrowActionListener);
	tmpPanel.add(applyBaseIndent_Bt);
	arrowLabelBaseIndentUp_Bt = this.getNewViewImgUpButton();
	arrowLabelBaseIndentUp_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelBaseIndentUp_Bt);
	arrowLabelBaseIndentDown_Bt = this.getNewViewImgDownButton();
	arrowLabelBaseIndentDown_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelBaseIndentDown_Bt);
	mainPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	arrowLabelAngle_TF = new JTextField(4);
	arrowLabelAngle_TF.setText("" + StringUtil.roundStrVal(arrowObj.getAngle(), 2));
	tmpPanel.add(this.getNewViewLabel("Angle: "));
	tmpPanel.add(arrowLabelAngle_TF);
	arrowLabelAngle_TF.addActionListener(arrowActionListener);
	JButton applyAngle_Bt = this.getNewViewImgPlainButton();
	applyAngle_Bt.addActionListener(arrowActionListener);
	tmpPanel.add(applyAngle_Bt);
	arrowLabelAngleUp_Bt = this.getNewViewImgUpButton();
	arrowLabelAngleUp_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelAngleUp_Bt);
	arrowLabelAngleDown_Bt = this.getNewViewImgDownButton();
	arrowLabelAngleDown_Bt.addMouseListener(arrowMouseListener);
	tmpPanel.add(arrowLabelAngleDown_Bt);
	mainPanel.add(tmpPanel);

	this.addCommonLabelProperties(mainPanel);
	this.addEditProperties(mainPanel);

	return (mainPanel);
}

/*
public Box
showComplexAreaProperties()
throws Exception
{
	Box mainPanel = super.showComplexAreaProperties();

	this.addEditProperties(mainPanel);
	double lineLength = this.getCurrentWorkComplexArea().getNucLabelLineLength(this.getCurrentWorkNuc().getID());
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	return (mainPanel);
}
*/

public Box
showComplexSceneProperties()
throws Exception
{
	Box mainPanel = super.showComplexSceneProperties();

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	complexSceneNameTF = new JTextField(12);
	complexSceneNameTF.setText(this.getCurrentWorkComplexScene().getName());
	complexSceneNameTF.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkComplexScene().setName(complexSceneNameTF.getText().trim());
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	tmpPanel.add(this.getNewViewLabel("Name: "));
	tmpPanel.add(complexSceneNameTF);
	mainPanel.add(tmpPanel);

	mainPanel.add(this.changeDrawObjectXPanel());
	mainPanel.add(this.changeDrawObjectYPanel());

	this.addEditProperties(mainPanel);

	double lineLength = 0.0;
	for (int graphNodeID = 0;graphNodeID < this.getCurrentWorkComplexScene().getItemCount();graphNodeID++)
	{
		ComplexScene obj = (ComplexScene)this.getCurrentWorkComplexScene().getItemAt(graphNodeID);
		if (obj == null)
			continue;
		for (int objID = 0;objID < obj.getItemCount();objID++)
		{
			Object subObj = obj.getItemAt(objID);
			if (subObj == null)
				continue;
			if (subObj instanceof SSData2D)
			{
				SSData2D sstr = (SSData2D)subObj;
				// NEED to put the version that's in NucCollection into SSData2D and
				// have SSData2D return (0.0) if it can't find one and carry on if get 0.0
				lineLength = sstr.getNucLabelLineLength(1);
				if (lineLength != 0.0)
					break;
			}
		}
		if (lineLength != 0.0)
			break;
	}
	if (lineLength <= 0.0)
		lineLength = ComplexDefines.NUCLABEL_LINE_LENGTH;
	nucLineLength_TF.setText(StringUtil.roundStrVal(lineLength, 2));
	nucNumberLabelStartID_TF.setText("1");

	/*
	JButton setAllNucsLinesBt = this.getNewViewButton("Set All Nucs linelengths");
	setAllNucsLinesBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			double length = 0.0;
			try
			{
				length = Double.parseDouble(nucLineLength_TF.getText());
			}
			catch (NumberFormatException e)
			{
				alert("Invalid length value: " +
					nucLineLength_TF.getText());
				return;
			}
			try
			{
				getCurrentWorkComplexScene().resetNucLabelsLineLengths(length);
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.setAllNucsLinesBt:", e, 101);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(setAllNucsLinesBt));
	*/

	JButton checkAllNucsLineBt = this.getNewViewButton("Check All Nucs line endpts");
	checkAllNucsLineBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				Nuc2D nuc = getCurrentWorkComplexScene().checkNucLabelsLineEndPts();
				if (nuc != null)
					alert("nuc's endpts need flipping: " + nuc);
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.checkAllNucsLineBt:", e, 101);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(checkAllNucsLineBt));

	/* I think I was using this to set hidden attribute once colored. I'd rather
	 * have a hide single strands button in cycle
	JButton setAllSingleStrandNucsToCurrentColorBt = this.getNewViewButton("Color All SS Nucs");
	setAllSingleStrandNucsToCurrentColorBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkComplexScene().colorAllRNASingleStrandedNucs(ComplexSceneEditTab.this.getColorChooser().getColor());
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.setAllSingleStrandNucsToCurrentColorBt:", e, 101);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(setAllSingleStrandNucsToCurrentColorBt));
	*/

	JButton clearAllHiddenNucsBt = this.getNewViewButton("Clear all nucs hidden attribute");
	clearAllHiddenNucsBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getCurrentWorkComplexScene().clearHidden();
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.clearAllHiddenNucsBt:", e, 101);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonPanel(clearAllHiddenNucsBt));

	return (mainPanel);
}

private void
initRotateTransform()
throws Exception
{
	parentAffTr = ((DrawObjectCollection)
		this.getCurrentWorkNuc().getParentCollection()).getG2Transform();
	parentInvAffTr = parentAffTr.createInverse();
	if (parentTransformTestPt == null)
		parentTransformTestPt = new Point2D.Double();
	if (parentTransformedPt == null)
		parentTransformedPt = new Point2D.Double();
	if (rotateAffTr == null)
		rotateAffTr = new AffineTransform();
	if (rotateMousePt == null)
		rotateMousePt = new Point2D.Double();
	if (lastRotateMousePt == null)
		lastRotateMousePt = new Point2D.Double();
	if (rotateAnchorPt == null)
		rotateAnchorPt = new Point2D.Double(); // pt on drawObject rotateMousePt translates to
}

private void
setRotateTransform(double lastMouseX, double lastMouseY, double x, double y)
{
	// set init pick pt
	parentTransformTestPt.setLocation((double)lastMouseX, (double)lastMouseY);
	parentInvAffTr.transform(parentTransformTestPt, lastRotateMousePt);

	parentTransformTestPt.setLocation((double)x, (double)y);
	parentInvAffTr.transform(parentTransformTestPt, rotateMousePt);
	
	// NEED to figure what's going on here. should have to use radians, but
	// it rotate way behind mouse motion.
	double angle = /*MathDefines.DegToRad*/0.3 * (MathUtil.angleInXYPlane(lastRotateMousePt) -
		MathUtil.angleInXYPlane(rotateMousePt));
	// debug("rotating angle: " + angle);

	rotateAffTr.setToIdentity();
	rotateAffTr.rotate(angle, rotateAnchorPt.getX(), rotateAnchorPt.getY());
}

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

	super.runMousePressed(event); // if rht bt then resetCurrentVars gets hit
		// which will set boolean currentVarsSet

	// this.printConsole("Mouse State: " + complexSceneView.getCurrentMouseState());
	// this.printConsole("is shift down: " + event.isShiftDown());

	if ((complexSceneView.getCurrentMouseState() == 4) ||
		(complexSceneView.getCurrentMouseState() == 5)) // then right bt pressed
	{
		if (!this.currentVarsSet)
			return;
		rhtButtonPressed = true;
		boolean test = initTabScene();
		return;
	}

	// if not left button pressed then return
	if (!((complexSceneView.getCurrentMouseState() == 16) || // lft bt
		(complexSceneView.getCurrentMouseState() == 17)))  // lft bt + shift key
		return;

	if (event.isShiftDown())
		shiftKeyPressed = true;
	
	if (!this.resetCurrentVars(true))
		return;

	DrawObject drawObject = this.getCurrentDrawObject();
	if (drawObject == null)
		return;

	if (addNewStringLabelRB.isSelected())
	{
		runAddNewStringLabelEditMousePressed(drawObject);
	}
	else
	{
		this.setCursor(complexEditCursor);
		if (!initTabScene())
			this.setCursor(complexSceneView.complexDefaultCursor);

		// HERE 2
		// drawObject has been erased from viewport upon mouse press;
		// need to redraw while mouse is still pressed and before mouse
		// is dragged.
		this.drawCurrentDrawObject();
		complexSceneView.paintWindowView();
	}
}

public void
runMouseDragged(int x, int y)
throws Exception
{
	if (mouseReleased)
		return;
	if (rhtButtonPressed)
		return;
	if (this.getCurrentDrawObject() == null)
		return;
	this.runMoveEditMouseDragged(x, y);
}

public void
runMouseReleased()
throws Exception
{
	mouseReleased = true;
	super.runMouseReleased();
	if (rhtButtonPressed)
		return;

	this.setCursor(complexSceneView.complexDefaultCursor);
	DrawObject drawObject = this.getCurrentDrawObject();
	if (drawObject == null)
		return;

	runMoveEditMouseReleased(drawObject);
	
	// doesn't render very well, typically have to push render button
	// might try clearing view area only and then drawing view area only.
	// NEED to figure out clipping while drawing.
	// NEED to see if I can draw to a different image, like the image
	// that is just in viewport
	
	// too slow, and resets position during render
	// complexSceneView.renderDrawObjectView();
}

private boolean
checkNucLineConstraints()
throws Exception
{
	return (this.checkNucLineConstraints(this.getCurrentWorkNuc(),
		this.getCurrentWorkSingleStrand()));
}

public boolean
checkNucLineConstraints(Nuc2D refNuc, RNASingleStrand2D singleStrand)
throws Exception
{
	// check to see if non-natural delineator:
	if (refNuc.getIsSingleStrandDelineator())
	{
		if (singleStrand.getLinePartition() == MathDefines.LINE_PARTITION_MID)
		{
			alert("SOMETHING WRONG");
			return (false);
		}
		if (singleStrand.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
		{
			if (!refNuc.lastNuc().isSingleStrandDelineator())
			{
				alert("Single Strand delineator is ambiguous for this operation\nPick a non-delineating nuc");
				return (false);
			}
		}
		else if (singleStrand.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
		{
			if (!refNuc.nextNuc().isSingleStrandDelineator())
			{
				alert("Single Strand delineator is ambiguous for this operation\nPick a non-delineating nuc");
				return (false);
			}
		}
	}
	else
	{
		// check to see if moving a base pair
		if (singleStrand.getLinePartition() == MathDefines.LINE_PARTITION_MID)
		{
			if (singleStrand.getFivePrimeDelineateNuc2D().isBasePair())
			{
				alert("Can't translate since nuc: " + singleStrand.getFivePrimeDelineateNuc2D().getID() +
					" is a base pair.\nNeed to set another single strand delineate nuc to translate");
				return (false);
			}
			if (singleStrand.getThreePrimeDelineateNuc2D().isBasePair())
			{
				alert("Can't translate since nuc: " + singleStrand.getThreePrimeDelineateNuc2D().getID() +
					" is a base pair.\nNeed to set another single strand delineate nuc to translate");
				return (false);
			}
		}
		else if (singleStrand.getLinePartition() == MathDefines.LINE_PARTITION_TAIL)
		{
			if (singleStrand.getFivePrimeDelineateNuc2D().isBasePair())
			{
				alert("Can't translate since nuc: " + singleStrand.getFivePrimeDelineateNuc2D().getID() +
					" is a base pair.\nNeed to set another single strand delineate nuc to translate");
				return (false);
			}
		}
		else if (singleStrand.getLinePartition() == MathDefines.LINE_PARTITION_HEAD)
		{
			if (singleStrand.getThreePrimeDelineateNuc2D().isBasePair())
			{
				alert("Can't translate since nuc: " + singleStrand.getThreePrimeDelineateNuc2D().getID() +
					" is a base pair.\nNeed to set another single strand delineate nuc to translate");
				return (false);
			}
		}
	}
	return (true);
}

public boolean
initTabScene()
throws Exception
{
	// MIGHT WANT TO RECONSIDER:
	//
	// if (rhtButtonPressed)
		// return (false);
	//
	
	if (!super.initTabScene())
		return (false);

	// first constrain to see if valid translate edit

	// allow for all Labels to me moved no matter what contraint
	if (this.getCurrentWorkLabel() != null)
	{
		if (!rhtButtonPressed)
			this.getCurrentWorkLabel().runSetLocationHashtable(this.incCurrentUndoLevel());
	}
	else
	switch (this.getCurrentComplexPickMode()) // then constrain
	{
	  case ComplexDefines.InRNASingleNuc :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc to affect");
			return (false);
		}
		if (rhtButtonPressed)
			break;
	  	if (this.getCurrentWorkNuc().isBasePair())
		{
			alert("Must pick Non-BasePaired Nuc to affect");
			return (false);
		}
		this.getCurrentWorkNuc().runSetLocationHashtable(this.incCurrentUndoLevel());
	  	break;

	  case ComplexDefines.InRNASingleStrand :
	  	if ((this.getCurrentWorkNuc() == null) || this.getCurrentWorkNuc().isBasePair())
		{
			alert("Must pick Non-BasePaired Nuc in single strand to affect");
			return (false);
		}

	  	RNASingleStrand2D singleStrand = this.getCurrentWorkSingleStrand();

		// currently only constrain for left mouse edit
		if (rhtButtonPressed)
			break;
		Nuc2D refNuc = this.getCurrentWorkNuc();
		if (singleStrand.isStraightLine() || singleStrandFormatStraight_RB.isSelected())
		{
			boolean check = this.checkNucLineConstraints();
			if (!check)
				return (false); // NEED to be able to back out of UNDO here
			break;
		}
		else
		{
			if (singleStrand.isRefNucDelineator(refNuc))
			{
				alert("Need to pick nuc more towards center of arc");
				return (false);
			}
		}
		singleStrand.runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InRNABasePair :
	  	if ((this.getCurrentWorkNuc() == null) || !this.getCurrentWorkNuc().isBasePair())
		{
			alert("Must pick BasePaired Nuc in Helix to edit");
			return (false);
		}
		RNAHelix2D bpHelix = new RNAHelix2D(this.getCurrentWorkBasePair().getFivePrimeNuc2D());
		if ((!rhtButtonPressed) && (!bpHelix.isSingleBasePairHelix()))
		{
			alert("Can't left mouse edit a base pair that is contiguous\n" +
				" with other base pairs in a helix");
			return (false);
		}
		this.getCurrentWorkBasePair().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InRNAHelix :
	  	if ((this.getCurrentWorkNuc() == null) || !this.getCurrentWorkNuc().isBasePair())
		{
			alert("Must pick BasePaired Nuc in Helix to affect");
			return (false);
		}
		if (!rhtButtonPressed)
			this.getCurrentWorkHelix().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InRNAHelicalRun :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in stacked helix to affect");
			return (false);
		}
		if (!rhtButtonPressed)
			this.getCurrentWorkStackedHelix().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InRNASubDomain :
	  	if ((this.getCurrentWorkNuc() == null) || !this.getCurrentWorkNuc().isBasePair())
		{
			alert("Must pick BasePaired Nuc in sub-domain to affect");
			return (false);
		}
		if (!rhtButtonPressed)
			this.getCurrentWorkSubDomain().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InRNANamedGroup :
	  	if ((this.getCurrentWorkNuc() == null) || (this.getCurrentWorkNuc().getGroupName() == null))
		{
			alert("Must pick Nuc in a named group to affect");
			return (false);
		}
		if (this.getCurrentWorkNamedGroup().basePairsOutsideOfCollection())
		{
			alert("This collection of like named nucs base pairs outside\n" +
				"of collection and therefore can't be translated");
			return (false);
		}
		if (!rhtButtonPressed)
			this.getCurrentWorkNamedGroup().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InRNAColorUnit :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in color-unit to affect");
			return (false);
		}
		if (this.getCurrentWorkColorUnit().basePairsOutsideOfCollection())
		{
			alert("This collection of like colored nucs base pairs outside\n" +
				"of collection and therefore can't be translated");
			return (false);
		}
		if (!rhtButtonPressed)
			this.getCurrentWorkColorUnit().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InRNACycle :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in level to affect");
			return (false);
		}
		this.getCurrentWorkCycle().runSetLocationHashtable(this.incCurrentUndoLevel());
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
		// this.getCurrentWorkListNucs().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InRNASSData :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in a rna strand to affect");
			return (false);
		}
	  	if (this.getCurrentWorkSStr().interacts())
		{
			alert("This rna strand interacts with another strand;\n" +
				"use 'rna strand group' mode, 'rna named group' mode,\n" +
				"or 'rna color unit' mode to edit rna strand");
			return (false);
		}
		if (!rhtButtonPressed)
			this.getCurrentWorkSStr().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InComplex :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in complex to affect");
			return (false);
		}
		if (!rhtButtonPressed)
			this.getCurrentWorkRNASSComplex().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
	  case ComplexDefines.InLabelsOnly :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Label to affect");
			return (false);
		}
		if (!rhtButtonPressed)
			this.getCurrentWorkLabel().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;
		/*
	  case ComplexDefines.InComplexArea :
	  	if (this.getCurrentWorkNuc() == null)
		{
			alert("Must pick Nuc in an area to affect");
			return (false);
		}
		break;
		*/
	  case ComplexDefines.InComplexScene :
		if (!rhtButtonPressed)
			this.getCurrentWorkComplexScene().runSetLocationHashtable(this.incCurrentUndoLevel());
		break;

	  default :
	  	break;
	}

	// erase drawObject from sceneImg
	if (this.getCurrentWorkLabel() != null)
	{
		editErase(this.getCurrentWorkLabel(), ComplexDefines.InLabelsOnly);
	}
	else
	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		editErase(this.getCurrentWorkNuc(), ComplexDefines.InRNASingleNuc);
	  	break;
	  case ComplexDefines.InRNASingleStrand :
	  	RNASingleStrand2D singleStrand = this.getCurrentWorkSingleStrand();

		/* LEAVE FOR AWHILE:
		if (singleStrandFormatStraight_RB.isSelected())
			singleStrand.setFormattedArced(false);
		else
			singleStrand.setFormattedArced(((formatArcedRB != null) &&
				formatArcedRB.isSelected()) || singleStrand.isArc());
		*/

		editErase(singleStrand, ComplexDefines.InRNASingleStrand);
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
		/*
	  case ComplexDefines.InComplexArea :
		editErase(this.getCurrentWorkComplexArea(), ComplexDefines.InComplexArea);
		break;
		*/
	  case ComplexDefines.InComplexScene :
		// NOT SURE yet what this implies
		// editErase(this.getCurrentWorkComplexScene(), ComplexDefines.InComplexScene);
		//
		break;
	  default :
	  	break;
	}

	this.initTabGraphics();

	// HERE 3
	this.updateSceneImgG2Clip();

	// do any extra init before updateEditColor
	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNABasePair :
		RNAHelix2D bpHelix = new RNAHelix2D(this.getCurrentWorkBasePair().getFivePrimeNuc2D());
		if (rhtButtonPressed && !bpHelix.isSingleBasePairHelix())
		{
			alert("Warning: any editing of a base pair contiguous with\n" +
				"other base pairs in a helix will be overwritten by a\n" +
				"subsequent reformat of that helix");
		}
		/* Not sure I want this to stay; need to make sure this
		 * only happens to isSingleBasePairHelix
		else if (shiftKeyPressed)
		{
			this.initRotateTransform();
			rotateAnchorPt.setLocation(
				bpHelix.getFivePrimeMidPt().getX(),
				bpHelix.getFivePrimeMidPt().getY());
		}
		*/
		break;
	  case ComplexDefines.InRNAHelix :
		if (shiftKeyPressed)
		{
			this.initRotateTransform();
			rotateAnchorPt.setLocation(
				this.getCurrentWorkHelix().getFivePrimeMidPt().getX(),
				this.getCurrentWorkHelix().getFivePrimeMidPt().getY());
		}
		break;
	  case ComplexDefines.InRNAHelicalRun :
	  	if (shiftKeyPressed)
		{
			this.initRotateTransform();
			rotateAnchorPt.setLocation(
				this.getCurrentWorkStackedHelix().getStartHelix2D().getFivePrimeMidPt().getX(),
				this.getCurrentWorkStackedHelix().getStartHelix2D().getFivePrimeMidPt().getY());
		}
		break;
	  case ComplexDefines.InRNASubDomain :
		if (shiftKeyPressed)
		{
			this.initRotateTransform();
			rotateAnchorPt.setLocation(
				this.getCurrentWorkSubDomain().getStartHelix2D().getFivePrimeMidPt().getX(),
				this.getCurrentWorkSubDomain().getStartHelix2D().getFivePrimeMidPt().getY());
		}
		break;
	  default :
	  	break;
	}
	this.updateEditColor(this.getCurrentComplexPickMode(), complexSceneView.moveableEditingColor, sceneImgG2); 

	return (true);
}

// I think getOffScreenImg is small viewport image minus stuff being moved
public void
runMoveEditMouseDragged(int x, int y)
throws Exception
{
	if (rhtButtonPressed)
		return;
	if ((lastMouseX - x == 0) && (lastMouseY - y == 0))
		return;
	if (complexSceneView.getOffScreenImg() == null)
		return;

	// HERE 0

	// draw offscreen img that has cleared out drwObject
	this.drawOffScreenImg();

	// MAYBE put code back here rather than in sub-routine
	drawShiftDrawObject(lastMouseX, lastMouseY, x, y);

	/* SAVE FOR DRAWING IMG WITH XOR
	if (this.getCurrentWorkNuc() != null)
	{
		this.getCurrentWorkNuc().shiftXY(
			((double)(lastMouseX - x)) / figureScale,
			-((double)(lastMouseY - y)) / figureScale);
		this.getCurrentWorkNuc().draw(sceneImgG2, null);
		
		// SAVE if go back to drawing with img rather than graphical primitive
		// moveEditXORG2.drawImage(this.getCurrentWorkNuc().getSceneImg(),
			// moveEditAffTranOp, x + mouseDeltaX, y + mouseDeltaY);
		//
	}
	else if (this.getCurrentWorkLabel() != null)
	{
		this.getCurrentWorkLabel().shiftXY(
			((double)(lastMouseX - x)) / figureScale,
			-((double)(lastMouseY - y)) / figureScale);
		this.getCurrentWorkLabel().draw(sceneImgG2, null);
	}
	*/

	lastMouseX = x;
	lastMouseY = y;

	complexSceneView.paintWindowView();
}

public void
drawShiftDrawObject(int lastMouseX, int lastMouseY, int x, int y)
throws Exception
{
	// allow for all Labels to me moved no matter what constraint
	if (this.getCurrentWorkLabel() != null)
	{
		DrawObject label = this.getCurrentWorkLabel();
		label.shiftXY(((double)(lastMouseX - x)) / figureScale,
			-((double)(lastMouseY - y)) / figureScale);

		// this needs to be the case for at least DrawTriangleObject,
		// but is expensive:
		label.update(sceneImgG2);

		label.draw(sceneImgG2, null);
	}
	else
	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		Nuc2D nuc = this.getCurrentWorkNuc();
		nuc.setX(nuc.getX() - ((double)(lastMouseX - x)/figureScale));
		nuc.setY(nuc.getY() + ((double)(lastMouseY - y)/figureScale));
		nuc.draw(sceneImgG2, null);
	  	break;
	  case ComplexDefines.InRNASingleStrand :
	  	RNASingleStrand2D singleStrand = this.getCurrentWorkSingleStrand();

	  	// NEED to see if straight line or arc (always consider a singlestrand
		// with only one nuc as formatting an arc)
		/* LEAVE FOR AWHILE:
		if (singleStrand.getFormattedArced()) // reformat an arc
		*/
		if (singleStrandFormatStraight_RB.isSelected() ||
			singleStrand.isStraightLine()) // reformat a straight nuc line
		{
			try
			{
				singleStrand.formatEditNucLine(
					((double)(lastMouseX - x)) / figureScale,
					-((double)(lastMouseY - y)) / figureScale, true);
			}
			catch (ComplexException ce)
			{
				if ((ce.getErrorMsg() != null) && (ce.getComment() != null))
					alert(ce.getErrorMsg() + "\n" + ce.getComment());
				else if (ce.getErrorMsg() != null)
					alert(ce.getErrorMsg());
				runMouseReleased();
			}
		}
		else
		{
			// try just moving nuc and then reformatting arc to nuc
			singleStrand.reformatEditArc(this.getCurrentWorkNuc(),
				((double)(lastMouseX - x)) / figureScale,
				-((double)(lastMouseY - y)) / figureScale);
		}
		singleStrand.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNABasePair :
		RNAHelix2D bpHelix = new RNAHelix2D(this.getCurrentWorkBasePair().getFivePrimeNuc2D());
		if (!bpHelix.isSingleBasePairHelix())
		{
			alert("0 Can't edit a base pair contiguous with other base pairs in a helix");
		}
		else if (shiftKeyPressed)
		{
			this.setRotateTransform(lastMouseX, lastMouseY, x, y);
			bpHelix.transform(rotateAffTr);
		}
		else
		{
			bpHelix.setX(bpHelix.getX() - ((double)(lastMouseX - x)/figureScale));
			bpHelix.setY(bpHelix.getY() + ((double)(lastMouseY - y)/figureScale));
		}
		bpHelix.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNAHelix :
		RNAHelix2D helix = this.getCurrentWorkHelix();
		if (shiftKeyPressed)
		{
			this.setRotateTransform(lastMouseX, lastMouseY, x, y);
			helix.transform(rotateAffTr);
		}
		else
		{
			helix.setX(helix.getX() - ((double)(lastMouseX - x)/figureScale));
			helix.setY(helix.getY() + ((double)(lastMouseY - y)/figureScale));
		}
		helix.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNAHelicalRun :
		RNAStackedHelix2D stackedHelix = this.getCurrentWorkStackedHelix();
	  	if (shiftKeyPressed)
		{
			this.setRotateTransform(lastMouseX, lastMouseY, x, y);
			stackedHelix.transform(rotateAffTr);
		}
		else
		{
			stackedHelix.setX(stackedHelix.getX() -
				((double)(lastMouseX - x)/figureScale));
			stackedHelix.setY(stackedHelix.getY() +
				((double)(lastMouseY - y)/figureScale));
		}
		stackedHelix.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNASubDomain :
		RNASubDomain2D subDomain = this.getCurrentWorkSubDomain();
	  	if (shiftKeyPressed)
		{
			this.setRotateTransform(lastMouseX, lastMouseY, x, y);
			subDomain.transform(rotateAffTr);
		}
		else
		{
			subDomain.setX(subDomain.getX() -
				((double)(lastMouseX - x)/figureScale));
			subDomain.setY(subDomain.getY() +
				((double)(lastMouseY - y)/figureScale));
		}
		subDomain.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNANamedGroup :
		RNANamedGroup2D namedGroup = this.getCurrentWorkNamedGroup();
		namedGroup.shiftXY(
			((double)(lastMouseX - x)) / figureScale,
			-((double)(lastMouseY - y)) / figureScale);
		namedGroup.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNAColorUnit :
		RNAColorUnit2D colorUnit = getCurrentWorkColorUnit();
		colorUnit.shiftXY(
			((double)(lastMouseX - x)) / figureScale,
			-((double)(lastMouseY - y)) / figureScale);
		colorUnit.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNACycle :
		/*
		this.getCurrentWorkCycle().shiftXY(
			((double)(lastMouseX - x)) / figureScale,
			-((double)(lastMouseY - y)) / figureScale);
		*/
		alert("Can't translate in RNA Cycle constraint mode; use RNASubDomain");
		this.getCurrentWorkCycle().draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNAListNucs :
		// THIS won't work yet till get concept of picking and then
		// interactively editing. Currently pick with properties window
		// which doesn't allow for interactive
		RNAListNucs2D listNucs = this.getCurrentWorkListNucs();
		if (!listNucs.isSet())
			break;
		if (listNucs.containsNonSelfRefBasePairs())
		{
			// NEED to determine if listNucs has bp's that will pull
			// apart on editing regardless if the bp's are self ref
			// or not.
		}
		else
		{
			listNucs.shiftXY(
				((double)(lastMouseX - x)) / figureScale,
				-((double)(lastMouseY - y)) / figureScale);
		}
		listNucs.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNASSData :
		SSData2D sstr = getCurrentWorkSStr();
		sstr.shiftXY(
			((double)(lastMouseX - x)) / figureScale,
			-((double)(lastMouseY - y)) / figureScale);
		sstr.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InComplex :
		ComplexSSDataCollection2D complex = this.getCurrentWorkRNASSComplex();
		complex.shiftXY(
			((double)(lastMouseX - x)) / figureScale,
			-((double)(lastMouseY - y)) / figureScale);
		complex.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InLabelsOnly :
		// dealing with up above before start of switch statement
		break;
		/*
	  case ComplexDefines.InComplexArea :
		RNAComplexArea2D complexArea = this.getCurrentWorkComplexArea();
		complexArea.setX(complexArea.getX() -
			((double)(lastMouseX - x)/figureScale));
		complexArea.setY(complexArea.getY() +
			((double)(lastMouseY - y)/figureScale));
		complexArea.draw(sceneImgG2, null);
		break;
		*/
	  case ComplexDefines.InComplexScene :
		/* NOT SURE yet what this implies
		ComplexScene2D complexScene = this.getCurrentWorkComplexScene();
		complexScene.shiftXY(
			((double)(lastMouseX - x)) / figureScale,
			-((double)(lastMouseY - y)) / figureScale);
		complexScene.draw(sceneImgG2, null);
		*/
		break;
	  default :
	  	break;
	}
}

public void
drawCurrentDrawObject()
throws Exception
{
	// allow for all Labels to me moved no matter what constraint
	if (this.getCurrentWorkLabel() != null)
	{
		DrawObject label = this.getCurrentWorkLabel();

		// this needs to be the case for at least DrawTriangleObject,
		// but is expensive:
		label.update(sceneImgG2);

		label.draw(sceneImgG2, null);
	}
	else
	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		Nuc2D nuc = this.getCurrentWorkNuc();
		// nuc.update(sceneImgG2);
		nuc.draw(sceneImgG2, null);
	  	break;
	  case ComplexDefines.InRNASingleStrand :
	  	RNASingleStrand2D singleStrand = this.getCurrentWorkSingleStrand();
		singleStrand.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNABasePair :
		RNAHelix2D bpHelix = new RNAHelix2D(this.getCurrentWorkBasePair().getFivePrimeNuc2D());
		bpHelix.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNAHelix :
		RNAHelix2D helix = this.getCurrentWorkHelix();
		helix.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNAHelicalRun :
		RNAStackedHelix2D stackedHelix = this.getCurrentWorkStackedHelix();
		stackedHelix.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNASubDomain :
		RNASubDomain2D subDomain = this.getCurrentWorkSubDomain();
		subDomain.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNANamedGroup :
		RNANamedGroup2D namedGroup = this.getCurrentWorkNamedGroup();
		namedGroup.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNAColorUnit :
		RNAColorUnit2D colorUnit = getCurrentWorkColorUnit();
		colorUnit.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNACycle :
		this.getCurrentWorkCycle().draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNAListNucs :
		// THIS won't work yet till get concept of picking and then
		// interactively editing. Currently pick with properties window
		// which doesn't allow for interactive
		RNAListNucs2D listNucs = this.getCurrentWorkListNucs();
		if (!listNucs.isSet())
			break;
		listNucs.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InRNASSData :
		SSData2D sstr = getCurrentWorkSStr();
		sstr.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InComplex :
		ComplexSSDataCollection2D complex = this.getCurrentWorkRNASSComplex();
		complex.draw(sceneImgG2, null);
		break;
	  case ComplexDefines.InLabelsOnly :
		// dealing with up above before start of switch statement
		break;
	  case ComplexDefines.InComplexScene :
		// NOT SURE yet what this implies
		ComplexScene2D complexScene = this.getCurrentWorkComplexScene();
		complexScene.draw(sceneImgG2, null);
		break;
	  default :
	  	break;
	}
}

private void
runMoveEditMouseReleased(DrawObject drawObject)
throws Exception
{
	if (rhtButtonPressed)
		return;

	/* NEED to always do this method to undo edit color scheme
	if ((complexSceneView.getCurrentImgSpaceX() -
			complexSceneView.getMousePressedImgSpaceX() == 0) &&
		(complexSceneView.getCurrentImgSpaceY() -
			complexSceneView.getMousePressedImgSpaceY() == 0))
		return;
	*/

	/* SAVE if go back to drawing with images rather than graphical primitives
	double shiftX = 0.0;
	double shiftY = 0.0;
	if (this.getCurrentWorkNuc() != null)
	{
		shiftX = ((double)(complexSceneView.getMousePressedImgSpaceX() -
			complexSceneView.getCurrentImgSpaceX() -
			((DrawFontObject)this.getCurrentWorkNuc().getNucDrawObject()).getStrBBoxXDiff()) + 1.0)/
				figureScale;
		shiftY = -((double)(complexSceneView.getMousePressedImgSpaceY() -
			complexSceneView.getCurrentImgSpaceY() +
			((DrawFontObject)this.getCurrentWorkNuc().getNucDrawObject()).getStrBBoxYDiff()) + 1.0)/
				figureScale;
	}
	else if (drawObject instanceof DrawFontObject)
	{
		shiftX = ((double)(complexSceneView.getMousePressedImgSpaceX() -
			complexSceneView.getCurrentImgSpaceX() -
			((DrawFontObject)drawObject).getStrBBoxXDiff()) + 1.0)/
				figureScale;
		shiftY = -((double)(complexSceneView.getMousePressedImgSpaceY() -
			complexSceneView.getCurrentImgSpaceY() +
			((DrawFontObject)drawObject).getStrBBoxYDiff()) + 1.0)/
				figureScale;
	}
	drawObject.shiftX(shiftX);
	drawObject.shiftY(shiftY);
	*/

	if (this.getCurrentWorkLabel() != null)
	{
		this.updateEditColor(this.getCurrentWorkLabel(), null, sceneImgG2);

		// not sure why this needs to be here, possibly since it is
		// a raw drawobject and not wrapped up like a Nuc2D is:
		this.getCurrentWorkLabel().update(sceneImgG2);
	}
	else
	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		break;
	  case ComplexDefines.InRNASingleStrand :
		// this.getCurrentWorkSingleStrand().update(sceneImgG2);
		// THIS WORKS, but is way too slow. (preliminary tests, though,
		// seems to suggest that update is fast. tried out e.coli 23s
		// with both 5' and 3' and can edit a nuc line and repick and
		// edit very fast).
		
		// THIS MAY BE A SOURCE OF REDRAW PROBLEMS
		this.getCurrentWorkComplexScene().update(sceneImgG2);
		break;
	  case ComplexDefines.InRNABasePair :
		break;
	  case ComplexDefines.InRNAHelix :
		break;
	  case ComplexDefines.InRNAHelicalRun :
		break;
	  case ComplexDefines.InRNASubDomain :
		break;
	  case ComplexDefines.InRNANamedGroup :
		break;
	  case ComplexDefines.InRNAColorUnit :
		break;
	  case ComplexDefines.InRNACycle :
		break;
	  case ComplexDefines.InRNAListNucs :
		break;
	  default :
		break;
	}

	// HERE 1
	this.drawOffScreenImg();
	// draw final for after mouse release; need to not clip at this point
	sceneImgG2.setClip(null);
	this.drawCurrentDrawObject();
	complexSceneView.paintWindowView();
}

private void
runMoveEditKeyPressed(DrawObject drawObject, int keyCode)
throws Exception
{
	if (drawObject == null)
	{
		alert("Must pick an object to move with keys");
		return;
	}
	
	if (editClearPaintG2 == null)
	{
		alert("editClearPaintG2 null");
		return;
	}
	
	if (moveEditAffTranOp == null)
	{
		alert("moveEditAffTranOp null");
		return;
	}
	
	/*
	// draw offscreen img that has cleared out nuc
	this.drawOffScreenImg();
	*/

	double shiftX = 0.0;
	double shiftY = 0.0;
	double unitAmt = 1.0/figureScale;
	boolean helixAxisDirection = true;

	switch (keyCode)
	{
	  case 38 : // up arrow
	  	shiftX = 0.0;
	  	shiftY = unitAmt;
		helixAxisDirection = true;
		break;
	  case 33 : // up rht arrow
	  	shiftX = unitAmt;
	  	shiftY = unitAmt;
		helixAxisDirection = true;
		break;
	  case 39 : // rht arrow
	  	shiftX = unitAmt;
	  	shiftY = 0.0;
		helixAxisDirection = true;
		break;
	  case 34 : // down rht arrow
	  	shiftX = unitAmt;
	  	shiftY = unitAmt;
		helixAxisDirection = false;
		break;
	  case 40 : // down arrow
	  	shiftX = 0.0;
	  	shiftY = -unitAmt;
		helixAxisDirection = false;
		break;
	  case 35 : // down lft arrow
	  	shiftX = -unitAmt;
	  	shiftY = -unitAmt;
		helixAxisDirection = false;
		break;
	  case 37 : // lft arrow
	  	shiftX = -unitAmt;
	  	shiftY = 0.0;
		helixAxisDirection = false;
		break;
	  case 36 : // up lft arrow
	  	shiftX = -unitAmt;
	  	shiftY = unitAmt;
		helixAxisDirection = false;
		break;
	  default :
		// debug("IN DEF MV WITH KEY");
	  	shiftX = 0.0;
	  	shiftY = 0.0;
		break;
	}

	if (this.getCurrentWorkLabel() != null)
	{
		DrawObject label = this.getCurrentWorkLabel();
		if (keyInitiallyPressed)
			label.runSetLocationHashtable(this.incCurrentUndoLevel());
		label.shiftXY(-shiftX, -shiftY);
		animateEdit(label);
	}
	else
	{
	// debug("SHIFTING COMP: " + this.getCurrentComplexPickMode());
	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		Nuc2D nuc = this.getCurrentWorkNuc();
		if (nuc == null)
			break;
		if (keyInitiallyPressed)
			nuc.runSetLocationHashtable(this.incCurrentUndoLevel());
		nuc.shiftXY(-shiftX, -shiftY);
		animateEdit(nuc);
		break;
	  case ComplexDefines.InRNASingleStrand :
		if (this.getCurrentWorkSingleStrand() == null)
			break;
		RNASingleStrand2D singleStrand = this.getCurrentWorkSingleStrand();
		if (keyInitiallyPressed)
			singleStrand.runSetLocationHashtable(this.incCurrentUndoLevel());
		/* LEAVE FOR AWHILE:
		if (singleStrand.getFormattedArced()) // reformat an arc
		*/
		if (singleStrandFormatStraight_RB.isSelected() ||
			singleStrand.isStraightLine()) // reformat a straight nuc line
		{
			singleStrand.formatEditNucLine(-shiftX, -shiftY, true);
		}
		else
		{
			// try just moving nuc and then reformatting arc to nuc
			singleStrand.reformatEditArc(this.getCurrentWorkNuc(),
				-shiftX, -shiftY);
		}
		animateEdit(singleStrand);
		break;
	  case ComplexDefines.InRNABasePair :
		if (this.getCurrentWorkBasePair() == null)
			break;
		RNAHelix2D bpHelix = new RNAHelix2D(this.getCurrentWorkBasePair().getFivePrimeNuc2D());
		if (keyInitiallyPressed)
			bpHelix.runSetLocationHashtable(this.incCurrentUndoLevel());
		if (!bpHelix.isSingleBasePairHelix())
			alert("1 Can't edit a base pair contiguous with other base pairs in a helix");
		if (moveAlongAxisEditModeRB.isSelected())
			bpHelix.shiftDistance(unitAmt, helixAxisDirection);
		else
			bpHelix.shiftXY(-shiftX, -shiftY);
		bpHelix.draw(sceneImgG2, null);
		animateEdit(bpHelix);
		break;
	  case ComplexDefines.InRNAHelix :
		if (this.getCurrentWorkHelix() == null)
			break;
		if (keyInitiallyPressed)
			this.getCurrentWorkHelix().runSetLocationHashtable(this.incCurrentUndoLevel());
		if (moveAlongAxisEditModeRB.isSelected())
			this.getCurrentWorkHelix().shiftDistance(unitAmt, helixAxisDirection);
		else
			this.getCurrentWorkHelix().shiftXY(-shiftX, -shiftY);
		this.getCurrentWorkHelix().draw(sceneImgG2, null);
		animateEdit(this.getCurrentWorkHelix());
		break;
	  case ComplexDefines.InRNAHelicalRun :
		if (this.getCurrentWorkStackedHelix() == null)
			break;
		if (keyInitiallyPressed)
			this.getCurrentWorkStackedHelix().runSetLocationHashtable(this.incCurrentUndoLevel());
		if (moveAlongAxisEditModeRB.isSelected())
			this.getCurrentWorkStackedHelix().shiftDistance(unitAmt, helixAxisDirection);
		else
			this.getCurrentWorkStackedHelix().shiftXY(-shiftX, -shiftY);
		animateEdit(this.getCurrentWorkStackedHelix());
		break;
	  case ComplexDefines.InRNASubDomain :
		if (this.getCurrentWorkSubDomain() == null)
			break;
		if (keyInitiallyPressed)
			this.getCurrentWorkSubDomain().runSetLocationHashtable(this.incCurrentUndoLevel());
		if (moveAlongAxisEditModeRB.isSelected())
			this.getCurrentWorkSubDomain().shiftDistance(unitAmt, helixAxisDirection);
		else
			this.getCurrentWorkSubDomain().shiftXY(-shiftX, -shiftY);
		animateEdit(this.getCurrentWorkSubDomain());
		break;
	  case ComplexDefines.InRNANamedGroup :
		if (this.getCurrentWorkNamedGroup() == null)
			break;
		if (keyInitiallyPressed)
			this.getCurrentWorkNamedGroup().runSetLocationHashtable(this.incCurrentUndoLevel());
		this.getCurrentWorkNamedGroup().shiftXY(-shiftX, -shiftY);
		animateEdit(this.getCurrentWorkNamedGroup());
		break;
	  case ComplexDefines.InRNAColorUnit :
		if (this.getCurrentWorkColorUnit() == null)
			break;
		if (keyInitiallyPressed)
			this.getCurrentWorkColorUnit().runSetLocationHashtable(this.incCurrentUndoLevel());
		this.getCurrentWorkColorUnit().shiftXY(-shiftX, -shiftY);
		animateEdit(this.getCurrentWorkColorUnit());
		break;
	  case ComplexDefines.InRNACycle :
		return;
	  case ComplexDefines.InRNAListNucs :
		if (this.getCurrentWorkListNucs() == null)
			break;
		if (!this.getCurrentWorkListNucs().isSet())
			return;
		if (keyInitiallyPressed)
			this.getCurrentWorkListNucs().runSetLocationHashtable(this.incCurrentUndoLevel());
		animateEdit(this.getCurrentWorkListNucs());
		return;
	  case ComplexDefines.InRNASSData :
	  	if (this.getCurrentWorkSStr() == null)
			break;
		if (keyInitiallyPressed)
			this.getCurrentWorkSStr().runSetLocationHashtable(this.incCurrentUndoLevel());
	  	this.getCurrentWorkSStr().shiftXY(-shiftX, -shiftY);
		animateEdit(this.getCurrentWorkSStr());
		break;
	  case ComplexDefines.InComplex :
		alert("Not Yet");
		return;
	  case ComplexDefines.InLabelsOnly :
		alert("Not Yet");
		return;
	  case ComplexDefines.InComplexScene :
		alert("Not Yet");
		return;
	  default :
	  	alert("SOMETHING WRONG");
		break;
	}
	}

	// complexSceneView.paintWindowView();
}

private void
runMoveEditKeyReleased(DrawObject drawObject, int keyCode)
throws Exception
{
	if (this.getCurrentDrawObject() != null)
		this.getCurrentDrawObject().update(sceneImgG2);

	this.drawOffScreenImg();
	// draw final for after mouse release; need to not clip at this point
	sceneImgG2.setClip(null);
	this.drawCurrentDrawObject();
	complexSceneView.paintWindowView();
}

private void
runAddNewStringLabelEditMousePressed(DrawObject drawObject)
throws Exception
{
	this.printConsole(this.printPickMode("to add new string label"));
}

private void
redrawBasePairDistance(double distance)
throws Exception
{
	BLine2D bpLine = new BLine2D(
		getCurrentWorkBasePair().get5PBasePairRay().getMidPt(),
		getCurrentWorkBasePair().get5PBasePairRay().getP1());
	Point2D new5PPt = bpLine.ptAtDistance(0.5 * distance);

	bpLine.setLine(
		getCurrentWorkBasePair().get5PBasePairRay().getMidPt(),
		getCurrentWorkBasePair().get5PBasePairRay().getP2());
	Point2D new3PPt = bpLine.ptAtDistance(0.5 * distance);

	getCurrentWorkBasePair().getFivePrimeNuc2D().setX(new5PPt.getX());
	getCurrentWorkBasePair().getFivePrimeNuc2D().setY(new5PPt.getY());
	getCurrentWorkBasePair().getThreePrimeNuc2D().setX(new3PPt.getX());
	getCurrentWorkBasePair().getThreePrimeNuc2D().setY(new3PPt.getY());

	bpDistanceTF.setText("" + StringUtil.roundStrVal(
		getCurrentWorkBasePair().get5PBasePairRay().length(), 2));
	complexSceneView.runRenderBt();
}

private void
redrawCycleRadius(double radius)
throws Exception
{
	if (cycleCircle == null)
		return;
	cycleCircle.setRadius(radius);
	resetCycle.resetCycleCircle(cycleCircle);
	resetCycle.partialReset(true);
	if (resetCycleHelicesAnglesRB.isSelected())
		resetCycle.formatCycle(cycleCircle.getCenterPt(), true, false,
			!resetCycleHelicesAnglesRB.isSelected());
	if (levelRefHelix != null)
		domainRotateCycleAngle_TF.setText("" + StringUtil.roundStrVal(levelRefHelix.getAngle(), 2));
	complexSceneView.runRenderBt();
}

private void
redrawCycleDomain(double newDomainCycleAngle)
throws Exception
{
	if (cycleCircle == null)
		return;
	RNASubDomain2D subDomain = new RNASubDomain2D(levelRefHelix);
	AffineTransform affTr = new AffineTransform();
	affTr.setToIdentity();
	affTr.rotate(MathDefines.DegToRad *
		(newDomainCycleAngle - levelRefHelix.getAngle()),
		cycleCircle.getX(), cycleCircle.getY());
	subDomain.transform(affTr);

	Nuc2D refNuc = levelRefHelix.getFivePrimeStartNuc2D().lastNuc2D();
	boolean clockWiseFormatted = levelRefHelix.isClockWiseFormatted();
	if ((refNuc != null) && refNuc.isSingleStranded())
		redrawCycleSingleStrandAt(refNuc, clockWiseFormatted);
	refNuc = levelRefHelix.getThreePrimeEndNuc2D().nextNuc2D();
	if ((refNuc != null) && refNuc.isSingleStranded())
		redrawCycleSingleStrandAt(refNuc, clockWiseFormatted);
	domainRotateCycleAngle_TF.setText("" + StringUtil.roundStrVal(newDomainCycleAngle, 2));
}

private void
redrawCycleSingleStrandAt(Nuc2D refNuc, boolean clockWiseFormatted)
throws Exception
{
	if (cycleCircle == null)
		return;
	RNASingleStrand2D singleStrand = new RNASingleStrand2D(refNuc);
	double radius = cycleCircle.getCenterPt().distance(
		resetCycle.getEntryHelix2D().getThreePrimeStartNuc2D().getPoint2D());
	singleStrand.formatArc(cycleCircle.getCenterPt(),
		radius, clockWiseFormatted);
}

private void
redrawCircleObjectTFs(double x, double y, double radius, double startAngle, double angleExtent)
throws Exception
{
	DrawCircleObject circleObj = (DrawCircleObject)getCurrentWorkLabel();
	Graphics2D g2 = complexSceneView.getCurrentGraphics2D();
	g2.setTransform(circleObj.getParentG2Transform());
	editErase(circleObj, ComplexDefines.InLabelsOnly);
	circleObj.setRadius(radius);
	circleObj.setLocation(x, y);
	circleObj.setStartAngle(startAngle);
	circleObj.setAngleExtent(angleExtent);
	circleObj.update(g2);
	editRedraw(circleObj, g2);
	circle_Radius_TF.setText(StringUtil.roundStrVal(circleObj.getRadius(), 2));
}

private void
redrawTriangleObjectTFs(double x, double y, double width, double height, double angle)
throws Exception
{
	DrawTriangleObject triangleObj = (DrawTriangleObject)getCurrentWorkLabel();
	Graphics2D g2 = complexSceneView.getCurrentGraphics2D();
	g2.setTransform(triangleObj.getParentG2Transform());
	editErase(triangleObj, ComplexDefines.InLabelsOnly);
	triangleObj.setX(x);
	triangleObj.setY(y);
	triangleObj.setBaseWidth(width);
	triangleObj.setHeight(height);
	triangleObj.setAngle(angle);
	triangleObj.update(g2);
	editRedraw(triangleObj, g2);
}

private void
redrawParallelogramObjectTFs(double x, double y, double side1, double angle1, double side2, double angle2)
throws Exception
{
	DrawParallelogramObject parallelogramObj = (DrawParallelogramObject)getCurrentWorkLabel();
	Graphics2D g2 = complexSceneView.getCurrentGraphics2D();
	g2.setTransform(parallelogramObj.getParentG2Transform());
	editErase(parallelogramObj, ComplexDefines.InLabelsOnly);
	parallelogramObj.setX(x);
	parallelogramObj.setY(y);
	parallelogramObj.setAngle1(angle1);
	parallelogramObj.setSide1(side1);
	parallelogramObj.setAngle2(angle2);
	parallelogramObj.setSide2(side2);
	parallelogramObj.update(g2);
	editRedraw(parallelogramObj, g2);
}

private void
redrawArrowObjectTFs(double x, double y, double height, double baseWidth,
	double tailLength, double tailWidth, double baseIndent, double angle)
throws Exception
{
	DrawArrowObject arrowObj = (DrawArrowObject)getCurrentWorkLabel();
	Graphics2D g2 = complexSceneView.getCurrentGraphics2D();
	g2.setTransform(arrowObj.getParentG2Transform());
	editErase(arrowObj, ComplexDefines.InLabelsOnly);
	arrowObj.setX(x);
	arrowObj.setY(y);
	arrowObj.setHeight(height);
	arrowObj.setBaseWidth(baseWidth);
	arrowObj.setTailLength(tailLength);
	arrowObj.setTailWidth(tailWidth);
	arrowObj.setBaseIndent(baseIndent);
	arrowObj.setAngle(angle);
	arrowObj.update(g2);
	editRedraw(arrowObj, g2);
}

public boolean
resetCurrentVars(boolean constrain)
throws Exception
{
	// undo edit coloring if possible
	if ((this.currentWorkDrawObject != null) && (this.currentWorkDrawObject.getIsEditable()))
	{
		if ((currentWorkDrawObject == this.getCurrentWorkListNucs()) &&
			!this.getCurrentWorkListNucs().isSet())
			return (super.resetCurrentVars(constrain));
		this.undoEditColorScheme();
	}
	
	return (super.resetCurrentVars(constrain));
}

/*************  END MOUSE STUFF *********************/

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
			// reDraw();
			//
		}
	});	
}

/*
public static final String clearImgString =
"474946383961D2008400F7000000217300217B00297B00317B00318400318C00398408" +
"318408398408398C10397B10398410398C10428C18397B18427B18428C184A8C214273";
*/

private void
makeCursors()
{
	Toolkit toolKit = Toolkit.getDefaultToolkit();
	// Image cursorImg = toolKit.getImage("../images/clear_pixel.gif");
	// debug("cursorImg: " + cursorImg);
	
	// byte gifBytes[] = MathOps.convertHexToByte(imgString);
	// Image cursorImg = toolKit.createImage(MathUtil.convertHexToByte(clearImgString));


	URL clearImgURL = this.getClass().getResource("/images/clear_pixel.gif");
	Image cursorImg = toolKit.createImage(clearImgURL);

	complexEditCursor = toolKit.createCustomCursor((Image)cursorImg,
		new Point(0, 0), "ComplexCursor");
}

public void
printEditTabPickMode()
throws Exception
{
	if (moveAlongAxisEditModeRB.isSelected())
		printConsole(printPickMode("to shift along axis"));
	else
		printConsole(printPickMode("to move with mouse"));
}

private void
setBestGuessNucLabelFont()
{
	Font bestGuessFont = this.getBestGuessNucLabelFont();
	if (bestGuessFont != null)
		complexSceneView.fontChooser.setCurrentFont(bestGuessFont);
}

private Font
getBestGuessNucLabelFont()
{
	// first look for a neighbor with a nuc label

	if ((this.getCurrentWorkNuc() == null) &&
		(this.getCurrentComplexPickMode() == ComplexDefines.InComplexScene) &&
		(complexSceneView.getComplexScene() != null))

	{
		return (complexSceneView.getComplexScene().getBestGuessNucLabelFont());
	}

	SSData2D sstr = this.getCurrentWorkNuc().getParentSSData2D();
	Font sstrBestGuessFont = sstr.getBestGuessNucLabelFont(this.getCurrentWorkNuc().getID());
	if (sstrBestGuessFont != null)
		return (sstrBestGuessFont);

	return (new Font("Helvetica", Font.PLAIN, ComplexDefines.DEFAULT_NUC_FONT_SIZE));
}

/************** PropertyChangeListener Implementaion ***************/

public void
propertyChange(PropertyChangeEvent evt)
{
	super.propertyChange(evt);

	String propertyName = evt.getPropertyName();

	if (propertyName == null)
		return;

	if (propertyName.equals("CurrentComplexPickMode"))
	{
		try
		{
			this.printEditTabPickMode();
		}
		catch (Exception e)
		{
			debug("Error in ComplexSceneEditTab.propertyChange().updateUserMsg(): " + e.toString());
			ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(
				new DataOutputStream(excptArray)));
			debug(new String(excptArray.toByteArray()));
		}
		// debug("MADE IT TO EDIT.pickMode: " + this.getCurrentComplexPickMode());
		// this.setCurrentComplexPickMode((int)((Integer)evt.getNewValue()).intValue());
		int oldCurrentComplexPickMode = this.getCurrentComplexPickMode();
		// debug("FIRING: " + propertyChangeListeners.hasListeners("CurrentComplexPickMode"));
		propertyChangeListeners.firePropertyChange(
			"CurrentComplexPickMode",
			new Integer(-1), new Integer(this.getCurrentComplexPickMode()));
	}
	else if (propertyName.equals("ComplexCollectionSelected"))
	{
	}
}

/************** End PropertyChangeListener Implementaion *************/

/************** Start KeyListener Implementaion *************/

/*
public void
keyTyped(KeyEvent e)
{
	printConsole("KEY TYPED: ");
	debug("KEY TYPED: ");
}

public void
keyPressed(KeyEvent e)
{
	printConsole("KEY PRESSED: ");
	debug("KEY PRESSED: ");
}

public void
keyReleased(KeyEvent e)
{
	printConsole("KEY RELEASED: ");
	debug("KEY RELEASED: ");
}
*/

/************** End KeyListener Implementaion *************/


/*************** Private Classes *************************/

private class
PartitionSSPanel
extends JPanel
implements PropertyChangeListener
{

private String title = null;
private JLabel titleLabel = null;
private JTextField userMsgTextField = null;

public
PartitionSSPanel(String title)
{
	try
	{
	this.title = title;
	complexSceneView.printConsole(" ");
	this.buildGui();
	}
	catch (Exception e)
	{
		debug("Exception in PartitionSSPanel.Constructor: " + e.toString());
		ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(
			new DataOutputStream(excptArray)));
		debug(new String(excptArray.toByteArray()));
	}
}

private void
buildGui()
throws Exception
{
	this.setLayout(new GridLayout(20, 1));
	this.setBackground(complexSceneView.guiBGColor);
	this.setForeground(Color.black);

	titleLabel = new JLabel(title, JLabel.CENTER);
	this.add(titleLabel);

	JButton applyBt = new JButton("Apply");
	btInsets = applyBt.getInsets();
	applyBt.setMargin(new Insets(btInsets.top, 0, btInsets.bottom,
		btInsets.right + lftInset + 60));
	applyBt.setFont(btFont);
	applyBt.setBackground(complexSceneView.guiBGColor);
	applyBt.setForeground(Color.black);
	applyBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				runApplyPartitionSS();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneEdit.applyBt:", e, 101);
			}
		}
	});
	this.add(applyBt);

	userMsgTextField = new JTextField();
	userMsgTextField.setEditable(false);
	userMsgTextField.setBackground(Color.white);
	userMsgTextField.setForeground(Color.black);
	this.add(userMsgTextField);
	updateUserMsg();
}

private void
updateUserMsg()
throws Exception
{
	ComplexCollection complexNode = complexSceneView.getComplexTreePick();
	int nodeLevel = ComplexScene.getChildLevel(complexNode);

	switch (getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		userMsgTextField.setText("Pick Single Nuc");
		break;
	  case ComplexDefines.InRNASingleStrand :
		userMsgTextField.setText("Pick Single Strand");
		if (nodeLevel == 0)
		{
		}
		else if (nodeLevel == 1)
		{
		}
		else if (nodeLevel == 2)
		{
			SSData2D sstr = (SSData2D)complexNode;
			// ComplexSceneEditTab.this.runSchematics(sstr);

		}
		break;
	  case ComplexDefines.InRNABasePair :
		userMsgTextField.setText("Pick BasePair");
		break;
	  case ComplexDefines.InRNAHelix :
		userMsgTextField.setText("Pick Helix");
		break;
	  case ComplexDefines.InRNAHelicalRun :
		userMsgTextField.setText("Pick Stacked Helix");
		break;
	  case ComplexDefines.InRNASubDomain :
		userMsgTextField.setText("Pick Sub Domain");
		break;
	  case ComplexDefines.InRNANamedGroup :
		userMsgTextField.setText("Pick Domain");
		break;
	  case ComplexDefines.InRNAColorUnit :
		userMsgTextField.setText("Pick Color");
		break;
	  case ComplexDefines.InRNACycle :
		userMsgTextField.setText("Pick Cycle");
		break;
	  case ComplexDefines.InRNAListNucs :
		userMsgTextField.setText("Pick Consecutive Nucs");
		break;
	  case ComplexDefines.InRNASSData :
		userMsgTextField.setText("Pick SSData");
		break;
	  case ComplexDefines.InComplex :
		userMsgTextField.setText("Pick Complex");
		break;
	  case ComplexDefines.InLabelsOnly :
		userMsgTextField.setText("Pick Labels");
		break;
		/*
	  case ComplexDefines.InComplexArea :
		userMsgTextField.setText("Pick top.lft/btm.rht Area");
		break;
		*/
	  case ComplexDefines.InComplexScene :
		userMsgTextField.setText("Pick Complex Scene");
		for (int objID = 0;objID <
			complexSceneView.getComplexScene().getItemCount();objID++)
		{
			Object obj =
				complexSceneView.getComplexScene().getItemAt(objID);
			if (obj == null)
				continue;
			if (obj instanceof ComplexSSDataCollection2D)
			{
				ComplexSSDataCollection2D collection =
					(ComplexSSDataCollection2D)obj;
				for (int obj1ID = 0;obj1ID < collection.getItemCount();obj1ID++)
				{
					Object obj1 = collection.getItemAt(obj1ID);
					if (obj1 instanceof SSData2D)
					{
						// ComplexSceneEditTab.this.runSchematics((SSData2D)obj1);
					}
				}
			}
		}
		break;
	  default :
		break;
	}
}

private void
runApplyPartitionSS()
throws Exception
{
	ComplexCollection complexNode = complexSceneView.getComplexTreePick();
	int nodeLevel = ComplexScene.getChildLevel(complexNode);

	if (!(complexNode instanceof SSData2D))
	{
		alert("Must pick a nuc in SSData");
		/* NEED to refigure with RNAListNucs
		firstListNuc = null;
		secondListNuc = null;
		*/
		return;
	}

	Graphics2D g2 = complexSceneView.getInitViewGraphics2D();
	g2.setRenderingHints(GraphicsUtil.aliasedRenderHints);

	DrawObject drawObject = ComplexSceneEditTab.this.getCurrentDrawObject();
	DrawObjectCollection parent =
		(DrawObjectCollection)drawObject.getParentCollection();

	if ((parent == null) || !(parent instanceof Nuc2D) ||
		(((Nuc2D)parent).getNucDrawObject() != drawObject))
	{
		alert("Must pick Nuc");
		return;
	}
	
	Nuc2D nuc = (Nuc2D)parent;

	switch (getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		alert("NOT YET 1");
		break;
	  case ComplexDefines.InRNASingleStrand :
		if (nodeLevel == 0)
		{
			alert("NOT YET 2");
		}
		else if (nodeLevel == 1)
		{
			alert("NOT YET 3");
		}
		else if (nodeLevel == 2)
		{
			SSData2D sstr = (SSData2D)complexNode;
			// ComplexSceneEditTab.this.runSchematics(sstr);

		}
		break;
	  case ComplexDefines.InRNABasePair :
		alert("NOT YET 4");
		break;
	  case ComplexDefines.InRNAHelix :
		alert("NOT YET 5");
		break;
	  case ComplexDefines.InRNAHelicalRun :
		alert("NOT YET 6");
		break;
	  case ComplexDefines.InRNASubDomain :
		alert("NOT YET 7");
		break;
	  case ComplexDefines.InRNANamedGroup :
		alert("NOT YET 7");
		break;
	  case ComplexDefines.InRNAColorUnit :
		alert("NOT YET 7.5");
		break;
	  case ComplexDefines.InRNACycle :
		alert("NOT YET 8");
		break;
	  case ComplexDefines.InRNAListNucs :
		/* NEED to refigure with new RNAListNucs
		if (firstListNuc == null)
		{
			firstListNuc = nuc;
			secondListNuc = null;
			userMsgTextField.setText("Pick next nuc in List");
		}
		else
		{
			userMsgTextField.setText("Pick first nuc in List");
			secondListNuc = nuc;

			// now apply
			runPartitionSS((SSData2D)complexNode, firstListNuc, secondListNuc);

			firstListNuc = null;
			secondListNuc = null;
		}
		*/
		break;
	  case ComplexDefines.InRNASSData :
		alert("NOT YET 9");
		break;
	  case ComplexDefines.InComplex :
		alert("NOT YET 10");
		break;
	  case ComplexDefines.InLabelsOnly :
		alert("NOT YET 11");
		return;
		/*
	  case ComplexDefines.InComplexArea :
		alert("NOT YET 11");
		return;
		*/
	  case ComplexDefines.InComplexScene :
		for (int objID = 0;objID <
			complexSceneView.getComplexScene().getItemCount();objID++)
		{
			Object obj =
				complexSceneView.getComplexScene().getItemAt(objID);
			if (obj == null)
				continue;
			if (obj instanceof ComplexSSDataCollection2D)
			{
				ComplexSSDataCollection2D collection =
					(ComplexSSDataCollection2D)obj;
				for (int obj1ID = 0;obj1ID < collection.getItemCount();obj1ID++)
				{
					Object obj1 = collection.getItemAt(obj1ID);
					if (obj1 instanceof SSData2D)
					{
						// ComplexSceneEditTab.this.runSchematics((SSData2D)obj1);
					}
				}
			}
		}
		break;
	  default :
		break;
	}
}

public void
runPartitionSS(SSData2D sstr, Nuc2D firstListNuc, Nuc2D secondListNuc)
throws Exception
{
	/* NEED to refigure with new RNAListNucs
	ComplexScene2D complexScene =
		ComplexSSDataCollection2D.getNewPartitionedComplexScene(
			sstr, firstListNuc, secondListNuc);
	complexSceneView.setComplexScene(complexScene.getComplexSceneFromParse());
	complexSceneView.resetPickTreePanels();
	complexSceneView.renderDrawObjectView();
	*/
}

// ************** PropertyChangeListener Implementaion **************

public void
propertyChange(PropertyChangeEvent evt)
{
	String propertyName = evt.getPropertyName();

	if (propertyName == null)
		return;

	// USE if need to be informed of a new drawobject picked
	if (propertyName.equals("CurrentDrawObject"))
	{
		//
		// debug("MADE IT TO PartitionsS PANEL CurrentDrawObject");
		// debug("CURR OBJ: " + ComplexSceneEditTab.this.getCurrentDrawObject());
		//
		try
		{
			debug("RUNNING PARTITION");
			this.runApplyPartitionSS();
		}
		catch (Exception e)
		{
			debug("Error in ComplexSceneEditTab.PartitionSSPanel.propertyChange().runApplyPartitionSS(): " + e.toString());
			ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(
				new DataOutputStream(excptArray)));
			debug(new String(excptArray.toByteArray()));
		}
	}
	else if (propertyName.equals("CurrentComplexPickMode"))
	{
		//
		// this.setCurrentComplexPickMode((int)((Integer)evt.getNewValue()).intValue());
		//
		String tmp = ComplexScene.getPickModePrefixTitle(
			ComplexSceneEditTab.this.getCurrentComplexPickMode()) +
				" PartitionSS";
		partitionSSFrame.setTitle(tmp);
		this.titleLabel.setText(tmp);
		try
		{
			printEditTabPickMode();
			updateUserMsg();
		}
		catch (Exception e)
		{
			debug("Error in ComplexSceneEditTab.propertyChange().updateUserMsg(): " + e.toString());
			ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(
				new DataOutputStream(excptArray)));
			debug(new String(excptArray.toByteArray()));
		}
	}
}

private void
debug(String s)
{
	System.err.println("PartitionSSPanel-> " + s);
}

}

/*************** End Private Classes *************************/

private static void
debug(String s)
{
	System.err.println("ComplexSceneEditTab-> " + s);
}

}
