package com.qeweb.scm.epmodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.mail.MailObject;
import com.qeweb.scm.basemodule.service.MailSendService;
import com.qeweb.scm.basemodule.service.UserServiceImpl;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.epmodule.constans.EpConstans;
import com.qeweb.scm.epmodule.entity.EpPriceEntity;
import com.qeweb.scm.epmodule.entity.EpVendorEntity;
import com.qeweb.scm.epmodule.repository.EpVendorDao7;

/**
 * 询价供应商实体类service
 * @author u
 *
 */
@Service
@Transactional
public class EpVendorService {
	public static final Integer STATUS_INIT = 0;		//状态初始值
	public static final Integer STATUS_YES = 1;		

	@Autowired
	private EpVendorDao7 epVendorDao;
	@Autowired
	private UserServiceImpl userService;
	
	/**
	 * 根据询价供应商获得询价单的id
	 * @param searchParamMap
	 * @return
	 */
	public List<Long> getEpPriceIdList(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpVendorEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpVendorEntity.class);
		List<EpVendorEntity> epVendorList = epVendorDao.findAll(spec);
		List<Long> epPriceIdList = new ArrayList<Long>();
		if(epVendorList != null && epVendorList.size()>0){
			for (EpVendorEntity epVendor : epVendorList) {
				Long epPriceId = epVendor.getEpPrice().getId();
				epPriceIdList.add(epPriceId);
			}
		}
		return epPriceIdList;
	}
	
	
	
	/**
	 * 获得询价供应商列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<EpVendorEntity> getEpVendorLists(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<EpVendorEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), EpVendorEntity.class);
		return epVendorDao.findAll(spec, pagin);
	}
	
	/**
	 * 保存询价供应商
	 * @param epVendor
	 * @return
	 */
	public EpVendorEntity save(EpVendorEntity epVendor){
		return epVendorDao.save(epVendor);
	}
	
	/**
	 * 根据询价供应商id查找询价供应商
	 * @param id
	 * @return
	 */
	public EpVendorEntity findById(Long id){
		return epVendorDao.findOne(id);
	}
	
	/**
	 * 编辑询价供应商后保存
	 * @param epPrice
	 * @param vendordatas
	 * @return
	 */
	/*public List<EpVendorEntity> saveVendorList(EpPriceEntity epPrice,String vendordatas,String likePath){
		JSONObject object = JSONObject.fromObject(vendordatas);    
		JSONArray array = (JSONArray) object.get("rows");
		List<EpVendorEntity> epVendorList = new ArrayList<EpVendorEntity>();
		for(int i=0;i<array.size();i++){
			object = array.getJSONObject(i);
			EpVendorEntity epVendor = new EpVendorEntity();
			String idStr = StringUtils.convertToString(object.get("id"));
			if(idStr != null && idStr.length()>0){
				Long id = Long.parseLong(idStr);
				epVendor = findById(id);
			}
			if(epVendor == null){
				epVendor = new EpVendorEntity();
			}
			epVendor.setIsOutData(StatusConstant.STATUS_YES);
			epVendor.setAbolished(Constant.UNDELETE_FLAG);
			epVendor.setEpPrice(epPrice); 		//询价单
			epVendor.setVendorId(Long.parseLong(object.get("vendorId") + ""));	//供应商Id
			epVendor.setVendorCode(object.get("vendorCode") + "");				//供应商编码
			epVendor.setVendorName(object.get("vendorName") + "");				//供应商名称
			epVendor.setAccessStatus(StringUtils.convertToInteger(object.get("accessStatus") + ""));				//准入状态
			epVendor.setAddress(StringUtils.convertToString(object.get("address")));				//地址
			epVendor.setLegalRep(object.get("legalRep") + "");				//联系人
			epVendor.setLinkPhone(object.get("linkPhone") + "");						//联系电话
			epVendor.setOrgEmail(object.get("orgEmail") + "");			//Email
			epVendor.setCooperatStatus(STATUS_INIT);				//合作状态 ：0=未合作
			epVendor.setApplicationStatus(STATUS_INIT);				//报名状态 ：0=未报名
			save(epVendor);
			//邀请类型：当询价项目发布后，被邀请供应商将收到提醒（短信、邮件、站内消息）
			if(epPrice.getPublishStatus().equals(STATUS_YES) && epPrice.getJoinWay().equals(STATUS_INIT)){
				List<UserEntity> users = userService.findByCompany(epVendor.getVendorId());
				OtherThread otherThread = new OtherThread(epVendor,likePath,users);
				//启用线程
				otherThread.setDaemon(true);
				otherThread.start();
			}
			epVendorList.add(epVendor);
		}
		return epVendorList;
	}*/
	public List<EpVendorEntity> saveVendorList(EpPriceEntity epPrice,JSONObject object, JSONArray array,String likePath){
		List<EpVendorEntity> epVendorList = new ArrayList<EpVendorEntity>();
		for(int i=0;i<array.size();i++){
			object = array.getJSONObject(i);
			EpVendorEntity epVendor = new EpVendorEntity();
			String idStr = StringUtils.convertToString(object.get("id"));
			if(idStr != null && idStr.length()>0){
				Long id = Long.parseLong(idStr);
				epVendor = findById(id);
			}
			if(epVendor == null){
				epVendor = new EpVendorEntity();
			}
			epVendor.setIsOutData(StatusConstant.STATUS_YES);
			epVendor.setAbolished(Constant.UNDELETE_FLAG);
			epVendor.setEpPrice(epPrice); 		//询价单
			epVendor.setVendorId(Long.parseLong(object.get("vendorId") + ""));	//供应商Id
			epVendor.setVendorCode(object.get("vendorCode") + "");				//供应商编码
			epVendor.setVendorName(object.get("vendorName") + "");				//供应商名称
			epVendor.setAccessStatus(StringUtils.convertToInteger(object.get("accessStatus") + ""));				//准入状态
			epVendor.setAddress(StringUtils.convertToString(object.get("address")));				//地址
			epVendor.setLegalRep(object.get("legalRep") + "");				//联系人
			epVendor.setLinkPhone(object.get("linkPhone") + "");						//联系电话
			epVendor.setOrgEmail(object.get("orgEmail") + "");			//Email
			epVendor.setCooperatStatus(STATUS_INIT);				//合作状态 ：0=未合作
			epVendor.setApplicationStatus(STATUS_INIT);				//报名状态 ：0=未报名
			save(epVendor);
			//邀请类型：当询价项目发布后，被邀请供应商将收到提醒（短信、邮件、站内消息）
			if(epPrice.getPublishStatus().equals(STATUS_YES) && epPrice.getJoinWay().equals(STATUS_INIT)){
				List<UserEntity> users = userService.findByCompany(epVendor.getVendorId());
				OtherThread otherThread = new OtherThread(epVendor,likePath,users);
				//启用线程
				otherThread.setDaemon(true);
				otherThread.start();
			}
			epVendorList.add(epVendor);
		}
		return epVendorList;
	}
	
	/**
	 * 删除询价物料
	 * @param epVendor
	 */
	public void deleteEpVendor(EpVendorEntity epVendor){
		epVendorDao.delete(epVendor);
	}
	
	/**
	 * 根据询价单id与供应商id查找询价供应商
	 */
	public EpVendorEntity findByEpPriceIdAndEpVendorId(Long epPriceId,Long epVendorId){
		return epVendorDao.findByEpPriceIdAndEpVendorId(epPriceId, epVendorId);
	}
	
	/**
	 * 根据询价单id查找询价供应商
	 */
	public List<EpVendorEntity> findByEpPriceId(Long epPriceId){
		return epVendorDao.findByEpPrice(epPriceId);
	}
	
	/**
	 * 供应商报名
	 */
	public EpVendorEntity applicationEpPrice(EpPriceEntity epPrice,OrganizationEntity org){
		EpVendorEntity epVendor = new EpVendorEntity();
		epVendor.setIsOutData(StatusConstant.STATUS_YES);
		epVendor.setAbolished(Constant.UNDELETE_FLAG);
		epVendor.setEpPrice(epPrice); 		//询价单
		epVendor.setVendorId(org.getId());	//供应商Id
		epVendor.setVendorCode(org.getCode());				//供应商编码
		epVendor.setVendorName(org.getName());				//供应商名称
		//epVendor.setAccessStatus();				//准入状态
		//epVendor.setAddress();				//地址
		epVendor.setLegalRep(org.getLegalPerson());				//联系人
		epVendor.setLinkPhone(org.getPhone());						//联系电话
		epVendor.setOrgEmail(org.getEmail());			//Email
		epVendor.setCooperatStatus(STATUS_INIT);				//合作状态 ：0=未合作
		epVendor.setApplicationStatus(STATUS_YES);				//报名状态：1=已报名
		save(epVendor);
		return epVendor;
	}
	

	/**
	 * 循环询价供应商集合
	 * @param epVendorList
	 */
	public boolean isAllAppalication(List<EpVendorEntity> epVendorList){
		boolean temp = true;
		for (EpVendorEntity epVendor : epVendorList) {
			if(epVendor.getApplicationStatus().equals(EpConstans.STATUS_NO)){
				temp = false;
				break;
			}else{
				temp = true;
				continue;
			}
		}
		return temp;
	}
	
	
	//add by yao.jin  处理当选择按照供应商维度询价时的数据
	
	public List<EpVendorEntity> initVendorData(EpPriceEntity epPrice,OrganizationEntity vendor,String likePath){
		List<EpVendorEntity> epVendorList = new ArrayList<EpVendorEntity>();
		EpVendorEntity epVendor = epVendorDao.findByEpPriceIdAndEpVendorId(epPrice.getId(), vendor.getId());
		if(epVendor == null){
			epVendor = new EpVendorEntity();
		}
		epVendor.setIsOutData(StatusConstant.STATUS_YES);
		epVendor.setAbolished(Constant.UNDELETE_FLAG);
		epVendor.setEpPrice(epPrice); 		//询价单
		epVendor.setVendorId(vendor.getId());	//供应商Id
		epVendor.setVendorCode(vendor.getCode());				//供应商编码
		epVendor.setVendorName(vendor.getName());				//供应商名称
		//epVendor.setAccessStatus();				//准入状态
		//epVendor.setAddress("");				//地址
		epVendor.setLegalRep(vendor.getLegalPerson());				//联系人
		epVendor.setLinkPhone(vendor.getPhone());						//联系电话
		epVendor.setOrgEmail(vendor.getEmail());			//Email
		epVendor.setCooperatStatus(STATUS_INIT);				//合作状态 ：0=未合作
		epVendor.setApplicationStatus(STATUS_INIT);				//报名状态 ：0=未报名
		save(epVendor);
		//邀请类型：当询价项目发布后，被邀请供应商将收到提醒（短信、邮件、站内消息）
		if(epPrice.getPublishStatus().equals(STATUS_YES) && epPrice.getJoinWay().equals(STATUS_INIT)){
			List<UserEntity> users = userService.findByCompany(epVendor.getVendorId());
			OtherThread otherThread = new OtherThread(epVendor,likePath,users);
			//启用线程
			otherThread.setDaemon(true);
			otherThread.start();
		}
		epVendorList.add(epVendor);
		return epVendorList;
	}
	//end add
	
	
	
	
	
	
}

//其他线程
class OtherThread extends Thread {
	private EpVendorEntity epVendor;
	private String linkPath;
	private List<UserEntity> users;
	
	public OtherThread(EpVendorEntity epVendor,String linkPath){
		this.epVendor = epVendor;
		this.linkPath = linkPath;
	}
	public OtherThread(EpVendorEntity epVendor,String linkPath,List<UserEntity> users){
		this.epVendor = epVendor;
		this.linkPath = linkPath;
		this.users =users;
	}
	
	@Override
	public void run() {
		sendUp();
	}
	
	private synchronized void sendUp() {
			//给供应商发邀请邮件
			MailSendService mailSendService=new MailSendService();
			for (UserEntity userEntity : users) {
				MailObject mo = new MailObject();
				mo.toMail = userEntity.getEmail();
				mo.templateName = "defaultTemp";
				Map<String, String> params = new HashMap<String, String>();
				params.put("vendorName",epVendor.getVendorName());
				params.put("link",linkPath);
				params.put("tempMessage","采方已创建询价单，邀请您参与报价！");
				params.put("signText","邮件发送");
				mo.params = params;
				mo.title = "邀请信息";
				mailSendService.send(mo, 2);
			}
	}
}
