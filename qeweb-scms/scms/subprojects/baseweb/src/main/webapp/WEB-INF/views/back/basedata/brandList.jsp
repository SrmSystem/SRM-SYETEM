<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>品牌管理</title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript">
		function managerFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editBrand('+r.id+');">编辑</a>';
		}
		
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/brandList.js"></script>
</head>

<body style="margin:0;padding:0;">
<div>
	<table id="datagrid-brand-list" title="品牌列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/basedata/brand',method:'post',singleSelect:false,
		toolbar:'#brandListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'">品牌编号</th>
		<th data-options="field:'name'">品牌名称</th>
		<th data-options="field:'manager',formatter:managerFmt">管理</th>
		</tr></thead>
	</table>
	<div id="brandListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addBrand()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteBrand()">删除</a>
		</div>
		<div>
			<form id="form-brand-search" method="post">
			品牌编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			品牌名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchBrand()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-brand-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-brand-addoredit" class="easyui-window" title="新增品牌" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-brand-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>品牌编号:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>品牌名称:</td><td><input class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditbrand()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="resetForm()">重置</a>
				</div>
			</form>
		</div>
	</div>
</div>


</body>
</html>
