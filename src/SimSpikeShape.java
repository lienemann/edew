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

public class SimSpikeShape extends Simulation
{
    JCheckBox opts_spikeshape_confined;
    JTextField opts_spikeshape_surfacetension;
    JTextField opts_spikeshape_cangle;
    JTextField opts_spikeshape_cangle_top;
    JLabel     opts_spikeshape_cangle_topl;
    Component opts_spikeshape_cangle_topb;
    JTextField opts_spikeshape_topdist;
    JLabel opts_spikeshape_topdistl;
    Component opts_spikeshape_topdistb;
    JTextField opts_spikeshape_vol;
    JSpinner opts_spikeshape_px;
    JTextField opts_spikeshape_psx;
    JTextField opts_spikeshape_psy;
    JTextField opts_spikeshape_pgap;
    JTextField opts_spikeshape_spikelength;
    JTextField opts_spikeshape_spx;
    JTextField opts_spikeshape_voltage;
    JTextField opts_spikeshape_epsilon;
    JTextField opts_spikeshape_thickness;
    JRadioButton[] opts_spikeshape_shapes;
    String opts_spikeshape_chosenshape;
    JRadioButton opts_spikeshape_own;
    JTextField opts_spikeshape_ownf;
    JLabel opts_spikeshape_picture;
    JTextField opts_spikeshape_par1;
    JTextField opts_spikeshape_par2;
    JTextField opts_spikeshape_par3;
    GridBagLayout oagb;
    Preferences prefs;

    public String toString() {
	return("Optimization of Spike Shapes");
    }

    protected String getfunc(String filepath)  throws IOException {
	BufferedReader bf = null;
	bf = new BufferedReader(new FileReader(filepath));
	String s;
	if ((s = bf.readLine()) == null) {
	    if (bf != null) bf.close();
	    throw(new IOException("Could not read function from "+filepath));
	}
	else
	    return(s);
    }

