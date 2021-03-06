package ssview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SingleSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import io.STRParser;
import io.SVGParser;
import io.XRNAData;
import jimage.DrawObject;
import jimage.DrawObjectCollection;
import jimage.DrawObjectView;
import jimage.FontChooser;
import jimage.GenFileFilter;
import jimage.HexColorChooserPanel;
import jimage.ViewImgCanvas;
import util.Tuple2;
import util.Tuple3;
import util.Tuple4;
import util.Tuple5;

public class ComplexSceneView extends DrawObjectView implements Printable, AdjustmentListener {

	public BevelBorder beveledBorder = new BevelBorder(1);
	public Font btFont = new Font("Dialog", Font.BOLD, 10);

	public JScrollPane genImgScrollPane = null;
	public ViewImgCanvas complexPickPanel = null;
	public BevelBorder border = null;
	public BevelBorder raisedBorder = null;

	protected JTextField scaleTF = null;
	protected JComboBox scaleCB = null;
	public JPanel scaleUndoEdit_Panel = null;
	public JPanel scaleUndoFormat_Panel = null;
	public JPanel scale_Panel = null;
	public JPanel undoEdit_Panel = null;
	public JPanel undoFormat_Panel = null;
	protected JPanel subScalePanel = null;

	private JScrollPane userConsoleScrollPane = null;
	private JTextField userConsoleOut = null;

	public ComplexSceneIOTab ioTabPanel = null;
	public ComplexSceneAnnotateTab annotateTabPanel = null;
	public ComplexSceneEditTab editTabPanel = null;
	public ComplexSceneFormatSStrTab formatSStrTabPanel = null;

	JButton pickStrandBt = null;
	private JInternalFrame pickStrandFrame = null;
	private JPanel pickStrandPanel = null;
	public JTree pickStrandTree = null;

	private JInternalFrame findFrame = null;
	private JPanel findPanel = null;
	private JTextField findNuc_TF = null;

	protected JTabbedPane complexTabbedPane = null;

	public JComboBox nucPickMode_CB = null;

// this is the godam size of the little zoom panel
	protected int controlPanelW = 240;
	protected int controlPanelH = 240;

	public Cursor complexDefaultCursor = null;
	public Cursor complexWaitCursor = null;

	private int lftInset = 2;
	private Insets btInsets = null;

	private boolean runTestOnly = false;
	public boolean fileEdited = false;

	protected transient PropertyChangeSupport propertyChangeListeners = null;

	Robot viewRobot = null;

	public File complexSceneOutFile = null;

	/*
	 * protected String javaVersion = System.getProperty("java.version");
	 */
// might want to put these into DrawObjectView.java:
	public boolean hostIsLinux = false;
	public boolean hostIsMac = false;
	public boolean hostIsWindows = false;
	public boolean hostIsSolaris = false;

	private boolean suppressAlert = false;

	public Color moveableEditingColor = Color.blue;
	public Color staticEditingColor = Color.red;

