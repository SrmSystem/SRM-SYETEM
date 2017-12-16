
//数据字典主项
function Dict(){
	
}

//操作格式化主项
Dict.operateFmt = function(v,r,i){
//		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Dict.showItem('+r.id+');">添加子项</a>&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Dict.edit('+r.id+');">编辑</a>';
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Dict.showItem('+r.id+');">'+$.i18n.prop('vendor.basedataJs.AddSubkey')+'</a>&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Dict.edit('+r.id+');">'+$.i18n.prop('vendor.basedataJs.Edit')+'</a>';
}

//查看子项目
Dict.showItem =function(dictId){
//	new dialog().showWin("查看子项", 700,500, ctx + '/manager/basedata/dict/showDictItemList/' + dictId);
	new dialog().showWin($.i18n.prop('vendor.basedataJs.ViewSubkey'), 700,500, ctx + '/manager/basedata/dict/showDictItemList/' + dictId);
}

//查询主项
Dict.search = function(){
	var searchParamArray = $('#form-dict-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-dict-list').datagrid('load',searchParams);
}


//添加主项
Dict.add = function(){
	$('#dict_form').form('clear');
	$('#win-dict-addoredit').window('open'); 
	$('#dictId').val(0);
	
}


//编辑主项
Dict.edit = function(id){
	var $win = $('#win-dict-addoredit');
	var $form = $('#dict_form');
	$form.form('load',ctx+'/manager/basedata/dict/findDictById/'+id);
	$win.window('open');
	
}

//提交主项
Dict.submit = function(){
	$.messager.progress();
	$('#dict_form').form('submit',{
		ajax:true,
		url:ctx+'/manager/basedata/dict/addOrUpdateDict',
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
//				$.messager.alert('提示',"操作成功",'info');
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.OperateSuccess'),'info');
				$('#win-dict-addoredit').window('close');
				$('#datagrid-dict-list').datagrid('reload');
			}else{
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');/*提示*/
			}
			}catch (e) {
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');/*提示*/
			}
		},
		error: function(data) {
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');/*提示*/
		}
	});
}

//删除主项
Dict.del = function(){
	var selections = $('#datagrid-dict-list').datagrid('getSelections');
	if(selections.length==0){
//		$.messager.alert('提示','没有选择任何记录！','info');
		$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.NochoiceRecord'),'info');
		return false;
	}
	$.messager.progress();
	var params = $.toJSON(selections);
//	$.messager.confirm("操作提示", "确定删除操作吗？", function (data) {
	$.messager.confirm($.i18n.prop('vendor.basedataJs.OperatePromit'), $.i18n.prop('vendor.basedataJs.ConfimSureOperate'), function (data) {  
		 if (data) {
				$.ajax({
					url:ctx+'/manager/basedata/dict/deleteDict',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
							$.messager.show({
								title:$.i18n.prop('vendor.basedataJs.News'),/*'消息'*/
								msg:$.i18n.prop('vendor.basedataJs.DeleteSucessful'),/*'删除成功'*/
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-dict-list').datagrid('reload');
					}
				});
		 }
	});

}



//子项目
function DictItem(){
	
}

//操作格式化子项
DictItem.operateFmt = function(v,r,i){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="DictItem.edit('+r.id+');">'+$.i18n.prop('vendor.basedataJs.Edit')+'</a>';/*编辑*/
}

//查询子项
DictItem.search = function(){
	var searchParamArray = $('#form-dictItem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-dictItem-list').datagrid('load',searchParams);
}


//添加子项
DictItem.add = function(dictId){
	$('#dictItem_form').form('clear');
	$('#win-dictItem-addoredit').window('open'); 
	$('#dictItemId').val(0);
	$('#dictId').val(dictId);
}


//编辑子项
DictItem.edit = function(dictId){
	var $win = $('#win-dictItem-addoredit');
	var $form = $('#dictItem_form');
	$form.form('load',ctx+'/manager/basedata/dict/findDictItemById/'+dictId);
	$('#dictId').val(dictId);
	$win.window('open');
	
}

//提交子项
DictItem.submit = function(){
	$.messager.progress();
	debugger;
	var dictId=document.getElementById("dictId").value;
	$('#dictItem_form').form('submit',{
		ajax:true,
		url:ctx+'/manager/basedata/dict/addOrUpdateDictItem/'+dictId,
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
//				$.messager.alert('提示',"操作成功",'info');
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.OperateSuccess'),'info');
				$('#win-dictItem-addoredit').window('close');
				$('#datagrid-dictItem-list').datagrid('reload');
			}else{
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),result.msg,'error');/*提示*/
			}
			}catch (e) {
				$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');/*提示*/
			}
		},
		error: function(data) {
			$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),data,'error');/*提示*/
		}
	});
}

//删除子项
DictItem.del = function(){
	var selections = $('#datagrid-dictItem-list').datagrid('getSelections');
	if(selections.length==0){
//		$.messager.alert('提示','没有选择任何记录！','info');
		$.messager.alert($.i18n.prop('vendor.basedataJs.Promit'),$.i18n.prop('vendor.basedataJs.NochoiceRecord'),'info');
		return false;
	}
	$.messager.progress();
	var params = $.toJSON(selections);
//	$.messager.confirm("操作提示", "确定删除操作吗？", function (data) {
	$.messager.confirm($.i18n.prop('vendor.basedataJs.OperatePromit'), $.i18n.prop('vendor.basedataJs.ConfimSureOperate'), function (data) { 
		 if (data) {
				$.ajax({
					url:ctx+'/manager/basedata/dict/deleteDictItem',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
							$.messager.show({
								title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
								msg:$.i18n.prop('vendor.basedataJs.DeleteSucessful'),/*'删除成功'*/
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-dictItem-list').datagrid('reload');
					}
				});
		 }
	});

}