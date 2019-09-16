package com.spike.BookkeepingMvn;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.*;

public class BookkeepingTest {
	
	Bookkeeping b = new Bookkeeping();

	@Test
	public void testCalToDateString() {
		int[][] calValue = {{1000, 0, 1}, {2020, 10, 20}};
		String[] dateValue = {"1000-01-01", "2020-11-20"};
		
		
		for(int i = 0; i < dateValue.length; i++) {
			Calendar cal = new Calendar.Builder().setDate(calValue[i][0], calValue[i][1], calValue[i][2]).build();
			String expDate = dateValue[i];
			String date = b.calToDateString(cal);
			assertEquals(expDate, date);
		}
		//fail("Not yet implemented");
	}

	@Test()
	public void testDateStringToCal() {
		String[] dateValue = {"1000-01-01", "2020-11-20"};
		int[][] calValue = {{1000, 0, 1}, {2020, 10, 20}};
		
		for(int i = 0; i < dateValue.length; i++) {
			Calendar expCal = new Calendar.Builder().setDate(calValue[i][0], calValue[i][1], calValue[i][2]).build();
			Calendar cal = b.dateStringToCal(dateValue[i]);
			assertEquals(expCal, cal);
		}
		//fail("Not yet implemented");
	}

}
