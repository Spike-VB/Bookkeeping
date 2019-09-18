package com.spike.Bookkeeping;

import java.util.*;
//import java.sql.*;
//import javax.swing.*;
import javax.swing.event.*;
//import javax.swing.table.*;
//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.border.*;
//import com.toedter.calendar.*;
//import java.io.*;

public class Bookkeeping {
	
	/*
	File dbPath = new File("bookkeeping.db"); //+
	private Connection dbCon; //+
	
	private final int insCompL = 100; //+
	private final int insCompH = 30; //+
	private final Dimension insCompDim = new Dimension(insCompL, insCompH); //+
	private final int dateL = 150; //+
	
	private JFrame frame; //+
	private JPanel resultPanel = new JPanel(new GridLayout(1,0)); //+
	private JPanel sumPanel = new JPanel(new GridLayout(1,0)); //+
	
	private JDateChooser insertDateChooser; //+
	private JComboBox<String> operation; //+
	private JTextField amount; //+
	private JTextField commentary; //+
	
	private JDateChooser fromDateChooser; //+
	private JDateChooser toDateChooser; //+
	
	private JButton deleteButton; //+
	private DeleteButtonListener delButtonListener; //+
	
	private Vector<Integer> operationId = new Vector<Integer>(); //+
	private Vector<Vector<Object>> data; //+
	*/
	
	
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
	
