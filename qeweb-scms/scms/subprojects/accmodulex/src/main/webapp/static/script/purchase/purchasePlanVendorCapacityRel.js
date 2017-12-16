function showdialog(id,type){
	/**
	 * 显示新增和编辑页面
	 */
	var url ="/manager/order/purchasePlanVendorCapacityRel/capacityRel/"+id;
	if(type == "add"){
		new dialog().showWin($.i18n.prop('新增'), 600, 480, ctx + url); 
	}else{
		new dialog().showWin($.i18n.prop('编辑'), 600, 480, ctx + url); 
	}
	
}
function lookUser(){
	$('#kk').window('open');
	$('#form2').form('reset');
	$('#datagridss').datagrid({url: ctx+'/manager/admin/org/getVendorList'
	});
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
//		$.messager.alert('提示','没有选择任何记录！','info');
		$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
		return false;
	}
	
	var rows = $("#vendorCapacity").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		for(j = 0; j < selections.length; j ++) {
			if(rows[i].vendor.id ==selections[j].id){
//				$.messager.alert('提示',selections[j].name+'已存在列表中，请修改选择项目！','info');
				$.messager.alert($.i18n.prop('label.remind'),selections[j].name+$.i18n.prop('vendor.purchaseJs.ExitedModifyProject12'),'info');
				return false;
			}
		}
	}
	
	selectionsIds = "";
	selectionsTexts = "";
	for(i = 0; i < selections.length; i ++) {
		selectionsIds=selectionsIds+selections[i].id + ",";
		selectionsTexts=selectionsTexts+selections[i].name + ",";
	} 
	$("#companyId").val(selectionsIds);
	$("#companyName").textbox('setText',selectionsTexts);
	$('#kk').window('close');
}

function submitCapacity(type) {
	var codes="";
	var capacityId= $("#capacityId").val();
	var companyIds =$("#companyId").val();     
	var abolished =$("#abolished").val();
	var inputs=document.getElementById("myTable_id").getElementsByTagName("input");
	var needArray=new Array();
	for(var i=0;i<inputs.length;i++){
		if(inputs[i].type=="checkbox"){
			if(inputs[i].checked){
				//收集信息
				codes=codes+ inputs[i].name+",";
				needArray.push(inputs[i]);
			}
		}
	}

	if(needArray.length == 0	){
//		$.messager.alert('提示','请选择产能表信息！','info');
		$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('vendor.purchaseJs.ChoiceInfomation13'),'info');
		return false;
	}
	
	
	var data = {	
			capacityId :capacityId,
			vendorIds : companyIds,
			codes:codes,
			abolished :abolished
	}
	
	var url = "";
	if(type == "add"){
		url = ctx+'/manager/order/purchasePlanVendorCapacityRel/add';
	}else{
		 url = ctx+'/manager/order/purchasePlanVendorCapacityRel/update';
	}
	$.messager.progress({
//		title:'提示',
//		msg : '提交中...'
		title:$.i18n.prop('label.remind'),
		msg : $.i18n.prop('label.insubmit')
	});
  	$.ajax({
  		url:url,
        type: 'post',
        data: data, 
        dataType:"json",
        success: function (data) {   
			$.messager.progress('close');
			var obj = data;
			try{
				if(obj.success){ 
					$.messager.show({
						title:$.i18n.prop('label.news')/*'消息'*/,
						msg:  $.i18n.prop('receive.message16')/*"操作成功"*/, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#vendorCapacity').datagrid('reload');
				}else{
					$.messager.alert($.i18n.prop('label.remind'),obj.message,'error');
				}
			}catch (e) {
				$.messager.alert($.i18n.prop('label.remind'),e,'error');/* 提示*/
			} 
        }
      });

}

function deleteCapacity() {
	var selections = $('#vendorCapacity').datagrid('getSelections');
	if(selections.length==0){
//		$.messager.alert('提示','没有选择任何记录！','info');
		$.messager.alert($.i18n.prop('label.remind'),$.i18n.prop('label.not.choose.record'),'info');
		return false;
	}
	var params = $.toJSON(selections);
	url = ctx+'/manager/order/purchasePlanVendorCapacityRel/delete';
//	$.messager.confirm('提示','确定要删除？<font style="color: #F00;font-weight: 900;"></font>',function(r){
	$.messager.confirm($.i18n.prop('label.remind'),$.i18n.prop('label.sureToDelete')+'<font style="color: #F00;font-weight: 900;"></font>',function(r){
		if(r){
			$.messager.progress({
				title:$.i18n.prop('label.remind'),/*'提示'*/
				msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
			});
			
		  	$.ajax({
		  		url:url,
		        type: 'post',
		        data: params, 
		        dataType:"json",
		    	contentType : 'application/json',
		        success: function (data) {   
					$.messager.progress('close');
					var obj = data;
					try{
						if(obj.success){ 
							$.messager.show({
								title:$.i18n.prop('label.news')/*'消息'*/,
								msg:  $.i18n.prop('receive.message16')/*"操作成功"*/, 
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#vendorCapacity').datagrid('reload');
						}else{
							$.messager.alert($.i18n.prop('label.remind'),obj.message,'error');
						}
					}catch (e) {
						$.messager.alert($.i18n.prop('label.remind'),e,'error'); 
					} 
		        }
		      });

		}
	});


}
