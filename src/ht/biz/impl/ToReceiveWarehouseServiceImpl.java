package ht.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ht.biz.IToReceiveWarehouseService;
import ht.dao.IToReceiveWarehouseDao;
import ht.entity.ToReceiveWarehouse;

@Component("trWarehouseService")
public class ToReceiveWarehouseServiceImpl implements IToReceiveWarehouseService{
    @Autowired
    private IToReceiveWarehouseDao trWarehouseDao;

	@Override
    public List<ToReceiveWarehouse> findAllToReceiveWarehouse() {
        List<ToReceiveWarehouse> list = trWarehouseDao.listData("select * from ToReceiveWarehouse where Type in ('A','B','C') " +
        		" and Sequence = (select max(Sequence) from ToReceiveWarehouse) and ReturnWarehouseTime ='' order by Type, GRN ");
        return list;
    }

	@Override
    public void saveToReceiveWarehouse(List<ToReceiveWarehouse> list) throws Exception {
    	StringBuilder sbSql = new StringBuilder();
		String[] arraySql = new String[list.size()];
		int i=0;
		for (ToReceiveWarehouse trw : list) {
			sbSql.append("insert into ToReceiveWarehouse(GRN,ItemNumber,GRNQuantity,ReceivingLocation,ProductionTime,AegisQualify");
			sbSql.append(",SAPQualify,WaitTimeToMainbin,plant,UID,Type,Sequence, ReturnWarehouseTime, createdate, closeDate");
			sbSql.append(") values('"+trw.getGRN()+"','"+trw.getItemNumber()+"','"+trw.getGRNQuantity()+"','"+trw.getReceivingLocation()+"',");
			sbSql.append("'"+trw.getProductionTime()+"','"+trw.getAegisQualify()+"','"+trw.getSAPQualify()+"','"+trw.getWaitTimeToMainbin()+"',");
			sbSql.append("'"+trw.getPlant()+"','"+trw.getUID()+"','"+trw.getType()+"','"+trw.getSequence()+"',");
			sbSql.append("'"+trw.getReturnWarehouseTime()+"','"+trw.getCreatedate()+"','"+trw.getCloseDate()+"')");
			arraySql[i] = sbSql.toString();
			i++;
			sbSql.setLength(0);
		}
		if(i>0){
			trWarehouseDao.batchExecute(arraySql);
		}
    }

	@Override
    public void deleteAllToReceiveWarehouse() throws Exception {
        trWarehouseDao.execute("delete from ToReceiveWarehouse");  
    }
}
