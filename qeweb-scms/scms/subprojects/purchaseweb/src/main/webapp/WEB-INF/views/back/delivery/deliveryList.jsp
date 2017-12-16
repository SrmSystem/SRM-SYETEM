<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>发货管理</title>
	<script type="text/javascript">
		function deliveryCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showDelivery('+ r.id +');">' + v + '</a>';
		}

		function deliveryStatusFmt(v,r,i){
			if(r.deliveryStatus == 0)
				return '未发货';
			else if(r.deliveryStatus == 1)
				return '已发货';
			else if(r.deliveryStatus == 2)
				return '部分发货';
			return '未发货';
		}
		
		function receiveStatusFmt(v,r,i){
			if(r.receiveStatus == 0)
				return '未收货';
			else if(r.receiveStatus == 1)
				return '已收货';
			else if(r.receiveStatus == 2)
				return '部分收货';
			else if(r.receiveStatus == 3)
				return '退货';
			
			return '未收货';
		}
	</script>
</head>

<body style="margin:0;padding:0;">
	<html>
<head>
	<title>发货管理</title>
	<script type="text/javascript">
		function deliveryCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showDelivery('+ r.id +');">' + v + '</a>';
		}		

		function deliveryStatusFmt(v,r,i){
			if(r.deliveryStatus == 0)
				return '未发货';
			else if(r.deliveryStatus == 1)
				return '已发货';
			else if(r.deliveryStatus == 2)
				return '部分发货';
			return '未发货';
		}
		
		function receiveStatusFmt(v,r,i){
			if(r.receiveStatus == 0)
				return '未收货';
			else if(r.receiveStatus == 1)
				return '已收货';
			else if(r.receiveStatus == 2)
				return '部分收货';
			else if(r.receiveStatus == 3)
				return '退货';
			
			return '未收货';
		}
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-delivery-list" title="发货单列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/delivery',method:'post',singleSelect:false,
		toolbar:'#deliveryListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"    
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'deliveryCode',formatter:deliveryCodeFmt">发货单号</th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}">供应商编码</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		<th data-options="field:'buyerCode',formatter:function(v,r,i){return r.buyer.code;}">采购商编码</th>
		<th data-options="field:'buyerName',formatter:function(v,r,i){return r.buyer.name;}">采购商名称</th>
		<th data-options="field:'receiveOrg'">收货方</th>
		<th data-options="field:'deliveryStatus',formatter:deliveryStatusFmt">发货状态</th>
		<th data-options="field:'deliveyUserName',formatter:function(v,r,i){if(r.deliveryUser) return r.deliveryUser.name; return '';}">发货人</th>
		<th data-options="field:'deliveyTime'">发货时间</th>
		<th data-options="field:'receiveStatus',formatter:receiveStatusFmt">收货状态</th>
		</tr></thead>
	</table>
	<div id="deliveryListToolbar" style="padding:5px;">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="delivery()">发货</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="canceldelivery()">取消发货单</a>
		</div>
		<div>
			<form id="form-delivery-search" method="post">
			发货单号：<input type="text" name="search-LIKE_deliveryCode" class="easyui-textbox" style="width:80px;"/>
			采购商编码：<input type="text" name="search-LIKE_buyer.name" class="easyui-textbox" style="width:80px;"/>
			发货状态：<select class="easyui-combobox" name="search-EQ_deliveryStatus"><option value="0">未发货</option><option value="1">已发货</option></select>
			收货状态：<select class="easyui-combobox" name="search-EQ_receiveStatus"><option value="0">未收货</option><option value="1">已收货</option></select>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchDelivery()">查询</a>
			</form>
		</div>
	</div>
	
	
	<div id="win-delivery-detail" class="easyui-window" title="发货单详情" style="width:600px;height:400px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id=deliveryItemToolbar" style="padding:5px;">
				<div>
					<form id="form-deliveryitem-search">
						<input id="id" name="id" value="-1" type="hidden"/>
					发货单号：<input type="text" name="deliveryCode" class="easyui-textbox"  style="width:120px;" data-options="required:true" readonly="readonly"/>
					收货方：<input type="text" name="receiveOrg" class="easyui-textbox" style="width:120px;" data-options="required:true" readonly="readonly"/>
					发货时间：<input type="week" name="deliveyTime" class="easyui-textbox" style="width:80px;" data-options="required:true" readonly="readonly"/>
					
					<!--打印操作 -->
					<!--  <button onclick="gotoPrint();" class="noprint">打印</button>-->
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true" onclick="gotoPrint()">打印</a>					
					</form>  
				</div>
			</div>
			<table id="datagrid-deliveryitem-list" title="发货单详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#deliveryItemToolbar',
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'itemNo'">行号</th>
				<th data-options="field:'orderCode', formatter:function(v,r,i){return r.orderItem.order.orderCode}">采购订单号</th>
				<th data-options="field:'materialCode', formatter:function(v,r,i){return r.material.code}">物料编码</th>
				<th data-options="field:'materialName', formatter:function(v,r,i){return r.material.name}">物料名称</th>
				<th data-options="field:'deliveryQty'">发货数量</th>
				<th data-options="field:'receiveQty'">收货数量</th>
				<th data-options="field:'receiveStatus',formatter:receiveStatusFmt">收货状态</th>
				 
				</tr></thead>
			</table>
		</div>
	</div>

<script type="text/javascript">
function searchDelivery(){
	var searchParamArray = $('#form-delivery-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-delivery-list').datagrid('load',searchParams);
}


//查看发货单详情
function showDelivery(id){
	$('#form-deliveryitem-search').form('clear');
	$('#win-delivery-detail').window('open');
	$('#form-deliveryitem-search').form('load','${ctx}/manager/order/delivery/getDelivery/'+id);
	//详情
	$('#datagrid-deliveryitem-list').datagrid({   
    	url:'${ctx}/manager/order/delivery/deliveryitem/' + id     
	});
	$('#datagrid-deliveryitem-list').datagrid('load',{});
	aid=id;
	return aid;
}



//发货
function delivery(){
	var selections = $('#datagrid-delivery-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].deliveryStatus == 1) {
			$.messager.alert('提示','包含已发货记录无法重复发货','error');  
			return false;
		}
	}
	var params = $.toJSON(selections);
	$.messager.confirm("操作提示", "确定发货操作吗？", function (data) {  
        if (data) {
        	$.ajax({
        		url:'${ctx}/manager/order/delivery/dodelivery',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			if(data.success){
        				$.messager.show({
        					title:'消息',
        					msg:'发货成功',
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#datagrid-delivery-list').datagrid('reload');
        			}
        		}
        	});   
        }
	});
}

//取消发货单
function canceldelivery() {
	var selections = $('#datagrid-delivery-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].receiveStatus > 0) {
			$.messager.alert('提示','包含已收货记录无法取消发货单！','error');
			return false;
		}
	}
	var params = $.toJSON(selections);
	$.messager.confirm("操作提示", "执行此操作将删除发货单,确定要执行操作吗？", function (data) {
        if (data) {
        	$.ajax({
        		url:'${ctx}/manager/order/delivery/canceldelivery',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			if(data.success){
        				$.messager.show({
        					title:'消息',
        					msg:'取消发货成功',
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#datagrid-delivery-list').datagrid('reload');
        			}
        		}
        	});
        }
    });
}

//打印信息
function gotoPrint(){
	var pid=aid;
	window.open('${ctx}/manager/order/printdelivery/pending/'+pid);
}


</script>
</body>
</html>

