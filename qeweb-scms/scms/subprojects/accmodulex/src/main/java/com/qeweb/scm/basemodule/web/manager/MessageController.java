package com.qeweb.scm.basemodule.web.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MessageEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.service.MessageService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.service.ViewService;
import com.qeweb.scm.basemodule.utils.DateUtil;

@Controller
@RequestMapping("/manager/basedata/msg")
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ViewService viewService;
	
	private Map<String,Object> map;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "back/basedata/messageList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> materialList(@RequestParam(value="page") int pageNumber,
			@RequestParam(value="rows") int pageSize,
			Model model,ServletRequest request){
		Map<String,Object> searchParamMap = Servlets.getParametersStartingWith(request, "search-");
		Page<MessageEntity> userPage = messageService.getMessageList(pageNumber,pageSize,searchParamMap);
		map = new HashMap<String, Object>();
		map.put("rows",userPage.getContent());
		map.put("total",userPage.getTotalElements());
		return map;
	}
	
	@RequestMapping(value = "close/{id}")
	@ResponseBody
	public Map<String, Object> read(@PathVariable("id") Long id) {
		map = new HashMap<String, Object>();
		messageService.closeMessage(id);
		map.put("message", "关闭消息成功");
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping(value="/getMessage")
	@ResponseBody
	public String  addnoties(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("EQ_toUserId", user.id + "");
		param.put("EQ_isRead", StatusConstant.STATUS_NO + "");     
		param.put("EQ_abolished", StatusConstant.STATUS_NO + "");     
		List<MessageEntity> msgList = messageService.getMessageList(param);
		if(CollectionUtils.isEmpty(msgList))
			return "";
		
		StringBuffer sb = new StringBuffer();
		for(MessageEntity ms : msgList) {
			sb.append("<li id='msg_" + ms.getId() + "'><a href='javascript:;' class='easyui-linkbutton' data-options=\"iconCls:'icon-delete',plain:true\" onclick=\"operate(" + ms.getId() + ")\"></a>");
			sb.append(DateUtil.dateToString(ms.getCreateTime(), DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM)).append(ms.getTitle() + "</li>");
		}
		return sb.toString();
	}
	@RequestMapping(value="/getMenu")
	@ResponseBody
	public String  getMenu(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<ViewEntity> menuList = ViewService.getUserViews(user.id);
		if(CollectionUtils.isEmpty(menuList))
			return "";
		
		StringBuffer sb = new StringBuffer();
		/*sb = getMenuString(sb, menuList);*/
		for(ViewEntity ms : menuList) {
			sb.append("<li class=\"l1\"><a href=\"javascript:c(m"+ms.getId()+");\" id=\"m"+ms.getId()+"\"><span><img src=\"static/cuslibs/images/ico/2.gif\" align=\"absMiddle\"/>"+ms.getViewName()+"</span></a></li>");
			sb.append("<ul id=\"m"+ms.getId()+"d\" style=\"display:none;\" class=\"U1\"></ul>");
		}
		return sb.toString();
	}
	@RequestMapping(value="/getSonMenu/{mid}")
	@ResponseBody
	public String  getMenu(@PathVariable("mid") String mid){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		List<ViewEntity> menuList = ViewService.getUserViews(user.id);
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isEmpty(menuList))
			return "";
		Long id=Long.parseLong(mid.replace("m", ""));
		for(ViewEntity vm:menuList)
		{
			if(vm.getId()==id)
			{
				sb=getMenuString(sb,vm.getItemList(),1);
			}
		}
		return sb.toString();
	}
	private  StringBuffer  getMenuString(StringBuffer sb,List<ViewEntity> menuList,Integer numb){
		for(ViewEntity ms : menuList) {
			if(ms.getItemList()!=null&&!ms.getItemList().isEmpty()){
				sb.append("<li class=\"l21\"><a href=\"javascript:c(f"+ms.getId()+");\" id=\"f"+ms.getId()+"\"><span><img src=\"static/cuslibs/images/ico/2.gif\" align=\"absMiddle\"/>"+ms.getViewName()+"</span></a></li>");
				sb.append("<ul id=\"f"+ms.getId()+"d\" style=\"display:none;\">");
				getMenuString(sb,ms.getItemList(),121);
				sb.append(" </ul>");
			}
			else
			{
				if(numb==121)
				{
					sb.append("<li class=\"l3\"><a  href=\"javascript:a('notify/show','"+ms.getId()+"');\" id=\"f"+ms.getId()+"\" onclick=\"toManager('"+ms.getViewName()+"','"+ms.getViewUrl()+"')\"><span><img src=\"static/cuslibs/images/ico/2.gif\" align=\"absMiddle\"/>"+ms.getViewName()+"</span></a></li>");
				}
				else
				{
					sb.append("<li class=\"l22\"><a  href=\"javascript:a('notify/show','"+ms.getId()+"');\" id=\"f"+ms.getId()+"\" onclick=\"toManager('"+ms.getViewName()+"','"+ms.getViewUrl()+"')\"><span><img src=\"static/cuslibs/images/ico/2.gif\" align=\"absMiddle\"/>"+ms.getViewName()+"</span></a></li>");
				}
			}
		}
		return sb;
	}
}
