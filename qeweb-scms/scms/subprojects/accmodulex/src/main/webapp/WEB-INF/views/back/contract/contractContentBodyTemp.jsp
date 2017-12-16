<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>临时合同预览与打印</title>
</head>
<body>
<script src="${ctx}/static/base/jquery.jqprint/jquery-migrate-1.1.0.js"></script>
<script type="text/javascript" src="${ctx}/static/base/jquery.jqprint/jquery.jqprint-0.3.js"></script> 

<div id="print_div" style="width: 100%;height: 100%;">
<form id="form_print_body" action="" method="post" enctype='application/json'>
<div align="left" id="opt_div" style="display: block;">
	<div id="partDiv">
			<input id="partA" type="hidden" name="id"  value="${contractExt.id}"></input>
			<input id="partB" type="hidden" name="contractId"  value="${contract.id}"></input>
	 </div>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true" onclick="goPrint()">打印</a><!-- 打印 -->
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-book',plain:true" onclick="createPdfByHtml(${contractId},2)">生成pdf</a><!-- 生成pdf -->
</div>
<div>
<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<span style="font-family:黑体;line-height:125%;font-size:14pt;"></span>
</p>
<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<span style="font-family:黑体;line-height:125%;font-size:14pt;"></span>
</p>

<p class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:left;width: 90%;text-indent: 2em" >
	<b><span style="font-family:仿宋;line-height:125%;font-size:16pt;">甲方(需方):</span></b>
	<b>
		<u>
		<span style="font-family:仿宋;line-height:125%;font-size:16pt;" id="span_part1">
		<input id="part1" name="buyerName" value="${contract.buyer.name }" type="textField" onfocus="clearValue(this.value)" onkeyup="changeWidth(this.value,1)"
		style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 500px">
		</input>
		</span>
		</u>
	</b>
</p>

<p class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:left;width: 90%;text-indent: 2em">
	<b><span style="font-family:仿宋;line-height:125%;font-size:16pt;">乙方(供方):</span></b>
	<b><u>
		<span style="font-family:仿宋;line-height:125%;font-size:16pt;" id="span_part2">
		<input id="part2" name="vendorName" value="${contract.vendor.name }" type="textField" onfocus="clearValue(this.value)" onkeyup="changeWidth(this.value,2)"
		style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 500px">
		</input>
		</span>
	</u></b>
</p>

<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<span style="font-family:黑体;line-height:125%;font-size:14pt;"></span>
</p>

<p class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:left;width: 90%;text-indent: 4em">
	<span style="font-family:仿宋;line-height:125%;font-size:15pt;">
	为保护甲乙双方的合法权益,根据《中华人民共和国合同法》及有关法律规定,双方经友好协商，就乙方为甲方提供
	</span>
	<u>
		<span style="font-family:仿宋;line-height:125%;font-size:15pt;" id="span_part3" name="attr_1">
		<input id="part3" name="attr_1" value="${contractExt.attr_1}" type="text" style="width:1000px;font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
		</input>
		</span>
	</u>
</p>

<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<span style="font-family:黑体;line-height:125%;font-size:14pt;">&nbsp;&nbsp;</span>
</p>
<c:if test="${fn:length(contentList) > 0}">
	<c:set var="index" value="0" scope="request" />
	<c:set var="levelVal" value="0" scope="request" /> 
	<c:set var="contractContentList" value="${contentList}" scope="request"/>
	<c:forEach items="${contractContentList}" var="moduleItem" varStatus="itemIndex">
	   		<c:set var="index" value="${moduleItem.sqenum}" scope="request" />
			<p id="content_val_${moduleItem.sqenum}" class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:left;width: 90%;text-indent: 2em;">
				 <c:set var="levelVal" value="${moduleItem.sqenum}" scope="request" />
				<span id="val_${moduleItem.sqenum}" style="font-family:宋体;font-size:15.0000pt;">
				${moduleItem.sqenum}、${moduleItem.content}
				</span>
			</p>
			<c:choose>  
		       <c:when test="${fn:length(moduleItem.itemList) > 0}">  
		          <c:set var="childItemList" value="${moduleItem.itemList}" scope="request" />
		           <c:forEach items="${childItemList}" var="childItem" varStatus="childIndex">
							<p id="content_val_${index}${childItem.sqenum}" class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:left;width: 90%;text-indent: 2em;">
								<span style="font-family:宋体;font-size:15.0000pt;">${index}.${childItem.sqenum}、${childItem.content}</span>
							</p>
					</c:forEach>
			   </c:when>	      
		       <c:otherwise>  
		       </c:otherwise>   
			</c:choose>     
	</c:forEach>
