package com.qeweb.scm.basemodule.service;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.entity.BaseEntity;
import com.qeweb.scm.basemodule.entity.CommentEntity;
import com.qeweb.scm.basemodule.entity.NoticeEntity;
import com.qeweb.scm.basemodule.entity.NoticeLookEntity;
import com.qeweb.scm.basemodule.entity.NoticeVendorCFGEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.RoleEntity;
import com.qeweb.scm.basemodule.entity.RoleUserEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.CommentDao;
import com.qeweb.scm.basemodule.repository.NoticeDao;
import com.qeweb.scm.basemodule.repository.NoticeLookDao;
import com.qeweb.scm.basemodule.repository.NoticeVendorCFGDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.RoleDao;
import com.qeweb.scm.basemodule.repository.RoleUserDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;

@Service
@Transactional
public class NoticeService {

	@Autowired
	private NoticeDao noticeDao;
	
	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private NoticeVendorCFGDao noticeVendorCFGDao;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleUserDao roleUserDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private NoticeLookDao noticeLookDao;
	
	public List<NoticeEntity> getNoticeStart() {
		List<NoticeEntity>  list=noticeDao.getNoticeEntity(DateUtil.getCurrentTimestamp());
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<?> entit1ies = roleUserDao.findByUserIds(user.id);
		for(int i=0;i<list.size();i++)
		{
			List<NoticeVendorCFGEntity> listn=noticeVendorCFGDao.findByNoticeId(list.get(i).getId());
			if(listn!=null&&listn.size()>0)
			{
				
				NoticeVendorCFGEntity nv=noticeVendorCFGDao.findByNoticeIdAndOrgId(list.get(i).getId(),user.orgId);
				NoticeVendorCFGEntity nv1=noticeVendorCFGDao.findByNoticeIdAndUserId(list.get(i).getId(), user.id);
				NoticeVendorCFGEntity nv2=null;
				for (int j = 0; j < entit1ies.size(); j++) {
					nv2=noticeVendorCFGDao.findByNoticeIdAndRoleId(list.get(i).getId(), com.qeweb.scm.basemodule.utils.StringUtils.convertToLong(entit1ies.get(j) +""));
					if(nv2!=null)
					{
						break;
					}
				}
				if(nv2==null&&nv==null&&nv1==null)
				{
					list.remove(i);
					i--;
				}
			}
		}
		
		return list;
	}

	public NoticeEntity getNotice(Long id) {
		NoticeEntity  noticeEntity=noticeDao.findOne(id);
		Integer lookNumber=noticeEntity.getLookNumber();
		lookNumber=lookNumber+1;
		noticeEntity.setLookNumber(lookNumber);
		noticeDao.save(noticeEntity);
		NoticeLookEntity noticeLookEntity=new NoticeLookEntity();
		noticeLookEntity.setNoticeId(noticeEntity.getId());;
		noticeLookDao.save(noticeLookEntity);
		return noticeEntity;
	}
	public List<CommentEntity> getCommentEntitys(Long id) {
		List<CommentEntity>  commentEntities=commentDao.findByNoticeIdOrderByIdDesc(id);
		return commentEntities;
	}

	public String addcomment(CommentEntity commentEntity) {
		BaseEntity baseEntity=new BaseEntity();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user==null){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return "0";
		}
		String loginName=user.loginName;
		UserEntity userentiy=userDao.findByLoginName(loginName);
		commentEntity.setCreateUserId(userentiy.getId());
		commentEntity.setCreateUserName(userentiy.getName());
		commentEntity.setCreateTime(DateUtil.getCurrentTimestamp());
		commentEntity.setUpdateUserId(userentiy.getId());
		commentEntity.setUpdateUserName(userentiy.getName());
		commentEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
		commentEntity.setAbolished(0);
		commentEntity.setCommentType(0);
		commentDao.save(commentEntity);
		String tsStr=null;
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        try {  
            //方法一  
            tsStr = sdf.format(commentEntity.getCreateTime());  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
		String str="";
		str += "<p class=\"username\">"+commentEntity.getCreateUserName()+"</p>";
		str += "<p class=\"comment-body\">"+commentEntity.getContent()+"</p>";
		str += "<p class=\"comment-footer\">"+tsStr+"</p>";
		return str;
	}

