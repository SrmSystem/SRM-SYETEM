<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>主要产品管理</title>
		<link href="${ctx}/static/base/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
	<link rel="stylesheet" href="${ctx}/static/base/zTree_v3/css/metroStyle/metroStyle.css" type="text/css">
	<link href="${ctx}/static/styles/register.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/static/base/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${ctx}/static/base/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${ctx}/static/base/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${ctx}/static/base/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
    
	<script>
	//产品树形
	var setting = {
			async : {
				enable:true,
				url : '${ctx}/manager/basedata/materialType/getMaterialTypeTree',
				dataType : 'json',
				autoParam:["id"]
			},
			check: {
				enable: true,
				chkboxType: {"Y":"", "N":""}
			},
			view: {
				dblClickExpand: false
			},
			data: {
				key : {name:'text',iconSkin:'iconCls'}
			},
			callback: {
				beforeClick: beforeClick,
				onCheck: onCheck
			}
		};
	
	    //var zNodes = [ { "id" : " ", "text" : "全部分类", "state" : "open", "checked" : "", "iconCls" : null, "children" : [ { "id" : "1", "text" : "动力系统", "state" : "closed", "checked" : null, "iconCls" : "icon-sitemap_color", "children" : null, "attributes" : { "needSecondVendor" : 0, "parentId" : 0, "importance" : 0, "remark" : null, "code" : "P001", "levelLayer" : 0 } }, { "id" : "1080", "text" : "传动变速器010", "state" : "closed", "checked" : null, "iconCls" : "icon-sitemap_color", "children" : null, "attributes" : { "needSecondVendor" : 0, "parentId" : 0, "importance" : 0, "remark" : null, "code" : "P003", "levelLayer" : 0 } }, { "id" : "1083", "text" : "变速器总成7", "state" : "closed", "checked" : null, "iconCls" : "icon-sitemap_color", "children" : null, "attributes" : { "needSecondVendor" : 0, "parentId" : 0, "importance" : 0, "remark" : null, "code" : "P006", "levelLayer" : 0 } }, { "id" : "47325", "text" : "wewew", "state" : "closed", "checked" : null, "iconCls" : "icon-sitemap_color", "children" : null, "attributes" : { "needSecondVendor" : 0, "parentId" : 0, "importance" : null, "remark" : null, "code" : "ewew", "levelLayer" : 1 } }, { "id" : "1081", "text" : "变速器091", "state" : "closed", "checked" : null, "iconCls" : "icon-sitemap_color", "children" : null, "attributes" : { "needSecondVendor" : 0, "parentId" : 0, "importance" : 0, "remark" : null, "code" : "P004", "levelLayer" : 0 } }, { "id" : "1085", "text" : "变速器", "state" : "closed", "checked" : null, "iconCls" : "icon-sitemap_color", "children" : null, "attributes" : { "needSecondVendor" : 0, "parentId" : 0, "importance" : 0, "remark" : null, "code" : "P0012", "levelLayer" : 0 } } ], "attributes" : null } ];


		function beforeClick(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("mpTree");
			zTree.checkNode(treeNode, !treeNode.checked, null, true);
			return false;
		}
		
		function onCheck(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("mpTree"),
			nodes = zTree.getCheckedNodes(true),
			v = "";
			vIds = "";
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].text + ",";
				vIds += nodes[i].id + ",";
			}
			if (v.length > 0 ) {
				v = v.substring(0, v.length-1);
				vIds = vIds.substring(0, vIds.length-1);
			}
			var $target = $("#mainProduct");
			var $targetV = $("#mainProductV");
			$target.attr("value", v);
			$targetV.attr("value", vIds);
			
		}

		function showMenu() {
			var $treeTarget = $("#mainProduct");
			var targetOffset = $("#mainProduct").offset();
			var width = $treeTarget.width();
			$('#mpTree').css('width',width);
			$("#mpContent").css({left:targetOffset.left + "px", top:targetOffset.top + $treeTarget.outerHeight() + "px"}).slideDown("fast");
			$("body").bind("mousedown", onBodyDown);
		}
		function hideMenu() {
			$("#mpContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "mainProduct" || event.target.id == "mpContent" || $(event.target).parents("#mpContent").length>0)) {
				hideMenu();
			}
		}
		 $(document).ready(function() {
				$.fn.zTree.init($("#mpTree"), setting);
				
			});	
		</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-area-list" title="主要产品" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/vendor/mainProduct',method:'post',singleSelect:false,
		toolbar:'#areaListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id'">ID</th>
		<th data-options="field:'materialTypeCode'">产品编码</th>
		<th data-options="field:'materialTypeName'">产品名称</th>
		</tr></thead>
	</table>
	<div id="areaListToolbar" style="padding:5px;">
		<div>
		</div>
		<div>
			<form id="form-edit-product" method="post">
								    <input name="mainProduct" id="mainProductV" type="hidden"/>
							    <input class="frm_input required" id="mainProduct" type="text" readonly value="" style="width:70%" onclick="showMenu();" />
							    &nbsp;<a id="menuBtn" href="#" onclick="showMenu(); return false;">选择</a>
							    
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="saveProduct()">维护</a>
		
			</form>
		</div>
	</div>

		<div id="mpContent" class="mpContent" style="display:none; position: absolute;">
	       <ul id="mpTree" class="ztree" style="margin-top:0; width:460px; height: 300px;"></ul>
        </div>	

<script type="text/javascript">
function searchMainProduct(){
	var searchParamArray = $('#form-area-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-area-list').datagrid('load',searchParams);
}

function saveProduct(){
	var mainProduct=$('#mainProduct').val();
	
	if(mainProduct==null||mainProduct==''){
		$.messager.alert('提示','主要产品不能为空！','error');
		return ;
	}

	var params = $('#mainProductV').val();
	$.ajax({
		url:'${ctx}/manager/vendor/mainProduct/saveMainProduct',
		type:'POST',
		data:params,
		contentType : 'text/html',
		success:function(data){
			
				$.messager.show({
					title:'消息',
					msg:'保存成功',
					timeout:2000,
					showType:'show',
					style:{
						right:'',
						top:document.body.scrollTop+document.documentElement.scrollTop,
						bottom:''
					}
				});

				$('#datagrid-area-list').datagrid('reload');
			
		}
	});
}

</script>
</body>
</html>