</c:if>
<p class="MsoNormal">
	<span style="font-family:黑体;font-size:15pt;"></span><span style="font-family:黑体;font-size:15pt;"></span>
</p>
<div style="page-break-before: always;"></div><!-- 强制在元素后出现页分割符 -->
<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
	&nbsp;
</p>

<p class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:center;">
	<b><span style="font-family:黑体;line-height:125%;font-size:16pt;">双方账户信息</span></b>
	<b><span style="font-family:黑体;line-height:125%;font-size:16pt;"></span></b>
</p>
<table class="MsoTableGrid" style="border-collapse:collapse;width:90%;" align="center">
	<tbody>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">甲方增值税专用发票开具信息</span><span style="font-family:黑体;line-height:150%;font-size:15pt;"></span>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">乙方增值税专用发票开具信息</span><span style="font-family:黑体;line-height:150%;font-size:15pt;"></span>
				</p>
			</td>
		</tr>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">单位名称:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part4">
						<input id="part4" name="buyer" value="${contract.buyer.name }" type="textField" onfocus="clearValue(this.value)" onkeyup="changeWidth(this.value,4)"
						style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 500px">
						</input>
						</span>
					</u>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">单位名称:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part5">
						<input id="part5" name="vendor" value="${contract.vendor.name }" type="textField" onfocus="clearValue(this.value)" onkeyup="changeWidth(this.value,5)"
						style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 500px">
						</input>
						</span>
					</u>
				</p>
			</td>
		</tr>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">开户行:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part6" name="attr_2">
						<input id="part6" name="attr_2" value="${contractExt.attr_2}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">开户行:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part7"  name="attr_3">
						<input id="part7" name="attr_3" value="${contractExt.attr_3}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
		</tr>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">纳税人识别号:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part8"  name="attr_4">
						<input id="part8" name="attr_4" value="${contractExt.attr_4}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">纳税人识别号:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part9"  name="attr_5">
						<input id="part9" type="text" name="attr_5" value="${contractExt.attr_5}" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
		</tr>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">账号:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part10"  name="attr_6">
						<input id="part10" name="attr_6" value="${contractExt.attr_6}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">账号:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part11"  name="attr_7">
						<input id="part11" name="attr_7" value="${contractExt.attr_7}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
		</tr>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">地址:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part12"  name="attr_8">
						<input id="part12" name="attr_8" value="${contractExt.attr_8}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">地址:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part13"  name="attr_9">
						<input id="part13" name="attr_9" value="${contractExt.attr_9}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
		</tr>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">电话:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part14"  name="attr_10">
						<input id="part14" name="attr_10" value="${contractExt.attr_10}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">电话:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part15"  name="attr_11">
						<input id="part15" name="attr_11" value="${contractExt.attr_11}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
		</tr>
	</tbody>
</table>
<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
	&nbsp;
</p>
<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
	&nbsp;
</p>

