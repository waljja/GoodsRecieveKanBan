package ht.mappergenerator;

import ht.model.AegisModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 丁国钊
 * @date 2022-11-20
 */
@Mapper
public interface AegisMapper {
    /**
     * 根据 UID 查找 Aegis 库存信息
     * @param identifier
     * @return
     */
    List<AegisModel> selectStockByIdentifier(String identifier);
}
