<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>供货品牌重合度统计</title>
</head>

<body style="margin:0;padding:0;">

	<table id="datagrid-materialSupplyRel-list"  class="easyui-datagrid"
		data-options="
		fit:true,
		url:'${ctx}/manager/vendor/brandOverlap/statiVendorBrandOverlapList',method:'post',singleSelect:false,
		toolbar:'#materialSupplyRelListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
			<thead><tr>
			<th data-options="field:'id',checkbox:true"></th>
            <th data-options="field:'materialRelId',hidden:true"></th>
			<th data-options="field:'vendorName'">供应商名称</th>
			<th data-options="field:'allBrand'">供货品牌</th>
			<th data-options="field:'brandCount'">供货品牌数量</th>
			</tr></thead>
	</table>
	<div id="materialSupplyRelListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-download',plain:true" onclick="exp()">导出</a>
		<div>
			<form id="form-materialRel-search" method="post">
			供应商名称：<input type="text" name="search-LIKE_vendorName" class="easyui-textbox" style="width:80px;"/>
			供货品牌：<input type="text" name="search-LIKE_materialName" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="query()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialRel-search').form('reset')">重置</a>
			</form>
		</div>
		</div>
	</div>
<script type="text/javascript">
function exp(){
	//导出
	$('#form-materialRel-search').form('submit',{
		url:'${ctx}/manager/vendor/brandOverlap/exportExcel',
		success:function(data){
			$.messager.progress('close');
		}
	});
}
function query() {
	var searchParamArray = $('#form-materialRel-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-materialSupplyRel-list').datagrid('load', searchParams);
}
</script>
</body>
</html>