	public ComplexSceneView(String fileName, JFrame parent, boolean suppressAlert) {
		this.suppressAlert = suppressAlert;
		frameParent = parent;

		String osName = System.getProperty("os.name").toLowerCase();
		if (osName != null) {
			if (osName.indexOf("linux") > -1) {
				hostIsLinux = true;
				// System.out.println("host is linux: " + osName);
			} else if (osName.indexOf("mac") > -1) {
				hostIsMac = true;
				this.setImgType(BufferedImage.TYPE_INT_ARGB_PRE);
				// System.out.println("host is mac: " + osName);
			} else if (osName.indexOf("windows") > -1) {
				hostIsWindows = true;
				// System.out.println("host is windows: " + osName);
			} else if (osName.indexOf("solaris") > -1) {
				hostIsSolaris = true;
				// System.out.println("host is solaris: " + osName);
			}
		} else {
			System.out.println("unknown host: " + osName);
		}

		try {
			this.setInputFileName(fileName);
			this.setComplexCursors();
			this.setCursor(this.complexDefaultCursor);

			propertyChangeListeners = new PropertyChangeSupport(this);

			if (fontChooser == null) {
				fontChooser = new FontChooser(this);
				fontChooser.resetFontSize(10);
				fontChooser.resetFontStyle(Font.PLAIN);
				fontChooser.fontTestTF.setText("A U C G R T Y");
				this.addPropertyChangeListener((PropertyChangeListener) fontChooser);
			}

			if ((colorChooser == null) && !runTestOnly) {
				colorChooser = new JColorChooser(Color.green);

				AbstractColorChooserPanel[] acList = colorChooser.getChooserPanels();
				AbstractColorChooserPanel[] newACList = new AbstractColorChooserPanel[4];

				newACList[0] = new HexColorChooserPanel();
				newACList[1] = acList[0];
				newACList[2] = acList[1];
				newACList[3] = acList[2];

				colorChooser.setChooserPanels(newACList);
				colorChooser.setPreviewPanel(new JPanel(true));
			}

			this.buildScalePanel();
			this.buildUndoPanels();

			complexPickPanel = new ViewImgCanvas();
			complexPickPanel.setBorder(new BevelBorder(1));
			complexPickPanel.setBackground(guiBGColor);
			complexPickPanel.setPreferredSize(new Dimension(controlPanelW, controlPanelW));

			complexPickPanel.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent event) {
					Point2D imgCenteredPt = new Point2D.Double(
							(double) event.getX() - (double) complexPickPanel.getWidth() / 2.0,
							(double) complexPickPanel.getHeight() / 2.0 - (double) event.getY());
					double scaleVal = 1.0 / getComplexPickScaleVal();
					try {
						ComplexSceneView.this.centerWindowToImgCoords(
								ComplexSceneView.this.imgSpaceX(imgCenteredPt.getX() * scaleVal),
								ComplexSceneView.this.imgSpaceY(-imgCenteredPt.getY() * scaleVal));
					} catch (Exception e) {
						ComplexSceneView.this.handleException(e, 1);
					}
				}

				public void mouseReleased(MouseEvent event) {
				}

				public void mouseEntered(MouseEvent event) {
					complexPickPanel.requestFocus();
				}

				public void mouseExited(MouseEvent event) {
				}
			});

			ioTabPanel = new ComplexSceneIOTab(guiBGColor, controlPanelW, controlPanelH);
			ioTabPanel.setParentDrawObjectView(this);
			ioTabPanel.setFontChooser(fontChooser);
			ioTabPanel.setColorChooser(colorChooser);
			ioTabPanel.setColorChangedState();
			this.addPropertyChangeListener(ioTabPanel);

			annotateTabPanel = new ComplexSceneAnnotateTab(guiBGColor, controlPanelW, controlPanelH);
			annotateTabPanel.setParentDrawObjectView(this);
			annotateTabPanel.setFontChooser(fontChooser);
			annotateTabPanel.setColorChooser(colorChooser);
			annotateTabPanel.setColorChangedState();
			this.addPropertyChangeListener(annotateTabPanel);

			editTabPanel = new ComplexSceneEditTab(guiBGColor, controlPanelW, controlPanelH);
			editTabPanel.setParentDrawObjectView(this);
			editTabPanel.setFontChooser(fontChooser);
			editTabPanel.setColorChooser(colorChooser);
			editTabPanel.setColorChangedState();
			this.addPropertyChangeListener(editTabPanel);

			formatSStrTabPanel = new ComplexSceneFormatSStrTab(guiBGColor, controlPanelW, controlPanelH);
			formatSStrTabPanel.setParentDrawObjectView(this);
			formatSStrTabPanel.setFontChooser(fontChooser);
			formatSStrTabPanel.setColorChooser(colorChooser);
			formatSStrTabPanel.setColorChangedState();
			this.addPropertyChangeListener(formatSStrTabPanel);

			buildGui();

			ioTabPanel.setPostBuildGuiMethods();
			annotateTabPanel.setPostBuildGuiMethods();
			editTabPanel.setPostBuildGuiMethods();
			formatSStrTabPanel.setPostBuildGuiMethods();

			this.viewRobot = new Robot();
			this.setPreShapesList(new Vector());
			this.setPostShapesList(new Vector());
		} catch (Exception e) {
			handleEx("Exception from Constructor: ", e, 1);
		}
	}

	public void addInternalFrame(JInternalFrame jif) {
		((ComplexParentFrame) frameParent).addInternalFrame(jif);
	}

	private void buildScalePanel() {
		scale_Panel = new JPanel(new BorderLayout(), true);
		scale_Panel.setBackground(guiBGColor);
		scale_Panel.setBorder(new TitledBorder(new BevelBorder(0), "Scale Figure:"));
		scaleTF = new JTextField(" 0.00", 3);
		scaleTF.setForeground(Color.black);
		scaleTF.setBackground(Color.white);
		scaleTF.setVisible(true);
		scaleTF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					String tfStr = scaleTF.getText();
					double tfDoubleVal = Double.parseDouble(tfStr);
					boolean containsScaleVal = false;
					for (int i = 0; i < scaleCB.getItemCount(); i++) {
						String cbStrVal = (String) scaleCB.getItemAt(i);
						if (tfStr.equals(cbStrVal)) {
							containsScaleVal = true;
							break;
						}
						double cbDoubleVal = Double.parseDouble(cbStrVal);
						if (tfDoubleVal == cbDoubleVal) {
							containsScaleVal = true;
							if (tfStr.indexOf(".") < 0)
								tfStr = tfStr + ".0";
							break;
						}
					}

					if (containsScaleVal) {
						scaleCB.setSelectedItem(tfStr);
					} else {
						boolean inserted = false;
						double cbDoubleVal = 0.0;
						for (int i = 0; i <= scaleCB.getItemCount() - 2; i++) {
							cbDoubleVal = Double.valueOf((String) scaleCB.getItemAt(i)).doubleValue();
							double nextVal = Double.valueOf((String) scaleCB.getItemAt(i + 1)).doubleValue();

							if ((tfDoubleVal > cbDoubleVal) && (tfDoubleVal < nextVal)) {
								scaleCB.insertItemAt(tfStr, i);
								scaleCB.setSelectedIndex(i);
								inserted = true;
								break;
							}
						}
						if (!inserted) {
							cbDoubleVal = Double.valueOf((String) scaleCB.getItemAt(0)).doubleValue();
							if (tfDoubleVal < cbDoubleVal) {
								scaleCB.insertItemAt(tfStr, 0);
								scaleCB.setSelectedIndex(0);
							} else {
								scaleCB.insertItemAt(tfStr, scaleCB.getItemCount());
								scaleCB.setSelectedIndex(scaleCB.getItemCount());
							}
						}
					}
				} catch (NumberFormatException e) {
					alert("Number format exception in scale text field: " + e.toString());
					// keep last angle and don't do anything
				} catch (Exception e) {
					ComplexSceneView.handleException("Exception in scaleTF.actionPerformed:", e, 101);
				}
			}
		});

		scaleCB = new JComboBox(new String[] { "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0", "2.0",
				"3.0", "4.0", "5.0", "6.0", "7.0", "8.0", "9.0", "10.0" });

		scaleCB.setActionCommand("Scale Figure");
		scaleCB.setFont(btFont);
		scaleCB.setForeground(Color.black);
		scaleCB.setBackground(guiBGColor);

		subScalePanel = new JPanel(new FlowLayout(FlowLayout.CENTER), true);
		subScalePanel.setFont(btFont);
		subScalePanel.setForeground(Color.black);
		subScalePanel.setBackground(guiBGColor);

		subScalePanel.add(scaleCB);
		subScalePanel.add(scaleTF);
		scale_Panel.add(BorderLayout.CENTER, subScalePanel);

		scaleCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// debug("SCALECB.AE: " + ae.toString());
				try {
					// setCursor doesn't work yet:
					// ComplexSceneIOTab.this.setCursor(
					setCursor(complexWaitCursor);
					setCursor(complexWaitCursor);
					scaleCB.setCursor(complexWaitCursor);
					int imgWidth = drawObjectImgW;
					int imgHeight = drawObjectImgH;

					double scaleVal = 1.0 / getFigureScale();

					double val = Double.parseDouble((String) scaleCB.getSelectedItem());

					// made it past valid double exception
					setFigureScale(val);
					scaleTF.setText(Double.toString(val));
					renderDrawObjectView();

					centerWindowToImgCoords(
							imgSpaceX(((double) (getWindowViewX() + (getWindowViewW() / 2)) - (double) imgWidth / 2.0)
									* scaleVal),
							imgSpaceY(-((double) imgHeight / 2.0 - (double) (getWindowViewY() + (getWindowViewH() / 2)))
									* scaleVal));

					setCursor(complexDefaultCursor);
					setCursor(complexDefaultCursor);
					scaleCB.setCursor(complexDefaultCursor);

					/*
					 * SAVE to show how go from one img to another: int imgWidth =
					 * complexPickPanel.getWidth(); int imgHeight = complexPickPanel.getHeight();
					 * 
					 * Point2D imgCenteredPt = new Point2D.Double( (double)event.getX() -
					 * (double)imgWidth/2.0, (double)imgHeight/2.0 - (double)event.getY());
					 * 
					 * // double scaleVal = getFigureScale()/getComplexPickScaleVal(); // double
					 * scaleVal = getFigureScale()/getComplexPickScaleVal()/getFigureScale(); //
					 * scaleVal /= getFigureScale(); double scaleVal = 1.0/getComplexPickScaleVal();
					 * 
					 * debug("NEW SCALE: " + scaleVal);
					 * 
					 * Point2D sceneCenteredPt = new Point2D.Double( imgCenteredPt.getX() *
					 * scaleVal, imgCenteredPt.getY() * scaleVal);
					 * 
					 * int sceneImgX = imgSpaceX(sceneCenteredPt.getX()); int sceneImgY =
					 * imgSpaceY(-sceneCenteredPt.getY());
					 * 
					 * printConsole("pick x,y: " + event.getX() + " " + event.getY() +
					 * " imgcenteredPt: " + StringUtil.roundStrVal(imgCenteredPt.getX(), 2) + " " +
					 * StringUtil.roundStrVal(imgCenteredPt.getY(), 2) + " sceneCenteredPt: " +
					 * StringUtil.roundStrVal(sceneCenteredPt.getX(), 2) + " " +
					 * StringUtil.roundStrVal(sceneCenteredPt.getY(), 2) + " sceneimgPt: " +
					 * sceneImgX + " " + sceneImgY);
					 * 
					 * try { centerWindowToImgCoords( sceneImgX, sceneImgY); } catch (Exception e) {
					 * handleException(e, 1); } END SAVE
					 */

				} catch (NumberFormatException e) {
					System.out.println("IN NUMBERFORMAT EXCEPTION in scaleCB: " + e.toString());
					// keep last angle and don't do anything
				} catch (Exception e) {
					ComplexSceneView.handleException("Exception in scaleCB.actionPerformed:", e, 101);
				}
			}
		});
	}

	private void buildUndoPanels() {
		int btLengthAdjust = -15; // make longest string centered

		undoEdit_Panel = new JPanel(new BorderLayout(), true);
		undoEdit_Panel.setBackground(guiBGColor);
		undoEdit_Panel.setBorder(new TitledBorder(new BevelBorder(1), "Undo Last Edit Command:"));

		JButton undo_Edit_Bt = new JButton();
		btInsets = undo_Edit_Bt.getInsets();
		undo_Edit_Bt.setMargin(new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
		undo_Edit_Bt.setText("Undo");
		undo_Edit_Bt.setActionCommand("Undo");
		undo_Edit_Bt.setFont(btFont);
		undo_Edit_Bt.setForeground(Color.black);
		undo_Edit_Bt.setBackground(guiBGColor);
		undo_Edit_Bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					String debugMsg = runUndoEdit_Bt();
					if (debugMsg != null)
						alert(debugMsg);
				} catch (Exception exp) {
					handleEx("Exception in ComplexSceneView.stateChanged:", exp, 101);
				}
			}
		});

		undoEdit_Panel.add(BorderLayout.CENTER, undo_Edit_Bt);

		undoFormat_Panel = new JPanel(new BorderLayout(), true);
		undoFormat_Panel.setBackground(guiBGColor);
		undoFormat_Panel.setBorder(new TitledBorder(new BevelBorder(1), "Undo Last Format Command:"));

		JButton undo_Format_Bt = new JButton();
		btInsets = undo_Format_Bt.getInsets();
		undo_Format_Bt.setMargin(new Insets(btInsets.top, lftInset, btInsets.bottom, btInsets.right + btLengthAdjust));
		undo_Format_Bt.setText("Undo");
		undo_Format_Bt.setActionCommand("Undo");
		undo_Format_Bt.setFont(btFont);
		undo_Format_Bt.setForeground(Color.black);
		undo_Format_Bt.setBackground(guiBGColor);
		undo_Format_Bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					String debugMsg = runUndoFormat_Bt();
					if (debugMsg != null)
						alert(debugMsg);
				} catch (Exception exp) {
					handleEx("Exception in ComplexSceneView.stateChanged:", exp, 101);
				}
			}
		});

		undoFormat_Panel.add(BorderLayout.CENTER, undo_Format_Bt);

		// MIGHT have to only add scale_Panel when needed. Maybe
		// can't place more than one copy around

		scaleUndoEdit_Panel = new JPanel(new BorderLayout(), true);
		// scaleUndoEdit_Panel.add(BorderLayout.CENTER, scale_Panel);
		scaleUndoEdit_Panel.add(BorderLayout.SOUTH, undoEdit_Panel);

		scaleUndoFormat_Panel = new JPanel(new BorderLayout(), true);
		// scaleUndoFormat_Panel.add(BorderLayout.CENTER, scale_Panel);
		scaleUndoFormat_Panel.add(BorderLayout.SOUTH, undoFormat_Panel);
	}

	public ComplexSceneView(String fileName, JFrame parent) {
		this(fileName, parent, false);
	}

	public ComplexSceneView(String fileName) {
		this(fileName, null);
	}

	public ComplexSceneView(String fileName, boolean testOnly) {
		try {
			this.setInputFileName(fileName);
			this.runTestOnly = testOnly;
		} catch (Exception e) {
			handleEx("Exception from Constructor: ", e, 1);
		}
	}

	public ComplexSceneView(ComplexScene2D complexScene) {
		this(null, null);
		this.setComplexScene(complexScene);
	}

	public ComplexSceneView(ComplexScene2D complexScene, boolean testOnly) {
		this.runTestOnly = testOnly;
		this.setComplexScene(complexScene);
	}

	public ComplexSceneView() {
		this(null, null);
	}

	static public void main(String args[]) {
		try {
			(new ComplexSceneView(null, null)).setVisible(true);
		} catch (Exception e) {
			handleException("Exception from main: ", e, 101);
		}
	}

	public int stopXRNA() {
		int result = 0;
		if (getComplexScene() == null)
			return (result); // go ahead and exit

		if (this.getCurrentInputFile() != null) {
			if (this.getCurrentInputFile().exists() && !this.getCurrentInputFile().canWrite())
				return (result);
		} else {
			return (result);
		}

		if (!fileEdited)
			return (result);

		try {
			Object[] message = new Object[3];
			message[1] = new JLabel("Are you sure you want to quit?");
			message[2] = new JLabel("Quitting before writing out will cause loss of any changes made to: "
					+ getComplexScene().getName());

			String[] options = { "Yes", "No", "Cancel" };
			result = JOptionPane.showOptionDialog(frameParent, // the parent that the dialog blocks
					message, // the dialog message array
					"Check User Cancel", // the title of the dialog window
					JOptionPane.OK_CANCEL_OPTION, // option type
					JOptionPane.PLAIN_MESSAGE, // message type
					null, // optional icon, use null to use the default icon
					options, // options string array, will be made into buttons
					options[0] // option that should be made into a default button
			);

			switch (result) {
			case 0: // yes
				// genImgExit();
				break;
			case 1: // no
				break;
			case 2: // cancel (same as no?)
				break;
			default:
				break;
			}
		} catch (Exception e) {
			handleEx("Exception in genFileChooser:", e, 98);
		}

		return (result);
	}

	public ActionListener stopXRNA_AL = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			int result = stopXRNA();
			switch (result) {
			case 0: // yes
				genImgExit();
				break;
			case 1: // no
				break;
			case 2: // cancel (same as no?)
				break;
			default:
				break;
			}
		}
	};

	public void buildGui() {
		border = new BevelBorder(1);
		raisedBorder = new BevelBorder(BevelBorder.RAISED);

		this.setLayout(new BorderLayout());
		this.setVisible(false);
		// this.setBounds(getGuiStartX(), getGuiStartY(), getGuiWidth(),
		// getGuiHeight());
		this.setBackground(guiBGColor);

		JPanel controlPanel = new JPanel(true);
		controlPanel.setLayout(new BorderLayout(0, 0));
		controlPanel.setFont(new Font("Dialog", Font.PLAIN, 10));
		controlPanel.setForeground(Color.black);
		controlPanel.setBackground(guiBGColor);
		controlPanel.setBorder(new TitledBorder(border, "Main Controls:"));
		this.add(BorderLayout.NORTH, controlPanel);

		generalBtPanel = new JPanel(true);
		generalBtPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		generalBtPanel.setFont(new Font("Dialog", Font.PLAIN, 10));
		generalBtPanel.setForeground(Color.black);
		generalBtPanel.setBackground(guiBGColor);
		generalBtPanel.setBorder(border);
		controlPanel.add("North", generalBtPanel);

		pickStrandBt = new JButton();
		pickStrandBt.setText("Pick Strand");
		pickStrandBt.setActionCommand("Pick_Strand");
		pickStrandBt.setFont(btFont);
		pickStrandBt.setForeground(Color.black);
		pickStrandBt.setBackground(guiBGColor);
		pickStrandBt.setVisible(true);
		generalBtPanel.add(pickStrandBt);
		pickStrandBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (getComplexScene() == null) {
					alert("Display Secondary Structure First");
					return;
				}
				try {
					if (pickStrandPanel == null) {
						pickStrandPanel = new JPanel(true);
						pickStrandPanel.setBackground(guiBGColor);
						pickStrandPanel.setForeground(Color.black);
						pickStrandPanel.setPreferredSize(new Dimension(200, 300));

						getComplexScene().resetComplexNodes();
						TreeNode complexSceneNode = getComplexScene().createComplexNodes();
						pickStrandTree = new JTree(complexSceneNode);
						pickStrandTree.setBackground(guiBGColor);
						pickStrandTree.setForeground(Color.black);

						pickStrandTree.addTreeExpansionListener(ioTabPanel);
						pickStrandTree.addTreeExpansionListener(annotateTabPanel);
						pickStrandTree.addTreeExpansionListener(editTabPanel);
						pickStrandTree.addTreeExpansionListener(formatSStrTabPanel);

						pickStrandTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

						pickStrandTree.addTreeSelectionListener(ioTabPanel);
						pickStrandTree.addTreeSelectionListener(annotateTabPanel);
						pickStrandTree.addTreeSelectionListener(editTabPanel);
						pickStrandTree.addTreeSelectionListener(formatSStrTabPanel);

						pickStrandPanel.add(BorderLayout.CENTER, pickStrandTree);
					}

					pickStrandTree.setVisible(true);
					pickStrandPanel.setVisible(true);

					pickStrandFrame = getBasicInternalFrame(110, 60, 200, 500);
					updateBasicInternalFrame("Structure List", "Select Current Structure:", pickStrandPanel,
							pickStrandFrame);
					if (pickStrandFrame.isIcon())
						pickStrandFrame.setIcon(false);
				} catch (Exception e) {
					handleEx("Exception in ComplexSceneView.pickStrandBt:", e, 101);
				}
			}
		});

		nucPickMode_CB = new JComboBox();
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNASingleNuc));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNASingleStrand));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNABasePair));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNAHelix));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNAHelicalRun));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNASubDomain));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNACycle));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNAListNucs));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNASSData));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNAColorUnit));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InRNANamedGroup));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InComplex));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InLabelsOnly));
		nucPickMode_CB.addItem(ComplexDefines.nucModeDefineToString(ComplexDefines.InComplexScene));

		nucPickMode_CB.setActionCommand("Pick_Mode");
		nucPickMode_CB.setSelectedItem("rna single nuc");
		nucPickMode_CB.setFont(btFont);
		nucPickMode_CB.setForeground(Color.black);
		nucPickMode_CB.setBackground(guiBGColor);
		JLabel gridLabel = new JLabel("Pick Mode:");
		gridLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		gridLabel.setLabelFor(nucPickMode_CB);
		generalBtPanel.add(nucPickMode_CB);
		nucPickMode_CB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				/*
				 * if (getComplexScene() == null) { alert("Display Secondary Structure First");
				 * return; }
				 */
				setCurrentComplexPickMode(nucPickMode_CB.getSelectedIndex());
			}
		});

		JButton renderBt = new JButton();
		renderBt.setText("Render");
		renderBt.setActionCommand("Render");
		renderBt.setFont(btFont);
		renderBt.setForeground(Color.black);
		renderBt.setBackground(guiBGColor);
		generalBtPanel.add(renderBt);
		renderBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				runRenderBt();
			}
		});

		/*
		 * testBt = new JButton(); testBt.setText("Test");
		 * testBt.setActionCommand("Test"); testBt.setFont(btFont);
		 * testBt.setForeground(Color.black); testBt.setBackground(guiBGColor);
		 * generalBtPanel.add(testBt); testBt.addActionListener(new ActionListener() {
		 * public void actionPerformed(ActionEvent evt) { runTestBt(); } });
		 */

		JButton updateXMLBt = new JButton();
		updateXMLBt.setText("Save");
		// updateXMLBt.setActionCommand("Save");
		updateXMLBt.setFont(btFont);
		updateXMLBt.setForeground(Color.black);
		updateXMLBt.setBackground(guiBGColor);
		generalBtPanel.add(updateXMLBt);
		updateXMLBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					boolean writeComplete = runWriteXMLBt(getCurrentInputFile());
					if (writeComplete) {
						printConsole("XRNA file updated to: " + getCurrentInputFile().getName());
						fileEdited = false;
					}
				} catch (Exception e) {
					handleEx("Exception in updateXMLBt:", e, 98);
				}
			}
		});

		JButton findBt = new JButton();
		findBt.setText("Find");
		findBt.setActionCommand("find");
		findBt.setFont(btFont);
		findBt.setForeground(Color.black);
		findBt.setBackground(guiBGColor);
		generalBtPanel.add(findBt);
		findBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					if (getComplexScene() == null)
						return;
					buildFindPanel();
					initFindFrame();
				} catch (Exception e) {
					handleEx("Exception in findBt:", e, 98);
				}
			}
		});

		genFileChooser = new JFileChooser(this.getComplexXMLRepositoryPath());
		this.setComplexXMLRepositoryPath(genFileChooser.getCurrentDirectory().toString());

		// Filetypes it will output. Just visually giving options

		genWriteFileFilter = new GenFileFilter();
		genWriteFileFilter.addExtension("xml");
		genWriteFileFilter.addExtension("xrna");
		genWriteFileFilter.setDescription("XRNA I/O files");

		genWriteFileFilterCSV = new GenFileFilter();
		genWriteFileFilterCSV.addExtension("csv");
		genWriteFileFilterCSV.setDescription("RiboVision CSV file");

		genWriteFileFilterSVG = new GenFileFilter();
		genWriteFileFilterSVG.addExtension("svg");
		genWriteFileFilterSVG.setDescription("RiboVision SVG file");

		genWriteFileFilterTR = new GenFileFilter();
		genWriteFileFilterTR.addExtension("tr");
		genWriteFileFilterTR.setDescription("RiboVision TR file");

		genWriteFileFilterBPSeq = new GenFileFilter();
		genWriteFileFilterBPSeq.addExtension("bpseq");
		genWriteFileFilterBPSeq.setDescription("RiboVision BPSeq file");

		stopBt = new JButton();
		stopBt.setText("Stop XRNA");
		stopBt.setActionCommand("Stop");
		stopBt.setFont(btFont);
		stopBt.setForeground(Color.black);
		stopBt.setBackground(guiBGColor);
		generalBtPanel.add(stopBt);
		stopBt.addActionListener(stopXRNA_AL);

		// File types it will read in
		genReadFileFilter = new GenFileFilter();
		genReadFileFilter.addExtension("xml");
		genReadFileFilter.addExtension("xrna");
		genReadFileFilter.addExtension("ss");
		genReadFileFilter.addExtension("ps");
		genReadFileFilter.addExtension("svg");
		genReadFileFilter.addExtension("str");
		genReadFileFilter.setDescription(
				"XRNA I/O files, old-style XRNA .ss files, old-style XRNA .ps files, STR files, or manually edited SVG files");

		complexTabbedPane = new JTabbedPane();
		complexTabbedPane.setBackground(new Color(0xff9999ff));

		this.add(complexTabbedPane, BorderLayout.CENTER);

		this.setViewImgCanvas(new ViewImgCanvas(this));

		this.getViewImgCanvas().setBackground(guiBGColor);
		this.getViewImgCanvas().setOpaque(true);
		/*
		 * debug("IMG CANVAS is DOUBLE BUFFERED: " +
		 * this.getViewImgCanvas().isDoubleBuffered());
		 * debug("THIS is DOUBLE BUFFERED: " + this.isDoubleBuffered());
		 * debug("IMG CANVAS is OPTIMIZED: " +
		 * this.getViewImgCanvas().isOptimizedDrawingEnabled());
		 * debug("THIS is OPTIMIZED: " + this.isOptimizedDrawingEnabled());
		 */

		genImgScrollPane = new JScrollPane(this.getViewImgCanvas(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		genImgScrollPane.setBorder(new TitledBorder(border, "Import/Export Structure:"));
		genImgScrollPane.setBackground(guiBGColor);
		genImgScrollPane.getVerticalScrollBar().addAdjustmentListener(this);
		genImgScrollPane.getHorizontalScrollBar().addAdjustmentListener(this);
		genImgScrollPane.setVisible(true);
		/*
		 * genImgScrollPane.getVerticalScrollBar().setBlockIncrement(0);
		 * genImgScrollPane.getHorizontalScrollBar().setBlockIncrement(0);
		 * genImgScrollPane.getVerticalScrollBar().setUnitIncrement(0);
		 * genImgScrollPane.getHorizontalScrollBar().setUnitIncrement(0);
		 */

		/*
		 * KeyListener keyListeners[] =
		 * genImgScrollPane.getVerticalScrollBar().getKeyListeners();
		 * debug("LISTEN COUNT: " + keyListeners.length);
		 */
		/*
		 * ActionListener actionListener =
		 * genImgScrollPane.getVerticalScrollBar().getActionForKeyStroke(KeyStroke.
		 * getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0)); debug("actionListener: " +
		 * actionListener);
		 */
		/*
		 * genImgScrollPane.getVerticalScrollBar().unregisterKeyboardAction(KeyStroke.
		 * getKeyStroke(java.awt.event.KeyEvent.VK_UP, 0));
		 */

		/*
		 * InputMap im = genImgScrollPane.getVerticalScrollBar().getInputMap();
		 * ActionMap am = genImgScrollPane.getVerticalScrollBar().getActionMap(); //
		 * InputMap im = genImgScrollPane.getInputMap(); // ActionMap am =
		 * genImgScrollPane.getActionMap(); debug("am.size: " + am.size());
		 * debug("im.size: " + im.size()); KeyStroke up =
		 * KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0); Object upKey = im.get(up);
		 * am.put(upKey, null);
		 */

		genImgScrollPane.getVerticalScrollBar().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent keyEvent) {
				// debug("MADE IT SCROLLBAR KEYPRESSED");
				genImgScrollPane.getVerticalScrollBar().setUnitIncrement(0);
			}

			public void keyReleased(KeyEvent keyEvent) {
				// debug("MADE IT SCROLLBAR KEYReleased");
				genImgScrollPane.getVerticalScrollBar().setUnitIncrement(1);
			}

			public void keyTyped(KeyEvent keyEvent) {
				// debug("MADE IT KEYTyped");
			}
		});

		/*
		 * this.getViewImgCanvas().addKeyListener(new KeyAdapter() { public void
		 * keyPressed(KeyEvent keyEvent) { // runTestOnly = true; //
		 * debug("MADE IT KEYPressed"); }
		 * 
		 * public void keyReleased(KeyEvent keyEvent) { // runTestOnly = false; //
		 * debug("MADE IT KEYReleased"); }
		 * 
		 * public void keyTyped(KeyEvent keyEvent) { // debug("MADE IT KEYTyped"); } });
		 */

		/*
		 * JViewport viewport = genImgScrollPane.getViewport();
		 * viewport.setViewPosition(new Point(1000, 1000));
		 * genImgScrollPane.setViewport(viewport);
		 */
		/*
		 * JScrollBar vsb = genImgScrollPane.getVerticalScrollBar(); JScrollBar hsb =
		 * genImgScrollPane.getHorizontalScrollBar(); vsb.setValue(300);
		 * hsb.setValue(300);
		 */

		this.getViewImgCanvas().setVisible(true);

		complexTabbedPane.addTab("Import/Export", ioTabPanel);
		complexTabbedPane.addTab("Annotate", annotateTabPanel);
		complexTabbedPane.addTab("Edit", editTabPanel);
		complexTabbedPane.addTab("Format", formatSStrTabPanel);

		// HERE. change tabpanel scrollpane with setselectedindex
		complexTabbedPane.setSelectedIndex(0);
		if (complexTabbedPane.getSelectedIndex() == 0) {
			ioTabPanel.add(BorderLayout.CENTER, genImgScrollPane);
			ioTabPanel.controlPanel.add(BorderLayout.SOUTH, this.complexPickPanel);
			ioTabPanel.controlPanel.add(BorderLayout.NORTH, this.scale_Panel);
		} else if (complexTabbedPane.getSelectedIndex() == 1) {
			annotateTabPanel.add(BorderLayout.CENTER, genImgScrollPane);
			annotateTabPanel.controlPanel.add(BorderLayout.SOUTH, this.complexPickPanel);
			annotateTabPanel.controlPanel.add(BorderLayout.NORTH, this.scale_Panel);
		} else if (complexTabbedPane.getSelectedIndex() == 2) // Edit Tab
		{
			editTabPanel.add(BorderLayout.CENTER, genImgScrollPane);
			editTabPanel.controlPanel.add(BorderLayout.SOUTH, this.complexPickPanel);

			this.scaleUndoEdit_Panel.add(BorderLayout.CENTER, scale_Panel);
			editTabPanel.controlPanel.add(BorderLayout.NORTH,
					/*
					 * this.scale_Panel);
					 */
					this.scaleUndoEdit_Panel);
		} else if (complexTabbedPane.getSelectedIndex() == 3) // Format Tab
		{
			formatSStrTabPanel.add(BorderLayout.CENTER, genImgScrollPane);
			formatSStrTabPanel.controlPanel.add(BorderLayout.SOUTH, this.complexPickPanel);
			this.scaleUndoFormat_Panel.add(BorderLayout.CENTER, scale_Panel);
			formatSStrTabPanel.controlPanel.add(BorderLayout.NORTH,
					/*
					 * this.scale_Panel);
					 */
					this.scaleUndoFormat_Panel);
		}

		this.add(BorderLayout.CENTER, complexTabbedPane);

		// bug: currently doesn't work for metal L&F
		UIManager.put("TabbedPane.selected", guiBGColor);

		/*
		 * complexTabbedPane.setBackgroundAt(0, guiBGColor.darker());
		 * complexTabbedPane.setForegroundAt(0, Color.black);
		 * complexTabbedPane.setBackgroundAt(1, guiBGColor.darker());
		 * complexTabbedPane.setForegroundAt(1, Color.black);
		 * complexTabbedPane.setBackgroundAt(1, guiBGColor.darker());
		 * complexTabbedPane.setForegroundAt(1, Color.black);
		 */

		userConsoleScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		userConsoleScrollPane.setOpaque(true);
		userConsoleScrollPane.setForeground(Color.black);
		userConsoleScrollPane.setBackground(guiBGColor);
		userConsoleScrollPane.setBorder(new TitledBorder(border, "System Out"));
		userConsoleOut = new JTextField();
		userConsoleOut.setMargin(new java.awt.Insets(0, 0, 0, 0));
		userConsoleOut.setFont(new Font("Monospaced", Font.PLAIN, 10));
		userConsoleOut.setForeground(Color.black);
		userConsoleOut.setBackground(Color.white);
		userConsoleOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setUserConsoleString(userConsoleOut.getText());
				clearConsole();
				resetTabPanelFocus();
			}
		});
		userConsoleScrollPane.getViewport().add(userConsoleOut);

		this.add(BorderLayout.SOUTH, userConsoleScrollPane);

		complexTabbedPane.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				try {
					SingleSelectionModel model = (SingleSelectionModel) e.getSource();
					// debug("SELECTED INDEX: " + model.getSelectedIndex());
					switch (model.getSelectedIndex()) {
					case 0: // transform tab
						ioTabPanel.add(BorderLayout.CENTER, genImgScrollPane);
						ioTabPanel.controlPanel.add(BorderLayout.SOUTH, complexPickPanel);
						ioTabPanel.controlPanel.add(BorderLayout.NORTH, scale_Panel);
						annotateTabPanel.remove(genImgScrollPane);
						annotateTabPanel.controlPanel.remove(complexPickPanel);
						annotateTabPanel.controlPanel.remove(scale_Panel);
						editTabPanel.remove(genImgScrollPane);
						editTabPanel.controlPanel.remove(complexPickPanel);
						editTabPanel.controlPanel.remove(scale_Panel);
						editTabPanel.resetCurrentVars(true);
						formatSStrTabPanel.remove(genImgScrollPane);
						formatSStrTabPanel.controlPanel.remove(complexPickPanel);
						formatSStrTabPanel.controlPanel.remove(scale_Panel);
						break;
					case 1: // annotate tab
						ioTabPanel.remove(genImgScrollPane);
						ioTabPanel.controlPanel.remove(complexPickPanel);
						ioTabPanel.controlPanel.remove(scale_Panel);
						annotateTabPanel.add(BorderLayout.CENTER, genImgScrollPane);
						annotateTabPanel.controlPanel.add(BorderLayout.SOUTH, complexPickPanel);
						annotateTabPanel.controlPanel.add(BorderLayout.NORTH, scale_Panel);
						editTabPanel.remove(genImgScrollPane);
						editTabPanel.controlPanel.remove(complexPickPanel);
						editTabPanel.controlPanel.remove(scale_Panel);
						editTabPanel.resetCurrentVars(true);
						formatSStrTabPanel.remove(genImgScrollPane);
						formatSStrTabPanel.controlPanel.remove(complexPickPanel);
						formatSStrTabPanel.controlPanel.remove(scale_Panel);
						break;
					case 2: // edit tab
						ioTabPanel.remove(genImgScrollPane);
						ioTabPanel.controlPanel.remove(complexPickPanel);
						ioTabPanel.controlPanel.remove(scale_Panel);
						annotateTabPanel.remove(genImgScrollPane);
						annotateTabPanel.controlPanel.remove(complexPickPanel);
						annotateTabPanel.controlPanel.remove(scale_Panel);
						editTabPanel.add(BorderLayout.CENTER, genImgScrollPane);
						editTabPanel.controlPanel.add(BorderLayout.SOUTH, complexPickPanel);
						/*
						 * editTabPanel.controlPanel.add(BorderLayout.NORTH, scale_Panel);
						 */
						scaleUndoEdit_Panel.add(BorderLayout.CENTER, scale_Panel);
						editTabPanel.controlPanel.add(BorderLayout.NORTH, scaleUndoEdit_Panel);

						formatSStrTabPanel.remove(genImgScrollPane);
						formatSStrTabPanel.controlPanel.remove(complexPickPanel);
						formatSStrTabPanel.controlPanel.remove(scale_Panel);
						break;
					case 3: // formatSStr tab
						ioTabPanel.remove(genImgScrollPane);
						ioTabPanel.controlPanel.remove(complexPickPanel);
						ioTabPanel.controlPanel.remove(scale_Panel);
						annotateTabPanel.remove(genImgScrollPane);
						annotateTabPanel.controlPanel.remove(complexPickPanel);
						annotateTabPanel.controlPanel.remove(scale_Panel);
						editTabPanel.remove(genImgScrollPane);
						editTabPanel.controlPanel.remove(complexPickPanel);
						editTabPanel.controlPanel.remove(scale_Panel);
						editTabPanel.resetCurrentVars(true);
						formatSStrTabPanel.add(BorderLayout.CENTER, genImgScrollPane);
						formatSStrTabPanel.controlPanel.add(BorderLayout.SOUTH, complexPickPanel);
						/*
						 * formatSStrTabPanel.controlPanel.add(BorderLayout.NORTH, scale_Panel);
						 */
						scaleUndoFormat_Panel.add(BorderLayout.CENTER, scale_Panel);
						formatSStrTabPanel.controlPanel.add(BorderLayout.NORTH, scaleUndoFormat_Panel);
						break;
					default:
						break;
					}
					ComplexSceneView.this.resetImgTitle();
					ComplexSceneView.this.resetTabPanelFocus();
				} catch (Exception exp) {
					handleEx("Exception in ComplexSceneView.stateChanged:", exp, 101);
				}
			}
		});

		addComponentListener(new ComponentListener() {
			/*
			 * ... //where initialization occurs: aFrame = new JFrame("A Frame");
			 * ComponentPanel p = new ComponentPanel(this);
			 * aFrame.addComponentListener(this); p.addComponentListener(this); ...
			 */

			public void componentHidden(ComponentEvent e) {
				/*
				 * System.out.println("componentHidden event from " +
				 * e.getComponent().getClass().getName());
				 */
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentResized(ComponentEvent e) {
			}

			public void componentShown(ComponentEvent e) {
			}
		});
	}

	public String complexTabType() {
		if (complexTabbedPane.getSelectedIndex() == 0)
			return ("Import/Export");
		else if (complexTabbedPane.getSelectedIndex() == 1)
			return ("Annotate");
		else if (complexTabbedPane.getSelectedIndex() == 2)
			return ("Edit");
		else if (complexTabbedPane.getSelectedIndex() == 3)
			return ("Format");
		return (null);
	}

	public String runUndoEdit_Bt() throws Exception {
		if (ComplexSceneView.this.getComplexScene() == null) {
			return ("Nothing to undo");
		}
		if (getCurrentUndoLevel() <= 0) {
			return ("Nothing to undo");
		}
		if (!ComplexSceneView.this.getComplexScene().resetLocationFromHashtable(getCurrentUndoLevel())) {
			return ("Nothing to undo: " + getCurrentUndoLevel());
		}
		ComplexSceneView.this.getComplexScene().clearLocationFromHashtable(getCurrentUndoLevel());
		System.gc();

		decCurrentUndoLevel();

		runRenderBt();
		ComplexSceneView.this.renderDrawObjectView();

		return (null);
	}

	public String runUndoFormat_Bt() throws Exception {
		if (ComplexSceneView.this.getComplexScene() == null) {
			return ("Nothing to undo");
		}
		if (getCurrentUndoLevel() <= 0) {
			return ("Nothing to undo");
		}
		if (!ComplexSceneView.this.getComplexScene().resetBasePairFromHashtable(getCurrentUndoLevel())) {
			return ("Nothing to undo: " + getCurrentUndoLevel());
		}
		ComplexSceneView.this.getComplexScene().clearBasePairFromHashtable(getCurrentUndoLevel());
		System.gc();

		decCurrentUndoLevel();

		runRenderBt();
		ComplexSceneView.this.renderDrawObjectView();

		return (null);
	}

	public void runTestBt() {
		try {
			debug("Java memory in use = " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));

			/*
			 * printConsole("CURR WIN CENTER: " + (this.getWindowViewX() +
			 * this.getWindowViewW()/2) + " " + (this.getWindowViewY() +
			 * this.getWindowViewH()/2));
			 */

			// showUserMsg("MADE it to testbt");

			// this.centerScrollBars();

			/*
			 * File genImgOutFile = new File("images/test_off_img.jpg");
			 * debug("WORKING ON: " + genImgOutFile.toString() + " " +
			 * genImgOutFile.canWrite()); String outFileName =
			 * JAIUtil.printImage(this.getOffScreenImg(), genImgOutFile);
			 * 
			 * genImgOutFile = new File("images/test_on_img.jpg"); debug("WORKING ON: " +
			 * genImgOutFile.toString() + " " + genImgOutFile.canWrite()); outFileName =
			 * JAIUtil.printImage(this.getSceneImg(), genImgOutFile);
			 */

			/*
			 * BufImgApps.printGifImage(this.getOffScreenImg(), genImgOutFile);
			 */

			/*
			 * // Use the pre-defined flavor for a Printable from an InputStream DocFlavor
			 * flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
			 * 
			 * // Specify the type of the output stream // String psMimeType =
			 * DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
			 * 
			 * // Locate factory which can export a GIF image stream as Postscript //
			 * StreamPrintServiceFactory[] factories =
			 * StreamPrintServiceFactory.lookupStreamPrintServiceFactories( flavor,
			 * psMimeType); if (factories.length == 0) { alert("No suitable factories");
			 * return; }
			 * 
			 * try { // Create a file for the exported postscript // FileOutputStream fos =
			 * new FileOutputStream("out.ps");
			 * 
			 * // Create a Stream printer for Postscript // StreamPrintService sps =
			 * factories[0].getPrintService(fos);
			 * 
			 * // Create and call a Print Job // DocPrintJob pj = sps.createPrintJob();
			 * PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
			 * 
			 * Doc doc = new SimpleDoc(this, flavor, null);
			 * 
			 * pj.print(doc, aset); fos.close();
			 * 
			 * } catch (PrintException pe) {
			 * handleEx("Exception in ComplexSceneView.runTestBt0:", pe, 101); } catch
			 * (IOException ie) { handleEx("Exception in ComplexSceneView.runTestBt1:", ie,
			 * 101); }
			 */
		} catch (Exception e) {
			handleEx("Exception in ComplexSceneView.runTestBt:", e, 101);
		}
	}

	public void runRenderBt() {
		try {
			if (this.getComplexScene() != null)
				this.getComplexScene().setEditColor(null);
			this.renderDrawObjectView();
			if (editTabPanel != null)
				editTabPanel.setCursor(this.complexDefaultCursor);
		} catch (Exception e) {
			handleEx("Exception in ComplexSceneView.runRenderBt:", e, 101);
		}
	}

	public void runInitFromProperties() {
		try {
			if (this.getInputFileName() != null)
				this.runInitFromProperties(new File(this.getInputFileName()));
		} catch (Exception e) {
			handleEx("\nException in ComplexSceneView.runInitFromProperties:", e, 98);
		}
	}

	public void runInitFromProperties(File dataFile, String path) {
		try {
			this.setComplexXMLRepositoryPath(path);
			if (dataFile == null)
				return;

			String fileName = dataFile.getName();
			if (fileName.endsWith(".ss") || fileName.endsWith(".xrna") || fileName.endsWith(".xml")
					|| fileName.endsWith(".ps") || fileName.endsWith(".svg") || fileName.endsWith(".str"))
				this.setCurrentInputFile(dataFile);
			else
				this.setCurrentInputFile(new File(fileName + ".xrna"));

			this.runSetFromInputFile();
			this.resetImgTitle();
		} catch (Exception e) {
			handleEx("\nException in ComplexSceneView.runInitFromProperties:", e, 98);
		}
	}

	public void runInitFromProperties(File dataFile) {
		this.runInitFromProperties(dataFile, ".");
	}

	/**
	 * 
	 * Duplicating runWriteXMLBt() for runWriteCSVBt()
	 * 
	 **/
	public boolean runWriteXMLBt() throws Exception {
		return (this.runWriteXMLBt(complexSceneOutFile));
	}

	public boolean runWriteCSVBt() throws Exception {
		return (this.runWriteCSVBt(complexSceneOutFile));
	}

	public boolean runWriteTRBt() throws Exception {
		return this.runWriteTRBt(complexSceneOutFile);
	}

	public boolean runWriteSVGBt() throws Exception {
		return this.runWriteSVGBt(complexSceneOutFile);
	}

	public boolean runWriteBPSeqBt() throws Exception {
		return this.runWriteBPSeqBt(complexSceneOutFile);
	}

	public boolean runWriteXMLBt(String fileName) throws Exception {
		return (this.runWriteXMLBt(new File(fileName)));
	}

	public boolean runWriteCSVBt(String fileName) throws Exception {
		return (this.runWriteCSVBt(new File(fileName)));
	}

	public boolean runWriteTBt(String fileName) throws Exception {
		return this.runWriteTRBt(new File(fileName));
	}

	public boolean runWriteXMLBt(File outFile) throws Exception {
		if (outFile == null)
			return (false);
		if (outFile.exists() && (!outFile.canWrite())) {
			alert("Can't write " + outFile.getName() + " ; Need to set writeable");
			return (false);
		}

		if (outFile.getName().endsWith(".ss")) {
			alert("Can't write to file ending with .ss: " + outFile.getName() + "; Need to change extension\n"
					+ "to .xrna in Import/Export Tab with 'Write XRNA I/O file' button");
			return (false);
		}

		if (outFile.getName().endsWith(".ps")) {
			alert("Can't write to file ending with .ps: " + outFile.getName() + "; Need to change extension\n"
					+ "to .xrna in Import/Export Tab with 'Write XRNA I/O file' button");
			return (false);
		}

		try {
			this.getComplexScene().printComplexXML(outFile);
		} catch (java.io.FileNotFoundException e) {
			alert("Can't find file: " + outFile.getName() + "\n" + e.toString());

			return (false);
		}

		return (true);
	}

	public boolean runWriteCSVBt(File outFile) throws Exception {
		if (outFile == null)
			return (false);

		if (outFile.exists() && (!outFile.canWrite())) {
			alert("Can't write " + outFile.getName() + " ; Need to set writeable");
			return (false);
		}

		try {
			// System.out.println("ComplexSceneView.java this.getComplexScene() is: " +
			// this.getComplexScene());
			this.getComplexScene().printComplexCSV(outFile);
		} catch (java.io.FileNotFoundException e) {
			alert("Can't find file: " + outFile.getName() + "\n" + e.toString());

			return (false);
		}

		return (true);
	}

	public boolean runWriteTRBt(File outFile) throws Exception {
		if (outFile == null) {
			return false;
		}
		if (outFile.exists() && !outFile.canWrite()) {
			alert("Can't write " + outFile.getName() + " ; Need to set writeable");
			return false;
		}
		try {
			this.getComplexScene().printComplexTR(outFile);
		} catch (java.io.FileNotFoundException ex) {
			alert("Can't find file: " + outFile.getName() + "\n" + ex);
			return false;
		}
		return true;
	}

	public boolean runWriteSVGBt(File outFile) throws Exception {
		if (outFile == null) {
			return false;
		}
		if (outFile.exists() && !outFile.canWrite()) {
			alert("Can't write" + outFile.getName() + " ; Need to set writeable");
			return false;
		}
		try {
			this.getComplexScene().printComplexSVG(outFile);
		} catch (java.io.FileNotFoundException ex) {
			alert("Can't find file: " + outFile.getName() + "\n" + ex);
			return false;
		}
		return true;
	}

	public boolean runWriteBPSeqBt(File outFile) throws Exception {
		if (outFile == null) {
			return false;
		}
		if (outFile.exists() && !outFile.canWrite()) {
			alert("Can't write" + outFile.getName() + " ; Need to set writeable");
			return false;
		}
		try {
			this.getComplexScene().printComplexBPSeq(outFile);
		} catch (java.io.FileNotFoundException ex) {
			alert("Can't find file: " + outFile.getName() + "\n" + ex);
			return false;
		}
		return true;
	}

	public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
		// super.addPropertyChangeListener(l);
		propertyChangeListeners.addPropertyChangeListener(l);
	}

	public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
		// super.removePropertyChangeListener(l);
		propertyChangeListeners.removePropertyChangeListener(l);
	}

	/************* MOUSE STUFF *******************************/

	public void setMouseMethod() {
	}

	public void doViewImgMousePressed() {
	}

	public void doViewImgMouseReleased() {
	}

	public void doViewImgMouseDragged() {
	}

	public void doViewImgMouseMoved() {
	}

	/**************** END MOUSE STUFF **************************/

	private void showUserMsg(String msg) {
		if (runTestOnly)
			return;
		JOptionPane.showMessageDialog(ComplexSceneView.this, msg);
	}

	public void centerScrollBars() {
		/*
		 * debug("VIEWPORT BOUNDS 0: " + genImgScrollPane.getViewport().getViewRect());
		 * debug("VIEWPORT SIZE 0: " + genImgScrollPane.getViewport().getViewSize());
		 * debug("VIEWPORT EXTENT SIZE 0: " +
		 * genImgScrollPane.getViewport().getExtentSize()); debug("VIEW SIZE 0: " +
		 * genImgScrollPane.getViewport().getView().getBounds()); debug("PANE SIZE 0: "
		 * + genImgScrollPane.getBounds());
		 */

		// this.genImgScrollPane.addNotify();

		Dimension currDim = this.getViewImgCanvas().getPreferredSize();
		int currW = (int) Math.round(currDim.getWidth());
		int currH = (int) Math.round(currDim.getHeight());
		JScrollBar scrollBarX = genImgScrollPane.getHorizontalScrollBar();
		JScrollBar scrollBarY = genImgScrollPane.getVerticalScrollBar();
		// scrollBarX.addNotify();
		// scrollBarY.addNotify();

		/*
		 * debug("BEFORE: "); debug("SCROLL X MIN: " + scrollBarX.getMinimum());
		 * debug("SCROLL Y MIN: " + scrollBarY.getMinimum()); debug("SCROLL X MAx: " +
		 * scrollBarX.getMaximum()); debug("SCROLL Y MAx: " + scrollBarY.getMaximum());
		 * debug("SCROLL X VIS AMT: " + scrollBarX.getVisibleAmount());
		 * debug("SCROLL Y VIS AMT: " + scrollBarY.getVisibleAmount());
		 */

		/*
		 * scrollBarX.setMaximum(currW); scrollBarY.setMaximum(currH); //
		 * debug("setting scroll bar X,Y MXS: " + currW + " " + scrollBarX.getMaximum()
		 * + " " + currH + " " + scrollBarY.getMaximum());
		 */

		// this indicates that genImgScrollPane isn't showing yet and therefore
		// can't get all the goods. Need to supply some best guess info.
		if ((scrollBarX.getVisibleAmount() < 100) && (scrollBarY.getVisibleAmount() < 100)) {
			scrollBarX.setValue(currW / 2 - 600 / 2);
			scrollBarY.setValue(currH / 2 - 453 / 2);
		} else {
			scrollBarX.setValue(currW / 2 - scrollBarX.getVisibleAmount() / 2);
			scrollBarY.setValue(currH / 2 - scrollBarY.getVisibleAmount() / 2);
		}

		/*
		 * debug("AFTER: "); debug("SCROLL X MIN: " + scrollBarX.getMinimum());
		 * debug("SCROLL Y MIN: " + scrollBarY.getMinimum()); debug("SCROLL X MAx: " +
		 * scrollBarX.getMaximum()); debug("SCROLL Y MAx: " + scrollBarY.getMaximum());
		 * debug("SCROLL X VIS AMT: " + scrollBarX.getVisibleAmount());
		 * debug("SCROLL Y VIS AMT: " + scrollBarY.getVisibleAmount());
		 */

		// this simply triggers stuff to happen
		genImgScrollPane.setViewport(genImgScrollPane.getViewport());
		/*
		 * debug("VIEWPORT BOUNDS 1: " + genImgScrollPane.getViewport().getViewRect());
		 * debug("VIEWPORT SIZE 1: " + genImgScrollPane.getViewport().getViewSize());
		 * debug("VIEWPORT EXTENT SIZE 1: " +
		 * genImgScrollPane.getViewport().getExtentSize()); debug("VIEW SIZE 1: " +
		 * genImgScrollPane.getViewport().getView().getBounds()); debug("PANE SIZE 1: "
		 * + genImgScrollPane.getBounds());
		 */
	}

	public Point getViewPortPt() {
		return (this.genImgScrollPane.getViewport().getLocationOnScreen());
	}

	public void setMouseToNuc(DrawObject drwObj) throws Exception {
		if (drwObj == null)
			return;
		double drwObjX = drwObj.getX();
		double drwObjY = drwObj.getY();

		AffineTransform parentAffTrans = ((DrawObjectCollection) drwObj.getParentCollection()).getG2Transform();
		AffineTransform drwObjAffTrans = drwObj.getG2Transform();

		Point2D testPt = new Point2D.Double(drwObjAffTrans.getTranslateX(), drwObjAffTrans.getTranslateY());
		Point2D newTestPt = new Point2D.Double();
		parentAffTrans.inverseTransform(testPt, newTestPt);
		Point2D newerTestPt = new Point2D.Double();

		// this should be back to screen coords:
		parentAffTrans.transform(newTestPt, newerTestPt);

		// this.centerWindowToImgCoords((int)newerTestPt.getX(),
		// (int)newerTestPt.getY());
		JScrollBar scrollBarX = genImgScrollPane.getHorizontalScrollBar();
		JScrollBar scrollBarY = genImgScrollPane.getVerticalScrollBar();

		// Point viewPortPt = this.genImgScrollPane.getViewport().getLocationOnScreen();
		Point viewPortPt = this.getViewPortPt();
		int viewPortStartX = (int) viewPortPt.getX();
		int viewPortStartY = (int) viewPortPt.getY();

		int newMouseX = viewPortStartX + (int) newerTestPt.getX() - scrollBarX.getValue();
		int newMouseY = viewPortStartY + (int) newerTestPt.getY() - scrollBarY.getValue();

		this.viewRobot.mouseMove(newMouseX, newMouseY);
	}

	public void setWindowToDrawObjectCenter(DrawObject drwObj) throws Exception {
		if (drwObj == null)
			return;
		double drwObjX = drwObj.getX();
		double drwObjY = drwObj.getY();

		AffineTransform parentAffTrans = ((DrawObjectCollection) drwObj.getParentCollection()).getG2Transform();
		AffineTransform drwObjAffTrans = drwObj.getG2Transform();

		Point2D testPt = new Point2D.Double(drwObjAffTrans.getTranslateX(), drwObjAffTrans.getTranslateY());
		Point2D newTestPt = new Point2D.Double();
		parentAffTrans.inverseTransform(testPt, newTestPt);
		Point2D newerTestPt = new Point2D.Double();

		// this should be back to screen coords:
		parentAffTrans.transform(newTestPt, newerTestPt);

		// this.centerWindowToImgCoords((int)newerTestPt.getX(),
		// (int)newerTestPt.getY());
		JScrollBar scrollBarX = genImgScrollPane.getHorizontalScrollBar();
		JScrollBar scrollBarY = genImgScrollPane.getVerticalScrollBar();

		// Point viewPortPt = this.genImgScrollPane.getViewport().getLocationOnScreen();
		Point viewPortPt = this.getViewPortPt();
		int viewPortStartX = (int) viewPortPt.getX();
		int viewPortStartY = (int) viewPortPt.getY();
		int viewPortEndX = viewPortStartX + scrollBarX.getVisibleAmount();
		int viewPortEndY = viewPortStartY + scrollBarY.getVisibleAmount();

		// centers on viewport
		// newMouseX += scrollBarX.getVisibleAmount()/2;
		// newMouseY += scrollBarY.getVisibleAmount()/2;
		//

		int newMouseX = viewPortStartX + (int) newerTestPt.getX() - scrollBarX.getValue();
		int newMouseY = viewPortStartY + (int) newerTestPt.getY() - scrollBarY.getValue();

		if ((newMouseX < viewPortStartX) || (newMouseX > viewPortEndX) || (newMouseY < viewPortStartY)
				|| (newMouseY > viewPortEndY)) {
			// debug("SETTING mousex,y from outside to: " + newMouseX + " " + newMouseY);
			scrollBarX.setValue((int) newerTestPt.getX() - scrollBarX.getVisibleAmount() / 2);
			scrollBarY.setValue((int) newerTestPt.getY() - scrollBarY.getVisibleAmount() / 2);

			viewPortStartX = (int) viewPortPt.getX();
			viewPortStartY = (int) viewPortPt.getY();
			viewPortEndX = viewPortStartX + scrollBarX.getVisibleAmount();
			viewPortEndY = viewPortStartY + scrollBarY.getVisibleAmount();

			newMouseX = viewPortStartX + (int) newerTestPt.getX() - scrollBarX.getValue();
			newMouseY = viewPortStartY + (int) newerTestPt.getY() - scrollBarY.getValue();
		}
		this.viewRobot.mouseMove(newMouseX, newMouseY);
	}

	public void centerWindowToImgCoords() throws Exception {
		this.centerWindowToImgCoords((int) this.viewSpaceX(this.getWindowViewX() + this.getWindowViewW() / 2),
				(int) this.viewSpaceY(this.getWindowViewY() + this.getWindowViewH() / 2));
	}

	/*
	 * Doesn't work yet (but not sure I'll need it) public void
	 * newCenterWindowToImgCoords(int imgX, int imgY) throws Exception { if
	 * (!this.isShowing()) return;
	 * 
	 * JScrollBar scrollBarX = genImgScrollPane.getHorizontalScrollBar(); JScrollBar
	 * scrollBarY = genImgScrollPane.getVerticalScrollBar();
	 * 
	 * JViewport viewport = genImgScrollPane.getViewport(); viewport.setViewSize(new
	 * Dimension(this.drawObjectImgW, this.drawObjectImgH)); //
	 * debug("viewport.getViewSize(): " + viewport.getViewSize()); ViewImgCanvas
	 * view = (ViewImgCanvas)viewport.getView(); debug("ViewImgCanvas.size: " +
	 * view.getWidth() + " " + view.getHeight());
	 * 
	 * viewport.scrollRectToVisible( new Rectangle( imgX -
	 * scrollBarX.getVisibleAmount()/2, imgY - scrollBarY.getVisibleAmount()/2, imgX
	 * + scrollBarX.getVisibleAmount()/2, imgY + scrollBarY.getVisibleAmount()/2 )
	 * ); debug("VIEWPORT SIZE: " + viewport.getViewSize());
	 * 
	 * resetTabPanelFocus(); }
	 */

