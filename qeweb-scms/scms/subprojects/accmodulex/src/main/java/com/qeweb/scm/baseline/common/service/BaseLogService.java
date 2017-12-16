package com.qeweb.scm.baseline.common.service;

import java.io.IOException;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.baseline.common.entity.BaseLogEntity;
import com.qeweb.scm.baseline.common.repository.BaseLogDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;



@Service
@Transactional
public class BaseLogService extends BaseService{

	@Autowired
	private BaseLogDao baseLogDao;	
	
	@Autowired
	private GenerialDao generialDao;

	public Page<BaseLogEntity> getLogList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
//		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "createTime","desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BaseLogEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BaseLogEntity.class);
		return baseLogDao.findAll(spec,pagin);
	}
	
	/**
	 * 记录日志
	 * @param billId
	 * @param billType
	 * @param logContent
	 * @throws IOException
	 */
	public void saveLog(Long billId,String billType,String logContent,String optUserName,String pcName) {
		Session session = generialDao.getSession();
		LobHelper lh = session.getLobHelper();
		
		BaseLogEntity log=new BaseLogEntity();
		log.setBillId(billId);
		log.setBillType(billType);
		log.setLogContent(lh.createClob(logContent));
		log.setOptUserName(optUserName);
		log.setPcName(pcName);
		baseLogDao.save(log);
	}
	
	public void saveLoginLog(Long billId,String billType,String optUserName,String roleType,String pcName,String ip) {
		BaseLogEntity log=new BaseLogEntity();
		log.setBillId(billId);
		log.setBillType(billType);
		log.setRoleType(roleType);
		log.setPcName(pcName);
		log.setOptUserName(optUserName);
		log.setIp(ip);
		log.setAbolished(0);
		log.setCreateTime(DateUtil.getCurrentTimestamp());
		baseLogDao.save(log);
	}
	

	
}
