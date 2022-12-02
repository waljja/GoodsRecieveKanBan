package ht.task;

import ht.biz.IUrgentMaterialCheckNotOCRService;
import ht.entity.UrgentMaterialCheckNotOCR;
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
 * 急料检验（非OCR）看板
 *
 * @author 刘惠明
 * @date 2020-9-3
 */
public class AutoUrgentMaterialCheckNotOCR {
	@Autowired
    private IUrgentMaterialCheckNotOCRService umCheckNotOCRService;

	private static Log commonsLog = LogFactory.getLog(AutoUrgentMaterialCheckNotOCR.class);

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
	    	ConKanBan conKanBan = new ConKanBan();
	    	Connection connKanBan = conKanBan.con;
	    	SAPService sap = new SAPService();
	        //
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        String nowDay = df.format(c.getTime());
	        String year = nowDay.substring(0, 4);
	        String nowDayTime = df2.format(c.getTime());
	        c.add(Calendar.DATE, -2); // -7 天
	        String nowDay_2 = df.format(c.getTime());
	        //自动任务在系统时间08:00-12:00 13:00-17:00 18:00-21:00时执行
			if( (nowDayTime.substring(11).compareTo("08:00") >0 && nowDayTime.substring(11).compareTo("12:00") <=0 ) 
					|| (nowDayTime.substring(11).compareTo("13:00") >0 && nowDayTime.substring(11).compareTo("17:00") <=0 )
					|| (nowDayTime.substring(11).compareTo("18:00") >0 && nowDayTime.substring(11).compareTo("21:00") <=0 )  ){
				
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
				String nowDayAdd5 = df.format(c.getTime());
				c.add(Calendar.DATE, +1);
				nowDayAdd5 = df.format(c.getTime());
				pstmt.setString(1, nowDayAdd5);
				rsDay = pstmt.executeQuery();
				while(rsDay.next()) {
					c.add(Calendar.DATE, +1);
					nowDayAdd5 = df.format(c.getTime());
					pstmt.setString(1, nowDayAdd5);
					rsDay = pstmt.executeQuery();
				}
				//
				List<UrgentMaterialCheckNotOCR> list = new ArrayList<UrgentMaterialCheckNotOCR>();
				List<String> deleteList = new ArrayList<String>();
				int seq = 1;
				ResultSet rs = grnewdbDB.executeQuery("select max(Sequence) as maxseq from UrgentMaterialCheckNotOCR where Sequence is not null");
				if(rs.next()) {
					seq = rs.getInt("maxseq")+1;
				}
				// Aegis
		        /* PreparedStatement pstmtA1 = connMes.prepareStatement("select II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " left join StockLocations SL on SL.ID = IIH.StockLocationID" +
	                    " where II.Identifier = ? and SL.Identifier is not null " +
	            		" order by IIH.TimePosted_BaseDateTimeUTC desc "); */
				PreparedStatement pstmtA1 = connMes.prepareStatement("select " +
						"II.UID, II.ToStock_Input as StockLocation, IIH.StockLocation as 'historyStock', IIH.TimePosted_BaseDateTimeUTC AS 'localtime' " +
						"from " +
						"[dbo].[UID_xTend_MaterialTransactionsRHDCDW] II " +
						"left join " +
						"ItemInventoryHistories IIH " +
						"on " +
						"IIH.UID COLLATE Chinese_PRC_CI_AS = II.UID " +
						"where " +
						"II.UID = ? " +
						"and " +
						"IIH.StockLocation is not null " +
						"order by " +
						"IIH.TimePosted_BaseDateTimeUTC " +
						"desc");
		        /*PreparedStatement pstmtA2 = connMes.prepareStatement("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
	                    "where ReceivingNumber=?");*/
				/*PreparedStatement pstmtA3 = connAegis.prepareStatement("select RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
						" where PartNumber = ? and convert(varchar(10),RequireTime,23) =? order by RequireTime ");*/
		        PreparedStatement pstmtA3 = connMes.prepareStatement("select " +
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
		        /*PreparedStatement pstmtA4 = connMes.prepareStatement("select II.Identifier, II.StockLocation " +
	        			" from ItemInventories II " +
	        			" where (II.StockLocation like'%QM%') and II.Identifier = ? ");*/
		        /*PreparedStatement pstmtA5 = connMes.prepareStatement("select II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
			    		" from ItemInventories II " +
			            " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
			            " left join StockLocations SL on SL.ID = IIH.StockLocationID" +
			            " where (SL.Identifier like'%IQ%' ) and II.Identifier = ? " +
			    		" order by IIH.TimePosted_BaseDateTimeUTC desc ");*/
				PreparedStatement pstmtA5 = connMes.prepareStatement("select " +
						"II.UID, II.ToStock_Input as StockLocation, IIH.StockLocation as 'historyStock', IIH.TimePosted_BaseDateTimeUTC AS 'localtime' " +
						"from " +
						"[dbo].[UID_xTend_MaterialTransactionsRHDCDW] II " +
						"left join " +
						"ItemInventoryHistories IIH " +
						"on " +
						"IIH.UID COLLATE Chinese_PRC_CI_AS = II.UID " +
						"where " +
						"II.UID = ? " +
						"and " +
						"IIH.StockLocation like '%IQ%' " +
						"order by " +
						"IIH.TimePosted_BaseDateTimeUTC " +
						"desc");
		        /*PreparedStatement pstmtA6 = connMes.prepareStatement("select II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
			    		" from ItemInventories II " +
			            " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
			            " left join StockLocations SL on SL.ID = IIH.StockLocationID" +
			            " where (SL.Identifier like'%QM%' ) and II.Identifier = ? " +
			    		" order by IIH.TimePosted_BaseDateTimeUTC asc ");*/
				PreparedStatement pstmtA6 = connMes.prepareStatement("select " +
						"II.UID, II.ToStock_Input as StockLocation, IIH.StockLocation as 'historyStock', IIH.TimePosted_BaseDateTimeUTC AS 'localtime' " +
						"from " +
						"[dbo].[UID_xTend_MaterialTransactionsRHDCDW] II " +
						"left join " +
						"ItemInventoryHistories IIH " +
						"on " +
						"IIH.UID COLLATE Chinese_PRC_CI_AS = II.UID " +
						"where " +
						"II.UID = ? " +
						"and " +
						"IIH.StockLocation like '%QM%' " +
						"order by " +
						"IIH.TimePosted_BaseDateTimeUTC " +
						"desc ");
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
		        PreparedStatement pstmtDB1 = connDB.prepareStatement("select inventory,needQty,gotQty,soStartDate " +
	            		" from NotFinishSO where plant=? and bom=? order by soStartDate ");
		        PreparedStatement pstmtDB2 = connDB.prepareStatement("select * from UrgentMaterialCheckNotOCR where closeDate is not null " +
						" and GRN=? ");
				//vendorrid
		        //2020-12-11 by XiaoYing HAO
		        //009 021 023 035 050 051 096 110 121 123 129 140 147  155 233 237 256 262 265 275 470
				pstmt = connVPS.prepareStatement("select grn, partNumber, printQTY, rid, plent, GRNDATE, UpAegisDATE from vendorrid " +
		        		" where convert(varchar(10),GRNDATE,23) between ? and ? and printQTY<>0.0 and plent in ('1100','1200','5000')  and ( " +
		                " partNumber not like '009%' and partNumber not like '021%' and partNumber not like '023%' and " +
		                " partNumber not like '035%' and partNumber not like '050%' and partNumber not like '051%' and " +
		                " partNumber not like '096%' and partNumber not like '110%' and partNumber not like '121%' and partNumber not like '123%' and " +
		                " partNumber not like '129%' and partNumber not like '140%' and partNumber not like '147%' and partNumber not like '155%' and " +
		                " partNumber not like '233%' and partNumber not like '237%' and partNumber not like '256%' and partNumber not like '262%' and partNumber not like '265%' and " +
		                " partNumber not like '275%' and partNumber not like '470%' " +
		                " )  ");
				pstmt.setString(1, nowDay_2);
				pstmt.setString(2, nowDay);
				rs = pstmt.executeQuery();
				Map<String,String[]> rsMap=new HashMap<String,String[]>();
		        while (rs.next()) {
		        	String grn = rs.getString("grn");
		        	String pn = rs.getString("partNumber");
		        	pstmtDB2.setString(1, grn);
					ResultSet rsFinish  = pstmtDB2.executeQuery();
					if(rsFinish.next()) {
						grnewdbDB.executeUpdate("delete from UrgentMaterialCheckNotOCR where closeDate is null " +
								" and GRN='"+grn+"'");
					}else {
						commonsLog.info("赋值：" + rs.getString("rid"));
						String[] dou=new String[7];
			            if(rsMap.containsKey(grn+pn)){
			                dou=rsMap.get(grn+pn);
			                dou[0]=rs.getString("partNumber");
			                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
			                dou[2]=(Integer.parseInt(dou[2])+1) + "";
			                if(dou[3].length() < 50000) {
			                    dou[3]=rs.getString("rid")+","+dou[3];
			                }             
			                dou[4]=rs.getString("plent");
			                dou[5]=rs.getString("GRNDATE");
			                dou[6]=rs.getString("UpAegisDATE");
			                rsMap.put(grn+pn, dou);
			            }else{
			                dou[0]=rs.getString("partNumber");
			                dou[1]=rs.getString("printQTY");
			                dou[2]="1";
			                dou[3]=rs.getString("rid");
			                dou[4]=rs.getString("plent");
			                dou[5]=rs.getString("GRNDATE");
			                dou[6]=rs.getString("UpAegisDATE");
			                rsMap.put(grn+pn, dou);
			            }
					}
		        }
		        commonsLog.info("2a vendorrid size:"+rsMap.size());
		        for(String key : rsMap.keySet()) {
	            	//
		            String[] temp = rsMap.get(key);
		            ResultSet rsA = null;
		            boolean flagIQC = false;
		            String[] UIDs = temp[3].split(",");
		            for (String UID : UIDs) {
		            	pstmtA5.setString(1, UID) ;
		            	rsA = pstmtA5.executeQuery();
		            	if(rsA.next()) {
		            		flagIQC = true;
		            		break;
		            	}
					}
		            if(flagIQC){
		            	//有IQ库位
		            	UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
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
								umcn.setCloseDate(iqcDate);
							} else {
								endDateTime = nowDayTime;
								umcn.setCloseDate(null);
							}
						}*/
		            	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
		            	if(!"".equals(sapDateTime[2])) {
		            		endDateTime = sapDateTime[2];
		            		umcn.setCloseDate(sapDateTime[2]);
		            	}else{
		            		endDateTime = nowDayTime;
		            		umcn.setCloseDate(null);
		            	}
		            	String uid = rsA.getString("UID");
		            	commonsLog.info("flagIQC: " + uid);
		            	pstmtA1.setString(1, uid);
		            	ResultSet rsA2 = pstmtA1.executeQuery();
		            	umcn.setIQCReturnTime("");
		            	if(rsA2.next() && !rsA2.getString("historyStock").contains("IQ")){
		            		//有IQ库位，但此UID，还有QM在后，表示已归还
		            		if(rsA2.getString("localtime") != null && !"null".equals(rsA2.getString("localtime")) && !"".equals(rsA2.getString("localtime"))) {
			            		umcn.setIQCReturnTime(rsA2.getString("localtime").substring(0,16));
			            	}
		            	}
		                umcn.setGRN(key.substring(0,10));
		                umcn.setItemNumber(temp[0]);
		                //绑QM的时间
		                pstmtA6.setString(1, uid);
		            	ResultSet rsA6 = pstmtA6.executeQuery();
		            	if(rsA6.next() && rsA6.getString("localtime") != null && !"null".equals(rsA6.getString("localtime")) && !"".equals(rsA6.getString("localtime"))) {
		            		umcn.setRDFinishTime(rsA6.getString("localtime").substring(0,16));
		            	}else {
			                umcn.setRDFinishTime("");
		            	}
		                umcn.setIQCGetTime(rsA.getString("localtime").substring(0,16));//IQC取样时间
	                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                    pstmtA3.setString(1, temp[0]);
	                    pstmtA3.setString(2, nowDay);
	                    ResultSet rsB = pstmtA3.executeQuery();
	                    if(rsB.next()) {
	                        umcn.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
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
	                        umcn.setProductionTime(soStartDate);
	                    }
	                    umcn.setType("");
						String[] split = temp[0].split("-");
						String plant = temp[4];
						if(plant.equals("5000")&&!split[2].contains("D")){
							umcn.setType("D");
						}else{
		                    if(!"NA".equals(umcn.getProductionTime()) ){
	    						if(umcn.getProductionTime().compareTo(nowDayAdd2) <= 0) {
	    							umcn.setType("A");
	    						}else if(umcn.getProductionTime().compareTo(nowDayAdd4) <= 0) {
	    							umcn.setType("B");
	    						}else{
									continue;
								}
							}else {
								continue;
							}							
						}
						

	                    umcn.setUID(uid);
	                    umcn.setPlant(temp[4]);
	                    umcn.setGRNDATE(temp[5]);
	                    // 计算IQC检验等待时间
	                    String auditDataTime = umcn.getRDFinishTime();              
	                    if (!"".equals(auditDataTime)&&!"null".equalsIgnoreCase(auditDataTime)) {
	                        if (auditDataTime.substring(11).compareTo("21:00")>0) {
	                        	auditDataTime = auditDataTime.substring(0, 10) + " 21:00";
	                        }
	                        if (auditDataTime.substring(11).compareTo("08:00")<0) {
	                        	auditDataTime = auditDataTime.substring(0, 10) + " 08:00";
	                        }
	                        if(auditDataTime.compareTo(endDateTime) > 0) {
	                            umcn.setIQCCheckWaitTime("0.0");
	                        }else {
	                        	int day = DateUtils.diffDays(df.parse(endDateTime.substring(0, 10)), df.parse(auditDataTime.substring(0, 10)) );
								int hour = 0;
								int min = 0;
								if(day==0) {  // 同一天
									hour = Integer.parseInt(endDateTime.substring(11, 13)) - Integer.parseInt(auditDataTime.substring(11, 13));
									min = Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
									min = min + calFoodTime(auditDataTime, endDateTime);
								}else {
									if(df2.parse(auditDataTime).getDay() == 6) {
										day = day-1; //周六过账的，周日11小时 不计入
									}
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
								umcn.setIQCCheckWaitTime(String.format("%.2f", minToHour));
	                        }
	                    }else{
	                        umcn.setIQCCheckWaitTime("0.0");
	                    }
	                    umcn.setSequence(seq);
	                    umcn.setCreatedate(nowDayTime);
	                    if(!"".equals(umcn.getRDFinishTime())||umcn.getType().equals("D")) {
	                    	list.add(umcn);
							commonsLog.info("IQ" + umcn.getGRN());
							commonsLog.info("IQ" + umcn.getUID());
							commonsLog.info("IQ" + umcn.getPlant());
	                    }
	                    //end 有IQ 
		            }else {
		            	boolean flagQM = false;
		            	for (String UID : UIDs) {
		            		pstmtA6.setString(1, UID);
			            	rsA = pstmtA6.executeQuery();
			            	if(rsA.next()) {
			            		flagQM = true;
			            		break;
			            	}
						}
		            	if(flagQM) {
		            		UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
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
									umcn.setCloseDate(iqcDate);
								} else {
									endDateTime = nowDayTime;
									umcn.setCloseDate(null);
								}
							}*/
			            	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
			            	if(!"".equals(sapDateTime[2])) {
			            		endDateTime = sapDateTime[2];
			            		umcn.setCloseDate(sapDateTime[2]);
			            	}else{
			            		endDateTime = nowDayTime;
			            		umcn.setCloseDate(null);
			            	}
		            		String uid = rsA.getString("UID");
			                umcn.setGRN(key.substring(0,10));
			                umcn.setItemNumber(temp[0]);
			                //绑QM的时间
			                //pstmtA6.setString(1, uid);
			            	//ResultSet rsA6 = pstmtA6.executeQuery();
			            	if(rsA.getString("localtime") != null && !"null".equals(rsA.getString("localtime")) && !"".equals(rsA.getString("localtime"))) {
			            		umcn.setRDFinishTime(rsA.getString("localtime").substring(0,16));
			            	}else {
				                umcn.setRDFinishTime("");
			            	}
			                //
			            	umcn.setIQCGetTime("");
			            	umcn.setIQCReturnTime("");
		                    //
		                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
		                    pstmtA3.setString(1, temp[0]);
		                    pstmtA3.setString(2, nowDay);
		                    ResultSet rsB = pstmtA3.executeQuery();
		                    if(rsB.next()) {
		                        umcn.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
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
		                        umcn.setProductionTime(soStartDate);
		                    }
		                    umcn.setType("");
							String[] split = temp[0].split("-");
							String plant = temp[4];
							if(plant.equals("5000")&&!split[2].contains("D")){
								umcn.setType("D");
							}else{
			                    if(!"NA".equals(umcn.getProductionTime()) ){
		    						if(umcn.getProductionTime().compareTo(nowDayAdd2) <= 0) {
		    							umcn.setType("A");
		    						}else if(umcn.getProductionTime().compareTo(nowDayAdd4) <= 0) {
		    							umcn.setType("B");
		    						}else{
										continue;
									}
								}else {
									continue;
								}							
							}
		                    umcn.setUID(uid);
		                    umcn.setPlant(temp[4]);
		                    umcn.setGRNDATE(temp[5]);
		                    String auditDataTime = umcn.getRDFinishTime();              
		                    if (!"".equals(auditDataTime)&&!"null".equalsIgnoreCase(auditDataTime)) {
		                        if (auditDataTime.substring(11).compareTo("21:00")>0) {
		                        	auditDataTime = auditDataTime.substring(0, 10) + " 21:00";
		                        }
		                        if (auditDataTime.substring(11).compareTo("08:00")<0) {
		                        	auditDataTime = auditDataTime.substring(0, 10) + " 08:00";
		                        }
		                        if(auditDataTime.compareTo(endDateTime) > 0) {
		                            umcn.setIQCCheckWaitTime("0.0");
		                        }else {
		                        	int day = DateUtils.diffDays(df.parse(endDateTime.substring(0, 10)), df.parse(auditDataTime.substring(0, 10)) );
									int hour = 0;
									int min = 0;
									if(day==0) {  // 同一天
										hour = Integer.parseInt(endDateTime.substring(11, 13)) - Integer.parseInt(auditDataTime.substring(11, 13));
										min = Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
										min = min + calFoodTime(auditDataTime, endDateTime);
									}else {
										if(df2.parse(auditDataTime).getDay() == 6) {
											day = day-1; //周六过账的，周日11小时 不计入
										}
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
									umcn.setIQCCheckWaitTime(String.format("%.2f", minToHour));
		                        }
		                    }else{
		                        umcn.setIQCCheckWaitTime("0.0");
		                    }
		                    umcn.setSequence(seq);
		                    umcn.setCreatedate(nowDayTime);
		                    if(!"".equals(umcn.getRDFinishTime())||umcn.getType().equals("D")) {
		                    	list.add(umcn);
								commonsLog.info("QM" + umcn.getGRN());
								commonsLog.info("QM" + umcn.getUID());
								commonsLog.info("QM" + umcn.getPlant());
		                    }
		            	}
		            }
		        }
		        
