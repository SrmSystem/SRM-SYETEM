<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>绩效分数查看页面</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforScores.js"></script>
</head>
<body>
<div id="layout" class="easyui-layout" fit="true">   
 <div data-options="region:'center'">
    <table id="datagrid" data-options="
    fit:true,border:false,
    url:'${ctx}/manager/vendor/vendorPerforScores/vendorList',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#datagrid-tt'
    ">
    <thead>
      <tr>
        <th width="20px" data-options="field:'id',checkbox:true"></th>
<!--         <th data-options="field:'opt',formatter:VendorPerforResult.optFmt">操作</th> -->
        <th width="40px" data-options="field:'cycle'">周期	</th>
        <th width="100px" data-options="field:'assessDate'">评估时间</th>
        <th width="160px" data-options="field:'orgCode'">供应商代码</th>
        <th width="160px" data-options="field:'orgName'">供应商名称</th>
        <th width="160px" data-options="field:'levelName'">等级</th>
        <th width="160px" data-options="field:'totalScore'">总得分</th>
        <th width="60px" data-options="field:'ranking'">排名</th>
        <th width="100px" data-options="field:'adjustStatus',formatter:function(v,r,i){return StatusRender.render(v,'adjust',true)},align:'center'">调分状态</th>
        <th width="100px" data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',true)},align:'center'">发布状态</th>
        <c:forEach items="${mappingScore}" var="mapping">
          <th width="100px" data-options="field:'${mapping.key}',formatter:function(v,r,i){return '<a href=javascript:; onclick=VendorPerforResult.dimView('+r.id+',\'${mapping.key}\',\'${mapping.value}\')>'+v+'</a>';}">${mapping.value}</th>
        </c:forEach>
      </tr>
    </thead>
  </table>
  <div id="datagrid-tt">
    <form id="form" method="post">
       <input itemId="id" name="id" type="hidden"/>
                        周期:<input id="ccb" class="easyui-combobox" name="search-EQ_cycleId"  style="width:150px;"/>
   	            评估时间:<input class="easyui-datebox" name="search-GTE_assessDate_DATE" data-options="showSeconds:false" value="" style="width:150px">
   	    ~<input class="easyui-datebox" name="search-LTE_assessDate_DATE" data-options="showSeconds:false" value="" style="width:150px">
   	 <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="VendorPerforResult.searchVendor11()">查询</a>
    </form>
  </div>
   </div>
</div> 
<!-- 调分窗口 -->
<div class="easyui-dialog" id="dialog-adjust" data-options="
width:460,
modal:true,title:'调分',iconCls:'icon-application',closed:true,
buttons:[{
  text:'提交',
  iconCls:'icon-save',
  handler:VendorPerforResult.adjustScoreSubmit
}]
">
  <div itemId="ct">
  <form itemId="form" class="baseform" method="post">
  <input type="hidden" name="scoresId" itemId="scoresId"/>
  <input type="hidden" name="id" itemId="id"/>
  <c:forEach items="${mappingScore}" var="mapping">
     <div>
       <label style="width:150px;">${mapping.value}</label>
       <input class="easyui-textbox mapping" type="text" required="true" itemId="${mapping.key}" name="${mapping.key}"/>
       <code>原得分：<span itemId="${mapping.key}-old"></span></code>
     </div>
  </c:forEach>
  <div>
    <label style="width:150px;">调分原因</label>
    <input class="easyui-textbox" data-options="multiline:true,required:true,height:50" name="adjustReason"/>
  </div>
  </form>
  </div>
</div>

<!-- 指标得分查看窗口 -->
<div class="easyui-dialog" id="dialog-indexScore" data-options="
width:460,
modal:true,title:'指标得分',iconCls:'icon-application',closed:true
">
    <table id="dialog-indexScore-dg" itemId="datagrid" class="easyui-datagrid" data-options="
    fit:true,
    toolbar:'#dialog-indexScore-datagrid-tt'
    ">
        <thead>
          <tr>
            <th data-options="field:'name'">名称</th>
            <th data-options="field:'score'">分数</th>
            <th data-options="field:'weight'">权重</th>
          </tr>
        </thead>
    </table>
    <div id="dialog-indexScore-datagrid-tt">
      <form itemId="form">
         <div>
         <label>维度名:</label><code itemId="dimName"></code>
         </div>
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
