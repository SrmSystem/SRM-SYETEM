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
if (window != top) {
	top.location.href = location.href; 
	}
		/*if(top.location!=self.location){
			top.location.reload();
		}*/
/* 		var clientWidth = document.body.clientWidth;
		var clientHeight = document.body.clientHeight; */
	</script>
	<meta name="viewport"
        content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible"
        content="ie=edge">
    <title>Document</title>
    <style>
        body {
            width: 100%;
            background-color: #E5E5E5;
            margin: 0;
        }

        .container {
            background-image: url('${ctx}/static/cuslibs/login/images/1.png');
            width: 1347px;
            height: 676px;
            /*  width: clientWidth;
            height: clientHeight; */
            margin: 0 auto;
            position: relative;
            top: -4px;left:-8%;
        }
        .login-username {
        	position:absolute /* relative */; top:65%; left:55%;
        	/* margin: 1px 2px 0 5px; */
        }
        .login-password {
        	position:absolute /* relative */; top:72%; left:55%;
        }
        .login-submit {
        	position:absolute /* relative */; top:79%; left:62%;
        }
    </style>
</head>
<body>
    <link rel="stylesheet" href="${ctx}/static/cuslibs/login/css/login.css"> 
 	<%-- <link rel="stylesheet" href="${ctx}/static/cuslibs/login/css/supersized.css">
    <link rel="stylesheet" href="${ctx}/static/base/bootstrap-3.3.4-dist/css/bootstrap.min.css">
	 --%>
	<!-- <script type="text/javascript">
		  var ctx = '${pageContext.request.contextPath}';
    </script> -->
<div class="login_language">
</div>
<%-- 
<div class="page-container">
	<div class="main_box" style="left:45%">
		<div style="float:left;">
			<img style="width: 100%; height: 100%;float:right;" src="${ctx}/static/cuslibs/login/images/3.png" alt="图片找不到"/> 
		</div>
		<div class="login_box" >
			<div class="login_form">
				<form id="login_form" action="${ctx}/public/login" method="post">
					<div class="login-username">
						<label for="j_username" class="t"><spring:message code="sys.username"/>：</label> 
						<input id="username" value="${username}" name="username" type="text" class="login-user-name" autocomplete="off">
					</div>
					<br/>
					<div class="login-password">
						<label for="j_password" class="t"><spring:message code="sys.password"/>：</label> 
						<input id="password" value="" name="password" type="password" class="login-pass-word">
					</div>
					<div class="login-submit">
						<label class="t" style="width: 75px;"></label>　
						<button type="submit"  id="submit_btn" class="btn btn-primary btn-lg"><spring:message code="sys.login.signin"/></button>
						&nbsp;&nbsp;&nbsp;
						<button type="reset"  id="submit_btn" class="btn btn-default btn-lg"><spring:message code="sys.login.reset"/></button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
 --%>
 
<div class="page-container">
	<div class="main_box" style="left:44%;z-index=90;">
		<div style="float:left;">
			<img style="width: 100%; height: 100%;float:right;" src="${ctx}/static/cuslibs/login/images/3.png" alt="图片找不到"/> 
		</div>
		<div class="login_box">
			<div class="login_form">
				<form id="login_form" action="${ctx}/public/login" method="post">
					<div class="form-group" style="position:absolute; top:65%; left:52%;">
						<%-- <label for="j_username" class="t"><spring:message code="sys.username"/>：</label>  --%>
						<label for="j_username" class="t" style="font-weight: bold;font-family: 宋体,黑体,楷体,仿宋,'Courier New'"><spring:message code="vendor.font.UserName"/><!-- 用户名称 -->：</label> 
						<input id="username" value="${username}" name="username" type="text" class="form-control x319 in" 
							autocomplete="off" style="width: 180px;">
					</div>
					<br/>
					<div class="form-group" style="position:absolute;top:72%; left:52%;">
						<%-- <label for="j_password" class="t"><spring:message code="sys.password"/>：</label>  --%>
						<label for="j_password" class="t" style="font-weight: bold;font-family: 宋体,黑体,楷体,仿宋,'Courier New'"><spring:message code="vendor.font.UserPassword"/><!-- 用户密码 -->：</label> 
						<input id="password" value="" name="password" type="password" class="password form-control x319 in"
							style="width: 180px;">
					</div>
					<div class="form-group space" style="position:absolute;top:77%; left:60%;">
						<label class="t" style="width: 75px;"></label>　
						<button type="submit" style="font-weight: bold;font-family: 宋体,黑体,楷体,仿宋,'Courier New'" id="submit_btn" class="btn btn-primary btn-lg"><spring:message code="sys.login.signin"/></button>
						&nbsp;&nbsp;&nbsp;
						<button type="reset" style="font-weight: bold;font-family: 宋体,黑体,楷体,仿宋,'Courier New'" id="submit_btn" class="btn btn-default btn-lg"><spring:message code="sys.login.reset"/></button>
					</div>
				</form>
				<!-- <div id="loginSV" class="bottom_" style="position:absolute;top:87%; left:55%;">
					<a href="http://www.aactechnologies.com" target="_bank">http://www.aactechnologies.com</a>
				</div> -->
			</div>
		</div>
	</div>
	<!-- <div id="loginSV" class="bottom_" style="margin-top: 26%;text-align: center;z-index=100;">
			<a href="http://www.aactechnologies.com" target="_bank">http://www.aactechnologies.com</a>
		</div> -->
