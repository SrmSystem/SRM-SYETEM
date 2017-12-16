function managerFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editSerialNumber('+r.id+');">编辑</a>';
		}
		
		function dateFormatter(date) {
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			var d = date.getDate();
			return y + "" + (m < 10 ? ("0" + m) : m) + "" + (d < 10 ? ("0" + d) : d) + ""; 
		} 
		
		function dateParser(s){
			if (!s) return new Date();
			var y = parseInt(s.substring(0,4),10);
			var m = parseInt(s.substring(4,6),10);
			var d = parseInt(s.substring(6,8),10);
			if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
				return new Date(y,m-1,d);
			} else {
				return new Date();
			}
		}
		function searchProductLine(){
			var searchParamArray = $('#form-productLine-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-serial-list').datagrid('load',searchParams);
		}

		function addSerialNumber(){
			$('#win-serialNumber-addoredit').window({
				iconCls:'icon-add',
				title:'新增流水单号'
			});
			$('#key').textbox('enable');  
			$('#form-serialNumber-addoredit').form('clear');
			$('#id').val(0);
			$('#win-serialNumber-addoredit').window('open');
		}

		function submitAddoreditSerialNumber(){
			var url = ctx+'/manager/basedata/serial/addNewSerial';
			if($('#id').val()!=0 && $('#id').val()!='0'){
				url = ctx+'/manager/basedata/serial/update';
			}
			$.messager.progress({
				title:'提示',
				msg : '提交中...'
			});
			$('#form-serialNumber-addoredit').form('submit',{
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
						$('#win-serialNumber-addoredit').window('close');
						$('#datagrid-serial-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.message,'error');
					}
					}catch (e) {
						$.messager.alert('提示',data,'error');
					}
				}
				
			});
		}

		function deleteSerialNumber(){
			var selections = $('#datagrid-serial-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			var params = $.toJSON(selections);
			
			
			$.messager.confirm("操作提示", "执行此操作将删除流水号设置,确定要执行操作吗？", function (data) {
		        if (data) {
		        	$.messager.progress({
						title:'提示',
						msg : '提交中...'
					});
					$.ajax({
						url:ctx+'/manager/basedata/serial/deleteSerial',
						type:'POST',
						data:params,
						dataType:"json",
						contentType : 'application/json',
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
									$('#datagrid-serial-list').datagrid('reload');
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

		function editSerialNumber(id){
			$('#win-serialNumber-addoredit').window({
				iconCls:'icon-edit',
				title:'编辑流水单号'
			});
			$('#key').textbox('disable');
			$('#win-serialNumber-addoredit').window('open');
			$('#form-serialNumber-addoredit').form('load',ctx+'/manager/basedata/serial/getSerial/'+id);  
		}

		//form重置
		function resetForm() {     
			var id = $("form:eq(1) input:eq(0)").val(); 
			if(id == 0) 
				$('#form-serialNumber-addoredit').form('reset');
			else
				$('#form-serialNumber-addoredit').form('load',ctx+'/manager/basedata/serial/getSerial/'+id); 	
		}
