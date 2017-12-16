<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.qeweb.scm.basemodule.context.ProjectContextUtil"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
var ctx = '${pageContext.request.contextPath}';
</script>
<html>
<head>
<title><spring:message code="<%=ProjectContextUtil.getProjectName() %>"/></title>
<script type="text/javascript" src="${ctx}/static/base/easyui/extension/datagrid-scrollview.js"></script>
<style type="text/css">
#manager-menu a{
text-decoration: none;
color: #333;
}
</style>
</head>
<body>

<div id="manager-menu" data-options="region:'west',split:true,iconCls:'icon-house'" style="width: 205px; padding: 1px;" title="<spring:message code="sys.myapp"/>">
        <ul id="tree-menu" class="easyui-tree" data-options="lines:false,animate:true">
          <c:forEach items="${menuList}" var="menu" varStatus="menuStatus">
             <li>
               <span><spring:message code="${menu.viewName }"/></span>
               <c:if test="${menu.itemList!=null}">
			      <ul>
			        <tags:menuRe list="${menu.itemList}"></tags:menuRe>
			      </ul>
			   </c:if>
             </li>
          </c:forEach>
        </ul>	

</div>
<div id="manager-content" data-options="region:'center',border:true">
	<div class="easyui-tabs" id="manager-tab" data-options="fit:true, border:false,
	tools:'#manager-content-tabs-tool'
	">
		<div id="desk" title="<spring:message code="sys.desk"/>" data-options="iconCls:'icon-computer'">
		<div class="easyui-layout" data-options="fit: true">
		<div data-options="region: 'center', border: false" style="overflow: hidden;">
		  <div id="desk-pp" style="position: relative;">
			<div style="width:30%;">
				<div title="<spring:message code="sys.backlog"/>" data-options="iconCls:'icon-clock',collapsible:true,closable:true" style="height:250px;">
			    	<table id="datagrid-backlogcfg" class="easyui-datagrid"
						data-options="fit:true,
						url:'${ctx}/manager/backlog/list',method:'post',singleSelect:false,
						view:scrollview,rownumbers:true,queryParams:{page:1,rows:10},
				        autoRowHeight:false,pageSize:10"			       
						>
						<thead><tr>
						<th data-options="field:'id',hidden:true"></th>
						<th data-options="field:'viewId',hidden:true"></th>
						<th data-options="field:'viewName',width:100"><spring:message code="sys.backlog.module"/></th>
						<th data-options="field:'content',width:130"><spring:message code="sys.backlog.content"/></th>
						<th data-options="field:'count',formatter:backlogFmt"><spring:message code="sys.backlog.count"/></th>
						</tr></thead>
					</table>
			    </div>
			    <div title="<spring:message code="sys.message"/>" data-options="iconCls:'icon-comment',closable:true" style="height:250px;">
<!-- 					<input class="easyui-searchbox" /> -->
					<ul id="msglist">
						<%--
						<c:forEach items="${msgList}" var="msg" varStatus="msgStatus">  
							<li id="msg_${msg.id}"><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="operate(${msg.id})"></a>
							<fmt:formatDate value="${msg.createTime }" pattern="yy-MM-dd HH:mm"/> ${msg.title }</li>
						</c:forEach>
						 --%>
					</ul>  
					<!-- 
					  <div data-options="iconCls:'icon-server_wrench'" title="${menu.viewName}" >
					    ${msg.title}
					  </div>
					 -->
				</div>
			</div>
<%-- 			<div id="tishi" class="easyui-dialog" title="警告提示" style="width:60%;height:80%;"   
			        data-options="iconCls:'icon-save',resizable:true,modal:true">
				<div style="background: #FF0;width: 100%;height: 100%;">
					<font style="color: #000;font-size: 24px;font-weight: 900px;">你的浏览器版本过低，可能导致网站不能正常访问！<BR/>为了你能正常使用网站功能，请使用这些浏览器。<font color="red">(点击图标直接下载)</font></font>
					<br/>
						<table style="width: 100%;">
							<tr/>
								<td align="center">
								<font color="red">强烈建议</font><br/>
									<a href="http://w.x.baidu.com/alading/anquan_soft_down_ub/14744" target="_blank">
						           	 <img src="${ctx}/static/cuslibs/images/Chrome.png" style="WIDTH: 100px; HEIGHT: 100px"><br>chrome
						            </a>
								</td>
								<td align="center">
									<a href="http://www.firefox.com.cn/" target="_blank"><img
						            src="${ctx}/static/cuslibs/images/Firefox.png" style="WIDTH: 100px; HEIGHT: 100px"><br>firefox
						            </a>
								</td>
								<td align="center">
									<a href="http://support.apple.com/kb/dl1531" target="_blank"><img
						            src="${ctx}/static/cuslibs/images/Safari.png" style="WIDTH: 100px; HEIGHT: 100px"><br>safari
						            </a>
								</td>
								<td align="center">
									<a
						            href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie"
						            target="_blank"><img
						            src="${ctx}/static/cuslibs/images/IE.png" style="WIDTH: 100px; HEIGHT: 100px"><br>ie9及以上
						            </a>
								</td>
							</tr>
						</table>
				</div>   
			</div>  --%>
			<div style="width:30%;">
				<div id="gg" title="<spring:message code="sys.notice"/>" data-options="iconCls:'icon-note',closable:true" style="min-height:250px;">
				</div>
				
			</div>
			<div style="width:30%;">
				<div id="userStatus" title="<spring:message code="sys.status"/>" data-options="iconCls:'icon-vcard',closable:true" style="min-height:250px;">
				</div>
			</div>
			<!-- 
			<div style="width:30%;">
				<div title="消息提醒" data-options="iconCls:'icon-search',closable:true" style="height:200px;">
					<input class="easyui-searchbox" />
				</div>
			</div>
			 -->  
		</div>
		</div>
		</div>
		</div>
	</div>
