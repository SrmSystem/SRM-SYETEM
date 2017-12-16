<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>数据导入</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforScoresList.js"></script>
</head>
<body>
    <table id="datagrid" class="easyui-datagrid-x" data-options=" 
    fit:true,border:false,
    url:'${ctx}/manager/vendor/vendorPerforScores',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#datagrid-tt'
    ">
    <thead>
      <tr>
        <th width="5%" data-options="field:'id',checkbox:true">评估ID</th>
        <th width="20%" data-options="field:'opt',formatter:VendorPerforDo.optFmt">操作</th>
        <th width="10%" data-options="field:'cycleName',formatter:function(v,r){if(r.cycleEntity) return r.cycleEntity.cycleName;}">周期	</th>
        <th width="10%" data-options="field:'modelName'">绩效模型</th>
        <th width="10%" data-options="field:'assessStartDate',formatter:VendorPerforDo.viewFmt">评估时间</th>
        <th width="10%" data-options="field:'vendorNumber'">供应商数量	</th>
        <th width="10%" data-options="field:'process',formatter:VendorPerforDo.processFmt">阶段</th>
        <th width="10%" data-options="field:'perforTemplatename',formatter:function(v,r){if(r.perforTemplate) return r.perforTemplate.name;}">引用模板（存档）</th>
        <th width="10%" data-options="field:'countStatus',align:'center',formatter:function(v){return StatusRender.render(v,'count',true);}">计算状态</th>
        <th width="5%" data-options="field:'logPath',formatter:VendorPerforDo.logFmt">日志</th>
      </tr>
    </thead>
  </table>
  <div id="datagrid-tt">
    <div>
      <div>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-setting',plain:true" href="javascript:;" onclick="VendorPerforDo.initList()">初始化</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-publish',plain:true" href="javascript:;" onclick="VendorPerforDo.confirmList()">确认</a>
      </div>
      <form id="form" method="post">
         <input itemId="id" name="id" type="hidden"/>
                           周期:<input id="ccb" class="easyui-combobox" name="search-EQ_cycleId"  style="width:150px;"/>
                           绩效模型:<input id="ccb" class="easyui-textbox" name="search-LIKE_modelName"  style="width:150px;"/>
      	   开始评估时间:<input class="easyui-datebox" name="search-GTE_assessStartDate" data-options="showSeconds:false" value="" style="width:150px">
      	   结束评估时间:<input class="easyui-datebox" name="search-LTE_assessStartDate" data-options="showSeconds:false" value="" style="width:150px">
      	 <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="VendorPerforDo.search()">查询</a>
      	 <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>
      </form>
    </div>
  </div>
<script type="text/javascript">
	$(function(){
		$('#datagrid').datagrid();
	});
</script>
</body>
</html>
