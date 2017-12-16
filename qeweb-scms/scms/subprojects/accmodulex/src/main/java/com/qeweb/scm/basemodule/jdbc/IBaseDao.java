package com.qeweb.scm.basemodule.jdbc;

import java.io.Serializable;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

/**
 * Spring JDBC 持久化接口
 *
 */
public interface IBaseDao extends Serializable {
	
	public int save(String sql);
	
	public int save(String sql, Object[] parameters);
	
	public int[] batchSave(String[] sqls);
	
	public int delete(String sql);
	
	public int delete(String sql, Object[] parameters);
	
	public int update(String sql);
	
	public int update(String sql, Object[] parameters);
	
	public List<?> find(String sql);
	
	public List<?> find(String sql, Object[] args);
	
	public List<?> find(String sql, RowMapper<?> mapper);
	
	public List<?> find(String sql, Object[] args, RowMapper<?> rowMapper); 

	public Object findUniqueResult(String sql);
}
