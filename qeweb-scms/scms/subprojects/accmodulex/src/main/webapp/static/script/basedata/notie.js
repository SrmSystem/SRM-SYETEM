var notieJS = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	getlookNumber : function(v,r,i){
		return '<button class="btn-link" onclick="lookUser3('+r.id+')">'+r.lookNumber+'</button>';
	},
	getspkeaNumber : function(v,r,i){
		return '<button class="btn-link" onclick="lookUser4('+r.id+')">'+r.spkeaNumber+'</button>';
	},
	updatess : function(v,r,i){
		return '<button class="btn-link" onclick="notieJS.updateapp('+r.id+')">修改</button>';
	},
	openvfSurvey : function(id){
		
	},
	add : function() {// org的ID
		$('#dd').window('open'); 
		qingqiu();
	},
	updateapp : function(id) {// org的ID
		$('#udd').window('open'); 
		qingqiuu(id);
	},
	del : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm("保存并发布", "确定执行此操作吗？", function (r) {
			if(r){
				$.ajax({
					url:ctx+'/manager/vendor/notice/deleteNotice',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
							$.messager.show({
								title:'消息',
								msg:'删除公告成功',
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
		})
	}
}
function qingqiu ()
{
	$("#addnotiess").remove();
	$.post(ctx+"/manager/vendor/notice/addnoties",{"ctx":ctx},function(data){
		$("#dd").html(data);
	},"text");	
}
function qingqiuu (id)
{
	$("#addnotiess").remove();
	$.post(ctx+"/manager/vendor/notice/updateNoties",{"ctx":ctx,"id":id},function(data){
		$("#udd").html(data);
	},"text");	
}