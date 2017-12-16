<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
	<title>评估模版列表</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/template.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,title:'模版设置',
    url:'${ctx}/manager/vendorperformance/template',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#datagrid-tt'
  ">
    <thead>
      <tr>
        <th width="5%" data-options="field:'id',checkbox:true">等级ID</th>
        <th width="20%" data-options="field:'operate',formatter:PerTemplate.optFmt">操作</th>
        <th width="20%" data-options="field:'code'">编码</th>
        <th width="20%" data-options="field:'name'">名称</th>
        <th width="15%" data-options="field:'cycle',formatter:function(v,r,i){return v.cycleName;}">周期</th>
        <th width="15%" data-options="field:'defaulted',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'yesOrNo',true);}">默认模版</th>
        <th width="5%" data-options="field:'describe'">描述</th>
      </tr>
    </thead>
  </table>
  <div id="datagrid-tt">
    <a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" href="javascript:;" onclick="PerTemplate.addNew('#dialog-adder')">新增</a>
    <a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-trash'" href="javascript:;" onclick="PerTemplate.abolish()">废除</a>
    <a class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'" href="javascript:;" onclick="PerTemplate.deleteList()">删除</a>
  </div>
  <!-- 模版新增窗口 -->
  <div class="easyui-dialog" data-options="title:'新增',iconCls:'icon-add',closed:true,modal:true,buttons:'#dialog-adder-bb'" id="dialog-adder">
    <div itemId="ct">
      <form method="post" id="template-form" itemId="dialog-adder-form" class="baseform">
        <div>
          <label>编码</label>
          <input class="easyui-textbox" data-options="required:true,prompt:'编码'" name="code"/>
        </div>
        <div>
          <label>名称</label>
          <input class="easyui-textbox" data-options="required:true,prompt:'名称'" name="name"/>
        </div>
        <div>
          <label>周期</label>
          <input class="easyui-combobox" data-options="
          url : '${ctx}/manager/vendor/performance/cycle/getAll',
          valueField:'id',
          textField:'cycleName',
          editable:false,
          required:true,prompt:'周期'" name="cycleId"/>
        </div>
        <div>
          <label>是否默认</label>
          <select class="easyui-combobox"  data-options="editable:false,icons:[{iconCls:'icon-information',disabled:true,title:'默认模版唯一，选择【是】将替换'}]" name="defaulted">
            <option value="0">否</option>
            <option value="1">是</option>
          </select>
          
        </div>
        <div>
          <label>权重模式</label>
          <select class="easyui-combobox" name="weight" data-options="editable:false">
            <option value="0">平均</option>
            <option value="1">手动</option>
          </select>
        </div>
        <div>
          <label>描述</label>
          <textarea name="describe"></textarea>
        </div>
      </form>
    </div>
    <div id="dialog-adder-bb">
      <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="PerTemplate.submit()" href="javascript:;">保存</a>
    </div>
  </div>
  <script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		});
	</script>
</body>
</html>
