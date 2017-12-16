package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;


public interface PurchaseGoodsRequestDao extends BaseRepository<PurchaseGoodsRequestEntity, Serializable>,JpaSpecificationExecutor<PurchaseGoodsRequestEntity>{

	@Override
	public List<PurchaseGoodsRequestEntity> findAll();
	
	
	public PurchaseGoodsRequestEntity findById(Long id);
	
	
	/**
	 * 根据工厂+物料+供应商+日期 确定唯一性
	 * @author chao.gu
	 * @param factoryCode
	 * @param materialCode
	 * @param rq
	 * @return
	 */
	@Query("from PurchaseGoodsRequestEntity a where a.factory.code=?1 and a.material.code=?2 and a.vendor.code=?3 and a.flag = ?4 and a.rq=?5")
	public List<PurchaseGoodsRequestEntity>  findByFactoryAndMaterialAndVendorAndRQ(String factoryCode,String materialCode,String vendorCode,String flag,Timestamp rq);
	
	
	 
	@Query("from PurchaseGoodsRequestEntity a where a.factory.code=?1 and a.material.code=?2 and a.vendor.code=?3 and a.flag is null and a.rq=?4")
	public List<PurchaseGoodsRequestEntity>  findByFactoryAndMaterialAndVendorAndRQAndFlagIsNull(String factoryCode,String materialCode,String vendorCode,Timestamp rq);
	
	
	/**
	 * 获取未匹配完的订单
	 * @author hao.QIN
	 * @param factoryCode
	 * @param materialCode
	 * @param rq
	 * @return
	 */
	@Query("from PurchaseGoodsRequestEntity a where a.surQry >= 0.01 order by a.rq asc")
	public List<PurchaseGoodsRequestEntity>  findNotClear();
	
	/**
	 * 通过工厂和物料获取要货计划
	 * @author hao,qin
	 * @param factoryCode
	 * @param materialCode
	 * @param rq
	 * @return
	 */
	@Query("from PurchaseGoodsRequestEntity a where a.factory.code=?1 and a.material.code=?2 and  a.abolished=0 order by a.rq asc")
	public List<PurchaseGoodsRequestEntity>  findByFatoryCodeAndMaterialCode(String factoryCode,String materialCode);
	
	//报表相关
	@Query("from PurchaseGoodsRequestEntity a where a.factory.code=?1 and a.material.code=?2 and  a.group.code = ?3 and a.vendor.code = ?4  and a.rq >= ?5  and a.rq  <= ?6  and  a.abolished=0 order by a.rq asc")
	public List<PurchaseGoodsRequestEntity>  findGoodsForReport(String factoryCode,String materialCode,String groupCode,String vendorCode,Timestamp startTime,Timestamp endTime);

    /**
     * 查找未发布的要货计划
     * @param flag
     * @param factoryId
     * @param purchasingGroupId
     * @param materialId
     * @param meins
     * @param ysts
     * @param shpl
     * @param vendorId
     * @param beginRq
     * @param endRq
     * @return
     */
	@Query("from PurchaseGoodsRequestEntity a where  a.flag=?1 and a.factory.id=?2 and a.group.id=?3 and a.material.id=?4 and a.meins=?5  and a.vendor.id=?6 and a.rq>=?7 and a.rq<=?8 and a.publishStatus!=1 and a.abolished=0")
	public List<PurchaseGoodsRequestEntity> getUnpublishGoodsRequest(
			String flag, Long factoryId, Long purchasingGroupId,
			Long materialId, String meins,
			Long vendorId, Timestamp beginRq, Timestamp endRq);
	
	
	 /**
     * 查找供应商未确认的要货计划
     * @param flag
     * @param factoryId
     * @param purchasingGroupId
     * @param materialId
     * @param meins
     * @param ysts
     * @param shpl
     * @param vendorId
     * @param beginRq
     * @param endRq
     * @return
     */
	@Query("from PurchaseGoodsRequestEntity a where  a.flag=?1 and a.factory.id=?2 and a.group.id=?3 and a.material.id=?4 and a.meins=?5 and a.vendor.id=?6 and a.rq>=?7 and a.rq<=?8 and a.publishStatus!=0 and a.vendorConfirmStatus!=1 and a.abolished=0")
	public List<PurchaseGoodsRequestEntity> getUnVendorConfirmGoodsRequest(
			String flag, Long factoryId, Long purchasingGroupId,
			Long materialId, String meins, 
			Long vendorId, Timestamp beginRq, Timestamp endRq);
	
}
