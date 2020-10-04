package ssview;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
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
import jimage.DefaultLinePanel;
import jimage.DrawTriangleObject;
import jimage.DefaultTrianglePanel;
import jimage.DrawArrowObject;
import jimage.DefaultArrowPanel;
import jimage.DrawParallelogramObject;
import jimage.DefaultParallelogramPanel;
import jimage.FontChooser;

import util.*;
import util.math.*;

public class
ComplexSceneAnnotateTab
extends ComplexSceneWorkTab
{

private JRadioButton inquireOnlyRB = null;
private JRadioButton addStringLabel_RB = null;
private JTextField addStringLabel_TF = null;
private JRadioButton addLineLabel_RB = null;
private JRadioButton addTriangleLabel_RB = null;
private JRadioButton addCircleLabel_RB = null;
private JRadioButton addParallelogramLabel_RB = null;
private JRadioButton addArrowLabel_RB = null;

private JTextField schemLineWidth_TF = null;
private JTextField schemBPLineWidth_TF = null;

private JTextField nucPathLineWidth_TF = null;

private JTextField bpSchemGap_TF = null;
private JTextField fpSchemGap_TF = null;
private JTextField tpSchemGap_TF = null;
public JInternalFrame defaultCircleFrame = null;
public DefaultCirclePanel defaultCirclePanel = null;
public JInternalFrame defaultLineFrame = null;
public DefaultLinePanel defaultLinePanel = null;
public JInternalFrame defaultTriangleFrame = null;
public DefaultTrianglePanel defaultTrianglePanel = null;
public JInternalFrame defaultParallelogramFrame = null;
public DefaultParallelogramPanel defaultParallelogramPanel = null;
public JInternalFrame defaultArrowFrame = null;
public DefaultArrowPanel defaultArrowPanel = null;

private JPanel schematicsPanel = null;
private JPanel nucPathPanel = null;

protected transient PropertyChangeSupport
	propertyChangeListeners = null;

public
ComplexSceneAnnotateTab()
{
	this.buildGui(complexSceneView.guiBGColor,
		complexSceneView.controlPanelW, complexSceneView.controlPanelH);
	propertyChangeListeners = new PropertyChangeSupport(this);	
}

public
ComplexSceneAnnotateTab(Color guiBGColor, int panelW, int panelH)
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
	this.resetAnnotateRBs();
	defaultCirclePanel = new DefaultCirclePanel(
		new DrawCircleObject(3.0, 0.5), complexSceneView.guiBGColor,
		complexSceneView.getFigureScale(), getColorChooser());
	defaultLinePanel = new DefaultLinePanel(
		new DrawLineObject(-10.0, 0.0, 10.0, 0.0, 0.5, Color.black), complexSceneView.guiBGColor,
		complexSceneView.getFigureScale(), getColorChooser());
	defaultTrianglePanel = new DefaultTrianglePanel(
		new DrawTriangleObject(5.0, 5.0, 0.0, 1.0, 0.5, true,
			Color.black), complexSceneView.guiBGColor,
			complexSceneView.getFigureScale(), getColorChooser());
	defaultParallelogramPanel = new DefaultParallelogramPanel(
		new DrawParallelogramObject(0.0, 0.0, 0.0, 5.0, 90.0, 5.0,
			0.5, true, Color.black), complexSceneView.guiBGColor,
			complexSceneView.getFigureScale(), getColorChooser());
	defaultArrowPanel = new DefaultArrowPanel(
		new DrawArrowObject(10.0, 10.0, 6.0, 1.0, 3.0, 0.0, 0.5, true, Color.black),
			complexSceneView.guiBGColor,
			complexSceneView.getFigureScale(), getColorChooser());
}

/*
JLabel currObjLabel = null;
*/

