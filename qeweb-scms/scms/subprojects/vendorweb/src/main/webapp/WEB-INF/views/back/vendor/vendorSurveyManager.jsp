<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.qeweb.scm.vendormodule.constants.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>调查表维护-供应商</title>	
	<script type="text/javascript" src="${ctx}/static/base/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/areaSelect.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/surveyManager.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/component/surveyView.js"></script>
</head>

<body style="margin:0;padding:0;">
<!-- gloab var -->
<input id="orgId" value="${orgId}" type="hidden"/>
<input id="attachmentSize" value="${attachmentSize}" type="hidden"/>
<div class="easyui-layout" fit="true">
  <div class="easyui-panel" title="当前调查表" data-options="region:'west',width:180,split:true" >
    <ul id="tree-survey" class="easyui-tree" data-options="
      border:false,
      url:'${ctx}/manager/vendor/cfg/getVendorPhaseSurveyCfg',
      queryParams : {'vendorId':${vendor.id}}
    ">
    </ul>
  </div>
  <div class="easyui-panel" data-options="region:'center'">
    <div class="easyui-tabs" data-options="fit:true,border:false" id="tabs-survey" style="height: 100%;">
      <div title="基本信息">
        <form id="survey-base" enctype="multipart/form-data" method="post">
          <input type="hidden" itemId="baseInfo" name="baseInfo"/>
          <div style="display: none;" itemId="fileCt"></div>
        </form>
        <!-- 保存按钮组 -->
        <div class="container-fluid">
          <div class="pull-left" id="btn-group-save-base">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="Survey.saveBase('survey-base')">保存</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Survey.submitBase('survey-base')">提交</a>
          </div>
          <!-- 变更按钮组 -->
	      <div class="pull-left hidden" id="btn-group-change-base">
	        <a class="easyui-linkbutton" itemId="change-apply" data-options="iconCls:'icon-bullet_wrench_red'" onclick="Survey.change('base')">变更</a>
	      </div>
	      <!-- 显示历史 -->
          <div class="pull-right" id="group-change-base">
			<table id="survey-his-base" class="easyui-datagrid" data-options="title:'版本信息',
			width:500,
			collapsible:true,collapsed:true,rownumbers:true,
			url:'${ctx}/manager/vendor/admittance/getBaseInfoHis',
			onExpand : function(){Survey.getHis('survey-his-base');},
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
        <div id="survey-base-ct" class="container-fluid text-right" name="ct" >
        <form itemId="validate-form">
          <input type="hidden" itemId="id" name="id" value="${vendor.id}"/>
          <input type="hidden" name="orgId" value="${vendor.orgId}"/>
          <input type="hidden" itemId="submitStatus" name="submitStatus" value="${vendorSurveyCfg.submitStatus}"/>
          <input type="hidden" itemId="auditStatus" name="auditStatus" value="${vendorSurveyCfg.auditStatus}"/>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>供应商所有权信息</small></span></legend>
            <div class="row row-form">
              <div class="col-md-6">供应商名称:<input type="hidden" name="name" value="${vendor.name}"/>${vendor.name}</div>
              <div class="col-md-6">企业性质:
              <input class="easyui-combobox" id="property" data-options="
              valueField:'value',textField:'text',
              url : '${ctx}/public/vendor/getVendorProperties',
              required:true" 
              style="width:140px;" 
              itemId="property" name="property" value="${vendor.property}"/>
              </div>
            </div>
            <div class="row row-form">
              <div class="col-md-12">厂址:
              <input id="base-province" class="easyui-combobox"
               data-options="valueField:'value',textField:'text',
               url : '${ctx}/public/common/areaselect/getAreaSelectByLevel/2',
               onSelect : function(r){AreaSelector.onSelect(r,'base-province','base-city');}"
               id="combobox_province"
               style="width:140px;" name="province" value="${vendor.province}"/><input id="base-city" class="easyui-combobox" 
               data-options="valueField:'value',textField:'text',
               url : '${ctx}/public/common/areaselect/getAreaSelect?id=${vendor.province}'"
              style="width:140px;" name="city" value="${vendor.city}"/>
              <input class="easyui-textbox" required="true" style="width:140px;" name="address" value="${vendor.address}"/>
              </div>
            </div>
            <div class="row row-form">
              <div class="col-md-6">电话:<input class="easyui-datebox" style="width:140px;" name="col1" data-options="editable:false"  value="${vendor.col1}"/></div>
              <div class="col-md-6">邮政编码:<input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="col12" value="${vendor.col12}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-6">传真:<input class="easyui-datebox" style="width:140px;" name="col3" data-options="editable:false"  value="${vendor.col3}"/></div>
              <div class="col-md-6">邮箱:<input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="col14" value="${vendor.col14}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-6">法人代表:<input class="easyui-datebox" style="width:140px;" name="legalPerson" data-options="editable:false"  value="${vendor.legalPerson}"/></div>
              <div class="col-md-6">中级以上技术职称人数:<input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="col7" value="${vendor.col7}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-6">员工总数:<input class="easyui-datebox" style="width:140px;" name="employeeAmount" data-options="editable:false"  value="${vendor.employeeAmount}"/></div>
              <div class="col-md-6">中级以上技术职称人数:<input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="col8" value="${vendor.col8}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-6">工厂面积:<input class="easyui-datebox" style="width:140px;" name="col6" data-options="editable:false"  value="${vendor.col6}"/></div>
              <div class="col-md-6">质量管理机构名称:<input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="col9" value="${vendor.col9}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-6">固定资产:<input class="easyui-datebox" style="width:140px;" name="totalCapital" data-options="editable:false"  value="${vendor.totalCapital}"/></div>
              <div class="col-md-6">质管部门负责人姓名:<input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="col10" value="${vendor.col10}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-6">上年总产值:<input class="easyui-datebox" style="width:140px;" name="col11" data-options="editable:false"  value="${vendor.col11}"/></div>
              <div class="col-md-6">质管联系人/联系电话:<input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="col12" value="${vendor.col12}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-6">上年销售收入:<input class="easyui-datebox" style="width:140px;" name="lastYearIncome" data-options="editable:false"  value="${vendor.lastYearIncome}"/></div>
              <div class="col-md-6">质量管理体系/有效期:<input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="col12" value="${vendor.col13}"/></div>
            </div>
          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>是否需要改进</small></span></legend>
            <div class="row row-form">
              <div class="col-md-12">质量管理方面:<input class="easyui-datebox" style="width:140px;" name="col14" data-options="editable:false"  value="${vendor.col14}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-12">文明生产方面:<input class="easyui-datebox" style="width:140px;" name="col15" data-options="editable:false"  value="${vendor.col15}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-12">现场控制方面:<input class="easyui-datebox" style="width:140px;" name="col16" data-options="editable:false"  value="${vendor.col16}"/></div>
            </div>
          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>供应商生产地址信息</small></span></legend>
            <div class="row row-form">
              <div class="col-md-4">所在省:<input id="base-province" class="easyui-combobox"
               data-options="valueField:'value',textField:'text',
               url : '${ctx}/public/common/areaselect/getAreaSelectByLevel/2',
               onSelect : function(r){AreaSelector.onSelect(r,'base-province','base-city');}
               "
               id="combobox_province"
               style="width:140px;" name="province" value="${vendor.province}"/></div>
              <div class="col-md-4">所在市:<input id="base-city" class="easyui-combobox" 
               data-options="valueField:'value',textField:'text',
               url : '${ctx}/public/common/areaselect/getAreaSelect?id=${vendor.province}'
               "
              style="width:140px;" name="city" value="${vendor.city}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">距离怀柔公里:<input class="easyui-textbox" required="true" style="width:140px;" name="col1" value="${vendor.col1}"/></div>
              <div class="col-md-4">距离密云公里:<input class="easyui-textbox" required="true" style="width:140px;" name="col2" value="${vendor.col2}"/></div>
              <div class="col-md-4">距离潍坊公里:<input class="easyui-textbox" required="true" style="width:140px;" name="col3" value="${vendor.col3}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">距离诸城公里:<input class="easyui-textbox" required="true" style="width:140px;" name="col4" value="${vendor.col4}"/></div>
              <div class="col-md-4">距离长沙公里:<input class="easyui-textbox" required="true" style="width:140px;" name="col5" value="${vendor.col5}"/></div>
              <div class="col-md-4">距离南海公里:<input class="easyui-textbox" required="true" style="width:140px;" name="col6" value="${vendor.col6}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">距离昌平公里:<input class="easyui-textbox" required="true" style="width:140px;" name="col7" value="${vendor.col7}"/></div>
            </div>
          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>供应商联系人信息</small></span></legend>
            <div class="row row-form">
              <div class="col-md-4">总经理:<input class="easyui-textbox" required="true" style="width:140px;" name="col8" value="${vendor.col8}"/></div>
              <div class="col-md-4">联系电话:<input class="easyui-textbox" required="true" style="width:140px;" name="col9" value="${vendor.col9}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">销售负责人:<input class="easyui-textbox" required="true" style="width:140px;" name="col10" value="${vendor.col10}"/></div>
              <div class="col-md-4">联系电话:<input class="easyui-textbox" required="true" style="width:140px;" name="col11" value="${vendor.col11}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">质量负责人:<input class="easyui-textbox" required="true" style="width:140px;" name="col12" value="${vendor.col12}"/></div>
              <div class="col-md-4">联系电话:<input class="easyui-textbox" required="true" style="width:140px;" name="col13" value="${vendor.col13}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">研发负责人:<input class="easyui-textbox" required="true" style="width:140px;" name="col14" value="${vendor.col14}"/></div>
              <div class="col-md-4">联系电话:<input class="easyui-textbox" required="true" style="width:140px;" name="col15" value="${vendor.col15}"/></div>
            </div>
          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>供应商所有权信息</small></span></legend>
            <div class="row row-form">
              <div class="col-md-4">进入福田公司年份:<input class="easyui-datebox" data-options="editable:false" style="width:140px;" name="col16" value="${vendor.col16}"/></div>
              <div class="col-md-4">离开福田公司年份:<input class="easyui-datebox" data-options="editable:false" style="width:140px;" name="col17" value="${vendor.col17}"/></div>
            </div>
          </fieldset>
          </form>
        </div>
        
         
        
      </div>
    </div>
  </div>
</div>
<div id="dialog-survey-hisview" class="easyui-dialog"  data-options="modal:true,closed:true,title:'历史查看'">

</div>
<script>
Survey.initComponent('base');
</script>
</body>
</html>
