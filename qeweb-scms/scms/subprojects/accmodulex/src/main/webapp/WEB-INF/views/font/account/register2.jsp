<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title><!-- 用户注册 --><spring:message code="vendor.font.UserRegister"/></title>
	<link href="${ctx}/static/styles/register.css" type="text/css" rel="stylesheet" />
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#loginName").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					loginName: {
						remote: "${ctx}/public/register/checkLoginName"
					}
					,orgName : {
						remote: "${ctx}/public/register/checkOrgName"
					}
					,orgEmail : {
						remote: "${ctx}/public/register/checkOrgEmail"
					}
					
				},
				messages: {
					loginName: {
						remote: '<spring:message code="vendor.font.TheUserLoginNameHasBeenRegistered"/>'/* 用户登录名已注册 */
					}
					,orgName : {
						remote:'<spring:message code="vendor.font.TheNameOfTheOrganizationHasBeenRegistered"/>'/* 该组织名称已注册 */
					}
					,orgEmail : {
						remote:'<spring:message code="vendor.font.TheEmailHasBeenRegistered"/>'/* 该email已注册 */
					}
				}
			});
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/public/register" method="post" class="form-horizontal">
		<fieldset>
			<legend><small><spring:message code="vendor.font.UserRegister"/><!-- 用户注册 --></small><small>  <spring:message code="vendor.font.StepsToFillInTheInstructions"/><!-- 填写说明 必须步骤 -->：</small><small><strong class="text-warning">①<spring:message code="vendor.font.RegistrationInformation"/><!-- 注册信息 --></strong></small><small>->②<spring:message code="vendor.font.EssentialInformation"/><!-- 基本信息 --></small></legend>
			<div class="control-group">
				<label for="loginName" class="control-label"><spring:message code="vendor.font.LoginName"/><!-- 登录名 -->:</label>
				<div class="controls">
					<input type="text" id="loginName" name="loginName" class="input-large required" minlength="3"/>
				</div>
			</div>
			<div class="control-group">
				<label for="name" class="control-label"><spring:message code="vendor.font.UserName"/><!-- 用户名 -->:</label>
				<div class="controls">
					<input type="text" id="name" name="name" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="password" class="control-label"><spring:message code="vendor.font.PassWorld"/><!-- 密码 -->:</label>
				<div class="controls">
					<input type="password" id="password" name="password" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="confirmPassword" class="control-label"><spring:message code="vendor.font.ConfirmationPassword"/><!-- 确认密码 -->:</label>
				<div class="controls">
					<input type="password" id="confirmPassword" name="confirmPassword" class="input-large required" equalTo="#password"/>
				</div>
			</div>
			<div class="control-group">
				<label for="orgName" class="control-label"><spring:message code="vendor.font.EnterpriseName"/><!-- 企业全称 -->:</label>
				<div class="controls">
					<input type="text" id="orgName" name="orgName" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="orgPhone" class="control-label"><spring:message code="vendor.font.ContactNumber"/><!-- 联系电话 -->:</label>
				<div class="controls">
					<input type="text" id="orgPhone" name="orgPhone" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="orgEmail" class="control-label">Email:</label>
				<div class="controls">
					<input type="text" id="orgEmail" name="orgEmail" class="input-large required email"/>
				</div>
			</div>
			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;	
				<input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
			</div>
		</fieldset>
	</form>
</body>
</html>