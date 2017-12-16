/**
 * 截取时间为YYYY-MM-DD HH 格式
 */
function formatDate(date){
    var month = date.getMonth()+1;
    if( "" != date ){
        if( date.getMonth() +1 < 10 ){
            month = '0' + (date.getMonth() +1);
        }
        var day = date.getDate();
        if( date.getDate() < 10 ){
            day = '0' + date.getDate();
        }
       //返回格式化后的时间
        return date.getFullYear()+'-'+month+'-'+day+" "+date.getHours();
    }else{
        return "";
    }
}
function formatterdate(val, row) {
	if (val != null) {
		var date = new Date(val);
		return date.getFullYear() + '-' + (date.getMonth() + 1) + '-'+ date.getDate();
	}
}
//根据条件查询
  function searchEpPriceList(){
  	var searchParamArray = $('#form-epPrice-search').serializeArray();
  	var searchParams = $.jqexer.formToJson(searchParamArray);
  	$('#datagrid-epPrice-list').datagrid('load',searchParams);
  }

  //新增询价单--跳转到新增询价单页面
  function addPriceEpModule(epPriceId,titlemsg,type){
	  if(epPriceId != "0"){
		  titlemsg = "查看询价单";
		  type = "seeWin";
	  }
	  new dialog().showWin(titlemsg, clientWidth, clientHeight, ctx+'/manager/ep/epPrice/openAddEpPriceWin/'+epPriceId+'/'+type)
  }
  
  //供应商查看询价单
  function vendorEpPriceEpModule(epPriceId){
	 var titlemsg = "查看询价单";
	 var type = "seeWin";
	  new dialog().showWin(titlemsg, 800, 500, ctx+'/manager/ep/epPrice/openVendorEpPriceWin/'+epPriceId+'/'+type)
  }
  
