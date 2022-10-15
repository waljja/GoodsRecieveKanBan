package ht.task;

import ht.biz.IToReceiveWarehouseService;
import ht.dao.INotFinishSODao;
import ht.entity.NotFinishSO;
import ht.entity.ToReceiveWarehouse;
import ht.util.ConAegis;
import ht.util.ConDashBoard;
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
 */

public class CopyOfAutoToReceiveWarehouse {
	@Autowired
    private IToReceiveWarehouseService trWarehouseService;
	
	public void execute() throws Exception {
		System.out.println("start...AutoToReceiveWarehouse "+new Date());
		
		try {
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
	        //trWarehouseService.deleteAllToReceiveWarehouse();
	        //vendorrid
	        ResultSet rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent from vendorrid " +
	                " where GRNDATE between '"+nowDay_7+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000')  ");
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
	        System.out.println("AutoToReceiveWarehouse vendorrid size:"+rsMap.size());
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
	                    " where II.Identifier in ("+temp[3]+") and FRB.Name like '%QM%' and PMD.MeasurementType ='321'" +
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
	                " where GRNDATE between '"+nowDay_7+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000') ");
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
	        System.out.println("AutoToReceiveWarehouse pcbvendorrid size:"+rsMap.size());
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
	                    " where II.Identifier in ("+temp[3]+") and FRB.Name like '%QM%' and PMD.MeasurementType ='321'" +
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
	        //
	        vpsDB.close();
	        aegisDB.close();
	        grnewdbDB.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end...AutoToReceiveWarehouse "+new Date());
	}
}