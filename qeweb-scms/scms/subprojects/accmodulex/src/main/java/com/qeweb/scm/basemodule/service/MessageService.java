package com.qeweb.scm.basemodule.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MessageEntity;
import com.qeweb.scm.basemodule.repository.MessageDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Service
@Transactional
public class MessageService implements IMessageService{
	
	private static final Log logger = LogFactory.getLog(MessageService.class);
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private UserDao userDao;

	/**
	 * 获取消息列表
	 * @param pageNumber 页数
	 * @param pageSize 每页大小
	 * @param searchParamMap 查询参数
	 * @return
	 */
	public Page<MessageEntity> getMessageList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<MessageEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), MessageEntity.class);
		return messageDao.findAll(spec,pagin);
	}
	
	public List<MessageEntity> getMessageList(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<MessageEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), MessageEntity.class);
		return messageDao.findAll(spec);
	}

	public MessageEntity getMessageById(long id) {
		return messageDao.findOne(id);
	}

	@Override
	public void closeMessage(long messageId) {
		MessageEntity msg = getMessageById(messageId);
		msg.setIsRead(StatusConstant.STATUS_YES);
		messageDao.save(msg);
	}

	@Override
	public void sendToUsers(long moduleId, String message, long fromUserId, String[] toUserCodes) {
		List<Long> list = userDao.findUserIdByCode(toUserCodes);
		if(CollectionUtils.isEmpty(list))
			return;
		
		sendToUsers(moduleId, message, fromUserId, list.toArray(new Long[]{}));
	}

	@Override
	public void sendToUsers(long moduleId, String message, long fromUserId, Long[] toUserIds) {
		logger.info("Sending message '" + message + "' to users '" + toUserIds + "'.");
        for (long toUserId : toUserIds) {
            try {
            	MessageEntity msg = new MessageEntity();
            	msg.setTitle((!StringUtils.isEmpty(message) && message.length() > 64) ? (message.substring(0, 60) + "...") : message);
            	msg.setMsg(message);
                msg.setFromUserId(fromUserId);
                msg.setToUserId(toUserId);
                msg.setModuleId(moduleId);
                msg.setIsRead(StatusConstant.STATUS_NO); 
                msg.setIsOutData(StatusConstant.STATUS_YES);
                msg.setAbolished(0); 
                msg.setMsg(message);
                messageDao.save(msg);      
            } catch (Exception ex) {
                logger.warn("Can not send message to user '" + toUserId + "', message is '" + message + "'.", ex);
            }
        }
	}

	@Override
	public void sendToOrgUsers(long moduleId, String message, long fromUserId, Long[] orgIds) {
		List<Long> list = userDao.findUserIdByOrg(orgIds);
		if(CollectionUtils.isEmpty(list))
			return;
		
		sendToUsers(moduleId, message, fromUserId, list.toArray(new Long[]{}));
	}

	@Override
	public void sendToUsers(long moduleId, String message, String[] toUserCodes) {
		sendToUsers(moduleId, message, -1, toUserCodes);
	}

	@Override
	public void sendToUsers(long moduleId, String message, Long[] toUserIds) {
		sendToUsers(moduleId, message, -1, toUserIds);
	}

	@Override
	public void sendToOrgUsers(long moduleId, String message, Long[] orgIds) {
		sendToOrgUsers(moduleId, message, -1, orgIds);
	}
	
}
