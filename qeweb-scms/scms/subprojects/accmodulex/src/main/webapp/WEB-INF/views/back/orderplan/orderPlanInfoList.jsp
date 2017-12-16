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
    		a =  '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showCapacityMain('+ r.id +');"> <spring:message code="vendor.orderplan.view"/></a>';
    	}
		return a;
	} 
    //查看产能表字表
	function formattercapacity(v,r,i) {
		var a = "";
		if(r.uploadStatus != 0){
    		a = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showCapacityItem('+ r.id +');"><spring:message code="vendor.orderplan.view"/></a>';
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
            	return '<spring:message code="vendor.orderplan.rejectedRefused"/>';
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
		
		
		function getNumFmt(v,r,i) {
	 		if(v=="" || v==null || v == "0.000" ){
				return "0.000";
			}
			var num=v;
		    num = num.toString().replace(/\$|\,/g,'');  
		    // 获取符号(正/负数)  
		    sign = (num == (num = Math.abs(num)));  
		    num = Math.floor(num*Math.pow(10,3)+0.50000000001);  // 把指定的小数位先转换成整数.多余的小数位四舍五入  
		    cents = num%Math.pow(10,3);              // 求出小数位数值  
		    num = Math.floor(num/Math.pow(10,3)).toString();   // 求出整数位数值  
		    cents = cents.toString();               // 把小数位转换成字符串,以便求小数位长度  
		    // 补足小数位到指定的位数  
		    while(cents.length<3)  
		      cents = "0" + cents;  
		      // 对整数部分进行千分位格式化.  
		      for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
		        num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));  

		      return (((sign)?'':'-') + num + '.' + cents);   
		   
		   
		}
		
		

	</script>   
</head>
<body style="margin:0;padding:0;" onLoad="javascript:document.yourFormName.reset()">
	<!-- 计划详情 -->
		<div class="easyui-panel" data-options="fit:true">
			<div id="purchaseplanListitemToolbar" style="padding:5px;">
				<div>
					<form id="form-purchaseplanitem-search"  method="post"  >
						<table>
						<tr>
							<td style="display:none">
						      <spring:message code="vendor.orderplan.timePeriod"/>:<input class="easyui-datebox" name="search-EQ_startDate" data-options="showSeconds:false,editable:false" value="" style="width:150px">                                              
							</td>
							<td style="display:none" >
						      <spring:message code="vendor.orderplan.to"/><input  class="easyui-datebox" name="search-EQ_endDate" data-options="showSeconds:false,editable:false" value="" style="width:150px">
							</td>
							<td >
							  <spring:message code="vendor.orderplan.versionNumber"/>：<input type="text" name="search-LIKE_versionNumber" class="easyui-textbox" style="width:140px;"  />
							</td>
							<td >
							  <spring:message code="vendor.orderplan.confirmStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus"><option value="">-<spring:message code="vendor.orderplan.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.orderplan.unconfirmed"/><!-- 未确认 --></option><option value="1"><spring:message code="vendor.orderplan.confirmed"/><!-- 已确认 --></option><option value="-1"><spring:message code="vendor.orderplan.reject"/><!-- 驳回 --></option><option value="-2"><spring:message code="vendor.orderplan.rejectedRefused"/><!-- 驳回拒绝 --></option></select>
							</td>
							<td >
							<spring:message code="vendor.orderplan.supplierCode"/>：<input type="text" name="search-EQ_vendor.code" class="easyui-textbox" style="width:140px;"  />
							</td>
