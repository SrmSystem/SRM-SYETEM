<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
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
		    <script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
	<script type="text/javascript">
    //查看产能表信息主表
	function formattercapacityMain(v,r,i) {
    	var a = "";
    	if(r.uploadStatus != 0){
    		a =  '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showCapacityMain('+ r.id +');"><spring:message code="vendor.orderplan.view"/></a>';
    	}
		return a;
	} 
    //查看产能表字表
	function formattercapacity(v,r,i) {
		var a = "";
		if(r.uploadStatus != 0){
    		a = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showCapacityItem('+ r.id +');"><spring:message code="vendor.orderplan.view"/> </a>';
    	}
		return a;
	} 

	
    //格式化状态(确认状态)
	function formatOpt(v,r,i) {
    	var v= "";
    	if(r.isNew == 1 && r.publishStatus == 0){
		  v = '<a href="#" onclick="deletePlan('+r.id+','+r.publishStatus+')"><spring:message code="vendor.orderplan.deleting"/></a>';	
		 }	
    	return v;
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

	    //格式化发布状态
		function formatterPublishStatus(v,r,i) {
			if(r.publishStatus == 0){
				return '<spring:message code="vendor.orderplan.notRelease"/>';
			}
			if(r.publishStatus == 1){
				return '<spring:message code="vendor.orderplan.published"/>';
			}
		}

	    //查看子项目
 		function monthFmt(v,r,i) {
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showPlanDetail('+ r.id +');">' + v + '</a>';
		} 
 		/*		
		function vendorPlanFmt(v,r,i) {
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showVendorPlanDetail('+ r.id +');">查看</a>';
		} 
		
		function modifyFmt(v,r,i) {
			if(r.publishStatus == 1)
				return '';  
			return '<a href="javascript:;" class="easyui-linkbutton"  data-options="plain:true" onclick="showPlanDetail('+ r.id +',0);">修改</a>';
		} 
		 */
		function initPlanDate(v,r,i){
			var dateStr = v;
			if(dateStr.length>10){
				dateStr = dateStr.substring(0,10);
			}
			return dateStr;
		}
		
		var editIndex = undefined;
		function endEditing(){
			if (editIndex == undefined){return true}
			if ($('#datagrid-purchaseplanitem-list').datagrid('validateRow', editIndex)){
				$('#datagrid-purchaseplanitem-list').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} else {
				return false;
			}
		}
		function onClickCell(index, field){
			if (endEditing()){
				$('#datagrid-purchaseplanitem-list').datagrid('selectRow', index)
						.datagrid('editCell', {index:index,field:field});
				editIndex = index;
			}
		}
		function getRowIndex(target){
			var tr = $(target).closest('tr.datagrid-row');
			return parseInt(tr.attr('datagrid-row-index'));
		}
		function editrow(target){
			$('#datagrid-purchaseplanitem-list').datagrid('beginEdit', getRowIndex(target));
		}
		
		function saverow(target){
			$('#datagrid-purchaseplanitem-list').datagrid('endEdit', getRowIndex(target));
		}
		function cancelrow(target){
			$('#datagrid-purchaseplanitem-list').datagrid('cancelEdit', getRowIndex(target));
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
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="vendor.orderplan.releaseStatus"/></th>
		<th data-options="field:'uploadStatus',formatter:function(v,r,i){return StatusRender.render(v,'uploadStatus',false);}"><spring:message code="vendor.orderplan.capacityUploadStatus"/></th>
		<th data-options="field:'uploadInfo' ,formatter:formattercapacityMain   " ><spring:message code="vendor.orderplan.capacityInformation"/></th>
		<th data-options="field:'createTime'"><spring:message code="vendor.orderplan.creationTime"/></th>
		</tr></thead>
	</table>
	<div id="purchaseplanListToolbar" style="padding:5px;">
		<div>
<%-- 			<shiro:hasPermission name="purchase:plan:rel"> --%>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="publishPlan()"><spring:message code="vendor.orderplan.posted"/></a>
<%-- 			</shiro:hasPermission>
			<shiro:hasPermission name="purchase:plan:imp"> --%>
				<a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-add',plain:true"  onclick="importPlan()"><spring:message code="vendor.orderplan.importPredictionScheme"/></a>
<%-- 			</shiro:hasPermission>
			<shiro:hasPermission name="purchase:plan:imp"> --%>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteImportPlan()"><spring:message code="vendor.orderplan.deletePrediction"/></a>
<%-- 			</shiro:hasPermission> --%>
               <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="exportPlanner()"><spring:message code="vendor.orderplan.exportPlanner"/></a>
		</div>
		<div>
			<form id="form-purchaseplan-search" method="post">
			<spring:message code="vendor.orderplan.versionNumber"/>：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:80px;"/>
		               <!-- 采购组编码：<input type="text" name="search-LIKE_group.code" class="easyui-textbox" style="width:80px;"/> -->
			<spring:message code="vendor.orderplan.releaseStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus"><option value="">-<spring:message code="vendor.orderplan.all"/>-</option><option value="0" ><spring:message code="vendor.orderplan.notRelease"/></option><option value="1"><spring:message code="vendor.orderplan.published"/></option><option value="2"><spring:message code="vendor.orderplan.partRelease"/></option></select>
			<spring:message code="vendor.orderplan.capacityUploadStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_uploadStatus"><option value="">-<spring:message code="vendor.orderplan.all"/>-</option><option value="0"><spring:message code="vendor.orderplan.notUpload"/></option><option value="1"><spring:message code="vendor.orderplan.uploaded"/></option><option value="2"><spring:message code="vendor.orderplan.partUpload"/></option></select>
			<!-- 待办 --><input type="hidden" id="backlogId" name="search-IN_backlogId" value="${backlogId}"/><!-- 待办 -->
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchasePlan()"><spring:message code="vendor.orderplan.enquiries"/></a>  
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$('#form-purchaseplan-search').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>  
			</form>
		</div>
	</div>
	
	<!-- 导入计划 -->
	<div id="win-plan-import" class="easyui-window" title="导入预测计划" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-plan-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/order/purchaseplan/filesUpload"> 
				<div style="margin-bottom:20px">
					<spring:message code="vendor.orderplan.file"/>：<input type=file id="file" name="planfiles" /><br>
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveimportplan();"><spring:message code="vendor.orderplan.save"/></a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-plan-import').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>
				</div>
				</form>  
				<form id="form-insertPurchaseplan-filesDownload" style="margin-top:-50px">
				<spring:message code="vendor.orderplan.formwork"/>：<a href="javascript:;"  onclick="filesDownload('insert')"><spring:message code="vendor.orderplan.importTemplate"/>.xls</a>
			    </form>
			
		</div>
	</div>
	
	<!-- 删除预测计划 -->
	<div id="win-plan-deleteImport" class="easyui-window" title="删除计划" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-plan-deleteImport" method="post" enctype="multipart/form-data" action="${ctx}/manager/order/purchaseplan/filesUpload"> 
				<div style="margin-bottom:20px">
					<spring:message code="vendor.orderplan.file"/>：<input type=file id="file" name="planfiles" /><br>		
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveDeleteImportplan();"><spring:message code="vendor.orderplan.save"/></a> 
					<a href="javascript:;" class="easyui-linkbutton"   data-options="iconCls:'icon-download'" onclick="$('#form-plan-import').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>
				</div>
			</form>  
			
			<form id="form-deletePurchaseplan-filesDownload" style="margin-top:-50px">
				<spring:message code="vendor.orderplan.formwork"/>：<a href="javascript:;"  onclick="filesDownload('delete')"><spring:message code="vendor.orderplan.deleteTemplate"/>.xls</a>
			</form>
		</div>
	</div>
	
	<div>
		<form id="form-purchaseTotalplanitem-search">
	
	     </form>
	</div>
						

<script type="text/javascript">
$(function() {
	var searchParamArray = $('#form-purchaseplan-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplan-list').datagrid({url:'${ctx}/manager/order/purchaseplan',queryParams:searchParams});
    //若待办是同一个大版本，直接打开详情页面
	var planId = "${planId}";
    if(null!=planId && ''!=planId){
    	showPlanDetail(planId);
    }
    //打开详情
});


//查看计划详情
function showPlanDetail(id) {
	//加载表头
	var backlogId = "${backlogId}";
	var clientWidth = document.body.clientWidth;	
	var clientHeight = document.body.clientHeight;	
	var title='<spring:message code="vendor.orderplan.planningDetails"/>';	
	new dialog().showWin(title, 1000, clientHeight, '${ctx}/manager/order/purchaseplan/planitemInfo?id='+id+'&backlogId='+backlogId);
}



//查询
function searchPurchasePlan(){
	document.getElementById("backlogId").value="";//清除待办
	var searchParamArray = $('#form-purchaseplan-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplan-list').datagrid('load',searchParams);
}

//查询表头
function getPurchasePlanItemHead(id){
	//查询进来
	if(id == null){
      id =  $("#id").val();
	}
	//获取时间选择框内容

 	var startDate =$("input[name='search-EQ_startDate']").val()  ;
	var endDate = $("input[name='search-EQ_endDate']").val() ;
	if(startDate ==null && startDate == ""){
		startDate="";
	}
	if(endDate ==null && endDate == ""){
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



//发布主采购计划
function publishPlan() {

	var selections = $('#datagrid-purchaseplan-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.noRecordSelected"/>！','info');
		return false;
	}
	var rows = $('#datagrid-purchaseplan-list').datagrid('getSelections');
	for(i = 0;i < rows.length;i++) {
		 if(rows[i].publishStatus != null && rows[i].publishStatus == 1) {
			 $.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.ccPublished"/>！','error');
			return false;
		}   
    }  
	var params = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/order/purchaseplan/publishPlan',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.show({
				title:'<spring:message code="vendor.orderplan.news"/>',
				msg:'<spring:message code="vendor.orderplan.releasePlanSuccessful"/>',
				timeout:2000,
				showType:'show',
				style:{
					right:'',
					top:document.body.scrollTop+document.documentElement.scrollTop,
					bottom:''
				}
			});
			$('#datagrid-purchaseplan-list').datagrid('reload');
		}   
	});
}

