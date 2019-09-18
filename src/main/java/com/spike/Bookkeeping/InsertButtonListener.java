package com.spike.Bookkeeping;

import java.awt.event.*;

public class InsertButtonListener implements ActionListener {
	
	private Bookkeeping bookkeeping;
	private Gui gui;
	
	public InsertButtonListener(Bookkeeping b, Gui g) {
		bookkeeping = b;
		gui = g;
	}
	
	public void actionPerformed(ActionEvent ev) {
		bookkeeping.insertData(gui.getInsertDateCal(), gui.getOperation(), gui.getAmount(), gui.getCommentary());
		gui.updateResults();
	}
}
