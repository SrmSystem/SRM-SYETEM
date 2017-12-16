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
<sitemesh:head/>
</head>
<body class="panel-fit">
<div id="gloabContainer" class="easyui-layout" data-options="fit:true,border:false">
	<div id="header" data-options="region:'north'">
		<div id="title" class="easyui-panel" style="overflow:hidden;"
			data-options="noheader:true,border:false"> 
			<div class="p-logo">
				<img src="${ctx}/<%=ProjectContextUtil.getProjectLogo() %>" alt="图片找不到"/> 
				<strong>Qeweb</strong>
			</div>
			<div id="user-bar">
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
							<a style="color:#ffffff" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-man'" onclick="personal()"><spring:message code="sys.person.settion"/>（<spring:message code="sys.change.password"/>）</a>
							<a style="color:#ffffff" href="${ctx}/logout" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-control_power'"><spring:message code="sys.exit"/></a>
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
		<p>Copyright2 &copy; 2008-2015 <a href="http://www.qeweb.com">Qeweb.com Inc.</a></p>
	</div>
</div>	
<script type="text/javascript">
function personal()
{
	$('#dds').dialog({         
	    closed: false,    
	    cache: false,    
	    href: ctx+'/manager/vendor/personal',    
	    modal: true   
	}); 
}
</script>
</body>
</html>