//导入当月计划
function importPlan() {
	$('#form-plan-import').form('clear');   
	$('#win-plan-import').window('open');  
}

//删除预测计划
function deleteImportPlan() {
	$('#form-plan-deleteImport').form('clear');   
	$('#win-plan-deleteImport').window('open');  
}

//模板的下载
function filesDownload(type){
	if(type=="delete"){
		type="delete"
		$('#form-deletePurchaseplan-filesDownload').form('submit',{
			url:'${ctx}/manager/order/purchaseplan/filesDownload/' + type, 
			success:function(data){
				$.messager.progress('close');
			}
		});
	}else{
		type="insert"
		$('#form-insertPurchaseplan-filesDownload').form('submit',{
			url:'${ctx}/manager/order/purchaseplan/filesDownload/' + type, 
			success:function(data){
				$.messager.progress('close');
			}
		});
	}
}

function saveimportplan(){
	$.messager.progress();
	$('#form-plan-import').form('submit',{
		ajax:true,
		iframe: true,    
		url:'${ctx}/manager/order/purchaseplan/filesUpload', 
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
				$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.forecastplanSuccessful"/>','info');
				$('#win-plan-import').window('close');
				$('#datagrid-purchaseplan-list').datagrid('reload');         
			}else{
				//清空数据
					$('#form-plan-import').form('clear'); 
				$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>',result.msg + "<br>"+'<spring:message code="vendor.orderplan.importLog"/>'+"<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>"+'<spring:message code="vendor.orderplan.logFile"/>'+"</b></a>" ,'error');
			}
			}catch (e) {  
				$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>',data,'error');
			}
		}
	});
}   


