package ht.task;

import ht.biz.IUrgentMaterialCheckOCRService;
import ht.dao.INotFinishSODao;
import ht.entity.NotFinishSO;
import ht.entity.UrgentMaterialCheckOCR;
import ht.util.ConAegis;
import ht.util.ConDashBoard;
import ht.util.ConOCR;
import ht.util.ConVPS;
import ht.util.DateUtils;
import ht.util.SAPService;

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

public class CopyOfAutoUrgentMaterialCheckOCR {
    @Autowired
    private IUrgentMaterialCheckOCRService umCheckOCRService;
	
	public void execute() throws Exception {
		System.out.println("start...AutoUrgentMaterialCheckOCR "+new Date());
		
		try {
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
	        //umCheckOCRService.deleteAllUrgentMaterialCheckOCR();
	        //vendorrid
	        ResultSet rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from vendorrid " +
	                " where GRNDATE between '"+nowDay_7+"' and '"+nowDay+"' and UpAegis='pass'  and plent in ('1100','1200','5000') and ( " +
	                " partNumber like '009%' or partNumber like '020%' or partNumber like  '021%' or partNumber like '023%' or " +
	                " partNumber like '035%' or partNumber like '040%' or partNumber like  '050%' or partNumber like '051%' or " +
	                " partNumber like '059%' or partNumber like '110%' or partNumber like  '121%' or partNumber like '123%' or " +
	                " partNumber like '129%' or partNumber like '140%' or partNumber like  '147%' or partNumber like '155%' or " +
	                " partNumber like '233%' or partNumber like '237%' or partNumber like  '262%' or partNumber like '265%' or " +
	                " partNumber like '275%' or partNumber like '470%' " +
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
	        System.out.println("AutoUrgentMaterialCheckOCR vendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " where II.Identifier in ("+temp[3]+") and FRB.Name like '%QM%' " +
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
	        		" where GRNDATE between '"+nowDay_7+"' and '"+nowDay+"' and UpAegis='pass'  and plent in ('1100','1200','5000') and ( " +
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
	        System.out.println("AutoUrgentMaterialCheckOCR pcbvendorrid size:"+rsMap.size());
	        for(String key : rsMap.keySet()) {
	            String[] temp = rsMap.get(key);
	            ResultSet rsA = aegisDB.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
	            		" from ItemInventories II " +
	                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
	                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
	                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
	                    " where II.Identifier in ("+temp[3]+") and FRB.Name like '%QM%' " +
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
			//
	        vpsDB.close();
	        aegisDB.close();
	        grnewdbDB.close();
	        ocrDB.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end...AutoUrgentMaterialCheckOCR "+new Date());
	}
}