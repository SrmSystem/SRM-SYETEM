
function searchArea(){
	var searchParamArray = $('#form-area-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-area-list').datagrid('load',searchParams);
}

function addProvince(){
	$('#win-province-addoredit').window({
		iconCls:'icon-add',
//		title:'新增区域'
		title:$.i18n.prop('button.add')
	});
	$('#code').textbox('enable');
	$('#form-province-addoredit').form('reset');
	$('#id').val(0);
	$('#win-province-addoredit').window('open');
}
function submitAddorEditProvince(){
	var id = $('#combobox_province').combobox('getValue');
	var id1 = $('#combobox_city').combobox('getValue');
	var id2 = $('#combobox_area').combobox('getValue');
	var url = ctx+'/manager/basedata/area/addNewArea';
//	var sucMeg = '添加区域成功！';
	var sucMeg = $.i18n.prop('label.operateOK');
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx+'/manager/basedata/area/update';
//		sucMeg = '编辑区域成功！';
		sucMeg = $.i18n.prop('label.operateOK');
	}
	if(id==null||id==''){
		$('#parentId').val(0);
		$('level').val(1);
	}else if(id1==null||id1==''){
		$('#parentId').val(id);
		$('level').val(2);
	}else if(id2==null||id2==''){
		$('#parentId').val(id1);
		$('level').val(3);
	}else{
		$('#parentId').val(id2);
		$('level').val(4);
	}
	$.messager.progress({
		title:$.i18n.prop('label.remind'),
//		msg : '提交中...'
		msg : $.i18n.prop('base.Mail.Submiting')
	});
	$('#form-province-addoredit').form('submit',{
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
					$.messager.alert($.i18n.prop('label.remind'),sucMeg,'info');
					$('#win-province-addoredit').window('close');
					$('#datagrid-area-list').datagrid('reload');
				}else{
					$.messager.alert($.i18n.prop('label.remind'),result.msg,'error');
				}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),data,'error');
				}
		}
		
	});
} 
/* 
function addCountry(){
	$('#win-country-addoredit').window({
		iconCls:'icon-add',
		title:'新增国家'
	});
	$('#code').textbox('enable');
	$('#form-country-addoredit').form('reset');  
	$('#id').val(0);
	$('#win-country-addoredit').window('open');
}


function submitAddorEditarea(){
	var url = ctx+'/manager/basedata/area/addNewArea';
	var sucMeg = '添加区域成功！';
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx+'/manager/basedata/area/update';
		sucMeg = '编辑区域成功！';
	}
	$.messager.progress({
		title:$.i18n.prop('label.remind'),
		msg : '提交中...'
	});
	$('#form-country-addoredit').form('submit',{
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
					$.messager.alert($.i18n.prop('label.remind'),sucMeg,'info');
					$('#win-country-addoredit').window('close');
					$('#datagrid-area-list').datagrid('reload');
				}else{
					$.messager.alert($.i18n.prop('label.remind'),result.msg,'error');
				}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind'),data,'error');
				}
		}
		
	});
}


function editCountry(id){
	$('#win-country-addoredit').window({
		iconCls:'icon-edit',
		title:'编辑国家'
	});
	$('#code').textbox('disable');
	$('#win-country-addoredit').window('open');
	$('#form-country-addoredit').form('load',ctx+'/manager/basedata/area/getArea/'+id);
}
*/

function deleteArea(){
	var selections = $('#datagrid-area-list').datagrid('getSelections');
	if(selections.length==0){
//		$.messager.alert($.i18n.prop('label.remind'),'没有选择任何记录！','info');
		$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('delivery.message2'),'info');
		return false;
	}
	var params = $.toJSON(selections);
//	$.messager.confirm($.i18n.prop('label.remind'),'确定要删除该区域吗？',function(r){
	$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('label.sureToDelete'),function(r){
		if(r){
			$.ajax({
				url:ctx+'/manager/basedata/area/deleteArea',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
					
						$.messager.show({
							title:$.i18n.prop('label.news'),
//							msg:'删除区域成功',
							msg:$.i18n.prop('label.operateOK'),
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});

						//$.messager.alert($.i18n.prop('label.remind'),'删除用户成功!','info');
						$('#datagrid-area-list').datagrid('reload');
					
				}
			});
		}
	});
}

