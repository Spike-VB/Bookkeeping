package com.spike.Bookkeeping;

import java.util.Calendar;

public class DateUtil {
	
	public static String calToDateString(Calendar cal) {
		String date = cal.get(Calendar.YEAR) + "-" + 
				(cal.get(Calendar.MONTH) < 9 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + "-" +
				(cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + cal.get(Calendar.DAY_OF_MONTH) : cal.get(Calendar.DAY_OF_MONTH));
		return date;
	}
	
	public static Calendar dateStringToCal(String date) {
		Calendar cal = new Calendar.Builder().setDate(Integer.parseInt(date.split("-")[0]), 
				Integer.parseInt(date.split("-")[1]) - 1, Integer.parseInt(date.split("-")[2])).build();
		return cal;
	}
}
