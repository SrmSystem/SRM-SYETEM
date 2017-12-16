var ProcessPhase = {
	'1':'数据导入',
	'2':'指标计算',
	'3':'维度计算',
	'4':'总分计算',
	'5':'数据调整'
}
/** 评估执行对象 */
var VendorPerforDo = {
	/** 操作格式化 */
	optFmt : function(v,r,i){
		var html = '<a class="easyui-linkbutton" href="javascript:;" onclick="VendorPerforDo.deleteList('+r.id+')">删除</a>';
		//已确认的记录
		if(r.publishStatus != 1) {
//			html+='&nbsp;<a class="easyui-linkbutton" href="javascript:;" onclick="VendorPerforDo.impData('+r.id+')">数据导入</a>';
			html+='&nbsp;<a class="easyui-linkbutton" href="javascript:;" onclick="VendorPerforDo.countIndex('+r.id+')">指标计算</a>';
			html+='&nbsp;<a class="easyui-linkbutton" href="javascript:;" onclick="VendorPerforDo.countDim('+r.id+')">维度计算</a>';
			html+='&nbsp;<a class="easyui-linkbutton" href="javascript:;" onclick="VendorPerforDo.countTotal('+r.id+')">总分计算</a>';
		}
		return html;
	},
	processFmt : function(v,r,i){
		return ProcessPhase[v];
	},
	viewFmt : function(v,r,i){
		var html = '<a class="easyui-linkbutton" href="javascript:;" onclick="VendorPerforDo.view('+r.id+')">'+v+'</a>'
	    return html;
	},
	logFmt : function(v,r,i){
		if(v!=null && v!='')
		return '<a href="javascript:;" onclick="File.showLog(\''+v+'\')">日志</a>';
	},
	/** 查询 */	
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	/** 执行列表的手动初始化 */
	initList : function(){
		$.messager.progress();
		$.ajax({
			url : ctx+'/manager/vendor/vendorPerforScores/initList',
			dataType : 'json',
			method : 'post',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.success('初始化成功');
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.fail(data.msg);
				}
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
			
		});
	},
	/** 删除 */
	deleteList : function(id){
		$.messager.confirm("删除", "确定执行此操作吗？", function (data) {
			if (data) {
				$.messager.progress();
				$.ajax({
					url : ctx+'/manager/vendor/vendorPerforScores/delete',
					data : {id:id},
					dataType : 'json',
					method : 'post',
					success : function(data){
						$.messager.progress('close');
						if(data.success){
							$.messager.success('删除成功');
							$('#datagrid').datagrid('reload');
						}else{
							$.messager.fail(data.msg);
							$('#datagrid').datagrid('reload');
						}
					},
					error:function(data) {
						$.messager.progress('close');
						$.messager.fail(data.responseText);
					}
				});
			}
		});
	},
	/** 发布 */
	confirmList : function(){
		var ids = [];
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length<=0){
			$.messager.alert('提示','未选择记录！','warning');
			return;
		}
		$.each(selections,function(i,n){
			ids.push(n.id);
		});
		$.messager.progress();
		$.ajax({
			url : ctx+'/manager/vendor/vendorPerforScores/confirmList',
			data : {ids:ids},
			dataType : 'json',
			method : 'post',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.success(data.msg);
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.fail(data.msg);
					$('#datagrid').datagrid('reload');
				}
			},
			error : function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		});
	},
	/** 指标计算 */
	countIndex : function(id){
		//单独计算每一个评估列表
		$.messager.progress({text:'开始计算指标'});
		$.ajax({
			url : ctx + '/manager/vendor/vendorPerforScores/countIndex',
			data : {id:id},
			dataType : 'json',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.success('计算成功');
				}else{
					$.messager.fail(data.msg || '计算失败，请查看日志');
				}
				$('#datagrid').datagrid('reload');
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
			
		});
		
	},
	/** 维度计算 */
	countDim : function(id){
		//单独计算每一个评估列表
		$.messager.progress({text:'开始计算维度'});
		$.ajax({
			url : ctx + '/manager/vendor/vendorPerforScores/countDim',
			data : {id:id},
			dataType : 'json',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.success('计算成功');
				}else{
					$.messager.fail('计算失败，请查看日志');
				}
				$('#datagrid').datagrid('reload');
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
			
		});
		
	},
	/** 总分计算 */
	countTotal : function(id){
		//单独计算每一个评估列表
		$.messager.progress({text:'开始计算总分'});
		$.ajax({
			url : ctx + '/manager/vendor/vendorPerforScores/countTotal',
			data : {id:id},
			dataType : 'json',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.success('计算成功');
				}else{
					$.messager.fail('计算失败，请查看日志');
				}
				$('#datagrid').datagrid('reload');
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		});
		
	},
	view : function(id){
		$('#form').getCmp('id').val(id);
		$('#form').prop('action',ctx+'/manager/vendor/vendorPerforScores/toViewScore');
		$('#form').submit();
	}
}
$(function(){
	$('#ccb').combobox({    
	    url:ctx+'/manager/vendor/vendorPerforReviews/getVendorPerforCycle',    
	    valueField:'id',    
	    textField:'text' ,
	   	onSelect: function(rec){    
	           $("input[name='search-EQ_cycleId']").val(rec.id);   
	      }
	})
})