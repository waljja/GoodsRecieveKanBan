package ht.mappergenerator.mapper33;

import ht.model.model33.ToReceiveWarehouse;
import ht.model.model33.ToReceiveWarehouseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ToReceiveWarehouseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int countByExample(ToReceiveWarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int deleteByExample(ToReceiveWarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int insert(ToReceiveWarehouse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int insertSelective(ToReceiveWarehouse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    List<ToReceiveWarehouse> selectByExample(ToReceiveWarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    ToReceiveWarehouse selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int updateByExampleSelective(@Param("record") ToReceiveWarehouse record, @Param("example") ToReceiveWarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int updateByExample(@Param("record") ToReceiveWarehouse record, @Param("example") ToReceiveWarehouseExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int updateByPrimaryKeySelective(ToReceiveWarehouse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ToReceiveWarehouse
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int updateByPrimaryKey(ToReceiveWarehouse record);
}