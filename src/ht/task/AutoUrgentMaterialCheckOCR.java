package ht.task;

import ht.biz.IUrgentMaterialCheckOCRService;
import ht.entity.UrgentMaterialCheckOCR;
import ht.util.*;
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
 * 2b
 * @date 2020-9-3
 * @author 刘惠明
 * 
 *
 */

public class AutoUrgentMaterialCheckOCR {
	@Autowired
	private IUrgentMaterialCheckOCRService umCheckOCRService;

	private static Log commonsLog = LogFactory.getLog(AutoUrgentMaterialCheckOCR.class);
	
	public void execute() throws Exception {
		commonsLog.info("start...");
		try {
	    	ConVPS vpsDB = new ConVPS();
	    	Connection connVPS = vpsDB.con;
			ConMes conMes = new ConMes();
			Connection connMes = conMes.con;
			ConOrbitX conOrbitX = new ConOrbitX();
			Connection connOrbitX = conOrbitX.con;
	    	ConDashBoard grnewdbDB = new ConDashBoard();
	    	Connection connDB = grnewdbDB.con;
	        SAPService sap = new SAPService();
	        //
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        String nowDay = df.format(c.getTime());
	        String year = nowDay.substring(0, 4);
	        String nowDayTime = df2.format(c.getTime());
	        c.add(Calendar.DATE, -2); // -2 天
	        String nowDay_2 = df.format(c.getTime());
	        // 21 -> 24 测试暂时修改
			if( (nowDayTime.substring(11).compareTo("08:00") >0 && nowDayTime.substring(11).compareTo("12:00") <=0 ) 
					|| (nowDayTime.substring(11).compareTo("13:00") >0 && nowDayTime.substring(11).compareTo("17:00") <=0 )
					|| (nowDayTime.substring(11).compareTo("18:00") >0 && nowDayTime.substring(11).compareTo("24:00") <=0 )  ){
				c.setTime(new Date());
				c.add(Calendar.DATE, +1);
				String nowDayAdd2 = df.format(c.getTime());
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
				List<UrgentMaterialCheckOCR> list = new ArrayList<UrgentMaterialCheckOCR>();
				int seq = 1;
				ResultSet rs = grnewdbDB.executeQuery("select max(Sequence) as maxseq from UrgentMaterialCheckOCR where Sequence is not null");
				if(rs.next()) {
					seq = rs.getInt("maxseq")+1;
				}
				/*PreparedStatement pstmtA1 = connMes.prepareStatement("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
		                            "where ReceivingNumber=? ");*/
				/*PreparedStatement pstmtA2 = connMes.prepareStatement("select RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
		                            " where PartNumber = ? and convert(varchar(10),RequireTime,23) =? order by RequireTime ");*/
				PreparedStatement pstmtA2 = connMes.prepareStatement("select " +
						"a.CreateTime as RequireTime " +
						"from " +
						"TO_TransportOrder a , TO_TransportOrderItem b " +
						"where " +
						"a.ID = b.TransportOrderID " +
						"and " +
						"b.Status = 'MissingPart' " +
						"and " +
						"b.PartNumber = ? " +
						"and " +
						"a.LLDHDate= ? " +
						"order by " +
						"CreateTime");
				/*PreparedStatement pstmtA3 = connMes.prepareStatement("select II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemInventoryHistorifes IIH on IIH.ItemInventoryID = II.ID " +
	                    " where (StockLocation like '%QM%' or StockLocation like '%RH%') and II.Identifier =? " +
	                    " order by IIH.TimePosted_BaseDateTimeUTC desc ");*/
				PreparedStatement pstmtA3 = connMes.prepareStatement("select " +
						"II.ToStock_Input as StockLocation, IIH.TimePosted_BaseDateTimeUTC AS 'localtime' " +
						"from " +
						"[dbo].[UID_xTend_MaterialTransactionsRHDCDW] II " +
						"left join " +
						"ItemInventoryHistories IIH " +
						"on " +
						"IIH.UID COLLATE Chinese_PRC_CI_AS = II.UID " +
						"where " +
						"II.UID = ? " +
						"and " +
						"(II.ToStock_Input like '%QM%' or II.ToStock_Input like '%RH%')" +
						"order by " +
						"IIH.TimePosted_BaseDateTimeUTC " +
						"desc");
				// 196 Orbit X
				// IQC 321 122 时间
				PreparedStatement findIqcDateByGrn = connOrbitX.prepareStatement("select distinct " +
						"IQCdate " +
						"from " +
						"[OrBitX].[dbo].[xTend_MaterialReceived]" +
						"where " +
						"GRN = ? " +
						"and " +
						"IQCMVT = '321' " +
						"union all " +
						"select distinct " +
						"IQCdate " +
						"from " +
						"[OrBitX].[dbo].[xTend_MaterialReceived] " +
						"where " +
						"GRN = ? " +
						"and " +
						"IQCMVT = '122'");
				PreparedStatement pstmtDB1 = connDB.prepareStatement(" select inventory,needQty,gotQty,soStartDate " +
						" from NotFinishSO where plant=? and bom=? order by soStartDate ");
		        PreparedStatement pstmtDB2 = connDB.prepareStatement("select * from UrgentMaterialCheckOCR where closeDate is not null " +
						" and GRN=? ");
				//vendorrid
				//2020-12-11 by XiaoYing HAO
		        //009 021 023 035 050 051 096 110 121 123 129 140 147  155 233 237 256 262 265 275 470
				rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent, UpAegisDATE from vendorrid " +
		                " where convert(varchar(10),GRNDATE,23) between '"+nowDay_2+"' and '"+nowDay+"' and printQTY<>0.0  and plent in ('1100','1200','5000') and ( " +
		                " partNumber like '009%' or partNumber like '021%' or partNumber like '023%' or " +
		                " partNumber like '035%' or partNumber like '050%' or partNumber like '051%' or " +
		                " partNumber like '096%' or partNumber like '110%' or partNumber like '121%' or partNumber like '123%' or " +
		                " partNumber like '129%' or partNumber like '140%' or partNumber like '147%' or partNumber like '155%' or " +
		                " partNumber like '233%' or partNumber like '237%' or partNumber like '256%' or partNumber like '262%' or partNumber like '265%' or " +
		                " partNumber like '275%' or partNumber like '470%' " +
		                " ) ");
				Map<String,String[]> rsMap=new HashMap<String,String[]>();
				// 测试
				int i = 0;
				// 循环开始时间
				long startTime = System.currentTimeMillis();
				//1.vps查询所有对应pn
		        while (rs.next()) {
		        	String grn = rs.getString("grn");
		        	pstmtDB2.setString(1, grn);
		        	//2.根据grn 查询 UrgentMaterialCheckOCR closeDate是否为空 
					ResultSet rsFinish  = pstmtDB2.executeQuery();
					if(rsFinish.next()) {
						grnewdbDB.executeUpdate("delete from UrgentMaterialCheckOCR where closeDate is null " +
								" and GRN='"+grn+"'");
					}else {
						String[] dou=new String[6];
			            String pn = rs.getString("partNumber");
			            if(rsMap.containsKey(grn+pn)){
			                dou=rsMap.get(grn+pn);
			                dou[0]=rs.getString("partNumber");
			                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
			                dou[2]=(Integer.parseInt(dou[2])+1) + "";
			                if(dou[3].length()< 50000) {
			                    dou[3]=rs.getString("rid")+","+dou[3];
			                }             
			                dou[4]=rs.getString("plent");
			                dou[5]=rs.getString("UpAegisDATE");
			                rsMap.put(grn+pn, dou);
			            }else{
			                dou[0]=rs.getString("partNumber");
			                dou[1]=rs.getString("printQTY");
			                dou[2]="1";
			                dou[3]=rs.getString("rid");
			                dou[4]=rs.getString("plent");
			                dou[5]=rs.getString("UpAegisDATE");
			                rsMap.put(grn+pn, dou);
			            }
					}
					i += 1;
					commonsLog.info("VPS查询GRN:" + i);
					commonsLog.info("GRN:" + grn);
		        }
				System.out.println("循环耗时：" + (System.currentTimeMillis() - startTime) / 60000);
				commonsLog.info("退出循环");
		        commonsLog.info("2b vendorrid size:"+rsMap.size());
		        for(String key : rsMap.keySet()) {
		            String[] temp = rsMap.get(key);
		            ResultSet rsA = null;
		            boolean flagQM = false;
		            String[] UIDs = temp[3].split(",");
		            for (String UID : UIDs) {
		            	pstmtA3.setString(1, UID) ;
		            	rsA = pstmtA3.executeQuery();
		            	if(rsA.next()) {
		            		commonsLog.info("time");
		            		commonsLog.info(rsA.getString("localtime"));
		            		flagQM = true;
		            		break;
		            	}
					}
		            if(flagQM){
		            	UrgentMaterialCheckOCR umc = new UrgentMaterialCheckOCR();
		            	String endDateTime = "";
						/*String grnSub = key.substring(0, 10);
						ResultSet rsIqc;
						// sa获取 321 122 时间改为IQC
						findIqcDateByGrn.setString(1, grnSub);
						rsIqc = findIqcDateByGrn.executeQuery();
						if (rsIqc.next()) {
							// 321 或 122 时间
							String iqcDate = rsIqc.getString("IQCdate");
							if (iqcDate.length() != 0 && iqcDate != null ) {
								endDateTime = iqcDate;
								umc.setCloseDate(iqcDate);
							} else {
								endDateTime = nowDayTime;
								umc.setCloseDate(null);
							}
						}*/
		            	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
		            	if(!"".equals(sapDateTime[2])) {
		            		endDateTime = sapDateTime[2];
		            		umc.setCloseDate(sapDateTime[2]);
		            	}else{
		            		endDateTime = nowDayTime;
		            		umc.setCloseDate(null);
		            	}
	                    umc.setGRN(key.substring(0,10));
	                    umc.setItemNumber(temp[0]);
	                    umc.setGRNQuantity(temp[1]);
	                    umc.setUIDQuantity(temp[2]);
	                    umc.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                    if(rsA.getString("localtime") != null && !"null".equals(rsA.getString("localtime")) && !"".equals(rsA.getString("localtime"))) {
	                    	umc.setRDFinishTime(rsA.getString("localtime"));
	                    }else {
		                    umc.setRDFinishTime("");
	                    }
	                    String auditDataTime = umc.getRDFinishTime();
	                    pstmtA2.setString(1, temp[0]);
	                    pstmtA2.setString(2, nowDay);
	                    ResultSet rsB = pstmtA2.executeQuery();
	                    if(rsB.next()) {
	                        umc.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                    }else {
	                        //根据工厂 物料 取工单开始日期
	                        double totalInventory = 0.0;
	                        double leftInventory = 0.0;
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
	                        umc.setProductionTime(soStartDate);
	                    }
	                    
	                    umc.setType("");
						String[] split = temp[0].split("-");
						String plant = temp[4];
						if(plant.equals("5000")&&!split[2].contains("D")){
							umc.setType("D");
						}else{
		                    if(!"NA".equals(umc.getProductionTime()) ){
	    						if(umc.getProductionTime().compareTo(nowDayAdd2) <= 0) {
	    							umc.setType("A");
	    						}else if(umc.getProductionTime().compareTo(nowDayAdd4) <= 0) {
	    							umc.setType("B");
	    						}else{
									continue;
								}
							}else {
								continue;
							}							
						}
	                    umc.setUID(UIDs[0]);
	                    umc.setPlant(temp[4]);
	                    umc.setAuditDataTime(auditDataTime);
	                    //计算OCR 检验等待时间
	                    if (!"".equals(auditDataTime)&&!"null".equalsIgnoreCase(auditDataTime)) {
	                        if (auditDataTime.substring(11).compareTo("21:00")>0) {
	                            auditDataTime = auditDataTime.substring(0, 10) + " 21:00";
	                        }
	                        if (auditDataTime.substring(11).compareTo("08:00")<0) {
	                            auditDataTime = auditDataTime.substring(0, 10) + " 08:00";
	                        }
	                        if(auditDataTime.compareTo(endDateTime) > 0) {
	                            umc.setOCRCheckWaitTime("0.0");
	                        }else {
	                        	int day = DateUtils.diffDays(df.parse(endDateTime.substring(0, 10)), df.parse(auditDataTime.substring(0, 10)) );
								int hour = 0;
								int min = 0;
								if(day==0) {  // 同一天
									hour = Integer.parseInt(endDateTime.substring(11, 13)) - Integer.parseInt(auditDataTime.substring(11, 13));
									min = Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
									min = min + calFoodTime(auditDataTime, endDateTime);
								}else {
									day = day-1;
									hour = day*11;
									hour = hour + 21 - Integer.parseInt(auditDataTime.substring(11, 13));
									min = min + Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
									min = min + calFoodTime(auditDataTime, auditDataTime.substring(0, 10)+" 21:00");
									hour = hour + Integer.parseInt(endDateTime.substring(11, 13)) - 8;
									min = min + calFoodTime(endDateTime.substring(0, 10)+" 08:00", endDateTime);
								}
								min = hour * 60 + min;
								//
								double minToHour = min/60.0;
	                            umc.setOCRCheckWaitTime(String.format("%.2f", minToHour));
	                        }
	                    }else {
	                        umc.setOCRCheckWaitTime("0.0");
	                    }
	                    umc.setSequence(seq);
	                    umc.setCreatedate(nowDayTime);
	                    if(!"".equals(umc.getRDFinishTime())){
	                    	commonsLog.info("添加");
	                    	list.add(umc);
	                    }
		            }
		        }
		        //pcbvendorrid
		        rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent, UpAegisDATE from pcbvendorrid " +
		                " where convert(varchar(10),GRNDATE,23) between '"+nowDay_2+"' and '"+nowDay+"' and printQTY<>0.0  and plent in ('1100','1200','5000') and ( " +
		                " partNumber like '009%' or partNumber like '021%' or partNumber like '023%' or " +
		                " partNumber like '035%' or partNumber like '050%' or partNumber like '051%' or " +
		                " partNumber like '096%' or partNumber like '110%' or partNumber like '121%' or partNumber like '123%' or " +
		                " partNumber like '129%' or partNumber like '140%' or partNumber like '147%' or partNumber like '155%' or " +
		                " partNumber like '233%' or partNumber like '237%' or partNumber like '256%' or partNumber like '262%' or partNumber like '265%' or " +
		                " partNumber like '275%' or partNumber like '470%' " +
		                " ) ");
				rsMap=new HashMap<String,String[]>();
		        while (rs.next()) {
		        	String grn = rs.getString("grn");
		        	pstmtDB2.setString(1, grn);
					ResultSet rsFinish  = pstmtDB2.executeQuery();
					if(rsFinish.next()) {
						grnewdbDB.executeUpdate("delete from UrgentMaterialCheckOCR where closeDate is null " +
								" and GRN='"+grn+"'");
					}else {
						String[] dou=new String[6];
			            String pn = rs.getString("partNumber");
			            if(rsMap.containsKey(grn+pn)){
			                dou=rsMap.get(grn+pn);
			                dou[0]=rs.getString("partNumber");
			                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
			                dou[2]=(Integer.parseInt(dou[2])+1) + "";
			                if(dou[3].length()< 50000) {
			                    dou[3]=rs.getString("rid")+","+dou[3];
			                }             
			                dou[4]=rs.getString("plent");
			                dou[5]=rs.getString("UpAegisDATE");
			                rsMap.put(grn+pn, dou);
			            }else{
			                dou[0]=rs.getString("partNumber");
			                dou[1]=rs.getString("printQTY");
			                dou[2]="1";
			                dou[3]=rs.getString("rid");
			                dou[4]=rs.getString("plent");
			                dou[5]=rs.getString("UpAegisDATE");
			                rsMap.put(grn+pn, dou);
			            }
					}
		        }
		        commonsLog.info("2b pcbvendorrid size:"+rsMap.size());
		        for(String key : rsMap.keySet()) {
		            String[] temp = rsMap.get(key);
		            ResultSet rsA = null;
		            boolean flagQM = false;
		            String[] UIDs = temp[3].split(",");
		            for (String UID : UIDs) {
		            	pstmtA3.setString(1, UID) ;
		            	rsA = pstmtA3.executeQuery();
		            	if(rsA.next()) {
		            		flagQM = true;
		            		break;
		            	}
					}
		            if(flagQM){
		            	UrgentMaterialCheckOCR umc = new UrgentMaterialCheckOCR();
		            	String endDateTime = "";
						/*String grnSub = key.substring(0, 10);
						ResultSet rsIqc;
						// sa获取 321 122 时间改为IQC
						findIqcDateByGrn.setString(1, grnSub);
						rsIqc  = findIqcDateByGrn.executeQuery();
						if (rsIqc.next()) {
							// 321 或 122 时间
							String iqcDate = rsIqc.getString("IQCdate");
							if (iqcDate.length() != 0 && iqcDate != null ) {
								endDateTime = iqcDate;
								umc.setCloseDate(iqcDate);
							} else {
								endDateTime = nowDayTime;
								umc.setCloseDate(null);
							}
						}*/
		            	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
		            	if(!"".equals(sapDateTime[2])) {
		            		endDateTime = sapDateTime[2];
		            		umc.setCloseDate(sapDateTime[2]);
		            	}else{
		            		endDateTime = nowDayTime;
		            		umc.setCloseDate(null);
		            	}
	                    umc.setGRN(key.substring(0,10));
	                    umc.setItemNumber(temp[0]);
	                    umc.setGRNQuantity(temp[1]);
	                    umc.setUIDQuantity(temp[2]);
	                    umc.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                    if(rsA.getString("localtime") != null && !"null".equals(rsA.getString("localtime")) && !"".equals(rsA.getString("localtime"))) {
	                    	umc.setRDFinishTime(rsA.getString("localtime"));
	                    }else {
		                    umc.setRDFinishTime("");
	                    }
	                    String auditDataTime = umc.getRDFinishTime();
	                    pstmtA2.setString(1, temp[0]);
	                    pstmtA2.setString(2, nowDay);
	                    ResultSet rsB = pstmtA2.executeQuery();
	                    if(rsB.next()) {
	                        umc.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
	                    }else {
	                        //根据工厂 物料 取工单开始日期
	                        double totalInventory = 0.0;
	                        double leftInventory = 0.0;
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
	                        umc.setProductionTime(soStartDate);
	                    }
	                    umc.setType("");
						String[] split = temp[0].split("-");
						String plant = temp[4];
						if(plant.equals("5000")&&!split[2].contains("D")){
							umc.setType("D");
						}else{
		                    if(!"NA".equals(umc.getProductionTime()) ){
	    						if(umc.getProductionTime().compareTo(nowDayAdd2) <= 0) {
	    							umc.setType("A");
	    						}else if(umc.getProductionTime().compareTo(nowDayAdd4) <= 0) {
	    							umc.setType("B");
	    						}else{
									continue;
								}
							}else {
								continue;
							}							
						}
	                    umc.setUID(UIDs[0]);
	                    umc.setPlant(temp[4]);
	                    umc.setAuditDataTime(auditDataTime);
	                    //计算OCR 检验等待时间
	                    if (!"".equals(auditDataTime)&&!"null".equalsIgnoreCase(auditDataTime)) {
	                        if (auditDataTime.substring(11).compareTo("21:00")>0) {
	                            auditDataTime = auditDataTime.substring(0, 10) + " 21:00";
	                        }
	                        if (auditDataTime.substring(11).compareTo("08:00")<0) {
	                            auditDataTime = auditDataTime.substring(0, 10) + " 08:00";
	                        }
	                        if(auditDataTime.compareTo(endDateTime) > 0) {
	                            umc.setOCRCheckWaitTime("0.0");
	                        }else {
	                        	int day = DateUtils.diffDays(df.parse(endDateTime.substring(0, 10)), df.parse(auditDataTime.substring(0, 10)) );
								int hour = 0;
								int min = 0;
								if(day==0) {  // 同一天
									hour = Integer.parseInt(endDateTime.substring(11, 13)) - Integer.parseInt(auditDataTime.substring(11, 13));
									min = Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
									min = min + calFoodTime(auditDataTime, endDateTime);
								}else {
									day = day-1;
									hour = day*11;
									hour = hour + 21 - Integer.parseInt(auditDataTime.substring(11, 13));
									min = min + Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
									min = min + calFoodTime(auditDataTime, auditDataTime.substring(0, 10)+" 21:00");
									hour = hour + Integer.parseInt(endDateTime.substring(11, 13)) - 8;
									min = min + calFoodTime(endDateTime.substring(0, 10)+" 08:00", endDateTime);
								}
								min = hour * 60 + min;
								//
								double minToHour = min/60.0;
	                            umc.setOCRCheckWaitTime(String.format("%.2f", minToHour));
	                        }
	                    }else {
	                        umc.setOCRCheckWaitTime("0.0");
	                    }
	                    umc.setSequence(seq);
	                    umc.setCreatedate(nowDayTime);
	                    if(!"".equals(umc.getRDFinishTime())){
	                    	list.add(umc);
	                    }
		            }
		        }
				commonsLog.info("list输出");
				for (int j = 0; j < list.size(); j++) {
					commonsLog.info("list" + j + ":" + list.get(j).getGRN());
				}
				umCheckOCRService.saveUrgentMaterialCheckOCR(list);
				commonsLog.info("插入完成");
			}
	        vpsDB.close();
	        conMes.close();
	        grnewdbDB.close();
		} catch (Exception e) {
			commonsLog.error("Exception:", e);
		}
		commonsLog.info("end...");
	}
	
	public static void main(String[] args) {
		try {
			new AutoUrgentMaterialCheckOCR().execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	
	
}