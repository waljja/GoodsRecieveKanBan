package ht.biz.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ht.biz.IToReceiveWarehouseService;
import ht.dao.IToReceiveWarehouseDao;
import ht.entity.ToReceiveWarehouse;

/**
 * 插入看板数据（代入主料仓看板）
 * @author 刘惠明
 * @createDate 2020-9-3
 * @updateUser 丁国钊
 * @updateDate 2022-11-3
 * @updateRemark 增加注释
 */
@Component("trWarehouseService")
public class ToReceiveWarehouseServiceImpl implements IToReceiveWarehouseService{
    @Autowired
    private IToReceiveWarehouseDao trWarehouseDao;
    
    public List<ToReceiveWarehouse> findAllToReceiveWarehouse() {
        List<ToReceiveWarehouse> list = trWarehouseDao.listData("select * from ToReceiveWarehouse where Type in ('A','B','C') " +
        		" and Sequence = (select max(Sequence) from ToReceiveWarehouse) and ReturnWarehouseTime ='' order by Type, GRN ");
        return list;
    }

	/**
	 * @description 把获取到的看板数据插入看板数据库
	 * @param list
	 * @throws Exception
	 */
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
			// 批量插入数据
			trWarehouseDao.batchExecute(arraySql);
		}
    }

	@Override
    public void deleteAllToReceiveWarehouse() throws Exception {
        trWarehouseDao.execute("delete from ToReceiveWarehouse");  
    }
}