// currently just works for pick img
	public void centerWindowToImgCoords(int imgX, int imgY) throws Exception {
		if (!this.isShowing())
			return;

		JViewport viewport = genImgScrollPane.getViewport();
		viewport.setViewSize(new Dimension(this.drawObjectImgW, this.drawObjectImgH));
		genImgScrollPane.setViewport(viewport);

		JScrollBar scrollBarX = genImgScrollPane.getHorizontalScrollBar();
		JScrollBar scrollBarY = genImgScrollPane.getVerticalScrollBar();

//	debug("CENTERING ON IMG X,Y: " + imgX + " " + imgY);

		scrollBarX.setMaximum(this.drawObjectImgW);
		scrollBarY.setMaximum(this.drawObjectImgH);
		// debug("scrollBarX.getMaximum: " + scrollBarX.getMaximum());
		// debug("scrollBarY.getMaximum: " + scrollBarY.getMaximum());
		// debug("scrollBarX.getVisibleAmount: " + scrollBarX.getVisibleAmount());
		// debug("scrollBarY.getVisibleAmount: " + scrollBarY.getVisibleAmount());

		/*
		 * debug("SETTING SCROLLX,Y TO: " + (imgX - scrollBarX.getVisibleAmount()/2) +
		 * " " + (imgY - scrollBarY.getVisibleAmount()/2));
		 */
		scrollBarX.setValue(imgX - scrollBarX.getVisibleAmount() / 2);
		scrollBarY.setValue(imgY - scrollBarY.getVisibleAmount() / 2);
		/*
		 * debug("SCROLLBARS GOT SET TO: " + scrollBarX.getValue() + " " +
		 * scrollBarY.getValue());
		 */

		/*
		 * scrollBarX.setMaximum(this.drawObjectImgW);
		 * scrollBarY.setMaximum(this.drawObjectImgH);
		 */
		/*
		 * debug("BUT gets SET TO: " + scrollBarX.getValue() + " " +
		 * scrollBarY.getValue()); debug("OR gets SET TO: " + (scrollBarX.getValue() +
		 * scrollBarX.getVisibleAmount()/2) + " " + (scrollBarY.getValue() +
		 * scrollBarY.getVisibleAmount()/2));
		 */

		// genImgScrollPane.setViewport(genImgScrollPane.getViewport());

		resetTabPanelFocus();
	}

	public void updateImgWindowView() {
		if (runTestOnly)
			return;

		Dimension currDim = this.getViewImgCanvas().getPreferredSize();
		int currW = (int) Math.round(currDim.getWidth());
		int currH = (int) Math.round(currDim.getHeight());
		if ((currW != drawObjectImgW) || (currH != drawObjectImgH)) {
			this.getViewImgCanvas().setPreferredSize(new Dimension(drawObjectImgW, drawObjectImgH));
		}

		if (this.genImgScrollPane == null)
			return;

		genImgScrollPane.setViewport(genImgScrollPane.getViewport());

		JScrollBar scrollBarY = this.genImgScrollPane.getVerticalScrollBar();
		JScrollBar scrollBarX = this.genImgScrollPane.getHorizontalScrollBar();

		scrollBarX.setMaximum(currW);
		scrollBarY.setMaximum(currH);

		this.setWindowViewParams(scrollBarX.getValue(), scrollBarY.getValue(), scrollBarX.getVisibleAmount(),
				scrollBarY.getVisibleAmount());
	}

	class ComplexPickPanelShowing extends Thread {
		public void run() {
			while (true) {
				if (ComplexSceneView.this.complexPickPanel.isShowing())
					break;
				this.yield();
			}
			try {
				ComplexSceneView.this.updateComplexPickPanel();
			} catch (Exception e) {
				handleEx("Exception in ComplexSceneView.ComplexPickPanelShowing.run():", e, 101);
			}
		}
	}

	double fitWidth = 0.0;
	double fitHeight = 0.0;

	public void updateComplexPickPanel() throws Exception {
		if (this.complexPickPanel == null)
			return;
		if (this.getComplexScene() == null)
			return;

		int imgWidth = this.complexPickPanel.getWidth();
		int imgHeight = this.complexPickPanel.getHeight();

		if ((imgWidth <= 0) || (imgHeight <= 0)) {
			return;
		}

		BufferedImage img = new BufferedImage(imgWidth, imgHeight, this.getImgType());

		if (this.getCurrentBoundingBox() != null) {
			fitWidth = this.getCurrentBoundingBox().getWidth();
			fitHeight = this.getCurrentBoundingBox().getHeight();
		}

		Graphics2D g2 = img.createGraphics();
		g2.setColor(this.guiBGColor);
		g2.fillRect(0, 0, imgWidth, imgHeight);
		g2.setColor(Color.black);

		// try to fit the biggest dimension into the smallest
		if (fitWidth > fitHeight) {
			if (imgWidth > imgHeight)
				this.setComplexPickScaleVal((double) imgHeight / fitWidth);
			else
				this.setComplexPickScaleVal((double) imgWidth / fitWidth);
		} else {
			if (imgWidth > imgHeight)
				this.setComplexPickScaleVal((double) imgHeight / fitHeight);
			else
				this.setComplexPickScaleVal((double) imgWidth / fitHeight);
		}

		g2.translate(imgWidth / 2, imgHeight / 2);
		g2.setClip(-imgWidth / 2, -imgHeight / 2, imgWidth, imgHeight);
		g2.scale(this.getComplexPickScaleVal(), this.getComplexPickScaleVal());
		g2.setColor(Color.black);
		/*
		 * g2.drawLine(0, 0, 1000, 1000); g2.drawLine(0, 0, -1000, -1000);
		 * g2.drawLine(0, 0, -1000, 1000); g2.drawLine(0, 0, 1000, -1000);
		 */

		ComplexScene2D scene = this.getComplexScene();

		g2.translate(scene.getX(), -scene.getY());

		for (int sceneItemID = 0; sceneItemID < scene.getItemCount(); sceneItemID++) {
			Object obj = scene.getItemAt(sceneItemID);
			if (!(obj instanceof ComplexSSDataCollection2D)) {
				alert("obj IS NOT ComplexSSDataCollection2D");
				return;
			}
			ComplexSSDataCollection2D ssDataCollection = (ComplexSSDataCollection2D) obj;
			g2.translate(ssDataCollection.getX(), -ssDataCollection.getY());
			for (int i = 0; i < ssDataCollection.getItemCount(); i++) {
				/*
				 * SSData2D sstr = (SSData2D)ssDataCollection.getItemAt(i);
				 * g2.translate(sstr.getX(), -sstr.getY()); sstr.drawBackbone(g2);
				 * g2.translate(-sstr.getX(), sstr.getY());
				 */
				NucCollection2D nucCollection = (NucCollection2D) ssDataCollection.getItemAt(i);
				g2.translate(nucCollection.getX(), -nucCollection.getY());
				nucCollection.drawBackbone(g2);
				g2.translate(-nucCollection.getX(), nucCollection.getY());
			}
			g2.translate(-ssDataCollection.getX(), ssDataCollection.getY());
		}

		this.complexPickPanel.setImage(img);
		this.complexPickPanel.repaint();
	}

	public void updateAfterRender() throws Exception {
		if (!runTestOnly) {
			(new ComplexPickPanelShowing()).start();
		}
	}

	private Vector preShapesList = null;

	public void setPreShapesList(Vector preShapesList) {
		this.preShapesList = preShapesList;
	}

	public Vector getPreShapesList() {
		return (this.preShapesList);
	}

	private Vector postShapesList = null;

	public void setPostShapesList(Vector postShapesList) {
		this.postShapesList = postShapesList;
	}

	public Vector getPostShapesList() {
		return (this.postShapesList);
	}

	public void createDrawList() throws Exception {
		if (this.getPreShapesList() != null) {
			for (Enumeration e = this.getPreShapesList().elements(); e.hasMoreElements();)
				this.getDrawObjectList().add((Shape) e.nextElement());
		}

		if (this.getComplexScene() != null)
			this.getDrawObjectList().add(this.getComplexScene());

		if (this.getPostShapesList() != null) {
			for (Enumeration e = this.getPostShapesList().elements(); e.hasMoreElements();) {
				this.getDrawObjectList().add((Shape) e.nextElement());
			}
		}
	}

	public void resetTabPanelFocus() {
		((JPanel) this.complexTabbedPane.getSelectedComponent()).requestFocus();
	}

	public void resetImgTitle() throws Exception {
		if (genImgScrollPane == null)
			return;

		String title = null;

		switch (complexTabbedPane.getSelectedIndex()) {
		case 0: // IO tab
			title = "Import/Export  ";
			break;
		case 1: // annotate tab
			title = "Annotate  ";
			break;
		case 2: // edit tab
			title = "Edit  ";
			break;
		case 3: // format tab
			title = "Format  ";
			break;
		default:
			title = "View  ";
			break;
		}

		if (this.getComplexScene() == null)
			title += "Structure";
		else
			title += "'" + getComplexScene().getName() + "'";

		if (this.getCurrentInputFile() != null) {
			title += "  In File  " + getCurrentInputFile().getName();
			String filePath = getCurrentInputFile().getAbsolutePath();
			filePath = filePath.substring(0, filePath.lastIndexOf(getCurrentInputFile().separatorChar));
			title += "  In Directory  " + filePath;
		}
		title += " :";

		genImgScrollPane.setBorder(new TitledBorder(border, title));
	}

	private ComplexScene2D complexScene = null;

	public void setComplexScene(ComplexScene2D complexScene) {
		this.complexScene = complexScene;

		/*
		 * DefaultMutableTreeNode node =
		 * (DefaultMutableTreeNode)this.getComplexScene().createComplexNodes();
		 */
		/*
		 * debug("TREE.depth, childcount, leafcount: " + node.getDepth() + " " +
		 * node.getChildCount() + " " + node.getLeafCount());
		 */

		/*
		 * if (pickStrandBt != null) { if (node.getLeafCount() > 1)
		 * pickStrandBt.setVisible(true); else pickStrandBt.setVisible(false); //
		 * this.updateUI(); }
		 */

		if (complexScene == null)
			return;
		/*
		 * if (!(complexScene.isJDK13_0 || complexScene.isJDK14_1_01))
		 * alert("WARNING: Untested java version: " + javaVersion);
		 */
	}

	public ComplexScene2D getComplexScene() {
		return (this.complexScene);
	}

	@Override
	public void runSetFromInputFile() throws Exception {
		if (this.getCurrentInputFile() == null) {
			alert("Currently no ComplexFile Set");
			return;
		}

		// debug("Opening: " + getCurrentInputFile().getPath());
		File
			currentInputFile = this.getCurrentInputFile();
		String
			currentInputFileName = currentInputFile.getName();
		if (currentInputFileName.endsWith(".ss")) {
			String fileName = getCurrentInputFile().getName();
			String ssName = fileName.substring(0, fileName.length() - 3);

			this.setComplexScene(new ComplexScene2D(ssName, (String) null));
			ComplexSSDataCollection2D newComplex = new ComplexSSDataCollection2D(ssName, (String) null);
			newComplex.setNewSSComplexElement(SSData2D.getSSDataFromSSFile(getCurrentInputFile()));

			this.getComplexScene().addItem(newComplex);
			this.resetFigureScale(1.0);
			this.getComplexScene().center();
			this.renderDrawObjectView();
			if (runTestOnly) {
				this.renderDrawObjectView();
			} else {
				this.centerScrollBars();
			}
		} else if (currentInputFileName.endsWith(".ps")) {
			String fileName = getCurrentInputFile().getName();
			String ssName = fileName.substring(0, fileName.length() - 3);

			this.setComplexScene(new ComplexScene2D(ssName, (String) null));
			ComplexSSDataCollection2D newComplex = new ComplexSSDataCollection2D(ssName, (String) null);
			try {
				newComplex.setNewSSComplexElement(SSData2D.getSSDataFromPSFile(getCurrentInputFile()));
			} catch (ComplexException ce) {
				if ((ce.getErrorMsg() != null) && (ce.getComment() != null)) {
					alert(ce.getErrorMsg() + "\n" + ce.getComment());
				} else if (ce.getErrorMsg() != null) {
					alert(ce.getErrorMsg());
				}
				return;
			}

			this.getComplexScene().addItem(newComplex);
			this.resetFigureScale(1.0);
			this.getComplexScene().center();
			this.renderDrawObjectView();
			if (runTestOnly) {
				this.renderDrawObjectView();
			} else {
				this.centerScrollBars();
			}
		} else if (currentInputFileName.endsWith(".xml")
				|| currentInputFileName.endsWith(".xrna")) {
			this.parseXrna();
		} else if (currentInputFileName.endsWith(".str")) {
			STRParser
				strParser = new STRParser(currentInputFile.getAbsolutePath());
			File
				temporaryXrna = /*new File("C:\\Users\\caede\\OneDrive\\Desktop\\Output.xrna");*/File.createTempFile("temp", ".xrna");
			temporaryXrna.deleteOnExit();
			// strParser.display();
			strParser.printToXRNAFile(temporaryXrna);
			ComplexSceneView.this.setCurrentInputFile(temporaryXrna);
			ComplexSceneView.this.parseXrna();
		} else if (currentInputFileName.endsWith(".svg")) {
			// new SVGToXRNAParser();
			SVGParser
				svgParser = new SVGParser(currentInputFile.getAbsolutePath());
			// svgParser.helixTexts = svgParser.inputHelixTexts();
			// svgParser.labelTexts = svgParser.inputLabelTexts();
			// svgParser.nucleotideTexts = svgParser.inputNucleotideTexts();
			// svgParser.labelLines = svgParser.inputLabelLines();
			// svgParser.nucleotideLines = svgParser.inputNucleotideLines();
			// svgParser.invertYCoordinates();
			// svgParser.display();

			File
				temporaryXrna = /*new File("C:\\Users\\caede\\OneDrive\\Desktop\\Output.xrna");*/File.createTempFile("temp", ".xrna");
			temporaryXrna.deleteOnExit();
			svgParser.printToXRNAFile(temporaryXrna);
			ComplexSceneView.this.setCurrentInputFile(temporaryXrna);
			ComplexSceneView.this.parseXrna();
		} else {
			alert("Currently can't work with file: " + getCurrentInputFile().getName());
			this.setCurrentInputFile(null);
			return;
		}

		resetPickTreePanels();
		if (ioTabPanel != null) {
			ioTabPanel.reset();
		}
		if (annotateTabPanel != null) {
			annotateTabPanel.reset();
		}
		if (editTabPanel != null) {
			editTabPanel.reset();
		}
		if (formatSStrTabPanel != null) {
			formatSStrTabPanel.reset();
		}
	}

	private void parseXrna() throws Exception {
		if (this.getCurrentInputFile().exists() && !this.getCurrentInputFile().canWrite()) {
			if (!(runTestOnly || suppressAlert)) {
				alert("Warning: " + this.getCurrentInputFile().getName()
						+ " is not writeable;\n If changing then need to set writeable");
			}
		}

		ComplexXMLParser complexXMLParser = new ComplexXMLParser();
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(this.getCurrentInputFile());
//			if ((genFileChooser == null) || (genFileChooser.getCurrentDirectory() == null)) {
//				fileReader = new FileReader(getCurrentInputFile().getName());
//			} else {
//				fileReader = new FileReader(genFileChooser.getCurrentDirectory() +
//					Character.toString(getCurrentInputFile().separatorChar) +
//					getCurrentInputFile().getName());
//			}
		} catch (java.io.FileNotFoundException e) {
			alert("Can't find file: " + getCurrentInputFile().getName());
			this.setCurrentInputFile(null);
			return;
		}

		complexXMLParser.parse(fileReader);
		fileReader.close();
		this.setComplexScene(complexXMLParser.getComplexScene());
		this.resetFigureScale(complexXMLParser.getFigureScale());
		if (!runTestOnly) {
			ioTabPanel.psScale_TF.setText("" + this.getComplexScene().getPSScale());

			// THIS renders so we get 2 renders upon startup, along with scaleCB.setSelected
			// above. Need to tie the two of them together. NEED to tie the 2 ways of
			// starting up together (cmd line arg of filename or button to read in).

			// THIS indirectly sets this.setLandscapeModeOn()
			ioTabPanel.landscapeMode_CB.setSelected(this.getComplexScene().getLandscapeMode());
			//
			// genImgScrollPane.setViewportView(this.getViewImgCanvas());
			// this.setLandscapeView(true, true);
		}
		if (runTestOnly) {
			this.renderDrawObjectView();
		} else {
			this.centerScrollBars();
		}
	}

	public void setCurrentInputFile(File currentInputFile) throws Exception {
		super.setCurrentInputFile(currentInputFile);
		// this.resetImgTitle();
	}

	/*
	 * To have every level expanded you could use -
	 * 
	 * public void expandPath(TreePath path) Ensures that the node identified by the
	 * specified path is expanded and viewable.
	 * 
	 * Parameters: path - the TreePath identifying a node of javax.swing.JTree. The
	 * trick is to compute the TreePaths. The general algorithm would be to compute
	 * the leaf nodes and force expansion of each of the leaf nodes unique parent.
	 * 
	 * If your tree is made out of a DefaultMutableTreeNodes then you can get the
	 * enumeration of all the nodes and thus compute the TreePaths easily.
	 */

	public boolean sceneContainsOneRNAStrand() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.getComplexScene().createComplexNodes();
		return (node.getLeafCount() == 1);
	}

	public void resetPickTreePanels() {
		// currently tells pickStrandTree that there is a new tree to build
		if (pickStrandPanel != null) {
			pickStrandFrame.setVisible(false);
			pickStrandPanel = null;
		}
	}

	public void runPDFPrinterJob(boolean showDialog, String jobName) throws Exception {
		/*
		 * SAVE if want to go back to using JPDFPrinter.jar from QOPPA PrinterJob pj =
		 * PDFPrinterJob.getPrinterJob(); PageFormat pf = pj.defaultPage();
		 * 
		 * if (this.getLandscapeModeOn()) { pf.setOrientation(PageFormat.LANDSCAPE); //
		 * pj.setPrintable((Printable)this, pf); } else { //
		 * pj.setPrintable((Printable)this); } Paper paper = new Paper(); double margin
		 * = 36.0; paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2,
		 * paper.getHeight() - margin * 2); pf.setPaper(paper);
		 * pj.setPrintable((Printable)this, pf);
		 * 
		 * if (showDialog) { if (pj.printDialog()) { try { pj.print(); } catch
		 * (PrinterException e) { throw e; //
		 * handleEx("PrinterException in ComplexSceneView.runComplexPrinterJob0:", e,
		 * 101); } catch (Exception e) { throw e; //
		 * handleEx("Exception in ComplexSceneView.runComplexPrinterJob0:", e, 101); } }
		 * return; } pj.setJobName(jobName); if (fileName != null) {
		 * ((PDFPrinterJob)pj).print(fileName); } else if (outStream != null) {
		 * ((PDFPrinterJob)pj).print(outStream); } else if (jobName != null) {
		 * pj.print(); }
		 */
	}

	public void runComplexPrinterJob(boolean showDialog, String jobName) {
		try {
			String cn = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(cn); // Use the native L&F
		} catch (Exception cnf) {
		}

		PrinterJob job = PrinterJob.getPrinterJob();

		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		PageFormat pf = job.pageDialog(aset);
		job.setPrintable(this, pf);
//    job.setPrintable(new Printable(), pf);
		boolean ok = job.printDialog(aset);
		if (ok) {
			try {
				job.print(aset);
			} catch (PrinterException ex) {
				/* The job did not successfully complete */
			}
		}

	}

	public void runComplexPDFJob(boolean showDialog, String jobName) {
		try {
			String cn = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(cn); // Use the native L&F
		} catch (Exception cnf) {
		}

		PrinterJob job = PrinterJob.getPrinterJob();

		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		PageFormat pf = job.pageDialog(aset);
		job.setPrintable(this, pf);
//    job.setPrintable(new Printable(), pf);
		boolean ok = job.printDialog(aset);
		if (ok) {
			try {
				job.print(aset);
			} catch (PrinterException ex) {
				/* The job did not successfully complete */
			}
		}

	}

	/****************** Printable Implementation *******************/

	public int print(Graphics g, PageFormat pf, int pageIndex) {

		if (pageIndex != 0)
			return (Printable.NO_SUCH_PAGE);

		Graphics2D g2 = (Graphics2D) g;
//	g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
//		RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		try {

//		 this.draw(g2, null);
//		// this.setFigureScale(this.getComplexScene().getPSScale());
			this.printPSFromJDK(g2, this.getComplexScene().getPSScale());

		} catch (Exception e) {
			handleEx("Exception in ComplexSceneView.print(): ", e, 1);
		}

		return PAGE_EXISTS;
	}

	/****************** End Printable Implementation *******************/

	public static JInternalFrame getBasicInternalFrame(int x, int y, int w, int h) {
		JInternalFrame tmpFrame = new JInternalFrame();
		tmpFrame.setClosable(true);
		tmpFrame.setIconifiable(true);
		tmpFrame.setMaximizable(true);
		tmpFrame.setResizable(true);
		tmpFrame.setBounds(x, y, w, h);

		return (tmpFrame);
	}

	public void updateBasicInternalFrame(String borderTitle, JComponent component, JInternalFrame jit) {
		this.updateBasicInternalFrame(this.complexTabType() + " Properties", borderTitle, component, jit);
	}

	public void updateBasicInternalFrame(String jitTitle, String borderTitle, JComponent component,
			JInternalFrame jit) {
		jit.setTitle(jitTitle);
		jit.getContentPane().removeAll();

		JScrollPane basicScrollPane = new JScrollPane(component, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		basicScrollPane.setBackground(guiBGColor);
		basicScrollPane.setBorder(new TitledBorder(beveledBorder, borderTitle));
		basicScrollPane.setVisible(true);

		jit.getContentPane().add(BorderLayout.CENTER, basicScrollPane);

		this.addInternalFrame(jit);

		component.setVisible(true);
		if (!(component instanceof JColorChooser))
			component.updateUI();

		jit.setVisible(true);
		jit.updateUI();
	}

	private SSData2D findNucSSData = null;

	public void setFindNucSSData(SSData2D findNucSSData) {
		this.findNucSSData = findNucSSData;
	}

	public SSData2D getFindNucSSData() {
		return (this.findNucSSData);
	}

	private void buildFindPanel() throws Exception {
		findPanel = new JPanel(new GridLayout(8, 1));
		findPanel.setFont(new Font("Helvetica", Font.PLAIN, 10));
		findPanel.setForeground(Color.black);
		findPanel.setBackground(guiBGColor);
		findPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
		findPanel.setAlignmentX(CENTER_ALIGNMENT);

		if (this.getComplexScene() != null) {
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) this.getComplexScene().createComplexNodes();

			if (rootNode.getLeafCount() < 2) {
				if (this.getFindNucSSData() == null)
					this.setFindNucSSData(getComplexScene().getFirstComplexSSData2D());
			} else {
				ButtonGroup sstrBtGroup = new ButtonGroup();
				for (Enumeration e = rootNode.depthFirstEnumeration(); e.hasMoreElements();) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
					if (!node.isLeaf())
						continue;

					ComplexScene parentComplexCollection = this.getComplexScene()
							.getChildByName(node.getParent().toString());
					if (parentComplexCollection == null)
						throw new Exception(
								"Error in " + "ComplexSceneView.getComplexTreePick(): " + "Invalid node: " + node);

					SSData2D sstr = (SSData2D) parentComplexCollection.getChildByName(node.toString());
					if (sstr == null)
						throw new Exception(
								"Error in " + "ComplexSceneView.getComplexTreePick(): " + "Invalid node: " + node);

					JRadioButton sstr_RB = new JRadioButton();
					sstr_RB.setSelected(false);
					sstr_RB.setActionCommand(node.getParent().toString() + "&" + sstr.getName());
					sstr_RB.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							try {
								SSData2D sstr = (SSData2D) getComplexScene().getChildByName(evt.getActionCommand(),
										'&');
								setFindNucSSData(sstr);
							} catch (Exception e) {
								handleException(e, 1);
							}
						}
					});
					if (this.getFindNucSSData() == null)
						this.setFindNucSSData(sstr);

					if ((this.getFindNucSSData() != null) && (this.getFindNucSSData().equals(sstr)))
						sstr_RB.setSelected(true);
					sstrBtGroup.add(sstr_RB);
					findPanel.add(editTabPanel.getNewViewButtonLeftPanel(sstr_RB, sstr.getName()));
				}
			}
		}

		ActionListener findNuc_AL = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					setWindowLocationFromFindString(findNuc_TF.getText().trim());
					viewRobot.mousePress(InputEvent.BUTTON1_MASK);
				} catch (Exception e) {
					handleEx("Exception in ComplexScene.findNucBt:", e, 98);
				}
			}
		};

		JPanel tfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
		tfPanel.setBackground(guiBGColor);
		tfPanel.setForeground(Color.black);

		JLabel label = new JLabel("nuc:", JLabel.LEFT);
		label.setFont(btFont);
		label.setForeground(Color.black);
		tfPanel.add(label);

		findNuc_TF = new JTextField(3);
		findNuc_TF.setText("1");
		findNuc_TF.addActionListener(findNuc_AL);
		tfPanel.add(findNuc_TF);

		JButton findNuc_Bt = editTabPanel.getNewViewImgPlainButton();
		tfPanel.add(findNuc_Bt);
		findNuc_Bt.addActionListener(findNuc_AL);

		findPanel.add(tfPanel);

		tfPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
		tfPanel.setBackground(guiBGColor);
		tfPanel.setForeground(Color.black);

		label = new JLabel("last nuc:", JLabel.LEFT);
		label.setFont(btFont);
		label.setForeground(Color.black);
		tfPanel.add(label);

		JButton findLastNuc_Bt = editTabPanel.getNewViewImgPlainButton();
		tfPanel.add(findLastNuc_Bt);
		findLastNuc_Bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					if (getFindNucSSData() == null)
						return;
					setWindowLocationFromFindString(getFindNucSSData().getNucCount());
					viewRobot.mousePress(InputEvent.BUTTON1_MASK);
				} catch (Exception e) {
					handleEx("Exception in ComplexSceneFormatSStrTab.findNucBt:", e, 98);
				}
			}
		});

		findPanel.add(tfPanel);
	}

	private void initFindFrame() throws Exception {
		if (findFrame == null) {
			findFrame = getBasicInternalFrame(110, 60, 240, 400);
			findFrame.addInternalFrameListener(new InternalFrameListener() {
				public void internalFrameClosing(InternalFrameEvent e) {
					findFrame.restoreSubcomponentFocus();
				}

				public void internalFrameClosed(InternalFrameEvent e) {
					try {
						InternalFrameListener[] listenerList = findFrame.getInternalFrameListeners();
						if (listenerList.length > 1)
							debug("Problem in ComplexSceneWorkTab.updateComplexPropertiesFrame().internalFrameClosed(): More than one listener");
						requestFocus();

					} catch (Exception evt) {
						handleEx("Exception in CopmlexSceneFormatSStrTab.findFrame.internalFrameClosing:", evt, 101);
					}
				}

				public void internalFrameOpened(InternalFrameEvent e) {
				}

				public void internalFrameIconified(InternalFrameEvent e) {
				}

				public void internalFrameDeiconified(InternalFrameEvent e) {
				}

				public void internalFrameActivated(InternalFrameEvent e) {
				}

				public void internalFrameDeactivated(InternalFrameEvent e) {
					requestFocus();
				}
			});
		}

		updateBasicInternalFrame("Find Structures", "enter nuc number in text field:", findPanel, findFrame);
		if (findFrame.isIcon())
			findFrame.setIcon(false);
	}

	public void setWindowLocationFromFindString(String nucString) {
		int nucID = 0;
		try {
			nucID = Integer.parseInt(nucString);
		} catch (NumberFormatException e) {
			alert("non integer input: " + nucString);
			return;
		}
		this.setWindowLocationFromFindString(nucID);
	}

	public void setWindowLocationFromFindString(int nucID) {
		if (this.getComplexScene() == null)
			return;
		SSData2D sstr = this.getFindNucSSData();
		if (sstr == null) {
			alert("NO rna strand picked");
			return;
		}

		Nuc2D nuc = sstr.getNuc2DAt(nucID);
		if (nuc == null) {
			alert("Non-existent nuc in SSData " + sstr.getName() + "at: " + nucID);
			return;
		}

		try {
			this.setWindowToDrawObjectCenter(sstr.getNuc2DAt(nucID));
			requestFocus();
		} catch (Exception e) {
			this.handleEx("Exception in ComplexSceneEdit.propertyChange:", e, 101);
		}
	}

	private String userConsoleString = null;

	public void setUserConsoleString(String userConsoleString) {
		this.userConsoleString = userConsoleString;
		propertyChangeListeners.firePropertyChange("CurrentUserConsoleString", null, userConsoleString);
	}

	public String getUserConsoleString() {
		return (this.userConsoleString);
	}

	private int currentComplexPickMode = 0;

	public void setCurrentComplexPickMode(int currentComplexPickMode) {
		int oldCurrentComplexPickMode = this.currentComplexPickMode;
		this.currentComplexPickMode = currentComplexPickMode;
		propertyChangeListeners.firePropertyChange("CurrentComplexPickMode", new Integer(oldCurrentComplexPickMode),
				new Integer(currentComplexPickMode));
	}

	public int getCurrentComplexPickMode() {
		return (this.currentComplexPickMode);
	}

	private String complexXMLRepositoryPath = ".";

	public void setComplexXMLRepositoryPath(String complexXMLRepositoryPath) {
		this.complexXMLRepositoryPath = complexXMLRepositoryPath;
	}

	public String getComplexXMLRepositoryPath() {
		return (this.complexXMLRepositoryPath);
	}

	/************** AdjustmentListener Implementaion ***************/

	public void adjustmentValueChanged(AdjustmentEvent e) {
		//
		// debug("\n*****");
		// debug("MADE IT ADJ: " + e.toString());
		// debug("ADJ: " + e.paramString());
		// debug("ADJ: " + e.getAdjustmentType() +
		// e.getValue());
		// debug("**********\n");
		//
		// debug("IMG SCROLLED");

		// debug("BEFORE ADJ");
		// debug("ADJ: " + e.getSource());

		updateImgWindowView();

		// debug("AFTER ADJ");
	}

	/************** End AdjustmentListener Implementaion ***************/

	/************** Private Classes *********************/

	private void setComplexCursors() {
		complexDefaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		complexWaitCursor = new Cursor(Cursor.WAIT_CURSOR);
		Toolkit toolKit = Toolkit.getDefaultToolkit();
		Dimension cursorDim = toolKit.getBestCursorSize(1, 1);
		int cursorW = (int) cursorDim.getWidth();
		int cursorH = (int) cursorDim.getHeight();
	}

	public void setLandscapeView(boolean landscapeMode, boolean redraw) throws Exception {
		if (landscapeMode) {
			drawObjectImgW = BASE_IMG_HEIGHT;
			drawObjectImgH = BASE_IMG_WIDTH;
		} else {
			drawObjectImgW = BASE_IMG_WIDTH;
			drawObjectImgH = BASE_IMG_HEIGHT;
		}

		setInitTransX(drawObjectImgW / 2);
		setInitTransY(drawObjectImgH / 2);

		if (redraw) {
			genImgScrollPane.setViewportView(this.getViewImgCanvas());
			renderDrawObjectView();
		}
	}

	public ComplexCollection getComplexTreePick() throws Exception {
		if (this.pickStrandTree == null)
			// then assume using default of entire scene
			return (this.getComplexScene());

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.pickStrandTree.getLastSelectedPathComponent();
		if (node == null)
			// then assume using default of entire scene
			return (this.getComplexScene());

		// debug("NODE: " + node.toString());

		ComplexCollection complexCollection = null;
		switch (node.getLevel()) {
		case 0: // top level
			complexCollection = this.getComplexScene();
			break;
		case 1: // ssdata collection level
			complexCollection = this.getComplexScene().getChildByName(node.toString());
			if (complexCollection == null)
				throw new Exception("Error in " + "ComplexSceneView.getComplexTreePick(): " + "Invalid node: " + node);
			break;
		case 2: // ssdata level
			TreeNode parentNode = node.getParent();
			// debug("2 LEVEL, parent: " + parentNode.toString());

			ComplexScene parentComplexCollection = this.getComplexScene().getChildByName(parentNode.toString());
			if (parentComplexCollection == null)
				throw new Exception("Error in " + "ComplexSceneView.getComplexTreePick(): " + "Invalid node: " + node);

			complexCollection = parentComplexCollection.getChildByName(node.toString());
			if (complexCollection == null)
				throw new Exception("Error in " + "ComplexSceneView.getComplexTreePick(): " + "Invalid node: " + node);
			break;
		default:
			throw new Exception(
					"Error in " + "ComplexSceneView.getComplexTreePick(): Invalid Level: " + node.getLevel());
		}

		// shouldn't reach here; complexCollection should be null
		return (complexCollection);
	}

	public void resetFigureScale(double val) {
		if (runTestOnly) {
			this.setFigureScale(val);
			if (this.getComplexScene() != null)
				this.getComplexScene().setScaleValue(val);
		} else {
			/*
			 * propertyChangeListeners.firePropertyChange("CurrentScaleValue", null, new
			 * Double(val));
			 */
			scaleTF.setText(Double.toString(val));
			scaleTF.postActionEvent();
		}
	}

	private double complexPickScaleVal = 1.0;

	public void setComplexPickScaleVal(double complexPickScaleVal) {
		this.complexPickScaleVal = complexPickScaleVal;
	}

	public double getComplexPickScaleVal() {
		return (this.complexPickScaleVal);
	}

	public void printConsole(String s) {
		userConsoleOut.setText(s);
	}

	public void printConsoleEdit(String s) {
		userConsoleOut.setText(s);
		userConsoleOut.requestFocus();
	}

	public void clearConsole() {
		userConsoleOut.setText("  ");
	}

	private int currentUndoLevel = 0;

	public void setCurrentUndoLevel(int currentUndoLevel) {
		if (currentUndoLevel < 0)
			this.currentUndoLevel = 0;
		else
			this.currentUndoLevel = currentUndoLevel;
	}

	public int getCurrentUndoLevel() {
		return (this.currentUndoLevel);
	}

	public int incCurrentUndoLevel() {
		this.setCurrentUndoLevel(this.getCurrentUndoLevel() + 1);

		return (this.getCurrentUndoLevel());
	}

	public int decCurrentUndoLevel() {
		this.setCurrentUndoLevel(this.getCurrentUndoLevel() - 1);

		return (this.getCurrentUndoLevel());
	}

	public void resetCurrentUndoLevel() {
		this.setCurrentUndoLevel(0);
	}

	public void splitOffSStr(NucCollection2D nucCollection) throws Exception {
		if (nucCollection == null)
			throw new ComplexException(ComplexDefines.COMPLEX_SCENE_SSTR_SPLIT_ERROR_MSG,
					ComplexDefines.COMPLEX_SCENE_SSTR_SPLIT_ERROR,
					"nucCollection null in ComplexSceneView.splitOffSStr()");

		// first make sure that all nucs in collection have same parent
		Vector delineators = nucCollection.getItemListDelineators();
		SSData2D sstr = null;
		for (int i = 0; i < delineators.size(); i += 2) {
			Nuc2D startNuc = (Nuc2D) delineators.elementAt(i);
			if (sstr == null)
				sstr = startNuc.getParentSSData2D();
			Nuc2D endNuc = (Nuc2D) delineators.elementAt(i + 1);
			Nuc2D nuc = startNuc;
			while (true) {
				if ((nuc != startNuc) && (nuc.getParentSSData2D() != sstr))
					throw new ComplexException(ComplexDefines.COMPLEX_SCENE_SSTR_SPLIT_ERROR_MSG,
							ComplexDefines.COMPLEX_SCENE_SSTR_SPLIT_ERROR,
							"nuc: " + nuc.getID() + " has different parent in ComplexSceneView.splitOffSStr()");

				if (nuc.getGroupName() == null)
					throw new ComplexException(ComplexDefines.COMPLEX_SCENE_SSTR_SPLIT_ERROR_MSG,
							ComplexDefines.COMPLEX_SCENE_SSTR_SPLIT_ERROR,
							"nuc: " + nuc.getID() + " in " + sstr.getName() + " " + "isn't in a named group");

				// for now no basepairing allowed across named groups
				if (nuc.isFivePrimeBasePair() && (((nuc.getBasePair().getGroupName() == null)
						|| !nuc.getBasePair().getGroupName().equals(nuc.getGroupName()))))
					throw new ComplexException(ComplexDefines.COMPLEX_SCENE_SSTR_SPLIT_ERROR_MSG,
							ComplexDefines.COMPLEX_SCENE_SSTR_SPLIT_ERROR,
							"nuc: " + nuc.getID() + " in " + sstr.getName() + " " + "basepairs outside of named group");

				if (nuc.equals(endNuc))
					break;
				nuc = nuc.nextNuc2D();
			}
		}

		// now split out new sstr
		SSData2D newSStr = new SSData2D(((Nuc2D) delineators.elementAt(0)).getGroupName());
		int newNucID = 1;
		for (int i = 0; i < delineators.size(); i += 2) {
			Nuc2D startNuc = (Nuc2D) delineators.elementAt(i);
			Nuc2D endNuc = (Nuc2D) delineators.elementAt(i + 1);
			Nuc2D nuc = startNuc;
			while (true) {
				Nuc2D newNuc = new Nuc2D(nuc);
				int bpIDShift = nuc.getID() - newNucID;
				newNuc.setID(newNucID++);
				if (newNuc.isBasePair()) {
					newNuc.setBasePairID(newNuc.getBasePair().getID() - bpIDShift);

					// NEED to deal with getBasePairSStrName()
					// NEED to determine if basepairing outside of group name.
					// if so then need to set up base pairing accordingly. Need
					// to maintain nuc numbering of base pair id outside of
					// group name.
				}
				newNuc.setParentCollection(newSStr);
				newSStr.addNuc(newNuc);
				if (nuc.equals(endNuc))
					break;
				nuc = nuc.nextNuc2D();
			}
		}
		Vector newDelineators = newSStr.getItemListDelineators();
		for (int i = 0; i < newDelineators.size(); i += 2) {
			Nuc2D startNuc = (Nuc2D) newDelineators.elementAt(i);
			Nuc2D endNuc = (Nuc2D) newDelineators.elementAt(i + 1);
			Nuc2D nuc = startNuc;
			while (true) {
				nuc.resetBasePair();
				if (nuc.equals(endNuc))
					break;
				nuc = nuc.nextNuc2D();
			}
		}

		nucCollection.delete();
		newSStr.setParentCollection(sstr.getParentCollection());
		((ComplexCollection) this.getComplexScene().getItemAt(0)).addItem(newSStr);
	}

	private void handleEx(String extraMsg, Throwable t, int id) {
		if (runTestOnly) {
			switch (id) {
			/*
			 * case 98 : debug("ComplexXMLParser Error:\n" + extraMsg + " " + t.toString() +
			 * "\n"); break;
			 */
			default:
				ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
				t.printStackTrace(new PrintStream(new DataOutputStream(excptArray)));
				debug(id + " " + extraMsg + t.toString() + (new String(excptArray.toByteArray())));
				break;
			}
		} else {
			switch (id) {
			/*
			 * case 98 : showUserMsg("ComplexXMLParser Error:\n" + extraMsg + " " +
			 * t.toString() + "\n"); break;
			 */
			default:
				ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
				t.printStackTrace(new PrintStream(new DataOutputStream(excptArray)));
				debug(id + " " + extraMsg + t.toString() + (new String(excptArray.toByteArray())));
				break;
			}
		}
		if (id >= 100)
			System.exit(0);
	}

	public void alert(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	private static void debug(String s) {
		System.err.println("ComplexSceneView-> " + s);
	}

}
