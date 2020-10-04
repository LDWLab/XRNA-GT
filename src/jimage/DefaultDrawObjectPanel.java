package jimage;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import util.*;

public class
DefaultDrawObjectPanel
extends JPanel
{

JPanel drwObjPanel = null;
JPanel btPanel = null;
JColorChooser colorChooser = null;

Color guiBGColor = Color.white;

public Font btFont = new Font("Dialog", Font.BOLD, 12);
public Font headerFont = new Font("Dialog", Font.BOLD, 14);
public Font smBtFont = new Font("Dialog", Font.BOLD, 10);
public Font labelFont = new Font("Dialog", Font.BOLD, 11);
public boolean mouseIsPressed = false;

// need to be defined here;for some reason inner class in
// children only will see null if declared in child

public JTextField parallelogramAngle1_TF = null;
public JTextField parallelogramAngle2_TF = null;
public JTextField parallelogramSide1_TF = null;
public JTextField parallelogramSide2_TF = null;

public JTextField circleRadius_TF = null;
public JTextField circleStartAngle_TF = null;
public JTextField circleAngleExtent_TF = null;

public JTextField lineWidth_TF = null;
public JTextField lineLength_TF = null;
public JTextField lineAngle_TF = null;

public JTextField arrowHeight_TF = null;
public JTextField arrowBaseWidth_TF = null;
public JTextField arrowAngle_TF = null;
public JTextField arrowScale_TF = null;
public JTextField arrowTailLength_TF = null;
public JTextField arrowTailWidth_TF = null;
public JTextField arrowBaseIndent_TF = null;

public JTextField triangleHeight_TF = null;
public JTextField triangleBaseWidth_TF = null;
public JTextField triangleAngle_TF = null;
public JTextField triangleScale_TF = null;

public JRadioButton fill_RB = null;
public JRadioButton useCurrColor_RB = null;
/*
public JLabel parentScaleValLabel = null;
*/

public
DefaultDrawObjectPanel(DrawObjectLeafNode defaultDrwObj, Color guiBGColor, double scaleVal, JColorChooser colorChooser)
{
	this.guiBGColor = guiBGColor;
	this.colorChooser = colorChooser;
	this.setParentViewScaleVal(scaleVal);
	this.setDefaultDrawObject(defaultDrwObj);
	this.preBuildGui();
	this.buildGui();
	this.postBuildGui();
}

public void
preBuildGui()
{
}

public void
buildGui()
{
	this.setLayout(new BorderLayout());
	this.setBackground(guiBGColor);
	this.setForeground(Color.black);
	this.setFont(headerFont);
	this.setDoubleBuffered(true);

	/*
	parentScaleValLabel = new JLabel("Figure Scale: " + this.getParentViewScaleVal());
	parentScaleValLabel.setFont(btFont);
	parentScaleValLabel.setForeground(Color.black);
	parentScaleValLabel.setBackground(guiBGColor);
	btPanel.add(parentScaleValLabel);
	*/

	if (!(this instanceof DefaultLinePanel))
	{
		fill_RB = new JRadioButton();
		fill_RB.setSelected(false);
		fill_RB.setText("fill");
		fill_RB.setFont(btFont);
		fill_RB.setForeground(Color.black);
		fill_RB.setBackground(guiBGColor);
		btPanel.add(fill_RB);
		fill_RB.addActionListener(new ActionListener()
		{
			public void
			actionPerformed(ActionEvent evt)
			{
				try
				{
					if (getDefaultDrawObject() == null)
						return;
					getDefaultDrawObject().setIsOpen(!fill_RB.isSelected());
					DefaultDrawObjectPanel.this.repaint();
				}
				catch (Exception e)
				{
					handleException(e, 1);
				}
			}
		});
	}

	useCurrColor_RB = new JRadioButton();
	useCurrColor_RB.setSelected(false);
	useCurrColor_RB.setText("Use Current Color");
	useCurrColor_RB.setFont(btFont);
	useCurrColor_RB.setForeground(Color.black);
	useCurrColor_RB.setBackground(guiBGColor);
	btPanel.add(useCurrColor_RB);
	useCurrColor_RB.addActionListener(new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				if (getDefaultDrawObject() == null)
					return;
				if (useCurrColor_RB.isSelected())
					getDefaultDrawObject().setColor(colorChooser.getColor());
				else
					getDefaultDrawObject().setColor(Color.black);
				DefaultDrawObjectPanel.this.repaint();
			}
			catch (Exception e)
			{
				handleException(e, 1);
			}
		}
	});

	ActionListener lineWidth_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultDrawObject().setLineWidth(
					Double.parseDouble(lineWidth_TF.getText()));
				DefaultDrawObjectPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid lineWidth value: " + lineWidth_TF.getText());
				return;
			}
			catch (Exception e)
			{
				handleException(e, 1);
			}
		}
	};

	JPanel tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	JLabel label = new JLabel("line width:");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	lineWidth_TF = new JTextField(
		StringUtil.roundStrVal(this.getDefaultDrawObject().getLineWidth(), 2), 4);

	lineWidth_TF.setFont(smBtFont);
	tmpPanel.add(lineWidth_TF);
	lineWidth_TF.addActionListener(lineWidth_AL);

	JButton lineWidth_Bt = this.getNewViewImgPlainButton();
	lineWidth_Bt.addActionListener(lineWidth_AL);
	tmpPanel.add(lineWidth_Bt);

	btPanel.add(tmpPanel);

	drwObjPanel = new DefaultDrwObjCanvas();
	this.add(BorderLayout.CENTER, drwObjPanel);
	drwObjPanel.setVisible(true);
}

