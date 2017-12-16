var reviews = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	save :function(){
		$('#form-reviews-save').form('submit',{
			url:ctx+'/manager/vendor/vendorPerforReviews/releaseReviews', 
			success:function(data){
				if(data == 1) {
					$.messager.alert('提示',"参评成功",'info');
					$('#dialog-reviews-save').window('close');
					$('#datagrid').datagrid('load');
				} else {
					$.messager.alert('提示', data,'error');
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
		$.messager.confirm('提示','确定参评吗？',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["joinStatus"]=='1'){
						$.messager.alert('提示','存在已经参评的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/vendorPerforReviews/releaseLevelj',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1') {
							$.messager.alert('提示',"参评成功",'info');
						} else {
							$.messager.alert('提示',data,'info');
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
		$.messager.confirm('提示','确定取消参评吗？',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["joinStatus"]=='0'){
						$.messager.alert('提示','存在未参评的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/vendorPerforReviews/delsReviews',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
							$.messager.alert('提示',"取消参评成功",'info');
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
	xuanzhe : function(){
		$('#dialog-reviews-vendor').dialog();
		$('#ccdd').combobox({    
		    url:ctx+'/manager/vendor/vendorInfor/getVendorPhase',    
		    valueField:'id',    
		    textField:'text' ,
		   	onSelect: function(rec){    
		           $("input[name='search-LIKE_vendorPhase.name']").val(rec.text);   
		      }
		})
		$('#blistssssss').datagrid({url: ctx+'/manager/vendor/vendorInfor'});
		$('#datagrid3').datagrid({url: ctx+'/manager/vendor/performance/cycle?search-EQ_abolished=0'});
		$('#dialog-reviews-vendor').window('open');
		
	},
	remove : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定删除吗？',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/vendor/vendorPerforReviews/removeReviews',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
							$.messager.alert('提示',"删除成功",'info');
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
	showImportWin : function() {
		$('#form-reviews-import').form('clear');   
		$('#win-reviews-import').window('open');  
	},
	importSetting : function() {
		$.messager.progress();
		$('#form-reviews-import').form('submit',{
			ajax:true,
			iframe: true,    
			url: ctx + '/manager/vendor/vendorPerforReviews/imp', 
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
						$.messager.alert('提示', result.msg ,'info');
						$('#win-reviews-import').window('close');
						$('#datagrid').datagrid('reload');
					}else{
						$.messager.alert('提示', result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
					}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});
	}
}
var BaseVendor = {
	search : function() {
		var searchParamArray = $('#form2').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#blistssssss').datagrid('load', searchParams);
	},
	xuanzhe1 : function(){
		var flag = BaseVendor.validate();
		if(!flag)return false;
		var selectionsb = $('#blistssssss').datagrid('getSelections');
		if(selectionsb.length==0){
			$.messager.alert('提示','没有选择任何供应商','info');
			return false;
		}
		var params = $.toJSON(selectionsb);
		$.messager.confirm('提示','勾选的供应商默认参评，确定要参评吗？',function(r){
			if(r){
				var bps="";
				var selections = $('#datagrid3').datagrid('getSelections');
				for(var i=0;i<selections.length;i++){
					bps=bps+","+selections[i]["id"];
				}
				var url = BaseVendor.getUrl(1);
				$.messager.progress();
				$.ajax({
					url:url+bps,
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
						if(data=='1')
						{
							$.messager.alert('提示',"带回供应商成功",'info');
							$('#dialog-reviews-vendor').window('close');
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
	xuanzhe2 : function(){
		var flag = BaseVendor.validate();
		if(!flag)return false;
		$.messager.confirm('提示','根据条件带回的供应商默认参评，确定要参评吗？',function(r){
			if(r){
				var bps="";
				var selections = $('#datagrid3').datagrid('getSelections');
				for(var i=0;i<selections.length;i++){
					if(selections[i]["joinStatus"]=='1'){
						$.messager.alert('提示','存在无效的周期！','info');
						return false;
					}
					bps=bps+","+selections[i]["id"];
				}
				$.messager.progress();
				var url = BaseVendor.getUrl(2);
				$('#form2').form('submit',{
					url:url+bps,
					success:function(data){
						$.messager.progress('close');
						if(data==1)
						{
							$.messager.alert('提示',"带回供应商成功",'info');
							$('#dialog-reviews-vendor').window('close');
							$('#datagrid').datagrid('load');
						}
						else
						{
							$.messager.alert('提示',data,'error');
						}
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	xuanzhe3 : function(){
		var flag = BaseVendor.validate();
		if(!flag)return false;
		$.messager.confirm('提示','带回全部的供应商默认参评，确定要参评吗？',function(r){
			if(r){
				var bps="";
				var selections = $('#datagrid3').datagrid('getSelections');
				for(var i=0;i<selections.length;i++){
					bps=bps+","+selections[i]["id"];
				}
				var url = BaseVendor.getUrl(3)
				$.messager.progress();
				$('#formeee').form('submit',{
					url:url+bps,
					success:function(data){
						$.messager.progress('close');
						if(data==1)
						{
							$.messager.alert('提示',"带回供应商成功",'info');
							$('#dialog-reviews-vendor').window('close');
							$('#datagrid').datagrid('load');
						}
						else
						{
							$.messager.alert('提示',data,'error');
						}
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	validate : function(){
		var selections = $('#datagrid3').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何周期','info');
			return false;
		}
		return true;
	},
	getUrl : function(type){
		if(type==1){
			return ctx+'/manager/vendor/vendorPerforReviews/releaseReviews/';
		}else if(type==2){
			return ctx+'/manager/vendor/vendorPerforReviews/releaseReviews2/';
		}else if(type==3){
			return ctx+'/manager/vendor/vendorPerforReviews/releaseReviews3/';
		}
	}
}
$(function(){
	$('#cc').combobox({    
	    url:ctx+'/manager/vendor/vendorPerforReviews/getVendorPerforCycle',    
	    valueField:'id',    
	    textField:'text' ,
	   	onSelect: function(rec){    
	           $("input[name='cycid']").val(rec.id);   
	      }
	})
})