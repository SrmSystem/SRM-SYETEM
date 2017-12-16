function managerFmt(v,r,i){
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editTask(\''+r.key.schedName+'\', \''+r.key.jobName+'\', \''+r.key.jobGroup+'\');">编辑</a>&nbsp;'
		 + '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="operateTask(\''+r.key.schedName+'\', \''+r.key.jobName+'\', \''+r.key.jobGroup+'\',0);">立即触发</a>&nbsp;'
	 	 + '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="operateTask(\''+r.key.schedName+'\', \''+r.key.jobName+'\', \''+r.key.jobGroup+'\',1);">删除</a>';
}

function taskDetailFmt(v,r,i){	
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showTriggers(\''+r.key.schedName+'\', \''+r.key.jobName+'\', \''+r.key.jobGroup+'\');">' + (r.key.jobName) + '</a>';       
}

function triggersFmt(v,r,i){
	var s = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="operateTrigger(\''+r.key.schedName+'\', \''+r.key.triggerName+'\', \''+r.key.triggerGroup+'\', 0);">暂停</a>&nbsp;';
	if(r.triggerState == 'PAUSED') {
		s = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="operateTrigger(\''+r.key.schedName+'\', \''+r.key.triggerName+'\', \''+r.key.triggerGroup+'\', 1);">启动</a>&nbsp;';
	}
	return s + '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="operateTrigger(\''+r.key.schedName+'\', \''+r.key.triggerName+'\', \''+r.key.triggerGroup+'\', -1);">删除</a>';
}  

//查询任务
function searchTask(){
	var searchParamArray = $('#form-task-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-task-list').datagrid('load',searchParams);
}

//TO编辑任务
function editTask(schedName, jobName, jobGroup){
	$('#win-task-addoredit').window({
		iconCls:'icon-edit',
		title:'编辑任务'
	});
	$('#win-task-addoredit').window('open');
	$('#form-task-addoredit').form('load',ctx+'/manager/basedata/task/getTask/'+ schedName +'/' + jobName + '/' + jobGroup);  
}

//编辑任务
function submitAddorEditTask(){
	var	url = ctx+'/manager/basedata/task/update';
	$.messager.progress({ title:'提示', msg : '提交中...'});
	$('#form-task-addoredit').form('submit',{
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
				$.messager.alert('提示',result.message,'info');
				$('#win-task-addoredit').window('close');
				$('#datagrid-task-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		}
	});
}

//删除任务
function operateTask(schedName, jobName, jobGroup, flag){
	var url = ctx+'/manager/basedata/task/execTask';
	if(1 == flag)
		url = ctx+'/manager/basedata/task/deleteTask';
	$.messager.confirm("操作提示", "确定要执行操作吗？", function (data) {
      if (data) {
      	$.messager.progress({ title:'提示', msg : '提交中...'});
			$.ajax({
				url:url,
				type:'POST',
				data:"schedName=" + schedName + "&jobName=" + jobName + "&jobGroup=" + jobGroup,
				dataType:"json",  
				success:function(data){
					$.messager.progress('close');
					try{
						if(data.success){ 
							$.messager.show({
								title:'消息',
								msg: data.message,
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-task-list').datagrid('reload');
						}else{
							$.messager.alert('提示',data.message,'error');
						}
					}catch (e) {
						$.messager.alert('提示',e,'error'); 
					} 
				}
			});
      }
    });
}


//查询Triggers
function searchTriggers(){
	var searchParamArray = $('#form-triggers-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);  
	$('#datagrid-trigger-list').datagrid({
	    url:ctx+'/manager/basedata/jobTriggers',
	    queryParams: searchParams
	});
}

//任务详情
function showTriggers(schedName, jobName, jobGroup){
	$('#win-taskdetail-addoredit').window('open');
	$('#search-EQ_schedName').val(schedName);  
	$('#search-EQ_jobName').val(jobName);
	$('#search-EQ_jobGroup').val(jobGroup);
	searchTriggers();   
}

//新增触发器   
function addTrigger(){
	$('#win-trigger-add').window({
		iconCls:'icon-add',
		title:'新增触发器'
	});
	$('#form-trigger-add').form('clear');     
	$('#win-trigger-add').window('open');
	setInitTriggerFormValue($('#search-EQ_schedName').val(), $('#search-EQ_jobName').val(), $('#search-EQ_jobGroup').val());
}

function setInitTriggerFormValue(schedName, jobName, jobGroup) {
	$("form:eq(3) input:eq(0)").val(schedName);
	$("form:eq(3) input:eq(1)").val(jobName);
	$("form:eq(3) input:eq(2)").val(jobGroup);    
}

//保存触发器信息
function submitNewTrigger(){
	var url = ctx+'/manager/basedata/jobTriggers/addNewTrigger';
	$.messager.progress({ title:'提示', msg : '提交中...' });
	$('#form-trigger-add').form('submit',{
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
				$.messager.alert('提示',result.message,'info');
				$('#win-trigger-add').window('close');
				$('#datagrid-trigger-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.message,'error');  
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		}
	});
}

//flag: 0:暂停触发器 1：启动触发器 -1：删除触发器
function operateTrigger(schedName, triggerName, triggerGroup, flag){
	var url = "${ctx}/manager/basedata/jobTriggers/pauseTrigger";
	if(1== flag) {
		url = "${ctx}/manager/basedata/jobTriggers/resumeTrigger";
	} else if(-1== flag) {
		url = "${ctx}/manager/basedata/jobTriggers/deleteTrigger";
	}
	$.messager.confirm("操作提示", "确定要执行操作吗？", function (data) {
      if (data) {
      	$.messager.progress({title:'提示', msg : '提交中...'});
			$.ajax({
				url: url,
				type:'POST',
				data:"schedName=" + schedName + "&triggerName=" + triggerName + "&triggerGroup=" + triggerGroup,
				dataType:"json",
				success:function(data){
					$.messager.progress('close');
					try{
						if(data.success){ 
							$.messager.show({
								title:'消息',
								msg: data.message,
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid-trigger-list').datagrid('reload');
						}else{
							$.messager.alert('提示',data.message,'error');
						}
					}catch (e) {
						$.messager.alert('提示',e,'error'); 
					} 
				}
			});
      }
    });
}