var vendorMat = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	add : function(){
		$('#dialog-vendormaterialType-saveUpadte').dialog();
		$('#dialog-vendormaterialType-saveUpadte').window('open');
		$("#id").val(0);
		$("#abolished").val(0);
		$('#form-vendormaterialType-saveUpadte').form('reset');
	},
	update : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条数据','info');
			return false;
		}
		$('#dialog-vendormaterialType-saveUpadte').dialog();
		$('#dialog-vendormaterialType-saveUpadte').window('open');
		$("#id").val(selections[0].id);
		$("#abolished").val(selections[0].abolished);
		$("#reviewsId").val(selections[0].re.id);
		$("#materialtypeId").val(selections[0].mt.id);
		$("#vName").textbox('setValue',selections[0].re.orgName);
		$("#matypeName").textbox('setValue',selections[0].mt.name);
	},
	submit : function(){
		var url=ctx;
		url=url+'/manager/vendor/vendorPerforVendorMat/addUpdateVendorMat';
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});	
		$('#form-vendormaterialType-saveUpadte').form('submit',{
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
					data= JSON.parse(data);
					if(data.success) {
						$.messager.alert('提示',data.msg,'info');
						$('#dialog-vendormaterialType-saveUpadte').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示',data.msg,'info');
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	release : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
        $.messager.confirm('提示','确定启用吗？',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='0'){
						$.messager.alert('提示','存在已启用的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/vendorPerforVendorMat/releaseVendorMat',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1') {
							$.messager.alert('提示',"启用成功",'info');
						} else {
							$.messager.alert('提示',data,'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
        })
	},
	dels : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定作废吗？',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert('提示','存在已作废的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/vendorPerforVendorMat/delsVendorMat',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
							$.messager.alert('提示',"作废成功",'info');
						}
						else
						{
							$.messager.alert('提示',data,'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	xuanzhe1 : function(){
		var selection = $('#datagrid1').datagrid('getSelections');
		if(selection.length==0){
			$.messager.alert('提示','没有选择任何供应商','info');
			return false;
		}
		$("#reviewsId").val(selection[0].id);
		$("#vName").textbox('setValue',selection[0].orgName);
		$('#dialog-reviews-vendor').window('close');
	},
	xuanzhe2 : function(){
		var selection = $('#datagrid2').datagrid('getSelections');
		if(selection.length==0){
			$.messager.alert('提示','没有选择任何物料类别','info');
			return false;
		}
		$("#materialtypeId").val(selection[0].id);
		$("#matypeName").textbox('setValue',selection[0].name);
		$('#dialog-reviews-materialType').window('close');
	},
	seer : function(tyo){
		if(tyo=='1')
		{
			$('#dialog-reviews-vendor').dialog();
			$('#datagrid1').datagrid({url:ctx+'/manager/vendor/vendorPerforReviews'});
			$('#dialog-reviews-vendor').window('open');
		}
		else if(tyo=='2')
		{
			$('#dialog-reviews-materialType').dialog();
			$('#datagrid2').datagrid({url:ctx+'/manager/vendor/vendorPerforMaterialType/matypageList'});
			$('#dialog-reviews-materialType').window('open');
		}
	}
}
var reviews = {
	search : function() {
		var searchParamArray = $('#form1').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid1').datagrid('load', searchParams);
	}
}
var materialType = {
		search : function() {
			var searchParamArray = $('#form2').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid2').datagrid('load', searchParams);
		}
}