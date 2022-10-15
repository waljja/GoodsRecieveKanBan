package ht.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestTimePeriod {
	public static void main(String[] args) {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		String nowDay = df.format(c.getTime());
		String nowDayTime = df2.format(c.getTime());
		nowDayTime = "2020-12-22 01:00";
		if( (nowDayTime.substring(11).compareTo("08:00") >=0 && nowDayTime.substring(11).compareTo("12:00") <=0 ) 
				|| (nowDayTime.substring(11).compareTo("13:00") >=0 && nowDayTime.substring(11).compareTo("17:00") <=0 )
				|| (nowDayTime.substring(11).compareTo("18:00") >=0 && nowDayTime.substring(11).compareTo("21:00") <=0 )  ){
			System.out.println("YES");
		}else{
			System.out.println("NO");
		}
	}
}
