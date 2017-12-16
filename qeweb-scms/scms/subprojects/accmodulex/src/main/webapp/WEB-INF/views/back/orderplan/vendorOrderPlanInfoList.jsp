<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.orderplan.forecastManagement"/></title>
	<META HTTP-EQUIV="pragma" CONTENT="no-cache" >
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache,must-revalidate" >
	<META HTTP-EQUIV="expires" CONTENT="0" >
	
	    <script type="text/javascript" src="${ctx}/static/script/basedata/dialog.js"></script>
	<script type="text/javascript">
		
		function initPlanDate(v,r,i){
			var dateStr = v;
			if(dateStr.length>10){
				dateStr = dateStr.substring(0,10);
			}
			return dateStr;
		}
		
	    //格式化状态(确认状态)
		function formatterCheckStatus(v,r,i) {
	    	if(r.confirmStatus == -2){
	    		return '<spring:message code="vendor.orderplan.rejectedRefused"/>';
	    	}
            if(r.confirmStatus == -1){
            	return '<spring:message code="vendor.orderplan.reject"/>';
            	/* var r = JSON.stringify(r);
            	$("#reject-ac").val(r);
            	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true"  onclick="planItemReject()" >驳回 </a>'; */
	    	}
			if(r.confirmStatus == 0){
				return '<spring:message code="vendor.orderplan.unconfirmed"/>';
			}
			if(r.confirmStatus == 1){
				return '<spring:message code="vendor.orderplan.confirmed"/>';
			}
		} 
		
	</script>   
</head>

