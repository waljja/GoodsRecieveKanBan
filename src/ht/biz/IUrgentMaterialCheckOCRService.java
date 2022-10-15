package ht.biz;

import ht.entity.UrgentMaterialCheckOCR;
import java.util.List;



public interface IUrgentMaterialCheckOCRService {
    public List<UrgentMaterialCheckOCR> findAllUrgentMaterialCheckOCR();
    
    public void saveUrgentMaterialCheckOCR(List<UrgentMaterialCheckOCR> list) throws Exception;
    
    public void deleteAllUrgentMaterialCheckOCR() throws Exception;
}
