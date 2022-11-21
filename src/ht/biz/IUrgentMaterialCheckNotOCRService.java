package ht.biz;


import ht.entity.UrgentMaterialCheckNotOCR;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUrgentMaterialCheckNotOCRService {
    public List<UrgentMaterialCheckNotOCR> findAllUrgentMaterialCheckNotOCR();
    
    public void saveUrgentMaterialCheckNotOCR(List<UrgentMaterialCheckNotOCR> list) throws Exception;
    
    public void deleteAllUrgentMaterialCheckNotOCR() throws Exception;
}
