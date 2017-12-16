/** 待办配置对象 */
var BacklogCfg = {
	/**
	 * 查询
	 */
	query : function(datagridId, formId) {
		$(datagridId).datagrid('load',$.jqexer.formIdToJson(formId));
	},
	/** 新增窗口打开 */
	add : function(dgId){
		$(dgId).dialog('autoSizeMax');
		$(dgId).dialog('setTitle','新增');
		$("#dialog-editor-form").form('reset'); 
		$('#id').val(0);
		$(dgId).dialog('open');
	},
	managerFmt : function(v,r,i) {
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="BacklogCfg.edit('+r.id+');">编辑</a>';
	},
	edit : function(id) {
		$("#dialog-editor").dialog('autoSizeMax');
		$("#dialog-editor").dialog('setTitle','编辑');
		$("#dialog-editor").dialog('open');
		$('#dialog-editor-form').form('load', ctx + '/manager/backlog/getBacklog/'+id);
	},
	del : function() {
		var selections = $('#datagrid-backlogcfg').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定删除操作？',function(r){
			if(r){
				$.ajax({
					url:'${ctx}/manager/backlog/delete',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						$.messager.show({
							title:'消息',
							msg:data.msg,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});

						$('#datagrid-backlogcfg').datagrid('reload');
					}
				});
			}
		});
	},
	/** 非菜单节点不能选择 */
	onSelectView : function(node){
		//返回树对象  
        var tree = $(this).tree;  
        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中  
        var isLeaf = tree('isLeaf', node.target);  
        if (!isLeaf) {  
            //清除选中  
            $('#combotree-viewId').combotree('clear');  
        }  
	},
	/** 保存提交 */
	addSubmit : function(formId){
		var url = ctx+'/manager/backlog/add';
		$.messager.progress();
		$(formId).form('submit',{
		    url : url,
		    success : function(data){
		    	$.messager.progress('close');
		    	var data = $.parseJSON(data);
		    	if(data.success){
		    	$('#datagrid-backlogcfg').datagrid('reload');
		    	$('#dialog-editor').dialog('close');
		    	}
		    }
		})
	}
		
}