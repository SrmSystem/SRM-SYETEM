<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>采购商查看整项报价详情页面</title>


	<style>
     .infoT {
        border-top:1px solid #888;
        margin-top: 10px;
    }
    .infoT th{
        text-align: center; 
        height: 25px;
        font-size: 12px;
        background-color: #D0D0D0;
        border: 1px solid #888;
    }
    .infoT td{
        text-align: center; 
        height: 20px;
        font-size: 12px;
        border: 1px solid #888;
    }
</style>
</head>

<body style="margin:0;padding:0;">
	<script type="text/javascript">
	var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($('#datagrid-item-list').datagrid('validateRow', editIndex)){
			$('#datagrid-item-list').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}

	function onClickRow2(index,data){
		if (editIndex != index){
			if (endEditing()){
				$('#datagrid-item-list').datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-item-list').datagrid('selectRow', editIndex);
			}
		}
		 var ed = $('#datagrid-item-list').datagrid('getEditor', { index: index, field: 'qty' });
		 var total=$('#datagrid-item-list').datagrid('getEditor', { index: index, field: 'negotiatedSubTotalPrice' });
		 var taxPricex=$('#datagrid-item-list').datagrid('getEditor', { index: index, field: 'negotiatedSubPrice' });
	
		 $(ed.target).numberbox({ onChange: function () {
	    	 caculate();
	    	 caculateTotalX();
	     }    
	     });
	     $(taxPricex.target).numberbox({ onChange: function () {
	    	
	    	 caculate();
	    	 caculateTotalX();
	     }    
	     });
	     function caculate(){
	          var itemQty=ed.target.val();
	          var taxPrice=taxPricex.target.val();;
	          $(ed.target).numberbox('setValue',itemQty);    
	          $(taxPricex.target).numberbox('setValue',taxPrice);    
	          $(total.target).numberbox('setValue',itemQty*taxPrice);    
	     }
	     
	     function caculateTotalX(){
	    	 var rows = $("#datagrid-item-list").datagrid("getRows");
	    	  var tt=0;
	    	  for(var i=0;i<rows.length;i++){
	    		  var total=$('#datagrid-item-list').datagrid('getEditor', { index: i, field: 'negotiatedSubTotalPrice' });
	    		  if(total == null){
	    			  tt = parseFloat(tt)+parseFloat(rows[i].negotiatedSubTotalPrice);
	    		  }else{
	    			  tt=parseFloat(tt)+parseFloat(total.target.val());
	    		  }
	    	  //$("#negotiatedPrice").val(tt);
	    		      
	    	  }
	    	  
	    	  $("#negotiatedPrice").numberbox('setValue',tt);
	     }
	}
	</script>
