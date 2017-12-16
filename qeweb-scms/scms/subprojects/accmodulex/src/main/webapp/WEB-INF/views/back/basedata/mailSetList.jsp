<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>

<head>
	<title><spring:message code="purchase.basedata.MailboxSettings"/><!-- 邮箱设置 --></title>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/mailSetList.js"></script>
</head>

<body style="margin:0;padding:0;">
<table id="datagrid-mailSet-list" title='<spring:message code="purchase.basedata.MailboxSettingsList"/><!-- 邮箱设置 列表-->' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/mailSet',method:'post',singleSelect:false,
		toolbar:'#mailSetListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
		<thead>
		<tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'mailTemplateId',formatter:mailSetDetailFmt"><spring:message code="purchase.basedata.MailboxTemplateName"/> <!-- 邮箱模板名称 --></th>
		<th data-options="field:'mailAddress'"><spring:message code="purchase.basedata.MailboxAdress"/><!-- 邮箱地址 --></th>
		<th data-options="field:'serverAddress'"><spring:message code="purchase.basedata.SendServerAddress"/><!-- 发送服务器地址 --></th>
		<th data-options="field:'account'"><spring:message code="purchase.basedata.AccountNumber"/><!-- 账号 --></th>
		<th data-options="field:'password'"><spring:message code="purchase.basedata.PassWords"/><!-- 密码 --></th>
		<th data-options="field:'abolished',formatter:mailAbolishedFmt"><spring:message code="purchase.basedata.State"/><!-- 状态 --></th>
		<th data-options="field:'manager',formatter:managerFmt"><spring:message code="purchase.basedata.Administration"/><!-- 管理 --></th>
		</tr>
		</thead>
	</table>
		<div id="mailSetListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addMailSet()"><spring:message code="vendor.new"/><!-- 新增 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="abolishMailSet()"><spring:message code="purchase.basedata.ToVoid"/><!-- 作废 --></a>
		</div>
		<div>
			<form id="form-mailSet-search" method="post">
			<spring:message code="purchase.basedata.MailboxTemplateName"/> <!-- 邮箱模板名称 -->：<select class="easyui-combobox" name="search-EQ_mailTemplateId" data-options="editable:false" style="width:120px">
			<option value="">---<spring:message code="vendor.all"/><!-- 全部 -->---</option><option value="1"><spring:message code="purchase.basedata.SelfRegistrationSuccess"/><!-- 自主注册成功 --></option><option value="6"><spring:message code="purchase.basedata.InvitationRegistrationTemplate"/><!-- 邀请注册模板 --></option>
			<option value="2"><spring:message code="purchase.basedata.InviteRegistrationSuccess"/><!-- 邀请注册成功 --></option><option value="3"><spring:message code="purchase.basedata.IntentionConfirmation"/><!-- 意向确认通过 --></option><option value="4"><spring:message code="purchase.basedata.IntentionConfirmRefuse"/><!-- 意向确认拒绝 --></option>
			<option value="5"><spring:message code="purchase.basedata.QualificationReminder"/><!-- 资质提醒 --></option></select>
			<spring:message code="purchase.basedata.State"/><!-- 状态 -->：<select class="easyui-combobox" name="search-EQ_abolished" data-options="editable:false" style="width:120px">
			<option value="">---<spring:message code="vendor.all"/><!-- 全部 -->---</option><option value="0"><spring:message code="purchase.basedata.Effective"/><!-- 有效 --></option><option value="1"><spring:message code="purchase.basedata.ToVoid"/><!-- 作废 --></option></select>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchMailSet()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-mailSet-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
