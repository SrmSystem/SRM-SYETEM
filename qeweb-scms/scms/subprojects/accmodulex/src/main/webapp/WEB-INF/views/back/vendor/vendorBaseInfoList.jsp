<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>供应商准入创建</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorDataBase.js"></script>
</head>

<body>
<div title=""  style="padding:10px;height:100%;bottom: 0;">
  <table id="datagrid-${phase.id}" class="easyui-datagrid" data-options="
    url:'${ctx}/manager/vendor/vendorBaseInfo',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true">供应商代码</th>
        <th data-options="field:'org',formatter:BaseVendor.codeFmt">供应商代码</th>
        <th data-options="field:'shortName'">企业名称</th>
        <th data-options="field:'property'">企业属性</th>
        <th data-options="field:'country'">国家</th>
        <th data-options="field:'province'">省份</th>
        <th data-options="field:'regtime'">注册时间</th>
        <th data-options="field:'vendorPhase',formatter:BaseVendor.phaseFmt">所属阶段</th>
        <th data-options="field:'surveySubmitInfo',align:'center',styler:BaseVendor.cellRender.survey">调查表提交</th>
        <th data-options="field:'surveyAuditInfo',align:'center',styler:BaseVendor.cellRender.survey,formatter:BaseVendor.auditSurveyFmt">调查表审核</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
      <form id="form">
                             供应商代码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
                             供应商名称:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
                             供应商阶段:<input class="easyui-textbox" name="search-EQ_vendorPhase.id" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchVendorPhase()">查询</a>          
      </form>
    </div>
  </div>
</div>

<div id="window-audit" class="easyui-window" data-options="modal:true,closed:true,title:'调查表审核'">
</div>
</body>
</html>
