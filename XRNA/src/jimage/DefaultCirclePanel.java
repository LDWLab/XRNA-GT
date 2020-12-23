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
DefaultCirclePanel
extends DefaultDrawObjectPanel
{

JButton radiusDownBt = null;
JButton radiusUpBt = null;

public
DefaultCirclePanel(DrawObjectLeafNode defaultDrwObj, Color bgColor,
	double scaleVal, JColorChooser colorChooser)
{
	super(defaultDrwObj, bgColor, scaleVal, colorChooser);
}

public void
preBuildGui()
{
	this.setDefaultCircle((DrawCircleObject)this.getDefaultDrawObject());
}

public void
buildGui()
{
	btPanel = new JPanel(new GridLayout(6, 1), true);
	btPanel.setBackground(guiBGColor);
	btPanel.setForeground(Color.black);

	super.buildGui();

	ActionListener radius_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			try
			{
				getDefaultCircle().setRadius(
					Double.parseDouble(circleRadius_TF.getText()));
				DefaultCirclePanel.this.repaint();
			}
			catch (NumberFormatException e)
			{
				alert("Invalid radius value: " + circleRadius_TF.getText());
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
	JLabel label = new JLabel("radius");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	circleRadius_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultCircle().getRadius(), 2), 4);
	circleRadius_TF.setFont(smBtFont);
	tmpPanel.add(circleRadius_TF);
	circleRadius_TF.addActionListener(radius_AL);

	JButton applyRadiusBt = this.getNewViewImgPlainButton();
	applyRadiusBt.setMargin(new Insets(0,0,0,0));
	applyRadiusBt.setForeground(Color.black);
	applyRadiusBt.setBackground(guiBGColor);
	tmpPanel.add(applyRadiusBt);
	applyRadiusBt.addActionListener(radius_AL);

	radiusDownBt = this.getNewViewImgDownButton();
	tmpPanel.add(radiusDownBt);
	radiusDownBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new CircleRadiusBtPressed(false)).start();
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

	radiusUpBt = this.getNewViewImgUpButton();
	tmpPanel.add(radiusUpBt);
	radiusUpBt.addMouseListener(new MouseAdapter()
	{
		public void
		mousePressed(MouseEvent event)
		{
			try
			{
				mouseIsPressed = true;
				(new CircleRadiusBtPressed(true)).start();
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

	ActionListener arc_AL = new ActionListener()
	{
		public void
		actionPerformed(ActionEvent evt)
		{
			String actionCmd = evt.getActionCommand();
			if (actionCmd.equals("circleStartAngle_TF") ||
				actionCmd.equals("circleStartAngle_Bt"))
			{
				try
				{
					getDefaultCircle().setStartAngle(
						Double.parseDouble(circleStartAngle_TF.getText()));

					DefaultCirclePanel.this.repaint();
				}
				catch (NumberFormatException e)
				{
					alert("Invalid start angle value: " + circleStartAngle_TF.getText());
					return;
				}
				catch (Exception e)
				{
					handleException(e, 1);
				}
			}
			else if (actionCmd.equals("circleAngleExtent_TF") ||
				actionCmd.equals("circleAngleExtent_Bt"))
			{
				try
				{
					getDefaultCircle().setAngleExtent(
						Double.parseDouble(circleAngleExtent_TF.getText()));

					DefaultCirclePanel.this.repaint();
				}
				catch (NumberFormatException e)
				{
					alert("Invalid angle extent value: " + circleAngleExtent_TF.getText());
					return;
				}
				catch (Exception e)
				{
					handleException(e, 1);
				}
			}
		}
	};

	tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	label = new JLabel("start angle");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	circleStartAngle_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultCircle().getStartAngle(), 2), 4);
	circleStartAngle_TF.setFont(smBtFont);
	circleStartAngle_TF.setActionCommand("circleStartAngle_TF");
	tmpPanel.add(circleStartAngle_TF);
	circleStartAngle_TF.addActionListener(arc_AL);

	JButton applyStartAngle_Bt = this.getNewViewImgPlainButton();
	applyStartAngle_Bt.setMargin(new Insets(0,0,0,0));
	applyStartAngle_Bt.setForeground(Color.black);
	applyStartAngle_Bt.setBackground(guiBGColor);
	applyStartAngle_Bt.setActionCommand("circleStartAngle_Bt");
	tmpPanel.add(applyStartAngle_Bt);
	applyStartAngle_Bt.addActionListener(arc_AL);

	btPanel.add(tmpPanel);

	tmpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT), true);
	tmpPanel.setBackground(guiBGColor);
	tmpPanel.setForeground(Color.black);
	tmpPanel.setFont(smBtFont);
	label = new JLabel("angle extent");
	label.setBackground(guiBGColor);
	label.setForeground(Color.black);
	label.setFont(smBtFont);
	tmpPanel.add(label);
	circleAngleExtent_TF = new JTextField(
		StringUtil.roundStrVal(getDefaultCircle().getAngleExtent(), 2), 4);
	circleAngleExtent_TF.setFont(smBtFont);
	circleAngleExtent_TF.setActionCommand("circleAngleExtent_TF");
	tmpPanel.add(circleAngleExtent_TF);
	circleAngleExtent_TF.addActionListener(arc_AL);

	JButton applyAngleExtent_Bt = this.getNewViewImgPlainButton();
	applyAngleExtent_Bt.setMargin(new Insets(0,0,0,0));
	applyAngleExtent_Bt.setForeground(Color.black);
	applyAngleExtent_Bt.setBackground(guiBGColor);
	applyAngleExtent_Bt.setActionCommand("circleAngleExtent_Bt");
	tmpPanel.add(applyAngleExtent_Bt);
	applyAngleExtent_Bt.addActionListener(arc_AL);

	btPanel.add(tmpPanel);
}

class CircleRadiusBtPressed
extends Thread
{
private double radiusInc = 0.2;
private boolean radiusDirection = false;

CircleRadiusBtPressed(boolean direction)
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
	double currentRadius = getDefaultCircle().getRadius();

	if (radiusDirection && (currentRadius >= 40.0))
		return;
	else if ((!radiusDirection) && (currentRadius <= 2.0))
		return;

	if (radiusDirection)
		getDefaultCircle().setRadius(currentRadius + radiusInc);
	else
		getDefaultCircle().setRadius(currentRadius - radiusInc);
	circleRadius_TF.setText(StringUtil.roundStrVal(
		getDefaultCircle().getRadius(), 2));
	
	DefaultCirclePanel.this.repaint();
}
} // end inner class

public void
updateGui()
{
	super.updateGui();
	if (circleRadius_TF != null)
		circleRadius_TF.setText(StringUtil.roundStrVal(
			getDefaultCircle().getRadius(), 2));
}

private DrawCircleObject defaultCircle = null;

public void
setDefaultCircle(DrawCircleObject defaultCircle)
{
    this.defaultCircle = defaultCircle;
	this.setDefaultDrawObject(this.getDefaultCircle());
}

public DrawCircleObject
getDefaultCircle()
{
	if (this.defaultCircle == null)
		this.defaultCircle = (DrawCircleObject)this.getDefaultDrawObject();
    return (this.defaultCircle);
}

private static void
debug(String s)
{
	System.out.println("DefaultCirclePanel-> " + s);
}

}
