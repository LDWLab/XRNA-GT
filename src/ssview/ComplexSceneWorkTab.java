package ssview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import jimage.DrawArrowObject;
import jimage.DrawCharObject;
import jimage.DrawCircleObject;
import jimage.DrawLineObject;
import jimage.DrawObject;
import jimage.DrawObjectCollection;
import jimage.DrawObjectLeafNode;
import jimage.DrawObjectView;
import jimage.DrawParallelogramObject;
import jimage.DrawStringObject;
import jimage.DrawTriangleObject;
import jimage.FontChooser;
import jimage.GenFileFilter;
import jimage.ViewImgCanvas;
import util.GraphicsUtil;
import util.math.BRectangle2D;

public class
ComplexSceneWorkTab
extends JPanel
implements PropertyChangeListener, TreeExpansionListener,
	TreeSelectionListener
{
public int lftInset = 2;
public Insets btInsets = null;

public JPanel controlPanel = null;
public JPanel controlBtPanel = null;

public static JInternalFrame complexPropertiesFrame = null;

public Box complexPropertiesPanel = null;
public JScrollPane controlBtPanelScrollPane = null;

public boolean mouseIsPressed = false;

public void
buildGui(Color guiBGColor, int panelW, int panelH)
{
	this.setLayout(new BorderLayout());
	this.setFont(new Font("Dialog", Font.BOLD, 12));
	this.setBackground(guiBGColor);
	this.setForeground(Color.black);

	controlPanel = new JPanel(new BorderLayout(), true);
	controlPanel.setPreferredSize(new Dimension(panelW, panelH));

	this.add(BorderLayout.WEST, controlPanel);
}

public void
reset()
throws Exception
{
	this.setCurrentWorkListNucs(null);
	this.resetCurrentVars(false);
}

public Color guiBGColor = Color.white;

public boolean
isCurrentTabPanel()
{
	return ((JPanel)complexSceneView.complexTabbedPane.
		getSelectedComponent() == this);
}

protected ComplexSceneView complexSceneView = null;
protected ViewImgCanvas viewImgCanvas = null;
protected JFileChooser genFileChooser = null;
protected GenFileFilter genWriteFileFilter = null;
protected GenFileFilter genWriteFileFilterCSV = null;
protected GenFileFilter genWriteFileFilterSVG = null;
protected GenFileFilter genWriteFileFilterTR = null;

public void
setParentDrawObjectView(DrawObjectView parentDrawObjectView)
{
	complexSceneView = (ComplexSceneView)parentDrawObjectView;
}

public void
setPostBuildGuiMethods()
throws Exception
{
	viewImgCanvas = complexSceneView.getViewImgCanvas();	
	genFileChooser = complexSceneView.genFileChooser;
	genWriteFileFilter = complexSceneView.genWriteFileFilter;
	genWriteFileFilterCSV = complexSceneView.genWriteFileFilterCSV;
	genWriteFileFilterSVG = complexSceneView.genWriteFileFilterSVG;
	genWriteFileFilterTR = complexSceneView.genWriteFileFilterTR;
}

public void
setMouseMethod()
{
	if (complexSceneView.getViewImgCanvas() == null)
		return;
	complexSceneView.getViewImgCanvas().addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			if (!isCurrentTabPanel())
				return;
			requestFocus();
			complexSceneView.setCurrentViewX(event.getX());
			complexSceneView.setCurrentViewY(event.getY());

			complexSceneView.setMousePressedImgSpaceX(event.getX());
			complexSceneView.setMousePressedImgSpaceY(event.getY());

			complexSceneView.setCurrentMouseState(event.getModifiers());

			try
			{
				runMousePressed(event);
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}

		public void
		mouseReleased(MouseEvent event)
		{
			if (!isCurrentTabPanel())
				return;
			complexSceneView.setCurrentViewX(event.getX());
			complexSceneView.setCurrentViewY(event.getY());
			try
			{
				runMouseReleased();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}

		public void
		mouseEntered(MouseEvent event)
		{
			// NEED to get REFOCUS WORKING here

			// debug("MOUSE ENTERED");

			/*
			if (ComplexSceneWorkTab.this instanceof ComplexSceneIOTab)
			{
				debug("IN ComplexSceneIOTab");
				((ComplexSceneIOTab)ComplexSceneWorkTab.this).requestFocus();
			}
			else if (ComplexSceneWorkTab.this instanceof ComplexSceneAnnotateTab)
			{
				debug("IN ComplexSceneAnnotateTab");
				((ComplexSceneAnnotateTab)ComplexSceneWorkTab.this).requestFocus();
			}
			else if (ComplexSceneWorkTab.this instanceof ComplexSceneEditTab)
			{
				debug("IN ComplexSceneEditTab");
				((ComplexSceneEditTab)ComplexSceneWorkTab.this).requestFocus();
			}
			else if (ComplexSceneWorkTab.this instanceof ComplexSceneFormatSStrTab)
			{
				debug("IN ComplexSceneFormatSStrTab");
				((ComplexSceneFormatSStrTab)ComplexSceneWorkTab.this).requestFocus();
			}
			*/
			// requestFocus();

			// ComplexSceneWorkTab.this.requestFocus();

			// MIGHT HAVE TO PUT BACK IN: (03/11/03)
			// Messes up with scrolling if comment out
			complexSceneView.getViewImgCanvas().requestFocus();

			// complexSceneView.getViewImgCanvas().grabFocus();

			// always seems to be true:
			// debug("FOCUS ENABLED??: " + complexSceneView.getViewImgCanvas().isRequestFocusEnabled());
			// debug("HAS FOCUS??: " + complexSceneView.getViewImgCanvas().hasFocus());
		}

		public void
		mouseExited(MouseEvent event)
		{
		}

	});

	complexSceneView.getViewImgCanvas().addMouseMotionListener(new MouseMotionListener()
	{
		public void
		mouseDragged(MouseEvent event)
		{
			if (!isCurrentTabPanel())
				return;
			try
			{
				runMouseDragged(event.getX(), event.getY());
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}

		public void
		mouseMoved(MouseEvent event)
		{
			if (!isCurrentTabPanel())
				return;

			complexSceneView.setCurrentViewX(event.getX());
			complexSceneView.setCurrentViewY(event.getY());

			/* SVE for debug purposes
			if (complexSceneView.getShowMousePos())
				complexSceneView.printCurrentMousePosInfo();
			*/

			try
			{
				runMouseMoved();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
}

public void
setKeyMethod()
{
	this.addKeyListener(new KeyAdapter()
	{
		public void
		keyTyped(KeyEvent keyEvent)
		{
		}

		public void
		keyPressed(KeyEvent keyEvent)
		{
		}

		public void
		keyReleased(KeyEvent keyEvent)
		{
		}
	});
}

public int
incCurrentUndoLevel()
{
	return (complexSceneView.incCurrentUndoLevel());
}

private int currentComplexPickMode = 0;

public void
setCurrentComplexPickMode(int currentComplexPickMode)
{
    this.currentComplexPickMode = currentComplexPickMode;
}

public int
getCurrentComplexPickMode()
{
    return (this.currentComplexPickMode);
}

private DrawObject currentDrawObject = null;

public void
setCurrentDrawObject(DrawObject currentDrawObject)
{
	this.currentDrawObject = currentDrawObject;
}

public DrawObject
getCurrentDrawObject()
{
	return (this.currentDrawObject);
}

private FontChooser fontChooser = null;

public void
setFontChooser(FontChooser fontChooser)
{
    this.fontChooser = fontChooser;
}

public FontChooser
getFontChooser()
{
    return (this.fontChooser);
}

public void
setColorChangedState()
{
}

private JColorChooser colorChooser = null;

public void
setColorChooser(JColorChooser colorChooser)
{
    this.colorChooser = colorChooser;
}

public JColorChooser
getColorChooser()
{
    return (this.colorChooser);
}


/*************  START MOUSE STUFF *********************/

public void
runMousePressed(MouseEvent event)
throws Exception
{
	if ((complexSceneView == null) || (complexSceneView.getComplexScene() == null))
		return;
	complexSceneView.clearConsole();

	Vector excludeTypes = null;
	if (getCurrentComplexPickMode() == ComplexDefines.InLabelsOnly)
	{
		excludeTypes = new Vector();
		excludeTypes.add("Nuc2D");
	}

	if (complexPropertiesPanel != null)
		complexPropertiesPanel.setVisible(false);
	if (complexPropertiesFrame != null)
		complexPropertiesFrame.setVisible(false);

	// debug("Currend DRWOBJ: " + this.getCurrentDrawObject());

	// debug("IN TRUE FINDLEAFNODE: " + complexSceneView.getCurrentViewX() + " " + complexSceneView.getCurrentViewY());
	this.setCurrentDrawObject(
		(DrawObject)complexSceneView.getComplexScene().findLeafNode(
			complexSceneView.getCurrentViewX(), complexSceneView.getCurrentViewY(), null, excludeTypes));

	complexSceneView.fileEdited = true;
	
	switch (complexSceneView.getCurrentMouseState())
	{
	  case 16 : // lft mouse bt
		if (this.getCurrentDrawObject() != null)
			complexSceneView.printConsole(ComplexScene.printDrawObjectPath(
				(DrawObjectCollection)this.getCurrentDrawObject()));	
		break;
	  case 24 : // lft mouse bt + alt key
		if (event.isAltDown()) // redundant
		{
		JScrollBar scrollBarY =
			complexSceneView.genImgScrollPane.getVerticalScrollBar();
		JScrollBar scrollBarX =
			complexSceneView.genImgScrollPane.getHorizontalScrollBar();
		int scrollAdjX = 
			scrollBarX.getMinimum() +
			((scrollBarX.getMaximum() - scrollBarX.getMinimum() -
				scrollBarX.getVisibleAmount())/2) -
					scrollBarX.getValue();

		complexSceneView.getComplexScene().setX(
			complexSceneView.getComplexScene().getX() -
				complexSceneView.getCurrentViewX() -
					((double)scrollAdjX / complexSceneView.getFigureScale())
				);

		int scrollAdjY = 
			scrollBarY.getMinimum() +
			((scrollBarY.getMaximum() - scrollBarY.getMinimum() -
				scrollBarY.getVisibleAmount())/2) -
					scrollBarY.getValue();

		complexSceneView.getComplexScene().setY(
			complexSceneView.getComplexScene().getY() +
				complexSceneView.getCurrentViewY() +
					((double)scrollAdjY / complexSceneView.getFigureScale())
				);

		complexSceneView.runRenderBt();

		}
		break;
	  case 4 : // rht mouse bt
		runRightMouseClick();
	  	break;
	  case 20 : // both lft & rht mouse bt
	  	break;
	  default :
	  	break;
	}
}

private JTextField localXTF = null;
private JTextField localYTF = null;
private JTextField nameTF = null;
public Font btFont = new Font("Dialog", Font.BOLD, 12);
public Font smBtFont = new Font("Dialog", Font.BOLD, 10);
public Font labelFont = new Font("Dialog", Font.BOLD, 11);
public Font headerFont = new Font("Dialog", Font.BOLD, 14);

private DrawObjectCollection currentWorkParent = null;

public void
setCurrentWorkParent(DrawObjectCollection currentWorkParent)
{
    this.currentWorkParent = currentWorkParent;
}

public DrawObjectCollection
getCurrentWorkParent()
{
    return (this.currentWorkParent);
}

private Nuc2D currentWorkNuc = null;

public void
setCurrentWorkNuc(Nuc2D currentWorkNuc)
{
    this.currentWorkNuc = currentWorkNuc;
}

public Nuc2D
getCurrentWorkNuc()
{
    return (this.currentWorkNuc);
}

private RNASingleStrand2D currentWorkSingleStrand = null;

public void
setCurrentWorkSingleStrand(RNASingleStrand2D currentWorkSingleStrand)
{
    this.currentWorkSingleStrand = currentWorkSingleStrand;
}

public RNASingleStrand2D
getCurrentWorkSingleStrand()
{
    return (this.currentWorkSingleStrand);
}

private RNABasePair2D currentWorkBasePair = null;

public void
setCurrentWorkBasePair(RNABasePair2D currentWorkBasePair)
{
    this.currentWorkBasePair = currentWorkBasePair;
}

public RNABasePair2D
getCurrentWorkBasePair()
{
    return (this.currentWorkBasePair);
}

private RNAHelix2D currentWorkHelix = null;

public void
setCurrentWorkHelix(RNAHelix2D currentWorkHelix)
{
    this.currentWorkHelix = currentWorkHelix;
}

public RNAHelix2D
getCurrentWorkHelix()
{
    return (this.currentWorkHelix);
}

private RNAStackedHelix2D currentWorkStackedHelix = null;

public void
setCurrentWorkStackedHelix(RNAStackedHelix2D currentWorkStackedHelix)
{
    this.currentWorkStackedHelix = currentWorkStackedHelix;
}

public RNAStackedHelix2D
getCurrentWorkStackedHelix()
{
    return (this.currentWorkStackedHelix);
}

private RNASubDomain2D currentWorkSubDomain = null;

public void
setCurrentWorkSubDomain(RNASubDomain2D currentWorkSubDomain)
{
    this.currentWorkSubDomain = currentWorkSubDomain;
}

public RNASubDomain2D
getCurrentWorkSubDomain()
{
    return (this.currentWorkSubDomain);
}

private RNANamedGroup2D currentWorkNamedGroup = null;

public void
setCurrentWorkNamedGroup(RNANamedGroup2D currentWorkNamedGroup)
{
    this.currentWorkNamedGroup = currentWorkNamedGroup;
}

public RNANamedGroup2D
getCurrentWorkNamedGroup()
{
    return (this.currentWorkNamedGroup);
}

private RNAColorUnit2D currentWorkColorUnit = null;

public void
setCurrentWorkColorUnit(RNAColorUnit2D currentWorkColorUnit)
{
    this.currentWorkColorUnit = currentWorkColorUnit;
}

public RNAColorUnit2D
getCurrentWorkColorUnit()
{
    return (this.currentWorkColorUnit);
}

private RNACycle2D currentWorkCycle = null;

public void
setCurrentWorkCycle(RNACycle2D currentWorkCycle)
{
    this.currentWorkCycle = currentWorkCycle;
}

public RNACycle2D
getCurrentWorkCycle()
{
    return (this.currentWorkCycle);
}

private RNAListNucs2D currentWorkListNucs = null;

public void
setCurrentWorkListNucs(RNAListNucs2D currentWorkListNucs)
{
    this.currentWorkListNucs = currentWorkListNucs;
}

public RNAListNucs2D
getCurrentWorkListNucs()
{
    return (this.currentWorkListNucs);
}

private SSData2D currentWorkSStr = null;

public void
setCurrentWorkSStr(SSData2D currentWorkSStr)
{
    this.currentWorkSStr = currentWorkSStr;
}

public SSData2D
getCurrentWorkSStr()
{
    return (this.currentWorkSStr);
}

private ComplexSSDataCollection2D currentWorkRNASSComplex = null;

public void
setCurrentWorkRNASSComplex(ComplexSSDataCollection2D currentWorkRNASSComplex)
{
	this.currentWorkRNASSComplex = currentWorkRNASSComplex;
}

public ComplexSSDataCollection2D
getCurrentWorkRNASSComplex()
{
	return (this.currentWorkRNASSComplex);
}

private DrawObject currentWorkLabel = null;

public void
setCurrentWorkLabel(DrawObject currentWorkLabel)
{
    this.currentWorkLabel = currentWorkLabel;
}

public DrawObject
getCurrentWorkLabel()
{
    return (this.currentWorkLabel);
}

/*
private RNAComplexArea2D currentWorkComplexArea = null;

public void
setCurrentWorkComplexArea(RNAComplexArea2D currentWorkComplexArea)
{
    this.currentWorkComplexArea = currentWorkComplexArea;
}

public RNAComplexArea2D
getCurrentWorkComplexArea()
{
    return (this.currentWorkComplexArea);
}
*/

private ComplexScene2D currentComplexScene = null;

public void
setCurrentWorkComplexScene(ComplexScene2D currentComplexScene)
{
    this.currentComplexScene = currentComplexScene;
}

public ComplexScene2D
getCurrentWorkComplexScene()
{
    return (this.currentComplexScene);
}

public DrawObject
currentDrawObjectStructure()
{
	switch (getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		if (getCurrentWorkNuc() != null)
			return ((DrawObject)getCurrentWorkNuc());
		break;
	  case ComplexDefines.InRNASingleStrand :
		if (getCurrentWorkSingleStrand() != null)
			return ((DrawObject)getCurrentWorkSingleStrand());
		break;
	  case ComplexDefines.InRNABasePair :
		if (getCurrentWorkBasePair() != null)
			return ((DrawObject)getCurrentWorkBasePair());
		break;
	  case ComplexDefines.InRNAHelix :
		if (getCurrentWorkHelix() != null)
			return ((DrawObject)getCurrentWorkHelix());
		break;
	  case ComplexDefines.InRNAHelicalRun :
		if (getCurrentWorkStackedHelix() != null)
			return ((DrawObject)getCurrentWorkStackedHelix());
		break;
	  case ComplexDefines.InRNASubDomain :
		if (getCurrentWorkSubDomain() != null)
			return ((DrawObject)getCurrentWorkSubDomain());
		break;
	  case ComplexDefines.InRNANamedGroup :
		if (getCurrentWorkNamedGroup() != null)
			return ((DrawObject)getCurrentWorkNamedGroup());
		break;
	  case ComplexDefines.InRNAColorUnit :
		if (getCurrentWorkColorUnit() != null)
			return ((DrawObject)getCurrentWorkColorUnit());
		break;
	  case ComplexDefines.InRNACycle :
		if (getCurrentWorkCycle() != null)
			return ((DrawObject)getCurrentWorkCycle());
		break;
	  case ComplexDefines.InRNAListNucs :
		if (getCurrentWorkListNucs() != null)
			return ((DrawObject)getCurrentWorkListNucs());
		break;
	  case ComplexDefines.InRNASSData :
		if (getCurrentWorkSStr() != null)
			return ((DrawObject)getCurrentWorkSStr());
		break;
	  case ComplexDefines.InComplex :
		if (getCurrentWorkRNASSComplex() != null)
			return ((DrawObject)getCurrentWorkRNASSComplex());
		break;
	  case ComplexDefines.InLabelsOnly :
		if (getCurrentWorkLabel() != null)
			return ((DrawObject)getCurrentWorkLabel());
		break;
		/*
	  case ComplexDefines.InComplexArea :
		if (getCurrentWorkComplexArea() != null)
			return ((DrawObject)getCurrentWorkComplexArea());
		break;
		*/
	  case ComplexDefines.InComplexScene :
		if (getCurrentWorkComplexScene() != null)
			return ((DrawObject)getCurrentWorkComplexScene());
		break;
	  default :
		break;
	}
	// hopefully this means a label was picked:
	return ((DrawObject)getCurrentWorkLabel());
}

public DrawObject currentWorkDrawObject = null;
public boolean currentVarsSet = false;

public boolean
resetCurrentVars(boolean constrain)
throws Exception
{
	currentVarsSet = false;
	currentWorkDrawObject = null;

	this.setCurrentWorkParent(null);
	this.setCurrentWorkNuc(null);
	this.setCurrentWorkBasePair(null);
	this.setCurrentWorkSingleStrand(null);
	this.setCurrentWorkHelix(null);
	this.setCurrentWorkStackedHelix(null);
	this.setCurrentWorkSubDomain(null);
	this.setCurrentWorkNamedGroup(null);
	this.setCurrentWorkColorUnit(null);
	this.setCurrentWorkCycle(null);
	if ((this.getCurrentDrawObject() == null) ||
		((this.getCurrentWorkListNucs() != null) &&
			this.getCurrentWorkListNucs().isSet()))
	{
		this.setCurrentWorkListNucs(null);
	}
	this.setCurrentWorkSStr(null);
	this.setCurrentWorkRNASSComplex(null);
	this.setCurrentWorkLabel(null);

	// this should always be the case
	this.setCurrentWorkComplexScene(complexSceneView.getComplexScene());

	if (this.getCurrentDrawObject() != null)
		this.setCurrentWorkParent((DrawObjectCollection)
			this.getCurrentDrawObject().getParentCollection());

	// see if dealing with the label part of a nuc; set currentNuc if so
	if ( /* (getCurrentComplexPickMode() != ComplexDefines.InLabelsOnly) && */ (this.getCurrentWorkParent() != null) &&
		(this.getCurrentWorkParent() instanceof Nuc2D) &&
		(((Nuc2D)this.getCurrentWorkParent()).getNucDrawObject() == this.getCurrentDrawObject()))
		this.setCurrentWorkNuc((Nuc2D)this.getCurrentWorkParent());

	if ((this.getCurrentWorkNuc() != null) && (this.getCurrentWorkNuc().getParentCollection() instanceof SSData2D))
		this.setCurrentWorkSStr((SSData2D)this.getCurrentWorkNuc().getParentCollection());

	// hopefully this defines an extraneous label
	if ((this.getCurrentWorkNuc() == null) && (this.getCurrentDrawObject() instanceof DrawObjectLeafNode))
	{
		this.setCurrentWorkLabel(this.getCurrentDrawObject());
		if (this.getCurrentWorkLabel() instanceof DrawLineObject)
		{
			// set what part of line was picked (divided into thirds).
			AffineTransform affTrans = ((DrawObjectCollection)this.getCurrentWorkLabel().getParentCollection()).getG2Transform();

			Point2D testPt = new Point2D.Double(
				(double)complexSceneView.getMousePressedImgSpaceX(),
				(double)complexSceneView.getMousePressedImgSpaceY());
			Point2D newTestPt = new Point2D.Double();
			affTrans.inverseTransform(testPt, newTestPt);

			((DrawLineObject)this.getCurrentWorkLabel()).setLinePartition(
				newTestPt.getX(), newTestPt.getY());
		}
	}
	
	// label picked, only work on it.
	if (this.getCurrentWorkLabel() != null)
	{
		currentVarsSet = true;
		return (true);
	}

	// label not picked, make sure there is a refnuc to work with
	if (this.getCurrentWorkNuc() == null)
	{
		if (this.getCurrentComplexPickMode() == ComplexDefines.InComplexScene)
		{
			currentVarsSet = true;
			return (true);
		}

		// no refnuc so don't carry on.
		currentVarsSet = false;
		return (false);
	}

	switch (this.getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		if (this.getCurrentWorkNuc() == null)
		{
			if (constrain) alert("Must pick Nuc");
			currentVarsSet = false;
			return (false);
		}
		currentWorkDrawObject = this.getCurrentWorkNuc();
		break;
	  case ComplexDefines.InRNASingleStrand :
		if ((this.getCurrentWorkNuc() == null) ||
			(this.getCurrentWorkNuc().isBasePair()))
		{
			if (constrain) alert("Must pick single stranded nuc");
			currentVarsSet = false;
			return (false);
		}
		try
		{
			this.setCurrentWorkSingleStrand(
				new RNASingleStrand2D(this.getCurrentWorkNuc()));
		}
		catch (ComplexException ce)
		{
			this.setCurrentWorkSingleStrand(null);
			currentVarsSet = false;

			if (ce.getErrorCode() ==
				(ComplexDefines.RNA_SINGLE_STRAND_ERROR + ComplexDefines.CREATE_ERROR))
			{
				if (constrain) alert(ce.getErrorMsg() + "\nTry picking a different nuc in this single strand");
				currentVarsSet = false;
				return (false);
			}
			else
			{
				throw ce;
			}
		}
		
		if (this.getCurrentWorkSingleStrand().isStraightLine())
		{
			this.getCurrentWorkSingleStrand().setLinePartition(
				this.getCurrentWorkNuc());
		}
		currentWorkDrawObject = this.getCurrentWorkSingleStrand();
		break;
	  case ComplexDefines.InRNABasePair :
		if ((this.getCurrentWorkNuc() == null) || !this.getCurrentWorkNuc().isBasePair())
		{
			if (constrain) alert("Must pick base paired nuc");
			currentVarsSet = false;
			return (false);
		}
		this.setCurrentWorkBasePair(
			new RNABasePair2D(this.getCurrentWorkNuc()));
		currentWorkDrawObject = this.getCurrentWorkBasePair();
		break;
	  case ComplexDefines.InRNAHelix :
		if ((this.getCurrentWorkNuc() == null) || !this.getCurrentWorkNuc().isBasePair())
		{
			if (constrain) alert("Must pick base paired nuc");
			currentVarsSet = false;
			return (false);
		}
		this.setCurrentWorkHelix(
			new RNAHelix2D(this.getCurrentWorkNuc()));
		currentWorkDrawObject = this.getCurrentWorkHelix();
		break;
	  case ComplexDefines.InRNAHelicalRun :
		if (this.getCurrentWorkNuc() == null)
		{
			if (constrain) alert("Must pick Nuc");
			currentVarsSet = false;
			return (false);
		}
		try
		{
			this.setCurrentWorkStackedHelix(
				new RNAStackedHelix2D(this.getCurrentWorkNuc()));
		}
		catch (ComplexException ce)
		{
			this.setCurrentWorkStackedHelix(null);
			/*
			if (ce.getErrorCode() == (ComplexDefines.RNA_HELICAL_RUN_ERROR +
				ComplexDefines.CREATE_LEVEL_PSEUDOKNOT_ERROR))
			{
				if (constrain) alert("Must pick non pseudo knot helical run");
				currentVarsSet = false;
				return (false);
			}
			else if (ce.getErrorCode() == (ComplexDefines.RNA_LEVEL_ERROR +
				ComplexDefines.CREATE_ERROR))
			{
				if (constrain) alert(
					"This stacked helix possibly contains base pairing with\n" +
					"another strand and is currently unsupported for this operation.\n" +
					"Try a different constraint mode.");
				currentVarsSet = false;
				return (false);
			}
			else if (ce.getErrorCode() == ComplexDefines.RNA_HELICAL_RUN_ERROR)
			{
				if (constrain) alert("Must pick valid helical run");
				currentVarsSet = false;
				return (false);
			}
			else
			{
				// throw ce;
				if (constrain) alert(ce.getErrorMsg());
				currentVarsSet = false;
				return (false);
			}
			*/
			if (constrain) alert(ce.getErrorMsg() + "\nTry a different constraint mode");
			currentVarsSet = false;
			return (false);
		}
		catch (Exception e)
		{
			throw e;
		}
		currentWorkDrawObject = this.getCurrentWorkStackedHelix();
		break;
	  case ComplexDefines.InRNASubDomain :
		if ((this.getCurrentWorkNuc() == null) || !this.getCurrentWorkNuc().isBasePair())
		{
			if (constrain) alert("Must pick base paired nuc");
			currentVarsSet = false;
			return (false);
		}
		try
		{
			this.setCurrentWorkSubDomain(
				new RNASubDomain2D(this.getCurrentWorkNuc()));
		}
		catch (ComplexException ce)
		{
			this.setCurrentWorkSubDomain(null);
			if (constrain) alert(ce.getErrorMsg() + "\nTry a different constraint mode");
			currentVarsSet = false;
			return (false);
		}
		currentWorkDrawObject = this.getCurrentWorkSubDomain();
		break;
	  case ComplexDefines.InRNANamedGroup :
		if ((this.getCurrentWorkNuc() == null) || (this.getCurrentWorkNuc().getGroupName() == null))
		{
			if (constrain) alert("Must pick nuc in named group");
			currentVarsSet = false;
			return (false);
		}
		this.setCurrentWorkNamedGroup(
			new RNANamedGroup2D(complexSceneView.getComplexScene(),
				this.getCurrentWorkNuc().getGroupName()));
		currentWorkDrawObject = this.getCurrentWorkNamedGroup();
		break;
	  case ComplexDefines.InRNAColorUnit :
		if (this.getCurrentWorkNuc() == null)
		{
			if (constrain) alert("Must pick nuc in common color");
			currentVarsSet = false;
			return (false);
		}
		this.setCurrentWorkColorUnit(
			new RNAColorUnit2D(complexSceneView.getComplexScene(),
				this.getCurrentWorkNuc().getColor()));
		currentWorkDrawObject = this.getCurrentWorkColorUnit();
		break;
	  case ComplexDefines.InRNACycle :
		if (this.getCurrentWorkNuc() == null)
		{
			if (constrain) alert("Must pick Nuc");
			currentVarsSet = false;
			return (false);
		}
		try
		{
			this.setCurrentWorkCycle(
				new RNACycle2D(this.getCurrentWorkNuc()));
		}
		catch (ComplexException ce)
		{
			if (ce.getErrorCode() == ComplexDefines.RNA_LEVEL_ERROR + ComplexDefines.CREATE_ERROR)
			{
				if (constrain) alert(ce.getErrorMsg());
				currentVarsSet = false;
				return (false);
			}
			else
			{
				throw ce;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		currentWorkDrawObject = this.getCurrentWorkCycle();
		break;
	  case ComplexDefines.InRNAListNucs :
	  	if (this.getCurrentWorkNuc() == null)
		{
			if (constrain) alert("Must pick Nuc");
			currentVarsSet = false;
			return (false);
		}
		try
		{
			if ((this.getCurrentWorkListNucs() != null) &&
				this.getCurrentWorkListNucs().firstNucPicked())
			{
				this.getCurrentWorkListNucs().set(
					this.getCurrentWorkListNucs().getFirstNucPicked(),
					this.getCurrentWorkNuc());
			}
			else
			{
				this.setCurrentWorkListNucs(
					new RNAListNucs2D(this.getCurrentWorkNuc()));
			}
		}
		catch (ComplexException ce)
		{
			this.setCurrentWorkListNucs(null);
			if (constrain) alert(ce.getErrorMsg() + "\nTry a different constraint mode");
			currentVarsSet = false;
			return (false);
		}
		currentWorkDrawObject = this.getCurrentWorkListNucs();
		break;
	  case ComplexDefines.InRNASSData :
	  	if (this.getCurrentWorkNuc() == null)
		{
			if (constrain) alert("Must pick Nuc");
			currentVarsSet = false;
			return (false);
		}
		this.setCurrentWorkSStr(this.getCurrentWorkNuc().getParentSSData2D());
		currentWorkDrawObject = this.getCurrentWorkSStr();
		break;
	  case ComplexDefines.InComplex :
	  	if (this.getCurrentWorkNuc() == null)
		{
			if (constrain) alert("Must pick Nuc");
			currentVarsSet = false;
			return (false);
		}
		// NEED to work out for other stuff like proteins
		this.setCurrentWorkRNASSComplex((ComplexSSDataCollection2D)
			this.getCurrentWorkSStr().getParentCollection());
		currentWorkDrawObject = this.getCurrentWorkRNASSComplex();
		break;
	  case ComplexDefines.InLabelsOnly :
		break;
		/*
	  case ComplexDefines.InComplexArea :
		break;
		*/
	  case ComplexDefines.InComplexScene :
		break;
	  default :
		break;
	}	
	currentVarsSet = true;

	if ((this.getCurrentWorkListNucs() != null) &&
		!(currentWorkDrawObject instanceof RNAListNucs))
	{
		this.setCurrentWorkListNucs(null);
	}

	return (true);
}

/*
public Box
showRNASingleNucProperties()
throws Exception
{
	return (null);
}
public Box
showRNASingleStrandProperties()
throws Exception
{
	return (null);
}
public Box
showRNABasePairProperties()
throws Exception
{
	return (null);
}
public Box
showRNAHelixProperties()
throws Exception
{
	return (null);
}
public Box
showRNAStackedHelixProperties()
throws Exception
{
	return (null);
}
public Box
showRNASubDomainProperties()
throws Exception
{
	return (null);
}
public Box
showRNAColorUnitProperties()
throws Exception
{
	return (null);
}
public Box
showRNACycleProperties()
throws Exception
{
	return (null);
}
public Box
showRNAListNucsProperties()
throws Exception
{
	return (null);
}
public Box
showRNASSDataProperties()
throws Exception
{
	return (null);
}
public Box
showComplexProperties()
throws Exception
{
	return (null);
}
public Box
showLabelsOnlyProperties()
throws Exception
{
	return (null);
}
public Box
showComplexAreaProperties()
throws Exception
{
	return (null);
}
public Box
showComplexSceneProperties()
throws Exception
{
	return (null);
}
*/

public int propertiesFrameStartX = 210;
public int propertiesFrameStartY = 60;
// public int propertiesFrameWidth = 260;
public int propertiesFrameWidth = 400;
public int propertiesFrameHeight = 500;
public int propertiesFrameLabelHeight = 20;
public int propertiesFrameButtonHeight = 30;

static URL plainBtURL = null;

public JButton
getNewViewImgPlainButton()
{
	if (plainBtURL == null)
		plainBtURL = this.getClass().getResource("/images/plain_bt.jpg");
	return (this.getNewViewImgButton(new ImageIcon(plainBtURL)));
}

static URL upBtURL = null;

public JButton
getNewViewImgUpButton()
{
	if (upBtURL == null)
		upBtURL = this.getClass().getResource("/images/go_up_btn_s.gif");
	return (this.getNewViewImgButton(new ImageIcon(upBtURL)));
}

static URL downBtURL = null;

public JButton
getNewViewImgDownButton()
{
	if (downBtURL == null)
		downBtURL = this.getClass().getResource("/images/go_down_btn_s.gif");
	return (this.getNewViewImgButton(new ImageIcon(downBtURL)));
}

static URL rightBtURL = null;

public JButton
getNewViewImgRightButton()
{
	if (rightBtURL == null)
		rightBtURL = this.getClass().getResource("/images/go_right_btn_s.gif");
	return (this.getNewViewImgButton(new ImageIcon(rightBtURL)));
}

static URL leftBtURL = null;

public JButton
getNewViewImgLeftButton()
{
	if (leftBtURL == null)
		leftBtURL = this.getClass().getResource("/images/go_left_btn_s.gif");
	return (this.getNewViewImgButton(new ImageIcon(leftBtURL)));
}

public JButton
getNewViewImgButton(ImageIcon imgIcon)
{
	JButton tmpBt = new JButton(imgIcon);
	tmpBt.setMargin(new Insets(0,0,0,0));
	tmpBt.setBackground(guiBGColor);
	tmpBt.setForeground(Color.black);
	return(tmpBt);
}

public JButton
getNewViewButton(String btText)
{
	JButton tmpBt = new JButton(btText);
	tmpBt.setFont(btFont);
	tmpBt.setBackground(guiBGColor);
	tmpBt.setForeground(Color.black);	
	tmpBt.setAlignmentX(Component.CENTER_ALIGNMENT);
	return (tmpBt);
}

public JButton
getNewViewLeftButton(String btText)
{
	JButton tmpBt = new JButton(btText);
	tmpBt.setFont(btFont);
	tmpBt.setBackground(guiBGColor);
	tmpBt.setForeground(Color.black);	
	tmpBt.setAlignmentX(Component.LEFT_ALIGNMENT);
	return (tmpBt);
}

public JRadioButton
getNewViewRadioButton(String btText)
{
	JRadioButton tmpBt = new JRadioButton(btText);
	tmpBt.setFont(btFont);
	tmpBt.setBackground(guiBGColor);
	tmpBt.setForeground(Color.black);	
	tmpBt.setAlignmentX(Component.CENTER_ALIGNMENT);
	return (tmpBt);
}

public JToggleButton
getNewViewToggleButton(String btText)
{
	JToggleButton tmpBt = new JToggleButton(btText);
	tmpBt.setFont(btFont);
	tmpBt.setBackground(guiBGColor);
	tmpBt.setForeground(Color.black);	
	tmpBt.setAlignmentX(Component.CENTER_ALIGNMENT);
	return (tmpBt);
}

public JPanel
getNewViewButtonPanel(JButton bt)
{
	JPanel tmpPanel = this.getNewFlowCenterPanel();
	tmpPanel.setPreferredSize(new Dimension(propertiesFrameWidth, propertiesFrameButtonHeight));
	tmpPanel.add(bt);

	return (tmpPanel);
}

public JPanel
getNewViewButtonLeftPanel(JButton bt)
{
	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.setPreferredSize(new Dimension(propertiesFrameWidth, propertiesFrameButtonHeight));
	tmpPanel.add(bt);

	return (tmpPanel);
}

public JPanel
getNewViewButtonPanel(JRadioButton bt)
{
	JPanel tmpPanel = this.getNewFlowCenterPanel();
	tmpPanel.add(bt);

	return (tmpPanel);
}

public JPanel
getNewViewButtonPanel(JToggleButton bt)
{
	JPanel tmpPanel = this.getNewFlowCenterPanel();
	bt.setBackground(guiBGColor);
	bt.setForeground(Color.black);	
	bt.setMargin(new Insets(-3,0,0,0));
	tmpPanel.add(bt);

	return (tmpPanel);
}

public JPanel
getNewViewButtonPanel(JRadioButton bt, String btText)
{
	JPanel tmpPanel = this.getNewFlowCenterPanel();
	bt.setBackground(guiBGColor);
	bt.setForeground(Color.black);	
	bt.setMargin(new Insets(-3,0,0,0));
	bt.setText(btText);
	tmpPanel.add(bt);

	return (tmpPanel);
}

public JPanel
getNewViewButtonLeftPanel(JRadioButton bt, String btText)
{
	JPanel tmpPanel = this.getNewFlowLeftPanel();
	bt.setBackground(guiBGColor);
	bt.setForeground(Color.black);	
	bt.setMargin(new Insets(-3,0,0,0));
	bt.setText(btText);
	tmpPanel.add(bt);

	return (tmpPanel);
}

public JPanel
getNewViewButtonLeftPanel(JComboBox cb, String text)
{
	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel(text, btFont));
	tmpPanel.add(cb);

	return (tmpPanel);
}

public JPanel
getNewViewButtonPanel(String btText)
{
	return (this.getNewViewButtonPanel(this.getNewViewButton(btText)));
}

public JPanel
getNewFlowLeftPanel()
{
	JPanel tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	return (tmpPanel);
}

public JPanel
getNewFlowCenterPanel()
{
	JPanel tmpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER), true);
	tmpPanel.setPreferredSize(new Dimension(propertiesFrameWidth, propertiesFrameLabelHeight));
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	return (tmpPanel);
}

public JLabel
getNewViewLabel(String text, Font font)
{
	JLabel label = new JLabel(text, JLabel.LEFT);
	label.setFont(font);
	label.setForeground(Color.black);
	return (label);
}

public JLabel
getNewViewLabel(String text)
{
	return (this.getNewViewLabel(text, labelFont));
}

public JPanel
getNewLabelPanel(String labelText)
{
	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(this.getNewViewLabel(labelText));
	tmpPanel.setPreferredSize(new Dimension(propertiesFrameWidth, propertiesFrameLabelHeight));

	return (tmpPanel);
}

public JPanel
getNewLabelPanel(JLabel label)
{
	JPanel tmpPanel = this.getNewFlowLeftPanel();
	tmpPanel.add(label);
	tmpPanel.setPreferredSize(new Dimension(propertiesFrameWidth, propertiesFrameLabelHeight));

	return (tmpPanel);
}

public BevelBorder beveledBorder = new BevelBorder(1);

public Box
basicPropertiesPanel(String title, int height)
{
	Box panel = new Box(BoxLayout.Y_AXIS);

	panel.setBackground(guiBGColor);
	panel.setForeground(Color.black);
	panel.setPreferredSize(new Dimension(propertiesFrameWidth, height));
	panel.setBorder(new TitledBorder(beveledBorder, title));

	return (panel);
}

private Box
basicPropertiesPanel()
{
	Box panel = new Box(BoxLayout.Y_AXIS);

	panel.setBackground(guiBGColor);
	panel.setForeground(Color.black);

	return (panel);
}

// NEED to provide a Vector of Strings
private Box
basicPropertiesPanel(String subString0, String subString1)
{
	Box panel = new Box(BoxLayout.Y_AXIS);
	panel.setBackground(guiBGColor);
	panel.setForeground(Color.black);

	JPanel tmpPanel = this.getNewFlowCenterPanel();
	tmpPanel.setPreferredSize(new Dimension(propertiesFrameWidth, propertiesFrameLabelHeight));
	JLabel label = new JLabel(this.complexTabType() + " " + subString0,
		JLabel.CENTER);
	label.setFont(headerFont);
	label.setPreferredSize(new Dimension(300, 20));
	label.setForeground(Color.black);
	tmpPanel.add(label);
	panel.add(tmpPanel);

	tmpPanel = this.getNewFlowCenterPanel();
	tmpPanel.setPreferredSize(new Dimension(propertiesFrameWidth, propertiesFrameLabelHeight));
	label = new JLabel(subString1, JLabel.CENTER);
	label.setFont(headerFont);
	label.setPreferredSize(new Dimension(300, 20));
	label.setForeground(Color.black);
	tmpPanel.add(label);
	panel.add(tmpPanel);

	panel.setBackground(guiBGColor);
	panel.setForeground(Color.black);

	/* Save if need to refresh the panel
	JButton refreshButton = new JButton("Refresh");
	refreshButton.setFont(btFont);
	refreshButton.setBackground(guiBGColor);
	refreshButton.setForeground(Color.black);
	refreshButton.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				runRightMouseClick();
			}
			catch (Exception e)
			{
				complexSceneView.handleException(e, 1);
			}
		}
	});
	panel.add(refreshButton);
	*/

	return (panel);
}

public Box
showRNASingleNucProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	StringBuffer strBuf = new StringBuffer();
	strBuf.append("Nuc: " +
		this.getCurrentWorkNuc().getID() + " " +
		this.getCurrentWorkNuc().getNucChar());

	if (this.getCurrentWorkNuc().isBasePair())
	{
		strBuf.append(", Base Pair: " +
			this.getCurrentWorkNuc().getBasePair().getID() +
			" " +
			((Nuc2D)this.getCurrentWorkNuc().getBasePair()).getDrawCharObject().getDrawChar());
	}
	mainPanel.add(this.getNewLabelPanel(strBuf.toString()));

	mainPanel.add(this.getNewLabelPanel("In RNA Strand: " +
		((SSData)this.getCurrentWorkNuc().getParentCollection()).getName()));

	return (mainPanel);
}

public Box
showRNASingleStrandProperties()
throws Exception
{
	this.setCurrentWorkSingleStrand(new RNASingleStrand2D(this.getCurrentWorkNuc()));
	RNASingleStrand2D singleStrand = getCurrentWorkSingleStrand();

	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	String name = singleStrand.getName();
	if (name == null)
		name = " ";
	mainPanel.add(this.basicPropertiesPanel());

	mainPanel.add(this.getNewLabelPanel("Picked Nuc: " +
		this.getCurrentWorkNuc().getID() + " " +
		((DrawCharObject)this.getCurrentDrawObject()).getDrawChar()));

	mainPanel.add(this.getNewLabelPanel("End Nucs: " +
		singleStrand.getFivePrimeNuc().getID() + " - " +
		singleStrand.getThreePrimeNuc().getID()
	));

	mainPanel.add(this.getNewLabelPanel("Delineated End Nucs: " +
		singleStrand.getFivePrimeDelineateNuc().getID() + " - " +
		singleStrand.getThreePrimeDelineateNuc().getID()
	));

	mainPanel.add(this.getNewLabelPanel("Nuc Count: " +
		singleStrand.getAllNucCount()
	));

	String lineType = null;
	if (singleStrand.isHairPin())
		lineType = "In Helix HairPin";
	else
		lineType = "In connecting strand";
	if (singleStrand.isWellFormattedArc())
		lineType += " Best Arc";
	else if (singleStrand.isArc())
		lineType += " Arc";
	else
		lineType += " Line";

	if (singleStrand.isArc())
	{
		if (singleStrand.isClockWiseFormatted())
			lineType += " Clock Wise Formatted";
		else
			lineType += " CounterClock Wise Formatted";
	}

	mainPanel.add(this.getNewLabelPanel(lineType));

	return (mainPanel);
}

public Box
showRNABasePairProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	mainPanel.add(this.getNewLabelPanel("5' Basepaired Nuc: " +
		this.getCurrentWorkBasePair().getFivePrimeNuc().getID() + " " +
		this.getCurrentWorkBasePair().getFivePrimeNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("3' Basepaired Nuc: " +
		this.getCurrentWorkBasePair().getThreePrimeNuc().getID() + " " +
		this.getCurrentWorkBasePair().getThreePrimeNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("Base Pair Type: " +
		this.getCurrentWorkBasePair().typeToString()));

	return (mainPanel);
}

public Box
showRNAHelixProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	RNAHelix2D helix = this.getCurrentWorkHelix();

	mainPanel.add(this.getNewLabelPanel("5' Start Nuc: " +
		helix.getFivePrimeStartNuc().getID() + " " +
		helix.getFivePrimeStartNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("3' End Nuc: " +
		helix.getThreePrimeEndNuc().getID() + " " +
		helix.getThreePrimeEndNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("5' End Nuc: " +
		helix.getFivePrimeEndNuc().getID() + " " +
		helix.getFivePrimeEndNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("3' Start Nuc: " +
		helix.getThreePrimeStartNuc().getID() + " " +
		helix.getThreePrimeStartNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("formatted: " +
			(helix.isClockWiseFormatted() ?
			 "clockwise, left-handed" : "anti-clockwise, right-handed")));

	return (mainPanel);
}

public Box
showRNAStackedHelixProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkStackedHelix() == null)
		return (mainPanel);

	mainPanel.add(this.getNewLabelPanel("5' Start Nuc: " +
		this.getCurrentWorkStackedHelix().getFivePrimeStartNuc().getID() + " " +
		this.getCurrentWorkStackedHelix().getFivePrimeStartNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("3' End Nuc: " +
		this.getCurrentWorkStackedHelix().getThreePrimeEndNuc().getID() + " " +
		this.getCurrentWorkStackedHelix().getThreePrimeEndNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("5' End Nuc: " +
		this.getCurrentWorkStackedHelix().getFivePrimeEndNuc().getID() + " " +
		this.getCurrentWorkStackedHelix().getFivePrimeEndNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("3' Start Nuc: " +
		this.getCurrentWorkStackedHelix().getThreePrimeStartNuc().getID() + " " +
		this.getCurrentWorkStackedHelix().getThreePrimeStartNuc().getNucChar()));

	if (this.getCurrentWorkStackedHelix().isHairPin())
		mainPanel.add(this.getNewLabelPanel("terminated with hair pin"));
	else
		mainPanel.add(this.getNewLabelPanel("internal stacked helix"));

	return (mainPanel);
}

public Box
showRNASubDomainProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkSubDomain() == null)
		return (mainPanel);
	
	mainPanel.add(this.getNewLabelPanel("5' Start Nuc: " +
		this.getCurrentWorkSubDomain().getFivePrimeNuc().getID() + " " +
		this.getCurrentWorkSubDomain().getFivePrimeNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("3' End Nuc: " +
		this.getCurrentWorkSubDomain().getThreePrimeNuc().getID() + " " +
		this.getCurrentWorkSubDomain().getThreePrimeNuc().getNucChar()));

	return (mainPanel);
}

public Box
showRNANamedGroupProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkNamedGroup() == null)
		return (mainPanel);
	
	mainPanel.add(this.getNewLabelPanel("Group Name: " +
		this.getCurrentWorkNamedGroup().getGroupName()));

	return (mainPanel);
}

public Box
showRNAColorUnitProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkColorUnit() == null)
		return (mainPanel);
	
	/*
	mainPanel.add(this.getNewLabelPanel("5' Start Nuc: " +
		this.getCurrentWorkColorUnit().getFivePrimeNuc().getID() + " " +
		this.getCurrentWorkColorUnit().getFivePrimeNuc().getNucChar()));

	mainPanel.add(this.getNewLabelPanel("3' End Nuc: " +
		this.getCurrentWorkColorUnit().getThreePrimeNuc().getID() + " " +
		this.getCurrentWorkColorUnit().getThreePrimeNuc().getNucChar()));
	*/

	return (mainPanel);
}

public Box
showRNACycleProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkCycle() == null)
		return (mainPanel);
	
	return (mainPanel);
}

public Box
showRNAListNucsProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkListNucs() == null)
		return (mainPanel);
	
	JLabel label = null;
	if (this.getCurrentWorkListNucs().firstNucPicked())
	{
		mainPanel.add(this.getNewLabelPanel("First Picked Nuc: " +
			this.getCurrentWorkListNucs().getFirstNucPicked().getID() + " " +
			this.getCurrentWorkListNucs().getFirstNucPicked().getNucChar()));
	}

	if (this.getCurrentWorkListNucs().getFivePrimeNuc() != null)
		mainPanel.add(this.getNewLabelPanel("5' Start Nuc: " +
			this.getCurrentWorkListNucs().getFivePrimeNuc().getID() + " " +
			this.getCurrentWorkListNucs().getFivePrimeNuc().getNucChar()));
	else
		mainPanel.add(this.getNewLabelPanel("5' Start Nuc: "));

	if (this.getCurrentWorkListNucs().getThreePrimeNuc() != null)
		mainPanel.add(this.getNewLabelPanel("3' End Nuc: " +
			this.getCurrentWorkListNucs().getThreePrimeNuc().getID() + " " +
			this.getCurrentWorkListNucs().getThreePrimeNuc().getNucChar()));
	else
		mainPanel.add(this.getNewLabelPanel("3' End Nuc: "));

	return (mainPanel);
}

