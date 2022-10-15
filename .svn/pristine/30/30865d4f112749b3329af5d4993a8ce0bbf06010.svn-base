package ht.biz.impl;

import ht.biz.IToReceiveCheckService;
import ht.dao.IToReceiveCheckDao;
import ht.entity.ToReceiveCheck;
import ht.mapper.ToReceiveCheckMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fr.third.org.apache.poi.hssf.usermodel.HSSFCell;


@Component("trCheckService")
public class ToReceiveCheckServiceImpl implements IToReceiveCheckService{
    @Autowired
    private IToReceiveCheckDao trCheckDao;
    
    public List<ToReceiveCheck> findAllToReceiveCheck() throws Exception {
        List<ToReceiveCheck> list = trCheckDao.listData("select * from ToReceiveCheck where Type in ('A','B','C') " +
        		" and Sequence = (select max(Sequence) from ToReceiveCheck) and closeDate IS  NULL" +
        		" AND ItemNumber in ( select a.bom from NotFinishSO a left join ( select  plant,bom,sum(convert(float,needQty)) qty from NotFinishSO group by bom,plant) b on a.bom=b.bom where b.qty> convert(float,REPLACE(inventory,'null',0.0)))" +
        		"order by Type, GRN");  //ProductionTime
        return list;
    }
    
    private List<ToReceiveCheck> getLatestList(){
        String condition = " Type in ('A','B','C') and Sequence = (select max(Sequence) from ToReceiveCheck) ";
        LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
        orderby.put("Type", "asc");
        orderby.put("GRN", "asc");
        List<ToReceiveCheck> list = trCheckDao.listData("ToReceiveCheck", condition, orderby, new ToReceiveCheckMapper());
        return list;
    }
    
    public InputStream doExport() throws Exception {
        InputStream is = null;
        List<ToReceiveCheck> list = getLatestList();
        HSSFWorkbook wb = new HSSFWorkbook();
        if (list!=null) {
            org.apache.poi.hssf.usermodel.HSSFSheet sheet = wb.createSheet("LatestList");
            org.apache.poi.hssf.usermodel.HSSFRow row = null;
            org.apache.poi.hssf.usermodel.HSSFCell cell = null;
            
            //标题字体
            HSSFFont titleFont = wb.createFont();
            titleFont.setFontHeightInPoints((short)10);
            titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
            titleFont.setFontName("新細明體");          
            //内容字体
            HSSFFont commonFont = wb.createFont();
            commonFont.setFontHeightInPoints((short)9);
            commonFont.setFontName("新細明體");         
            //表头样式
            HSSFCellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(titleFont);
            headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框实线
            headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框实线
            headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框实线
            headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框实线
            headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
            headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            headerStyle.setFillForegroundColor((short) 5);// 设置背景色
            headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            headerStyle.setWrapText(true);          
            //内容文字样式
            HSSFCellStyle textStyle = wb.createCellStyle();
            textStyle.setFont(commonFont);
            textStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框实线
            textStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框实线
            textStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框实线
            textStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框实线
            textStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中                                            
            //内容日期样式
            HSSFCellStyle dateStyle = wb.createCellStyle();
            dateStyle.setFont(commonFont);
            dateStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框实线
            dateStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框实线
            dateStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框实线
            dateStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框实线
            dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            HSSFDataFormat format= wb.createDataFormat();
            dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
            
            //表头
            String header[] = {"GRN","工厂","物料编号","GRN数量","UID个数","急料类别","待收料等待时间"};
            
            row = sheet.createRow(0); 
            for (int j = 0; j < header.length; j++) {
                cell = row.createCell(j);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(header[j]);
            }
            int rownum = 0;
            for (int i = 0; i < list.size(); i++) {
            	ToReceiveCheck item = list.get(i);
                rownum = rownum+1;
                row = sheet.createRow(rownum);
                int cellindex = 0;
                
                cell = row.createCell(cellindex++);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(item.getGRN());
                
                cell = row.createCell(cellindex++);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(item.getPlant());
                
                cell = row.createCell(cellindex++);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(item.getItemNumber());
                
                cell = row.createCell(cellindex++);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(item.getGRNQuantity());
                            
                cell = row.createCell(cellindex++);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(item.getUIDQuantity());
                
                cell = row.createCell(cellindex++);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(item.getType());
                
                cell = row.createCell(cellindex++);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellStyle(textStyle);
                cell.setCellValue(item.getWaittime());

            }
            
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);       
        byte[] content = os.toByteArray();
        is = new ByteArrayInputStream(content);
        return is;
    }
    
	public void saveToReceiveCheck(List<ToReceiveCheck> list) throws Exception {
		StringBuilder sbSql = new StringBuilder();
		String[] arraySql = new String[list.size()];
		int i=0;
		for (ToReceiveCheck trc : list) {
			sbSql.append("insert into ToReceiveCheck(GRN,ItemNumber,GRNQuantity,UIDQuantity,ProductionTime, WaitTime, " +
					" plant, UID, GRNDATE, GRN103,Type,Sequence, createdate ");
			sbSql.append(") values('"+trc.getGRN()+"','"+trc.getItemNumber()+"','"+trc.getGRNQuantity()+"','"+trc.getUIDQuantity()+"', '"+trc.getProductionTime()+"'");
			sbSql.append(",'"+trc.getWaittime()+"','"+trc.getPlant()+"','"+trc.getUID()+"','"+trc.getGRNDATE()+"','"+trc.getGRN103()+"','"+trc.getType()+"'");
			sbSql.append(",'"+trc.getSequence()+"','"+trc.getCreatedate()+"'");
			sbSql.append(")");
			arraySql[i] = sbSql.toString();
			i++;
			sbSql.setLength(0);
		}
		if(i>0){
			trCheckDao.batchExecute(arraySql);
		}
		//System.out.println(sbSql);
	}
	
	public void deleteAllToReceiveCheck() throws Exception {
		trCheckDao.execute("delete from ToReceiveCheck");
	}

}
