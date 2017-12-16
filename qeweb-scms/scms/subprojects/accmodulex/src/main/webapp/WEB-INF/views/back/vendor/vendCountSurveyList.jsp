<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>供应商供货关系及系数统计</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendCountSurvey.js"></script>
</head>

<body>
  <table id="datagrid" class="easyui-datagrid" data-options="
    fit:true,title:'供应商供货关系及系数统计',
    url:'${ctx}/manager/vendor/vendCountSurvey',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th data-options="field:'code',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.code; else ''}">供应商代码</th>
        <th data-options="field:'name',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.name; else ''}">供应商名称</th>
        <th data-options="field:'shortName',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.shortName; else ''}">供应商简称</th>
        <th data-options="field:'duns',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.duns; else ''}">邓白氏编码</th>
        <th data-options="field:'property',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.property; else ''}">供应商性质</th>
        <th data-options="field:'ipo',formatter:function(v,r){if(r.vendorBaseInfoEntity.ipo==1) return '是';else return '否';}">是否为<br/>上市<br/>公司</th>
        <th data-options="field:'vendorPhase',formatter:function(v,r){if(r.vendorBaseInfoEntity.vendorPhase) return r.vendorBaseInfoEntity.vendorPhase.name; else ''}">供应商阶段</th>
        <th data-options="field:'vendorClassify2',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.vendorClassify2; else ''}">供应商<br/>分类<br/>（A,B,C）</th>
        <th data-options="field:'vendorLevel',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.vendorLevel; else ''}">供应商等级</th>
        <th data-options="field:'materialId',formatter:function(v,r){if(r.materialEntity) return r.materialEntity.code; else ''}">物料图号</th>
        <th data-options="field:'materialName',formatter:function(v,r){if(r.materialEntity) return r.materialEntity.name; else ''}">物料名称</th>
        <th data-options="field:'materialType1',formatter:function(v,r){if(v) return v.name; else ''}">所属系统</th>
        <th data-options="field:'brandName'">供货品牌</th>
        <th data-options="field:'factoryName'">供货工厂</th>
        <th data-options="field:'supplyCoefficient'">供货系数</th>
        <th data-options="field:'productLineName'">产品线</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
      <form id="form" method="post">
        <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="exp()">导出报表</a>
        <div>
        <table>
        	<tr>
        	 <!-- <td>系统名称:</td>
        	 <td><input class="easyui-textbox" name="special-matt1" type="text" style="width:80px;"/></td> -->
        	 <td>供应商代码:</td>
        	 <td><input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.code" type="text" style="width:80px;"/></td>
        	 <td>供应商阶段:</td>
        	 <td><input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.vendorPhase.name" type="text" style="width:80px;"/></td>
        	 <td>邓白氏编码:</td>
        	 <td><input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.duns" type="text" style="width:80px;"/></td>
        	 <!-- <td>分组名称:</td>
        	 <td><input class="easyui-textbox" name="special-matt2" type="text" style="width:80px;"/></td> -->
        	 <td>供应商名称:</td>
        	 <td><input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.name" type="text" style="width:80px;"/></td>
        	 <td>供应商简称:</td>
        	 <td><input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.shortName" type="text" style="width:80px;"/> </td>
        	</tr>
        	<tr>
        	 <td>供应商等级:</td>
        	 <td><input id="ff" class="easyui-combobox" name="search-LIKE_vendorBaseInfoEntity.vendorLevel"  style="width:80px;" data-options="editable:false"/></td>
        	 <td>子组名称:</td>
        	 <td><input class="easyui-textbox" name="special-matt3" type="text" style="width:80px;"/></td>
        	 <td>物料图号:</td>
        	 <td><input class="easyui-textbox" name="search-LIKE_materialEntity.code" type="text" style="width:80px;"/></td>
        	 <td>供应商状态:</td>
        	 <td><input id="v_approveStatus" class="easyui-combobox" name="search-EQ_vendorBaseInfoEntity.abolished" style="width:80px;" data-options="editable:false"/></td>
        	 <td>供应商性质:</td>
        	 <td><input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.property" type="text" style="width:80px;"/></td>
        	</tr>
        	<tr>
        	<td>供货品牌:</td>
        	<td><input class="easyui-textbox" name="search-LIKE_brandName" type="text" style="width:80px;"/></td>
        	<td>物料名称:</td>
        	<td><input class="easyui-textbox" name="search-LIKE_materialName" type="text" style="width:80px;"/></td>
        	<td>供应商分类:</td>
        	<td><input id="aa" class="easyui-combobox" name="search-LIKE_vendorBaseInfoEntity.vendorClassify2"  style="width:80px;" data-options="editable:false"/></td>
        	<td>产品线:</td>
        	<td><input class="easyui-textbox" name="search-LIKE_productLineName" type="text" style="width:80px;"/></td>
        	<td></td>
        	<td><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendCountSurvey.search()">查询</a></td>
        	<td></td>
        	<td></td>
        	</tr>
        </table>
        </div>
      </form>
    </div>
  </div>
  <script type="text/javascript">
$(function(){
	$('#ff').combobox({    
    url:ctx+'/manager/vendor/vendorInfor/getVendorLevel',    
    valueField:'id',    
    textField:'text' ,
   	onSelect: function(rec){    
           $("input[name='search-LIKE_vendorBaseInfoEntity.vendorLevel']").val(rec.text);   
      }
})
	$('#aa').combobox({    
    url:ctx+'/manager/vendor/vendorInfor/getVendorClassify2',    
    valueField:'id',    
    textField:'text' ,
   	onSelect: function(rec){    
           $("input[name='search-LIKE_vendorBaseInfoEntity.vendorClassify2']").val(rec.text);   
      }
})


$('#v_approveStatus').combobox({    
    url:ctx+'/manager/database/statusDict/getStatusByStatusTypeCombobox/abolish',    
    valueField:'id',    
    textField:'text' ,
   	onSelect: function(rec){  
           $("input[name='search-EQ_vendorBaseInfoEntity.abolished']").val(rec.id);   
      }
})
});  
</script>
</body>

</html>
