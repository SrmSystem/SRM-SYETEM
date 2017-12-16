<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
<head>
	<title>采购订单管理</title>
	<script type="text/javascript">
		function orderCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showOrderDetail('+ r.id +');">' + v + '</a>';
		}
		
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-order-list" title="采购订单列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/purchaseorder/${vendor }',method:'post',singleSelect:false,   
		toolbar:'#orderListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'orderCode',formatter:orderCodeFmt">采购订单号</th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}">供应商编码</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		<th data-options="field:'receiveOrg'">收货方</th>
		<th data-options="field:'purchaseUser',formatter:function(v,r,i){return r.purchaseUser.name;}">采购员</th>   
		<th data-options="field:'orderDate'">订单日期</th>
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}">发布状态</th>    
		<th data-options="field:'publishTime'">发布时间</th>
		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}">确认状态</th>
		<th data-options="field:'confirmTime'">确认时间</th>
		<tags:dynamic objName='${extended }' type='queryTable'></tags:dynamic>
		<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}">发货状态</th>
		<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}">收货状态</th>
		<th data-options="field:'closeStatus',formatter:function(v,r,i){return StatusRender.render(v,'closeStatus',false);}">关闭状态</th>
		</tr></thead>
	</table>
	<div id="orderListToolbar" style="padding:5px;">
		<div>
			<c:if test="${vendor == false}">  
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="operateOrder('publish')">发布</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="operateOrder('close')">关闭</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="importOrder()">导入订单</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()">同步订单</a>
			</c:if>  
			<c:if test="${vendor}">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="operateOrder('confirm')">确认</a>
			</c:if>  
		</div>
		<div>
			<form id="form-order-search" method="post">
			采购订单号：<input type="text" name="search-LIKE_orderCode" class="easyui-textbox" style="width:80px;"/>
			供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			发布状态：<select class="easyui-combobox" name="search-EQ_publishStatus"><option value="">-请选择-</option><option value="0">未发布</option><option value="1">已发布</option></select>
			确认状态：<select class="easyui-combobox" name="search-EQ_confirmStatus"><option value="">-请选择-</option><option value="0">未确认</option><option value="1">已确认</option></select><br>
			发货状态：<select class="easyui-combobox" name="search-EQ_deliveryStatus"><option value="">-请选择-</option><option value="0">未发货</option><option value="1">已发货</option><option value="2">部分发货</option></select>
			收货状态：<select class="easyui-combobox" name="search-EQ_receiveStatus"><option value="">-请选择-</option><option value="0">未收货</option><option value="1">已收货</option><option value="2">部分收货</option></select>
			关闭状态：<select class="easyui-combobox" name="search-EQ_closeStatus"><option value="">-请选择-</option><option value="0">未关闭</option><option value="1">已关闭</option></select>
			<tags:dynamic objName='${extended }' type='queryForm'></tags:dynamic> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrder()">查询</a>
			</form>
		</div>
	</div>
	
	<!-- 导入订单 -->
	<div id="win-order-import" class="easyui-window" title="导入订单" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-order-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/order/purchaseorder/filesUpload"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
					模板：<a href="javascript:;" onclick="File.download('WEB-INF/template/PurchaseOrder.xls','采购订单模版')">采购订单模版.xls</a>    
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveimportorder();">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-order-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
	
	<div id="win-order-detail" class="easyui-window" title="订单详情" style="width:700px;height:500px"  
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="purchaseorderitemToolbar" style="padding:5px;">
				<div>
					<form id="form-purchaseorderitem-search">
						<input id="id" name="id" value="-1" type="hidden"/>
					采购订单号：<input type="text" name="orderCode" class="easyui-textbox" style="width:80px;" data-options="required:true" readonly="readonly"/>
					
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="exportPurchasePlan()">导出</a>
					</form>
				</div>
			</div>
			<table id="datagrid-purchaseorderitem-list" title="采购订单详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#purchaseorderitemToolbar',
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'itemNo'">行号</th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
				<th data-options="field:'receiveOrg'">收货方</th>   
				<th data-options="field:'orderQty'">订单数量</th>   
				<th data-options="field:'requestTime'">要求到货时间</th>   
				<th data-options="field:'currency'">币种</th>   
				<th data-options="field:'unitName'">单位</th>      
				<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}">确认状态</th>   
				<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}">发货状态</th>   
				<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}">收货状态</th>   
				</tr></thead>
			</table>
			<!-- 发货单 -->
			<table id="datagrid-purchasedelivery-list" title="发货单详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'deliveryCode',formatter:function(v,r,i){return r.delivery.deliveryCode;}">发货单号</th>
				<th data-options="field:'itemNo'">行号</th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
				<th data-options="field:'receiveOrg',formatter:function(v,r,i){return r.delivery.receiveOrg;}">收货方</th>   
				<th data-options="field:'deliveryQty'">发货数量</th>   
				<th data-options="field:'receiveQty'">收货数量</th>   
				<th data-options="field:'unitName',formatter:function(v,r,i){return r.orderItem.unitName;}">单位</th>     
				<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}">发货状态</th>   
				<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}">收货状态</th>   
				</tr></thead>
			</table>  
			<!-- 收货单 -->  
			<table id="datagrid-purchasereceive-list" title="收货单详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'receiveCode',formatter:function(v,r,i){return r.receive.receiveCode;}">行号</th>
				<th data-options="field:'itemNo'">行号</th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
				<th data-options="field:'receiveOrg',formatter:function(v,r,i){return r.receive.receiveOrg;}">收货方</th>   
				<th data-options="field:'deliveryQty',formatter:function(v,r,i){return r.deliveryItem.deliveryQty;}">发货数量</th>   
				<th data-options="field:'receiveQty'">收货数量</th>   
				<th data-options="field:'inStoreQty'">入库数量</th>   
				<th data-options="field:'returnQty'">验退数量</th>     
				</tr></thead>
			</table>  
		</div>
	</div>
	
