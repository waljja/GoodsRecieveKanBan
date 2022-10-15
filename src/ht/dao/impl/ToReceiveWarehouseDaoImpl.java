package ht.dao.impl;

import org.springframework.stereotype.Repository;

import ht.dao.IToReceiveWarehouseDao;
import ht.entity.ToReceiveWarehouse;

@Repository("trWarehouseDao")
public class ToReceiveWarehouseDaoImpl extends JdbcTemplateSupport<ToReceiveWarehouse> implements IToReceiveWarehouseDao{

}
