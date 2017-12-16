package com.qeweb.scm.purchasemodule.service;
import java.util.ArrayList;



import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.purchasemodule.entity.FileDescriptEntity;
import com.qeweb.scm.purchasemodule.repository.FileDescriptDao;


/**
 *文件，弃用
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class FileDescriptService{
	
	@Autowired
	private FileDescriptDao fileDescriptDao;
	
	public FileDescriptEntity getFileDescriptEntity(Long id) {
		return fileDescriptDao.findOne(id);
	}
	
	public List<FileDescriptEntity> getAllEntity(){
		List<FileDescriptEntity> entityList=new ArrayList<FileDescriptEntity>();
		return (List<FileDescriptEntity>) fileDescriptDao.findAll();
	}
}
