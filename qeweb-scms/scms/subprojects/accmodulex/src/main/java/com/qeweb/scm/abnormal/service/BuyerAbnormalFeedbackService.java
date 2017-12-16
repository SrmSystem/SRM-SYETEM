package com.qeweb.scm.abnormal.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.abnormal.entity.AbnormalFeedbackEntity;
import com.qeweb.scm.abnormal.repository.BuyerAbnormalFeedbackDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;

@Service
@Transactional
public class BuyerAbnormalFeedbackService {

	@Autowired
	private BuyerAbnormalFeedbackDao buyerAbnormalFeedbackDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	
	public Page<AbnormalFeedbackEntity> getAbnormalFeedList(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<AbnormalFeedbackEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), AbnormalFeedbackEntity.class);//bySearchFilterExNoUserData(filters.values(), BuyerAbnormalFeedbackEntity.class);
		return buyerAbnormalFeedbackDao.findAll(spec,pagin);
	}
	/**
	 * 对数据进行新增
	 * @param abfe
	 */
	public void getAddAbnormalFeed(AbnormalFeedbackEntity abfe){
		
		ShiroUser curruser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		AbnormalFeedbackEntity javabean = buyerAbnormalFeedbackDao.findById(abfe.getId());
		AbnormalFeedbackEntity addAbnormal = new AbnormalFeedbackEntity();
		if (javabean == null||javabean.equals("")) {
			addAbnormal.setIsVendor(curruser.orgRoleType);
			addAbnormal.setAbnormalFeedbackName(abfe.getAbnormalFeedbackName());
			addAbnormal.setCommentArea(abfe.getCommentArea());
			addAbnormal.setPublishStatus(0);
			addAbnormal.setCommentYn(abfe.getCommentYn());
			addAbnormal.setEffectiveStartDate(abfe.getEffectiveStartDate());
			addAbnormal.setEffectiveEndDate(abfe.getEffectiveEndDate());
			addAbnormal.setTopYn(abfe.getTopYn());
			addAbnormal.setPublishUserId(abfe.getPublishUserId());
		    addAbnormal.setVendorIds(abfe.getVendorIds());	

		}
		buyerAbnormalFeedbackDao.save(addAbnormal);
	}

	/**
	 * 对数据进行删除
	 * 2017年7月12日15:29:46
	 */
	public void getDeleteAbnormalFeed(List<AbnormalFeedbackEntity> abnormalFeedbackList){
			buyerAbnormalFeedbackDao.delete(abnormalFeedbackList);
	}
	/**
	 * 对数据进行编辑操作
	 * 2017年7月12日15:43:18
	 */
	public void getEditAbnormalFeed(AbnormalFeedbackEntity editeabfe){
		AbnormalFeedbackEntity javalist = buyerAbnormalFeedbackDao.findById(editeabfe.getId());
			javalist.setAbnormalFeedbackName(editeabfe.getAbnormalFeedbackName());
			javalist.setCommentArea(editeabfe.getCommentArea());
			javalist.setCommentYn(editeabfe.getCommentYn());
			javalist.setEffectiveEndDate(editeabfe.getEffectiveEndDate());
			javalist.setEffectiveStartDate(editeabfe.getEffectiveStartDate());
			javalist.setPublishUserId(editeabfe.getPublishUserId());
			javalist.setTopYn(editeabfe.getTopYn());
			javalist.setVendorIds(editeabfe.getVendorIds());
			buyerAbnormalFeedbackDao.save(javalist);
	}
	
	
	public AbnormalFeedbackEntity getAbnormalFeedbackId(long id){
		return buyerAbnormalFeedbackDao.findById(id);
	}
	
	/**
	 * 发布操作
	 */
	
	public void publish(List<AbnormalFeedbackEntity> abnormalFeedback){
		Timestamp curr=DateUtil.getCurrentTimestamp();
		ShiroUser curruser =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		for (AbnormalFeedbackEntity abnormalfe : abnormalFeedback) {
			AbnormalFeedbackEntity javalist = buyerAbnormalFeedbackDao.findById(abnormalfe.getId());
			javalist.setPublishStatus(1);
			javalist.setPublishTime(curr);
			javalist.setPublishUserId(curruser.id);
			javalist.setPublishName(curruser.name);
			buyerAbnormalFeedbackDao.save(javalist);
		}
		
		
	}
	
}
