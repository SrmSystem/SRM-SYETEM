<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>接口信息</title>
	<script type="text/javascript">
		function optFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showLog('+ r.id +');">日志</a>&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showItem('+ r.id +');">详情</a>';
		}
		function styleResult(a,b,c){
			if (a = '失败')
				return 'background-color: red';
		}
		function styleStatus(a,b,c){
			if (a = '未完成')
				return 'background-color: red';
		}
	</script>
</head>

<body style="margin:0;padding:0;">

	<table id="datagrid-list" title="接口数据列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/interface/msg/list',method:'post',singleSelect:false,
		toolbar:'#msgListToolbar',
		pagination:true,rownumbers:true,pageSize:50,pageList:[50,100]"
		>
		<thead><tr>   
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'opt',formatter:optFmt">操作</th>
		<th data-options="field:'code'">接口编号</th>
		<th data-options="field:'name'">接口名称</th>
		<th data-options="field:'os'">对接系统</th>
		<th data-options="field:'result',styler:styleResult">执行结果</th>
		<th data-options="field:'status',styler:styleStatus">完成状态</th>
		<th data-options="field:'beginTime'">开始时间</th>
		<th data-options="field:'endTime'">结束时间</th>
		
		</tr></thead>
	</table>
	<div id="msgListToolbar" style="padding:5px;">

		<div>
			<form id="form-search" method="post">
			接口编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>  
			接口名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			对接系统：<input type="text" name="search-LIKE_os" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchList()">查询</a>  
			</form>
		</div>
	</div>  
	
<div id="win-log-detail" class="easyui-window" title="日志详情" style="width:600px;height:400px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
			<div id="logToolbar">
				
				<form id="form-log-search">
					<input id="id" name="id" value="-1" type="hidden"/>
					<!-- <table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>清单号:</td><td><input class="easyui-textbox" id="billCode" name="billCode" type="text"  readonly="readonly" data-options="required:true"/></td>
							<td>合计:</td><td><input class="easyui-textbox" id="totalPrice" name="totalPrice" type="text" readonly="readonly" data-options="required:true"/></td> 
						</tr>
						<tr>
							<td>税率:</td><td><input class="easyui-textbox" id="tax" name="tax" type="text" readonly="readonly" data-options="required:true"/></td>
							<td>含税合计:</td><td><input class="easyui-textbox" id="totalTaxPrice" name="totalTaxPrice" type="text" readonly="readonly" data-options="required:true"/></td>
						</tr>
					</table> -->    
				</form>  
			</div>
			<table id="datagrid-log-list" class="easyui-datagrid" title="日志详情"
				data-options="fit:true,method:'post',singleSelect:false,
				toolbar:'#logToolbar',
				pagination:true,rownumbers:true,pageSize:50,pageList:[50,100]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'dmlType'">DML类型</th>
				<th data-options="field:'logType'">方法</th>
				<th data-options="field:'logContent'">内容</th>
				<th data-options="field:'sql'">SQL</th>
				</tr></thead>
			</table>
			
	</div>
	
	
	<div id="win-item-detail" class="easyui-window" title="日志明细" style="width:600px;height:400px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="itemToolbar" style="padding:5px;">
				
				<form id="form-item-search">
					<input id="id" name="id" value="-1" type="hidden"/>
					<!-- <table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
							<td>清单号:</td><td><input class="easyui-textbox" id="billCode" name="billCode" type="text"  readonly="readonly" data-options="required:true"/></td>
							<td>合计:</td><td><input class="easyui-textbox" id="totalPrice" name="totalPrice" type="text" readonly="readonly" data-options="required:true"/></td> 
						</tr>
						<tr>
							<td>税率:</td><td><input class="easyui-textbox" id="tax" name="tax" type="text" readonly="readonly" data-options="required:true"/></td>
							<td>含税合计:</td><td><input class="easyui-textbox" id="totalTaxPrice" name="totalTaxPrice" type="text" readonly="readonly" data-options="required:true"/></td>
						</tr>
					</table> -->    
				</form>  
			</div>
			<table id="datagrid-item-list" title="日志明细" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,fit:true,
				toolbar:'#itemToolbar',
				pagination:true,rownumbers:true,pageSize:50,pageList:[50,100]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'insId'">实例Id</th>
				<th data-options="field:'status'">状态</th>
				<th data-options="field:'finishFlag'" >完成标识</th>
				<th data-options="field:'errorFlag'">错误标识</th>
				<th data-options="field:'errorInfo'">错误信息</th>
				<th data-options="field:'content'">内容</th>
				</tr></thead>
			</table>
			
		</div>
	</div>
	
	
<script type="text/javascript">
function searchList(){
	var searchParamArray = $('#form-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-list').datagrid('load',searchParams);
}

//详情
function showLog(id) {
	$('#form-log-search').form('clear');
	$('#win-log-detail').window('open');
	$('#datagrid-log-list').datagrid({
    	url:'${ctx}/manager/interface/msg/showInfos/' + id     
	});
}

function showItem(id) {
	$('#form-item-search').form('clear');
	$('#win-item-detail').window('open');
	$('#datagrid-item-list').datagrid({
    	url:'${ctx}/manager/interface/msg/showItems/' + id     
	});
}


</script>   
</body>
</html>
