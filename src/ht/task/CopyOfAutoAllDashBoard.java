package ht.task;

import ht.biz.IToReceiveCheckService;
import ht.biz.IToReceiveWarehouseBService;
import ht.biz.IToReceiveWarehouseService;
import ht.biz.IUrgentMaterialCheckNotOCRService;
import ht.biz.IUrgentMaterialCheckOCRService;
import ht.dao.INotFinishSODao;
import ht.entity.NotFinishSO;
import ht.entity.ToReceiveCheck;
import ht.entity.ToReceiveWarehouse;
import ht.entity.ToReceiveWarehouseB;
import ht.entity.UrgentMaterialCheckNotOCR;
import ht.entity.UrgentMaterialCheckOCR;
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

public class CopyOfAutoAllDashBoard {
    @Autowired
    private IToReceiveCheckService trCheckService;
    @Autowired
    private IUrgentMaterialCheckNotOCRService umCheckNotOCRService;
    @Autowired
    private IUrgentMaterialCheckOCRService umCheckOCRService;
	@Autowired
    private IToReceiveWarehouseService trWarehouseService;
    @Autowired
    private IToReceiveWarehouseBService trWarehouseBService;
    
	public void execute() throws Exception {
		System.out.println("start...AutoAllDashBoard "+new Date());
		try {
			ConMes conMes = new ConMes();
	    	ConVPS vpsDB = new ConVPS();
	    	ConAegis aegisDB = new ConAegis();
	    	ConDashBoard grnewdbDB = new ConDashBoard();
	    	ConOCR ocrDB = new ConOCR();
	    	//
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			String nowDay = df.format(c.getTime());
			String nowDayTime = df2.format(c.getTime());
			c.add(Calendar.DATE, -2); // -2 天
			String nowDay_2 = df.format(c.getTime());
			c.add(Calendar.DATE, -5); // -5 天
			String nowDay_30 = df.format(c.getTime());
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
			//part01
			//trCheckService.deleteAllToReceiveCheck();
			//vendorrid
			ResultSet rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103 from vendorrid " +
					" where GRNDATE between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000') ");
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
				ResultSet rsA = conMes.executeQuery(SqlStatements.findStock(temp[3])); // 131 DB modified by GuoZhao Ding

				/*
					ResultSet rsA = aegisDB.executeQuery(" select FRB.Name as stockname from ItemInventories II " +
						" left join ItemTypes IT on IT.ID = II.ItemTypeID " +
						" left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
						" where II.Identifier = '"+temp[3]+"' and FRB.Name is not null and FRB.Name <> '' ");
				*/

				if(!rsA.next()){
					//System.out.println("vendorrid UID:"+temp[3]);
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
					" where GRNDATE between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000') ");
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
						" where II.Identifier = '"+temp[3]+"' and FRB.Name is not null and FRB.Name <> '' ");
				if(!rsA.next()){
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
			
			//part02
			rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent, GRNDATE from vendorrid " +
	        		" where GRNDATE between '"+nowDay_30+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000')  and ( " +
	                " partNumber not like '009%' and partNumber not like '020%' and partNumber not like  '021%' and partNumber not like '023%' and " +
	                " partNumber not like '035%' and partNumber not like '040%' and partNumber not like  '050%' and partNumber not like '051%' and " +
	                " partNumber not like '059%' and partNumber not like '110%' and partNumber not like  '121%' and partNumber not like '123%' and " +
	                " partNumber not like '129%' and partNumber not like '140%' and partNumber not like  '147%' and partNumber not like '155%' and " +
	                " partNumber not like '233%' and partNumber not like '237%' and partNumber not like  '262%' and partNumber not like '265%' and " +
	                " partNumber not like '275%' and partNumber not like '470%' " +
	                " )");
	        rsMap=new HashMap<String,String[]>();
	        while (rs.next()) {
	            String[] dou=new String[6];
	            String pn = rs.getString("partNumber");
	            String grnDate = rs.getString("GRNDATE").substring(0, 10);
	            if(rsMap.containsKey(pn+grnDate)){
	                dou=rsMap.get(pn+grnDate);
	                dou[0]=rs.getString("partNumber");
	                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
	                dou[2]=(Integer.parseInt(dou[2])+1) + "";
	                if(dou[3].length()< 4000) {
	                	dou[3]="'"+rs.getString("rid")+"',"+dou[3]+"";
	                }
	                dou[4]=rs.getString("plent");
	                dou[5]=rs.getString("grn");
	                rsMap.put(pn+grnDate, dou);
	            }else{
	                dou[0]=rs.getString("partNumber");
	                dou[1]=rs.getString("printQTY");
	                dou[2]="1";
	                dou[3]="'"+rs.getString("rid")+"'";
	                dou[4]=rs.getString("plent");
	                dou[5]=rs.getString("grn");
	                rsMap.put(pn+grnDate, dou);
	            }
	        }
	        System.out.println("AutoUrgentMaterialCheckNotOCR");
	        System.out.println("vendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            //System.out.println(temp[3]);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " left join StockLocations SL on SL.ID = IIH.StockLocationID" +
	                    " where (SL.Identifier like'%IQ%') and II.Identifier in ("+temp[3]+") " +
	            		" order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            if(rsA.next()){ //有IQ库位，但此UID，还有QM在后，表示已归还
	            	String uid = rsA.getString("Identifier");
	            	System.out.println("Identifier:"+uid);
	            	ResultSet rsA2 = aegisDB.executeQuery(" select top 1 II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
		            		" from ItemInventories II " +
		                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
		                    " left join StockLocations SL on SL.ID = IIH.StockLocationID" +
		                    " where II.Identifier = '"+uid+"' and SL.Identifier is not null " +
		            		" order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            	if(rsA2.next() && rsA2.getString("historyStock").contains("QM")){
	            		//有IQ库位，但此UID，还有QM在后，表示已归还
	            	}else{
	            		UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
		                umcn.setGRN(temp[5]);
		                umcn.setItemNumber(temp[0]);
		                ResultSet rsC = aegisDB.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
		                        "where ReceivingNumber='"+temp[5]+"'");
		                if (rsC.next()) {
		                    umcn.setRDFinishTime(rsC.getString("CreateDate").substring(0,16));//收货时间
		                }else {
		                    umcn.setRDFinishTime("");
		                }
		                umcn.setIQCGetTime(rsA.getString("localtime").substring(0,16));//IQC取样时间
	                	//
	                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                    ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                            " where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                    if(rsB.next()) {
	                        umcn.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                    }else {
	                        //根据工厂 物料 取工单开始日期
	                        double totalInventory = 0.0;
	                        double leftInventory = 0.0;
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
	                        umcn.setProductionTime(soStartDate);
	                    }
	                    if(!"NA".equals(umcn.getProductionTime()) ){
    						if(umcn.getProductionTime().compareTo(nowDay) <= 0) {
    							umcn.setType("A");
    						}else if(umcn.getProductionTime().compareTo(nowDayAdd2) <= 0) {
    							umcn.setType("B");
    						}else if(umcn.getProductionTime().compareTo(nowDayAdd4) <= 0) {
    							umcn.setType("C");
    						}
    					}
	                    umcn.setUID("");  //temp[3].replaceAll("'", "").substring(0,21)
	                    umcn.setPlant(temp[4]);
	                    //计算IQC检验等待时间
	                    String IQCGetTime = umcn.getIQCGetTime();              
	                    if (!"".equals(IQCGetTime)&&!"null".equalsIgnoreCase(IQCGetTime)) {
	                        //System.out.println("IQCGETTIME---"+IQCGetTime);
	                        if (IQCGetTime.substring(11).compareTo("21:00")>0) {
	                            IQCGetTime = IQCGetTime.substring(0, 10) + " 21:00";
	                        }
	                        if (IQCGetTime.substring(11).compareTo("08:00")<0) {
	                            IQCGetTime = IQCGetTime.substring(0, 10) + " 08:00";
	                        }
	                    
	                        if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                        }
	                        if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                        }
	                        
	                        if(IQCGetTime.compareTo(nowDayTime) > 0) {
	                            umcn.setIQCCheckWaitTime("");
	                        }else {
	                            int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(IQCGetTime.substring(0, 10)) );
	                            int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(IQCGetTime.substring(11, 13));
	                            int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(IQCGetTime.substring(14, 16));
	                            if(min < 0) {
	                                min = 60 + min;
	                                hour = hour - 1;
	                            }
	                            umcn.setIQCCheckWaitTime(hour+"小时"+min+"分钟");
	                        } 
	                    }else{
	                        umcn.setIQCCheckWaitTime("");
	                    }
	                    //umCheckNotOCRService.saveUrgentMaterialCheckNotOCR(umcn);
	            	}
	            }else {
	            	rsA = aegisDB.executeQuery(" select top 1 II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            			" from ItemInventories II " +
	            			" left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	            			" left join StockLocations SL on SL.ID = IIH.StockLocationID" +
	            			" where (SL.Identifier like'%QM%') and II.Identifier = '"+temp[3].replaceAll("'", "").substring(0,21)+"' " +
		            		" order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            	if(rsA.next()) {
	            		UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
		                umcn.setGRN(temp[5]);
		                umcn.setItemNumber(temp[0]);
		                ResultSet rsC = aegisDB.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
		                        "where ReceivingNumber='"+temp[5]+"'");
		                if (rsC.next()) {
		                    umcn.setRDFinishTime(rsC.getString("CreateDate").substring(0,16));//收货时间
		                }else {
		                    umcn.setRDFinishTime("");
		                }
		                //
		            	umcn.setIQCGetTime("");
	                    //
	                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                    ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                            " where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                    if(rsB.next()) {
	                        umcn.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                    }else {
	                        //根据工厂 物料 取工单开始日期
	                        double totalInventory = 0.0;
	                        double leftInventory = 0.0;
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
	                        umcn.setProductionTime(soStartDate);
	                    }
	                    if(!"NA".equals(umcn.getProductionTime()) ){
							if(umcn.getProductionTime().compareTo(nowDay) <= 0) {
								umcn.setType("A");
							}else if(umcn.getProductionTime().compareTo(nowDayAdd2) <= 0) {
								umcn.setType("B");
							}else if(umcn.getProductionTime().compareTo(nowDayAdd4) <= 0) {
								umcn.setType("C");
							}
						}
	                    umcn.setUID(temp[3].replaceAll("'", "").substring(0,21));
	                    umcn.setPlant(temp[4]);
	                    
	                    //计算IQC检验等待时间
	                    String IQCGetTime = umcn.getIQCGetTime();              
	                    if (!"".equals(IQCGetTime)&&!"null".equalsIgnoreCase(IQCGetTime)) {
	                        //System.out.println("IQCGETTIME---"+IQCGetTime);
	                        if (IQCGetTime.substring(11).compareTo("21:00")>0) {
	                            IQCGetTime = IQCGetTime.substring(0, 10) + " 21:00";
	                        }
	                        if (IQCGetTime.substring(11).compareTo("08:00")<0) {
	                            IQCGetTime = IQCGetTime.substring(0, 10) + " 08:00";
	                        }
	                    
	                        if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                        }
	                        if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                        }
	                        
	                        if(IQCGetTime.compareTo(nowDayTime) > 0) {
	                            umcn.setIQCCheckWaitTime("");
	                        }else {
	                            int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(IQCGetTime.substring(0, 10)) );
	                            int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(IQCGetTime.substring(11, 13));
	                            int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(IQCGetTime.substring(14, 16));
	                            if(min < 0) {
	                                min = 60 + min;
	                                hour = hour - 1;
	                            }
	                            umcn.setIQCCheckWaitTime(hour+"小时"+min+"分钟");
	                        } 
	                    }else{
	                        umcn.setIQCCheckWaitTime("");
	                    }
	                    //umCheckNotOCRService.saveUrgentMaterialCheckNotOCR(umcn);
	            	}
	            }
	        }
	        //pcbvendorrid
	        rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent, GRNDATE from pcbvendorrid " +
	        		" where GRNDATE between '"+nowDay_30+"' and '"+nowDay+"' and UpAegis='pass'  and plent in ('1100','1200','5000')  and ( " +
	                " partNumber not like '009%' and partNumber not like '020%' and partNumber not like  '021%' and partNumber not like '023%' and " +
	                " partNumber not like '035%' and partNumber not like '040%' and partNumber not like  '050%' and partNumber not like '051%' and " +
	                " partNumber not like '059%' and partNumber not like '110%' and partNumber not like  '121%' and partNumber not like '123%' and " +
	                " partNumber not like '129%' and partNumber not like '140%' and partNumber not like  '147%' and partNumber not like '155%' and " +
	                " partNumber not like '233%' and partNumber not like '237%' and partNumber not like  '262%' and partNumber not like '265%' and " +
	                " partNumber not like '275%' and partNumber not like '470%' " +
	                " )");
	        rsMap=new HashMap<String,String[]>();
	        while (rs.next()) {
	            String[] dou=new String[6];
	            String pn = rs.getString("partNumber");
	            String grnDate = rs.getString("GRNDATE").substring(0, 10);
	            if(rsMap.containsKey(pn+grnDate)){
	                dou=rsMap.get(pn+grnDate);
	                dou[0]=rs.getString("partNumber");
	                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
	                dou[2]=(Integer.parseInt(dou[2])+1) + "";
	                if(dou[3].length()< 4000) {
	                	dou[3]="'"+rs.getString("rid")+"',"+dou[3]+"";
	                }
	                dou[4]=rs.getString("plent");
	                dou[5]=rs.getString("grn");
	                rsMap.put(pn+grnDate, dou);
	            }else{
	                dou[0]=rs.getString("partNumber");
	                dou[1]=rs.getString("printQTY");
	                dou[2]="1";
	                dou[3]="'"+rs.getString("rid")+"'";
	                dou[4]=rs.getString("plent");
	                dou[5]=rs.getString("grn");
	                rsMap.put(pn+grnDate, dou);
	            }
	        }
	        System.out.println("pcbvendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            //System.out.println(temp[3]);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " left join StockLocations SL on SL.ID = IIH.StockLocationID" +
	                    " where (SL.Identifier like'%IQ%') and II.Identifier in ("+temp[3]+") " +
	            		" order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            if(rsA.next()){ //有IQ库位，但此UID，还有QM在后，表示已归还
	            	String uid = rsA.getString("Identifier");
	            	System.out.println("Identifier:"+uid);
	            	ResultSet rsA2 = aegisDB.executeQuery(" select top 1 II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
		            		" from ItemInventories II " +
		                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
		                    " left join StockLocations SL on SL.ID = IIH.StockLocationID" +
		                    " where II.Identifier = '"+uid+"' and SL.Identifier is not null  " +
		            		" order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            	if(rsA2.next() && rsA2.getString("historyStock").contains("QM")){
	            		//有IQ库位，但此UID，还有QM在后，表示已归还
	            	}else{
	            		UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
		                umcn.setGRN(temp[5]);
		                umcn.setItemNumber(temp[0]);
		                ResultSet rsC = aegisDB.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
		                        "where ReceivingNumber='"+temp[5]+"'");
		                if (rsC.next()) {
		                    umcn.setRDFinishTime(rsC.getString("CreateDate").substring(0,16));//收货时间
		                }else {
		                    umcn.setRDFinishTime("");
		                }
		                umcn.setIQCGetTime(rsA.getString("localtime").substring(0,16));//IQC取样时间
	                	//
	                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                    ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                            " where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                    if(rsB.next()) {
	                        umcn.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                    }else {
	                        //根据工厂 物料 取工单开始日期
	                        double totalInventory = 0.0;
	                        double leftInventory = 0.0;
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
	                        umcn.setProductionTime(soStartDate);
	                    }
	                    if(!"NA".equals(umcn.getProductionTime()) ){
    						if(umcn.getProductionTime().compareTo(nowDay) <= 0) {
    							umcn.setType("A");
    						}else if(umcn.getProductionTime().compareTo(nowDayAdd2) <= 0) {
    							umcn.setType("B");
    						}else if(umcn.getProductionTime().compareTo(nowDayAdd4) <= 0) {
    							umcn.setType("C");
    						}
    					}
	                    umcn.setUID("");  //temp[3].replaceAll("'", "").substring(0,21)
	                    umcn.setPlant(temp[4]);
	                    //计算IQC检验等待时间
	                    String IQCGetTime = umcn.getIQCGetTime();              
	                    if (!"".equals(IQCGetTime)&&!"null".equalsIgnoreCase(IQCGetTime)) {
	                        //System.out.println("IQCGETTIME---"+IQCGetTime);
	                        if (IQCGetTime.substring(11).compareTo("21:00")>0) {
	                            IQCGetTime = IQCGetTime.substring(0, 10) + " 21:00";
	                        }
	                        if (IQCGetTime.substring(11).compareTo("08:00")<0) {
	                            IQCGetTime = IQCGetTime.substring(0, 10) + " 08:00";
	                        }
	                    
	                        if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                        }
	                        if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                        }
	                        
	                        if(IQCGetTime.compareTo(nowDayTime) > 0) {
	                            umcn.setIQCCheckWaitTime("");
	                        }else {
	                            int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(IQCGetTime.substring(0, 10)) );
	                            int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(IQCGetTime.substring(11, 13));
	                            int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(IQCGetTime.substring(14, 16));
	                            if(min < 0) {
	                                min = 60 + min;
	                                hour = hour - 1;
	                            }
	                            umcn.setIQCCheckWaitTime(hour+"小时"+min+"分钟");
	                        } 
	                    }else{
	                        umcn.setIQCCheckWaitTime("");
	                    }
	                    //umCheckNotOCRService.saveUrgentMaterialCheckNotOCR(umcn);
	            	}
	            }else {
	            	rsA = aegisDB.executeQuery(" select top 1 II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            			" from ItemInventories II " +
	            			" left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	            			" left join StockLocations SL on SL.ID = IIH.StockLocationID" +
	            			" where (SL.Identifier like'%QM%') and II.Identifier = '"+temp[3].replaceAll("'", "").substring(0,21)+"' " +
		            		" order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            	if(rsA.next()) {
	            		UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
		                umcn.setGRN(temp[5]);
		                umcn.setItemNumber(temp[0]);
		                ResultSet rsC = aegisDB.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
		                        "where ReceivingNumber='"+temp[5]+"'");
		                if (rsC.next()) {
		                    umcn.setRDFinishTime(rsC.getString("CreateDate").substring(0,16));//收货时间
		                }else {
		                    umcn.setRDFinishTime("");
		                }
		                //
		            	umcn.setIQCGetTime("");
	                    //
	                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                    ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                            " where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                    if(rsB.next()) {
	                        umcn.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                    }else {
	                        //根据工厂 物料 取工单开始日期
	                        double totalInventory = 0.0;
	                        double leftInventory = 0.0;
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
	                        umcn.setProductionTime(soStartDate);
	                    }
	                    if(!"NA".equals(umcn.getProductionTime()) ){
							if(umcn.getProductionTime().compareTo(nowDay) <= 0) {
								umcn.setType("A");
							}else if(umcn.getProductionTime().compareTo(nowDayAdd2) <= 0) {
								umcn.setType("B");
							}else if(umcn.getProductionTime().compareTo(nowDayAdd4) <= 0) {
								umcn.setType("C");
							}
						}
	                    umcn.setUID(temp[3].replaceAll("'", "").substring(0,21));
	                    umcn.setPlant(temp[4]);
	                    
	                    //计算IQC检验等待时间
	                    String IQCGetTime = umcn.getIQCGetTime();              
	                    if (!"".equals(IQCGetTime)&&!"null".equalsIgnoreCase(IQCGetTime)) {
	                        //System.out.println("IQCGETTIME---"+IQCGetTime);
	                        if (IQCGetTime.substring(11).compareTo("21:00")>0) {
	                            IQCGetTime = IQCGetTime.substring(0, 10) + " 21:00";
	                        }
	                        if (IQCGetTime.substring(11).compareTo("08:00")<0) {
	                            IQCGetTime = IQCGetTime.substring(0, 10) + " 08:00";
	                        }
	                    
	                        if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                        }
	                        if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                        }
	                        
	                        if(IQCGetTime.compareTo(nowDayTime) > 0) {
	                            umcn.setIQCCheckWaitTime("");
	                        }else {
	                            int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(IQCGetTime.substring(0, 10)) );
	                            int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(IQCGetTime.substring(11, 13));
	                            int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(IQCGetTime.substring(14, 16));
	                            if(min < 0) {
	                                min = 60 + min;
	                                hour = hour - 1;
	                            }
	                            umcn.setIQCCheckWaitTime(hour+"小时"+min+"分钟");
	                        } 
	                    }else{
	                        umcn.setIQCCheckWaitTime("");
	                    }
	                    //umCheckNotOCRService.saveUrgentMaterialCheckNotOCR(umcn);
	            	}
	            }
	        }
	        //
			rs = grnewdbDB.executeQuery("select top 1 Sequence from UrgentMaterialCheckNotOCR where Sequence is not null order by Sequence desc");
			if(rs.next()) {
				grnewdbDB.executeUpdate("update UrgentMaterialCheckNotOCR set Sequence="+(rs.getInt("Sequence") + 1)+" where Sequence is null");
			}else{
				grnewdbDB.executeUpdate("update UrgentMaterialCheckNotOCR set Sequence=1 where Sequence is null");
			}
			
	        //part03
			rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from vendorrid " +
	                " where GRNDATE between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass'  and plent in ('1100','1200','5000') and ( " +
	                " partNumber like '009%' or partNumber like '020%' or partNumber like  '021%' or partNumber like '023%' or " +
	                " partNumber like '035%' or partNumber like '040%' or partNumber like  '050%' or partNumber like '051%' or " +
	                " partNumber like '059%' or partNumber like '110%' or partNumber like  '121%' or partNumber like '123%' or " +
	                " partNumber like '129%' or partNumber like '140%' or partNumber like  '147%' or partNumber like '155%' or " +
	                " partNumber like '233%' or partNumber like '237%' or partNumber like  '262%' or partNumber like '265%' or " +
	                " partNumber like '275%' or partNumber like '470%' " +
	                " )");
	        rsMap=new HashMap<String,String[]>();
	        while (rs.next()) {
	            String[] dou=new String[5];
	            String grn = rs.getString("grn");
	            String pn = rs.getString("partNumber");
	            if(rsMap.containsKey(grn+pn)){
	                dou=rsMap.get(grn+pn);
	                dou[0]=rs.getString("partNumber");
	                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
	                dou[2]=(Integer.parseInt(dou[2])+1) + "";
	                if(dou[3].length()< 4000) {
	                    dou[3]="'"+rs.getString("rid")+"',"+dou[3]+"";
	                }             
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }else{
	                dou[0]=rs.getString("partNumber");
	                dou[1]=rs.getString("printQTY");
	                dou[2]="1";
	                dou[3]="'"+rs.getString("rid")+"'";
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }
	        }
	        System.out.println("AutoUrgentMaterialCheckOCR");
	        System.out.println("vendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " where FRB.Name like '%QM%' and II.Identifier in ("+temp[3]+") " +
	                    " order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            if(rsA.next()){
	                String auditDataTime = rsA.getString("localtime").substring(0,16);
	            	ResultSet ocrRs = ocrDB.executeQuery("select UID from LabelMsg where UID in ("+temp[3]+")");
	            	if(!ocrRs.next()){
	            		UrgentMaterialCheckOCR umc = new UrgentMaterialCheckOCR();
	                    umc.setGRN(key.substring(0,10));
	                    umc.setItemNumber(temp[0]);
	                    umc.setGRNQuantity(temp[1]);
	                    umc.setUIDQuantity(temp[2]);
	                    umc.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                    ResultSet rsD = aegisDB.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
	                            "where ReceivingNumber='"+key.substring(0,10)+"'");
	                    if (rsD.next()) {
	                        umc.setRDFinishTime(rsD.getString("CreateDate").substring(0,16));//收货时间
	                    }else {
	                        umc.setRDFinishTime("");
	                    }
	                    ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                            " where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                    if(rsB.next()) {
	                        umc.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                    }else {
	                        //根据工厂 物料 取工单开始日期
	                        double totalInventory = 0.0;
	                        double leftInventory = 0.0;
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
	                        umc.setProductionTime(soStartDate);
	                    }
	                    if(!"NA".equals(umc.getProductionTime()) ){
							if(umc.getProductionTime().compareTo(nowDay) <= 0) {
								umc.setType("A");
							}else if(umc.getProductionTime().compareTo(nowDayAdd2) <= 0) {
								umc.setType("B");
							}else if(umc.getProductionTime().compareTo(nowDayAdd4) <= 0) {
								umc.setType("C");
							}
						}
	                    umc.setUID(temp[3].replaceAll("'", "").substring(0,21));
	                    umc.setPlant(temp[4]);
	                    umc.setAuditDataTime(auditDataTime);
	                    //计算OCR 检验等待时间
	                    if (!"".equals(auditDataTime)&&!"null".equalsIgnoreCase(auditDataTime)) {
	                        //System.out.println("IQCGETTIME---"+IQCGetTime);
	                        if (auditDataTime.substring(11).compareTo("21:00")>0) {
	                            auditDataTime = auditDataTime.substring(0, 10) + " 21:00";
	                        }
	                        if (auditDataTime.substring(11).compareTo("08:00")<0) {
	                            auditDataTime = auditDataTime.substring(0, 10) + " 08:00";
	                        }
	                    
	                        if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                        }
	                        if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                            nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                        }
	                        
	                        if(auditDataTime.compareTo(nowDayTime) > 0) {
	                            umc.setOCRCheckWaitTime("");
	                        }else {
	                            int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(auditDataTime.substring(0, 10)) );
	                            int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(auditDataTime.substring(11, 13));
	                            int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
	                            if(min < 0) {
	                                min = 60 + min;
	                                hour = hour - 1;
	                            }
	                            umc.setOCRCheckWaitTime(hour+"小时"+min+"分钟");
	                        }
	                    }else {
	                        umc.setOCRCheckWaitTime("");
	                    }               
	                    //umCheckOCRService.saveUrgentMaterialCheckOCR(umc);
	                } 
	            }
	                
	            
	        }
	        //pcbvendorrid
	        rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from pcbvendorrid " +
	        		" where GRNDATE between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass'  and plent in ('1100','1200','5000') and ( " +
	                " partNumber like '009%' or partNumber like '020%' or partNumber like  '021%' or partNumber like '023%' or " +
	                " partNumber like '035%' or partNumber like '040%' or partNumber like  '050%' or partNumber like '051%' or " +
	                " partNumber like '059%' or partNumber like '110%' or partNumber like  '121%' or partNumber like '123%' or " +
	                " partNumber like '129%' or partNumber like '140%' or partNumber like  '147%' or partNumber like '155%' or " +
	                " partNumber like '233%' or partNumber like '237%' or partNumber like  '262%' or partNumber like '265%' or " +
	                " partNumber like '275%' or partNumber like '470%' " +
	                " )");
	        rsMap=new HashMap<String,String[]>();
	        while (rs.next()) {
	            String[] dou=new String[5];
	            String grn = rs.getString("grn");
	            String pn = rs.getString("partNumber");
	            if(rsMap.containsKey(grn+pn)){
	                dou=rsMap.get(grn+pn);
	                dou[0]=rs.getString("partNumber");
	                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
	                dou[2]=(Integer.parseInt(dou[2])+1) + "";
	                if(dou[3].length()< 4000) {
	                    dou[3]="'"+rs.getString("rid")+"',"+dou[3]+"";
	                }
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }else{
	                dou[0]=rs.getString("partNumber");
	                dou[1]=rs.getString("printQTY");
	                dou[2]="1";
	                dou[3]="'"+rs.getString("rid")+"'";
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }
	        }
	        System.out.println("pcbvendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " where FRB.Name like '%QM%' and II.Identifier in ("+temp[3]+") " +
	                    " order by IIH.TimePosted_BaseDateTimeUTC desc");
	            if(rsA.next()){
	                String auditDataTime = rsA.getString("localtime").substring(0,16);
	            	ResultSet ocrRs = ocrDB.executeQuery("select UID from LabelMsg where UID in ("+temp[3]+")");
	            	if(!ocrRs.next()){
	            		  UrgentMaterialCheckOCR umc = new UrgentMaterialCheckOCR();
	                      umc.setGRN(key.substring(0,10));
	                      umc.setItemNumber(temp[0]);
	                      umc.setGRNQuantity(temp[1]);
	                      umc.setUIDQuantity(temp[2]);
	                      umc.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                      ResultSet rsD = aegisDB.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
	                              "where ReceivingNumber='"+key.substring(0,10)+"'");
	                      if (rsD.next()) {
	                          umc.setRDFinishTime(rsD.getString("CreateDate").substring(0,16));//收货时间
	                      }else {
	                          umc.setRDFinishTime("");
	                      }
	                      ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                              " where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                      if(rsB.next()) {
	                          umc.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                      }else {
	                          //根据工厂 物料 取工单开始日期
	                          double totalInventory = 0.0;
	                          double leftInventory = 0.0;
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
	                          umc.setProductionTime(soStartDate);
	                      }
	                      if(!"NA".equals(umc.getProductionTime()) ){
	  						if(umc.getProductionTime().compareTo(nowDay) <= 0) {
	  							umc.setType("A");
	  						}else if(umc.getProductionTime().compareTo(nowDayAdd2) <= 0) {
	  							umc.setType("B");
	  						}else if(umc.getProductionTime().compareTo(nowDayAdd4) <= 0) {
	  							umc.setType("C");
	  						}
	  					}
	                      umc.setUID(temp[3].replaceAll("'", "").substring(0,21));
	                      umc.setPlant(temp[4]);
	                      umc.setAuditDataTime(auditDataTime);
	                      //计算OCR 检验等待时间
	                      if (!"".equals(auditDataTime)&&!"null".equalsIgnoreCase(auditDataTime)) {
	                          //System.out.println("IQCGETTIME---"+IQCGetTime);
	                          if (auditDataTime.substring(11).compareTo("21:00")>0) {
	                              auditDataTime = auditDataTime.substring(0, 10) + " 21:00";
	                          }
	                          if (auditDataTime.substring(11).compareTo("08:00")<0) {
	                              auditDataTime = auditDataTime.substring(0, 10) + " 08:00";
	                          }
	                      
	                          if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                              nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                          }
	                          if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                              nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                          }
	                          
	                          if(auditDataTime.compareTo(nowDayTime) > 0) {
	                              umc.setOCRCheckWaitTime("");
	                          }else {
	                              int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(auditDataTime.substring(0, 10)) );
	                              int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(auditDataTime.substring(11, 13));
	                              int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
	                              if(min < 0) {
	                                  min = 60 + min;
	                                  hour = hour - 1;
	                              }
	                              umc.setOCRCheckWaitTime(hour+"小时"+min+"分钟");
	                          }
	                      }else {
	                          umc.setOCRCheckWaitTime("");
	                      } 
	                      //umCheckOCRService.saveUrgentMaterialCheckOCR(umc);
	                  }
	            }
	        }
	        //
			rs = grnewdbDB.executeQuery("select top 1 Sequence from UrgentMaterialCheckOCR where Sequence is not null order by Sequence desc");
			if(rs.next()) {
				grnewdbDB.executeUpdate("update UrgentMaterialCheckOCR set Sequence="+(rs.getInt("Sequence") + 1)+" where Sequence is null");
			}else{
				grnewdbDB.executeUpdate("update UrgentMaterialCheckOCR set Sequence=1 where Sequence is null");
			}
			
			//part04
			rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from vendorrid " +
	                " where GRNDATE between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000')  ");
	        rsMap=new HashMap<String,String[]>();
	        while (rs.next()) {
	            String[] dou=new String[5];
	            String grn = rs.getString("grn");
	            String pn = rs.getString("partNumber");
	            if(rsMap.containsKey(grn+pn)){
	                dou=rsMap.get(grn+pn);
	                dou[0]=rs.getString("partNumber");
	                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
	                dou[2]=(Integer.parseInt(dou[2])+1) + "";
	                if(dou[3].length()< 4000) {
	                    dou[3]="'"+rs.getString("rid")+"',"+dou[3]+"";
	                }
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }else{
	                dou[0]=rs.getString("partNumber");
	                dou[1]=rs.getString("printQTY");
	                dou[2]="1";
	                dou[3]="'"+rs.getString("rid")+"'";
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }
	        }
	        System.out.println("AutoToReceiveWarehouse");
	        System.out.println("vendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " left join ProductRouteTransactions PRT on PRT.ItemInventoryID = II.ID " +
	                    " left join ProductInspectionHistories PIH on PIH.ProductRouteTransactionID = PRT.ID" +
	                    " left join ProductMeasurementDatas PMD on PMD.ProductInspectionHistoryID = PIH.ID" +
	                    " where FRB.Name like '%QM%' and PMD.MeasurementType ='321' and II.Identifier in ("+temp[3]+") " +
	                    " order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            //IQC归还时间, TimePosted_BaseDateTimeUTC
	            if(rsA.next()){
	                ToReceiveWarehouse trw = new ToReceiveWarehouse();
	                trw.setGRN(key.substring(0, 10));
	                trw.setItemNumber(temp[0]);
	                trw.setGRNQuantity(temp[1]);               
	                trw.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                        " where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                if(rsB.next()) {
	                    trw.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                }else {
	                    //根据工厂 物料 取工单开始日期
	                    double totalInventory = 0.0;
	                    double leftInventory = 0.0;
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
	                    trw.setProductionTime(soStartDate);
	                }
	                if(!"NA".equals(trw.getProductionTime()) ){
						if(trw.getProductionTime().compareTo(nowDay) <= 0) {
							trw.setType("A");
						}else if(trw.getProductionTime().compareTo(nowDayAdd2) <= 0) {
							trw.setType("B");
						}else if(trw.getProductionTime().compareTo(nowDayAdd4) <= 0) {
							trw.setType("C");
						}
					}
	                trw.setUID(temp[3].replaceAll("'", "").substring(0,21));
	                trw.setPlant(temp[4]);
	                //
	                trw.setAegisQualify("是");
	                trw.setSAPQualify("是");
	                
	                //计算待入主料仓时间
	                String IQCReturnTime = rsA.getString("localtime"); //IQC归还时间                                
	                if(!"".equals(IQCReturnTime)&&!"null".equalsIgnoreCase(IQCReturnTime)) { 
	                    //System.out.println("IQCGETTIME---"+IQCGetTime);
	                    if (IQCReturnTime.substring(11).compareTo("21:00")>0) {
	                        IQCReturnTime = IQCReturnTime.substring(0, 10) + " 21:00";
	                    }
	                    if (IQCReturnTime.substring(11).compareTo("08:00")<0) {
	                        IQCReturnTime = IQCReturnTime.substring(0, 10) + " 08:00";
	                    }
	                
	                    if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                        nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                    }
	                    if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                        nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                    }
	                    
	                    if(IQCReturnTime.compareTo(nowDayTime) > 0) {
	                        trw.setWaitTimeToMainbin("");
	                    }else {
	                        int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(IQCReturnTime.substring(0, 10)) );
	                        int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(IQCReturnTime.substring(11, 13));
	                        int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(IQCReturnTime.substring(14, 16));
	                        if(min < 0) {
	                            min = 60 + min;
	                            hour = hour - 1;
	                        }
	                        trw.setWaitTimeToMainbin(hour+"小时"+min+"分钟");
	                    }
	                }else{
	                    trw.setWaitTimeToMainbin("");
	                }
	                //trWarehouseService.saveToReceiveWarehouse(trw);
	            }
	        }
	        //pcbvendorrid
	        rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from pcbvendorrid " +
	                " where GRNDATE between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000') ");
	        rsMap=new HashMap<String,String[]>();
	        while (rs.next()) {
	            String[] dou=new String[5];
	            String grn = rs.getString("grn");
	            String pn = rs.getString("partNumber");
	            if(rsMap.containsKey(grn+pn)){
	                dou=rsMap.get(grn+pn);
	                dou[0]=rs.getString("partNumber");
	                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
	                dou[2]=(Integer.parseInt(dou[2])+1) + "";
	                if(dou[3].length()< 4000) {
	                    dou[3]="'"+rs.getString("rid")+"',"+dou[3]+"";
	                }
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }else{
	                dou[0]=rs.getString("partNumber");
	                dou[1]=rs.getString("printQTY");
	                dou[2]="1";
	                dou[3]="'"+rs.getString("rid")+"'";
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }
	        }
	        System.out.println("pcbvendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " left join ProductRouteTransactions PRT on PRT.ItemInventoryID = II.ID " +
	                    " left join ProductInspectionHistories PIH on PIH.ProductRouteTransactionID = PRT.ID" +
	                    " left join ProductMeasurementDatas PMD on PMD.ProductInspectionHistoryID = PIH.ID" +
	                    " where FRB.Name like '%QM%' and PMD.MeasurementType ='321' and II.Identifier in ("+temp[3]+") " +
	                    " order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            if(rsA.next()){
	                ToReceiveWarehouse trw = new ToReceiveWarehouse();
	                trw.setGRN(key.substring(0, 10));
	                trw.setItemNumber(temp[0]);
	                trw.setGRNQuantity(temp[1]);
	                trw.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                ResultSet rsB = aegisDB.executeQuery("select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                        "where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                if(rsB.next()) {
	                    trw.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                }else {
	                    //根据工厂 物料 取工单开始日期
	                    double totalInventory = 0.0;
	                    double leftInventory = 0.0;
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
	                    trw.setProductionTime(soStartDate);
	                }
	                if(!"NA".equals(trw.getProductionTime()) ){
						if(trw.getProductionTime().compareTo(nowDay) <= 0) {
							trw.setType("A");
						}else if(trw.getProductionTime().compareTo(nowDayAdd2) <= 0) {
							trw.setType("B");
						}else if(trw.getProductionTime().compareTo(nowDayAdd4) <= 0) {
							trw.setType("C");
						}
					}
	                trw.setUID(temp[3].replaceAll("'", "").substring(0,21));
	                trw.setPlant(temp[4]);
	                //
	                trw.setAegisQualify("是");
	                trw.setSAPQualify("是");
	                //计算待入主料仓时间
	                String IQCReturnTime = rsA.getString("localtime"); //IQC归还时间                                      
	                if(!"".equals(IQCReturnTime)&&!"null".equalsIgnoreCase(IQCReturnTime)) { 
	                    //System.out.println("IQCGETTIME---"+IQCGetTime);
	                    if (IQCReturnTime.substring(11).compareTo("21:00")>0) {
	                        IQCReturnTime = IQCReturnTime.substring(0, 10) + " 21:00";
	                    }
	                    if (IQCReturnTime.substring(11).compareTo("08:00")<0) {
	                        IQCReturnTime = IQCReturnTime.substring(0, 10) + " 08:00";
	                    }
	                
	                    if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                        nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                    }
	                    if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                        nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                    }
	                    
	                    if(IQCReturnTime.compareTo(nowDayTime) > 0) {
	                        trw.setWaitTimeToMainbin("");
	                    }else {
	                        int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(IQCReturnTime.substring(0, 10)) );
	                        int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(IQCReturnTime.substring(11, 13));
	                        int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(IQCReturnTime.substring(14, 16));
	                        if(min < 0) {
	                            min = 60 + min;
	                            hour = hour - 1;
	                        }
	                        trw.setWaitTimeToMainbin(hour+"小时"+min+"分钟");
	                    }
	                }else{
	                    trw.setWaitTimeToMainbin("");
	                }                
	                //trWarehouseService.saveToReceiveWarehouse(trw);   
	            }
	        }
	        //
			rs = grnewdbDB.executeQuery("select top 1 Sequence from ToReceiveWarehouse where Sequence is not null order by Sequence desc");
			if(rs.next()) {
				grnewdbDB.executeUpdate("update ToReceiveWarehouse set Sequence="+(rs.getInt("Sequence") + 1)+" where Sequence is null");
			}else{
				grnewdbDB.executeUpdate("update ToReceiveWarehouse set Sequence=1 where Sequence is null");
			}
			
			//part05
			rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from vendorrid " +
	                " where GRNDATE between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000')  ");
	        rsMap=new HashMap<String,String[]>();
	        while (rs.next()) {
	            String[] dou=new String[5];
	            String grn = rs.getString("grn");
	            String pn = rs.getString("partNumber");
	            if(rsMap.containsKey(grn+pn)){
	                dou=rsMap.get(grn+pn);
	                dou[0]=rs.getString("partNumber");
	                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
	                dou[2]=(Integer.parseInt(dou[2])+1) + "";
	                if(dou[3].length()< 4000) {
	                    dou[3]="'"+rs.getString("rid")+"',"+dou[3]+"";
	                }
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }else{
	                dou[0]=rs.getString("partNumber");
	                dou[1]=rs.getString("printQTY");
	                dou[2]="1";
	                dou[3]="'"+rs.getString("rid")+"'";
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }
	        }
	        System.out.println("AutoToReceiveWarehouseB");
	        System.out.println("vendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " left join ProductRouteTransactions PRT on PRT.ItemInventoryID = II.ID " +
	                    " left join ProductInspectionHistories PIH on PIH.ProductRouteTransactionID = PRT.ID" +
	                    " left join ProductMeasurementDatas PMD on PMD.ProductInspectionHistoryID = PIH.ID" +
	                    " where FRB.Name like '%QM%' and PMD.MeasurementType ='122' and II.Identifier in ("+temp[3]+") " +
	                    " order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            if(rsA.next()){
	                ToReceiveWarehouseB trwb = new ToReceiveWarehouseB();
	                trwb.setGRN(key.substring(0,10));
	                trwb.setItemNumber(temp[0]);
	                trwb.setGRNQuantity(temp[1]);
	                trwb.setReceivingLocation(rsA.getString("StockLocation"));//收货库位         
	                ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                        " where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                if(rsB.next()) {
	                    trwb.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                }else {
	                    //根据工厂 物料 取工单开始日期
	                    double totalInventory = 0.0;
	                    double leftInventory = 0.0;
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
	                    trwb.setProductionTime(soStartDate);
	                }
	                if(!"NA".equals(trwb.getProductionTime()) ){
						if(trwb.getProductionTime().compareTo(nowDay) <= 0) {
							trwb.setType("A");
						}else if(trwb.getProductionTime().compareTo(nowDayAdd2) <= 0) {
							trwb.setType("B");
						}else if(trwb.getProductionTime().compareTo(nowDayAdd4) <= 0) {
							trwb.setType("C");
						}
					}
	                trwb.setUID(temp[3].replaceAll("'", "").substring(0,21));
	                trwb.setPlant(temp[4]);
	                //
	                trwb.setAegisQualify("否");
	                trwb.setSAPQualify("否");
	                //计算待入退貨仓或 開MRB时间
	                String IQCReturnTime = rsA.getString("localtime"); //IQC归还时间                                    
	                if(!"".equals(IQCReturnTime)&&!"null".equalsIgnoreCase(IQCReturnTime)) { 
	                    //System.out.println("IQCGETTIME---"+IQCGetTime);
	                    if (IQCReturnTime.substring(11).compareTo("21:00")>0) {
	                        IQCReturnTime = IQCReturnTime.substring(0, 10) + " 21:00";
	                    }
	                    if (IQCReturnTime.substring(11).compareTo("08:00")<0) {
	                        IQCReturnTime = IQCReturnTime.substring(0, 10) + " 08:00";
	                    }
	                
	                    if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                        nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                    }
	                    if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                        nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                    }
	                    
	                    if(IQCReturnTime.compareTo(nowDayTime) > 0) {
	                        trwb.setReturnWarehouseTime("");
	                    }else {
	                        int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(IQCReturnTime.substring(0, 10)) );
	                        int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(IQCReturnTime.substring(11, 13));
	                        int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(IQCReturnTime.substring(14, 16));
	                        if(min < 0) {
	                            min = 60 + min;
	                            hour = hour - 1;
	                        }
	                        trwb.setReturnWarehouseTime(hour+"小时"+min+"分钟");
	                    }
	                }else{
	                    trwb.setReturnWarehouseTime("");
	                }
	                //trWarehouseBService.saveToReceiveWarehouseB(trwb);
	            } 
	            
	        }
	        //pcbvendorrid
	        rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from pcbvendorrid " +
	                " where GRNDATE between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000')  ");
	        rsMap=new HashMap<String,String[]>();
	        while (rs.next()) {
	            String[] dou=new String[5];
	            String grn = rs.getString("grn");
	            String pn = rs.getString("partNumber");
	            if(rsMap.containsKey(grn+pn)){
	                dou=rsMap.get(grn+pn);
	                dou[0]=rs.getString("partNumber");
	                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
	                dou[2]=(Integer.parseInt(dou[2])+1) + "";
	                if(dou[3].length()< 4000) {
	                    dou[3]="'"+rs.getString("rid")+"',"+dou[3]+"";
	                }
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }else{
	                dou[0]=rs.getString("partNumber");
	                dou[1]=rs.getString("printQTY");
	                dou[2]="1";
	                dou[3]="'"+rs.getString("rid")+"'";
	                dou[4]=rs.getString("plent");
	                rsMap.put(grn+pn, dou);
	            }
	        }
	        System.out.println("pcbvendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " left join ProductRouteTransactions PRT on PRT.ItemInventoryID = II.ID " +
	                    " left join ProductInspectionHistories PIH on PIH.ProductRouteTransactionID = PRT.ID" +
	                    " left join ProductMeasurementDatas PMD on PMD.ProductInspectionHistoryID = PIH.ID" +
	                    " where FRB.Name like '%QM%' and PMD.MeasurementType ='122' and II.Identifier in ("+temp[3]+") " +
	                    " order by IIH.TimePosted_BaseDateTimeUTC desc ");
	            if(rsA.next()){
	                ToReceiveWarehouseB trwb = new ToReceiveWarehouseB();
	                trwb.setGRN(key.substring(0,10));
	                trwb.setItemNumber(temp[0]);
	                trwb.setGRNQuantity(temp[1]);
	                trwb.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                ResultSet rsB = aegisDB.executeQuery("select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	                        "where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
	                if(rsB.next()) {
	                    trwb.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                }else {
	                    //根据工厂 物料 取工单开始日期
	                    double totalInventory = 0.0;
	                    double leftInventory = 0.0;
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
	                    trwb.setProductionTime(soStartDate);
	                }
	                if(!"NA".equals(trwb.getProductionTime()) ){
						if(trwb.getProductionTime().compareTo(nowDay) <= 0) {
							trwb.setType("A");
						}else if(trwb.getProductionTime().compareTo(nowDayAdd2) <= 0) {
							trwb.setType("B");
						}else if(trwb.getProductionTime().compareTo(nowDayAdd4) <= 0) {
							trwb.setType("C");
						}
					}
	                trwb.setUID(temp[3].replaceAll("'", "").substring(0,21));
	                trwb.setPlant(temp[4]);
	                //
	                trwb.setAegisQualify("否");
	                trwb.setSAPQualify("否");
	                //计算待入退貨仓或 開MRB时间
	                String IQCReturnTime = rsA.getString("localtime"); //IQC归还时间                                    
	                if(!"".equals(IQCReturnTime)&&!"null".equalsIgnoreCase(IQCReturnTime)) { 
	                    //System.out.println("IQCGETTIME---"+IQCGetTime);
	                    if (IQCReturnTime.substring(11).compareTo("21:00")>0) {
	                        IQCReturnTime = IQCReturnTime.substring(0, 10) + " 21:00";
	                    }
	                    if (IQCReturnTime.substring(11).compareTo("08:00")<0) {
	                        IQCReturnTime = IQCReturnTime.substring(0, 10) + " 08:00";
	                    }
	                
	                    if(nowDayTime.substring(11).compareTo("21:00") > 0) {
	                        nowDayTime = nowDayTime.substring(0, 10) + " 21:00";
	                    }
	                    if(nowDayTime.substring(11).compareTo("08:00") < 0) {
	                        nowDayTime = nowDayTime.substring(0, 10) + " 08:00";
	                    }
	                    
	                    if(IQCReturnTime.compareTo(nowDayTime) > 0) {
	                        trwb.setReturnWarehouseTime("");
	                    }else {
	                        int day = DateUtils.diffDays(df.parse(nowDayTime.substring(0, 10)), df.parse(IQCReturnTime.substring(0, 10)) );
	                        int hour = day*13 + Integer.parseInt(nowDayTime.substring(11, 13)) - Integer.parseInt(IQCReturnTime.substring(11, 13));
	                        int min = Integer.parseInt(nowDayTime.substring(14, 16)) - Integer.parseInt(IQCReturnTime.substring(14, 16));
	                        if(min < 0) {
	                            min = 60 + min;
	                            hour = hour - 1;
	                        }
	                        trwb.setReturnWarehouseTime(hour+"小时"+min+"分钟");
	                    }
	                }else{
	                    trwb.setReturnWarehouseTime("");
	                }
	                //trWarehouseBService.saveToReceiveWarehouseB(trwb);
	            }
	        }
	        //
			rs = grnewdbDB.executeQuery("select top 1 Sequence from ToReceiveWarehouseB where Sequence is not null order by Sequence desc");
			if(rs.next()) {
				grnewdbDB.executeUpdate("update ToReceiveWarehouseB set Sequence="+(rs.getInt("Sequence") + 1)+" where Sequence is null");
			}else{
				grnewdbDB.executeUpdate("update ToReceiveWarehouseB set Sequence=1 where Sequence is null");
			}
			//
			vpsDB.close();
			aegisDB.close();
			grnewdbDB.close();
			ocrDB.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("end...AutoAllDashBoard "+new Date());
	}
}