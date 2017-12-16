package com.qeweb.scm.basemodule.jdbc.impl;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.qeweb.scm.basemodule.jdbc.IBaseDao;

/**
 * spring jdbc
 * @author ALEX
 *
 */
public class BaseDaoJDBCImpl extends JdbcDaoSupport implements IBaseDao {

	private static final long serialVersionUID = 2539704428694785011L;

	@Override
	public int save(String sql) {
		return getJdbcTemplate().update(sql);
	}

	@Override
	public int save(String sql, Object[] parameters) {
		return getJdbcTemplate().update(sql, parameters);
	}

	@Override
	public int[] batchSave(String[] sqls) {
		return getJdbcTemplate().batchUpdate(sqls);
	}

	@Override
	public int delete(String sql) {
		return getJdbcTemplate().update(sql);
	}

	@Override
	public int delete(String sql, Object[] parameters) {
		return getJdbcTemplate().update(sql, parameters);
	}

	@Override
	public int update(String sql) {
		return getJdbcTemplate().update(sql);
	}

	@Override
	public int update(String sql, Object[] parameters) {
		return getJdbcTemplate().update(sql, parameters);
	}

	@Override
	public List<?> find(String sql) {
		return getJdbcTemplate().queryForList(sql);
	}
	
	@Override
	public List<?> find(String sql, Object[] args) {
		return getJdbcTemplate().queryForList(sql, args);
	}

	@Override
	@SuppressWarnings("all")
	public List<?> find(String sql, RowMapper mapper) {
		return getJdbcTemplate().query(sql, mapper);
	}

	@Override
	@SuppressWarnings("all")
	public List<?> find(String sql, Object[] args, RowMapper rowMapper) {
		return getJdbcTemplate().query(sql, args, rowMapper);
	}

	@Override
	public Object findUniqueResult(String sql) {
		List<?> list = find(sql);
		return (list == null || list.size() == 0) ? null : list.get(0);
	}

}
