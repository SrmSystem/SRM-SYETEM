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
		
		var editIndex = undefined;
		function endEditing(datagrid){
			if (editIndex == undefined){return true}
			if (datagrid.datagrid('validateRow', editIndex)){
				datagrid.datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		
		function onClickBillItemCell(index, field){
			var datagrid = $('#datagrid-billitem-list');  
			if (endEditing(datagrid)){
				datagrid.datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
				editIndex = index;
			}
		}
		
		function onClickInvoiceCell(index, field){
			var datagrid = $('#datagrid-invoice-list');
			if (endEditing(datagrid)){
				datagrid.datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
				editIndex = index;
			}
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
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="makeInvoince()">开票</a>
				</div>
				<form id="form-billitem-search">
					<input id="id" name="id" value="-1" type="hidden"/>
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>清单号:</td><td><input class="easyui-textbox" id="billCode" name="billCode" type="text"  readonly="readonly" data-options="required:true"/></td>
							<td>合计:</td><td><input class="easyui-textbox" id="totalPrice" name="totalPrice" type="text" data-options="required:true"/></td> 
						</tr>   
						<tr>
							<td>税率:</td><td><input class="easyui-textbox" id="tax" name="tax" type="text" data-options="required:true"/></td>
							<td>含税合计:</td><td><input class="easyui-textbox" id="totalTaxPrice" name="totalTaxPrice" type="text" data-options="required:true"/></td>
						</tr>
					</table>    
				</form>  
			</div>
			<table id="datagrid-billitem-list" title="开票清单详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#billItemToolbar',
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]
				,onClickCell: onClickBillItemCell"
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
				<th data-options="field:'accountQty',editor:'numberbox',required:true">结算数量</th>  
				<th data-options="field:'price',editor:'numberbox',required:true">单价</th>
				</tr></thead>
			</table>
			<table id="datagrid-invoice-list" title="发票详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				<c:choose>
					<c:when test="${type eq 'online'}">  
						toolbar:'#tb',
					</c:when>
				</c:choose>
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],
				onClickCell: onClickInvoiceCell" >
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'invoiceCode',width:'160',editor:'text',required:true">发票号</th>
				<th data-options="field:'invoiceMoney',width:'160',editor:'numberbox',required:true">金额</th>
				</tr></thead>
			</table>
			
			<c:choose>
					<c:when test="${type eq 'online'}">  
						<div id="tb" style="padding:5px;height:auto">
							<div style="margin-bottom:5px">
								<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="javascript:appendRow()"></a>
								<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:deleteRow()"></a> 
							</div>
						</div>
					</c:when>
				</c:choose>
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
	$('#datagrid-invoice-list').datagrid('load',{});
}

/**
 * 开票
 */
function makeInvoince() {
	$.messager.progress();
	//验证清单明细
 	var rows = $('#datagrid-billitem-list').datagrid('getRows');
	for(i = 0;i < rows.length;i++) {
		 if(rows[i].accountQty == null || rows[i].accountQty == '') {
			$.messager.progress('close');
			$.messager.alert('提示','结算数量不能为空','error');
			return false;
		} 
		 if(rows[i].price == null || rows[i].price == '') {
			$.messager.progress('close');
			$.messager.alert('提示','单价不能为空','error');
			return false;
		} 
    } 
	//验证发票信息
	var rows = $('#datagrid-invoice-list').datagrid('getRows');
	for(i = 0;i < rows.length;i++) { 
		 if(rows[i].invoiceCode == null || rows[i].invoiceCode == ''
				 || rows[i].invoiceMoney == null || rows[i].invoiceMoney =='') {
			$.messager.progress('close');
			$.messager.alert('提示','发票号，发票金额不能为空','error');
			return false;
		} 
    } 
	//billItem
	var o =$('#datagrid-billitem-list').datagrid('getData'); 
	var datas = encodeURI(JSON.stringify(o));
	//invoince
	var o2 = $('#datagrid-invoice-list').datagrid('getData'); 
	var datas2 = encodeURI(JSON.stringify(o2));
  	$.ajax({
	 	url:'${ctx}/manager/account/bill/saveBill',
        type: 'post',
        data:  "datas=" + datas +"&datas2=" + datas2 +"&" + $('#form-billitem-search').serialize(), 
        dataType:"json",
        success: function (data) {
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.alert('提示', data.message ,'info');
					$('#win-bill-detail').window('close');
					$('#datagrid-bill-list').datagrid('reload'); 
				}else{
					$.messager.alert('提示',data.message,'error');
				}
			}catch (e) {
				$.messager.alert('提示',e,'error'); 
			} 
       	}
      }); 
}

function appendRow() {
	$('#datagrid-invoice-list').datagrid('appendRow', {         
		id: '',
		invoiceCode: '',
		invoiceMoney: ''                  
    }).datagrid('getRows');
}

function deleteRow() {
	var row = $('#datagrid-invoice-list').datagrid('getSelected');
	if (row) {
         var rowIndex = $('#datagrid-invoice-list').datagrid('getRowIndex', row);
         $('#datagrid-invoice-list').datagrid('deleteRow', rowIndex);    
	 }
}

</script>   
</body>
</html>
