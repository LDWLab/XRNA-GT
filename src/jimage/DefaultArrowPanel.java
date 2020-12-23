package jimage;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import util.*;

public class
DefaultArrowPanel
extends DefaultDrawObjectPanel
{

// attribute bts
JButton angleDown_Bt = null;
JButton angleUp_Bt = null;
JButton baseWidthDown_Bt = null;
JButton baseWidthUp_Bt = null;
JButton heightDown_Bt = null;
JButton heightUp_Bt = null;
JButton scaleDown_Bt = null;
JButton scaleUp_Bt = null;
JButton tailLengthDown_Bt = null;
JButton tailLengthUp_Bt = null;
JButton tailWidthDown_Bt = null;
JButton tailWidthUp_Bt = null;
JButton baseIndentDown_Bt = null;
JButton baseIndentUp_Bt = null;

public
DefaultArrowPanel(DrawObjectLeafNode defaultDrwObj, Color bgColor,
	double scaleVal, JColorChooser colorChooser)
{
	super(defaultDrwObj, bgColor, scaleVal, colorChooser);
}

public void
preBuildGui()
{
	this.setDefaultArrow((DrawArrowObject)this.getDefaultDrawObject());
}

public void
buildGui()
{
	btPanel = new JPanel(new GridLayout(10, 1), true);
	btPanel.setBackground(guiBGColor);
	btPanel.setForeground(Color.black);

	super.buildGui();

	MouseAdapter arrowProperties_MA = new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new ArrowAttributeBtPressed(event.getComponent())).start();
			}
			catch (Exception e)
			{
				handleException(e, 1);
			}
		}

		public void
		mouseReleased(MouseEvent event)
		{
			mouseIsPressed = false;
		}
	};

	ActionListener angle_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultArrow().setAngle(
					Double.parseDouble(arrowAngle_TF.getText()));
				DefaultArrowPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + arrowAngle_TF.getText());
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
	JLabel label = new JLabel("angle");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	arrowAngle_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultArrow().getAngle(), 2), 4);
	arrowAngle_TF.setFont(smBtFont);
	tmpPanel.add(arrowAngle_TF);
	arrowAngle_TF.addActionListener(angle_AL);

	JButton applyAngleBt = this.getNewViewImgPlainButton();
	tmpPanel.add(applyAngleBt);
	applyAngleBt.addActionListener(angle_AL);

	angleDown_Bt = this.getNewViewImgDownButton();
	angleDown_Bt.setActionCommand("angle_down");
	tmpPanel.add(angleDown_Bt);
	angleDown_Bt.addMouseListener(arrowProperties_MA);

	angleUp_Bt = this.getNewViewImgUpButton();
	angleUp_Bt.setActionCommand("angle_up");
	tmpPanel.add(angleUp_Bt);
	angleUp_Bt.addMouseListener(arrowProperties_MA);

	btPanel.add(tmpPanel);

	ActionListener baseWidth_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultArrow().setBaseWidth(
					Double.parseDouble(arrowBaseWidth_TF.getText()));
				DefaultArrowPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid base value: " + arrowBaseWidth_TF.getText());
				return;
			}
			catch (Exception e)
			{
				handleException(e, 1);
			}
		}
	};

	tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	label = new JLabel("base width");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	arrowBaseWidth_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultArrow().getBaseWidth(), 2), 4);
	arrowBaseWidth_TF.setFont(smBtFont);
	tmpPanel.add(arrowBaseWidth_TF);
	arrowBaseWidth_TF.addActionListener(baseWidth_AL);

	JButton applyBaseWidthBt = this.getNewViewImgPlainButton();
	applyBaseWidthBt.setMargin(new Insets(0,0,0,0));
	applyBaseWidthBt.setForeground(Color.black);
	applyBaseWidthBt.setBackground(guiBGColor);
	tmpPanel.add(applyBaseWidthBt);
	applyBaseWidthBt.addActionListener(baseWidth_AL);

	baseWidthDown_Bt = this.getNewViewImgDownButton();
	baseWidthDown_Bt.setActionCommand("baseWidth_down");
	tmpPanel.add(baseWidthDown_Bt);
	baseWidthDown_Bt.addMouseListener(arrowProperties_MA);

	baseWidthUp_Bt = this.getNewViewImgUpButton();
	baseWidthUp_Bt.setActionCommand("baseWidth_up");
	tmpPanel.add(baseWidthUp_Bt);
	baseWidthUp_Bt.addMouseListener(arrowProperties_MA);
	btPanel.add(tmpPanel);

	ActionListener height_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultArrow().setHeight(
					Double.parseDouble(arrowHeight_TF.getText()));
				DefaultArrowPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid height value: " + arrowHeight_TF.getText());
				return;
			}
			catch (Exception e)
			{
				handleException(e, 1);
			}
		}
	};

	tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	label = new JLabel("height");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	arrowHeight_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultArrow().getHeight(), 2), 4);
	arrowHeight_TF.setFont(smBtFont);
	tmpPanel.add(arrowHeight_TF);
	arrowHeight_TF.addActionListener(height_AL);

	JButton applyHeightBt = this.getNewViewImgPlainButton();
	applyHeightBt.setMargin(new Insets(0,0,0,0));
	applyHeightBt.setForeground(Color.black);
	applyHeightBt.setBackground(guiBGColor);
	tmpPanel.add(applyHeightBt);
	applyHeightBt.addActionListener(height_AL);

	heightDown_Bt = this.getNewViewImgDownButton();
	heightDown_Bt.setActionCommand("height_down");
	tmpPanel.add(heightDown_Bt);
	heightDown_Bt.addMouseListener(arrowProperties_MA);

	heightUp_Bt = this.getNewViewImgUpButton();
	heightUp_Bt.setActionCommand("height_up");
	tmpPanel.add(heightUp_Bt);
	heightUp_Bt.addMouseListener(arrowProperties_MA);

	btPanel.add(tmpPanel);

	ActionListener scale_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultArrow().setScale(
					Double.parseDouble(arrowScale_TF.getText()));
				DefaultArrowPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid scale value: " + arrowScale_TF.getText());
				return;
			}
			catch (Exception e)
			{
				handleException(e, 1);
			}
		}
	};

	tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	label = new JLabel("scale");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	arrowScale_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultArrow().getScale(), 2), 4);
	arrowScale_TF.setFont(smBtFont);
	tmpPanel.add(arrowScale_TF);
	arrowScale_TF.addActionListener(scale_AL);

	JButton applyScaleBt = this.getNewViewImgPlainButton();
	applyScaleBt.setMargin(new Insets(0,0,0,0));
	applyScaleBt.setForeground(Color.black);
	applyScaleBt.setBackground(guiBGColor);
	tmpPanel.add(applyScaleBt);
	applyScaleBt.addActionListener(scale_AL);

	scaleDown_Bt = this.getNewViewImgDownButton();
	scaleDown_Bt.setActionCommand("scale_down");
	tmpPanel.add(scaleDown_Bt);
	scaleDown_Bt.addMouseListener(arrowProperties_MA);

	scaleUp_Bt = this.getNewViewImgUpButton();
	scaleUp_Bt.setActionCommand("scale_up");
	tmpPanel.add(scaleUp_Bt);
	scaleUp_Bt.addMouseListener(arrowProperties_MA);
	btPanel.add(tmpPanel);

	ActionListener tailLength_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultArrow().setTailLength(
					Double.parseDouble(arrowTailLength_TF.getText()));
				DefaultArrowPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid tailLength value: " + arrowTailLength_TF.getText());
				return;
			}
			catch (Exception e)
			{
				handleException(e, 1);
			}
		}
	};

	tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	label = new JLabel("tail length");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	arrowTailLength_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultArrow().getTailLength(), 2), 4);
	arrowTailLength_TF.setFont(smBtFont);
	tmpPanel.add(arrowTailLength_TF);
	arrowTailLength_TF.addActionListener(tailLength_AL);

	JButton applyTailLength_Bt = this.getNewViewImgPlainButton();
	applyTailLength_Bt.setMargin(new Insets(0,0,0,0));
	applyTailLength_Bt.setForeground(Color.black);
	applyTailLength_Bt.setBackground(guiBGColor);
	tmpPanel.add(applyTailLength_Bt);
	applyTailLength_Bt.addActionListener(tailLength_AL);

	tailLengthDown_Bt = this.getNewViewImgDownButton();
	tailLengthDown_Bt.setActionCommand("tailLength_down");
	tmpPanel.add(tailLengthDown_Bt);
	tailLengthDown_Bt.addMouseListener(arrowProperties_MA);

	tailLengthUp_Bt = this.getNewViewImgUpButton();
	tailLengthUp_Bt.setActionCommand("tailLength_up");
	tmpPanel.add(tailLengthUp_Bt);
	tailLengthUp_Bt.addMouseListener(arrowProperties_MA);
	btPanel.add(tmpPanel);


	/*********************************************************/

	// NEED TO CHG TO tailWIdth:
	ActionListener tailWidth_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultArrow().setTailWidth(
					Double.parseDouble(arrowTailWidth_TF.getText()));
				DefaultArrowPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid tailWidth value: " + arrowTailWidth_TF.getText());
				return;
			}
			catch (Exception e)
			{
				handleException(e, 1);
			}
		}
	};

	tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	label = new JLabel("tail width");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	arrowTailWidth_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultArrow().getTailWidth(), 2), 4);
	arrowTailWidth_TF.setFont(smBtFont);
	tmpPanel.add(arrowTailWidth_TF);
	arrowTailWidth_TF.addActionListener(tailWidth_AL);

	JButton applyTailWidth_Bt = this.getNewViewImgPlainButton();
	applyTailWidth_Bt.setMargin(new Insets(0,0,0,0));
	applyTailWidth_Bt.setForeground(Color.black);
	applyTailWidth_Bt.setBackground(guiBGColor);
	tmpPanel.add(applyTailWidth_Bt);
	applyTailWidth_Bt.addActionListener(tailWidth_AL);

	tailWidthDown_Bt = this.getNewViewImgDownButton();
	tailWidthDown_Bt.setActionCommand("tailWidth_down");
	tmpPanel.add(tailWidthDown_Bt);
	tailWidthDown_Bt.addMouseListener(arrowProperties_MA);

	tailWidthUp_Bt = this.getNewViewImgUpButton();
	tailWidthUp_Bt.setActionCommand("tailWidth_up");
	tmpPanel.add(tailWidthUp_Bt);
	tailWidthUp_Bt.addMouseListener(arrowProperties_MA);
	btPanel.add(tmpPanel);

	/*********************************************************/

	ActionListener baseIndent_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultArrow().setBaseIndent(
					Double.parseDouble(arrowBaseIndent_TF.getText()));
				DefaultArrowPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid baseIndent value: " + arrowBaseIndent_TF.getText());
				return;
			}
			catch (Exception e)
			{
				handleException(e, 1);
			}
		}
	};

	tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	label = new JLabel("base indent");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	arrowBaseIndent_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultArrow().getBaseIndent(), 2), 4);
	arrowBaseIndent_TF.setFont(smBtFont);
	tmpPanel.add(arrowBaseIndent_TF);
	arrowBaseIndent_TF.addActionListener(baseIndent_AL);

	JButton applyBaseIndent_Bt = this.getNewViewImgPlainButton();
	applyBaseIndent_Bt.setMargin(new Insets(0,0,0,0));
	applyBaseIndent_Bt.setForeground(Color.black);
	applyBaseIndent_Bt.setBackground(guiBGColor);
	tmpPanel.add(applyBaseIndent_Bt);
	applyBaseIndent_Bt.addActionListener(baseIndent_AL);

	baseIndentDown_Bt = this.getNewViewImgDownButton();
	baseIndentDown_Bt.setActionCommand("baseIndent_down");
	tmpPanel.add(baseIndentDown_Bt);
	baseIndentDown_Bt.addMouseListener(arrowProperties_MA);

	baseIndentUp_Bt = this.getNewViewImgUpButton();
	baseIndentUp_Bt.setActionCommand("baseIndent_up");
	tmpPanel.add(baseIndentUp_Bt);
	baseIndentUp_Bt.addMouseListener(arrowProperties_MA);

	btPanel.add(tmpPanel);
}

