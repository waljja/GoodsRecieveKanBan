package ht.util;

import java.sql.*;

public class ConVPS
{
	private static String strDBDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String strDBUrl = "jdbc:sqlserver://172.31.2.26;databaseName=imslabel";
	/*    */ 
	/* 15 */   public Connection con = null;
	/* 16 */   private ResultSet rs = null;
	/* 17 */   private Statement stmt = null;
			   private String user = "htitsa";
	/* 20 */   private String password = "htitprogram";
	/*    */   public ConVPS() {
	/* 23 */     init();
	/*    */   }
	/*    */ 
	/*    */   public void init() {
	/*    */     try {
	/* 28 */       Class.forName(strDBDriver).newInstance();
	/* 29 */       this.con = DriverManager.getConnection(strDBUrl, this.user, this.password);
	
	/* 30 */       this.stmt = this.con
	/* 31 */         .createStatement();
	/*    */     } catch (InstantiationException e) {
	/* 33 */       e.printStackTrace();
	/*    */     } catch (IllegalAccessException e) {
	/* 35 */       e.printStackTrace();
	/*    */     } catch (ClassNotFoundException e) {
	/* 37 */       System.err.println("ConBean():" + e.getMessage());
	/*    */     } catch (SQLException e) {
	/* 39 */       System.err.println("ConBean:" + e.getMessage());
	/*    */     }
	/*    */   }
	/*    */ 
	/*    */   public ResultSet executeQuery(String sql) {
	/*    */     try {
	/* 45 */       this.stmt = this.con.createStatement();
	/* 46 */       this.rs = this.stmt.executeQuery(sql);
	/*    */     } catch (SQLException ex) {
	/* 48 */       ex.printStackTrace();
	/*    */     }
	/* 50 */     return this.rs;
	/*    */   }
	/*    */ 
	/*    */   public void executeUpdate(String sql) throws SQLException {
	/* 54 */     this.stmt = this.con.createStatement();
	/* 55 */       this.stmt.executeUpdate(sql);
	/*    */   }
	/*    */ 
	/*    */   public void close() {
	/*    */     try {
	/* 60 */       if (this.rs != null) {
	/* 61 */         this.rs.close();
	/* 62 */         this.rs = null;
	/*    */       }
	/*    */     } catch (Exception e) {
	/* 65 */       System.out.println("dbBean close rs error!");
	/*    */       try
	/*    */       {
	/* 68 */         if (this.stmt != null) {
	/* 69 */           this.stmt.close();
	/* 70 */           this.stmt = null;
	/*    */         }
	/*    */       } catch (Exception ex) {
	/* 73 */         System.out.println("dbBean close stmt error!");
	/*    */         try
	/*    */         {
	/* 76 */           if (this.con == null) return; this.con.close();
	/* 78 */           this.con = null;
	/*    */         }
	/*    */         catch (Exception ee) {
	/* 81 */           System.out.println("dbBean close con error!");
	/*    */         }
	/*    */       }
	/*    */       finally
	/*    */       {
	/*    */         try
	/*    */         {
	/* 76 */           if (this.con != null) {
	/* 77 */             this.con.close();
	/* 78 */             this.con = null;
	/*    */           }
	/*    */         } catch (Exception ex) {
	/* 81 */           System.out.println("dbBean close con error!");
	/*    */         }
	/*    */       }
	/*    */       try
	/*    */       {
	/* 76 */         if (this.con != null) {
	/* 77 */           this.con.close();
	/* 78 */           this.con = null;
	/*    */         }
	/*    */       } catch (Exception ex) {
	/* 81 */         System.out.println("dbBean close con error!");
	/*    */       }
	/*    */     }
	/*    */     finally
	/*    */     {
	/*    */       try
	/*    */       {
	/* 68 */         if (this.stmt != null) {
	/* 69 */           this.stmt.close();
	/* 70 */           this.stmt = null;
	/*    */         }
	/*    */       } catch (Exception e) {
	/* 73 */         System.out.println("dbBean close stmt error!");
	/*    */         try
	/*    */         {
	/* 76 */           if (this.con != null) {
	/* 77 */             this.con.close();
	/* 78 */             this.con = null;
	/*    */           }
	/*    */         } catch (Exception ex) {
	/* 81 */           System.out.println("dbBean close con error!");
	/*    */         }
	/*    */       }
	/*    */       finally
	/*    */       {
	/*    */         try
	/*    */         {
	/* 76 */           if (this.con != null) {
	/* 77 */             this.con.close();
	/* 78 */             this.con = null;
	/*    */           }
	/*    */         } catch (Exception e) {
	/* 81 */           System.out.println("dbBean close con error!");
	/*    */         }
	/*    */       }
	/*    */     }
	/*    */   }
	/*    */ 
	/*    */   public static void main(String[] args) {
	/* 88 */     ConVPS c = new ConVPS();
	/* 89 */     System.out.println(c.con);
	/*    */   }

	
}
