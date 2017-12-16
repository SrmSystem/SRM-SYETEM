
var Factory = {
	operateFmt : function(v,r,i){
		if(r.abolished==0)
		{
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Factory.edit('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
		}
		else
		{
			return '';
		}
	},
	query : function(){
		var searchParamArray = $('#form-factory-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-factory-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-factory-addoredit').window({
			iconCls:'icon-edit',
			title:$.i18n.prop('vendor.basedataJs.EditFactory')/*'编辑工厂'*/
		});
		$('#code').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-factory-addoredit').window('open');
		$('#form-factory-addoredit').form('load',ctx+'/manager/basedata/factory/getFactory/'+id);
		$('#win-factory-addoredit').window('autoSize');
	},
	abolish : function(){
		var selections = $('#datagrid-factory-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('vendor.basedataJs.SureCancleFactory')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要废除该工厂吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('base.Mail.Already.Exists.InvalidMail')/*'存在已作废的记录！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/factory/abolishBatch',
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
									msg:$.i18n.prop('vendor.basedataJs.TovoidFactorySuccess')/*'废除工厂成功'*/,
									timeout:2000,
									showType:'show',
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop,
										bottom:''
									}
								});
								$('#datagrid-factory-list').datagrid('reload');
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
		var selections = $('#datagrid-factory-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.sureEnterFactory')+'<br/><font style="color: #F00;font-weight: 900;"></font>',function(r){/*确定要生效该工厂吗？*/
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='0'){
						$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('vendor.basedataJs.RecordOfEexistenceItNoRepeated')/*'存在生效的记录！无法重复生效 ！'*/,'info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/basedata/factory/effectBatch',
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
									msg:$.i18n.prop('verdor.organizationStructureJs.successfulFactory')/*'生效工厂成功'*/,
									timeout:2000,
									showType:'show',
									style:{
										right:'',
										top:document.body.scrollTop+document.documentElement.scrollTop,
										bottom:''
									}
								});
								$('#datagrid-factory-list').datagrid('reload');
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
		var selections = $('#datagrid-factory-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.organizationStructureJs.operationAreDelete')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){/*该操作不可恢复,确定要删除该工厂吗？*/
			if(r){
				$.messager.progress({
					title:$.i18n.prop('label.remind')/*'提示'*/,
					msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/basedata/factory/deleteFactory',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('vendor.basedataJs.News')/*'消息'*/,
							msg:$.i18n.prop('vendor.basedataJs.DeleteFactorySuccess')/*'删除工厂成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-factory-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-factory-addoredit').window({
			iconCls:'icon-add',
			title:$.i18n.prop('vendor.basedataJs.AddNewFactory')/*'新增工厂'*/
		});
		$('#form-factory-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#code').textbox('enable');
		$('#win-factory-addoredit').window('open');
		$('#win-factory-addoredit').window('autoSize');
	},
	
	submit : function(){
		var url = ctx+'/manager/basedata/factory/addNewFactory';
		var sucMeg = $.i18n.prop('vendor.basedataJs.AddFactorySuccess');/*'添加工厂成功！'*/
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/basedata/factory/update';
			sucMeg = $.i18n.prop('vendor.basedataJs.EditFactorySuccess');/*'编辑工厂成功！'*/
		}
		$.messager.progress({
			title:$.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-factory-addoredit').form('submit',{
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
					$('#win-factory-addoredit').window('close');
					$('#datagrid-factory-list').datagrid('reload');
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


