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


// import java.util.*;
// import java.util.prefs.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class Evolverwin extends JFrame {
    JTextField inp;
    JTextArea outp;
    JButton goButton;
    Evolverwin me;
    JScrollBar scrollb;
    JCheckBox showCB;
    JCheckBox quietgoCB;
    JButton redrawButton;
    JButton refineButton;
    JCheckBox autoDispCB;
    JButton clearButton;
    Process proc = null;
    JBufferedWriter outpw;
    BufferedReader ro = null;
    BufferedReader re = null;
    ConnectStdInOut conn = null;
    JBufferedWriter logwin = null;
    JBufferedWriter logwall = null;
    JFrame showcontrols;
    JButton sccpl, sccpr, sccpu, sccpd, sccpc, 
	sccpC, sccpz, sccps, sccpm, sccpR,
	sccpe, sccpv, sccpH, sccpal, sccpar, sccpau, sccpad;

    protected JButton makeEvolverButton(String text, final String command) {
	final JButton b = new JButton(text);
	b.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent e) {
		    try {
			if (e.getSource() == b) {
				writeToEvolver(command);
			}
		    }
		    catch (Exception ee) {
			JOptionPane.showMessageDialog(me,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);

		    }
		}
	    }
			       );
	return(b);
    }

    protected void addToGb(GridBagLayout g, JComponent c, Container p,
			   int gw, int place, int max) {
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.gridx = (place == 1)? 0 : GridBagConstraints.RELATIVE;
	gbc.gridwidth = (place == max) ? GridBagConstraints.REMAINDER:
	    ((place == max-1) ? GridBagConstraints.RELATIVE : gw);
	gbc.fill = GridBagConstraints.BOTH;
	gbc.weightx = 0;
	gbc.anchor = GridBagConstraints.WEST;
	g.setConstraints(c, gbc);
	p.add(c);

    }

    public void killproc() {
	try { if (conn != null) conn.stopme(); }
	catch (Exception ee) { System.out.println(ee.getMessage()); }
	try { Thread.sleep(200);
	if (ro != null) ro.close(); }
	catch (Exception ee) { System.out.println(ee.getMessage()); }
	try { if (re != null) re.close(); }
	catch (Exception ee) { System.out.println(ee.getMessage()); }
	try { if (outpw != null) outpw.close(); }
	catch (Exception ee) { System.out.println(ee.getMessage()); }
	try { if (logwin != null) logwin.close(); }
	catch (Exception ee) { System.out.println(ee.getMessage()); }
	try { if (logwall != null) logwall.close(); }
	catch (Exception ee) { System.out.println(ee.getMessage()); }
	try { Thread.sleep(100);
	if (proc != null) proc.destroy(); }
	catch (Exception ee) { System.out.println(ee.getMessage()); }
    }
    
    public void writeToEvolver(String s) throws IOException {
	outpw.writeln(s);
	outpw.flush();
	outp.append(s+"\n");
	logwin.writeln(s);
	logwall.writeln(s);
    }

    Evolverwin(String evolverBinary, String runPath, String filename, Simulation sim) {
	JPanel p = new JPanel();
	GridBagLayout g = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	me = this;
	setTitle("Surface Evolver control");

	p.setLayout(g);
	p.setBorder(BorderFactory.createEmptyBorder(12,12,11,11));

	outp = new JTextArea(filename+"\n\n", 15, 80);
	outp.setEditable(false);
	outp.setLineWrap(false);
	outp.setFont(new Font("Monospaced", Font.PLAIN, 12));

	JScrollPane outpscr = new JScrollPane(outp, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	outpscr.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
		       "Evolver output"));
	scrollb = outpscr.getVerticalScrollBar();

	c.gridx = 0;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.fill =  GridBagConstraints.BOTH;
	c.weightx = 1;
	c.weighty = 1;
	g.setConstraints(outpscr, c);
	p.add(outpscr);

	Component b0 = Box.createRigidArea(new Dimension(1,5));
	c.gridx = 0;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.fill = GridBagConstraints.NONE;
	c.weightx = 0;
	c.weighty = 0;
	g.setConstraints(b0, c);
	p.add(b0);

	JLabel l = new JLabel("Evolver input:");
	Component b = Box.createRigidArea(new Dimension(12,1));
	inp = new JTextField();

	c.gridx = 0;
	c.gridwidth = 1;
	c.weightx = 0;
	c.fill = GridBagConstraints.NONE;
	g.setConstraints(l, c);
	p.add(l);

	c.gridx = GridBagConstraints.RELATIVE;
	c.gridwidth = 1;
	c.fill = GridBagConstraints.NONE;
	c.weightx = 0;
	g.setConstraints(b, c);
	p.add(b);

	c.gridx = GridBagConstraints.RELATIVE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 1;
	g.setConstraints(inp, c);
	p.add(inp);