//询价单号操作列
  
  
  function optxxFmt(v,r,i){
	  return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editEndTime('+ r.id +');">'+'修改截止时间</a>';
  }
  
  function editEndTime(epPriceId){
		$('#win-time-addoredit').dialog({
			iconCls:'icon-edit',
			title:'编辑'
		});
		$('#form-time-addoredit').form('clear');
		$('#win-time-addoredit').dialog('open');
		$('#form-time-addoredit').form('load',ctx+'/manager/ep/epPrice/getEpPrice/'+epPriceId);
  }
  function saveEditTime(){
		var url = ctx+'/manager/ep/epPrice/saveEditTime';
		var sucMeg = '操作成功！';
	
		$.messager.progress({
			title:'提示',
			msg : '提交中...'
		});
		$('#form-time-addoredit').form('submit',{
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
					$.messager.alert('提示',sucMeg,'info');
					$('#win-time-addoredit').dialog('close');
					$('#datagrid-epPrice-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
  }
  
  function epCodeFmt(v,r,i){
	  var title = "查看询价单";
	  var type = "seeWin";
	  return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="addPriceEpModule('+ r.id +');">'+v+'</a>';
  }
//供应商询价单号操作列
  function epCodeVendorFmt(v,r,i){
	  var title = "查看询价单";
	  var type = "seeWin";
	  //return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="vendorEpPriceEpModule('+ r.id +');">'+v+'</a>';
	  return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="vendorEpPriceEpModule('+ r.epPrice.id +');">'+r.epPrice.enquirePriceCode+'</a>';
  }
  
//物料列表操作列
 function optMaterialFmt(v,r,i){
	 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="removeMaterialList('+ i +');">删除</a> ';
 } 
 
 function  optCompareFmt(v,r,i){
	 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="comparePrice('+ r.id +','+ r.price.id +');">比价</a>';
 }
 
//供应商列表操作列
 function optVendorFmt(v,r,i){
	 var str ="";
	 var publishStatusVal =$("#publishVal").val();
	 if(publishStatusVal != 1 ){
	    str = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="removeVendorList('+ i +');">删除</a> ';
	 }else if(r.id !=null &&  r.id !="" && r.epPrice !=null){
		 str ='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="seeEpWholeQuoList('+ r.id +','+ r.epPrice.id +','+ r.epPrice.quoteWay +');">报价信息</a> ';
	 }
	 return str;
 } 
 
//供应商报名列表操作列
 function optApplactionFmt(v,r,i){
	 
	 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="applactionEpPrice('+r.id+')">报名</a> '+
     '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="displayApplicationEpPrice('+r.id+')">查看</a> ';
 }
 
 //供应商报名处查看
 function displayApplicationEpPrice(epPriceId){
	 var titlemsg = "查看询价单";
	  new dialog().showWin(titlemsg, 1000, 500, ctx+'/manager/ep/epPrice/displayApplicationEpPrice/'+epPriceId)
 }

//删除物料数据
 function removeMaterialList(editIndex){
	 var datagridId = '#datagrid-item-material';
	 $.messager.confirm('提示','确定要删除数据吗！',function(r){
		if(r){
			$(datagridId).datagrid('acceptChanges');
			$(datagridId).datagrid('deleteRow',editIndex);
			var rows = $(datagridId).datagrid("getRows");
			 $(datagridId).datagrid("loadData",rows);
			/*$.ajax({
				url :ctx + '/manager/ep/epPrice/deleteEpMaterial',
				data : {epMaterialId:id},
				dataType : 'json',
				type : 'POST',
				success : function(data){
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
						$(datagridId).datagrid('reload');
					}
				}
			});*/
		}
	});
 }
 
//删除供应商数据
 function removeVendorList(editIndex){
	 var datagridId = '#datagrid-item-vendor';
	 $.messager.confirm('提示','确定要删除数据吗！',function(r){
		if(r){
			$(datagridId).datagrid('acceptChanges');
			$(datagridId).datagrid('deleteRow',editIndex);
			var rows = $(datagridId).datagrid("getRows");
			 $(datagridId).datagrid("loadData", rows);
			/*
			 	$(datagridId).datagrid('cancelEdit',editIndex).datagrid('deleteRow',editIndex);
			 	$.ajax({
				url :ctx + '/manager/ep/epPrice/deleteEpVendor',
				data : {epVendorId:id},
				dataType : 'json',
				type : 'POST',
				success : function(data){
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
						$(datagridId).datagrid('reload');
					}
				}
			});*/
		}
	});
 }
 
 //保存、保存并发布询价单
 function saveEpPrice(type){
/*	 var checkDep = $("#checkDep").combobox('getValue');
	 var personId3 = $("#personId3").val();
	 var personId4 = $("#personId4").val();
	 if(checkDep==null||checkDep==''){
		 $.messager.alert('提示',"审批部门不能为空",'info');
		 return ;
	 }
	 if(checkDep==1){
		 if(personId3==null||personId4==null||personId3==''||personId4==''){
			 $.messager.alert('提示',"技术审批时，采购副总经理和技术中心必填",'info');
			 return ;
		 }
	 }*/
	 $('#datagrid-item-material').datagrid('acceptChanges');
	 $('#datagrid-item-vendor').datagrid('acceptChanges');
	 var materialData = $('#datagrid-item-material').datagrid('getData');
	 var materialdatas = JSON.stringify(materialData);
	 var rows = $('#datagrid-item-material').datagrid('getRows');
	 var vendorRows = $('#datagrid-item-vendor').datagrid('getRows');
	 var joinWayVal = $('#joinWay').combobox('getValue');
	/* var isDimVal = $('#isDim').combobox('getValue');*/
/*	 if(isDimVal != 1){*/
		 if(rows.length==0){
				$.messager.alert('提示',"物料不能为空",'info');
				return ;
			}
		/*for(var i=0;i<rows.length;i++ ){
			if(rows[i].arrivalTime==null||rows[i].arrivalTime==''){
				 $.messager.alert('提示',"期望到库日期不能为空",'info');
				 return ;
			}
		}*/
		if(vendorRows.length==0&&joinWayVal==0){
			$.messager.alert('提示',"报价方式为邀请时供应商不能为空",'info');
			return ;
		}
	/* }*/
	 var start=$('#priceStartTime').datebox('getValue');
	 var end=$('#priceEndTime').datebox('getValue');
	 if(start != "" && end != "" && end<=start){
		 $.messager.progress('close');
		 $.messager.alert('提示','有效期开始时间不能大于结束时间','info');
		 return false;
	 }
	 var vendorData = $('#datagrid-item-vendor').datagrid('getData');
	 var vendordatas = JSON.stringify(vendorData);
	 
	 var applicationDeadlineVal = $('#applicationDeadline').datetimebox('getValue');
	 var quoteEndTimeVal = $('#quoteEndTime').datetimebox('getValue');
	 
	// alert("applicationDeadlineVal=========="+applicationDeadlineVal + ";" +"quoteEndTimeVal========="+quoteEndTimeVal);
	 $.messager.progress();
	 $('#form-epPrice-add').form('submit',{
			ajax:true,
			iframe: true,   
			url:ctx+'/manager/ep/epPrice/saveEpPrice',
			onSubmit:function(param){
				param.type = type;
				param.deadlineVal = applicationDeadlineVal;
				param.quoteEndTimeVal = quoteEndTimeVal;
				param.materialdatas = materialdatas;	//询价物料
				param.vendordatas = vendordatas;		//询价供应商
				
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				data = $.evalJSON(data);
				if(data.success){
					$.messager.alert('提示',data.msg,'info');
					$('#datagrid-epPrice-list').datagrid('reload');
				}else{
					$.messager.alert('提示',data.msg,'error');
				}
				$dialog.dialog('close');
				//$('#win-item-add').window('close');
			}
		});
 }
 
 //供应商报名
 function applactionEpPrice(epPriceId){
	 var datagridId = '#datagrid-application-list';
	 
	 $.messager.confirm('提示','确定要报名吗！',function(r){
			if(r){
				$.ajax({
					url :ctx + '/manager/ep/epPrice/applicationEpPrice',
					data : {epPriceId:epPriceId},
					dataType : 'json',
					type : 'POST',
					success : function(data){
						if(data.success){
							$.messager.alert('提示',data.msg,'info');
							$(datagridId).datagrid('reload');
						}
					}
				});
			}
		});
 }

 /**
  * 供应商拒绝参与、参与
  * @param type (refuse=拒绝参与；accept=参与)
  */
 function isTakePart(type){
	var formId = '#form-item';
	var msg = "";
	if("refuse" == type){
		msg = "确定拒绝参与吗？"
	}else if("accept" == type){
		msg = "确定参与吗？"
	}
	var epVendorId = $("#epVendorId").val();
	$.messager.confirm('提示',msg,function(r){
		if(r){
			 $(formId).form('submit',{
					ajax:true,
					iframe: true,   
					url:ctx+'/manager/ep/epVendor/isTakePart',
					onSubmit:function(param){
						param.type = type;
						param.epVendorId = epVendorId;	//询价供应商id
					},
					success:function(data){
						data = $.evalJSON(data);
						if(data.success){
							$.messager.alert('提示',data.msg,'info');
							//$(formId).form("load",data);
						}else{
							$.messager.alert('提示',data.msg,'error');
						}
						//$dialog.dialog('close');
						$dialog.dialog('open');
					}
				});
			}
		});
 }
 
 /**
  * 采购商通过询价供应商查看整项报价信息
  * @param epVendorId
  * @param epPriceId
  */
 function seeEpWholeQuoList(epVendorId,epPriceId,quoteWay){
	var isVendor = 0;
	var href = ctx + '/manager/ep/epWholeQuo/openSeeWin?epPriceId='+ epPriceId+'&epVendorId='+epVendorId+'&isVendor='+isVendor;
	var title = "";
	if(quoteWay == 0){
		title= "分项报价列表";
	}else if(quoteWay == 1){
		title = "整项报价列表";
	}
	parent.myWidow(title,href);	
 }
 
 function comparePrice(epMaterialId,epPriceId){
		var href = ctx + '/manager/ep/epComparePrice/openComparePriceList?epMaterialId='+ epMaterialId+'&epPriceId='+epPriceId;
		var title = "比价";
		parent.myWidow(title,href);	
 }
 
 //add by yao.jin 2016.08.19
 //增加采购主管停止报价权限，直接填写协商价，提交到BPM审批
 function stopQuote(){
	 var datagridId  = "#datagrid-epPrice-list";
	 var selections = $(datagridId).datagrid('getSelections');
	 if(selections.length == 0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	 }
	 for(var i=0;i<selections.length;i++){
		 if(selections[i].publishStatus==0){
			 $.messager.alert('提示','包含未发布的数据,请重新选择！','info');
			 return false;
		 }
	 }
	 var params = $.toJSON(selections);
	 $.messager.confirm('提示','确定要停止报价吗？',function(r){
		if(r){
			$.ajax({
				url:ctx+'/manager/ep/epPrice/stopQuoteOpt',
				type:'POST',
				data:params,
				contentType : 'application/json',
				dataType:"json",
				success:function(data){
					if(data.success){
						$.messager.alert('提示',data.msg,'info');
					}else{
						$.messager.alert('提示',data.msg,'error');
					}
					$(datagridId).datagrid('reload');
					
				},
				error:function(data) {
					$.messager.fail(data.responseText);
				}
			});
		}
	});
 }
 //end add
 
 
 
 
 
 
 
var OrgManage = {
		//弹出选择框	roleType = 1 时表示供应商
		openWin : function() {
			var buyerId=$('#buyerId').combobox('getValue');
			
			$('#win-org-detail').window('open'); 
			$('#datagrid-org-list').datagrid({   
		    	//url: ctx + '/manager/ep/epPrice/getOrgList'
				url: ctx + '/manager/ep/epPrice/getOrgList/'+buyerId
			});
		},
		searchOrg: function() {
			var searchParamArray = $('#form-org-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-org-list').datagrid('load',searchParams);
		},
		//选择
		choice : function(datagridId) {
			datagridId = '#'+datagridId;
			var selections = $('#datagrid-org-list').datagrid('getSelections');
			var allRows = $(datagridId).datagrid('checkAll').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','未选择！','info');
				return false;
			}
			for(var i=0;i<selections.length;i++){
				var isRow=true;
				if(allRows.length!=0){
					for(var j=0;j<allRows.length;j++){
						if(selections[i].id==allRows[j].vendorId){
							isRow=false;
						}
					}
				}
				if(isRow){
					var new_row = {};
					new_row.vendorId = selections[i].id;				//供应商id
					new_row.vendorCode = selections[i].code;			//供应商编码
					new_row.vendorName = selections[i].name;			//供应商名称
					new_row.address = selections[i].address;			//地址
					new_row.legalRep = selections[i].legalPerson;		//联系人
					new_row.linkPhone = selections[i].phone;			//联系电话
					new_row.orgEmail = selections[i].email;				//email
					$(datagridId).datagrid('appendRow',new_row);
				}
			}
			$(datagridId).datagrid('uncheckAll')
			$('#win-org-detail').window('close'); 
		}
	}

var MaterialManage={
		//弹出选择框	
		openWin : function() {
			var buyerId=$('#buyerId').combobox('getValue');
			
			$('#win-material-detail').window('open'); 
			$('#datagrid-material-list').datagrid({   
		    	//url: ctx + '/manager/basedata/material'
				url: ctx + '/manager/ep/epPrice/getMaterialList/'+buyerId
			});
		},
		searchMaterial: function() {
			var searchParamArray = $('#form-material-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-material-list').datagrid('load',searchParams);
		},
		//选择
		choice : function(datagridId) {
			datagridId = '#'+datagridId;
			var selections = $('#datagrid-material-list').datagrid('getSelections');
			var allRows = $(datagridId).datagrid('checkAll').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','未选择！','info');
				return false;
			}
			for(var i=0;i<selections.length;i++){
				var isRow=true;
				if(allRows.length!=0){
					for(var j=0;j<allRows.length;j++){
						if(selections[i].id==allRows[j].materialId){
							isRow=false;
						}
					}
				}
				if(isRow){
					var new_row = {};
					new_row.materialId = selections[i].id;			//物料id
					new_row.materialCode = selections[i].code;		//物料编码
					new_row.materialName = selections[i].name;		//物料名称
					new_row.materialSpec = selections[i].specification;		//型号/规格
					new_row.materialUnit = selections[i].unit;		//计量单位
					$(datagridId).datagrid('appendRow',new_row);
				}
			}
			$(datagridId).datagrid('uncheckAll')
			$('#win-material-detail').window('close'); 
		}
}

var UserManage = {
		//弹出选择框	
		openWin : function(tempVal,roleCode) {
			$('#win-signPerson-detail').window('open'); 
			$('#tempVal').val(tempVal);
			$('#datagrid-signPerson-list').datagrid({   
		    	//url: ctx + '/manager/ep/epPrice/getUserList'
				url: ctx + '/manager/ep/epPrice/getUserListByRoleType/'+roleCode
			});
		},
		search: function() {
			var searchParamArray = $('#form-signPerson-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-signPerson-list').datagrid('load',searchParams);
		},
		//选择
		choice : function() {
			var selections = $('#datagrid-signPerson-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','未选择！','info');
				return false;
			}
			
			var tempVal = $("#tempVal").val();
			var inputId1 = '#personId'+tempVal;
			var inputId2 = '#singPerson'+tempVal;
			$(inputId1).val(selections[0].id);
			$(inputId2).textbox('setValue', selections[0].name);
			$('#win-signPerson-detail').window('close');	
		}
	}
