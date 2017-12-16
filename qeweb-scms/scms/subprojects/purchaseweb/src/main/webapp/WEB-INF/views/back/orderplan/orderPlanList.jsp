<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>采购计划管理</title>
	<script type="text/javascript">
		function monthFmt(v,r,i) {
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showPlanDetail('+ r.id +');">' + v + '</a>';
		} 
		
		function vendorPlanFmt(v,r,i) {
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showVendorPlanDetail('+ r.id +');">查看</a>';
		} 
		
		function modifyFmt(v,r,i) {
			if(r.publishStatus == 1)
				return '';  
			return '<a href="javascript:;" class="easyui-linkbutton"  data-options="plain:true" onclick="showPlanDetail('+ r.id +',0);">修改</a>';
		} 
		
		var editIndex = undefined;
		function endEditing(){
			if (editIndex == undefined){return true}
			if ($('#datagrid-purchaseplanitem-list').datagrid('validateRow', editIndex)){
				$('#datagrid-purchaseplanitem-list').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickCell(index, field){
			if (endEditing()){
				$('#datagrid-purchaseplanitem-list').datagrid('selectRow', index)
						.datagrid('editCell', {index:index,field:field});
				editIndex = index;
			}
		}
	</script>   
</head>
<body style="margin:0;padding:0;">
	<table id="datagrid-purchaseplan-list" title="采购计划列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/purchaseplan',method:'post',singleSelect:false,
		toolbar:'#purchaseplanListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'month',formatter:monthFmt">月份</th>
		<th data-options="field:'buyerCode',formatter:function(v,r,i){return r.buyer.code;}">采购方编码</th>
		<th data-options="field:'buyerName',formatter:function(v,r,i){return r.buyer.name;}">采购方名称</th>  
		<th data-options="field:'createUserName'">创建人</th>
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}">发布状态</th>
		<th data-options="field:'publishTime'">发布时间</th>
		<th data-options="field:'createTime'">创建时间</th>
<!-- 		<th data-options="field:'view',width:'80',formatter:vendorPlanFmt">供应商计划</th>   -->
		<th data-options="field:'modify',formatter:modifyFmt">操作</th> 
		</tr></thead>
	</table>
	<div id="purchaseplanListToolbar" style="padding:5px;">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="publishPlan()">发布</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="importPlan()">导入当月计划</a>
		</div>
		<div>
			<form id="form-purchaseplan-search" method="post">
			月份：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:80px;"/>
			创建人：<input type="text" name="search-LIKE_createUserName" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchasePlan()">查询</a> 
			</form>
		</div>
	</div>
	
	<!-- 导入计划 -->
	<div id="win-plan-import" class="easyui-window" title="导入计划" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-plan-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/order/purchaseplan/filesUpload"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
					模板：<a href="javascript:;" onclick="File.download('WEB-INF/template/PurchasePlan.xls','采购计划导入模版')">采购计划导入模版.xls</a> 
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveimportplan();">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-plan-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
	
	<!-- 计划详情 -->
	<div id="win-planitem-addoredit" class="easyui-window" title="采购计划详情" style="width:600px;height:400px"
		data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="purchaseplanListitemToolbar" style="padding:5px;">
				<div>
					<form id="form-purchaseplanitem-search">
						<input id="id" name="id" value="-1" type="hidden"/>
					月份：<input type="text" name="month" class="easyui-textbox" style="width:80px;" data-options="required:true" readonly="readonly"/>
					创建人：<input type="text" name="createUserName" class="easyui-textbox" style="width:80px;" data-options="required:true" readonly="readonly"/>
					创建时间：<input type="text" name="createTime" class="easyui-textbox" style="width:120px;" data-options="required:true" readonly="readonly"/>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="exportPurchasePlan()">导出</a>
					<a href="javascript:;" id="modifySave" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="savePurchasePlan()">保存</a>
					</form>
				</div>
			</div>
			<table id="datagrid-purchaseplanitem-list" title="采购计划详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#purchaseplanitemListToolbar',
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20],
				onClickCell: onClickCell" "
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
				<th data-options="field:'itemNo'">行号</th>
				<th data-options="field:'totalPlanQty',editor:'numberbox',required:true">需求数量</th>   
				<th data-options="field:'planRecTime'">需求数量</th>   
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
function showPlanDetail(id, type) {
	$('#win-planitem-addoredit').window({
		iconCls:'icon-add',
		title:'采购计划详情'
	});  
	$('#form-purchaseplanitem-search').form('clear');
	$('#win-planitem-addoredit').window('open');
	$('#form-purchaseplanitem-search').form('load','${ctx}/manager/order/purchaseplan/getPlan/'+id);
	if(0 == type) {
		$("#modifySave").show();
	} else {
		$("#modifySave").hide();  
	}
	
	//显示计划详情
	$('#datagrid-purchaseplanitem-list').datagrid({   
    	url:'${ctx}/manager/order/purchaseplan/planitem/' + id     
	});
}

