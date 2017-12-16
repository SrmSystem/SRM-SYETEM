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
		$.messager.confirm('提示', '确定执行此操作吗？', function (data) {
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
					$.messager.alert('提示', '保存数据权限成功','info');
				}else{
					$.messager.alert('提示', '保存数据权限失败','error');
				}
			}
		});
	}
}

function managerFmt(v,r,i){
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editUser('+r.id+');">编辑</a>';
}
function managerRightFmt(v,r,i){
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openData('+r.id+');">数据权限</a>';
}
function userDataFmt(v,r,i) {
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="User.dataSet('+r.id+','+r.roleId+')">设置</a>'
	+'&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="User.dataClear('+r.id+','+r.roleId+')">清除</a>';
}
function xuanzhe() {
	$("#companyId").val("");
	var selections = $('#datagridss').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	if(selections.length>1){
		$.messager.alert('提示','只能选择一条记录！','info');
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
	$('#datagridss').datagrid({url: ctx+'/manager/admin/org'
	});
}
function searchUser(){
	var searchParamArray = $('#form-user-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-user-list').datagrid('load',searchParams);
}

function addUser(){
	$('#win-user-addoredit').window({
		iconCls:'icon-add',
		title:'新增用户'
	});
	$('#loginName').textbox('enable');
	$('#form-user-addoredit').form('clear');
	$('#id').val(0);
	/* $('#companyId').combobox("setValue",<%=user.orgId%>);
	//$('#companyId').combobox('readonly');*/
	$('#win-user-addoredit').window('open');
}

function submitAddorEditUser(){
	var url = ctx +'/manager/admin/user/addNewUser';
	var sucMeg = '添加用户成功！';
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = ctx +'/manager/admin/user/update';
		sucMeg = '编辑用户成功！';
	}
	$.messager.progress({
		title:'提示',
		msg : '提交中...'
	});
	
	if(isNaN($('#companyId').val())){
		$.messager.progress('close');
		alert("上级组织填写有误!");
		return false;
	}
	$('#form-user-addoredit').form('submit',{
		ajax:true,
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
				var result = eval('('+data+')');
			if(result.success){
				$.messager.alert('提示',sucMeg,'info');
				$('#win-user-addoredit').window('close');
				$('#datagrid-user-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		},
		error: function(data) {
			$.messager.alert('提示',data,'error');
		}
	});
}

function deleteUser(){
	var selections = $('#datagrid-user-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var params = $.toJSON(selections);
	$.messager.confirm('提示','确定删除操作？',function(r){
		if(r){
			$.ajax({
				url: ctx + '/manager/admin/user/deleteUser',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
						$.messager.show({
							title:'消息',
							msg:'删除用户成功',
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
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var params = $.toJSON(selections);
	$.ajax({
		url: ctx + '/manager/admin/user/resetPass',
		type:'POST',
		data:params,
		contentType : 'application/json',
		success:function(data){
				$.messager.show({
					title:'消息',
					msg:'重置密码成功',
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
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	if(selections.length>1){
		$.messager.alert('提示','只能选择一条记录！','info');
		return false;
	}
	$("#companyId").val(selections[0].company.id);
	$("#companyName").textbox('setText',selections[0].company.name);
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