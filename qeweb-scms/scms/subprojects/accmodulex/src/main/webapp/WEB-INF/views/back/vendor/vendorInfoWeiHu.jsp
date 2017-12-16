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
</head>

<body class="ticketBody">
	 <link rel="stylesheet" type="text/css" href="${ctx}/static/styles/global.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/styles/css.css"/>
<!--中间-->
<div class="webIndex">
<div class="stay-list clearFloat" >
    <div class="stay-list-left"  style="width: 900px;height: 400px">
        <h3 class="search-title">维护分类/等级</h3>
            <div class="sea-div">
                <label class="search-lab">供应商编号:</label><input id="code" type="text" class="hotel-name" readonly="readonly" style="background: #F4F4F4" value="${vendorBaseInfo.code }"/>
                <label class="search-lab">供应商名称:</label><input id="name" type="text"  class="hotel-name" readonly="readonly" style="background: #F4F4F4" value="${vendorBaseInfo.name }"/>
            </div>
            <div class="sea-div">
                <label class="search-lab">供应商分类:</label>
                <select id="vendorClassify" name="vendorClassify"  style="width: 259px;height: 33px;  margin-left: 28px;float: left;" >
                <option value="">-全部-</option>
               
                <c:forEach items="${vendorClassify}" var="vendorClassify">
			       <option value="${vendorClassify }" 
			       <c:if test="${vendorClassify==vendorBaseInfo.vendorClassify }"> 
			         selected="selected"
			       </c:if>
			       >${vendorClassify }</option>
			     </c:forEach>
			     </select>
                <label class="search-lab">供应商等级:</label>
                <select id="vendorLevel" name="vendorLevel" style="width: 259px;height: 33px;  margin-left: 28px;float: left;" >
		          <option value="全部">全部</option>
		          	<c:forEach items="${vendorLevel}" var="vendorLevel">
				       <option value="${vendorLevel }" 
				       <c:if test="${vendorLevel==vendorBaseInfo.vendorLevel }"> 
				         selected="selected"
				       </c:if>
				       >${vendorLevel }</option>
				     </c:forEach>
		        </select>
                
            </div>
            <div class="sea-div">
                <label class="search-lab">供应商分类<br/>(A,B,C):</label>
                 <select id="vendorClassify2" style="width: 259px;height: 33px;  margin-left: 28px;float: left;" >
	                <option value="">-全部-</option>
	                <c:forEach items="${vendorClassify2}" var="vendorClassify2">
				       <option value="${vendorClassify2 }" 
				       <c:if test="${vendorClassify2==vendorBaseInfo.vendorClassify2 }"> 
				         selected="selected"
				       </c:if>
				       >${vendorClassify2 }</option>
				     </c:forEach>
			     </select>
            </div>
            <a href="javascript:;" class="easyui-linkbutton" style="width: 130px;
               display: block;float: left;margin: 50px 5px 0px -80px;cursor: pointer;" onclick="updateClass()">保存</a>
            <a href="javascript:;" class="easyui-linkbutton" style="width: 130px;
               display: block;margin: 50px 0px 0px 5px;cursor: pointer;" onclick="resetClass()">重置</a>
    </div>
</div>
</div>
<script type="text/javascript">
function resetClass(){
	var vc2 = $("#vendorClassify2");
	var vl = $("#vendorLevel");
	var vc = $("#vendorClassify");
	vc2[0][0].selected = true;
	vc[0][0].selected = true;
	vl[0][0].selected = true;
}
function updateClass()
{
	var code=$("#code").val();
	var vendorClassify=$("#vendorClassify").val();
	var vendorLevel=$("#vendorLevel").val();
	var vendorClassify2=$("#vendorClassify2").val();
	$.post(ctx+"/manager/vendor/vendorInfor/updateVendorClassify",{"code":code,"vendorClassify":vendorClassify,"vendorLevel":vendorLevel,"vendorClassify2":vendorClassify2},function(data){
		if(data=="1")
		{
			$('#dd').window('close');
			$.messager.alert('修改提示','修改成功','info');
			$('#datagrid').datagrid('reload');
		}
		else
		{
			$.messager.alert('修改提示','修改失败','error');
		}
	},"text");
}
</script>
</body>
</html>