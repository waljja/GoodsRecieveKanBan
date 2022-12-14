package ht.util;

import java.sql.*;

 // @Scope("prototype")
public class ConAegis
{
	private static String strDBDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String strDBUrl = "jdbc:sqlserver://172.31.2.108;databaseName=HT_FactoryLogix";

	public Connection con = null;
	private ResultSet rs = null;
	private Statement stmt = null;
	private String user = "Aegis-sa";
	private String password = "ABC789_";
	public ConAegis() {
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

	public boolean executeUpdate(String sql) {
			boolean flag = true;
			try {
				this.stmt = this.con.createStatement();
				this.stmt.executeUpdate(sql);
			} catch(SQLException ex) {
				flag = false;
				System.out.println(ex.getMessage());
			}
			return flag;
	}

	public void close() {
	  try {
	    if (this.rs != null) {
	      this.rs.close();
	      this.rs = null;
	    }
	  } catch (Exception e) {
	    System.out.println("dbBean close rs error!");
	    try
	    {
	      if (this.stmt != null) {
	        this.stmt.close();
	        this.stmt = null;
	      }
	    } catch (Exception ex) {
	      System.out.println("dbBean close stmt error!");
	      try
	      {
	        if (this.con == null) return; this.con.close();
	        this.con = null;
	      }
	      catch (Exception ee) {
	        System.out.println("dbBean close con error!");
	      }
	    }
	    finally
	    {
	      try
	      {
	        if (this.con != null) {
	          this.con.close();
	          this.con = null;
	        }
	      } catch (Exception ex) {
	        System.out.println("dbBean close con error!");
	      }
	    }
	    try
	    {
	      if (this.con != null) {
	        this.con.close();
	        this.con = null;
	      }
	    } catch (Exception ex) {
	      System.out.println("dbBean close con error!");
	    }
	  }
	  finally
	  {
	    try
	    {
	      if (this.stmt != null) {
	        this.stmt.close();
	        this.stmt = null;
	      }
	    } catch (Exception e) {
	      System.out.println("dbBean close stmt error!");
	      try
	      {
	        if (this.con != null) {
	          this.con.close();
	          this.con = null;
	        }
	      } catch (Exception ex) {
	        System.out.println("dbBean close con error!");
	      }
	    }
	    finally
	    {
	      try
	      {
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
	  ConAegis c = new ConAegis();
	  System.out.println(c.con);
	}
}
