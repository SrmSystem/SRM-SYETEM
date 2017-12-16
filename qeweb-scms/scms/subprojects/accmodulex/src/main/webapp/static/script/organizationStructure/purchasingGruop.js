
var Group = {
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
		var searchParamArray = $('#form-group-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-group-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-group-addoredit').window({
			iconCls:'icon-edit',
			title:$.i18n.prop('verdor.organizationStructureJs.editorialPurchasingUnit')/*'编辑采购组'*/
		});
		$('#code').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-group-addoredit').window('open');
		$('#form-group-addoredit').form('load',ctx+'/manager/basedata/purchasingGroup/getPurchasingGroup/'+id);
		$('#win-group-addoredit').window('autoSize');
	},
	abolish : function(){
		var selections = $('#datagrid-group-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.sureWantGroup')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要废除该采购组吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('base.Mail.Already.Exists.InvalidMail')/*'存在已作废的记录！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/purchasingGroup/abolishBatch',
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
									title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
									msg:$.i18n.prop('verdor.organizationStructureJs.abolishGroupSuccess')/*'废除采购组成功'*/,
									timeout:2000,
									showType:'show',
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop,
										bottom:''
									}
								});
								$('#datagrid-group-list').datagrid('reload');
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
	
	
	
	effect : function(){
		var selections = $('#datagrid-group-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.wantEnterPurchasingGroup')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要生效该采购组吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='0'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('vendor.basedataJs.RecordOfEexistenceItNoRepeated')/*'存在生效的记录！无法重复生效 ！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/purchasingGroup/effectBatch',
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
									title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
									msg:$.i18n.prop('verdor.organizationStructureJs.successfulPurchasingGroup')/*'生效采购组成功'*/,
									timeout:2000,
									showType:'show',
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop,
										bottom:''
									}
								});
								$('#datagrid-group-list').datagrid('reload');
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
		var selections = $('#datagrid-group-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.restoredDeleteGroup')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){/*该操作不可恢复,确定要删除该采购组吗？*/
			if(r){
				$.messager.progress({
					title:$.i18n.prop('label.remind')/*'提示'*/,
					msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/basedata/purchasingGroup/deletePurchasingGroup',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
							msg:$.i18n.prop('verdor.organizationStructureJs.deleteGroupSuccessfully')/*'删除采购组成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-group-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-group-addoredit').window({
			iconCls:'icon-add',
			title:$.i18n.prop('verdor.organizationStructureJs.newProcurementUnit')/*'新增采购组'*/
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
		
		$('#form-group-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#code').textbox('enable');
		$('#win-group-addoredit').window('open');
		$('#win-group-addoredit').window('autoSize');
	},
	
	submit : function(){
		var url = ctx+'/manager/basedata/purchasingGroup/addNewPurchasingGroup';
		var sucMeg = $.i18n.prop('verdor.organizationStructureJs.addGroupRelationship');/*'添加采购组关系成功！'*/
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/basedata/purchasingGroup/update';
			sucMeg = $.i18n.prop('verdor.organizationStructureJs.editGroupSuccess');/*'编辑采购组成功！'*/
		}
		$.messager.progress({
			title:$.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-group-addoredit').form('submit',{
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
					$('#win-group-addoredit').window('close');
					$('#datagrid-group-list').datagrid('reload');
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