<table class="MsoTableGrid" style="border-collapse:collapse;width:90%;" align="center">
	<tbody>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">甲方:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part16">
						</span>
					</u>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">乙方:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part17">
						</span>
					</u>
				</p>
			</td>
		</tr>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">委托代理人:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part18"  name="attr_12">
						<input id="part18" name="attr_12" value="${contractExt.attr_12}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">委托代理人:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part19"  name="attr_13">
						<input id="part19" name="attr_13" value="${contractExt.attr_13}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
		</tr>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">签订地点:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part20"  name="attr_14">
						<input id="part20" name="attr_14" value="${contractExt.attr_14}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">签订地点:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part21"  name="attr_15">
						<input id="part21" name="attr_15" value="${contractExt.attr_15}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
						</span>
					</u>
				</p>
			</td>
		</tr>
		<tr>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">签订时间:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;width: 50px" id="span_part22">
						<input id="part22" type="text" name="AYear" value="${year }" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 50px">
						</input>
						</span>
					</u>
					<span style="font-family:黑体;line-height:125%;font-size:15pt;">年</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;width: 50px" id="span_part23">
						<input id="part23" type="text" name="AMonth" value="${month }" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 50px">
						</input>
						</span>
					</u>
					<span style="font-family:黑体;line-height:125%;font-size:15pt;">月</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;width: 50px" id="span_part24">
						<input id="part24" type="text" name="ADay" value="${day }" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 50px">
						</input>
						</span>
					</u>
					<span style="font-family:黑体;line-height:125%;font-size:15pt;">日</span>
				</p>
			</td>
			<td width="284" valign="top">
				<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
					<span style="font-family:黑体;line-height:150%;font-size:15pt;">签订时间:</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part25">
						<input id="part25" name="BYear" value="${year }" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 50px">
						</input>
						</span>
					</u>
					<span style="font-family:黑体;line-height:125%;font-size:15pt;">年</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part26">
						<input id="part26" name="BMonth" value="${month }" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 50px">
						</input>
						</span>
					</u>
					<span style="font-family:黑体;line-height:125%;font-size:15pt;">月</span>
					<u>
						<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part27">
						<input id="part27" name="BDay" value="${day }" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 50px">
						</input>
						</span>
					</u>
					<span style="font-family:黑体;line-height:125%;font-size:15pt;">日</span>
				</p>
			</td>
		</tr>
	</tbody>
</table>
<div style="page-break-before: always;"></div><!-- 强制在元素后出现页分割符 -->

<p class="MsoNormal" style="margin-left:0.0000pt;text-indent:0.0000pt;">
	&nbsp;
</p>

<p class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:center;">
	<b><span style="font-family:黑体;line-height:125%;font-size:16pt;">购销商品清单和价格(此价格为含税含运费结算价格,单位：元)</span></b>
	<b><span style="font-family:黑体;line-height:125%;font-size:16pt;"></span></b>
</p>

<table class="MsoTableGrid" style="border-collapse:collapse;width:90%;" align="center">
	<tbody>
		<tr>
			<td width="47" valign="top" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">序号</span><span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
			<td width="75" valign="top" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">物料代码</span><span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
			<td width="74" valign="top" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">产品名称</span><span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
			<td width="77" valign="top" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">计量单位</span><span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
			<td width="66" valign="top" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">数量</span><span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
			<td width="61" valign="top" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">单价</span><span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
			<td width="65" valign="top" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">合计</span><span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
			<td width="100" valign="top" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">备注</span><span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
		</tr>
		<c:set var="index" value="0" scope="request" />
		<c:set var="allCount" value="0" scope="request" />
		<c:forEach items="${contentItemList}" var="contractItem" varStatus="itemIndex">  
		 	<c:set var="index" value="${index + 1}" scope="request" /><!-- 每一次循环，index+1 -->  
		 	<c:set var="allCount" value="${allCount + contractItem.totalPrice}" scope="request" />
		 	<tr>
				<td width="47" valign="top" style="border:1.0000pt solid windowtext;">
					<p class="MsoNormal">
						<span style="font-family:Calibri;font-size:15pt;">${index}</span><!-- 序号 -->
					</p>
				</td>
				<td width="75" valign="top" style="border:1.0000pt solid windowtext;">
					<p class="MsoNormal">
						<span style="font-family:Calibri;font-size:15pt;">${contractItem.materialCode}</span><!-- 物料代码 -->
					</p>
				</td>
				<td width="74" valign="top" style="border:1.0000pt solid windowtext;">
					<p class="MsoNormal">
						<span style="font-family:Calibri;font-size:15pt;">${contractItem.materialName}</span><!-- 产品名称 -->
					</p>
				</td>
				<td width="77" valign="top" style="border:1.0000pt solid windowtext;">
					<p class="MsoNormal">
						<span style="font-family:Calibri;font-size:15pt;">${contractItem.unitName}</span><!-- 计量单位 -->
					</p>
				</td>
				<td width="66" valign="top" style="border:1.0000pt solid windowtext;">
					<p class="MsoNormal">
						<span style="font-family:Calibri;font-size:15pt;">${contractItem.itemQty}</span><!-- 数量 -->
					</p>
				</td>
				<td width="61" valign="top" style="border:1.0000pt solid windowtext;">
					<p class="MsoNormal">
						<span style="font-family:Calibri;font-size:15pt;">${contractItem.taxPrice}</span><!-- 单价 -->
					</p>
				</td>
				<td width="65" valign="top" style="border:1.0000pt solid windowtext;">
					<p class="MsoNormal">
						<span style="font-family:Calibri;font-size:15pt;">${contractItem.totalPrice}</span><!-- 合计 -->
					</p>
				</td>
				<td width="100" valign="top" style="border:1.0000pt solid windowtext;">
					<p class="MsoNormal">
						<span style="font-family:Calibri;font-size:15pt;">${contractItem.remarks}</span><!-- 备注 -->
					</p>
				</td>
			</tr> 
		</c:forEach> 
		<tr>
			<td width="123" valign="top" colspan="2" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">合计金额:</span>
					<span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
			<td width="445" valign="top" colspan="6" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:Calibri;font-size:15pt;">${allCount}</span>
				</p>
			</td>
		</tr>
		<tr>
			<td width="123" valign="top" colspan="2" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15pt;">备注</span><span style="font-family:黑体;font-size:15pt;"></span>
				</p>
			</td>
			<td width="445" valign="top" colspan="6" style="border:1.0000pt solid windowtext;">
				<p class="MsoNormal">
					<span style="font-family:黑体;line-height:125%;font-size:15pt;" id="span_part28"  name="attr_16">
						<input id="part28" name="attr_16" value="${contractExt.attr_16}" type="text" style="font-family:仿宋;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 1000px">
						</input>
					</span>
				</p>
			</td>
		</tr>
	</tbody>
