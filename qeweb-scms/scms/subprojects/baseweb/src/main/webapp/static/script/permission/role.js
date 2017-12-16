var Role = {
	rowManagerFmt : function(v,r,i){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Role.openMenu('+r.id+')">菜单权限</a>'
		+'&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Role.openUser('+r.id+')">用户分配</a>'
		+'&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Role.openData('+r.id+')">数据权限</a>'
		+'&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Role.edit('+r.id+','+r.roleType+')">编辑</a>';
		
	},
	rowDataFmt : function(v,r,i) {
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Role.dataSet('+r.id+','+r.roleId+')">设置</a>'
		+'&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Role.dataClear('+r.id+','+r.roleId+')">清除</a>';
	},
	edit : function(id, roleType){
		editRole(id, roleType);
	},
	openUser : function(id){
		$('#win-role-user').window('open');
		//给角色ID赋值
		$('#win-role-user').getCmp('roleId').val(id);
		var $seler = $('#sel-role-user');
		$seler.html('');
		//异步获取已分配用户
		$.ajax({
			url:ctx+'/manager/admin/role/getRoleUser',
			data:{roleId:id},
			dataType:'json',
			success:function(data){
				var userList = data;
				for(var i=0;i<userList.length;i++){
					var n = userList[i];
					$seler.append('<option value="'+n.id+'">'+n.name+'</option>');
				}
			}
		});
	},
	openData : function(roleId){
		$('#win-role-data').window('open');
		//给角色ID赋值
		$('#win-role-data').getCmp('roleId').val(roleId);
		$('#datagrid-role-data-list').datagrid({
			url : ctx + '/manager/admin/roledata/' + roleId,
			queryParams : {}
		});
	},
	/**
	 * 角色数据权限设置
	 * @param rdcid
	 * @param roleId
	 */
	dataSet : function(rdcid, roleId) {
		var $dialog = $('#dialog-data');
		var url = ctx + '/manager/admin/roledata/getRoleDataList/' + roleId + '/' + rdcid;
		$dialog.dialog({
			href : url,
			onOpen : function() {
			}
		});
		$dialog.dialog('open');
	},
	/**
	 * 角色数据权限清除
	 * @param rdcid
	 * @param roleId
	 */
	dataClear : function(rdcid, roleId) {
		$.messager.confirm('提示', '确定执行此操作吗？', function (data) {
			if (data) {
				$.messager.progress();  
				$.ajax({
					url : ctx + '/manager/admin/roledata/clearRoleData/' + roleId + '/' + rdcid,
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
	addRoleData : function(roleId, roleDataCfgId) {
		var selections = $('#datagrid-role-data').datagrid('getSelections');
		var dataIds = '';
		for(var i = 0; i < selections.length; i ++) {
			dataIds += (selections[i].id + ',');
		}
		$("#dialog-data").dialog('close');
		$.messager.progress();  
		$.ajax({
			url:ctx+'/manager/admin/role/addRoleData',
			method:'post',
			data:{roleId:roleId,roleDataCfgId:roleDataCfgId, dataIds:dataIds},
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
	},
	selectUser : function(){
		var userList = $('#datagrid-user-list').datagrid('getSelections');
		if(userList.length<=0){
			$.messager.alert('提示','没有选择用户','warning');
			return;
		}
		var $seler = $('#sel-role-user');
		$.each(userList,function(i,n){
			if($seler.find('option[value="'+n.id+'"]').length>0){
				return true;
			}
			$seler.append('<option value="'+n.id+'">'+n.name+'</option>');
		});
	},
	removeUser : function(){
		var $seler = $('#sel-role-user');
		var $selUserList = $seler.find('option:selected');
		if($selUserList.length<=0){
			$.messager.alert('提示','没有选择要移除用户','warning');
			return;
		}
		$selUserList.each(function(i){
			$(this).remove();
		});
	},
	openMenu : function(id){//打开该角色的菜单权限
		$('#btn-addMenuRight').unbind('click');
		$('#win-role-menu').window('open');
		$('#btn-addMenuRight').click(function(i){
			Role.addMenuRight(id);
		});
//		$('#tree-role-menu').tree();
		//获取已选中的菜单
		$.ajax({
			url:ctx+'/manager/admin/menu/getRoleMenu',
			data:{roleId:id},
			dataType:'json',
			cache : false,
			success:function(data){
				var menuIdList = data;
				for(var i=0;i<menuIdList.length;i++){
					var node = $('#tree-role-menu').tree('find',menuIdList[i]);
					$('#tree-role-menu').tree('check',node.target);
				};
				var nodeList = $('#tree-role-menu').tree('getChecked',['checked','indeterminate']);
				$.each(nodeList,function(i,n){
					var flag = false;
					for(var j=0;j<menuIdList.length;j++){
						if(n.id==menuIdList[j]){
							flag = true;
							break;
						}
					};
					if(!flag){
						$('#tree-role-menu').tree('uncheck',n.target);
					}
				});
			}
			
		});
	},
	addRoleUser : function(){
		var $win = $('#win-role-user');
		var roleId = $win.getCmp('roleId').val();
		//组装要分配的用户ID
		var $select = $('#sel-role-user');
		var $userList = $select.find('option');
		var userIdList = [];
		$userList.each(function(i){
			userIdList.push($(this).val());
		});
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});
		$.ajax({
			url:ctx+'/manager/admin/role/addRoleUser',
			method:'post',
			data:{roleId:roleId,userIdList:userIdList},
			dataType:'json',
			success:function(data){
				$.messager.progress('close');
				if(data.success){
					$win.window('close');
					$.messager.alert('提示','分配用户成功！','info');
				}else{
					$.messager.alert('提示','分配用户失败！','error');
				}
			}
		});
	},
	addMenuRight : function(roleId){
		var nodeList = $('#tree-role-menu').tree('getChecked',['checked','indeterminate']);
		var menuList = [];
		var roleMenuList =[];
		$.each(nodeList,function(i,n){
			roleMenuList.push({roleId:roleId,viewType:0,viewId:n.id,viewPid:n.attributes.parentId});
		});
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});
		$.ajax({
			url:ctx+'/manager/admin/role/addMenuRight',
			method:'post',
			contentType:'application/json',
			data:$.toJSON(roleMenuList),
			dataType:'json',
			success:function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.alert('提示','添加权限成功','info');
				}
			}
		});
	}
		
}

$(function(){
	$("#form-role-addoredit").validate();
});

//$.extend($.fn.validatebox.defaults.rules, {    
//    remoteCheck: {    
//        validator: function(value,param){
//        	var params = {};
//
//        	var $param1 = $('input[name='+param[1]+"]:eq(0)");
//        	var $param2 = $('#'+param[2]);
////        	alert($param1.val());
//        	params[$param1.attr('name')] = $param1.val();
//        	params[$param2.attr('name')] = $param2.val();
//        	var flag = $.ajax({
//        	  url:param[0],
//        	  data:params,
//        	  type : 'post', 
//        	  dataType:'json',
//        	  async:false,
//        	  success:function(data){
//        		  return data;
//        	  }
//        	}).responseJSON;
//        	debugger;
//        	return flag;
//        },
//        message: 'Field do not match.'
//    }    
//}); 