var tyu=null;
var vendorPerforIndex = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	blistssssss : function() {
		var searchParamArray = $('#form2').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#blistssssss').datagrid('load', searchParams);
	},
	vfmt : function(v,r,i){
		if(r.abolished == 1){
			return '';
		}
		return '<button class="btn-link" onclick="vendorPerforIndex.update('+r.id+')">修改</button>';
	},
	addGS : function(){
		var numbers=parseInt($("#numbers").val());
		var string='<tr id="trid'+numbers+'" style="height: 200px"><td><input class="easyui-combobox" id="cycleId'+numbers+'" name="cycleId-'+numbers+'" data-options="required:true,editable:false"/></td><td>'+
					'<input class="easyui-textbox" id="content'+numbers+'" name="content'+numbers+'" type="text" style="width:100%;height: 100%" data-options="required:true,height:40,multiline:true"/></td>'+
					'<td><a id="editid'+numbers+'" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:\'icon-edit\'" onclick="vendorPerforIndex.edit('+numbers+')">导入元素</a>'+
					'<a id="deltedg'+numbers+'" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:\'icon-delete\'" onclick="vendorPerforIndex.deleteGS('+numbers+')">删除</a></td>'+
		           '</tr>'
					;
		$("#apk").append(string);
		$('#cycleId'+numbers).combobox({    
			url:ctx+'/manager/vendor/performance/index/getVendorPerforCycle',    
			valueField:'id',    
			textField:'text' ,
			onSelect: function(rec){    
				$("input[name='cycleId']").val(rec.id);   
			}
		});
		
		$.parser.parse($('#content'+numbers).parent());
		$.parser.parse($('#editid'+numbers).parent());
		$.parser.parse($('#deltedg'+numbers).parent());
		$.parser.parse($('#cycleId'+numbers).parent());
		numbers=numbers+1;
		$("#numbers").val(numbers)
	},
	addGSu : function(){
		var numbers=parseInt($("#numbers").val());
		var string='<tr id="trid'+numbers+'" style="height: 200px"><td><input class="easyui-combobox" id="cycleId'+numbers+'" name="cycleId-'+numbers+'" data-options="required:true,editable:false"/></td><td>'+
		'<input class="easyui-textbox" id="content'+numbers+'" name="content'+numbers+'" type="text" style="width:100%;height: 100%" data-options="required:true,height:40,multiline:true"/></td>'+
		'<td><a id="editid'+numbers+'" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:\'icon-edit\'" onclick="vendorPerforIndex.edit('+numbers+')">导入元素</a>'+
		'<a id="deltedg'+numbers+'" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:\'icon-delete\'" onclick="vendorPerforIndex.deleteGS('+numbers+')">删除</a></td>'+
		'</tr>'
		;
		$("#apku").append(string);
		$('#cycleId'+numbers).combobox({    
			url:ctx+'/manager/vendor/performance/index/getVendorPerforCycle',    
			valueField:'id',    
			textField:'text' ,
			onSelect: function(rec){    
				$("input[name='cycleId']").val(rec.id);   
			}
		});
		
		$.parser.parse($('#content'+numbers).parent());
		$.parser.parse($('#editid'+numbers).parent());
		$.parser.parse($('#deltedg'+numbers).parent());
		$.parser.parse($('#cycleId'+numbers).parent());
		numbers=numbers+1;
		$("#numbers").val(numbers)
	},
	deleteGS :function(i){
		$("#trid"+i).remove();
	},
	edit : function(i){
		$("#numberGS").val("");
		$("#numberGS").val(i);
		$('#dialog-vendorPerforIndex-purchase').window('open');
		$('#blistssssss').datagrid({url: ctx+'/manager/vendor/fAllocation'});
	},
	seer : function(ty){
		tyu=ty;
		$('#dialog-weidu-purchase').window('open');
		$('#treegrid-dimen-list').treegrid({url: ctx+'/manager/vendor/performance/dimensions/getSetting'});
	},
	xuanzhe1 : function(){
		var numberGS=$("#numberGS").val();
		var content=$("#content"+numberGS).val();
		var selections = $('#blistssssss').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		for(var i=0;i<selections.length;i++){
			content=content+"{"+selections[i]["name"]+"}";
		}
		$("#content"+numberGS).textbox('setValue',content);
		$("#dialog-vendorPerforIndex-purchase").window('close');
	},
	add : function(){
		$("#vsfid").val("");
		for(var i=0;i<$("#numbers").val();i++)
		{
			$("#trid"+i).remove();
		}
		$("#numbers").val("0");
		$('#form-vendorPerforIndex-saveUpadte').form('reset');
		$('#dialog-vendorPerforIndex-saveUpadte').window('open');
	},
	update : function(id){
		$('#updateapp').dialog({    
		    title: '指标维度',    
		    closed: false,    
		    cache: false, 
		    iconCls:'icon-save',
		    href: ctx+'/manager/vendor/performance/index/updateVendorPerforIndexStart/'+id,    
		    modal: true   
		}); 
	},
	submit : function(){
		$.messager.progress();
		var url=ctx;
		url=url+'/manager/vendor/performance/index/addVendorPerforIndex';
		$('#form-vendorPerforIndex-saveUpadte').form('submit',{
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
					var obj = JSON.parse(data);
					if(obj.success) {
						$.messager.alert('提示',obj.msg,'info');
						$('#dialog-vendorPerforIndex-saveUpadte').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示',obj.msg,'error');
						
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
		});
	},
	submit2 : function(){
		$.messager.progress();
		var url=ctx;
		url=url+'/manager/vendor/performance/index/updateVendorPerforIndex';
		$('#form-vendorPerforIndex-Upadte').form('submit',{
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
					var obj = JSON.parse(data);
					if(obj.success) {
						$.messager.alert('提示',obj.msg,'info');
						$('#updateapp').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示',obj.msg,'error');
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
		});
	},

	dels : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定作废吗？<br/>',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert('提示','存在已作废的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/performance/index/deleteVendorPerforIndex',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1') {
							$.messager.alert('提示',"作废成功",'info');
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
	deleteList : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定删除吗？<br/>',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/vendor/performance/index/deleteList',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1') {
							$.messager.alert('提示',"删除成功",'info');
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
	}
}
$(function(){
	$('#cc').combobox({    
	    url:ctx+'/manager/vendor/performance/index/getDimensions',    
	    valueField:'id',    
	    textField:'text' ,
	   	onSelect: function(rec){    
	           $("input[name='cycName']").val(rec.text);   
	   	}
	});
	$('#ccb').combobox({    
		url:ctx+'/manager/vendor/performance/index/getDimensions',    
		valueField:'id',    
		textField:'text' ,
		onSelect: function(rec){    
			$("input[name='cycName']").val(rec.text);   
		}
	});
})