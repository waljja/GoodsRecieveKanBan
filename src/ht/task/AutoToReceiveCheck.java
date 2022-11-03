package ht.task;

import ht.biz.IToReceiveCheckService;
import ht.entity.ToReceiveCheck;
import ht.util.ConDashBoard;
import ht.util.ConMes;
import ht.util.ConVPS;
import ht.util.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 1.待点收看板
 * @date 2020-9-3
 * @author 刘惠明
 *
 */

public class AutoToReceiveCheck {
	private static Log commonsLog = LogFactory.getLog(AutoToReceiveCheck.class);
    @Autowired
    private IToReceiveCheckService trCheckService;
	
	public void execute() throws Exception {
		commonsLog.info("start...");
		try {
			ConMes conMes = new ConMes();
			Connection connMes = conMes.con;
	    	ConVPS vpsDB = new ConVPS();
	    	Connection connVPS = vpsDB.con;
	    	ConDashBoard grnewdbDB = new ConDashBoard();
	    	Connection connDB = grnewdbDB.con;
	    	//
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			String nowDay = df.format(c.getTime());//当天
			String nowDayTime = df2.format(c.getTime());//当天
			c.add(Calendar.DATE, -2); // -2 天
			String nowDay_2 = df.format(c.getTime());
			// 8点-12点     13点-17点    18点-21点
			if( (nowDayTime.substring(11).compareTo("08:00") >0 && nowDayTime.substring(11).compareTo("12:00") <=0 ) 
					|| (nowDayTime.substring(11).compareTo("13:00") >0 && nowDayTime.substring(11).compareTo("17:00") <=0 )
					|| (nowDayTime.substring(11).compareTo("18:00") >0 && nowDayTime.substring(11).compareTo("21:00") <=0 )  ){
				c.setTime(new Date());
				c.add(Calendar.DATE, +1);
				String nowDayAdd2 = df.format(c.getTime());//跳过节假日后时间
				
				PreparedStatement pstmt = connDB.prepareStatement("select ExcludeDate from schedul_ExcludeDate where ExcludeDate=?");
				pstmt.setString(1, nowDayAdd2);
				ResultSet rsDay = pstmt.executeQuery();
				while(rsDay.next()) {
					c.add(Calendar.DATE, +1);
					nowDayAdd2 = df.format(c.getTime());
					pstmt.setString(1, nowDayAdd2);
					rsDay = pstmt.executeQuery();
				}
				//
				c.add(Calendar.DATE, +1);
				nowDayAdd2 = df.format(c.getTime());
				pstmt.setString(1, nowDayAdd2);
				rsDay = pstmt.executeQuery();
				while(rsDay.next()) {
					c.add(Calendar.DATE, +1);
					nowDayAdd2 = df.format(c.getTime());
					pstmt.setString(1, nowDayAdd2);
					rsDay = pstmt.executeQuery();
				}
				//
				c.add(Calendar.DATE, +1);
				String nowDayAdd4 = df.format(c.getTime());
				pstmt.setString(1, nowDayAdd4);
				rsDay = pstmt.executeQuery();
				while(rsDay.next()) {
					c.add(Calendar.DATE, +1);
					nowDayAdd4 = df.format(c.getTime());
					pstmt.setString(1, nowDayAdd4);
					rsDay = pstmt.executeQuery();
				}
				//
				c.add(Calendar.DATE, +1);
				nowDayAdd4 = df.format(c.getTime());
				pstmt.setString(1, nowDayAdd4);
				rsDay = pstmt.executeQuery();
				while(rsDay.next()) {
					c.add(Calendar.DATE, +1);
					nowDayAdd4 = df.format(c.getTime());
					pstmt.setString(1, nowDayAdd4);
					rsDay = pstmt.executeQuery();
				}
				//
				List<ToReceiveCheck> list = new ArrayList<ToReceiveCheck>();
				int seq = 1;
				ResultSet rs = grnewdbDB.executeQuery("select max(Sequence) as maxseq from ToReceiveCheck where Sequence is not null");
				if(rs.next()) {
					seq = rs.getInt("maxseq")+1;
				}
				// 131 DB modified by GuoZhao Ding
				PreparedStatement pstmtA1 = connMes.prepareStatement("select " +
						"ToStock_Input as stockname" +
						"from " +
						"[dbo].[UID_xTend_MaterialTransactionsRHDCDW]" +
						"where" +
						"UID = ?" +
						"and" +
						"(ToStock_Input is NOT NULL" +
						"or " +
						"ToStock_Input != '')");
				/*
				 * 这段唯一不同的地方是?
				 PreparedStatement pstmtA1 = connAegis.prepareStatement("select FRB.Name as stockname from ItemInventories II " +
						" left join ItemTypes IT on IT.ID = II.ItemTypeID " +
						" left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
						" where II.Identifier = ? and (FRB.Name is not null and FRB.Name <> '') ");
				*/
				// VN版本是 VN_TransportOrder 和 VN_TransportOrderItem 表
				PreparedStatement pstmtA3 = connMes.prepareStatement("select RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
						" where PartNumber = ? and convert(varchar(10),RequireTime,23) =? order by RequireTime ");
				PreparedStatement pstmtDB1 = connDB.prepareStatement("select inventory,needQty,gotQty,soStartDate " +
						" from NotFinishSO where plant=? and bom=? order by soStartDate ");
				PreparedStatement pstmtDB2 = connDB.prepareStatement("select * from ToReceiveCheck where closeDate is not null " +
							" and GRN=? ");
				// vendorrid
				// 1.查询2天内VPS所有过账('1100','5000')
				rs = vpsDB.executeQuery("select * from (select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103, UpAegisDATE from vendorrid " +
						" where convert(varchar(10),GRNDATE,23) between '"+nowDay_2+"' and '"+nowDay+"' and printQTY<>0.0 and plent in ('1100','5000')" +
						 "union  select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103, UpAegisDATE from pcbvendorrid " +
						" where convert(varchar(10),GRNDATE,23) between '"+nowDay_2+"' and '"+nowDay+"' and printQTY<>0.0 and plent in ('1100','5000') )m  order by grn, partNumber"); //and UpAegisDATE is not null
				// rsMap：存放closedate为null的数 key:grn+pn value:String[] dou(统计对象数组)
				Map<String,String[]> rsMap=new HashMap<String,String[]>();
				System.out.println("test1:"+new Date());
				while (rs.next()) {
					System.out.println("rsMap："+rsMap.size());
					pstmtDB2.setString(1, rs.getString("grn"));
					// 2.根据vps提供的过账GRN查询看板 closeDate不为null的数据
					 ResultSet rsFinish  = pstmtDB2.executeQuery();
					// GRN 没有关闭
					if(!rsFinish.next()) {
						String[] dou=new String[8];
						String grn = rs.getString("grn");
						String pn = rs.getString("partNumber");
						// 2.1将同rsMap: grn pn 为key,看板对象数组为value,统计uid数量，grn数量
						if(rsMap.containsKey(grn+pn)){
							dou=rsMap.get(grn+pn);
							dou[0]=rs.getString("partNumber");
							dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
							dou[2]=(Integer.parseInt(dou[2])+1) + "";
							dou[3]=rs.getString("rid");
							dou[4]=rs.getString("plent");
							dou[5]=rs.getString("GRNDATE");
							dou[6]=rs.getString("GRN103");
							dou[7]=rs.getString("UpAegisDATE");
							rsMap.put(grn+pn, dou);
						}else{
							dou[0]=rs.getString("partNumber");
							dou[1]=rs.getString("printQTY");
							dou[2]="1";
							dou[3]=rs.getString("rid");
							dou[4]=rs.getString("plent");
							dou[5]=rs.getString("GRNDATE");
							dou[6]=rs.getString("GRN103");
							dou[7]=rs.getString("UpAegisDATE");
							rsMap.put(grn+pn, dou);
						}
					}
				}
				System.out.println("test2:"+new Date());
				commonsLog.info("1 vendorrid size:"+rsMap.size());
				for(String key : rsMap.keySet()) { 
					String[] temp = rsMap.get(key);
					pstmtA1.setString(1, temp[3]);//temp[3] == UID
					//3.根据看板对象数组的rid 查询Aegis
					ResultSet rsA = pstmtA1.executeQuery();
					if(!rsA.next()){//查询到代表UID绑库，查询不到代表没有绑定库位
						//3.1封装看板对象
						ToReceiveCheck trc = new ToReceiveCheck();
						trc.setGRN(key.substring(0, 10));
						trc.setItemNumber(temp[0]);
						trc.setGRNQuantity(temp[1]);
						trc.setUIDQuantity(temp[2]);
						pstmtA3.setString(1, temp[0]);
						pstmtA3.setString(2, nowDay);
						//
						ResultSet rsB = pstmtA3.executeQuery();
						if(rsB.next()) {
							trc.setProductionTime(rsB.getString("RequireTime").substring(0, 10));
						}else {
							//根据工厂 物料 取工单开始日期
							double totalInventory = 0.0;
							String soStartDate = "NA";
							pstmtDB1.setString(1, temp[4]);
							pstmtDB1.setString(2, temp[0]);
							ResultSet rs31  = pstmtDB1.executeQuery();
							if(rs31.next()) {
								if(!"null".equals(rs31.getString("inventory"))) {
									totalInventory = rs31.getDouble("inventory");
								}
							}
							ResultSet rs32  = pstmtDB1.executeQuery();
							while(rs32.next()) {
								totalInventory = totalInventory - (rs32.getDouble("needQty") - rs32.getDouble("gotQty"));
								if(totalInventory < 0.0) {
									soStartDate = rs32.getString("soStartDate");
									break;
								}
							}
							trc.setProductionTime(soStartDate);
						}
						trc.setType("");
						if(!"NA".equals(trc.getProductionTime()) ){
							if(trc.getProductionTime().compareTo(nowDayAdd2) <= 0) {
								trc.setType("A");
							}else if(trc.getProductionTime().compareTo(nowDayAdd4) <= 0) {
								trc.setType("B");
							}else{
								continue;
							}
						}else {
							continue;
						}
						trc.setUID(temp[3]);
						trc.setPlant(temp[4]);
						trc.setGRNDATE(temp[5]);
						trc.setGRN103(temp[6]);
						//计算 待收料等待时间  temp[7]   上传aegis时间----
						if(temp[7]!=null && !"".equals(temp[7]) && !"null".equalsIgnoreCase(temp[7])) {
							String grnDateTime = temp[5].substring(0, 16);
							if(temp[6]!=null && !"".equals(temp[6]) && !"null".equalsIgnoreCase(temp[6])) {  //103/105，18-24-08 点 全算作08：00
								trc.setGRNDATE(temp[7]); //UpAegisDATE
								grnDateTime = temp[7].substring(0, 16); //UpAegisDATE
								if(grnDateTime.substring(11).compareTo("18:00") > 0) {
									c.setTime(df.parse(grnDateTime.substring(0, 10)));
									c.add(Calendar.DATE, +1);
									String grnNextDay = df.format(c.getTime());
									grnDateTime = grnNextDay + " 08:00";
								}
								if(grnDateTime.substring(11).compareTo("08:00") < 0) {
									c.setTime(df.parse(grnDateTime.substring(0, 10)));
									grnDateTime = grnDateTime.substring(0, 10) + " 08:00";
								}
							}else { //101 21：00-08：00 全算 08：00
								if(grnDateTime.substring(11).compareTo("21:00") > 0) {
									c.setTime(df.parse(grnDateTime.substring(0, 10)));
									c.add(Calendar.DATE, +1);
									String grnNextDay = df.format(c.getTime());
									grnDateTime = grnNextDay + " 08:00";
								}
								if(grnDateTime.substring(11).compareTo("08:00") < 0) {
									c.setTime(df.parse(grnDateTime.substring(0, 10)));
									grnDateTime = grnDateTime.substring(0, 10) + " 08:00";
								}
							}
							if(grnDateTime.compareTo(nowDayTime) > 0) {
								trc.setWaittime("0.0");
							}else {
								
								int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(grnDateTime.substring(0, 10)) );
								int hour = 0;
								int min = 0;
								String grn_log = "";
								if(day==0) {  // 同一天
									hour = Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(grnDateTime.substring(11, 13));
									min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(grnDateTime.substring(14, 16));
									min = min + calFoodTime(grnDateTime, nowDayTime);
									grn_log = trc.getGRN() + ":grnDateTime:"+grnDateTime +"   nowDayTime:"+nowDayTime +"hour:"+hour+" min:"+min +"1 FoodTime:"+calFoodTime(grnDateTime, nowDayTime);
						            grnewdbDB.executeUpdate("insert into grn_log(grn,grn_log)values('"+trc.getGRN()+"','"+grn_log+"')");
								}else {
									day = day-1;
									hour = day*11;
									hour = hour + 21 - Integer.parseInt(grnDateTime.substring(11, 13));
									min = min + Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(grnDateTime.substring(14, 16));
									min = min + calFoodTime(grnDateTime, grnDateTime.substring(0, 10)+" 21:00");
									hour = hour + Integer.parseInt(nowDayTime.substring(11, 13)) - 8;
									min = min + calFoodTime(nowDayTime.substring(0, 10)+" 08:00", nowDayTime);
									System.out.println(trc.getGRN() + " 2 FoodTime:"+calFoodTime(nowDayTime.substring(0, 10)+" 08:00", nowDayTime));
									grn_log = trc.getGRN() + ":grnDateTime:"+grnDateTime +"   nowDayTime:"+nowDayTime +"hour:"+hour+" min:"+min+"2 FoodTime:"+calFoodTime(nowDayTime.substring(0, 10)+" 08:00", nowDayTime);
						            grnewdbDB.executeUpdate("insert into grn_log(grn,grn_log)values('"+trc.getGRN()+"','"+grn_log+"')");
								}
								min = hour * 60 + min;
								double minToHour = min/60.0;
								trc.setWaittime(String.format("%.2f", minToHour));
							}
						}else{
							trc.setWaittime("NA");
						}
						trc.setSequence(seq);
						trc.setCreatedate(nowDayTime);
						list.add(trc);
					}else { //绑库了 更新closeDate，考虑删除此GRN的历史刷新
						System.out.println("update ToReceiveCheck set closeDate = (select max(createdate) from ToReceiveCheck where GRN='"+key.substring(0, 10)+"')  where " +
								" Sequence = (select max(Sequence) from ToReceiveCheck where GRN='"+key.substring(0, 10)+"') " +
								" and GRN='"+key.substring(0, 10)+"' and closeDate is null ");
						grnewdbDB.executeUpdate("update ToReceiveCheck set closeDate = (select max(createdate) from ToReceiveCheck where GRN='"+key.substring(0, 10)+"')  where " +
								" Sequence = (select max(Sequence) from ToReceiveCheck where GRN='"+key.substring(0, 10)+"') " +
								" and GRN='"+key.substring(0, 10)+"' and closeDate is null ");
						System.out.println("delete from ToReceiveCheck where closeDate is null and GRN='"+key.substring(0, 10)+"'");
						grnewdbDB.executeUpdate("delete from ToReceiveCheck where closeDate is null and GRN='"+key.substring(0, 10)+"'");
					}
				}
				trCheckService.saveToReceiveCheck(list);
			}
			vpsDB.close();
			conMes.close();
			grnewdbDB.close();
		} catch (Exception e) {
			commonsLog.error("Exception:", e);
		}
		commonsLog.info("end...");
	}
	
	//减去 用餐时间
	public int calFoodTime(String startDateTime, String endDateTime) {
		int min = 0;
		if(startDateTime.substring(11).compareTo("12:00") < 0) {
			if(endDateTime.substring(11).compareTo("18:00") >= 0){
				min = min - 120;
			}else if(endDateTime.substring(11).compareTo("13:00") >= 0){
				min = min - 60;
			}
		}else if(startDateTime.substring(11).compareTo("13:00") < 0) {
			int grnTimeTo12 = Integer.parseInt(startDateTime.substring(14, 16));
			if(endDateTime.substring(11).compareTo("18:00") >= 0){
				min = min - 120 + grnTimeTo12;
			}else if(endDateTime.substring(11).compareTo("13:00") >= 0){
				min = min - 60 + grnTimeTo12;
			}
		}else if(startDateTime.substring(11).compareTo("17:00") < 0) {
			if(endDateTime.substring(11).compareTo("18:00") >= 0){
				min = min - 60;
			}
		}else if(startDateTime.substring(11).compareTo("18:00") < 0) {
			int grnTimeTo17 = Integer.parseInt(startDateTime.substring(14, 16));
			if(endDateTime.substring(11).compareTo("18:00") >= 0){
				min = min - 60 + grnTimeTo17;
			}
		}
		return min;
	}
	public static void main(String[] args) {
		try {
			new AutoToReceiveCheck().execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}