		        //pcbvendorrid
		        pstmt = connVPS.prepareStatement("select grn, partNumber, printQTY, rid, plent, GRNDATE, UpAegisDATE from pcbvendorrid " +
		        		" where convert(varchar(10),GRNDATE,23) between ? and ? and printQTY<>0.0 and plent in ('1100','1200','5000')  and ( " +
		                " partNumber not like '009%' and partNumber not like '021%' and partNumber not like '023%' and " +
		                " partNumber not like '035%' and partNumber not like '050%' and partNumber not like '051%' and " +
		                " partNumber not like '096%' and partNumber not like '110%' and partNumber not like '121%' and partNumber not like '123%' and " +
		                " partNumber not like '129%' and partNumber not like '140%' and partNumber not like '147%' and partNumber not like '155%' and " +
		                " partNumber not like '233%' and partNumber not like '237%' and partNumber not like '256%' and partNumber not like '262%' and partNumber not like '265%' and " +
		                " partNumber not like '275%' and partNumber not like '470%' " +
		                " )  ");
				pstmt.setString(1, nowDay_2);
				pstmt.setString(2, nowDay);
				rs = pstmt.executeQuery();
				rsMap=new HashMap<String,String[]>();
		        while (rs.next()) {
		        	String grn = rs.getString("grn");
		        	String pn = rs.getString("partNumber");
		        	pstmtDB2.setString(1, grn);
					ResultSet rsFinish  = pstmtDB2.executeQuery();
					if(rsFinish.next()) {
						grnewdbDB.executeUpdate("delete from UrgentMaterialCheckNotOCR where closeDate is null " +
								" and GRN='"+grn+"'");
					}else {
						String[] dou=new String[7];
			            if(rsMap.containsKey(grn+pn)){
			                dou=rsMap.get(grn+pn);
			                dou[0]=rs.getString("partNumber");
			                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
			                dou[2]=(Integer.parseInt(dou[2])+1) + "";
			                if(dou[3].length()< 50000) {
			                    dou[3]=rs.getString("rid")+","+dou[3];
			                }             
			                dou[4]=rs.getString("plent");
			                dou[5]=rs.getString("GRNDATE");
			                dou[6]=rs.getString("UpAegisDATE");
			                rsMap.put(grn+pn, dou);
			            }else{
			                dou[0]=rs.getString("partNumber");
			                dou[1]=rs.getString("printQTY");
			                dou[2]="1";
			                dou[3]=rs.getString("rid");
			                dou[4]=rs.getString("plent");
			                dou[5]=rs.getString("GRNDATE");
			                dou[6]=rs.getString("UpAegisDATE");
			                rsMap.put(grn+pn, dou);
			            }
					}
		        }
		        commonsLog.info("2a pcbvendorrid size:"+rsMap.size());
		        for(String key : rsMap.keySet()) {
	            	//
		            String[] temp = rsMap.get(key);
		            ResultSet rsA = null;
		            boolean flagIQC = false;
		            String[] UIDs = temp[3].split(",");
		            for (String UID : UIDs) {
		            	pstmtA5.setString(1, UID) ;
		            	rsA = pstmtA5.executeQuery();
		            	if(rsA.next()) {
		            		flagIQC = true;
		            		break;
		            	}
					}
		            if(flagIQC){ //有IQ库位
		            	UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
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
								umcn.setCloseDate(iqcDate);
							} else {
								endDateTime = nowDayTime;
								umcn.setCloseDate(null);
							}
						}*/
		            	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
		            	if(!"".equals(sapDateTime[2])) {
		            		endDateTime = sapDateTime[2];
		            		umcn.setCloseDate(sapDateTime[2]);
		            	}else{
		            		endDateTime = nowDayTime;
		            		umcn.setCloseDate(null);
		            	}
		            	String uid = rsA.getString("UID");
		            	pstmtA1.setString(1, uid);
		            	ResultSet rsA2 = pstmtA1.executeQuery();
		            	umcn.setIQCReturnTime("");
		            	if(rsA2.next() && !rsA2.getString("historyStock").contains("IQ")){
		            		//有IQ库位，但此UID，还有QM在后，表示已归还
		            		if(rsA2.getString("localtime") != null && !"null".equals(rsA2.getString("localtime")) && !"".equals(rsA2.getString("localtime"))) {
			            		umcn.setIQCReturnTime(rsA2.getString("localtime").substring(0,16));
			            	}
		            	}
		                umcn.setGRN(key.substring(0,10));
		                umcn.setItemNumber(temp[0]);
		                //绑QM的时间
		                pstmtA6.setString(1, uid);
		            	ResultSet rsA6 = pstmtA6.executeQuery();
		            	if(rsA6.next() && rsA6.getString("localtime") != null && !"null".equals(rsA6.getString("localtime")) && !"".equals(rsA6.getString("localtime"))) {
		            		umcn.setRDFinishTime(rsA6.getString("localtime").substring(0,16));
		            	}else {
			                umcn.setRDFinishTime("");
		            	}
		                umcn.setIQCGetTime(rsA.getString("localtime").substring(0,16));//IQC取样时间
	                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
	                    pstmtA3.setString(1, temp[0]);
	                    pstmtA3.setString(2, nowDay);
	                    ResultSet rsB = pstmtA3.executeQuery();
	                    if(rsB.next()) {
	                        umcn.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
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
	                        umcn.setProductionTime(soStartDate);
	                    }
	                    umcn.setType("");
						String[] split = temp[0].split("-");
						String plant = temp[4];
						if(plant.equals("5000")&&!split[2].contains("D")){
							umcn.setType("D");
						}else{
		                    if(!"NA".equals(umcn.getProductionTime()) ){
	    						if(umcn.getProductionTime().compareTo(nowDayAdd2) <= 0) {
	    							umcn.setType("A");
	    						}else if(umcn.getProductionTime().compareTo(nowDayAdd4) <= 0) {
	    							umcn.setType("B");
	    						}else{
									continue;
								}
							}else {
								continue;
							}							
						}
	                    umcn.setUID(uid);
	                    umcn.setPlant(temp[4]);
	                    umcn.setGRNDATE(temp[5]);
	                    //计算IQC检验等待时间
	                    String auditDataTime = umcn.getRDFinishTime();              
	                    if (!"".equals(auditDataTime)&&!"null".equalsIgnoreCase(auditDataTime)) {
	                        if (auditDataTime.substring(11).compareTo("21:00")>0) {
	                        	auditDataTime = auditDataTime.substring(0, 10) + " 21:00";
	                        }
	                        if (auditDataTime.substring(11).compareTo("08:00")<0) {
	                        	auditDataTime = auditDataTime.substring(0, 10) + " 08:00";
	                        }
	                        if(auditDataTime.compareTo(endDateTime) > 0) {
	                            umcn.setIQCCheckWaitTime("0.0");
	                        }else {
	                        	int day = DateUtils.diffDays(df.parse(endDateTime.substring(0, 10)), df.parse(auditDataTime.substring(0, 10)) );
								int hour = 0;
								int min = 0;
								if(day==0) {  // 同一天
									hour = Integer.parseInt(endDateTime.substring(11, 13)) - Integer.parseInt(auditDataTime.substring(11, 13));
									min = Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
									min = min + calFoodTime(auditDataTime, endDateTime);
								}else {
									if(df2.parse(auditDataTime).getDay() == 6) {
										day = day-1; //周六过账的，周日11小时 不计入
									}
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
								umcn.setIQCCheckWaitTime(String.format("%.2f", minToHour));
	                        }
	                    }else{
	                        umcn.setIQCCheckWaitTime("0.0");
	                    }
	                    umcn.setSequence(seq);
	                    umcn.setCreatedate(nowDayTime);
	                    if(!"".equals(umcn.getRDFinishTime())||umcn.getType().equals("D")) {
	                    	list.add(umcn);
							commonsLog.info("IQ" + umcn.getGRN());
							commonsLog.info("IQ" + umcn.getUID());
							commonsLog.info("IQ" + umcn.getPlant());
	                    }
	                    //end 有IQ 
		            }else {
		            	boolean flagQM = false;
		            	for (String UID : UIDs) {
		            		pstmtA6.setString(1, UID);
			            	rsA = pstmtA6.executeQuery();
			            	if(rsA.next()) {
			            		flagQM = true;
			            		break;
			            	}
						}
		            	if(flagQM) {
		            		UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
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
									umcn.setCloseDate(iqcDate);
								} else {
									endDateTime = nowDayTime;
									umcn.setCloseDate(null);
								}
							}*/
			            	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
			            	if(!"".equals(sapDateTime[2])) {
			            		endDateTime = sapDateTime[2];
			            		umcn.setCloseDate(sapDateTime[2]);
			            	}else{
			            		endDateTime = nowDayTime;
			            		umcn.setCloseDate(null);
			            	}
			            	//
		            		String uid = rsA.getString("UID");
			                umcn.setGRN(key.substring(0,10));
			                umcn.setItemNumber(temp[0]);
			                //绑QM的时间
			                //pstmtA6.setString(1, uid);
			            	//ResultSet rsA6 = pstmtA6.executeQuery();
			            	if(rsA.getString("localtime") != null && !"null".equals(rsA.getString("localtime")) && !"".equals(rsA.getString("localtime"))) {
			            		umcn.setRDFinishTime(rsA.getString("localtime").substring(0,16));
			            	}else {
				                umcn.setRDFinishTime("");
			            	}
			                //
			            	umcn.setIQCGetTime("");
			            	umcn.setIQCReturnTime("");
		                    //
		                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
		                    pstmtA3.setString(1, temp[0]);
		                    pstmtA3.setString(2, nowDay);
		                    ResultSet rsB = pstmtA3.executeQuery();
		                    if(rsB.next()) {
		                        umcn.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
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
		                        umcn.setProductionTime(soStartDate);
		                    }
		                    umcn.setType("");
							String[] split = temp[0].split("-");
							String plant = temp[4];
							if(plant.equals("5000")&&!split[2].contains("D")){
								umcn.setType("D");
							}else{
			                    if(!"NA".equals(umcn.getProductionTime()) ){
		    						if(umcn.getProductionTime().compareTo(nowDayAdd2) <= 0) {
		    							umcn.setType("A");
		    						}else if(umcn.getProductionTime().compareTo(nowDayAdd4) <= 0) {
		    							umcn.setType("B");
		    						}else{
										continue;
									}
								}else {
									continue;
								}							
							}
		                    umcn.setUID(uid);
		                    umcn.setPlant(temp[4]);
		                    umcn.setGRNDATE(temp[5]);
		                    String auditDataTime = umcn.getRDFinishTime();              
		                    if (!"".equals(auditDataTime)&&!"null".equalsIgnoreCase(auditDataTime)) {
		                        if (auditDataTime.substring(11).compareTo("21:00")>0) {
		                        	auditDataTime = auditDataTime.substring(0, 10) + " 21:00";
		                        }
		                        if (auditDataTime.substring(11).compareTo("08:00")<0) {
		                        	auditDataTime = auditDataTime.substring(0, 10) + " 08:00";
		                        }
		                        if(auditDataTime.compareTo(endDateTime) > 0) {
		                            umcn.setIQCCheckWaitTime("0.0");
		                        }else {
		                        	int day = DateUtils.diffDays(df.parse(endDateTime.substring(0, 10)), df.parse(auditDataTime.substring(0, 10)) );
									int hour = 0;
									int min = 0;
									if(day==0) {  // 同一天
										hour = Integer.parseInt(endDateTime.substring(11, 13)) - Integer.parseInt(auditDataTime.substring(11, 13));
										min = Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(auditDataTime.substring(14, 16));
										min = min + calFoodTime(auditDataTime, endDateTime);
									}else {
										if(df2.parse(auditDataTime).getDay() == 6) {
											day = day-1; //周六过账的，周日11小时 不计入
										}
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
									umcn.setIQCCheckWaitTime(String.format("%.2f", minToHour));
		                        }
		                    }else{
		                        umcn.setIQCCheckWaitTime("0.0");
		                    }
		                    umcn.setSequence(seq);
		                    umcn.setCreatedate(nowDayTime);
		                    if(!"".equals(umcn.getRDFinishTime())||umcn.getType().equals("D")) {
		                    	list.add(umcn);
								commonsLog.info("QM" + umcn.getGRN());
								commonsLog.info("QM" + umcn.getUID());
								commonsLog.info("QM" + umcn.getPlant());
		                    }
		            	}
		            }
		        }
				commonsLog.info("list输出");
				for (int j = 0; j < list.size(); j++) {
					commonsLog.info("list" + (j+1) + ":" + list.get(j).getGRN());
				}
				umCheckNotOCRService.saveUrgentMaterialCheckNotOCR(list);
			}
	        //
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

	public static void main(String[]args) throws Exception{
		new AutoUrgentMaterialCheckNotOCR().execute();
	}
	
}