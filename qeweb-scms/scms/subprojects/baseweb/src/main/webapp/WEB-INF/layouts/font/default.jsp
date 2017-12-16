<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<title><sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<link type="image/x-icon" href="${ctx}/static/base/images/favicon.ico" rel="shortcut icon">

<%-- <link href="${ctx}/static/base/styles/default.css" type="text/css" rel="stylesheet" /> --%>
<script src="${ctx}/static/base/jquery/jquery-1.9.1.min.js" type="text/javascript"></script>
<script src="${ctx}/static/base/jquery-validation/1.11.1/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx}/static/base/jquery-validation/1.11.1/messages_bs_zh2.js" type="text/javascript"></script>

<sitemesh:head/>
</head>

<body>

	
		<%@ include file="/WEB-INF/layouts/font/header.jsp"%>
		<div id="content">
			<sitemesh:body/>
		</div>
	<script src="${ctx}/static/base/bootstrap/2.3.2/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>