<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>供应商阶段管理</title>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorPhase.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-vendorPhase-list" title="供应商阶段列表" class="easyui-datagrid"
		data-options="
		fit:true,
		url:'${ctx}/manager/vendor/vendorPhase',method:'post',singleSelect:false,
		toolbar:'#vendorPhaseListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'">供应商阶段编号</th>
		<th data-options="field:'name'">供应商阶段名称</th>
		<th data-options="field:'roleName'">指定角色</th>
		<th data-options="field:'manager',formatter:VendorPhase.managerFmt">管理</th>
		</tr></thead>
	</table>
	<div id="vendorPhaseListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="VendorPhase.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="VendorPhase.del()">删除</a>
		</div>
		<div>
			<form id="form-vendorPhase-search" method="post">
			供应商阶段编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			供应商阶段名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="VendorPhase.search()">查询</a>
			</form>
		</div>
	</div>
	<div id="win-vendorPhase-addoredit" class="easyui-window" title="新增供应商阶段" style="width:400px;height:230px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-vendorPhase-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>供应商阶段编号:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>供应商阶段名称:</td><td><input class="easyui-textbox" id="name" name="name" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>指定角色:</td><td><input class="easyui-combobox" name="roleId" 
						data-options="
						url:'${ctx}/manager/admin/role/getRoleList',
						valueField : 'id',
						textField : 'name',
						required:true"
					/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="VendorPhase.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-vendorPhase-add').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
