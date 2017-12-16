/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qeweb.scm.basemodule.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 邮件发送器
 *
 * @author micfans
 */
public class MailSender {

    private final static Log _logger = LogFactory.getLog(MailSender.class);
    /**
     * 邮件队列
     */
    public final static List mails = Collections.synchronizedList(new ArrayList<Object []>());
    /**
     * 邮件发送线程
     */
    private final static MailThread mailThread = new MailThread();

    /**
     *
     */
    public static void init() {
        mailThread.setDaemon(true);
        mailThread.start();
    }
    
    static{
    	init();
    }

    /**
     *
     * @param mail
     */
    public static void send(MailObject mail) {
        send(mail.templateName, mail.params, mail.toMail, mail.title,mail.fromMail,mail.password,mail.host);
    }

    /**
     * 发送模板邮件，模板需要在mail_template.xml中进行配置
     *
     * @param templateName 模板名称
     * @param params 模板替换参数
     * @param toMail 收件人
     * @param title 邮件标题， 可以为空，则取模板中定义的标题
     */
    public static void send(String templateName, Map<String, String> params, String toMail, String title,String from,String password,String host) {
    	MailTemplateInitor initor = new MailTemplateInitor();
    	try {
			initor.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
        MailTemplate templa = MailHandlerImpl.mailTemplateList.get(templateName);
        if (templa == null) {
            _logger.error("Can not send mail, because the template not exists for '" + templateName + "'.");
            return;
        }
        if (toMail == null || toMail.indexOf("@") == -1) {
            _logger.error("Can not send mail, because the to mail is not valid.");
            return;
        }
        String content = templa.getTemplate();
        title = title == null ? templa.getSubject() : title;
        if (params != null) {
            Set<Map.Entry<String, String>> set = params.entrySet();
            Iterator<Map.Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                content = content.replaceAll("\\$" + entry.getKey(), entry.getValue());
                title = title.replaceAll("\\$" + entry.getKey(), entry.getValue());
            }
        }
        content = content.replaceAll("\\$curr_date", new Date().toString());
        sendHtml(from.trim() , toMail.trim(), title, content,password.trim(),host.trim());
    }

    /**
     * 发送普通文本邮件
     *
     * @param from
     * @param to
     * @param title
     * @param content
     */
    public static void sendText(String from, String to, String title, String content) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(to.trim());
        smm.setFrom(from.trim());
        smm.setSubject(title);
        smm.setText(content);
        mails.add(smm);
        synchronized (MailSender.mails) {
            MailSender.mails.notifyAll();
        }
    }
    
  

    /**
     * 发送HTML邮件
     * 
     * @param from 发件人邮箱
     * @param to 收件人邮箱
     * @param title 邮件标题
     * @param content 邮件内容
     * @param password 发件邮箱密码
     * @param host SMTP名称
     */
    public static void sendHtml(String from, String to, String title, String content, String password, String host) {
        try {
//          JavaMailSender sender = (JavaMailSender) SpringConstant.getCTX().getBean("mailSender");
        	JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setUsername(from); 
            sender.setPassword(password); 
//          sender.setPort(25);
            sender.setHost(host);  
            Properties p = new Properties();
            p.setProperty("mail.smtp.timeout", "20000");
            p.setProperty("mail.smtp.auth", "true");
            p.setProperty("mail.debug", "false");
            sender.setJavaMailProperties(p);
            MimeMessage smm = sender.createMimeMessage();
            MimeMessageHelper help = new MimeMessageHelper(smm, true, "UTF-8");
            help.setFrom(from);
            help.setSubject(title);
            String[] ccs = to.split(",");
            help.setTo(ccs[0].trim());
            for (int i = 1; i < ccs.length; i++) {
                String c = ccs[i].trim();
                if (!c.equals("") && c.indexOf("@") > -1) {
                    help.addCc(ccs[i].trim());
                }
            }
            help.setText(content, true); 
            smm.setDescription(content);
            mails.add(new Object[]{smm,sender});
            synchronized (MailSender.mails) {
                MailSender.mails.notifyAll();
            }
        } catch (Exception ex) {
            _logger.error("Send mail faild. ", ex);
        }
    }
    
    
}

/**
 *
 * @author micfans
 */
class MailThread extends Thread {

    private final static Log _logger = LogFactory.getLog(MailThread.class);
    public boolean isWait = true;

    @Override
    public void run() {
        while (true) {
            if (!MailSender.mails.isEmpty()) {
                while (!MailSender.mails.isEmpty()) {
                    send(MailSender.mails.get(0));
                }
            } else {
                synchronized (MailSender.mails) {
                    try {
                        _logger.info("Waiting for new mail ...");
                        MailSender.mails.wait();
                    } catch (Exception ex) {
                    }
                }
            }
        }

    }

    /**
     *
     * @param mail
     */
    public void send(Object... mail) {
    	Object [] obj = (Object[]) mail[0];
        try {
            _logger.info("Sending mail ...");
//            JavaMailSender sender = (JavaMailSender) SpringConstant.getCTX().getBean("mailSender");
            JavaMailSenderImpl sender = (JavaMailSenderImpl) obj[1];
            if (obj[0] instanceof MimeMessage) {
                sender.send((MimeMessage) obj[0]);
            } else if (obj[0] instanceof SimpleMailMessage) {
                sender.send((SimpleMailMessage) obj[0]);
            }
        } catch (Exception ex) {
            _logger.error("Sending mail faild.", ex);
        } finally {
            MailSender.mails.remove(mail[0]);
            _logger.info("Remove mail from queuee.");
        }
    }
}
