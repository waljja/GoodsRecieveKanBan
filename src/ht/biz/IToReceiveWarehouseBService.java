package ht.biz;


import ht.entity.ToReceiveWarehouseB;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IToReceiveWarehouseBService {
    public List<ToReceiveWarehouseB> findAllToReceiveWarehouseB();
    
    public void saveToReceiveWarehouseB(List<ToReceiveWarehouseB> list) throws Exception;
    
    public void deleteAllToReceiveWarehouseB() throws Exception;
}
