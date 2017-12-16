<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>供应商信息管理</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorInfor.js"></script>
</head>

<body>
  <table id="datagrid" class="easyui-datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/vendorInfor',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="100px" data-options="field:'id',checkbox:true">供应商代码</th>
        <th width="130px" data-options="field:'code'">供应商代码</th>
        <th width="200px" data-options="field:'name',formatter:BaseVendor.viewFmt">供应商名称</th>
        <th width="100px" data-options="field:'property',formatter:function(v,r,i){if(r.property==1) return '国有企业';else if(r.property==2) return'国有控股企业';else if(r.property==3) return '外资企业';else if(r.property==4) return '合资企业';else if(r.property==5) return '私营企业';else  return r.property;}">企业属性</th>
        <th width="100px" data-options="field:'countryText'">国家</th>
        <th width="100px" data-options="field:'provinceText'">省份</th>
        <th width="100px" data-options="field:'vendorPhase',formatter:function(v,r){if(r.vendorPhase) return r.vendorPhase.name; else ''}">阶段</th>
        <th width="100px" data-options="field:'vendorLevel'">供应商等级</th>
        <th width="100px" data-options="field:'vendorClassify'">供应商分类</th>
        <th width="100px" data-options="field:'vendorClassify2'">供应商分类<br/>&nbsp;&nbsp;(A,B,C)</th>
        <th width="100px" data-options="field:'qsa'">近三年质量体系<br/>审核结果</th>
        <th width="100px" data-options="field:'qsaResult'">近三年的评优<br/>结果</th>
        <th width="130px" data-options="field:'regtime'">注册时间</th>
        <th width="80px" data-options="field:'isControl',formatter:function(v,r,i){return v!=null&&v==0?'否':'是'}">是否统管</th>
        <th width="80px" data-options="field:'nop0',formatter:BaseVendor.vfmt0">供应商信息<br/>导出</th>
        <th width="80px" data-options="field:'nop',formatter:BaseVendor.vfmt">供货关系</th>
        <th width="100px" data-options="field:'nop1',formatter:BaseVendor.vfmt1">分类/等级</th>
        <th width="100px" data-options="field:'nop2',formatter:BaseVendor.vfmt2">近三年<br/>质量体系审核<br/>评优结果</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-imp',plain:true" onclick="BaseVendor.imp()">批量维护分类/等级</a>    
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-imp',plain:true" onclick="BaseVendor.imp2()">批量质量体系审核结果和评优结果</a><br/>  
      <a href="javascript:;" class="easyui-linkbutton" id="matRelExpBtn" data-options="iconCls:'icon-download',plain:true" onclick="expVendorSuCol1()">导出财务指标评价表</a>
      <a href="javascript:;" class="easyui-linkbutton" id="matRelExpBtn" data-options="iconCls:'icon-download',plain:true" onclick="expVendorSuCol2()">导出X年X季度供应商财务状况评估表</a>
      <a href="javascript:;" class="easyui-linkbutton" id="matRelExpBtn" data-options="iconCls:'icon-download',plain:true" onclick="expVendorSuCol3()">导出X年盈利情况每季度汇总</a>
      <a href="javascript:;" class="easyui-linkbutton" id="matRelExpBtn" data-options="iconCls:'icon-download',plain:true" onclick="expVendorSuCol4()">导出X年各个季度供应商现金流情况汇总</a> 
      <form id="form" method="post">
                             供应商代码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
                             可选省份:<input id="cddc" class="easyui-combobox" name="search-LIKE_provinceText"  style="width:80px;"/>
                             供应商名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:80px;"/>
                             供应商阶段:<input id="cc" class="easyui-combobox" name="search-LIKE_vendorPhase.name"  style="width:80px;"/>
                             供应商分类（A,B,C）:<input id="aa" class="easyui-combobox" name="search-LIKE_vendorClassify2"  style="width:80px;"/>
                             供应商等级:<input id="ff" class="easyui-combobox" name="search-LIKE_vendorLevel"  style="width:80px;"/>
                             供应商分类:<input id="bb" class="easyui-combobox" name="search-LIKE_vendorClassify"  style="width:80px;"/>
                             是否统管:<select id="search-EQ_isControl" name="search-EQ_isControl" class="easyui-combobox" style="width:80px;">
                             	<option value="">是/否</option>
                             	<option value="0">否</option>
                             	<option value="1">是</option>
                             </select>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="BaseVendor.search()">查询</a> 
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>       
      </form>
    </div>
  </div>
<form id="form-export">
</form>
<div id="win-materialSupplyRel" class="easyui-dialog" title="My Dialog" style="width:70%;height:70%;"   
        data-options="iconCls:'icon-setting',resizable:true,modal:true">   
	   <table id="datagrid-materialSupplyRel-list" class="easyui-datagrid"
					data-options="method:'post',singleSelect:true,
					pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
					>
						<thead><tr>
						<th width="100px" data-options="field:'materialName',formatter:function(v,r){if(r.material) return r.material.code; else ''}">物料图号</th>
						<th width="100px" data-options="field:'vendorName',formatter:function(v,r){if(r.material) return r.material.name; else ''}">物料名称</th>
						<th width="100px" data-options="field:'material',formatter:function(v,r){if(r.material) return r.material.partsCode; else ''}">所属车型</th>
						<th width="100px" data-options="field:'vendorId',formatter:function(v,r){if(r.material) return r.material.partsName; else ''}">所属零部件</th>
						<th width="100px" data-options="field:'dataFrom'">数据来源</th>
						<th width="100px" data-options="field:'nopos',formatter:BaseVendor.vfmt8">操作</th>
						</tr></thead>
				</table>   
				<div id="gonghuoxishu">
					<table id="tb" type="tb" class="table table-bordered table-condensed" >
							<thead  class="datagrid-header"><tr class="datagrid-cell">
								<th>业务范围</th>
								<th>业务类型</th>
								<th>品牌</th>
								<th>产品线</th>
								<th>工厂</th>
								<th>是否主供</th>
								<th>供货系数</th>
							</tr></thead>
					        <tbody id="fixedsid">
					        <tr> 
					        </tr>
					        </tbody>  
					</table>
				</div>
