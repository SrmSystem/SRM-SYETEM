<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>产品线管理</title>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/productLineList.js">
	</script>
</head>

<body style="margin:0;padding:0;">
<div>
	<table id="datagrid-productLine-list" title="产品线列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/basedata/productLine',method:'post',singleSelect:false,
		toolbar:'#productLineListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'">产品线编号</th>
		<th data-options="field:'name'">产品线名称</th>
		<th data-options="field:'manager',formatter:managerFmt">管理</th>
		</tr></thead>
	</table>
	<div id="productLineListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addProductLine()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteProductLine()">删除</a>
		</div>
		<div>
			<form id="form-productLine-search" method="post">
			产品线编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			产品线名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchProductLine()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-productLine-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-productLine-addoredit" class="easyui-window" title="新增产品线" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-productLine-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>产品线编号:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>产品线名称:</td><td><input class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditproductLine()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-productLine-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>