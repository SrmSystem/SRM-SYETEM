/** 评估结果 */
var sxl;
var VendorPerforResult = {
	/** 操作格式列 */
	optFmt : function(v,r,i){
		var html = '<a class="easyui-linkbutton" href="javascript:;" onclick="VendorPerforResult.adjustScore('+r.id+')">调分</a>';
	    return html;
	},
	/** 调分 */
	adjustScore : function(id){
		var row = Dg.getRowById($('#datagrid'),id);
		if(row==null){
			$.messager.alert('提示','没有可调分记录','warning');
			return;
		}
		
		var $form = $('#dialog-adjust').getCmp('form');
		$form.form('clear');
		var mappings = $form.find('.mapping');
		$form.getCmp('scoresId').val(row.scoresId);
		$form.getCmp('id').val(row.id);
		$.each(mappings,function(i,n){
			$form.getCmp($(n).attr('itemId')+'-old').html(row[$(n).attr('itemId')]);
		});
		$('#dialog-adjust').dialog('open');
		$('#dialog-adjust').dialog('autoSize');
	},
	/** 调分提交 */
	adjustScoreSubmit : function(){
		var $form = $('#dialog-adjust').getCmp('form');
		$.messager.progress();
		$form.form('submit',{
			url : ctx + '/manager/vendor/vendorPerforScores/adjustScore',
			success : function(data){
				$.messager.progress('close');
				try{
					data = $.parseJSON(data);
					if(data.success){
						$('#dialog-adjust').dialog('close');
						$('#datagrid').datagrid('reload');
						$.messager.success();
					}else{
						$.messager.fail(data.msg);
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	/**
	 * 显示调分历史
	 */
	showHisWin : function(v,r,i) {
		var html = '';
		if(r.adjustStatus == 1)
			html = '<a class="easyui-linkbutton" href="javascript:;" onclick="VendorPerforResult.viewHis('+r.scoresId+','+r.id+')">查看</a>';
		
		return html;
	},
	
	viewHis : function(scoresId, id) {
		new dialog().showWin('调分历史', 600, 400, ctx + '/manager/vendor/vendorPerforScores/toViewScoreHis/' + scoresId + '/' + id);
	},
	/**
	 * 发布
	 */
	publishList : function(datagridId) {
		var ids = [];
		var selections = $(datagridId).datagrid('getSelections');
		if(selections.length == 0 ){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		$.each(selections,function(i,n){
			ids.push(n.id);
		});
		$.messager.progress();  
		$.ajax({
			url : ctx + '/manager/vendor/vendorPerforScores/publishTotalList',
			data:{ids:ids},
			dataType : 'json',
			method : 'post',
			success : function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.success(data.msg);
					$(datagridId).datagrid('reload');
				}else{
					$.messager.fail(data.msg);
					$(datagridId).datagrid('reload');
				}
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		});
	},
	/**
	 * 整改
	 */
	correctionList : function(datagridId) {
		sxl=datagridId;
		var ids = [];
		var selections = $(datagridId).datagrid('getSelections');
		if(selections.length == 0 ){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		$.each(selections,function(i,n){
			ids.push(n.id);
			ids.push(',');   
		});
		$('#dialog-correction-form').form('reset');
		$('#correntIds').val(ids.join(''));
		$('#dialog-correction').dialog('open');
	},
	
	/**
	 * 提交整改
	 * @returns {Boolean}
	 */
	submitCorrectionList : function(datagridId) {
		var isValid = $("#dialog-correction-form").form('validate');
		if(!isValid) {
			return false;
		}
		$.messager.progress(); 
		$("#dialog-correction-form").form('submit',{
			url : ctx + '/manager/vendor/vendorPerforScores/correctionList',
			dataType : 'json',
			method : 'post',
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success : function(data){
				$.messager.progress('close');
				data= JSON.parse(data);
				if(data.success){
					$.messager.success(data.msg);
					$(sxl).datagrid('reload');
				}else{
					$.messager.fail(data.msg);
					$(sxl).datagrid('reload');
				}
				$('#dialog-correction').dialog('close');
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		});
	},
	/** 维度得分查看 */
	dimView : function(id,mapping,dimName){
		var $indexScoreDg = $('#dialog-indexScore').getCmp('datagrid');
		$('#dialog-indexScore-datagrid-tt').getCmp('dimName').html(dimName);
		$indexScoreDg.datagrid({
			url : ctx + '/manager/vendor/vendorPerforScores/getIndexScore',
			queryParams : {id:id,mapping:mapping}
		});
		$('#dialog-indexScore').dialog('open');
		$('#dialog-indexScore').dialog('autoSizeMax');
	},
	/** 质量维度得分查看 */
	dimViewQ : function(id,mapping,dimName){
		var $indexScoreDg = $('#dialog-indexScoreQ').getCmp('datagrid');
		$('#dialog-indexScore-datagrid-ttQ').getCmp('dimName').html(dimName);
		$indexScoreDg.datagrid({
			url : ctx + '/manager/vendor/vendorPerforScores/getIndexScoreQ',
			queryParams : {id:id,mapping:mapping}
		});
		$('#dialog-indexScoreQ').dialog('open');
		$('#dialog-indexScoreQ').dialog('autoSizeMax');
	},
	/**子模版得分查看**/
	tepView : function(id,mapping, brandId) {
		//TODO::
		$('#form').getCmp('id').val(id);
		$('#form').prop('action',ctx+'/manager/vendor/vendorPerforScores/toViewScore');
		$('#form').submit();
	},
	/** 供应商查询结果 */
	searchVendor : function(datagrid){
		var searchParamArray = $('#form-'+datagrid).serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-'+datagrid).datagrid('load', searchParams);
	},
	searchVendorbo : function(datagrid){
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	searchVendor11 : function(){
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	searchVendor2 : function(datagrid){
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$(datagrid).datagrid('load', searchParams);
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
	$('#ccb2').combobox({    
		url:ctx+'/manager/vendor/level/getVendorPerforLevel',    
		valueField:'id',    
		textField:'text' ,
		onSelect: function(rec){    
			$("input[name='search-LIKE_levelName']").val(rec.id);   
		}
	})
})