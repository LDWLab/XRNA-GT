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
DefaultParallelogramPanel
extends DefaultDrawObjectPanel
{

JButton angle1DownBt = null;
JButton angle1UpBt = null;
JButton angle2DownBt = null;
JButton angle2Up_Bt = null;

JButton side1DownBt = null;
JButton side1UpBt = null;
JButton side2DownBt = null;
JButton side2UpBt = null;

public
DefaultParallelogramPanel(DrawObjectLeafNode defaultDrwObj, Color bgColor, double scaleVal, JColorChooser colorChooser)
{
	super(defaultDrwObj, bgColor, scaleVal, colorChooser);
}

public void
preBuildGui()
{
	this.setDefaultParallelogram((DrawParallelogramObject)this.getDefaultDrawObject());
}

public void
buildGui()
{
	btPanel = new JPanel(new GridLayout(8, 1), true);
	btPanel.setBackground(guiBGColor);
	btPanel.setForeground(Color.black);

	super.buildGui();

	ActionListener angle1_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				defaultParallelogram.setAngle1(
					Double.parseDouble(parallelogramAngle1_TF.getText()));
				DefaultParallelogramPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle1 value: " + parallelogramAngle1_TF.getText());
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
	JLabel label = new JLabel("angle 1");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	parallelogramAngle1_TF = new JTextField(
		StringUtil.roundStrVal(defaultParallelogram.getAngle1(), 2), 4);
	parallelogramAngle1_TF.setFont(smBtFont);
	tmpPanel.add(parallelogramAngle1_TF);
	parallelogramAngle1_TF.addActionListener(angle1_AL);

	JButton applyAngle1Bt = this.getNewViewImgPlainButton();
	tmpPanel.add(applyAngle1Bt);
	applyAngle1Bt.addActionListener(angle1_AL);

	angle1DownBt = this.getNewViewImgDownButton();
	tmpPanel.add(angle1DownBt);
	
	angle1DownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new ParallelogramAngleBtPressed(true, false)).start();
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

	angle1UpBt = this.getNewViewImgUpButton();
	tmpPanel.add(angle1UpBt);

	angle1UpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new ParallelogramAngleBtPressed(true, true)).start();
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

	ActionListener angle2_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				defaultParallelogram.setAngle2(
					Double.parseDouble(parallelogramAngle2_TF.getText()));
				DefaultParallelogramPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid angle2 value: " + parallelogramAngle2_TF.getText());
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
	label = new JLabel("angle 2");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	parallelogramAngle2_TF = new JTextField(
		StringUtil.roundStrVal(defaultParallelogram.getAngle2(), 2), 4);
	parallelogramAngle2_TF.setFont(smBtFont);
	tmpPanel.add(parallelogramAngle2_TF);
	parallelogramAngle2_TF.addActionListener(angle2_AL);

	JButton applyAngle2_Bt = this.getNewViewImgPlainButton();
	tmpPanel.add(applyAngle2_Bt);
	applyAngle2_Bt.addActionListener(angle2_AL);

	angle2DownBt = this.getNewViewImgDownButton();
	tmpPanel.add(angle2DownBt);
	
	angle2DownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new ParallelogramAngleBtPressed(false, false)).start();
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

	angle2Up_Bt = this.getNewViewImgUpButton();
	tmpPanel.add(angle2Up_Bt);

	angle2Up_Bt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new ParallelogramAngleBtPressed(false, true)).start();
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

	ActionListener side1_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				defaultParallelogram.setSide1(
					Double.parseDouble(parallelogramSide1_TF.getText()));
				DefaultParallelogramPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid side1 value: " + parallelogramSide1_TF.getText());
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
	label = new JLabel("side 1");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	parallelogramSide1_TF = new JTextField(
		StringUtil.roundStrVal(defaultParallelogram.getSide1(), 2), 4);
	parallelogramSide1_TF.setFont(smBtFont);
	tmpPanel.add(parallelogramSide1_TF);
	parallelogramSide1_TF.addActionListener(side1_AL);

	JButton applySide1_Bt = this.getNewViewImgPlainButton();
	tmpPanel.add(applySide1_Bt);
	applySide1_Bt.addActionListener(side1_AL);

	side1DownBt = this.getNewViewImgDownButton();
	tmpPanel.add(side1DownBt);
	
	side1DownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new ParallelogramSideBtPressed(true, false)).start();
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

	side1UpBt = this.getNewViewImgUpButton();
	tmpPanel.add(side1UpBt);

	side1UpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new ParallelogramSideBtPressed(true, true)).start();
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

	ActionListener side2_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				defaultParallelogram.setSide2(
					Double.parseDouble(parallelogramSide2_TF.getText()));
				DefaultParallelogramPanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid side2 value: " + parallelogramSide2_TF.getText());
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
	label = new JLabel("side 2");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	parallelogramSide2_TF = new JTextField(
		StringUtil.roundStrVal(defaultParallelogram.getSide2(), 2), 4);
	parallelogramSide2_TF.setFont(smBtFont);
	tmpPanel.add(parallelogramSide2_TF);
	parallelogramSide2_TF.addActionListener(side2_AL);

	JButton applySide2_Bt = this.getNewViewImgPlainButton();
	tmpPanel.add(applySide2_Bt);
	applySide2_Bt.addActionListener(side2_AL);

	side2DownBt = this.getNewViewImgDownButton();
	tmpPanel.add(side2DownBt);
	
	side2DownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new ParallelogramSideBtPressed(false, false)).start();
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

	side2UpBt = this.getNewViewImgUpButton();
	side2UpBt.setMargin(new Insets(0,0,0,0));
	side2UpBt.setForeground(Color.black);
	side2UpBt.setBackground(guiBGColor);
	tmpPanel.add(side2UpBt);

	side2UpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new ParallelogramSideBtPressed(false, true)).start();
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

