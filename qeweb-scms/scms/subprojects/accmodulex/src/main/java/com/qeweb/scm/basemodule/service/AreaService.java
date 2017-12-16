package com.qeweb.scm.basemodule.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.AreaEntity;
import com.qeweb.scm.basemodule.repository.AreaDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
/**
 * 区域Service
 * @author lw
 * 创建时间：2015年6月2日14:18:53
 * 最后更新时间：2015年6月8日 10:24:40
 * 最后更新人：lw
 */
@Service
@Transactional
public class AreaService {
	
	@Autowired
	private AreaDao areaDao;


	public Page<AreaEntity> getAreaList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
//		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "id","desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<AreaEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), AreaEntity.class);
		Page<AreaEntity> pageArea = areaDao.findAll(spec,pagin);
		for(int i = 0; i < pageArea.getContent().size(); i++){
			AreaEntity entity = pageArea.getContent().get(i);
//			AreaEntity upEntity = areaDao.findOne(entity.getParentId());
			//供查询页面展示上级完整地区
			pageArea.getContent().get(i).setUpName(getWholeArea(entity.getParentId()));
		}
		return pageArea;
	}

	public void addNewArea(AreaEntity area) {
		if(area.getParentId()==null){
			area.setParentId(0l);
		}
		areaDao.save(area);
	}

	public List<AreaEntity> getAreaByLevel(Integer level){
		List<AreaEntity> areaList = null;
		if(level != null){
			areaList = areaDao.findByLevel(level);
		}
		return areaList;
	}
	/**
	 * 根据上级id查询区域
	 * @param parentId
	 * @return
	 */
	public List<AreaEntity> getAreaByParentId(Long parentId){
		List<AreaEntity> areaList = null;
		if(parentId != null){
			areaList = areaDao.getAreaEntity(parentId);
		}
		return areaList;
	}
	/**
	 * 根据区域id查询到该区域对应的完整地区
	 * @param id
	 * @return String
	 */
	public String getWholeArea(Long id){
		String area = "";
		AreaEntity entity = getArea(id);
		if(entity==null){
			return area;
		}
		area = entity.getName()+area;
		while(entity.getParentId()!=0){
			entity = getArea(entity.getParentId());
			if(entity != null)
				area = entity.getName()+area;
		}
		return area;
	}
	public AreaEntity getArea(Long id) {
		return areaDao.findOne(id);
	}

	public void updateArea(AreaEntity area) {
		areaDao.save(area);
	}

	public void deleteAreaList(List<AreaEntity> areaList) {
		//areaDao.delete(areaList);
		for (AreaEntity areaEntity : areaList) {
			areaDao.abolish(areaEntity.getId());
		}
	}

}
