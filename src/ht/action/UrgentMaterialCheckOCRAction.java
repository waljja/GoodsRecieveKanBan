package ht.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import ht.biz.IUrgentMaterialCheckOCRService;
import ht.entity.UrgentMaterialCheckOCR;
import ht.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 *
 * 急料检验看板（OCR）
 *
 */
@Controller("UrgentMaterialCheckOCRAction")
@SuppressWarnings("unchecked")
public class UrgentMaterialCheckOCRAction extends ActionSupport{
    @Autowired
    private IUrgentMaterialCheckOCRService umCheckOCRService;
    
    public String doList()throws Exception {
        List<UrgentMaterialCheckOCR> list = umCheckOCRService.findAllUrgentMaterialCheckOCR();
        if (list!=null&&list.size()>0) {
            Map request = (Map) ActionContext.getContext().get("request");
            request.put("urgentMaterialCheckOCRList", list); 
        }
        getTotalSummary();
        list = null;
        return "tolist";
    }
    
    public void getTotalSummary() throws Exception {
    	Map request = (Map) ActionContext.getContext().get("request");
    	ConDashBoard grnewdbDB = new ConDashBoard();
    	ResultSet rs = grnewdbDB.executeQuery("select count(*) a1 from ToReceiveCheck where Type='A' and Sequence = (select max(Sequence) from ToReceiveCheck) and closeDate IS  NULL" +
		" and ItemNumber in ( select a.bom from NotFinishSO a left join ( select  plant,bom,sum(convert(float,needQty)) qty from NotFinishSO group by bom,plant) b on a.bom=b.bom and b.plant=a.plant where b.qty> convert(float,REPLACE(inventory,'null',0.0))) ");
		if(rs.next()){
			request.put("a1", rs.getInt("a1"));
		}
		rs = grnewdbDB.executeQuery("select count(*) b1 from ToReceiveCheck where Type='B' and Sequence = (select max(Sequence) from ToReceiveCheck) and closeDate IS  NULL" +
				" and ItemNumber in ( select a.bom from NotFinishSO a left join ( select  plant,bom,sum(convert(float,needQty)) qty from NotFinishSO group by bom,plant) b on a.bom=b.bom and b.plant=a.plant where b.qty> convert(float,REPLACE(inventory,'null',0.0))) ");
		if(rs.next()){
			request.put("b1", rs.getInt("b1")); 
		}
    	//
    	rs = grnewdbDB.executeQuery("select count(*) a2 from UrgentMaterialCheckNotOCR where Type='A' and Sequence = (select max(Sequence) from UrgentMaterialCheckNotOCR) and closeDate is null ");
    	if(rs.next()){
    		request.put("a2", rs.getInt("a2"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b2 from UrgentMaterialCheckNotOCR where Type='B' and Sequence = (select max(Sequence) from UrgentMaterialCheckNotOCR) and closeDate is null  ");
    	if(rs.next()){
    		request.put("b2", rs.getInt("b2")); 
    	}
    	//
    	rs = grnewdbDB.executeQuery("select count(*) a3 from UrgentMaterialCheckOCR where Type='A' and Sequence = (select max(Sequence) from UrgentMaterialCheckOCR) and closeDate is null ");
    	if(rs.next()){
    		request.put("a3", rs.getInt("a3"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b3 from UrgentMaterialCheckOCR where Type='B' and Sequence = (select max(Sequence) from UrgentMaterialCheckOCR) and closeDate is null  ");
    	if(rs.next()){
    		request.put("b3", rs.getInt("b3"));
    	}
    	//
    	rs = grnewdbDB.executeQuery("select count(*) a4 from ToReceiveWarehouse where Type='A' and Sequence = (select max(Sequence) from ToReceiveWarehouse) and ReturnWarehouseTime =''  ");
    	if(rs.next()){
    		request.put("a4", rs.getInt("a4"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b4 from ToReceiveWarehouse where Type='B' and Sequence = (select max(Sequence) from ToReceiveWarehouse) and ReturnWarehouseTime ='' ");
    	if(rs.next()){
    		request.put("b4", rs.getInt("b4")); 
    	}
    	//
    	rs = grnewdbDB.executeQuery("select count(*) a5 from ToReceiveWarehouseB where Type='A' and Sequence = (select max(Sequence) from ToReceiveWarehouseB) and ReturnWarehouseTime =''  ");
    	if(rs.next()){
    		request.put("a5", rs.getInt("a5"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b5 from ToReceiveWarehouseB where Type='B' and Sequence = (select max(Sequence) from ToReceiveWarehouseB) and ReturnWarehouseTime =''  ");
    	if(rs.next()){
    		request.put("b5", rs.getInt("b5"));
    	}
    	//
    	grnewdbDB.close();
    }
    
    public void doSaveRecords() throws Exception {
        ConMes conMes = new ConMes();
        ConVPS vpsDB = new ConVPS();
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
        //umCheckOCRService.deleteAllUrgentMaterialCheckOCR();
        ArrayList<UrgentMaterialCheckOCR> list = new ArrayList<UrgentMaterialCheckOCR>();
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
        //System.out.println("vendorrid size:"+rsMap.size());
        for(String key : rsMap.keySet()) {
            String[] temp = rsMap.get(key);
            // 131 DB modified by GuoZhao Ding
            ResultSet rsA = conMes.executeQuery(SqlStatements.findStockInfo(temp[3]));

           /*
            ResultSet rsA = conMes.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
            		" from ItemInventories II " +
                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
                    " where II.Identifier in ("+temp[3]+") and FRB.Name like '%QM%' " +
                    " order by IIH.TimePosted_BaseDateTimeUTC desc ");
            */

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

                    // 131 DB modified by GuoZhao Ding（暂时不使用xTend_MaterialReceived表，因为该表数据来源于VPS）
                    ResultSet rsD = vpsDB.executeQuery(SqlStatements.findGRDate_v(key.substring(0,10))); // 先查vendorrid表

                    if (!rsD.next()) { // 如果vendorrid表查不到，查pcbvendorrid表
                        rsD = vpsDB.executeQuery(SqlStatements.findGRDate_PV(key.substring(0,10)));
                    }
                    // 131 DB modified by GuoZhao Ding（暂时不使用xTend_MaterialReceived表，因为该表数据来源于VPS）

                    /*
                        ResultSet rsD = conMes.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
                            "where ReceivingNumber='"+key.substring(0,10)+"'");
                    */

                    if (rsD.next()) {
                        umc.setRDFinishTime(rsD.getString("CreateDate").substring(0,16));//收货时间
                    }else {
                        umc.setRDFinishTime("");
                    }

                    ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

					/*
						ResultSet rsB = conMes.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
							" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
					*/

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
                    if(!"NA".equals(umc.getProductionTime())) {
                    	list.add(umc);
    				}
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
        for(String key : rsMap.keySet()) {
            String[] temp = rsMap.get(key);
            // 131 DB modified by GuoZhao Ding
            ResultSet rsA = conMes.executeQuery(SqlStatements.findStockInfo(temp[3]));

            /*
            ResultSet rsA = conMes.executeQuery(" select top 1 FRB.Name, II.StockLocation, DATEADD(HOUR,8,IIH.TimePosted_BaseDateTimeUTC) AS 'localtime' " +
            		" from ItemInventories II " +
                    " left join ItemTypes IT on IT.ID = II.ItemTypeID " +
                    " left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
                    " left join ItemInventoryHistories IIH on IIH.ItemInventoryID = II.ID " +
                    " where II.Identifier in ("+temp[3]+") and FRB.Name like '%QM%' " +
                    " order by IIH.TimePosted_BaseDateTimeUTC desc");
            */

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

                    // 131 DB modified by GuoZhao Ding（暂时不使用xTend_MaterialReceived表，因为该表数据来源于VPS）
                    ResultSet rsD = vpsDB.executeQuery(SqlStatements.findGRDate_v(key.substring(0,10))); // 先查vendorrid表

                    if (!rsD.next()) { // 如果vendorrid表查不到，查pcbvendorrid表
                        rsD = vpsDB.executeQuery(SqlStatements.findGRDate_PV(key.substring(0,10)));
                    }
                    // 131 DB modified by GuoZhao Ding（暂时不使用xTend_MaterialReceived表，因为该表数据来源于VPS）

                    /*
                        ResultSet rsD = conMes.executeQuery("SELECT CreateDate FROM [HT_FactoryLogix].[dbo].[xTend_MaterialReceived] " +
                            "where ReceivingNumber='"+key.substring(0,10)+"'");
                    */

                    if (rsD.next()) {
                        umc.setRDFinishTime(rsD.getString("CreateDate").substring(0,16));//收货时间
                    }else {
                        umc.setRDFinishTime("");
                    }

                    ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

					/*
						ResultSet rsB = conMes.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
							" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
					*/

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
                    if(!"NA".equals(umc.getProductionTime())) {
                    	list.add(umc);
      				}
            	}
            }
        }
        //排序
        ListSort(list);
        Map request = (Map) ActionContext.getContext().get("request");
        request.put("urgentMaterialCheckOCRList", list);
        
        vpsDB.close();
        conMes.close();
        grnewdbDB.close();
        ocrDB.close();
    }
    
    public void ListSort(List<UrgentMaterialCheckOCR> list) { 
        Collections.sort(list,new Comparator<UrgentMaterialCheckOCR>(){          
            public int compare(UrgentMaterialCheckOCR o1,
                    UrgentMaterialCheckOCR o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if ("NA".equals(o1.getProductionTime())) {
                        o1.setProductionTime("9999-12-31");
                    }
                    if ("NA".equals(o2.getProductionTime())) {
                        o2.setProductionTime("9999-12-31");
                    }
                    Date dt1 = format.parse(o1.getProductionTime());
                    Date dt2 = format.parse(o2.getProductionTime()); 
                    if (dt1.getTime()>dt2.getTime()) {
                        return 1;
                    }else if(dt1.getTime()<dt2.getTime()){
                        return -1;
                    }else if(dt1.getTime()==dt2.getTime()){
                        return 0;
                    }                        
                } catch (Exception e) {
                    // TODO: handle exception
                }                
                return 0;
            }
        }); 
    }

}
