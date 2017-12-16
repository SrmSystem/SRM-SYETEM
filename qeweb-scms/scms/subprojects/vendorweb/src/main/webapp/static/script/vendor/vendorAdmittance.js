var VendorAdmittance = {
  codeFmt : function(v,r,i){
	 return v.code;
  },
  vendorOptFmt : function(v,r,i){
	  var enableStatus = r.org.enableStatus;
	  if(enableStatus==1){
		  return '<button onclick="VendorAdmittance.enable(this,0,\'禁用\','+r.org.id+','+r.phaseId+')" class="btn-link">禁用</button>';
	  }else{
		  return '<button onclick="VendorAdmittance.enable(this,1,\'启用\','+r.org.id+','+r.phaseId+')" class="btn-link">启用</button>';
	  }
  },
  enable : function(btn,type,typeText,orgId,phaseId){//禁用/启用 //TODO
	  var $dialog = $('#dialog-change');
	  $dialog.getCmp('phaseId').val(phaseId);
	  $dialog.getCmp('orgId').val(orgId);
	  $dialog.getCmp('enableStatus').val(type);
	  $dialog.getCmp('changeType').val(type);
	  $dialog.getCmp('changeTypeText').val(typeText);
	  $dialog.getCmp('changeReason').textbox('setValue','');
	  $dialog.dialog('setTitle','供应商变更-'+typeText);
	  $dialog.dialog('open');
	  $dialog.dialog('autoSize');
  },
  enableSubmit : function(){
	var $dialog = $('#dialog-change');
	var phaseId = $dialog.getCmp('phaseId').val();
	var changeText = $dialog.getCmp('changeTypeText').val();
	$.ajax({
		method : 'post',
		url : ctx+'/manager/vendor/vendorBaseInfo/enable',
		data : {orgId : $dialog.getCmp('orgId').val(),
			    enableStatus: $dialog.getCmp('enableStatus').val(),
			    changeType : $dialog.getCmp('changeType').val(),
			    changeTypeText:$dialog.getCmp('changeTypeText').val(),
			    changeReason : $dialog.getCmp('changeReason').textbox('getValue')
			    },
	    dataType : 'json',
		success : function(data){
			if(data.success){
				$.messager.alert('提示',changeText+'成功!','info');
				$('#datagrid-'+phaseId).datagrid('reload');
				$dialog.dialog('close');
			}else{
				$.messager.alert('提示',data.msg,'error');
			}
		}
		
	});  
  },
  getHis:function(url,datagridId){
		$('#'+datagridId).datagrid('reload');
  },
  promoteBatch : function(phaseId){//批量晋级，当前阶段的ID,用来获取当前阶段选中的供应商
	 var selArray = $('#datagrid-'+phaseId).datagrid('getSelections');
	 if(selArray.length<=0){
		 $.messager.alert('提示','没有选择晋级的供应商！','warning');
		 return;
	 }
	 VendorAdmittance.promoteSubmit(selArray,function(data){
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
	  $.messager.confirm('提示','确认晋级吗?',function(r){
		  if(!r)return;
		  VendorAdmittance.promoteSubmit(baseVendorList,function(data){
			  if(data.success){
	             var $dialog = $('#dialog-audit');
	             var $datagrid = $('#datagrid-'+data.phaseId);
	             $.messager.alert('提示','晋级成功','info');
	             $dialog.dialog('close');
	             $datagrid.datagrid('reload');

//				  window.parent.location.href = window.parent.location.href;
			  }else{
				  $.messager.alert('提示',data.msg,'warning');
			  }
			  
		  });
		  
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
  demotion : function(vendorId,phaseId){//vendorBaseId 弹出降级框
	  var $dialog = $('#dialog-demotion');
	  $dialog.getCmp('phaseId').val(phaseId);
	  $('#datagrid-demotion-phase').datagrid('load',ctx+'/manager/vendor/cfg/getDemotionPhasesxl/'+$("#vendorsxlid").val());
	  $dialog.getCmp("changeReason").textbox('setValue','');
	  $dialog.dialog('open');
	  
  },
  demotionSubmit : function(){
	  var $dialog = $('#dialog-demotion');
	  var $datagrid = $('#datagrid-demotion-phase');
	  var phaseId = $dialog.getCmp('phaseId').val();
	  var selected = $datagrid.datagrid('getSelected');
	  if(selected==null){
		  $.messager.alert('提示','请选择要降级的阶段','warning');
		  return false;
	  }
	  
	  //获取降级阶段和降级原因
	  var changeReason = $dialog.getCmp('changeReason').textbox('getValue');
	  $.ajax({
			 url : ctx+'/manager/vendor/vendorBaseInfo/vendorDemotion',
			 data : {vendorPhaseId:selected.id,changeReason:changeReason},
			 method : 'post',
			 dataType : 'json',
			 success : function(data){
				 if(data.success){
		             var $dialogAudit = $('#dialog-audit');
		             var $dialogDemotion = $('#dialog-demotion');
		             var $datagridPhase = $('#datagrid-'+phaseId);
		          
		             $.messager.alert('提示','降级成功','info');
		             $dialogDemotion.dialog('close');
		             $dialogAudit.dialog('close');
		             $datagridPhase.datagrid('reload');
		             $dialog.getCmp('phaseId').val("");
//					  window.parent.location.href = window.parent.location.href;
				  }else{
					  $.messager.alert('提示',data.msg,'warning');
				  }
			 }
		 });
  },
  cellRender : {
	  survey : function(v,r,i){
		  return 'background-color:#ffee00;color:red;';
	  }
	  
  },
  auditSurveyFmt : function(v,r,i){
	  if(v==null)v = '0/0';
	  return '<a href="javascript:;" onclick="VendorAdmittance.openAuditSurvey('+r.orgId+','+r.phaseId+')">'+v+'</a>';
  },
  openAuditSurvey : function(id,phaseId){//org的ID
	  var $dialog = $('#dialog-audit');
	  var url = ctx+'/manager/vendor/admittance/auditSurveyPage/'+id;
	  $dialog.dialog({
		  //content:'<iframe src="'+url+'" scrolling="no" frameborder="0" width="100%" height="99%"></iframe>',
		  href : url,
		  //onOpen : function(){
		//	  $dialog.dialog('autoSizeMax',{body:true});
		  //},
		  onClose : function(){
			  $('#datagrid-'+phaseId).datagrid('reload');//阶段供应商刷新
		  }
	  });
	  $dialog.dialog('autoSizeMax',{body:true});
	  $dialog.dialog('open');
  },
  audit : function(surveyCfgId){//审核调查表
	  $('#window-survey-auditSubmit').window('open');
		$('#form-survey-auditSubmit').form('clear');
		$("#auditStatus").trigger('click');
		$('#form-survey-auditSubmit').getCmp('id').val(surveyCfgId);
		$('#form-survey-auditSubmit').getCmp('id')
	},
	auditSubmit : function(){
		VendorAdmittance.commonSubmit('window-survey-auditSubmit','form-survey-auditSubmit');
	},
	auditBase : function(){//审核供应商基本信息
		$('#window-auditSubmit').window('open');
	},
	auditBaseSubmit : function(){//提交审核供应商基本信息表
		VendorAdmittance.commonSubmit('window-auditSubmit','form-auditSubmit');
	},
	commonSubmit : function(wId,fId){
		$.messager.progress();
		$('#'+fId).form('submit',{
			ajax : true,
			success : function(data){
				$.messager.progress('close');
				data = $.parseJSON(data);
				if(data.success){
					$('#'+wId).window('close');
					var surveyCfg = data.surveyCfg;
					var cfgId = surveyCfg.id;//调查表编辑为配置ID
					var node = $('#tree-survey').tree('find',cfgId);
					if(node){
						$('#tree-survey').tree('update',{
							target : node.target,
							iconCls : data.statusIcon
						});
					}
					//审核提交之后，对各个组件做处置和对状态赋值.
					VendorAdmittance.setSurveyAuditStatus(data.surveyCfg);
					var surveyCode = surveyCfg.surveyCode;
					if(surveyCode=='base'){
						cfgId = 'base';
					}
					VendorAdmittance.auditSurveyInit(cfgId);
					VendorAdmittance.refreshHis(cfgId,data.ctId,data.currentId);
				}else{
					$.messager.alert('提示',data.msg,'warning');
				}
			}
			
		});
	},
	setSurveyAuditStatus : function(surveyCfg){
		var surveyCode = surveyCfg.surveyCode;
		var $ct = $('#survey-'+surveyCfg.id+'-ct');
		if(surveyCode=='base'){
			$ct = $('#survey-base-ct');
		}
		$ct.getCmp('auditStatus').val(surveyCfg.auditStatus);
	},
	auditSurveyInit : function(ctId){//审核初始化,ID标记
		var $ct = $('#survey-'+ctId+'-ct');
		var auditStatus = $ct.getCmp('auditStatus').val();
		var submitStatus = $ct.getCmp('submitStatus').val();
		if(auditStatus!=0 || submitStatus==0){//如果未提交或者已经审核过
			$('#btn-group-audit-'+ctId).addClass('hidden');
		}
	},
	promoteInit : function(phaseSn,isEndPhase,enableStatus){
		var $promoteBtn = $('#tabs-tool-audit').getCmp('btn-promote');
		var $demotionBtn = $('#tabs-tool-audit').getCmp('btn-demotion');
		if(enableStatus==1){
			if(isEndPhase){
				$promoteBtn.linkbutton({
					disabled : true
				});
			}
			if(phaseSn==1){
				$demotionBtn.linkbutton({
					disabled : true
				});
			}
		}else{
			$promoteBtn.linkbutton({
				disabled : true
			});
			$demotionBtn.linkbutton({
				disabled : true
			});
		}
	},
	refreshTabDatagrid : function(title,index){//刷新Tab内的grid
		var $tabs = $('#vendorAdmittance-tabs');
		var tab = $tabs.tabs('getTab',index);
		var phaseId = $(tab).attr('itemId');
		$('#datagrid-'+phaseId).datagrid('reload');
	},
	refreshHis : function(surveyCfgId,ctId,currentId){//刷新历史记录
		var $hisDatagrid = $('#survey-his-'+surveyCfgId);
		$hisDatagrid.datagrid('load',{
			ctId : ctId,
			currentId : currentId
		});
	}
}

var SuveryAuditor = {
	initTree : function(){
		//初始化调查表列表信息
	    $('#tree-survey').tree({
			onClick: function(node){
				if(node.attributes.surveyCode){
					SuveryAuditor.toSurvey(node.attributes.surveyName,node.id);
				}
				
			}
		});
	},
	/** 调查表列表点击查看调查表详情 */
	toSurvey : function(title,id){
			if($('#tabs-survey').tabs('exists',title)){
				$('#tabs-survey').tabs('select',title);
				return false;
			}
			var url = ctx+'/manager/vendor/admittance/getSurveyAudit/'+id;
			$('#tabs-survey').tabs('add',{
				title:title,
//				content:'<iframe src="'+url+'" frameborder="0" style="width:100%;height:460px;"></iframe>',
				href:ctx+'/manager/vendor/admittance/getSurveyAudit/'+id,
				closable:true
			});
			
	}
	
		
}



