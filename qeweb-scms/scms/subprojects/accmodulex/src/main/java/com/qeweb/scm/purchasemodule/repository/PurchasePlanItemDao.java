package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanItemEntity;


public interface PurchasePlanItemDao extends BaseRepository<PurchasePlanItemEntity, Serializable>,JpaSpecificationExecutor<PurchasePlanItemEntity>{

	@Override
	public List<PurchasePlanItemEntity> findAll();
	
	public PurchasePlanItemEntity findById(Long id);
	
	public List<PurchasePlanItemEntity> findByPlanId(Long planId);
	
	@Override
	public Page<PurchasePlanItemEntity> findAll(Pageable page);

	@Query("select a from PurchasePlanItemEntity a where a.plan.id = ?1 and a.itemNo = ?2 and a.material.code= ?3 and a.vendor.code= ?4 and a.planRecTime= ?5 and a.abolished=0")
	public PurchasePlanItemEntity findPurchasePlanItemEntityByMainId(long planId, Integer itemNo,String materialCode,String vendorCode,Timestamp planRecTime);
	
	@Query(value="select sum(total_plan_qty) from qeweb_purchase_plan_item where material_id=?1",nativeQuery=true)
	public BigDecimal findPlanQtyByMaterial(Long materialId);
	
	@Query("select a from PurchasePlanItemEntity a where a.plan.id = ?1 and  a.group.code = ?2  and   a.factory.code = ?3 and  a.material.code= ?4 and a.vendor.code= ?5  and a.abolished=0  and a.isNew = 1")
	public PurchasePlanItemEntity findPurchasePlanItemEntityByKeys(long planId,String groupCode ,String factoryCode, String materialCode,String vendorCode);
	
	@Modifying 
	@Query("update PurchasePlanItemEntity set isNew = 0  where id = ?1")
	public void invalidPurchasePlanItem(Long planItemId);
	
	//删除计划获取数据（未发布的可以删除）
	@Query("select a from PurchasePlanItemEntity a where   a.group.code = ?1  and   a.factory.code = ?2 and  a.material.code= ?3 and a.vendor.code= ?4   and a.plan.month = ?5  and a.abolished=0  and a.isNew = 1 and a.publishStatus = 0")
	public PurchasePlanItemEntity findPurchasePlanItemEntityByDeleteKeys(String buyerCode ,String factoryCode, String materialCode,String vendorCode,String month);
	
	//获取采购商发布给供应商的数据
	@Query("select a from PurchasePlanItemEntity a where   a.vendor.id = ?1  and a.abolished= 0  and a.isNew = 1 and a.publishStatus = 1")
	public List<PurchasePlanItemEntity> findPurchasePlanItemEntityByVendor(Long vendorId);
	
	//根据采购计划主id和供应商id  获取采购计划子项目
	@Query("select a from PurchasePlanItemEntity a where a.plan.id = ?1 AND  a.vendor.id = ?2 and a.abolished= 0  and a.isNew = 1 and a.publishStatus = 1 and a.uploadStatus = 0")
	public List<PurchasePlanItemEntity> findPurchasePlanItemEntityByPlanidAndVendor(Long planId,Long vendorId);
	
	@Query("select a from PurchasePlanItemEntity a where   a.group.code = ?1  and   a.factory.code = ?2 and  a.material.code= ?3 and a.vendor.code= ?4 and a.plan.id = ?5  and a.abolished=0  and a.isNew = 1 order by createTime  desc")
	public List<PurchasePlanItemEntity> findPurchasePlanItemEntityByMainKeys(String groupCode ,String factoryCode, String materialCode,String vendorCode,Long id);
	
	
	//获取采购商发布给供应商的数据
	@Query("select a from PurchasePlanItemEntity a where   a.vendor.id = ?1  and a.plan.id = ?2 and a.abolished= 0  and a.isNew = 1 and a.publishStatus = 1")
	public List<PurchasePlanItemEntity> findPurchasePlanItemEntityByVendorAndPlanId(Long vendorId,Long planId);
	
	/**
	 * 根据明细ID列表获取大版本ID(待办用)
	 * @author chao.gu
	 * @param itemIds
	 * @return
	 */
	@Query("select distinct(a.plan.id) from PurchasePlanItemEntity a where   a.id in(?1) and a.abolished=0")
	public List<Long> findPurchasePlanIdsByItemIds(List<Long> itemIds);
	
	
	/**
	 * 预警用（查询发布的数据）
	 * @author hao.qin
	 * @param mainId
	 * @return
	 */
	@Query("select a from PurchasePlanItemEntity a where   a.plan.id = ?1  and a.abolished= 0  and a.isNew = 1 and a.publishStatus = 1")
	public List<PurchasePlanItemEntity> findUnPublishByMainId(Long id);
	
	/**
	 * 预警用（查询驳回的数据）
	 * @author hao.qin
	 * @param mainId
	 * @return
	 */
	@Query("select a from PurchasePlanItemEntity a where   a.plan.id = ?1 and a.abolished= 0  and a.isNew = 1 and a.confirmStatus = -1")
	public List<PurchasePlanItemEntity> findRejectByMainId(Long id);
	
	
	/**
	 * 预警用（查询供应商未确认的计划行）
	 * @author hao.qin
	 * @param mainId
	 * @return
	 */
	@Query("select a from PurchasePlanItemEntity a where   a.plan.id = ?1 and a.vendor.id = ?2 and a.abolished= 0  and a.isNew = 1 and a.confirmStatus = 0")
	public List<PurchasePlanItemEntity> findUnconfimByMainIdForVendor(Long id,Long vendorId);
	
	/**
	 * 预警用（查询供应商驳回拒绝的计划行）
	 * @author hao.qin
	 * @param mainId
	 * @return
	 */
	@Query("select a from PurchasePlanItemEntity a where   a.plan.id = ?1 and a.vendor.id = ?2 and a.abolished= 0  and a.isNew = 1 and a.confirmStatus = -2")
	public List<PurchasePlanItemEntity> findVoteByMainIdForVendor(Long id,Long vendorId);

	@Query("FROM PurchasePlanItemEntity a where a.factory.code = ?1 and a.material.code = ?2 and a.group.code = ?3 and a.vendor.code=?4 and a.abolished= 0  and a.isNew = 1")
	public List<PurchasePlanItemEntity> findPurchasePlanItemList(
			String factoryCode, String materialCode, String groupCode,
			String vendorCode);
	
	
	
}
