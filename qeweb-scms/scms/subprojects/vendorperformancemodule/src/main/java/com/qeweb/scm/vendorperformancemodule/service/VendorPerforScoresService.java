package com.qeweb.scm.vendorperformancemodule.service;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Collections3;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.VendorBFMatTypeDao;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.LogUtil;
import com.qeweb.scm.basemodule.utils.MatcherUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.vendormodule.entity.VendorMaterialTypeRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorMaterialSupplyRelDao;
import com.qeweb.scm.vendormodule.repository.VendorMaterialTypeRelDao;
import com.qeweb.scm.vendorperformancemodule.constants.PerformanceProConstant;
import com.qeweb.scm.vendorperformancemodule.constants.PerformanceTypeConstant;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDataInEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDimensionsEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFAllocationEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFormulasEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforLevelEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforMappedEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforReviewsEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresDimEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresIndexEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresTotalEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresTotalHisEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateSettingEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforCycleDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDataInDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDimensionsDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforFAllocationDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforFormulasDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforLevelDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforMappedDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforReviewsDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforScoresDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforScoresDimDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforScoresIndexDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforScoresTotalDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforScoresTotalHisDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforTemplateDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforTemplateSettingDao;
import com.qeweb.scm.vendorperformancemodule.utils.PerformanceParseUtil;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforScoresTotalVo;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforScoresTotalVo2;

@Service
@Transactional
public class VendorPerforScoresService {
	
	@Autowired
	private VendorPerforScoresDao dao;
	@Autowired
	private VendorPerforScoresIndexDao vendorPerforScoresIndexDao;
	@Autowired
	private VendorPerforScoresDimDao vendorPerforScoresDimDao;
	@Autowired
	private VendorPerforScoresTotalDao vendorPerforScoresTotalDao;
	@Autowired
	private VendorPerforScoresTotalHisDao vendorPerforScoresTotalHisDao;
	@Autowired
	private VendorPerforTemplateDao templateDao;
	@Autowired
	private VendorPerforCycleDao vendorPerforCycleDao;
	@Autowired
	private VendorPerforReviewsDao vendorPerforReviewsDao;
	@Autowired
	private VendorPerforTemplateSettingDao vendorPerforTemplateSettingDao;
	@Autowired
	private VendorPerforTemplateDao vendorPerforTemplateDao;
	@Autowired
	private VendorPerforFormulasDao vendorPerforFormulasDao;
	@Autowired
	private VendorPerforDataInDao vendorPerforDateInDao;
	@Autowired
	private VendorPerforFAllocationDao vendorPerforFAllocationDao;
	@Autowired
	private VendorPerforDimensionsDao vendorPerforDimensionsDao;
	@Autowired
	private VendorPerforLevelDao vendorPerforLevelDao;
	@Autowired
	private VendorMaterialTypeRelDao vendorMaterialTypeRelDao;
    @Autowired
    private VendorPerforMappedDao vendorPerforMappedDao;
    @Autowired 
    private VendorMaterialSupplyRelDao vendorMaterialSupplyRelDao;
    @Autowired 
    private VendorBFMatTypeDao vendorBFMatTypeDao;
    @Autowired 
    private MaterialDao materialDao;
    
    @Autowired
    private VendorPerforCorrectionService vendorPerforCorrectionService;
    
    private static Integer COUNT_LEVEL = Integer.parseInt(PropertiesUtil.getProperty(PerformanceProConstant.PERFORMANCE_LEVEL,"0"));
	
