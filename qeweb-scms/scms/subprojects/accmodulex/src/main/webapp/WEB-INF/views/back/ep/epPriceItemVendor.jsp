<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<input id="epQuoteWay"  value="${newEpPrice.quoteWay}" hidden="true" />

<!-- 供应商询价单详情 -->
<div class="easyui-panel" data-options="fit:true">
	<form title="询价单详情" id="form-item" title="" method="post">
		<input id="priceId" name="id" value="${epPriceId}" hidden="true" />
		<input id="epVendorId" name="epVendorId" value="${epVendor.id}" hidden="true" />
	<div id="see-epPrice-item">
		<div>
		<c:if test="${newEpPrice.publishStatus eq 1 && epVendor.applicationStatus eq 0 && newEpPrice.applicationStatus eq 1 && newEpPrice.quoteStatus eq 1 }">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="isTakePart('refuse')">拒绝参与</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="isTakePart('accept')">参与</a>
		</c:if>
		</div>
		<table style="padding:10px;width: 90%;margin: auto;">
		<tr>
			<td>询价单号：${newEpPrice.enquirePriceCode}</td>
			<td>询价时间：<fmt:formatDate value="${newEpPrice.createTime}" type="both" pattern="yyyy-MM-dd"/></td>
			<td>报价截止时间：<fmt:formatDate value="${newEpPrice.quoteEndTime}" type="both" pattern="yyyy-MM-dd"/></td>
			
			<%-- <td>询价物料类型：<c:if test="${newEpPrice.materialType eq 0 }">无料号</c:if> <c:if test="${newEpPrice.materialType eq 1 }">有料号</c:if></td> --%>
		</tr>
		<tr>
			<td>采购员：${newEpPrice.createUserName}</td>
			<td>报价方式：<c:if test="${newEpPrice.quoteWay eq 0 }">分项报价</c:if> <c:if test="${newEpPrice.quoteWay eq 1 }">整体报价</c:if></td>
			<td>参与方式：<c:if test="${newEpPrice.joinWay eq 0 }">邀请</c:if> <c:if test="${newEpPrice.joinWay eq 1 }">公开</c:if></td>
			<%-- <td>报价结果公开方式：<c:if test="${newEpPrice.resultOpenWay eq 0 }">全公开</c:if> <c:if test="${newEpPrice.resultOpenWay eq 1 }">仅公开供应商</c:if>
							<c:if test="${newEpPrice.resultOpenWay eq 2 }">仅公开价格</c:if><c:if test="${newEpPrice.resultOpenWay eq 3 }">不公开</c:if></td>
			 --%>
			
			<%-- <td>报价级别：<c:if test="${newEpPrice.isTop eq 1}">一级</c:if> <c:if test="${newEpPrice.isTop eq 2 }">二级</c:if></td>
			<td>分类名称定义方：<c:if test="${newEpPrice.isVendor eq 0 }">采购商</c:if> <c:if test="${newEpPrice.isVendor eq 1 }">供应商</c:if></td> --%>
		</tr>
		<tr>
		<td>报价类型：<c:if test="${newEpPrice.quoteType eq 0 }">新产品报价</c:if> <c:if test="${newEpPrice.quoteType eq 1 }">商改产品报价</c:if></td>
		<%-- <td>工厂：<c:if test="${newEpPrice.factory eq '2270' }">北京</c:if><c:if test="${newEpPrice.factory eq '2420' }">南海</c:if></td> --%>
		<td>价格有效开始时间：<fmt:formatDate value="${newEpPrice.priceStartTime}" type="both" pattern="yyyy-MM-dd"/></td>
		<td>价格有效结束时间：<fmt:formatDate value="${newEpPrice.priceEndTime}" type="both" pattern="yyyy-MM-dd"/></td>
		<%-- <c:if test="${newEpPrice.joinWay eq 0 }">
		<td>是否按供应商维度询价:<c:if test="${newEpPrice.isDim eq '0' }">否</c:if><c:if test="${newEpPrice.isDim eq '1' }">是</c:if></td>
		</c:if> --%>
		</tr>
		<tr>
			<td>询价附件：<a href="javascript:;" onclick="File.download('${newEpPrice.quoteSpecificationUrl}','${newEpPrice.quoteSpecFileName}')">${newEpPrice.quoteSpecFileName}</a></td>
			<td>特殊报价模板：<a href="javascript:;" onclick="File.download('${newEpPrice.quoteTemplateUrl }','${newEpPrice.quoteTemplateFileName}')">${newEpPrice.quoteTemplateFileName}</a></td>
		</tr>
		<tr>
		<td colspan="5">询价公告：<input class="easyui-textbox" name="remarks" value="${newEpPrice.remarks}" type="text" max="300"
					data-options="multiline:true,editable:false" style="width:90%;height: 80px;border: 0px"/></td>
		</tr>
		<tr>
			<td>发布状态：<c:if test="${newEpPrice.publishStatus eq 0 }">未发布</c:if> <c:if test="${newEpPrice.publishStatus eq 1 }">已发布</c:if></td>
			<td>报价状态：<c:if test="${newEpPrice.quoteStatus eq 0 }">未报价</c:if> <c:if test="${newEpPrice.quoteStatus eq 1 }">报价中</c:if><c:if test="${newEpPrice.quoteStatus eq 2 }">报价完成</c:if></td>
			<td>合作状态：<c:if test="${epVendor.cooperatStatus eq 0 }">未合作</c:if> <c:if test="${epVendor.cooperatStatus eq 1 }">已合作</c:if></td>
			
		</tr>
		<tr>
		<td>关闭状态：<c:if test="${newEpPrice.closeStatus eq 0 }">未关闭</c:if> <c:if test="${newEpPrice.closeStatus eq 1 }">已关闭</c:if></td>
		<td>报名状态：<c:if test="${epVendor.applicationStatus eq -1 }">拒绝</c:if><c:if test="${epVendor.applicationStatus eq 0 }">未报名</c:if> <c:if test="${epVendor.applicationStatus eq 1 }">已报名</c:if></td>
		
		</tr>
		</table>
	</div>