<script type="text/javascript">
function searchOrder(){
	var searchParamArray = $('#form-order-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-order-list').datagrid('load',searchParams);
}

function showOrderDetail(id) {
	$('#win-order-detail').window({
		iconCls:'icon-add',
		title:'采购订单详情'
	});  
	$('#form-order-detail').form('clear');
	$('#win-order-detail').window('open');   
	$('#form-purchaseorderitem-search').form('load','${ctx}/manager/order/purchaseorder/getOrder/'+id);
	
	//显示订单详情
	$('#datagrid-purchaseorderitem-list').datagrid({   
    	url:'${ctx}/manager/order/purchaseorder/orderitem/' + id     
	});
	
	//显示发货详情
	$('#datagrid-purchasedelivery-list').datagrid({   
    	url:'${ctx}/manager/order/delivery/byorderdeliveryitem/' + id     
	});
	
	//显示收货详情
	$('#datagrid-purchasereceive-list').datagrid({   
    	url:'${ctx}/manager/order/receive/byorderreceiveitem/' + id       
	});
}

//发布、确认、关闭
function operateOrder(st){
	var selections = $('#datagrid-order-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(st == "publish" && selections[i].publishStatus == 1) {
			$.messager.alert('提示','包含已发布记录无法重复发布！','error');
			return false;
		} else if(st == "confirm" && selections[i].confirmStatus == 1) {
			$.messager.alert('提示','包含已确认记录无法重复确认！','error');
			return false;
		} else if(st == "close" && selections[i].closeStatus == 1) {
			$.messager.alert('提示','包含已关闭记录无法重复关闭！','error');
			return false;
		}
	}
	$.messager.progress();
	var params = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/order/purchaseorder/' + st + 'Order',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'消息',
						msg:  data.message, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#datagrid-order-list').datagrid('reload'); 
				}else{
					$.messager.alert('提示',data.message,'error');
				}
			}catch (e) {
				$.messager.alert('提示',e,'error'); 
			} 
		}
	});
}

//导入采购订单
function importOrder() {
	$('#form-order-import').form('clear');   
	$('#win-order-import').window('open');  
}

//保存采购订单
function saveimportorder() {
	$.messager.progress();
	$('#form-order-import').form('submit',{
		ajax:true,
		iframe: true,    
		url:'${ctx}/manager/order/purchaseorder/filesUpload', 
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success:function(data){
			$.messager.progress('close');
			try{
			var result = eval('('+data+')');
			if(result.success){
				$.messager.alert('提示','导入采购订单成功','info');
				$('#win-order-import').window('close');
				$('#datagrid-order-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
			}
			}catch (e) {  
				$.messager.alert('提示',data,'error');
			}
		}
	});
}
</script>
</body>
</html>