    protected static String xpat = "(^|\\b)x($|\\b)";
    public void writefile(File outfile) throws IOException {
	System.out.println("Writing output file for Optimization of Spike Shapes with:");
	System.out.println("Confined droplet:     " +
			   opts_spikeshape_confined.isSelected());
	System.out.println("Surface tension:      " +
			   opts_spikeshape_surfacetension.getText());
	if (opts_spikeshape_confined.isSelected()) {
	    System.out.println("Contact angle top:    " +
			       opts_spikeshape_cangle_top.getText());
	    System.out.println("Top distance:         " +
			       opts_spikeshape_topdist.getText());
	}
	System.out.println("Contact angle bottom: " +
			   opts_spikeshape_cangle.getText());
	System.out.println("Droplet volume:       " +
			   opts_spikeshape_vol.getText());
	System.out.println("# pads:                " +
			   opts_spikeshape_px.getValue());
	System.out.println("Pad size x:           " +
			   opts_spikeshape_psx.getText());
	System.out.println("Pad size y:           " +
			   opts_spikeshape_psy.getText());
	System.out.println("Pad gap:             " +
			   opts_spikeshape_pgap.getText());
	System.out.println("Spike length:        " +
			   opts_spikeshape_spikelength.getText());
	System.out.println("Start position:      " +
			   opts_spikeshape_spx.getText());
	System.out.println("Actuation voltage:   " +
			   opts_spikeshape_voltage.getText());
	System.out.println("Dielectric constant: " +
			   opts_spikeshape_epsilon.getText());
	System.out.println("Layer thickness:     " +
			   opts_spikeshape_thickness.getText());
	System.out.println("Parameter par1:      " +
			   opts_spikeshape_par1.getText());
	System.out.println("Parameter par2:      " +
			   opts_spikeshape_par2.getText());
	System.out.println("Parameter par3:      " +
			   opts_spikeshape_par3.getText());
	System.out.println("Shape:               " +
			   opts_spikeshape_chosenshape);
	JBufferedWriter outw = null;
	try {
	    outw = new JBufferedWriter(new FileWriter(outfile));
	    String name = outfile.getName();
	    String libraryDir = Prefwin.getLibraryDir(prefs);
	    int px = ((Integer)(opts_spikeshape_px.getValue())).intValue();
	    String confined = opts_spikeshape_confined.isSelected()?"conf":"";
	    appendFileToWriter(libraryDir+"/Simulations/SimSpikeShape/1"+confined+".fe",
			       outw);
	    outw.writeln("parameter surface_tension = " + opts_spikeshape_surfacetension.getText() + ";");
	    outw.writeln("parameter cangle_bottom = " + opts_spikeshape_cangle.getText() + ";");
	    if (opts_spikeshape_confined.isSelected()) {
		outw.writeln("parameter cangle_top = " + opts_spikeshape_cangle_top.getText() + ";");
		outw.writeln("parameter topdist = " + opts_spikeshape_topdist.getText() + ";");
	    }
	    outw.writeln("parameter drop_volume = " + opts_spikeshape_vol.getText() + ";");
	    outw.writeln("parameter psx = " + opts_spikeshape_psx.getText() + ";");
	    outw.writeln("parameter psy = " + opts_spikeshape_psy.getText() + ";");
	    outw.writeln("parameter pg = " + opts_spikeshape_pgap.getText() + ";");
	    outw.writeln("parameter sl = " + opts_spikeshape_spikelength.getText() + ";");
	    outw.writeln("parameter drop_x = " + opts_spikeshape_spx.getText() + ";");
	    outw.writeln("parameter voltage = " + opts_spikeshape_voltage.getText() + ";");
	    outw.writeln("parameter epsilon = " + opts_spikeshape_epsilon.getText() + ";");
	    outw.writeln("parameter thickn = " + opts_spikeshape_thickness.getText() + ";");
	    if (!opts_spikeshape_par1.getText().trim().equals(""))
		outw.writeln("parameter par1 = " +
			     opts_spikeshape_par1.getText() + ";");
	    if (!opts_spikeshape_par2.getText().trim().equals(""))
		outw.writeln("parameter par2 = " +
			     opts_spikeshape_par2.getText() + ";");
	    if (!opts_spikeshape_par3.getText().trim().equals(""))
		outw.writeln("parameter par3 = " +
			     opts_spikeshape_par3.getText() + ";");
	    outw.writeln("parameter numberofpads = " + px + ";");
	    for (int i = 1; i <= px; i++) {
		outw.writeln("parameter gLSV"+i+" = 0");
	    }
	    appendFileToWriter(libraryDir+"/Simulations/SimSpikeShape/2"+confined+".fe",
			       outw);
	    String f;
	    if (opts_spikeshape_chosenshape.equals("Own_shape")) 
		f = opts_spikeshape_ownf.getText();
	    
	    else
		f = getfunc(libraryDir+
				   "/Simulations/SimSpikeShape/Shape"+
				   opts_spikeshape_chosenshape
				   +".fkt");

	    for (int i = 0; i <= px-1; i++) {
// 			outw.writeln(((i == 0)?"":"+ ")
// 	         +"gLSV"+(i+1)+"*((x<pbl*"+i+")?0:"
//                                +"((x<pbl*"+i+"+sl)?("
// 		               +f.replaceAll("(^|\\b)x($|\\b)","((x-pbl*"+i+")/sl)")
// 			        +"):"
//         	               +"((x<pbl*"+i+"+psx-sl)?1:"
//                                +"((x<pbl*"+i+"+psx)?("
// 		               +f.replaceAll("(^|\\b)x($|\\b)","((pbl*"+i+"+psx-x)/sl)")
// 			        +"):"
// 			     +"0))))" + ((i == px-1)?")":"\\"));
		String f1 = "sl*("+f.replaceAll(xpat,"1")+")";
		outw.writeln(((i == 0)?"":"+")+" gLSV"+(i+1)+"*"
		  +"( (x<pbl*"+i+")?0:\\\n"
                  +"( (x<pbl*"+i+"+sl)?(sl*("
		   +f.replaceAll(xpat,"((x-pbl*"+i+")/sl)")
				     +")):\\\n"
		  +"( (x<pbl*"+i+"+psx-sl)?("+f1
		   +"+x-(pbl*"+i+"+sl)"
			                 +"):\\\n"
                  +"( (x<pbl*"+i+"+psx)?(2*"+f1+"+psx-2*sl-sl*("
		   +f.replaceAll(xpat,"((pbl*"+i+"+psx-x)/sl)")
			              +")):\\\n"
		  +"( 2*"+f1+"+psx-2*sl"
                  +")))))" + ((i == px-1)?")":"\\"));
	    }
	    appendFileToWriter(libraryDir+"/Simulations/SimSpikeShape/3"+confined+".fe",
			       outw);
	    outw.writeln("prefix := \"" + name + "\";");
	    for (int i = 1; i <= px; i++) {
		outw.writeln("on"+i+" := { gLSV"+i+" := -0.5/thickn*epsilon*8.85e-12*voltage^2; set facet color red where padnr=="+i+"; redraw; }");
		outw.writeln("off"+i+" := { gLSV"+i+" := 0; set facet color lightgray where padnr=="+i+"; redraw; }");
	    }
	    appendFileToWriter(libraryDir+"/Simulations/SimSpikeShape/4"+confined+".fe",
			       outw);
	    outw.close();
	}
	catch (IOException e) {
	    try { if (outw != null) outw.close();}
	    catch (IOException ee) {}
	    throw(e);
	}
    }

