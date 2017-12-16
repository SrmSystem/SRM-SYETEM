package com.qeweb.scm.vendormodule.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.Collections3;

import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.constants.OrgType;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.service.AccountService;
import com.qeweb.scm.basemodule.service.OrgService;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoExEntity;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoExDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialTypeRelDao;

@Service
@Transactional
public class VendorService {
	@Autowired
	private OrgService orgService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private VendorBaseInfoDao vendorBaseInfoDao;
	@Autowired
	private VendorBaseInfoExDao vendorBaseInfoExDao;
	@Autowired
	private MaterialTypeDao materialTypeDao;
	@Autowired
	private VendorMaterialTypeRelDao vendorMaterialTypeRelDao;
	
	/**
	 * 注册一个供应商
	 * @param user 用户
	 * @param org 组织
	 */

	public void registerVendorAccount(UserEntity user, OrganizationEntity org) {
		//注册一个供应商
		org.setCode(serialNumberService.geneterNextNumberByKey(OddNumbersConstant.VENDOR)); 
		org.setOrgType(OrgType.ORG_TYPE_COMPANY);
		org.setRoleType(OrgType.ROLE_TYPE_VENDOR);
		org.setRegisterTime(DateUtil.getCurrentTimestamp());
		org.setParentId(0L);
		org.setTopParentId(0L);
		orgService.addOrg(org);
		//给供应商分配一个用户
		user.setCompanyId(org.getId());
		accountService.registerUser(user);
		//给该用户分配权限 第一步注册赋予的权限 TODO
	}

	/**
	 * 激活供应商，其实是完成基本信息的填写
	 * @param vendorBaseInfo 基本信息
	 * @param userId 当前用户ID
	 * @param orgId 当前组织ID
	 */
	public void activeVendor(VendorBaseInfoEntity vendorBaseInfo, Long userId, Long orgId) {
		OrganizationEntity org = orgService.getOrg(orgId);
		org.setActiveStatus(StatusConstant.STATUS_YES);
		org.setConfirmStatus(StatusConstant.STATUS_NO);
		orgService.updateOrg(org);
		vendorBaseInfo.setOrg(org);
		List<VendorMaterialTypeRelEntity> venMatTypeList = convertVenMatTypeList(vendorBaseInfo);
		vendorBaseInfo.setCode(org.getCode());
		vendorBaseInfo.setName(org.getName());
		vendorBaseInfo.setOrgId(orgId);
		vendorBaseInfo.setVersionNO(1);
		vendorBaseInfo.setCurrentVersion(StatusConstant.STATUS_YES);
		vendorBaseInfo.setAuditStatus(StatusConstant.STATUS_NO);
		vendorBaseInfo.setSubmitStatus(StatusConstant.STATUS_NO);
		vendorBaseInfo.setApproveStatus(StatusConstant.STATUS_NO);
		vendorBaseInfoDao.save(vendorBaseInfo);
		//保存扩展信息
		vendorMaterialTypeRelDao.save(venMatTypeList);
		saveEx(vendorBaseInfo);
	}

	/**
	 * 转换成主供分类
	 * @param vendorBaseInfo 基本信息
	 * @return 转换后信息
	 */
	private List<VendorMaterialTypeRelEntity> convertVenMatTypeList(VendorBaseInfoEntity vendorBaseInfo) {
		String mainPt = vendorBaseInfo.getMainProduct();
		if(StringUtils.isEmpty(mainPt)) {
			return null;
		}
		List<VendorMaterialTypeRelEntity> vmtrList = new ArrayList<VendorMaterialTypeRelEntity>();
		String[] matTypeIdArray = mainPt.split(",");
		StringBuilder mainProBui = new StringBuilder();
		for(String matTypeId : matTypeIdArray){
			if(StringUtils.isEmpty(matTypeId)) {
				continue;
			}
			MaterialTypeEntity matType = materialTypeDao.findOne(Long.parseLong(matTypeId));
			VendorMaterialTypeRelEntity vmtr = new VendorMaterialTypeRelEntity();
			vmtr.setMaterialTypeCode(matType.getCode());
			vmtr.setMaterialTypeId(matType.getId());
			vmtr.setMaterialTypeName(matType.getName());
			vmtr.setOrgCode(vendorBaseInfo.getOrg().getCode());
			vmtr.setOrgId(vendorBaseInfo.getOrg().getId());
			vmtr.setOrgName(vendorBaseInfo.getOrg().getName());
			vmtrList.add(vmtr);
			mainProBui.append(",").append(matType.getName());
		}
		vendorBaseInfo.setMainProduct(mainProBui.toString().substring(1));
		return vmtrList;
	}

	/**
	 * 保存扩展信息
	 * @param vendorBaseInfo 基本信息
	 */
	private void saveEx(VendorBaseInfoEntity vendorBaseInfo) {
		List<VendorBaseInfoExEntity> exList = vendorBaseInfo.getExList();
		if(Collections3.isEmpty(exList)) {
			return;
		}
		for(VendorBaseInfoExEntity ex : new ArrayList<VendorBaseInfoExEntity>(exList)){
			//过滤一些信息
			if(ex.getExType()==null) {
				exList.remove(ex);
				continue;
			}
			ex.setOrgId(vendorBaseInfo.getOrgId());
			ex.setVendorId(vendorBaseInfo.getId());
		}
		vendorBaseInfoExDao.save(exList);
		
	}

}
