package ht.task;

import ht.dao.IToReceiveCheckDao;
import ht.dao.IToReceiveWarehouseBDao;
import ht.dao.IToReceiveWarehouseDao;
import ht.dao.IUrgentMaterialCheckNotOCRDao;
import ht.dao.IUrgentMaterialCheckOCRDao;
import ht.entity.ToReceiveCheck;
import ht.entity.ToReceiveWarehouse;
import ht.entity.ToReceiveWarehouseB;
import ht.entity.UrgentMaterialCheckNotOCR;
import ht.entity.UrgentMaterialCheckOCR;
import ht.mapper.ToReceiveCheckMapper;
import ht.mapper.ToReceiveWarehouseBMapper;
import ht.mapper.ToReceiveWarehouseMapper;
import ht.mapper.UrgentMaterialCheckNotOCRMapper;
import ht.mapper.UrgentMaterialCheckOCRMapper;
import ht.util.ConAegis;
import ht.util.EmailcontrolByAdmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @date 2020-11-28
 * @author 刘惠明
 *
 */
public class AutoEmailDashBoardReport {
	private static Log commonsLog = LogFactory.getLog(AutoEmailDashBoardReport.class);
	
    @Autowired
    private IToReceiveCheckDao toReceiveCheckDao;
    @Autowired
    private IUrgentMaterialCheckNotOCRDao umCheckNotOCRDao;
    @Autowired
    private IUrgentMaterialCheckOCRDao umCheckOCRDao;
    @Autowired
    private IToReceiveWarehouseDao trWarehouseDao;
    @Autowired
    private IToReceiveWarehouseBDao trWarehouseBDao;
    
    
    //执行
    public void execute() throws Exception {
    	commonsLog.info("start...");
    	
        EmailcontrolByAdmin email = new EmailcontrolByAdmin();
		ArrayList attachment = new ArrayList();
		String doDate = generateExcel(attachment);
		String body = "您好：<br>" + "<br>" + "附件是《今天DashBoard Daily Report》，请参阅！<br>"
				+ "<br>";
		
		email.sendMailWidthAttach("messenger.system@honortone.com",
				"RickyKU@honortone.com;bonnie.kong@honortone.com;guangrui.zheng@honortone.com;" +
				"ZhengWei.LAI@honortone.com;Kenney.CHOY@honortone.com;Franki.CHAN@honortone.com;" +
				"xiaoying.hao@honortone.com;zhufurong@honortone.com;Ni.CAI@honortone.com;" +
				"liaobinchang@honortone.com;yilin.zhong@honortone.com;mugang.rao@honortone.com;", 
				
				"jiayu.yin@honortone.com;",
				
				"", "DashBoard Daily Report("+doDate+")测试版", body, attachment);
		

/*		email.sendMailWidthAttach("messenger.system@honortone.com",
				
				"bonnie.kong@honortone.com;", 
				
				"huiming.liu@honortone.com;",
				
				"", "DashBoard Daily Report("+doDate+")测试版", body, attachment);*/

		
		for (Object file : attachment) {
			String path = file.toString();
			new File(path).delete();
		}
        commonsLog.info("end...");
    }
    
    
    public String generateExcel(ArrayList list) throws Exception {
    	
    	ConAegis aegisDB = new ConAegis();
    	
        Calendar c = Calendar.getInstance();     
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        c.setTime(new Date());
        c.add(Calendar.DATE, -0);  // ???????????
        String todayDay = df.format(c.getTime());
        String month = todayDay.substring(5, 7);
        
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Daily report");
        sheet.setColumnWidth(0, 1000*2);
		sheet.setColumnWidth(1, 1000*4);
		sheet.setColumnWidth(2, 1000*4);
		sheet.setColumnWidth(3, 1000*4);
		sheet.setColumnWidth(4, 1000*3);
		sheet.setColumnWidth(5, 1000*3);
		
		
        HSSFRow row = null;
        HSSFCell cell = null;
        // 标题字体
        HSSFFont titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        titleFont.setFontName("新細明體");
        //
        HSSFFont titleFont2 = wb.createFont();
        titleFont2.setFontHeightInPoints((short) 12);
        titleFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
        titleFont2.setFontName("新細明體");
        // 内容字体
        HSSFFont commonFont = wb.createFont();
        commonFont.setFontHeightInPoints((short) 9);
        commonFont.setFontName("新細明體");
        // 表头样式
        HSSFCellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFont(titleFont);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框实线
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框实线
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 有边框实线
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框实线
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        headerStyle.setFillForegroundColor((short) 5);// 设置背景色
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);
        // 内容文字样式
        HSSFCellStyle textStyle = wb.createCellStyle();
        textStyle.setFont(commonFont);
        textStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框实线
        textStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框实线
        textStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 有边框实线
        textStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框实线
        textStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        textStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        // 内容文字样式
        HSSFCellStyle textStyle1= wb.createCellStyle();
        textStyle1.setFont(commonFont);
        textStyle1.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框实线
        textStyle1.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框实线
        textStyle1.setBorderRight(HSSFCellStyle.BORDER_THIN);// 有边框实线
        textStyle1.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框实线
        textStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
        // 内容数字样式
        HSSFCellStyle numberStyle = wb.createCellStyle();
        numberStyle.setFont(commonFont);
        numberStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 右边对齐
        
