package ht.task;

import ht.biz.IUrgentMaterialCheckNotOCRService;
import ht.dao.INotFinishSODao;
import ht.entity.NotFinishSO;
import ht.entity.UrgentMaterialCheckNotOCR;
import ht.util.*;

import java.sql.Connection;
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
 *
 */

public class CopyOfAutoUrgentMaterialCheckNotOCR {
	@Autowired
    private IUrgentMaterialCheckNotOCRService umCheckNotOCRService;
	
	public void execute() throws Exception {
		
		System.out.println("start...AutoUrgentMaterialCheckNotOCR "+new Date());
		
		try {
			ConMes conMes = new ConMes();
			ConVPS vpsDB = new ConVPS();
	        ConDashBoard grnewdbDB = new ConDashBoard();
	        //
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        String nowDay = df.format(c.getTime());
	        String nowDayTime = df2.format(c.getTime());
	        c.add(Calendar.DATE, -30); // -30 天
	        String nowDay_7 = df.format(c.getTime());
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
	        //umCheckNotOCRService.deleteAllUrgentMaterialCheckNotOCR();
	        //vendorrid
	        ResultSet rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from vendorrid " +
	        		" where GRNDATE between '"+nowDay_7+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000')  and ( " +
	                " partNumber not like '009%' and partNumber not like '020%' and partNumber not like  '021%' and partNumber not like '023%' and " +
	                " partNumber not like '035%' and partNumber not like '040%' and partNumber not like  '050%' and partNumber not like '051%' and " +
	                " partNumber not like '059%' and partNumber not like '110%' and partNumber not like  '121%' and partNumber not like '123%' and " +
	                " partNumber not like '129%' and partNumber not like '140%' and partNumber not like  '147%' and partNumber not like '155%' and " +
	                " partNumber not like '233%' and partNumber not like '237%' and partNumber not like  '262%' and partNumber not like '265%' and " +
	                " partNumber not like '275%' and partNumber not like '470%' " +
	                " )");
	        Map<String,String[]> rsMap=new HashMap<String,String[]>();
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
	        System.out.println("AutoUrgentMaterialCheckNotOCR vendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = conMes.executeQuery(" select top 1 FRB.Name, II.Identifier, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " where II.Identifier in ("+temp[3]+") and (FRB.Name like '%QM%' or  FRB.Name like '%IQ%') " +
	            		" order by FRB.Name asc, IIH.TimePosted_BaseDateTimeUTC desc ");
	            if(rsA.next()){
	                UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
	                umcn.setGRN(key.substring(0,10));
	                umcn.setItemNumber(temp[0]);
	                ResultSet rsC = conMes.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
	                        "where ReceivingNumber='"+key.substring(0,10)+"'");
	                if (rsC.next()) {
	                    umcn.setRDFinishTime(rsC.getString("CreateDate").substring(0,16));//收货时间
	                }else {
	                    umcn.setRDFinishTime("");
	                }
	                //System.out.println(temp[3]+","+rsA.getString("Name"));
	                if(rsA.getString("Name").contains("QM")) {
	                	ResultSet rsD = conMes.executeQuery("select II.Identifier, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime', SL.Identifier " +
	                			" from ItemInventories II" +
	                			" left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID" +
	                			" left join StockLocations SL on SL.ID = IIH.StockLocationID" +
	                			" where II.Identifier in ("+temp[3]+") and SL.Identifier like '%IQ%' " +
	                			" order by IIH.TimePosted_BaseDateTimeUTC desc ");
	                    if (rsD.next()) {  //如果同一个UID，历史记录有IQ，表示归还了
	                    	if(rsA.getString("Identifier").equals(rsD.getString("Identifier"))){
	                    		//System.out.println("vendorrid GRN:"+umcn.getGRN());
	                    		//System.out.println(temp[3]+","+rsA.getString("Name"));
	                    	}else {
	                    		umcn.setIQCGetTime(rsD.getString("localtime").substring(0,16));//IQC取样时间
	                        	//
	                            umcn.setReceivingLocation(rsD.getString("StockLocation"));//收货库位

								ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

								/*
									ResultSet rsB = conMes.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
										" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
								*/

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
	                    }else {
	                        umcn.setIQCGetTime("");
	                        //
	                        umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位

							ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

							/*
								ResultSet rsB = conMes.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
									" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
							*/

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
	                }else if(rsA.getString("Name").contains("IQ")) {
	                	umcn.setIQCGetTime(rsA.getString("localtime").substring(0,16));//IQC取样时间
	                	//
	                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位

						ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

						/*
							ResultSet rsB = conMes.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
								" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
						*/

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
	        rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from pcbvendorrid " +
	        		" where GRNDATE between '"+nowDay_7+"' and '"+nowDay+"' and UpAegis='pass'  and plent in ('1100','1200','5000')  and ( " +
	                " partNumber not like '009%' and partNumber not like '020%' and partNumber not like  '021%' and partNumber not like '023%' and " +
	                " partNumber not like '035%' and partNumber not like '040%' and partNumber not like  '050%' and partNumber not like '051%' and " +
	                " partNumber not like '059%' and partNumber not like '110%' and partNumber not like  '121%' and partNumber not like '123%' and " +
	                " partNumber not like '129%' and partNumber not like '140%' and partNumber not like  '147%' and partNumber not like '155%' and " +
	                " partNumber not like '233%' and partNumber not like '237%' and partNumber not like  '262%' and partNumber not like '265%' and " +
	                " partNumber not like '275%' and partNumber not like '470%' " +
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
	        System.out.println("AutoUrgentMaterialCheckNotOCR pcbvendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = conMes.executeQuery(" select top 1 FRB.Name, II.Identifier, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " where II.Identifier in ("+temp[3]+") and (FRB.Name like '%QM%' or  FRB.Name like '%IQ%') " +
	            		" order by FRB.Name asc, IIH.TimePosted_BaseDateTimeUTC desc ");
	            if(rsA.next()){
	                UrgentMaterialCheckNotOCR umcn = new UrgentMaterialCheckNotOCR();
	                umcn.setGRN(key.substring(0,10));
	                umcn.setItemNumber(temp[0]);
	                ResultSet rsC = conMes.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
	                        "where ReceivingNumber='"+key.substring(0,10)+"'");
	                if (rsC.next()) {
	                    umcn.setRDFinishTime(rsC.getString("CreateDate").substring(0,16));//收货时间
	                }else {
	                    umcn.setRDFinishTime("");
	                }
	                //System.out.println(temp[3]+","+rsA.getString("Name"));
	                if(rsA.getString("Name").contains("QM")) {
	                	ResultSet rsD = conMes.executeQuery("select II.Identifier, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime', SL.Identifier " +
	                			" from ItemInventories II" +
	                			" left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID" +
	                			" left join StockLocations SL on SL.ID = IIH.StockLocationID" +
	                			" where II.Identifier in ("+temp[3]+") and SL.Identifier like '%IQ%' " +
	                			" order by IIH.TimePosted_BaseDateTimeUTC desc ");
	                    if (rsD.next()) {  //如果同一个UID，历史记录有IQ，表示归还了
	                    	if(rsA.getString("Identifier").equals(rsD.getString("Identifier"))){
	                    		//System.out.println("pcbvendorrid GRN:"+umcn.getGRN());
	                    		//System.out.println(temp[3]+","+rsA.getString("Name"));
	                    	}else {
	                    		umcn.setIQCGetTime(rsD.getString("localtime").substring(0,16));//IQC取样时间
	                        	//
	                            umcn.setReceivingLocation(rsD.getString("StockLocation"));//收货库位

								ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

								/*
									ResultSet rsB = conMes.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
										" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
								*/

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
	                    }else {
	                        umcn.setIQCGetTime("");
	                        //
	                        umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位

							ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

							/*
								ResultSet rsB = conMes.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
									" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
							*/

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
	                }else if(rsA.getString("Name").contains("IQ")) {
	                	umcn.setIQCGetTime(rsA.getString("localtime").substring(0,16));//IQC取样时间
	                	//
	                    umcn.setReceivingLocation(rsA.getString("StockLocation"));//收货库位

						ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

						/*
							ResultSet rsB = conMes.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
								" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
						*/

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
	        //
	        vpsDB.close();
	        conMes.close();
	        grnewdbDB.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end...AutoUrgentMaterialCheckNotOCR "+new Date());
	}
}