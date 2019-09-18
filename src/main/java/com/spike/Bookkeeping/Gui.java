package com.spike.Bookkeeping;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.toedter.calendar.*;

public class Gui {
	
	private Bookkeeping bookkeeping;
	
	private final int insCompL = 100;
	private final int insCompH = 30;
	private final Dimension insCompDim = new Dimension(insCompL, insCompH);
	private final int dateL = 150;
	
	private JFrame frame;
	private JPanel resultPanel = new JPanel(new GridLayout(1,0));
	private JPanel sumPanel = new JPanel(new GridLayout(1,0));
	
	private JDateChooser insertDateChooser;
	private JComboBox<String> operation;
	private JTextField amount;
	private JTextField commentary;
	
	private JDateChooser fromDateChooser;
	private JDateChooser toDateChooser;
	
	private JButton deleteButton;
	private DeleteButtonListener delButtonListener;
	
	public Gui(Bookkeeping b) {
		bookkeeping = b;
	}
	
	public void buildGui() {
		frame = new JFrame("Bookkeeping");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(400, 100, 1000, 800);
		frame.addWindowListener(new FrameWindowListener(bookkeeping));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel insertPanel = getInsertPanel();
		JPanel choicePanel = getChoicePanel();
		createPeriodResultPanel();
		createSumPanel();
		JPanel outputPanel = new JPanel(new BorderLayout());
		
		mainPanel.add(BorderLayout.SOUTH, insertPanel);
		mainPanel.add(BorderLayout.NORTH, choicePanel);
		outputPanel.add(BorderLayout.CENTER, resultPanel);
		outputPanel.add(BorderLayout.SOUTH, sumPanel);
		mainPanel.add(BorderLayout.CENTER, outputPanel);
		
		frame.setContentPane(mainPanel);
		frame.setVisible(true);
	}
	
	private JPanel getInsertPanel() {
		JPanel insertPanel = new JPanel();
		insertPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Insert data"), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		insertPanel.setLayout(new BoxLayout(insertPanel, BoxLayout.PAGE_AXIS));
		insertPanel.setPreferredSize(new Dimension(0, 170));
		
		JPanel headersInsertPanel = new JPanel();
		headersInsertPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
		Label dateLabel = new Label("Date", Label.CENTER);
		dateLabel.setPreferredSize(new Dimension(dateL, insCompH));
		Label operationLabel = new Label("Operation", Label.CENTER);
		operationLabel.setPreferredSize(insCompDim);
		Label amountLabel = new Label("Amount", Label.CENTER);
		amountLabel.setPreferredSize(insCompDim);
		Label commentaryLabel = new Label("Commentary", Label.CENTER);
		commentaryLabel.setPreferredSize(new Dimension(insCompL * 3, insCompH));
		headersInsertPanel.add(dateLabel);
		headersInsertPanel.add(operationLabel);
		headersInsertPanel.add(amountLabel);
		headersInsertPanel.add(commentaryLabel);
		
		JPanel dataInsertPanel = new JPanel();
		dataInsertPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		
		insertDateChooser = new JDateChooser(new JSpinnerDateEditor());
		insertDateChooser.setDate(new java.util.Date());
		insertDateChooser.setPreferredSize(new Dimension(dateL, insCompH));
		
		String[] operations = {"income", "spending"};
		operation = new JComboBox<String>(operations);
		operation.setPreferredSize(insCompDim);
		
		amount = new JTextField();
		amount.setPreferredSize(insCompDim);
		commentary = new JTextField();
		commentary.setPreferredSize(new Dimension(insCompL * 3, insCompH));
		
		dataInsertPanel.add(insertDateChooser);
		dataInsertPanel.add(operation);
		dataInsertPanel.add(amount);
		dataInsertPanel.add(commentary);

		JPanel insertButtonPanel = new JPanel();
		insertButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton insertButton = new JButton("Insert");
		insertButton.addActionListener(new InsertButtonListener());
		insertButton.setPreferredSize(new Dimension(740, insCompH));
		insertButtonPanel.add(insertButton);
		
		insertPanel.add(headersInsertPanel);
		insertPanel.add(dataInsertPanel);
		insertPanel.add(insertButtonPanel);
		
		return insertPanel;
	}
	
	private JPanel getChoicePanel() {
		JPanel choicePanel = new JPanel();
		choicePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Time period to show"), 
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		choicePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		
		fromDateChooser = new JDateChooser(new JSpinnerDateEditor());
		fromDateChooser.setCalendar(new Calendar.Builder().setDate(
				new Calendar.Builder().setInstant(new java.util.Date()).build().get(Calendar.YEAR), 
				new Calendar.Builder().setInstant(new java.util.Date()).build().get(Calendar.MONTH), 1).build());
		fromDateChooser.setPreferredSize(new Dimension(dateL, insCompH));
		
		toDateChooser = new JDateChooser(new JSpinnerDateEditor());
		toDateChooser.setDate(new java.util.Date());
		toDateChooser.setPreferredSize(new Dimension(dateL, insCompH));
		
		Label fromDateLabel = new Label("Show results from");
		Label toDateLabel = new Label("to");
		
		JButton showButton = new JButton("Show results");
		showButton.addActionListener(new ShowButtonListener());
		showButton.setPreferredSize(new Dimension(dateL, insCompH));
		
		choicePanel.add(fromDateLabel);
		choicePanel.add(fromDateChooser);
		choicePanel.add(toDateLabel);
		choicePanel.add(toDateChooser);
		choicePanel.add(showButton);
		
		return choicePanel;
	}
	
