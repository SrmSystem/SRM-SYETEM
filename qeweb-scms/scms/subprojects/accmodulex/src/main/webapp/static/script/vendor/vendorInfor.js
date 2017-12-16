var BaseVendor = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	search2 : function() {
		var searchParamArray = $('#form2').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid2').datagrid('load', searchParams);
	},
	viewFmt : function(v,r,i){
		return '<button class="btn-link" onclick="BaseVendor.openAuditSurvey('+r.id+')">'+v+'</button>';
	},
	vfmt0 : function(v,r,i){
		return '<button class="btn-link" onclick="exp('+r.id+')">导出</button>';
	},
	vfmt : function(v,r,i){
		return '<button class="btn-link" onclick="BaseVendor.openvfSurvey('+r.id+','+i+')">查看</button>';
	},
	vfmt1 : function(v,r,i){
		return '<button class="btn-link" onclick="BaseVendor.openvfSurvey1('+r.id+')">维护(修改)</button>';
	},
	vfmt2 : function(v,r,i){
		return '<button class="btn-link" onclick="BaseVendor.openvfSurvey2('+r.id+')">维护(修改)</button>';
	},
	vfmt8 : function(v,r,i){
		return '<button class="btn-link" onclick="lookgonghuoxishu('+r.id+','+r.vendorId+')">查看</button>';
	},
	imp:function (){
		$('#win-materialSupplyRel-import').window({
			iconCls : 'icon-disk_upload',
			title : '批量维护分类/等级'
		});
		$('#form-materialSupplyRel-import').form('clear');
		$('#win-materialSupplyRel-import').window('open');
	},
	saveImp:function() {
		$.messager.progress();
		$('#form-materialSupplyRel-import').form('submit',{
			ajax:true,
			iframe: true,    
			url:ctx+'/manager/vendor/vendorInfor/filesUpload', 
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
					$.messager.alert('提示','批量更新分类/类型成功','info');
					$('#win-materialSupplyRel-import').window('close');
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
				}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	imp2:function (){
		$('#win-materialSupplyRel-import2').window({
			iconCls : 'icon-disk_upload',
			title : '批量质量体系审核结果和评优结果'
		});
		$('#form-materialSupplyRel-import2').form('clear');
		$('#win-materialSupplyRel-import2').window('open');
	},
	saveImp2:function() {
		$.messager.progress();
		$('#form-materialSupplyRel-import2').form('submit',{
			ajax:true,
			iframe: true,    
			url:ctx+'/manager/vendor/vendorInfor/filesUpload2', 
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
						$.messager.alert('提示','批量质量体系审核结果和评优结果','info');
						$('#win-materialSupplyRel-import2').window('close');
						$('#datagrid').datagrid('reload');
					}else{
						$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
					}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	openAuditSurvey : function(id) {// org的ID
		$('#addd').dialog({    
		    title: '供应商基本信息',    
		    closed: false,    
		    cache: false, 
		    iconCls:'icon-save',
		    href: ctx+'/manager/vendor/vendorInfor/vendorInfoRela/'+id,    
		    modal: true   
		});    
//		$('#addd').dialog('autoSizeMax');
	},
	openvfSurvey : function(id,i) {// org的ID
		$("#fixedsid").empty();
		$('#win-materialSupplyRel').window({
			title:'供货关系详情'
		});
		$('#win-materialSupplyRel').window('open');
		$('#datagrid-materialSupplyRel-list').datagrid('reload',ctx+'/manager/vendor/vendorInfor/vendorInfoCon/'+id);
		$("#datagrid-row-r1-2-"+i).removeClass("datagrid-row-selected");
	},
	openvfSurvey1 : function(id) {// org的ID
		$('#dd').dialog({    
			title: '维护分类/等级',    
			width: 919,    
			height: 456,    
			closed: false,    
			cache: false,    
			href: ctx+'/manager/vendor/vendorInfor/vendorInfoWeiHuStart/'+id,    
			modal: true   
		});    
	},
	openvfSurvey2 : function(id) {// org的ID
		$('#dd').dialog({    
			title: '维护近三年质量体系审核/近三年评优结果',    
			width: 919,    
			height: 456,    
			closed: false,    
			cache: false,    
			href: ctx+'/manager/vendor/vendorInfor/vendorInfoWeiHuStart2/'+id,    
			modal: true   
		});    
	}
}
function exp(id){

	
	$('#form-export').form('submit',{
		url:ctx+'/manager/vendor/vendorInfor/download/'+id, 
		success:function(data){
			$.messager.progress('close');
		}
	});
}