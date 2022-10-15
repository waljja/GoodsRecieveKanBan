package ht.task;

import ht.biz.IToReceiveWarehouseBService;
import ht.entity.ToReceiveWarehouseB;
import ht.entity.ToReceiveWarehouseB;
import ht.util.ConAegis;
import ht.util.ConDashBoard;
import ht.util.ConVPS;
import ht.util.DateUtils;
import ht.util.SAPService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;

/**
 * 3b
 * @date 2020-9-3
 * @author 刘惠明
 * 
 */

public class AutoToReceiveWarehouseB {
	private static Log commonsLog = LogFactory.getLog(AutoToReceiveWarehouseB.class);
    @Autowired
    private IToReceiveWarehouseBService trWarehouseBService;
	
    public void execute() throws Exception {
		commonsLog.info("start...");
		try {
	    	ConVPS vpsDB = new ConVPS();
	    	Connection connVPS = vpsDB.con;
	    	ConAegis aegisDB = new ConAegis();
	    	Connection connAegis = aegisDB.con;
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
	        //
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
				List<ToReceiveWarehouseB> list = new ArrayList<ToReceiveWarehouseB>();
				int seq = 1;
				ResultSet rs = grnewdbDB.executeQuery("select max(Sequence) as maxseq from ToReceiveWarehouseB where Sequence is not null");
				if(rs.next()) {
					seq = rs.getInt("maxseq")+1;
				}
				PreparedStatement pstmtA1 = connAegis.prepareStatement(" select RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
	             		" where PartNumber = ? and convert(varchar(10),RequireTime,23) =? order by RequireTime ");
				
		        PreparedStatement pstmtA2 = connAegis.prepareStatement("select II.Identifier, II.StockLocation, SL.Identifier as 'historyStock', DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
			    		" from ItemInventories II " +
			            " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
			            " left join StockLocations SL on SL.ID = IIH.StockLocationID" +
			            " where SL.Identifier is not null and (SL.Identifier like'%RT%') and II.Identifier = ? " +
			    		" order by IIH.TimePosted_BaseDateTimeUTC desc ");
		        PreparedStatement pstmtA3 = connAegis.prepareStatement("select II.Identifier, II.StockLocation " +
	        			" from ItemInventories II " +
	        			" where II.Identifier = ? ");
				PreparedStatement pstmtDB1 = connDB.prepareStatement(" select inventory,needQty,gotQty,soStartDate " +
	                 	" from NotFinishSO where plant=? and bom=? order by soStartDate ");
				PreparedStatement pstmtDB2 = connDB.prepareStatement("select * from ToReceiveWarehouseB where ReturnWarehouseTime <>'' " +
						" and GRN=? ");
				//vendorrid
				rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from vendorrid " +
		                " where convert(varchar(10),GRNDATE,23) between '"+nowDay_2+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000')  ");
				Map<String,String[]> rsMap=new HashMap<String,String[]>();
		        while (rs.next()) {
		        	String grn = rs.getString("grn");
		        	String pn = rs.getString("partNumber");
		        	pstmtDB2.setString(1, grn);
					ResultSet rsFinish  = pstmtDB2.executeQuery();
					if(rsFinish.next()) {
						grnewdbDB.executeUpdate("delete from ToReceiveWarehouseB where ReturnWarehouseTime ='' " +
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
		        commonsLog.info("3b vendorrid size:"+rsMap.size());
		        for(String key : rsMap.keySet()) {
		        	//
		        	String startDateTime = "";
		        	String endDateTime = "";
		        	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
	            	if("X".equalsIgnoreCase(sapDateTime[1])) {
	            		ToReceiveWarehouseB trw = new ToReceiveWarehouseB();
	            		startDateTime = sapDateTime[2];
	            		trw.setCloseDate(sapDateTime[2]);
	            		trw.setSAPQualify("否");
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
			            	trw.setAegisQualify("否");
			            	endDateTime = rsA.getString("localtime").substring(0,16);
			            	trw.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
			            }else {
			            	trw.setUID(temp[3].substring(0,21));
			            	trw.setReturnWarehouseTime("");
			            	trw.setAegisQualify("否");
			            	endDateTime = nowDayTime;
			            	pstmtA3.setString(1, trw.getUID());
			            	ResultSet rsA3 = pstmtA3.executeQuery();
			            	if(rsA3.next()) {
			            		trw.setReceivingLocation(rsA3.getString("StockLocation"));//收货库位
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
								if(day==0) {  // 同一天
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
						grnewdbDB.executeUpdate("delete from ToReceiveWarehouseB where ReturnWarehouseTime ='' " +
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
		        commonsLog.info("3b pcbvendorrid size:"+rsMap.size());
		        for(String key : rsMap.keySet()) {
		        	//
		        	String startDateTime = "";
		        	String endDateTime = "";
		        	String[] sapDateTime = sap.getGrnStatus(key.substring(0, 10), year);
	            	if("X".equalsIgnoreCase(sapDateTime[1])) {
	            		ToReceiveWarehouseB trw = new ToReceiveWarehouseB();
	            		startDateTime = sapDateTime[2];
	            		trw.setCloseDate(sapDateTime[2]);
	            		trw.setSAPQualify("否");
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
			            	trw.setAegisQualify("否");
			            	endDateTime = rsA.getString("localtime").substring(0,16);
			            	trw.setReceivingLocation(rsA.getString("StockLocation"));//收货库位
			            }else {
			            	trw.setUID(temp[3].substring(0,21));
			            	trw.setReturnWarehouseTime("");
			            	trw.setAegisQualify("否");
			            	endDateTime = nowDayTime;
			            	pstmtA3.setString(1, trw.getUID());
			            	ResultSet rsA3 = pstmtA3.executeQuery();
			            	if(rsA3.next()) {
			            		trw.setReceivingLocation(rsA3.getString("StockLocation"));//收货库位
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
								if(day==0) {  // 同一天
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
		        trWarehouseBService.saveToReceiveWarehouseB(list);   
			}
	        //
	        vpsDB.close();
	        aegisDB.close();
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
	
	
}