<div id="ep-epmaterial" style="margin-top: 20px;height: 380px">
	    <%-- <table title="询价单物料" id="datagrid-item-epmaterial" class="easyui-datagrid" data-options="url:'${ctx}/manager/ep/epPrice/getEpMaterialList/${epPriceId}',iconCls:'icon-edit',method:'post',toolbar:'#epWholeQuoBar',
	    	singleSelect:false,border:false,rownumbers:true,onClickCell: RowEditor.onClickCell"> --%>
	    	
	    <table title="询价单物料" id="datagrid-item-epmaterial" class="easyui-datagrid" data-options="url:'${ctx}/manager/ep/epPrice/getEpMaterialList/${epPriceId}',iconCls:'icon-edit',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#epWholeQuoBar',pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,onClickCell: RowEditor.onClickCell">
	        <thead><tr>
	        	<th width="50px" data-options="field:'id',checkbox:true"></th>
	        	<c:if test="${epVendor.applicationStatus eq 1}">
	        	<th width="130px" data-options="field:'opt',formatter:optMaterialVendorFmt">操作</th>
	        	</c:if>
	        	<c:if test="${epVendor.applicationStatus eq -1}">
	        	<th width="130px" data-options="field:'opt'">操作</th>
	        	</c:if>
	        	<th width="150px" data-options="field:'materialId',hidden:true">物料id</th>
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
	   	<div id="epWholeQuoBar" style="padding:5px;height:auto;display: none">
	   	<c:if test="${epVendor.applicationStatus eq 1 && epVendor.epPrice.quoteWay eq 1 }">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="openMassWin()">批量报价</a>
	   </c:if>
	   	</div>
	   	
   	</div>
   	
   	<%-- <div id="whole_quo_his"  style="margin-top: 20px;display: none" >	
		 <table title="报价历史" id="datagrid-whole_quo_his" class="easyui-datagrid" data-options="url:'${ctx}/manager/ep/epWholeQuoHis/getEpWholeQuoHis/${epPriceId}',iconCls:'icon-edit',method:'post',
	    	singleSelect:false,border:false,pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
		     <thead><tr>
		     	<th data-options="field:'id',checkbox:true"></th> 
		     	<th data-options="field:'quoteCount',formatter:optEpWholeHisFmt">报价轮数</th> 
		     	<!-- <th data-options="field:'quoteEndTime'">报价截止时间</th> 
		     	<th data-options="field:'createTime'">报价时间</th> 
		     	<th data-options="field:'createUserName'">报价人</th> 
		     	<th data-options="field:'totalQuotePrice'">报价总价</th> -->
		     	<th data-options="field:'materialCode',formatter:function(v,r,i){return r.epMaterial.materialCode}">物料编码</th>
		     	<th data-options="field:'materialName',formatter:function(v,r,i){return r.epMaterial.materialName}">物料名称</th>
		     	<th data-options="field:'planPurchaseQty',formatter:function(v,r,i){return r.epMaterial.planPurchaseQty}">数量</th>
		     	<th data-options="field:'freight',formatter:function(v,r,i){return r.epMaterial.freight}">运输费用</th>
		     	<th data-options="field:'totalQuotePrice'">无税单价</th>
		     	<th data-options="field:'quotePrice'">含税单价</th>
		     	<th data-options="field:'supplyCycle'">供货周期</th>
		     	<th data-options="field:'taxRate'">税率</th>
		     	<th data-options="field:'taxCategory'">税种</th>
		     	<th data-options="field:'warrantyPeriod'">保质期</th>
		     	<th data-options="field:'transportationMode'">运输方式</th>
		     	<th data-options="field:'paymentMeans'">付款方式</th>
		     </tr></thead>
	   	</table>
   	</div> --%>
   	</form> 
</div>