<!-- 							<td >
							<spring:message code="vendor.orderplan.supplierName"/>：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:140px;"  />
							</td> -->
							<td >
							<spring:message code="vendor.orderplan.materialNumber"/>：<input type="text" name="search-EQ_material.code" class="easyui-textbox" style="width:140px;"  />
							</td>
						</tr>
						<tr>
							
							<td >
							<spring:message code="vendor.orderplan.releaseStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus"><option value="">-<spring:message code="vendor.orderplan.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.orderplan.notRelease"/><!-- 未发布 --></option><option value="1"><spring:message code="vendor.orderplan.published"/><!-- 已发布 --></option></select>
							</td>
							<td>	
							<spring:message code="vendor.orderplan.validState"/>：<select class="easyui-combobox"  id="isNew" data-options="editable:false" name="search-EQ_isNew"><option value="">-<spring:message code="vendor.orderplan.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.orderplan.lapse"/><!-- 失效 --></option><option value="1" selected ="selected"><spring:message code="vendor.orderplan.validity"/><!-- 有效 --></option></select>
							</td>
							<td>	
							<spring:message code="vendor.orderplan.factoryCode"/>：<input type="text" name="search-EQ_factory.code" class="easyui-textbox" style="width:140px;"  />
							</td>
							<td>	
						        <spring:message code="vendor.orderplan.purchasingCoding"/>：<input type="text" name="search-EQ_group.code" class="easyui-textbox" style="width:140px;"/>
						     </td>
						     <td >
						     <!-- 待办 --><input type="hidden" id="itemBacklogId" name="search-IN_backlogId" value="${backlogId}"/><!-- 待办 -->
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchasePlanItem()"><spring:message code="vendor.orderplan.enquiries"/></a> 
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="$('#form-purchaseplanitem-search').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a> 
							</td>	
						</tr>
						<tr>
			
						</tr>
						</table>
					</form>
				</div>
			</div>
			<div style=" padding: 7px;border-top: 1px solid #E5E5E5;">
				    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="publishPlanItem()"><spring:message code="vendor.orderplan.posted"/></a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="unVetoOrderItems()"><spring:message code="vendor.orderplan.agreedDismiss"/></a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete'" onclick="vetoOrderItems()"><spring:message code="vendor.orderplan.rejectedRefused"/></a>

			</div>
			<table id="datagrid-purchaseplanitem-list" style="height:350px;width:100%" title="预测计划详情    ----- 红色为历史版本 ------   绿色为生效版本  ------" 
				data-options="method:'post',singleSelect:false,
				toolbar:'#purchaseplanitemListToolbar',
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]
				"
				>
				<thead data-options="frozen:true">
				<tr>
					<th data-options="field:'id',checkbox:true"></th>
					<th data-options="field:'opt',formatter:formatOpt"><spring:message code="vendor.orderplan.operation"/></th>
		            <th data-options="field:'factoryCode',formatter:function(v,r,i){return r.factory.code;}"><spring:message code="vendor.orderplan.factoryCode"/></th>
		            <th data-options="field:'groupCode',formatter:function(v,r,i){return r.group.code;}"><spring:message code="vendor.orderplan.purchasingGroup"/></th>
		            <th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}"><spring:message code="vendor.orderplan.supplierCode"/></th>
		            <th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="vendor.orderplan.materialNumber"/></th>
		             <th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="vendor.orderplan.materialDescription"/></th>
		          </tr>
		         </thead>
		         <thead>
		         <tr>
		         	<th data-options="field:'versionNumber'"><spring:message code="vendor.orderplan.versionNumber"/></th>
		            <th data-options="field:'publishStatus',formatter:formatterPublishStatus"><spring:message code="vendor.orderplan.releaseStatus"/></th>
		            <th data-options="field:'confirmStatus',formatter:formatterCheckStatus"><spring:message code="vendor.orderplan.confirmStatus"/></th>
		            <th data-options="field:'rejectReason'"><spring:message code="vendor.orderplan.dismissReason"/></th>
		            <th data-options="field:'factoryName',formatter:function(v,r,i){return r.factory.name;}"><spring:message code="vendor.orderplan.factoryName"/></th>
		                <th data-options="field:'groupName',formatter:function(v,r,i){return r.group.name;}"><spring:message code="vendor.orderplan.namePurchasingGroup"/></th>
		                 <th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}"><spring:message code="vendor.orderplan.supplierName"/></th>

		            <th data-options="field:'materialUnit',formatter:function(v,r,i){return r.material.unit;}"><spring:message code="vendor.orderplan.unit"/></th>
		            <th data-options="field:'createTime'"><spring:message code="vendor.orderplan.modifyTime"/></th>
		             <th data-options="field:'capa',formatter:formattercapacity"><spring:message code="vendor.orderplan.capacityTable"/></th>

		            <th id='col1'  data-options="field:'col1',formatter:function(v,r,i){return  getNumFmt( r.purchasePlanItemHeadVO.col1) }"></th>
		            <th id='col2'  data-options="field:'col2',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col2) }"></th>
		            <th id='col3'  data-options="field:'col3',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col3)  }"></th>
		            <th id='col4'  data-options="field:'col4',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col4) }"></th>
		            <th id='col5'  data-options="field:'col5',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col5) } "></th>
		            <th id='col6'  data-options="field:'col6',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col6)    }"></th>
		            <th id='col7'  data-options="field:'col7',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col7)  } "></th>
		            <th id='col8'  data-options="field:'col8',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col8) }"></th>
		            <th id='col9'  data-options="field:'col9',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col9)  }"></th>
		            <th id='col10'  data-options="field:'col10',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col10)  }"></th>
		            <th id='col11'  data-options="field:'col11',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col11)  }"></th>
		            <th id='col12'  data-options="field:'col12',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col12)  }"></th>
		            <th id='col13'  data-options="field:'col13',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col13)  }"></th>
		            <th id='col14'  data-options="field:'col14',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col14) }"></th>
		            <th id='col15'  data-options="field:'col15',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col15)  } "></th>
		            <th id='col16'  data-options="field:'col16',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col16)  }"></th>
		            <th id='col17'  data-options="field:'col17',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col17)  }"></th>
		            <th id='col18'  data-options="field:'col18',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col18)  }"></th>
		            <th id='col19'  data-options="field:'col19',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col19)  }"></th>
		            <th id='col20'  data-options="field:'col20',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col20)}"></th>
		            <th id='col21'  data-options="field:'col21',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col21)  }"></th>
		            <th id='col22'  data-options="field:'col22',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col22)  }"></th>
		            <th id='col23'  data-options="field:'col23',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col23)   }"></th>
		            <th id='col24'  data-options="field:'col24',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col24)   }"></th>
		            <th id='col25'  data-options="field:'col25',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col25)  }"></th>
		            <th id='col26'  data-options="field:'col26',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col26)  }"></th>
		            <th id='col27'  data-options="field:'col27',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col27)  }"></th>
		            <th id='col28'  data-options="field:'col28',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col28) }"></th>		            
		            <th data-options="field:'dayStock'"><spring:message code="vendor.orderplan.dayInventory"/></th>
		             <th data-options="field:'unpaidQty'"><spring:message code="vendor.orderplan.POnotDay"/></th>
		         </tr>
		         </thead>
				<input type="hidden" id="id" name="id" class="easyui-textbox" style="width:140px;"  />
				<input type="hidden" id="reject-ac" name="reject-ac" class="easyui-textbox" style="width:140px;"  />
				<input type="hidden" id="re-id" name="re-id" class="easyui-textbox" style="width:140px;"  />
			</table>
		</div>
	


	<!-- 驳回信息-->
	<div id="win-planitem-reject" class="easyui-dialog" title="供应商驳回确认" style="width:600px;height:350px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div id="" style="padding:5px;">
		
			<form id="form-purchaseplanitem-reject"	>
				<table>
					<tr class="border-none">
						<td>
						<label class="common-label"><spring:message code="vendor.orderplan.supplierCode"/>：</label><label  id="re-code"></label>
						</td>
						<td>
						<label class="common-label"><spring:message code="vendor.orderplan.supplierName"/>>：</label><label   id="re-name"></label>
						</td>
					</tr>

					<tr id="reject-detail" style="display: black;" class="border-none">
						<td colspan="2">
						<label class="common-label" style="float: left;"><spring:message code="vendor.orderplan.dismissalDescription"/>:</label>
						<textarea id="re-reason" name="rejectReason" class="easyui-validatebox" style="width: 400px; height: 60px;" readonly></textarea>
						</td>
					</tr> 
				 </table>
				 <div id="confirm-button"  style="text-align: center;padding:5px;display:block;">
				    <a href="javascript:;" class="easyui-linkbutton" onclick="rejectPlanItem('ok')"><spring:message code="vendor.orderplan.consent"/></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="rejectPlanItem('no')"><spring:message code="vendor.orderplan.reject"/></a>
				</div>
			</form>
		</div>
	</div>

