var BaseVendor = {
  codeFmt : function(v,r,i){
	 return v.code;
  },
  phaseFmt : function(v,r,i){
	  return v.name;
  },
  promoteBatch : function(phaseId){//批量晋级，当前阶段的ID,用来获取当前阶段选中的供应商
	 var selArray = $('#datagrid-'+phaseId).datagrid('getSelections');
	 if(selArray.length<=0){
		 $.messager.alert('提示','没有选择晋级的供应商！','warning');
		 return;
	 }
	 BaseVendor.promoteSubmit(selArray,function(data){
		 if(data.success){
			 $.messager.alert('提示','供应商已成功晋级','info');
			 $('#datagrid-'+phaseId).datagrid('reload');
		 }else{
			 $.messager.alert('提示','供应商晋级失败','warning');
		 }
	 });
  },
  promote : function(id){//是vendorBase的ID
	  var baseVendorList = [{id:id}];
	  BaseVendor.promoteSubmit(baseVendorList,function(data){
		  if(data.success){
			  var pwin = window.parent;
			  pwin.find('#window-audit').window('close');
			  pwin.find('#datagrid-'+data.phaseId).datagrid('reload');
			  $.messager.alert('提示','供应商已成功晋级','info');
		  }else{
			  $.messager.alert('提示',data.msg,'warning');
		  }
		  
	  });
	  
  },
  promoteSubmit : function(baseVendorList,callBack){
	  $.ajax({
			 url : ctx+'/manager/vendor/vendorBaseInfo/vendorPromote',
			 data : $.toJSON(baseVendorList),
			 method : 'post',
			 contentType : 'application/json',
			 dataType : 'json',
			 success : function(data){
				 callBack(data);
			 }
			 
		 });
  },
  cellRender : {
	  survey : function(v,r,i){
		  return 'background-color:#ffee00;color:red;';
	  }
	  
  },
  auditSurveyFmt : function(v,r,i){
	  return '<a href="javascript:;" onclick="BaseVendor.openAuditSurvey('+r.orgId+')">'+v+'</a>';
  },
  openAuditSurvey : function(id){//org的ID
	  var url = ctx+'/manager/vendor/admittance/auditSurveyPage/'+id;
	  $('#window-audit').window({
		  content:'<iframe src="'+url+'" frameborder="0" style="width:100%;height:99%;"></iframe>',
		  width : $(document).innerWidth()-20,
		  height : $(document).innerHeight()-20
	  });
	  $('#window-audit').window('open');
	  $('#window-audit').window('center');
	  
  }
}