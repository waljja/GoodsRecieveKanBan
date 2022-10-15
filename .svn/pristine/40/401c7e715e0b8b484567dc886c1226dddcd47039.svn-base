package ht.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBConVPS {
	
	private static Log commonsLog = LogFactory.getLog(DBConVPS.class);
	
	private static Connection con = null;
	
	private DBConVPS() {
	}
	
	public static synchronized Connection getInstance() {
		if(con == null) {
			try{
				commonsLog.info("initialize DB Connection");
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
				con = DriverManager.getConnection("jdbc:sqlserver://172.31.2.26;databaseName=imslabel", 
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
