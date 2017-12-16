<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div id="cddck" class="easyui-layout" style="width:100%;height:100%;"> 
	<form id="form-vendorPerforIndex-Upadte" method="post" >	
			 <div data-options="region:'west'" style="width:30%;">
				<input id="id" name="id" type="hidden" value="${vi.id }"/>		
				<table style="text-align: left;padding:5px;float: left;" border="1" cellpadding="5">
				<tr>
					<td>指标名称:</td><td><input class="easyui-textbox" id="indexName" name="indexName" type="text"
						data-options="required:true" value="${vi.indexName }"
					/>
					</td>
				</tr>
				<tr>
					<td>指标描述:</td><td><input class="easyui-textbox" id="describe" name="describe" type="text"  value="${vi.describe }"/></td>
				</tr>
 				<tr>
					<td>维度:</td><td>
					<input id="dimensionsId" type="hidden" name="dimensionsId" value=""/>
						<input class="easyui-textbox" id="dimensionsNameU" name="dimensionsNameU"  data-options="required:true,editable:false" value="${vi.dimensionsEntity.dimName }"/>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="vendorPerforIndex.seer('U')">选择维度</a>
					</td>
				</tr>
				<tr>
					<td>一票否决（维度）:</td><td>
					<input type="radio" id="otvd" name="otvd" value="1"  
					<c:if test='${vi.otvd ==1}'>checked="checked"</c:if>
					/>是&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;           <input type="radio" id="otvd" name="otvd" value="0"
					<c:if test='${vi.otvd ==0}'>checked="checked"</c:if>
					/>否</td>
				</tr>
				<tr>
					<td>一票否决(供应商) :</td><td><input type="radio" id="otvv" name="otvv" value="1" 
					<c:if test='${vi.otvv ==1}'>checked="checked"</c:if>
					/>是&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;            <input type="radio" id="otvv" name="otvv" value="0"
					<c:if test='${vi.otvv ==0}'>checked="checked"</c:if>
					/>否</td>
				</tr>
				<tr>
					<td>类型 :</td>
					<td><select name="indexType" class="easyui-combobox" style="width:99%;" data-options="editable:false,required:true">
	                       	<option value="0"
	                       	<c:if test='${vi.indexType ==0}'>selected="selected"</c:if>
	                       	>手动</option>
	                       	<option value="1"
	                       	<c:if test='${vi.indexType ==1}'>selected="selected"</c:if>
	                       	>自动</option>
	                    </select> 
	                </td>
				</tr>
				<tr>
					<td>是否启用 :</td><td><input type="radio" id="abolished" name="abolished" value="1"
					<c:if test="${vi.abolished ==1}">checked="checked"</c:if>
					/>未启用&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;    <input type="radio" id="abolished" name="abolished" value="0" 
					<c:if test="${vi.abolished ==0}">checked="checked"</c:if>
					/>启用</td>
				</tr>
				</table>
				</div>
				 <div data-options="region:'east'" style="width:70%;">
				<table id="apku" style="text-align:center;padding:5px;float: left;margin-left: 5PX;width: 95%" border="1" cellpadding="10">
				<tr>
					<th style="text-align: left;" colspan="3">
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="vendorPerforIndex.addGSu()">新增公式</a>
					</th>
				</tr>
				<tr>
					<th style="text-align: center;width: 20%">公式周期<input type="hidden" id="numbers" value="0" /></th>
					<th style="text-align: center;width: 60%">公式设定<font style="color: #F00">*公式要素以“[&nbsp;]”（中号括）括起来</font></th>
					<th style="text-align: center;width: 20%">操作</th>
				</tr>
				<c:forEach items="${vfs}" var="vfs">
					<tr id="trid${vfs.id}" style="height: 200px"><td><input class="easyui-combobox" id="cycleId${vfs.id}" name="cycleId-${vfs.id}"  data-options="required:true,editable:false"/></td>
						<td><input class="easyui-textbox" id="content${vfs.id}" name="content${vfs.id}" type="text" style="width:100%;height: 100%" data-options="required:true,height:40,multiline:true" value="${vfs.content }"/></td>
						<td>
						<a id="editid${vfs.id }" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="vendorPerforIndex.edit('${vfs.id }')">导入元素</a>
						<a id="deltedg${vfs.id }" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete'" onclick="vendorPerforIndex.deleteGS('${vfs.id }')">删除</a>
						</td>
			           </tr>
			           <script type="text/javascript">
							$("#cycleId${vfs.id}").combobox({    
								url:ctx+'/manager/vendor/performance/index/getVendorPerforCycle',    
								valueField:'id',    
								textField:'text' ,
								onSelect: function(rec){    
									$("#cycleId${vfs.id}").val(rec.id);   
								},
								onLoadSuccess:function(){
									 $('#cycleId${vfs.id}').combobox('setValue',${vfs.cycleId});
								}
							});
						</script>
		          </c:forEach>
				</table>
				</div>
			</form>
		</div>
			<script type="text/javascript">
			  $(function(){
				  $("input[name='dimensionsId']").val(${vi.dimensionsId });   
			})
			</script>
</body>
</html>