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


import java.util.prefs.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Prefwin extends JDialog {

    JTextField evolverBinary;
    JTextField libraryDir;
    JTextField runDir;
    JButton cancelButton;
    JButton saveButton;

    static JFileChooser fc;
    JFrame outerfr;
    static Preferences prefs;

    static String defaultEvolverBinaryUNIX =
	"/usr/local/bin/evolver";
    static String defaultEvolverBinaryWin =
	"C:\\Program Files\\Evolver\\evolver.exe";
    static String defaultLibraryDirUNIX =
	"/usr/local/lib/microprotein_simulation_tool/";
    static String defaultLibraryDirWin =
	"C:\\Program Files\\Microprotein Simulation Tool\\";
    static String defaultRunDirUNIX = "/tmp/";
    static String defaultRunDirWin = "C:\\Windows\\Temp\\";

    protected JTextField makeFileTextField(GridBagLayout g, Container co, 
				       String caption,
				       String t,
				       String tooltip, boolean dir) {
	GridBagConstraints c = new GridBagConstraints();
	JLabel l = new JLabel(caption);
	l.setToolTipText(tooltip);
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = 1;
	c.gridx = 0;
	g.setConstraints(l, c);
	co.add(l);

	c.gridx = GridBagConstraints.RELATIVE;
	Component b = Box.createRigidArea(new Dimension(12,1));
	g.setConstraints(b, c);
	co.add(b);

	final JTextField f = new JTextField(t);
	f.setToolTipText(tooltip);
	c.gridwidth = GridBagConstraints.RELATIVE;
	g.setConstraints(f, c);
	co.add(f);

	final JButton bt = new JButton("Browse...");
	if (dir) {
	    bt.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == bt) {
			    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    fc.setCurrentDirectory(new File(f.getText()));
			    int ret = fc.showDialog(outerfr, "Select");
			    try {
				if (ret != JFileChooser.APPROVE_OPTION)
				    return;
				if (!fc.getSelectedFile().isDirectory())
				    throw(new IOException("Please choose a directory."));
				f.setText(fc.getSelectedFile().getPath());
			    }
			    catch (IOException ee) {
				JOptionPane.showMessageDialog(outerfr,
					    ee.getMessage(),
					    "Problem choosing directory",
					    JOptionPane.ERROR_MESSAGE);
				return;
			    }
			}
		    }
		});
	}
	else {
	    bt.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == bt) {
			    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    fc.setCurrentDirectory(new File(f.getText()));
			    int ret = fc.showDialog(outerfr, "Select");
			    try {
				if (ret != JFileChooser.APPROVE_OPTION)
				    return;
				if (!fc.getSelectedFile().isFile())
				    throw(new IOException("Please choose a executable."));
				f.setText(fc.getSelectedFile().getPath());
			    }
			    catch (IOException ee) {
				JOptionPane.showMessageDialog(outerfr,
					    ee.getMessage(),
					    "Problem choosing file",
					    JOptionPane.ERROR_MESSAGE);
				return;
			    }
			}
		    }
		});
	}
	c.gridwidth = GridBagConstraints.REMAINDER;
	g.setConstraints(bt, c);
	co.add(bt);

	return(f);
    }

    public static String getLibraryDir(Preferences prefs) {
	return(
	       removeSlash(prefs.get("libraryDir",
			      System.getProperty("os.name")=="Windows"?
			      Prefwin.defaultLibraryDirWin:
			      Prefwin.defaultLibraryDirUNIX))
	       );
    }
    public static String getEvolverBinary(Preferences prefs) {
	return(
	       prefs.get("evolverBinary",
			 System.getProperty("os.name")=="Windows"?
			 Prefwin.defaultEvolverBinaryWin:
			 Prefwin.defaultEvolverBinaryUNIX)
	       );
    }
    public static String getRunDir(Preferences prefs) {
	return(
	       removeSlash(prefs.get("evolverRunDir",
			      System.getProperty("os.name")=="Windows"?
			      Prefwin.defaultRunDirWin:
			      Prefwin.defaultRunDirUNIX))
	       );
    }

    protected static String removeSlash(String s) {
	if (s.length()>1 &&
	    s.endsWith(File.pathSeparator))
	    return(s.substring(0, s.length()-1));
	else return(s);
    }

    Prefwin(final Preferences prefs, JFrame outerframe) {

	super(outerframe, "Preferences", true);
	outerfr = outerframe;
	Container cp = getContentPane();
 	cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
	JPanel sub1 = new JPanel();

	GridBagLayout g = new GridBagLayout();
//	GridBagConstraints c = new GridBagConstraints();
 	sub1.setLayout(g);
	sub1.setBorder(BorderFactory.createEmptyBorder(12,12,11,11));
	libraryDir = makeFileTextField(g, sub1, "Library directory:",
	     prefs.get("libraryDir",
		       System.getProperty("os.name")=="Windows"?
		       defaultLibraryDirWin:
		       defaultLibraryDirUNIX),
	     "Enter the directory where you installed the simulation tool to.",
				       true);

	evolverBinary = makeFileTextField(g, sub1, "Evolver binary:",
	     prefs.get("evolverBinary",
		       System.getProperty("os.name")=="Windows"?
		       defaultEvolverBinaryWin:
		       defaultEvolverBinaryUNIX),
	     "Enter the directory where you installed the surface evolver to.",
				      false);

	runDir = makeFileTextField(g, sub1, "Run directory:",
	     prefs.get("evolverRunDir",
		       (System.getProperty("os.name")=="Windows"?
			defaultRunDirWin:
			defaultRunDirUNIX)+
		       "mprot_sim_"+System.getProperty("user.name")),
	     "Enter the directory where you want the evolver to run and put its output files. Directory will be created if it does not exist.",
			       true);

	cp.add(sub1);
	JPanel restartP = new JPanel();
	restartP.add(new JLabel("Please restart program after changing the Library directory to make the change effective!"));
	cp.add(restartP);

	JPanel buttons = new JPanel();
	buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
	buttons.add(Box.createGlue());

	saveButton = new JButton("Save");
	saveButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == saveButton) {
			prefs.put("libraryDir", libraryDir.getText());
			prefs.put("evolverBinary", evolverBinary.getText());
			prefs.put("evolverRunDir", runDir.getText());
			dispose();
		    }
		}
	    });
	buttons.add(saveButton);

	buttons.add(Box.createRigidArea(new Dimension(5,1)));

	cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == cancelButton) {
			dispose();
		    }
		}
	    });
	buttons.add(cancelButton);
	buttons.setBorder(BorderFactory.createEmptyBorder(12,17,11,11));
	cp.add(buttons);

	pack();
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
