<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- <c:forEach items="${moduleItemList}" var="moduleItem" varStatus="itemIndex">  
  <c:set var="index" value="${index + 1}" scope="request" /><!-- 每一次循环，index+1 -->   --%>
 	<tr>
		<td width="47" valign="top" style="border:1.0000pt solid windowtext;">
			<p class="MsoNormal">
				<span style="font-family:Calibri;font-size:15pt;">&nbsp;</span>
			</p>
		</td>
		<td width="75" valign="top" style="border:1.0000pt solid windowtext;">
			<p class="MsoNormal">
				<span style="font-family:Calibri;font-size:15pt;">&nbsp;</span>
			</p>
		</td>
		<td width="74" valign="top" style="border:1.0000pt solid windowtext;">
			<p class="MsoNormal">
				<span style="font-family:Calibri;font-size:15pt;">&nbsp;</span>
			</p>
		</td>
		<td width="77" valign="top" style="border:1.0000pt solid windowtext;">
			<p class="MsoNormal">
				<span style="font-family:Calibri;font-size:15pt;">&nbsp;</span>
			</p>
		</td>
		<td width="66" valign="top" style="border:1.0000pt solid windowtext;">
			<p class="MsoNormal">
				<span style="font-family:Calibri;font-size:15pt;">&nbsp;</span>
			</p>
		</td>
		<td width="61" valign="top" style="border:1.0000pt solid windowtext;">
			<p class="MsoNormal">
				<span style="font-family:Calibri;font-size:15pt;">&nbsp;</span>
			</p>
		</td>
		<td width="65" valign="top" style="border:1.0000pt solid windowtext;">
			<p class="MsoNormal">
				<span style="font-family:Calibri;font-size:15pt;">&nbsp;</span>
			</p>
		</td>
		<td width="100" valign="top" style="border:1.0000pt solid windowtext;">
			<p class="MsoNormal">
				<span style="font-family:Calibri;font-size:15pt;">&nbsp;</span>
			</p>
		</td>
	</tr>
<%--   <c:if test="${fn:length(moduleItem.itemList) > 0}">
    <c:set var="level" value="${level + 1}" scope="request" />
    <c:set var="moduleItemList" value="${moduleItem.itemList}" scope="request" />
    <c:import url="contractReTemp.jsp" /><!-- 递归-->  
  </c:if>  
</c:forEach>  --%>
<c:set var="level" value="${level - 1}" scope="request" /><!-- 退出时，level-1 --> 