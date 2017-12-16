package com.qeweb.scm.purchasemodule.service;
import javax.transaction.Transactional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.purchasemodule.entity.FileUpEntity;
import com.qeweb.scm.purchasemodule.repository.FileUpDao;

/**
 * 
 *文件，弃用
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class FileUpService{
	
	@Autowired
	private FileUpDao fileUpDao;
	
	public FileUpEntity getFileUpEntity(Long id) {
		return fileUpDao.findOne(id);
	}
	
	public FileUpEntity getByDesriptIdAndReceiveId(Long desriptId,Long receiveId){
		return fileUpDao.findByDesriptIdAndReceiveId(desriptId,receiveId);
	}
}
