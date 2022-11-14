package ht.util;

import java.sql.*;

/**
 * 172.31.2.26 imslabel 数据库
 * @author 刘惠明
 * @createDate 2020-9-3
 * @version 1.0.0
 */
public class ConVPS
{
	private static String strDBDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String strDBUrl = "jdbc:sqlserver://172.31.2.26;databaseName=imslabel";

	public Connection con = null;
	private ResultSet rs = null;
	private Statement stmt = null;
	private String user = "htitsa";
	private String password = "htitprogram";

	public ConVPS() {
	  init();
	}

	public void init() {
		try {
		  Class.forName(strDBDriver).newInstance();
		  	this.con = DriverManager.getConnection(strDBUrl, this.user, this.password);

		  	this.stmt = this.con
		  	  .createStatement();
		} catch (InstantiationException e) {
		  	e.printStackTrace();
		} catch (IllegalAccessException e) {
		  	e.printStackTrace();
		} catch (ClassNotFoundException e) {
		  	System.err.println("ConBean():" + e.getMessage());
		} catch (SQLException e) {
		  	System.err.println("ConBean:" + e.getMessage());
		}
	}

	public ResultSet executeQuery(String sql) {
	 	try {
	 	  	this.stmt = this.con.createStatement();
	 	  	this.rs = this.stmt.executeQuery(sql);
	 	} catch (SQLException ex) {
	 	  	ex.printStackTrace();
	 	}
	 	return this.rs;
	}

	public void executeUpdate(String sql) throws SQLException {
	  	this.stmt = this.con.createStatement();
	    this.stmt.executeUpdate(sql);
	}

	public void close() {
	  	try {
	  	 	if (this.rs != null) {
	  	 	  	this.rs.close();
	  	 	  	this.rs = null;
	  	 	}
	  	} catch (Exception e) {
	  	 	System.out.println("dbBean close rs error!");
	  	 	try{
	  	 	  	if (this.stmt != null) {
	  	 	  	  	this.stmt.close();
	  	 	  	  	this.stmt = null;
	  	 	  	}
	  	 	} catch (Exception ex) {
	  	 	  	System.out.println("dbBean close stmt error!");
	  	 	  	try{
	  	 	  	  	if (this.con == null) return; this.con.close();
	  	 	  	  	this.con = null;
	  	 	  	}
	  	 	  	catch (Exception ee) {
	  	 	  	  	System.out.println("dbBean close con error!");
	  	 	  	}
	  	 	}
	  	 	finally{
	  	 	  	try{
	  	 	  	  	if (this.con != null) {
	  	 	  	  	  this.con.close();
	  	 	  	  	  this.con = null;
	  	 	  	  	}
	  	 	  	} catch (Exception ex) {
	  	 	  	  	System.out.println("dbBean close con error!");
	  	 	  	}
	  	 	}
	  	 	try{
	  	 	  	if (this.con != null) {
	  	 	  	  	this.con.close();
	  	 	  	  	this.con = null;
	  	 	  	}
	  	 	} catch (Exception ex) {
	  	 	  	System.out.println("dbBean close con error!");
	  	 	}
	  	}
	  	finally {
	  	  	try {
	  	  	  	if (this.stmt != null) {
	  	  	  	  	this.stmt.close();
	  	  	  	  	this.stmt = null;
	  	  	  	}
	  	  	} catch (Exception e) {
	  	  	  	System.out.println("dbBean close stmt error!");
	  	  	  	try {
	  	  	  	  	if (this.con != null) {
	  	  	  	  	  	this.con.close();
	  	  	  	  	  	this.con = null;
	  	  	  	  	}
	  	  	  	} catch (Exception ex) {
	  	  	  	  	System.out.println("dbBean close con error!");
	  	  	  	}
	  	  	}
	  	  	finally {
	  	  	  	try {
	  	  	  	  	if (this.con != null) {
	  	  	  	  	  	this.con.close();
	  	  	  	  	  	this.con = null;
	  	  	  	  	}
	  	  	  	} catch (Exception e) {
	  	  	  	  	System.out.println("dbBean close con error!");
	  	  	  	}
	  	  	}
	  	}
	}

	public static void main(String[] args) {
	 	ConVPS c = new ConVPS();
	 	System.out.println(c.con);
	}
}