<body style="margin:0;padding:0;" onLoad="javascript:document.yourFormName.reset()">
	
		<!-- 计划详情 -->
		<div class="easyui-panel" data-options="fit:true">
		<input id="planId"  type="hidden">
			<div id="purchaseplanListitemToolbar" style="padding:5px;">
				<div>
					<form id="form-purchaseplanitem-search" method="post" >
						<table>
						<tr>
							<td style="display:none" >
						             <spring:message code="vendor.orderplan.timePeriod"/>:<input class="easyui-datebox" name="search-EQ_startDate" data-options="showSeconds:false,editable:false" value="" style="width:150px">                                              
							</td>
							<td style="display:none">
						            <spring:message code="vendor.orderplan.to"/><input  class="easyui-datebox" name="search-EQ_endDate" data-options="showSeconds:false,editable:false" value="" style="width:150px">
							</td>
							<td >
							<spring:message code="vendor.orderplan.releasePeople"/>：<input type="text" name="search-EQ_publishUser.name" class="easyui-textbox" style="width:120px;"  />
							</td>
							<td colspan="1" >
							<spring:message code="vendor.orderplan.materialNumber"/>：<input type="text" name="search-EQ_material.code" class="easyui-textbox" style="width:120px;"  />
							</td>
							<td >
							<spring:message code="vendor.orderplan.confirmStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus"><option value="">-<spring:message code="vendor.orderplan.all"/>-</option><option value="0"><spring:message code="vendor.orderplan.unconfirmed"/></option><option value="1"><spring:message code="vendor.orderplan.confirmed"/></option><option value="-1"><spring:message code="vendor.orderplan.reject"/></option><option value="-2"><spring:message code="vendor.orderplan.refusedDismiss"/></option></select>
							</td>
							<td >
							<!-- 待办 --><input type="hidden" id="itemBacklogId" name="search-IN_backlogId" value="${backlogId}"/><!-- 待办 -->
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchasePlanItem()"><spring:message code="vendor.orderplan.enquiries"/></a> 
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$('#form-purchaseplanitem-search').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a> 
							</td>

						</tr>

						</table>
					</form>
				</div>
			</div>
			<div style=" padding: 7px;border-top: 1px solid #E5E5E5;">
				   <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="confirmItemPlan()"><spring:message code="vendor.orderplan.confirm"/></a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete'" onclick="rejectItemPlan()"><spring:message code="vendor.orderplan.reject"/></a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="exportPurchasePlan()"><spring:message code="vendor.orderplan.derivation"/></a>
			</div>
			<table id="datagrid-purchaseplanitem-list" style="height:350px" title="预测计划详情"
				data-options="method:'post',singleSelect:false,
				toolbar:'#purchaseplanitemListToolbar',
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
				
				<thead data-options="frozen:true">
				<tr>
					<th data-options="field:'id',checkbox:true"></th>
				
		            <th data-options="field:'factoryCode',formatter:function(v,r,i){return r.factory.code;}"><spring:message code="vendor.orderplan.factoryCode"/></th>
		               <th data-options="field:'groupCode',formatter:function(v,r,i){return r.group.code;}"><spring:message code="vendor.orderplan.purchasingGroup"/></th>
		            <th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="vendor.orderplan.materialNumber"/></th>
		             <th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="vendor.orderplan.materialDescription"/></th>
		         </tr>
		         </thead>
		         <thead>
		         <tr>
		         	<th data-options="field:'confirmStatus',formatter:formatterCheckStatus"><spring:message code="vendor.orderplan.confirmStatus"/></th>
		            <th data-options="field:'vetoReason'"><spring:message code="vendor.orderplan.dismissReason"/></th>
	                <th data-options="field:'factoryName',formatter:function(v,r,i){return r.factory.name;}"><spring:message code="vendor.orderplan.factoryName"/></th>
	               <th data-options="field:'groupName',formatter:function(v,r,i){return r.group.name;}"><spring:message code="vendor.orderplan.namePurchasingGroup"/></th>
	               <th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}"><spring:message code="vendor.orderplan.supplierCode"/></th>
		           <th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}"><spring:message code="vendor.orderplan.supplierName"/></th>
		            <th data-options="field:'materialUnit',formatter:function(v,r,i){return r.material.unit;}"><spring:message code="vendor.orderplan.unit"/></th>
		            <th data-options="field:'publishName',formatter:function(v,r,i){return r.publishUser.name;}"><spring:message code="vendor.orderplan.releasePeople"/></th>
		             <th data-options="field:'publishTime',formatter:function(v,r,i){return r.publishTime;}"><spring:message code="vendor.orderplan.releaseTime"/></th>
		            <th data-options="field:'createTime'"><spring:message code="vendor.orderplan.modifyTime"/></th>


		            <th id='col1'  data-options="field:'col1',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col1;}"></th>
		            <th id='col2'  data-options="field:'col2',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col2;}"></th>
		            <th id='col3'  data-options="field:'col3',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col3;}"></th>
		            <th id='col4'  data-options="field:'col4',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col4;}"></th>
		            <th id='col5'  data-options="field:'col5',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col5;}"></th>
		            <th id='col6'  data-options="field:'col6',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col6;}"></th>
		            <th id='col7'  data-options="field:'col7',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col7;}"></th>
		            <th id='col8'  data-options="field:'col8',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col8;}"></th>
		            <th id='col9'  data-options="field:'col9',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col9;}"></th>
		            <th id='col10'  data-options="field:'col10',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col10;}"></th>
		            <th id='col11'  data-options="field:'col11',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col11;}"></th>
		            <th id='col12'  data-options="field:'col12',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col12;}"></th>
		            <th id='col13'  data-options="field:'col13',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col13;}"></th>
		            <th id='col14'  data-options="field:'col14',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col14;}"></th>
		            <th id='col15'  data-options="field:'col15',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col15;}"></th>
		            <th id='col16'  data-options="field:'col16',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col16;}"></th>
		            <th id='col17'  data-options="field:'col17',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col17;}"></th>
		            <th id='col18'  data-options="field:'col18',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col18;}"></th>
		            <th id='col19'  data-options="field:'col19',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col19;}"></th>
		            <th id='col20'  data-options="field:'col20',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col20;}"></th>
		            <th id='col21'  data-options="field:'col21',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col21;}"></th>
		            <th id='col22'  data-options="field:'col22',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col22;}"></th>
		            <th id='col23'  data-options="field:'col23',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col23;}"></th>
		            <th id='col24'  data-options="field:'col24',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col24;}"></th>
		            <th id='col25'  data-options="field:'col25',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col25;}"></th>
		            <th id='col26'  data-options="field:'col26',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col26;}"></th>
		            <th id='col27'  data-options="field:'col27',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col27;}"></th>
		            <th id='col28'  data-options="field:'col28',formatter:function(v,r,i){return r.purchasePlanItemHeadVO.col28;}"></th>		            
		             <th data-options="field:'dayStock'"><spring:message code="vendor.orderplan.dayInventory"/></th>
		             <th data-options="field:'unpaidQty'"><spring:message code="vendor.orderplan.POnotDay"/></th>
		         </tr>
		         </thead>
				<input type="hidden" id="id" name="id" class="easyui-textbox" style="width:140px;"  />
				<input type="hidden" id="reject-ac" name="reject-ac" class="easyui-textbox" style="width:140px;"  />
				<input type="hidden" id="re-id" name="re-id" class="easyui-textbox" style="width:140px;"  />
			</table>
		</div>
	
