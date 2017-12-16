<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>发货看板</title>
	<script type="text/javascript">
	
	var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($('#datagrid-delivery-list').datagrid('validateRow', editIndex)){
			$('#datagrid-delivery-list').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickCell(index, field){
		if (endEditing()){
			$('#datagrid-delivery-list').datagrid('selectRow', index)
					.datagrid('editCell', {index:index,field:field});
			editIndex = index;
		}
	}
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-pending-list" title="发货看板列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/delivery/pending',method:'post',singleSelect:false,
		toolbar:'#pendingListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'orderCode',formatter:function(v,r,i){return r.order.orderCode;}">采购订单号</th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.order.vendor.code;}">供应商编码</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.order.vendor.name;}">供应商名称</th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
		<th data-options="field:'receiveOrg'">收货方</th>
		<th data-options="field:'orderQty'">订购数量</th>
		<th data-options="field:'shouldQty'">应发数量</th>  
		<th data-options="field:'deliveryQty'">已发数量</th>
		<th data-options="field:'receiveQty'">实收数量</th>
		<th data-options="field:'returnQty'">验退数量</th> 
		<th data-options="field:'diffQty'">差异数量</th>
		<th data-options="field:'requestTime'">要求到货时间</th>
		<th data-options="field:'currency'">币种</th>
		<th data-options="field:'unitName'">单位</th>
		
		</tr></thead>
	</table>
	<div id="pendingListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="toAddDelivery()">创建发货单</a>
		</div>
		<div>
			<form id="form-pending-search" method="post">
			采购订单号：<input type="text" name="search-LIKE_order.orderCode" class="easyui-textbox" style="width:80px;"/>
			供应商编码：<input type="text" name="search-LIKE_order.vendor.code" class="easyui-textbox" style="width:80px;"/>
			物料编码：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPending()">查询</a>
			</form>
		</div>
	</div>
	
	<div id="win-delivery-addoredit" class="easyui-window" title="创建发货单" style="width:600px;height:400px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-delivery-addoredit" method="post" >
				<div id="deliveryListToolbar" style="padding:5px;">
					<div>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="saveDelivery(0)">保存</a>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="saveDelivery(1)">保存并发布</a>
					</div>
					<div>
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>发货单号:</td><td><input class="easyui-textbox" id="deliveryCode" name="deliveryCode" type="text" data-options="required:true"/></td>
						</tr>
					</table>
					</div>
				</div>
			</form>
				<table id="datagrid-delivery-list" title="发货单详情" class="easyui-datagrid", 
					data-options="
					iconCls: 'icon-edit',
					singleSelect: true,
					method:'post',
					onClickCell: onClickCell" >
					<thead><tr>
					<th data-options="field:'id',checkbox:true"></th>      
					<th data-options="field:'itemNo'">行号</th>
					<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
					<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
					<th data-options="field:'receiveOrg'">收货方</th>   
					<th data-options="field:'orderQty'">订单数量</th>   
					<th data-options="field:'shouldQty'">应发数量</th>
					<th data-options="field:'sendQty',width:80,align:'right',editor:'numberbox',required:true">发货数量</th>   
					<th data-options="field:'requestTime'">要求到货时间</th>   
					<th data-options="field:'currency'">币种</th>   
					<th data-options="field:'unitName'">单位</th>        
					</tr></thead>
				</table>  
			
		</div>
	</div>
<script type="text/javascript">
function searchPending(){
	var searchParamArray = $('#form-pending-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-pending-list').datagrid('load',searchParams);
}

function toAddDelivery(){
	var selections = $('#datagrid-pending-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var select = new Array();
	var receiveorg = "";
	for(var i = 0; i < selections.length; i ++) {
		select.push(selections[i].id + ",");
		if(receiveorg == "")
			receiveorg = selections[i].receiveOrg;
		else if(receiveorg != selections[i].receiveOrg) {
			$.messager.alert('提示','请选择同一收货方的数据进行发货！！','info');
			return false;
		}
	}
	$('#win-delivery-addoredit').window('open');  
	$('#form-delivery-addoredit').form('load','${ctx}/manager/order/delivery/createDelivery');  
	
	//明细
	$('#datagrid-delivery-list').datagrid({   
    	url:'${ctx}/manager/order/delivery/createDeliveryItem'
	});
	$('#datagrid-delivery-list').datagrid('load',{"search-IN_id":select.join('')});           
} 


//保存发货单
function saveDelivery(type) {  
	$.messager.progress();
 	var rows = $('#datagrid-delivery-list').datagrid('getRows');
	for(i = 0;i < rows.length;i++) {
		 if(rows[i].sendQty == null || rows[i].sendQty == '') {
			$.messager.progress('close');
			$.messager.alert('提示','发货数量不能为空','error');
			return false;
		} 
    } 
	var o =$('#datagrid-delivery-list').datagrid('getData'); 
	var datas = encodeURI(JSON.stringify(o)); 
  	$.ajax({
	 	url:'${ctx}/manager/order/delivery/saveDelivery',
        type: 'post',
        data:  "datas=" + datas +"&" + $('#form-delivery-addoredit').serialize() + "&type=" + type, 
        dataType:"json",
        success: function (data) {   
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.alert('提示', data.message ,'info');
					$('#win-delivery-addoredit').window('close');
					$('#datagrid-pending-list').datagrid('reload'); 
				}else{
					$.messager.alert('提示',data.message,'error');
				}
			}catch (e) {
				$.messager.alert('提示',e,'error'); 
			} 
       	}
      }); 
}
	
</script>
</body>
</html>
