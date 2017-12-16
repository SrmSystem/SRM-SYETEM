<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>用户注册</title>
	<link href="${ctx}/static/styles/register.css" type="text/css" rel="stylesheet" />
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#shortName").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();
		});
	</script>
</head>

<body>
	<form id="inputForm" action="${ctx}/public/registerVendorBase" method="post" class="form-horizontal">
		<input type="hidden" id="name" name="${vendor.name}"/>
		<fieldset>
			<legend><small>用户注册</small><small>  填写说明 必须步骤：</small><small>①注册信息</small><small>-><strong class="text-warning">②基本信息</strong></small></legend>
			<div class="control-group">
				<label for="shortName" class="control-label">企业简称:</label>
				<div class="controls">
					<input type="text" id="shortName" name="shortName" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="property" class="control-label">企业性质:</label>
				<div class="controls">
					<input type="text" id="property" name="property" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="country" class="control-label">国家:</label>
				<div class="controls">
					<input type="text" id="country" name="country" class="input-large required"/>
				</div>
			</div>
			
			<div class="control-group">
				<label for="regtime" class="control-label">成立时间:</label>
				<div class="controls">
					<input type="text" id="regtime" name="regtime" class="input-large required"/>
				</div>
			</div>
			<div class="control-group">
				<label for="legalPerson" class="control-label">企业法人:</label>
				<div class="controls">
					<input type="text" id="legalPerson" name="legalPerson" class="input-large required email"/>
				</div>
			</div>
			<div class="control-group">
				<label for="stockShare" class="control-label">股比构成:</label>
				<div class="controls">
					<input type="text" id="stockShare" name="stockShare" class="input-large required email"/>
				</div>
			</div>
			<div class="control-group">
				<label for="stockShare" class="control-label">股比构成:</label>
				<div class="controls">
					<input type="text" id="stockShare" name="stockShare" class="input-large required email"/>
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
