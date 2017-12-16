<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>任务管理</title>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/taskList.js">
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-task-list" title="任务列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/task',method:'post',singleSelect:false,
		toolbar:'#taskListToolbar',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
		<thead><tr>
		<th data-options="field:'key',checkbox:true"></th>
    	<th data-options="field:'jobName',formatter:taskDetailFmt">任务名称</th>
		<th data-options="field:'jobGroup',formatter:function(v,r,i){return r.key.jobGroup;}">任务组</th>
		<th data-options="field:'jobClassName'">任务类</th>
		<th data-options="field:'description'">任务描述</th>
		<th data-options="field:'manager',formatter:managerFmt">管理</th>
		</tr></thead>
	</table>
	
	<div id="taskListToolbar" style="padding:5px;">
		<div>
<!-- 			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteTask()">删除</a> -->
		</div>
		<div>
			<form id="form-task-search" method="post">
			任务名称：<input type="text" name="search-LIKE_key.jobName" class="easyui-textbox" style="width:150px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchTask()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-task-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
		
	<div id="win-task-addoredit" class="easyui-window" title="修改任务" style="width:500px;height:280px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-task-addoredit" method="post" >
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<input name="schedName" type="hidden"/>
				<tr>
					<td>任务名称:</td><td><input class="easyui-textbox" id="jobName" name="jobName" type="text" data-options="required:true" style="width:350px" readonly="readonly"/></td>
				</tr>
				<tr>
					<td>任务组:</td><td><input class="easyui-textbox" id="jobGroup" name="jobGroup" type="text" data-options="required:true" style="width:350px" readonly="readonly"/></td>
				</tr>
				<tr>
					<td>任务类:</td><td><input class="easyui-textbox" name="jobClassName" type="text" data-options="required:true" style="width:350px" readonly="readonly"/></td>
				</tr>
				<tr>
					<td>描述:</td><td>
						<input class="easyui-textbox" id="desc" name="description" data-options="multiline:true" style="height:60px;width:350px"></input>
					</td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditTask()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#desc').textbox('clear')">重置</a>
				</div>
			</form>
		</div>
	</div>	
	
	<div id="win-taskdetail-addoredit" class="easyui-window" title=" " style="width:900px;height:400px"
	data-options="modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">  
			<div id="triggerToolbar" style="padding:5px;">
			    <div>
		            <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addTrigger()">新增触发器</a>
		        </div>
				<div>
					<form id="form-triggers-search" method="post">
					触发器名称：<input type="text" name="search-LIKE_key.triggerName" class="easyui-textbox" style="width:150px;"/>
					<input type="hidden" id="search-EQ_schedName" name="search-EQ_key.schedName">
					<input type="hidden" id="search-EQ_jobName" name="search-EQ_jobName">
					<input type="hidden" id="search-EQ_jobGroup" name="search-EQ_jobGroup">    
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchTriggers()">查询</a>
					</form>
		       </div>
			</div>
	
		   <table id="datagrid-trigger-list" title="任务详情" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,toolbar:'#triggerToolbar',
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]">
				<thead><tr>   
		    	<th data-options="field:'triggerName',formatter:function(v,r,i){return r.key.triggerName;}">触发器名称</th>
		    	<th data-options="field:'triggerGroup',formatter:function(v,r,i){return r.key.triggerGroup;}">触发器组名</th>
                <th data-options="field:'cronExpression',formatter:function(v,r,i){if(r.jobCron) return r.jobCron.cronExpression; else ''}">cron表达式</th>
				<th data-options="field:'nextFireTime',formatter:function(value,row,index){var unixTimestamp = new Date(value);
				return unixTimestamp.toLocaleString();}">下次触发时间</th>
				<th data-options="field:'prevFireTime',formatter:function(value,row,index){if(value == -1) return ''; var unixTimestamp = new Date(value);
				return unixTimestamp.toLocaleString();}">上次触发时间</th>	 
				<th data-options="field:'startTime',formatter:function(value,row,index){var unixTimestamp = new Date(value);
				return unixTimestamp.toLocaleString();}">开始时间</th>  
				<th data-options="field:'endTime',formatter:function(value,row,index){if(value == 0) return ''; var unixTimestamp = new Date(value);
				return unixTimestamp.toLocaleString();}">结束时间</th>
				<th data-options="field:'triggerState'">状态</th>
				<th data-options="field:'manager',formatter:triggersFmt">操作</th>
				</tr></thead>
			</table>
		</div>	
	</div>  
	
	<div id="win-trigger-add" class="easyui-window" title="新增触发器" style="width:400px;height:200px"
	     data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-trigger-add" method="post" >
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<input name="schedName" type="hidden"/> 
				<input name="jobName" type="hidden"/> 
				<input name="jobGroup" type="hidden"/>   
				<tr>
					<td><font color="red">*</font>触发器名称:</td><td><input class="easyui-textbox" id="triggerName" name="triggerName" type="text" data-options="required:true" style="width:200px" /></td>
				</tr>
				<!-- 
				<tr>
					<td><font color="red">*</font>触发器组名:</td><td><input class="easyui-textbox" id="triggerGroup" name="triggerGroup" type="text" data-options="required:true" style="width:200px" /></td>
				</tr>
				 -->
				<tr>
					<td><font color="red">*</font>Cron表达式:</td><td><input class="easyui-textbox" id="cronExpression" name="cronExpression" type="text" data-options="required:true" style="width:200px" /></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitNewTrigger()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-trigger-add').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
