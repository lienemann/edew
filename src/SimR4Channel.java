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

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.prefs.*;

public class SimR4Channel extends Simulation
{
    //    JCheckBox opts_r4c_confined;
    JTextField opts_r4c_surfacetension;
    JTextField opts_r4c_width;
    JTextField opts_r4c_height;
    JTextField opts_r4c_canglet;
    JTextField opts_r4c_cangleb;
    JTextField opts_r4c_canglel;
    JTextField opts_r4c_cangler;
    JTextField opts_r4c_thicknesst;
    JTextField opts_r4c_thicknessb;
    JTextField opts_r4c_thicknessl;
    JTextField opts_r4c_thicknessr;
    JTextField opts_r4c_epsilont;
    JTextField opts_r4c_epsilonb;
    JTextField opts_r4c_epsilonl;
    JTextField opts_r4c_epsilonr;
    JTextField opts_r4c_voltaget;
    JTextField opts_r4c_voltageb;
    JTextField opts_r4c_voltagel;
    JTextField opts_r4c_voltager;
    GridBagLayout oagb;
    Preferences prefs;

    public String toString() {
	return("Rectangular channel");
    }

    public void writefile(File outfile) throws IOException {
	System.out.println("Writing output file for R4Channel with:");
	System.out.println("Surface tension:        " +
			   opts_r4c_surfacetension.getText());
	System.out.println("Channel width:          " +
			   opts_r4c_width.getText());
	System.out.println("Channel height:         " +
			   opts_r4c_height.getText());
	System.out.println("Contact angle top:      " +
			   opts_r4c_canglet.getText());
	System.out.println("Contact angle bottom:   " +
			   opts_r4c_cangleb.getText());
	System.out.println("Contact angle left:     " +
			   opts_r4c_canglel.getText());
	System.out.println("Contact angle right:    " +
			   opts_r4c_cangler.getText());

	System.out.println("Layer thickness top:    " +
			   opts_r4c_thicknesst.getText());
	System.out.println("Layer thickness bottom: " +
			   opts_r4c_thicknessb.getText());
	System.out.println("Layer thickness left:   " +
			   opts_r4c_thicknessl.getText());
	System.out.println("Layer thickness right:  " +
			   opts_r4c_thicknessr.getText());

	System.out.println("Act. voltage top:       " +
			   opts_r4c_voltaget.getText());
	System.out.println("Act. voltage botton:    " +
			   opts_r4c_voltageb.getText());
	System.out.println("Act. voltage left:      " +
			   opts_r4c_voltagel.getText());
	System.out.println("Act. voltage right:     " +
			   opts_r4c_voltager.getText());

	System.out.println("Diel. constant top:     " +
			   opts_r4c_epsilont.getText());
	System.out.println("Diel. constant bottom:  " +
			   opts_r4c_epsilonb.getText());
	System.out.println("Diel. constant left:    " +
			   opts_r4c_epsilonl.getText());
	System.out.println("Diel. constant right:   " +
			   opts_r4c_epsilonr.getText());
	JBufferedWriter outw = null;
	try {
	    outw = new JBufferedWriter(new FileWriter(outfile));
	    String name = outfile.getName();
	    String libraryDir = Prefwin.getLibraryDir(prefs);

	    appendFileToWriter(libraryDir+"/Simulations/SimR4Channel/1.fe",
			       outw);

	    outw.writeln("parameter surface_tension = " + opts_r4c_surfacetension.getText() + ";");
	    outw.writeln("parameter width = " + opts_r4c_width.getText() + ";");
	    outw.writeln("parameter height = " + opts_r4c_height.getText() + ";");
	    outw.writeln("parameter canglet = " + opts_r4c_canglet.getText() + ";");
	    outw.writeln("parameter cangleb = " + opts_r4c_cangleb.getText() + ";");
	    outw.writeln("parameter canglel = " + opts_r4c_canglel.getText() + ";");
	    outw.writeln("parameter cangler = " + opts_r4c_cangler.getText() + ";");
	    outw.writeln("parameter thicknesst = " + opts_r4c_thicknesst.getText() + ";");
	    outw.writeln("parameter thicknessb = " + opts_r4c_thicknessb.getText() + ";");
	    outw.writeln("parameter thicknessl = " + opts_r4c_thicknessl.getText() + ";");
	    outw.writeln("parameter thicknessr = " + opts_r4c_thicknessr.getText() + ";");
	    outw.writeln("parameter voltaget = " + opts_r4c_voltaget.getText() + ";");
	    outw.writeln("parameter voltageb = " + opts_r4c_voltageb.getText() + ";");
	    outw.writeln("parameter voltagel = " + opts_r4c_voltagel.getText() + ";");
	    outw.writeln("parameter voltager = " + opts_r4c_voltager.getText() + ";");
	    outw.writeln("parameter epsilont = " + opts_r4c_epsilont.getText() + ";");
	    outw.writeln("parameter epsilonb = " + opts_r4c_epsilonb.getText() + ";");
	    outw.writeln("parameter epsilonl = " + opts_r4c_epsilonl.getText() + ";");
	    outw.writeln("parameter epsilonr = " + opts_r4c_epsilonr.getText() + ";");

	    appendFileToWriter(libraryDir+"/Simulations/SimR4Channel/2.fe", outw);

	    outw.writeln("prefix := \"" + name + "\";");

	    appendFileToWriter(libraryDir+"/Simulations/SimR4Channel/3.fe", outw);
	    outw.close();
	}
	catch (IOException e) {
	    try { if (outw != null) outw.close();}
	    catch (IOException ee) {}
	    throw(e);
	}
    }

