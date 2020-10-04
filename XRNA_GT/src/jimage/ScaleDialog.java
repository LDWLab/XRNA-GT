package jimage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ScaleDialog extends JDialog implements ActionListener {

	private JButton ok, cancel;
	private JComboBox scaleChoice;

	private String[] scaleSizes;


        // constants
	private static final String okS = "ok";
	private static final String cancelS = "cancel";



	private void buildGUI() {

		JPanel contentPane = new JPanel();
		contentPane.setLayout( new BorderLayout() );

		JPanel sp = new JPanel();
		createScaleChoice();
		sp.add( new JLabel("Scale Factor: ") );
		sp.add(scaleChoice);

                JPanel bp = createButtons();


                contentPane.add(sp, BorderLayout.NORTH);
                contentPane.add(bp, BorderLayout.SOUTH);

		setContentPane(contentPane);

		// make ok the default button
		getRootPane().setDefaultButton(ok);

	}

	private JPanel createButtons() {

		JPanel p = new JPanel();

		ok = new JButton("Ok");
                ok.addActionListener(this);
                ok.setActionCommand(okS);
                cancel = new JButton("Cancel");
                cancel.addActionListener(this);
                cancel.setActionCommand(cancelS);

                p.add(ok);
                p.add(cancel);

                return p;

	}

	private void createScaleChoice() {

		scaleChoice = new JComboBox(scaleSizes);
		scaleChoice.setEditable(true);
                scaleChoice.setToolTipText("Scale/Zoom");
                scaleChoice.setPreferredSize( new Dimension(80, 25) );

	}

	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();

		if ( actionCommand.equals(okS) ) {

		   setVisible(false);

		}

		else if ( actionCommand.equals(cancelS) )
		   setVisible(false);

	}

}


