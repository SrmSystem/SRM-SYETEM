<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>供应商阶段设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
</head>

<body>
<script type="text/javascript" src="${ctx}/static/script/vendor/vendorInfoRela.js"></script>
<input type="hidden" id="vendorId" name="vendorId" value="${vendorBaseInfo.id }">
<input type="hidden" id="orgid" name="orgid" value="${vendorBaseInfo.orgId }">
<div class="container-fluid">
<h3 class="page-header">供应商信息<br/><small style="color: #990000;">基本信息</small>
  <div class="btn-group"style="float: right;">
  <button type="button" class="btn btn-default" onclick="lookGKJJSS(${vendorBaseInfo.orgId })">供应商状态变更履历</button>
  <button type="button" class="btn btn-default" onclick="look()">查看完整基本信息</button>
  <button type="button" class="btn btn-default" onclick="BaseVendor.openvfSurvey(${vendorBaseInfo.id })">供货关系</button>
 </div>
</h3>

<table style="width: 100%;">
<tr>
<td><h5 style="font-weight: 900;">供应商代码:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.code }</small></h5></td>
<td><h5 style="font-weight: 900;">供应商名称:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.name }</small></h5></td>
<td><h5 style="font-weight: 900;">企业性质:<small style="color: #000000;margin: 10PX">
<c:if test='${vendorBaseInfo.property eq 1}'>国企</c:if>
<c:if test='${vendorBaseInfo.property eq 2}'>独资</c:if>
<c:if test='${vendorBaseInfo.property eq 3}'>合资</c:if>
<c:if test='${vendorBaseInfo.property eq 4}'>民营</c:if>
</small></h5></td>
<td><h5 style="font-weight: 900;">注册日期:<small style="color: #000000;margin: 10PX"><fmt:formatDate value="${vendorBaseInfo.regtime}" pattern="yyyy-MM-dd"/></small></h5></td>
</tr>
<tr>
<td><h5 style="font-weight: 900;">邓白氏编码:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.duns }</small></h5></td>
<td><h5 style="font-weight: 900;">主要产品:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.mainProduct }</small></h5></td>
<td><h5 style="font-weight: 900;">供应商分类:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.vendorClassify }</small></h5></td>
<td><h5 style="font-weight: 900;">供应商等级:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.vendorLevel }</small></h5></td>
</tr>
<tr>
<td><h5 style="font-weight: 900;">近三年质量体系审核结果:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.qsa }</small></h5></td>
<td><h5 style="font-weight: 900;">近三年的评优结果:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.qsaResult }</small></h5></td>
<td> <h5 style="font-weight: 900;">供应商分类:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.vendorClassify2 }</small></h5></td>
<td></td>
</tr>
<tr id="yincang">
<td><h5 style="font-weight: 900;">企业法人:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.legalPerson }</small></h5></td>
<td><h5 style="font-weight: 900;">上年度销售收入（万元）:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.lastYearIncome }</small></h5></td>
<td> <h5 style="font-weight: 900;">股比构成:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.stockShare }</small></h5></td>
<td><h5 style="font-weight: 900;">网址:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.webAddr }</small></h5></td>
</tr>
<tr id="yincang2">
<td><h5 style="font-weight: 900;">产品线:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.productLine }</small></h5></td>
<td><h5 style="font-weight: 900;">企业所有权:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.ownership }</small></h5></td>
<td> <h5 style="font-weight: 900;">注册资本:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.regCapital }</small></h5></td>
<td><h5 style="font-weight: 900;">总资本:<small style="color: #000000;margin: 10PX">${vendorBaseInfo.totalCapital }</small></h5></td>
</tr>
</table>
<h1 class='page-header'><small style='color: #990000;'>调查表</small></h1>
<table id="tb" type="tb" class="table table-bordered table-condensed" >
		<thead  class="datagrid-header"><tr class="datagrid-cell">
		<th >名称</th>
		<th >状态</th>
		<th >版本</th>
		<th >提交人</th>
		<th >提交时间</th>
		<th >审核人</th>
		<th >审核时间</th>
		</tr></thead>
        <c:forEach items="${vendorSurveyCfgEntitys }" var="vendorSurveyCfgEntitys">
        <tbody>
        <tr fixed="true"> 
            <td>${vendorSurveyCfgEntitys.surveyName }</td>
            <td>
            <c:if test='${vendorSurveyCfgEntitys.vendorSurveyBaseEntitys.auditStatus eq -1}'>审核驳回</c:if>
			<c:if test='${vendorSurveyCfgEntitys.vendorSurveyBaseEntitys.auditStatus eq 0}'>待审核</c:if>
			<c:if test='${vendorSurveyCfgEntitys.vendorSurveyBaseEntitys.auditStatus eq 1}'>审核通过</c:if>
            </td>
            <td>${vendorSurveyCfgEntitys.vendorSurveyBaseEntitys.versionNO }</td>
            <td>${vendorSurveyCfgEntitys.vendorSurveyBaseEntitys.createUserName }</td>
            <td><fmt:formatDate value="${vendorSurveyCfgEntitys.vendorSurveyBaseEntitys.createTime }" type="date" dateStyle="medium"/></td>
            <td>${vendorSurveyCfgEntitys.auditUser }</td>
            <td>
            <c:if test="${vendorSurveyCfgEntitys.auditUser != null}"><fmt:formatDate value="${vendorSurveyCfgEntitys.vendorSurveyBaseEntitys.lastUpdateTime }" type="date" dateStyle="medium"/></td></c:if>
            <c:if test="${vendorSurveyCfgEntitys.auditUser == ''}"></c:if>
            
        </tr> 
        </tbody>  
       </c:forEach>   