public Box
showRNASSDataProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkSStr() == null)
		return (mainPanel);

	mainPanel.add(this.getNewLabelPanel("SSData Name: " + this.getCurrentWorkSStr().getName()));
	mainPanel.add(this.getNewLabelPanel("SSData Nuc Count: " + this.getCurrentWorkSStr().getNucCount()));

	return (mainPanel);
}

public Box
showComplexProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkRNASSComplex() == null)
		return (mainPanel);

	mainPanel.add(this.getNewLabelPanel("Complex Name: " + this.getCurrentWorkRNASSComplex().getName()));

	return (mainPanel);
}

public Box
showLabelsOnlyProperties()
throws Exception
{
	String labStr0 = null;
	if (this.getCurrentWorkLabel() instanceof DrawLineObject)
		labStr0 = "Line Label associated";
	else if (this.getCurrentWorkLabel() instanceof DrawStringObject)
		labStr0 = "String Label associated";
	else if (this.getCurrentWorkLabel() instanceof DrawCircleObject)
		labStr0 = "Circle Label associated";
	else if (this.getCurrentWorkLabel() instanceof DrawTriangleObject)
		labStr0 = "Triangle Label associated";
	else if (this.getCurrentWorkLabel() instanceof DrawParallelogramObject)
		labStr0 = "Parallelogram Label associated";
	else
		labStr0 = "Label associated";
	String labStr1 = null;

	if (this.getCurrentWorkParent() instanceof Nuc2D)
		labStr1 = "with Nuc: " + ((Nuc2D)this.getCurrentWorkParent()).toShortStringWithParentsName();
	else if (this.getCurrentWorkParent() instanceof SSData2D)
		labStr1 = "with SSData: " + ((SSData2D)this.getCurrentWorkParent()).getName();
	else
		labStr1 = "with Collection: " + ((ComplexScene)this.getCurrentWorkParent()).getName();

	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel(labStr0, labStr1));
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkLabel() instanceof DrawStringObject)
	{
		Font font = ((DrawStringObject)this.getCurrentWorkLabel()).getFont();
		mainPanel.add(this.getNewLabelPanel("Font Size: " + font.getSize()));

		mainPanel.add(this.getNewLabelPanel("Font Name: " + font.getName()));
	}

	return (mainPanel);
}

