<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>维度设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/dimensions.js"></script>
</head>

<body>
<div class="easyui-panel" region="center" data-options="fit:true">
	<table id="treegrid-dimen-list"  class="easyui-treegrid"
		data-options="url:'${ctx}/manager/vendor/performance/dimensions/getSetting',singleSelect:true,
		fit:true,idField:'id',treeField:'code',animate:true,onContextMenu:onContextDimen,rownumbers:true,
		toolbar:'#toolbar-dimen'">
		<thead><tr>
		<th width="2%" data-options="field:'id',hidden:true">序号</th>
		<th width="20%" data-options="field:'code'">维度编码</th>
		<th width="18%" data-options="field:'dimName',editor:'text'">维度名称</th>
		<th width="20%" data-options="field:'factoryType', formatter:function(v,r,i){
							if(v == '1')
								return '零部件的最低分';
							else if(v == '2')
								return '零部件的平均分';
							else if(v == '3')
								return '总分—零部件的扣分综合';
							else 
								return '';
						}, editor: {type: 'combobox', options: { valueField: 'factoryType', textField: 'name', data:[{factoryType:'1',name:'零部件的最低分'},{factoryType:'2',name:'零部件的平均分'},{factoryType:'3',name:'总分—零部件的扣分综合'}], panelHeight: 'auto', required: true } }">工厂计算模式</th>
		<th width="10%" data-options="field:'mappingScore'">结果映射列</th>
		<th width="20%" data-options="field:'remarks',editor:'text'">备注</th>
		<th width="6%" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
		</tr></thead>
	</table>
  </div>
	<div id="dimen-dimen" class="easyui-menu">
		<div data-options="iconCls:'icon-add'" onclick="addNewDimen(0)">新增平级维度</div>
		<div data-options="iconCls:'icon-add'" onclick="addNewDimen(1)">新增下级维度</div>
		<div data-options="iconCls:'icon-edit'" onclick="editDimen()">编辑维度</div>
		<div data-options="iconCls:'icon-accept'" onclick="dimensions.release()">启用</div>
		<div data-options="iconCls:'icon-remove'" onclick="dimensions.dels()">作废</div>
		<div data-options="iconCls:'icon-remove'" onclick="dimensions.deletes()">删除</div>
	</div>
	<div id="toolbar-dimen" style="padding:5px;">
		<a href="javascript:;" onclick="updateDimen()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">提交更新</a>
		<a href="javascript:;" onclick="addNewDimen(-1)" class="easyui-linkbutton" data-options="iconCls:'icon-save'">新增最上层维度</a>
	</div>
<div id="win-dimen-add" title="新增维度" style="width: 30%"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
		<div itemId="ct">
			<form id="form-dimen-add" method="post" class="baseform">
			    <input id="parentId" name="parentId" type="hidden"/>
				<!-- <div>
		          <label>维度编码:</label>
		          <input class="easyui-textbox" id="code" name="code" type="text"data-options="required:true"/>
		        </div> -->
				<div>
		          <label>维度名称:</label>
		          <input class="easyui-textbox" id="dimName" name="dimName" type="text"data-options="required:true"/>
		        </div>
		        <div>
		          <label>工厂计算模式:</label>
		          <select id="factoryType" name="factoryType" class="easyui-combobox"  data-options="required:true,editable:false">
							<option value='1'>零部件的最低分</option>
							<option value='2'>零部件的平均分</option>
							<option value='3'>总分—零部件的扣分综合</option>
						</select>
		        </div>
				<div>
		          <label>备注:</label>
		          <input class="easyui-textbox" id="remarks" name="remarks" type="text"/>
		        </div>
				<div>
		          <label>生效状态:</label>
		          <select id="abolished" name="abolished" class="easyui-combobox"  data-options="required:true,editable:false">
							<option value=''></option>
							<option value='0'>启用</option>
							<option value='1'>禁用</option>
						</select>
		        </div>
				<div>
		          <label>结果映射:</label>
		          <input id="mappingScore" name="mappingScore" class="easyui-combobox"  data-options="required:true,editable:false,disabled:true" />
		        </div>
			</form>
		</div>
		<div id="dialog-adder-bb">
			<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="submitAddDimen()">提交</a>
			<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-dimen-add').form('reset')">重置</a>
		</div>
	</div>