	public Page<VendorPerforScoresEntity> getVendorPerforScoresList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforScoresEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforScoresEntity.class);
		Page<VendorPerforScoresEntity>  page= dao.findAll(spec,pagin);
		return page;
	}
	
	public Page<VendorPerforScoresTotalHisEntity> getVendorScoresTotalHisList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pageReq = PageUtil.buildPageRequest(pageNumber, pageSize, "createTime", "desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforScoresTotalHisEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforScoresTotalHisEntity.class);
		Page<VendorPerforScoresTotalHisEntity>  page= vendorPerforScoresTotalHisDao.findAll(spec,pageReq);
		return page;
	}
	
	public VendorPerforScoresTotalEntity getScoresTotalById(Long id) {
		return vendorPerforScoresTotalDao.findOne(id);
	}

	/**
	 * 
	 * @param searchParamMap
	 * @return
	 */
	public Page<VendorPerforScoresTotalEntity> getVendorScoresTotalList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pageReq = PageUtil.buildPageRequest(pageNumber, pageSize, "assessDate,ranking", "desc,asc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforScoresTotalEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforScoresTotalEntity.class);
		Page<VendorPerforScoresTotalEntity>  page= vendorPerforScoresTotalDao.findAll(spec,pageReq);
		return page;
	}

	public String[] getVendorPerforScoresListGet() {
		
		return null;
	}

	/**
	 * 初始化该时间内需要评估的列表
	 * @return 初始化结果
	 * @throws Exception 
	 */
	@Transactional(rollbackOn=Exception.class)
	public Map<String, Object> initList() throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		//先获取周期
		List<VendorPerforCycleEntity> cycleList = vendorPerforCycleDao.findByAbolished(StatusConstant.STATUS_NO);
		List<VendorPerforScoresEntity> scoresList = new ArrayList<VendorPerforScoresEntity>();
		Date currentDate = DateUtil.getCurrentTimestamp();
		for(VendorPerforCycleEntity cycle : cycleList){
			//进行判断
			Timestamp[] dateArray = PerformanceParseUtil.parseAssessTime(cycle.getCode(),0);
			if(dateArray!=null){
				Timestamp startDate = dateArray[0];
				Integer preDate = cycle.getInitDates();
				Date waitingDate = DateUtil.addDay(startDate, preDate);
				//在开始阶段，那么加入
				if(DateUtil.between(currentDate, startDate, waitingDate)){
					VendorPerforScoresEntity score = new VendorPerforScoresEntity();
					score.setAssessStartDate(new Timestamp(currentDate.getTime()));
					score.setCycleId(cycle.getId());
					score.setCountStartDate(startDate);
					score.setCountEndDate(new Timestamp(waitingDate.getTime()));
					score.setProcess(1);
					scoresList.add(score);
				}
			}
		}
		
		//开始新增评估列表
		if(Collections3.isEmpty(scoresList)){
			return map;
		}
		
		for(VendorPerforScoresEntity score : scoresList){
			//查询是否已经产生了该周期该模型的评估列表,如果已经完成，重新初始化
			VendorPerforScoresEntity exist = dao.findByCycleIdAndModelIdAndAssessStartDateBetween(score.getCycleId(),score.getModelId(),score.getCountStartDate(),score.getCountEndDate());
		    if(exist!=null){
		    	exist.setAssessEndDate(null);
		    	exist.setCountStatus(StatusConstant.STATUS_NO);
		    	exist.setLogPath("");
		    	dao.save(exist);
		    	vendorPerforScoresDimDao.deleteByScoresId(exist.getId());
		    	vendorPerforScoresIndexDao.deleteByScoresId(exist.getId());
		    	vendorPerforScoresTotalDao.deleteByScoresId(exist.getId());
		    	continue;
		    }
		    //获取该周期内的参评供应商数目
		    Integer vendorCount = vendorPerforReviewsDao.getVendorCount(score.getCycleId(),StatusConstant.STATUS_YES);
		    score.setVendorNumber(vendorCount);
		    //获取当时的模版
		    List<VendorPerforTemplateEntity> templateList = templateDao.findByCycleIdOrderByCreateTimeDesc(score.getCycleId());
		    if(Collections3.isNotEmpty(templateList)){
		    	score.setPerforTemplateId(templateList.get(0).getId());
		    }
		    dao.save(score);
		}
		return map;
	}
	
	/**
	 * 根据id删除记录（硬删除）
	 * @param id
	 * @return
	 */
	public Map<String,Object> delete(Long id){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		VendorPerforScoresEntity scores = dao.findOne(id);
		vendorPerforScoresIndexDao.deleteByOrgx(id);
		vendorPerforScoresDimDao.deleteByOrgx(id);
		vendorPerforScoresTotalDao.deleteByOrgx(id);
		vendorPerforScoresTotalHisDao.deleteByOrgx(id);
		if(scores.getProcess()>1){
			map.put("success", false);
			map.put("msg", "该模版已不在第一阶段，不能删除");
		} else {
			dao.delete(id);
		}
		return map;
	}
	
	/**
	 * 校验并计算指标
	 * @param id 评估列表ID
	 * @return 计算结果
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackOn=Exception.class)
	public Map<String, Object> countIndex(Long id) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		VendorPerforScoresEntity score = dao.findOne(id);
		map = validateCount(score);
		boolean success = (Boolean) map.get("success");
		if(!success){
			return map;
		}
		StringBuilder logMsg = new StringBuilder((String) map.get("logMsg"));
		//开始计算
		//模版配置和参评供应商
		Map<String,Object> keyMap = (Map<String, Object>) map.get("keyMap");
		Map<String,Object> elementMap = (Map<String, Object>) map.get("elementMap");
		VendorPerforTemplateEntity template = score.getPerforTemplate();
		List<VendorPerforTemplateSettingEntity> settingList = vendorPerforTemplateSettingDao.findByTemplateIdAndEnableStatus(template.getId(),StatusConstant.STATUS_YES);
		List<VendorPerforScoresTotalEntity> resultList = getWaitingCountResult(score.getCycleId());
		//评估开始和结束时间
		//VendorPerforCycleEntity cycle = score.getCycleEntity();
		//Timestamp[] assessTimeArray = PerformanceParseUtil.parseAssessTime(cycle.getCode(),-1);
		//Timestamp startTime = assessTimeArray[0];
		//Timestamp endTime = assessTimeArray[1];
		for(VendorPerforScoresTotalEntity result : resultList){
			logMsg.append("开始计算供应商：").append(result.getOrgName()).append("指标:\n");
			//删除当前供应商的指标得分
			deleteOldIndex(result,score);
			for(VendorPerforTemplateSettingEntity setting : settingList){
				//维度不计算
				if(setting.getSourceType().equals(PerformanceTypeConstant.PER_TEMPLATE_SET_DIM)){
					continue;
				}
				//指标的话需要分析元素，使用正则分析
				setting = dealWithFormula(setting);
			    String formula = setting.getFormula();
			    String keyMapKey = getKeyMapKey(result, setting.getId());
				formula =  PerformanceParseUtil.parseFormula(formula,keyMapKey,keyMap,setting.getId()+"",elementMap);
				Double indexScore = PerformanceParseUtil.countFormula(formula);
				if(indexScore<0){
					indexScore = 0d;
				}
				//保存指标得分
				VendorPerforScoresIndexEntity indexScoreEntity = createIndexScore(setting,result,score,indexScore) ;
				indexScoreEntity = vendorPerforScoresIndexDao.save(indexScoreEntity);
				logMsg.append("\t计算指标：").append(setting.getName()).append("完成!\n");
			}
	    }
		score.setCountStatus(StatusConstant.STATUS_PART);
		saveCountLog(score, logMsg.toString(), false);
		return map;
	}
	
	private void deleteOldIndex(VendorPerforScoresTotalEntity result, VendorPerforScoresEntity score) {
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN) {
			vendorPerforScoresIndexDao.deleteByOrg(result.getOrgId(),score.getId());
		}
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE) {
			vendorPerforScoresIndexDao.deleteByOrgAndMaterial(result.getOrgId(),result.getMaterialTypeId(),score.getId());
		}
	}
	
	private void deleteOldDim(VendorPerforScoresTotalEntity result, VendorPerforScoresEntity scoreList) {
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN) {
			vendorPerforScoresDimDao.deleteByOrg(result.getOrgId(), scoreList.getId());
		}
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE) {
			vendorPerforScoresDimDao.deleteByOrgAndMaterial(result.getOrgId(),result.getMaterialTypeId(),scoreList.getId());
		}
	}
	
	private void deleteOldTotal(VendorPerforScoresTotalEntity result, VendorPerforScoresEntity scoreList) {
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN) {
			vendorPerforScoresTotalDao.deleteByOrg(result.getOrgId(), scoreList.getId());
		}
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE) {
			vendorPerforScoresTotalDao.deleteByOrgAndMaterial(result.getOrgId(),result.getMaterialTypeId(),scoreList.getId());
		}
	}


	/**
	 * 计算评估列表的维度
	 * @param id 评估列表ID
	 * @return 计算维度的结果
	 * @throws Exception 
	 */
	@Transactional(rollbackOn=Exception.class)
	public Map<String,Object> countDim(Long id) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		VendorPerforScoresEntity scoreList = dao.findOne(id);
		VendorPerforTemplateEntity template = scoreList.getPerforTemplate();
		List<VendorPerforScoresTotalEntity> resultList = getWaitingCountResult(scoreList.getCycleId());
		//如果指标还未计算，维度还不能计算
		Long indexScoreNumber = vendorPerforScoresIndexDao.findCountByScoresId(id); 
		if(indexScoreNumber<=0){
			map.put("success", false);
			map.put("msg", "指标未计算完成！");
			return map;
		}
		List<VendorPerforTemplateSettingEntity> dimSettingList = vendorPerforTemplateSettingDao.findByTemplateIdAndEnableStatusAndSourceType(template.getId(), StatusConstant.STATUS_YES,PerformanceTypeConstant.PER_TEMPLATE_SET_DIM);
		List<VendorPerforTemplateSettingEntity> dimSettingSortList = new ArrayList<VendorPerforTemplateSettingEntity>();
		sortMulDimSetting(dimSettingList,dimSettingSortList,null);
		Collections.reverse(dimSettingSortList);
		StringBuilder logMsg = new StringBuilder();
		//维度的分层级处理
		for(VendorPerforScoresTotalEntity result : resultList){
			//先删除该次评估的维度,按粒度删除
			deleteOldDim(result,scoreList);
			logMsg.append("计算供应商“").append(result.getOrgName()).append("”的维度:\n");
			for(VendorPerforTemplateSettingEntity dimSetting : dimSettingSortList){
				List<VendorPerforScoresIndexEntity> indexScoresList = getWaitingCountIndex(result,scoreList,dimSetting);
				double dimScoreV = _countDim(scoreList,dimSetting,indexScoresList,result);
				VendorPerforScoresDimEntity dimScore = createDimScore(dimSetting, result, scoreList, dimScoreV);
				vendorPerforScoresDimDao.save(dimScore);
				logMsg.append("\t计算维度“").append(dimSetting.getName()).append("”完成！\n");
			}
		}
		scoreList.setCountStatus(StatusConstant.STATUS_PART);
		saveCountLog(scoreList,logMsg.toString(),true);
		return map;
	}
	
	


	/**
	 * 计算评估列表的总分
	 * @param id 评估列表ID
	 * @return 计算维度的结果
	 * @throws Exception 
	 */
	@Transactional(rollbackOn=Exception.class)
	public Map<String,Object> countTotalScore(Long id) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("success", true);
		VendorPerforScoresEntity scoreList = dao.findOne(id);
		//VendorPerforTemplateEntity template = scoreList.getPerforTemplate();
		List<VendorPerforScoresTotalEntity> resultList = getWaitingCountResult(scoreList.getCycleId());
		//如果指标还未计算，维度还不能计算
		Long dimScoreNumber = vendorPerforScoresDimDao.findCountByScoresId(id); 
		if(dimScoreNumber<=0){
			map.put("success", false);
			map.put("msg", "维度未计算完成！");
			return map;
		}
		StringBuilder logMsg = new StringBuilder();
		List<VendorPerforScoresTotalEntity> totalList = new ArrayList<VendorPerforScoresTotalEntity>();
		//维度的分层级处理
		for(VendorPerforScoresTotalEntity result : resultList){
			//先删除该次评估的总分
			logMsg.append("计算供应商“").append(result.getOrgName()).append("”的总分:\n");
			VendorPerforScoresTotalEntity total = countVendorTotal(result,scoreList);
			totalList.add(total);
			logMsg.append("\t计算总分“").append("”完成！\n");
			
		}
		sortRanking(totalList);
		vendorPerforScoresTotalDao.save(totalList);
		scoreList.setCountStatus(StatusConstant.STATUS_YES);
		scoreList.setAssessEndDate(DateUtil.getCurrentTimestamp());
		saveCountLog(scoreList,logMsg.toString(),true);
		return map;
	}
	
    /**
     * 计算总分
     * @param result
     * @param scoreList
     * @return
     * @throws Exception
     */
	private VendorPerforScoresTotalEntity countVendorTotal(VendorPerforScoresTotalEntity result, VendorPerforScoresEntity scoreList) throws Exception {
		deleteOldTotal(result,scoreList);
		List<VendorPerforScoresDimEntity> dimScoresList = getWaitingCountDim(result,scoreList);
		double totalScoreV = _countTotal(dimScoresList,result);
		VendorPerforScoresTotalEntity totalScore = createTotalScore(result, scoreList, dimScoresList,totalScoreV);
		//totalScore = vendorPerforScoresTotalDao.save(totalScore);
		return totalScore;
		
	}

	
	


	private List<VendorPerforScoresDimEntity> getWaitingCountDim(VendorPerforScoresTotalEntity result,
			VendorPerforScoresEntity scoreList) {
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN) {
			return vendorPerforScoresDimDao.findByScoresIdAndOrgIdAndDimParentIdIsNull(scoreList.getId(),result.getOrgId());
		} else if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE) {
			return vendorPerforScoresDimDao.findByScoresIdAndOrgIdAndMaterialTypeIdAndDimParentIdIsNull(scoreList.getId(),result.getOrgId(),result.getMaterialTypeId());
		}
		return null;
	}


	/**
	 * 获得当前维度下需要计算的指标
	 * @param result 总得分结果
	 * @param scoreList 待评估项
	 * @param dimSetting 模版维度配置
	 * @return 需要计算的指标
	 */
	private List<VendorPerforScoresIndexEntity> getWaitingCountIndex(VendorPerforScoresTotalEntity result,
			VendorPerforScoresEntity scoreList, VendorPerforTemplateSettingEntity dimSetting) {
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN) {
			return vendorPerforScoresIndexDao.findByScoresIdAndDimIdAndOrgId(scoreList.getId(),dimSetting.getSourceId(),result.getOrgId());
		} else if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE) {
			return vendorPerforScoresIndexDao.findByScoresIdAndDimIdAndOrgIdAndMaterialTypeId(scoreList.getId(),dimSetting.getSourceId(),result.getOrgId(),result.getMaterialTypeId());
		}
		return null;
	}
	


	/**
	 * 排名更新
	 * @param totalList
	 */
	private void sortRanking(List<VendorPerforScoresTotalEntity> totalList) {
		Collections.sort(totalList,new Comparator<VendorPerforScoresTotalEntity>() {
			@Override
			public int compare(VendorPerforScoresTotalEntity o1, VendorPerforScoresTotalEntity o2) {
				return o2.getTotalScore().compareTo(o1.getTotalScore());
			}
		});
		//排序完成后设置排名
		int rank = 1;
		for(VendorPerforScoresTotalEntity total : totalList){
			if(rank==1){
				total.setRanking(rank);
				rank++;
				continue;
			}
			VendorPerforScoresTotalEntity totalPre = totalList.get(rank-2);
			if(total.getTotalScore().equals(totalPre.getTotalScore())){
				total.setRanking(totalPre.getRanking());
			}else{
				total.setRanking(rank);
			}
			rank++;
		}
		
		
	}

	/**
	 * 创建总得分对象
	 * @param result 参与者
	 * @param scoreList 评估列表
	 * @param dimScoresList 维度得分列表
	 * @param totalScoreV 总分
	 * @return 总得分对象
	 * @throws Exception 
	 */
	private VendorPerforScoresTotalEntity createTotalScore(VendorPerforScoresTotalEntity result,
			VendorPerforScoresEntity scoreList, List<VendorPerforScoresDimEntity> dimScoresList,
			double totalScoreV) throws Exception{
		VendorPerforScoresTotalEntity total = new VendorPerforScoresTotalEntity();
		BeanUtil.copyPropertyNotNull(result, total, "id");
		total.setScoresId(scoreList.getId());
		total.setModelId(scoreList.getModelId());
		total.setTemplateId(scoreList.getPerforTemplateId());
		total.setTotalScore(totalScoreV);
		total.setAssessDate(scoreList.getAssessStartDate());
		total.setCycle(scoreList.getCycleEntity().getCycleName());
		total.setCycleId(scoreList.getCycleEntity().getId());
		total.setLevelName(getPerformanceLevel(totalScoreV));
		total.setAdjustStatus(StatusConstant.STATUS_NO);
		total.setPublishStatus(StatusConstant.STATUS_NO);
		//total.setRanking(ranking);排名最后算
		for(VendorPerforScoresDimEntity dimScore : dimScoresList){
			VendorPerforDimensionsEntity dim = vendorPerforDimensionsDao.findOne(dimScore.getDimId());
			String methodName = "set"+StringUtils.toUperFirstChar(dim.getMappingScore());
			//利用反射设置指定的维度得分
			Method targetM = total.getClass().getMethod(methodName, Double.class);
			targetM.invoke(total, dimScore.getScore());
		}
		return total;
	}

	/**
	 * 根据分数获得评分等级
	 * @param totalScoreV 总分
	 * @return
	 */
	private String getPerformanceLevel(double totalScoreV) {
		if(totalScoreV<0d){
			totalScoreV = 0d;
		}
		int total = (int) BigDecimalUtil.roundUp(totalScoreV, 0);
		System.out.println(totalScoreV+":"+total);
		VendorPerforLevelEntity level = vendorPerforLevelDao.findByLowerLimitLessThanAndUpperLimitGreaterThanEqual(total,total);
		return level!=null?level.getLevelName():"";
	}

	/**
     * 根据维度得分运算总得分
     * @param dimScoresList 维度得分列表
     * @param result 参与者
     * @return 总得分
     */
    private double _countTotal(List<VendorPerforScoresDimEntity> dimScoresList, VendorPerforScoresTotalEntity result) {
    	double total = 0d;
    	for(VendorPerforScoresDimEntity dim : dimScoresList){
    		total = BigDecimalUtil.add(total, BigDecimalUtil.mul(dim.getScore(),BigDecimalUtil.div(dim.getWeight(),100)));
	    }
		return total;
    }

	/**
     * 将维度设置按层级排序
     * @param dimSettingList
     * @param dimSettingSortList 
     */
	private void sortMulDimSetting(List<VendorPerforTemplateSettingEntity> dimSettingList, List<VendorPerforTemplateSettingEntity> dimSettingSortList,VendorPerforTemplateSettingEntity parent) {
		if(parent!=null){
			dimSettingSortList.add(parent);
			dimSettingList.remove(parent);
		}
		for(VendorPerforTemplateSettingEntity dimSetting : new ArrayList<VendorPerforTemplateSettingEntity>(dimSettingList)){
			if(parent==null && dimSetting.getParentId()==null){
				sortMulDimSetting(dimSettingList, dimSettingSortList,dimSetting);
			}else if(parent != null && dimSetting.getParentId()!=null && dimSetting.getParentId().equals(parent.getSourceId())){
				sortMulDimSetting(dimSettingList, dimSettingSortList,dimSetting);
			}
		}
		
		
	}


	/**
	 * 用指标得分计算每个供应商的
	 * @param scoreList 
	 * @param dimSetting
	 * @param indexScoreList
	 * @param result
	 * @return
	 */
	private double _countDim(VendorPerforScoresEntity scoreList, VendorPerforTemplateSettingEntity dimSetting,
			List<VendorPerforScoresIndexEntity> indexScoreList, VendorPerforScoresTotalEntity result) {
		double score = 0d;
		if(Collections3.isEmpty(indexScoreList)){
			//如果没有指标得分，则可能为子维度
			List<VendorPerforScoresDimEntity> dimScoreList = vendorPerforScoresDimDao.findByScoresIdAndDimParentId(scoreList.getId(),dimSetting.getSourceId());
		    for(VendorPerforScoresDimEntity dimScore : dimScoreList){
		    	double scoreV = BigDecimalUtil.mul(dimScore.getScore(), BigDecimalUtil.div(dimScore.getWeight(),100));
		    	score = BigDecimalUtil.add(score, scoreV);
		    }
		}else{
			for(VendorPerforScoresIndexEntity indexScore : indexScoreList){
				Double scoreV = null;
				if(indexScore.getWeight()!=null){
				  scoreV = BigDecimalUtil.mul(indexScore.getScore(), BigDecimalUtil.div(indexScore.getWeight(),100));
				}else{
					Long size = vendorPerforTemplateSettingDao.findCountByTemplateIdAndEnableStatusAndParentId(indexScore.getTemplateId(),StatusConstant.STATUS_YES, indexScore.getDimId());
					scoreV = BigDecimalUtil.mul(indexScore.getScore(), BigDecimalUtil.div(BigDecimalUtil.div(size, 1),100));
				}
				score = BigDecimalUtil.add(score, scoreV);
			}
		}
		return score;
	}

	/**
	 * 创建一个指标得分的对象
	 * @param setting 配置
	 * @param result 参评供应商
	 * @param scoreList 评估列表对象
	 * @param indexScore 得分
	 * @return 指标得分对象
	 */
	private VendorPerforScoresIndexEntity createIndexScore(VendorPerforTemplateSettingEntity setting, VendorPerforScoresTotalEntity result, VendorPerforScoresEntity scoreList, Double indexScore) {
		VendorPerforScoresIndexEntity score = new VendorPerforScoresIndexEntity();
		score.setDimId(setting.getParentId());
		score.setIndexId(setting.getSourceId());
		score.setName(setting.getName());
		score.setOrgId(result.getOrgId());
		score.setScoresId(scoreList.getId());
		score.setTemplateId(setting.getTemplateId());
		score.setTemplateSettingId(setting.getId());
		score.setWeight(setting.getWeightNumber());
		score.setScore(indexScore);
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE){
			score.setMaterialTypeId(result.getMaterialTypeId());
		}
		return score;
	}
	
	/**
	 * 创建一个维度得分的对象
	 * @param setting 模版配置
	 * @param result 参与者
	 * @param scoreList 得分列表
	 * @param scoreV 得分
	 * @return 维度得分对象
	 */
	private VendorPerforScoresDimEntity createDimScore(VendorPerforTemplateSettingEntity setting,
			VendorPerforScoresTotalEntity result, VendorPerforScoresEntity scoreList, double scoreV) {
		VendorPerforScoresDimEntity score = new VendorPerforScoresDimEntity();
		score.setDimId(setting.getSourceId());
		score.setDimParentId(setting.getParentId());
		score.setName(setting.getName());
		score.setOrgId(result.getOrgId());
		score.setScoresId(scoreList.getId());
		score.setTemplateId(setting.getTemplateId());
		score.setTemplateSettingId(setting.getId());
		score.setWeight(setting.getWeightNumber());
		score.setScore(scoreV);
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE) {
			score.setMaterialTypeId(result.getMaterialTypeId());
		}
		return score;
	}

	/**
	 * 根据模版校验是否具备计算条件
	 * @param score 评估列表
	 * @return 校验结果
	 * @throws Exception 
	 */
	private Map<String, Object> validateCount(VendorPerforScoresEntity score) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> elementMap = new HashMap<String, Object>();
		Map<String,Object> keyMap = new HashMap<String, Object>();
		map.put("success", true);
		//先获取设置
		VendorPerforTemplateEntity template = score.getPerforTemplate();
		List<VendorPerforTemplateSettingEntity> settingList = vendorPerforTemplateSettingDao.findByTemplateIdAndEnableStatus(template.getId(),StatusConstant.STATUS_YES);
		if(Collections3.isEmpty(settingList)){
			map.put("success", false);
			map.put("msg", "模版还为设置，无法计算");
			return map;
		}
		//还要判断参评供应商
		List<VendorPerforScoresTotalEntity> resultList = getWaitingCountResult(score.getCycleId());
		if(Collections3.isEmpty(resultList)){
			map.put("success", false);
			map.put("msg", "没有符合条件的参评供应商，无法计算");
			return map;
		}
		//或得评估开始和结束时间
		VendorPerforCycleEntity cycle = score.getCycleEntity();
		Timestamp[] assessTimeArray = PerformanceParseUtil.parseAssessTime(cycle.getCode(),-1);
		Timestamp startTime = assessTimeArray[0];
		Timestamp endTime = assessTimeArray[1];
		StringBuilder logMsg = new StringBuilder();
		boolean validationFlag = true;//校验结果
		//开始校验每个供应商的指标元素
		logMsg.append("评估时间："+DateUtil.dateToString(score.getAssessStartDate()))
		.append(",评估周期："+score.getCycleEntity().getCycleName()).append(",开始进行数据校验");
		for(VendorPerforScoresTotalEntity result : resultList){
			logMsg.append("供应商“").append(result.getOrgName()).append("”开始校验所有指标:\n");
			for(VendorPerforTemplateSettingEntity setting : settingList){
				//维度没有校验的必要
				if(setting.getSourceType().equals(PerformanceTypeConstant.PER_TEMPLATE_SET_DIM)){
					continue;
				}
				logMsg.append("指标“").append(setting.getName()).append("”开始校验:\n");
				//指标的话需要分析元素，使用正则分析
				setting = dealWithFormula(setting);
			    String formula = setting.getFormula();
			    if(StringUtils.isEmpty(formula)){
			    	validationFlag = false;
			    	logMsg.append("\t<font color='#c00'>公式</font>").append("为空。\n");
			    	continue;
			    }
				Map<String,List<String>> paramMap =  PerformanceParseUtil.parseFormulaParams(formula);
				//开始判断该指标的要素是否都存在
				List<String> keyList = paramMap.get("keyList");
				List<String> elementList = paramMap.get("elementList");
				//可能要根据品牌或者物料来判断，先全局判断
				for(String key : keyList){
					//查找该要素是否存在，不存在则加入提示 TODO 如果要分级别，这里处理不一样
					VendorPerforDataInEntity keyData = getAllKeyData(result,score,setting,key,startTime,endTime);
					if(keyData==null){
						validationFlag = false;
						logMsg.append("\t<font color='#c00'>物料编号["+result.getMaterialTypeCode()+","+result.getMaterialTypeName()+"]要素[").append(key).append("]缺失。</font>\n");
					} else {
						String keyValue = keyData.getElementValue();
						//验证元素值是否为数值，如果不是数值，去取映射
						if(!MatcherUtil.isDouble(keyData.getElementValue())){
							keyValue = getKeyMappingValue(key,keyValue);
							if(keyValue==null){
								validationFlag = false;
								logMsg.append("\t<font color='#c00'>要素[").append(key).append("]映射不正确。</font>\n");
							}
						}
						String keyPre = getKeyMapKey(result,setting.getId());
						keyMap.put(keyPre+"-"+key, keyValue);
					}
				}
				for(String element : elementList){
					//查找该元素是否存在，不存在则加入提示
					VendorPerforFAllocationEntity elementData = vendorPerforFAllocationDao.findByName(element);
					if(elementData==null){
						validationFlag = false;
						logMsg.append("\t<font color='#c00'>元素[").append(element).append("]缺失。</font>\n");
					} else {
						elementMap.put(setting.getId()+"-"+element, elementData.getFallValue());
					}
					
				}
				
			}
	    }
		if(!validationFlag){
			score.setCountStatus(StatusConstant.STATUS_FAIL);
			logMsg.append("校验不通过，不能计算！");
			map.put("success", false);
			map.put("msg", "校验失败,请查看校验日志");
			map.put("logMsg", logMsg.toString());
		}
		map.put("keyMap", keyMap);
		map.put("elementMap",elementMap);
		map.put("logMsg",logMsg.toString());
		saveCountLog(score, logMsg.toString(), false);
		return map;
	}

	/**
	 * 获得映射的key名称
	 * @param result 结果
	 * @param id 配置ID
	 * @param key 要素名
	 * @return map的key
	 */
	private String getKeyMapKey(VendorPerforScoresTotalEntity result, long id) {
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN){
			return result.getOrgId()+"-"+id;
		}else if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE){
			return result.getOrgId()+"-"+result.getMaterialTypeId()+"-"+id;
		}
		return null;
	}


	/**
	 * 获得所有的要素数据
	 * @param result 结果
	 * @param score 分数
	 * @param setting 
	 * @param endTime 
	 * @param startTime 
	 * @return 要素数据
	 */
	private VendorPerforDataInEntity getAllKeyData(VendorPerforScoresTotalEntity result, VendorPerforScoresEntity score, VendorPerforTemplateSettingEntity setting, String element,Timestamp startTime, Timestamp endTime) {
		//先或得配置的计算级别
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN){
			return vendorPerforDateInDao.findByElementAndCycleIdAndIndexNameAndVendorCode(element,score.getCycleId(), setting.getName(), result.getOrgCode());
		}else if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE){
			return vendorPerforDateInDao.getElementAndCycleIdAndIndexNameAndVendorCodeAndMaterialCode(element,score.getCycleId(), setting.getName(), result.getOrgCode(),result.getMaterialTypeCode(),startTime,endTime);
		}
		return null;
	}


	/**
	 * 获取元素映射的值
	 * @param keyValue 映射值
	 * @param keyValue2 
	 * @return
	 */
	private String getKeyMappingValue(String key, String keyValue) {
		List<VendorPerforMappedEntity> list= vendorPerforMappedDao.findByMappedName(key);
		if(list!=null&&list.size()>0)
		{
			for(VendorPerforMappedEntity mapper:list)
			{
				if(mapper.getMappedType()==1)//SQL
				{
					return "sql";
				}
				else if(mapper.getMappedType()==2)//数值
				{
					if(mapper.getName().equals(keyValue))
					{
						if(StringUtils.isEmpty(mapper.getMappedValue())) {
							return null;
						}
						if(!MatcherUtil.isDouble(mapper.getMappedValue())) {
							return null;
						}
						return mapper.getMappedValue();
					}
					else
					{
						continue;
					}
				}
				else if(mapper.getMappedType()==3)//小于
				{
					try
					{
						Double dou1=Double.parseDouble(mapper.getName().replaceAll("%",""))/100;
						Double dou2=Double.parseDouble(keyValue.replaceAll("%",""))/100;
						if(dou1>dou2)
						{
							return mapper.getMappedValue();
						}
					}
					catch(Exception e)
					{
						int i=mapper.getName().compareTo(keyValue);
						if(i>0)
						{
							return mapper.getMappedValue();
						}
						else
						{
							continue;
						}
					}
				}
				else if(mapper.getMappedType()==4)//小于或等于
				{
					try
					{
						Double dou1=Double.parseDouble(mapper.getName().replaceAll("%",""))/100;
						Double dou2=Double.parseDouble(keyValue.replaceAll("%",""))/100;
						if(dou1>=dou2)
						{
							return mapper.getMappedValue();
						}
					}
					catch(Exception e)
					{
						int i=mapper.getName().compareTo(keyValue);
						if(i>=0)
						{
							return mapper.getMappedValue();
						}
						else
						{
							continue;
						}
					}
				}
				else if(mapper.getMappedType()==5)//大于
				{
					try
					{
						Double dou1=Double.parseDouble(mapper.getName().replaceAll("%",""))/100;
						Double dou2=Double.parseDouble(keyValue.replaceAll("%",""))/100;
						if(dou1<dou2)
						{
							return mapper.getMappedValue();
						}
					}
					catch(Exception e)
					{
						int i=mapper.getName().compareTo(keyValue);
						if(i<0)
						{
							return mapper.getMappedValue();
						}
						else
						{
							continue;
						}
					}
				}
				else if(mapper.getMappedType()==6)//大于或等于
				{
					try
					{
						Double dou1=Double.parseDouble(mapper.getName().replaceAll("%",""))/100;
						Double dou2=Double.parseDouble(keyValue.replaceAll("%",""))/100;
						if(dou1<=dou2)
						{
							return mapper.getMappedValue();
						}
					}
					catch(Exception e)
					{
						int i=mapper.getName().compareTo(keyValue);
						if(i<=0)
						{
							return mapper.getMappedValue();
						}
						else
						{
							continue;
						}
					}
				}
				else if(mapper.getMappedType()==7)//区间 包含上下区间
				{
					String[] mname=mapper.getName().split("~");
					if(mname.length>=2)
					{
						try
						{
							Double dou0=Double.parseDouble(mname[0].replaceAll("%",""))/100;
							Double dou1=Double.parseDouble(mname[1].replaceAll("%",""))/100;
							Double dou2=Double.parseDouble(keyValue.replaceAll("%",""))/100;
							if(dou0<=dou2&&dou2<=dou1)
							{
								return mapper.getMappedValue();
							}
						}
						catch(Exception e)
						{
							int i=mname[0].compareTo(keyValue);
							int j=mname[1].compareTo(keyValue);
							if(i<=0&&j>=0)
							{
								return mapper.getMappedValue();
							}
							else
							{
								continue;
							}
						}
					}
					else
					{
						continue;
					}
				}
				else if(mapper.getMappedType()==8)//区间不包含上下区间
				{
					String[] mname=mapper.getName().split("~");
					if(mname.length>=2)
					{
						try
						{
							Double dou0=Double.parseDouble(mname[0].replaceAll("%",""))/100;
							Double dou1=Double.parseDouble(mname[1].replaceAll("%",""))/100;
							Double dou2=Double.parseDouble(keyValue.replaceAll("%",""))/100;
							if(dou0<dou2&&dou2<dou1)
							{
								return mapper.getMappedValue();
							}
						}
						catch(Exception e)
						{
							int i=mname[0].compareTo(keyValue);
							int j=mname[1].compareTo(keyValue);
							if(i<0&&j>0)
							{
								return mapper.getMappedValue();
							}
							else
							{
								continue;
							}
						}
					}
					else
					{
						continue;
					}
				}
				else if(mapper.getMappedType()==9)//区间 包含上不包含下区间
				{
					String[] mname=mapper.getName().split("~");
					if(mname.length>=2)
					{
						try
						{
							Double dou0=Double.parseDouble(mname[0].replaceAll("%",""))/100;
							Double dou1=Double.parseDouble(mname[1].replaceAll("%",""))/100;
							Double dou2=Double.parseDouble(keyValue.replaceAll("%",""))/100;
							if(dou0<=dou2&&dou2<dou1)
							{
								return mapper.getMappedValue();
							}
						}
						catch(Exception e)
						{
							int i=mname[0].compareTo(keyValue);
							int j=mname[1].compareTo(keyValue);
							if(i<=0&&j>0)
							{
								return mapper.getMappedValue();
							}
							else
							{
								continue;
							}
						}
					}
					else
					{
						continue;
					}
				}
				else if(mapper.getMappedType()==10)//区间 不包含上包含下区间
				{
					String[] mname=mapper.getName().split("~");
					if(mname.length>=2)
					{
						try
						{
							Double dou0=Double.parseDouble(mname[0].replaceAll("%",""))/100;
							Double dou1=Double.parseDouble(mname[1].replaceAll("%",""))/100;
							Double dou2=Double.parseDouble(keyValue.replaceAll("%",""))/100;
							if(dou0<dou2&&dou1<=dou2)
							{
								return mapper.getMappedValue();
							}
						}
						catch(Exception e)
						{
							int i=mname[0].compareTo(keyValue);
							int j=mname[1].compareTo(keyValue);
							if(i<0&&j>=0)
							{
								return mapper.getMappedValue();
							}
							else
							{
								continue;
							}
						}
					}
					else
					{
						continue;
					}
				}
			}
			return null;
		}
		else
		{
			return null;
		}
	}


	/**
	 * 对配置中公式的处理，如果配置中有公式，直接使用原有公式，如果没有公式，获取最新的公式并更新到配置中
	 * @param setting 配置
	 * @return 公式
	 */
	private VendorPerforTemplateSettingEntity dealWithFormula(VendorPerforTemplateSettingEntity setting) {
		String formula = setting.getFormula();
		if(StringUtils.isNotEmpty(formula)){
			return setting;
		}
		VendorPerforFormulasEntity formulaEntity = vendorPerforFormulasDao.findByIndexIdAndCycleId(setting.getSourceId(), setting.getTemplate().getCycleId());
		if(formulaEntity==null || StringUtils.isEmpty(formulaEntity.getContent())){
			return setting;
		}
		setting.setFormula(formulaEntity.getContent());
		setting = vendorPerforTemplateSettingDao.save(setting);
		return setting;
	}
	

	/**
	 * 保存评估列表的计算日志
	 * @param scoreList 评估列表集合
	 * @param logMsg 日志内容
	 * @param append 是否追加
	 * @throws Exception
	 */
	private void saveCountLog(VendorPerforScoresEntity scoreList, String logMsg, boolean append) throws Exception {
		String logPath = LogUtil.logBussiness(logMsg, this.getClass().getName(), append);
		scoreList.setLogPath(logPath);
		dao.save(scoreList);
		
	}

	/**
	 * 获取得分集合和展示形式
	 * @param id 评估执行ID
	 * @return 此次评估的集合
	 */
	public Map<String, Object> getScoresColums(Long id) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("id", id);
		//获得评估执行的对象
		VendorPerforScoresEntity score = dao.findOne(id);
		map.put("cycle", score.getCycleEntity().getCycleName());
		map.put("assessDate", DateUtil.dateToString(score.getAssessStartDate()));
		//获得评估维度的映射
		List<VendorPerforTemplateSettingEntity> settingList = vendorPerforTemplateSettingDao.findByTemplateIdAndEnableStatusAndSourceTypeAndParentIdIsNull(score.getPerforTemplateId(), StatusConstant.STATUS_YES, PerformanceTypeConstant.PER_TEMPLATE_SET_DIM);
		Map<String,String> dimMapping = new HashMap<String, String>(); 
		for(VendorPerforTemplateSettingEntity setting : settingList){
			VendorPerforDimensionsEntity dim = vendorPerforDimensionsDao.findOne(setting.getSourceId());
			dimMapping.put(dim.getMappingScore(),dim.getDimName());
		}
		map.put("mappingScore", dimMapping);
		return map;
	}
	
	/**
	 * 获取得分集合和展示形式
	 * @param id 评估执行ID
	 * @return 此次评估的集合
	 */
	public Map<String, Object> getScoresColumsAll() {
		Map<String,Object> map = new HashMap<String, Object>();
		//获得评估维度的映射
		List<VendorPerforDimensionsEntity> dimList = vendorPerforDimensionsDao.findByParentIdIsNull();
		
		Map<String,String> dimMapping = new HashMap<String, String>(); 
		for(VendorPerforDimensionsEntity dim : dimList){
			dimMapping.put(dim.getMappingScore(),dim.getDimName());
		}
		map.put("mappingScore", dimMapping);
		return map;
	}

    /**
     * 获得本次评估的得分结果
     * @param pageNumber 页数
     * @param pageSize 每页最大记录
     * @param searchParamMap 查询参数
     * @return 评分结果
     */
	public Page<VendorPerforScoresTotalEntity> getVendorPerforScoresTotalList(int pageNumber, int pageSize,
			Map<String, Object> searchParamMap) {
		PageRequest pageable = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String,SearchFilterEx> paramMap = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforScoresTotalEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(paramMap.values(), VendorPerforScoresTotalEntity.class);
		Page<VendorPerforScoresTotalEntity> page = vendorPerforScoresTotalDao.findAll(spec, pageable);
		return page;
	}

	/**
	 * 调分处理
	 * @param total 总得分记录
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackOn=Exception.class)
	public Map<String, Object> adjustScore(VendorPerforScoresTotalEntity total) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		//得到本次评估
		VendorPerforScoresTotalEntity oldTotal = vendorPerforScoresTotalDao.findOne(total.getId());
		//记录历史
		VendorPerforScoresTotalHisEntity hisTotal = new VendorPerforScoresTotalHisEntity();
		BeanUtil.copyPropertyNotNull(oldTotal, hisTotal, "id");
		hisTotal.setAdjustReason(total.getAdjustReason()); 
		vendorPerforScoresTotalHisDao.save(hisTotal);
		
		//BeanUtil.copyPropertyNotNull(total, oldTotal);
		//total = oldTotal;
		VendorPerforScoresEntity scoreList = dao.findOne(total.getScoresId());
		//VendorPerforReviewsEntity joiner = vendorPerforReviewsDao.findByCycleIdAndOrgId(scoreList.getCycleId(),oldTotal.getOrgId());
		//获得维度得分
		List<VendorPerforScoresDimEntity> dimScoreList = vendorPerforScoresDimDao.findByScoresIdAndDimParentIdIsNull(total.getScoresId());
		for(VendorPerforScoresDimEntity dimScore : dimScoreList){
			//通过反射处理维度分数
			VendorPerforDimensionsEntity dim = vendorPerforDimensionsDao.findOne(dimScore.getDimId());
			String mapping = dim.getMappingScore();
			Method method = VendorPerforScoresTotalEntity.class.getMethod("get"+StringUtils.toUperFirstChar(mapping));
			Double score = (Double) method.invoke(total);
			dimScore.setScore(score);
		}
		vendorPerforScoresDimDao.save(dimScoreList);
		String adjustReason = total.getAdjustReason();
		total = countVendorTotal(oldTotal,scoreList);
		total.setAdjustReason(adjustReason);
		total.setAdjustStatus(StatusConstant.STATUS_YES);
		vendorPerforScoresTotalDao.save(total);
		
		List<VendorPerforScoresTotalEntity> totalList = vendorPerforScoresTotalDao.findByScoresId(scoreList.getId());
		sortRanking(totalList);
		vendorPerforScoresTotalDao.save(totalList);
		return map;
	}

	/**
	 * 获得指标得分
	 * @param totalId 总得分ID 
	 * @param mapping 映射的维度
	 * @return 指标得分
	 */
	public List<VendorPerforScoresIndexEntity> getIndexScore(Long totalId, String mapping) {
		VendorPerforScoresTotalEntity total = vendorPerforScoresTotalDao.findOne(totalId);
		VendorPerforDimensionsEntity dim = vendorPerforDimensionsDao.findByMappingScore(mapping);
		List<Long> dimScoreIdList = new ArrayList<Long>();
		recurDimScore(dim,total.getOrgId(),total.getScoresId(),dimScoreIdList);
		List<VendorPerforScoresIndexEntity> indexList = vendorPerforScoresIndexDao.findByScoresIdAndOrgIdAndDimIdIn(total.getScoresId(),total.getOrgId(),dimScoreIdList);
		if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE) {
			indexList = vendorPerforScoresIndexDao.findByScoresIdAndOrgIdAndMaterialTypeIdAndDimIdIn(total.getScoresId(),total.getOrgId(),total.getMaterialTypeId(),dimScoreIdList);
		}
		return indexList;
	}

	private void recurDimScore(VendorPerforDimensionsEntity dim, Long orgId,
			Long scoresId, List<Long> dimScoreIdList) {
		List<VendorPerforScoresDimEntity> dimList = vendorPerforScoresDimDao.findByScoresIdAndOrgIdAndDimParentId(scoresId, orgId,dim.getId());
		if(Collections3.isNotEmpty(dimList)){
			for(VendorPerforScoresDimEntity dimScore : dimList){
				VendorPerforDimensionsEntity dimS = vendorPerforDimensionsDao.findOne(dimScore.getDimId());
				recurDimScore(dimS, orgId,
						scoresId, dimScoreIdList);
			}
		}else{
			dimScoreIdList.add(dim.getId());
		}
	}
	
	/**
	 * 根据周期获得待计算的结果
	 * @param cycleId 周期ID
	 * @return 待计算结果
	 */
	private List<VendorPerforScoresTotalEntity> getWaitingCountResult(Long cycleId){
		List<VendorPerforReviewsEntity> joinVendorList = vendorPerforReviewsDao.findByCycleIdAndJoinStatus(cycleId,StatusConstant.STATUS_YES);
		List<VendorPerforScoresTotalEntity> resultList = new ArrayList<VendorPerforScoresTotalEntity>();
	    //先或得配置的计算级别
		for(VendorPerforReviewsEntity join : joinVendorList){
			if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN){
				VendorPerforScoresTotalEntity total = new VendorPerforScoresTotalEntity();
				total.setOrgId(join.getOrgId());
				total.setOrgName(join.getOrgName());
				total.setOrgCode(join.getOrgCode());
				total.setCycleId(cycleId);
				resultList.add(total);
			}else if(COUNT_LEVEL.intValue()==PerformanceTypeConstant.PERFORMANCE_LEVEL_VEN_MATTYPE){
				List<VendorMaterialTypeRelEntity> typeList = vendorMaterialTypeRelDao.findByOrgId(join.getOrgId());
				if(Collections3.isEmpty(typeList)) {
					continue;
				}
				for(VendorMaterialTypeRelEntity type : typeList){
					VendorPerforScoresTotalEntity total = new VendorPerforScoresTotalEntity();
					total.setOrgId(join.getOrgId());
					total.setOrgName(join.getOrgName());
					total.setOrgCode(join.getOrgCode());
					total.setCycleId(cycleId);
					total.setMaterialTypeId(type.getMaterialTypeId());
					total.setMaterialTypeName(type.getMaterialTypeName());
					total.setMaterialTypeCode(type.getMaterialTypeCode());
					resultList.add(total);
				}
			}
		}
		return resultList;
	}

	/**
	 * 发布结果
	 * @param ids 列表ID
	 * @return 发布结果
	 */
	public Map<String, Object> publish(List<Long> ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		StringBuilder msg = new StringBuilder();
		for(Long id : ids){
			VendorPerforScoresEntity score = dao.findOne(id);
			if(score.getAssessEndDate()==null){
				msg.append("评估周期："+score.getCycleEntity().getCycleName()+" 评估时间:"+DateUtil.dateToString(score.getAssessStartDate())+" 还未评估完成不能发布");
				continue;
			}
			score.setPublishStatus(StatusConstant.STATUS_YES);
			//找到总得分发布
			List<VendorPerforScoresTotalEntity> totalList = vendorPerforScoresTotalDao.findByScoresId(score.getId());
			for(VendorPerforScoresTotalEntity total : totalList){
				total.setPublishStatus(StatusConstant.STATUS_YES);
			}
			vendorPerforScoresTotalDao.save(totalList);
			dao.save(score);
		}
		map.put("msg", "发布成功!<br>"+msg.toString());
		return map;
	}
	
	/**
	 * 发布总得分
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> publishTotal(List<Long> ids)  throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		
		StringBuilder msg = new StringBuilder();
		for(Long id : ids){
			VendorPerforScoresTotalEntity score = vendorPerforScoresTotalDao.findOne(id);
			if(score.getPublishStatus() != null && score.getPublishStatus() == StatusConstant.STATUS_YES) {
				throw new Exception("包含已发布的记录，不能重复发布！");
			}
			score.setPublishStatus(StatusConstant.STATUS_YES);
			vendorPerforScoresTotalDao.save(score);
		}
		map.put("success", true);
		map.put("msg", "发布成功!<br>"+msg.toString());
		return map;
	}
	
	/**
	 * 提交整改
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public Boolean correction(String url,String correntIds, String requireDate, String correctionContent)  throws Exception{
		boolean juct=false;
		if(StringUtils.isEmpty(correntIds)) {
			throw new Exception("选择整改内容为空！");
		}
		
		String [] ids = StringUtils.split(correntIds, ",");
		List<VendorPerforScoresTotalEntity> list = Lists.newArrayList();
		VendorPerforScoresTotalEntity score = null;
		Timestamp requireDateTime = DateUtil.stringToTimestamp(requireDate, DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
		for(String id : ids){
			score = vendorPerforScoresTotalDao.findOne(StringUtils.convertLong(id));
			if(score.getCorrectionStatus() != null && score.getCorrectionStatus() == StatusConstant.STATUS_YES) {
				throw new Exception("包含已提交整改的记录，不能重复提交整改！");
			}
			score.setCorrectionStatus(StatusConstant.STATUS_YES);
			vendorPerforScoresTotalDao.save(score);
			score.setRequireDate(requireDateTime);
			score.setCorrectionContent(correctionContent); 
			list.add(score);
		}
		//提交整改
		juct=vendorPerforCorrectionService.addVendorPerforCorrectionEntity(list,url);
		return juct;
	}
	
	/**
	 * 取消发布结果
	 * @param ids 列表ID
	 * @return 发布结果
	 */
	public Map<String, Object> unPublish(List<Long> ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		for(Long id : ids){
			VendorPerforScoresEntity score = dao.findOne(id);
			//找到总得分发布
			List<VendorPerforScoresTotalEntity> totalList = vendorPerforScoresTotalDao.findByScoresId(score.getId());
			for(VendorPerforScoresTotalEntity total : totalList){
				total.setPublishStatus(StatusConstant.STATUS_NO);
			}
			vendorPerforScoresTotalDao.save(totalList);
			score.setPublishStatus(StatusConstant.STATUS_NO);
			dao.save(score);
		}
		map.put("success", "取消发布成功!");
		return map;
	}

	public Map<String, Object> sjtx(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VendorPerforScoresTotalEntity> getVendorScoresTotalList(Map<String, Object> searchParamMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public void exportTotalList(VendorPerforScoresTotalEntity total, ServletOutputStream outputStream) {
		// TODO Auto-generated method stub
		
	}

	public List<VendorPerforScoresTotalVo2> getVendorPerforScoresTotalVo(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforScoresTotalEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforScoresTotalEntity.class);
		List<VendorPerforScoresTotalEntity>  page= vendorPerforScoresTotalDao.findAll(spec);
		List<VendorPerforScoresTotalVo2> list=new ArrayList<VendorPerforScoresTotalVo2>();
		for(VendorPerforScoresTotalEntity to:page)
		{
			VendorPerforScoresTotalVo2 vo=new VendorPerforScoresTotalVo2();
			vo.setCol1(to.getCycle()==null?"":to.getCycle()+"");
			vo.setCol2(""+to.getAssessDate());
			vo.setCol3(to.getOrgCode()==null?"":to.getOrgCode()+"");
			vo.setCol4(to.getOrgName()==null?"":to.getOrgName()+"");
			vo.setCol5(""+to.getTotalScore()==null?"":to.getTotalScore()+"");
			vo.setCol6(""+to.getRanking()==null?"":to.getRanking()+"");
			vo.setCol7(to.getScore4()==null?"":to.getScore4()+"");
			vo.setCol8(to.getScore2()==null?"":to.getScore2()+"");
//			vo.setCol9(to.getScore3()==null?"":to.getScore3()+"");
			vo.setCol10(to.getScore1()==null?"":to.getScore1()+"");
			list.add(vo);
		}
		return list;
	}
}