//	goButton = new JButton("Go");
	inp.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == inp) {
			try {
			    String t = inp.getText();
			    writeToEvolver(t);
			    Thread.sleep(50);
			    inp.setText("");
			    scrollb.setValue(scrollb.getMaximum()-scrollb.getVisibleAmount());
			}
			catch (Exception ee) {
			    JOptionPane.showMessageDialog(me,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);

			}
		    }
		}
	    });
// 	c.gridx = GridBagConstraints.RELATIVE;
// 	c.gridwidth = GridBagConstraints.REMAINDER;
// 	//	c.gridheight = GridBagConstraints.RELATIVE;
// 	c.fill = GridBagConstraints.NONE;
// 	c.weightx = 0;
// 	g.setConstraints(goButton, c);
// 	p.add(goButton);

	clearButton = new JButton("Clear");
	clearButton.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == clearButton) {
			    outp.setText(null);
			}
		}
	    }
			       );
	c.gridx = GridBagConstraints.RELATIVE;
	c.gridwidth = GridBagConstraints.REMAINDER;
	//	c.gridheight = GridBagConstraints.RELATIVE;
	c.fill = GridBagConstraints.NONE;
	c.weightx = 0;
	c.anchor = GridBagConstraints.EAST;
	g.setConstraints(clearButton, c);
	p.add(clearButton);

// 	b0 = Box.createRigidArea(new Dimension(1,5));
// 	c.gridx = GridBagConstraints.RELATIVE;
// 	c.gridwidth = 1;
// 	c.fill = GridBagConstraints.NONE;
// 	c.weightx = 0;
// 	c.weighty = 0;
// 	g.setConstraints(b0, c);
// 	p.add(b0);

	JPanel ec = new JPanel();
	ec.setLayout(new BoxLayout(ec, BoxLayout.X_AXIS));
	showCB = new JCheckBox("Show", true);
	showCB.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent e) {
		    try {
			if (e.getSource() == showCB) {
			    if (showCB.isSelected()) {
				writeToEvolver("showq; showon:=1");
				showcontrols.show();
			    }
			    else {
				writeToEvolver("close_show; showon:=0");
				showcontrols.hide();
			    }
			}
		    }
		    catch (Exception ee) {
			JOptionPane.showMessageDialog(me,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);

		    }
		}
	    }
			       );
	ec.add(showCB);

	b0 = Box.createRigidArea(new Dimension(12,1));
	ec.add(b0);

	quietgoCB = new JCheckBox("Quietgo", true);
	quietgoCB.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent e) {
		    try {
			if (e.getSource() == quietgoCB) {
			    if (quietgoCB.isSelected()) {
				writeToEvolver("quietgo on");
			    }
			    else {
				writeToEvolver("quietgo off");
			    }
			}
		    }
		    catch (Exception ee) {
			JOptionPane.showMessageDialog(me,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);

		    }
		}
	    }
			       );
	ec.add(quietgoCB);

	b0 = Box.createRigidArea(new Dimension(12,1));
	ec.add(b0);

	autoDispCB = new JCheckBox("Auto Redraw", false);
	autoDispCB.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent e) {
		    try {
			if (e.getSource() == autoDispCB) {
			    if (autoDispCB.isSelected()) {
				writeToEvolver("autodisplay on");
			    }
			    else {
				writeToEvolver("autodisplay off");
			    }
			}
		    }
		    catch (Exception ee) {
			JOptionPane.showMessageDialog(me,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);

		    }
		}
	    }
			       );
	ec.add(autoDispCB);
	
	b0 = Box.createRigidArea(new Dimension(12,1));
	ec.add(b0);

	redrawButton = new JButton("Redraw");
	redrawButton.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == redrawButton) {
			    try {
				if (showCB.isSelected())
				    writeToEvolver("showq;");
			    }
			    catch (Exception ee) {
				JOptionPane.showMessageDialog(me,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			    }
			}
		}
	    });

	ec.add(redrawButton);

	b0 = Box.createRigidArea(new Dimension(12,1));
	ec.add(b0);

	refineButton = new JButton("Refine");
	refineButton.addActionListener(new ActionListener() {
  		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == refineButton) {
			    try {
				writeToEvolver("rr;");
			    }
			    catch (Exception ee) {
				JOptionPane.showMessageDialog(me,
					  ee.getMessage(),
					  "Problem with input to evolver",
					  JOptionPane.ERROR_MESSAGE);
			    }
			}
		}
	    });

	ec.add(refineButton);