</div> 
<div id="dd" class="easyui-dialog" title="My Dialog" style="width:919px;height:456px;" data-options="iconCls:'icon-setting',resizable:true,modal:true">   
</div> 
<div id="addd" class="easyui-dialog" title="供应商" data-options="closed:true"style="width:95%;height:95%">   
</div> 
<div id="win-materialSupplyRel-import" class="easyui-window" title="批量维护分类/等级" style="width:400px;height:200px" data-options="iconCls:'icon-setting',modal:true,closed:true" >
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			
			<form id="form-materialSupplyRel-import" method="post" enctype="multipart/form-data"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
					<%-- 模板：<a href="${ctx}/templates/LevelClassify.xls">分类/等级.xls</a>   --%>  
					模板：<a href="javascript:;" onclick="File.download('WEB-INF/templates/LevelClassify.xls','LevelClassify')">分类/等级.xls</a>
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="BaseVendor.saveImp()">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialSupplyRel-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
<div id="expVendorSuCol1" class="easyui-window" title="导出" style="width:400px;height:250px" data-options="iconCls:'icon-setting',modal:true,closed:true" >
		<div class="easyui-panel" data-options="fit:true" style="padding:30px 70px 50px 70px">
			<form id="form-expVendorSuCol1-import" method="post"> 
				<div style="margin-bottom:20px">
					<input type="hidden" id="orgid" name="orgid" value=""/>
					<input type="hidden" id="orgids" name="orgids" value=""/>
					<input type="hidden" id="tyoe" name="tyoe" value=""/>
					年份：<input type="text" name="year" class="easyui-textbox" required="required"/><br> <br> 
					<div id="jidu">季度：<select name="month">
							<option value="1">第一季度</option>
							<option value="2">第二季度</option>
							<option value="3">第三季度</option>
							<option value="4">第四季度</option>
						  </select><br> 
				     </div>
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="expVendor()">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-expVendorSuCol1-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
</div>
<div id="win-materialSupplyRel-import2" class="easyui-window" title="批量维护质量体系审核结果和评优结果" style="width:400px;height:200px" data-options="iconCls:'icon-setting',modal:true,closed:true" >
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			
			<form id="form-materialSupplyRel-import2" method="post" enctype="multipart/form-data"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
					<%-- 模板：<a href="${ctx}/templates/qsaResult.xls">质量体系审核结果和评优结果.xls</a>    --%> 
					模板：<a href="javascript:;" onclick="File.download('WEB-INF/templates/qsaResult.xls','qsaResult')">质量体系审核结果和评优结果.xls</a>
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="BaseVendor.saveImp2()">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialSupplyRel-import2').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
<script type="text/javascript">
$(function(){
	$('#win-materialSupplyRel').window('close');
	$('#dd').window('close');
	$('#cc').combobox({    
    url:ctx+'/manager/vendor/vendorInfor/getVendorPhase',    
    valueField:'id',    
    textField:'text' ,
   	onSelect: function(rec){    
           $("input[name='search-LIKE_vendorPhase.name']").val(rec.text);   
      }
})
	$('#cddc').combobox({    
    url:ctx+'/manager/vendor/vendorInfor/getprovince',    
    valueField:'id',    
    textField:'text' ,
   	onSelect: function(rec){    
           $("input[name='search-LIKE_provinceText']").val(rec.text);   
      }
})
	$('#ff').combobox({    
    url:ctx+'/manager/vendor/vendorInfor/getVendorLevel',    
    valueField:'id',    
    textField:'text' ,
   	onSelect: function(rec){    
           $("input[name='search-LIKE_vendorLevel']").val(rec.text);   
      }
})
	$('#aa').combobox({    
    url:ctx+'/manager/vendor/vendorInfor/getVendorClassify2',    
    valueField:'id',    
    textField:'text' ,
   	onSelect: function(rec){    
           $("input[name='search-LIKE_vendorClassify2']").val(rec.text);   
      }
})
	$('#bb').combobox({    
    url:ctx+'/manager/vendor/vendorInfor/getVendorClassify',    
    valueField:'id',    
    textField:'text' ,
   	onSelect: function(rec){    
           $("input[name='search-LIKE_vendorClassify']").val(rec.text);   
      }
})
});  
function lookgonghuoxishu(id,vid)
{
	$.post(ctx+"/manager/vendor/vendorInfor/getLookgonghuoxishu/"+id+"/"+vid,{},function(data){
		$("#fixedsid").html(data);
	},"text");
}
</script>
</body>
</html>
