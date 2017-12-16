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
	  //如果验证有问题
	  <c:if test="${error!=null}">
	  $.messager.alert('提示','$(error.msg)','error');
      </c:if>
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorNav.js"></script>
</head>

<body style="margin:0;padding:0;">
<script type="text/javascript">
  //如果验证有问题
  <c:if test="${error!=null}">
  $.messager.alert('提示','${error.msg}','error');
  </c:if>
</script>
<div class="easyui-panel" data-options="title:'一、模版创建'">
<div style="width:50%;margin: 0 auto;text-align: center;padding-bottom: 10px;">
  <form id="vendorNavTemplateForm" action="${ctx}/manager/vendor/vendorNav/createNavTemplate" method="post">
    <input type="hidden" name="id" value="<c:if test="${vendorNavTemplate==null}">-1</c:if><c:if test="${vendorNavTemplate!=null}">${vendorNavTemplate.id}</c:if>"/>
    <table cellpadding="5">
      <tr>
        <td>编号:</td>
        <td><input class="easyui-textbox" name="code" data-options="required:true" value="${vendorNavTemplate.code}"/></td>
      </tr>
      <tr>
        <td>名称:</td>
        <td><input class="easyui-textbox" name="name" data-options="required:true" value="${vendorNavTemplate.name}"/></td>
      </tr>
    </table>
  </form>
  <div style="width:256px;">
    <a href="javascript:;" class="easyui-linkbutton" onclick="createVendorNavTemplate()">下一步</a>
  </div>
</div>
</div>	
</body>
</html>