<!-- 供应商驳回窗口 -->
	<div id="win-reject" class="easyui-dialog" title='驳回' style="width:400px;height:150px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb2'">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-reject" method="post" >
				<input id="reject_ids" name="reject_ids" type="hidden"/>
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="label.reson"/><!-- 原因 --><textarea rows="4" cols="50" id="reject_reason" name="reject_reason" data-options="required:true"  ></textarea></td> 
				</tr>
				</table>
				<div id="dialog-adder-bb2" style="text-align: center;">
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="rejectItemPlanOpt()"><spring:message code="vendor.orderplan.submit"/></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-reject').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>
				</div>
			</form>
		</div>
	</div>
	
	
		
	
<script type="text/javascript">
$(function() {
	//加载表头
	getPurchasePlanItemHead("${id}");
	var searchParamArray = $('#form-purchaseplanitem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplanitem-list').datagrid({url:'${ctx}/manager/order/purchaseplanvendor/vendorPlanItem/${id}',queryParams:searchParams});
});


//查询
function searchPurchasePlanItem(){
	document.getElementById("itemBacklogId").value="";//清除待办
	var  id = "${id}";
    getPurchasePlanItemHead(id);
    var searchParamArray = $('#form-purchaseplanitem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplanitem-list').datagrid({url:'${ctx}/manager/order/purchaseplanvendor/vendorPlanItem/${id}',queryParams:searchParams});
}

//查询表头
function getPurchasePlanItemHead(id){
	//获取时间选择框内容

	var startDate =$("input[name='search-EQ_startDate']").val()  ;
	var endDate = $("input[name='search-EQ_endDate']").val() ;
	if(startDate ==null || startDate == ""){
		startDate="";
	}
	if(endDate ==null || endDate == ""){
		endDate="";
	}
	
  $.ajax({  
  	url:'${ctx}/manager/order/purchaseplan/planitemHead/' + id +"?"+"startDate=" +startDate +"&"+"endDate="+endDate,
      async: false, // 注意此处需要同步，因为先绑定表头，才能绑定数据   
      type:'POST',  
      dataType:'json',  
      cache:false,
      success:function(datas){//获取表头数据成功后，使用easyUi的datagrid去生成表格   	
      	$("#col1").text(datas.col1);   
    	$("#col2").text(datas.col2);  
    	$("#col3").text(datas.col3);  
    	$("#col4").text(datas.col4);  
    	$("#col5").text(datas.col5);  
    	$("#col6").text(datas.col6);  
    	$("#col7").text(datas.col7);  
    	$("#col8").text(datas.col8);  
    	$("#col9").text(datas.col9);  
    	$("#col10").text(datas.col10);  
    	$("#col11").text(datas.col11);  
    	$("#col12").text(datas.col12);  
    	$("#col13").text(datas.col13);  
    	$("#col14").text(datas.col14);  
    	$("#col15").text(datas.col15);  
    	$("#col16").text(datas.col16);  
    	$("#col17").text(datas.col17);  
    	$("#col18").text(datas.col18);  
    	$("#col19").text(datas.col19);  
    	$("#col20").text(datas.col20);  
    	$("#col21").text(datas.col21);  
    	$("#col22").text(datas.col22);  
    	$("#col23").text(datas.col23);  
    	$("#col24").text(datas.col24);  
    	$("#col25").text(datas.col25);  
    	$("#col26").text(datas.col26);  
    	$("#col27").text(datas.col27); 
    	$("#col28").text(datas.col28); 
      }  
  });
}


