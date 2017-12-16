<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<input id="epQuoteStatus"  value="${newEpPrice.quoteStatus}" hidden="true" />

<!-- 报名查看询价单详情 -->
<div class="easyui-panel" data-options="fit:true">
	<form title="询价单详情" id="form-item" title="" method="post">
	<div id="see-epPrice-item">
		<table style="padding:10px;width: 90%;margin: auto;">
		<tr>
			<td>询价单号：${newEpPrice.enquirePriceCode}</td>
			<td>询价时间：<fmt:formatDate value="${newEpPrice.createTime}" type="both" pattern="yyyy-MM-dd"/></td>
			<td>报价截止时间：<fmt:formatDate value="${newEpPrice.quoteEndTime}" type="both" pattern="yyyy-MM-dd"/></td>
			<td>采购员：${newEpPrice.createUserName}</td>
			<td></td>
		</tr>
		<tr>
			<td>报价方式：<c:if test="${newEpPrice.quoteWay eq 0 }">分项报价</c:if> <c:if test="${newEpPrice.quoteWay eq 1 }">整体报价</c:if></td>
			<td>参与方式：<c:if test="${newEpPrice.joinWay eq 0 }">邀请</c:if> <c:if test="${newEpPrice.joinWay eq 1 }">公开</c:if></td>
			<%-- <td>报价结果公开方式：<c:if test="${newEpPrice.resultOpenWay eq 0 }">全公开</c:if> <c:if test="${newEpPrice.resultOpenWay eq 1 }">仅公开供应商</c:if>
							<c:if test="${newEpPrice.resultOpenWay eq 2 }">仅公开价格</c:if><c:if test="${newEpPrice.resultOpenWay eq 3 }">不公开</c:if></td>
			 --%>
			<td>报价类型：<c:if test="${newEpPrice.quoteType eq 0 }">新产品报价</c:if> <c:if test="${newEpPrice.quoteType eq 1 }">商改产品报价</c:if></td>
		</tr>
		<tr>
		<td>价格有效开始时间：<fmt:formatDate value="${newEpPrice.priceStartTime}" type="both" pattern="yyyy-MM-dd"/></td>
		<td>价格有效结束时间：<fmt:formatDate value="${newEpPrice.priceEndTime}" type="both" pattern="yyyy-MM-dd"/></td>
		</tr>
		<tr>
			<td>询价附件：<a href="javascript:;" onclick="File.download('${newEpPrice.quoteSpecificationUrl}','${newEpPrice.quoteSpecFileName}')">${newEpPrice.quoteSpecFileName}</a></td>
			<td>特殊报价模板：<a href="javascript:;" onclick="File.download('${newEpPrice.quoteTemplateUrl }','${newEpPrice.quoteTemplateFileName}')">${newEpPrice.quoteTemplateFileName}</a></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
		<td colspan="5">询价公告：<input class="easyui-textbox" name="remarks" value="${newEpPrice.remarks}" type="text" max="300"
					data-options="multiline:true,editable:false" style="width:90%;height: 80px;border: 0px"/></td>
		</tr>
		<tr>
			<td>发布状态：<c:if test="${newEpPrice.publishStatus eq 0 }">未发布</c:if> <c:if test="${newEpPrice.publishStatus eq 1 }">已发布</c:if></td>
			<td>报价状态：<c:if test="${newEpPrice.quoteStatus eq 0 }">未报价</c:if> <c:if test="${newEpPrice.quoteStatus eq 1 }">报价中</c:if><c:if test="${newEpPrice.quoteStatus eq 2 }">报价完成</c:if></td>
			<td>合作状态：<c:if test="${epVendor.cooperatStatus eq 0 }">未合作</c:if> <c:if test="${epVendor.cooperatStatus eq 1 }">已合作</c:if></td>
			<td>关闭状态：<c:if test="${newEpPrice.closeStatus eq 0 }">未关闭</c:if> <c:if test="${newEpPrice.closeStatus eq 1 }">已关闭</c:if></td>
			<td>报名状态：<c:if test="${epVendor.applicationStatus eq -1 }">拒绝</c:if><c:if test="${epVendor.applicationStatus eq 0 }">未报名</c:if> <c:if test="${epVendor.applicationStatus eq 1 }">已报名</c:if></td>
		</tr>
		</table>
	</div>
<div id="ep-epmaterial" style="margin-top: 20px;height: 380px">
	    <table title="询价单物料" id="datagrid-item-epmaterial" class="easyui-datagrid" data-options="url:'${ctx}/manager/ep/epPrice/getApplicationEpMaterialList/${epPriceId}',iconCls:'icon-edit',method:'post',
		fit:true,border:false,pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,onClickCell: RowEditor.onClickCell">
	        <thead><tr>
	        	<th width="150px" data-options="field:'materialCode'">物料编码</th> 
	        	<th width="150px" data-options="field:'materialName'">物料名称</th> 
	        	<th width="150px" data-options="field:'materialSpec'">规格</th> 
	        	<th width="150px" data-options="field:'materialUnit'">计量单位</th>
	        	<th width="150px" data-options="field:'offerPrice',hidden:true">报价</th> 
	        	<th width="150px" data-options="field:'checkPrice',hidden:true">核价</th>
	        	<th width="150px" data-options="field:'expectedBrand'">产地/品牌</th>
	        	<th width="150px" data-options="field:'warranty'">保修期</th>
	        	<th width="150px" data-options="field:'planPurchaseQty'">预计采购量</th> 
	        	<th width="150px" data-options="field:'arrivalTime'" width="180px">期望到库日期</th> 	        	
	        	<th width="150px" data-options="field:'remarks'" width="200px">备注</th>
	       	</tr></thead>
	   	</table>
   	</div>
   	</form> 
</div>