	private void createPeriodResultPanel() {
		Vector<Object> headers = new Vector<Object>();
		headers.add("Date");
		headers.add("Operation");
		headers.add("Amount");
		headers.add("Current");
		headers.add("Commentary");
		headers.add("Delete");
		Vector<Vector<Object>> data = bookkeeping.fillPeriodResults(fromDateChooser.getCalendar(), toDateChooser.getCalendar());
		
		@SuppressWarnings("serial")
		JTable table = new JTable(data, headers) {
			
			public boolean isCellEditable(int row, int column) {
				return column == 3 ? false : true;
			}
			
			public TableCellEditor getCellEditor(int row, int column) {
				int modelColumn = convertColumnIndexToModel(column);
				String[] operations = {"income", "spending"};

				if(modelColumn == 0)
					return (TableCellEditor) new TableDateEditor();
				else if(modelColumn == 1)
	                return (TableCellEditor) new DefaultCellEditor(new JComboBox<String>(operations));
				else if(modelColumn == 5)
					return (TableCellEditor) new TableRowRemover();
	            else
	                return super.getCellEditor(row, column);
			}
			
			public TableCellRenderer getCellRenderer(int row, int column) {
				int modelColumn = convertColumnIndexToModel(column);
				
				if(modelColumn == 5)
					return (TableCellRenderer) new TableButtonRenderer();
				else
					return super.getCellRenderer(row, column);
			}
		};
		
		table.getColumnModel().getColumn(0).setPreferredWidth(120);
		table.getColumnModel().getColumn(4).setPreferredWidth(500);
		table.getColumnModel().getColumn(5).setPreferredWidth(40);
		table.setFillsViewportHeight(true);
		table.getModel().addTableModelListener(new resultTableListener());
		
		JScrollPane scrollPane = new JScrollPane(table);
		resultPanel.add(scrollPane);
		
		deleteButton = new JButton();
		delButtonListener = new DeleteButtonListener();
		deleteButton.addActionListener(delButtonListener);
	}
	
	private void createSumPanel() {
		Object[] sumHeaders = {"Sum of period income", "Sum of period spending", "Current overall sum"}; 
		String[][] sumData = bookkeeping.fillSum(fromDateChooser.getCalendar(), toDateChooser.getCalendar());
		
		@SuppressWarnings("serial")
		JTable sumTable = new JTable(sumData, sumHeaders) {

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		sumTable.setRowSelectionAllowed(false);
		sumTable.setColumnSelectionAllowed(false);
		sumTable.setCellSelectionEnabled(false);
		JScrollPane scrollPane = new JScrollPane(sumTable);
		sumPanel.add(scrollPane);
		sumPanel.setPreferredSize(new Dimension(0, 40));
	}
	
	private void updateResults() {
		resultPanel.removeAll();
		sumPanel.removeAll();
		createPeriodResultPanel();
		createSumPanel();
		frame.revalidate();
	}
	
	private class InsertButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			bookkeeping.insertData(insertDateChooser.getCalendar(), operation.getSelectedIndex(), amount.getText(), commentary.getText());
			updateResults();
		}
	}
	
	private class ShowButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			updateResults();
		}
	}
	
	public class DeleteButtonListener implements ActionListener {	
		
		private int row;
		
		public void setRow(int r) {
			row = r;
		}
		
		public void actionPerformed(ActionEvent ev) {
			bookkeeping.deleteRow(row);
			updateResults();
		}
	}
	
	public class resultTableListener implements TableModelListener {

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
			updateResults();
		}
	}
	
	@SuppressWarnings("serial")
	public class TableDateEditor extends AbstractCellEditor implements TableCellEditor {
		JDateChooser chooser = new JDateChooser(new JSpinnerDateEditor());
		
		public Object getCellEditorValue() {
	        return chooser.getCalendar();
	    }
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			chooser.setDateFormatString("yyyy, MMM, dd");
			chooser.setCalendar(DateUtil.dateStringToCal((String) value));
			return chooser;
		}
	}
	
	@SuppressWarnings("serial")
	public class TableRowRemover extends AbstractCellEditor implements TableCellEditor {

		public Object getCellEditorValue() {
			return true;
		}
		
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			delButtonListener.setRow(row);
			return deleteButton;
		}
	}
	
	public class TableButtonRenderer implements TableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			return new JButton("Del");
		}
	}
}
