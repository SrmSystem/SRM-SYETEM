package com.qeweb.scm.vendorperformancemodule.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.BussinessRangeDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.FileUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.ResultUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendorperformancemodule.constants.PerformanceProConstant;
import com.qeweb.scm.vendorperformancemodule.constants.PerformanceTypeConstant;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDimensionsEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforIndexEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforModelEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforSourceDataEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforCycleDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDataInDetailsDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDimensionsDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforFormulasDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforIndexDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforModelDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforScoresDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforSourceDataDao;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforSourceDataVo;

@Service
@Transactional
public class VendorPerforSourceDataService {
	
	public static String ATT_DIR = PropertiesUtil.getProperty(PerformanceProConstant.BASEDATA_DIR,"");
	/** 原始数据 */
	public static final String VPSD = "SD";
	
	@Autowired
	private VendorPerforSourceDataDao dao;
	@Autowired
	private VendorBaseInfoDao baseInfoDao;
	@Autowired
	private MaterialTypeDao materialTypeDao;
	@Autowired
	private BussinessRangeDao bussinessRangeDao;
	@Autowired
	private VendorPerforIndexDao indexDao;
	@Autowired
	private VendorPerforFormulasDao formulasDao;
	@Autowired
	private VendorPerforCycleDao cycleDao;
	@Autowired
	private FactoryDao factoryDao;
	@Autowired
	private VendorPerforScoresDao vendorPerforScoresDao;
	@Autowired
	private VendorPerforDataInDetailsDao vendorPerforDataInDetailsDao;
	@Autowired
	private OrganizationDao orgDao;
	@Autowired
	private VendorPerforModelDao vendorPerforModelDao;
	@Autowired
	private VendorPerforDimensionsDao vendorPerforDimensionsDao;
	@Autowired
	private VendorPerforIndexDao vendorPerforIndexDao;
	@Autowired 
	private SerialNumberService serialNumberService;
	