<!-- 采购商确认驳回窗口 -->
	<div id="win-veto" class="easyui-dialog" title="供应商驳回确认" style="width:600px;height:350px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div id="" style="padding:5px;">
			<form id="form-veto" method="post" 	>
			  <input id="veto_type" name="veto_type" type="hidden"/>
				<input id="veto_ids" name="veto_ids" type="hidden"/>
				<table>
					<tr class="border-none">
						<td>
						<label class="common-label"><spring:message code="vendor.orderplan.supplierCode"/>：</label><label  id="ve-code"></label>
						</td>
						<td>
						<label class="common-label"><spring:message code="vendor.orderplan.supplierName"/>：</label><label   id="ve-name"></label>
						</td>
					</tr>

					<tr id="veto-detail" style="display: black;" class="border-none">
						<td colspan="2">
						<label class="common-label" style="float: left;"><spring:message code="vendor.orderplan.dismissalDescription"/>:</label>
						<textarea id="ve-reason" name="rejectReason" class="easyui-validatebox" style="width: 400px; height: 60px;" readonly></textarea>
						</td>
					</tr> 
					
					<tr id="veto-detail1" style="display: black;" class="border-none">
						<td colspan="2">
						<label  id = "veto-label"  class="common-label" style="float: left;"></label>
						<textarea id="ve-veto" name="vetoReason" class="easyui-validatebox"    style="width:400px; height: 60px;"   ></textarea>
						</td>
					</tr> 
				 </table>
				<div id="dialog-adder-bbb1" style="text-align: center; display : none"  >
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="unVeto()"><spring:message code="vendor.orderplan.submit"/></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="resetVeto()"><spring:message code="vendor.orderplan.resetting"/></a>
				</div>
				<div id="dialog-adder-bbb2" style="text-align: center;display : none">
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="veto()"><spring:message code="button.submit"/><spring:message code="vendor.orderplan.submit"/></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="resetVeto()"><spring:message code="button.reset"/><spring:message code="vendor.orderplan.resetting"/></a>
				</div>
			</form>
		</div>
	</div>





