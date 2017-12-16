//公司管理
var Company = {
	operateFmt : function(v,r,i){
		if(r.abolished==0)
		{
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Company.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
		}
		else
		{
			return '';
		}
	},
	query : function(){
		var searchParamArray = $('#form-company-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-company-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-company-addoredit').window({
			iconCls:'icon-edit',
			title:$.i18n.prop('verdor.organizationStructureJs.editCompany')/*'编辑公司'*/
		});
		$('#code').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-company-addoredit').window('open');
		$('#form-company-addoredit').form('load',ctx+'/manager/basedata/company/getCompany/'+id);
		$('#win-company-addoredit').window('autoSize');
	},
	abolish : function(){
		var selections = $('#datagrid-company-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.sureRepealCompany')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要废除该公司吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('base.Mail.Already.Exists.InvalidMail')/*'存在已作废的记录！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/company/abolishBatchCompany',
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
									msg:$.i18n.prop('verdor.organizationStructureJs.repealSuccess')/*'废除公司成功'*/,
									timeout:2000,
									showType:'show',
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop,
										bottom:''
									}
								});
								$('#datagrid-company-list').datagrid('reload');
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
		var selections = $('#datagrid-company-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.operationDeleteCompany')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){/*该操作不可恢复,确定要删除该公司吗？*/
			if(r){
				$.messager.progress({
					title:$.i18n.prop('label.remind')/*'提示'*/,
					msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/basedata/company/deleteCompany',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('basedata.text.DeleteBusinessSuccessfuly')/*'删除业务范围成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						
						//$.messager.alert('提示','删除用户成功!','info');
						$('#datagrid-company-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-company-addoredit').window({
			iconCls:'icon-add',
			title:$.i18n.prop('verdor.organizationStructureJs.newCompany')/*'新增公司'*/
		});
		$('#form-company-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#code').textbox('enable');
		$('#win-company-addoredit').window('open');
		$('#win-company-addoredit').window('autoSize');
	},
	
	submit : function(){
		var url = ctx+'/manager/basedata/company/addNewCompany';
		var sucMeg = $.i18n.prop('verdor.organizationStructureJs.addCompanySuccess');/*'添加公司成功！'*/
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/basedata/company/update';
			sucMeg = $.i18n.prop('verdor.organizationStructureJs.editorialCompany');/*'编辑公司成功！'*/
		}
		$.messager.progress({
			title:$.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-company-addoredit').form('submit',{
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
					$('#win-company-addoredit').window('close');
					$('#datagrid-company-list').datagrid('reload');
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
		$('#gongsiname').textbox('setValue','');
		$('#gongsiremark').textbox('setValue','');
	}
};


