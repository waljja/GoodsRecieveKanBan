package ht.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestMonthDay {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 获取当月第一天和最后一天  
		Calendar cale = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		String firstday;
		String lastday;  
		// 获取当月的第一天  
		cale = Calendar.getInstance();  
		cale.add(Calendar.MONTH, 0);  
		cale.set(Calendar.DAY_OF_MONTH, 1);  
		firstday = format.format(cale.getTime());  
		// 获取当月的最后一天  
		cale = Calendar.getInstance();  
		cale.add(Calendar.MONTH, 1);  
		cale.set(Calendar.DAY_OF_MONTH, 0);  
		lastday = format.format(cale.getTime()); 
		
		System.out.println(firstday);
		System.out.println(lastday);
	}

}