/*
public Box
showComplexAreaProperties()
throws Exception
{
	this.setCurrentWorkComplexScene(complexSceneView.getComplexScene());

	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	return (mainPanel);
}
*/

public Box
showComplexSceneProperties()
throws Exception
{
	Box mainPanel = new Box(BoxLayout.Y_AXIS);
	mainPanel.add(this.basicPropertiesPanel());
	mainPanel.setBackground(guiBGColor);
	mainPanel.setForeground(Color.black);

	if (this.getCurrentWorkComplexScene() == null)
		return (mainPanel);

	mainPanel.add(this.getNewLabelPanel("Name: " + this.getCurrentWorkComplexScene().getName()));

	return (mainPanel);
}

public void
runRightMouseClick()
throws Exception
{
	// run alerts in reset
	if (!this.resetCurrentVars(true))
		return;
	
	String title = null;

	DrawObject labelObject = this.getCurrentWorkLabel();
	Nuc2D nuc = this.getCurrentWorkNuc();
	if (nuc != null)
	{
		switch (this.getCurrentComplexPickMode())
		{
		  case ComplexDefines.InRNASingleNuc :
			title = this.complexTabType() + " RNA Nuc";
			complexPropertiesPanel = showRNASingleNucProperties();
			break;
		  case ComplexDefines.InRNASingleStrand :
			title = this.complexTabType() + " RNA Single Strand";
			complexPropertiesPanel = showRNASingleStrandProperties();
			break;
		  case ComplexDefines.InRNABasePair :
			title = this.complexTabType() + " RNA Base Pair";
			complexPropertiesPanel = showRNABasePairProperties();
			break;
		  case ComplexDefines.InRNAHelix :
			// NEED also to determine if BP in Helix
			title = this.complexTabType() + " RNA Helix";
			complexPropertiesPanel = showRNAHelixProperties();
			break;
		  case ComplexDefines.InRNAHelicalRun :
			title = this.complexTabType() + " RNA Helical Run";
			complexPropertiesPanel = showRNAStackedHelixProperties();
			break;
		  case ComplexDefines.InRNASubDomain :
			title = this.complexTabType() + " RNA Sub Domain";
			complexPropertiesPanel = showRNASubDomainProperties();
			break;
		  case ComplexDefines.InRNANamedGroup :
			title = this.complexTabType() + " RNA Named Group";
			complexPropertiesPanel = showRNANamedGroupProperties();
			break;
		  case ComplexDefines.InRNAColorUnit :
			title = this.complexTabType() + " RNA Color Unit";
			complexPropertiesPanel = showRNAColorUnitProperties();
			break;
		  case ComplexDefines.InRNACycle :
			title = this.complexTabType() + " RNA Cycle";
			complexPropertiesPanel = showRNACycleProperties();
			break;
		  case ComplexDefines.InRNAListNucs :
			title = this.complexTabType() + " RNA List Nucs";
			complexPropertiesPanel = showRNAListNucsProperties();
			break;
		  case ComplexDefines.InRNASSData :
			title = this.complexTabType() + " RNA SSData";
			complexPropertiesPanel = showRNASSDataProperties();
			break;
		  case ComplexDefines.InComplex :
			title = this.complexTabType() + " Complex";
			complexPropertiesPanel = showComplexProperties();
			break;
		  //
		  // case ComplexDefines.InLabelsOnly :
			// complexPropertiesPanel = showLabelsOnlyProperties();
			// break;
		  //
			/*
		  case ComplexDefines.InComplexArea :
			title = this.complexTabType() + " Complex Area";
			break;
			*/
		  case ComplexDefines.InComplexScene :
			title = this.complexTabType() + " Complex Scene";
			complexPropertiesPanel = showComplexSceneProperties();
			break;
		  default :
			break;
		}
	}
	else if (labelObject != null) // rht clicked on extraneous label
	{
		String labStr0 = " Label";
		if (labelObject instanceof DrawLineObject)
			labStr0 = " Line Label";
		else if (labelObject instanceof DrawStringObject)
			labStr0 = " String Label";
		else if (labelObject instanceof DrawCircleObject)
			labStr0 = " Circle Label";
		else if (labelObject instanceof DrawTriangleObject)
			labStr0 = " Triangle Label";
		else if (labelObject instanceof DrawParallelogramObject)
			labStr0 = " Parallelogram Label";
		else if (labelObject instanceof DrawArrowObject)
			labStr0 = " Arrow Label";

		title = this.complexTabType() + labStr0;
		complexPropertiesPanel = showLabelsOnlyProperties();
	}
	else // rht clicked on scene
	{
		title = this.complexTabType() + " Complex Scene";
		if (this.getCurrentComplexPickMode() == ComplexDefines.InComplexScene)
		{
			complexPropertiesPanel = showComplexSceneProperties();
		}
		else
		{
			alert("Need to right mouse click on label, nuc, or graphical primitive");
			return;
		}
	}

	if (complexPropertiesPanel == null)
	{
		if (this instanceof ComplexSceneAnnotateTab)
		{
			alert("No Annotate properties menu");
		}
		else if (this instanceof ComplexSceneEditTab)
		{
			alert("No Edit properties menu");
		}
		else if (this instanceof ComplexSceneFormatSStrTab)
		{
			alert("No Format properties menu");
		}
		else if (this instanceof ComplexSceneIOTab)
		{
			alert("No IO properties menu");
		}
		return;
	}

	/*
	// debug("title: " + title);
	if (title == null)
	{
		this.updateComplexPropertiesFrame("TEAST");
	}
	else
	*/
		this.updateComplexPropertiesFrame(title);
}

