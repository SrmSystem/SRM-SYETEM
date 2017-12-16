var twoAudit = {
	search : function(){
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	planDownLoad:function(v,r,i){
		if(r.planFilePath==null){
			return "";
			}else{
		return '<a href="javascript:;" onclick="File.download(\''+r.planFilePath+'\',\'\')">附件下载</a>';}
	},
	fileUrl:function(v,r,i){
		if(r.fileUrl==null){
			return "";
		}else{
			return '<a href="javascript:;" onclick="File.download(\''+r.fileUrl+'\',\'\')">附件下载</a>';}
	},
	addsearch : function(){
		var searchParamArray = $('#form-twoAuditVendor-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-twoAuditVendor-list').datagrid('load',searchParams);	
	},
	correctiveStatusFmt(v,r,i){
		if(r.correctionStatus == -1)
			return '未发布';
		else if(r.correctionStatus == 0)
			return '已经发布！等待整改';
		else if(r.correctionStatus == 1)
			return '已提交整改';
		else if(r.correctionStatus == 2)
			return '整改驳回';
		else if(r.correctionStatus == 3)
			return '驳回反馈';
		else if(r.correctionStatus == 4)
			return '整改结束';
		return '未知编码！'+r.correctionStatus ;
	},
	endStatus(v,r,i){
		if(r.endStatus == 0)
			return '未结案';
		else if(r.endStatus == 1)
			return '已经结案';
		return '未知编码！'+r.endStatus;
	},
	reportFilePath:function(v,r,i){
		if(r.reportFilePath==null){
			return "";
			}else{
		return '<a href="javascript:;" onclick="File.download(\''+r.reportFilePath+'\',\'\')">附件下载</a>';}
	},
	qrs : function(){
		$.messager.confirm('提示','确定吗？确定后供应商将能看到',function(r){
			if(r){
				var selections = $('#datagrid').datagrid('getSelections');
				if(selections.length==0){
					$.messager.alert('提示','没有选择任何记录！','error');
					return false;
				}
				for(var i=0;i<selections.length;i++){
					if(selections[i]["correctionStatus"]!=-1){
						$.messager.alert('提示','存在不能发布的阶段','info');
						return false;
					}
				}
				var params = $.toJSON(selections);
				$.ajax({
					url:ctx+'/manager/qualityassurance/twoAudit/getQrs',
					type:'POST',
					data:params,
					contentType : 'application/json',
					dataType : 'json',
					success:function(data){
						if(data.success)
						{
							$.messager.alert('提示',data.msg,'info');
							$('#datagrid').datagrid('reload');
							$('#win-import3').window('close');
						}
						else
						{
							$.messager.alert('提示',data.msg,'error');
							$('#datagrid').datagrid('reload');
							$('#win-import3').window('close');
						}
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	solution : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','error');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条！','error');
			return false;
		}
		if(selections[0]["correctionStatus"]!=1){
			$.messager.alert('提示','此阶段无法审核方案','error');
			return false;
		}
		var fileUrl=''
		if(selections[0]["fileUrl"]!=""&&selections[0]["fileUrl"]!=null)
		{
			fileUrl='<a  href="javascript:;" onclick="File.download(\''+selections[0]["fileUrl"]+'\',\'\')">查看附件</a>';
		}
		$('#win-import').dialog();
		$("#fujian").html(fileUrl);
		$('#form-import').form('load',selections[0]);
		$("#uid").val(selections[0]["id"]);
		$('#win-import').window('open');
	},
	addPlan:function(){
		$('#form-twoAudit-addPlan').form('clear');
		$("#id").val(0);
		$('#win-twoAudit-addPlan').window('open');
	},
	savePlan:function(){
		var aplan= $('#planFile').val();
		if(aplan == null || aplan == ''){
			alert("请导入附件");
			return false;
		}
		$.messager.progress();
		$('#form-twoAudit-addPlan').form('submit',{
			ajax:true,
			url:ctx+'/manager/qualityassurance/twoAudit/planUpload',
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
						$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'info');
						$('#win-twoAudit-addPlan').window('close');
						$('#datagrid').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
				}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});		
	},
	solutionSubmit : function(typee){
		$("#typee").val(typee);
		$.messager.progress();
		$('#form-import').form('submit',{
			url:ctx+'/manager/qualityassurance/twoAudit/getSolutionSubmit', 
			contentType : 'application/json',
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
					data = JSON.parse(data);
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
						$('#win-import').window('close');
						$('#datagrid').datagrid('reload');
					}else{
						$.messager.alert('提示',data.msg  ,'error');
					}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	lookSolution : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','error');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条！','error');
			return false;
		}
//		if(selections[0]["correctionStatus"]<2){
//			$.messager.alert('提示','此阶段无法查看过程','error');
//			return false;
//		}
		$('#win-import2').dialog();
		$('#win-import2').window('open');
		$('#datagrid2').datagrid({url: ctx+'/manager/qualityassurance/twoAudit/getLookSolution/'+selections[0]["id"]});
	},
	endSolutionStart : function (){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','error');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条！','error');
			return false;
		}
		for(var i=0;i<selections.length;i++){
			if(selections[i]["endStatus"]!=0){
				$.messager.alert('提示','存在已经结案的','info');
				return false;
			}
			if(selections[i]["correctionStatus"]<2){
				$.messager.alert('提示','存在不能结案的阶段','info');
				return false;
			}
		}
		$('#win-import3').dialog();
		$('#win-import3').window('open');
	},
	endSolution : function(){
		var usContent=$("#usContent").val();
		if(usContent==''||usContent==null)
		{
			$.messager.alert('提示','请填写结案评论！','error');
		}
		$.messager.confirm('提示','确定结案吗？结案后将不能做任何操作',function(r){
			if(r){
				var selections = $('#datagrid').datagrid('getSelections');
				if(selections.length==0){
					$.messager.alert('提示','没有选择任何记录！','error');
					return false;
				}
				if(selections.length>1){
					$.messager.alert('提示','只能选取一条！','error');
					return false;
				}
				for(var i=0;i<selections.length;i++){
					if(selections[i]["endStatus"]!=0){
						$.messager.alert('提示','存在已经结案的','info');
						return false;
					}
					if(selections[i]["correctionStatus"]<2){
						$.messager.alert('提示','存在不能结案的阶段','info');
						return false;
					}
				}
				
				var params = $.toJSON(selections);
				
				$.ajax({
					url:ctx+'/manager/qualityassurance/twoAudit/getEndSolution/'+encodeURIComponent(usContent),
					type:'POST',
					data:params,
					contentType : 'application/json',
					dataType : 'json',
					success:function(data){
						if(data.success)
						{
							$.messager.alert('提示',"结案成功",'info');
							$('#datagrid').datagrid('reload');
							$('#win-import3').window('close');
						}
						else
						{
							$.messager.alert('提示',data.msg,'error');
							$('#datagrid').datagrid('reload');
							$('#win-import3').window('close');
						}
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	vendorsolution : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','error');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条！','error');
			return false;
		}
		if(selections[0]["correctionStatus"]!=0&&selections[0]["correctionStatus"]!=3){
			$.messager.alert('提示','此阶段无法提交方案','error');
			return false;
		}
		$('#win-import').dialog();
		$('#form-importww').form('load',selections[0]);
		$("#uid").val(selections[0].id);
		$('#win-import').window('open');
	},
	chooseVenCode:function(){
		$('#kk').window('open');
		$('#datagrid-twoAuditVendor-list').datagrid({url: ctx+'/manager/vendor/vendorInfor'
		});			
	}
}
function bringBack(){
	var selections = $('#datagrid-twoAuditVendor-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	
	$("#orgId").val(selections[0].org.id);
	$("#vendCode").textbox('setValue',selections[0].org.code);
	$("#vendName").textbox('setValue',selections[0]['name']);
	$('#kk').window('close');
}