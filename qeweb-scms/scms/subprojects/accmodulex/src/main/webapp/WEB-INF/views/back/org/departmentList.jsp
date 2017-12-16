<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.org.departmentManagement"/><!-- 部门管理 --></title>
	<script type="text/javascript">
		function treeFmt(node){
			return node.text;
		}
		
		function addDepartment(){
			var node = $('#tree-department').tree('getSelected');
			$('#win-department-addoredit').window('open');
			$('#form-department-addoredit-parentId').combobox('loadData',[{value:node.id,
				text:node.text}]
			);
			$('#id').val(0);
			$('#form-department-addoredit-parentId').combobox('setValue',node.id);
			$('#tr-department-code').css('display','none');
		}
		
		function submitAddorEditDepartment(){
			var url = '${ctx}/manager/admin/org/addNewDepartment';
			var sucMeg = '<spring:message code="vendor.org.addDepartmentSuccess"/>！';/* 添加部门成功 */
			var isEdit = false;
			if($('#id').val()!=0 && $('#id').val()!='0'){
				url = '${ctx}/manager/admin/org/update';
				sucMeg = '<spring:message code="vendor.org.editorialSuccess"/>！';/* 编辑部门成功 */
				isEdit = true;
			}
			$.messager.progress({title:'<spring:message code="vendor.submission"/>'/* 提交中 */,msg:'<spring:message code="vendor.org.waitFor"/>......'});/* 请等待 */
			$('#form-department-addoredit').form('submit',{
				ajax:true,
				url:url,
				onSubmit:function(){
					var isValid = $(this).form('validate');
					if(!isValid){
						$.messager.progress('close');
					}
					return isValid;
				},
				success:function(data){
					$.messager.progress('close');
					try{
					var result = eval('('+data+')');
					if(result.success){
						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,sucMeg,'info');
						$('#win-department-addoredit').window('close');
						var currentNode = $('#tree-department').tree('getSelected');
						if(currentNode.state=='open' && !isEdit){
							$('#tree-department').tree('append',{
								parent:currentNode.target,
								data:[result.node]
								
							});
						}
						
						if(isEdit){
							var newNode = result.node;
							newNode.target = currentNode.target;
							$('#tree-department').tree('update',newNode);
						}
					}else{
						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,result.msg,'error');
					}
					}catch (e) {
						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data,'error');
					}
				}
				
			});
		}
		
		function editDepartment(){
			var node = $('#tree-department').tree('getSelected');
			var pnode = $('#tree-department').tree('getParent',node.target);
			var id = node.id;
			$('#win-department-addoredit').window('open');
			$('#tr-department-code').css('display','');
			//上级组织要重新加载
			$('#form-department-addoredit-parentId').combobox('loadData',[{value:pnode.id,
				text:pnode.text}]
			);
			$('#form-department-addoredit').form('load','${ctx}/manager/admin/org/getOrg/'+id);
		}
		
		function delDepartment(){
			var node = $('#tree-department').tree('getSelected');
			var params = '[{"id":'+node.id+'}]';
			$.ajax({
				url:'${ctx}/manager/admin/org/deleteOrg',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
					$('#tree-department').tree('remove',node.target);
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.org.deleteDepartmentSuccessful"/>!'/* 删除部门成功 */,'info');
				}
			});
			
		}
	</script>
</head>

<body style="margin:0;padding:0;">
	<div class="easyui-panel" data-options="border:false">
		<ul id="tree-department" class="easyui-tree"
			data-options="url:'${ctx}/manager/admin/org/getOrgEasyuiTree',queryParams:{id:0},fomatter:treeFmt
			,onContextMenu:function(e,node){
				e.preventDefault();
				$('#tree-department').tree('select',node.target);
				if(node.attributes.orgType==0){
					$('#menu-department-edit').css('display','none');
					$('#menu-department-del').css('display','none');
				}else{
					$('#menu-department-edit').css('display','');
					$('#menu-department-del').css('display','');
				}
				$('#menu-department').menu('show',{
					left:e.pageX,
					top:e.pageY
				});
			}
			"
		>
		
		</ul>
	</div>	
	<div id="menu-department" class="easyui-menu">
		<div id="menu-department-add" onclick="addDepartment()" data-options="iconCls:'icon-add'"><spring:message code="vendor.org.addDepartment"/><!-- 添加部门 --></div>
		<div id="menu-department-edit" onclick="editDepartment()" data-options="iconCls:'icon-edit'"><spring:message code="vendor.org.editorialDepartment"/><!-- 编辑部门 --></div>
		<div id="menu-department-del" onclick="delDepartment()" data-options="iconCls:'icon-delete'"><spring:message code="vendor.org.deleteDepartment"/><!-- 删除部门 --></div>
	</div>
	
	<div id="win-department-addoredit" class="easyui-window" title="新增部门" style="width:400px;height:260px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-department-addoredit" method="post" >
				<input id="id" name="id" value="0" type="hidden"/>
				<input name="orgType" value="1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr id="tr-department-parentId">
					<td><spring:message code="vendor.org.superiorOrganization"/><!-- 上级组织 -->:</td>
						<td><input class="easyui-combobox" name="parentId" id="form-department-addoredit-parentId" type="text"
							data-options="textField:'text',valueField:'value'"
						/>
					</td>
				</tr>
				<tr id="tr-department-code">
					<td><spring:message code="vendor.coding"/><!-- 编码 -->:</td><td><input class="easyui-textbox" name="code" type="text"
						data-options="disabled:true"
					/></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.appellation"/><!-- 名称 -->:</td><td><input class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.org.responsiblePerson"/><!-- 负责人 -->:</td><td><input class="easyui-textbox" name="legalPerson" type="text"
						data-options="required:true"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditDepartment()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-department-addoredit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>	
</body>
</html>
