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
        <h3 class="search-title">维护质量体系审核/评优结果</h3>
            <div class="sea-div">
                <label class="search-lab" style="width: 120px">供应商编号:</label>
                <input id="code" type="text" class="hotel-name" readonly="readonly" style="background: #F4F4F4" value="${vendorBaseInfo.code }"/>
                <label class="search-lab">供应商名称:</label>
                <input id="name" type="text"  class="hotel-name" readonly="readonly" style="background: #F4F4F4" value="${vendorBaseInfo.name }"/>
            </div>
            <div class="sea-div">
                <label class="search-lab" style="width: 120px">近三年质量体系审核:</label>
                <input id="qsa" type="text"  class="hotel-name"  value="${vendorBaseInfo.qsa }"/>       
            </div>
            <div class="sea-div">
                <label class="search-lab" style="width: 120px">近三年评优结果:</label>
                 <input id="qsaResult" type="text"  class="hotel-name"  value="${vendorBaseInfo.qsaResult }"/>
            </div>
            <a href="javascript:;" class="easyui-linkbutton" style="width: 130px;
               display: block;float: left;margin: 50px 5px 0px -80px;cursor: pointer;" onclick="updateClass2()">保存</a>
            <a href="javascript:;" class="easyui-linkbutton" style="width: 130px;
               display: block;margin: 50px 0px 0px 5px;cursor: pointer;" onclick="resetClass()">重置</a>
    </div>
</div>
</div>
<script type="text/javascript">
function resetClass(){
	var qsa = $("#qsa");
	var qsaResult = $("#qsaResult");
	qsa.val("");
	qsaResult.val("");
}
function updateClass2()
{
	var code=$("#code").val();
	var qsa=$("#qsa").val();
	var qsaResult=$("#qsaResult").val();
	$.post(ctx+"/manager/vendor/vendorInfor/updateVendorQSA",{"code":code,"qsa":qsa,"qsaResult":qsaResult},function(data){
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