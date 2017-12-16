<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.qeweb.scm.basemodule.utils.UserContext"%>
<%@ page import="com.qeweb.scm.basemodule.context.ProjectContextUtil"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html class="panel-fit">
<head>
<title><sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE">
<script type="text/javascript">
 var ctx = '${ctx}';
 debugger;
 var ctx1  =<shiro:principal property="orgRoleType" />;
 var ctx2  ="<shiro:principal property='roles' />";
</script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/style/base.css">
<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${ctx}/static/base/easyui/themes/<%=UserContext.getUserTheme() %>/easyui.css">
<%-- <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${ctx}/static/base/easyui/themes/ui-cupertino/easyui.css"> --%>
<link rel="stylesheet" type="text/css" href="${ctx}/static/style/icons/IconExtension.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/base/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/static/base/jquery-easyui-portal/portal.css">
<script type="text/javascript" src="${ctx}/static/base/easyui/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/static/base/jquery-i18n/jquery.i18n.properties-min-1.0.9.js"></script>
<script type="text/javascript">
/** 动态加载easyui-lang **/
var lans = {'zh-CN' : 'zh_CN','zh_CN' : 'zh_CN', 'en-us' : 'en', 'en':'en'};
var lan = navigator.language || navigator.userLanguage;
var arrStr = document.cookie.split("; ");
for (var i = 0; i < arrStr.length; i++) {
    var temp = arrStr[i].split("=");
    if (temp[0] == 'Language') {
        lan = unescape(temp[1]);
    }
}
/** 初始化jQuery.i18n **/
jQuery.i18n.properties({
    name:'message',
    path:'${ctx}/static/script/',
    mode:'map',
    language: lans[lan],
    callback: function() {// 加载成功后设置显示内容
    }
});
$.getScript('${ctx}/static/base/easyui/locale/easyui-lang-' + lans[lan] + '.js');
</script>
<script type="text/javascript" src="${ctx}/static/base/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript">
// document.write('<script type="text/javascript" src="${ctx}/static/base/easyui/locale/easyui-lang-'+lans[lan]+'.js"/>');
</script>
<%-- <script type="text/javascript" src="${ctx}/static/base/easyui/locale/easyui-lang-zh_CN.js"></script> --%>
<script type="text/javascript" src="${ctx}/static/base/jquery-easyui-portal/jquery.portal.js"></script>
<script type="text/javascript" src="${ctx}/static/base/jquery-cookie/jquery.cookie.js"></script>
<script type="text/javascript" src="${ctx}/static/script/system/theme.js"></script>
<script src="${ctx}/static/script/html5.js"></script>
 <script  type="text/javascript" src="${ctx}/static/script/basedata/base-loading.js"></script>
<script type="text/javascript">
 baseLoading();
</script>
<sitemesh:head/>
</head>
<body class="panel-fit">
<div id="gloabContainer" class="easyui-layout" data-options="fit:true,border:false">
	<div id="header" data-options="region:'north'">
		<div id="title" class="easyui-panel" style="overflow:hidden;"
			data-options="noheader:true,border:false"> 
			<div class="p-logo" style="padding:0px; width: 100%; height: 100%; position: absolute;z-index=90;">
				<div style="float:left;height: 95%;width: 16.2%;left:-4px">
					<img style="float:left;" src="${ctx}/<%=ProjectContextUtil.getProjectLogo() %>" alt="图片找不到"/> 
				</div>
				<div style="width: 83.8%; height: 100%;float:left;left:10%">
					<img style="width: 100%; height: 100%;float:right;" src="${ctx}/static/cuslibs/login/images/2.png" alt="图片找不到"/> 
				</div>
				<!-- <strong>Qeweb</strong> -->
			<%-- 	<img src="${ctx}/<%=ProjectContextUtil.getProjectLogo() %>" alt="图片找不到"/>  --%>
			</div>
			<div id="user-bar" style="position: relative;z-index=100;" >
				<div id="user-style" class="user-style fl">
				<table>
				<!-- <tr>
					<td style="vertical-align: middle;">风格</td>
					<td>
					<select id="stylecombobox" class="easyui-combobox"
						data-options="onSelect:changeTheme,value:'<%=UserContext.getUserTheme() %>'" 
					>
						<option value="default">default</option>
						<option value="bootstrap">bootstrap</option>
						<optgroup label="metro">
							<option value="metro">metro</option>
							<option value="metro-blue">metroBlue</option>
						</optgroup>
						
						<option value="gray">gray</option>
					</select>
					</td></tr>
					 -->
					</table>
					
					
				</div>
				<div id="user-info" class="fr">	
					<div id="user-info-tool">
							<a style="color:#ffffff" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-help'"><spring:message code="sys.help"/></a>
							<a style="color:#ffffff" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-information'"><spring:message code="sys.about"/></a>
							<a style="color:#ffffff" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-man'" onclick="personal()"> <spring:message code="sys.person.settion"/>  <label id="show">（<spring:message code="sys.change.password"/>）</label>          </a>
							<a style="color:#ffffff"  onclick="logout()"  class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-control_power'"><spring:message code="sys.exit"/></a>
					</div>
					<div id="user-info-msg">
						<shiro:user>
							<span class="l-btn-left l-btn-icon-left"><span class="icon-20130410120031302_easyicon_net_16 l-btn-icon">&nbsp;</span><span class="l-btn-text"><shiro:principal property="name" /></span></span>
						</shiro:user>
					</div>
				</div>
			</div>
		</div>
	</div>
<div id="dds" class="easyui-dialog" title='<spring:message code="sys.person.settion"/>' style="width:53%;height:37%;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true">   
</div> 
	<sitemesh:body/>
	<div id="footer" class="easyui-panel"
		data-options="region:'south',border:false" style="text-align: center;height: 40px;"
	>
		<p>Copyright &copy; 2008-2017 <a href="http://www.aactechnologies.com">Aactechnologies.com Inc.</a></p>
	</div>
</div>	
<script type="text/javascript">


setInterval("verification()",5000);//1000为1秒钟
function verification(){
	$.post(ctx+"/verification/verificationSession",function(data){
		if(data != "true"){
			 $.messager.alert('提示信息', "登录超时！请重新登录！", 'info',function(){
                 window.location.href = ctx+'/logout';
             });
		}
	},"text");
 }




$(function() { 
	if(ctx1 == "0" && ctx2 !="admin"){
		document.getElementById('show').innerText='';
	  /*  document.getElementById('upass').innerHTML=''; */
	}	
})
function personal()
{
	$('#dds').dialog({         
	    closed: false,    
	    cache: false,    
	    href: ctx+'/manager/vendor/personal',    
	    modal: true   
	}); 
}
function logout(){
	$.messager.confirm('提示','是否确定退出系统吗？<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){
		if(r){
			window.location.href=ctx+'/logout';
		}
	});
}



</script>
</body>
</html>