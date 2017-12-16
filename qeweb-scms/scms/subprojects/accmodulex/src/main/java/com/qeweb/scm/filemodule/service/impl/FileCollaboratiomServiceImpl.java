package com.qeweb.scm.filemodule.service.impl;



import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.DictItemService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.filemodule.entity.FileCollaborationEntity;
import com.qeweb.scm.filemodule.entity.FileFeedbackEntity;
import com.qeweb.scm.filemodule.repository.FileCollaborationDao;
import com.qeweb.scm.filemodule.service.FileCollaborationService;
import com.qeweb.scm.filemodule.service.FileFeedbackService;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;


/**
 * 
 * 文件协同serviceImpl
 *
 */
@Service
@Transactional
public class FileCollaboratiomServiceImpl  implements FileCollaborationService{

	
	@Autowired
	private FileCollaborationDao fileCollaborationDao;
	
	@Autowired
	private FileFeedbackService fileFeedbackService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private DictItemService dictItemService;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;

	
	public Page<FileCollaborationEntity> getFileCollaboationList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<FileCollaborationEntity> spec = DynamicSpecificationsEx.bySearchFilterExNoUserData(filters.values(), FileCollaborationEntity.class);
		return fileCollaborationDao.findAll(spec,pagin);
	}

	public Page<FileFeedbackEntity> getFeedbackList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		return fileFeedbackService.getFeedbackList(pageNumber,pageSize,searchParamMap);
	}
	
	
	
	public FileCollaborationEntity getFileCollaboationByTitle(String title) {
		return fileCollaborationDao.findBytitle(title);
	}

	
	public void add(FileCollaborationEntity fileCollaboration) {	
		//通过code获取字典表对象
		DictItemEntity dictItem = 	dictItemService.findDictItemByCode(fileCollaboration.getCollaborationTypeCode());
		if(dictItem != null){
			fileCollaboration.setCollaborationType(dictItem);
		 }
		fileCollaborationDao.save(fileCollaboration);
	}

	
	public void update(FileCollaborationEntity fileCollaboration) {
		//通过code获取字典表对象
		DictItemEntity dictItem = 	dictItemService.findDictItemByCode(fileCollaboration.getCollaborationTypeCode());
		if(dictItem != null){
			fileCollaboration.setCollaborationType(dictItem);
		 }
		fileCollaborationDao.save(fileCollaboration);
	}

	
	public void delete(List<FileCollaborationEntity> fileCollaborationList) {
		fileCollaborationDao.delete(fileCollaborationList);
	}

	public FileCollaborationEntity get(Long id) {
		return fileCollaborationDao.findOne(id);
	}

	public void publish(List<FileCollaborationEntity> fileCollaborationList)throws IOException {
		Timestamp curr=DateUtil.getCurrentTimestamp();
		ShiroUser curruser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		//添加文件反馈表
		FileFeedbackEntity fileFeedback  = null;
		List<FileFeedbackEntity> fileFeedbackList =  new ArrayList<FileFeedbackEntity>();
		
		//发布的同时在添加文件回传
		for(FileCollaborationEntity  fileCollaboration : fileCollaborationList){
			fileCollaboration = fileCollaborationDao.findById(fileCollaboration.getId());
			fileCollaborationDao.publish(fileCollaboration.getId(),curr, curruser.id);

			if(fileCollaboration.getVendorIds() != null  && fileCollaboration.getVendorIds() != ""){
				//添加选择的供应商
				String [] vendorIds=fileCollaboration.getVendorIds().split(",");
				if(null!=vendorIds && vendorIds.length>0){
					for (String str : vendorIds) {
						if(!StringUtils.isEmpty(str)){
							fileFeedback = new FileFeedbackEntity();
							fileFeedback.setFileCollaboration(fileCollaboration);
							fileFeedback.setVendor(organizationDao.findOne(Long.valueOf(str)));
							fileFeedback.setBackStatus(0);
							fileFeedbackList.add(fileFeedback);
						}
					}	
				}

			}else{
				//添加所有的供应商当前采购员下的供应商
				List<Long> vendors= buyerOrgPermissionUtil.getVendorIds();
				for(Long id : vendors){
					fileFeedback = new FileFeedbackEntity();
					OrganizationEntity vendor =  organizationDao.findOne(id);
					fileFeedback.setFileCollaboration(fileCollaboration);
					fileFeedback.setVendor(vendor);
					fileFeedback.setBackStatus(0);
					fileFeedbackList.add(fileFeedback);
				}

			}
			
			//添加反馈表
			fileFeedbackService.addList(fileFeedbackList);
		}
		
	}

	public void fileFeedbackUpload(Long id, String fileName, String filePath,Blob fileContent) {
		ShiroUser curruser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		FileFeedbackEntity fileFeedback = new FileFeedbackEntity();
		Timestamp curr=DateUtil.getCurrentTimestamp();
		//查询反馈表数据
		fileFeedback = fileFeedbackService.findFileFeedbackByfileCollaborationIdAndVendorId(id, curruser.orgId);
		if(fileFeedback != null){
			fileFeedback.setFileContent(fileContent);
			fileFeedback.setFileName(fileName);
			fileFeedback.setFilePath(filePath);
			fileFeedback.setBackStatus(1);
			fileFeedback.setBackTime(curr);
			fileFeedback.setBackUser( new UserEntity(curruser.id));
		}
		fileFeedbackService.add(fileFeedback);
	}

   //定时任务修改文件协同的状态
	public boolean execute(ILogger ilogger) throws Exception {
		Boolean flag = false;
		ilogger.log("method execute start");
		
		List<FileCollaborationEntity>  fileCollaborationList = fileCollaborationDao.findAll();
		for(FileCollaborationEntity  fileCollaboration : fileCollaborationList){
			if(fileCollaboration.getAbolished().equals(0) && fileCollaboration.getPublishStatus() != 3){
				Timestamp curr=DateUtil.getCurrentTimestamp();
				if(curr.before(fileCollaboration.getValidEndTime())){
					//修改文件协同一结束
					fileCollaborationDao.closeFileCollaboration(fileCollaboration.getId());
					ilogger.log("close FileCollaboration " + fileCollaboration.getTitle());
				}
			}
		}
		
		ilogger.log("method execute end");
		
		return flag;
	}


}