public void
buildGui(Color guiBGColor, int panelW, int panelH)
{
	super.buildGui(guiBGColor, panelW, panelH);

	controlBtPanel = new JPanel(new GridLayout(18, 1), true);
	controlBtPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
	controlBtPanel.setForeground(Color.black);
	controlBtPanel.setBackground(guiBGColor);

	// NEED to have a msg come up somewhere saying who going to add too
	/*
	JLabel currObjLabel = new JLabel("current object: " + this.currentContainerName());
	controlBtPanel.add(this.getNewLabelPanel(currObjLabel));
	*/

	int btLengthAdjust = -15; // make longest string centered

	JButton annotateColorChooserBt = new JButton();
	btInsets = annotateColorChooserBt.getInsets();
	annotateColorChooserBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	annotateColorChooserBt.setText("Select Color");
	annotateColorChooserBt.setActionCommand("Select_Color");
	annotateColorChooserBt.setFont(btFont);
	annotateColorChooserBt.setForeground(Color.black);
	annotateColorChooserBt.setBackground(guiBGColor);
	controlBtPanel.add(annotateColorChooserBt);
	annotateColorChooserBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			try
			{
				ComplexSceneAnnotateTab.this.setColorFrame();
				ComplexSceneAnnotateTab.this.getColorChooser().setVisible(true);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneAnnotateTab.annotateColorChooserBt: ", e, 101);
			}
		}
	});

	ButtonGroup annotateGroupRBs = new ButtonGroup();

	inquireOnlyRB = new JRadioButton();
	inquireOnlyRB.setSelected(true);
	controlBtPanel.add(getNewViewButtonLeftPanel(
		inquireOnlyRB, "InquireOnly"));
	annotateGroupRBs.add(inquireOnlyRB);

	addStringLabel_RB = new JRadioButton();
	addStringLabel_RB.setSelected(false);
	controlBtPanel.add(getNewViewButtonLeftPanel(
		addStringLabel_RB, "Add String Label"));
	annotateGroupRBs.add(addStringLabel_RB);

	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel("string text:"));
	addStringLabel_TF = new JTextField("test", 8);	
	tmpPanel.add(addStringLabel_TF);
	controlBtPanel.add(tmpPanel);

	JButton annotateFontChooserBt = new JButton();
	btInsets = annotateFontChooserBt.getInsets();
	annotateFontChooserBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	annotateFontChooserBt.setText("Select Font");
	annotateFontChooserBt.setActionCommand("Select_Font");
	annotateFontChooserBt.setFont(btFont);
	annotateFontChooserBt.setForeground(Color.black);
	annotateFontChooserBt.setBackground(guiBGColor);
	controlBtPanel.add(annotateFontChooserBt);
	annotateFontChooserBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			ComplexSceneAnnotateTab.this.getFontChooser().setVisible(true);
		}
	});

	addLineLabel_RB = new JRadioButton();
	addLineLabel_RB.setSelected(false);
	controlBtPanel.add(getNewViewButtonLeftPanel(
		addLineLabel_RB, "Add Line Label"));
	annotateGroupRBs.add(addLineLabel_RB);

	JButton annotateLineDefaultBt = new JButton();
	btInsets = annotateLineDefaultBt.getInsets();
	annotateLineDefaultBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	annotateLineDefaultBt.setText("Default Line");
	annotateLineDefaultBt.setFont(btFont);
	annotateLineDefaultBt.setForeground(Color.black);
	annotateLineDefaultBt.setBackground(guiBGColor);
	controlBtPanel.add(annotateLineDefaultBt);
	annotateLineDefaultBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			try
			{
				initDefaultLineFrame();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneAnnotateTab.initDefaultTriangleFrame: ", e, 101);
			}
		}
	});

	addTriangleLabel_RB = new JRadioButton();
	addTriangleLabel_RB.setSelected(false);
	controlBtPanel.add(getNewViewButtonLeftPanel(
		addTriangleLabel_RB, "Add Triangle Label"));
	annotateGroupRBs.add(addTriangleLabel_RB);

	JButton annotateTriangleDefaultBt = new JButton();
	btInsets = annotateTriangleDefaultBt.getInsets();
	annotateTriangleDefaultBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	annotateTriangleDefaultBt.setText("Default Triangle");
	annotateTriangleDefaultBt.setFont(btFont);
	annotateTriangleDefaultBt.setForeground(Color.black);
	annotateTriangleDefaultBt.setBackground(guiBGColor);
	controlBtPanel.add(annotateTriangleDefaultBt);
	annotateTriangleDefaultBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			try
			{
				initDefaultTriangleFrame();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneAnnotateTab.initDefaultTriangleFrame: ", e, 101);
			}
		}
	});

	addCircleLabel_RB = new JRadioButton();
	addCircleLabel_RB.setSelected(false);
	controlBtPanel.add(getNewViewButtonLeftPanel(
		addCircleLabel_RB, "Add Circle Label"));
	annotateGroupRBs.add(addCircleLabel_RB);

	JButton annotateCircleDefaultBt = new JButton();
	btInsets = annotateCircleDefaultBt.getInsets();
	annotateCircleDefaultBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	annotateCircleDefaultBt.setText("Default Circle");
	annotateCircleDefaultBt.setFont(btFont);
	annotateCircleDefaultBt.setForeground(Color.black);
	annotateCircleDefaultBt.setBackground(guiBGColor);
	controlBtPanel.add(annotateCircleDefaultBt);
	annotateCircleDefaultBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			try
			{
				initDefaultCircleFrame();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneAnnotateTab.initDefaultCircleFrame: ", e, 101);
			}
		}
	});

	addParallelogramLabel_RB = new JRadioButton();
	addParallelogramLabel_RB.setSelected(false);
	controlBtPanel.add(getNewViewButtonLeftPanel(
		addParallelogramLabel_RB, "Add Parallelogram Label"));
	annotateGroupRBs.add(addParallelogramLabel_RB);

	JButton annotateParallelogramDefaultBt = new JButton();
	btInsets = annotateParallelogramDefaultBt.getInsets();
	annotateParallelogramDefaultBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	annotateParallelogramDefaultBt.setText("Default Parallelogram");
	annotateParallelogramDefaultBt.setFont(btFont);
	annotateParallelogramDefaultBt.setForeground(Color.black);
	annotateParallelogramDefaultBt.setBackground(guiBGColor);
	controlBtPanel.add(annotateParallelogramDefaultBt);
	annotateParallelogramDefaultBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			try
			{
				initDefaultParallelogramFrame();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneAnnotateTab.initDefaultParallelogramFrame: ", e, 101);
			}
		}
	});

	addArrowLabel_RB = new JRadioButton();
	addArrowLabel_RB.setSelected(false);
	controlBtPanel.add(getNewViewButtonLeftPanel(
		addArrowLabel_RB, "Add Arrow Label"));
	annotateGroupRBs.add(addArrowLabel_RB);

	JButton annotateArrowDefaultBt = new JButton();
	btInsets = annotateArrowDefaultBt.getInsets();
	annotateArrowDefaultBt.setMargin(
		new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
	annotateArrowDefaultBt.setText("Default Arrow");
	annotateArrowDefaultBt.setFont(btFont);
	annotateArrowDefaultBt.setForeground(Color.black);
	annotateArrowDefaultBt.setBackground(guiBGColor);
	controlBtPanel.add(annotateArrowDefaultBt);
	annotateArrowDefaultBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent actionEvt)
		{
			try
			{
				initDefaultArrowFrame();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(
					"Exception in ComplexSceneAnnotateTab.initDefaultArrowFrame: ", e, 101);
			}
		}
	});

	// HERE
	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.setFont(btFont);
	JLabel tmpLabel = this.getNewViewLabel("schematic linewidth");
	// tmpLabel.setFont(btFont);
	tmpPanel.add(tmpLabel);
	schemLineWidth_TF = new JTextField("1.0", 3);
	schemLineWidth_TF.setFont(smBtFont);
	tmpPanel.add(schemLineWidth_TF);
	controlBtPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.setFont(btFont);
	tmpLabel = this.getNewViewLabel("schematic BP linewidth");
	// tmpLabel.setFont(btFont);
	tmpPanel.add(tmpLabel);
	schemBPLineWidth_TF = new JTextField("1.0", 3);
	schemBPLineWidth_TF.setFont(smBtFont);
	tmpPanel.add(schemBPLineWidth_TF);
	controlBtPanel.add(tmpPanel);

	controlBtPanelScrollPane = new JScrollPane(controlBtPanel,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	controlPanel.add(BorderLayout.CENTER, controlBtPanelScrollPane);
}

