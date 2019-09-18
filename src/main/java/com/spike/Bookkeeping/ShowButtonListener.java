package com.spike.Bookkeeping;

import java.awt.event.*;

public class ShowButtonListener implements ActionListener {
	
	private Gui gui;
	
	public ShowButtonListener(Gui g) {
		gui = g;
	}
	
	public void actionPerformed(ActionEvent ev) {
		gui.updateResults();
	}
}
