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
		data-options="url:'${ctx}/manager/ep/epCheck/getCheckNoUploadList',method:'post',singleSelect:false,
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
		<!-- <th width="100px" data-options="field:'paymentMeans'">付款方式</th> -->
		<th width="100px" data-options="field:'quoteStatus',formatter:function(v,r,i){return StatusRender.render(v,'wholeQuoteStatus',false);}">报价状态</th>
		<th width="100px" data-options="field:'negotiatedStatus',formatter:function(v,r,i){return StatusRender.render(v,'negotiatedStatus',false);}">采方议价状态</th>
		<th width="100px" data-options="field:'auditStatus',formatter:function(v,r,i){if(r.eipApprovalStatus==null||r.eipApprovalStatus=='') return '未审核'; else return StatusRender.render(r.eipApprovalStatus,'audit',false);}">审核状态</th>
				<th width="100px" data-options="field:'eipStatus',formatter:function(v,r,i){return getEipStatus(v);}">询价结果回传状态</th>
		</tr></thead>
	</table>
	<div id="epWholeQuoToolbar" style="padding:5px;height:auto">
	
		  <form id="form-lingshi-search" method="post">
	  
    <input type="hidden" id="tableDatas" name="tableDatas" />
	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="uploadQuotePrice('datagrid-epEPWholeQuo-list')">询价结果回传</a>  
						

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

function searchEpPriceListx(){
  	var searchParamArray = $('#form-epPricex-search').serializeArray();
  	var searchParams = $.jqexer.formToJson(searchParamArray);
  	$('#datagrid-epEPWholeQuo-list').datagrid('load',searchParams);
  }
  
/**
 * 回传询价结果
 */
function uploadQuotePrice(datagridId){
	$.messager.progress();
	
	datagridId = "#"+datagridId;
	$(datagridId).datagrid('acceptChanges'); //保存编辑
	
   var selections = $(datagridId).datagrid('getSelections');
   
	if(selections.length == 0){
		$.messager.progress('close');
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}


	var new_sels = new Array();
	for(var i=0;i<selections.length;i++){
/*		if(selections[i].eipStatus == 1||selections[i].eipApprovalStatus!=1){
			$.messager.progress('close');
			$.messager.alert('提示','未审核通过的或者上传成功的不能回传！','info');
			return false;
		}*/
		var new_sel = {};
	 	new_sel.id = selections[i].id;
	 	new_sels.push(new_sel);
	}
	
	
	var params = $.toJSON(new_sels);
	$("#tableDatas").val(params);
	
	$('#form-lingshi-search').form('submit',{
		ajax:true,
		iframe: true,    
		url: ctx + '/manager/ep/epWholeQuo/uploadQuotePrice',

		onSubmit:function(){
		},
		success:function(data){
			$.messager.progress('close');
			try{
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert("提示", result.msg, 'info');
					$(datagridId).datagrid('reload');
				}else{
					$.messager.alert("提示", result.msg, 'error');
				}
			}catch (e) {  
				$.messager.alert("提示", e,'error');
			}
		}
	});
}

 function getEipStatus(status) {
	if(status == 0)
		return '未回传';
	else if(status == 1)
		return '成功';
	else if(status == -1)
		return '失败';
	else 
		return '';
}


</script>

</body>
</html>
