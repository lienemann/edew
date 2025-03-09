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

public class Sim1DPath extends Simulation
{
    JCheckBox opts_path_confined;
    JTextField opts_path_surfacetension;
    JTextField opts_path_cangle;
    JTextField opts_path_cangle_top;
    JLabel     opts_path_cangle_topl;
    Component opts_path_cangle_topb;
    JTextField opts_path_topdist;
    JLabel opts_path_topdistl;
    Component opts_path_topdistb;
    JTextField opts_path_vol;
    JSpinner opts_path_px;
    JTextField opts_path_psx;
    JTextField opts_path_psy;
    JTextField opts_path_pgap;
    JTextField opts_path_spikelength;
    JTextField opts_path_spx;
    JTextField opts_path_voltage;
    JTextField opts_path_epsilon;
    JTextField opts_path_thickness;
    GridBagLayout oagb;
    Preferences prefs;

    public String toString() {
	return("1D path");
    }

    public void writefile(File outfile) throws IOException {
	System.out.println("Writing output file for 1D path with:");
	System.out.println("Confined droplet:     " +
			   opts_path_confined.isSelected());
	System.out.println("Surface tension:      " +
			   opts_path_surfacetension.getText());
	if (opts_path_confined.isSelected()) {
	    System.out.println("Contact angle top:    " +
			       opts_path_cangle_top.getText());
	    System.out.println("Top distance:         " +
			       opts_path_topdist.getText());
	}
	System.out.println("Contact angle bottom: " +
			   opts_path_cangle.getText());
	System.out.println("Droplet volume:       " +
			   opts_path_vol.getText());
	System.out.println("# pads:                " +
			   opts_path_px.getValue());
	System.out.println("Pad size x:           " +
			   opts_path_psx.getText());
	System.out.println("Pad size y:           " +
			   opts_path_psy.getText());
	System.out.println("Pad gap:             " +
			   opts_path_pgap.getText());
	System.out.println("Spike length:        " +
			   opts_path_spikelength.getText());
	System.out.println("Start position:      " +
			   opts_path_spx.getText());
	System.out.println("Actuation voltage:   " +
			   opts_path_voltage.getText());
	System.out.println("Dielectric constant: " +
			   opts_path_epsilon.getText());
	System.out.println("Layer thickness:     " +
			   opts_path_thickness.getText());
	JBufferedWriter outw = null;
	try {
	    outw = new JBufferedWriter(new FileWriter(outfile));
	    String name = outfile.getName();
	    String libraryDir = Prefwin.getLibraryDir(prefs);
	    int px = ((Integer)(opts_path_px.getValue())).intValue();
	    String confined = opts_path_confined.isSelected()?"conf":"";
	    appendFileToWriter(libraryDir+"/Simulations/Sim1DPath/1"+confined+".fe",
			       outw);
	    outw.writeln("parameter surface_tension = " + opts_path_surfacetension.getText() + ";");
	    outw.writeln("parameter cangle_bottom = " + opts_path_cangle.getText() + ";");
	    if (opts_path_confined.isSelected()) {
		outw.writeln("parameter cangle_top = " + opts_path_cangle_top.getText() + ";");
		outw.writeln("parameter topdist = " + opts_path_topdist.getText() + ";");
	    }
	    outw.writeln("parameter drop_volume = " + opts_path_vol.getText() + ";");
	    outw.writeln("parameter psx = " + opts_path_psx.getText() + ";");
	    outw.writeln("parameter psy = " + opts_path_psy.getText() + ";");
	    outw.writeln("parameter pg = " + opts_path_pgap.getText() + ";");
	    outw.writeln("parameter sl = " + opts_path_spikelength.getText() + ";");
	    outw.writeln("parameter drop_x = " + opts_path_spx.getText() + ";");
	    outw.writeln("parameter voltage = " + opts_path_voltage.getText() + ";");
	    outw.writeln("parameter epsilon = " + opts_path_epsilon.getText() + ";");
	    outw.writeln("parameter thickn = " + opts_path_thickness.getText() + ";");
	    outw.writeln("parameter numberofpads = " + px + ";");
	    for (int i = 1; i <= px; i++) {
		outw.writeln("parameter gLSV"+i+" = 0");
	    }
	    appendFileToWriter(libraryDir+"/Simulations/Sim1DPath/2"+confined+".fe",
			       outw);
	    for (int i = 0; i <= px-1; i++) {
		outw.writeln(((i == 0)?"":"+ ")+
			     "gLSV"+(i+1)+"*(x<pbl*"+i+"?0:"+
                     "((x<pbl*"+i+"+sl)?1/2/sl*(x-pbl*"+i+")^2:"+
        	     "((x<pbl*"+i+"+psx-sl)?sl/2+(x-pbl*"+i+"-sl):"+
                     "((x<pbl*"+i+"+psx)?sl/2+psx-2*sl-((x-(pbl*"+i+"+psx-sl))^2)/2/sl+x-(pbl*"+i+"+psx-sl):"+
			                    "(psx-sl"+
")))))" + ((i == px-1)?"))":"\\"));
	    }
	    appendFileToWriter(libraryDir+"/Simulations/Sim1DPath/3"+confined+".fe",
			       outw);
	    outw.writeln("prefix := \"" + name + "\";");
	    for (int i = 1; i <= px; i++) {
		outw.writeln("on"+i+" := { gLSV"+i+" := -0.5/thickn*epsilon*8.85e-12*voltage^2; set facet color red where padnr=="+i+"; redraw; }");
		outw.writeln("off"+i+" := { gLSV"+i+" := 0; set facet color lightgray where padnr=="+i+"; redraw; }");
	    }
	    appendFileToWriter(libraryDir+"/Simulations/Sim1DPath/4"+confined+".fe",
			       outw);
	    outw.close();
	}
	catch (IOException e) {
	    try { if (outw != null) outw.close();}
	    catch (IOException ee) {}
	    throw(e);
	}
    }

