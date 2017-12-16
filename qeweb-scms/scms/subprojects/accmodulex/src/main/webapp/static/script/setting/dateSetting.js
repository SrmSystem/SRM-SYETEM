var dateSetting = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	vfmt : function(v,r,i){
		return '<button class="btn-link" onclick="dateSetting.update('+r.id+')">'+$.i18n.prop('vendor.settingJs.modify')+'</button>';/*修改*/
	},
	add : function(){
		$('#dialog-edit-form').form('reset')
		$("#id").val(0);
		$('#day').numberbox({    
			min:1,
		    max:28  
		});
		$('#dialog-edit').dialog('setTitle',$.i18n.prop('vendor.settingJs.Add'));/*新增*/
		var $form = $('#dialog-edit').getCmp('form');
		$form.form('reset');
		$('#dialog-edit').dialog('open');
		$('#dialog-edit').dialog('autoSize');
	},
	/*submit : function(){
		var $form = $('#dialog-edit').getCmp('form');
		var url = '/manager/check/checks/addDateSetting';
		SubmitUtil.form('#dialog-edit-form',url,null,function(){
				$('#datagrid').datagrid('reload');
				$('#dialog-edit').dialog('close');
		},true);
	},*/
	submit : function(){
		var url = ctx +'/manager/check/checks/addDateSetting';
		$('#dialog-edit-form').form('submit',{
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
					$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),result.msg,'info');/*提示*/
					$('#datagrid').datagrid('reload');
					$('#dialog-edit').dialog('close');
				}else{
					$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),result.msg,'error');/*提示*/
				}
				}catch (e) {
					$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),data,'error');/*提示*/
				}
			}
			
		});
	},
	update : function(id){
		$('#dialog-edit').dialog();
		$('#dialog-edit').dialog('setTitle',$.i18n.prop('vendor.settingJs.Edition'));/*'编辑'*/
		$.ajax({
			url:ctx+"/manager/check/checks/getDatesetting/"+id,
			type:'POST',
			contentType : 'application/json',
			success:function(data){
				$('#dialog-edit-form').form('reset');
				$('#day').numberbox({    
					min:1,
				    max:28  
				});
				var da= data.split(",");
				var idVal = da[0];
				if($("#id").val()!=''&&$("#id").val()!=null)
				{
					$("#id").val(idVal);
				}
				else
				{
					$("#dialog-edit-form").append('<input id="id" name="id" value="'+idVal+'" type="hidden"/>');
				}
				$("#orgId").textbox('setValue',da[1]);
				$("#orgName").textbox('setValue',da[2]);
				var s=parseInt(da[3]);
				$("#day").numberbox('setValue',s);
				if(da[4]=='0')
				{
					$('#abolished').combobox('select',"0");
				}
				else if(da[4]=='1')
				{
					$('#abolished').combobox('select',"1");
				}
				$('#dialog-edit').window('open');
				$('#dialog-edit').dialog('autoSize');
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
		});
	},
	release : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
	//		$.messager.alert('提示','没有选择任何记录！','info');
			$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.NoChoice'),'info');
			return false;
		}
		var params = $.toJSON(selections);
    //    $.messager.confirm('提示','确定启用吗？',function(r){
		 $.messager.confirm($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.MakeSureUse'),function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='0'){
	//					$.messager.alert('提示','存在已启用的记录！','info');
						$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.ExistRecord'),'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/check/checks/releaseDateSetting',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
	//						$.messager.alert('提示',"启用成功",'info');
							$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.EnableSuccess'),'info');
						}
						else
						{
	//						$.messager.alert('提示',"启用失败",'error');
							$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.EnableFail'),'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
        })
	},
	dels : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
//			$.messager.alert('提示','没有选择任何记录！','info');
			$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.NoChoice'),'info');
			return false;
		}
		var params = $.toJSON(selections);
//		$.messager.confirm('提示','确定作废吗？',function(r){
		$.messager.confirm($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.MakeTovoid'),function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
//						$.messager.alert('提示','存在已作废的记录！','info');
						$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.ExistTovoidRecord'),'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/check/checks/delsDateSetting',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
//							$.messager.alert('提示',"作废成功",'info');
							$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.ToviodSuccess'),'info');
						}
						else
						{
//							$.messager.alert('提示',"作废失败",'error');
							$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.ToviodFail'),'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	deleteDatesetting : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
//			$.messager.alert('提示','没有选择任何记录！','info');
			$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.NoChoice'),'info');
			return false;
		}
		var params = $.toJSON(selections);
//		$.messager.confirm('提示','确定删除吗？',function(r){
		$.messager.confirm($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.SureDelete'),function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/check/checks/deleteDatesettings',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
//							$.messager.alert('提示',"删除成功",'info');
							$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.DeleteSucessful'),'info');
						}
						else
						{
//							$.messager.alert('提示',"删除失败",'error');
							$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),$.i18n.prop('vendor.settingJs.DeleteFail'),'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},updateDefaultDateSetting : function(){
		$.messager.progress();
		var url = ctx +'/manager/check/checks/updateDefaultDateSetting';
		$('#formDefault').form('submit',{
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
					$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),result.msg,'info');/*提示*/
				}else{
					$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),result.msg,'error');/*提示*/
				}
				}catch (e) {
					$.messager.alert($.i18n.prop('vendor.settingJs.Prompt'),data,'error');/*提示*/
				}
			}
			
		});
	}
}