package ht.biz.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ht.biz.IToReceiveWarehouseBService;
import ht.dao.IToReceiveWarehouseBDao;
import ht.entity.ToReceiveWarehouseB;

@Component("trWarehouseBService")
public class ToReceiveWarehouseServiceBImpl implements IToReceiveWarehouseBService{
    @Autowired
    private IToReceiveWarehouseBDao trWarehouseBDao;
    
    public List<ToReceiveWarehouseB> findAllToReceiveWarehouseB() {
        List<ToReceiveWarehouseB> list = trWarehouseBDao.listData("select * from ToReceiveWarehouseB where Type in ('A','B','C') " +
        		" and Sequence = (select max(Sequence) from ToReceiveWarehouseB) and ReturnWarehouseTime ='' order by Type, GRN ");
        return list;
    }


    public void saveToReceiveWarehouseB(List<ToReceiveWarehouseB> list)throws Exception {
    	StringBuilder sbSql = new StringBuilder();
		String[] arraySql = new String[list.size()];
		int i=0;
		for (ToReceiveWarehouseB trwb : list) {
			sbSql.append("insert into ToReceiveWarehouseB(GRN,ItemNumber,GRNQuantity,ReceivingLocation,ProductionTime,AegisQualify");
	        sbSql.append(",SAPQualify,WaitTimeToMainbin,plant,UID,Type,Sequence, ReturnWarehouseTime, createdate, closeDate");
	        sbSql.append(") values('"+trwb.getGRN()+"','"+trwb.getItemNumber()+"','"+trwb.getGRNQuantity()+"','"+trwb.getReceivingLocation()+"',");
	        sbSql.append("'"+trwb.getProductionTime()+"','"+trwb.getAegisQualify()+"','"+trwb.getSAPQualify()+"','"+trwb.getWaitTimeToMainbin()+"',");
			sbSql.append("'"+trwb.getPlant()+"','"+trwb.getUID()+"','"+trwb.getType()+"','"+trwb.getSequence()+"',");
			sbSql.append("'"+trwb.getReturnWarehouseTime()+"','"+trwb.getCreatedate()+"','"+trwb.getCloseDate()+"')");
			arraySql[i] = sbSql.toString();
			i++;
			sbSql.setLength(0);
		}
		if(i>0){
			trWarehouseBDao.batchExecute(arraySql);
		}
        
    }
    
    public void deleteAllToReceiveWarehouseB() throws Exception {
        trWarehouseBDao.execute("delete from ToReceiveWarehouseB");  
    }
}