    Sim1DPath(Preferences myprefs, final JFrame outerframe) {
	/*               Simulation options for 1D    */
	super(outerframe);
	prefs = myprefs;
	oagb = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

 	GUI.setLayout(oagb);

	opts_path_confined =
	    makeCheckBox(oagb, GUI, "Confined droplet", false);
	opts_path_surfacetension =
	    makeTextField(oagb, GUI, "Surface tension [J/m^2]:", 20, "72e-3");
	opts_path_cangle =
	    makeTextField(oagb, GUI, "Contact angle bottom [deg]:", 20, "110");
	opts_path_vol =
	    makeTextField(oagb, GUI, "Droplet volume [m^3]:", 20, "1e-9");
	opts_path_voltage =
	    makeTextField(oagb, GUI, "Actuation voltage [V]:", 20, "40");
	opts_path_thickness =
	    makeTextField(oagb, GUI, "Layer thickness [m]:", 20, "1e-6");
	opts_path_epsilon =
	    makeTextField(oagb, GUI, "Rel. dielectric constant:", 20, "3");
	opts_path_px =
	    makeNumSpinner(oagb, GUI, "# Pads:", 1, 100, 5);
	opts_path_psx =
	    makeTextField(oagb, GUI, "Pad size x [m]:", 20, "1000e-6");
	opts_path_psy =
	    makeTextField(oagb, GUI, "Pad size y [m]:", 20, "1500e-6");
	opts_path_pgap =
	    makeTextField(oagb, GUI, "Pad gap [m]:", 20, "100e-6");
	opts_path_spikelength =
	    makeTextField(oagb, GUI, "Spike length [m]:", 20, "100e-6");
	opts_path_spx =
	    makeTextField(oagb, GUI, "Start position x [m]:", 20, "500e-6");

 	opts_path_cangle_topl = new JLabel("Contact angle top [deg]:");
 	opts_path_cangle_topb = Box.createRigidArea(new Dimension(12,1));
 	opts_path_cangle_top = new JTextField("110",20);
 	opts_path_topdistl = new JLabel("Distance to top [m]:");
 	opts_path_topdistb = Box.createRigidArea(new Dimension(12,1));
 	opts_path_topdist = new JTextField("1000e-6",20);

  	opts_path_confined.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent e) {
  		    if (e.getSource() == opts_path_confined) {
  			if (opts_path_confined.isSelected()) {
  			    GridBagConstraints c = new GridBagConstraints();
  			    c.fill = GridBagConstraints.BOTH;
  			    c.gridwidth = 1;
  			    c.gridx = 0;
  			    oagb.setConstraints(opts_path_cangle_topl, c);
  			    GUI.add(opts_path_cangle_topl, 4);
  			    c.gridx = GridBagConstraints.RELATIVE;
  			    c.gridwidth = GridBagConstraints.RELATIVE;
  			    oagb.setConstraints(opts_path_cangle_topb, c);
  			    GUI.add(opts_path_cangle_topb, 5);
  			    c.gridwidth = GridBagConstraints.REMAINDER;
  			    oagb.setConstraints(opts_path_cangle_top, c);
  			    GUI.add(opts_path_cangle_top, 6);
  			    c.fill = GridBagConstraints.BOTH;
  			    c.gridwidth = 1;
  			    c.gridx = 0;
  			    oagb.setConstraints(opts_path_topdistl, c);
  			    GUI.add(opts_path_topdistl, 7);
  			    c.gridx = GridBagConstraints.RELATIVE;
  			    c.gridwidth = GridBagConstraints.RELATIVE;
  			    oagb.setConstraints(opts_path_topdistb, c);
  			    GUI.add(opts_path_topdistb, 8);
  			    c.gridwidth = GridBagConstraints.REMAINDER;
  			    oagb.setConstraints(opts_path_topdist, c);
  			    GUI.add(opts_path_topdist, 9);
 			    outerframe.pack();
  			}
 			else {
 			    GUI.remove(opts_path_cangle_topl);
 			    GUI.remove(opts_path_cangle_topb);
 			    GUI.remove(opts_path_cangle_top);
 			    GUI.remove(opts_path_topdistl);
 			    GUI.remove(opts_path_topdistb);
 			    GUI.remove(opts_path_topdist);
 			    outerframe.pack();
 			}
 		    }
  		}
  	    });
    }

    public JComponent getEvolverGUI(final Evolverwin ev) {
	JPanel evolverGUI = new JPanel();
	evolverGUI.setLayout(new BoxLayout(evolverGUI, BoxLayout.Y_AXIS));

	JPanel padsonoff = new JPanel();
	final int px = ((Integer)(opts_path_px.getValue())).intValue();

	final JCheckBox[] b = new JCheckBox[px];
	for (int i=1; i <= px; i++) {
	    b[i-1] = new JCheckBox(i+" ", false);
	    final int ii = i;
	    if (i == px) { 
	    }
	    b[i-1].addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			try {
			    if(e.getSource() == b[ii-1] && ((JCheckBox)e.getSource()).isSelected()) {
				ev.writeToEvolver("on"+ii);
			    }
			    else {
				ev.writeToEvolver("off"+ii);
			    }
			}
			catch (Exception ee) {
			    JOptionPane.showMessageDialog(ev,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			}
		    }
		}
				 );
	    padsonoff.add(b[i-1]);
	}

	JScrollPane padsonoff_sp = new JScrollPane(padsonoff,
			    JScrollPane.VERTICAL_SCROLLBAR_NEVER,
			    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	evolverGUI.add(padsonoff_sp);


	JPanel controls = new JPanel();

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
	Component b0 = Box.createRigidArea(new Dimension(12,1));
	controls.add(b0);

 	controls.add(new JLabel("Voltage: "));
	final JTextField voltageTF = new JTextField(opts_path_voltage.getText(), 3);
	voltageTF.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == voltageTF ) {
			    try {
				ev.writeToEvolver("voltage := "+voltageTF.getText());
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
	controls.add(voltageTF);

	final JButton voltageToAll = new JButton("-> to all sel.");
	voltageToAll.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == voltageToAll ) {
			    try {
				ev.writeToEvolver("voltage := "+voltageTF.getText());
				for (int i=1; i <= px; i++) {
				    if (b[i-1].isSelected())
					ev.writeToEvolver("on"+i);
				}
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
	controls.add(voltageToAll);
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
	evolverGUI.add(controls);
	Dimension dc = controls.getPreferredSize();
	Dimension de = evolverGUI.getPreferredSize();
	de.setSize(dc.getWidth(), de.getHeight());
	evolverGUI.setPreferredSize(de);
	return (evolverGUI);
    }
}
