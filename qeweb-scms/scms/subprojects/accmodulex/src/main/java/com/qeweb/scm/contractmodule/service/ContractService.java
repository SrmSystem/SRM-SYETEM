package com.qeweb.scm.contractmodule.service;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.contractmodule.ContractConstant;
import com.qeweb.scm.contractmodule.entity.ContractContentEntity;
import com.qeweb.scm.contractmodule.entity.ContractEntity;
import com.qeweb.scm.contractmodule.entity.ContractExtEntity;
import com.qeweb.scm.contractmodule.entity.ContractFilesEntity;
import com.qeweb.scm.contractmodule.entity.ContractItemEntity;
import com.qeweb.scm.contractmodule.entity.ContractModuleItemEntity;
import com.qeweb.scm.contractmodule.repository.ContractContentDao;
import com.qeweb.scm.contractmodule.repository.ContractDao;
import com.qeweb.scm.contractmodule.repository.ContractExtDao;
import com.qeweb.scm.contractmodule.repository.ContractFilesDao;
import com.qeweb.scm.contractmodule.repository.ContractItemDao;
import com.qeweb.scm.contractmodule.repository.ContractModuleItemDao;
@Service
@Transactional(rollbackOn=Exception.class)
public class ContractService extends BaseService{
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ContractItemDao contractItemDao;
	
	@Autowired
	private ContractFilesDao contractFilesDao;
	
	@Autowired
	private ContractContentDao contractContentDao;
	
	@Autowired
	private ContractModuleItemDao contractModuleItemDao;
	
	@Autowired
	private RoleUserDao roleUserDao;
	
	private final static String CODE_KEY = "CONTRACT";  
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ContractExtDao contractExtDao;
	
	
	

	 
	
	 
	public void sendMail(String email,String vendorName,String title,String message) {
		String link = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();  		//发送邮件时的网址
		MailSendService mailSendService=new MailSendService();
		MailObject mo = new MailObject();
		mo.toMail = email;
		mo.templateName = "defaultTemp";
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorName",vendorName);
		params.put("link",link);
		params.put("tempMessage",message);
		params.put("signText","邮件发送");
		mo.params = params;
		mo.title = title;
		mailSendService.send(mo, 2);
}
	
	
	public ContractEntity findOne(Long id) {
		return contractDao.findOne(id);
	}
	
	public ContractFilesEntity findContractFile(Long busId){
		List<ContractFilesEntity> files=contractFilesDao.findByBusIdAndBusTypeOrderByCreateTimeDesc(busId, ContractConstant.CONTRACT_FILE);;
		return files.size()>0?files.get(0):new ContractFilesEntity();
	}
 	
	public ContractFilesEntity findContractSealFile(Long busId){
		List<ContractFilesEntity> files=contractFilesDao.findByBusIdAndBusTypeOrderByCreateTimeDesc(busId, ContractConstant.CONTRACT_SEAL_FILE);;
		return files.size()>0?files.get(0):new ContractFilesEntity();
	}
	
	public ContractFilesEntity findContractConfirmFile(Long busId){
		List<ContractFilesEntity> files=contractFilesDao.findByBusIdAndBusTypeOrderByCreateTimeDesc(busId, ContractConstant.CONTRACT_CONFIRM_FILE);;
		return files.size()>0?files.get(0):new ContractFilesEntity();
	}
	
