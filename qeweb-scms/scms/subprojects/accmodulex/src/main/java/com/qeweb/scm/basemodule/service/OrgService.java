package com.qeweb.scm.basemodule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.qeweb.sap.VendorSAP;
import com.qeweb.scm.baseline.common.service.BuyerOrgPermissionUtil;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.convert.EasyuiTree;
import com.qeweb.scm.basemodule.entity.CompanyOrganizationRelEntity;
import com.qeweb.scm.basemodule.entity.FactoryOrganizationRelEntity;
import com.qeweb.scm.basemodule.entity.GroupOrganizationRelEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.repository.CompanyOrganizationRelDao;
import com.qeweb.scm.basemodule.repository.FactoryOrganizationRelDao;
import com.qeweb.scm.basemodule.repository.GroupOrganizationRelDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
@Service
@Transactional
public class OrgService extends BaseService{
	
	@Autowired
	private OrganizationDao orgDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MailSendService mailSendService;
	
	@Autowired
	private GenerialDao generialDao;
	
	@Autowired
	private FactoryOrganizationRelDao factoryOrganizationRelDao;
	
	@Autowired
	private GroupOrganizationRelDao groupOrganizationRelDao;
	
	@Autowired
	private CompanyOrganizationRelDao companyOrganizationRelDao;
	
	@Autowired
	private BuyerOrgPermissionUtil buyerOrgPermissionUtil;
	
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
	
	public List<OrganizationEntity> getOrgsByBuyer(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Long buyerId) {
		List<OrganizationEntity> list= (List<OrganizationEntity>) generialDao.querybyhql(getOrgListHql(searchParamMap,buyerId), pageNumber, pageSize);
		return list;
	}
	
	public Integer getOrgListCount( Map<String, Object> searchParamMap,Long buyerId) {
		return generialDao.findCountByHql(getOrgListCountHql(searchParamMap,buyerId));
	}
	
	public String getOrgListHql(Map<String, Object> searchParamMap,Long buyerId){
		StringBuilder sql=new StringBuilder();
		sql.append("select distinct org  from BuyerVendorRelEntity  rel,OrganizationEntity org where rel.vendorId =org.id ");
		sql.append(" and rel.buyerId =").append(buyerId);
		for (String key : searchParamMap.keySet()) {
			String value=String.valueOf(searchParamMap.get(key));
			if(!StringUtils.isEmpty(value)){
				String[] keyList= key.split("_");
				if("EQ".equals(keyList[0])){
					sql.append(" and org.").append(keyList[1]).append(" = ").append(value);
				}else if("LIKE".equals(keyList[0])){
					sql.append(" and org.").append(keyList[1]).append(" like '%").append(value).append("%'");
				}
			}
		}
		return sql.toString();
	}
	public String getOrgListCountHql(Map<String, Object> searchParamMap,Long buyerId){
		StringBuilder sql=new StringBuilder();
		sql.append("select count(distinct org)  from BuyerVendorRelEntity  rel,OrganizationEntity org where rel.vendorId =org.id ");
		sql.append(" and rel.buyerId =").append(buyerId);
		for (String key : searchParamMap.keySet()) {
			String value=String.valueOf(searchParamMap.get(key));
			if(!StringUtils.isEmpty(value)){
				String[] keyList= key.split("_");
				if("EQ".equals(keyList[0])){
					sql.append(" and org.").append(keyList[1]).append(" = ").append(value);
				}else if("LIKE".equals(keyList[0])){
					sql.append(" and org.").append(keyList[1]).append(" like '%").append(value).append("%'");
				}
			}
		}
		return sql.toString();
	}
	
