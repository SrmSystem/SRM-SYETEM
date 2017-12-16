<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title><spring:message code="vendor.common.warningAlert"/><!-- 预警提醒 --></title>
<script type="text/javascript" src="${ctx}/static/script/contract/dialog.js"></script>

<script type="text/javascript" src="${ctx}/static/script/basedata/warn.js"></script>


</head>
<body>
		<table id="datagrid-warn-list" class="easyui-datagrid"
				data-options="url:'${ctx}/manager/common/warning',method:'post',singleSelect:false,
				fit:true,border:false,toolbar:'#warningListToolbar',
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead>
				<tr>
				<th data-options="field:'operate',formatter:operateFmt,width:'150'"><spring:message code="vendor.operation"/><!-- 操作 --></th>
				<th data-options="field:'moduleName' ,width:'100'"><spring:message code="vendor.common.nameModule"/><!-- 模块名称 --></th>
				<th data-options="field:'name'"><spring:message code="vendor.common.remindName"/><!-- 提醒名称 --></th>
				<th data-options="field:'isVendor',formatter:isVendorFmt,width:'100'"><spring:message code="vendor.common.characterName"/><!-- 角色名称 --></th>
				<th data-options="field:'warnTime' ,width:'100'"><spring:message code="vendor.common.processingTimeHour"/><!-- 处理时长（小时） --></th>
				<!-- <th data-options="field:'content' ,width:'100'">提醒内容</th> -->
				<th data-options="field:'isWarning' , formatter:isWarningFmt,width:'100'"><spring:message code="vendor.common.whetherStartWarning"/><!-- 是否开启预警 --></th>
				<!-- <th data-options="field:'warnContent',width:'100'">预警内容</th> -->
				<th data-options="field:'enableStatus',formatter:enableStatusFmt,width:'100'"><spring:message code="vendor.common.effectiveState"/><!-- 生效状态 --></th>
				<th data-options="field:'isMail',formatter:isMailFmt,width:'100'"><spring:message code="vendor.common.whetherSendEmail"/><!-- 是否发送邮件 --></th>
				
				</tr>
				</thead>
		</table>
		
		
		<div id="warningListToolbar" style="padding:5px;">
		<div>
			<form id="form-warn-search" method="post">
			<spring:message code="vendor.common.nameModule"/><!-- 模块名称 -->：<input type="text" name="search-LIKE_moduleName" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.common.remindName"/><!-- 提醒名称 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.common.effectiveState"/><!-- 生效状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_enableStatus">
			<option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option>
			<option value="0"><spring:message code="vendor.lapse"/><!-- 失效 --></option>
			<option value="1"><spring:message code="vendor.takeEffect"/><!-- 生效 --></option>
		   </select>
		   
		   <div>
				<table style="width: 100%">
					<tr align="right">
						<td>
		   <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchWarn()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-warn-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			 
		</form>
		</div>
		</div>
		
		
		<div id="win-warn-edit" class="easyui-dialog" title="编辑预警信息" style="width:540px;height:500px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-warn-edit" method="post" >
				<input name="id" type="hidden" />
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<input name="moduleName" type="hidden"/>
				<input name="code" type="hidden"/>
				<input name="enableStatus" type="hidden"/>
				<input name="abolished" type="hidden"/>
				<tr>
					<td><spring:message code="vendor.common.remindName"/><!-- 提醒名称 -->:</td><td><input class="easyui-textbox"  style="width:180px" id="name" name="name" type="text"
						readOnly="true"/></td>
					</tr>
				<tr>
					<td><spring:message code="vendor.common.role"/><!-- 角色 -->:</td><td>
					<input type="hidden" style="width:180px"  name="isVendor" type="text"
						 readOnly="true" />
						 <input class="easyui-textbox"  id="isVendorName"  style="width:180px"  name="isVendorName" type="text"
						 readOnly="true" /></td>
					</tr>
					<tr>
					<td><spring:message code="vendor.common.whetherSendEmail"/><!-- 是否发送邮件 -->:</td>
					<td>
					<select class="easyui-combobox" style="width:180px"  id="isMail"   name="isMail" data-options="required:true">
			        <option value="0"><spring:message code="vendor.no"/><!-- 否 --></option>
			        <option value="1"><spring:message code="vendor.yes"/><!-- 是 --></option>
			        
			        </select>
			        </td>
			        </tr>
			        <tr>
						<td><spring:message code="vendor.common.processingTimeHour"/><!-- 处理时长 -->:</td><td>
						 <input class="easyui-textbox"  id="warnTime"  style="width:180px"  name="warnTime" type="text"/></td>
					</tr>
			        
				<tr>
					<td><spring:message code="vendor.common.remindContent"/><!-- 提醒内容 -->:</td><td><textarea id="content"   class="textarea easyui-validatebox"  style="width:180px;height:120px" name="content" type="text"
						data-options="required:true" > </textarea> </td>
					</tr>
					
				<tr>
					<td><spring:message code="vendor.common.whetherStartWarning"/><!-- 是否开启预警 -->:</td>
					<td>
					<select class="easyui-combobox" style="width:180px"  id="isWarning"   name="isWarning" data-options="required:true">
			        <option value="0"><spring:message code="vendor.no"/><!-- 否 --></option>
			        <option value="1" ><spring:message code="vendor.yes"/><!-- 是 --></option>
			        </select>
			        </td>
				</tr>

				<tr id="show">
					<td><spring:message code="vendor.common.warningContent"/><!-- 预警内容 -->:</td><td><textarea id="warnContent"  class="textarea easyui-validatebox"  style="width:180px;height:120px" name="warnContent" type="text"
						></textarea></td>
					</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitEditWarnInfo()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="resetInfo()"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
		
		
		
</body>


</html>