package com.spike.Bookkeeping;

import java.awt.Component;

import javax.swing.*;
import javax.swing.table.*;

public class TableDelButtonRenderer implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return new JButton("Del");
	}
}
