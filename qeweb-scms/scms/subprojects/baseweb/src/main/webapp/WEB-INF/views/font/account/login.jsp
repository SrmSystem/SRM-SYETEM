<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><spring:message code="${projectName}"/>-<spring:message code="sys.login.signin"/></title>
<script type="text/javascript">
		/*if(top.location!=self.location){
			top.location.reload();
		}*/
	</script>
</head>
<body>
 	<link rel="stylesheet" href="${ctx}/static/cuslibs/login/css/supersized.css">
    <link rel="stylesheet" href="${ctx}/static/cuslibs/login/css/login.css"> 
    <link rel="stylesheet" href="${ctx}/static/base/bootstrap-3.3.4-dist/css/bootstrap.min.css">
	<script type="text/javascript">
		  var ctx = '${pageContext.request.contextPath}';
    </script>
<div class="login_language">
	<form action="${ctx }/public/common/language" method="post"> 
			Language:<select id="language" name="language" style="width:160px;height:20px;">
					<option value="zh" <c:if test="${language eq 'zh' }">selected</c:if>>Chinese</option>
					<option value="en" <c:if test="${language eq 'en' }">selected</c:if>>English</option></select>
	</form>
</div>
<div class="page-container">
	<div class="main_box">
		<div class="login_box">
			<div class="login_logo">
				<div id="ssdsdf"></div>
				<h2><a href="${ctx}"><spring:message code="${projectName}"/></a><small>--<spring:message code="sys.login.signin"/></small></h2>
			</div>
		
			<div class="login_form">
				<form id="login_form" action="${ctx}/public/login" method="post">
					<div class="form-group">
						<label for="j_username" class="t"><spring:message code="sys.username"/>：</label> 
						<input id="username" value="${username}" name="username" type="text" class="form-control x319 in" autocomplete="off">
					</div>
					<br/>
					<div class="form-group">
						<label for="j_password" class="t"><spring:message code="sys.password"/>：</label> 
						<input id="password" value="" name="password" type="password" class="password form-control x319 in">
						<a href="${ctx}/public/register/backPassword"><spring:message code="sys.forgot.password"/></a>
					</div>
					<div class="form-group">
					<label for="j_password" class="t"><spring:message code="sys.remember"/>：</label> 
                          <input type="checkbox" id="rememberMe" name="rememberMe" /> 
                          
					</div>
					<div class="form-group space">
						<label class="t" style="width: 75px;"></label>　
						<button type="submit"  id="submit_btn" class="btn btn-primary btn-lg"><spring:message code="sys.login.signin"/></button>
						&nbsp;&nbsp;&nbsp;
						<button type="reset"  id="submit_btn" class="btn btn-default btn-lg"><spring:message code="sys.login.reset"/></button>
						&nbsp;&nbsp;&nbsp;
						<button type="button"  class="btn btn-primary btn-lg" style="background-color: #06C752" onclick="topso()"><spring:message code="sys.login.register"/> </button>
					</div>
				</form>
<!-- 				<div class="controls text-center"><span class="help-block">(管理员: <b>admin/admin</b>, 普通用户: <b>user/user</b>&nbsp;&nbsp;敲回车登陆)</span></div> -->
			</div>
		</div>
		<div id="loginSV" class="bottom"> <a href="http://www.qeweb.com" target="_bank">Qeweb.com Inc.</a></div>
	</div>
</div>
	<script type="text/javascript" src="${ctx}/static/base/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/base/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/static/cuslibs/login/js/tooltips.js"></script>
	<script type="text/javascript" src="${ctx}/static/cuslibs/login/js/login.js"></script>
	
	<script src="${ctx}/static/cuslibs/login/js/supersized.3.2.7.min.js"></script>
	<script src="${ctx}/static/cuslibs/login/js/supersized-init.js"></script>
	<script src="${ctx}/static/cuslibs/login/js/scripts.js"></script>
    <% 
	String error = (String) (request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME));
	if(error!=null){
		if(error.contains("DisabledAccountException")){
			error="用户已被屏蔽,请登录其他用户.";
		}
		else{
			error="登录失败，请重试.";
		}
	}
    
    
    if(error != null){
 	%> 
	 	<script type="text/javascript">
	 	showError('<%=error%>');
	 	</script>
 	<% 
	}
 	%>  
 	<script type="text/javascript">
 		function topso()
 		{
 			window.location.href=ctx+"/public/register";
 		}
 		
 		$(function(){
 			$('#language').change(function(){ 
 				//var language = $(this).children('option:selected').val(); 
 				//document.location.href="${ctx}/public/common/language/" + language;
 				 document.forms[0].submit();   
 			});
 		});
 	</script>
</body>
</html>