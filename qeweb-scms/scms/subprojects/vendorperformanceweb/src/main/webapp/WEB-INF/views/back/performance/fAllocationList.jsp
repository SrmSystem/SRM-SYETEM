<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>公式元素设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/fAllocation.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,url:'${ctx}/manager/vendor/fAllocation',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="50px" data-options="field:'id',checkbox:true">公式元素ID</th>
        <th width="100px" data-options="field:'nop',formatter:fAllocation.vfmt">操作</th>
        <th width="200px" data-options="field:'name'">公式元素名称</th>
        <th width="120px" data-options="field:'fallValue'">公式元素值</th>
        <th width="200px" data-options="field:'describe'">公式元素描述</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="fAllocation.add()">新增</a>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="fAllocation.dels()">删除</a>
      <form id="form" method="post">
                            公式元素名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="fAllocation.search()">查询</a>
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>      
      </form>
    </div>
  </div>
<form id="form-export">
</form>
<div id="dialog-fAllocation-saveUpadte" title="公式元素"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
			<form id="form-fAllocation-saveUpadte" method="post" >
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td> 公式元素名称:</td><td><input class="easyui-textbox" id="name" name="name" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>公式元素描述:</td><td><input class="easyui-textbox" id="describe" name="describe" type="text"/></td>
				</tr>
				<tr>
					<td>公式元素值:</td><td><input class="easyui-textbox" id="fallValue" name="fallValue" type="text"
					data-options="required:true"
					/></td>
				</tr>
				</table>
				<div id="dialog-adder-bb">
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="fAllocation.submit()">提交</a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-fAllocation-saveUpadte').form('reset')">重置</a>
				</div>
			</form>
	</div>
<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		});
</script>
</body>
</html>
