<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="win-newproduct-import" class="easyui-window" title="导入新产品开发质量" style="width:400px;height:200px"
data-options="iconCls:'icon-add',modal:true,closed:true">
	<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
		<form id="form-newproduct-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/qualityassurance/Newproduct/filesUpload"> 
			<div style="margin-bottom:20px">
				<spring:message code="vendor.file"/><!-- 文件 -->：<input type=file id="file" name="planfiles" /><br>
				<spring:message code="vendor.formwork"/><!-- 模板 -->： <a href="javascript:;" onclick="File.download('WEB-INF/template/NewProduct.xls','新产品开发质量模版.xls')"><spring:message code="vendor.newproduct.newproductDevelopmentQuality"/><!-- 新产品开发质量 -->.xls</a>     
			</div>
			<div style="text-align: center;padding:5px;">
				<a href="javascript:;" class="easyui-linkbutton" onclick="NewProduct.saveimport();"><spring:message code="vendor.preservation"/><!-- 保存 --></a> 
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-newproduct-import').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</div>
		</form>  
	</div>
</div>