class ArrowAttributeBtPressed
extends Thread
{
private JButton bt = null;
private double inc = 0.2;

ArrowAttributeBtPressed(Component btPressed)
{
	mouseIsPressed = true;
	this.bt = (JButton)btPressed;
}

public void
run()
{
	try
	{
		setNewAttribute();
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
			setNewAttribute();
		}
	}
	catch (Exception e)
	{
		handleException(e, 1);
	}
}

private void
setNewAttribute()
throws Exception
{
	if (bt.getActionCommand().equals("angle_down"))
	{
		double currentAngle = getDefaultArrow().getAngle();
		if (currentAngle == 360.0)
			currentAngle = 0.0;
		if (currentAngle == 0.0)
			return;
		currentAngle -= inc;
		getDefaultArrow().setAngle(currentAngle);
		arrowAngle_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getAngle(), 2));
	}
	else if (bt.getActionCommand().equals("angle_up"))
	{
		double currentAngle = getDefaultArrow().getAngle();
		if (currentAngle == 360.0)
			currentAngle = 0.0;
			currentAngle += inc;
		getDefaultArrow().setAngle(currentAngle);
		arrowAngle_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getAngle(), 2));
	}
	else if (bt.getActionCommand().equals("baseWidth_down"))
	{
		double currentBaseWidth = getDefaultArrow().getBaseWidth();
		if (currentBaseWidth <= 2.0)
			return;
		getDefaultArrow().setBaseWidth(currentBaseWidth - inc);
		arrowBaseWidth_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getBaseWidth(), 2));
	}
	else if (bt.getActionCommand().equals("baseWidth_up"))
	{
		double currentBaseWidth = getDefaultArrow().getBaseWidth();
		if (currentBaseWidth >= 200.0)
			return;
		getDefaultArrow().setBaseWidth(currentBaseWidth + inc);
		arrowBaseWidth_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getBaseWidth(), 2));
	}
	else if (bt.getActionCommand().equals("height_down"))
	{
		double currentHeight = getDefaultArrow().getHeight();
		if (currentHeight <= 2.0)
			return;
		getDefaultArrow().setHeight(currentHeight - inc);
		arrowHeight_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getHeight(), 2));
	}
	else if (bt.getActionCommand().equals("height_up"))
	{
		double currentHeight = getDefaultArrow().getHeight();
		if (currentHeight >= 200.0)
			return;
		getDefaultArrow().setHeight(currentHeight + inc);
		arrowHeight_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getHeight(), 2));
	}
	else if (bt.getActionCommand().equals("scale_down"))
	{
		double currentScale = getDefaultArrow().getScale();
		if (currentScale <= 0.2)
			return;
		getDefaultArrow().setScale(currentScale - inc);
		arrowScale_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getScale(), 2));
	}
	else if (bt.getActionCommand().equals("scale_up"))
	{
		double currentScale = getDefaultArrow().getScale();
		if (currentScale >= 10.0)
			return;
		getDefaultArrow().setScale(currentScale + inc);
		arrowScale_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getScale(), 2));
	}
	else if (bt.getActionCommand().equals("tailLength_down"))
	{
		double currentTailLength = getDefaultArrow().getTailLength();
		if (currentTailLength <= 0.2)
			return;
		getDefaultArrow().setTailLength(currentTailLength - inc);
		arrowTailLength_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getTailLength(), 2));
	}
	else if (bt.getActionCommand().equals("tailLength_up"))
	{
		double currentTailLength = getDefaultArrow().getTailLength();
		if (currentTailLength >= 10.0)
			return;
		getDefaultArrow().setTailLength(currentTailLength + inc);
		arrowTailLength_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getTailLength(), 2));
	}
	else if (bt.getActionCommand().equals("tailWidth_down"))
	{
		double currentTailWidth = getDefaultArrow().getTailWidth();
		if (currentTailWidth <= 0.2)
			return;
		getDefaultArrow().setTailWidth(currentTailWidth - inc);
		arrowTailWidth_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getTailWidth(), 2));
	}
	else if (bt.getActionCommand().equals("tailWidth_up"))
	{
		double currentTailWidth = getDefaultArrow().getTailWidth();
		if (currentTailWidth >= 10.0)
			return;
		getDefaultArrow().setTailWidth(currentTailWidth + inc);
		arrowTailWidth_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getTailWidth(), 2));
	}
	else if (bt.getActionCommand().equals("baseIndent_down"))
	{
		DrawArrowObject arrowObj = getDefaultArrow();
		double currentBaseIndent = arrowObj.getBaseIndent();
		double height = arrowObj.getHeight();

		if (currentBaseIndent < 0.0)
		{
			arrowObj.setBaseIndent(0.0);
			arrowBaseIndent_TF.setText(StringUtil.roundStrVal(
				arrowObj.getBaseIndent(), 2));
			arrowObj.reset();
		}
		if (currentBaseIndent <= 0.0)
			return;
		currentBaseIndent = arrowObj.getBaseIndent();
		arrowObj.setBaseIndent(currentBaseIndent - inc);
		arrowBaseIndent_TF.setText(StringUtil.roundStrVal(
			arrowObj.getBaseIndent(), 2));
	}
	else if (bt.getActionCommand().equals("baseIndent_up"))
	{
		DrawArrowObject arrowObj = getDefaultArrow();
		double currentBaseIndent = arrowObj.getBaseIndent();
		double height = arrowObj.getHeight();
		if (currentBaseIndent < 0.0)
		{
			arrowObj.setBaseIndent(0.0);
			arrowBaseIndent_TF.setText(StringUtil.roundStrVal(
				arrowObj.getBaseIndent(), 2));
			arrowObj.reset();
		}
		if (currentBaseIndent > height - 4.0 - 2.0*arrowObj.getLineWidth())
		{
			arrowObj.setBaseIndent(height - 4.0 - 2.0*arrowObj.getLineWidth());
			arrowBaseIndent_TF.setText(StringUtil.roundStrVal(
				arrowObj.getBaseIndent(), 2));
			arrowObj.reset();
			return;
		}
		currentBaseIndent = arrowObj.getBaseIndent();
		arrowObj.setBaseIndent(currentBaseIndent + inc);
		arrowBaseIndent_TF.setText(StringUtil.roundStrVal(
			arrowObj.getBaseIndent(), 2));
	}
	getDefaultArrow().reset();
	DefaultArrowPanel.this.repaint();
}
}

