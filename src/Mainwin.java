/*
 * Microprotein simulation tool
 * Copyright (C) 2004 Jan Lienemann <lieneman@imtek.de>
 * @(#) $Id$
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License   
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


import java.util.*;
import java.util.prefs.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Mainwin extends JPanel
{


    JLabel title;
    JComboBox simtype;
    Box sbar;
    JTextField sbartext;
    JButton writefileButton;
    JButton startevolverButton;
    JButton preferencesButton;
    JButton quitButton;
    JPanel simopts;
    JFileChooser fc;
    JFrame outerfr;

//     String[] simtypes = {"1D path",
// 		       "2D array",
// 		       "Ramping (1 Ramp)",
// 		       "Ramping (Multiple)",
// 		       "Actuation schemes", // Programm eingeben
// 		       // Feld V(high) V(low) V(high imp) V(gnd)
// 		       // -> Optimization tool
// 		       "Channel"};
    Simulation sim2darray;

    protected File getSaveFile() {
	int ret = fc.showSaveDialog(outerfr);
	try {
	    if (ret != JFileChooser.APPROVE_OPTION)
		return(null);
	    if (fc.getSelectedFile().isDirectory())
		throw(new IOException("Please choose a file, not a directory."));
	    if (fc.getSelectedFile().isFile() &&
		JOptionPane.showConfirmDialog(outerfr, "File " + 
					      fc.getSelectedFile() +
					      " exists! Overwrite?",
					      "File exists!",
					      JOptionPane.YES_NO_OPTION,
					      JOptionPane.WARNING_MESSAGE)
		!= JOptionPane.YES_OPTION)
		return(null);
	    return(fc.getSelectedFile());
	}
	catch (IOException e) {
	    JOptionPane.showMessageDialog(outerfr,
					  e.getMessage(),
					  "Problem selecting file",
					  JOptionPane.ERROR_MESSAGE);
	    return(null);
	}
    }

    Mainwin(final Preferences pref, final JFrame outerframe)
    {
	super();
	Dimension hsp12 = new Dimension(12,1);
	fc = new JFileChooser();
	Prefwin.fc = new JFileChooser();
	outerfr = outerframe;

 	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	JPanel sub1 = new JPanel();
 	sub1.setLayout(new BoxLayout(sub1, BoxLayout.Y_AXIS));
	sub1.setBorder(BorderFactory.createEmptyBorder(12,12,11,11));

	/*************** Title ************************/
	JPanel sub2 = new JPanel();
	sub2.setLayout(new BoxLayout(sub2, BoxLayout.X_AXIS));
	title = new JLabel("EDEW 2.0");
	title.setFont(new Font("", Font.BOLD, 24));
	title.setRequestFocusEnabled(false);
 	sub2.add(title);
	sub1.add(sub2);
	sub1.add(Box.createRigidArea(new Dimension(1,11)));

	Vector simulations = new Vector();
	/*************** Simulation type **************/
	simulations.add(new Sim1DPath(pref, outerframe));
 	simulations.add(new SimSpikeShape(pref, outerframe));
// 	simulations.add(new Sim2DArray(pref, outerframe));
// 	simulations.add(new SimActSch(outerframe));
	simulations.add(new SimR4Channel(pref, outerframe));
// 	simulations.add(new SimRamp_one(outerframe));
// 	simulations.add(new SimRamp_mult(outerframe));

 	simtype = new JComboBox(simulations);
 	simtype.setToolTipText("Choose simulation type");
 	simtype.setEditable(false);
	simtype.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == simtype) {
			simopts.remove(0);
			simopts.add(((Simulation)((JComboBox)e.getSource()).getSelectedItem()).GUI);
			outerframe.pack();
		    }
		}
	    });
	
 	sub1.add(simtype);

	sub1.add(Box.createRigidArea(new Dimension(1,11)));

	/*************** Simulation options ***********/
	simopts = new JPanel();
	simopts.setBorder(BorderFactory.createTitledBorder("Simulation options"));
	simopts.setLayout(new BoxLayout(simopts, BoxLayout.Y_AXIS));

	simopts.add(((Simulation)simulations.elementAt(0)).GUI);

	sub1.add(simopts);

	sub1.add(Box.createGlue());

	add(sub1, BorderLayout.CENTER);

	/*************** Buttons **********************/
	JPanel buttons = new JPanel();
	buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
	buttons.add(Box.createGlue());
	preferencesButton = new JButton("Preferences...");
	preferencesButton.setToolTipText("Modify library path, surface evolver path etc.");
	preferencesButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == preferencesButton) {
			new Prefwin(pref, outerframe).show();
		    }
		}
	    });
	buttons.add(preferencesButton);


	buttons.add(Box.createRigidArea(new Dimension(5,1)));
	writefileButton = new JButton("Write file...");
	writefileButton.setToolTipText("Write a surface evolver file with the current settings");
	writefileButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == writefileButton) {
			try {
			    File f = getSaveFile();
			    ((Simulation)simtype.getSelectedItem()).writefile(f);
			}
			catch (Exception ee) {
			    JOptionPane.showMessageDialog(outerfr,
					    ee.getMessage(),
					    "Problem with writing file",
					    JOptionPane.ERROR_MESSAGE);
			}
		    }
		}
	    });
	buttons.add(writefileButton);
	buttons.add(Box.createRigidArea(new Dimension(5,1)));
	startevolverButton = new JButton("Start Evolver");
	startevolverButton.setToolTipText("Start the surface evolver with the current settings");
	startevolverButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == startevolverButton) {
			File tf = null;
			try {
			    String dir = Prefwin.getRunDir(pref);
			    File d = new File(dir);
			    d.mkdirs();
			    tf = File.createTempFile("mprot_", ".fe", d);
			    ((Simulation)simtype.getSelectedItem()).writefile(tf);			    
			}
			catch (Exception ee) {
			    JOptionPane.showMessageDialog(outerfr,
					    ee.getMessage(),
					    "Problem with writing tempfile",
					    JOptionPane.ERROR_MESSAGE);
			    return;
			}
			try{
			    Evolverwin ev =
				new Evolverwin(Prefwin.getEvolverBinary(pref),
					       Prefwin.getRunDir(pref),
					       tf.getAbsolutePath(),
					       ((Simulation)simtype.getSelectedItem()));
			    ev.pack();
			    ev.setVisible(true);
			}
			catch (Exception ee) {
			    JOptionPane.showMessageDialog(outerfr,
					    ee.getMessage(),
					    "Problem with running evolver",
					    JOptionPane.ERROR_MESSAGE);
			}
		    }
		}
	    });
	buttons.add(startevolverButton);
	buttons.add(Box.createRigidArea(new Dimension(5,1)));
	quitButton = new JButton("Quit");
	quitButton.setToolTipText("Quit the application");
	quitButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == quitButton) {
			System.exit(0);
		    }
		}
	    });
	buttons.add(quitButton);
	buttons.setBorder(BorderFactory.createEmptyBorder(12,17,11,11));
	add(buttons);

// 	sbar = Box.createHorizontalBox();
//  	sbartext = new JTextField("Please choose a simulation type");
// 	sbartext.setEditable(false);
//   	sbar.add(sbartext);
//  	add(sbar);

	//	prefwin = new Prefwin(pref, outerframe);
    }

//     public void actionPerformed(ActionEvent e) {
	
//     }
    
};
