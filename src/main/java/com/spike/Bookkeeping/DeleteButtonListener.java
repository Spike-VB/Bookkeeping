package com.spike.Bookkeeping;

import java.awt.event.*;

public class DeleteButtonListener implements ActionListener {	
	
	private Bookkeeping bookkeeping;
	private Gui gui;
	private int row;
	
	public DeleteButtonListener(Bookkeeping b, Gui g) {
		bookkeeping = b;
		gui = g;
	}
	
	public void setRow(int r) {
		row = r;
	}
	
	public void actionPerformed(ActionEvent ev) {
		bookkeeping.deleteRow(row);
		gui.updateResults();
	}
}
