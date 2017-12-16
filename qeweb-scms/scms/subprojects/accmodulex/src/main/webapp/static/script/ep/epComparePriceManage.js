
var comparePriceManage={
		viewFmt : function(v,r,i){
				if(r.cooperationStatus==1){
					return '';
				}else{
					return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="comparePriceManage.cooperation(' + r.id +')">合作</a>';
				}
		},
		viewModuleItemFmt : function(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="comparePriceManage.openWin(' + r.id +')">分项比价</a>';
		},
		openWin : function(moduleItemId) {
			$('#win-sub-detail').dialog('open'); 
			$('#datagrid-subQuo-list').datagrid({   
		    	url: ctx + '/manager/ep/epComparePrice/getSubList/'+moduleItemId
			});
		},
		cooperation : function(wholeId){
			$.ajax({
				url:ctx+'/manager/ep/epComparePrice/cooperation/'+wholeId,
				type:'POST',
				contentType : 'application/json',
				success:function(data){
						$.messager.show({
							title:'消息',
							msg:'操作成功',
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-epEPWholeQuox-list').datagrid('reload');
						//window.location.reload();//刷新当前页面.
				},
				error:function(data) {
					$.messager.fail(data.responseText);
				}
			});
		}
}


