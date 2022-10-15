package ht.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConDashBoardTest {
	public static void main(String[] args) throws Exception {
		/*for(int i=0; i<10; i++) {
			System.out.println(DBConDashBoard.getInstance());
		}*/
		//System.out.println("new : "+new DBConDashBoard());
		
		/*PreparedStatement pstm = DBConDashBoard.getInstance().prepareStatement("select GRN from ToReceiveCheck where Sequence=?");
		pstm.setInt(1, 2059);
		ResultSet rs = pstm.executeQuery();
		while(rs.next()) {
			System.out.println(rs.getString(1));
		}*/
		
		PreparedStatement pstm = DBConDashBoard.getInstance().prepareStatement
		("select Sequence from ToReceiveCheck where GRN in (?)");  //只能采取拼接的方法
		pstm.setString(1, "5002958854','5002959763");
		ResultSet rs = pstm.executeQuery();
		while(rs.next()) {
			System.out.println(rs.getString(1));  
		}
	}
}
