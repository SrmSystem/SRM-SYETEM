package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;

/**
 * 供应商阶段Dao
 * @author pjjxiajun
 * @date 2015年5月6日
 * @path com.qeweb.scm.vendormodule.repository.VendorPhaseDao.java
 */
public interface VendorBaseInfoDao extends BaseRepository<VendorBaseInfoEntity, Serializable>,JpaSpecificationExecutor<VendorBaseInfoEntity>{
	
	VendorBaseInfoEntity findByOrgIdAndCurrentVersionAndCol30(Long orgId, int statusYes,String col30);
	
	@Override
	List<VendorBaseInfoEntity> findAll(Specification<VendorBaseInfoEntity> spec);
	
	VendorBaseInfoEntity findByOrgId(Long orgId);

	VendorBaseInfoEntity findByOrgIdAndCurrentVersion(Long orgId, int statusYes);

	List<VendorBaseInfoEntity> findByOrgIdAndIdNot(Long orgId, Long currentId);
	
	List<VendorBaseInfoEntity> findByCurrentVersionAndPhaseIdIn(int statusYes,ArrayList<Long> newArrayList);
	
	@Modifying  
	@Query("update VendorBaseInfoEntity set vendorLevel=:vendorLevel,vendorClassify=:vendorClassify,vendorClassify2=:vendorClassify2 where code=:code")
	public void updateVendorClassify(@Param("code") String code,@Param("vendorLevel")  String vendorLevel,@Param("vendorClassify")  String vendorClassify,@Param("vendorClassify2") String vendorClassify2);
	@Modifying  
	@Query("update VendorBaseInfoEntity set qsa=:qsa,qsaResult=:qsaResult where code=:code")
	public void updateVendorQSA(@Param("code") String code,@Param("qsa")  String qsa,@Param("qsaResult")  String qsaResult);

	List<VendorBaseInfoEntity> findByOrgIdOrderByVersionNODesc(Long orgId);

	List<VendorBaseInfoEntity> findByOrgIdAndVersionNOLessThan(Long orgId, Integer versionNO);
	
	/**
	 * 按身份统计供应商数量
	 * @return
	 */
	@Query(value="SELECT PROVINCE,PROVINCE_TEXT,count(id) FROM QEWEB_VENDOR_BASE_INFO GROUP BY PROVINCE,PROVINCE_TEXT",nativeQuery=true)
	public List<Object[]> getChartDataProvince();
	
	/**
	 * 按省份、供应商性质统计供应商数量
	 * @return
	 */
	@Query(value="SELECT PROVINCE_TEXT,PHASE_ID,COUNT(PHASE_ID) FROM QEWEB_VENDOR_BASE_INFO GROUP BY PROVINCE_TEXT,PHASE_ID",nativeQuery=true)
	public List<Object[]> getChartDataProvAndPhase();
	/**
	 * 按零部件LEVEL_LAYER=3、供应商性质统计供应商数量
	 * @return
	 */
	@Query(value="SELECT V.MATERIAL_TYPE_ID,V.PHASE_ID,COUNT(V.PHASE_ID) FROM QEWEB_VENDOR_BASE_INFO V INNER JOIN QEWEB_MATERIAL_TYPE M ON V.MATERIAL_TYPE_ID=M.ID WHERE M.LEVEL_LAYER=3 GROUP BY V.MATERIAL_TYPE_ID,V.PHASE_ID",nativeQuery=true)
	public List<Object[]> getChartDataMaterialTypeAndPhase();
	
	VendorBaseInfoEntity findByOrgIdAndSubmitStatusAndAuditStatus(Long orgId, int statusYes, int statusNo);
	
	
	List<VendorBaseInfoEntity> findByOrgIdAndIdNotOrderByVersionNODesc(Long orgId, Long currentId);

	VendorBaseInfoEntity findByCodeAndCurrentVersion(String vendorCode,int statusYes);

	List<VendorBaseInfoEntity> findByOrgIdAndVersionNOLessThanEqualOrderByVersionNODesc(Long orgId, Integer versionNO);

	List<VendorBaseInfoEntity> findByOrgIdAndSubmitStatusOrderByVersionNODesc(Long orgId, int statusYes);

	List<VendorBaseInfoEntity> findByCurrentVersionAndPhaseIdInAndCodeLike(int statusYes, ArrayList<Long> newArrayList, String string);

	List<VendorBaseInfoEntity> findByCurrentVersionAndPhaseIdInAndNameLike(int statusYes, ArrayList<Long> newArrayList, String string);

	List<VendorBaseInfoEntity> findByCurrentVersionAndPhaseIdInAndCodeLikeAndNameLike(int statusYes, ArrayList<Long> newArrayList, String string,String string2);

	@Query(value="SELECT PROVINCE_TEXT from QEWEB_VENDOR_BASE_INFO WHERE  CURRENT_VERSION=1 AND PROVINCE_TEXT IS NOT null GROUP BY PROVINCE_TEXT",nativeQuery=true)
	List<String>  getprovince();

	//add by zhangjiejun 2015.9.15 start
	/**
	 * 通过code查询VendorBaseInfoEntity对象
	 * @param 	code	自身的code值
	 * @return	VendorBaseInfoEntity对象
	 */
	VendorBaseInfoEntity findByCode(String code);
	//add by zhangjiejun 2015.9.15 ends

	List<VendorBaseInfoEntity> findBySoIdIn(List<Long> idList);

	List<VendorBaseInfoEntity> findByCurrentVersionAndSoIdIn(Integer cv,List<Long> idList);
	
	/**
	 * 获取当前有效的供应商数据
	 * @author chao.gu
	 * 20170510
	 * @param cv
	 * @param abolished
	 * @return
	 */
	List<VendorBaseInfoEntity> findByCurrentVersionAndAbolished(Integer cv,Integer abolished);
}
