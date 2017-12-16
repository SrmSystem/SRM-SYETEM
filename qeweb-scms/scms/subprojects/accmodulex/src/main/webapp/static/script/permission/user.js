var User = {
	/**
	 * 用户数据权限设置
	 * @param rdcid
	 * @param roleId
	 */
	dataSet : function(rdcid, userId) {
		var $dialog = $('#dialog-data');
		var url = ctx + '/manager/admin/roledata/getUserDataList/' + userId + '/' + rdcid;
		$dialog.dialog({
			href : url,
			onOpen : function() {
			}
		});
		$dialog.dialog('open');
	},
	/**
	 * 用户数据权限删除
	 * @param rdcid
	 * @param roleId
	 */
	dataClear : function(rdcid, userId) {
		$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/, $.i18n.prop('verdor.permissionJs.determineOperation')/*'确定执行此操作吗？'*/, function (data) {
			if (data) {
				$.messager.progress();  
				$.ajax({
					url : ctx + '/manager/admin/roledata/clearUserData/' + userId + '/' + rdcid,
					dataType : 'json',
					method : 'post',
					success : function(data){
						$.messager.progress('close');
						if(data.success){
							$.messager.success(data.msg);
						}else{
							$.messager.fail(data.msg);
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
	//保存数据权限
	addUserData : function(userId, roleDataCfgId) {
		
		var selections = $('#datagrid-role-data').datagrid('getSelections');
		var dataIds = '';
		for(var i = 0; i < selections.length; i ++) {
			dataIds += (selections[i].id + ',');
		}
		$("#dialog-data").dialog('close');
		$.messager.progress();  
		$.ajax({
			url:ctx+'/manager/admin/user/addUserData',
			method:'post',
			data:{userId:userId,roleDataCfgId:roleDataCfgId, dataIds:dataIds},
			dataType:'json',
			success:function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.savePermissionsSuccess') /*'保存数据权限成功'*/,'info');
				}else{
					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/, $.i18n.prop('verdor.permissionJs.failedSave')/*'保存数据权限失败'*/,'error');
				}
			}
		});
	}
}

function managerFmt(v,r,i){
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editUser('+r.id+');">'+$.i18n.prop('button.edit')+'</a>';/*编辑*/
}
function managerRightFmt(v,r,i){
	var str= '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openData('+r.id+');">'+$.i18n.prop('verdor.permissionJs.dataAccess')+'</a>';/*数据权限*/
	if(r.enabledStatus==1){
		str=str+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="disableUser('+r.id+');">'+$.i18n.prop('verdor.permissionJs.lock')+'</a>';/*锁定*/
	}else{
		str=str+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="enableUser('+r.id+');">'+$.i18n.prop('verdor.permissionJs.toTakeEffect')+'</a>';/*生效*/
	}
	str=str+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openUser('+r.id+');">'+$.i18n.prop('verdor.permissionJs.role')+'</a>';/*角色*/
	str=str+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showLog('+r.id+');">'+$.i18n.prop('verdor.permissionJs.logbook')+'</a>';/*日志*/
	if(r.company.roleType==0){
	str=str+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showUserRel('+r.id+');">'+$.i18n.prop('verdor.permissionJs.promotionReminder')+'</a>';/*晋级提醒*/
	str=str+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="groupConfig('+r.id+');">'+$.i18n.prop('verdor.permissionJs.procurementAllocation')+'</a>';/*采购组配置*/
	}
	return str;
}

function showLog(id){
	$('#win-user-log').window('open');
	$('#datagrid-user-log-list').datagrid({   
    	url:ctx+'/manager/admin/user/showUserLog/' + id     
	});
	$('#datagrid-user-log-list').datagrid('load',{});
}

function openUser (id){
	$('#win-user-role').window('open');
	//给角色ID赋值
	$('#win-user-role').getCmp('userId').val(id);
	$('#form-role-user-search').form('reset')
	$('#datagrid-role-list').datagrid({   
    	url: ctx + '/manager/admin/role/getRoleList/'+id
	});
	
}

function getEnableStatus(status,effectiveTime) {
	
	var crtTime = new Date();
	crtTime = dateFtt("yyyy-MM-dd hh:mm:ss",crtTime);
	if(effectiveTime>=crtTime){
		if(status == 0)
			return $.i18n.prop('verdor.permissionJs.lock')/*'锁定'*/;
		else if(status == 1)
			return $.i18n.prop('verdor.permissionJs.toTakeEffect')/*'生效'*/;
		else 
			return '';
	}else{
		return $.i18n.prop('verdor.permissionJs.lapse')/*'失效'*/;
	}
	
}

//日期转换
function dateFtt(fmt,date)   
	{ 
	  var o = {   
	    "M+" : date.getMonth()+1,                 //月份   
	    "d+" : date.getDate(),                    //日   
	    "h+" : date.getHours(),                   //小时   
	    "m+" : date.getMinutes(),                 //分   
	    "s+" : date.getSeconds(),                 //秒   
	    "q+" : Math.floor((date.getMonth()+3)/3), //季度   
	    "S"  : date.getMilliseconds()             //毫秒   
	  };   
	  if(/(y+)/.test(fmt))   
	    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));   
	  for(var k in o)   
	    if(new RegExp("("+ k +")").test(fmt))   
	  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
	  return fmt;   
} 

function disableUser(id){
	$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.determineLockingOperation')/*'确定锁定操作？'*/,function(r){
		if(r){
			$.ajax({
				url: ctx + '/manager/admin/user/disableUser/'+id,
				type:'POST',
				contentType : 'application/json',
				success:function(data){
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('verdor.permissionJs.lockUserSuccess')/*'锁定用户成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-user-list').datagrid('reload');
				}
			});
		}
	});
}

function enableUser(id){
	$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.confirmOperation')/*'确定生效操作？'*/,function(r){
		if(r){
			$.ajax({
				url: ctx + '/manager/admin/user/enableUser/'+id,
				type:'POST',
				contentType : 'application/json',
				success:function(data){
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg: $.i18n.prop('verdor.permissionJs.effectiveUserSuccess')/*'生效用户成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-user-list').datagrid('reload');
				}
			});
		}
	});
}
function userDataFmt(v,r,i) {
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="User.dataSet('+r.id+','+r.roleId+')">'+$.i18n.prop('verdor.permissionJs.setting')+'</a>'/*设置*/
	+'&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="User.dataClear('+r.id+','+r.roleId+')">'+$.i18n.prop('verdor.permissionJs.clearOut')+'</a>';/*清除*/
}
function xuanzhe() {
	$("#companyId").val("");
	var selections = $('#datagridss').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2'),'info');/*'没有选择任何记录！'*/
		return false;
	}
	if(selections.length>1){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.youOnlyRecord')/*'只能选择一条记录！'*/,'info');
		return false;
	}
	$("#companyId").val(selections[0].id);
	$("#companyName").textbox('setText',selections[0].name);
	$('#kk').window('close');
}
function addsearch() {
	var searchParamArray = $('#form2').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagridss').datagrid("load",searchParams);
}
function lookUser()
{
	$('#kk').window('open');
	$('#form2').form('reset');
	$('#datagridss').datagrid({url: ctx+'/manager/admin/org/getVendorList'
	});
}
function searchUser(){
	var searchParamArray = $('#form-user-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-user-list').datagrid('load',searchParams);
}

function addUser(type){
	$('#win-user-addoredit').window({
		iconCls:'icon-add',
		title:$.i18n.prop('verdor.permissionJs.newUsers')/*'新增用户'*/
	});
	$('#loginName').textbox('enable');
	$('#form-user-addoredit').form('clear');
	$('#id').val(0);
	if(type==0){
		$("#vendorDiv").css("display", "none");
		$("#buyerDiv").css("display", "table-row");
		$("#companyId").val(1);
	}else{
		$("#vendorDiv").css("display", "table-row");
		$("#buyerDiv").css("display", "none");
	}
	

   var curr_time = new Date();
   var strDate = curr_time.getFullYear()+100+"-";
   strDate += curr_time.getMonth()+1+"-";
   strDate += curr_time.getDate();
   $("#dd").datebox("setValue", strDate);
	
   
   $('#classSystemId').combobox({ 
	   url:ctx+'/manager/common/classSystem/findEffective',
       editable:false,
       cache: false,
       required:true,
       valueField:'id',   
       textField:'name',
  }); 
   
	
	/* $('#companyId').combobox("setValue",<%=user.orgId%>);
	//$('#companyId').combobox('readonly');*/
	$('#win-user-addoredit').window('open');
}

function submitAddorEditUser(){
	var url = ctx +'/manager/admin/user/addNewUser';
	var sucMeg = $.i18n.prop('verdor.permissionJs.addUserSuccess')/*'添加用户成功！'*/;
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx +'/manager/admin/user/update';
		sucMeg = $.i18n.prop('verdor.permissionJs.editUserSuccess')/*'编辑用户成功！'*/;
	}
	var companyId = $('#companyId').val()
	if(companyId==undefined || companyId=='' || companyId==null){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.superiorOrganizationEmpty')/*"上级组织不能为空!"*/,'error');
		return;
	}
	var loginName = $('#loginName').val();
	if(loginName==undefined || loginName=='' || loginName==null){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.userNameCannotEmpty')/*"用户名不能为空!"*/,'error');
		return;
	}
	
	var name = $('#name').val();
	if(name==undefined || name=='' || name==null){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.nameCannotBeEmpty')/*"姓名不能为空!"*/,'error');
		return;
	}
	
	var email = $('#email').val();
	if(email==undefined || email=='' || email==null){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.emailCannotBeEmpty')/*"Email不能为空!"*/,'error');
		return;
	}
	
	var classSystemId = $("#classSystemId").combobox('getValue');
	if(classSystemId==undefined || classSystemId=='' || classSystemId==null){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.classCannotEmpty')/*"班制不能为空!"*/,'error');
		return;
	}
	
	$.messager.progress({
		title:$.i18n.prop('label.remind')/*'提示'*/,
		msg : $.i18n.prop('label.insubmit')/*'提交中...'*/
	});
	
	var params = $('#form-user-addoredit').serializeArray(); //自动序列化表单元素为JSON对象
	$.ajax({
		url:url,
		type:'POST',
		data:params,
		dataType:"json",
		success:function(result){
			$.messager.progress('close');
			try{
				if(result.success){
					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,sucMeg,'info');
					$('#win-user-addoredit').window('close');
					$('#datagrid-user-list').datagrid('reload');
				}else{
					$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,result.msg,'error');
				}
			}catch (e) {
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,result,'error');
			}
		}, 
		error:function(data) {
			$.messager.fail(data.responseText);
		}
	});
	
}

function deleteUser(){
	var selections = $('#datagrid-user-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
		return false;
	}
	var params = $.toJSON(selections);
	$.messager.confirm($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.deleteOperations')/*'确定删除操作？'*/,function(r){
		if(r){
			$.ajax({
				url: ctx + '/manager/admin/user/deleteUser',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
						$.messager.show({
							title:$.i18n.prop('label.news')/*'消息'*/,
							msg:$.i18n.prop('verdor.permissionJs.deleteUserSuccess')/*'删除用户成功'*/,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-user-list').datagrid('reload');
				}
			});
		}
	});
}
function resetPass(){
	var selections = $('#datagrid-user-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
		return false;
	}
	for(var i=0;i<selections.length;i++){
		if(selections[i].companyId==1){
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.AACCannotPassword')/*'AAC内部用户不能重置密码'*/,'info');
			return false;
		}
	}
	
	var params = $.toJSON(selections);
	$.ajax({
		url: ctx + '/manager/admin/user/resetPass',
		type:'POST',
		data:params,
		contentType : 'application/json',
		success:function(data){
				$.messager.show({
					title:$.i18n.prop('label.news')/*'消息'*/,
					msg:$.i18n.prop('verdor.permissionJs.resetPasswordSuccess')/*'重置密码成功'*/,
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});
				$('#datagrid-user-list').datagrid('reload');
		}
	});
}
function editUser(){
	var selections = $('#datagrid-user-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
		return false;
	}
	if(selections.length>1){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('verdor.permissionJs.youOnlyRecord')/*'只能选择一条记录！'*/,'info');
		return false;
	}
   $('#classSystemId').combobox({ 
	   url:ctx+'/manager/common/classSystem/findEffective',
       editable:false,
       cache: false,
       required:true,
       valueField:'id',   
       textField:'name',
  }); 
	
	$("#companyId").val(selections[0].company.id);
	if(selections[0].company.id==1){
			$("#vendorDiv").css("display", "none");
			$("#buyerDiv").css("display", "table-row");
		
		}else{
			$("#vendorDiv").css("display", "table-row");
			$("#buyerDiv").css("display", "none");
			$("#companyName").textbox('setText',selections[0].company.name);
		}
	

	$('#form-user-addoredit').form('load',selections[0]);
	$('#win-user-addoredit').window('open');
}

function openData(uid) {
	$('#win-user-data').window('open');
	//给角色ID赋值
	$('#win-user-data').getCmp('userId').val(uid);
	$('#datagrid-user-data-list').datagrid({
		url : ctx + '/manager/admin/roledata/' + uid,
		queryParams : {}
	});
}
/*#########################################采购组配置相关*##################################*/
function groupConfig(userId){
	
/*	var $dialog = $('#dialog-dataGroup');
	var url = ctx + '/manager/admin/user/showGroupConfigList/' + userId;
	$dialog.dialog({
		href : url,
		onOpen : function() {
		}
	});
	$dialog.dialog('open');*/
	
	
	new dialog().showWin($.i18n.prop('verdor.permissionJs.addPurchasingGroup')/*"添加采购组"*/, 700,500, ctx + '/manager/admin/user/showGroupConfigList/' + userId);



}


/*#########################################分配晋级提醒相关*##################################*/

function showUserRel(userId){
	new dialog().showWin($.i18n.prop('verdor.permissionJs.addPromotionReminder')/*"添加晋级提醒"*/, 700,500, ctx + '/manager/admin/user/showUserWarnRelList/' + userId);
}

function searchUserWarn(){
	var searchParamArray = $('#form-userWarnItem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-userWarn-list').datagrid('load',searchParams);
}

function delUserWarn(){
	var selections = $('#datagrid-userWarn-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('delivery.message2')/*'没有选择任何记录！'*/,'info');
		return false;
	}
	$.messager.progress();
	var params = $.toJSON(selections);
	$.messager.confirm($.i18n.prop('verdor.permissionJs.operatingHints')/*"操作提示"*/, $.i18n.prop('verdor.permissionJs.deleteOperations')/*"确定删除操作吗？"*/, function (data) {  
		 if (data) {
				$.ajax({
					url:ctx+'/manager/admin/user/deleteUserWarnRel',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.progress('close');
							$.messager.show({
								title:$.i18n.prop('label.news')/*'消息'*/,
								msg:$.i18n.prop('label.delete.success')/*'删除成功'*/,
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-userWarn-list').datagrid('reload');
					}
				});
		 }
	});
}

function addUserWarn(userId){
	//加载信息
	$('#roleId').combobox({ 
	   url:ctx+'/manager/common/warning/roleSelect',
       editable:false,
       cache: false,
       required:true,
       valueField:'id',   
       textField:'name',
       onSelect: function (object) {
           _zhbid.combobox({
               disabled: false,
          	   url:ctx+'/manager/admin/user/getWarnUserListByRoleId/'+object.id,
               valueField: 'id',
               textField: 'name'
           }).combobox('clear');
       }
  }); 
	
	var _zhbid = $('#roleUserId').combobox({
        disabled: true,
        valueField: 'id',
        textField: 'name'
    });
	$('#win-userWarn-addoredit').window('open'); 
	$('#userWarn_form').form('clear');
	$('#userId').val(userId);
}


function submitUserWarn(){
	$.messager.progress();
	$('#userWarn_form').form('submit',{
		ajax:true,
		url:ctx+'/manager/admin/user/addUserWarnRel',
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
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,$.i18n.prop('label.operateOK')/*"操作成功"*/,'info');
				$('#win-userWarn-addoredit').window('close');
				$('#datagrid-userWarn-list').datagrid('reload');
			}else{
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,result.msg,'error');
			}
			}catch (e) {
				$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data,'error');
			}
		},
		error: function(data) {
			$.messager.alert($.i18n.prop('label.remind')/*'提示'*/,data,'error');
		}
	});
}







