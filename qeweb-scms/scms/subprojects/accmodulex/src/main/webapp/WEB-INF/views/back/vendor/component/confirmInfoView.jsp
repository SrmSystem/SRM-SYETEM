<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<body>
<div class="container-fluid">
<input id="confirmStatus_hidden" type="hidden" value="${vendorBaseInfo.org.confirmStatus}" />
		<fieldset>
			<div class="row">
			<div class="col-md-4">
				<label for="shortName" class="control-label">企业简称:</label>
				<label class="control-label">${vendorBaseInfo.shortName}</label>
			</div>
			<div class="col-md-4">
				<label for="duns" class="control-label">邓白氏编码:</label>
				<label class="control-label">${vendorBaseInfo.duns}</label>
			</div>
			<div class="col-md-4">
				<label for="property" class="control-label">企业性质:</label>
				<label class="control-label">
				<c:if test='${vendorBaseInfo.property eq 1}'>国企</c:if>
              	<c:if test='${vendorBaseInfo.property eq 2}'>独资</c:if>
              	<c:if test='${vendorBaseInfo.property eq 3}'>合资</c:if>
              	<c:if test='${vendorBaseInfo.property eq 4}'>民营</c:if></label>
			</div>
			</div>
			<div class="row">
			<div class="col-md-4">
				<label for="regtime" class="control-label">成立时间:</label>
				<label class="control-label"><fmt:formatDate value="${vendorBaseInfo.regtime}" type="date" dateStyle="full"/></label>
			</div>
			<div class="col-md-4">
				<label for="legalPerson" class="control-label">企业法人:</label>
				<label class="control-label">${vendorBaseInfo.legalPerson}</label>
			</div>
			<div class="col-md-4">
				<label for="stockShare" class="control-label">股比构成:</label>
				<label class="control-label">${vendorBaseInfo.stockShare}</label>
			</div>
			</div>
			<div class="row">
			 <div class="col-md-12">
				<label for="address" class="control-label">地址:</label>
				<label for="address" class="control-label">
					${vendorBaseInfo.countryText}
					${vendorBaseInfo.provinceText}
					${vendorBaseInfo.cityText}
					${vendorBaseInfo.districtText}
				</label>
			</div>
			</div>
			<div class="row">
			<div class="col-md-12">
				<label for="stockShare" class="control-label">详细地址:</label>
				<label class="control-label">
					${vendorBaseInfo.address}
				</label>
			</div>
			</div>
			
		</fieldset>
		<fieldset>
		  <legend></legend>
		  <div class="row">
			<div class="col-md-12">
				<label for="mainProduct" class="control-label">主要产品:</label>
				<label class="control-label">
					${vendorBaseInfo.mainProduct}
				</label>
			</div>
		  </div>
		</fieldset>
		<fieldset>
		  <legend></legend>
		  <div class="row">
			<div class="col-md-12">
				<label class="control-label">主要客户:</label>
			</div>
			<div class="col-md-12">
				<div>
				  <table class="table table-bordered">
				    <thead>
				      <tr>
				        <th>序号</th>
				        <th>主机厂</th>
				        <th>产品线</th>
				        <th>车型</th>
				      </tr>
				    </thead>
				    <tbody id="mainCustomerCt">
				      <c:forEach items="${vendorBaseInfo.exList}" var="vendorBaseInfoEx" varStatus="status">
				      <tr>
				        <td>${status.count}</td>   
				        <td>${vendorBaseInfoEx.motorFactory}</td>   
				        <td>${vendorBaseInfoEx.productLine}</td>   
				        <td>${vendorBaseInfoEx.carModel}</td>   
				      </tr>
				      </c:forEach>
				    </tbody>
				  </table>
				</div>
			</div>
		  </div>
		</fieldset>
        <fieldset>
			<legend></legend>
			<div class="row">
			<div class="col-md-3">
				<label for="saleIncoming" class="control-label">上一年度销售收入（万元）:</label>
				<label class="control-label">
					${vendorBaseInfo.lastYearIncome}
				</label>
			</div>
			<div class="col-md-3">
				<label for="employCount" class="control-label">员工人数（人）:</label>
				<label class="control-label">
					${vendorBaseInfo.employeeAmount}
				</label>
			</div>
			<div class="col-md-3">
				<label for="productPower" class="control-label">产能（万台/年）:</label>
				<label class="control-label">
					${vendorBaseInfo.productPower}
				</label>
			</div>
			</div>
		</fieldset>		
        <fieldset>
			<legend></legend>
			<div class="row">
			<div class="">
				<label for="quality" class="control-label">质量体系证书:</label>
				<label class="control-label">
				${vendorBaseInfo.qsCertificate}
				</label>
			</div>
			</div>
		</fieldset>	
		<div style="margin-left: 45%">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="VendorConfirm.confirm2(${vendorBaseInfo.id})">确认</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-2012080412301'" onclick="VendorConfirm.confirmReject2(${vendorBaseInfo.id})">驳回</a>
		</div>	
</div>
</body>
</html>
