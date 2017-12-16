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
	function formattercapacityMain(v,r,i) {
		var a="";
		var b="";
		if(r.uploadStatus !=  1 ){
			a= '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showCapacityInfo('+ r.plan.id +','+r.id+');"><spring:message code="vendor.orderplan.upload"/> </a>';
		}
		if(r.uploadStatus != 0 ){
			b='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showCapacityList('+ r.plan.id +','+r.id+');"><spring:message code="vendor.orderplan.view"/> </a>';
		}
		
		return a+b;
	} 
		function monthFmt(v,r,i) {
			//赋值
			$("#planId").val(r.plan.id);
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showPlanDetail('+ r.plan.id +');">' + r.plan.month + '</a>';
		} 
		
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
	<table id="datagrid-purchaseplan-list" title="预测计划列表" class="easyui-datagrid" fit="true"
		data-options="method:'post',singleSelect:false,
		toolbar:'#purchaseplanListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'month',formatter:monthFmt"><spring:message code="vendor.orderplan.versionNumber"/></th>
	    <th data-options="field:'createUserName'"><spring:message code="vendor.orderplan.buyer"/></th>  
		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="vendor.orderplan.confirmStatus"/></th>
		<th data-options="field:'uploadStatus',formatter:function(v,r,i){return StatusRender.render(v,'uploadStatus',false);}"><spring:message code="vendor.orderplan.uploadStatus"/></th>
		<th data-options="field:'createTime'"><spring:message code="vendor.orderplan.creationTime"/></th>
		<th data-options="field:'uploadInfo' ,formatter:formattercapacityMain ,width:100 " ><spring:message code="vendor.orderplan.capacityTable"/></th>
		</tr></thead>
	</table>
	<div id="purchaseplanListToolbar" style="padding:5px;">
		<div>
<%-- 		<shiro:hasPermission name="purchase:plan:v:confirm">  
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="confirmPlan()">确认</a>
		</shiro:hasPermission> --%>
		</div>
		<div>
			<form id="form-purchaseplan-search" method="post">
		    <spring:message code="vendor.orderplan.versionNumber"/>：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:80px;"/>
		    <spring:message code="vendor.orderplan.buyer"/><input type="text" name="search-LIKE_createUserName" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.orderplan.confirmStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus"><option value="">-<spring:message code="vendor.orderplan.all"/>-</option><option value="0"><spring:message code="vendor.orderplan.confirmed"/></option><option value="1"><spring:message code="vendor.orderplan.confirmed"/></option><option value="2"><spring:message code="vendor.orderplan.partConfirmation"/></option></select>
			<spring:message code="vendor.orderplan.capacityUploadStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_uploadStatus"><option value="">-<spring:message code="vendor.orderplan.all"/>-</option><option value="0"><spring:message code="vendor.orderplan.notUpload"/></option><option value="1"><spring:message code="vendor.orderplan.uploaded"/></option><option value="2"><spring:message code="vendor.orderplan.partUpload"/></option></select>
			<!-- 待办 --><input type="hidden" id="backlogId" name="search-IN_backlogId" value="${backlogId}"/><!-- 待办 -->
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchasePlan()"><spring:message code="vendor.orderplan.enquiries"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-purchaseplan-search').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>
			</form>
		</div>
	</div>
	
	
<script type="text/javascript">
$(function() {
	var searchParamArray = $('#form-purchaseplan-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplan-list').datagrid({url:'${ctx}/manager/order/purchaseplanvendor',queryParams:searchParams});
    //若待办是同一个大版本，直接打开详情页面
	var vendorPlanId = "${vendorPlanId}";
    if(null!=vendorPlanId && ''!=vendorPlanId){
    	showPlanDetail(vendorPlanId);
    }
    //打开详情
});

//查询
function searchPurchasePlan(){
	document.getElementById("backlogId").value="";//清除待办
	var searchParamArray = $('#form-purchaseplan-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplan-list').datagrid('load',searchParams);
}

// 显示计划详情
function showPlanDetail(id) {
	var backlogId = "${backlogId}";
	var clientWidth = document.body.clientWidth;	
	 var clientHeight = document.body.clientHeight;	
	var title='<spring:message code="vendor.orderplan.planningDetails"/>';	
	new dialog().showWin(title, 1000, clientHeight, '${ctx}/manager/order/purchaseplanvendor/orderPlanInfoList?id='+id+'&backlogId='+backlogId);

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

</script>
</body>
</html>
