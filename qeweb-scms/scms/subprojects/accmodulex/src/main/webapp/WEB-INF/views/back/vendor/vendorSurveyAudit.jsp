<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>调查表审核-采购商</title>
</head>

<body style="margin:0;padding:0;">
<script type="text/javascript" src="${ctx}/static/script/vendor/vendorAdmittance_foton.js"></script>
<!-- gloab var -->
<input id="orgId" value="${orgId}" type="hidden"/>
<div class="easyui-layout" data-options="fit:true">
  <div class="easyui-panel" title="调查表列表" data-options="region:'west',width:180">
    <ul id="tree-survey" class="easyui-tree" data-options="
    border : false,
    url:'${ctx}/manager/vendor/cfg/getVendorPhaseSurveyCfg',
    queryParams : {'vendorId':${vendor.id}}
    ">
    </ul>
  </div>
  <div class="easyui-panel" data-options="region:'center',border:false">
    <div class="easyui-tabs" id="tabs-survey" data-options="fit:true,tools:'#tabs-tool-audit'">
      <div title="基本信息">
        <form id="survey-base" enctype="multipart/form-data" method="post">
          <input type="hidden" itemId="baseInfo" name="baseInfo"/>
          <div style="display: none;" itemId="fileCt"></div>
        </form>
        <div class="container-fluid">
          <div id="btn-group-audit-base" class="pull-left">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-application_go'" onclick="VendorAdmittance.audit(${vendorSurveyCfg.id})">审核</a>
          </div>
          <!-- 显示历史 -->
          <div class="pull-right">
			<table id="survey-his-base" class="easyui-datagrid" data-options="title:'版本信息',
			width:500,
			collapsible:true,collapsed:true,rownumbers:true,
			url:'${ctx}/manager/vendor/admittance/getBaseInfoAuditHis',
			onExpand : function(){VendorAdmittance.getHis('survey-his-base');},
			queryParams:{'ctId':${vendor.orgId},'currentId':${vendor.id}}">
			<thead>
			<tr>
			<th data-options="field:'id',hidden:true">ID</th>
			<th data-options="field:'versionNO',formatter:SurveyView.viewBaseHisFmt">版本号</th>
			<th data-options="field:'auditUser'">审核人</th>
			<th data-options="field:'auditReason'">审核原因</th>
			<th data-options="field:'lastUpdateTime',formatter:SurveyView.viewAuditTime">审核时间</th>
			<th data-options="field:'auditStatus',formatter:function(v){return StatusRender.render(v,'audit',true)}">审核状态</th>
			<th data-options="field:'currentVersion',formatter:function(v){return StatusRender.render(v,'yesOrNo',true);}">当前版本</th>
			</tr>
			</thead>
			</table>
          </div>
          
        </div>
      
        <div id="survey-base-ct" class="container-fluid text-right" name="ct">
          <input type="hidden" id="vendorsxlid" name="id" value="${vendor.id}"/>
          <input type="hidden" name="orgId" value="${vendor.orgId}"/>
          <input type="hidden" itemId="submitStatus" value="${vendorSurveyCfg.submitStatus}"/>
          <input type="hidden" itemId="auditStatus" value="${vendorSurveyCfg.auditStatus}"/>
          <fieldset><legend class="text-left"><span class="label label-primary"><small>企业所有权信息</small></span></legend>
          	<table style="width: 100%; font-size: 12px;">
          		<tr>
          			<th  width="10%">供应商名称:</th>
          			<td  width="23%" align="left">${vendor.name}</td>
          			<th  width="10%">供应商简称:</th>
          			<td  width="23%" align="left">${vendor.shortName}</td>
          			<th  width="11%">邓白氏编码:</th>
          			<td  width="23%" align="left">${vendor.duns}</td>
          		</tr>

          		<tr>
          			<th>成立时间:</th>
          			<td width="23%" align="left">${vendor.regtime}</td>
          			<th>企业法人:</th>
          			<td width="23%" align="left">${vendor.legalPerson}</td>
          			<th>上市公司:</th>
          			<td width="23%" align="left">${vendor.duns}</td>
          		</tr>

          		<tr>
          			<th>企业性质:${vendor.property}</th>
          			<td width="23%" align="left">
          				<c:if test='${vendor.property eq 1}'>国有企业</c:if>
		             	<c:if test='${vendor.property eq 2}'>国有控股企业</c:if>
		             	<c:if test='${vendor.property eq 3}'>外资企业</c:if>
		             	<c:if test='${vendor.property eq 4}'>合资企业</c:if>
		             	<c:if test='${vendor.property eq 5}'>私营企业</c:if>
		             	<c:if test='${vendor.property ne 1}'>
			             	<c:if test='${vendor.property ne 2}'>
				             	<c:if test='${vendor.property ne 3}'>
				             		<c:if test='${vendor.property ne 4}'>
				             			<c:if test='${vendor.property ne 5}'>${vendor.property }</c:if>
				             		</c:if>
				             	</c:if>
			             	</c:if>
		             	</c:if>
		            </td>
          			<th>股比构成:</th>
          			<td width="23%" align="left">${vendor.stockShare}</td>
          			<th>网址:</th>
          			<td width="23%" align="left">${vendor.webAddr}</td>
          		</tr>

          		<tr>
          			<th>税务登记号:</th>
          			<td width="23%" align="left">${vendor.taxId}
		            </td>
          			<th>银行名称:</th>
          			<td width="23%" align="left">${vendor.bankName}</td>
          			<th>银行帐号:</th>
          			<td width="23%" align="left">${vendor.bankCard}</td>
          		</tr>

          		<tr>
          			<th>注册地址:</th>
          			<td width="23%" align="left">${vendor.address}
		            </td>
          			<th>主供事业部:</th>
          			<td width="23%" align="left">
          			${vendor.mainBUss }
          			</td>
          			<th>供应商类别:</th>
          			<td width="23%" align="left"><c:if test='${vendor.vendorType eq 1}'>代理供应商</c:if>
                <c:if test='${vendor.vendorType eq 2}'>生产供应商</c:if>
                <c:if test='${vendor.vendorType eq 3}'>服务供应商</c:if>
                <c:if test='${vendor.vendorType eq 4}'>经销供应商</c:if></td>
          		</tr>
          	</table>
          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>企业规模</small></span></legend>
          <table style="width: 100%; font-size: 12px;">
          		<tr>
          			<th  width="13%">注册资本(万元):</th>
          			<td  width="20%" align="left">${vendor.regCapital}</td>
          			<th  width="13%">资本总额(万元):</th>
          			<td  width="20%" align="left">${vendor.totalCapital}</td>
          			<th  width="14%">流动资金总额(万元):</th>
          			<td  width="20%" align="left">${vendor.workingCapital}</td>
          		</tr>
          		<tr>
          			<th  width="13%">占地面积(万平方米):</th>
          			<td  width="20%" align="left">${vendor.floorArea}</td>
          			<th  width="13%"></th>
          			<td  width="20%" align="left"></td>
          			<th  width="14%"></th>
          			<td  width="20%" align="left"></td>
          		</tr>
          </table>

          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>生产地址信息</small></span></legend>
          <table style="width: 100%; font-size: 12px;">
          		<tr>
          			<th  width="10%">所在省:</th>
          			<td  width="23%" align="left">${vendor.provinces.name}</td>
          			<th  width="10%">所在市:</th>
          			<td  width="23%" align="left">${vendor.citys.name}</td>
          			<th  width="11%"></th>
          			<td  width="23%" align="left"></td>
          		</tr>
          		<tr>
          			<th  width="10%">距离怀柔公里:</th>
          			<td  width="23%" align="left">${vendor.col1}</td>
          			<th  width="10%">距离密云公里:</th>
          			<td  width="23%" align="left">${vendor.col2}</td>
          			<th  width="11%">距离潍坊公里:</th>
          			<td  width="23%" align="left">${vendor.col3}</td>
          		</tr>
          		<tr>
          			<th  width="10%">距离诸城公里:</th>
          			<td  width="23%" align="left">${vendor.col4}</td>
          			<th  width="10%">距离长沙公里:</th>
          			<td  width="23%" align="left">${vendor.col5}</td>
          			<th  width="11%">距离南海公里:</th>
          			<td  width="23%" align="left">${vendor.col6}</td>
          		</tr>
          		<tr>
          			<th  width="10%">距离昌平公里:</th>
          			<td  width="23%" align="left">${vendor.col7}</td>
          			<th  width="10%"></th>
          			<td  width="23%" align="left"></td>
          			<th  width="11%"></th>
          			<td  width="23%" align="left"></td>
          		</tr>
          </table>

          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>联系人信息</small></span></legend>
            <table style="width: 100%; font-size: 12px;">
          		<tr>
          			<th  width="10%">总经理:</th>
          			<td  width="40%" align="left">${vendor.col8}</td>
          			<th  width="10%">联系电话:</th>
          			<td  width="40%" align="left">${vendor.col9}</td>
          		</tr>
          		<tr>
          			<th  width="10%">销售负责人:</th>
          			<td  width="40%" align="left">${vendor.col10}</td>
          			<th  width="10%">联系电话:</th>
          			<td  width="40%" align="left">${vendor.col11}</td>
          		</tr>
          		<tr>
          			<th  width="10%">质量负责人:</th>
          			<td  width="40%" align="left">${vendor.col12}</td>
          			<th  width="10%">联系电话:</th>
          			<td  width="40%" align="left">${vendor.col13}</td>
          		</tr>
          		<tr>
          			<th  width="10%">研发负责人:</th>
          			<td  width="40%" align="left">${vendor.col14}</td>
          			<th  width="10%">联系电话:</th>
          			<td  width="40%" align="left">${vendor.col15}</td>
          		</tr>
          </table>

          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>其他信息（新供应商可不用填写）</small></span></legend>
            <table style="width: 100%; font-size: 12px;">
          		<tr>
          			<th  width="10%">进入神农客公司年份:</th>
          			<td  width="40%" align="left">${vendor.col16}</td>
          			<th  width="10%">离开神农客公司年份:</th>
          			<td  width="40%" align="left">${vendor.col17}</td>
          		</tr>		
          </table>
          </fieldset>
        </div>
      </div>
    </div>
    <div id="tabs-tool-audit">
      <a class="easyui-linkbutton" itemId="btn-promote" data-options="iconCls:'icon-control_add_blue'" onclick="VendorAdmittance.promote(${vendor.id},'${vendor.name}',${vendor.phaseId},${vendor.phaseSn})">晋级</a>
      <a class="easyui-linkbutton" itemId="btn-demotion" data-options="iconCls:'icon-control_remove_blue'" onclick="VendorAdmittance.demotion(${vendor.id},${vendor.phaseId})">降级</a>
    </div>
  </div>





