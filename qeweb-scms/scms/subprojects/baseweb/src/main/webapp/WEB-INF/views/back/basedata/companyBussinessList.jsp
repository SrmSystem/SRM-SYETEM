<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>公司业务管理</title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript">
		function managerFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editCompanyBussiness('+r.id+');">编辑</a>';
		}
		
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/companyBussinessList.js"></script>
</head>

<body style="margin:0;padding:0;">
<div>
	<table id="datagrid-companyBussiness-list" title="公司业务列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/companyBussiness',method:'post',singleSelect:false,
		toolbar:'#companyBussinessListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'">公司业务编号</th>
		<th data-options="field:'name'">公司业务名称</th>
		<th data-options="field:'manager',formatter:managerFmt">管理</th>
		</tr></thead>
	</table>
	<div id="companyBussinessListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addCompanyBussiness()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteCompanyBussiness()">删除</a>
		</div>
		<div>
			<form id="form-companyBussiness-search" method="post">
			公司业务编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			公司业务名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchCompanyBussiness()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-companyBussiness-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-companyBussiness-addoredit" class="easyui-window" title="新增公司业务" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-companyBussiness-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>公司业务编号:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>公司业务名称:</td><td><input class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditcompanyBussiness()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-companyBussiness-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
</div>


</body>
</html>
