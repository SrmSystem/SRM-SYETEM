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
<title>合同预览与打印</title>
</head>
<body>
<script src="${ctx}/static/base/jquery.jqprint/jquery-migrate-1.1.0.js"></script>
<script type="text/javascript" src="${ctx}/static/base/jquery.jqprint/jquery.jqprint-0.3.js"></script> 
	
<div id="print_div" style="width: 100%;height: 100%;">
<form id="form_print_body" action="" method="post">
<div align="left" id="opt_div" style="display: block;">
	<div>
	<input id="partAA" type="hidden" name="id"  value="${contractExt.id}"/>
	<input id="partBB" type="hidden" name="contractId"  value="${contract.id}"/>
	
	 </div>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true" onclick="goPrint()">打印</a><!-- 打印 -->
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-book',plain:true" onclick="createPdfByHtml(${contractId},2)">生成pdf</a><!-- 生成pdf -->
</div>
<div>

<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<u><span style="font-family:黑体;line-height:125%;font-size:14.0000pt;" id="span_partX" name="attr_1">
		<input id="partX" name="attr_1" value="${contractExt.attr_1}" type="text" style="font-family:黑体;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
	</input></u>
	</span><span style="font-family:黑体;line-height:125%;font-size:14.0000pt;"></span>
</p>
<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<span style="font-family:黑体;line-height:125%;font-size:14.0000pt;">编号：</span>
	<u>
	<span style="font-family:黑体;line-height:125%;font-size:14pt;" id="span_partA">
	<input id="partA" name="contractCode" value="${contract.code }" type="text" style="font-family:黑体;line-height:125%;font-size:14pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
	</input>
	</span>
	</u>
</p>
<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<span style="font-family:黑体;line-height:125%;font-size:14pt;">&nbsp;&nbsp;</span>
</p>
<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<span style="font-family:黑体;line-height:125%;font-size:14pt;">&nbsp;&nbsp;</span>
</p>
<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<span style="font-family:黑体;line-height:125%;font-size:14pt;">&nbsp;&nbsp;</span>
</p>
<p class="MsoNormal" style="margin-left:0pt;text-indent:0pt;">
	<span style="font-family:黑体;line-height:125%;font-size:14pt;">&nbsp;&nbsp;</span>
</p>
<p class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:center;">
	<b><span style="font-family:仿宋;line-height:125%;font-size:24pt;">${contract.contractName }</span></b><b><span style="font-family:仿宋;line-height:125%;font-size:24pt;"></span></b>
</p>
<p class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:center;">
	<b><span style="font-family:仿宋;line-height:125%;font-size:26pt;"></span></b><b><span style="font-family:仿宋;line-height:125%;font-size:26pt;"></span></b>
</p>
<p class="MsoNormal" align="justify" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;">
	<b><span style="font-family:宋体;line-height:125%;font-size:10.5pt;">&nbsp;&nbsp;</span></b>
</p>
<p class="MsoNormal" align="justify" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;">
	<b><span style="font-family:宋体;line-height:125%;font-size:10.5pt;">&nbsp;&nbsp;</span></b>
</p>
<p class="MsoNormal" align="justify" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;">
	<b><span style="font-family:宋体;line-height:125%;font-size:10.5pt;">&nbsp;&nbsp;</span></b>
</p>
<p class="MsoNormal" align="justify" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;">
	<b><span style="font-family:宋体;line-height:125%;font-size:10.5pt;">&nbsp;&nbsp;</span></b>
</p>
<p class="MsoNormal" align="justify" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;">
	<b><span style="font-family:宋体;line-height:125%;font-size:10.5pt;">&nbsp;&nbsp;</span></b>
</p>
<p class="MsoNormal" align="justify" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;">
	<b><span style="font-family:宋体;line-height:125%;font-size:10.5pt;">&nbsp;&nbsp;</span></b>
</p>
<p class="MsoNormal" align="justify" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;">
	<b><span style="font-family:宋体;line-height:125%;font-size:10.5pt;">&nbsp;&nbsp;</span></b>
</p>

