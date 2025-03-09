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
import java.io.*;

public abstract class Simulation
{
    public JPanel GUI;
    public JFrame outerfr;
    String libpath;

    Simulation() {};
    Simulation(final JFrame outerframe) {
        GUI = new JPanel();
	outerfr = outerframe;
    }
    public abstract void writefile(File outfile) throws IOException;

    protected void appendFileToWriter(String f, BufferedWriter w) throws IOException {
	BufferedReader bf = null;
	bf = new BufferedReader(new FileReader(f));
	String s;
	while ((s = bf.readLine()) != null) {
	    w.write(s);
	    w.newLine();
	}
	if (bf != null) bf.close();
    }

    public abstract JComponent getEvolverGUI(Evolverwin ev);

    protected JTextField makeTextField(GridBagLayout g, Container co, 
				 String caption,
				 int width, String t) {
	GridBagConstraints c = new GridBagConstraints();
	JLabel l = new JLabel(caption);
	c.fill = GridBagConstraints.BOTH;
	c.anchor = GridBagConstraints.NORTHWEST;
	c.gridwidth = 1;
	c.gridheight = 1;
	c.gridx = 0;
	c.weightx = 0;
	c.weighty = 0;
	g.setConstraints(l, c);
	co.add(l);
	Component b = Box.createRigidArea(new Dimension(12,1));
	c.gridx = GridBagConstraints.RELATIVE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	g.setConstraints(b, c);
	co.add(b);

	JTextField f = new JTextField(t,width);
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.weightx = 1;
	g.setConstraints(f, c);
	co.add(f);
	return(f);
    }

    protected JCheckBox makeCheckBox(GridBagLayout g, Container co,
				String caption,
				boolean checked) {
	GridBagConstraints c = new GridBagConstraints();
	JCheckBox cb = new JCheckBox(caption, checked);
	c.gridx = 0;
	c.fill =  GridBagConstraints.BOTH;
	c.gridwidth = GridBagConstraints.REMAINDER;
	c.fill = GridBagConstraints.BOTH;
	g.setConstraints(cb, c);
	co.add(cb);
	return(cb);
    }

    protected JSpinner makeNumSpinner(GridBagLayout g, Container co, 
				  String caption,
				  int min, int max, int v) {
	GridBagConstraints c = new GridBagConstraints();
	JLabel l = new JLabel(caption);
	c.fill = GridBagConstraints.BOTH;
	c.gridwidth = 1;
	c.gridx = 0;
	g.setConstraints(l, c);
	co.add(l);
	Component b = Box.createRigidArea(new Dimension(12,1));
	c.gridx = GridBagConstraints.RELATIVE;
	c.gridwidth = GridBagConstraints.RELATIVE;
	g.setConstraints(b, c);
	co.add(b);

	JSpinner s = new JSpinner(new SpinnerNumberModel(v, min, max, 1));
	c.gridwidth = GridBagConstraints.REMAINDER;
	g.setConstraints(s, c);
	co.add(s);
	return(s);
    }
}
