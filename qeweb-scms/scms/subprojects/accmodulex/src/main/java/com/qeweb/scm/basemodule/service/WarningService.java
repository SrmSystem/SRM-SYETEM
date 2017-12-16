package com.qeweb.scm.basemodule.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.scm.baseline.common.entity.ClassSystemEntity;
import com.qeweb.scm.baseline.common.entity.WarnItemEntity;
import com.qeweb.scm.baseline.common.entity.WarnMainEntity;
import com.qeweb.scm.baseline.common.entity.WarnMessageEntity;
import com.qeweb.scm.baseline.common.repository.WarnItemDao;
import com.qeweb.scm.baseline.common.repository.WarnMainDao;
import com.qeweb.scm.baseline.common.repository.WarnMessageDao;
import com.qeweb.scm.baseline.common.service.IClassSystemService;
import com.qeweb.scm.baseline.common.service.WarnMainService;
import com.qeweb.scm.basemodule.entity.RoleUserEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;

@Service
@Transactional(rollbackOn=Exception.class)
public class WarningService extends BaseService{
	
	private PurchaseOrderItemDao purchaseOrderItemDao;
	@Autowired
	private WarnMessageDao warnMessageDao;
	
	@Autowired
	private WarnMainDao warnMainDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private WarnItemDao warnItemDao;
	
	@Autowired
	private RoleUserDao roleUserDao;
	
	@Autowired
	private MailSendService mailSendService;
	
	@Autowired
	private WarnMainService warnMainService;
	
	@Autowired
	private IClassSystemService classSystemService;
	
	public boolean execute(ILogger ilogger) throws Exception{
		ilogger.log("warning start");
		scanWarnMessage(ilogger);
		return true;
	}
	

	/**
	 * 扫描warnMessage表，查看没有作废的数据，如果预警时长，将isOutTime字段赋值为0；
	 * @param ilogger
	 */
	public void scanWarnMessage(ILogger ilogger){
		
		//查询预警的数据
		List<WarnMessageEntity> warnMessages = warnMessageDao.findWarnForEmail();
		
		for(WarnMessageEntity warnMessage : warnMessages){
			if(warnMessage.getIsPromotion() == 0){
				
			}else{	
				Long warnMainId = warnMessage.getWarnMainId();
				WarnMainEntity warnMain = warnMainDao.findOne(warnMainId);
				Date currentDate = new Date();
			    boolean flag = currentDate.after(warnMessage.getWarnTime());
			if(flag){
				if( warnMain.getIsMail() == 1 ){

                    MailObject mo = new MailObject();
            		UserEntity user = userDao.findOne(warnMessage.getUserId());

            		mo.toMail = user.getEmail();
            		mo.templateName = "warnReminderForOrder";
            		Map<String, String> params = new HashMap<String, String>();
            		mo.title = warnMessage.getWarnTitle();
            		params.put("content", warnMessage.getWarnMessage());
            		mo.params = params;
            		mo.title=warnMain.getName();
            		mailSendService.send(mo, 3);
            		
            		//设置已发送房子重复的发送邮件
            		warnMessage.setIsOutTime(1);
            		warnMessageDao.save(warnMessage);
            		
				}
			}
			
			
		}
		}
	}


}