</div>
	<div id="win-mailSet-addoredit" class="easyui-dialog" title='<spring:message code="purchase.basedata.NewAddMailboxSettings"/><!-- 新增邮箱设置 -->' style="width:640px;height:300px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-mailSet-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="purchase.basedata.TemplateName"/><!-- 模板名称 -->:</td>
					<td>
					<select class="easyui-combobox" style="width:180px" name="mailTemplateId" data-options="required:true,editable:false">
			        <option value="1"><spring:message code="purchase.basedata.SelfRegistrationSuccess"/><!-- 自主注册成功 --></option><option value="6"><spring:message code="purchase.basedata.InvitationRegistrationTemplate"/><!-- 邀请注册模板 --></option>
			        <option value="2"><spring:message code="purchase.basedata.InviteRegistrationSuccess"/><!-- 邀请注册成功 --></option><option value="3"><spring:message code="purchase.basedata.IntentionConfirmation"/><!-- 意向确认通过 --></option>
			        <option value="4"><spring:message code="purchase.basedata.IntentionConfirmRefuse"/><!-- 意向确认拒绝 --></option><option value="5"><spring:message code="purchase.basedata.QualificationReminder"/><!-- 资质提醒 --></option>
			        </select>
			        </td>
					<td><spring:message code="purchase.basedata.MailboxAdress"/><!-- 邮箱地址 -->:</td><td><input class="easyui-textbox" style="width:180px" name="mailAddress" type="text"
						data-options="required:true"/></td>
				</tr>
				<tr>
					<td><spring:message code="purchase.basedata.SendServerAddress"/><!-- 发送服务器地址 -->:</td><td><input class="easyui-textbox" style="width:180px" name="serverAddress" type="text"
						data-options="required:true"/></td>
					<td><spring:message code="purchase.basedata.AccountNumber"/><!-- 账号 -->:</td><td><input class="easyui-textbox" style="width:180px" name="account" type="text" 
					data-options="required:true"/></td>
				</tr>
				<tr>
					<td><spring:message code="purchase.basedata.PassWords"/><!-- 密码 -->:</td><td><input class="easyui-textbox" style="width:180px" name="password" type="text" 
					data-options="required:true"/></td>
				</tr>
				<tr>
					<td><spring:message code="purchase.basedata.MailContent"/><!-- 邮件内容 -->:</td><td><input class="easyui-textbox" name="mailContent" type="text" 
					data-options="multiline:true" style="width:180px; height:120px"/></td>

					<td><spring:message code="purchase.basedata.Sign"/><!-- 签名 -->:</td><td><input class="easyui-textbox" name="signature" type="text" 
					data-options="multiline:true" style="width:180px; height:120px"/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditmailSet()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-mailSet-addoredit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
		<div id="win-mailSet-view" class="easyui-window" title='<spring:message code="purchase.basedata.LookMailboxSettings"/><!-- 查看邮箱设置 -->' style="width:640px;height:260px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-mailSet-view" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="purchase.basedata.TemplateName"/><!-- 模板名称 -->:</td>
					<td>
					<select class="easyui-combobox" style="width:155px" name="mailTemplateId" data-options="required:true" disabled="disabled">
			        <option value="1"><spring:message code="purchase.basedata.SelfRegistrationSuccess"/><!-- 自主注册成功 --></option><option value="6"><spring:message code="purchase.basedata.InvitationRegistrationTemplate"/><!-- 邀请注册模板 --></option>
			        <option value="2"><spring:message code="purchase.basedata.InviteRegistrationSuccess"/><!-- 邀请注册成功 --></option><option value="3"><spring:message code="purchase.basedata.IntentionConfirmation"/><!-- 意向确认通过 --></option>
			        <option value="4"><spring:message code="purchase.basedata.IntentionConfirmRefuse"/><!-- 意向确认拒绝 --></option><option value="5"><spring:message code="purchase.basedata.QualificationReminder"/><!-- 资质提醒 --></option>
			        </select>
			        </td>
					<td><spring:message code="purchase.basedata.MailboxAdress"/><!-- 邮箱地址 -->:</td><td><input class="easyui-textbox" name="mailAddress" type="text"
						data-options="required:true"/></td>
				</tr>
				<tr>
					<td><spring:message code="purchase.basedata.SendServerAddress"/><!-- 发送服务器地址 -->:</td><td><input class="easyui-textbox" name="serverAddress" type="text"
						data-options="required:true"/></td>
					<td><spring:message code="purchase.basedata.AccountNumber"/><!-- 账号 -->:</td><td><input class="easyui-textbox" name="account" type="text" 
					data-options="required:true"/></td>
				</tr>
				<tr>
					<td><spring:message code="purchase.basedata.PassWords"/><!-- 密码 -->:</td><td><input class="easyui-textbox" name="password" type="text" 
					data-options="required:true"/></td>
				</tr>
				<tr>
					<td><spring:message code="purchase.basedata.MailContent"/><!-- 邮件内容 -->:</td><td><input class="easyui-textbox" name="mailContent" type="text" 
					data-options="multiline:true" style="height:80px"/></td>

					<td><spring:message code="purchase.basedata.Sign"/><!-- 签名 -->:</td><td><input class="easyui-textbox" name="signature" type="text" 
					data-options="multiline:true" style="height:80px"/></td>
				</tr>
				</table>
			</form>
		</div>
	</div>

</body>
</html>