<table class="MsoTableGrid" style="border-collapse:collapse;text-align: left" align="center" >
	<tbody>
		<tr>
			<td width="35%" valign="top">
				<p class="MsoNormal" align="justify" style="text-align:justify;">
					<b><span style="font-family:宋体;line-height:125%;font-size:16pt;">&nbsp;</span></b>
				</p>
			</td>
			<td width="65%" valign="top">
				<p class="MsoNormal">
					<b><span style="font-family:楷体;line-height:125%;font-size:16pt;">甲 &nbsp;方：</span></b>
					<b><u>
					<span style="font-family:楷体;line-height:125%;font-size:16pt;" id="span_part1">
						<input id="part1" name="buyerName" value="${contract.buyer.name }" type="textField" onfocus="clearValue(this.value)" onkeyup="changeWidth(this.value,1)"
						style="font-family:楷体;line-height:125%;font-size:16pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 500px">
						</input>
					</span>
					</u></b>
				</p>
			</td>
		</tr>
		<tr>
			<td width="35%" valign="top">
				<p class="MsoNormal" align="justify" style="text-align:justify;">
					<b><span style="font-family:宋体;line-height:125%;font-size:16pt;">&nbsp;</span></b>
				</p>
			</td>
			<td width="65%" valign="top">
				<p class="MsoNormal">
					<b><span style="font-family:楷体;line-height:125%;font-size:16pt;">乙 &nbsp;方：</span></b>
					<b><u>
					<span style="font-family:楷体;line-height:125%;font-size:16pt;" id="span_part2">
						<input id="part2" name="vendorName" value="${contract.vendor.name }" type="textField" onfocus="clearValue(this.value)" onkeyup="changeWidth(this.value,2)"
						style="font-family:楷体;line-height:125%;font-size:16pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 500px">
						</input>
					</span>
					</u></b>
				</p>
			</td>
		</tr>
		<tr>
			<td width="35%" valign="top">
				<p class="MsoNormal" align="justify" style="text-align:justify;">
					<b><span style="font-family:宋体;line-height:125%;font-size:16pt;">&nbsp;</span></b>
				</p>
			</td>
			<td width="65%" valign="top">
				<p class="MsoNormal" align="justify" style="text-align:justify;">
					<b><span style="font-family:楷体;line-height:125%;font-size:16pt;">签订地点：</span></b>
					<b><span style="font-family:楷体;line-height:125%;font-size:16pt;" id="span_part3">
						<input id="part3" name="signAddress" value="${contract.signAddress }" type="text" style="font-family:楷体;line-height:125%;font-size:16pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 500px;">
						</input>
					</span></b>
				</p>
			</td>
		</tr>
		<tr>
			<td width="35%" valign="top">
				<p class="MsoNormal" align="justify" style="text-align:justify;">
					<b><span style="font-family:宋体;line-height:125%;font-size:16pt;">&nbsp;</span></b>
				</p>
			</td>
			<td width="65%" valign="top">
				<p class="MsoNormal">
					<b><span style="font-family:楷体;line-height:125%;font-size:16pt;">签订时间：</span></b>
					<span style="font-family:楷体;line-height:125%;font-size:16pt;" id="span_part4">
						<input id="part4" name="AYear" type="text" value="${year }" style="font-family:楷体;line-height:125%;font-size:16pt;width: 100px;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
					</span>
					<b><span style="font-family:楷体;line-height:125%;font-size:16pt;">年</span></b>
					<span style="font-family:楷体;line-height:125%;font-size:16pt;" id="span_part5">
						<input id="part5" name="AMonth" type="text" value="${month }" style="font-family:楷体;line-height:125%;font-size:16pt;width: 100px;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
					</span>
					<b><span style="font-family:楷体;line-height:125%;font-size:16pt;">月</span></b>
					<span style="font-family:楷体;line-height:125%;font-size:16pt;" id="span_part6">
						<input id="part6" name="ADay" type="text" value="${day }" style="font-family:楷体;line-height:125%;font-size:16pt;width: 100px;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
						</input>
					</span>
					<b><span style="font-family:楷体;line-height:125%;font-size:16pt;">日</span></b>
				</p>
			</td>
		</tr>
	</tbody>
</table>
<p class="MsoNormal" align="justify" style="margin-left:126.0000pt;text-indent:21.0000pt;text-align:justify;">
	<b><span style="font-family:楷体;line-height:125%;font-size:16pt;"></span></b><b><span style="font-family:楷体;line-height:125%;font-size:16pt;"></span></b>
</p>
<p class="MsoNormal" align="justify" style="margin-left:126.0000pt;text-indent:21.0000pt;text-align:justify;">
	<b><span style="font-family:楷体;line-height:125%;font-size:16pt;"></span></b>