	public Page<ContractEntity> getList(int pageNumber , int pageSize, Map<String, Object> searchParamMap){
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ContractEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ContractEntity.class);
		Page<ContractEntity> page = contractDao.findAll(spec, pagin);
		return page;
	}
	
	/**
	 * 创建模板的编号
	 */
	public String createContractCode(){
		String contractCode = getSerialNumberService().geneterNextNumberByKey(CODE_KEY);
		return contractCode;
	}
	
	
	public void saveContract(Long moduleId,List<ContractItemEntity> itemList,ContractEntity po,File f,String fileName) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Long contractId=null;
		if(po.getId()<=0){
			if(f!=null){
				po.setHasAttachement(1);
			}
			po.setModuleId(moduleId);
			contractDao.save(po);	
			contractId=po.getId();
			
			Map<Long, Long> map=new HashMap<Long, Long>();
			//拷贝模板条款
			if(moduleId!=null&&moduleId>0){
				List<ContractModuleItemEntity> moduleList=contractModuleItemDao.getItemByModuleId(moduleId, 0);
				List<ContractContentEntity> saveList=new ArrayList<ContractContentEntity>();
				for (ContractModuleItemEntity contractModuleItemEntity : moduleList) {
					ContractContentEntity content=new ContractContentEntity();
					BeanUtil.copyPropertyNotNull(contractModuleItemEntity, content);
					content.setContractId(contractId);
					content.setId(0);
					content.setBuyerId(user.orgId);
					contractContentDao.save(content);
					map.put(contractModuleItemEntity.getId(), content.getId());
					saveList.add(content);
				}
				for (ContractContentEntity content : saveList) {
					if(content.getParentId()!=null&&content.getParentId()>0){
						content.setParentId(map.get(content.getParentId()));
						contractContentDao.save(content);
					}
				}
			}
			
		}else{
			ContractEntity old=contractDao.findOne(po.getId());
			if(f!=null){
				old.setHasAttachement(1);
			}
			BeanUtil.copyPropertyNotNull(po, old);
			contractDao.save(old);
			contractId=old.getId();
			//先删除当前所有条款
//			List<ContractContentEntity> res=contractContentDao.getItemByContractId(contractId,0);
//			contractContentDao.delete(res);
		}
		if(moduleId>0){
			if(po.getId()<=0){
				for (ContractItemEntity item : itemList) {
					item.setContractId(contractId);
					item.setContractType(0);
					contractItemDao.save(item);
				}
			}else{
				Map<Long, ContractItemEntity> oldMap=new HashMap();
				Map<Long, ContractItemEntity> newMap=new HashMap();
				List<ContractItemEntity> oldItems=contractItemDao.findByContractIdAndAbolished(contractId, 0);
				for (ContractItemEntity contractItemEntity : oldItems) {
					oldMap.put(contractItemEntity.getId(), contractItemEntity);
				}
				for (ContractItemEntity item : itemList) {
					if(item.getId()<=0){
						item.setContractId(contractId);
						item.setContractType(0);
						contractItemDao.save(item);
					}else{
						ContractItemEntity updateItem= oldMap.get(item.getId());
						BeanUtil.copyPropertyNotNull(item, updateItem);
						contractItemDao.save(updateItem);
						newMap.put(item.getId(), item);
					}
				}
				for(Long itemId : oldMap.keySet()){
					if(!newMap.containsKey(itemId)){
						contractItemDao.delete(itemId);
					}
				}
			}
		}
		
		if(f!=null){
			ContractFilesEntity pf=new ContractFilesEntity();
			pf.setFileName(fileName);
			pf.setFilePath(f.getPath());
			pf.setBusId(contractId);
			pf.setBusType(ContractConstant.CONTRACT_FILE);
			contractFilesDao.save(pf);
		}
	}

	/**
	 * 提交审核
	 * @param contractId
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public String checkContract(Long contractId) throws RemoteException {
		List<String> sapResult=null;
		ContractEntity contract=contractDao.findOne(contractId);
		List<ContractItemEntity> items=contractItemDao.findByContractIdAndAbolished(contract.getId(), 0);
		List<ContractExtEntity> extList=contractExtDao.findByContractIdOrderByCreateTimeDesc(contract.getId());
		ContractExtEntity contractExt=null;
		if(extList==null||extList.size()<=0){
		}else{
			contractExt=extList.get(0);
		}
		

			//contract.setAuditStatus(ContractConstant.AUDIT_STATUS_WAIT);
		
		contract.setAuditStatus(ContractConstant.AUDIT_STATUS_PASS);
		contract.setAuditTime(DateUtil.getCurrentTimestamp());
			contractDao.save(contract);
		
		return "";
	}

	/**
	 * 发布
	 * @param contractId
	 */
	public void publishContract(Long contractId) {
		ContractEntity contract=contractDao.findOne(contractId);
		contract.setPublishStatus(ContractConstant.PUBLISH_STATUS_YES);
		contract.setPublishTime(DateUtil.getCurrentTimestamp());
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		contract.setPublishUserIdFk(user.id);
		contract.setConfirmStatus(ContractConstant.CONFIRM_STATUS_NO);
		contractDao.save(contract);
		if(!StringUtils.isEmpty(contract.getVendor().getEmail())){
			sendMail(contract.getVendor().getEmail(),contract.getVendor().getName(), "合同发布", "有与您合作的新合同等待您处理，请登录系统及时处理,合同编号为"+contract.getCode()+"!");
		}
	}
	
	
	/**
	 * 供应商确认
	 * @param contractId
	 * @param confirmStatus
	 */
	public void confirmContract(Long contractId,int confirmStatus) {
		ContractEntity contract=contractDao.findOne(contractId);
		contract.setConfirmStatus(confirmStatus);
		contract.setConfirmTime(DateUtil.getCurrentTimestamp());
		ShiroUser userx = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		contract.setConfirmUserId(userx.id);
		String title="";
		String content="";
		if(ContractConstant.CONFIRM_STATUS_REJECT==confirmStatus){
			contract.setPublishStatus(ContractConstant.PUBLISH_STATUS_NO);
			contract.setAuditStatus(ContractConstant.AUDIT_STATUS_NO);
			title="供应商驳回合同";
			content="您创建的合同，"+contract.getCode()+"已经被供应驳回，请处理";
		}else{
			title="供应商确认合同";
			content="您创建的合同，"+contract.getCode()+"已经被供应商确认，请处理";
		}
		contractDao.save(contract);
		
		UserEntity user=userDao.findOne(contract.getCreateUserId());
		if(!StringUtils.isEmpty(user.getEmail())){
			sendMail(user.getEmail(), user.getName(), title, content);
		}
	}
	
	/**
	 * 确认盖章附件，生效
	 * @param contractId
	 * @param confirmStatus
	 */
	public void effectiveContract(Long contractId,int confirmStatus) {
		ContractEntity contract=contractDao.findOne(contractId);
		contract.setSealAttConfirmStatus(confirmStatus);//盖章附件确认状态
		String title="";
		String content="";
		if(ContractConstant.FILE_CONFIRM_STATUS_PASS==confirmStatus){
			contract.setEnabledStatus(ContractConstant.EFFECTIVE_STATUS_YES);
			ShiroUser userx = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			contract.setEnabledTime(DateUtil.getCurrentTimestamp());
			contract.setEnabledUserIdFk(userx.id);
			title="合同生效";
			content="签订的合同已经生效,合同编号为"+contract.getCode()+"!";
		}else{
			contract.setHasSealAttchement(ContractConstant.FILE_STATUS_NO);
			title="合同盖章附件驳回";
			content="合同盖章附件驳回,请重新上传盖章扫描件,合同编号为"+contract.getCode()+"!";
		}
		contractDao.save(contract);
		if(!StringUtils.isEmpty(contract.getVendor().getEmail())){
			sendMail(contract.getVendor().getEmail(), contract.getVendor().getName(), title, content);
		}
	}
	
	/**
	 * 上传盖章附件
	 * @param contractId
	 * @param f
	 * @param fileName
	 */
	public void saveSealFile(Long contractId,File f,String fileName){
		ContractEntity contract=contractDao.findOne(contractId);
		contract.setHasSealAttchement(ContractConstant.FILE_STATUS_YES);
		contract.setSealAttConfirmStatus(ContractConstant.FILE_CONFIRM_STATUS_NO);
		contractDao.save(contract);

		ContractFilesEntity file=new ContractFilesEntity();
		file.setFileName(fileName);
		file.setFilePath(f.getPath());
		file.setBusId(contractId);
		file.setBusType(ContractConstant.CONTRACT_SEAL_FILE);
		contractFilesDao.save(file);
	}
	
	/**
	 * 供应商确认
	 * @param contractId
	 * @param f
	 * @param fileName
	 */
	public void saveConfirmFile(Long contractId,File f,String fileName){
		ContractEntity contract=contractDao.findOne(contractId);
		contract.setConfirmStatus(ContractConstant.CONFIRM_STATUS_PASS);
		contract.setConfirmTime(DateUtil.getCurrentTimestamp());
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		contract.setConfirmUserId(user.id);
		contractDao.save(contract);

		ContractFilesEntity file=new ContractFilesEntity();
		file.setFileName(fileName);
		file.setFilePath(f.getPath());
		file.setBusId(contractId);
		file.setBusType(ContractConstant.CONTRACT_CONFIRM_FILE);
		contractFilesDao.save(file);
	}

	/**
	 * 删除合同
	 * @param list
	 */
	public void deleteContract(List<ContractEntity> list) {
		for (ContractEntity contractEntity : list) {
			List<ContractItemEntity> items=contractItemDao.findByContractIdAndAbolished(contractEntity.getId(), 0);
			contractItemDao.delete(items);
			List<ContractContentEntity> contents=contractContentDao.getItemByContractId(contractEntity.getId(),0);
			contractContentDao.delete(contents);
		}
		contractDao.delete(list);
	}

	/**
	 * 失效合同
	 * @param id
	 */
	public void disableContract(Long id) {
		ContractEntity contract=contractDao.findOne(id);
		ShiroUser userx = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		contract.setEnabledTime(DateUtil.getCurrentTimestamp());
		contract.setEnabledUserIdFk(userx.id);
		contract.setEnabledStatus(ContractConstant.EFFECTIVE_STATUS_DEL);
	}
	
	/**
	 * 根据角色的Code查找到属于该角色的用户
	 * @param roleCode
	 * @return
	 */
	public Page<UserEntity> getByRoleCode(int pageNumber, int number,String roleCode){
		PageRequest pagin = new PageRequest(pageNumber-1, number);
		return roleUserDao.findAllByPage(pagin, "CountersignPerson");
	}
	
	public Page<UserEntity> getAllUser(int pageNumber, int number,String roleCode){
		PageRequest pagin = new PageRequest(pageNumber-1, number);
		return userDao.findAll(pagin);
	}
	

}
