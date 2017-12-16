<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>收货管理</title>
	<script type="text/javascript">
		function receiveCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showReceive('+ r.id +');">' + v + '</a>';
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
			
			return '未收货';
		}
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-receive-list" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/receive',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#receiveListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'attr7'">采购订单号</th>
		<%-- <th data-options="field:'deliveryCode',formatter:function(v,r,i){return r.delivery.deliveryCode;}">发货单号</th> --%>
		<th data-options="field:'receiveCode',formatter:receiveCodeFmt">收货单号</th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}">供应商编码</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		<th data-options="field:'buyerCode',formatter:function(v,r,i){return r.buyer.code;}">采购商编码</th>
		<th data-options="field:'buyerName',formatter:function(v,r,i){return r.buyer.name;}">采购商名称</th>
		<th data-options="field:'receiveOrg'">收货方</th>
		<th data-options="field:'receiveStatus',formatter:receiveStatusFmt">收货状态</th>
		<th data-options="field:'receiveTime'">收货时间</th>
		<th data-options="field:'attr8'">收货人</th>
		</tr></thead>
	</table>
	<div id="receiveListToolbar" style="padding:5px;">
		<div>
		<!--   <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="importReceive()">导入收货单</a> -->
		</div>
		<div>
			<form id="form-receive-search" method="post">
			发货单号：<input type="text" name="search-LIKE_delivery.deliveryCode" class="easyui-textbox" style="width:80px;"/>
			收货单号：<input type="text" name="search-LIKE_receiveCode" class="easyui-textbox" style="width:80px;"/>
			采购商编码：<input type="text" name="search-LIKE_buyer.code" class="easyui-textbox" style="width:80px;"/>
			采购商名称：<input type="text" name="search-LIKE_buyer.name" class="easyui-textbox" style="width:80px;"/>
			供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/><br>
			收货方：<input type="text" name="search-LIKE_receiveOrg" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchReceive()">查询</a>
			</form>
		</div>
	</div>
	
	<!-- 导入收货单 -->
	<div id="win-receive-import" class="easyui-window" title="导入收货单" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-receive-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/order/receive/filesUpload"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
					<%-- 模板：<a href="${ctx}/template/Receives.xls">收货单模版.xls</a> --%>   
					模板：<a href="javascript:;" onclick="File.download('WEB-INF/template/Receives.xls','收货单模版')">收货单模版.xls</a> 
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveimportreceive();">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-receive-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
	
	<div id="win-receive-detail" class="easyui-window" title="收货单详情" style="width:600px;height:400px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id=receiveItemToolbar" style="padding:5px;">
				<div>
					<form id="form-receiveitem-search">
						<input id="id" name="id" value="-1" type="hidden"/>
					收货单号：<input type="text" name="receiveCode" class="easyui-textbox"  style="width:120px;" readonly="readonly"/>
					收货方：<input type="text" name="receiveOrg" class="easyui-textbox" style="width:120px;" readonly="readonly"/>
					<!-- 收货时间：<input type="week" name="receiveTime" class="easyui-textbox" style="width:120px;" readonly="readonly"/> -->
					</form>  
				</div>
			</div>
			<table id="datagrid-receiveitem-list" title="收货单详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#receiveItemToolbar',
				pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'orderCode', formatter:function(v,r,i){return r.orderItem.order.orderCode}">采购订单号</th>
				<th data-options="field:'materialCode'">物料编码</th>
				<th data-options="field:'materialName'">物料名称</th>
				<th data-options="field:'deliveryQty'">发货数量</th>
				<th data-options="field:'receiveQty'">收货数量</th>
				<th data-options="field:'inStoreQty'">入库数量</th>
				<th data-options="field:'returnQty'">退货数量</th>
				</tr></thead>
			</table>
		</div>
	</div>
	
<script type="text/javascript">
function searchReceive(){
	var searchParamArray = $('#form-receive-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-receive-list').datagrid('load',searchParams);
}

//查看发货单详情
function showReceive(id){
	$('#form-receiveitem-search').form('clear');
	$('#win-receive-detail').window('open');
	$('#form-receiveitem-search').form('load','${ctx}/manager/order/receive/getReceive/'+id);
	//详情
	$('#datagrid-receiveitem-list').datagrid({   
    	url:'${ctx}/manager/order/receive/receiveitem/' + id     
	});
	$('#datagrid-receiveitem-list').datagrid('load',{});
}

//导入收货单
function importReceive() {
	$('#form-receive-import').form('clear');   
	$('#win-receive-import').window('open');  
}

//保存收货单
function saveimportreceive() {
	$.messager.progress();
	$('#form-receive-import').form('submit',{
		ajax:true,
		iframe: true,    
		url:'${ctx}/manager/order/receive/filesUpload', 
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
				$('#win-receive-import').window('close');
				$('#datagrid-receive-list').datagrid('reload');
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
