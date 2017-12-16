
var FactoryInventory = {
	operateFmt : function(v,r,i){
		if(r.abolished==0)
		{
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="FactoryInventory.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
		}
		else
		{
			return '';
		}
	},
	query : function(){
		var searchParamArray = $('#form-factoryInventoryRel-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-factoryInventoryRel-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-factoryInventoryRel-addoredit').window({
			iconCls:'icon-edit',
			title:$.i18n.prop('verdor.organizationStructureJs.editRelationshipLnventory')/*'编辑工厂和库存地点的关系'*/
		});
		$('#code').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-factoryInventoryRel-addoredit').window('open');
		$('#form-factoryInventoryRel-addoredit').form('load',ctx+'/manager/basedata/factoryInventoryRel/getFactoryInventoryRel/'+id);
		$('#win-factoryInventoryRel-addoredit').window('autoSize');
	},
	abolish : function(){
		var selections = $('#datagrid-factoryInventoryRel-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.areWantPlantInventory')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要废除该工厂和库存地点的关系吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('base.Mail.Already.Exists.InvalidMail')/*'存在已作废的记录！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/factoryInventoryRel/abolishBatch',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						
							$.messager.show({
								title:$.i18n.prop('label.news')/*'消息'*/,
								msg:$.i18n.prop('verdor.organizationStructureJs.abolishFactoryLocation')/*'废除工厂和库存地点成功'*/,
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-factoryInventoryRel-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	//暂时不用
	del : function(){
		var selections = $('#datagrid-factoryInventoryRel-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.notRecoverableDeleteLocation')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){/*该操作不可恢复,确定要删除该工厂和库存地点关系吗？*/
			if(r){
				$.messager.progress({
					title:$.i18n.prop('label.remind')/*'提示'*/,
					msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/basedata/factoryInventoryRel/deleteFactoryInventoryRel',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('verdor.organizationStructureJs.removeLocationsSuccessfully')/*'删除工厂和库存地点成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-factoryInventoryRel-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-factoryInventoryRel-addoredit').window({
			iconCls:'icon-add',
			title:$.i18n.prop('verdor.organizationStructureJs.newFactoryLocationRelationship')/*'新增工厂和库存地点关系'*/
		});
		//获取工厂下拉列表
		  $('#combobox_factory').combobox({ 
			  url:ctx+'//manager/basedata/factory/getEffectiveFactorySelect',
	        editable:false,
	        cache: false,
	        valueField:'value',   
	        textField:'text',
	   }); 
		//获取库存地点下拉列表
		  $('#combobox_inventory').combobox({ 
			  url:ctx+'//manager/basedata/inventoryLocation/getEffectiveInventoryLocationSelect',
	        editable:false,
	        cache: false,
	        valueField:'value',   
	        textField:'text',
	   }); 
		
		$('#form-factoryInventoryRel-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#code').textbox('enable');
		$('#win-factoryInventoryRel-addoredit').window('open');
		$('#win-factoryInventoryRel-addoredit').window('autoSize');
	},
	
	submit : function(){
		var url = ctx+'/manager/basedata/factoryInventoryRel/addNewFactoryInventoryRel';
		var sucMeg = $.i18n.prop('verdor.organizationStructureJs.addInventoryRelationship');/*'添加工厂和库存地点关系成功！'*/
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/basedata/factoryInventoryRel/update';
			sucMeg = $.i18n.prop('verdor.organizationStructureJs.editStockLocation');/*'编辑工厂和库存地点成功！'*/
		}
		$.messager.progress({
			title:$.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-factoryInventoryRel-addoredit').form('submit',{
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
					$('#win-factoryInventoryRel-addoredit').window('close');
					$('#datagrid-factoryInventoryRel-list').datagrid('reload'); 
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
		$('#remark').textbox('setValue','');
	}
};


