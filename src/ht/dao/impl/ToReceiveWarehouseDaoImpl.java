package ht.dao.impl;

import org.springframework.stereotype.Repository;

import ht.dao.IToReceiveWarehouseDao;
import ht.entity.ToReceiveWarehouse;

/**
 * @description 待入主料仓物料 看板数据库批量插入、删除实现类
 * @author 刘惠明
 * @createDate 2020-9-3
 * @version 1.0.0
 */
@Repository("trWarehouseDao")
public class ToReceiveWarehouseDaoImpl extends JdbcTemplateSupport<ToReceiveWarehouse> implements IToReceiveWarehouseDao{

}
