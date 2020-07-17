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
DefaultLinePanel
extends DefaultDrawObjectPanel
{

public
DefaultLinePanel(DrawObjectLeafNode defaultDrwObj, Color bgColor,
	double scaleVal, JColorChooser colorChooser)
{
	super(defaultDrwObj, bgColor, scaleVal, colorChooser);
}

public void
preBuildGui()
{
	defaultLine = (DrawLineObject)this.getDefaultDrawObject();
}

public void
buildGui()
{
	btPanel = new JPanel(new GridLayout(6, 1), true);
	btPanel.setBackground(guiBGColor);
	btPanel.setForeground(Color.black);

	super.buildGui();

	ActionListener lineLength_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				defaultLine.setFromLength(
					Double.parseDouble(lineLength_TF.getText()));
				DefaultLinePanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid length value: " + lineLength_TF.getText());
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

	JLabel label = new JLabel("length:");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	lineLength_TF = new JTextField(
		StringUtil.roundStrVal(defaultLine.getLength(), 2), 4);
	lineLength_TF.setFont(smBtFont);
	tmpPanel.add(lineLength_TF);
	lineLength_TF.addActionListener(lineLength_AL);
	JButton lineLength_Bt = this.getNewViewImgPlainButton();
	lineLength_Bt.addActionListener(lineLength_AL);
	tmpPanel.add(lineLength_Bt);

	JButton lengthDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(lengthDownBt);
	lengthDownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new LineLengthBtPressed(false)).start();
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

	JButton lengthUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(lengthUpBt);
	lengthUpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new LineLengthBtPressed(true)).start();
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

	ActionListener lineAngle_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				defaultLine.setFromAngle(
					Double.parseDouble(lineAngle_TF.getText()));
				DefaultLinePanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle value: " + lineAngle_TF.getText());
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

	label = new JLabel("angle:");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	lineAngle_TF = new JTextField(
		StringUtil.roundStrVal(defaultLine.getAngle(), 2), 4);
	lineAngle_TF.setFont(smBtFont);
	tmpPanel.add(lineAngle_TF);
	lineAngle_TF.addActionListener(lineAngle_AL);
	JButton lineAngle_Bt = this.getNewViewImgPlainButton();
	lineAngle_Bt.addActionListener(lineAngle_AL);
	tmpPanel.add(lineAngle_Bt);

	JButton angleDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(angleDownBt);
	angleDownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new LineAngleBtPressed(false)).start();
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

	JButton angleUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(angleUpBt);
	angleUpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new LineAngleBtPressed(true)).start();
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

class LineLengthBtPressed
extends Thread
{
private double lengthInc = 0.2;
private boolean lengthDirection = false;

LineLengthBtPressed(boolean direction)
{
	mouseIsPressed = true;
	lengthDirection = direction;
}

public void
run()
{
	try
	{
		setNewLength();
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
			setNewLength();
		}
	}
	catch (Exception e)
	{
		handleException(e, 1);
	}
}

private void
setNewLength()
throws Exception
{
	double currentLength = defaultLine.getLength();

	if (lengthDirection && (currentLength >= 40.0))
		return;
	else if ((!lengthDirection) && (currentLength <= 2.0))
		return;

	if (lengthDirection)
		defaultLine.setFromLength(currentLength + lengthInc);
	else
		defaultLine.setFromLength(currentLength - lengthInc);
	lineLength_TF.setText(StringUtil.roundStrVal(
		defaultLine.getLength(), 2));
	
	DefaultLinePanel.this.repaint();
}
} // end inner class

class LineAngleBtPressed
extends Thread
{
private double angleInc = 0.2;
private boolean angleDirection = false;

LineAngleBtPressed(boolean direction)
{
	mouseIsPressed = true;
	angleDirection = direction;
}

public void
run()
{
	try
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
	catch (Exception e)
	{
		handleException(e, 1);
	}
}

private void
setNewAngle()
throws Exception
{
	double currentAngle = defaultLine.getAngle();

	if (currentAngle == 360.0)
		currentAngle = 0.0;

	if (angleDirection)
		currentAngle += angleInc;
	else
		currentAngle -= angleInc;

	if (currentAngle == 360.0)
		currentAngle = 0.0;
	else if (currentAngle >= 360.0)
		currentAngle -= 360.0;
	else if (currentAngle < 0.0)
		currentAngle += 360.0;

	defaultLine.setFromAngle(currentAngle);
	lineAngle_TF.setText(StringUtil.roundStrVal(
		defaultLine.getAngle(), 2));
	DefaultLinePanel.this.repaint();
}
} // end inner class

public void
updateGui()
{
	super.updateGui();
	if (lineLength_TF != null)
		lineLength_TF.setText(StringUtil.roundStrVal(
			getDefaultLine().getLength(), 2));
	if (lineAngle_TF != null)
		lineAngle_TF.setText(StringUtil.roundStrVal(
			getDefaultLine().getAngle(), 2));
}

private DrawLineObject defaultLine = null;

public void
setDefaultLine(DrawLineObject defaultLine)
{
    this.defaultLine = defaultLine;
	this.setDefaultDrawObject(this.getDefaultLine());
}

public DrawLineObject
getDefaultLine()
{
	if (this.defaultLine == null)
		this.defaultLine = (DrawLineObject)this.getDefaultDrawObject();
    return (this.defaultLine);
}

private static void
debug(String s)
{
	System.out.println("DefaultLinePanel-> " + s);
}

}