public void
runMouseDragged(int x, int y)
throws Exception
{
	complexSceneView.setCurrentViewX(x);
	complexSceneView.setCurrentViewY(y);

	/* SVE for debug purposes
	if (complexSceneView.getShowMousePos())
		complexSceneView.printCurrentMousePosInfo();
	*/

	if (this.getCurrentDrawObject() == null)
		return;
}

public void
runMouseMoved()
throws Exception
{
	if (complexSceneView == null)
		return;
	ComplexScene2D complexScene = complexSceneView.getComplexScene();
	if (complexScene == null)
		return;
	
	Nuc2D nuc = complexScene.findNuc(
		complexSceneView.getCurrentViewX(),
		complexSceneView.getCurrentViewY());

	if (nuc == null)
		complexSceneView.printConsole(" ");
	else
		complexSceneView.printConsole(nuc.getNucChar() + " " + nuc.getID() +
			" in rna strand: " + nuc.getParentSSData2D().getName());
}

public void
runMouseReleased()
throws Exception
{
	if (this.getCurrentDrawObject() == null)
		return;
}

// G2 for painting background that has minus drawObject
public Graphics2D editClearPaintG2 = null;

/* SAVE FOR DRAWING IMG WITH XOR
public Graphics2D moveEditXORG2 = null;
*/
public Graphics2D sceneImgG2 = null;
public AffineTransform moveEditAffTran = null;
public AffineTransformOp moveEditAffTranOp = null;
/* SAVE if want to go back to drawing with img rather than graphical primitive
public int mouseDeltaX = 0;
public int mouseDeltaY = 0;
*/
public int lastWinX = 0;
public int lastWinY = 0;
public int lastMouseX = 0;
public int lastMouseY = 0;
public int lineRayPos = 0; // for now -1 is tail, 0 is mid, 1 is head
public double figureScale = 1.0;

