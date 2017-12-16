package com.qeweb.scm.vendormodule.service;


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
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.entity.VendorInviteEmailEntity;
import com.qeweb.scm.vendormodule.repository.VendorInviteEmailDao;
/**
 * 邀请注册Service
 * @author lw
 * 创建时间：2015年7月1日11:30:32
 * 最后更新时间：2015年7月2日17:21:58
 * 最后更新人：lw
 */
@Service
@Transactional
public class VendorInviteEmailService {
	@Autowired
	private VendorInviteEmailDao vendorInviteEmailDao;
	@Autowired
	private MailSendService mailSendService;
	public Page<VendorInviteEmailEntity> getVendorInviteEmailList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "createTime","desc");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorInviteEmailEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorInviteEmailEntity.class);
		return vendorInviteEmailDao.findAll(spec,pagin);
	}

	public void addNewVendorInviteEmail(VendorInviteEmailEntity vendorInviteEmail) {
		vendorInviteEmailDao.save(vendorInviteEmail);
	}
	
	public void addNewAndSendMail(VendorInviteEmailEntity vendorInviteEmail,String url){
		vendorInviteEmailDao.save(vendorInviteEmail);
		MailObject mo = new MailObject();
		mo.toMail = vendorInviteEmail.getVendorEmail();
		mo.templateName = "defaultTemp";
		Map<String, String> params = new HashMap<String, String>();
		params.put("vendorName", vendorInviteEmail.getVendorName());
		params.put("inviteName", vendorInviteEmail.getInviteName());
		params.put("expiryDate", (vendorInviteEmail.getExpiryDate()+"").substring(0, 10));
		String regUrl = url+"/public/register/inviteReg/toRegister/"+vendorInviteEmail.getId()+"/";
		params.put("message", "<a href='"+regUrl+"'>点击注册</a>");
		params.put("link", "<a href='"+regUrl+"'>"+regUrl+"</a>");
		params.put("tempMessage", " ");
		params.put("signText", " ");
		mo.params = params;
		mo.title = "邀请注册";
		mailSendService.send(mo, 2);
	}

	public VendorInviteEmailEntity getVendorInviteEmail(Long id) {
		return vendorInviteEmailDao.findOne(id);
	}
	public List<VendorInviteEmailEntity> getVendorInviteEmailByName(String name) {
		return vendorInviteEmailDao.findByVendorName(name);
	}

	public void updateVendorInviteEmail(VendorInviteEmailEntity vendorInviteEmail) {
		vendorInviteEmailDao.save(vendorInviteEmail);
	}

	public void deleteVendorInviteEmailList(List<VendorInviteEmailEntity> vendorInviteEmailList) {
		vendorInviteEmailDao.delete(vendorInviteEmailList);
		
	}
	
	public List<VendorInviteEmailEntity> findByVendorNameAndVendorEmail(
			String vendorName, String vendorEmail) {
		return vendorInviteEmailDao.findByVendorNameAndVendorEmail(vendorName,vendorEmail);
	}
}
