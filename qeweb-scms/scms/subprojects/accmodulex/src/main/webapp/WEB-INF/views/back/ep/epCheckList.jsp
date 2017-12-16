<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>审核信息</title>

	<script type="text/javascript" src="${ctx}/static/script/contract/dialog.js"></script>

</head>
<body style="margin:0;padding:0;">
<div class="easyui-panel" style="overflow: auto;width: 100%;height: 100%">
	<table id="datagrid-epEPWholeQuo-list" title="" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/ep/epCheck/getCheckList',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#epWholeQuoToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,idField: 'id'">
		<thead><tr>   
		<th width="50px" data-options="field:'id',checkbox:true,hidden:false"></th>
		
		<th width="100px" data-options="field:'epMaterialCode',formatter:function(v,r,i){return r.epMaterial.materialCode}">物料编码</th>
		<th width="200px" data-options="field:'epMaterialName',formatter:function(v,r,i){return r.epMaterial.materialName}">物料名称</th>
		
		
		<th width="100px" data-options="field:'planPurchaseQty',formatter:function(v,r,i){return r.epMaterial.planPurchaseQty}">数量</th>
		<th width="100px" data-options="field:'freight',formatter:function(v,r,i){return r.epMaterial.freight}">运输费用</th>
		<th width="100px" data-options="field:'totalQuotePrice'">无税单价</th>
		<th width="100px" data-options="field:'quotePrice'">含税单价</th>
		<th width="100px" data-options="field:'negotiatedPrice'">协商单价</th>
		<th width="100px" data-options="field:'supplyCycle'">供货周期</th>
		<th width="100px" data-options="field:'taxRate'">税率</th>
		<th width="100px" data-options="field:'taxCategory'">税种</th>
		<th width="100px" data-options="field:'warrantyPeriod'">保质期</th>
		<th width="100px" data-options="field:'transportationMode'">运输方式</th>
		<th width="100px" data-options="field:'paymentMeans'">付款方式</th>
		<th width="100px" data-options="field:'quoteStatus',formatter:function(v,r,i){return StatusRender.render(v,'wholeQuoteStatus',false);}">报价状态</th>
		<th width="100px" data-options="field:'negotiatedStatus',formatter:function(v,r,i){return StatusRender.render(v,'negotiatedStatus',false);}">采方议价状态</th>
		<th width="100px" data-options="field:'auditStatus',formatter:function(v,r,i){if(r.eipApprovalStatus==null||r.eipApprovalStatus=='') return '未审核'; else return StatusRender.render(r.eipApprovalStatus,'audit',false);}">审核状态</th>
		</tr></thead>
	</table>
	<div id="epWholeQuoToolbar" style="padding:5px;height:auto">
	
		  <form id="form-lingshi-search" method="post">
	  
    <input type="hidden" id="tableDatas" name="tableDatas" />
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="displayCheckx()">提交审核</a>
						

	  </form>
	  
		    <form id="form-epPricex-search" method="post">
	  <table style="width: 90%">
		<tr>
	  <td style="width: 20%">物料编码：<input type="text" name="search-LIKE_epMaterial.materialCode" class="easyui-textbox" style="width:160px;"/></td> 
	<td style="width: 20%">物料名称：<input type="text" name="search-LIKE_epMaterial.materialName" class="easyui-textbox" style="width:160px;"/></td> 
	 
	  </tr>
	  </table>
	  	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchEpPriceListx()">查询</a>  
	  <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-epPricex-search').form('reset')">重置</a>
	  </br>
	  
	  </form>
	  

	</div> 
</div>





<script type="text/javascript">
var clientWidth = document.body.clientWidth;	
var clientHeight = document.body.clientHeight;	

function getIsa(status) {
	if(status == 0)
		return '否';
	else if(status == 1)
		return '是';
	else 
		return '';
}

function searchEpPriceListx(){
  	var searchParamArray = $('#form-epPricex-search').serializeArray();
  	var searchParams = $.jqexer.formToJson(searchParamArray);
  	$('#datagrid-epEPWholeQuo-list').datagrid('load',searchParams);
  }
  
  
function displayCheckx() {
	var selections = $('#datagrid-epEPWholeQuo-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var param='';
	for(var i=0;i<selections.length;i++){
		param=param+selections[i].id+',';
		
	}
	 $('#datagrid-epEPWholeQuo-list').datagrid('clearSelections');
	 $('#datagrid-epEPWholeQuo-list').datagrid('clearChecked');
	 
	 
		$('#form-lingshi-search').form('submit',{
			ajax:true,
			url:ctx+'/manager/ep/epCheck/submitCheck/' + param,
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
						$('#datagrid-epEPWholeQuo-list').datagrid('reload');
						$.messager.alert('提示',result.msg,'info');
						
					}else{
						$.messager.alert('提示',result.msg,'error');
					}
					}catch (e) {
						$.messager.alert('提示',data,'error');
					}
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
			
			
		});
	

}


function displayCheck() {
	var selections = $('#datagrid-epEPWholeQuo-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	var param='';
	for(var i=0;i<selections.length;i++){
		param=param+selections[i].id+',';
		
	}
	 $('#datagrid-epEPWholeQuo-list').datagrid('clearSelections');
	 $('#datagrid-epEPWholeQuo-list').datagrid('clearChecked');
	 
	 
		$('#form-lingshi-search').form('submit',{
			ajax:true,
			url:ctx+'/manager/ep/epCheck/validateCheck/' + param,
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
						var title = "提交审核";
						new dialog().showWin(title, clientWidth, clientHeight, ctx
								+ '/manager/ep/epCheck/openCheckWin/'+param,
								'dialog-ep-check');
						
					}else{
						$.messager.alert('提示',result.msg,'error');
					}
					}catch (e) {
						$.messager.alert('提示',data,'error');
					}
			},
			error:function(data) {
				$.messager.fail(data.responseText);
			}
			
			
		});
	

}

function subCheck(){
	 var checkDep = $("#checkDep").combobox('getValue');
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
	 }
	$.messager.progress();
	$('#form-vendorQuotaCheck').form('submit',{
		ajax:true,
	
		url:ctx+'/manager/ep/epCheck/subCheck',  
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success:function(data){
			data = $.evalJSON(data);
			if(data.success){
				$.messager.alert('提示',data.msg,'info');
			}else{
				$.messager.alert('提示',data.msg,'error');
			}
			$.messager.progress('close');
			
			 $('#dialog-ep-check').dialog('destroy');
			$('#datagrid-epEPWholeQuo-list').datagrid('reload');
			
			
		}
	});
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
</script>

</body>
</html>
