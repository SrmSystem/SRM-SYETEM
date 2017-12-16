<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>整项报价页面</title>
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
<div class="easyui-panel" style="overflow: auto;width: 100%;height: 100%">
  <form id="form-epWholeQuo-offer" method="post" enctype="multipart/form-data">
  	<input id="epWholeQuoId" value="${epWholeQuo.id}" hidden="true" />
  	<div>
  	<c:if test="${epWholeQuo.quoteStatus ne 1 && isVendor eq 1 && epWholeQuo.epPrice.quoteStatus eq 1}">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveEpWholeQuo('save')">保存</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveEpWholeQuo('submit')">提交</a>
	</c:if>
	<c:if test="${epWholeQuo.negotiatedStatus eq 1 && epWholeQuo.negotiatedCheckStatus eq 0}">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="confirmEpWholeQuo('accept')">接受议价</a>
		<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="confirmEpWholeQuo('refuse')">拒绝议价</a> -->
	</c:if>
	</div>
	
	  <table style="width: 90%;margin: auto;margin-top: 20px">
		<tr>
			<td style="width: 30%">询价单号：${epPrice.enquirePriceCode}</td>
			<td style="width: 30%">供应商：${epWholeQuo.epVendor.vendorName}</td> 
			<td style="width: 30%">报价日期：
			<c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
					<fmt:formatDate value="${epWholeQuo.createTime}" type="both" pattern="yyyy-MM-dd"/>
				</c:when>
				<c:otherwise>
					<input id="epWholeQuoTime" name="createTime" value="${epWholeQuo.createTime}" class="easyui-datebox" data-options="required:true,editable:false" style="width:130px"/>
				</c:otherwise>
			</c:choose>
			</td>  
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
            <th style="width: 7%;">无税单价</th>
            <th style="width: 10%;">含税单价</th>
            <c:if test="${epWholeQuo.negotiatedStatus eq 1}">
           		<th style="width:10%;">协商单价</th>
            </c:if>
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
            <td>
            <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.totalQuotePrice}
				</c:when>
				<c:otherwise>
					<input id="totalQuotePrice" name="totalQuotePrice" value=" ${epWholeQuo.totalQuotePrice}" class="easyui-numberbox" data-options="required:true,precision:2" style="width:100px;"/>				
				</c:otherwise>
			</c:choose>
            </td>				<!-- 无税单价 -->
            <td>	
            <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.quotePrice}
				</c:when>
				<c:otherwise>
					<input id="quotePrice" name="quotePrice" value=" ${epWholeQuo.quotePrice}" class="easyui-numberbox" data-options="readonly:false,precision:2" style="width:100px;"/>	<!-- 含税单价 -->			
				</c:otherwise>
			</c:choose>				
            </td>
            <c:if test="${epWholeQuo.negotiatedStatus eq 1}">
           		<td>${epWholeQuo.negotiatedPrice}</td>		<!-- 协议单价 -->
            </c:if>
            <td>${epWholeQuo.brand}</td>				<!-- 产地/品牌 -->
            <td>${epWholeQuo.epMaterial.warranty}</td>				<!-- 保修期 -->
            <td>
          	<c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.supplyCycle}
				</c:when>
				<c:otherwise>
          			<input id="supplyCycle" name="supplyCycle" value=" ${epWholeQuo.supplyCycle}" class="easyui-textbox" style="width:100px;"/>
          		</c:otherwise>
			</c:choose>
            </td>				<!-- 供货周期 -->	
            <td>${epWholeQuo.epMaterial.remarks}</td>				<!-- 备注  -->	
         </tr>
	   </table>
	   
	   <table style="width: 90%;margin: auto;margin-top: 10px">
	   <tr>
		   <td>税率：
		   <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
					<c:if test="${epWholeQuo.taxRate eq 0.03 }">3%</c:if> <c:if test="${epWholeQuo.taxRate eq 0.04 }">4%</c:if>
					<c:if test="${epWholeQuo.taxRate eq 0.06 }">6%</c:if> <c:if test="${epWholeQuo.taxRate eq 0.07 }">7%</c:if>
					<c:if test="${epWholeQuo.taxRate eq 0.11 }">11%</c:if> <c:if test="${epWholeQuo.taxRate eq 0.13 }">13%</c:if>
					<c:if test="${epWholeQuo.taxRate eq 0.17 }">17%</c:if>
					
				</c:when>
				<c:otherwise>
	         			<select class="easyui-combobox" id="taxRate" name="taxRate" data-options="required:true,validType:'comboVry'" style="width: 130px">
						<option value="">-全部-</option>
						<option value="0.03" ${epWholeQuo.taxRate eq "0.03" ? "selected" : "" }>3%</option>
						<option value="0.04" ${epWholeQuo.taxRate eq "0.04" ? "selected" : "" }>4%</option>
						<option value="0.06" ${epWholeQuo.taxRate eq "0.06" ? "selected" : "" }>6%</option>
						<option value="0.07" ${epWholeQuo.taxRate eq "0.07" ? "selected" : "" }>7%</option>
						<option value="0.11" ${epWholeQuo.taxRate eq "0.11" ? "selected" : "" }>11%</option>
						<option value="0.13" ${epWholeQuo.taxRate eq "0.13" ? "selected" : "" }>13%</option>
						<option value="0.17" ${epWholeQuo.taxRate eq "0.17" ? "selected" : "" }>17%</option>
						</select>
	       		</c:otherwise>
			</c:choose>
			</td>
		   <td>税种：
		    <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.taxCategory}
				</c:when>
				<c:otherwise>
	         		<select class="easyui-combobox" id="taxCategory" name="taxCategory" data-options="required:true,validType:'comboVry'" style="width: 130px" >
					<option value="">-全部-</option>
					<option value="可抵扣" ${epWholeQuo.taxCategory eq "可抵扣" ? "selected" : "" }>可抵扣</option>
					<option value="不可抵扣" ${epWholeQuo.taxCategory eq "不可抵扣" ? "selected" : "" }>不可抵扣</option>
					</select>
	       		</c:otherwise>
			</c:choose>
		   </td>
			<td></td>
			<td></td>
	   </tr>
	   <tr>
	   	   <td colspan="4">保质期：
	   	   <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.warrantyPeriod}
				</c:when>
				<c:otherwise>
	         		<input id="warrantyPeriod" name="warrantyPeriod" value="${epWholeQuo.warrantyPeriod}" class="easyui-textbox" data-options="required:true" style="width:130px;"/>
	       		</c:otherwise>
			</c:choose>
	   	   </td>
	   </tr>
	   <tr>
	   	   <td colspan="4">运输方式：
	   	    <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.transportationMode}
				</c:when>
				<c:otherwise>
					<select class="easyui-combobox" id="transportationMode" name="transportationMode" data-options="required:true,validType:'comboVry'" style="width: 130px" >
					<option value="">-全部-</option>
					<option value="空运" ${epWholeQuo.transportationMode eq "空运" ? "selected" : "" }>空运</option>
					<option value="汽运" ${epWholeQuo.transportationMode eq "汽运" ? "selected" : "" }>汽运</option>
					<option value="铁运" ${epWholeQuo.transportationMode eq "铁运" ? "selected" : "" }>铁运</option>
					<option value="船运" ${epWholeQuo.transportationMode eq "船运" ? "selected" : "" }>船运</option>
					<option value="需方自提" ${epWholeQuo.transportationMode eq "需方自提" ? "selected" : "" }>需方自提</option>
					</select>	       		
				</c:otherwise>
			</c:choose>
	   	   </td>
	   </tr>
	   <%-- <tr>
		   <td colspan="4">结算方式：
		    <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.epVendor.specialPurchaseType}
				</c:when>
				<c:otherwise>
					<input id="paymentMeans" name="paymentMeans" value="${epWholeQuo.paymentMeans}" class="easyui-textbox" data-options="required:true" style="width:130px;"/>    
				</c:otherwise>
			</c:choose>
		   </td>
	   </tr> --%>
	   <tr>
		   <td colspan="4">报价附件：
			<c:if test="${epWholeQuo.quoteStatus ne 1}">
				 <input class="easyui-filebox" name="quoteTemplate" data-options="buttonText:'浏览'" style="width: 200px"/>
			</c:if>
			<a href="javascript:;" onclick="File.download('${epWholeQuo.quoteTemplateUrl}','${epWholeQuo.quoteTemplateName}')">${epWholeQuo.quoteTemplateName}</a></td>
		   <!-- <td>图片附件：</td> -->
	   </tr>
