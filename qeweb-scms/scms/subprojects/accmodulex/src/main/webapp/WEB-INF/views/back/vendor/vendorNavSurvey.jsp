<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.qeweb.scm.vendormodule.constants.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<html>
<head>
	<title>供应商模版创建-分配调查表</title>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	  var Constants = {
		surTemType : <%=VendorModuleTypeConstant.getSurveyTemplateTypeJson()%>//调查表模版类型	  
	  };
	 </script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorNavSurvey.js"></script>
</head>

<body style="margin:0;padding:0;">
<div class="easyui-panel" data-options="title:'四、指定调查表'">
  <div>
    <div id="templatePhase">
      <table id="datagrid-templatePhase" class="easyui-datagrid"
      data-options="url:'${ctx}/manager/vendor/vendorTemplatePhase/getByTemplate',
      singleSelect:true,rownumbers:true,queryParams:{'templateId':${vendorNavTemplate.id}}
      "
      >
        <thead>
          <tr>
            <th data-options="field:'operate',formatter:VendorNavSurvey.selectSurveyFmt">选择调查表</th>
            <th data-options="field:'id',hidden:true"></th>
            <th data-options="field:'code'">编码</th>
            <th data-options="field:'name'">名称</th>
            <th data-options="field:'phaseSn'">晋级顺序</th>
          </tr>
        </thead>
      </table>
    </div>
  </div>  	
  <div class="pull-right" style="width:256px;">
    <a href="javascript:;" class="easyui-linkbutton" onclick="VendorNavSurvey.toPre()">上一步</a>
    <a href="javascript:;" class="easyui-linkbutton" onclick="VendorNavSurvey.createTemplateSurvey()">下一步</a>
  </div>

</div>	

<div class="easyui-window" id="window-phaseSurvey" data-options="title:'选择调查表',width:700,height:500,
  modal:true,closed:true
">
  <input type="hidden" id="templatePhaseId"/>
  <table id="datagrid-vendorSurveyTemplate-list" title="调查表模版列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/vendor/vendorSurveyTemplate/all',
		method:'post',singleSelect:false,idField:'id',
		checkOnSelect:false,
		onCheckAll:VendorNavSurvey.onCheckAll,
		toolbar:'#vendorSurveyTemplateListToolbar'"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'">调查表模版编号</th>
		<th data-options="field:'name'">调查表模版名称</th>
		<th data-options="field:'templateType',formatter:VendorNavSurvey.surTemTypeFmt">模版类型</th>
		<th data-options="field:'beanId'">bean</th>
		<th data-options="field:'path'">路径</th>
		</tr></thead>
  </table>
  <div id="vendorSurveyTemplateListToolbar">
    <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="VendorNavSurvey.selectSurvey()">选择</a>
  </div>
</div>


<form id="createNavTypeForm" action="${ctx}/manager/vendor/vendorNav/createNavType" method="post">
  <input type="hidden" itemId="templateId" name="id" value="${vendorNavTemplate.id}"/>
</form>

</body>
</html>
