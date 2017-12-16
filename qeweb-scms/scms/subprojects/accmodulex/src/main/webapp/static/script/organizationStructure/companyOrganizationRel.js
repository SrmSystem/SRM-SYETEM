
var CompanyOrganization = {
	operateFmt : function(v,r,i){
		if(r.abolished==0)
		{
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="CompanyOrganization.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
		}
		else
		{
			return '';
		}
	},
	query : function(){
		var searchParamArray = $('#form-companyOrganizationRel-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-companyOrganizationRel-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-companyOrganizationRel-addoredit').window({
			iconCls:'icon-edit',
			title:$.i18n.prop('verdor.organizationStructureJs.relationshipCompanyProcurement')/*'编辑公司与采购组织的关系'*/
		});
		$('#code').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-companyOrganizationRel-addoredit').window('open');
		$('#form-companyOrganizationRel-addoredit').form('load',ctx+'/manager/basedata/companyOrganizationRel/getCompanyFactoryRel/'+id);
		$('#win-companyOrganizationRel-addoredit').window('autoSize');
	},
	abolish : function(){
		var selections = $('#datagrid-companyOrganizationRel-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('vendor.basedataJs.NochoiceRecord')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.determineRelationshipOrganization')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要废除该公司和采购组织的关系吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('base.Mail.Already.Exists.InvalidMail')/*'存在已作废的记录！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/companyOrganizationRel/abolishBatch',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						
							$.messager.show({
								title:$.i18n.prop('label.news')/*'消息'*/,
								msg:$.i18n.prop('verdor.organizationStructureJs.abolishOrganizations')/*'废除公司和采购组织成功'*/,
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-companyOrganizationRel-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	//暂时不用
	del : function(){
		var selections = $('#datagrid-companyOrganizationRel-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('vendor.basedataJs.NochoiceRecord')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.operationDeleteOrganization')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){/*该操作不可恢复,确定要删除该公司和采购组织关系吗？*/
			if(r){
				$.messager.progress({
					title:$.i18n.prop('label.remind')/*'提示'*/,
					msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/basedata/companyOrganizationRel/deleteCompanyOrganizationRel',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('verdor.organizationStructureJs.deletionProcurementSuccess')/*'删除公司与采购组织成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-companyOrganizationRel-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-companyOrganizationRel-addoredit').window({
			iconCls:'icon-add',
			title:$.i18n.prop('verdor.organizationStructureJs.newCompanyRelationship')/*'新增公司和采购组织关系'*/
		});
		//获取公司下拉列表
		  $('#combobox_company').combobox({ 
			  url:ctx+'//manager/basedata/company/getEffectiveCompanySelect',
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
		
		$('#form-companyOrganizationRel-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#code').textbox('enable');
		$('#win-companyOrganizationRel-addoredit').window('open');
		$('#win-companyOrganizationRel-addoredit').window('autoSize');
	},
	
	submit : function(){
		var url = ctx+'/manager/basedata/companyOrganizationRel/addNewCompanyOrganizationRel';
		var sucMeg = $.i18n.prop('verdor.organizationStructureJs.addCompanyOrganization');/*'添加公司和采购组织成功！'*/
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/basedata/companyOrganizationRel/update';
			sucMeg = $.i18n.prop('verdor.organizationStructureJs.editorialCompanyOrganization');/*'编辑公司与采购组织成功！'*/
		}
		$.messager.progress({
			title:$.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-companyOrganizationRel-addoredit').form('submit',{
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
					$('#win-companyOrganizationRel-addoredit').window('close');
					$('#datagrid-companyOrganizationRel-list').datagrid('reload'); 
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


