<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<body>
  <div title="基本信息" class="container-fluid text-right">   
    <fieldset><legend class="text-left"><span class="label label-primary"><small>企业所有权信息</small></span></legend>
        <table style="width: 100%; font-size: 16px;">
          		<tr>
          			<th  width="13%">供应商名称:</th>
          			<td  width="20%" align="left">${vendor.name}</td>
          			<th  width="13%">供应商简称:</th>
          			<td  width="20%" align="left">${vendor.shortName}</td>
          			<th  width="14%">邓白氏编码:</th>
          			<td  width="20%" align="left">${vendor.duns}</td>
          		</tr>
          		<tr>
          			<th>成立时间:</th>
          			<td width="20%" align="left">${vendor.regtime}</td>
          			<th>企业法人:</th>
          			<td width="20%" align="left">${vendor.legalPerson}</td>
          			<th>上市公司:</th>
          			<td width="20%" align="left">${vendor.duns}</td>
          		</tr>
          		<tr>
          			<th>企业性质:</th>
          			<td width="20%" align="left"><c:if test='${vendor.property eq 1}'>国企</c:if>
		             	<c:if test='${vendor.property eq 2}'>独资</c:if>
		             	<c:if test='${vendor.property eq 3}'>合资</c:if>
		             	<c:if test='${vendor.property eq 4}'>民营</c:if>
		            </td>
          			<th>股比构成:</th>
          			<td width="20%" align="left">${vendor.stockShare}</td>
          			<th>网址:</th>
          			<td width="20%" align="left">${vendor.webAddr}</td>
          		</tr>
          		<tr>
          			<th>税务登记号:</th>
          			<td width="20%" align="left">${vendor.taxId}
		            </td>
          			<th>银行名称:</th>
          			<td width="20%" align="left">${vendor.bankName}</td>
          			<th>银行帐号:</th>
          			<td width="20%" align="left">${vendor.bankCard}</td>
          		</tr>

          		<tr>
          			<th>注册地址:</th>
          			<td width="20%" align="left">${vendor.address}
		            </td>
          			<th>主供事业部:</th>
          			<td width="20%" align="left"><c:forEach items="${vendorBU }" var="vendorBU">
            	<c:if test="${vendor.mainBU==vendorBU.codes}"> ${vendorBU.name }</c:if>
             </c:forEach>   </td>
          			<th>供应商类别:</th>
          			<td width="20%" align="left"><c:if test='${vendor.vendorType eq 1}'>代理供应商</c:if>
                <c:if test='${vendor.vendorType eq 2}'>生产供应商</c:if>
                <c:if test='${vendor.vendorType eq 3}'>服务供应商</c:if>
                <c:if test='${vendor.vendorType eq 4}'>经销供应商</c:if></td>
          		</tr>
          	</table>
          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>企业规模</small></span></legend>
          <table style="width: 100%; font-size: 16px;">
          		<tr>
          			<th  width="13%">注册资本(万元):</th>
          			<td  width="20%" align="left">${vendor.regCapital}</td>
          			<th  width="13%">资本总额(万元):</th>
          			<td  width="20%" align="left">${vendor.totalCapital}</td>
          			<th  width="14%">流动资金总额(万元):</th>
          			<td  width="20%" align="left">${vendor.workingCapital}</td>
          		</tr>
          		<tr>
          			<th  width="13%">占地面积(万平方米):</th>
          			<td  width="20%" align="left">${vendor.floorArea}</td>
          			<th  width="13%"></th>
          			<td  width="20%" align="left"></td>
          			<th  width="14%"></th>
          			<td  width="20%" align="left"></td>
          		</tr>
          </table>

          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>生产地址信息</small></span></legend>
          <table style="width: 100%; font-size: 16px;">
          		<tr>
          			<th  width="13%">所在省:</th>
          			<td  width="20%" align="left">${vendor.provinceText}</td>
          			<th  width="13%">所在市:</th>
          			<td  width="20%" align="left">${vendor.cityText}</td>
          			<th  width="14%"></th>
          			<td  width="20%" align="left"></td>
          		</tr>
          		<tr>
          			<th  width="13%">距离怀柔公里:</th>
          			<td  width="20%" align="left">${vendor.col1}</td>
          			<th  width="13%">距离密云公里:</th>
          			<td  width="20%" align="left">${vendor.col2}</td>
          			<th  width="14%">距离潍坊公里:</th>
          			<td  width="20%" align="left">${vendor.col3}</td>
          		</tr>
          		<tr>
          			<th  width="13%">距离诸城公里:</th>
          			<td  width="20%" align="left">${vendor.col4}</td>
          			<th  width="13%">距离长沙公里:</th>
          			<td  width="20%" align="left">${vendor.col5}</td>
          			<th  width="14%">距离南海公里:</th>
          			<td  width="20%" align="left">${vendor.col6}</td>
          		</tr>
          		<tr>
          			<th  width="13%">距离昌平公里:</th>
          			<td  width="20%" align="left">${vendor.col7}</td>
          			<th  width="13%"></th>
          			<td  width="20%" align="left"></td>
          			<th  width="14%"></th>
          			<td  width="20%" align="left"></td>
          		</tr>
          </table>

          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>联系人信息</small></span></legend>
            <table style="width: 100%; font-size: 16px;">
          		<tr>
          			<th  width="25%">总经理:</th>
          			<td  width="25%" align="left">${vendor.col8}</td>
          			<th  width="25%">联系电话:</th>
          			<td  width="25%" align="left">${vendor.col9}</td>
          		</tr>
          		<tr>
          			<th  width="25%">销售负责人:</th>
          			<td  width="25%" align="left">${vendor.col10}</td>
          			<th  width="25%">联系电话:</th>
          			<td  width="25%" align="left">${vendor.col11}</td>
          		</tr>
          		<tr>
          			<th  width="25%">质量负责人:</th>
          			<td  width="25%" align="left">${vendor.col12}</td>
          			<th  width="25%">联系电话:</th>
          			<td  width="25%" align="left">${vendor.col13}</td>
          		</tr>
          		<tr>
          			<th  width="25%">研发负责人:</th>
          			<td  width="25%" align="left">${vendor.col14}</td>
          			<th  width="25%">联系电话:</th>
          			<td  width="25%" align="left">${vendor.col15}</td>
          		</tr>
          </table>

          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>其他信息（新供应商可不用填写）</small></span></legend>
            <table style="width: 100%; font-size: 16px;">
          		<tr>
          			<th  width="25%">进入神农客公司年份:</th>
          			<td  width="25%" align="left">${vendor.col16}</td>
          			<th  width="25%">离开神农客公司年份:</th>
          			<td  width="25%" align="left">${vendor.col17}</td>
          		</tr>		
          </table>
          </fieldset>
    </div>



</body>
</html>
