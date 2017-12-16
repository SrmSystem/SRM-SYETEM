<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>字符映射设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/mapped.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/mapped',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="30px" data-options="field:'id',checkbox:true">字符映射ID</th>
        <th width="15%" data-options="field:'mappedName'">映射名称</th>
        <th width="15%" data-options="field:'name'">字符名称</th>
        <th width="15%" data-options="field:'mappedType',formatter:function(v,r,i){if(v==1)return 'SQL语句'; else  {if(v==2)return '数值'; else  {if(v==3) return '小于'; else  {if(v==4)return '小于或等于'; else  {if(v==5)return '大于'; else  {if(v==6)return '大于或等于'; else  {if(v==7)return '区间（包含上下区间）'; else  {if(v==8)return '区间（不包含上下区间）'; else  {if(v==9)return '区间（包含上，不包含下区间）'; else{if(v==10) return '区间（不包含上，包含下区间）';}}}}}}}}}}">映射类型</th>
        <th width="15%" data-options="field:'mappedValue'">映射值</th>
         <th width="15%" data-options="field:'describe'">字符映射描述</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="mapped.add()">新增</a>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-tab_edit',plain:true" onclick="mapped.update()">修改</a>
      <form id="form" method="post">
                            字符名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="mapped.search()">查询</a>  
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>      
      </form>
    </div>
  </div>
<form id="form-export">
</form>
<div id="dialog-mapped-saveUpadte" class="easyui-dialog" title="字符映射"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
			<form id="form-mapped-saveUpadte" method="post" >
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td> 映射名称:</td><td><input class="easyui-textbox" id="mappedName" name="mappedName" type="text"
						data-options="required:true" style="width: 100%"
					/>
					</td>
				</tr>
				<tr>
					<td> 映射类型:</td><td>
						<select class="easyui-combobox" id="mappedType" name="mappedType" data-options="required:true,editable:false">
							<!-- <option value="1">SQL语句</option> -->
							<option value="2">数值</option>
							<option value="3">小于</option>
							<option value="4">小于或等于</option>
							<option value="5">大于</option>
							<option value="6">大于或等于</option>
							<option value="7">区间（包含上下区间）</option>
							<option value="8">区间（不包含上下区间）</option>
							<option value="9">区间（包含上，不包含下区间）</option>
							<option value="10">区间（不包含上，包含下区间）</option>
						</select>
					</td>
				</tr>
				<tr>
					<td> 字符名称:</td><td><input class="easyui-textbox" id="name" name="name" type="text"
						data-options="required:true" style="width: 100%"
					/>
					</td>
				</tr>
				<tr>
					<td>字符描述:</td><td><input class="easyui-textbox" id="describe" name="describe" type="text"  style="width: 100%"/></td>
				</tr>
				<tr>
					<td>映射值:</td><td><input class="easyui-textbox" id="mappedValue" name="mappedValue" type="text"
					data-options="required:true"  style="width: 100%"
					/></td>
				</tr>
				</table>
				<div id="dialog-adder-bb">
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="mapped.submit()">提交</a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-mapped-saveUpadte').form('reset')">重置</a>
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
