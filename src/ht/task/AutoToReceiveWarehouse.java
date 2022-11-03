package ht.task;

import ht.biz.IToReceiveWarehouseService;
import ht.entity.ToReceiveWarehouse;
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
 * 定时任务，获取数据，自动刷新 待入主料仓物料 看板
 * @author 刘惠明
 * @createDate 2020-9-3
 * @updateUser 丁国钊
 * @updateDate 2022-11-3
 * @updateRemark 替换Aegis数据源，增加代码注释，删除行尾注释，删除魔法值，使用boolean值替代if语句复杂条件判断
 * @version 2.0.0
 */
public class AutoToReceiveWarehouse {
	@Autowired
    private IToReceiveWarehouseService trWarehouseService;

	private static Log commonsLog = LogFactory.getLog(AutoToReceiveWarehouse.class);

	/**
	 * @description 获取看板数据
	 */
	public void execute() throws Exception {
		commonsLog.info("start...");
		try {
			// 数据库连接
			ConMes conMes = new ConMes();
			Connection connMes = conMes.con;
	    	ConVPS vpsDB = new ConVPS();
	    	Connection connVPS = vpsDB.con;
	    	ConDashBoard grnewdbDB = new ConDashBoard();
	    	Connection connDB = grnewdbDB.con;
	    	// SAP接口工具类
	    	SAPService sap = new SAPService();
	        // 时间变量
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        // 当前年月日
	        String nowDay = df.format(c.getTime());
	        // 当前年份
	        String year = nowDay.substring(0, 4);
	        // 当前年月份及小时、分钟数
	        String nowDayTime = df2.format(c.getTime());
	        // 当前小时、分钟数
	        String currentHM = nowDayTime.substring(11);
			// 当期时间 -2 天
	        c.add(Calendar.DATE, -2);
	        String nowDay_2 = df.format(c.getTime());
			// 所有计时为上班时间 8:00-12:00、13:00-17:00、18:00-21:00，其他时间进行数据获取
	        Boolean isWorkingTime = (currentHM.compareTo("08:00") >0 && currentHM.compareTo("12:00") <=0 )
					|| (currentHM.compareTo("13:00") >0 && currentHM.compareTo("17:00") <=0 )
					|| (currentHM.compareTo("18:00") >0 && currentHM.compareTo("21:00") <=0 ) ;
			// 在工作时间内，进行数据获取
			if(isWorkingTime){
				// 查询不同时间段的数据
				// + 1 天
				c.setTime(new Date());
				c.add(Calendar.DATE, +1);
				String nowDayAdd2 = df.format(c.getTime());
				PreparedStatement pstmt = connDB.prepareStatement("select ExcludeDate from schedul_ExcludeDate where ExcludeDate=?");
				pstmt.setString(1, nowDayAdd2);
				ResultSet rsDay = pstmt.executeQuery();
				while(rsDay.next()) {
					// + 1 天
					c.add(Calendar.DATE, +1);
					nowDayAdd2 = df.format(c.getTime());
					pstmt.setString(1, nowDayAdd2);
					rsDay = pstmt.executeQuery();
				}
				// + 1 天
				c.add(Calendar.DATE, +1);
				nowDayAdd2 = df.format(c.getTime());
				pstmt.setString(1, nowDayAdd2);
				rsDay = pstmt.executeQuery();
				while(rsDay.next()) {
					// + 1 天
					c.add(Calendar.DATE, +1);
					nowDayAdd2 = df.format(c.getTime());
					pstmt.setString(1, nowDayAdd2);
					rsDay = pstmt.executeQuery();
				}
				// + 1 天
				c.add(Calendar.DATE, +1);
				String nowDayAdd4 = df.format(c.getTime());
				pstmt.setString(1, nowDayAdd4);
				rsDay = pstmt.executeQuery();
				while(rsDay.next()) {
					// + 1 天
					c.add(Calendar.DATE, +1);
					nowDayAdd4 = df.format(c.getTime());
					pstmt.setString(1, nowDayAdd4);
					rsDay = pstmt.executeQuery();
				}
				// + 1 天
				c.add(Calendar.DATE, +1);
				nowDayAdd4 = df.format(c.getTime());
				pstmt.setString(1, nowDayAdd4);
				rsDay = pstmt.executeQuery();
				while(rsDay.next()) {
					// + 1 天
					c.add(Calendar.DATE, +1);
					nowDayAdd4 = df.format(c.getTime());
					pstmt.setString(1, nowDayAdd4);
					rsDay = pstmt.executeQuery();
				}
				List<ToReceiveWarehouse> list = new ArrayList<ToReceiveWarehouse>();
				// 查询最大序号
				int seq = 1;
				ResultSet rs = grnewdbDB.executeQuery("select max(Sequence) as maxseq from ToReceiveWarehouse where Sequence is not null");
				if(rs.next()) {
					seq = rs.getInt("maxseq")+1;
				}
				// SQL
				/*
				 * 条件为PartNumber、需求时间
				 * 按需求时间排序
				 * 原先为xTend_MissingMaterials表
				 * 改成TO_TransportOrder表和TO_TransportOrderItem表
				 * 131 DB modified by GuoZhao Ding
				 */
				PreparedStatement pstmtA1 = connMes.prepareStatement(" select a.CreateTime as RequireTime from [dbo].[TO_TransportOrder] a ,  " +
						"[dbo].[TO_TransportOrderItem] b where a.ID = b.TransportOrderID and b.Status = 'MissingPart' and b.PartNumber = ? " +
						"and a.LLDHDate= ? order by CreateTime ");
				/*
				PreparedStatement pstmtA1 = connMes.prepareStatement(" select RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	             		" where PartNumber = ? and convert(varchar(10),RequireTime,23) =? order by RequireTime ");
				*/
				/*
				 * 条件为UID、仓位（RH和CU）
				 * 按时间排序
				 * 原先为ItemInventories、StockLocations等表
				 * 改成UID_xTend_MaterialTransactionsRHDCDW表
				 * 131 DB modified by GuoZhao Ding
				*/
				PreparedStatement pstmtA2 = connMes.prepareStatement("select UID, ToStock_Input as StockLocation, TransactionTime AS 'localtime'  " +
						"from  " +
						"[dbo].[UID_xTend_MaterialTransactionsRHDCDW] UXM   " +
						"where  " +
						"UID = ?  " +
						"and  " +
						"ToStock_Input is not null  " +
						"and  " +
						"(ToStock_Input like'%RH%' or ToStock_Input like'%CU%')  " +
						"order by  " +
						"TransactionTime desc");
		        /*
		        PreparedStatement pstmtA2 = connMes.prepareStatement("select II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
			    		" from ItemInventories II " +
			            " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
			            " left join StockLocations SL on SL.ID = IIH.StockLocationID" +
			            " where SL.Identifier is not null and (SL.Identifier like'%RH%' or SL.Identifier like'%CU%') and II.Identifier = ? " +
			    		" order by IIH.TimePosted_BaseDateTimeUTC desc ");
			    */
				/*
				 * 条件为UID
				 * 原先为ItemInventories表
				 * 改成UID_xTend_MaterialTransactionsRHDCDW表
				 * 131 DB modified by GuoZhao Ding
				 */
				PreparedStatement pstmtA3 = connMes.prepareStatement("select " +
						"ToStock_Input as StockLocation " +
						"from " +
						"UID_xTend_MaterialTransactionsRHDCDW " +
						"where " +
						"UID = ? " +
						"and " +
						"ToStock_Input is not null " +
						"and " +
						"ToStock_Input != ''");
		        /*
		        PreparedStatement pstmtA3 = connMes.prepareStatement("select II.Identifier, II.StockLocation " +
	        			" from ItemInventories II " +
	        			" where II.Identifier = ? ");
	        	*/
				PreparedStatement pstmtDB1 = connDB.prepareStatement(" select inventory,needQty,gotQty,soStartDate " +
	                 	" from NotFinishSO where plant=? and bom=? order by soStartDate ");
				PreparedStatement pstmtDB2 = connDB.prepareStatement("select * from ToReceiveWarehouse where ReturnWarehouseTime <>'' " +
						" and GRN=? ");
				// vendorrid
				rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from vendorrid " +
		                " where convert(varchar(10),GRNDATE,23) between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000')  ");
				// 把查询结果存入map
				Map<String,String[]> rsMap=new HashMap<String,String[]>();
		        while (rs.next()) {
		        	// grn 来源于vps
		        	String grn = rs.getString("grn");
		        	String pn = rs.getString("partNumber");
		        	pstmtDB2.setString(1, grn);
					ResultSet rsFinish  = pstmtDB2.executeQuery();
					if(rsFinish.next()) {
						grnewdbDB.executeUpdate("delete from ToReceiveWarehouse where ReturnWarehouseTime ='' " +
								" and GRN='"+grn+"'");
					}else {
						String[] dou=new String[5];
			            if(rsMap.containsKey(grn+pn)){
			                dou=rsMap.get(grn+pn);
			                dou[0]=rs.getString("partNumber");
			                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
			                dou[2]=(Integer.parseInt(dou[2])+1) + "";
			                if(dou[3].length()< 5000) {
			                    dou[3]=rs.getString("rid")+","+dou[3];
			                }
			                dou[4]=rs.getString("plent");
			                rsMap.put(grn+pn, dou);
			            }else{
			                dou[0]=rs.getString("partNumber");
			                dou[1]=rs.getString("printQTY");
			                dou[2]="1";
			                dou[3]=rs.getString("rid");
			                dou[4]=rs.getString("plent");
			                rsMap.put(grn+pn, dou);
			            }
					}
		        }
		        commonsLog.info("3a vendorrid size:"+rsMap.size());
		        // 把Aegis数据放入model中，在jsp中展示
		        for(String key : rsMap.keySet()) {
		        	//
		        	String startDateTime = "";
		        	String endDateTime = "";
		        	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
	            	if("X".equalsIgnoreCase(sapDateTime[0])) {
	            		// 前端model
	            		ToReceiveWarehouse trw = new ToReceiveWarehouse();
	            		startDateTime = sapDateTime[2];
	            		trw.setCloseDate(sapDateTime[2]);
	            		trw.setSAPQualify("是");
	            		//
	            		String[] temp = rsMap.get(key);
			            ResultSet rsA = null;
			            boolean flagRH = false;
			            String[] UIDs = temp[3].split(",");
			            for (String UID : UIDs) {
			            	pstmtA2.setString(1, UID) ;
			            	rsA = pstmtA2.executeQuery();
			            	if(rsA.next()) {
			            		flagRH = true;
			            		break;
			            	}
						}
			            if(flagRH){
			            	trw.setUID(rsA.getString("Identifier"));
			            	trw.setReturnWarehouseTime(rsA.getString("localtime").substring(0,16));
			            	trw.setAegisQualify("是");
			            	endDateTime = rsA.getString("localtime").substring(0,16);
							// 收货库位
			            	trw.setReceivingLocation(rsA.getString("StockLocation"));
			            }else {
			            	trw.setUID(temp[3].substring(0,21));
			            	trw.setReturnWarehouseTime("");
			            	trw.setAegisQualify("是");
			            	endDateTime = nowDayTime;
			            	pstmtA3.setString(1, trw.getUID());
			            	ResultSet rsA3 = pstmtA3.executeQuery();
			            	if(rsA3.next()) {
								// 收货库位
			            		trw.setReceivingLocation(rsA3.getString("StockLocation"));
			            	}
			            }
		                trw.setGRN(key.substring(0, 10));
		                trw.setItemNumber(temp[0]);
		                trw.setGRNQuantity(temp[1]);
		                // partNumber
		                pstmtA1.setString(1, temp[0]);
		                pstmtA1.setString(2, nowDay);
		                ResultSet rsB = pstmtA1.executeQuery();
		                if(rsB.next()) {
		                	// 没用的
		                    trw.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
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
		                    trw.setProductionTime(soStartDate);
		                }
		                trw.setType("");
		                if(!"NA".equals(trw.getProductionTime()) ){
							if(trw.getProductionTime().compareTo(nowDayAdd2) <= 0) {
								trw.setType("A");
							}else if(trw.getProductionTime().compareTo(nowDayAdd4) <= 0) {
								trw.setType("B");
							}else{
								continue;
							}
						}else {
							continue;
						}
		                trw.setPlant(temp[4]);
		                //计算待入主料仓时间                   
		                if(!"".equals(startDateTime)&&!"null".equalsIgnoreCase(startDateTime)) { 
		                    if (startDateTime.substring(11).compareTo("21:00")>0) {
		                    	startDateTime = startDateTime.substring(0, 10) + " 21:00";
		                    }
		                    if (startDateTime.substring(11).compareTo("08:00")<0) {
		                    	startDateTime = startDateTime.substring(0, 10) + " 08:00";
		                    }
		                    if(startDateTime.compareTo(endDateTime) > 0) {
		                        trw.setWaitTimeToMainbin("0.0");
		                    }else {
	                        	int day = DateUtils.diffDays(df.parse(endDateTime.substring(0, 10)), df.parse(startDateTime.substring(0, 10)) );
								int hour = 0;
								int min = 0;
								// 同一天
								if(day==0) {
									hour = Integer.parseInt(endDateTime.substring(11, 13)) - Integer.parseInt(startDateTime.substring(11, 13));
									min = Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(startDateTime.substring(14, 16));
									min = min + calFoodTime(startDateTime, endDateTime);
								}else {
									day = day-1;
									hour = day*11;
									hour = hour + 21 - Integer.parseInt(startDateTime.substring(11, 13));
									min = min + Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(startDateTime.substring(14, 16));
									min = min + calFoodTime(startDateTime, startDateTime.substring(0, 10)+" 21:00");
									hour = hour + Integer.parseInt(endDateTime.substring(11, 13)) - 8;
									min = min + calFoodTime(endDateTime.substring(0, 10)+" 08:00", endDateTime);
								}
								min = hour * 60 + min;
								//
								double minToHour = min/60.0;
		                        trw.setWaitTimeToMainbin(String.format("%.2f", minToHour));
		                    }
		                }else{
		                    trw.setWaitTimeToMainbin("0.0");
		                }
		                trw.setSequence(seq);
		                trw.setCreatedate(nowDayTime);
		                list.add(trw);
		            }
		        }
		        //pcbvendorrid
				rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from pcbvendorrid " +
		                " where convert(varchar(10),GRNDATE,23) between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000')  ");
				rsMap=new HashMap<String,String[]>();
				while (rs.next()) {
		        	String grn = rs.getString("grn");
		        	String pn = rs.getString("partNumber");
		        	pstmtDB2.setString(1, grn);
					ResultSet rsFinish  = pstmtDB2.executeQuery();
					if(rsFinish.next()) {
						grnewdbDB.executeUpdate("delete from ToReceiveWarehouse where ReturnWarehouseTime ='' " +
								" and GRN='"+grn+"'");
					}else {
						String[] dou=new String[5];
			            if(rsMap.containsKey(grn+pn)){
			                dou=rsMap.get(grn+pn);
			                dou[0]=rs.getString("partNumber");
			                dou[1]=(Double.parseDouble(dou[1])+Double.parseDouble(rs.getString("printQTY"))) + "";
			                dou[2]=(Integer.parseInt(dou[2])+1) + "";
			                if(dou[3].length()< 5000) {
			                    dou[3]=rs.getString("rid")+","+dou[3];
			                }
			                dou[4]=rs.getString("plent");
			                rsMap.put(grn+pn, dou);
			            }else{
			                dou[0]=rs.getString("partNumber");
			                dou[1]=rs.getString("printQTY");
			                dou[2]="1";
			                dou[3]=rs.getString("rid");
			                dou[4]=rs.getString("plent");
			                rsMap.put(grn+pn, dou);
			            }
					}
		        }
		        commonsLog.info("3a pcbvendorrid size:"+rsMap.size());
		        for(String key : rsMap.keySet()) {
		        	//
		        	String startDateTime = "";
		        	String endDateTime = "";
		        	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
	            	if("X".equalsIgnoreCase(sapDateTime[0])) {
	            		ToReceiveWarehouse trw = new ToReceiveWarehouse();
	            		startDateTime = sapDateTime[2];
	            		trw.setCloseDate(sapDateTime[2]);
	            		trw.setSAPQualify("是");
	            		//
	            		String[] temp = rsMap.get(key);
			            ResultSet rsA = null;
			            boolean flagRH = false;
			            String[] UIDs = temp[3].split(",");
			            for (String UID : UIDs) {
			            	pstmtA2.setString(1, UID) ;
			            	rsA = pstmtA2.executeQuery();
			            	if(rsA.next()) {
			            		flagRH = true;
			            		break;
			            	}
						}
			            if(flagRH){
			            	trw.setUID(rsA.getString("Identifier"));
			            	trw.setReturnWarehouseTime(rsA.getString("localtime").substring(0,16));
			            	trw.setAegisQualify("是");
			            	endDateTime = rsA.getString("localtime").substring(0,16);
							// 收货库位
			            	trw.setReceivingLocation(rsA.getString("StockLocation"));
			            }else {
			            	trw.setUID(temp[3].substring(0,21));
			            	trw.setReturnWarehouseTime("");
			            	trw.setAegisQualify("是");
			            	endDateTime = nowDayTime;
			            	pstmtA3.setString(1, trw.getUID());
			            	ResultSet rsA3 = pstmtA3.executeQuery();
			            	if(rsA3.next()) {
								// 收货库位
			            		trw.setReceivingLocation(rsA3.getString("StockLocation"));
			            	}
			            }
		                trw.setGRN(key.substring(0, 10));
		                trw.setItemNumber(temp[0]);
		                trw.setGRNQuantity(temp[1]);
		                pstmtA1.setString(1, temp[0]);
		                pstmtA1.setString(2, nowDay);
		                ResultSet rsB = pstmtA1.executeQuery();
		                if(rsB.next()) {
		                    trw.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
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
		                    trw.setProductionTime(soStartDate);
		                }
		                trw.setType("");
		                if(!"NA".equals(trw.getProductionTime()) ){
							if(trw.getProductionTime().compareTo(nowDayAdd2) <= 0) {
								trw.setType("A");
							}else if(trw.getProductionTime().compareTo(nowDayAdd4) <= 0) {
								trw.setType("B");
							}else{
								continue;
							}
						}else {
							continue;
						}
		                
		                trw.setPlant(temp[4]);
		                //计算待入主料仓时间                   
		                if(!"".equals(startDateTime)&&!"null".equalsIgnoreCase(startDateTime)) { 
		                    if (startDateTime.substring(11).compareTo("21:00")>0) {
		                    	startDateTime = startDateTime.substring(0, 10) + " 21:00";
		                    }
		                    if (startDateTime.substring(11).compareTo("08:00")<0) {
		                    	startDateTime = startDateTime.substring(0, 10) + " 08:00";
		                    }
		                    if(startDateTime.compareTo(endDateTime) > 0) {
		                        trw.setWaitTimeToMainbin("0.0");
		                    }else {
	                        	int day = DateUtils.diffDays(df.parse(endDateTime.substring(0, 10)), df.parse(startDateTime.substring(0, 10)) );
								int hour = 0;
								int min = 0;
								// 同一天
								if(day==0) {
									hour = Integer.parseInt(endDateTime.substring(11, 13)) - Integer.parseInt(startDateTime.substring(11, 13));
									min = Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(startDateTime.substring(14, 16));
									min = min + calFoodTime(startDateTime, endDateTime);
								}else {
									day = day-1;
									hour = day*11;
									hour = hour + 21 - Integer.parseInt(startDateTime.substring(11, 13));
									min = min + Integer.parseInt(endDateTime.substring(14, 16)) - Integer.parseInt(startDateTime.substring(14, 16));
									min = min + calFoodTime(startDateTime, startDateTime.substring(0, 10)+" 21:00");
									hour = hour + Integer.parseInt(endDateTime.substring(11, 13)) - 8;
									min = min + calFoodTime(endDateTime.substring(0, 10)+" 08:00", endDateTime);
								}
								min = hour * 60 + min;
								//
								double minToHour = min/60.0;
		                        trw.setWaitTimeToMainbin(String.format("%.2f", minToHour));
		                    }
		                }else{
		                    trw.setWaitTimeToMainbin("0.0");
		                }
		                trw.setSequence(seq);
		                trw.setCreatedate(nowDayTime);
		                list.add(trw);
		            }
		        }
		        // 把查出来的数据插入看板数据库
		        trWarehouseService.saveToReceiveWarehouse(list);   
			}
	        // 关闭数据库连接
	        vpsDB.close();
	        conMes.close();
	        grnewdbDB.close();
		} catch (Exception e) {
			commonsLog.error("Exception:", e);
		}
		commonsLog.info("end...");
	}

	/**
	 * @param startDateTime endDateTime
	 * @return min
	 * 减去用餐时间
	 */
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