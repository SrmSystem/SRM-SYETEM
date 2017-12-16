
var GroupOrg = {
	operateFmt : function(v,r,i){
		if(r.abolished==0)
		{
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Group.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
		}
		else
		{
			return '';
		}
	},
	query : function(){
		var searchParamArray = $('#form-groupOrg-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-groupOrg-list').datagrid('load',searchParams);
	},
	abolish : function(){
		var selections = $('#datagrid-groupOrg-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.group'sRelationshipOrganization')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要废除该采购组与采购组织关系吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('base.Mail.Already.Exists.InvalidMail')/*'存在已作废的记录！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/groupOrganizationRel/abolishBatch',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						
							$.messager.show({
								title:$.i18n.prop('label.news')/*'消息'*/,
								msg:$.i18n.prop('verdor.organizationStructureJs.toRelationshipProcurementOrganizations')/*'废除采购组与采购组织关系成功'*/,
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-groupOrg-list').datagrid('reload');
					}
				});
			}
			
			
		});
	}
};


