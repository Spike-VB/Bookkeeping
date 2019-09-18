package com.spike.Bookkeeping;

import java.util.*;

import javax.swing.event.*;

public class Bookkeeping {
	
	DbAccess db;
	Gui gui;
	
	public static void main(String[] args) {
		Bookkeeping b = new Bookkeeping();
		b.startApp();
	}
	
	public void startApp() {
		db = new DbAccess();
		db.startDb();
		
		Gui gui = new Gui(this);
		gui.buildGui();
	}
	
	public void closeDb() {
		db.closeDb();
	}
	
	public void insertData(Calendar cal, int operation, int amount, String commentary) {
		db.insertData(cal, operation, amount, commentary);
	}
	
	public void deleteRow(int row) {
		db.deleteRow(row);
	}
	
	public Vector<Vector<Object>> fillPeriodResults(Calendar fromCal, Calendar toCal) {
		return db.fillPeriodResults(fromCal, toCal);
	}
	
	public String[][] fillSum(Calendar fromCal, Calendar toCal) {
		return db.fillSum(fromCal, toCal);
	}
	
	public void updateDate(TableModelEvent e) {
		db.updateDate(e);
	}
	
	public void updateAmount(TableModelEvent e) {
		db.updateAmount(e);
	}
	
	public void updateCommentary(TableModelEvent e) {
		db.updateCommentary(e);
	}
}
