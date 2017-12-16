<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>供应商模版创建</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/base/easyui/extension/datagrid-scrollview.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorNavType.js"></script>
</head>

<body style="margin:0;padding:0;">
<div class="easyui-panel" data-options="title:'三、设定模版范围'">
  <div>
    <label class="radio-inline"><input type="radio" value="1" checked="checked"  name="templateType"/>默认</label>
<!--     <label class="radio-inline"><input type="radio" value="2" onclick="changeTemplateType(this)" name="templateType"/>物料分类</label> -->
<!--     <label class="radio-inline"><input type="radio" value="3" onclick="changeTemplateType(this)" name="templateType"/>物料</label> -->
  </div>
  <div id="materialTypeDiv">
    <div>
      <table id="datagrid-materialType" class="easyui-datagrid" style="height:300px;width:700px"
       data-options="title:'物料分类列表',view:scrollview,rownumbers:true,singleSelect:false,queryParams:{page:1,rows:50},
				autoRowHeight:false,pageSize:50"
       > 
       
  	    <thead>
  	      <tr>
  	        <th data-options="field:'id',checkbox:true"></th>
  	        <th data-options="field:'code',width:60">编码</th>
  	        <th data-options="field:'name',width:70">名称</th>
  	      </tr>
  	    </thead>
  	  </table>
    </div>
    <div id="datagrid-selectedMaterialType">
    </div>
    <div>
      <table class="easyui-datagrid">
      </table>
    </div>
  </div>
  <div id="materialDiv">
    <div>
    <table id="datagrid-material" title="物料列表" class="easyui-datagrid" style="height:300px;width:700px;"
		data-options="view:scrollview,rownumbers:true,singleSelect:true,queryParams:{page:1,rows:50},
				autoRowHeight:false,pageSize:50"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'">物料图号</th>
		<th data-options="field:'name'">物料名称</th>
		<th data-options="field:'describe'">物料描述</th>
		<th data-options="field:'picStatus'">图纸状态</th>
		<th data-options="field:'technician'">设计者</th>
		<th data-options="field:'enableStatus'">物料状态</th>
		</tr></thead>
	</table>
    </div>
    <div></div>
  </div>
  <div class="pull-right">
    <a class="easyui-linkbutton" onclick="createNavPhase()">上一步</a>
    <a class="easyui-linkbutton" onclick="createNavType()">下一步</a>
  </div>
</div>
<form id="createNavTypeForm" action="${ctx}/manager/vendor/vendorNav/createNavType" method="post">
  <input type="hidden" itemId="id" name="id" value="${vendorNavTemplate.id}"/>
  <input type="hidden" itemId="rangeType" name="rangeType"/>
</form>
<!-- 上一步的跳转 -->
<form id="vendorNavTemplateForm" action="${ctx}/manager/vendor/vendorNav/createNavTemplate" method="post">
    <input type="hidden" name="id" value="${vendorNavTemplate.id}"/>
    <input type="hidden" name="code" value="${vendorNavTemplate.code}"/>
    <input type="hidden" name="name" value="${vendorNavTemplate.name}"/>
</form>
</body>
</html>
