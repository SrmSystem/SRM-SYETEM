package com.qeweb.scm.baseline.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.basemodule.constants.DataType;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.vendormodule.entity.BuyerVendorRelEntity;
import com.qeweb.scm.vendormodule.repository.BuyerMaterialRelDao;
import com.qeweb.scm.vendormodule.repository.BuyerMaterialTypeRelDao;
import com.qeweb.scm.vendormodule.repository.BuyerVendorRelDao;


@Service
@Transactional
public class BuyerOrgPermissionUtil {

	@Autowired
	private BuyerVendorRelDao buyerVendorRelDao ;
	
	@Autowired
	private BuyerMaterialTypeRelDao buyerMaterialTypeRelDao;
	
	@Autowired
	private BuyerMaterialRelDao buyerMaterialRelDao;
	
	/**
	 * 供应商属于的组织
	 * @param vendorId
	 * @return
	 */
	public List<Long> getBuyerIdsByVendor(Long vendorId){
		return buyerVendorRelDao.findBuyerIdsByVendorId(vendorId);
	}
	
	/**
	 * 获得当前用户拥有的用户数据权限
	 * @return
	 */
	public List<Long> getUserIds(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Set<Long> set= user.dataRight.get(DataType.DATA_TYPE_USER+"");
		if(set==null){
			return null;
		}
		List<Long> list=new ArrayList<Long>();
		list.addAll(set);
		return list;
	}
	
	/**
	 * 获得当前用户拥有的采购组数据权限
	 * @return
	 */
	public List<Long> getGroupIds(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Set<Long> set= user.dataRight.get(DataType.DATA_TYPE_GROUP+"");
		if(set==null){
			return null;
		}
		List<Long> list=new ArrayList<Long>();
		list.addAll(set);
		return list;
	}
	
	
	/**
	 * 获得当前用户拥有的采购组织权限
	 * @return
	 */
	public List<Long> getBuyerIds(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Set<Long> set= user.dataRight.get(DataType.DATA_TYPE_BUYER+"");
		if(set==null||set.size()<=0){
			return null;
		}
		List<Long> list=new ArrayList<Long>();
		list.addAll(set);
		return list;
	}
	
	/**
	 * 判断当前用户是否有导入的采购组织权限
	 * @param impBuyerId
	 * @return
	 */
	public boolean isContainsBuyerIds(Long impBuyerId){
		List<Long> buyerIds=getBuyerIds();
		if(null!=buyerIds && buyerIds.size()>0){
			for (Long long1 : buyerIds) {
				if(long1.longValue()==impBuyerId.longValue()){
					return true;
				}
			}
		}
		return false;
	}
	
	public StringBuilder getBuyerIdCond(){
		StringBuilder sb=new StringBuilder();
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Set<Long> set= user.dataRight.get(DataType.DATA_TYPE_BUYER+"");
		if(set==null||set.size()<=0){
			return null;
		}
		sb.append(" in (");
		int size=set.size();
		int i=1;
		for (Long long1 : set) {
			if(i==size){
				sb.append(long1);
			}else{
				sb.append(long1).append(",");
			}
			i++;
		}
		sb.append(")");
		
		return sb;
	}
	
	/**
	 * 通过组织权限查找供应商
	 * @return
	 */
	public  List<Long> getVendorIds(){
		List<Long> list=new ArrayList<Long>();
		List<Long> buyerIds = getBuyerIds();
		if(buyerIds == null){
			return null;
		}else{
			List<BuyerVendorRelEntity> res=buyerVendorRelDao.findByBuyerIdIn(buyerIds);
			if(res==null){
				return null;
			}else{
				for (BuyerVendorRelEntity buyerVendorRelEntity : res) {
					list.add(buyerVendorRelEntity.getVendorId());
				}
			}
		}
		return list;
	}
	
	public Long getBuyerIdsByOrgId(Long orgId){
		List<BuyerVendorRelEntity> res = buyerVendorRelDao.findByVendorId(orgId);
		
		
		return res!=null&&res.size()>0?res.get(0).getBuyerId():1l;
	}
	

}