class ArrowAngleBtPressed
extends Thread
{
private double angleInc = 0.2;
private boolean angleDirection = false;

ArrowAngleBtPressed(boolean direction)
{
	mouseIsPressed = true;
	angleDirection = direction;
}

public void
run()
{
	setNewAngle();
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
		setNewAngle();
	}
}

private void
setNewAngle()
{
	double currentAngle = getDefaultArrow().getAngle();

	if (currentAngle == 360.0)
		currentAngle = 0.0;
	if ((!angleDirection) && (currentAngle == 0.0))
		return;
	if (angleDirection)
		currentAngle += angleInc;
	else
		currentAngle -= angleInc;

	getDefaultArrow().setAngle(currentAngle);
	arrowAngle_TF.setText(StringUtil.roundStrVal(
		getDefaultArrow().getAngle(), 2));
	DefaultArrowPanel.this.repaint();
}
} // end inner class

class ArrowHeightBtPressed
extends Thread
{
private double heightInc = 0.2;
private boolean heightDirection = false;

ArrowHeightBtPressed(boolean direction)
{
	mouseIsPressed = true;
	heightDirection = direction;
}

public void
run()
{
	try
	{
		setNewHeight();
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
			setNewHeight();
		}
	}
	catch (Exception e)
	{
		handleException(e, 1);
	}
}