</table>
<h1 class='page-header'><small style='color: #990000;'>报告表</small></h1>
<table id="tb2" type="tb" class="table table-bordered table-condensed" >
		<thead  class="datagrid-header"><tr class="datagrid-cell">
		<th >名称</th>
		<th >提交人</th>
		<th >提交时间</th>
		</tr></thead>
        <tbody id="strue">
         <c:forEach items="${vendorReportEntitys }" var="vendorReportEntitys">
        <tr> 
            <td><a href='${vendorReportEntitys.reportUrl}'>${vendorReportEntitys.reportName }</a></td>
            <td>${vendorReportEntitys.createUserName }</td>
            <td><fmt:formatDate value="${vendorReportEntitys.createTime }" type="date" dateStyle="medium"/></td>
        </tr> 
        </c:forEach>   
        </tbody>  
     
</table>
<h1 class='page-header'><small style='color: #990000;'>整改记录</small></h1>
<table id="tb3" type="tb" class="table table-bordered table-condensed" >
		<thead  class="datagrid-header"><tr class="datagrid-cell">
		<th>序号</th>
		<th>整改要求</th>
		<th>提出整改要求时间</th>
		<th>要求整改完成时间</th>
		<th>整改状态</th>
		<th>结案状态</th>
		<th>整改结案评论</th>
		</tr></thead>
        <c:forEach items="${vendorReportEntitys }" var="vendorReportEntitys">
        <tbody>
        <tr fixed="true"> 
        </tr> 
        </tbody>  
       </c:forEach>   
</table>
<!-- 
<h1 class='page-header'><small style='color: #990000;'>整改记录</small></h1>
<table id="tb4" type="tb" class="table table-bordered table-condensed" >
		<thead  class="datagrid-header"><tr class="datagrid-cell">
		<th class='span13'>序号</th>
		<th class='span3'>扩展信息1</th>
		<th class='span3'>扩展信息2</th>
		<th class='span3'>扩展信息3</th>
		<th class='span2'>.........</th>
		</tr></thead>
        <c:forEach items="${vendorReportEntitys }" var="vendorReportEntitys">
        <tbody>
        <tr fixed="true"> 
        </tr> 
        </tbody>  
       </c:forEach>   
</table>
 -->
<h1 class="page-header"></h1>
  <div class="btn-group"style="float: right;">
  <button type="button" class="btn btn-default" onclick="imp()">上传报告</button>
 </div>
</div>

<div id="win-materialSupplyRel-import3" class="easyui-dialog" title="批量维护质量体系审核结果和评优结果" style="width:400px;height:200px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true" >
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-materialSupplyRel-import3" method="post" enctype="multipart/form-data"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveImp2()">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialSupplyRel-import3').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
<div id="adsss" class="easyui-dialog" title="查看审核意见" style="width:500px;height:300px;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true">   
				 <table id="listssssss" title="供货关系详情列表" class="easyui-datagrid"
					data-options="fit:true,method:'post',singleSelect:false,
					pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
					>
						<thead><tr>
						<th data-options="field:'auditStatus'">审核状态</th>
						<th data-options="field:'auditUser'">审核人</th>
						<th data-options="field:'lastUpdateTime'">审核时间</th>
						<th data-options="field:'auditReason'">审核意见</th>
						</tr></thead>
				</table>
	 
</div> 
<div id="bdsss" class="easyui-dialog" title="查看供应商状态变更履历" style="width:500px;height:300px;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true">   
				 <table id="blistssssss" title="供应商状态变更履历" class="easyui-datagrid"
					data-options="fit:true,method:'post',singleSelect:false,
					pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
					>
						<thead><tr>
						<th data-options="field:'changeTypeText'">变更类型</th>
						<th data-options="field:'changeUser'">变更人</th>
						<th data-options="field:'changeTime'">变更时间</th>
						<th data-options="field:'changeReason'">变更意见</th>
						</tr></thead>
				</table>
	 
</div> 
<!-- survey(1);       1代表没有审核意见选项，2代表有审核意见选项 -->
<script type="text/javascript">
$(function(){
	$("#yincang").hide();
	$("#yincang2").hide();
});
function look()
{
	$("#yincang").toggle();
	$("#yincang2").toggle();
}
function shenhe(id,cfgid) {
	$('#adsss').window('open');
	$('#listssssss').datagrid('reload',ctx+'/manager/vendor/vendorInfor/examine/'+id+"/"+cfgid);    
};
function lookGKJJSS(orgId) {
	$('#bdsss').window('open');
	$('#blistssssss').datagrid('reload',ctx+'/manager/vendor/vendorInfor/lookGKJJSS/'+orgId);    
};
function imp(){
	$('#win-materialSupplyRel-import3').window({
		iconCls : 'icon-disk_upload',
		title : '上传报告'
	});
	$('#win-materialSupplyRel-import3').form('clear');
	$('#win-materialSupplyRel-import3').window('open');
};
function saveImp2() {
	$.messager.progress();
	$('#form-materialSupplyRel-import3').form('submit',{
		ajax:true,
		iframe: true,    
		url:ctx+'/manager/vendor/vendorInfor/filesUpload3/'+$("#vendorId").val(), 
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success:function(data){
			$.messager.progress('close');
			try{
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert('提示','成功','info');
					$('#win-materialSupplyRel-import3').window('close');
					$.post(ctx+"/manager/vendor/vendorInfor/getReport/"+$("#vendorId").val(),{},function(data){
						$("#strue").html(data);
					},"text");
				}else{
					$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href="+ctx+"'/common/download/"+result.log+"/日志文件'><b>日志文件</b></a>" ,'error');
				}
			}catch (e) {  
				$.messager.alert('提示',data,'error');
			}
		}
	});
}
</script>
</body>
</html>