</table>
</div>
</form>
</div>

<script type="text/javascript">
$("body").css("overflow","auto");
var b = new Array();

//将阿拉伯数字转换成大写的汉字
function fmtNum(num){
	if(!/^\d*(\.\d*)?$/.test(num)){
		alert( "数字错误! ");   
		return   "数字错误! ";
	}         
	var   AA   =   new   Array( "","一", "二", "三", "四", "五", "六", "七", "八", "九");         
	var   BB   =   new   Array( "","十", "百", "千", "万", "亿", "点", "");                 
	var   a   =   ( ""+   num).replace(/(^0*)/g,"").split( "."),   k   =   0,   re   =   "";
	for(var i=a[0].length-1;i>=0;i--){ 
		switch(k){
		case   0   :   re   =   BB[7]   +   re;   break; 
		case   4   :   if(!new   RegExp( "0{4}\\d{ "+   (a[0].length-i-1)   + "}$ ").test(a[0])) re   =   BB[4]   +   re;   break;                         
		case   8   :   re   =   BB[5]   +   re;   BB[7]   =   BB[5];   k   =   0;   break;
		} 
		if(k%4 == 2 && a[0].charAt(i+2)!=0   &&   a[0].charAt(i+1)==0)   
			re   =   AA[0]   +   re; 
		if(a[0].charAt(i)   !=   0)   re   =   AA[a[0].charAt(i)]   +   BB[k%4]   +   re;   k++;
	}
    if(a.length> 1)   //加上小数部分(如果有小数部分) 
    {                 
    	re   =   re.substring(0,re.length-1); re   +=   BB[6];
           for(var   i=0;   i <a[1].length;   i++)
           	re   +=   AA[a[1].charAt(i)]; re   +=   BB[0];
    } 
    
    if((re.length==3 || re.length==2) && re.charAt(0)=="一"){
    	re = re.replace(/一/,"");
    }
    return   re;
}

//判断文字中是否包含"[]"将之间的文字添加下划线
function optStr(content){
	var str = content;
	var str1 = str.substring(0,str.indexOf("["));	//获取"["之前的文字
	var str2 = "<u>" + str.substring(str.indexOf("[")+1,str.indexOf("]")) +"</u>";	//获取"[]"中间的文字,包括"[]"
	var str3 = str.substring(str.indexOf("]")+1,str.length);		//获取"]"之后的文字
	return str1+str2+str3;
}

