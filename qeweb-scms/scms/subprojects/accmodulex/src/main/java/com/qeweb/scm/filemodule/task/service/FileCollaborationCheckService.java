package com.qeweb.scm.filemodule.task.service;
import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.filemodule.entity.FileCollaborationEntity;
import com.qeweb.scm.filemodule.repository.FileCollaborationDao;

@Service
@Transactional(rollbackOn=Exception.class)
public class FileCollaborationCheckService extends BaseService{
	
	@Autowired
	private FileCollaborationDao fileCollaborationDao;
	
	public boolean execute(ILogger iLogger) throws Exception {
		iLogger.log("method execute start");
		List<FileCollaborationEntity>  fileCollaborationList = fileCollaborationDao.findAll();
		for(FileCollaborationEntity  fileCollaboration : fileCollaborationList){
			if(fileCollaboration.getAbolished().equals(0) && fileCollaboration.getPublishStatus() != 3){
				Timestamp curr=DateUtil.getCurrentTimestamp();
				if(curr.before(fileCollaboration.getValidEndTime())){
					//修改文件协同一结束
					fileCollaborationDao.closeFileCollaboration(fileCollaboration.getId());
					iLogger.log("close FileCollaboration " + fileCollaboration.getTitle());
				}
			}
		}
		iLogger.log("method execute end");
		return true;
	}

}