        //标题样式
        HSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(titleFont2);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        
        //1
        try{
        	//标题
            CellRangeAddress region=new CellRangeAddress(0, 0, 0, 5); 
            sheet.addMergedRegion(region);
            row = sheet.createRow(0);
            row.setHeight((short) 0x200);
            cell = row.createCell(0);
            cell.setCellStyle(titleStyle);
            cell.setCellValue("1. 待点收看板");
               
            row = sheet.createRow(1);
            String stringtitle[] = new String[6];
            stringtitle[0]="";
            stringtitle[1]="没有完成的数据";
            stringtitle[2]="达标";
            stringtitle[3]="不达标";
            stringtitle[4]="达标";
            stringtitle[5]="不达标";
            for (int i = 0; i < stringtitle.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringtitle[i]);
            }
            
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(2,2,2,3));
            row = sheet.createRow(2);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单四小时内完成");
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(2,2,4,5));
            cell = row.createCell(4);
            cell.setCellStyle(textStyle);
            cell.setCellValue("急单今天完成");
            
            //计算A
            List grnListA = toReceiveCheckDao.listData("SELECT GRN FROM ToReceiveCheck " +
            		" where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' group by GRN ");
            int a1 = 0; //达标
            int a2 = 0; //不达标
            int a1_2 = 0;
            int a2_2 = 0;
            int a3 = 0;
            if (grnListA!=null && grnListA.size()>0) { 
                for(Object grn : grnListA){
                    Map map = (Map)grn;  
                	List listA = toReceiveCheckDao.listData("SELECT TOP 1 * FROM ToReceiveCheck " +
                    		" WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' ORDER BY createdate desc");              
                    if (listA!=null&&listA.size()>0) {                   
                        Map item = (Map)listA.get(0);
                        Object closeDate = item.get("closeDate");
                        if(closeDate != null && !"null".equals(closeDate)) {
                        	Object waittime = item.get("WaitTime");
                        	Object grnDate = item.get("GRNDATE");
                            if (Double.parseDouble(waittime.toString()) <= 4.00) {
                                a1++;
                                if(todayDay.equals(grnDate.toString().substring(0, 10))) {
                                	a1_2++ ;
                                }
                            }else {
                                a2++;
                                if(todayDay.equals(grnDate.toString().substring(0, 10))) {
                                	a2_2++ ;
                                }
                            }
                        }else{
                        	a3++;
                        }
                    }
                    
                }
            }
            row = sheet.createRow(3);       
            String stringA[] = new String[6];
            stringA[0]="A";
            stringA[1]=String.valueOf(a3);
            stringA[2]=String.valueOf(a1);
            stringA[3]=String.valueOf(a2);
            stringA[4]=String.valueOf(a1_2);
            stringA[5]=String.valueOf(a2_2);
            for (int i = 0; i < stringA.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringA[i]);
            }
            
            //计算B
            List grnListB = toReceiveCheckDao.listData("SELECT GRN FROM ToReceiveCheck " +
            		"where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' group by GRN");
            int b1 = 0; //达标
            int b2 = 0; //不达标
            int b1_2 = 0;
            int b2_2 = 0;
            int b3 = 0;
            if (grnListB!=null && grnListB.size()>0) { 
                for(Object grn : grnListB){
                    Map map = (Map)grn; 
                	List listB = toReceiveCheckDao.listData("SELECT TOP 1 * FROM ToReceiveCheck " +
                    		" WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' ORDER BY createdate desc");              
                    if (listB!=null&&listB.size()>0) {
                        Map item = (Map)listB.get(0);
                        Object closeDate = item.get("closeDate");
                        if(closeDate != null && !"null".equals(closeDate)) {
                            Object waittime = item.get("WaitTime");
                            Object grnDate = item.get("GRNDATE");
                            if (Double.parseDouble(waittime.toString()) <= 4.00) {
                                b1++;
                                if(todayDay.equals(grnDate.toString().substring(0, 10))) {
                                	b1_2++ ;
                                }
                            }else {
                                b2++;
                                if(todayDay.equals(grnDate.toString().substring(0, 10))) {
                                	b2_2++ ;
                                }
                            }
                        }else {
                        	b3++;
                        }
                    }
                }
            }
            row = sheet.createRow(4);       
            String stringB[] = new String[6];
            stringB[0]="B";
            stringB[1]=String.valueOf(b3);
            stringB[2]=String.valueOf(b1);
            stringB[3]=String.valueOf(b2);
            stringB[4]=String.valueOf(b1_2);
            stringB[5]=String.valueOf(b2_2);
            for (int i = 0; i < stringB.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringB[i]);
            }
           
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(5,5,2,3));
            row = sheet.createRow(5);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单二十四小时内完成");
            
            //计算C
            List grnListC = toReceiveCheckDao.listData("SELECT GRN FROM ToReceiveCheck where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' group by GRN");
            int c1 = 0; //达标
            int c2 = 0; //不达标
            int c3 = 0;
            if (grnListC!=null && grnListC.size()>0) { 
                for(Object grn : grnListC){
                    Map map = (Map)grn;
                	 List listC = toReceiveCheckDao.listData("SELECT TOP 1 * FROM ToReceiveCheck WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' ORDER BY createdate desc");              
                     if (listC!=null&&listC.size()>0) {
                         Map item = (Map)listC.get(0);   
                         Object closeDate = item.get("closeDate");
                         if(closeDate != null && !"null".equals(closeDate)) {
                             Object waittime = item.get("WaitTime"); 
                             if (Double.parseDouble(waittime.toString()) <= 24.00) {
                                 c1++;
                             }else {
                                 c2++;
                             }            
                         }else {
                         	c3++;
                         }   
                    }
                }
            }
            
            row = sheet.createRow(6);       
            String stringC[] = new String[4];
            stringC[0]="C";
            stringC[1]=String.valueOf(c3);
            stringC[2]=String.valueOf(c1);
            stringC[3]=String.valueOf(c2);
            for (int i = 0; i < stringC.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringC[i]);
            }
            
            //计算其他
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(7,7,2,3));
            row = sheet.createRow(7);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单三十六小时内完成");
            
            List grnListD = toReceiveCheckDao.listData("SELECT GRN FROM ToReceiveCheck where convert(varchar(10),createdate,23) " +
            		" = '"+todayDay+"' and Type not in ('A','B','C') group by GRN");
             int d1 = 0; //达标
             int d2 = 0; //不达标
             int d3 = 0;
             if (grnListD!=null && grnListD.size()>0) { 
                 for(Object grn : grnListD){
                     Map map = (Map)grn;  
                	 List listD = toReceiveCheckDao.listData("SELECT TOP 1 * FROM ToReceiveCheck WHERE GRN='"+map.get("GRN")+"' " +
                      		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type not in ('A','B','C') ORDER BY createdate desc");              
                     if (listD!=null&&listD.size()>0) {
                          Map item = (Map)listD.get(0);  
                          Object closeDate = item.get("closeDate");
                          if(closeDate != null && !"null".equals(closeDate)) {
                              Object waittime = item.get("WaitTime");
                              if (Double.parseDouble(waittime.toString()) <= 36.00) {
                                  d1++;
                              }else {
                                  d2++;
                              }             
                          }else {
                         	 d3++;
                          }    
                     }
                 }
             }
             
             row = sheet.createRow(8);       
             String stringD[] = new String[4];
             stringD[0]="其他";
             stringD[1]=String.valueOf(d3);
             stringD[2]=String.valueOf(d1);
             stringD[3]=String.valueOf(d2);
             for (int i = 0; i < stringD.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(stringD[i]);
             }
             
             row = sheet.createRow(9); 
             String string[] = new String[4];
             string[0]="Total";
             string[1]=String.valueOf(a3+b3+c3+d3);
             string[2]=String.valueOf(a1+b1+c1+d1);
             string[3]=String.valueOf(a2+b2+c2+d2);
             for (int i = 0; i < string.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(string[i]);
             }
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
             		" values('1. 待点收看板','A','每单四小时内完成','达标', '"+a1+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
             		" values('1. 待点收看板','A','每单四小时内完成','不达标', '"+a2+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
             		" values('1. 待点收看板','A','急单今天完成','达标', '"+a1_2+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
             		" values('1. 待点收看板','A','急单今天完成','不达标', '"+a2_2+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('1. 待点收看板','B','每单四小时内完成','达标', '"+b1+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('1. 待点收看板','B','每单四小时内完成','不达标', '"+b2+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('1. 待点收看板','B','急单今天完成','达标', '"+b1_2+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('1. 待点收看板','B','急单今天完成','不达标', '"+b2_2+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
             		" values('1. 待点收看板','C','每单二十四小时内完成','达标', '"+c1+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
             		" values('1. 待点收看板','C','每单二十四小时内完成','不达标', '"+c2+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('1. 待点收看板','其他','每单三十六小时内完成','达标', '"+d1+"','"+month+"','"+todayDay+"' )");
             toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('1. 待点收看板','其他','每单三十六小时内完成','不达标', '"+d2+"','"+month+"','"+todayDay+"' )");
        }catch(Exception e) {
        	commonsLog.error("1", e);
        }
        commonsLog.info("end 1...");
        
        row = sheet.createRow(10);
        row = sheet.createRow(11);
        //2a
        try{
        	//标题
            CellRangeAddress region=new CellRangeAddress(12, 12, 0, 5); 
            sheet.addMergedRegion(region);
            row = sheet.createRow(12);
            row.setHeight((short) 0x200);
            cell = row.createCell(0);
            cell.setCellStyle(titleStyle);
            cell.setCellValue("2a.急料检验看板（非OCR）");
               
            row = sheet.createRow(13);
            String stringtitle[] = new String[6];
            stringtitle[0]="";
            stringtitle[1]="没有完成的数据";
            stringtitle[2]="达标";
            stringtitle[3]="不达标";
            stringtitle[4]="达标";
            stringtitle[5]="不达标";
            for (int i = 0; i < stringtitle.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringtitle[i]);
            }
            
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(14,14,2,3));
            row = sheet.createRow(14);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单四小时内完成");
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(14,14,4,5));
            cell = row.createCell(4);
            cell.setCellStyle(textStyle);
            cell.setCellValue("急单今天完成");
            
            //计算A
            List grnListA = umCheckNotOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckNotOCR where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' group by GRN");
            int a1 = 0; //达标
            int a2 = 0; //不达标
            int a1_2 = 0;
            int a2_2 = 0;
            int a3 = 0;
            if (grnListA!=null && grnListA.size()>0) { 
                for(Object grn : grnListA){
                    Map map = (Map)grn;  
                    List listA = umCheckNotOCRDao.listData("SELECT TOP 1 * FROM UrgentMaterialCheckNotOCR WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' ORDER BY createdate desc");              
                    if (listA!=null&&listA.size()>0) {                   
                        Map item = (Map)listA.get(0);
                        Object closeDate = item.get("closeDate");
                        if(closeDate != null && !"null".equals(closeDate)) {
                        	Object waittime = item.get("IQCCheckWaitTime");
                        	Object RDFinishTime = item.get("RDFinishTime");
                        	if (Double.parseDouble(waittime.toString()) <= 4.00) {
                                a1++;
                                if(todayDay.equals(RDFinishTime.toString().substring(0, 10))) {
                                	a1_2++ ;
                                }
                            }else {
                                a2++;
                                if(todayDay.equals(RDFinishTime.toString().substring(0, 10))) {
                                	a2_2++ ;
                                }
                            }
                        }else {
                        	a3++;
                        }
                    }
                }
            }
          
            row = sheet.createRow(15);       
            String stringA[] = new String[6];
            stringA[0]="A";
            stringA[1]=String.valueOf(a3);
            stringA[2]=String.valueOf(a1);
            stringA[3]=String.valueOf(a2);
            stringA[4]=String.valueOf(a1_2);
            stringA[5]=String.valueOf(a2_2);
            for (int i = 0; i < stringA.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringA[i]);
            }
            
            //计算B
            List grnListB = umCheckNotOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckNotOCR where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' group by GRN");
            int b1 = 0; //达标
            int b2 = 0; //不达标
            int b1_2 = 0;
            int b2_2 = 0;
            int b3 = 0;
            if (grnListB!=null && grnListB.size()>0) { 
                for(Object grn : grnListB){
                    Map map = (Map)grn;  
                    List listB = umCheckNotOCRDao.listData("SELECT TOP 1 * FROM UrgentMaterialCheckNotOCR WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' ORDER BY createdate desc");              
                    if (listB!=null&&listB.size()>0) {
                        Map item = (Map)listB.get(0);   
                        Object closeDate = item.get("closeDate");
                        if(closeDate != null && !"null".equals(closeDate)) {
                        	Object waittime = item.get("IQCCheckWaitTime");
                        	Object RDFinishTime = item.get("RDFinishTime");
		                	if (Double.parseDouble(waittime.toString()) <= 4.00) {
		                        b1++;
		                        if(todayDay.equals(RDFinishTime.toString().substring(0, 10))) {
		                        	b1_2++ ;
		                        }
		                    }else {
		                        b2++;
		                        if(todayDay.equals(RDFinishTime.toString().substring(0, 10))) {
		                        	b2_2++ ;
		                        }
		                    }
                        }else{
                        	b3++;
                        }
                    }
                }
            }
            row = sheet.createRow(16);       
            String stringB[] = new String[6];
            stringB[0]="B";
            stringB[1]=String.valueOf(b3);
            stringB[2]=String.valueOf(b1);
            stringB[3]=String.valueOf(b2);
            stringB[4]=String.valueOf(b1_2);
            stringB[5]=String.valueOf(b2_2);
            for (int i = 0; i < stringB.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringB[i]);
            }
           
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(17,17,2,3));
            row = sheet.createRow(17);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单二十四小时内完成");
            
          //计算C
           List grnListC = umCheckNotOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckNotOCR where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' group by GRN");
            int c1 = 0; //达标
            int c2 = 0; //不达标
            int c3 = 0;
            if (grnListC!=null && grnListC.size()>0) { 
                for(Object grn : grnListC){
                    Map map = (Map)grn;  
                    List listC = umCheckNotOCRDao.listData("SELECT TOP 1 * FROM UrgentMaterialCheckNotOCR WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' ORDER BY createdate desc");              
                    if (listC!=null&&listC.size()>0) {
                        Map item = (Map)listC.get(0); 
                        Object closeDate = item.get("closeDate");
                        if(closeDate != null && !"null".equals(closeDate)) {
                        	Object waittime = item.get("IQCCheckWaitTime");             
                        	if (Double.parseDouble(waittime.toString()) <= 24.00) {
                                c1++;
                            }else {
                                c2++;
                            } 
                        }else {
                        	c3++;
                        }           
                    }                
                }
            }
            
            row = sheet.createRow(18);       
            String stringC[] = new String[4];
            stringC[0]="C";
            stringC[1]=String.valueOf(c3);
            stringC[2]=String.valueOf(c1);
            stringC[3]=String.valueOf(c2);
            for (int i = 0; i < stringC.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringC[i]);
            }
            
            //计算其他
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(19,19,2,3));
            row = sheet.createRow(19);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单三十六小时内完成");
            
            List grnListD = umCheckNotOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckNotOCR where convert(varchar(10),createdate,23) " +
            		" = '"+todayDay+"' and Type not in ('A','B','C') group by GRN");
             int d1 = 0; //达标
             int d2 = 0; //不达标
             int d3 = 0;
             if (grnListD!=null && grnListD.size()>0) { 
                 for(Object grn : grnListD){
                     Map map = (Map)grn;  
                     List listD = umCheckNotOCRDao.listData("SELECT TOP 1 * FROM UrgentMaterialCheckNotOCR WHERE GRN='"+map.get("GRN")+"' " +
                     		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type not in ('A','B','C') ORDER BY createdate desc");              
                     if (listD!=null&&listD.size()>0) {
                         Map item = (Map)listD.get(0);
                         Object closeDate = item.get("closeDate");
                         if(closeDate != null && !"null".equals(closeDate)) {
                        	 Object waittime = item.get("IQCCheckWaitTime");
                             if (Double.parseDouble(waittime.toString()) <= 36.00) {
                                 d1++;
                             }else {
                                 d2++;
                             }    
                         }else {
                        	 d3++;
                         }      
                     }                
                 }
             }
             
             row = sheet.createRow(20);       
             String stringD[] = new String[4];
             stringD[0]="其他";
             stringD[1]=String.valueOf(d3);
             stringD[2]=String.valueOf(d1);
             stringD[3]=String.valueOf(d2);
             for (int i = 0; i < stringD.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(stringD[i]);
             }
             
             row = sheet.createRow(21); 
             String string[] = new String[4];
             string[0]="Total";
             string[1]=String.valueOf(a3+b3+c3+d3);
             string[2]=String.valueOf(a1+b1+c1+d1);
             string[3]=String.valueOf(a2+b2+c2+d2);
             for (int i = 0; i < string.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(string[i]);
             }
             
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2a.急料检验看板（非OCR）','A','每单四小时内完成','达标', '"+a1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2a.急料检验看板（非OCR）','A','每单四小时内完成','不达标', '"+a2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2a.急料检验看板（非OCR）','A','急单今天完成','达标', '"+a1_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2a.急料检验看板（非OCR）','A','急单今天完成','不达标', '"+a2_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2a.急料检验看板（非OCR）','B','每单四小时内完成','达标', '"+b1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2a.急料检验看板（非OCR）','B','每单四小时内完成','不达标', '"+b2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2a.急料检验看板（非OCR）','B','急单今天完成','达标', '"+b1_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2a.急料检验看板（非OCR）','B','急单今天完成','不达标', '"+b2_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2a.急料检验看板（非OCR）','C','每单二十四小时内完成','达标', '"+c1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2a.急料检验看板（非OCR）','C','每单二十四小时内完成','不达标', '"+c2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2a.急料检验看板（非OCR）','其他','每单三十六小时内完成','达标', '"+d1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2a.急料检验看板（非OCR）','其他','每单三十六小时内完成','不达标', '"+d2+"','"+month+"','"+todayDay+"' )");
        }catch(Exception e) {
        	commonsLog.error("2a", e);
        }
        
        commonsLog.info("end 2a...");
        
        row = sheet.createRow(22);
        row = sheet.createRow(23);
        
        //2b
        try{
        	//标题
            CellRangeAddress region=new CellRangeAddress(24, 24, 0, 5); 
            sheet.addMergedRegion(region);
            row = sheet.createRow(24);
            row.setHeight((short) 0x200);
            cell = row.createCell(0);
            cell.setCellStyle(titleStyle);
            cell.setCellValue("2b.急料检验看板（OCR）");
               
            row = sheet.createRow(25);
            String stringtitle[] = new String[6];
            stringtitle[0]="";
            stringtitle[1]="没有完成的数据";
            stringtitle[2]="达标";
            stringtitle[3]="不达标";
            stringtitle[4]="达标";
            stringtitle[5]="不达标";
            for (int i = 0; i < stringtitle.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringtitle[i]);
            }
            
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(26,26,2,3));
            row = sheet.createRow(26);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单四小时内完成");
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(26,26,4,5));
            cell = row.createCell(4);
            cell.setCellStyle(textStyle);
            cell.setCellValue("急单今天完成");
            
            //计算A
            List grnListA = umCheckOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckOCR where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' group by GRN");
            int a1 = 0; //达标
            int a2 = 0; //不达标
            int a1_2 = 0;
            int a2_2 = 0;
            int a3 = 0;
            if (grnListA!=null && grnListA.size()>0) { 
                for(Object grn : grnListA){
                    Map map = (Map)grn;  
                    List listA = umCheckOCRDao.listData("SELECT TOP 1 * FROM UrgentMaterialCheckOCR WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' ORDER BY createdate desc");              
                    if (listA!=null&&listA.size()>0) {                   
                        Map item = (Map)listA.get(0);
                        Object closeDate = item.get("closeDate");
                        if(closeDate != null && !"null".equals(closeDate)) {
                        	Object waittime = item.get("OCRCheckWaitTime");
                        	Object RDFinishTime = item.get("RDFinishTime");
                            if (Double.parseDouble(waittime.toString()) <= 4.00) {
                                a1++;
                                if(todayDay.equals(RDFinishTime.toString().substring(0, 10))) {
                                	a1_2++ ;
                                }
                            }else {
                                a2++;
                                if(todayDay.equals(RDFinishTime.toString().substring(0, 10))) {
                                	a2_2++ ;
                                }
                            }
                        }else {
                        	a3++;
                        }
                        
                    }
                }
            }
          
            row = sheet.createRow(27);       
            String stringA[] = new String[6];
            stringA[0]="A";
            stringA[1]=String.valueOf(a3);
            stringA[2]=String.valueOf(a1);
            stringA[3]=String.valueOf(a2);
            stringA[4]=String.valueOf(a1_2);
            stringA[5]=String.valueOf(a2_2);
            for (int i = 0; i < stringA.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringA[i]);
            }
            
            //计算B
            List grnListB = umCheckOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckOCR where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' group by GRN");
            int b1 = 0; //达标
            int b2 = 0; //不达标
            int b1_2 = 0;
            int b2_2 = 0;
            int b3 = 0;
            if (grnListB!=null && grnListB.size()>0) { 
                for(Object grn : grnListB){
                    Map map = (Map)grn;  
                    List listB = umCheckOCRDao.listData("SELECT TOP 1 * FROM UrgentMaterialCheckOCR WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' ORDER BY createdate desc");              
                    if (listB!=null&&listB.size()>0) {
                        Map item = (Map)listB.get(0);  
                        Object closeDate = item.get("closeDate");
                        if(closeDate != null && !"null".equals(closeDate)) {
	                        Object waittime = item.get("OCRCheckWaitTime");
	                        Object RDFinishTime = item.get("RDFinishTime");
	                        if (Double.parseDouble(waittime.toString()) <= 4.00) {
	                            b1++;
	                            if(todayDay.equals(RDFinishTime.toString().substring(0, 10))) {
	                            	b1_2++ ;
	                            }
	                        }else {
	                            b2++;
	                            if(todayDay.equals(RDFinishTime.toString().substring(0, 10))) {
	                            	b2_2++ ;
	                            }
	                        }
                        }else{
                        	b3++;
                        }
                    }
                }
            }
            row = sheet.createRow(28);       
            String stringB[] = new String[6];
            stringB[0]="B";
            stringB[1]=String.valueOf(b3);
            stringB[2]=String.valueOf(b1);
            stringB[3]=String.valueOf(b2);
            stringB[4]=String.valueOf(b1_2);
            stringB[5]=String.valueOf(b2_2);
            for (int i = 0; i < stringB.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringB[i]);
            }
           
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(29,29,2,3));
            row = sheet.createRow(29);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单二十四小时内完成");
            
          //计算C
           List grnListC = umCheckOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckOCR where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' group by GRN");
            int c1 = 0; //达标
            int c2 = 0; //不达标
            int c3 = 0;
            if (grnListC!=null && grnListC.size()>0) { 
                for(Object grn : grnListC){
                    Map map = (Map)grn;  
                    List listC = umCheckOCRDao.listData("SELECT TOP 1 * FROM UrgentMaterialCheckOCR WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' ORDER BY createdate desc");              
                    if (listC!=null&&listC.size()>0) {
                        Map item = (Map)listC.get(0);   
                        Object closeDate = item.get("closeDate");
                        if(closeDate != null && !"null".equals(closeDate)) {
                        	Object waittime = item.get("OCRCheckWaitTime");
                            if (Double.parseDouble(waittime.toString()) <= 24.00) {
                                c1++;
                            }else {
                                c2++;
                            }
                        }else{
                        	c3++;
                        }
                        
                    }                
                }
            }
            
            row = sheet.createRow(30);       
            String stringC[] = new String[4];
            stringC[0]="C";
            stringC[1]=String.valueOf(c3);
            stringC[2]=String.valueOf(c1);
            stringC[3]=String.valueOf(c2);
            for (int i = 0; i < stringC.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringC[i]);
            }
            
            //计算其他
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(31,31,2,3));
            row = sheet.createRow(31);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单三十六小时内完成");
            
            List grnListD = umCheckOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckOCR where convert(varchar(10),createdate,23) " +
            		" = '"+todayDay+"' and Type not in ('A','B','C') group by GRN");
             int d1 = 0; //达标
             int d2 = 0; //不达标
             int d3 = 0;
             if (grnListD!=null && grnListD.size()>0) { 
                 for(Object grn : grnListD){
                     Map map = (Map)grn;  
                     List listD = umCheckOCRDao.listData("SELECT TOP 1 * FROM UrgentMaterialCheckOCR WHERE GRN='"+map.get("GRN")+"' " +
                     		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type not in ('A','B','C') ORDER BY createdate desc");              
                     if (listD!=null&&listD.size()>0) {
                         Map item = (Map)listD.get(0);  
                         Object closeDate = item.get("closeDate");
                         if(closeDate != null && !"null".equals(closeDate)) {
                        	 Object waittime = item.get("OCRCheckWaitTime");
                             if (Double.parseDouble(waittime.toString()) <= 36.00) {
                                 d1++;
                             }else {
                                 d2++;
                             }  
                         }else {
                        	 d3++;
                         }    
                     }                
                 }
             }
             
             row = sheet.createRow(32);       
             String stringD[] = new String[4];
             stringD[0]="其他";
             stringD[1]=String.valueOf(d3);
             stringD[2]=String.valueOf(d1);
             stringD[3]=String.valueOf(d2);
             for (int i = 0; i < stringD.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(stringD[i]);
             }
             
             row = sheet.createRow(33); 
             String string[] = new String[4];
             string[0]="Total";
             string[1]=String.valueOf(a3+b3+c3+d3);
             string[2]=String.valueOf(a1+b1+c1+d1);
             string[3]=String.valueOf(a2+b2+c2+d2);
             for (int i = 0; i < string.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(string[i]);
             }
             
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2b.急料检验看板（OCR）','A','每单四小时内完成','达标', '"+a1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2b.急料检验看板（OCR）','A','每单四小时内完成','不达标', '"+a2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2b.急料检验看板（OCR）','A','急单今天完成','达标', '"+a1_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2b.急料检验看板（OCR）','A','急单今天完成','不达标', '"+a2_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2b.急料检验看板（OCR）','B','每单四小时内完成','达标', '"+b1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2b.急料检验看板（OCR）','B','每单四小时内完成','不达标', '"+b2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2b.急料检验看板（OCR）','B','急单今天完成','达标', '"+b1_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2b.急料检验看板（OCR）','B','急单今天完成','不达标', '"+b2_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2b.急料检验看板（OCR）','C','每单二十四小时内完成','达标', '"+c1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('2b.急料检验看板（OCR）','C','每单二十四小时内完成','不达标', '"+c2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2b.急料检验看板（OCR）','其他','每单三十六小时内完成','达标', '"+d1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('2b.急料检验看板（OCR）','其他','每单三十六小时内完成','不达标', '"+d2+"','"+month+"','"+todayDay+"' )");
              
        }catch(Exception e) {
        	commonsLog.error("2b", e);
        }
        
        commonsLog.info("end 2b...");
        row = sheet.createRow(34); 
        row = sheet.createRow(35);
        
        //3a
        try{
        	//标题
            CellRangeAddress region=new CellRangeAddress(36, 36, 0, 5); 
            sheet.addMergedRegion(region);
            row = sheet.createRow(36);
            row.setHeight((short) 0x200);
            cell = row.createCell(0);
            cell.setCellStyle(titleStyle);
            cell.setCellValue("3a.待入主料仓物料看板");
               
            row = sheet.createRow(37);
            String stringtitle[] = new String[6];
            stringtitle[0]="";
            stringtitle[1]="没有完成的数据";
            stringtitle[2]="达标";
            stringtitle[3]="不达标";
            stringtitle[4]="达标";
            stringtitle[5]="不达标";
            for (int i = 0; i < stringtitle.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringtitle[i]);
            }
            
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(38,38,2,3));
            row = sheet.createRow(38);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单四小时内完成");
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(38,38,4,5));
            cell = row.createCell(4);
            cell.setCellStyle(textStyle);
            cell.setCellValue("急单今天完成");
            
            //计算A
            List grnListA = trWarehouseDao.listData("SELECT GRN FROM ToReceiveWarehouse where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' group by GRN");
            int a1 = 0; //达标
            int a2 = 0; //不达标
            int a1_2 = 0;
            int a2_2 = 0;
            int a3 = 0;
            if (grnListA!=null && grnListA.size()>0) { 
                for(Object grn : grnListA){
                    Map map = (Map)grn;  
                    List listA = trWarehouseDao.listData("SELECT TOP 1 * FROM ToReceiveWarehouse WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' ORDER BY createdate desc");              
                    if (listA!=null&&listA.size()>0) {                   
                        Map item = (Map)listA.get(0);
                        Object returnWarehouseTime = item.get("ReturnWarehouseTime");
                        if(!"".equals(returnWarehouseTime)) {
                        	Object waittime = item.get("WaitTimeToMainbin");
                        	Object closeDate = item.get("closeDate");
                        	if (Double.parseDouble(waittime.toString()) <= 4.00) {
                                a1++;
                                if(todayDay.equals(closeDate.toString().substring(0, 10))) {
                                	a1_2++ ;
                                }
                            }else {
                                a2++;
                                if(todayDay.equals(closeDate.toString().substring(0, 10))) {
                                	a2_2++ ;
                                }
                            }
                        }else {
                        	a3++;
                        }
                    }
                }
            }
          
            row = sheet.createRow(39);       
            String stringA[] = new String[6];
            stringA[0]="A";
            stringA[1]=String.valueOf(a3);
            stringA[2]=String.valueOf(a1);
            stringA[3]=String.valueOf(a2);
            stringA[4]=String.valueOf(a1_2);
            stringA[5]=String.valueOf(a2_2);
            for (int i = 0; i < stringA.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringA[i]);
            }
            
            //计算B
            List grnListB = trWarehouseDao.listData("SELECT GRN FROM ToReceiveWarehouse where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' group by GRN");
            int b1 = 0; //达标
            int b2 = 0; //不达标
            int b1_2 = 0;
            int b2_2 = 0;
            int b3 = 0;
            if (grnListB!=null && grnListB.size()>0) { 
                for(Object grn : grnListB){
                    Map map = (Map)grn;  
                    List listB = trWarehouseDao.listData("SELECT TOP 1 * FROM ToReceiveWarehouse WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' ORDER BY createdate desc");              
                    if (listB!=null&&listB.size()>0) {
                    	Map item = (Map)listB.get(0);
                        Object returnWarehouseTime = item.get("ReturnWarehouseTime");
                        if(!"".equals(returnWarehouseTime)) {
                        	Object waittime = item.get("WaitTimeToMainbin");
                        	Object closeDate = item.get("closeDate");
                        	if (Double.parseDouble(waittime.toString()) <= 4.00) {
                                b1++;
                                if(todayDay.equals(closeDate.toString().substring(0, 10))) {
                                	b1_2++ ;
                                }
                            }else {
                                b2++;
                                if(todayDay.equals(closeDate.toString().substring(0, 10))) {
                                	b2_2++ ;
                                }
                            }
                        }else {
                        	b3++;
                        }
                    }
                    
                }
            }
            row = sheet.createRow(40);       
            String stringB[] = new String[6];
            stringB[0]="B";
            stringB[1]=String.valueOf(b3);
            stringB[2]=String.valueOf(b1);
            stringB[3]=String.valueOf(b2);
            stringB[4]=String.valueOf(b1_2);
            stringB[5]=String.valueOf(b2_2);
            for (int i = 0; i < stringB.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringB[i]);
            }
           
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(41,41,2,3));
            row = sheet.createRow(41);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单二十四小时内完成");
            
            //计算C
           List grnListC = trWarehouseDao.listData("SELECT GRN FROM ToReceiveWarehouse where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' group by GRN");
            int c1 = 0; //达标
            int c2 = 0; //不达标
            int c3 = 0;
            if (grnListC!=null && grnListC.size()>0) { 
                for(Object grn : grnListC){
                    Map map = (Map)grn;  
                    List listC = trWarehouseDao.listData("SELECT TOP 1 * FROM ToReceiveWarehouse WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' ORDER BY createdate desc");              
                    if (listC!=null&&listC.size()>0) {
                    	Map item = (Map)listC.get(0);
                        Object returnWarehouseTime = item.get("ReturnWarehouseTime");
                        if(!"".equals(returnWarehouseTime)) {
                        	Object waittime = item.get("WaitTimeToMainbin");
                        	 if (Double.parseDouble(waittime.toString()) <= 24.00) {
                                 c1++;
                             }else {
                                 c2++;
                             }
                        }else {
                        	c3++;
                        }            
                    }                
                }
            }
            
            row = sheet.createRow(42);       
            String stringC[] = new String[4];
            stringC[0]="C";
            stringC[1]=String.valueOf(c3);
            stringC[2]=String.valueOf(c1);
            stringC[3]=String.valueOf(c2);
            for (int i = 0; i < stringC.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringC[i]);
            }
            
            //计算其他
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(43,43,2,3));
            row = sheet.createRow(43);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单三十六小时内完成");
            
            List grnListD = trWarehouseDao.listData("SELECT GRN FROM ToReceiveWarehouse where convert(varchar(10),createdate,23) " +
            		" = '"+todayDay+"' and Type not in ('A','B','C') group by GRN");
             int d1 = 0; //达标
             int d2 = 0; //不达标
             int d3 = 0;
             if (grnListD!=null && grnListD.size()>0) { 
                 for(Object grn : grnListD){
                     Map map = (Map)grn;  
                     List listD = trWarehouseDao.listData("SELECT TOP 1 * FROM ToReceiveWarehouse WHERE GRN='"+map.get("GRN")+"' " +
                     		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type not in ('A','B','C') ORDER BY createdate desc");              
                     if (listD!=null&&listD.size()>0) {
                    	 Map item = (Map)listD.get(0);
                         Object returnWarehouseTime = item.get("ReturnWarehouseTime");
                         if(!"".equals(returnWarehouseTime)) {
                         	Object waittime = item.get("WaitTimeToMainbin");
                         	 if (Double.parseDouble(waittime.toString()) <= 36.00) {
                                  d1++;
                              }else {
                                  d2++;
                              }
                         }else {
                         	d3++;
                         }     
                     }                
                 }
             }
             
             row = sheet.createRow(44);       
             String stringD[] = new String[4];
             stringD[0]="其他";
             stringD[1]=String.valueOf(d3);
             stringD[2]=String.valueOf(d1);
             stringD[3]=String.valueOf(d2);
             for (int i = 0; i < stringD.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(stringD[i]);
             }
             
             row = sheet.createRow(45); 
             String string[] = new String[4];
             string[0]="Total";
             string[1]=String.valueOf(a3+b3+c3+d3);
             string[2]=String.valueOf(a1+b1+c1+d1);
             string[3]=String.valueOf(a2+b2+c2+d2);
             for (int i = 0; i < string.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(string[i]);
             }
             
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3a.待入主料仓物料看板','A','每单四小时内完成','达标', '"+a1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3a.待入主料仓物料看板','A','每单四小时内完成','不达标', '"+a2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3a.待入主料仓物料看板','A','急单今天完成','达标', '"+a1_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3a.待入主料仓物料看板','A','急单今天完成','不达标', '"+a2_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3a.待入主料仓物料看板','B','每单四小时内完成','达标', '"+b1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3a.待入主料仓物料看板','B','每单四小时内完成','不达标', '"+b2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3a.待入主料仓物料看板','B','急单今天完成','达标', '"+b1_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3a.待入主料仓物料看板','B','急单今天完成','不达标', '"+b2_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3a.待入主料仓物料看板','C','每单二十四小时内完成','达标', '"+c1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3a.待入主料仓物料看板','C','每单二十四小时内完成','不达标', '"+c2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3a.待入主料仓物料看板','其他','每单三十六小时内完成','达标', '"+d1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3a.待入主料仓物料看板','其他','每单三十六小时内完成','不达标', '"+d2+"','"+month+"','"+todayDay+"' )");
        }catch(Exception e) {
        	commonsLog.error("3a", e);
        }
        
        commonsLog.info("end 3a...");
        
        row = sheet.createRow(46); 
        row = sheet.createRow(47);
        //3b
        try{
        	//标题
            CellRangeAddress region=new CellRangeAddress(48, 48, 0, 5); 
            sheet.addMergedRegion(region);
            row = sheet.createRow(48);
            row.setHeight((short) 0x200);
            cell = row.createCell(0);
            cell.setCellStyle(titleStyle);
            cell.setCellValue("3b.待入退貨仓物料看板");
               
            row = sheet.createRow(49);
            String stringtitle[] = new String[6];
            stringtitle[0]="";
            stringtitle[1]="没有完成的数据";
            stringtitle[2]="达标";
            stringtitle[3]="不达标";
            stringtitle[4]="达标";
            stringtitle[5]="不达标";
            for (int i = 0; i < stringtitle.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringtitle[i]);
            }
            
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(50,50,2,3));
            row = sheet.createRow(50);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单四小时内完成");
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(50,50,4,5));
            cell = row.createCell(4);
            cell.setCellStyle(textStyle);
            cell.setCellValue("急单今天完成");
            
            //计算A
            List grnListA = trWarehouseBDao.listData("SELECT GRN FROM ToReceiveWarehouseB where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' group by GRN");
            int a1 = 0; //达标
            int a2 = 0; //不达标
            int a1_2 = 0;
            int a2_2 = 0;
            int a3 = 0;
            if (grnListA!=null && grnListA.size()>0) { 
                for(Object grn : grnListA){
                    Map map = (Map)grn;  
                    List listA = trWarehouseBDao.listData("SELECT TOP 1 * FROM ToReceiveWarehouseB WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='A' ORDER BY createdate desc");              
                    if (listA!=null&&listA.size()>0) {                   
                    	Map item = (Map)listA.get(0);
                        Object returnWarehouseTime = item.get("ReturnWarehouseTime");
                        if(!"".equals(returnWarehouseTime)) {
                        	Object waittime = item.get("WaitTimeToMainbin");
                        	Object closeDate = item.get("closeDate");
                        	if (Double.parseDouble(waittime.toString()) <= 4.00) {
                                a1++;
                                if(todayDay.equals(closeDate.toString().substring(0, 10))) {
                                	a1_2++ ;
                                }
                            }else {
                                a2++;
                                if(todayDay.equals(closeDate.toString().substring(0, 10))) {
                                	a2_2++ ;
                                }
                            }
                        }else {
                        	a3++;
                        }
                    }
                }
            }
          
            row = sheet.createRow(51);       
            String stringA[] = new String[6];
            stringA[0]="A";
            stringA[1]=String.valueOf(a3);
            stringA[2]=String.valueOf(a1);
            stringA[3]=String.valueOf(a2);
            stringA[4]=String.valueOf(a1_2);
            stringA[5]=String.valueOf(a2_2);
            for (int i = 0; i < stringA.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringA[i]);
            }
            
            //计算B
            List grnListB = trWarehouseBDao.listData("SELECT GRN FROM ToReceiveWarehouseB where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' group by GRN");
            int b1 = 0; //达标
            int b2 = 0; //不达标
            int b1_2 = 0;
            int b2_2 = 0;
            int b3 = 0;
            if (grnListB!=null && grnListB.size()>0) { 
                for(Object grn : grnListB){
                    Map map = (Map)grn;  
                    List listB = trWarehouseBDao.listData("SELECT TOP 1 * FROM ToReceiveWarehouseB WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='B' ORDER BY createdate desc");              
                    if (listB!=null&&listB.size()>0) {
                    	Map item = (Map)listB.get(0);
                        Object returnWarehouseTime = item.get("ReturnWarehouseTime");
                        if(!"".equals(returnWarehouseTime)) {
                        	Object waittime = item.get("WaitTimeToMainbin");
                        	Object closeDate = item.get("closeDate");
                        	if (Double.parseDouble(waittime.toString()) <= 4.00) {
                                b1++;
                                if(todayDay.equals(closeDate.toString().substring(0, 10))) {
                                	b1_2++ ;
                                }
                            }else {
                                b2++;
                                if(todayDay.equals(closeDate.toString().substring(0, 10))) {
                                	b2_2++ ;
                                }
                            }
                        }else {
                        	b3++;
                        }
                    }
                }
            }
            row = sheet.createRow(52);       
            String stringB[] = new String[6];
            stringB[0]="B";
            stringB[1]=String.valueOf(b3);
            stringB[2]=String.valueOf(b1);
            stringB[3]=String.valueOf(b2);
            stringB[4]=String.valueOf(b1_2);
            stringB[5]=String.valueOf(b2_2);
            for (int i = 0; i < stringB.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringB[i]);
            }
           
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(53,53,2,3));
            row = sheet.createRow(53);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单二十四小时内完成");
            
          //计算C
           List grnListC = trWarehouseBDao.listData("SELECT GRN FROM ToReceiveWarehouseB where convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' group by GRN");
            int c1 = 0; //达标
            int c2 = 0; //不达标
            int c3 = 0;
            if (grnListC!=null && grnListC.size()>0) { 
                for(Object grn : grnListC){
                    Map map = (Map)grn;  
                    List listC = trWarehouseBDao.listData("SELECT TOP 1 * FROM ToReceiveWarehouseB WHERE GRN='"+map.get("GRN")+"' AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type='C' ORDER BY createdate desc");              
                    if (listC!=null&&listC.size()>0) {
                    	Map item = (Map)listC.get(0);
                        Object returnWarehouseTime = item.get("ReturnWarehouseTime");
                        if(!"".equals(returnWarehouseTime)) {
                        	Object waittime = item.get("WaitTimeToMainbin");
                        	 if (Double.parseDouble(waittime.toString()) <= 24.00) {
                                 c1++;
                             }else {
                                 c2++;
                             }
                        }else {
                        	c3++;
                        }       
                    }                
                }
            }
            
            row = sheet.createRow(54);       
            String stringC[] = new String[4];
            stringC[0]="C";
            stringC[1]=String.valueOf(c3);
            stringC[2]=String.valueOf(c1);
            stringC[3]=String.valueOf(c2);
            for (int i = 0; i < stringC.length; i++) {
                cell = row.createCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(stringC[i]);
            }
            
          //计算其他
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(55,55,2,3));
            row = sheet.createRow(55);
            cell = row.createCell(2);
            cell.setCellStyle(textStyle);
            cell.setCellValue("每单三十六小时内完成");
            
            List grnListD = trWarehouseBDao.listData("SELECT GRN FROM ToReceiveWarehouseB where convert(varchar(10),createdate,23) " +
            		" = '"+todayDay+"' and Type not in ('A','B','C') group by GRN");
             int d1 = 0; //达标
             int d2 = 0; //不达标
             int d3 = 0;
             if (grnListD!=null && grnListD.size()>0) { 
                 for(Object grn : grnListD){
                     Map map = (Map)grn;  
                     List listD = trWarehouseBDao.listData("SELECT TOP 1 * FROM ToReceiveWarehouseB WHERE GRN='"+map.get("GRN")+"' " +
                     		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' and Type not in ('A','B','C') ORDER BY createdate desc");              
                     if (listD!=null&&listD.size()>0) {
                    	 Map item = (Map)listD.get(0);
                         Object returnWarehouseTime = item.get("ReturnWarehouseTime");
                         if(!"".equals(returnWarehouseTime)) {
                         	Object waittime = item.get("WaitTimeToMainbin");
                         	 if (Double.parseDouble(waittime.toString()) <= 36.00) {
                                  d1++;
                              }else {
                                  d2++;
                              }
                         }else {
                         	d3++;
                         }            
                     }                
                 }
             }
             
             row = sheet.createRow(56);       
             String stringD[] = new String[4];
             stringD[0]="其他";
             stringD[1]=String.valueOf(d3);
             stringD[2]=String.valueOf(d1);
             stringD[3]=String.valueOf(d2);
             for (int i = 0; i < stringD.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(stringD[i]);
             }
             
             row = sheet.createRow(57); 
             String string[] = new String[4];
             string[0]="Total";
             string[1]=String.valueOf(a3+b3+c3+d3);
             string[2]=String.valueOf(a1+b1+c1+d1);
             string[3]=String.valueOf(a2+b2+c2+d2);
             for (int i = 0; i < string.length; i++) {
                 cell = row.createCell(i);
                 cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellStyle(textStyle);
                 cell.setCellValue(string[i]);
             }
             
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3b.待入退貨仓物料看板','A','每单四小时内完成','达标', '"+a1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3b.待入退貨仓物料看板','A','每单四小时内完成','不达标', '"+a2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3b.待入退貨仓物料看板','A','急单今天完成','达标', '"+a1_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3b.待入退貨仓物料看板','A','急单今天完成','不达标', '"+a2_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3b.待入退貨仓物料看板','B','每单四小时内完成','达标', '"+b1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3b.待入退貨仓物料看板','B','每单四小时内完成','不达标', '"+b2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3b.待入退貨仓物料看板','B','急单今天完成','达标', '"+b1_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3b.待入退貨仓物料看板','B','急单今天完成','不达标', '"+b2_2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3b.待入退貨仓物料看板','C','每单二十四小时内完成','达标', '"+c1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
              		" values('3b.待入退貨仓物料看板','C','每单二十四小时内完成','不达标', '"+c2+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3b.待入退貨仓物料看板','其他','每单三十六小时内完成','达标', '"+d1+"','"+month+"','"+todayDay+"' )");
              toReceiveCheckDao.execute("insert into DailyTrend(report,type,remark,result,qty,month,dodate) " +
               		" values('3b.待入退貨仓物料看板','其他','每单三十六小时内完成','不达标', '"+d2+"','"+month+"','"+todayDay+"' )");
        }catch(Exception e) {
        	commonsLog.error("3b", e);
        }
        commonsLog.info("end 3b...");
        
        //sheet2
        HSSFSheet sheet2 = wb.createSheet("Detail_1待点收看板");
        {
        	 //表头
    		String header[] = {"id", "Sequence", "GRN","GRN时间(101/105)", "最后刷新时间", "等待时间(H)","收货部绑库时间", "物料编号","GRN总数","UID个数","生产需求日期", 
    				 "工厂", "急料类别", "是否完成", "目标时间是否达标(A&B<=4小時, C<=24小時,其它<=36小時)", "是否达标(当天)" };
    		row = sheet2.createRow(0);
    		//表头
    		for (int j = 0; j < header.length; j++) {
    			cell = row.createCell(j);
    			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
    			cell.setCellStyle(headerStyle);
    			cell.setCellValue(header[j]);
    		}
    		
            List grnList = toReceiveCheckDao.listData("SELECT GRN FROM ToReceiveCheck where convert(varchar(10),createdate,23)" +
            		" = '"+todayDay+"' group by GRN");
            if(grnList != null && grnList.size()>0) {
            	int i=1;
            	for(Object grn : grnList){
                    Map map = (Map)grn;  
                    List<ToReceiveCheck> d1list = toReceiveCheckDao.listData(" SELECT top 1 * FROM ToReceiveCheck WHERE GRN='"+map.get("GRN")+"' " +
                     		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' order by id desc ", new ToReceiveCheckMapper() );
                    ToReceiveCheck item = d1list.get(0);
					row = sheet2.createRow(i);
					int j=0;
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getId());
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getSequence());
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getGRN());
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getGRNDATE().substring(0, 16));
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getCreatedate().substring(0, 16));
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getWaittime());
	                //
	                boolean isFinish = false;
	                if (item.getCloseDate()!=null && !"null".equals(item.getCloseDate())) {
	                	cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getCloseDate().toString().substring(0, 16));
		                isFinish = true;
	                }else {
	                	cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue("");
	                }
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getItemNumber());
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getGRNQuantity());
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getUIDQuantity());
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getProductionTime());
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getPlant());
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(item.getType());
	                //
	                if(isFinish) {
	                	cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue("是");
		                
		                boolean isTarget = false;
		                if("A".equals(item.getType()) || "B".equals(item.getType()) ) {
		                	if( Double.parseDouble(item.getWaittime()) <= 4.0) {
		                		isTarget = true;
		                	}
		                }else if("C".equals(item.getType())) {
		                	if( Double.parseDouble(item.getWaittime()) <= 24.0) {
		                		isTarget = true;
		                	}
		                }else {
		                	if( Double.parseDouble(item.getWaittime()) <= 36.0) {
		                		isTarget = true;
		                	}
		                }
		                
		                if(isTarget) {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("是");
			                
			                if(todayDay.equals(item.getGRNDATE().substring(0, 10))) {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("是");
			                } else {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("否");
			                }
		                }else {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
			                
			                cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
		                }
	                }else{
	                	cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue("否");
		                
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue("否");
		                
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue("否");
	                }
	                i++;
            	}
    		}
        }
       
        commonsLog.info("end sheet2...");
        
        //sheet3
        HSSFSheet sheet3 = wb.createSheet("Detail_2a");
        {
		   	//表头
			String header[] = {"id", "Sequence","GRN","物料编号", "ReceivingLocation", "收货部绑库时间", "最后刷新时间", "IQCGetTime", 
					"等待时间(H)", "SAP321/122时间", "IQCReturnTime", "生产需求日期", "工厂", "急料类别", "UID", 
					"是否完成", "目标时间是否达标(A&B<=4小時, C<=24小時,其它<=36小時)", "是否达标(当天)" };
			row = sheet3.createRow(0);
			//表头
			for (int j = 0; j < header.length; j++) {
				cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(header[j]);
			}
			
		       List grnList = umCheckNotOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckNotOCR where convert(varchar(10),createdate,23)" +
		       		" = '"+todayDay+"' group by GRN");
		       if(grnList != null && grnList.size()>0) {
		       	int i=1;
		       	for(Object grn : grnList){
		               Map map = (Map)grn;  
		               List<UrgentMaterialCheckNotOCR> d1list = umCheckNotOCRDao.listData(" SELECT top 1 * FROM UrgentMaterialCheckNotOCR WHERE GRN='"+map.get("GRN")+"' " +
		                		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' order by id desc ", new UrgentMaterialCheckNotOCRMapper() );
		                UrgentMaterialCheckNotOCR item = d1list.get(0);
						row = sheet3.createRow(i);
						int j=0;
						cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getId());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getSequence());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getGRN());
		                
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getItemNumber());
		                
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getReceivingLocation());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getRDFinishTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getCreatedate().substring(0, 16));
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getIQCGetTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getIQCCheckWaitTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                if(item.getCloseDate() !=null && !"null".equals(item.getCloseDate())) {
		                	cell.setCellValue(item.getCloseDate().substring(0, 16));
		                }else {
		                	cell.setCellValue("");
		                }
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                if(item.getIQCReturnTime() != null && !"null".equals(item.getIQCReturnTime()) && !"".equals(item.getIQCReturnTime())) {
		                	cell.setCellValue(item.getIQCReturnTime().substring(0, 16));
		                }else {
		                	cell.setCellValue("");
		                }
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getProductionTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getPlant());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getType());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getUID());
		                //
		                if(item.getCloseDate() != null && !"null".equals(item.getCloseDate())) {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("是");
			                
			                boolean isTarget = false;
			                if("A".equals(item.getType()) || "B".equals(item.getType()) ) {
			                	if( Double.parseDouble(item.getIQCCheckWaitTime()) <= 4.0) {
			                		isTarget = true;
			                	}
			                }else if("C".equals(item.getType())) {
			                	if( Double.parseDouble(item.getIQCCheckWaitTime()) <= 24.0) {
			                		isTarget = true;
			                	}
			                }else {
			                	if( Double.parseDouble(item.getIQCCheckWaitTime()) <= 36.0) {
			                		isTarget = true;
			                	}
			                }
			                if(isTarget) {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("是");
				                
				                if(item.getCloseDate().substring(0, 10).equals(item.getRDFinishTime().substring(0, 10))) {
				                	cell = row.createCell(j++);
					                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					                cell.setCellStyle(textStyle);
					                cell.setCellValue("是");
				                } else {
				                	cell = row.createCell(j++);
					                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					                cell.setCellStyle(textStyle);
					                cell.setCellValue("否");
				                }
				                
			                }else {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("否");
				                
				                cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("否");
			                }
			                
		                }else {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
			                //
			                cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
			                //
			                cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
		                }
		                i++;
		       	}
			}
       }
        
        commonsLog.info("end sheet3...");
        //sheet4
        HSSFSheet sheet4 = wb.createSheet("Detail_2b");
        {
        	//表头
			String header[] = {"id", "Sequence", "GRN", "物料编号","ReceivingLocation","UID个数","GRN总数", "收货部绑库时间", "最后刷新时间",
					"等待时间(H)", "SAP321/122时间(OCR物料)", "生产需求日期", "工厂", "急料类别" , "是否完成", "目标时间是否达标(A&B<=4小時, C<=24小時,其它<=36小時)", "是否达标(当天)"};
			row = sheet4.createRow(0);
			//表头
			for (int j = 0; j < header.length; j++) {
				cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(header[j]);
			}
			
		       List grnList = umCheckOCRDao.listData("SELECT GRN FROM UrgentMaterialCheckOCR where convert(varchar(10),createdate,23)" +
		       		" = '"+todayDay+"' group by GRN");
		       if(grnList != null && grnList.size()>0) {
		       	int i=1;
		       	for(Object grn : grnList){
		               Map map = (Map)grn;  
		               List<UrgentMaterialCheckOCR> d1list = umCheckOCRDao.listData(" SELECT top 1 * FROM UrgentMaterialCheckOCR WHERE GRN='"+map.get("GRN")+"' " +
		                		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' order by id desc ", new UrgentMaterialCheckOCRMapper() );
		               UrgentMaterialCheckOCR item = d1list.get(0);
						row = sheet4.createRow(i);
						int j=0;
						cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getId());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getSequence());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getGRN());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getItemNumber());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getReceivingLocation());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getUIDQuantity());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getGRNQuantity());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getRDFinishTime().substring(0, 16));
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getCreatedate().substring(0, 16));
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getOCRCheckWaitTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                if(item.getCloseDate() !=null && !"null".equals(item.getCloseDate())) {
		                	cell.setCellValue(item.getCloseDate().substring(0, 16));
		                }else {
		                	cell.setCellValue("");
		                }
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getProductionTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getPlant());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getType());
		                //
		                if(item.getCloseDate() != null && !"null".equals(item.getCloseDate())) {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("是");
			                
			                boolean isTarget = false;
			                if("A".equals(item.getType()) || "B".equals(item.getType()) ) {
			                	if( Double.parseDouble(item.getOCRCheckWaitTime()) <= 4.0) {
			                		isTarget = true;
			                	}
			                }else if("C".equals(item.getType())) {
			                	if( Double.parseDouble(item.getOCRCheckWaitTime()) <= 24.0) {
			                		isTarget = true;
			                	}
			                }else {
			                	if( Double.parseDouble(item.getOCRCheckWaitTime()) <= 36.0) {
			                		isTarget = true;
			                	}
			                }
			                if(isTarget) {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("是");
				                
				                if(todayDay.equals(item.getRDFinishTime().substring(0, 10))) {
				                	cell = row.createCell(j++);
					                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					                cell.setCellStyle(textStyle);
					                cell.setCellValue("是");
				                } else {
				                	cell = row.createCell(j++);
					                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					                cell.setCellStyle(textStyle);
					                cell.setCellValue("否");
				                }
				                
			                }else {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("否");
				                
				                cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("否");
			                }
			                
		                }else {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
			                //
			                cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
			                //
			                cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
		                }
		                
		                
		                i++;
		       	}
			}
        }
        commonsLog.info("end sheet4...");
        
        //sheet5
        HSSFSheet sheet5 = wb.createSheet("Detail_3a");
        {
        	//表头
			String header[] = {"id", "Sequence", "GRN", "物料编号", "GRN总数", "ReceivingLocation", "生产需求日期", "AegisQualify", "SAPQualify",
					"SAP321/122时间", "RH或CU绑库时间", "等待时间(H)", "最后刷新时间", "工厂", "急料类别", "是否完成", "目标时间是否达标(A&B<=4小時, C<=24小時,其它<=36小時)", "是否达标(当天)"};
			row = sheet5.createRow(0);
			//表头
			for (int j = 0; j < header.length; j++) {
				cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(header[j]);
			}
			
		       List grnList = trWarehouseDao.listData("SELECT GRN FROM ToReceiveWarehouse where convert(varchar(10),createdate,23)" +
		       		" = '"+todayDay+"' group by GRN");
		       if(grnList != null && grnList.size()>0) {
		       	int i=1;
		       	for(Object grn : grnList){
		               Map map = (Map)grn;  
		               List<ToReceiveWarehouse> d1list = trWarehouseDao.listData(" SELECT top 1 * FROM ToReceiveWarehouse WHERE GRN='"+map.get("GRN")+"' " +
		                		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' order by id desc ", new ToReceiveWarehouseMapper() );
		               ToReceiveWarehouse item = d1list.get(0);
						row = sheet5.createRow(i);
						int j=0;
						cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getId());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getSequence());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getGRN());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getItemNumber());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getGRNQuantity());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getReceivingLocation());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getProductionTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getAegisQualify());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getSAPQualify());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getCloseDate().substring(0, 16));
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getReturnWarehouseTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getWaitTimeToMainbin());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getCreatedate().substring(0, 16));
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getPlant());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getType());
		                //
		                if(item.getReturnWarehouseTime() != null && !"".equals(item.getReturnWarehouseTime())) {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("是");
			                
			                boolean isTarget = false;
			                if("A".equals(item.getType()) || "B".equals(item.getType()) ) {
			                	if( Double.parseDouble(item.getWaitTimeToMainbin()) <= 4.0) {
			                		isTarget = true;
			                	}
			                }else if("C".equals(item.getType())) {
			                	if( Double.parseDouble(item.getWaitTimeToMainbin()) <= 24.0) {
			                		isTarget = true;
			                	}
			                }else {
			                	if( Double.parseDouble(item.getWaitTimeToMainbin()) <= 36.0) {
			                		isTarget = true;
			                	}
			                }
			                if(isTarget) {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("是");
				                
				                if(todayDay.equals(item.getCloseDate().substring(0, 10))) {
				                	cell = row.createCell(j++);
					                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					                cell.setCellStyle(textStyle);
					                cell.setCellValue("是");
				                } else {
				                	cell = row.createCell(j++);
					                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					                cell.setCellStyle(textStyle);
					                cell.setCellValue("否");
				                }
				                
			                }else {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("否");
				                
				                cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("否");
			                }
			                
		                }else {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
			                //
			                cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
			                //
			                cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
		                }
		                
		                i++;
		       	}
			}
        }
        commonsLog.info("end sheet5...");
        
        //sheet6
        HSSFSheet sheet6 = wb.createSheet("Detail_3b");
        {
        	//表头
			String header[] = {"id", "Sequence", "GRN", "物料编号", "GRN总数", "ReceivingLocation", "生产需求日期", "AegisQualify", "SAPQualify",
					"SAP122时间", "RT绑库时间",  "等待时间(H)", "最后刷新时间",  "工厂", "急料类别", "是否完成", "目标时间是否达标(A&B<=4小時, C<=24小時,其它<=36小時)", "是否达标(当天)"};
			row = sheet6.createRow(0);
			//表头
			for (int j = 0; j < header.length; j++) {
				cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(header[j]);
			}
			
		       List grnList = trWarehouseBDao.listData("SELECT GRN FROM ToReceiveWarehouseB where convert(varchar(10),createdate,23)" +
		       		" = '"+todayDay+"' group by GRN");
		       if(grnList != null && grnList.size()>0) {
		       	int i=1;
		       	for(Object grn : grnList){
		               Map map = (Map)grn;  
		               List<ToReceiveWarehouseB> d1list = trWarehouseBDao.listData(" SELECT top 1 * FROM ToReceiveWarehouseB WHERE GRN='"+map.get("GRN")+"' " +
		                		" AND convert(varchar(10),createdate,23) = '"+todayDay+"' order by id desc ", new ToReceiveWarehouseBMapper() );
		               ToReceiveWarehouseB item = d1list.get(0);
						row = sheet6.createRow(i);
						int j=0;
						cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getId());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getSequence());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getGRN());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getItemNumber());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getGRNQuantity());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getReceivingLocation());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getProductionTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getAegisQualify());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getSAPQualify());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getCloseDate().substring(0, 16));
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getReturnWarehouseTime());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getWaitTimeToMainbin());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getCreatedate().substring(0, 16));
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getPlant());
		                //
		                cell = row.createCell(j++);
		                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		                cell.setCellStyle(textStyle);
		                cell.setCellValue(item.getType());
		                
		                //
		                if(item.getReturnWarehouseTime() != null && !"".equals(item.getReturnWarehouseTime())) {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("是");
			                
			                boolean isTarget = false;
			                if("A".equals(item.getType()) || "B".equals(item.getType()) ) {
			                	if( Double.parseDouble(item.getWaitTimeToMainbin()) <= 4.0) {
			                		isTarget = true;
			                	}
			                }else if("C".equals(item.getType())) {
			                	if( Double.parseDouble(item.getWaitTimeToMainbin()) <= 24.0) {
			                		isTarget = true;
			                	}
			                }else {
			                	if( Double.parseDouble(item.getWaitTimeToMainbin()) <= 36.0) {
			                		isTarget = true;
			                	}
			                }
			                if(isTarget) {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("是");
				                
				                if(todayDay.equals(item.getCloseDate().substring(0, 10))) {
				                	cell = row.createCell(j++);
					                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					                cell.setCellStyle(textStyle);
					                cell.setCellValue("是");
				                } else {
				                	cell = row.createCell(j++);
					                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					                cell.setCellStyle(textStyle);
					                cell.setCellValue("否");
				                }
				                
			                }else {
			                	cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("否");
				                
				                cell = row.createCell(j++);
				                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				                cell.setCellStyle(textStyle);
				                cell.setCellValue("否");
			                }
			                
		                }else {
		                	cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
			                //
			                cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
			                //
			                cell = row.createCell(j++);
			                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			                cell.setCellStyle(textStyle);
			                cell.setCellValue("否");
		                }
		                
		                i++;
		       	}
			}
        }
        commonsLog.info("end sheet6...");
        
        
        
        //
        commonsLog.info("start sheet7: Daily Trend ...");
        HSSFSheet sheet7 = wb.createSheet("Daily Trend");
        {
        	//表头
        	//获取当月第一天和最后一天  
    		Calendar cale = Calendar.getInstance();
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
    		String firstday;
    		String lastday;  
    		//获取当月的第一天  
    		cale = Calendar.getInstance();  
    		cale.add(Calendar.MONTH, 0);  
    		cale.set(Calendar.DAY_OF_MONTH, 1);  
    		firstday = format.format(cale.getTime());  
    		//获取当月的最后一天  
    		cale = Calendar.getInstance();  
    		cale.add(Calendar.MONTH, 1);  
    		cale.set(Calendar.DAY_OF_MONTH, 0);  
    		lastday = format.format(cale.getTime()); 
    		//
    		int totalDay = Integer.parseInt(lastday.substring(8));
    		String[] dates = new String[totalDay];
    		cale.setTime(format.parse(firstday));
    		for(int i=0;i<totalDay; ) {
    			dates[i]=format.format(cale.getTime()); 
    			i++;
    			cale.add(Calendar.DATE, i);
    		}
			String header[] = {"位置","急料类别","事项要求","达标/不达标"};
			row = sheet7.createRow(0);
			//表头
			for (int j = 0; j < header.length; j++) {
				cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(header[j]);
			}
			//
			int rownum = 1;
			{
				String[] body1 = {"1. 待点收看板", "A", "每单四小时内完成", "达标"};
				List calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body1[0]+"' and type='"+body1[1]+"' " +
	       				" and remark='"+body1[2]+"' and result='"+body1[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j = header.length;
					for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(headerStyle);
						cell.setCellValue(map.get("dodate").toString());
					}
					//
					j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				//
				String[] body2 = {"1. 待点收看板", "A", "每单四小时内完成", "不达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body2[0]+"' and type='"+body2[1]+"' " +
	       				" and remark='"+body2[2]+"' and result='"+body2[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body3 = {"1. 待点收看板", "A", "急单今天完成", "达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body3[0]+"' and type='"+body3[1]+"' " +
	       				" and remark='"+body3[2]+"' and result='"+body3[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body4 = {"1. 待点收看板", "A", "急单今天完成", "不达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body4[0]+"' and type='"+body4[1]+"' " +
	       				" and remark='"+body4[2]+"' and result='"+body4[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body5 = {"1. 待点收看板", "B", "每单四小时内完成", "达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body5[0]+"' and type='"+body5[1]+"' " +
	       				" and remark='"+body5[2]+"' and result='"+body5[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body6 = {"1. 待点收看板", "B", "每单四小时内完成", "不达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body6[0]+"' and type='"+body6[1]+"' " +
	       				" and remark='"+body6[2]+"' and result='"+body6[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body7 = {"1. 待点收看板", "B", "急单今天完成", "达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body7[0]+"' and type='"+body7[1]+"' " +
	       				" and remark='"+body7[2]+"' and result='"+body7[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body8 = {"1. 待点收看板", "B", "急单今天完成", "不达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body8[0]+"' and type='"+body8[1]+"' " +
	       				" and remark='"+body8[2]+"' and result='"+body8[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body9 = {"1. 待点收看板", "C", "每单二十四小时内完成", "达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body9[0]+"' and type='"+body9[1]+"' " +
	       				" and remark='"+body9[2]+"' and result='"+body9[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body10 = {"1. 待点收看板", "C", "每单二十四小时内完成", "不达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body10[0]+"' and type='"+body10[1]+"' " +
	       				" and remark='"+body10[2]+"' and result='"+body10[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body11 = {"1. 待点收看板", "其他", "每单三十六小时内完成", "达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body11[0]+"' and type='"+body11[1]+"' " +
	       				" and remark='"+body11[2]+"' and result='"+body11[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				String[] body12 = {"1. 待点收看板", "其他", "每单三十六小时内完成", "不达标"};
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body12[0]+"' and type='"+body12[1]+"' " +
	       				" and remark='"+body12[2]+"' and result='"+body12[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
			}
			
			
			{
				String[] body1 = {"2a.急料检验看板（非OCR）", "A", "每单四小时内完成", "达标"};
				String[] body2 = {"2a.急料检验看板（非OCR）", "A", "每单四小时内完成", "不达标"};
				String[] body3 = {"2a.急料检验看板（非OCR）", "A", "急单今天完成", "达标"};
				String[] body4 = {"2a.急料检验看板（非OCR）", "A", "急单今天完成", "不达标"};
				String[] body5 = {"2a.急料检验看板（非OCR）", "B", "每单四小时内完成", "达标"};
				String[] body6 = {"2a.急料检验看板（非OCR）", "B", "每单四小时内完成", "不达标"};
				String[] body7 = {"2a.急料检验看板（非OCR）", "B", "急单今天完成", "达标"};
				String[] body8 = {"2a.急料检验看板（非OCR）", "B", "急单今天完成", "不达标"};
				String[] body9 = {"2a.急料检验看板（非OCR）", "C", "每单二十四小时内完成", "达标"};
				String[] body10 = {"2a.急料检验看板（非OCR）", "C", "每单二十四小时内完成", "不达标"};
				String[] body11 = {"2a.急料检验看板（非OCR）", "其他", "每单三十六小时内完成", "达标"};
				String[] body12 = {"2a.急料检验看板（非OCR）", "其他", "每单三十六小时内完成", "不达标"};
				List calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body1[0]+"' and type='"+body1[1]+"' " +
	       				" and remark='"+body1[2]+"' and result='"+body1[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				//
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body2[0]+"' and type='"+body2[1]+"' " +
	       				" and remark='"+body2[2]+"' and result='"+body2[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body3[0]+"' and type='"+body3[1]+"' " +
	       				" and remark='"+body3[2]+"' and result='"+body3[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body4[0]+"' and type='"+body4[1]+"' " +
	       				" and remark='"+body4[2]+"' and result='"+body4[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body5[0]+"' and type='"+body5[1]+"' " +
	       				" and remark='"+body5[2]+"' and result='"+body5[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body6[0]+"' and type='"+body6[1]+"' " +
	       				" and remark='"+body6[2]+"' and result='"+body6[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body7[0]+"' and type='"+body7[1]+"' " +
	       				" and remark='"+body7[2]+"' and result='"+body7[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body8[0]+"' and type='"+body8[1]+"' " +
	       				" and remark='"+body8[2]+"' and result='"+body8[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body9[0]+"' and type='"+body9[1]+"' " +
	       				" and remark='"+body9[2]+"' and result='"+body9[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body10[0]+"' and type='"+body10[1]+"' " +
	       				" and remark='"+body10[2]+"' and result='"+body10[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body11[0]+"' and type='"+body11[1]+"' " +
	       				" and remark='"+body11[2]+"' and result='"+body11[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body12[0]+"' and type='"+body12[1]+"' " +
	       				" and remark='"+body12[2]+"' and result='"+body12[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
			}
			
			
			{
				String[] body1 = {"2b.急料检验看板（OCR）", "A", "每单四小时内完成", "达标"};
				String[] body2 = {"2b.急料检验看板（OCR）", "A", "每单四小时内完成", "不达标"};
				String[] body3 = {"2b.急料检验看板（OCR）", "A", "急单今天完成", "达标"};
				String[] body4 = {"2b.急料检验看板（OCR）", "A", "急单今天完成", "不达标"};
				String[] body5 = {"2b.急料检验看板（OCR）", "B", "每单四小时内完成", "达标"};
				String[] body6 = {"2b.急料检验看板（OCR）", "B", "每单四小时内完成", "不达标"};
				String[] body7 = {"2b.急料检验看板（OCR）", "B", "急单今天完成", "达标"};
				String[] body8 = {"2b.急料检验看板（OCR）", "B", "急单今天完成", "不达标"};
				String[] body9 = {"2b.急料检验看板（OCR）", "C", "每单二十四小时内完成", "达标"};
				String[] body10 = {"2b.急料检验看板（OCR）", "C", "每单二十四小时内完成", "不达标"};
				String[] body11 = {"2b.急料检验看板（OCR）", "其他", "每单三十六小时内完成", "达标"};
				String[] body12 = {"2b.急料检验看板（OCR）", "其他", "每单三十六小时内完成", "不达标"};
				List calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body1[0]+"' and type='"+body1[1]+"' " +
	       				" and remark='"+body1[2]+"' and result='"+body1[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				//
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body2[0]+"' and type='"+body2[1]+"' " +
	       				" and remark='"+body2[2]+"' and result='"+body2[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body3[0]+"' and type='"+body3[1]+"' " +
	       				" and remark='"+body3[2]+"' and result='"+body3[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body4[0]+"' and type='"+body4[1]+"' " +
	       				" and remark='"+body4[2]+"' and result='"+body4[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body5[0]+"' and type='"+body5[1]+"' " +
	       				" and remark='"+body5[2]+"' and result='"+body5[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body6[0]+"' and type='"+body6[1]+"' " +
	       				" and remark='"+body6[2]+"' and result='"+body6[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body7[0]+"' and type='"+body7[1]+"' " +
	       				" and remark='"+body7[2]+"' and result='"+body7[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body8[0]+"' and type='"+body8[1]+"' " +
	       				" and remark='"+body8[2]+"' and result='"+body8[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body9[0]+"' and type='"+body9[1]+"' " +
	       				" and remark='"+body9[2]+"' and result='"+body9[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body10[0]+"' and type='"+body10[1]+"' " +
	       				" and remark='"+body10[2]+"' and result='"+body10[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body11[0]+"' and type='"+body11[1]+"' " +
	       				" and remark='"+body11[2]+"' and result='"+body11[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body12[0]+"' and type='"+body12[1]+"' " +
	       				" and remark='"+body12[2]+"' and result='"+body12[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
			}
			
			{
				String[] body1 = {"3a.待入主料仓物料看板", "A", "每单四小时内完成", "达标"};
				String[] body2 = {"3a.待入主料仓物料看板", "A", "每单四小时内完成", "不达标"};
				String[] body3 = {"3a.待入主料仓物料看板", "A", "急单今天完成", "达标"};
				String[] body4 = {"3a.待入主料仓物料看板", "A", "急单今天完成", "不达标"};
				String[] body5 = {"3a.待入主料仓物料看板", "B", "每单四小时内完成", "达标"};
				String[] body6 = {"3a.待入主料仓物料看板", "B", "每单四小时内完成", "不达标"};
				String[] body7 = {"3a.待入主料仓物料看板", "B", "急单今天完成", "达标"};
				String[] body8 = {"3a.待入主料仓物料看板", "B", "急单今天完成", "不达标"};
				String[] body9 = {"3a.待入主料仓物料看板", "C", "每单二十四小时内完成", "达标"};
				String[] body10 = {"3a.待入主料仓物料看板", "C", "每单二十四小时内完成", "不达标"};
				String[] body11 = {"3a.待入主料仓物料看板", "其他", "每单三十六小时内完成", "达标"};
				String[] body12 = {"3a.待入主料仓物料看板", "其他", "每单三十六小时内完成", "不达标"};
				List calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body1[0]+"' and type='"+body1[1]+"' " +
	       				" and remark='"+body1[2]+"' and result='"+body1[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				//
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body2[0]+"' and type='"+body2[1]+"' " +
	       				" and remark='"+body2[2]+"' and result='"+body2[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body3[0]+"' and type='"+body3[1]+"' " +
	       				" and remark='"+body3[2]+"' and result='"+body3[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body4[0]+"' and type='"+body4[1]+"' " +
	       				" and remark='"+body4[2]+"' and result='"+body4[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body5[0]+"' and type='"+body5[1]+"' " +
	       				" and remark='"+body5[2]+"' and result='"+body5[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body6[0]+"' and type='"+body6[1]+"' " +
	       				" and remark='"+body6[2]+"' and result='"+body6[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body7[0]+"' and type='"+body7[1]+"' " +
	       				" and remark='"+body7[2]+"' and result='"+body7[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body8[0]+"' and type='"+body8[1]+"' " +
	       				" and remark='"+body8[2]+"' and result='"+body8[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body9[0]+"' and type='"+body9[1]+"' " +
	       				" and remark='"+body9[2]+"' and result='"+body9[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body10[0]+"' and type='"+body10[1]+"' " +
	       				" and remark='"+body10[2]+"' and result='"+body10[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body11[0]+"' and type='"+body11[1]+"' " +
	       				" and remark='"+body11[2]+"' and result='"+body11[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body12[0]+"' and type='"+body12[1]+"' " +
	       				" and remark='"+body12[2]+"' and result='"+body12[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
			}
			
			{
				String[] body1 = {"3b.待入退貨仓物料看板", "A", "每单四小时内完成", "达标"};
				String[] body2 = {"3b.待入退貨仓物料看板", "A", "每单四小时内完成", "不达标"};
				String[] body3 = {"3b.待入退貨仓物料看板", "A", "急单今天完成", "达标"};
				String[] body4 = {"3b.待入退貨仓物料看板", "A", "急单今天完成", "不达标"};
				String[] body5 = {"3b.待入退貨仓物料看板", "B", "每单四小时内完成", "达标"};
				String[] body6 = {"3b.待入退貨仓物料看板", "B", "每单四小时内完成", "不达标"};
				String[] body7 = {"3b.待入退貨仓物料看板", "B", "急单今天完成", "达标"};
				String[] body8 = {"3b.待入退貨仓物料看板", "B", "急单今天完成", "不达标"};
				String[] body9 = {"3b.待入退貨仓物料看板", "C", "每单二十四小时内完成", "达标"};
				String[] body10 = {"3b.待入退貨仓物料看板", "C", "每单二十四小时内完成", "不达标"};
				String[] body11 = {"3b.待入退貨仓物料看板", "其他", "每单三十六小时内完成", "达标"};
				String[] body12 = {"3b.待入退貨仓物料看板", "其他", "每单三十六小时内完成", "不达标"};
				List calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body1[0]+"' and type='"+body1[1]+"' " +
	       				" and remark='"+body1[2]+"' and result='"+body1[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body1[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				//
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body2[0]+"' and type='"+body2[1]+"' " +
	       				" and remark='"+body2[2]+"' and result='"+body2[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body2[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body3[0]+"' and type='"+body3[1]+"' " +
	       				" and remark='"+body3[2]+"' and result='"+body3[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body3[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body4[0]+"' and type='"+body4[1]+"' " +
	       				" and remark='"+body4[2]+"' and result='"+body4[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body4[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body5[0]+"' and type='"+body5[1]+"' " +
	       				" and remark='"+body5[2]+"' and result='"+body5[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body5[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body6[0]+"' and type='"+body6[1]+"' " +
	       				" and remark='"+body6[2]+"' and result='"+body6[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body6[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body7[0]+"' and type='"+body7[1]+"' " +
	       				" and remark='"+body7[2]+"' and result='"+body7[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body7[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body8[0]+"' and type='"+body8[1]+"' " +
	       				" and remark='"+body8[2]+"' and result='"+body8[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body8[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body9[0]+"' and type='"+body9[1]+"' " +
	       				" and remark='"+body9[2]+"' and result='"+body9[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body9[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body10[0]+"' and type='"+body10[1]+"' " +
	       				" and remark='"+body10[2]+"' and result='"+body10[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body10[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body11[0]+"' and type='"+body11[1]+"' " +
	       				" and remark='"+body11[2]+"' and result='"+body11[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body11[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
				
				calList = toReceiveCheckDao.listData("SELECT qty, dodate" +
						" FROM DailyTrend where month='"+month+"' " +
	       				" and report='"+body12[0]+"' and type='"+body12[1]+"' " +
	       				" and remark='"+body12[2]+"' and result='"+body12[3]+"'  order by dodate, id ");
				if(calList != null && calList.size()>0) {
					int j=0;
					row = sheet7.createRow(rownum++);
					cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[0]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[1]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[2]);
	                //
	                cell = row.createCell(j++);
	                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	                cell.setCellStyle(textStyle);
	                cell.setCellValue(body12[3]);
	                //
	                for(Object item : calList){
						Map map = (Map)item;  
						cell = row.createCell(j++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellStyle(textStyle);
						cell.setCellValue(map.get("qty").toString());
					}
				}
			}
			
			
        }
        commonsLog.info("end sheet7: Daily Trend ...");
        
        //文件先保存到服务器下的目录
        try {
            String filename = "IQC_DashBoardDailyReport_"+todayDay+".xls";
            String path = "D:/dashboard/" + filename;
            FileOutputStream out = new FileOutputStream(new File(path));
            wb.write(out);
            //为了删除文件
            list.add(path);
            //
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        aegisDB.close();
        
        return todayDay;
    
    }

}
