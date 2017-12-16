package com.qeweb.scm.qualityassurance.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
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
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.qualityassurance.entity.QualityImproveEntity;
import com.qeweb.scm.qualityassurance.repository.QualityImproveDao;

@Service
@Transactional
public class QualityImproveService {

	@Autowired
	private QualityImproveDao qualityImproveDao;

	@Autowired
	private SerialNumberService serialNumberService;

	public Page<QualityImproveEntity> getQualityImproveList(int pageNumber, int pageSize,
			Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<QualityImproveEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(),
				QualityImproveEntity.class);
		return qualityImproveDao.findAll(spec, pagin);
	}

	public QualityImproveEntity getQualityImprove(Long id) {
		return qualityImproveDao.findOne(id);
	}

	// 上传通知 这块
	public Map<String, Object> informUpLoad(File savefile, String fileName, QualityImproveEntity qualityImproveEntity) {
		Map<String, Object> map = new HashMap<String, Object>();
		qualityImproveEntity.setImproveStatus(0);
		qualityImproveEntity.setDataStatus(0);
		qualityImproveEntity.setInformFileName(fileName);
		qualityImproveEntity.setInformFilePath(savefile.getPath().replaceAll("\\\\", "/"));
		qualityImproveDao.save(qualityImproveEntity);
		map.put("success", true);
		map.put("msg", "通知新增成功!");
		return map;
	}

	// 修改通知
	public Map<String, Object> modInform(File informFile, String fileName, QualityImproveEntity qualityImproveEntity) {
		Map<String, Object> map = new HashMap<String, Object>();
		QualityImproveEntity oldInformEntity = qualityImproveDao.findOne(qualityImproveEntity.getId());
		oldInformEntity.setVendor(qualityImproveEntity.getVendor());
		if (informFile != null && informFile.length() > 0) {
			oldInformEntity.setInformFileName(fileName);
			oldInformEntity.setInformFilePath(informFile.getPath().replaceAll("\\\\", "/"));
		}
		qualityImproveDao.save(oldInformEntity);
		map.put("success", true);
		map.put("msg", "通知修改成功!");
		return map;
	}

	/**
	 * 发布通知
	 * 
	 * @param informs
	 */
	public void publishInforms(List<QualityImproveEntity> informs) {
		for (QualityImproveEntity inform : informs) {
			inform.setDataStatus(1);
		}
		qualityImproveDao.save(informs);
	}

	/**
	 * 关闭通知
	 * 
	 * @param informs
	 */
	public void closeInforms(List<QualityImproveEntity> informs) {
		for (QualityImproveEntity inform : informs) {
			inform.setDataStatus(-1);
		}
		qualityImproveDao.save(informs);
	}

	/**
	 * 废除通知
	 * 
	 * @param informs
	 */
	public void abolishInforms(List<QualityImproveEntity> informs) {
		for (QualityImproveEntity inform : informs) {
			qualityImproveDao.abolish(inform.getId());
		}
	}

	// 上传/修改质量改进方案 这块
	public Map<String, Object> saveImprove(File savefile, QualityImproveEntity qualityImproveEntity) {
		Map<String, Object> map = new HashMap<String, Object>();
//		qualityImproveEntity.setImproveFileName(savefile.getName());
		qualityImproveEntity.setImproveFilePath(savefile.getPath().replaceAll("\\\\", "/"));
		qualityImproveEntity.setImproveStatus(1);
		qualityImproveDao.save(qualityImproveEntity);
		map.put("success", true);
		map.put("msg", "上传/修改质量改进方案成功!");
		return map;
	}

	// 继续整改
	public Map<String, Object> conImprove(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		QualityImproveEntity conImproveEntity = qualityImproveDao.findOne(id);
		conImproveEntity.setImproveStatus(-1);

		qualityImproveDao.save(conImproveEntity);
		map.put("success", true);
		map.put("msg", "保存成功!");
		return map;
	}

	// 完成整改 这块
	public Map<String, Object> finishImprove(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		QualityImproveEntity finishImproveEntity = qualityImproveDao.findOne(id);
		finishImproveEntity.setImproveStatus(1);

		qualityImproveDao.save(finishImproveEntity);
		map.put("success", true);
		map.put("msg", "保存成功!");
		return map;
	}

}