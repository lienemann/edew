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

import java.io.*;
import javax.swing.*;
import java.awt.*;

class ConnectStdInOut extends Thread {
    BufferedReader ro;
    BufferedReader re;
    JTextArea out;
    BufferedWriter logwall;
    boolean stopme = false;
    Component parent;
    JScrollBar scrollb;

    ConnectStdInOut(BufferedReader mro, BufferedReader mre, JTextArea mout, BufferedWriter mlogwall, Component mparent, JScrollBar mscrollb)
    {
	ro = mro;
	re = mre;
	out = mout;
	parent = mparent;
	scrollb = mscrollb;
	logwall = mlogwall;
    }

    public void run() {
	char[] bufo = new char[1000];
	char[] bufe = new char[1000];
	int so = 0, se = 0;

	try {
	    if (ro.ready()) so = ro.read(bufo, 0, 1000); else so = 0;
	    if (re.ready()) se = re.read(bufe, 0, 1000); else se = 0;;
	    while ( ( so >= 0 || se >= 0) && !stopme)
		{
		    if (se == 0 && so == 0)
			Thread.sleep(50);
		    else {
			if (so > 0) { 
			    out.append(new String(bufo, 0, so));
			    logwall.write(bufo, 0, so);
			}
			if (se > 0) {
			    out.append(new String(bufe, 0, se));
			    logwall.write(bufe, 0, se);
			}
			Thread.sleep(100);
			scrollb.setValue(scrollb.getMaximum()-scrollb.getVisibleAmount());
		    }
		    if (ro.ready()) so = ro.read(bufo, 0, 1000);
					else so = 0;
		    if (re.ready()) se = re.read(bufe, 0, 1000);
					else se = 0;
		}
	}
	catch (Exception e) {
	    JOptionPane.showMessageDialog(parent,
					  e.getMessage(),
					  "Problem with running evolver",
					  JOptionPane.ERROR_MESSAGE);
	}
    }
    
    public void stopme() {
	stopme = true;
    }
}
