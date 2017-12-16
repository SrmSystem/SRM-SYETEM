//文件协同管理
var FileCollaboation = {		
	operateFmt : function(v,r,i){
		if(r.publishStatus==0){
			var type = "'update'";
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="FileCollaboation.edit('+r.id+');">'+$.i18n.prop('button.update')+'</a>';/*修改*/
		}
		else{
			return '';
		}
	},
	viewFmt : function(v,r,i){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="FileCollaboation.view('+r.id+');">'+$.i18n.prop('button.view')+'</a>';/*查看*/
	},
	view :  function(id){
		 $dialog = $('<div/>').dialog({     
		        title: $.i18n.prop('file.fileCollaboation.fileCollaborationList')/*'文件协同列表'*/,     
		        iconCls : 'pag-search',    
		        closed: true,     
		        cache: false,     
		        href: ctx + '/manager/file/fileCollaboation/viewDetailed/' +id,     
		        modal: true,  
		        maximizable:true,
		        maximized:true,
		        onLoad:function(){  
		        	
		        },               
		        onClose:function(){
		            $(this).dialog('destroy');
		        },
		        buttons : [ 
		         ]  

		   });    
		  $dialog.dialog('open');
		
	},
	query : function(){
		var searchParamArray = $('#form-fileCollaboation-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-fileCollaboation-list').datagrid('load',searchParams);
	},
	edit : function(id){
		$('#win-fileCollaboation-addoredit').window({
			iconCls:'icon-edit',
			title:$.i18n.prop('file.fileCollaboation.editingFileCollaboration')/*'编辑文件协同'*/
		});
		$("#msg").text("");
		//获取协同类型下拉列表
		  $('#collaborationTypeCode').combobox({ 
			 url:ctx+'/manager/basedata/dict/getDict/FILE_COLLABOATION',
	        editable:false,
	        cache: false,
	        valueField:'value',   
	        textField:'text',
	   }); 
		  
		$('#form-fileCollaboation-addoredit').form('clear');
		
		$('#title').textbox('disable');
		$('#br1').css('display','none');
		$('#br2').css('display','block');
		$('#win-fileCollaboation-addoredit').window('open');
		/*$('#form-fileCollaboation-addoredit').form('load',ctx+'/manager/file/fileCollaboation/get/'+id);*/
		
		//获取
		$.ajax({
			url:ctx+'/manager/file/fileCollaboation/get/'+id,
			type:'POST',
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				if(data){
					//赋值
					$("#id").val(data.id);
					$("#title").textbox("setValue", data.title);
					$("#filePath").val(data.filePath);
					$("#collaborationTypeCode").combobox("setValue", data.collaborationTypeCode);
					$('#fileName').text(data.fileName);//模板					
					$("#validStartTime").datebox("setValue", data.validStartTime);
					$("#validEndTime").datebox("setValue", data.validEndTime);
					$("#vendorIds").val(data.vendorIds);
					$("#companyName").textbox("setValue", data.vendorNames);
					KindEditor.instances[0].html(data.describe);
					
				}
			}
		});
		
		$('#win-fileCollaboation-addoredit').window('autoSize');
	},
	del : function(){
		var selections = $('#datagrid-fileCollaboation-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}

		for(i = 0; i < selections.length; i ++) {
			if(selections[i].publishStatus == 1) {
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('file.fileCollaboation.publishedDeleted')/*'已发布的数据无法删除！'*/,'error');
				return false;
			} 
		}
		var params = $.toJSON(selections);
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,/*该操作不可恢复,确定要删除该数据吗？*/$.i18n.prop('file.fileCollaboation.operationDoWant')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){
			if(r){
				$.messager.progress({
					title:$.i18n.prop('label.remind')/*'提示'*/,
					msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
				});
				$.ajax({
					url:ctx+'/manager/file/fileCollaboation/delete',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('abnormal.abnormalback.deleteDataSuccess')/*'删除数据成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-fileCollaboation-list').datagrid('reload');
					}
				});
			}
			
			
		});
		
		
	},
	add : function(){
		$('#win-fileCollaboation-addoredit').window({
			iconCls:'icon-add',
			title:$.i18n.prop('file.fileCollaboation.newCollaboration')/*'新增文件协同'*/
		});
		
		//获取协同类型下拉列表
		  $('#collaborationTypeCode').combobox({ 
			 url:ctx+'/manager/basedata/dict/getDict/FILE_COLLABOATION',
	        editable:false,
	        cache: false,
	        valueField:'value',   
	        textField:'text',
	   }); 
		  
		$('#form-fileCollaboation-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display','block');
		$('#br2').css('display','none');
		$('#fileName').html("");
		$('#title').textbox('enable');
		$('#win-fileCollaboation-addoredit').window('open');
		$('#win-fileCollaboation-addoredit').window('autoSize');
		$("#msg").text("");
	},
	
	submit : function(){
		var url = ctx+'/manager/file/fileCollaboation/add';
		var sucMeg = $.i18n.prop('file.fileCollaboation.addData');/*'添加数据成功！'*/
		if($('#id').val()!=0 && $('#id').val()!='0'){
			url = ctx+'/manager/file/fileCollaboation/update';
			sucMeg = $.i18n.prop('file.fileCollaboation.editData');/*'编辑数据成功！'*/
		}else{
			var file = $("#file").filebox("getValue");
			if(file==null||file==''||file==undefined){
				$("#msg").text("上传附件不能为空")
				return false;
			}
		}
		$.messager.progress({
			title:$.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-fileCollaboation-addoredit').form('submit',{
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
					$('#win-fileCollaboation-addoredit').window('close');
					$('#datagrid-fileCollaboation-list').datagrid('reload');
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
		
		
		$("#collaborationTypeCode").combobox("setValue", "");
		$('#file').filebox("setValue", "");//模板					
		$("#validStartTime").datebox("setValue", "");
		$("#validEndTime").datebox("setValue", "");
		$("#vendorIds").val("");
		$("#companyName").textbox("setValue", "");
		KindEditor.instances[0].html("");
	
	},
	
	publish : function(){
		var selections = $('#datagrid-fileCollaboation-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
			return false;
		}

		for(i = 0; i < selections.length; i ++) {
			if(selections[i].publishStatus == 1) {
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('abnormal.abnormalback.publishedDataPublished')/*'已发布的数据无法重复发布！'*/,'error');
				return false;
			} 
		}
		var params = $.toJSON(selections);
		$.ajax({
			url:ctx+'/manager/file/fileCollaboation/publish',
			type:'POST',
			data:params,
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
				$.messager.show({
					title:$.i18n.prop('label.news')/*'消息'*/,
					msg:$.i18n.prop('abnormal.abnormalback.publishDataSuccess')/*'发布数据成功'*/,
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});
				$('#datagrid-fileCollaboation-list').datagrid('reload');
			}
		});

	},

};

