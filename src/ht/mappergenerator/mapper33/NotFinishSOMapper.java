package ht.mappergenerator.mapper33;

import ht.model.model33.NotFinishSO;
import ht.model.model33.NotFinishSOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NotFinishSOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int countByExample(NotFinishSOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int deleteByExample(NotFinishSOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int insert(NotFinishSO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int insertSelective(NotFinishSO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    List<NotFinishSO> selectByExample(NotFinishSOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    NotFinishSO selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int updateByExampleSelective(@Param("record") NotFinishSO record, @Param("example") NotFinishSOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int updateByExample(@Param("record") NotFinishSO record, @Param("example") NotFinishSOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int updateByPrimaryKeySelective(NotFinishSO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table NotFinishSO
     *
     * @mbggenerated Mon Nov 21 21:52:33 CST 2022
     */
    int updateByPrimaryKey(NotFinishSO record);
}