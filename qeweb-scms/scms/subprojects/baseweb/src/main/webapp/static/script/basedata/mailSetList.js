function mailSetDetailFmt(v,r,i){
	    	
	    	var view = "";
	
	if(r.mailTemplateId == 1)
		view =  '自主注册成功';
	else if(r.mailTemplateId == 2)
		view =  '邀请注册成功';
	else if(r.mailTemplateId == 3)
		view =  '意向确认通过';
	else if(r.mailTemplateId == 4)
		view =  '意向确认拒绝';	
	else if(r.mailTemplateId == 5)
		view =  '资质提醒';
	else if(r.mailTemplateId == 6)
		view =  '邀请注册模板';
	
    return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showMailSetDetail('+ r.id +');">' + view + '</a>';
 
}
	
function managerFmt(v,r,i){
	
	var name = "";
	
	if (r.abolished == 0)
		name = '编辑';
		
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editMailSet('+r.id+');">'+name+'</a>';
}


function mailAbolishedFmt(v,r,i){
	if(r.abolished == 0)
		return '有效';
	else if(r.abolished == 1)
		return '作废';
}
function searchMailSet(){
	var searchParamArray = $('#form-mailSet-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-mailSet-list').datagrid('load',searchParams);
}

function addMailSet(){
	$('#win-mailSet-addoredit').dialog({
		iconCls:'icon-add',
		title:'新增邮箱设置'
	});
	$('#code').textbox('enable');
	$('#form-mailSet-addoredit').form('clear');
	$('#id').val(0);
	$('#win-mailSet-addoredit').dialog('open');
}

function submitAddorEditmailSet(){
	var url = ctx+'/manager/basedata/mailSet/addNewMailSet';
	var sucMeg = '添加邮箱设置成功！';
		
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx+'/manager/basedata/mailSet/update';
		sucMeg = '编辑邮箱设置成功！';
	}
	$.messager.progress({
		title:'提示',
		msg : '提交中...'
	});
	$('#form-mailSet-addoredit').form('submit',{
		ajax:true,
		url:url,
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
				$.messager.alert('提示',sucMeg,'info');
				$('#win-mailSet-addoredit').dialog('close');
				$('#datagrid-mailSet-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		}
		
	});
}

function abolishMailSet(){
	var selections = $('#datagrid-mailSet-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}

	var params = $.toJSON(selections);
	$.messager.confirm("操作提示", "您确定要作废该邮箱设置吗？", function (data) {
        if (data) {
        	for(var i=0;i<selections.length;i++){
				if(selections[i]["abolished"]=='1'){
					$.messager.alert('提示','存在已作废的记录！','info');
					return false;
				}
			}
        	$.ajax({
        		url:ctx+'/manager/basedata/mailSet/abolishMailSet',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			if(data.success){
        				$.messager.show({
        					title:'消息',
        					msg:'作废邮箱设置成功',
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#datagrid-mailSet-list').datagrid('reload');
        			}
        		}
        	});
        }
    });
}

function editMailSet(id){
	$('#win-mailSet-addoredit').dialog({
		iconCls:'icon-edit',
		title:'编辑邮箱设置'
	});
	$('#mailTemplateId').combobox('disable');
	$('#win-mailSet-addoredit').dialog('open');
	$('#form-mailSet-addoredit').form('load',ctx+'/manager/basedata/mailSet/getMailSet/'+id);
}

function showMailSetDetail(id){
	$('#win-mailSet-view').window({
		iconCls:'icon-edit',
		title:'查看邮箱设置'
	});
	$('#win-mailSet-view').window('open');
	$('#form-mailSet-view').form('load',ctx+'/manager/basedata/mailSet/getMailSet/'+id);
}