<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
$(function(){
	if($('#senderId')) {
		$('#senderId').combobox({
			url: '${ctx}/manager/order/goodsrequest/getSenders/${po.vendor.id}',   
			valueField:'id',
			textField:'name',
			onLoadSuccess: function () { //加载完成后,设置选中第一项
		        var val = $(this).combobox("getData");
		        $('#senderId').combobox('setValue', '${po.sender.id}');
		    }
		});  
	}
});
</script>	
<div data-options="fit:true">
	<div>
		<c:if test="${!vendor && po.publishStatus eq 0 }">  
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="RequestManage.editReqInfo()">保存</a>
		</c:if>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="RequestManage.exportPurchasePlan()">导出</a>
	</div>
	
	<div class="easyui-panel">
		<form id="form-goodsrequestitem-search">
			<input id="id" name="id" value="${po.id }" type="hidden"/>
			<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>要货单号：${po.requestCode }</td>
					<td>供应商编码：${po.vendor.code }</td>
					<td>供应商名称：${po.vendor.name }</td>
				</tr>
				<tr>
					<c:choose>
						<c:when test="${!vendor && po.publishStatus eq 0}">
							<td>发货方：<select id="senderIdView" name="senderId" class="easyui-combobox" style="width:150px;" data-options="editable:false,required:true,events:{focus:function(){OrgManage.viewChoice('${po.vendor.id}');}}" >
								 </select></td> 
						</c:when>
						<c:otherwise>
							<td>发货方：${po.sender.name }</td>
						</c:otherwise>
					</c:choose>
				
					<td>收货方：${po.receiveOrg }</td>
				</tr>
		</form>  
	</div>
	 
	<table id="datagrid-goodsrequestitem-list" title="要货单详情" class="easyui-datagrid"
		data-options="method:'post',singleSelect:false,cache:false,
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,30],
		<c:if test="${!vendor && po.publishStatus eq 0 }">
			onClickCell: RowEditor.onClickCell,
		</c:if>
		url: '${ctx }/manager/order/goodsrequest/requestitem/${po.id }'">
		<thead><tr>  
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'itemNo'">行号</th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
		<th data-options="field:'receiveOrg'">收货方</th>   
		<th data-options="field:'orderQty',editor:{type:'numberbox',options:{required:true}}">需求数量</th> 
		<th data-options="field:'storeQty',formatter:function(v,r,i){return '';}">三方库存</th>   
		<th data-options="field:'wmsQty',formatter:function(v,r,i){return '';}">WMS库存</th>   
		<th data-options="field:'vendorQty',formatter:function(v,r,i){return '';}">供应商库存</th>   
		<th data-options="field:'curMonthPlanQty',formatter:function(v,r,i){return '';}">当月采购计划</th>   
		<th data-options="field:'curOrderQty',formatter:function(v,r,i){return '';}">当月已采购量</th>     
		<th data-options="field:'requestTime',editor:'datetimebox',required:true,showSeconds:true">要求到货时间</th> 
		<th data-options="field:'currency'">币种</th>   
		<th data-options="field:'unitName'">单位</th>   
		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(r.request.confirmStatus,'confirm',false);}">确认状态</th>   
		<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}">发货状态</th>   
		<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}">收货状态</th>   
		</tr></thead>
	</table> 
</div>
	
