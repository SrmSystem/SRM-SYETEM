<%@page import="com.qeweb.scm.contractmodule.entity.ContractContentEntity"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<%
	List<ContractContentEntity> list = (List<ContractContentEntity>)request.getAttribute("contentList");
	for(ContractContentEntity e:list){
		System.out.println(e.getContent());
		System.out.println(new String(e.getContent().getBytes("iso8859-1"), "UTF-8"));
		System.out.println(new String(e.getContent().getBytes("gbk"), "UTF-8"));
		System.out.println(new String(e.getContent().getBytes("gb2312"), "UTF-8"));
	}
	%>
<c:forEach items="${contentList}" var="moduleItem" varStatus="itemIndex">  
  <c:set var="index" value="${index + 1}" scope="request" /><!-- 每一次循环，index+1 -->  
  <tr><td valign="top" style="font-family:宋体;font-size:15.0000pt;">
	 <c:if test="${level eq 0}"> <!--level为0时表示为最上层  -->
		<p id="content_val_${index}">
			 <c:set var="levelVal" value="${moduleItem.sqenum}" scope="request" />
	  		<span id="val_${moduleItem.sqenum}">${moduleItem.sqenum}</span>、
	  		${moduleItem.content}
	  	</p>
	</c:if>
	<c:if test="${level ne 0}">
	  	<p id="content_val_${index}" style="text-indent: 2em">
	  		${levelVal}.${moduleItem.sqenum}、${moduleItem.content}
	  	</p>
	</c:if>
	  	
  </td></tr>
  <c:if test="${fn:length(moduleItem.itemList) > 0}">
    <c:set var="level" value="${level + 1}" scope="request" />
    <c:set var="moduleItemList" value="${moduleItem.itemList}" scope="request" />
    <c:import url="contractContentItem.jsp" /><!-- 递归-->  
  </c:if>  
</c:forEach> 
<c:set var="level" value="${level - 1}" scope="request" /><!-- 退出时，level-1 --> 