	public Page<NoticeEntity> getNoticeList(int pageNumber, int pageSize,
			Map<String, Object> searchParamMap) {
		PageRequest pagin =  PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<NoticeEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), NoticeEntity.class);
		Page<NoticeEntity> page = noticeDao.findAll(spec,pagin);
		for(NoticeEntity no:page.getContent())
		{
			List<CommentEntity> cos=commentDao.findByNoticeId(no.getId());
			if(cos!=null)
			{
				no.setSpkeaNumber(""+cos.size());
			}
		}
		return page;
	}

	public String addnotie(NoticeEntity noticeEntity) {
		BaseEntity baseEntity=new BaseEntity();
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		if(user==null){
			baseEntity.setCreateTime(DateUtil.getCurrentTimestamp());
			baseEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			return "0";
		}
		noticeEntity.setNoticeTypeNames("占空");
		String loginName=user.loginName;
		UserEntity userentiy=userDao.findByLoginName(loginName);
		String addids=noticeEntity.getAddids();
		noticeEntity.setCreateUserId(userentiy.getId());
		noticeEntity.setCreateUserName(userentiy.getName());
		noticeEntity.setCreateTime(DateUtil.getCurrentTimestamp());
		noticeEntity.setUpdateUserId(userentiy.getId());
		noticeEntity.setUpdateUserName(userentiy.getName());
		noticeEntity.setLastUpdateTime(DateUtil.getCurrentTimestamp());
		noticeEntity.setAbolished(0);
		noticeEntity.setLookNumber(0);
		noticeDao.save(noticeEntity);
		if(addids!=null&&!(addids.equals("")))
		{
			String []ikd=addids.split(",");
			for(int i=0; i<ikd.length;i++)
			{
				if(noticeEntity.getNtype()==1)
				{
					
					NoticeVendorCFGEntity ncc=new NoticeVendorCFGEntity();
					ncc.setNoticeId(noticeEntity.getId());
					ncc.setOrgId(Long.parseLong(ikd[i]));
					ncc.setNtype(1);
					noticeVendorCFGDao.save(ncc);
				}
				else if(noticeEntity.getNtype()==2)
				{
					NoticeVendorCFGEntity ncc=new NoticeVendorCFGEntity();
					ncc.setNoticeId(noticeEntity.getId());
					ncc.setRoleId(Long.parseLong(ikd[i]));
					ncc.setNtype(2);
					noticeVendorCFGDao.save(ncc);
				}else if(noticeEntity.getNtype()==3)
				{
					NoticeVendorCFGEntity ncc=new NoticeVendorCFGEntity();
					ncc.setNoticeId(noticeEntity.getId());
					ncc.setUserId(Long.parseLong(ikd[i]));
					ncc.setNtype(3);
					noticeVendorCFGDao.save(ncc);
				}
				
			}	
		}
		return "1";
	}

	public void deleteNoticeList(List<NoticeEntity> noticeEntityList) {
		for(NoticeEntity noticeEntity:noticeEntityList)
		{
			noticeDao.abolish(noticeEntity.getId());
		}
		
	}

	public Page<NoticeLookEntity> getNoticeLookList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap, Long id) {
		PageRequest pagin =  PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		searchParamMap.put("EQ_noticeId", id);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<NoticeLookEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), NoticeLookEntity.class);
		Page<NoticeLookEntity> page = noticeLookDao.findAll(spec,pagin);
		return page;
	}

	public NoticeEntity updateNotieS(Long id) throws Exception {
		NoticeEntity nvd=noticeDao.findOne(id);
		NoticeEntity nv=new NoticeEntity();
		PropertyUtils.copyProperties(nv, nvd);;
		List<NoticeVendorCFGEntity> ncc=noticeVendorCFGDao.findByNoticeId(nv.getId());
		String addids="";
		String name="";
		Integer ntys=0;
		for(int i=0;i<ncc.size();i++)
		{
			NoticeVendorCFGEntity nc = ncc.get(i);
			if(nc.getNtype()==1)
			{
					OrganizationEntity org = organizationDao.findOne(nc.getOrgId());
					if(i==0)
					{
						addids=""+nc.getOrgId();
					}
					else
					{
						addids=addids+","+nc.getOrgId();
					}
					name=name+"<span id=\"span"+nc.getOrgId()+"\" class=\"tag\"><span>"+org.getName()+"</span><button onclick=\"window.parent.adddelecturls('"+nc.getOrgId()+"')\">x</button></span>";
				ntys=nc.getNtype();
			}
			else if(nc.getNtype()==2)
			{
				RoleEntity rol= roleDao.findOne(nc.getRoleId());
				if(i==0)
				{
					addids=""+nc.getRoleId();
				}
				else
				{
					addids=addids+","+nc.getRoleId();
				}
				name=name+"<span id=\"span"+nc.getRoleId()+"\" class=\"tag\"><span>"+rol.getName()+"</span><button onclick=\"window.parent.adddelecturls('"+nc.getRoleId()+"')\">x</button></span>";
				ntys=nc.getNtype();
			}
			else if(nc.getNtype()==3){
					UserEntity user =userDao.findById(nc.getUserId());
					if(i==0)
					{
						addids=""+nc.getUserId();
					}
					else
					{
						addids=addids+","+nc.getUserId();
					}
					name=name+"<span id=\"span"+nc.getUserId()+"\" class=\"tag\"><span>"+user.getName()+"</span><button onclick=\"window.parent.adddelecturls('"+nc.getUserId()+"')\">x</button></span>";
			}
		}
		nv.setNoticeTypeNames(name);
		nv.setAddids(addids);
		nv.setNtype(ntys);
		return nv;
	}

	public String updateNotie(NoticeEntity noticeEntity) {
		NoticeEntity n= noticeDao.findOne(noticeEntity.getId());
		n.setTitle(noticeEntity.getTitle());
		n.setContent(noticeEntity.getContent());
		n.setNoticeType(noticeEntity.getNoticeType());
		n.setCommentPower(noticeEntity.getCommentPower());
		n.setValidStartTime(noticeEntity.getValidStartTime());
		n.setValidEndTime(noticeEntity.getValidEndTime());
		String addids=noticeEntity.getAddids();
		noticeVendorCFGDao.delete(noticeVendorCFGDao.findByNoticeId(noticeEntity.getId()));
		if(addids!=null&&!(addids.equals("")))
		{
			String []ikd=addids.split(",");
			for(int i=0; i<ikd.length;i++)
			{
				if(noticeEntity.getNtype()==1)
				{
					NoticeVendorCFGEntity ncc=new NoticeVendorCFGEntity();
					ncc.setNoticeId(noticeEntity.getId());
					ncc.setOrgId(Long.parseLong(ikd[i]));
					ncc.setNtype(1);
					noticeVendorCFGDao.save(ncc);
				}
				else if(noticeEntity.getNtype()==2)
				{
					NoticeVendorCFGEntity ncc=new NoticeVendorCFGEntity();
					ncc.setNoticeId(noticeEntity.getId());
					ncc.setRoleId(Long.parseLong(ikd[i]));
					ncc.setNtype(2);
					noticeVendorCFGDao.save(ncc);
				}else if(noticeEntity.getNtype()==3){
					NoticeVendorCFGEntity ncc=new NoticeVendorCFGEntity();
					ncc.setNoticeId(noticeEntity.getId());
					ncc.setUserId(Long.parseLong(ikd[i]));
					ncc.setNtype(3);
					noticeVendorCFGDao.save(ncc);
				}
				
			}	
		}
		noticeDao.save(n);
		return "1";
	}

	public Page<CommentEntity> getCommentLookList(int pageNumber, int pageSize,Map<String, Object> searchParamMap, Long id) {
		PageRequest pagin =  PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		searchParamMap.put("EQ_noticeId", id);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<CommentEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), CommentEntity.class);
		Page<CommentEntity> page = commentDao.findAll(spec,pagin);
		return page;
	}
	
}