	public Page<OrganizationEntity> getBuyerListByRoleType(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		searchParamMap.put("roleType", 0);
		PageRequest pageable = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String,SearchFilterEx> filterMap =  SearchFilterEx.parse(searchParamMap);
		Specification<OrganizationEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filterMap.values(), OrganizationEntity.class);
		return orgDao.findAll(spec, pageable);
	}
	
	public List<OrganizationEntity> getBuyerListByRoleType1() {
		return orgDao.getBuyerListByRoleType1();
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
		org.setEnableStatus(1);
		org.setAbolished(StatusConstant.STATUS_NO);
		orgDao.save(org);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public String openVendor(OrganizationEntity org) {
		String msg="";
		try{
			List<String> res=VendorSAP.getVendorByCode(org.getCode());
			if(res.size()<2){
				msg="获取供应商接口异常";
				return msg;
			}
			if(StringUtils.isEmpty(res.get(0))||StringUtils.isEmpty(res.get(1))){
				msg="获取供应商接口异常";
				return msg;
			}
			org.setName(res.get(0));
			org.setMidCode(res.get(1));
			//再反写给sap开通的供应商
			msg=VendorSAP.returnVendor(org);
			if(!StringUtils.isEmpty(msg)){
				return msg;
			}
			org.setEnableStatus(1);
			org.setActiveStatus(1);
			org.setRoleType(OrgType.ROLE_TYPE_VENDOR);
			org.setOrgType(OrgType.ORG_TYPE_COMPANY);
			org.setConfirmStatus(StatusConstant.STATUS_YES);
			org.setParentId(0L);
			org.setTopParentId(0L);
			org.setAbolished(StatusConstant.STATUS_NO);
			orgDao.save(org);
		}catch(Exception e){
			msg="获取供应商接口异常";
			e.printStackTrace();
		}
		return msg;
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
		//orgDao.delete(org);
		orgDao.abolish(org.getId());
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
	
	public List<OrganizationEntity> findEffective() {
		return orgDao.findByAbolished(0);
	}


	public Map<String, Object> abolishBatch(List<OrganizationEntity> orgList) {
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = "";
		Boolean flag = true;
		
		for(OrganizationEntity org:orgList){
			List<FactoryOrganizationRelEntity> foList = factoryOrganizationRelDao.findByOrgIdAndAbolished(org.getId(), 0);
			List<GroupOrganizationRelEntity> goList = groupOrganizationRelDao.findByOrgIdAndAbolished(org.getId(), 0);
			List<CompanyOrganizationRelEntity>  coList = companyOrganizationRelDao.findByOrganizationIdAndAbolished(org.getId(), 0);
			if(foList  != null && foList.size() != 0){
				for(FactoryOrganizationRelEntity fo : foList){
					msg = msg + " 工厂："+fo.getFactory().getName()+"已于采购组织："+fo.getOrg().getName()+"绑定,无法废除！\r\n";
				}
				flag = false;
			}
           if(goList  != null && goList.size() != 0){
        	   for(GroupOrganizationRelEntity go : goList){
        		   msg = msg + " 采购组："+go.getGroup().getName()+"已于采购组织："+go.getOrg().getName()+"绑定,无法废除！\r\n";
        	   }
        		flag = false;
           }
           if(coList  != null && coList.size() != 0){
        	   for(CompanyOrganizationRelEntity co: coList ){
        		   msg = msg + "公司："+co.getCompany().getName()+"已于采购组织："+co.getOrganizationEntity().getName()+"绑定,无法废除！\r\n";
        	   }
				flag = false;
			}
		}
		
		if(!flag){
			map.put("msg", msg);
			map.put("success", false);
		}else{
			//废除
			for(OrganizationEntity org:orgList){
				orgDao.abolish(org.getId());
			}
			map.put("success", true);
		}
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
	public String updatePassword(HttpServletRequest request) {
		UserEntity user =  userDao.findById(Long.valueOf(String.valueOf(request.getParameter("loginUserId"))));
		if(user==null){
			return "用户不存在";
		}
		String passwords=(String) (request.getParameter("oldPassword"));
		String newpassword=(String) (request.getParameter("password"));
		String newpassword1=(String) (request.getParameter("confirmPassword"));
		
		String regex="^.*[a-zA-Z]+.*$";
		String regex1="^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$";
		String regex2="^.*[0-9]+.*$";

		byte[] oldPasswordbyte = Digests.sha1(passwords.getBytes(), Encodes.decodeHex(user.getSalt()), HASH_INTERATIONS);
		if(!Encodes.encodeHex(oldPasswordbyte).equals(user.getPassword())){
			return "原密码错误";
		}
			if(newpassword.length()>=8&&newpassword.matches(regex)&&newpassword.matches(regex1)&&newpassword.matches(regex2)){
			}else{
				return "密码必须包含字母数字符号且长度大于等于8位";
			}

			if(newpassword.equals(newpassword1))
			{
				byte[] salt = Digests.generateSalt(SALT_SIZE);
				user.setSalt(Encodes.encodeHex(salt));
				byte[] Passwordbyte = Digests.sha1(newpassword.getBytes(),salt, HASH_INTERATIONS);
				user.setPassword(Encodes.encodeHex(Passwordbyte));
				user.setIsOutData(1);
				user.setIsNeedUpdatePass(1);//1是不需要修改
				userDao.save(user);
				return "0";
			}else{
				return "两次输入的密码不一样";
			}
	}
	
	public OrganizationEntity findByCodeAndAbolished(String code ,int i) {
		return orgDao.findByCodeAndAbolished(code , i);	
	}
	
	
	
	
}
