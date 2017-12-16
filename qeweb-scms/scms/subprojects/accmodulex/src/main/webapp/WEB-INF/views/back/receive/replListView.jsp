<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
<script>
function inspectionReportViewFmt(v,r,i){
	  if(null!=r.inspectionPath && null!=r.inspectionName){
			return '<a tyle="margin-right:10px" href="javascript:;" onclick="File.download(\''+r.inspectionPath+'\',\'\')">'+r.inspectionName+'</a>';
		}
}
</script>
<div style="padding: 5px">
	<div class="easyui-panel">
		<form id="form-receiveitem-search" method="post" enctype="multipart/form-data">
		<div style="padding: 5px;">
		<table id="datagrid-receiveitem-list" title="补货单详情" class="easyui-datagrid" 
			data-options="url:'${ctx}/manager/order/delivery/replDlvItem/${id}',method:'post',singleSelect:false,
			pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
			>
			<thead><tr>
					<th data-options="field:'deliveryCode',formatter:function(v,r,i){return r.delivery.deliveryCode}"><spring:message code="vendor.receive.ASNNumber"/><!-- ASN单号 --></th>
					<th data-options="field:'dn'">DN</th>
					<th data-options="field:'itemNo'"><spring:message code="vendor.receive.lineNumbers"/><!-- 行号 --></th>
				    <th data-options="field:'materialCode', formatter:function(v,r,i){return r.material.code}"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
					<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="vendor.nameMaterial"/><!-- 物料名称 --></th>
					<th data-options="field:'orderQty',formatter:getNumFmt"><spring:message code="vendor.receive.cargoQuantity"/><!-- 要货数量 --></th>   
					<th data-options="field:'deliveryQty',formatter:getNumFmt"><spring:message code="vendor.receive.deliveryNumber"/><!-- 发货数量 --></th>   
					<th data-options="field:'standardBoxNum',formatter:getNumFmt"><spring:message code="vendor.receive.bulkPackQuantity"/><!-- 大包装数量 --></th>
					<th data-options="field:'boxNum',formatter:getNumIntegerFmt"><spring:message code="vendor.receive.bulkPack"/><!-- 大包装总数 --></th>
					<th data-options="field:'minPackageQty',formatter:getNumFmt"><spring:message code="vendor.receive.smallPackageQuantity"/><!-- 小包装数量 --></th>  
					<th data-options="field:'minBoxNum',formatter:getNumIntegerFmt"><spring:message code="vendor.receive.totalNumberSmallPackages"/><!-- 小包装总数 --></th>
				
					<th data-options="field:'requestTime',formatter:function(v,r,i){return r.delivery.deliveyTime}"><spring:message code="vendor.receive.deliveryTime"/><!-- 发货时间 --></th> 
					<th data-options="field:'manufactureDate'"><spring:message code="vendor.receive.productionDate"/><!-- 生产日期 --></th> 
					<th data-options="field:'version'"><spring:message code="vendor.receive.version"/><!-- 版本 --></th>   
					<th data-options="field:'charg'"><spring:message code="vendor.receive.batchNumber"/><!-- 批号 --></th> 
					<th data-options="field:'vendorCharg'"><spring:message code="vendor.receive.tracesBatchNumber"/><!-- 追溯批号 --></th> 
					
					<th data-options="field:'meins'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
					<th data-options="field:'remark'"><spring:message code="vendor.remark"/><!-- 备注 --></th>    
					<th data-options="field:'inspectionReport',width:250,formatter:inspectionReportViewFmt"><spring:message code="vendor.receive.inspectionReport"/><!-- 检验报告 --></th>   
					
					<th data-options="field:'zlock',formatter:function(v,r,i){if(v=='X') return '已锁定';else return '未锁定';}"><spring:message code="vendor.receive.locked"/><!-- 锁定状态 --></th>
					<th data-options="field:'lockStatus',formatter:function(v,r,i){if(v=='1') return '已冻结';else return '未冻结';}"><spring:message code="vendor.receive.frozen"/><!-- 冻结状态 --></th>
					<th data-options="field:'loekz',formatter:function(v,r,i){if(v=='X') return '已删除';else return '未删除';}"><spring:message code="vendor.receive.removeTag"/><!-- 删除标记 --></th>
					<th data-options="field:'elikz',formatter:function(v,r,i){if(v=='X') return '已交付';else return '未交付';}"><spring:message code="vendor.receive.deliveryStatus"/><!-- 交付状态 --></th>
					<th data-options="field:'bstae',formatter:function(v,r,i){if(v=='X') return '否';else return '是';}"><spring:message code="vendor.receive.identifyDeliveryForm"/><!-- 内向交货单标识 --></th>
					<th data-options="field:'deliveryStatus',formatter:function(v,r,i){if(r.delivery.deliveryStatus==1) return '已发货';else return '未发货';}"><spring:message code="vendor.receive.deliveryStatus"/><!-- 发货状态 --></th> 
					<th data-options="field:'auditStatus',formatter:function(v,r,i){if(r.delivery.auditStatus==1) return '审核通过';else if(r.delivery.auditStatus==-1)  return '审核驳回'; else return '未审核';}"><spring:message code="vendor.receive.reviewStatus"/><!-- 审核状态 --></th>  
					<th data-options="field:'receiveStatus',formatter:function(v,r,i){if(r.delivery.receiveStatus==1) return '已收货';else return '未收货';}"><spring:message code="vendor.receive.stateGoods"/><!-- 收货状态 --></th> 
					
				</tr></thead>
		</table>
		</div>
	</form>
	</div>
 </div>