package com.qeweb.scm.vendorperformancemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforLevelEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforLevelDao;

@Service
@Transactional
public class VendorPerforLevelService {
	
	@Autowired
	private VendorPerforLevelDao levelDao;
	
	@Autowired
	private SerialNumberService serialNumberService;

	public Page<VendorPerforLevelEntity> getLevelList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforLevelEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforLevelEntity.class);
		Page<VendorPerforLevelEntity> levelEntityPage = levelDao.findAll(spec,pagin);
		return levelEntityPage;
	}

	public String getminAndmax() {
		Integer max=levelDao.getLevelEntityMax(0);
		if(max==null)
		{
			return "0,0";
		}
		else
		{
			VendorPerforLevelEntity levelEntity=levelDao.findByUpperLimitAndAbolished(max, 0);
			return max+","+levelEntity.getId();
		}
	}

	public Map<String,Object> addLevel(VendorPerforLevelEntity levelEntity) {
		Map<String,Object> map=new HashMap<String, Object>();
		if(levelEntity.getLowerLimit()==levelEntity.getUpperLimit()||levelEntity.getLowerLimit()>=100)
		{
			map.put("success",false);
			map.put("msg", "添加失败！100已经是顶部了.");
			return map;
		}
		else
		{
			if(levelEntity.getCode()==null||levelEntity.getCode().equals(""))
			{
				levelEntity.setCode(getSerialNumberService().geneterNextNumberByKey(OddNumbersConstant.LEV));
			}
			levelDao.save(levelEntity);
			map.put("success",true);
			map.put("msg", "添加成功");
			return map;
		}
	}

	public String updateLevelStart(VendorPerforLevelEntity levelEntity) {
		levelEntity=levelDao.findOne(levelEntity.getId());
		VendorPerforLevelEntity levelEntity1=levelDao.findByLowerLimitAndAbolished(levelEntity.getUpperLimit(),0);
		String data="";
		data=levelEntity.getId()+",";
		data=data+levelEntity.getCode()+",";
		data=data+levelEntity.getLevelName()+",";
		data=data+levelEntity.getLowerLimit()+",";
		data=data+levelEntity.getUpperLimit()+",";
		data=data+levelEntity.getQuadrant()+",";
		if(levelEntity.getRemarks()==null)
		{
			levelEntity.setRemarks("");
		}
		data=data+levelEntity.getRemarks()+",";
		data=data+levelEntity.getAbolished()+",";
		if(levelEntity1!=null&&!(levelEntity1.getId()==levelEntity.getId()))
		{
			data=data+(levelEntity1.getUpperLimit()-1)+",";
		}
		else
		{
			data=data+"100,";
		}
		data=data+levelEntity.getFatherId();
		return data;
	}

	public Map<String,Object> updateLevel(VendorPerforLevelEntity levelEntity) {
		Map<String,Object> map=new HashMap<String, Object>();
		VendorPerforLevelEntity levelEntity1=levelDao.findOne(levelEntity.getId());
		VendorPerforLevelEntity levelEntity2=levelDao.findByLowerLimitAndAbolished(levelEntity1.getUpperLimit(),0);
		if(levelEntity2!=null)
		{
			levelDao.updateLevel(levelEntity.getUpperLimit(), DateUtil.getCurrentTimestamp(), levelEntity2.getId());
		}

		levelDao.updateLevel2(levelEntity.getLevelName(), levelEntity.getUpperLimit(),levelEntity.getQuadrant() ,levelEntity.getRemarks(),DateUtil.getCurrentTimestamp(), levelEntity.getId());
		map.put("success",true);
		map.put("msg", "修改成功");
		return map;
	}

	public String releaseLevel(List<VendorPerforLevelEntity> levelEntitys) {
		for(VendorPerforLevelEntity levelEntity:levelEntitys)
		{
			if(!juctLevels(levelEntity))
			{
				return "启用中断！<br/>原因：名称为“"+levelEntity.getLevelName()+"”的上一等级有作废的，请启用它们后在启用";
			}
			if(!juctLevels2(levelEntity))
			{
				return "启用中断！<br/>原因：名称为“"+levelEntity.getLevelName()+"”的同一等级有启用的,请禁用它们后在启用";
			}
			levelDao.updateLevelabolished(0, DateUtil.getCurrentTimestamp(), levelEntity.getId());
		}
		return "1";
	}

	public String delsLevel(List<VendorPerforLevelEntity> levelEntitys) {
		List<VendorPerforLevelEntity> list = null;
		VendorPerforLevelEntity levelEntity = null;
		for(VendorPerforLevelEntity level:levelEntitys) {
			list=new ArrayList<VendorPerforLevelEntity>();
			VendorPerforLevelEntity l= levelDao.findByFatherIdAndAbolished(level.getId(),0);
			if(l!=null) {
				list.add(l);
				delsLevel(list);
			}
			levelEntity = levelDao.findOne(level.getId());
			levelEntity.setAbolished(1);
			levelDao.save(levelEntity); 
		}
		return "1";
	}
	
	private Boolean juctLevels(VendorPerforLevelEntity levelEntity)
	{
		if(levelEntity.getFatherId()==0)
		{
			return true;
		}
		VendorPerforLevelEntity levelEntity2=levelDao.findByIdAndAbolished(levelEntity.getFatherId(),0);
		if(levelEntity2!=null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private Boolean juctLevels2(VendorPerforLevelEntity levelEntity)
	{
		VendorPerforLevelEntity levelEntity2=levelDao.findByFatherIdAndAbolished(levelEntity.getFatherId(),0);
		if(levelEntity2!=null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public SerialNumberService getSerialNumberService() {
		return serialNumberService;
	}

	public void setSerialNumberService(SerialNumberService serialNumberService) {
		this.serialNumberService = serialNumberService;
	}

	public String getVendorPerforLevel() {
		List<VendorPerforLevelEntity> iterable=levelDao.findByAbolishedOrderByIdAsc(0);
		String data="[";
		int i=0;
		for(VendorPerforLevelEntity entity:iterable){
			if(i>0)
			{
				data=data+",";
			}
			data=data+"{\"id\":\""+entity.getLevelName()+"\",\"text\":\""+entity.getLevelName()+"\"}";
					i++;
		}
		data=data+"]";
		return data;  
	}
	
}
