<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>采购计划管理</title>
	<script type="text/javascript">
		function monthFmt(v,r,i) {
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showPlanDetail('+ r.id +');">' + r.plan.month + '</a>';
		} 
	</script>   
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-purchaseplan-list" title="采购计划列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/purchaseplanvendor',method:'post',singleSelect:false,
		toolbar:'#purchaseplanListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'month',formatter:monthFmt">月份</th>
		<th data-options="field:'buyerCode',formatter:function(v,r,i){return r.plan.buyer.code;}">采购方编码</th>
		<th data-options="field:'buyerName',formatter:function(v,r,i){return r.plan.buyer.name;}">采购方名称</th>   
		<th data-options="field:'createUserName',formatter:function(v,r,i){return r.plan.createUserName;}">创建人</th>
		<th data-options="field:'createTime',formatter:function(v,r,i){return r.plan.createTime;}">创建时间</th>
		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}">确认状态</th>
		</tr></thead>
	</table>
	<div id="purchaseplanListToolbar" style="padding:5px;">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="confirmPlan()">确认</a>
		</div>
		<div>
			<form id="form-purchaseplan-search" method="post">
			月份：<input type="text" name="search-LIKE_plan.month" class="easyui-textbox" style="width:80px;"/>
			创建人：<input type="text" name="search-LIKE_createUserName" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchasePlan()">查询</a>
			</form>
		</div>
	</div>
	
	<!-- 计划详情 -->
	<div id="win-planitem-addoredit" class="easyui-window" title="采购计划详情" style="width:600px;height:400px"
		data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="purchaseplanListitemToolbar" style="padding:5px;">
				<form id="form-purchaseplanitem-search">
					<input id="id" name="id" value="-1" type="hidden"/>    
				月份：<input type="text" id="month" name="month" class="easyui-textbox" style="width:80px;" data-options="required:true"/>
				创建人：<input type="text" id="createUserName" name="createUserName" class="easyui-textbox" style="width:80px;" data-options="required:true"/>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="exportPurchasePlan()">导出</a>
				</form>
			</div>
			<table id="datagrid-purchaseplanitem-list" title="采购计划详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#purchaseplanitemListToolbar',
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendorPlan.vendor.name;}">供应商</th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
				<th data-options="field:'planQty'">需求数量</th>   
				</tr></thead>
			</table>
		</div>
	</div>
<script type="text/javascript">
//查询
function searchPurchasePlan(){
	var searchParamArray = $('#form-purchaseplan-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplan-list').datagrid('load',searchParams);
}

// 查看计划详情
function showPlanDetail(id) {
	$('#win-planitem-addoredit').window({
		iconCls:'icon-add',
		title:'采购计划详情'
	});  
	$('#form-purchaseplanitem-search').form('clear');
	$('#win-planitem-addoredit').window('open');
	//获取主单信息
	//$('#form-purchaseplanitem-search').form('load','${ctx}/manager/order/purchaseplanvendor/getPlan/'+id);  
	$.ajax({   
		url:'${ctx}/manager/order/purchaseplanvendor/getPlan/'+id,
		type:'POST',
		dataType:"json",
		contentType : 'application/json',
		success:function(data){ 
			$('#id').val(data.id);            
			$('#month').textbox('setValue', data.plan.month);             
			$('#createUserName').textbox('setValue', data.createUserName);           
		}   
	});    
	
	
	//显示计划详情
	var searchParamArray = $('#form-purchaseplanitem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplanitem-list').datagrid({   
    	url:'${ctx}/manager/order/purchaseplanvendor/planitem/' + id       
	});
	//$('#datagrid-purchaseplanitem-list').datagrid('load',searchParams);
}

function confirmPlan() {
	var selections = $('#datagrid-purchaseplan-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	for(i = 0;i < selections.length;i++) {
		 if(selections[i].confirmStatus != null && selections[i].confirmStatus == '1') {
			 $.messager.alert('提示','包含已确认记录无法重复发布！','error');
			return false;
		}   
    } 
	var params = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/order/purchaseplanvendor/confirmPlan',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.show({
				title:'消息',
				msg:'确认采购计划成功',
				timeout:2000,
				showType:'show',
				style:{
					right:'',
					top:document.body.scrollTop+document.documentElement.scrollTop,
					bottom:''
				}
			});
			$('#datagrid-purchaseplan-list').datagrid('reload');
		}   
	});
}

//导出
function exportPurchasePlan() {
	$('#form-purchaseplanitem-search').form('submit',{
		url: '${ctx}/manager/order/purchaseplanvendor/exportExcel',
		success:function(data){
			$.messager.progress('close');
		}
	});
}
</script>
</body>
</html>
