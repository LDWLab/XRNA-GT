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
DefaultTrianglePanel
extends DefaultDrawObjectPanel
{

JButton angleDownBt = null;
JButton angleUpBt = null;
JButton baseDownBt = null;
JButton baseUpBt = null;
JButton heightDownBt = null;
JButton heightUpBt = null;
JButton scaleDownBt = null;
JButton scaleUpBt = null;

public
DefaultTrianglePanel(DrawObjectLeafNode defaultDrwObj, Color bgColor,
	double scaleVal, JColorChooser colorChooser)
{
	super(defaultDrwObj, bgColor, scaleVal, colorChooser);
}

public void
preBuildGui()
{
	this.setDefaultTriangle((DrawTriangleObject)this.getDefaultDrawObject());
}

public void
buildGui()
{
	btPanel = new JPanel(new GridLayout(8, 1), true);
	btPanel.setBackground(guiBGColor);
	btPanel.setForeground(Color.black);

	super.buildGui();

	ActionListener angle_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultTriangle().setAngle(
					Double.parseDouble(triangleAngle_TF.getText()));
				DefaultTrianglePanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + triangleAngle_TF.getText());
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
	triangleAngle_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultTriangle().getAngle(), 2), 4);
	triangleAngle_TF.setFont(smBtFont);
	tmpPanel.add(triangleAngle_TF);
	triangleAngle_TF.addActionListener(angle_AL);

	JButton applyAngleBt = this.getNewViewImgPlainButton();
	tmpPanel.add(applyAngleBt);
	applyAngleBt.addActionListener(angle_AL);

	angleDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(angleDownBt);
	angleDownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new TriangleAngleBtPressed(false)).start();
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
	});

	angleUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(angleUpBt);
	angleUpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new TriangleAngleBtPressed(true)).start();
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
	});

	btPanel.add(tmpPanel);

	ActionListener baseWidth_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultTriangle().setBaseWidth(
					Double.parseDouble(triangleBaseWidth_TF.getText()));
				DefaultTrianglePanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid base value: " + triangleBaseWidth_TF.getText());
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
	triangleBaseWidth_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultTriangle().getBaseWidth(), 2), 4);
	triangleBaseWidth_TF.setFont(smBtFont);
	tmpPanel.add(triangleBaseWidth_TF);
	triangleBaseWidth_TF.addActionListener(baseWidth_AL);

	JButton applyBaseWidthBt = this.getNewViewImgPlainButton();
	applyBaseWidthBt.setMargin(new Insets(0,0,0,0));
	applyBaseWidthBt.setForeground(Color.black);
	applyBaseWidthBt.setBackground(guiBGColor);
	tmpPanel.add(applyBaseWidthBt);
	applyBaseWidthBt.addActionListener(baseWidth_AL);

	baseDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(baseDownBt);
	baseDownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new TriangleBaseWidthBtPressed(false)).start();
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
	});

	baseUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(baseUpBt);
	baseUpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new TriangleBaseWidthBtPressed(true)).start();
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
	});

	btPanel.add(tmpPanel);

	ActionListener height_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultTriangle().setHeight(
					Double.parseDouble(triangleHeight_TF.getText()));
				DefaultTrianglePanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid height value: " + triangleHeight_TF.getText());
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
	triangleHeight_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultTriangle().getHeight(), 2), 4);
	triangleHeight_TF.setFont(smBtFont);
	tmpPanel.add(triangleHeight_TF);
	triangleHeight_TF.addActionListener(height_AL);

	JButton applyHeightBt = this.getNewViewImgPlainButton();
	applyHeightBt.setMargin(new Insets(0,0,0,0));
	applyHeightBt.setForeground(Color.black);
	applyHeightBt.setBackground(guiBGColor);
	tmpPanel.add(applyHeightBt);
	applyHeightBt.addActionListener(height_AL);

	heightDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(heightDownBt);
	heightDownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new TriangleHeightBtPressed(false)).start();
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
	});

	heightUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(heightUpBt);
	heightUpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new TriangleHeightBtPressed(true)).start();
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
	});

	btPanel.add(tmpPanel);

	ActionListener scale_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultTriangle().setScale(
					Double.parseDouble(triangleScale_TF.getText()));
				DefaultTrianglePanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid scale value: " + triangleScale_TF.getText());
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
	triangleScale_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultTriangle().getScale(), 2), 4);
	triangleScale_TF.setFont(smBtFont);
	tmpPanel.add(triangleScale_TF);
	triangleScale_TF.addActionListener(scale_AL);

	JButton applyScaleBt = this.getNewViewImgPlainButton();
	applyScaleBt.setMargin(new Insets(0,0,0,0));
	applyScaleBt.setForeground(Color.black);
	applyScaleBt.setBackground(guiBGColor);
	tmpPanel.add(applyScaleBt);
	applyScaleBt.addActionListener(scale_AL);

	scaleDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(scaleDownBt);
	scaleDownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new TriangleScaleBtPressed(false)).start();
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
	});

	scaleUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(scaleUpBt);
	scaleUpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new TriangleScaleBtPressed(true)).start();
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
	});

	btPanel.add(tmpPanel);
}

