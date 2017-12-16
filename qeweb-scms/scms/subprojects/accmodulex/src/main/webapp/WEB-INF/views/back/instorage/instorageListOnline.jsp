<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>待开票入库单</title>
	<script type="text/javascript" src="${ctx}/static/script/purchase/InStorage.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/Bill.js"></script>
	<script type="text/javascript">
		var vendor = ${vendor};
		$(function(){
			
		});
		
		function inStorageTypeFmt(v,r,i){
			if(r.inStorage.inStorageType == 0)
				return '入库单';
			else if (r.inStorage.inStorageType == 1)
				return '退库单';
			
			return '入库单';
		}
	</script>
</head>

<body style="margin:0;padding:0;">

	<table id="datagrid-instorage-list" title="待开票入库单列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/account/instorage/onlineList/${vendor}',method:'post',singleSelect:false,     
		fit:true,border:false,toolbar:'#accsettingListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>   
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'inStorageCode',formatter:function(v,r,i){return r.inStorage.inStorageCode;}">单号</th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.inStorage.vendor.code;}">供应商编码</th>
		<th data-options="field:'inStorageQty'">数量</th>
		<!-- <th data-options="field:'inAccountQty'">已结数量</th> -->
		<th data-options="field:'inStorageType',formatter:inStorageTypeFmt">类型</th>
		<th data-options="field:'receiveOrg'">收货方</th>
		<th data-options="field:'buyer',formatter:function(v,r,i){return r.inStorage.buyer.name;}">采购商名称</th>
		<th data-options="field:'poNumber',formatter:function(v,r,i){return r.inStorage.poNumber;}">采购订单号</th>
		<!-- <th data-options="field:'createUserName'">创建人</th> -->
		<th data-options="field:'inStorageTime'">时间</th>
		</tr></thead>
	</table>
	<div id="accsettingListToolbar" style="padding:5px;">
			<form id="form-instorage-search" method="post">
			单号：<input type="text" name="search-LIKE_inStorage.inStorageCode" class="easyui-textbox" style="width:80px;"/>
			物料编码：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>
			类型：<select class="easyui-combobox" name="search-EQ_inStorage.inStorageType"><option value="">-全部-</option><option value="0">入库单</option><option value="1">退库单</option></select> 
			时间： <input type="text" name="search-GT_inStorageTime" class="easyui-datebox" >~  
					  <input type="text" name="search-LT_inStorageTime" class="easyui-datebox" >
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchAccountSetting()">查询</a>
			</form>
		</div>
	</div>  

<script type="text/javascript">

</script>   
</body>
</html>
