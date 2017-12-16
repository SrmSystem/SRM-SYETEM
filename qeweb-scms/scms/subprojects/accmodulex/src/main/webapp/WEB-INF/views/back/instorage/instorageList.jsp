<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
		data-options="url:'${ctx}/manager/account/instorage/${vendor}',method:'post',singleSelect:false,     
		fit:true,border:false,toolbar:'#accsettingListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>   
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'inStorageCode',formatter:function(v,r,i){return r.inStorage.inStorageCode;}">单号</th>
		<th data-options="field:'receiveCode',formatter:function(v,r,i){return r.receiveItem.receive.receiveCode}">收货单号</th>
		<th data-options="field:'itemNo'">行号</th>
		<th data-options="field:'unitName'">单位</th>
		<th data-options="field:'price'">单价</th>
		<th data-options="field:'createTime'">单据生成时间</th>
		<th data-options="field:'deliveryCode'">ASN发货单号</th>
		<th data-options="field:'requestTime'">要求到货时间</th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.inStorage.vendor.name;}">供应商名称</th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.inStorage.vendor.code;}">供应商编码</th>
		<th data-options="field:'inStorageQty'">数量</th>
		<!-- <th data-options="field:'inAccountQty'">已结数量</th> -->
		<th data-options="field:'inStorageType',formatter:inStorageTypeFmt">类型</th>
		<th data-options="field:'receiveOrg'">收货方</th>
		<th data-options="field:'buyer',formatter:function(v,r,i){return r.inStorage.buyer.name;}">采购商名称</th>
		<th data-options="field:'poNumber',formatter:function(v,r,i){return r.inStorage.poNumber;}">采购订单号</th>
		<!-- <th data-options="field:'createUserName'">创建人</th> -->
		<th data-options="field:'inStorageTime'">同步时间</th>
		<th data-options="field:'checkedStatus',formatter:function(v,r,i){if(v==1) return '已结算';else return '未结算';}">是否已结算</th>
		</tr></thead>
	</table>
	<div id="accsettingListToolbar" style="padding:5px;">
		<div><c:if test="${vendor == true }">
		<shiro:hasPermission name="vendor:instorage:toAddBillList"> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="toAddBillList()">开票</a>
		</shiro:hasPermission>
		<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycInstorage()">同步入库单</a> -->
		</c:if></div>
		<div>
			<form id="form-instorage-search" method="post">
			单号：<input type="text" name="search-LIKE_inStorage.inStorageCode" class="easyui-textbox" style="width:80px;"/>
			物料编码：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>
			类型：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_inStorage.inStorageType"><option value="">-全部-</option><option value="0">入库单</option><option value="1">退库单</option></select> 
			时间： <input type="text" name="search-GT_inStorageTime" data-options="editable:false" class="easyui-datebox" >~  
					  <input type="text" name="search-LT_inStorageTime" data-options="editable:false" class="easyui-datebox" >
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchAccountSetting()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-instorage-search').form('reset')">重置</a>
			</form>
		</div>
	</div>  

	<!-- 创建开票清单 --> 
	<div id="win-instorage-add" class="easyui-window" title="创建开票清单" style="width:850px;height:500px"
	data-options="iconCls:'icon-add',modal:true,closed:true,onClose:function(){
                window.location.reload()}">
		<div class="easyui-panel" data-options="fit:true">   
			<div>
			<shiro:hasPermission name="vendor:instorage:saveInvoince"> 
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveInvoince(0)">保存</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="vendor:instorage:toAddBillList"> 
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="saveInvoince(1)">开票</a>
			</shiro:hasPermission>
			</div>   
			<form id="form-bill-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
					<tr>
						<td>清单号:</td><td><input class="easyui-textbox" id="billCode" name="billCode" type="text"  readonly="readonly" data-options="required:true"/></td>
						<td>合计:</td><td><input class="easyui-textbox" id="totalPrice" name="totalPrice" type="text" data-options="required:true,events:{blur:function(){calcTotalPrice()}}"/></td> 
					</tr>   
					<tr>
						<td>税率:</td><td><input class="easyui-textbox" id="tax" name="tax" type="text" data-options="required:true,events:{blur:function(){calcTotalPrice()}}" /></td>
						<td>含税合计:</td><td><input class="easyui-textbox" id="totalTaxPrice" name="totalTaxPrice" type="text" data-options="required:true"/></td>
					</tr>
				</table>   
			</form>
			<table id="datagrid-billit-list" title="入库单详情" class="easyui-datagrid",     
				data-options="
				iconCls: 'icon-edit',
				singleSelect: true,
				method:'post',
				onClickCell: CellEditor.onClickCell" >
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'inStorageCode',formatter:function(v,r,i){return r.inStorage.inStorageCode;}">入库单号</th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
				<th data-options="field:'inStorageQty'">入库数量</th>
				<!-- <th data-options="field:'inAccountQty'">已结数量</th> -->
				<th data-options="field:'accountQty',editor:{type:'numberbox',options:{required:true,events:{blur:function(){calcPrice(1)}}}}">结算数量</th>
				<th data-options="field:'price',editor:{type:'numberbox',options:{required:true,precision:2,events:{blur:function(){calcPrice(2)}}}}">单价</th>
				<th data-options="field:'receiveOrg'">收货方</th>
				<th data-options="field:'createUserName'">创建人</th>
				<th data-options="field:'inStorageTime'">入库时间</th>        
				</tr></thead>
			</table>  
			<table id="datagrid-invoice-list" title="发票详情" class="easyui-datagrid", 
				data-options="
				iconCls: 'icon-edit',
				singleSelect: true,
				toolbar:'#tb',
				method:'post',
				onClickCell: RowEditor.onClickCell" >
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'invoiceCode',width:'160',editor:'text',required:true">发票号</th>
				<th data-options="field:'invoiceMoney',width:'160',editor:{type:'numberbox',options:{precision:2}},required:true">发票金额</th>
				<th data-options="field:'invoiceTime',width:160,editor:'datetimebox',required:true">发票时间</th>  
				</tr></thead>
			</table>
			<div id="tb" style="padding:5px;height:auto">
				<!-- <div style="margin-bottom:5px">
					<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="RowEditor.append('datagrid-invoice-list')"></a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="RowEditor.removeit('datagrid-invoice-list')"></a> 
				</div> -->
			</div>
		</div>
	</div>
	  
<script type="text/javascript">

</script>   
</body>
</html>
