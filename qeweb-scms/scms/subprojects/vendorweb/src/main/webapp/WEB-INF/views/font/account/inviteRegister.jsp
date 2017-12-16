<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>受邀注册</title>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#loginName").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					"loginName": {
						remote: "${ctx}/public/register/checkLoginName"
					}
					,"orgName" : {
						remote: "${ctx}/public/register/checkOrgName"
					}
					,"orgEmail" : {
						remote: "${ctx}/public/register/checkOrgEmail"
					}
					
				},
				messages: {
					"loginName": {
						remote: "<font style='color: #FF3765;'>用户登录名已注册 </font>"
					}
					,"orgName" : {
						remote:"<font style='color: #FF3765;'>该组织名称已注册</font>"
					}
					,"orgEmail" : {
						remote:"<font style='color: #FF3765;'>该email已注册</font>"
					}
				}
			});
		});
	</script>
	<script type="text/javascript">
		  var ctx = '${pageContext.request.contextPath}';
    </script>
</head>

<body>
	<link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/base.css" />
    <link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/layout.css"/>
	<div id="wrapper">
		<header id="header">

			<div id="headBox">
				<div class="w960 oh">
					<nav id="navs" class="fr">
						<a href="${backherf}">登录界面</a>
						<a class="active">注册界面</a>
					</nav>
				</div>
			</div>
		</header><!-- // header end -->
		<div class="container w960 mt20">
			<div id="processor" >
				<ol class="processorBox oh">
					<li  class="current">
						<div class="step_inner fl">
							<span class="icon_step">1</span>
							<h4>注册帐号信息</h4>
						</div>
					</li>
					<li>
						<div class="step_inner">
							<span class="icon_step">2</span>
							<h4>填写基本信息</h4>
						</div>
					</li>
					<li>
						<div class="step_inner fr">
							<span class="icon_step">3</span>
							<h4>注册结果</h4>
						</div>
					</li>
				</ol>
				<div class="step_line"></div>
			</div>
			<div class="content">
				<div id="step1" class="step hide" style="  margin-left: 20%;">
						<form id="inputForm" action="${ctx}/public/register" method="post">
						<input type="hidden" id="inviteMailId" name="inviteMailId" value="${inviteMailId}"/>
						<div class="frm_control_group">
							<label class="frm_label">企业全称</label>
							<div class="frm_controls">
							    <input type="text" id="orgName" name="orgName" value="${orgName}" readonly="readonly" class="frm_input required"/>
								<p class="frm_tips">企业全称--必填项</p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label">Email</label>
							<div class="frm_controls">
							    <input type="text" id="orgEmail" name="orgEmail" value="${email}" readonly="readonly" class="frm_input required email"/>
								<p class="frm_tips">--必填项</p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label">登录名</label>
							<div class="frm_controls">
								<input type="text" id="loginName" name="loginName" class="frm_input required" minlength="3"/>
								<p class="frm_tips">作为登录帐号，请填写未被注册的登录名</p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label">昵称</label>
							<div class="frm_controls">
							<input type="text" id="name" name="name" class="frm_input required"/>
								<p class="frm_tips">昵称--必填项</p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label">密码</label>
							<div class="frm_controls">
							    <input type="password" id="password" name="password" class="frm_input passwd required"/>
								<p class="frm_tips">字母、数字或者英文符号，最短6位，区分大小写</p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label">再次输入密码</label>
							<div class="frm_controls">
							     <input type="password" id="confirmPassword" name="confirmPassword" class="frm_input passwd2 required" equalTo="#password"/>
							     <p class="frm_tips">用于让用户再次确认密码------必填项</p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label">联系电话</label>
							<div class="frm_controls">
							    <input type="text" id="orgPhone" name="orgPhone" class="frm_input required"/>
								<p class="frm_tips">--必填项</p>
							</div>
						</div>
						<div class="toolBar">
						    <input id="nextBtn" class="btn btn_primary" type="submit" value="下一步"/>&nbsp;	
						</div>
					</form>
				</div><!-- // step1 end -->
				<div id="step2" class="step hide">
				</div>
				<div id="step3" class="step hide">
				</div>
			</div>
		</div><!-- // container end -->
		<footer id="footer" class="w960 oh">
			<span class="fl">Copyright © 2008-2015 <a href="http://www.qeweb.com" target="_bank">Qeweb.com Inc.</a></span>
		</footer><!-- // footer end -->
	</div><!-- // wrapper end -->
</body>
</html>
