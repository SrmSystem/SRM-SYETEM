package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.annotation.LogClass;
import com.qeweb.scm.basemodule.entity.CommentEntity;
import com.qeweb.scm.basemodule.entity.NoticeEntity;
import com.qeweb.scm.basemodule.entity.NoticeLookEntity;
import com.qeweb.scm.basemodule.service.NoticeService;

@Controller
@RequestMapping(value="/manager/vendor/notice")
public class NoticeController {
	
	@Autowired
	private NoticeService noticeService;
	
	private Map<String,Object> map;
	
	@RequestMapping(value="/getNoticeStart")
	public String getNoticeStart(HttpServletRequest httpServletRequest)
	{
		httpServletRequest.setAttribute("list", noticeService.getNoticeStart());
		return "back/notie";
	}
	@RequestMapping(value="/getNotice/{id}")
	public String getNoticeStart(@PathVariable("id") Long id,HttpServletRequest httpServletRequest)
	{
		httpServletRequest.setAttribute("noticeEntity", noticeService.getNotice(id));
		httpServletRequest.setAttribute("comments",noticeService.getCommentEntitys(id));
		return "/back/comment";
	}
	@RequestMapping(value="/getNoticeLookList/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getNoticeLookList(@PathVariable("id") Long id,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,HttpServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<NoticeLookEntity> page = noticeService.getNoticeLookList(pageNumber, pageSize, searchParamMap, id);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="/getCommentLookList/{id}",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getCommentLookList(@PathVariable("id") Long id,@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,HttpServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<CommentEntity> page = noticeService.getCommentLookList(pageNumber, pageSize, searchParamMap, id);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="/addcomment")
	@ResponseBody
	public String  addcomment(CommentEntity commentEntity){
		return noticeService.addcomment(commentEntity);
	}
	@RequestMapping(value="/getNoticeList",method = RequestMethod.GET)
	public String getNoticeList(HttpServletRequest httpServletRequest){
		return "/back/user/notieList";
	}
	
	@LogClass(method="查看", module="公告管理")
	@RequestMapping(value="/getNoticeList",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> getNoticeList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,HttpServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<NoticeEntity> page = noticeService.getNoticeList(pageNumber, pageSize, searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",page.getContent());
		map.put("total",page.getTotalElements());
		return map;
	}
	@RequestMapping(value="/addnotie",method = RequestMethod.GET)
	public String addnotie(HttpServletRequest httpServletRequest){
		return "/back/user/addnotie";
	}
	@LogClass(method="新增", module="公告新增")
	@RequestMapping(value="/addnotie",method = RequestMethod.POST)
	@ResponseBody
	public String  addnotie(NoticeEntity noticeEntity){
		
		return noticeService.addnotie(noticeEntity);
	}
	@RequestMapping(value="/updateNotie/{id}",method = RequestMethod.GET)
	public String updateNotie(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) throws Exception
	{
		httpServletRequest.setAttribute("noticeEntity", noticeService.updateNotieS(id));
		return "/back/user/updatenotie";
	}
	@LogClass(method="修改", module="公告修改")
	@RequestMapping(value="/updateNotie",method = RequestMethod.POST)
	@ResponseBody
	public String  updateNotie(NoticeEntity noticeEntity){
		return noticeService.updateNotie(noticeEntity);
	}
	
	@RequestMapping(value="/addnoties")
	@ResponseBody
	public String  addnoties(String ctx){
		
		return "<iframe  id='addnotiess' src='"+ctx+"/manager/vendor/notice/addnotie' width='100%' height='99%' frameborder='0'></iframe>";
	}
	@RequestMapping(value="/updateNoties")
	@ResponseBody
	public String  addnoties(String ctx,Long id){
		
		return "<iframe  id='addnotiess' src='"+ctx+"/manager/vendor/notice/updateNotie/"+id+"' width='100%' height='99%' frameborder='0'></iframe>";
	}
	@LogClass(method="删除", module="公告删除")
	@RequestMapping(value = "deleteNotice",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteNoticeList(@RequestBody List<NoticeEntity> noticeEntityList){
		Map<String,Object> map = new HashMap<String, Object>();
		noticeService.deleteNoticeList(noticeEntityList);
		map.put("success", true);
		return map;
	}
	@RequestMapping(value="/getNoticeStars")
	@ResponseBody
	public String  getNoticeStars(String ctx,String os,HttpServletRequest httpServletRequest){
		httpServletRequest.getSession().setAttribute("os", os);
		return "<iframe src='"+ctx+"/manager/vendor/notice/getNoticeStart' width='99%' style='min-height:98%;'frameborder='0'></iframe>";

	}
}