public boolean
initTabScene()
throws Exception
{
	lastMouseX = complexSceneView.getMousePressedImgSpaceX();
	lastMouseY = complexSceneView.getMousePressedImgSpaceY();	

	figureScale = this.complexSceneView.getFigureScale();
	
	lastWinX = complexSceneView.getWindowViewX();
	lastWinY = complexSceneView.getWindowViewY();

	sceneImgG2 = complexSceneView.getSceneImg().createGraphics();
	sceneImgG2.setRenderingHints(complexSceneView.getDrawObjectRenderHints());

	// Save if want to go back to drawing with img rather than graphical primitive
	// AffineTransform affTr = ((DrawObjectCollection)
	//	 drawObject.getParentCollection()).getG2Transform();
	// sceneImgG2.setTransform(affTr);
	// AffineTransform invAffTr = affTr.createInverse();
	// 
	// Point2D testPt = new Point2D.Double(
	// 	(double)complexSceneView.getMousePressedImgSpaceX(),
	// 	(double)complexSceneView.getMousePressedImgSpaceY());
	// Point2D newTestPt = new Point2D.Double();
	// invAffTr.transform(testPt, newTestPt);
	// 
	// double diffMouseDrwObjX = newTestPt.getX() - drawObject.getX();
	// double diffMouseDrwObjY = newTestPt.getY() + drawObject.getY();
	// 
	// mouseDeltaX = -(int)Math.round(
	// 		figureScale * (drawObject.getDeltaX() + diffMouseDrwObjX));
	// mouseDeltaY = -(int)Math.round(
	// 		figureScale * (drawObject.getDeltaY() + diffMouseDrwObjY));
	//

	sceneImgG2.setBackground(complexSceneView.getDrawImgBGColor());
	// sceneImgG2.setBackground(Color.green);

	return (true);
}

