package ht.util;

import ht.entity.NotFinishSO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;


public class SAPService {
	static String ABAP_AS = "ABAP_AS_WITHOUT_POOL_LEDLIGHT";
	static String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL_LEDLIGHT";
	static String ABAP_MS = "ABAP_MS_WITHOUT_POOL_LEDLIGHT";

	public SAPService() {
	  InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sap.properties");   
  	  Properties p = new Properties();   
  	  try {   
  	   p.load(inputStream);   
  	  } catch (IOException e1) {   
  	   e1.printStackTrace();   
  	  }   
	
		try {
			Properties connectProperties = new Properties();
			connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST,p.getProperty("JCO_ASHOST"));
			connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,p.getProperty("JCO_SYSNR"));
			//connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,"01");//测试机
			connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT,p.getProperty("JCO_CLIENT"));
			connectProperties.setProperty(DestinationDataProvider.JCO_USER,p.getProperty("JCO_USER"));
			connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD,p.getProperty("JCO_PASSWD"));
			connectProperties.setProperty(DestinationDataProvider.JCO_LANG,"en");
			createDataFile(ABAP_AS, "jcoDestination", connectProperties);

			connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, p.getProperty("JCO_POOL_CAPACITY"));
	        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,    p.getProperty("JCO_PEAK_LIMIT"));
			createDataFile(ABAP_AS_POOLED, "jcoDestination", connectProperties);

		} catch (Exception e) {
			System.out.print("取得SAP链接有问题:"+e.getMessage());
			e.printStackTrace();
		}
			

	}

	private void createDataFile(String name, String suffix,
			Properties properties) {
		File cfg = new File(name + "." + suffix);
		if (!cfg.exists()) {
			try {
				FileOutputStream fos = new FileOutputStream(cfg, false);
				properties.store(fos, "for tests only !");
				fos.close();
			} catch (Exception e) {
				throw new RuntimeException(
						"Unable to create the destination file "
								+ cfg.getName(), e);
			}
		}
	}
	
    
	//so list
    public List<NotFinishSO> getMsdList(String startDate, String endDate) {
    	ConDashBoard grnewdbDB = new ConDashBoard();
    	List<NotFinishSO> msdList = new ArrayList<NotFinishSO>();
		try {
			// 获取连接池
			JCoDestination destination = JCoDestinationManager
					.getDestination(ABAP_AS_POOLED);
			// 获取功能函数
			JCoFunction function = destination.getRepository().getFunction("Z_JAVA_GET_WO_STOCK");
			if (function == null) {
				throw new RuntimeException("Z_JAVA_GET_WO_STOCK not found in SAP.");
			}
			// 给功能函数输入参数
			JCoParameterList input = function.getImportParameterList();
			input.setValue("I_FROMGSTRP", startDate);
			input.setValue("I_TOGSTRP", endDate);
			try {
				function.execute(destination); // 函数执行
			} catch (AbapException e) {
				System.out.println(e.toString());
				return null;
			}
			//直接返回值取值
			//工单与成品
			JCoTable rs1 = function.getTableParameterList().getTable("ET_AFKO");
			Map<String,String[]> mapSo=new HashMap<String,String[]>(); 
			for(int i=0; i<rs1.getNumRows(); i++) {
				String[] dou=new String[2];
				String so = rs1.getString("AUFNR");
				if(!mapSo.containsKey(so)){
					dou[0]=rs1.getString("PLNBEZ");
					dou[1]=rs1.getString("GSTRP");
					mapSo.put(so, dou);
				}
				rs1.nextRow();
			}
			//工厂 物料 - 库存
			JCoTable rs2 = function.getTableParameterList().getTable("ET_MARD");
			Map<String,String> mapStock=new HashMap<String,String>(); 
			for(int i=0; i<rs2.getNumRows(); i++) {
				String dou=new String();
				String plant = rs2.getString("WERKS");
				String pn = rs2.getString("MATNR");
				String stock = rs2.getString("LGORT");
				String a=plant+pn;
				double b=0.0;
				double c=0.0;
				if(a.equals("1100522-0000072-00R1")){
					c = rs2.getString("LABST")!=null&&!"null".equals(rs2.getString("LABST"))?Double.parseDouble(rs2.getString("LABST")):0.0;
					b+= c;
					System.out.println(stock+" | "+b);
				}
//				ResultSet rs = grnewdbDB.executeQuery("select plant, stock from NotFinishStock where plant='"+plant+"' ");
//				if(rs.next()) {
				
					if(!mapStock.containsKey(plant+pn)){
						dou=rs2.getString("LABST");
						if("".equals(dou)) {
							dou="0";
						}
						mapStock.put(plant+pn, dou);
					}else{
						String oldValue = mapStock.get(plant+pn);
						dou=rs2.getString("LABST");
						if("".equals(dou)) {
							dou="0";
						}
						mapStock.put(plant+pn, String.valueOf(Double.parseDouble(oldValue) + Double.parseDouble(dou)));
					}	
				
//				}
				rs2.nextRow();
			}
			//BOM 明细
			JCoTable rs = function.getTableParameterList().getTable("ET_RESB");
			for(int i=0; i<rs.getNumRows(); i++) {
				NotFinishSO nfso = new NotFinishSO();
				String so = rs.getString("AUFNR");
				String plant = rs.getString("WERKS");
				String bom = rs.getString("MATNR");
				String stock = rs.getString("LGORT");
				nfso.setSo(so);
				nfso.setPlant(plant);
				nfso.setBom(bom);
				nfso.setStock(stock);
				nfso.setNeedQty(rs.getString("BDMNG"));
				nfso.setGotQty(rs.getString("ENMNG"));
				if(mapSo.containsKey(so)) {
					String[] temp = mapSo.get(so);
					nfso.setPn(temp[0]);
					nfso.setSoStartDate(temp[1]);
				}
				//System.out.println(plant+bom);
				if(mapStock.containsKey(plant+bom)) {
					String temp = mapStock.get(plant+bom);
					nfso.setInventory(temp==null||"".equals(temp)?"0.0":temp);
				}
				msdList.add(nfso);
				rs.nextRow();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		grnewdbDB.close();
		return msdList;
	}
    
    
     //获取GRN 是否做过  321 或者 122
	public String[] getGrnStatus(String GRN, String year){
		String[] rtc = new String[3];
		rtc[0] = "";
		rtc[1] = "";
		rtc[2] = "";
		try {
			// 获取连接池
			JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
			// 获取功能函数
			JCoFunction function = destination.getRepository().getFunction("Z_JAVA_GET_GRN_STATUS");
			if (function == null)
				throw new RuntimeException("Z_JAVA_GET_GRN_STATUS not found in SAP.");
			
			JCoParameterList input = function.getImportParameterList();
			input.setValue("I_MBLNR", GRN);   //
			input.setValue("I_MJAHR", year);   //
			try {
				function.execute(destination); // 函数执行
			} catch (AbapException e) {
				e.printStackTrace();
				System.out.println(e.toString());
			}
			
			JCoParameterList exports = function.getExportParameterList();
     	    String r321 = exports.getString("E_MV321");
     	    String r122 = exports.getString("E_MV122");
     	    if("X".equalsIgnoreCase(r321) || "X".equalsIgnoreCase(r122)) {
     	    	rtc[0] = r321;
     	    	rtc[1] = r122;
     	    	rtc[2] = exports.getString("E_CPUDT").substring(0, 10) + " " 
     	    		+ exports.getString("E_CUPTM").substring(0, 5) ;
     	    	//System.out.println(GRN+","+exports.getString("E_CPUDT").substring(0, 10)+","+exports.getString("E_CUPTM").substring(0, 5) );
     	    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtc;
	}
    
    
	public static void main(String[] args) {
		//SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		SAPService sap=new SAPService();
		//System.out.println(sap.getPlantByPo("4500318530"));
		//List<NotFinishSO> msdList = sap.getMsdList("20220419", "20220619");
		sap.getGrnStatus("5003311598", "2022");
		sap.getGrnStatus("5003311599", "2022");

	}
}
