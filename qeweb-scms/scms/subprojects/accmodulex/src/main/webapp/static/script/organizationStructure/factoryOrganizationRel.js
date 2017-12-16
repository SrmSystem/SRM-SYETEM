
var FactoryOrg = {
	operateFmt : function(v,r,i){
		if(r.abolished==0)
		{
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="FactoryOrg.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
		}
		else
		{
			return '';
		}
	},
	query : function(){
		var searchParamArray = $('#form-factoryOrganization-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-factoryOrganization-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-factoryOrganization-addoredit').window({
			iconCls:'icon-edit',
			title:$.i18n.prop('verdor.organizationStructureJs.relationshipPurchasingOrganizations')/*'编辑工厂和采购组织的关系'*/
		});
		$('#code').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-factoryOrganization-addoredit').window('open');
		$('#form-factoryOrganization-addoredit').form('load',ctx+'/manager/basedata/factoryOrganizationRel/getFactoryOrganizationRel/'+id);
		$('#win-factoryOrganization-addoredit').window('autoSize');
	},
	abolish : function(){
		var selections = $('#datagrid-factoryOrganization-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.abolishProcurementOrganization')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要废除该工厂和采购组织的关系吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('base.Mail.Already.Exists.InvalidMail')/*'存在已作废的记录！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/factoryOrganizationRel/abolishBatch',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						
							$.messager.show({
								title:$.i18n.prop('label.news')/*'消息'*/,
								msg:$.i18n.prop('verdor.organizationStructureJs.abolitionOrganizationsSuccessful')/*'废除工厂和采购组织成功'*/,
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-factoryOrganization-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	//暂时不用
	del : function(){
		var selections = $('#datagrid-factoryOrganization-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.recoverableDoOrganization')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){/*该操作不可恢复,确定要删除该工厂和采购组织关系吗？*/
			if(r){
				$.messager.progress({
					title:$.i18n.prop('label.remind')/*'提示'*/,
					msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/basedata/factoryOrganizationRel/deleteFactoryOrganizationRel',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('verdor.organizationStructureJs.removePlantPurchasing')/*'删除工厂和采购组织成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-factoryOrganization-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-factoryOrganizationRel-addoredit').window({
			iconCls:'icon-add',
			title:$.i18n.prop('verdor.organizationStructureJs.newPlantRelations')/*'新增工厂和采购组织关系'*/
		});
		//获取工厂下拉列表
		  $('#combobox_factory').combobox({ 
			  url:ctx+'//manager/basedata/factory/getEffectiveFactorySelect',
	        editable:false,
	        cache: false,
	        valueField:'value',   
	        textField:'text',
	   }); 
		//获取采购组织下拉列表
		  $('#combobox_org').combobox({ 
			  url:ctx+'//manager/admin/org/getEffectiveOrgSelect',
	        editable:false,
	        cache: false,
	        valueField:'value',   
	        textField:'text',
	   }); 
		
		$('#form-factoryOrganization-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#code').textbox('enable');
		$('#win-factoryOrganization-addoredit').window('open');
		$('#win-factoryOrganization-addoredit').window('autoSize');
	},
	
	submit : function(){
		var url = ctx+'/manager/basedata/factoryOrganizationRel/addNewFactoryOrganizationRel';
		var sucMeg = $.i18n.prop('verdor.organizationStructureJs.addOrganizationSuccess');/*'添加工厂和采购组织关系成功！'*/
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/basedata/factoryOrganizationRel/update';
			sucMeg = $.i18n.prop('verdor.organizationStructureJs.editPurchasingSucceed');/*'编辑工厂和采购组织成功！'*/
		}
		$.messager.progress({
			title:$.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-factoryOrganization-addoredit').form('submit',{
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
					$('#win-factoryOrganization-addoredit').window('close');
					$('#datagrid-factoryOrganization-list').datagrid('reload');
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


