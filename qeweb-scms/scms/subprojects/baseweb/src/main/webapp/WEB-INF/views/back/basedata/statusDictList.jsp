<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>状态管理</title>
	<script type="text/javascript" src="${ctx}/static/script/statusDict.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/iconSelect.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-statusDict-list" title="状态列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/database/statusDict',method:'post',singleSelect:false,
		toolbar:'#statusDictListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'operate',formatter:StatusDict.operateFmt">操作</th>
		<th data-options="field:'statusName'">状态名称</th>
		<th data-options="field:'statusType'">状态编号</th>
		<th data-options="field:'statusValue'">状态值</th>
		<th data-options="field:'statusText'">状态文本</th>
		<th data-options="field:'statusIcon',formatter:StatusDict.iconFmt">状态图标</th>
		</tr></thead>
	</table>
	<div id="statusDictListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="StatusDict.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="StatusDict.del()">删除</a>
		</div>
		<div>
			<form id="form-statusDict-search" method="post">
			状态编号：<input type="text" name="search-LIKE_statusType" class="easyui-textbox" style="width:80px;"/>
			状态名称：<input type="text" name="search-LIKE_statusName" class="easyui-textbox" style="width:80px;"/>
			状态文本：<input type="text" name="search-LIKE_statusText" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="StatusDict.search()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-statusDict-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-statusDict-addoredit" class="easyui-window" title="新增状态" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-statusDict-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>状态编号:</td><td><input class="easyui-textbox" name="statusType" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>状态名称:</td><td><input class="easyui-textbox" name="statusName" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>状态文本:</td><td><input class="easyui-textbox" name="statusText" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>状态值:</td><td><input class="easyui-numberbox" name="statusValue"
						data-options="required:true,editor:'number'"
					/></td>
				</tr>
				<tr>
					<td>状态图标:</td><td><input id="statusIcon" name="statusIcon" type="hidden"/>
					<label id="statusIcon-text"></label>
					<a class="easyui-linkbutton" onclick="Iconer.open('statusIcon')">选择</a></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="StatusDict.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-statusDict-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