<script type="text/javascript">
$(function() {
	//加载表头
	getPurchasePlanItemHead("${id}");
	
	//显示计划详情
 	var searchParamArray = $('#form-purchaseplanitem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplanitem-list').datagrid({   
    	url:'${ctx}/manager/order/purchaseplan/planitem/${id}',
    	queryParams:searchParams,
    	rowStyler:function(index,row){
			if (row.isNew == 1){
			  color = 'background-color:;color:green;font-weight:bold;';
            }else{
          	  color = 'background-color:;color:red;font-weight:bold;';
            }
			 return color;
		}
	});
});


function formatOper(val,row,index){  
    return '<a href="#" onclick="editUser('+index+')"><spring:message code="vendor.orderplan.modification"/></a>';  
} 


//查询
function searchPurchasePlanItem(){
	document.getElementById("itemBacklogId").value="";//清除待办
	var  id =  "${id}";
    getPurchasePlanItemHead(null);
  //显示计划详情
 	var searchParamArray = $('#form-purchaseplanitem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseplanitem-list').datagrid({   
    	url:'${ctx}/manager/order/purchaseplan/planitem/${id}',
    	queryParams:searchParams,
    	rowStyler:function(index,row){
			if (row.isNew == 1){
			  color = 'background-color:;color:green;font-weight:bold;';
            }else{
          	  color = 'background-color:;color:red;font-weight:bold;';
            }
			 return color;
		}
	});
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



//发布采购计划明细
function publishPlanItem() {
	var selections = $('#datagrid-purchaseplanitem-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert(<spring:message code="vendor.orderplan.prompting"/>,'<spring:message code="vendor.orderplan.noRecordSelected"/>！','info');
		return false;
	}
	var rows = $('#datagrid-purchaseplanitem-list').datagrid('getSelections');
	for(i = 0;i < rows.length;i++) {
		 if(rows[i].publishStatus != null && rows[i].publishStatus == 1) {
			 $.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.ccPublished"/>！','error');
			return false;
		}   
		if(rows[i].isNew != null && rows[i].isNew == 0){
			 $.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.containsNotPublished"/>！','error');
				return false;
		 }
    } 
	var params = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/order/purchaseplan/publishPlanItem',
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
			$('#datagrid-purchaseplanitem-list').datagrid('reload');
		}   
	});
}



//修改采购计划
function savePurchasePlan() {
	$.messager.progress();
	if (endEditing()){
		$('#datagrid-purchaseplanitem-list').datagrid('acceptChanges');    
	}  
 	var rows = $('#datagrid-purchaseplanitem-list').datagrid('getRows');
	for(i = 0;i < rows.length;i++) {
		 if(rows[i].planQty == null || rows[i].planQty == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.numberCannotEmpty"/>','error');
			return false;
		}   
    } 
	var o =$('#datagrid-purchaseplanitem-list').datagrid('getData'); 
	var datas = encodeURI(JSON.stringify(o)); 
  	$.ajax({
	 	url:'${ctx}/manager/order/purchaseplan/updatePlan', 
        type: 'post',
        data:  "datas=" + datas +"&" + $('#form-purchaseplanitem-search').serialize(), 
        dataType:"json",
        success: function (data) {
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>', data.message ,'info');
					$('#win-planitem-addoredit').window('close');
					$('#datagrid-purchaseplan-list').datagrid('reload'); 
				}else{
					$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>',data.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>',e,'error'); 
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
				msg : '<spring:message code="vendor.orderplan.submission"/>'
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
				msg : '<spring:message code="vendor.orderplan.submission"/>'
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
