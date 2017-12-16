package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.convert.EasyuiTree;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.basemodule.utils.TreeUtil;

@Service
@Transactional
public class OrgService extends BaseService{
	
	@Autowired
	private OrganizationDao orgDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MailSendService mailSendService;
	
	private static final String SERNO_BY="BY_CODE";//采购商流水号
	private static final String SERNO_VE="VE_CODE";//供应商流水号
	
	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	

	public OrganizationEntity getOrg(Long id) {
		return orgDao.findOne(id);
	}
	public void saveOrg(OrganizationEntity organizationEntity) {
		orgDao.save(organizationEntity);
	}

	public Page<OrganizationEntity> getOrgs(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pageable = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String,SearchFilterEx> filterMap =  SearchFilterEx.parse(searchParamMap);
		Specification<OrganizationEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filterMap.values(), OrganizationEntity.class);
		return orgDao.findAll(spec, pageable);
	}
	
	public Page<OrganizationEntity> getBuyerListByRoleType(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		searchParamMap.put("roleType", 0);
		PageRequest pageable = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String,SearchFilterEx> filterMap =  SearchFilterEx.parse(searchParamMap);
		Specification<OrganizationEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filterMap.values(), OrganizationEntity.class);
		return orgDao.findAll(spec, pageable);
	}
	
	public List<OrganizationEntity> getOrgList(Map<String,Object> searchParamMap){
		Map<String,SearchFilterEx> filterMap = SearchFilterEx.parse(searchParamMap);
		Specification<OrganizationEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filterMap.values(), OrganizationEntity.class);
		return orgDao.findAll(spec);
	}

	public List<OrganizationEntity> getOrgListByPId(Long pId) {
		List<OrganizationEntity> orgList = null;
		if(pId==null){
			orgList = orgDao.findByParentIdIsNull();
		}else{
		 orgList = orgDao.findByParentId(pId);
		}
		return orgList;
	}

	
	public void addOrg(OrganizationEntity org) {
		//根据是否有上级组织查询
		if(org.getParentId()!=null && org.getParentId()!=0l){
			OrganizationEntity parentOrg = orgDao.findOne(org.getParentId());
			if(parentOrg!=null){
				org.setTopParentId(parentOrg.getTopParentId());
				org.setLevelDescribe(parentOrg.getLevelDescribe()+"-"+org.getParentId());
				org.setRoleType(parentOrg.getRoleType());
			}
		}
		try{
		if(StringUtils.isEmpty(org.getCode())) {
			org.setCode(getSerialNumberService().geneterNextNumberByKey(org.getOrgType().intValue()==OrgType.ROLE_TYPE_BUYER?SERNO_BY: OddNumbersConstant.VENDOR));
		}
		org.setActiveStatus(1);
		org.setEnableStatus(1);
		orgDao.save(org);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param pId
	 * @param orgType
	 * @return
	 */
	public List<OrganizationEntity> getOrgListByPIdAndOrgType(Long pId, Integer orgType) {
		List<OrganizationEntity> orgList = null;
		if(pId==null && orgType == null){
			orgList = orgDao.findByParentIdIsNullAndAbolished(pId,0);
		}else if(pId==null){
			orgList = orgDao.findByParentIdIsNullAndOrgTypeAndAbolished(orgType,0);
		}else if(pId == 0 && orgType == null){
			orgList = orgDao.findByParentIdAndAbolished(pId, 0);
		}else if(pId == 0){
			orgList = orgDao.findByParentIdAndOrgTypeAndAbolished(pId,orgType,0);
		}else if(orgType == null){
		    orgList = orgDao.findByParentIdAndAbolished(pId,0);
		}else{
			orgList = orgDao.findByParentIdAndOrgTypeAndAbolished(pId,orgType,0);
		}
		return orgList;
	}
	
	
	/**
	 * 根据角色类型查询组织
	 * @param pId
	 * @param roleType
	 * @return
	 */
	public List<OrganizationEntity> getOrgListByPIdAndRoleType(Long pId, Integer roleType) {
		List<OrganizationEntity> orgList = null;
		if(pId==null && roleType == null){
			orgList = orgDao.findByParentIdIsNullAndAbolished(pId,0);
		}else if(pId==null){
			orgList = orgDao.findByParentIdIsNullAndRoleTypeAndAbolished(roleType,0);
		}else if(pId == 0 && roleType == null){
			orgList = orgDao.findByParentIdAndAbolished(pId, 0);
		}else if(pId == 0){
			orgList = orgDao.findByParentIdAndRoleTypeAndAbolished(pId,roleType,0);
		}else if(roleType == null){
		    orgList = orgDao.findByParentIdAndAbolished(pId,0);
		}else{
			orgList = orgDao.findByParentIdAndRoleTypeAndAbolished(pId,roleType,0);
		}
		return orgList;
	}
	
	public List<OrganizationEntity> getOrgByCode(String code){
		List<OrganizationEntity> orgList = null;
		if(StringUtils.isNotEmpty(code)) {
			orgList = orgDao.findByCode(code);
		}
		return orgList;
	}
	
	public List<OrganizationEntity> getOrgByUpperCode(String code){
		List<OrganizationEntity> orgList = null;
		if(StringUtils.isNotEmpty(code)) {
			orgList = orgDao.findByUpperCode(code);
		}
		return orgList;
	}

	
	public void deleteOrgList(List<OrganizationEntity> orgList) {
		for(OrganizationEntity org : orgList){
			deleteOrg(org);
		}
		
	}

	public void deleteOrg(OrganizationEntity org) {
		List<OrganizationEntity> orgList = orgDao.findByParentId(org.getId());
		if(!CollectionUtils.isEmpty(orgList)){
			deleteOrgList(orgList);
		}
		orgDao.delete(org);
	}

	public void updateOrg(OrganizationEntity org) {
		orgDao.save(org);
		
	}

	public OrganizationEntity getByName(String orgName) {
		return orgDao.findByName(orgName);
	}

	public OrganizationEntity getByEmail(String orgEmail) {
		return orgDao.findByEmail(orgEmail);
	}

	public String backPasswordTIJ(String orgName,String orgEmail)
	{
		UserEntity userEntity=userDao.findByLoginName(orgName);
		if(userEntity==null)
		{
			return "1";//帐号不存在
		}
		else
		{
			if(userEntity.getEmail().equals(orgEmail))
			{
				byte[] salt = Digests.generateSalt(SALT_SIZE);
				userEntity.setSalt(Encodes.encodeHex(salt));
				
				byte[] Passwordbyte = Digests.sha1("123456".getBytes(),salt, HASH_INTERATIONS);
				userEntity.setPassword(Encodes.encodeHex(Passwordbyte));
				userDao.updatePassword(orgName, userEntity.getPassword(), userEntity.getSalt());
				MailObject mo = new MailObject();
				mo.toMail = orgEmail;
				mo.templateName = "backpassword";
				Map<String, String> params = new HashMap<String, String>();
				params.put("vendorName",orgName);
				params.put("curr_date",""+new Date());
				params.put("password","123456");
				mo.params = params;
				mo.title = "忘记密码邮件";
				mailSendService.send(mo, 2);
				
				return "0";
			}
			else
			{
				return "2";//邮箱错误
			}
		}
	}

	public List<OrganizationEntity> findAll() {
		return (List<OrganizationEntity>) orgDao.findAll();
	}

	public Map<String, Object> abolishBatch(List<OrganizationEntity> orgList) {
		Map<String, Object> map = new HashMap<String, Object>();
		for(OrganizationEntity org:orgList)
		{
			orgDao.abolish(org.getId());
		}
		map.put("success", true);
		return map;
	}
	public List<OrganizationEntity> getOrgListByRoleType(Integer orgRoleType) {
		return orgDao.findByRoleTypeAndAbolished(orgRoleType, StatusConstant.STATUS_NO);
	}
	public List<OrganizationEntity> getOrgListByOrgType(Integer orgTypeDeparment) {
		return orgDao.findByOrgTypeAndAbolished(orgTypeDeparment, StatusConstant.STATUS_NO);
	}
	
	

	public List<EasyuiTree> orgNodeList(Map<String, Object> searchParamMap){
		if(Long.parseLong(searchParamMap.get("EQ_parentId")==null?"0":searchParamMap.get("EQ_parentId")+"")!=0){
			Page<OrganizationEntity> userPage = getOrgs(1,10000,searchParamMap);
			List<OrganizationEntity> orgList = userPage.getContent();
			orgList = setOrgItem(orgList);
			List<EasyuiTree> orgNodeList = toEasyuiTreeBU(orgList);
			return orgNodeList;
		}
		if(OrgType.orgTreeList.size() > 0 ){
			return OrgType.orgTreeList;
		}
		Page<OrganizationEntity> userPage = getOrgs(1,10000,searchParamMap);
		List<OrganizationEntity> orgList = userPage.getContent();
		orgList = setOrgItem(orgList);
		List<EasyuiTree> orgNodeList = toEasyuiTreeBU(orgList);
		OrgType.orgTreeList = orgNodeList;
		return orgNodeList;
	}
	/**
	 * 递归
	 * @param orgList
	 * @return
	 */
	public List<OrganizationEntity> setOrgItem(List<OrganizationEntity> orgList){
		for (int i = 0; i < orgList.size(); i++) {
			Long orgid = orgList.get(i).getId();
			List<OrganizationEntity> child = getOrgListByPId(orgid);
			orgList.get(i).setItemList(child);
			setOrgItem(child);
		}
		return orgList;
	}
	
	
	/**
	 * 将组织部门转换成easyuitree
	 * @param orgList 组织部门集合
	 * @return 树集合
	 */
	private List<EasyuiTree> toEasyuiTreeBU(List<OrganizationEntity> orgList) {
		if(CollectionUtils.isEmpty(orgList)){
			return null;
		}
		List<EasyuiTree> nodeList = new ArrayList<EasyuiTree>();
		for(OrganizationEntity org : orgList){
			EasyuiTree treeNode = toEasyuiTreeNodeBU(org);
			nodeList.add(treeNode);
		}
		return nodeList;
	}

	private EasyuiTree toEasyuiTreeNodeBU(OrganizationEntity org){
		//定义icon 
		String icon = "icon-sitemap_color";
		if(org.getParentId()!=null && org.getParentId()!=0l) {
			icon = "icon-group";
		}
		Map<String,Object> attributes = new HashMap<String,Object>();
		attributes.put("orgType", org.getOrgType());
		attributes.put("parentId", org.getParentId());
		if(CollectionUtils.isEmpty(org.getItemList())){
			attributes.put("leaf", true);
		}
		EasyuiTree treeNode = new EasyuiTree(org.getId()+"", org.getName(), "closed", "",icon, null, attributes);
		if(!CollectionUtils.isEmpty(org.getItemList())){
			List<EasyuiTree> children = new ArrayList<EasyuiTree>();
			for(OrganizationEntity orgNode : org.getItemList()){
				EasyuiTree treeItem = toEasyuiTreeNodeBU(orgNode);
				children.add(treeItem);
			}
			treeNode.setChildren(children);
		}
		return treeNode;
	}
	public List<OrganizationEntity> reLoad(List<OrganizationEntity> orgs) {
		for(OrganizationEntity org:orgs){
			if(org.getParentId()!=null&&org.getParentId()!=0l){
				OrganizationEntity parentOrg = orgDao.findOne(org.getParentId());
				org.setParentOrgName(parentOrg.getName());
			}
		}
		return orgs;
	}
}
