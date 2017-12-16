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
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorNav.js"></script>
</head>

<body style="margin:0;padding:0;">
<form id="setTemplatePhaseForm" action="${ctx}/manager/vendor/vendorNav/createTemplatePhase" method="post">
<input type="hidden" id="templateId" name="templateId" value="${vendorNavTemplate.id}"/>
</form>
<div class="easyui-layout" data-options="border:false" style="width: 100%;height: 400px;">
  <div data-options="region:'north',title:'二、指定阶段晋级路线',collapsible:false,border:false"></div>
  <div data-options="region:'west',border:true,title:'待选阶段'" style="width:45%;height:300px;collapsible:false">
      <div class="pull-right">
      <table id="selectingPhase" class="easyui-datagrid" data-options="url:'${ctx}/manager/vendor/vendorPhase/all',
      method:'post',singleSelect:false,showHeader:false,border:false,width:200">
        <thead>
          <tr>
            <th data-options="field:'id',checkbox:true"></th>
            <th data-options="field:'code'">阶段编码</th>
            <th data-options="field:'name',fixed:true">阶段名称</th>
          </tr>
        </thead>
      </table>
      </div>
  </div>
  <div data-options="region:'center',border:true,title:'操作',headerCls:'text-center',collapsible:false">
   <div class="center-block" style="width:16px;">
    <p><a class="easyui-linkbutton" data-options="iconCls:'icon-arrow_right'" href="javascript:;" onclick="selectPhase()"></a></p>
    <p><a class="easyui-linkbutton" data-options="iconCls:'icon-arrow_left'" href="javascript:;" onclick="unSelectPhase()"></a></p>
   </div> 
  </div>
  <div data-options="region:'east',title:'已选阶段',border:true" style="width:45%;">
    <div class="pull-left">
      <table id="selectedPhase" class="easyui-datagrid" data-options="idField:'id',
      method:'post',singleSelect:false,border:false,width:300,onClickCell: CellEditor.onClickCell">
        <thead>
          <tr>
            <th data-options="field:'id',checkbox:true"></th>
            <th data-options="field:'code'">阶段编码</th>
            <th data-options="field:'name',fixed:true">阶段名称</th>
            <th data-options="field:'phaseSn',editor:'numberbox'">晋级顺序</th>
          </tr>
        </thead>
      </table>
    </div>
  </div>
 <div data-options="region:'south',border:false,height:30">
   <div class="pull-right">
 	<a class="easyui-linkbutton" href="${ctx}/manager/vendor/vendorNav?id=${vendorNavTemplate.id}">上一步</a>
 	<a class="easyui-linkbutton" onclick="createTemplatePhase()">下一步</a>
   </div>
 </div>
</div>	
</body>
</html>