<div class="easyui-panel" style="overflow: auto;width: 100%;height: 100%">
  <form id="form-epWholeQuo-Item" method="post" enctype="multipart/form-data">
  	<input id="id" name="id" value="${epWholeQuo.id}" hidden="true" />
  	<input id="epPriceQuoteWay" value="${epPrice.quoteWay}" hidden="true" />
  	<div>
 <%--  	<c:if test="${epWholeQuo.negotiatedStatus eq 0 && epWholeQuo.epPrice.quoteWay eq 0 && (epWholeQuo.epVendor.eipApprovalStatus lt 1 || epWholeQuo.epVendor.eipApprovalStatus==null)}"> --%>
  	
  	<c:if test="${epWholeQuo.negotiatedStatus eq 0 && epWholeQuo.epPrice.quoteWay eq 0 && epWholeQuo.epVendor.quoteStatus eq 1}">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="submitByItem('save',${epWholeQuo.cooperationStatus})">保存</a>
	</c:if>
	
	</div>
	
	  <table style="width: 90%;margin: auto;margin-top: 20px">
		<tr>
			<td style="width: 30%">询价单号：${epPrice.enquirePriceCode}</td>
			<td style="width: 30%">供应商：${epWholeQuo.epVendor.vendorName}</td> 
			<td style="width: 30%">报价日期：${epWholeQuo.createTime}</td>  
		</tr>
	  </table>
	  <table class="infoT" style="width: 90%;margin: auto;margin-top: 10px;border: 1px">
        <tr >
            <th style="width: 6%;">物料编码</th>       
            <th style="width: 6%;">物料名称</th>
            <th style="width: 10%;">规格型号</th>
            <th style="width: 5%;">单位</th>
            <th style="width: 6%;">数量</th>       
            <th style="width: 7%;">运输费用</th>
            <th style="width: 10%;">无税单价</th>
            <th style="width: 10%;">含税单价</th>
           	<th style="width:10%;">协商单价</th>
            <th style="width: 13%;">产地/品牌</th>       
            <th style="width: 10%;">保修期</th>
            <th style="width: 10%;">供货周期</th>
            <th style="width: 17%;">备注</th>
        </tr>
        <tr>
            <td>${epWholeQuo.epMaterial.materialCode}</td>				<!-- 物料编码 -->
            <td>${epWholeQuo.epMaterial.materialName}</td>				<!-- 物料名称 -->	     
            <td>${epWholeQuo.epMaterial.materialSpec}</td>				<!-- 规格型号 -->
            <td>${epWholeQuo.epMaterial.materialUnit}</td>				<!-- 单位 -->
            <td>${epWholeQuo.epMaterial.planPurchaseQty}</td>			<!-- 数量 -->	
            <td>${epWholeQuo.epMaterial.freight}</td>				<!-- 运输费用 -->
            <td>${epWholeQuo.totalQuotePrice}</td>				<!-- 无税单价 -->
            <td>${epWholeQuo.quotePrice}</td>				<!-- 含税单价 -->
            <td>
             <c:if test="${epPrice.quoteWay eq 1}">
		       <c:choose>
					<c:when test="${epWholeQuo.negotiatedStatus eq 0}">
						<c:choose>
							<c:when test="${epWholeQuo.negotiatedPrice eq null || epWholeQuo.negotiatedPrice eq 0 || empty epWholeQuo.negotiatedPrice}">
							    <input id="negotiatedPrice" name="negotiatedPrice" value="${epWholeQuo.totalQuotePrice}" class="easyui-numberbox" data-options="required:true,precision:2" style="width:100px;"/>
							</c:when>
							<c:otherwise>
								<input id="negotiatedPrice" name="negotiatedPrice" value="${epWholeQuo.negotiatedPrice}" class="easyui-numberbox" data-options="required:true,precision:2" style="width:100px;"/>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						${epWholeQuo.negotiatedPrice}
					</c:otherwise>
			   </c:choose>
            </c:if>
             <c:if test="${epPrice.quoteWay eq 0}">
	            	 <input class="easyui-numberbox" data-options="precision:2" type="text" id="negotiatedPrice" name="negotiatedPrice" value="${epWholeQuo.negotiatedPrice}"   style="width:100px;"/>
            </c:if>
            </td>
            <td>${epWholeQuo.brand}</td>				<!-- 产地/品牌 -->
            <td>${epWholeQuo.epMaterial.warranty}</td>				<!-- 保修期 -->
            <td>${epWholeQuo.supplyCycle}</td>				<!-- 供货周期 -->	
            <td>${epWholeQuo.epMaterial.remarks}</td>				<!-- 备注  -->	
         </tr>
	   </table>
	   
	   <table style="width: 90%;margin: auto;margin-top: 10px">
	   <tr>
		   <td>税率：
		   		<c:if test="${epWholeQuo.taxRate eq 0.03 }">3%</c:if> <c:if test="${epWholeQuo.taxRate eq 0.04 }">4%</c:if>
				<c:if test="${epWholeQuo.taxRate eq 0.06 }">6%</c:if> <c:if test="${epWholeQuo.taxRate eq 0.07 }">7%</c:if>
				<c:if test="${epWholeQuo.taxRate eq 0.11 }">11%</c:if> <c:if test="${epWholeQuo.taxRate eq 0.13 }">13%</c:if>
				<c:if test="${epWholeQuo.taxRate eq 0.17 }">17%</c:if>
		   </td>
		   <td>税种：${epWholeQuo.taxCategory}</td>
			<td></td>
			<td></td>
	   </tr>
	   <%-- 	<tr>
	   	   <td colspan="4">价格变动因素：
	   	 						<select class="easyui-combobox" id="priceChangeNum"
									name="priceChangeNum"
									data-options="required:true,validType:'comboVry',editable:false"
									style="width: 130px">
									<option value="">-全部-</option>
									<option value="1" ${epWholeQuo.priceChangeNum eq 1 ? "selected" : "" }>首次定价</option>
									<option value="2" ${epWholeQuo.priceChangeNum eq 2 ? "selected" : "" }>商务调价</option>
									<option value="3" ${epWholeQuo.priceChangeNum eq 3 ? "selected" : "" }>原材料影响调价</option>
									<option value="4" ${epWholeQuo.priceChangeNum eq 4 ? "selected" : "" }>质量影响调价</option>
									<option value="5" ${epWholeQuo.priceChangeNum eq 5 ? "selected" : "" }>设计影响调价</option>
									<option value="6" ${epWholeQuo.priceChangeNum eq 6 ? "selected" : "" }>其它</option>
				
								</select>


						
			</td>
	   </tr> --%>
	   
	   <tr>
	   	   <td colspan="4">保质期：${epWholeQuo.warrantyPeriod}</td>
	   </tr>
	   <tr>
	   	   <td colspan="4">运输方式：${epWholeQuo.transportationMode}</td>
	   </tr>
	  <%--  <tr>
		   <td colspan="4">付款方式：${epWholeQuo.paymentMeans}</td>
	   </tr> --%>
	   <tr>
		   <td colspan="4">报价附件：
				<a href="javascript:;" onclick="File.download('${epWholeQuo.quoteTemplateUrl}','${epWholeQuo.quoteTemplateName}')">${epWholeQuo.quoteTemplateName}</td>
		   <!-- <td>图片附件：</td> -->
	   </tr>