public void
initTabGraphics()
{
	// copy current getScreenImg() with deleted drawObject to offScreenImg
	// now both are same without drawObject but with offScreenImg to fit viewport.
	this.complexSceneView.setOffScreenImg();

	
	// G2 for painting background that has minus drawObject
	editClearPaintG2 = complexSceneView.getSceneImg().createGraphics();
	editClearPaintG2.setPaintMode();

	// SAVE FOR DRAWING IMG WITH XOR
	// moveEditXORG2 = complexSceneView.getSceneImg().createGraphics();
	// moveEditXORG2.setXORMode(complexSceneView.getDrawImgBGColor());
	//

	if ((moveEditAffTran == null) || (moveEditAffTranOp == null))
	{
		moveEditAffTran = new AffineTransform();
		moveEditAffTranOp = new AffineTransformOp(moveEditAffTran,
			GraphicsUtil.imageRenderHints);
	}
}

static BRectangle2D constrainedArea = new BRectangle2D();
static Point2D constrainedTransPt = new Point2D.Double();
static Point2D constrainedNewPt = new Point2D.Double();
/*
public void
setDrawConstrainedArea(DrawObject drwObj, Graphics2D g2)
throws Exception
{
	g2.setTransform(drwObj.getParentG2Transform());
	if (!(
		(drwObj instanceof Nuc2D) ||
		(drwObj instanceof SSData2D) ||
		(drwObj instanceof ComplexSSDataCollection2D) ||
		(drwObj == this.getCurrentWorkLabel())))
		g2.translate(-drwObj.getX(), drwObj.getY());
	drwObj.update(g2);

	drwObj.clearBoundingBoxArea(g2);

	double scaleVal = complexSceneView.getFigureScale();

	// NEED to make up a new hide criteria just for stuff like this so
	// it doesn't interfere with previously hidden stuff
	drwObj.setHideForConstrain(true);
	constrainedArea.setRect(drwObj.getBoundingBox());
	constrainedTransPt.setLocation(
		drwObj.getBoundingBox().getX(), drwObj.getBoundingBox().getY());
	g2.getTransform().transform(constrainedTransPt, constrainedNewPt);
	constrainedArea.setRect(constrainedNewPt.getX(), constrainedNewPt.getY(),
		scaleVal * constrainedArea.getWidth(),
		scaleVal * constrainedArea.getHeight());
	complexSceneView.drawConstrainedArea(g2, constrainedArea);
	drwObj.setHideForConstrain(false);
}
*/