public void
postBuildGui()
{
	this.add(BorderLayout.WEST, btPanel);
}

public void
updateGui()
{
	if (lineWidth_TF != null)
		lineWidth_TF.setText(StringUtil.roundStrVal(this.getDefaultDrawObject().getLineWidth(), 2));
	if (fill_RB != null)
		fill_RB.setSelected(!this.getDefaultDrawObject().getIsOpen());
}

private DrawObjectLeafNode defaultDrawObject = null;

public void
setDefaultDrawObject(DrawObjectLeafNode defaultDrawObject)
{
    this.defaultDrawObject = defaultDrawObject;
}

public DrawObjectLeafNode
getDefaultDrawObject()
{
    return (this.defaultDrawObject);
}

private double parentViewScaleVal = 1.0;

public void
setParentViewScaleVal(double parentViewScaleVal)
{
    this.parentViewScaleVal = parentViewScaleVal;
	/*
	if (parentScaleValLabel != null)
		parentScaleValLabel.setText("Figure Scale: " + this.getParentViewScaleVal());
	*/
}

public double
getParentViewScaleVal()
{
    return (this.parentViewScaleVal);
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

private class
DefaultDrwObjCanvas
extends JPanel
{
public
DefaultDrwObjCanvas()
{
	this.setBackground(Color.white);
	this.setForeground(Color.black);
	this.setBorder(new BevelBorder(BevelBorder.RAISED));
	this.setDoubleBuffered(true);
}

public void
paint(Graphics g)
{
	super.paint(g);

	if (getDefaultDrawObject() == null)
		return;

	Graphics2D g2 = (Graphics2D)g;
	g2.setColor(Color.white);
	g2.fillRect(0, 0, this.getWidth(), this.getHeight());
	g2.setColor(Color.black);
	/*
	g2.drawLine(0, 0, this.getWidth(), this.getHeight());
	g2.drawLine(this.getWidth(), 0, 0, this.getHeight());
	*/

	try
	{
		getDefaultDrawObject().setX(0.0);
		getDefaultDrawObject().setY(0.0);
		getDefaultDrawObject().update(g2);
		g2.translate((double)(this.getWidth()/2),
			(double)(this.getHeight()/2));
		g2.scale(getParentViewScaleVal(), getParentViewScaleVal());
		getDefaultDrawObject().draw(g2, null);
		// debug("X,Y: " + getDefaultDrawObject().getX() + " " + getDefaultDrawObject().getY());
	}
	catch (Exception e)
	{
		handleException("Exception in DefaultDrawObjectPanel.paint():", e, 101);
	}

}

} // end inner class

public static void
handleException(String extraMsg, Throwable t, int id)
{
	switch(id)
	{
	  case 98 :
	  	debug("ComplexXMLParser Error:\n" + extraMsg + t.toString() + "\n");
		break;
	  default :
		ByteArrayOutputStream excptArray = new ByteArrayOutputStream();
		t.printStackTrace(new PrintStream(
			new DataOutputStream(excptArray)));
		debug(id + " " + extraMsg + t.toString() +
			(new String(excptArray.toByteArray())));
		break;
	}
	if (id >= 100)
		System.exit(0);
}

public static void
handleException(Throwable t, int id)
{
	handleException(" ", t, id);
}

public void
alert(String msg)
{
	JOptionPane.showMessageDialog(this, msg);
}

private static void
debug(String s)
{
	System.out.println("DefaultDrawObjectPanel-> " + s);
}

}
