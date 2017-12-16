<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.menu.menuManagement"/></title>
	<script type="text/javascript">
		function managerFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editUser('+r.id+');"><spring:message code="vendor.edit"/></a>';
		}
		function getTypeStatus(status) {
			if(status == 'Y')
				return '<spring:message code="vendor.menu.suppliers"/>';
			else if(status == 'N')
				return '<spring:message code="vendor.menu.purchaser"/>';
			else 
				return '';
		}
	</script>
</head>

<body>
<div class="easyui-panel" region="center" data-options="fit:true">
	<table id="treegrid-menu-list" class="easyui-treegrid"
		data-options="fit:true,
		url:'${ctx}/manager/admin/menu/getMenuEasyuiTreeGrid',method:'post',singleSelect:true,
		idField:'id',treeField:'viewName',animate:true,onContextMenu:onContextMenu,rownumbers:true,
		toolbar:'#toolbar-menu',onLoadSuccess:gridLoadSuc,onEndEdit:gridLoadSuc,onCancelEdit:gridLoadSuc
		"
		>
		<thead><tr>
		<th data-options="field:'id',hidden:true"></th>
		<th data-options="field:'viewName',editor:'text'"><spring:message code="vendor.appellation"/></th>
		<th data-options="field:'viewCode',editor:'text'"><spring:message code="vendor.coding"/></th>
		
		<th data-options="field:'isVendor',width:100,formatter:function(v,r,i){return getTypeStatus(v);},editor:
					 			{type:'combobox',
		                           options:{
		                               data:
		                               	[{'id':'Y','text':'供应商'},
		                               	{'id':'N','text':'采购商'}],   
		                               valueField:'id',
		                               textField:'text',
		                               required:true,
		                               editable:false
	                               }
	                           }"><spring:message code="vendor.menu.roleType"/><!-- 角色类型 --></th> 
		
		<th data-options="field:'viewNameZH',editor:'text'"><spring:message code="vendor.menu.remark"/></th>
		<th width="400" data-options="field:'viewUrl',editor:'text'"><spring:message code="vendor.menu.path"/></th>
		<th width="100" data-options="field:'operate',formatter:fmt_upAndDown"><spring:message code="vendor.operation"/></th>
		</tr></thead>
	</table>
  </div>
	<div id="menu-menu" class="easyui-menu">
		<div data-options="iconCls:'icon-add'" onclick="addNewMenu(0)"><spring:message code="vendor.menu.addLevelMenu"/></div>
		<div data-options="iconCls:'icon-add'" onclick="addNewMenu(1)"><spring:message code="vendor.menu.lowerMenu"/></div>
		<div data-options="iconCls:'icon-copy'" onclick="copyMenu()"><spring:message code="vendor.menu.copyNew"/><!-- 复制新增 --></div>
		<div data-options="iconCls:'icon-edit'" onclick="editMenu()"><spring:message code="vendor.menu.editMenu"/><!-- 编辑菜单 --></div>
		<div data-options="iconCls:'icon-delete'" onclick="deleteMenu()"><spring:message code="vendor.menu.deleteMenu"/><!-- 删除菜单 --></div>
	</div>
	<div id="toolbar-menu" style="padding:5px;">
		<a href="javascript:;" onclick="updateMenu()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true"></a>
	</div>
	<div id="win-menu-add" class="easyui-window" title="新增菜单" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-menu-add" method="post" >
				<input id="parentId" name="parentId" type="hidden"/>
				<input id="beforeId" name="beforeId" type="hidden"/>
				<input id="viewType" name="viewType" value="0" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="vendor.menu.nameMenu"/>菜单名称:</td><td><input class="easyui-textbox" id="viewName" name="viewName" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td><spring:message code="vendor.menu.menuNumber"/><!-- 菜单编号 -->:</td><td><input class="easyui-textbox" name="viewCode" type="text"
						data-options="required:true"
					/></td>
				</tr>
								<tr>
					<td><spring:message code="vendor.menu.suppliers"/><!-- 供应商 -->:</td>
						<td><select  id="isVendor" class="easyui-combobox" name="isVendor" data-options="required:true,width:'138'">
							<option value="Y"><spring:message code="vendor.menu.suppliers"/><!-- 供应商 --></option>
							<option value="N"><spring:message code="vendor.menu.purchaser"/><!-- 采购商 --></option>
						</select></td>
					
				</tr>

				
				<tr>
					<td><spring:message code="vendor.menu.menuPath"/><!-- 菜单路径 -->:</td><td><input class="easyui-textbox" name="viewUrl" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddMenu()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-menu-add').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
<script type="text/javascript">
var curSeledMenuId = null;
function onContextMenu(e,row){
	e.preventDefault();
	$(this).treegrid('select',row.id);
	$('#menu-menu').menu('show',{
		left:e.pageX,
		top:e.pageY
		
	});
}

/**
 * 新增一个菜单节点
 * @type 0:平级 1:下级
 */
function addNewMenu(type){
	$('#win-menu-add').window('open');
	//赋值
	var selectedMenu = $('#treegrid-menu-list').treegrid('getSelected');
	//如果为平级节点
	if(type==0){
		$('#parentId').val(selectedMenu._parentId);
		$('#beforeId').val(selectedMenu.id);
	}else if(type==1){
		$('#parentId').val(selectedMenu.id);
		$('#beforeId').val(0);
	}
}