function saveDeleteImportplan(){
	$.messager.progress();
	$('#form-plan-deleteImport').form('submit',{
		ajax:true,
		iframe: true,    
		url:'${ctx}/manager/order/purchaseplan/filesUploadDelete', 
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
				$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.deleteSuccessfully"/>','info');
				$('#win-plan-deleteImport').window('close');
				$('#datagrid-purchaseplan-list').datagrid('reload');         
			}else{
				//清空数据
				$('#form-plan-deleteImport').form('clear'); 
				$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>',result.msg + "<br>"+'<spring:message code="vendor.orderplan.importLog"/>'+"<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>"+'<spring:message code="vendor.orderplan.logFile"/>'+"</b></a>" ,'error');
			}
			}catch (e) {  
				$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>',data,'error');
			}
		}
	});
}



//导出
function exportPurchasePlan() {
	$('#form-purchaseplanitem-search').form('submit',{
		url: '${ctx}/manager/order/purchaseplan/exportExcel',
		success:function(data){
			$.messager.progress('close');
		}
	});
}
//查看产能表
function showCapacityMain(poPlanid) {
	
	var isVendor = false;
	var vendorPlanid = "11111";
	var url = "/manager/order/purchaseplanvendor/showCapacityInfoList/"+poPlanid+"/"+vendorPlanid+"/"+isVendor;
	new dialog().showWin($.i18n.prop('vendor.orderplan.viewCapacityTable'), 600, 480, ctx + url); 
}
//查看产能表
function showCapacityItem(itemId) {
	var url = "/manager/order/purchaseplanvendor/viewCapacityInfo/"+itemId;
	new dialog().showWin($.i18n.prop('vendor.orderplan.detailProductionTable'), 600, 480, ctx + url);  
}

