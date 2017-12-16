<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	response.setStatus(200);
%>
<!DOCTYPE html>
<html>
<head>
<title>没有权限访问</title>
<style type="text/css">
.Absolute-Center {
  margin: auto;
  position: absolute;
  top: 0; left: 0; bottom: 0; right: 0;
}
</style>
</head>
<body>
	<div class="Absolute-Center" style="height: 30px; width: 300px;">  
	 <img src="${ctx }/static/style/image/promptImg.gif"><span>您没有权限进行此操作！</span>
	</div>
</body>
</html>