<%-- 	   <tr>
		   <td colspan="4">技术承诺：<input class="easyui-textbox" id="technologyPromises" name="technologyPromises" value="${epWholeQuo.technologyPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr>
	   <tr>
		   <td colspan="4">质量承诺：<input class="easyui-textbox" id="qualityPromises" name="qualityPromises" value="${epWholeQuo.qualityPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr>
	   <tr>
		   <td colspan="4">服务承诺：<input class="easyui-textbox" id="servicePromises" name="servicePromises" value="${epWholeQuo.servicePromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr>
	   <tr>
		   <td colspan="4">交期承诺：<input class="easyui-textbox" id="deliveryPromises" name="deliveryPromises" value="${epWholeQuo.deliveryPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr> --%>
	   <tr>
		   <td colspan="4">其他承诺：<input class="easyui-textbox" id="otherPromises" name="otherPromises" value="${epWholeQuo.otherPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr>
	   
	   	<input type="hidden" name="tableDatas" id="tableDatas" />
	   </table>
  </form>
  
    <c:if test="${epPrice.quoteWay eq 0}">
  			<table id="datagrid-item-list" title="报价明细" class="easyui-datagrid"
		data-options="method:'post',singleSelect:false,
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
		onClickRow: onClickRow2,
		url: '${ctx }/manager/ep/epSubQuo/getSubQuoByWholeId/${epWholeQuo.id}'">
		<thead><tr>  
		<th width="50px" data-options="field:'id',checkbox:true"></th>
		<th width="50px" data-options="field:'wholeQuoId',hidden:true"></th>
		    
		<th width="80px" data-options="field:'materialName',width:80,editor:'textbox'">费目类别</th>
		<th width="80px" data-options="field:'remarks',width:80,editor:'textbox'">费目备注</th>
			
		<th width="80px" data-options="field:'totalQuotePrice',width:80,editor:{type:'numberbox', options: {readonly:true,precision:2}},formatter:subQuoManage.viewCostFmt">无税单价</th>
		<th width="80px" data-options="field:'qty',width:80,editor:{type:'numberbox',options: {readonly:true}}">数量</th>
		<th width="80px" data-options="field:'subtotal',width:80,editor:{type:'numberbox', options: {precision:2}}">小计</th>
		
		<th width="80px" data-options="field:'negotiatedSubPrice',width:80,editor:{type:'numberbox',options: {readonly:false,precision:2}}">协商价格</th>
		<th width="80px" data-options="field:'negotiatedSubTotalPrice',width:80,editor:{type:'numberbox',options: {readonly:true,precision:2}}">协商小计</th>

		</tr></thead>
	</table> 
	
		<div id="win-cost-detail" title="费目明细" class="easyui-window"
			data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
			
			<div id="tbCost" style="padding:5px;height:auto">
				<div style="margin-bottom:5px">
		        	
				</div>
			</div>
		 
		  
	
	  		<table id="datagrid-costItem-list" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit', 
		  			singleSelect: true,toolbar: '#tbCost',method: 'post',pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
	              <thead><tr>
	            
	        <th width="50px" data-options="field:'id',checkbox:true"></th>
	        <th width="50px" data-options="field:'epQuoSubId',hidden:true"></th>
	        
			<th width="100px" data-options="field:'name',width:80,editor:'textbox'">费目名称</th>
			<th width="100px" data-options="field:'price',width:80,editor:{type:'numberbox', options: {readonly:false,precision:2}}">含税单价</th>
			<th width="100px" data-options="field:'qty',width:80,editor:{type:'numberbox', options: {readonly:false}}">数量</th>
			<th width="100px" data-options="field:'totalPrice',width:80,editor:{type:'numberbox', options: {readonly:true,precision:2}}">小计</th>
	   
	          </tr></thead>
	   		</table>
		
		</div>
    </c:if>
    
</div>


</body>
</html>
