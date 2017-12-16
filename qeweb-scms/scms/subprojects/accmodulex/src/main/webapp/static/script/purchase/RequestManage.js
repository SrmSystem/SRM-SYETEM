//要货单管理
var ysp;
var RequestManage = {
		//查询要货单
		searchrequest : function() {
			var searchParamArray = $('#form-request-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-request-list').datagrid('load',searchParams);
		},
		//查看要货单详情
		requestCodeFmt : function(v,r,i){
			if(!vendor&&r.publishStatus==0){
				return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="RequestManage.showRequestDetail('+ r.id +');">修改</a>';
			}else{
				return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="RequestManage.showRequestDetail('+ r.id +');">查看</a>';
			}
		},
		showRequestDetail : function(id,type){    
			//alert(id + "   " + type);
			new dialog2().showWin("要货单详情", 900, 480, ctx + '/manager/order/goodsrequest/getRequest/' + id + "/" + vendor);
		},
		//编辑管理
		managerFmt : function(v,r,i) {
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="RequestManage.showRequestDetail('+ r.id +',0);">编辑</a>';
		},
		//编辑保存要货单信息
		editReqInfo : function() {
			RowEditor.accept('datagrid-goodsrequestitem-list');
			var rows=$('#datagrid-goodsrequestitem-list').datagrid('getRows');
			for(i=0;i<rows.length;i++){
				if(rows[i].orderQty == null || rows[i].orderQty == ''){
					$.messager.progress('close');
					$.messager.alert('提示','订购数量不能为空','error');
					return false;
				}
				if(rows[i].requestTime == null || rows[i].requestTime == ''){
					$.messager.progress('close');
					$.messager.alert('提示','要求到货时间不能为空','error');
					return false;
				}
			}
			$.messager.progress();//显示动态进度条
			var isValid = $('#form-goodsrequestitem-search').form('validate');
			if(!isValid){
				$.messager.progress('close');
				return false;
			}
			var o =$('#datagrid-goodsrequestitem-list').datagrid('getData');
			var datas = encodeURI(JSON.stringify(o)); 
			$.ajax({
			 	url: ctx + '/manager/order/goodsrequest/updateRequest', 
		        type: 'post',
		        data:  "datas=" + datas +"&" + $('#form-goodsrequestitem-search').serialize(), 
		        dataType:"json",
		        success: function (data) {
					$.messager.progress('close');
					try{
						if(data.success){ 
							$.messager.alert('提示成功', data.msg ,'info');
							//$('#win-request-detail').window('close');
							$dialog.dialog("close");
							$('#datagrid-request-list').datagrid('reload'); 
						}else{
							$.messager.alert('提示出错',data.msg,'error');
						}
					}catch (e) {
						$.messager.alert('提示失败',e,'error'); 
					} 
		       	}
		      });
		},
		//新增要货单
		addRequest : function() {
			new dialog2().showWin("要货单详情", 900, 480, ctx + '/manager/order/goodsrequest/createReq');
			$.ajax({
				url: ctx + '/manager/order/goodsrequest/getReceivers',
				type:'POST',
				dataType:'json',
				contentType : 'application/json',
				success:function(data){
					$('#receiveOrg').combobox({  
						panelHeight:'auto',
						valueField:'name',  
						textField:'name',
						data:data
					}); 
				}
			});
		},
		//新增 保存
		saveRequest : function() {
			$.messager.progress();
			var isValid = $('#form-request-addoredit').form('validate');
			if(!isValid){
				$.messager.progress('close');
				$.messager.alert('提示','数据项请填写完整','error');
				return false;
			}	
			RowEditor.accept('datagrid-item-addoredit');  
			var rows = $('#datagrid-item-addoredit').datagrid('getRows');
			if(rows.length == 0) {
				$.messager.progress('close');
				$.messager.alert('提示','明细数据项不能为空','error');
				return false;
			}
			for(i = 0;i < rows.length;i++) {
				 if(rows[i].material == null || rows[i].orderQty == null) {
					$.messager.progress('close');
					$.messager.alert('提示','明细数据项请填写完整','error');
					return false;
				} 
		    }
			var o =$('#datagrid-item-addoredit').datagrid('getData'); 
			var datas = JSON.stringify(o);   
			$.messager.progress('close');
		  	$.ajax({
			 	url: ctx + '/manager/order/goodsrequest/addRequest',  
		        type: 'post',
		        data: $('#form-request-addoredit').serialize() + "&datas=" + datas,   
		        dataType:"json",
		        success: function (data) {
					$.messager.progress('close');
					try{
						if(data.success){ 
							$.messager.alert('提示', data.message ,'info');
							$('#form-request-addoredit').form('clear');   
							//$('#win-request-addoredit').window('close'); 
							$dialog.dialog("close");
							$('#datagrid-request-list').datagrid('reload'); 
						}else{
							$.messager.alert('提示',data.msg,'error');
						}
					}catch (e) {
						$.messager.alert('提示',data,'error'); 
					} 
		       	}
		      });
		},
		importRequest: function() {
			$('#form-request-import').form('clear');   
			$('#win-request-import').window('open');  
		},
		
		//保存导入的要货单
		saveimportrequest : function() {
			$.messager.progress();
			$('#form-request-import').form('submit',{
				ajax:true,
				iframe: true,    
				url: ctx + '/manager/order/goodsrequest/filesUpload', 
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
						$.messager.alert('提示','导入要货单成功','info');
						$('#win-request-import').window('close');
						$('#datagrid-request-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
					}
					}catch (e) {  
						$.messager.alert('提示',data,'error');
					}
				}
			});
		},
		/*
		 * 发布 确认 关闭
		 */
		operateRequest : function (st) {
			var selections = $('#datagrid-request-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			for(i = 0; i < selections.length; i ++) {
				if(st == "publish" && selections[i].publishStatus == 1) {
					$.messager.alert('提示','包含已发布记录无法重复发布！','error');
					return false;
				} else if(st == "confirm" && selections[i].confirmStatus == 1) {
					$.messager.alert('提示','包含已确认记录无法重复确认！','error');
					return false;
				} else if(st == "close" && selections[i].closeStatus == 1) {
					$.messager.alert('提示','包含已确认记录无法重复确认！','error');
					return false;
				}
				if(st == "publish" && !selections[i].sender) {
					$.messager.alert('提示','包含未指定发货方的要货单，请指定发货方后再发布！','error');
					return false;
				}
			}
			var params = $.toJSON(selections);
			
			$.messager.confirm("操作提示", "确定要执行操作吗？", function (data) {
				if (data) {
					$.ajax({
						url: ctx + '/manager/order/goodsrequest/' + st + 'Request',
						type:'POST',
						data:params,
						dataType:"json",
						contentType : 'application/json',
						success:function(data){
							$.messager.progress('close');
							if(data.success){
								$.messager.success("操作成功");
							}else{
								$.messager.fail(data.msg);
							}
							$('#datagrid-request-list').datagrid('reload');
						}
					});
				} else {
					$.messager.progress('close');
				}
			});
		},
		
		//导出采购计划
		exportPurchasePlan : function() {
			$.messager.alert('提示','导出.....','info');
		}
}

