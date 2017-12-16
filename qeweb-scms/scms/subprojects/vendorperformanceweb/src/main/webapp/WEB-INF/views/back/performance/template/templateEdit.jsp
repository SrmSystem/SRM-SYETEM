<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
	<title>评估模版编辑</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/template.js"></script>
</head>
<body>
<div class="easyui-panel" data-options="fit:true,title:'${template.name}-设置',iconCls:'icon-page',footer:'#tt'">
<div class="easyui-layout" fit="true" border="false">
  <div class="easyui-panel" data-options="region:'west',width:300,title:'基本信息',iconCls:'icon-information'">
    <form class="baseform" id="form-template">
      <input type="hidden" name="id" value="${template.id}"/>
      <div><label>编号</label><code><ins>${template.code}</ins></code></div>
      <div><label>名称</label><input class="easyui-textbox" data-options="required:true" name="name" value="${template.name}"/></div>
      <div>
          <label>周期</label>
          <input class="easyui-combobox" data-options="
          url : '${ctx}/manager/vendor/performance/cycle/getAll',
          valueField:'id',
          textField:'cycleName',
          editable:false,
          required:true,prompt:'周期'" name="cycleId" value="${template.cycleId}"/>
        </div>
        <div>
          <label>是否默认</label>
          <select class="easyui-combobox"  data-options="value:'${template.defaulted}',editable:false,icons:[{iconCls:'icon-information-import',disabled:true,title:'默认模版唯一，选择【是】将替换'}]" name="defaulted">
            <option value="0">否</option>
            <option value="1">是</option>
          </select>
        </div>
        <div>
          <label>权重模式</label>
          <select class="easyui-combobox" name="weight" itemId="weight" data-options="value:'${template.weight}',editable:false,onSelect:PerTemplate.weightSelect">
            <option value="0">平均</option>
            <option value="1">手动</option>
            <option value="2">不权重</option>
            <option value="3">自定义权重</option>
          </select>
         </div>
         <div>
            <label>描述</label>
            <textarea name="describe">${template.describe}</textarea>
          </div>
      </form>
  </div>
  <div class="easyui-panel" data-options="region:'center',border:false">
    <table id="treegrid-setting" data-options="
    url : '${ctx}/manager/vendorperformance/template/getSetting',
    queryParams : {'templateId':'${template.id}'},
    title:'参数设置',iconCls:'icon-setting',fit:true,
    idField:'id',treeField:'name',
    onBeforeEdit:PerTemplate.setRowBeforEdit,
	onAfterEdit:PerTemplate.setRowAfterEdit,
	onCancelEdit:PerTemplate.setRowCancelEdit
    ">
      <thead>
        <th width="30px" data-options="field:'id',hidden:true"></th>
        <th width="100px" data-options="field:'enableStatus',hidden:true"></th>
        <th width="100px" data-options="field:'operate',width:90,align:'center',formatter:PerTemplate.setEditorFmt">操作</th>
        <th width="150px" data-options="field:'name'">参数名</th>
        <th width="50px" data-options="field:'sourceType',formatter:PerTemplate.sourceTypeFmt">类型</th>
        <th width="80px" data-options="field:'weightNumber',editor:{type:'numberbox',options:{max:100}},width:80">权重值</th>
        <th width="60px" data-options="field:'enableStatusIcon',align:'center',formatter:function(v,r,i){return StatusRender.render(r.enableStatus,'enable',true);}">启用状态</th>
        <th width="80px" data-options="field:'remark',editor:'textbox',width:120">备注</th>
      </thead>
    </table>
  <div>
  </div>


</div>
<div id="tt" style="text-align: right;">
   <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="PerTemplate.updateSubmit()">保存</a>
   <a class="easyui-linkbutton" data-options="iconCls:'icon-back'" href="${ctx}/manager/vendorperformance/template">返回</a>
</div>
</div>

<!-- 维度的设置对话框 -->
<div class="easyui-dialog" id="dialog-dim" data-options="
  iconCls:'icon-page',
  title:'模版维度设置',
  modal:true,
  closed:true,
  width:600,
  height:300
">
  <div class="easyui-layout" fit="true">
    <div class="easyui-panel" data-options="region:'west',width:300,title:'维度设置'">
      <form class="baseform">
        <div>
          <label></label>
        </div>
      </form>
    </div>
    <div class="easyui-panel" data-options="region:'center',title:'指标设置'"></div>
  </div>

  
</div>
<script type="text/javascript">
	$(function(){
		$('#treegrid-setting').treegrid();
	});
</script>
</body>
</html>
