<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>组织管理</title>
	<script type="text/javascript">
		function managerFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editOrg('+r.id+');">编辑</a>';
		}
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-org-list" title="组织列表" class="easyui-datagrid"
		data-options="
		fit:true,
		url:'${ctx}/manager/admin/org',method:'post',singleSelect:false,
		toolbar:'#orgListToolbar',queryParams:{search_EQ_orgType:0},
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'manager',formatter:managerFmt">操作</th>
		<th data-options="field:'code'">编码</th>
		<th data-options="field:'name'">名称</th>
		<th data-options="field:'parentOrgName'">上级组织名称</th>
		<th data-options="field:'registerTime'">注册时间</th>
		<th data-options="field:'_orgType'">组织级别</th>
		<th data-options="field:'_roleType'">组织类型</th>
		</tr></thead>
	</table>
	<div id="orgListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addOrg()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteOrg()">废除</a>
		</div>
		<div>
			<form id="form-org-search" method="post">
			编码：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
			名称：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
			组织类型：<input name="search_EQ_roleType" class="easyui-combobox" style="width:120px" value=""
				data-options="url:'${ctx}/common/easyuiselect/getOrgRoleType/true',panelHeight:'auto'"
			/>
			<input type="hidden" name="search_EQ_orgType" value="0"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrg()">查询</a>
			</form>
		</div>
	</div>
	<div id="win-org-addoredit" class="easyui-dialog" title="新增组织" style="width:400px;height:260px"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
			<form id="form-org-addoredit" method="post"  class="baseform" >
				<input id="id" name="id" value="0" type="hidden"/>
				<input name="orgType" value="0" type="hidden"/>
				<input name="parentId" value="0" type="hidden"/>
				<input name="abolished" value="0" type="hidden"/>
				<div>
			          <label>编码:</label>
			          <input class="easyui-textbox" name="code" type="text"
							data-options="disabled:true"
						/>
			        </div>
					<div>
			          <label>名称:</label>
			          <input class="easyui-textbox" id="name" name="name" type="text"
							data-options="required:true"
						/>
			        </div>
					<div>
			          <label>组织类型:</label>
			          <input id="win-org-addoredit-roleType" name="roleType" class="easyui-combobox"
						data-options="url:'${ctx}/common/easyuiselect/getOrgRoleType/false',panelHeight:'auto',required:true"
						/>
			        </div>
			</form>
		<div id="dialog-adder-bb">
			<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="submitAddorEditOrg()">提交</a>
			<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton"  onclick="crel()">重置</a>
		</div>
	</div>
<%-- 	<div id="pp" class="easyui-pagination" data-options="total:${fn:length(users)},pageSize:10" style="background:#efefef;border:1px solid #ccc;"></div> --%>
<script type="text/javascript">
function searchOrg(){
	var searchParamArray = $('#form-org-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-org-list').datagrid('load',searchParams);
}
function crel(){
	$('#name').textbox('setValue','');
	$('#legalPerson').textbox('setValue','');
	$('#win-org-addoredit-roleType').textbox('setValue','');
}
function addOrg(){
	$('#win-org-addoredit').window({
		iconCls:'icon-add',
		title:'新增组织'
	});
	$('#tr-org-code').css('display','none');
	$('#form-org-addoredit').form('reset');
	$('#id').val(0);
	$('#win-org-addoredit').window('open');
	$('#win-org-addoredit-roleType').combobox('setValue','0');
}


function submitAddorEditOrg(){
	var url = '${ctx}/manager/admin/org/addNewOrg';
	var sucMeg = '添加组织成功！';
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = '${ctx}/manager/admin/org/update';
		sucMeg = '编辑组织成功！';
	}
	$.messager.progress({title:'提交中',msg:'请等待......'});
	$('#form-org-addoredit').form('submit',{
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
				$('#win-org-addoredit').window('close');
				$('#datagrid-org-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		}
		
	});
}

function deleteOrg(){
	var selections = $('#datagrid-org-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var params = $.toJSON(selections); 
	$.messager.confirm("操作提示", "确定要执行操作吗？", function (data) {
        if (data) {
        	$.ajax({
        		url:'${ctx}/manager/admin/org/deleteOrg',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			if(data.success){
        				$.messager.show({
        					title:'消息',
        					msg:'删除组织成功',
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#datagrid-org-list').datagrid('reload');
        			}
        		}
        	});
        }
	});
}

function editOrg(id){
	$('#win-org-addoredit').window({
		iconCls:'icon-edit',
		title:'编辑组织'
	});
	$('#tr-org-code').css('display','');
	$('#win-org-addoredit').window('open');
	$('#form-org-addoredit').form('load','${ctx}/manager/admin/org/getOrg/'+id);
}

function selectOrgType(r){
	if(r.value=='1'){
		var parentId = $('#form-org-addoredit-parentId').combotree('getValue');
		if(parentId==null || parentId==''){
			$.messager.alert('提示','需要选择上级组织','info');
			$('#win-org-addoredit-orgType').combobox('setValue','0');
			return false;
		}
	}
}

$(function(){
	
	
	
	
});

</script>
</body>
</html>
