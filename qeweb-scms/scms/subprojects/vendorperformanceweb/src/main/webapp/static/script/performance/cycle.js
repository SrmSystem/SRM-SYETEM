var cycle = {
	search : function() {
		var searchParamArray = $('#form').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load', searchParams);
	},
	search2 : function() {
		var searchParamArray = $('#form2').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#blistssssss').datagrid('load', searchParams);
	},
	vfmt : function(v,r,i){
		return '<button class="btn-link" onclick="cycle.saveUpadte('+r.id+')">修改</button>';
	},
	saveUpadte : function(id){
		$('#dialog-cycle-saveUpadte').dialog();
		$('#form-cycle-saveUpadte').form('clear');
		if(id==0) {
			$("#id").val("0");
			$('#dialog-cycle-saveUpadte').window('open');
		} else {
			$.ajax({
				url:ctx+"/manager/vendor/performance/cycle/updateCycleStart/"+id,
				type:'POST',
				contentType : 'application/json',
				success:function(data){
					var da= data.split(",");
					$("#id").val(da[0]);
					$("#code").textbox('setValue',da[1]);
					$("#cycleName").textbox('setValue',da[2]);
					$("#initDates").numberbox('setValue',da[3]);
					$("#fixDates").numberbox('setValue',da[4]);
					$("#defaultPurchase").numberbox('setValue',da[5]);
					$("#remarks").textbox('setValue',da[6]);
					$('#dialog-cycle-saveUpadte').window('open');
				},
				error:function(data) {
					$.messager.fail(data.responseText);
				}
			});
		}
		
	},
	submit : function(){
		var url=ctx;
		if($("#id").val()=="0")
		{
			url=url+'/manager/vendor/performance/cycle/addCycle';
		}
		else
		{
			url=url+'/manager/vendor/performance/cycle/updateCycle';
		}
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});	
		$('#form-cycle-saveUpadte').form('submit',{
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
				try {
					data = JSON.parse(data);
					if(data.success) {
						$.messager.alert('提示',data.msg,'info');
						$('#dialog-cycle-saveUpadte').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示',data.msg,'info');
					}
				}catch(e) {
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	submit2 : function(){
		var url = ctx+'/manager/vendor/performance/cycle/updateCycle';
		$('#form-cycle-purchase').form('submit',{
			url:url, 
			success:function(data){
				try{
					var obj = JSON.parse(data);
					if(obj.success) {
						$.messager.alert('提示', data,'info');
						$('#dialog-cycle-saveUpadte').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示',obj.msg,'info');
					}
				}catch(e){
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	sumction : function(){
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});
		$('#form-cycle-purchase1').form('submit',{
			url:ctx+'/manager/vendor/performance/cycle/saveUpdatePCycle',
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
					var obj = JSON.parse(data);
					if(obj.success) {
						$.messager.alert('提示','提交成功','info');
						$('#dialog-cycle-purchase').window('close');
						$('#datagrid').datagrid('load');
					} else {
						$.messager.alert('提示',obj.msg,'error');
						$('#datagrid').datagrid('load');
					}
				}catch(e){
					$.messager.alert('提示',data,'error');
				}
			}
		});
	},
	purchase : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条！','info');
			return false;
		}
		$('#dialog-cycle-purchase').dialog();
		$("#mts").val("");
		$("#cid").val(selections[0]["id"]);
		$("#addupo").html("");
		var mts="";
		$.ajax({
			url:ctx+"/manager/vendor/performance/cycle/getPurchase/"+selections[0]["id"],
			type:'POST',
			data:params,
			contentType : 'application/json',
			success:function(data){
				var jsonarray =data;
				for(var i =0;i<jsonarray.length;i++){
					var jsonobj = jsonarray[i];
					var intd=parseInt(jsonobj["purchaseNumber"]);
						$("#addupo").append(
								'<tr id="p'+jsonobj["materialtypeId"]+'">'
								+'<td>'+jsonobj["materialtypeNamef"]+'</td>'
								+'<td>'+jsonobj["materialtypeName"]+'</td>'
								+'<td><input class="easyui-numberbox" value="'+intd+'" name="purchaseNumber" value="" type="text"   required="true"/>'
								+'<input type="hidden" name="materialtypeId" value="'+jsonobj["materialtypeId"]+'"/>'
								+'<input type="hidden" name="materialtypeName" value="'+jsonobj["materialtypeName"]+'"/>'
								+'<input type="hidden" name="materialtypeNamef" value="'+jsonobj["materialtypeNamef"]+'"/>'
								+'</td>'
								+'<td><a href="javascript:;" id="sc'+jsonobj["materialtypeId"]+'" class="easyui-linkbutton" onclick="cycle.deletep(\'p'+jsonobj["materialtypeId"]+'\')">删除</a>'
								+'</td>'
								+'</tr>'
						);
						mts=mts+","+jsonobj["materialtypeId"];
						$.parser.parse($('#addupo').parent());
				}
				if(jsonarray.length==0)
				{
					$("#trueid").hide();
				}
				$("#mts").val(mts);
				$('#blistssssss').datagrid('reload',ctx+'/manager/vendor/performance/cycle/getMaterialtype?mts='+mts);
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
		});
		$('#dialog-cycle-purchase').window('open');
	},
	xuanzhe1 : function(){
		var mts=$("#mts").val();
		var selections = $('#blistssssss').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		for(var i=0;i<selections.length;i++){
			$("#addupo").append(
					'<tr id="p'+selections[i]["id"]+'">'
					+'<td>'+selections[i]["faname"]+'</td>'
					+'<td>'+selections[i]["name"]+'</td>'
					+'<td><input class="easyui-numberbox"  name="purchaseNumber" type="text"  required="true" /></td>'
					+'<td><a href="javascript:;" class="easyui-linkbutton" onclick="cycle.deletep(\'p'+selections[i]["id"]+'\')">删除</a>'
					+'<input type="hidden" name="materialtypeId" value="'+selections[i]["id"]+'"/>'
					+'<input type="hidden" name="materialtypeName" value="'+selections[i]["name"]+'"/>'
					+'<input type="hidden" name="materialtypeNamef" value="'+selections[i]["faname"]+'"/>'
					+'</td>'
					+'</tr>'
			);
			mts=mts+","+selections[i]["id"];
		}
		$("#mts").val(mts);
		if(mts=='')
		{
			$("#trueid").hide();
		}
		else
		{
			$("#trueid").show();
		}
		$.parser.parse($('#daopetin').parent());
		$('#blistssssss').datagrid('reload',ctx+'/manager/vendor/performance/cycle/getMaterialtype?mts='+mts);
	},
	deletep : function(i){
		var cid=$("#cid").val();
		var s=i.replace("p", "");
		
		$.ajax({
			url:ctx+"/manager/vendor/performance/cycle/deletePurchase/"+s+"/"+cid,
			type:'POST',
			contentType : 'application/json',
			success:function(data){
				if(data==1)
				{
					var mts=$("#mts").val();
					var m=mts.split(",");
					var mt="";
					for(var j=1;j<m.length;j++)
					{
						if(m[j]!=s)
						{
							mt=mt+","+m[j];
						}
					}
					$("#mts").val(mt)
					$("#"+i).remove();
					$.parser.parse($('#daopetin').parent());
					$('#blistssssss').datagrid('reload',ctx+'/manager/vendor/performance/cycle/getMaterialtype?mts='+mt);
					if(mt=='')
					{
						$("#trueid").hide();
					}
					else
					{
						$("#trueid").show();
					}
				}
			},
			error:function(data) {
				$.messager.fail(data.responseText);
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
		$.messager.confirm('提示','确定启用吗？',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='0'){
						$.messager.alert('提示','存在已启用的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/performance/cycle/releaseCycle',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1')
						{
							$.messager.alert('提示',"启用成功",'info');
						}
						else
						{
							$.messager.alert('提示',data,'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	},
	dels : function(){
		var selections = $('#datagrid').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		var params = $.toJSON(selections);
		$.messager.confirm('提示','确定作废吗？',function(r){
			if(r){
				for(var i=0;i<selections.length;i++){
					if(selections[i]["abolished"]=='1'){
						$.messager.alert('提示','存在已作废的记录！','info');
						return false;
					}
				}
				$.ajax({
					url:ctx+'/manager/vendor/performance/cycle/delsCycle',
					type:'POST',
					data:params,
					contentType : 'application/json',
					success:function(data){
						if(data=='1') {
							$.messager.alert('提示',"作废成功",'info');
						} else {
							$.messager.alert('提示',data,'error');
						}
						$('#datagrid').datagrid('reload');
					},
					error:function(data) {
						$.messager.fail(data.responseText);
					}
				});
			}
		})
	}
}