private void
setNewHeight()
throws Exception
{
	double currentHeight = getDefaultArrow().getHeight();

	if (heightDirection && (currentHeight >= 200.0))
		return;
	else if ((!heightDirection) && (currentHeight <= 2.0))
		return;

	if (heightDirection)
		getDefaultArrow().setHeight(currentHeight + heightInc);
	else
		getDefaultArrow().setHeight(currentHeight - heightInc);
	arrowHeight_TF.setText(StringUtil.roundStrVal(
		getDefaultArrow().getHeight(), 2));
	getDefaultArrow().reset();

	DefaultArrowPanel.this.repaint();
}
} // end inner class

class ArrowBaseWidthBtPressed
extends Thread
{
private double baseWidthInc = 0.2;
private boolean baseWidthDirection = false;

ArrowBaseWidthBtPressed(boolean direction)
{
	mouseIsPressed = true;
	baseWidthDirection = direction;
}

public void
run()
{
	try
	{
		setNewBaseWidth();
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
			setNewBaseWidth();
		}
	}
	catch (Exception e)
	{
		handleException(e, 1);
	}
}

private void
setNewBaseWidth()
throws Exception
{
	double currentBaseWidth = getDefaultArrow().getBaseWidth();

	if (baseWidthDirection && (currentBaseWidth >= 200.0))
		return;
	else if ((!baseWidthDirection) && (currentBaseWidth <= 2.0))
		return;

	if (baseWidthDirection)
		getDefaultArrow().setBaseWidth(currentBaseWidth + baseWidthInc);
	else
		getDefaultArrow().setBaseWidth(currentBaseWidth - baseWidthInc);
	arrowBaseWidth_TF.setText(StringUtil.roundStrVal(
		getDefaultArrow().getBaseWidth(), 2));
	getDefaultArrow().reset();

	DefaultArrowPanel.this.repaint();
}
} // end inner class

