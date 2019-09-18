package com.spike.Bookkeeping;

import java.awt.event.*;

public class FrameWindowListener implements WindowListener {
	
	private Bookkeeping bookkeeping;
	
	public FrameWindowListener (Bookkeeping b) {
		bookkeeping = b;
	}
	
	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
		bookkeeping.closeDb();
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}
}
