<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>WMS库存管理</title>
	<script type="text/javascript">
	
	</script>
</head>

<body style="margin:0;padding:0;">

	<table id="datagrid-inventory-list" title="WMS库存列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/inventory/wms',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#inventoryListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>   
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'materialCode',width:100,formatter:function(v,r,i){return r.material.code;}">物料编码</th>
		<th data-options="field:'materialName',width:100,formatter:function(v,r,i){return r.material.name;}">物料名称</th>
		<th data-options="field:'stockQty',width:100">库存</th>  
		</tr></thead>
	</table>
	<div id="inventoryListToolbar" style="padding:5px;"> 
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-download',plain:true" onclick="exportInventory()">导出WMS库存</a>
		</div>
		<div>
			<form id="form-inventory-search" method="post">
			物料编码：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>   
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchInventory()">查询</a>
			</form>
		</div>
	</div>  

<script type="text/javascript">
function searchInventory(){
	var searchParamArray = $('#form-inventory-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-inventory-list').datagrid('load',searchParams);
}

//导出WMS库存
function exportInventory() {
	$('#form-inventory-search').form('submit',{  
		url: '${ctx}/manager/inventory/wms/exportExcel',
		success:function(data){
			$.messager.progress('close');
		}
	});
}

</script>   
</body>
</html>