<%-- 	   <tr>
		   <td colspan="4">技术承诺：
			<c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
 					<input class="easyui-textbox" id="technologyPromises" name="technologyPromises" value="${epWholeQuo.technologyPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/>				
				</c:when>
				<c:otherwise>
					 <input class="easyui-textbox" id="technologyPromises" name="technologyPromises" value="${epWholeQuo.technologyPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px"/>
				</c:otherwise>
			</c:choose>
			</td>
	   </tr>
	   <tr>
		   <td colspan="4">质量承诺：
		   <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
 					<input class="easyui-textbox" id="qualityPromises" name="qualityPromises" value="${epWholeQuo.qualityPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/>			
				</c:when>
				<c:otherwise>
					 <input class="easyui-textbox" id="qualityPromises" name="qualityPromises" value="${epWholeQuo.qualityPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px"/>
				</c:otherwise>
			</c:choose>
		  </td>
	   </tr>
	   <tr>
		   <td colspan="4">服务承诺：
		   <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
 					<input class="easyui-textbox" id="servicePromises" name="servicePromises" value="${epWholeQuo.servicePromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/>		
				</c:when>
				<c:otherwise>
					 <input class="easyui-textbox" id="servicePromises" name="servicePromises" value="${epWholeQuo.servicePromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px"/>
				</c:otherwise>
			</c:choose>
		  </td>
	   </tr>
	   <tr>
		   <td colspan="4">交期承诺：
		   <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
 					<input class="easyui-textbox" id="deliveryPromises" name="deliveryPromises" value="${epWholeQuo.deliveryPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/>
				</c:when>
				<c:otherwise>
					<input class="easyui-textbox" id="deliveryPromises" name="deliveryPromises" value="${epWholeQuo.deliveryPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px"/>
				</c:otherwise>
			</c:choose>
		  </td>
	   </tr> --%>
	   <tr>
		   <td colspan="4">其他承诺：
		   <c:choose>
				<c:when test="${epWholeQuo.quoteStatus eq 1}">
 					<input class="easyui-textbox" id="otherPromises" name="otherPromises" value="${epWholeQuo.otherPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/>
				</c:when>
				<c:otherwise>
					<input class="easyui-textbox" id="otherPromises" name="otherPromises" value="${epWholeQuo.otherPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px"/>
				</c:otherwise>
			</c:choose>
		   </td>
	   </tr>
	   </table>
  </form>
</div>
<script type="text/javascript">
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
