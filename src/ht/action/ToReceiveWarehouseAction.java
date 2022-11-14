package ht.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import ht.biz.IToReceiveWarehouseService;
import ht.entity.ToReceiveWarehouse;
import ht.util.ConDashBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @description 待入仓物料看板
 * @author 刘慧明
 * @date 2020-9-3
 * @updateUser 丁国钊
 * @updateDate 2022-11-3
 * @updateRemark 增加代码注释，删除行尾注释，删除无用代码
 * @version 2.0.0
 */
@Controller("ToReceiveWarehouseAction")
@SuppressWarnings("unchecked")
public class ToReceiveWarehouseAction extends ActionSupport{
    @Autowired
    private IToReceiveWarehouseService trWarehouseService;

    /**
     * 获取看板数据，加载页面
     * @return 重定向页面
     */
    public String doList()throws Exception {
    	// 看板数据获取（33 DB GRNewDashBoard3），存入model
        List<ToReceiveWarehouse> list = trWarehouseService.findAllToReceiveWarehouse();    
        if (list != null && list.size()>0) {
            Map request = (Map) ActionContext.getContext().get("request");
            request.put("toReceiveWarehouseList", list); 
        }
        getTotalSummary();
        list = null;
        return "tolist";
    }

    /**
     * 统计各个类别
     */
    public void getTotalSummary() throws Exception {
    	Map request = (Map) ActionContext.getContext().get("request");
    	// 33 GRNewDashBoard3 数据库
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
    	rs = grnewdbDB.executeQuery("select count(*) a2 from UrgentMaterialCheckNotOCR where Type='A' and Sequence = (select max(Sequence) from UrgentMaterialCheckNotOCR) and closeDate is null ");
    	if(rs.next()){
    		request.put("a2", rs.getInt("a2"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b2 from UrgentMaterialCheckNotOCR where Type='B' and Sequence = (select max(Sequence) from UrgentMaterialCheckNotOCR) and closeDate is null ");
    	if(rs.next()){
    		request.put("b2", rs.getInt("b2")); 
    	}
    	rs = grnewdbDB.executeQuery("select count(*) a3 from UrgentMaterialCheckOCR where Type='A' and Sequence = (select max(Sequence) from UrgentMaterialCheckOCR) and closeDate is null ");
    	if(rs.next()){
    		request.put("a3", rs.getInt("a3"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b3 from UrgentMaterialCheckOCR where Type='B' and Sequence = (select max(Sequence) from UrgentMaterialCheckOCR) and closeDate is null  ");
    	if(rs.next()){
    		request.put("b3", rs.getInt("b3"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) a4 from ToReceiveWarehouse where Type='A' and Sequence = (select max(Sequence) from ToReceiveWarehouse) and ReturnWarehouseTime ='' ");
    	if(rs.next()){
    		request.put("a4", rs.getInt("a4"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b4 from ToReceiveWarehouse where Type='B' and Sequence = (select max(Sequence) from ToReceiveWarehouse) and ReturnWarehouseTime ='' ");
    	if(rs.next()){
    		request.put("b4", rs.getInt("b4")); 
    	}
    	rs = grnewdbDB.executeQuery("select count(*) a5 from ToReceiveWarehouseB where Type='A' and Sequence = (select max(Sequence) from ToReceiveWarehouseB) and ReturnWarehouseTime =''  ");
    	if(rs.next()){
    		request.put("a5", rs.getInt("a5"));
    	}
    	rs = grnewdbDB.executeQuery("select count(*) b5 from ToReceiveWarehouseB where Type='B' and Sequence = (select max(Sequence) from ToReceiveWarehouseB) and ReturnWarehouseTime =''  ");
    	if(rs.next()){
    		request.put("b5", rs.getInt("b5"));
    	}
    	grnewdbDB.close();
    }
}
