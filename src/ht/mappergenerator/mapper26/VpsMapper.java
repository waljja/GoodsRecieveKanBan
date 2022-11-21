package ht.mappergenerator.mapper26;

import ht.model.model26.Vps;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Vps mapper 接口
 *
 * @author 丁国钊
 * @date 2022-11-21
 */
@Mapper
public interface VpsMapper {
    /**
     * 根据 日期 获取 GRN 信息
     * @param currentDate
     * @param dateSub2
     * @return
     */
    List<Vps> selectGrnByDate(String currentDate, String dateSub2);
}
