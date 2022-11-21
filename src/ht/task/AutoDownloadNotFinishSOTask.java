package ht.task;

import ht.dao.INotFinishSODao;
import ht.entity.NotFinishSO;
import ht.util.SAPService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * SAP下载
 * @date 2020-8-7
 * @author 刘惠明
 */
public class AutoDownloadNotFinishSOTask {
	@Autowired
	private INotFinishSODao nfsoDao;
	
	public void execute() throws Exception {
		try {
			System.out.println("start...AutoDownloadNotFinishSOTask "+new Date());
			
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, +30);
			String nextMonth = df.format(c.getTime());
			c.setTime(new Date());
			c.add(Calendar.DATE, -30);
			String lastMonth = df.format(c.getTime());
			SAPService sap = new SAPService(); 
			List<NotFinishSO> list = sap.getMsdList(lastMonth, nextMonth);
			//
			if(list!=null && list.size() >0){
				System.out.println(list.size());
				nfsoDao.execute("delete from NotFinishSO ");
				StringBuffer sbSql = new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					sbSql.setLength(0);
					sbSql.append("insert into NotFinishSO(so,plant,pn,bom,needQty,gotQty,stock,inventory,soStartDate) values(");
					sbSql.append("'"+list.get(i).getSo()+"','"+list.get(i).getPlant()+"'");
					sbSql.append(",'"+list.get(i).getPn()+"','"+list.get(i).getBom()+"'");
					sbSql.append(",'"+list.get(i).getNeedQty()+"','"+list.get(i).getGotQty()+"'");
					sbSql.append(",'"+list.get(i).getStock()+"','"+list.get(i).getInventory()+"'");
					sbSql.append(",'"+list.get(i).getSoStartDate()+"'");
					sbSql.append(")");
					nfsoDao.execute(sbSql.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("end...AutoDownloadNotFinishSOTask "+new Date());
	}
}