package com.qeweb.scm.basemodule.service;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.MailSetEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.mail.MailSender;
import com.qeweb.scm.basemodule.repository.MailSetDao;

@Component
@Transactional
public class MailSendService {
	
	@Autowired
	MailSetDao mailSetDao;	
	
	public void send_(MailObject mail, Integer t)  {
		mailSetDao=SpringContextUtils.getBean("mailSetDao");
		MailSetEntity entity = mailSetDao.findByMailTemplateIdAndAbolished(t, 0);
		if(entity == null)
			return;
		
		String content = entity.getMailContent();
        String title = mail.title;
        if (mail.params != null) {
            Set<Map.Entry<String, String>> set = mail.params.entrySet();
            Iterator<Map.Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                content = content.replaceAll("\\$" + entry.getKey(), entry.getValue());
                title = title.replaceAll("\\$" + entry.getKey(), entry.getValue());
            }
        }
        content = content.replaceAll("\\$curr_date", new Date().toString());
        content += "<hr/><br/>" + entity.getSignature();
        MailSender.sendHtml(entity.getMailAddress() , mail.toMail.trim(), mail.title, content,entity.getPassword(),entity.getServerAddress());
	}

	public void send(MailObject mail, Integer t)  {
		mailSetDao=SpringContextUtils.getBean("mailSetDao");
		MailSetEntity entity = mailSetDao.findByMailTemplateIdAndAbolished(t, 0);
		if(entity == null)
			return;
		
		mail.fromMail = entity.getMailAddress();
		mail.password = entity.getPassword();
		mail.host = entity.getServerAddress();
		MailSender.send(mail);
//		this.send2(mail.toMail,mail.title,mail.templateName,mail.params,entity.getMailTemplateId());
	}
	
	public void sendExForInviteReg(MailObject mail, Integer t)  {
		mailSetDao=SpringContextUtils.getBean("mailSetDao");
		MailSetEntity entity = mailSetDao.findByMailTemplateIdAndAbolished(t, 0);
		if(entity == null)
			return;
		
		mail.fromMail = entity.getMailAddress();
		mail.password = entity.getPassword();
		mail.host = entity.getServerAddress();
		mail.params.put("tempMessage", entity.getMailContent());
		mail.params.put("signText", entity.getSignature());
		MailSender.send(mail);
//		this.send2(mail.toMail,mail.title,mail.templateName,mail.params,entity.getMailTemplateId());
	}
	
	public void send2(String toMail,String title,String template,Map<String,String> params, Integer mailId)  {
		MailSetEntity entity = mailSetDao.findByMailTemplateIdAndAbolished(mailId, 0);
		if(entity == null)
			return;
		
		MailObject mail = new MailObject();
//		params.put("tempMessage", entity.getMailContent().replace("\r\n", "<br/>").replace("\r", "<p/>").replace("\n", "<br/>"));
//		params.put("signText", entity.getSignature().replace("\r\n", "<br/>").replace("\r", "<p/>").replace("\n", "<br/>"));
		mail.toMail = toMail;
		mail.templateName = template;
		mail.params = params;
		mail.title = title;
		mail.fromMail = entity.getMailAddress();
		mail.password = entity.getPassword();
		mail.host = entity.getServerAddress();
		MailSender.send(mail);
	}
	
	public void send(String toMail,String title,String template,Map<String,String> params, Integer mailId)  {
		MailSetEntity entity = mailSetDao.findByMailTemplateIdAndAbolished(mailId, 0);
		MailObject mail = new MailObject();
		params.put("tempMessage", entity.getMailContent().replace("\r\n", "<br/>").replace("\r", "<p/>").replace("\n", "<br/>"));
		params.put("signText", entity.getSignature().replace("\r\n", "<br/>").replace("\r", "<p/>").replace("\n", "<br/>"));
		mail.toMail = toMail;
		mail.templateName = template;
		mail.params = params;
		mail.title = title;
		mail.fromMail = entity.getMailAddress();
		mail.password = entity.getPassword();
		mail.host = entity.getServerAddress();
		MailSender.send(mail);
	}

	public void sends(MailObject mail, Integer t)  {
		MailSetEntity entity = mailSetDao.findByMailTemplateIdAndAbolished(t, 0);
		mail.fromMail = entity.getMailAddress();
		mail.password = entity.getPassword();
		mail.host = entity.getServerAddress();
		MailSender.send(mail);
	}
}