class ArrowScaleBtPressed
extends Thread
{
private double scaleInc = 0.02;
private boolean scaleDirection = false;

ArrowScaleBtPressed(boolean direction)
{
	mouseIsPressed = true;
	scaleDirection = direction;
}

public void
run()
{
	try
	{
		setNewScale();
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
			setNewScale();
		}
	}
	catch (Exception e)
	{
		handleException(e, 1);
	}
}

private void
setNewScale()
throws Exception
{
	double currentScale = getDefaultArrow().getScale();

	if (scaleDirection && (currentScale >= 10.0))
		return;
	else if ((!scaleDirection) && (currentScale <= 0.2))
		return;

	if (scaleDirection)
		getDefaultArrow().setScale(currentScale + scaleInc);
	else
		getDefaultArrow().setScale(currentScale - scaleInc);
	arrowScale_TF.setText(StringUtil.roundStrVal(
		getDefaultArrow().getScale(), 2));
	getDefaultArrow().reset();

	DefaultArrowPanel.this.repaint();
}
} // end inner class

class ArrowTailLengthBtPressed
extends Thread
{
private double tailLengthInc = 0.02;
private boolean tailLengthDirection = false;

ArrowTailLengthBtPressed(boolean direction)
{
	mouseIsPressed = true;
	tailLengthDirection = direction;
}

public void
run()
{
	try
	{
		setNewTailLength();
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
			setNewTailLength();
		}
	}
	catch (Exception e)
	{
		handleException(e, 1);
	}
}