</div>



<%-- <div class="page-container">
<div class="main_box" style="left:45%">
	<div style="float:left;">
		<img style="width: 100%; height: 100%;float:right;" src="${ctx}/static/cuslibs/login/images/3.png" alt="图片找不到"/> 
	</div>
	<div class="login_box" >
		<div class="login_form">
				<form id="login_form" action="${ctx}/public/login" method="post">
					<div class="login-username">
						<label for="j_username" class="t"><spring:message code="sys.username"/>：</label> 
						<input id="username" value="${username}" name="username" type="text" class="login-user-name" autocomplete="off">
					</div>
					<br/>
					<div class="login-password">
						<label for="j_password" class="t"><spring:message code="sys.password"/>：</label> 
						<input id="password" value="" name="password" type="password" class="login-pass-word">
					</div>
					<div class="login-submit">
						<label class="t" style="width: 75px;"></label>　
						<button type="submit"  id="submit_btn" class="btn btn-primary btn-lg"><spring:message code="sys.login.signin"/></button>
						&nbsp;&nbsp;&nbsp;
						<button type="reset"  id="submit_btn" class="btn btn-default btn-lg"><spring:message code="sys.login.reset"/></button>
					</div>
				</form>
			</div>
</div>
		</div>
</div>
<%-- <div class="page-container">
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
					</div>
					<div class="form-group space">
						<label class="t" style="width: 75px;"></label>　
						<button type="submit"  id="submit_btn" class="btn btn-primary btn-lg"><spring:message code="sys.login.signin"/></button>
						&nbsp;&nbsp;&nbsp;
						<button type="reset"  id="submit_btn" class="btn btn-default btn-lg"><spring:message code="sys.login.reset"/></button>
					</div>
				</form>
			</div>
		</div>
		<div id="loginSV" class="bottom"><a href="http://www.aactechnologies.com" target="_bank">http://www.aactechnologies.com</a></div>
	</div>
</div> --%>
	<script type="text/javascript" src="${ctx}/static/base/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/base/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/static/cuslibs/login/js/tooltips.js"></script>
	<script type="text/javascript" src="${ctx}/static/cuslibs/login/js/login.js"></script>
	
	<script src="${ctx}/static/cuslibs/login/js/supersized.3.2.7.min.js"></script>
<%-- 	<script src="${ctx}/static/cuslibs/login/js/supersized-init.js"></script> --%>
	<script src="${ctx}/static/cuslibs/login/js/scripts.js"></script>
    <% 
	String error = (String) (request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME));
     if(error!=null){
		if(error.contains("DisabledAccountException")){
			error="用户已被禁用,请登录其他用户.";
		}
		else if(error.contains("LockedAccountException")){
			error="用户账号已锁定，请联系系统管理员！";
		}else if(error.contains("ExpiredCredentialsException")){
			error="用户账号已过期，请联系系统管理员！";
		}else if(error.contains("IpLoginException")){
			error="外网无法访问SRM系统";
		}else if(error.contains("FirstLoginException")){
				error=null;
				Long loginUserId =Long.valueOf(String.valueOf(request.getAttribute("loginUserId")));
				%> 
    		 	<script type="text/javascript">
    		 		alert('<spring:message code="vendor.font.TheFirstLoginYouNeedToModifyThePassword"/>');/* 第一次登录，需要修改密码！ */
    		 		window.location.href="${ctx}/public/register/updatePassword/"+<%=loginUserId%>;
    		 	</script>
    	 	<% 
		}else{
			error="登录失败，账号或密码错误.";
		}
	}
     
     if(error != null){
    	 	%> 
    		 	<script type="text/javascript">
    		 	alert('<%=error%>');
    		 	</script>
    	 	<% 
    		}
     
 	%>  
 	<script type="text/javascript">
 		function topso()
 		{
 			window.location.href=ctx+"/public/register";
 		}
 		
 	</script>
</body>
</html>