</p>
<c:if test="${fn:length(contentList) > 0}">
<div style="page-break-before: always;"></div><!-- 强制在元素后出现页分割符 -->
		<c:set var="index" value="0" scope="request" />
		<c:set var="levelVal" value="0" scope="request" /> 
		<c:set var="contentItemList" value="${contentList}" scope="request"/>
		<c:forEach items="${contentItemList}" var="moduleItem" varStatus="itemIndex">
     		<c:set var="index" value="${moduleItem.sqenum}" scope="request" />
				<p id="content_val_${moduleItem.sqenum}"  class="MsoNormal" align="center" style="margin-left:0.0000pt;text-indent:0.0000pt;text-align:left;width: 90%;text-indent: 2em;">
					 <c:set var="levelVal" value="${moduleItem.sqenum}" scope="request" />
					<span id="val_${moduleItem.sqenum}" style="font-family:宋体;font-size:15.0000pt;">${moduleItem.sqenum}、
					${moduleItem.content}
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
<p class="MsoNormal">
	<span style="font-family:宋体;font-size:10.5000pt;">
</span><span style="font-family:宋体;font-size:10.5000pt;"></span>
</p>
</c:if>
<div style="page-break-before: always;"></div><!-- 强制在元素后出现页分割符 -->
<table  class="MsoTableGrid" style="border-collapse:collapse;width:90%;" align="center">
	<tbody>
		<tr>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">甲方：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partB">
					
					</span>
				</p>
			</td>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">乙方：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partC">
				
					</span>
				</p>
			</td>
		</tr>
		<tr>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">名称：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partD">
					<input id="partD" name="buyName" value="${contract.buyer.name }" type="textField" onfocus="clearValue(this.value)" onkeyup="changeWidth(this.value,D)"
					style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 500px">
					</input>
					</span>
				</p>
			</td>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">名称：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partE">
					<input id="partE" name="venName" value="${contract.vendor.name }" type="textField" onfocus="clearValue(this.value)" onkeyup="changeWidth(this.value,E)"
					style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 500px">
					</input>
					</span>
				</p>
			</td>
		</tr>
		<tr>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">地址：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partF" name="attr_2">
					<input id="partF" type="text" name="attr_2" value="${contractExt.attr_2}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">地址：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partG" name="attr_3">
					<input id="partG" type="text" name="attr_3" value="${contractExt.attr_3}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
		</tr>
		<tr>
			<td width="581" valign="top" colspan="2">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;"></span>
				</p>
			</td>
		</tr>
		<tr>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">法人代表(委托代理人)：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partH" name="attr_4">
					<input id="partH" type="text" name="attr_4" value="${contractExt.attr_4}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">法人代表(委托代理人)：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partI" name="attr_5">
					<input id="partI" type="text" name="attr_5" value="${contractExt.attr_5}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
		</tr>
		<tr>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">电话：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partJ" name="attr_6">
					<input id="partJ" type="text" name="attr_6" value="${contractExt.attr_6}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">电话：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partK" name="attr_7">
					<input id="partK" type="text" name="attr_7" value="${contractExt.attr_7}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
		</tr>
		<tr>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">传真：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partL" name="attr_8">
					<input id="partL" type="text" name="attr_8" value="${contractExt.attr_8}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">传真：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partM" name="attr_9">
					<input id="partM" type="text" name="attr_9" value="${contractExt.attr_9}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
		</tr>
		<tr>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">邮编：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partN" name="attr_10">
					<input id="partN" type="text" name="attr_10" value="${contractExt.attr_10}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
			<td width="290" valign="top">
				<p class="MsoNormal">
					<span style="font-family:黑体;font-size:15.0000pt;">邮编：</span><span style="font-family:黑体;font-size:15.0000pt;"></span>
					<span style="font-family:黑体;font-size:15.0000pt;" id="span_partO" name="attr_11">
					<input id="partO" type="text" name="attr_11" value="${contractExt.attr_11}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black">
					</input>
					</span>
				</p>
			</td>
		</tr>
	</tbody>
</table>
<p class="MsoNormal">
	<span style="font-family:宋体;font-size:10.5000pt;">
</span><span style="font-family:宋体;font-size:10.5000pt;"></span>
</p>
<p class="MsoNormal">
	<span style="font-family:宋体;font-size:10.5000pt;"></span>
</p>
<div style="page-break-before: always;"></div>
<p class="MsoNormal">
	<b><span style="font-family:仿宋;font-size:16pt;">附加条款：</span></b>
	<b><span style="font-family:仿宋;font-size:16pt;" id="span_partP" name="attr_12">
	<input id="partP" type="text" name="attr_12" value="${contractExt.attr_12}" style="font-family:黑体;font-size:15.0000pt;border-left: none;border-right: none;border-top: none;border-bottom-color: black;width: 1000px">
	</input>
	</span></b>
</p>
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
	/*for(var n=0;n<obj3.length;n++){
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