private void
setNewTailLength()
throws Exception
{
	double currentTailLength = getDefaultArrow().getTailLength();

	if (tailLengthDirection && (currentTailLength >= 10.0))
		return;
	else if ((!tailLengthDirection) && (currentTailLength <= 0.2))
		return;

	if (tailLengthDirection)
		getDefaultArrow().setTailLength(currentTailLength + tailLengthInc);
	else
		getDefaultArrow().setTailLength(currentTailLength - tailLengthInc);
	arrowTailLength_TF.setText(StringUtil.roundStrVal(
		getDefaultArrow().getTailLength(), 2));
	getDefaultArrow().reset();

	DefaultArrowPanel.this.repaint();
}
} // end inner class

class ArrowTailWidthBtPressed
extends Thread
{
private double tailWidthInc = 0.02;
private boolean tailWidthDirection = false;

ArrowTailWidthBtPressed(boolean direction)
{
	mouseIsPressed = true;
	tailWidthDirection = direction;
}

public void
run()
{
	try
	{
		setNewTailWidth();
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
			setNewTailWidth();
		}
	}
	catch (Exception e)
	{
		handleException(e, 1);
	}
}

private void
setNewTailWidth()
throws Exception
{
	double currentTailWidth = getDefaultArrow().getTailWidth();

	if (tailWidthDirection && (currentTailWidth >= 10.0))
		return;
	else if ((!tailWidthDirection) && (currentTailWidth <= 0.2))
		return;

	if (tailWidthDirection)
		getDefaultArrow().setTailWidth(currentTailWidth + tailWidthInc);
	else
		getDefaultArrow().setTailWidth(currentTailWidth - tailWidthInc);
	arrowTailWidth_TF.setText(StringUtil.roundStrVal(
		getDefaultArrow().getTailWidth(), 2));
	getDefaultArrow().reset();

	DefaultArrowPanel.this.repaint();
}
}