// 	b0 = Box.createRigidArea(new Dimension(12,1));
// 	ec.add(b0);

	c.gridx = GridBagConstraints.RELATIVE;
	c.gridwidth = GridBagConstraints.REMAINDER;
	//	c.gridheight = GridBagConstraints.RELATIVE;
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 1;
	c.anchor = GridBagConstraints.WEST;
	g.setConstraints(ec, c);
	p.add(ec);

	b0 = Box.createRigidArea(new Dimension(1,5));
	p.add(b0);
	pack();

	JComponent simGUI = sim.getEvolverGUI(this);
	c.gridx = 0;
	c.gridwidth = GridBagConstraints.REMAINDER;
	//	c.gridheight = 1;
	c.fill = GridBagConstraints.BOTH;
	c.weighty = 0;
	c.weightx = 1;
	g.setConstraints(simGUI, c);
	//Dimension dmin = simGUI.getPreferredSize();
	//simGUI.setMinimumSize(dmin);

	//dmin.width = inp.getWidth();
	//simGUI.setPreferredSize(dmin);
	p.add(simGUI);

	addWindowListener( new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    try {
			writeToEvolver("q");
			writeToEvolver("q");
			writeToEvolver("q");
		    }
		    catch (Exception ee) {}
		    showcontrols.dispose();
		    killproc();
		}
	    });

	getContentPane().add(p);
	getRootPane().setDefaultButton(goButton);
	pack();

	showcontrols = new JFrame("Display controls");
	Container sccp = showcontrols.getContentPane();

	GridBagLayout sccpgb = new GridBagLayout();
	sccp.setLayout(sccpgb);

	// cw   ^ ccw e
	// <   . O  > v
	// R    v   C H
	sccpc = makeEvolverButton("CW","show_trans \"c\"; showq;");
	addToGb(sccpgb, sccpc, sccp, 1, 1, 5);
	sccpu = makeEvolverButton("(^)","show_trans \"u\"; showq;");
	addToGb(sccpgb, sccpu, sccp, 2, 2, 5);
	sccpC = makeEvolverButton("CCW","show_trans \"C\"; showq;");
	addToGb(sccpgb, sccpC, sccp, 1, 4, 5);
	sccpe = makeEvolverButton("Toggle edges","show_trans \"e\"; showq;");
	addToGb(sccpgb, sccpe, sccp, 1, 5, 5);

 	sccpl = makeEvolverButton("(<)","show_trans \"l\"; showq;");
	addToGb(sccpgb, sccpl, sccp, 1, 1, 5);
	sccps = makeEvolverButton(".","show_trans \"s\"; showq;");
	addToGb(sccpgb, sccps, sccp, 1, 2, 5);
	sccpz = makeEvolverButton("O","show_trans \"z\"; showq;");
	addToGb(sccpgb, sccpz, sccp, 1, 3, 5);
	sccpr = makeEvolverButton("(>)","show_trans \"r\"; showq;");
	addToGb(sccpgb, sccpr, sccp, 1, 4, 5);
	sccpv = makeEvolverButton("Toggle ridges/valleys","show_trans \"v\"; showq;");
	addToGb(sccpgb, sccpv, sccp, 1, 5, 5);

	sccpR = makeEvolverButton("#","show_trans \"R\"; showq;");
	addToGb(sccpgb, sccpR, sccp, 1, 1, 5);
	sccpd = makeEvolverButton("(v)","show_trans \"d\"; showq;");
	addToGb(sccpgb, sccpd, sccp, 2, 2, 5);
	sccpm = makeEvolverButton(">*<","show_trans \"m\"; showq;");
	addToGb(sccpgb, sccpm, sccp, 1, 4, 5);
	sccpH = makeEvolverButton("Toggle hidden surfaces","show_trans \"H\"; showq;");
	addToGb(sccpgb, sccpH, sccp, 1, 5, 5);

	sccpal = makeEvolverButton("<|","view_matrix[2][4]-=0.5; showq;");
	addToGb(sccpgb, sccpal, sccp, 1, 1, 5);
	sccpar = makeEvolverButton("|>","view_matrix[2][4]+=0.5; showq;");
	addToGb(sccpgb, sccpar, sccp, 1, 2, 5);
	sccpau = makeEvolverButton("_^_","view_matrix[3][4]+=0.5; showq;");
	addToGb(sccpgb, sccpau, sccp, 1, 3, 5);
	sccpad = makeEvolverButton("-v-","view_matrix[3][4]-=0.5; showq;");
	addToGb(sccpgb, sccpad, sccp, 1, 4, 5);
	

	showcontrols.pack();
	showcontrols.show();

	try {
	    String[] cmd = {evolverBinary, filename};
	    proc = Runtime.getRuntime().exec(cmd,
					     (String[])null,
					     new File(runPath));
	    ro = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	    re = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
	    outpw = new JBufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
	    String basename = filename.substring(0, filename.length()-3);
	    logwin = new JBufferedWriter(new FileWriter(new File(basename+".log")));
	    logwall = new JBufferedWriter(new FileWriter(new File(basename+".out")));
	    conn = new ConnectStdInOut(ro, re, outp, logwall, this, scrollb);
	    conn.start();
	}
	catch (Exception e) {
	    killproc();
	    JOptionPane.showMessageDialog(this,
					  e.getMessage(),
					  "Problem with running evolver",
					  JOptionPane.ERROR_MESSAGE);
	}
    }
}