class ParallelogramAngleBtPressed
extends Thread
{
private double angleInc = 0.2;
private boolean angleDirection = false;
private boolean isAngle1 = true;

ParallelogramAngleBtPressed(boolean isAngle1, boolean direction)
{
	mouseIsPressed = true;
	this.isAngle1 = isAngle1;
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
	double currentAngle1 = getDefaultParallelogram().getAngle1();
	double currentAngle2 = getDefaultParallelogram().getAngle2();
	double angleBetweenSides =
		getDefaultParallelogram().angleBetweenSide1AndSide2();
	// debug("angleBetweenSide1AndSide2(): " + getDefaultParallelogram().angleBetweenSide1AndSide2());

	if (isAngle1)
	{
		if (angleDirection && (angleBetweenSides <= 20.0))
			return;
		else if ((!angleDirection) && (angleBetweenSides >= 160.0))
			return;
	}
	else
	{
		if (angleDirection && (angleBetweenSides >= 160.0))
			return;
		else if ((!angleDirection) && (angleBetweenSides <= 20.0))
			return;
	}

	if (isAngle1)
	{
		if (angleDirection)
			getDefaultParallelogram().setAngle1(currentAngle1 + angleInc);
		else
			getDefaultParallelogram().setAngle1(currentAngle1 - angleInc);
		parallelogramAngle1_TF.setText(StringUtil.roundStrVal(
			getDefaultParallelogram().getAngle1(), 2));
	}
	else
	{
		if (angleDirection)
			getDefaultParallelogram().setAngle2(currentAngle2 + angleInc);
		else
			getDefaultParallelogram().setAngle2(currentAngle2 - angleInc);
		parallelogramAngle2_TF.setText(StringUtil.roundStrVal(
			getDefaultParallelogram().getAngle2(), 2));
	}
	DefaultParallelogramPanel.this.repaint();
}
} // end inner class


class ParallelogramSideBtPressed
extends Thread
{
private double sideInc = 0.2;
private boolean sideDirection = false;
private boolean isSide1 = true;

ParallelogramSideBtPressed(boolean isSide1, boolean direction)
{
	mouseIsPressed = true;
	this.isSide1 = isSide1;
	sideDirection = direction;
}

public void
run()
{
	setNewSide();
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
		setNewSide();
	}
}

private void
setNewSide()
{
	double currentSide1 = getDefaultParallelogram().getSide1();
	double currentSide2 = getDefaultParallelogram().getSide2();

	if (isSide1)
	{
		if (sideDirection && (currentSide1 >= 80.0))
			return;
		else if ((!sideDirection) && (currentSide1 <= 2.0))
			return;
	}
	else
	{
		if (sideDirection && (currentSide2 >= 80.0))
			return;
		else if ((!sideDirection) && (currentSide2 <= 2.0))
			return;
	}

	if (isSide1)
	{
		if (sideDirection)
			getDefaultParallelogram().setSide1(currentSide1 + sideInc);
		else
			getDefaultParallelogram().setSide1(currentSide1 - sideInc);
		parallelogramSide1_TF.setText(StringUtil.roundStrVal(
			getDefaultParallelogram().getSide1(), 2));
	}
	else
	{
		if (sideDirection)
			getDefaultParallelogram().setSide2(currentSide2 + sideInc);
		else
			getDefaultParallelogram().setSide2(currentSide2 - sideInc);
		parallelogramSide2_TF.setText(StringUtil.roundStrVal(
			getDefaultParallelogram().getSide2(), 2));
	}
	DefaultParallelogramPanel.this.repaint();
}
} // end inner class

public void
updateGui()
{
	super.updateGui();
	if (parallelogramAngle1_TF != null)
		parallelogramAngle1_TF.setText(StringUtil.roundStrVal(
			getDefaultParallelogram().getAngle1(), 2));
	if (parallelogramAngle2_TF != null)
		parallelogramAngle2_TF.setText(StringUtil.roundStrVal(
			getDefaultParallelogram().getAngle2(), 2));
	if (parallelogramSide1_TF != null)
		parallelogramSide1_TF.setText(StringUtil.roundStrVal(
			getDefaultParallelogram().getSide1(), 2));
	if (parallelogramSide2_TF != null)
		parallelogramSide2_TF.setText(StringUtil.roundStrVal(
			getDefaultParallelogram().getSide2(), 2));
}

private DrawParallelogramObject defaultParallelogram = null;

public void
setDefaultParallelogram(DrawParallelogramObject defaultParallelogram)
{
    this.defaultParallelogram = defaultParallelogram;
	this.setDefaultDrawObject(this.getDefaultParallelogram());
}

public DrawParallelogramObject
getDefaultParallelogram()
{
	if (this.defaultParallelogram == null)
		this.defaultParallelogram = (DrawParallelogramObject)this.getDefaultDrawObject();
    return (this.defaultParallelogram);
}


private static void
debug(String s)
{
	System.out.println("DefaultParallelogramPanel-> " + s);
}

}