//打印
function goPrint(){
	var a =new Array();
	$("body").css("overflow","hidden");		//打印时去除页面的滚动条
	$('#opt_div').css("display","none");	//将页面的操作按钮隐藏
	var obj = $("input[id^='part']");
	for(var i=0;i<obj.length;i++){
		a[i] = obj[i].value;
		var spanId='#span_'+obj[i].id;
		$(spanId).text(obj[i].value)
	}
	
	$("#print_div").jqprint();				//执行打印操作
	
	$('#opt_div').css("display","block");	//显示操作按钮
	$("body").css("overflow","auto");		//添加滚动条
	for(var i=0;i<obj.length;i++){
		var inputId = '#' +obj[i].id;
		var spanId='#span_'+obj[i].id;
		$(spanId).html(b[i]);
		$(inputId).val(a[i]);
		
	}
}
/* 
function getMap() {//初始化map_,给map_对象增加方法，使map_像Map    
    var map_ = new Object();    
    map_.put = function(key, value) {    
        map_[key+'_'] = value;    
    };    
    map_.get = function(key) {    
        return map_[key+'_'];    
    };    
    map_.remove = function(key) {    
        delete map_[key+'_'];    
    };    
    map_.keyset = function() {    
        var ret = "";    
        for(var p in map_) {    
            if(typeof p == 'string' && p.substring(p.length-1) == "_") {    
                ret += ",";    
                ret += p.substring(0,p.length-1);    
            }    
        }    
        if(ret == "") {    
            return ret.split(",");    
        } else {    
            return ret.substring(1).split(",");    
        }    
    };    
    return map_;    
}     */

//生成pdf
function createPdfByHtml(moduleId,type){
	document.getElementById( "opt_div").style.display= "none";
	
	var a =new Array();
	var mapstr1 ="{";
	var mapstr2 ="}";
	var mapStr="";
	var obj = $("input[id^='part']");
	for(var i=0;i<obj.length;i++){
		mapStr =mapStr + ("'"+obj[i].name +"':'"+obj[i].value+"'");
		if(i<(obj.length-1)){
			mapStr += ",";
		}
		a[i] = obj[i].value;
		var spanId='#span_'+obj[i].id;
		$(spanId).text(obj[i].value)
	}
	var map =mapstr1+mapStr+mapstr2;
	var datas = JSON.stringify(map); 
	
	var divHtml = document.getElementById( "opt_div").innerHTML;
	document.getElementById( "opt_div").innerHTML="";
	var source=document.documentElement.outerHTML;
	$('#form_print_body').form('submit',{ 
		ajax:true,
		iframe: true,    
		url: ctx + '/manager/contract/contractContent/buildPDF/'+moduleId+'/'+type, 
		onSubmit:function(param){
			param.source = source;  
			param.mapSource = datas;
		},
		success:function(data){
			$.messager.progress('close');	
		}
	});
	
	document.getElementById( "opt_div").innerHTML=divHtml;
	document.getElementById( "opt_div").style.display= "block";
	for(var i=0;i<obj.length;i++){
		var inputId = '#' +obj[i].id;
		var spanId='#span_'+obj[i].id;
		$(spanId).html(b[i]);
		$(inputId).val(a[i]);	
	}
 }


$(function(){
	var val = "";
	var contentVal="";
	var obj2 = $("p[id^='content_val_']");
	var obj3 = $("span[id^='val_']");
	for(var j=0;j<obj2.length;j++){
		var pId =  '#'+obj2[j].id;
		contentVal = $(pId).html();
		var contentVal2=optStr(contentVal);
		$(pId).html(contentVal2);
	}
/*	for(var n=0;n<obj3.length;n++){
		var spanId ='#'+ obj3[n].id;
		//val = $(spanId).text();
		//$(spanId).text(fmtNum(val))
		val = $(spanId).html();
		$(spanId).html(fmtNum(val))
	}*/
	
	var obj = $("input[id^='part']");
	for(var i=0;i<obj.length;i++){
		var spanId='#span_'+obj[i].id;
		b[i] = $(spanId).html();
	}
 })
 
  function changeWidth(o, type) {
		var width1 = 500;
		var width2 = 800;
		var elementId = "part"+type;
		if (o.length * 20 >= width1 && o.length * 20 <= width2) {
			document.getElementById(elementId).style.width = o.length * 20
					+ "px";
		} else if (o.length * 20 < width1) {
			document.getElementById(elementId).style.width = width1 + "px";
		} else {
			document.getElementById(elementId).style.width = width2 + "px";
		}
	
	}
</script>
</body>
</html>