package ht.biz;

import ht.entity.ToReceiveCheck;

import java.io.InputStream;
import java.util.List;


public interface IToReceiveCheckService {
    public List<ToReceiveCheck> findAllToReceiveCheck() throws Exception;

    public void saveToReceiveCheck(List<ToReceiveCheck> list) throws Exception;
    
    public void deleteAllToReceiveCheck() throws Exception;
    
    public InputStream doExport() throws Exception;
    
    
}
