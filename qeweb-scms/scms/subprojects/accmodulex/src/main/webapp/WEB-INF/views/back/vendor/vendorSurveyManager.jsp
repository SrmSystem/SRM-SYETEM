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
	<script type="text/javascript" src="${ctx}/static/script/vendor/surveyManager_foton.js"></script>
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
              <div class="col-md-4">供应商名称:<input type="hidden" name="name" value="${vendor.name}"/>${vendor.name}</div>
              <div class="col-md-4">供应商简称:<input type="hidden" name="shortName" value="${vendor.shortName}"/>${vendor.shortName}</div>
              <div class="col-md-4">邓白氏编码:<input class="easyui-textbox" style="width:140px;" id="dunsValue" name="duns" value="${vendor.duns}" data-options="validType:'length[9,9]'"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">成立时间:<input class="easyui-datebox" style="width:140px;" name="regtime" data-options="editable:false,required:true"  value="${vendor.regtime}"/></div>
              <div class="col-md-4">企业法人:
              		<c:if test="${vendorPhase.code ne \"official\"}"><input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="legalPerson" value="${vendor.legalPerson}"/></c:if>
              		<c:if test="${vendorPhase.code eq \"official\"}"><input type="hidden" name="legalPerson" value="${vendor.legalPerson}"/>${vendor.legalPerson}</c:if>
              </div>
              <div class="col-md-4">上市公司:<div class="text-left" style="width:140px;display:inline-block;">
              <label class="radio-inline"><input type="radio" <c:if test="${vendor.ipo==1}">checked="true"</c:if> name="ipo" value="1"/>是</label><label class="radio-inline"><input type="radio" <c:if test="${vendor.ipo==null || vendor.ipo==0}">checked="true"</c:if> value="0" name="ipo"/>否</label>
              </div></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">企业性质:
              <input class="easyui-combobox" id="property" data-options="
              valueField:'value',textField:'text',
              url : '${ctx}/public/vendor/getVendorProperties',
              required:true,editable:false" 
              style="width:140px;" 
              itemId="property" name="property" value="${vendor.property}"/>
              </div>
              <div class="col-md-4">股比构成:<input class="easyui-textbox" required="true" style="width:140px;" name="stockShare" value="${vendor.stockShare}"/></div>
              <div class="col-md-4">网址:<input class="easyui-textbox" style="width:140px;" id="webAddrValue" name="webAddr" value="${vendor.webAddr}" data-options="validType:'isUrl'"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">税务登记号:
              	<c:if test="${vendorPhase.code ne \"official\"}"><input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="taxId" value="${vendor.taxId}"/></c:if>
              	<c:if test="${vendorPhase.code eq \"official\"}"><input type="hidden" name="taxId" value="${vendor.taxId}"/>${vendor.taxId}</c:if>
              </div>
              <div class="col-md-4">银行名称:
              	<c:if test="${vendorPhase.code ne \"official\"}"><input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="bankName" value="${vendor.bankName}"/></c:if>
              	<c:if test="${vendorPhase.code eq \"official\"}"><input type="hidden" name="bankName" value="${vendor.bankName}"/>${vendor.bankName}</c:if>
              </div>
              <div class="col-md-4">银行帐号:
              	<c:if test="${vendorPhase.code ne \"official\"}"><input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="bankCard" value="${vendor.bankCard}"/></c:if>
              	<c:if test="${vendorPhase.code eq \"official\"}"><input type="hidden" name="bankCard" value="${vendor.bankCard}"/>${vendor.bankCard}</c:if>
              </div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">注册地址:
              	<c:if test="${vendorPhase.code ne \"official\"}"><input class="easyui-textbox" data-options="required:true"  style="width:140px;" name="address" value="${vendor.address}"/></c:if>
              	<c:if test="${vendorPhase.code eq \"official\"}"><input type="hidden" name="address" value="${vendor.address}"/>${vendor.address}</c:if>
              </div>
              <div class="col-md-4">主供事业部:
              <select id="testsetset" class="easyui-combobox" style="width:140px;" data-options="multiple:true,editable:false,required:true" value="${vendor.mainBU}">
              <c:forEach items="${vendorBUs}" var="vendorBUs">
              		<option value="${vendorBUs.codes }"
              		<c:if test="${vendorBUs.codes==vendor.mainBU}">selected="selected"</c:if>
              		>${vendorBUs.name }</option>
              </c:forEach>
                
              </select>
              <input type="hidden" name="mainBU" id="hiddenMainBU">
              </div>
              <div class="col-md-4">供应商类别:
                <select id="vendorType" class="easyui-combobox" data-options="editable:false,required:true" style="width:140px;" name="vendorType" >
                  <option value="1">代理供应商</option>
                  <option value="2">生产供应商</option>
                  <option value="3">服务供应商</option>
                  <option value="4">经销供应商</option>
                </select>
              </div>
            </div>
          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>供应商所有权信息</small></span></legend>
            <div class="row row-form">
              <div class="col-md-4">注册资本(万元):<input class="easyui-textbox" required="true" style="width:140px;" name="regCapital" value="${vendor.regCapital}"/></div>
              <div class="col-md-4">资本总额(万元):<input class="easyui-textbox" required="true" style="width:140px;" name="totalCapital" value="${vendor.totalCapital}"/></div>
              <div class="col-md-4">流动资金总额(万元):<input class="easyui-textbox" required="true" style="width:140px;" name="workingCapital" value="${vendor.workingCapital}"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">占地面积(万平方米):<input class="easyui-textbox" required="true" style="width:140px;" name="floorArea" value="${vendor.floorArea}"/></div>
            </div>
          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>供应商所有权信息</small></span></legend>
            <div class="row row-form">
              <div class="col-md-4">所在省:<input id="base-province" class="easyui-combobox"
               data-options="editable:false,valueField:'value',textField:'text',
               url : '${ctx}/public/common/areaselect/getAreaSelectByLevel/2',
               onSelect : function(r){AreaSelector.onSelect(r,'base-province','base-city');}
               "
               id="combobox_province"
               style="width:140px;" name="province" value="${vendor.province}"/></div>
              <div class="col-md-4">所在市:<input id="base-city" class="easyui-combobox" 
               data-options="editable:false,valueField:'value',textField:'text',
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
          <legend class="text-left"><span class="label label-primary"><small>供应商所有权信息</small></span></legend>
            <div class="row row-form">
              <div class="col-md-4">总经理:<input class="easyui-textbox" required="true" style="width:140px;" name="col8" value="${vendor.col8}"/></div>
              <div class="col-md-4">联系电话:<input class="easyui-textbox" style="width:140px;" id="num1" name="col9" value="${vendor.col9}" data-options="required:true,validType:'isPhone'"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">销售负责人:<input class="easyui-textbox" required="true" style="width:140px;" name="col10" value="${vendor.col10}"/></div>
              <div class="col-md-4">联系电话:<input class="easyui-textbox" style="width:140px;" id="num2" name="col11" value="${vendor.col11}" data-options="required:true,validType:'isPhone'"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">质量负责人:<input class="easyui-textbox" required="true" style="width:140px;" name="col12" value="${vendor.col12}"/></div>
              <div class="col-md-4">联系电话:<input class="easyui-textbox" style="width:140px;" id="num3" name="col13" value="${vendor.col13}" data-options="required:true,validType:'isPhone'"/></div>
            </div>
            <div class="row row-form">
              <div class="col-md-4">研发负责人:<input class="easyui-textbox" required="true" style="width:140px;" name="col14" value="${vendor.col14}"/></div>
              <div class="col-md-4">联系电话:<input class="easyui-textbox" style="width:140px;" id="num4" name="col15" value="${vendor.col15}" data-options="required:true,validType:'isPhone'"/></div>
            </div>
          </fieldset>
          <fieldset>
          <legend class="text-left"><span class="label label-primary"><small>供应商所有权信息</small></span></legend>
            <div class="row row-form">
              <div class="col-md-4">进入神农客公司年份:<input class="easyui-datebox" data-options="editable:false" style="width:140px;" id="startTime" name="col16" value="${vendor.col16}"/></div>
              <div class="col-md-4">离开神农客公司年份:<input class="easyui-datebox" data-options="editable:false" style="width:140px;" id="endTime" name="col17" value="${vendor.col17}"/></div>
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
var mainBUStr = '${vendor.mainBU}';
Survey.initComponent('base');
$(function(){
	$('#testsetset').combobox('setValues',mainBUStr.split(','));
	$('#hiddenMainBU').val(mainBUStr);
	$('#vendorType').combobox('setValues',${vendor.vendorType}+'');
});
</script>

<script type="text/javascript">
$.extend($.fn.validatebox.defaults.rules, {
    isPhone: {
        validator: function (value, param) {//param为默认值
        	debugger;
        	//验证手机号
        	 var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
        	 var temp1 = reg.test(value);
        	 //验证固定号码
        	 var re = /^0\d{2,3}-?\d{7,8}$/;
        	 var temp2 = re.test(value)

        	 if(temp1 || temp2){
        		 return true;
        	 }else{
        		 return false;
        	 }
        },
        message: '请输入正确的联系电话'
    },
    isUrl: {
        validator: function (value, param) {//param为默认值
        	var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
        	var objExp=new RegExp(Expression); 
                if (objExp.test(value)){
                    return true;
                }else{
                    return false;
                }
        },
        message: '请输入正确的网址'
    }
});
</script>
</body>
</html>
