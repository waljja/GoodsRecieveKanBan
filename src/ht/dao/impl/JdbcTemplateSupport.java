package ht.dao.impl;

import ht.dao.JdbcTemplateDao;
import ht.util.QueryResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class JdbcTemplateSupport<T> implements JdbcTemplateDao<T>
{
	private static final Logger log = LoggerFactory.getLogger(JdbcTemplateSupport.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Connection conn = null;
	private PreparedStatement pstm = null;
	private ResultSet rs = null;
	
	/**
	 * 获取JdbcTemplate
	 * 
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}

	@Override
	public QueryResult<T> pagingDataOnSQLServer2008(String tableName, String identify, int pageindex, int maxresult, String condition,
			LinkedHashMap<String, String> orderby, RowMapper rowMapper){
		String where = (condition==null||condition==""||"".equals(condition) ? "" : " WHERE " + condition) + "";
		String order = buildOrderby(orderby);
		String sql = "SELECT TOP " + maxresult + " * FROM " + tableName + ("".equals(where)?" WHERE ":where + " and ") + identify + " not in (SELECT TOP " + maxresult
				* (pageindex - 1) + " " + identify + " FROM " + tableName + where + order + ") ";				
		sql += (condition==null||condition==""||"".equals(condition)?"":"and "+condition) + order;
		String countSql = "SELECT count(*) FROM " + tableName + where;
		QueryResult<T> qr = new QueryResult<T>();
		try{
			log.debug("jdbcTemplate sql : {}", sql);
			List<T> result = jdbcTemplate.query(sql, rowMapper);
			long count = jdbcTemplate.queryForLong(countSql);
			qr.setResultlist(result);
			qr.setTotalrecord(count);

			return qr;
		}catch (RuntimeException e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public QueryResult<T> pagingDataOnSQLServer(String tableName, int pageindex, int maxresult, String condition,
			LinkedHashMap<String, String> orderby, RowMapper rowMapper){
		String where = (null==condition||condition==""||"".equals(condition) ? "" : "" + condition) + "";
		String order = buildOrderby(orderby);
		String sql = "(SELECT TOP " + maxresult*pageindex + " * FROM " + tableName + " WHERE " + where + ") ";
		sql += "EXCEPT ";
		sql += "(SELECT TOP " + maxresult*(pageindex - 1) + " * FROM " + tableName + " WHERE " + where + ")";
		sql += order;
		String countSql = "SELECT count(*) FROM " + tableName + " WHERE " + where;
		QueryResult<T> qr = new QueryResult<T>();
		try{
			log.debug("jdbcTemplate sql : {}", sql);
			List<T> result = jdbcTemplate.query(sql, rowMapper);
			long count = jdbcTemplate.queryForLong(countSql);
			qr.setResultlist(result);
			qr.setTotalrecord(count);

			return qr;
		}catch (RuntimeException e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<T> listData(String tableName, String condition, LinkedHashMap<String, String> orderby, RowMapper rowMapper){
		String order = buildOrderby(orderby);
		String sql = "SELECT * FROM " + tableName;
		if(condition!=null&&condition!=""&&!"".equals(condition)){
			sql += " WHERE " + condition;
		}
		sql += order;
		List<T> result = jdbcTemplate.query(sql, rowMapper);
		return result;
	}

	@Override
	public List<T> listData(String sql){		
		List result = jdbcTemplate.queryForList(sql);
		return result;
	}

	@Override
	public ResultSet listForJdbcCustom(String sql){
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			pstm = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstm.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public void execute(String sql){
		jdbcTemplate.execute(sql);
	}

	@Override
	public Object execute(CallableStatementCreator creator, CallableStatementCallback callback){
		return jdbcTemplate.execute(creator, callback);
	}

	@Override
	public int insertData(String sql) {
		// TODO Auto-generated method stub
		final String v_sql = sql;
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		jdbcTemplate.update(new PreparedStatementCreator(){ 
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException { 
				PreparedStatement ps=con.prepareStatement(v_sql, Statement.RETURN_GENERATED_KEYS); 
				return ps; 
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	protected static String buildOrderby(LinkedHashMap<String, String> orderby) {
		StringBuffer orderbyql = new StringBuffer("");
		if (orderby != null && orderby.size() > 0) {
			orderbyql.append(" order by ");
			for (String key : orderby.keySet()){
				orderbyql.append(key).append(" ").append(orderby.get(key)).append(",");
			}
			orderbyql.deleteCharAt(orderbyql.length() - 1);
		}
		return orderbyql.toString();
	}
	
	public void close(){
		try {
			if (rs!=null) {
				rs.close();
			}
			if (pstm!=null) {
				pstm.close();
			}
			if (conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int[] batchExecute(String[] sql) {
		return jdbcTemplate.batchUpdate(sql);
	}

	@Override
	public List<T> listData(String sql, RowMapper rowMapper){
		List<T> result = jdbcTemplate.query(sql, rowMapper);
		return result;
	}
}
