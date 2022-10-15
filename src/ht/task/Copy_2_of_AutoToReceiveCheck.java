package ht.task;

import ht.biz.IToReceiveCheckService;
import ht.dao.INotFinishSODao;
import ht.entity.NotFinishSO;
import ht.entity.ToReceiveCheck;
import ht.util.*;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @date 2020-9-3
 * @author 刘惠明
 *
 */

public class Copy_2_of_AutoToReceiveCheck {
    @Autowired
    private IToReceiveCheckService trCheckService;
	
	public void execute() throws Exception {
		System.out.println("start...AutoToReceiveCheck "+new Date());
		
		try {
			ConMes conMes = new ConMes();
	    	ConVPS vpsDB = new ConVPS();
	    	ConAegis aegisDB = new ConAegis();
	    	ConDashBoard grnewdbDB = new ConDashBoard();
	    	//
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			String nowDay = df.format(c.getTime());
			String nowDayTime = df2.format(c.getTime());
			c.add(Calendar.DATE, -2); // -2 天
			String nowDay_2 = df.format(c.getTime());
			//
			c.setTime(new Date());
			c.add(Calendar.DATE, +1);
			String nowDayAdd2 = df.format(c.getTime());
			ResultSet rsDay = grnewdbDB.executeQuery("select ExcludeDate from schedul_ExcludeDate where ExcludeDate='"+nowDayAdd2+"'");
			while(rsDay.next()) {
				c.add(Calendar.DATE, +1);
				nowDayAdd2 = df.format(c.getTime());
				rsDay = grnewdbDB.executeQuery("select ExcludeDate from schedul_ExcludeDate where ExcludeDate='"+nowDayAdd2+"'");
			}
			//
			c.add(Calendar.DATE, +1);
			nowDayAdd2 = df.format(c.getTime());
			rsDay = grnewdbDB.executeQuery("select ExcludeDate from schedul_ExcludeDate where ExcludeDate='"+nowDayAdd2+"'");
			while(rsDay.next()) {
				c.add(Calendar.DATE, +1);
				nowDayAdd2 = df.format(c.getTime());
				rsDay = grnewdbDB.executeQuery("select ExcludeDate from schedul_ExcludeDate where ExcludeDate='"+nowDayAdd2+"'");
			}
			//
			c.add(Calendar.DATE, +1);
			String nowDayAdd4 = df.format(c.getTime());
			rsDay = grnewdbDB.executeQuery("select ExcludeDate from schedul_ExcludeDate where ExcludeDate='"+nowDayAdd4+"'");
			while(rsDay.next()) {
				c.add(Calendar.DATE, +1);
				nowDayAdd4 = df.format(c.getTime());
				rsDay = grnewdbDB.executeQuery("select ExcludeDate from schedul_ExcludeDate where ExcludeDate='"+nowDayAdd4+"'");
			}
			//
			c.add(Calendar.DATE, +1);
			nowDayAdd4 = df.format(c.getTime());
			rsDay = grnewdbDB.executeQuery("select ExcludeDate from schedul_ExcludeDate where ExcludeDate='"+nowDayAdd4+"'");
			while(rsDay.next()) {
				c.add(Calendar.DATE, +1);
				nowDayAdd4 = df.format(c.getTime());
				rsDay = grnewdbDB.executeQuery("select ExcludeDate from schedul_ExcludeDate where ExcludeDate='"+nowDayAdd4+"'");
			}
			//
			//trCheckService.deleteAllToReceiveCheck();
			//vendorrid
			ResultSet rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103 from vendorrid " +
					" where convert(varchar(10),GRNDATE,23) between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000') ");
			Map<String,String[]> rsMap=new HashMap<String,String[]>();
			while (rs.next()) {
				String[] dou=new String[7];
				String grn = rs.getString("grn");
				String pn = rs.getString("partNumber");
				if(rsMap.containsKey(grn+pn)){
					dou=rsMap.get(grn+pn);
					dou[0]=rs.getString("partNumber");
					dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
					dou[2]=(Integer.parseInt(dou[2])+1) + "";
					dou[3]=rs.getString("rid");
					dou[4]=rs.getString("plent");
					dou[5]=rs.getString("GRNDATE");
					dou[6]=rs.getString("GRN103");
					rsMap.put(grn+pn, dou);
				}else{
					dou[0]=rs.getString("partNumber");
					dou[1]=rs.getString("printQTY");
					dou[2]="1";
					dou[3]=rs.getString("rid");
					dou[4]=rs.getString("plent");
					dou[5]=rs.getString("GRNDATE");
					dou[6]=rs.getString("GRN103");
					rsMap.put(grn+pn, dou);
				}
			}
			System.out.println("AutoToReceiveCheck");
			System.out.println("vendorrid size:"+rsMap.size());
			for(String key : rsMap.keySet()) { 
				String[] temp = rsMap.get(key);
				ResultSet rsA = conMes.executeQuery(SqlStatements.findStockNull(temp[3])); // 131 DB Modified by GuoZhao Ding

				/*
					ResultSet rsA = conMes.executeQuery(" select FRB.Name as stockname from ItemInventories II " +
						" left join ItemTypes IT on IT.ID = II.ItemTypeID " +
						" left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
						" where II.Identifier = '"+temp[3]+"' and (FRB.Name is null or FRB.Name = '') ");
				*/
					
				if(rsA.next()){
					ToReceiveCheck trc = new ToReceiveCheck();
					trc.setGRN(key.substring(0, 10));
					trc.setItemNumber(temp[0]);
					trc.setGRNQuantity(temp[1]);
					trc.setUIDQuantity(temp[2]);
					ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
							" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
					if(rsB.next()) {
						trc.setProductionTime(rsB.getString("RequireTime").substring(0, 10));
					}else {
						//根据工厂 物料 取工单开始日期
						double totalInventory = 0.0;
						String soStartDate = "NA";
						String sql = "select inventory,needQty,gotQty,soStartDate " +
						" from NotFinishSO where plant='"+temp[4]+"' and bom='"+temp[0]+"' order by soStartDate ";
						ResultSet rs31  = grnewdbDB.executeQuery(sql);
						if(rs31.next()) {
							totalInventory = rs31.getDouble("inventory");
						}
						ResultSet rs32  = grnewdbDB.executeQuery(sql);
						while(rs32.next()) {
							totalInventory = totalInventory - (rs32.getDouble("needQty") - rs32.getDouble("gotQty"));
							if(totalInventory < 0.0) {
								soStartDate = rs32.getString("soStartDate");
								break;
							}
						}
						trc.setProductionTime(soStartDate);
					}
					if(!"NA".equals(trc.getProductionTime()) ){
						if(trc.getProductionTime().compareTo(nowDay) <= 0) {
							trc.setType("A");
						}else if(trc.getProductionTime().compareTo(nowDayAdd2) <= 0) {
							trc.setType("B");
						}else if(trc.getProductionTime().compareTo(nowDayAdd4) <= 0) {
							trc.setType("C");
						}
					}
					trc.setUID(temp[3]);
					trc.setPlant(temp[4]);
					trc.setGRNDATE(temp[5]);
					trc.setGRN103(temp[6]);
					//计算 待收料等待时间
					String grnDateTime = temp[5].substring(0, 16);
					//System.out.println("vendorrid GRNDATE,nowDayTime:"+grnDateTime+", "+nowDayTime);
					if(nowDayTime.substring(11).compareTo("21:00") > 0) {
						nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
					}
					if(nowDayTime.substring(11).compareTo("08:00") < 0) {
						nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
					}
					if(!"".equals(temp[6]) && !"null".equalsIgnoreCase(temp[6])) {  //105
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
					}else { //101
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
						trc.setWaittime("");
					}else {
						int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(grnDateTime.substring(0, 10)) );
						int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(grnDateTime.substring(11, 13));
						int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(grnDateTime.substring(14, 16));
						if(min < 0) {
							min = 60 + min;
							hour = hour - 1;
						}
						trc.setWaittime(hour+"小时"+min+"分钟");
					}
				    //trCheckService.saveToReceiveCheck(trc);
				} 
			}
			//pcbvendorrid
			rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103 from pcbvendorrid " +
					" where convert(varchar(10),GRNDATE,23) between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000') ");
			rsMap=new HashMap<String,String[]>();
			while (rs.next()) {
				String[] dou=new String[7];
				String grn = rs.getString("grn");
				String pn = rs.getString("partNumber");
				if(rsMap.containsKey(grn+pn)){
					dou=rsMap.get(grn+pn);
					dou[0]=rs.getString("partNumber");
					dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
					dou[2]=(Integer.parseInt(dou[2])+1) + "";
					dou[3]=rs.getString("rid");
					dou[4]=rs.getString("plent");
					dou[5]=rs.getString("GRNDATE");
					dou[6]=rs.getString("GRN103");
					rsMap.put(grn+pn, dou);
				}else{
					dou[0]=rs.getString("partNumber");
					dou[1]=rs.getString("printQTY");
					dou[2]="1";
					dou[3]=rs.getString("rid");
					dou[4]=rs.getString("plent");
					dou[5]=rs.getString("GRNDATE");
					dou[6]=rs.getString("GRN103");
					rsMap.put(grn+pn, dou);
				}
			}
			System.out.println("pcbvendorrid size:"+rsMap.size());
			for(String key : rsMap.keySet()) {
				String[] temp = rsMap.get(key);
				ResultSet rsA = aegisDB.executeQuery(" select FRB.Name from ItemInventories II " +
						" left join ItemTypes IT on IT.ID = II.ItemTypeID " +
						" left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
						" where II.Identifier = '"+temp[3]+"' and (FRB.Name is null or FRB.Name = '') ");
				//" where II.Identifier = '"+temp[3]+"' and FRB.Name is not null and FRB.Name <> '' ");
				if(rsA.next()){
					//System.out.println("pcbvendorrid UID:"+temp[3]);
					ToReceiveCheck trc = new ToReceiveCheck();
					trc.setGRN(key.substring(0, 10));
					trc.setItemNumber(temp[0]);
					trc.setGRNQuantity(temp[1]);
					trc.setUIDQuantity(temp[2]);
					ResultSet rsB = aegisDB.executeQuery("select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
							" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
					if(rsB.next()) {
						trc.setProductionTime(rsB.getString("RequireTime").substring(0, 10));
					}else {
						//根据工厂 物料 取工单开始日期
						double totalInventory = 0.0;
						String soStartDate = "NA";
						String sql = "select inventory,needQty,gotQty,soStartDate " +
						" from NotFinishSO where plant='"+temp[4]+"' and bom='"+temp[0]+"' order by soStartDate ";
						ResultSet rs31  = grnewdbDB.executeQuery(sql);
						if(rs31.next()) {
							totalInventory = rs31.getDouble("inventory");
						}
						ResultSet rs32  = grnewdbDB.executeQuery(sql);
						while(rs32.next()) {
							totalInventory = totalInventory - (rs32.getDouble("needQty") - rs32.getDouble("gotQty"));
							if(totalInventory < 0.0) {
								soStartDate = rs32.getString("soStartDate");
								break;
							}
						}
						trc.setProductionTime(soStartDate);
					}
					if(!"NA".equals(trc.getProductionTime()) ){
						if(trc.getProductionTime().compareTo(nowDay) <= 0) {
							trc.setType("A");
						}else if(trc.getProductionTime().compareTo(nowDayAdd2) <= 0) {
							trc.setType("B");
						}else if(trc.getProductionTime().compareTo(nowDayAdd4) <= 0) {
							trc.setType("C");
						}
					}
					trc.setUID(temp[3]);
					trc.setPlant(temp[4]);
					trc.setGRNDATE(temp[5]);
					trc.setGRN103(temp[6]);
					//计算 待收料等待时间
					String grnDateTime = temp[5].substring(0, 16);
					//System.out.println("vendorrid GRNDATE,nowDayTime:"+grnDateTime+", "+nowDayTime);
					if(nowDayTime.substring(11).compareTo("21:00") > 0) {
						nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
					}
					if(nowDayTime.substring(11).compareTo("08:00") < 0) {
						nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
					}
					if(!"".equals(temp[6]) && !"null".equalsIgnoreCase(temp[6])) {  //105
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
					}else { //101
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
						trc.setWaittime("");
					}else {
						int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(grnDateTime.substring(0, 10)) );
						int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(grnDateTime.substring(11, 13));
						int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(grnDateTime.substring(14, 16));
						if(min < 0) {
							min = 60 + min;
							hour = hour - 1;
						}
						trc.setWaittime(hour+"小时"+min+"分钟");
					}
					//trCheckService.saveToReceiveCheck(trc);
				}
			}
			//
			rs = grnewdbDB.executeQuery("select top 1 Sequence from ToReceiveCheck where Sequence is not null order by Sequence desc ");
			if(rs.next()) {
				grnewdbDB.executeUpdate("update ToReceiveCheck set Sequence="+(rs.getInt("Sequence") + 1)+" where Sequence is null ");
			}else{
				grnewdbDB.executeUpdate("update ToReceiveCheck set Sequence=1 where Sequence is null ");
			}
			//
			vpsDB.close();
			aegisDB.close();
			grnewdbDB.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("end...AutoToReceiveCheck "+new Date());
	}
}