<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.qeweb.scm.vendormodule.constants.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
	<title>用户注册</title>
	<link href="${ctx}/static/base/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
<%-- 	<link rel="stylesheet" href="${ctx}/static/base/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css"> --%>
	<link rel="stylesheet" href="${ctx}/static/base/zTree_v3/css/metroStyle/metroStyle.css" type="text/css">
	<link href="${ctx}/static/styles/register.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${ctx}/static/base/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${ctx}/static/base/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
    <script type="text/javascript" src="${ctx}/static/base/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${ctx}/static/base/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
    
<%--    <link href="${ctx}/static/base/bootstrap-multiselect-dist/css/bootstrap-multiselect.css" type="text/css" rel="stylesheet" /> --%>
<%-- 	<script type="text/javascript" src="${ctx}/static/base/bootstrap-multiselect-dist/js/bootstrap-multiselect.js" ></script> --%>
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
	
	//企业性质常量
	var vendorPropertyConstant = <%=VendorModuleTypeConstant.getVendorPropertyJson()%>;
	var customerIndex = 0;
	function addMainCustomer(){
		var tr = '<tr>';
		tr+='<td><input type="checkbox" name="mainCustomer"/><input type="hidden" value="0" name="exList['+customerIndex+'].exType"/></td>';
		tr+='<td><input class="input" type="input" name="exList['+customerIndex+'].motorFactory"/></td>';
		tr+='<td><input class="input" type="input" name="exList['+customerIndex+'].productLine"/></td>';
		tr+='<td><input class="input" type="input" name="exList['+customerIndex+'].carModel"/></td>';
		tr+='</tr>';
		$('#mainCustomerCt').append(tr);
		customerIndex++;
		
	}
	
	function minusMainCustomer(){
		var $ckList = $('#mainCustomerCt').find('input:checked');
		$ckList.each(function(i){
			$(this).parentsUntil('tr').parent().remove();
		});
	}

       
	function changeComplex(obj,link,child) {
		var id = obj.value;
		//给文本赋值
		var $targetOption = $(obj).find('option[value="'+id+'"]:eq(0)');
		$('#'+obj.id+'-text').val($targetOption.html());
		if($targetOption.html()!="中国"&&link=='province')
		{
			$("#searchprov").removeClass("required");
			$("#searchcity").removeClass("required");
			$("#searchdistrict").removeClass("required");
			$("#addressss").removeClass("required");
		}
		else
		{
			$("#searchprov").addClass("required");
			$("#searchcity").addClass("required");
			$("#searchdistrict").addClass("required");
			$("#addressss").addClass("required");
		}
		
		var $country = $("#searchcountry");
	    var $prov = $("#searchprov");
	    var $city = $("#searchcity");
	    var $dist = $("#searchdistrict");
	    var initText  = '';
		//根据parent判断级别
		if(link=='province'){
			$prov.html(''); 
			$city.html(''); 
			$dist.html(''); 
			$prov.append('<option value="">请选择省份</option>');
        	$city.append('<option value="">请选择城市</option>');
        	$dist.append('<option value="">请选择区域</option>');
        	initText = '请选择省份';
		}
		if(link=='city'){
			$city.html(''); 
			$dist.html(''); 
        	$city.append('<option value="">请选择城市</option>');
        	$dist.append('<option value="">请选择区域</option>');
        	initText = '请选择城市';
		}
		if(link=='district'){
			$dist.html(''); 
        	$dist.append('<option value="">请选择区域</option>');
        	initText = '请选择区域';
		}
		
		$.ajax({
	        type: "POST",
	        url: "${ctx}/public/common/areaselect/getAreaSelect",
	        data:{id:id},
	        cache: false,
	        dataType : "json",
	        success: function(data){
	        	var $children = $('#'+child);
	        	$children.html('');
	        	$children.append('<option value="">'+initText+'</option>');
	        	$.each(data,function(i,n){
	        		$children.append('<option value="'+n.value+'">'+n.text+'</option>');
	        	});
	        }
	      });
	}
	
    $(document).ready(function() {
		//初始化企业性质
		for(var i in vendorPropertyConstant)
		$('#property').append('<option value="'+i+'">'+vendorPropertyConstant[i]+'</option>');
		
		//聚焦第一个输入框
		$("#shortName").focus();
		$("#inputForm").validate({
			//errorPlacement  : function(error,el){}
			rules: {
				"duns":{
						rangelength:[9,9]
					},				
				"stockShare":{
					required: true,
					}					
			},
			messages: {
				"duns": {
					rangelength: "<font style='color: #F00;'>邓白氏编码9位数字</font>"
				},
				"stockShare": {
					required: "<font style='color: #F00;'>必选字段</font>"
				}
			}
			
		});
		//为inputForm注册validate函数
		$('.form_date').datetimepicker({
	        language:  'zh-CN',
	        format: 'yyyy-mm-dd',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0,
			pickerPosition: "bottom-left",
			initialDate :'2000-01-01'
	    });
		//获得产品
//			$.ajax({
//				url:'${ctx}/manager/basedata/materialType/getLeafMaterialTypeList',
//				data:{leaf:1},
//				method:'post',
//				dataType:'json',
//				success:function(data){
//					//$('#mainProduct').append('<option value="">--请选择--</option>');
//					for(var i=0;i<data.length;i++){
//						var matType = data[i];
//						$('#mainProduct').append('<option value="'+matType.id+'">'+matType.name+'</option>');
//					}
//					$('#mainProduct').multiselect();
//				}
			
//			});

		$.ajax({//省市
	        type: "POST",
	        url: "${ctx}/public/common/areaselect/getAreaSelect",
	        data:{id:0},
	        cache: false,
	        dataType : "json",
	        success: function(data){
	        	//给第一级的赋值
	        	var $country = $("#searchcountry");
	        	$country.append('<option value="">请选择国家</option>');
	        	$.each(data,function(i,n){
	        		$country.append('<option value="'+n.value+'">'+n.text+'</option>');
	        	});
	        	//其他的都给初始值
	        	var $prov = $('#searchprov');
	        	var $city = $('#searchcity');
	        	var $district = $('#searchdistrict');
	        	$prov.append('<option value="">请选择省份</option>');
	        	$city.append('<option value="">请选择城市</option>');
	        	$district.append('<option value="">请选择区域</option>');
	        	
	        	
	        }
	      });
		$.fn.zTree.init($("#mpTree"), setting);
		
	});	
		
	</script>