class ArrowBaseIndentBtPressed
extends Thread
{
private double baseIndentInc = 0.02;
private boolean baseIndentDirection = false;

ArrowBaseIndentBtPressed(boolean direction)
{
	mouseIsPressed = true;
	baseIndentDirection = direction;
}

public void
run()
{
	try
	{
		setNewBaseIndent();
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
			setNewBaseIndent();
		}
	}
	catch (Exception e)
	{
		handleException(e, 1);
	}
}

private void
setNewBaseIndent()
throws Exception
{
	DrawArrowObject arrowObj = getDefaultArrow();
	double currentBaseIndent = arrowObj.getBaseIndent();
	double height = arrowObj.getHeight();

	if (currentBaseIndent < 0.0)
	{
		arrowObj.setBaseIndent(0.0);
		arrowBaseIndent_TF.setText(StringUtil.roundStrVal(
			arrowObj.getBaseIndent(), 2));
		arrowObj.reset();
	}
	if (baseIndentDirection && (currentBaseIndent > height - 4.0 - 2.0*arrowObj.getLineWidth()))
	{
		arrowObj.setBaseIndent(height - 4.0 - 2.0*arrowObj.getLineWidth());
		arrowBaseIndent_TF.setText(StringUtil.roundStrVal(
			arrowObj.getBaseIndent(), 2));
		arrowObj.reset();
		return;
	}
	else if ((!baseIndentDirection) && (currentBaseIndent <= 0.0))
		return;
	currentBaseIndent = arrowObj.getBaseIndent();

	if (baseIndentDirection)
		arrowObj.setBaseIndent(currentBaseIndent + baseIndentInc);
	else
		arrowObj.setBaseIndent(currentBaseIndent - baseIndentInc);

	arrowBaseIndent_TF.setText(StringUtil.roundStrVal(
		arrowObj.getBaseIndent(), 2));
	arrowObj.reset();

	DefaultArrowPanel.this.repaint();
}
} // end inner class

public void
updateGui()
{
	super.updateGui();
	if (arrowHeight_TF != null)
		arrowHeight_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getHeight(), 2));
	if (arrowBaseWidth_TF != null)
		arrowBaseWidth_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getBaseWidth(), 2));
	if (arrowAngle_TF != null)
		arrowAngle_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getAngle(), 2));
	if (arrowScale_TF != null)
		arrowScale_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getScale(), 2));
	if (arrowTailLength_TF != null)
		arrowTailLength_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getTailLength(), 2));
	if (arrowBaseIndent_TF != null)
		arrowBaseIndent_TF.setText(StringUtil.roundStrVal(
			getDefaultArrow().getBaseIndent(), 2));
}

private DrawArrowObject defaultArrow = null;

public void
setDefaultArrow(DrawArrowObject defaultArrow)
{
    this.defaultArrow = defaultArrow;
	this.setDefaultDrawObject(this.getDefaultArrow());
}

public DrawArrowObject
getDefaultArrow()
{
	if (this.defaultArrow == null)
		this.defaultArrow = (DrawArrowObject)this.getDefaultDrawObject();
    return (this.defaultArrow);
}

private static void
debug(String s)
{
	System.out.println("DefaultArrowPanel-> " + s);
}

}
