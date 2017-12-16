<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.qeweb.scm.vendormodule.constants.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>向导模版管理</title>
	<script type="text/javascript">
	  var Constants = {
		surTemType : <%=VendorModuleTypeConstant.getSurveyTemplateTypeJson()%>//调查表模版类型	  
	  };
	</script>
	<script type="text/javascript" src="${ctx}/static/base/easyui/extension/datagrid-detailview.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorSurvey.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorNavSurvey.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorNavTemplate.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-vendorNavTemplate-list" title="向导模版列表" class="easyui-datagrid"
		data-options="
		fit:true,
		url:'${ctx}/manager/vendor/vendorNavTemplate',method:'post',singleSelect:false,
		toolbar:'#vendorNavTemplateListToolbar',queryParams:{'search-EQ_abolished':0},
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'oper',formatter:VendorNavTemplate.phaseSetFmt">操作</th>
		<th data-options="field:'code',formatter:VendorNavTemplate.viewFmt">向导模版编号</th>
		<th data-options="field:'name'">向导模版名称</th>
		<th data-options="field:'defaultFlag',align:'center',formatter:VendorNavTemplate.defaultFlagFmt">是否默认</th>
		<th data-options="field:'finishStatus',formatter:VendorNavTemplate.finishStatusFmt">完成状态</th>
		</tr></thead>
	</table>
	<div id="vendorNavTemplateListToolbar" style="padding:5px;">
		<div>
		<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="VendorNavTemplate.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="VendorNavTemplate.del()">删除</a> -->
		</div>
		<div>
			<form id="form" method="post">
			<input type="hidden" value="0" name="search-EQ_abolished"/>
			向导模版编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			向导模版名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchVendorNavTemplate()">查询</a>
			</form>
		</div>
	</div>
	<div id="win-vendorNavTemplate-view" class="easyui-window" title="模版详情" style="width:500px;height:400px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		  <div itemId="ct">
			<table class="easyui-datagrid" title="晋级详情" id="datagrid-vendorTemplatePhase">
			  <thead>
			    <tr>
			      <th data-options="field:'id',hidden:true"></th>
			      <th data-options="field:'templateId',hidden:true"></th>
			      <th data-options="field:'phaseId',hidden:true"></th>
			      <th data-options="field:'code'">阶段编码</th>
			      <th data-options="field:'name'">阶段名称</th>
			      <th data-options="field:'phaseSn'">晋级顺序</th>
			    </tr>
			  </thead>
			</table>
		  </div>
	</div>

<!-- 调查表预览弹出窗口 -->
   <div id="window-surveyPreview" class="easyui-window" data-options="closed:true,modal:true">
   </div>
<!-- 调查表重设弹出窗口 -->

   	<div id="win-reset" class="easyui-window" title="重设" style="width:500px;height:400px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		  <div itemId="ct">
			<table class="easyui-datagrid" title="晋级详情" itemId="datagrid-reset">
			  <thead>
			    <tr>
			      <th data-options="field:'id',checkbox:true"></th>
			      <th data-options="field:'operate',formatter:VendorNavSurvey.selectSurveyFmt">操作</th>
			      <th data-options="field:'code'">阶段编码</th>
			      <th data-options="field:'name'">阶段名称</th>
			      <th data-options="field:'phaseSn',editor:'numberbox'">晋级顺序</th>
			    </tr>
			  </thead>
			</table>
			<div >
			</div>
		  </div>
	</div>

<!-- 调查表选择 -->	
<div class="easyui-window" id="window-phaseSurvey" data-options="title:'选择调查表',width:700,height:500,
  modal:true,closed:true
">
  <input type="hidden" id="templatePhaseId"/>
  <table id="datagrid-vendorSurveyTemplate-list" title="调查表模版列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/vendor/vendorSurveyTemplate/all',
		method:'post',singleSelect:false,idField:'id',
		checkOnSelect:false,
		onCheckAll:VendorNavSurvey.onCheckAll,
		toolbar:'#vendorSurveyTemplateListToolbar'"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'">调查表模版编号</th>
		<th data-options="field:'name'">调查表模版名称</th>
		<th data-options="field:'templateType',formatter:VendorNavSurvey.surTemTypeFmt">模版类型</th>
		<th data-options="field:'beanId'">bean</th>
		<th data-options="field:'path'">路径</th>
		</tr></thead>
  </table>
  <div id="vendorSurveyTemplateListToolbar">
    <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="VendorNavSurvey.selectSurvey()">选择</a>
  </div>
</div>	

<script type="text/javascript">
function searchVendorNavTemplate(){
	var searchParamArray = $('#form').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-vendorNavTemplate-list').datagrid('load',searchParams);
}

function addVendorNavTemplate(){
	$('#win-vendorNavTemplate-addoredit').window({
		iconCls:'icon-add',
		title:'新增向导模版'
	});
	$('#code').textbox('enable');
	$('#form-vendorNavTemplate-addoredit').form('clear');
	$('#id').val(0);
	$('#win-vendorNavTemplate-addoredit').window('open');
}

function submitAddorEditvendorNavTemplate(){
	var url = '${ctx}/manager/vendor/vendorNavTemplate/addNewVendorNavTemplate';
	var sucMeg = '添加向导模版成功！';
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = '${ctx}/manager/vendor/vendorNavTemplate/update';
		sucMeg = '编辑向导模版成功！';
	}
	$.messager.progress();
	$('#form-vendorNavTemplate-addoredit').form('submit',{
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
				$('#win-vendorNavTemplate-addoredit').window('close');
				$('#datagrid-vendorNavTemplate-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		}
		
	});
}

function deleteVendorNavTemplate(){
	var selections = $('#datagrid-vendorNavTemplate-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var params = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/vendor/vendorNavTemplate/deleteVendorNavTemplate',
		type:'POST',
		data:params,
		contentType : 'application/json',
		success:function(data){
			
				$.messager.show({
					title:'消息',
					msg:'删除向导模版成功',
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});

				//$.messager.alert('提示','删除用户成功!','info');
				$('#datagrid-vendorNavTemplate-list').datagrid('reload');
			
		}
	});
}

function editVendorNavTemplate(id){
	$('#win-vendorNavTemplate-addoredit').window({
		iconCls:'icon-edit',
		title:'编辑向导模版'
	});
	$('#code').textbox('disable');
	$('#win-vendorNavTemplate-addoredit').window('open');
	$('#form-vendorNavTemplate-addoredit').form('load','${ctx}/manager/vendor/vendorNavTemplate/getVendorNavTemplate/'+id);
}

$(function(){
	
	
	
	
});

</script>
</body>
</html>