</head>

<body>
    <shiro:guest>
       <form id="active_login_form" action="${ctx}/public/login" method="post">
          <input name="username" type="hidden" value="${username}"/>
          <input name="password" type="hidden" value="${password}"/>
       </form>
       <script>
         $('#active_login_form').submit();
       </script>
    </shiro:guest>
	<link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/base.css" />
	<link rel="stylesheet" href="${ctx}/static/cuslibs/register/css/layout.css"/>
	<div id="wrapper">
		<header id="header">

			<div id="headBox">
				<div class="w960 oh">
					<nav id="navs" class="fr">
						<a href="${ctx}/logout">登录界面</a>
						<a class="active" href="#">注册界面</a>
					</nav>
				</div>
			</div>
		</header><!-- // header end -->
		<div class="container w960 mt20">
			<div id="processor" >
				<ol class="processorBox oh">
					<li>
						<div class="step_inner fl">
							<span class="icon_step">1</span>
							<h4>注册帐号信息</h4>
						</div>
					</li>
					<li class="current">
						<div class="step_inner">
							<span class="icon_step">2</span>
							<h4>填写基本信息</h4>
						</div>
					</li>
					<li>
						<div class="step_inner fr">
							<span class="icon_step">3</span>
							<h4>注册结果</h4>
						</div>
					</li>
				</ol>
				<div class="step_line"></div>
			</div>
			<div class="content">
				<div id="step1" class="step hide">
				<form id="inputForm" action="${ctx}/vendor/active/activeVendor" method="post">
				<input type="hidden" id="name" name="${org.name}"/>
				<input type="hidden" id="orgId" name="${org.id}"/>
					<table>
						<tr style="height: 60px">
							<td align="center"><label for="shortName" class="frm_label">企业简称</label></td>
							<td>
								<input type="text" id="shortName" name="shortName" class="frm_input required"/>
							</td>
							<td><label  for="duns" class="frm_label">邓白氏编码</label></td>
							<td>
								<input type="number" id="duns" name="duns" class="frm_input"/>
							</td>
						</tr>
						<tr style="height: 60px">
							<td align="center"><label  for="property"  class="frm_label">企业性质</label></td>
							<td>
								<select class="frm_input" id="property" name="property">
								</select>
							</td>
							<td  align="center"><label  for="regtime" class="frm_label">成立时间</label></td>
							<td>
								<div class="input-append  date form_date" data-date="1979-09-16" data-link-field="regtime" >
				                    <input size="16" type="text" value="2000-01-01" class="frm_input" readonly style="width:100px;">
									<span class="add-on"><i class="icon-th"></i></span>
							     </div>
							     <input type="hidden" id="regtime" name="regtime" class="required" value="2000-01-01 00:00:00" />
							</td>
						</tr>
						<tr style="height: 60px">
							<td align="center"><label for="legalPerson" class="frm_label">企业法人</label></td>
							<td>
								<input type="text" id="legalPerson" name="legalPerson" class="frm_input required"/>
							</td>
							<td align="center"><label for="stockShare" class="frm_label">股比构成</label></td>
							<td>
								<input type="text" id="stockShare" name="stockShare" class="frm_input required"/>
							</td>
						</tr>
						<tr style="height: 60px">
							<td align="center"><label for="address" class="frm_label">地址</label></td>
							<td id="address" colspan="3">
								<select id="searchcountry" name="country" class="frm_input required" style="width:150px" onChange="changeComplex(this,'province','searchprov');"></select>&nbsp;&nbsp;
								<input type="hidden" id="searchcountry-text" name="countryText"/>
								<select id="searchprov" name="province" class="frm_input" style="width:150px" onChange="changeComplex(this,'city','searchcity');"></select>&nbsp;&nbsp;
								<input type="hidden" id="searchprov-text" name="provinceText"/>
								<select id="searchcity" name="city" class="frm_input" style="width:150px" onChange="changeComplex(this,'district','searchdistrict');"></select>&nbsp;&nbsp;
								<input type="hidden" id="searchcity-text" name="cityText"/>
								<select id="searchdistrict" name="district" class="frm_input" style="width:150px"></select>
							    <input type="hidden" id="searchdistrict-text" name="districtText"/>
							</td>
						</tr>
						<tr style="height: 60px">
							<td align="center"><label for="address" class="frm_label">详细地址</label></td>
							<td colspan="3">
								<input type="text" id="addressss" name="address" class="frm_input span8 required" style="width:70%"/>
							</td>
						</tr>
						<tr  style="height: 60px">
							<td align="center"><label  for="mainProduct" class="frm_label">主要产品</label></td>
							<td colspan="3">
							    <input name="mainProduct" id="mainProductV" type="hidden"/>
							    <input class="frm_input required" id="mainProduct" type="text" readonly value="" style="width:70%" onclick="showMenu();" />
							    &nbsp;<a id="menuBtn" href="#" onclick="showMenu(); return false;">选择</a>
							</td>
						</tr>
						<tr  style="height: 60px">
							<td align="center"><label  for="mainProduct" class="frm_label">主要客户</label>
							</td>
							<td colspan="3">
								<a class="btn" onclick="addMainCustomer()"><i class="icon-plus"></i></a>
                				<a class="btn" onclick="minusMainCustomer()"><i class="icon-minus"></i></a>
							</td>
						</tr>
						<tr  style="height: 60px">
							<td colspan="4">
								<table style="width:100%" border="2" class="table">
							    <thead>
							      <tr>
							        <th>序号</th>
							        <th>主机厂</th>
							        <th>产品线</th>
							        <th>车型</th>
							      </tr>
							    </thead>
							    <tbody id="mainCustomerCt"></tbody>
							  </table>
							</td>
						</tr>
						<tr  style="height: 60px">
							<td align="center"><label for="lastYearIncome" class="frm_label">上一年度销售收入（万元）</label></td>
							<td>
								<input type="text" id="lastYearIncome" name="lastYearIncome" class="frm_input required"/>
							</td>
							<td align="center"><label for="employeeAmount" class="frm_label">员工人数（人）</label></td>
							<td>
								<input type="text" id="employeeAmount" name="employeeAmount" class="frm_input required"/>
							</td>
						</tr>
						<tr style="height: 60px">
							<td align="center"><label for="productPower" class="frm_label">产能（万台/年）</label></td>
							<td colspan="3">
								<input type="text" id="productPower" name="productPower" class="frm_input required"/>
							</td>
						</tr>
						<tr style="height: 60px">
							<td align="center"><label  for="qsCertificate"  class="frm_label">质量体系证书</label></td>
							<td colspan="3">
								<textarea rows="3" class="span8 required" name="qsCertificate"></textarea>
							</td>
						</tr>
					</table>
					<div class="toolBar">
					        <button id="submit_btn" type="button" onclick="saveSub()" class="btn btn-primary">提交</button>&nbsp;
							<a id="cancel_btn" class="btn" type="button" href="${ctx}/logout">退出</a>
					</div>
				</form>
			</div>
		</div>
		
		<!-- // container end -->
		<footer id="footer" class="w960 oh">
			<span class="fl">Copyright © 2008-2015 <a href="http://www.qeweb.com" target="_bank">Qeweb.com Inc.</a></span>
		</footer><!-- // footer end -->
	</div><!-- // wrapper end -->
	
	   <div id="mpContent" class="mpContent" style="display:none; position: absolute;">
	       <ul id="mpTree" class="ztree" style="margin-top:0; width:460px; height: 300px;"></ul>
        </div>
	<script>
		function saveSub(){
			var searchprov=$('#searchprov').val();
			var searchcity=$('#searchcity').val();
			var searchdistrict=$('#searchdistrict').val();
			if($('#searchprov option').length>1&&(searchprov==null||searchprov=="")){
				alert("省份不能为空!");
				return false;
			}
			if($('#searchcity option').length>1&&(searchcity==null||searchcity=="")){
				alert("城市不能为空!");
				return false;
			}
			if($('#searchdistrict option').length>1&&(searchdistrict==null||searchdistrict=="")){
				alert("区域不能为空!");
				return false;
			}
			$('#inputForm').submit();
		}
</script>
</body>
</html>