//确认订单明细
function confirmItemPlan() {
	var selections = $('#datagrid-purchaseplanitem-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.noRecordSelected"/>！','info');
		return false;
	}
	for(i = 0;i < selections.length;i++) {
		 if(selections[i].confirmStatus != null && selections[i].confirmStatus == '1') {
			 $.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.containsCannotReconfirmed"/>！','error');
			return false;
		}   
    } 
	var params = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/order/purchaseplanvendor/confirmItemPlan',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.show({
				title:'<spring:message code="vendor.orderplan.news"/>',
				msg:'<spring:message code="vendor.orderplan.confirmSuccessful"/>',
				timeout:2000,
				showType:'show',
				style:{
					right:'',
					top:document.body.scrollTop+document.documentElement.scrollTop,
					bottom:''
				}
			});
			$('#datagrid-purchaseplanitem-list').datagrid('reload');
		}   
	});
}

//驳回订单明细打开
function rejectItemPlan() {
	var selections = $('#datagrid-purchaseplanitem-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.noRecordSelected"/>！','info');
		return false;
	}
	   //驳回数据的ids
 	var reject_ids="";
	for(i = 0;i < selections.length;i++) {
		reject_ids=reject_ids+selections[i].id + ",";
		 if(selections[i].confirmStatus != null && selections[i].confirmStatus == '1') {
			 $.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.recordsCannotDismissed"/>！','error');
			return false;
		}
		 if(selections[i].confirmStatus != null && selections[i].confirmStatus == '-2') {
			 $.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.orderAllowedOnce"/>！','error');
			return false;
		}
 		 if(selections[i].confirmStatus != null && selections[i].confirmStatus == '-1') {
			 $.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.orderAllowedOnce"/>！','error');
			return false;
		}   
		 if(selections[i].confirmStatus != null && selections[i].vetoStatus == 0) {
				$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.orderAllowedOnce"/>！','error');
				return false;
			} 
    } 	
	document.getElementById("reject_ids").value=reject_ids;
	$('#win-reject').window('open');   
}
function rejectItemPlanOpt(){
	var re = $("#reject_reason").val();
	if(re == "" || re == null){
		 $.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.reasonCannotEmpty"/>！','error');
		 return false;
	}
	$.messager.confirm('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.setAside"/>？<font style="color: #F00;font-weight: 900;"></font>',function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="vendor.orderplan.prompting"/>',
				msg : '<spring:message code="vendor.orderplan.submission"/>...'
			});
			$('#form-reject').form('submit',{
				onSubmit:function(){
					var isValid = $(this).form('validate');
					if(!isValid){
						$.messager.progress('close');
					}
					return isValid;
				},
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/order/purchaseplanvendor/rejectItemPlan', 
				success:function(data){
					$.messager.progress('close');
					var obj = JSON.parse(data);
					debugger;
					try{
						if(obj.success){ 
							$.messager.show({
								title:'<spring:message code="vendor.orderplan.news"/>',
								msg:  obj.message, 
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$('#win-reject').window('close');
							$('#datagrid-purchaseplanitem-list').datagrid('reload');
						}else{
							$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>',obj.message,'error');
						}
					}catch (e) {
						$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>',e,'error'); 
					} 
				}
			});
		}
	});
}

/**
 * 显示产能表页面(上传)
 */
 function showCapacityInfo(poPlanid,vendorPlanid) {
	var url = "/manager/order/purchaseplanvendor/showCapacityInfo/"+poPlanid+"/"+vendorPlanid;
	new dialog().showWin($.i18n.prop('vendor.orderplan.capacityTable'), 600, 480, ctx + url);   
}; 

/**
 * 显示产能表页面(查看)
 */
 function showCapacityList(poPlanid,vendorPlanid) {
	var isVendor = true;
	var url = "/manager/order/purchaseplanvendor/showCapacityInfoList/"+poPlanid+"/"+vendorPlanid+"/"+isVendor;
	new dialog().showWin($.i18n.prop('vendor.orderplan.viewCapacityTable'), 600, 480, ctx + url);   
}; 





//导出
function exportPurchasePlan() {
	var planid = "${id}";
	$('#form-purchaseplanitem-search').form('submit',{
		url: '${ctx}/manager/order/purchaseplanvendor/exportExcel/'+planid,
		success:function(data){
			$.messager.progress('close');
		}
	});
}
</script>
</body>
</html>