//关闭窗口产能表
function closeWindow(){
	$('#win-planitem-capacity').window('close');
}




//删除采购计划
function deletePlan(id , status){

	 if(status == 1) {
		$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.recordCannotDeleted"/>！','error');
		return false;
	}   
	$.messager.confirm('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.notRecoverable"/>？',function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="vendor.orderplan.prompting"/>',
				msg : '<spring:message code="vendor.orderplan.submission"/>...'
			});
			$.ajax({
				url:ctx+'/manager/order/purchaseplan/deletePlan/'+ id,
				type:'POST',
				dataType:"json",
				success:function(data){
					$.messager.progress('close');
					$.messager.show({
						title:'<spring:message code="vendor.orderplan.news"/>',
						msg:'<spring:message code="vendor.orderplan.deleteSuccess"/>',
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
		
		
	});
}

//采购商的驳回
function vetoOrderItems(){
	//驳回
	var selections = $("#datagrid-purchaseplanitem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.noRecordSelected"/>！','info');
		return false;
	}
    //驳回数据的ids
	var veto_ids="";
	var veto_type='orderItem';
	var data="";
	for(i = 0; i < selections.length; i ++) {
		veto_ids=veto_ids+selections[i].id + ",";
		if(selections[i].isNew == 0){
			$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.recordsCannotOperated"/>！','error');
			return false;
		}
		if( selections[i].vetoStatus == 0) {
			$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.recordCannotSupplier"/>！','error');
			return false;
		} 
		if(selections[i].confirmStatus != -1) {
			$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.rejectRecord"/>！','error');
			return false;
		} 
		if(selections[i].confirmStatus == 1) {
			$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.recordsCannotDismissed"/>！','error');
			return false;
		} 

		data = selections[i];
	}
	//赋值
	debugger;
	document.getElementById("ve-code").innerHTML = data.vendor.code;
	document.getElementById("ve-name").innerHTML = data.vendor.name;
	document.getElementById("ve-reason").value  = data.rejectReason;
	
	document.getElementById("veto_ids").value=veto_ids;
	document.getElementById("veto_type").value=veto_type;

	$("#veto-label").text('<spring:message code="vendor.orderplan.refusalReject"/>：');	
	
	document.getElementById("dialog-adder-bbb1").style.display="none";//隐藏
	document.getElementById("dialog-adder-bbb2").style.display="";//显示

	$('#win-veto').window('open');   
}
//采购商的同意
function unVetoOrderItems(){
	//驳回
	var selections = $("#datagrid-purchaseplanitem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.noRecordSelected"/>！','info');
		return false;
	}
    //驳回数据的ids
	var veto_ids="";
	var veto_type='orderItem';
	var data="";
	for(i = 0; i < selections.length; i ++) {
		veto_ids=veto_ids+selections[i].id + ",";
		if(selections[i].isNew == 0){
			$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.recordsCannotOperated"/>！','error');
			return false;
		}
		if( selections[i].vetoStatus == 1) {
			$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.CrecordsCannotSupplier"/>！','error');
			return false;
		} 
		if(selections[i].confirmStatus != -1) {
			$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.rejectRecord"/>！','error');
			return false;
		} 
		if(selections[i].confirmStatus == 1) {
			$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.recordsCannotAgreed"/>！','error');
			return false;
		} 

		data = selections[i];
	}
	//赋值
	document.getElementById("ve-code").innerHTML = data.vendor.code;
	document.getElementById("ve-name").innerHTML = data.vendor.name;
	document.getElementById("ve-reason").value  = data.rejectReason;
	
	document.getElementById("veto_ids").value=veto_ids;
	document.getElementById("veto_type").value=veto_type;
	
	
	$("#veto-label").text('<spring:message code="vendor.orderplan.adjustReason"/>：');
	document.getElementById("dialog-adder-bbb1").style.display="";//隐藏
	document.getElementById("dialog-adder-bbb2").style.display="none";//显示
	
	$('#win-veto').window('open');   
}

//采购商的同意提交
function unVeto(){
	$.messager.confirm('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.determineOperation"/>？<font style="color: #F00;font-weight: 900;"></font>',function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="vendor.orderplan.prompting"/>',
				msg : '<spring:message code="vendor.orderplan.submission"/>...'
			});
			
			$('#form-veto').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/order/purchaseplan/unVetoOrderItemPlan', 
				success:function(data){
					$.messager.progress('close');
					var obj = JSON.parse(data);
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
							$('#win-veto').window('close');
							$('#datagrid-purchaseplanitem-list').datagrid('reload');
							$('#datagrid-purchaseplan-list').datagrid('reload');
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
//采购商的驳回提交
function veto(){
	
	$.messager.confirm('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.determineOperation"/>？<font style="color: #F00;font-weight: 900;"></font>',function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="vendor.orderplan.prompting"/>',
				msg : '<spring:message code="vendor.orderplan.submission"/>...'
			});
			
			$('#form-veto').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/order/purchaseplan/vetoOrderItemPlan', 
				success:function(data){
					$.messager.progress('close');
					var obj = JSON.parse(data);
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
							$('#win-veto').window('close');
							$('#datagrid-purchaseplanitem-list').datagrid('reload');
							$('#datagrid-purchaseplan-list').datagrid('reload');
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

function resetVeto (){
	$("#ve-veto").val(""); 
}

//导出计划员数据
function exportPlanner() {		
	$('#form-purchaseTotalplanitem-search').form('submit',{
		url:ctx+'/manager/order/purchaseplan/exportPlanner',
		success:function(data){
			$.messager.progress('close');
		}
	});
	
}




</script>
<style type="text/css">
.border-none .textbox {
    border:1px solid #fff !important;
}
.border-none #remarks{
 border:1px solid #fff !important;
}

</style>

</body>
</html>
