package ht.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBConDashBoard {
	
	private static Log commonsLog = LogFactory.getLog(DBConDashBoard.class);
	
	private static Connection con = null;
	
	private DBConDashBoard() {
	}
	
	public static synchronized Connection getInstance() {  // synchronized 必须的，否则不是单例
		if(con == null) {
			try{
				commonsLog.info("initialize DB Connection");
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
				con = DriverManager.getConnection("jdbc:sqlserver://172.31.2.33;databaseName=GRNewDashboard", 
						"htitsa", "htitprogram");
			}catch (InstantiationException e) {
				commonsLog.error("InstantiationException",e);
			}catch (IllegalAccessException e) {
				commonsLog.error("IllegalAccessException",e);
			}catch(ClassNotFoundException e) {
				commonsLog.error("ClassNotFoundException",e);
			}catch(SQLException e) {
				commonsLog.error("SQLException",e);
			} 
		}
		commonsLog.info(con);
		return con;
	}

}
