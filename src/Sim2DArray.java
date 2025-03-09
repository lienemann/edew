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
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.prefs.*;

public class Sim2DArray extends Simulation
{
    JCheckBox opts_array_confined;
    JTextField opts_array_surfacetension;
    JTextField opts_array_cangle;
    JTextField opts_array_cangle_top;
    JLabel     opts_array_cangle_topl;
    Component opts_array_cangle_topb;
    JTextField opts_array_vol;
    JSpinner opts_array_px;
    JSpinner opts_array_py;
    JTextField opts_array_psx;
    JTextField opts_array_psy;
    JTextField opts_array_spx;
    JTextField opts_array_spy;
    GridBagLayout oagb;
    Preferences prefs;

    public String toString() {
	return("2D array");
    }

    public void writefile(File outfile) throws IOException {
	System.out.println("Writing output file for 2D array with:");
	System.out.println("Confined droplet:     " +
			   opts_array_confined.isSelected());
	System.out.println("Surface tension:      " +
			   opts_array_surfacetension.getText());
	if (opts_array_confined.isSelected())
	    System.out.println("Contact angle top:    " +
			       opts_array_cangle_top.getText());
	System.out.println("Contact angle bottom: " +
			   opts_array_cangle.getText());
	System.out.println("Droplet volume:       " +
			   opts_array_vol.getText());
	System.out.println("# pads in x:          " +
			   opts_array_px.getValue());
	System.out.println("# pads in y:          " +
			   opts_array_py.getValue());
	System.out.println("Pad size x:           " +
			   opts_array_psx.getText());
	System.out.println("Pad size y:           " +
			   opts_array_psy.getText());
	System.out.println("Start position x:     " +
			   opts_array_spx.getText());
	System.out.println("Start position y:     " +
			   opts_array_spy.getText());
	JBufferedWriter outw = null;
	try {
	    outw = new JBufferedWriter(new FileWriter(outfile));
	    String name = outfile.getName();
	    String libraryDir = Prefwin.getLibraryDir(prefs);
	    appendFileToWriter(libraryDir+"/Simulations/Sim2DArray/1.fe",
			       outw);
	    outw.writeln("parameter prefix = \"" + name.substring(0, name.length()-3) + "\";");
	    outw.close();
	}
	catch (IOException e) {
	    try { if (outw != null) outw.close();}
	    catch (IOException ee) {}
	    throw(e);
	}
    }

    Sim2DArray(Preferences myprefs, final JFrame outerframe) {
	/*               Simulation options for 2D    */
	super(outerframe);
	prefs = myprefs;
	oagb = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	GUI.setLayout(oagb);

	opts_array_confined =
	    makeCheckBox(oagb, GUI, "Confined droplet", false);
	opts_array_surfacetension =
	    makeTextField(oagb, GUI, "Surface tension:", 20, "72e-3");
	opts_array_cangle =
	    makeTextField(oagb, GUI, "Contact angle bottom:", 20, "110");
	opts_array_vol =
	    makeTextField(oagb, GUI, "Droplet Volume:", 20, "1e-9");
	opts_array_px =
	    makeNumSpinner(oagb, GUI, "# Pads in x:", 1, 100, 5);
	opts_array_py =
	    makeNumSpinner(oagb, GUI, "# Pads in y:", 1, 100, 5);
	opts_array_psx =
	    makeTextField(oagb, GUI, "Pad size x:", 20, "300e-6");
	opts_array_psy =
	    makeTextField(oagb, GUI, "Pad size y:", 20, "300e-6");
	opts_array_spx =
	    makeTextField(oagb, GUI, "Start position x:", 20, "150e-6");
	opts_array_spy =
	    makeTextField(oagb, GUI, "Start position y:", 20, "150e-6");

	opts_array_cangle_topl = new JLabel("Contact angle top:");
	opts_array_cangle_topb = Box.createRigidArea(new Dimension(12,1));
	opts_array_cangle_top = new JTextField("110",20);

 	opts_array_confined.addActionListener(new ActionListener() {
 		public void actionPerformed(ActionEvent e) {
 		    if (e.getSource() == opts_array_confined) {
 			if (opts_array_confined.isSelected()) {
 			    GridBagConstraints c = new GridBagConstraints();
 			    c.fill = GridBagConstraints.BOTH;
 			    c.gridwidth = 1;
 			    c.gridx = 0;
 			    oagb.setConstraints(opts_array_cangle_topl, c);
 			    GUI.add(opts_array_cangle_topl, 4);
 			    c.gridx = GridBagConstraints.RELATIVE;
 			    c.gridwidth = GridBagConstraints.RELATIVE;
 			    oagb.setConstraints(opts_array_cangle_topb, c);
 			    GUI.add(opts_array_cangle_topb, 5);
 			    c.gridwidth = GridBagConstraints.REMAINDER;
 			    oagb.setConstraints(opts_array_cangle_top, c);
 			    GUI.add(opts_array_cangle_top, 6);
			    outerframe.pack();
 			}
			else {
			    GUI.remove(opts_array_cangle_topl);
			    GUI.remove(opts_array_cangle_topb);
			    GUI.remove(opts_array_cangle_top);
			    outerframe.pack();
			}
		    }
 		}
 	    });
    }

    public JComponent getEvolverGUI(Evolverwin ev) {
	return (new JPanel());
    }

}
