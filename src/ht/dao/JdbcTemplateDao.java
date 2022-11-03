package ht.dao;

import ht.util.QueryResult;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * 通用JdbcDAO接口 （Spring JdbcTemplate）
 * 
 * @author michellechen
 *
 * @param <T>
 */

public interface JdbcTemplateDao<T> {
	/**
	 * 分页查询（SQLServer2008, 只适合有标示符的表）
	 * 
	 * @param tableName
	 *            - 表的名称
	 * @param tableName
	 *            - 标示符
	 * @param pageindex
	 *            - 要查询的页码
	 * @param maxresult
	 *            - 需要获取的记录数
	 * @param condition
	 *            - 查询条件
	 * @param orderby
	 *            - 排序
	 * @param rowMapper
	 *            - 解析器
	 * @return
	 */
	public QueryResult<T> pagingDataOnSQLServer2008(String tableName, String identify, int pageindex, int maxresult,
			String condition, LinkedHashMap<String, String> orderby, RowMapper rowMapper);

	/**
	 * 分页查询（SQLServer2008, 只适合有标示符的表）
	 * 
	 * @param tableName
	 *            - 表的名称
	 * @param tableName
	 *            - 标示符
	 * @param pageindex
	 *            - 要查询的页码
	 * @param maxresult
	 *            - 需要获取的记录数
	 * @param condition
	 *            - 查询条件
	 * @param orderby
	 *            - 排序
	 * @param rowMapper
	 *            - 解析器
	 * @return
	 */
	public QueryResult<T> pagingDataOnSQLServer(String tableName, int pageindex, int maxresult,
			String condition, LinkedHashMap<String, String> orderby, RowMapper rowMapper);
	
	/**
	 * 查询
	 * 
	 * @param tableName
	 *            - 表的名称
	 * @param condition
	 *            - 查询条件
	 * @param orderby
	 *            - 排序
	 * @param rowMapper
	 *            - 解析器
	 * @return
	 */
	public List<T> listData(String tableName, String condition, LinkedHashMap<String, String> orderby,
			RowMapper rowMapper);
	
	/**
	 * 查询自定义字段
	 * 	 
	 * @return
	 */
	public List<T> listData(String sql);
	
	/**
	 * 自定义select语句，一般用于多表查询时
	 * 
	 * @param sql
	 * @return
	 */
	public ResultSet listForJdbcCustom(String sql);
	
	/**
	 * insert or update method
	 * 
	 * @param sql
	 *            - sql语句
	 * @return
	 */
	public void execute(String sql);
  
	/**
	 * 有返回值的存储过程（非结果集）
	 * 
	 * @param creator
	 * @param callback
	 * @return
	 */
	public Object execute(CallableStatementCreator creator, CallableStatementCallback callback);
	
	/**
	 * 插入记录并返回自动生成的主键Id(MySQL中不行，Oracle可以)
	 * 
	 * @param sql
	 *            - sql语句
	 * @return
	 */
	public int insertData(String sql);

	/**
	 * @description jdbc批量SQL执行
	 * @param sql
	 */
	public int[] batchExecute(String[] sql);

	/**
	 * @description 查询数据
	 * @param sql
	 * @param rowMapper
	 * @return
	 */
	public List<T> listData(String sql, RowMapper rowMapper);
}