private void
initDefaultTriangleFrame()
throws Exception
{
	if (defaultTriangleFrame == null)
	{
		defaultTriangleFrame = complexSceneView.getBasicInternalFrame(110, 60, 300, 300);
		defaultTriangleFrame.addInternalFrameListener(new InternalFrameListener()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				defaultTriangleFrame.restoreSubcomponentFocus();
			}

			public void internalFrameClosed(InternalFrameEvent e)
			{
				try
				{
					InternalFrameListener[] listenerList =
						defaultTriangleFrame.getInternalFrameListeners();
					if (listenerList.length > 1)
						debug("Problem in ComplexSceneAnnotateTab.defaultTriangleFrame().internalFrameClosed(): More than one listener");
					requestFocus();
				}
				catch (Exception evt)
				{
					complexSceneView.handleException("Exception in CopmlexSceneFormatSStrTab.defaultTriangleFrame.internalFrameClosing:", evt, 101);
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
	defaultTrianglePanel.setParentViewScaleVal(this.complexSceneView.getFigureScale());
	defaultTrianglePanel.updateGui();
	complexSceneView.updateBasicInternalFrame("Default Triangle",
		"Set Properties for Figure Scale value " + this.complexSceneView.getFigureScale() + ":",
		defaultTrianglePanel, defaultTriangleFrame);
	if (defaultTriangleFrame.isIcon())
		defaultTriangleFrame.setIcon(false);
}

private void
initDefaultCircleFrame()
throws Exception
{
	if (defaultCircleFrame == null)
	{
		defaultCircleFrame = complexSceneView.getBasicInternalFrame(110, 60, 300, 300);
		defaultCircleFrame.addInternalFrameListener(new InternalFrameListener()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				defaultCircleFrame.restoreSubcomponentFocus();
			}

			public void internalFrameClosed(InternalFrameEvent e)
			{
				try
				{
					InternalFrameListener[] listenerList =
						defaultCircleFrame.getInternalFrameListeners();
					if (listenerList.length > 1)
						debug("Problem in ComplexSceneAnnotateTab.defaultCircleFrame().internalFrameClosed(): More than one listener");
					requestFocus();
				}
				catch (Exception evt)
				{
					complexSceneView.handleException("Exception in CopmlexSceneFormatSStrTab.defaultCircleFrame.internalFrameClosing:", evt, 101);
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
	defaultCirclePanel.setParentViewScaleVal(this.complexSceneView.getFigureScale());
	defaultCirclePanel.updateGui();
	complexSceneView.updateBasicInternalFrame("Default Circle",
		"Set Properties for Figure Scale value " + this.complexSceneView.getFigureScale() + ":",
		defaultCirclePanel, defaultCircleFrame);
	if (defaultCircleFrame.isIcon())
		defaultCircleFrame.setIcon(false);
}

private void
initDefaultLineFrame()
throws Exception
{
	if (defaultLineFrame == null)
	{
		defaultLineFrame = complexSceneView.getBasicInternalFrame(110, 60, 300, 300);
		defaultLineFrame.addInternalFrameListener(new InternalFrameListener()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				defaultLineFrame.restoreSubcomponentFocus();
			}

			public void internalFrameClosed(InternalFrameEvent e)
			{
				try
				{
					InternalFrameListener[] listenerList =
						defaultLineFrame.getInternalFrameListeners();
					if (listenerList.length > 1)
						debug("Problem in ComplexSceneAnnotateTab.defaultLineFrame().internalFrameClosed(): More than one listener");
					requestFocus();
				}
				catch (Exception evt)
				{
					complexSceneView.handleException("Exception in CopmlexSceneFormatSStrTab.defaultLineFrame.internalFrameClosing:", evt, 101);
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
	defaultLinePanel.setParentViewScaleVal(this.complexSceneView.getFigureScale());
	defaultLinePanel.updateGui();
	complexSceneView.updateBasicInternalFrame("Default Line",
		"Set Properties for Figure Scale value " + this.complexSceneView.getFigureScale() + ":",
		defaultLinePanel, defaultLineFrame);
	if (defaultLineFrame.isIcon())
		defaultLineFrame.setIcon(false);
}

private void
initDefaultParallelogramFrame()
throws Exception
{
	if (defaultParallelogramFrame == null)
	{
		defaultParallelogramFrame = complexSceneView.getBasicInternalFrame(110, 60, 300, 300);
		defaultParallelogramFrame.addInternalFrameListener(new InternalFrameListener()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				defaultParallelogramFrame.restoreSubcomponentFocus();
			}

			public void internalFrameClosed(InternalFrameEvent e)
			{
				try
				{
					InternalFrameListener[] listenerList =
						defaultParallelogramFrame.getInternalFrameListeners();
					if (listenerList.length > 1)
						debug("Problem in ComplexSceneAnnotateTab.defaultParallelogramFrame().internalFrameClosed(): More than one listener");
					requestFocus();
				}
				catch (Exception evt)
				{
					complexSceneView.handleException("Exception in CopmlexSceneFormatSStrTab.defaultParallelogramFrame.internalFrameClosing:", evt, 101);
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
	defaultParallelogramPanel.setParentViewScaleVal(this.complexSceneView.getFigureScale());
	defaultParallelogramPanel.updateGui();
	complexSceneView.updateBasicInternalFrame("Default Parallelogram",
		"Set Properties for Figure Scale value " + this.complexSceneView.getFigureScale() + ":",
		defaultParallelogramPanel, defaultParallelogramFrame);
	if (defaultParallelogramFrame.isIcon())
		defaultParallelogramFrame.setIcon(false);
}

private void
initDefaultArrowFrame()
throws Exception
{
	if (defaultArrowFrame == null)
	{
		defaultArrowFrame = complexSceneView.getBasicInternalFrame(110, 60, 300, 300);
		defaultArrowFrame.addInternalFrameListener(new InternalFrameListener()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				defaultArrowFrame.restoreSubcomponentFocus();
			}

			public void internalFrameClosed(InternalFrameEvent e)
			{
				try
				{
					InternalFrameListener[] listenerList =
						defaultArrowFrame.getInternalFrameListeners();
					if (listenerList.length > 1)
						debug("Problem in ComplexSceneAnnotateTab.defaultArrowFrame().internalFrameClosed(): More than one listener");
					requestFocus();
				}
				catch (Exception evt)
				{
					complexSceneView.handleException("Exception in CopmlexSceneFormatSStrTab.defaultArrowFrame.internalFrameClosing:", evt, 101);
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
	defaultArrowPanel.setParentViewScaleVal(this.complexSceneView.getFigureScale());
	defaultArrowPanel.updateGui();
	complexSceneView.updateBasicInternalFrame("Default Arrow",
		"Set Properties for Figure Scale value " + this.complexSceneView.getFigureScale() + ":",
		defaultArrowPanel, defaultArrowFrame);
	if (defaultArrowFrame.isIcon())
		defaultArrowFrame.setIcon(false);
}

/* Not sure why I need this; taking out 7/16/03
public void
setCurrentDrawObject(DrawObject currentDrawObject)
{
	DrawObject oldDrawObject = this.getCurrentDrawObject();
	super.setCurrentDrawObject(currentDrawObject);

	propertyChangeListeners.firePropertyChange("CurrentDrawObject",
		oldDrawObject, this.getCurrentDrawObject());
}

public synchronized void
addPropertyChangeListener(PropertyChangeListener l)
{
	// super.addPropertyChangeListener(l);
	propertyChangeListeners.addPropertyChangeListener(l);
}

public synchronized void
removePropertyChangeListener(PropertyChangeListener l)
{
	// super.removePropertyChangeListener(l);
	propertyChangeListeners.removePropertyChangeListener(l);
}
*/

/*************** MOUSE STUFF ***************************/

AnnotateBtListener annotateBtListener = new AnnotateBtListener();

private Box
addTextAnnotateProperties(Box panel)
throws Exception
{
	JButton setCurrentColorBt = this.getNewViewButton("Set To Current Color");
	setCurrentColorBt.setActionCommand(ComplexDefines.ANNOTATE_COLOR);
	setCurrentColorBt.addActionListener(annotateBtListener);
	panel.add(this.getNewViewButtonLeftPanel(setCurrentColorBt));

	JButton setCurrentFontBt = this.getNewViewButton("Set To Current Font");
	setCurrentFontBt.setActionCommand(ComplexDefines.ANNOTATE_FONT);
	setCurrentFontBt.addActionListener(annotateBtListener);
	panel.add(this.getNewViewButtonLeftPanel(setCurrentFontBt));

	return (panel);
}

private Box
addSymbolAnnotateProperties(Box panel)
throws Exception
{
	JPanel tmpPanel = null;

	JButton setCurrentCircleBt = this.getNewViewButton("Set To Current Circle");
	setCurrentCircleBt.setActionCommand(ComplexDefines.ANNOTATE_CIRCLE);
	setCurrentCircleBt.addActionListener(annotateBtListener);
	panel.add(this.getNewViewButtonLeftPanel(setCurrentCircleBt));

	JButton setCurrentTriangleBt = this.getNewViewButton("Set To Current Triangle");
	setCurrentTriangleBt.setActionCommand(ComplexDefines.ANNOTATE_TRIANGLE);
	setCurrentTriangleBt.addActionListener(annotateBtListener);
	panel.add(this.getNewViewButtonLeftPanel(setCurrentTriangleBt));

	JButton setCurrentParallelogramBt = this.getNewViewButton("Set To Current Parallelogram");
	setCurrentParallelogramBt.setActionCommand(ComplexDefines.ANNOTATE_PARALLELOGRAM);
	setCurrentParallelogramBt.addActionListener(annotateBtListener);
	panel.add(this.getNewViewButtonLeftPanel(setCurrentParallelogramBt));

	JButton setLowerCaseBt = this.getNewViewButton("Set To LowerCase");
	setLowerCaseBt.setActionCommand(ComplexDefines.ANNOTATE_LOWER_CASE);
	setLowerCaseBt.addActionListener(annotateBtListener);
	panel.add(this.getNewViewButtonLeftPanel(setLowerCaseBt));

	JButton setCurrentClearBt = this.getNewViewButton("Clear Symbols");
	setCurrentClearBt.setActionCommand(ComplexDefines.ANNOTATE_CLEAR);
	setCurrentClearBt.addActionListener(annotateBtListener);
	panel.add(this.getNewViewButtonLeftPanel(setCurrentClearBt));

	if (getCurrentComplexPickMode() == ComplexDefines.InRNASingleNuc)
	{
		if (getCurrentWorkNuc().isBasePair() &&
			(getCurrentWorkNuc().isSchematic() ||
			 getCurrentWorkNuc().getBasePair2D().isSchematic()))
			schematicsPanel = new JPanel(new GridLayout(5, 1), true);
		else
			schematicsPanel = new JPanel(new GridLayout(4, 1), true);
	}
	else
	{
		schematicsPanel = new JPanel(new GridLayout(2, 1), true);
	}

	schematicsPanel.setFont(new Font("Helvetica", Font.PLAIN, 12));
	schematicsPanel.setForeground(Color.black);
	schematicsPanel.setBackground(guiBGColor);
	schematicsPanel.setBorder(new TitledBorder(
		new SoftBevelBorder(BevelBorder.LOWERED), "Schematics:"));
	schematicsPanel.setAlignmentX(LEFT_ALIGNMENT);

	JButton setCurrentSchematicsBt = this.getNewViewButton("Schematicize");
	setCurrentSchematicsBt.setActionCommand(ComplexDefines.ANNOTATE_SCHEMATICIZE);
	setCurrentSchematicsBt.addActionListener(annotateBtListener);
	schematicsPanel.add(this.getNewViewButtonLeftPanel(setCurrentSchematicsBt));

	JButton setCurrentClearSchematicsBt = this.getNewViewButton("Clear Schematics");
	setCurrentClearSchematicsBt.setActionCommand(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE);
	setCurrentClearSchematicsBt.addActionListener(annotateBtListener);
	schematicsPanel.add(this.getNewViewButtonLeftPanel(setCurrentClearSchematicsBt));

	if (getCurrentComplexPickMode() == ComplexDefines.InRNASingleNuc)
	{
		/*
		JButton setCurrentClearSchematicsBt = this.getNewViewButton("");
		setCurrentClearSchematicsBt.setActionCommand(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE);
		setCurrentClearSchematicsBt.addActionListener(annotateBtListener);
		schematicsPanel.add(this.getNewViewButtonLeftPanel(setCurrentClearSchematicsBt));
		*/

		ActionListener
		changeSchemGaps_AL = new ActionListener()
		{
			public void
			actionPerformed(ActionEvent evt)
			{
				double gap = 0.0;
				if (evt.getActionCommand().equals(ComplexDefines.ANNOTATE_BP_GAP_SCHEMATICIZE))
				{
					try
					{
						gap = Double.parseDouble(bpSchemGap_TF.getText());
					}
					catch (NumberFormatException e)
					{
						alert("Invalid Base Pair gap value: " + bpSchemGap_TF.getText());
						return;
					}
					try
					{
						getCurrentWorkNuc().setBPSchemGap(gap);
						complexSceneView.runRenderBt();
					}
					catch (Exception e)
					{
						complexSceneView.handleException(
							"Exception in ComplexSceneAnnotateTab.changeSchemGaps_AL:", e, 101);
					}
				}
				else if (evt.getActionCommand().equals(ComplexDefines.ANNOTATE_FP_GAP_SCHEMATICIZE))
				{
					try
					{
						gap = Double.parseDouble(fpSchemGap_TF.getText());
					}
					catch (NumberFormatException e)
					{
						alert("Invalid 5' gap value: " + fpSchemGap_TF.getText());
						return;
					}
					try
					{
						getCurrentWorkNuc().setFPSchemGap(gap);
						complexSceneView.runRenderBt();
					}
					catch (Exception e)
					{
						complexSceneView.handleException(
							"Exception in ComplexSceneAnnotateTab.changeSchemGaps_AL:", e, 101);
					}
				}
				else if (evt.getActionCommand().equals(ComplexDefines.ANNOTATE_TP_GAP_SCHEMATICIZE))
				{
					try
					{
						gap = Double.parseDouble(tpSchemGap_TF.getText());
					}
					catch (NumberFormatException e)
					{
						alert("Invalid 3' gap value: " + tpSchemGap_TF.getText());
						return;
					}
					try
					{
						getCurrentWorkNuc().setTPSchemGap(gap);
						complexSceneView.runRenderBt();
					}
					catch (Exception e)
					{
						complexSceneView.handleException(
							"Exception in ComplexSceneAnnotateTab.changeSchemGaps_AL:", e, 101);
					}
				}
			}
		};

		if (getCurrentWorkNuc().isBasePair())
		{
			tmpPanel = this.getNewFlowLeftPanel();
			tmpPanel.add(this.getNewViewLabel("Base Pair Gap: "));
			bpSchemGap_TF = new JTextField(3);
			bpSchemGap_TF.setFont(smBtFont);
			bpSchemGap_TF.setText("" + StringUtil.roundStrVal(getCurrentWorkNuc().getBPSchemGap(), 2));
			bpSchemGap_TF.setActionCommand(ComplexDefines.ANNOTATE_BP_GAP_SCHEMATICIZE);
			bpSchemGap_TF.addActionListener(changeSchemGaps_AL);
			tmpPanel.add(bpSchemGap_TF);
			JButton applyBPSchemGap_Bt = this.getNewViewImgPlainButton();
			applyBPSchemGap_Bt.setActionCommand(ComplexDefines.ANNOTATE_BP_GAP_SCHEMATICIZE);
			applyBPSchemGap_Bt.addActionListener(changeSchemGaps_AL);
			tmpPanel.add(applyBPSchemGap_Bt);
			schematicsPanel.add(tmpPanel);
		}

		tmpPanel = this.getNewFlowLeftPanel();
		tmpPanel.add(this.getNewViewLabel("5' Gap: "));
		fpSchemGap_TF = new JTextField(3);
		fpSchemGap_TF.setFont(smBtFont);
		fpSchemGap_TF.setText("" + StringUtil.roundStrVal(getCurrentWorkNuc().getFPSchemGap(), 2));
		fpSchemGap_TF.setActionCommand(ComplexDefines.ANNOTATE_FP_GAP_SCHEMATICIZE);
		fpSchemGap_TF.addActionListener(changeSchemGaps_AL);
		tmpPanel.add(fpSchemGap_TF);
		JButton applyFPSchemGap_Bt = this.getNewViewImgPlainButton();
		applyFPSchemGap_Bt.setActionCommand(ComplexDefines.ANNOTATE_FP_GAP_SCHEMATICIZE);
		applyFPSchemGap_Bt.addActionListener(changeSchemGaps_AL);
		tmpPanel.add(applyFPSchemGap_Bt);
		schematicsPanel.add(tmpPanel);

		tmpPanel = this.getNewFlowLeftPanel();
		tmpPanel.add(this.getNewViewLabel("3' Gap: "));
		tpSchemGap_TF = new JTextField(3);
		tpSchemGap_TF.setFont(smBtFont);
		tpSchemGap_TF.setText("" + StringUtil.roundStrVal(getCurrentWorkNuc().getTPSchemGap(), 2));
		tpSchemGap_TF.setActionCommand(ComplexDefines.ANNOTATE_TP_GAP_SCHEMATICIZE);
		tpSchemGap_TF.addActionListener(changeSchemGaps_AL);
		tmpPanel.add(tpSchemGap_TF);
		JButton applyTPSchemGap_Bt = this.getNewViewImgPlainButton();
		applyTPSchemGap_Bt.setActionCommand(ComplexDefines.ANNOTATE_TP_GAP_SCHEMATICIZE);
		applyTPSchemGap_Bt.addActionListener(changeSchemGaps_AL);
		tmpPanel.add(applyTPSchemGap_Bt);
		schematicsPanel.add(tmpPanel);
	}

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(schematicsPanel);
	schematicsPanel.setVisible(true);
	panel.add(tmpPanel);

	nucPathPanel = new JPanel(new GridLayout(3, 1), true);
	nucPathPanel.setFont(new Font("Helvetica", Font.PLAIN, 12));
	nucPathPanel.setForeground(Color.black);
	nucPathPanel.setBackground(guiBGColor);
	nucPathPanel.setBorder(new TitledBorder(
		new SoftBevelBorder(BevelBorder.LOWERED), "Nuc Path:"));
	nucPathPanel.setAlignmentX(LEFT_ALIGNMENT);

	JButton setCurrentNucPathBt = this.getNewViewButton("Set Nuc Path");
	setCurrentNucPathBt.setActionCommand(ComplexDefines.ANNOTATE_NUC_PATH);
	setCurrentNucPathBt.addActionListener(annotateBtListener);
	nucPathPanel.add(this.getNewViewButtonLeftPanel(setCurrentNucPathBt));

	JButton setCurrentClearNucPathBt = this.getNewViewButton("Clear Nuc Path");
	setCurrentClearNucPathBt.setActionCommand(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH);
	setCurrentClearNucPathBt.addActionListener(annotateBtListener);
	nucPathPanel.add(this.getNewViewButtonLeftPanel(setCurrentClearNucPathBt));

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.setFont(btFont);
	JLabel tmpLabel = this.getNewViewLabel("Nuc Path Line Width");
	tmpLabel.setFont(btFont);
	tmpPanel.add(tmpLabel);
	String pathWidthStr = "" + getCurrentWorkNuc().getNucPathLineWidth();

	// MAYBE make up a new nuc(A,U,C,G) and get bounding boxes for each,
	// taking largest width or height instead of taking pick nuc like below.

	if (getCurrentWorkNuc().getNucPathLineWidth() <= 0.0)
	{
		Nuc2D testNuc = new Nuc2D(getCurrentWorkNuc());
		testNuc.update(GraphicsUtil.unitG2);
		Rectangle2D rect = testNuc.getBoundingBox();
		double width = rect.getWidth();
		double height = rect.getHeight();
		if (width > height)
			pathWidthStr = "" + (width + 2.0);
		else
			pathWidthStr = "" + (height + 2.0);
	}
	nucPathLineWidth_TF = new JTextField(pathWidthStr, 4);

	nucPathLineWidth_TF.setFont(smBtFont);
	tmpPanel.add(nucPathLineWidth_TF);
	nucPathPanel.add(tmpPanel);

	tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(nucPathPanel);
	nucPathPanel.setVisible(true);
	panel.add(tmpPanel);

	return (panel);
}

private class
AnnotateBtListener
implements ActionListener
{
	public void
	actionPerformed(ActionEvent evt)
	{
		String actionCmd = evt.getActionCommand();
		try
		{
		if (getCurrentWorkLabel() != null)
		{
			editErase(getCurrentWorkLabel(), ComplexDefines.InLabelsOnly);
			if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
				getCurrentWorkLabel().setColor(getColorChooser().getColor());
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT) &&
				(getCurrentWorkLabel() instanceof DrawFontObject))
				((DrawFontObject)getCurrentWorkLabel()).setFont(getFontChooser().getCurrentFont());
			editRedraw(getCurrentWorkLabel(), sceneImgG2);
		}
		else
		switch (getCurrentComplexPickMode())
		{
		  case ComplexDefines.InRNASingleNuc :
			Nuc2D nuc = getCurrentWorkNuc();
			if (nuc == null)
			{
				alert("Must pick nuc to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				nuc.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
				complexPropertiesPanel.updateUI();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					nuc.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				nuc.clearAnnotation();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(nuc, ComplexDefines.InRNASingleNuc);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					nuc.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					nuc.setFont(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					nuc.setSymbol(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					nuc.setSymbol(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					nuc.setSymbol(getDefaultDrawParallelogramObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_LOWER_CASE))
					nuc.setSymbol(new DrawCharObject(Character.toLowerCase(nuc.getNucChar()), nuc.getFont()));
				editRedraw(nuc, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNASingleStrand :
			RNASingleStrand singleStrand = getCurrentWorkSingleStrand();
			if (singleStrand == null)
			{
				alert("Must pick RNA single stranded region to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				singleStrand.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					singleStrand.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				singleStrand.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(singleStrand, ComplexDefines.InRNASingleStrand);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					singleStrand.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					singleStrand.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					singleStrand.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					singleStrand.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					singleStrand.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(singleStrand, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNABasePair :
			RNABasePair2D basePair = getCurrentWorkBasePair();
			if (basePair == null)
			{
				alert("Must pick RNA Basepair to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				basePair.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					basePair.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				basePair.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(basePair, ComplexDefines.InRNASingleStrand);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					basePair.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					basePair.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					basePair.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					basePair.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					basePair.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(basePair, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNAHelix :
			RNAHelix2D helix = getCurrentWorkHelix();
			if (helix == null)
			{
				alert("Must pick RNA Helix to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				helix.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					helix.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				helix.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(helix, ComplexDefines.InRNAHelix);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					helix.setHelixColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					helix.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					helix.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					helix.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					helix.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(helix, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNAHelicalRun :
		  	RNAStackedHelix2D helicalRun = getCurrentWorkStackedHelix();
			if (helicalRun == null)
			{
				alert("Must pick RNA Helical Run to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				helicalRun.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					helicalRun.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				helicalRun.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(helicalRun, ComplexDefines.InRNAHelicalRun);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					helicalRun.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					helicalRun.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					helicalRun.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					helicalRun.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					helicalRun.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(helicalRun, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNASubDomain :
			RNASubDomain2D subDomain = getCurrentWorkSubDomain();
			if (subDomain == null)
			{
				alert("Must pick RNA SubDomain to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				subDomain.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					subDomain.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				subDomain.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(subDomain, ComplexDefines.InRNASubDomain);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					subDomain.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					subDomain.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					subDomain.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					subDomain.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					subDomain.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(subDomain, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNANamedGroup :
			RNANamedGroup2D namedGroup = getCurrentWorkNamedGroup();
			if (namedGroup == null)
			{
				alert("Must pick RNA Domain to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				namedGroup.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					namedGroup.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				namedGroup.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(namedGroup, ComplexDefines.InRNANamedGroup);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					namedGroup.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					namedGroup.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					namedGroup.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					namedGroup.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					namedGroup.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(namedGroup, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNAColorUnit :
			RNAColorUnit2D colorUnit = getCurrentWorkColorUnit();
			if (colorUnit == null)
			{
				alert("Must pick RNA Domain to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				colorUnit.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					colorUnit.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				colorUnit.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(colorUnit, ComplexDefines.InRNAColorUnit);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					colorUnit.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					colorUnit.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					colorUnit.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					colorUnit.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					colorUnit.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(colorUnit, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNACycle :
			RNACycle2D cycle = getCurrentWorkCycle();
			if (cycle == null)
			{
				alert("Must pick RNA Cycle to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				cycle.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					cycle.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				cycle.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(cycle, ComplexDefines.InRNACycle);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					cycle.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					cycle.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					cycle.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					cycle.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					cycle.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(cycle, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNAListNucs :
			RNAListNucs2D listNucs = getCurrentWorkListNucs();
			if (listNucs == null)
			{
				alert("Must pick consecutive nucs to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				listNucs.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					listNucs.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				listNucs.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(listNucs, ComplexDefines.InRNAListNucs);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					listNucs.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					listNucs.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					listNucs.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					listNucs.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					listNucs.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(listNucs, sceneImgG2);
			}
			break;
		  case ComplexDefines.InRNASSData :
		  	SSData2D sstr = getCurrentWorkSStr();
			if (sstr == null)
			{
				alert("Must pick Secondary Structure to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				sstr.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					sstr.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				sstr.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				editErase(sstr, ComplexDefines.InRNASSData);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					sstr.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					sstr.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					sstr.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					sstr.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					sstr.setSymbols(getDefaultDrawParallelogramObject());
				editRedraw(sstr, sceneImgG2);
			}
			break;
		  case ComplexDefines.InComplex :
		  	ComplexSSDataCollection2D complex = getCurrentWorkRNASSComplex();
			if (complex == null)
			{
				alert("Must pick Complex to annotate");
				return;
			}
			if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
			{
				complex.setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
				complexSceneView.runRenderBt();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					complex.runSetIsNucPath(actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
				complexSceneView.renderDrawObjectView();
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
			{
				complex.clearAnnotations();
				complexSceneView.runRenderBt();
			}
			else
			{
				Graphics2D g2 = complexSceneView.getCurrentGraphics2D();
				complex.erase(g2);
				if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
					complex.setColor(getColorChooser().getColor());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
					complex.setFonts(getFontChooser().getCurrentFont());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
					complex.setSymbols(getDefaultDrawCircleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
					complex.setSymbols(getDefaultDrawTriangleObject());
				else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
					complex.setSymbols(getDefaultDrawParallelogramObject());
				g2.setTransform(complex.getParentG2Transform());
				editRedraw(complex, g2);
			}
			break;

		  /* NEED to get label, not nuc
		  case ComplexDefines.InLabelsOnly :
			Nuc2D nuc = getCurrentWorkNuc();
			if (nuc == null)
			{
				alert("Must pick nuc to annotate");
				return;
			}
			g2.setTransform(nuc.getParentG2Transform());
			nuc.erase(g2);

			if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
				nuc.setColor(getColorChooser().getColor());
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
				nuc.setFont(getFontChooser().getCurrentFont());
			break;
		  */

			/*
		  case ComplexDefines.InComplexArea :
			break;
			*/

		  case ComplexDefines.InComplexScene :
			if (actionCmd.equals(ComplexDefines.ANNOTATE_COLOR))
				getCurrentWorkComplexScene().setColor(getColorChooser().getColor());
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_FONT))
				getCurrentWorkComplexScene().setFonts(getFontChooser().getCurrentFont());
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CIRCLE))
				getCurrentWorkComplexScene().setSymbols(getDefaultDrawCircleObject());
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_TRIANGLE))
				getCurrentWorkComplexScene().setSymbols(getDefaultDrawTriangleObject());
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_PARALLELOGRAM))
				getCurrentWorkComplexScene().setSymbols(getDefaultDrawParallelogramObject());
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_SCHEMATICIZE))
				getCurrentWorkComplexScene().setAllNucsIsSchematic(
					actionCmd.equals(ComplexDefines.ANNOTATE_SCHEMATICIZE),
					Double.parseDouble(schemLineWidth_TF.getText()),
					Double.parseDouble(schemBPLineWidth_TF.getText()),
					getColorChooser().getColor());
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH) ||
				actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR_NUC_PATH))
			{
				try
				{
					getCurrentWorkComplexScene().runSetIsNucPath(
						actionCmd.equals(ComplexDefines.ANNOTATE_NUC_PATH),
						Double.parseDouble(nucPathLineWidth_TF.getText().trim()),
						getColorChooser().getColor());
				}
				catch (NumberFormatException e)
				{
					alert("Invalid path width value: " + nucPathLineWidth_TF.getText().trim());
					return;
				}
			}
			else if (actionCmd.equals(ComplexDefines.ANNOTATE_CLEAR))
				getCurrentWorkComplexScene().clearAnnotations();

			complexSceneView.runRenderBt();

			break;
		  default :
		  	break;
		}
		}
		catch (Exception e)
		{
			complexSceneView.handleException("Exception in ComplexSceneAnnotate.AnnotateBtListener:", e, 101);
		}
	}
}

public Box
showRNASingleNucProperties()
throws Exception
{
	Box mainPanel = super.showRNASingleNucProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);

	JButton setCurrentColorFromBt = this.getNewViewButton("Set Current Color From");
	setCurrentColorFromBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getColorChooser().setColor(getCurrentWorkNuc().getColor());
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneAnnotate.setCurrentColorFromBt:", e, 101);
			}
		}
	});
	mainPanel.add(this.getNewViewButtonLeftPanel(setCurrentColorFromBt));
	
	return (mainPanel);
}

public Box
showRNASingleStrandProperties()
throws Exception
{
	Box mainPanel = super.showRNASingleStrandProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNABasePairProperties()
throws Exception
{
	Box mainPanel = super.showRNABasePairProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNAHelixProperties()
throws Exception
{
	Box mainPanel = super.showRNAHelixProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNAStackedHelixProperties()
throws Exception
{
	Box mainPanel = super.showRNAStackedHelixProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNASubDomainProperties()
throws Exception
{
	Box mainPanel = super.showRNASubDomainProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNANamedGroupProperties()
throws Exception
{
	Box mainPanel = super.showRNANamedGroupProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNAColorUnitProperties()
throws Exception
{
	Box mainPanel = super.showRNAColorUnitProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNACycleProperties()
throws Exception
{
	Box mainPanel = super.showRNACycleProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNAListNucsProperties()
throws Exception
{
	Box mainPanel = super.showRNAListNucsProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showRNASSDataProperties()
throws Exception
{
	Box mainPanel = super.showRNASSDataProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showComplexProperties()
throws Exception
{
	Box mainPanel = super.showComplexProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showLabelsOnlyProperties()
throws Exception
{
	Box mainPanel = super.showLabelsOnlyProperties();
	this.addTextAnnotateProperties(mainPanel);
	
	return (mainPanel);
}

public Box
showComplexSceneProperties()
throws Exception
{
	Box mainPanel = super.showComplexSceneProperties();
	this.addTextAnnotateProperties(mainPanel);
	this.addSymbolAnnotateProperties(mainPanel);

	JButton setCurrentBGColorBt = this.getNewViewButton("Set Background Color");
	// setCurrentBGColorBt.setActionCommand(ComplexDefines.ANNOTATE_BG_COLOR);
	setCurrentBGColorBt.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				complexSceneView.setDrawImgBGColor(getColorChooser().getColor());
				complexSceneView.renderDrawObjectView();
			}
			catch (Exception e)
			{
				complexSceneView.handleException("Exception in ComplexSceneAnnotate.applyBt:", e, 101);
			}
		}
	});

	mainPanel.add(this.getNewViewButtonPanel(setCurrentBGColorBt));

	return (mainPanel);
}

public void
runMousePressed(MouseEvent event)
throws Exception
{
	super.runMousePressed(event);

	DrawObject drawObject = this.getCurrentDrawObject();
	
	switch (complexSceneView.getCurrentMouseState())
	{
	  case 16 : // lft mouse bt
		break;
	  case 4 : // rht mouse bt
	  case 5 : // rht mouse bt
		if (!this.currentVarsSet)
			return;
		boolean test = initTabScene();
	  	return;
	  case 20 : // both lft & rht mouse bt
	  	return;
	  default :
	  	return;
	}

	if (inquireOnlyRB.isSelected())
	{
		resetCurrentVars(false);
		return;
	}

	resetCurrentVars(true);

	if (drawObject != null)
	{
		// alert("Need to pick background");
		return;
	}
	
	if (!initTabScene())
		return;
	
	if (addStringLabel_RB.isSelected())
		this.runAddExtraneousTextLabel();
	else if (addLineLabel_RB.isSelected())
		this.runAddExtraneousLineLabel();
	else if (addTriangleLabel_RB.isSelected())
		this.runAddExtraneousTriangleLabel();
	else if (addCircleLabel_RB.isSelected())
		this.runAddExtraneousCircleLabel();
	else if (addParallelogramLabel_RB.isSelected())
		this.runAddExtraneousParallelogramLabel();
	else if (addArrowLabel_RB.isSelected())
		this.runAddExtraneousArrowLabel();
}

private void
runAddExtraneousTextLabel()
throws Exception
{
	String strLabel = addStringLabel_TF.getText().trim();
	ComplexCollection collection = complexSceneView.getComplexTreePick();
	DrawObjectCollection ptr = (DrawObjectCollection)collection;
	double deltaX = 0.0;
	double deltaY = 0.0;
	while (ptr != null)
	{
		deltaX += ptr.getX();
		deltaY += ptr.getY();
		ptr = (DrawObjectCollection)ptr.getParentCollection();
	}

	DrawStringObject strObj = new DrawStringObject(
		complexSceneView.getCurrentViewX(),
		-complexSceneView.getCurrentViewY(),
		getFontChooser().getCurrentFont(), getColorChooser().getColor(),
		strLabel.trim());

	strObj.update(GraphicsUtil.unitG2);
	strObj.setX(strObj.getX() + strObj.getDeltaX() - deltaX);
	strObj.setY(strObj.getY() + strObj.getDeltaY() - deltaY);

	collection.addLabel(strObj);
	complexSceneView.renderDrawObjectView();
	// debug("strObj: " + strObj + " " + strObj.getX() + " " + strObj.getY());
	// animateEdit(strObj);
}

private void
runAddExtraneousLineLabel()
throws Exception
{
	ComplexCollection collection = complexSceneView.getComplexTreePick();
	DrawObjectCollection ptr = (DrawObjectCollection)collection;
	double deltaX = 0.0;
	double deltaY = 0.0;
	while (ptr != null)
	{
		deltaX += ptr.getX();
		deltaY += ptr.getY();
		ptr = (DrawObjectCollection)ptr.getParentCollection();
	}

	double length = this.getDefaultDrawLineObject().getLength();
	DrawLineObject lineObj = new DrawLineObject(
		complexSceneView.getCurrentViewX() - 0.5*length,
		-complexSceneView.getCurrentViewY(),
		complexSceneView.getCurrentViewX() + 0.5*length,
		-complexSceneView.getCurrentViewY(),
		this.getDefaultDrawLineObject().getLineWidth(),
		this.getDefaultDrawLineObject().getColor());
	lineObj.setFromLength(length);
	lineObj.setFromAngle(this.getDefaultDrawLineObject().getAngle());
	
	/*
	DrawLineObject lineObj =
		new DrawLineObject(this.getDefaultDrawLineObject());
	lineObj.setX(complexSceneView.getCurrentViewX());
	lineObj.setY(-complexSceneView.getCurrentViewY());
	lineObj.update(GraphicsUtil.unitG2);
	lineObj.setX(lineObj.getX() + lineObj.getDeltaX() - deltaX);
	lineObj.setY(lineObj.getY() + lineObj.getDeltaY() - deltaY);
	*/

	lineObj.update(GraphicsUtil.unitG2);
	BLine2D tmpLine = lineObj.getBLine2D();
	lineObj.reset(
		tmpLine.getP1().getX() - deltaX,
		tmpLine.getP1().getY() - deltaY,
		tmpLine.getP2().getX() - deltaX,
		tmpLine.getP2().getY() - deltaY);

	collection.addLabel(lineObj);
	complexSceneView.renderDrawObjectView();
	// animateEdit(lineObj);
}

private void
runAddExtraneousTriangleLabel()
throws Exception
{
	/*
	double length = 0.0;
	try
	{
		length = Double.parseDouble(addTriangleLabelLength_TF.getText().trim());
	}
	catch (NumberFormatException e)
	{
		alert("Invalid line length value: " + addTriangleLabelLength_TF.getText().trim());
		return;
	}

	double angle = 0.0;
	try
	{
		angle = Double.parseDouble(addTriangleLabelAngle_TF.getText().trim());
	}
	catch (NumberFormatException e)
	{
		alert("Invalid line length value: " + addTriangleLabelAngle_TF.getText().trim());
		return;
	}
	*/

	ComplexCollection collection = complexSceneView.getComplexTreePick();
	DrawObjectCollection ptr = (DrawObjectCollection)collection;
	double deltaX = 0.0;
	double deltaY = 0.0;
	while (ptr != null)
	{
		deltaX += ptr.getX();
		deltaY += ptr.getY();
		ptr = (DrawObjectCollection)ptr.getParentCollection();
	}

	DrawTriangleObject triangleObj =
		new DrawTriangleObject(this.getDefaultDrawTriangleObject());

	triangleObj.setX(complexSceneView.getCurrentViewX());
	triangleObj.setY(-complexSceneView.getCurrentViewY());

	triangleObj.update(GraphicsUtil.unitG2);
	triangleObj.setX(triangleObj.getX() + triangleObj.getDeltaX() - deltaX);
	triangleObj.setY(triangleObj.getY() + triangleObj.getDeltaY() - deltaY);

	collection.addLabel(triangleObj);
	complexSceneView.renderDrawObjectView();
	// animateEdit(lineObj);
}

private void
runAddExtraneousCircleLabel()
throws Exception
{
	ComplexCollection collection = complexSceneView.getComplexTreePick();
	DrawObjectCollection ptr = (DrawObjectCollection)collection;
	double deltaX = 0.0;
	double deltaY = 0.0;
	while (ptr != null)
	{
		deltaX += ptr.getX();
		deltaY += ptr.getY();
		ptr = (DrawObjectCollection)ptr.getParentCollection();
	}

	DrawCircleObject circleObj =
		new DrawCircleObject(this.getDefaultDrawCircleObject());

	circleObj.setX(complexSceneView.getCurrentViewX());
	circleObj.setY(-complexSceneView.getCurrentViewY());

	circleObj.update(GraphicsUtil.unitG2);
	circleObj.setX(circleObj.getX() + (0.5 * circleObj.getDeltaX()) - deltaX);
	circleObj.setY(circleObj.getY() + (0.5 * circleObj.getDeltaY()) - deltaY);

	collection.addLabel(circleObj);
	complexSceneView.renderDrawObjectView();
	// animateEdit(lineObj);
}

private void
runAddExtraneousParallelogramLabel()
throws Exception
{
	ComplexCollection collection = complexSceneView.getComplexTreePick();
	DrawObjectCollection ptr = (DrawObjectCollection)collection;
	double deltaX = 0.0;
	double deltaY = 0.0;
	while (ptr != null)
	{
		deltaX += ptr.getX();
		deltaY += ptr.getY();
		ptr = (DrawObjectCollection)ptr.getParentCollection();
	}

	DrawParallelogramObject parallelogramObj =
		new DrawParallelogramObject(this.getDefaultDrawParallelogramObject());

	parallelogramObj.setX(complexSceneView.getCurrentViewX());
	parallelogramObj.setY(-complexSceneView.getCurrentViewY());

	parallelogramObj.update(GraphicsUtil.unitG2);
	parallelogramObj.setX(parallelogramObj.getX() + parallelogramObj.getDeltaX() - deltaX);
	parallelogramObj.setY(parallelogramObj.getY() + parallelogramObj.getDeltaY() - deltaY);

	collection.addLabel(parallelogramObj);
	complexSceneView.renderDrawObjectView();
	// animateEdit(lineObj);
}

private void
runAddExtraneousArrowLabel()
throws Exception
{
	ComplexCollection collection = complexSceneView.getComplexTreePick();
	DrawObjectCollection ptr = (DrawObjectCollection)collection;
	double deltaX = 0.0;
	double deltaY = 0.0;
	while (ptr != null)
	{
		deltaX += ptr.getX();
		deltaY += ptr.getY();
		ptr = (DrawObjectCollection)ptr.getParentCollection();
	}

	DrawArrowObject arrowObj =
		new DrawArrowObject(this.getDefaultDrawArrowObject());

	arrowObj.setX(complexSceneView.getCurrentViewX());
	arrowObj.setY(-complexSceneView.getCurrentViewY());

	arrowObj.update(GraphicsUtil.unitG2);
	arrowObj.setX(arrowObj.getX() + arrowObj.getDeltaX() - deltaX);
	arrowObj.setY(arrowObj.getY() + arrowObj.getDeltaY() - deltaY);

	collection.addLabel(arrowObj);
	complexSceneView.renderDrawObjectView();
	// animateEdit(lineObj);
}

// NEED to readjust this to be in Work along with Format and IO.
public boolean
initTabScene()
throws Exception
{
	if (!super.initTabScene())
		return (false);
	
	this.initTabGraphics();

	return (true);
}

/*************  END MOUSE STUFF *********************/

private void
resetAnnotateRBs()
{
	inquireOnlyRB.setSelected(false);
	addStringLabel_RB.setSelected(false);
	addLineLabel_RB.setSelected(false);
	addTriangleLabel_RB.setSelected(false);
	addCircleLabel_RB.setSelected(false);
	addParallelogramLabel_RB.setSelected(false);
	addArrowLabel_RB.setSelected(false);
}

public DrawCircleObject
getDefaultDrawCircleObject()
{
	return ((DrawCircleObject)
		((DefaultDrawObjectPanel)defaultCirclePanel).getDefaultDrawObject());
}

public DrawTriangleObject
getDefaultDrawTriangleObject()
{
	return ((DrawTriangleObject)
		((DefaultDrawObjectPanel)defaultTrianglePanel).getDefaultDrawObject());
}

public DrawParallelogramObject
getDefaultDrawParallelogramObject()
{
	return ((DrawParallelogramObject)
		((DefaultDrawObjectPanel)defaultParallelogramPanel).getDefaultDrawObject());
}

public DrawLineObject
getDefaultDrawLineObject()
throws Exception
{
	/*
	return (new DrawLineObject(-10.0, 0.0, 10.0, 0.0,
		0.5, Color.black));
	*/

	return ((DrawLineObject)
		((DefaultDrawObjectPanel)defaultLinePanel).getDefaultDrawObject());
}

public DrawArrowObject
getDefaultDrawArrowObject()
{
	return ((DrawArrowObject)
		((DefaultDrawObjectPanel)defaultArrowPanel).getDefaultDrawObject());
}

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
			// debug("IN ANNOTATE TAB COLOR STATE CHANGED");
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
		// debug("MADE IT TO ANNOTATE.pickMode: " + this.getCurrentComplexPickMode());
		// this.setCurrentComplexPickMode((int)((Integer)evt.getNewValue()).intValue());
		int oldCurrentComplexPickMode = this.getCurrentComplexPickMode();
		// debug("FIRING: " + propertyChangeListeners.hasListeners("CurrentComplexPickMode"));
		propertyChangeListeners.firePropertyChange(
			"CurrentComplexPickMode",
			new Integer(-1), new Integer(this.getCurrentComplexPickMode()));
	}
}

/************** End PropertyChangeListener Implementaion *************/

/************** TreeSelectionListener Implementaion ***************/

/* NEED to get a method that returns string connected with curr obj
JLabel currObjLabel = "current object
controlBtPanel.add(this.getNewViewLabel("label text:"));
*/

public String
currentContainerName()
{
	try
	{
		ComplexCollection complexNode = complexSceneView.getComplexTreePick();
		if (complexNode instanceof ComplexScene2D)
			return ("Scene: " + ((ComplexScene)complexNode).getName());
		if (complexNode instanceof ComplexSSDataCollection2D)
			return ("RNA Strand Collection: " + ((ComplexScene)complexNode).getName());
		if (complexNode instanceof SSData2D)
			return ("RNA Strand: " + ((ComplexScene)complexNode).getName());
	}
	catch (Exception ex)
	{
		return ("No Container");
		// complexSceneView.handleException("Exception in ComplexSceneAnnotate.TreeSelectionListener.valueChanged:", ex, 101);
	}
	return ("No Container");
}

public void
valueChanged(TreeSelectionEvent e)
{
	super.valueChanged(e);

	try
	{
		ComplexCollection complexNode = complexSceneView.getComplexTreePick();
		// debug("CURRENT: " + complexNode.toString());
	}
	catch (Exception ex)
	{
		complexSceneView.handleException("Exception in ComplexSceneAnnotate.TreeSelectionListener.valueChanged:", ex, 101);
	}
}

/************** End TreeSelectionListener Implementaion ***************/

private static void
debug(String s)
{
	System.err.println("ComplexSceneAnnotateTab-> " + s);
}

}
