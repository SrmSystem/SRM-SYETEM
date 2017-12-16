<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>批量整项报价页面</title>
	<script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/ep/epWholeQuoManage.js"></script>
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
<script type="text/javascript">
	$.extend($.fn.validatebox.defaults.rules, {
	    comboVry: {
	        validator: function (value, param) {//param为默认值
	        	if (value == "" || value.indexOf('-全部-') >= 0) { 
	                return false;  
	             }else {  
	                return true;  
	             }     
	           // return value == param;
	        },
	        message: '该输入项为必输项'
	    }
	});
</script>
</head>

<body style="margin:0;padding:0;">
<script type="text/javascript">
	var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($('#datagrid-epWholeQuo-mass').datagrid('validateRow', editIndex)){
			$('#datagrid-epWholeQuo-mass').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	
	function onClickRow2(index,data){
		$("#indexVal").val(index);
		if (editIndex != index){
			if (endEditing()){
				$('#datagrid-epWholeQuo-mass').datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-epWholeQuo-mass').datagrid('selectRow', editIndex);
			}
		}
	}
	
</script>
<div class="easyui-panel" style="overflow: auto;width: 100%;height: 100%">
  <form id="form-epWholeQuo-mass" method="post" enctype="multipart/form-data">
  	<input id="epPriceId" value="${epPriceId}" hidden="true" />
  	<input id="epVendorId" value="${epVendorId}" hidden="true" />
  	<input id = "taxCodeVal" hidden="true">
  	<input id = "indexVal" hidden="true">
  	<div>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveEpWholeQuoMass('save')">保存</a> -->
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveEpWholeQuoMass('submit')">提交</a>
	</div>
	  <table style="width: 90%;margin: auto;margin-top: 20px">
		<tr>
			<td style="width: 30%">询价单号：${epPrice.enquirePriceCode}</td>
			<td style="width: 30%">供应商：${epVendor.vendorName}</td> 
			<td style="width: 30%">报价日期：
				<input id="epWholeQuoTime" name="createTime" value="" class="easyui-datebox" data-options="required:true,editable:false" style="width:130px"/>
			</td>  
		</tr>
	  </table>
	  <div style="width: 90%;margin: auto;margin-top: 10px">
	  <table id="datagrid-epWholeQuo-mass" title="" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/ep/epWholeQuo/buyerGetList/${epPriceId}/${epVendorId}/0',method:'post',singleSelect:false,
		fit:false,border:false,queryParams:{'search-EQ_epMaterial.id':'${epMaterialIds}'},
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,onClickRow: onClickRow2">
		     <thead><tr>
		     	<th width="50px" data-options="field:'id',checkbox:true,hidden:true"></th> 
		     	<th width="100px" data-options="field:'materialCode',formatter:function(v,r,i){return r.epMaterial.materialCode}">物料编码</th> 
		     	<th width="100px" data-options="field:'materialName',formatter:function(v,r,i){return r.epMaterial.materialName}">物料名称</th> 
		     	<th width="100px" data-options="field:'materialSpec',formatter:function(v,r,i){return r.epMaterial.materialSpec}">规格型号</th> 
		     	<th width="100px" data-options="field:'materialUnit',formatter:function(v,r,i){return r.epMaterial.materialUnit}">单位</th>	<!-- 选择供应商自动带出  -->
		     	<th width="100px" data-options="field:'planPurchaseQty',formatter:function(v,r,i){return r.epMaterial.planPurchaseQty}">数量</th> 
		     	<th width="100px" data-options="field:'freight',formatter:function(v,r,i){return r.epMaterial.freight}">运输费用</th> 
		     	<th width="100px" data-options="field:'totalQuotePrice',
		     		editor:{
			     		type:'numberbox',
			     		options:{
			     		required:true,
			     		precision:2,
			     		onChange:function(rec,i){
			     			var taxRateVal = $('#taxCodeVal').val();
			     			var index = $('#indexVal').val();
			     			var quotePriceVal = (parseFloat(1)+parseFloat(taxRateVal))*parseFloat(rec);
			     			//var row=$('#datagrid-epWholeQuo-mass').datagrid('getSelected');
							//var index=$('#datagrid-epWholeQuo-mass').datagrid('getRowIndex',row);
			     			var ed = $('#datagrid-epWholeQuo-mass').datagrid('getEditor', {index:index,field:'quotePrice'});
							$(ed.target).numberbox('setValue', quotePriceVal);
			     		}
			     	}
			     }
		     	">无税单价</th> 
		     	<th data-options="field:'quotePrice',editor:{type:'numberbox',options:{readonly:true,precision:2}}">含税单价</th> 
		     	<th data-options="field:'negotiatedPrice'">协商单价</th> 
		     	<th data-options="field:'brand'">产地/品牌</th> 
		     	<th data-options="field:'warranty',formatter:function(v,r,i){return r.epMaterial.warranty}">保修期</th> 
		     	<th data-options="field:'supplyCycle',editor:{type:'textbox'}" width="120px">供货周期</th> 
		     	<th data-options="field:'remarks',formatter:function(v,r,i){return r.epMaterial.remarks}" width="140px">备注</th> 
		     </tr></thead>
	   </table>
	   </div>
	   <table style="width: 90%;margin: auto;margin-top: 10px">
	   <tr>
		   <td>税率：
        		<select class="easyui-combobox" id="taxRateMass" name="taxRate" data-options="required:true,validType:'comboVry'" style="width: 130px" >
				<option value="">-全部-</option>
				<option value="0.03">3%</option>
				<option value="0.04">4%</option>
				<option value="0.06">6%</option>
				<option value="0.07">7%</option>
				<option value="0.11">11%</option>
				<option value="0.13">13%</option>
				<option value="0.17">17%</option>
				</select>
			</td>
		   <td>税种：
         		<select class="easyui-combobox" id="taxCategory" name="taxCategory" data-options="required:true,validType:'comboVry'" style="width: 130px" >
				<option value="">-全部-</option>
				<option value="可抵扣">可抵扣</option>
				<option value="不可抵扣">不可抵扣</option>
				</select>
		   </td>
			<td></td>
			<td></td>
	   </tr>
	   <tr>
	   	   <td colspan="4">保质期：
         		<input id="warrantyPeriod" name="warrantyPeriod" value="" class="easyui-textbox" data-options="required:true" style="width:130px;"/>
	   	   </td>
	   </tr>
	   <tr>
	   	   <td colspan="4">运输方式：
				<select class="easyui-combobox" id="transportationMode" name="transportationMode" data-options="required:true,validType:'comboVry'" style="width: 130px" >
				<option value="">-全部-</option>
				<option value="空运">空运</option>
				<option value="汽运">汽运</option>
				<option value="铁运">铁运</option>
				<option value="船运">船运</option>
				<option value="需方自提">需方自提</option>
				</select>	       		
	   	   </td>
	   </tr>
	  <!--  <tr>
		   <td colspan="4">付款方式：
				<input id="paymentMeans" name="paymentMeans" value="" class="easyui-textbox" data-options="required:true" style="width:130px;"/>    
		   </td>
	   </tr> -->
	   <tr>
		   	<td colspan="4">报价附件：
				 <input class="easyui-filebox" name="quoteTemplate" data-options="buttonText:'浏览'" style="width: 200px"/>
			</td>
		   <!-- <td>图片附件：</td> -->
	   </tr>
<!-- 	   <tr>
		   <td colspan="4">技术承诺：
				 <input class="easyui-textbox" id="technologyPromises" name="technologyPromises" value="" type="text" max="300"
				data-options="multiline:true" style="width:90%;height: 80px"/>
			</td>
	   </tr>
	   <tr>
		   <td colspan="4">质量承诺：
				 <input class="easyui-textbox" id="qualityPromises" name="qualityPromises" value="" type="text" max="300"
				data-options="multiline:true" style="width:90%;height: 80px"/>
		  </td>
	   </tr>
	   <tr>
		   <td colspan="4">服务承诺：
				 <input class="easyui-textbox" id="servicePromises" name="servicePromises" value="" type="text" max="300"
				data-options="multiline:true" style="width:90%;height: 80px"/>
		  </td>
	   </tr>
	   <tr>
		   <td colspan="4">交期承诺：
				<input class="easyui-textbox" id="deliveryPromises" name="deliveryPromises" value="" type="text" max="300"
				data-options="multiline:true" style="width:90%;height: 80px"/>
		  </td>
	   </tr> -->
	   <tr>
		   <td colspan="4">其他承诺：
				<input class="easyui-textbox" id="otherPromises" name="otherPromises" value="" type="text" max="300"
				data-options="multiline:true" style="width:90%;height: 80px"/>
		   </td>
	   </tr>
	   </table>
  </form>
</div>
<script type="text/javascript">
/* $('#datagrid-epWholeQuo-mass').datagrid({onLoadSuccess : function(data){
  //  $('#datagrid-epWholeQuo-mass').datagrid('selectRow',0);
	 $('#datagrid-epWholeQuo-mass').datagrid('selectRow', 0).datagrid('beginEdit', 0);
	 $("#indexVal").val(0);
}}); */
$(function(){
		   var curr_time = new Date();
		   var strDate = curr_time.getFullYear()+"-";
		   strDate += curr_time.getMonth()+1+"-";
		   strDate += curr_time.getDate()+"-";
		   strDate += curr_time.getHours()+":";
		   strDate += curr_time.getMinutes()+":";
		   strDate += curr_time.getSeconds();
		   $("#epWholeQuoTime").datebox("setValue", strDate); 
});
</script>
</body>
</html>
