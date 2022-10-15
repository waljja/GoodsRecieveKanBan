package ht.dao.impl;

import org.springframework.stereotype.Repository;

import ht.dao.IToReceiveCheckDao;
import ht.entity.ToReceiveCheck;


@Repository("trCheckDao")
public class ToReceiveCheckDaoImpl extends JdbcTemplateSupport<ToReceiveCheck> implements IToReceiveCheckDao{

}
