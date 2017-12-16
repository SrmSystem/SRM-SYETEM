/** 调查表模版对象 */
var SurveyTemplate = {	
	managerFmt : function(v,r,i){
	  return '<a href="javascript:;" class="btn-link" role="button" onclick="SurveyTemplate.edit('+r.id+');">编辑</a>';
	},
	typeFmt : function(v,r,i){//类型格式化
		return Constants.templateType[v];
	},
	codeFmt : function(v,r,i){//将编号转换成点击查看详情连接
		var html = '<a class="btn-link" role="button" href="javascript:;" onclick="SurveyTemplate.openSurvey('+r.id+')">'+v+'</a>';
		return html;
	},
	search : function(){
		var searchParamArray = $('#form-vendorSurveyTemplate-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-vendorSurveyTemplate-list').datagrid('load',searchParams);
	},
	add : function(){
		var $win = $('#win-vendorSurveyTemplate-addoredit');
		var $form = $('#form-vendorSurveyTemplate-addoredit');
		$win.window({
			iconCls:'icon-add',
			title:'新增调查表模版'
		});
		$('#code').textbox('enable');
		$form.form('clear');
		$('#id').val(0);
		$win.window('open');
		$win.window('autoSize');
	},
	edit : function(id){
		$('#win-vendorSurveyTemplate-addoredit').window({
			iconCls:'icon-edit',
			title:'编辑调查表模版'
		});
		$('#form-vendorSurveyTemplate-addoredit').form('load',ctx+'/manager/vendor/vendorSurveyTemplate/getVendorSurveyTemplate/'+id);
		$('#win-vendorSurveyTemplate-addoredit').window('open');
		$('#win-vendorSurveyTemplate-addoredit').window('autoSize');
	},
	del : function(){
		var selections = $('#datagrid-vendorSurveyTemplate-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		//校验下，base选项不能被删除
		var flag = true;
		$.each(selections,function(i,n){
			if(n.code=='base'){
				$.messager.alert('提示','基本信息不能被删除','warning');
				flag = false;
				return false;
			}
		});
		if(!flag)return false;
		var params = $.toJSON(selections);
		$.ajax({
			url:ctx+'/manager/vendor/vendorSurveyTemplate/deleteVendorSurveyTemplate',
			type:'POST',
			data:params,
			contentType : 'application/json',
			success:function(data){
				
					$.messager.show({
						title:'消息',
						msg:'删除调查表模版成功',
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#datagrid-vendorSurveyTemplate-list').datagrid('reload');
				
			}
		});
	},
	loading : function(){
		var selections = $('#datagrid-vendorSurveyTemplate-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		//校验下，base选项不能被删除
		var flag = true;
		$.each(selections,function(i,n){
			if(n.code=='base'){
				$.messager.alert('提示','基本信息不需要加载。。。。','warning');
				flag = false;
				return false;
			}
		});
		if(!flag)return false;
		var params = $.toJSON(selections);
		$.messager.progress();
		$.ajax({
			url:ctx+'/manager/vendor/vendorSurveyTemplate/loadingVendorSurveyTemplate',
			type:'POST',
			data:params,
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
				$.messager.show({
					title:'消息',
					msg:'加载调查表模版成功',
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});
				$('#datagrid-vendorSurveyTemplate-list').datagrid('reload');
				
			}
		});
	},
	submit : function(){
		var url = ctx+'/manager/vendor/vendorSurveyTemplate/addNewVendorSurveyTemplate';
		var sucMeg = '添加调查表模版成功！';
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/vendor/vendorSurveyTemplate/update';
			sucMeg = '编辑调查表模版成功！';
		}
		$.messager.progress();
		$('#form-vendorSurveyTemplate-addoredit').form('submit',{
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
					$.messager.alert('提示',sucMeg,'info');
					$('#win-vendorSurveyTemplate-addoredit').window('close');
					$('#datagrid-vendorSurveyTemplate-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	},
	openSurvey : function(id){//打开调查表的预览页面
		$('#window-surveyPreview').window({
			title : '调查表预览',
			href : ctx+'/manager/vendor/vendorSurveyTemplate/preview/'+id,
			width : $(document).width()-50,
			height : $(document).height()-50
		});
		$('#window-surveyPreview').window('center');
		$('#window-surveyPreview').window('open');
		
	},
	infoLoadAfter : function(data){//模版信息加载后，不允许再编辑基本信息的编码
		$code = $('#form-vendorSurveyTemplate-addoredit').getCmp('code');
		if(data.code=='base'){
			$code.textbox('disable');
		}else{
			$code.textbox('enable');
		}
		$filePathLink = $('#form-vendorSurveyTemplate-addoredit').getCmp('pathFile-link');
		$filePathLink.html('附件');
		$filePathLink.prop('title',data.fileName);
		$filePathLink.bind('click',function(){File.download(data.path,'')});;
		
	}
		
		
}

