<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title><!-- 修改密码 --><spring:message code="vendor.font.ModifyPassword"/></title>
	<link href="${ctx}/static/base/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
	<link rel="stylesheet" href="${ctx}/static/base/zTree_v3/css/metroStyle/metroStyle.css" type="text/css">
	<link href="${ctx}/static/styles/register.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/static/base/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${ctx}/static/base/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${ctx}/static/base/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${ctx}/static/base/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
	<script>

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
							<h4><spring:message code="vendor.font.EnterTheOriginalPassword"/><!-- 输入原密码 --></h4>
						</div>
					</li>
					<li>
						<div class="step_inner">
							<span class="icon_step">2</span>
							<h4><spring:message code="vendor.font.ModifyTheNewPassword"/><!-- 修改新密码 --></h4>
						</div>
					</li>
					<li>
						<div class="step_inner fr">
							<span class="icon_step">3</span>
							<h4><spring:message code="vendor.font.LoginSystemAgain"/><!-- 重新登录系统 --></h4>
						</div>
					</li>
					
				</ol>
				<div class="step_line"></div>
			</div>

  <input type="hidden" id="loginUserId" name="loginUserId" value="${loginUserId }">
  
			<div class="content">
				<div id="step1" class="step hide" style="  margin-left: 20%;">
						<form id="inputForm"  method="post">
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.OriginalPassword"/><!-- 原密码 --></label>
							<div class="frm_controls">
							    <input type="password" id="oldPassword" name="oldPassword" class="frm_input passwd required"/>
								<p class="frm_tips"></p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.PassWorld"/><!-- 密码 --></label>
							<div class="frm_controls">
							    <input type="password" id="password" name="password" class="frm_input passwd required"/>
								<p class="frm_tips"><spring:message code="vendor.font.passwordMustContainLettersNumbersSymbolsAndLengthsGreaterThanOrEqualTo8Bits"/><!-- 密码必须包含字母数字符号且长度大于等于8位 --></p>
							</div>
						</div>
						<div class="frm_control_group">
							<label class="frm_label"><spring:message code="vendor.font.EnterPasswordAgain"/><!-- 再次输入密码 --></label>
							<div class="frm_controls">
							     <input type="password" id="confirmPassword" name="confirmPassword" class="frm_input passwd2 required" equalTo="#password"/>
							     <p class="frm_tips"><spring:message code="vendor.font.UsedToAllowUsersToReconfirmPasswords"/><!-- 用于让用户再次确认密码 -->------<spring:message code="vendor.font.Required"/><!-- 必填项 --></p>
							</div>
						</div>

						<div class="toolBar">
						    <input id="nextBtn" class="btn btn_primary" type="button" onclick="saveSub()" value="保存"/>&nbsp;	
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

		</footer><!-- // footer end -->
	</div><!-- // wrapper end -->
	<script>
		function saveSub(){
			var oldPassword = document.getElementById('oldPassword').value;
			if(null==oldPassword || oldPassword==""){
				alert("<spring:message code="vendor.font.TheOriginalPasswordCanNotBeEmpty"/>");/* 原密码不能为空 */
				return ;
			}
			var password = document.getElementById('password').value;
			if(null==password || password==""){
				alert("<spring:message code="vendor.font.PasswordCannotBeEmpty"/>");/* 密码不能为空 */
				return ;
			}
			var confirmPassword = document.getElementById('confirmPassword').value;
			if(null==confirmPassword || confirmPassword==""){
				alert("<spring:message code="vendor.font.ThePasswordCannotBeEmptyAgain"/>");/* 再次输入密码不能为空 */
				return ;
			}
			var loginUserId = document.getElementById('loginUserId').value;
				$.post(ctx+"/public/register/saveUpdatePassword",{"loginUserId":loginUserId,"oldPassword":oldPassword,"password":password,"confirmPassword":confirmPassword},function(data){
					if(data==0)
					{
						alert("<spring:message code="vendor.font.PasswordChangesSuccessfullyReturnToLoginPage"/>");/* 密码修改成功，返回登录页面 */
						window.location.href=ctx+"/public/login";
					}else{
						alert(data);
					}
				},"text");
		
		}
	</script>
</body>
</html>
