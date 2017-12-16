function searchDynamicData(){
	var searchParamArray = $('#form-dynamicData-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-dynamicData-list').datagrid('load',searchParams);
}
//查看详情
function showDynamicDataDetail(id) {
	$('#win-dynamicData-addoredit').dialog({
		iconCls : 'icon-edit',
		title : '动态数据详情'
	});
	$('#dynamic-save').hide();
	$('#dynamic-item-add').hide();
	$('#dynamic-item-del').hide();
	$('#beanId').textbox('disable');
	$('#objectName').textbox('disable');
	$('#enable').textbox('disable');  
	$('#remark').textbox('disable');  
	$('#win-dynamicData-addoredit').dialog('open');
	$('#form-dynamic-addoredit').form('load', ctx+'/manager/basedata/dynamicData/getDynamicData/' + id);
	//详情
	$('#datagrid-dynamicItem-list').datagrid({   
    	url:ctx+'/manager/basedata/dynamicData/getDynamicItem/' + id     
	});
}

//新增
function addDynamicData(){
	$('#win-dynamicData-addoredit').dialog({
		iconCls:'icon-add',
		title:'新增动态数据',
		maximizable:true
	});
	$('#id').val(0);
	$('#dynamic-save').show();
	$('#dynamic-item-add').show();
	$('#dynamic-item-del').show();
	$('#beanId').textbox('enable');
	$('#objectName').textbox('enable');
	$('#enable').textbox('enable');  
	$('#remark').textbox('enable');
	$('#form-dynamic-addoredit').form('clear');
	$('#win-dynamicData-addoredit').dialog('open');
	$('#datagrid-dynamicItem-list').datagrid({   
    	url:ctx+'/manager/basedata/dynamicData/getDynamicItem/0'        
	});
}

//编辑
function editDynamicData(id) {
	$('#win-dynamicData-addoredit').dialog({
		iconCls : 'icon-edit',
		title : '编辑动态数据',
		maximizable:true
	});
	
	$('#dynamic-save').show();
	$('#dynamic-item-add').show();
	$('#dynamic-item-del').show();
	$('#beanId').textbox('disable');
	$('#objectName').textbox('enable');
	$('#enable').textbox('enable');  
	$('#remark').textbox('enable');
	$('#win-dynamicData-addoredit').dialog('open');
	$('#form-dynamic-addoredit').form('load', ctx+'/manager/basedata/dynamicData/getDynamicData/' + id);
	//详情
	$('#datagrid-dynamicItem-list').datagrid({   
    	url:ctx+'/manager/basedata/dynamicData/getDynamicItem/' + id     
	});
}

//启用禁用
function enableDynamic(status) {
	var selections = $('#datagrid-dynamicData-list').datagrid('getSelections');
	if (selections.length == 0) {
		$.messager.alert('提示', '没有选择任何记录！', 'info');
		return false;
	}
	var url = ctx+'/manager/basedata/dynamicData/enable';
	if(0 == status)
		url = ctx+'/manager/basedata/dynamicData/disable';
		
	var params = $.toJSON(selections);   
	$.messager.confirm("操作提示", "您确定要执行此操作吗？", function (data) {  
        if (data) {
        	$.ajax({
        		url: url,
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			if(data.success){
        				$.messager.show({
        					title:'消息',
        					msg: data.message,
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#datagrid-dynamicData-list').datagrid('reload');
        			}
        		}
        	});  
        }
	});
}
//添加删除行
function appendRow() {
	if (endEditing()){
		$('#datagrid-dynamicItem-list').datagrid('appendRow',{show:'是'});
		editIndex = $('#datagrid-dynamicItem-list').datagrid('getRows').length-1;
		$('#datagrid-dynamicItem-list').datagrid('selectRow', editIndex)
				.datagrid('beginEdit', editIndex);
	}
}

function deleteRow() {
	var row = $('#datagrid-dynamicItem-list').datagrid('getSelected');
	if (row) {
         var rowIndex = $('#datagrid-dynamicItem-list').datagrid('getRowIndex', row);
         $('#datagrid-dynamicItem-list').datagrid('deleteRow', rowIndex);    
	 }
}

//删除
function delDynamic() {
	var selections = $('#datagrid-dynamicData-list').datagrid('getSelections');
	if (selections.length == 0) {
		$.messager.alert('提示', '没有选择任何记录！', 'info');
		return false;
	}
	
	var params = $.toJSON(selections);   
	$.messager.confirm("操作提示", "您确定要执行此操作吗？", function (data) {  
        if (data) {
        	$.ajax({
        		url: ctx+'/manager/basedata/dynamicData/delete',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			if(data.success){
        				$.messager.show({
        					title:'消息',
        					msg: data.message,
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#datagrid-dynamicData-list').datagrid('reload');
        			}
        		}
        	});  
        }
	});
}
//保存
function saveDynamic() {  
	$.messager.progress();
	var isValid = $('#form-dynamic-addoredit').form('validate');
	if(!isValid){
		$.messager.progress('close');
		$.messager.alert('提示','数据项不能为空','error');
		return false;
	}
	
	if (endEditing()){
		$('#datagrid-dynamicItem-list').datagrid('acceptChanges');  
	}
    var rows = $('#datagrid-dynamicItem-list').datagrid('getRows');
	if(rows == null || rows.length == 0) {
			$.messager.progress('close');
			$.messager.alert('提示','请添加动态字段','error');
			return false;
    } 
	var o =$('#datagrid-dynamicItem-list').datagrid('getData'); 
	var datas = JSON.stringify(o);   
  	$.ajax({
	 	url:ctx+'/manager/basedata/dynamicData/saveDynamic',  
        type: 'post',
        data: $('#form-dynamic-addoredit').serialize() + "&datas=" + datas,   
        dataType:"json",
        success: function (data) {
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.alert('提示', data.message ,'info');
					$('#win-dynamicData-addoredit').dialog('close');
					$('#datagrid-dynamicData-list').datagrid('reload'); 
				}else{
					$.messager.alert('提示',data.message,'error');
				}
			}catch (e) {
				$.messager.alert('提示',e,'error'); 
			} 
       	}
      });
}

function importDynamic() {
	$('#form-dynamic-import').form('clear');   
	$('#win-dynamic-import').dialog('open');  
}

//保存导入
function saveimportdynamic() {
	$.messager.progress();
	$('#form-dynamic-import').form('submit',{
		ajax:true,
		iframe: true,    
		url:ctx+'/manager/basedata/dynamicData/filesUpload', 
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
				$.messager.alert('提示','导入成功','info');
				$('#win-dynamic-import').dialog('close');
				$('#datagrid-dynamicData-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
			}
			}catch (e) {  
				$.messager.alert('提示',data,'error');
			}
		}
	});
}