class TriangleAngleBtPressed
extends Thread
{
private double angleInc = 0.2;
private boolean angleDirection = false;

TriangleAngleBtPressed(boolean direction)
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
	double currentAngle = getDefaultTriangle().getAngle();

	if (currentAngle == 360.0)
		currentAngle = 0.0;
	if ((!angleDirection) && (currentAngle == 0.0))
		return;
	if (angleDirection)
		currentAngle += angleInc;
	else
		currentAngle -= angleInc;

	getDefaultTriangle().setAngle(currentAngle);
	triangleAngle_TF.setText(StringUtil.roundStrVal(
		getDefaultTriangle().getAngle(), 2));
	DefaultTrianglePanel.this.repaint();
}
} // end inner class

class TriangleHeightBtPressed
extends Thread
{
private double heightInc = 0.2;
private boolean heightDirection = false;

TriangleHeightBtPressed(boolean direction)
{
	mouseIsPressed = true;
	heightDirection = direction;
}

public void
run()
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

private void
setNewHeight()
{
	double currentHeight = getDefaultTriangle().getHeight();

	if (heightDirection && (currentHeight >= 200.0))
		return;
	else if ((!heightDirection) && (currentHeight <= 2.0))
		return;

	if (heightDirection)
		getDefaultTriangle().setHeight(currentHeight + heightInc);
	else
		getDefaultTriangle().setHeight(currentHeight - heightInc);
	triangleHeight_TF.setText(StringUtil.roundStrVal(
		getDefaultTriangle().getHeight(), 2));
	getDefaultTriangle().reset();

	DefaultTrianglePanel.this.repaint();
}
} // end inner class

class TriangleBaseWidthBtPressed
extends Thread
{
private double baseWidthInc = 0.2;
private boolean baseWidthDirection = false;

TriangleBaseWidthBtPressed(boolean direction)
{
	mouseIsPressed = true;
	baseWidthDirection = direction;
}

public void
run()
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

private void
setNewBaseWidth()
{
	double currentBaseWidth = getDefaultTriangle().getBaseWidth();

	if (baseWidthDirection && (currentBaseWidth >= 200.0))
		return;
	else if ((!baseWidthDirection) && (currentBaseWidth <= 2.0))
		return;

	if (baseWidthDirection)
		getDefaultTriangle().setBaseWidth(currentBaseWidth + baseWidthInc);
	else
		getDefaultTriangle().setBaseWidth(currentBaseWidth - baseWidthInc);
	triangleBaseWidth_TF.setText(StringUtil.roundStrVal(
		getDefaultTriangle().getBaseWidth(), 2));
	getDefaultTriangle().reset();

	DefaultTrianglePanel.this.repaint();
}
} // end inner class

class TriangleScaleBtPressed
extends Thread
{
private double scaleInc = 0.02;
private boolean scaleDirection = false;

TriangleScaleBtPressed(boolean direction)
{
	mouseIsPressed = true;
	scaleDirection = direction;
}

public void
run()
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

private void
setNewScale()
{
	double currentScale = getDefaultTriangle().getScale();

	if (scaleDirection && (currentScale >= 10.0))
		return;
	else if ((!scaleDirection) && (currentScale <= 0.2))
		return;

	if (scaleDirection)
		getDefaultTriangle().setScale(currentScale + scaleInc);
	else
		getDefaultTriangle().setScale(currentScale - scaleInc);
	triangleScale_TF.setText(StringUtil.roundStrVal(
		getDefaultTriangle().getScale(), 2));
	getDefaultTriangle().reset();

	DefaultTrianglePanel.this.repaint();
}
} // end inner class

public void
updateGui()
{
	super.updateGui();
	if (triangleHeight_TF != null)
		triangleHeight_TF.setText(StringUtil.roundStrVal(
			getDefaultTriangle().getHeight(), 2));
	if (triangleBaseWidth_TF != null)
		triangleBaseWidth_TF.setText(StringUtil.roundStrVal(
			getDefaultTriangle().getBaseWidth(), 2));
	if (triangleAngle_TF != null)
		triangleAngle_TF.setText(StringUtil.roundStrVal(
			getDefaultTriangle().getAngle(), 2));
	if (triangleScale_TF != null)
		triangleScale_TF.setText(StringUtil.roundStrVal(
			getDefaultTriangle().getScale(), 2));
}

private DrawTriangleObject defaultTriangle = null;

public void
setDefaultTriangle(DrawTriangleObject defaultTriangle)
{
    this.defaultTriangle = defaultTriangle;
	this.setDefaultDrawObject(this.getDefaultTriangle());
}

public DrawTriangleObject
getDefaultTriangle()
{
	if (this.defaultTriangle == null)
		this.defaultTriangle = (DrawTriangleObject)this.getDefaultDrawObject();
    return (this.defaultTriangle);
}

private static void
debug(String s)
{
	System.out.println("DefaultTrianglePanel-> " + s);
}

}
