<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>开票列表</title>
	<script type="text/javascript">
		function billCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showBillDetail('+ r.id +');">' + v + '</a>';
		}
	
		function invoiceStatusFmt(v,r,i){
			if(r.invoiceStatus == 0)
				return '未开票';
			else if (r.invoiceStatus == 1)
				return '已开票';
			
			return '';
		}
		
		function receiveStatusFmt(v,r,i){
			if(r.receiveStatus == 0)
				return '未接收';
			else if (r.receiveStatus == 1)
				return '已接收';
			else if (r.receiveStatus == -1)
				return '已驳回';
			
			return '';
		}
	</script>
</head>

<body style="margin:0;padding:0;">

	<table id="datagrid-bill-list" title="开票列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/account/bill/${type}',method:'post',singleSelect:false,
		toolbar:'#billListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>   
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'billCode',formatter:billCodeFmt">开票单号</th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}">供应商编码</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		<th data-options="field:'totalPrice'">合计金额</th>
		<th data-options="field:'tax'">税率</th>
		<th data-options="field:'totalTaxPrice'">含税合计</th>
		<th data-options="field:'invoiceStatus',formatter:invoiceStatusFmt">开票状态</th>
		<th data-options="field:'receiveStatus',formatter:receiveStatusFmt">接收状态 </th>
		<th data-options="field:'createUserName'">创建人</th>
		<th data-options="field:'createTime'">创建时间</th>
		</tr></thead>
	</table>
	<div id="billListToolbar" style="padding:5px;">
		<div>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="exportSetting()">导出</a> -->
		</div>
		<div>
			<form id="form-bill-search" method="post">
			开票单号：<input type="text" name="search-LIKE_billCode" class="easyui-textbox" style="width:80px;"/>  
			供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			开票状态：<select class="easyui-combobox" name="search-EQ_invoiceStatus"><option value="0">未开票</option><option value="1">已开票</option></select> 
			接收状态：<select class="easyui-combobox" name="search-EQ_receiveStatus"><option value="0">未接收</option><option value="1">已接收</option><option value="-1">已驳回</option></select> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchBillList()">查询</a>  
			</form>
		</div>
	</div>  
	
	<div id="win-bill-detail" class="easyui-window" title="开票清单详情" style="width:600px;height:400px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id=billItemToolbar" style="padding:5px;">
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="receiveInvoince(1)">接收发票</a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="receiveInvoince(-1)">打回修改</a>
				</div>
				<form id="form-billitem-search">
					<input id="id" name="id" value="-1" type="hidden"/>
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>清单号:</td><td><input class="easyui-textbox" id="billCode" name="billCode" type="text"  readonly="readonly" data-options="required:true"/></td>
							<td>合计:</td><td><input class="easyui-textbox" id="totalPrice" name="totalPrice" type="text" readonly="readonly" data-options="required:true"/></td> 
						</tr>   
						<tr>
							<td>税率:</td><td><input class="easyui-textbox" id="tax" name="tax" type="text" readonly="readonly" data-options="required:true"/></td>
							<td>含税合计:</td><td><input class="easyui-textbox" id="totalTaxPrice" name="totalTaxPrice" type="text" readonly="readonly" data-options="required:true"/></td>
						</tr>
					</table>    
				</form>  
			</div>
			<table id="datagrid-billitem-list" title="开票清单详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#billItemToolbar',
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<c:choose>
					<c:when test="${type eq 'in'}"> 
						<th data-options="field:'inStorageCode',formatter:function(v,r,i){if(r.instorageItem) return r.instorageItem.inStorage.inStorageCode; return ''}">入库单号</th>
					</c:when>
					<c:otherwise>
						<th data-options="field:'onlineCode',formatter:function(v,r,i){if(r.onlineItem) return r.onlineItem.onlineSettlement.onlineCode; return ''}">上线单号</th>
					</c:otherwise>
				</c:choose>
				<th data-options="field:'materialCode', formatter:function(v,r,i){return r.material.code}">物料编码</th>    
				<th data-options="field:'materialName', formatter:function(v,r,i){return r.material.name}">物料名称</th>
				<c:choose>
					<c:when test="${type eq 'in'}"> 
						<th data-options="field:'inStorageQty',formatter:function(v,r,i){if(r.instorageItem) return r.instorageItem.inStorageQty; return ''}">入库数量</th>
					</c:when>
					<c:otherwise>
						<th data-options="field:'onlineQty',formatter:function(v,r,i){if(r.onlineItem) return r.onlineItem.onlineQty; return ''}">上线数量</th>
					</c:otherwise>
				</c:choose>
				<th data-options="field:'accountQty'">结算数量</th>
				<th data-options="field:'price'">单价</th>
				</tr></thead>
			</table>
			<table id="datagrid-invoice-list" title="发票详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]" >
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'invoiceCode'">发票号</th>
				<th data-options="field:'invoiceMoney'">金额</th>
				</tr></thead>
			</table>
		</div>
	</div>
	
<script type="text/javascript">
function searchBillList(){
	var searchParamArray = $('#form-bill-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-bill-list').datagrid('load',searchParams);
}

//详情
function showBillDetail(id) {
	$('#form-billitem-search').form('clear');
	$('#win-bill-detail').window('open');
	$('#form-billitem-search').form('load','${ctx}/manager/account/bill/getBillList/'+id);
	//详情
	$('#datagrid-billitem-list').datagrid({   
    	url:'${ctx}/manager/account/bill/billitem/' + id     
	});
	$('#datagrid-billitem-list').datagrid('load',{});  
	//发票
	$('#datagrid-invoice-list').datagrid({   
    	url:'${ctx}/manager/account/bill/billinvoice/' + id     
	});
}

/**
 * type 1:接收 -1：驳回修改
 */
function receiveInvoince(type) {
	$.messager.progress();
	var billId = $("form:eq(1) input:eq(0)").val(); //清单主表ID
	$.messager.confirm("操作提示", "确定要执行操作吗？", function (data) {
        if (data) {
        	$.ajax({
        		url:'${ctx}/manager/account/bill/receiveInvoice',
        		type:'POST',
        		data: "billId=" + billId + "&recstatus=" + type,
        		dataType:"json",
        		success:function(data){
        			$.messager.progress('close');
        			if(data.success){
        				$.messager.show({
        					title:'消息',
        					msg:data.message,
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#win-bill-detail').window('close');
        				$('#datagrid-bill-list').datagrid('reload');
        			}
        		}
        	});
        } else {
        	$.messager.progress('close');
        }
    });
}

//导出
function exportSetting() {
	$('#form-bill-search').form('submit',{
		url: '${ctx}/manager/account/bill/exportExcel',
		success:function(data){
			$.messager.progress('close');
		}
	});
}

</script>   
</body>
</html>
