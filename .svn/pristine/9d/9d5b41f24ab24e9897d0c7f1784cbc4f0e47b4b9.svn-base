package ht.biz.impl;

import ht.biz.IUrgentMaterialCheckNotOCRService;
import ht.dao.IUrgentMaterialCheckNotOCRDao;
import ht.entity.UrgentMaterialCheckNotOCR;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("umCheckNotOCRService")
public class UrgentMaterialCheckNotOCRServiceImpl implements IUrgentMaterialCheckNotOCRService{
    @Autowired
    private IUrgentMaterialCheckNotOCRDao umCheckNotOCRDao;
    
    
    public List<UrgentMaterialCheckNotOCR> findAllUrgentMaterialCheckNotOCR() {
        List<UrgentMaterialCheckNotOCR> list = umCheckNotOCRDao.listData("select * from UrgentMaterialCheckNotOCR where Type in ('A','B','C') " +
        		" and Sequence = (select max(Sequence) from UrgentMaterialCheckNotOCR) and closeDate is null  order by Type, GRN ");
        return list;
    }
    
  
    public void saveUrgentMaterialCheckNotOCR(List<UrgentMaterialCheckNotOCR> list) throws Exception {
    	StringBuilder sbSql = new StringBuilder();
		String[] arraySql = new String[list.size()];
		int i=0;
		for (UrgentMaterialCheckNotOCR umcn : list) {
			sbSql.append("insert into UrgentMaterialCheckNotOCR(GRN,ItemNumber,ReceivingLocation,RDFinishTime,ProductionTime,");
	        sbSql.append("IQCGetTime,IQCCheckWaitTime,IQCReturnTime, plant,UID,Type,Sequence,GRNDATE, createdate , closeDate");
	        sbSql.append(") values('"+umcn.getGRN()+"','"+umcn.getItemNumber()+"','"+umcn.getReceivingLocation()+"','"+umcn.getRDFinishTime()+"'");
	        sbSql.append(",'"+umcn.getProductionTime()+"','"+umcn.getIQCGetTime()+"','"+umcn.getIQCCheckWaitTime()+"','"+umcn.getIQCReturnTime()+"'");
	        sbSql.append(",'"+umcn.getPlant()+"','"+umcn.getUID()+"','"+umcn.getType()+"','"+umcn.getSequence()+"','"+umcn.getGRNDATE()+"'");
	        if(umcn.getCloseDate() == null){
	        	sbSql.append(",'"+umcn.getCreatedate()+"',null)");
	        }else {
	        	sbSql.append(",'"+umcn.getCreatedate()+"','"+umcn.getCloseDate()+"')");
	        }
			arraySql[i] = sbSql.toString();
			i++;
			sbSql.setLength(0);
		}
		if(i>0){
			umCheckNotOCRDao.batchExecute(arraySql);
		}
		
    }
    

    public void deleteAllUrgentMaterialCheckNotOCR() throws Exception {
        umCheckNotOCRDao.execute("delete from UrgentMaterialCheckNotOCR");       
    }
}
