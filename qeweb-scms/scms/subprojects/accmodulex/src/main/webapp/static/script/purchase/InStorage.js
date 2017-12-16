function searchAccountSetting(){
	var searchParamArray = $('#form-instorage-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-instorage-list').datagrid('load',searchParams);
}

function toAddBillList() {
	var selections = $('#datagrid-instorage-list').datagrid('getSelections');
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
	$('#win-instorage-add').window('open'); 
	//主信息
	$('#form-bill-addoredit').form('load', ctx + '/manager/account/instorage/createBill/' + select.join(''));  
	
	//明细
	$('#datagrid-billit-list').datagrid({     
    	url: ctx + '/manager/account/instorage/createBillItem'
	});
	$('#datagrid-billit-list').datagrid('load',{"search-IN_id":select.join('')});  
	//发票信息
	$('#datagrid-invoice-list').datagrid({   
    	url: ctx + '/manager/account/instorage/createInvoice'
	});   
	$('#datagrid-invoice-list').datagrid('load',{});  
	
	
	var objGrid = $("#datagrid-billit-list");
	var invQtyEdt = objGrid.datagrid('getEditor', {index:1,field:'accountQty'});
}

//保存对账清单
function saveInvoince(type) {
	$.messager.progress();
	CellEditor.accept('datagrid-billit-list'); 
	RowEditor.accept('datagrid-invoice-list');    
	//验证清单明细
 	var rows = $('#datagrid-billit-list').datagrid('getRows');
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
		if((~~rows[i].accountQty + ~~rows[i].inAccountQty) > rows[i].inStorageQty) {
			$.messager.progress('close');
			$.messager.alert('提示','索引行[' + i + '] 结算数量大于入库数量' ,'error'); 
			return false;
		}
    } 
	//验证发票信息
	var rows = $('#datagrid-invoice-list').datagrid('getRows');
	for(i = 0;i < rows.length;i++) {
		 if(rows[i].invoiceCode == null || rows[i].invoiceCode == ''
				 || rows[i].invoiceMoney == null || rows[i].invoiceMoney ==''
			     || rows[i].invoiceTime == null) {
			$.messager.progress('close');
			$.messager.alert('提示','发票号，发票金额，发票时间不能为空','error');
			return false;
		} 
    } 
	//billItem
	var o =$('#datagrid-billit-list').datagrid('getData'); 
	var datas = encodeURI(JSON.stringify(o));
	//invoince
	var o2 = $('#datagrid-invoice-list').datagrid('getData'); 
	var datas2 = encodeURI(JSON.stringify(o2));
  	$.ajax({
	 	url: ctx + '/manager/account/instorage/saveBill',
        type: 'post',
        data:  "datas=" + datas +"&datas2=" + datas2 +"&" + $('#form-bill-addoredit').serialize() + "&invoiceStatus=" + type, 
        dataType:"json",
        success: function (data) {
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.alert('提示', data.message ,'info');
					$('#win-instorage-add').window('close');
					$('#datagrid-instorage-list').datagrid('reload'); 
				}else{
					$.messager.alert('提示',data.message,'error');
				}
			}catch (e) {
				$.messager.alert('提示',e,'error'); 
			} 
       	}
      }); 
}

//导出
function exportSetting() {
	$('#form-instorage-search').form('submit',{
		url: ctx + '/manager/account/setting/exportExcel',
		success:function(data){
			$.messager.progress('close');
		}
	});
}