	public void insertData(Calendar cal, int operation, String amount, String commentary) {
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
	
	/*
	private void checkDb() {
		if(dbPath.exists()) {
			connectDb();
		}
		else {
			connectDb();
			createDb();
		}
	}
	
	private void createDb() {
		try {
			Statement st = dbCon.createStatement();
			
			String query = 
					"CREATE TABLE activity " +
					"( " +
					"operation_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
					"date DATE, " +
					"amount INTEGER, " +
					"current INTEGER, " +
					"commentary VARCHAR(50))";
			st.executeUpdate(query);
			
			query =
					"INSERT INTO activity " +
					"(date, amount, current, commentary) " +
					"VALUES " +
					"('0001-01-01', 0, 0, 'Starting row')";
			st.executeUpdate(query);
			st.close();	
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private void connectDb() {
		
		try {
			//Class.forName("org.sqlite.JDBC");
			dbCon = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			System.out.println("Base is connected");
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void closeDb() {
		try {
			dbCon.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void buildGui() {
		frame = new JFrame("Bookkeeping");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new frameWindowListener());
		frame.setBounds(400, 100, 1000, 800);

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
		data = fillPeriodResults();
		
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
		String[][] sumData = fillSum();
		
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
	
	private void createInputMessage() {
		JFrame frame = new JFrame("Message");
		frame.setBounds(700, 300, 300, 200);
		Label message = new Label("Insert an amount.", Label.CENTER);
		Font font = new Font("Default", Font.PLAIN, 22);
		message.setFont(font);
		frame.getContentPane().add(BorderLayout.CENTER, message);
		frame.setVisible(true);
	}
	
	private String[][] fillSum1() {
		String[][] data = new String[1][3];
		
		Calendar fromCal = fromDateChooser.getCalendar();
		Calendar toCal = toDateChooser.getCalendar();
		
		try {
			Statement st = dbCon.createStatement();
			
			String selectQuery = 
					"SELECT SUM(amount) FROM activity " +
					"WHERE date >= '" + DateUtil.calToDateString(fromCal) + "' " +
					"AND date <= '" + DateUtil.calToDateString(toCal) + "' " + 
					"AND amount >= 0";
			ResultSet result = st.executeQuery(selectQuery);
			data[0][0] = "" + result.getInt("SUM(amount)");
			
			selectQuery = 
					"SELECT ABS(SUM(amount)) FROM activity " +
					"WHERE date >= '" + DateUtil.calToDateString(fromCal) + "' " +
					"AND date <= '" + DateUtil.calToDateString(toCal) + "' " + 
					"AND amount < 0";
			result = st.executeQuery(selectQuery);
			data[0][1] = "" + result.getInt("ABS(SUM(amount))");
			
			selectQuery = 
					"SELECT current FROM activity " +
					"WHERE operation_id = (SELECT MAX(operation_id) FROM activity " +
					"WHERE date = (SELECT MAX(date) FROM activity))";
			result = st.executeQuery(selectQuery);
			data[0][2] = "" + result.getInt("current");
			
			result.close();
			st.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}
	
	public String calToDateString(Calendar cal) {
		String date = cal.get(Calendar.YEAR) + "-" + 
				(cal.get(Calendar.MONTH) < 9 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + "-" +
				(cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH));
		return date;
	}
	
	public Calendar dateStringToCal(String date) {
		Calendar cal = new Calendar.Builder().setDate(Integer.parseInt(date.split("-")[0]), 
				Integer.parseInt(date.split("-")[1]) - 1, Integer.parseInt(date.split("-")[2])).build();
		return cal;
	}
	
	private void updateResults() {
		resultPanel.removeAll();
		sumPanel.removeAll();
		createPeriodResultPanel();
		createSumPanel();
		frame.revalidate();
	}
	
	public void updateCurrentColumn(Calendar cal) {
		
		try {
			Statement commonSt = dbCon.createStatement();
			Statement updateSt = dbCon.createStatement();
			
			String query = 
					"SELECT MAX(date) FROM activity " +
					"WHERE date < '" + DateUtil.calToDateString(cal) + "'";
			String minDate = commonSt.executeQuery(query).getString("MAX(date)");
			
			query = 
					"SELECT MAX(operation_id) FROM activity " +
					"WHERE date = '" + minDate + "'";
			int id = commonSt.executeQuery(query).getInt("MAX(operation_id)");
			
			query = 
					"SELECT operation_id, amount, current FROM activity " +
					"WHERE date >= '" + minDate + "' " +
					"ORDER BY date, operation_id";
			ResultSet result = commonSt.executeQuery(query);
			
			int updateCurrent = 0;
			boolean toWrite = false;
			boolean thereIsCurrent = false;
			
			while(result.next()) {
				
				if(result.getInt("operation_id") == id) {
					toWrite = true;
				}
				
				if(toWrite) {
					if(thereIsCurrent == false) {
						updateCurrent = result.getInt("current");
						thereIsCurrent = true;
					}
					else {
						updateCurrent = updateCurrent + result.getInt("amount");
						query = 
								"UPDATE activity " +
								"SET current = " + updateCurrent + " " +
								"WHERE operation_id = " + result.getInt("operation_id");
						updateSt.executeUpdate(query);
					}
				}
			}
			updateSt.close();
			commonSt.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void updateDate(TableModelEvent e) {
		int id = operationId.get(e.getFirstRow());
		
		try {
			Statement st = dbCon.createStatement();
			
			String dateQuery = 
					"SELECT date FROM activity " +
					"WHERE operation_id = " + id;

			Calendar cal1 = DateUtil.dateStringToCal(st.executeQuery(dateQuery).getString("date"));
			Calendar cal2 = (Calendar) data.get(e.getFirstRow()).get(0);
			Calendar minCal = cal1.compareTo(cal2) < 0 ? cal1 : cal2;
			
			String updateQuery =
					"UPDATE activity " +
					"SET " +
					"date = '" + DateUtil.calToDateString((Calendar) data.get(e.getFirstRow()).get(0)) + "' " +
					"WHERE operation_id = " + id;
			st.executeUpdate(updateQuery);
			st.close();
			updateCurrentColumn(minCal);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void updateAmount(TableModelEvent e) {	
		String amountSign = data.get(e.getFirstRow()).get(1).equals("income") ? "" : "-1 * ";
		
		String updateQuery =
				"UPDATE activity " +
				"SET " +
				(e.getColumn() == 1 ? "amount = " + amountSign + "ABS(amount)" : 
				"amount = " + amountSign + "ABS(" + data.get(e.getFirstRow()).get(2) + ") ") +
				"WHERE operation_id = " + operationId.get(e.getFirstRow());
		try {
			Statement st = dbCon.createStatement();
			st.executeUpdate(updateQuery);		
			st.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		updateCurrentColumn(DateUtil.dateStringToCal((String) data.get(e.getFirstRow()).get(0)));
	}
	
	private void updateCommentary(TableModelEvent e) {
		String updateQuery =
				"UPDATE activity " +
				"SET " +
				"commentary = '" + data.get(e.getFirstRow()).get(e.getColumn()) + "' " +
				"WHERE operation_id = " + operationId.get(e.getFirstRow());
		try {
			Statement st = dbCon.createStatement();
			st.executeUpdate(updateQuery);
			st.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public class InsertButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			Calendar cal = insertDateChooser.getCalendar();
			try {
				int amountWithSign = (operation.getSelectedIndex() == 0 ? 
						Integer.parseInt(amount.getText()) : (-1 * Integer.parseInt(amount.getText())));
				Statement st = dbCon.createStatement();
				
				String insertQuery = 
						"INSERT INTO activity " +
						"(date, amount, commentary) " +
						"VALUES " +
						"('" + DateUtil.calToDateString(cal) + "', " + amountWithSign + ", " + "'" + commentary.getText() + "')";
			
				st.executeUpdate(insertQuery);
				st.close();
			}
			catch(NumberFormatException ex) {
				createInputMessage();
				System.out.println("Input a number");
			}
			catch(SQLException ex) {
				ex.printStackTrace();
			}
			updateCurrentColumn(cal);
			updateResults();
		}
	}
	
	public class ShowButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			updateResults();
		}
	}
	
	public class DeleteButtonListener implements ActionListener {
		
		private int row;
		private int id;
		private Calendar cal;
		
		public void setRow(int r) {
			row = r;
			id = operationId.get(row);
			cal = DateUtil.dateStringToCal((String) data.get(row).get(0));
		}
		
		public void actionPerformed(ActionEvent ev) {
			String deleteQuery = 
					"DELETE FROM activity " +
					"WHERE " + 
					"operation_id = " + id;
			try {
				Statement st = dbCon.createStatement();
				st.executeUpdate(deleteQuery);
				st.close();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			updateCurrentColumn(cal);
			updateResults();
		}
	}
	
	public class frameWindowListener implements WindowListener {

		public void windowOpened(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
		}

		public void windowClosed(WindowEvent e) {
			try {
				dbCon.close();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
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
	
	public class resultTableListener implements TableModelListener {

		public void tableChanged(TableModelEvent e) {
			switch(e.getColumn()) {
				case 0:
					updateDate(e);
					break;
				case 1:
					updateAmount(e);
					break;
				case 2:
					updateAmount(e);
					break;
				case 4:
					updateCommentary(e);
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
	*/
}