    SimR4Channel(Preferences myprefs, final JFrame outerframe) {
	/*               Simulation options for 1D    */
	super(outerframe);
	prefs = myprefs;
	oagb = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

 	GUI.setLayout(oagb);

	opts_r4c_surfacetension =
	    makeTextField(oagb, GUI, "Surface tension [J/m^2]:", 20, "72e-3");
	opts_r4c_width =
	    makeTextField(oagb, GUI, "Channel width [m]:", 20, "100e-6");
	opts_r4c_height =
	    makeTextField(oagb, GUI, "Channel height [m]:", 20, "100e-6");

	opts_r4c_canglet =
	    makeTextField(oagb, GUI, "Contact angle top [deg]:", 20, "110");
	opts_r4c_cangleb =
	    makeTextField(oagb, GUI, "Contact angle bottom [deg]:", 20, "110");
	opts_r4c_canglel =
	    makeTextField(oagb, GUI, "Contact angle left [deg]:", 20, "110");
	opts_r4c_cangler =
	    makeTextField(oagb, GUI, "Contact angle right [deg]:", 20, "110");

	opts_r4c_thicknesst =
	    makeTextField(oagb, GUI, "Layer thickness top [m]:", 20, "1e-6");
	opts_r4c_thicknessb =
	    makeTextField(oagb, GUI, "Layer thickness bottom [m]:", 20, "1e-6");
	opts_r4c_thicknessl =
	    makeTextField(oagb, GUI, "Layer thickness left [m]:", 20, "1e-6");
	opts_r4c_thicknessr =
	    makeTextField(oagb, GUI, "Layer thickness right [m]:", 20, "1e-6");

	opts_r4c_voltaget =
	    makeTextField(oagb, GUI, "Actuation voltage top [V]:", 20, "40");
	opts_r4c_voltageb =
	    makeTextField(oagb, GUI, "Actuation voltage bottom [V]:", 20, "40");
	opts_r4c_voltagel =
	    makeTextField(oagb, GUI, "Actuation voltage left [V]:", 20, "40");
	opts_r4c_voltager =
	    makeTextField(oagb, GUI, "Actuation voltage right [V]:", 20, "40");

	opts_r4c_epsilont =
	    makeTextField(oagb, GUI, "Rel. dielectric constant top:", 20, "3");
	opts_r4c_epsilonb =
	    makeTextField(oagb, GUI, "Rel. dielectric constant bottom:", 20, "3");
	opts_r4c_epsilonl =
	    makeTextField(oagb, GUI, "Rel. dielectric constant left:", 20, "3");
	opts_r4c_epsilonr =
	    makeTextField(oagb, GUI, "Rel. dielectric constant right:", 20, "3");

    }

