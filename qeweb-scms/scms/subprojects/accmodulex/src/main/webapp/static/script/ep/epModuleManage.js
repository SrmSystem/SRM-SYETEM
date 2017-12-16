
function searchEpModuleList(){
	var searchParamArray = $('#form-epModule-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-epModule-list').datagrid('load',searchParams);
}

//详情
function showEpModuleDetail(id) {
	new dialog2().showWin("模板详情", 900, 480, ctx + '/manager/ep/epModule/getEpModule/' + id + '/' + false);
}

//修改
function updateEpModule() {
	var selections = $('#datagrid-epModule-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	if(selections.length > 1){
		$.messager.alert('提示','只能选一条记录！','info');
		return false;
	}
	var id = selections[0].id;
	
	 new dialog2().showWin("模板详情", 900, 480, ctx + '/manager/ep/epModule/getEpModule/' + id + '/' + true);
	 
	 /*	var lbsj = $('#datagrid-epModule-list').datagrid('getRows');
		var code = document.getElementById('code').value;
		if(code == lbsj.code){
			$.messager.progress('close');
			$.messager.alert('提示','该编号已存在，请重新输入','error');
			return false;
		}
		*/
	 
	
}

//删除
function deleteEpModule() {
	var selections = $('#datagrid-epModule-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	if(selections.length > 1){
		$.messager.alert('提示','只能选一条记录！','info');
		return false;
	}
	var id = selections[0].id;
	$.messager.confirm("操作提示", "确定要删除该条记录以及对应的明细记录吗？", function (data) {
		if (data) {
			$.ajax({
				url: ctx + '/manager/ep/epModule/deleteEpModule/' + id,
				type:'POST',
				dataType:"json",
				contentType : 'application/json',
				success:function(data){
					$.messager.progress('close');
					if(data.success){
						$.messager.success("操作成功");
					}else{
						$.messager.fail(data.msg);
					}
					$('#datagrid-epModule-list').datagrid('reload');
				}
			});
		} else {
			$.messager.progress('close');
		}
	});
}

//二级模板详情
function showEpModuleItemDetail() {
	var selections = $('#datagrid-item-addoredit').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	if(selections.length > 1){
		$.messager.alert('提示','只能选一条记录！','info');
		return false;
	}
	var id = selections[0].id;
	new dialog2().showWin("一级模板详情", 900, 480, ctx + '/manager/ep/epModule/getEpModuleItemDetail/' + id + '/' + false);
}

//修改二级模板详情
function updateEpModuleItemDetail() {
	var selections = $('#datagrid-item-addoredit').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	if(selections.length > 1){
		$.messager.alert('提示','只能选一条记录！','info');
		return false;
	}
	var id = selections[0].id;
	new dialog2().showWin("一级模板详情", 900, 480, ctx + '/manager/ep/epModule/getEpModuleItemDetail/' + id + '/' + true);
}


function addEpModule(){
	new dialog2().showWin("模板详情", 900, 480, ctx + '/manager/ep/epModule/createEpModule');
	var url = ctx + '/manager/ep/epModule/getEpModuleItem/';
}
function addSecondItem(id){
	new dialog2().showWin("模板一级明细详情", 900, 480, ctx + '/manager/ep/epModule/addSecondItem/' + id);
}


function saveEpModule(type) {
	$.messager.progress();
	var isValid = $('#form-epModule-addoredit').form('validate');
	if(!isValid){
		$.messager.progress('close');
		$.messager.alert('提示','数据项请填写完整','error');
		return false;
	}	
	RowEditor.accept('datagrid-item-addoredit');  
	var rows = $('#datagrid-item-addoredit').datagrid('getRows');
	if(rows.length == 0) {
		$.messager.progress('close');
		$.messager.alert('提示','明细数据项不能为空','error');
		return false;
	}
	for(var j=0;j<rows.length;j++){
		if(rows[j].name==null||rows[j].name==''){
			$.messager.progress('close');
			$.messager.alert('提示','名称不能为空','error');
			return false;
		}
	}
	if(type == 1){
		var isDefault = $('#isDefault').val();
		if(isDefault == '是'){
			$("#isDefault").textbox('setValue', 1);
		}
		if(isDefault == '否'){
			$("#isDefault").textbox('setValue', 0);
		}
	}
	
/*	var lbsj = $('#datagrid-epModule-list').datagrid('getRows');
	var code = document.getElementById('code').value;
	if(code == lbsj.code){
		$.messager.progress('close');
		$.messager.alert('提示','该编号已存在，请重新输入','error');
		return false;
	}
	*/
	var o =$('#datagrid-item-addoredit').datagrid('getData'); 
	var datas = JSON.stringify(o);   
	$.messager.progress('close');
  	$.ajax({
	 	url: ctx + '/manager/ep/epModule/addEpModule/' + type,  
        type: 'post',
        data: $('#form-epModule-addoredit').serialize() + "&datas=" + datas,   
        dataType:"json",
        success: function (data) {
			try{
				if(data.success){ 
					$dialog.dialog('destroy');//关闭弹出框  
					$.messager.alert('提示', data.message ,'info');
					var epModuleId =data.epModuleId;
					var url = ctx + '/manager/ep/epModule/getEpModuleItem/' + epModuleId;
					$('#datagrid-item-addoredit').datagrid({url:url});
					$('#datagrid-item-addoredit').datagrid('reload');
					$('#datagrid-epModule-list').datagrid('reload'); 
					$('#id').val(epModuleId);
					if(type == 1){
						$('#isDefault').combobox({
							data: [{id: "1", text: "是"},{id: "0", text: "否"}],
							valueField: "id",   
							textField: "text",
							panelHeight : 50,
							editable: false
						});
					}
				}else{
					$.messager.alert('提示',data.msg,'error');
				}
			}catch (e) {
				$.messager.alert('提示',data,'error'); 
			} 
       	}
      });
}
function saveSecondItem() {
	$.messager.progress();
	RowEditor.accept('datagrid-secondItem-addoredit');  
	var rows = $('#datagrid-secondItem-addoredit').datagrid('getRows');
	if(rows.length == 0) {
		$.messager.progress('close');
		$.messager.alert('提示','明细数据项不能为空','error');
		return false;
	}
	var o =$('#datagrid-secondItem-addoredit').datagrid('getData'); 
	var datas = JSON.stringify(o);   
	$.messager.progress('close');
	$.ajax({
		url: ctx + '/manager/ep/epModule/saveSecondItem',  
		type: 'post',
		data: $('#form-epModuleItem-search').serialize() + "&datas=" + datas,   
		dataType:"json",
		success: function (data) {
			try{
				if(data.success){ 
					$.messager.alert('提示', data.message ,'info');
					$('#datagrid-secondItem-addoredit').datagrid('reload'); 
				}else{
					$.messager.alert('提示',data.msg,'error');
				}
			}catch (e) {
				$.messager.alert('提示',data,'error'); 
			} 
		}
	});
}

function codeFmt(v,r,i){
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showEpModuleDetail('+ r.id +');">' + v + '</a>';
}

function addSecondItemFmt(v,r,i){
	if(r.id > 0){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="addSecondItem('+ r.id +');">添加</a>';
	}
}