public void
editErase(DrawObject drwObj, int nucMode)
throws Exception
{
	sceneImgG2.setTransform(drwObj.getParentG2Transform());

	if (!(
		(drwObj instanceof Nuc2D) ||
		(drwObj instanceof SSData2D) ||
		(drwObj instanceof ComplexSSDataCollection2D) ||
		(drwObj == this.getCurrentWorkLabel())))
		sceneImgG2.translate(-drwObj.getX(), drwObj.getY());
	drwObj.update(sceneImgG2);
	drwObj.clearBoundingBoxArea(sceneImgG2);

	double scaleVal = complexSceneView.getFigureScale();
	drwObj.setHideForConstrain(true);
	constrainedArea.setRect(drwObj.getBoundingBox());
	constrainedTransPt.setLocation(
		drwObj.getBoundingBox().getX(), drwObj.getBoundingBox().getY());
	sceneImgG2.getTransform().transform(constrainedTransPt, constrainedNewPt);
	constrainedArea.setRect(constrainedNewPt.getX(), constrainedNewPt.getY(),
		scaleVal * constrainedArea.getWidth(),
		scaleVal * constrainedArea.getHeight());
	complexSceneView.drawConstrainedArea(sceneImgG2, constrainedArea);
	drwObj.setHideForConstrain(false);

	switch (nucMode)
	{
	  case ComplexDefines.InRNASingleNuc :
	  case ComplexDefines.InLabelsOnly :
	  case ComplexDefines.InRNASSData :
	  case ComplexDefines.InComplex :
	  case ComplexDefines.InRNAColorUnit :
	  case ComplexDefines.InRNANamedGroup :
		sceneImgG2.setTransform(drwObj.getParentG2Transform());
		break;
	  case ComplexDefines.InRNAListNucs :
	  case ComplexDefines.InRNASingleStrand :
		sceneImgG2.setTransform(drwObj.getParentG2Transform());
		sceneImgG2.translate(-drwObj.getX(), drwObj.getY());
		break;

	  default :
		break;
	}

	complexSceneView.paintWindowView();

}

public void
updateEditColor(DrawObject drwObj, Color color, Graphics2D g2, boolean redraw)
throws Exception
{
	if ((drwObj == null) || (editClearPaintG2 == null))
	{
		// alert("drwObj NULL in ComplexSceneEditTab.updateEditColor()");
		return;
	}
	if (drwObj.getIsHidden())
		return;

	drwObj.setEditColor(color);
	if (redraw)
		this.animateEdit(drwObj);
}

public void
updateEditColor(DrawObject drwObj, Color color, Graphics2D g2)
throws Exception
{
	if ((drwObj == null) || (editClearPaintG2 == null))
	{
		// alert("drwObj NULL in ComplexSceneEditTab.updateEditColor()");
		return;
	}
	if (drwObj.getIsHidden())
		return;

	drwObj.setEditColor(color);
	this.animateEdit(drwObj);
}

public void
updateEditColor(DrawObject drwObj, Color color, Graphics2D g2, int x, int y)
throws Exception
{
	// for now movEditAffTranOp and complexSceneView.getOffScreenImg() not
	// considered null if editClearPaintG2 is not null
	if ((drwObj == null) || (editClearPaintG2 == null))
	{
		// alert("drwObj NULL in ComplexSceneEditTab.updateEditColor()");
		return;
	}
	if (drwObj.getIsHidden())
		return;

	drwObj.setEditColor(color);
	this.animateEdit(drwObj, x, y);
}

public void
updateEditColor(int complexPickMode, Color color, Graphics2D g2)
throws Exception
{
	// for now movEditAffTranOp and complexSceneView.getOffScreenImg() not
	// considered null if editClearPaintG2 is not null
	DrawObject drwObj = this.getCurrentDrawObject();
	if ((drwObj == null) || (editClearPaintG2 == null))
	{
		// alert("drwObj NULL in ComplexSceneEditTab.updateEditColor()");
		return;
	}
	if (drwObj.getIsHidden())
		return;

	/*
	drwObj.setEditColor(color);
	this.animateEdit(drwObj);
	*/

	if (this.getCurrentWorkLabel() != null)
	{
		this.updateEditColor(this.getCurrentWorkLabel(),
			this.getCurrentWorkLabel().getColor(), g2, false);
	}
	else
	switch (complexPickMode)
	{
	  case ComplexDefines.InRNASingleNuc :
		this.updateEditColor(this.getCurrentWorkNuc(), color, g2, false);
	  	break;
	  case ComplexDefines.InRNASingleStrand :
		this.updateEditColor(this.getCurrentWorkSingleStrand(), color, g2, false);
		break;
	  case ComplexDefines.InRNABasePair :
		this.updateEditColor(this.getCurrentWorkBasePair(), color, g2, false);
		break;
	  case ComplexDefines.InRNAHelix :
		this.updateEditColor(this.getCurrentWorkHelix(), color, g2, false);
		break;
	  case ComplexDefines.InRNAHelicalRun :
		this.updateEditColor(this.getCurrentWorkStackedHelix(), color, g2, false);
		break;
	  case ComplexDefines.InRNASubDomain :
		this.updateEditColor(this.getCurrentWorkSubDomain(), color, g2, false);
		break;
	  case ComplexDefines.InRNANamedGroup :
		this.updateEditColor(this.getCurrentWorkNamedGroup(), color, g2, false);
		break;
	  case ComplexDefines.InRNAColorUnit :
		this.updateEditColor(this.getCurrentWorkColorUnit(), color, g2, false);
		break;
	  case ComplexDefines.InRNACycle :
		this.updateEditColor(this.getCurrentWorkCycle(), color, g2, false);
		break;
	  case ComplexDefines.InRNAListNucs :
		if (this.getCurrentWorkListNucs().isSet())
			this.updateEditColor(this.getCurrentWorkListNucs(), color, g2, false);
		break;
	  case ComplexDefines.InRNASSData :
		this.updateEditColor(this.getCurrentWorkSStr(), color, g2, false);
		break;
	  case ComplexDefines.InComplex :
		this.updateEditColor(this.getCurrentWorkRNASSComplex(), color, g2, false);
		break;
	  case ComplexDefines.InLabelsOnly :
		// dealing with up above before start of switch statement
		break;
		/*
	  case ComplexDefines.InComplexArea :
		this.updateEditColor(this.getCurrentWorkComplexArea(), color, g2, false);
		break;
		*/
	  case ComplexDefines.InComplexScene :
		// NOT SURE yet what this implies
		// this.updateEditColor(this.getCurrentWorkComplexScene(), color, g2, false);
		//
		break;
	  default :
	  	break;
	}
}

public void
drawOffScreenImg()
throws Exception
{
	this.drawOffScreenImg(complexSceneView.getWindowViewX(), complexSceneView.getWindowViewY());
}

public void
drawOffScreenImg(int x, int y)
throws Exception
{
	editClearPaintG2.drawImage(complexSceneView.getOffScreenImg(), moveEditAffTranOp, x, y);
}

public void
animateEdit(DrawObject drwObj)
throws Exception
{
	this.drawOffScreenImg();
	this.updateSceneImgG2Clip();
	editRedraw(drwObj);
}

public void
animateEdit(DrawObject drwObj, int x, int y)
throws Exception
{
	drawOffScreenImg(x, y);
	this.updateSceneImgG2Clip();
	editRedraw(drwObj);
}

public void
editRedraw(DrawObject drwObj)
throws Exception
{
	drwObj.update(sceneImgG2);
	drwObj.draw(sceneImgG2, null);
	complexSceneView.paintWindowView();
}

public void
editRedraw(DrawObject drwObj, Graphics2D g2)
throws Exception
{
	drwObj.update(g2);
	drwObj.draw(g2, null);
	complexSceneView.paintWindowView();
}

public void
resetSceneImgG2(boolean setClip)
{
	sceneImgG2 = complexSceneView.getSceneImg().createGraphics();
	sceneImgG2.setRenderingHints(complexSceneView.getDrawObjectRenderHints());
	if (setClip)
		sceneImgG2.setClip(
			complexSceneView.getWindowViewX(),
			complexSceneView.getWindowViewY(),
			complexSceneView.getWindowViewW(),
			complexSceneView.getWindowViewH());
}

public void
updateSceneImgG2Clip()
{
	AffineTransform tmpAT = sceneImgG2.getTransform();
	int intScaleVal = (int)Math.round(tmpAT.getScaleX());
	sceneImgG2.setClip(
		(complexSceneView.getWindowViewX() -
			(int)Math.round(complexSceneView.viewSpaceX(
				complexSceneView.imgSpaceX(tmpAT.getTranslateX()))))/intScaleVal + 1,
		(complexSceneView.getWindowViewY() -
			(int)Math.round(complexSceneView.viewSpaceY(
				complexSceneView.imgSpaceY(tmpAT.getTranslateY()))))/intScaleVal + 1,
		complexSceneView.getWindowViewW()/intScaleVal - 2,
		complexSceneView.getWindowViewH()/intScaleVal - 2);
}

/*************  END MOUSE STUFF *********************/

/************** PropertyChangeListener Implementaion ***************/

public void
propertyChange(PropertyChangeEvent evt)
{
	String propertyName = evt.getPropertyName();

	if (propertyName == null)
		return;

	if (propertyName.equals("CurrentDrawObject"))
	{
		this.setCurrentDrawObject((DrawObject)evt.getNewValue());
	}
	else if (propertyName.equals("CurrentComplexPickMode"))
	{
		this.setCurrentComplexPickMode((int)((Integer)evt.getNewValue()).intValue());
	}
}

/************** TreeExpansionListener Implementaion ***************/

public void
treeExpanded(TreeExpansionEvent evt)
{
// debug("Tree-expanded event detected: " + evt.toString());
}

public void
treeCollapsed(TreeExpansionEvent evt)
{
	// debug("Tree-collapsed event detected: " + evt.toString());
}

/************** TreeSelectionListener Implementaion ***************/

public void
valueChanged(TreeSelectionEvent e)
{
	DefaultMutableTreeNode node =
		(DefaultMutableTreeNode)complexSceneView.pickStrandTree.
			getLastSelectedPathComponent();

	if (node == null)
		return;

	Object nodeInfo = node.getUserObject();
	if (node.isLeaf())
	{
		complexSceneView.printConsole("LEAF NODE: " + nodeInfo.toString() + " at depth: " +
			node.getDepth());
		//
		// BookInfo book = (BookInfo)nodeInfo;
		// displayURL(book.bookURL);
		//
	}
	else
	{
		complexSceneView.printConsole("BRANCH NODE: " + nodeInfo.toString() + " at depth: " +
			node.getDepth());
		//
		// displayURL(helpURL); 
		//
	}

	// NOW identify collection with name and depth and put up
	// properties window for it if in Annotate or Edit mode
	
}

/*********** COLOR STUFF **********************/

private JInternalFrame colorFrame = null;

public JInternalFrame
getColorFrame()
{
	return (this.colorFrame);
}

