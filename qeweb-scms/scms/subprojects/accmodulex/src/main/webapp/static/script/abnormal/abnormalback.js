
//文件协同管理
var AbnormalBack = {		
	operateFmt : function(v,r,i){
		var a= "";
		var b ="";
		a='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="AbnormalBack.view('+r.id+');">'+$.i18n.prop('button.view')+'</a> &nbsp&nbsp&nbsp';/*查看*/
		b = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="AbnormalBack.updateOpt('+r.id+');">'+$.i18n.prop('button.update')+'</a>';/*修改*/
		if(r.publishStatus==0){
			var type = "'update'";
			return  a+"&nbsp"+b;
		}
		else{
			return a;
		}
	},

	
	searchOrder : function() {
			var searchParamArray = $('#form-abnormal-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-abnormal-list').datagrid('load',searchParams);
	},
	
	publish : function(){
		var ids="";
		var selections = $('#datagrid-abnormal-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
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
			url:ctx+'/manager/abnormal/abnormalFeedback/publishAbnormal',
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
				$('#datagrid-abnormal-list').datagrid('reload');
			}
		});

		
	},
	updateOpt : function(id) {
		$('#win-abnormal-addoredit').window({
			iconCls : 'icon-edit',
			title : $.i18n.prop('abnormal.abnormalback.updateExceptionInformation')/*'更新异常信息'*/
		});
		$('#br1').css('display', 'none');
		$('#br2').css('display', 'block');
		$('#win-abnormal-addoredit').window('open');
		$('#form-abnormal-addoredit').form('clear');
		
		
		 $('#name').textbox('enable');
		 $('#topYn').combobox('enable');
		 $('#effectiveStartDate').datebox('enable');
		 $('#effectiveEndDate').datebox('enable');
		 $('#select').css('display', '');
		
		//获取
		$.ajax({
			url:ctx + '/manager/abnormal/abnormalFeedback/getAbnormal/' + id,
			type:'POST',
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				if(data){
					//赋值
					$("#id").val(data.id);
					$("#abnormalFeedbackName").textbox("setValue", data.abnormalFeedbackName);//标题
			/*		$("#commentYn").combobox("setValue", data.commentYn);//是否评论
*/					$("#topYn").combobox("setValue", data.topYn);//是否置顶					
					$("#effectiveStartDate").datebox("setValue", data.effectiveStartDate);
					$("#effectiveEndDate").datebox("setValue", data.effectiveEndDate);
					$("#vendorIds").val(data.vendorIds);
					$("#companyName").textbox("setValue", data.vendorNames);
					KindEditor.instances[0].html(data.commentArea);	
			
				}
			}
		});
		
	},
	
	view : function(id) {
		$('#win-abnormal-addoredit').window({
			iconCls : 'icon-edit',
			title : $.i18n.prop('abnormal.abnormalback.updateExceptionInformation')/*'更新异常信息'*/
		});
		$('#br1').css('display', 'none');
		$('#br2').css('display', 'block');
		$('#win-abnormal-addoredit').window('open');
		$('#form-abnormal-addoredit').form('clear');
		
		
		$('#br1').css('display', 'none');
		$('#br2').css('display', 'none');
		//设置只读
		 $('#abnormalFeedbackName').textbox('disable');
		 $('#topYn').combobox('disable');
		 $('#effectiveStartDate').datebox('disable');
		 $('#effectiveEndDate').datebox('disable');
		 $('#select').css('display', 'none');
		//获取
		$.ajax({
			url:ctx + '/manager/abnormal/abnormalFeedback/getAbnormal/' + id,
			type:'POST',
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				if(data){
					//赋值
					$("#id").val(data.id);
					$("#abnormalFeedbackName").textbox("setValue", data.abnormalFeedbackName);//标题
			/*		$("#commentYn").combobox("setValue", data.commentYn);//是否评论
*/					$("#topYn").combobox("setValue", data.topYn);//是否置顶					
					$("#effectiveStartDate").datebox("setValue", data.effectiveStartDate);
					$("#effectiveEndDate").datebox("setValue", data.effectiveEndDate);
					$("#vendorIds").val(data.vendorIds);
					$("#companyName").textbox("setValue", data.vendorNames);
					KindEditor.instances[0].html(data.commentArea);	
			
				}
			}
		});
	},	
	
	
	
	
	addOpt : function() {
		$('#win-abnormal-addoredit').window({
			iconCls : 'icon-add',
			title : $.i18n.prop('abnormal.abnormalback.newExceptionInformation')/*'新增异常信息'*/
		});
		$('#win-abnormal-addoredit').form('clear');
		$('#id').val(0);
		$('#br1').css('display', 'block');
		$('#br2').css('display', 'none');
		KindEditor.instances[0].html("");	
		
		 $('#abnormalFeedbackName').textbox('enable');
		 $('#topYn').combobox('enable');
		 $('#effectiveStartDate').datebox('enable');
		 $('#effectiveEndDate').datebox('enable');
		 $('#select').css('display', '');
		$('#win-abnormal-addoredit').window('open');

	},
	
	submit : function(type) {
		//赋值
		$("#commentArea").val( KindEditor.instances[0].html());
		var url = ctx+'/manager/abnormal/abnormalFeedback/AddAbnormal';
		var sucMeg = '添加异常信息成功！';
		if ($('#id').val() != 0 && $('#id').val() != '0') {
			url = ctx+'/manager/abnormal/abnormalFeedback/editAbnormal';
			sucMeg ='编辑异常信息成功！';
		}

		$.messager.progress({
			title : $.i18n.prop('label.remind')/*'提示'*/,
			msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
		});
		$('#form-abnormal-addoredit').form('submit', {
			ajax : true,
			url : url,
			onSubmit : function() {

				var isValid = $(this).form('validate');
				if (!isValid) {
					$.messager.progress('close');
					return false;
				}
				return isValid;
			},
			success : function(data) {
				$.messager.progress('close');
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/, sucMeg, 'info');
				$('#win-abnormal-addoredit').window('close');
				$('#datagrid-abnormal-list').datagrid('reload');
				
				/*try {
					alert(data);
					var result = eval('(' + data + ')');
					if (result.success) {
						$.messager.alert($.i18n.prop('label.remind')'提示', sucMeg, 'info');
							$('#win-abnormal-addoredit').window('close');
							$('#datagrid-abnormal-list').datagrid('reload');
					} else {
						$.messager.alert($.i18n.prop('label.remind')'提示', result.msg, 'error');
					}
				} catch (e) {
					$.messager.alert($.i18n.prop('label.remind')'提示', data, 'error');
				}*/
			}
		});

	},
	reset  : function(){		
	/*	$("#commentYn").combobox("setValue", "");//是否评论
*/		$("#topYn").combobox("setValue","");//是否置顶					
		$("#effectiveStartDate").datebox("setValue", "");
		$("#effectiveEndDate").datebox("setValue", "");
		$("#vendorIds").val("");
		$("#companyName").textbox("setValue", "");
		KindEditor.instances[0].html("");	
	},
	del :function() {
		var ids="";
		var selections = $('#datagrid-abnormal-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
			return false;
		}
		
		for(i = 0; i < selections.length; i ++) {
			if(selections[i].publishStatus == 1) {
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('abnormal.abnormalback.publishedDataDeleted')/*'已发布的数据无法进行删除！'*/,'error');
				return false;
			} 
		}
		for(var i=0;i<selections.length;i++){
			ids+=selections[i].id+",";
		}
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.sureToDelete')/*'确定要删除吗？'*/,function(r){
			
		var params = $.toJSON(selections);
		if(r){
			$.messager.progress({
				title:$.i18n.prop('label.remind')/*'提示'*/,
				msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
			});
			$.ajax({
				url:ctx+'/manager/abnormal/abnormalFeedback/deleteAddAbnormalFeed',
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
					$('#datagrid-abnormal-list').datagrid('reload');
				}
			});
			
	
		}
		});
	
	}

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
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
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




