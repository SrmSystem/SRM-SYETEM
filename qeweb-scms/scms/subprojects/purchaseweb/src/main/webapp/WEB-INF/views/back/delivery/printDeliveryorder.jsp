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
<title>发货单的打印</title>
<style>
	.infoF {
    }
    .infoF td{
        padding-top: 10px;
    }
    .infoF th {
        padding-top: 10px;
        text-align: right;
        font-size: 12px;
    }
    .infoF td {
        border-bottom: 1px solid #888;
        height: 20px;
        font-size: 12px;
    }
    
     .infoT {
        border-top:1px solid #888;
        margin-top: 10px;
    }
    .infoT th{
        text-align: center; 
        height: 25px;
        font-size: 12px;
        background-color: #888;
    }
    .infoT td{
        text-align: left; 
        height: 20px;
        font-size: 12px;
        border-bottom: 1px solid #888;
    }
</style>
</head>
<body>
<div style="width: 100%;height: 100%;">
<div align="left">
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true" onclick="goPrint()">打印</a>
</div>
<div>
<table width="100%" style="margin-top: 1px; border-bottom: 5px #444 dashed; ">
    <tr>
        <td width="200" style="text-align: center; vertical-align: top;">
        	<img src="${ctx }/barcode?code=${delivery_Info.deliveryCode}&type=code39">
        </td>  
        <td width="200" align="center">
            <h1 align="center">发货单</h1>
        </td>
        <td width="200" nowrap align="left" style="font: 12px tahoma,arial,helvetica,sans-serif;color:#444; vertical-align: top;">
            <div style="border: 0px solid #444;">
                <span style="font-weight: bold;">预览时间：</span><fmt:formatDate value="<%=new java.util.Date()%>" pattern="MM/dd HH:mm:ss"/>
                <br/>
                <span style="font-weight: bold;">打印时间：</span><span id="ptime"></span>
                <br/>
                <span style="font-weight: bold;">签字：</span>
            </div>
        </td>
    </tr>
</table>
</div>
</br>
<div id="deliveryList" title="发货单详情" style="weight:80%;height:'500px';margin-left: auto;margin-right: auto;">
	<div class="easyui-panel">
		<div>
		<form id="form-deliveryitem-info">
			<table class="infoF" width="100%" align="left">
			<tr>
			<th>发货单编号:</th>
    		<td>${delivery_Info.deliveryCode}</td>
    		<th>发货日期：</th>
			<td>${delivery_Info.deliveyTime}</td>
			<th>发货人：</th>
			<td>${delivery_Info.deliveryUser.name}</td>
			</tr>
			<tr>
			<th>供应商名称：</th>
			<td>${delivery_Info.vendor.name}</td>
			<th>采购组织名称：</th>
			<td>${delivery_Info.buyer.name}</td>
			<th>收货组织名称：</th>
			<td>${delivery_Info.receiveOrg}</td>
			</tr>
			</table>
			</br>
			</br>
			<table width="100%" class="infoT" border="1px">
        <tr >
            <th>序号</th>           
            <th>发货单号</th>
            <th>采购单号</th>
            <th>物料代码</th>
            <th>物料名称</th>
            <th>发货数量</th>
            <th>收货数量</th>
        </tr>
        <c:forEach var="bo" items="${list}" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td>${bo.delivery.deliveryCode }</td>
                <td>${bo.orderItem.order.orderCode }</td>
                <td>${bo.material.code}</td>
                <td>${bo.material.name}</td>
                <td>${bo.deliveryQty}</td>
                <td>${bo.receiveQty}</td>               
                </tr>
        </c:forEach>
    </table>
		</form>  
		</div>
	</div>
</div>
</br>
</div>


<script type="text/javascript">

//发货单的列表中的信息
function showDeliveryInfo(id){
	$('#form-deliveryitem-info').form('clear');
	$('#form-deliveryitem-info').form('load','${ctx}/manager/order/printdelivery/getDelivery/'+id);
	//详情
	$('#datagrid-deliveryiteminfo-list').datagrid({   
    	url:'${ctx}/manager/order/printdelivery/deliveryitem/' + id     
	});
	$('#datagrid-deliveryiteminfo-list').datagrid('load',{});
}

function goPrint() {
	   var t = new Date().toLocaleString();
	   document.getElementById("ptime").innerHTML = t;
	    window.print();
	} 
</script>
</body>
</html>