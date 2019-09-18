package com.spike.Bookkeeping;

import java.awt.Component;

import javax.swing.*;
import javax.swing.table.*;

@SuppressWarnings("serial")
public class TableRowRemover extends AbstractCellEditor implements TableCellEditor {
	
	private JButton deleteButton;
	private DeleteButtonListener delButtonListener;
	
	public TableRowRemover(JButton b, DeleteButtonListener d) {
		deleteButton = b;
		delButtonListener = d;
	}

	public Object getCellEditorValue() {
		return true;
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		delButtonListener.setRow(row);
		return deleteButton;
	}
}
