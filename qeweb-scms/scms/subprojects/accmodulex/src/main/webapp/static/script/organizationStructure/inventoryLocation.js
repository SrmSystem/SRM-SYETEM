
var Inventory = {
	operateFmt : function(v,r,i){
		if(r.abolished==0)
		{
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Inventory.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
		}
		else
		{
			return '';
		}
	},
	query : function(){
		var searchParamArray = $('#form-inventory-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-inventory-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-inventory-addoredit').window({
			iconCls:'icon-edit',
			title:$.i18n.prop('verdor.organizationStructureJs.editInventoryLocation')/*'编辑库存地点'*/
		});
		$('#code').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-inventory-addoredit').window('open');
		$('#form-inventory-addoredit').form('load',ctx+'/manager/basedata/inventoryLocation/getInventoryLocation/'+id);
		$('#win-inventory-addoredit').window('autoSize');
	},
	abolish : function(){
		var selections = $('#datagrid-inventory-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.sureWantInventory')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要废除该库存地点吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('base.Mail.Already.Exists.InvalidMail')/*'存在已作废的记录！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/inventoryLocation/abolishBatchInventoryLocation',
					type:'POST',
					data:params,
					contentType : 'application/json',
					dataType:"json",
					success:function(data1){
						$.messager.progress('close');
						try{
							var result = data1;
							if(result.success){
								$.messager.show({
									title:$.i18n.prop('label.news')/*'消息'*/,
									msg:$.i18n.prop('verdor.organizationStructureJs.abolishInventoryLocation')/*'废除库存地点成功'*/,
									timeout:2000,
									showType:'show',
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop,
										bottom:''
									}
								});
								$('#datagrid-inventory-list').datagrid('reload');
							}else{
								$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,result.msg,'error');
							}
							}catch (e) {
								$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data,'error');
							}	
							
					}
				});
			}
			
			
		});
		
		
	},
	//暂时不用
	del : function(){
		var selections = $('#datagrid-inventory-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.notRecoverableDelete')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){/*该操作不可恢复,确定要删除该库存地点吗？*/
			if(r){
				$.messager.progress({
					title:$.i18n.prop('label.remind')/*'提示'*/,
					msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/basedata/inventoryLocation/deleteInventoryLocation',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg: $.i18n.prop('verdor.organizationStructureJs.deleteInventoryLocation')/*'删除库存地点成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-inventory-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-inventory-addoredit').window({
			iconCls:'icon-add',
			title:$.i18n.prop('verdor.organizationStructureJs.newStockLocation')/*'新增库存地点'*/
		});
		$('#form-inventory-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#code').textbox('enable');
		$('#win-inventory-addoredit').window('open');
		$('#win-inventory-addoredit').window('autoSize');
	},
	
	submit : function(){
		var url = ctx+'/manager/basedata/inventoryLocation/addNewInventoryLocation';
		var sucMeg = $.i18n.prop('verdor.organizationStructureJs.addInventoryLocation');/*'添加库存地点成功！'*/
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/basedata/inventoryLocation/update';
			sucMeg = $.i18n.prop('verdor.organizationStructureJs.editInventoryLocation');/*'编辑库存地点成功！'*/
		}
		$.messager.progress({
			title:$.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-inventory-addoredit').form('submit',{
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
					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,sucMeg,'info');
					$('#win-inventory-addoredit').window('close');
					$('#datagrid-inventory-list').datagrid('reload');
				}else{
					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,result.msg,'error');
				}
				}catch (e) {
					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data,'error');
				}
			}
			
		});
	},
	reset  : function(){
		$('#name').textbox('setValue','');
		$('#address').textbox('setValue','');
		$('#remark').textbox('setValue','');
	}
};