<!-- 供货关系弹出框 福田定制
<div id="dialog-supply-material" class="easyui-dialog" data-options="
  modal:true,closed:true,title:'供货关系列表',toolbar:'#dialog-supply-material-tt'
">
  <input type="hidden" itemId="phaseId" name="phaseId"/>
  <input type="hidden" itemId="vendorId" name="vendorId"/>
  <input type="hidden" itemId="orgId" name="orgId"/>
  <input type="hidden" itemId="changeType" name="changType"/>
  <input type="hidden" itemId="changeTypeText" name="changTypeText"/>
  <div id="dialog-supply-material-tt">
    <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:;" onclick="VendorAdmittance.promoteWithSupMat(${vendor.id})">提交</a>
  </div>
  <div class="easyui-layout" data-options="fit:true">
    <div class="easyui-panel" data-options="region:'north',height:250,title:'可选物料列表'">
      <table id="datagrid-supply-material-select" class="easyui-datagrid" data-options="
        fit:true,
        queryParams : {'search-EQ_abolished':0},
        pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20],
        method:'post',toolbar:'#datagrid-supply-material-select-tt'
      ">
        <thead>
          <tr>
            <th data-options="field:'id',checkbox:true"></th>
            <th data-options="field:'code'">物料编码</th>
            <th data-options="field:'name'">物料名称</th>
			<th data-options="field:'partsCode'">零部件编号</th>
			<th data-options="field:'partsName'">零部件类别</th>
			<th data-options="field:'picStatus'">图纸状态</th>
			<th data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">物料状态</th>
          </tr>
        </thead>
      </table>
      <div id="datagrid-supply-material-select-tt">
        <form id="form-supply-material-select" method="post">
			<input type="hidden" name="search-EQ_abolished" value="0"/>
			物料图号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			图纸状态：<input id="aa" class="easyui-combobox" name="search-LIKE_picStatus"  style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
 			零部件编号：<input type="text" name="search-LIKE_partsCode" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="VendorAdmittance.Material.search()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-material-search').form('reset')">重置</a>
	    </form>
	    <div>
	      <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:;" onclick="VendorAdmittance.Material.select()">选择</a>
	    </div>
      </div>
    </div>
    <div class="easyui-panel" data-options="region:'center',title:'已选物料列表(点击行可编辑)'">
      <table id="datagrid-supply-material-selected" class="easyui-datagrid" data-options="
      fit:true,border:false,
      idField:'id',
      toolbar:'#datagrid-supply-material-selected-tt',
      onClickCell: RowEditor.onClickCell
      ">
        <thead>
          <tr>
            <th data-options="field:'id',checkbox:true"></th>
            <th data-options="field:'code',width:160">物料编码</th>
            <th data-options="field:'name',width:200">物料名称</th>
            <th data-options="field:'unit',width:80,editor:'textbox'">单位</th>
            <th data-options="field:'partsName',width:200">所属零部件</th>
            <th data-options="field:'dataFrom',width:100,editor:{
              type:'combobox',
              editable:false,
              options:{
                  valueField:'value',
                  textField:'text',
                  data : [
                  {value:'新产品开发',text:'新产品开发'},
                  {value:'二次布点',text:'二次布点'},
                  {value:'商改',text:'商改'},
                  ]
              }
            }">数据来源</th>
          </tr>
        </thead>
      </table>
      <div id="datagrid-supply-material-selected-tt">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="VendorAdmittance.Material.removeSelected('#datagrid-supply-material-selected')">移除</a>
      </div>
    </div>
  </div>
</div>
-->
<script type="text/javascript">
$(':radio[name="auditStatus"]').bind('click',function(){
	$(':radio[name="auditStatus"]').each(function(i){
		$(this).attr('check',false);
	});
	$(this).attr('check',true);
	
});
$('#aa').combobox({    
    url:ctx+'/manager/basedata/material/getPicStatus',    
    valueField:'id',    
    textField:'text' ,
   	onSelect: function(rec){    
           $("input[name='search-LIKE_picStatus']").val(rec.text);   
      }
});
VendorAdmittance.auditSurveyInit('base');
VendorAdmittance.promoteInit(${vendor.phaseSn},${isEndPhase},${vendor.org.enableStatus});
SuveryAuditor.initTree();
</script>
</body>
</html>