var OrgManage = {
	//弹出选择框	
	openWin : function() {
		$('#win-org-detail').window('open'); 
		$('#datagrid-org-list').datagrid({   
	    	url: ctx + '/manager/admin/org',
	    	queryParams: {
	    		search_EQ_roleType : 1
	    	},
	    	onDblClickCell: function(rowIndex){
	    		$(this).datagrid('selectRow', rowIndex);
	    		var selections = $('#datagrid-org-list').datagrid('getSelections');
	    		$('#vendorId').val(selections[0].id);
	    		$("#vendorName").textbox('setValue', selections[0].name);
	    		$("#vendorCode").textbox('setValue', selections[0].code);
	    		$.ajax({
	    			url: ctx + '/manager/order/goodsrequest/getSenders/' + selections[0].id,
	    			type:'POST',
	    			dataType:'json',
	    			contentType : 'application/json',
	    			success:function(data){
	    				var a = new Object();
	    				a.id=-1;
	    				a.name="第三方仓库发货";
	    				data.push(a);
	    				$('#senderId2').combobox({  
	    					valueField:'id',  
	    					textField:'name',
	    					panelHeight:'auto',
	    					data:data
	    				}); 
	    			}
	    		});
	    		
	    		$('#win-org-detail').window('close');
	    	}
		});
	},
	searchOrg: function() {
		var searchParamArray = $('#form-org-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-org-list').datagrid('load',searchParams);
	},
	
	
	//选择
	choice : function() {
		var selections = $('#datagrid-org-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		$('#vendorId').val(selections[0].id);
		$("#vendorName").textbox('setValue', selections[0].name);
		$("#vendorCode").textbox('setValue', selections[0].code);
		
		$.ajax({
			url: ctx + '/manager/order/goodsrequest/getSenders/' + selections[0].id,
			type:'POST',
			dataType:'json',
			contentType : 'application/json',
			success:function(data){
				var a = new Object();
				a.id=-1;
				a.name="第三方仓库发货";
				data.push(a);
				$('#senderId2').combobox({  
					valueField:'id',  
					textField:'name',
					panelHeight:'auto',
					data:data
				}); 
			}
		});
		
		$('#win-org-detail').window('close'); 
	},
	
	viewChoice:function(vendorId){
		$.ajax({
			url: ctx + '/manager/order/goodsrequest/getSenders/' + vendorId,
			type:'POST',
			dataType:'json',
			contentType : 'application/json',
			success:function(data){
				var a = new Object();
				a.id=-1;
				a.name="第三方仓库发货";
				data.push(a);
				$('#senderIdView').combobox({  
					valueField:'id',  
					textField:'name',
					data:data
				}); 
			}
		});
	}
}

/*
function parseToDate(value) {
	if (value == null || value == '') {
		return undefined;
	}
	var dt;
	if (value instanceof Date) {
		dt = value;
	} else {
		if (!isNaN(value)) {
			dt = new Date(value);
		} else if (value.indexOf('/Date') > -1) {
			value = value.replace(/\/Date(−?\d+)\//, '$1');
			dt = new Date();
			dt.setTime(value);
		} else if (value.indexOf('/') > -1) {
			dt = new Date(Date.parse(value.replace(/-/g, '/')));
		} else {
			dt = new Date(value);
		}
	}
	return dt;
}

//定义日期格式的方法
function formatDatebox(value) {
	if (value == null || value == '') {
		return '';
	}
	var dt = parseToDate(value);//将那个长字符串的日期值转换成正常的JS日期格式  
	return dt.format("yyyy-MM-dd");
}
//带时间的
function formatDateBoxFull(value) {
	if (value == null || value == '') {
		return '';
	}
	var dt = parseToDate(value);
	return dt.format("yyyy-MM-dd hh:mm:ss");
}

//为Date类型拓展一个format方法，用于格式化日期  
Date.prototype.format = function(format) //author: meizz   
{
	var o = {
		"M+" : this.getMonth() + 1, //month   
		"d+" : this.getDate(), //day   
		"h+" : this.getHours(), //hour   
		"m+" : this.getMinutes(), //minute   
		"s+" : this.getSeconds(), //second   
		"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter   
		"S" : this.getMilliseconds()
	//millisecond   
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};  
	*/
	
	 