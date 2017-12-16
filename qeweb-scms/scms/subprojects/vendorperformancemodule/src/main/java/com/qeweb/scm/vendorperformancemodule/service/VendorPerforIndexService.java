package com.qeweb.scm.vendorperformancemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qeweb.scm.basemodule.constants.OddNumbersConstant;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDimensionsEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFormulasEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforIndexEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforTemplateSettingEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforCycleDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDimensionsDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforFormulasDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforIndexDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforTemplateSettingDao;

@Service
@Transactional
public class VendorPerforIndexService {
	
	@Autowired
	private VendorPerforIndexDao dao;
	
	@Autowired
	private VendorPerforDimensionsDao dimensionsDao;
	
	@Autowired
	private VendorPerforFormulasDao formulasDao;
	
	@Autowired
	private VendorPerforCycleDao cycleDao;
	
	@Autowired
	private VendorPerforTemplateSettingDao vendorPerforTemplateSettingDao;
	
	@Autowired
	private SerialNumberService serialNumberService;

	public Page<VendorPerforIndexEntity> getVendorPerforIndexList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforIndexEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforIndexEntity.class);
		Page<VendorPerforIndexEntity> vendorPerforIndexEntityPage = dao.findAll(spec,pagin);
		return vendorPerforIndexEntityPage;
	}

	public Map<String,Object> addVendorPerforIndex(VendorPerforIndexEntity vendorPerforIndex, String[] cycleIds, String[] contents) {
		Map<String,Object> map=new HashMap<String, Object>();
		VendorPerforIndexEntity nameSameEntity = null;
		vendorPerforIndex.setCode(getSerialNumberService().geneterNextNumberByKey(OddNumbersConstant.VPI));
		if(vendorPerforIndex.getId()>0) {
			nameSameEntity=dao.findByIndexNameAndIdNot(vendorPerforIndex.getIndexName(),vendorPerforIndex.getId());
		}else{
			nameSameEntity=dao.findByIndexName(vendorPerforIndex.getIndexName());
		}
		if(nameSameEntity!=null) {
			map.put("success", false);
			map.put("msg", "名称有重复的!请重新填写");
			return map;
		}
		dao.save(vendorPerforIndex);
		List<VendorPerforTemplateSettingEntity> settingList = null;
		if(cycleIds!=null) {
			for(int i=0;i<cycleIds.length;i++) {
				VendorPerforFormulasEntity va=formulasDao.findByIndexIdAndCycleId(vendorPerforIndex.getId(), Long.parseLong(cycleIds[i]));
				if(va!=null)
				{
					VendorPerforCycleEntity cyc=cycleDao.findOne(Long.parseLong(cycleIds[i]));
					throw new RuntimeException("指标名称："+vendorPerforIndex.getIndexName()+"和周期："+cyc.getCycleName()+"的公式相同");
				}
				else
				{
					va=new VendorPerforFormulasEntity();
				}
				va.setCycleId(Long.parseLong(cycleIds[i]));
				va.setContent(contents[i]);
				va.setIndexId(vendorPerforIndex.getId());
				formulasDao.save(va);
				//更新模版公式设置
				settingList = vendorPerforTemplateSettingDao.findBySourceIdAndCycleId(va.getIndexId(), va.getCycleId());
				if(CollectionUtils.isEmpty(settingList))
					continue;
				
				for(VendorPerforTemplateSettingEntity setting : settingList) {
					setting.setFormula(va.getContent()); 
				}
				vendorPerforTemplateSettingDao.save(settingList);
			}
		}
		map.put("success", true);
		map.put("msg", "操作成功！");
		return map;
	}

	public VendorPerforIndexEntity updateVendorPerforIndexStart2(long id) {
		VendorPerforIndexEntity entity=dao.findOne(id);
		return  entity;
	}
	
	public List<VendorPerforFormulasEntity> updateVendorPerforIndexStart(long id) {
		List<VendorPerforFormulasEntity> entitys=formulasDao.findByIndexId(id);
		return  entitys;
	}

	public  Map<String,Object> updateVendorPerforIndex(String[] cycleIds, String[] contents, VendorPerforIndexEntity vendorPerforIndexEntity) {
		Map<String,Object> map=new HashMap<String, Object>();
		List<VendorPerforFormulasEntity> entitys=formulasDao.findByIndexId(vendorPerforIndexEntity.getId());
		formulasDao.delete(entitys);
		map = addVendorPerforIndex(vendorPerforIndexEntity, cycleIds, contents);
		return map;
	}

	public String deleteVendorPerforIndex(List<VendorPerforIndexEntity> vendorPerforIndexEntitys) {
		for(VendorPerforIndexEntity v:vendorPerforIndexEntitys)
		{
			dao.abolish(v.getId());
		}
		return "1";
	}
	public String deleteList(List<VendorPerforIndexEntity> vendorPerforIndexEntitys) {
		dao.delete(vendorPerforIndexEntitys);
		return "1";
	}

	public String getDimensions() {
		List<VendorPerforDimensionsEntity> iterable=(List<VendorPerforDimensionsEntity>) dimensionsDao.findAll();
		String data="[";
		int i=0;
		for(VendorPerforDimensionsEntity dimensionsEntity:iterable){
			if(i>0)
			{
				data=data+",";
			}
			data=data+"{\"id\":\""+dimensionsEntity.getId()+"\",\"text\":\""+dimensionsEntity.getDimName()+"\"}";
					i++;
		}
		data=data+"]";
		return data;  
	}

	public String getVendorPerforCycle() {
		List<VendorPerforCycleEntity> iterable=(List<VendorPerforCycleEntity>) cycleDao.findAll();
		String data="[";
		int i=0;
		for(VendorPerforCycleEntity cycleEntity:iterable){
			if(i>0)
			{
				data=data+",";
			}
			data=data+"{\"id\":\""+cycleEntity.getId()+"\",\"text\":\""+cycleEntity.getCycleName()+"\"}";
					i++;
		}
		data=data+"]";
		return data;  
	}

	public SerialNumberService getSerialNumberService() {
		return serialNumberService;
	}

	public void setSerialNumberService(SerialNumberService serialNumberService) {
		this.serialNumberService = serialNumberService;
	}

	public List<VendorPerforIndexEntity> getList(Long dimId) {
		Map<String, Object> param = Maps.newHashMap(); 
		if(dimId > 0)
			param.put("EQ_dimensionsId", dimId + "");  
		Map<String, SearchFilter> filters = SearchFilter.parse(param);
		Specification<VendorPerforIndexEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforIndexEntity.class);
		return (List<VendorPerforIndexEntity>) dao.findAll(spec);
	}

	/**
	 * 获取指标下的要素
	 * @param indexId
	 * @return
	 */
	public List<Map<String, String>> getkeyList(Long indexId) {
		List<VendorPerforFormulasEntity> list = formulasDao.findByIndexId(indexId);
		List<String> formulsSet = new ArrayList<String>();
		if(CollectionUtils.isEmpty(list))
			return Lists.newArrayList();
		
		String find = "(?<=\\[).*?(?=\\])";
		Pattern pattern = Pattern.compile(find);
		Matcher matcher = null;
		for(VendorPerforFormulasEntity entity : list) {
			if(StringUtils.isEmpty(entity.getContent()))
				continue;
			
			matcher = pattern.matcher(entity.getContent());
	        while(matcher.find()) {
	        	formulsSet.add(matcher.group());
	        }
		}
		List<Map<String, String>> mapList = Lists.newArrayList();
		Map<String, String> map = null;
		for(String key : formulsSet) {
			map = Maps.newHashMap();
			map.put("key", key);
			map.put("val", key);
			mapList.add(map);
		}
		return mapList;
	}
}
