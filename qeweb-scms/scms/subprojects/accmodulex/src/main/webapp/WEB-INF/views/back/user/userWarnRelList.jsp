<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.user.promotionReminderSettings"/><!-- 晋级提醒设置 --></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-userWarn-list" title="晋级提醒人员列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/admin/user/getUserWarnRelList/${userId}',method:'post',singleSelect:false,
		toolbar:'#userWarnListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'role'  ,formatter:function(v,r,i){return r.role.name;}  " ><spring:message code="vendor.characterName"/><!-- 角色名称 --></th>
		<th data-options="field:'userRole' ,formatter:function(v,r,i){return r.roleUser.name;}   "><spring:message code="vendor.TheuserName"/><!-- 用户名称 --></th>
		</tr></thead>
	</table>
	<div id="userWarnListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addUserWarn(${userId})"><spring:message code="vendor.new"/><!-- 新增 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="delUserWarn()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
		</div>
		<div>
			<form id="form-userWarnItem-search" method="post">
			<spring:message code="vendor.characterName"/><!-- 角色名称 -->：<input type="text" name="search-LIKE_role.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.TheuserName"/><!-- 用户名称 -->：<input type="text" name="search-LIKE_roleUser.name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchUserWarn()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-userWarnItem-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	
	
	<!-- 新增 -->
	<div id="win-userWarn-addoredit" class="easyui-window" title="添加信息" style="width:400px;height:250px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="userWarn_form" method="post">
			     <input id="userId" name="userId" value="${userId}" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="vendor.role"/><!-- 角色 -->:</td><td><input class="easyui-combobox" id="roleId" name="roleId" 
					    data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td><spring:message code="vendor.users"/><!-- 用户 -->:</td><td><input class="easyui-combobox" id="roleUserId" name="roleUserId" 
						data-options="required:true"/></td>
				</tr>
				</table>
			</form>
			 <div align="center">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitUserWarn()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#userWarn_form').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</div>
	  </div>
	</div>  
	  
</body>
</html>
