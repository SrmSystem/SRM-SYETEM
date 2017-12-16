package com.qeweb.scm.basemodule.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.basemodule.entity.ColumnSettingEntity;
import com.qeweb.scm.basemodule.repository.ColumnSettingDao;

@Service
@Transactional
public class ColumnSettingService {
	
	@Autowired
	private ColumnSettingDao dao;

	public ColumnSettingEntity getColumnSetting(Long id) {
		return dao.findOne(id);
	}

	public ColumnSettingEntity getColumnSettingByUserAndPathAndTable(Long userId,String path,String table) {
		return dao.findByUserIdAndPathAndTable(userId, path, table);
	}

	public void add(ColumnSettingEntity en){
		dao.save(en);
	}
	
	public void update(ColumnSettingEntity en){
		dao.save(en);
	}

	public ColumnSettingDao getDao() {
		return dao;
	}

	public void setDao(ColumnSettingDao dao) {
		this.dao = dao;
	}
	
	
}