    public JComponent getEvolverGUI(final Evolverwin ev) {
	JPanel evolverGUI = new JPanel();
	evolverGUI.setLayout(new BoxLayout(evolverGUI, BoxLayout.Y_AXIS));

	JPanel voltages = new JPanel();
	JPanel controls = new JPanel();


 	voltages.add(new JLabel("Voltage top: "));
	final JTextField voltaget = new JTextField(opts_r4c_voltaget.getText(), 3);
	voltaget.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == voltaget ) {
			    try {
				ev.writeToEvolver("voltaget := "+voltaget.getText());
			    }
			    catch (Exception ee) {
				JOptionPane.showMessageDialog(ev,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			    }
			}
		    }
		}
				 );
	voltages.add(voltaget);

	Component b0 = Box.createRigidArea(new Dimension(12,1));
	voltages.add(b0);

 	voltages.add(new JLabel("bottom: "));
	final JTextField voltageb = new JTextField(opts_r4c_voltageb.getText(), 3);
	voltageb.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == voltageb ) {
			    try {
				ev.writeToEvolver("voltageb := "+voltageb.getText());
			    }
			    catch (Exception ee) {
				JOptionPane.showMessageDialog(ev,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			    }
			}
		    }
		}
				 );
	voltages.add(voltageb);

	b0 = Box.createRigidArea(new Dimension(12,1));
	voltages.add(b0);

 	voltages.add(new JLabel("left: "));
	final JTextField voltagel = new JTextField(opts_r4c_voltagel.getText(), 3);
	voltagel.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == voltagel ) {
			    try {
				ev.writeToEvolver("voltagel := "+voltagel.getText());
			    }
			    catch (Exception ee) {
				JOptionPane.showMessageDialog(ev,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			    }
			}
		    }
		}
				 );
	voltages.add(voltagel);

	b0 = Box.createRigidArea(new Dimension(12,1));
	voltages.add(b0);

 	voltages.add(new JLabel("right: "));
	final JTextField voltager = new JTextField(opts_r4c_voltager.getText(), 3);
	voltager.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == voltager ) {
			    try {
				ev.writeToEvolver("voltager := "+voltager.getText());
			    }
			    catch (Exception ee) {
				JOptionPane.showMessageDialog(ev,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			    }
			}
		    }
		}
				 );
	voltages.add(voltager);

	b0 = Box.createRigidArea(new Dimension(12,1));
	voltages.add(b0);

	final JButton init = new JButton("Init");
	init.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == init ) {
			    try {
				ev.writeToEvolver("doinit");
			    }
			    catch (Exception ee) {
				JOptionPane.showMessageDialog(ev,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			    }
			}
		    }
		}
				);
	controls.add(init);
	b0 = Box.createRigidArea(new Dimension(12,1));
	controls.add(b0);

	final JButton equil = new JButton("Equil.");
	equil.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == equil ) {
			    try {
				ev.writeToEvolver("equil");
			    }
			    catch (Exception ee) {
				JOptionPane.showMessageDialog(ev,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			    }
			}
		    }
		}
				);
	controls.add(equil);
	b0 = Box.createRigidArea(new Dimension(12,1));
	controls.add(b0);

 	controls.add(new JLabel("Perform # steps: "));
	final JCheckBox saveps = new JCheckBox("Save PS");
	final JTextField dosteps = new JTextField("10", 3);
	dosteps.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == dosteps ) {
			    try {
				if (saveps.isSelected())
				ev.writeToEvolver("{ dostep; saveps } "+dosteps.getText());
				else
				ev.writeToEvolver("dostep "+dosteps.getText());
			    }
			    catch (Exception ee) {
				JOptionPane.showMessageDialog(ev,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			    }
			}
		    }
		}
				 );
	controls.add(dosteps);
	b0 = Box.createRigidArea(new Dimension(12,1));
	controls.add(b0);
	controls.add(saveps);
	evolverGUI.add(voltages);
	evolverGUI.add(controls);
	return (evolverGUI);
    }
}
