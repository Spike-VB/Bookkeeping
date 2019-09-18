package com.spike.Bookkeeping;

import javax.swing.event.*;

public class ResultTableListener implements TableModelListener {
	
	private Bookkeeping bookkeeping;
	private Gui gui;
	
	public ResultTableListener(Bookkeeping b, Gui g) {
		bookkeeping = b;
		gui = g;
	}

	public void tableChanged(TableModelEvent e) {
		switch(e.getColumn()) {
			case 0:
				bookkeeping.updateDate(e);
				break;
			case 1:
				bookkeeping.updateAmount(e);
				break;
			case 2:
				bookkeeping.updateAmount(e);
				break;
			case 4:
				bookkeeping.updateCommentary(e);
				break;
		}
		gui.updateResults();
	}
}
