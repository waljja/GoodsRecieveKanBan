package ht.biz;


import ht.entity.ToReceiveWarehouse;
import java.util.List;


public interface IToReceiveWarehouseService {
    public List<ToReceiveWarehouse> findAllToReceiveWarehouse();
    
    public void saveToReceiveWarehouse(List<ToReceiveWarehouse> list) throws Exception;
    
    public void deleteAllToReceiveWarehouse() throws Exception;
}