//发布
function publishPlan() {
	var selections = $('#datagrid-purchaseplan-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var rows = $('#datagrid-purchaseplan-list').datagrid('getRows');
	for(i = 0;i < rows.length;i++) {
		 if(rows[i].publishStatus != null && rows[i].publishStatus == '1') {
			 $.messager.alert('提示','包含已发布记录无法重复发布！','error');
			return false;
		}   
    } 
	var params = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/order/purchaseplan/publishPlan',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.show({
				title:'消息',
				msg:'发布采购计划成功',
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

//修改采购计划
function savePurchasePlan() {
	$.messager.progress();
	if (endEditing()){
		$('#datagrid-purchaseplanitem-list').datagrid('acceptChanges');    
	}  
 	var rows = $('#datagrid-purchaseplanitem-list').datagrid('getRows');
	for(i = 0;i < rows.length;i++) {
		 if(rows[i].totalPlanQty == null || rows[i].totalPlanQty == '') {
			$.messager.progress('close');
			$.messager.alert('提示','需求数量不能为空','error');
			return false;
		}   
    } 
	var o =$('#datagrid-purchaseplanitem-list').datagrid('getData'); 
	var datas = encodeURI(JSON.stringify(o)); 
  	$.ajax({
	 	url:'${ctx}/manager/order/purchaseplan/updatePlan', 
        type: 'post',
        data:  "datas=" + datas +"&" + $('#form-purchaseplanitem-search').serialize(), 
        dataType:"json",
        success: function (data) {
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.alert('提示', data.message ,'info');
					$('#win-planitem-addoredit').window('close');
					$('#datagrid-purchaseplan-list').datagrid('reload'); 
				}else{
					$.messager.alert('提示',data.message,'error');
				}
			}catch (e) {
				$.messager.alert('提示',e,'error'); 
			} 
       	}
      }); 
}

//导入当月计划
function importPlan() {
	$('#form-plan-import').form('clear');   
	$('#win-plan-import').window('open');  
}

function saveimportplan(){
	$.messager.progress();
	$('#form-plan-import').form('submit',{
		ajax:true,
		iframe: true,    
		url:'${ctx}/manager/order/purchaseplan/filesUpload', 
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
				$.messager.alert('提示','导入采购计划成功','info');
				$('#win-plan-import').window('close');
				$('#datagrid-purchaseplan-list').datagrid('reload');         
			}else{
				$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
			}
			}catch (e) {  
				$.messager.alert('提示',data,'error');
			}
		}
	});
}   

//导出
function exportPurchasePlan() {
	$('#form-purchaseplanitem-search').form('submit',{
		url: '${ctx}/manager/order/purchaseplan/exportExcel',
		success:function(data){
			$.messager.progress('close');
		}
	});
}
</script>
</body>
</html>