//文件协同详细管理
var Feedback = {		
		query : function(){
			var searchParamArray = $('#form-feedback-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-feedback-list').datagrid('load',searchParams);
		},		
		viewFmt : function(v,r,i){
			if(r.backStatus == 1){
				return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Feedback.downloadFmt('+r.id+');">'+$.i18n.prop('file.fileCollaboation.download')+'</a>';/*下载*/
			}else{
				return "";
			}
		},
		downloadFmt :function(id){
			var url = ctx+'/manager/file/fileCollaboation/downloadFeedbackFile';
			var inputs = '<input type="hidden" name="billId" value="'+id+'">';
			
			jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
		    .appendTo('body').submit().remove(); 
		},
};





//组织选择
function lookUser(){
	$('#kk').window('open');
	$('#form2').form('reset');
	$('#datagridss').datagrid({url: ctx+'/manager/admin/org/getVendorList'
	,queryParams : {}});
}
function addsearch() {
	var searchParamArray = $('#form2').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagridss').datagrid("load",searchParams);
}
function xuanzhe() {
	$("#companyId").val("");
	var selections = $('#datagridss').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.not.choose.record')/*'没有选择任何记录！'*/,'info');
		return false;
	}
	
	
	selectionsIds = "";
	selectionsTexts = "";
	for(i = 0; i < selections.length; i ++) {
		selectionsIds=selectionsIds+selections[i].id + ",";
		selectionsTexts=selectionsTexts+selections[i].name + ",";
	} 
	$("#vendorIds").val(selectionsIds);
	$("#companyName").textbox('setText',selectionsTexts);
	$('#kk').window('close');

}
function downFile(){
	var fileName = $("#fileName").html();
	var filePath  = $("#filePath").val();
	filePath = filePath.replace(/\//g,"\\");;
	File.download(filePath,fileName);
	
	/*var id = $("#id").val();
	var url = ctx+'/manager/file/fileCollaboation/downloadFile';
	var inputs = '<input type="hidden" name="billId" value="'+id+'">';
	
	jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
    .appendTo('body').submit().remove(); */
} 


