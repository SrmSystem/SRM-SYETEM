var questionnaire = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	vfmt : function(v,r,i){
		var s="";
		if(null==r.releaseTime||r.releaseTime=="")
		{
			return '<button class="btn-link"  onclick="questionnaire.openvfSurvey(\''+r.id+'\',\''+r.title+'\',\''+r.endTime+'\')">修改</button>|<button class="btn-link"  onclick="questionnaire.looks(\''+r.id+'\',\''+r.title+'\',\''+r.endTime+'\')">查看</button>|<button class="btn-link"  onclick="questionnaire.tongji(\''+r.id+'\')">统计</button>';
		}
		return '<button  style="color:#ccc" class="btn-link" >修改</button>|<button class="btn-link"  onclick="questionnaire.looks(\''+r.id+'\',\''+r.title+'\',\''+r.endTime+'\')">查看</button>|<button class="btn-link"  onclick="questionnaire.tongji(\''+r.id+'\')">统计</button>'
	},
	vfmt1 : function(v,r,i){
		if(r.status=='-1')
		{
			return '过期';
		}
		if(r.status=='0')
		{
			return '未发布';
		}
		if(r.status=='1')
		{
			return '已发布';
		}
	},
	add : function() {
		$("#sxlzongyang").textbox({disabled:false,value:""});
		$("#addupo").html("");
		$("#endTime").datetimebox({disabled:false,value:""});
		$("#addtrr").linkbutton({disabled:false});
		$("#addtr").linkbutton({disabled:false});
		$('#dd').window('open'); 
	},
	openvfSurvey : function(id,name,endTime) {
		$("#addupo").html("<input type='hidden' name='qid' value='"+id+"'/>");
		$("#sxlzongyang").textbox({disabled:false,value:name});
		$("#endTime").datetimebox({disabled:false,value:endTime});
		$.get(ctx+"/manager/Questionnaire/updatequestionnaire/"+id,{},function(data){
			$("#addupo").append(data);
			$.parser.parse($('#addupo'));
		},"text"); 
		$("#addtrr").linkbutton({disabled:false});
		$("#addtr").linkbutton({disabled:false});
		$('#dd').window('open'); 
	},
	looks : function(id,name,endTime) {
		$("#addupo").html("<input type='hidden' name='qid' value='"+id+"'/>");
		$("#sxlzongyang").textbox({disabled:true,value:name});
		$("#endTime").datetimebox({disabled:true,value:endTime});
		$.get(ctx+"/manager/Questionnaire/looksquestionnaire/"+id,{},function(data){
			$("#addupo").append(data);
			$.parser.parse($('#addupo'));
		},"text"); 
		$("#addtrr").linkbutton({disabled:true});
		$("#addtr").linkbutton({disabled:true});
		$('#dd').window('open'); 
	},
	tongji : function(id) {
		$('#ddS').dialog({    
		    title: '问卷调查',      
		    closed: false,    
		    cache: false,    
		    href: ctx+'/manager/Questionnaire/getQuestionsSS/'+id,    
		    modal: true   
		}); 
	},
	del : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定删除吗？<br/>',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/Questionnaire/deleteQuestionnaire',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
							$.messager.show({
								title:'消息',
								msg:'删除成功',
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid').datagrid('reload');
					}
				});
			}
		});
	},
	release : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定发布吗？<br/>',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/Questionnaire/releaseQuestionnaire',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
							$.messager.show({
								title:'消息',
								msg:'发布成功',
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid').datagrid('reload');
					}
				});
			}
		});
	},
	dels : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定过期吗？<br/>',function(r){
			if(r){
				$.ajax({
					url:ctx+'/manager/Questionnaire/delsQuestionnaire',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
							$.messager.show({
								title:'消息',
								msg:'过期成功',
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#datagrid').datagrid('reload');
					}
				});
			}
		});
	}
}
function showerroer(error)
{
	$.messager.alert('错误提示',error,'error');	
}
function showinfo(info)
{
	$('#dd').window('close'); 
	$.messager.alert('提示',info,'info');	
	$('#datagrid').datagrid('reload');
}