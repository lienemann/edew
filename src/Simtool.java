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
import java.awt.event.*;


/**
 * This program serves as a wrapper for the Surface Evolver program.
 * It creates input files to model some procedures of the Microprotein EU project.
 * 
 * @author Jan Lienemann
 */

// Preferences:  Library-Pfad -> System
//               Surface-Evolver-Pfad: System (default), User
//               Zahlenwerte: User
// tmpdir: System.getProperty("java.io.tmp","/tmp");


class Simtool {

    public static String preferencesNode = "de.imtek.simulation.microprotein.simtool";

    public static void main(String[] args)
    {

	System.out.println(" EDEW Version 2");
	System.out.println(" Copyright(C) 2004 Jan Lienemann");
	System.out.println(" This tool comes with ABSOLUTELY NO WARRANTY.");
	System.out.println(" It is free software, and you are welcome to redistribute it");
	System.out.println(" under certain conditions. See LICENSE.txt for details.");

	JFrame f = new JFrame("EDEW 2.0");
	Mainwin s = new Mainwin(Preferences.userRoot().node(preferencesNode), f);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.getContentPane().add(s);
	f.pack();
	//	f.setResizable(false);
	f.setVisible(true);
    }
};