function copyMenu(){
	var selectedMenu = $('#treegrid-menu-list').treegrid('getSelected');
	delete selectedMenu.itemList;
	$.ajax({
		url:'${ctx}/manager/admin/menu/copyMenu',
		type:'POST',
		data:selectedMenu,
		dataType : 'json',
		success:function(data){
			$.messager.progress('close');
			if(data.success){
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.menu.copySuccess"/>'/* 复制成功 */,'info');
				$('#treegrid-menu-list').treegrid('reload');
			}
		}
	});
}

/**
 * 提交新增菜单
 */
function submitAddMenu(){
	$('#form-menu-add').form('submit',{
		url:'${ctx}/manager/admin/menu/saveMenu',
		success:function(data){
			data = $.evalJSON(data);
			if(data.success){
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.menu.saveSuccess"/>'/* 保存成功 */,'info');
				$('#treegrid-menu-list').treegrid('reload');
			}else{
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data.msg,'error');
			}
		}
	});
}

/** 编辑菜单 */
function editMenu(){
	var selectedNode = $('#treegrid-menu-list').treegrid('getSelected');
	var selectedId = selectedNode.id;
	//如果不等于正在编辑的行，则询问
	if(curSeledMenuId!=null && selectedId!=curSeledMenuId){
		$.messager.confirm('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.menu.doWantSaveEdits"/>？'/* 有未保存的编辑,您要保存后编辑吗 */,function(r){
			if(r){
				updateMenu();
			}else{
				$('#treegrid-menu-list').treegrid('cancelEdit',curSeledMenuId);
			}
			$('#treegrid-menu-list').treegrid('beginEdit',selectedId);
			curSeledMenuId = selectedId;
		});
	}else{
		$('#treegrid-menu-list').treegrid('beginEdit',selectedId);
		curSeledMenuId = selectedId;
	}
	
}

/*
 * 更新菜单
 * @menu 菜单对象
 */
function updateMenu(menuId){
	$('#treegrid-menu-list').treegrid('endEdit',curSeledMenuId);
	var	menu = $('#treegrid-menu-list').treegrid('find',curSeledMenuId)
	if(menuId!=null){
		menu = $('#treegrid-menu-list').treegrid('find',menuId)
	}
	//不需要子集
	delete menu.itemList;
	$.messager.progress({
		title:'<spring:message code="vendor.prompting"/>',/* 提示 */
		msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
	});
	//var params = $.toJSON(menu);
	//debugger;
	$.ajax({
		url:'${ctx}/manager/admin/menu/updateMenu',
		type:'POST',
		data:menu,
		//contentType:'application/json',
		//默认为application/x-www-form-urlencoded，
		//那么springmvc都可以处理，而且传输为json对象就行，如果要传json字符串,必须要用requestBody接收;
		//而且要用application/json文档类型
		dataType : 'json',
		success:function(data){
			$.messager.progress('close');
			if(data.success){
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.menu.updateSuccessful"/>'/* 更新成功 */,'info');
				$('#treegrid-menu-list').treegrid('reload');
			}
		}
	});
}

/** 监听菜单的点击事件
function menuClick(row){
	var selectedNode = $('#datagrid-user-list').treegrid('getSelected');
	var selectedId = selectedNode.id;
	if(selectedId!=curSeledMenuId){
		$('#datagrid-user-list').treegrid('endEdit',curSeledMenuId);
		curSeledMenuId = null;
	}
}
 */
 
/*
 * 移动菜单位置
 * @id 移动的菜单ID
 * @moveType 移动类型 0：上移，1：下移
 */ 
function moveMenu(id,moveType){
	 $.messager.progress({
			title:'<spring:message code="vendor.prompting"/>',/* 提示 */
			msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
		});
	 $.ajax({
			url:'${ctx}/manager/admin/menu/moveMenu',
			type:'POST',
			data:{id:id,moveType:moveType},
			//contentType:'application/json',
			//默认为application/x-www-form-urlencoded，
			//那么springmvc都可以处理，而且传输为json对象就行，如果要传json字符串,必须要用requestBody接收;
			//而且要用application/json文档类型
			dataType : 'json',
			success:function(data){
				$.messager.progress('close');
				if(data.success){
					//$.messager.alert('提示','更新成功','info');
					$('#treegrid-menu-list').treegrid('reload');
				}
			}
		}); 
}
 
/**
 * 删除菜单
 */
function deleteMenu(){
	var selectedMenu = $('#treegrid-menu-list').treegrid('getSelected');
	$.ajax({
		url : '${ctx}/manager/admin/menu/deleteMenu',
		data : {id:selectedMenu.id},
		dataType : 'json',
		type : 'POST',
		success : function(data){
			if(data.success){
				$.messager.alert('<spring:message code="vendor.prompting"/>/* 提示 */','<spring:message code="vendor.menu.deleteMenuSuccess"/>!'/* 删除菜单成功 */,'info');
				$('#treegrid-menu-list').treegrid('reload');
			}
		}
	});
}

/** 上移下移列的渲染 */
function fmt_upAndDown(v,r,i){
	return '<a class="wait-up" href="javascript:;" onclick="moveMenu('+r.id+',0)"></a><a href="javascript:;" class="wait-down" onclick="moveMenu('+r.id+',1)"></a>';
}
 
/** 树状grid加载后对上移下移列的渲染 */
function gridLoadSuc(data){
	$('.wait-up').linkbutton({
		iconCls:'icon-arrow_up',
		plain:true
	});
	$('.wait-down').linkbutton({
		iconCls:'icon-arrow_down',
		plain:true
	});
} 
 
 

</script>
</body>
</html>
