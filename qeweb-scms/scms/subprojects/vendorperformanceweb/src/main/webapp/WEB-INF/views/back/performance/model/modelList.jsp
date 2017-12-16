<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
<head>
	<title>绩效模版管理</title>
	<script type="text/javascript" src="${ctx}/static/script/performance/model.js"></script> 
</head>

<body style="margin:0;padding:0;">
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/performance/model',
    method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar : '#datagrid-tb'
  ">
    <thead>
      <tr>
        <th width="5%" data-options="field:'id',checkbox:true">ID</th>
        <th width="30%" data-options="field:'operate',formatter:Model.optFmt">操作</th>
        <th width="25%" data-options="field:'code'">编码</th>
        <th width="25%" data-options="field:'name'">名称</th>
        <th width="15%" data-options="field:'enableStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'enable',true);}">生效状态</th>
      </tr>
    </thead>
  </table>
  <div id="datagrid-tb">
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Model.add()">新增</a>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="Model.batchDelete()">删除</a>
  </div>
  
  <div class="easyui-dialog" id="dg" data-options="
    title:'新增',iconCls:'icon-add',
    modal:true,
    closed:true,
    buttons:[{
      text:'保存',
      iconCls:'icon-save',
      handler:function(){Model.submit();}
    }]
  ">
    <div itemId="ct">
      <form id="dg-form" class="baseform" method="post">
        <input type="hidden" itemId="id" name="id" value="0"/>
        <!-- 
        <div>
          <label>编号</label><input class="easyui-textbox" name="code" required="true"/>
        </div>     
         -->
        <div>
          <label>名称</label><input class="easyui-textbox" name="name" required="true"/>
        </div>     
        <div>
          <label>启用</label>
          <select class="easyui-combobox" name="enableStatus" itemId="enableStatus">   
		    <option value="1">启用</option>   
		    <option value="0">禁用</option>   
		  </select>    
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