<script type="text/javascript">
$(function(){
	$('#treegrid-dimen-list').treegrid();
});
var curSeledDId=null;
function onContextDimen(e,row){
	e.preventDefault();
	$(this).treegrid('select',row.id);
	$('#dimen-dimen').menu('show',{
		left:e.pageX,
		top:e.pageY
		
	});
}

/**
 * 新增一个菜单节点
 * @type 0:平级 1:下级
 */
function addNewDimen(type){
	 $('#win-dimen-add').dialog();
	 $('#form-dimen-add').form('clear');
	 $('#abolished').combobox('select','0');
	 $('#win-dimen-add').window('open');
	 $('#mappingScore').combobox({
		 disabled : true
	 });
	 $('#win-dimen-add').window('autoSize');
	 if(type!=-1)
	 {
		//赋值
		var selecteddimen = $('#treegrid-dimen-list').treegrid('getSelected');
		//如果为平级节点
		if(type==0){
			$('#parentId').val(selecteddimen._parentId);
			if(selecteddimen._parentId==null || selecteddimen._parentId==0){
				 $('#mappingScore').combobox({
					    disabled : false,
						url : ctx+'/manager/vendor/performance/dimensions/getMappingScores',
						valueField : 'value',
						textField : 'text'
					 });
			}
		}else if(type==1){
			$('#parentId').val(selecteddimen.id);
		}
	 }else{
		 $('#mappingScore').combobox({
			    disabled : false,
				url : ctx+'/manager/vendor/performance/dimensions/getMappingScores',
				valueField : 'value',
				textField : 'text'
			 });
	 }
	
}

/**
 * 提交新增
 */
function submitAddDimen(){
	$('#form-dimen-add').form('submit',{
		url:ctx+'/manager/vendor/performance/dimensions/addDimensions',
		success:function(data){
			try{
				var obj = JSON.parse(data);
				if(obj.success) {
					$.messager.alert('提示','更新成功','info');
					$('#treegrid-dimen-list').treegrid('reload');
					$('#win-dimen-add').window('close');
				} else {
					$.messager.alert('提示',obj.msg,'error');
					$('#treegrid-dimen-list').treegrid('reload');
				}
			}catch(e) {
				$.messager.alert('提示',data,'error');
			}
		}
	});
}

/** 编辑菜单 */
function editDimen(){
	var selectedNode = $('#treegrid-dimen-list').treegrid('getSelected');
	var selectedId = selectedNode.id;
	//如果不等于正在编辑的行，则询问
	if(curSeledDId!=null && selectedId!=curSeledDId){
		$.messager.confirm('提示','有未保存的编辑,您要保存后编辑吗？',function(r){
			if(r){
				updateDimen(curSeledDId);
			}else{
				$('#treegrid-dimen-list').treegrid('cancelEdit',curSeledDId);
			}
			$('#treegrid-dimen-list').treegrid('beginEdit',selectedId);
			curSeledDId = selectedId;
		});
	}else{
		$('#treegrid-dimen-list').treegrid('beginEdit',selectedId);
		curSeledDId = selectedId;
	}
	
}

/*
 * 更新菜单
 * @dimen 菜单对象
 */
function updateDimen(Id){
	$('#treegrid-dimen-list').treegrid('endEdit',curSeledDId);
	var	dimen = $('#treegrid-dimen-list').treegrid('find',curSeledDId)
	if(Id!=null){
		dimen = $('#treegrid-dimen-list').treegrid('find',Id)
	}

	delete dimen.itemList;
	$.messager.progress({
		title:'提示',
		msg : '提交中...'
	});
	var params = $.toJSON(dimen);
	curSeledDId=null;
	 $.ajax({
			url:ctx+'/manager/vendor/performance/dimensions/updateDimensions',
			type:'POST',
			data:params,
			contentType:'application/json',
			dataType : 'json',
			success:function(data){
				$.messager.progress('close');
				if(data.success)
				{
					$.messager.alert('提示','更新成功','info');
					$('#treegrid-dimen-list').treegrid('reload');
				}
				else
				{
					$.messager.alert('提示',data.msg,'error');
					$('#treegrid-dimen-list').treegrid('reload');
				}
			},
			error:function(data) {
				$.messager.progress('close');
				$.messager.fail(data.responseText);
			}
		}); 
}
</script>
</body>
</html>
