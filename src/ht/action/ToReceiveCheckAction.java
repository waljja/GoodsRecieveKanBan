package ht.action;

import ht.biz.IToReceiveCheckService;
import ht.entity.ToReceiveCheck;
import ht.util.*;

import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import static ht.util.SqlStatements.findStock;

/**
 *
 * 待点收看板
 *
 */
@Controller("ToReceiveCheckAction")
@SuppressWarnings("unchecked")
public class ToReceiveCheckAction extends ActionSupport{
    @Autowired
    private IToReceiveCheckService trCheckService;
    
    private InputStream downloadFile;
    
    
    public String doList()throws Exception {
    	//doSaveRecords();
    	//System.out.println("ToReceiveCheckAction refresh time: "+ new Date());
        List<ToReceiveCheck> list = trCheckService.findAllToReceiveCheck();    
        if (list!=null&&list.size()>0) {
            Map request = (Map) ActionContext.getContext().get("request");
            request.put("toReceiveCheckList", list); 
        }
        getTotalSummary();
        list = null;
        return "tolist";
    }
    
    public String doExport() throws Exception {
        downloadFile = trCheckService.doExport();                  
        return "doExport";
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
    	rs = grnewdbDB.executeQuery("select count(*) b2 from UrgentMaterialCheckNotOCR where Type='B' and Sequence = (select max(Sequence) from UrgentMaterialCheckNotOCR) and closeDate is null ");
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

    // 统计类别
    public String reloadIndex() throws Exception {
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
    	rs = grnewdbDB.executeQuery("select count(*) a2 from UrgentMaterialCheckNotOCR where Type='A' and Sequence = (select max(Sequence) from UrgentMaterialCheckNotOCR) and closeDate is null  ");
    	if(rs.next()){
    		request.put("a2", rs.getInt("a2"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b2 from UrgentMaterialCheckNotOCR where Type='B' and Sequence = (select max(Sequence) from UrgentMaterialCheckNotOCR) and closeDate is null   ");
    	if(rs.next()){
    		request.put("b2", rs.getInt("b2")); 
    	}
    	//
    	rs = grnewdbDB.executeQuery("select count(*) a3 from UrgentMaterialCheckOCR where Type='A' and Sequence = (select max(Sequence) from UrgentMaterialCheckOCR)  and closeDate is null  ");
    	if(rs.next()){
    		request.put("a3", rs.getInt("a3"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b3 from UrgentMaterialCheckOCR where Type='B' and Sequence = (select max(Sequence) from UrgentMaterialCheckOCR)  and closeDate is null  ");
    	if(rs.next()){
    		request.put("b3", rs.getInt("b3"));
    	}
    	//
    	rs = grnewdbDB.executeQuery("select count(*) a4 from ToReceiveWarehouse where Type='A' and Sequence = (select max(Sequence) from ToReceiveWarehouse) and ReturnWarehouseTime ='' ");
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

    	return "toIndex";
    }
    
    public void doSaveRecords() throws Exception {
    	ConMes conMes = new ConMes();
    	ConVPS vpsDB = new ConVPS();
    	ConDashBoard grnewdbDB = new ConDashBoard();
    	//f
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		String nowDay = df.format(c.getTime());
		String nowDayTime = df2.format(c.getTime());
		c.add(Calendar.DATE, -2); // -2 天
		String nowDay_7 = df.format(c.getTime());
		//trCheckService.deleteAllToReceiveCheck();
		ArrayList<ToReceiveCheck> list = new ArrayList<ToReceiveCheck>();
		//vendorrid
		ResultSet rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103 from vendorrid " +
				" where GRNDATE between '"+nowDay_7+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000') ");
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

		for(String key : rsMap.keySet()) { 
			String[] temp = rsMap.get(key);
			ResultSet rsA = conMes.executeQuery(findStock(temp[3])); // 131 DB modified by GuoZhao Ding

			/*
				ResultSet rsA = aegisDB.executeQuery(" select FRB.Name as stockname from ItemInventories II " +
					" left join ItemTypes IT on IT.ID = II.ItemTypeID " +
					" left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
					" where II.Identifier = '"+temp[3]+"' and FRB.Name is not null and FRB.Name <> '' ");
			*/

			if(!rsA.next()){
				ToReceiveCheck trc = new ToReceiveCheck();
				trc.setGRN(key.substring(0, 10));
				trc.setItemNumber(temp[0]);
				trc.setGRNQuantity(temp[1]);
				trc.setUIDQuantity(temp[2]);

				ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

				/*
					ResultSet rsB = aegisDB.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
						" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
				*/

				if(rsB.next()) {
					trc.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
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
				if(!"NA".equals(trc.getProductionTime())) {
					list.add(trc);
				}
			} 
		}
		//pcbvendorrid
		rs = vpsDB.executeQuery("select grn, partNumber, printQTY, rid, plent, GRNDATE, GRN103 from pcbvendorrid " +
				" where GRNDATE between '"+nowDay_7+"' and '"+nowDay+"' and UpAegis='pass' and plent in ('1100','1200','5000') ");
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

		for(String key : rsMap.keySet()) {
			String[] temp = rsMap.get(key);

			ResultSet rsA = conMes.executeQuery(findStock(temp[3])); // 131 DB Modified by GuoZhao Ding

			/*
				ResultSet rsA = conMes.executeQuery(" select FRB.Name from ItemInventories II " +
					" left join ItemTypes IT on IT.ID = II.ItemTypeID " +
					" left join FactoryResourceBases FRB on FRB.ID = II.StockResourceID " +
					" where II.Identifier = '"+temp[3]+"' and FRB.Name is not null and FRB.Name <> '' ");
			*/

			if(!rsA.next()){
				ToReceiveCheck trc = new ToReceiveCheck();
				trc.setGRN(key.substring(0, 10));
				trc.setItemNumber(temp[0]);
				trc.setGRNQuantity(temp[1]);
				trc.setUIDQuantity(temp[2]);

				ResultSet rsB = conMes.executeQuery(SqlStatements.findEarliestReqTime(temp[0], nowDay)); // 131 DB modified by GuoZhao Ding

				/*
					ResultSet rsB = conMes.executeQuery(" select top 1 RequireTime from [HT_InterfaceExchange].[dbo].[xTend_MissingMaterials] " +
						" where PartNumber = '"+temp[0]+"' and convert(varchar(10),RequireTime,23) ='"+nowDay+"' order by RequireTime ");
				*/

				if(rsB.next()) {
					trc.setProductionTime(rsB.getString("RequireTime").substring(0, 16));
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
				if(!"NA".equals(trc.getProductionTime())) {
					list.add(trc);
				}
			}
		}
		//排序
		ListSort(list);
		Map request = (Map) ActionContext.getContext().get("request");
        request.put("toReceiveCheckList", list);
		//
		vpsDB.close();
		conMes.close();
		grnewdbDB.close();
    }
    
    public void ListSort(List<ToReceiveCheck> list) { 
        Collections.sort(list,new Comparator<ToReceiveCheck>(){          
            public int compare(ToReceiveCheck o1,
                    ToReceiveCheck o2) {
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

	public InputStream getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(InputStream downloadFile) {
		this.downloadFile = downloadFile;
	}
    
}