	public Page<VendorPerforSourceDataEntity> query(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforSourceDataEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforSourceDataEntity.class);
		Page<VendorPerforSourceDataEntity>  page= dao.findAll(spec,pagin);
		return page;
	}

	public Map<String, Object> update(VendorPerforSourceDataEntity sourceData, MultipartFile attFile) throws Exception {
		Map<String, Object> map = ResultUtil.createMap();
		setField(sourceData,attFile);
		sourceData = dao.save(sourceData);
		return map;
	}
	
	public Map<String, Object> delete(List<Long> ids) throws Exception {
		Map<String, Object> map = ResultUtil.createMap();
		for(Long id : ids) {
			dao.delete(id);
		}
		map.put("success", true);
		map.put("msg", "删除成功");
		return map;
	}
	
	/**
	 * 批量删除原始数据
	 * @param idList id集合
	 * @return
	 */
	public Map<String,Object> batchDelete(List<Long> idList){
		Map<String,Object> map = ResultUtil.createMap();
		for(Long id : idList){
			dao.delete(id);
		}
		return map;
		
	}

	/**
	 * 增加基础数据
	 * @param sourceData 基础数据
	 * @param attFile 附件
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackOn=Exception.class)
	public Map<String, Object> add(VendorPerforSourceDataEntity sourceData, MultipartFile attFile) throws Exception {
		Map<String, Object> map = ResultUtil.createMap();
		sourceData.setCode(getAutoCode());
		setField(sourceData,attFile);
		sourceData = dao.save(sourceData);
		return map;
	}

	/**
	 * 对字段进行设置
	 * @param sourceData 基础数据对象
	 * @param attFile 
	 * @throws Exception 
	 */
	private void setField(VendorPerforSourceDataEntity sourceData, MultipartFile attFile) throws Exception {
		if(attFile!=null && !attFile.isEmpty()){
			File file = FileUtil.savefile(attFile, ATT_DIR);
			sourceData.setAttFileName(attFile.getOriginalFilename());
			sourceData.setAttFilePath(file.getAbsolutePath());
		}
		if(sourceData.getAssessTime()==null){
			sourceData.setAssessTime(DateUtil.getCurrentTimestamp());
		}
		//查询绩效模型
		VendorPerforModelEntity model = vendorPerforModelDao.findOne(sourceData.getPerformanceModelId());
		sourceData.setPerformanceModelName(model.getName());
		//查询供应商
		OrganizationEntity org = orgDao.findOne(sourceData.getOrgId());
		sourceData.setOrgCode(org.getCode());
		sourceData.setOrgName(org.getName());
		//查询品牌
		BussinessRangeEntity brand = bussinessRangeDao.findOne(sourceData.getBrandId());
		sourceData.setBrandCode(brand.getCode());
		sourceData.setBrandName(brand.getName());
		//查询物料工厂
		FactoryEntity factory = factoryDao.findOne(sourceData.getFactoryId());
		sourceData.setFactoryCode(factory.getCode());
		sourceData.setFactoryName(factory.getName());
		//查询物料分类
		MaterialTypeEntity matType = materialTypeDao.findOne(sourceData.getMatTypeId());
		sourceData.setMatTypeCode(matType.getCode());
		sourceData.setMatTypeName(matType.getName());
		//查询维度
		if(sourceData.getDimId()!=null && sourceData.getDimId().longValue()!=0L){
			VendorPerforDimensionsEntity dim = vendorPerforDimensionsDao.findOne(sourceData.getDimId());
			sourceData.setDimCode(dim.getCode());
			sourceData.setDimName(dim.getDimName());
		}
		//查询指标
		if(sourceData.getIndexId()!=null && sourceData.getIndexId().longValue()!=0L){
			VendorPerforIndexEntity index = vendorPerforIndexDao.findOne(sourceData.getIndexId());
			sourceData.setIndexCode(index.getCode());
			sourceData.setIndexName(index.getIndexName());
		}
		
		
	}

	/**
	 * 靠ID获得
	 * @param id 主键
	 * @return
	 */
	public VendorPerforSourceDataEntity get(Long id) {
		return dao.findOne(id);
	}

	/**
	 * 导入数据
	 * @param list 导入的数据集合
	 * @param logger 日志类
	 * @return 导入结果
	 */
	@Transactional(rollbackOn=Exception.class)
	public Map<String, Object> imp(List<VendorPerforSourceDataVo> list, ILogger logger) throws Exception {
	    Map<String,Object> map = ResultUtil.createMap();
	    boolean allTrue  = true;
	    logger.log("-->开始导入...");
	    for(VendorPerforSourceDataVo vo : list){
	    	VendorPerforSourceDataEntity data = new VendorPerforSourceDataEntity();
	    	BeanUtils.copyProperties(data, vo);
	    	data.setCode(getAutoCode());
	    	//根据名字查绩效类型
	    	VendorPerforModelEntity model =  vendorPerforModelDao.findByName(vo.getPerformanceModelName());
	    	if(model == null){
	    		logger.log("绩效："+vo.getPerformanceModelName()+"未在系统维护!");
	    		allTrue = false;
	    		continue;
	    	}
	    	data.setPerformanceModelName(model.getName());
	    	data.setPerformanceModelId(model.getId());
	    	//供应商
	    	OrganizationEntity org = orgDao.findByCodeAndAbolished(vo.getOrgCode(),StatusConstant.STATUS_NO);
	    	if(org==null){
	    		logger.log("供应商："+vo.getOrgName()+"("+vo.getOrgCode()+")未在系统维护!");
	    		allTrue = false;
	    		continue;
	    	}
	    	data.setOrgId(org.getId());
	    	data.setOrgCode(org.getCode());
	    	data.setOrgName(org.getName());
	    	//品牌
	    	BussinessRangeEntity brand = bussinessRangeDao.findByCodeAndBussinessTypeAndAbolished(vo.getBrandCode(), PerformanceTypeConstant.BUSSINESSRANGE_TYPE_2,StatusConstant.STATUS_NO);
	    	if(brand==null){
	    		logger.log("品牌："+vo.getBrandName()+"未在系统维护!");
	    		allTrue = false;
	    		continue;
	    	}
	    	data.setBrandCode(brand.getCode());
	    	data.setBrandId(brand.getId());
	    	data.setBrandName(brand.getName());
	    	//工厂
	    	FactoryEntity factory = factoryDao.findByNameAndAbolished(vo.getFactoryName(), StatusConstant.STATUS_NO);
	    	if(factory==null){
	    		logger.log("工厂："+vo.getFactoryName()+"未在系统维护!");
	    		allTrue = false;
	    		continue;
	    	}
	    	data.setFactoryId(factory.getId());
	    	data.setFactoryCode(factory.getCode());
	    	data.setFactoryName(factory.getName());
	    	//分类
	    	Integer levelLayer = PropertiesUtil.getInteger(PerformanceProConstant.MATERIALTYPE_LEAFLEVEL, 1);
	    	MaterialTypeEntity matType = materialTypeDao.findByCodeAndLevelLayer(vo.getMatTypeCode(),levelLayer);
	    	if(matType==null){
	    		logger.log("分类："+vo.getMatTypeName()+"未在系统维护!");
	    		allTrue = false;
	    		continue;
	    	}
	    	data.setMatTypeId(matType.getId());
	    	data.setMatTypeCode(matType.getCode());
	    	data.setMatTypeName(matType.getName());
	    	if(vo.getAssessTime()==null){
	    		data.setAssessTime(DateUtil.getCurrentTimestamp());
	    	}
	    	dao.save(data);
	    }
	    if(!allTrue){
	    	map.put(ResultUtil.SUCCESS, false);
	    	map.put("msg", "部分数据导入不成功！请查看日志！");
	    }
		return map;
	}
	
	public String getAutoCode(){
		return serialNumberService.geneterNextNumberByKey(VPSD);
	}

	/**
	 * 增加基础数据 多品牌批量新增
	 * @param sourceData 基础数据
	 * @param attFile 附件
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackOn=Exception.class)
	public Map<String, Object> addEx(VendorPerforSourceDataEntity sourceData, MultipartFile attFile) throws Exception {
		Map<String, Object> map = ResultUtil.createMap();
		List<VendorPerforSourceDataEntity> list = new ArrayList<VendorPerforSourceDataEntity>();
		if(StringUtils.isNotEmpty(sourceData.getBrandIds())){
			String [] brandIds = sourceData.getBrandIds().split(",");
			for (int i = 0; i < brandIds.length; i++) {
				VendorPerforSourceDataEntity newSourceData = new VendorPerforSourceDataEntity();
				BeanUtils.copyProperties(newSourceData, sourceData);
				newSourceData.setBrandId(Long.parseLong(brandIds[i]));
				setFieldEx(newSourceData,attFile);
				list.add(newSourceData);
			}
		}
		
		dao.save(list);
		return map;
	}
	
	/**
	 * 对字段进行设置 多品牌批量
	 * @param sourceData 基础数据对象
	 * @param attFile 
	 * @throws Exception 
	 */
	private void setFieldEx(VendorPerforSourceDataEntity sourceData, MultipartFile attFile) throws Exception {
		sourceData.setCode(getAutoCode());
		if(attFile!=null && !attFile.isEmpty()){
			File file = FileUtil.savefile(attFile, ATT_DIR);
			sourceData.setAttFileName(attFile.getOriginalFilename());
			sourceData.setAttFilePath(file.getAbsolutePath());
		}
		if(sourceData.getAssessTime()==null){
			sourceData.setAssessTime(DateUtil.getCurrentTimestamp());
		}
		//查询绩效模型
		VendorPerforModelEntity model = vendorPerforModelDao.findOne(sourceData.getPerformanceModelId());
		sourceData.setPerformanceModelName(model.getName());
		//查询供应商
		OrganizationEntity org = orgDao.findOne(sourceData.getOrgId());
		sourceData.setOrgCode(org.getCode());
		sourceData.setOrgName(org.getName());
		//查询品牌
		BussinessRangeEntity brand = bussinessRangeDao.findOne(sourceData.getBrandId());
		sourceData.setBrandCode(brand.getCode());
		sourceData.setBrandName(brand.getName());
		//查询物料工厂
		FactoryEntity factory = null;
		if(sourceData.getFactoryId() != null){
			factory = factoryDao.findOne(sourceData.getFactoryId());
			sourceData.setFactoryCode(factory.getCode());
			sourceData.setFactoryName(factory.getName());
		}
		//查询物料分类
		MaterialTypeEntity matType = null;
		if(sourceData.getMatTypeId() != null){
			matType = materialTypeDao.findOne(sourceData.getMatTypeId());
			sourceData.setMatTypeCode(matType.getCode());
			sourceData.setMatTypeName(matType.getName());
		}
		//查询维度
		if(sourceData.getDimId()!=null && sourceData.getDimId().longValue()!=0L){
			VendorPerforDimensionsEntity dim = vendorPerforDimensionsDao.findOne(sourceData.getDimId());
			sourceData.setDimCode(dim.getCode());
			sourceData.setDimName(dim.getDimName());
		}
		//查询指标
		if(sourceData.getIndexId()!=null && sourceData.getIndexId().longValue()!=0L){
			VendorPerforIndexEntity index = vendorPerforIndexDao.findOne(sourceData.getIndexId());
			sourceData.setIndexCode(index.getCode());
			sourceData.setIndexName(index.getIndexName());
		}
		
		
	}


}