    SimSpikeShape(Preferences myprefs, final JFrame outerframe) {
	/*               Simulation options for SpikeShape    */
	super(outerframe);
	prefs = myprefs;
	oagb = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

 	GUI.setLayout(oagb);

	opts_spikeshape_confined =
	    makeCheckBox(oagb, GUI, "Confined droplet", false);
	opts_spikeshape_surfacetension =
	    makeTextField(oagb, GUI, "Surface tension [J/m^2]:", 20, "72e-3");
	opts_spikeshape_cangle =
	    makeTextField(oagb, GUI, "Contact angle bottom [deg]:", 20, "110");
	opts_spikeshape_vol =
	    makeTextField(oagb, GUI, "Droplet volume [m^3]:", 20, "1e-9");
	opts_spikeshape_voltage =
	    makeTextField(oagb, GUI, "Actuation voltage [V]:", 20, "10");
	opts_spikeshape_thickness =
	    makeTextField(oagb, GUI, "Layer thickness [m]:", 20, "1e-6");
	opts_spikeshape_epsilon =
	    makeTextField(oagb, GUI, "Rel. dielectric constant:", 20, "3");
	opts_spikeshape_px =
	    makeNumSpinner(oagb, GUI, "# Pads:", 1, 100, 2);
	opts_spikeshape_psx =
	    makeTextField(oagb, GUI, "Pad size x [m]:", 20, "1000e-6");
	opts_spikeshape_psy =
	    makeTextField(oagb, GUI, "Pad size y [m]:", 20, "1500e-6");
	opts_spikeshape_pgap =
	    makeTextField(oagb, GUI, "Pad gap [m]:", 20, "100e-6");
	opts_spikeshape_spikelength =
	    makeTextField(oagb, GUI, "Spike length [m]:", 20, "100e-6");
	opts_spikeshape_spx =
	    makeTextField(oagb, GUI, "Start position x [m]:", 20, "500e-6");
	opts_spikeshape_par1 =
	    makeTextField(oagb, GUI, "Parameter par1:", 20, "");
	opts_spikeshape_par2 =
	    makeTextField(oagb, GUI, "Parameter par2:", 20, "");
	opts_spikeshape_par3 =
	    makeTextField(oagb, GUI, "Parameter par3:", 20, "");

	GUI.add(Box.createRigidArea(new Dimension(1,12)));
	
 	opts_spikeshape_cangle_topl = new JLabel("Contact angle top [deg]:");
 	opts_spikeshape_cangle_topb = Box.createRigidArea(new Dimension(12,1));
 	opts_spikeshape_cangle_top = new JTextField("110",20);
 	opts_spikeshape_topdistl = new JLabel("Distance to top [m]:");
 	opts_spikeshape_topdistb = Box.createRigidArea(new Dimension(12,1));
 	opts_spikeshape_topdist = new JTextField("1000e-6",20);

  	opts_spikeshape_confined.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent e) {
  		    if (e.getSource() == opts_spikeshape_confined) {
  			if (opts_spikeshape_confined.isSelected()) {
  			    GridBagConstraints c = new GridBagConstraints();
			    c.fill = GridBagConstraints.BOTH;
			    c.anchor = GridBagConstraints.NORTHWEST;
			    c.gridheight = 1;
			    c.weightx = 0;
			    c.weighty = 0;
  			    c.fill = GridBagConstraints.BOTH;
  			    c.gridwidth = 1;
  			    c.gridx = 0;
  			    oagb.setConstraints(opts_spikeshape_cangle_topl, c);
  			    GUI.add(opts_spikeshape_cangle_topl, 4);
  			    c.gridx = GridBagConstraints.RELATIVE;
  			    c.gridwidth = GridBagConstraints.RELATIVE;
  			    oagb.setConstraints(opts_spikeshape_cangle_topb, c);
  			    GUI.add(opts_spikeshape_cangle_topb, 5);
  			    c.gridwidth = GridBagConstraints.REMAINDER;
			    c.weightx = 1;
  			    oagb.setConstraints(opts_spikeshape_cangle_top, c);
  			    GUI.add(opts_spikeshape_cangle_top, 6);
  			    c.fill = GridBagConstraints.BOTH;
  			    c.gridwidth = 1;
  			    c.gridx = 0;
			    c.weightx = 0;
  			    oagb.setConstraints(opts_spikeshape_topdistl, c);
  			    GUI.add(opts_spikeshape_topdistl, 7);
  			    c.gridx = GridBagConstraints.RELATIVE;
  			    c.gridwidth = GridBagConstraints.RELATIVE;
  			    oagb.setConstraints(opts_spikeshape_topdistb, c);
  			    GUI.add(opts_spikeshape_topdistb, 8);
  			    c.gridwidth = GridBagConstraints.REMAINDER;
			    c.weightx = 1;
  			    oagb.setConstraints(opts_spikeshape_topdist, c);
  			    GUI.add(opts_spikeshape_topdist, 9);
 			    outerframe.pack();
  			}
 			else {
 			    GUI.remove(opts_spikeshape_cangle_topl);
 			    GUI.remove(opts_spikeshape_cangle_topb);
 			    GUI.remove(opts_spikeshape_cangle_top);
 			    GUI.remove(opts_spikeshape_topdistl);
 			    GUI.remove(opts_spikeshape_topdistb);
 			    GUI.remove(opts_spikeshape_topdist);
 			    outerframe.pack();
 			}
 		    }
  		}
  	    });
	JPanel shapes = new JPanel();
	shapes.setLayout(new BorderLayout());
	JPanel shapes_bt = new JPanel();
	shapes_bt.setLayout(new BoxLayout(shapes_bt, 
				       BoxLayout.Y_AXIS));
	JScrollPane shapes_sp = new JScrollPane(shapes,
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	String[] shapefiles =
	    new File(Prefwin.getLibraryDir(prefs)+
		     "/Simulations/SimSpikeShape").
	    list(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		      return(name.substring(Math.max(0,name.length()-4),name.length()).
			       equalsIgnoreCase(".fkt")&&
			       name.substring(0,Math.min(5,name.length())).
			       equalsIgnoreCase("shape")
			       );
		    }
		});

	
	ButtonGroup shapes_bg = new ButtonGroup();
	ActionListener shapes_al = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    opts_spikeshape_chosenshape = e.getActionCommand();
		    if (e.getSource()==opts_spikeshape_own) {
			opts_spikeshape_ownf.setEnabled(true);
		    }
		    else
			{
			    opts_spikeshape_ownf.setEnabled(false);
			}
		    opts_spikeshape_picture.
			setIcon(new ImageIcon(Prefwin.
					      getLibraryDir(prefs)+
			      "/Simulations/SimSpikeShape/Shape"+
					      opts_spikeshape_chosenshape+
					      ".png"));
		}
	    };

	if (shapefiles!=null) {
	    int l = shapefiles.length;
	    opts_spikeshape_shapes = new JRadioButton[l];
	    for (int i = 0; i < l; i++) {
		String sname = shapefiles[i].
		    substring(5,shapefiles[i].length()-4);
		opts_spikeshape_shapes[i] =
		    new JRadioButton(sname);
		opts_spikeshape_shapes[i].setActionCommand(sname);
		opts_spikeshape_shapes[i].addActionListener(shapes_al);
		shapes_bg.add(opts_spikeshape_shapes[i]);
		shapes_bt.add(opts_spikeshape_shapes[i]);	   
	    }
	    
	    opts_spikeshape_own = new JRadioButton("Own shape (please "+
		      "enter F(x)=integral_0^x[f(X) dX] below)");
	    opts_spikeshape_own.setActionCommand("Own_Shape");
	    opts_spikeshape_own.addActionListener(shapes_al);
	    shapes_bg.add(opts_spikeshape_own);
	    shapes_bt.add(opts_spikeshape_own);	   
	    
	    opts_spikeshape_ownf = new JTextField("0.5*x", 20);
	    opts_spikeshape_ownf.setEnabled(false);
	    
	    shapes_bt.add(opts_spikeshape_ownf);	   
	    
	    
	    shapes.add(shapes_bt, BorderLayout.WEST);
	    
	    shapes.add(Box.createRigidArea(new Dimension(12,1)), BorderLayout.CENTER);
	    
	    opts_spikeshape_chosenshape = opts_spikeshape_shapes[0].getText();
	    opts_spikeshape_picture = new JLabel(new ImageIcon(Prefwin.
				      getLibraryDir(prefs)+
		 "/Simulations/SimSpikeShape/Shape"+
			   opts_spikeshape_chosenshape +".png"));
	    
	    opts_spikeshape_shapes[0].setSelected(true);	
	    
	    shapes.add(opts_spikeshape_picture, BorderLayout.EAST);
	    
	    shapes_sp.setBorder(BorderFactory.createEmptyBorder());
	    shapes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Electrode spike shape"));

	    c.gridx = 0;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.gridheight = GridBagConstraints.REMAINDER;
	    c.fill = GridBagConstraints.BOTH;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    c.weighty = 1;
	    oagb.setConstraints(shapes_sp, c);
	    GUI.add(shapes_sp);
	} else
	    JOptionPane.showMessageDialog(outerframe,
					  "Could not find library files for SpikeShape Simulation.\nPlease check library path and restart program.",
					  "Problem while initializing SpikeShape Simulation",
					  JOptionPane.ERROR_MESSAGE);
    }

    public JComponent getEvolverGUI(final Evolverwin ev) {
	JPanel evolverGUI = new JPanel();
	evolverGUI.setLayout(new BoxLayout(evolverGUI, BoxLayout.Y_AXIS));

	JPanel padsonoff = new JPanel();
	final int px = ((Integer)(opts_spikeshape_px.getValue())).intValue();

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

	JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

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
	final JTextField voltageTF = new JTextField(opts_spikeshape_voltage.getText(), 3);
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
	evolverGUI.add(controls);

	JPanel controls2 = new JPanel(new FlowLayout(FlowLayout.LEFT));

	final JCheckBox fixcentrx = new JCheckBox("Centroid fixed");
	fixcentrx.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    try {
			if(e.getSource() == fixcentrx && ((JCheckBox)e.getSource()).isSelected()) {
			    ev.writeToEvolver("fix centrx; centrx.target := centrx.value; print centrx.value");
			}
			else {
			    ev.writeToEvolver("unfix centrx");
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
	controls2.add(fixcentrx);
	b0 = Box.createRigidArea(new Dimension(12,1));
	controls2.add(b0);
	JLabel fixto = new JLabel("Fix to:");
	controls2.add(fixto);
	final JTextField centrxsetTF =
	    new JTextField(opts_spikeshape_spx.getText(), 6);
	centrxsetTF.addActionListener( new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == centrxsetTF ) {
			    try {
				ev.writeToEvolver("fix centrx; centrx.target := "+centrxsetTF.getText());
				fixcentrx.setSelected(true);
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
	controls2.add(centrxsetTF);
	b0 = Box.createRigidArea(new Dimension(12,1));
	controls2.add(b0);
	final JButton centrxplpl = new JButton("++");
	final JButton centrxpl = new JButton("+");
	final JTextField centrxpmTF = new JTextField("5e-6", 6);
	final JButton centrxmns = new JButton("-");
	final JButton centrxmnsmns = new JButton("--");
	ActionListener centrxpmAL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    try {
			String sp="";
			if (saveps.isSelected())
			    sp=" saveps;";
			if (e.getSource() == centrxpl ) {
			    ev.writeToEvolver("fix centrx; centrx.target := centrx.value+"+centrxpmTF.getText()+"; print centrx.target; docstep;"+sp);
			    fixcentrx.setSelected(true);
			}
			else if (e.getSource() == centrxmns ) {
			    ev.writeToEvolver("fix centrx; centrx.target := centrx.value-"+centrxpmTF.getText()+"; print centrx.target; docstep;"+sp);
			    fixcentrx.setSelected(true);
			}
			else if (e.getSource() == centrxplpl) {
			    fixcentrx.setSelected(true);
			    ev.writeToEvolver("fix centrx; centrx.target := centrx.value; { centrx.target += "+centrxpmTF.getText()+"; print centrx.target; docstep"+sp+" } "+dosteps.getText());
			}
			else if (e.getSource() == centrxmnsmns) {
			    fixcentrx.setSelected(true);
			    ev.writeToEvolver("fix centrx; centrx.target := centrx.value; { centrx.target -= "+centrxpmTF.getText()+"; print centrx.target; docstep"+sp+" } "+dosteps.getText());
			}
		    }
		    catch (Exception ee) {
			JOptionPane.showMessageDialog(ev,
			  ee.getMessage(),
			  "Problem with input to evolver",
			  JOptionPane.ERROR_MESSAGE);
		    }
		}
	    };
	centrxpl.addActionListener(centrxpmAL);
	centrxmns.addActionListener(centrxpmAL);
	centrxplpl.addActionListener(centrxpmAL);
	centrxmnsmns.addActionListener(centrxpmAL);
	controls2.add(centrxmnsmns);
	controls2.add(centrxmns);
	controls2.add(centrxpmTF);
	controls2.add(centrxpl);
	controls2.add(centrxplpl);
	evolverGUI.add(controls2);
	Dimension dc = controls.getPreferredSize();
	Dimension dc2 = controls2.getPreferredSize();
	Dimension de = evolverGUI.getPreferredSize();
	de.setSize(Math.max(dc.getWidth(),dc2.getWidth()),
		   de.getHeight());
	evolverGUI.setPreferredSize(de);

	//TODO
	return (evolverGUI);
    }
}
