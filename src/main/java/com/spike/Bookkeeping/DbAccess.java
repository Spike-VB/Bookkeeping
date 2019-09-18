package com.spike.Bookkeeping;

import java.io.File;
import java.sql.*;
import java.util.*;

import javax.swing.event.*;

public class DbAccess {

	private File dbPath = new File("bookkeeping.db");
	private Connection dbCon;
	
	private Vector<Vector<Object>> data;
	private Vector<Integer> operationId = new Vector<Integer>();
	
	public void startDb() {
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
	
	public void insertData(Calendar cal, int operation, int amount, String commentary) {
		try {
			int amountWithSign = (operation == 0 ? 
					amount : (-1 * amount));
			Statement st = dbCon.createStatement();
			
			String insertQuery = 
					"INSERT INTO activity " +
					"(date, amount, commentary) " +
					"VALUES " +
					"('" + DateUtil.calToDateString(cal) + "', " + amountWithSign + ", " + "'" + commentary + "')";
		
			st.executeUpdate(insertQuery);
			st.close();
		}
		catch(NumberFormatException ex) {
			new InputErrorMessage();
		}
		catch(SQLException ex) {
			ex.printStackTrace();
		}
		updateCurrentColumn(cal);
	}
	
	public void deleteRow(int row) {
		int id = operationId.get(row);
		Calendar cal = DateUtil.dateStringToCal((String) data.get(row).get(0));
		
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
	
	public Vector<Vector<Object>> fillPeriodResults(Calendar fromCal, Calendar toCal) {
		Vector<Object> tempData = new Vector<Object>();
		Vector<Vector<Object>> dataList =  new Vector<Vector<Object>>();
		operationId.clear();
		
		String selectQuery = 
				"SELECT * FROM activity " +
				"WHERE date >= '" + DateUtil.calToDateString(fromCal) + "' " +
				"AND date <= '" + DateUtil.calToDateString(toCal) + "' " + 
				"ORDER BY date, operation_id";	
		try {
			Statement st = dbCon.createStatement();
			ResultSet result = st.executeQuery(selectQuery);
			
			while(result.next()) {
				int id = result.getInt("operation_id");
				String date = result.getString("date");
				String operation = result.getInt("amount") > 0 ? "income" : "spending";
				int amount = Math.abs(result.getInt("amount"));
				int current = result.getInt("current");
				String commentary = result.getString("commentary");
				
				operationId.add(id);
				tempData.add(date);
				tempData.add(operation);
				tempData.add(amount);
				tempData.add(current);
				tempData.add(commentary);
				
				dataList.add(new Vector<Object>(tempData));
				
				tempData.clear();
			}
			result.close();
			st.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		data = dataList;
		return dataList;
	}
	
	public String[][] fillSum(Calendar fromCal, Calendar toCal) {
		String[][] sumData = new String[1][3];
		
		try {
			Statement st = dbCon.createStatement();
			
			String selectQuery = 
					"SELECT SUM(amount) FROM activity " +
					"WHERE date >= '" + DateUtil.calToDateString(fromCal) + "' " +
					"AND date <= '" + DateUtil.calToDateString(toCal) + "' " + 
					"AND amount >= 0";
			ResultSet result = st.executeQuery(selectQuery);
			sumData[0][0] = "" + result.getInt("SUM(amount)");
			
			selectQuery = 
					"SELECT ABS(SUM(amount)) FROM activity " +
					"WHERE date >= '" + DateUtil.calToDateString(fromCal) + "' " +
					"AND date <= '" + DateUtil.calToDateString(toCal) + "' " + 
					"AND amount < 0";
			result = st.executeQuery(selectQuery);
			sumData[0][1] = "" + result.getInt("ABS(SUM(amount))");
			
			selectQuery = 
					"SELECT current FROM activity " +
					"WHERE operation_id = (SELECT MAX(operation_id) FROM activity " +
					"WHERE date = (SELECT MAX(date) FROM activity))";
			result = st.executeQuery(selectQuery);
			sumData[0][2] = "" + result.getInt("current");
			
			result.close();
			st.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return sumData;
	} 
	
	public void updateDate(TableModelEvent e) {
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
	
	public void updateAmount(TableModelEvent e) {	
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
	
	public void updateCommentary(TableModelEvent e) {
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
}