</div>
<div id="manager-content-tabs-tool">
	<a href="javascript:;" title="刷新页面" class="easyui-linkbutton" plain="true" iconCls="icon-reload" onclick="reloadTab()"></a>
	<a href="javascript:;" title="关闭所有选项卡" class="easyui-linkbutton" plain="true" iconCls="icon-cancel" onclick="closeTab()"></a>
</div>

<div id="dd" class="easyui-dialog" title="公告详细内容" style="width:90%;height:90%;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true">   
</div> 
<script type="text/javascript">
setInterval("myInterval()",300000);//1000为1秒钟
function myInterval()
{
	$.post(ctx+"/manager/vendor/notice/getNoticeStars",{"ctx":ctx},function(data){
		$("#gg").html(data);
	},"text");	
}
	function toManager(title,url){
		if($('#manager-tab').tabs('exists',title)){
			$('#manager-tab').tabs('select',title);
			return false;
		}
		url = '${ctx}/'+url;
		$('#manager-tab').tabs('add',{
			title:title,
			content:'<iframe src="'+url+'" frameborder="0" style="width:100%;height:99%;"></iframe>',
			closable:true
		});
		
	}
	
	function backlogFmt(v,r,i){
		if(v=='0'){
			return v;
		}
		return '<a href="javascript:;" onclick="toManager(\''+r.viewName+'\',\''+r.viewUrl+'\')">'+v+'</a>';
	}
	
	function lookNotice(id)
	{
		var os;
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        var s;
        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
        (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
        (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
 
        //以下进行测试
        if (Sys.ie) os='IE: ' + Sys.ie;
        if (Sys.firefox) os='Firefox: ' + Sys.firefox;
        if (Sys.chrome) os='Chrome: ' + Sys.chrome;
        if (Sys.opera) os='Opera: ' + Sys.opera;
        if (Sys.safari) os='Safari: ' + Sys.safari;
        
		$('#dd').dialog({    
		    title: '公告详细内容',      
		    closed: false,    
		    cache: false,    
		    href: ctx+'/manager/vendor/notice/getNotice/'+id,   
		    modal: true   
		}); 
	}
	
	$(function(){	
		var os;
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        var s;
        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
        (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
        (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
        (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
        (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
 
        //以下进行测试
        if (Sys.ie) os='IE: ' + Sys.ie;
        if (Sys.firefox) os='Firefox: ' + Sys.firefox;
        if (Sys.chrome) os='Chrome: ' + Sys.chrome;
        if (Sys.opera) os='Opera: ' + Sys.opera;
        if (Sys.safari) os='Safari: ' + Sys.safari;

		$('#dd').window('close');
		$('#desk-pp').portal({
			border:false,
			fit:false
		});

		$.post(ctx+"/manager/vendor/notice/getNoticeStars",{"ctx":ctx,"os":os},function(data){
			$("#gg").html(data);
		},"text");
		$.post(ctx+"/manager/basedata/msg/getMessage",function(data){
			$("#msglist").html(data);
			$.parser.parse($('#msglist'));
		},"text");
		$('#desk-pp').portal('resize');
	});
	
	//设置消息已读
	function operate(id) {
		$.ajax({
			url:'${ctx}/manager/basedata/msg/close/' + id,
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.show({
							title:'消息',
							msg:  data.message, 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$("#msg_" + id).hide();   
					}else{
						$.messager.alert('提示',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',e,'error'); 
				} 
			}
		});
	}

	function closeTab(){
		$(".tabs-closable").each(function(index, obj) {
            //获取所有可关闭的选项卡  
            var tab = this.textContent;  
            $(".easyui-tabs").tabs('close', tab);  
      	});
		/* var currTab =  $('#manager-tab').tabs('getSelected'); 
		if("desk" == currTab.panel('options').id)
			return false;
		
		var title = currTab.panel('options').title;
		$('#manager-tab').tabs('close',title); */
	}	

	function reloadTab() {
		var currTab =  $('#manager-tab').tabs('getSelected'); //获得当前tab
		if("desk" == currTab.panel('options').id)
			return false;
		
	    var url = $(currTab.panel('options').content).attr('src');
	    $('#manager-tab').tabs('update', {
	      tab : currTab,
	      options : {
	       content : '<iframe src="'+url+'" frameborder="0" style="width:100%;height:99%;"></iframe>'
	      }
		});
	}
</script>
</body>
</html>