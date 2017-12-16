package com.qeweb.scm.basemodule.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.FeedbackEntity;
import com.qeweb.scm.basemodule.repository.FeedbackDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.PageUtil;


@Service
@Transactional
public class FeedbackService extends BaseService{

	@Autowired
	private FeedbackDao feedbackdao;
	
	/**
	 * 获取反馈信息列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<FeedbackEntity> getFeedbacks(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<FeedbackEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), FeedbackEntity.class);
		return feedbackdao.findAll(spec,pagin);
	}
	
	public List<FeedbackEntity> getFeedbacks(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<FeedbackEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), FeedbackEntity.class);
		Sort sort = new Sort(Direction.ASC, "id");
		return feedbackdao.findAll(spec, sort);      
	}
	
	/**
	 * 获得反馈的信息
	 * @param id
	 * @return
	 */
	public FeedbackEntity getFeedback(Long id){
		FeedbackEntity feedbackInfo=feedbackdao.findOne(id);
		return  feedbackInfo;
	}
	
	/**
	 * 添加一个新的反馈信息
	 * @param feedback 反馈信息类
	 */
	public void addNewFeedback(FeedbackEntity feedback) {
		feedbackdao.save(feedback);
	}

	//add by zhangjiejun 2015.10.28 start
	/**
	 * 修改新消息显示类型
	 * @param list	反馈数据
	 * @param type	新消息显示类型
	 */
	public void changeImgType(List<FeedbackEntity> list, Integer type) {
		if(list != null && list.size() != 0){
			for (FeedbackEntity feedback : list) {
				if(feedback.getAbolished() == 0){
					feedback.setAbolished(type);
				}
			}
			feedbackdao.save(list);
		}
	}
	//add by zhangjiejun 2015.10.28 end
	
	
	public Integer findFeedBackCountByOrderItemId(Long orderItemId){
		Integer count=feedbackdao.findFeedBackCountByOrderItemId(orderItemId);
		return null==count?0:count;
	}
}
