package ht.biz.impl;

import ht.biz.IUrgentMaterialCheckOCRService;
import ht.dao.IUrgentMaterialCheckOCRDao;
import ht.entity.UrgentMaterialCheckOCR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("umCheckOCRService")
public class UrgentMaterialCheckOCRServiceImpl implements IUrgentMaterialCheckOCRService{
    @Autowired
    private IUrgentMaterialCheckOCRDao umCheckOCRDao;

	/**
	 * 增加 "D" 类型
	 * @return
	 */
	@Override
    public List<UrgentMaterialCheckOCR> findAllUrgentMaterialCheckOCR() {
        List<UrgentMaterialCheckOCR> list = umCheckOCRDao.listData("select * from UrgentMaterialCheckOCR where Type in ('A','B','C','D') " +
        		" and Sequence = (select max(Sequence) from UrgentMaterialCheckOCR) and closeDate is null order by Type, GRN ");
        return list;
    }

    @Override
    public void saveUrgentMaterialCheckOCR(List<UrgentMaterialCheckOCR> list) throws Exception {
    	StringBuilder sbSql = new StringBuilder();
		String[] arraySql = new String[list.size()];
		int i=0;
		for (UrgentMaterialCheckOCR umc : list) {
			sbSql.append("insert into UrgentMaterialCheckOCR(GRN,ItemNumber,ReceivingLocation,UIDQuantity,GRNQuantity,RDFinishTime,ProductionTime," +
					" OCRCheckWaitTime,plant,UID,auditDataTime," +
					" Type,Sequence, createdate , closeDate");
	        sbSql.append(") values('"+umc.getGRN()+"','"+umc.getItemNumber()+"','"+umc.getReceivingLocation()+"','"+umc.getUIDQuantity()+"','"+umc.getGRNQuantity()+"','"+umc.getRDFinishTime()+"','"+umc.getProductionTime()+"',");
	        if(umc.getCloseDate() == null) {
	        	sbSql.append("'"+umc.getOCRCheckWaitTime()+"','"+umc.getPlant()+"','"+umc.getUID()+"','"+umc.getAuditDataTime()+
		        		 "','"+umc.getType()+"','"+umc.getSequence()+"','"+umc.getCreatedate()+"',"+umc.getCloseDate()+")");
	        }else {
	        	sbSql.append("'"+umc.getOCRCheckWaitTime()+"','"+umc.getPlant()+"','"+umc.getUID()+"','"+umc.getAuditDataTime()+
		        		 "','"+umc.getType()+"','"+umc.getSequence()+"','"+umc.getCreatedate()+"','"+umc.getCloseDate()+"')");
	        }
			arraySql[i] = sbSql.toString();
			i++;
			sbSql.setLength(0);
		}
		if(i>0){
			umCheckOCRDao.batchExecute(arraySql);
		}
    }

    @Override
    public void deleteAllUrgentMaterialCheckOCR() throws Exception {
        umCheckOCRDao.execute("delete from UrgentMaterialCheckOCR");
    }
}
