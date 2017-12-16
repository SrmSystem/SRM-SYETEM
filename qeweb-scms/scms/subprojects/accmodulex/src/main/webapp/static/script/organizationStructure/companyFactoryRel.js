
var CompanyFactory = {
	operateFmt : function(v,r,i){
		if(r.abolished==0)
		{
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="CompanyFactory.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
		}
		else
		{
			return '';
		}
	},
	query : function(){
		var searchParamArray = $('#form-companyFactoryRel-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-companyFactoryRel-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-companyFactoryRel-addoredit').window({
			iconCls:'icon-edit',
			title:$.i18n.prop('verdor.organizationStructureJs.editRelationshipFactory')/*'编辑公司与工厂的关系'*/
		});
		$('#code').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-companyFactoryRel-addoredit').window('open');
		$('#form-companyFactoryRel-addoredit').form('load',ctx+'/manager/basedata/companyFactoryRel/getCompanyFactoryRel/'+id);
		$('#win-companyFactoryRel-addoredit').window('autoSize');
	},
	abolish : function(){
		var selections = $('#datagrid-companyFactoryRel-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.sureWantBetweenFactory')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要废除该公司和工厂的关系吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('base.Mail.Already.Exists.InvalidMail')/*'存在已作废的记录！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/companyFactoryRel/abolishBatch',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						
							$.messager.show({
								title:$.i18n.prop('label.news')/*'消息'*/,
								msg:$.i18n.prop('verdor.organizationStructureJs.abolishCompaniesFactories')/*'废除公司和工厂成功'*/,
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-companyFactoryRel-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	//暂时不用
	del : function(){
		var selections = $('#datagrid-companyFactoryRel-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.operationDeleteCompany')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){/*该操作不可恢复,确定要删除该公司和采购组织关系吗？*/
			if(r){
				$.messager.progress({
					title:$.i18n.prop('label.remind')/*'提示'*/,
					msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/basedata/companyFactoryRel/deleteCompanyFactoryRel',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('verdor.organizationStructureJs.deleteCompanyfactorySuccess')/*'删除公司与工厂成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-companyFactoryRel-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-companyFactoryRel-addoredit').window({
			iconCls:'icon-add',
			title:$.i18n.prop('verdor.organizationStructureJs.newCompanyfactoryRelationship')/*'新增公司和工厂关系'*/
		});
		//获取公司下拉列表
		  $('#combobox_company').combobox({ 
			  url:ctx+'//manager/basedata/company/getEffectiveCompanySelect',
	        editable:false,
	        cache: false,
	        valueField:'value',   
	        textField:'text',
	   }); 
		//获取工厂下拉列表
		  $('#combobox_factory').combobox({ 
			  url:ctx+'/manager/basedata/factory/getFactroyItem',
	        editable:false,
	        cache: false,
	        valueField:'value',   
	        textField:'text',
	   }); 
		
		$('#form-companyFactoryRel-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#code').textbox('enable');
		$('#win-companyFactoryRel-addoredit').window('open');
		$('#win-companyFactoryRel-addoredit').window('autoSize');
	},
	
	submit : function(){
		var url = ctx+'/manager/basedata/companyFactoryRel/addNewCompanyFactoryRel';
		var sucMeg = $.i18n.prop('verdor.organizationStructureJs.addCompanyfactorySuccess')/*'添加公司和工厂成功！'*/;
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/basedata/companyFactoryRel/update';
			sucMeg = $.i18n.prop('verdor.organizationStructureJs.editorialFactorySuccessful')/*'编辑公司工厂成功！'*/;
		}
		$.messager.progress({
			title:$.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-companyFactoryRel-addoredit').form('submit',{
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
					$('#win-companyFactoryRel-addoredit').window('close');
					$('#datagrid-companyFactoryRel-list').datagrid('reload'); 
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


