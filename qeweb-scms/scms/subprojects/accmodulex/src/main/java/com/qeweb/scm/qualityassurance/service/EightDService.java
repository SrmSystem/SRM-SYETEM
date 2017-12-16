package com.qeweb.scm.qualityassurance.service;

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
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.qualityassurance.entity.EightDRectification;
import com.qeweb.scm.qualityassurance.entity.EightDReport;
import com.qeweb.scm.qualityassurance.entity.EightDReportDetail;
import com.qeweb.scm.qualityassurance.repository.EightDDao;
import com.qeweb.scm.qualityassurance.repository.EightDReportDao;
import com.qeweb.scm.qualityassurance.repository.EightDReportDetailDao;

@Service
@Transactional
public class EightDService {

	@Autowired
	private EightDDao eightDDao;
	@Autowired
	private EightDReportDetailDao eightDReportDetailDao;
	@Autowired
	private EightDReportDao eightDReportDao;
	
	public  Page<EightDRectification> getEightDList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EightDRectification> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EightDRectification.class);
		return eightDDao.findAll(spec, pagin);
	}

	public void saveEightD(EightDRectification rectification) {
		eightDDao.save(rectification);
	}

	public EightDRectification getRectification(Long id) {
		return eightDDao.findOne(id);
	}

	public void addReportDetails(List<EightDReportDetail> reportDetailList) {
		eightDReportDetailDao.save(reportDetailList);
	}

	public void saveEightDReport(EightDReport report) {
		eightDReportDao.save(report);
		
	}

	public void deleteEightD(Long id) {
		List<EightDReport> reports = eightDReportDao.getReportsByEightId(id);
		if(reports != null && reports.size() > 0){
			eightDReportDao.delete(reports);
		}
		List<EightDReportDetail> reportDetailList =  eightDReportDetailDao.getDetailsByEightId(id);
		if(reportDetailList != null && reportDetailList.size() > 0){
			eightDReportDetailDao.delete(reportDetailList);
		}
		eightDDao.delete(id);
		
	}

	public EightDReport getReport(Long id) {
		List<EightDReport> list = eightDReportDao.getReportsByEightId(id);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public List<EightDReportDetail> getDetails(Long id, int type) {
		return eightDReportDetailDao.getDetailsByEightIdAndType(id,type);
	}

	public Map<String, Object> fileUpLoad(String filePath,String fileName, EightDRectification rectification) {
		Map<String,Object> map = new HashMap<String, Object>();
		rectification.setStatus(0);
		rectification.setPublishStatus(0);
		rectification.setReproveStatus(0);
		if(fileName != null){
			rectification.setAttachmentName(fileName);
		}
		if(filePath != null){
			rectification.setAttachmentPath(filePath.replaceAll("\\\\", "/"));
		}
		eightDDao.save(rectification);
		map.put("success", true);
		map.put("message", "新增8D整改成功!");
		return map;		
	}
}
