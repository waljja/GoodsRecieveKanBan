package ht.biz;


import ht.entity.UrgentMaterialCheckNotOCR;
import java.util.List;


public interface IUrgentMaterialCheckNotOCRService {
    public List<UrgentMaterialCheckNotOCR> findAllUrgentMaterialCheckNotOCR();
    
    public void saveUrgentMaterialCheckNotOCR(List<UrgentMaterialCheckNotOCR> list) throws Exception;
    
    public void deleteAllUrgentMaterialCheckNotOCR() throws Exception;
}