public void
setColorFrame()
throws Exception
{
	if (this.colorFrame == null)
	{
		this.colorFrame = complexSceneView.getBasicInternalFrame(10, 60, 500, 526);
		this.colorFrame.addInternalFrameListener(new InternalFrameListener()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				colorFrame.restoreSubcomponentFocus();
			}

			public void internalFrameClosed(InternalFrameEvent e)
			{
				try
				{
					InternalFrameListener[] listenerList =
						colorFrame.getInternalFrameListeners();
					if (listenerList.length > 1)
						debug("Problem in ComplexSceneAnnotateTab.this.colorFrame().internalFrameClosed(): More than one listener");
					requestFocus();
				}
				catch (Exception evt)
				{
					complexSceneView.handleException("Exception in CopmlexSceneFormatSStrTab.this.colorFrame.internalFrameClosing:", evt, 101);
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

	complexSceneView.updateBasicInternalFrame("XRNA Color Chooser",
		"Choose color selection type:",
		this.getColorChooser(), this.colorFrame);
	if (this.colorFrame.isIcon())
		this.colorFrame.setIcon(false);
}

/*********** END COLOR STUFF **********************/

/*********** MISC STUFF **********************/

public Point2D firstAreaPickPt = null;
public Point2D secondAreaPickPt = null;

public String
printPickMode(String extraMsg)
throws Exception
{
	ComplexCollection complexNode = complexSceneView.getComplexTreePick();
	int nodeLevel = ComplexScene.getChildLevel(complexNode);

	SSData2D sstr = null;
	ComplexSSDataCollection2D ssComplex = null;
	ComplexScene2D complexScene = null;
	String levelMsg = " ";
	if (nodeLevel == 0)
		levelMsg = " in Complex Scene ";
	else if (nodeLevel == 1)
		levelMsg = " in Complex: " + ((ComplexSSDataCollection2D)complexNode).getName() + " " ;
	else if (nodeLevel == 2)
		levelMsg = " in Secondary Structure: " + ((SSData2D)complexNode).getName() + " " ;

	switch (getCurrentComplexPickMode())
	{
	  case ComplexDefines.InRNASingleNuc :
		return ("Pick Single Nuc or Labels" + levelMsg + extraMsg);
	  case ComplexDefines.InRNASingleStrand :
		return ("Pick Single Strand" + levelMsg + extraMsg);
	  case ComplexDefines.InRNABasePair :
		return ("Pick BasePair" + levelMsg + extraMsg);
	  case ComplexDefines.InRNAHelix :
		return ("Pick Helix" + levelMsg + extraMsg);
	  case ComplexDefines.InRNAHelicalRun :
		return ("Pick Stacked Helix" + levelMsg + extraMsg);
	  case ComplexDefines.InRNASubDomain :
		return ("Pick Sub Domain" + levelMsg + extraMsg);
	  case ComplexDefines.InRNANamedGroup :
		return ("Pick Named Group" + levelMsg + extraMsg);
	  case ComplexDefines.InRNAColorUnit :
		return ("Pick Color" + levelMsg + extraMsg);
	  case ComplexDefines.InRNACycle :
		return ("Pick Cycle" + levelMsg + extraMsg);
	  case ComplexDefines.InRNAListNucs :
		return ("Pick Consecutive Nucs" + levelMsg + extraMsg);
	  case ComplexDefines.InRNASSData :
		return ("Pick SSData" + levelMsg + extraMsg);
	  case ComplexDefines.InComplex :
		return ("Pick Complex" + levelMsg + extraMsg);
	  case ComplexDefines.InLabelsOnly :
		return ("Pick Labels" + levelMsg + extraMsg);
		/*
	  case ComplexDefines.InComplexArea :
		return ("Pick top.lft/btm.rht Area" + levelMsg + extraMsg);
		*/
	  case ComplexDefines.InComplexScene :
		return ("Pick Complex Scene" + levelMsg + extraMsg);
	  default :
		return ("Must Choose Complex Pick Mode");
	}
}

public String
complexTabType()
{
	if (this instanceof ComplexSceneIOTab)
		return("Import/Export");
	else if (this instanceof ComplexSceneAnnotateTab)
		return("Annotate");
	else if (this instanceof ComplexSceneEditTab)
		return("Edit");
	else if (this instanceof ComplexSceneFormatSStrTab)
		return("Format");
	return (null);
}

/*
public static JInternalFrame
getBasicInternalFrame(int x, int y, int w, int h)
{
	JInternalFrame tmpFrame = new JInternalFrame();
	tmpFrame.setClosable(true);
	tmpFrame.setIconifiable(true);
	tmpFrame.setMaximizable(true);
	tmpFrame.setResizable(true);
	tmpFrame.setBounds(x, y, w, h);

	return (tmpFrame);
}

public void
updateBasicInternalFrame(String borderTitle, JComponent component, JInternalFrame jit)
{
	this.updateBasicInternalFrame(this.complexTabType() + " Properties", borderTitle,
		component, jit);
}

public void
updateBasicInternalFrame(String jitTitle, String borderTitle, JComponent component, JInternalFrame jit)
{
	jit.setTitle(jitTitle);
	jit.getContentPane().removeAll();

	JScrollPane basicScrollPane = new JScrollPane(component,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	basicScrollPane.setBackground(guiBGColor);
	basicScrollPane.setBorder(new TitledBorder(beveledBorder, borderTitle));
	basicScrollPane.setVisible(true);

	jit.getContentPane().add(BorderLayout.CENTER, basicScrollPane);
	
	complexSceneView.addInternalFrame(jit);

	component.setVisible(true);
	component.updateUI();

	jit.setVisible(true);
	jit.updateUI();
}
*/

public void
undoEditColorScheme()
throws Exception
{
	if ((this.currentWorkDrawObject != null) && (this.currentWorkDrawObject.getIsEditable()))
	{
		this.updateEditColor(currentWorkDrawObject, null,
			sceneImgG2, lastWinX, lastWinY);
	}
}

public void
updateComplexPropertiesFrame(String title)
throws Exception
{
	if (complexPropertiesFrame == null)
	{
		complexPropertiesFrame = complexSceneView.getBasicInternalFrame(
			propertiesFrameStartX, propertiesFrameStartY,
			propertiesFrameWidth, propertiesFrameHeight);

		complexPropertiesFrame.addInternalFrameListener(new InternalFrameListener()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				complexPropertiesFrame.restoreSubcomponentFocus();
			// debug("internalFram closing");

			}

			public void internalFrameClosed(InternalFrameEvent e)
			{
				try
				{
					InternalFrameListener[] listenerList =
						complexPropertiesFrame.getInternalFrameListeners();
					/*
					for (int i = 0;i < listenerList.length;i++)
						complexPropertiesFrame.removeInternalFrameListener(listenerList[i]);
					*/
					if (listenerList.length > 1)
						debug("Problem in ComplexSceneWorkTab.updateComplexPropertiesFrame().internalFrameClosed(): More than one listener");

					/*
					// need to include ListNucs here as it was messing up since
					// ListNucs takes 2 clicks to pull off
					if ((getCurrentWorkNuc() != null) &&
						(!getCurrentWorkNuc().isHidden()) &&
						(getCurrentWorkListNucs() == null))
					{
						complexSceneView.setMouseToNuc(getCurrentWorkNuc());
						complexSceneView.viewRobot.mousePress(InputEvent.BUTTON1_MASK);
						complexSceneView.viewRobot.mouseRelease(InputEvent.BUTTON1_MASK);
					}
					*/

					// MAYBE NEED some sort of method that will clear all edit colors
					// and re-render in leu of above
					undoEditColorScheme();

					setCurrentWorkListNucs(null);

					requestFocus();
				}
				catch (Exception evt)
				{
					complexSceneView.handleException("Exception in ComplexSceneEdit.internalFrameClosing:", evt, 101);
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

	complexSceneView.updateBasicInternalFrame(title, complexPropertiesPanel, complexPropertiesFrame);
	
	if (complexPropertiesFrame.isIcon())
		complexPropertiesFrame.setIcon(false);
}

/*********** END MISC STUFF **********************/

public void
alert(String msg)
{
	JOptionPane.showMessageDialog(this, msg);
}

public void
showUserMessage(String msg)
{
	JOptionPane.showMessageDialog(this, msg);
}

public boolean
checkNucInSingleStrand(NucNode nuc)
{
	// JOptionPane.showMessageDialog(ComplexSceneView.this, "Made IT");
	this.showUserMessage("Made it to check nuc in single strand");
	return (true);
}

public void
warpMouse(int x, int y)
{
	complexSceneView.viewRobot.mouseMove(x, y);
}

public void
redirectPropertiesMenu(Nuc2D nuc, int nucMode)
throws Exception
{
	complexSceneView.nucPickMode_CB.setSelectedItem(
		ComplexDefines.nucModeDefineToString(nucMode));
	complexSceneView.setWindowToDrawObjectCenter(nuc);
	complexSceneView.viewRobot.mousePress(InputEvent.BUTTON3_MASK);
	complexSceneView.viewRobot.mouseRelease(InputEvent.BUTTON3_MASK);
}

public void
runComplexExceptionAlert(ComplexException ce, boolean showExceptionMsg)
{
	if (showExceptionMsg && (ce.getExceptionMsg() != null) &&
		(ce.getExceptionMsg().length() > 0) &&
		(ce.getErrorMsg() != null) &&
		(ce.getErrorMsg().length() > 0) &&
		(ce.getComment() != null) &&
		(ce.getComment().length() > 0))
		alert(ce.getExceptionMsg() + "\n" + ce.getErrorMsg() + "\n" + ce.getComment());
	else if (showExceptionMsg && (ce.getExceptionMsg() != null) &&
		(ce.getExceptionMsg().length() > 0) &&
		(ce.getErrorMsg() != null) &&
		(ce.getErrorMsg().length() > 0))
		alert(ce.getExceptionMsg() + "\n" + ce.getErrorMsg());
	else if (showExceptionMsg && (ce.getExceptionMsg() != null) &&
		(ce.getExceptionMsg().length() > 0) &&
		(ce.getComment() != null) &&
		(ce.getComment().length() > 0))
		alert(ce.getExceptionMsg() + "\n" + ce.getComment());
	else if (
		(ce.getErrorMsg() != null) &&
		(ce.getErrorMsg().length() > 0) &&
		(ce.getComment() != null) &&
		(ce.getComment().length() > 0))
		alert(ce.getErrorMsg() + "\n" + ce.getComment());
	else if (
		(showExceptionMsg && ce.getExceptionMsg() != null) &&
		(ce.getExceptionMsg().length() > 0))
		alert(ce.getExceptionMsg());
	else if (
		(ce.getErrorMsg() != null) &&
		(ce.getErrorMsg().length() > 0))
		alert(ce.getErrorMsg());
	else if (
		(ce.getComment() != null) &&
		(ce.getComment().length() > 0))
		alert(ce.getComment());
	else
		alert("Complex Exception contains no information");
}

public void
printConsole(String s)
{
	complexSceneView.printConsole(s);
}

public void
printConsoleEdit(String s)
{
	complexSceneView.printConsoleEdit(s);
}

private static void
debug(String s)
{
	System.err.println("ComplexSceneWorkTab-> " + s);
}

private static void
debug(String s0, String s1)
{
	debug(s0 + " " + s1);
}

private static void
debug(String s0, String s1, String s2)
{
	debug(s0 + " " + s1 + " " + s2);
}

private static void
debug(String s0, int s1, int s2)
{
	debug(s0 + " " + s1 + " " + s2);
}

}
