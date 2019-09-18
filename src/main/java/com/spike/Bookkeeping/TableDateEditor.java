package com.spike.Bookkeeping;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

import com.toedter.calendar.*;

@SuppressWarnings("serial")
public class TableDateEditor extends AbstractCellEditor implements TableCellEditor {
	
	private JDateChooser chooser = new JDateChooser(new JSpinnerDateEditor());
	
	public Object getCellEditorValue() {
        return chooser.getCalendar();
    }
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		chooser.setDateFormatString("yyyy, MMM, dd");
		chooser.setCalendar(DateUtil.dateStringToCal((String) value));
		return chooser;
	}
}
